package com.antSimulator.logic;

import java.awt.Point;
import java.util.Random;

import javafx.geometry.VPos;

public class Ant {

	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	
	public static final int SEARCH = 5;
	public static final int FOUND = 6;
	public static final int FOUNDPHRELEASE = 12;
	public static final int SEARCHPHRELEASE= 8;
	
	public static final int MAXSTEPPHRELEASE=60;
	public static final int MAXSTEPSAMEDIR=10;
	
	private int direction;
	private int antState;
	private int level;
	private Point position;
	private String name;
	private int step_Ant=0;
	private Random r;
	private int currentDirection = new Random().nextInt(4);
	private int stepPhReleased;
	private boolean releasePh;
 
	public Ant(int nestLevel, Point pos, int num) {
		r=new Random();
		setDirection(r.nextInt(4));
		setAntState(SEARCH);
		setLevel(nestLevel);
		position=setPosition(pos);
		name="Ant".concat(String.valueOf(num));
		stepPhReleased=0;
		releasePh=true;
		
	}
	
	private Point setPosition(Point pos) {
		int x=new Random().nextInt(World.NEST_WIDTH);
		int y=new Random().nextInt(World.NEST_HEIGHT);
		
		return new Point(pos.x+x,pos.y+y);
	}

	public Ant(Ant ant){
		
		this.direction = ant.direction;
		this.antState = ant.antState;
		this.setLevel(ant.getLevel());
		this.position = ant.position;
		this.name = ant.name;
	}

	public void increaseStepPhRelease(){
		stepPhReleased++;
	}
	
	public void checkStepPhRelease(){
		if(stepPhReleased==MAXSTEPPHRELEASE){
			releasePh=false;
			
		}	
	}
	
	public void restartPhRelease(){
		releasePh=true;
		stepPhReleased=0;
	}
	public void nextStep(){
		step_Ant++;
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
	
	public int getXPos(){
		return position.x;
	}
	
	public int getYPos(){
		return position.y;
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

	public int getStep_Ant() {
		return step_Ant;
	}

	public void setStep_Ant(int step_Ant) {
		this.step_Ant = step_Ant;
	}

	public void nextDir() {
		
		setCurrentDirection(r.nextInt(4));
	}

	public int getStepPhReleased() {
		return stepPhReleased;
	}

	public void setStepPhReleased(int stepPhReleased) {
		this.stepPhReleased = stepPhReleased;
	}

	public boolean isReleasePh() {
		return releasePh;
	}

	public void setReleasePh(boolean releasePh) {
		this.releasePh = releasePh;
	}
	
}
