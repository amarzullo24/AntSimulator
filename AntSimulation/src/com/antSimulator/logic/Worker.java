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
				
				Manager.getInstance().lock.lock();
				while(Manager.getInstance().updating)
					Manager.getInstance().condition.await();
				
				Manager.getInstance().lock.unlock();
				
				Ant a = ants.take();
				
				Manager.getInstance().moveAnt(a);
	
				ants.put(a);
				
				sleepQuietly(Manager.SLEEP_TIME);
			}
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	}
	
	private void sleepQuietly(int time){
		
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	

}
