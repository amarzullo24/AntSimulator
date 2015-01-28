package com.antSimulator.gui;


import java.util.Arrays;

import com.antSimulator.logic.Manager;
import com.antSimulator.logic.Observed;
import com.antSimulator.logic.Observer;
import com.antSimulator.logic.World;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.SliderBuilder;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;

public class RightPanel implements Observed{

	private static final String RIGHTLAYOUT = "rightLayout";
	public final static String LABELRIGHT = "labelRight";
	public final static String SPEEDSLIDE = "SpeedSlide";
	public final static String BUTTONLEFT = "buttonl";
	public final static String BUTTONCENTER = "buttonc";
	public final static String BUTTONRIGHT = "buttonr";

	private VBox box;
	private Label antValue;
	private Label stepValue;

	private VBox slider_box;
	private VBox statistics_box;
	
	//TODO
	/*
	 * TEMPO MEDIO DI ANDATA E RITORNO
	 * MEDIA CIBO PORTATO A DESTINAZIONE
	 * 
	 */

    public static StackedBarChart<String,Number> stackedBarChart;
	
	public RightPanel() {
		
		box = VBoxBuilder.create().id(RIGHTLAYOUT).build();
		box.setSpacing(10);
		box.setPrefSize(400, 400);
		box.setAlignment(Pos.CENTER);
		HBox antbox = new HBox();
		HBox stepbox = new HBox();
		HBox buttonbox = new HBox();
		slider_box = new VBox();
		statistics_box = new VBox();
		
		box.getChildren().add(statistics_box);
		initChart();
		
		buttonbox.setAlignment(Pos.CENTER);
		ToggleGroup group = new ToggleGroup();
		RadioButton addFoodButton = new RadioButton("Add Food");
		RadioButton removeFoodButton = new RadioButton("Remove Food");
		RadioButton modifyButton = new RadioButton("Modify Ground");
		addFoodButton.setToggleGroup(group);
		addFoodButton.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean oldVal, Boolean newVal) {
				if(newVal)
					AntPanel.currentButtonSelection=AntPanel.ADDFOOD;
				
			}

			
		});
		removeFoodButton.setToggleGroup(group);
		removeFoodButton.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean arg1, Boolean newVal) {
				if(newVal)
					AntPanel.currentButtonSelection=AntPanel.DELETEFOOD;
				
			}
		});
		modifyButton.setToggleGroup(group);
		modifyButton.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean arg1, Boolean newVal) {
				if(newVal)
					AntPanel.currentButtonSelection=AntPanel.MODIFYLEVEL;
				
			}
		});
		modifyButton.setSelected(true);
		slider_box.setSpacing(10);
		antbox.setSpacing(10);
		stepbox.setSpacing(10);
		buttonbox.setSpacing(10);

		buttonbox.getChildren().addAll(addFoodButton, removeFoodButton,
				modifyButton);
		buildSlideBar("PHREDUCTION", 1, 100, Manager.PHREDUCTION);
		buildSlideBar("UPDATE_TIME", 10, 300, Manager.UPDATE_TIME);
		buildSlideBar("NUM_OF_ANTS", 1, World.MAX_NUM_OF_ANT, 10);

		box.getChildren().addAll(antbox, stepbox, slider_box, buttonbox);

	}

	static short STEP = 1;
	private void initChart() {
		
		final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();

        xAxis.setCategories(FXCollections.<String> observableArrayList(Arrays.asList("nested","total" )));
           
        stackedBarChart = new StackedBarChart<>(xAxis,yAxis);
   
        stackedBarChart.setTitle("Nested Food");
          
        //Series 1
        XYChart.Series<String,Number> series1 = new Series<String, Number>();
        series1.getData().add(new Data<String, Number>("nested", Manager.NESTED_FOOD));
        
        XYChart.Series<String,Number> series2 = new Series<String, Number>();
        series2.getData().add(new Data<String, Number>("total", Manager.TOTAL_FOOD));
        
        stackedBarChart.getData().add(series1);
        stackedBarChart.getData().add(series2);
        
        stackedBarChart.setAnimated(true);
        
        statistics_box.getChildren().add(stackedBarChart);
		
	}

	private void buildSlideBar(final String toSet, int maxValue, int minValue,
			int defaultValue) {

		Slider s = SliderBuilder.create().id(SPEEDSLIDE).min(maxValue)
				.max(minValue).scaleX(0.8).showTickLabels(true).scaleY(0.8)
				.build();

		s.setValue(defaultValue);
		s.setTooltip(new Tooltip(toSet));

		slider_box.setAlignment(Pos.CENTER_LEFT);

		s.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number newVal) {

				try {

					Manager.getInstance().getClass().getDeclaredField(toSet)
							.set(this, newVal.intValue());

				} catch (IllegalArgumentException | IllegalAccessException
						| NoSuchFieldException | SecurityException e) {

					try {

						// al momento l'unico metodo che non è nel manager lo
						// metto nel catch
						World.NUM_OF_ANTS = newVal.intValue();
						Manager.getInstance().world.respawnAnts();

					} catch (SecurityException e1) {

						e1.printStackTrace();
					} catch (IllegalArgumentException e1) {

						e1.printStackTrace();
					}
				}
			}
		});

		slider_box.getChildren().addAll(s);
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

	@Override
	public void update() {
		System.out.println("lasdf");
		stackedBarChart.getData().get(0).getData().set(0, new Data<String, Number>("nested", Manager.NESTED_FOOD));
		
	}

}
