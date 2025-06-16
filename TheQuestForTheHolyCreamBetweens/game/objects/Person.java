package com.game.objects;

import java.util.Random;

public class Person {
	public int x, y, w, h, townLocationPicker, town2LocationPicker, moveTimer;											
	public String location;
	Random random;
	
	public Person(int x, int y, int width, int height, String locationRange) {
		this.x = x;
		this.y = y;
		this.w = width;
		this.h = height;
		townLocationPicker = -1;
		town2LocationPicker = -1;
		random = new Random();
		
		setLocation(locationRange);
		moveTimer = 2000;
	}
	
	//sets the character's location inside of a town (inside a building or outside)
	public void setLocation(String locationRange) {
		if(locationRange == "town") {
			townLocationPicker = random.nextInt(3);
		}
		else {
			if(locationRange == "town2") {
				town2LocationPicker = random.nextInt(4);
			}
		}
					
		if(townLocationPicker == 0) {
			location = "town";
		}
		else {
			if(townLocationPicker == 1) {
				location = "armorer";
			}
			else {
				if(townLocationPicker == 2) {
					location = "blacksmith";
				}
				else {
					if(town2LocationPicker == 0) {
						location = "town2";
					}
					else {
						if(town2LocationPicker == 1) {
							location = "armorer2";
						}
						else {
							if(town2LocationPicker == 2) {
								location = "blacksmith2";
							}
							else {
								if(town2LocationPicker == 3) {
									location = "magic shop";
								}
							}
						}
					}
				}
			}
		}
	}
}
