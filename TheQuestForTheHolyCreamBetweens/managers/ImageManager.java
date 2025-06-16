package com.managers;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ImageManager {
	public JLabel label;
	public Image guy;
	
	public ImageManager(String imagePath) {
		guy = new ImageIcon(imagePath).getImage();
		label = new JLabel(imagePath);
	}
}
