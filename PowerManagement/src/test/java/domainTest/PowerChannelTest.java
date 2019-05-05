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
    private domain.PowerLine line;
    private ui.StatusLed statusLed;
    private ToggleButton offlineButton;
    private ToggleButton shutdownButton;
    private Gauge lineOutputGauge;
    
    
    
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
        for (int i = 0; i < lines; i++) {
            manager.getPowerLine(i).setStatusLed(new ui.StatusLed());
        }
        for (int i = 0; i < lines; i++) {
            manager.getPowerLine(i).setOfflineButton(new ToggleButton());
        }
        for (int i = 0; i < lines; i++) {
            manager.getPowerLine(i).setShutdownButton(new ToggleButton());
        }
        for (int i = 0; i < lines; i++) {
            manager.getPowerLine(i).setLineOutputGauge(GaugeBuilder.create().build());
        }
        line = manager.getPowerLine(0);
        statusLed = manager.getPowerLine(0).getStatusLed();
        shutdownButton = manager.getPowerLine(0).getShutdownButton();
        offlineButton = manager.getPowerLine(0).getOfflineButton();
        lineOutputGauge = manager.getPowerLine(0).getLineOutputGauge();
        for (int i = 0; i < lines / 2; i++) {
            manager.getPowerChannel(i).setBalanceGauge(GaugeBuilder.create().build());
        }
        for (int i = 0; i < lines / 2; i++) {
            manager.getPowerChannel(i).getLeftBreaker().setBreakerLight(new ui.StatusLed());
            manager.getPowerChannel(i).getRightBreaker().setBreakerLight(new ui.StatusLed());
            manager.getPowerChannel(i).getLeftBreaker().setBreakerButton(new Button());
            manager.getPowerChannel(i).getRightBreaker().setBreakerButton(new Button());
        }
        channel = manager.getPowerChannel(0);
        balanceGauge = manager.getPowerChannel(0).getBalanceGauge();
        leftBreakerLight = manager.getPowerChannel(0).getLeftBreaker().getBreakerLight();
        rightBreakerLight = manager.getPowerChannel(0).getRightBreaker().getBreakerLight();
        leftBreakerButton = manager.getPowerChannel(0).getLeftBreaker().getBreakerButton();
        rightBreakerButton = manager.getPowerChannel(0).getRightBreaker().getBreakerButton();
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
