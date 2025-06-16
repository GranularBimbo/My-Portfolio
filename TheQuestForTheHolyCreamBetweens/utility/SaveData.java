package com.utility;

import com.game.objects.Enemy;
import com.game.objects.WantedPoster;

public class SaveData implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	public int hp,maxHP,str,luck,Int,sneak,xp,maxXP,level,gold,speech,mana,maxMana,manaGain,manaRegenTimer,chopped;
	public int fine,guardHP,leatherRestock,thiefHeadRestock,thiefChestRestock,mageHeadRestock,mageChestRestock,
	woodSwordRestock,steelSwordRestock,shankRestock,daggerRestock,steelHeadRestock,steelChestRestock,health,
	steelLeggingsRestock,hpPotionRestock,manaPotionRestock,t2ThiefHoodRestock,t2ThiefRobeRestock,maxHealth,Xp,Gold,
	oldXp,oldLevel,skillPoints,oldHp,oldStr,oldLuck,oldSneak,oldInt,oldAgi,t2ThiefPantsRestock,lesserHealRestock,
	average,luckBonus,strBonus,hpBonus,sneakBonus,agiBonus,oldYear,year;
	public String race,Class,helmet,chest,legs,weapon,location,name,loc;
	public boolean weirdTreeDone,wanted,hostile,guard2hostile;
	public String[] inventory;
	public double caught;
	public String[] spells,locs,names;
	public int[] hps;
	public int[] maxhps;
	public int[] xps;
	public int[] golds;
	public boolean[] quests;
	public double xpChange;
	
	
	public SaveData() {
		hp = 50;
		luckBonus = 0;
		strBonus = 0;
		sneakBonus = 0;
		hpBonus = 0;
		agiBonus = 0;
		name = "";
		t2ThiefHoodRestock = 0;
		t2ThiefRobeRestock = 0;
		t2ThiefPantsRestock = 0;
		lesserHealRestock = 0;
		health = 20;
		oldYear = 0;
		year = 0;
		maxHealth = 20;
		Xp = 10;
		golds = new int[] {
				0,0,0
		};
		maxhps = new int[] {
				0,0,0
		};
		hps = new int[] {
				0,0,0
		};
		names = new String[] {
				"","",""
		};
		xps = new int[] {
				0,0,0
		};
		locs = new String[] {
				"","",""
		};
		names = new String[] {
				"","",""
		};
		Gold = 10;
		loc = "";
		maxHP = 50;
		str = 10;
		luck = 10;
		average = 10;
		oldHp = 0;
		oldStr = 0;
		oldInt = 0;
		oldSneak = 0;
		oldAgi = 0;
		oldLuck = 0;
		oldLevel = 0;
		Int = 10;
		sneak = 10;
		skillPoints = 0;
		xp = 0;
		maxXP = 20;
		level = 1;
		gold = 15;
		speech = 10;
		xpChange = 50;
		maxMana = 10;
		manaGain = 2;
		chopped = 0;
		oldXp = 0;
		fine = 0;
		mana = maxMana;
		race = "orc";
		Class = "warrior";
		manaRegenTimer = -3;
		helmet = "";
		chest = "";
		legs = "";
		weapon = "";
		location = "";
		leatherRestock = 0;
		thiefHeadRestock = 0;
		thiefChestRestock = 0;
		mageHeadRestock = 0;
		mageChestRestock = 0;
		woodSwordRestock = 0;
		steelSwordRestock = 0;
		shankRestock = 0;
		daggerRestock = 0;
		hpPotionRestock = 0;
		manaPotionRestock = 0;
		steelHeadRestock = 0;
		steelChestRestock =0;
		caught = 0;
		steelLeggingsRestock = 0;
		weirdTreeDone = true;
		wanted = false;
		hostile = false;
		guardHP = 0;
		
		
		inventory = new String[10];
		spells = new String[10];
		quests = new boolean[1];
	}
	
	
	public void updatePosterEnemies(Enemy[] enemies,WantedPoster[] posters) {
		for(int i = 0; i < enemies.length; i++) {
			for(int r = 0; r < xps.length; r++) {
				hps[r] = enemies[i].hp;
				maxhps[r] = enemies[i].maxHP;
				names[r] = enemies[i].name;
				locs[r] = enemies[i].location;
			}
		}
		
		for(int i = 0; i < posters.length; i++) {
			for(int r = 0; r < xps.length; r++) {
				xps[r] = posters[i].xp;
				golds[r] = posters[i].gold;
			}
		}
	}
	
	
	public void update2(int LR,int THR,int TCR,int MHR,int MCR,int WSR,int SSR,int SR,int DR,int SHR,int SCR,
			int SLR,int HPR,int MPR) {
		leatherRestock = LR;
		thiefHeadRestock = THR;
		thiefChestRestock = TCR;
		mageHeadRestock = MHR;
		mageChestRestock = MCR;
		woodSwordRestock = WSR;
		steelSwordRestock = SSR;
		shankRestock = SR;
		daggerRestock = DR;
		steelHeadRestock = SHR;
		steelChestRestock =SCR;
		steelLeggingsRestock = SLR;
		hpPotionRestock = HPR;
		manaPotionRestock = MPR;
	}
	
	public void update3(int oldYear,int year) {
		this.oldYear = oldYear;
		this.year = year;
	}
	
	public void updateData(int hp,int maxHP,int str,int luck,int Int,int sneak,int xp,int maxXP,
			int level,int gold,int speech,int mana,int maxMana,int manaGain,double xpChange,String race,
			String Class,String helmet,String chest,String legs,String[] inventory,String[] spells,
			int manaRegenTimer,String playerWeapon,boolean[] quests,boolean weirdTree,String location,
			int chopped,boolean wanted,boolean hostile,int fine,int guardHP,boolean guard2Hostile,
			double caught,int oldXp,int oldLevel,int skillPoints,int oldHp,int oldLuck,int oldAgi,int oldSneak,
			int oldStr,int oldInt,int t2ThiefHoodRestock,int t2ThiefRobeRestock,int t2ThiefPantsRestock,
			int lesserHealRestock,int average,int luckBonus,int strBonus,int sneakBonus,int hpBonus,
			int agiBonus,String name,int year) {
		
		this.hp = hp;
		this.maxHP = maxHP;
		this.str = str;
		this.luck = luck;
		this.Int = Int;
		this.average = average;
		this.sneak = sneak;
		this.xp = xp;
		this.maxXP = maxXP;
		this.level = level;
		this.gold = gold;
		this.speech = speech;
		this.xpChange = xpChange;
		this.maxMana = maxMana;
		this.manaGain = manaGain;
		this.mana = mana;
		this.race = race;
		this.Class = Class;
		this.helmet = helmet;
		this.chest = chest;
		this.legs = legs;
		this.inventory = inventory;
		this.spells = spells;
		this.manaRegenTimer = manaRegenTimer;
		weapon = playerWeapon;
		this.quests = quests;
		weirdTreeDone = weirdTree;
		this.location = location;
		this.chopped = chopped;
		this.wanted = wanted;
		this.hostile = hostile;
		this.fine = fine;
		this.guardHP = guardHP;
		this.guard2hostile = guard2Hostile;
		this.caught = caught;
		this.oldXp = oldXp;
		this.oldLevel = oldLevel;
		this.skillPoints = skillPoints;
		this.oldHp = oldHp;
		this.oldLuck = oldLuck;
		this.oldInt = oldInt;
		this.oldStr = oldStr;
		this.oldSneak = oldSneak;
		this.oldAgi = oldAgi;
		this.t2ThiefHoodRestock = t2ThiefHoodRestock;
		this.t2ThiefRobeRestock = t2ThiefRobeRestock;
		this.t2ThiefPantsRestock = t2ThiefPantsRestock;
		this.lesserHealRestock = lesserHealRestock;
		this.luckBonus = luckBonus;
		this.strBonus = strBonus;
		this.sneakBonus = sneakBonus;
		this.hpBonus = hpBonus;
		this.agiBonus = agiBonus;
		this.name = name;
		this.year = year;
	}
}
