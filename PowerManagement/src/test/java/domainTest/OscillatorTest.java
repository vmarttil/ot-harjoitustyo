package domainTest;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
public class OscillatorTest {
    private int lines;
    private domain.Manager manager;
    private domain.PowerLine line;
    private domain.PowerChannel channel;
    private Fluctuator reactorFluctuator;
    private Oscillator reactorLine;
    private Random randomGenerator;
    
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
        lines = 4;
        manager = new domain.Manager(lines);
        manager.createPowerLines(lines);
        manager.createPowerChannels(lines / 2);
        line = manager.getPowerLine(0);
        channel = manager.getPowerChannel(0);
        reactorLine = line.getReactorLine();
        reactorFluctuator = line.getInputFluctuator();
        randomGenerator = new Random(524637);
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
        assertEquals(100, reactorLine.getBaseFrequency());
    }
    
    @Test
    public void OscillatorCreationFrequencyChangeTest() {
        reactorLine.setCurrentFrequency(120);
        assertEquals(120, reactorLine.getCurrentFrequency().intValue());
    }
    
    @Test
    public void ChangeCurrentCheckBaseAmplitude() {
        reactorLine.setCurrentAmplitude(130);
        assertEquals(100, reactorLine.getBaseAmplitude());
    }
    
    
    @Test
    public void OscillatorFluctuateOscillatorFrequencyTest() {
        reactorFluctuator.fluctuateFrequency(100, new Random(45134));
        assertEquals(127, reactorLine.getCurrentFrequency().intValue());
    }
    
    @Test
    public void OscillatorFluctuateOscillatorAmplitudeTest() {
        reactorFluctuator.fluctuateAmplitude(10, new Random(34344));
        assertEquals(105, reactorLine.getCurrentAmplitude().intValue());
    }
    
    @Test
    public void OscillatorFluctuateOscillatorPhaseTest() {
        reactorFluctuator.fluctuatePhase(40, new Random(387684));
        assertEquals(0.0314, reactorLine.getCurrentPhase().doubleValue(), 0.0001);
    }
    
    @Test
    public void FluctuateAndResetFrequencyTest() {
        reactorFluctuator.fluctuateFrequency(70, new Random(434468));
        reactorLine.setCurrentFrequency(100);
        assertEquals(100, reactorLine.getCurrentFrequency().intValue());
    }
    
}
