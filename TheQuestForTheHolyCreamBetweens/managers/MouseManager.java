package com.managers;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseManager implements MouseListener, MouseMotionListener {
	private boolean rightPressed, leftPressed;
	public int mousex, mousey;
	
	public MouseManager() {
		
	}
	
	//getters so that other classes can use the private variables
	public boolean isLeftPressed() {
		return leftPressed;
	}
	
	public boolean isRightPressed() {
		return rightPressed;
	}
	
	//mouse buttons and motion
	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			leftPressed = true;
		}
		else {
			if(e.getButton() == MouseEvent.BUTTON3) {
				rightPressed = true;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			leftPressed = false;
		}
		else {
			if(e.getButton() == MouseEvent.BUTTON3) {
				rightPressed = false;
			}
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		mousex = e.getX();
		mousey = e.getY();
	}

	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
