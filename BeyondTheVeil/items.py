import random
from colorama import *
import monsters


def isItemAtPoint(point, level):
    global allItems
    x, y = point
    for i in allItems:
        if i[1] in level[y][x]:
            return True
    return False

# def creatureOnNode(coords, enemyList, player):
#     x, y = coords
#     if x == player.x and y == player.y:
#         return True
#     else:
#         for m in enemyList:
#             if x == m.x and y == m.y:
#                 return True
#     return False

def creatureOnNode(coords, enemyList):
    x, y = coords
    for m in enemyList:
            if x == m.x and y == m.y:
                return True
    return False

def findCreatureAtPos(coords, enemyList):
    x, y = coords
    for m in enemyList:
        if x == m.x and y == m.y:
            return m

def validNode(coords, level, width, height):
    #make it so that spots that are adjacent to walls are invalid to see how it looks
    x, y = coords
    if (x >= 0 and x < width) and (y >= 0 and y < height):
        if '.' in level[y][x] or '▓' in level[y][x] or '+' in level[y][x] or isItemAtPoint([x,y], level):
            return True
    return False

def spawnItems(depth, floorItemList):
    numItems = random.randint(0, 5)
    if depth < 3:
        numItems += depth

    for i in range(numItems):
        selector = random.randint(0, len(allItems) - 1)
        itemStats = allItems[selector]
        it = Item(itemStats[0], itemStats[1], itemStats[2], itemStats[3], itemStats[4])
        floorItemList.append(it)


def placeItem(item, level, allInsideRoomNodes):
    startPos = allInsideRoomNodes[random.randint(0, len(allInsideRoomNodes) - 1)]
    item.x, item.y = startPos[0], startPos[1]
    if '.' in level[item.y][item.x]:
        item.under = level[item.y][item.x]
        if item.type == "Food":
            color = Fore.LIGHTRED_EX
        else:
            color = Fore.LIGHTBLUE_EX
        level[item.y][item.x] = color + item.icon + Fore.LIGHTWHITE_EX
    else:
        placeItem(item, level, allInsideRoomNodes)

def placeItemAtPos(item, level, pos):
    #use this to place the thrown item before each drawWorld
    item.x, item.y = pos
    item.under = level[item.y][item.x]
    if item.type == "Food":
        color = Fore.LIGHTRED_EX
    else:
        color = Fore.LIGHTBLUE_EX
    level[item.y][item.x] = color + item.icon + Fore.LIGHTWHITE_EX

class Item():
    def __init__(self, name, icon, type, damageDice, ac):
        self.name = name
        self.icon = icon
        self.type = type
        self.damageDice = damageDice
        self.ac = ac
        self.x = 0
        self.y = 0
        self.under = ''
        self.thrown = False

    def attack(self, creature):
        creature.chasing = True
        toHit = random.randint(1, 20)
        if toHit >= creature.ac:
            numDice, dieSides = self.damageDice
            dmg = (random.randint(1, dieSides))*numDice
            creature.hp -= dmg

    def move(self, direction, level, roomList, enemyList, width, height, floorItemList, invDisplay, player=None):
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
            target_x = self.x + dx
            target_y = self.y + dy

            # Check if there's a creature at the target position BEFORE moving
            if creatureOnNode([target_x, target_y], enemyList):
                target = findCreatureAtPos([target_x, target_y], enemyList)
                level[self.y][self.x] = self.under
                if player is not None:
                    roll = random.randint(1, 20)
                    if roll >= player.thac0 - target.ac:
                        if self.type == "Potion":
                            self.use(target)
                        else:
                            self.attack(target)
                        self.thrown = False
                        floorItemList.remove(self)
                    else:
                        self._dropNear(target, level, enemyList, floorItemList, width, height)
                else:
                    self._dropNear(target, level, enemyList, floorItemList, width, height)
            elif validNode([target_x, target_y], level, width, height):
                # Only move if no creature was hit
                level[self.y][self.x] = self.under
                self.x = target_x
                self.y = target_y
                placeItemAtPos(self, level, [self.x, self.y])
            else:
                # Stop if the path is blocked by a wall or invalid tile
                self.thrown = False

            self.moved = True

    def use(self, target):
        if self.type == "Potion":
            if "Tree-Like Vigor" in self.name:
                treeLikeVigor(target)
            elif "Health" in self.name:
                heal(target)
        elif self.type == "Scroll":
            pass
        elif self.type == "Armor":
            pass
        elif self.type == "Light":
            pass
        elif self.type == "Wand":
            if "Tree-Like Vigor" in self.name:
                treeLikeVigor(target)
        elif self.type == "Staff":
            if "Tree-Like Vigor" in self.name:
                treeLikeVigor(target)
        elif self.type == "Food":
            pass

    def _dropNear(self, creature_target, level, enemyList, floorItemList, width, height):
        level[self.y][self.x] = self.under
        self.thrown = False

        candidates = []
        for dx in (-1, 0, 1):
            for dy in (-1, 0, 1):
                if dx == 0 and dy == 0:
                    continue
                nx = creature_target.x + dx
                ny = creature_target.y + dy
                if 0 <= nx < width and 0 <= ny < height:
                    if validNode([nx, ny], level, width, height) and not creatureOnNode([nx, ny], enemyList):
                        if not isItemAtPoint([nx, ny], level) and not any(i.x == nx and i.y == ny for i in floorItemList):
                            if level[ny][nx] != '@':
                                candidates.append((nx, ny))

        if candidates:
            self.x, self.y = random.choice(candidates)
            self.under = level[self.y][self.x]
            placeItemAtPos(self, level, [self.x, self.y])
        else:
            floorItemList.remove(self)


def treeLikeVigor(target):
    target.vigorFor = 20

def heal(target):
    target.hp += 35
    if target.hp > target.maxHP:
        target.hp = target.maxHP

# Potions
healthPotion = ["Health Potion", '!', "Potion", [0,0], 0]
treeLikeVigorPotion = ["Potion of Tree-Like Vigor", '!', "Potion", [0,0], 0]
potionOfSpacialGrasp = ["Potion of Spacial Grasp", '!', "Potion", [0,0], 0]

# Wands
wandOfHealing = ["Wand of Healing", '-', "Wand", [1,2], 0]
wandOfTreeLikeVigor = ["Wand of Tree-Like Vigor", '-', "Wand", [1,2], 0]
wandOfSpacialGrasp = ["Wand of Spacial Grasp", '-', "Wand", [1,2], 0]

# Staves
staffOfHealing = ["Staff of Healing", '/', "Wand", [1,2], 0]
staffOfTreeLikeVigor = ["Staff of Tree-Like Vigor", '/', "Wand", [1,2], 0]
staffOfSpacialGrasp = ["Staff of Spacial Grasp", '/', "Wand", [1,2], 0]

# Scrolls
scrollOfSenseFlesh = ["Scroll of Sense Flesh", '♪', "Scroll", [0,0], 0]
scrollOfDeadFunneling = ["Scroll of Dead Funneling", '♪', "Scroll", [0,0], 0]
scrollOfEnchantWeapon = ["Scroll of Enchant Weapon", '♪', "Scroll", [0,0], 0]

# Armor
hunterGarb = ["Hunter's Garb", ']', "Armor", [0,0], 3]
archerCoat = ["Archer's Coat", ']', "Armor", [0,0], 3]
leatherArmor = ["Leather Armor", ']', "Armor", [0,0], 4]

# Weapons
longSword = ["Longsword", '↑', "Weapon", [1,6], 0]
dagger = ["Dagger", '↑', "Weapon", [1,4], 0]
bow = ["Bow", '↑', "Weapon", [1,4], 0]

# Food
foodRation = ["Food Ration", '♧', "Food", [0,0], 0]


allItems = [healthPotion, treeLikeVigorPotion, scrollOfSenseFlesh, hunterGarb, longSword,
            scrollOfDeadFunneling, archerCoat, leatherArmor, dagger, bow, potionOfSpacialGrasp,
            scrollOfEnchantWeapon, wandOfHealing, wandOfTreeLikeVigor, wandOfSpacialGrasp,
            staffOfHealing, staffOfSpacialGrasp, staffOfTreeLikeVigor, foodRation]

#allItems = [leatherArmor, archerCoat, hunterGarb]

def findItemByName(itemName):
    global allItems

    for i in allItems:
        if i[0] == itemName:
            return i