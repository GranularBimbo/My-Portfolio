import random

import colorama
from colorama import *
import keyboard
import objects
import math
import time
import os
import sys

you = objects.Creature(Fore.LIGHTWHITE_EX + '@')
cursor = objects.Creature(Back.GREEN)

num = 0
done = False

dDown = False
aDown = False
cursorActive = False
firstTimeDrawn = True
inputPrimed = True
falling = False
curY = you.y
modes = ['Build', 'Break']
mode = 1
frameNum = 0
timeSinceJump = time.time()

heights = []

def clear():
    os.system('cls')

def movecur(x, y):
    sys.stdout.write(f"\033[{y};{x}H")
    sys.stdout.flush()

#-1 for left, 1 for right
def modeSelect(dir):
    global modes
    global mode
    global inputPrimed

    if inputPrimed:
        inputPrimed = False
        if dir == -1:
            mode -= 1
            if mode < 0:
                mode = len(modes)-1
        elif dir == 1:
            mode += 1
            if mode > len(modes)-1:
                mode = 0

        drawWorld()

def generateMap(width, height):
    global heights
    map = []
    lastHeight = 0

    if width < 3:
        width = 3
    if height < 10:
        height = 10

    for i in range(0, height):
        r = []
        col = 0
        for c in range(0, width+2):
            if i == 0 and lastHeight == 0:
                h = random.randint(3, height-4)
                heights.append(h)
                lastHeight = h
            elif i == 0 and lastHeight > 0:
                h = random.randint(lastHeight-2, lastHeight+2)
                if h < 2:
                    h = 2
                elif h > height-4:
                    h = height-4
                heights.append(h)
                lastHeight = h
            elif i > 0:
                h = heights[col]

            color = Fore.LIGHTWHITE_EX
            if i == height+1-heights[col]:
                color = Fore.LIGHTGREEN_EX
            elif i > height+1-heights[col] and i < height+1-math.floor(heights[col]/2):
                chance = random.randint(1,5)
                if chance == 1:
                    color = Fore.LIGHTBLACK_EX
                else:
                    color = Fore.YELLOW
            elif i >= height+1-math.floor(heights[col]/2):
                color = Fore.LIGHTBLACK_EX

            if i > (height-h):
                r.append(color + Back.BLACK + '▓' + Fore.LIGHTWHITE_EX + Back.BLACK)
            else:
                r.append(Fore.LIGHTWHITE_EX + Back.BLACK + ' ' + Back.BLACK)
            col += 1

        map.append(r)

    return map

width = 0
height = 0

def customizeWidth():
    global width
    clear()
    choice = input(Fore.LIGHTWHITE_EX + "Input the desired width of your world: ")
    try:
        width = int(choice)
    except:
        customizeWidth()

def customizeHeight():
    global height
    clear()
    print("Width: " + str(width))
    choice = input(Fore.LIGHTWHITE_EX + "Input the desired height of your world: ")
    try:
        height = int(choice)
    except:
        customizeHeight()

customizeWidth()
customizeHeight()
world = generateMap(width, height)
you.x = int(math.floor(len(world[0]) / 2))
you.y = len(world)+1-heights[you.x]
if you.y < 2:
    you.y = 2
underPlayer = world[you.y][you.x]
underCursor = ' '
try:
    if '▓' in world[you.y+1][you.x]:
        world[you.y+1][you.x] = Fore.LIGHTGREEN_EX + '▓'
except:
    pass
#world[you.y][you.x] = you.icon

def drawWorld():
    global cursorActive
    global firstTimeDrawn
    global curY
    global modes
    global mode

    if firstTimeDrawn:
        firstTimeDrawn = False
        clear()
        for r in world:
            row = ""
            for c in r:
                row = row + c
            print(Fore.LIGHTWHITE_EX + Back.BLACK + "|" + row + Back.BLACK + Fore.LIGHTWHITE_EX + "|")

        sys.stdout.write('\x1b[' + str(len(world) - 1 - you.y) + 'A')
        curY = you.y
        sys.stdout.flush()
    else:
        #print('\003[0;' + str(len(world) - 1 - you.y) + 'H')
        #print('\r')
        #print(Cursor.POS(3,3))

        sys.stdout.write('\x1b[' + str(len(world) + 5) + 'A')
        print(Fore.LIGHTWHITE_EX + "mode: " + modes[mode])
        if cursorActive:
            if not (cursor.y == you.y and cursor.x == you.x):
                print("Selected Block: " + underCursor)
            else:
                #print("Selected Block: " + underCursor)
                print("Selected Block: " + you.icon)
        else:
            print("Selected Block:  ")

        for r in world:
            row = ""
            for c in r:
                row = row + c
            print(Fore.LIGHTWHITE_EX + Back.BLACK + "|" + row + Back.BLACK + Fore.LIGHTWHITE_EX + "|")
        sys.stdout.flush()
        drawHotbar()

def onGround():
    global world
    global you
    global underCursor
    if ('▓' in world[you.y + 1][you.x]) or ('▓' in underCursor and ' ' in world[cursor.y][cursor.x]):
        return True
    else:
        return False

def canMoveRight():
    global world
    global you
    if not '▓' in world[you.y][you.x + 1]:
        return True
    else:
        return False

def canMoveLeft():
    global world
    global you
    if not '▓' in world[you.y][you.x - 1]:
        return True
    else:
        return False

def fall():
    global underPlayer
    global you
    global world

    world[you.y][you.x] = underPlayer
    you.y += 1
    underPlayer = world[you.y][you.x]
    time.sleep(0.1)

def jumpRight():
    global underPlayer
    global you
    global world
    global inputPrimed
    global cursorActive
    global falling
    global timeSinceJump

    try:
        if inputPrimed and not ('▓' in world[you.y-1][you.x+1]) and not falling and (time.time()-timeSinceJump > 0.012):
            falling = True
            inputPrimed = False
            cursorActive = False
            world[you.y][you.x] = underPlayer
            you.y -= 1
            you.x += 1
            underPlayer = world[you.y][you.x]
            world[you.y][you.x] = you.icon
            drawWorld()
            #drawRow()
            time.sleep(0.1)

            while not onGround():
                world[you.y][you.x] = underPlayer

                if '▓' in world[you.y+1][you.x+1]:
                    if '▓' in world[you.y][you.x+1]:
                        you.y += 1
                    else:
                        you.x += 1
                else:
                    you.y += 1
                    you.x += 1

                underPlayer = world[you.y][you.x]
                world[you.y][you.x] = you.icon
                drawWorld()
                time.sleep(0.1)

            falling = False
            timeSinceJump = time.time()
    except:
        pass

def jumpLeft():
    global underPlayer
    global you
    global world
    global inputPrimed
    global cursorActive
    global falling
    global timeSinceJump

    try:
        if inputPrimed and not ('▓' in world[you.y-1][you.x-1]) and not falling and (time.time()-timeSinceJump > 0.012):
            falling = True
            inputPrimed = False
            cursorActive = False
            world[you.y][you.x] = underPlayer
            you.y -= 1
            you.x -= 1
            underPlayer = world[you.y][you.x]
            world[you.y][you.x] = you.icon
            drawWorld()
            time.sleep(0.1)

            while not onGround():
                world[you.y][you.x] = underPlayer

                if '▓' in world[you.y + 1][you.x - 1]:
                    if '▓' in world[you.y][you.x - 1]:
                        you.y += 1
                    else:
                        you.x -= 1
                else:
                    you.y += 1
                    you.x -= 1

                underPlayer = world[you.y][you.x]
                world[you.y][you.x] = you.icon
                drawWorld()
                time.sleep(0.1)

            falling = False
            timeSinceJump = time.time()
    except:
        pass

def jumpUp():
    global underPlayer
    global you
    global world
    global cursorActive
    global inputPrimed
    global falling

    try:
        if inputPrimed and not falling:
            falling = True
            inputPrimed = False
            cursorActive = False
            world[you.y][you.x] = underPlayer
            you.y -= 1
            underPlayer = world[you.y][you.x]
            world[you.y][you.x] = you.icon
            drawWorld()
            time.sleep(0.1)

            while not onGround():
                world[you.y][you.x] = underPlayer
                you.y += 1
                underPlayer = world[you.y][you.x]
                world[you.y][you.x] = you.icon
                drawWorld()

            falling = False
    except:
        pass

def moveCursorRight():
    global cursor
    global cursorActive
    global world
    global underCursor
    global underPlayer
    global inputPrimed
    if inputPrimed:
        inputPrimed = False
        if (not cursorActive) and you.x < len(world[you.y]) - 1:
            cursorActive = True
            cursor.x = you.x + 1
            cursor.y = you.y
            underCursor = world[cursor.y][cursor.x]
        else:
            if cursor.x < len(world[cursor.y]) - 1:
                world[cursor.y][cursor.x] = underCursor
                cursor.x += 1
                if cursor.y == you.y and cursor.x == you.x:
                    underCursor = underPlayer
                else:
                    underCursor = world[cursor.y][cursor.x]

def moveCursorLeft():
    global cursor
    global cursorActive
    global world
    global underCursor
    global underPlayer
    global inputPrimed
    if inputPrimed:
        inputPrimed = False
        if (not cursorActive) and you.x > 0:
            cursorActive = True
            cursor.x = you.x - 1
            cursor.y = you.y
            underCursor = world[cursor.y][cursor.x]
        else:
            if cursor.x > 0:
                world[cursor.y][cursor.x] = underCursor
                cursor.x -= 1
                if cursor.y == you.y and cursor.x == you.x:
                    underCursor = underPlayer
                else:
                    underCursor = world[cursor.y][cursor.x]

def moveCursorUp():
    global cursor
    global cursorActive
    global world
    global underCursor
    global underPlayer
    global inputPrimed
    if inputPrimed:
        inputPrimed = False
        if (not cursorActive) and you.y > 0:
            cursorActive = True
            cursor.x = you.x
            cursor.y = you.y - 1
            underCursor = world[cursor.y][cursor.x]
        else:
            if cursor.y > 0:
                world[cursor.y][cursor.x] = underCursor
                cursor.y -= 1
                if cursor.y == you.y and cursor.x == you.x:
                    underCursor = underPlayer
                else:
                    underCursor = world[cursor.y][cursor.x]

def moveCursorDown():
    global cursor
    global cursorActive
    global world
    global underCursor
    global underPlayer
    global inputPrimed
    if inputPrimed:
        inputPrimed = False
        if (not cursorActive) and you.y < len(world)-1:
            cursorActive = True
            cursor.x = you.x
            cursor.y = you.y + 1
            underCursor = world[cursor.y][cursor.x]
        else:
            if cursor.y < len(world)-1:
                world[cursor.y][cursor.x] = underCursor
                cursor.y += 1
                if cursor.y == you.y and cursor.x == you.x:
                    underCursor = underPlayer
                else:
                    underCursor = world[cursor.y][cursor.x]

def moveRight():
    global underPlayer
    global you
    global world
    global cursorActive
    global inputPrimed
    if inputPrimed:
        inputPrimed = False
        if you.x < len(world[you.y]) - 1 and canMoveRight():
            cursorActive = False
            world[you.y][you.x] = underPlayer
            you.x += 1
            underPlayer = world[you.y][you.x]

def moveLeft():
    global underPlayer
    global you
    global world
    global cursorActive
    global inputPrimed
    if inputPrimed:
        inputPrimed = False
        if you.x > 0 and canMoveLeft():
            cursorActive = False
            world[you.y][you.x] = underPlayer
            you.x -= 1
            underPlayer = world[you.y][you.x]
def cursorAction():
    global mode
    global underCursor
    global cursor
    global world
    global inputPrimed
    if inputPrimed:
        inputPrimed = False
        if mode == 1:
            underCursor = ' '
            world[cursor.y][cursor.x] = ' '
        elif mode == 0:
            if ' ' in underCursor:
                underCursor = Fore.YELLOW + '▓'
                world[cursor.y][cursor.x] = Fore.YELLOW + '▓'

def drawHotbar():
    global you
    print(Fore.LIGHTWHITE_EX + "╔═╗"*10)
    row = ""
    for i in you.hotBar:
        row = row + "║" + i + Fore.LIGHTWHITE_EX + "║"
    print(Fore.LIGHTWHITE_EX + "╚═╝"*10)

while not done:
    #world = generateMap(30, 10)
    if you.x >= len(world[0]):
        you.x = len(world[0])-1
    elif you.x <= 0:
        you.x = 0

    if you.y > len(world):
        you.y = len(world)-3

    if mode > len(modes)-1:
        mode = 0
    elif mode < 0:
        mode = len(modes)-1

    world[you.y][you.x] = you.icon

    if cursorActive:
        world[cursor.y][cursor.x] = Back.CYAN + ' ' + Back.BLACK
    else:
        if not (cursor.y == you.y and cursor.x == you.x):
            world[cursor.y][cursor.x] = underCursor

    if (Back.CYAN + ' ') in underPlayer:
        underPlayer = underCursor

    if '▓' in underPlayer:
        underPlayer = ' '

    drawWorld()

    try:
        if not onGround():
            fall()
        else:
            event = keyboard.read_event()
    except:
        event = keyboard.read_event()

    '''
    if event.event_type == keyboard.KEY_DOWN and event.name == 'right':
        if you.x < len(testVector)-1:
            testVector[you.x] = ' '
            you.x += 1
    '''

    keyboard.add_hotkey('space+d', lambda: jumpRight())
    keyboard.add_hotkey('d', lambda: moveRight())
    keyboard.add_hotkey('space+a', lambda: jumpLeft())
    keyboard.add_hotkey('a', lambda: moveLeft())
    keyboard.add_hotkey('space+w', lambda: jumpUp())
    keyboard.add_hotkey('shift+d', lambda: moveCursorRight())
    keyboard.add_hotkey('shift+a', lambda: moveCursorLeft())
    keyboard.add_hotkey('shift+w', lambda: moveCursorUp())
    keyboard.add_hotkey('shift+s', lambda: moveCursorDown())
    keyboard.add_hotkey('f', lambda: cursorAction())
    keyboard.add_hotkey('left', lambda: modeSelect(-1))
    keyboard.add_hotkey('right', lambda: modeSelect(1))

    if keyboard.is_pressed('esc'):
        clear()
        print("done!")
        done = True

    inputPrimed = True
