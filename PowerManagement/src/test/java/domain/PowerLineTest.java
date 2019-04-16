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
public class PowerLineTest {
    domain.Manager powerManager;
    
    
    public PowerLineTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        powerManager = new domain.Manager();
        //powerManager.startReactorService(10);
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
    public void PowerLineCreationStabilityTest() {
        domain.PowerLine line = new domain.PowerLine(powerManager, 1);
        assertEquals(50, line.getStability());
    }
    
    @Test
    public void PowerLineCreationFrequencyTest() {
        domain.PowerLine line = new domain.PowerLine(powerManager, 1);
        int frequency = line.getReactorFrequency().intValue();
        assertEquals(100, frequency);
    }
    
    @Test
    public void PowerLineCreationAmplitudeTest() {
        domain.PowerLine line = new domain.PowerLine(powerManager, 1);
        int amplitude = line.getReactorAmplitude().intValue();
        assertEquals(100, amplitude);
    }
    
    @Test
    public void PowerLineCreationPhaseTest() {
        domain.PowerLine line = new domain.PowerLine(powerManager, 1);
        double phase = line.getReactorPhase().doubleValue();
        assertEquals(0.0, phase,0.01);
    }
    
    @Test
    public void OffLinePowerOutputTest() {
        domain.PowerLine line = new domain.PowerLine(powerManager, 1);
        line.setOutputPower(0);
        assertEquals(0.0, line.getOutputPower().doubleValue(),0.01);
    }
    
}
