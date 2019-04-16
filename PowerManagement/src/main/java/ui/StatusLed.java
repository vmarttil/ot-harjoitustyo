/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 *
 * @author Ville
 */
public class StatusLed extends StackPane {
    private static final Logger ERRORLOGGER = Logger.getLogger(StatusLed.class.getName());
    String status; 
    boolean blink;
    Image offImage;
    Image okImage;
    Image warningImage;
    Image alertImage;
    Circle testDisplay;
    ImageView display;
    // PauseTransition blinkTransition;
    Timeline slowBlinkTimeline;
    Timeline fastBlinkTimeline;
    
    
    public StatusLed() {
        this.status = "off";
        this.blink = false;
        this.offImage = loadImage("src/main/resources/dark_led.png");
        this.okImage = loadImage("src/main/resources/green_led.png");
        this.warningImage = loadImage("src/main/resources/yellow_led.png");
        this.alertImage = loadImage("src/main/resources/red_led.png");
        this.testDisplay = new Circle(50.0, Paint.valueOf("BLACK"));
        this.display = new ImageView(okImage);
        this.display.setFitHeight(30.0);
        this.display.setFitWidth(30.0);
        this.setAlignment(Pos.CENTER);
        this.getChildren().add(this.display);
        this.slowBlinkTimeline = setupBlinkTimeline(500);
        this.fastBlinkTimeline = setupBlinkTimeline(200);
    }

    private Image loadImage(String filepath) {
        Image image = null;
        File imageFile = new File(filepath);
        try (FileInputStream offImageInputStream = new FileInputStream(imageFile);) {
            image = new Image(offImageInputStream, 50, 0, false, false);
	} catch (FileNotFoundException e) {
            ERRORLOGGER.log(Level.SEVERE, "File " + imageFile + " not found.", e);
	} catch (IOException e) {
            ERRORLOGGER.log(Level.SEVERE, "Failed to load file " + imageFile + ".", e);
	}
        return image;
    }
    
    private Timeline setupBlinkTimeline(int interval) {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(interval),
            event -> {
                if (this.display.getImage().equals(offImage)) {
                    if (this.status.equals("off")) {
                        this.display.setImage(offImage);
                    } else if (this.status.equals("ok")) {
                        this.display.setImage(okImage);
                    } else if (this.status.equals("warning")) {
                        this.display.setImage(warningImage);
                    } else if (this.status.equals("alert")) {
                        this.display.setImage(alertImage);
                    }
                } else {
                    this.display.setImage(offImage);
                }
            }));
        timeline.setCycleCount(Animation.INDEFINITE);
        return timeline;
    }
    
    // Getters

    public String getstatus() {
        return this.status;
    }

    public boolean isBlink() {
        return this.blink;
    }
    
    // Setters
    
    public void setStatus(String status) {
        this.status = status;
        if (this.status.equals("off")) {
            this.display.setImage(offImage);
        } else if (this.status.equals("ok")) {
            this.display.setImage(okImage);
        } else if (this.status.equals("warning")) {
            this.display.setImage(warningImage);
        } else if (this.status.equals("alert")) {
            this.display.setImage(alertImage);
        }
    }

    public void setBlink(boolean blink) {
        this.blink = blink;
        if (this.blink == true) {
            this.slowBlinkTimeline.play();
        } else {
            this.slowBlinkTimeline.stop();
        }
    } 
    
    public void setRapidBlink(boolean blink) {
        this.blink = blink;
        if (this.blink == true) {
            this.fastBlinkTimeline.play();
        } else {
            this.fastBlinkTimeline.stop();
        }
    }
        
    
}
