/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import javafx.scene.paint.Color;

/**
 *
 * @author Ville
 */
public class Led {
    Color color; 
    boolean blink;
    int brightness;
    
    public Led() {
        this.color = Color.SLATEGRAY;
        this.blink = false;
        this.brightness = 0;
    }
    
    // Getters

    public Color getColor() {
        return this.color;
    }

    public boolean isBlink() {
        return this.blink;
    }

    public int getBrightness() {
        return this.brightness;
    }
    
    // Setters

    public void setColor(Color color) {
        this.color = color;
    }

    public void setBlink(boolean blink) {
        this.blink = blink;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }
    
    
    
    
    
}
