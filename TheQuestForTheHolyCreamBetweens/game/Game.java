package com.game;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import com.game.Tester;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Random;
import com.game.objects.*;
import com.utility.*;
import com.game.objects.InventoryItem;

import com.managers.*;
import com.prediction_ai.Predictor;
import com.game.displays.*;

import javax.swing.Timer;

import com.window.Display;

public class Game implements ActionListener {
//caughtChecker will see if caught is under 10 for a certain amount of time(since it still catches you while above 9)
	public int WIDTH, HEIGHT, caughtChecker, pressDelay, skillPoints, sneakBonus, intBonus, strBonus, agiBonus, luckBonus, hpBonus;
	public boolean running, playerMenuVisible, wanted, hostile, typing, North, South, West, East;
	public KeyManager keymanager = new KeyManager();
	public MouseManager mousemanager = new MouseManager();
	public double caught,luckDegrader,oldProb,prob;
	public Graphics g;
	public String playerMenuState,reading;
	public int dialogueSlide, weaponDamage, damage, woodsMonsterChance,walkAnimTimer,yChange,yOffset,xChange;
	public int xOffset,oldXp,odioTimer,difference,guard2Respawn,oldLevel,timesGameOpened;
	public String swapHelm;
	public String turn;	//who's turn it is to attack
	public boolean buyMenuVisible, launchingFireball, inventoryVisible, statsVisible, walking;
	public boolean talkingToClerk, talkingToGuard, talkingToCitizen, selectingTarget, weirdTreeFinished,inDungeon;
	public int guardRespawn, fireballX, fireballY;
	public Enemy enemyTarget;
	public Journal questJournal;
	public Predictor predictor;
	public int[] surroundingLocations;
	public int healthWidth,manaWidth,manaBonus;
	public int chopped,musicTimer,threshHold,letterTimer;
	public Enemy[] enemies,posterEnemies,guards;
	public float percent;
	public NoticeBoard[] boards;
	public Audio audioManager;
	public InventoryItem[] inventoryItems;
	public String[] clothes;
	public String lastWorn;
	public Tester tester;
	public Dungeon firstDungeon;
	public Dungeon[] dungeons;
	public Enemy[] dungeonEnemies;
	public Bloodline bloodline;
	public char oldChar;
	public FPScounter FPSCounter;
	public ShopItem lastItem;
	
	public Location Town,Woods,Woods2,Woods3,Woods4,Woods5,Woods6,Woods7,Camp,Town2;
	
	public Location[] locations;
	
	public InventoryItem leatherHelmetItem,leatherChestItem,mageHoodItem,mageRobeItem,thiefHoodItem,thiefRobeItem,woodSwordItem,
	shankItem,daggerItem,wandItem,staffItem,steelSwordItem,steelHelmetItem,steelChestItem,steelPantsItem,t2ThiefHoodItem,t2ThiefRobeItem,
	t2ThiefPantsItem,hpPotionItem,manaPotionItem,fireballItem,lesserHealItem;
	
	//music & sounds
	public String overworld,mainTheme,door,itemPickup,paperSound,potionSound,writingSound,swing;
	
	//shops
	public Shop Armor,Blacksmith,Armor2,Blacksmith2,MagicShop;
	
	public NoticeBoard board;
	
	public Shop[] shops;
	
	public boolean attackShowing;
	
	//public Audio audio;
	
	public Building[] buildings;
	
	public ResourceManager resourceManager;
	public SaveData crazyData;
	
	public int averageDamage, timesYouDeltDamage, lastDamageDelt, helm, ch, pa;
	
	//public Ann ann;
	
	public int clerkx, clerky, clerkw, clerkh;
	
	public int menuBox_x, menuBox_y, menuBox_w, menuBox_h;
	
	//enemies
	public Enemy chicken;
	
	//quests
	Quest weirdTree;
	
	//buildings
	public Building blackSmith,Armorer,Castle,blackSmith2,Armorer2,Castle2,magicShop,dungeon1;

	//shop items
	public ShopItem leather_chest,thief_hood,thief_robe,mage_hood,mage_robe,woodSword,SteelSword,Dagger,Shank;
	public ShopItem Wand,Staff,steel_chest,steel_helm,steel_leggings,Dagger2,SteelSword2,hpPotion,mPotion,lesserHeal,leatherHelmet,
	tier2ThiefHood,tier2ThiefRobe,tier2ThiefPants;
	
	//people
	public Person armorerClerk,jim,king;
	
	Random random;	//creates the random game object
	
	String playerWeapon;
	String holdingSpot;
	
	//dialogues
	Dialogue caughtDialogue;
	Dialogue armorerDialogue;
	Dialogue citizenDialogue;
	
	//Guards
	public Enemy guard,guard2;
	
	//misc images
	public ImageManager noticeBoard,bigBoard,poster1,poster2,poster3,paper,wantedposter,healIcon,characterLayout,
	background,bones,bonePile,Door,creationMenu,statsMenu,bigSteel;
	
	//vfx
	public ImageManager heal1,heal2,heal3,heal4,heal5,heal6,heal7,heal8,heal9;
	
	//weapons
	public ImageManager woodenSword,steelSword,wand,staff,shank,dagger,woodenSword_I,steelSword_I,wand_I;
	public ImageManager staff_I,shank_I,dagger_I,map;
	
	//enemy images
	public ImageManager chicken_i;
	
	//inventory slot position
	public int slot1x, slot1y, slot2x, slot2y, slot3x, slot3y, slot4x, slot4y, slot5x, slot5y, slot6x, slot6y;
	public int slot7x, slot7y, slot8x, slot8y, slot9x, slot9y, slot10x, slot10y, slot11x, slot11y, slot12x,
	slot12y;
	
	//locations
	public ImageManager town,town2,armorer,blacksmith,castle,woods,woods2,woods3,woods4,woods5,woods6,woods7;
	public ImageManager camp,chop,woodsNormal,mShop;
	
	//spells
	public ImageManager fire_ball;
	
	//people images
	public ImageManager armorClerk, guardImage,kingImage;
	
	//body part images
	public ImageManager orcHead,orcTorso,orcLegs,toolbar_orc,toolbar_human,toolbar_leather,toolbar_thief,
	toolbar_skull;
	public ImageManager toolbar_steel,toolbar_hood,humanHead,humanTorso,humanLegs,mediumOrcTorso,
	mediumHumanTorso,skull,skeletonTorso,skeletonLegs;
	
	//inventory items (_I will be for inventory items)
	public ImageManager leatherChest_I,mageRobe_I,thiefRobe_I,steelChest_I,steelLeggings_I;	
	
	//armor images
	public int armorx, armory, armorw, armorh, armorRestock;
	public boolean armorVisible;
	
	//helmets
	public ImageManager hoodHelm,leatherHelm,thiefHelm,steelHelm,crown,mediumSteelHelm,mediumCrown,tier2ThiefHoodImage,
	mediumTier2ThiefHood,bigTier2ThiefHood;

	//chest plates
	public ImageManager leatherChest,thiefChest,robeChest,steelChest,cape,mediumLeatherChest,
	mediumMageChest,mediumThiefChest,mediumSteelChest,mediumCape,tier2ThiefRobeImage,tier2ThiefRobeImage_I,
	mediumTier2ThiefRobe;
	
	//leggings
	public ImageManager steelLeggings,mediumSteelLegs,mediumPants,tier2ThiefPantsImage,tier2ThiefPantsImage_I,
	mediumTier2ThiefPants;
	
	
	//button images
	public ImageManager newGame,newGameActive,left,leftActive,right,rightActive,done,doneActive,ok,okActive;
	public ImageManager southButton,southButtonActive,northButton,northButtonActive,compass,backpack;
	public ImageManager magic,diary,diaryActive,backpackActive,magicActive,character,characterActive,no;
	public ImageManager noActive,save,saveActive,load,loadActive,healthPotion,manaPotion,smallLeft,
	smallLeftActive,smallRight,smallRightActive,smallDown;
	
	//UI
	public ImageManager bigOrcHead,bigHumanHead,mediumOrc,mediumHuman,bigLeather,bigHood,bigThief,mediumLeather,
	bigSkull,mediumSkull,mediumSkeletonChest,mediumSkeletonLegs;
	public ImageManager mediumHood,mediumThief,cursor,journal,attackCursor;
	
	//buttons
	public Button newGameButton,raceLeft,raceRight,hpLeft,hpRight,strLeft,strRight,luckLeft,luckRight,IntLeft;
	public Button IntRight,sneakLeft,sneakRight,speechLeft,speechRight,classLeft,classRight,doneButton;
	public Button playerMenuLeft,playerMenuRight,okButton,south,north,west,east,Diary,Inventory,stats,noButton;
	public Button saveButton,loadButton,boardBack,posterBack,Name;
	
	//spell books
	public ImageManager lesserHealBook,fireballBook;
	
	//player, window, and game state
	Player player;
	public String state;
	Display display;
	Console console;
	Console goldDisplay;
	Console fineDisplay;
	public String location;
	public boolean creatingCharacter;
	 
	public Game(int w, int h) {
		display = new Display(w,h);
		audioManager = new Audio();
		Timer timer = new Timer(20, this);	//idk how this timer works i found it in a youtube video lol
		player = new Player();
		cursor = new ImageManager("assets/img/cursor.png");
		attackCursor = new ImageManager("assets/img/attack.png");
		dialogueSlide = 0;
		clothes = new String[5];
		tester = new Tester();
		
		FPSCounter = new FPScounter();
		
		Town = new Location("town",null,null,null,"woods");
		Town2 = new Location("town2","woods3","woods",null,null);
		Woods = new Location("woods","woods2","town",null,"town2");
		Woods2 = new Location("woods2","woods5","woods4","woods","woods3");
		Woods3 = new Location("woods3","woods6","woods2","town2",null);
		Woods4 = new Location("woods4",null,null,null,"woods2");
		Woods5 = new Location("woods5",null,null,"woods2","woods6");
		Woods6 = new Location("woods6","woods7","woods5","woods3",null);
		Woods7 = new Location("woods7",null,"camp","woods6",null);
		Camp = new Location("camp",null,null,null,"woods7");
		
		locations = new Location[] {
				Town,Town2,Woods,Woods2,Woods3,Woods4,Woods5,Woods6,Woods7,Camp
		};
		
		luckDegrader = 1;
		oldProb = 0.1;
		
		North = false;
		South = false;
		East = false;
		West = false;
		
		typing = false;
		
		prob = 0;
		
		bloodline = new Bloodline();
		
		Door = new ImageManager("assets/img/dungeons/door.png");
		
		lastWorn = "";
		
		oldChar = keymanager.lastKeyPressed;
		
		timesGameOpened = 0;
		
		musicTimer = -3;
		weirdTreeFinished = true;
		
		reading = "";
		
		attackShowing = false;
		
		board = new NoticeBoard(32*22,32*6,"town",player.level);
		
		for(int i = 0; i < board.posters.length; i++) {
			board.posters[i].boardLocation = "board1";
		}
		
		boards = new NoticeBoard[] {
				board
		};
		
		walking = false;
		
		
		xChange = 0;
		yChange = 0;
		xOffset = 0;
		yOffset = 0;
		
		inDungeon = false;
		
		oldLevel = player.level;
		
		resourceManager = new ResourceManager();
		crazyData = new SaveData();
		
		musicTimer = -3;
		
		averageDamage = 0;
		timesYouDeltDamage = 0;
		lastDamageDelt = 0;
		
		
		//audio = new Audio();
		
		walkAnimTimer = -3;
		
		inventoryVisible = true;
		
		//dungeons
		firstDungeon = new Dungeon(5,"dungeon1",5);
		firstDungeon.Generate();
		dungeon1 = new Building(32*3,32*2,"Dungeon","woods6",firstDungeon.Rooms[0].dungeonName);
		
		
		dungeons = new Dungeon[] {
				firstDungeon
		};
		
		dungeonEnemies = new Enemy[firstDungeon.Rooms.length];
		
		for(int r = 0; r < dungeons.length; r++) {
			for(int c = 0; c < dungeons[r].Rooms.length; c++) {
				if(dungeons[r].Rooms[c] != null) {
					for(int f = 0; f < dungeons[r].Rooms[c].enemyClasses.length; f++) {
						if(dungeons[r].Rooms[c].enemyClasses[f] != null) {
							dungeons[r].Rooms[c].enemyClasses[f].damage = -1;
						}
					}
				}
			}
		}
		
		for(int r = 0; r < dungeons.length; r++) {
			for(int i = 0; i < dungeonEnemies.length; i++) {
				for(int c = 0; c < dungeons[r].Rooms.length; c++) {
					if(dungeons[r].Rooms[c] != null) {
						for(int f = 0; f < dungeons[r].Rooms[c].enemyClasses.length; f++) {
							if(dungeons[r].Rooms[c].enemyClasses[f] != null) {
								dungeonEnemies[i] = dungeons[r].Rooms[c].enemyClasses[f];
							}
						}
					}
				}
			}
		}
		
		//ann = new Ann();
		//ann.adjustLocationConnections(false, 0, 0, ann.loc1Connections);
		
		fireballX = 32*16;
		fireballY = player.y;
		
		letterTimer = -3;
		
		//music & sounds
		overworld = "assets/audio/music/normalTheme.wav";
		mainTheme = "assets/audio/music/aSolomnBeginning.wav";
		door = "assets/audio/sfx/door.wav";
		itemPickup = "assets/audio/sfx/itemPickup.wav";
		paperSound = "assets/audio/sfx/paper.wav";
		potionSound = "assets/audio/sfx/potion.wav";
		writingSound = "assets/audio/sfx/writing.wav";
		swing = "assets/audio/sfx/swing.wav";
		
		audioManager.playSound(mainTheme, true);
		
		//spell books
		fireballBook = new ImageManager("assets/img/spells/spellBooks/fireball.png");
		lesserHealBook = new ImageManager("assets/img/spells/spellBooks/lesserHeal.png");
		
		//misc images
		noticeBoard = new ImageManager("assets/img/misc/board.png");
		bigBoard = new ImageManager("assets/img/misc/bigBoard.png");
		poster1 = new ImageManager("assets/img/misc/poster1.png");
		poster2 = new ImageManager("assets/img/misc/poster2.png");
		poster3 = new ImageManager("assets/img/misc/poster3.png");
		healthPotion = new ImageManager("assets/img/inventory/potions/health.png");
		manaPotion = new ImageManager("assets/img/inventory/potions/mana.png");
		paper = new ImageManager("assets/img/inventory/misc/paper.png");
		wantedposter = new ImageManager("assets/img/toolbar/wantedPoster.png");
		map = new ImageManager("assets/img/menus/map.png");
		characterLayout = new ImageManager("assets/img/menus/character.png");
		background = new ImageManager("assets/img/menus/background.png");
		creationMenu = new ImageManager("assets/img/menus/characterCreation.png");
		statsMenu = new ImageManager("assets/img/menus/stats.png");
		
		//spells
		fire_ball = new ImageManager("assets/img/spells/fireball.png");
		healIcon = new ImageManager("assets/img/spells/heal.png");
		
		//enemies
		chicken_i = new ImageManager("assets/img/enemies/chicken.png");
		
		//people
		armorClerk = new ImageManager("assets/img/people/armorClerk.png");
		guardImage = new ImageManager("assets/img/people/guard.png");
		
		//vfx
		heal1 = new ImageManager("assets/img/vfx/healing/1.png");
		heal2 = new ImageManager("assets/img/vfx/healing/2.png");
		heal3 = new ImageManager("assets/img/vfx/healing/3.png");
		heal4 = new ImageManager("assets/img/vfx/healing/4.png");
		heal5 = new ImageManager("assets/img/vfx/healing/5.png");
		heal6 = new ImageManager("assets/img/vfx/healing/6.png");
		heal7 = new ImageManager("assets/img/vfx/healing/7.png");
		heal8 = new ImageManager("assets/img/vfx/healing/8.png");
		heal9 = new ImageManager("assets/img/vfx/healing/9.png");
		
		//body parts
		orcHead = new ImageManager("assets/img/heads/orcHead.png");
		orcTorso = new ImageManager("assets/img/torsos/orcBody.png");
		orcLegs = new ImageManager("assets/img/legs/orcLegs.png");
		humanHead = new ImageManager("assets/img/heads/humanHead.png");
		humanTorso = new ImageManager("assets/img/torsos/humanBody.png");
		humanLegs = new ImageManager("assets/img/legs/humanLegs.png");
		skull = new ImageManager("assets/img/heads/skull.png");
		skeletonTorso = new ImageManager("assets/img/torsos/skeleton.png");
		skeletonLegs = new ImageManager("assets/img/legs/skeleton.png");
		mediumHumanTorso = new ImageManager("assets/img/torsos/mediums/mediumHuman.png");
		mediumOrcTorso = new ImageManager("assets/img/torsos/mediums/mediumOrc.png");
		
		//weapons
		woodenSword = new ImageManager("assets/img/weapons/swords/woodenSword.png");
		steelSword = new ImageManager("assets/img/weapons/swords/steelSword.png");
		wand = new ImageManager("assets/img/weapons/magic/wand.png");
		staff = new ImageManager("assets/img/weapons/magic/staff.png");
		shank = new ImageManager("assets/img/weapons/knives/shank.png");
		dagger = new ImageManager("assets/img/weapons/knives/dagger.png");
		woodenSword_I = new ImageManager("assets/img/inventory/weapons/woodenSword.png");
		steelSword_I = new ImageManager("assets/img/inventory/weapons/steelSword.png");
		wand_I = new ImageManager("assets/img/inventory/weapons/wand.png");
		staff_I = new ImageManager("assets/img/inventory/weapons/staff.png");
		shank_I = new ImageManager("assets/img/inventory/weapons/shank.png");
		dagger_I = new ImageManager("assets/img/inventory/weapons/dagger.png");
		
		//big things
		bigOrcHead = new ImageManager("assets/img/heads/big heads/BigOrcHead.png");
		bigHumanHead = new ImageManager("assets/img/heads/big heads/BigHumanHead.png");
		bigSkull = new ImageManager("assets/img/heads/big heads/skull.png");
		mediumSkull = new ImageManager("assets/img/heads/mediums/skull.png");
		mediumSkeletonChest = new ImageManager("assets/img/torsos/mediums/skeleton.png");
		mediumSkeletonLegs = new ImageManager("assets/img/legs/mediums/skeleton.png");
		mediumOrc = new ImageManager("assets/img/heads/mediums/mediumOrc.png");
		mediumSteelHelm = new ImageManager("assets/img/armor/helmets/mediums/steel.png");
		mediumCrown = new ImageManager("assets/img/armor/helmets/mediums/crown.png");
		mediumLeatherChest = new ImageManager("assets/img/armor/chestplates/mediums/mediumLeather.png");
		mediumMageChest = new ImageManager("assets/img/armor/chestplates/mediums/mediumMage.png");
		mediumThiefChest = new ImageManager("assets/img/armor/chestplates/mediums/mediumThief.png");
		mediumSteelChest = new ImageManager("assets/img/armor/chestplates/mediums/mediumSteel.png");
		mediumSteelLegs = new ImageManager("assets/img/armor/leggings/mediums/mediumSteelLegs.png");
		mediumPants = new ImageManager("assets/img/legs/mediums/mediumPants.png");
		mediumCape = new ImageManager("assets/img/armor/chestplates/mediums/mediumCape.png");
		mediumHuman = new ImageManager("assets/img/heads/mediums/mediumHuman.png");
		bigLeather = new ImageManager("assets/img/armor/helmets/bighelmets/leather.png");
		bigThief = new ImageManager("assets/img/armor/helmets/bighelmets/thief.png");
		bigHood = new ImageManager("assets/img/armor/helmets/bighelmets/hood.png");
		mediumLeather = new ImageManager("assets/img/armor/helmets/mediums/leather.png");
		mediumThief = new ImageManager("assets/img/armor/helmets/mediums/thief.png");
		mediumHood = new ImageManager("assets/img/armor/helmets/mediums/hood.png");
		toolbar_orc = new ImageManager("assets/img/toolbar/heads/orc.png");
		bigSteel = new ImageManager("assets/img/armor/helmets/bighelmets/steel.png");
		toolbar_human = new ImageManager("assets/img/toolbar/heads/human.png");
		toolbar_skull = new ImageManager("assets/img/toolbar/heads/skull.png");
		toolbar_leather = new ImageManager("assets/img/toolbar/helmets/leather.png");
		toolbar_thief = new ImageManager("assets/img/toolbar/helmets/thief.png");
		toolbar_hood = new ImageManager("assets/img/toolbar/helmets/hood.png");
		toolbar_steel = new ImageManager("assets/img/toolbar/helmets/steel.png");
		journal = new ImageManager("assets/img/toolbar/book.png");
		
		//locations
		town = new ImageManager("assets/img/locations/town.jpg");
		town2 = new ImageManager("assets/img/locations/town2.png");
		armorer = new ImageManager("assets/img/locations/armorer.jpg");
		woods = new ImageManager("assets/img/locations/woods.jpg");
		woods2 = new ImageManager("assets/img/locations/woods2.png");
		woods3 = new ImageManager("assets/img/locations/woods3.png");
		woods4 = new ImageManager("assets/img/locations/woods4.png");
		woods5 = new ImageManager("assets/img/locations/woods5.png");
		woods6 = new ImageManager("assets/img/locations/woods6.png");
		woods7 = new ImageManager("assets/img/locations/woods7.png");
		camp = new ImageManager("assets/img/locations/camp.png");
		blacksmith = new ImageManager("assets/img/locations/weapons.jpg");
		mShop = new ImageManager("assets/img/locations/magic.png");
		castle = new ImageManager("assets/img/locations/castle.jpg");
		chop = new ImageManager("assets/img/locations/chopped.png");
		woodsNormal = new ImageManager("assets/img/locations/woodsNormal.png");
		
		//armor
		leatherHelm = new ImageManager("assets/img/armor/helmets/leather.png");
		hoodHelm = new ImageManager("assets/img/armor/helmets/hood.png");
		thiefHelm = new ImageManager("assets/img/armor/helmets/thief.png");
		leatherChest = new ImageManager("assets/img/armor/chestplates/leather.png");
		thiefChest = new ImageManager("assets/img/armor/chestplates/thief.png");
		robeChest = new ImageManager("assets/img/armor/chestplates/robe.png");
		leatherChest_I = new ImageManager("assets/img/inventory/armor/leatherChest.png");
		mageRobe_I = new ImageManager("assets/img/inventory/armor/mageRobe.png");
		thiefRobe_I = new ImageManager("assets/img/inventory/armor/thiefRobe.png");
		steelChest_I = new ImageManager("assets/img/inventory/armor/steelChest.png");
		steelLeggings_I = new ImageManager("assets/img/inventory/armor/steelLeggings.png");
		steelChest= new ImageManager("assets/img/armor/chestplates/steel.png");
		steelHelm = new ImageManager("assets/img/armor/helmets/steel.png");
		steelLeggings = new ImageManager("assets/img/armor/leggings/steel.png");
		crown = new ImageManager("assets/img/armor/helmets/crown.png");
		cape = new ImageManager("assets/img/armor/chestplates/cape.png");
		kingImage = new ImageManager("assets/img/people/king.png");
		tier2ThiefHoodImage = new ImageManager("assets/img/armor/helmets/tier2Thief.png");
		tier2ThiefRobeImage = new ImageManager("assets/img/armor/chestplates/tier2Thief.png");
		tier2ThiefPantsImage = new ImageManager("assets/img/armor/leggings/tier2Thief.png");
		tier2ThiefRobeImage_I = new ImageManager("assets/img/inventory/armor/tier2ThiefRobe.png");
		tier2ThiefPantsImage_I = new ImageManager("assets/img/inventory/armor/tier2thiefLeggings.png");
		bigTier2ThiefHood = new ImageManager("assets/img/toolbar/helmets/tier2Thief.png");
		mediumTier2ThiefHood = new ImageManager("assets/img/armor/helmets/mediums/tier2Thief.png");
		mediumTier2ThiefRobe = new ImageManager("assets/img/armor/chestplates/mediums/tier2Thief.png");
		mediumTier2ThiefPants = new ImageManager("assets/img/armor/leggings/mediums/tier2Thief.png");
		
		//buttons
		newGame = new ImageManager("assets/img/buttons/newGame.png");
		newGameActive = new ImageManager("assets/img/buttons/newGameActive.png");
		left = new ImageManager("assets/img/buttons/left.png");
		leftActive = new ImageManager("assets/img/buttons/leftActive.png");
		right = new ImageManager("assets/img/buttons/right.png");
		rightActive = new ImageManager("assets/img/buttons/rightActive.png");
		smallLeft = new ImageManager("assets/img/buttons/smallLeft.png");
		smallDown = new ImageManager("assets/img/buttons/smallDown.png");
		smallLeftActive = new ImageManager("assets/img/buttons/smallLeftActive.png");
		smallRight = new ImageManager("assets/img/buttons/smallRight.png");
		smallRightActive = new ImageManager("assets/img/buttons/smallRightActive.png");
		done = new ImageManager("assets/img/buttons/done.png");
		doneActive = new ImageManager("assets/img/buttons/doneActive.png");
		ok = new ImageManager("assets/img/buttons/ok.png");
		okActive = new ImageManager("assets/img/buttons/okActive.png");
		southButton = new ImageManager("assets/img/buttons/south.png");
		southButtonActive = new ImageManager("assets/img/buttons/southActive.png");
		northButton = new ImageManager("assets/img/buttons/north.png");
		northButtonActive = new ImageManager("assets/img/buttons/northActive.png");
		compass = new ImageManager("assets/img/buttons/compass.png");
		backpack = new ImageManager("assets/img/buttons/backpack.png");
		backpackActive = new ImageManager("assets/img/buttons/backpackActive.png");
		magic = new ImageManager("assets/img/buttons/magic.png");
		magicActive = new ImageManager("assets/img/buttons/magicActive.png");
		diary = new ImageManager("assets/img/buttons/diary.png");
		diaryActive = new ImageManager("assets/img/buttons/diaryActive.png");
		character = new ImageManager("assets/img/buttons/character.png");
		characterActive = new ImageManager("assets/img/buttons/characterActive.png");
		no = new ImageManager("assets/img/buttons/no.png");
		noActive = new ImageManager("assets/img/buttons/noActive.png");
		save = new ImageManager("assets/img/buttons/save.png");
		saveActive = new ImageManager("assets/img/buttons/saveActive.png");
		load = new ImageManager("assets/img/buttons/load.png");
		loadActive = new ImageManager("assets/img/buttons/loadActive.png");
		
		//inventory items
		leatherHelmetItem = new InventoryItem("leather helmet",leatherHelm.guy," 5 armor, tier 1","helmet",0,0);
		leatherChestItem = new InventoryItem("leather chest",leatherChest_I.guy," 7 armor, tier 1","chest",0,0);
		mageHoodItem = new InventoryItem("mage hood",hoodHelm.guy," 3 armor, tier 1","helmet",0,0);
		mageRobeItem = new InventoryItem("mage robe",mageRobe_I.guy," 4 armor, tier 1","chest",0,0);
		thiefHoodItem = new InventoryItem("thief hood",thiefHelm.guy," 3 armor, tier 1","helmet",0,0);
		thiefRobeItem = new InventoryItem("thief robe",thiefRobe_I.guy," 4 armor, tier 1","chest",0,0);
		t2ThiefHoodItem = new InventoryItem("thief hood t2",tier2ThiefHoodImage.guy," 6 armor, +3 luck, tier 2","helmet",3,luckBonus);
		t2ThiefRobeItem = new InventoryItem("thief robe t2",tier2ThiefRobeImage_I.guy," 8 armor, +3 agi, tier 2","chest",3,agiBonus);
		t2ThiefPantsItem = new InventoryItem("thief pants t2",tier2ThiefPantsImage_I.guy," 5 armor, +3 sneak, tier 2","pants",3,sneakBonus);
		steelHelmetItem = new InventoryItem("steel helm",steelHelm.guy," 7 armor, +2 luck, tier 2","helmet",2,luckBonus);
		steelChestItem = new InventoryItem("steel chest",steelChest_I.guy," 10 armor, +5 hp, tier 2","chest",5,hpBonus);
		steelPantsItem = new InventoryItem("steel leggings",steelLeggings_I.guy," 6 armor, +2 hp, tier 2","pants",2,hpBonus);
		woodSwordItem = new InventoryItem("wooden sword",woodenSword_I.guy," 3 weapon strength","weapon",0,0);
		steelSwordItem = new InventoryItem("steel sword",steelSword_I.guy," 5 weapon strength, +3 str","weapon",3,strBonus);
		shankItem = new InventoryItem("shank",shank_I.guy," 2 weapon strength","weapon",0,0);
		daggerItem = new InventoryItem("dagger",dagger_I.guy," 4 weapon strength","weapon",0,0);
		wandItem = new InventoryItem("wand",wand_I.guy," 1 weapon strength, +10 mana","weapon",0,0);
		hpPotionItem = new InventoryItem("health potion",healthPotion.guy," restores 30 hp","potion",0,0);
		manaPotionItem = new InventoryItem("mana potion",manaPotion.guy," restores 20 mana","potion",0,0);
		fireballItem = new InventoryItem("fireball",fire_ball.guy," deals 20 damage, cost: 10 mana","spell",0,0);
		lesserHealItem = new InventoryItem("lesser heal",healIcon.guy," restores 12 hp, cost: 25 mana","spell",0,0);
		
		inventoryItems = new InventoryItem[] {
			leatherHelmetItem,leatherChestItem,mageHoodItem,mageRobeItem,thiefHoodItem,thiefRobeItem,
			t2ThiefHoodItem,t2ThiefRobeItem,t2ThiefPantsItem,steelHelmetItem,steelChestItem,steelPantsItem,
			woodSwordItem,steelSwordItem,shankItem,daggerItem,wandItem,hpPotionItem,manaPotionItem,fireballItem,
			lesserHealItem
		};
		
		//shop items
		leather_chest = new ShopItem(32*12,32*4,32,32,30,"leather chest","armorer",leatherChest_I.guy);
		thief_hood = new ShopItem(32*14,32*4,32,32,5,"thief hood","armorer",thiefHelm.guy);
		thief_robe = new ShopItem(32*16,32*4,32,32,15,"thief robe","armorer",thiefRobe_I.guy);
		mage_hood = new ShopItem(32*18,32*4,32,32,5,"mage hood","armorer",hoodHelm.guy);
		mage_robe = new ShopItem(32*20,32*4,32,32,15,"mage robe","armorer",mageRobe_I.guy);
		woodSword = new ShopItem(32*12,32*4,32,32,20,"wooden sword","blacksmith",woodenSword_I.guy);
		SteelSword = new ShopItem(32*14,32*4,32,32,50,"steel sword","blacksmith",steelSword_I.guy);
		Shank = new ShopItem(32*16,32*4,32,32,3,"shank","blacksmith",shank_I.guy);
		Dagger = new ShopItem(32*18,32*4,32,32,20,"dagger","blacksmith",dagger_I.guy);
		Wand = new ShopItem(32*10,32*4,32,32,20,"wand","magic shop",wand_I.guy);
		Staff = new ShopItem(32*12,32*4,32,64,40,"staff","magic shop",staff_I.guy);
		steel_helm = new ShopItem(32*10,32*4,32,32,40,"steel helm","armorer2",steelHelm.guy);
		steel_chest = new ShopItem(32*12,32*4,32,32,100,"steel chest","armorer2",steelChest_I.guy);
		steel_leggings = new ShopItem(32*14,32*4,32,32,75,"steel leggings","armorer2",steelLeggings_I.guy);
		tier2ThiefHood = new ShopItem(32*16,32*4,32,32,35,"thief hood t2","armorer2",tier2ThiefHoodImage.guy);
		tier2ThiefRobe = new ShopItem(32*18,32*4,32,32,70,"thief robe t2","armorer2",tier2ThiefRobeImage_I.guy);
		tier2ThiefPants = new ShopItem(32*20,32*4,32,32,80,"thief pants t2","armorer2",tier2ThiefPantsImage_I.guy);
		Dagger2 = new ShopItem(32*10,32*4,32,32,20,"dagger","blacksmith2",dagger_I.guy);
		SteelSword2 = new ShopItem(32*12,32*4,32,32,50,"steel sword","blacksmith2",steelSword_I.guy);
		hpPotion = new ShopItem(32*10,32*4,32,32,15,"health potion","magic shop",healthPotion.guy);
		mPotion = new ShopItem(32*12,32*4,32,32,10,"mana potion","magic shop",manaPotion.guy);
		lesserHeal = new ShopItem(32*14,32*4,32,32,50,"lesser heal","magic shop",lesserHealBook.guy);
		leatherHelmet = new ShopItem(32*10,32*4,32,32,20,"leather helmet","armorer",leatherHelm.guy);
		
		Armor = new Shop(new ShopItem[] {leatherHelmet,leather_chest,thief_hood,mage_hood,mage_robe,thief_robe});
		Armor2 = new Shop(new ShopItem[] {steel_helm,steel_chest,steel_leggings,tier2ThiefHood,tier2ThiefRobe,tier2ThiefPants});
		Blacksmith = new Shop(new ShopItem[] {Dagger,woodSword,SteelSword,Shank});
		MagicShop = new Shop(new ShopItem[] {hpPotion,mPotion,lesserHeal});
		
		shops = new Shop[] {
			Armor,Armor2,Blacksmith,MagicShop
		};
		
		this.WIDTH = w;
		this.HEIGHT = h;
		
		timer.start();
		run();
	}
	
	public void calcPercent() {
		percent = (float) Math.floor((((double)player.xp/(double)player.maxXP)*100));
	}
	
	public void playerCalcDamage() {
		int decider = 0;
		int criticalDecider = 0;
		int variation = 0;
		int critical = 0;
		int critialChance = 0;
		
		decider = (int) ((Math.floor(random.nextInt(player.luck)/2)+1)-(Math.floor(random.nextInt(player.luck)/3)+1));
		 
		if(playerWeapon.equals("dagger")) {
			critical = weaponDamage*(player.agi+(random.nextInt(player.luck-5)+5));
			criticalDecider = (int) ((Math.floor(random.nextInt(player.luck)/2)+1)-(Math.floor(random.nextInt(player.luck)/3)+1));
		}
		
		if(playerWeapon.equals("shank")) {
			critical = weaponDamage*(player.agi+(random.nextInt(player.luck-5)+5));
			criticalDecider = (int) ((Math.floor(random.nextInt(player.luck)/2)+1)-(Math.floor(random.nextInt(player.luck)/3)+1));
		}
		
		if(decider < 2) {
			if(criticalDecider < 2) {
				variation = ((weaponDamage*(player.str+(random.nextInt(player.luck)+1)))/player.luck)*-1;
				damage = ((weaponDamage*player.str - (weaponDamage*player.str / player.luck)) + variation + critical);
			}
			else {
				variation = ((weaponDamage*(player.str+(random.nextInt(player.luck)+1)))/player.luck)*-1;
				damage = ((weaponDamage*player.str - (weaponDamage*player.str / player.luck)) + variation);
			}
		}
		else {
			if(criticalDecider < 2) {
				variation = (weaponDamage*(player.str+(random.nextInt(player.luck)+1)))/player.luck;
				damage = ((weaponDamage*player.str - (weaponDamage*player.str / player.luck)) + variation + critical);
			}
			else {
				variation = (weaponDamage*(player.str+(random.nextInt(player.luck)+1)))/player.luck;
				damage = ((weaponDamage*player.str - (weaponDamage*player.str / player.luck)) + variation);
			}
		}
	}
	
	public String enemyName() {
		for(int i = 0; i < enemies.length; i++) {
			if(mouseCollided(enemies[i].x,enemies[i].y,enemies[i].w,enemies[i].h) && location.equals(enemies[i].location)) {
				return enemies[i].name;
			}
		}
		
		return "";
	}
	
	public String dungeonEnemyName() {
		for(int i = 0; i < dungeons.length; i++) {
			for(int c = 0; c < dungeons[i].Rooms.length; c++) {
				if(dungeons[i].Rooms[c] != null) {
					for(int r = 0; r < dungeons[i].Rooms[c].enemyClasses.length; r++) {
						if(dungeons[i].Rooms[c].enemyClasses[r] != null) {
							if(mouseCollided(dungeons[i].Rooms[c].enemyClasses[r].x,dungeons[i].Rooms[c].enemyClasses[r].y,dungeons[i].Rooms[c].enemyClasses[r].w,dungeons[i].Rooms[c].enemyClasses[r].h) && location.equals(dungeons[i].Rooms[c].enemyClasses[r].location) && dungeons[i].Rooms[c].enemyClasses[r].hp > 0) {
								return dungeons[i].Rooms[c].enemyClasses[r].name;
							}
						}
					}
				}
			}
		}
		
		return "";
	}
	
	public void saveTimes() {
		TimesPlayed data = new TimesPlayed();
		data.updateData(timesGameOpened);
		
		try{
			resourceManager.save(data, "times.save");
		}
		catch(Exception e) {
			System.out.println("Error: could not save " + e.getMessage());
		}
	}
	
	public void loadTimes() {
		try {
			TimesPlayed data = (TimesPlayed) resourceManager.load("times.save");
			timesGameOpened = data.times;
		}
		catch(Exception e) {
			System.out.println("Error: could not load " + e.getMessage());
		}
	}
	
	public void saveYears() {
		SaveData data = new SaveData();
		bloodline.oldYear = bloodline.year;
		data.update3(bloodline.oldYear, bloodline.year);
		
		try{
			resourceManager.save(data, "years.save");
		}
		catch(Exception e) {
			System.out.println("Error: could not save " + e.getMessage());
		}
	}
	
	public void loadYears() {
		try {
			SaveData data = (SaveData) resourceManager.load("years.save");
			bloodline.oldYear = data.oldYear;
			
			/*
			System.out.println("year: " + bloodline.year);
			System.out.println("oldYear: " + bloodline.oldYear);
			System.out.println("dataoldYear: " + bloodline.oldYear);
			System.out.println("generations: " + bloodline.generations);
			System.out.println("relation: " + bloodline.relation);
			*/
		}
		catch(Exception e) {
			System.out.println("Error: could not load " + e.getMessage());
		}
	}
	
	public void saveGame() {
		WantedPoster[] posters = new WantedPoster[3];
		
		saveYears();
		
		SaveData data = new SaveData();
		data.update2(leather_chest.restockTimer,thief_hood.restockTimer,thief_robe.restockTimer,
				mage_hood.restockTimer,mage_robe.restockTimer, woodSword.restockTimer,
				SteelSword.restockTimer,Shank.restockTimer,Dagger.restockTimer,steel_helm.restockTimer,
				steel_chest.restockTimer,steel_leggings.restockTimer,hpPotion.restockTimer,mPotion.restockTimer);
		
		data.updateData(player.hp,player.maxHP,player.str,player.luck,player.Int,player.sneak,player.xp,
				player.maxXP,player.level,player.gold,player.agi,player.mana,player.maxMana,player.manaGain,
				player.xpChange,player.race,player.Class,player.helmet,player.chest,player.legs,player.inventory,
				player.spells,player.manaRegenTimer,playerWeapon,questJournal.questsCompleted,weirdTree.finished,
				location,chopped,wanted,guard.hostile,player.fine,guard.hp,guard2.hostile,caught,oldXp,
				oldLevel,skillPoints,player.oldHp,player.oldLuck,player.oldAgi,player.oldSneak,player.oldStr,
				player.oldInt,tier2ThiefHood.restockTimer,tier2ThiefRobe.restockTimer,tier2ThiefPants.restockTimer,
				lesserHeal.restockTimer,tester.average,luckBonus,strBonus,sneakBonus,hpBonus,agiBonus,player.name,
				bloodline.year);
		
		
		try{
			resourceManager.save(data, "save1.save");
		}
		catch(Exception e) {
			System.out.println("Error: could not save " + e.getMessage());
		}
	}
	
	public void loadGame() {
		try {
			SaveData data = (SaveData) resourceManager.load("save1.save");
			tier2ThiefHood.restockTimer = data.t2ThiefHoodRestock;
			tier2ThiefRobe.restockTimer = data.t2ThiefRobeRestock;
			tier2ThiefPants.restockTimer = data.t2ThiefPantsRestock;
			lesserHeal.restockTimer = data.lesserHealRestock;
			player.hp = data.hp;
			player.maxHP = data.maxHP;
			player.mana = data.mana;
			player.Int = data.Int;
			player.str = data.str;
			player.sneak = data.sneak;
			player.agi = data.speech;
			player.xp = data.xp;
			player.maxXP = data.maxXP;
			player.xpChange = data.xpChange;
			player.manaGain = data.manaGain;
			player.manaRegenTimer = data.manaRegenTimer;
			player.maxMana = data.maxMana;
			player.level = data.level;
			player.luck = data.luck;
			player.gold = data.gold;
			player.inventory = data.inventory;
			player.spells = data.spells;
			bloodline.year = data.year;
			player.race = data.race;
			player.Class = data.Class;
			player.name = data.name;
			player.helmet = data.helmet;
			player.chest = data.chest;
			tester.average = data.average;
			player.legs = data.legs;
			playerWeapon = data.weapon;
			questJournal.questsCompleted = data.quests;
			weirdTreeFinished = data.weirdTreeDone;
			location = data.location;
			chopped = data.chopped;
			wanted = data.wanted;
			guard.hostile = data.hostile;
			guard2.hostile = data.guard2hostile;
			player.fine = data.fine;
			leather_chest.restockTimer = data.leatherRestock;
			thief_hood.restockTimer = data.thiefHeadRestock;
			thief_robe.restockTimer = data.thiefChestRestock;
			mage_hood.restockTimer = data.mageHeadRestock;
			mage_robe.restockTimer = data.mageChestRestock;
			woodSword.restockTimer = data.woodSwordRestock;
			SteelSword.restockTimer = data.steelSwordRestock;
			Shank.restockTimer = data.shankRestock;
			Dagger.restockTimer = data.daggerRestock;
			steel_helm.restockTimer = data.steelHeadRestock;
			steel_chest.restockTimer = data.steelChestRestock;
			steel_leggings.restockTimer = data.steelLeggingsRestock;
			hpPotion.restockTimer = data.hpPotionRestock;
			mPotion.restockTimer = data.manaPotionRestock;
			skillPoints = data.skillPoints;
			player.oldHp = data.oldHp;
			player.oldLuck = data.oldLuck;
			player.oldAgi = data.oldAgi;
			player.oldInt = data.oldInt;
			player.oldSneak = data.oldSneak;
			player.oldStr = data.oldStr;
			caught = data.caught;
			oldXp = data.oldXp;
			luckBonus = data.luckBonus;
			strBonus = data.strBonus;
			sneakBonus = data.sneakBonus;
			hpBonus = data.hpBonus;
			agiBonus = data.agiBonus;
			
			calcPercent();
			
			player.x = 32*16;
			player.y = 32*10;
			
			inDungeon = false;
			firstDungeon.playerX = 0;
			firstDungeon.playerY = 0;
			
			
			woodsMonsterChance = 0;
			
			loadYears();
			
			oldXp = player.xp;
			oldLevel = player.level;
			
			board = new NoticeBoard(32*22,32*6,"town",player.level);
			boards[0] = board;
			
			posterEnemies = new Enemy[] {
					board.posters[0].enemy,board.posters[1].enemy,board.posters[2].enemy
			};
			
			enemies = new Enemy[] {
					chicken,guard,guard2,board.posters[0].enemy,board.posters[1].enemy,board.posters[2].enemy
			};
			
			dialogueSlide = 0;
			talkingToGuard = false;
			talkingToCitizen = false;
			talkingToClerk = false;
			
			reading = "";
			
			difference = 0;
			
			if(weirdTreeFinished == false) {
				questJournal.addQuest(weirdTree);
				weirdTree.finished = false;
				chopped = -3;
			}
			else {
				questJournal.removeQuest(weirdTree);
				weirdTree.finished = true;
			}
			
			slotCheck(1);
		}
		catch(Exception e) {
			System.out.println("Error: could not load " + e.getMessage());
		}
	}
	
	public void showDesk(String name,String type) {
		int xPosition = (mousemanager.mousex-((300/2)+((name.length()*15)/2)));
		String thing;
		
		g.setFont(new Font("Times New Roman",Font.BOLD,30));
		
		g.setColor(Color.gray.darker());
		g.fillRect(mousemanager.mousex - 300, mousemanager.mousey + 50, 300, 100);
		
		g.setColor(Color.black);
		g.drawRect(mousemanager.mousex - 300, mousemanager.mousey + 50, 300, 100);
		
		g.setColor(Color.black);
		g.drawString(name, xPosition, mousemanager.mousey + 110);
		
		g.setFont(new Font("Times New Roman",Font.BOLD,16));
		if(type.equals("spell")) {
			thing = "click to cast";
			g.drawString(thing, mousemanager.mousex-((300/2)+((thing.length()*7)/2)), mousemanager.mousey + 130);
		}
		else {
			if(type.equals("helmet") || type.equals("chest") || type.equals("pants") || type.equals("weapon")) {
				thing = "click to equip";
				g.drawString(thing, mousemanager.mousex-((300/2)+((thing.length()*7)/2)), mousemanager.mousey + 130);
			}
			else {
				if(type.equals("potion")) {
					thing = "click to drink";
					g.drawString(thing, mousemanager.mousex-((300/2)+((thing.length()*7)/2)), mousemanager.mousey + 130);
				}
			}
		}
	}
	
	public void spellCheck() {
		int[] slotXs = new int[] {slot1x,slot2x,slot3x,slot4x,slot5x,slot6x,slot7x,slot8x,slot9x,slot10x};
		int[] slotYs = new int[] {slot1y,slot2y,slot3y,slot4y,slot5y,slot6y,slot7y,slot8y,slot9y,slot10y};
		
		for(int i = 0; i < player.spells.length; i++) {
			if(player.spells[i] != null) {
				if(inventoryVisible == false) {
					if(player.spells[i].equals("fireball")) {
						g.drawImage(fire_ball.guy, slotXs[i], slotYs[i], null);
						
						//equips the helmet and replaces it's slot with what you were originally wearing
						if(mouseCollided(slotXs[i],slotYs[i],32,32)) {
							if(mousemanager.isLeftPressed() && pressDelay == 0) {
								selectingTarget = true;
							}
						}
					}
					else {
						if(player.spells[i].equals("lesser heal")) {
							g.drawImage(healIcon.guy, slotXs[i], slotYs[i], null);

							//equips the helmet and replaces it's slot with what you were originally wearing
							if(mouseCollided(slotXs[i],slotYs[i],32,32)) {
								if(mousemanager.isLeftPressed() && pressDelay == 0) {
									if(player.mana > 24){
										player.hp += 12;
										player.mana -= 25;
										player.manaRegenTimer = 50;
										pressDelay = 15;
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void statsArmorCheck() {
		int[] slotXs = new int[] {slot1x,slot2x,slot3x,slot4x,slot5x,slot6x,slot7x,slot8x,slot9x,slot10x,
				slot11x,slot12x};
		int[] slotYs = new int[] {slot1y,slot2y,slot3y,slot4y,slot5y,slot6y,slot7y,slot8y,slot9y,slot10y,
				slot11y,slot12y};
		//beans
		int[] statsMenuXs = new int[] {1049,1050,1049,1009};
		int[] statsMenuYs = new int[] {117,164,216,185};
		String[] equipment = new String[5]; 
		
		for(int i = 0; i < inventoryItems.length; i++) {
			if(player.helmet.equals(inventoryItems[i].name)) {
				if(playerMenuVisible) {
					g.drawImage(inventoryItems[i].image,statsMenuXs[0],statsMenuYs[0],null);
					equipment[0] = inventoryItems[i].name;
				}
				
				if(!inventoryItems[i].added) {
					inventoryItems[i].stat += inventoryItems[i].bonus;
					inventoryItems[i].added = true;
				}
				
				if(clothes[0] == null) {
					clothes[0] = player.helmet;
				}
				
				if(!player.helmet.equals(clothes[0])) {
					if(inventoryItems[i].stat > 0) {
						inventoryItems[i].stat += inventoryItems[i].bonus;
						inventoryItems[i].added = true;
					}
					
					clothes[0] = player.helmet;
				}
			}
			
			if(player.chest.equals(inventoryItems[i].name)) {
				if(playerMenuVisible) {
					g.drawImage(inventoryItems[i].image,statsMenuXs[1],statsMenuYs[1],null);
					equipment[1] = inventoryItems[i].name;
				}
			}
			
			if(player.legs.equals(inventoryItems[i].name)) {
				if(playerMenuVisible) {
					g.drawImage(inventoryItems[i].image,statsMenuXs[2],statsMenuYs[2],null);
					equipment[2] = inventoryItems[i].name;
				}
			}
			
			if(playerWeapon.equals(inventoryItems[i].name)) {
				if(playerMenuVisible) {
					g.drawImage(inventoryItems[i].image,statsMenuXs[3],statsMenuYs[3],null);
					equipment[3] = inventoryItems[i].name;
				}
			}
			
			for(int c = 0; c < equipment.length; c++) {
				if(mouseCollided(statsMenuXs[0],statsMenuYs[0],32,32)) {
					if(player.helmet.equals(equipment[c])) {
						if(equipment[c].equals(inventoryItems[i].name)) {
							g.setFont(new Font("Times New Roman",Font.BOLD,26));
							g.drawString(inventoryItems[i].desc, 887, 100);
							g.drawString(" " + inventoryItems[i].name, 887, 320);
						}
					}
				}
				else {
					if(mouseCollided(statsMenuXs[1],statsMenuYs[1],32,32)) {
						if(player.chest.equals(equipment[c])) {
							if(equipment[c].equals(inventoryItems[i].name)) {
								g.setFont(new Font("Times New Roman",Font.BOLD,26));
								g.drawString(inventoryItems[i].desc, 887, 100);
								g.drawString(" " + inventoryItems[i].name, 887, 320);
							}
						}
					}
					else {
						if(mouseCollided(statsMenuXs[2],statsMenuYs[2],32,32)) {
							if(player.legs.equals(equipment[c])) {
								if(equipment[c].equals(inventoryItems[i].name)) {
									g.setFont(new Font("Times New Roman",Font.BOLD,26));
									g.drawString(inventoryItems[i].desc, 887, 100);
									g.drawString(" " + inventoryItems[i].name, 887, 320);
								}
							}
						}
						else {
							if(mouseCollided(statsMenuXs[3],statsMenuYs[3],32,32)) {
								if(playerWeapon.equals(equipment[c])) {
									if(equipment[c].equals(inventoryItems[i].name)) {
										g.setFont(new Font("Times New Roman",Font.BOLD,26));
										g.drawString(inventoryItems[i].desc, 887, 100);
										g.drawString(" " + inventoryItems[i].name, 887, 320);
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void slotCheck(int load) {
		int[] slotXs = new int[] {slot1x,slot2x,slot3x,slot4x,slot5x,slot6x,slot7x,slot8x,slot9x,slot10x,
				slot11x,slot12x};
		int[] slotYs = new int[] {slot1y,slot2y,slot3y,slot4y,slot5y,slot6y,slot7y,slot8y,slot9y,slot10y,
				slot11y,slot12y};
		
		
		for(int i = 0; i < player.inventory.length; i++) {
			//moves each item over if an open slot is available
			if(player.inventory[i] == null || player.inventory[i].equals("")) {
				if(i < player.inventory.length-1) {
					if(player.inventory[i+1] != null && !player.inventory[i+1].equals("")) {
						player.inventory[i] = player.inventory[i+1];
						player.inventory[i+1] = "";
					}
				}	
			}
			
			for(int c = 0; c < inventoryItems.length; c++) {
				if(inventoryVisible) {
					if(player.inventory[i] != null) {
						if(player.inventory[i].equals(inventoryItems[c].name)) {
							if(mouseCollided(slotXs[i],slotYs[i],32,32)) {
								showDesk(inventoryItems[c].name,inventoryItems[c].type);
							}
						}
					}
				}
				else {
					//moves each item over if an open slot is available
					if(player.spells[i] == null || player.spells[i].equals("")) {
						if(i < player.spells.length-1) {
							if(player.spells[i+1] != null && !player.spells[i+1].equals("")) {
								player.spells[i] = player.spells[i+1];
								player.spells[i+1] = "";
							}
						}
					}
					
					if(player.spells[i] == null) {
						player.spells[i] = "";
					}
					
					if(player.spells[i] != null) {
						if(player.spells[i].equals(inventoryItems[c].name)) {
							if(mouseCollided(slotXs[i],slotYs[i],32,32)) {
								showDesk(inventoryItems[c].name,inventoryItems[c].type);
							}
						}
					}
				}
			}
		}
		
	
		for(int i = 0; i < player.inventory.length; i++) {
			//shows each item in the inventory
			if(player.inventory[i] != null) {
				if(inventoryVisible) {
					if(player.inventory[i].equals("leather helmet")) {
						g.drawImage(leatherHelm.guy, slotXs[i], slotYs[i], null);
						
						//equips the helmet and replaces it's slot with what you were originaly wearing
						if(mouseCollided(slotXs[i],slotYs[i],32,32)) {
							if(mousemanager.isLeftPressed() && pressDelay == 0) {
								swapHelm = player.inventory[i];
								lastWorn = player.helmet;
								player.inventory[i] = player.helmet;
								player.helmet = swapHelm;
								armorCheck();
								pressDelay = 15;
							}
						}
					}
					else {
						if(player.inventory[i].equals("mage hood")) {
							g.drawImage(hoodHelm.guy, slotXs[i], slotYs[i], null);
							
							if(mouseCollided(slotXs[i],slotYs[i],32,32)) {
								if(mousemanager.isLeftPressed() && pressDelay == 0) {
									swapHelm = player.inventory[i];
									lastWorn = player.helmet;
									player.inventory[i] = player.helmet;
									player.helmet = swapHelm;
									armorCheck();
									pressDelay = 15;
								}
							}
						}
						else {
							if(player.inventory[i].equals("thief hood")) {
								g.drawImage(thiefHelm.guy, slotXs[i], slotYs[i], null);
								
								if(mouseCollided(slotXs[i],slotYs[i],32,32)) {
									if(mousemanager.isLeftPressed() && pressDelay == 0) {
										swapHelm = player.inventory[i];
										lastWorn = player.helmet;
										player.inventory[i] = player.helmet;
										player.helmet = swapHelm;
										armorCheck();
										pressDelay = 15;
									}
								}
							}
							else {
								if(player.inventory[i].equals("leather chest")) {
									g.drawImage(leatherChest_I.guy, slotXs[i], slotYs[i], null);
									
									if(mouseCollided(slotXs[i],slotYs[i],32,32)) {
										if(mousemanager.isLeftPressed() && pressDelay == 0) {
											swapHelm = player.inventory[i];
											lastWorn = player.chest;
											player.inventory[i] = player.chest;
											player.chest = swapHelm;
											armorCheck();
											pressDelay = 15;
										}
									}
								}
								else {
									if(player.inventory[i].equals("thief robe")) {
										g.drawImage(thiefRobe_I.guy, slotXs[i], slotYs[i], null);
										
										if(mouseCollided(slotXs[i],slotYs[i],32,32)) {
											if(mousemanager.isLeftPressed() && pressDelay == 0) {
												swapHelm = player.inventory[i];
												lastWorn = player.chest;
												player.inventory[i] = player.chest;
												player.chest = swapHelm;
												armorCheck();
												pressDelay = 15;
											}
										}
									}
									else {
										if(player.inventory[i].equals("mage robe")) {
											g.drawImage(mageRobe_I.guy, slotXs[i], slotYs[i], null);
											
											if(mouseCollided(slotXs[i],slotYs[i],32,32)) {
												if(mousemanager.isLeftPressed() && pressDelay == 0) {
													swapHelm = player.inventory[i];
													lastWorn = player.chest;
													player.inventory[i] = player.chest;
													player.chest = swapHelm;
													armorCheck();
													pressDelay = 15;
												}
											}
										}
										else {
											if(player.inventory[i].equals(woodSword.name)) {
												g.drawImage(woodenSword_I.guy, slotXs[i], slotYs[i], null);
												
												//equips the helmet and replaces it's slot with what you were originaly wearing
												if(mouseCollided(slotXs[i],slotYs[i],32,32)) {
													if(mousemanager.isLeftPressed() && pressDelay == 0) {
														holdingSpot = woodSword.name;
														lastWorn = playerWeapon;
														player.inventory[i] = playerWeapon;
														playerWeapon = holdingSpot;
														armorCheck();
														pressDelay = 15;
													}
												}
											}
											else {
												if(player.inventory[i].equals("dagger")) {
													g.drawImage(dagger_I.guy, slotXs[i], slotYs[i], null);
													
													//equips the helmet and replaces it's slot with what you were originaly wearing
													if(mouseCollided(slotXs[i],slotYs[i],32,32)) {
														if(mousemanager.isLeftPressed() && pressDelay == 0) {
															holdingSpot = "dagger";
															lastWorn = playerWeapon;
															player.inventory[i] = playerWeapon;
															playerWeapon = holdingSpot;
															armorCheck();
															pressDelay = 15;
														}
													}
												}
												else {
													if(player.inventory[i].equals("wand")) {
														g.drawImage(wand_I.guy, slotXs[i], slotYs[i], null);
														
														//equips the helmet and replaces it's slot with what you were originaly wearing
														if(mouseCollided(slotXs[i],slotYs[i],32,32)) {
															if(mousemanager.isLeftPressed() && pressDelay == 0) {
																holdingSpot = "wand";
																lastWorn = playerWeapon;
																player.inventory[i] = playerWeapon;
																playerWeapon = holdingSpot;
																armorCheck();
																pressDelay = 15;
															}
														}
													}
													else {
														if(player.inventory[i].equals("shank")) {
															g.drawImage(shank_I.guy, slotXs[i], slotYs[i], null);
															
															//equips the helmet and replaces it's slot with what you were originaly wearing
															if(mouseCollided(slotXs[i],slotYs[i],32,32)) {
																if(mousemanager.isLeftPressed() && pressDelay == 0) {
																	holdingSpot = "shank";
																	lastWorn = playerWeapon;
																	player.inventory[i] = playerWeapon;
																	playerWeapon = holdingSpot;
																	armorCheck();
																	pressDelay = 15;
																}
															}
														}
														else {
															if(player.inventory[i].equals("staff")) {
																g.drawImage(staff_I.guy, slotXs[i], slotYs[i], null);
																
																//equips the helmet and replaces it's slot with what you were originaly wearing
																if(mouseCollided(slotXs[i],slotYs[i],32,32)) {
																	if(mousemanager.isLeftPressed() && pressDelay == 0) {
																		holdingSpot = "staff";
																		lastWorn = playerWeapon;
																		player.inventory[i] = playerWeapon;
																		playerWeapon = holdingSpot;
																		armorCheck();
																		pressDelay = 15;
																	}
																}
															}
															else {
																if(player.inventory[i].equals(SteelSword.name)) {
																	g.drawImage(steelSword_I.guy, slotXs[i], slotYs[i], null);
																	
																	//equips the helmet and replaces it's slot with what you were originaly wearing
																	if(mouseCollided(slotXs[i],slotYs[i],32,32)) {
																		if(mousemanager.isLeftPressed() && pressDelay == 0) {
																			strBonus += 3;
																			holdingSpot = SteelSword.name;
																			lastWorn = playerWeapon;
																			player.inventory[i] = playerWeapon;
																			playerWeapon = holdingSpot;
																			armorCheck();
																			pressDelay = 15;
																		}
																	}
																}
																else {
																	if(player.inventory[i].equals("steel helm")) {
																		g.drawImage(steelHelm.guy, slotXs[i], slotYs[i], null);
																		
																		//equips the helmet and replaces it's slot with what you were originaly wearing
																		if(mouseCollided(slotXs[i],slotYs[i],32,32)) {
																			if(mousemanager.isLeftPressed() && pressDelay == 0) {
																				holdingSpot = "steel helm";
																				luckBonus += 2;
																				lastWorn = player.helmet;
																				player.inventory[i] = player.helmet;
																				player.helmet = holdingSpot;
																				armorCheck();
																				pressDelay = 15;
																			}
																		}
																	}
																	else {
																		if(player.inventory[i].equals("steel chest")) {
																			g.drawImage(steelChest_I.guy, slotXs[i], slotYs[i], null);
																			
																			//equips the helmet and replaces it's slot with what you were originaly wearing
																			if(mouseCollided(slotXs[i],slotYs[i],32,32)) {
																				g.setFont(new Font("Times New Roman",Font.BOLD,30));
																				if(mousemanager.isLeftPressed() && pressDelay == 0) {
																					holdingSpot = "steel chest";
																					hpBonus += 5;
																					lastWorn = player.chest;
																					player.inventory[i] = player.chest;
																					player.chest = holdingSpot;
																					armorCheck();
																					pressDelay = 15;
																				}
																			}
																		}
																		else {
																			if(player.inventory[i].equals("steel leggings")) {
																				g.drawImage(steelLeggings_I.guy, slotXs[i], slotYs[i], null);
																				
																				//equips the helmet and replaces it's slot with what you were originaly wearing
																				if(mouseCollided(slotXs[i],slotYs[i],32,32)) {
																					if(mousemanager.isLeftPressed() && pressDelay == 0) {
																						holdingSpot = "steel leggings";
																						hpBonus += 2;
																						lastWorn = player.legs;
																						player.inventory[i] = player.legs;
																						player.legs = holdingSpot;
																						armorCheck();
																						pressDelay = 15;
																					}
																				}
																			}
																			else {
																				if(player.inventory[i].equals("health potion")) {
																					g.drawImage(healthPotion.guy, slotXs[i], slotYs[i], null);
																					
																					//equips the helmet and replaces it's slot with what you were originaly wearing
																					if(mouseCollided(slotXs[i],slotYs[i],32,32)) {
																						if(mousemanager.isLeftPressed() && pressDelay == 0) {
																							player.hp += 30;
																							audioManager.playSound(potionSound, false);
																							player.inventory[i] = "";
																							armorCheck();
																							pressDelay = 15;
																						}
																					}
																				}
																				else {
																					if(player.inventory[i].equals("mana potion")) {
																						g.drawImage(manaPotion.guy, slotXs[i], slotYs[i], null);
																						
																						//equips the helmet and replaces it's slot with what you were originaly wearing
																						if(mouseCollided(slotXs[i],slotYs[i],32,32)) {
																							if(mousemanager.isLeftPressed() && pressDelay == 0) {
																								player.mana += 35;
																								audioManager.playSound(potionSound, false);
																								player.inventory[i] = "";
																								armorCheck();
																								pressDelay = 15;
																							}
																						}
																					}
																					else {
																						if(player.inventory[i].equals("thief hood t2")) {
																							g.drawImage(tier2ThiefHoodImage.guy, slotXs[i], slotYs[i], null);
																							
																							//equips the helmet and replaces it's slot with what you were originaly wearing
																							if(mouseCollided(slotXs[i],slotYs[i],32,32)) {
																								if(mousemanager.isLeftPressed() && pressDelay == 0) {
																									holdingSpot = "thief hood t2";
																									luckBonus += 3;
																									lastWorn = player.helmet;
																									player.inventory[i] = player.helmet;
																									player.helmet = holdingSpot;
																									armorCheck();
																									pressDelay = 15;
																								}
																							}
																						}
																						else {
																							if(player.inventory[i].equals("thief robe t2")) {
																								g.drawImage(tier2ThiefRobeImage_I.guy, slotXs[i], slotYs[i], null);
																								
																								//equips the helmet and replaces it's slot with what you were originaly wearing
																								if(mouseCollided(slotXs[i],slotYs[i],32,32)) {
																									if(mousemanager.isLeftPressed() && pressDelay == 0) {
																										agiBonus += 3;
																										holdingSpot = "thief robe t2";
																										lastWorn = player.chest;
																										player.inventory[i] = player.chest;
																										player.chest = holdingSpot;
																										armorCheck();
																										pressDelay = 15;
																									}
																								}
																							}
																							else {
																								if(player.inventory[i].equals("thief pants t2")) {
																									g.drawImage(tier2ThiefPantsImage_I.guy, slotXs[i], slotYs[i], null);
																									
																									//equips the helmet and replaces it's slot with what you were originaly wearing
																									if(mouseCollided(slotXs[i],slotYs[i],32,32)) {
																										if(mousemanager.isLeftPressed() && pressDelay == 0) {
																											sneakBonus += 3;
																											holdingSpot = "thief pants t2";
																											lastWorn = player.legs;
																											player.inventory[i] = player.legs;
																											player.legs = holdingSpot;
																											armorCheck();
																											pressDelay = 15;
																										}
																									}
																								}
																								else {
																									switch(load) {
																									case 1:
																										player.inventory[i] = "";
																										
																									case 0:
																										player.inventory[i] = player.inventory[i];
																									}
																									
																									for(int c = 0; c < boards.length; c++) {
																										for(int r = 0; r < boards[c].posters.length; r++) {
																											if(player.inventory[i].equals(boards[c].posters[r].enemy.name)) {
																												g.drawImage(paper.guy, slotXs[i], slotYs[i], null);
																												
																												//equips the helmet and replaces it's slot with what you were originaly wearing
																												if(mouseCollided(slotXs[i],slotYs[i],32,32)) {
																													g.setFont(new Font("Times New Roman",Font.BOLD,30));
																													
																													g.setColor(Color.gray.darker());
																													g.fillRect(mousemanager.mousex - 300, mousemanager.mousey + 50, 300, 100);
																													
																													g.setColor(Color.black);
																													g.drawRect(mousemanager.mousex - 300, mousemanager.mousey + 50, 300, 100);
																													
																													g.setColor(Color.black);
																													g.drawString("wanted poster", mousemanager.mousex - 220, mousemanager.mousey + 110);
																													
																													g.setFont(new Font("Times New Roman",Font.BOLD,16));
																													g.drawString("click to read", mousemanager.mousex - 200, mousemanager.mousey + 130);
																													
																													if(mousemanager.isLeftPressed() && pressDelay == 0) {
																														reading = player.inventory[i];
																														audioManager.playSound(paperSound,false);
																														pressDelay = 15;
																													}
																												}
																											}
																										}
																									}
																								}
																							}
																						}
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					} //4
				}
			}
		}
		
	}
	
	public void castSpell(Enemy target) {
		enemyTarget = target;
	}
	
	public Enemy targetSelection() {
		for(int i = 0; i < enemies.length; i++) {
			if(location.equals(enemies[i].location)) {
				if(mouseCollided(enemies[i].x,enemies[i].y,enemies[i].w,enemies[i].h)) {
					if(mousemanager.isLeftPressed() && pressDelay  == 0) {
						return enemies[i];
					}
					else {
						return null;
					}
				}
				else {
					return null;
				}
			}
			else {
				return null;
			}
		}
		return null;
	}
	
	//a function so i dont have to rewrite the collision code for each button
	public boolean mouseCollided(int obj_x,int obj_y,int obj_width,int obj_height) {
		if(mousemanager.mousex > obj_x && mousemanager.mousex < obj_x + obj_width && mousemanager.mousey > obj_y && mousemanager.mousey < obj_y + obj_height) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void steal(ShopItem itemObject,String itemName) {
		if(itemObject == lesserHeal) {
			player.addToSpells("lesser heal");
			inventoryVisible = false;
			caughtCalculator();
			caughtChecker = 25;
			pressDelay = 25;
			itemObject.restockTimer = 15000;
			
			lastItem = itemObject;
		}
		else {
			player.addToInv(itemName,inventoryVisible);
			inventoryVisible = true;
			caughtCalculator();
			caughtChecker = 25;
			pressDelay = 25;
			itemObject.restockTimer = 15000;
			
			lastItem = itemObject;
		}
	}
	
	public void buy(ShopItem itemObject,String itemName) {
		if(player.gold >= itemObject.price) {
			player.gold -= itemObject.price;
			
			if(itemObject == lesserHeal) {
				player.addToSpells(itemName);
				inventoryVisible = false;
				pressDelay = 25;
				itemObject.restockTimer = 15000;
			}
			else {
				player.addToInv(itemName,inventoryVisible);
				inventoryVisible = true;
				pressDelay = 25;
				itemObject.restockTimer = 15000;
			}
		}
		else {
			System.out.println("Insufficient funds");
			itemObject.restockTimer = 0;
		}
	}
	
	public void armorCheck() {
		if(player.helmet.equals("leather helmet")) {
			helm = 5;
		}
		else {
			if(player.helmet.equals("mage hood")) {
				helm = 3;
			}
			else {
				if(player.helmet.equals("thief hood")) {
					helm = 3;
				}
				else {
					if(player.helmet.equals("steel helm")) {
						helm = 7;
					}
					else {
						if(player.helmet.equals("thief hood t2")) {
							helm = 6;
						}
					}
				}
			}
		}
		
		if(player.chest.equals("leather chest")) {
			ch = 7;
		}
		else {
			if(player.chest.equals("mage robe")) {
				ch = 4;
			}
			else {
				if(player.chest.equals("thief robe")) {
					ch = 4;
				}
				else {
					if(player.chest.equals("steel chest")) {
						ch = 10;
					}
					else {
						if(player.chest.equals("thief robe t2")) {
							ch = 8;
						}
					}
				}
			}
		}
		
		if(player.legs.equals("steel leggings")) {
			pa = 6;
		}
		else {
			if(player.legs.equals("thief pants t2")) {
				pa = 5;
			}
			else {
				pa = 2;
			}
		}
		
		
		if(lastWorn.equals("thief pants t2")) {
			if(sneakBonus > 0) {
				sneakBonus -= 3;
			}
		}
		
		if(lastWorn.equals("thief hood t2")) {
			if(luckBonus > 0) {
				luckBonus -= 3;
			}
		}
		
		if(lastWorn.equals("thief robe t2")) {
			if(agiBonus > 0) {
				agiBonus -= 3;
			}
		}
		
		if(lastWorn.equals("steel helm")) {
			if(luckBonus > 0) {
				luckBonus -= 2;
			}
		}
		
		if(lastWorn.equals("steel chest")) {
			if(hpBonus > 0) {
				hpBonus -= 5;
			}
		}
		
		if(lastWorn.equals("steel leggings")) {
			if(hpBonus > 0) {
				hpBonus -= 2;
			}
		}
		
		if(lastWorn.equals("steel sword")) {
			if(strBonus > 0) {
				strBonus -= 3;
			}
		}
	}
	
	public void init() {
		running = true;
		state = "main menu";
		playerMenuVisible = false;
		console = new Console(32*5,32*19,24); 
		goldDisplay = new Console((32*34) + 5,(32*18) - 10,20);
		fineDisplay = new Console((32*34) + 5,(32*17),20);
		newGameButton = new Button(false,WIDTH/2-150,HEIGHT/2-200,300,100); //false is if it's lit up or not
		Name = new Button(false, 690, 145,127,25);
		raceLeft = new Button(false,535,372,30,23);
		raceRight = new Button(false,260,372,30,23);
		hpLeft = new Button(false,120,424,30,23);
		hpRight = new Button(false,180,424,30,23);
		strLeft = new Button(false,120,477,30,23);
		strRight = new Button(false,180,477,30,30);
		luckLeft = new Button(false,160,498,23,23);
		luckRight = new Button(false,220,498,23,23);
		IntLeft = new Button(false,120,450,30,23);
		IntRight = new Button(false,180,450,30,23);
		sneakLeft = new Button(false,170,466,30,23);
		sneakRight = new Button(false,230,466,30,23);
		speechLeft = new Button(false,190,499,30,23);
		speechRight = new Button(false,250,499,30,23);
		classLeft = new Button(false,560,399,30,23);
		classRight = new Button(false,595,399,30,23);
		doneButton = new Button(false,675,245,110,31);
		playerMenuLeft = new Button(false,32*20 + 10,32*6 + 10,50,50);
		playerMenuRight = new Button(false,32*20 + 70,32*6 + 10,50,50);
		okButton = new Button(false,32*5,32*20,50,50);
		south = new Button(false,32*35,32*21 + 4,50,50);
		north = new Button(false,32*35,32*20 - 73,50,50);
		west = new Button(false,32*34-23,north.y+55,50,50);
		east = new Button(false,32*36+20,north.y+55,50,50);
		Diary = new Button(false,32*5,32*20,50,50);
		Inventory = new Button(false,32*7,32*20,50,50);
		stats = new Button(false,32*9,32*20,50,50);
		saveButton = new Button(false,32*11,32*20,50,50);
		loadButton = new Button(false,32*13,32*20,50,50);
		noButton = new Button(false,32*7,32*20,50,50);
		boardBack = new Button(false,0,0,50,50);
		
		
		creatingCharacter = false;
		wanted = false;
		turn = "you";
		location = "town";
		
		oldXp = player.xp;
		
		guard2Respawn = -1;
		
		odioTimer = -3;
		
		difference = 0;
		
		chopped = -3;
		
		surroundingLocations = new int[4];
		
		clerkx = 32*16;
		clerky = 32*2;
		clerkw = 32;
		clerkh = 64;
		
		predictor = new Predictor();
		
		predictor.randomize(surroundingLocations);
		
		//town 1
		Armorer = new Building(32,32*4,"Armorer","town","armorer");
		Castle = new Building(32*13,32*4,"Castle","town","castle");
		blackSmith = new Building(32*26,32*4,"Blacksmith","town","blacksmith");
		
		//town 2
		blackSmith2 = new Building(32*3,-32,"Blacksmith","town2","blacksmith2");
		Armorer2 = new Building(32*3,32*5,"Armorer","town2","armorer2");
		magicShop = new Building(32*23,-32,"Magic Shop","town2","magic shop");
		Castle2 = new Building(32*23,32*5,"Castle","town2","castle2");
		
		buildings = new Building[] {
				Armorer,Castle,blackSmith,blackSmith2,Armorer2,magicShop,Castle2,dungeon1
		};
		
		
		//enemies
		chicken = new Enemy(32*17,32*15,10,5,32,32,"chicken",null,"woods",null,null,null);
		
		//people
		armorerClerk = new Person(clerkx,clerky,clerkw,clerkh,null);
		jim = new Person(32*6,32*10,32,64,"town");
		
		//quests
		weirdTree = new Quest("Weird Tree","Find and chop down weird tree",10,10);
		
		questJournal = new Journal();
		
		posterBack= new Button(false,questJournal.x-50,questJournal.y+30,50,50);
		
		//dialogues
		caughtDialogue = new Dialogue(4);	//4 is how many "slides" are in the dialogue, or how many times
		armorerDialogue = new Dialogue(2);	//you would have to click to see it all
		
		if(weirdTree.finished) {
			citizenDialogue = new Dialogue(1);
		}
		else {
			citizenDialogue = new Dialogue(5);
		}
		
		//might change where and how dialogue slides are added for more variety in the future
		//for instance i could use arrays of possible sentances and have a random one chosen
		if(weirdTree.finished) {
			citizenDialogue.addSlide(">> Citizen: Hey.");
		}
		else {
			citizenDialogue.addSlide(">> Citizen: Hey, can you do me a favor?");
			citizenDialogue.addSlide(">> Citizen: Can you cut down weird tree for me?");
			citizenDialogue.addSlide(">> Citizen: Ill pay you generously for your service.");
			citizenDialogue.addSlide(">> Citizen: Not cool dog.");
			citizenDialogue.addSlide(">> Citizen: Thanks odio. Here's your reward.");
		}
		
		
		caughtDialogue.addSlide(">> Guard: Stop!");
		caughtDialogue.addSlide(">> Guard: Pay your fine or DIE odio eater!");
		caughtDialogue.addSlide(">> Guard: THEN DIE!");
		caughtDialogue.addSlide(">> Guard: I hope you learn from your mistakes.");
		
		armorerDialogue.addSlide(">> Merchant: Hey there!");
		armorerDialogue.addSlide(">> Merchant: Why not take a look at my wares?");
		
		
		guard = new Enemy(32*4,32*10,1000,20,32,64,"guard",null,"town",null,null,null);
		guard2 = new Enemy(32*18,32*7,1000,20,32,64,"guard",null,"town2",null,null,null);
		
		posterEnemies = new Enemy[] {
				board.posters[0].enemy,board.posters[1].enemy,board.posters[2].enemy
		};
		
		enemies = new Enemy[] {
				chicken,guard,guard2,board.posters[0].enemy,board.posters[1].enemy,board.posters[2].enemy
		};
		
		guards = new Enemy[] {
				guard,guard2
		};
		
		//inventory slot positions
		slot1x = 32*34 + 20;
		slot1y = 12 + 74;
		
		slot2x = slot1x + 42;	//42 is the image's width + 10 so there is some space between them
		slot2y = slot1y;
		
		slot3x = slot2x + 42;
		slot3y = slot2y;
		
		slot4x = slot3x + 42;
		slot4y = slot3y;
		
		slot5x = slot1x;
		slot5y = slot1y + 42;
		
		slot6x = slot2x;
		slot6y = slot5y;
		
		slot7x = slot3x;
		slot7y = slot6y;
		
		slot8x = slot4x;
		slot8y = slot7y;
		
		slot9x = slot5x;
		slot9y = slot5y + 42;
		
		slot10x = slot6x;
		slot10y = slot9y;
		
		slot11x = slot7x;
		slot11y = slot10y;
		
		slot12x = slot8x;
		slot12y = slot11y;
		
		
		//player menu state and armor position
		playerMenuState = "yeet lol";
		
		armorx = 32*10;
		armory = 32*4;
		armorw = 32;
		armorh = 32;
		
		caught = 100;	//its above 10 so it doesn't trigger the code for getting caught if you haven't done anything
		caughtChecker = -3;	//i set it to -3 because it sets off the code for getting caught when it = 0
		
		random = new Random();
		
		location = "town";	//placeholder, eventually spawning location will be in the character creation menu
		
		//also got this cursor stuff from youtube
		// Transparent 16 x 16 pixel cursor image.
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

		// Create a new blank cursor.
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
		    cursorImg, new Point(0, 0), "blank cursor");

		// Set the blank cursor to the JFrame.
		display.getJFrame().getContentPane().setCursor(blankCursor);
		
		skillPoints = 3;
		strBonus = 0;
		intBonus = 0;
		sneakBonus = 0;
		agiBonus = 0;
		luckBonus = 0;
		hpBonus = 0;
		
		//adds the keymanager and mousemanager to the window so it can be used in-game
		display.getJFrame().addKeyListener(keymanager);
		display.getJFrame().addMouseListener(mousemanager);
		display.getJFrame().addMouseMotionListener(mousemanager);
		display.getCanvas().addMouseListener(mousemanager);
		display.getCanvas().addMouseMotionListener(mousemanager);
		
		for(int i = 0; i < enemies.length; i++) {
			enemies[i].damage = -1;
		}
	}
	
	public void caughtCalculator() {
		luckDegrader = (Math.pow(Math.E, oldProb));
		
		caught = (((player.sneak+sneakBonus)+(player.luck/luckDegrader))/100);
		oldProb = caught;
		
		prob = random.nextDouble();

		
		//tester.Test(player.sneak,sneakBonus,player.luck,luckBonus);
		//caught = (int) (((double)(random.nextInt(((player.sneak+sneakBonus)+(player.luck+luckBonus))-1)+1)/
		//		 ((player.sneak+sneakBonus)+(player.luck+luckBonus)))*((player.sneak+sneakBonus)+(player.luck+luckBonus)));
	}
	
	public void render() {
		FPSCounter.StartCounter();
		
		BufferStrategy bs = display.getCanvas().getBufferStrategy();
		//checks if there is a buffer strategy
		if(bs == null) {
			display.getCanvas().createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();
		
		update(g);
		update2(g);
		update3(g);
		
		bs.show();
		g.dispose();
		
		FPSCounter.StopAndPost();
	}
	
	public void createName() {
		if(letterTimer > 0) {
			letterTimer--;
			System.out.println(" + letterTimer");
		}
	}
	
	public void update3(Graphics graphics) {
		if(musicTimer > 0) {
			musicTimer--;
		}
		
		if(prob > caught) {
			caughtChecker = -3;
			player.fine += lastItem.price;
			wanted = true;
			prob = caught;
		}
		
		if(player.x == 32*16) {
			East = false;
			West = false;
		}
		
		if(player.y == 32*10) {
			North = false;
			South = false;
		}
		
		if(South) {
			player.y++;	//change this by some kind of formula with agi, giving the character speed
			//the speed HAS TO BE A MULTIPLE OF THE PLAYER Y
			if(player.y >= HEIGHT - 150) {
				player.y = 0;
				
				for(int i = 0; i < locations.length; i++) {
					if(location.equals(locations[i].name)) {
						if(locations[i].destinations[3] != null) {
							for(int r = 0; r < guards.length; r++) {
								if(location.equals(guards[r].location) && wanted) {
									wanted = false;
									guards[r].hostile = true;
									turn = guards[r].name;
								}
							}
							location = locations[i].destinations[3];
							dialogueSlide = 0;
							break;
						}
					}
				}
			}
		}
		else {
			if(North) {
				player.y--;
				
				if(player.y <= 0) {
					player.y = (HEIGHT-150)-64;
					
					for(int i = 0; i < locations.length; i++) {
						if(location.equals(locations[i].name)) {
							if(locations[i].destinations[1] != null) {
								for(int r = 0; r < guards.length; r++) {
									if(location.equals(guards[r].location) && wanted) {
										wanted = false;
										guards[r].hostile = true;
										turn = guards[r].name;
									}
								}
								location = locations[i].destinations[1];
								dialogueSlide = 0;
								break;
							}
						}
					}
				}
			}
			else {
				if(East) {
					player.x++;
					
					if(player.x >= WIDTH - 150) {
						player.x = 0;
						
						for(int i = 0; i < locations.length; i++) {
							if(location.equals(locations[i].name)) {
								if(locations[i].destinations[2] != null) {
									for(int r = 0; r < guards.length; r++) {
										if(location.equals(guards[r].location) && wanted) {
											wanted = false;
											guards[r].hostile = true;
											turn = guards[r].name;
										}
									}
									location = locations[i].destinations[2];
									dialogueSlide = 0;
									break;
								}
							}
						}
					}
				}
				else {
					if(West) {
						player.x--;
						
						if(player.x <= 0) {
							player.x = (WIDTH-150)-32;
							
							for(int i = 0; i < locations.length; i++) {
								if(location.equals(locations[i].name)) {
									if(locations[i].destinations[0] != null) {
										for(int r = 0; r < guards.length; r++) {
											if(location.equals(guards[r].location) && wanted) {
												wanted = false;
												guards[r].hostile = true;
												turn = guards[r].name;
											}
										}
										location = locations[i].destinations[0];
										dialogueSlide = 0;
										break;
									}
								}
							}
						}
					}
				}
			}
		}
		
		threshHold = (tester.average + (int) (player.luck/3));
		
		player.maxMana = player.Int + 45;
		
		if(player.hp > player.maxHP) {
			player.hp = player.maxHP;
		}
		
		if(player.mana > player.maxMana) {
			player.mana = player.maxMana;
		}
		
		
		if(state.equals("game")) {
			if(player.maxHP ==  player.oldHp) {
				hpRight.x = 170;
				hpLeft.x = -50;	//moves the left arrow so both aren't clicked at the same time
			}
			else {
				hpLeft.x = 170;
				hpRight.x = 230;
			}
			
			//only shows the increasing arrow if your speech is 10
			if(player.agi == player.oldAgi) {
				speechRight.x = 130;
				speechLeft.x = -50;	//moves the left arrow so both aren't clicked at the same time
			}
			else {
				speechLeft.x = 130;
				speechRight.x = 190;
			}
			
			
			//only shows the increasing arrow if your luck is 10
			if(player.luck ==  player.oldLuck) {
				luckRight.x = 150;
				luckLeft.x = -50;	//moves the left arrow so both aren't clicked at the same time
			}
			else {
				luckLeft.x = 150;
				luckRight.x = 210;
			}
			
			if(player.str == player.oldStr) {
				strRight.x = 120;
				strLeft.x = -50;	//moves the left arrow so both aren't clicked at the same time
			}
			else {
				strLeft.x = 120;
				strRight.x = 180;
			}

			//only shows the increasing arrow if your sneak is 10
			if(player.sneak == player.oldSneak) {
				sneakRight.x = 170;
				sneakLeft.x = -50;	//moves the left arrow so both aren't clicked at the same time
			}
			else {
				sneakLeft.x = 170;
				sneakRight.x = 230;
			}
			
			//only shows the increasing arrow if your Int is 10
			if(player.Int == player.oldInt) {
				IntRight.x = 120;
				IntLeft.x = -50;	//moves the left arrow so both aren't clicked at the same time
			}
			else {
				IntLeft.x = 120;
				IntRight.x = 180;
			}
		}
		
		
		if(player.xp > oldXp || player.level > oldLevel) {
			if(difference == 0) {
				odioTimer = 500;
				difference = 1;
			}
		}
		
		if(odioTimer > 0) {
			if(player.level > oldLevel) {
				g.setFont(new Font("Times New Roman",Font.BOLD,16));
				g.setColor(Color.BLACK);
				g.drawString("Level Up!" , player.x+35, player.y-10);
			}
			else {
				g.setFont(new Font("Times New Roman",Font.BOLD,16));
				g.setColor(Color.BLACK);
				g.drawString("+" + (player.xp-oldXp) + " " + "EXP" , player.x+35, player.y-10);
			}
		}
		
		
		if(odioTimer == 0 && difference == 1) {
			oldXp = player.xp;
			oldLevel = player.level;
			odioTimer = -3;
			difference = 0;
		}
		
		if(odioTimer > 0) {
			odioTimer--;
		}
		
		if(chicken.hp < 1) {
			chicken.x = 5000;
		}
		else {
			if(chicken.hp >= 1 && woodsMonsterChance > 4) {
				chicken.x = 32*17;
			}
		}
		
		for(int i = 0; i < boards.length; i++) {
			for(int c = 0; c < boards[i].posters.length; c++) {
				if(boards[i].posters[c].enemy.hp < 1) {
					player.xp += boards[i].posters[c].xp;
					player.gold += boards[i].posters[c].gold;
					
					
					for(int r = 0; r < player.inventory.length; r++) {
						try {
							if(player.inventory[r].equals(boards[i].posters[c].enemy.name)) {
								boards[i].posters[c].createEnemy(player.level);
								
								posterEnemies = new Enemy[] {
										board.posters[0].enemy,board.posters[1].enemy,board.posters[2].enemy
								};
								
								enemies = new Enemy[] {
										chicken,guard,guard2,board.posters[0].enemy,board.posters[1].enemy,board.posters[2].enemy
								};
								
								boards[i].posters[c].taken = false;
								player.inventory[r] = "";
								break;
							}
							else {
								if(r == player.inventory.length) {
									if(player.inventory[r].equals("") || player.inventory[r] == null) {
										break;
									}
								}
							}
						}
						catch(Exception e) {
							break;
						}
					}
				}
			}
		}
		
		for(int r = 0; r < boards.length; r++) {
			if(boards[r].posters[0].enemy.location.equals(boards[r].posters[1].enemy.location)) {
				int thing = random.nextInt(boards[r].posters[0].locations.length);
				boards[r].posters[0].enemy.location = boards[r].posters[0].locations[thing];
			}
			else {
				if(boards[r].posters[0].enemy.location.equals(boards[r].posters[2].enemy.location)) {
					int thing = random.nextInt(boards[r].posters[0].locations.length);
					boards[r].posters[0].enemy.location = boards[r].posters[0].locations[thing];
				}
				else {
					if(boards[r].posters[1].enemy.location.equals(boards[r].posters[2].enemy.location)) {
						int thing = random.nextInt(boards[r].posters[1].locations.length);
						boards[r].posters[1].enemy.location = boards[r].posters[1].locations[thing];
					}
				}
			}
		}
		
		for(int i = 0; i < boards.length; i++) {
			if(location.equals(boards[i].location) && state.equals("game")) {
				if(mouseCollided(boards[i].x,boards[i].y,boards[i].w,boards[i].h)) {
					if(boards[i].x > WIDTH/2) {
						boards[i].showDeskAlt(g, mousemanager);
					}
					else {
						boards[i].showDesc(g, mousemanager);
					}
					
					if(mousemanager.isLeftPressed() && pressDelay == 0) {
						location = "board1";
						pressDelay = 15;
					}
				}
			}
		}

		
		for(int i = 0; i < boards.length; i++) {
			for(int c = 0; c < boards[i].posters.length; c++) {
				if(location.equals(boards[i].posters[c].enemy.location) && !boards[i].posters[c].enemy.hostile) {
					if(boards[i].posters[c].taken) {
						dialogueSlide = -3;
						console.showText(">> You see " + boards[i].posters[c].enemy.name, g);
					}
				}
			}
		}
		
		
		for(int i = 0; i < guards.length; i++) {
			if(location.equals(guards[i].location) && player.fine > 0 && !guards[i].hostile) {
				wanted = true;
			}
		}
		
		
		for(int i = 0; i < shops.length; i++) {
			for(int c = 0; c < shops[i].items.length; c++) {
				if(mouseCollided(shops[i].items[c].x,shops[i].items[c].y,shops[i].items[c].w,shops[i].items[c].h)) {
					if(mousemanager.isRightPressed() && pressDelay == 0 && location.equals(shops[i].items[c].location)) {
						if(shops[i].items[c].restockTimer == 0) {
							steal(shops[i].items[c],shops[i].items[c].name);
							shops[i].items[c].restockTimer = 15000;
						}
					}
					else {
						if(mousemanager.isLeftPressed() && pressDelay == 0 && location.equals(shops[i].items[c].location)) {
							if(shops[i].items[c].restockTimer == 0) {
								buy(shops[i].items[c],shops[i].items[c].name);
							}
						}
					}
				}
			}
		}
		
		for(int i = 0; i < shops.length; i++) {
			for(int c = 0; c < shops[i].items.length; c++) {
				if(shops[i].items[c].restockTimer > 0) {
					shops[i].items[c].restockTimer--;
				}
				else {
					if(shops[i].items[c].restockTimer < 0) {
						shops[i].items[c].restockTimer = 0;
					}
				}
			}
		}
		
		if(state == "game" && talkingToCitizen == false && talkingToClerk == false && talkingToGuard == false) {
			if(Diary.active) {
				g.drawImage(diaryActive.guy, Diary.x, Diary.y, null);
			}
			else {
				g.drawImage(diary.guy, Diary.x, Diary.y, null);
			}
			
			if(saveButton.active) {
				g.drawImage(saveActive.guy,saveButton.x,saveButton.y,null);
			}
			else {
				g.drawImage(save.guy,saveButton.x,saveButton.y,null);
			}
			
			if(loadButton.active) {
				g.drawImage(loadActive.guy,loadButton.x,loadButton.y,null);
			}
			else {
				g.drawImage(load.guy,loadButton.x,loadButton.y,null);
			}
			
			if(inventoryVisible) {
				if(Inventory.active) {
					g.drawImage(magicActive.guy, Inventory.x, Inventory.y, null);
				}
				else {
					g.drawImage(magic.guy, Inventory.x, Inventory.y, null);
				}
			}
			else {
				if(Inventory.active) {
					g.drawImage(backpackActive.guy, Inventory.x, Inventory.y, null);
				}
				else {
					g.drawImage(backpack.guy, Inventory.x, Inventory.y, null);
				}
			}
			
			if(stats.active) {
				g.drawImage(characterActive.guy, stats.x, stats.y, null);
			}
			else {
				g.drawImage(character.guy, stats.x, stats.y, null);
			}
			
			if(skillPoints > 0) {
				g.drawImage(smallDown.guy, stats.x+10, stats.y-30,null);
			}
		}
		
		if(mouseCollided(Inventory.x,Inventory.y,Inventory.w,Inventory.h)) {
			Inventory.active = true;
			
			if(mousemanager.isLeftPressed() && pressDelay == 0 && state == "game" && talkingToCitizen == false && talkingToClerk == false && talkingToGuard == false) {
				if(inventoryVisible) {
					inventoryVisible = false;
					pressDelay = 15;
				}
				else {
					inventoryVisible = true;
					pressDelay = 15;
				}
			}
		}
		else {
			Inventory.active = false;
		}
		
		if(mouseCollided(saveButton.x,saveButton.y,saveButton.w,saveButton.h)) {
			saveButton.active = true;
			
			if(mousemanager.isLeftPressed() && pressDelay == 0 && state == "game" && talkingToCitizen == false && talkingToClerk == false && talkingToGuard == false && !inDungeon) {
				saveGame();
				pressDelay = 15;
			}
		}
		else {
			saveButton.active = false;
		}
		
		if(mouseCollided(loadButton.x,loadButton.y,loadButton.w,loadButton.h)) {
			loadButton.active = true;
			
			if(mousemanager.isLeftPressed() && pressDelay == 0 && state == "game" && talkingToCitizen == false && talkingToClerk == false && talkingToGuard == false) {
				loadGame();
				pressDelay = 15;
			}
		}
		else {
			loadButton.active = false;
		}
		
		if(mouseCollided(Diary.x,Diary.y,Diary.w,Diary.h)) {
			Diary.active = true;
			
			if(mousemanager.isLeftPressed() && pressDelay == 0 && state == "game" && talkingToCitizen == false && talkingToClerk == false && talkingToGuard == false) {
				if(questJournal.visible) {
					questJournal.visible = false;
					pressDelay = 15;
				}
				else {
					questJournal.visible = true;
					pressDelay = 15;
				}
			}
		}
		else {
			Diary.active = false;
		}
		
		if(mouseCollided(stats.x,stats.y,stats.w,stats.h)) {
			stats.active = true;
			
			if(mousemanager.isLeftPressed() && pressDelay == 0) {
				//show stats
			}
		}
		else {
			stats.active = false;
		}
		
		
		if(mouseCollided(noButton.x,noButton.y,noButton.w,noButton.h)) {
			noButton.active = true;
			
			if(mousemanager.isLeftPressed() && pressDelay == 0) {
				if(talkingToCitizen) {
					dialogueSlide = 3;
				}
				
				if(talkingToGuard && wanted) {
					pressDelay = 15;
					wanted = false;
					for(int i = 0; i < guards.length; i++) {
						if(location.equals(guards[i].location)) {
							guards[i].hostile = true;
							turn = guards[i].name;
						}
					}
				}
			}
		}
		else {
			noButton.active = false;
		}
		
		if(dialogueSlide == 15 && weirdTree.finished == false) {
			questJournal.removeQuest(weirdTree);
		}
		
		
		for(int i = 0; i < shops.length; i++) {
			for(int c = 0; c < shops[i].items.length; c++) {
				if(mouseCollided(shops[i].items[c].x,shops[i].items[c].y,shops[i].items[c].w,shops[i].items[c].h)) {
					if(location.equals(shops[i].items[c].location) && shops[i].items[c].restockTimer < 1) {
						shops[i].items[c].showDesc(g, mousemanager);

						g.setFont(new Font("Times New Roman",Font.BOLD,16));
						g.drawString("Left click to buy", mousemanager.mousex + 125, mousemanager.mousey + 130);
						g.drawString("Right click to steal", mousemanager.mousex + 118, mousemanager.mousey + 145);
					}
				}
			}
		}
		
		if(state == "game" && !playerMenuVisible) {
			goldDisplay.showText("Gold: " + player.gold, g);
			fineDisplay.showText("Fine: " + player.fine, g);
		}
		
		//quest journal
		if(questJournal.visible == true) {
			g.drawImage(journal.guy,questJournal.x,questJournal.y,null);
			
			if(questJournal.quests[0] != null) {
				questJournal.slot1.showText("" + questJournal.quests[0].name + ": " + questJournal.quests[0].objective, g);
				questJournal.slot1L2.showText("Exp: " + questJournal.quests[0].xp + " Gold: " + questJournal.quests[0].gold, g);
			}
		}
		
		
		for(int i = 0; i < dungeons.length; i++) {
			for(int c = 0; c < dungeons[i].Rooms.length; c++) {
				if(dungeons[i].Rooms[c] != null) {
					for(int r = 0; r < dungeons[i].Rooms[c].enemyClasses.length; r++) {
						if(dungeons[i].Rooms[c].enemyClasses[r] != null) {
							if(location.equals(dungeons[i].Rooms[c].enemyClasses[r].location) && inDungeon) {
								for(int f = 0; f < dungeons[i].Rooms[c].enemyClasses[r].character.length; f++) {
									if(dungeons[i].Rooms[c].enemyClasses[r].hp > 0) {
										g.drawImage(dungeons[i].Rooms[c].enemyClasses[r].character[f].guy,dungeons[i].Rooms[c].enemyClasses[r].x,dungeons[i].Rooms[c].enemyClasses[r].y,null);
									}
								}
							}
						}
					}
				}
			}
		}
		
		
		for(int i = 0; i < board.posters.length; i++) {
			String[] woodsLocations = new String[] {
					"woods","woods2","woods3","woods4","woods5","woods6","woods7"
			};
			
			String[] difficulties = new String[] {
				"","bubble blowin' double baby","easy","kinda hard but not really","ultra-violent droogy","too hard for you"	
			};
			
			for(int r = 0; r < boards.length; r++) {
				if(reading.equals(boards[r].posters[i].enemy.name)) {
					g.drawImage(wantedposter.guy,questJournal.x-100,questJournal.y,null);
					
					if(!reading.equals("")) {
						if(!posterBack.active) {
							g.drawImage(left.guy,posterBack.x,posterBack.y,null);
						}
						else {
							g.drawImage(leftActive.guy,posterBack.x,posterBack.y,null);
						}
						
						if(mouseCollided(posterBack.x,posterBack.y,posterBack.w,posterBack.h)) {
							posterBack.active = true;
							
							if(mousemanager.isLeftPressed() && pressDelay == 0) {
								reading = "";
								audioManager.playSound(paperSound,false);
							}
						}
						else {
							posterBack.active = false;
						}
					}
					
					for(int c = 0; c < boards[r].posters[i].enemy.character.length; c++) {
						g.drawImage(boards[r].posters[i].enemy.character[c].guy,questJournal.x+27,questJournal.y+190,null);
					}
					g.drawImage(boards[r].posters[i].enemy.helmet.guy,questJournal.x+27,questJournal.y+190,null);
					g.drawImage(boards[r].posters[i].enemy.chest.guy,questJournal.x+27,questJournal.y+190,null);
					g.drawImage(boards[r].posters[i].enemy.legging.guy,questJournal.x+27,questJournal.y+190,null);
					
					g.drawString("Name: " + boards[r].posters[i].enemy.name, questJournal.x-18, questJournal.y+300);
					
					for(int t = 0; t < woodsLocations.length; t++) {
						if(boards[r].posters[i].enemy.location.equals(woodsLocations[t])) {
							g.drawString("Last seen: the woods", questJournal.x-18, questJournal.y+320);
						}
					}
					
					if(boards[r].posters[i].enemy.location.equals("camp")) {
						g.drawString("Last seen: the camp", questJournal.x-18, questJournal.y+320);
					}
					
					for(int p = 0; p < boards[r].posters[i].difficulty; p++) {
						g.drawString("Difficulty: " + difficulties[boards[r].posters[i].difficulty], questJournal.x-18, questJournal.y+340);
					}
					
					g.drawString("Reward: " + boards[r].posters[i].gold + "g", questJournal.x-18, questJournal.y+360);
					g.drawString("Exp: " + boards[r].posters[i].xp, questJournal.x-18, questJournal.y+380);
				}
			}
		}
		
		if(!attackShowing) {
			g.drawImage(cursor.guy, mousemanager.mousex, mousemanager.mousey, null);
		}
		
		if(attackShowing) {
			g.drawImage(attackCursor.guy, mousemanager.mousex, mousemanager.mousey, null);
		}
		
		
		if(enemyName().equals("guard")) {
			attackShowing = false;
		}
		else {
			if(enemyName().equals("")) {
				attackShowing = false;
			}
			else {
				attackShowing = true;
			}
		}
		
		if(inDungeon) {
			if(dungeonEnemyName().equals("guard")) {
				attackShowing = false;
			}
			else {
				if(dungeonEnemyName().equals("")) {
					attackShowing = false;
				}
				else {
					attackShowing = true;
				}
			}
		}
	}
	
	public void rotateImage(ImageManager image,double degrees) {
		BufferedImage newCanvas = new BufferedImage(image.guy.getWidth(null),image.guy.getHeight(null),BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D) newCanvas.getGraphics();
		g2.rotate(Math.toRadians(degrees),image.guy.getWidth(null)/2,image.guy.getHeight(null)/2);
		g2.drawImage(image.guy, 0, 0, null);
		image.guy = newCanvas;
	}
	
	public boolean invEmpty() {
		int numOfFull = 0;
		
		for(int i = 0; i < player.inventory.length; i++) {
			if(player.inventory[i] != null) {
				numOfFull++;
			}
		}
		
		if(numOfFull > 0) {
			return false;
		}
		else {
			return true;
		}
		
	}
	
	public boolean spellEmpty() {
		int numOfFull = 0;
		
		for(int i = 0; i < player.spells.length; i++) {
			if(player.spells[i] != null) {
				numOfFull++;
			}
		}
		
		if(numOfFull > 0) {
			return false;
		}
		else {
			return true;
		}
		
	}
	
	public void update2(Graphics graphics) {
		if(!invEmpty() && !playerMenuVisible) {
			slotCheck(0);
		}
		
		if(!spellEmpty() && !playerMenuVisible) {
			spellCheck();
		}
	}
	
	public void update(Graphics graphics) {
		if(state == "game") {	//graphics that show while you are in the game
			//constantly checks if the player should level up
			if(player.xp >= player.maxXP) {
				if(player.xp > player.maxXP) {
					player.xp = (player.xp - player.maxXP);
				}
				else {
					player.xp = 0;
				}
				
				player.levelUp();
				player.oldHp = player.maxHP;
				player.oldLuck = player.luck;
				player.oldAgi = player.agi;
				player.oldInt = player.Int;
				player.oldSneak = player.sneak;
				player.oldStr = player.str;
				skillPoints += 3;
				player.hp = player.maxHP;
				player.mana = player.maxMana;
			}
			
			if((healthWidth + manaWidth) < (south.x - 10)) {
				healthWidth = (player.maxHP * (player.maxHP / 15));
				manaWidth = (player.maxMana * (player.maxMana / 15));
			}
			else {
				healthWidth -= ((healthWidth+manaWidth)-(south.x-10)-1);
			}
			
			if(player.hp+hpBonus < 0) {
				player.hp = 0;
			}
			
			if(guardRespawn > 0) {
				guardRespawn--;
			}
			
			if(guard.hp < 1 && guard.hp > -99999999 ) {
				guard.hp = -99999999;
				guardRespawn = 12000;
			}
			
			if(guard2.hp < 1 && guard2.hp > -99999999 ) {
				guard2.hp = -99999999;
				guard2Respawn = 12000;
			}
			
			if(guard2Respawn > 0) {
				guard2Respawn--;
			}
			
			if(guardRespawn == 0) {
				guard.hp = guard.maxHP;
				guardRespawn = -1;
			}
			
			if(guard2Respawn == 0) {
				guard2.hp = guard2.maxHP;
				guard2Respawn = -1;
			}
			
			if(location.equals("armorer") || location.equals("blacksmith")) {
				if(mouseCollided(armorerClerk.x,armorerClerk.y,armorerClerk.w,armorerClerk.h)) {
					if(mousemanager.isLeftPressed() && pressDelay == 0) {
						talkingToClerk = true;
						talkingToCitizen = false;
					}
				}
			}
			
			if(mouseCollided(jim.x,jim.y,jim.w,jim.h)) {
				if(mousemanager.isLeftPressed() && pressDelay == 0 && location == jim.location) {
					talkingToCitizen = true;
					dialogueSlide = 0;
				}
			}
			
			for(int i = 0; i < firstDungeon.Rooms.length; i++) {
				if(firstDungeon.Rooms[i] != null) {
					if(firstDungeon.playerX == firstDungeon.Rooms[i].x && firstDungeon.playerY == firstDungeon.Rooms[i].y && inDungeon) {
						location = firstDungeon.Rooms[i].dungeonName;
						player.x = 32*16;
						player.y = 32*10;
						g.drawImage(firstDungeon.Rooms[i].roomVariation,0,0,null);
					}
				}
			}
			
			for(int i = 0; i < firstDungeon.Rooms.length; i++) {
				if(firstDungeon.Rooms[i] != null) {
					if(firstDungeon.inRoom(firstDungeon.Rooms[i].x-1, firstDungeon.Rooms[i].y) != null) {
						if(inDungeon && location.equals(firstDungeon.Rooms[i].dungeonName)) {
							graphics.drawImage(Door.guy,0,32*9,null);
							
							if(mouseCollided(0,32*9,32,32)) {
								if(mousemanager.isLeftPressed() && pressDelay == 0) {
									firstDungeon.playerX--;
									
									for(int r = 0; r < dungeons.length; r++) {
										for(int c = 0; c < dungeons[r].Rooms.length; c++) {
											if(dungeons[r].Rooms[c] != null) {
												for(int f = 0; f < dungeons[r].Rooms[c].enemyClasses.length; f++) {
													if(dungeons[r].Rooms[c].enemyClasses[f] != null) {
														dungeons[r].Rooms[c].enemyClasses[f].damage = -1;
													}
												}
											}
										}
									}
									
									dialogueSlide = 0;
									audioManager.playSound(door, false);
									pressDelay = 20;
								}
							}
						}
					}
					
					if(firstDungeon.inRoom(firstDungeon.Rooms[i].x+1, firstDungeon.Rooms[i].y) != null) {
						if(inDungeon && location.equals(firstDungeon.Rooms[i].dungeonName)) {
							graphics.drawImage(Door.guy,32*33,32*9,null);
							
							if(mouseCollided(32*33,32*9,32,32)) {
								if(mousemanager.isLeftPressed() && pressDelay == 0) {
									firstDungeon.playerX++;
									
									for(int r = 0; r < dungeons.length; r++) {
										for(int c = 0; c < dungeons[r].Rooms.length; c++) {
											if(dungeons[r].Rooms[c] != null) {
												for(int f = 0; f < dungeons[r].Rooms[c].enemyClasses.length; f++) {
													if(dungeons[r].Rooms[c].enemyClasses[f] != null) {
														dungeons[r].Rooms[c].enemyClasses[f].damage = -1;
													}
												}
											}
										}
									}
									
									dialogueSlide = 0;
									audioManager.playSound(door, false);
									pressDelay = 20;
								}
							}
						}
					}
					
					if(firstDungeon.inRoom(firstDungeon.Rooms[i].x, firstDungeon.Rooms[i].y+1) != null) {
						if(inDungeon && location.equals(firstDungeon.Rooms[i].dungeonName)) {
							graphics.drawImage(Door.guy,32*16,0,null);
							
							if(mouseCollided(32*16,0,32,32)) {
								if(mousemanager.isLeftPressed() && pressDelay == 0) {
									firstDungeon.playerY++;
									
									for(int r = 0; r < dungeons.length; r++) {
										for(int c = 0; c < dungeons[r].Rooms.length; c++) {
											if(dungeons[r].Rooms[c] != null) {
												for(int f = 0; f < dungeons[r].Rooms[c].enemyClasses.length; f++) {
													if(dungeons[r].Rooms[c].enemyClasses[f] != null) {
														dungeons[r].Rooms[c].enemyClasses[f].damage = -1;
													}
												}
											}
										}
									}
									
									dialogueSlide = 0;
									audioManager.playSound(door, false);
									pressDelay = 20;
								}
							}
						}
					}
					
					if(firstDungeon.inRoom(firstDungeon.Rooms[i].x, firstDungeon.Rooms[i].y-1) != null) {
						if(inDungeon && location.equals(firstDungeon.Rooms[i].dungeonName)) {
							graphics.drawImage(Door.guy,32*16,32*17,null);
							
							if(mouseCollided(32*16,32*17,32,32)) {
								if(mousemanager.isLeftPressed() && pressDelay == 0) {
									firstDungeon.playerY--;
									
									for(int r = 0; r < dungeons.length; r++) {
										for(int c = 0; c < dungeons[r].Rooms.length; c++) {
											if(dungeons[r].Rooms[c] != null) {
												for(int f = 0; f < dungeons[r].Rooms[c].enemyClasses.length; f++) {
													if(dungeons[r].Rooms[c].enemyClasses[f] != null) {
														dungeons[r].Rooms[c].enemyClasses[f].damage = -1;
													}
												}
											}
										}
									}
									
									dialogueSlide = 0;
									audioManager.playSound(door, false);
									pressDelay = 20;
								}
							}
						}
					}
					
					if(firstDungeon.inRoom(firstDungeon.Rooms[0].x, firstDungeon.Rooms[0].y-1) == null && firstDungeon.inRoom(firstDungeon.Rooms[0].x, firstDungeon.Rooms[0].y+1) == null && firstDungeon.inRoom(firstDungeon.Rooms[0].x-1, firstDungeon.Rooms[0].y) == null && firstDungeon.inRoom(firstDungeon.Rooms[0].x+1, firstDungeon.Rooms[0].y) == null) {
						firstDungeon.Generate();
						
						for(int r = 0; r < dungeons.length; r++) {
							for(int c = 0; c < dungeons[r].Rooms.length; c++) {
								if(dungeons[r].Rooms[c] != null) {
									for(int f = 0; f < dungeons[r].Rooms[c].enemyClasses.length; f++) {
										if(dungeons[r].Rooms[c].enemyClasses[f] != null) {
											dungeons[r].Rooms[c].enemyClasses[f].damage = -1;
										}
									}
								}
							}
						}
					}
				}
			}
			
			if(location.equals("town")) {
				g.drawImage(town.guy,0,0,null);
			}
			else {	//draws the armor store
				if(location.equals("armorer")) {
					g.drawImage(armorer.guy,0,0,null);
					if(armorRestock == 0) {	//this is a timer for how long it disappears
						armorVisible = true;	//while this is true it shows the armor
					}
					
					g.drawImage(armorClerk.guy, armorerClerk.x, armorerClerk.y, null);
				}
				else {
					if(location.equals("woods")) {
						if(chopped == -3) {
							g.drawImage(woods.guy, 0, 0, null);
						}
						else {
							if(chopped > 0) {
								g.drawImage(chop.guy,0,0,null);
							}
							else {
								if(chopped == 0) {
									g.drawImage(woodsNormal.guy, 0, 0, null);
								}
							}
						}
					}
					else {
						if(location.equals("castle") || location.equals("castle2")) {
							g.drawImage(castle.guy, 0, 0, null);
						}
						else {
							if(location.equals("blacksmith") || location.equals("blacksmith2")) {
								g.drawImage(blacksmith.guy, 0, 0, null);
								g.drawImage(armorClerk.guy, armorerClerk.x, armorerClerk.y, null);
							}
							else {
								if(location.equals("town2")) {
									g.drawImage(town2.guy, 0, 0, null);
								}
								else {
									if(location.equals("woods2")) {
										g.drawImage(woods2.guy, 0, 0, null);
									}
									else {
										if(location.equals("woods3")) {
											g.drawImage(woods3.guy, 0, 0, null);
										}
										else {
											if(location.equals("woods4")) {
												g.drawImage(woods4.guy, 0, 0, null);
											}
											else {
												if(location.equals("woods5")) {
													g.drawImage(woods5.guy, 0, 0, null);
												}
												else {
													if(location.equals("woods6")) {
														g.drawImage(woods6.guy, 0, 0, null);
													}
													else {
														if(location.equals("woods7")) {
															g.drawImage(woods7.guy, 0, 0, null);
														}
														else {
															if(location.equals("camp")) {
																g.drawImage(camp.guy, 0, 0, null);
															}
															else {
																if(location.equals("armorer2")) {
																	g.drawImage(armorer.guy,0,0,null);
																	g.drawImage(armorClerk.guy, armorerClerk.x, armorerClerk.y, null);
																}
																else {
																	if(location.equals("magic shop")) {
																		g.drawImage(mShop.guy,0,0,null);
																		g.drawImage(armorClerk.guy, armorerClerk.x, armorerClerk.y, null);
																	}
																	else {
																		if(location.equals("board1")) {
																			g.drawImage(bigBoard.guy,0,0,null);
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			
			if(location.equals(board.location) && state.equals("game")) {
				g.drawImage(noticeBoard.guy,board.x,board.y,null);
			}
			
			for(int i = 0; i < shops.length; i++) {
				for(int c = 0; c < shops[i].items.length; c++) {
					if(location.equals(shops[i].items[c].location) && shops[i].items[c].restockTimer == 0) {
						g.drawImage(shops[i].items[c].invImage, shops[i].items[c].x, shops[i].items[c].y, null);
					}
				}
			}
			
			
			
			//draws the character
			if(player.race.equals("orc")) {
				g.drawImage(orcHead.guy,player.x+xOffset,player.y-yOffset,null);
				g.drawImage(orcTorso.guy,player.x+xOffset,player.y-yOffset, null);
				g.drawImage(orcLegs.guy,player.x+xOffset,player.y-yOffset,null);
			}
			else {
				if(player.race.equals("human")) {
					g.drawImage(humanHead.guy,player.x+xOffset,player.y-yOffset,null);
					g.drawImage(humanTorso.guy,player.x+xOffset,player.y-yOffset, null);
					g.drawImage(humanLegs.guy,player.x+xOffset,player.y-yOffset,null);
				}
				else {
					g.drawImage(skull.guy,player.x+xOffset,player.y-yOffset,null);
					g.drawImage(skeletonTorso.guy,player.x+xOffset,player.y-yOffset, null);
					g.drawImage(humanLegs.guy,player.x+xOffset,player.y-yOffset,null);
				}
			}
			
			
			if(launchingFireball) {
				g.drawImage(fire_ball.guy, fireballX, fireballY, null);
				
				if(fireballX + 32 > enemyTarget.x && fireballX < enemyTarget.x + enemyTarget.w && fireballY + 32 > enemyTarget.y && fireballY < enemyTarget.y + enemyTarget.h) {
					launchingFireball = false;
				}
				else {
					if(fireballX > enemyTarget.x + enemyTarget.w) {
						fireballX--;
					}
					else {
						if(fireballX + 32 < enemyTarget.x) {
							fireballX++;
						}
					}
							
					if(fireballY > enemyTarget.y + enemyTarget.h) {
						fireballY--;
					}
					else {
						if(fireballY + 32 < enemyTarget.y) {
							fireballY++;
						}
					}
					
					if(fireballX+32>=enemyTarget.x&&fireballX<=enemyTarget.x+enemyTarget.w) {
						if(fireballY+32>=enemyTarget.y&&fireballY<=enemyTarget.y+enemyTarget.h) {
							launchingFireball = false;
							enemyTarget.hp -= 20;
							dialogueSlide = -4;
							pressDelay = 40;
							enemyTarget.hostile = true;
							turn = enemyTarget.name;
							launchingFireball = false;	
						}	
					}
				}
			}
			
			if(enemyTarget != null) {
				if(fireballX + 32 > enemyTarget.x && fireballX < enemyTarget.x + enemyTarget.w && fireballY + 32 > enemyTarget.y && fireballY < enemyTarget.y + enemyTarget.h) {
					launchingFireball = false;
					enemyTarget.hp -= 20;
					dialogueSlide = -4;
					pressDelay = 40;
					enemyTarget.hostile = true;
					turn = enemyTarget.name;
					launchingFireball = false;
				}
			}
			
			
			
			if(launchingFireball == false) {
				fireballX = player.x;
				fireballY = player.y;
			}
			
			//draws equipment
			//helmets
			if(player.helmet.equals("leather helmet") || crazyData.helmet == "leather helmet") {
				g.drawImage(leatherHelm.guy, player.x+xOffset, player.y-yOffset, null);
			}
			else {
				if(player.helmet.equals("mage hood")) {
					g.drawImage(hoodHelm.guy, player.x+xOffset, player.y-yOffset, null);
				}
				else {
					if(player.helmet.equals("thief hood")) {
						g.drawImage(thiefHelm.guy, player.x+xOffset, player.y-yOffset, null);
					}
					else {
						if(player.helmet.equals("steel helm")) {
							g.drawImage(steelHelm.guy, player.x+xOffset, player.y-yOffset, null);
						}
						else {
							if(player.helmet.equals("thief hood t2")) {
								g.drawImage(tier2ThiefHoodImage.guy, player.x+xOffset, player.y-yOffset, null);
							}
						}
					}
				}
			}
			
			//chest
			if(player.chest.equals("leather chest")) {
				g.drawImage(leatherChest.guy, player.x+xOffset, player.y-yOffset, null);
			}
			else {
				if(player.chest.equals("mage robe")) {
					g.drawImage(robeChest.guy, player.x+xOffset, player.y-yOffset, null);
				}
				else {
					if(player.chest.equals("thief robe")) {
						g.drawImage(thiefChest.guy, player.x+xOffset, player.y-yOffset, null);
					}
					else {
						if(player.chest.equals("steel chest")) {
							g.drawImage(steelChest.guy, player.x+xOffset, player.y-yOffset, null);
						}
						else {
							if(player.chest.equals("thief robe t2")) {
								g.drawImage(tier2ThiefRobeImage.guy, player.x+xOffset, player.y-yOffset, null);
							}
						}
					}
				}
			}
			
			//legs
			if(player.legs.equals("steel leggings")) {
				g.drawImage(steelLeggings.guy, player.x+xOffset, player.y-yOffset, null);
			}
			else {
				if(player.legs.equals("thief pants t2")) {
					g.drawImage(tier2ThiefPantsImage.guy, player.x+xOffset, player.y-yOffset, null);
				}
			}
			
			//weapon
			if(playerWeapon.equals(woodSword.name)) {
				g.drawImage(woodenSword.guy, player.x+xOffset, player.y-yOffset, null);
				manaBonus = 0;
			}
			else {
				if(playerWeapon.equals(SteelSword.name)) {
					g.drawImage(steelSword.guy, player.x+xOffset, player.y-yOffset, null);
					manaBonus = 0;
				}
				else {
					if(playerWeapon.equals("wand")) {
						g.drawImage(wand.guy, player.x+xOffset, player.y-yOffset, null);
						manaBonus = 10;
					}
					else {
						if(playerWeapon.equals("staff")) {
							g.drawImage(staff.guy, player.x+xOffset, player.y-yOffset, null);
							manaBonus = 25;
						}
						else {
							if(playerWeapon.equals("shank")) {
								g.drawImage(shank.guy, player.x+xOffset, player.y-yOffset, null);
								manaBonus = 0;
							}
							else {
								if(playerWeapon.equals("dagger")) {
									g.drawImage(dagger.guy, player.x+xOffset, player.y-yOffset, null);
									manaBonus = 0;
								}
							}
						}
					}
				}
			}
			
			
			if(location.equals("board1")) {
				g.drawImage(bigBoard.guy, 0, 0, null);
				
				int x1 = 100, y1 = 100, x2 = 400, y2 = 300, x3 = 700, y3 = 200;
				
				int[] xs = new int[] {
						x1,x2,x3
				};
				
				int[] ys = new int[] {
						y1,y2,y3
				};
				
				Image[] images = new Image[] {
						poster1.guy,poster2.guy,poster3.guy
				};
				
				for(int i = 0; i < board.posters.length; i++) {
					if(!board.posters[i].taken) {
						g.drawImage(images[i], xs[i], ys[i], null);
					}
				}
				
				if(mouseCollided(boardBack.x,boardBack.y,boardBack.w,boardBack.h)) {
					boardBack.active = true;
					
					if(mousemanager.isLeftPressed() && pressDelay == 0) {
						location = "town";
					}
				}
				else {
					boardBack.active = false;
				}
				
				if(!boardBack.active) {
					g.drawImage(left.guy,boardBack.x,boardBack.y,null);
				}
				else {
					g.drawImage(leftActive.guy,boardBack.x,boardBack.y,null);
				}
			}
			
			
			//draws the in-game UI
			g.setColor(Color.gray.darker());
			g.fillRect(1085, 0, WIDTH - 1080, HEIGHT);
			
			g.setColor(Color.black);
			g.drawRect(1085, 0, WIDTH - 1080, HEIGHT);
			
			g.setColor(Color.gray.darker());
			g.fillRect(2, HEIGHT - 149, WIDTH - 4, 148);
			
			if(inventoryVisible) {
				g.drawImage(backpack.guy, 1085, 0, null);
			}
			else {
				g.drawImage(magic.guy, 1085, 0, null);
			}
			
			g.setColor(Color.black);
			
			//shows the head in the corner (in game) while dialogue is closed
			if(talkingToClerk == false && talkingToGuard == false && talkingToCitizen == false) {
				if(player.race.equals("orc")) {
					g.drawImage(toolbar_orc.guy, 2, HEIGHT - 149, null);
				}
				else {
					if(player.race.equals("human")) {
						g.drawImage(toolbar_human.guy, 2, HEIGHT - 149, null);
					}
					else {
						g.drawImage(toolbar_skull.guy, 2, HEIGHT - 149, null);
					}
				}
				
				if(player.helmet.equals("leather helmet")) {
					g.drawImage(toolbar_leather.guy, 2, HEIGHT - 149, null);
				}
				else {
					if(player.helmet.equals("thief hood")) {
						g.drawImage(toolbar_thief.guy, 2, HEIGHT - 149, null);
					}
					else {
						if(player.helmet.equals("mage hood")) {
							g.drawImage(toolbar_hood.guy, 2, HEIGHT - 149, null);
						}
						else {
							if(player.helmet.equals("steel helm")) {
								g.drawImage(toolbar_steel.guy, 2, HEIGHT - 149, null);
							}
							else {
								if(player.helmet.equals("thief hood t2")) {
									g.drawImage(bigTier2ThiefHood.guy, 2, HEIGHT - 149, null);
								}
							}
						}
					}
				}
			}
			
			if(talkingToGuard) {
				for(int i = 0; i < guards.length; i++) {
					if(wanted && location.equals(guards[i].location) && guards[i].hp > 0) {
						g.drawImage(toolbar_human.guy, 2, HEIGHT - 149, null);
						g.drawImage(toolbar_steel.guy, 2, HEIGHT - 149, null);
					}
				}
			}
			
			
			if(dialogueSlide < 5) {
				if(location.equals("armorer") || location.equals("town") || location.equals("blacksmith")) {
					if(talkingToCitizen) {
						g.drawImage(toolbar_human.guy, 2, HEIGHT - 149, null);
						g.drawImage(toolbar_leather.guy, 2, HEIGHT - 149, null);
					}
				}
			}
			
			if(dialogueSlide == 0 || dialogueSlide == 1) {
				if(talkingToClerk) {
					if(location.equals("armorer") || location.equals("blacksmith")) {
						g.drawImage(toolbar_human.guy, 2, HEIGHT - 149, null);
						g.drawImage(toolbar_leather.guy, 2, HEIGHT - 149, null);
					}
				}
			}
			else {
				if(location.equals("armorer") || location.equals("blacksmith")) {
					if(pressDelay == 0 && talkingToCitizen == false && talkingToClerk == false) {
						dialogueSlide = 0;
						talkingToClerk = false;
					}
				}
			}
		
			g.drawRect(2, HEIGHT - 149, WIDTH - 4, 148);
			g.drawRect(2, HEIGHT - 149, 32*5 - 10, 32*4);
			
			if(talkingToClerk == false && talkingToCitizen == false) {
				if(wanted) {
					if(!location.equals("town") && !location.equals("town2")){
						console.showText(">> Theft Failed: you've been caught", g);
					}
				}
			}
			
			for(int i = 0; i < guards.length; i++) {
				if(guards[i].hostile) {
					talkingToGuard = false;
				}
			}

			/*
			if(wanted && location.equals("town") && guard.hp > 0) {
				talkingToGuard = true;
				
				if(dialogueSlide == 0 && talkingToGuard) {
					console.showText(caughtDialogue.slides[0], g);
				}
				else {
					if(dialogueSlide == 1 && talkingToGuard) {
						console.showText(caughtDialogue.slides[1], g);
					}
				}
			}
			*/
			
			if(hostile) {
				talkingToGuard = false;
			}
			
			if(talkingToClerk && talkingToCitizen == false) {
				if(location.equals("armorer") || location.equals("blacksmith")) {
					if(dialogueSlide == 0) {
						console.showText(armorerDialogue.slides[0], g);
					}
					else {
						if(dialogueSlide == 1) {
							console.showText(armorerDialogue.slides[1], g);
						}
					}
				}
			}
			
			if(talkingToCitizen) {
				if(weirdTree.finished) {
					console.showText(">> Citizen: Hey!", g);
				}
				else {
					if(dialogueSlide < 5 && dialogueSlide > -1) {
						if(chopped == -3) {
							console.showText(citizenDialogue.slides[dialogueSlide], g);
						}
						else {
							dialogueSlide = 2;
							console.showText(">> Citizen: Thanks odio. Here's your reward.", g);
						}
					}
				}
			}
			
			if(dialogueSlide == -1) {
				for(int i = 0; i < enemies.length; i++) {
					if(enemies[i].damage > -1) {
						console.showText(">> Health lost: " + enemies[i].damage, g);
					}
				}
			}

			if(dialogueSlide == -1) {
				for(int i = 0; i < dungeons.length; i++) {
					for(int c = 0; c < dungeons[i].rooms.length; c++) {
						if(dungeons[i].Rooms[c] != null) {
							for(int r = 0; r < dungeons[i].Rooms[c].enemyClasses.length; r++) {
								if(dungeons[i].Rooms[c].enemyClasses[r].damage > -1) {
									console.showText(">> Health lost: " + dungeons[i].Rooms[c].enemyClasses[r].damage, g);
								}
							}
						}
					}
				}
			}
			
			
			if(dialogueSlide == -2) {
				console.showText(">> Damage delt: " + damage, g);;
			}
			
			if(dialogueSlide == -4) {
				console.showText(">> Damage delt: 20", g);;
			}
			
			
			for(int i = 0; i < guards.length; i++) {
				if(dialogueSlide == 2 && location.equals(guards[i].location) && talkingToCitizen == false) {
					if(player.fine > 0 && player.gold < player.fine) {
						turn = guards[i].name;
						wanted = false;
						guards[i].hostile = true;
					}
					else {
						player.gold -= player.fine;
						player.removeFromInv(lastItem.name);
						player.fine = 0;
						dialogueSlide = 0;
						talkingToGuard = false;
						wanted = false;
					}
				}
				
				if(dialogueSlide == 3 && location.equals(guards[i].location) && talkingToCitizen == false) {
					turn = guards[i].name;
					wanted = false;
					guards[i].hostile = true;
				}
			}
			
			if(location.equals(guard.location) && guard.hp > 0) {
				g.drawImage(guardImage.guy, guard.x, guard.y, null);
			}
			
			if(location.equals(guard2.location) && guard2.hp > 0) {
				g.drawImage(guardImage.guy, guard2.x, guard2.y, null);
			}
			
			for(int i = 0; i < board.posters.length; i++) {
				for(int r = 0; r < boards.length; r++) {
					if(location.equals(boards[r].posters[i].enemy.location) && boards[r].posters[i].enemy.hp > 0) {
						if(boards[r].posters[i].taken) {
							boards[r].posters[i].enemy.x = 32*20;
							for(int c = 0; c < boards[r].posters[i].enemy.character.length; c++) {
								g.drawImage(boards[r].posters[i].enemy.character[c].guy, boards[r].posters[i].enemy.x, boards[r].posters[i].enemy.y, null);
							}
							
							g.drawImage(boards[r].posters[i].enemy.helmet.guy, boards[r].posters[i].enemy.x, boards[r].posters[i].enemy.y, null);
							g.drawImage(boards[r].posters[i].enemy.chest.guy, boards[r].posters[i].enemy.x, boards[r].posters[i].enemy.y, null);
							g.drawImage(boards[r].posters[i].enemy.legging.guy, boards[r].posters[i].enemy.x, boards[r].posters[i].enemy.y, null);
						}
						else {
							boards[r].posters[i].enemy.x = 32*200;
						}
					}
				}
			}
			
			if(location.equals(jim.location)) {
				g.drawImage(armorClerk.guy, jim.x, jim.y, null);
			}
			
			
			if(talkingToClerk && location.equals("armorer") && dialogueSlide > -1 && dialogueSlide < 2) {	//this if statement will change as more towns are added
				if(okButton.active) {
					g.drawImage(okActive.guy, okButton.x, okButton.y, null);
				}
				else {
					g.drawImage(ok.guy, okButton.x, okButton.y, null);
				}
			}
			
			for(int i = 0; i < guards.length; i++) {
				if(wanted && location.equals(guards[i].location) && dialogueSlide > -1 && guards[i].hp > 0) {
					if(okButton.active) {
						g.drawImage(okActive.guy, okButton.x, okButton.y, null);
					}
					else {
						g.drawImage(ok.guy, okButton.x, okButton.y, null);
					}
				}
			}
			
			if(talkingToClerk && location.equals("blacksmith") && dialogueSlide > -1 && dialogueSlide < 2) {	//this if statement will change as more towns are added
				if(okButton.active) {
					g.drawImage(okActive.guy, okButton.x, okButton.y, null);
				}
				else {
					g.drawImage(ok.guy, okButton.x, okButton.y, null);
				}
			}
			
			for(int i = 0; i < guards.length; i++) {
				if(wanted && location.equals(guards[i].location) && dialogueSlide > -1 && guards[i].hp > 0) {
					if(dialogueSlide != 3) {
						if(noButton.active) {
							g.drawImage(noActive.guy, noButton.x, noButton.y, null);
						}
						else {
							g.drawImage(no.guy, noButton.x, noButton.y, null);
						}
					}
				}
			}
			
			if(weirdTree.finished) {
				if(talkingToCitizen && dialogueSlide > -1 && dialogueSlide < 1) {	//this if statement will change as more towns are added
					if(okButton.active) {
						g.drawImage(okActive.guy, okButton.x, okButton.y, null);
					}
					else {
						g.drawImage(ok.guy, okButton.x, okButton.y, null);
					}
				}
			}
			else {
				if(talkingToCitizen && dialogueSlide > -1 && dialogueSlide < 5) {	//this if statement will change as more towns are added
					if(okButton.active) {
						g.drawImage(okActive.guy, okButton.x, okButton.y, null);
					}
					else {
						g.drawImage(ok.guy, okButton.x, okButton.y, null);
					}
				}
				
				if(chopped == -3 && dialogueSlide != 3) {
					if(noButton.active) {
						g.drawImage(noActive.guy, noButton.x, noButton.y, null);
					}
					else {
						g.drawImage(no.guy, noButton.x, noButton.y, null);
					}
				}
			}
			
			
			if(south.active) {
				g.drawImage(southButtonActive.guy, south.x, south.y, null);
			}
			else {
				g.drawImage(southButton.guy, south.x, south.y, null);
			}
			
			if(north.active) {
				g.drawImage(northButtonActive.guy, north.x, north.y, null);
			}
			else {
				g.drawImage(northButton.guy, north.x, north.y, null);
			}
			
			g.drawImage(compass.guy, north.x-9, north.y+45, null);
			
			if(west.active) {
				g.drawImage(leftActive.guy, west.x, west.y, null);
			}
			else {
				g.drawImage(left.guy, west.x, west.y, null);
			}
			
			if(east.active) {
				g.drawImage(rightActive.guy, east.x, east.y, null);
			}
			else {
				g.drawImage(right.guy, east.x, east.y, null);
			}
			
			
			
			if(location.equals("woods") && chopped == -3) {
				if(mouseCollided(32*3,32*3,32,32) && weirdTree.finished == false) {	//chopping down weird tree
					g.setFont(new Font("Times New Roman",Font.BOLD,30));
					
					g.setColor(Color.black);
					g.drawRect(mousemanager.mousex + 20, mousemanager.mousey + 50, 300, 100);
					
					g.setColor(Color.gray.darker());
					g.fillRect(mousemanager.mousex + 21, mousemanager.mousey + 51, 299, 99);
					
					g.setColor(Color.black);
					g.drawString("Weird tree", mousemanager.mousex + 100, mousemanager.mousey + 110);
					
					g.setFont(new Font("Times New Roman",Font.BOLD,16));
					g.drawString("click to chop", mousemanager.mousex + 125, mousemanager.mousey + 130);
					
					if(mousemanager.isLeftPressed() && pressDelay == 0) {
						chopped = 10000;
						pressDelay = 15;
					}
				}
			}
			
			//directional buttons
			if(mouseCollided(south.x,south.y,south.w,south.h)) {
				south.active = true;
				
				if(mousemanager.isLeftPressed() && pressDelay == 0) {
					if(!East && !West) {
						if(location.equals("town")) {
							player.y++;
							South = true;
							//location = "woods";
							
							for(int i = 0; i < enemies.length; i++) {
								enemies[i].damage = -1;
							}
							
							selectingTarget = false;
							talkingToCitizen = false;
							dialogueSlide = 0;
							woodsMonsterChance = random.nextInt(10);
							pressDelay = 15;
						}
						else {
							if(location.equals("woods")) {
								
								player.y++;
								South = true;
								//location = "town2";
								for(int i = 0; i < enemies.length; i++) {
									enemies[i].damage = -1;
								}
								for(int r = 0; r < guards.length; r++) {
									if(guards[r].hostile) {
										turn = guards[r].name;
									}
								}
								selectingTarget = false;
								talkingToCitizen = false;
								dialogueSlide = 0;
								pressDelay = 15;
							}
							else {
								if(location.equals("woods2")) {
									player.y++;
									South = true;
									
									//location = "woods3";
									for(int i = 0; i < enemies.length; i++) {
										enemies[i].damage = -1;
									}
									selectingTarget = false;
									talkingToCitizen = false;
									dialogueSlide = 0;
									pressDelay = 15;
								}
								else {
									if(location.equals("woods4")) {
										player.y++;
										South = true;
										
										//location = "woods2";
										for(int i = 0; i < enemies.length; i++) {
											enemies[i].damage = -1;
										}
										selectingTarget = false;
										talkingToCitizen = false;
										dialogueSlide = 0;
										pressDelay = 15;
									}
									else {
										if(location.equals("woods5")) {
											player.y++;
											South = true;
											
											//location = "woods6";
											for(int i = 0; i < enemies.length; i++) {
												enemies[i].damage = -1;
											}
											selectingTarget = false;
											talkingToCitizen = false;
											dialogueSlide = 0;
											pressDelay = 15;
										}
										else {
											if(location.equals("camp")) {
												player.y++;
												South = true;
												
												//location = "woods7";
												for(int i = 0; i < enemies.length; i++) {
													enemies[i].damage = -1;
												}
												selectingTarget = false;
												talkingToCitizen = false;
												dialogueSlide = 0;
												pressDelay = 15;
											}
										}
									}
								}
							}
						}
					}
				}
			}
			else {
				south.active = false;
			}
		
			
			if(mouseCollided(east.x,east.y,east.w,east.h)) {
				east.active = true;
				
				if(mousemanager.isLeftPressed() && pressDelay == 0) {
					if(!North && !South) {
						if(location.equals("woods2")) {
							player.x++;
							East = true;
							
							//location = "woods";
							
							for(int i = 0; i < enemies.length; i++) {
								enemies[i].damage = -1;
							}
							
							selectingTarget = false;
							talkingToCitizen = false;
							dialogueSlide = 0;
							woodsMonsterChance = random.nextInt(10);
							pressDelay = 15;
						}
						else {
							if(location.equals("woods3")) {
								player.x++;
								East = true;
								
								//location = "town2";
								for(int i = 0; i < enemies.length; i++) {
									enemies[i].damage = -1;
								}
								selectingTarget = false;
								talkingToCitizen = false;
								
								dialogueSlide = 0;
								pressDelay = 15;
							}
							else {
								if(location.equals("woods5")) {
									player.x++;
									East = true;
									
									//location = "woods2";
									for(int i = 0; i < enemies.length; i++) {
										enemies[i].damage = -1;
									}
									selectingTarget = false;
									talkingToCitizen = false;
									dialogueSlide = 0;
									pressDelay = 15;
								}
								else {
									if(location.equals("woods6")) {
										player.x++;
										East = true;
										
										//location = "woods3";
										for(int i = 0; i < enemies.length; i++) {
											enemies[i].damage = -1;
										}
										selectingTarget = false;
										talkingToCitizen = false;
										dialogueSlide = 0;
										pressDelay = 15;
									}
									else {
										if(location.equals("woods7")) {
											player.x++;
											East = true;
											
											//location = "woods6";
											for(int i = 0; i < enemies.length; i++) {
												enemies[i].damage = -1;
											}
											selectingTarget = false;
											talkingToCitizen = false;
											dialogueSlide = 0;
											pressDelay = 15;
										}
									}
								}
							}
						}
					}
				}
			}
			else {
				east.active = false;
			}
			
			if(mouseCollided(west.x,west.y,west.w,west.h)) {
				west.active = true;
				
				if(mousemanager.isLeftPressed() && pressDelay == 0) {
					if(!North && !South) {
						if(location.equals("woods")) {
							player.x--;
							West = true;
							
							//location = "woods2";
							
							for(int i = 0; i < enemies.length; i++) {
								enemies[i].damage = -1;
							}
							
							selectingTarget = false;
							talkingToCitizen = false;
							dialogueSlide = 0;
							pressDelay = 15;
						}
						else {
							if(location.equals("town2")) {
								for(int i = 0; i < enemies.length; i++) {
									enemies[i].damage = -1;
								}
								player.x--;
								West = true;
								//location = "woods3";
								selectingTarget = false;
								talkingToCitizen = false;
								talkingToGuard = false;
								dialogueSlide = 0;
								pressDelay = 15;
							}
							else {
								if(location.equals("woods2")) {
									player.x--;
									West = true;
									//location = "woods5";
									for(int i = 0; i < enemies.length; i++) {
										enemies[i].damage = -1;
									}
									selectingTarget = false;
									talkingToCitizen = false;
									dialogueSlide = 0;
									pressDelay = 15;
								}
								else {
									if(location.equals("woods3")) {
										player.x--;
										West = true;
										//location = "woods6";
										for(int i = 0; i < enemies.length; i++) {
											enemies[i].damage = -1;
										}
										selectingTarget = false;
										talkingToCitizen = false;
										dialogueSlide = 0;
										pressDelay = 15;
									}
									else {
										if(location.equals("woods6")) {
											player.x--;
											West = true;
											//location = "woods7";
											for(int i = 0; i < enemies.length; i++) {
												enemies[i].damage = -1;
											}
											selectingTarget = false;
											talkingToCitizen = false;
											dialogueSlide = 0;
											pressDelay = 15;
										}
									}
								}
							}
						}
					}
				}
			}
			else {
				west.active = false;
			}
			
			
			if(location.equals("woods") && wanted) {
				hostile = true;
				wanted = false;
			}
			
			
			if(mouseCollided(north.x,north.y,north.w,north.h)) {
				north.active = true;
				
				if(mousemanager.isLeftPressed() && pressDelay == 0) {
					if(!East && !West) {
						if(location.equals("woods")) {
							chicken.hostile = false;
							
							player.y--;
							North = true;
							//location = "town";
							
							for(int i = 0; i < enemies.length; i++) {
								enemies[i].damage = -1;
							}
							
							selectingTarget = false;
							
							turn = "guard";
							selectingTarget = false;
							talkingToCitizen = false;
							dialogueSlide = 0;
							pressDelay = 15;
						}
						else {
							if(location.equals("town2")) {
								for(int r = 0; r < guards.length; r++) {
									if(location.equals(guards[r].location) && wanted) {
										wanted = false;
										guards[r].hostile = true;
										turn = guards[r].name;
									}
								}
								
								player.y--;
								North = true;
								//location = "woods";
								for(int i = 0; i < enemies.length; i++) {
									enemies[i].damage = -1;
								}
								woodsMonsterChance = random.nextInt(10);
								selectingTarget = false;
								talkingToCitizen = false;
								dialogueSlide = 0;
								pressDelay = 15;
							}
							else {
								if(location.equals("woods3")) {
									player.y--;
									North = true;
									//location = "woods2";
									for(int i = 0; i < enemies.length; i++) {
										enemies[i].damage = -1;
									}
									selectingTarget = false;
									talkingToCitizen = false;
									dialogueSlide = 0;
									pressDelay = 15;
								}
								else {
									if(location.equals("woods2")) {
										player.y--;
										North = true;
										//location = "woods4";
										for(int i = 0; i < enemies.length; i++) {
											enemies[i].damage = -1;
										}
										selectingTarget = false;
										talkingToCitizen = false;
										dialogueSlide = 0;
										pressDelay = 15;
									}
									else {
										if(location.equals("woods6")) {
											player.y--;
											North = true;
											//location = "woods5";
											for(int i = 0; i < enemies.length; i++) {
												enemies[i].damage = -1;
											}
											selectingTarget = false;
											talkingToCitizen = false;
											dialogueSlide = 0;
											pressDelay = 15;
										}
										else {
											if(location.equals("woods7")) {
												player.y--;
												North = true;
												//location = "camp";
												for(int i = 0; i < enemies.length; i++) {
													enemies[i].damage = -1;
												}
												selectingTarget = false;
												talkingToCitizen = false;
												dialogueSlide = 0;
												pressDelay = 15;
											}
										}
									}
								}
							}
						}
					}
				}
			}
			else {
				north.active = false;
			}
			
			if(woodsMonsterChance > 4 && location.equals("woods") && dialogueSlide == 0) {
				console.showText(">> A chicken has appeared", g);
				caught = tester.average+10;
			}
			
			if(woodsMonsterChance > 4 && location.equals("woods") && chicken.hp > 0) {
				g.drawImage(chicken_i.guy, chicken.x, chicken.y,null);
			}
			
			if(location != "woods") {
				chicken.hp = chicken.maxHP;
			}
			
			if(location.equals("woods")) {
				if(chicken.hp < 1 || woodsMonsterChance < 5) {
					dialogueSlide = 0;
					g.setColor(Color.gray.darker());
					g.fillRect(console.x, console.y - 30, 500, 50);
					console.showText(">> You see a town to the north and a town to the south", g);
				}
			}
			
			if(inDungeon) {
				if(dialogueSlide == 0) {
					console.showText(">> You are inside of Mr.Clean's dungeon", g);
				}
			}
			
			if(location.equals("board1")) {	//change this message to show the number of posters available
				int[] xs = new int[] {
					100,400,700
				};
				
				int[] ys = new int[] {
						100,300,200
				};
				
				int[] ws = new int[] {
					189,171,188	
				};
				
				int[] hs = new int[] {
						184,234,231
				};
				
				console.showText(">> You see a notice board filled with wanted posters", g);
				
				for(int i = 0; i < xs.length; i++) {
					if(mouseCollided(xs[i],ys[i],ws[i],hs[i])) {
						if(mousemanager.isLeftPressed() && pressDelay == 0) {
							if(!board.posters[i].taken) {
								player.addToInv(board.posters[i].enemy.name,inventoryVisible);
								inventoryVisible = true;
								board.posters[i].taken = true;
								pressDelay = 15;
							}
						}
					}
				}
			}
			
			if(location.equals("woods2") && dialogueSlide == 0) {
				dialogueSlide = 0;
				g.setColor(Color.gray.darker());
				g.fillRect(console.x, console.y - 30, 500, 50);
				console.showText(">> You can't see any nearby exit from the woods", g);
			}
			
			if(location.equals("camp") && dialogueSlide == 0) {
				dialogueSlide = 0;
				g.setColor(Color.gray.darker());
				g.fillRect(console.x, console.y - 30, 500, 50);
				console.showText(">> You can't see any nearby exit from the woods", g);
			}
			
			if(location.equals("woods7") && dialogueSlide == 0) {
				dialogueSlide = 0;
				g.setColor(Color.gray.darker());
				g.fillRect(console.x, console.y - 30, 500, 50);
				console.showText(">> You see a small camp to the north", g);
			}
			
			if(location.equals("woods4") && dialogueSlide == 0) {
				dialogueSlide = 0;
				g.setColor(Color.gray.darker());
				g.fillRect(console.x, console.y - 30, 500, 50);
				console.showText(">> You can't see any nearby exit from the woods", g);
			}
			
			if(location.equals("woods6") && dialogueSlide == 0) {
				dialogueSlide = 0;
				g.setColor(Color.gray.darker());
				g.fillRect(console.x, console.y - 30, 500, 50);
				console.showText(">> You see a dungeon off to the side... and it's, unnervingly clean...", g);
			}
			
			if(location.equals("woods5") && dialogueSlide == 0) {
				dialogueSlide = 0;
				g.setColor(Color.gray.darker());
				g.fillRect(console.x, console.y - 30, 500, 50);
				console.showText(">> You can't see any nearby exit from the woods", g);
			}
			
			if(location.equals("woods3") && dialogueSlide == 0) {
				dialogueSlide = 0;
				g.setColor(Color.gray.darker());
				g.fillRect(console.x, console.y - 30, 500, 50);
				console.showText(">> You see a town to the east", g);
			}
			
			if(location.equals("armorer") || location.equals("armorer2")) {
				if(talkingToClerk == false && talkingToCitizen == false && prob <= caught) {
					if(!wanted) {
						console.showText(">> You see a variety of armor for sale on the counter", g);
					}
				}
			}
			
			if(location.equals("magic shop")) {
				if(talkingToClerk == false && talkingToCitizen == false && prob <= caught) {
					if(!wanted) {
						console.showText(">> You see a variety of magic items for sale on the counter", g);
					}
				}
			}
			
			if(location.equals("castle") || location.equals("castle2")) {
				if(talkingToClerk == false && talkingToCitizen == false && prob <= caught) {
					console.showText(">> The castle is very fancy, with just the king inside", g);
				}
			}
			
			if(location.equals("blacksmith") || location.equals("blacksmith2")) {
				if(talkingToClerk == false && talkingToCitizen == false && prob <= caught) {
					if(!wanted) {
						console.showText(">> You see a variety of weapons for sale on the counter", g);
					}
				}
			}
			
			if(location.equals("town")) {
				if(talkingToCitizen == false && wanted == false && dialogueSlide == 0) {
					console.showText(">> You are in a town with 2 houses, a castle, an armorer, and a blacksmith", g);
				}
			}
			
			if(location.equals("town2")) {
				if(talkingToCitizen == false && wanted == false && dialogueSlide == 0) {
					console.showText(">> You are in a town with 3 houses, a castle, an armorer, a blacksmith, and a magic shop", g);
				}
			}
			
			
			//xp and rewards
			if(chicken.hp <= 0 && location.equals("woods")) {
				if(chicken.hp != -2) {
					chicken.hp = -1;
				}
			}
			
			for(int r = 0; r < dungeons.length; r++) {
				for(int c = 0; c < dungeons[r].Rooms.length; c++) {
					if(dungeons[r].Rooms[c] != null) {
						for(int f = 0; f < dungeons[r].Rooms[c].enemyClasses.length; f++) {
							if(dungeons[r].Rooms[c].enemyClasses[f] != null) {
								if(dungeons[r].Rooms[c].enemyClasses[f].hp < 1) {
									if(dungeons[r].Rooms[c].enemyClasses[f].hp != -2) {
										dungeons[r].Rooms[c].enemyClasses[f].hp = -1;
									}
									
									if(dungeons[r].Rooms[c].enemyClasses[f].hp == -1) {
										player.xp += dungeons[r].Rooms[c].enemyClasses[f].xp;
										dungeons[r].Rooms[c].enemyClasses[f].hp = -2;
									}
								}
							}
						}
					}
				}
			}
			
			if(chicken.hp == -1) {
				player.xp += 5;
				chicken.hostile = false;
				woodsMonsterChance = 0;
				chicken.hp = -2;
			}
			
			//wanted
			for(int i = 0; i < guards.length; i++) {
				if(wanted && location.equals(guards[i].location)) {
					talkingToGuard = true;
					
					if(dialogueSlide == 0 && talkingToGuard) {
						console.showText(caughtDialogue.slides[0], g);
					}
					else {
						if(dialogueSlide == 1 && talkingToGuard) {
							console.showText(caughtDialogue.slides[1], g);
						}
					}
				}
			}
			
			//attacking
			for(int i = 0; i < enemies.length; i++) {
				if(mouseCollided(enemies[i].x,enemies[i].y,enemies[i].w,enemies[i].h) && location.equals(enemies[i].location)) {
					if(!playerMenuVisible) {
						if(mousemanager.isLeftPressed() && pressDelay == 0) {
							if(selectingTarget) {
								enemyTarget = enemies[i];
								pressDelay = 15;
								
								if(player.mana + manaBonus >= 10) {
									player.mana -= 10;
									player.manaRegenTimer = 50;
									launchingFireball = true;
									selectingTarget = false;
								}
								else {
									selectingTarget = false;
								}
							}
							else {
								//algorythm that decides how much damage you deal
								if(!launchingFireball) {
									playerCalcDamage();
									
									audioManager.playSound(swing, false);
									enemies[i].hp -= damage;
									dialogueSlide = -2;
									talkingToGuard = false;
									talkingToCitizen = false;
									enemies[i].hostile = true;
									turn = enemies[i].name;
									pressDelay = 40;
								}
							}
						}
					}
				}
			}
			
			for(int i = 0; i < dungeons.length; i++) {
				for(int c = 0; c < dungeons[i].Rooms.length; c++) {
					if(dungeons[i].Rooms[c] != null) {
						for(int r = 0; r < dungeons[i].Rooms[c].enemyClasses.length; r++) {
							if(dungeons[i].Rooms[c].enemyClasses[r] != null) {
								if(mouseCollided(dungeons[i].Rooms[c].enemyClasses[r].x,dungeons[i].Rooms[c].enemyClasses[r].y,dungeons[i].Rooms[c].enemyClasses[r].w,dungeons[i].Rooms[c].enemyClasses[r].h) && location.equals(dungeons[i].Rooms[c].enemyClasses[r].location)) {;
									if(!playerMenuVisible) {
										if(mousemanager.isLeftPressed() && pressDelay == 0) {
											if(selectingTarget) {
												enemyTarget = dungeons[i].Rooms[c].enemyClasses[r];
												pressDelay = 15;
												
												if(player.mana + manaBonus >= 10) {
													player.mana -= 10;
													player.manaRegenTimer = 50;
													launchingFireball = true;
													selectingTarget = false;
												}
												else {
													selectingTarget = false;
												}
											}
											else {
												//algorythm that decides how much damage you deal
												if(!launchingFireball) {
													playerCalcDamage();
													audioManager.playSound(swing, false);
													dungeons[i].Rooms[c].enemyClasses[r].hp -= damage;
													dialogueSlide = -2;
													talkingToGuard = false;
													talkingToCitizen = false;
													dungeons[i].Rooms[c].enemyClasses[r].hostile = true;
													turn = dungeons[i].Rooms[c].enemyClasses[r].name;
													location = dungeons[i].Rooms[c].enemyClasses[r].location;
													pressDelay = 40;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			
			
			for(int i = 0; i < dungeons.length; i++) {
				for(int c = 0; c < dungeons[i].rooms.length; c++) {
					if(dungeons[i].Rooms[c] != null) {
						for(int r = 0; r < dungeons[i].Rooms[c].enemyClasses.length; r++) {
							if(dungeons[i].Rooms[c].enemyClasses[r].hostile && location.equals(dungeons[i].Rooms[c].enemyClasses[r].location)) {
								if(turn.equals(dungeons[i].Rooms[c].enemyClasses[r].name) && dungeons[i].Rooms[c].enemyClasses[r].hp > 0 && pressDelay == 0) {
									armorCheck();
									audioManager.playSound(swing, false);
									dungeons[i].Rooms[c].enemyClasses[r].calcDamage(helm, ch, pa, player.luck);
									player.hp -= dungeons[i].Rooms[c].enemyClasses[r].damage;
									dialogueSlide = -1;
									turn = "you";
									break;
								}
							}
						}
					}
				}
			}
			
			for(int i = 0; i < enemies.length; i++) {
				if(enemies[i].hostile && location.equals(enemies[i].location)) {
					if(turn.equals(enemies[i].name) && enemies[i].hp > 0 && pressDelay == 0) {
						armorCheck();
						audioManager.playSound(swing, false);
						enemies[i].calcDamage(helm, ch, pa, player.luck);
						player.hp -= enemies[i].damage;
						dialogueSlide = -1;
						turn = "you";
						break;
					}
				}
			}
			
			for(int i = 0; i < dungeonEnemies.length; i++) {
				if(dungeonEnemies[i] != null) {
					if(dungeonEnemies[i].hostile && location.equals(dungeonEnemies[i].location)) {
						if(turn.equals(dungeonEnemies[i].name) && dungeonEnemies[i].hp > 0 && pressDelay == 0) {
							armorCheck();
							audioManager.playSound(swing, false);
							dungeonEnemies[i].calcDamage(helm, ch, pa, player.luck);
							player.hp -= dungeonEnemies[i].damage;
							dialogueSlide = -1;
							turn = "you";
							break;
						}
					}
				}
			}
		
			
			if(pressDelay < 1 && guard.hostile && dialogueSlide == -2) {
				turn = guard.name;
			}
			
			if(pressDelay < 1 && chicken.hostile && dialogueSlide == -2) {
				turn = chicken.name;
			}
			
			
			//health bar
			g.setColor(Color.red);
			//fills the bar based with red on your hp, the math extends the bar so it's not too small
			g.fillRect(3, HEIGHT - 20, (player.hp * (player.hp / 15)), 15);
			
			if((healthWidth + manaWidth) < (south.x - 10)) {
				g.setColor(Color.CYAN);
				g.fillRect(3+healthWidth, HEIGHT-20, (player.mana * (player.mana / 15)), 15);
			}
			else {
				g.setColor(Color.CYAN);
				g.fillRect(3+healthWidth, HEIGHT-20, manaWidth, 15);
			}
			
			g.setColor(Color.black);
			//the math here sets the maximum width of the bar based on your max hp
			g.drawRect(3, HEIGHT - 20, healthWidth, 15);
			
			
			g.setColor(Color.black);
			g.drawRect(3+healthWidth, HEIGHT-20, manaWidth, 15);
			
			g.setFont(new Font("Times New Roman",Font.BOLD,15));
			//sets text that displays your hp in the middle of the health bar
			g.drawString("Hp: " + (player.hp+hpBonus) + "/" + (player.maxHP+hpBonus), (healthWidth/2)-30, HEIGHT-8);
			g.drawString("Mana: " + (player.mana+manaBonus) + "/" + (player.maxMana+manaBonus), healthWidth+((manaWidth/2)-30), HEIGHT-8);
			
			if(buyMenuVisible) {
				g.setColor(Color.gray.darker());
				menuBox_x = armorx + 20;
				menuBox_y = armory + 30;
				menuBox_w = 200;
				menuBox_h = 100;
				
				g.fillRect(menuBox_x, menuBox_y, menuBox_w, menuBox_h);
				
				g.setColor(Color.black);
				g.drawRect(menuBox_x, menuBox_y, menuBox_w, menuBox_h);
			}
			
			for(int i = 0; i < buildings.length; i++) {
				if(mouseCollided(buildings[i].x,buildings[i].y,buildings[i].w,buildings[i].h)) {
					if(!playerMenuVisible) {
						if(location.equals(buildings[i].location)) {
							if(buildings[i].x > WIDTH/2) {
								buildings[i].showDeskAlt(g, mousemanager);
							}
							else {
								buildings[i].showDesc(g, mousemanager);
							}
							
							if(mousemanager.isLeftPressed() && pressDelay == 0) {
								for(int c = 0; c < guards.length; c++) {
									if(talkingToGuard || wanted) {
										if(location.equals(guards[c].location)) {
											wanted = false;
											guards[c].hostile = true;
										}
									}
									
									if(guards[c].hostile) {
										turn = guards[c].name;
									}
								}
								
								location = buildings[i].destination;
								player.x = 32*16;
								player.y = 32*10;
								audioManager.playSound(door, false);
								
								if(buildings[i] == dungeon1) {
									inDungeon = true;
								}
								
								for(int c = 0; c < enemies.length; c++) {
									enemies[c].damage = -1;
								}
								
								
								caught = tester.average+10;
								talkingToCitizen = false;
								talkingToGuard = false;
								dialogueSlide = 0;
								
								pressDelay = 15;
							}
						}
					}
				}
			}
			
			if(firstDungeon.playerX == 0 && firstDungeon.playerY == 0 && inDungeon) {
				if(mousemanager.mousex > 32*16 && mousemanager.mousex < 32*17 && mousemanager.mousey > HEIGHT-181 && mousemanager.mousey < HEIGHT-149) {
					g.setFont(new Font("Times New Roman",Font.BOLD,30));
						
					g.setColor(Color.black);
					g.drawRect(mousemanager.mousex + 20, mousemanager.mousey - 120, 300, 100);
						
					g.setColor(Color.gray.darker());
					g.fillRect(mousemanager.mousex + 21, mousemanager.mousey - 121, 299, 99);
						
					g.setColor(Color.black);
					g.drawString("Exit", mousemanager.mousex + 130, mousemanager.mousey - 65);
						
					g.setFont(new Font("Times New Roman",Font.BOLD,16));
					g.drawString("click to exit", mousemanager.mousex + 125, mousemanager.mousey - 45);
						
					if(mousemanager.isLeftPressed() && pressDelay == 0) {
						location = "woods6";
						inDungeon = false;
						audioManager.playSound(door, false);
						talkingToCitizen = false;
						pressDelay = 15;
						dialogueSlide = 0;
					}
				}
			}
				
			if(location.equals("armorer")) {
				if(mousemanager.mousex > 32*16 && mousemanager.mousex < 32*17 && mousemanager.mousey > HEIGHT-181 && mousemanager.mousey < HEIGHT-149) {
					g.setFont(new Font("Times New Roman",Font.BOLD,30));
						
					g.setColor(Color.black);
					g.drawRect(mousemanager.mousex + 20, mousemanager.mousey - 120, 300, 100);
						
					g.setColor(Color.gray.darker());
					g.fillRect(mousemanager.mousex + 21, mousemanager.mousey - 121, 299, 99);
						
					g.setColor(Color.black);
					g.drawString("Town", mousemanager.mousex + 130, mousemanager.mousey - 65);
						
					g.setFont(new Font("Times New Roman",Font.BOLD,16));
					g.drawString("click to enter", mousemanager.mousex + 125, mousemanager.mousey - 45);
				}
			}
			else {
				if(location.equals("castle")) {
					if(mousemanager.mousex > 32*16 && mousemanager.mousex < 32*17 && mousemanager.mousey > HEIGHT-181 && mousemanager.mousey < HEIGHT-149) {
						g.setFont(new Font("Times New Roman",Font.BOLD,30));
							
						g.setColor(Color.black);
						g.drawRect(mousemanager.mousex + 20, mousemanager.mousey - 120, 300, 100);
							
						g.setColor(Color.gray.darker());
						g.fillRect(mousemanager.mousex + 21, mousemanager.mousey - 121, 299, 99);
							
						g.setColor(Color.black);
						g.drawString("Town", mousemanager.mousex + 130, mousemanager.mousey - 65);
							
						g.setFont(new Font("Times New Roman",Font.BOLD,16));
						g.drawString("click to enter", mousemanager.mousex + 125, mousemanager.mousey - 45);
							
						if(mousemanager.isLeftPressed()) {
							location = "town";
							audioManager.playSound(door, false);
							talkingToCitizen = false;
							pressDelay = 15;
							dialogueSlide = 0;
						}
					}
				}
				else {
					if(location.equals("blacksmith")) {
						if(mousemanager.mousex > 32*16 && mousemanager.mousex < 32*17 && mousemanager.mousey > HEIGHT-181 && mousemanager.mousey < HEIGHT-149) {
							g.setFont(new Font("Times New Roman",Font.BOLD,30));
								
							g.setColor(Color.black);
							g.drawRect(mousemanager.mousex + 20, mousemanager.mousey - 120, 300, 100);
								
							g.setColor(Color.gray.darker());
							g.fillRect(mousemanager.mousex + 21, mousemanager.mousey - 121, 299, 99);
								
							g.setColor(Color.black);
							g.drawString("Town", mousemanager.mousex + 130, mousemanager.mousey - 65);
								
							g.setFont(new Font("Times New Roman",Font.BOLD,16));
							g.drawString("click to enter", mousemanager.mousex + 125, mousemanager.mousey - 45);
								
							if(mousemanager.isLeftPressed()) {
								location = "town";
								audioManager.playSound(door, false);
								talkingToCitizen = false;
								talkingToClerk = false;
								pressDelay = 15;
								dialogueSlide = 0;
							}
						}
					}
					else {
						if(location.equals("castle2") || location.equals("blacksmith2") || location.equals("armorer2") || location.equals("magic shop")) {
							if(mousemanager.mousex > 32*16 && mousemanager.mousex < 32*17 && mousemanager.mousey > HEIGHT-181 && mousemanager.mousey < HEIGHT-149) {
								g.setFont(new Font("Times New Roman",Font.BOLD,30));
									
								g.setColor(Color.black);
								g.drawRect(mousemanager.mousex + 20, mousemanager.mousey - 120, 300, 100);
									
								g.setColor(Color.gray.darker());
								g.fillRect(mousemanager.mousex + 21, mousemanager.mousey - 121, 299, 99);
									
								g.setColor(Color.black);
								g.drawString("Town", mousemanager.mousex + 130, mousemanager.mousey - 65);
									
								g.setFont(new Font("Times New Roman",Font.BOLD,16));
								g.drawString("click to enter", mousemanager.mousex + 125, mousemanager.mousey - 45);
									
								if(mousemanager.isLeftPressed()) {
									location = "town2";
									talkingToClerk = false;
									audioManager.playSound(door, false);
									talkingToCitizen = false;
									pressDelay = 15;
									dialogueSlide = 0;
								}
							}
						}
					}
				}
			}
			
			
			
			if(mouseCollided(stats.x,stats.y,stats.w,stats.h)) {
				if(mousemanager.isLeftPressed() && pressDelay == 0) {
					if(!playerMenuState.equals("stats")) {
						playerMenuVisible = true;
						playerMenuState = "stats";
						calcPercent();
						pressDelay = 15;
					}
					else {
						playerMenuVisible = false;
						playerMenuState = "yeet lol";
						pressDelay = 15;
					}
				}
			}
			
			
			//shows the player stat menu
			if(playerMenuVisible) {
				g.drawImage(statsMenu.guy,0,0,null);
				
				g.setColor(Color.black);
				g.drawImage(background.guy,0,0,null);
				g.drawRect(0, 0, 200, 200);
				
				statsArmorCheck();
				
				if(playerMenuState == "stats") {
					g.setColor(Color.black);
					g.setFont(new Font("Times New Roman",Font.BOLD,26));
					g.drawString(player.race, 565, 394);
					g.drawString(player.Class, 565, 420);
					g.drawString(player.name,((Name.w/2)-30)+690,Name.y);
					g.drawString("" + bloodline.year, 690, 182);
					g.drawString("Stupid Idiot", 675,265);
					
					g.drawString("" + (player.maxHP + hpBonus), 535, 445);
					g.drawString("" + (player.Int + intBonus), 535, 473);
					g.drawString("" + (player.str + strBonus), 535, 499);
					g.drawString("" + (player.agi + agiBonus), 536, 521);
					g.drawString("" + (player.luck + luckBonus), 700, 520);
					g.drawString("" + (player.sneak + sneakBonus), 715, 488);
					
					
					g.drawString("Skill points: " + skillPoints, 647, 457);
					
					g.drawString("Level: " + player.level, 200, 110);
					g.drawString("Exp: " + player.xp + "/" + player.maxXP, 200, 150);
					g.drawString("Level progress: " + (int)percent + "%", 200, 190);
				} 
				
				//beans
				g.drawImage(characterLayout.guy,1010,112,null);
				
				if(player.race.equals("orc")) {
					g.drawImage(bigOrcHead.guy, 445, 210, null);
				}
				else {
					if(player.race.equals("human")) {
						g.drawImage(bigHumanHead.guy, 445, 210, null);
					}
					else {
						g.drawImage(bigSkull.guy, 445, 210, null);
					}
				}
				
				if(player.helmet.equals("leather helmet")) {
					g.drawImage(bigLeather.guy, 446, 210, null);
				}
				else {
					if(player.helmet.equals("thief hood")) {
						g.drawImage(bigThief.guy, 445, 210, null);
					}
					else {
						if(player.helmet.equals("mage hood")) {
							g.drawImage(bigHood.guy, 445, 210, null);
						}
						else {
							if(player.helmet.equals("steel helm")) {
								g.drawImage(bigSteel.guy, 445, 210, null);
							}
						}
					}
				}
				
				//only shows the increasing arrow if your hp is 50
				if(player.maxHP ==  player.oldHp) {
					hpRight.x = 570;
					hpLeft.x = -50;	//moves the left arrow so both aren't clicked at the same time
				}
				else {
					if(player.hp == player.oldHp + hpBonus && hpBonus == 5) {	//fixes the arrows moving when 55 isn't the minimum
						hpRight.x = 570;
						hpLeft.x = -50;
					}
					else {
						//puts both in their original positions
						hpLeft.x = 570;
						hpRight.x = 605;
					}
				}

					
				//only shows the increasing arrow if your str is 10
				if(player.str == player.oldStr) {
					strRight.x = 570;
					strLeft.x = -50;	//moves the left arrow so both aren't clicked at the same time
				}
				else {
					//puts both in their original positions
					strLeft.x = 570;
					strRight.x = 605;
				}
					
					
				//only shows the increasing arrow if your luck is 10
				if(player.luck ==  player.oldLuck) {
					luckRight.x = 735;
					luckLeft.x = -50;	//moves the left arrow so both aren't clicked at the same time
				}
				else {
					//puts both in their original positions
					luckLeft.x = 735;
					luckRight.x = 770;
				}
					
					
				//only shows the increasing arrow if your Int is 10
				if(player.Int == player.oldInt) {
					IntRight.x = 570;
					IntLeft.x = -50;	//moves the left arrow so both aren't clicked at the same time
				}
				else {
					//puts both in their original positions
					IntLeft.x = 570;
					IntRight.x = 605;
				}
					
					
				//only shows the increasing arrow if your sneak is 10
				if(player.sneak == player.oldSneak) {
					sneakRight.x = 750;
					sneakLeft.x = -50;	//moves the left arrow so both aren't clicked at the same time
				}
				else {
					//puts both in their original positions
					sneakLeft.x = 750;
					sneakRight.x = 785;
				}
					
					
				//only shows the increasing arrow if your speech is 10
				if(player.agi == player.oldAgi) {
					speechRight.x = 570;
					speechLeft.x = -50;	//moves the left arrow so both aren't clicked at the same time
				}
				else {
					//puts both in their original positions
					speechLeft.x = 570;
					speechRight.x = 605;
				}
				
				if(skillPoints > 0) {
					//displays the hp arrow buttons
					if(hpLeft.active == false) {
						g.drawImage(smallLeft.guy, hpLeft.x, hpLeft.y, null);
					}
					else {
						g.drawImage(smallLeftActive.guy, hpLeft.x, hpLeft.y, null);
					}
					
					if(hpRight.active == false) {
						g.drawImage(smallRight.guy, hpRight.x, hpRight.y, null);
					}
					else {
						g.drawImage(smallRightActive.guy, hpRight.x, hpRight.y, null);
					}
					
					
					//displays the str arrow buttons
					if(strLeft.active == false) {
						g.drawImage(smallLeft.guy, strLeft.x, strLeft.y, null);
					}
					else {
						g.drawImage(smallLeftActive.guy, strLeft.x, strLeft.y, null);
					}
					
					if(strRight.active == false) {
						g.drawImage(smallRight.guy, strRight.x, strRight.y, null);
					}
					else {
						g.drawImage(smallRightActive.guy, strRight.x, strRight.y, null);
					}
					
					
					//displays the luck arrow buttons
					if(luckLeft.active == false) {
						g.drawImage(smallLeft.guy, luckLeft.x, luckLeft.y, null);
					}
					else {
						g.drawImage(smallLeftActive.guy, luckLeft.x, luckLeft.y, null);
					}
					
					if(luckRight.active == false) {
						g.drawImage(smallRight.guy, luckRight.x, luckRight.y, null);
					}
					else {
						g.drawImage(smallRightActive.guy, luckRight.x, luckRight.y, null);
					}
					
					
					//displays the Int arrow buttons
					if(IntLeft.active == false) {
						g.drawImage(smallLeft.guy, IntLeft.x, IntLeft.y, null);
					}
					else {
						g.drawImage(smallLeftActive.guy, IntLeft.x, IntLeft.y, null);
					}
					
					if(IntRight.active == false) {
						g.drawImage(smallRight.guy, IntRight.x, IntRight.y, null);
					}
					else {
						g.drawImage(smallRightActive.guy, IntRight.x, IntRight.y, null);
					}
					
					
					//displays the sneak arrow buttons
					if(sneakLeft.active == false) {
						g.drawImage(smallLeft.guy, sneakLeft.x, sneakLeft.y, null);
					}
					else {
						g.drawImage(smallLeftActive.guy, sneakLeft.x, sneakLeft.y, null);
					}
					
					if(sneakRight.active == false) {
						g.drawImage(smallRight.guy, sneakRight.x, sneakRight.y, null);
					}
					else {
						g.drawImage(smallRightActive.guy, sneakRight.x, sneakRight.y, null);
					}
					
					
					//displays the speech arrow buttons
					if(speechLeft.active == false) {
						g.drawImage(smallLeft.guy, speechLeft.x, speechLeft.y, null);
					}
					else {
						g.drawImage(smallLeftActive.guy, speechLeft.x, speechLeft.y, null);
					}
					
					if(speechRight.active == false) {
						g.drawImage(smallRight.guy, speechRight.x, speechRight.y, null);
					}
					else {
						g.drawImage(smallRightActive.guy, speechRight.x, speechRight.y, null);
					}
					
					
					if(mouseCollided(hpLeft.x,hpLeft.y,hpLeft.w,hpLeft.h)) {
						hpLeft.active = true;
						
						if(pressDelay == 0) {
							if(mousemanager.isLeftPressed()) {
								skillPoints++;
								player.maxHP--;
								pressDelay = 15;
							}
						}
					}
					else {
						hpLeft.active = false;
					}
					
					if(mouseCollided(hpRight.x,hpRight.y,hpRight.w,hpRight.h)) {
						hpRight.active = true;
						
						if(pressDelay == 0) {
							if(mousemanager.isLeftPressed() && skillPoints > 0) {
								player.maxHP++;
								player.hp = player.maxHP;
								skillPoints--;
								pressDelay = 15;
							}
						}
					}
					else {
						hpRight.active = false;
					}
					
					
					if(mouseCollided(strLeft.x,strLeft.y,strLeft.w,strLeft.h)) {
						strLeft.active = true;
						
						if(mousemanager.isLeftPressed() && pressDelay == 0) {
							player.str--;
							skillPoints++;
							pressDelay = 15;
						}
					}
					else {
						strLeft.active = false;
					}
					
					if(mouseCollided(strRight.x,strRight.y,strRight.w,strRight.h)) {
						strRight.active = true;
						
						if(mousemanager.isLeftPressed() && pressDelay == 0) {
							if(skillPoints > 0) {
								player.str++;
								skillPoints--;
								pressDelay = 15;
							}
						}
					}
					else {
						strRight.active = false;
					}
					
					
					if(mouseCollided(luckLeft.x,luckLeft.y,luckLeft.w,luckLeft.h)) {
						luckLeft.active = true;
						
						if(mousemanager.isLeftPressed() && pressDelay == 0) {
							player.luck--;
							skillPoints++;
							pressDelay = 15;
						}
					}
					else {
						luckLeft.active = false;
					}
					
					if(mouseCollided(luckRight.x,luckRight.y,luckRight.w,luckRight.h)) {
						luckRight.active = true;
						
						if(mousemanager.isLeftPressed() && pressDelay == 0) {
							if(skillPoints > 0) {
								player.luck++;
								skillPoints--;
								pressDelay = 15;
							}
						}
					}
					else {
						luckRight.active = false;
					}
					
					
					if(mouseCollided(IntLeft.x,IntLeft.y,IntLeft.w,IntLeft.h)) {
						IntLeft.active = true;
						
						if(mousemanager.isLeftPressed() && pressDelay == 0) {
							player.Int--;
							skillPoints++;
							pressDelay = 15;
						}
					}
					else {
						IntLeft.active = false;
					}
					
					if(mouseCollided(IntRight.x,IntRight.y,IntRight.w,IntRight.h)) {
						IntRight.active = true;
						
						if(mousemanager.isLeftPressed() && pressDelay == 0) {
							if(skillPoints > 0) {
								player.Int++;
								skillPoints--;
								pressDelay = 15;
							}
						}
					}
					else {
						IntRight.active = false;
					}
					
					
					if(mouseCollided(sneakLeft.x,sneakLeft.y,sneakLeft.w,sneakLeft.h)) {
						sneakLeft.active = true;
						
						if(mousemanager.isLeftPressed() && pressDelay == 0) {
							player.sneak--;
							skillPoints++;
							pressDelay = 15;
						}
					}
					else {
						sneakLeft.active = false;
					}
					
					if(mouseCollided(sneakRight.x,sneakRight.y,sneakRight.w,sneakRight.h)) {
						sneakRight.active = true;
						
						if(mousemanager.isLeftPressed() && pressDelay == 0) {
							if(skillPoints > 0) {
								player.sneak++;
								skillPoints--;
								pressDelay = 15;
							}
						}
					}
					else {
						sneakRight.active = false;
					}
					
					
					if(mouseCollided(speechLeft.x,speechLeft.y,speechLeft.w,speechLeft.h)) {
						speechLeft.active = true;
						
						if(mousemanager.isLeftPressed() && pressDelay == 0) {
							player.agi--;
							skillPoints++;
							pressDelay = 15;
						}
					}
					else {
						speechLeft.active = false;
					}
					
					if(mouseCollided(speechRight.x,speechRight.y,speechRight.w,speechRight.h)) {
						speechRight.active = true;
						
						if(mousemanager.isLeftPressed() && pressDelay == 0) {
							if(skillPoints > 0) {
								player.agi++;
								skillPoints--;
								pressDelay = 15;
							}
						}
					}
					else {
						speechRight.active = false;
					}
				}
				
				
				//shows the face
				if(player.race.equals("orc")) {
					g.drawImage(mediumOrc.guy, 67, 30, null);
				}
				else {
					if(player.race.equals("human")) {
						g.drawImage(mediumHuman.guy, 67, 30, null);
					}
					else {
						g.drawImage(mediumSkull.guy, 67, 30, null);
					}
				}
				
				//shows torso
				if(player.race.equals("orc")) {
					g.drawImage(mediumOrcTorso.guy, 67, 30, null);
				}
				else {
					if(player.race.equals("human")) {
						g.drawImage(mediumHumanTorso.guy, 67, 30, null);
					}
					else {
						g.drawImage(mediumSkeletonChest.guy, 67, 30, null);
					}
				}
				
				//shows chestplates
				if(player.chest.equals("leather chest")) {
					g.drawImage(mediumLeatherChest.guy, 67, 30, null);
				}
				else {
					if(player.chest.equals("steel chest")) {
						g.drawImage(mediumSteelChest.guy, 67, 30, null);
					}
					else {
						if(player.chest.equals("thief robe")) {
							g.drawImage(mediumThiefChest.guy, 67, 30, null);
						}
						else {
							if(player.chest.equals("mage robe")) {
								g.drawImage(mediumMageChest.guy, 67, 30, null);
							}
							else {
								if(player.chest.equals("thief robe t2")) {
									g.drawImage(mediumTier2ThiefRobe.guy, 67, 30, null);
								}
							}
						}
					}
				}
				
				//shows legs
				if(player.legs.equals("steel leggings")) {
					g.drawImage(mediumSteelLegs.guy, 67, 30, null);
				}
				else {
					if(player.legs.equals("thief pants t2")) {
						g.drawImage(mediumTier2ThiefPants.guy, 67, 30, null);
					}
					else {
						g.drawImage(mediumPants.guy, 67, 30, null);
					}
				}
				
				
				//shows your helmet
				if(player.helmet.equals("leather helmet")) {
					g.drawImage(mediumLeather.guy, 67, 30, null);
				}
				else {
					if(player.helmet.equals("mage hood")) {
						g.drawImage(mediumHood.guy, 67, 30, null);
					}
					else {
						if(player.helmet.equals("thief hood")) {
							g.drawImage(mediumThief.guy, 67, 30, null);
							sneakBonus = 1;
						}
						else {
							if(player.helmet.equals("crown")) {
								g.drawImage(mediumCrown.guy, 67, 30, null);
							}
							else {
								if(player.helmet.equals("thief hood t2")) {
									g.drawImage(mediumTier2ThiefHood.guy, 67, 30, null);
								}
								else {
									g.drawImage(mediumSteelHelm.guy, 67, 30, null);
								}
							}
						}
					}
				}
				
				if(talkingToCitizen) {
					g.drawImage(toolbar_human.guy, 32*18, 32*6 + 15, null);
					g.drawImage(toolbar_leather.guy, 32*18, 32*6 + 15, null);
				}
			}
		}
		
		
		if(state == "main menu") {	//graphics you see while in the main menu
			//gray background until i have a main menu image
			g.setColor(Color.gray);
			g.fillRect(0, 0, WIDTH, HEIGHT);
			
			//new game button
			if(newGameButton.active == false) {
				g.drawImage(newGame.guy,newGameButton.x,newGameButton.y,null);
			}
			else {
				g.drawImage(newGameActive.guy,newGameButton.x,newGameButton.y,null);
			}
			
			if(keymanager.keyPress) {
				audioManager.playSound(writingSound, false);
				player.name = player.name + keymanager.lastKeyPressed;
				keymanager.keyPress = false;
			}
			
			if(keymanager.backspace && player.name.length() > 0) {
				audioManager.playSound(writingSound, false);
				player.name = player.name.substring(0,player.name.length()-1);
				keymanager.backspace = false;
			}
			
			
			//draws the character creation menu
			if(creatingCharacter) {	//sets up the character creation screen
				//shows the head in the box
				g.drawImage(creationMenu.guy,0,0,null);
				
				if(player.race.equals("orc")) {
					g.drawImage(bigOrcHead.guy, 445, 210, null);
				}
				else {
					if(player.race.equals("human")) {
						g.drawImage(bigHumanHead.guy, 445, 210, null);
					}
					else {
						g.drawImage(bigSkull.guy, 445, 210, null);
					}
				}
				
				//shows starter equipment on the head and sets the player's starter weapon
				if(player.Class == "warrior") {
					g.drawImage(bigLeather.guy, 446, 210, null);
					playerWeapon = woodSword.name;
				}
				else {
					if(player.Class == "thief") {
						g.drawImage(bigThief.guy, 445, 210, null);
						playerWeapon = "shank";
					}
					else {
						g.drawImage(bigHood.guy, 445, 210, null);
						playerWeapon = "wand";
					}
				}
				
				//text
				g.setColor(Color.black);
				g.setFont(new Font("Times New Roman",Font.BOLD,26));
				g.drawString(player.race, 565, 394);
				g.drawString(player.Class, 565, 420);
				
				g.drawString("" + (player.maxHP + hpBonus), 535, 445);
				g.drawString("" + (player.Int + intBonus), 535, 473);
				g.drawString("" + (player.str + strBonus), 535, 499);
				g.drawString("" + (player.agi + agiBonus), 536, 521);
				g.drawString("" + (player.luck + luckBonus), 700, 520);
				g.drawString("" + (player.sneak + sneakBonus), 715, 488);
				
				g.drawString("Skill points: " + skillPoints, 647, 457);
				
				g.drawString("" + bloodline.year, 690, 182);
				
				//displays the done button
				if(skillPoints == 0 && !player.name.equals("")) {
					if(doneButton.active == false) {
						g.drawImage(done.guy, doneButton.x, doneButton.y, null);
					}
					else {
						g.drawImage(doneActive.guy, doneButton.x, doneButton.y, null);
					}
				}

				
				//changes the arrow buttons' x based on the race(so it doesn't sit on top of the text)
				if(player.race.equals("orc")) {
					raceLeft.x = 610;
					raceRight.x = 645;
				}
				else {
					raceLeft.x = 650;
					raceRight.x = 685;
				}
				
				if(!typing && player.name.equals("")) {
					g.setFont(new Font("Times New Roman",Font.BOLD,20));
					g.drawString("(Your name here)",Name.x,Name.y);
				}
				else {
					g.setFont(new Font("Times New Roman",Font.BOLD,20));
					g.drawString(player.name,((Name.w/2)-30)+690,Name.y);
				}
				
				//displays the race arrow buttons
				if(raceLeft.active == false) {
					g.drawImage(smallLeft.guy, raceLeft.x, raceLeft.y, null);
				}
				else {
					g.drawImage(smallLeftActive.guy, raceLeft.x, raceLeft.y, null);
				}
				
				if(raceRight.active == false) {
					g.drawImage(smallRight.guy, raceRight.x, raceRight.y, null);
				}
				else {
					g.drawImage(smallRightActive.guy, raceRight.x, raceRight.y, null);
				}
				
				
				//changes class button positions
				if(player.Class.equals("warrior")) {
					classLeft.x = 650;
					classRight.x = 685;
				}
				else {
					classLeft.x = 630;
					classRight.x = 665;
				}
				
				//only shows the increasing arrow if your hp is 50
				if(player.maxHP ==  player.oldHp) {
					hpRight.x = 570;
					hpLeft.x = -50;	//moves the left arrow so both aren't clicked at the same time
				}
				else {
					if(player.hp == player.oldHp + hpBonus && hpBonus == 5) {	//fixes the arrows moving when 55 isn't the minimum
						hpRight.x = 570;
						hpLeft.x = -50;
					}
					else {
						//puts both in their original positions
						hpLeft.x = 570;
						hpRight.x = 605;
					}
				}

					
				//only shows the increasing arrow if your str is 10
				if(player.str == 10) {
					strRight.x = 570;
					strLeft.x = -50;	//moves the left arrow so both aren't clicked at the same time
				}
				else {
					if(player.str == 15 && strBonus == 5) {
						strRight.x = 570;
						strLeft.x = -50;
					}
					else {
						//puts both in their original positions
						strLeft.x = 570;
						strRight.x = 605;
					}
				}
					
					
				//only shows the increasing arrow if your luck is 10
				if(player.luck ==  10) {
					luckRight.x = 735;
					luckLeft.x = -50;	//moves the left arrow so both aren't clicked at the same time
				}
				else {
					if(player.luck == 15 && luckBonus == 5) {
						luckRight.x = 735;
						luckLeft.x = -50;
					}
					else {
						//puts both in their original positions
						luckLeft.x = 735;
						luckRight.x = 770;
					}
				}
					
					
				//only shows the increasing arrow if your Int is 10
				if(player.Int == 10) {
					IntRight.x = 570;
					IntLeft.x = -50;	//moves the left arrow so both aren't clicked at the same time
				}
				else {
					if(player.Int == 15 && intBonus == 5) {
						IntRight.x = 570;
						IntLeft.x = -50;
					}
					else {
						//puts both in their original positions
						IntLeft.x = 570;
						IntRight.x = 605;
					}
				}
					
					
				//only shows the increasing arrow if your sneak is 10
				if(player.sneak == 10) {
					sneakRight.x = 750;
					sneakLeft.x = -50;	//moves the left arrow so both aren't clicked at the same time
				}
				else {
					if(player.sneak == 15 && sneakBonus == 5) {
						sneakRight.x = 750;
						sneakLeft.x = -50;
					}
					else {
						//puts both in their original positions
						sneakLeft.x = 750;
						sneakRight.x = 785;
					}
				}
					
					
				//only shows the increasing arrow if your speech is 10
				if(player.agi == 10) {
					speechRight.x = 570;
					speechLeft.x = -50;	//moves the left arrow so both aren't clicked at the same time
				}
				else {
					if(player.agi == 15 && agiBonus == 5) {
						speechRight.x = 570;
						speechLeft.x = -50;	//moves the left arrow so both aren't clicked at the same time
					}
					else {
						//puts both in their original positions
						speechLeft.x = 570;
						speechRight.x = 605;
					}
				}
				
				
				//displays the hp arrow buttons
				if(hpLeft.active == false) {
					g.drawImage(smallLeft.guy, hpLeft.x, hpLeft.y, null);
				}
				else {
					g.drawImage(smallLeftActive.guy, hpLeft.x, hpLeft.y, null);
				}
				
				if(hpRight.active == false) {
					g.drawImage(smallRight.guy, hpRight.x, hpRight.y, null);
				}
				else {
					g.drawImage(smallRightActive.guy, hpRight.x, hpRight.y, null);
				}
				
				
				//displays the str arrow buttons
				if(strLeft.active == false) {
					g.drawImage(smallLeft.guy, strLeft.x, strLeft.y, null);
				}
				else {
					g.drawImage(smallLeftActive.guy, strLeft.x, strLeft.y, null);
				}
				
				if(strRight.active == false) {
					g.drawImage(smallRight.guy, strRight.x, strRight.y, null);
				}
				else {
					g.drawImage(smallRightActive.guy, strRight.x, strRight.y, null);
				}
				
				
				//displays the luck arrow buttons
				if(luckLeft.active == false) {
					g.drawImage(smallLeft.guy, luckLeft.x, luckLeft.y, null);
				}
				else {
					g.drawImage(smallLeftActive.guy, luckLeft.x, luckLeft.y, null);
				}
				
				if(luckRight.active == false) {
					g.drawImage(smallRight.guy, luckRight.x, luckRight.y, null);
				}
				else {
					g.drawImage(smallRightActive.guy, luckRight.x, luckRight.y, null);
				}
				
				
				//displays the Int arrow buttons
				if(IntLeft.active == false) {
					g.drawImage(smallLeft.guy, IntLeft.x, IntLeft.y, null);
				}
				else {
					g.drawImage(smallLeftActive.guy, IntLeft.x, IntLeft.y, null);
				}
				
				if(IntRight.active == false) {
					g.drawImage(smallRight.guy, IntRight.x, IntRight.y, null);
				}
				else {
					g.drawImage(smallRightActive.guy, IntRight.x, IntRight.y, null);
				}
				
				
				//displays the sneak arrow buttons
				if(sneakLeft.active == false) {
					g.drawImage(smallLeft.guy, sneakLeft.x, sneakLeft.y, null);
				}
				else {
					g.drawImage(smallLeftActive.guy, sneakLeft.x, sneakLeft.y, null);
				}
				
				if(sneakRight.active == false) {
					g.drawImage(smallRight.guy, sneakRight.x, sneakRight.y, null);
				}
				else {
					g.drawImage(smallRightActive.guy, sneakRight.x, sneakRight.y, null);
				}
				
				
				//displays the speech arrow buttons
				if(speechLeft.active == false) {
					g.drawImage(smallLeft.guy, speechLeft.x, speechLeft.y, null);
				}
				else {
					g.drawImage(smallLeftActive.guy, speechLeft.x, speechLeft.y, null);
				}
				
				if(speechRight.active == false) {
					g.drawImage(smallRight.guy, speechRight.x, speechRight.y, null);
				}
				else {
					g.drawImage(smallRightActive.guy, speechRight.x, speechRight.y, null);
				}
				
				
				//displays the class arrow buttons
				if(classLeft.active == false) {
					g.drawImage(smallLeft.guy, classLeft.x, classLeft.y, null);
				}
				else {
					g.drawImage(smallLeftActive.guy, classLeft.x, classLeft.y, null);
				}
				
				if(classRight.active == false) {
					g.drawImage(smallRight.guy, classRight.x, classRight.y, null);
				}
				else {
					g.drawImage(smallRightActive.guy, classRight.x, classRight.y, null);
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {	//all game logic goes in here
		//change this later to loading previous save game
		if(player.hp <= 0) {	//player death
			try {
				loadGame();
			}
			catch(Exception e) {
				player = new Player();
				questJournal = new Journal();
				location = "town";
				guard.hostile = false;
				wanted = false;
				chopped = -3;
				weirdTree.finished = false;
				skillPoints = 3;
				dialogueSlide = 0;
				inventoryVisible = true;
				creatingCharacter = false;
				state = "main menu";
			}
		}
		
		if(pressDelay > 0) {	//delay so buttons dont regester like 50 clicks instead of just 1
			pressDelay--;
		}
		
		if(jim.moveTimer > 0) {
			jim.moveTimer--;
		}
		
		if(jim.moveTimer == 0 && location != jim.location) {
			jim.setLocation("town");
			jim.moveTimer = 2000;
		}
		
		if(chopped > 0) {
			chopped--;
		}
		
		if(player.manaRegenTimer > 0) {
			player.manaRegenTimer--;
		}
		
		if(player.manaRegenTimer == 0) {
			player.mana += Math.floor(player.manaGain);
			player.manaRegenTimer = 50;
		}
		
		if(player.mana > player.maxMana) {
			player.mana = player.maxMana;
		}
		
		
		
		if(playerWeapon != null) {
			if(playerWeapon.equals(woodSword.name)) {
				weaponDamage = 3;
			}
			else {
				if(playerWeapon.equals("wand")) {
					weaponDamage = 1;
				}
				else {
					if(playerWeapon.equals("shank")) {
						weaponDamage = 2;
					}
					else {
						if(playerWeapon.equals(SteelSword.name)) {
							weaponDamage = 5;
						}
						else {
							if(playerWeapon.equals("dagger")) {
								weaponDamage = 4;
							}
						}
					}
				}
			}
		}
		
		//armor restock timer and caught checking timer
		if(armorRestock > 0) {
			armorRestock--;
		}
		
		//it is used to see if caught is below 10 for about 1 second
		if(caughtChecker > 0) {
			caughtChecker--;
		}
		if(wanted) {
			if(mouseCollided(okButton.x,okButton.y,okButton.w,okButton.h)) {
				okButton.active = true;
				
				if(mousemanager.isLeftPressed() && pressDelay == 0) {
					if(dialogueSlide > -1 && dialogueSlide != 2) {	//change back to 1 later when i add decisions
						if(dialogueSlide == 3 && chopped == -3 && talkingToCitizen) {
							dialogueSlide = 0;
							talkingToCitizen = false;
							pressDelay = 15;
						}
						else {
							dialogueSlide++;
							pressDelay = 15;
						}
					}
				}
			}
			else {
				okButton.active = false;
			}
		}
		
		if(talkingToClerk) {
			if(mouseCollided(okButton.x,okButton.y,okButton.w,okButton.h)) {
				okButton.active = true;
				
				if(mousemanager.isLeftPressed() && pressDelay == 0) {
					if(dialogueSlide > -1) {	//change back to 1 later when i add decisions
						dialogueSlide++;
						pressDelay = 15;
						
						if(dialogueSlide > 1) {
							talkingToClerk = false;
							dialogueSlide = 0;
						}	
					}
				}
			}
			else {
				okButton.active = false;
			}
		}
		
		
		if(talkingToCitizen) {
			if(mouseCollided(okButton.x,okButton.y,okButton.w,okButton.h)) {
				okButton.active = true;
				
				if(mousemanager.isLeftPressed() && pressDelay == 0) {
					if(dialogueSlide > -1) {	//change back to 1 later when i add decisions
						dialogueSlide++;
						pressDelay = 15;
						
						if(dialogueSlide == 4) {
							dialogueSlide = 15;
						}
						
						if(weirdTree.finished) {
							if(dialogueSlide > 0) {
								talkingToCitizen = false;
								talkingToClerk = false;
								dialogueSlide = 0;
							}
						}
						else {
							if(dialogueSlide >= 3 && dialogueSlide < 15) {
								talkingToCitizen = false;
								talkingToClerk = false;
								questJournal.addQuest(weirdTree);
								dialogueSlide = 0;
								
								if(chopped > -3 && !weirdTree.finished) {
									questJournal.questsCompleted[0] = true;
									weirdTree.finished = true;
									player.xp += weirdTree.xp;
									player.gold += weirdTree.gold;
									questJournal.removeQuest(weirdTree);
								}
							}
							else {
								if(dialogueSlide == 15) {
									talkingToCitizen = false;
									talkingToClerk = false;
									questJournal.removeQuest(weirdTree);
									dialogueSlide = 0;
								}
							}
						}
					}
				}
			}
			else {
				okButton.active = false;
			}
		}
		

		
		if(location.equals("armorer")) {
			if(mousemanager.mousex > 32*16 && mousemanager.mousex < 32*17 && mousemanager.mousey > HEIGHT-181 && mousemanager.mousey < HEIGHT-149) {
				if(mousemanager.isLeftPressed()) {
					location = "town";
					talkingToClerk = false;
					audioManager.playSound(door, false);
					talkingToCitizen = false;
				}
			}
		}
		
		if(state == "main menu") {	//main menu logic
			//adds class bonuses to stats and sets starter gear
			if(player.Class == "thief") {
				sneakBonus = 5;
				agiBonus = 5;
				intBonus = 0;
				hpBonus = 0;
				luckBonus = 0;
				strBonus = 0;
				
				player.helmet = "thief hood";
				player.chest = "thief robe";
				player.legs = "thief pants";
			}
			else {
				if(player.Class == "mage") {
					intBonus = 5;
					luckBonus = 5;
					sneakBonus = 0;
					agiBonus = 0;
					hpBonus = 0;
					strBonus = 0;
					
					player.helmet = "mage hood";
					player.chest = "mage robe";
					player.legs = "thief pants";
				}
				else {
					strBonus = 5;
					hpBonus = 5;
					intBonus = 0;
					luckBonus = 0;
					sneakBonus = 0;
					agiBonus = 0;
					
					player.helmet = "leather helmet";
					player.chest = "leather chest";
					player.legs = "leather pants";
				}
			}
		}
		
		if(state == "main menu") {
			//button logic
			//checks if the mouse is touching the button
			if(mousemanager.mousex >= newGameButton.x && mousemanager.mousex <= newGameButton.x + newGameButton.w && mousemanager.mousey >= newGameButton.y && mousemanager.mousey <= newGameButton.y + newGameButton.h) {
				newGameButton.active = true;
				
				if(mousemanager.isLeftPressed() && !creatingCharacter) {
					creatingCharacter = true;	//goes to character creation when button is pressed
					
					loadYears();
					bloodline.Setup(timesGameOpened);
				}
			}
			else {
				newGameButton.active = false;	//makes the button darker when the mouse is not touching it
			}
			
			
			if(mouseCollided(raceLeft.x,raceLeft.y,raceLeft.w,raceLeft.h)) {
				raceLeft.active = true;
				
				//cycles between races when the arrows are pressed
				if(mousemanager.isLeftPressed() && pressDelay == 0) {
					if(player.race.equals("human")) {
						player.race = "orc";
						pressDelay = 15;
					}
					else {
						if(player.race.equals("orc")) {
							player.race = "undead";
							pressDelay = 15;
						}
						else {
							player.race = "human";
							pressDelay = 15;
						}
					}
				}
			}
			else {
				raceLeft.active = false;
			}
			
			if(mouseCollided(raceRight.x,raceRight.y,raceRight.w,raceRight.h)) {
				raceRight.active = true;
				
				if(mousemanager.isLeftPressed() && pressDelay == 0) {
					if(player.race.equals("human")) {
						player.race = "undead";
						pressDelay = 15;
					}
					else {
						if(player.race.equals("undead")) {
							player.race = "orc";
							pressDelay = 15;
						}
						else {
							player.race = "human";
							pressDelay = 15;
						}
					}
				}
			}
			else {
				raceRight.active = false;
			}
			
			if(mouseCollided(hpLeft.x,hpLeft.y,hpLeft.w,hpLeft.h)) {
				hpLeft.active = true;
				
				if(pressDelay == 0) {
					if(mousemanager.isLeftPressed()) {
						skillPoints++;
						player.maxHP--;
						pressDelay = 15;
					}
				}
			}
			else {
				hpLeft.active = false;
			}
			
			if(mouseCollided(hpRight.x,hpRight.y,hpRight.w,hpRight.h)) {
				hpRight.active = true;
				
				if(pressDelay == 0) {
					if(mousemanager.isLeftPressed() && skillPoints > 0) {
						player.maxHP++;
						player.hp = player.maxHP;
						skillPoints--;
						pressDelay = 15;
					}
				}
			}
			else {
				hpRight.active = false;
			}
			
			
			if(mouseCollided(strLeft.x,strLeft.y,strLeft.w,strLeft.h)) {
				strLeft.active = true;
				
				if(mousemanager.isLeftPressed() && pressDelay == 0) {
					player.str--;
					skillPoints++;
					pressDelay = 15;
				}
			}
			else {
				strLeft.active = false;
			}
			
			if(mouseCollided(strRight.x,strRight.y,strRight.w,strRight.h)) {
				strRight.active = true;
				
				if(mousemanager.isLeftPressed() && pressDelay == 0) {
					if(skillPoints > 0) {
						player.str++;
						skillPoints--;
						pressDelay = 15;
					}
				}
			}
			else {
				strRight.active = false;
			}
			
			
			if(mouseCollided(luckLeft.x,luckLeft.y,luckLeft.w,luckLeft.h)) {
				luckLeft.active = true;
				
				if(mousemanager.isLeftPressed() && pressDelay == 0) {
					player.luck--;
					skillPoints++;
					pressDelay = 15;
				}
			}
			else {
				luckLeft.active = false;
			}
			
			if(mouseCollided(luckRight.x,luckRight.y,luckRight.w,luckRight.h)) {
				luckRight.active = true;
				
				if(mousemanager.isLeftPressed() && pressDelay == 0) {
					if(skillPoints > 0) {
						player.luck++;
						skillPoints--;
						pressDelay = 15;
					}
				}
			}
			else {
				luckRight.active = false;
			}
			
			
			if(mouseCollided(IntLeft.x,IntLeft.y,IntLeft.w,IntLeft.h)) {
				IntLeft.active = true;
				
				if(mousemanager.isLeftPressed() && pressDelay == 0) {
					player.Int--;
					skillPoints++;
					pressDelay = 15;
				}
			}
			else {
				IntLeft.active = false;
			}
			
			if(mouseCollided(IntRight.x,IntRight.y,IntRight.w,IntRight.h)) {
				IntRight.active = true;
				
				if(mousemanager.isLeftPressed() && pressDelay == 0) {
					if(skillPoints > 0) {
						player.Int++;
						skillPoints--;
						pressDelay = 15;
					}
				}
			}
			else {
				IntRight.active = false;
			}
			
			
			if(mouseCollided(sneakLeft.x,sneakLeft.y,sneakLeft.w,sneakLeft.h)) {
				sneakLeft.active = true;
				
				if(mousemanager.isLeftPressed() && pressDelay == 0) {
					player.sneak--;
					skillPoints++;
					pressDelay = 15;
				}
			}
			else {
				sneakLeft.active = false;
			}
			
			if(mouseCollided(sneakRight.x,sneakRight.y,sneakRight.w,sneakRight.h)) {
				sneakRight.active = true;
				
				if(mousemanager.isLeftPressed() && pressDelay == 0) {
					if(skillPoints > 0) {
						player.sneak++;
						skillPoints--;
						pressDelay = 15;
					}
				}
			}
			else {
				sneakRight.active = false;
			}
			
			
			if(mouseCollided(speechLeft.x,speechLeft.y,speechLeft.w,speechLeft.h)) {
				speechLeft.active = true;
				
				if(mousemanager.isLeftPressed() && pressDelay == 0) {
					player.agi--;
					skillPoints++;
					pressDelay = 15;
				}
			}
			else {
				speechLeft.active = false;
			}
			
			if(mouseCollided(speechRight.x,speechRight.y,speechRight.w,speechRight.h)) {
				speechRight.active = true;
				
				if(mousemanager.isLeftPressed() && pressDelay == 0) {
					if(skillPoints > 0) {
						player.agi++;
						skillPoints--;
						pressDelay = 15;
					}
				}
			}
			else {
				speechRight.active = false;
			}
			
			
			if(mouseCollided(classLeft.x,classLeft.y,classLeft.w,classLeft.h)) {
				classLeft.active = true;
				
				if(mousemanager.isLeftPressed() && pressDelay == 0) {
					if(player.Class == "warrior") {
						player.Class = "mage";
						pressDelay = 15;
					}
					else {
						if(player.Class == "mage") {
							player.Class = "thief";
							pressDelay = 15;
						}
						else {
							player.Class = "warrior";
							pressDelay = 15;
						}
					}
				}
			}
			else {
				classLeft.active = false;
			}
			
			if(mouseCollided(classRight.x,classRight.y,classRight.w,classRight.h)) {
				classRight.active = true;
				
				if(mousemanager.isLeftPressed() && pressDelay == 0) {
					if(player.Class == "warrior") {
						player.Class = "thief";
						pressDelay = 15;
					}
					else {
						if(player.Class == "thief") {
							player.Class = "mage";
							pressDelay = 15;
						}
						else {
							player.Class = "warrior";
							pressDelay = 15;
						}
					}
				}
			}
			else {
				classRight.active = false;
			}
			
			
			if(mouseCollided(doneButton.x,doneButton.y,doneButton.w,doneButton.h)) {
				doneButton.active = true;
				
				if(mousemanager.isLeftPressed() && pressDelay == 0) {
					if(skillPoints == 0 && !player.name.equals("")) {
						loadTimes();
						saveYears();
						
						player.hp = player.maxHP;
						player.maxMana = player.Int + 45;
						player.manaGain = player.maxMana/30;
						player.mana = player.maxMana;
						state = "game";
						player.manaRegenTimer = 50;
						musicTimer = 0;
						pressDelay = 15;
						audioManager.stopSound();
						audioManager.playSound(overworld,true);
						
						if(sneakBonus > 0) {
							player.sneak += (sneakBonus-1);
						}
						
						if(timesGameOpened == 0) {
							saveGame();
							loadGame();
						}
						
						timesGameOpened++;
						saveTimes();
						
						if(player.Class == "mage") {
							player.addToSpells("fireball");
							player.addToInv("", false);
							inventoryVisible = false;
						}
					}
				}
			}
			else {
				doneButton.active = false;
			}
			
		}
		else {
			if(state == "game") {
				//gameplay controls and logic will go here
			}
		}
	}
	
	public void run() {
		init();
		
		while(running) {
			render();
		}
	}

	
	public MouseManager getMouseManager() {
		return mousemanager;
	}
	
	public KeyManager getKeyManager() {
		return keymanager;
	}
}
