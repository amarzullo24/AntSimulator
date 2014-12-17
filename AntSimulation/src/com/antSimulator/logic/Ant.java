package com.antSimulator.logic;

import java.util.Random;

public class Ant {

	public static final int UP=0;
	public static final int DOWN=1;
	public static final int LEFT=2;
	public static final int RIGHT=3;
	
	public static final int SEARCH=5;
	public static final int FOUND=6;
	
	private int direction;
	private int antState;
	private int level;
 
	public Ant(int nestLevel) {
		direction=chooseDir();
		antState=SEARCH;
		level=nestLevel;
	}
	
	public int chooseDir(){
		// modificare
		Random r=new Random(4);
		int d=r.nextInt();
		return d;
	}
}
