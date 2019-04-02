/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.scene.chart.XYChart;

/**
 *
 * @author Ville
 */
public class PowerLine {
    Manager manager;
    Oscillator reactorLine;
    Fluctuator inputFluctuator;
    Oscillator inputAdjuster;
    XYChart.Series<Integer,Double> outputData;
    int inputPower;
    double outputPower;
    
    public PowerLine(Manager manager, int number) {
        this.manager = manager;
        this.reactorLine = new Oscillator(100, 100, 0.0);
        this.inputFluctuator = new Fluctuator(reactorLine,50,10);
        this.inputAdjuster = new Oscillator(100, 100, Math.PI);
        this.outputData = new XYChart.Series<>();
        this.inputPower = 100;
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
    
    public XYChart.Series<Integer,Double> getOutputData() {
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
    
}
