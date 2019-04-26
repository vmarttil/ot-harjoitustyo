/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

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
import javafx.util.Duration;
import ui.Main;

/**
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
 
    // Getters
    
    public int getStability() {
        return this.stability;
    }
    
    public int getNumber() {
        return this.number;
    }
    
    public Oscillator getReactorLine() {
        return this.reactorLine;
    }
    
    public Fluctuator getInputFluctuator() {
        return this.inputFluctuator;
    }
    
    public Oscillator getInputAdjuster() {
        return this.inputAdjuster;
    }
    
    public ObservableList<XYChart.Series<Number, Number>> getOutputData() {
        return outputData;
    }
    
    public double getInputPower() {
        return this.inputPower;
    }
    
    public DoubleProperty getOutputPower() {
        return this.outputPower;
    }
    
    public IntegerProperty getReactorFrequency() {
        return this.reactorLine.getCurrentFrequency();
    }
    
    public IntegerProperty getReactorAmplitude() {
        return this.reactorLine.getCurrentAmplitude();
    }
    
    public DoubleProperty getReactorPhase() {
        return this.reactorLine.getCurrentPhase();
    }
    
    public IntegerProperty getControlFrequency() {
        return this.inputAdjuster.getCurrentFrequency();
    }
    
    public IntegerProperty getControlAmplitude() {
        return this.inputAdjuster.getCurrentAmplitude();
    }
    
    public DoubleProperty getControlPhase() {
        return this.inputAdjuster.getCurrentPhase();
    }
    
    public boolean isOnline() {
        return this.online;
    }
    
    public boolean isUnstable() {
        return this.unstable;
    }
    
    public double getRms() {
        return this.rms;
    }
    
    public int getImbalance() {
        return this.imbalance;
    }
    
    // Setters

    public void setStability(int stability) {
        this.stability = stability;
    }
    
    public void setInputPower(int power) {
        this.inputPower = power;
    }
    
    public void setOutputPower(double power) {
        double powerOutput = (double) ((int) Math.round(power * 10) / 10.0);
        outputPower.set(powerOutput);
    }
    
    public void setChannel(PowerChannel channel) {
        this.channel = channel;
    }
    
    public void setOnline() {
        if (Main.getShutdownButtons()[number].isSelected() == false) {
            this.online = true;
            Main.getStatusLeds()[number].setStatus("ok");
            this.updateOscilloscopeData();
        }
    }
    
    public void setOffline() {
        if (Main.getShutdownButtons()[number].isSelected() == false) {
            this.online = false;
            Main.getStatusLeds()[number].setStatus("warning");
            this.setOutputPower(0);
            channel.updateOutput();
        }
    }
    
    public void setUnstable() {
        this.unstable = true;
        this.unstableTimeline.play();
        if (Main.getOfflineButtons()[number].isSelected() == false) {
            Main.getStatusLeds()[number].setStatus("alert");
            Main.getStatusLeds()[number].setFastBlink(true);
        }
    }
    
    public void setStable() {
        this.unstable = false;
        this.unstableTimeline.stop();
        Main.getStatusLeds()[number].setFastBlink(false);
        if (Main.getShutdownButtons()[number].isSelected() == true) {
            Main.getStatusLeds()[number].setStatus("off");
        } else if (Main.getOfflineButtons()[number].isSelected() == true) {
            Main.getStatusLeds()[number].setStatus("warning");
        } else {
            Main.getStatusLeds()[number].setStatus("ok");
        } 
    }
    
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
    
    // Adjusting the power line
    
    public void fluctuateLine() {
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
    
    public void resetLine() {
        reactorLine.setCurrentFrequency(reactorLine.getBaseFrequency());
        reactorLine.setCurrentAmplitude(reactorLine.getBaseAmplitude());
        reactorLine.setCurrentPhase(reactorLine.getBasePhase());
        inputAdjuster.setCurrentFrequency(inputAdjuster.getBaseFrequency());
        inputAdjuster.setCurrentAmplitude(inputAdjuster.getBaseAmplitude());
        inputAdjuster.setCurrentPhase(inputAdjuster.getBasePhase());
        this.updateOscilloscopeData();
    }
    
    public void shutdownProcess() {
        Timer shutdownTimer = new Timer();
        TimerTask shutdownTask = new TimerTask() {
            public void run() {
                finishShutdown();
            }
        };
        if (Main.getOfflineButtons()[number].isSelected() == false) {
            this.online = false;
            Main.getStatusLeds()[number].setStatus("warning");
            this.setOutputPower(0);
            Main.getOfflineButtons()[number].setSelected(true);
        }
        Main.getStatusLeds()[number].setSlowBlink(true);
        Main.getOfflineButtons()[number].setDisable(true);
        Main.getShutdownButtons()[number].setDisable(true);
        shutdownTimer.schedule(shutdownTask, 10000l);
    }
    
    public void startupProcess() {
        Timer startupTimer = new Timer();
        TimerTask startupTask = new TimerTask() {
            public void run() {
                finishStartup();
            }
        };
        if (Main.getOfflineButtons()[number].isSelected() == true) {
            Main.getStatusLeds()[number].setStatus("warning");
        } else {
            Main.getStatusLeds()[number].setStatus("ok");
        }
        Main.getStatusLeds()[number].setSlowBlink(true);
        Main.getOfflineButtons()[number].setDisable(true);
        Main.getShutdownButtons()[number].setDisable(true);
        startupTimer.schedule(startupTask, 10000l);
    }
    
    public void finishShutdown() {
        Platform.runLater(new Runnable() {
            public void run() {
                Main.getStatusLeds()[number].setSlowBlink(false);
                Main.getStatusLeds()[number].setStatus("off");
                Main.getPowerManager().getPowerLine(number).resetLine();
                Main.getOfflineButtons()[number].setDisable(false);
                Main.getShutdownButtons()[number].setDisable(false);
                Main.getShutdownButtons()[number].setText("Startup");
            }
        });
    }
    
    public void finishStartup() {
        Platform.runLater(new Runnable() {
            public void run() {
                Main.getStatusLeds()[number].setSlowBlink(false);
                if (Main.getOfflineButtons()[number].isSelected() == true) {
                    Main.getStatusLeds()[number].setStatus("warning");
                } else {
                    Main.getStatusLeds()[number].setStatus("ok");
                    Main.getPowerManager().getPowerLine(number).setOnline();
                    Main.getPowerManager().getPowerLine(number).updateOscilloscopeData();
                }
                Main.getOfflineButtons()[number].setDisable(false);
                Main.getShutdownButtons()[number].setDisable(false);
                Main.getShutdownButtons()[number].setText("Shutdown");
            }
        });
    }
    
    // Setting up timelines for extra fluctuation
    
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
    
    // Calculating data for the oscilloscope
    
    public void createOscilloscopeData() {
        rmsSum = 0;
        int reactorFrequency = getReactorFrequency().intValue();
        int reactorAmplitude = getReactorAmplitude().intValue();
        double reactorPhase = getReactorPhase().doubleValue();
        int controlFrequency = getControlFrequency().intValue();
        int controlAmplitude = getControlAmplitude().intValue();
        double controlPhase = getControlPhase().doubleValue();
        XYChart.Series<Number, Number> dataSeriesOutput = new XYChart.Series<>();
        XYChart.Series<Number, Number> dataSeriesReactor = new XYChart.Series<>();
        XYChart.Series<Number, Number> dataSeriesControl = new XYChart.Series<>();
        for (int i = 0; i < 40; i++) {
            createOscilloscopeDatapoint(dataSeriesOutput, dataSeriesReactor, dataSeriesControl, 
                                        reactorFrequency, reactorAmplitude, reactorPhase, 
                                        controlFrequency, controlAmplitude, controlPhase, i);
        }
        dataSeriesOutput.setName("Reactor" + number + "OutputData");
        dataSeriesReactor.setName("Reactor" + number + "ReactorData");
        dataSeriesControl.setName("Reactor" + number + "ControlData");
        this.outputData.addAll(dataSeriesOutput, dataSeriesReactor, dataSeriesControl);
        if (this.online == true) {
            rms = Math.sqrt(rmsSum / 40);
            setOutputPower(100 - (rms));
        }
    }
    
    private void createOscilloscopeDatapoint(XYChart.Series<Number, Number> dataSeriesOutput, 
                                             XYChart.Series<Number, Number> dataSeriesReactor, 
                                             XYChart.Series<Number, Number> dataSeriesControl,
                                             int reactorFrequency, int reactorAmplitude, double reactorPhase,
                                             int controlFrequency, int controlAmplitude, double controlPhase, 
                                             int i) {
        XYChart.Data<Number, Number> datapointOutput = new XYChart.Data<>();
        XYChart.Data<Number, Number> datapointReactor = new XYChart.Data<>();
        XYChart.Data<Number, Number> datapointControl = new XYChart.Data<>();
        Double reactorValue = reactorAmplitude * Math.sin(2 * Math.PI * reactorFrequency * ((double) i * 5 / 10000) + reactorPhase);
        Double controlValue = controlAmplitude * Math.sin(2 * Math.PI * controlFrequency * ((double) i * 5 / 10000) + controlPhase);
        datapointOutput.setXValue((Number) (i * 5));
        datapointOutput.setYValue((Number) (reactorValue + controlValue));
        dataSeriesOutput.getData().add(datapointOutput);
        datapointReactor.setXValue((Number) (i * 5));
        datapointReactor.setYValue((Number) (reactorValue));
        dataSeriesReactor.getData().add(datapointReactor);
        datapointControl.setXValue((Number) (i * 5));
        datapointControl.setYValue((Number) (reactorValue));
        dataSeriesControl.getData().add(datapointControl);
        if (this.online == true) {
            this.rmsSum = this.rmsSum + Math.pow(reactorValue + controlValue, 2);
        }
    }
    
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
            this.outputData.get(1).getData().get(i).setYValue(reactorValue);
            this.outputData.get(2).getData().get(i).setYValue(controlValue);
            if (this.online == true) {
                this.rmsSum = this.rmsSum + Math.pow(reactorValue + controlValue, 2);
            }
        }
        if (this.online == true) {
            rms = Math.sqrt(rmsSum / 40);
            if (this.inputPower - rms >= 0) {
                setOutputPower(this.inputPower - rms);
            } else {
                setOutputPower(0);
            }
            if (getOutputPower().doubleValue() > 100.0) {
                Main.getLineOutputGauges()[number].setLedOn(true);
            } else {
                Main.getLineOutputGauges()[number].setLedOn(false);
            }
        }
        channel.updateOutput();
        if (overshoot == true) {
            setUnstable();
        } else {
            setStable();
        }
    }
    
    
}
