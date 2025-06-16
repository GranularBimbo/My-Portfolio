package com.game.objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import com.managers.ImageManager;
import com.managers.MouseManager;

public class ShopItem {
	public int restockTimer, x, y, w, h, price;
	public String name,location;
	public Image invImage;
	
	public ShopItem(int x, int y, int width, int height, int price,String name,String location,Image invImage){
		this.restockTimer = 0;
		this.x = x;
		this.y = y;
		this.w = width;
		this.h= height;
		this.price = price;
		this.name = name;
		this.location = location;
		this.invImage = invImage;
	}
	
	public void showDesc(Graphics g,MouseManager mouse) {
		String fullname = name + ": " + price + "g";
		
		int xPosition = (mouse.mousex+((300/2)-((fullname.length()*10)/2)));
		
		g.setFont(new Font("Times New Roman",Font.BOLD,30));
		
		g.setColor(Color.gray.darker());
		g.fillRect(mouse.mousex + 20, mouse.mousey + 50, 300, 100);
		
		g.setColor(Color.black);
		g.drawRect(mouse.mousex + 20, mouse.mousey + 50, 300, 100);
		g.drawString(fullname, xPosition, mouse.mousey + 110);
	}
}
