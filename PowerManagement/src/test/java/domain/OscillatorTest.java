package domain;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
public class OscillatorTest {
    
    public OscillatorTest() {
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
    public void OscillatorCreationFrequencyTest() {
        Oscillator reactorLine = new Oscillator(100, 100, 0.0);
        assertEquals(100, reactorLine.getBaseFrequency());
    }
    
    @Test
    public void OscillatorCreationFrequencyChangeTest() {
        Oscillator reactorLine = new Oscillator(100, 100, 0.0);
        reactorLine.setCurrentFrequency(120);
        assertEquals(120, reactorLine.getCurrentFrequency().intValue());
    }
    
    @Test
    public void FluctuatorNoFluctuationTest() {
        Oscillator reactorLine = new Oscillator(100, 100, 0.0);
        Fluctuator inputFluctuator = new Fluctuator(reactorLine,0,10);
        inputFluctuator.fluctuateAmplitude();
        assertEquals(100, reactorLine.getCurrentAmplitude().intValue());
    }
    
    @Test
    public void FluctuatorForcedFluctuationTest() {
        Oscillator reactorLine = new Oscillator(100, 100, 0.0);
        Fluctuator inputFluctuator = new Fluctuator(reactorLine,0,10);
        inputFluctuator.fluctuateFrequency(100,30);
        assertNotEquals(100, reactorLine.getCurrentFrequency().intValue());
    }
}
