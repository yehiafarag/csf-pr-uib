/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.logic.export.util;

import java.awt.Color;
import java.awt.Font;
import java.util.Set;
import javax.swing.JLabel;
import javax.swing.JPanel;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import org.jfree.chart.ChartPanel;

/**
 *
 * @author Yehia Farag
 */
public class LinechartToExport extends JPanel {

    public LinechartToExport(int width, int height, Set<QuantDiseaseGroupsComparison> selectedComparisonsList, String proteinKey, int custTrend) {
        this.setLayout(null);
        this.setBackground(Color.WHITE);
        this.setVisible(true);
        this.setSize(width, height); 

        int y = 10;
        int x = 0;    

        ExportStudiesLineChart lineChart = new ExportStudiesLineChart(width, height);
        lineChart.updateData(selectedComparisonsList, proteinKey, custTrend);
        ChartPanel chartPanel = lineChart.getChartPanel();
        chartPanel.setLocation(x, y);
        this.add(chartPanel);

    }

}
