/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

/**
 * The class defines a fluctuator that generates random variation in the 
 * frequency, amplitude and phase of an oscillator object, based on the 
 * parameters defined in the constructor. 
 * 
 * @param   oscillator  The Oscillator object controlled by this fluctuator
 * @param   probability   The probability (from 0 to 100) that the given parameter undergoes fluctuation.
 * @param   volatility  The maximum percentage (from 0 to 100) of the total variation range (+/- 64% of base 
 * value for frequency and amplitude and +/- PI for phase) that a parameter can change in one fluctuation event.
 * 
 * @author Ville Marttila
 */
public class Fluctuator {
    
    Oscillator oscillator;
    int volatility;
    Random randomGenerator;
    
    public Fluctuator(Oscillator oscillator, int volatility) {
        this.oscillator = oscillator;
        this.volatility = volatility;
        this.randomGenerator = new Random();
    }
    
    // Getters
    public int getVolatility() {
        return this.volatility;
    }
    
    // Setters
    public void setVolatility(int volatility) {
        this.volatility = volatility;
    }
    
    // Methods for causing fluctuation in the oscillator
    
    public void fluctuateFrequency() {
        fluctuateFrequency(this.volatility);
    }
    
    public void fluctuateFrequency(int volatility) {
        int newFrequency = oscillator.getCurrentFrequency().intValue();
        int baseFrequency = oscillator.getBaseFrequency();
        int currentFrequency = oscillator.getCurrentFrequency().intValue();
        int minFrequency = (int) Math.round((double) baseFrequency - 0.64 * baseFrequency);
        int maxFrequency = (int) Math.round((double) baseFrequency + 0.63 * baseFrequency);
        int fluctuation = (int) Math.round(randomGenerator.nextInt((int) Math.round((double) volatility * 0.64)) / 100.0 * baseFrequency);
        if (randomGenerator.nextInt(100) < (int) Math.round(((double) maxFrequency - currentFrequency) / ((double) maxFrequency - minFrequency) * 100)) {
            newFrequency = currentFrequency + fluctuation;
        } else {
            newFrequency = currentFrequency - fluctuation;
        }
        if (newFrequency > maxFrequency) {
            newFrequency = maxFrequency;
        }
        if (newFrequency < minFrequency) {
            newFrequency = minFrequency;
        }
        oscillator.setCurrentFrequency(newFrequency);
    }
    
    public void fluctuateAmplitude() {
        fluctuateAmplitude(this.volatility);
    }
    
    public void fluctuateAmplitude(int volatility) {
        int newAmplitude = oscillator.getCurrentAmplitude().intValue();
        int baseAmplitude = oscillator.getBaseAmplitude();
        int currentAmplitude = oscillator.getCurrentAmplitude().intValue();
        int minAmplitude = (int) Math.round((double) baseAmplitude - 0.64 * baseAmplitude);
        int maxAmplitude = (int) Math.round((double) baseAmplitude + 0.63 * baseAmplitude);
        int fluctuation = (int) Math.round(randomGenerator.nextInt((int) Math.round((double) volatility * 0.64)) / 100.0 * baseAmplitude);
        if (randomGenerator.nextInt(100) < ((double) maxAmplitude - currentAmplitude) / ((double) maxAmplitude - minAmplitude) * 100) {
            newAmplitude = currentAmplitude + fluctuation;
        } else {
            newAmplitude = currentAmplitude - fluctuation;
        }
        if (newAmplitude > maxAmplitude) {
            newAmplitude = maxAmplitude;
        }
        if (newAmplitude < minAmplitude) {
            newAmplitude = minAmplitude;
        }
        oscillator.setCurrentAmplitude(newAmplitude);
    }
    
    public void fluctuatePhase() {
        fluctuatePhase(this.volatility);
    }
    
    public void fluctuatePhase(int volatility) {
        double newPhase = oscillator.getCurrentPhase().doubleValue();
        double basePhase = 0;
        double currentPhase = oscillator.getCurrentPhase().doubleValue();
        double minPhase = -(Math.PI);
        double maxPhase = Math.PI;
        double fluctuation = randomGenerator.nextInt((int) Math.round((double) volatility * 0.64)) / 100.0 * Math.PI;
        if (randomGenerator.nextInt(100) < ((double) maxPhase - currentPhase) / ((double) maxPhase - minPhase) * 100) {
            newPhase = currentPhase + fluctuation;
        } else {
            newPhase = currentPhase - fluctuation;
        }
        if (newPhase > maxPhase) {
            newPhase = maxPhase;
        }
        if (newPhase < minPhase) {
            newPhase = minPhase;
        }
        oscillator.setCurrentPhase(newPhase);
    }
    
    public void fluctuateAll() {
        fluctuateAll(this.volatility);
    }
    
    public void fluctuateAll(int volatility) {
        fluctuateFrequency(volatility);
        fluctuateAmplitude(volatility);
        fluctuatePhase(volatility);
    }
    
}
