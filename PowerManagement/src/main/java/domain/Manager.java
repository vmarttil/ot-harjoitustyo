/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.Random;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;
import ui.Main;

/**
 * The class defines the power manager which serves as the central hub of the 
 * application logic that creates, coordinates and links the application logic 
 * components of the application and provides the global services that 
 * control the timing of application events.
 * 
 * @author Ville
 */
public class Manager {
    private PowerLine[] powerLines;
    private PowerChannel[] powerChannels;
    private int lines;
    private ReactorService reactorService;
    private int baseReactorPeriod;
    private HeatService heatService;
    private int mainOutputLevel; 
    
    /**
     * The constructor defines the main power manager object and creates 
     * the power lines and channels and the output stage that are responsible  
     * for the application logic.
     * @param lines the number of power lines in the manager
     */
    public Manager(int lines) {
        this.lines = lines;
        this.powerLines = new PowerLine[this.lines];
        this.powerChannels = new PowerChannel[this.lines / 2];  
        this.baseReactorPeriod = 5;
        this.mainOutputLevel = 100;
    }
    
    // Getters
    
    /**
     * The method returns an array containing references to the power lines of 
     * the manager.
     * @return array of power lines as PowerLine objects
     */
    public PowerLine[] getPowerLines() {
        return this.powerLines;
    }
    
    /**
     * The method returns a reference to a single power line object,  
     * based on their order (starting from 0).
     * @param number the number of the power line to be returned
     * @return the power line object indicated by the parameter
     */
    public PowerLine getPowerLine(int number) {
        return this.powerLines[number];
    }

    /**
     * The method returns an array containing references to the power channels of 
     * the manager.
     * @return array of power channels as PowerChannel objects
     */
    public PowerChannel[] getPowerChannels() {
        return this.powerChannels;
    }
    
    /**
     * The method returns a reference to a single power channel object,  
     * based on their order (starting from 0).
     * @param number the number of the power channel to be returned
     * @return the power channel object indicated by the parameter
     */
    public PowerChannel getPowerChannel(int number) {
        return this.powerChannels[number];
    }

    /**
     * The method returns the number of lines in this power manager.
     * @return number of lines
     */
    public int getLines() {
        return this.lines;
    }
    
    /**
     * The method returns the base reactor period which determines the interval 
     * at which the waveform properties of the power input from the reactor to 
     * the power lines fluctuates. 
     * @return the interval of reactor power fluctuation
     */
    public int getBaseReactorPeriod() {
        return this.baseReactorPeriod;
    }
    
    /**
     * The method returns the main output level set for the power manager 
     * [to be relegated to the class representing the main output stage].
     * @return main output power level
     */
    public int getMainOutputLevel() {
        return this.mainOutputLevel;
    }
        
    // Setters
    /**
     * The method sets the base reactor period which determines the interval 
     * at which the waveform properties of the power input from the reactor to 
     * the power lines fluctuates.
     * @param period the interval of fluctuations in seconds
     */
    public void setReactorPeriod(int period) {
        reactorService.setPeriod(Duration.seconds(period));
    }
    
    /**
     * The method sets the main output level set for the power manager 
     * [to be relegated to the class representing the main output stage].
     * @param main output power level
     */
    public void setMainOutputLevel(int level) {
        this.mainOutputLevel = level;
    }
    
    // Creating power lines and channels
    
    /**
     * The method creates a number of new power line objects and stores 
     * references to them in an array.
     * @param lines the number of power lines to create
     */
    public void createPowerLines(int lines) {
        for (int i = 0; i < lines; i++) {
            PowerLine line = new PowerLine(this, i);
            this.powerLines[i] = line;
        }
    }
    
    /**
     * The method creates a number of new power channel objects and stores 
     * references to them in an array.
     * @param lines the number of power lines to create
     */
    public void createPowerChannels(int channels) {
        for (int i = 0; i < channels; i++) {
            PowerChannel channel = new PowerChannel(this, i);
            this.powerChannels[i] = channel;
        }
    }
    
    // Reactor service taking care of timing power fluctuation
    
    /**
     * The method randomly triggers fluctuation in one of the power line 
     * reactor output oscillators.
     * @param lines the number of lines in this power manager
     */
    static public void triggerFluctuation(int lines) {
        Platform.runLater(new Runnable() {
            public void run() {
                Random random = new Random();
                int reactorLine = random.nextInt(lines);
                Main.getPowerManager().getPowerLine(reactorLine).fluctuateLine();
            }
        });
    }
    
    /**
     * The method creates an instance of a reactor timing service, sets its 
     * period and starts it.
     */
    public void startReactorService() {
        reactorService = new ReactorService();
        reactorService.setPeriod(Duration.seconds(baseReactorPeriod));
        reactorService.start();
    }

    /**
     * This class defines a scheduled service that is used to trigger 
     * fluctuations in the power line inputs at regular intervals.
     */
    private static class ReactorService extends ScheduledService<Void> {
        protected Task<Void> createTask() {
            return new Task<Void>() {
                protected Void call() {
                    triggerFluctuation(Main.getPowerManager().getLines());
                    return null;
                }
            };
        }
    }
    
    // Heat service taking care of timing heat accumulation and dissipation
    
    /**
     * The method triggers the calculation of heat accumulation in each of the 
     * power breakers of the power manager.
     */
    static public void checkHeating() {
        Platform.runLater(new Runnable() {
            public void run() {
                for (int i = 0; i < 2; i++) {
                    if (Main.getPowerManager().getPowerChannel(i).getLeftBreaker().getBreakerDelta().doubleValue() != 0.0) {
                        Main.getPowerManager().getPowerChannel(i).getLeftBreaker().applyHeatDelta();
                    }
                    if (Main.getPowerManager().getPowerChannel(i).getRightBreaker().getBreakerDelta().doubleValue() != 0.0) {
                        Main.getPowerManager().getPowerChannel(i).getRightBreaker().applyHeatDelta();
                    }
                }
            }
        });
    }
    
    /**
     * The method creates an instance of a heat accumulation timing service, 
     * sets its period and starts it.
     */
    public void startHeatService() {
        heatService = new HeatService();
        heatService.setPeriod(Duration.seconds(1));
        heatService.start();
    }

    /**
     * This class defines a scheduled service that is used to keep
     * track of heat accumulation the the power circuits.
     */
    private static class HeatService extends ScheduledService<Void> {
        protected Task<Void> createTask() {
            return new Task<Void>() {
                protected Void call() {
                    checkHeating();
                    return null;
                }
            };
        }
    }
    
    
}


