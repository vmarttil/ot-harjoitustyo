/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.Random;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;
import ui.Main;
import ui.StatusLed;

/**
 *
 * @author Ville
 */
public class Manager {
    PowerLine[] powerLines;
    PowerChannel[] powerChannels;
    int lines;
    ReactorService reactorService;
    int baseReactorPeriod;
    
    public Manager() {
        this.lines = 4;
        this.powerLines = new PowerLine[this.lines];
        this.powerChannels = new PowerChannel[this.lines / 2];  
        this.baseReactorPeriod = 5;
    }
    
    // Getters
    
    public PowerLine[] getPowerLines() {
        return this.powerLines;
    }
    
    public PowerLine getPowerLine(int number) {
        return this.powerLines[number];
    }

    public PowerChannel[] getPowerChannels() {
        return this.powerChannels;
    }
    
    public PowerChannel getPowerChannel(int number) {
        return this.powerChannels[number];
    }
    
    // Setters
    
    public void setReactorPeriod(int period) {
        reactorService.setPeriod(Duration.seconds(period));
    }
    
    // Creating power lines and channels
    
    public void createPowerLines(int lines) {
        for (int i = 0; i < lines; i++) {
            PowerLine line = new PowerLine(this, i);
            getPowerLines()[i] = line;
        }
    }
    
    public void createPowerChannels(int channels) {
        for (int i = 0; i < channels; i++) {
            PowerChannel channel = new PowerChannel(this, i);
            getPowerChannels()[i] = channel;
        }
    }
    
    // Reactor service taking care of timing
    
    static public void triggerFluctuation() {
        Platform.runLater(new Runnable() {
            public void run() {
                Random random = new Random();
                int reactorLine = random.nextInt(4);
                Main.getPowerManager().getPowerLine(reactorLine).fluctuateLine();
            }
        });
    }
    
    
    public void startReactorService() {
        reactorService = new ReactorService();
        reactorService.setPeriod(Duration.seconds(baseReactorPeriod));
        reactorService.start();
    }

    private static class ReactorService extends ScheduledService<Void> {
        protected Task<Void> createTask() {
            return new Task<Void>() {
                protected Void call() {
                    triggerFluctuation();
                    return null;
                }
            };
        }
    }
    
    
}


