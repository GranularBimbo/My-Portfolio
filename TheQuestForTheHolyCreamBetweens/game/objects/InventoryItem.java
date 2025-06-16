package com.game.objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import com.managers.MouseManager;

public class InventoryItem {
	public String name,desc,type;
	public Image image;
	public int bonus,stat;
	public boolean added;
	
	public InventoryItem(String name,Image image,String desc,String type,int bonus,int stat) {
		this.name = name;
		this.image = image;
		this.desc = desc;
		this.type = type;
		this.bonus = bonus;
		this.stat = stat;
		added = false;
	}
	
	public void showDesk(Graphics g,MouseManager mouse) {
		int xPosition = (mouse.mousex-((300/2)+((name.length()*15)/2)));
		String thing = name;
		
		g.setFont(new Font("Times New Roman",Font.BOLD,30));
		
		g.setColor(Color.gray.darker());
		g.fillRect(mouse.mousex - 300, mouse.mousey + 50, 300, 100);
		
		g.setColor(Color.black);
		g.drawRect(mouse.mousex - 300, mouse.mousey + 50, 300, 100);
		
		g.setColor(Color.black);
		g.drawString(name, xPosition, mouse.mousey + 110);
		
		g.setFont(new Font("Times New Roman",Font.BOLD,16));
		if(type.equals("spell")) {
			g.drawString("click to cast", mouse.mousex + ((300/2)-30), mouse.mousey + 130);
		}
		else {
			if(type.equals("equipable")) {
				g.drawString("click to equip", mouse.mousex + ((300/2)-30), mouse.mousey + 130);
			}
			else {
				if(type.equals("potion")) {
					g.drawString("click to drink", mouse.mousex + ((300/2)-30), mouse.mousey + 130);
				}
			}
		}
	}
}
