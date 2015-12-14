/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters;

import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.codec.binary.Base64;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.NumberTick;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.axis.Tick;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBubbleRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.text.TextUtilities;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
import probe.com.handlers.CSFPRHandler;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
import probe.com.model.beans.quant.QuantProtein;
import probe.com.selectionmanager.CSFFilter;
import probe.com.selectionmanager.QuantCentralManager;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.DiseaseGroupsComparisonsProteinLayout;
import probe.com.view.core.InfoPopupBtn;
import probe.com.view.core.jfreeutil.SquaredDot;

/*
 * @author Yehia Farag
 */
public class ComparisonsSelectionOverviewBubbleChart extends VerticalLayout implements CSFFilter, LayoutEvents.LayoutClickListener {

    private final String teststyle;
    private final Page.Styles styles = Page.getCurrent().getStyles();
    private String defaultImgURL = "";
    private final ChartRenderingInfo chartRenderingInfo = new ChartRenderingInfo();
    private final QuantCentralManager Quant_Central_Manager;
    private int width;
    private final int height;
    private final CSFPRHandler handler;
    private final VerticalLayout initialLayout;
    private JFreeChart chart;
    private final AbsoluteLayout chartLayout = new AbsoluteLayout();
    private final HorizontalLayout btnsLayout = new HorizontalLayout();
    private final Button resetBtn;
    private final Button exportPdfBtn;
    private final List<QuantProtein> searchQuantificationProtList;
    private boolean isNewImge = true;
    private byte imageData[];
    private final String[] tooltipLabels = new String[]{"( Low <img src='VAADIN/themes/dario-theme/img/greendot.png' alt='Low'>" + " )", "( Low <img src='VAADIN/themes/dario-theme/img/lgreendot.png' alt='Low'>" + " )", "( Stable <img src='VAADIN/themes/dario-theme/img/bluedot.png' alt='Stable'>" + " )", " ( High <img src='VAADIN/themes/dario-theme/img/lreddot.png' alt='High'>" + " )", " ( High <img src='VAADIN/themes/dario-theme/img/reddot.png' alt='High'>" + " )"};

    public void updateSize(int updatedWidth) {
        if (chart == null) {
            return;
        }
        defaultImgURL = saveToFile(chart, updatedWidth, height);
        this.setWidth(updatedWidth + "px");
        this.chartLayout.setWidth(updatedWidth + "px");
        width = updatedWidth;
        this.redrawChart();

    }

    public HorizontalLayout getBtnsLayout() {
        return btnsLayout;
    }

    private final Map<String, double[]> tooltipsProtNumberMap = new HashMap<String, double[]>();

    public ComparisonsSelectionOverviewBubbleChart(final QuantCentralManager Quant_Central_Manager, final CSFPRHandler handler, int chartWidth, int chartHeight, Set<QuantDiseaseGroupsComparison> selectedComparisonList, List<QuantProtein> searchQuantificationProtList) {
        this.searchQuantificationProtList = searchQuantificationProtList;
        this.width = chartWidth;
        this.height = chartHeight;
        this.handler = handler;
        this.setWidth(width + "px");
        this.setHeightUndefined();

        chartLayout.setWidth(width + "px");
        chartLayout.setHeight(height + "px");

        btnsLayout.setWidth(100 + "%");
        btnsLayout.setHeight(20 + "px");
        btnsLayout.setSpacing(true);

        this.Quant_Central_Manager = Quant_Central_Manager;
        this.Quant_Central_Manager.registerStudySelectionListener(ComparisonsSelectionOverviewBubbleChart.this);

        this.teststyle = "__" + System.currentTimeMillis() + "_heatmapOverviewBubbleChart";
        initialLayout = new VerticalLayout();
        initialLayout.setWidth(width + "px");
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

        HorizontalLayout btnContainerLayout = new HorizontalLayout();
        btnContainerLayout.setSpacing(true);
        btnContainerLayout.setMargin(new MarginInfo(false, true, false, false));
        btnContainerLayout.setWidthUndefined();
        btnContainerLayout.setHeightUndefined();
        btnsLayout.addComponent(btnContainerLayout);
        btnsLayout.setComponentAlignment(btnContainerLayout, Alignment.TOP_RIGHT);

        final OptionGroup significantProteinsOnlyOption = new OptionGroup();
        btnContainerLayout.addComponent(significantProteinsOnlyOption);
        significantProteinsOnlyOption.setWidth("80px");
        significantProteinsOnlyOption.setNullSelectionAllowed(true); // user can not 'unselect'
        significantProteinsOnlyOption.setMultiSelect(true);

        significantProteinsOnlyOption.addItem("Significant");
        significantProteinsOnlyOption.addStyleName("horizontal");
        significantProteinsOnlyOption.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Quant_Central_Manager.updateSignificantOnlySelection(significantProteinsOnlyOption.getValue().toString().equalsIgnoreCase("[Significant]"));

            }

        });

        resetBtn = new Button("Clear");
        resetBtn.setDescription("Unselect all data");
        resetBtn.setPrimaryStyleName("clearselectionbtn");
        resetBtn.setWidth("50px");
        resetBtn.setHeight("20px");

        btnContainerLayout.addComponent(resetBtn);

//        Button exportBtn = new Button("Save Chart Image");
//        exportBtn.setDescription("Save the chart image as pdf");
//        exportBtn.setStyleName(Reindeer.BUTTON_LINK);
//        exportBtn.addClickListener(new Button.ClickListener() {
//
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
////                exporter.writeChartToPDFFile(chart, 595, 842, "comparisons_overview.pdf");
//                ExternalResource res =new ExternalResource(defaultImgURL, "image/png");
//             LegacyWindow lw = new LegacyWindow();
//             lw.open(res, "save image ",595,842,BorderStyle.MINIMAL);
//                    
//                     
//            }
//        });
        exportPdfBtn = new Button("");
        exportPdfBtn.setWidth("20px");
        exportPdfBtn.setHeight("20px");
        exportPdfBtn.setPrimaryStyleName("exportpdfbtn");
        exportPdfBtn.setDescription("Export chart image");

        StreamResource myResource = createResource();
        FileDownloader fileDownloader = new FileDownloader(myResource);
        fileDownloader.extend(exportPdfBtn);

        btnContainerLayout.addComponent(exportPdfBtn);

        InfoPopupBtn info = new InfoPopupBtn("The bubble chart give an overview for the proteins existed in the selected comparisons.<br/>The diameter of the bubble represents the number of the proteins in the selected comparison and the color represents the trend<br/>");
        info.setWidth("20px");
        info.setHeight("20px");
        btnContainerLayout.addComponent(info);

//        btnsLayout.setExpandRatio(exportPdfBtn, 50);
//        btnsLayout.setComponentAlignment(exportPdfBtn, Alignment.MIDDLE_RIGHT);
        this.btnsLayout.setVisible(false);
        this.chartLayout.setVisible(false);
        this.addComponent(initialLayout);

        this.addComponent(chartLayout);
//        this.addComponent(btnsLayout);

        resetBtn.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                Quant_Central_Manager.setQuantProteinsSelection(new HashSet<String>(), "");
                resetChart();

            }
        });

        chartLayout.addLayoutClickListener(ComparisonsSelectionOverviewBubbleChart.this);

    }

    private StreamResource createResource() {
        return new StreamResource(new StreamSource() {
            @Override
            @SuppressWarnings("CallToPrintStackTrace")
            public InputStream getStream() {
                try {
                    byte[] pdfFile = handler.exportBubbleChartAsPdf(chart, "bubblechart_comparisons_selection.pdf", "Proteins Overview");
                    return new ByteArrayInputStream(pdfFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }
        }, "bubblechart_comparisons_selection.pdf");
    }

    private JFreeChart updateBubbleChartChart(Set<QuantDiseaseGroupsComparison> selectedComparisonList) {

        tooltipsProtNumberMap.clear();
        DefaultXYZDataset defaultxyzdataset = new DefaultXYZDataset();
        int counter = 0;
        int upper = -1;
        boolean significantOnly = this.Quant_Central_Manager.isSignificantOnly();

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
                if (upperCounter > upper) {
                    upper = upperCounter;
                }

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
            double[] tooltipNumbess = new double[tempWidthValue.length];
            System.arraycopy(tempWidthValue, 0, tooltipNumbess, 0, tempWidthValue.length);
            this.tooltipsProtNumberMap.put(qc.getComparisonHeader(), tooltipNumbess);
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
                widthValue[0] = 1;
            }
            seriousColorMap.put(counter, serColorArr);

            double[][] seriesValues = {yAxisValue, xAxisValue, widthValue};
            defaultxyzdataset.addSeries(qc.getComparisonHeader(), seriesValues);
            counter++;
        }

        final Color[] labelsColor = new Color[]{new Color(80, 183, 71), Color.LIGHT_GRAY, new Color(1, 141, 244), Color.LIGHT_GRAY, new Color(204, 0, 0)};
        Font font = new Font("Verdana", Font.PLAIN, 14);
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
        yAxis.setTickLabelFont(font);
        yAxis.setGridBandsVisible(false);
        yAxis.setAxisLinePaint(Color.LIGHT_GRAY);

        String[] xAxisLabels = new String[selectedComparisonList.size()];
        int x = 0;
        int maxLength = -1;
        for (QuantDiseaseGroupsComparison comp : selectedComparisonList) {
            xAxisLabels[x] = comp.getComparisonHeader() + " (" + comp.getDatasetIndexes().length + ")    ";
            if (xAxisLabels[x].length() > maxLength) {
                maxLength = xAxisLabels[x].length();
            }
            x++;

        }
        SymbolAxis xAxis;
        final boolean finalNum;
        finalNum = maxLength > 30 && selectedComparisonList.size() > 4;
        xAxis = new SymbolAxis(null, xAxisLabels) {
            private final boolean localfinal = finalNum;

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
                            tickLabel = formatter.format(currentTickValue) + "  ";
                        } else {
                            tickLabel = valueToString(currentTickValue) + "  ";
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

//        }
        xAxis.setTickLabelFont(font);
        xAxis.setTickLabelInsets(new RectangleInsets(2, 20, 2, 20));

        xAxis.setGridBandsVisible(false);
        xAxis.setAxisLinePaint(Color.LIGHT_GRAY);
        int scale = XYBubbleRenderer.SCALE_ON_RANGE_AXIS;

        XYItemRenderer xyitemrenderer = new XYBubbleRenderer(scale) {
            private int counter = 0;
            private int localSerious = -1;
            private final Map<Integer, Color[]> localSeriousColorMap = seriousColorMap;

            @Override
            public Paint getSeriesPaint(int series) {
                if (series != localSerious || isNewImge || localSeriousColorMap.get(series).length == counter) {
                    counter = 0;
                    isNewImge = false;
                }
                localSerious = series;
                Color c = localSeriousColorMap.get(series)[counter];
                counter++;

                return c;
            }

        };

        XYPlot xyplot = new XYPlot(defaultxyzdataset, xAxis, yAxis, xyitemrenderer) {
            private final Color[] labelsColor = new Color[]{new Color(0, 153, 0), new Color(0, 229, 132), new Color(1, 141, 244), new Color(255, 51, 51), new Color(204, 0, 0)};
            private final Font font = new Font("Verdana", Font.PLAIN, 12);
            private final String[] labels = new String[]{"Low 100%", "Low <100% ", "Stable", " High <100%", "High 100%"};

            @Override
            public LegendItemCollection getLegendItems() {
                LegendItemCollection legendItemCollection = new LegendItemCollection();
                for (int i = 0; i < labelsColor.length; i++) {
                    LegendItem item = new LegendItem(labels[i], labelsColor[i]);
                    item.setLabelFont(font);

                    legendItemCollection.add(item);

                }

                return legendItemCollection;//To change body of generated methods, choose Tools | Templates.
            }

        };

        JFreeChart generatedChart = new JFreeChart(xyplot) {

        };
        xyplot.setOutlineVisible(false);
        LegendTitle legend = generatedChart.getLegend();
        legend.setVisible(true);
        legend.setMargin(20, 0, 20, 0);
//        legend.setBorder(1, 1, 1, 1);
        legend.setFrame(new BlockBorder(1, 0, 1, 0, Color.LIGHT_GRAY));

//        generatedChart.removeLegend();
        xyplot.setForegroundAlpha(0.65F);
        xyplot.setBackgroundPaint(Color.WHITE);
        generatedChart.setBackgroundPaint(Color.WHITE);
        Quant_Central_Manager.setProteinsOverviewBubbleChart(generatedChart);
//        exporter.writeChartToPDFFile(generatedChart, 595, 842, "bublechart.pdf");
        return generatedChart;

    }

    private String saveToFile(final JFreeChart chart, final double width, final double height) {
        isNewImge = true;

        Set<SquaredDot> set = new TreeSet<SquaredDot>();
        try {

            imageData = ChartUtilities.encodeAsPNG(chart.createBufferedImage((int) width, (int) height, chartRenderingInfo));
            chartLayout.removeAllComponents();
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
                    int sqheight = (largeY - smallY);
                    if (sqheight < 2) {
                        continue;
                    } else if (sqheight < 14) {
                        smallY = smallY - (14 - sqheight);
                    }

                    int sqwidth = (largeX - smallX);
                    int finalWidth;
                    if (sqwidth < 20) {
                        finalWidth = 20;
                        smallX = smallX - ((finalWidth - sqwidth) / 2);

                    } else {
                        finalWidth = sqwidth;
                    }
                    int finalHeight;

                    if (sqheight < 20) {
                        finalHeight = 20;
                        if (sqheight < 14) {
                            smallY = smallY - (((finalHeight - sqheight) / 2) - (14 - sqheight));
                        } else {
                            smallY = smallY - ((finalHeight - sqheight) / 2);
                        }

                    } else {
                        finalHeight = sqheight;
                    }
                    square.setWidth(finalWidth + "px");
                    square.setHeight(finalHeight + "px");
                    if (selectedComparisonList == null || selectedComparisonList.isEmpty()) {
                        return "";
                    }

                    String header = ((QuantDiseaseGroupsComparison) selectedComparisonList.toArray()[catEnt.getSeriesIndex()]).getComparisonHeader();
                    int itemNumber = (int) ((XYItemEntity) entity).getDataset().getYValue(((XYItemEntity) entity).getSeriesIndex(), ((XYItemEntity) entity).getItem());

                    square.setDescription(header + "    <br/>#Proteins " + (int) tooltipsProtNumberMap.get(header)[itemNumber] + tooltipLabels[itemNumber]);
                    square.setParam("seriesIndex", ((XYItemEntity) entity).getSeriesIndex());
                    square.setParam("categIndex", (double) itemNumber);
//                    System.out.println("at top is "+smallY+"   ");
                    if (smallY < 0) {
//                       square.setHeight((finalHeight-smallY) + "px");
//                       smallY=0;
                    }
                    square.setParam("position", "left: " + smallX + "px; top: " + (smallY) + "px;");
                    set.add(square);
                }
            }
            for (SquaredDot square : set) {
                chartLayout.addComponent(square, square.getParam("position").toString());
            }
            String base64 = Base64.encodeBase64String(imageData);
            base64 = "data:image/png;base64," + base64;

//         exportPdfBtn.setValue("<a href='" + base64 + "'style='color: rgb(27, 105, 159);font-family: Verdana,;font-size: 12px;font-stretch: normal;font-style: normal;font-variant: normal;font-weight: normal;height: auto;line-height: normal;text-align: left;text-decoration: underline;text-shadow: none;'target='_blank' download> Export</a>");
//         exportPdfBtn.setImmediate(true);
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

        styles.add("." + teststyle + " { background-image: url(" + defaultImgURL + " );background-position:0px 0px; background-repeat: no-repeat; }");
        chartLayout.setStyleName(teststyle);
    }

    private Set<QuantDiseaseGroupsComparison> selectedComparisonList;

    @Override
    public void selectionChanged(String type) {
        if (type.equalsIgnoreCase("Comparison_Selection")) {
            selectedComparisonList = this.Quant_Central_Manager.getSelectedDiseaseGroupsComparisonList();

            Iterator<QuantDiseaseGroupsComparison> itr = selectedComparisonList.iterator();
            while (itr.hasNext()) {
                if (itr.next().getComparProtsMap() == null) {
                    selectedComparisonList = handler.getComparisonProtList(selectedComparisonList, searchQuantificationProtList);
                    break;
                }

            }
            if (selectedComparisonList.isEmpty()) {
                initialLayout.setVisible(true);
                chartLayout.removeAllComponents();
                chartLayout.setVisible(false);
                btnsLayout.setVisible(false);
                this.addComponent(initialLayout);
                defaultImgURL = "";
            } else {
                initialLayout.setVisible(false);
                btnsLayout.setVisible(true);
                chartLayout.setVisible(true);
                chart = this.updateBubbleChartChart(selectedComparisonList);
                defaultImgURL = saveToFile(chart, width, height);
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
     * @param max
     * @param upperLimit
     * @param lowerLimit
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

    private SquaredDot lastselectedComponent;

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (event.getClickedComponent() == null) {
            return;
        }
        if (event.getClickedComponent() instanceof SquaredDot) {
            SquaredDot square = (SquaredDot) event.getClickedComponent();
            if (square == lastselectedComponent) {
                resetBtn.click();

                return;

            }

            double trendIndex = (Double) ((SquaredDot) square).getParam("categIndex");
            int seriousIndex = (Integer) ((SquaredDot) square).getParam("seriesIndex");
            Set<String> selectionMap = new HashSet<String>();

            for (String key : ((QuantDiseaseGroupsComparison) Quant_Central_Manager.getSelectedDiseaseGroupsComparisonList().toArray()[seriousIndex]).getComparProtsMap().keySet()) {
                DiseaseGroupsComparisonsProteinLayout compProt = ((QuantDiseaseGroupsComparison) Quant_Central_Manager.getSelectedDiseaseGroupsComparisonList().toArray()[seriousIndex]).getComparProtsMap().get(key);
                if (compProt.getSignificantTrindCategory() == trendIndex) {
                    selectionMap.add(key.split("_")[1]);

                }

            }

            Quant_Central_Manager.setQuantProteinsSelection(selectionMap, ((QuantDiseaseGroupsComparison) Quant_Central_Manager.getSelectedDiseaseGroupsComparisonList().toArray()[seriousIndex]).getComparisonHeader());

            Iterator<Component> itr = chartLayout.iterator();
            while (itr.hasNext()) {
                SquaredDot tsquare = (SquaredDot) itr.next();
                tsquare.unselect();
            }
            square.select();
            lastselectedComponent = square;

        }
    }

    private void resetChart() {
        lastselectedComponent = null;
        Iterator<Component> itr = chartLayout.iterator();
        while (itr.hasNext()) {
            SquaredDot tsquare = (SquaredDot) itr.next();
            tsquare.reset();

        }

    }

}
