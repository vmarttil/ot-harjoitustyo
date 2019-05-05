/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException; 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Ville
 */
public class JSONLogDao implements LogDao<JSONObject, JSONArray> {
    public JSONArray logList;
    private domain.Manager manager;
    private int lines;
    
    public JSONLogDao(domain.Manager manager) {
        this.logList = new JSONArray();
        this.manager = manager;
        this.lines = manager.getLines();
    }

    @Override
    public double logMainOutputLevel() {
        return manager.getMainOutputLevel().doubleValue();
    }
    
    @Override
    public double logAdjustedOutputLevel(int number) {
        return manager.getOutputValue(number).doubleValue();
    }
    
    @Override
    public int logOutputAdjusterValue(int number) {
        return manager.getPowerAdjusterValue(number).intValue();
    }
    
    @Override
    public JSONObject logMainOutputSection() {
        JSONObject mainSection = new JSONObject();
        mainSection.put("mainOutputLevel", logMainOutputLevel());
        for (int i = 0; i < lines / 2; i++) {
            mainSection.put("adjustedOutputLevel" + i, logAdjustedOutputLevel(i));
        }
        for (int i = 0; i < lines / 2; i++) {
            mainSection.put("outputAdjusterValue" + i, logOutputAdjusterValue(i));
        }
        return mainSection;
    }
    
    @Override
    public double logChannelOutputLevel(int channel) {
        return manager.getPowerChannel(channel).getOutputPower().doubleValue();
    }
    
    @Override
    public double logChannelOutputBalance(int channel) {
        return manager.getPowerChannel(channel).getOutputBalance().doubleValue();
    }
    
    @Override
    public int logChannelBalancerValue(int channel) {
        return manager.getPowerChannel(channel).getBalancerValue().intValue();
    }
    
    @Override
    public String logChannelBreakerStatus(int channel, int number) {
        if (number % 2 == 0) {
            return manager.getPowerChannel(channel).getLeftBreaker().getStatus();
        } else {
            return manager.getPowerChannel(channel).getRightBreaker().getStatus();
        }
    }
    
    @Override
    public double logChannelBreakerTemperature(int channel, int number) {
        if (number % 2 == 0) {
            return manager.getPowerChannel(channel).getLeftBreaker().getBreakerHeat().doubleValue();
        } else {
            return manager.getPowerChannel(channel).getRightBreaker().getBreakerHeat().doubleValue();
        }
    }
    
    @Override
    public JSONObject logChannel(int channel) {
        JSONObject channelObject = new JSONObject();
        channelObject.put("channelOutputLevel", logChannelOutputLevel(channel));
        channelObject.put("channelOutputBalance", logChannelOutputBalance(channel));
        channelObject.put("channelBalancerValue", logChannelBalancerValue(channel));
        for (int i = 0; i < lines / 2; i++) {
            channelObject.put("channelBreaker" + i + "Status" , logChannelBreakerStatus(channel, i));
        }
        for (int i = 0; i < lines / 2; i++) {
            channelObject.put("channelBreaker" + i + "Temperature", logChannelBreakerTemperature(channel, i));
        }
        return channelObject;
    }
    
    @Override
    public JSONArray logChannelSection() {
        JSONArray channelSectionObject = new JSONArray();
        for (int i = 0; i < lines / 2; i++) {
            channelSectionObject.add(logChannel(i));
        }
        return channelSectionObject;
    }
    
    @Override
    public double logLineOutputLevel(int line) {
        return manager.getPowerLine(line).getOutputPower().doubleValue();
    }
    
    @Override
    public String logLineStatus(int line) {
        return manager.getPowerLine(line).getStatusLed().getstatus();
    }
    
    @Override
    public boolean logLineOnlineState(int line) {
        return manager.getPowerLine(line).isOnline();
    }
    
    @Override
    public int logLineInputAdjusterFrequency(int line) {
        return manager.getPowerLine(line).getControlFrequency().intValue();
    }
    
    @Override
    public int logLineInputAdjusterAmplitude(int line) {
        return manager.getPowerLine(line).getControlAmplitude().intValue();
    }
    
    @Override
    public double logLineInputAdjusterPhase(int line) {
        return manager.getPowerLine(line).getControlPhase().doubleValue();
    }
    
    @Override
    public int logReactorLineFrequency(int line) {
        return manager.getPowerLine(line).getReactorFrequency().intValue();
    }
    
    @Override
    public int logReactorLineAmplitude(int line) {
        return manager.getPowerLine(line).getReactorAmplitude().intValue();
    }
    
    @Override
    public double logReactorLinePhase(int line) {
        return manager.getPowerLine(line).getReactorPhase().doubleValue();
    }
    
    @Override
    public double logReactorLineInputLevel(int line) {
        return manager.getPowerLine(line).getInputPower();
    }
    
    @Override
    public JSONObject logLine(int line) {
        JSONObject lineObject = new JSONObject();
        lineObject.put("lineOutputLevel", logLineOutputLevel(line));
        lineObject.put("lineStatus", logLineStatus(line));
        lineObject.put("lineOnlineState", logLineOnlineState(line));
        lineObject.put("lineInputAdjusterFrequency", logLineInputAdjusterFrequency(line));
        lineObject.put("lineInputAdjusterAmplitude", logLineInputAdjusterAmplitude(line));
        lineObject.put("lineInputAdjusterPhase", logLineInputAdjusterPhase(line));
        lineObject.put("reactorLineFrequency", logReactorLineFrequency(line));
        lineObject.put("reactorLineAmplitude", logReactorLineAmplitude(line));
        lineObject.put("reactorLinePhase", logReactorLinePhase(line));
        lineObject.put("reactorLineInputLevel", logReactorLineInputLevel(line));
        return lineObject;
    }
    
    @Override
    public JSONArray logLineSection() {
        JSONArray lineSectionObject = new JSONArray();
        for (int i = 0; i < lines; i++) {
            lineSectionObject.add(logLine(i));
        }
        return lineSectionObject;
    }
    
    @Override
    public void addLogEntry() {
        JSONObject logEntry = new JSONObject();
        logEntry.put("timestamp", System.currentTimeMillis());
        logEntry.put("mainOutput", logMainOutputSection());
        logEntry.put("channels", logChannelSection());
        logEntry.put("lines", logLineSection());
        this.logList.add(logEntry);
        if (logList.size() >= 100) {
            exportLogContents();
            logList.clear();
        }
    }
    
    @Override
    public void exportLogContents() {
        File file = new File("PowerManagementLog.json");
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(logList.toJSONString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }       
}