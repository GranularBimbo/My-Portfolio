package com.game;

import java.util.Random;

public class Tester {
	public int caught,sneak,sneakBonus,luck,average;
	public Random random;
	public int[] answers;
	
	public Tester() {
		random = new Random();
		answers = new int[100];
		sneak = 20;
		average = 10;
		sneakBonus = 0;
		luck = 20;
	}
	
	public void Test(int sneak,int sneakBonus,int luck,int luckBonus) {
		for(int i = 0; i < answers.length; i++) {
			caught = (int) (((double)(random.nextInt(((sneak+sneakBonus)+(luck+luckBonus))-1)+1)/
									 ((sneak+sneakBonus)+(luck+luckBonus)))*((sneak+sneakBonus)+(luck+luckBonus)));
			answers[i] = caught;
			//System.out.println("Test " + i + "; " + sneak + " sneak; " + luck + " luck:" + answers[i]);
		}
		
		for(int i = 0; i < answers.length; i++) {
			average += answers[i];
			
			if(i == 99) {
				average = (int) average/100;
				System.out.println("average: " + average);
			}
		}
	}
}
