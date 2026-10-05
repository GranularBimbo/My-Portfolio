import dearpygui.dearpygui as dpg
import random
import math
import entities
import items
import world
import image_handling

# Global variables

# Whether or not an encounter ambushed from behind the party
parties_behind = {} # party_name: behind(bool)

# Whether or not an encounter was ambushed from behind by the party
enemies_behind = {} # party_name: behind(bool)

# ==========================================
# Classes
# ==========================================

class Encounter():
    def __init__(self, party: str, layout_callback: function, 
                 name: str = "Default Encounter", text: str = "",
                 choice_callbacks: list = [], width: int = 500,
                 height: int = 400):
        self.party = party
        self.name = name
        self.text = text
        self.layout_callback = layout_callback
        self.choice_callbacks = choice_callbacks
        self.width = width
        self.height = height

        self.window_tag = f"{self.party}_encounter_window"
        
        dpg.add_window(label=f"[!] {self.party} - {self.name}", tag=self.window_tag, show=True,
                       width=self.width, height=self.height)
        self.layout_callback(self)


# ==========================================
# Encounter Callbacks
# ==========================================


def destroy_window(sender, app_data, user_data):
    dpg.delete_item(sender)


def draw_encounter_window(texture_tag, encounter_obj: Encounter):
    box_width = 483
    box_height = 220
    
    with dpg.drawlist(width=box_width, height=box_height, parent=encounter_obj.window_tag):
        dpg.draw_image(texture_tag, pmin=[150, 20], pmax=[box_width-150, box_height-20])
        dpg.draw_rectangle(pmin=[0, 0], pmax=[box_width, box_height], color=[100, 100, 100], thickness=1)

    dpg.add_spacer(height=10, parent=encounter_obj.window_tag)

    text = encounter_obj.text.replace('/p', encounter_obj.party)

    dpg.add_text(text, parent=encounter_obj.window_tag)


def locked_chest_layout(encounter_obj: Encounter):
    draw_encounter_window(texture_tag="chest_image_texture", encounter_obj=encounter_obj)

    with dpg.table(parent=encounter_obj.window_tag, header_row=False):
        dpg.add_table_column()
        dpg.add_table_column()
        dpg.add_table_column()
        dpg.add_table_column()

        with dpg.table_row():
            dpg.add_button(label="Pick Lock", tag=f"{encounter_obj.party}_pick_lock", width=-1)
            dpg.add_button(label="Force Lock", tag=f"{encounter_obj.party}_force_lock", width=-1)
            dpg.add_button(label="Take Chest", tag=f"{encounter_obj.party}_take_locked_chest", width=-1)
            dpg.add_button(label="Leave", tag=f"{encounter_obj.party}_leave_locked_chest", width=-1)


def pick_lock_callback(encounter_obj: Encounter):
    pass


# ==========================================
# Encounter Stats
# ==========================================


# /p inserts the party name
ENCOUNTER_DATA = {
    "Locked Chest": {
        "name": "Locked Chest",
        "text": "/p finds a wooden chest, it appears to be locked.",
        "layout": locked_chest_layout
    }
}


# ==========================================
# Combat
# ==========================================

def party_turn(party_list, enemy_list):

    party_obj = world.parties.get(party_list[0].party)
    
    surprised_party = party_obj.surprised
    surprised_enemy = party_obj.surprised_enemies

    for character in party_list:
            if party_obj.combat_mode == "Manual":
                target_num = int(dpg.get_value(f"target {character.name}_id={character.id}"))

                action = dpg.get_value(f"action {character.name}_id={character.id}")

                world.add_to_func_queue(func=character.combat_turn,
                                        args=[enemy_list[target_num-1], action, surprised_party, surprised_enemy])

            elif party_obj.combat_mode == "Automatic":
                pass

            character.refresh_combat_callback = refresh_ui_callback
            character.refresh_combat_args = [party_list[0].party, party_list, enemy_list]
            

def enemy_turn(party_list, enemy_list):

    party_obj = world.parties.get(party_list[0].party)
    
    surprised_party = party_obj.surprised
    surprised_enemy = party_obj.surprised_enemies

    for enemy in enemy_list:
            target_num = random.randint(0, len(party_list)-1)

            action = "Attack"

            world.add_to_func_queue(func=enemy.combat_turn,
                                    args=[party_list[target_num], action, surprised_party, surprised_enemy])

            enemy.refresh_combat_callback = refresh_ui_callback
            enemy.refresh_combat_args = [party_list[0].party, party_list, enemy_list]


def shared_turn(party_list, enemy_list, automatic: bool = False):

    party_obj = world.parties.get(party_list[0].party)

    surprised_party = party_obj.surprised
    surprised_enemy = party_obj.surprised_enemies

    total = party_list.copy()
    for enemy in enemy_list:
        total.append(enemy)

    total = random.shuffle(total)

    for c in total:
        if c.type == "Character":
            if party_obj.combat_mode == "Manual":
                target_num = int(dpg.get_value(f"target {c.name}_id={c.id}"))

                action = dpg.get_value(f"action {c.name}_id={c.id}")

                world.add_to_func_queue(func=c.combat_turn, 
                                        args=[enemy_list[target_num-1], action, surprised_party, surprised_enemy])

                c.refresh_combat_callback = refresh_ui_callback
                c.refresh_combat_args = [party_list[0].party, party_list, enemy_list]
            
            elif party_obj.combat_mode == "Automatic":
                pass

        else:
            pass


def execute_combat_round(sender, app_data, user_data):
    party_list, enemy_list, party_name, no_party = user_data

    window_tag = f"[!] {party_name} - Combat Encounter"

    world.console_log("===========")
    world.console_log(f" Round {world.round_nums[window_tag]}")
    world.console_log("===========\n")

    world.custom_log(log_tag=f"{party_name}_combat_log", message="==========", not_time_stamped=True)
    world.custom_log(log_tag=f"{party_name}_combat_log", message=f" Round {world.round_nums[window_tag]}",
                     not_time_stamped=True)
    world.custom_log(log_tag=f"{party_name}_combat_log", message="==========\n", not_time_stamped=True)

    party_initiative = party_list[0].roll_dice((1,6))
    enemy_initiative = enemy_list[0].roll_dice((1,6))

    world.console_log(f"Player Initiative Roll: {party_initiative}")
    world.console_log(f"Enemy Initiative Roll: {enemy_initiative}\n")

    world.custom_log(log_tag=f"{party_name}_combat_log", message=f"Player Initiative Roll: {party_initiative}")
    world.custom_log(log_tag=f"{party_name}_combat_log", message=f"Enemy Initiative Roll: {enemy_initiative}\n")

    if party_initiative > enemy_initiative:
        world.console_log("[!] Player Goes First!\n")
        world.custom_log(log_tag=f"{party_name}_combat_log", message="[!] Player Goes First!\n")

        world.time_flow = "combat"
        party_turn(party_list=party_list, enemy_list=enemy_list)
        enemy_turn(party_list=party_list, enemy_list=enemy_list)
        #world.time_flow = "paused"

    elif enemy_initiative > party_initiative:
        world.console_log("[!] Enemy Goes First!\n")
        world.custom_log(log_tag=f"{party_name}_combat_log", message="[!] Enemy Goes First!\n")

        world.time_flow = "combat"
        enemy_turn(party_list=party_list, enemy_list=enemy_list)
        party_turn(party_list=party_list, enemy_list=enemy_list)
        #world.time_flow = "paused"

    else:
        idx = 0
        for character in party_list:
            idx += 1

    world.round_nums[window_tag] += 1

    # build_party_combat_table(party_name=party_name, party_list=party_list, enemy_list=enemy_list,
    #                          no_party=no_party)
    # build_monster_combat_table(party_name=party_name, party_list=party_list, enemy_list=enemy_list)


def start_combat(monster_name: str = "Ghoul", party_name: str = "", party_list: list = [], char_id: int = -1,
                 no_party: bool = False):
    global parties_behind
    global enemies_behind

    data = entities.BESTIARY_DATA[monster_name]
    num_monsters = roll_dice(data["number_appearing"])
    enemy_list = []
    world.time_flow = "paused"
    world.in_combat_time = True

    if char_id != -1:
        character = entities.all_creatures.get(char_id)
        party_list = [character]
        party_name = f"{character.name}_id={character.id}"
        
    for i in range(num_monsters):
         enemy_list.append(entities.spawn_monster(monster_name))
         enemy_list[i].party = party_name

    surprise = surprise_roll(party_name)
    if surprise == "party":
        parties_behind[party_name] = True
        enemies_behind[party_name] = False
    elif surprise == "enemy":
        enemies_behind[party_name] = True
        parties_behind[party_name] = False
    
    open_combat_window(party_name=party_name, 
                                party_list=party_list, 
                                enemy_list=enemy_list,
                                no_party=no_party)


def surprise_roll(party_name: str):
    party_roll = roll_dice((1,6))
    enemy_roll = roll_dice((1,6))

    if party_roll == 1 and enemy_roll != 1:
        world.parties.get(party_name).surprised = True
        world.parties.get(party_name).surprised_enemies = False
        return "party"
    elif enemy_roll == 1 and party_roll != 1:
        world.parties.get(party_name).surprised = False
        world.parties.get(party_name).surprised_enemies = True
        return "enemy"
    elif party_roll == 1 and enemy_roll == 1:
        world.parties.get(party_name).surprised = True
        world.parties.get(party_name).surprised_enemies = True
        return "both"
    else:
        world.parties.get(party_name).surprised = False
        world.parties.get(party_name).surprised_enemies = False
        return "neither"


def get_surprised(party_name: str):
    global parties_behind
    global enemies_behind

    if parties_behind.get(party_name) is not None and parties_behind.get(party_name):
        return "party"
    elif enemies_behind.get(party_name) is not None and enemies_behind.get(party_name):
        return "enemy"
    else:
        return "none"


def build_party_combat_table(party_name, party_list, enemy_list, no_party: bool = False):

    if dpg.does_item_exist(f"{party_name}_combat_table"):
        dpg.delete_item(f"{party_name}_combat_table", children_only=True)

    dpg.add_table_column(label="[Character]", parent=f"{party_name}_combat_table")
    dpg.add_table_column(label="[HP]", parent=f"{party_name}_combat_table")
    dpg.add_table_column(label="[AC]", parent=f"{party_name}_combat_table")
    dpg.add_table_column(label="[THAC0]", parent=f"{party_name}_combat_table")
    dpg.add_table_column(label="[Damage]", parent=f"{party_name}_combat_table")
    dpg.add_table_column(label="[Range]", parent=f"{party_name}_combat_table")
    dpg.add_table_column(label="[Weapon]", parent=f"{party_name}_combat_table")
    dpg.add_table_column(label="[Action]", parent=f"{party_name}_combat_table")
    dpg.add_table_column(label="[Target]", parent=f"{party_name}_combat_table")

    for character in party_list:
        with dpg.table_row(parent=f"{party_name}_combat_table"):
            num, die = character.weapon.dmg_dice

            actions = ["Attack", "Dodge"]
            spells = character.spells.keys()

            if len(spells) > 0:
                for s in spells:
                    actions.append(f"Cast {s}")


            dpg.add_text(f"{character.name}")
            dpg.add_text(f"{character.HP}", tag=f"HP {character.name}_id={character.id}")
            dpg.add_text(f"{character.AC}", tag=f"AC {character.name}_id={character.id}")
            dpg.add_text(f"{character.THAC0}")
            dpg.add_text(f"{num}d{die}", tag=f"damage {character.name}_id={character.id}")
            dpg.add_text(f"{character.weapon.range}", tag=f"range {character.name}_id={character.id}")
            dpg.add_combo(default_value=f"{character.weapon.name}", width=-1, tag=f"weapon {character.name}_id={character.id}")
            dpg.add_combo(default_value="Attack", items=actions, width=-1, tag=f"action {character.name}_id={character.id}")
            
            monster_count = 0
            target_combo_items = []
            for monster in enemy_list:
                monster_count += 1
                target_combo_items.append(str(monster_count))
                
            dpg.add_combo(default_value="1", items=target_combo_items, width=-1, tag=f"target {character.name}_id={character.id}")


def build_monster_combat_table(party_name, party_list, enemy_list, first_time: bool = False):
    
     # Monster Table
    if dpg.does_item_exist(f"{party_name}_monster_table"):
        dpg.delete_item(f"{party_name}_monster_table", children_only=True)

    dpg.add_table_column(label="[ID]", parent=f"{party_name}_monster_table")
    dpg.add_table_column(label="[Name]", parent=f"{party_name}_monster_table")
    dpg.add_table_column(label="[HP]", parent=f"{party_name}_monster_table")
    
    idx = 1
    for monster in enemy_list:
        with dpg.table_row(parent=f"{party_name}_monster_table"):
            dpg.add_text(f"{idx}")
            dpg.add_text(f"{monster.name}")
            dpg.add_text(f"{monster.HP}")

        idx += 1


def open_combat_window(party_name, party_list, enemy_list, no_party: bool = False):
    height = (24*(len(enemy_list)+1)) + (24*(len(party_list)+1)) + 300

    if no_party:
        window_label = f"[!] {party_list[0].name} - Combat Encounter"
        party_list[0].party = party_name
        window_tag = f"[!] {party_name} - Combat Encounter"
    else:
        window_label = f"[!] {party_name} - Combat Encounter"
        window_tag = f"[!] {party_name} - Combat Encounter"

    world.round_nums[window_tag] = 1
    #print(f"party_name from open_combat_window: {party_name}")

    with dpg.window(tag=window_tag, label=window_label, width=1000, height=height, on_close=destroy_window):
        
        with dpg.group(horizontal=True):
            dpg.add_text("Party: ")
            dpg.add_spacer(width=13)

            dpg.add_table(tag=f"{party_name}_combat_table", borders_outerV=True, borders_outerH=True, borders_innerV=True, borders_innerH=True)

            build_party_combat_table(party_name=party_name, party_list=party_list, enemy_list=enemy_list,
                                no_party=no_party)
            
        dpg.add_spacer(height=20)

        with dpg.group(horizontal=True):
            dpg.add_text("Monsters: ")
            #dpg.add_spacer(width=7)

            dpg.add_table(tag=f"{party_name}_monster_table", borders_outerV=True, borders_outerH=True, borders_innerV=True, borders_innerH=True)
            
            build_monster_combat_table(party_name=party_name, party_list=party_list, enemy_list=enemy_list,
                                       first_time=True)

        dpg.add_spacer(height=10)

        with dpg.group(horizontal=True):
            dpg.add_spacer(width=20)
            dpg.add_button(label="[EXECUTE ROUND]", callback=execute_combat_round, user_data=(party_list, enemy_list, party_name, no_party))
            dpg.add_spacer(width=310)
            dpg.add_button(label="[FLEE]")
            dpg.add_spacer(width=330)
            dpg.add_button(label="[NEGOTIATE]")

        dpg.add_spacer(height=20)

        dpg.add_text("Combat Log")
        dpg.add_input_text(tag=f"{party_name}_combat_log", readonly=True, multiline=True, default_value="", width=980, height=150)


# ==========================================
# Helpers
# ==========================================

def refresh_ui_callback(party_name, party_list, enemy_list, no_party: bool = False):
    build_party_combat_table(party_name=party_name, party_list=party_list, enemy_list=enemy_list, no_party=no_party)
    build_monster_combat_table(party_name=party_name, party_list=party_list, enemy_list=enemy_list)


def roll_dice(dice):
        num, die = dice
        total = 0

        for i in range(0, num):
            total += random.randint(1,die)
        
        return total