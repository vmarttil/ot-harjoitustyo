/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.paint.Color;
import ui.Main;

/**
 *
 * @author Ville
 */
public class PowerLine {
    int number;
    int stability;
    Random randomGenerator;
    Manager manager;
    Oscillator reactorLine;
    Fluctuator inputFluctuator;
    Oscillator inputAdjuster;
    ObservableList<XYChart.Series<Number, Number>> outputData;
    boolean online;
    int inputPower;
    SimpleDoubleProperty outputPower;
    double rmsSum;
    double rms;
    
    public PowerLine(Manager manager, int number) {
        this.stability = 50;
        this.number = number;
        this.randomGenerator = new Random();
        this.manager = manager;
        this.reactorLine = new Oscillator(100, 100, 0.0);
        this.inputFluctuator = new Fluctuator(reactorLine, 10);
        this.inputAdjuster = new Oscillator(100, 100, Math.PI);
        this.outputData = FXCollections.observableArrayList();
        this.outputPower = new SimpleDoubleProperty(100);
        this.inputPower = 100;
        this.online = true;
        createOscilloscopeData();
    }
 
    // Getters
    
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
    
    public int getInputPower() {
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
    
    // Setters

    public void setStability(int stability) {
        this.stability = stability;
    }
    
    public void setInputPower(int power) {
        this.inputPower = power;
    }
    
    public void setOutputPower(double power) {
        outputPower.set(power);
    }
    
    public void setOnline() {
        this.online = true;
        this.updateOscilloscopeData();
    }
    
    public void setOffline() {
        this.online = false;
        this.setOutputPower(0);
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
        Main.getStatusLeds()[number].setColor(Color.RED);
        Main.getStatusLeds()[number].setBlink(true);
        // TimeUnit.SECONDS.sleep(5);
        this.resetLine();
        Main.getStatusLeds()[number].setBlink(false);
    }
    
    public void startupProcess() {
        
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
            setOutputPower(100 - rms);
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
        int reactorFrequency = getReactorFrequency().intValue();
        int reactorAmplitude = getReactorAmplitude().intValue();
        double reactorPhase = getReactorPhase().doubleValue();
        int controlFrequency = getControlFrequency().intValue();
        int controlAmplitude = getControlAmplitude().intValue();
        double controlPhase = getControlPhase().doubleValue();
        for (int i = 0; i < 40; i++) {            
            Double reactorValue = reactorAmplitude * Math.sin(2 * Math.PI * reactorFrequency * ((double) i * 5 / 10000) + reactorPhase);
            Double controlValue = controlAmplitude  *  Math.sin(2 * Math.PI * controlFrequency * ((double) i * 5 / 10000) + controlPhase);
            this.outputData.get(0).getData().get(i).setYValue(reactorValue + controlValue);
            this.outputData.get(1).getData().get(i).setYValue(reactorValue);
            this.outputData.get(2).getData().get(i).setYValue(controlValue);
            if (this.online == true) {
                this.rmsSum = this.rmsSum + Math.pow(reactorValue + controlValue, 2);
            }
        }
        if (this.online == true) {
            rms = Math.sqrt(rmsSum / 40);
            setOutputPower(100 - rms);
        }
    }
}
