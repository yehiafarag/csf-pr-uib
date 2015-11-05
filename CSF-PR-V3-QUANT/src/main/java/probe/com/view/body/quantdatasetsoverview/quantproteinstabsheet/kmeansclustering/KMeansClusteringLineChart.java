package probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.kmeansclustering;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.ui.AbsoluteLayout;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.apache.commons.codec.binary.Base64;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberTick;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.axis.Tick;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.SeriesRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.text.TextUtilities;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.TextAnchor;
import org.jfree.util.ShapeUtilities;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.DiseaseGroupsComparisonsProteinLayout;

/**
 *
 * @author Yehia Farag
 */
public class KMeansClusteringLineChart extends AbsoluteLayout implements LayoutEvents.LayoutClickListener {

    private final String teststyle;
    private final Page.Styles styles = Page.getCurrent().getStyles();
    private String defaultKMeansClusteringImgUrl;
    private final ChartRenderingInfo chartRenderingInfo;
    private final JFreeChart chart;
    private final int imgWidth, imgHeight;

    public KMeansClusteringLineChart(int imgWidth, int imgHeight, String proteinKey, String proteinAccession, String proteinName, Map<String, DiseaseGroupsComparisonsProteinLayout[]> protSelectionMap, Set<QuantDiseaseGroupsComparison> selectedComparisonList) {
        imgWidth = imgWidth - 100;
        setWidth(imgWidth + "px");
        setHeight(imgHeight + "px");
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
        teststyle = proteinAccession.replace(" ", "_").replace(")", "_").replace("(", "_").toLowerCase() + "_" + proteinName.replace(" ", "_").replace(")", "_").replace("(", "_").toLowerCase() + "_kmeansclusteringplot";

        addLayoutClickListener(KMeansClusteringLineChart.this);
        chart = this.generateLineChart(proteinKey, protSelectionMap, selectedComparisonList);
        chartRenderingInfo = new ChartRenderingInfo();
        defaultKMeansClusteringImgUrl = this.generateChartImage(chart, imgWidth, imgHeight, chartRenderingInfo);
        styles.add("." + teststyle + " {  background-image: url(" + defaultKMeansClusteringImgUrl + " );background-position:center; background-repeat: no-repeat; }");
        setStyleName(teststyle);

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {

    }
    private String lastSelectedIndex = "";
    private String mainProteinSeriesKey = "";
    private int mainProteinSeriesIndex = -1;
    private final Random rand = new Random();
    private final BasicStroke lineStyle = new BasicStroke(
            1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
            1.0f, new float[]{10.0f, 6.0f}, 0.0f
    );
    private final Shape defaultShape = ShapeUtilities.createRegularCross(2f, 5f);
    private final Shape lineShape = ShapeUtilities.createDiamond(2f);

    ;

    public final void updateChartImage(String proteinKey) {
        XYPlot plot = ((XYPlot) chart.getPlot());
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        DefaultXYDataset dataset = (DefaultXYDataset) plot.getDataset();

        for (int serId = 0; serId < dataset.getSeriesCount(); serId++) {
            renderer.setSeriesPaint(serId,Color.LIGHT_GRAY);// new Color(240, 240, 240));
        }
        if (lastSelectedIndex.equalsIgnoreCase(proteinKey)) {
            proteinKey = mainProteinSeriesKey;
        }

        Integer seriesIndex = proteinToSeriesMap.get(proteinKey);
        String tempProtKey = "__" + proteinKey + "__" + (rand.nextInt(100000000) + 1);
        dataset.addSeries(tempProtKey, seriesKeyToSeriesMap.get(proteinKey));
                 
        
        if (seriesIndex == null || seriesIndex == mainProteinSeriesIndex) {
            renderer.setSeriesPaint(dataset.getSeriesCount() - 1, Color.RED);

        } else {
            renderer.setSeriesPaint(dataset.getSeriesCount() - 1, Color.BLUE);

        }
        if (dataset.getItemCount(dataset.getSeriesCount() - 1) > 1) {
            renderer.setSeriesStroke(dataset.getSeriesCount() - 1, lineStyle);
            renderer.setSeriesShapesVisible(dataset.getSeriesCount() - 1, true);
             renderer.setSeriesShape(dataset.getSeriesCount() - 1, lineShape);
            renderer.setSeriesLinesVisible(dataset.getSeriesCount() - 1, true);

        } else {
            renderer.setSeriesShape(dataset.getSeriesCount() - 1, defaultShape);
            renderer.setSeriesShapesVisible(dataset.getSeriesCount() - 1, true);
            renderer.setSeriesLinesVisible(dataset.getSeriesCount() - 1, false);
        }

       lastSelectedIndex = proteinKey;
        plot.setRenderer(renderer);
        plot.setSeriesRenderingOrder(SeriesRenderingOrder.FORWARD);
        defaultKMeansClusteringImgUrl = this.generateChartImage(chart, imgWidth, imgHeight, chartRenderingInfo);
        styles.add("." + teststyle + " {  background-image: url(" + defaultKMeansClusteringImgUrl + " );background-position:center; background-repeat: no-repeat; }");
        setStyleName(teststyle);

    }

    private final Map<String, Integer> proteinToSeriesMap = new HashMap<String, Integer>();
    private final Map<String, double[][]> seriesKeyToSeriesMap = new HashMap<String, double[][]>();

    private JFreeChart generateLineChart(String proteinKey, Map<String, DiseaseGroupsComparisonsProteinLayout[]> comparisonProteins, Set<QuantDiseaseGroupsComparison> selectedComparisonList) {

        proteinToSeriesMap.clear();
        int comparisonsSize = 0;
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        int seriousCounter = 0;
        DefaultXYDataset dataset = new DefaultXYDataset();

        for (String key : comparisonProteins.keySet()) {
            List<Double> xLineList = new ArrayList<Double>();
            List<Double> yLineList = new ArrayList<Double>();
            double[][] linevalues = new double[2][comparisonProteins.get(key).length];
            comparisonsSize = comparisonProteins.get(key).length;
            int comparisonIndexer = 0;
            int compIndex = 0;

            for (DiseaseGroupsComparisonsProteinLayout cp : comparisonProteins.get(key)) {
                if (cp == null) {
                    comparisonIndexer++;
                    continue;
                }
                if (compIndex >= selectedComparisonList.size()) {
                    break;
                }
                xLineList.add((double) comparisonIndexer);
                if (cp.getSignificantTrindCategory() == 4) {
                    yLineList.add(4d);
                } else if (cp.getSignificantTrindCategory() == 3) {
                    yLineList.add(3d);
                } else if (cp.getSignificantTrindCategory() == 2) {
                    yLineList.add(2d);
                } else if (cp.getSignificantTrindCategory() == 1) {
                    yLineList.add(1d);
                } else if (cp.getSignificantTrindCategory() == 0) {
                    yLineList.add(0d);
                }
                compIndex++;
                comparisonIndexer++;

            }

            double[] xLineValues = new double[xLineList.size()];
            double[] yLineValues = new double[yLineList.size()];
            for (int x = 0; x < yLineList.size(); x++) {
                xLineValues[x] = xLineList.get(x);
                yLineValues[x] = yLineList.get(x);
            }

            linevalues[0] = xLineValues;
            linevalues[1] = yLineValues;

            dataset.addSeries(key, linevalues);

            if (key.equalsIgnoreCase(proteinKey)) {
                renderer.setSeriesPaint(seriousCounter, Color.RED);
                renderer.setSeriesStroke(seriousCounter, new BasicStroke(
                        1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{10.0f, 6.0f}, 0.0f
                ));
                mainProteinSeriesIndex = seriousCounter;
                mainProteinSeriesKey = proteinKey;
            } else {
                renderer.setSeriesPaint(seriousCounter, Color.LIGHT_GRAY);
            }
            if (comparisonProteins.get(key).length == 1) {
                renderer.setSeriesShape(seriousCounter, defaultShape);

            } else {
                renderer.setSeriesShapesVisible(seriousCounter, false);
            }
            proteinToSeriesMap.put(key, seriousCounter);
            seriesKeyToSeriesMap.put(key, linevalues);

            seriousCounter++;

        }

        String[] xAxisLabels = new String[comparisonsSize];
        int x = 0;
        for (QuantDiseaseGroupsComparison comp : selectedComparisonList) {
            xAxisLabels[x] = comp.getComparisonHeader();
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
        final Color[] labelsColor = new Color[]{new Color(80, 183, 71), Color.LIGHT_GRAY, new Color(1, 141, 244), Color.LIGHT_GRAY, Color.RED};
        SymbolAxis yAxis = new SymbolAxis(null, new String[]{"Low", " ", "Stable", " ", "High"}) {
            int x = 0;

            @Override
            public Paint getTickLabelPaint() {
                if (x >= labelsColor.length) {
                    x = 0;
                }
                return labelsColor[x++];
            }
        };
        xAxis.setGridBandsVisible(false);
        yAxis.setGridBandsVisible(false);
        yAxis.setAxisLinePaint(Color.WHITE);
        XYPlot xyplot = new XYPlot(dataset, xAxis, yAxis, renderer) {
            private int x = 0;

            @Override
            public Paint getRangeGridlinePaint() {
                if (x >= 5) {
                    x = 0;
                }
                if (x == 2) {
                    x++;
                    return super.getRangeGridlinePaint();
                } else {
                    x++;
                    return Color.WHITE;
                }
            }
        };
//        xyplot.setSeriesRenderingOrder(SeriesRenderingOrder.REVERSE);

//
        xyplot.setRangeTickBandPaint(Color.WHITE);
        JFreeChart jFreeChart = new JFreeChart(null, new Font("Tahoma", 0, 18), xyplot, false);

        jFreeChart.setBackgroundPaint(Color.WHITE);
        final XYPlot plot = jFreeChart.getXYPlot();

        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setOutlinePaint(Color.GRAY);
        jFreeChart.setBorderVisible(false);
        jFreeChart.setAntiAlias(false);

        return jFreeChart;
    }

    private String generateChartImage(JFreeChart jFreeChart, int w, int h, ChartRenderingInfo chartRenderingInfo) {
        String imgUrl = saveToFile(jFreeChart, w, h, chartRenderingInfo);
//      
        return imgUrl;

    }

    private String saveToFile(final JFreeChart chart, final double width, final double height, ChartRenderingInfo chartRenderingInfo) {
        try {
            byte[] imageData = ChartUtilities.encodeAsPNG(chart.createBufferedImage((int) width, (int) height, chartRenderingInfo));
            String base64 = Base64.encodeBase64String(imageData);
            base64 = "data:image/png;base64," + base64;
            return base64;
        } catch (IOException e) {
            System.err.println("at error " + e.getMessage());
        }
        return "";

    }

}
