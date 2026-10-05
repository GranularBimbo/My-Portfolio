import random
import time
import sys
import os

from colorama import *
import math
import pathfinding
import monsters
import items

def drawMap(player, turns, level, depth):
    statLines = [player.name, player.Class.name if hasattr(player.Class, 'name') else str(player.Class), '',
                 'STR  : ' + str(player.Str), 'DEX  : ' + str(player.Dex), 'CON  : ' + str(player.Con),
                 'INT  : ' + str(player.Int), 'WIS  : ' + str(player.Wis), 'CHA  : ' + str(player.Cha), '',
                 'HP   : ' + str(player.hp), 'MAXHP: ' + str(player.maxHP), '',
                 'LEV  : ' + str(player.level), 'EXP  : ' + str(player.exp), 'DEPT : ' + str(depth), '',
                 'AC   : ' + str(player.ac), 'THAC0: ' + str(player.thac0), 'GOLD : ' + str(player.gold), 'TURNS: ' + str(turns)]
    #print("drawing")

    for r in level:
        row = ""
        for c in r:
            row = row + c

        ui = ""
        if len(statLines) > 0:
            ui = statLines.pop(0)

        print(Fore.LIGHTWHITE_EX + Back.BLACK + ui + ((18-len(ui))*' ') + row + Back.BLACK + Fore.LIGHTWHITE_EX)
    sys.stdout.write('\x1b[' + str(len(level)) + 'A')
    sys.stdout.flush()

def isItemAtPoint(point, level):
    x, y = point
    for i in items.allItems:
        if i[1] in level[y][x]:
            return True
    return False

def getItemAtPoint(point, floorItemList):
    x, y = point
    for i in floorItemList:
        if i.x == x and i.y == y:
            return i

def validNode(coords, level, width, height):
    #make it so that spots that are adjacent to walls are invalid to see how it looks
    x, y = coords
    if (x >= 0 and x < width) and (y >= 0 and y < height):
        if '.' in level[y][x] or '▓' in level[y][x] or '+' in level[y][x] or isItemAtPoint([x,y], level) or 'Æ' in level[y][x] or '⁋' in level[y][x] or '¶' in level[y][x] or '⁂' in level[y][x] or '¥' in level[y][x] or '~' in level[y][x] or '^' in level[y][x] or '„' in level[y][x] or ',' in level[y][x] or '>' in level[y][x] or '<' in level[y][x]:
            return True
    return False

def creatureOnNode(coords, enemyList, player):
    x, y = coords
    if x == player.x and y == player.y:
        return True
    else:
        for m in enemyList:
            if x == m.x and y == m.y:
                return True
    return False

def diceRoll(numDice, dieSides):
    total = 0
    for i in range(numDice):
        total += random.randint(1, dieSides)
    return total

def distanceBetweenPoints(point1, point2):
    x1, y1 = point1
    x2, y2 = point2
    return math.sqrt(((x2-x1)**2) + ((y2-y1)**2))

def findCreatureAtPos(coords, enemyList):
    x, y = coords
    for m in enemyList:
        if x == m.x and y == m.y:
            return m

class Creature():                                                         # [num of dice, num of sides]
    def __init__(self, name, Class , icon, Str, Dex, Con, Int, Wis, Cha, Ac, Thac0, damageDice):
        self.icon = icon
        self.x = 0
        self.y = 0
        self.world_x = 0
        self.world_y = 0
        self.under = Fore.LIGHTGREEN_EX + '.' + Fore.LIGHTWHITE_EX
        self.Str = Str
        self.Dex = Dex
        self.Con = Con
        self.Int = Int
        self.Wis = Wis
        self.Cha = Cha
        self.maxHP = 10
        self.hp = self.maxHP
        self.toHit = 0
        self.level = 1
        self.maxExp = 10
        self.exp = 0
        self.gold = 0
        self.ac = Ac
        self.thac0 = Thac0
        self.baseAC = 10
        self.vigorFor = 0
        self.name = name
        self.Class = Class
        self.sleeping = False
        self.chasing = False
        self.pathToPlayer = []
        self.pathTail = []
        self.damageDice = damageDice
        self.moved = False
        self.armor = ""
        self.inventory = []
        self.invSize = 20
        self.weapon = ""

    def rollStats(self):
        self.Str = diceRoll(3, 6)
        self.Dex = diceRoll(3, 6)
        self.Con = diceRoll(3, 6)
        self.Int = diceRoll(3, 6)
        self.Wis = diceRoll(3, 6)
        self.Cha = diceRoll(3, 6)

    def addToInv(self, item, floorItemList):
        if len(self.inventory) < self.invSize:
            self.inventory.append(item.name)
            self.under = item.under
            floorItemList.remove(getItemAtPoint([self.x,self.y], floorItemList))

    def dropItem(self, itemIndex, floorItemList):
        itemStats = items.findItemByName(self.inventory[itemIndex])
        it = items.Item(itemStats[0], itemStats[1], itemStats[2], itemStats[3], itemStats[4])
        it.x, it.y = [self.x, self.y]
        it.under = self.under
        if it.type == "Food":
            color = Fore.LIGHTRED_EX
        else:
            color = Fore.LIGHTBLUE_EX
        self.under = color + it.icon + Fore.LIGHTWHITE_EX
        floorItemList.append(it)
        self.inventory.pop(itemIndex)

    def statMod(self, statNum):
        return math.floor((statNum-10)/2)

    # finds the id of the room that the given point is inside
    # returns -1 if it is not in a room
    def findRoomInsideID(self, roomsList):
        for ro in roomsList:
            if self.x > ro.x and self.x < (ro.x + ro.width):
                if self.y > ro.y and self.y < (ro.y + ro.height):
                    return ro.id

        return -1

    def throw(self, dir, itemIdx, level, width, height, roomList, enemyList, floorItemList, turns, depth):
        itemStats = items.findItemByName(self.inventory[itemIdx])
        item = items.Item(itemStats[0], itemStats[1], itemStats[2], itemStats[3], itemStats[4])
        item.x = self.x
        item.y = self.y
        # restore the tile under the player before placing the thrown item
        level[self.y][self.x] = self.under
        item.under = self.under
        items.placeItemAtPos(item, level, [item.x, item.y])
        item.thrown = True
        self.inventory.pop(itemIdx)
        floorItemList.append(item)
        dir_map = {"left": (-1, 0),
                   "up left": (-1, -1),
                   "up": (0, -1),
                   "up right": (1, -1),
                   "right": (1, 0),
                   "down right": (1, 1),
                   "down": (0, 1),
                   "down left": (-1, 1)}

        dx, dy = dir_map[dir]
        #print("Begin Throwing")
        while (validNode([item.x+dx, item.y+dy], level, width, height) or creatureOnNode([item.x+dx, item.y+dy], enemyList, self)) and item.thrown:
            #print("Throwing now!")
            sys.stdout.write('\033[H')
            sys.stdout.flush()
            drawMap(self, turns, level, depth)
            item.move(dir, level, roomList, enemyList, width, height, floorItemList, False, self)
            level[self.y][self.x] = self.icon
            sys.stdout.write('\033[H')
            sys.stdout.flush()
            drawMap(self, turns, level, depth)
            time.sleep(0.05)
        #floorItemList.pop()

    def move(self, direction, level, roomList, enemyList, width, height, floorItemList, invDisplay, in_overworld):
        if not invDisplay:
            dir_map = {"left": (-1, 0),
                       "up left": (-1, -1),
                       "up": (0, -1),
                       "up right": (1, -1),
                       "right": (1, 0),
                       "down right": (1, 1),
                       "down": (0, 1),
                       "down left": (-1, 1)}

            dx, dy = dir_map[direction]
            monsters.monsterPathUpdates(self, enemyList)
            if validNode([self.x + dx, self.y + dy], level, width, height):
                level[self.y][self.x] = self.under
                if in_overworld:
                    self.world_x += dx
                    self.world_y += dy
                    self.x += dx
                    self.y += dy
                else:
                    self.x += dx
                    self.y += dy
                self.under = level[self.y][self.x]

            if creatureOnNode([self.x+dx, self.y+dy], enemyList, self):
                self.attack(findCreatureAtPos([self.x+dx, self.y+dy], enemyList))

            self.moved = True

            for i in floorItemList:
                if i.x == self.x and i.y == self.y:
                    self.addToInv(i, floorItemList)

            monsters.monsterMovement(level, roomList, self, enemyList, False, 0, width, height)

    def setPos(self, pos, level, enemyList, player, width, height):
        x, y = pos
        if validNode(pos, level, width, height) and not creatureOnNode(pos, enemyList, player):
            level[self.y][self.x] = self.under
            self.y = y
            self.x = x
            self.under = level[self.y][self.x]

    def attack(self, creature):
        creature.chasing = True
        toHit = random.randint(1, 20)
        if toHit >= (self.thac0 - creature.ac):
            numDice, dieSides = self.damageDice
            dmg = (random.randint(1, dieSides))*numDice
            creature.hp -= dmg

