/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.util.Duration;
import ui.Main;

/**
 *
 * @author Ville
 */
public class Breaker {
    PowerChannel channel;
    PowerLine line;
    String status;
    SimpleDoubleProperty breakerHeat;
    SimpleDoubleProperty breakerDelta;
    ui.StatusLed breakerLight;
    Button breakerButton;
    
    
    public Breaker(PowerChannel channel, PowerLine line) {
        this.channel = channel;
        this.line = line;
        this.status = "ok";
        this.breakerHeat = new SimpleDoubleProperty(0.0);
        this.breakerDelta = new SimpleDoubleProperty(0.0);
    }

    // Getters
    
    public String getStatus() {
        return this.status;
    }
    
    public SimpleDoubleProperty getBreakerHeat() {
        return this.breakerHeat;
    }
    
    public SimpleDoubleProperty getBreakerDelta() {
        return this.breakerDelta;
    }
    
    public ui.StatusLed getBreakerLight() {
        return this.breakerLight;
    }
    
    public Button getBreakerButton() {
        return this.breakerButton;
    }

    // Setters
    
    public void setStatus(String status) {
        this.status = status;
        if (status.equals("ok")) {
            getBreakerLight().setStatus("ok");
            getBreakerLight().setSlowBlink(false);
            getBreakerLight().setFastBlink(false);
        } else if (status.equals("warning")) {
            getBreakerLight().setStatus("warning");
            getBreakerLight().setSlowBlink(false);
            getBreakerLight().setFastBlink(false);
        } else if (status.equals("initialising")) {
            getBreakerLight().setStatus("warning");
            getBreakerLight().setSlowBlink(true);
            getBreakerLight().setFastBlink(false);
        } else if (status.equals("hot")) {
            getBreakerLight().setStatus("alert");
            getBreakerLight().setSlowBlink(false);
            getBreakerLight().setFastBlink(true);
        } else if (status.equals("broken")) {
            getBreakerLight().setStatus("off");
            getBreakerLight().setSlowBlink(false);
            getBreakerLight().setFastBlink(false);
        }
    }
    
    public void setBreakerLight(ui.StatusLed breakerLight) {
        this.breakerLight = breakerLight;
    }
    
    public void setBreakerButton(Button breakerButton) {
        this.breakerButton = breakerButton;
    }
    
    // Heat management methods
    
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
            // Imbalance heat factor
            if (this.line.getNumber() % 2 == 0 && this.channel.getOutputBalance().doubleValue() > 40.0) {
                heatDelta = heatDelta + (this.channel.getOutputBalance().doubleValue() / 2.0);
            } else if (this.line.getNumber() % 2 == 1 && this.channel.getOutputBalance().doubleValue() < -40.0) {
                heatDelta = heatDelta + (this.channel.getOutputBalance().doubleValue() / -2.0);
            } else if (this.line.getNumber() % 2 == 0 && this.channel.getOutputBalance().doubleValue() > 25.0) {
                heatDelta = heatDelta + (this.channel.getOutputBalance().doubleValue() / 5.0);
            } else if (this.line.getNumber() % 2 == 1 && this.channel.getOutputBalance().doubleValue() < -25.0) {
                heatDelta = heatDelta + (this.channel.getOutputBalance().doubleValue() / -5.0);
            } else if (this.line.getNumber() % 2 == 0 && this.channel.getOutputBalance().doubleValue() > 15.0) {
                heatDelta = heatDelta + (this.channel.getOutputBalance().doubleValue() / 10.0);
            } else if (this.line.getNumber() % 2 == 1 && this.channel.getOutputBalance().doubleValue() < -15.0) {
                heatDelta = heatDelta + (this.channel.getOutputBalance().doubleValue() / -10.0);
            }
        }
        // Power draw heat factor
        heatDelta = heatDelta - ((120 - Main.getPowerManager().getMainOutputLevel()) / 10);
        this.breakerDelta.set(heatDelta);
    }
    
    public void applyHeatDelta() {
        this.breakerHeat.set(this.breakerHeat.doubleValue() + this.breakerDelta.doubleValue());
        if (this.breakerHeat.doubleValue() <= 0) {
            this.breakerHeat.set(0);
        } else if (this.breakerHeat.doubleValue() >= 1000) {
            setStatus("hot");
        } else if (this.breakerHeat.doubleValue() >= 800 && getStatus().equals("ok")) {
            setStatus("warning");
        } else if (this.status.equals("hot") && this.breakerHeat.doubleValue() < 800 && getBreakerButton().isDisabled() == true) {
            setStatus("broken");
            getBreakerButton().setDisable(false);
        }
    }

    public void initialisingBreaker() {
        setStatus("initialising");
        Timer initialisingTimer = new Timer();
        TimerTask initialisingTask = new TimerTask() {
            public void run() {
                finishInitialisation();
            }
        };
        initialisingTimer.schedule(initialisingTask, 60000l);
        
    }
    
    public void finishInitialisation() {
        Platform.runLater(new Runnable() {
            public void run() {
                setStatus("ok");
                getBreakerButton().setDisable(true);
            }
        });
    }
    
    

    
    
}







