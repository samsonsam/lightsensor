package de.htw.samuelerb.graph;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.UnknownKeyException;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.util.HashMap;

/**
 * Created by samuelerb on 12.01.19.
 * Matr_nr: s0556350
 * Package: graph
 */
public class Graph extends ApplicationFrame {

    public XYSeriesCollection data;

    /**
     * A demonstration application showing an XY series containing a null value.
     *
     * @param title the frame title.
     */
    public Graph(String title) {
        super(title);
        this.data = new XYSeriesCollection();
        final JFreeChart chart = ChartFactory.createXYLineChart(
                "",
                "Lux",
                "LDR - Nanosekunden",
                data,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }

    public void addSeries(String tableName, HashMap<Integer, Integer> map) {
        XYSeries series = new XYSeries(tableName);
        map.forEach((x, y) -> {
            if (y < 5000_000) {
                series.add(x, y);
            }
        });
        this.data.addSeries(series);
    }

    public void addEntry(String tableName, Integer key, Integer value) {
        XYSeries series = null;
        try {
            series = this.data.getSeries(tableName);
        } catch (UnknownKeyException uke) {
            series = new XYSeries(tableName);
            this.data.addSeries(series);
        }
        series.add(key, value);
    }
}
