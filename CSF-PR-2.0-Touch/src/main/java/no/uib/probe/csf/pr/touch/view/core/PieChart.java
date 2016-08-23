/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.itextpdf.text.pdf.codec.Base64;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.text.AttributedString;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.PieSectionEntity;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.plot.PieLabelLinkStyle;
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
public abstract class PieChart extends AbsoluteLayout implements LayoutEvents.LayoutClickListener {

    private final ChartRenderingInfo chartRenderingInfo = new ChartRenderingInfo();
    private final Image chartBackgroundImg;
    private final Map<Comparable, String> valuesMap = new HashMap<>();
    private final VerticalLayout middleDountLayout;
    private PiePlot plot;
    private JFreeChart chart;
    private final int width;
    private final int height;
    private DefaultPieDataset dataset;
    private final Label selectAllLabel;
    private final Set<String> selectionSet;
    private final Color selectedColor = Color.decode("#197de1");
    private final Map<Comparable, Color> defaultColors;
    private final Map<Comparable, Color> selectedColors;

    public PieChart(int filterWidth, int filterHeight, String title, boolean interactive) {

        this.setWidth(filterWidth, Unit.PIXELS);
        this.setHeight(filterHeight, Unit.PIXELS);

        this.width = filterWidth;
        this.height = filterHeight;

        this.defaultColors = new HashMap<>();
        this.selectedColors = new HashMap<>();

        this.chartBackgroundImg = new Image();
        if (interactive) {
            this.addLayoutClickListener(PieChart.this);
        }
        this.addStyleName("pointer");
        chartBackgroundImg.setWidth(100, Unit.PERCENTAGE);
        chartBackgroundImg.setHeight(100, Unit.PERCENTAGE);
        this.addComponent(chartBackgroundImg);

        middleDountLayout = new VerticalLayout();
        middleDountLayout.setWidth(100, Unit.PIXELS);
        middleDountLayout.setHeight(100, Unit.PIXELS);
        middleDountLayout.setStyleName("middledountchart");

        selectAllLabel = new Label();
        selectAllLabel.setWidth(100, Unit.PERCENTAGE);
        selectAllLabel.setStyleName(ValoTheme.LABEL_TINY);
        selectAllLabel.addStyleName(ValoTheme.LABEL_SMALL);
        middleDountLayout.addComponent(selectAllLabel);
        middleDountLayout.setComponentAlignment(selectAllLabel, Alignment.MIDDLE_CENTER);

        this.initPieChart(title);
        selectionSet = new LinkedHashSet<>();

    }

    public VerticalLayout getMiddleDountLayout() {
        return middleDountLayout;
    }

    private void initPieChart(String title) {
        dataset = new DefaultPieDataset();

        plot = new PiePlot(dataset);
        plot.setNoDataMessage("No data available");
        plot.setCircular(true);
        plot.setLabelGap(0);
        plot.setLabelFont(new Font("Helvetica Neue", Font.PLAIN, 13));
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

        plot.setSimpleLabels(false);
        plot.setLabelBackgroundPaint(null);
        plot.setLabelShadowPaint(null);
        plot.setLabelPaint(Color.GRAY);
        plot.setLabelOutlinePaint(null);
        plot.setLabelLinksVisible(true);
        plot.setLabelLinkStyle(PieLabelLinkStyle.QUAD_CURVE);
        plot.setLabelLinkMargin(0);

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
        chart.setTitle(new TextTitle(title, new Font("Helvetica Neue", Font.PLAIN, 13)));
        chart.setBorderPaint(null);
        chart.setBackgroundPaint(null);
        chart.getLegend().setVisible(false);
        chart.getLegend().setFrame(BlockBorder.NONE);

        chart.getLegend().setItemFont(new Font("Helvetica Neue", Font.PLAIN, 12));

    }

    private String saveToFile(final JFreeChart chart, double width, double height) {
        byte imageData[];
        try {
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
    public void initializeFilterData(String[] items, Integer[] values, Color[] colors) {
        this.defaultColors.clear();
        this.selectedColors.clear();
        dataset.clear();
        for (int x = 0; x < items.length; x++) {
            if (values[x] == 0) {
                continue;
            }
            dataset.setValue(items[x], new Double(values[x]));
            valuesMap.put(items[x], values[x] + "");
            if (colors != null) {
                plot.setSectionPaint(items[x], colors[x]);
                this.selectedColors.put(items[x], colors[x].darker());
                this.defaultColors.put(items[x], colors[x]);
            }
        }
        selectAllLabel.setValue(values[values.length - 1] + "");
//        this.redrawChart();

    }

    public int getValue(String key) {
        if (valuesMap.containsKey(key)) {
            return Integer.valueOf(valuesMap.get(key));
        } else {
            return 0;
        }
    }

    /**
     *
     */
    public void redrawChart() {
        String imgUrl = saveToFile(chart, width, height);
        this.chartBackgroundImg.setSource(new ExternalResource(imgUrl));

        this.removeComponent(middleDountLayout);
        this.addComponent(middleDountLayout, "left: " + ((chartRenderingInfo.getPlotInfo().getDataArea().getCenterX()) - 50) + "px; top: " + (chartRenderingInfo.getPlotInfo().getDataArea().getCenterY() - 50) + "px");

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (!chartRenderingInfo.getPlotInfo().getDataArea().contains(event.getRelativeX(), event.getRelativeY())) {
            return;
        }
        Comparable sliceName = "all";
        if (event.getClickedComponent() == null || event.getClickedComponent() instanceof Image) {
            ChartEntity entity = chartRenderingInfo.getEntityCollection().getEntity(event.getRelativeX(), event.getRelativeY());
            if (entity instanceof PieSectionEntity) {
                PieSectionEntity pieEnt = (PieSectionEntity) entity;
                selectSlice(pieEnt.getSectionKey());
                sliceName = pieEnt.getSectionKey();
            }

        } else if (event.getClickedComponent() instanceof VerticalLayout || event.getClickedComponent() instanceof Label) {
            selectSlice("all");

        }

        sliceClicked(sliceName);
    }

    public void selectSlice(Comparable sliceKey) {

        if (selectionSet.contains("all")) {
            selectionSet.clear();
        } else if (sliceKey.toString().equalsIgnoreCase("all")) {

            if (middleDountLayout.getStyleName().contains("selected")) {
                middleDountLayout.removeStyleName("selected");
                selectionSet.remove("all");
            } else {
                middleDountLayout.addStyleName("selected");
                selectionSet.clear();
                selectionSet.add("all");
            }
        } else if (plot.getSectionOutlinePaint(sliceKey) == selectedColor) {
            plot.setSectionOutlinePaint(sliceKey, null);
            plot.setSectionPaint(sliceKey, defaultColors.get(sliceKey));
            selectionSet.remove(sliceKey.toString());
        } else {
            plot.setSectionPaint(sliceKey, selectedColors.get(sliceKey));
            plot.setSectionOutlinePaint(sliceKey, selectedColor);
//            plot.setSectionOutlineStroke(sliceKey, stroke);
            selectionSet.add(sliceKey.toString());
        }
        if (selectionSet.contains("all") || selectionSet.size() == valuesMap.size() || (!selectionSet.isEmpty() && valuesMap.size() == 1)) {
            middleDountLayout.addStyleName("selected");
            selectionSet.clear();
            selectionSet.add("all");
            valuesMap.keySet().stream().map((keys) -> {
                plot.setSectionOutlinePaint(keys, selectedColor);
                return keys;
            }).forEach((keys) -> {
                plot.setSectionPaint(keys, selectedColors.get(keys));
            });
        }
        if (selectionSet.isEmpty()) {
            resetChart();
        } else {
            redrawChart();
        }

    }

    public Set<String> getSelectionSet() {
        if ((!selectionSet.isEmpty() && valuesMap.size() == 1)) {
            selectionSet.clear();
            selectionSet.add(valuesMap.keySet().iterator().next().toString());
        }
        return selectionSet;
    }

    public void resetChart() {
        middleDountLayout.removeStyleName("selected");
        valuesMap.keySet().stream().map((keys) -> {
            plot.setSectionOutlinePaint(keys, null);
            return keys;
        }).forEach((keys) -> {
            plot.setSectionPaint(keys, defaultColors.get(keys));
        });
        redrawChart();
    }

    public abstract void sliceClicked(Comparable sliceKey);

}
