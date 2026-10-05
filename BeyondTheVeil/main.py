# Pyrax: Beyond The Veil
# A "Pyrax: The Veil of Flesh" spinoff roguelike about delving into the underwell to kill Zeroth
# Written by Anthony Tanevski

import random

import colorama
import items
from colorama import *
import keyboard
import math
import time
import os
import sys
import creature
import room
import pathfinding
import monsters
import characters
import worldGen

running = True
on_overworld = True
on_tilemap = False
creatingCharacter = True
typingName = False
nameBuffer = "Adventurer"
sheetSelectorPos = 0
world = []
enemies = []
overworld_enemies = []
dungeon_enemies = {}
floorItems = []
overworld_floorItems = []
dungeon_floorItems = {}
dungeon_player_state = {}
you = creature.Creature('Garius of Leyawind', 'Quickblade', Fore.LIGHTWHITE_EX + '@', 10, 10, 10, 10, 10, 10, 9, 19, [1,4])
#you.rollStats()
minRooms = 6
maxRooms = 9
width = 100
height = 23
depth = 0
turns = 0
invDisplay = False
doors = []
hallways = []
allInsideRoomNodes = []
inputPrimed = True
sprintingRight = False
sprinting = False
sprintDirection = ""
adjL = {}
canSpawnEnemy = True
droppingItem = False
quaffingPotion = False
readingScroll = False
zapping = False
wearingItem = False
eating = False
throwing = False
removing = False
wielding = False
choosingThrowDirection = False
floorMonstersSpawned = False
classOptions = [characters.fighter, characters.dwarf]
classIndex = 0
selectingClass = False

currentClass = classOptions[classIndex]

alphabetIndexMap = {'a': 0, 'b': 1, 'c': 2, 'd': 3, 'e': 4, 'f': 5, 'g': 6, 'h': 7,
                    'i': 8, 'j': 9, 'k': 10, 'l': 11, 'm': 12, 'n': 13, 'o': 14,
                    'p': 15, 'q': 16, 'r': 17, 's': 18, 't': 19, 'u': 20, 'v': 21,
                    'w': 22, 'x': 23, 'y': 24, 'z': 25}

allIconTags = {'!': "Item", 'V': "Enemy", 'G': "Enemy", '.': "Floor", '▓': "Hallway"}

def clear():
    os.system('cls')

def move_cursor(row, col):
    print(f"\x1b[{row};{col}H", end="")

def adjacentTiles(point, diag_allowed):
    global world
    global adjL

    tiles = []
    adjL = {}

    if diag_allowed:
        if point[0]-1 > 0 and point[1]-1 > 0 and point[0]-1 < width and point[1]-1 < height:
            tiles.append(world[point[1]-1][point[0]-1])
            adjL["up left"] = world[point[1]-1][point[0]-1]

        if point[0] + 1 > 0 and point[1] - 1 > 0 and point[0] + 1 < width and point[1] - 1 < height:
            tiles.append(world[point[1] - 1][point[0] + 1])
            adjL["up right"] = world[point[1] - 1][point[0] + 1]

        if point[0] - 1 > 0 and point[1] + 1 > 0 and point[0] - 1 < width and point[1] + 1 < height:
            tiles.append(world[point[1] + 1][point[0] - 1])
            adjL["down left"] = world[point[1] + 1][point[0] - 1]

        if point[0] + 1 > 0 and point[1] + 1 > 0 and point[0] + 1 < width and point[1] + 1 < height:
            tiles.append(world[point[1] + 1][point[0] + 1])
            adjL["down right"] = world[point[1] + 1][point[0] + 1]

    if point[0] > 0 and point[1] - 1 > 0 and point[0] < width and point[1] - 1 < height:
        tiles.append(world[point[1]-1][point[0]])
        adjL["up"] = world[point[1]-1][point[0]]

    if point[0]-1 > 0 and point[1] > 0 and point[0]-1 < width and point[1] < height:
        tiles.append(world[point[1]][point[0]-1])
        adjL["left"] = world[point[1]][point[0]-1]

    if point[0]+1 > 0 and point[1] > 0 and point[0]+1 < width and point[1] < height:
        tiles.append(world[point[1]][point[0]+1])
        adjL["right"] = world[point[1]][point[0]+1]

    if point[0] > 0 and point[1]+1 > 0 and point[0] < width and point[1]+1 < height:
        tiles.append(world[point[1]+1][point[0]])
        adjL["down"] = world[point[1]+1][point[0]]

    return tiles

'''
def pointCollidingWithWall(point, room):
    if (point[0] <= (room.x + room.width)) and (point[0] >= room.x):
        if (point[1] == room.y) or (point[1] == (room.y+room.height)):
            return True
    if (point[1] <= (room.y + room.height)) and (point[1] >= room.y):
        if (point[0] == room.x) or (point[0] == (room.x+room.width)):
            return True

    return False
'''

def moveRight():
    global you
    global world
    global height
    global width
    global inputPrimed

    if inputPrimed:
        inputPrimed = False
        if validNode([you.x+1,you.y], world):
            if you.x+1 < width:
                world[you.y][you.x] = you.under
                you.x += 1
                you.under = world[you.y][you.x]

def moveUpAndRight():
    global you
    global world
    global height
    global width
    global inputPrimed

    if inputPrimed:
        inputPrimed = False
        if validNode([you.x + 1, you.y - 1], world):
            if you.x+1 < width and you.y-1 > 0:
                world[you.y][you.x] = you.under
                you.x += 1
                you.y -= 1
                you.under = world[you.y][you.x]

def moveUpAndLeft():
    global you
    global world
    global height
    global width
    global inputPrimed

    if inputPrimed:
        inputPrimed = False
        if validNode([you.x - 1, you.y - 1], world):
            if you.x-1 > 0 and you.y-1 > 0:
                world[you.y][you.x] = you.under
                you.x -= 1
                you.y -= 1
                you.under = world[you.y][you.x]

def moveDownAndRight():
    global you
    global world
    global height
    global width
    global inputPrimed

    if inputPrimed:
        inputPrimed = False
        if validNode([you.x + 1, you.y + 1], world):
            if you.x+1 < width and you.y+1 < height:
                world[you.y][you.x] = you.under
                you.x += 1
                you.y += 1
                you.under = world[you.y][you.x]

def moveDownAndLeft():
    global you
    global world
    global height
    global width
    global inputPrimed

    if inputPrimed:
        inputPrimed = False
        if validNode([you.x - 1, you.y + 1], world):
            if you.x-1 > 0 and you.y+1 < height:
                world[you.y][you.x] = you.under
                you.x -= 1
                you.y += 1
                you.under = world[you.y][you.x]

def moveLeft():
    global you
    global world
    global height
    global width
    global inputPrimed

    if inputPrimed:
        inputPrimed = False
        if validNode([you.x - 1, you.y], world):
            if you.x-1 > 0:
                world[you.y][you.x] = you.under
                you.x -= 1
                you.under = world[you.y][you.x]

def moveUp():
    global you
    global world
    global height
    global width
    global inputPrimed

    if inputPrimed:
        inputPrimed = False
        if validNode([you.x, you.y - 1], world):
            if you.y-1 > 0:
                world[you.y][you.x] = you.under
                you.y -= 1
                you.under = world[you.y][you.x]

def moveDown():
    global you
    global world
    global height
    global width
    global inputPrimed

    if inputPrimed:
        inputPrimed = False
        if validNode([you.x, you.y + 1], world):
            if you.y+1 < height:
                world[you.y][you.x] = you.under
                you.y += 1
                you.under = world[you.y][you.x]

def sprintRight():
    global you
    global world
    global height
    global width
    global inputPrimed

    if validNode([you.x + 1, you.y], world):
        if you.x + 1 < width:
            world[you.y][you.x] = you.under
            you.x += 1
            you.under = world[you.y][you.x]

def sprint(direction):
    global you
    global world
    global rooms
    global enemies
    global turns

    dir_map = {"left": (-1,0),
               "up left": (-1,-1),
               "up": (0,-1),
               "up right": (1,-1),
               "right": (1,0),
               "down right": (1,1),
               "down": (0,1),
               "down left": (-1,1)}

    passTurn()
    dx, dy = dir_map[direction]
    if validNode([you.x+dx, you.y+dy], world) and not creature.creatureOnNode([you.x + dx, you.y + dy], enemies, you):
        world[you.y][you.x] = you.under
        you.x += dx
        you.y += dy
        you.under = world[you.y][you.x]

    #monsters.monsterMovement(world, rooms, you, enemies)

def rest():
    global world
    global rooms
    global you
    global enemies
    global width
    global height
    global turns
    global invDisplay
    global inputPrimed

    if not invDisplay:
        if inputPrimed:
            inputPrimed = False
            passTurn()
            monsters.monsterMovement(world, worldGen.rooms, you, enemies, False, 0, width, height)

def toggleSprint(direction):
    global inputPrimed
    global sprinting
    global sprintDirection
    global invDisplay

    if not invDisplay:
        if inputPrimed:
            inputPrimed = False
            sprintDirection = direction

            if sprinting:
                sprinting = False

def enableSprint(direction):
    global inputPrimed
    global sprinting
    global sprintDirection
    global invDisplay

    if not invDisplay:
        if inputPrimed:
            inputPrimed = False
            sprintDirection = direction
            sprinting = True

def toggleSprintRight():
    global inputPrimed
    global sprintingRight

    if inputPrimed:
        inputPrimed = False

        if sprintingRight:
            sprintingRight = False
        else:
            sprintingRight = True

def validNode(coords, level):
    global width
    global height
    #make it so that spots that are adjacent to walls are invalid to see how it looks
    x, y = coords
    if (x >= 0 and x < width) and (y >= 0 and y < height):
        if level[y][x] == (Fore.YELLOW + '#' + Fore.LIGHTWHITE_EX) or level[y][x] == (' '):
            return False
        return True
    return False

def flipDirection(direction):
    if direction == "left":
        flipped = "right"
    elif direction == "up left":
        flipped = "down right"
    elif direction == "up":
        flipped = "down"
    elif direction == "up right":
        flipped = "down left"
    elif direction == "right":
        flipped = "left"
    elif direction == "down right":
        flipped = "up left"
    elif direction == "down":
        flipped = "up"
    elif direction == "down left":
        flipped = "up right"

    return flipped

def selectItem():
    global droppingItem
    global invDisplay
    global readingScroll
    global quaffingPotion
    global zapping
    global choosingThrowDirection

    if throwing:
        choosingThrowDirection = False

    sys.stdout.write('\x1b[1A')
    sys.stdout.write('\033[25C')
    sys.stdout.flush()
    if choosingThrowDirection:
        print(Back.LIGHTBLACK_EX + "Which direction would you like to throw it in?" + Back.BLACK)
        sys.stdout.write('\033[25C')
        sys.stdout.flush()
    print(Back.LIGHTBLACK_EX + 'Selection: ' + Fore.LIGHTWHITE_EX + Back.BLACK)
    sys.stdout.write('\x1b[1A')
    sys.stdout.write('\033[25C')
    sys.stdout.flush()
    while True:
        event = keyboard.read_event()
        if event.event_type == keyboard.KEY_DOWN:
            key = event.name
            if key == 'enter':
                continue  # Ignore empty

            if key in alphabetIndexMap.keys():
                print(Back.LIGHTBLACK_EX + 'Selection: ' + key + Fore.LIGHTWHITE_EX + Back.BLACK)  # Echo key
            elif key == 'esc':
                droppingItem = False
                readingScroll = False
                quaffingPotion = False
                zapping = False
                invDisplay = False
                #sys.stdout.write('\x1b[' + str(len(world)) + 'A')
                sys.stdout.write('\033[H')
                sys.stdout.flush()
                #drawMap()
                #clear()
            else:
                print(Back.LIGHTBLACK_EX + 'Selection: ' + Fore.LIGHTWHITE_EX + Back.BLACK)
            return key

def instantiateItem(itemIdx):
    global you
    itemStats = items.findItemByName(you.inventory[itemIdx])
    return items.Item(itemStats[0], itemStats[1], itemStats[2], itemStats[3], itemStats[4])

def removeArmor():
    global you
    global floorItems

    if len(you.inventory) < you.invSize and you.armor != '':
        itemStats = items.findItemByName(you.armor)
        it = items.Item(itemStats[0], itemStats[1], itemStats[2], itemStats[3], itemStats[4])
        you.baseAC -= it.ac
        you.armor = ''
        you.inventory.append(it.name)

def wearArmor(itemIdx):
    global you
    global floorItems
    it = instantiateItem(itemIdx)

    if you.armor == '':
        you.armor = it.name
        you.baseAC += it.ac
    else:
        itemStats = items.findItemByName(you.armor)
        oldItem = items.Item(itemStats[0], itemStats[1], itemStats[2], itemStats[3], itemStats[4])
        you.baseAC -= oldItem.ac
        you.armor = it.name
        you.baseAC += it.ac
        if len(you.inventory) < you.invSize:
            you.inventory.append(oldItem.name)

def wield(itemIdx):
    global you
    global floorItems
    it = instantiateItem(itemIdx)

    if you.weapon == '':
        you.weapon = it.name
        you.damageDice = it.damageDice
    else:
        itemStats = items.findItemByName(you.weapon)
        oldItem = items.Item(itemStats[0], itemStats[1], itemStats[2], itemStats[3], itemStats[4])
        you.weapon = it.name
        you.damageDice = it.damageDice
        if len(you.inventory) < you.invSize:
            you.inventory.append(oldItem.name)

def equipLight(itemIdx):
    global you
    it = instantiateItem(itemIdx)

    if you.light == '':
        you.light = it.name
    else:
        #itemStats = items.findItemByName(you.armor)
        #oldItem = items.Item(itemStats[0], itemStats[1], itemStats[2], itemStats[3], itemStats[4])
        you.light = it.name

def displayInventory():
    global you
    global droppingItem
    global invDisplay
    global turns
    global quaffingPotion
    global readingScroll
    global zapping
    global wearingItem
    global eating
    global wielding
    global throwing
    global removing
    global choosingThrowDirection

    sys.stdout.write('\x1b[' + str(len(world)) + 'A')
    sys.stdout.write('\033[25C')
    sys.stdout.flush()

    alphabet = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
                's', 't', 'u', 'v', 'w', 'x', 'y', 'z']

    message = "INVENTORY"
    if droppingItem:
        message = message + " - Which item would you like to drop?"
    elif quaffingPotion:
        message = message + " - Which potion would you like to drink?"
    elif readingScroll:
        message = message + " - Which scroll would you like to read?"
    elif zapping:
        message = message + " - Which wand/staff would you like to use?"
    elif wearingItem:
        message = message + " - Which item would you like to wear?"
    elif eating:
        message = message + " - Which item would you like to eat?"
    elif wielding:
        message = message + " - Which item would you like to wield?"
    elif throwing:
        message = message + " - Which item would you like to throw?"

    print(Fore.LIGHTWHITE_EX + Back.LIGHTBLACK_EX + "   " + message + (' '*(56-len(message))))
    lines = []
    for i in you.inventory:
        if quaffingPotion:
            if items.findItemByName(i)[2] == "Potion":
                sys.stdout.write('\033[25C')
                sys.stdout.flush()
                invItemID = '(' + alphabet.pop(0) + ')'
                print(invItemID + i + (' ' * (59-(len(i)+len(invItemID)))))
        elif readingScroll:
            if items.findItemByName(i)[2] == "Scroll":
                sys.stdout.write('\033[25C')
                sys.stdout.flush()
                invItemID = '(' + alphabet.pop(0) + ')'
                print(invItemID + i + (' ' * (59-(len(i)+len(invItemID)))))
        elif zapping:
            if items.findItemByName(i)[2] == "Wand" or items.findItemByName(i)[2] == "Staff":
                sys.stdout.write('\033[25C')
                sys.stdout.flush()
                invItemID = '(' + alphabet.pop(0) + ')'
                print(invItemID + i + (' ' * (59-(len(i)+len(invItemID)))))
        elif wearingItem:
            if items.findItemByName(i)[2] == "Armor" or items.findItemByName(i)[2] == "Light":
                sys.stdout.write('\033[25C')
                sys.stdout.flush()
                invItemID = '(' + alphabet.pop(0) + ')'
                print(invItemID + i + (' ' * (59-(len(i)+len(invItemID)))))
        elif eating:
            if items.findItemByName(i)[2] == "Food":
                sys.stdout.write('\033[25C')
                sys.stdout.flush()
                invItemID = '(' + alphabet.pop(0) + ')'
                print(invItemID + i + (' ' * (59-(len(i)+len(invItemID)))))
        elif wielding:
            if items.findItemByName(i)[2] == "Weapon":
                sys.stdout.write('\033[25C')
                sys.stdout.flush()
                invItemID = '(' + alphabet.pop(0) + ')'
                print(invItemID + i + (' ' * (59-(len(i)+len(invItemID)))))
        else:
            sys.stdout.write('\033[25C')
            sys.stdout.flush()
            invItemID = '(' + alphabet.pop(0) + ')'
            print(invItemID + i + (' ' * (59-(len(i)+len(invItemID)))))
    #sys.stdout.write('\x1b[' + str(len(world)) + 'A')
    #sys.stdout.flush()

    if droppingItem:
        sys.stdout.write('\033[1B')
        sys.stdout.flush()
        selection = selectItem()
        try:
            itemIdx = alphabetIndexMap.get(selection)
            #print("idx: " + str(itemIdx))
            you.dropItem(itemIdx, floorItems)
            passTurn()
            droppingItem = False
            invDisplay = False
        except:
            pass
    elif quaffingPotion:
        sys.stdout.write('\033[1B')
        sys.stdout.flush()
        selection = selectItem()
        try:
            itemIdx = alphabetIndexMap.get(selection)
            it = instantiateItem(itemIdx)
            it.use(you)
            you.inventory.pop(itemIdx)
            passTurn()
            quaffingPotion = False
            invDisplay = False
        except:
            pass
    elif readingScroll:
        sys.stdout.write('\033[1B')
        sys.stdout.flush()
        selection = selectItem()
        try:
            itemIdx = alphabetIndexMap.get(selection)
            it = instantiateItem(itemIdx)
            it.use(you)
            you.inventory.pop(itemIdx)
            passTurn()
            readingScroll = False
            invDisplay = False
        except:
            pass
    elif zapping:
        sys.stdout.write('\033[1B')
        sys.stdout.flush()
        selection = selectItem()
        try:
            itemIdx = alphabetIndexMap.get(selection)
            it = instantiateItem(itemIdx)
            it.use(you)
            you.inventory.pop(itemIdx)
            passTurn()
            zapping = False
            invDisplay = False
        except:
            pass
    elif eating:
        sys.stdout.write('\033[1B')
        sys.stdout.flush()
        selection = selectItem()
        try:
            itemIdx = alphabetIndexMap.get(selection)
            it = instantiateItem(itemIdx)
            it.use(you)
            you.inventory.pop(itemIdx)
            passTurn()
            eating = False
            invDisplay = False
        except:
            pass
    elif wielding:
        sys.stdout.write('\033[1B')
        sys.stdout.flush()
        selection = selectItem()
        try:
            itemIdx = alphabetIndexMap.get(selection)
            it = instantiateItem(itemIdx)
            wield(itemIdx)
            you.inventory.pop(itemIdx)
            passTurn()
            wielding = False
            invDisplay = False
        except:
            pass
    elif throwing:
        sys.stdout.write('\033[1B')
        sys.stdout.flush()
        selection = selectItem()
        try:
            itemIdx = alphabetIndexMap.get(selection)
            if itemIdx is None or itemIdx < 0 or itemIdx >= len(you.inventory):
                raise ValueError("Invalid throw item selection")
            it = instantiateItem(itemIdx)
            sys.stdout.write('\033[H')
            throwing = False
            choosingThrowDirection = True
            drawMap()
            direction = selectItem()
            if direction == "right" or direction == "l":
                dir = "right"
            elif direction == "left" or direction == "h":
                dir = "left"
            elif direction == "up" or direction == "k":
                dir = "up"
            elif direction == "down" or direction == "j":
                dir = "down"
            elif direction == "y":
                dir = "up left"
            elif direction == "u":
                dir = "up right"
            elif direction == "b":
                dir = "down left"
            elif direction == "n":
                dir = "down right"
            else:
                raise ValueError("Invalid throw direction")

            choosingThrowDirection = False
            invDisplay = False
            keyboard.unhook_all_hotkeys()
            you.throw(dir, itemIdx, world, width, height, worldGen.rooms, enemies, floorItems, turns, depth)
            monsters.monsterMovement(world, worldGen.rooms, you, enemies, sprinting, sprintsDone, width, height)
            passTurn()
        except Exception:
            pass

        regHotKeys()
    elif wearingItem:
        sys.stdout.write('\033[1B')
        sys.stdout.flush()
        selection = selectItem()
        try:
            itemIdx = alphabetIndexMap.get(selection)
            it = instantiateItem(itemIdx)
            if it.type == "Armor":
                wearArmor(itemIdx)
            elif it.type == "Light":
                equipLight(itemIdx)
            you.inventory.pop(itemIdx)
            passTurn()
            wearingItem = False
            invDisplay = False
        except:
            pass
    else:
        while True:
            event = keyboard.read_event()
            if event.event_type == keyboard.KEY_DOWN:
                key = event.name
                if key == 'enter':
                    continue  # Ignore empty

                if key == 'esc' or key == 'space' or key == 'i':
                    invDisplay = False
                    sys.stdout.write('\033[H')
                    sys.stdout.flush()
                return key

    #sys.stdout.write('\x1b[' + str(len(world)) + 'A')
    sys.stdout.write('\033[H')
    sys.stdout.flush()

def toggleInv():
    global invDisplay
    global inputPrimed
    global droppingItem
    global quaffingPotion
    global readingScroll
    global zapping
    global wearingItem
    global wielding
    global eating
    global throwing
    global removing
    global choosingThrowDirection

    if inputPrimed and not droppingItem:
        inputPrimed = False
        if invDisplay:
            invDisplay = False
        else:
            invDisplay = True
        droppingItem = False
        quaffingPotion = False
        readingScroll = False
        zapping = False
        wearingItem = False
        wielding = False
        eating = False
        throwing = False
        removing = False
        choosingThrowDirection = False

def drawMap():
    global you
    global world
    global invDisplay
    global world

    statLines = [you.name, you.Class.name, '', 'STR  : ' + str(you.Str), 'DEX  : ' + str(you.Dex), 'CON  : ' + str(you.Con),
                 'INT  : ' + str(you.Int), 'WIS  : ' + str(you.Wis), 'CHA  : ' + str(you.Cha), '',
                 'HP   : ' + str(you.hp), 'MAXHP: ' + str(you.maxHP), '', 'LEV  : ' + str(you.level), 'EXP  : ' + str(you.exp), 'DEPT : ' + str(depth), '',
                 'AC   : ' + str(you.ac), 'THAC0: ' + str(you.thac0), 'GOLD : ' + str(you.gold), 'TURNS: ' + str(turns)]
    #print("drawing")

    for r in world:
        row = ""
        for c in r:
            row = row + c

        ui = ""
        if len(statLines) > 0:
            ui = statLines.pop(0)

        print(Fore.LIGHTWHITE_EX + Back.BLACK + ui + ((18-len(ui))*' ') + row + Back.BLACK + Fore.LIGHTWHITE_EX)
    sys.stdout.write('\x1b[' + str(len(world)) + 'A')
    sys.stdout.flush()

def dropItem():
    global invDisplay
    global droppingItem
    global inputPrimed
    global quaffingPotion
    global readingScroll
    global zapping
    global wearingItem
    global wielding
    global eating
    global throwing
    global removing
    global choosingThrowDirection

    if inputPrimed:
        inputPrimed = False
        invDisplay = True
        droppingItem = True
        quaffingPotion = False
        readingScroll = False
        zapping = False
        wearingItem = False
        wielding = False
        eating = False
        throwing = False
        removing = False
        choosingThrowDirection = False

def wearItem():
    global invDisplay
    global droppingItem
    global quaffingPotion
    global readingScroll
    global inputPrimed
    global zapping
    global wearingItem
    global wielding
    global eating
    global throwing
    global removing
    global choosingThrowDirection

    if inputPrimed:
        inputPrimed = False
        invDisplay = True
        readingScroll = False
        droppingItem = False
        quaffingPotion = False
        zapping = False
        wearingItem = True
        wielding = False
        eating = False
        throwing = False
        removing = False
        choosingThrowDirection = False

def readScroll():
    global invDisplay
    global droppingItem
    global quaffingPotion
    global readingScroll
    global inputPrimed
    global zapping
    global wearingItem
    global wielding
    global eating
    global throwing
    global removing
    global choosingThrowDirection

    if inputPrimed:
        inputPrimed = False
        invDisplay = True
        readingScroll = True
        droppingItem = False
        quaffingPotion = False
        zapping = False
        wearingItem = False
        wielding = False
        eating = False
        throwing = False
        removing = False
        choosingThrowDirection = False

def eatItem():
    global invDisplay
    global quaffingPotion
    global inputPrimed
    global droppingItem
    global readingScroll
    global zapping
    global wearingItem
    global wielding
    global eating
    global throwing
    global removing
    global choosingThrowDirection

    if inputPrimed:
        inputPrimed = False
        invDisplay = True
        quaffingPotion = False
        droppingItem = False
        readingScroll = False
        zapping = False
        wearingItem = False
        wielding = False
        eating = True
        throwing = False
        removing = False
        choosingThrowDirection = False

def wieldItem():
    global invDisplay
    global quaffingPotion
    global inputPrimed
    global droppingItem
    global readingScroll
    global zapping
    global wearingItem
    global wielding
    global eating
    global throwing
    global removing
    global choosingThrowDirection

    if inputPrimed:
        inputPrimed = False
        invDisplay = True
        quaffingPotion = False
        droppingItem = False
        readingScroll = False
        zapping = False
        wearingItem = False
        wielding = True
        eating = False
        throwing = False
        removing = False
        choosingThrowDirection = False

def toggleThrowing():
    global invDisplay
    global quaffingPotion
    global inputPrimed
    global droppingItem
    global readingScroll
    global zapping
    global wearingItem
    global wielding
    global eating
    global throwing
    global removing
    global choosingThrowDirection

    if inputPrimed:
        inputPrimed = False
        invDisplay = True
        quaffingPotion = False
        droppingItem = False
        readingScroll = False
        zapping = False
        wearingItem = False
        wielding = False
        eating = False
        throwing = True
        removing = False
        choosingThrowDirection = False

def quaffPotion():
    global invDisplay
    global quaffingPotion
    global inputPrimed
    global droppingItem
    global readingScroll
    global zapping
    global wearingItem
    global wielding
    global eating
    global throwing
    global removing
    global choosingThrowDirection

    if inputPrimed:
        inputPrimed = False
        invDisplay = True
        quaffingPotion = True
        droppingItem = False
        readingScroll = False
        zapping = False
        wearingItem = False
        wielding = False
        eating = False
        throwing = False
        removing = False
        choosingThrowDirection = False

def toggleRemove():
    global inputPrimed
    global removing

    if inputPrimed:
        inputPrimed = False
        removeArmor()
        passTurn()

def zap():
    global invDisplay
    global quaffingPotion
    global inputPrimed
    global droppingItem
    global readingScroll
    global zapping
    global wearingItem
    global wielding
    global eating
    global throwing
    global removing

    if inputPrimed:
        inputPrimed = False
        invDisplay = True
        quaffingPotion = False
        droppingItem = False
        readingScroll = False
        zapping = True
        wearingItem = False
        wielding = False
        eating = False
        throwing = False
        removing = False

def shutdown():
    clear()
    print("Done!")
    keyboard.unhook_all()
    keyboard.unhook_all_hotkeys()
    print("\033[?25h", end="")  # restore cursor
    sys.stdout.flush()
    sys.exit(0)

def spawnPlayer():
    global world

    random.seed(worldGen.seeds[you.world_y][you.world_x])

    if not worldGen.allInsideRoomNodes:
        return

    valid_positions = [pos for pos in worldGen.allInsideRoomNodes if '.' in world[pos[1]][pos[0]]]
    if not valid_positions:
        return

    x, y = random.choice(valid_positions)
    #world[y][x] = Fore.MAGENTA + '<' + Style.RESET_ALL
    you.x, you.y = x, y
    you.under = Fore.MAGENTA + '<' + Style.RESET_ALL

def spawnPlayerInWorld():
    global world

    x = random.randint(1, width-2)
    y = random.randint(1, height-2)
    you.x, you.y = x, y
    you.world_x, you.world_y = x, y
    you.under = world[y][x]

def goDown():
    global on_overworld
    global on_tilemap
    global world
    global depth
    global enemies
    global floorItems
    global dungeon_enemies
    global dungeon_floorItems
    global overworld_enemies
    global overworld_floorItems

    if on_overworld:
        on_overworld = False
        on_tilemap = True
        overworld_enemies = enemies
        overworld_floorItems = floorItems
        world[you.y][you.x] = you.under

        if worldGen.all_tile_maps[you.y][you.x] == None:
            worldGen.all_tile_maps[you.y][you.x] = worldGen.generate_tilemap(worldGen.biomes_map[you.y][you.x], width, height, you.x, you.y)
        
        if worldGen.dungeon_locations[you.y][you.x] != None:
            x = worldGen.dungeon_locations[you.y][you.x][0]
            y = worldGen.dungeon_locations[you.y][you.x][1]
            worldGen.all_tile_maps[you.y][you.x][y][x] = Fore.MAGENTA + '>' + Style.RESET_ALL

        world = worldGen.all_tile_maps[you.y][you.x]
        you.under = world[you.y][you.x]
    elif on_tilemap:
        if '>' in you.under:
            world[you.y][you.x] = you.under
            location = (you.world_x, you.world_y)
            depth = 1
            dungeon_enemies.setdefault((location, depth), [])
            dungeon_floorItems.setdefault((location, depth), [])

            if worldGen.dungeon_maps.get(location) is None or len(worldGen.dungeon_maps.get(location, [])) == 0:
                worldGen.generateLevel(location)
                worldGen.renderLevel(depth, dungeon_enemies[(location, depth)], dungeon_floorItems[(location, depth)], location)

            enemies = dungeon_enemies[(location, depth)]
            floorItems = dungeon_floorItems[(location, depth)]
            world = worldGen.dungeon_maps[location]

            if (location, depth) in dungeon_player_state:
                you.x, you.y, you.under = dungeon_player_state[(location, depth)]
            else:
                spawnPlayer()
                dungeon_player_state[(location, depth)] = (you.x, you.y, you.under)

            on_tilemap = False
            depth = 1

def goUp():
    global on_overworld
    global on_tilemap
    global world
    global you
    global depth
    global enemies
    global floorItems
    global dungeon_enemies
    global dungeon_floorItems
    global overworld_enemies
    global overworld_floorItems

    if not on_overworld and not on_tilemap and '<' in you.under and depth == 1:
        world[you.y][you.x] = you.under
        location = (you.world_x, you.world_y)
        dungeon_player_state[(location, depth)] = (you.x, you.y, you.under)
        dungeon_enemies[(location, depth)] = enemies
        dungeon_floorItems[(location, depth)] = floorItems
        enemies = overworld_enemies
        floorItems = overworld_floorItems
        world = worldGen.all_tile_maps[you.world_y][you.world_x]
        you.x = worldGen.dungeon_locations[you.world_y][you.world_x][0]
        you.y = worldGen.dungeon_locations[you.world_y][you.world_x][1]
        depth = 0
        on_tilemap = True
        you.under = world[you.y][you.x]
    elif on_tilemap:
        world[you.y][you.x] = you.under
        enemies = overworld_enemies
        floorItems = overworld_floorItems
        world = worldGen.overworld
        you.x = you.world_x
        you.y = you.world_y
        on_overworld = True
        on_tilemap = False
        you.under = world[you.y][you.x]

def passTurn():
    global turns
    turns += 1
    statusProcs()

def statusProcs():
    global you
    if you.vigorFor > 0:
        you.vigorFor -= 1
        you.ac = you.baseAC + math.ceil(you.baseAC*0.2)
    else:
        you.ac = you.baseAC

def regHotKeys():
    keyboard.unhook_all_hotkeys
    keyboard.add_hotkey('shift+.', lambda: goDown())
    keyboard.add_hotkey('shift+comma', lambda: goUp())
    keyboard.add_hotkey('right', lambda: you.move("right", world, worldGen.rooms, enemies, width, height, floorItems, invDisplay, on_overworld))
    keyboard.add_hotkey('shift+right', lambda: enableSprint("right"))
    keyboard.add_hotkey('shift+l', lambda: enableSprint("right"))
    keyboard.add_hotkey('shift+left', lambda: enableSprint("left"))
    keyboard.add_hotkey('shift+h', lambda: enableSprint("left"))
    keyboard.add_hotkey('shift+up', lambda: enableSprint("up"))
    keyboard.add_hotkey('shift+k', lambda: enableSprint("up"))
    keyboard.add_hotkey('shift+down', lambda: enableSprint("down"))
    keyboard.add_hotkey('shift+j', lambda: enableSprint("down"))
    keyboard.add_hotkey('shift+u', lambda: enableSprint("up right"))
    keyboard.add_hotkey('shift+y', lambda: enableSprint("up left"))
    keyboard.add_hotkey('shift+b', lambda: enableSprint("down left"))
    keyboard.add_hotkey('shift+n', lambda: enableSprint("down right"))
    keyboard.add_hotkey('l', lambda: you.move("right", world, worldGen.rooms, enemies, width, height, floorItems, invDisplay, on_overworld))
    keyboard.add_hotkey('left', lambda: you.move("left", world, worldGen.rooms, enemies, width, height, floorItems, invDisplay, on_overworld))
    keyboard.add_hotkey('h', lambda: you.move("left", world, worldGen.rooms, enemies, width, height, floorItems, invDisplay, on_overworld))
    keyboard.add_hotkey('up', lambda: you.move("up", world, worldGen.rooms, enemies, width, height, floorItems, invDisplay, on_overworld))
    keyboard.add_hotkey('k', lambda: you.move("up", world, worldGen.rooms, enemies, width, height, floorItems, invDisplay, on_overworld))
    keyboard.add_hotkey('down', lambda: you.move("down", world, worldGen.rooms, enemies, width, height, floorItems, invDisplay, on_overworld))
    keyboard.add_hotkey('j', lambda: you.move("down", world, worldGen.rooms, enemies, width, height, floorItems, invDisplay, on_overworld))
    keyboard.add_hotkey('y', lambda: you.move("up left", world, worldGen.rooms, enemies, width, height, floorItems, invDisplay, on_overworld))
    keyboard.add_hotkey('u', lambda: you.move("up right", world, worldGen.rooms, enemies, width, height, floorItems, invDisplay, on_overworld))
    keyboard.add_hotkey('b', lambda: you.move("down left", world, worldGen.rooms, enemies, width, height, floorItems, invDisplay, on_overworld))
    keyboard.add_hotkey('n', lambda: you.move("down right", world, worldGen.rooms, enemies, width, height, floorItems, invDisplay, on_overworld))
    keyboard.add_hotkey('.', lambda: rest())
    keyboard.add_hotkey('i', lambda: toggleInv())
    keyboard.add_hotkey('d', lambda: dropItem())
    keyboard.add_hotkey('q', lambda: quaffPotion())
    keyboard.add_hotkey('r', lambda: readScroll())
    keyboard.add_hotkey('m', lambda: zap())
    keyboard.add_hotkey('shift+w', lambda: wearItem())
    keyboard.add_hotkey('e', lambda: eatItem())
    keyboard.add_hotkey('w', lambda: wieldItem())
    keyboard.add_hotkey('t', lambda: toggleThrowing())
    keyboard.add_hotkey('shift+t', lambda: toggleRemove())
    keyboard.add_hotkey('shift+q', lambda: quit())

def diceRoll(numDice, dieSides):
    total = 0
    for i in range(numDice):
        total += random.randint(1, dieSides)
    return total

def moveSheetSelector(dir):
    global sheetSelectorPos
    if dir == "down":
        if sheetSelectorPos < 2:
            sheetSelectorPos += 1
        else:
            sheetSelectorPos = 0
    elif dir == "up":
        if sheetSelectorPos > 0:
            sheetSelectorPos -= 1
        else:
            sheetSelectorPos = 2


try:
    sys.stdout.write("\033[?25l")  # hide cursor
    sys.stdout.flush()
    def createCharacter():
        global you
        global creatingCharacter
        global sheetSelectorPos
        global typingName
        global nameBuffer
        global classIndex
        global classOptions
        global selectingClass
        global currentClass

        #keyboard.add_hotkey('down', lambda: moveSheetSelector("down"))
        #keyboard.add_hotkey('up', lambda: moveSheetSelector("up"))
        clear()

        sVal = diceRoll(3, 6)
        dVal = diceRoll(3, 6)
        cVal = diceRoll(3, 6)
        iVal = diceRoll(3, 6)
        wVal = diceRoll(3, 6)
        chVal = diceRoll(3, 6)
        displayName = "Adventurer"

        while creatingCharacter:
            if typingName:
                displayName = nameBuffer if nameBuffer.strip() != "" else ""

            if sheetSelectorPos == 0:
                if typingName:
                    print(f"> Name:  {displayName}_ <          ")
                else:
                    print(f"> Name:  {displayName} <          ")
            else:
                print(f"Name:  {displayName}          ")
            
            if selectingClass:
                print(f"> Class:  {classOptions[classIndex].name} v <          \n")
            elif sheetSelectorPos == 1:
                print(f"> Class:  {classOptions[classIndex].name} <          \n")
            else:
                print(f"Class:  {classOptions[classIndex].name}          \n")

            if sheetSelectorPos == 2:
                print("> Roll Stats:  <\n")
            else:
                print("Roll Stats      \n")

            print(f"STR: {sVal}          ")
            print(f"DEX: {dVal}          ")
            print(f"CON: {cVal}          ")
            print(f"INT: {iVal}          ")
            print(f"WIS: {wVal}          ")
            print(f"CHA: {chVal}          ")

            currentClass.clear_table_area(2, 50, currentClass.MAX_TABLE_HEIGHT)
            currentClass.draw_title(2, 50, 55)
            currentClass.drawClassTable(start_row=2, start_col=50)

            event = keyboard.read_event()

            if event.event_type == keyboard.KEY_DOWN:
                key = event.name

                # ESC exits
                if key == 'esc' and not droppingItem:
                    creatingCharacter = False
                    you.name = displayName if displayName.strip() != "" else "Adventurer"
                    you.Class = classOptions[classIndex]
                    you.Str = sVal
                    you.Dex = dVal
                    you.Con = cVal
                    you.Int = iVal
                    you.Wis = wVal
                    you.Cha = chVal

                # --- NAME TYPING MODE ---
                if typingName:
                    if key == 'enter':
                        displayName = nameBuffer if nameBuffer.strip() != "" else "Adventurer"
                        typingName = False
                    elif key == 'backspace':
                        nameBuffer = nameBuffer[:-1]
                    elif len(key) == 1:
                        if len(nameBuffer) < 20:  # optional limit
                            nameBuffer += key

                # --- CLASS SELECTION MODE ---
                elif selectingClass:
                    if key == 'enter':
                        selectingClass = False
                    elif key == 'down':
                        classIndex = (classIndex + 1) % len(classOptions)
                        currentClass = classOptions[classIndex]
                    elif key == 'up':
                        classIndex = (classIndex - 1) % len(classOptions)
                        currentClass = classOptions[classIndex]

                else:
                    # normal menu behavior
                    if key == 'down':
                        moveSheetSelector("down")
                    elif key == 'up':
                        moveSheetSelector("up")
                    elif key == 'enter':
                        if sheetSelectorPos == 0:
                            typingName = True
                            nameBuffer = ""  # or keep existing: you.name
                        elif sheetSelectorPos == 1:
                            selectingClass = True
                        elif sheetSelectorPos == 2:
                            sVal = diceRoll(3, 6)
                            dVal = diceRoll(3, 6)
                            cVal = diceRoll(3, 6)
                            iVal = diceRoll(3, 6)
                            wVal = diceRoll(3, 6)
                            chVal = diceRoll(3, 6)

            sys.stdout.write('\033[H')
            sys.stdout.flush()
finally:
    sys.stdout.write("\033[?25h")  # show cursor
    sys.stdout.flush()

createCharacter()

if running:
    world = worldGen.generate_world("dungeon", width, height)
    spawnPlayerInWorld()
    #generateLevel()
    #renderLevel()
    #spawnPlayer()
    regHotKeys()
    clear()

# game loop
try:
    sys.stdout.write("\033[?25l")  # hide cursor
    sys.stdout.flush()
    while running:
        if depth > 0:
            if turns%70 == 0 and worldGen.floorMonstersSpawned and canSpawnEnemy:
                monsters.spawnMonsters(depth, dungeon_enemies[((you.world_x,you.world_y),depth)], worldGen.floorMonstersSpawned)
                newM = dungeon_enemies[((you.world_x,you.world_y),depth)].pop()
                monsters.placeMonster(newM, world, worldGen.allInsideRoomNodes)
                dungeon_enemies[((you.world_x,you.world_y),depth)].append(newM)
                #maybe make that spawned enemy chase the player?
                canSpawnEnemy = False
            elif turns%70 != 0:
                canSpawnEnemy = True

        if you.x >= len(world[0]):
            you.x = len(world[0])-1
        elif you.x <= 0:
            you.x = 0

        if you.y > len(world):
            you.y = len(world)-3

        dir_map = {"left": (-1, 0),
                "up left": (-1, -1),
                "up": (0, -1),
                "up right": (1, -1),
                "right": (1, 0),
                "down right": (1, 1),
                "down": (0, 1),
                "down left": (-1, 1)}


        sprintsDone = 0
        if sprinting:
            dx, dy = dir_map[sprintDirection]

            cameFromDir = flipDirection(sprintDirection)

            surroundingHalls = 0
            surroundingMonsters = 0

            for a in adjacentTiles([you.x,you.y], True):
                for m in monsters.allMonsters:
                    if m in a:
                        surroundingMonsters += 1

            if '▓' in you.under:
                for a in adjacentTiles([you.x,you.y], False):
                    if '▓' in a:
                        surroundingHalls += 1

                if surroundingHalls == 2 and (world[you.y+dy][you.x+dx] == ' ' or '#' in world[you.y+dy][you.x+dx]):
                    for k in adjL.keys():
                        if '▓' in adjL.get(k) and k != cameFromDir:
                            sprintDirection = k
                            break

            sprint(sprintDirection)
            sprintsDone = 1
            cond = ('#' in world[you.y+dy][you.x+dx]) or ('!' in adjacentTiles([you.x, you.y], True) or
                    (creature.creatureOnNode([you.x + dx, you.y + dy], enemies, you)))
            while not cond:
                dx, dy = dir_map[sprintDirection]

                cameFromDir = flipDirection(sprintDirection)

                surroundingHalls = 0
                surroundingMonsters = 0

                for a in adjacentTiles([you.x, you.y], True):
                    for m in monsters.allMonsters:
                        if m in a:
                            surroundingMonsters += 1

                if '▓' in you.under:
                    for a in adjacentTiles([you.x, you.y], False):
                        if '▓' in a:
                            surroundingHalls += 1

                    if surroundingHalls == 2 and (
                            world[you.y + dy][you.x + dx] == ' ' or '#' in world[you.y + dy][you.x + dx]):
                        for k in adjL.keys():
                            if '▓' in adjL.get(k) and k != cameFromDir:
                                sprintDirection = k
                                break

                if not '▓' in you.under:
                    cond = ('#' in world[you.y + dy][you.x + dx]) or ('!' in adjacentTiles([you.x, you.y], True)) or (
                            '+' in adjacentTiles([you.x, you.y], False) or (surroundingHalls >= 3) or (surroundingMonsters > 0))
                elif '▓' in you.under:
                    cond = ('!' in adjacentTiles([you.x, you.y], True)) or ('+' in adjacentTiles([you.x, you.y], False)
                            or (surroundingHalls >= 3) or (surroundingMonsters > 0))

                '''
                if surroundingMonsters > 0:
                    adjacentTiles([you.x,you.y], True)
                    for m in monsters.allMonsters:
                        for k in adjL.keys():
                            if m in adjL.get(k) and k != cameFromDir:
                                cond = False
                '''

                if not cond:
                    sprint(sprintDirection)
                    sprintsDone += 1

            for m in enemies:
                m.pathToPlayer = pathfinding.getPath([m.x, m.y], [you.x, you.y], world, True, True)

            sprintsToDo = sprintsDone
            for i in range(sprintsToDo):
                monsters.monsterMovement(world, worldGen.rooms, you, enemies, sprinting, sprintsDone, width, height)
                sprintsDone -= 1

            sprinting = False

        world[you.y][you.x] = you.icon
        for m in enemies:
            if m.hp > 0:
                world[m.y][m.x] = m.icon
            else:
                world[m.y][m.x] = m.under
                enemies.remove(m)

        #print(pathfinding.getPath([0,0], [5,5], world, False))
        #print("adjTiles: " + str(adjacentTiles([you.x,you.y])))

        if not invDisplay:
            drawMap()

        if invDisplay:
            displayInventory()

        event = keyboard.read_event()

        if keyboard.is_pressed('esc') and not droppingItem:
            shutdown()

        inputPrimed = True
        if you.moved:
            passTurn()
            you.moved = False
        
        move_cursor(0,0)
        print(f"dungeon here?: {worldGen.dungeon_locations[you.y][you.x]}")
finally:
    sys.stdout.write("\033[?25h")  # show cursor
    sys.stdout.flush()

