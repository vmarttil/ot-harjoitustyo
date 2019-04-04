/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import eu.hansolo.fx.smoothcharts.SmoothedChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Ville
 */
public class Oscilloscope extends StackPane {
    int column;
    domain.PowerLine powerLine;
    // XYChart.Series<Number,Number> dataSeries;
    SmoothedChart<Number, Number> oscilloscopeChart;
    
    public Oscilloscope(int column, domain.PowerLine powerLine) {
        this.column = column;
        this.powerLine = powerLine;
        // this.dataSeries.dataProperty().bind(powerLine.getOutputData().dataProperty());
        this.oscilloscopeChart = createChart();
        this.getChildren().add(this.oscilloscopeChart);
    }
    
    private SmoothedChart<Number, Number> createChart() {        
        NumberAxis xAxis = new NumberAxis(0,200,10);
        NumberAxis yAxis = new NumberAxis(-200,200,20);
        SmoothedChart<Number, Number> oscilloscopeChart = new SmoothedChart<>(xAxis, yAxis);
        oscilloscopeChart.setTitle("Reactor Line " + (column+1));
        oscilloscopeChart.setCreateSymbols(false);
        oscilloscopeChart.setData(powerLine.getOutputData());
        return oscilloscopeChart;
    }
    
    public void updateChart() {
        
    }
}
