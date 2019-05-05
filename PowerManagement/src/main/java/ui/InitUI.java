/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.Marker;
import eu.hansolo.medusa.Section;
import eu.hansolo.medusa.TickMarkType;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import static javafx.geometry.Orientation.HORIZONTAL;
import static javafx.geometry.Orientation.VERTICAL;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Ville
 */

public class InitUI {
    private static domain.Manager manager = Main.getPowerManager();
    
    public static GridPane createManagerPane(int columns) {
        GridPane managerPane = new GridPane();
        // Position the manager pane at the center of the screen, both vertically and horizontally
        managerPane.setAlignment(Pos.CENTER);
        // Set a padding of 60px on each side
        managerPane.setPadding(new Insets(10, 10, 10, 10));
        // Set the horizontal gap between columns
        managerPane.setHgap(20);
        // Set the vertical gap between rows
        managerPane.setVgap(0);
        // Add column constraints
        for (int i = 0; i < columns; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100 / columns);
            column.setHalignment(HPos.CENTER);
            managerPane.getColumnConstraints().add(column);
        }
        return managerPane;
    }
    
    public static BorderPane createPowerLineControls(int column) {
        
        Slider frequencyControl = Main.getFrequencyControls()[column];
        Label frequencyLabel = new Label("Frequency");
        frequencyLabel.setLabelFor(frequencyControl);
        frequencyLabel.setAlignment(Pos.CENTER_LEFT);
        frequencyLabel.setRotate(270);
        
        Slider amplitudeControl = Main.getAmplitudeControls()[column];
        Label amplitudeLabel = new Label("Amplitude");
        amplitudeLabel.setLabelFor(amplitudeControl);
        amplitudeLabel.setAlignment(Pos.CENTER_RIGHT);
        amplitudeLabel.setRotate(90);
        
        Slider phaseControl = Main.getPhaseControls()[column];
        Label phaseLabel = new Label("Phase");
        phaseLabel.setLabelFor(phaseControl);
        phaseLabel.setAlignment(Pos.CENTER);
        
        VBox controlButtonFrame = Main.getControlButtonFrames()[column];
        BorderPane powerLineControlBlock = new BorderPane();
        GridPane.setRowIndex(powerLineControlBlock, 7);
        GridPane.setColumnIndex(powerLineControlBlock, column);
        powerLineControlBlock.setId("powerLineControls" + column);
        controlButtonFrame.setTranslateY(5);
        
        HBox frequencyBlock = new HBox(-20);
        frequencyBlock.getChildren().addAll(frequencyLabel, frequencyControl);
        frequencyBlock.setAlignment(Pos.CENTER_LEFT);
        frequencyBlock.setMaxHeight(20);
        controlButtonFrame.setTranslateY(5);
        
        HBox amplitudeBlock = new HBox(-20);
        amplitudeBlock.getChildren().addAll(amplitudeControl, amplitudeLabel);
        amplitudeBlock.setAlignment(Pos.CENTER_RIGHT);
        amplitudeBlock.setMaxHeight(20);
        controlButtonFrame.setTranslateY(5);
        
        VBox phaseBlock = new VBox();
        phaseBlock.getChildren().addAll(phaseLabel, phaseControl);
        phaseBlock.setAlignment(Pos.CENTER);
        phaseBlock.setTranslateY(10);
        
        Label lineLabel = new Label("Reactor Line " + (column + 1));
        lineLabel.setStyle("-fx-font-size: 1.5em; -fx-font-weight: bold;");
        
        controlButtonFrame.setAlignment(Pos.CENTER);
        powerLineControlBlock = formatControlPane(powerLineControlBlock, phaseBlock, frequencyBlock, controlButtonFrame, amplitudeBlock, lineLabel);
        return powerLineControlBlock;
    }
    
    private static BorderPane formatControlPane(BorderPane controlBlock, Node top, Node left, Node center, Node right, Node bottom) {
        controlBlock.setTop(top);
        controlBlock.setAlignment(top, Pos.TOP_CENTER);
        controlBlock.setMargin(top, new Insets(0, 40, 0, 40));
        controlBlock.setCenter(center);
        controlBlock.setAlignment(center, Pos.CENTER);
        controlBlock.setLeft(left);
        controlBlock.setAlignment(left, Pos.CENTER_LEFT);
        controlBlock.setMargin(left, new Insets(0, 20, 0, 40));
        controlBlock.setRight(right);
        controlBlock.setAlignment(right, Pos.CENTER_RIGHT);
        controlBlock.setMargin(right, new Insets(0, 40, 0, 20));
        controlBlock.setMinWidth(350);
        controlBlock.setBottom(bottom);
        controlBlock.setAlignment(bottom, Pos.CENTER);
        return controlBlock;
    }
    
    public static Slider createFrequencyControl(int column) {
        Slider frequencyControl = makeSlider(0, 127, 64, VERTICAL, "Frequency");
        int baseFrequency = manager.getPowerLine(column).getInputAdjuster().getBaseFrequency();
        frequencyControl.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                manager.getPowerLine(column).getInputAdjuster().setCurrentFrequency((int) Math.round((newValue.intValue() - 64) / 100.0 * baseFrequency + baseFrequency));
                manager.getPowerLine(column).updateOscilloscopeData();
            }
        });
        return frequencyControl;
    }
    
    public static Slider createAmplitudeControl(int column) {
        Slider amplitudeControl = makeSlider(0, 127, 64, VERTICAL, "Amplitude");        
        int baseAmplitude = manager.getPowerLine(column).getInputAdjuster().getBaseAmplitude();
        amplitudeControl.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                manager.getPowerLine(column).getInputAdjuster().setCurrentAmplitude((int) Math.round((newValue.intValue() - 64) / 100.0 * baseAmplitude + baseAmplitude));
                manager.getPowerLine(column).updateOscilloscopeData();
            }
        });
        return amplitudeControl;
    }
    
    public static Slider createPhaseControl(int column) {
        Slider phaseControl = makeSlider(0, 127, 64, HORIZONTAL, "Phase");
        double basePhase = manager.getPowerLine(column).getInputAdjuster().getBasePhase();
        phaseControl.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                manager.getPowerLine(column).getInputAdjuster().setCurrentPhase((newValue.intValue() - 64) / 100.0 * Math.PI + basePhase);
                manager.getPowerLine(column).updateOscilloscopeData();
            }
        });
        return phaseControl;
    }
    
    public static StatusLed createStatusLed(int column) {
        StatusLed statusLed = new StatusLed();
        statusLed.setId("statusLed" + column);
        manager.getPowerLine(column).setStatusLed(statusLed);
        return statusLed;
    }
    
    public static ToggleButton createOfflineButton(int column) {
        ToggleButton offlineButton = new ToggleButton("Offline");
        offlineButton.setId("offlineButton" + column);
        offlineButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (offlineButton.isSelected() == true) {
                manager.getPowerLine(column).setOffline();
            }
            if (offlineButton.isSelected() == false) {
                manager.getPowerLine(column).setOnline();
            }
        }));
        manager.getPowerLine(column).setOfflineButton(offlineButton);
        return offlineButton;
    }
    
    public static ToggleButton createShutdownButton(int column) {
        ToggleButton shutdownButton = new ToggleButton("Shutdown");
        shutdownButton.setId("shutdownButton" + column);
        shutdownButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (shutdownButton.isSelected() == true) {
                manager.getPowerLine(column).shutdownProcess();
            }
            if (shutdownButton.isSelected() == false) {
                manager.getPowerLine(column).startupProcess();
            }
        }));
        manager.getPowerLine(column).setShutdownButton(shutdownButton);
        return shutdownButton;
    }
    
    public static VBox createControlButtonFrame(int column) {
        StatusLed statusLed = Main.getStatusLeds()[column];
        ToggleButton offlineButton = Main.getOfflineButtons()[column];
        ToggleButton shutdownButton = Main.getShutdownButtons()[column];
        VBox controlButtonFrame = new VBox(10);
        controlButtonFrame.getChildren().addAll(statusLed, offlineButton, shutdownButton);
        controlButtonFrame.setAlignment(Pos.BOTTOM_CENTER);
        return controlButtonFrame; 
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
        oscilloscope.getChart().setMinHeight(100);
        oscilloscope.getChart().setPrefHeight(120);
        oscilloscope.getChart().setMaxHeight(150);
        GridPane.setRowIndex(oscilloscope, 6);
        GridPane.setColumnIndex(oscilloscope, column);
        return oscilloscope;
    }
    
    public static Gauge createLineOutputGauge(int column, domain.PowerLine powerline) {
        Gauge gauge = GaugeBuilder  
        .create()  
        .minSize(100,100)
        .prefSize(150,150) // Set the preferred size of the control  
        .maxSize(150,150)
        .foregroundBaseColor(Color.BLACK)  // Defines a color foreground elements  
        .title("Line Output") // Set the text for the title  
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
        .ledColor(Color.rgb(255, 0, 0)) // Color of the LED  
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
        .gradientBarStops(new Stop(0.24, Color.LIGHTBLUE),
                          new Stop(0.25, Color.LIGHTGREEN),
                          new Stop(0.40, Color.LIGHTGREEN),
                          new Stop(0.55, Color.YELLOW),
                          new Stop(0.66, Color.ORANGE),
                          new Stop(0.67, Color.ORANGERED),
                          new Stop(0.89, Color.ORANGERED),
                          new Stop(0.91, Color.LIGHTBLUE))
        .sectionsVisible(false)  // Sections will be visible  
        .sections(new Section(100, 120, Color.RED)) // Sections that will be drawn  
        .checkSectionsForValue(false) // Check current value against each section  
        .markersVisible(true) // Markers will be visible  
        .markers(new Marker(50, "Min", Color.YELLOW)) // Markers that will be drawn  
        .animated(true) // Needle will be animated  
        .animationDuration(500)  // Speed of the needle in milliseconds (10 - 10000 ms)  
        .build();
        GridPane.setRowIndex(gauge, 5);
        GridPane.setColumnIndex(gauge, column);
        gauge.setId("gauge" + column);
        gauge.valueProperty().bind(powerline.getOutputPower());
        powerline.setLineOutputGauge(gauge);
        return gauge;     
    }
    
    public static StackPane createBreaker(int line) {
        StackPane breaker = new StackPane();
        Image breakerImage;
        // Image for left breaker
        if (line % 2 == 0) {
            breakerImage = new Image(InitUI.class.getClassLoader().getResource("graphics/leftBreaker.png").toString());
        } else {
        // Image for right breaker
            breakerImage = new Image(InitUI.class.getClassLoader().getResource("graphics/rightBreaker.png").toString());
        }
        ImageView display = new ImageView(breakerImage);
        display.setMouseTransparent(true);
        display.setFitHeight(40.0);
        display.setFitWidth(66.0);
        breaker.setAlignment(Pos.CENTER);
        StatusLed breakerLed = new StatusLed();
        breakerLed.setMouseTransparent(true);
        breakerLed.setAlignment(Pos.BOTTOM_CENTER);
        breakerLed.setTranslateY(-15);
        breakerLed.setStatus("ok");          
        breaker.getChildren().addAll(display, breakerLed);
        GridPane.setRowIndex(breaker, 4);
        GridPane.setColumnIndex(breaker, line);
        breaker.setTranslateY(10);
        if (line % 2 == 0) {
            manager.getPowerChannel(Math.floorDiv(line, 2)).getLeftBreaker().setBreakerLight(breakerLed);
            breaker.setTranslateX(30);
        } else {
            manager.getPowerChannel(Math.floorDiv(line, 2)).getRightBreaker().setBreakerLight(breakerLed);
            breaker.setTranslateX(-30);
        }
        return breaker;
    }
    
    public static Button createBreakerButton(int line) {
        Image breakerButtonIcon;
        // Image for left breaker button
        if (line % 2 == 0) {
            breakerButtonIcon = new Image(InitUI.class.getClassLoader().getResource("graphics/leftBreakerButton.png").toString());
        } else {
        // Image for right breaker button
            breakerButtonIcon = new Image(InitUI.class.getClassLoader().getResource("graphics/rightBreakerButton.png").toString());
        }
        Button breakerButton = new Button();
        breakerButton.setDisable(true);
        breakerButton.setAlignment(Pos.CENTER);
        ImageView breakerButtonView = new ImageView(breakerButtonIcon);
        breakerButtonView.setFitHeight(20.0);
        breakerButtonView.setFitWidth(41.0);
        breakerButton.setGraphic(breakerButtonView);
        breakerButton.setId("breakerButton" + line);
        breakerButton.setMaxSize(43, 22);
        breakerButton.setMinSize(43, 22);
        if (line % 2 == 0) {
            breakerButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    manager.getPowerChannel(Math.floorDiv(line, 2)).getLeftBreaker().resetBreaker();
                }
            });
        } else {
            breakerButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    manager.getPowerChannel(Math.floorDiv(line, 2)).getRightBreaker().resetBreaker();
                }
            });
        }
        GridPane.setRowIndex(breakerButton, 4);
        GridPane.setColumnIndex(breakerButton, line);
        breakerButton.toFront();
        if (line % 2 == 0) {
            manager.getPowerChannel(Math.floorDiv(line, 2)).getLeftBreaker().setBreakerButton(breakerButton);
            breakerButton.setTranslateX(30);
        } else {
            manager.getPowerChannel(Math.floorDiv(line, 2)).getRightBreaker().setBreakerButton(breakerButton);
            breakerButton.setTranslateX(-30);
        }
        return breakerButton;
    }
    
    public static Gauge createTempGauge(int column, domain.PowerChannel powerchannel) {
        Gauge tempGauge = GaugeBuilder  
        .create()  
        .minSize(50,50)
        .prefSize(75,75) // Set the preferred size of the control  
        .foregroundBaseColor(Color.BLACK)  // Defines a color foreground elements  
        .title("Temp") // Set the text for the title  
        .titleColor(Color.BLACK) // Define the color for the title text       
        .unit("") // Set the text for the unit  
        .unitColor(Color.TRANSPARENT) // Define the color for the unit  
        .valueColor(Color.TRANSPARENT) // Define the color for the value text  
        .decimals(0) // Set the number of decimals for the value/lcd text  
        .minValue(0) // Set the start value of the scale  
        .maxValue(1000) // Set the end value of the scale  
        .startAngle(330) // Start angle of your scale (bottom -> 0, direction -> CCW)  
        .angleRange(300) // Angle range of your scale starting from the start angle    
        .tickLabelColor(Color.TRANSPARENT) // Color for tick labels  
        .majorTickMarksVisible(true) // Major tick marks should be visible  
        .majorTickMarkType(TickMarkType.LINE) // LINE, DOT, TRIANGLE, TICK_LABEL  
        .majorTickMarkColor(Color.BLACK) // Color for the major tick marks  
        .mediumTickMarksVisible(false) // Medium tick marks should be visible   
        .minorTickMarksVisible(false) // Minor tick marks should be visible  
        .needleColor(Color.CRIMSON) // Color of the needle  
        .knobType(Gauge.KnobType.STANDARD) // STANDARD, METAL, PLAIN, FLAT  
        .knobColor(Color.LIGHTGRAY) // Color that should be used for the center knob  
        .thresholdVisible(true) // Threshold indicator should be visible  
        .threshold(800) // Value for the threshold  
        .thresholdColor(Color.GOLD) // Color for the threshold  
        .checkThreshold(false) // Check each value against threshold  
        .onThresholdExceeded(thresholdEvent -> System.out.println("Threshold exceeded"))  
        .onThresholdUnderrun(thresholdEvent -> System.out.println("Threshold underrun"))    
        .gradientBarEnabled(true) // Gradient filled bar should be visible  
        .gradientBarStops(new Stop(0.10, Color.LIGHTBLUE),
                          new Stop(0.16, Color.LIGHTGREEN),
                          new Stop(0.35, Color.LIGHTGREEN),
                          new Stop(0.52, Color.YELLOW),
                          new Stop(0.68, Color.ORANGE),
                          new Stop(0.76, Color.ORANGERED),
                          new Stop(0.89, Color.ORANGERED),
                          new Stop(0.91, Color.LIGHTBLUE))
        .sectionsVisible(false)  // Sections will be visible  
        .sections(new Section(100, 120, Color.RED)) // Sections that will be drawn  
        .checkSectionsForValue(false) // Check current value against each section  
        .markersVisible(false) // Markers will be visible  
        .animated(true) // Needle will be animated  
        .animationDuration(500)  // Speed of the needle in milliseconds (10 - 10000 ms)  
        .build();
        GridPane.setRowIndex(tempGauge, 4);
        GridPane.setColumnIndex(tempGauge, column);
        tempGauge.setId("tempGauge" + column);
        tempGauge.setMouseTransparent(true);
        if (column% 2 == 0) {
            tempGauge.valueProperty().bind(powerchannel.getLeftBreaker().getBreakerHeat());
            tempGauge.setTranslateX(-60);
        } else {
            tempGauge.valueProperty().bind(powerchannel.getRightBreaker().getBreakerHeat());
            tempGauge.setTranslateX(60);
        }
        return tempGauge;     
    }
    
    
    public static Slider createBalanceControl(int channel) {
        Slider balanceControl = makeSlider(0, 127, 64, HORIZONTAL, "Balance");        
        balanceControl.setPadding(new Insets(0, 0, 5, 0));
        balanceControl.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                manager.getPowerChannel(channel).setBalancerValue(new SimpleIntegerProperty(newValue.intValue() - 64));
                manager.getPowerChannel(channel).updateOutput();
            }
        });
        return balanceControl;
    }
    
    public static VBox createBalanceControlBlock(int channel) {
        Slider balanceControl = Main.getBalanceControls()[channel];
        Label balanceLabel = new Label("Balance");
        balanceLabel.setLabelFor(balanceControl);
        VBox balanceControlBlock = new VBox();
        balanceControlBlock.getChildren().addAll(balanceControl, balanceLabel);
        balanceControlBlock.setAlignment(Pos.TOP_CENTER);
        balanceControlBlock.setMaxHeight(130);
        GridPane.setRowIndex(balanceControlBlock, 5);
        GridPane.setColumnIndex(balanceControlBlock, channel * 2);
        GridPane.setColumnSpan(balanceControlBlock, 2);
        return balanceControlBlock;
    }
    
    public static Gauge createBalanceGauge(int channel, domain.PowerChannel powerchannel) {
        Gauge gauge = GaugeBuilder  
        .create()
        .skinType(Gauge.SkinType.HORIZONTAL)
        .minSize(100,50) 
        .prefSize(150,75) // Set the preferred size of the control  
        .foregroundBaseColor(Color.BLACK)  // Defines a color foreground elements  
        .title("Balance") // Set the text for the title  
        .titleColor(Color.BLACK) // Define the color for the title text       
        .unit("") // Set the text for the unit  
        .unitColor(Color.BLACK) // Define the color for the unit  
        .valueColor(Color.TRANSPARENT) // Define the color for the value text  
        .decimals(0) // Set the number of decimals for the value/lcd text  
        .minValue(-50) // Set the start value of the scale  
        .maxValue(50) // Set the end value of the scale  
        .startAngle(270) // Start angle of your scale (bottom -> 0, direction -> CCW)  
        .angleRange(180) // Angle range of your scale starting from the start angle    
        .tickLabelColor(Color.TRANSPARENT) // Color for tick labels  
        .majorTickMarksVisible(true) // Major tick marks should be visible  
        .majorTickMarkType(TickMarkType.LINE) // LINE, DOT, TRIANGLE, TICK_LABEL  
        .majorTickMarkColor(Color.BLACK) // Color for the major tick marks  
        .mediumTickMarksVisible(false) // Medium tick marks should be visible  
        .mediumTickMarkType(TickMarkType.LINE) // LINE, DOT, TRIANGLE  
        .mediumTickMarkColor(Color.BLACK) // Color for the medium tick marks  
        .minorTickMarksVisible(false) // Minor tick marks should be visible  
        .minorTickMarkType(TickMarkType.LINE) // LINE, DOT, TRIANGLE  
        .minorTickMarkColor(Color.BLACK) // Color for minor tick marks  
        .ledVisible(true) // LED should be visible  
        .ledColor(Color.rgb(255, 0, 0)) // Color of the LED  
        .ledBlinking(false) // LED should blink  
        .needleColor(Color.CRIMSON) // Color of the needle  
        .knobType(Gauge.KnobType.STANDARD) // STANDARD, METAL, PLAIN, FLAT  
        .knobColor(Color.LIGHTGRAY) // Color that should be used for the center knob      
        .gradientBarEnabled(false) // Gradient filled bar should be visible  
        .gradientBarStops(new Stop(-0.99, Color.ORANGERED),
                          new Stop(-0.60, Color.YELLOW),
                          new Stop(-0.25, Color.TRANSPARENT),
                          new Stop(0.25, Color.TRANSPARENT),
                          new Stop(0.60, Color.YELLOW),
                          new Stop(0.99, Color.ORANGERED))
        .sectionsVisible(true)  // Sections will be visible  
        .sections(new Section(-15, 15, Color.LIGHTGREEN), new Section(-25, -15, Color.LIGHTYELLOW), new Section(15, 25, Color.LIGHTYELLOW), new Section(-50, -25, Color.PINK), new Section(25, 50, Color.PINK)) // Sections that will be drawn  
        .checkSectionsForValue(false) // Check current value against each section  
        .markersVisible(false) // Markers will be visible  
        .markers(new Marker(50, "Min", Color.YELLOW)) // Markers that will be drawn  
        .animated(true) // Needle will be animated  
        .animationDuration(500)  // Speed of the needle in milliseconds (10 - 10000 ms)  
        .build();
        gauge.setId("balanceGauge" + channel);
        gauge.valueProperty().bind(powerchannel.getOutputBalance());
        gauge.setMouseTransparent(true);
        GridPane.setRowIndex(gauge, 4);
        GridPane.setColumnIndex(gauge, channel * 2);
        GridPane.setColumnSpan(gauge, 2);
        powerchannel.setBalanceGauge(gauge);
        return gauge;     
    }
    
    public static Gauge createChannelOutputGauge(int channel, domain.PowerChannel powerchannel) {   
        Gauge channelGauge = GaugeBuilder  
        .create()
        .minSize(100,100)
        .prefSize(150,150) // Set the preferred size of the control  
        .maxSize(150,150)
        .foregroundBaseColor(Color.BLACK)  // Defines a color foreground elements  
        .title("Channel Output") // Set the text for the title  
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
        .ledVisible(false) // LED should be visible  
        .needleColor(Color.CRIMSON) // Color of the needle  
        .knobType(Gauge.KnobType.STANDARD) // STANDARD, METAL, PLAIN, FLAT  
        .knobColor(Color.LIGHTGRAY) // Color that should be used for the center knob  
        .thresholdVisible(true) // Threshold indicator should be visible  
        .threshold(100) // Value for the threshold  
        .thresholdColor(Color.RED) // Color for the threshold  
        .checkThreshold(false) // Check each value against threshold  
        .onThresholdExceeded(thresholdEvent -> System.out.println("Threshold exceeded"))  
        .onThresholdUnderrun(thresholdEvent -> System.out.println("Threshold underrun"))    
        .gradientBarEnabled(false) // Gradient filled bar should be visible  
        .animated(true) // Needle will be animated  
        .animationDuration(500)  // Speed of the needle in milliseconds (10 - 10000 ms)  
        .build();
     GridPane.setRowIndex(channelGauge, 3);
     GridPane.setColumnIndex(channelGauge, channel * 2);
     GridPane.setColumnSpan(channelGauge, 2);
     channelGauge.setTranslateY(30);
     channelGauge.setId("channelOutputGauge" + channel);
     channelGauge.valueProperty().bind(powerchannel.getOutputPower());
     return channelGauge;   
    }
    
    public static HBox createMainOutputControls(domain.Manager manager, int channels) {
        HBox outputGauges = new HBox(40);
        outputGauges.setAlignment(Pos.CENTER);
        outputGauges.setPadding(new Insets(10, 150, 10, 150));
        for (int i = 0; i < channels; i++) {
            HBox outputBox = new HBox(0);
            Slider adjuster = Main.getOutputAdjusters()[i];
            adjuster.setTranslateY(15);
            Gauge gauge = createOutputGauge(manager, i);
            manager.setOutputGauge(i, gauge);
            outputBox.getChildren().addAll(gauge, adjuster);
            outputBox.setAlignment(Pos.CENTER);
            VBox labeledOutputBox = new VBox(-30);
            Label outputLabel = new Label("Output " + (i + 1));
            outputLabel.maxHeight(10);
            outputLabel.setTranslateY(-5);
            outputLabel.setStyle("-fx-font-size: 0.8em;");
            labeledOutputBox.setAlignment(Pos.CENTER);
            labeledOutputBox.getChildren().addAll(outputBox, outputLabel);
            outputGauges.getChildren().add(labeledOutputBox);
        }
        outputGauges.setAlignment(Pos.CENTER);
        outputGauges.setTranslateY(-15);
        GridPane.setRowIndex(outputGauges, 2);
        GridPane.setColumnIndex(outputGauges, 1);
        GridPane.setRowSpan(outputGauges, 2);
        GridPane.setColumnSpan(outputGauges, 2);
        return outputGauges;
    }
    
    public static Gauge createOutputGauge(domain.Manager manager, int number) {
        Gauge gauge = GaugeBuilder
                .create()
                .skinType(Gauge.SkinType.LINEAR)
                .minSize(60,240)
                .prefSize(60,240)
                .maxSize(60,240)
                .foregroundBaseColor(Color.BLACK) // Defines a color foreground elements  
                .title("") // Set the text for the title  
                .titleColor(Color.BLACK) // Define the color for the title text       
                .unit("") // Set the text for the unit  
                .unitColor(Color.TRANSPARENT) // Define the color for the unit  
                .valueColor(Color.TRANSPARENT) // Define the color for the value text  
                .decimals(0) // Set the number of decimals for the value/lcd text  
                .minValue(0) // Set the start value of the scale  
                .maxValue(150) // Set the end value of the scale  
                .tickLabelColor(Color.BLACK) // Color for tick labels  
                .majorTickMarksVisible(true) // Major tick marks should be visible  
                .majorTickMarkType(TickMarkType.LINE) // LINE, DOT, TRIANGLE, TICK_LABEL  
                .majorTickMarkColor(Color.BLACK) // Color for the major tick marks  
                .mediumTickMarksVisible(true) // Medium tick marks should be visible  
                .mediumTickMarkType(TickMarkType.LINE) // LINE, DOT, TRIANGLE  
                .mediumTickMarkColor(Color.BLACK) // Color for the medium tick marks  
                .minorTickMarksVisible(false) // Minor tick marks should be visible  
                .minorTickMarkType(TickMarkType.LINE) // LINE, DOT, TRIANGLE  
                .minorTickMarkColor(Color.BLACK) // Color for minor tick marks  
                .ledVisible(false) // LED should be visible  
                .ledColor(Color.rgb(255, 0, 0)) // Color of the LED  
                .ledBlinking(false) // LED should blink  
                .needleColor(Color.CRIMSON) // Color of the needle  
                .knobType(Gauge.KnobType.STANDARD) // STANDARD, METAL, PLAIN, FLAT  
                .knobColor(Color.CRIMSON) // Color that should be used for the center knob      
                .sectionsVisible(true) // Sections will be visible  
                .sections(new Section(100, 150, Color.RED)) // Sections that will be drawn  
                .checkSectionsForValue(false) // Check current value against each section  
                .markersVisible(false) // Markers will be visible  
                .markers(new Marker(50, "Min", Color.YELLOW)) // Markers that will be drawn  
                .animated(true) // Needle will be animated  
                .animationDuration(500) // Speed of the needle in milliseconds (10 - 10000 ms)  
                .build();
        gauge.setId("OutputGauge" + number);
        gauge.valueProperty().bind(manager.getOutputValue(number));
        gauge.setPadding(new Insets(0, 0, 0, 0));
        gauge.setMouseTransparent(true);
        return gauge;
    }
    
    public static Slider createOutputAdjuster(domain.Manager manager, int number) {
        Slider outputAdjuster = makeSlider(0, 120, 100, VERTICAL, "Output " + number);
        outputAdjuster.setShowTickLabels(true);
        outputAdjuster.setShowTickMarks(true);
        outputAdjuster.setMajorTickUnit(100);
        outputAdjuster.setMinorTickCount(10);
        outputAdjuster.setSnapToTicks(false);
        outputAdjuster.setMinSize(30,143);
        outputAdjuster.setPrefSize(30,143);
        outputAdjuster.setMaxSize(30,143);
        outputAdjuster.setPadding(new Insets(0, 0, 0, 0));
        outputAdjuster.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                manager.setOutputAdjusterValue(number, newValue.intValue());
                manager.calculateOutputValues();
                manager.getPowerChannel(number).getLeftBreaker().calculateHeatDelta();
                manager.getPowerChannel(number).getRightBreaker().calculateHeatDelta();
            }
        });
        return outputAdjuster;
    }
    
    public static StackPane createMainOutputDisplay(domain.Manager manager) {
        Label output = new Label();
        output.setMinSize(100,40);
        output.setPrefSize(100,40);
        output.setMaxSize(100,40);
        output.setAlignment(Pos.CENTER);
        output.textProperty().bind(manager.getMainOutputLevel().asString("%.2f"));
        output.setId("MainOutputDisplay");    
        output.setStyle("-fx-font-size: 2em; -fx-font-weight: bold;");
        Rectangle frame = new Rectangle();
        frame.setWidth(100);
        frame.setHeight(40);
        frame.setStroke(Color.LIGHTGRAY);
        frame.setStrokeWidth(2.0);
        frame.setFill(Color.TRANSPARENT);
        StackPane outputDisplay = new StackPane();
        outputDisplay.setAlignment(Pos.CENTER);
        outputDisplay.getChildren().addAll(output, frame);
        GridPane.setRowIndex(outputDisplay, 1);
        GridPane.setColumnIndex(outputDisplay, 1);
        GridPane.setColumnSpan(outputDisplay, 2);
        outputDisplay.setMouseTransparent(true);
        return outputDisplay;
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
            slider.setMinWidth(80);
            slider.setPrefWidth(100);
            slider.setMaxWidth(120);
        } else {
            slider.setMinHeight(120);
            slider.setPrefHeight(150);
            slider.setMaxHeight(200);
        }
        return slider;
    }    
}
