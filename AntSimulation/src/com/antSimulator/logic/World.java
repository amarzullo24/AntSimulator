package com.antSimulator.logic;

import java.awt.Point;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class World {

	private Cell[][] world;
	private BlockingQueue<Ant> ants;
	private Point nest;
	private int nestlevel;
	private Point food;

	public World() {
		loadWorld();
		spawnAnts();

	}

	private void spawnAnts() {
		ants = new ArrayBlockingQueue<Ant>(100);
		
		for (int i = 0; i < ants.size(); i++)
			ants.add(new Ant(nestlevel));

	}

	private void loadWorld() {
		world = new Cell[1000][1000];
		nest=new Point(5,5);
		nestlevel=world[5][5].getG().getLevel();
		food=new Point(400,400);

	}

}
