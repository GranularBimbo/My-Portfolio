package com.game.objects;

import com.managers.ImageManager;

public class DungeonDoor {
	public String destination;
	public int x,y,posx,posy;
	public ImageManager image;
	
	public DungeonDoor(int posx,int posy,String destination,int x,int y) {
		this.destination = destination;
		this.x = x;
		this.y = y;
		this.posx = posx;
		this.posy = posy;
		
		image = new ImageManager("assets/img/dungeons/door.png");
	}
}
