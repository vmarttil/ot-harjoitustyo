/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import domain.Manager;
import eu.hansolo.medusa.Gauge;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The main class calls the initialisation methods for both the program logic and UI
 * components, located in their own classes, stores references to all of 
 * the UI elements and spawns the UI.
 * @author Ville
 */
public class Main extends Application {
    private static int lines;
    private static domain.Manager powerManager;
    private static GridPane managerPane;
    private static Slider[] frequencyControls;
    private static Slider[] amplitudeControls;
    private static Slider[] phaseControls;
    private static StatusLed[] statusLeds;
    private static ToggleButton[] offlineButtons;
    private static ToggleButton[] shutdownButtons;
    private static VBox[] controlButtonFrames;
    private static BorderPane[] powerLineControlBlocks;
    private static Oscilloscope[] oscilloscopes;
    private static Gauge[] lineOutputGauges;
    private static StackPane[] breakers;
    private static Button[] breakerButtons;
    private static Gauge[] tempGauges;
    private static Slider[] balanceControls;
    private static VBox[] balanceControlBlocks;
    private static Gauge[] balanceGauges;
    private static Gauge[] channelOutputGauges;
    private static Slider[] outputAdjusters;
    private static Gauge[] outputGauges;
    private static StackPane mainOutputDisplay;
    
    /*
    @Override
    public void init() throws Exception {
        
    }
    */
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        lines = 4;
        // Create Power Manager
        powerManager = new domain.Manager(lines);
        powerManager.createPowerLines(lines);
        powerManager.createPowerChannels(lines / 2);
        powerManager.createMainOutputs(lines / 2);
        // Create user interface
        primaryStage.setTitle("Power Management Application");
        managerPane = createUserInterface(lines);
        Scene scene = new Scene(managerPane, 1200, 800);        
        primaryStage.setScene(scene);
        powerManager.startReactorService();
        powerManager.startHeatService();
        primaryStage.show();
    }
    
    /**
     * The method creates arrays for storing references to all of the UI 
     * elements and calls methods that generate the UI elements and store 
     * references to them in the arrays for future use.
     * @param lines the number of power lines in the power manager to be created
     * @return the user interface pane of the application
     */
    private GridPane createUserInterface(int lines) {
        managerPane = InitUI.createManagerPane(lines);
        frequencyControls = new Slider[lines];
        amplitudeControls = new Slider[lines];
        phaseControls = new Slider[lines];
        controlButtonFrames = new VBox[lines];
        statusLeds = new StatusLed[lines];
        offlineButtons = new ToggleButton[lines];
        shutdownButtons = new ToggleButton[lines];
        powerLineControlBlocks = new BorderPane[lines];
        oscilloscopes = new Oscilloscope[lines];
        lineOutputGauges = new Gauge[lines];
        breakers = new StackPane[lines];
        breakerButtons = new Button[lines];
        tempGauges = new Gauge[lines];
        balanceControls = new Slider[lines / 2];
        balanceControlBlocks = new VBox[lines / 2];        
        balanceGauges = new Gauge[lines / 2];
        channelOutputGauges = new Gauge[lines / 2];
        outputAdjusters = new Slider[lines / 2];
        outputGauges = new Gauge[lines / 2];
        generatePowerLineControls(lines);
        generatePowerChannelControls(lines / 2);
        generateMainOutputControls(lines / 2);
        return managerPane;
    }
    
    /**
     * The method calls the initialisation methods for the UI elements related 
     * to the power lines, adds references to them into the arrays and adds them 
     * as children to the main UI pane.
     * @param lines the number of power lines in the power manager
     */
    private void generatePowerLineControls(int lines) {
        for (int i = 0; i < lines; i++) {
            frequencyControls[i] = InitUI.createFrequencyControl(i);
            amplitudeControls[i] = InitUI.createAmplitudeControl(i);
            phaseControls[i] = InitUI.createPhaseControl(i);
            statusLeds[i] = InitUI.createStatusLed(i);
            offlineButtons[i] = InitUI.createOfflineButton(i);
            shutdownButtons[i] = InitUI.createShutdownButton(i);
            controlButtonFrames[i] = InitUI.createControlButtonFrame(i);
            powerLineControlBlocks[i] = InitUI.createPowerLineControls(i);
            oscilloscopes[i] = InitUI.createOscilloscope(i, powerManager.getPowerLine(i));
            lineOutputGauges[i] = InitUI.createLineOutputGauge(i, powerManager.getPowerLine(i));
            managerPane.getChildren().add(powerLineControlBlocks[i]);
            managerPane.getChildren().add(oscilloscopes[i]);
            managerPane.getChildren().add(lineOutputGauges[i]);
        }
    }
    
    /**
     * The method calls the initialisation methods for the UI elements related 
     * to the power channels, adds references to them into the arrays and adds 
     * them as children to the main UI pane.
     * @param channels the number of power channels in the power manager
     */
    private void generatePowerChannelControls(int channels) {
        for (int i = 0; i < channels; i++) {
            breakers[2 * i] = InitUI.createBreaker(2 * i);
            breakers[2 * i + 1] = InitUI.createBreaker(2 * i + 1);
            breakerButtons[2 * i] = InitUI.createBreakerButton(2 * i);
            breakerButtons[2 * i + 1] = InitUI.createBreakerButton(2 * i + 1);
            tempGauges[2 * i] = InitUI.createTempGauge(2 * i, powerManager.getPowerChannel(i));
            tempGauges[2 * i + 1] = InitUI.createTempGauge(2 * i + 1, powerManager.getPowerChannel(i));
            balanceControls[i] = InitUI.createBalanceControl(i);
            balanceControlBlocks[i] = InitUI.createBalanceControlBlock(i);
            balanceGauges[i] = InitUI.createBalanceGauge(i, powerManager.getPowerChannel(i));
            channelOutputGauges[i] = InitUI.createChannelOutputGauge(i, powerManager.getPowerChannel(i));
            managerPane.getChildren().add(balanceControlBlocks[i]);
            managerPane.getChildren().add(breakers[2 * i]);
            managerPane.getChildren().add(breakers[2 * i + 1]);
            managerPane.getChildren().add(breakerButtons[2 * i]);
            managerPane.getChildren().add(breakerButtons[2 * i + 1]);
            managerPane.getChildren().add(tempGauges[2 * i]);
            managerPane.getChildren().add(tempGauges[2 * i + 1]);
            managerPane.getChildren().add(balanceGauges[i]);
            managerPane.getChildren().add(channelOutputGauges[i]);
        }
    }
    
    /**
     * The method calls the initialisation methods for the UI elements related 
     * to the main outputs and adds them as children to the main UI pane.
     * @param channels the number of power channels in the power manager
     */
    private void generateMainOutputControls(int channels) {
        for (int i = 0; i < channels; i++) {
            outputAdjusters[i] = InitUI.createOutputAdjuster(powerManager, i);
            outputGauges[i] = InitUI.createOutputGauge(powerManager, i);
        }
        HBox mainOutputControl = InitUI.createMainOutputControls(powerManager, channels);
        this.mainOutputDisplay = InitUI.createMainOutputDisplay(powerManager);
        managerPane.getChildren().addAll(mainOutputControl, mainOutputDisplay);
    }
    
    /*
    @Override
    public void stop() throws Exception {
        
    }
    */
    
    public static void main(String[] args) {
    
        launch(args);
       
    }
    
    /**
     * The methods returns the power manager object responsible for creating, 
     * coordinating and interlinking the application logic components 
     * @return the main power manager object
     */
    public static Manager getPowerManager() {
        return powerManager;
    }

    /**
     * The methods returns the main UI pane of the application, containing all 
     * of the UI elements as its children.
     * @return the main UI pane object
     */
    public static GridPane getManagerPane() {
        return managerPane;
    }

    /**
     * The method returns an array containing references to the UI control 
     * blocks of each power line.
     * @return array of UI control blocks
     */
    public static BorderPane[] getPowerLineControlBlocks() {
        return powerLineControlBlocks;
    }

    /**
     * The method returns an array containing references to the frequency 
     * controls of each power line.
     * @return array of frequency control sliders
     */
    public static Slider[] getFrequencyControls() {
        return frequencyControls;
    }

    /**
     * The method returns an array containing references to the amplitude 
     * controls of each power line.
     * @return array of amplitude control sliders
     */
    public static Slider[] getAmplitudeControls() {
        return amplitudeControls;
    }

    /**
     * The method returns an array containing references to the phase
     * controls of each power line.
     * @return array of phase control sliders
     */
    public static Slider[] getPhaseControls() {
        return phaseControls;
    }

    /**
     * The method returns an array containing references to the control button 
     * frames of each power line.
     * @return array of control button frames as VBox objects
     */
    public static VBox[] getControlButtonFrames() {
        return controlButtonFrames;
    }

    /**
     * The method returns an array containing references to the status leds 
     * of each power line.
     * @return array of status leds as StatusLed objects
     */
    public static StatusLed[] getStatusLeds() {
        return statusLeds;
    }
    
    /**
     * The method returns an array containing references to the offline switches
     * of each power line.
     * @return array of online/offline toggle buttons
     */
    public static ToggleButton[] getOfflineButtons() {
        return offlineButtons;
    }

    /**
     * The method returns an array containing references to the shutdown switches
     * of each power line.
     * @return array of shutdown toggle buttons
     */
    public static ToggleButton[] getShutdownButtons() {
        return shutdownButtons;
    }
    
    /**
     * The method returns an array containing references to the oscilloscope 
     * displays of each power line as Oscilloscope objects.
     * @return array of oscilloscope displays 
     */
    public static Oscilloscope[] getOscilloscopes() {
        return oscilloscopes;
    }
    
    /**
     * The method returns an array containing references to the power output
     * gauges of each power line as Gauge objects.
     * @return array of power output gauges
     */
    public static Gauge[] getLineOutputGauges() {
        return lineOutputGauges;
    }
    
    /**
     * The method returns an array containing references to the power breaker
     * UI elements of each power channel as StackPane objects.
     * @return array of power breakers
     */
    public static StackPane[] getBreakers() {
        return breakers;
    }
    
    /**
     * The method returns an array containing references to the temperature
     * gauges of each power channel as Gauge objects.
     * @return array of temperature gauges
     */
    public static Gauge[] getTempGauges() {
        return tempGauges;
    }
    
    /**
     * The method returns an array containing references to the power balance
     * controls of each power channel as Slider objects.
     * @return array of balance sliders
     */
    public static Slider[] getBalanceControls() {
        return balanceControls;
    }
    
    /**
     * The method returns an array containing references to the power balance
     * control blocks of each power channel as VBox objects.
     * @return array of balance control blocks
     */
    public static VBox[] getBalanceControlBlocks() {
        return balanceControlBlocks;
    }
    
    /**
     * The method returns an array containing references to the balance
     * gauges of each power channel as Gauge objects.
     * @return array of balance gauges
     */
    public static Gauge[] getBalanceGauges() {
        return balanceGauges;
    }
    
    /**
     * The method returns an array containing references to the power output
     * gauges of each power channel as Gauge objects.
     * @return array of temperature gauges
     */
    public static Gauge[] getChannelOutputGauges() {
        return channelOutputGauges;
    }
    
    /**
     * The method returns an array containing references to the output
     * adjusters of each output and Slider objects.
     * @return array of adjuster sliders
     */
    public static Slider[] getOutputAdjusters() {
        return outputAdjusters;
    }
    
    /**
     * The method returns an array containing references to the adjusted output
     * gauges of each output as Gauge objects.
     * @return array of output gauges
     */
    public static Gauge[] getOutputGauges() {
        return outputGauges;
    }
    
}
