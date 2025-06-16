package com.managers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener {
	public int[] keys;
	public char[] keyTranslation;
	public char lastKeyPressed;
	public boolean backspace,keyPress;
	
	public KeyManager() {
		keys = new int[26];
		lastKeyPressed = ' ';
		keyTranslation = new char[26];
		backspace = false;
	}
	

	@Override
	public void keyPressed(KeyEvent e) {
		keys = new int[] {
				KeyEvent.VK_Q,KeyEvent.VK_W,KeyEvent.VK_E,KeyEvent.VK_R,KeyEvent.VK_T,KeyEvent.VK_Y,
				KeyEvent.VK_U,KeyEvent.VK_I,KeyEvent.VK_O,KeyEvent.VK_P,KeyEvent.VK_A,KeyEvent.VK_S,
				KeyEvent.VK_D,KeyEvent.VK_F,KeyEvent.VK_G,KeyEvent.VK_H,KeyEvent.VK_J,KeyEvent.VK_K,
				KeyEvent.VK_L,KeyEvent.VK_Z,KeyEvent.VK_X,KeyEvent.VK_C,KeyEvent.VK_V,KeyEvent.VK_B,
				KeyEvent.VK_N,KeyEvent.VK_M,KeyEvent.VK_SPACE
		};
		
		keyTranslation = new char[] {
				'q','w','e','r','t','y','u','i','o','p','a','s','d','f','g','h','j','k','l','z','x',
				'c','v','b','n','m',' '
		};
		
		for(int i = 0; i < keys.length; i++) {
			if(e.getKeyCode() == keys[i]) {
				lastKeyPressed = keyTranslation[i];
				keyPress = true;
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			backspace = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}
