package no.uib.probe.csf.pr.touch.view.components;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import no.uib.probe.csf.pr.touch.logic.beans.QuantComparisonProtein;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFListener;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFPR_Central_Manager;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFSelection;
import no.uib.probe.csf.pr.touch.view.components.datasetfilters.GroupSwichBtn;
import no.uib.probe.csf.pr.touch.view.core.ImageContainerBtn;
import no.uib.probe.csf.pr.touch.view.core.InfoPopupBtn;
import no.uib.probe.csf.pr.touch.view.core.BubbleComponent;
import no.uib.probe.csf.pr.touch.view.core.InformationButton;
import no.uib.probe.csf.pr.touch.view.core.TrendLegend;
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
import org.jfree.chart.renderer.xy.XYBubbleRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.text.TextUtilities;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;

/**
 *
 * @author Yehia Farag
 *
 * this class is represents bubble-chart for comparisons overview
 *
 */
public abstract class BubbleChartComponent extends VerticalLayout implements CSFListener, LayoutEvents.LayoutClickListener {
    
    private final CSFPR_Central_Manager CSFPR_Central_Manager;
    private final AbsoluteLayout chartLayoutContainer, chartComponentLayout;
    private int width, height;
    private final Image chartImage;
    private boolean significantOnly = false;
    private boolean isNewImge = true;
    
    private final Map<String, double[]> tooltipsProtNumberMap;
    private Color stableColor;
    
    private String defaultImgURL = "", thumbImgUrl = "";
    private byte imageData[];
    private final ChartRenderingInfo chartRenderingInfo;
    private Set<QuantDiseaseGroupsComparison> selectedComparisonList;
    private  QuantDiseaseGroupsComparison userCustomizedComparison;
    private  int userDataCounter;
    private final Set<BubbleComponent> lastselectedComponents;
    private final String[] tooltipLabels;
    private final String[] trendStyles;
    
    private boolean activeMultiSelect = true;
    
    public VerticalLayout getControlBtnsContainer() {
        return controlBtnsContainer;
    }
    
    private final VerticalLayout controlBtnsContainer;
    
    public BubbleChartComponent(CSFPR_Central_Manager CSFPR_Central_Manager, int width, int height) {
        this.CSFPR_Central_Manager = CSFPR_Central_Manager;
        
      
        
        this.setWidth(100, Unit.PERCENTAGE);
        this.setHeight(height, Unit.PIXELS);
        
        VerticalLayout bodyContainer = new VerticalLayout();
        bodyContainer.setWidth(100, Unit.PERCENTAGE);
        bodyContainer.setHeightUndefined();
        bodyContainer.setSpacing(true);
        this.addComponent(bodyContainer);

        //init toplayout
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setHeight(25, Unit.PIXELS);
        topLayout.setWidth(100, Unit.PERCENTAGE);
        topLayout.setSpacing(true);
        topLayout.setMargin(new MarginInfo(false, false, false, true));
        bodyContainer.addComponent(topLayout);
        
        HorizontalLayout titleLayoutWrapper = new HorizontalLayout();
        titleLayoutWrapper.setHeight(25, Unit.PIXELS);
        titleLayoutWrapper.setWidthUndefined();
        titleLayoutWrapper.setSpacing(true);
        titleLayoutWrapper.setMargin(false);
        topLayout.addComponent(titleLayoutWrapper);
        
        Label overviewLabel = new Label("Overview");
        overviewLabel.setStyleName(ValoTheme.LABEL_BOLD);
        overviewLabel.setWidth(75, Unit.PIXELS);
        titleLayoutWrapper.addComponent(overviewLabel);
        titleLayoutWrapper.setComponentAlignment(overviewLabel, Alignment.MIDDLE_CENTER);
        
//        InfoPopupBtn info = new InfoPopupBtn("The bubble chart give an overview for the proteins existed in the selected comparisons.<br/>The diameter of the bubble represents the number of the proteins in the selected comparison and the color represents the trend.<br/>");
//        titleLayoutWrapper.addComponent(info);
//        titleLayoutWrapper.setComponentAlignment(info, Alignment.MIDDLE_CENTER);
//        
        TrendLegend legendLayout = new TrendLegend("bubblechart");
        legendLayout.setWidthUndefined();
        legendLayout.setHeight(24, Unit.PIXELS);
        topLayout.addComponent(legendLayout);
        topLayout.setComponentAlignment(legendLayout, Alignment.MIDDLE_RIGHT);

        //end of toplayout
        //start chart layout
        VerticalLayout chartLayoutFrame = new VerticalLayout();
        height = height - 44;
        
        width = width - 50;
        chartLayoutFrame.setWidth(width, Unit.PIXELS);
        chartLayoutFrame.setHeight(height, Unit.PIXELS);
        chartLayoutFrame.addStyleName("roundedborder");
        chartLayoutFrame.addStyleName("padding20");
        chartLayoutFrame.addStyleName("whitelayout");
        bodyContainer.addComponent(chartLayoutFrame);
        bodyContainer.setComponentAlignment(chartLayoutFrame, Alignment.MIDDLE_CENTER);
        height = height - 40;
        width = width - 40;
        this.height = height;
        this.width = width;
        chartLayoutContainer = new AbsoluteLayout();
        chartLayoutContainer.setWidth(width, Unit.PIXELS);
        chartLayoutContainer.setHeight(this.height, Unit.PIXELS);
        chartLayoutFrame.addComponent(chartLayoutContainer);
        
        chartImage = new Image();
//        chartImage.setSource(new ThemeResource(""));
        chartImage.setWidth(100, Unit.PERCENTAGE);
        chartImage.setHeight(100, Unit.PERCENTAGE);
        chartLayoutContainer.addComponent(chartImage, "left: " + 0 + "px; top: " + 0 + "px;");
        
        chartComponentLayout = new AbsoluteLayout();
        chartComponentLayout.setWidth(100, Unit.PERCENTAGE);
        chartComponentLayout.setHeight(100, Unit.PERCENTAGE);
        chartComponentLayout.addLayoutClickListener(BubbleChartComponent.this);
        chartLayoutContainer.addComponent(chartComponentLayout, "left: " + 0 + "px; top: " + 0 + "px;");
        this.chartRenderingInfo = new ChartRenderingInfo();

        //init data structure
        tooltipsProtNumberMap = new HashMap<>();
        lastselectedComponents = new HashSet<>();
        tooltipLabels = new String[]{"", "  Decreased" + " ", "  Decreased" + " ", "  Equal" + " ", "  Increased>" + " ", "  Increased" + " ", ""};
        trendStyles = new String[]{"", "decreased100", "decreasedless100", "stable", "increasedless100", "increased100", ""};
        
        this.CSFPR_Central_Manager.registerListener(BubbleChartComponent.this);

        //init side control btns layout 
        controlBtnsContainer = new VerticalLayout();
        controlBtnsContainer.setHeightUndefined();
        controlBtnsContainer.setWidthUndefined();
        controlBtnsContainer.setSpacing(true);
        
        GroupSwichBtn groupSwichBtn = new GroupSwichBtn() {
            
            @Override
            public Set<QuantDiseaseGroupsComparison> getUpdatedComparsionList() {
                return CSFPR_Central_Manager.getSelectedComparisonsList();
            }
            
            @Override
            public void updateComparisons(LinkedHashSet<QuantDiseaseGroupsComparison> updatedComparisonList) {
                
                CSFSelection selection = new CSFSelection("comparisons_selection_update", getFilterId(), updatedComparisonList, null);
                CSFPR_Central_Manager.selectionAction(selection);
                
            }
            
            @Override
            public Map<QuantDiseaseGroupsComparison, QuantDiseaseGroupsComparison> getEqualComparsionMap() {
                return CSFPR_Central_Manager.getEqualComparisonMap();
            }
            
        };
        
        controlBtnsContainer.addComponent(groupSwichBtn);
        controlBtnsContainer.setComponentAlignment(groupSwichBtn, Alignment.MIDDLE_CENTER);
        
        ThemeResource scatterplotApplied = new ThemeResource("img/scatter_plot_applied_updated.png");
        ThemeResource scatterplotUnapplied = new ThemeResource("img/scatter_plot_unapplied.png");
        final ImageContainerBtn hideStableBtn = new ImageContainerBtn() {
            
            @Override
            public void onClick() {
                if (this.getDescription().equalsIgnoreCase("Hide stable proteins")) {
                    this.updateIcon(scatterplotUnapplied);
                    significantOnly = true;
                    this.setDescription("Show stable proteins");
                } else {
                    this.updateIcon(scatterplotApplied);
                    
                    significantOnly = false;
                    this.setDescription("Hide stable proteins");
                    
                }
                updateChart();

            }
        };
        
        hideStableBtn.setHeight(40, Unit.PIXELS);
        hideStableBtn.setWidth(40, Unit.PIXELS);
        hideStableBtn.updateIcon(scatterplotApplied);
        hideStableBtn.setEnabled(true);
        controlBtnsContainer.addComponent(hideStableBtn);
        controlBtnsContainer.setComponentAlignment(hideStableBtn, Alignment.MIDDLE_CENTER);
        hideStableBtn.setDescription("Hide stable proteins");
        ImageContainerBtn exportPdfBtn = new ImageContainerBtn() {
            
            @Override
            public void onClick() {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
            
        };
        
        exportPdfBtn.setHeight(40, Unit.PIXELS);
        exportPdfBtn.setWidth(40, Unit.PIXELS);
        exportPdfBtn.updateIcon(new ThemeResource("img/pdf-text-o.png"));
        exportPdfBtn.setEnabled(true);
        controlBtnsContainer.addComponent(exportPdfBtn);
        controlBtnsContainer.setComponentAlignment(exportPdfBtn, Alignment.MIDDLE_CENTER);
        exportPdfBtn.setDescription("Export chart image");

//        StreamResource myResource = null;//createResource();
//        FileDownloader fileDownloader = new FileDownloader(myResource);
//
//        fileDownloader.extend(groupSwichBtn);
        controlBtnsContainer.addComponent(exportPdfBtn);
        ImageContainerBtn unselectAllBtn = new ImageContainerBtn() {
            
            @Override
            public void onClick() {
                lastselectedComponents.clear();
                rePaintChart();
            }
            
        };
        unselectAllBtn.updateIcon(new ThemeResource("img/grid-small-o.png"));
        unselectAllBtn.setEnabled(true);
        unselectAllBtn.setWidth(40, Unit.PIXELS);
        unselectAllBtn.setHeight(40, Unit.PIXELS);
        unselectAllBtn.addStyleName("smallimg");
        controlBtnsContainer.addComponent(unselectAllBtn);
        controlBtnsContainer.setComponentAlignment(unselectAllBtn, Alignment.MIDDLE_CENTER);
        unselectAllBtn.setDescription("Unselect all disease group comparisons");
        
        final ImageContainerBtn selectMultiBtn = new ImageContainerBtn() {
            
            @Override
            public void onClick() {
                if (this.getStyleName().contains("selectmultiselectedbtn")) {
                    activeMultiSelect = false;
                    this.removeStyleName("selectmultiselectedbtn");
                    
                } else {
                    activeMultiSelect = true;
                    this.addStyleName("selectmultiselectedbtn");
                    
                }
            }
            
        };
        selectMultiBtn.addStyleName("selectmultiselectedbtn");
        selectMultiBtn.addStyleName("smallimg");
        selectMultiBtn.setDescription("Multiple selection");
        selectMultiBtn.updateIcon(new ThemeResource("img/grid-small-multi.png"));
        selectMultiBtn.setEnabled(true);
        selectMultiBtn.setWidth(40, Unit.PIXELS);
        selectMultiBtn.setHeight(40, Unit.PIXELS);
        controlBtnsContainer.addComponent(selectMultiBtn);
        controlBtnsContainer.setComponentAlignment(selectMultiBtn, Alignment.MIDDLE_CENTER);
        
         InformationButton info = new InformationButton("Info", false);
        controlBtnsContainer.addComponent(info);
        
    }
    
    private JFreeChart generateBubbleChart(Set<QuantDiseaseGroupsComparison> selectedComparisonList) {
         tooltipsProtNumberMap.clear();
        DefaultXYZDataset defaultxyzdataset = new DefaultXYZDataset();
        int counter = 0;
        int upper = -1;
        Set<QuantDiseaseGroupsComparison> tselectedComparisonList = new LinkedHashSet<>();
        if (userCustomizedComparison != null) {
         tselectedComparisonList.add(userCustomizedComparison);
         if (userCustomizedComparison.getQuantComparisonProteinMap().size() > upper) {
            upper = userCustomizedComparison.getQuantComparisonProteinMap().size();
        }

            
        }
        tselectedComparisonList.addAll(selectedComparisonList);
        
       
        
        for (QuantDiseaseGroupsComparison qc : tselectedComparisonList) {
            if (significantOnly) {
                int upperCounter = 0;
                upperCounter = qc.getQuantComparisonProteinMap().values().stream().filter((quantComparisonProtein) -> !(quantComparisonProtein == null)).filter((quantComparisonProtein) -> !(quantComparisonProtein.getSignificantTrindCategory() == 2 || quantComparisonProtein.getSignificantTrindCategory() == 5)).map((_item) -> 1).reduce(upperCounter, Integer::sum);
                if (upperCounter > upper) {
                    upper = upperCounter;
                }
                
            } else {
                if (qc.getQuantComparisonProteinMap() == null) {                    
                    System.out.println("null qc " + qc.getComparisonHeader());                    
                }
                if (qc.getQuantComparisonProteinMap().size() > upper) {
                    upper = qc.getQuantComparisonProteinMap().size();
                }
            }
            
        }
        
        final Map<Integer, Color[]> seriousColorMap = new HashMap<>();
        
        Color[] dataColor;
        
        dataColor = new Color[]{Color.WHITE, new Color(0, 153, 0), new Color(0, 229, 132), stableColor, new Color(247, 119, 119), new Color(204, 0, 0), Color.WHITE};
        
        double[] yAxisValueI = new double[]{0, 0, 0, 0, 0, 0, 0};
        double[] xAxisValueI = new double[]{0, 0, 0, 0, 0, 0, 0};
        double[] widthValueI = new double[]{0, 0, 0, 0, 0, 0, 0};
        double[][] seriesValuesI = {yAxisValueI, xAxisValueI, widthValueI};
        seriousColorMap.put(0, new Color[]{Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE});
        defaultxyzdataset.addSeries("   ", seriesValuesI);
        
        for (QuantDiseaseGroupsComparison quantComparison : tselectedComparisonList) {
            
            double[] tempWidthValue = new double[8];
            if (quantComparison.getQuantComparisonProteinMap() == null) {
                continue;
            }
            
            quantComparison.getQuantComparisonProteinMap().keySet().stream().forEach((key) -> {
                
                QuantComparisonProtein quantComparisonProtein = quantComparison.getQuantComparisonProteinMap().get(key);
                quantComparisonProtein.finalizeQuantData();
                
                if (significantOnly && (quantComparison.getQuantComparisonProteinMap().get(key).getSignificantTrindCategory() == 2 || quantComparison.getQuantComparisonProteinMap().get(key).getSignificantTrindCategory() == 5)) {
                    tempWidthValue[3] = 0;
                    tempWidthValue[6] = 0;
                } else {
                    tempWidthValue[quantComparison.getQuantComparisonProteinMap().get(key).getSignificantTrindCategory() + 1] = tempWidthValue[quantComparison.getQuantComparisonProteinMap().get(key).getSignificantTrindCategory() + 1] + 1;
                }
            });
            
            if (tempWidthValue[3] > 0 && tempWidthValue[6] >= 0) {
                stableColor = new Color(1, 141, 244);
                trendStyles[3] = "stable";
            } else if (tempWidthValue[3] == 0 && tempWidthValue[6] > 0) {
                stableColor = Color.decode("#b5babb");
                trendStyles[3] = "nodata";
            }
            
            tempWidthValue[3] = tempWidthValue[3] + tempWidthValue[6];
            tempWidthValue[6] = 0;
            dataColor[3] = stableColor;
            
            int length = 0;
            if (upper < 10) {
                upper = 10;
            }
            
            double[] tooltipNumbess = new double[tempWidthValue.length];
            System.arraycopy(tempWidthValue, 0, tooltipNumbess, 0, tempWidthValue.length);
            this.tooltipsProtNumberMap.put(quantComparison.getComparisonHeader(), tooltipNumbess);
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
                    yAxisValue[length] = counter + 1;
                    widthValue[length] = tempWidthValue[x];
                    serColorArr[length] = dataColor[x];
                    length++;
                }
                
            }
            
            if (length == 1 && tselectedComparisonList.size() == 1) {
                widthValue[0] = 1;
            }
            seriousColorMap.put(counter + 1, serColorArr);
            
            double[][] seriesValues = {yAxisValue, xAxisValue, widthValue};
            defaultxyzdataset.addSeries(quantComparison.getComparisonHeader(), seriesValues);
            counter++;
        }
        double[] yAxisValueII = new double[0];
        double[] xAxisValueII = new double[0];
        double[] widthValueII = new double[0];
        seriousColorMap.put(counter + 1, new Color[]{});
        double[][] seriesValuesII = {yAxisValueII, xAxisValueII, widthValueII};
        defaultxyzdataset.addSeries(" ", seriesValuesII);
        
        final Color[] labelsColor = new Color[]{Color.LIGHT_GRAY, new Color(80, 183, 71), Color.LIGHT_GRAY, new Color(1, 141, 244), Color.LIGHT_GRAY, new Color(204, 0, 0), Color.LIGHT_GRAY};
        Font font = new Font("Open Sans", Font.BOLD, 13);
        SymbolAxis yAxis = new SymbolAxis(null, new String[]{"  ", "Decreased", " ", "Equal", " ", "Increased", "  "}) {
            int x = 0;
            
            @Override
            public Paint getTickLabelPaint() {
                if (x >= labelsColor.length) {
                    x = 0;
                }
                return labelsColor[x++];
            }
        };
        yAxis.setAutoRangeStickyZero(true);
        yAxis.setFixedAutoRange(8);
        yAxis.setTickLabelFont(font);
        yAxis.setGridBandsVisible(false);
        yAxis.setAxisLinePaint(Color.LIGHT_GRAY);
        yAxis.setTickMarksVisible(false);
        yAxis.setUpperBound(6);
        
        String[] xAxisLabels = new String[tselectedComparisonList.size() + 2];
        int x = 0;
        xAxisLabels[x] = "";
        int maxLength = -1;
        //init labels color

        final Color[] diseaseGroupslabelsColor = new Color[tselectedComparisonList.size() + 2];
        diseaseGroupslabelsColor[x] = Color.WHITE;
        x++;
        
        for (QuantDiseaseGroupsComparison comp : tselectedComparisonList) {
            String header = comp.getComparisonHeader();
            String updatedHeader = header.split(" / ")[0].split("__")[0] + " / " + header.split(" / ")[1].split("__")[0] + "";
            
            xAxisLabels[x] = updatedHeader + " (" + comp.getDatasetMap().size() + ")    ";
            if (xAxisLabels[x].length() > maxLength) {
                maxLength = xAxisLabels[x].length();
            }
            diseaseGroupslabelsColor[x] = Color.decode(comp.getDiseaseCategoryColor());
            x++;
            
        }
        xAxisLabels[x] = "";
        diseaseGroupslabelsColor[x] = Color.WHITE;
        
        SymbolAxis xAxis;
        final boolean finalNum;
        finalNum = maxLength > 50 && tselectedComparisonList.size() > 4;
        
        xAxis = new SymbolAxis(null, xAxisLabels) {
            
            int x = 0;
            
            @Override
            public Paint getTickLabelPaint() {
                if (x >= diseaseGroupslabelsColor.length) {
                    x = 0;
                }
                return diseaseGroupslabelsColor[x++];
            }
            
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
        xAxis.setAutoRangeStickyZero(true);
        xAxis.setTickMarksVisible(false);
        xAxis.setUpperBound(diseaseGroupslabelsColor.length - 1);
        
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
            
            @Override
            protected void drawRangeGridlines(Graphics2D g2, Rectangle2D area, List ticks) {
                try {
                    if (!ticks.isEmpty()) {
                        ticks.remove(0);
                    }
                    if (!ticks.isEmpty()) {
                        ticks.remove(ticks.size() - 1);
                    }
                } catch (Exception e) {
                }
                super.drawRangeGridlines(g2, area, ticks); //To change body of generated methods, choose Tools | Templates.
            }
            
        };
        ;
        
        JFreeChart generatedChart = new JFreeChart(xyplot) {
            
        };
        xyplot.setOutlineVisible(false);
        LegendTitle legend = generatedChart.getLegend();
        legend.setVisible(false);
        xyplot.setForegroundAlpha(0.5F);
        
        xyplot.setBackgroundPaint(Color.WHITE);
        generatedChart.setBackgroundPaint(Color.WHITE);
        generatedChart.setPadding(new RectangleInsets(0, 0, 0, 0));
//        Quant_Central_Manager.setProteinsOverviewBubbleChart(generatedChart);
//        exporter.writeChartToPDFFile(generatedChart, 595, 842, "bublechart.pdf");
        return generatedChart;
        
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
        double logMax = (Math.log(max) / Math.log(2));
        double logValue = (Math.log(linearValue + 1) / Math.log(2));
        logValue = (logValue * 2 / logMax) + lowerLimit;
        return logValue;
    }
    
    @Override
    public void selectionChanged(String type) {
        
        if (type.equalsIgnoreCase("comparisons_selection")) {
            lastselectedComponents.clear();
            this.selectedComparisonList = CSFPR_Central_Manager.getSelectedComparisonsList();            
            if (selectedComparisonList.isEmpty()) {
                
                chartComponentLayout.removeAllComponents();
                chartImage.setSource(null);
                updateIcon(null);
                return;
                
            }
            
            updateChart();
            updateSelectionManager();
            
        }
         if (type.equalsIgnoreCase("quant_compare")) {
//            lastselectedComponents.clear();
//            this.selectedComparisonList = CSFPR_Central_Manager.getSelectedComparisonsList();         
//            this.userCustomizedComparison = CSFPR_Central_Manager.getQuantSearchSelection().getUserCustComparison();
//            if (selectedComparisonList.isEmpty()) {                
//                chartComponentLayout.removeAllComponents();
//                chartImage.setSource(null);
//                updateIcon(null);
//                return;
//                
//            }
//            
//            updateChart();
//            updateSelectionManager();
//            
        }
//        
        
        
    }
    
    private void updateChart() {
        JFreeChart chart = generateBubbleChart(selectedComparisonList);
        updateChartLayoutComponents(chart, width, height);
        DefaultXYZDataset emptyxyzdataset = new DefaultXYZDataset();
        DefaultXYZDataset dataset = ((DefaultXYZDataset) chart.getXYPlot().getDataset());
        for (int i = 0; i < dataset.getSeriesCount(); i++) {
            emptyxyzdataset.addSeries(dataset.getSeriesKey(i), new double[][]{{}, {}, {}});
        }
        chart.getXYPlot().setNoDataMessagePaint(Color.WHITE);
        chart.getXYPlot().setDataset(emptyxyzdataset);
        defaultImgURL = getChartImage(chart, width, height);
        
        chartImage.setSource(new ExternalResource(defaultImgURL));
        XYPlot xyplot = chart.getXYPlot();
        xyplot.getDomainAxis().setVisible(false);
        xyplot.getRangeAxis().setVisible(false);
        chart.setBorderVisible(true);
        chart.setBorderPaint(Color.LIGHT_GRAY);
        chart.getXYPlot().setDataset(dataset);
        thumbImgUrl = getChartImage(chart, 200, 200);
        updateIcon(thumbImgUrl);
        
    }
    
    private String getChartImage(JFreeChart chart, int width, int height) {
        if (chart == null) {
            return null;
        }
        
        String base64 = "";
        try {
            base64 = "data:image/png;base64," + Base64.encodeBase64String(ChartUtilities.encodeAsPNG(chart.createBufferedImage((int) width, (int) height, chartRenderingInfo)));
            
        } catch (IOException ex) {
            System.err.println("at error " + this.getClass() + " line 536 " + ex.getLocalizedMessage());
        }
        return base64;
        
    }
    
    private void updateChartLayoutComponents(final JFreeChart chart, final double width, final double height) {
        chart.getXYPlot().setNoDataMessage((int) width + "," + (int) height);
        
        if (width < 1 || height < 1) {
            return;
        }
        try {
            ChartUtilities.encodeAsPNG(chart.createBufferedImage((int) width, (int) height, chartRenderingInfo));
        } catch (IOException ex) {
            Logger.getLogger(BubbleChartComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
        isNewImge = true;
        Set<BubbleComponent> set = new TreeSet<>();
        Set<BubbleComponent> updatedselectedComponents = new HashSet<>();
        chartComponentLayout.removeAllComponents();
        for (int i = 0; i < chartRenderingInfo.getEntityCollection().getEntityCount(); i++) {
            ChartEntity entity = chartRenderingInfo.getEntityCollection().getEntity(i);
            if (entity instanceof XYItemEntity) {
                XYItemEntity catEnt = (XYItemEntity) entity;
                BubbleComponent square = new BubbleComponent("cycle");
                square.setStyleName("bubblechart");
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
                square.setWidth((finalWidth + 2), Unit.PIXELS);
                square.setHeight((finalHeight + 2), Unit.PIXELS);
                if (selectedComparisonList == null || selectedComparisonList.isEmpty()) {
                    return;
                }
                QuantDiseaseGroupsComparison comparison;
                if (userCustomizedComparison != null && catEnt.getSeriesIndex() == 0) {
                    continue;
                } else if (userCustomizedComparison != null && catEnt.getSeriesIndex() == 1) {
                    comparison = userCustomizedComparison;
                } else {
                    comparison = ((QuantDiseaseGroupsComparison) selectedComparisonList.toArray()[catEnt.getSeriesIndex() - 1 - userDataCounter]);
                }
                
                String header = comparison.getComparisonHeader();
                String updatedHeader = comparison.getComparisonFullName();//header.split(" / ")[0].split("\n")[0] + " / " + header.split(" / ")[1].split("\n")[0] + " - " + header.split(" / ")[1].split("\n")[1].replace("_", " ").replace("-", "'").replace("Disease", "") + "";
                int itemNumber = (int) ((XYItemEntity) entity).getDataset().getYValue(((XYItemEntity) entity).getSeriesIndex(), ((XYItemEntity) entity).getItem());
                square.addStyleName(trendStyles[itemNumber]);
                square.setDescription(updatedHeader +"<br/>Category: "+ tooltipLabels[itemNumber] + "<br/>#Proteins: " + (int) tooltipsProtNumberMap.get(header)[itemNumber]);
                double categIndex = (double) itemNumber;
                int seriesIndex = ((XYItemEntity) entity).getSeriesIndex();
                square.setParam("seriesIndex", seriesIndex);
                square.setParam("categIndex", categIndex);
                
                if (!lastselectedComponents.isEmpty()) {
                    square.select(false);
                    for (BubbleComponent lastselectedComponent : lastselectedComponents) {
                        if (lastselectedComponent != null && categIndex == (Double) lastselectedComponent.getParam("categIndex") && seriesIndex == (Integer) lastselectedComponent.getParam("seriesIndex")) {
                            square.select(true);
                            updatedselectedComponents.add(square);
                            break;
                        }
                    }
                    
                }
                square.setParam("position", "left: " + (smallX - 1) + "px; top: " + (smallY - 1) + "px;");
                square.setParam("proteinList", comparison.getProteinsByTrendMap().get((itemNumber - 1)));
                set.add(square);
            }
            
        }
        lastselectedComponents.clear();
        lastselectedComponents.addAll(updatedselectedComponents);
        set.stream().forEach((square) -> {
            chartComponentLayout.addComponent(square, square.getParam("position").toString());
        });
        
    }
    
    @Override
    public String getFilterId() {
        return "bubble_chart_listener";
    }
    
    @Override
    public void removeFilterValue(String value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        BubbleComponent selectedComponent = (BubbleComponent) event.getClickedComponent();
        updateSelectionList(selectedComponent);
        updateSelectionManager();
        
    }
    
    public abstract void updateIcon(String imageUrl);
    
    private void updateSelectionList(BubbleComponent selectedComponent) {
        if (selectedComponent == null) {
            lastselectedComponents.clear();
        } else if (this.activeMultiSelect) {
            if (lastselectedComponents.contains(selectedComponent)) {
                lastselectedComponents.remove(selectedComponent);
            } else {
                lastselectedComponents.add(selectedComponent);
            }
            
        } else {
            if (lastselectedComponents.contains(selectedComponent)) {
                lastselectedComponents.clear();
            } else {
                lastselectedComponents.clear();
                lastselectedComponents.add(selectedComponent);
            }
        }
        rePaintChart();
        
    }
    
    private void rePaintChart() {
        Iterator<Component> itr = chartComponentLayout.iterator();
        boolean selectAction = false;
        if (lastselectedComponents.isEmpty()) {
            selectAction = true;
        }
        
        while (itr.hasNext()) {
            Component component = itr.next();
            if (lastselectedComponents.contains((BubbleComponent) component)) {
                ((BubbleComponent) component).select(true);
                
            } else {
                ((BubbleComponent) component).select(selectAction);
            }
            
        }
        
    }
    
    private void updateSelectionManager() {
        Set<QuantComparisonProtein> selectedProteinsList;
        if (lastselectedComponents.isEmpty()) {
            selectedProteinsList = null;
            
        } else {
            selectedProteinsList = new LinkedHashSet<>();
            lastselectedComponents.stream().forEach((component) -> {
                selectedProteinsList.addAll((Set<QuantComparisonProtein>) component.getParam("proteinList"));
            });            
            
        }
        
        CSFSelection selection = new CSFSelection("protein_selection", getFilterId(), selectedComparisonList, selectedProteinsList);
        CSFPR_Central_Manager.selectionAction(selection);
    }
    public void addCustmisedUserDataCompariosn(QuantDiseaseGroupsComparison userCustomizedComparison){
      this.userCustomizedComparison = userCustomizedComparison;
        if (userCustomizedComparison == null) {
            userDataCounter = 0;
        } else {
            userDataCounter = 1;
        }
    
    }
}
