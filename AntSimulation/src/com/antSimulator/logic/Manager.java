package com.antSimulator.logic;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Manager {

	int cont_left = 0;
	int cont_r = 0;
	int cont_u = 0;
	int cont_d = 0;

	public static int PHREDUCTION = 50;
	public static int UPDATE_TIME = 200;

	public static final int CORE_NUMBER = 7;
	public static boolean ISACTIVE = true;
	public static final int SLEEP_TIME = 5;

	public boolean updating;

	public World world;
	public Lock lock = new ReentrantLock();
	public Condition condition = lock.newCondition();
	private static Manager instance;
	private ArrayList<Worker> workers = new ArrayList<Worker>();

	public static Manager getInstance() {

		if (instance == null)
			instance = new Manager();

		return instance;
	}

	public void start() {
		updating = false;
		world = loadWorld();
		for (int i = 0; i < CORE_NUMBER; i++) {
			workers.add(new Worker(world.getAnts()));
			workers.get(i).start();
		}
		new UpdateThread().start();
	}

	public World loadWorld() {
		return new World();

	}

	public void moveAnts(Ant a) {
		ArrayList<Integer> erasedDirection = new ArrayList<Integer>();
		erasedDirection.add(backDirection(a));

		a.nextStep();
		if (a.getStep_Ant() == 30) {
			a.setStep_Ant(0);
			erasedDirection.add(a.getCurrentDirection());
		}

		boolean check = false;
		int k = a.getCurrentDirection();
		Cell currentChoise = null;
		while (!check && !(erasedDirection.size() == 4)) {
			if (a.getAntState() == Ant.SEARCH) {
				k = chooseSearchDirection(a, erasedDirection);
			} else if (a.getAntState() == Ant.FOUND) {
				k = chooseFoundDirection(a, erasedDirection);

			}
			switch (k) {
			case Ant.UP:

				currentChoise = world.getCell((int) a.getXPos(),
						(int) a.getYPos() - 1);
				if (currentChoise != null) {
					check = choose(0, -1, currentChoise, a);
				}
				break;
			case Ant.DOWN:

				currentChoise = world.getCell((int) a.getXPos(),
						(int) a.getYPos() + 1);
				if (currentChoise != null) {
					check = choose(0, 1, currentChoise, a);

				}
				break;

			case Ant.LEFT:

				currentChoise = world.getCell((int) a.getXPos() - 1,
						(int) a.getYPos());
				if (currentChoise != null)
					check = choose(-1, 0, currentChoise, a);

				break;

			case Ant.RIGHT:

				currentChoise = world.getCell((int) a.getXPos() + 1,
						(int) a.getYPos());
				if (currentChoise != null)
					check = choose(1, 0, currentChoise, a);

				break;
			default:
				break;
			}
			if (!check)
				erasedDirection.add(k);

		}
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

	public void moveAnt(Ant a) throws InterruptedException {
		ArrayList<Integer> erasedDirection = new ArrayList<Integer>();
		erasedDirection.add(backDirection(a));

		a.nextStep();

		if (a.getStep_Ant() == Ant.MAXSTEPSAMEDIR) {
			a.setStep_Ant(0);
			a.nextDir();
		}
		// sto provando a dare una direzione privilegiata alle formiche per
		// simulare un movimento più naturale. Aldo
		if (a.getAntState() == Ant.FOUND)
			chooseRandomDirection(a);
		else {

			int k = chooseSearchDirection(a, erasedDirection);
			if (k != -1) {
				boolean check = false;
				Cell currentChoise = null;
				switch (k) {
				case Ant.UP:

					currentChoise = world.getCell((int) a.getXPos(),
							(int) a.getYPos() - 1);
					if (currentChoise != null) {
						check = choose(0, -1, currentChoise, a);

					}
					break;
				case Ant.DOWN:

					currentChoise = world.getCell((int) a.getXPos(),
							(int) a.getYPos() + 1);
					if (currentChoise != null) {
						check = choose(0, 1, currentChoise, a);

					}
					break;

				case Ant.LEFT:

					currentChoise = world.getCell((int) a.getXPos() - 1,
							(int) a.getYPos());
					if (currentChoise != null)
						check = choose(-1, 0, currentChoise, a);

					break;

				case Ant.RIGHT:

					currentChoise = world.getCell((int) a.getXPos() + 1,
							(int) a.getYPos());
					if (currentChoise != null)
						check = choose(1, 0, currentChoise, a);

					break;
				default:
					break;
				}
				if (!check)
					chooseRandomDirection(a);
			}
		}

	}

	private void chooseRandomDirection(Ant a) {
		ArrayList<Integer> dir = new ArrayList<Integer>();
		dir.add(Ant.UP);
		dir.add(Ant.DOWN);
		dir.add(Ant.LEFT);
		dir.add(Ant.RIGHT);
		boolean check = false;

		int k = a.getCurrentDirection();
		while (dir.size() != 0 && !check) {

			Cell currentChoise = world.getCell((int) a.getXPos(),
					(int) a.getYPos());

			switch (dir.get(k)) {
			case Ant.UP:

				currentChoise = world.getCell((int) a.getXPos(),
						(int) a.getYPos() - 1);
				if (currentChoise != null) {
					check = choose(0, -1, currentChoise, a);

				}
				break;
			case Ant.DOWN:

				currentChoise = world.getCell((int) a.getXPos(),
						(int) a.getYPos() + 1);
				if (currentChoise != null) {
					check = choose(0, 1, currentChoise, a);

				}
				break;

			case Ant.LEFT:

				currentChoise = world.getCell((int) a.getXPos() - 1,
						(int) a.getYPos());
				if (currentChoise != null)
					check = choose(-1, 0, currentChoise, a);

				break;

			case Ant.RIGHT:

				currentChoise = world.getCell((int) a.getXPos() + 1,
						(int) a.getYPos());
				if (currentChoise != null)
					check = choose(1, 0, currentChoise, a);

				break;
			default:
				break;
			}

			dir.remove(k);
			if (dir.size() != 0)
				k = new Random().nextInt(dir.size());

			if (!check)
				a.setCurrentDirection(k);
		}

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
			ph = (int) world.getCell(dx, dy).getG().getSearchPhLevel();

		return ph;
	}

	private int getFoundPh(int dx, int dy) {
		int ph = 0;
		if (world.getCell(dx, dy) != null)
			ph = (int) world.getCell(dx, dy).getG().getFoundPhLevel();

		return ph;
	}

	private boolean choose(int dx, int dy, Cell currentChoise, Ant a) {

		boolean check = false;
		int x = (int) a.getXPos();
		int y = (int) a.getYPos();
		int nx = currentChoise.getX();
		int ny = currentChoise.getY();

		world.lockCell(x, y, nx, ny);

		currentChoise = world.getAvailableCell((int) a.getXPos() + dx,
				(int) (a.getYPos() + dy));

		if (currentChoise != null) {
			check = true;
			transaction(a, currentChoise);
		}

		world.unlockCell(x, y, nx, ny);

		return check;
	}

	private void transaction(Ant a, Cell whereGo) {

		Cell current = world.getCell((int) a.getXPos(), (int) a.getYPos());

		if (whereGo.getA() == null) {

			if (a.getLevel() == whereGo.getG().getLevel()) {

				a.setXPos(whereGo.getX());
				a.setYPos(whereGo.getY());
				if (a.getAntState() == Ant.SEARCH) {
					current.getG().increaseSearchPh(a);

					a.releasePheromones(Ant.RESEARCHPHEROMONE);
					diffusePheromones(current.getG(), a);

				}
				if (a.getAntState() == Ant.FOUND) {
					current.getG().increaseFoundPh(a);
					a.releasePheromones(Ant.FOUNDPHEROMONE);
					diffusePheromones(current.getG(), a);

				}

				if (founded(a)) {
					if (a.getAntState() == Ant.SEARCH) {
						a.setStep_Ant(0);
						a.setAntState(Ant.FOUND);
						a.setCurrentDirection(backDirection(a));
						a.restartPhRelease(Ant.FOUNDPHEROMONE);
					}
				}
				if (nested(a)) {
					if (a.getAntState() == Ant.FOUND) {
						a.setStep_Ant(0);
						a.setAntState(Ant.SEARCH);
						a.setCurrentDirection(backDirection(a));
						a.restartPhRelease(Ant.RESEARCHPHEROMONE);
						//System.out.println("Ho portato il cibo alla tana. "+ a.getName());
					}

				}
				whereGo.setA(a);
				current.setA(null);
			} else if (a.getLevel() < whereGo.getG().getLevel())
				a.setLevel(a.getLevel() + 1);
			else
				a.setLevel(a.getLevel() - 1);

		}

	}

	private boolean nested(Ant a) {
		Point nest = world.getNest();
		if (nest.x + World.NEST_WIDTH >= a.getXPos() && nest.x <= a.getXPos()
				&& nest.y <= a.getYPos()
				&& nest.y + World.NEST_HEIGHT >= a.getYPos())
			return true;
		return false;
	}

	private boolean founded(Ant a) {
		ArrayList<Point> foods = world.getFood();
		for (Point food : foods) {
			if (food.getX() + World.FOOD_WIDTH >= a.getXPos()
					&& food.getX() <= a.getXPos()
					&& food.getY() + World.FOOD_HEIGHT >= a.getYPos()
					&& food.getY() <= a.getYPos()) {

				return true;
			}
		}
		return false;
	}

	public void update() {

		lock.lock();
		updating = true;

		for (int i = 0; i < World.WIDTH; i++) {
			for (int j = 0; j < World.HEIGHT; j++) {
				world.getCell(i, j)
						.getG()
						.setFoundPhLevel(
								world.getCell(i, j).getG().getFoundPhLevel()
										- PHREDUCTION);
				world.getCell(i, j)
						.getG()
						.setSearchPhLevel(
								world.getCell(i, j).getG().getSearchPhLevel()
										- PHREDUCTION);
				if (world.getCell(i, j).getG().getSearchPhLevel() < 0) {
					world.getCell(i, j).getG().setSearchPhLevel(0);
				}
				if (world.getCell(i, j).getG().getFoundPhLevel() < 0)
					world.getCell(i, j).getG().setFoundPhLevel(0);
			}

		}
		updating = false;
		condition.signalAll();
		lock.unlock();

	}

	private void diffusePheromones(GroundState groundState, Ant a) {
		ArrayList<Cell> neighbour = getNeighbour(a.getXPos(), a.getYPos());
		for (Cell c : neighbour) {
			if (a.getAntState() == Ant.FOUND)
				c.getG().increaseNeigFoundPh(a, Ant.FOUNDPHEROMONE);
			else
				c.getG().increaseNeigSearchPh(a, Ant.RESEARCHPHEROMONE);
		}

	}

	private ArrayList<Cell> getNeighbour(int xPos, int yPos) {
		ArrayList<Cell> neig = new ArrayList<Cell>();
		if (world.getCell(xPos + 1, yPos) != null) {
			neig.add(world.getCell(xPos + 1, yPos));
		}
		if (world.getCell(xPos - 1, yPos) != null) {
			neig.add(world.getCell(xPos - 1, yPos));
		}
		if (world.getCell(xPos + 1, yPos - 1) != null) {
			neig.add(world.getCell(xPos + 1, yPos - 1));
		}
		if (world.getCell(xPos - 1, yPos - 1) != null) {
			neig.add(world.getCell(xPos - 1, yPos - 1));
		}
		if (world.getCell(xPos + 1, yPos + 1) != null) {
			neig.add(world.getCell(xPos + 1, yPos + 1));
		}
		if (world.getCell(xPos - 1, yPos + 1) != null) {
			neig.add(world.getCell(xPos - 1, yPos + 1));
		}
		if (world.getCell(xPos, yPos + 1) != null) {
			neig.add(world.getCell(xPos, yPos + 1));
		}
		if (world.getCell(xPos, yPos - 1) != null) {
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
