package com.game.displays;

public class Button {
	public boolean active;
	public int x,y,w,h;
	
	public Button(boolean active,int x,int y,int w,int h) {
		this.active = active;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public void swap() {
		if(active == false) {
			active = true;
		}
		else {
			active = false;
		}
	}
}
