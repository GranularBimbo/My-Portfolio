package Functionality;

import java.util.Random;

public class Ecosystem {
	Random random = new Random();
	public int amountOfFood,predetors,foodInTrees,treeHeight,humans,tallPeople,landFood,day,dayLength;
	public final int oldP,oldF,pSpeed,pStamina;
	public boolean trees;
	private int num;
	public Human[] population;
	
	public Ecosystem(int aof,int p,boolean trees,int pop,int treeHeight) {
		amountOfFood = aof;
		predetors = p;
		oldP = p;
		oldF = aof;
		pSpeed = 11;
		pStamina = 9;
		dayLength = 0;
		day = 0;
		this.trees = trees;
		this.treeHeight = treeHeight;
		population = new Human[pop+10000]; //plus 10000 for room to reproduce
		humans = pop;
		
		for(int i = 0; i < pop; i++) {
			population[i] = new Human(this);
			
			if(population[i].height >= treeHeight-2) {
				tallPeople++;
			}
		}
	}
	
	public void Day() {
		if(humans > population.length) {
			humans = population.length;
		}
		
		for(int i = 0; i < humans; i++) {
			if(!population[i].dead) {
				population[i].IsSpotted();
			}
			//if spotted KillPredetor() is called
			//if p is not killed EscapePrededor() is called
		}
		
		ClearDead();
		
		for(int i = 0; i < humans; i++) {
			if(!population[i].dead) {
				population[i].Reproduce();
			}
		}
		
		System.out.println("Average Height:" + AverageHeight() + "ft");
		System.out.println("Average Speed:" + AverageSpe());
		System.out.println("Average Stamina:" + AverageSta());
		System.out.println("Average Strength:" + AverageStr());
		System.out.println("Average Intelligence:" + AverageInt());
		System.out.println("Average Attention Span:" + AverageAS());
		System.out.println("Tree Height:" + treeHeight + "ft");
		System.out.println("Amount of Food:" + amountOfFood);
		System.out.println("People Who Reproduced:" + PeopleReproduced());
		System.out.println("People Spotted By Predetors: " + PeopleSpotted());
		System.out.println("Predetors Killed: " + PredetorsKilled());
		System.out.println("People Who Escaped Predetors: " + PredetorsEscaped());
		System.out.println("People Dead: " + PeopleKilled());
		System.out.println("People Who Got Food: " + PeopleFood());
		System.out.println("Population: " + humans);
		
		if(landFood < 0) {
			landFood = 0;
		}
		
		predetors = oldP;
		amountOfFood = oldF;
		PlaceFood();
	}
	
	public void Week() {
		for(int i = 0; i < 7; i++) {
			System.out.println("");
			System.out.println("");
			System.out.println("Day" + (i+1) + ":");
			day = i+1;
			Day();
		}
	}
	
	public void Month() {
		for(int i = 0; i < 31; i++) {
			System.out.println("");
			System.out.println("");
			System.out.println("Day" + (i+1) + ":");
			day = i+1;
			Day();
		}
	}
	
	public void Months(int months) {
		for(int i = 0; i < months+1; i++) {
			System.out.println("");
			System.out.println("");
			System.out.println("Month" + (i+1) + ":");
			Month();
		}
	}
	
	public int AverageSpe() {
		int[] speeds = new int[humans];
		int sum = 0;
		int avg = 0;
		
		for(int i = 0; i < humans; i++) {
			if(!population[i].dead) {
				speeds[i] = population[i].speed;
			}
		}
		
		for(int i = 0; i < speeds.length; i++) {
			sum += speeds[i];
		}
		
		if(humans > 0) {
			avg = (int) (sum/speeds.length);
		}
		else {
			avg = 1;
		}
		
		return avg;
	}
	
	public void ClearDead() {
		for(int i = 0; i < humans; i++) {
			if(population[i].dead) {
				humans--;
				//population[i] = null;
			}
		}
	}
	
	public int AverageSta() {
		int[] staminas = new int[humans];
		int sum = 0;
		int avg = 0;
		
		for(int i = 0; i < humans; i++) {
			if(!population[i].dead) {
				staminas[i] = population[i].stamina;
			}
		}
		
		for(int i = 0; i < staminas.length; i++) {
			sum += staminas[i];
		}
		
		if(humans > 0) {
			avg = (int) (sum/staminas.length);
		}
		else {
			avg = 1;
		}
		
		return avg;
	}
	
	public int AverageHeight() {
		int[] hs = new int[humans];
		int sum = 0;
		int avg = 0;
		
		for(int i = 0; i < humans; i++) {
			if(!population[i].dead) {
				hs[i] = population[i].height;
			}
		}
		
		for(int i = 0; i < hs.length; i++) {
			sum += hs[i];
		}
		
		if(humans > 0) {
			avg = (int) (sum/hs.length);
		}
		else {
			avg = 1;
		}
		
		return avg;
	}
	
	public int AverageStr() {
		int[] strs = new int[humans];
		int sum = 0;
		int avg = 0;
		
		for(int i = 0; i < humans; i++) {
			if(!population[i].dead) {
				strs[i] = population[i].strength;
			}
		}
		
		for(int i = 0; i < strs.length; i++) {
			sum += strs[i];
		}
		
		if(humans > 0) {
			avg = (int) (sum/strs.length);
		}
		else {
			avg = 1;
		}
		
		return avg;
	}
	
	public int AverageInt() {
		int[] ints = new int[humans];
		int sum = 0;
		int avg = 0;
		
		for(int i = 0; i < humans; i++) {
			if(!population[i].dead) {
				ints[i] = population[i].intelligence;
			}
		}
		
		for(int i = 0; i < ints.length; i++) {
			sum += ints[i];
		}
		
		if(humans > 0) {
			avg = (int) (sum/ints.length);
		}
		else {
			avg = 1;
		}
		
		return avg;
	}
	
	public int AverageAS() {
		int[] as = new int[humans];
		int sum = 0;
		int avg = 0;
		
		for(int i = 0; i < humans; i++) {
			if(!population[i].dead) {
				as[i] = population[i].attentionSpan;
			}
		}
		
		for(int i = 0; i < as.length; i++) {
			sum += as[i];
		}
		
		if(humans > 0) {
			avg = (int) (sum/as.length);
		}
		else {
			avg = 1;
		}
		
		return avg;
	}
	
	public int PeopleSpotted() {
		int sum = 0;
		
		for(int i = 0; i < humans; i++) {
			if(population[i].spotted && !population[i].dead) {
				sum++;
			}
		}
		
		return sum;
	}
	
	public int PredetorsKilled() {
		int sum = 0;
		
		for(int i = 0; i < humans; i++) {
			if(population[i] != null) {
				if(population[i].killedP && !population[i].dead) {
					sum++;
				}
			}
		}
		
		return sum;
	}
	
	public int PredetorsEscaped() {
		int sum = 0;
		
		for(int i = 0; i < humans; i++) {
			if(population[i] != null) {
				if(population[i].escapedP && !population[i].dead) {
					sum++;
				}
			}
		}
		
		return sum;
	}
	
	public int PeopleKilled() {
		int sum = 0;
		
		for(int i = 0; i < humans; i++) {
			if(population[i] != null) {
				if(population[i].dead) {
					sum++;
				}
			}
		}
		
		return sum;
	}
	
	public int PeopleFood() {
		int sum = 0;
		
		for(int i = 0; i < humans; i++) {
			if(population[i] != null) {
				if(population[i].gotFood && !population[i].dead) {
					sum++;
				}
			}
		}
		
		return sum;
	}
	
	public int PeopleReproduced() {
		int sum = 0;
		
		for(int i = 0; i < humans; i++) {
			if(population[i] != null) {
				if(population[i].reproduced && !population[i].dead) {
					sum++;
				}
			}
		}
		
		return sum;
	}
	
	public void PlaceFood() {
		foodInTrees = 0;
		landFood = amountOfFood;
		
		if(trees) {
			for(int i = 0; i < amountOfFood; i++) {
				num = random.nextInt(3);
				
				if(num == 1) {
					foodInTrees++;
					landFood  = amountOfFood-foodInTrees;
				}
			}
		}
	}
}
