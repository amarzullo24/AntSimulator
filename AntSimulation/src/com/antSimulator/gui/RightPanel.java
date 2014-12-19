package com.antSimulator.gui;
import javax.swing.GroupLayout.Alignment;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.Slider;
import javafx.scene.control.SliderBuilder;
import javafx.scene.control.SplitPane;
import javafx.scene.control.SplitPaneBuilder;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleButtonBuilder;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.util.StringConverter;

public class RightPanel {

	private static final String RIGHTLAYOUT = "rightLayout";
	public final static String LABELRIGHT = "labelRight";
	public final static String SPEEDSLIDE = "SpeedSlide";
	public final static String BUTTONLEFT = "buttonl";
	public final static String BUTTONCENTER = "buttonc";
	public final static String BUTTONRIGHT = "buttonr";
	

	private VBox box;
	private Label antValue;
	private Label stepValue;

	public RightPanel() {
		box = VBoxBuilder.create().id(RIGHTLAYOUT).build();
		box.setSpacing(10);
		box.setPrefSize(400, 400);
		box.setAlignment(Pos.CENTER);
		HBox antbox = new HBox();
		HBox stepbox = new HBox();
		HBox buttonbox = new HBox();
		VBox sliderbox = new VBox();


		buttonbox.setAlignment(Pos.CENTER);
		sliderbox.setSpacing(10);
		antbox.setSpacing(10);
		stepbox.setSpacing(10);
		Label antLabel = LabelBuilder.create().id(LABELRIGHT)
				.text("Numero di Formiche").build();
		Label stepLabel = LabelBuilder.create().id(LABELRIGHT)
				.text("Numero di step").build();
		Label speedLabel = LabelBuilder.create().id(LABELRIGHT).text("Speed")
				.build();

		antValue = LabelBuilder.create().id(LABELRIGHT).text("0").build();
		stepValue = LabelBuilder.create().id(LABELRIGHT).text("0").build();
		Slider speedSlide = SliderBuilder.create().id(SPEEDSLIDE).min(1.0)
				.max(4.0).scaleX(0.8).showTickLabels(true).scaleY(0.8).build();
		sliderbox.setAlignment(Pos.CENTER_LEFT);
		ToggleButton start = ToggleButtonBuilder.create().id(BUTTONLEFT)
				.text("Start").build();
		ToggleButton pause = ToggleButtonBuilder.create().id(BUTTONCENTER)
				.text("Pause").build();
		ToggleButton stop = ToggleButtonBuilder.create().id(BUTTONRIGHT)
				.text("Stop").build();

		ToggleGroup buttons = new ToggleGroup();
		start.setToggleGroup(buttons);
		pause.setToggleGroup(buttons);
		stop.setToggleGroup(buttons);

		buttons.selectToggle(stop);
		antbox.getChildren().addAll(antLabel, antValue);
		stepbox.getChildren().addAll(stepLabel, stepValue);
		sliderbox.getChildren().addAll(speedLabel, speedSlide);
		buttonbox.getChildren().addAll(start, pause, stop);

		box.getChildren().addAll(antbox, stepbox, sliderbox, buttonbox);

	}

	public void updateData(String ant, String step) {
		antValue.setText(ant);
		stepValue.setText(step);
	}

	public VBox getBox() {
		return box;
	}

	public void setBox(VBox box) {
		this.box = box;
	}

}
