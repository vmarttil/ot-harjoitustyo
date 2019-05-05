/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import eu.hansolo.medusa.Gauge;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * The class defines the intermediate component of the power management system, 
 * the power channel, which connects the input power lines to the main output 
 * and contains two power breakers at the input and a balancer that can be used 
 * to maintain a balance between the two input lines. The breakers monitor the 
 * heat generated in the system, display it on a gauge and break if too much 
 * heat is generated in the channel input.
 * 
 * @author Ville
 */
public class PowerChannel {
    private int number;
    private Manager manager;
    private PowerLine leftPowerLine;
    private PowerLine rightPowerLine;
    private Breaker leftBreaker;
    private Breaker rightBreaker;
    private SimpleDoubleProperty outputPower;
    private SimpleIntegerProperty balancerValue;
    private SimpleDoubleProperty outputBalance;
    private Gauge balanceGauge;
    
    /**
     * The constructor defines the power channel object, creates the balancer 
     * and the breakers and sets up links between the two input power lines and 
     * the channel, so that they can interact with each other.
     * @param manager the Manager object controlling the channel
     * @param number the number of the channel
     */
    public PowerChannel(Manager manager, int number) {
        this.number = number;
        this.manager = manager;
        this.leftPowerLine = manager.getPowerLine(2 * number);
        this.rightPowerLine = manager.getPowerLine(2 * number + 1);
        this.outputPower = new SimpleDoubleProperty((this.leftPowerLine.getOutputPower().doubleValue() + this.rightPowerLine.getOutputPower().doubleValue()) / 2.0);
        this.balancerValue = new SimpleIntegerProperty(0);
        this.outputBalance = new SimpleDoubleProperty(0);
        this.leftBreaker = new Breaker(this, this.leftPowerLine, 1000);
        this.rightBreaker = new Breaker(this, this.rightPowerLine, 1000);
        manager.getPowerLine(2 * number).setChannel(this);
        manager.getPowerLine(2 * number + 1).setChannel(this);
    }
    
     /**
     * The method returns the number of the channel.
     * @return number of the channel
     */
    public int getNumber() {
        return this.number;
    }
    
    /**
     * The method returns the power manager controlling the channel.
     * @return manager of the channel
     */
    public Manager getManager() {
        return this.manager;
    }
    
    /**
     * The method returns the PowerLine object serving as the left input line 
     * of the channel.
     * @return left input line
     */
    public PowerLine getLeftPowerLine() {
        return this.leftPowerLine;
    }

    /**
     * The method returns the PowerLine object serving as the right input line 
     * of the channel.
     * @return right input line
     */
    public PowerLine getRightPowerLine() {
        return this.rightPowerLine;
    }

    /**
     * The method returns the left or first Breaker object of the channel.
     * @return left breaker
     */
    public Breaker getLeftBreaker() {
        return this.leftBreaker;
    }
    
    /**
     * The method returns the right or second Breaker object of the channel.
     * @return right breaker
     */
    public Breaker getRightBreaker() {
        return this.rightBreaker;
    }
    
    /**
     * The method returns the total output level of the channel as a percentage
     * of nominal power.
     * @return output power level
     */
    public SimpleDoubleProperty getOutputPower() {
        return this.outputPower;
    }

    /**
     * The method returns the current setting of the balancer control of the 
     * channel, used to adjust the ratio of the right and left input fed into 
     * the output.
     * @return balancer control value
     */
    public SimpleIntegerProperty getBalancerValue() {
        return this.balancerValue;
    }

    /**
     * The method returns the current output balance of the channel, which is 
     * dependent on the balance between the outputs of the input channels and 
     * the setting of the balancer control. The output balance affects both heat 
     * generation and reactor line stability.
     * @return channel output balance
     */
    public SimpleDoubleProperty getOutputBalance() {
        return this.outputBalance;
    }
    
    /**
     * The method returns the balance gauge UI object.
     * @return UI balance gauge
     */
    public Gauge getBalanceGauge() {
        return this.balanceGauge;
    }

    /**
     * The method sets the channel's power output level, displayed in the 
     * output gauge and fed into the corresponding output.
     * @param outputPower channel power output as a property
     */ 
    public void setOutputPower(SimpleDoubleProperty outputPower) {
        this.outputPower = outputPower;
    }

    /**
     * The method sets the channel's balancer control value, which 
     * affects both the output balance and the output power level.
     * @param value value of the control as a property, from -64 to 63
     */ 
    public void setBalancerValue(SimpleIntegerProperty value) {
        this.balancerValue = value;
    }

    /**
     * The method sets the channel's output balance, which affects both heat 
     * generation and reactor line fluctuation.
     * @param outputBalance output balance as a property, with a value from 
     * -100 to 100
     */
    public void setOutputBalance(SimpleDoubleProperty outputBalance) {
        this.outputBalance = outputBalance;
    }
    
    /**
     * The method sets a reference to the balance gauge UI object associated 
     * with the channel.
     * @param balanceGauge the balance gauge object
     */ 
    public void setBalanceGauge(Gauge balanceGauge) {
        this.balanceGauge = balanceGauge;
    }

    /**
     * The method handles the core of the program logic for the power channel, 
     * calculating and updating the various class parameters and also triggering 
     * the recalculation of the main output stage.
     */
    public void updateOutput() {
        double leftInput;
        double rightInput;
        if (this.leftBreaker.getStatus().equals("broken") || this.leftBreaker.getStatus().equals("hot") || this.leftBreaker.getStatus().equals("resetting")) {
            leftInput = 0.0;
        } else {
            leftInput = this.leftPowerLine.getOutputPower().doubleValue();
        }
        if (this.rightBreaker.getStatus().equals("broken") || this.rightBreaker.getStatus().equals("hot") || this.rightBreaker.getStatus().equals("resetting")) {
            rightInput = 0.0;
        } else {
            rightInput = this.rightPowerLine.getOutputPower().doubleValue();
        }
        double balance = calculateChannelOutputBalance(leftInput, rightInput);
        this.outputBalance.set(balance);
        double channelOutput = calculateChannelPowerOutput(leftInput, rightInput);
        this.outputPower.set(channelOutput);        
        checkInstabilityAndFluctuation(balance);
        this.manager.calculateOutputValues();
        this.leftBreaker.calculateHeatDelta();
        this.rightBreaker.calculateHeatDelta();
    }
    
    /**
     * The method calculates the output balance of the channel, from -100 to 
     * 100, 0 being centered, based on the inputs and the position of the 
     * balance controller.
     * @param leftInput the power level of the left input line
     * @param rightInput the power level of the right input line
     */
    private double calculateChannelOutputBalance(double leftInput, double rightInput) {
        double balance;
        if (leftInput == 0 && rightInput == 0) {
            balance = (this.balancerValue.intValue() / 2) / 64.0 * 100.0;
        } else if (leftInput <= rightInput) {         
            balance = Math.max(((int) Math.round(leftInput / rightInput * 64) - 64) + this.balancerValue.intValue(), -32) / 64.0 * 100.0;
        } else {
            balance = Math.min((63 - (int) Math.round(rightInput / leftInput * 63)) + this.balancerValue.intValue(), 32) / 63.0 * 100.0;
        }
        return balance;
    }
    
    /**
     * The method calculates the total power output of the channel based on the 
     * inputs and the position of the balance controller.
     * @param leftInput the power level of the left input line
     * @param rightInput the power level of the right input line
     */
    private double calculateChannelPowerOutput(double leftInput, double rightInput) {    
        double leftOutputPower;
        double rightOutputPower;
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
        return (leftOutputPower + rightOutputPower) / 2.0;
    }
        
    /**
     * The method checks the current balance of the channel against set limits
     * to determine whether it is unbalanced enough to cause instability in the 
     * reactor lines feeding the channel, affecting both the stability value 
     * of the reactor line, affecting the likelihood of its fluctuation, and in 
     * more extreme causes extra instability, triggering additional fluctuation 
     * events.
     * @param balance the current balance value of the channel, from -100 to 100
     */
    private void checkInstabilityAndFluctuation(double balance) {
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
    }
        
    /**
     * The method, called from the main program logic, lights the balance 
     * gauge warning led on, if the balance is too much off.
     */
    private void controllingBalanceGaugeLed() {
        if (getOutputBalance().doubleValue() > 25.0 || getOutputBalance().doubleValue() < -25.0) {
            this.balanceGauge.setLedOn(true);
        } else {
            this.balanceGauge.setLedOn(false);
        }
    }
}
