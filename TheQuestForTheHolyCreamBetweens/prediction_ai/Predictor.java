package com.prediction_ai;

import java.util.Random;

public class Predictor {
	public int[] locations;
	Random random;
	public int sum;
	public double percentError;
	
	public Predictor() {
		locations = new int[4];
		random = new Random();
		sum = 0;
		percentError = 0;
	}
	
	public void randomize(int[] list) {
		for(int i = 0; i < list.length; i++) {
			list[i] = (random.nextInt(6) + 1);
		}
	}
	
	public void sum(int[] list) {
		for(int i = 0; i < list.length; i++) {
			sum = 0;
			sum += list[i];
		}
	}
	
	public void calcPercentError(int targetValue,int[] list) {
		sum(list);
		System.out.println("sum: " + sum);
		percentError = Math.abs(((sum-targetValue)/targetValue)*100);
		System.out.println("percentError: " + percentError);
	}
	
	public void predict(int targetValue,int[] list) {
		for(int i = 0; i < list.length; i++) {
			double oldError = percentError;
			int number = 1;
			
			list[i] += number;
			calcPercentError(targetValue,list);
			
			if(number < 7 && number > -7) {
				if(oldError > percentError) {
					if(number > 0) {
						number++;
						number *= -1;
						list[i] = 0;
						list[i] += number;
						calcPercentError(targetValue,list);
					}
					else {
						number--;
						number *= -1;
						list[i] = 0;
						list[i] += number;
						calcPercentError(targetValue,list);
					}
				}
			}
		}
	}
}
