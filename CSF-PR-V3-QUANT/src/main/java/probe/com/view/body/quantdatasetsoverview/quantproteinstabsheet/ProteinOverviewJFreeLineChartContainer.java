package probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
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
import probe.com.handlers.CSFPRHandler;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.DiseaseGroupsComparisonsProteinLayout;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
import probe.com.model.util.AlphanumComparator;
import probe.com.model.util.vaadintoimageutil.peptideslayout.ProteinInformationDataForExport;
import probe.com.selectionmanager.QuantCentralManager;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.TrendLegend;
import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.kmeansclustering.KMeansClusteringPopupPanel;
import probe.com.view.core.InfoPopupBtn;
import probe.com.view.core.jfreeutil.SquaredDot;

/**
 * Interactive JfreeChart
 *
 * @author Yehia Farag
 */
public class ProteinOverviewJFreeLineChartContainer extends HorizontalLayout {

    public int getChartHeight() {
        return chartHeight;
    }

    private final int chartHeight;
    private String defaultLineChartImgUrl = "";
    private String orderedLineChartImg = "";
    private final ChartRenderingInfo defaultLineChartRenderingInfo = new ChartRenderingInfo();
    private final ChartRenderingInfo orderedLineChartRenderingInfo = new ChartRenderingInfo();
    private String thumbChart = "";
    private final JFreeChart defaultChart;
    private JFreeChart orderedChart;
    private final int selectedComparisonListSize;
    private final int width;
    private final VerticalLayout orederingTrendBtn = new VerticalLayout();
    ;
    private final StudiesPeptidesDetailsContainerLayout studiesScatterChartsLayout;
    private final AbsoluteLayout defaultLineChartContainer, orderedLineChartContainer;
//    private final String teststyle;
//    private final Page.Styles styles = Page.getCurrent().getStyles();
    private final QuantCentralManager Quant_Central_Manager;
    private final CSFPRHandler CSFPR_Handler;
    private final Shape notRShape = ShapeUtilities.createDiamond(6f);
    private final Shape emptyRShape = ShapeUtilities.createDiamond(6f);
    private final Shape downArr = ShapeUtilities.createDownTriangle(6f);
    private final Shape upArr = ShapeUtilities.createUpTriangle(6f);
    private final Map<String, Color> diseaseColorMap = new HashMap<String, Color>();
    private final Color[] customizedUserDataColor = new Color[]{Color.decode("#e5ffe5"), Color.WHITE, Color.decode("#e6f4ff"), Color.WHITE, Color.decode("#ffe5e5")};
    private final Image defaultChartImage = new Image();
    private final AbsoluteLayout defaultChartLayout = new AbsoluteLayout();
    private final Image trendOrderedChartImage = new Image();
    private final AbsoluteLayout trendOrederdChartLayout = new AbsoluteLayout();

    private final Set<ProteinInformationDataForExport> defaultPeptidesExportInfoSet;
    private Set<ProteinInformationDataForExport> orderedPeptidesExportInfoSet;

    private DiseaseGroupsComparisonsProteinLayout[] inUseComparisonProteins;
    private boolean verticalLabels;
    private XYPlot xyplot;
    private int custTrend = -1;

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
    public VerticalLayout getOrederingOptionGroup() {
        return orederingTrendBtn;
    }
    private final LayoutEvents.LayoutClickListener chartListener;
    private final String proteinName;

    /**
     *
     * @param Quant_Central_Manager
     * @param CSFPR_Handler
     * @param orgComparisonProteins
     * @param selectedComparisonList
     * @param widthValue
     * @param proteinName
     * @param proteinAccession
     * @param searchingMode
     * @param proteinKey
     */
    public ProteinOverviewJFreeLineChartContainer(QuantCentralManager Quant_Central_Manager, CSFPRHandler CSFPR_Handler, final DiseaseGroupsComparisonsProteinLayout[] orgComparisonProteins, final Set<QuantDiseaseGroupsComparison> selectedComparisonList, int widthValue, final String proteinName, final String proteinAccession, boolean searchingMode, final String proteinKey) {
        Map<String, String> diseaseHashedColorMap = Quant_Central_Manager.getDiseaseHashedColorMap();
        for (String str : diseaseHashedColorMap.keySet()) {
            diseaseColorMap.put(str, Color.decode(diseaseHashedColorMap.get(str)));
        }

        this.proteinName = proteinName;
        this.setStyleName(Reindeer.LAYOUT_WHITE);
        this.setSpacing(true);
        this.setHeightUndefined();
        this.setWidth(100 + "%");
        this.Quant_Central_Manager = Quant_Central_Manager;
        this.CSFPR_Handler = CSFPR_Handler;
        final DiseaseGroupsComparisonsProteinLayout[] comparisonProteins = new DiseaseGroupsComparisonsProteinLayout[orgComparisonProteins.length];
        int count = 0;
        for (DiseaseGroupsComparisonsProteinLayout quantProt : orgComparisonProteins) {
            if (quantProt == null) {

                QuantDiseaseGroupsComparison qdcomp = (QuantDiseaseGroupsComparison) selectedComparisonList.toArray()[count];
                quantProt = new DiseaseGroupsComparisonsProteinLayout(count, qdcomp, count);
                quantProt.setSignificantTrindCategory(-1);
            }
            comparisonProteins[count] = quantProt;
            count++;

        }

        VerticalLayout proteinOverviewPanelLayout = new VerticalLayout();
        VerticalLayout studyOverviewPanelLayout = new VerticalLayout();
        this.addComponent(proteinOverviewPanelLayout);
        this.addComponent(studyOverviewPanelLayout);
        //init toplayout left
        proteinOverviewPanelLayout.setSpacing(true);
        proteinOverviewPanelLayout.setWidth("100%");

        HorizontalLayout topLayout = new HorizontalLayout();
        proteinOverviewPanelLayout.addComponent(topLayout);
        topLayout.setSpacing(true);
        boolean isUniprot = Boolean.valueOf(proteinKey.split(",")[2].trim());
        Label overviewLabel;
        if (isUniprot) {
            overviewLabel = new Label("<font style='margin-left :50px'>Overview - <a href='http://www.uniprot.org/uniprot/" + proteinAccession.toUpperCase() + "' target='_blank'" + ">" + proteinName.replace("(", "__").split("__")[0].trim() + "</a>" + "</font> ");

        } else {
            overviewLabel = new Label("<font style='margin-left :50px'>Overview - " + proteinName.replace("(", "__").split("__")[0].trim() + "</font> ");

        }

        overviewLabel.setDescription(proteinAccession.toUpperCase() + "-" + proteinName.replace("(", "__").split("__")[0].trim() + "");
        overviewLabel.setContentMode(ContentMode.HTML);
        topLayout.addComponent(overviewLabel);
        overviewLabel.setStyleName("subtitle");
        overviewLabel.setWidth("100%");

        InfoPopupBtn info = new InfoPopupBtn("add text");
        info.setWidth("16px");
        info.setHeight("16px");
        topLayout.addComponent(info);

        //init chartlayout
        defaultChart = generateLineChart(comparisonProteins, selectedComparisonList);//, (width - 100), height, defaultLineChartRenderingInfo);

        int labelHeight = 0;
        int labelLetterCounter = 0;
        for (QuantDiseaseGroupsComparison qdcomp : selectedComparisonList) {
            if ((qdcomp.getComparisonHeader().length() * 6) > labelHeight) {
                labelHeight = (qdcomp.getComparisonHeader().length());

            }
            labelLetterCounter += qdcomp.getComparisonHeader().length();
        }
        width = ((widthValue) / 2) - 40;
        if (verticalLabels) {
            chartHeight = 400 + (labelHeight *= 2.5);
        } else if (((double) labelLetterCounter / (double) width) < 0.4) {
            chartHeight = 400 + 20;
        } else {
            chartHeight = 400 + (labelHeight *= 2.1);

        }
        this.selectedComparisonListSize = selectedComparisonList.size();
        VerticalLayout linechartContainerLayout = new VerticalLayout();
        linechartContainerLayout.setWidthUndefined();
        linechartContainerLayout.setHeightUndefined();
        linechartContainerLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        linechartContainerLayout.setSpacing(true);
        proteinOverviewPanelLayout.addComponent(linechartContainerLayout);
        chartListener = new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                Component c = event.getClickedComponent();
                if (c instanceof SquaredDot) {
                    QuantDiseaseGroupsComparison gc = (QuantDiseaseGroupsComparison) ((SquaredDot) c).getParam("GroupsComparison");
                    studiesScatterChartsLayout.highlightComparison(gc, true);
                }

            }
        };

        defaultLineChartContainer = new AbsoluteLayout();
        linechartContainerLayout.addComponent(defaultLineChartContainer);
        defaultLineChartContainer.setWidth(width + "px");
        defaultLineChartContainer.setHeight(chartHeight + "px");
        defaultLineChartContainer.addComponent(defaultChartImage, "left: " + 0 + "px; top: " + 0 + "px;");
        defaultLineChartContainer.addComponent(defaultChartLayout, "left: " + 0 + "px; top: " + 0 + "px;");
        defaultChartLayout.setWidth(width + "px");
        defaultChartLayout.setHeight(chartHeight + "px");

        defaultChartLayout.addLayoutClickListener(chartListener);
//        teststyle = proteinName.replace(" ", "_").replace(")", "_").replace("(", "_").replace(";", "_").toLowerCase().replace("#", "_").replace("?", "_").replace("[", "").replace("]", "").replace("/", "_").replace(":", "_").replace("'", "_").replace(".", "_") + "linechart";
//        styles.add("." + teststyle + " {  background-image: url(" + defaultLineChartImgUrl + " );background-position:center; background-repeat: no-repeat; }");
//        defaultLineChartContainer.setStyleName(teststyle);
//        defaultLineChartContainer.addLayoutClickListener(chartListener);

        orderedLineChartContainer = new AbsoluteLayout();
        linechartContainerLayout.addComponent(orderedLineChartContainer);
        orderedLineChartContainer.setWidth((width) + "px");
        orderedLineChartContainer.setHeight(chartHeight + "px");
        orderedLineChartContainer.setVisible(false);
        orderedLineChartContainer.addComponent(trendOrderedChartImage, "left: " + 0 + "px; top: " + 0 + "px;");
        orderedLineChartContainer.addComponent(trendOrederdChartLayout, "left: " + 0 + "px; top: " + 0 + "px;");
        trendOrederdChartLayout.setWidth(width + "px");
        trendOrederdChartLayout.setHeight(chartHeight + "px");
        trendOrederdChartLayout.addLayoutClickListener(chartListener);

//        orderedLineChartContainer.addLayoutClickListener(chartListener);
        //end of linechart layout
        //init bottom layout     
        HorizontalLayout bottomLayout = new HorizontalLayout();
        bottomLayout.setWidth((width - 5) + "px");
        bottomLayout.setHeight("24px");
        bottomLayout.setMargin(false);
        proteinOverviewPanelLayout.addComponent(bottomLayout);

        HorizontalLayout exportBtnLayout = new HorizontalLayout();
        exportBtnLayout.setWidthUndefined();
        exportBtnLayout.setHeightUndefined();
        exportBtnLayout.setSpacing(true);
        bottomLayout.addComponent(exportBtnLayout);
        bottomLayout.setComponentAlignment(exportBtnLayout, Alignment.TOP_RIGHT);

        TrendLegend legendLayout = new TrendLegend("linechart");
        legendLayout.setWidthUndefined();
        legendLayout.setHeight("24px");
        exportBtnLayout.addComponent(legendLayout);
        exportBtnLayout.setComponentAlignment(legendLayout, Alignment.TOP_RIGHT);

        exportBtnLayout.addComponent(orederingTrendBtn);
        orederingTrendBtn.setWidth("24px");
        orederingTrendBtn.setHeight("24px");
        orederingTrendBtn.setStyleName("defaultorder");
        orederingTrendBtn.setDescription("Order datasets by trend");
        orederingTrendBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            private DiseaseGroupsComparisonsProteinLayout[] ordComparisonProteins;

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                if (orederingTrendBtn.getStyleName().equalsIgnoreCase("trendorder")) {
//                    styles.add("." + teststyle + " {  background-image: url(" + defaultLineChartImgUrl + " );background-position:center; background-repeat: no-repeat; }");
//                    defaultLineChartContainer.setStyleName(teststyle);
                    defaultLineChartContainer.setVisible(true);
                    orderedLineChartContainer.setVisible(false);
                    studiesScatterChartsLayout.orderComparisons(comparisonProteins);
                    orederingTrendBtn.setStyleName("defaultorder");
                    orederingTrendBtn.setDescription("Order datasets by trend");

                } else {
                    orederingTrendBtn.setStyleName("trendorder");
                    orederingTrendBtn.setDescription("Restore datasets order");
                    if (orderedLineChartImg.equalsIgnoreCase("")) {
                        //order the comparisons and proteins
                        TreeMap<AlphanumComparator, DiseaseGroupsComparisonsProteinLayout> orderedCompProteins = new TreeMap<AlphanumComparator, DiseaseGroupsComparisonsProteinLayout>();
                        LinkedHashSet<QuantDiseaseGroupsComparison> orederedComparisonSet = new LinkedHashSet<QuantDiseaseGroupsComparison>();
                        for (DiseaseGroupsComparisonsProteinLayout cp : comparisonProteins) {
                            if (cp.getSignificantTrindCategory() == -1) {
                                AlphanumComparator key = new AlphanumComparator((102) + "-z" + cp.getComparison().getComparisonHeader());
                                orderedCompProteins.put(key, cp);
                            } else {
                                AlphanumComparator key = new AlphanumComparator((cp.getSignificantTrindCategory() + cp.getSignificantCellValue() + 100) + "-" + cp.getComparison().getComparisonHeader());
                                orderedCompProteins.put(key, cp);
                            }
                        }
                        ordComparisonProteins = new DiseaseGroupsComparisonsProteinLayout[orderedCompProteins.size()];
                        int i = 0;
                        for (AlphanumComparator cpHeader : orderedCompProteins.keySet()) {
                            DiseaseGroupsComparisonsProteinLayout cp = orderedCompProteins.get(cpHeader);
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
                        orderedLineChartImg = generateChartImage(orderedChart, (width), chartHeight, orderedLineChartRenderingInfo, trendOrederdChartLayout);
                        trendOrderedChartImage.setSource(new ExternalResource(orderedLineChartImg));
                    }
//                    styles.add("." + teststyle + " {  background-image: url(" + orderedLineChartImg + " );background-position:center; background-repeat: no-repeat; }");
//                    orderedLineChartContainer.setStyleName(teststyle);

                    defaultLineChartContainer.setVisible(false);
                    orderedLineChartContainer.setVisible(true);
                    studiesScatterChartsLayout.orderComparisons(ordComparisonProteins);
                    orderedPeptidesExportInfoSet = studiesScatterChartsLayout.getOrderedPeptidesExportInfoSet();
                }
            }
        });

        Button exportChartBtn = new Button("");
        exportChartBtn.setStyleName(Reindeer.BUTTON_LINK);
        exportChartBtn.setWidth("24px");
        exportChartBtn.setHeight("24px");
        exportChartBtn.setPrimaryStyleName("exportpdfbtn");
        exportChartBtn.setDescription("Export protein overview, datasets and peptides charts");
        exportBtnLayout.addComponent(exportChartBtn);
        StreamResource proteinInformationResource = createProteinsInformationResource();
        FileDownloader fileDownloader = new FileDownloader(proteinInformationResource);
        fileDownloader.extend(exportChartBtn);

        Button exportFullReportBtn = new Button("");
        exportFullReportBtn.setStyleName(Reindeer.BUTTON_LINK);
        exportFullReportBtn.setWidth("24px");
        exportFullReportBtn.setHeight("24px");
        exportFullReportBtn.setPrimaryStyleName("exportreportbtn");
        exportFullReportBtn.setDescription("Export full report");
        exportBtnLayout.addComponent(exportFullReportBtn);

        StreamResource fullReportResource = createFullReportResource();
        FileDownloader fullReportDownloader = new FileDownloader(fullReportResource);
        fullReportDownloader.extend(exportFullReportBtn);

        HorizontalLayout clusterKMeanLayout = new HorizontalLayout();
        clusterKMeanLayout.setMargin(new MarginInfo(true, false, false, false));
        clusterKMeanLayout.setHeight("50px");
        clusterKMeanLayout.setStyleName(Reindeer.LAYOUT_WHITE);

        Button clusterKMeanBtn = new Button("Protein K-Means Clustering ");
        clusterKMeanBtn.setStyleName(Reindeer.BUTTON_LINK);
        clusterKMeanBtn.setEnabled(false);
        clusterKMeanLayout.addComponent(clusterKMeanBtn);
        clusterKMeanLayout.setComponentAlignment(clusterKMeanBtn, Alignment.MIDDLE_CENTER);
        clusterKMeanBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                runKmeansClustering(proteinKey, proteinName, proteinAccession, selectedComparisonList);
            }
        });

        studiesScatterChartsLayout = new StudiesPeptidesDetailsContainerLayout(Quant_Central_Manager, comparisonProteins, selectedComparisonList, (width), chartHeight - 2, custTrend);
        studyOverviewPanelLayout.addComponent(studiesScatterChartsLayout);
        defaultPeptidesExportInfoSet = studiesScatterChartsLayout.getDefaultPeptidesExportInfoSet();

    }

    /**
     *
     * @param Quant_Central_Manager
     * @param CSFPR_Handler
     * @param orgComparisonProteins
     * @param selectedComparisonList
     * @param widthValue
     * @param proteinName
     * @param proteinAccession
     * @param searchingMode
     * @param proteinKey
     */
    public ProteinOverviewJFreeLineChartContainer(QuantCentralManager Quant_Central_Manager, CSFPRHandler CSFPR_Handler, final DiseaseGroupsComparisonsProteinLayout[] orgComparisonProteins, final Set<QuantDiseaseGroupsComparison> selectedComparisonList, int widthValue, final String proteinName, final String proteinAccession, boolean searchingMode, final String proteinKey, int custTrend) {
        this.custTrend = custTrend;
        Map<String, String> diseaseHashedColorMap = Quant_Central_Manager.getDiseaseHashedColorMap();
        for (String str : diseaseHashedColorMap.keySet()) {
            diseaseColorMap.put(str, Color.decode(diseaseHashedColorMap.get(str)));
        }

        this.proteinName = proteinName;
        this.setStyleName(Reindeer.LAYOUT_WHITE);
        this.setSpacing(true);
        this.setHeightUndefined();
        this.setWidth(100 + "%");
        this.Quant_Central_Manager = Quant_Central_Manager;
        this.CSFPR_Handler = CSFPR_Handler;
        final DiseaseGroupsComparisonsProteinLayout[] comparisonProteins = new DiseaseGroupsComparisonsProteinLayout[orgComparisonProteins.length];
        int count = 0;
        for (DiseaseGroupsComparisonsProteinLayout quantProt : orgComparisonProteins) {
            if (quantProt == null) {

                QuantDiseaseGroupsComparison qdcomp = (QuantDiseaseGroupsComparison) selectedComparisonList.toArray()[count];
                quantProt = new DiseaseGroupsComparisonsProteinLayout(count, qdcomp, count);
                quantProt.setSignificantTrindCategory(-1);
            }
            comparisonProteins[count] = quantProt;
            count++;

        }

        VerticalLayout proteinOverviewPanelLayout = new VerticalLayout();
        VerticalLayout studyOverviewPanelLayout = new VerticalLayout();
        this.addComponent(proteinOverviewPanelLayout);
        this.addComponent(studyOverviewPanelLayout);
        //init toplayout left
        proteinOverviewPanelLayout.setSpacing(true);
        proteinOverviewPanelLayout.setWidth("100%");

        HorizontalLayout topLayout = new HorizontalLayout();
        proteinOverviewPanelLayout.addComponent(topLayout);
        topLayout.setSpacing(true);
        boolean isUniprot = Boolean.valueOf(proteinKey.split(",")[2].trim());
        Label overviewLabel;
        if (isUniprot) {
            overviewLabel = new Label("<font style='margin-left :50px'>Overview - <a href='http://www.uniprot.org/uniprot/" + proteinAccession.toUpperCase() + "'" + ">" + proteinName.replace("(", "__").split("__")[0].trim() + "</a>" + "</font> ");

        } else {
            overviewLabel = new Label("<font style='margin-left :50px'>Overview - " + proteinName.replace("(", "__").split("__")[0].trim() + "</font> ");

        }

        overviewLabel.setDescription(proteinAccession.toUpperCase() + "-" + proteinName.replace("(", "__").split("__")[0].trim() + "");
        overviewLabel.setContentMode(ContentMode.HTML);
        topLayout.addComponent(overviewLabel);
        overviewLabel.setStyleName("subtitle");
        overviewLabel.setWidth("100%");

        InfoPopupBtn info = new InfoPopupBtn("add text");
        info.setWidth("16px");
        info.setHeight("16px");
        topLayout.addComponent(info);

        //init chartlayout
        defaultChart = generateLineChart(comparisonProteins, selectedComparisonList);//, (width - 100), height, defaultLineChartRenderingInfo);

        int labelHeight = 0;
        int labelLetterCounter = 0;
        for (QuantDiseaseGroupsComparison qdcomp : selectedComparisonList) {
            if ((qdcomp.getComparisonHeader().length() * 6) > labelHeight) {
                labelHeight = (qdcomp.getComparisonHeader().length());

            }
            labelLetterCounter += qdcomp.getComparisonHeader().length();
        }
        width = ((widthValue) / 2) - 40;
        if (verticalLabels) {
            chartHeight = 400 + (labelHeight *= 2.5);
        } else if (((double) labelLetterCounter / (double) width) < 0.4) {
            chartHeight = 400 + 20;
        } else {
            chartHeight = 400 + (labelHeight *= 2.1);

        }
        this.selectedComparisonListSize = selectedComparisonList.size();
        VerticalLayout linechartContainerLayout = new VerticalLayout();
        linechartContainerLayout.setWidthUndefined();
        linechartContainerLayout.setHeightUndefined();
        linechartContainerLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        linechartContainerLayout.setSpacing(true);
        proteinOverviewPanelLayout.addComponent(linechartContainerLayout);
        chartListener = new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                Component c = event.getClickedComponent();
                if (c instanceof SquaredDot) {
                    QuantDiseaseGroupsComparison gc = (QuantDiseaseGroupsComparison) ((SquaredDot) c).getParam("GroupsComparison");
                    studiesScatterChartsLayout.highlightComparison(gc, true);
                }

            }
        };

        defaultLineChartContainer = new AbsoluteLayout();
        linechartContainerLayout.addComponent(defaultLineChartContainer);
        defaultLineChartContainer.setWidth(width + "px");
        defaultLineChartContainer.setHeight(chartHeight + "px");
        defaultLineChartContainer.addComponent(defaultChartImage, "left: " + 0 + "px; top: " + 0 + "px;");

        defaultLineChartContainer.addComponent(defaultChartLayout, "left: " + 0 + "px; top: " + 0 + "px;");
        defaultChartLayout.setWidth(width + "px");
        defaultChartLayout.setHeight(chartHeight + "px");
        defaultChartLayout.addLayoutClickListener(chartListener);
//        teststyle = proteinName.replace(" ", "_").replace(")", "_").replace("(", "_").replace(";", "_").toLowerCase().replace("#", "_").replace("?", "_").replace("[", "").replace("]", "").replace("/", "_").replace(":", "_").replace("'", "_").replace(".", "_") + "linechart";
//        styles.add("." + teststyle + " {  background-image: url(" + defaultLineChartImgUrl + " );background-position:center; background-repeat: no-repeat; }");
//        defaultLineChartContainer.setStyleName(teststyle);
//        defaultLineChartContainer.addLayoutClickListener(chartListener);

        orderedLineChartContainer = new AbsoluteLayout();
        linechartContainerLayout.addComponent(orderedLineChartContainer);
        orderedLineChartContainer.setWidth((width) + "px");
        orderedLineChartContainer.setHeight(chartHeight + "px");
        orderedLineChartContainer.setVisible(false);
        orderedLineChartContainer.addComponent(trendOrderedChartImage, "left: " + 0 + "px; top: " + 0 + "px;");
        orderedLineChartContainer.addComponent(trendOrederdChartLayout, "left: " + 0 + "px; top: " + 0 + "px;");
        trendOrederdChartLayout.setWidth(width + "px");
        trendOrederdChartLayout.setHeight(chartHeight + "px");
        trendOrederdChartLayout.addLayoutClickListener(chartListener);

//        orderedLineChartContainer.addLayoutClickListener(chartListener);
        //end of linechart layout
        //init bottom layout     
        HorizontalLayout bottomLayout = new HorizontalLayout();
        bottomLayout.setWidth((width - 5) + "px");
        bottomLayout.setHeight("24px");
        bottomLayout.setMargin(false);
        proteinOverviewPanelLayout.addComponent(bottomLayout);

        HorizontalLayout exportBtnLayout = new HorizontalLayout();
        exportBtnLayout.setWidthUndefined();
        exportBtnLayout.setHeightUndefined();
        exportBtnLayout.setSpacing(true);
        bottomLayout.addComponent(exportBtnLayout);
        bottomLayout.setComponentAlignment(exportBtnLayout, Alignment.TOP_RIGHT);

        TrendLegend legendLayout = new TrendLegend("linechart");
        legendLayout.setWidthUndefined();
        legendLayout.setHeight("24px");
        exportBtnLayout.addComponent(legendLayout);
        exportBtnLayout.setComponentAlignment(legendLayout, Alignment.TOP_RIGHT);

        exportBtnLayout.addComponent(orederingTrendBtn);
        orederingTrendBtn.setWidth("24px");
        orederingTrendBtn.setHeight("24px");
        orederingTrendBtn.setStyleName("defaultorder");
        orederingTrendBtn.setDescription("Order datasets by trend");
        orederingTrendBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            private DiseaseGroupsComparisonsProteinLayout[] ordComparisonProteins;

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                if (orederingTrendBtn.getStyleName().equalsIgnoreCase("trendorder")) {
//                    styles.add("." + teststyle + " {  background-image: url(" + defaultLineChartImgUrl + " );background-position:center; background-repeat: no-repeat; }");
//                    defaultLineChartContainer.setStyleName(teststyle);
                    defaultLineChartContainer.setVisible(true);
                    orderedLineChartContainer.setVisible(false);
                    studiesScatterChartsLayout.orderComparisons(comparisonProteins);
                    orederingTrendBtn.setStyleName("defaultorder");
                    orederingTrendBtn.setDescription("Order datasets by trend");

                } else {
                    orederingTrendBtn.setStyleName("trendorder");
                    orederingTrendBtn.setDescription("Restore datasets order");
                    if (orderedLineChartImg.equalsIgnoreCase("")) {
                        //order the comparisons and proteins
                        TreeMap<AlphanumComparator, DiseaseGroupsComparisonsProteinLayout> orderedCompProteins = new TreeMap<AlphanumComparator, DiseaseGroupsComparisonsProteinLayout>();
                        LinkedHashSet<QuantDiseaseGroupsComparison> orederedComparisonSet = new LinkedHashSet<QuantDiseaseGroupsComparison>();
                        for (DiseaseGroupsComparisonsProteinLayout cp : comparisonProteins) {
                            if (cp.getSignificantTrindCategory() == -1) {
                                AlphanumComparator key = new AlphanumComparator((102) + "-z" + cp.getComparison().getComparisonHeader());
                                orderedCompProteins.put(key, cp);
                            } else {
                                AlphanumComparator key = new AlphanumComparator((cp.getSignificantTrindCategory() + cp.getSignificantCellValue() + 100) + "-" + cp.getComparison().getComparisonHeader());
                                orderedCompProteins.put(key, cp);
                            }
                        }
                        ordComparisonProteins = new DiseaseGroupsComparisonsProteinLayout[orderedCompProteins.size()];
                        int i = 0;
                        for (AlphanumComparator cpHeader : orderedCompProteins.keySet()) {
                            DiseaseGroupsComparisonsProteinLayout cp = orderedCompProteins.get(cpHeader);
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
                        orderedLineChartImg = generateChartImage(orderedChart, (width), chartHeight, orderedLineChartRenderingInfo, trendOrederdChartLayout);
                        trendOrderedChartImage.setSource(new ExternalResource(orderedLineChartImg));
                    }
//                    styles.add("." + teststyle + " {  background-image: url(" + orderedLineChartImg + " );background-position:center; background-repeat: no-repeat; }");
//                    orderedLineChartContainer.setStyleName(teststyle);

                    defaultLineChartContainer.setVisible(false);
                    orderedLineChartContainer.setVisible(true);
                    studiesScatterChartsLayout.orderComparisons(ordComparisonProteins);
                    orderedPeptidesExportInfoSet = studiesScatterChartsLayout.getOrderedPeptidesExportInfoSet();
                }
            }
        });

        Button exportChartBtn = new Button("");
        exportChartBtn.setStyleName(Reindeer.BUTTON_LINK);
        exportChartBtn.setWidth("24px");
        exportChartBtn.setHeight("24px");
        exportChartBtn.setPrimaryStyleName("exportpdfbtn");
        exportChartBtn.setDescription("Export protein overview, datasets and peptides charts");
        exportBtnLayout.addComponent(exportChartBtn);
        StreamResource proteinInformationResource = createProteinsInformationResource();
        FileDownloader fileDownloader = new FileDownloader(proteinInformationResource);
        fileDownloader.extend(exportChartBtn);

        Button exportFullReportBtn = new Button("");
        exportFullReportBtn.setStyleName(Reindeer.BUTTON_LINK);
        exportFullReportBtn.setWidth("24px");
        exportFullReportBtn.setHeight("24px");
        exportFullReportBtn.setPrimaryStyleName("exportreportbtn");
        exportFullReportBtn.setDescription("Export full report");
        exportBtnLayout.addComponent(exportFullReportBtn);

        StreamResource fullReportResource = createFullReportResource();
        FileDownloader fullReportDownloader = new FileDownloader(fullReportResource);
        fullReportDownloader.extend(exportFullReportBtn);

        HorizontalLayout clusterKMeanLayout = new HorizontalLayout();
        clusterKMeanLayout.setMargin(new MarginInfo(true, false, false, false));
        clusterKMeanLayout.setHeight("50px");
        clusterKMeanLayout.setStyleName(Reindeer.LAYOUT_WHITE);

        Button clusterKMeanBtn = new Button("Protein K-Means Clustering ");
        clusterKMeanBtn.setStyleName(Reindeer.BUTTON_LINK);
        clusterKMeanBtn.setEnabled(false);
        clusterKMeanLayout.addComponent(clusterKMeanBtn);
        clusterKMeanLayout.setComponentAlignment(clusterKMeanBtn, Alignment.MIDDLE_CENTER);
        clusterKMeanBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                runKmeansClustering(proteinKey, proteinName, proteinAccession, selectedComparisonList);
            }
        });

        studiesScatterChartsLayout = new StudiesPeptidesDetailsContainerLayout(Quant_Central_Manager, comparisonProteins, selectedComparisonList, (width), chartHeight - 2, custTrend);
        studyOverviewPanelLayout.addComponent(studiesScatterChartsLayout);
        defaultPeptidesExportInfoSet = studiesScatterChartsLayout.getDefaultPeptidesExportInfoSet();

    }

    private JFreeChart generateLineChart(DiseaseGroupsComparisonsProteinLayout[] comparisonProteins, Set<QuantDiseaseGroupsComparison> selectedComparisonList) {

        int upcounter = 0;
        int noValueprovidedcounter = 0;
        int midupcounter = 0;
        int notcounter = 0;
        int downcounter = 0;
        int middowncounter = 0;
        int counter = 0;// comparisonProteins.length;
        int emptyCounter = 0;
        verticalLabels = false;

        for (DiseaseGroupsComparisonsProteinLayout cp : comparisonProteins) {
            switch (cp.getSignificantTrindCategory()) {
                case -1:
                    emptyCounter++;
                    break;
                case 0:
                    downcounter++;
                    break;
                case 1:
                    middowncounter++;
                    break;
                case 2:
                    notcounter++;
                    break;
                case 3:
                    midupcounter++;
                    break;
                case 4:
                    upcounter++;
                    break;
                case 5:
                    noValueprovidedcounter++;
                    break;

            }

            counter++;

        }
        if (counter == 1) {
            orederingTrendBtn.setEnabled(false);
        }
        inUseComparisonProteins = new DiseaseGroupsComparisonsProteinLayout[counter];

        DefaultXYDataset dataset = new DefaultXYDataset();

        double[][] linevalues = new double[2][counter];

        double[] xLineValues = new double[counter];
        double[] yLineValues = new double[counter];

        double[][] noValueProvidedvalues = new double[2][noValueprovidedcounter];
        double[] xNoValueProvided = new double[noValueprovidedcounter];
        double[] yNoValueProvided = new double[noValueprovidedcounter];

        double[][] upvalues = new double[2][upcounter];
        double[] xUpValues = new double[upcounter];
        double[] yUpValues = new double[upcounter];

        double[][] midupvalues = new double[2][midupcounter];
        double[] xMidUpValues = new double[midupcounter];
        double[] yMidUpValues = new double[midupcounter];

        double[][] stablevalues = new double[2][notcounter];
        double[] xStabletValues = new double[notcounter];
        double[] yStableValues = new double[notcounter];

        double[][] downvalues = new double[2][downcounter];
        double[] xDownValues = new double[downcounter];
        double[] yDownValues = new double[downcounter];
        double[][] middownvalues = new double[2][middowncounter];
        double[] xMidDownValues = new double[middowncounter];
        double[] yMidDownValues = new double[middowncounter];

        double[][] emptyValues = new double[2][emptyCounter];
        double[] xEmptyValues = new double[emptyCounter];
        double[] yEmptyValues = new double[emptyCounter];

        int upIndex = 0;
        int notIndex = 0;
        int downIndex = 0;
        int midupIndex = 0;
        int middownIndex = 0;
        int emptyIndex = 0;
        int noValueProvidedIndex = 0;

        int compIndex = 0;
        int comparisonIndexer = 0;

        for (DiseaseGroupsComparisonsProteinLayout cp : comparisonProteins) {
            inUseComparisonProteins[compIndex] = cp;
            xLineValues[compIndex] = comparisonIndexer;

            if (cp == null || cp.getSignificantTrindCategory() == -1) {
                yLineValues[compIndex] = 0;
                xEmptyValues[emptyIndex] = comparisonIndexer;
                yEmptyValues[emptyIndex] = 0;
                emptyIndex++;
//                continue;
            } else if (cp.getSignificantTrindCategory() == 5) {
                yLineValues[compIndex] = cp.getOverallCellPercentValue();
                xNoValueProvided[noValueProvidedIndex] = comparisonIndexer;
                yNoValueProvided[noValueProvidedIndex] = cp.getOverallCellPercentValue();
                noValueProvidedIndex++;
            } else if (cp.getSignificantTrindCategory() == 4) {
                yLineValues[compIndex] = cp.getOverallCellPercentValue();
                xUpValues[upIndex] = comparisonIndexer;
                yUpValues[upIndex] = cp.getOverallCellPercentValue();
                upIndex++;
            } else if (cp.getSignificantTrindCategory() == 3) {
                xMidUpValues[midupIndex] = comparisonIndexer;
                yMidUpValues[midupIndex] = cp.getOverallCellPercentValue();
                midupIndex++;
                yLineValues[compIndex] = cp.getOverallCellPercentValue();
            } else if (cp.getSignificantTrindCategory() == 2) {
                yLineValues[compIndex] = cp.getOverallCellPercentValue();
                xStabletValues[notIndex] = comparisonIndexer;
                yStableValues[notIndex] = cp.getOverallCellPercentValue();
                notIndex++;
            } else if (cp.getSignificantTrindCategory() == 1) {
                yLineValues[compIndex] = cp.getOverallCellPercentValue();
                xMidDownValues[middownIndex] = comparisonIndexer;
                yMidDownValues[middownIndex] = cp.getOverallCellPercentValue();
                middownIndex++;
            } else if (cp.getSignificantTrindCategory() == 0) {
                yLineValues[compIndex] = cp.getOverallCellPercentValue();
                xDownValues[downIndex] = comparisonIndexer;
                yDownValues[downIndex] = cp.getOverallCellPercentValue();
                downIndex++;
            }

//            }
            compIndex++;
            comparisonIndexer++;

        }

        linevalues[0] = xLineValues;
        linevalues[1] = yLineValues;
        upvalues[0] = xUpValues;
        upvalues[1] = yUpValues;

        midupvalues[0] = xMidUpValues;
        midupvalues[1] = yMidUpValues;

        stablevalues[0] = xStabletValues;
        stablevalues[1] = yStableValues;

        middownvalues[0] = xMidDownValues;
        middownvalues[1] = yMidDownValues;

        downvalues[0] = xDownValues;
        downvalues[1] = yDownValues;

        emptyValues[0] = xEmptyValues;
        emptyValues[1] = yEmptyValues;

        noValueProvidedvalues[0] = xNoValueProvided;
        noValueProvidedvalues[1] = yNoValueProvided;

        dataset.addSeries("line", linevalues);
        dataset.addSeries("up", upvalues);
        dataset.addSeries("midup", midupvalues);
        dataset.addSeries("equal", stablevalues);
        dataset.addSeries("middown", middownvalues);
        dataset.addSeries("down", downvalues);
        dataset.addSeries("empty", emptyValues);
        dataset.addSeries("noValueProvided", noValueProvidedvalues);

        String[] xAxisLabels = new String[selectedComparisonList.size()];
        final Color[] diseaseGroupslabelsColor = new Color[selectedComparisonList.size()];
        int maxLength = -1;

        int x = 0;
        for (QuantDiseaseGroupsComparison comp : selectedComparisonList) {
            String groupCompTitle = comp.getComparisonHeader();
            String updatedHeader = groupCompTitle.split(" / ")[0].split("\n")[0] + " / " + groupCompTitle.split(" / ")[1].split("\n")[0];//+ " ( " + groupCompTitle.split(" / ")[1].split("\n")[1] + " )";

            xAxisLabels[x] = updatedHeader;
            if (xAxisLabels[x].length() + 5 > maxLength) {
                maxLength = xAxisLabels[x].length() + 5;
            }
            diseaseGroupslabelsColor[x] = diseaseColorMap.get(groupCompTitle.split(" / ")[0].split("\n")[1]);
            x++;

        }
        verticalLabels = maxLength > 30 && selectedComparisonList.size() > 4;

        Font font = new Font("Verdana", Font.BOLD, 13);

        SymbolAxis xAxis = new SymbolAxis(null, xAxisLabels) {
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

        final String[] tickLabels = new String[]{"Decreased", " ", "Equal", " ", "Increased"};

        NumberAxis yAxis = new NumberAxis() {
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

//            @Override
//            public AxisState draw(Graphics2D g2, double cursor, Rectangle2D plotArea, Rectangle2D dataArea, RectangleEdge edge, PlotRenderingInfo plotState) {
//                return super.draw(g2, cursor, plotArea, dataArea, edge, plotState); //To change body of generated methods, choose Tools | Templates.
//            }
//            @Override
//            protected AxisState drawTickMarksAndLabels(Graphics2D g2, double cursor, Rectangle2D plotArea, Rectangle2D dataArea, RectangleEdge edge) {
//                System.out.println("at ------------------------ cursor " + cursor);
//                return super.drawTickMarksAndLabels(g2, cursor, plotArea, dataArea, edge); //To change body of generated methods, choose Tools | Templates.
//            }
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
//                return super.valueToString(value); //To change body of generated methods, choose Tools | Templates.
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

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.GRAY);
        Color[] dataColor = new Color[]{new Color(0, 153, 0), new Color(0, 229, 132), new Color(1, 141, 244), new Color(247, 119, 119), new Color(204, 0, 0), Color.decode("#b5babb"), new Color(204, 204, 204)};

        renderer.setUseOutlinePaint(true);
        renderer.setSeriesPaint(1, dataColor[4]);
        renderer.setSeriesOutlinePaint(1, dataColor[4]);

        renderer.setSeriesPaint(2, dataColor[3]);
        renderer.setSeriesOutlinePaint(2, dataColor[3]);

        renderer.setSeriesPaint(3, dataColor[2]);
        renderer.setSeriesOutlinePaint(3, dataColor[2]);

        renderer.setSeriesPaint(4, dataColor[1]);
        renderer.setSeriesOutlinePaint(4, dataColor[1]);

        renderer.setSeriesPaint(5, dataColor[0]);
        renderer.setSeriesOutlinePaint(5, dataColor[0]);

        renderer.setSeriesPaint(6, Color.WHITE);
        renderer.setSeriesOutlinePaint(6, Color.GRAY);

        renderer.setSeriesPaint(7, dataColor[5]);
        renderer.setSeriesOutlinePaint(7, Color.GRAY);

        renderer.setSeriesShape(7, emptyRShape);
        renderer.setSeriesShape(6, emptyRShape);
        renderer.setSeriesShape(5, downArr);
        renderer.setSeriesShape(4, downArr);
        renderer.setSeriesShape(3, notRShape);
        renderer.setSeriesShape(2, upArr);
        renderer.setSeriesShape(1, upArr);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesStroke(0, new BasicStroke(
                1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1.0f, new float[]{10.0f, 6.0f}, 0.0f
        ));

        renderer.setSeriesLinesVisible(1, false);
        renderer.setSeriesLinesVisible(2, false);
        renderer.setSeriesLinesVisible(3, false);
        renderer.setSeriesLinesVisible(4, false);
        renderer.setSeriesLinesVisible(5, false);
        renderer.setSeriesLinesVisible(6, false);
        renderer.setSeriesLinesVisible(7, false);

        xyplot = new XYPlot(dataset, xAxis, yAxis, renderer) {

            private int counter = 0;

            @Override
            public Paint getRangeGridlinePaint() {
                if (counter == 239) {
                    counter = 0;
                }
                if (counter == 20 || (counter == 120) || (counter == 220)) {
                    counter++;
                    return super.getRangeGridlinePaint(); //To change body of generated methods, choose Tools | Templates.
                }
                if (custTrend != -1) {
                    if (custTrend == 0) {
                        if ((counter >= 10 && counter <= 19) || (counter >= 21 && counter <= 29)) {
                            counter++;
                            return customizedUserDataColor[custTrend];
                        }

                    }
                    if (custTrend == 2) {
                        if ((counter >= 110 && counter <= 119) || (counter >= 121 && counter <= 129)) {
                            counter++;
                            return customizedUserDataColor[custTrend];
                        }

                    }
                    if (custTrend == 4) {
                        if ((counter >= 210 && counter <= 219) || (counter >= 221 && counter <= 229)) {
                            counter++;
                            return customizedUserDataColor[custTrend];
                        }

                    }

                }
                counter++;
                return Color.WHITE;

            }

            private BasicStroke highlitedLineStrok = new BasicStroke(10f);
            private int counterII = 0;

            @Override
            public Stroke getRangeGridlineStroke() {
                if (counterII == 239) {
                    counterII = 0;
                    highlitedLineStrok = new BasicStroke(4f);
                }

                if (custTrend != -1) {
                    if (custTrend == 0) {
                        if ((counterII >= 10 && counterII <= 19) || (counterII >= 21 && counterII <= 29)) {
                            counterII++;
                            return highlitedLineStrok;
                        }

                    }
                    if (custTrend == 2) {
                        if ((counterII >= 110 && counterII <= 119) || (counterII >= 121 && counterII <= 129)) {
                            counterII++;
                            return highlitedLineStrok;
                        }

                    }
                    if (custTrend == 4) {
                        if ((counterII >= 210 && counterII <= 219) || (counterII >= 221 && counterII <= 229)) {
                            counterII++;
                            return highlitedLineStrok;
                        }

                    }

                }
                counterII++;

                return super.getRangeGridlineStroke();
            }

            @Override
            public void drawRangeTickBands(Graphics2D g2, Rectangle2D dataArea, List ticks) {

                if (custTrend == -1) {
                    super.drawRangeTickBands(g2, dataArea, ticks);
                    return;

                }
                int counterI = 0;
                List updatedTicksList = new ArrayList();
                for (Object tick : ticks) {

                    if (tick.toString().equalsIgnoreCase(tickLabels[custTrend])) {
                        for (int i = counterI - 1; i > counterI - 10; i--) {
                            updatedTicksList.add(ticks.get(i));
                        }
                        updatedTicksList.add(tick);
                        for (int i = counterI + 1; i < counterI + 11; i++) {
                            updatedTicksList.add(ticks.get(i));
                        }
                    }
                    counterI++;
                }
                Rectangle2D up;
                if (custTrend == 4) {
                    up = new Rectangle((int) dataArea.getX(), (int) dataArea.getY(), (int) dataArea.getWidth(), (int) dataArea.getHeight());

                } else if (custTrend == 2) {
                    up = new Rectangle((int) dataArea.getX(), (int) dataArea.getY(), (int) dataArea.getWidth(), (int) dataArea.getHeight());//                    
//
                } else {
                    up = new Rectangle((int) dataArea.getX(), (int) dataArea.getY(), (int) dataArea.getWidth(), (int) dataArea.getHeight());
                }

                super.drawRangeTickBands(g2, up, updatedTicksList); //To change body of generated methods, choose Tools | Templates.
            }
            private int x = 0;

        };
        if (custTrend != -1) {
            if (custTrend == 4) {
                xyplot.setRangeTickBandPaint(customizedUserDataColor[4]);

            } else if (custTrend == 0) {
                xyplot.setRangeTickBandPaint(customizedUserDataColor[0]);
            } else if (custTrend == 2) {
                xyplot.setRangeTickBandPaint(customizedUserDataColor[2]);//TickBandPaint(customizedUserDataColor[2]);
            }

        } else {
            xyplot.setRangeTickBandPaint(Color.WHITE);
        }

        JFreeChart jFreeChart = new JFreeChart(null, new Font("Verdana", Font.PLAIN, 18), xyplot, true);

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

    private final String[] tooltipLabels = new String[]{"( Decreased <img src='VAADIN/themes/dario-theme/img/sdown.png' alt='Decreased'>" + " )", "( Decreased <img src='VAADIN/themes/dario-theme/img/sdown.png' alt='Decreased'>" + " )", "( Equal <img src='VAADIN/themes/dario-theme/img/snotreg.png' alt='Stable'>" + " )", " ( Increased <img src='VAADIN/themes/dario-theme/img/sup.png' alt='Increased'>" + " )", " ( Increased <img src='VAADIN/themes/dario-theme/img/sup.png' alt='Increased'>" + " )"};

    private String generateChartImage(JFreeChart jFreeChart, int w, int h, ChartRenderingInfo chartRenderingInfo, AbsoluteLayout layoutContainer) {
        String imgUrl = saveToFile(jFreeChart, w, h, chartRenderingInfo);
        for (int i = 0; i < chartRenderingInfo.getEntityCollection().getEntityCount(); i++) {
            final ChartEntity entity = chartRenderingInfo.getEntityCollection().getEntity(i);
            if (entity instanceof XYItemEntity && !((XYItemEntity) entity).getArea().toString().contains("java.awt.geom.Path2")) {
                String[] arr = ((XYItemEntity) entity).getShapeCoords().split(",");
                int xSer = Integer.valueOf(arr[10]);
                int ySer = Integer.valueOf(arr[11]);
                int trend;
                SquaredDot square = new SquaredDot("cycle");
                square.setWidth(17 + "px");
                square.setHeight(17 + "px");
                QuantDiseaseGroupsComparison gc;
                String paramName = "GroupsComparison";

                if (inUseComparisonProteins[((XYItemEntity) entity).getItem()] == null || inUseComparisonProteins[((XYItemEntity) entity).getItem()].getSignificantTrindCategory() == -1) {
                    gc = new QuantDiseaseGroupsComparison();
                    gc.setComparisonHeader("No data available");
                    paramName = "Empty value";
                    trend = 2;
                    String groupCompTitle = gc.getComparisonHeader();
                    square.setDescription(groupCompTitle);
                } else {
                    gc = inUseComparisonProteins[((XYItemEntity) entity).getItem()].getComparison();
                    DiseaseGroupsComparisonsProteinLayout protLayout = inUseComparisonProteins[((XYItemEntity) entity).getItem()];
                    trend = protLayout.getSignificantTrindCategory();
                    if (trend == 5) {
                        trend = 2;
                    }
                    String groupCompTitle = gc.getComparisonHeader();
                    String updatedHeader = groupCompTitle.split(" / ")[0].split("\n")[0] + " / " + groupCompTitle.split(" / ")[1].split("\n")[0] + " - " + groupCompTitle.split(" / ")[1].split("\n")[1].replace("_", " ").replace("Disease", "") + "";

                    square.setDescription("<h4>" + updatedHeader + "" + tooltipLabels[trend] + "" + "</h4>" + "<h5>" + protLayout.toString() + "</h5>");
                }

                square.setParam(paramName, gc);
                switch (trend) {
                    case 0:
                        layoutContainer.addComponent(square, "left: " + (xSer - 6) + "px; top: " + (ySer - 8) + "px;");
                        break;
                    case 1:
                        layoutContainer.addComponent(square, "left: " + (xSer - 6) + "px; top: " + (ySer - 8) + "px;");
                        break;
                    case 2:
                        layoutContainer.addComponent(square, "left: " + (xSer - 6) + "px; top: " + (ySer - 5) + "px;");
                        break;
                    case 3:
                        layoutContainer.addComponent(square, "left: " + (xSer - 6) + "px; top: " + (ySer - 5) + "px;");
                        break;
                    case 4:
                        layoutContainer.addComponent(square, "left: " + (xSer - 6) + "px; top: " + (ySer - 5) + "px;");
                        break;

                }

            }
        }
        return imgUrl;

    }

    private void resetThumbChart(JFreeChart chart, boolean isFullChart) {

        final XYPlot plot = chart.getXYPlot();
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        SymbolAxis xAxis = (SymbolAxis) plot.getDomainAxis();

        plot.setDomainGridlinesVisible(isFullChart);
        plot.setRangeGridlinesVisible(true);
        yAxis.setVisible(isFullChart);
        xAxis.setVisible(isFullChart);
        plot.setOutlineVisible(isFullChart);

        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        if (isFullChart) {
            renderer.setSeriesShape(7, emptyRShape);
            renderer.setSeriesShape(6, emptyRShape);
            renderer.setSeriesShape(5, downArr);
            renderer.setSeriesShape(4, downArr);
            renderer.setSeriesShape(3, notRShape);
            renderer.setSeriesShape(2, upArr);
            renderer.setSeriesShape(1, upArr);
            renderer.setSeriesShapesVisible(0, false);
            renderer.setSeriesStroke(0, new BasicStroke(
                    1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                    1.0f, new float[]{10.0f, 6.0f}, 0.0f
            ));

            chart.getLegend().setVisible(false);

        } else {

            Shape snotRShape = ShapeUtilities.createDiamond(3f);
            final Shape semptyRShape = ShapeUtilities.createDiamond(3f);
            final Shape sleftArr = ShapeUtilities.createDownTriangle(3f);
            final Shape srightArr = ShapeUtilities.createUpTriangle(3f);

            renderer.setSeriesShape(7, semptyRShape);
            renderer.setSeriesShape(6, semptyRShape);
            renderer.setSeriesShape(5, sleftArr);
            renderer.setSeriesShape(4, sleftArr);
            renderer.setSeriesShape(3, snotRShape);
            renderer.setSeriesShape(2, srightArr);
            renderer.setSeriesShape(1, srightArr);
            renderer.setSeriesPaint(0, Color.DARK_GRAY);
            renderer.setSeriesStroke(0, null);

            chart.getLegend().setVisible(false);
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

        if (orederingTrendBtn.getStyleName().equalsIgnoreCase("defaultorder")) {
            if (defaultLineChartImgUrl.equalsIgnoreCase("")) {

                defaultLineChartImgUrl = this.generateChartImage(defaultChart, (width), chartHeight, defaultLineChartRenderingInfo, defaultChartLayout);
                defaultChartImage.setSource(new ExternalResource(defaultLineChartImgUrl));
                defaultChart.getXYPlot().setNoDataMessage(((int) width) + "," + ((int) chartHeight));
            }
            defaultLineChartContainer.setVisible(true);
            orderedLineChartContainer.setVisible(false);
//            styles.add("." + teststyle + " {  background-image: url(" + defaultLineChartImgUrl + " );background-position:center; background-repeat: no-repeat; }");
//            defaultLineChartContainer.setStyleName(teststyle);
        } else {
            if (orderedLineChartImg.equalsIgnoreCase("")) {
                orderedLineChartImg = this.generateChartImage(orderedChart, (width), chartHeight, orderedLineChartRenderingInfo, trendOrederdChartLayout);
                trendOrderedChartImage.setSource(new ExternalResource(orderedLineChartImg));
            }
            defaultLineChartContainer.setVisible(false);
            orderedLineChartContainer.setVisible(true);
//            styles.add("." + teststyle + " {  background-image: url(" + orderedLineChartImg + " );background-position:center; background-repeat: no-repeat; }");
//            orderedLineChartContainer.setStyleName(teststyle);
        }

        studiesScatterChartsLayout.redrawCharts();
    }
    private KMeansClusteringPopupPanel kMeansClusteringPanel;

    private void runKmeansClustering(String protKey, String proteinName, String proteinAccession, Set<QuantDiseaseGroupsComparison> selectedComparisonList) {
        if (kMeansClusteringPanel == null) {
            Map<String, DiseaseGroupsComparisonsProteinLayout[]> protSelectionMap = Quant_Central_Manager.getQuantProteinsLayoutSelectionMap();
            kMeansClusteringPanel = new KMeansClusteringPopupPanel(Quant_Central_Manager, CSFPR_Handler, protKey, proteinName, proteinAccession, protSelectionMap, selectedComparisonList);
        }
        kMeansClusteringPanel.setVisible(true);
    }

    private StreamResource createProteinsInformationResource() {
        return new StreamResource(new StreamResource.StreamSource() {
            @Override
            @SuppressWarnings("CallToPrintStackTrace")
            public InputStream getStream() {
                Set<JFreeChart> set = new LinkedHashSet<JFreeChart>();
                if (orederingTrendBtn.getStyleName().equalsIgnoreCase("defaultorder")) {
                    try {

                        set.add(defaultChart);
                        set.addAll(studiesScatterChartsLayout.getScatterCharts());
                        byte[] pdfFile = CSFPR_Handler.exportProteinsInfoCharts(set, "proteins_information_charts.pdf", proteinName, defaultPeptidesExportInfoSet, (int) defaultLineChartContainer.getWidth(), (int) defaultLineChartContainer.getHeight());
                        return new ByteArrayInputStream(pdfFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }

                } else {
                    try {
                        set.add(orderedChart);
                        set.addAll(studiesScatterChartsLayout.getScatterCharts());
                        byte[] pdfFile = CSFPR_Handler.exportProteinsInfoCharts(set, "proteins_information_charts.pdf", proteinName, orderedPeptidesExportInfoSet, (int) defaultLineChartContainer.getWidth(), (int) defaultLineChartContainer.getHeight());
                        return new ByteArrayInputStream(pdfFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }

                }

            }
        }, "proteins_information_charts.pdf");
    }

    private StreamResource createFullReportResource() {
        return new StreamResource(new StreamResource.StreamSource() {
            @Override
            @SuppressWarnings("CallToPrintStackTrace")
            public InputStream getStream() {
                Map<String, Set<JFreeChart>> chartsMap = new LinkedHashMap<String, Set<JFreeChart>>();
                Set<ProteinInformationDataForExport> peptidesExportInfoSet;
                chartsMap.put("StudiesPieCharts", Quant_Central_Manager.getStudiesOverviewPieChart());
                chartsMap.put("proteinsOverviewBubbleChart", Quant_Central_Manager.getProteinsOverviewBubbleChart());
                Set<JFreeChart> set = new LinkedHashSet<JFreeChart>();
                if (orederingTrendBtn.getStyleName().equalsIgnoreCase("defaultorder")) {
                    set.add(defaultChart);
                    set.addAll(studiesScatterChartsLayout.getScatterCharts());
                    peptidesExportInfoSet = defaultPeptidesExportInfoSet;
                } else {
                    set.add(orderedChart);
                    set.addAll(studiesScatterChartsLayout.getScatterCharts());
                    peptidesExportInfoSet = orderedPeptidesExportInfoSet;
                }
                chartsMap.put("proteinInformationCharts", set);
//              
                Quant_Central_Manager.getExportQuantTableBtn().click();
                byte[] pdfFile = CSFPR_Handler.exportfullReportAsZip(chartsMap, "full_Report.pdf", proteinName, peptidesExportInfoSet);

                return new ByteArrayInputStream(pdfFile);
//               

            }
        }, "full_Report.pdf");
    }

}
