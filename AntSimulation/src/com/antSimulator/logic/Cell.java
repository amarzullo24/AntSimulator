package com.antSimulator.logic;

import java.util.HashMap;

public class Cell {

	public static final int MaxFood = 100;
	public static final int AntCapacity = 10;

	private HashMap<String, Ant> antsSet;
	private GroundState groundState;
	private int food;
	private int x;
	private int y;

	public Cell(int x, int y, int level) {
		this.setX(x);
		this.setY(y);
		food = 0;
		antsSet = new HashMap<String, Ant>();
		groundState = new GroundState(level);
	}

	public void decreaseCellPheromones(int decrease) {
		groundState.decreasePh(decrease);
	}

	public HashMap<String, Ant> getAntsSet() {
		return antsSet;
	}

	public void setA(HashMap<String, Ant> a) {
		this.antsSet = a;
	}

	public void insertAntInArray(Ant ant) {

		this.antsSet.put(ant.getName(), ant);
	}

	public void removeAntfromArray(Ant ant) {

		this.antsSet.remove(ant.getName());
	}

	public GroundState getGroundState() {
		return groundState;
	}

	public void setGroundState(GroundState g) {
		this.groundState = g;
	}

	public void insertFood() {
		food = MaxFood;
	}

	public void decreaseFood() {
		if (food > 0)
			food -= AntCapacity;
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

}
