package com.antSimulator.logic;

import java.awt.Point;
import java.util.Random;

public class Ant {

	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	
	public static final int SEARCH = 5;
	public static final int FOUND = 6;
	public static final int PHRELEASE = 7;
	
	private int direction;
	private int antState;
	private int level;
	private Point position;
	private String name;
	
	private int currentDirection = new Random().nextInt(4);
 
	public Ant(int nestLevel, Point pos, int num) {
		setDirection(-1);
		setAntState(SEARCH);
		setLevel(nestLevel);
		position=pos;
		name="Ant".concat(String.valueOf(num));
		
	}
	
	public Ant(Ant ant){
		
		this.direction = ant.direction;
		this.antState = ant.antState;
		this.setLevel(ant.getLevel());
		this.position = ant.position;
		this.name = ant.name;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	public void setXPos(int x){
		position.x=x;
		
	}
	
	public void setYPos(int y){
		position.y=y;
	}
	
	public double getXPos(){
		return position.getX();
	}
	
	public double getYPos(){
		return position.getY();
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getAntState() {
		return antState;
	}

	public void setAntState(int antState) {
		this.antState = antState;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCurrentDirection() {
		return currentDirection;
	}

	public void setCurrentDirection(int currentDirection) {
		this.currentDirection = currentDirection;
	}
	
}
