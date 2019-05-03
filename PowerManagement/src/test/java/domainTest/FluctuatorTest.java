/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domainTest;

import domain.Fluctuator;
import domain.Oscillator;
import java.util.Random;
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
    private int lines;
    private domain.Manager manager;
    private domain.PowerLine line;
    private domain.PowerChannel channel;
    private Fluctuator reactorFluctuator;
    private Oscillator reactorLine;
    private Random randomGenerator;
    
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
        lines = 4;
        manager = new domain.Manager(lines);
        manager.createPowerLines(lines);
        manager.createPowerChannels(lines / 2);
        line = manager.getPowerLine(0);
        channel = manager.getPowerChannel(0);
        reactorLine = line.getReactorLine();
        reactorFluctuator = line.getInputFluctuator();
        randomGenerator = new Random(45138464);
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
        assertEquals(5, reactorFluctuator.getVolatility());
    }
    
    @Test
    public void FluctuatorVolatilityChangeTest() {
        reactorFluctuator.setVolatility(20);
        assertEquals(20, reactorFluctuator.getVolatility());
    }
    
    @Test
    public void FluctuatorFluctuateOscillatorFrequencyTest() {
        reactorFluctuator.fluctuateFrequency(100, new Random(45138464));
        assertEquals(98, reactorLine.getCurrentFrequency().intValue());
    }
    
    @Test
    public void FluctuatorFluctuateOscillatorAmplitudeTest() {
        reactorFluctuator.fluctuateAmplitude(50, new Random(341364));
        assertEquals(79, reactorLine.getCurrentAmplitude().intValue());
    }
    
    @Test
    public void FluctuatorFluctuateOscillatorPhaseTest() {
        reactorFluctuator.fluctuatePhase(70, new Random(478648));
        assertEquals(-0.659, reactorLine.getCurrentPhase().doubleValue(),0.001);
    }
    
    @Test
    public void FluctuatorFluctuateOscillatorAmplitudeCheckFrequencyTest() {
        reactorFluctuator.fluctuateAmplitude();
        assertEquals(100, reactorLine.getCurrentFrequency().intValue());
    }
    
    @Test
    public void FluctuatorFluctuateAllTest() {
        reactorFluctuator.fluctuateAll(90, new Random(4513344));
        assertEquals(182.759, reactorLine.getCurrentAmplitude().intValue() + reactorLine.getCurrentFrequency().intValue() + reactorLine.getCurrentPhase().doubleValue(),0.001);
    }
    
}
