/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.LcdDesign;
import eu.hansolo.medusa.LcdFont;
import eu.hansolo.medusa.Marker;
import eu.hansolo.medusa.Section;
import eu.hansolo.medusa.TickMarkType;
import eu.hansolo.medusa.skins.ModernSkin;
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

/**
 *
 * @author Ville
 */

public class InitUI {

    public static GridPane createManagerPane(int columns) {
        GridPane managerPane = new GridPane();
        // Position the manager pane at the center of the screen, both vertically and horizontally
        managerPane.setAlignment(Pos.CENTER);
        // Set a padding of 60px on each side
        managerPane.setPadding(new Insets(30, 30, 30, 30));
        // Set the horizontal gap between columns
        managerPane.setHgap(20);
        // Set the vertical gap between rows
        managerPane.setVgap(10);
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
        
        HBox frequencyBlock = new HBox(-20);
        frequencyBlock.getChildren().addAll(frequencyLabel, frequencyControl);
        frequencyBlock.setAlignment(Pos.CENTER_LEFT);
        frequencyBlock.setMaxHeight(20);
        
        HBox amplitudeBlock = new HBox(-20);
        amplitudeBlock.getChildren().addAll(amplitudeControl, amplitudeLabel);
        amplitudeBlock.setAlignment(Pos.CENTER_RIGHT);
        amplitudeBlock.setMaxHeight(20);
        
        VBox phaseBlock = new VBox();
        phaseBlock.getChildren().addAll(phaseLabel, phaseControl);
        phaseBlock.setAlignment(Pos.CENTER);
        
        controlButtonFrame.setAlignment(Pos.CENTER);
        powerLineControlBlock = formatControlPane(powerLineControlBlock, phaseBlock, frequencyBlock, controlButtonFrame, amplitudeBlock);
        return powerLineControlBlock;
    }
    
    private static BorderPane formatControlPane(BorderPane controlBlock, Node top, Node left, Node center, Node right) {
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
    
    public static Gauge createLineOutputGauge(int column, domain.PowerLine powerline) {
        Gauge gauge = GaugeBuilder  
        .create()  
        .minSize(100,100)
        .prefSize(150,150) // Set the preferred size of the control  
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
        //gauge.setPadding(new Insets(-15, 0, -15, 0));
        gauge.setId("gauge" + column);
        gauge.valueProperty().bind(powerline.getOutputPower());
        if (powerline.getOutputPower().doubleValue() > 100.0) {
                gauge.setLedOn(true);
            } else {
                gauge.setLedOn(false);
            }
        return gauge;     
    }
    
    /*
    public static VBox createPowerChannelControls(int channel) {
        VBox powerChannelControlBlock = new VBox();
        powerChannelControlBlock.setId("powerChannelControls" + channel);
        HBox balanceControlBlock = Main.getBalanceControlBlocks()[channel];
        Gauge balanceGauge = Main.getBalanceGauges()[channel];
        Label channelOutputGauge = Main.getChannelOutputGauges()[channel];
        powerChannelControlBlock.setAlignment(Pos.CENTER);
        GridPane.setRowIndex(powerChannelControlBlock, 4);
        GridPane.setColumnIndex(powerChannelControlBlock, channel * 2);
        GridPane.setColumnSpan(powerChannelControlBlock, 2);
        powerChannelControlBlock.getChildren().addAll(channelOutputGauge, balanceGauge, balanceControlBlock);
        powerChannelControlBlock.setAlignment(Pos.CENTER);
        return powerChannelControlBlock;
    } */
    
    public static StackPane createBreaker(int line) {
        StackPane breaker = new StackPane();
        Image breakerImage;
        Image breakerButtonIcon;
        // Image for left breaker
        if (line % 2 == 0) {
            breakerImage = new Image(InitUI.class.getClassLoader().getResource("graphics/leftBreaker.png").toString());
            breakerButtonIcon = new Image(InitUI.class.getClassLoader().getResource("graphics/leftBreakerButton.png").toString());
        } else {
        // Image for right breaker
            breakerImage = new Image(InitUI.class.getClassLoader().getResource("graphics/rightBreaker.png").toString());
            breakerButtonIcon = new Image(InitUI.class.getClassLoader().getResource("graphics/rightBreakerButton.png").toString());
        }
        ImageView display = new ImageView(breakerImage);
        display.setFitHeight(40.0);
        display.setFitWidth(66.0);
        breaker.setAlignment(Pos.BOTTOM_CENTER);
        StatusLed breakerLed = new StatusLed();
        breakerLed.setAlignment(Pos.BOTTOM_CENTER);
        breakerLed.setStatus("ok");
        ToggleButton breakerButton = new ToggleButton("Offline");
        breakerButton.setAlignment(Pos.BOTTOM_CENTER);
        breakerButton.setTranslateY(11);
        breakerButton.setGraphic(new ImageView(breakerButtonIcon));
        breakerButton.setId("breakerButton" + line);
        breakerButton.setMaxSize(62, 29);
        breakerButton.setMinSize(62, 29);
        if (line % 2 == 0) {
            breakerButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
                if (breakerButton.isSelected() == true) {
                    Main.getPowerManager().getPowerChannel(Math.floorDiv(line, 2)).getLeftBreaker().initialisingBreaker();
                    breakerButton.setMouseTransparent(true);
                }
            }));
        } else {
            breakerButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
                if (breakerButton.isSelected() == true) {
                    Main.getPowerManager().getPowerChannel(Math.floorDiv(line, 2)).getRightBreaker().initialisingBreaker();
                    breakerButton.setMouseTransparent(true);
                }
            }));            
        }
        /*
        Button breakerButton = new Button("Reset");
        breakerButton.setAlignment(Pos.BOTTOM_LEFT);
        breakerButton.setMaxSize(60, 30);
        breakerButton.setDisable(true);
        if (line % 2 == 0) {
            breakerButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    Main.getPowerManager().getPowerChannel(Math.floorDiv(line, 2)).getLeftBreaker().initialisingBreaker();
                    breakerButton.setDisable(true);
                }
            });
        } else {
            breakerButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    Main.getPowerManager().getPowerChannel(Math.floorDiv(line, 2)).getRightBreaker().initialisingBreaker();
                    breakerButton.setDisable(true);
                }
            });    
        }
        */
        breaker.getChildren().addAll(display, breakerLed, breakerButton);
        GridPane.setRowIndex(breaker, 4);
        GridPane.setColumnIndex(breaker, line);
        breaker.setMouseTransparent(true);
        if (line % 2 == 0) {
            Main.getPowerManager().getPowerChannel(Math.floorDiv(line, 2)).getLeftBreaker().setBreakerButton(breakerButton);
            Main.getPowerManager().getPowerChannel(Math.floorDiv(line, 2)).getLeftBreaker().setBreakerLight(breakerLed);
            breaker.setTranslateX(30);
            breakerButton.setTranslateX(30);
        } else {
            Main.getPowerManager().getPowerChannel(Math.floorDiv(line, 2)).getRightBreaker().setBreakerButton(breakerButton);
            Main.getPowerManager().getPowerChannel(Math.floorDiv(line, 2)).getRightBreaker().setBreakerLight(breakerLed);
            breaker.setTranslateX(-30);
            breakerButton.setTranslateX(-30);
        }
        return breaker;
    }
    
    public static Slider createBalanceControl(int channel) {
        Slider balanceControl = makeSlider(0, 127, 64, HORIZONTAL, "Balance");        
        balanceControl.setPadding(new Insets(0, 0, 5, 0));
        balanceControl.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Main.getPowerManager().getPowerChannel(channel).setBalancerValue(new SimpleIntegerProperty(newValue.intValue() - 64));
                Main.getPowerManager().getPowerChannel(channel).updateOutput();
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
        balanceControlBlock.setAlignment(Pos.CENTER);
        balanceControlBlock.setMaxHeight(50);
        GridPane.setRowIndex(balanceControlBlock, 4);
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
        .title("Channel balance") // Set the text for the title  
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
        GridPane.setRowIndex(gauge, 3);
        GridPane.setColumnIndex(gauge, channel * 2);
        GridPane.setColumnSpan(gauge, 2);
        return gauge;     
    }
    
    public static Label createChannelOutputGauge(int channel, domain.PowerChannel powerchannel) {   
     
    Label channelGauge = new Label();
   /*Gauge channelGauge = GaugeBuilder  
     .create()  
     .skinType(Gauge.SkinType.LCD)
     .prefSize(200,100) // Set the preferred size of the control  
     // Related to Foreground Elements  
     .foregroundBaseColor(Color.BLACK)  // Defines a color foreground elements  
     // Related to Title Text  
     .title("Channel power") // Set the text for the title  
     .titleColor(Color.BLACK) // Define the color for the title text       
     // Related to Unit Text  
     .unit("%") // Set the text for the unit  
     .unitColor(Color.BLACK) // Define the color for the unit  
     // Related to Value Text  
     .valueColor(Color.BLACK) // Define the color for the value text  
     .decimals(1) // Set the number of decimals for the value/lcd text  
     // Related to LCD  
     .lcdVisible(false) // Display a LCD instead of the plain value text  
     .lcdDesign(LcdDesign.STANDARD) // Set the design for the LCD  
     .lcdFont(LcdFont.DIGITAL_BOLD) // Set the font for the LCD  
     // Related to scale  
     // Related to Value  
     .animated(false) // Needle will be animated  
     .animationDuration(500)  // Speed of the needle in milliseconds (10 - 10000 ms)  
     .build(); */
     GridPane.setRowIndex(channelGauge, 2);
     GridPane.setColumnIndex(channelGauge, channel * 2);
     GridPane.setColumnSpan(channelGauge, 2);
     channelGauge.setId("channelOutputGauge" + channel);
     channelGauge.textProperty().bind(powerchannel.getOutputPower().asString());
     return channelGauge;   
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
            slider.setMinHeight(150);
            slider.setPrefHeight(150);
            slider.setMaxHeight(200);
        }
        Label sliderLabel = new Label(label);
        sliderLabel.setLabelFor(slider);
        return slider;
    }
    
    
}







