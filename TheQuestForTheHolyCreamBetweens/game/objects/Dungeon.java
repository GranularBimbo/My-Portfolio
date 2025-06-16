package com.game.objects;

import java.util.Random;

public class Dungeon {
	public int[][] rooms;
	public Room[] Rooms;
	public DungeonDoor[] Doors;
	public int x,y,playerX,playerY;
	public Room[][] positions;
	public int difficulty,roomCounter,recommendedLevel;
	public Random random;
	String name;
	
	public Dungeon(String name) {
		random = new Random();
		roomCounter = 0;
		x = 0;
		y = 0;
		playerX = 0;
		playerY = 0;
		this.name = name;
		
		difficulty = (random.nextInt(9)+1);
		
		recommendedLevel = difficulty+3;
		
		rooms = new int[difficulty][];
		Rooms = new Room[difficulty];
		Doors = new DungeonDoor[difficulty];
		positions = new Room[difficulty][];
		
		SetDefault();
	}
	
	public Dungeon(int difficulty,String name,int recommendedLevel) {
		random = new Random();
		x = 0;
		y = 0;
		playerX = 0;
		playerY = 0;
		this.name = name;
		this.recommendedLevel = recommendedLevel;
		this.difficulty = difficulty;
		rooms = new int[difficulty][];
		Rooms = new Room[difficulty];
		Doors = new DungeonDoor[difficulty];
		positions = new Room[difficulty][];
		
		SetDefault();
	}
	
	public void SetDefault() {
		for(int i = 0; i < rooms.length; i++) {
			rooms[i] = new int[] {0,0,0,0};
		}
	}
	
	public void SetRandom() {
		for(int i = 0; i < rooms.length; i++) {
			for(int r = 0; r < rooms[i].length; r++) {
				rooms[i][r] = (int)(random.nextInt(difficulty/2));
				
				if(rooms[i][r] > 0) {
					roomCounter++;
				}
				
				if(i == rooms.length-1) {
					Rooms = new Room[roomCounter];
					Doors = new DungeonDoor[roomCounter*4];
					positions = new Room[roomCounter][];
					
					for(int c = 0; c < positions.length; c++) {
						positions[c] = new Room[1];
					}
				}
			}
		}
	}

	public void CreateRooms() {
		for(int i = 0; i < Rooms.length; i++) {
			for(int c = 0; c < rooms.length; c++) {
				for(int r = 0; r < rooms[c].length; r++) {
					if(rooms[c][r] > 0) {
						Rooms[i] = new Room(rooms[c][r],name + i);
					}
				}
			}
		}
	}
	
	public void RoomLocation(Room room) {
		for(int i = 0; i < positions.length; i++) {
			for(int c = 0; c < positions[i].length; c++) {
				if(positions[i][c] == room) {
					x = i;
					y = c;
				}
			}
		}
	}
	
	public void SamePosition(Room room) {
		for(int i = 0; i < Rooms.length; i++) {
			if(Rooms[i].x == room.x && Rooms[i].y == room.y) {
				if(Rooms[i] != room) {
					Rooms[i].x++;
					SamePosition(room);
				}
			}
			
			if(Rooms[i].x == 0 && Rooms[i].y == -1) {
				Rooms[i].x++;
			}
		}
	}
	
	public void DeleteExtras(Room room) {
		for(int i = 0; i < Rooms.length; i++) {
			if(Rooms[i] != null && room != null) {
				if(room.x == Rooms[i].x && room.y == Rooms[i].y) {
					if(Rooms[i] != room) {
						Rooms[i] = null;
					}
				}
			}
		}
	}
	

	public void ConnectRooms(Room room) {
		for(int i = 0; i < Rooms.length; i++) {
			if(Rooms[i] != null && room != null) {
				if(Rooms[i].x == room.x-1) {
					room.adjacentRooms[0] = Rooms[i];
				}
				else {
					if(Rooms[i].x == room.x+1) {
						room.adjacentRooms[2] = Rooms[i];
					}
					else {
						if(Rooms[i].y == room.y+1) {
							room.adjacentRooms[1] = Rooms[i];
						}
						else {
							if(Rooms[i].y == room.y-1) {
								room.adjacentRooms[3] = Rooms[i];
							}
						}
					}
				}
			}
		}
	}
	
	public void ConnectFarRooms(Room room) {
		int incrementer = 2;
		
		for(int i = 0; i < Rooms.length; i++) {
			if(Rooms[i] != null && room != null) {
				if(Rooms[i].x == room.x-incrementer) {
					room.adjacentRooms[0] = Rooms[i];
				}
				else {
					if(Rooms[i].x == room.x+incrementer) {
						room.adjacentRooms[2] = Rooms[i];
					}
					else {
						if(Rooms[i].y == room.y+incrementer) {
							room.adjacentRooms[1] = Rooms[i];
						}
						else {
							if(Rooms[i].y == room.y-incrementer) {
								room.adjacentRooms[3] = Rooms[i];
							}
						}
					}
				}
			}
			
			incrementer++;
		}
	}
	
	public void RandomizeEnemies() {
		for(int i = 0; i < Rooms.length; i++) {
			if(Rooms[i] != null) {
				Rooms[i].enemies = (int)(random.nextInt(difficulty/2));
				Rooms[i].dungeonName = name + i;
				
				Rooms[i].enemyClasses = new Enemy[Rooms[i].enemies];
			}
		}
	}
	
	public void createEnemies() {
		for(int i = 0; i < Rooms.length; i++) {
			if(Rooms[i] != null) {
				for(int c = 0; c < Rooms[i].enemyClasses.length; c++) {
					if(Rooms[i].enemyClasses[c] == null) {
						Rooms[i].enemyClasses[c] = new Enemy(random.nextInt(32*29)+64,random.nextInt(32*14)+64,32,64,(int)difficulty/2,"skeleton",recommendedLevel,Rooms[i].dungeonName);
						Rooms[i].enemyClasses[c].x = random.nextInt(32*29)+64;
						Rooms[i].enemyClasses[c].y = random.nextInt(32*13)+64;
						Rooms[i].enemyClasses[c].location = Rooms[i].dungeonName;
					}
				}
			}
		}
	}
	
	public void Sort() {
		for(int i = 0; i < Rooms.length; i++) {
			for(int c = 0; c < Rooms[i].adjacentRooms.length; c++) {
				if(Rooms[i].adjacentRooms[c] == null) {
					if(i < Rooms.length-1) {
						positions[(int)(positions.length/2)] = Rooms;
						Rooms[0].x = 0;
						Rooms[0].y = 0;
							
						int randRoom = random.nextInt(4);
									
						if(Rooms[i].adjacentRooms[randRoom] == null) {
								
							Rooms[i].adjacentRooms[randRoom] = Rooms[i+1];
							
							if(randRoom <= 1) {
								Rooms[i+1].adjacentRooms[randRoom+2] = Rooms[i];
							}
							else {
								Rooms[i+1].adjacentRooms[randRoom-2] = Rooms[i];
							}
							
							if(randRoom == 0) {
								Rooms[i+1].x = Rooms[i].x-1;
								Rooms[i+1].y = Rooms[i].y;
							}
							else {
								if(randRoom == 1) {
									Rooms[i+1].y = Rooms[i].y+1;
									Rooms[i+1].x = Rooms[i].x;
								}
								else {
									if(randRoom == 2) {
										Rooms[i+1].x = Rooms[i].x+1;
										Rooms[i+1].y = Rooms[i].y;
									}
									else {
										if(randRoom == 3) {
											Rooms[i+1].y = Rooms[i].y-1;
											Rooms[i+1].x = Rooms[i].x;
										}
									}
								}
							}
							
							SamePosition(Rooms[i]);
						}
						else {
							randRoom++;
						} 
						
						break;
					}
				}
			}
		}
	}
	
	public Room inRoom(int x,int y) {
		for(int i = 0; i < Rooms.length; i++) {
			if(Rooms[i] != null) {
				if(Rooms[i].x == x && Rooms[i].y == y) {
					return Rooms[i];
				}
			}
			else {
				return null;
			}
		}
		return null;
	}
	
	public void Generate() {
		SetRandom();
		CreateRooms();
		Sort();
		
		roomCounter = 0;
		
		for(int i = 0; i < Rooms.length; i++) {
			DeleteExtras(Rooms[i]);
		}
		
		for(int i = 0; i < Rooms.length; i++) {
			ConnectRooms(Rooms[i]);
		}
		
		for(int i = 0; i < Rooms.length; i++) {
			ConnectFarRooms(Rooms[i]);
		}
		
		RandomizeEnemies();
		createEnemies();
		
		Rooms[0].roomVariation = Rooms[0].entrance.guy;
		
		for(int i = 0; i < Rooms.length; i++) {
			if(Rooms[i] != null) {
				roomCounter++;
			}
		}
		
		if(roomCounter <= 3) {
			Generate();
			Rooms[0].roomVariation = Rooms[0].entrance.guy;
		}
	}
}
