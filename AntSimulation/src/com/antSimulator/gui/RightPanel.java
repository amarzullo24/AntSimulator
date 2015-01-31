package com.antSimulator.gui;


import java.util.Arrays;

import com.antSimulator.logic.Manager;
import com.antSimulator.logic.Observed;
import com.antSimulator.logic.World;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.SliderBuilder;
import javafx.scene.control.TitledPane;
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


	public static BarChart<String,Number> stackedBarChart;
	public static PieChart pieChart;
	
	public static XYChart.Data<String,Number>[] data;
	public static PieChart.Data[] pieData;

	public RightPanel() {

		box = VBoxBuilder.create().id(RIGHTLAYOUT).build();
		box.setSpacing(10);
		box.setPrefSize(400, 600);
		box.setAlignment(Pos.CENTER);

		slider_box = new VBox();
		statistics_box = new VBox();
		box.getChildren().add(statistics_box);

		initChart();
		initAccordion();
		initButtons();

		box.getChildren().addAll(slider_box);

		

	}


	private void initChart() {

		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();

		xAxis.setCategories(FXCollections.<String> observableArrayList(Arrays.asList("nested","total" )));

		stackedBarChart = new BarChart<>(xAxis,yAxis);

		stackedBarChart.setTitle("Nested Food");

		//Series 1
		XYChart.Series<String,Number> series1 = new XYChart.Series<String, Number>();
		XYChart.Series<String,Number> series2 = new XYChart.Series<String, Number>();

		data = new XYChart.Data[2];


		data[0] = new BarChart.Data<String, Number>("nested", Manager.NESTED_FOOD);
		data[1] = new BarChart.Data("total", Manager.TOTAL_FOOD);


		series1.getData().add(data[0]);
		series2.getData().add(data[1]);

		stackedBarChart.getData().add(series1);
		stackedBarChart.getData().add(series2);

		stackedBarChart.setAnimated(true);

		statistics_box.getChildren().add(stackedBarChart);
		
		//PIE CHART
		
		pieChart = new PieChart();
		pieData = new PieChart.Data[2];
		
		pieData[0] = new PieChart.Data("totalTime", Manager.TOTAL_TIME/Manager.TOTAL_ANTS_TO_NEST);
		pieData[1] = new PieChart.Data("avg_time", Manager.LAST_ANT_TO_NEST);
		
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(pieData[0],pieData[1]);
        
		pieChart.getData().addAll(pieChartData);
		statistics_box.getChildren().add(pieChart);
		
		// pie chart style
		pieChartData.get(0).getNode().setStyle("-fx-pie-color: #f0f8ffff;");
		pieChartData.get(1).getNode().setStyle("-fx-pie-color: #3399FF;");
	
		pieChart.setLabelLineLength(5);
		pieChart.setLegendSide(Side.LEFT);
		
		pieChart.setAnimated(true);
		pieChart.setTitle("avg time to nest food");


	}

	private Slider buildSlideBar(final String toSet, int maxValue, int minValue, int defaultValue, final Class<?> class1) {

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

					class1.getDeclaredField(toSet).set(this, newVal.intValue());
		
					if(toSet.equals("NUM_OF_ANTS"))
						Manager.getInstance().world.respawnAnts();
						

				} catch (IllegalArgumentException | IllegalAccessException
						| NoSuchFieldException | SecurityException e) {
					
					e.printStackTrace();
				}
			}
		});

		slider_box.getChildren().addAll(s);
		return s;
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

	private void initAccordion() {

		Slider phred = buildSlideBar("PHREDUCTION", 1, 100, Manager.PHREDUCTION, Manager.class);
		Slider uptime = buildSlideBar("UPDATE_TIME", 10, 300, Manager.UPDATE_TIME, Manager.class);
		Slider numants = buildSlideBar("NUM_OF_ANTS", 1, World.MAX_NUM_OF_ANT, 10, World.class);
		Slider fw = buildSlideBar("FOOD_WIDTH", 1, 5, World.FOOD_WIDTH, World.class);
		Slider fh = buildSlideBar("FOOD_HEIGHT", 1, 5, World.FOOD_HEIGHT, World.class);
		Slider gr = buildSlideBar("GROUND_RADIOUS", 1, 5, Manager.GROUND_RADIOUS, Manager.class);
		
		TitledPane t1 = new TitledPane("PH REDUCTION", phred);
		TitledPane t2 = new TitledPane("TIME STEP", uptime);
		TitledPane t3 = new TitledPane("NUMBER OF ANTS", numants);
		TitledPane t4 = new TitledPane("FOOD WIDTH", fw);
		TitledPane t5 = new TitledPane("FOOD HEIGHT", fh);
		TitledPane t6 = new TitledPane("GROUND RADIOUS", gr);
		
		Accordion accordion = new Accordion();
		accordion.getPanes().addAll(t1, t2, t3,t4,t5,t6);
		slider_box.getChildren().add(accordion);
	
	}

	private void initButtons() {
		
		HBox antbox = new HBox();
		HBox stepbox = new HBox();
		HBox buttonbox = new HBox();
		
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
		slider_box.setAlignment(Pos.CENTER_LEFT);
		antbox.setSpacing(10);
		stepbox.setSpacing(10);
		buttonbox.setSpacing(10);

		buttonbox.getChildren().addAll(addFoodButton, removeFoodButton,modifyButton);
		
		box.getChildren().addAll(antbox, stepbox, buttonbox);


	}

	@Override
	public void update() {


	}

}
