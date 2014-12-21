package com.antSimulator.gui;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;


public class LeftPanel {

	public static final String LEFTLAYOUT = "leftLayout";
	private VBox box;
	
	public LeftPanel() {
		String css=LeftPanel.class.getResource("Css.css").toExternalForm();
		
		box=VBoxBuilder.create().id("left").build();
		box.setPrefSize(400, 400);
		box.setSpacing(20);
		box.setAlignment(Pos.CENTER_LEFT);
		VBox zoomBox=new VBox();
		zoomBox.setSpacing(10);
		
		Label zoomLabel=new Label("Zoom");
		SplitPane paintComponent=new SplitPane();
		paintComponent.setDividerPositions(0.5);
		paintComponent.setId(LEFTLAYOUT);
		paintComponent.setPrefHeight(350);
		Slider slide=new Slider(1.0, 5.0, 0.5);
		slide.showTickLabelsProperty();
		slide.setValueChanging(true);
		zoomBox.getChildren().addAll(zoomLabel,slide);
		zoomBox.setAlignment(Pos.TOP_LEFT);
		box.getStylesheets().add(css);
		box.getChildren().addAll(paintComponent,zoomBox);
		
	}

	public VBox getBox() {
		return box;
	}

	public void setBox(VBox box) {
		this.box = box;
	}
}
