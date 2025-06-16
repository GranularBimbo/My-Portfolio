package Functionality;

import java.util.Random;

public class Human {
	public int height,speed,stamina,intelligence,strength,attentionSpan,daysWithoutFood;
	public final int pSpeed = 10;
	public boolean gotFood,killedP,escapedP,spotted,dead,reproduced;
	public Random r;
	Ecosystem eco;
	
	public Human(Ecosystem eco) {
		r = new Random();
		this.eco = eco;
		
		height = r.nextInt(4)+4; //sets a max of 8 and min of 4
		speed = r.nextInt(15)+5; //sets a max of 20 and min of 5
		stamina = r.nextInt(15)+5; //sets a max of 20 and min of 5
		intelligence = r.nextInt(15)+5; //sets a max of 20 and min of 5
		strength = r.nextInt(15)+5; //sets a max of 20 and min of 5
		attentionSpan = r.nextInt(15)+5; //sets a max of 20 and min of 5
		gotFood = false;
		spotted = false;
		daysWithoutFood = 0;
		reproduced = false;
		dead = false;
		escapedP = false;
		killedP = false;
		
		AccomodateStats();
	}
	
	public Human(int h,int spd,int sta,int Int,int str,int as,Ecosystem eco) {
		this.eco = eco;
		height = h;
		speed = spd;
		stamina = sta;
		intelligence = Int;
		strength = str;
		attentionSpan = as;
		gotFood = false;
		spotted = false;
		escapedP = false;
		daysWithoutFood = 0;
		killedP = false;
		dead = false;
		
		AccomodateStats();
	}
	
	public void FoodGathered() {
		if(!dead) {
			if(eco.amountOfFood > eco.humans) {
				gotFood = true;
				eco.amountOfFood--;
				eco.landFood--;
			}
			
			if(daysWithoutFood >= 3) {
				dead = true;
			}
			
			if(height < eco.treeHeight-2) {
				int probTree = 0;
				int probFood = (speed*2)+(attentionSpan*2)+(stamina*2);
				
				int decider = r.nextInt(100);
				
				if(height >= eco.treeHeight-2) {
					probTree = 75;
				}
				
				
				if(decider <= probTree) {
					if(eco.foodInTrees > 0) {
						gotFood = true;
						eco.foodInTrees--;
						eco.amountOfFood--;
						daysWithoutFood = 0;
					}
				}
				else {
					decider = r.nextInt(100);
					
					if(decider <= probFood) {
						if(eco.landFood > 0) {
							gotFood = true;
							eco.landFood--;
							eco.amountOfFood--;
							daysWithoutFood = 0;
						}else {
							gotFood = false;
							daysWithoutFood++;
						}
					}
					else {
						gotFood = false;
						daysWithoutFood++;
					}
				}
			}
			else {
				int probFood = (speed*3)+(attentionSpan*3)+(stamina*2);
				int decider = r.nextInt(100);
				
				if(decider <= probFood) {
					if(eco.landFood > 0) {
						gotFood = true;
						eco.landFood--;
						eco.amountOfFood--;
						daysWithoutFood = 0;
					}else {
						gotFood = false;
						daysWithoutFood++;
					}
				}
				else {
					gotFood = false;
					daysWithoutFood++;
				}
			}
		}
	}
	
	public void IsSpotted() {
		if(eco.predetors > 0) {
			int probCaught = (eco.humans/eco.predetors)*10;
			probCaught += (height/(eco.AverageHeight()+1))*2;
			
			int decider = r.nextInt(100);
			
			if(decider > probCaught) {
				spotted = true;
				eco.predetors--;
				KillPredetor();
			}
			else {
				spotted = false;
			}
		}
		else {
			spotted = false;
		}
	}
	
	public void KillPredetor() {
		int prob = 0;
		
		if(intelligence >= 17) {
			prob = 75;
		}
		else if(intelligence >= 13 && strength >= 15){
			prob = 75;
		}
		else {
			prob = intelligence+strength+stamina;
		}
		
		int decider = r.nextInt(100);
		
		if(decider > prob) {
			killedP = false;
			EscapePredetor();
		}
		else {
			killedP = true;
			gotFood = true;
			daysWithoutFood = 0;
		}
	}
	
	public void EscapePredetor() {
		if(eco.pSpeed > speed) {
			dead = true;
			escapedP = false;
		}
		else{
			if(stamina >= eco.pStamina) {
				escapedP = true;
				FoodGathered();
			}
			else {
				dead = true;
				escapedP = false;
			}
		}
	}
	
	public void Reproduce() {	//fitness function
		int prob = 0;
		r = new Random();
		
		if(gotFood) {
			prob += 30;
		}
		
		if(killedP || escapedP) {
			prob += 25;
		}
		
		if(!spotted) {
			prob += 20;
		}
		
		if(r.nextInt(100) <= prob) {
			if(eco.humans != eco.population.length) {
				if(eco.humans < eco.population.length-1) {
					if(Mutation()) {
						eco.population[eco.humans+1] = new Human(height+r.nextInt(2)-1,speed+r.nextInt(2)-1,
								stamina+r.nextInt(2)-1,intelligence+r.nextInt(2)-1,strength+r.nextInt(2)-1,
								attentionSpan+r.nextInt(2)-1,eco);
						eco.humans++;
					}
					else {
						eco.population[eco.humans+1] = new Human(height,speed,stamina,intelligence,strength,
						attentionSpan,eco);
						eco.humans++;
					}
					
					reproduced = true;
				}
			}
			else {
				for(int i = 0; i < eco.humans; i++) {
					if(eco.population[i].dead) {
						if(Mutation()) {
							eco.population[i] = new Human(height+r.nextInt(2)-1,speed+r.nextInt(2)-1,
									stamina+r.nextInt(2)-1,intelligence+r.nextInt(2)-1,strength+r.nextInt(2)-1,
									attentionSpan+r.nextInt(2)-1,eco);
							eco.humans++;
						}
						else {
							eco.population[i] = new Human(height,speed,stamina,intelligence,strength,
							attentionSpan,eco);
							eco.humans++;
						}
						
						reproduced = true;
					}
				}
			}
		}
	}
	
	public boolean Mutation() {
		int prob = 50;
		int decider = r.nextInt(100);
		
		if(decider <= prob) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void AccomodateStats(){
		if(speed >= 10) {
			stamina = (int) (speed/3)*2;
		}
		
		if(intelligence >= 10) {
			attentionSpan = (int) (intelligence/3)*2;
		}
	}
}
