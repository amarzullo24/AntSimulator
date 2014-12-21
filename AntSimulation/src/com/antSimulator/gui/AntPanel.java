package com.antSimulator.gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import com.antSimulator.logic.Cell;
import com.antSimulator.logic.GroundState;
import com.antSimulator.logic.Manager;
import com.antSimulator.logic.World;

public class AntPanel extends Application {

	public static final int CELLSIZE = 3;
	World world;

	public AntPanel() {
		world = Manager.getInstance().world;

	}

	public static void main(String[] args) {

		Manager.getInstance().start();
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Group root = new Group();
		Scene sc = new Scene(root, 900, 900);
		stage.setScene(sc);
		Group group = new Group();

		for (int i = 0; i < world.getWidth(); i++) {
			for (int j = 0; j < world.getHeight(); j++) {
				Cell c = world.getCell(i, j);
				GroundState g = c.getG();

				if (g.getLevel() == GroundState.MAXLEVEL) {
					Rectangle r = new Rectangle(i * CELLSIZE, j * CELLSIZE,
							CELLSIZE, CELLSIZE);
					r.setFill(Color.BLACK);
					group.getChildren().add(r);
				}
				if (c.getA() != null) {
					System.out.println("Ho spostato " + c.getA().getName() + " "
							+ c.getA().getXPos()+" "+c.getA().getYPos()+" con livello "+c.getA().getLevel()); 
							Rectangle ant = new Rectangle(i * CELLSIZE, j * CELLSIZE,
							CELLSIZE, CELLSIZE);
					ant.setFill(Color.RED);
					group.getChildren().add(ant);
				}

			}

		}

		root.getChildren().add(group);
		stage.show();
	}

}