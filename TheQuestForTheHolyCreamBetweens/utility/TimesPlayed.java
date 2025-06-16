package com.utility;

public class TimesPlayed implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	public int times;
	
	public TimesPlayed() {
		times = 0;
	}
	
	public void updateData(int times) {
		this.times = times;
	}
}
