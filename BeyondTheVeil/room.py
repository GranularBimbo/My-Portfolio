

class Room():
    def __init__(self, width, height, dark, x, y, id):
        self.width = width
        self.height = height
        self.dark = dark
        self.x = x
        self.y = y
        self.discovered = True
        self.id = id

