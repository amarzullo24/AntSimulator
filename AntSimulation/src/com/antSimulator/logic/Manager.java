
package com.antSimulator.logic;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Manager {

	public static final int NUMBCORE = 2;

	public World world;
	Lock lock = new ReentrantLock();
	Condition condition = lock.newCondition();
	private static Manager instance;
	private ArrayList<Worker> workers = new ArrayList<Worker>();

	public static Manager getInstance() {

		if (instance == null)
			instance = new Manager();

		return instance;
	}

	public void start() {

		world = loadWorld();
		for (int i = 0; i < NUMBCORE; i++) {
			workers.add(new Worker(world.getAnts()));
			workers.get(i).start();
		}
	}

	public World loadWorld() {
		return new World();
	}

	public void moveAnt(Ant a) throws InterruptedException {

		ArrayList<Integer> dir = new ArrayList<Integer>();
		dir.add(Ant.DOWN);
		dir.add(Ant.UP);
		dir.add(Ant.LEFT);
		dir.add(Ant.RIGHT);
		boolean check = false;
		while (dir.size() != 0 && !check) {
			// TO-DO

			int k = new Random().nextInt(dir.size());
			Cell currentChoise = world.getCell((int) a.getXPos(),
					(int) a.getYPos());
			switch (dir.get(k)) {
			case Ant.UP:
				currentChoise=world.getCell((int) a.getXPos(), (int) a.getYPos() - 1);
				if ( currentChoise!= null)
					check = choose(0, -1, currentChoise, a);
				break;
			case Ant.DOWN:
				currentChoise=world.getCell((int) a.getXPos(), (int) a.getYPos() + 1);
				if ( currentChoise!= null)
					check = choose(0, 1, currentChoise, a);
				break;

			case Ant.LEFT:
				currentChoise=world.getCell((int) a.getXPos()-1, (int) a.getYPos());
				if ( currentChoise!= null)
					check = choose(-1, 0, currentChoise, a);
				break;

			case Ant.RIGHT:
				currentChoise=world.getCell((int) a.getXPos()+1, (int) a.getYPos());
				if ( currentChoise!= null)
					check = choose(1, 0, currentChoise, a);
				break;
			default:
				break;
			}

			dir.remove(k);
		}

	}

	private boolean choose(int dx, int dy, Cell currentChoise, Ant a) {

		boolean check = false;
		int x = (int) a.getXPos();
		int y = (int) a.getYPos();
		int nx = (int) a.getXPos() + dx;
		int ny = (int) a.getYPos() + dy;
		world.lockCell(x, y, nx, ny);

		currentChoise = world.getAvailableCell((int) a.getXPos() + dx,
				(int) (a.getXPos() + dy));
		if (currentChoise != null) {
			check = true;
			transaction(a, currentChoise);
		}

		world.unlockCell(x, y, nx, ny);

		return check;
	}

	private void transaction(Ant a, Cell currentChoise) {
		Cell current = world.getCell((int) a.getXPos(), (int) a.getYPos());
		if (currentChoise.getA() == null) {
			current.setA(null);
			if (a.getLevel() == currentChoise.getG().getLevel()) {
				currentChoise.setA(a);
				a.setXPos(currentChoise.getX());
				a.setYPos(currentChoise.getY());
				// if(a.getAntState()==Ant.FOUND)
				current.getG().setPhLevel(
						current.getG().getPhLevel() + Ant.PHRELEASE);
			} else if (a.getLevel() < currentChoise.getG().getLevel())
				a.setLevel(a.getLevel() + 1);
			else
				a.setLevel(a.getLevel() - 1);

		}

	}
}
