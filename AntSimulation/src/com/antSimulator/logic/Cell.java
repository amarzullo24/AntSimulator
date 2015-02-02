package com.antSimulator.logic;

public class Cell {

	public static final int MAX_FOOD = 1000;
	public static final int ANT_CAPACITY = 10;

	private GroundState groundState;
	private int numberOfAnts;
	private int food;
	private int x;
	private int y;
	public boolean onFire = false;

	public Cell(int x, int y, int level) {
		this.setX(x);
		this.setY(y);
		food = 0;
		numberOfAnts = 0;
		groundState = new GroundState(level);
	}

	public int increaseGroundLevel() {
		return groundState.increaseLevel();
	}

	public void decreaseCellPheromones(int decrease) {
		groundState.decreasePh(decrease);
	}

	public void insertAntInArray() {

		numberOfAnts++;
	}

	public void removeAntfromArray() {
		if (numberOfAnts > 0)
			numberOfAnts--;
	}

	public GroundState getGroundState() {
		return groundState;
	}

	public void setGroundState(GroundState g) {
		this.groundState = g;
	}

	public void insertFood() {
		food = MAX_FOOD;
	}

	public void removeFood() {
		food = 0;
	}

	public void decreaseFood() {
		if (food > 0)
			food -= ANT_CAPACITY;
		else {
			food = 0;
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getFood() {
		return food;
	}

	public void setFood(int food) {
		this.food = food;
	}

	public int getNumberOfAnts() {
		return numberOfAnts;
	}

	public void setNumberOfAnts(int numberOfAnts) {
		this.numberOfAnts = numberOfAnts;
	}

	public void resetPheromones(float currentPhFound, float currentPhSearch) {
		groundState.setFoundPhLevel(currentPhFound);
		groundState.setSearchPhLevel(currentPhSearch);
		
	}

	

}
