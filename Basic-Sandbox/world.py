import dearpygui.dearpygui as dpg

# queue of functions to be called by creatures each game tick
# 2d list so that multiple funcs can be called per tick if necessary
func_queue = [] # [[func, [*args]]]
in_combat_time = False

# "party name": party_obj
parties = {} 

game_state = {
    "next_roll_seconds": 3,
    "day": 1,
    "in_dungeon": True,
    "dungeon_progress": 0.0,
    "hour": 9,
    "minute": 25,
    "second": 3,
}

# "time_type": (time per irl second, "time type per irl second")
time_flows = {
    "dungeon": (1, "Minutes"),
    "paused": (0, "Seconds"),
    "town": (1, "Hours"),
    "combat": (6, "Seconds")
}

round_nums = {} # combat_window_tag: round_num

time_flow = "dungeon"


# ==========================================
# FUNCTIONS
# ==========================================


def add_to_func_queue(func: function, args: list = None, congruent: bool = False):
    global func_queue

    if not congruent:
        func_queue.append([[func, args]])
    else:
        func_queue[-1].append([func, args])


def time_to_string():
    global game_state

    if game_state.get("hour") < 10:
        h = '0' + str(game_state.get("hour"))
    else:
        h = str(game_state.get("hour"))

    if game_state.get("minute") < 10:
        m = '0' + str(game_state.get("minute"))
    else:
        m = str(game_state.get("minute"))

    if game_state.get("second") < 10:
        s = '0' + str(game_state.get("second"))
    else:
        s = str(game_state.get("second"))

    return h + ":" + m + ":" + s


def update_log(log_tag, message):
    curr_val = dpg.get_value(log_tag)
    new_val = curr_val + message
    dpg.set_value(log_tag, new_val)


def console_log(message: str):
    global game_state
    update_log(log_tag="global_console", message=f"[T{game_state.get("day")} {time_to_string()}] {message}\n")


def custom_log(log_tag: str, message: str, not_time_stamped: bool = False):
    global game_state
    if not_time_stamped:
        update_log(log_tag=log_tag, message=f"{message}\n")
    else:
        update_log(log_tag=log_tag, message=f"[T{game_state.get("day")} {time_to_string()}] {message}\n")