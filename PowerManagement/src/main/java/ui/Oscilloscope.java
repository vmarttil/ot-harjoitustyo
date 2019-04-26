/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import eu.hansolo.fx.smoothcharts.SmoothedChart;
import javafx.geometry.Insets;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Ville
 */
public class Oscilloscope extends StackPane {
    private int column;
    private domain.PowerLine powerLine;
    private NumberAxis xAxis;
    private NumberAxis yAxis;
    private SmoothedChart<Number, Number> oscilloscopeChart;
    
    public Oscilloscope(int column, domain.PowerLine powerLine, int timeframe, int scale) {
        this.column = column;
        this.powerLine = powerLine;
        this.oscilloscopeChart = createChart(timeframe, scale);
        this.getChildren().add(this.oscilloscopeChart);
    }
    
    private SmoothedChart<Number, Number> createChart(int timeframe, int scale) {        
        this.xAxis = new NumberAxis(0 , timeframe, 10);
        this.yAxis = new NumberAxis(-1 * scale, scale, 20);
        SmoothedChart<Number, Number> oscilloscopeChart = new SmoothedChart<>(this.xAxis, this.yAxis);
        oscilloscopeChart.setTitle("Reactor Line " + (column + 1));
        oscilloscopeChart.setData(powerLine.getOutputData());
        oscilloscopeChart.setPadding(new Insets(-10, 0, -20, 0));
        return oscilloscopeChart;
    }
    
    // Getters
    
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
