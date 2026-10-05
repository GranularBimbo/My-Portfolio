import dearpygui.dearpygui as dpg
import entities as ent
import world_gen
import math
import encounters
import random
import world

dungeon_progress_visible = True
global_log_visible = True
inv_visible = True
travel_visible = True
party_manager_items = {}
party_tags = {}
char_tags = {}
chars = []
last_clicked_item_tag = ""
new_char = ent.Character(id=-1, name="Character", roll_stats_on_creation=True)
char_creator_showing = False
new_char.init_character()

# ==========================================
# Helpers
# ==========================================

def find_chars_in_party(party: str):
    char_list = []
    
    for c in chars:
        if c.party == party:
            char_list.append(c)

    return char_list

def touching_party_manager_item():
    for k in party_manager_items.keys():
        try:
            if dpg.is_item_hovered(party_manager_items[k]["tag"]):
                return (True, party_manager_items[k]["tag"])
        except Exception as e:
            print(f"k: {k}")
            print(f"Error checking if item is hovered: {e}")
            continue
    
    return (False, None)


def name_matches(name: str):
    for k in party_manager_items.keys():
        if name == party_manager_items[k]["tag"]:
            return True
    
    return False
                

# ==========================================
# Parties and Characters
# ==========================================

def delete_character():
    # If multiple copies exist, decrease number by 1 but delete the node and 
    # set original_exists to False. Otherwise remove from list and delete node.
    # If the node is already a copy, delete the node and decrease count for the original.
    pass

def open_combat_options(sender, app_data, user_data):
    item = last_clicked_item_tag

    party_name = item if item != "" else ""
    party_list = world.parties.get(item).chars if item != "" else []
    party_obj = world.parties.get(party_name) if item != "" else None

    if party_obj is not None:
        height = (24*(len(party_list)+1)) + 400
    else:
        height = 424

    if party_obj is None:
        window_label = f"[!] {party_list[0].name} - Combat Options"
        party_list[0].party = party_name
        window_tag = f"[!] {party_name} - Combat Options"
    else:
        window_label = f"[!] {party_name} - Combat Options"
        window_tag = f"[!] {party_name} - Combat Options"

    with dpg.window(tag=window_tag, label=window_label, width=1000, height=height, on_close=destroy_window):         
        with dpg.group(horizontal=True):
            dpg.add_text("Party: ")
            dpg.add_spacer(width=13)

            with dpg.table(tag=f"{party_name}_combat_options_table", borders_outerV=True, borders_outerH=True, borders_innerV=True, borders_innerH=True):
                dpg.add_table_column(label="Name")
                dpg.add_table_column(label="Class")
                dpg.add_table_column(label="Level")
                dpg.add_table_column(label="AC")
                dpg.add_table_column(label="HP")
                dpg.add_table_column(label="Row")

                for c in party_list:
                    with dpg.table_row():
                        dpg.add_text(c.name)
                        dpg.add_text(c.Class)
                        dpg.add_text(c.level)
                        dpg.add_text(c.AC)
                        dpg.add_text(c.HP)
                        dpg.add_combo(default_value=c.combat_row, items=["Front", "Back"], width=-1, tag=f"row {c.name}_id={c.id}")

        dpg.add_spacer(height=345)
        with dpg.group(horizontal=True):
            dpg.add_button(tag=f"{party_name}_combat_options_ok", label="Okay")
            dpg.add_spacer(width=890)
            dpg.add_button(tag=f"{party_name}_combat_options_close", label="Close")
    
                


def close_char_creator(sender, app_data, user_data):
    global new_char
    global char_creator_showing

    char_creator_showing = False
    dpg.hide_item("char_creator_window")
    dpg.set_value("char_class_combo", value="Fighter")
    new_char.Class = "Fighter"
    new_char.init_character()


def open_char_skills(sender, app_data, user_data):
    global new_char

    skill_tag = user_data

    if dpg.does_item_exist(skill_tag):
        dpg.delete_item(skill_tag, children_only=True)

    for l in new_char.languages:
        dpg.add_text(l, parent=skill_tag)

    for k in new_char.skills.keys():
        dpg.add_text(new_char.skills[k]["name"], parent=skill_tag)


def open_char_creator(sender, app_data, user_data):
    global new_char
    global char_creator_showing

    prog_tag, stat_tag, window_tag, skill_tag, skill_group_tag, stat_tag_hor = user_data

    if not char_creator_showing:
        char_creator_showing = True
        dpg.show_item("char_creator_window")
        new_char = ent.Character(id=-1, name="Character", roll_stats_on_creation=True)
        new_char.init_character()
        build_char_tables(sender, app_data, user_data)
        #print(f"skill_tag: {skill_tag}")
        open_char_skills(sender, app_data, skill_group_tag)
        dpg.configure_item("char_name_input", default_value="Character")
        dpg.configure_item("aln_choice", default_value="Law")
        dpg.focus_item("char_name_input")


def build_char_tables(sender, app_data, user_data):
    global new_char

    prog_tag, stat_tag, window_tag, skill_tag, skill_group_tag, stat_tag_hor = user_data
    build_char_prog_table(prog_tag, window_tag)

    if new_char.Class == "Elf" or new_char.Class == "Magic-User" or new_char.Class == "Cleric":
        dpg.show_item("char_spell_combo")
    else:
        dpg.hide_item("char_spell_combo")

    if new_char.Class == "Thief":
        build_char_stat_table_horizontal(stat_tag, stat_tag_hor)
    else:
        build_char_stat_table(stat_tag)
        dpg.hide_item(stat_tag_hor)
    
    build_char_skill_table(skill_tag, window_tag)
    open_char_skills(sender, app_data, skill_group_tag)


def build_char_skill_table(table_tag, window_tag="char_creator_window"):
    global new_char

    if dpg.does_item_exist(table_tag):
        dpg.delete_item(table_tag, children_only=True)

    dpg.add_table_column(label="Lvl", parent=table_tag)
    dpg.add_table_column(label="CS", parent=table_tag)
    dpg.add_table_column(label="TR", parent=table_tag)
    dpg.add_table_column(label="HN", parent=table_tag)
    dpg.add_table_column(label="HS", parent=table_tag)
    dpg.add_table_column(label="MS", parent=table_tag)
    dpg.add_table_column(label="OL", parent=table_tag)
    dpg.add_table_column(label="PP", parent=table_tag)

    for level in new_char.skill_table:
        row_tag = f"skill_row {level['Level']}"
        dpg.add_table_row(parent=table_tag, tag=row_tag)

        dpg.add_text(str(level["Level"]), parent=row_tag)
        dpg.add_text(str(level["CS"]), parent=row_tag)
        dpg.add_text(str(level["TR"]), parent=row_tag)
        dpg.add_text(f"{level["HN"][0]}-{level["HN"][1]}", parent=row_tag)
        dpg.add_text(str(level["HS"]), parent=row_tag)
        dpg.add_text(str(level["MS"]), parent=row_tag)
        dpg.add_text(str(level["OL"]), parent=row_tag)
        dpg.add_text(str(level["PP"]), parent=row_tag)

    if new_char.Class == "Thief":
        dpg.show_item(table_tag)
        dpg.configure_item(window_tag, width=700, height=670, pos=(430, 120))
        dpg.configure_item("char_creator_cancel", pos=(dpg.get_item_width("char_creator_window")-60, dpg.get_item_height("char_creator_window")-30))
        dpg.configure_item("char_creator_create_button", pos=(10, dpg.get_item_height("char_creator_window")-30))
    else:
        dpg.hide_item(table_tag)


def build_char_prog_table(table_tag, window_tag="char_creator_window"):
    global new_char

    new_char.Class = dpg.get_value("char_class_combo")
    new_char.init_character()

    if dpg.does_item_exist(table_tag):
        dpg.delete_item(table_tag, children_only=True)

    dpg.add_table_column(label="Level", parent=table_tag)
    dpg.add_table_column(label="XP", parent=table_tag)
    dpg.add_table_column(label="HD", parent=table_tag)
    dpg.add_table_column(label="THAC0", parent=table_tag)
    dpg.add_table_column(label="D", parent=table_tag)
    dpg.add_table_column(label="W", parent=table_tag)
    dpg.add_table_column(label="P", parent=table_tag)
    dpg.add_table_column(label="B", parent=table_tag)
    dpg.add_table_column(label="S", parent=table_tag)

    if new_char.Class == "Elf":
        dpg.add_table_column(label="1", parent=table_tag)
        dpg.add_table_column(label="2", parent=table_tag)
        dpg.add_table_column(label="3", parent=table_tag)
        dpg.add_table_column(label="4", parent=table_tag)
        dpg.add_table_column(label="5", parent=table_tag)
        dpg.configure_item(window_tag, width=630, height=470, pos=(450, 200))
        dpg.configure_item("char_creator_cancel", pos=(dpg.get_item_width("char_creator_window")-60, dpg.get_item_height("char_creator_window")-30))
        dpg.configure_item("char_creator_create_button", pos=(10, dpg.get_item_height("char_creator_window")-30))
    
    elif new_char.Class == "Fighter":
        dpg.configure_item(window_tag, width=570, height=470, pos=(500, 200))
        dpg.configure_item("char_creator_cancel", pos=(dpg.get_item_width("char_creator_window")-60, dpg.get_item_height("char_creator_window")-30))
        dpg.configure_item("char_creator_create_button", pos=(10, dpg.get_item_height("char_creator_window")-30))
    
    else:
        dpg.configure_item(window_tag, width=550, height=470, pos=(530, 200))
        dpg.configure_item("char_creator_cancel", pos=(dpg.get_item_width("char_creator_window")-60, dpg.get_item_height("char_creator_window")-30))
        dpg.configure_item("char_creator_create_button", pos=(10, dpg.get_item_height("char_creator_window")-30))

    bonus_hp = 0
    last_xp = 0

    for level in new_char.prog_table:
        row_tag = f"level_row {level['Level']}"
        dpg.add_table_row(parent=table_tag, tag=row_tag)

        dpg.add_text(str(level["Level"]), parent=row_tag)
        dpg.add_text(str(last_xp), parent=row_tag)
        last_xp = level["xp_needed"]

        if (level["Level"] > 9 and (new_char.Class == "Fighter" or new_char.Class == "Thief" or new_char.Class == "Elf")):
            bonus_hp += 2
            dpg.add_text(f"{level["HD"][0]}d{level["HD"][1]}+{bonus_hp}", parent=row_tag)
        
        elif (level["Level"] > 9 and new_char.Class == "Dwarf"):
            bonus_hp += 3
            dpg.add_text(f"{level["HD"][0]}d{level["HD"][1]}+{bonus_hp}", parent=row_tag)

        else:
            dpg.add_text(f"{level["HD"][0]}d{level["HD"][1]}", parent=row_tag)

        dpg.add_text(str(level["THAC0"]), parent=row_tag)
        dpg.add_text(str(level["death_save"]), parent=row_tag)
        dpg.add_text(str(level["wand_save"]), parent=row_tag)
        dpg.add_text(str(level["para_save"]), parent=row_tag)
        dpg.add_text(str(level["breath_save"]), parent=row_tag)
        dpg.add_text(str(level["spell_save"]), parent=row_tag)

        if new_char.Class == "Elf":
            dpg.add_text(str(level["lv1_spells"]), parent=row_tag)
            dpg.add_text(str(level["lv2_spells"]), parent=row_tag)
            dpg.add_text(str(level["lv3_spells"]), parent=row_tag)
            dpg.add_text(str(level["lv4_spells"]), parent=row_tag)
            dpg.add_text(str(level["lv5_spells"]), parent=row_tag)


def build_char_stat_table(table_tag):
    if dpg.does_item_exist(table_tag):
        dpg.delete_item(table_tag, children_only=True)

    dpg.show_item(table_tag)

    dpg.add_table_column(label="Atb", parent=table_tag)
    dpg.add_table_column(label="Val", parent=table_tag)

    with dpg.table_row(parent=table_tag):
        dpg.add_text("STR")
        dpg.add_text(new_char.Str)

    with dpg.table_row(parent=table_tag):
        dpg.add_text("DEX")
        dpg.add_text(new_char.Dex)

    with dpg.table_row(parent=table_tag):
        dpg.add_text("CON")
        dpg.add_text(new_char.Con)

    with dpg.table_row(parent=table_tag):
        dpg.add_text("INT")
        dpg.add_text(new_char.Int)
    
    with dpg.table_row(parent=table_tag):
        dpg.add_text("WIS")
        dpg.add_text(new_char.Wis)

    with dpg.table_row(parent=table_tag):
        dpg.add_text("CHA")
        dpg.add_text(new_char.Cha)


def build_char_stat_table_horizontal(vert_table_tag, target_table_tag):
    if dpg.does_item_exist(target_table_tag):
        dpg.delete_item(target_table_tag, children_only=True)

    dpg.show_item(target_table_tag)
    dpg.hide_item(vert_table_tag)

    dpg.add_table_column(label="STR", parent=target_table_tag)
    dpg.add_table_column(label="DEX", parent=target_table_tag)
    dpg.add_table_column(label="CON", parent=target_table_tag)
    dpg.add_table_column(label="INT", parent=target_table_tag)
    dpg.add_table_column(label="WIS", parent=target_table_tag)
    dpg.add_table_column(label="CHA", parent=target_table_tag)

    with dpg.table_row(parent=target_table_tag):
        dpg.add_text(new_char.Str)
        dpg.add_text(new_char.Dex)
        dpg.add_text(new_char.Con)
        dpg.add_text(new_char.Int)
        dpg.add_text(new_char.Wis)
        dpg.add_text(new_char.Cha)


def delete_party(sender, app_data, user_data):
    # If multiple copies exist, decrease number by 1 but delete the node and 
    # set original_exists to False. Otherwise remove from list and delete node.
    # If the node is already a copy, delete the node and decrease count for the original.

    item = last_clicked_item_tag
    party_tag = item if item != "" else ""

    if name_matches(party_tag):
        if not party_manager_items[party_tag]["is_copy"]:
            if party_manager_items[party_tag]["num"] > 1:
                party_manager_items[party_tag]["num"] -= 1
                party_manager_items[party_tag]["original_exists"] = False
            else:
                del party_manager_items[party_tag]
                del party_tags[party_tag]
        else:
            og_tag = party_manager_items[party_tag]["og_name"]
            if party_manager_items[og_tag]["num"] > 1:
                party_manager_items[og_tag]["num"] -= 1

                if party_manager_items[og_tag]["num"] == 1:
                    party_manager_items[og_tag]["delete_order"] = []
                else:
                    party_manager_items[og_tag]["delete_order"].append((party_tag, party_manager_items[party_tag]["copy_num"]))
            else:
                del party_manager_items[og_tag]
                del party_tags[og_tag]

        dpg.delete_item(party_tag)
        del world.parties[party_tag]
    else:
        print(f"Party '{party_tag}' does not exist.")


def start_character_creator():
    pass


def create_character(sender, app_data, user_data):
    # Default values for the party properties
    is_copy = False
    num = 1
    original_exists = True
    char_name = dpg.get_value("char_name_input")
    og_name = char_name
    copy_num = 0
    delete_order = []

    # Default name if the user doesn't enter one
    if char_name == "":
        char_name = "Character"

    # If the name already exists, add a number to the end of it and set is_copy to True.
    if name_matches(char_name) and party_manager_items[char_name]["original_exists"]:
        party_manager_items[char_name]["num"] += 1
        is_copy = True
        num = party_manager_items[char_name]["num"]
        original_exists = party_manager_items[char_name]["original_exists"]

        # If copies have been deletes, sort the order of the copies and assign the name of the
        # smallest copy number to the new party.
        if len(party_manager_items[char_name]["delete_order"]) > 0:
            party_manager_items[char_name]["delete_order"].sort(key=lambda x: x[1])
            char_name, copy_num = party_manager_items[char_name]["delete_order"].pop(0)

        else:
            char_name = f"{char_name} ({party_manager_items[char_name]['num']})"
            copy_num = num

    # If the name already exists but the original doesn't exist, 
    # set original_exists to True and increase the number by 1.
    elif name_matches(char_name) and not party_manager_items[char_name]["original_exists"]:
        party_manager_items[char_name]["original_exists"] = True
        party_manager_items[char_name]["num"] += 1
        num = party_manager_items[char_name]["num"]
        original_exists = party_manager_items[char_name]["original_exists"]
        delete_order = party_manager_items[char_name]["delete_order"]

    # Update dictionaries to reflect the new party and its properties
    party_manager_items[char_name] = {"num": num, "tag": char_name, "original_exists": original_exists,
                                       "is_copy": is_copy, "og_name": og_name, "delete_order": delete_order,
                                       "copy_num": copy_num}
    char_tags[char_name] = {"num": num, "tag": char_name, "original_exists": original_exists,
                              "is_copy": is_copy, "og_name": og_name, "delete_order": delete_order,
                              "copy_num": copy_num}
    
    char = ent.Character(name=char_name, Str=new_char.Str, Dex=new_char.Dex, Con=new_char.Con, Int=new_char.Int,
                              Wis=new_char.Wis, Cha=new_char.Wis, Class=new_char.Class)

    #print(f"new character created: {ent.all_creatures.get(char.id).name}_id={char.id}")
    print(f" all_creatures new addition: {ent.all_creatures[char.id].name} (ID: {char.id})")

    # Create the party node and hide the namer window
    dpg.add_selectable(parent="item_group", label=char_name, tag=f"{char.name}_id={char.id}")

    with dpg.drag_payload(parent=f"{char.name}_id={char.id}", drag_data=f"{char.name}_id={char.id}", payload_type="CHARACTER"):
        dpg.add_text(f"{char_name}")

    #dpg.add_drag_payload(parent="party_manager", label=char_name, tag=char_name, payload_type="CHARACTER")
    dpg.hide_item("namer_window")
    dpg.set_value("namer_text", value="")
    dpg.set_value("namer_ok", False)
    dpg.set_value("namer_cancel", False)
    
    char.Class = new_char.Class
    if char.Class == "Elf" or char.Class == "Magic User" or char.Class == "Cleric":
        new_spell = dpg.get_value("char_spell_combo")
        char.spells[new_spell] = 1

    char.init_character()
    char.party = f"{char.name}_id={char.id}"
    chars.append(char)

    # Creates a solo party for the character and adds them to it
    new_party = world.parties.get(char.party)

    if new_party is None:
        new_party = ent.Party(name=char.party)
        new_party.add_member(char_obj=char)
        world.parties[char.party] = new_party

    close_char_creator(sender, app_data, user_data)

    test_encounter_data = encounters.ENCOUNTER_DATA["Locked Chest"]
    test_encounter = encounters.Encounter(party=f"{char.name}_id={char.id}",
                                          name=test_encounter_data["name"],
                                          text=test_encounter_data["text"],
                                          layout_callback=test_encounter_data["layout"])


def drop_on_party_cb(sender, app_data, user_data):
    """
    Called when a character is dropped onto a party folder tree node.
    app_data: contains the unique ID (tag) of the character being dragged.
    sender: contains the unique ID (tag) of the party node it was dropped onto.
    """
    character_tag = app_data
    target_party_tag = sender
    char_id = -1

    try:
        _, id_str = character_tag.split("_id=")
        char_id = int(id_str)
    except (ValueError, IndexError):
        print(f"Error: Could not parse ID from tag '{character_tag}'")
        return
    
    #print(f"char_id: {char_id}")
    char_obj = ent.all_creatures.get(char_id)

    #print(f"char_obj: {char_obj}")

    if char_id != -1 and char_obj is not None:
        #world.parties[target_party_tag]["chars"].append(char_obj)
        world.parties.get(target_party_tag).add_member(char_obj)

        if world.parties.get(char_obj.party) is not None and char_obj.name in char_obj.party:
            del world.parties[char_obj.party]
        
        char_obj.party = target_party_tag

    #print(f"parties[{target_party_tag}]['chars']: {parties[target_party_tag]["chars"]}")
    
    # Reparent the character item in the DPG UI tree structure
    dpg.move_item(character_tag, parent=target_party_tag)

    #encounters.start_combat(monster_name="Ghoul", char_id=char_id, no_party=True)

    #if len(parties[target_party_tag]["chars"]) > 1:
    if len(world.parties.get(target_party_tag).chars) > 1:
        encounters.start_combat(monster_name="Ghoul", party_name=target_party_tag,
                                party_list=world.parties.get(target_party_tag).chars)
    #encounters.start_combat(monster_name="Ghoul", char_id=char_id, no_party=True)


def drop_on_background_cb(sender, app_data, user_data):
    """
    Called when a character is dropped onto the background of the party window.
    app_data: contains the unique ID (tag) of the character being dragged.
    sender: contains the unique ID (tag) of the party window it was dropped onto.
    """
    character_tag = app_data

    try:
        _, id_str = character_tag.split("_id=")
        char_id = int(id_str)

        char = ent.all_creatures[char_id]

        party = world.parties.get(char.party)
        party.remove_member(char)

        char.party = f"{char.name}_id={char.id}"

        if world.parties.get(char.party) is None:
            new_party = ent.Party(name=char.party)
            world.parties[char.party] = new_party
            world.parties.get(char.party).add_member(char_obj=char)
        
    except (ValueError, IndexError):
        print(f"Error: Could not parse ID from tag '{character_tag}'")
    
    # Reparent the character item in the DPG UI tree structure
    dpg.move_item(character_tag, parent="item_group")

def create_party(sender, app_data, user_data):
    # Default values for the party properties
    is_copy = False
    num = 1
    original_exists = True
    party_name = dpg.get_value("namer_text")
    og_name = party_name
    copy_num = 0
    delete_order = []

    # Default name if the user doesn't enter one
    if party_name == "":
        party_name = "Party"

    # If the name already exists, add a number to the end of it and set is_copy to True.
    if name_matches(party_name) and party_manager_items[party_name]["original_exists"]:
        party_manager_items[party_name]["num"] += 1
        is_copy = True
        num = party_manager_items[party_name]["num"]
        original_exists = party_manager_items[party_name]["original_exists"]

        # If copies have been deletes, sort the order of the copies and assign the name of the
        # smallest copy number to the new party.
        if len(party_manager_items[party_name]["delete_order"]) > 0:
            party_manager_items[party_name]["delete_order"].sort(key=lambda x: x[1])
            party_name, copy_num = party_manager_items[party_name]["delete_order"].pop(0)

        else:
            party_name = f"{party_name} ({party_manager_items[party_name]['num']})"
            copy_num = num

    # If the name already exists but the original doesn't exist, 
    # set original_exists to True and increase the number by 1.
    elif name_matches(party_name) and not party_manager_items[party_name]["original_exists"]:
        party_manager_items[party_name]["original_exists"] = True
        party_manager_items[party_name]["num"] += 1
        num = party_manager_items[party_name]["num"]
        original_exists = party_manager_items[party_name]["original_exists"]
        delete_order = party_manager_items[party_name]["delete_order"]

    # Create the party node and hide the namer window
    dpg.add_tree_node(parent="item_group", label=party_name, tag=party_name, drop_callback=drop_on_party_cb,
                      payload_type="CHARACTER")
    
    dpg.set_item_drop_callback(party_name, drop_on_party_cb)

    dpg.hide_item("namer_window")
    dpg.set_value("namer_text", value="")
    dpg.set_value("namer_ok", False)
    dpg.set_value("namer_cancel", False)

    # Update dictionaries to reflect the new party and its properties
    party_manager_items[party_name] = {"num": num, "tag": party_name, "original_exists": original_exists,
                                       "is_copy": is_copy, "og_name": og_name, "delete_order": delete_order,
                                       "copy_num": copy_num}
    party_tags[party_name] = {"num": num, "tag": party_name, "original_exists": original_exists,
                              "is_copy": is_copy, "og_name": og_name, "delete_order": delete_order,
                              "copy_num": copy_num}
    
    #parties[party_name] = {"name": party_name, "chars": [], "in_dungeon": False, "location": None}
    
    new_party = ent.Party(name=party_name)
    world.parties[party_name] = new_party


def name_party(sender, app_data, user_data):
    dpg.show_item("namer_window")
    dpg.focus_item("namer_text")


def close_namer(sender, app_data, user_data):
    dpg.hide_item("namer_window")
    dpg.set_value("namer_text", value="")
    dpg.set_value("namer_ok", False)
    dpg.set_value("namer_cancel", False)


def open_party_popup(sender, app_data, user_data):
    global last_clicked_item_tag

    last_clicked_item_tag = ""
    touching, item = touching_party_manager_item()

    # Only show the popup if the child window is hovered
    if dpg.is_item_hovered("party_manager") and not touching:
        # Position popup at mouse cursor
        mouse_pos = dpg.get_mouse_pos(local=False)
        dpg.configure_item("context_menu", show=True, pos=mouse_pos)

    elif touching:
        if item in party_tags:
            dpg.show_item("party_selected_popup")
            mouse_pos = dpg.get_mouse_pos(local=False)
            dpg.configure_item("party_selected_popup", show=True, pos=mouse_pos)
            last_clicked_item_tag = item
        elif item in chars:
            pass

# ==========================================
# UI Callbacks
# ==========================================

def destroy_window(sender, app_data, user_data):
    dpg.delete_item(sender)


def travel_resize(sender, app_data, user_data):
    win_width = dpg.get_item_width("travel_window")
    win_height = dpg.get_item_height("travel_window")

    map_width = math.ceil(win_width*0.65)
    map_height = math.ceil(win_height*0.65)
    dpg.configure_item("map_canvas", width=map_width, height=map_height)
    dpg.configure_item("map_drawlist", width=math.ceil(map_width*0.9), height=math.ceil(map_height*0.9))
    dpg.configure_item("map_legend", width=win_width*0.3)
    
    can_x, can_y = dpg.get_item_pos("map_canvas")
    dpg.configure_item("travel_options_text", pos=(can_x, can_y+win_height*0.65))
    dpg.configure_item("travel_party", pos=(can_x, can_y+win_height*0.7))
    
    # Safe bounds padding
    usable_width = math.ceil(map_width*0.9)
    usable_height = math.ceil(map_height*0.9)
    
    if usable_width <= 0 or usable_height <= 0:
        return

    # Calculate bounded radius limits
    max_radius_by_width = usable_width / (world_gen.MAP_COLS * math.sqrt(3) + 0.5)
    max_radius_by_height = usable_height / ((world_gen.MAP_ROWS - 1) * 1.5 + 2)
    
    current_hex_radius = min(max_radius_by_width, max_radius_by_height)+0.5
    world_gen.HEX_RADIUS = current_hex_radius
    
    # Keep the drawlist stretched to fit the window bounds
    dpg.configure_item("map_drawlist", width=win_width, height=win_height)
    world_gen.draw_hex_map()

def show_dungeon_progress(sender, app_data, user_data):
    global dungeon_progress_visible
    if dungeon_progress_visible:
        dpg.hide_item("dungeon_progress")
        dpg.configure_item("dungeon_progress_toggle", label="Dungeon Progress")
        dungeon_progress_visible = False
    else:
        dpg.show_item("dungeon_progress")
        dpg.configure_item("dungeon_progress_toggle", label="Dungeon Progress *")
        dungeon_progress_visible = True


def show_global_log(sender, app_data, user_data):
    global global_log_visible
    if global_log_visible:
        dpg.hide_item("global_log")
        dpg.configure_item("global_log_toggle", label="Global Log")
        global_log_visible = False
    else:
        dpg.show_item("global_log")
        dpg.configure_item("global_log_toggle", label="Global Log *")
        global_log_visible = True


def show_inv(sender, app_data, user_data):
    global inv_visible
    if inv_visible:
        dpg.hide_item("inventory")
        dpg.configure_item("inv_toggle", label="Inventory")
        inv_visible = False
    else:
        dpg.show_item("inventory")
        dpg.configure_item("inv_toggle", label="Inventory *")
        inv_visible = True

def show_travel(sender, app_data, user_data):
    global travel_visible
    if travel_visible:
        dpg.hide_item("travel_window")
        dpg.configure_item("travel_toggle", label="Travel")
        travel_visible = False
    else:
        dpg.show_item("travel_window")
        dpg.configure_item("travel_toggle", label="Travel *")
        travel_visible = True


def quit_game(sender, app_data, user_data):
    dpg.stop_dearpygui()