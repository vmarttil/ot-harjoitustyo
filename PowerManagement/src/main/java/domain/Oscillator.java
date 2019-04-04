/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author Ville
 */
public class Oscillator {
    int baseFrequency;
    int baseAmplitude;
    double basePhase;
    IntegerProperty currentFrequency;
    IntegerProperty currentAmplitude;
    DoubleProperty currentPhase;
    
    public Oscillator(int frequency, int amplitude, double phase) {
        this.baseFrequency = frequency;
        this.baseAmplitude = amplitude;
        this.basePhase = phase;
        this.currentFrequency = new SimpleIntegerProperty(frequency);
        this.currentAmplitude = new SimpleIntegerProperty(amplitude);
        this.currentPhase = new SimpleDoubleProperty(phase);
    }

    // Getters

    public int getBaseFrequency() {
        return this.baseFrequency;
    }

    public int getBaseAmplitude() {
        return this.baseAmplitude;
    }

    public double getBasePhase() {
        return this.basePhase;
    }

    public IntegerProperty getCurrentFrequency() {
        return this.currentFrequency;
    }

    public IntegerProperty getCurrentAmplitude() {
        return this.currentAmplitude;
    }

    public DoubleProperty getCurrentPhase() {
        return this.currentPhase;
    }

    // Setters
    
    public void setCurrentFrequency(int currentFrequency) {
        this.currentFrequency.setValue(currentFrequency);
    }

    public void setCurrentAmplitude(int currentAmplitude) {
        this.currentAmplitude.setValue(currentAmplitude);
    }

    public void setCurrentPhase(double currentPhase) {
        this.currentPhase.setValue(currentPhase);
    }
    
}
