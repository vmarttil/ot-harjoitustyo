/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import ui.Main;

/**
 *
 * @author Ville
 */
public class PowerChannel {
    int number;
    Manager manager;
    PowerLine leftPowerLine;
    PowerLine rightPowerLine;
    Breaker leftBreaker;
    Breaker rightBreaker;
    SimpleDoubleProperty outputPower;
    SimpleIntegerProperty balancerValue;
    SimpleDoubleProperty outputBalance;
    
    public PowerChannel(Manager manager, int number) {
        this.number = number;
        this.manager = manager;
        this.leftPowerLine = manager.getPowerLine(2 * number);
        this.rightPowerLine = manager.getPowerLine(2 * number + 1);
        this.outputPower = new SimpleDoubleProperty((this.leftPowerLine.getOutputPower().doubleValue() + this.rightPowerLine.getOutputPower().doubleValue()) / 2.0);
        this.balancerValue = new SimpleIntegerProperty(0);
        this.outputBalance = new SimpleDoubleProperty(0);
        this.leftBreaker = new Breaker(this, this.leftPowerLine);
        this.rightBreaker = new Breaker(this, this.rightPowerLine);
        manager.getPowerLine(2 * number).setChannel(this);
        manager.getPowerLine(2 * number + 1).setChannel(this);
    }
    
    // Getters

    public PowerLine getLeftPowerLine() {
        return this.leftPowerLine;
    }

    public PowerLine getRightPowerLine() {
        return this.rightPowerLine;
    }

    public Breaker getLeftBreaker() {
        return this.leftBreaker;
    }
    
    public Breaker getRightBreaker() {
        return this.rightBreaker;
    }
    
    public SimpleDoubleProperty getOutputPower() {
        return this.outputPower;
    }

    public SimpleIntegerProperty getBalancerValue() {
        return this.balancerValue;
    }

    public SimpleDoubleProperty getOutputBalance() {
        return this.outputBalance;
    }

    
    // Setters

    public void setOutputPower(SimpleDoubleProperty outputPower) {
        this.outputPower = outputPower;
    }

    public void setBalancerValue(SimpleIntegerProperty value) {
        this.balancerValue = value;
    }

    public void setOutputBalance(SimpleDoubleProperty outputBalance) {
        this.outputBalance = outputBalance;
    }

    
    // Adjusting the power channel output
    
    public void updateOutput() {
        double leftInput;
        double rightInput;
        double balance;
        double leftOutputPower;
        double rightOutputPower;
        // Checking for breakers
        if (this.leftBreaker.getStatus().equals("broken") || this.leftBreaker.getStatus().equals("hot") || this.leftBreaker.getStatus().equals("initialising")) {
            leftInput = 0.0;
        } else {
            leftInput = this.leftPowerLine.getOutputPower().doubleValue();
        }
        if (this.rightBreaker.getStatus().equals("broken") || this.rightBreaker.getStatus().equals("hot") || this.rightBreaker.getStatus().equals("initialising")) {
            rightInput = 0.0;
        } else {
            rightInput = this.rightPowerLine.getOutputPower().doubleValue();
        }
        // Calculate output balance
        if (leftInput == 0 && rightInput == 0) {
            balance = (this.balancerValue.intValue() / 2) / 64.0 * 100.0;;
        } else if (leftInput <= rightInput) {         
            balance = Math.max(((int) Math.round(leftInput / rightInput * 64) - 64) + this.balancerValue.intValue(), -32) / 64.0 * 100.0;
        } else {
            balance = Math.min((63 - (int) Math.round(rightInput / leftInput * 63)) + this.balancerValue.intValue(), 32) / 63.0 * 100.0;
        }
        this.outputBalance.set(balance);
        // Check for instability and fluctuations
        if (balance >= -15.0 && balance <= 15.0) {
            this.rightPowerLine.setStability(50);
            this.leftPowerLine.setStability(50);
            this.rightPowerLine.setImbalance(0);
            this.leftPowerLine.setImbalance(0);
        } else if (balance < -15.0 && balance >= -25.0) {
            this.rightPowerLine.setStability(50 + (int) Math.round(balance));
            this.leftPowerLine.setStability(50);
            this.rightPowerLine.setImbalance(0);
            this.leftPowerLine.setImbalance(0);
        } else if (balance > 15.0 && balance <= 25.0) {
            this.rightPowerLine.setStability(50);
            this.leftPowerLine.setStability(50 - (int) Math.round(Math.abs(balance)));
            this.rightPowerLine.setImbalance(0);
            this.leftPowerLine.setImbalance(0);
        } else if (balance < -25.0 && balance >= -40.0) {
            this.rightPowerLine.setStability(50 + (int) Math.round(balance));
            this.leftPowerLine.setStability(50);
            this.rightPowerLine.setImbalance(1);
            this.leftPowerLine.setImbalance(0);
        } else if (balance > 25.0 && balance <= 40.0) {
            this.rightPowerLine.setStability(50);
            this.leftPowerLine.setStability(50 + (int) Math.round(balance));
            this.rightPowerLine.setImbalance(0);
            this.leftPowerLine.setImbalance(1);
        } else if (balance < -40.0) {
            this.rightPowerLine.setStability(50 + (int) Math.round(balance));
            this.leftPowerLine.setStability(50);
            this.rightPowerLine.setImbalance(2);
            this.leftPowerLine.setImbalance(0);
        } else if (balance > 40.0) {
            this.rightPowerLine.setStability(50);
            this.leftPowerLine.setStability(50 + (int) Math.round(balance));
            this.rightPowerLine.setImbalance(0);
            this.leftPowerLine.setImbalance(2);
        }
        // Calculate output power
        if (this.balancerValue.intValue() >= 0) {
            rightOutputPower = rightInput;
        } else {
            rightOutputPower = (this.balancerValue.intValue() + 64) / 64.0 * rightInput;
        }
        if (this.balancerValue.intValue() <= 0) {
            leftOutputPower = leftInput;
        } else {
            leftOutputPower = (this.balancerValue.intValue() - 63) / -63.0 * leftInput;
        }
        this.outputPower.set((leftOutputPower + rightOutputPower) / 2.0);
        if (getOutputBalance().doubleValue() > 25.0 || getOutputBalance().doubleValue() < -25.0) {
                Main.getBalanceGauges()[number].setLedOn(true);
        } else {
            Main.getBalanceGauges()[number].setLedOn(false);
        }
        // Calculate heat production or dissipation in the breakers
        this.leftBreaker.calculateHeatDelta();
        this.rightBreaker.calculateHeatDelta();
        
    }
    
    
    
}
