/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.Random;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;
import ui.Main;

/**
 *
 * @author Ville
 */
public class Manager {
    
    PowerLine[] powerLines;
    int lines;
    ReactorService reactorService;
    
    public Manager() {
        lines = 1;
        powerLines = new PowerLine[lines];
        
    }
    
    public PowerLine[] getPowerLines() {
        return powerLines;
    }
    
    public PowerLine getPowerLine(int number) {
        return powerLines[number];
    }

    // Reactor service taking care of timing
    
    static public void triggerFluctuation() {
        Platform.runLater(new Runnable() {
            public void run() {
                Random random = new Random();
                    int reactorLine = random.nextInt(1);
                    Main.getPowerManager().getPowerLine(reactorLine).getInputFluctuator().fluctuateAll();
            }
        });
    }
    
    
    public void setReactorPeriod(int period) {
        reactorService.setPeriod(Duration.seconds(period));
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
                    // Randomize the reactor line to fluctuate
                    triggerFluctuation();
                    return null;
                }
            };
        }
    }
    
    
}


