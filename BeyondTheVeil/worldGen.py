import random
import colorama
from colorama import *
import room
import pathfinding
import monsters
import items

#colors
green = Fore.GREEN
light_green = Fore.LIGHTGREEN_EX
red = Fore.RED
yellow = Fore.YELLOW
light_yellow = Fore.LIGHTYELLOW_EX
blue = Fore.BLUE
light_blue = Fore.LIGHTBLUE_EX
white = Fore.WHITE
gray = Fore.LIGHTBLACK_EX
light_gray = Fore.WHITE
magenta = Fore.MAGENTA
light_magenta = Fore.LIGHTMAGENTA_EX

all_tile_maps = [] #array of 2d maps for each tile on the world map
all_dungeon_maps = [] #array of 2d maps for each dungeon
biomes_map = []
overworld = []
dungeon_locations = []
dungeon_seeds = []
dungeon_maps = {}
seeds = []
adjL = {}

rooms = []
doors = []
width = 100
height = 23
hallways = []
allInsideRoomNodes = []
minRooms = 6
maxRooms = 9
floorMonstersSpawned = False

biomes = {
    "Grasslands": [green + ',', green + '„', light_green + ',', light_green + '„'],
    "Desert": [light_yellow + ',' + Style.RESET_ALL, light_yellow + '„'],
    "Forest": [light_green + "⁋" + Style.RESET_ALL, light_green + "¶" + Style.RESET_ALL, light_green + "⁂" + Style.RESET_ALL, light_green + "¥" + Style.RESET_ALL],
    "Jungle": [green + "⁋" + Style.RESET_ALL, green + "¶" + Style.RESET_ALL, green + "⁂" + Style.RESET_ALL, green + "¥" + Style.RESET_ALL],
    "Lake": [light_blue + "~" + Style.RESET_ALL],
    "Ocean": [blue + "~" + Style.RESET_ALL],
    "Swamp": [gray + "⁋" + Style.RESET_ALL, gray + "¶" + Style.RESET_ALL, gray + "⁂" + Style.RESET_ALL, gray + "¥" + Style.RESET_ALL],
    "Mountain": [white + "^" + Style.RESET_ALL],
    "Settled": [magenta + "Æ" + Style.RESET_ALL]
}

forest_tiles = [light_green + ",", light_green + "„"]
jungle_tiles = [green + "¥", light_green + ",", light_green + "„", green + ',', green + '„']
swamp_tiles = [green + "¥", green + ",", green + "„", gray + ',', gray + '„', light_gray + ',', light_gray + '„']

# Helpers

def pointCollidingWithAnyWall(point):
    global rooms

    for room in rooms:
        if (point[0] <= (room.x + room.width)) and (point[0] >= room.x):
            if (point[1] == room.y) or (point[1] == (room.y+room.height)):
                return True
        if (point[1] <= (room.y + room.height)) and (point[1] >= room.y):
            if (point[0] == room.x) or (point[0] == (room.x+room.width)):
                return True

    return False

def pointCollidingWithAnyDoor(point):
    global doors

    for door in doors:
        if point[0] == door[0] and point[1] == door[1]:
            return True

    return False

def pointCollidingWithAnyHallway(point):
    global hallways

    for hall in hallways:
        for n in hall:
            if point[0] == n[0] and point[1] == n[1]:
                return True

    return False

def allPointsInRooms(world):
    global allInsideRoomNodes
    global rooms
    global doors

    allInsideRoomNodes = []
    y = 0
    for r in world:
        x = 0
        for c in r:
            for ro in rooms:
                if x > ro.x and x < (ro.x+ro.width):
                    if y > ro.y and y < (ro.y+ro.height):
                        allInsideRoomNodes.append([x,y])
            x += 1
        y += 1

def numWallsAtPoint(point):
    global rooms

    num = 0

    for room in rooms:
        if (point[0] <= (room.x + room.width)) and (point[0] >= room.x):
            if (point[1] == room.y) or (point[1] == (room.y+room.height)):
                num += 1
        if (point[1] <= (room.y + room.height)) and (point[1] >= room.y):
            if (point[0] == room.x) or (point[0] == (room.x+room.width)):
                num += 1

    return num

def roomsColliding(room1):
    global rooms

    left = room1.x
    right = room1.x+room1.width
    top = room1.y
    bottom = room1.y+room1.height

    for room in rooms:
        if room1.id != room.id:
            left2 = room.x
            right2 = room.x + room.width
            top2 = room.y
            bottom2 = room.y + room.height

            if(right >= left2-1 and right <= right2) or (left <= right2+1 and left >= left2):
                if(top <= bottom2+1 and top >= top2) or (bottom >= top2-1 and bottom <= bottom2):
                    return True
                if(top <= top2 and bottom >= bottom2):
                    return True

    return False

def validLevel():
    global rooms

    for room in rooms:
        if roomsColliding(room):
            return False

    return True

def validLevel2(world):
    global rooms

    for r in world:
        for c in r:
            if numWallsAtPoint([c, r]) > 1:
                return False

    return True

def adjacentTiles(point, width, height):
    global adjL

    tiles = []
    adjL = {}

    if point[0]-1 > 0 and point[1]-1 > 0 and point[0]-1 < width and point[1]-1 < height:
            tiles.append((point[0]-1, point[1]-1))
            adjL["up left"] = (point[0]-1, point[1]-1)
    else:
        tiles.append(None)
        adjL["up left"] = None

    if point[0] + 1 > 0 and point[1] - 1 > 0 and point[0] + 1 < width and point[1] - 1 < height:
        tiles.append((point[0] + 1, point[1] - 1))
        adjL["up right"] = (point[0] + 1, point[1] - 1)
    else:
        tiles.append(None)
        adjL["up right"] = None

    if point[0] - 1 > 0 and point[1] + 1 > 0 and point[0] - 1 < width and point[1] + 1 < height:
        tiles.append((point[0] - 1, point[1] + 1))
        adjL["down left"] = (point[0] - 1, point[1] + 1)
    else:
        tiles.append(None)
        adjL["down left"] = None

    if point[0] + 1 > 0 and point[1] + 1 > 0 and point[0] + 1 < width and point[1] + 1 < height:
        tiles.append((point[0] + 1, point[1] + 1))
        adjL["down right"] = (point[0] + 1, point[1] + 1)
    else:
        tiles.append(None)
        adjL["down right"] = None

    return tiles

def countAdjacentBiomes(point, width, height):
    global biomes_map

    biome_counts = {biome: 0 for biome in biomes.keys()}

    adjacent = adjacentTiles(point, width, height)

    for tile in adjacent:
        if tile is not None:
            x, y = tile
            #print(f"Biome at adjacent tile: {biomes_map[y][x]}")
            #print(f"Biomes Len: {len(biomes_map)}")
            biome = biomes_map[y][x]
            biome_counts[biome] += 1

    return biome_counts

def findHighestInDict(d):
    max_key = None
    max_value = float('-inf')

    for key, value in d.items():
        if value > max_value:
            max_value = value
            max_key = key

    return max_key

def generate_world(seed, width, height):
    global biomes_map
    global overworld
    global dungeon_locations
    random.seed(seed)
    world = []
    biomes_map = []
    biome = random.choice(list(biomes.keys()))
    old_biome = biome
    for y in range(height):
        row = []
        biome_row = []
        dungeon_row = []
        seed_row = []
        for x in range(width):
            biome = random.choice(list(biomes.keys())) if random.randint(1,3) == 3 else biome 
            if (biome == "Settled" and random.randint(1, 10) > 2) or old_biome == "Settled":
                ks = list(biomes.keys())
                ks.remove("Settled")
                biome = random.choice(ks)
                    
            tile = random.choice(biomes[biome])
            row.append(tile)
            biome_row.append(biome)
            
            if random.randint(1, 10) == 1:
                dungeon_row.append((random.randint(0, width - 1), random.randint(0, height - 1)))
                seed_row.append(random.randint(0, 1000000))
                dungeon_maps[(x, y)] = []
            else:
                dungeon_row.append(None)
                seed_row.append(None)
                dungeon_maps[(x, y)] = None

            old_biome = biome
        world.append(row)
        biomes_map.append(biome_row)
        dungeon_locations.append(dungeon_row)
        dungeon_seeds.append(seed_row)

    for y in range(height):
        for x in range(width):
            adjBiomes = countAdjacentBiomes((x, y), width, height)
            most_common = findHighestInDict(adjBiomes)
            if random.randint(1, 3) < 3:
                biomes_map[y][x] = most_common
                world[y][x] = random.choice(biomes[most_common])

    #generate_tile_maps(world, width, height)
    #print(f"Biomes len: {len(biomes_map)}")
    fill_tile_maps(world, width, height)
    overworld = world

    return world

# generates the zoomed in map for a tile
def generate_tilemap(biome, width, height, x, y):
    global seeds

    random.seed(seeds[y][x])

    tile_map = []
    for y in range(height):
        row = []
        for x in range(width):
            tile = random.choice(biomes[biome])

            if biome == "Forest":
                if random.randint(1, 10) == 1:
                    tile = yellow + "O" + Style.RESET_ALL
                else:
                    tile = random.choice(forest_tiles)
            elif biome == "Jungle":
                if random.randint(1, 10) == 1:
                    tile = yellow + "O" + Style.RESET_ALL
                else:
                    tile = random.choice(jungle_tiles)
            elif biome == "Swamp":
                if random.randint(1, 10) == 1:
                    tile = gray + "O" + Style.RESET_ALL
                else:
                    tile = random.choice(swamp_tiles)

            row.append(tile)
        tile_map.append(row)
    return tile_map

# not necessary anymore and too slow
def generate_tile_maps(world, width, height):
    global all_tile_maps
    global biomes_map
    global seeds

    all_tile_maps = [] #reset tile maps
    for world_y in range(height): #iterate through world map rows
        world_row = []
        for world_x in range(width): #iterate through world map columns
            #create a tilemap for the current tile and append it to the world row
            world_row.append(generate_tilemap(biomes_map[world_y][world_x], width, height, world_x, world_y))
        all_tile_maps.append(world_row)

# fills each tile map with None to be generated later
def fill_tile_maps(world, width, height):
    global all_tile_maps
    global biomes_map
    global seeds

    all_tile_maps = [] #reset tile maps
    all_dungeon_maps = [] #reset dungeon maps
    for world_y in range(height): #iterate through world map rows
        world_row = []
        seeds_row = []
        for world_x in range(width): #iterate through world map columns
            world_row.append(None)
            seeds_row.append(random.randint(0, 1000000))
        all_tile_maps.append(world_row)
        all_dungeon_maps.append(world_row.copy())
        seeds.append(seeds_row)

# Dungeon Generation
def generateLevel(location):
    global rooms
    global width
    global height
    global minRooms
    global maxRooms
    global floorMonstersSpawned
    # place 1 room starting at a random x and y, then randomly place one randomly along that x at
    # a similar y value. Then place one at a different y

    #create a list of rooms then have the map draw them from the list in a renderMap() function

    rng = random.Random(dungeon_seeds[location[1]][location[0]])

    floorMonstersSpawned = False

    while True:
        rooms = []
        roomsDone = 0
        roomsRejected = 0
        rtd = rng.randint(minRooms, maxRooms)

        while roomsDone < rtd:
            if roomsRejected > 5:
                break

            topLeftX = rng.randint(1, width - 5)
            topLeftY = rng.randint(1, height - 5)

            maxRoomWidth = (width - 5) - topLeftX
            if maxRoomWidth > 12:
                maxRoomWidth = 12
            elif maxRoomWidth < 4:
                maxRoomWidth = 4

            maxRoomHeight = (height - 5) - topLeftY
            if maxRoomHeight > 8:
                maxRoomHeight = 8
            elif maxRoomHeight < 4:
                maxRoomHeight = 4

            r = room.Room(rng.randint(4, maxRoomWidth), rng.randint(4, maxRoomHeight), False, topLeftX, topLeftY, roomsDone)
            if roomsColliding(r):
                roomsRejected += 1
            else:
                rooms.append(r)
                roomsDone += 1

        if roomsDone == rtd and validLevel():
            break

    return

def renderLevel(depth, enemies, floorItems, location):
    global rooms
    global doors
    global width
    global height
    global allInsideRoomNodes
    global floorMonstersSpawned
    global dungeon_maps
    global hallways

    generateDoors()

    if dungeon_maps.get(location) is None:
        dungeon_maps[location] = []
    else:
        dungeon_maps[location].clear()

    hallways = []

    y = 0
    for r in range(height):
        row = []
        x = 0
        for c in range(width):
            ch = ' '
            if pointCollidingWithAnyWall([x, y]):
                ch = Fore.YELLOW + '#' + Fore.LIGHTWHITE_EX

            if pointCollidingWithAnyDoor([x, y]):
                ch = Fore.YELLOW + '+' + Fore.LIGHTWHITE_EX

            row.append(ch)
            x += 1

        dungeon_maps[location].append(row)
        y += 1

    allPointsInRooms(dungeon_maps[location])

    monsters.spawnMonsters(depth, enemies, floorMonstersSpawned)
    items.spawnItems(depth, floorItems)
    floorMonstersSpawned = True
    for n in allInsideRoomNodes:
        dungeon_maps[location][n[1]][n[0]] = Fore.LIGHTGREEN_EX + '.' + Fore.LIGHTWHITE_EX


    for m in enemies:
        monsters.placeMonster(m, dungeon_maps[location], allInsideRoomNodes)

    for i in floorItems:
        items.placeItem(i, dungeon_maps[location], allInsideRoomNodes)

    #print("doors: " + str(doors))
    generateHallways(location)

    y = 0
    for r in range(height):
        x = 0
        for c in range(width):
            if pointCollidingWithAnyHallway([x, y]) and not pointCollidingWithAnyDoor([x, y]):
                ch = Fore.LIGHTBLACK_EX + '▓' + Fore.LIGHTWHITE_EX
                dungeon_maps[location][y][x] = ch
            x += 1

        y += 1


    #print("len: " + str(len(world[0])))

def generateHallways(location):
    global hallways
    global doors
    global dungeon_maps

    # create paths between doors
    for i in range(len(doors)):
        if i + 1 < len(doors):
            door = doors[i]
            door2 = doors[i + 1]

            dungeon_maps[location][door[1]][door[0]] = '+'
            dungeon_maps[location][door2[1]][door2[0]] = '+'

            hallways.append(pathfinding.getPath(door, door2, dungeon_maps[location], False, False))

def generateDoors():
    global rooms
    global doors
    global hallways
    global dungeon_maps

    doors = []

    # places doors
    for i in range(len(rooms)):
        if i + 1 < len(rooms):
            # places a door on room1's bottom wall if room2 is below it, places door on top wall otherwise
            if rooms[i + 1].y > rooms[i].y:
                door = [random.randint(rooms[i].x + 1, rooms[i].x + rooms[i].width - 1), rooms[i].y + rooms[i].height]
                doors.append(door)
            else:
                door = [random.randint(rooms[i].x + 1, rooms[i].x + rooms[i].width - 1), rooms[i].y]
                doors.append(door)

            # places a door on room2's left wall if room2 is right of it, places door on right wall otherwise
            if rooms[i + 1].x > rooms[i].x:
                door = [rooms[i + 1].x,
                        random.randint(rooms[i + 1].y + 1, rooms[i + 1].y + rooms[i + 1].height - 1)]
                doors.append(door)
            else:
                door = [rooms[i + 1].x + rooms[i + 1].width,
                              random.randint(rooms[i + 1].y + 1, rooms[i + 1].y + rooms[i + 1].height - 1)]
                doors.append(door)

        #world[door[1]][door[0]] = ' '