/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Timestamp;
import java.util.*;

/**
 *
 * @author Ville
 */
public interface LogDao<O,A> {
    
    double logMainOutputLevel();
    double logAdjustedOutputLevel(int number);
    int logOutputAdjusterValue(int number);
    
    O logMainOutputSection();
    
    double logChannelOutputLevel(int channel);
    double logChannelOutputBalance(int channel);
    int logChannelBalancerValue(int channel);
    String logChannelBreakerStatus(int channel, int number);
    double logChannelBreakerTemperature(int channel, int number);
    
    O logChannel(int number);
    A logChannelSection();
   
    double logLineOutputLevel(int line);
    String logLineStatus(int line);
    boolean logLineOnlineState(int line);
    int logLineInputAdjusterFrequency(int line);
    int logLineInputAdjusterAmplitude(int line);
    double logLineInputAdjusterPhase(int line);
    int logReactorLineFrequency(int line);
    int logReactorLineAmplitude(int line);
    double logReactorLinePhase(int line);
    double logReactorLineInputLevel(int line);
    
    O logLine(int line);
    A logLineSection();
    
    void addLogEntry();
    
    void exportLogContents() throws Exception;
    
}
