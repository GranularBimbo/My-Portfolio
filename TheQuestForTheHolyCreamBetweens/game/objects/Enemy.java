package com.game.objects;

import java.util.Random;

import com.managers.ImageManager;

public class Enemy{
	public int x,y,w,h,maxHP,hp,attack,damage,oldAttack,oldHP,hpScaleFactor,attackScaleFactor,xp;
	public String location,race,name,title;
	public boolean hostile;
	public String[] namePool,titlePool;
	public ImageManager orcHead,skull,skeletonChest,skeletonLegs,orcTorso,orcLegs,humanHead,humanTorso,humanLegs,t2ThiefHood,t2ThiefRobe;
	public ImageManager leatherHelm,t2ThiefLeggings,hoodHelm,thiefHelm,leatherChest,thiefChest,robeChest,steelHelm,steelChest;
	public ImageManager steelLeggings,helmet,chest,legging;
	public ImageManager[] character,orcBody,humanBody,helmets,chests,leggings,skeletonBody; 
	Random random;
	
	
	public Enemy() {
		
	}
	
	public Enemy(int x,int y,int width,int height,int difficulty,String type,int playerLevel,String location) {
		this.x = x;
		this.y = y;
		this.hp = maxHP;
		w = width;
		h = height;
		damage = 0;
		random = new Random();
		this.location = location;
		
		hpScaleFactor = 30;
		attackScaleFactor = 4;
		
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
		
		orcBody = new ImageManager[] {
				orcHead = new ImageManager("assets/img/heads/orcHead.png"),
				skeletonChest = new ImageManager("assets/img/torsos/orcBody.png"),
				skeletonLegs = new ImageManager("assets/img/legs/orcLegs.png")
		};
		
		skeletonBody = new ImageManager[] {
				skull = new ImageManager("assets/img/heads/skull.png"),
				skeletonChest = new ImageManager("assets/img/torsos/skeleton.png"),
				skeletonLegs = new ImageManager("assets/img/legs/skeleton.png")
		};
		
		humanBody = new ImageManager[] {
				humanHead = new ImageManager("assets/img/heads/humanHead.png"),
				humanTorso = new ImageManager("assets/img/torsos/humanBody.png"),
				humanLegs = new ImageManager("assets/img/legs/humanLegs.png")
		};
		
		helmets = new ImageManager[] {
				leatherHelm = new ImageManager("assets/img/armor/helmets/leather.png"),
				hoodHelm = new ImageManager("assets/img/armor/helmets/hood.png"),
				thiefHelm = new ImageManager("assets/img/armor/helmets/thief.png"),
				steelHelm = new ImageManager("assets/img/armor/helmets/steel.png"),
				t2ThiefHood = new ImageManager("assets/img/armor/helmets/tier2Thief.png")	
		};
		
		chests = new ImageManager[] {
				leatherChest = new ImageManager("assets/img/armor/chestplates/leather.png"),
				thiefChest = new ImageManager("assets/img/armor/chestplates/thief.png"),
				robeChest = new ImageManager("assets/img/armor/chestplates/robe.png"),
				steelChest= new ImageManager("assets/img/armor/chestplates/steel.png"),
				t2ThiefRobe = new ImageManager("assets/img/armor/chestplates/tier2Thief.png")	
		};
		
		leggings = new ImageManager[] {
				steelLeggings = new ImageManager("assets/img/armor/leggings/steel.png"),
				t2ThiefLeggings = new ImageManager("assets/img/armor/leggings/tier2thief.png")	
		};
		
		if(type.equals("skeleton")) {
			character = new ImageManager[] {
					skeletonBody[0],skeletonBody[1],skeletonBody[2]
			};
		}
		
		xp = (int)(((difficulty*difficulty) + (playerLevel*2))/2);
		
		hp = maxHP;
	}
	
	public Enemy(int x,int y,int maxHP,int attack,int width,int height,String name,String race,String location,ImageManager helmet,ImageManager chest,ImageManager legging) {
		this.x = x;
		this.y = y;
		this.maxHP = maxHP;
		this.hp = maxHP;
		this.attack = attack;
		this.race = race;
		this.location = location;
		this.helmet = helmet;
		this.legging = legging;
		this.chest = chest;
		this.name = name;
		w = width;
		h = height;
		damage = 0;
		random = new Random();
		
		hostile = false;
		
		orcBody = new ImageManager[] {
				orcHead = new ImageManager("assets/img/heads/orcHead.png"),
				orcTorso = new ImageManager("assets/img/torsos/orcBody.png"),
				orcLegs = new ImageManager("assets/img/legs/orcLegs.png")
		};
		
		skeletonBody = new ImageManager[] {
				skull = new ImageManager("assets/img/heads/skull.png"),
				skeletonChest = new ImageManager("assets/img/torsos/skeleton.png"),
				skeletonLegs = new ImageManager("assets/img/legs/skeleton.png")
		};
		
		humanBody = new ImageManager[] {
				humanHead = new ImageManager("assets/img/heads/humanHead.png"),
				humanTorso = new ImageManager("assets/img/torsos/humanBody.png"),
				humanLegs = new ImageManager("assets/img/legs/humanLegs.png")
		};
		
		helmets = new ImageManager[] {
				leatherHelm = new ImageManager("assets/img/armor/helmets/leather.png"),
				hoodHelm = new ImageManager("assets/img/armor/helmets/hood.png"),
				thiefHelm = new ImageManager("assets/img/armor/helmets/thief.png"),
				steelHelm = new ImageManager("assets/img/armor/helmets/steel.png"),
				t2ThiefHood = new ImageManager("assets/img/armor/helmets/tier2Thief.png")	
		};
		
		chests = new ImageManager[] {
				leatherChest = new ImageManager("assets/img/armor/chestplates/leather.png"),
				thiefChest = new ImageManager("assets/img/armor/chestplates/thief.png"),
				robeChest = new ImageManager("assets/img/armor/chestplates/robe.png"),
				steelChest= new ImageManager("assets/img/armor/chestplates/steel.png"),
				t2ThiefRobe = new ImageManager("assets/img/armor/chestplates/tier2Thief.png")	
		};
		
		leggings = new ImageManager[] {
				steelLeggings = new ImageManager("assets/img/armor/leggings/steel.png"),
				t2ThiefLeggings = new ImageManager("assets/img/armor/leggings/tier2thief.png")	
		};
		
		character = new ImageManager[3];
	}
	
	public void calcDamage(int helmet, int chest, int legs,int luck) {
		damage = ((((attack*(random.nextInt(3)+1))+(random.nextInt(9)))/((helmet+chest+legs)+1))*((random.nextInt(luck)+1)+10));
	}
}

