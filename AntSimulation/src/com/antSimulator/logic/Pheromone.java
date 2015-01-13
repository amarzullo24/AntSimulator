package com.antSimulator.logic;

public class Pheromone {

	private int quantity;

	public Pheromone(int q) {
		quantity = q;
	}

	public void reduceQuantity(int reduction) {
		if (quantity > 0)
			quantity -= reduction;
		else
			quantity = 0;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
