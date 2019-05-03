/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.Random;

/**
 * The class defines a fluctuator that generates random variation in the 
 * frequency, amplitude and phase of a linked oscillator object, based on the 
 * parameters defined in the constructor. 
 * 
 * @author Ville Marttila
 */
public class Fluctuator {
    private Oscillator oscillator;
    private int volatility;
    private Random randomGenerator;
    
    /**
     * The constructor defines a fluctuator based on the given parameters.
     * @param oscillator the oscillator to which the fluctuator is linked
     * @param volatility the initial volatility, from 0 to 100, of the 
     * fluctuator 
     */
    public Fluctuator(Oscillator oscillator, int volatility) {
        this.oscillator = oscillator;
        this.volatility = volatility;
        this.randomGenerator = new Random();
    }
    
    // Getters
    /**
     * The method returns the maximum percentage (from 0 to 100) of the total 
     * variation range (+/- 64% of base value for frequency and amplitude and 
     * +/- PI for phase) that a parameter can change in one fluctuation event.
     * @return the volatility of the fluctuator as a percentage
     */
    public int getVolatility() {
        return this.volatility;
    }
    
    // Setters
    /**
     * The method sets the default volatility of the fluctuator, or the 
     * maximum percentage (from 0 to 100) of the total variation range 
     * (+/- 64% of base value for frequency and amplitude and +/- PI for 
     * phase) that a parameter can change in one fluctuation event.
     * @param volatility the volatility to be set for the fluctuator as a percentage
     */
    public void setVolatility(int volatility) {
        this.volatility = volatility;
    }
    
    // Methods for causing fluctuation in the oscillator
    /**
     * The method triggers the fluctuation of the oscillator's frequency, using 
     * the default volatility of the fluctuator.
     */
    public void fluctuateFrequency() {
        fluctuateFrequency(this.volatility, this.randomGenerator);
    }
    
    /**
     * The method triggers the fluctuation of the oscillator's frequency, using 
     * the specified volatility.
     * @param volatility the volatility to be used for this fluctuation event
     */
    public void fluctuateFrequency(int volatility) {
        fluctuateFrequency(volatility, this.randomGenerator);
    }
    
    /**
     * The method triggers the fluctuation of the oscillator's frequency, using 
     * the specified volatility and the specified random generator.
     * @param volatility the volatility to be used for this fluctuation event
     * @param randomGenerator the random generator to be used for this 
     * fluctuation event
     */
    public void fluctuateFrequency(int volatility, Random randomGenerator) {
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
    
    
    
    /**
     * The method triggers the fluctuation of the oscillator's amplitude, using 
     * the default volatility of the fluctuator.
     */
    public void fluctuateAmplitude() {
        fluctuateAmplitude(this.volatility, this.randomGenerator);
    }
    
    /**
     * The method triggers the fluctuation of the oscillator's frequency, using 
     * the specified volatility.
     * @param volatility the volatility to be used for this fluctuation event
     */
    public void fluctuateAmplitude(int volatility) {
        fluctuateAmplitude(volatility, this.randomGenerator);
    }
    
    /**
     * The method triggers the fluctuation of the oscillator's frequency, using 
     * the specified volatility and the specified random generator..
     * @param volatility the volatility to be used for this fluctuation event
     * @param randomGenerator the random generator to be used for this 
     * fluctuation event
     */
    public void fluctuateAmplitude(int volatility, Random randomGenerator) {
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
    
    /**
     * The method triggers the fluctuation of the oscillator's phase, using 
     * the default volatility of the fluctuator.
     */
    public void fluctuatePhase() {
        fluctuatePhase(this.volatility, this.randomGenerator);
    }
    
    /**
     * The method triggers the fluctuation of the oscillator's frequency, using 
     * the specified volatility.
     * @param volatility the volatility to be used for this fluctuation event
     */
    public void fluctuatePhase(int volatility) {
        fluctuatePhase(volatility, this.randomGenerator);
    }
    
    /**
     * The method triggers the fluctuation of the oscillator's frequency, using 
     * the specified volatility and the specified random generator.
     * @param volatility the volatility to be used for this fluctuation event
     * @param randomGenerator the random generator to be used for this 
     * fluctuation event
     */
    public void fluctuatePhase(int volatility, Random randomGenerator) {
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
    
    /**
     * The method triggers the simultaneous fluctuation of the oscillator's 
     * frequency, amplitude and phase, using the default volatility of the 
     * fluctuator.
     */
    public void fluctuateAll() {
        fluctuateAll(this.volatility, this.randomGenerator);
    }
    
    /**
     * The method triggers the simultaneous fluctuation of the oscillator's 
     * frequency, amplitude and phase using the specified volatility.
     * @param volatility the volatility to be used for this fluctuation event
     */
    public void fluctuateAll(int volatility) {
        fluctuateAll(volatility, this.randomGenerator);
    }
    
    /**
     * The method triggers the simultaneous fluctuation of the oscillator's 
     * frequency, amplitude and phase using the specified volatility and the 
     * specified random generator.
     * @param volatility the volatility to be used for this fluctuation event
     * @param randomGenerator the random generator to be used for this 
     * fluctuation event
     */
    public void fluctuateAll(int volatility, Random randomGenerator) {
        fluctuateFrequency(volatility, randomGenerator);
        fluctuateAmplitude(volatility, randomGenerator);
        fluctuatePhase(volatility, randomGenerator);
    }
}
