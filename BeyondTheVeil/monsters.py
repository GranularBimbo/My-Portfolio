import random
import pathfinding
import creature

dist_old = 0
dist_new = 0

v = ["Vile Amalgam", "Flesh", 'V', 10, 10, 10, 10, 10, 10, 9, 19, [1,4]]
g = ["Gut Spitter", "Flesh", 'G', 10, 10, 10, 10, 10, 10, 9, 19, [1,4]]


allMonsters = [g[2], v[2]]
OTF = [g, v] # one through four
adjL = {}

def adjacentTiles(level, point, diag_allowed, width, height):
    global adjL

    tiles = []
    adjL = {}

    if diag_allowed:
        if point[0]-1 > 0 and point[1]-1 > 0 and point[0]-1 < width and point[1]-1 < height:
            tiles.append(level[point[1]-1][point[0]-1])
            adjL["up left"] = level[point[1]-1][point[0]-1]

        if point[0] + 1 > 0 and point[1] - 1 > 0 and point[0] + 1 < width and point[1] - 1 < height:
            tiles.append(level[point[1] - 1][point[0] + 1])
            adjL["up right"] = level[point[1] - 1][point[0] + 1]

        if point[0] - 1 > 0 and point[1] + 1 > 0 and point[0] - 1 < width and point[1] + 1 < height:
            tiles.append(level[point[1] + 1][point[0] - 1])
            adjL["down left"] = level[point[1] + 1][point[0] - 1]

        if point[0] + 1 > 0 and point[1] + 1 > 0 and point[0] + 1 < width and point[1] + 1 < height:
            tiles.append(level[point[1] + 1][point[0] + 1])
            adjL["down right"] = level[point[1] + 1][point[0] + 1]

    if point[0] > 0 and point[1] - 1 > 0 and point[0] < width and point[1] - 1 < height:
        tiles.append(level[point[1]-1][point[0]])
        adjL["up"] = level[point[1]-1][point[0]]

    if point[0]-1 > 0 and point[1] > 0 and point[0]-1 < width and point[1] < height:
        tiles.append(level[point[1]][point[0]-1])
        adjL["left"] = level[point[1]][point[0]-1]

    if point[0]+1 > 0 and point[1] > 0 and point[0]+1 < width and point[1] < height:
        tiles.append(level[point[1]][point[0]+1])
        adjL["right"] = level[point[1]][point[0]+1]

    if point[0] > 0 and point[1]+1 > 0 and point[0] < width and point[1]+1 < height:
        tiles.append(level[point[1]+1][point[0]])
        adjL["down"] = level[point[1]+1][point[0]]

    return tiles

def spawnMonsters(depth, enemyList, floorMonstersSpawned):
    if not floorMonstersSpawned:
        numMonsters = random.randint(3, 7) + depth
    else:
        numMonsters = 1

    for i in range(numMonsters):
        if depth < 3:
            m = random.randint(0, len(OTF)-1)
            c = creature.Creature(OTF[m][0], OTF[m][1], OTF[m][2], OTF[m][3], OTF[m][4], OTF[m][5], OTF[m][6], OTF[m][7], OTF[m][8], OTF[m][9], OTF[m][10], OTF[m][11])
            enemyList.append(c)

def placeMonster(monster, level, allInsideRoomNodes):
    startPos = allInsideRoomNodes[random.randint(0, len(allInsideRoomNodes) - 1)]
    monster.x, monster.y = startPos[0], startPos[1]
    if '.' in level[monster.y][monster.x]:
        monster.under = level[monster.y][monster.x]
        level[monster.y][monster.x] = monster.icon
    else:
        placeMonster(monster, level, allInsideRoomNodes)

def monsterPathUpdates(player, enemyList):
    global dist_old
    for m in enemyList:
        if m.pathToPlayer:
            dist_old = pathfinding.pointDist(m.pathToPlayer[-1], [player.x, player.y])
        else:
            dist_old = float("inf")  # or just skip

def monsterMovement(level, roomList, player, enemyList, sprinting, sprintsDone, width, height):
    global dist_old
    global dist_new

    for m in enemyList:
        if not m.sleeping and m.findRoomInsideID(roomList) == player.findRoomInsideID(roomList):
            m.chasing = True

        oldX = m.x
        oldY = m.y

        if m.chasing:
            playerAdjacent = False
            for a in adjacentTiles(level, [m.x,m.y], True, width, height):
                if '@' in a:
                    playerAdjacent = True

            if not playerAdjacent:
                if m.pathToPlayer:
                    dist_new = pathfinding.pointDist(m.pathToPlayer[-1], [player.x, player.y])
                else:
                    dist_new = float("inf")  # or just skip
                if sprintsDone >= len(m.pathToPlayer) and sprinting:
                    m.pathToPlayer = pathfinding.getPath([m.x, m.y], [player.x, player.y], level, True, True)
                    m.pathToPlayer.pop(0)
                    #m.pathTail = m.pathToPlayer[len(m.pathToPlayer)-1]
                elif not sprinting:
                    m.pathToPlayer = pathfinding.getPath([m.x, m.y], [player.x, player.y], level, True, True)
                    m.pathToPlayer.pop(0)
                    #m.pathTail = m.pathToPlayer[len(m.pathToPlayer) - 1]

                if len(m.pathToPlayer) > 1:
                    if not sprinting:
                        m.setPos(m.pathToPlayer.pop(0), level, enemyList, player, width, height)
                        if m.x == oldX and m.y == oldY:
                            #m.chasing = False
                            pass
                    else:
                        #print("sprints done: " + str(sprintsDone))
                        #print("len(path): " + str(len(m.pathToPlayer)) + " name: " + m.name)
                        m.setPos(m.pathToPlayer.pop(0), level, enemyList, player, width, height)
                        if m.x == oldX and m.y == oldY:
                            #m.chasing = False
                            pass
            else:
                m.attack(player)

