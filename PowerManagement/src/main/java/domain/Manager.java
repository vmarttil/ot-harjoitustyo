/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

/**
 *
 * @author Ville
 */
public class Manager {
    
    PowerLine[] powerLines;
    int lines;
    
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
    
    
    
}
