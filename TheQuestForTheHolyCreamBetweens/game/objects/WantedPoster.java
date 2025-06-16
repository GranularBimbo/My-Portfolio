package com.game.objects;

import java.util.Random;

import com.managers.ImageManager;

public class WantedPoster {
	public String name,title,location,race,boardLocation;
	public boolean taken;
	public int maxHP,attack,gold,xp,difficulty,oldHP,oldAttack;
	public String[] namePool,titlePool,locations;
	public ImageManager helmet,chest,legging;
	public Enemy enemy;
	Random random;
	
	//difficulty from 1 to 5

	public void createEnemy(int playerLevel) {
		int raceGenerator,helmet,chest,legging,loc,hpScaleFactor,attackScaleFactor;
		
		hpScaleFactor = 30;
		attackScaleFactor = 4;
		
		enemy = new Enemy(0,0,0,0,32,64,"","","",null,null,null);
		
		locations = new String[] {
				"camp","woods7","woods6","woods5","woods3","woods2"
		};
		
		namePool = new String[] {
				"Grubosh","Groble","Nate","Gaïrlan","Smelgish","Joe","Jenine","Anthony","Liam","Mason","Jarlog",
				"Putesk","Michael","Harper","Tablesh","Jake","Steve","Bill","Glarb","Shanon","Grill","Gebbler",
				"Rulon","Keith","Alex","Chris","Zügog","Tok","Red","Uncle","Charles","Daniel","Donald","Dupog",
				"Hallzar","Julian","Dimitri","Lucian","Niko","Gobbledinger","Boris","Zorka","Sabrina","Dane",
				"Harry","Dylan","Erik","Brent","Logan","Kevin","John","Jason","Raj","Ugosh","Grimmer","Spud",
				"Goolong","Robby","Mr.IThinkImFunny","Ethan"
		};
		
		titlePool = new String[] {
				"the Gross","the Odio","the Smelly","Smith","Miller","the Rude","Williams","Jones","the Rancid",
				"the Poop Eater","Davis","Brown","the Foot Washer","Reed","the Hamburger","the Dish Washer",
				"the Undergrad","the Part-Time Worker","Tanzenvezniski","the Grandpa","Hernandez","Moore",
				"Taylor","Anderson","the Door Licker","Morgan","Harris","the Cable Guy","Adams","Green",
				"Murphy","Bell","the Fitted Sheet","the Stupid Head","Troy","the Thicc Boi","McDavid",
				"King","Smith","Ross", "Kidd","Harkey","Haskall","Williams","Wentz","The Ps4 Cleaner",
				"The Housemade","The Karaoke Machine","The SoundCloud Rapper","The Mistake","The Sweeper",
				"Wicks","Hopper","Wick","The Ant Tamer","The Suicidal Guitarist","The Baby Blender",
				"The Fake Wolf","Styles","Wentz","Haskall","The White Knight",
				"The Graphics Card","The Letter Q","White","Segura","Appleseed","The Enema Giver","Ryder",
				"Hill","Clancy","The Cosplayer","The Glass Dealer","The Terrible Son","The Air Inhaler",
				"The Poop Dealer"
		};
		
		name = namePool[random.nextInt(namePool.length)];
		title = titlePool[random.nextInt(titlePool.length)];
		
		raceGenerator = random.nextInt(2);
		helmet = random.nextInt(enemy.helmets.length);
		chest = random.nextInt(enemy.chests.length);
		legging = random.nextInt(enemy.leggings.length);
		loc = random.nextInt(locations.length);
		
		difficulty = random.nextInt(4)+1;
		
		gold = difficulty*10;
		xp = ((difficulty*difficulty) + (playerLevel*2));
		
		this.helmet = enemy.helmets[helmet];
		this.chest = enemy.chests[chest];
		this.legging = enemy.leggings[legging];
		location = locations[loc];
		
		if(raceGenerator == 0) {
			race = "orc";
		}
		else {
			if(raceGenerator == 1) {
				race = "undead";
			}
			else {
				race = "human";
			}
		}
		
		
		if(playerLevel > 1) {
			maxHP = (int) ((difficulty*hpScaleFactor)*(Math.floor(playerLevel/2)));
			attack = (int) Math.floor((oldAttack+(playerLevel*3)));
		}
		else {
			maxHP = ((difficulty*hpScaleFactor)/(playerLevel));
			attack = ((difficulty*attackScaleFactor)*((playerLevel*3)/2));
			oldHP = maxHP;
			
			oldAttack = attack;
		}
		
		
		enemy = new Enemy(32*20,32*12,maxHP,attack,32,64,"" + this.name + " " + this.title,race,location,this.helmet,this.chest,this.legging);
		
		enemy.hp = enemy.maxHP;
		
		if(enemy.race.equals("orc")) {
			for(int i = 0; i < enemy.orcBody.length; i++) {
				enemy.character[i] = enemy.orcBody[i];
			}
		}
		else {
			if(enemy.race.equals("human")) {
				for(int c = 0; c < enemy.humanBody.length; c++) {
					enemy.character[c] = enemy.humanBody[c];
				}
			}
			else {
				if(enemy.race.equals("undead")) {
					for(int c = 0; c < enemy.skeletonBody.length; c++) {
						enemy.character[c] = enemy.skeletonBody[c];
					}
				}
			}
		}
	}
	
	public WantedPoster(int playerLevel) {
		random = new Random();
		taken = false;
		
		createEnemy(playerLevel);
	}
}
