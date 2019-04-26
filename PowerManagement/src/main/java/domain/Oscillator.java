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
 * The class defines an oscillator that represents a waveform source – used to 
 * simulate both reactor power output and a balancing oscillator used to 
 * condition it – by defining and tracking the frequency, amplitude and phase 
 * of the waveform. 
 * 
 * @author Ville Marttila
 */
public class Oscillator {
    private int baseFrequency;
    private int baseAmplitude;
    private double basePhase;
    private IntegerProperty currentFrequency;
    private IntegerProperty currentAmplitude;
    private DoubleProperty currentPhase;
    
    /**
     * The constructor defines an oscillator with the waveform properties 
     * represented by the given parameters.
     * @param frequency the base or "neutral" frequency of the oscillator
     * @param amplitude the base or "neutral" amplitude of the oscillator
     * @param phase  the base or "neutral" phase of the oscillator
     */
    public Oscillator(int frequency, int amplitude, double phase) {
        this.baseFrequency = frequency;
        this.baseAmplitude = amplitude;
        this.basePhase = phase;
        this.currentFrequency = new SimpleIntegerProperty(frequency);
        this.currentAmplitude = new SimpleIntegerProperty(amplitude);
        this.currentPhase = new SimpleDoubleProperty(phase);
    }

    // Getters
    /**
     * The method returns the fixed base or "neutral" frequency of the oscillator 
     * as an integer.
     * @return the base frequency of the oscillator
     */
    public int getBaseFrequency() {
        return this.baseFrequency;
    }

    /**
     * The method returns the fixed base or "neutral" amplitude of the oscillator 
     * as an integer.
     * @return the base amplitude of the oscillator
     */
    public int getBaseAmplitude() {
        return this.baseAmplitude;
    }

    /**
     * The method returns the fixed base or "neutral" phase of the oscillator 
     * as a double.
     * @return the base phase of the oscillator
     */
    public double getBasePhase() {
        return this.basePhase;
    }

    /**
     * The method returns the current, changeable frequency of the oscillator 
     * as an integer property (observable value).
     * @return the current frequency of the oscillator
     */
    public IntegerProperty getCurrentFrequency() {
        return this.currentFrequency;
    }

    /**
     * The method returns the current, changeable amplitude of the oscillator 
     * as an integer property (observable value).
     * @return the current amplitude of the oscillator
     */
    public IntegerProperty getCurrentAmplitude() {
        return this.currentAmplitude;
    }

    /**
     * The method returns the current, changeable phase of the oscillator 
     * as a double property (observable value).
     * @return the current phase of the oscillator
     */
    public DoubleProperty getCurrentPhase() {
        return this.currentPhase;
    }

    // Setters
    /**
     * The method sets the current, changeable frequency of the oscillator 
     * as an integer property (observable value).
     * @param currentFrequency the new frequency
     */
    public void setCurrentFrequency(int currentFrequency) {
        this.currentFrequency.setValue(currentFrequency);
    }

    /**
     * The method sets the current, changeable amplitude of the oscillator 
     * as an integer property (observable value).
     * @param currentAmplitude the new amplitude
     */
    public void setCurrentAmplitude(int currentAmplitude) {
        this.currentAmplitude.setValue(currentAmplitude);
    }

    /**
     * The method sets the current, changeable phase of the oscillator 
     * as an double property (observable value).
     * @param currentPhase the new phase
     */
    public void setCurrentPhase(double currentPhase) {
        this.currentPhase.setValue(currentPhase);
    }
}
