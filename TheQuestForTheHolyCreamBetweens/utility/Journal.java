package com.utility;

public class Journal {
	public Quest[] quests;
	public boolean[] questsCompleted;
	public Console slot1, slot2, slot3, slot1L2, slot2L2, slot3L2;
	public boolean visible;
	public int x, y;
	
	public Journal() {
		quests = new Quest[3];
		questsCompleted = new boolean[1];	//change size as more quests are added
		visible = false;
		x = 400;
		y = 100;
		slot1 = new Console(620,150,13);
		slot2 = new Console(slot1.x,slot1.y+50,13);
		slot3 = new Console(slot2.x,slot2.y+50,13);
		slot1L2 = new Console(slot1.x,slot1.y+13,13);
		slot2L2 = new Console(slot2.x,slot2.y+13,13);
		slot3L2 = new Console(slot3.x,slot3.y+13,13);
	}
	
	//removes a quest from your journal
	public void removeQuest(Quest quest) {
		for(int i = 0; i < quests.length; i++) {
			if(quests[i] == quest) {
				quests[i] = null;
				break;
			}
		}
	}
	
	//adds quest to your journal
	public void addQuest(Quest quest) {
		for(int i = 0; i < quests.length; i++) {
			if(quests[i] == null) {
				quests[i] = quest;
				questsCompleted[i] = false;
				break;
			}
			else {
				if(quest == quests[i]) {
					break;
				}
			}
		}
	}
}
