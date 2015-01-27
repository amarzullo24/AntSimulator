package com.antSimulator.logic;

public class GroundState {

	public static final int MAXLEVEL = 10;
	private int level;
	private float foundPhLevel;
	private float searchPhLevel;

	public GroundState(int l) {
		setLevel(l);
		setFoundPhLevel(0);
		setSearchPhLevel(0);

	}

	public void increaseFoundPh(Ant a) {

		foundPhLevel += a.getCurrentPH();
	}

	public void increaseSearchPh(Ant a) {

		searchPhLevel += a.getCurrentPH();
	}

	public void decreasePh(int decrease) {
		if (foundPhLevel > 0)
			foundPhLevel -= decrease;
		else
			foundPhLevel = 0;
		if (searchPhLevel > 0)
			searchPhLevel -= decrease;
		else
			searchPhLevel = 0;
	}

	public int increaseLevel() {
		if (level < MAXLEVEL)
			return level++;
		else
			return level;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public float getFoundPhLevel() {
		return foundPhLevel;
	}

	public void setFoundPhLevel(float phLevel) {
		this.foundPhLevel = phLevel;
	}

	public float getSearchPhLevel() {
		return searchPhLevel;
	}

	public void setSearchPhLevel(float searchPhLevel) {
		this.searchPhLevel = searchPhLevel;
	}

	public void increaseNeigFoundPh(Ant a) {
		int incrPh = a.getCurrentPH() / 10;
		setFoundPhLevel(getFoundPhLevel() + incrPh);

	}

	public void increaseNeigSearchPh(Ant a) {
		int incrPh = a.getCurrentPH() / 10;
		setSearchPhLevel(getSearchPhLevel() + incrPh);

	}

}
