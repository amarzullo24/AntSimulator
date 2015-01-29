package com.antSimulator.logic;

import java.util.ArrayList;

public class Observer {

	private static Observer instance = null;
	private ArrayList<Observed> observed = new ArrayList<Observed>();

	public final static short NESTED_FOOD_UPDATED = 0;
	public final static short TOTAL_FOOD_UPDATED = 1;
	public final static short FOOD_UPDATED = 0;

	public static Observer getIstance(){

		if (instance == null)
			instance = new Observer();

		return instance;
	}

	public void register(Observed o){
		observed.add(o);
	}

	public void update(short message){

		for (Observed ob : observed) {
				ob.update();
		}
	}

}
