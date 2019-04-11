/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import eu.hansolo.fx.smoothcharts.SmoothedChart;
import java.util.logging.Logger;
import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Ville
 */
public class Oscilloscope extends StackPane {
    private static final Logger errorLogger = Logger.getLogger(Oscilloscope.class.getName());
    int column;
    domain.PowerLine powerLine;
    NumberAxis xAxis;
    NumberAxis yAxis;
    SmoothedChart<Number, Number> oscilloscopeChart;
    
    public Oscilloscope(int column, domain.PowerLine powerLine, int timeframe, int scale) {
        this.column = column;
        this.powerLine = powerLine;
        this.oscilloscopeChart = createChart(timeframe, scale);
        this.getChildren().add(this.oscilloscopeChart);
    }
    
    private SmoothedChart<Number, Number> createChart(int timeframe, int scale) {        
        this.xAxis = new NumberAxis(0 , timeframe, 10);
        this.yAxis = new NumberAxis(-1 * scale, scale, 20);
        SmoothedChart<Number, Number> oscilloscopeChart = new SmoothedChart<>(xAxis, yAxis);
        oscilloscopeChart.setTitle("Reactor Line " + (column + 1));
        oscilloscopeChart.setData(powerLine.getOutputData());
        
        return oscilloscopeChart;
    }
    
    public SmoothedChart<Number, Number> getChart() {
        return this.oscilloscopeChart;
    }
    
    public NumberAxis getXAxis() {
        return this.xAxis;
    }
    
    public NumberAxis getYAxis() {
        return this.yAxis;
    }
    
    
    
    
}
