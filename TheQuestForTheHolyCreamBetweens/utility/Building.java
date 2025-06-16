package com.utility;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.managers.MouseManager;

public class Building{
	public int x,y,w,h;
	public String name,location,destination;
	
	public Building(int x,int y,String name,String location,String destination) {
		this.x = x;
		this.y = y;
		w = (32*7);
		h = 32*4;
		this.name = name;
		this.location = location;
		this.destination = destination;
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
		g.drawString("click to enter", mouse.mousex + ((300/2)-30), mouse.mousey + 130);
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
		g.drawString("click to enter", mouse.mousex-((300/2)+((thing.length()*7)/2)), mouse.mousey + 130);
	}
}
