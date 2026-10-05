import random
import items
import world
import math

all_creatures = {}
magic = None

# ==========================================
# Living Thing Classes
# ==========================================

class Creature():
    def __init__(self, name: str = "Default Creature", HD: int = 1, HD_type: int = 8, Str: int = 10, 
                 Dex: int = 10, Con: int = 10, Int: int = 10, Wis: int = 10, Cha: int = 10,
                 AC: int = 9, THAC0: int = 19, id: int = -1, roll_stats_on_creation: bool = False,
                 weapon: items.Weapon = items.fist, gp: int = 0, sp: int = 0, cp: int = 0,
                 pp: int = 0, ep: int = 0, armor: items.Armor = items.no_armor, save_death: int = 0,
                 save_wand: int = 0, save_para: int = 0, save_breath: int = 0, save_spell: int = 0,
                 languages: list = [], skills: dict = {}, attacks = None, 
                 passives = None, combat_row: str = "Front"):
        
        global all_creatures
        
        self.name = name
        self.HD = HD
        self.HD_type = HD_type
        self.Str = Str
        self.Dex = Dex
        self.Con = Con
        self.maxHP = self.roll_dice((self.HD, self.HD_type))
        self.HP = self.maxHP
        self.Int = Int
        self.Wis = Wis
        self.Cha = Cha
        self.AC = AC
        self.THAC0 = THAC0
        self.party = ""
        self.in_combat = False
        self.id = id
        self.type = "Creature"
        self.spells = {} # "{Spell Name}": Casts (int)

        self.combat_row = combat_row


        self.str_mod = 0
        self.dex_mod = 0
        self.con_mod = 0
        self.int_mod = 0
        self.wis_mod = 0
        self.cha_mod = 0

        self.refresh_combat_callback = None
        self.refresh_combat_args = []
        

        if self.name == "True Fighter":
            self.weapon = items.sword
        elif self.name == "True Ranger":
            self.weapon = items.bow
        else:
            self.weapon = weapon

        self.gp = gp
        self.sp = sp
        self.cp = cp
        self.pp = pp
        self.ep = ep
        self.save_death = save_death
        self.save_wand = save_wand
        self.save_para = save_para
        self.save_breath = save_breath
        self.save_spell = save_spell
        self.languages = languages
        self.skills = skills
        self.status_effects = {}
        self.attacks = attacks
        self.passives = passives
        self.inv = {}
        self.row = "Front" # Front or Back

        self.saves = {}
        self.attributes = {}
        self.update_saves()
        self.update_attributes()

        if self.id == -1:
            self.roll_id()
            all_creatures[self.id] = self
        else:
            if all_creatures.get(self.id) is None:
                all_creatures[self.id] = self

        if roll_stats_on_creation:
            self.roll_stats()


    def update_saves(self):
        self.saves = {"D": ("death", self.save_death), "W": ("wand", self.save_wand), 
                      "P": ("paralysis", self.save_para), "B": ("breath", self.save_breath), 
                      "S": ("spell", self.save_spell)}
        
    
    def update_attributes(self):
        self.attributes = {"Str": self.Str, "Dex": self.Dex, "Con": self.Con,
                           "Int": self.Int, "Wis": self.Wis, "Cha": self.Cha}


    def skill_check(self, skill: str = "Str"):
        if self.attributes.get(skill) is not None and skill != "Cha":
            target_num = self.attributes.get(skill)
            roll = self.roll_dice((1,20))

            if roll <= target_num:
                pass # success
            else:
                pass # failure


    def save_vs(self, save_type: str = "D"):
        if self.saves.get(save_type) is not None:
            save_name, save_val = self.saves.get(save_type)

            roll = self.roll_dice((1,20))+self.wis_mod if save_type=="S" else \
                self.roll_dice((1,20))
            
            if roll >= save_val:
                world.console_log(message=f"Roll: {roll} [!] {self.name} saved vs {save_name}!\n")

                if self.in_combat:
                    world.custom_log(log_tag=f"{self.party}_combat_log", message=f"Roll: {roll} [!] {self.name} saved vs {save_name}!\n")

                return True
            else:
                world.console_log(message=f"Roll: {roll} [!] {self.name} failed to save vs {save_name}!\n")

                if self.in_combat:
                    world.custom_log(log_tag=f"{self.party}_combat_log", message=f"Roll: {roll} [!] {self.name} failed to save vs {save_name}!\n")

                return False
            
        return -1


    def combat_turn(self, target: Creature, action: str = "Attack", surprised: bool = False, 
                    surprised_enemy: bool = False):
        
        self.in_combat = True
        
        if self.status_effects.get("Paralyzed") is None and self.status_effects.get("Sleeping") is None:
            if action == "Attack":
                if self.attacks is not None and len(self.attacks) > 0:
                    atk_idx = random.randint(0, len(self.attacks)-1)
                    self.attacks[atk_idx].execute(attacker=self, target=target)

                else:
                    self.attack(target)

            elif "Cast" in action:
                spell_str = action[5:]
                spell_obj = get_magic().spellbook.get(spell_str)
                spell_obj.cast(caster=self, target=target)
            
        elif self.status_effects.get("Paralyzed") is not None:
            world.console_log(f"[!] {self.name} is paralyzed for {self.status_effects.get("Paralyzed")} more rounds!\n")
            world.custom_log(log_tag=f"{self.party}_combat_log", 
                             message=f"[!] {self.name} is paralyzed for {self.status_effects.get("Paralyzed")} more rounds!\n")
            
            self.status_effects["Paralyzed"] -= 1
            if self.status_effects.get("Paralyzed") <= 0:
                del self.status_effects["Paralyzed"]

        elif self.status_effects.get("Sleeping") is not None:
            world.console_log(f"[!] {self.name} is sleeping for {self.status_effects.get("Sleeping")} more rounds!\n")
            world.custom_log(log_tag=f"{self.party}_combat_log", 
                                message=f"[!] {self.name} is sleeping for {self.status_effects.get("Sleeping")} more rounds!\n")
            
            self.status_effects["Sleeping"] -= 1
            if self.status_effects.get("Sleeping") <= 0:
                del self.status_effects["Sleeping"]

        self.refresh_combat_callback(*self.refresh_combat_args) if self.refresh_combat_callback is not None else None


    def attack(self, target: Creature):
        hit, roll = self.roll_hit(target=target)
        dmg = self.weapon.roll_damage(crit=(roll==20)) if hit else 0
        world.console_log(f"{self.name} attacks {target.name}...")
        world.custom_log(log_tag=f"{self.party}_combat_log", message=f"{self.name} attacks {target.name}...")

        if dmg > 0:
            target.HP -= dmg
            world.console_log(f"Roll: {roll} [!] {target.name} was hit for {dmg} damage!\n")
            world.custom_log(log_tag=f"{self.party}_combat_log", message=f"Roll: {roll} [!] {target.name} was hit for {dmg} damage!\n")
        else:
            world.console_log(f"Roll: {roll} | Miss!")
            world.custom_log(log_tag=f"{self.party}_combat_log", message=f"Roll: {roll} | Miss!\n")


    def roll_hit(self, target: Creature):
        to_hit = self.THAC0 - target.AC
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
    

    def roll_id(self):
        self.id = random.randint(0, 9999999)
        if all_creatures.get(self.id) is not None:
            self.roll_id()


    def roll_stats(self):
        self.Str = self.roll_dice((3,6))
        self.Dex = self.roll_dice((3,6))
        self.Con = self.roll_dice((3,6))
        self.Int = self.roll_dice((3,6))
        self.Wis = self.roll_dice((3,6))
        self.Cha = self.roll_dice((3,6))


class Character(Creature):
    def __init__(self, name: str = "Default Character", HD: int = 1, HD_type: int = 8, Str: int = 10, 
                 Dex: int = 10, Con: int = 10, Int: int = 10, Wis: int = 10, Cha: int = 10,
                 AC: int = 9, THAC0: int = 19, id: int = -1, roll_stats_on_creation: bool = False,
                 weapon: items.Weapon = items.fist, gp: int = 0, sp: int = 0, cp: int = 0, pp: int = 0,
                 ep: int = 0, armor: items.Armor = items.no_armor, save_death: int = 0, save_wand: int = 0, 
                 save_para: int = 0, save_breath: int = 0, save_spell: int = 0, languages: list = [],
                 skills: dict = {}, attacks = None, passives = None,
                 combat_row: str = "Front", Class: str = "Fighter"):
        
        super().__init__(name=name, HD=HD, HD_type=HD_type, Str=Str, Dex=Dex, Con=Con,
                         Int=Int, Wis=Wis, Cha=Cha, AC=AC, THAC0=THAC0, id=id,
                         roll_stats_on_creation=roll_stats_on_creation,
                         weapon=weapon, gp=gp, sp=sp, cp=cp, pp=pp, ep=ep, armor=armor,
                         save_death=save_death, save_wand=save_wand, save_para=save_para, save_breath=save_breath, save_spell=save_spell,
                         languages=languages, skills=skills,
                         attacks=attacks, passives=passives, combat_row=combat_row)
        
        self.xp = 0
        self.xp_needed = 0
        self.prog_table = [] # List of dictionaries for HD, THAC0, xp_needed, spells, etc... (per level)
        self.skill_table = [] # List of dictionaries for thief skills, etc... (per level)
        self.level = 1
        self.Class = Class

        self.type = "Character"

        self.gp = self.roll_dice((3,6))*10

        self.refresh_combat_callback = None
        self.refresh_combat_args = []

    def init_character(self):
        self.init_prog_table()
        self.init_skill_table()
        self.init_languages_and_skills()

        self.save_death = self.prog_table[self.level-1]["death_save"]
        self.save_wand = self.prog_table[self.level-1]["wand_save"]
        self.save_para = self.prog_table[self.level-1]["para_save"]
        self.save_breath = self.prog_table[self.level-1]["breath_save"]
        self.save_spell = self.prog_table[self.level-1]["spell_save"]

        self.update_saves()
        self.update_attributes()
        
    def init_languages_and_skills(self):
        self.languages = []
        self.skills = {}
        
        if self.Class == "Dwarf":
            self.languages = ["Alignment", "Common", "Dwarvish", "Gnomish", "Goblin", "Kobold"]

        elif self.Class == "Elf":
            self.languages = ["Alignment", "Common", "Elvish", "Gnoll", "Hobgoblin", "Orcish"]
            self.skills["Arcane Magic"] = {"name": "Arcane Magic", "desc": "Elves may spend time and money researching new spells to add to their spellbook.", "val": 0}
            self.skills["Detect Doors"] = {"name": "Detect Doors", "desc": "Elves have a 2 in 6 chance of detecting hidden doors.", "val": 2}
            self.skills["Ghoul Paralysis Immunity"] = {"name": "Ghoul Paralysis Immunity", "desc": "Elves can not be paralized by ghouls.", "val": 0}
            self.skills["Infravision"] = {"name": "Infravision", "desc": "Elves can see heat signatures in the dark for 60 feet.", "val": 60}
            self.skills["Listen at Doors"] = {"name": "Listen at Doors", "desc": "Elves have a 2 in 6 chance to successfully hear through the other side of a door.", "val": 2}

        elif self.Class == "Thief":
            self.languages = ["Alignment", "Common"]
            self.skills["Backstab"] = {"name": "Backstab","desc": "Thieves deal double damage from behind, they also get a +4 to hit.", "val": 4}
            self.skills["Read Languages"] = {"name": "Read Languages", "desc": "If a thief is above 3rd level, their chance to successfully read any language is 80%.", "val": 80}
            self.skills["Scroll Use"] = {"name": "Scroll Use", "desc": "A thief of 10th level or higher may cast arcane scrolls with a chance to fail of 10%.", "val": 10}
        
        elif self.Class == "Fighter":
            self.languages = ["Alignment", "Common"]


    def init_skill_table(self):
        if self.Class == "Thief":
            self.skill_table = [
                {"Level": 1, "CS": 87, "TR": 10, "HN": (1,2), "HS": 10, "MS": 20, "OL": 15, "PP": 20},
                {"Level": 2, "CS": 88, "TR": 15, "HN": (1,2), "HS": 15, "MS": 25, "OL": 20, "PP": 25},
                {"Level": 3, "CS": 89, "TR": 20, "HN": (1,3), "HS": 20, "MS": 30, "OL": 25, "PP": 30},
                {"Level": 4, "CS": 90, "TR": 25, "HN": (1,3), "HS": 25, "MS": 35, "OL": 30, "PP": 35},
                {"Level": 5, "CS": 91, "TR": 30, "HN": (1,3), "HS": 30, "MS": 40, "OL": 35, "PP": 40},
                {"Level": 6, "CS": 92, "TR": 40, "HN": (1,3), "HS": 36, "MS": 45, "OL": 45, "PP": 45},
                {"Level": 7, "CS": 93, "TR": 50, "HN": (1,4), "HS": 45, "MS": 55, "OL": 55, "PP": 55},
                {"Level": 8, "CS": 94, "TR": 60, "HN": (1,4), "HS": 55, "MS": 65, "OL": 65, "PP": 65},
                {"Level": 9, "CS": 95, "TR": 70, "HN": (1,4), "HS": 65, "MS": 75, "OL": 75, "PP": 75},
                {"Level": 10, "CS": 96, "TR": 80, "HN": (1,4), "HS": 75, "MS": 85, "OL": 85, "PP": 85},
                {"Level": 11, "CS": 97, "TR": 90, "HN": (1,5), "HS": 85, "MS": 95, "OL": 95, "PP": 95},
                {"Level": 12, "CS": 98, "TR": 95, "HN": (1,5), "HS": 90, "MS": 96, "OL": 96, "PP": 105},
                {"Level": 13, "CS": 99, "TR": 97, "HN": (1,5), "HS": 95, "MS": 98, "OL": 97, "PP": 115},
                {"Level": 14, "CS": 99, "TR": 99, "HN": (1,5), "HS": 99, "MS": 99, "OL": 99, "PP": 125},
            ]

    def init_prog_table(self):
        if self.Class == "Fighter":
            self.prog_table = [
                {"Level": 1, "HD": (1,8), "THAC0": 19, "xp_needed": 2000, "death_save": 12, "wand_save": 13, "para_save": 14, "breath_save": 15, "spell_save": 16},
                {"Level": 2, "HD": (2,8), "THAC0": 19, "xp_needed": 4000, "death_save": 12, "wand_save": 13, "para_save": 14, "breath_save": 15, "spell_save": 16},
                {"Level": 3, "HD": (3,8), "THAC0": 19, "xp_needed": 8000, "death_save": 12, "wand_save": 13, "para_save": 14, "breath_save": 15, "spell_save": 16},
                {"Level": 4, "HD": (4,8), "THAC0": 17, "xp_needed": 16000, "death_save": 10, "wand_save": 11, "para_save": 12, "breath_save": 13, "spell_save": 14},
                {"Level": 5, "HD": (5,8), "THAC0": 17, "xp_needed": 32000, "death_save": 10, "wand_save": 11, "para_save": 12, "breath_save": 13, "spell_save": 14},
                {"Level": 6, "HD": (6,8), "THAC0": 17, "xp_needed": 64000, "death_save": 10, "wand_save": 11, "para_save": 12, "breath_save": 13, "spell_save": 14},
                {"Level": 7, "HD": (7,8), "THAC0": 14, "xp_needed": 120000, "death_save": 8, "wand_save": 9, "para_save": 10, "breath_save": 10, "spell_save": 12},
                {"Level": 8, "HD": (8,8), "THAC0": 14, "xp_needed": 240000, "death_save": 8, "wand_save": 9, "para_save": 10, "breath_save": 10, "spell_save": 12},
                {"Level": 9, "HD": (9,8), "THAC0": 14, "xp_needed": 360000, "death_save": 8, "wand_save": 9, "para_save": 10, "breath_save": 10, "spell_save": 12},
                {"Level": 10, "HD": (9,8), "THAC0": 12, "xp_needed": 480000, "death_save": 6, "wand_save": 7, "para_save": 8, "breath_save": 8, "spell_save": 10},
                {"Level": 11, "HD": (9,8), "THAC0": 12, "xp_needed": 600000, "death_save": 6, "wand_save": 7, "para_save": 8, "breath_save": 8, "spell_save": 10},
                {"Level": 12, "HD": (9,8), "THAC0": 12, "xp_needed": 720000, "death_save": 6, "wand_save": 7, "para_save": 8, "breath_save": 8, "spell_save": 10},
                {"Level": 13, "HD": (9,8), "THAC0": 10, "xp_needed": 840000, "death_save": 4, "wand_save": 5, "para_save": 6, "breath_save": 5, "spell_save": 8},
                {"Level": 14, "HD": (9,8), "THAC0": 10, "xp_needed": float('inf'), "death_save": 4, "wand_save": 5, "para_save": 6, "breath_save": 5, "spell_save": 8},
            ]
        
        elif self.Class == "Dwarf":
            self.prog_table = [
                {"Level": 1, "HD": (1,8), "THAC0": 19, "xp_needed": 2200, "death_save": 8, "wand_save": 9, "para_save": 10, "breath_save": 13, "spell_save": 12},
                {"Level": 2, "HD": (2,8), "THAC0": 19, "xp_needed": 4400, "death_save": 8, "wand_save": 9, "para_save": 10, "breath_save": 13, "spell_save": 12},
                {"Level": 3, "HD": (3,8), "THAC0": 19, "xp_needed": 8800, "death_save": 8, "wand_save": 9, "para_save": 10, "breath_save": 13, "spell_save": 12},
                {"Level": 4, "HD": (4,8), "THAC0": 17, "xp_needed": 17000, "death_save": 6, "wand_save": 7, "para_save": 8, "breath_save": 10, "spell_save": 10},
                {"Level": 5, "HD": (5,8), "THAC0": 17, "xp_needed": 35000, "death_save": 6, "wand_save": 7, "para_save": 8, "breath_save": 10, "spell_save": 10},
                {"Level": 6, "HD": (6,8), "THAC0": 17, "xp_needed": 70000, "death_save": 6, "wand_save": 7, "para_save": 8, "breath_save": 10, "spell_save": 10},
                {"Level": 7, "HD": (7,8), "THAC0": 14, "xp_needed": 140000, "death_save": 4, "wand_save": 5, "para_save": 6, "breath_save": 7, "spell_save": 8},
                {"Level": 8, "HD": (8,8), "THAC0": 14, "xp_needed": 270000, "death_save": 4, "wand_save": 5, "para_save": 6, "breath_save": 7, "spell_save": 8},
                {"Level": 9, "HD": (9,8), "THAC0": 14, "xp_needed": 400000, "death_save": 4, "wand_save": 5, "para_save": 6, "breath_save": 7, "spell_save": 8},
                {"Level": 10, "HD": (9,8), "THAC0": 12, "xp_needed": 530000, "death_save": 2, "wand_save": 3, "para_save": 4, "breath_save": 4, "spell_save": 6},
                {"Level": 11, "HD": (9,8), "THAC0": 12, "xp_needed": 660000, "death_save": 2, "wand_save": 3, "para_save": 4, "breath_save": 4, "spell_save": 6},
                {"Level": 12, "HD": (9,8), "THAC0": 12, "xp_needed": float('inf'), "death_save": 2, "wand_save": 3, "para_save": 4, "breath_save": 4, "spell_save": 6},
            ]

        elif self.Class == "Elf":
            self.prog_table = [
                {"Level": 1, "HD": (1,6), "THAC0": 19, "xp_needed": 4000, 
                 "death_save": 12, "wand_save": 13, "para_save": 13, "breath_save": 15, "spell_save": 15,
                 "lv1_spells": 1, "lv2_spells": 0, "lv3_spells": 0, "lv4_spells": 0, "lv5_spells": 0},

                 {"Level": 2, "HD": (2,6), "THAC0": 19, "xp_needed": 8000, 
                 "death_save": 12, "wand_save": 13, "para_save": 13, "breath_save": 15, "spell_save": 15,
                 "lv1_spells": 2, "lv2_spells": 0, "lv3_spells": 0, "lv4_spells": 0, "lv5_spells": 0},

                 {"Level": 3, "HD": (3,6), "THAC0": 19, "xp_needed": 16000, 
                 "death_save": 12, "wand_save": 13, "para_save": 13, "breath_save": 15, "spell_save": 15,
                 "lv1_spells": 2, "lv2_spells": 1, "lv3_spells": 0, "lv4_spells": 0, "lv5_spells": 0},

                 {"Level": 4, "HD": (4,6), "THAC0": 17, "xp_needed": 32000, 
                 "death_save": 10, "wand_save": 11, "para_save": 11, "breath_save": 13, "spell_save": 12,
                 "lv1_spells": 2, "lv2_spells": 2, "lv3_spells": 0, "lv4_spells": 0, "lv5_spells": 0},

                 {"Level": 5, "HD": (5,6), "THAC0": 17, "xp_needed": 64000, 
                 "death_save": 10, "wand_save": 11, "para_save": 11, "breath_save": 13, "spell_save": 12,
                 "lv1_spells": 2, "lv2_spells": 2, "lv3_spells": 1, "lv4_spells": 0, "lv5_spells": 0},

                 {"Level": 6, "HD": (6,6), "THAC0": 17, "xp_needed": 120000, 
                 "death_save": 10, "wand_save": 11, "para_save": 11, "breath_save": 13, "spell_save": 12,
                 "lv1_spells": 2, "lv2_spells": 2, "lv3_spells": 2, "lv4_spells": 0, "lv5_spells": 0},

                 {"Level": 7, "HD": (7,6), "THAC0": 14, "xp_needed": 250000, 
                 "death_save": 8, "wand_save": 9, "para_save": 9, "breath_save": 10, "spell_save": 10,
                 "lv1_spells": 3, "lv2_spells": 2, "lv3_spells": 2, "lv4_spells": 1, "lv5_spells": 0},

                 {"Level": 8, "HD": (8,6), "THAC0": 14, "xp_needed": 400000, 
                 "death_save": 8, "wand_save": 9, "para_save": 9, "breath_save": 10, "spell_save": 10,
                 "lv1_spells": 3, "lv2_spells": 3, "lv3_spells": 2, "lv4_spells": 2, "lv5_spells": 0},

                 {"Level": 9, "HD": (9,6), "THAC0": 14, "xp_needed": 600000, 
                 "death_save": 8, "wand_save": 9, "para_save": 9, "breath_save": 10, "spell_save": 10,
                 "lv1_spells": 3, "lv2_spells": 3, "lv3_spells": 3, "lv4_spells": 2, "lv5_spells": 1},

                 {"Level": 10, "HD": (9,6), "THAC0": 12, "xp_needed": float('inf'), 
                 "death_save": 6, "wand_save": 7, "para_save": 8, "breath_save": 8, "spell_save": 8,
                 "lv1_spells": 3, "lv2_spells": 3, "lv3_spells": 3, "lv4_spells": 3, "lv5_spells": 2}
            ]

        elif self.Class == "Thief":
            self.prog_table = [
                {"Level": 1, "HD": (1,4), "THAC0": 19, "xp_needed": 1200, "death_save": 13, "wand_save": 14, "para_save": 13, "breath_save": 16, "spell_save": 15},
                {"Level": 2, "HD": (2,4), "THAC0": 19, "xp_needed": 2400, "death_save": 13, "wand_save": 14, "para_save": 13, "breath_save": 16, "spell_save": 15},
                {"Level": 3, "HD": (3,4), "THAC0": 19, "xp_needed": 4800, "death_save": 13, "wand_save": 14, "para_save": 13, "breath_save": 16, "spell_save": 15},
                {"Level": 4, "HD": (4,4), "THAC0": 19, "xp_needed": 9600, "death_save": 13, "wand_save": 14, "para_save": 13, "breath_save": 16, "spell_save": 15},
                {"Level": 5, "HD": (5,4), "THAC0": 17, "xp_needed": 20000, "death_save": 12, "wand_save": 13, "para_save": 11, "breath_save": 13, "spell_save": 14},
                {"Level": 6, "HD": (6,4), "THAC0": 17, "xp_needed": 40000, "death_save": 12, "wand_save": 13, "para_save": 11, "breath_save": 13, "spell_save": 14},
                {"Level": 7, "HD": (7,4), "THAC0": 17, "xp_needed": 80000, "death_save": 12, "wand_save": 13, "para_save": 11, "breath_save": 13, "spell_save": 14},
                {"Level": 8, "HD": (8,4), "THAC0": 17, "xp_needed": 160000, "death_save": 12, "wand_save": 13, "para_save": 11, "breath_save": 13, "spell_save": 14},
                {"Level": 9, "HD": (9,4), "THAC0": 14, "xp_needed": 280000, "death_save": 10, "wand_save": 11, "para_save": 9, "breath_save": 12, "spell_save": 10},
                {"Level": 10, "HD": (9,4), "THAC0": 14, "xp_needed": 400000, "death_save": 10, "wand_save": 11, "para_save": 9, "breath_save": 12, "spell_save": 10},
                {"Level": 11, "HD": (9,4), "THAC0": 14, "xp_needed": 520000, "death_save": 10, "wand_save": 11, "para_save": 9, "breath_save": 12, "spell_save": 10},
                {"Level": 12, "HD": (9,4), "THAC0": 14, "xp_needed": 640000, "death_save": 10, "wand_save": 11, "para_save": 9, "breath_save": 12, "spell_save": 10},
                {"Level": 13, "HD": (9,4), "THAC0": 12, "xp_needed": 760000, "death_save": 8, "wand_save": 9, "para_save": 7, "breath_save": 10, "spell_save": 8},
                {"Level": 14, "HD": (9,4), "THAC0": 12, "xp_needed": float('inf'), "death_save": 8, "wand_save": 9, "para_save": 7, "breath_save": 10, "spell_save": 8},
            ]


class Monster(Creature):
    def __init__(self, name: str = "Default Creature", HD: int = 1, HD_type: int = 8, Str: int = 10, 
                 Dex: int = 10, Con: int = 10, Int: int = 10, Wis: int = 10, Cha: int = 10,
                 AC: int = 9, THAC0: int = 19, id: int = -1, roll_stats_on_creation: bool = False,
                 weapon: items.Weapon = items.fist, gp: int = 0, sp: int = 0, cp: int = 0,
                 pp: int = 0, ep: int = 0, armor: items.Armor = items.no_armor, save_death: int = 0,
                 save_wand: int = 0, save_para: int = 0, save_breath: int = 0, save_spell: int = 0,
                 languages: list = [], skills: dict = {}, attacks = None, passives = None, 
                 number_appearing: int = (1,4), combat_row: str = "Front"):
        
        super().__init__(name=name, HD=HD, HD_type=HD_type, Str=Str, Dex=Dex, Con=Con,
                         Int=Int, Wis=Wis, Cha=Cha, AC=AC, THAC0=THAC0, id=id,
                         roll_stats_on_creation=roll_stats_on_creation,
                         weapon=weapon, gp=gp, sp=sp, cp=cp, pp=pp, ep=ep, armor=armor,
                         save_death=save_death, save_wand=save_wand, save_para=save_para, save_breath=save_breath, save_spell=save_spell,
                         languages=languages, skills=skills,
                         attacks=attacks, passives=passives, combat_row=combat_row)
        
        self.number_appearing = number_appearing

        self.type = "Monster"

        self.refresh_combat_callback = None
        self.refresh_combat_args = []
        
    def get_passive_by_hook(self, hook_type):
        """Finds all passives that should trigger right now."""
        return [p for p in self.passives if p.hook_type == hook_type]
    
    def set_saves(self):
        pass


# =====================================================================
# Item or Action Classes
# =====================================================================

class Party():
    def __init__(self, name: str, in_dungeon: bool = False, location = None, combat_mode = "Manual"):
        self.name = name
        self.chars = []
        self.in_dungeon = in_dungeon
        self.location = location
        self.combat_mode = combat_mode
        self.surprised = False
        self.surprised_enemies = False

    def add_member(self, char_obj: Creature = None):
        if char_obj is not None:
            self.chars.append(char_obj)
            char_obj.party = self.name
        else:
            print(f"\nError: No creature specified for addition to {self.name}.\n")

    def remove_member(self, char_obj: Creature = None):
        if char_obj is not None:
            self.chars.remove(char_obj)
            char_obj.party = f"{char_obj.name}_id={char_obj.id}"
        else:
            print(f"\nError: No creature specified for removal from {self.name}.\n")


class MonsterAttack:
    """Represents an active action a monster can take on its turn."""
    def __init__(self, name, hit_bonus, damage_dice, description="", effect_callback=None,
                 roll_to_hit: bool = True, range=10):
        self.name = name
        self.hit_bonus = hit_bonus
        self.damage_dice = damage_dice  # e.g., "1d6" or "2d8"
        self.description = description
        self.effect_callback = effect_callback  # Custom logic for effects (like poison)
        self.roll_to_hit = roll_to_hit
        self.range = range

    def execute(self, attacker: Creature, target: Creature):
        hit, roll = self.roll_hit(attacker=attacker, target=target)
        dmg = self.roll_dice(self.damage_dice) if hit else 0
        print(f"{attacker.name} uses {self.name} on {target.name}...")
        world.console_log(f"{attacker.name} uses {self.name} on {target.name}...")
        world.custom_log(log_tag=f"{attacker.party}_combat_log", message=f"{attacker.name} uses {self.name} on {target.name}...")

        if dmg > 0:
            target.HP -= dmg

            print(f"Roll: {roll} [!] {target.name} was hit for {dmg} damage!\n")
            world.console_log(f"Roll: {roll} [!] {target.name} was hit for {dmg} damage!\n")
            world.custom_log(log_tag=f"{attacker.party}_combat_log", message=f"Roll: {roll} [!] {target.name} was hit for {dmg} damage!\n")

            self.effect_callback(attacker=attacker, target=target)
        else:
            print(f"Roll: {roll} | Miss!\n")
            world.console_log(f"Roll: {roll} | Miss!\n")
            world.custom_log(log_tag=f"{attacker.party}_combat_log", message=f"Roll: {roll} | Miss!\n")

    def roll_hit(self, attacker: Creature, target: Creature):
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


class MonsterPassive:
    """Represents a passive trait that hooks into game events."""
    def __init__(self, name, hook_type, effect_callback, description=""):
        self.name = name
        self.hook_type = hook_type  # e.g., "on_damaged", "on_hit_target", "always"
        self.effect_callback = effect_callback
        self.description = description


# =====================================================================
# Ability Callbacks
# =====================================================================

def apply_status(attacker: Creature, target: Creature, status_name: str, save: bool = True, 
                 save_type: str = "P", duration = (2,4), effect_message: str = "be frozen"):
    
    save_info = target.saves.get(save_type)

    if save_info is not None and save:
        save_name, target_num = save_info

        print(f"   [!] {target.name} must save vs. {save_name} or {effect_message}!")
        world.console_log(f"   [!] {target.name} must save vs. {save_name} or {effect_message}!")
    
        world.custom_log(log_tag=f"{target.party}_combat_log", 
                        message=f"   [!] {target.name} must save vs. {save_name} or {effect_message}!",
                        not_time_stamped=True)

        if not target.save_vs(save_type=save_type):
            target.status_effects[status_name] = attacker.roll_dice(duration) if \
                isinstance(duration, tuple) else duration
        
    if not save:
        target.status_effects[status_name] = attacker.roll_dice(duration) if \
            isinstance(duration, tuple) else duration


def ghoul_paralysis_logic(attacker: Creature, target: Creature):
    if not (target.type == "Character" and target.Class == "Elf") and not (target.type == "Monster" and target.name == "Elf"):
        print(f"Target: {target.name} | Target Class: {target.Class}")
        apply_status(attacker=attacker, target=target, status_name="Paralyzed", effect_message="be paralyzed")


def rust_monster_logic(attacker, target):
    """Passive effect that triggers when a player hits the Rust Monster."""
    print(f"   [!] {target.name}'s metal weapon touches the Rust Monster and instantly corrodes into useless dust!")


# ==========================================
# Helper Functions
# ==========================================


def get_magic():
    global magic
    if magic is None:
        import magic as magic
    
    return magic


# =====================================================================
# Bestiary
# =====================================================================

def spawn_monster(monster_name):
    global all_creatures

    """Factory function to build a concrete monster from catalog data."""
    data = BESTIARY_DATA[monster_name]
    
    # 1. Build out the attacks
    atk_objects = []
    for atk in data["attacks"]:
        atk_objects.append(MonsterAttack(atk["name"], atk["hit_bonus"], atk["damage"], effect_callback=atk.get("effect"),
                                         range=atk["range"]))
        
    # 2. Build out the passives
    passive_objects = []
    for pas in data["passives"]:
        passive_objects.append(MonsterPassive(pas["name"], pas["hook"], pas["effect"]))

    monster = Monster(name=monster_name, HD=data["HD"], AC=data["AC"], THAC0=data["THAC0"], attacks=atk_objects, passives=passive_objects,
                   number_appearing=data["number_appearing"])

    #print(f" all_creatures new addition: {all_creatures[monster.id].name} (ID: {monster.id})")
        
    # 3. Return the fully-built sandbox monster instance
    return monster

BESTIARY_DATA = {
    "Ghoul": {
        "HD": 2, "AC": 6, "THAC0": 18, "number_appearing": (1,6),
        "attacks": [
            {"name": "Claw", "hit_bonus": 2, "damage": (1,3), "effect": ghoul_paralysis_logic,
             "range": 10},
            {"name": "Bite", "hit_bonus": 2, "damage": (1,3), "effect": ghoul_paralysis_logic,
             "range": 10}
        ],
        "passives": [
            {"name": "Undead Immunity", "hook": "on_status_effect", "effect": lambda a, t: print("   [Passive] Immune to sleep/charm.")}
        ]
    },

    "Rust Monster": {
        "HD": 5, "AC": 2, "THAC0": 15,
        "attacks": [
            {"name": "Antennae Touch", "hit_bonus": 3, "damage": (0,0), "effect": rust_monster_logic,
             "range": 10}
        ],
        "passives": [
            {"name": "Rust Aura", "hook": "on_attacked_by_metal", "effect": rust_monster_logic}
        ]
    }
}