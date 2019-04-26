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
    domain.Manager powerManager;
    int lines;
    
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
        powerManager = new domain.Manager(lines);
        for (int i = 0; i < lines; i++) {
            domain.PowerLine line = new domain.PowerLine(powerManager, i);
            powerManager.getPowerLines()[i] = line;
        }
        int line0Freq = powerManager.getPowerLine(0).getControlFrequency().intValue();
        int line1Freq = powerManager.getPowerLine(1).getControlFrequency().intValue();
        int line2Freq = powerManager.getPowerLine(2).getControlFrequency().intValue();
        int line3Freq = powerManager.getPowerLine(3).getControlFrequency().intValue();
        assertEquals(400, line0Freq + line1Freq + line2Freq + line3Freq);
    }
    
    @Test
    public void MAnagerPowerLineCreationInputAdjusterTest() {
        powerManager = new domain.Manager(lines);
        for (int i = 0; i < 4; i++) {
            domain.PowerLine line = new domain.PowerLine(powerManager, i);
            powerManager.getPowerLines()[i] = line;
        }
        powerManager.getPowerLine(0).getInputAdjuster().setCurrentFrequency(120);
        powerManager.getPowerLine(1).getInputAdjuster().setCurrentFrequency(80);
        powerManager.getPowerLine(2).getInputAdjuster().setCurrentFrequency(130);
        powerManager.getPowerLine(3).getInputAdjuster().setCurrentFrequency(90);
        int line0Freq = powerManager.getPowerLine(0).getControlFrequency().intValue();
        int line1Freq = powerManager.getPowerLine(1).getControlFrequency().intValue();
        int line2Freq = powerManager.getPowerLine(2).getControlFrequency().intValue();
        int line3Freq = powerManager.getPowerLine(3).getControlFrequency().intValue();
        assertEquals(420, line0Freq + line1Freq + line2Freq + line3Freq);
    }
    
}
