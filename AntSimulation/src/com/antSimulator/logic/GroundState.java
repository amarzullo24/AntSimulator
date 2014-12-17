package com.antSimulator.logic;

public class GroundState {

	private int level;
	private float phLevel;

	private float constPh=(float) 1.5;
	
	public GroundState(int l) {
		setLevel(l);
		phLevel=0;
	}
	
	public void increasePh(){
		phLevel+=constPh;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
