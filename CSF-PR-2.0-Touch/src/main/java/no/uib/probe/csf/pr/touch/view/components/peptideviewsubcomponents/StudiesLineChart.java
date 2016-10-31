package no.uib.probe.csf.pr.touch.view.components.peptideviewsubcomponents;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Image;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import no.uib.probe.csf.pr.touch.logic.AlphanumComparator;
import no.uib.probe.csf.pr.touch.logic.beans.QuantComparisonProtein;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDataset;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.view.core.TrendSymbol;
import org.apache.commons.codec.binary.Base64;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTick;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.axis.Tick;
import org.jfree.chart.axis.TickUnit;
import org.jfree.chart.axis.TickUnits;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.text.TextUtilities;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
import org.jfree.util.ShapeUtilities;

/**
 * This class represents line chart in the protein details tab, the chart view
 * the overall datasets trend and detailed dataset information.
 *
 * @author Yehia Farag
 */
public abstract class StudiesLineChart extends AbsoluteLayout implements LayoutEvents.LayoutClickListener {

    /**
     * JFree Components.
     */
    /**
     * JFreeChart used to generate thumb image and default chart image
     * background.
     */
    private JFreeChart lineChart;
    /**
     * Chart dataset.
     */
    private DefaultXYDataset dataset;
    /**
     * X Axis for JFree chart.
     */
    private SymbolAxis xAxis;
    /**
     * Y Axis for JFree chart.
     */
    private NumberAxis yAxis;
    /**
     * Counter is used for color mapping in the JFree chart.
     */
    private int gridcounter = 0;
    /**
     * Counter is used for color mapping in the JFree chart.
     */
    private int gridcounterII = 0;
    /**
     * Array of chart Y access ticks text showing the trend.
     */
    final String[] tickLabels = new String[]{"Decreased", " ", "Equal", " ", "Increased"};
    /**
     * Use vertical/horizontal label for X access.
     */
    private boolean verticalLabels;
    /**
     * Array of tool-tips for different protein trends .
     */
    private final String[] tooltipsIcon = new String[]{"All Increased", "Mainly Increased", "Equal", "Mainly Decreased", "All Decreased", "No Quant. Info", "No Data Available "};
    /**
     * Chart rendering information that has the all information required for
     * drawing Vaadin bubbles in the absolute layout.
     */
    private final ChartRenderingInfo chartRenderingInfo = new ChartRenderingInfo();
    /**
     * BufferedImage is used to generate encoded64 string images that is used in
     * Vaadin image background and thumb image.
     */
    private BufferedImage bufferedChartImg;

    /**
     * Customized user data trend AWT color needed for JFree chart image
     * generator based on user input data in quant comparison layout.
     */
    private final Color[] customizedUserDataColor = new Color[]{Color.decode("#e5ffe5"), Color.WHITE, Color.decode("#e6f4ff"), Color.WHITE, Color.decode("#ffe5e5")};

    /**
     * VaadinLayout Components.
     */
    /**
     * The main chart background image (to be updated using JFreechart).
     */
    private final Image chartImg;
    /**
     * The main chart data container (VaadinLayout bubble container).
     */
    private final AbsoluteLayout chartComponentsLayout;
    /**
     * The main component width.
     */
    private final int width;
    /**
     * The main component height.
     */
    private final int height;
    /**
     * Customized user data trend based on user input data in quant comparison
     * layout.
     */
    private int custTrend = -1;
    /**
     * Default chart image resource to work as URL resource generated from
     * JFreechart for the main chart layout.
     */
    private ExternalResource maxImgUrl;
    /**
     * Map of selected comparisons or datasets and its symbol component.
     */
    private final Map<String, TrendSymbol> symbolMap;
    /**
     * List of overall trends for protein comparisons(have same order as the
     * selected comparisons set) .
     */
    private final List<Integer> comparisonTrends;
    /**
     * The main protein key.
     */
    private String proteinKey;
    /**
     * The main protein name.
     */
    private String proteinName;
    /**
     * List of selected comparisons to be updated based on user selection for
     * comparisons across the system.
     */
    private Set<QuantDiseaseGroupsComparison> selectedComparisonList;
    /**
     * List of selected comparisons to be updated based on user selection and
     * ordered based on trend.
     */
    private Set<QuantDiseaseGroupsComparison> orderedComparisonList;
    /**
     * Map of selected comparisons and sub-datasets trends under each
     * comparison.
     */
    private final Map<String, Set<TrendSymbol>> subComparisonStudiesMap = new LinkedHashMap<>();

    /**
     * Map of selected comparisons and its overall trend for each comparison.
     */
    private final Map<String, Double> comparisonTrendMap = new LinkedHashMap<>();

    /**
     * Get main protein name
     *
     * @return proteinName Selected protein name.
     */
    public String getProteinName() {
        return proteinName;
    }

    /**
     * Get the current comparisons list (default or sorted list).
     *
     * @param trendOreder get sorted list based on trend.
     * @return selectedComparisonList the current used comparisons list.
     */
    public Set<QuantDiseaseGroupsComparison> getCurrentComparisonList(boolean trendOreder) {
        if (trendOreder) {
            return orderedComparisonList;
        } else {
            return selectedComparisonList;
        }
    }

    /**
     * Constructor to initialize the main attributes.
     *
     * @param width The line chart width.
     * @param height The line chart height.
     */
    public StudiesLineChart(int width, int height) {
        this.width = width;
        this.height = height;
        this.setWidth(width, Unit.PIXELS);
        this.setHeight(height, Unit.PIXELS);

        this.symbolMap = new LinkedHashMap<>();
        this.comparisonTrends = new ArrayList<>();

        chartImg = new Image();
        this.addStyleName("slowslide");
        chartImg.setWidth(100, Unit.PERCENTAGE);
        chartImg.setHeight(100, Unit.PERCENTAGE);
        this.addComponent(chartImg, "left: " + 0 + "px; top: " + 0 + "px;");

        chartComponentsLayout = new AbsoluteLayout();
        chartComponentsLayout.setWidth(100, Unit.PERCENTAGE);
        chartComponentsLayout.setHeight(100, Unit.PERCENTAGE);
        chartComponentsLayout.addLayoutClickListener((LayoutEvents.LayoutClickEvent event) -> {
            if (!(event.getClickedComponent() != null && (event.getClickedComponent() instanceof TrendSymbol))) {
                StudiesLineChart.this.select(null, -100);
            }
        });
        this.addComponent(chartComponentsLayout, "left: " + 0 + "px; top: " + 0 + "px;");
    }

    /**
     * Update chart data based on user selection.
     *
     * @param selectedComparisonsList Set of user selected comparisons(from heat
     * map component).
     * @param proteinKey The selected protein key (from proteins table
     * component).
     * @param custTrend The trend of user input data (in case of quant
     * comparisons mode)
     */
    public void updateData(Set<QuantDiseaseGroupsComparison> selectedComparisonsList, String proteinKey, int custTrend) {
        this.proteinKey = proteinKey;
        this.custTrend = custTrend;
        this.gridcounter = 0;
        this.gridcounterII = 0;
        this.selectedComparisonList = selectedComparisonsList;
        proteinName = null;

        this.updateDataset(selectedComparisonsList, proteinKey);

        if (lineChart == null) {
            this.lineChart = generateLineChart();//
        } else {
            lineChart.getXYPlot().setDataset(dataset);
            lineChart.getXYPlot().setDomainAxis((ValueAxis) xAxis);
            lineChart.getXYPlot().setRangeAxis((ValueAxis) yAxis);

        }

        rePaintChart(selectedComparisonsList);
        this.orderedComparisonList = getOrderStudiesByTrend();

    }

    private void rePaintChart(Set<QuantDiseaseGroupsComparison> comparisonsList) {
        lineChart.getXYPlot().getRenderer().setSeriesPaint(0, Color.GRAY);
        lineChart.getXYPlot().getDomainAxis().setVisible(true);
        lineChart.getXYPlot().getRangeAxis().setVisible(true);
        lineChart.getXYPlot().setOutlineVisible(false);
        lineChart.getXYPlot().getRenderer().setSeriesStroke(0, new BasicStroke(
                1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1.0f, new float[]{10.0f, 6.0f}, 0.0f
        ));
        lineChart.setPadding(new RectangleInsets(0, 0, 0, 0));
        maxImgUrl = new ExternalResource(this.getChartImage(lineChart, chartRenderingInfo, width, height));
        if (chartRenderingInfo.getEntityCollection().getEntityCount() < 4 || chartRenderingInfo.getPlotInfo().getDataArea().getHeight() < 220) {
            lineChart.getXYPlot().getDomainAxis().setVisible(false);
            maxImgUrl = new ExternalResource(this.getChartImage(lineChart, chartRenderingInfo, width, height));
            lineChart.getXYPlot().getDomainAxis().setVisible(true);

        }

        initLayoutComponents(comparisonsList);
        chartImg.setSource(maxImgUrl);
    }

    private void updateDataset(Set<QuantDiseaseGroupsComparison> selectedComparisonList, String key) {
        comparisonTrends.clear();
        dataset = new DefaultXYDataset();
        int compNumber = selectedComparisonList.size();
        double[][] linevalues = new double[2][compNumber];
        double[] xLineValues = new double[compNumber];
        double[] yLineValues = new double[compNumber];
        double trendValue;

        int comparisonIndex = 0;

        String[] xAxisLabels = new String[selectedComparisonList.size()];
        final Color[] diseaseGroupslabelsColor = new Color[selectedComparisonList.size()];
        int maxLength = -1;
        for (QuantDiseaseGroupsComparison comparison : selectedComparisonList) {
            String keyI = 0 + "_" + key;
            String keyII = 1 + "_" + key;
            String keyIII = 2 + "_" + key;
            String keyIV = 3 + "_" + key;
            String keyV = 4 + "_" + key;
            trendValue = 0.0;

            if (comparison.getQuantComparisonProteinMap().containsKey(keyI)) {
                trendValue = comparison.getQuantComparisonProteinMap().get(keyI).getOverallCellPercentValue();
                proteinName = comparison.getQuantComparisonProteinMap().get(keyI).getProteinName();
                comparisonTrends.add(comparison.getQuantComparisonProteinMap().get(keyI).getSignificantTrindCategory());
            } else if (comparison.getQuantComparisonProteinMap().containsKey(keyII)) {
                trendValue = comparison.getQuantComparisonProteinMap().get(keyII).getOverallCellPercentValue();
                proteinName = comparison.getQuantComparisonProteinMap().get(keyII).getProteinName();

                comparisonTrends.add(comparison.getQuantComparisonProteinMap().get(keyII).getSignificantTrindCategory());
            } else if (comparison.getQuantComparisonProteinMap().containsKey(keyIII)) {
                proteinName = comparison.getQuantComparisonProteinMap().get(keyIII).getProteinName();
                trendValue = comparison.getQuantComparisonProteinMap().get(keyIII).getOverallCellPercentValue();
                comparisonTrends.add(comparison.getQuantComparisonProteinMap().get(keyIII).getSignificantTrindCategory());
            } else if (comparison.getQuantComparisonProteinMap().containsKey(keyIV)) {
                trendValue = comparison.getQuantComparisonProteinMap().get(keyIV).getOverallCellPercentValue();
                proteinName = comparison.getQuantComparisonProteinMap().get(keyIV).getProteinName();

                comparisonTrends.add(comparison.getQuantComparisonProteinMap().get(keyIV).getSignificantTrindCategory());
            } else if (comparison.getQuantComparisonProteinMap().containsKey(keyV)) {
                trendValue = comparison.getQuantComparisonProteinMap().get(keyV).getOverallCellPercentValue();
                proteinName = comparison.getQuantComparisonProteinMap().get(keyV).getProteinName();

                comparisonTrends.add(comparison.getQuantComparisonProteinMap().get(keyV).getSignificantTrindCategory());
            } else {

                comparisonTrends.add(6);
            }

            xLineValues[comparisonIndex] = comparisonIndex;
            yLineValues[comparisonIndex] = trendValue;

            String groupCompTitle = comparison.getComparisonHeader();
            String updatedHeader = groupCompTitle.split(" / ")[0].split("__")[0] + " / " + groupCompTitle.split(" / ")[1].split("__")[0];//+ " ( " + groupCompTitle.split(" / ")[1].split("\n")[1] + " )";

            xAxisLabels[comparisonIndex] = updatedHeader;
            if (xAxisLabels[comparisonIndex].length() + 5 > maxLength) {
                maxLength = xAxisLabels[comparisonIndex].length() + 5;
            }
            diseaseGroupslabelsColor[comparisonIndex] = Color.decode(comparison.getDiseaseCategoryColor());
            comparisonIndex++;

        }

        linevalues[0] = xLineValues;
        linevalues[1] = yLineValues;
        dataset.addSeries("line", linevalues);
        verticalLabels = maxLength > 40 && selectedComparisonList.size() > 4;

        Font font = new Font("Helvetica Neue", Font.PLAIN, 13);

        xAxis = new SymbolAxis(null, xAxisLabels) {
            int x = 0;

            @Override
            public Paint getTickLabelPaint() {
                if (x >= diseaseGroupslabelsColor.length) {
                    x = 0;
                }
                return diseaseGroupslabelsColor[x++];
            }
//            

            private final boolean localfinal = verticalLabels;

            @Override
            protected List refreshTicksHorizontal(Graphics2D g2, Rectangle2D dataArea, RectangleEdge edge) {

                if (localfinal) {
                    setVerticalTickLabels(localfinal);
                    return super.refreshTicksHorizontal(g2, dataArea, edge);
                }
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
                        if (tickLabel == null) {
                            tickLabel = "";
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
        xAxis.setTickLabelFont(font);
        xAxis.setLabelInsets(new RectangleInsets(2, 5, 2, 5));

        yAxis = new NumberAxis() {
            final Color[] labelsColor = new Color[]{new Color(80, 183, 71), Color.LIGHT_GRAY, new Color(1, 141, 244), Color.LIGHT_GRAY, new Color(204, 0, 0)};
            private int counter = 0;

            @Override
            public Paint getTickLabelPaint() {

                if (counter >= 240) {

                    counter = 0;
                }

                if (counter == 20) {
                    counter++;
                    return labelsColor[0];
                }
                if (counter == 21) {
                    counter++;
                    return labelsColor[0];
                }

                if (counter == 120) {
                    counter++;
                    return labelsColor[2];
                }
                if (counter == 121) {
                    counter++;
                    return labelsColor[2];
                }
                if (counter == 220) {
                    counter++;
                    return labelsColor[4];
                }
                if (counter == 221) {
                    counter++;
                    return labelsColor[4];
                }
                counter++;

                return super.getTickLabelPaint(); //To change body of generated methods, choose Tools | Templates.
            }

        };
        TickUnits tus = new TickUnits();
        TickUnit unit = new NumberTickUnit(1) {

            @Override
            public String valueToString(double value) {
                if (value == 100.0) {
                    return "Increased";
                }
                if (value == 0.0) {
                    return "Equal";
                }
                if (value == -100.0) {
                    return "Decreased";
                }
                return "";
            }

        };
        tus.add(unit);

        yAxis.setStandardTickUnits(tus);
        yAxis.setUpperBound(120.0);
        yAxis.setLowerBound(-120.0);
        yAxis.setTickMarksVisible(false);
        yAxis.setAutoRangeStickyZero(false);

        yAxis.setTickLabelFont(font);
        xAxis.setGridBandsVisible(false);
        yAxis.setAxisLinePaint(Color.WHITE);

        int maxPatientsNumber = 0;
        for (QuantDiseaseGroupsComparison comparison : selectedComparisonList) {
            for (QuantDataset ds : comparison.getDatasetMap().values()) {
                int pNum = ds.getDiseaseMainGroup1Number() + ds.getDiseaseMainGroup2Number();
                if (pNum > maxPatientsNumber) {
                    maxPatientsNumber = pNum;
                }

            }

        }
        subComparisonStudiesMap.clear();

        comparisonIndex = 0;
        List<Point> subComparisonDatasetList = new ArrayList<>();
        for (QuantDiseaseGroupsComparison comparison : selectedComparisonList) {
            subComparisonDatasetList.addAll(this.updateSubComparisontsDataset(comparison, comparisonIndex, maxPatientsNumber));
            comparisonIndex++;

        }

        double[][] subcomparisonvalues = new double[2][subComparisonDatasetList.size()];
        double[] xSubcomparisonValues = new double[subComparisonDatasetList.size()];
        double[] ySubcomparisonValues = new double[subComparisonDatasetList.size()];

        int x = 0;
        for (Point point : subComparisonDatasetList) {
            xSubcomparisonValues[x] = point.getX();
            ySubcomparisonValues[x] = point.getY();
            x++;

        }

        subcomparisonvalues[0] = xSubcomparisonValues;
        subcomparisonvalues[1] = ySubcomparisonValues;

        dataset.addSeries("subdata", subcomparisonvalues);

    }

    private List<Point> updateSubComparisontsDataset(QuantDiseaseGroupsComparison comparison, int comparisonIndex, int maxPatientsNumber) {
        List<Point> subComparisonDatasetList = new ArrayList<>();
        String keyI = 0 + "_" + proteinKey;
        String keyII = 1 + "_" + proteinKey;
        String keyIII = 2 + "_" + proteinKey;
        String keyIV = 3 + "_" + proteinKey;
        String keyV = 4 + "_" + proteinKey;

        String key = "";
        Set<Integer> dsIdSetUp = new LinkedHashSet<>();
        Set<Integer> dsIdSetEqual = new LinkedHashSet<>();
        Set<Integer> dsIdSetLow = new LinkedHashSet<>();

        if (comparison.getQuantComparisonProteinMap().containsKey(keyI)) {
            key = keyI;

        } else if (comparison.getQuantComparisonProteinMap().containsKey(keyII)) {
            key = keyII;

        } else if (comparison.getQuantComparisonProteinMap().containsKey(keyIII)) {
            key = keyIII;

        } else if (comparison.getQuantComparisonProteinMap().containsKey(keyIV)) {
            key = keyIV;

        } else if (comparison.getQuantComparisonProteinMap().containsKey(keyV)) {
            key = keyV;

        }

        int numPatients;
        if (!key.equalsIgnoreCase("")) {
            QuantComparisonProtein prot = comparison.getQuantComparisonProteinMap().get(key);

            dsIdSetUp.addAll((prot.getPatientsNumToDSIDMap().get("up")));
            dsIdSetEqual.addAll((prot.getPatientsNumToDSIDMap().get("equal")));
            dsIdSetLow.addAll((prot.getPatientsNumToDSIDMap().get("down")));
        }

        for (QuantDataset ds : comparison.getDatasetMap().values()) {
            int absTrend = 6;
            int trend = 0;
            numPatients = ds.getDiseaseMainGroup1Number() + ds.getDiseaseMainGroup2Number();
            if (dsIdSetUp.contains(ds.getQuantDatasetIndex())) {
                absTrend = 100;
                trend = 0;

            } else if (dsIdSetEqual.contains(ds.getQuantDatasetIndex())) {
                absTrend = 0;
                trend = 2;
            } else if (dsIdSetLow.contains(ds.getQuantDatasetIndex())) {
                absTrend = -100;
                trend = 4;
            }
            if (absTrend != 6) {
                Point p = new Point(comparisonIndex, absTrend);
                subComparisonDatasetList.add(p);
                TrendSymbol symbol = new TrendSymbol(trend);
                double scale = Math.max(this.scaleValues(numPatients, maxPatientsNumber, 100, 0), 1);
                int w = (int) (12 * scale);

                String[] gr = comparison.getComparisonFullName().replace("__" + comparison.getDiseaseCategory(), "").split(" / ");
                String updatedHeader = ("Numerator: " + gr[0] + "<br/>Denominator: " + gr[1] + "<br/>Disease: " + comparison.getDiseaseCategory());
                String tooltip = updatedHeader + "<br/>#Patients :" + numPatients;//+ "<br/>Datasets included: " + dsNumber;

                symbol.setDescription(tooltip);
                symbol.setWidth(12, Unit.PIXELS);
                symbol.setHeight(12, Unit.PIXELS);
                symbol.addParam("resize", w);
                symbol.addParam("dsKey", ds.getQuantDatasetIndex());
                symbol.addParam("comparison", comparison);
//                symbol.setScale(scale,numPatients);
                if (!subComparisonStudiesMap.containsKey(comparisonIndex + "_" + absTrend)) {
                    subComparisonStudiesMap.put(comparisonIndex + "_" + absTrend, new HashSet<>());

                }
                Set<TrendSymbol> set = subComparisonStudiesMap.get(comparisonIndex + "_" + absTrend);
                set.add(symbol);
                subComparisonStudiesMap.put(comparisonIndex + "_" + absTrend, set);

            }

        }
        return subComparisonDatasetList;
    }

    private JFreeChart generateLineChart() {

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.GRAY);
        renderer.setUseOutlinePaint(false);

        renderer.setSeriesShape(0, ShapeUtilities.createDiamond(4));
        renderer.setSeriesShapesVisible(0, false);

        renderer.setSeriesShape(1, ShapeUtilities.createDiamond(4));
        renderer.setSeriesShapesVisible(1, true);
        renderer.setSeriesLinesVisible(1, false);
        renderer.setSeriesPaint(1, new Color(255, 255, 255, 0));

        renderer.setSeriesStroke(0, new BasicStroke(
                1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1.0f, new float[]{10.0f, 6.0f}, 0.0f
        ));

        XYPlot xyplot = new XYPlot(dataset, xAxis, yAxis, renderer) {

            @Override
            public Paint getRangeGridlinePaint() {
                if (gridcounter == 239) {
                    gridcounter = 0;
                }
                if (gridcounter == 20 || (gridcounter == 120) || (gridcounter == 220)) {
                    gridcounter++;
                    return super.getRangeGridlinePaint(); //To change body of generated methods, choose Tools | Templates.
                }
                if (custTrend != -1) {

                    if (custTrend == 0) {
                        if ((gridcounter >= 10 && gridcounter <= 19) || (gridcounter >= 21 && gridcounter <= 29)) {
                            gridcounter++;
                            return customizedUserDataColor[custTrend];
                        }

                    }
                    if (custTrend == 2) {
                        if ((gridcounter >= 110 && gridcounter <= 119) || (gridcounter >= 121 && gridcounter <= 129)) {
                            gridcounter++;
                            return customizedUserDataColor[custTrend];
                        }

                    }
                    if (custTrend == 4) {
                        if ((gridcounter >= 210 && gridcounter <= 219) || (gridcounter >= 221 && gridcounter <= 229)) {
                            gridcounter++;
                            return customizedUserDataColor[custTrend];
                        }

                    }

                }
                gridcounter++;
                return Color.WHITE;

            }

            private BasicStroke highlitedLineStrok = new BasicStroke(1f);

            @Override
            public Stroke getRangeGridlineStroke() {
                if (gridcounterII == 239) {
                    gridcounterII = 0;
                    highlitedLineStrok = new BasicStroke(4f);
                }

                if (custTrend != -1) {
                    if (custTrend == 0) {
                        if ((gridcounterII >= 10 && gridcounterII <= 19) || (gridcounterII >= 21 && gridcounterII <= 29)) {
                            gridcounterII++;
                            return highlitedLineStrok;
                        }

                    }
                    if (custTrend == 2) {
                        if ((gridcounterII >= 110 && gridcounterII <= 119) || (gridcounterII >= 121 && gridcounterII <= 129)) {
                            gridcounterII++;
                            return highlitedLineStrok;
                        }

                    }
                    if (custTrend == 4) {
                        if ((gridcounterII >= 210 && gridcounterII <= 219) || (gridcounterII >= 221 && gridcounterII <= 229)) {
                            gridcounterII++;
                            return highlitedLineStrok;
                        }

                    }

                }
                gridcounterII++;

                return super.getRangeGridlineStroke();
            }

//            @Override
//            public void drawRangeTickBands(Graphics2D g2, Rectangle2D dataArea, List ticks) {
//
//                if (custTrend == -1) {
//                    super.drawRangeTickBands(g2, dataArea, ticks);
//                    return;
//
//                }
//                int counterI = 0;
//                List updatedTicksList = new ArrayList();
//                for (Object tick : ticks) {
//
//                    if (tick.toString().equalsIgnoreCase(tickLabels[custTrend])) {
//                        for (int i = counterI - 1; i > counterI - 10; i--) {
//                            updatedTicksList.add(ticks.get(i));
//                        }
//                        updatedTicksList.add(tick);
//                        for (int i = counterI + 1; i < counterI + 11; i++) {
//                            updatedTicksList.add(ticks.get(i));
//                        }
//                    }
//                    counterI++;
//                }
//                Rectangle2D up;
//                if (custTrend == 4) {
//                    up = new Rectangle((int) dataArea.getX(), (int) dataArea.getY(), (int) dataArea.getWidth(), (int) dataArea.getHeight());
//
//                } else if (custTrend == 2) {
//                    up = new Rectangle((int) dataArea.getX(), (int) dataArea.getY(), (int) dataArea.getWidth(), (int) dataArea.getHeight());//                    
////
//                } else {
//                    up = new Rectangle((int) dataArea.getX(), (int) dataArea.getY(), (int) dataArea.getWidth(), (int) dataArea.getHeight());
//                }
//
//                super.drawRangeTickBands(g2, up, updatedTicksList); //To change body of generated methods, choose Tools | Templates.
//            }
        };
//        if (custTrend != -1) {
//            if (custTrend == 4) {
//                xyplot.setRangeTickBandPaint(customizedUserDataColor[4]);
//
//            } else if (custTrend == 0) {
//                xyplot.setRangeTickBandPaint(customizedUserDataColor[0]);
//            } else if (custTrend == 2) {
//                xyplot.setRangeTickBandPaint(customizedUserDataColor[2]);//TickBandPaint(customizedUserDataColor[2]);
//            }
//
//        } else {
//            xyplot.setRangeTickBandPaint(Color.WHITE);
//        }
        xyplot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        JFreeChart jFreeChart = new JFreeChart(null, new Font("Helvetica Neue", Font.PLAIN, 18), xyplot, true);
        jFreeChart.getXYPlot().getDomainAxis().setVisible(true);
        jFreeChart.getXYPlot().getRangeAxis().setVisible(true);
        jFreeChart.getXYPlot().setOutlineVisible(false);
        jFreeChart.getXYPlot().setRangeGridlinesVisible(true);
        jFreeChart.getXYPlot().setDomainGridlinesVisible(true);

        jFreeChart.setBackgroundPaint(Color.WHITE);
        final XYPlot plot = jFreeChart.getXYPlot();

        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);

        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setOutlinePaint(Color.GRAY);
        jFreeChart.setBorderVisible(false);
        jFreeChart.setPadding(new RectangleInsets(0, 0, 0, 0));
        LegendTitle legend = jFreeChart.getLegend();
        legend.setVisible(false);

        return jFreeChart;
    }

    /**
     * Generate JFree chart object that is used to generate the thumb icon used
     * for updating the left button icon.
     *
     * @return lineChart line chart object.
     */
    public JFreeChart generateThumbChart() {
        lineChart.getXYPlot().getDomainAxis().setVisible(false);
        lineChart.getXYPlot().getRangeAxis().setVisible(false);
        lineChart.getXYPlot().setOutlineVisible(true);
        lineChart.setPadding(new RectangleInsets(10, 5, 10, 5));
        lineChart.getXYPlot().getRenderer().setSeriesPaint(0, Color.decode("#1d69b4"));
        lineChart.getXYPlot().getRenderer().setSeriesStroke(0, new BasicStroke(5));
        return lineChart;
    }

    /**
     * Convert JFree chart object into decoded64 String image.
     *
     * @return String of Decoded64 String that is used as URL for the generated
     * image.
     */
    private String getChartImage(JFreeChart chart, ChartRenderingInfo chartRenderingInfo, int width, int height) {
        if (chart == null) {
            return null;
        }
        String base64 = "";
        try {
            bufferedChartImg = chart.createBufferedImage((int) width, (int) height, chartRenderingInfo);
            base64 = "data:image/png;base64," + Base64.encodeBase64String(ChartUtilities.encodeAsPNG(bufferedChartImg));

        } catch (IOException ex) {
            System.err.println("at error " + this.getClass() + " line 536 " + ex.getLocalizedMessage());
        }
        return base64;
    }

    /**
     * Initialize the main line chart components based on data from JFreeChart.
     *
     * @param comparisonList Set of comparison objects.
     */
    private void initLayoutComponents(Set<QuantDiseaseGroupsComparison> comparisonList) {
        chartComponentsLayout.removeAllComponents();
        comparisonTrendMap.clear();
        this.symbolMap.clear();
        for (int i = 0; i < chartRenderingInfo.getEntityCollection().getEntityCount(); i++) {
            final ChartEntity entity = chartRenderingInfo.getEntityCollection().getEntity(i);
            if (entity instanceof XYItemEntity) {
                XYItemEntity comparisonPoint = ((XYItemEntity) entity);
                if (comparisonPoint.getSeriesIndex() == 1) {
                    continue;
                }
                String[] arr = comparisonPoint.getShapeCoords().split(",");
                int xSer = Integer.valueOf(arr[6]);
                int ySer = Integer.valueOf(arr[1]);
                int trend = 6;
                double doubleTrend = (Double) comparisonPoint.getDataset().getY(0, comparisonPoint.getItem());
                double comparisonIndex = ((Double) comparisonPoint.getDataset().getX(0, comparisonPoint.getItem()));

                QuantDiseaseGroupsComparison gc = (QuantDiseaseGroupsComparison) comparisonList.toArray()[(int) comparisonIndex];
                String keyI = 0 + "_" + proteinKey;
                String keyII = 1 + "_" + proteinKey;
                String keyIII = 2 + "_" + proteinKey;
                String keyIV = 3 + "_" + proteinKey;
                String keyV = 4 + "_" + proteinKey;

                if (doubleTrend == 0.0) {
                    if (gc.getQuantComparisonProteinMap().containsKey(keyI) || gc.getQuantComparisonProteinMap().containsKey(keyII) || gc.getQuantComparisonProteinMap().containsKey(keyIII) || gc.getQuantComparisonProteinMap().containsKey(keyIV) || gc.getQuantComparisonProteinMap().containsKey(keyV)) {
                        trend = 2;
                    } else {
                        trend = 6;
                    }

                } else if (doubleTrend == 100.0) {
                    trend = 0;
                } else if (doubleTrend < 100 && doubleTrend > 0.0) {
                    trend = 1;
                } else if (doubleTrend < 0 && doubleTrend > -100.0) {
                    trend = 3;
                } else if (doubleTrend == -100.0) {
                    trend = 4;
                }

                TrendSymbol square = new TrendSymbol(trend);
                square.setWidth(12, Unit.PIXELS);
                square.setHeight(12, Unit.PIXELS);
                ComponentPosition position = new ComponentPosition();
                position.setCSSString("left: " + (xSer - 1) + "px; top: " + (ySer - 1) + "px;");
                square.addParam("postion", position);
                square.addParam("type", "comparison");

                chartComponentsLayout.addComponent(square, "left: " + (xSer - 3) + "px; top: " + (ySer) + "px;");
                comparisonTrendMap.put(gc.getComparisonHeader(), doubleTrend);
                this.symbolMap.put(doubleTrend + "," + comparisonIndex, square);
                square.addParam("comparison", gc);
                square.addParam("dsKey", -100);
                if (trend == 6) {
                    square.addParam("comparison", null);
                } else {
                    square.addStyleName("pointer");
                }
                String[] gr = gc.getComparisonFullName().replace("__" + gc.getDiseaseCategory(), "").split(" / ");
                String updatedHeader = ("Numerator: " + gr[0] + "<br/>Denominator: " + gr[1] + "<br/>Disease: " + gc.getDiseaseCategory());
                String tooltip = updatedHeader + "<br/>Overall trend: " + tooltipsIcon[trend];//+ "<br/>Datasets included: " + dsNumber;
                square.setDescription(tooltip);
                square.addLayoutClickListener(StudiesLineChart.this);
            }
        }

        for (int i = 0; i < chartRenderingInfo.getEntityCollection().getEntityCount(); i++) {
            final ChartEntity entity = chartRenderingInfo.getEntityCollection().getEntity(i);
            if (entity instanceof XYItemEntity) {
                XYItemEntity comparisonPoint = ((XYItemEntity) entity);
                if (comparisonPoint.getSeriesIndex() == 0) {
                    continue;
                }
                String[] arr = comparisonPoint.getShapeCoords().split(",");
                int xSer = Integer.valueOf(arr[6]);
                int ySer = Integer.valueOf(arr[1]);
                double doubleTrend = (Double) comparisonPoint.getDataset().getY(1, comparisonPoint.getItem());
                double comparisonIndex = ((Double) comparisonPoint.getDataset().getX(1, comparisonPoint.getItem()));

                if (subComparisonStudiesMap.containsKey((int) comparisonIndex + "_" + (int) doubleTrend)) {
                    int factor = 0;
                    for (TrendSymbol symbol : subComparisonStudiesMap.get((int) comparisonIndex + "_" + (int) doubleTrend)) {

                        int adju = (int) ((int) symbol.getParam("resize") - 12) / 2;
                        symbol.addParam("resizePosition", "left: " + (xSer - 2 - adju) + "px; top: " + (ySer + 4 + factor - adju) + "px;");
                        symbol.addParam("position", "left: " + (xSer - 2) + "px; top: " + (ySer + 4 + factor) + "px;");
                        symbol.addParam("type", "dataset");
                        symbol.addLayoutClickListener(StudiesLineChart.this);
                        chartComponentsLayout.addComponent(symbol, "left: " + (xSer - 2) + "px; top: " + (ySer + 4 + factor) + "px;");
                        symbol.setVisible(false);
                        factor += 6;
                    }
                }
            }
        }
    }

    /**
     * Converts the value from linear scale to log scale, the log scale numbers
     * are limited by the range of the type float, the linear scale numbers can
     * be any double value.
     *
     * @param linearValue the value to be converted to log scale.
     * @param max the biggest number of the data.
     * @param upperLimit The upper limit of the desired scale.
     * @param lowerLimit The lower limit for the scale.
     * @return the value in log scale.
     */
    public final double scaleValues(double linearValue, int max, double upperLimit, double lowerLimit) {
        double logMax = (Math.log(max) / Math.log(2));
        double logValue = (Math.log(linearValue) / Math.log(2));
        logValue = (logValue * 2 / logMax) + lowerLimit;
        return logValue;
    }

    /**
     * Set show all datasets or show the overall comparisons trend.
     *
     * @param showDetailedDatasets show every dataset included in each
     * comparison.
     */
    public void setViewDetailedDatasets(boolean showDetailedDatasets) {
        subComparisonStudiesMap.values().stream().forEach((dsSet) -> {
            dsSet.stream().forEach((ds) -> {
                ds.setVisible(showDetailedDatasets);
            });
        });

        symbolMap.values().stream().forEach((comp) -> {
            if (!comp.getStyleName().contains("graydiamond")) {
                comp.setVisible(!showDetailedDatasets);
            }
        });

    }

    /**
     * Resize datasets symbol based on number of patients in each comparison.
     *
     * @param resizeDatasetsSymbols Resize datasets based on number of patients.
     */
    public void setResizeDetailedDatasets(boolean resizeDatasetsSymbols) {
        if (resizeDatasetsSymbols) {
            subComparisonStudiesMap.values().stream().forEach((dsSet) -> {
                dsSet.stream().forEach((ds) -> {
                    String resizePostion = ds.getParam("resizePosition").toString();
                    ds.setWidth((int) ds.getParam("resize"), Unit.PIXELS);
                    ds.setHeight((int) ds.getParam("resize"), Unit.PIXELS);
                    ds.detach();
                    chartComponentsLayout.addComponent(ds, resizePostion);
                });
            });

        } else {
            subComparisonStudiesMap.values().stream().forEach((dsSet) -> {
                dsSet.stream().forEach((ds) -> {
                    String resizePostion = ds.getParam("position").toString();
                    ds.setWidth(12, Unit.PIXELS);
                    ds.setHeight(12, Unit.PIXELS);
                    ds.detach();
                    chartComponentsLayout.addComponent(ds, resizePostion);
                });
            });

        }

    }

    /**
     * Get the sorted comparisons set based on the overall trend order.
     *
     * @return orederedComparisonSet sorted list for the comparisons on overall
     * trend order.
     */
    private Set<QuantDiseaseGroupsComparison> getOrderStudiesByTrend() {
        TreeMap<AlphanumComparator, QuantDiseaseGroupsComparison> orderedCompProteins = new TreeMap<>();
        LinkedHashSet<QuantDiseaseGroupsComparison> orederedComparisonSet = new LinkedHashSet<>();

        selectedComparisonList.stream().filter((cp) -> !(!comparisonTrendMap.containsKey(cp.getComparisonHeader()))).forEach((cp) -> {
            double sigTrend = comparisonTrendMap.get(cp.getComparisonHeader());
            if (sigTrend == -1) {
                AlphanumComparator key = new AlphanumComparator((102) + "-z" + cp.getComparisonHeader());
                orderedCompProteins.put(key, cp);
            } else {
                AlphanumComparator key = new AlphanumComparator((sigTrend + 100) + "-" + cp.getComparisonHeader());
                orderedCompProteins.put(key, cp);
            }
        });
        orderedCompProteins.keySet().stream().map((cpHeader) -> orderedCompProteins.get(cpHeader)).forEach((cp) -> {
            orederedComparisonSet.add(cp);
        });
        return orederedComparisonSet;
    }

    /**
     * Set the order of comparisons based on the overall trend order or by
     * default order.
     *
     * @param trendOrder sort the comparisons on overall trend order.
     */
    public void setSortDatasetOnTrendOrder(boolean trendOrder) {
        if (trendOrder) {
            this.updateDataset(orderedComparisonList, proteinKey);
        } else {
            this.updateDataset(selectedComparisonList, proteinKey);

        }

        lineChart.getXYPlot().setDataset(dataset);
        lineChart.getXYPlot().setDomainAxis((ValueAxis) xAxis);
        lineChart.getXYPlot().setRangeAxis((ValueAxis) yAxis);
        if (trendOrder) {
            rePaintChart(orderedComparisonList);
        } else {
            rePaintChart(selectedComparisonList);
        }

    }

    /**
     * Select comparison or dataset from the line chart layout.
     *
     * @param event selection event
     */
    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        TrendSymbol symbol = (TrendSymbol) event.getComponent();
        QuantDiseaseGroupsComparison comparison = (QuantDiseaseGroupsComparison) symbol.getParam("comparison");
        if (comparison == null) {
            return;
        }
        int dsKey = (int) symbol.getParam("dsKey");
        select(comparison, dsKey);

    }

    /**
     * Select comparison or dataset from the line chart.
     *
     * @param comparison Comparison object.
     * @param dsId Dataset index (-100 if the selected is comparison).
     */
    public abstract void select(QuantDiseaseGroupsComparison comparison, int dsId);

}
