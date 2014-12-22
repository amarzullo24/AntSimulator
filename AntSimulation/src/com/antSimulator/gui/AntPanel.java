package com.antSimulator.gui;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import com.antSimulator.logic.Cell;
import com.antSimulator.logic.GroundState;
import com.antSimulator.logic.Manager;
import com.antSimulator.logic.World;

public class AntPanel extends Application {

	public static final int CELLSIZE = 3;
	private World world;
	private Group root;
	private Group group;
	private Scene sc;
	private Stage stage;
	private Canvas canvas;
	private GraphicsContext gc;

	public AntPanel() {
		world = Manager.getInstance().world;

	}

	public static void main(String[] args) {

		Manager.getInstance().start();
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		this.stage = stage;
		//		root = new Group();
		//		sc = new Scene(root, 900, 900);
		//		stage.setScene(sc);
		//		group = new Group();

		this.stage.setTitle("AntSimulator!");
		this.root = new Group();
		this.canvas = new Canvas(900, 900);
		this.gc = canvas.getGraphicsContext2D();

		//draws the logical matrix on a canvas object
		//repaint();

		//inits the repainter thread
		initThread();

		root.getChildren().add(canvas);
		stage.setScene(new Scene(root));
		stage.show();


		//closes the stage and stops the active threads
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent arg0) {
				Manager.ISACTIVE = false;
				Platform.exit();

			}
		});

	}

	private void initThread(){
//		Platform.runLater(new Runnable() {
//
//			@Override
//			public void run() {
//
//				while(true){
//					repaint();
//					try {
//						Thread.sleep(500);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//		});
		
		new AnimationTimer() {
			
			@Override
			public void handle(long arg0) {
				
				repaint();
				sleepQuietly(500);
				
			}
		}.start();
	}

	private void sleepQuietly(int time){
		
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	private void repaint(){

		gc.clearRect(0, 0, 900, 900);

		for (int i = 0; i < world.getWidth(); i++) {
			for (int j = 0; j < world.getHeight(); j++) {
				Cell c = world.getCell(i, j);
				GroundState g = c.getG();

				if (g.getLevel() == GroundState.MAXLEVEL) {

					gc.setFill(Color.BLACK);
					gc.fillRoundRect(i * CELLSIZE, j * CELLSIZE,
							CELLSIZE, CELLSIZE,10,10);


				}
				if (c.getA() != null) {
					//System.out.println("Ho spostato " + c.getA().getName() + " " + c.getA().getXPos()+" "+c.getA().getYPos()+" con livello "+c.getA().getLevel()); 

					gc.setFill(Color.RED);
					gc.fillRoundRect(i * CELLSIZE, j * CELLSIZE,
							CELLSIZE, CELLSIZE,10,10);

				}//if

			}//for

		}//for


	}//repaint

}//class
