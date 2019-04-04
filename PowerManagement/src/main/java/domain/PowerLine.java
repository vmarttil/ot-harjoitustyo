/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.ArrayList;
import java.util.Random;
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
    ObservableList<XYChart.Series<Number,Number>> outputData;
    
    
    int inputPower;
    double outputPower;
    
    public PowerLine(Manager manager, int number) {
        this.stability = 50;
        this.number = number;
        this.randomGenerator = new Random();
        this.manager = manager;
        this.reactorLine = new Oscillator(100, 100, 0.0);
        this.inputFluctuator = new Fluctuator(reactorLine,10);
        this.inputAdjuster = new Oscillator(100, 100, Math.PI);
        this.outputData = FXCollections.observableArrayList();
        this.inputPower = 100;
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
    
    public ObservableList<XYChart.Series<Number,Number>> getOutputData() {
        return outputData;
    }
    
    public int getInputPower() {
        return this.inputPower;
    }
    
    public double getOutputPower() {
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
    
    // Setters
    
    public void setInputPower(int power) {
        this.inputPower = power;
    }
    
    public void fluctuateLine() {
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
    
    // Calculating data for the oscilloscope
    
    public void createOscilloscopeData() {
        int reactorFrequency = getReactorFrequency().intValue();
        int reactorAmplitude = getReactorAmplitude().intValue();
        double reactorPhase = getReactorPhase().doubleValue();
        int controlFrequency = getControlFrequency().intValue();
        int controlAmplitude = getControlAmplitude().intValue();
        double controlPhase = getControlPhase().doubleValue();
        XYChart.Series<Number,Number> dataSeries = new XYChart.Series<>();
        for (int i = 0; i < 40; i++) {
            XYChart.Data<Number,Number> datapoint = new XYChart.Data<>();
            Double reactorValue = reactorAmplitude * Math.sin(2 * Math.PI * reactorFrequency * ((double) i*5 / 10000) + reactorPhase);
            Double controlValue = controlAmplitude * Math.sin(2 * Math.PI * controlFrequency * ((double) i*5 / 10000) + controlPhase);
            datapoint.setXValue((Number) (i*5));
            datapoint.setYValue((Number) (reactorValue + controlValue));
            dataSeries.getData().add(datapoint);
        }
        dataSeries.setName("Reactor_" + number + "_data" );
        this.outputData.add(dataSeries);
    }
    
    public void updateOscilloscopeData() {
        int reactorFrequency = getReactorFrequency().intValue();
        int reactorAmplitude = getReactorAmplitude().intValue();
        double reactorPhase = getReactorPhase().doubleValue();
        int controlFrequency = getControlFrequency().intValue();
        int controlAmplitude = getControlAmplitude().intValue();
        double controlPhase = getControlPhase().doubleValue();
        for (int i = 0; i < 40; i++) {            
            Double reactorValue = reactorAmplitude * Math.sin(2 * Math.PI * reactorFrequency * ((double) i*5 / 10000) + reactorPhase);
            Double controlValue = controlAmplitude * Math.sin(2 * Math.PI * controlFrequency * ((double) i*5 / 10000) + controlPhase);
            this.outputData.get(0).getData().get(i).setYValue(reactorValue + controlValue);
        }
    }
}
