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
    private static final Logger ERRORLOGGER = Logger.getLogger(Manager.class.getName());
    PowerLine[] powerLines;
    int lines;
    ReactorService reactorService;
    
    public Manager() {
        lines = 4;
        powerLines = new PowerLine[lines];
        
    }
    
    // Getters
    
    public PowerLine[] getPowerLines() {
        return powerLines;
    }
    
    public PowerLine getPowerLine(int number) {
        return powerLines[number];
    }

    // Setters
    
    public void setReactorPeriod(int period) {
        reactorService.setPeriod(Duration.seconds(period));
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
    
    
    public void startReactorService(int period) {
        reactorService = new ReactorService();
        reactorService.setPeriod(Duration.seconds(period));
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


