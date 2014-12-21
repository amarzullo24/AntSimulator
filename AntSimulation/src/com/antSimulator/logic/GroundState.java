package com.antSimulator.logic;

public class GroundState {

	public static final int MAXLEVEL = 10;
	private int level;
	private float phLevel;

	private float constPh=(float) 1.5;
	
	public GroundState(int l) {
		setLevel(l);
		setPhLevel(0);
	}
	
	public void increasePh(){
		setPhLevel(getPhLevel() + constPh);
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public float getPhLevel() {
		return phLevel;
	}

	public void setPhLevel(float phLevel) {
		this.phLevel = phLevel;
	}
}
