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
import javafx.util.Duration;

/**
 *
 * @author Ville
 */
public class StatusLed extends StackPane {
    private String status; 
    private boolean slowBlink;
    private boolean fastBlink;
    private Image offImage;
    private Image okImage;
    private Image warningImage;
    private Image alertImage;
    private ImageView display;
    private Timeline slowBlinkTimeline;
    private Timeline fastBlinkTimeline;
    
    public StatusLed() {
        this.status = "off";
        this.slowBlink = false;
        this.fastBlink = false;
        this.offImage = new Image(this.getClass().getClassLoader().getResource("graphics/dark_led.png").toString());
        this.okImage = new Image(this.getClass().getClassLoader().getResource("graphics/green_led.png").toString());
        this.warningImage = new Image(this.getClass().getClassLoader().getResource("graphics/yellow_led.png").toString());
        this.alertImage = new Image(this.getClass().getClassLoader().getResource("graphics/red_led.png").toString());
        this.display = new ImageView(okImage);
        this.display.setFitHeight(15.0);
        this.display.setFitWidth(15.0);
        this.setAlignment(Pos.CENTER);
        this.getChildren().add(this.display);
        this.slowBlinkTimeline = setupBlinkTimeline(500);
        this.fastBlinkTimeline = setupBlinkTimeline(200);
        this.setMouseTransparent(true);
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

    public boolean isSlowBlink() {
        return this.slowBlink;
    }
    
    public boolean isFastBlink() {
        return this.fastBlink;
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

    public void setSlowBlink(boolean blink) {
        this.slowBlink = blink;
        if (this.slowBlink == true) {
            this.slowBlinkTimeline.play();
            this.fastBlinkTimeline.stop();
        } else {
            this.slowBlinkTimeline.stop();
            if (this.fastBlink == true) {
                this.fastBlinkTimeline.play();
            }
        }
    } 
    
    public void setFastBlink(boolean blink) {
        this.fastBlink = blink;
        if (this.fastBlink == true) {
            this.fastBlinkTimeline.play();
            this.slowBlinkTimeline.stop();
        } else {
            this.fastBlinkTimeline.stop();
            if (this.slowBlink == true) {
                this.slowBlinkTimeline.play();
            }
        }
    }
    
    
    
}
