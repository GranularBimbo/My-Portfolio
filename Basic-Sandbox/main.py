# ==========================================
# B/X Sandbox: An old school sandbox RPG
# Author: Anthony Tanevski
# Date: 05/25/2026
# ==========================================

import dearpygui.dearpygui as dpg
import random
import time
import math
import interface
import entities
import items
import world_gen
import world
import encounters
import image_handling

# ==========================================
# SETUP AND HELPERS
# ==========================================

dpg.create_context()
dpg.create_viewport(title='B/X Sandbox', width=1024, height=640)

image_handling.load_images()

last_tick_time = time.time()

def on_viewport_resize():
    global screen_width
    global screen_height

    # Get the updated width of the total viewport
    screen_width = dpg.get_viewport_width()
    screen_height = dpg.get_viewport_height()

    dpg.configure_item("party_manager", pos=(screen_width-250, 20), height=screen_height-30)
    dpg.configure_item("global_log", width=screen_width-250, pos=(0, screen_height-210))
    dpg.configure_item("global_console", width=dpg.get_item_width("global_log")-16)
    dpg.configure_item("inventory", pos=(0, screen_height-410))
    dpg.configure_item("travel_window", pos=(screen_width-1025, screen_height-710))

def console_log(message: str):
    global game_state
    world.update_log(log_tag="global_console", message=f"[T{world.game_state.get("day")} {world.time_to_string()}] {message}\n")

def game_tick():
    global last_tick_time
    current_time = time.time()
    
    if current_time - last_tick_time >= 1.0:
        last_tick_time = current_time

        time_amount, time_type = world.time_flows[world.time_flow]

        if len(world.func_queue) > 0:
            func_list = world.func_queue.pop(0)

            for f in func_list:
                func_info = f
                func = func_info[0]
                args = func_info[1]

                if args is not None:
                    func(*args)
                else:
                    func()

                if len(world.func_queue) == 0 and world.in_combat_time:
                    world.time_flow = "paused"
                    time_amount, time_type = world.time_flows[world.time_flow]


        if time_amount > 0:
            if time_type == "Seconds":
                if world.game_state.get("second")+time_amount < 60:
                    world.game_state["second"] += time_amount
                    print(f"seconds increased by {time_amount}")
                else:
                    extra = (world.game_state["second"]+time_amount) - 59
                    world.game_state["second"] = 0 + extra
                    print(f"seconds increased by {time_amount} with overflow to the next minute")

                    if world.game_state.get("minute") < 59:
                        world.game_state["minute"] += 1
                    else:
                        world.game_state["minute"] = 0

                        if world.game_state.get("hour") < 23:
                            world.game_state["hour"] += 1
                        else:
                            world.game_state["hour"] = 0
                            world.game_state["day"] += 1
            
            elif time_type == "Minutes":
                if (world.game_state["minute"] + time_amount) < 60:
                    world.game_state["minute"] += time_amount
                else:
                    extra = (world.game_state["minute"]+time_amount) - 59
                    world.game_state["minute"] = 0 + extra

                    
                    if world.game_state.get("hour") < 23:
                        world.game_state["hour"] += 1
                    else:
                        world.game_state["hour"] = 0
                        world.game_state["day"] += 1

            elif time_type == "Hours":
                pass

        if world.time_flow != "paused":
            if world.game_state.get("in_dungeon") and world.game_state.get("dungeon_progress") < 1.0:
                world.game_state["dungeon_progress"] += 0.02
                update_progress("dungeon_bar", 0.02)
                #update_inv(char_id=0, inv_table_tag="inv_table", item="Sword", remove=True)

            if world.game_state.get("dungeon_progress") % 0.02 == 0:
                console_log("Random monster check: 1d6 = 2 (NO MONSTER)")
            
            world.game_state["next_roll_seconds"] -= 1
            if world.game_state["next_roll_seconds"] < 0:
                world.game_state["next_roll_seconds"] = 3  
                
                events = [
                    "Cleared Room 12,14 (Empty).",
                    "Room 13,14 reached. Current Event: Goblins! (Combat initiated)"
                ]
                chosen_event = random.choice(events)
                log_msg = f"[T{world.game_state['day']} 09:05:06] {chosen_event}\n"
            
            #current_log = dpg.get_value("exploration_log_text")
            #dpg.set_value("exploration_log_text", current_log + log_msg)

        #dpg.set_value("countdown_text", f"Next B/X Procedural Roll in: {game_state['next_roll_seconds']} seconds")

def find_item_row(table_tag, item):
    # Slot 1 contains the children (rows) of the table
    rows = dpg.get_item_children(table_tag, slot=1)
    
    for row in rows:
        # Slot 1 contains the children (cells/widgets) of the row
        cells = dpg.get_item_children(row, slot=1)
        for cell_id in cells:
            if dpg.get_value(cell_id) == item:
                return row
    return False

def update_progress(bar_tag, amount):
    curr_val = dpg.get_value(bar_tag)
    if curr_val < 1.0:
        new_val = curr_val + amount
        dpg.set_value(bar_tag, new_val)
        dpg.configure_item(bar_tag, overlay=f"{math.floor(world.game_state.get("dungeon_progress")*100)}%")

def update_inv(item: items.Item, char_id: int, qty: int = 1,
               add: bool = False, remove: bool = False, inv_table_tag = "inv_table"):

    curr_creature = entities.all_creatures[char_id]
    dpg.configure_item(inv_table_tag, label=f"Inventory - ({entities.all_creatures[0].name})")

    # =========================
    # ADD ITEM
    # =========================
    if add:

        # Item already exists -> increase qty
        if item.name in curr_creature.inv:

            curr_creature.inv[item.name]["qty"] += qty

            row_tag = curr_creature.inv[item.name]["row"]

            dpg.set_value(
                f"{row_tag}_qty",
                str(curr_creature.inv[item.name]["qty"])
            )

        # New item -> find first empty row
        else:

            free_row = None

            for i in range(12):

                row_tag = f"inv_row{i}"

                # If row has no children, it's empty
                if len(dpg.get_item_children(row_tag, 1)) == 0:
                    free_row = row_tag
                    break

            if free_row is None:
                console_log("Inventory full.")
                return

            # Store inventory data
            curr_creature.inv[item.name] = {
                "qty": qty,
                "row": free_row
            }

            # Add table cells
            dpg.add_selectable(label=item.name, parent=free_row, tag=f"{free_row}_button")
            dpg.add_text(str(qty), tag=f"{free_row}_qty", parent=free_row)

            # Placeholder stats
            if item.type == "Weapon":
                num, die = item.dmg_dice
                dpg.add_text(f"{num}d{die}", parent=free_row)
            else:
                dpg.add_text("-", parent=free_row)  # dmg

            if item.type == "Armor":
                dpg.add_text(f"{item.AC}", parent=free_row)
            else:
                dpg.add_text("-", parent=free_row)  # ac

            dpg.add_text(f"{item.value}gp", parent=free_row)  # value
            dpg.add_text(f"{item.size}", parent=free_row)  # size
            

    # =========================
    # REMOVE ITEM
    # =========================
    elif remove:

        if item not in curr_creature.inv:
            return

        curr_creature.inv[item]["qty"] -= qty

        row_tag = curr_creature.inv[item]["row"]

        # Remove item entirely if qty <= 0
        if curr_creature.inv[item]["qty"] <= 0:

            dpg.delete_item(row_tag)

            # Recreate empty row
            dpg.add_table_row(parent=inv_table_tag, tag=row_tag)

            del curr_creature.inv[item]

        else:

            dpg.set_value(
                f"{row_tag}_qty",
                str(curr_creature.inv[item]["qty"])
            )

# ==========================================
# Themes
# ==========================================
# with dpg.theme(tag="dark_theme"):
#     dpg.add_theme_color(dpg.mvThemeCol_TableRowBg, [40, 40, 60, 255], category=dpg.mvTable)
#     dpg.add_theme_color(dpg.mvThemeCol_TableRowBgAlt, [60, 60, 90, 255], category=dpg.mvTable)


# ==========================================
# MAIN INTERFACE
# ==========================================
screen_width = dpg.get_viewport_width()
screen_height = dpg.get_viewport_height()

with dpg.window(tag="primary_window", no_title_bar=True):
    with dpg.menu_bar():
        with dpg.menu(label="File", tag="File"):
            dpg.add_menu_item(label="Save")
            dpg.add_menu_item(label="Load")
            dpg.add_menu_item(label="Quit", callback=interface.quit_game)
        with dpg.menu(label="View", tag="View"):
            dpg.add_menu_item(label="Dungeon Progress *", tag="dungeon_progress_toggle", callback=interface.show_dungeon_progress)
            dpg.add_menu_item(label="Global Log *", tag="global_log_toggle", callback=interface.show_global_log)
            dpg.add_menu_item(label="Inventory *", tag="inv_toggle", callback=interface.show_inv)
            dpg.add_menu_item(label="Travel *", tag="travel_toggle", callback=interface.show_travel)
        with dpg.menu(label="Edit", tag="Edit"):
            pass

    # ==========================================
    # Dungeon Progress
    # ==========================================
    with dpg.window(label="Party A - Dungeon Progress (0,0)", tag="dungeon_progress", on_close=interface.show_dungeon_progress, width=300, height=200, pos=(0, 30)):
        dpg.add_text("Dungeon Progress")
        dpg.add_progress_bar(label="dungeon_bar", tag="dungeon_bar", width=250, height=20, default_value=world.game_state.get("dungeon_progress"), overlay=f"{math.floor(world.game_state.get("dungeon_progress")*100)}%")

    # ==========================================
    # Global Log
    # ==========================================
    with dpg.window(label="Global Log", tag="global_log", width=screen_width, height=200, pos=(0, 500), on_close=interface.show_global_log):
        dpg.add_input_text(tag="global_console", readonly=True, multiline=True, default_value="", width=(screen_width-20), height=150)

    # ==========================================
    # Inventory
    # ==========================================
    with dpg.window(label=f"Inventory - ({entities.all_creatures.get(0)})", tag="inventory", width=(screen_width/2), height=200, pos=(0, 250), on_close=interface.show_inv):
        with dpg.table(tag="inv_table", header_row=True, borders_innerH=True, borders_outerH=True, borders_innerV=True, borders_outerV=True) as inv_table:
            dpg.add_table_column(label="Item")
            dpg.add_table_column(label="Qty")
            dpg.add_table_column(label="Dmg")
            dpg.add_table_column(label="AC")
            dpg.add_table_column(label="Val")
            dpg.add_table_column(label="Size")
            for i in range(0, 12):
                dpg.add_table_row(tag=f"inv_row{i}")

    # ==========================================
    # Party Manager
    # ==========================================
    with dpg.child_window(label="Party Manager", tag="party_manager", width=250, pos=(screen_width+200, 20)):
        child_tag = dpg.last_item()
        dpg.add_text("Party Manager")
        dpg.add_group(tag="item_group")
        dpg.add_group(tag="char_group", payload_type="CHARACTER")
        dpg.add_spacer(width=250, height=screen_height, parent="char_group")
        #dpg.add_text("DROP HERE", parent="char_group")
        dpg.set_item_drop_callback("char_group", interface.drop_on_background_cb)

    # ==========================================
    # Party Manager Popup
    # ==========================================
    with dpg.window(tag="context_menu", show=False, popup=True, no_title_bar=True):
        dpg.add_selectable(label="Create Party", callback=interface.name_party)
        dpg.add_selectable(label="Create Character", callback=interface.open_char_creator, user_data=("char_prog_table", "char_stats_table", "char_creator_window", "char_skill_table", "char_skill_group", "char_stats_table_horizontal"))
        dpg.add_separator()
        dpg.add_selectable(label="Close", callback=lambda: dpg.configure_item("context_menu", show=False))

    # ==========================================
    # Party Selected Popup
    # ==========================================
    with dpg.window(tag="party_selected_popup", show=False, popup=True, no_title_bar=True):
        dpg.add_selectable(label="Create Character")
        dpg.add_selectable(label="Rename Party")
        dpg.add_selectable(label="Combat Options", callback=interface.open_combat_options)
        dpg.add_selectable(label="Delete Party", callback=interface.delete_party)

    # ==========================================
    # Namer Window
    # ==========================================
    with dpg.window(tag="namer_window", width=300, height=100, show=False, no_title_bar=True, pos=(630, 300),
                    no_resize=True, no_move=True):
        dpg.add_text("Name your party")
        dpg.add_input_text(default_value="", tag="namer_text", callback=interface.create_party, on_enter=True)
        dpg.add_selectable(tag="namer_ok", label="Ok", callback=interface.create_party)
        dpg.add_selectable(tag="namer_cancel", label="Cancel", callback=interface.close_namer)

    # ==========================================
    # Character Creator Window
    # ==========================================
    with dpg.window(tag="char_creator_window", width=500, height=470, show=False, no_title_bar=True, pos=(530, 200),
                    no_resize=True, no_move=True):
        dpg.add_text("Character Creator")
        dpg.add_input_text(default_value=interface.new_char.name, tag="char_name_input", label="Name",
                           callback=interface.create_character, on_enter=True,
                           user_data=("char_prog_table", "char_stats_table", "char_creator_window", "char_skill_table", "char_stats_table_horizontal"))
        dpg.add_combo(label="Class", items=["Fighter", "Dwarf", "Elf", "Thief"], tag="char_class_combo", default_value="Fighter",
                      callback= interface.build_char_tables, user_data=("char_prog_table", "char_stats_table", "char_creator_window", "char_skill_table", "char_skill_group", "char_stats_table_horizontal"))
    
        with dpg.group(horizontal=True):
            with dpg.group():
                with dpg.table(width=300, tag="char_skill_table", show=False, borders_innerH=True, borders_outerH=True, borders_innerV=True, borders_outerV=True, row_background=True):
                    dpg.add_table_column(label="Lvl")
                    dpg.add_table_column(label="CS")
                    dpg.add_table_column(label="TR")
                    dpg.add_table_column(label="HN")
                    dpg.add_table_column(label="HS")
                    dpg.add_table_column(label="MS")
                    dpg.add_table_column(label="OL")
                    dpg.add_table_column(label="PP")

                with dpg.group(horizontal=True):
                    with dpg.table(tag="char_stats_table", width=100, header_row=True, borders_innerH=True, borders_outerH=True, borders_innerV=True, borders_outerV=True, row_background=True,
                                show=True):
                        dpg.add_table_column(label="Atb")
                        dpg.add_table_column(label="Val")

                        with dpg.table_row():
                            dpg.add_text("STR")
                            dpg.add_text(interface.new_char.Str)

                        with dpg.table_row():
                            dpg.add_text("DEX")
                            dpg.add_text(interface.new_char.Dex)

                        with dpg.table_row():
                            dpg.add_text("CON")
                            dpg.add_text(interface.new_char.Con)

                        with dpg.table_row():
                            dpg.add_text("INT")
                            dpg.add_text(interface.new_char.Int)
                        
                        with dpg.table_row():
                            dpg.add_text("WIS")
                            dpg.add_text(interface.new_char.Wis)

                        with dpg.table_row():
                            dpg.add_text("CHA")
                            dpg.add_text(interface.new_char.Cha)

                    dpg.add_combo(label="Aln", tag="aln_choice", default_value="Law", width=80, items=["Law", "Neutral", "Chaos"])

                with dpg.table(tag="char_stats_table_horizontal", show=False, borders_innerH=True, borders_outerH=True, borders_innerV=True, borders_outerV=True, row_background=True):
                    dpg.add_table_column(label="STR")
                    dpg.add_table_column(label="DEX")
                    dpg.add_table_column(label="CON")
                    dpg.add_table_column(label="INT")
                    dpg.add_table_column(label="WIS")
                    dpg.add_table_column(label="CHA")

                dpg.add_button(label="Roll Stats", width=100)
                dpg.add_text("Skills & Languages")
                dpg.add_child_window(tag="char_skill_group", width=200, height=120, border=True)
                with dpg.table(tag="skills_layout_table", header_row=False, no_host_extendX=True):
                    dpg.add_table_column(width_fixed=True)

                dpg.add_button(label="Create Character", tag="char_creator_create_button", pos=(10, dpg.get_item_height("char_creator_window")-30),
                               callback=interface.create_character, user_data=("char_prog_table", "char_stats_table", "char_creator_window", "char_skill_table", "char_stats_table_horizontal"))

            dpg.add_spacer(width=20)

            with dpg.group():
                with dpg.table(tag="char_prog_table", policy=dpg.mvTable_SizingFixedFit, borders_innerH=True, borders_outerH=True, borders_innerV=True, borders_outerV=True, row_background=True):
                    dpg.add_table_column(label="Level")
                    dpg.add_table_column(label="XP")
                    dpg.add_table_column(label="HD")
                    dpg.add_table_column(label="THAC0")
                    dpg.add_table_column(label="D")
                    dpg.add_table_column(label="W")
                    dpg.add_table_column(label="P")
                    dpg.add_table_column(label="B")
                    dpg.add_table_column(label="S")

                    bonus_hp = 0
                    last_xp = 0
                    
                    for level in interface.new_char.prog_table:
                        with dpg.table_row(tag=f"level_row {level['Level']}"):
                            dpg.add_text(str(level["Level"]))
                            dpg.add_text(str(last_xp))
                            last_xp = level["xp_needed"]

                            if (level["Level"] > 9 and interface.new_char.Class == "Fighter"):
                                bonus_hp += 2
                                dpg.add_text(f"{level["HD"][0]}d{level["HD"][1]}+{bonus_hp}")
                            else:
                                dpg.add_text(f"{level["HD"][0]}d{level["HD"][1]}")

                            dpg.add_text(str(level["THAC0"]))
                            dpg.add_text(str(level["death_save"]))
                            dpg.add_text(str(level["wand_save"]))
                            dpg.add_text(str(level["para_save"]))
                            dpg.add_text(str(level["breath_save"]))
                            dpg.add_text(str(level["spell_save"]))

                dpg.add_combo(label="Starting Spell", items=["Sleep", "Charm Person", "Light", "Magic Missile"], 
                              tag="char_spell_combo", default_value="Sleep", width=150, show=False)


                dpg.add_button(label="Cancel", tag="char_creator_cancel" , pos=(dpg.get_item_width("char_creator_window")-60, dpg.get_item_height("char_creator_window")-30),
                               callback=interface.close_char_creator)

    # ==========================================
    # Travel Window
    # ==========================================
    with dpg.window(label="Travel", tag="travel_window", width=775, height=500, pos=(500, 300), on_close=interface.show_travel):
        with dpg.group(horizontal=True):
            with dpg.child_window(tag="map_canvas", no_scrollbar=True, border=True, no_scroll_with_mouse=True):
                with dpg.drawlist(tag="map_drawlist", width=550, height=500):
                    pass

            with dpg.group():
                dpg.add_text("Legend")
                with dpg.child_window(label="Legend", tag="map_legend", border=True, width=200, height=300):
                    pass

        dpg.add_text("Travel Options", tag="travel_options_text")
        dpg.add_combo(label="Party/Character", tag="travel_party", width=100)


dpg.setup_dearpygui()
dpg.show_viewport()
dpg.set_primary_window("primary_window", True)
dpg.toggle_viewport_fullscreen()

dpg.set_viewport_resize_callback(on_viewport_resize)

# ==========================================
# Test Stuff
# ==========================================

# Register a global right-click handler
with dpg.handler_registry():
    dpg.add_mouse_click_handler(button=dpg.mvMouseButton_Right, callback=interface.open_party_popup)

test_player = entities.Character(id=0, name="Player", roll_stats_on_creation=True)
# test_ghoul = entities.spawn_monster("Ghoul")

# for attack in test_ghoul.attacks:
#     attack.execute(attacker=test_ghoul, target=test_player)

for i in range(10):
    update_inv(char_id=0, inv_table_tag="inv_table", item=items.sword, qty=1, add=True)

update_inv(char_id=0, item=items.platemail, qty=2, add=True)
update_inv(char_id=0, item=items.chainmail, qty=1, add=True)
#update_inv(char_id=0, item="Plate Mail", qty=2, remove=True)

# ==========================================
# Setup
# ==========================================

world_gen.draw_hex_map()

with dpg.item_handler_registry(tag="window_resize_handler"):
    # 2. Add a resize listener that targets our calculation function
    dpg.add_item_resize_handler(callback=interface.travel_resize)

dpg.bind_item_handler_registry("travel_window", "window_resize_handler")

while dpg.is_dearpygui_running():
    game_tick()
    #interface.build_char_stat_table("char_stats_table")
    dpg.render_dearpygui_frame()

dpg.destroy_context()

