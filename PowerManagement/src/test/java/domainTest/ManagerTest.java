/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domainTest;

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
public class ManagerTest {
    private int lines;
    private domain.Manager manager;
    private domain.PowerLine line1;
    private domain.PowerLine line2;
    private domain.PowerLine line3;
    private domain.PowerLine line4;
    private domain.PowerChannel channel1;
    private domain.PowerChannel channel2;
    
    public ManagerTest() {
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
        line1 = manager.getPowerLine(0);
        line2 = manager.getPowerLine(1);
        line3 = manager.getPowerLine(2);
        line4 = manager.getPowerLine(3);
        channel1 = manager.getPowerChannel(0);
        channel2 = manager.getPowerChannel(1);
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
    public void ManagerPowerLineCreationControlFrequencyTest() {
        int line0Freq = line1.getControlFrequency().intValue();
        int line1Freq = line2.getControlFrequency().intValue();
        int line2Freq = line3.getControlFrequency().intValue();
        int line3Freq = line4.getControlFrequency().intValue();
        assertEquals(400, line0Freq + line1Freq + line2Freq + line3Freq);
    }
    
    @Test
    public void ManagerPowerLineCreationInputAdjusterTest() {
        line1.getInputAdjuster().setCurrentFrequency(120);
        line2.getInputAdjuster().setCurrentFrequency(80);
        line3.getInputAdjuster().setCurrentFrequency(130);
        line4.getInputAdjuster().setCurrentFrequency(90);
        int line0Freq = line1.getControlFrequency().intValue();
        int line1Freq = line2.getControlFrequency().intValue();
        int line2Freq = line3.getControlFrequency().intValue();
        int line3Freq = line4.getControlFrequency().intValue();
        assertEquals(420, line0Freq + line1Freq + line2Freq + line3Freq);
    }
    
}
