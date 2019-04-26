/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import com.sun.javafx.css.Stylesheet;
import domain.Manager;
import eu.hansolo.medusa.Gauge;
import java.io.File;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import static javafx.geometry.Orientation.HORIZONTAL;
import static javafx.geometry.Orientation.VERTICAL;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 *
 * @author Ville
 */
public class Main extends Application {
    private static final Logger ERRORLOGGER = Logger.getLogger(Main.class.getName());
    static int lines;
    static domain.Manager powerManager;
    static GridPane managerPane;
    static Slider[] frequencyControls;
    static Slider[] amplitudeControls;
    static Slider[] phaseControls;
    static StatusLed[] statusLeds;
    static ToggleButton[] offlineButtons;
    static ToggleButton[] shutdownButtons;
    static VBox[] controlButtonFrames;
    static BorderPane[] powerLineControlBlocks;
    static Oscilloscope[] oscilloscopes;
    static Gauge[] lineOutputGauges;
    static StackPane[] breakers;
    static Button[] breakerButtons;
    static Gauge[] tempGauges;
    static Slider[] balanceControls;
    static VBox[] balanceControlBlocks;
    static Gauge[] balanceGauges;
    static Label[] channelOutputGauges;
    
    /*
    @Override
    public void init() throws Exception {
        
    }
    */
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        lines = 4;
        // Create Power Manager
        powerManager = new domain.Manager();
        powerManager.createPowerLines(lines);
        powerManager.createPowerChannels(lines / 2);
        powerManager.startReactorService();
        powerManager.startHeatService();
        // Create user interface
        primaryStage.setTitle("Power Management Application");
        managerPane = createUserInterface(lines);
        Scene scene = new Scene(managerPane, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
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
        channelOutputGauges = new Label[lines / 2];
        generatePowerLineControls(lines);
        generatePowerChannelControls(lines / 2);
        return managerPane;
    }
    
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
    
    
    /*
    @Override
    public void stop() throws Exception {
        
    }
    */
    
    public static void main(String[] args) {
    
        launch(args);
       
    }
    
    // Getters

    public static Manager getPowerManager() {
        return powerManager;
    }

    public static GridPane getManagerPane() {
        return managerPane;
    }

    public static BorderPane[] getPowerLineControlBlocks() {
        return powerLineControlBlocks;
    }

    public static Slider[] getFrequencyControls() {
        return frequencyControls;
    }

    public static Slider[] getAmplitudeControls() {
        return amplitudeControls;
    }

    public static Slider[] getPhaseControls() {
        return phaseControls;
    }

    public static VBox[] getControlButtonFrames() {
        return controlButtonFrames;
    }

    public static StatusLed[] getStatusLeds() {
        return statusLeds;
    }
    
    public static ToggleButton[] getOfflineButtons() {
        return offlineButtons;
    }

    public static ToggleButton[] getShutdownButtons() {
        return shutdownButtons;
    }
    
    public static Oscilloscope[] getOscilloscopes() {
        return oscilloscopes;
    }
    
    public static Gauge[] getLineOutputGauges() {
        return lineOutputGauges;
    }
    
    public static StackPane[] getBreakers() {
        return breakers;
    }
    
    public static Slider[] getBalanceControls() {
        return balanceControls;
    }
    
    public static VBox[] getBalanceControlBlocks() {
        return balanceControlBlocks;
    }
    
    public static Gauge[] getBalanceGauges() {
        return balanceGauges;
    }
    
    public static Label[] getChannelOutputGauges() {
        return channelOutputGauges;
    }
    
}
