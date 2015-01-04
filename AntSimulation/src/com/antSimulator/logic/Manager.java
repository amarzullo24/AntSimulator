
package com.antSimulator.logic;

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
	
	public static final int CORE_NUMBER = 2;
	public static boolean ISACTIVE = true;
	public static final int SLEEP_TIME = 10;

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

		world = loadWorld();
		for (int i = 0; i < CORE_NUMBER; i++) {
			workers.add(new Worker(world.getAnts()));
			workers.get(i).start();
		}
	}

	public World loadWorld() {
		return new World();
	}


	public void moveAnt(Ant a) throws InterruptedException {

		ArrayList<Integer> dir = new ArrayList<Integer>();
		dir.add(Ant.UP);
		dir.add(Ant.DOWN);
		dir.add(Ant.LEFT);
		dir.add(Ant.RIGHT);
		boolean check = false;
		
		// sto provando a dare una direzione privilegiata alle formiche per simulare un movimento più naturale. Aldo
		int k = a.getCurrentDirection();
		
		while (dir.size() != 0 && !check) {
			
			Cell currentChoise = world.getCell((int) a.getXPos(),(int) a.getYPos());

			switch (dir.get(k)) {
			case Ant.UP:
				cont_u++;
				currentChoise=world.getCell((int) a.getXPos(), (int) a.getYPos() - 1);
				if ( currentChoise!= null)
					check = choose(0, -1, currentChoise, a);
				break;
			case Ant.DOWN:
				cont_d++;
				currentChoise=world.getCell((int) a.getXPos(), (int) a.getYPos() + 1);
				if ( currentChoise!= null){
					check = choose(0, 1, currentChoise, a);
				}
				break;

			case Ant.LEFT:
				cont_left++;
				currentChoise=world.getCell((int) a.getXPos()-1, (int) a.getYPos());
				if ( currentChoise!= null)
					check = choose(-1, 0, currentChoise, a);
				break;

			case Ant.RIGHT:
				cont_r++;
				currentChoise=world.getCell((int) a.getXPos()+1, (int) a.getYPos());
				if ( currentChoise!= null)
					check = choose(1, 0, currentChoise, a);
				break;
			default:
				break;
			}

			dir.remove(k);
			
			
			if(dir.size() != 0)
				k = new Random().nextInt(dir.size());
			if(!check)
				a.setCurrentDirection(k);
			
//			System.out.println("U: "+cont_u);
//			System.out.println("D: "+cont_d);
//			System.out.println("L: "+cont_left);
//			System.out.println("R: "+cont_r);
//			System.out.println();
		}
		
	}

	private boolean choose(int dx, int dy, Cell currentChoise, Ant a) {

		boolean check = false;
		int x = (int) a.getXPos();
		int y = (int) a.getYPos();
		int nx = currentChoise.getX();
		int ny = currentChoise.getY();

		world.lockCell(x, y, nx, ny);

		currentChoise = world.getAvailableCell((int) a.getXPos() + dx,(int) (a.getYPos() + dy));

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

				if(a.getAntState()==Ant.FOUND)
					current.getG().setPhLevel(current.getG().getPhLevel() + Ant.PHRELEASE);

				whereGo.setA(a);
				current.setA(null);
			} 
			else if (a.getLevel() < whereGo.getG().getLevel())
				a.setLevel(a.getLevel() + 1);
			else
				a.setLevel(a.getLevel() - 1);

		}

	}
}
