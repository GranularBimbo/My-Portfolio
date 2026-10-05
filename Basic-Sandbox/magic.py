import random
import world
import entities as ent


# ==========================================
# Super Class
# ==========================================


class Spell():
    def __init__(self, name: str = "Default Spell", level: int = 1, type: str = "Arcane",
                 damage_dice: tuple = None, auto_hit: bool = False, save_type: str = None):
        self.name = name
        self.level = level
        self.type = type # Arcane or Holy
        self.damage_dice = damage_dice
        self.auto_hit = auto_hit
        self.save_type = save_type


    def cast(self, caster: ent.Creature, target: ent.Creature):
        world.console_log(f"{caster.name} casts {self.name} on {target.name}...")
        save = None

        if caster.in_combat:
            world.custom_log(log_tag=f"{caster.party}_combat_log", message=f"{caster.name} casts {self.name} on {target.name}...")

        if self.save_type is not None:
            save = target.save_vs(save_type=self.save_type)

        if not self.auto_hit:
            hit, roll = self.roll_hit(attacker=caster, target=target)
            crit = True if roll == 20 else False
        elif self.auto_hit or (save is not None and save != -1):
            hit = True
            crit = False

        return (hit, crit)


    def roll_hit(self, attacker: ent.Creature, target: ent.Creature):
        to_hit = attacker.THAC0 - target.AC
        roll = self.roll_dice((1,20))

        if roll >= to_hit or roll == 20:
            return (True, roll)
        
        return (False, roll)


    def roll_dice(self, dice):
        num, die = dice
        total = 0

        for i in range(0, num):
            total += random.randint(1,die)
        
        return total


# ==========================================
# Spells
# ==========================================


class Sleep(Spell):
    def __init__(self):
        super().__init__(name="Sleep", level=1, type="Arcane", auto_hit=True)

    def cast(self, caster: ent.Creature, target: ent.Creature):
        hit, crit = super().cast(caster=caster, target=target)

        if target.HD <= 5:
            world.console_log(f"{target.name} has been put to sleep!\n")
    
            if caster.in_combat:
                world.custom_log(log_tag=f"{caster.party}_combat_log", message=f"{target.name} has been put to sleep!\n")
            
            ent.apply_status(attacker=caster, target=target, status_name="Sleeping", 
                             save=False if self.save_type is None else True, save_type=self.save_type)


# ==========================================
# Spellbook
# ==========================================


spellbook = {
    "Sleep": Sleep()
}