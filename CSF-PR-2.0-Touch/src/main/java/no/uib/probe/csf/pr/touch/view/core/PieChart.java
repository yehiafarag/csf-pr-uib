/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.itextpdf.text.pdf.codec.Base64;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Image;
import com.vaadin.ui.VerticalLayout;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import no.uib.probe.csf.pr.touch.view.components.datasetfilters.DatasetPieChartFilter;
import no.uib.probe.csf.pr.touch.view.components.datasetfilters.PieChartSlice;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

/**
 *
 * @author Yehia Farag
 *
 * this class represents interactive pie-chart the chart developed using JFree
 * chart and DiVA concept
 */
public class PieChart extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private final ChartRenderingInfo chartRenderingInfo = new ChartRenderingInfo();
    private final Image chartBackgroundImg;
    private final Map<Comparable, String> valuesMap = new HashMap<>();
//     private final Map<Comparable, PieChartSlice> chartData;
    private PiePlot plot;
    private JFreeChart chart;
    private final int width;
    private final int height;

    public PieChart(int filterWidth, int filterHeight, String title) {
        this.setWidth(filterWidth, Unit.PIXELS);
        this.setHeight(filterHeight, Unit.PERCENTAGE);

        this.width = filterWidth;
        this.height = filterHeight;
        this.chartBackgroundImg = new Image();
        this.addLayoutClickListener(PieChart.this);
        this.addStyleName("pointer");
        chartBackgroundImg.setWidth(100, Unit.PERCENTAGE);
        chartBackgroundImg.setHeight(100, Unit.PERCENTAGE);
        this.addComponent(chartBackgroundImg);
        this.initPieChart(title);
        this.redrawChart();
    }

    private void initPieChart(String title) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        plot = new PiePlot(dataset);
        plot.setNoDataMessage("No data available");
        plot.setCircular(true);
        plot.setLabelGap(0);
        plot.setLabelFont(new Font("Open Sans", Font.BOLD, 15));
        plot.setLabelGenerator(new PieSectionLabelGenerator() {

            @Override
            public String generateSectionLabel(PieDataset pd, Comparable cmprbl) {
                return valuesMap.get(cmprbl);
            }

            @Override
            public AttributedString generateAttributedSectionLabel(PieDataset pd, Comparable cmprbl) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        plot.setSimpleLabels(true);
        plot.setLabelBackgroundPaint(null);
        plot.setLabelShadowPaint(null);
        plot.setLabelPaint(Color.WHITE);
        plot.setLabelOutlinePaint(null);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setInteriorGap(0);
        plot.setShadowPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        plot.setBaseSectionOutlinePaint(Color.WHITE);
        plot.setSectionOutlinesVisible(true);
        plot.setBaseSectionOutlineStroke(new BasicStroke(1.2f));
        plot.setInteriorGap(0.05);
        plot.setIgnoreZeroValues(true);

        chart = new JFreeChart(plot);
        chart.setTitle(new TextTitle(title, new Font("Open Sans", Font.BOLD, 13)));
        chart.setBorderPaint(null);
        chart.setBackgroundPaint(null);
        chart.getLegend().setFrame(BlockBorder.NONE);
        chart.getLegend().setItemFont(new Font("Open Sans", Font.PLAIN, 12));

    }

    private String saveToFile(final JFreeChart chart, double width, double height) {
        byte imageData[];
        try {
            width = Math.max(width, 250);
            height = Math.max(height, 250);

            imageData = ChartUtilities.encodeAsPNG(chart.createBufferedImage((int) width, (int) height, chartRenderingInfo));
            String base64 = Base64.encodeBytes(imageData);
            base64 = "data:image/png;base64," + base64;
            return base64;
        } catch (IOException e) {
            System.err.println("at error " + e.getMessage());
        }
        return "";
    }

    /**
     * this method is responsible for initializing chart dataset
     *
     * @param chartData information required to update chart data
     */
    public void initializeFilterData(Map<Comparable, PieChartSlice> chartData) {
        
        
    }
    


    /**
     *
     */
    private void redrawChart() {
        String imgUrl = saveToFile(chart, width, height);
        this.chartBackgroundImg.setSource(new ExternalResource(imgUrl));
    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
