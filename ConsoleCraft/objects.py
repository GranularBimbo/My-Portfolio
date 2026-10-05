from colorama import *

class Creature():
    def __init__(self, icon):
        self.x = 0
        self.y = 3
        self.icon = icon
        self.inventory = {Fore.LIGHTGREEN_EX + '▓' : 0, Fore.LIGHTBLACK_EX + '▓' : 0, Fore.YELLOW + '▓' : 0}
        self.hotBar = [' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ']

    def addToInv(self, block):
        self.inventory[block] += 1

    def removeFromInv(self, block):
        self.inventory[block] -= 1