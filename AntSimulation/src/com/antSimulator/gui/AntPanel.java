package com.antSimulator.gui;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import com.antSimulator.logic.Cell;
import com.antSimulator.logic.GroundState;
import com.antSimulator.logic.Manager;
import com.antSimulator.logic.Observer;
import com.antSimulator.logic.World;

public class AntPanel extends Application {

	public static final int CELLSIZE = 5;
	public static final int PANEL_SIZE_X = World.WIDTH * CELLSIZE;
	public static final int PANEL_SIZE_Y = World.HEIGHT * CELLSIZE;

	public static final String ADDFOOD = "AddFood";
	public static final String DELETEFOOD = "DeleteFood";
	public static final String MODIFYLEVEL = "ModifyLevel";

	private World world;
	private Group root;
	private Stage stage;
	private Canvas canvas;
	private GraphicsContext gc;
	public static String currentButtonSelection;

	
	public AntPanel() {
		world = Manager.getInstance().world;
		currentButtonSelection = MODIFYLEVEL;

	}

	public static void main(String[] args) {

		Manager.getInstance().start();
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		this.stage = stage;

		this.stage.setTitle("AntSimulator!");
		this.root = new Group();
		this.canvas = new Canvas(PANEL_SIZE_X, PANEL_SIZE_Y);
		this.gc = canvas.getGraphicsContext2D();

		// inits the repainter thread
		initThread();

		root.getChildren().add(canvas);

		HBox hbox = new HBox();
		hbox.getChildren().add(canvas);
		RightPanel rp = new RightPanel(); 
		Observer.getIstance().register(rp);
		hbox.getChildren().add(rp.getBox());
		root.getChildren().add(hbox);

		stage.setScene(new Scene(root));
		stage.show();

		// closes the stage and stops the active threads
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent arg0) {
				Manager.ISACTIVE = false;
				Platform.exit();

			}
		});

		// Clear away portions as the user drags the mouse
		canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {

						short currentX = (short) (e.getX() / CELLSIZE);
						short currentY = (short) (e.getY() / CELLSIZE);

						Cell currentCell = world.getCell(currentX, currentY);

						if (currentCell == null)
							return;

						switch (currentButtonSelection) {
						case MODIFYLEVEL:
							int currentLevel = currentCell.increaseGroundLevel();

							int i = 1;
							while (i < 5) {

								for (int j = currentX - i; j <= currentX + i; j++) {

									currentCell = world
											.getCell(j, currentY - i);
									if (currentCell != null) {
										currentLevel = currentCell.increaseGroundLevel();
										}

									currentCell = world
											.getCell(j, currentY + i);
									if (currentCell != null) {
										currentLevel = currentCell.increaseGroundLevel();
									}
								}

								for (int j = currentY - i; j <= currentY + i; j++) {

									currentCell = world
											.getCell(currentX - i, j);
									if (currentCell != null) {
										currentLevel = currentCell.increaseGroundLevel();
									}

									currentCell = world
											.getCell(currentX + i, j);
									if (currentCell != null) {
										currentLevel = currentCell.increaseGroundLevel();
									}
								}

								i++;
							}
							break;
						case ADDFOOD:
							currentCell.insertFood();

							break;
						case DELETEFOOD:
							currentCell.removeFood();
							break;
						default:
							break;
						}

					}
				});

	}

	private void initThread() {

		new AnimationTimer() {

			@Override
			public void handle(long arg0) {

				repaint();
				RightPanel.data[0].setYValue(Manager.NESTED_FOOD);
				RightPanel.data[1].setYValue(Manager.TOTAL_FOOD);
				//da aggiungere un altro grafico o una label semplice
				int media = Manager.TOTAL_TIME/Manager.TOTAL_ANTS_TO_NEST;
				sleepQuietly(Manager.SLEEP_TIME);

			}
		}.start();
	}

	private void sleepQuietly(int time) {

		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void repaint() {

		gc.clearRect(0, 0, PANEL_SIZE_X, PANEL_SIZE_Y);
		gc.setFill(Color.BLACK);
		gc.strokeRect(0, 0, PANEL_SIZE_X, PANEL_SIZE_Y);

		Manager.getInstance().lock.lock();

		for (int i = 0; i < World.WIDTH; i++) {
			for (int j = 0; j < World.HEIGHT; j++) {

				Cell cell = world.getCell(i, j);
				GroundState g = cell.getGroundState();

				gc.setGlobalAlpha((double) g.getLevel()
						/ ((double) GroundState.MAXLEVEL * 2));
				gc.setFill(Color.GRAY);
				gc.fillRoundRect(i * CELLSIZE, j * CELLSIZE, CELLSIZE,
						CELLSIZE, 0, 0);

				gc.setGlobalAlpha(g.getFoundPhLevel() / 1000);
				gc.setFill(Color.GREENYELLOW);
				gc.fillRoundRect(i * CELLSIZE, j * CELLSIZE, CELLSIZE,
						CELLSIZE, 10, 10);

				gc.setGlobalAlpha(g.getSearchPhLevel() / 1000);
				gc.setFill(Color.BROWN);
				gc.fillRoundRect(i * CELLSIZE, j * CELLSIZE, CELLSIZE,
						CELLSIZE, 10, 10);

				gc.setGlobalAlpha(cell.getFood() / Cell.MAX_FOOD);
				gc.setFill(Color.GREEN);
				gc.fillRect(i * CELLSIZE, j * CELLSIZE, CELLSIZE, CELLSIZE);

				if (cell.getAntsSet().size() > 0) {

					gc.setGlobalAlpha(1.0);
					gc.setFill(Color.RED);
				}

				gc.fillRoundRect(i * CELLSIZE, j * CELLSIZE, CELLSIZE,
						CELLSIZE, 10, 10);
				

			}// for

		}// for
		gc.setGlobalAlpha(1.0);
		gc.setFill(Color.BROWN);
		gc.fillRoundRect(world.getNest().getX() * CELLSIZE, world.getNest()
				.getY() * CELLSIZE, CELLSIZE * World.NEST_WIDTH, CELLSIZE
				* World.NEST_HEIGHT, 10, 10);
		
		Manager.getInstance().lock.unlock();
	}// repaint

}// class
