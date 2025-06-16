package com.utility;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Console {	//this will control the text based part of the game
	public String text;
	public int x, y, size;
	
	public Console(int x,int y,int fontSize) {
		this.x = x;
		this.y = y;
		this.size = fontSize;
	}
	
	public void showText(String txt,Graphics g) {
		this.text = txt;
		g.setColor(Color.black);
		g.setFont(new Font("Times New Roman",Font.BOLD,size));
		
		g.drawString(this.text, this.x, this.y);
	}
}
