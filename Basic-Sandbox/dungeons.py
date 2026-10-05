import dearpygui.dearpygui as dpg
import random
import math

class Dungeon():
    def __init__(self, name: str = "Default Dungeon", id: int = -1, location: tuple = (0,0),
                 size: int = 1):
        self.name = name
        self.id = id
        self.location = location

        # 1, 2, 3, or 4 for small, medium, large, or mega
        self.size = 4 if size > 4 else 1 if size < 1 else size 

        self.percent_explored = 0
        self.percents_mapped = {} # party: percent
        self.points_of_interest = {} # percent: item/thing

