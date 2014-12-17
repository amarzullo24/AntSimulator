package com.antSimulator.logic;

public class Cell {
 
	private Ant a;
	private GroundState g;
	private int x;
	private int y;
	
	public Cell(int x, int y,int level) {
		this.x=x;
		this.y=y;
		g=new GroundState(level);
	}

	public Ant getA() {
		return a;
	}

	public void setA(Ant a) {
		this.a = a;
	}

	public GroundState getG() {
		return g;
	}

	public void setG(GroundState g) {
		this.g = g;
	}
	
	
	
}
