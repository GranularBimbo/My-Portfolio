package com.managers;

import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;

public class TextManager {
	public String text;
	public JLabel label;
	private Graphics g;
	
	public TextManager(String text) {
		this.text = text;
	}
	
	public void show(String text,int x,int y) {
		this.text = text;
		g.setFont(new Font("Times New Roman",Font.PLAIN,50));
		g.drawString(this.text, x, y);
	}
}
