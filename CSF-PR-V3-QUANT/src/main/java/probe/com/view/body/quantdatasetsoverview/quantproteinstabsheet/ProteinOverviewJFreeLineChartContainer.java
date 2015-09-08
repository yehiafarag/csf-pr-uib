package probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet;

import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.kmeansclustering.ProteinKMeansClusterLayout;
import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
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
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
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
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.text.TextUtilities;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.TextAnchor;
import org.jfree.util.ShapeUtilities;
import org.vaadin.marcus.MouseEvents;
import probe.com.handlers.CSFPRHandler;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.DiseaseGroupsComparisonsProteinLayout;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
import probe.com.selectionmanager.DatasetExploringCentralSelectionManager;
import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.kmeansclustering.KMeansClusteringPopupPanel;
import probe.com.view.core.CustomExternalLink;
import probe.com.view.core.jfreeutil.SquaredDot;

/**
 * Interactive JfreeChart
 *
 * @author Yehia Farag
 */
public class ProteinOverviewJFreeLineChartContainer extends HorizontalLayout {

    private final int height;
    private String defaultLineChartImgUrl = "";
    private String orderedLineChartImg = "";
    private final ChartRenderingInfo defaultLineChartRenderingInfo = new ChartRenderingInfo();
    private final ChartRenderingInfo orderedLineChartRenderingInfo = new ChartRenderingInfo();
    private String thumbChart = "";
    private final JFreeChart defaultChart;
    private JFreeChart orderedChart;
    private final int selectedComparisonListSize;
    private final VerticalLayout bottomLiftSide;
    private final int width;
    private final OptionGroup orederingOptionGroup = new OptionGroup();
    private final ProteinStudiesComparisonsContainerLayout studiesScatterChartsLayout;
    private final AbsoluteLayout lineChartContainer;
    private final String teststyle;
    private final Page.Styles styles = Page.getCurrent().getStyles();
    private final DatasetExploringCentralSelectionManager datasetExploringCentralSelectionManager;
    private final CSFPRHandler mainHandler;

    /**
     *
     * @return
     */
    public String getThumbChart() {
        if (thumbChart.equalsIgnoreCase("")) {
            resetThumbChart(defaultChart, false);
            thumbChart = saveToFile(defaultChart, (selectedComparisonListSize * 15), 35, null);
            resetThumbChart(defaultChart, true);
        }
        return thumbChart;
    }

    /**
     *
     * @return
     */
    public OptionGroup getOrederingOptionGroup() {
        return orederingOptionGroup;
    }
    private final LayoutEvents.LayoutClickListener chartListener;

    /**
     *
     * @param datasetExploringCentralSelectionManager
     * @param comparisonProteins
     * @param selectedComparisonList
     * @param widthValue
     * @param proteinName
     * @param searchingMode
     */
    public ProteinOverviewJFreeLineChartContainer(DatasetExploringCentralSelectionManager datasetExploringCentralSelectionManager, final DiseaseGroupsComparisonsProteinLayout[] comparisonProteins, final Set<QuantDiseaseGroupsComparison> selectedComparisonList, int widthValue, final String proteinName, final String proteinAccession, boolean searchingMode, final String proteinKey, CSFPRHandler mainHandler) {

        this.setStyleName(Reindeer.LAYOUT_WHITE);
        this.setSpacing(false);
        this.setHeightUndefined();
        this.setWidth(widthValue + "px");
        this.datasetExploringCentralSelectionManager = datasetExploringCentralSelectionManager;
        this.mainHandler = mainHandler;

        height = 400;
        widthValue = widthValue - 300;
        width = ((widthValue) / 2);
        this.selectedComparisonListSize = selectedComparisonList.size();

        GridLayout protInfoLayout = new GridLayout(2, 7);
        protInfoLayout.setMargin(false);
        protInfoLayout.setSpacing(true);

        Label accTitle = new Label("<b>Protein Accession</b>");
        accTitle.setContentMode(ContentMode.HTML);
        protInfoLayout.addComponent(accTitle, 0, 0);
        CustomExternalLink acc = new CustomExternalLink(proteinAccession.toUpperCase(), "http://www.uniprot.org/uniprot/" + proteinAccession.toUpperCase());
        protInfoLayout.addComponent(acc, 0, 1);
        Label nameTitle = new Label("<b>Protein Name</b>");
        nameTitle.setContentMode(ContentMode.HTML);
        protInfoLayout.addComponent(nameTitle, 0, 2);

        Label nameValue = new Label("<textarea rows='4' cols='20' readonly>" + proteinName + "</textarea>");
        nameValue.setContentMode(ContentMode.HTML);
        nameValue.setStyleName("valuelabel");
        nameValue.setReadOnly(true);
        protInfoLayout.addComponent(nameValue, 0, 3);
        protInfoLayout.setWidth("150px");
//        bodyLayout.addComponent(protInfoLayout);

        Label compOrederTitle = new Label("<b>Comparisons Order</b>");
        compOrederTitle.setContentMode(ContentMode.HTML);
        protInfoLayout.addComponent(compOrederTitle, 0, 4);
        protInfoLayout.setWidth("250px");

        HorizontalLayout clusterKMeanLayout = new HorizontalLayout();
        clusterKMeanLayout.setMargin(new MarginInfo(true, false, false, false));
        clusterKMeanLayout.setHeight("100px");
        clusterKMeanLayout.setStyleName(Reindeer.LAYOUT_WHITE);

        Button clusterKMeanBtn = new Button("Protein K-Means Clustering ");
        clusterKMeanBtn.setStyleName(Reindeer.BUTTON_LINK);
        clusterKMeanLayout.addComponent(clusterKMeanBtn);
        clusterKMeanLayout.setComponentAlignment(clusterKMeanBtn, Alignment.MIDDLE_CENTER);
        clusterKMeanBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                runKmeansClustering(proteinKey, proteinName, proteinAccession, selectedComparisonList);
            }
        });

        protInfoLayout.addComponent(clusterKMeanLayout, 0, 6);

        VerticalLayout leftSideLayout = new VerticalLayout();
        leftSideLayout.setWidthUndefined();
        leftSideLayout.setHeightUndefined();
        leftSideLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        leftSideLayout.setSpacing(true);
        leftSideLayout.setMargin(new MarginInfo(true, true, false, true));
        this.addComponent(leftSideLayout);

        HorizontalLayout topLeftSideLayout = new HorizontalLayout();
        topLeftSideLayout.setWidthUndefined();
        topLeftSideLayout.setHeightUndefined();
        topLeftSideLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        topLeftSideLayout.setSpacing(false);
        topLeftSideLayout.setMargin(new MarginInfo(true, false, false, false));
        leftSideLayout.addComponent(topLeftSideLayout);
        topLeftSideLayout.addComponent(protInfoLayout);

        bottomLiftSide = new VerticalLayout();
        bottomLiftSide.setWidth("100%");
        bottomLiftSide.setHeight(520 + "px");
        bottomLiftSide.setStyleName(Reindeer.LAYOUT_WHITE);
        leftSideLayout.addComponent(bottomLiftSide);

        VerticalLayout rightSideLayout = new VerticalLayout();
        rightSideLayout.setWidth("100%");
        rightSideLayout.setHeightUndefined();
        rightSideLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        rightSideLayout.setSpacing(true);
        rightSideLayout.setMargin(new MarginInfo(true, false, false, true));
        this.addComponent(rightSideLayout);
        lineChartContainer = new AbsoluteLayout();

        //init leftside components - linechart 
        defaultChart = generateLineChart(comparisonProteins, selectedComparisonList);//, (width - 100), height, defaultLineChartRenderingInfo);
        topLeftSideLayout.addComponent(lineChartContainer);
        lineChartContainer.setWidth((width - 100) + "px");
        lineChartContainer.setHeight(height + "px");

        teststyle = proteinName.replace(" ", "_").replace(")", "_").replace("(", "_").replace(";", "_").toLowerCase() + "linechart";
        styles.add("." + teststyle + " {  background-image: url(" + defaultLineChartImgUrl + " );background-position:center; background-repeat: no-repeat; }");
        lineChartContainer.setStyleName(teststyle);
        chartListener = new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                Component c = event.getClickedComponent();
                if (c instanceof SquaredDot) {
                    QuantDiseaseGroupsComparison gc = (QuantDiseaseGroupsComparison) ((SquaredDot) c).getParam("GroupsComparison");
                    studiesScatterChartsLayout.highlightComparison(gc, true);
                } else {
                    studiesScatterChartsLayout.highlightComparison(null, false);
                }

            }
        };
        lineChartContainer.addLayoutClickListener(chartListener);

        orederingOptionGroup.setWidth("250px");
        orederingOptionGroup.setNullSelectionAllowed(false); // user can not 'unselect'
        orederingOptionGroup.setMultiSelect(false);
        orederingOptionGroup.addItem("Default order");
        orederingOptionGroup.addItem("Trend order");
        orederingOptionGroup.setValue("Default order");
        orederingOptionGroup.addStyleName("horizontal");
        orederingOptionGroup.addValueChangeListener(new Property.ValueChangeListener() {
            private DiseaseGroupsComparisonsProteinLayout[] ordComparisonProteins;

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (orederingOptionGroup.getValue().toString().equalsIgnoreCase("Default order")) {
                    styles.add("." + teststyle + " {  background-image: url(" + defaultLineChartImgUrl + " );background-position:center; background-repeat: no-repeat; }");
                    lineChartContainer.setStyleName(teststyle);
                    studiesScatterChartsLayout.orderComparisons(comparisonProteins);

                } else {
                    if (orderedLineChartImg.equalsIgnoreCase("")) {
                        //order the comparisons and proteins
                        TreeMap<String, DiseaseGroupsComparisonsProteinLayout> orderedCompProteins = new TreeMap<String, DiseaseGroupsComparisonsProteinLayout>();
                        LinkedHashSet<QuantDiseaseGroupsComparison> orederedComparisonSet = new LinkedHashSet<QuantDiseaseGroupsComparison>();
                        for (DiseaseGroupsComparisonsProteinLayout cp : comparisonProteins) {
                            if (cp == null) {
                                continue;
                            }
                            if (cp.getCellValue() < 0 && cp.getCellValue() > -1) {
                                orderedCompProteins.put((cp.getCellValue() - 1) + "-" + cp.getComparison().getComparisonHeader(), cp);
                            } else {
                                orderedCompProteins.put((cp.getCellValue()) + "-" + cp.getComparison().getComparisonHeader(), cp);
                            }
                        }
                        ordComparisonProteins = new DiseaseGroupsComparisonsProteinLayout[orderedCompProteins.size()];
                        int i = 0;
                        for (DiseaseGroupsComparisonsProteinLayout cp : orderedCompProteins.values()) {
                            ordComparisonProteins[i] = cp;
                            orederedComparisonSet.add(cp.getComparison());
                            i++;
                        }
                        for (QuantDiseaseGroupsComparison gv : selectedComparisonList) {
                            if (!orederedComparisonSet.contains(gv)) {
                                orederedComparisonSet.add(gv);
                            }

                        }

                        orderedChart = generateLineChart(ordComparisonProteins, orederedComparisonSet);//, (width - 100), height, orderedLineChartRenderingInfo);
                        orderedLineChartImg = generateChartImage(orderedChart, (width - 100), height, orderedLineChartRenderingInfo);
//                        studiesScatterChartsLayout.orderComparisons(ordComparisonProteins);
                    }
                    styles.add("." + teststyle + " {  background-image: url(" + orderedLineChartImg + " );background-position:center; background-repeat: no-repeat; }");
                    lineChartContainer.setStyleName(teststyle);
                    studiesScatterChartsLayout.orderComparisons(ordComparisonProteins);
                }
            }
        });
        protInfoLayout.addComponent(orederingOptionGroup, 0, 5);

        VerticalLayout dsInfoPopupContainerLayout = new VerticalLayout();
        dsInfoPopupContainerLayout.setWidth((width - 100) + "px");
        dsInfoPopupContainerLayout.setHeight(400 + "px");
        dsInfoPopupContainerLayout.setStyleName(Reindeer.LAYOUT_WHITE);

//        init rightside components 
        studiesScatterChartsLayout = new ProteinStudiesComparisonsContainerLayout(comparisonProteins, selectedComparisonList, datasetExploringCentralSelectionManager, width, searchingMode);
        rightSideLayout.addComponent(studiesScatterChartsLayout);
        studiesScatterChartsLayout.setWidth(width * 2 + "px");
        this.setExpandRatio(leftSideLayout, 1.5f);
        this.setExpandRatio(rightSideLayout, 1.4f);
    }

    private DiseaseGroupsComparisonsProteinLayout[] inUseComparisonProteins;

    private JFreeChart generateLineChart(DiseaseGroupsComparisonsProteinLayout[] comparisonProteins, Set<QuantDiseaseGroupsComparison> selectedComparisonList) {
        int upcounter = 0;
        int notcounter = 0;
        int downcounter = 0;
        int counter = 0;

        for (DiseaseGroupsComparisonsProteinLayout cp : comparisonProteins) {
            if (cp == null) {
                continue;
            }

            if (cp.getCellValue() > 0) {
                upcounter++;
            } else if (cp.getCellValue() == 0) {
                notcounter++;
            } else if (cp.getCellValue() < 0) {
                downcounter++;
            }
            counter++;

        }
        if (counter == 1) {
            orederingOptionGroup.setEnabled(false);

        }
        inUseComparisonProteins = new DiseaseGroupsComparisonsProteinLayout[counter];

        DefaultXYDataset dataset = new DefaultXYDataset();

        double[][] linevalues = new double[2][counter];

        double[] xLineValues = new double[counter];
        double[] yLineValues = new double[counter];

        double[][] upvalues = new double[2][upcounter];
        double[] xUpValues = new double[upcounter];
        double[] yUpValues = new double[upcounter];

        double[][] notvalues = new double[2][notcounter];
        double[] xNotValues = new double[notcounter];
        double[] yNotValues = new double[notcounter];

        double[][] downvalues = new double[2][downcounter];
        double[] xDownValues = new double[downcounter];
        double[] yDownValues = new double[downcounter];

        int upIndex = 0;
        int notIndex = 0;
        int downIndex = 0;

        int compIndex = 0;
        int comparisonIndexer = 0;
        for (DiseaseGroupsComparisonsProteinLayout cp : comparisonProteins) {
            if (cp == null) {
                comparisonIndexer++;
                continue;
            } else {
                inUseComparisonProteins[compIndex] = cp;
                xLineValues[compIndex] = comparisonIndexer;

                if (cp.getTrindCategory() == 4) {
                    yLineValues[compIndex] = 4d;
                    xUpValues[upIndex] = comparisonIndexer;
                    yUpValues[upIndex] = 4d;
                    upIndex++;
                } else if (cp.getTrindCategory() == 3) {
                    xUpValues[upIndex] = comparisonIndexer;
                    yUpValues[upIndex] = 3d;
                    upIndex++;
                    yLineValues[compIndex] = 3d;
                } else if (cp.getTrindCategory() == 2) {
                    yLineValues[compIndex] = 2d;
                    xNotValues[notIndex] = comparisonIndexer;
                    yNotValues[notIndex] = 2d;
                    notIndex++;
                } else if (cp.getTrindCategory() == 1) {
                    yLineValues[compIndex] = 1d;
                    xDownValues[downIndex] = comparisonIndexer;
                    yDownValues[downIndex] = 1d;
                    downIndex++;
                } else if (cp.getTrindCategory() == 0) {
                    yLineValues[compIndex] = 0d;
                    xDownValues[downIndex] = comparisonIndexer;
                    yDownValues[downIndex] = 0d;
                    downIndex++;
                }

            }
            compIndex++;
            comparisonIndexer++;

        }

        linevalues[0] = xLineValues;
        linevalues[1] = yLineValues;
        upvalues[0] = xUpValues;
        upvalues[1] = yUpValues;
        notvalues[0] = xNotValues;
        notvalues[1] = yNotValues;
        downvalues[0] = xDownValues;
        downvalues[1] = yDownValues;

        dataset.addSeries("line", linevalues);
        dataset.addSeries("up", upvalues);
        dataset.addSeries("not", notvalues);
        dataset.addSeries("down", downvalues);

        String[] xAxisLabels = new String[selectedComparisonList.size()];
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
        xAxis.setGridBandsVisible(false);
        yAxis.setGridBandsVisible(false);
        yAxis.setAxisLinePaint(Color.WHITE);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.GRAY);

        renderer.setSeriesPaint(1, new Color(204, 0, 0));
        renderer.setSeriesPaint(2, new Color(1, 141, 244));
        renderer.setSeriesPaint(3, new Color(80, 183, 71));

        Shape notRShape = ShapeUtilities.createDiamond(6f);
        Shape leftArr = ShapeUtilities.createDownTriangle(6f);
        Shape rightArr = ShapeUtilities.createUpTriangle(6f);
        renderer.setSeriesShape(3, leftArr);
        renderer.setSeriesShape(2, notRShape);
        renderer.setSeriesShape(1, rightArr);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesStroke(0, new BasicStroke(
                1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1.0f, new float[]{10.0f, 6.0f}, 0.0f
        ));

        renderer.setSeriesLinesVisible(1, false);
        renderer.setSeriesLinesVisible(2, false);
        renderer.setSeriesLinesVisible(3, false);

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

        return jFreeChart;
    }

    private String generateChartImage(JFreeChart jFreeChart, int w, int h, ChartRenderingInfo chartRenderingInfo) {
        String imgUrl = saveToFile(jFreeChart, w, h, chartRenderingInfo);
        for (int i = 0; i < chartRenderingInfo.getEntityCollection().getEntityCount(); i++) {
            final ChartEntity entity = chartRenderingInfo.getEntityCollection().getEntity(i);
            if (entity instanceof XYItemEntity && !((XYItemEntity) entity).getArea().toString().contains("java.awt.geom.Path2")) {
                String[] arr = ((XYItemEntity) entity).getShapeCoords().split(",");
                int xSer = Integer.valueOf(arr[10]);
                int ySer = Integer.valueOf(arr[11]);
                SquaredDot square = new SquaredDot();
                square.setWidth(20 + "px");
                square.setHeight(20 + "px");
                QuantDiseaseGroupsComparison gc = inUseComparisonProteins[((XYItemEntity) entity).getItem()].getComparison();
                square.setDescription(gc.getComparisonHeader());
                square.setParam("GroupsComparison", gc);
                lineChartContainer.addComponent(square, "left: " + (xSer - 7) + "px; top: " + (ySer - 10) + "px;");
                MouseEvents.MouseOverListener mouseOverListener = new MouseEvents.MouseOverListener() {
                    private final QuantDiseaseGroupsComparison gc = inUseComparisonProteins[((XYItemEntity) entity).getItem()].getComparison();

                    @Override
                    public void mouseOver() {
                        studiesScatterChartsLayout.highlightComparison(gc, false);
                    }
                };
                MouseEvents.MouseOutListener mouseOutListener = new MouseEvents.MouseOutListener() {
                    @Override
                    public void mouseOut() {
                        studiesScatterChartsLayout.highlightComparison(null, false);
                    }
                };
                final MouseEvents mouseEvents = MouseEvents.enableFor(square);
                mouseEvents.addMouseOutListener(mouseOutListener);
                mouseEvents.addMouseOverListener(mouseOverListener);
            }
        }
        return imgUrl;

    }

    private void resetThumbChart(JFreeChart chart, boolean isFullChart) {

        final XYPlot plot = chart.getXYPlot();
        SymbolAxis yAxis = (SymbolAxis) plot.getRangeAxis();
        SymbolAxis xAxis = (SymbolAxis) plot.getDomainAxis();

        plot.setDomainGridlinesVisible(isFullChart);
        plot.setRangeGridlinesVisible(isFullChart);
        yAxis.setVisible(isFullChart);
        xAxis.setVisible(isFullChart);
        plot.setOutlineVisible(isFullChart);

        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesShapesVisible(1, isFullChart);
        renderer.setSeriesShapesVisible(2, isFullChart);
        renderer.setSeriesShapesVisible(3, isFullChart);
        if (isFullChart) {
            renderer.setSeriesPaint(0, Color.GRAY);
            renderer.setSeriesPaint(1, new Color(204, 0, 0));
            renderer.setSeriesPaint(2, new Color(1, 141, 244));
            renderer.setSeriesPaint(3, new Color(80, 183, 71));
            renderer.setSeriesStroke(0, new BasicStroke(
                    1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                    1.0f, new float[]{10.0f, 6.0f}, 0.0f
            ));
        } else {
            renderer.setSeriesPaint(0, Color.DARK_GRAY);
            renderer.setSeriesStroke(0, null);
        }

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

    /**
     * redraw quant bar charts
     */
    public void redrawCharts() {

        if (orederingOptionGroup.getValue().toString().equalsIgnoreCase("Default order")) {
            if (defaultLineChartImgUrl.equalsIgnoreCase("")) {

                defaultLineChartImgUrl = this.generateChartImage(defaultChart, (width - 100), height, defaultLineChartRenderingInfo);
            }
            styles.add("." + teststyle + " {  background-image: url(" + defaultLineChartImgUrl + " );background-position:center; background-repeat: no-repeat; }");
        } else {
            if (orderedLineChartImg.equalsIgnoreCase("")) {
                orderedLineChartImg = this.generateChartImage(orderedChart, (width - 100), height, orderedLineChartRenderingInfo);
            }
            styles.add("." + teststyle + " {  background-image: url(" + orderedLineChartImg + " );background-position:center; background-repeat: no-repeat; }");
        }
        lineChartContainer.setStyleName(teststyle);
        studiesScatterChartsLayout.redrawCharts();

    }
    private  KMeansClusteringPopupPanel kMeansClusteringPanel;
    private void runKmeansClustering(String protKey, String proteinName, String proteinAccession, Set<QuantDiseaseGroupsComparison> selectedComparisonList) {
        if(kMeansClusteringPanel == null){
            Map<String, DiseaseGroupsComparisonsProteinLayout[]> protSelectionMap = datasetExploringCentralSelectionManager.getQuantProteinsLayoutSelectionMap();       
            kMeansClusteringPanel = new KMeansClusteringPopupPanel(datasetExploringCentralSelectionManager,mainHandler,protKey, proteinName, proteinAccession, protSelectionMap, selectedComparisonList);        
        }
        kMeansClusteringPanel.setVisible(true);
    }

}
