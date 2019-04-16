/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ville
 */
public class FluctuatorTest {
    
    public FluctuatorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
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
    public void FluctuatorCreationVolatilityTest() {
        Oscillator reactorLine = new Oscillator(100, 100, 0.0);
        Fluctuator reactorFluctuator = new Fluctuator(reactorLine, 10);
        assertEquals(10, reactorFluctuator.getVolatility());
    }
    
    @Test
    public void FluctuatorVolatilityChangeTest() {
        Oscillator reactorLine = new Oscillator(100, 100, 0.0);
        Fluctuator reactorFluctuator = new Fluctuator(reactorLine, 10);
        reactorFluctuator.setVolatility(20);
        assertEquals(20, reactorFluctuator.getVolatility());
    }
    
    @Test
    public void FluctuatorFluctuateOscillatorAmplitudeTest() {
        Oscillator reactorLine = new Oscillator(100, 100, 0.0);
        Fluctuator reactorFluctuator = new Fluctuator(reactorLine, 10);
        reactorFluctuator.fluctuateAmplitude(50);
        assertNotEquals(100, reactorLine.getCurrentAmplitude().intValue());
    }
    
    @Test
    public void FluctuatorFluctuateOscillatorAmplitudeCheckFrequencyTest() {
        Oscillator reactorLine = new Oscillator(100, 100, 0.0);
        Fluctuator reactorFluctuator = new Fluctuator(reactorLine, 10);
        reactorFluctuator.fluctuateAmplitude();
        assertEquals(100, reactorLine.getCurrentFrequency().intValue());
    }
    
    @Test
    public void FluctuatorFluctuateAllTest() {
        Oscillator reactorLine = new Oscillator(100, 100, 0.0);
        Fluctuator reactorFluctuator = new Fluctuator(reactorLine, 10);
        reactorFluctuator.fluctuateAll(50);
        assertNotEquals(200.0, reactorLine.getCurrentAmplitude().intValue() + reactorLine.getCurrentFrequency().intValue() + reactorLine.getCurrentPhase().doubleValue(),0.1);
    }
    
}
