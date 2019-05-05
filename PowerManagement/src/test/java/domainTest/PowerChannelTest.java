/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domainTest;

import de.saxsys.javafx.test.JfxRunner;
import domain.Breaker;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

/**
 *
 * @author Ville
 */
@RunWith(JfxRunner.class)
public class PowerChannelTest {
    private int lines;
    private domain.Manager manager;
    private domain.PowerChannel channel;
    private domain.PowerLine leftPowerLine;
    private domain.PowerLine rightPowerLine;
    private Breaker leftBreaker;
    private Breaker rightBreaker;
    private ui.StatusLed leftBreakerLight;
    private ui.StatusLed rightBreakerLight;
    private Button leftBreakerButton;
    private Button rightBreakerButton;
    private Gauge balanceGauge;
    
    
    public PowerChannelTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        lines = 4;
        manager = new domain.Manager(lines);
        manager.createPowerLines(lines);
        manager.createPowerChannels(lines / 2);
        manager.createMainOutputs(lines / 2);
        leftPowerLine = manager.getPowerLine(0);
        rightPowerLine = manager.getPowerLine(1);
        channel = manager.getPowerChannel(0);
        balanceGauge = GaugeBuilder.create().build();
        channel.setBalanceGauge(balanceGauge);
        leftBreaker = channel.getLeftBreaker();
        rightBreaker = channel.getRightBreaker();
        leftBreaker.setBreakerLight(leftBreakerLight);
        rightBreaker.setBreakerLight(rightBreakerLight);
        leftBreakerLight = new ui.StatusLed();
        rightBreakerLight = new ui.StatusLed();
        leftBreaker.setBreakerLight(leftBreakerLight);
        leftBreaker.setBreakerButton(leftBreakerButton);
        rightBreaker.setBreakerLight(rightBreakerLight);
        rightBreaker.setBreakerButton(rightBreakerButton);
        manager.createReactorService();
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void PowerChannelInitialBalanceTest() {
        channel.updateOutput();
        assertEquals(0.00, channel.getOutputBalance().doubleValue(),0.01);
    }
    
    @Test
    public void PowerChannelBalancerTest1() {
        channel.setBalancerValue(new SimpleIntegerProperty(32));
        channel.updateOutput();
        assertEquals(50.00, channel.getOutputBalance().doubleValue(),0.01);
    }
    
    @Test
    public void PowerChannelBalancerTest2() {
        channel.setBalancerValue(new SimpleIntegerProperty(-16));
        channel.updateOutput();
        assertEquals(-25.00, channel.getOutputBalance().doubleValue(),0.01);
    }
    
    @Test
    public void PowerChannelBalancerOutputTest1() {
        channel.setBalancerValue(new SimpleIntegerProperty(32));
        channel.updateOutput();
        assertEquals(74.60, channel.getOutputPower().doubleValue(),0.01);
    }
    
    @Test
    public void PowerChannelBalancerOutputTest2() {
        channel.setBalancerValue(new SimpleIntegerProperty(-10));
        channel.updateOutput();
        assertEquals(92.18, channel.getOutputPower().doubleValue(),0.01);
    }
    
    @Test
    public void breakerBrokenBalanceTest() {
        channel.getLeftBreaker().setStatus("broken");
        channel.updateOutput();
        assertEquals(-50.00, channel.getOutputBalance().doubleValue(),0.01);
    }
    
    @Test
    public void breakerBrokenOutputTest() {
        channel.getRightBreaker().setStatus("broken");
        channel.updateOutput();
        assertEquals(50.00, channel.getOutputPower().doubleValue(),0.01);
    }
    
    @Test
    public void breakerBrokenAdjustedBalanceTest() {
        channel.getLeftBreaker().setStatus("broken");
        channel.setBalancerValue(new SimpleIntegerProperty(64));
        channel.updateOutput();
        assertEquals(0.00, channel.getOutputBalance().doubleValue(),0.01);
    }
    
}
