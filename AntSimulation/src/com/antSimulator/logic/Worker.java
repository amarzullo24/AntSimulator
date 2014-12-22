package com.antSimulator.logic;

import java.util.concurrent.BlockingQueue;

public class Worker extends Thread {

	BlockingQueue<Ant> ants;

	public Worker(BlockingQueue<Ant> ants) {
		this.ants = ants;
	}

	@Override
	public void run() {
		try {
			while (Manager.ISACTIVE) {

				Ant a = ants.take();
				
				Manager.getInstance().moveAnt(a);
				ants.put(a);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
