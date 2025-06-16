package com.game.objects;

public class Location {
	public String name;
	public String[] destinations;
	
	public Location(String name,String dest1,String dest2,String dest3,String dest4) {
		this.name = name;
		
		destinations = new String[] {
				dest1,dest2,dest3,dest4
		};
	}
}
