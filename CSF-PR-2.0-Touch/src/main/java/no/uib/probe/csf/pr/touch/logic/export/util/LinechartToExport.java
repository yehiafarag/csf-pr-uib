/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.logic.export.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
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

        Font font = new Font("Helvetica, Arial", Font.PLAIN, 12);

        int y = 10;
        int x = 0;
        JLabel header = new JLabel("Overview Chart");
        header.setFont(font);
        header.setForeground(Color.GRAY);
        header.setSize(width, 37);
        header.setLocation(x, y);
        header.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        header.setOpaque(false);
        header.setVisible(true);
        this.add(header);
        x += 10;
        y += 40;

        ExportStudiesLineChart lineChart = new ExportStudiesLineChart(width, height);
        lineChart.updateData(selectedComparisonsList, proteinKey, custTrend);
        ChartPanel chartPanel = lineChart.getChartPanel();
        chartPanel.setLocation(x, y);
        this.add(chartPanel);

    }

}
