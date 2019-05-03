/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domainTest;

import de.saxsys.javafx.test.JfxRunner;
import java.util.concurrent.TimeoutException;
import javafx.scene.control.Button;
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
    private domain.PowerChannel channel;
    private domain.Breaker breaker;
    private ui.StatusLed breakerLight;
    private Button breakerButton;
    
    
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
        line = manager.getPowerLine(0);
        channel = manager.getPowerChannel(0);
        breaker = channel.getLeftBreaker();
        breakerButton = new Button();
        breakerButton.setDisable(true);
        breakerLight = new ui.StatusLed();
        breaker.setBreakerButton(breakerButton);
        breaker.setBreakerLight(breakerLight);
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
        manager.setMainOutputLevel(120);
        breaker.calculateHeatDelta();
        double delta = breaker.getBreakerDelta().doubleValue();   
        assertEquals(2.0, delta, 0.001);
    }
    
    @Test
    public void BreakerPositiveHeatTest() {
        manager.getPowerLine(0).setInputPower(120);
        manager.setMainOutputLevel(120);
        breaker.calculateHeatDelta();
        breaker.applyHeatDelta();
        double heat = breaker.getBreakerHeat().doubleValue();   
        assertEquals(2.0, heat, 0.001);
    }
    
    @Test
    public void BreakerNegativeDeltaTest() {
        manager.setMainOutputLevel(80);
        breaker.calculateHeatDelta();
        double delta = breaker.getBreakerDelta().doubleValue();   
        assertEquals(-4.0, delta, 0.001);
    }
    
    @Test
    public void SetBreakerStatusTest() {
        breaker.setStatus("hot");
        String lightStatus = breaker.getBreakerLight().getstatus();
        assertEquals("alert", lightStatus);
    }
    
    
    @Test
    public void BreakerCompoundHeatTest() {
        manager.getPowerLine(0).setInputPower(120);
        manager.setMainOutputLevel(120);
        breaker.calculateHeatDelta();
        breaker.applyHeatDelta();
        breaker.applyHeatDelta();
        breaker.applyHeatDelta();
        breaker.applyHeatDelta();
        manager.setMainOutputLevel(80);
        breaker.calculateHeatDelta();
        breaker.applyHeatDelta();
        breaker.applyHeatDelta();
        double heat = breaker.getBreakerHeat().doubleValue();   
        assertEquals(4.0, heat, 0.001);
    }
    
}
