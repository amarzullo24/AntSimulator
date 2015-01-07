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

	public static final int PHREDUCTION = 1;
	public static final int CORE_NUMBER = 7;
	public static boolean ISACTIVE = true;
	public static final int SLEEP_TIME = 10;
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

	public void moveAnt(Ant a) throws InterruptedException {
		a.setStep_Ant(a.getStep_Ant() + 1);

		if (a.getStep_Ant() == 100) {
			a.setStep_Ant(0);
			a.setCurrentDirection(a.getNextDir());
		}
		// sto provando a dare una direzione privilegiata alle formiche per
		// simulare un movimento più naturale. Aldo
		if (a.getAntState() == Ant.FOUND)
			chooseRandomDirection(a);
		else {

			int k = chooseDirection(a);
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

	private int chooseDirection(Ant a) {
		int erase = backDirection(a);
		int dir;
		int ph;
		ArrayList<Integer> directionPriority = new ArrayList<Integer>();
		if (erase != Ant.UP) {
			dir = Ant.UP;
			ph = getPh(a.getXPos(), a.getYPos() - 1);

		} else {
			dir = Ant.RIGHT;
			ph = getPh(a.getXPos() + 1, a.getYPos());
		}
		directionPriority.add(dir);
		if (erase != Ant.RIGHT && ph < getPh(a.getXPos() + 1, a.getYPos())) {
			ph = getPh(a.getXPos() + 1, a.getYPos());
			directionPriority.add(0, dir);
			dir = Ant.RIGHT;
		} else
			directionPriority.add(Ant.RIGHT);
		if (erase != Ant.LEFT && ph < getPh(a.getXPos() - 1, a.getYPos())) {
			ph = getPh(a.getXPos() - 1, a.getYPos());
			directionPriority.add(0, dir);
			dir = Ant.LEFT;
		} else
			directionPriority.add(Ant.LEFT);
		if (erase != Ant.DOWN && ph < getPh(a.getXPos(), a.getYPos() + 1)) {
			ph = getPh(a.getXPos(), a.getYPos() + 1);
			directionPriority.add(0, dir);
			dir = Ant.DOWN;
		} else
			directionPriority.add(Ant.DOWN);

		switch (a.getCurrentDirection()) {
		case Ant.UP:
			if (ph == getPh(a.getXPos(), a.getYPos() - 1))
				return Ant.UP;
			break;
		case Ant.RIGHT:
			if (ph == getPh(a.getXPos() + 1, a.getYPos()))
				return Ant.RIGHT;
			break;
		case Ant.LEFT:
			if (ph == getPh(a.getXPos() - 1, a.getYPos()))
				return Ant.LEFT;
			break;
		case Ant.DOWN:
			if (ph == getPh(a.getXPos(), a.getYPos() + 1))
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

	private int getPh(int dx, int dy) {
		int ph = 0;
		if (world.getCell(dx, dy) != null)
			ph = (int) world.getCell(dx, dy).getG().getPhLevel();

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

				if (a.getAntState() == Ant.FOUND) {
					current.getG().setPhLevel(
							current.getG().getPhLevel() + Ant.PHRELEASE);

				}
				if (founded(a))
					a.setAntState(Ant.FOUND);
				if (nested(a))
					a.setAntState(Ant.SEARCH);
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
		if (nest.x == a.getXPos() && nest.y == a.getYPos())
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
						.setPhLevel(
								world.getCell(i, j).getG().getPhLevel()
										- PHREDUCTION);
				if (world.getCell(i, j).getG().getPhLevel() < 0)
					world.getCell(i, j).getG().setPhLevel(0);
			}

		}
		updating = false;
		condition.signalAll();
		lock.unlock();

	}

	private class UpdateThread extends Thread {

		@Override
		public void run() {
			while (ISACTIVE) {
				Manager.getInstance().update();

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}
}
