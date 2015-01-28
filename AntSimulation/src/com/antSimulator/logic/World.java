package com.antSimulator.logic;

import java.awt.Point;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class World {

	public static int NUM_OF_ANTS = 10;
	public static final short MAX_NUM_OF_ANT = 100;
	public static final int WIDTH = 100;
	public static final int HEIGHT = 100;
	public static final int FOOD_WIDTH = 5;
	public static final int FOOD_HEIGHT = 5;
	public static final int NEST_WIDTH = 6;
	public static final int NEST_HEIGHT = 6;
	public static final short MAX_PH_LEVEL = 1000;

	private Cell[][] matrix;
	private boolean[][] lockedCell;
	private BlockingQueue<Ant> ants;
	private Point nest;
	private int nestlevel;

	// private ArrayList<Point> food;

	public World() {

		loadWorld();
		spawnAnts();

	}

	private void spawnAnts() {
		ants = new ArrayBlockingQueue<Ant>(MAX_NUM_OF_ANT + 1);

		for (int i = 0; i < NUM_OF_ANTS; i++)
			try {

				Ant a = new Ant(nestlevel, (Point) nest.clone(), i + 1);
				ants.put(a);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

	}

	public void respawnAnts() {

		Manager.getInstance().lock.lock();

		if (ants.size() < NUM_OF_ANTS) {
			for (int i = ants.size(); i < NUM_OF_ANTS; i++)
				try {

					Ant a = new Ant(nestlevel, (Point) nest.clone(), i + 1);
					ants.put(a);

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		} else {

			while (ants.size() != NUM_OF_ANTS) {

				Ant a = ants.remove();
				this.matrix[a.getXPos()][a.getYPos()].removeAntfromArray(a);
			}

		}

		Manager.getInstance().lock.unlock();

	}

	private void loadWorld() {

		matrix = new Cell[WIDTH][HEIGHT];
		lockedCell = new boolean[WIDTH][HEIGHT];
		initWorld();

		nest = new Point(10, 15);
		initFood();
		// food = new ArrayList<Point>();
		// food.add(new Point(45, 20));
		// food.add(new Point(10, 40));

		nestlevel = getWorld()[nest.x][nest.y].getGroundState().getLevel();

	}

	private void initFood() {
		for (int i = 0; i < FOOD_WIDTH; i++)
			for (int j = 0; j < FOOD_HEIGHT; j++)
				matrix[45+i][20+j].insertFood();

	}

	private void initWorld() {
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				int newVal = 0;

				// int random = new Random().nextInt(2);
				// if (i - 1 >= 0) {
				// if (random == 0)
				// newVal = matrix[i - 1][j].getGroundState().getLevel() - 1;
				// else
				// newVal = matrix[i - 1][j].getGroundState().getLevel() + 1;
				//
				// if (newVal < 0)
				// newVal = 0;
				// if (newVal > GroundState.MAXLEVEL)
				// newVal--;
				// }

				matrix[i][j] = new Cell(i, j, newVal);
				lockedCell[i][j] = false;
			}
		}

		for (int k = 0; k < 5; k++) {

			int i = new Random().nextInt(HEIGHT);
			int j = new Random().nextInt(WIDTH);
			matrix[i][j].getGroundState().setLevel(GroundState.MAXLEVEL);
		}

	}

	public Cell[][] getWorld() {
		return matrix;
	}

	public void setWorld(Cell[][] world) {
		this.matrix = world;
	}

	public Cell getAvailableCell(int xPos, int yPos) {

		if (xPos < 0 || xPos >= WIDTH || yPos < 0 || yPos >= HEIGHT)
			return null;
		else if (matrix[xPos][yPos].getGroundState().getLevel() == GroundState.MAXLEVEL)
			return null;

		else
			return matrix[xPos][yPos];
	}

	public Cell getCell(int xPos, int yPos) {

		if (xPos < 0 || xPos >= WIDTH || yPos < 0 || yPos >= HEIGHT)
			return null;
		else
			return matrix[xPos][yPos];
	}

	public boolean[][] getLockedCell() {
		return lockedCell;
	}

	public void setLockedCell(boolean[][] lockedCell) {
		this.lockedCell = lockedCell;
	}

	public void unlockCell(int x, int y, int nx, int ny) {
		Manager.getInstance().lock.lock();
		try {

			lockedCell[x][y] = false;
			lockedCell[nx][ny] = false;

			Manager.getInstance().condition.signalAll();

		} finally {
			Manager.getInstance().lock.unlock();
		}
	}

	public void lockCell(int x, int y, int nx, int ny) {

		Manager.getInstance().lock.lock();
		try {

			while (lockedCell[x][y] || lockedCell[nx][ny])
				try {

					Manager.getInstance().condition.signalAll();
					Manager.getInstance().condition.await();

				} catch (InterruptedException e) {

					e.printStackTrace();
				}

			lockedCell[x][y] = true;
			lockedCell[nx][ny] = true;

		} finally {
			Manager.getInstance().lock.unlock();
		}

	}

	public BlockingQueue<Ant> getAnts() {
		return ants;
	}

	public void setAnts(BlockingQueue<Ant> ants) {
		this.ants = ants;
	}

	public void setCell(Cell cell) {
		this.matrix[cell.getX()][cell.getY()] = cell;
	}

	public Point getNest() {
		return nest;
	}

	public void setNest(Point nest) {
		this.nest = nest;
	}

}
