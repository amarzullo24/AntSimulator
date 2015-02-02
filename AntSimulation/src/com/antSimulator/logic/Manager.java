package com.antSimulator.logic;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Manager {

	public static int PHREDUCTION = 10;
	public static int DEFAULT_PH_REDUCTION = 10;
	public static int UPDATE_TIME = 100;

	public static final int CORE_NUMBER = 3;
	public static boolean ISACTIVE = true;
	public static final int SLEEP_TIME = 50;

	public boolean updating;

	public World world;
	public Lock lock = new ReentrantLock();
	public Condition condition = lock.newCondition();
	private static Manager instance;

	public static short TOTAL_TIME = 0;
	public static short TOTAL_ANTS_TO_NEST = 1;
	public static short LAST_ANT_TO_NEST = 0;

	public static int TOTAL_FOOD = World.FOOD_WIDTH * World.FOOD_HEIGHT * Cell.MAX_FOOD;
	public static short NESTED_FOOD = 0;
	
	public static int GROUND_RADIOUS = 2;
	public static boolean ITS_RAINING = false;

	public static Manager getInstance() {

		if (instance == null)
			instance = new Manager();

		return instance;
	}

	public void start() {
		updating = false;
		world = loadWorld();
		new UpdateThread().start();
	}

	public World loadWorld() {
		return new World();

	}

	public void moveAnts(Ant a) {
		ArrayList<Integer> erasedDirection = new ArrayList<Integer>();
		erasedDirection.add(backDirection(a));
		a.nextStep();

		if (a.getStep_Ant() == Ant.MAXSTEPSAMEDIR) {
			a.setStep_Ant(0);
			erasedDirection.add(a.getCurrentDirection());
		}

		boolean check = false;
		int k = a.getCurrentDirection();
		Cell currentChoise = null;
		while (!check && !(erasedDirection.size() == 4)) {
			if (a.getAntState() == Ant.SEARCH) {
				if (!nearFood(a))
					k = chooseSearchDirection(a, erasedDirection);
			} else if (a.getAntState() == Ant.FOUND) {
				if (!nearNest(a))
					k = chooseFoundDirection(a, erasedDirection);

			}
			switch (a.getCurrentDirection()) {
			case Ant.UP:

				currentChoise = world.getAvailableCell((int) a.getXPos(),
						(int) a.getYPos() - 1);
				break;
			case Ant.DOWN:

				currentChoise = world.getAvailableCell((int) a.getXPos(),
						(int) a.getYPos() + 1);
				break;

			case Ant.LEFT:

				currentChoise = world.getAvailableCell((int) a.getXPos() - 1,
						(int) a.getYPos());
				break;
			case Ant.RIGHT:

				currentChoise = world.getAvailableCell((int) a.getXPos() + 1,
						(int) a.getYPos());
				break;
			default:
				break;
			}

			check = choose(currentChoise, a);
			if (!check)
				erasedDirection.add(k);

		}
	}

	private boolean nearNest(Ant a) {

		if (nested(a.getXPos() + 1, a.getYPos())) {
			a.setCurrentDirection(Ant.RIGHT);
			return true;
		}

		if (nested(a.getXPos() - 1, a.getYPos())) {
			a.setCurrentDirection(Ant.LEFT);
			return true;
		}
		if (nested(a.getXPos(), a.getYPos() + 1)) {
			a.setCurrentDirection(Ant.DOWN);
			return true;
		}
		if (nested(a.getXPos(), a.getYPos() - 1)) {
			a.setCurrentDirection(Ant.UP);
			return true;
		}
		return false;
	}

	private boolean nearFood(Ant a) {
		if (checkFood(a.getXPos() + 1, a.getYPos())) {
			a.setCurrentDirection(Ant.RIGHT);
			return true;
		}
		if (checkFood(a.getXPos() - 1, a.getYPos())) {
			a.setCurrentDirection(Ant.LEFT);
			return true;
		}
		if (checkFood(a.getXPos(), a.getYPos() + 1)) {
			a.setCurrentDirection(Ant.DOWN);
			return true;
		}

		if (checkFood(a.getXPos(), a.getYPos() - 1)) {
			a.setCurrentDirection(Ant.UP);
			return true;
		}

		return false;
	}

	private boolean checkFood(int dx, int dy) {
		boolean checkFood = false;
		if (world.getAvailableCell(dx, dy) != null)
			checkFood = (world.getCell(dx, dy).getFood() > 0);
		return checkFood;
	}

	private int chooseFoundDirection(Ant a, ArrayList<Integer> erasedDirection) {
		int dir;
		int searchPh;

		if (!contains(Ant.UP, erasedDirection)) {
			dir = Ant.UP;
			searchPh = getSearchPh(a.getXPos(), a.getYPos() - 1);

		} else {
			dir = Ant.RIGHT;
			searchPh = getSearchPh(a.getXPos() + 1, a.getYPos());
		}

		if (!contains(Ant.RIGHT, erasedDirection)
				&& searchPh < getSearchPh(a.getXPos() + 1, a.getYPos())) {
			searchPh = getSearchPh(a.getXPos() + 1, a.getYPos());
			dir = Ant.RIGHT;
		}
		if (!contains(Ant.LEFT, erasedDirection)
				&& searchPh < getSearchPh(a.getXPos() - 1, a.getYPos())) {
			searchPh = getSearchPh(a.getXPos() - 1, a.getYPos());
			dir = Ant.LEFT;
		}
		if (!contains(Ant.DOWN, erasedDirection)
				&& searchPh < getSearchPh(a.getXPos(), a.getYPos() + 1)) {
			searchPh = getSearchPh(a.getXPos(), a.getYPos() + 1);
			dir = Ant.DOWN;
		}
		if (searchPh == 0 && contains(a.getCurrentDirection(), erasedDirection)) {
			dir = backDirection(a);
			while (contains(dir, erasedDirection))
				dir = new Random().nextInt(4);
			a.setCurrentDirection(dir);
			return dir;
		}
		switch (a.getCurrentDirection()) {
		case Ant.UP:
			if (searchPh == getSearchPh(a.getXPos(), a.getYPos() - 1))
				return Ant.UP;
			break;
		case Ant.RIGHT:
			if (searchPh == getSearchPh(a.getXPos() + 1, a.getYPos()))
				return Ant.RIGHT;
			break;
		case Ant.LEFT:
			if (searchPh == getSearchPh(a.getXPos() - 1, a.getYPos()))
				return Ant.LEFT;
			break;
		case Ant.DOWN:
			if (searchPh == getSearchPh(a.getXPos(), a.getYPos() + 1))
				return Ant.DOWN;
			break;
		default:
			break;
		}

		a.setCurrentDirection(dir);

		return dir;

	}

	// ahahahah XD
	private boolean contains(int dir, ArrayList<Integer> erasedDirection) {
		boolean True = false;
		for (Integer i : erasedDirection) {
			if (dir == i)
				return true;

		}
		return True;
	}

	private int chooseSearchDirection(Ant a, ArrayList<Integer> erasedDirection) {
		int dir;
		int foundPh;

		if (!contains(Ant.UP, erasedDirection)) {
			dir = Ant.UP;
			foundPh = getFoundPh(a.getXPos(), a.getYPos() - 1);

		} else {
			dir = Ant.RIGHT;
			foundPh = getFoundPh(a.getXPos() + 1, a.getYPos());
		}

		if (!contains(Ant.RIGHT, erasedDirection)
				&& foundPh < getFoundPh(a.getXPos() + 1, a.getYPos())) {
			foundPh = getFoundPh(a.getXPos() + 1, a.getYPos());
			dir = Ant.RIGHT;
		}
		if (!contains(Ant.LEFT, erasedDirection)
				&& foundPh < getFoundPh(a.getXPos() - 1, a.getYPos())) {
			foundPh = getFoundPh(a.getXPos() - 1, a.getYPos());
			dir = Ant.LEFT;
		}
		if (!contains(Ant.DOWN, erasedDirection)
				&& foundPh < getFoundPh(a.getXPos(), a.getYPos() + 1)) {
			foundPh = getFoundPh(a.getXPos(), a.getYPos() + 1);
			dir = Ant.DOWN;
		}

		if (foundPh == 00 && contains(a.getCurrentDirection(), erasedDirection)) {
			dir = backDirection(a);
			while (contains(dir, erasedDirection))
				dir = new Random().nextInt(4);
			a.setCurrentDirection(dir);
			return dir;
		}
		switch (a.getCurrentDirection()) {
		case Ant.UP:
			if (foundPh == getFoundPh(a.getXPos(), a.getYPos() - 1))
				return Ant.UP;
			break;
		case Ant.RIGHT:
			if (foundPh == getFoundPh(a.getXPos() + 1, a.getYPos()))
				return Ant.RIGHT;
			break;
		case Ant.LEFT:
			if (foundPh == getFoundPh(a.getXPos() - 1, a.getYPos()))
				return Ant.LEFT;
			break;
		case Ant.DOWN:
			if (foundPh == getFoundPh(a.getXPos(), a.getYPos() + 1))
				return Ant.DOWN;
			break;
		default:
			break;
		}

		a.setCurrentDirection(dir);

		return dir;
	}

	private int backDirection(Ant a) {
		switch (a.getCurrentDirection()) {
		case Ant.UP:

			return Ant.DOWN;

		case Ant.RIGHT:

			return Ant.LEFT;

		case Ant.LEFT:
			return Ant.RIGHT;

		case Ant.DOWN:

			return Ant.UP;

		default:
			break;
		}
		return -1;
	}

	private int getSearchPh(int dx, int dy) {
		int ph = 0;
		if (world.getCell(dx, dy) != null)
			ph = (int) world.getCell(dx, dy).getGroundState()
					.getSearchPhLevel();

		return ph;
	}

	private int getFoundPh(int dx, int dy) {
		int ph = 0;
		if (world.getAvailableCell(dx, dy) != null)
			ph = (int) world.getCell(dx, dy).getGroundState().getFoundPhLevel();

		return ph;
	}

	private boolean choose(Cell currentChoise, Ant a) {

		boolean check = false;
		if (currentChoise == null)
			return check;

		if (currentChoise != null) {
			check = true;
			transaction(a, currentChoise);
		}

		return check;
	}

	private void transaction(Ant a, Cell oldWhereGo) {

		a.stepOfRoundtrip++;
		if(a.onFire)
			a.onFire_cont--;
		
		Cell current = world.getCellToDraw(a.getXPos(), a.getYPos());
		Cell currentLogicMatrix = world.getCell(a.getXPos(), a.getYPos());
		
		Cell whereGo = world.getCellToDraw(oldWhereGo.getX(), oldWhereGo.getY());
		if (a.getLevel() == whereGo.getGroundState().getLevel()) {

			a.setXPos(whereGo.getX());
			a.setYPos(whereGo.getY());

			
			if (a.getAntState() == Ant.SEARCH) {
				whereGo.getGroundState().increaseSearchPh(a);
				//diffusePheromones(current.getGroundState(), a);
				a.releasePheromones();

				if (whereGo.getFood() > 0) {
					a.setMaxPheromones();
					a.setStep_Ant(0);
					a.setAntState(Ant.FOUND);
					whereGo.decreaseFood();
					a.setCurrentDirection(backDirection(a));
					a.restartPhRelease();
					TOTAL_FOOD -= Cell.ANT_CAPACITY;
				}
			} else if (a.getAntState() == Ant.FOUND) {
				whereGo.getGroundState().increaseFoundPh(a);
				//diffusePheromones(current.getGroundState(), a);
				a.releasePheromones();
				if (nested(a.getXPos(), a.getYPos())) {
					a.setMaxPheromones();
					a.setStep_Ant(0);
					a.setAntState(Ant.SEARCH);
					a.setCurrentDirection(backDirection(a));
					a.restartPhRelease();
					NESTED_FOOD += Cell.ANT_CAPACITY;
					TOTAL_TIME += a.stepOfRoundtrip;
					TOTAL_ANTS_TO_NEST++;
					LAST_ANT_TO_NEST = a.stepOfRoundtrip;
					a.stepOfRoundtrip = 0;
					Observer.getIstance().update();
				}

			}
			
			current.removeAntfromArray();
			

			
			if(whereGo.onFire)
				a.onFire = true;
			else if(a.onFire && a.onFire_cont > 0)
				whereGo.onFire = true;
			
			if(current.getNumberOfAnts() <= 0)
				current.onFire = false;
			
			if((a.onFire_cont > 0 && a.onFire) || !a.onFire)
				whereGo.insertAntInArray();

		} else if (a.getLevel() < whereGo.getGroundState().getLevel())
			a.setLevel(a.getLevel() + 1);
		else
			a.setLevel(a.getLevel() - 1);

	}

	private boolean nested(int x, int y) {
		Nest nest = world.getNest();
		if (nest.getxPos() + World.NEST_WIDTH > x && nest.getxPos() < x
				&& nest.getyPos() < y && nest.getyPos() + World.NEST_HEIGHT > y)
			return true;
		return false;
	}

	public void update() {

		lock.lock();
		updating = true;
		for (int i = 0; i < World.WIDTH; i++) {
			for (int j = 0; j < World.HEIGHT; j++) {
				
				float currentPhFound = world.getCell(i, j).getGroundState().getFoundPhLevel();
				float currentPhSearch = world.getCell(i, j).getGroundState().getSearchPhLevel();
				int currentFood = world.getCell(i, j).getFood();
				int currentGround = world.getCell(i, j).getGroundState().getLevel();
				boolean onFire = world.getCell(i, j).onFire;
				
				world.getCellToDraw(i, j).resetPheromones(currentPhFound,currentPhSearch);
				world.getCellToDraw(i, j).setNumberOfAnts(world.getCell(i, j).getNumberOfAnts());
				world.getCellToDraw(i, j).decreaseCellPheromones(PHREDUCTION);
				world.getCellToDraw(i, j).setFood(currentFood);
				world.getCellToDraw(i, j).getGroundState().setLevel(currentGround);
				world.getCellToDraw(i, j).onFire = onFire;
			}

		}
		
		ArrayList<Integer> index = new ArrayList<Integer>();
		
		int ik = 0;
		for (Ant a : world.getNest().getAnts()) {
			moveAnts(a);
			if(a.onFire && a.onFire_cont<=0)
				index.add(ik);
			ik++;		
		}
		
		world.nest.removeAntsUsingXY(index);
		world.setUpdated(!world.isUpdated());
		updating = false;
		condition.signalAll();
		lock.unlock();

	}

	private void diffusePheromones(GroundState groundState, Ant a) {
		ArrayList<Cell> neighbour = getNeighbour(a.getXPos(), a.getYPos());
		for (Cell c : neighbour) {
			if (a.getAntState() == Ant.FOUND)
				c.getGroundState().increaseNeigFoundPh(a);
			else
				c.getGroundState().increaseNeigSearchPh(a);
		}

	}

	private ArrayList<Cell> getNeighbour(int xPos, int yPos) {
		ArrayList<Cell> neig = new ArrayList<Cell>();
		if (world.getAvailableCell(xPos + 1, yPos) != null) {
			neig.add(world.getCell(xPos + 1, yPos));
		}
		if (world.getAvailableCell(xPos - 1, yPos) != null) {
			neig.add(world.getCell(xPos - 1, yPos));
		}
		if (world.getAvailableCell(xPos + 1, yPos - 1) != null) {
			neig.add(world.getCell(xPos + 1, yPos - 1));
		}
		if (world.getAvailableCell(xPos - 1, yPos - 1) != null) {
			neig.add(world.getCell(xPos - 1, yPos - 1));
		}
		if (world.getAvailableCell(xPos + 1, yPos + 1) != null) {
			neig.add(world.getCell(xPos + 1, yPos + 1));
		}
		if (world.getAvailableCell(xPos - 1, yPos + 1) != null) {
			neig.add(world.getCell(xPos - 1, yPos + 1));
		}
		if (world.getAvailableCell(xPos, yPos + 1) != null) {
			neig.add(world.getCell(xPos, yPos + 1));
		}
		if (world.getAvailableCell(xPos, yPos - 1) != null) {
			neig.add(world.getCell(xPos, yPos - 1));
		}
		return neig;
	}

	private class UpdateThread extends Thread {

		@Override
		public void run() {
			while (ISACTIVE) {
				Manager.getInstance().update();

				try {
					Thread.sleep(UPDATE_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}
	}
}
