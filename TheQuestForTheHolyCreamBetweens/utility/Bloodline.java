package com.utility;

import java.util.Random;

public class Bloodline {
	public Random random;
	public int year,generations,greatCounter,oldYear;
	public String relation;
	public String[] greats;
	public int lifeSpan;
	
	public Bloodline() {
		random = new Random();
		
		lifeSpan = 31;	//average medieval life expectancy
		generations = 0;
		greatCounter = 0;
		greats = new String[1];
		relation = "father";
		year = 0;
		oldYear = 0;
	}
	
	public void Setup(int times) {
		if(times > 0) {
			year = random.nextInt(200)+1100;	//picks a year between 1100 and 1200
			FindGeneration();
		}
		else {
			year = random.nextInt(200)+1100;	//picks a year between 1100 and 1200
			oldYear = year;
			System.out.println("" + oldYear);
		}
	}
	
	public void FindGeneration() {
		int difference = Math.abs(oldYear-year);
		
		System.out.println("difference: " + difference);
		
		generations = (int) Math.floor(difference/lifeSpan);
		System.out.println("" + generations);
		
		if(oldYear > 0) {
			if(generations == 0) {
				relation = "father";
			}
			else {
				if(generations == 1) {
					relation = "grandfather";
				}
				else {
					for(int i = 2; i < generations; i++) {
						greatCounter++;
					}
					
					greats = new String[greatCounter];
					
					for(int i = 0; i < greats.length; i++) {
						greats[i] = "great";
					}
					
					relation = "great ";
					
					for(int i = 0; i < greats.length; i++) {
						relation = relation.concat(greats[i] + " ");
					}
					
					relation = relation.concat( "grandfather");
				}
			}
		}
		
		/*
		System.out.println("year: " + year);
		System.out.println("oldYear: " + oldYear);
		System.out.println("dataoldYear: " + oldYear);
		System.out.println("generations: " + generations);
		System.out.println("relation: " + relation);
		*/
	}
}
