package com.antSimulator.logic;

import java.awt.Point;
import java.util.HashMap;
import java.util.Random;


public class Ant {

	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;

	public static final int SEARCH = 5;
	public static final int FOUND = 6;
	public static final int PHDESEASE = 10;
	public static final int MAXPHEROMONE = 500;

	public static final String RESEARCHPHEROMONE = "researchPheromone";
	public static final String FOUNDPHEROMONE = "foundPheromone";
	public static final int MAXSTEPSAMEDIR = 20;

	private int direction;
	private int antState;
	private int level;
	private Point position;
	private String name;
	private int step_Ant = 0;
	private Random r;
	private int currentDirection = new Random().nextInt(4);
	private HashMap<String, Pheromone> pheromones;

	public Ant(int nestLevel, Point pos, int num) {
		pheromones = new HashMap<String, Pheromone>();
		r = new Random();
		setDirection(r.nextInt(4));
		setAntState(SEARCH);
		setLevel(nestLevel);
		inizializePheromone();
		position = setPosition(pos);
		name = "Ant".concat(String.valueOf(num));

	}

	private void inizializePheromone() {
		pheromones.put(FOUNDPHEROMONE, new Pheromone(MAXPHEROMONE));
		pheromones.put(RESEARCHPHEROMONE, new Pheromone(MAXPHEROMONE));
	}

	private Point setPosition(Point pos) {
		int x = new Random().nextInt(World.NEST_WIDTH);
		int y = new Random().nextInt(World.NEST_HEIGHT);

		return new Point(pos.x + x, pos.y + y);
	}

	public Ant(Ant ant) {
        this.pheromones=ant.pheromones;
		this.direction = ant.direction;
		this.antState = ant.antState;
		this.setLevel(ant.getLevel());
		this.position = ant.position;
		this.name = ant.name;
	}

	public void releasePheromones(String typePh){
		pheromones.get(typePh).reduceQuantity(PHDESEASE);
	}

	public void restartPhRelease(String typePh) {
		pheromones.get(typePh).setQuantity(MAXPHEROMONE);
	}

	public void nextStep() {
		step_Ant++;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public void setXPos(int x) {
		position.x = x;

	}

	public void setYPos(int y) {
		position.y = y;
	}

	public int getXPos() {
		return position.x;
	}

	public int getYPos() {
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

	public HashMap<String, Pheromone> getPheromones() {
		return pheromones;
	}

	public void setPheromones(HashMap<String, Pheromone> pheromones) {
		this.pheromones = pheromones;
	}

}
