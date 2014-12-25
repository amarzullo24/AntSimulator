package com.antSimulator.logic;

import java.awt.Point;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class World {


	public static final int NUMBANT=20;
	public static final int WIDTH=300;
	public static final int HEIGHT=300;
	private Cell[][] matrix;
	private boolean[][] lockedCell;
	private BlockingQueue<Ant> ants;
	private Point nest;
	private int nestlevel;
	private Point food;
	private int width;
	private int height;

	public World() {

		loadWorld();
		spawnAnts();

	}

	private void spawnAnts() {
		ants = new ArrayBlockingQueue<Ant>(100);

		for (int i = 0; i < NUMBANT; i++)
			try {
				Ant a=new Ant(nestlevel, nest,i+1);
				ants.put(a);
				matrix[nest.x][nest.y].setA(a);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

	private void loadWorld() {
		setWidth(300);
		height = 300;
		matrix=new Cell[getWidth()][height];
		lockedCell=new boolean[width][height];
		initWorld();

		nest = new Point(5, 5);
		nestlevel = getWorld()[5][5].getG().getLevel();
		food = new Point(400, 400);

	}

	private void initWorld() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				matrix[i][j]=new Cell(i, j,new Random().nextInt(GroundState.MAXLEVEL + 1));
				lockedCell[i][j]=false;
			}
		}


	}

	public Cell[][] getWorld() {
		return matrix;
	}

	public void setWorld(Cell[][] world) {
		this.matrix = world;
	}


	public Cell getAvailableCell(int xPos, int yPos) {

		if (xPos < 0 || xPos >= getWidth() || yPos < 0 || yPos >= height)
			return null;
		else if (matrix[xPos][yPos].getG().getLevel() == GroundState.MAXLEVEL)
			return null;
		else
			return matrix[xPos][yPos];
	}

	public Cell getCell(int xPos, int yPos) {
		
		if (xPos < 0 || xPos >= getWidth() || yPos < 0 || yPos >= height)
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			lockedCell[x][y]=true;
			lockedCell[nx][ny]=true;

		}
		finally{
			Manager.getInstance().lock.unlock();
		}


	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public BlockingQueue<Ant> getAnts() {
		return ants;
	}

	public void setAnts(BlockingQueue<Ant> ants) {
		this.ants = ants;
	}

}
