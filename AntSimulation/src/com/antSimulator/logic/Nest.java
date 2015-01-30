package com.antSimulator.logic;

import java.awt.Point;
import java.util.ArrayList;

public class Nest {
	

	private int xPos;
	private int yPos;
	private GroundState groundState;
	
	private ArrayList<Ant> ants;
	
	public Nest(int x, int y, GroundState g) {
		xPos=x;
		yPos=y;
		groundState=g;
		ants=new ArrayList<Ant>();
	}

	public int getyPos() {
		return yPos;
	}

	public void setyPos(int yPos) {
		this.yPos = yPos;
	}

	public ArrayList<Ant> getAnts() {
		return ants;
	}

	public void setAnts(ArrayList<Ant> ants) {
		this.ants = ants;
	}

	public int getxPos() {
		return xPos;
	}

	public void setxPos(int xPos) {
		this.xPos = xPos;
	}

	public int getLevel(){
		return groundState.getLevel();
	}
	public GroundState getGroundState() {
		return groundState;
	}

	public void setGroundState(GroundState groundState) {
		this.groundState = groundState;
	}

	public void addAnt(int i) {
		Ant a=new Ant(getLevel(), new Point(xPos,yPos), i);
		ants.add(a);
		
	}

	public Ant removeLastAnt() {
		Ant a=ants.remove(ants.size()-1);
		return a;
	}


	
	
	
}
