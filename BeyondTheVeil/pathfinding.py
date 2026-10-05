import math
from collections import deque
from colorama import *

def isVisited(coords, visList):
    for n in visList:
        if coords[0] == n[0] and coords[1] == n[1]:
            return True
    return False

def equal(coords1, coords2):
    if coords1[0] == coords2[0] and coords1[1] == coords2[1]:
        return True
    return False

def validNode(coords, level, forMonster):
    #make it so that spots that are adjacent to walls are invalid to see how it looks
    if not forMonster:
        if level[coords[1]][coords[0]] == (Fore.YELLOW + '#' + Fore.LIGHTWHITE_EX) or level[coords[1]][coords[0]] == (Fore.LIGHTGREEN_EX + '.' + Fore.LIGHTWHITE_EX):
            return False
    else:
        if level[coords[1]][coords[0]] == (Fore.YELLOW + '#' + Fore.LIGHTWHITE_EX):
            return False
    return True

def tracePath(start, end, came_from):
    path = []
    curr = tuple(end)

    while curr != tuple(start):
        path.append(list(curr))
        curr = came_from.get(curr)
        if curr is None:
            return []  # No path found

    path.append(start)
    path.reverse()
    return path

def pointDist(p1, p2):
    x1, y1 = p1
    x2, y2 = p2
    return math.sqrt(math.pow(x2-x1, 2) + math.pow(y2-y1, 2))

#          [x,y] [x,y]
def getPath(start, end, level, diag_allowed, forMonster):
    queue = deque()
    queue.append(start)

    came_from = {tuple(start): None}
    width = len(level[0])
    height = len(level)

    # Directions: (dx, dy)
    directions = [(-1, 0), (0, -1), (1, 0), (0, 1)]
    if diag_allowed:
        directions += [(-1, -1), (1, -1), (1, 1), (-1, 1)]

    while queue:
        curr = queue.popleft()
        if curr == end:
            break

        for dx, dy in directions:
            nx, ny = curr[0] + dx, curr[1] + dy
            if 0 <= nx < width and 0 <= ny < height and validNode([nx,ny], level, forMonster):
                next_coord = (nx, ny)
                if next_coord not in came_from:
                    queue.append([nx, ny])
                    came_from[next_coord] = tuple(curr)

    return tracePath(start, end, came_from)

