/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import domain.Manager;
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
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
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
    private static final Logger errorLogger = Logger.getLogger(Main.class.getName());
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
    static Label[] outputGauges;
    /*
    @Override
    public void init() throws Exception {
        
    }
    */
    
    @Override
    public void start(Stage primaryStage) throws Exception {
//        Application.setUserAgentStylesheet(getClass().getResource("PowerManagement.css").toExternalForm());
        lines = 4;
        // Create Power Manager
        powerManager = new domain.Manager();
        // Create Power Lines
        for (int i = 0; i < lines; i++) {
            domain.PowerLine line = new domain.PowerLine(powerManager, i);
            powerManager.getPowerLines()[i] = line;
        }
        powerManager.startReactorService(10);
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
        outputGauges = new Label[lines];
        generatePowerLineControls(lines);
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
            outputGauges[i] = InitUI.createOutputGauge(i, powerManager.getPowerLine(i));
            managerPane.getChildren().add(powerLineControlBlocks[i]);
            managerPane.getChildren().add(oscilloscopes[i]);
            managerPane.getChildren().add(outputGauges[i]);
        }
    }
    
    /*
    @Override
    public void stop() throws Exception {
        
    }
    */
    
    public static void main(String[] args) {
    
        launch(args);
        
        /*
        domain.Oscillator osc1 = new domain.Oscillator(1000, 100);
        domain.Fluctuator flux1 = new domain.Fluctuator(osc1, 80, 10);
    
        
        for (int i = 0; i < 20; i++) {
            System.out.println("");
            System.out.println("Frequency: " + osc1.getCurrentFrequency());
            System.out.println("Amplitude: " + osc1.getCurrentAmplitude());
            System.out.println("Phase: " + osc1.getCurrentPhase());
        
            flux1.fluctuateAll();
        }
    */
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
    
    
    
    
    
    
    
    
    
}
