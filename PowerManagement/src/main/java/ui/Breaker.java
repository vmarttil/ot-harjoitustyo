/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
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
public class Breaker extends StackPane {
    int line;
    String status; 
    boolean warningBlink;
    boolean brokenBlink;
    Image breakerImage;
    Image brokenOnImage;
    Image repairingImage;
    Image warningImage;
    Image okImage;
    ImageView display;
    Timeline warningBlinkTimeline;
    Timeline brokenBlinkTimeline;
    
    public Breaker(int line) {
        this.line = line;
        this.status = "ok";
        this.warningBlink = false;
        this.brokenBlink = false;        
        // Images for left breakers
        if (line % 2 == 0) {
            this.brokenOffImage = new Image(this.getClass().getClassLoader().getResource("graphics/left_breaker_broken_dark.png").toString());
            this.brokenOnImage = new Image(this.getClass().getClassLoader().getResource("graphics/left_breaker_broken_red.png").toString());
            this.repairingImage = new Image(this.getClass().getClassLoader().getResource("graphics/left_breaker_broken_yellow.png").toString());
            this.warningImage = new Image(this.getClass().getClassLoader().getResource("graphics/left_breaker_intact_yellow.png").toString());
            this.okImage = new Image(this.getClass().getClassLoader().getResource("graphics/left_breaker_intact_green.png").toString());
        } else {
        // Images for right breakers
            this.brokenOffImage = new Image(this.getClass().getClassLoader().getResource("graphics/right_breaker_broken_dark.png").toString());
            this.brokenOnImage = new Image(this.getClass().getClassLoader().getResource("graphics/right_breaker_broken_red.png").toString());
            this.repairingImage = new Image(this.getClass().getClassLoader().getResource("graphics/right_breaker_broken_yellow.png").toString());
            this.warningImage = new Image(this.getClass().getClassLoader().getResource("graphics/right_breaker_intact_yellow.png").toString());
            this.okImage = new Image(this.getClass().getClassLoader().getResource("graphics/right_breaker_intact_green.png").toString());
        }
        this.display = new ImageView(okImage);
        this.display.setFitHeight(194.0);
        this.display.setFitWidth(338.0);
        this.setAlignment(Pos.CENTER);
        this.getChildren().add(this.display);
        this.warningBlinkTimeline = setupBlinkTimeline(200, "warning");
        this.brokenBlinkTimeline = setupBlinkTimeline(500, "broken");
    }
    
    private Timeline setupBlinkTimeline(int interval, String type) {
        Timeline timeline = new Timeline();
        if (type.equals("warning")) {
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(interval),
                event -> {
                    if (this.display.getImage().equals(okImage)) {
                        this.display.setImage(warningImage);
                    } else {
                        this.display.setImage(okImage);
                    }
                }));
        } 
        if (type.equals("broken")) {
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(interval),
                event -> {
                    if (this.display.getImage().equals(brokenOffImage)) {
                        if (this.status.equals("broken")) {
                            this.display.setImage(brokenOnImage);
                        } else if (this.status.equals("repairing")) {
                            this.display.setImage(repairingImage);
                        }
                    } else {
                        this.display.setImage(brokenOffImage);
                    }
                }));
        }
        timeline.setCycleCount(Animation.INDEFINITE);
        return timeline;
    }
    
    // Getters

    public String getstatus() {
        return this.status;
    }

    public boolean isWarningBlink() {
        return this.warningBlink;
    }
    
    public boolean isBrokenBlink() {
        return this.brokenBlink;
    }
    
    // Setters
    
    public void setStatus(String status) {
        this.status = status;
        if (this.status.equals("broken")) {
            this.display.setImage(brokenOffImage);
            setBrokenBlink(true);
            setWarningBlink(false);
        } else if (this.status.equals("repairing")) {
            this.display.setImage(repairingImage);
            setBrokenBlink(true);
            setWarningBlink(false);
        } else if (this.status.equals("warning")) {
            this.display.setImage(warningImage);
            setWarningBlink(true);
        } else if (this.status.equals("ok")) {
            this.display.setImage(okImage);
            setBrokenBlink(false);
            setWarningBlink(false);
        }
    }

    public void setWarningBlink(boolean blink) {
        this.warningBlink = blink;
        if (this.warningBlink == true) {
            this.warningBlinkTimeline.play();
            this.brokenBlinkTimeline.stop();
        } else {
            this.warningBlinkTimeline.stop();
            if (this.brokenBlink == true) {
                this.brokenBlinkTimeline.play();
            }
        }
    } 
    
    public void setBrokenBlink(boolean blink) {
        this.brokenBlink = blink;
        if (this.brokenBlink == true) {
            this.brokenBlinkTimeline.play();
            this.warningBlinkTimeline.stop();
        } else {
            this.brokenBlinkTimeline.stop();
            if (this.warningBlink == true) {
                this.warningBlinkTimeline.play();
            }
        }
    }
}
