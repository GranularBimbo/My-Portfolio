package com.utility;

public class Quest {
	public String objective,name;
	public int gold,xp;
	public boolean finished;
	
	public Quest(String name,String objective,int goldReward,int xpReward) {
		this.name = name;
		this.objective = objective;
		gold = goldReward;
		xp = xpReward;
		finished = false;
	}
	
	public void finishQuest() {
		finished = true;
	}
}
