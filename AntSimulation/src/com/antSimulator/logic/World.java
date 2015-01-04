package com.antSimulator.logic;

import java.awt.Point;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class World {


	public static final int NUM_OF_ANTS = 5;
	public static final int WIDTH = 200;
	public static final int HEIGHT = 200;
	private Cell[][] matrix;
	private boolean[][] lockedCell;
	private BlockingQueue<Ant> ants;
	private Point nest;
	private int nestlevel;
	private Point food;

	public World() {

		loadWorld();
		spawnAnts();

	}

	private void spawnAnts() {
		ants = new ArrayBlockingQueue<Ant>(NUM_OF_ANTS + 1);

		for (int i = 0; i < NUM_OF_ANTS; i++)
			try {
				
				Ant a=new Ant(nestlevel, (Point) nest.clone(),i+1);
				ants.put(a);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

	}

	private void loadWorld() {
		
		matrix=new Cell[WIDTH][HEIGHT];
		lockedCell=new boolean[WIDTH][HEIGHT];
		initWorld();

		nest = new Point(5, 5);
		nestlevel = getWorld()[5][5].getG().getLevel();
		food = new Point(400, 400);

	}

	private void initWorld() {
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				matrix[i][j]=new Cell(i, j,new Random().nextInt(GroundState.MAXLEVEL));
				lockedCell[i][j]=false;
			}
		}
		
		
		for (int k = 0; k < 5; k++) {
			
			int i = new Random().nextInt(HEIGHT);
			int j = new Random().nextInt(WIDTH);
			matrix[i][j] = new Cell(i, j, GroundState.MAXLEVEL);
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
		else if (matrix[xPos][yPos].getG().getLevel() == GroundState.MAXLEVEL)
			return null;
		else if (matrix[xPos][yPos].getA() != null)
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
	public void unlockCell(int x,int y, int nx, int ny){
		Manager.getInstance().lock.lock();
		try{

			lockedCell[x][y] = false;
			lockedCell[nx][ny] = false;

			Manager.getInstance().condition.signalAll();

		}
		finally{
			Manager.getInstance().lock.unlock();
		}
	}



	public void lockCell(int x,int y, int nx, int ny){

		Manager.getInstance().lock.lock();
		try{

			while(lockedCell[x][y] && lockedCell[nx][ny])
				try {
					
					Manager.getInstance().condition.signalAll();
					Manager.getInstance().condition.await();

				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}

			lockedCell[x][y]=true;
			lockedCell[nx][ny]=true;

		}
		finally{
			Manager.getInstance().lock.unlock();
		}


	}

	public BlockingQueue<Ant> getAnts() {
		return ants;
	}

	public void setAnts(BlockingQueue<Ant> ants) {
		this.ants = ants;
	}
	
	public void setCell(Cell cell){
		this.matrix[cell.getX()][cell.getY()] = cell;
	}
	

}
