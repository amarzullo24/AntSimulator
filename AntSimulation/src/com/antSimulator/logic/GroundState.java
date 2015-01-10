package com.antSimulator.logic;

public class GroundState {

	public static final int MAXLEVEL = 2;
	private int level;
	private float foundPhLevel;
	private float searchPhLevel;


	
	public GroundState(int l) {
		setLevel(l);
		setFoundPhLevel(0);
		setSearchPhLevel(0);
		
	}
	
	public void increaseFoundPh(){
		setFoundPhLevel(getFoundPhLevel() + Ant.FOUNDPHRELEASE);
	}
	
	public void increaseSearchPh(){
		setSearchPhLevel(getSearchPhLevel()+Ant.SEARCHPHRELEASE);
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public float getFoundPhLevel() {
		return foundPhLevel;
	}

	public void setFoundPhLevel(float phLevel) {
		this.foundPhLevel = phLevel;
	}

	public float getSearchPhLevel() {
		return searchPhLevel;
	}

	public void setSearchPhLevel(float searchPhLevel) {
		this.searchPhLevel = searchPhLevel;
	}
}
