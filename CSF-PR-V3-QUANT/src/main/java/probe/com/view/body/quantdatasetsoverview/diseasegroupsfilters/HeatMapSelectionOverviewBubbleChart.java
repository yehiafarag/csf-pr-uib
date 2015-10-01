/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters;

import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.codec.binary.Base64;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberTick;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.axis.Tick;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBubbleRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.text.TextUtilities;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.TextAnchor;
import probe.com.handlers.CSFPRHandler;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
import probe.com.selectionmanager.CSFFilter;
import probe.com.selectionmanager.DatasetExploringCentralSelectionManager;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.DiseaseGroupsComparisonsProteinLayout;
import probe.com.view.core.JfreeExporter;
import probe.com.view.core.jfreeutil.SquaredDot;

/**
 *
 * @author Yehia Farag
 */
public class HeatMapSelectionOverviewBubbleChart extends AbsoluteLayout implements CSFFilter {

    private final String teststyle;
    private final Page.Styles styles = Page.getCurrent().getStyles();
    private String defaultImgURL;
    private final ChartRenderingInfo chartRenderingInfo = new ChartRenderingInfo();
    private final DatasetExploringCentralSelectionManager datasetExploringCentralSelectionManager;
    private final int width, height;
    private final CSFPRHandler handler;
    private final VerticalLayout initialLayout;
    private JFreeChart chart;
    private JfreeExporter exporter = new JfreeExporter();

    public void updateSize(int updatedWidth) {
        defaultImgURL = saveToFile(chart, updatedWidth, height);
        this.setWidth(updatedWidth + "px");
        this.redrawChart();

    }

    public HeatMapSelectionOverviewBubbleChart(DatasetExploringCentralSelectionManager datasetExploringCentralSelectionManager, CSFPRHandler handler, int chartWidth, int chartHeight, Set<QuantDiseaseGroupsComparison> selectedComparisonList) {
        this.width = chartWidth;
        this.height = chartHeight;
        this.handler = handler;
        this.setWidth(width + "px");
        this.setHeight(height + "px");
        this.datasetExploringCentralSelectionManager = datasetExploringCentralSelectionManager;
        this.datasetExploringCentralSelectionManager.registerFilter(HeatMapSelectionOverviewBubbleChart.this);
//        this.defaultImgURL = initBarChart(chartWidth, chartHeight, selectedComparisonList);
        this.teststyle = "heatmapOverviewBubbleChart";
        initialLayout = new VerticalLayout();
//        this.addComponent(initialLayout);
        initialLayout.setWidth("100%");
        initialLayout.setHeightUndefined();

        VerticalLayout spacer = new VerticalLayout();
        spacer.setHeight("250px");
        spacer.setWidth("100%");
        spacer.setStyleName(Reindeer.LAYOUT_WHITE);
        initialLayout.addComponent(spacer);

        Label startLabel = new Label("<center><h2 style='color:gray;'><b>Select comparison from the table</b></h2></center>");
        startLabel.setContentMode(ContentMode.HTML);

        initialLayout.addComponent(startLabel);
        initialLayout.setComponentAlignment(startLabel, Alignment.MIDDLE_CENTER);

        Image handleft = new Image();
        handleft.setSource(new ThemeResource("img/handleft.png"));
        initialLayout.addComponent(handleft);
        initialLayout.setComponentAlignment(handleft, Alignment.MIDDLE_CENTER);
        this.addComponent(initialLayout);

    }
    private boolean isNewImge = true;

    private JFreeChart updateBubbleChartChart(Set<QuantDiseaseGroupsComparison> selectedComparisonList) {

        DefaultXYZDataset defaultxyzdataset = new DefaultXYZDataset();

//        double ad[] = {0, 1, 2};
//        double ad1[] = {0, 1, 3};
//        double ad2[] = {0.1, 1, 0.1};
//        double ad3[][] = {ad, ad1, ad2};
        int counter = 0;
        int upper = -1;
        boolean significantOnly = this.datasetExploringCentralSelectionManager.isSignificantOnly();

        for (QuantDiseaseGroupsComparison qc : selectedComparisonList) {

            if (significantOnly) {
                int upperCounter = 0;
                for (DiseaseGroupsComparisonsProteinLayout qp : qc.getComparProtsMap().values()) {
                    if (qp == null) {
                        continue;
                    }

                    if (qp.getSignificantTrindCategory() == 2) {
                        continue;
                    }
                    
                    upperCounter++;
                }
                if(upperCounter>upper)
                    upper=upperCounter;

            } else {
                if (qc.getComparProtsMap() == null) {
                    System.out.println("null qc " + qc.getComparisonHeader());

                }
                if (qc.getComparProtsMap().size() > upper) {
                    upper = qc.getComparProtsMap().size();
                }
            }

        }

        final Map<Integer, Color[]> seriousColorMap = new HashMap<Integer, Color[]>();
        Color[] dataColor = new Color[]{new Color(0, 153, 0), new Color(0, 229, 132), new Color(1, 141, 244), new Color(255, 51, 51), new Color(204, 0, 0)};

//        String[] rgbColorArr = new String[selectedComparisonList.size()];
        for (QuantDiseaseGroupsComparison qc : selectedComparisonList) {

            double[] tempWidthValue = new double[5];
            if (qc.getComparProtsMap() == null) {
                continue;
            }

            for (String key : qc.getComparProtsMap().keySet()) {
                qc.getComparProtsMap().get(key).updateLabelLayout();

                if (significantOnly && qc.getComparProtsMap().get(key).getSignificantTrindCategory() == 2) {
                    tempWidthValue[qc.getComparProtsMap().get(key).getSignificantTrindCategory()] = 0;
                } else {
                    tempWidthValue[qc.getComparProtsMap().get(key).getSignificantTrindCategory()] = tempWidthValue[qc.getComparProtsMap().get(key).getSignificantTrindCategory()] + 1;
                }
            }
            int length = 0;
            if (upper < 10) {
                upper = 10;
            }
            for (int x = 0; x < tempWidthValue.length; x++) {
                if (tempWidthValue[x] > 0) {
                    tempWidthValue[x] = scaleValues(tempWidthValue[x], upper, 2.5, 0.05);//Math.max(tempWidthValue[x] * 1.5 / upper, 0.05);
                    length++;
                }

            }

            double[] yAxisValue = new double[length];
            double[] xAxisValue = new double[length];
            double[] widthValue = new double[length];
            Color[] serColorArr = new Color[length];
            length = 0;
            for (int x = 0; x < tempWidthValue.length; x++) {
                if (tempWidthValue[x] > 0) {
                    xAxisValue[length] = x;
                    yAxisValue[length] = counter;
                    widthValue[length] = tempWidthValue[x];
                    serColorArr[length] = dataColor[x];
                    length++;
                }

            }

            if (length == 1 && selectedComparisonList.size() == 1) {
//                    System.out.print(" , " + scaleValues(tempWidthValue[x],upper,2.5,0.05));
                widthValue[0] = 1;
            }
            seriousColorMap.put(counter, serColorArr);

            double[][] seriesValues = {yAxisValue, xAxisValue, widthValue};
            defaultxyzdataset.addSeries(qc.getComparisonHeader(), seriesValues);
//            rgbColorArr[counter] = qc.getRgbStringColor();
            counter++;
        }
//         defaultxyzdataset.addSeries("ser 1", ad3);

//        JFreeChart jfreechart = ChartFactory.createBubbleChart(
//                null,
//                null,
//                null,
//                defaultxyzdataset,
//                PlotOrientation.HORIZONTAL,
//                false, true, false);
//
//        XYPlot rendererPlot = (XYPlot) jfreechart.getPlot();
        final Color[] labelsColor = new Color[]{new Color(80, 183, 71), Color.LIGHT_GRAY, new Color(1, 141, 244), Color.LIGHT_GRAY, new Color(204, 0, 0)};
        SymbolAxis yAxis = new SymbolAxis(null, new String[]{"Down Regulated", " ", "Not Regulated", " ", "Up Regulated"}) {
            int x = 0;

            @Override
            public Paint getTickLabelPaint() {
                if (x >= labelsColor.length) {
                    x = 0;
                }
                return labelsColor[x++];
            }
        };
//        
        yAxis.setGridBandsVisible(false);
        yAxis.setAxisLinePaint(Color.LIGHT_GRAY);

        String[] xAxisLabels = new String[selectedComparisonList.size()];
        int x = 0;
        for (QuantDiseaseGroupsComparison comp : selectedComparisonList) {
            xAxisLabels[x] = comp.getComparisonHeader() + " (" + comp.getDatasetIndexes().length + ")";
            x++;

        }
        SymbolAxis xAxis = new SymbolAxis(null, xAxisLabels) {
            @Override
            protected List refreshTicksHorizontal(Graphics2D g2, Rectangle2D dataArea, RectangleEdge edge) {
                List ticks = new java.util.ArrayList();
                Font tickLabelFont = getTickLabelFont();
                g2.setFont(tickLabelFont);
                double size = getTickUnit().getSize();
                int count = calculateVisibleTickCount();
                double lowestTickValue = calculateLowestVisibleTickValue();
                double previousDrawnTickLabelPos = 0.0;
                double previousDrawnTickLabelLength = 0.0;
                if (count <= ValueAxis.MAXIMUM_TICK_COUNT) {
                    for (int i = 0; i < count; i++) {
                        double currentTickValue = lowestTickValue + (i * size);
                        double xx = valueToJava2D(currentTickValue, dataArea, edge);
                        String tickLabel;
                        NumberFormat formatter = getNumberFormatOverride();
                        if (formatter != null) {
                            tickLabel = formatter.format(currentTickValue);
                        } else {
                            tickLabel = valueToString(currentTickValue);
                        }
                        // avoid to draw overlapping tick labels
                        Rectangle2D bounds = TextUtilities.getTextBounds(tickLabel, g2,
                                g2.getFontMetrics());
                        double tickLabelLength = isVerticalTickLabels()
                                ? bounds.getHeight() : bounds.getWidth();
                        boolean tickLabelsOverlapping = false;
                        if (i > 0) {
                            double avgTickLabelLength = (previousDrawnTickLabelLength
                                    + tickLabelLength) / 2.0;
                            if (Math.abs(xx - previousDrawnTickLabelPos)
                                    < avgTickLabelLength) {
                                tickLabelsOverlapping = true;
                            }
                        }
                        if (tickLabelsOverlapping) {
                            setVerticalTickLabels(true);
                        } else {
                            // remember these values for next comparison
                            previousDrawnTickLabelPos = xx;
                            previousDrawnTickLabelLength = tickLabelLength;
                        }
                        TextAnchor anchor;
                        TextAnchor rotationAnchor;
                        double angle = 0.0;
                        if (isVerticalTickLabels()) {
                            anchor = TextAnchor.CENTER_RIGHT;
                            rotationAnchor = TextAnchor.CENTER_RIGHT;
                            if (edge == RectangleEdge.TOP) {
                                angle = 76.5;
                            } else {
                                angle = -76.5;
                            }
                        } else {
                            if (edge == RectangleEdge.TOP) {
                                anchor = TextAnchor.BOTTOM_CENTER;
                                rotationAnchor = TextAnchor.BOTTOM_CENTER;
                            } else {
                                anchor = TextAnchor.TOP_CENTER;
                                rotationAnchor = TextAnchor.TOP_CENTER;
                            }
                        }
                        Tick tick = new NumberTick(new Double(currentTickValue),
                                tickLabel, anchor, rotationAnchor, angle);
                        ticks.add(tick);
                    }
                }
                return ticks;
            }
        };
        xAxis.setGridBandsVisible(false);
        xAxis.setAxisLinePaint(Color.LIGHT_GRAY);
//
//        yAxis.setUpperBound(5);
//        yAxis.setLowerBound(-1);

        int scale = XYBubbleRenderer.SCALE_ON_RANGE_AXIS;

        XYItemRenderer xyitemrenderer = new XYBubbleRenderer(scale) {
            int counter = 0;
            int localSerious = -1;

            @Override
            public Paint getSeriesPaint(int series) {

                if (series != localSerious || isNewImge) {
                    counter = 0;
                    isNewImge = false;
                }
                localSerious = series;
                Color c = seriousColorMap.get(series)[counter];
                counter++;

                return c;//super.getSeriesPaint(series); //To change body of generated methods, choose Tools | Templates.
            }

        };

//        xyitemrenderer.setSeriesShape(0, );
        XYPlot xyplot = new XYPlot(defaultxyzdataset, xAxis, yAxis, xyitemrenderer);

        JFreeChart generatedChart = new JFreeChart(xyplot);
        xyplot.setOutlineVisible(false);

        generatedChart.removeLegend();
        xyplot.setForegroundAlpha(0.65F);
        xyplot.setBackgroundPaint(Color.WHITE);
        generatedChart.setBackgroundPaint(Color.WHITE);

//        
//        for (int z = 0; z < rgbColorArr.length; z++) {
//            String rgb = rgbColorArr[z];
//            rgb = rgb.replace("RGB", "").replace("(", "").replace(")", "");Integer R = Integer.valueOf(rgb.split(",")[0]);
//            Integer G = Integer.valueOf(rgb.split(",")[1]);
//            Integer B = Integer.valueOf(rgb.split(",")[2]);
//            xyitemrenderer.setSeriesPaint(z, new Color(R, G, B));
//        }
//        NumberAxis numberaxis = (NumberAxis) xyplot.getDomainAxis();
//        numberaxis.setLowerMargin(0.2);
//        numberaxis.setUpperMargin(0.5);
//        NumberAxis numberaxis1 = (NumberAxis) xyplot.getRangeAxis();
//        numberaxis1.setLowerMargin(0.8);
//        numberaxis1.setUpperMargin(0.9);
        exporter.writeChartToPDFFile(generatedChart, 595, 842, "bublechart.pdf");
        return generatedChart;

    }

    private String saveToFile(final JFreeChart chart, final double width, final double height) {
        isNewImge = true;
//        CategoryPlot cp = generatedChart.getCategoryPlot();
//        CategoryAxis domainAxis = cp.getDomainAxis();
//        NumberAxis rangeAxis = (NumberAxis) cp.getRangeAxis();
//        boolean check = (width > 120);
//
//        domainAxis.setVisible(check);
//        rangeAxis.setVisible(check);
//        ((BarRenderer) cp.getRenderer()).setSeriesVisible(0, check);
//        ((BarRenderer) cp.getRenderer()).setSeriesVisible(1, check);
//        ((BarRenderer) cp.getRenderer()).setSeriesVisible(2, check);
//        ((BarRenderer) cp.getRenderer()).setSeriesVisible(3, check);
//        ((BarRenderer) cp.getRenderer()).setSeriesVisible(4, check);
//        if (check) {
//            cp.setRangeGridlinePaint(Color.LIGHT_GRAY);
//            TextTitle title = generatedChart.getTitle();
//            title.setPosition(RectangleEdge.TOP);
//            generatedChart.setTitle(title);
//                        
//        } else {
//            cp.setRangeGridlinePaint(Color.WHITE);
//            TextTitle title = generatedChart.getTitle();
//            title.setPosition(RectangleEdge.RIGHT);
//            generatedChart.setTitle(title);
//        }

        byte imageData[];
        try {

            imageData = ChartUtilities.encodeAsPNG(chart.createBufferedImage((int) width, (int) height, chartRenderingInfo));
            this.removeAllComponents();
            for (int i = 0; i < chartRenderingInfo.getEntityCollection().getEntityCount(); i++) {
                ChartEntity entity = chartRenderingInfo.getEntityCollection().getEntity(i);
                if (entity instanceof XYItemEntity) {
                    XYItemEntity catEnt = (XYItemEntity) entity;
                    SquaredDot square = new SquaredDot("cycle");
                    String[] coords = catEnt.getShapeCoords().split(",");
                    int smallX = Integer.MAX_VALUE;
                    int largeX = Integer.MIN_VALUE;
                    int smallY = Integer.MAX_VALUE;
                    int largeY = Integer.MIN_VALUE;
                    for (int x = 0; x < coords.length; x++) {
                        String coorX = coords[x++];
                        if (Integer.valueOf(coorX) < smallX) {
                            smallX = Integer.valueOf(coorX);
                        }
                        if (Integer.valueOf(coorX) > largeX) {
                            largeX = Integer.valueOf(coorX);
                        }

                        String coorY = coords[x];
                        if (Integer.valueOf(coorY) < smallY) {
                            smallY = Integer.valueOf(coorY);

                        }
                        if (Integer.valueOf(coorY) > largeY) {
                            largeY = Integer.valueOf(coorY);
                        }

                    }

//                     Integer.valueOf(coords[3]) - Integer.valueOf(coords[1]);
//                    if (sqheight < 2) {
//                        barCounter++;
//                        continue;
//                    } else if (sqheight < 14) {
//                        coords[1] = (Integer.valueOf(coords[1]) - (14 - sqheight)) + "";
//                    }
//
                    int sqheight = (largeY - smallY);
                    if (sqheight < 2) {
                        continue;
                    } else if (sqheight < 14) {
                        smallY = smallY - (14 - sqheight);
                    }

                    int sqwidth = (largeX - smallX);
                    square.setWidth(sqwidth + "px");
                    square.setHeight(sqheight + "px");
                    square.setDescription("#Proteins " + ((QuantDiseaseGroupsComparison) selectedComparisonList.toArray()[catEnt.getSeriesIndex()]).getComparProtsMap().size());
                    square.setParam("barIndex", ((XYItemEntity) entity).getSeriesIndex());
                    this.addComponent(square, "left: " + smallX + "px; top: " + (smallY) + "px;");
                }
            }
            String base64 = Base64.encodeBase64String(imageData);
            base64 = "data:image/png;base64," + base64;
            return base64;
        } catch (IOException e) {
            System.err.println("at error " + e.getMessage());
        }
        return "";
    }

    /**
     *
     */
    public final void redrawChart() {
        styles.add("." + teststyle + " {  background-image: url(" + defaultImgURL + " );background-position:center; background-repeat: no-repeat; }");
        this.setStyleName(teststyle);
    }

    private Set<QuantDiseaseGroupsComparison> selectedComparisonList;

    @Override
    public void selectionChanged(String type) {
        if (type.equalsIgnoreCase("Comparison_Selection")) {
            selectedComparisonList = this.datasetExploringCentralSelectionManager.getSelectedDiseaseGroupsComparisonList();
            Iterator<QuantDiseaseGroupsComparison> itr = selectedComparisonList.iterator();
            while (itr.hasNext()) {
                if (itr.next().getComparProtsMap() == null) {
                    selectedComparisonList = handler.getComparisonProtList(selectedComparisonList, null);
                    break;
                }

            }
            if (selectedComparisonList.isEmpty()) {
                initialLayout.setVisible(true);
                this.removeAllComponents();
                this.addComponent(initialLayout);
                defaultImgURL = "";
            } else {
                initialLayout.setVisible(false);
                chart = this.updateBubbleChartChart(selectedComparisonList);
                defaultImgURL = saveToFile(chart, width, height);;
            }
            this.redrawChart();

        }
    }

    @Override
    public String getFilterId() {
        return this.getClass().getName();
    }

    @Override
    public void removeFilterValue(String value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Converts the value from linear scale to log scale. The log scale numbers
     * are limited by the range of the type float. The linear scale numbers can
     * be any double value.
     *
     * @param linearValue the value to be converted to log scale
     * @return the value in log scale
     * @throws IllegalArgumentException if value out of range
     */
    public final double scaleValues(double linearValue, int max, double upperLimit, double lowerLimit) {
        if (linearValue == 0) {
            return 0.0;
        }
        double logValue = (linearValue * upperLimit / (double) max) + lowerLimit;
        return logValue;

//        float inverseNaturalLogBase = 1.0f / (float) Math.log(2.0f);
//        return (float) Math.log(linearValue) * inverseNaturalLogBase/10.0f;
    }

}
