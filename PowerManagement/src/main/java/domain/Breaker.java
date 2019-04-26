/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Button;
import ui.Main;

/**
 * The class defines a power breaker that will monitor the cumulative heat 
 * produced by the different stages of the power line and channel and if 
 * the accumulated heat reaches a threshold value, the breaker cuts the circuit 
 * and allows it to cool. 
 * 
 * @author Ville
 */
public class Breaker {
    private PowerChannel channel;
    private PowerLine line;
    private String status;
    private SimpleDoubleProperty breakerHeat;
    private SimpleDoubleProperty breakerDelta;
    private int breakThreshold;
    private ui.StatusLed breakerLight;
    private Button breakerButton;
    
    /**
     * The constructor defines a power breaker monitoring the power channel 
     * and power line indicated by the given parameters, and with a threshold 
     * defined by the third parameter.
     * @param channel the power channel monitored by the breaker
     * @param line the power line monitored by the breaker
     * @param threshold the threshold at which the breaker breaks
     */
    public Breaker(PowerChannel channel, PowerLine line, int threshold) {
        this.channel = channel;
        this.line = line;
        this.breakThreshold = threshold;
        this.status = "ok";
        this.breakerHeat = new SimpleDoubleProperty(0.0);
        this.breakerDelta = new SimpleDoubleProperty(0.0);
    }

    // Getters
    
    /**
     * The method returns the current status of the breaker as a string, the 
     * options being: 
     *      "ok" (heat level normal), 
     *      "warning" (heat level over 80% of maximum), 
     *      "hot" (heat level over threshold, breaker broken), 
     *      "broken" (breaker broken but heat level under 80%) and 
     *      "resetting" (breaker is being reset to a closed position).
     * @return current status of the breaker
     */
    public String getStatus() {
        return this.status;
    }
    
    /**
     * The method returns the current heat level of the breaker as a simple 
     * double property (observable value).
     * @return current heat level 
     */
    public SimpleDoubleProperty getBreakerHeat() {
        return this.breakerHeat;
    }
    
    /**
     * The method returns the breaker heat delta, or the rate of heat 
     * accumulation or dissipation per second, as a simple double property 
     * (observable value).
     * @return breaker heat delta
     */
    public SimpleDoubleProperty getBreakerDelta() {
        return this.breakerDelta;
    }
    
    /**
     * The method returns the breaking threshold of the breaker as an integer.
     * @return break threshold
     */
    public int getBreakThreshold() {
        return this.breakThreshold;
    }
    
    /**
     * The method returns the status led associated with this breaker as a 
     * StatusLed object.
     * @return breaker status led object
     */
    public ui.StatusLed getBreakerLight() {
        return this.breakerLight;
    }
    
    /**
     * The method returns the button object used to reset the breaker after it 
     * has been tripped by excess heat accumulation.
     * @return breaker reset button
     */
    public Button getBreakerButton() {
        return this.breakerButton;
    }

    // Setters
    /**
     * The method sets the status of the breaker to the value indicated by the 
     * parameter and sets the state of the status led in the UI accordingly. The 
     * possible statuses of the breaker are:
     *      "ok" (heat level normal), 
     *      "warning" (heat level over 80% of maximum), 
     *      "hot" (heat level over threshold, breaker broken), 
     *      "broken" (breaker broken but heat level under 80%) and 
     *      "resetting" (breaker is being reset to a closed position).
     * @param status status of the breaker 
     */
    public void setStatus(String status) {
        this.status = status;
        if (status.equals("ok") || status.equals("warning") || status.equals("broken")) {
            getBreakerLight().setSlowBlink(false);
            getBreakerLight().setFastBlink(false);
            if (status.equals("ok")) {
                getBreakerLight().setStatus("ok");
            } else if (status.equals("warning")) {
                getBreakerLight().setStatus("warning");
            } else {
                getBreakerLight().setStatus("off");
            }
        } else if (status.equals("resetting")) {
            getBreakerLight().setStatus("warning");
            getBreakerLight().setSlowBlink(true);
            getBreakerLight().setFastBlink(false);
        } else if (status.equals("hot")) {
            getBreakerLight().setStatus("alert");
            getBreakerLight().setSlowBlink(false);
            getBreakerLight().setFastBlink(true);   
        }
    }
    /**
     * The method associates the StatusLed object given as a parameter with 
     * this breaker.
     * @param breakerLight the status led of this breaker
     */
    public void setBreakerLight(ui.StatusLed breakerLight) {
        this.breakerLight = breakerLight;
    }
    
    /**
     * The method associates the Button object given as a parameter with this 
     * breaker.
     * @param breakerButton the reset button of this breaker
     */
    public void setBreakerButton(Button breakerButton) {
        this.breakerButton = breakerButton;
    }
    
    // Heat management methods
    
    /**
     * The method calculates the current total heat accumulation per second 
     * for this breaker, based on the current values of the power circuit and 
     * sets it as the value of the breakerDelta variable.
     */
    public void calculateHeatDelta() {
        double heatDelta = 0;
        if ((this.status.equals("ok") || this.status.equals("warning")) && this.line.isOnline() == true) {
            // Over- or underdrive heat factor
            heatDelta = heatDelta + ((this.line.getInputPower() - 100.0) / 10);
            // Irregularity heat factor
            heatDelta = heatDelta + (this.line.getRms() / 10);
            // Instability heat factor
            if (this.line.isUnstable() == true) {
                heatDelta = 5 * heatDelta;
            }
            heatDelta = heatDelta + calculateImbalanceHeat();
        }
        // Power draw heat factor
        heatDelta = heatDelta - ((120 - Main.getPowerManager().getMainOutputLevel()) / 10);
        this.breakerDelta.set(heatDelta);
    }
    
    /**
     * The method, called by the calculateHeatDelta method, calculates the 
     * heat accumulation resulting from imbalance in the balancer component 
     * of the power channel.
     * @return the heat accumulation caused by imbalance in the power channel
     */
    private double calculateImbalanceHeat() {
        double imbalanceHeat = 0;
        if (this.line.getNumber() % 2 == 0 && this.channel.getOutputBalance().doubleValue() > 40.0) {
            imbalanceHeat = imbalanceHeat + (this.channel.getOutputBalance().doubleValue() / 2.0);
        } else if (this.line.getNumber() % 2 == 1 && this.channel.getOutputBalance().doubleValue() < -40.0) {
            imbalanceHeat = imbalanceHeat + (this.channel.getOutputBalance().doubleValue() / -2.0);
        } else if (this.line.getNumber() % 2 == 0 && this.channel.getOutputBalance().doubleValue() > 25.0) {
            imbalanceHeat = imbalanceHeat + (this.channel.getOutputBalance().doubleValue() / 5.0);
        } else if (this.line.getNumber() % 2 == 1 && this.channel.getOutputBalance().doubleValue() < -25.0) {
            imbalanceHeat = imbalanceHeat + (this.channel.getOutputBalance().doubleValue() / -5.0);
        } else if (this.line.getNumber() % 2 == 0 && this.channel.getOutputBalance().doubleValue() > 15.0) {
            imbalanceHeat = imbalanceHeat + (this.channel.getOutputBalance().doubleValue() / 10.0);
        } else if (this.line.getNumber() % 2 == 1 && this.channel.getOutputBalance().doubleValue() < -15.0) {
            imbalanceHeat = imbalanceHeat + (this.channel.getOutputBalance().doubleValue() / -10.0);
        }
        return imbalanceHeat;
    }
    
    /**
     * The method, called by a timer once per second, updates the breakerHeat 
     * variable based on the current heat delta and triggers any effects it 
     * might have on th breaker.
     */
    public void applyHeatDelta() {
        this.breakerHeat.set(this.breakerHeat.doubleValue() + this.breakerDelta.doubleValue());
        if (this.breakerHeat.doubleValue() <= 0) {
            this.breakerHeat.set(0);
        } else if (this.breakerHeat.doubleValue() >= this.breakThreshold) {
            setStatus("hot");
        } else if (this.breakerHeat.doubleValue() >= 0.8 * this.breakThreshold && getStatus().equals("ok")) {
            setStatus("warning");
        } else if (this.status.equals("hot") && this.breakerHeat.doubleValue() < 800 && getBreakerButton().isDisabled() == true) {
            setStatus("broken");
            getBreakerButton().setDisable(false);
        }
    }

    /**
     * The method starts the reset process of the breaker, taking 60 seconds.
     */
    public void resetBreaker() {
        setStatus("resetting");
        Timer initialisingTimer = new Timer();
        TimerTask initialisingTask = new TimerTask() {
            public void run() {
                finishResetBreaker();
            }
        };
        initialisingTimer.schedule(initialisingTask, 60000l);
        
    }
    
    /**
     * The method, triggered by a timer finishes the reset process of the 
     * breaker and returns it to working order.
     */
    public void finishResetBreaker() {
        Platform.runLater(new Runnable() {
            public void run() {
                setStatus("ok");
                getBreakerButton().setDisable(true);
            }
        });
    }
}







