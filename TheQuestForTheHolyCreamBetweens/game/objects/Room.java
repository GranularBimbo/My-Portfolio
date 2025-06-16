package com.game.objects;

import java.awt.Image;
import java.util.Random;

import com.managers.ImageManager;

public class Room {
	public Room[] adjacentRooms;
	public int enemies,x,y;
	public boolean hasPortal;
	public String dungeonName;
	public Image roomVariation;
	public Image[] variations;
	public Random random;
	public Enemy[] enemyClasses;
	
	public ImageManager entrance,variation1,variation2,variation3,variation4,variation5,variation6;
	
	public Room(int enemies,String dungeonName) {
		random = new Random();
		int image;
		
		this.enemies = enemies;
		
		enemyClasses = new Enemy[this.enemies];
		
		adjacentRooms = new Room[4];
		x = 0;
		y = 0;
		hasPortal = false;
		this.dungeonName = dungeonName;
		
		entrance = new ImageManager("assets/img/dungeons/entrance.png");
		variation1 = new ImageManager("assets/img/dungeons/variation1.png");
		variation2 = new ImageManager("assets/img/dungeons/variation2.png");
		variation3 = new ImageManager("assets/img/dungeons/variation3.png");
		variation4 = new ImageManager("assets/img/dungeons/variation4.png");
		variation5 = new ImageManager("assets/img/dungeons/variation5.png");
		variation6 = new ImageManager("assets/img/dungeons/variation6.png");
		
		variations = new Image[] {
				variation1.guy,variation2.guy,variation3.guy,variation4.guy,variation5.guy,variation6.guy
		};
		
		image = random.nextInt(variations.length);
		roomVariation = variations[image];
	}
}
