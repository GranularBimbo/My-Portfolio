import random

# ==========================================
# Classes
# ==========================================

class Item():
    def __init__(self, name: str = "Default Item", value: int = 1, size: int = 1,
                 type: str = "Default Item"):
        self.name = name
        self.value = value
        self.size = size
        self.type = type

    def silver_cost(self):
        return self.value*10
    
    def roll_dice(self, dice):
        num, die = dice
        total = 0

        for i in range(0, num):
            total += random.randint(1,die)

        return total
    
class Weapon(Item):
    def __init__(self, name: str = "Default Weapon", value: int = 1, size: int = 1,
                 dmg_dice: tuple = (1,4), range: int = 10, ammo: str = "",
                 type: str = "Weapon"):
        super().__init__(name, value, size, type)
        self.dmg_dice = dmg_dice
        self.range = range
        self.ammo = ammo

    def roll_damage(self, crit: bool = False):
        num, die = self.dmg_dice
        return self.roll_dice(self.dmg_dice) if not crit \
            else self.roll_dice((num*2, die))
    
class Armor(Item):
    def __init__(self, name: str = "Default Armor", value: int = 1, size: int = 1,
                 AC: int = 9, is_metal: bool = True, type: str = "Armor"):
        super().__init__(name, value, size, type)
        self.AC = AC
        self.is_metal = is_metal


# ==========================================
# Weapons
# ==========================================

fist = Weapon(name="Fist", value=0, size=0, dmg_dice=(1,2), range=10)
sword = Weapon(name="Sword", value=10, size=1, dmg_dice=(1,8), range=10)
bow = Weapon(name="Bow", value=10, size=2, dmg_dice=(1,6), range=30)

# ==========================================
# Armor
# ==========================================

no_armor = Armor(name="No Armor", value=0, size=0, AC=9, is_metal=False)
platemail = Armor(name="Plate Mail", value=60, AC=3)
chainmail = Armor(name="Chain Mail", value=20, AC=5)

