/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import static javafx.geometry.Orientation.HORIZONTAL;
import static javafx.geometry.Orientation.VERTICAL;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Ville
 */

public class InitUI {
    private static final Logger ERRORLOGGER = Logger.getLogger(InitUI.class.getName());

    public static GridPane createManagerPane(int columns) {
        GridPane managerPane = new GridPane();
        // Position the manager pane at the center of the screen, both vertically and horizontally
        managerPane.setAlignment(Pos.CENTER);
        // Set a padding of 60px on each side
        managerPane.setPadding(new Insets(60, 60, 60, 60));
        // Set the horizontal gap between columns
        managerPane.setHgap(20);
        // Set the vertical gap between rows
        managerPane.setVgap(20);
        // Add column constraints
        for (int i = 0; i < columns; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(25);
            column.setHalignment(HPos.CENTER);
            managerPane.getColumnConstraints().add(column);
        }
        return managerPane;
    }
    
    public static BorderPane createPowerLineControls(int column) {
        Slider frequencyControl = Main.getFrequencyControls()[column];
        Slider amplitudeControl = Main.getAmplitudeControls()[column];
        Slider phaseControl = Main.getPhaseControls()[column];
        VBox controlButtonFrame = Main.getControlButtonFrames()[column];
        BorderPane powerLineControlBlock = new BorderPane();
        GridPane.setRowIndex(powerLineControlBlock, 7);
        GridPane.setColumnIndex(powerLineControlBlock, column);
        powerLineControlBlock.setId("powerLineControls" + column);
        powerLineControlBlock = formatControlPane(powerLineControlBlock, phaseControl, frequencyControl, controlButtonFrame, amplitudeControl);
        return powerLineControlBlock;
    }
    
    private static BorderPane formatControlPane(BorderPane controlBlock, Node top, Node left, Node center, Node right) {
        controlBlock.setTop(top);
        controlBlock.setAlignment(top, Pos.TOP_CENTER);
        controlBlock.setMargin(top, new Insets(0, 40, 0, 40));
        controlBlock.setCenter(center);
        controlBlock.setAlignment(center, Pos.CENTER);
        controlBlock.setLeft(left);
        controlBlock.setAlignment(left, Pos.CENTER);
        controlBlock.setMargin(left, new Insets(0, 20, 00, 40));
        controlBlock.setRight(right);
        controlBlock.setAlignment(right, Pos.CENTER);
        controlBlock.setMargin(right, new Insets(0, 40, 00, 20));
        return controlBlock;
    }
    
    public static Slider createFrequencyControl(int column) {
        Slider frequencyControl = makeSlider(0, 127, 64, VERTICAL, "Frequency");
        int baseFrequency = Main.getPowerManager().getPowerLine(column).getInputAdjuster().getBaseFrequency();
        frequencyControl.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Main.getPowerManager().getPowerLine(column).getInputAdjuster().setCurrentFrequency((int) Math.round((newValue.intValue() - 64) / 100.0 * baseFrequency + baseFrequency));
                Main.getPowerManager().getPowerLine(column).updateOscilloscopeData();
            }
        });
        return frequencyControl;
    }
    
    public static Slider createAmplitudeControl(int column) {
        Slider amplitudeControl = makeSlider(0, 127, 64, VERTICAL, "Amplitude");        
        int baseAmplitude = Main.getPowerManager().getPowerLine(column).getInputAdjuster().getBaseAmplitude();
        amplitudeControl.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Main.getPowerManager().getPowerLine(column).getInputAdjuster().setCurrentAmplitude((int) Math.round((newValue.intValue() - 64) / 100.0 * baseAmplitude + baseAmplitude));
                Main.getPowerManager().getPowerLine(column).updateOscilloscopeData();
            }
        });
        return amplitudeControl;
    }
    
    public static Slider createPhaseControl(int column) {
        Slider phaseControl = makeSlider(0, 127, 64, HORIZONTAL, "Phase");
        double basePhase = Main.getPowerManager().getPowerLine(column).getInputAdjuster().getBasePhase();
        phaseControl.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Main.getPowerManager().getPowerLine(column).getInputAdjuster().setCurrentPhase((newValue.intValue() - 64) / 100.0 * Math.PI + basePhase);
                Main.getPowerManager().getPowerLine(column).updateOscilloscopeData();
            }
        });
        return phaseControl;
    }
    
    public static StatusLed createStatusLed(int column) {
        StatusLed statusLed = new StatusLed();
        statusLed.setId("statusLed" + column);
        return statusLed;
    }
    
    public static ToggleButton createOfflineButton(int column) {
        ToggleButton offlineButton = new ToggleButton("Offline");
        offlineButton.setId("offlineButton" + column);
        offlineButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (offlineButton.isSelected() == true) {
                Main.powerManager.getPowerLine(column).setOffline();
            }
            if (offlineButton.isSelected() == false) {
                Main.powerManager.getPowerLine(column).setOnline();
            }
        }));
        return offlineButton;
    }
    
    public static ToggleButton createShutdownButton(int column) {
        ToggleButton shutdownButton = new ToggleButton("Shutdown");
        shutdownButton.setId("shutdownButton" + column);
        shutdownButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (shutdownButton.isSelected() == true) {
                Main.powerManager.getPowerLine(column).shutdownProcess();
            }
            if (shutdownButton.isSelected() == false) {
                Main.powerManager.getPowerLine(column).startupProcess();
            }
        }));
        return shutdownButton;
    }
    
    public static VBox createControlButtonFrame(int column) {
        StatusLed statusLed = Main.getStatusLeds()[column];
        ToggleButton offlineButton = Main.getOfflineButtons()[column];
        ToggleButton shutdownButton = Main.getShutdownButtons()[column];
        VBox controlButtonFrame = new VBox(20);
        controlButtonFrame.getChildren().addAll(statusLed, offlineButton, shutdownButton);
        controlButtonFrame.setAlignment(Pos.CENTER);
        return controlButtonFrame; 
    }
    
    private static Slider makeSlider(int minValue, int maxValue, int value, Orientation orientation, String label) {
        // Constructing and formatting the slider
        Slider slider = new Slider(minValue, maxValue, value);
        slider.setShowTickLabels(false);
        slider.setShowTickMarks(false);
        slider.setMajorTickUnit(16);
        slider.setMinorTickCount(16);
        slider.setSnapToTicks(true);
        slider.setBlockIncrement(1);
        slider.setOrientation(orientation);
        if (orientation.equals(HORIZONTAL)) {
            slider.setMinWidth(100);
            slider.setPrefWidth(150);
            slider.setMaxWidth(150);
        } else {
            slider.setMinHeight(100);
            slider.setPrefHeight(150);
            slider.setMaxHeight(200);
        }
        Label sliderLabel = new Label(label);
        sliderLabel.setLabelFor(slider);
        return slider;
    }
    
    public static Oscilloscope createOscilloscope(int column, domain.PowerLine powerLine) {
        int timeframe = 195;
        int scale = 150;        
        Oscilloscope oscilloscope = new Oscilloscope(column, powerLine, timeframe, scale);
        oscilloscope.setAlignment(Pos.CENTER);
        oscilloscope.getXAxis().setTickMarkVisible(false);
        oscilloscope.getXAxis().setMinorTickVisible(false);
        oscilloscope.getXAxis().setTickLabelsVisible(false);
        oscilloscope.getYAxis().setTickMarkVisible(false);
        oscilloscope.getYAxis().setMinorTickVisible(false);
        oscilloscope.getYAxis().setTickLabelsVisible(false);
        oscilloscope.getChart().setCreateSymbols(false);
        oscilloscope.getChart().setLegendVisible(false);
        oscilloscope.getChart().setAnimated(true);
        oscilloscope.getChart().setMinHeight(150);
        oscilloscope.getChart().setPrefHeight(200);
        oscilloscope.getChart().setMaxHeight(250);
        GridPane.setRowIndex(oscilloscope, 6);
        GridPane.setColumnIndex(oscilloscope, column);
        return oscilloscope;
    }
    
    
    public static Label createOutputGauge(int column, domain.PowerLine powerline) {
        Label outputGauge = new Label();
        outputGauge.textProperty().bind(powerline.getOutputPower().asString());
        GridPane.setRowIndex(outputGauge, 5);
        GridPane.setColumnIndex(outputGauge, column);
        return outputGauge;
        /*
        StackPane gaugePane = new StackPane();
        Gauge gauge = GaugeBuilder  
        .create()  
        .prefSize(300,300) // Set the preferred size of the control  
        .foregroundBaseColor(Color.BLACK)  // Defines a color foreground elements  
        .title("Output power") // Set the text for the title  
        .titleColor(Color.BLACK) // Define the color for the title text       
        .unit("%") // Set the text for the unit  
        .unitColor(Color.BLACK) // Define the color for the unit  
        .valueColor(Color.BLACK) // Define the color for the value text  
        .decimals(0) // Set the number of decimals for the value/lcd text  
        .minValue(0) // Set the start value of the scale  
        .maxValue(120) // Set the end value of the scale  
        .startAngle(340) // Start angle of your scale (bottom -> 0, direction -> CCW)  
        .angleRange(300) // Angle range of your scale starting from the start angle    
        .tickLabelColor(Color.BLACK) // Color for tick labels  
        .majorTickMarksVisible(true) // Major tick marks should be visible  
        .majorTickMarkType(TickMarkType.LINE) // LINE, DOT, TRIANGLE, TICK_LABEL  
        .majorTickMarkColor(Color.BLACK) // Color for the major tick marks  
        .mediumTickMarksVisible(true) // Medium tick marks should be visible  
        .mediumTickMarkType(TickMarkType.LINE) // LINE, DOT, TRIANGLE  
        .mediumTickMarkColor(Color.BLACK) // Color for the medium tick marks  
        .minorTickMarksVisible(true) // Minor tick marks should be visible  
        .minorTickMarkType(TickMarkType.LINE) // LINE, DOT, TRIANGLE  
        .minorTickMarkColor(Color.BLACK) // Color for minor tick marks  
        .ledVisible(true) // LED should be visible  
        .ledColor(Color.rgb(255, 200, 0)) // Color of the LED  
        .ledBlinking(false) // LED should blink  
        .needleColor(Color.CRIMSON) // Color of the needle  
        .knobType(Gauge.KnobType.STANDARD) // STANDARD, METAL, PLAIN, FLAT  
        .knobColor(Color.LIGHTGRAY) // Color that should be used for the center knob  
        .thresholdVisible(true) // Threshold indicator should be visible  
        .threshold(100) // Value for the threshold  
        .thresholdColor(Color.RED) // Color for the threshold  
        .checkThreshold(false) // Check each value against threshold  
        .onThresholdExceeded(thresholdEvent -> System.out.println("Threshold exceeded"))  
        .onThresholdUnderrun(thresholdEvent -> System.out.println("Threshold underrun"))    
        .gradientBarEnabled(true) // Gradient filled bar should be visible  
        .gradientBarStops(new Stop(0.0, Color.CHARTREUSE), // Gradient for gradient bar  
                          new Stop(0.2, Color.GREENYELLOW),  
                          new Stop(0.4, Color.YELLOW),  
                          new Stop(0.6, Color.GOLD),  
                          new Stop(0.8, Color.ORANGE),  
                          new Stop(1.0, Color.DARKORANGE))  
        .sectionsVisible(true)  // Sections will be visible  
        .sections(new Section(100, 120, Color.RED)) // Sections that will be drawn  
        .checkSectionsForValue(false) // Check current value against each section  
        .markersVisible(true) // Markers will be visible  
        .markers(new Marker(50, "Min", Color.YELLOW)) // Markers that will be drawn  
        .animated(true) // Needle will be animated  
        .animationDuration(500)  // Speed of the needle in milliseconds (10 - 10000 ms)  
        .build();
        GridPane.setRowIndex(gaugePane, 5);
        GridPane.setColumnIndex(gaugePane, column);
        gaugePane.setId("gaugePane" + column);
        gaugePane.getChildren().add(gauge);
        return gaugePane;
        */     
    }
    
    
    
}







