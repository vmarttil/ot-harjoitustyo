/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import eu.hansolo.medusa.Gauge;
import java.security.Timestamp;
import java.util.Random;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
    private SimpleDoubleProperty mainOutputLevel; 
    private SimpleIntegerProperty[] outputAdjusterValues;
    private SimpleDoubleProperty[] outputLevels;
    private Gauge[] outputGauges;
    private Gauge mainOutputGauge;
    private double[] heatDeltaComponents;
    private long fluctuateTime;
    public dao.JSONLogDao JSONLogging;
    
    
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
        this.baseReactorPeriod = 10;
        this.mainOutputLevel = new SimpleDoubleProperty(100.0);
        this.outputAdjusterValues = new SimpleIntegerProperty[lines / 2];
        this.outputLevels = new SimpleDoubleProperty[lines / 2];
        this.outputGauges = new Gauge[lines / 2];
        this.JSONLogging = new dao.JSONLogDao(this);
    }
    
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
     * The method returns the current value of the numbered output channel
     * power adjuster.
     * @param number the output number
     * @return value of the output 0 power adjuster
     */    
    public SimpleIntegerProperty getPowerAdjusterValue(int number) {
        return this.outputAdjusterValues[number];
    }
    
    /**
     * The method returns the adjusted output level of numbered output channel.
     * @param number the output number
     * @return adjusted channel 0 output power level
     */
    public SimpleDoubleProperty getOutputValue(int number) {
        return this.outputLevels[number];
    }
    
    /**
     * The method returns the total power output from the power manager.
     * @return main output power level
     */
    public SimpleDoubleProperty getMainOutputLevel() {
        return this.mainOutputLevel;
    }

    /**
     * The method returns the value of the fluctuation timer that enables the 
     * calculation of time since the last fluctuation for timing purposes.
     * @return time the time of the last fluctuation in seconds since 01-01-1970
     */    
    public long getFluctuateTime() {
        return this.fluctuateTime;
    }
    
    /**
     * The method returns the Dao object responsible for logging the events of 
     * the power manager in JSON format and exporting them to a file.
     * @return the JSON logger object
     */    
    public dao.JSONLogDao getJSONLogging() {
        return this.JSONLogging;
    }
    
    /**
     * The method acreates a reactorService for the manager without starting it 
     * (for testing purposes).
     */    
    public void createReactorService() {
        this.reactorService = new ReactorService();
    }
    
    /**
     * The method sets a reference to the output gauge object for the 
     * output given by the number parameter.
     * @param number the output number
     * @param outputGauge the output gauge object
     */    
    public void setOutputGauge(int number, Gauge outputGauge) {
        this.outputGauges[number] = outputGauge;
    }
    
    /**
     * The method sets a reference to the main output gauge object.
     * @param outputGauge the main output gauge object
     */    
    public void setMainOutputGauge(Gauge outputGauge) {
        this.mainOutputGauge = outputGauge;
    }
    
    /**
     * The method sets the fluctuation timer that enables the calculation of 
     * time since the last fluctuation for timing purposes.
     * @param time the time of the last fluctuation in seconds since 01-01-1970
     */    
    public void setFluctuateTime(long time) {
        this.fluctuateTime = time;
    }
    
    /**
     * The method sets the value of numbered output channel power adjuster.
     * @param number the output number
     * @param adjusterValue new value of the output power adjuster
     */    
    public void setOutputAdjusterValue(int number, int adjusterValue) {
        this.outputAdjusterValues[number].set(adjusterValue);
    }
    
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

    /**
     * The method creates the properties related to the main output controls
     * @param outputs the number of outputs in the manager
     */
    public void createMainOutputs(int outputs) {
        for (int i = 0; i < outputs; i++) {
            this.outputAdjusterValues[i] = new SimpleIntegerProperty(100);
            this.outputLevels[i] = new SimpleDoubleProperty(100);
        }
        this.mainOutputLevel = new SimpleDoubleProperty(100);
    }
    
    /**
     * The method calculates the amount of heat increase or decrease per second
     * caused by the level of power draw in the breakers of the channel, whose 
     * number is given as a parameter.
     * @param channel the number of the channel whose heat increase to calculate
     */
    public double calculateHeatDeltaComponent(int channel) {
        return (120 - this.getPowerAdjusterValue(channel).intValue()) / -10.0;
    }
    
    /**
     * The method calculates the adjusted output levels of all channel 
     * outputs and the total power output of the system, and sets the 
     * values of the appropriate properties accordingly so that they can be .
     */
    public void calculateOutputValues() {
        double totalOutputLevel = 0;
        for (int i = 0; i < this.lines / 2; i++) {
            double outputLevel = this.getPowerChannel(i).getOutputPower().doubleValue() * this.getPowerAdjusterValue(i).intValue() / 100.0;
            this.outputLevels[i].set(outputLevel);
            totalOutputLevel = totalOutputLevel + outputLevel;
        }
        this.mainOutputLevel.set(totalOutputLevel / this.outputLevels.length);        
        calculateReactorPeriodMultiplier();
        JSONLogging.addLogEntry();
    }
    
    /**
     * The method calculates the multiplier applied to the period of the timer 
     * that is used to trigger fluctuations in the reactor line inputs, based on 
     * the balance between the output levels and the amount of gain applied 
     * to them, and sets the period of the timer accordingly.
     */
    public void calculateReactorPeriodMultiplier() {
        // Imbalance multiplier
        double totalDeviation = 0;
        for (int i = 0; i < this.outputLevels.length; i++) {
            totalDeviation = totalDeviation + Math.abs(this.outputLevels[i].doubleValue() - this.mainOutputLevel.doubleValue());
        }
        double averageDeviation = totalDeviation / this.outputLevels.length;
        double imbalanceMultiplier = 1 / (1 + (averageDeviation / 10));
        // Power draw multiplier
        int totalPowerAdjusterValues = 0;
        for (int i = 0; i < this.outputAdjusterValues.length; i++) {
            totalPowerAdjusterValues = totalPowerAdjusterValues + this.outputAdjusterValues[i].intValue();
        }
        double averagePowerAdjusterValues = (double) totalPowerAdjusterValues / this.outputAdjusterValues.length;
        double powerOfTwo = (100 - averagePowerAdjusterValues) / 25;
        double powerOfTen = Math.pow(2, powerOfTwo);
        double powerDrawMultiplier = Math.pow(10, powerOfTen) / 10;
        setReactorPeriod((double) this.baseReactorPeriod * imbalanceMultiplier * powerDrawMultiplier);
    }
    
    // Reactor service taking care of timing power fluctuation
    
    public void setReactorPeriod(double period) {
        Platform.runLater(new Runnable() {
            public void run() {
                if (reactorService.isRunning() == true) {
                    reactorService.cancel();
                    reactorService.reset();
                    reactorService.setPeriod(Duration.seconds(period));
                    double secondsSinceFluctuation = (System.currentTimeMillis() / 1000L) - Main.getPowerManager().getFluctuateTime();
                    if (Duration.seconds(period).toSeconds() > secondsSinceFluctuation) {
                        reactorService.setDelay(Duration.seconds(period).subtract(Duration.seconds(secondsSinceFluctuation)));
                    }
                    reactorService.start();
                }
            }
        }); 
    }
    
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
                Main.getPowerManager().setFluctuateTime(System.currentTimeMillis() / 1000L);
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


