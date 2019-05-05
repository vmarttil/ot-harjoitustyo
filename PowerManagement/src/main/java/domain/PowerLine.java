/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import eu.hansolo.medusa.Gauge;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ToggleButton;
import javafx.util.Duration;

/**
 * The class defines the first component of the power management system, 
 * the reactor power line, which conditions the power coming in from the 
 * reactor for the power channels. It consists of a fluctuating power input, 
 * simulated by an oscillator that is modulated by a fluctuator, and a set 
 * of controls that allow the user to correct discrepancies in the frequency, 
 * amplitude and phase of the waveform in order to avoid heat generation and 
 * minimise power loss. The heart of the power line is the oscilloscope display 
 * that shows any deviations in the input waveform, allowing the user to correct 
 * them, and also measures the variation of the waveform from the norm (based on 
 * a root mean square (RMS) value calculated from the samples), which affects 
 * both the power throughput and the heat generated in the system.
 * 
 * @author Ville
 */
public class PowerLine {
    private int number;
    private int stability;
    private Random randomGenerator;
    private Manager manager;
    private Oscillator reactorLine;
    private Fluctuator inputFluctuator;
    private Oscillator inputAdjuster;
    private ObservableList<XYChart.Series<Number, Number>> outputData;
    private boolean online;
    private double inputPower;
    private PowerChannel channel;
    private SimpleDoubleProperty outputPower;
    private double rmsSum;
    private double rms;
    private boolean unstable;
    private Timeline unstableTimeline;
    private int imbalance;
    private Timeline imbalanceTimeline;
    private Timeline severeImbalanceTimeline;
    private ToggleButton shutdownButton;
    private ToggleButton offlineButton;
    private ui.StatusLed statusLed;
    private Gauge lineOutputGauge;
    
    /**
     * The constructor defines the power line object, creates the oscillators,
     * the fluctuator and the oscilloscope logic and keeps track of the 
     * stability of the power input.
     * @param manager the Manager object controlling the line
     * @param number the number of the line
     */
    public PowerLine(Manager manager, int number) {
        this.stability = 50;
        this.number = number;
        this.randomGenerator = new Random();
        this.manager = manager;
        this.reactorLine = new Oscillator(100, 100, 0.0);
        this.inputFluctuator = new Fluctuator(reactorLine, 5);
        this.inputAdjuster = new Oscillator(100, 100, Math.PI);
        this.outputData = FXCollections.observableArrayList();
        this.outputPower = new SimpleDoubleProperty(100);
        this.inputPower = 100.0;
        this.online = true;
        this.unstable = false;
        this.unstableTimeline = setupFluctuationTimeline(400);
        this.imbalance = 0;
        this.imbalanceTimeline = setupFluctuationTimeline(2000);
        this.severeImbalanceTimeline = setupFluctuationTimeline(1000);
        createOscilloscopeData();
    }
 
    /**
     * The method returns the stability value of the line, i.e. the percentage 
     * possibility that the line will resist fluctuation.
     * @return stability percentage
     */
    public int getStability() {
        return this.stability;
    }
    
    /**
     * The method returns the number of the line.
     * @return number of the line
     */
    public int getNumber() {
        return this.number;
    }
    
    /**
     * The method returns the power manager controlling the line.
     * @return manager of the line
     */
    public Manager getManager() {
        return this.manager;
    }
    
    /**
     * The method returns the Oscillator object representing the waveform of 
     * the incoming reactor line.
     * @return the reactor line oscillator
     */
    public Oscillator getReactorLine() {
        return this.reactorLine;
    }
    
    /**
     * The method returns the Fluctuator object that is responsible for 
     * variation in the parameters of the input oscillator.
     * @return the reactor line fluctuator
     */
    public Fluctuator getInputFluctuator() {
        return this.inputFluctuator;
    }
    
    /**
     * The method returns the Oscillator object that the user can control to 
     * counteract changes in the reactor line.
     * @return the adjustment oscillator
     */
    public Oscillator getInputAdjuster() {
        return this.inputAdjuster;
    }
    
    /**
     * The method returns the dataset, as a set of coordinates representing 
     * the waveform, calculated from the input and adjustment parameters and 
     * visualised by the oscilloscope
     * @return adjusted waveform as a series of coordinates
     */
    public ObservableList<XYChart.Series<Number, Number>> getOutputData() {
        return outputData;
    }
    
    /**
     * The method returns the current input level of the line arriving from the 
     * reactor (which is constant in this version but will be determined by 
     * the reactor component designed to augment this application).
     * @return input power level
     */
    public double getInputPower() {
        return this.inputPower;
    }
    
    /**
     * The method returns the total output level of the line as a percentage
     * of nominal power.
     * @return output power level
     */
    public DoubleProperty getOutputPower() {
        return this.outputPower;
    }
    
    /**
     * The method returns the current frequency of the reactor line input.
     * @return input frequency as an integer property
     */
    public IntegerProperty getReactorFrequency() {
        return this.reactorLine.getCurrentFrequency();
    }
    
    /**
     * The method returns the current amplitude of the reactor line input.
     * @return input amplitude as an integer property
     */
    public IntegerProperty getReactorAmplitude() {
        return this.reactorLine.getCurrentAmplitude();
    }
    
    /**
     * The method returns the current phase of the reactor line input.
     * @return input phase as an double property
     */
    public DoubleProperty getReactorPhase() {
        return this.reactorLine.getCurrentPhase();
    }
    
    /**
     * The method returns the current frequency of the adjustment controller, 
     * set by the user.
     * @return adjustment controller frequency as an integer property
     */
    public IntegerProperty getControlFrequency() {
        return this.inputAdjuster.getCurrentFrequency();
    }
    
    /**
     * The method returns the current amplitude of the adjustment controller, 
     * set by the user.
     * @return adjustment controller amplitude as an integer property
     */
    public IntegerProperty getControlAmplitude() {
        return this.inputAdjuster.getCurrentAmplitude();
    }
    
    /**
     * The method returns the current phase of the adjustment controller, 
     * set by the user.
     * @return adjustment controller phase as a double property
     */
    public DoubleProperty getControlPhase() {
        return this.inputAdjuster.getCurrentPhase();
    }
    
    /**
     * The method returns boolean value indicating whether the line is online 
     * or has been momentarily cut off.
     * @return online status of the line as a boolean value
     */
    public boolean isOnline() {
        return this.online;
    }
    
    /**
     * The method returns boolean value indicating whether the line is 
     * especially unstable as the result of distortion in the waveform.
     * @return stability status of the line as a boolean value
     */
    public boolean isUnstable() {
        return this.unstable;
    }
    
    /**
     * The method returns the current RMS value of the adjusted waveform, which 
     * not only determines the amount of power loss but also affects heat 
     * generation.
     * @return RMS value of the waveform as a double
     */
    public double getRms() {
        return this.rms;
    }
    
    /**
     * The method returns the degree of imbalance between this line and 
     * the line joined to it by the power channel balancer affecting 
     * the stability of the reactor input.
     * @return imbalance expressed by the values 0, 1 and 2 (from none to severe)
     */
    public int getImbalance() {
        return this.imbalance;
    }
    
    /**
     * The method returns the Toggle Button object that can be used to 
     * temporarily switch the line out of the system.
     * @return offline button object
     */
    public ToggleButton getOfflineButton() {
        return this.offlineButton;
    }
    
    /**
     * The method returns the Toggle Button object that can be used to 
     * shut down the line, cutting it out of the system and allowing it to 
     * reset itself in the case of xtreme distortion.
     * @return shutdown button object
     */
    public ToggleButton getShutdownButton() {
        return this.shutdownButton;
    }
    
    /**
     * The method returns a reference to the  StatusLed object that indicates 
     * the status of the power line (see the StatusLed class for a more 
     * detailed description).
     * @return status led object
     */
    public ui.StatusLed getStatusLed() {
        return this.statusLed;
    }
    
    /**
     * The method returns the line output gauge UI object.
     * @return UI output power gauge
     */
    public Gauge getLineOutputGauge() {
        return this.lineOutputGauge;
    }
    
    /**
     * The method sets the line's stability value, given as a percentage that 
     * the line can resist fluctuation.
     * @param stability the new stability percentage of the line
     */ 
    public void setStability(int stability) {
        this.stability = stability;
    }
    
    /**
     * The method sets the power level from the reactor the line (in future 
     * versions controlled by the reactor application).
     * @param power input power 
     */ 
    public void setInputPower(int power) {
        this.inputPower = power;
    }
    
    /**
     * The method sets the output level of the line, fed to the input of the 
     * corresponding channel.
     * @param power output power 
     */ 
    public void setOutputPower(double power) {
        double powerOutput = (double) ((int) Math.round(power * 10) / 10.0);
        outputPower.set(powerOutput);
    }
    
    /**
     * The method sets the power channel to which the line is linked.
     * @param channel associated power channel
     */ 
    public void setChannel(PowerChannel channel) {
        this.channel = channel;
    }
    
    /**
     * The method sets the line to online state, unless it has been set to 
     * shut down.
     */ 
    public void setOnline() {
        if (this.shutdownButton.isSelected() == false) {
            this.online = true;
            this.statusLed.setStatus("ok");
            this.updateOscilloscopeData();
        }
    }
    
    /**
     * The method sets the line to offline state, unless it has been set to 
     * shut down.
     */ 
    public void setOffline() {
        if (this.shutdownButton.isSelected() == false) {
            this.online = false;
            this.statusLed.setStatus("warning");
            this.setOutputPower(0);
            this.channel.updateOutput();
        }
    }
    
    /**
     * The method is used to set the line to an unstable state as the result 
     * of too much distortion in the waveform, triggering additional 
     * fluctuations and setting of the status led.
     */ 
    public void setUnstable() {
        this.unstable = true;
        this.unstableTimeline.play();
        if (this.online == true) {
            this.statusLed.setStatus("alert");
            this.statusLed.setFastBlink(true);
        }
    }
    
    /**
     * The method is used to set the line to a stable state when the waveform 
     * is back under control, resetting the status led to normal state and 
     * stopping the extra fluctuations.
     */ 
    public void setStable() {
        this.unstable = false;
        this.unstableTimeline.stop();
        this.statusLed.setFastBlink(false);
        if (this.shutdownButton.isSelected() == true) {
            this.statusLed.setStatus("off");
        } else if (this.offlineButton.isSelected() == true) {
            this.statusLed.setStatus("warning");
        } else {
            this.statusLed.setStatus("ok");
        } 
    }
    
    /**
     * The method is used to set the line into a turbulent state as the result 
     * of too unbalanced matching in the channel balancer.
     * @param degree the degree of imbalance, from 0 to 2
     */ 
    public void setImbalance(int degree) {
        this.imbalance = degree;
        if (this.imbalance == 0) {
            this.imbalanceTimeline.stop();
            this.severeImbalanceTimeline.stop();
        } else if (this.imbalance == 1) {
            this.imbalanceTimeline.play();
            this.severeImbalanceTimeline.stop();
        } else if (this.imbalance == 2) {
            this.imbalanceTimeline.stop();
            this.severeImbalanceTimeline.play();
        }
    }
    
    /**
     * The method sets a reference to the offline button UI object associated 
     * with the channel.
     * @param offlineButton the offline toggle button object
     */ 
    public void setOfflineButton(ToggleButton offlineButton) {
        this.offlineButton = offlineButton;
    }
    
    /**
     * The method sets a reference to the shutdown button UI object associated 
     * with the channel.
     * @param shutdownButton the shutdown toggle button object
     */ 
    public void setShutdownButton(ToggleButton shutdownButton) {
        this.shutdownButton = shutdownButton;
    }
    
    /**
     * The method sets a reference to the status led UI object associated 
     * with the channel.
     * @param statusLed the StatusLed object
     */ 
    public void setStatusLed(ui.StatusLed statusLed) {
        this.statusLed = statusLed;
    }
    
    /**
     * The method sets a reference to the line output gauge UI object associated 
     * with the channel.
     * @param lineOutputGauge the output gauge object
     */ 
    public void setLineOutputGauge(Gauge lineOutputGauge) {
        this.lineOutputGauge = lineOutputGauge;
    }
    
    
    /**
     * The method triggers the fluctuation of the three parameters of the 
     * reactor input, based on the stability value of the line and the default 
     * random generator of the line.
     */
    public void fluctuateLine() {
        fluctuateLine(this.randomGenerator);
    }

    /**
     * The method triggers the fluctuation of the three parameters of the 
     * reactor input, based on the stability value of the line and a random 
     * generator given as a parameter.
     * @param randomGenerator a random generator
     */
    public void fluctuateLine(Random randomGenerator) {
        if (this.online == true) {
            if (randomGenerator.nextInt(100) > this.stability) {
                inputFluctuator.fluctuateFrequency();
            }
            if (randomGenerator.nextInt(100) > this.stability) {
                inputFluctuator.fluctuateAmplitude();
            }
            if (randomGenerator.nextInt(100) > this.stability) {
                inputFluctuator.fluctuatePhase();
            }
            updateOscilloscopeData();
        }    
    }
    
    /**
     * The method resets both the input and adjustment parameters of the 
     * line back to their initial values, resulting the a perfectly stable 
     * waveform.
     */
    public void resetLine() {
        reactorLine.setCurrentFrequency(reactorLine.getBaseFrequency());
        reactorLine.setCurrentAmplitude(reactorLine.getBaseAmplitude());
        reactorLine.setCurrentPhase(reactorLine.getBasePhase());
        inputAdjuster.setCurrentFrequency(inputAdjuster.getBaseFrequency());
        inputAdjuster.setCurrentAmplitude(inputAdjuster.getBaseAmplitude());
        inputAdjuster.setCurrentPhase(inputAdjuster.getBasePhase());
        this.updateOscilloscopeData();
    }
    
    /**
     * The method initiates the shutdown process of the line, which means that 
     * its output drops to 0 and it is out of commission for 10 seconds, 
     * after which it resets itself and can be reinitialised.
     */
    public void shutdownProcess() {
        Timer shutdownTimer = new Timer();
        TimerTask shutdownTask = new TimerTask() {
            @Override
            public void run() {
                finishShutdown();
            }
        };
        if (this.offlineButton.isSelected() == false) {
            this.online = false;
            this.statusLed.setStatus("warning");
            this.setOutputPower(0);
            this.offlineButton.setSelected(true);
        }
        this.statusLed.setSlowBlink(true);
        this.offlineButton.setDisable(true);
        this.shutdownButton.setDisable(true);
        shutdownTimer.schedule(shutdownTask, 10000l);
    }
    
    /**
     * The method initiates the 10 second startup process of the line, during 
     * which its output is 0, and after which it is again operational and in 
     * a neutral state (but offline, if the offline button was pressed when 
     * it was reinitialised.
     */
    public void startupProcess() {
        Timer startupTimer = new Timer();
        TimerTask startupTask = new TimerTask() {
            @Override
            public void run() {
                finishStartup();
            }
        };
        if (this.offlineButton.isSelected() == true) {
            this.statusLed.setStatus("warning");
        } else {
            this.statusLed.setStatus("ok");
        }
        this.statusLed.setSlowBlink(true);
        this.offlineButton.setDisable(true);
        this.shutdownButton.setDisable(true);
        startupTimer.schedule(startupTask, 10000l);
    }
    
    /**
     * The method, triggered by a timer, initialises a runnable 
     * object that finishes the shutdown process once the 10 
     * seconds are up.
     */
    public void finishShutdown() {
        Platform.runLater(new ShutdownFinish(this));
    }
    
    /**
     * The method, triggered by a timer, initialises a runnable 
     * object that finishes the startup process once the 10 
     * seconds are up.
     */
    public void finishStartup() {
        Platform.runLater(new StartupFinish(this));
    }
    
    /**
     * The method sets up a timeline with the specified interval, 
     * triggering extra fluctuations in the line.
     * @param interval the interval at which the extra fluctuations occur
     */
    private Timeline setupFluctuationTimeline(int interval) {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(interval),
                event -> {
                    fluctuateLine();
                }
            ));
        timeline.setCycleCount(Animation.INDEFINITE);
        return timeline;
    }
    
    /**
     * The method creates the dataset for the oscilloscope and populates it 
     * with the initial values based on the initial values of the input 
     * and control oscillators.
     */
    public void createOscilloscopeData() {
        this.rmsSum = 0;
        int reactorFrequency = getReactorFrequency().intValue();
        int reactorAmplitude = getReactorAmplitude().intValue();
        double reactorPhase = getReactorPhase().doubleValue();
        int controlFrequency = getControlFrequency().intValue();
        int controlAmplitude = getControlAmplitude().intValue();
        double controlPhase = getControlPhase().doubleValue();
        XYChart.Series<Number, Number> dataSeriesOutput = new XYChart.Series<>();
        for (int i = 0; i < 40; i++) {
            createOscilloscopeDatapoint(dataSeriesOutput, reactorFrequency, reactorAmplitude, reactorPhase, 
                                        controlFrequency, controlAmplitude, controlPhase, i);
        }
        dataSeriesOutput.setName("Reactor" + number + "OutputData");
        this.outputData.addAll(dataSeriesOutput/*, dataSeriesReactor, dataSeriesControl */);
        
        
        if (this.online == true) {
            this.rms = Math.sqrt(this.rmsSum / 40);
            setOutputPower(100 - (this.rms));
        }
    }
    
    /**
     * The method, called by the oscilloscope data creation method calculates 
     * the individual data points for the oscilloscope dataset based on the 
     * current oscillator values.
     * @param dataSeriesOutput the output dataseries object
     * @param reactorFrequency the frequency of the reactor input
     * @param reactorAmplitude the amplitude of the reactor input
     * @param reactorPhase the phase of the reactor input
     * @param controlFrequency the frequency of the adjustment oscillator 
     * @param controlAmplitude the amplitude of the adjustment oscillator
     * @param controlPhase the phase of the adjustment oscillator
     * @param i the number of data points used for the oscilloscope
     */
    private void createOscilloscopeDatapoint(XYChart.Series<Number, Number> dataSeriesOutput, 
                                             int reactorFrequency, int reactorAmplitude, double reactorPhase,
                                             int controlFrequency, int controlAmplitude, double controlPhase, 
                                             int i) {
        XYChart.Data<Number, Number> datapointOutput = new XYChart.Data<>();
        Double reactorValue = reactorAmplitude * Math.sin(2 * Math.PI * reactorFrequency * ((double) i * 5 / 10000) + reactorPhase);
        Double controlValue = controlAmplitude * Math.sin(2 * Math.PI * controlFrequency * ((double) i * 5 / 10000) + controlPhase);
        datapointOutput.setXValue((Number) (i * 5));
        datapointOutput.setYValue((Number) (reactorValue + controlValue));
        dataSeriesOutput.getData().add(datapointOutput);
        if (this.online == true) {
            this.rmsSum = this.rmsSum + Math.pow(reactorValue + controlValue, 2);
        }
    }
    
    /**
     * The method, called after every fluctuation and user adjustment, updates 
     * the dataset for the oscilloscope with values based on the current 
     * waveform parameter values and calculates the sum of the squares of 
     * each individual value for the purposes of calculating the RMS.
     */
    public void updateOscilloscopeData() {
        rmsSum = 0;
        boolean overshoot = false;
        int reactorFrequency = getReactorFrequency().intValue();
        int reactorAmplitude = getReactorAmplitude().intValue();
        double reactorPhase = getReactorPhase().doubleValue();
        int controlFrequency = getControlFrequency().intValue();
        int controlAmplitude = getControlAmplitude().intValue();
        double controlPhase = getControlPhase().doubleValue();
        for (int i = 0; i < 40; i++) {            
            Double reactorValue = reactorAmplitude * Math.sin(2 * Math.PI * reactorFrequency * ((double) i * 5 / 10000) + reactorPhase);
            Double controlValue = controlAmplitude  *  Math.sin(2 * Math.PI * controlFrequency * ((double) i * 5 / 10000) + controlPhase);
            if (reactorValue + controlValue > 150) {
                this.outputData.get(0).getData().get(i).setYValue(149);
                overshoot = true;
            } else if (reactorValue + controlValue < -150) {
                this.outputData.get(0).getData().get(i).setYValue(-149);
                overshoot = true;
            } else {
                this.outputData.get(0).getData().get(i).setYValue(reactorValue + controlValue);
            }
            if (this.online == true) {
                this.rmsSum = this.rmsSum + Math.pow(reactorValue + controlValue, 2);
            }
        }
        calculateOutputPower();
        checkForInstability(overshoot);
    }
    
    /**
     * The method, called by the oscilloscope update method, calculates the 
     * current line power output, sets the value of the corresponding  
     * variable accordingly and calls the update method of the corresponding 
     * power channel in order to propagate the changes forward.
     */
    private void calculateOutputPower() {    
        if (this.online == true) {
            this.rms = Math.sqrt(this.rmsSum / 40);
            if (this.inputPower - this.rms >= 0) {
                setOutputPower(this.inputPower - this.rms);
            } else {
                setOutputPower(0);
            }
        }
        channel.updateOutput();
    }
    
    /**
     * The method, called by the oscilloscope update method, checks whether the 
     * waveform has exceeded the limits for instability and triggers the 
     * appropriate action.
     * @param overshoot the boolean value set by the oscilloscope data, indicating 
     *       whether it has exceeded a certain threshold
     */
    private void checkForInstability(boolean overshoot) {  
        if (overshoot == true) {
            setUnstable();
        } else {
            setStable();
        }
    }
}

/**
 * The class is a runnable helper class that defines a method that 
 * finishes the shutdown process once the timer has run out.
 */
class ShutdownFinish implements Runnable {
    PowerLine powerLine;
    
    public ShutdownFinish(PowerLine powerLine) {
        this.powerLine = powerLine;
    }
    
    @Override
    public void run() {
        this.powerLine.getStatusLed().setSlowBlink(false);
        this.powerLine.getStatusLed().setStatus("off");
        this.powerLine.resetLine();
        this.powerLine.getOfflineButton().setDisable(false);
        this.powerLine.getShutdownButton().setDisable(false);
        this.powerLine.getShutdownButton().setText("Startup");
    }
}

/**
 * The class is a runnable helper class that defines a method that 
 * finishes the startup process once the timer has run out.
 */
class StartupFinish implements Runnable {
    PowerLine powerLine;
    
    public StartupFinish(PowerLine powerLine) {
        this.powerLine = powerLine;
    }
    
    @Override
    public void run() {
        this.powerLine.getStatusLed().setSlowBlink(false);
        if (this.powerLine.getOfflineButton().isSelected() == true) {
            this.powerLine.getStatusLed().setStatus("warning");
        } else {
            this.powerLine.getStatusLed().setStatus("ok");
            this.powerLine.setOnline();
            this.powerLine.updateOscilloscopeData();
        }
        this.powerLine.getOfflineButton().setDisable(false);
        this.powerLine.getShutdownButton().setDisable(false);
        this.powerLine.getShutdownButton().setText("Shutdown");
    }
}
