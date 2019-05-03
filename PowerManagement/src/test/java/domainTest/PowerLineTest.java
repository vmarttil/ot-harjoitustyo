package domainTest;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import de.saxsys.javafx.test.JfxRunner;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import java.util.Random;
import java.util.concurrent.TimeoutException;
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
public class PowerLineTest {
    private int lines;
    private domain.Manager manager;
    private domain.PowerLine line;
    private domain.PowerChannel channel;
    private ui.StatusLed statusLed;
    private ToggleButton shutdownButton;
    private ToggleButton offlineButton;
    private Gauge lineOutputGauge;
    private Gauge balanceGauge;
    
    
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
        lines = 4;
        manager = new domain.Manager(lines);
        manager.createPowerLines(lines);
        manager.createPowerChannels(lines / 2);
        line = manager.getPowerLine(0);
        statusLed = new ui.StatusLed();
        shutdownButton = new ToggleButton();
        offlineButton = new ToggleButton();
        lineOutputGauge = GaugeBuilder.create().build();
        line.setStatusLed(statusLed);
        line.setShutdownButton(shutdownButton);
        line.setOfflineButton(offlineButton);
        line.setLineOutputGauge(lineOutputGauge);
        channel = manager.getPowerChannel(0);
        balanceGauge = GaugeBuilder.create().build();
        channel.setBalanceGauge(balanceGauge);
        
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
    public void PowerLineInitialStabilityTest() {
        assertEquals(50, line.getStability());
    }
    
    @Test
    public void PowerLineInitialFrequencyTest() {
        int frequency = line.getReactorFrequency().intValue();
        assertEquals(100, frequency);
    }
    
    @Test
    public void PowerLineInitialAmplitudeTest() {
        int amplitude = line.getReactorAmplitude().intValue();
        assertEquals(100, amplitude);
    }
    
    @Test
    public void PowerLineInitialPhaseTest() {
        double phase = line.getReactorPhase().doubleValue();
        assertEquals(0.0, phase,0.001);
    }
    
    @Test
    public void OfflinePowerOutputTest() {
        line.setOffline();
        assertEquals(0.0, line.getOutputPower().doubleValue(),0.1);
    }
    
    @Test
    public void OffOnlinePowerOutputTest() {
        line.setOffline();
        line.setOnline();
        assertEquals(100.0, line.getOutputPower().doubleValue(),0.1);
    }
    
    @Test
    public void OfflineChannelOutputTest() {
        line.setOffline();
        assertEquals(50.0, channel.getOutputPower().doubleValue(),0.1);
    }
    
    @Test
    public void UnstableLightTest() {
        line.setUnstable();
        assertEquals("alert", statusLed.getstatus());
    }
    
    @Test
    public void FluctuateLineTest() {
        line.getInputFluctuator().fluctuateFrequency(10, new Random(245246));
        line.getInputFluctuator().fluctuateAmplitude(10, new Random(21346));
        line.getInputFluctuator().fluctuatePhase(10, new Random(871451));
        line.updateOscilloscopeData();
        assertEquals(83.6, line.getOutputPower().doubleValue(),0.1);
    }
    
    @Test
    public void ResetLineTest() {
        line.getInputFluctuator().fluctuateFrequency(10, new Random(242876));
        line.getInputFluctuator().fluctuateAmplitude(10, new Random(68875));
        line.getInputFluctuator().fluctuatePhase(10, new Random(82342));
        line.updateOscilloscopeData();
        line.resetLine();
        assertEquals(100.0, line.getOutputPower().doubleValue(),0.1);
    }
    
    
    
    
    
    
    
}
