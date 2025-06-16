package com.game.objects;

import com.managers.Audio;

public class Player {
	public int hp,maxHP,str,luck,Int,sneak,xp,maxXP,level,gold,agi,mana,maxMana,manaGain,manaRegenTimer,fine;
	public int x,y,w,h,i,oldHp,oldStr,oldLuck,oldInt,oldSneak,oldAgi;
	public double xpChange;
	public String race,Class,helmet,chest,legs,name;	//helmet chest and legs are for what kind of armor you are wearing
	public String[] inventory;
	public String[] spells;
	public Audio audioYeet;
	
	public Player() {
		audioYeet = new Audio();
		
		x = 32*16;
		y = 32*10;
		w = 32;
		h = 64;
		hp = 50;
		maxHP = 50;
		str = 10;
		oldStr = str;
		i = 0;
		luck = 10;
		oldLuck = luck;
		Int = 10;
		name = "";
		oldInt  = Int;
		oldHp = maxHP;
		sneak = 10;
		oldSneak = sneak;
		xp = 0;
		maxXP = 20;
		level = 1;
		gold = 15;
		agi = 10;
		oldAgi = agi;
		xpChange = 50;
		maxMana = 10;
		manaGain = 2;
		manaRegenTimer = -3;
		mana = maxMana;
		race = "orc";
		fine = 0;
		Class = "warrior";
		helmet = "";	//this will decide which kind of helmet you have, so you can wear any kind
		
		inventory = new String[12];
		spells = new String[12];
	}
	
	//adds an item to your inventory
	public void addToInv(String item,boolean swap) {
		for(int i = 0; i < inventory.length; i++) {	//increases i by 1 while i isn't the length of the inventory
			if(inventory[i] == null || inventory[i].equals("")) {	//adds item to that slot if the slot is empty
				swap = true;
				inventory[i] = item;
				this.i = i;
				audioYeet.playSound("assets/audio/sfx/itemPickup.wav", false);
				break;
			}
		}
	}
	
	//removes item from inventory
	public void removeFromInv(String item) {
		for(int i = 0; i < inventory.length; i++) {
			if(inventory[i].equals(item)) {
				inventory[i] = "";
				break;
			}
		}
	}
	
	//adds a spell to your spells list
	public void addToSpells(String spell) {
		for(int i = 0; i < spells.length; i++) {
			if(spells[i] == null || spells[i].equals("")) {
				spells[i] = spell;
				this.i = i;
				audioYeet.playSound("assets/audio/sfx/itemPickup.wav", false);
				break;
			}
		}
	}
	
	//levels up the character
	public void levelUp() {
		level++;	//possibly change the stat upgrades later
		xpChange = Math.floor(Math.pow(2, this.level));
		maxXP += xpChange;
	}
}
