/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domainTest;

import de.saxsys.javafx.test.JfxRunner;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import java.util.concurrent.TimeoutException;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
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
public class BreakerTest {
    private int lines;
    private domain.Manager manager;
    private domain.PowerLine line;
    private Gauge lineOutputGauge;
    private ui.StatusLed statusLed;
    private ToggleButton shutdownButton;
    private ToggleButton offlineButton;
    private domain.PowerChannel channel;
    private Gauge balanceGauge;
    private domain.Breaker breaker;
    private ui.StatusLed breakerLight;
    private Button breakerButton;
    private Slider outputAdjuster;
    
    
    public BreakerTest() {
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
        line = manager.getPowerLine(0);
        statusLed = new ui.StatusLed();
        shutdownButton = new ToggleButton();
        offlineButton = new ToggleButton();
        lineOutputGauge = GaugeBuilder.create().build();
        channel = manager.getPowerChannel(0);
        balanceGauge = GaugeBuilder.create().build();
        breaker = channel.getLeftBreaker();
        breakerButton = new Button();
        breakerButton.setDisable(true);
        breakerLight = new ui.StatusLed();
        breaker.setBreakerButton(breakerButton);
        breaker.setBreakerLight(breakerLight);
        outputAdjuster = new Slider();
        for (int i = 0; i < lines; i++) {
            manager.getPowerLine(i).setStatusLed(statusLed);
            manager.getPowerLine(i).setShutdownButton(shutdownButton);
            manager.getPowerLine(i).setOfflineButton(offlineButton);
            manager.getPowerLine(i).setLineOutputGauge(lineOutputGauge);
        }
        for (int i = 0; i < lines / 2; i++) {
            manager.getPowerChannel(i).setBalanceGauge(balanceGauge);
        }
    }
    
    @After
    public void tearDown() throws TimeoutException {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void BreakerStartingDeltaTest() {
        double delta = breaker.getBreakerDelta().doubleValue();   
        assertEquals(0.0, delta, 0.001);
    }
    
    @Test
    public void BreakerStartingHeatTest() {
        double heat = breaker.getBreakerHeat().doubleValue();   
        assertEquals(0.0, heat, 0.001);
    }
    
    @Test
    public void BreakerPositiveDeltaTest() {
        line.setInputPower(120);
        manager.setOutputAdjusterValue(0, 120);
        breaker.calculateHeatDelta();
        double delta = breaker.getBreakerDelta().doubleValue();   
        assertEquals(4.0, delta, 0.001);
    }
    
    @Test
    public void BreakerPositiveHeatTest() {
        manager.getPowerLine(0).setInputPower(120);
        manager.setOutputAdjusterValue(0, 120);
        manager.setOutputAdjusterValue(1, 120);
        breaker.calculateHeatDelta();
        breaker.applyHeatDelta();
        double heat = breaker.getBreakerHeat().doubleValue();   
        assertEquals(4.0, heat, 0.001);
    }
    
    @Test
    public void BreakerNegativeDeltaTest() {
        manager.setOutputAdjusterValue(0, 80);
        manager.setOutputAdjusterValue(1, 80);
        breaker.calculateHeatDelta();
        double delta = breaker.getBreakerDelta().doubleValue();   
        assertEquals(-4.0, delta, 0.001);
    }
    
    @Test
    public void SetBreakerStatusHotTest() {
        breaker.setStatus("hot");
        String lightStatus = breaker.getBreakerLight().getstatus();
        assertEquals("alert", lightStatus);
    }
    
    @Test
    public void SetBreakerStatusBrokenTest() {
        breaker.setStatus("broken");
        String lightStatus = breaker.getBreakerLight().getstatus();
        assertEquals("off", lightStatus);
    }
    
    @Test
    public void SetBreakerStatusResettingBlinkTest() {
        breaker.setStatus("resetting");
        boolean slowBlinkStatus = breaker.getBreakerLight().isSlowBlink();
        assertEquals(true, slowBlinkStatus);
    }
    
    @Test
    public void BreakerCompoundHeatTest() {
        manager.getPowerLine(0).setInputPower(120);
        manager.setOutputAdjusterValue(0, 120);
        breaker.calculateHeatDelta();
        breaker.applyHeatDelta();
        breaker.applyHeatDelta();
        breaker.applyHeatDelta();
        breaker.applyHeatDelta();
        manager.setOutputAdjusterValue(0, 80);
        breaker.calculateHeatDelta();
        breaker.applyHeatDelta();
        breaker.applyHeatDelta();
        double heat = breaker.getBreakerHeat().doubleValue();   
        assertEquals(16.0, heat, 0.001);
    }

    @Test
    public void BreakerOverheatTest() {
        manager.getPowerLine(0).setInputPower(120);
        manager.setOutputAdjusterValue(0, 120);
        breaker.calculateHeatDelta();
        while (breaker.getBreakerHeat().doubleValue() <= breaker.getBreakThreshold()) {
            breaker.applyHeatDelta();
        }
        breaker.applyHeatDelta();
        assertEquals("hot", breaker.getStatus());
    }
    
    @Test
    public void BreakerHeatWarningTest() {
        manager.getPowerLine(0).setInputPower(120);
        manager.setOutputAdjusterValue(0, 120);
        breaker.calculateHeatDelta();
        while (breaker.getBreakerHeat().doubleValue() <= 0.8 * breaker.getBreakThreshold()) {
            breaker.applyHeatDelta();
        }
        breaker.applyHeatDelta();
        assertEquals("warning", breaker.getStatus());
    }

    @Test
    public void BreakerRecoveryTest() {
        breaker.getBreakerButton().setDisable(true);
        manager.getPowerLine(0).setInputPower(120);
        manager.setOutputAdjusterValue(0, 120);
        breaker.calculateHeatDelta();
        while (breaker.getBreakerHeat().doubleValue() <= 0.5 * breaker.getBreakThreshold()) {
            breaker.applyHeatDelta();
        }
        breaker.setStatus("hot");
        breaker.applyHeatDelta();
        assertEquals(false, breaker.getBreakerButton().isDisabled());
    }
}
