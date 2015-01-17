package com.antSimulator.gui;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.stage.Stage;

public class MainFrame extends Application {

	public final static String MAINLAYOUT = "mainLayout";
	RightPanel right;
	LeftPanel left;

	public static void main(String[] args) {

		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		String css = MainFrame.class.getResource("Css.css").toExternalForm();
		Group root = new Group();
		Scene sc = new Scene(root, 800, 400);
		stage.setScene(sc);

		VBox mainBox = VBoxBuilder.create().id(MAINLAYOUT).build();
		right = new RightPanel();
		HBox centralBox = HBoxBuilder.create().id(MAINLAYOUT).build();
		centralBox.setPrefWidth(800);
		centralBox.setPrefHeight(400);
		left = new LeftPanel();
		centralBox.getChildren().addAll(left.getBox(), right.getBox());
		Label bar = new Label("MenuBar");
		bar.setPrefSize(800, 100);
		bar.setAlignment(Pos.TOP_LEFT);
		mainBox.getChildren().addAll(bar, centralBox);
		root.getStylesheets().add(css);

		root.getChildren().add(mainBox);
		stage.show();
	}

}
