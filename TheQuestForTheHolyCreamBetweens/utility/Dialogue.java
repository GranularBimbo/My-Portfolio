package com.utility;

public class Dialogue {
	public String[] slides;
	
	//sets the dialogue to the length you want
	public Dialogue(int numSlides) {
		slides = new String[numSlides];
	}
	
	//adds text to the new dialogue
	public void addSlide(String message) {
		for(int i = 0; i < slides.length; i++) {
			if(slides[i] == null) {
				slides[i] = message;
				break;
			}
		}
	}
}
