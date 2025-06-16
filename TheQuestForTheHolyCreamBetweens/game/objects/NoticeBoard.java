package com.game.objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.managers.MouseManager;

public class NoticeBoard {
	public int x,y,w,h;
	public String location;
	public WantedPoster[] posters;
	public String name;
	
	public NoticeBoard(int x,int y,String location,int playerLevel) {
		this.x = x;
		this.y = y;
		w = 64;
		h = 64;
		this.location = location;
		name = "Notice Board";
		posters = new WantedPoster[] {
				new WantedPoster(playerLevel),new WantedPoster(playerLevel),new WantedPoster(playerLevel)
		};
		/*
		for(int i = 0; i < posters.length; i++) {
			if(i < posters.length-1) {
				if(posters[i].enemy.location.equals(posters[i+1].enemy.location)) {
					posters[i] = new WantedPoster(playerLevel);
				}
			}
		}
		*/
	}
	
	public void showDesc(Graphics g,MouseManager mouse) {
		int xPosition = (mouse.mousex+((300/2)-((name.length()*10)/2)));
		
		g.setFont(new Font("Times New Roman",Font.BOLD,30));
		
		g.setColor(Color.gray.darker());
		g.fillRect(mouse.mousex + 20, mouse.mousey + 50, 300, 100);
		
		g.setColor(Color.black);
		g.drawRect(mouse.mousex + 20, mouse.mousey + 50, 300, 100);
		g.drawString(name, xPosition, mouse.mousey + 110);
		
		g.setFont(new Font("Times New Roman",Font.BOLD,16));
		g.drawString("click to view", mouse.mousex + ((300/2)-30), mouse.mousey + 130);
	}
	
	public void showDeskAlt(Graphics g,MouseManager mouse) {
		int xPosition = (mouse.mousex-((300/2)+((name.length()*15)/2)));
		String thing = "click to enter";
		
		g.setFont(new Font("Times New Roman",Font.BOLD,30));
		
		g.setColor(Color.gray.darker());
		g.fillRect(mouse.mousex - 300, mouse.mousey + 50, 300, 100);
		
		g.setColor(Color.black);
		g.drawRect(mouse.mousex - 300, mouse.mousey + 50, 300, 100);
		
		g.setColor(Color.black);
		g.drawString(name, xPosition, mouse.mousey + 110);
		
		g.setFont(new Font("Times New Roman",Font.BOLD,16));
		g.drawString("click to view", mouse.mousex-((300/2)+((thing.length()*7)/2)), mouse.mousey + 130);
	}
}
