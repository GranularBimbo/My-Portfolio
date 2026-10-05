import math
import sys

def move_cursor(row, col):
    print(f"\x1b[{row};{col}H", end="")

class Class:
    def __init__(self, name, Str, Dex, Con, Int, Wis, Cha):
        self.name = name
        self.Str = Str
        self.Dex = Dex
        self.Con = Con
        self.Int = Int
        self.Wis = Wis
        self.Cha = Cha
        #Table Contents: XP, HD, THAC0, D, W, P, B, S
        self.progTable = []
        self.MAX_TABLE_HEIGHT = 40   # safe buffer
        self.TABLE_WIDTH = 80        # match your table width
        self.initTable()
    
    def initTable(self):
        if self.name == "Fighter":
            self.progTable = [
                [2000, [1,8], 19, 12, 13, 14, 15, 16],
                [4000, [2,8], 19, 12, 13, 14, 15, 16],
                [8000, [3,8], 19, 12, 13, 14, 15, 16],
                [16000, [4,8], 17, 10, 11, 12, 13, 14],
                [32000, [5,8], 17, 10, 11, 12, 13, 14],
                [64000, [6,8], 17, 10, 11, 12, 13, 14],
                [120000, [7,8], 14, 8, 9, 10, 10, 12],
                [240000, [8,8], 14, 8, 9, 10, 10, 12],
                [360000, [9,8], 14, 8, 9, 10, 10, 12],
                [480000, [10,8], 12, 6, 7, 8, 8, 10],
                [600000, [11,8], 12, 6, 7, 8, 8, 10],
                [720000, [12,8], 12, 6, 7, 8, 8, 10],
                [840000, [13,8], 10, 4, 5, 6, 5, 8],
                [math.inf, [14,8], 10, 4, 5, 6, 5, 8]
            ]
        elif self.name == "Dwarf":
            self.progTable = [
                [2200, [1,8], 19, 8, 9, 10, 13, 12],
                [4400, [2,8], 19, 8, 9, 10, 13, 12],
                [8800, [3,8], 19, 8, 9, 10, 13, 12],
                [17000, [4,8], 17, 6, 7, 8, 10, 10],
                [35000, [5,8], 17, 6, 7, 8, 10, 10],
                [70000, [6,8], 17, 6, 7, 8, 10, 10],
                [140000, [7,8], 14, 4, 5, 6, 7, 8],
                [270000, [8,8], 14, 4, 5, 6, 7, 8],
                [400000, [9,8], 14, 4, 5, 6, 7, 8],
                [530000, [10,8], 12, 2, 3, 4, 4, 6],
                [660000, [11,8], 12, 2, 3, 4, 4, 6],
                [math.inf, [12,8], 12, 2, 3, 4, 4, 6]
            ]
        elif self.name == "Magic-User":
            pass

    def clear_table_area(self, start_row, start_col, height):
        for i in range(height):
            move_cursor(start_row + i, start_col)
            print("\033[K", end="")

    def draw_title(self, start_row, start_col, width):
        title = "Progression"
        padded = title.center(width)

        move_cursor(start_row - 2, start_col)
        print(padded, end="")

    def drawClassTable(self, start_row=2, start_col=50):
        headers = ["LV", "XP", "HD", "TH", "D", "W", "P", "B", "S"]

        # Adjust widths (XP needs more space)
        col_widths = [4, 10, 6, 4, 4, 4, 4, 4, 4]

        def format_row(row):
            cells = []
            for val, w in zip(row, col_widths):
                cells.append(f"{str(val):^{w}}")
            return "│" + "│".join(cells) + "│"

        def border(left, mid, right):
            return left + mid.join("─" * w for w in col_widths) + right

        def move(row, col):
            print(f"\x1b[{row};{col}H", end="")

        # --- HEADER ---
        move(start_row, start_col)
        print(border("┌", "┬", "┐"))

        move(start_row + 1, start_col)
        print(format_row(headers))

        move(start_row + 2, start_col)
        print(border("├", "┼", "┤"))

        # --- DATA ---
        row_y = start_row + 3

        for i, entry in enumerate(self.progTable):
            xp, hd, thac0, d, w, p, b, s = entry

            hd_str = f"{hd[0]}d{hd[1]}"
            xp_str = xp if xp != math.inf else "∞"

            row = [i + 1, xp_str, hd_str, thac0, d, w, p, b, s]

            move(row_y, start_col)
            print(format_row(row))
            row_y += 1

        # --- FOOTER ---
        move(row_y, start_col)
        print(border("└", "┴", "┘"))


#Classes
fighter = Class("Fighter", 0, 0, 0, 0, 0, 0)
dwarf = Class("Dwarf", 0, 0, 9, 0, 0, 0)