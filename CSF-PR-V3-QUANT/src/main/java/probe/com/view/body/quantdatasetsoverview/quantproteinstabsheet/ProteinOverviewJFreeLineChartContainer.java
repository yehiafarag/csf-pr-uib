package probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet;

import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
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
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
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
import javax.swing.text.StyleConstants;
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
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.text.TextUtilities;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
import org.jfree.util.ShapeUtilities;
import probe.com.handlers.CSFPRHandler;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.DiseaseGroupsComparisonsProteinLayout;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
import probe.com.model.util.vaadintoimageutil.peptideslayout.ProteinInformationDataForExport;
import probe.com.selectionmanager.QuantCentralManager;
import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.kmeansclustering.KMeansClusteringPopupPanel;
import probe.com.view.core.CustomExternalLink;
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
//    private final VerticalLayout bottomLiftSide;
    private final int width;
    private final OptionGroup orederingOptionGroup = new OptionGroup();
    private final ProteinStudiesComparisonsContainerLayout studiesScatterChartsLayout;
    private final AbsoluteLayout defaultLineChartContainer, orderedLineChartContainer;
    private final String teststyle;
    private final Page.Styles styles = Page.getCurrent().getStyles();
    private final QuantCentralManager Quant_Central_Manager;
    private final CSFPRHandler CSFPR_Handler;
    private final Shape notRShape = ShapeUtilities.createDiamond(6f);
    private final Shape emptyRShape = ShapeUtilities.createDiamond(6f);
    private final Shape downArr = ShapeUtilities.createDownTriangle(6f);
    private final Shape upArr = ShapeUtilities.createUpTriangle(6f);
    private final Shape square = ShapeUtilities.rotateShape(ShapeUtilities.createDiamond(8f), 0.8f, 0f, 0f);
    private final Map<String, Color> diseaseColorMap = new HashMap<String, Color>();
    private final Color[] customizedUserDataColor = new Color[]{Color.decode("#e5ffe5"), Color.WHITE, Color.decode("#e6f4ff"), Color.WHITE, Color.decode("#ffe5e5")};

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
        this.setSpacing(false);
        this.setHeightUndefined();
        this.setWidth(widthValue + "px");
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

        defaultChart = generateLineChart(comparisonProteins, selectedComparisonList);//, (width - 100), height, defaultLineChartRenderingInfo);

        int labelHeight = 0;
        int labelLetterCounter = 0;
        for (QuantDiseaseGroupsComparison qdcomp : selectedComparisonList) {
            if ((qdcomp.getComparisonHeader().length() * 6) > labelHeight) {
                labelHeight = (qdcomp.getComparisonHeader().length());

            }
            labelLetterCounter += qdcomp.getComparisonHeader().length();
        }

        widthValue = widthValue - 320;
        width = ((widthValue) / 2) - 100;

        if (verticalLabels) {
            chartHeight = 400 + (labelHeight *= 2.5);
        } else if (((double) labelLetterCounter / (double) width) < 0.4) {
            chartHeight = 400 + 20;
        } else {
            chartHeight = 400 + (labelHeight *= 2.1);

        }

        this.selectedComparisonListSize = selectedComparisonList.size();

        VerticalLayout infoContainer = new VerticalLayout();
        infoContainer.setWidth("250px");
        infoContainer.setHeight(350 + "px");
        infoContainer.setStyleName("borderlayout");

        GridLayout protInfoLayout = new GridLayout(2, 8);
        protInfoLayout.setMargin(false);
        protInfoLayout.setSpacing(true);
        protInfoLayout.setHeightUndefined();
        protInfoLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        Label accTitle = new Label("<b>Protein Accession</b>");
        accTitle.setContentMode(ContentMode.HTML);
        protInfoLayout.addComponent(accTitle, 0, 0);
        boolean isUniprot = Boolean.valueOf(proteinKey.split(",")[2].trim());
        CustomExternalLink acc;
        if (isUniprot) {
            acc = new CustomExternalLink(proteinAccession.toUpperCase(), "http://www.uniprot.org/uniprot/" + proteinAccession.toUpperCase());
        } else {
            acc = new CustomExternalLink(proteinAccession.toUpperCase(), null);
        }
        acc.setMargin(new MarginInfo(false, false, true, false));

        protInfoLayout.addComponent(acc, 0, 1);
        Label nameTitle = new Label("<b>Protein Name</b>");
        nameTitle.setContentMode(ContentMode.HTML);
        protInfoLayout.addComponent(nameTitle, 0, 2);

        Label nameValue = new Label("<textarea rows='4' cols='30' readonly>" + proteinName + "</textarea>");
        nameValue.setContentMode(ContentMode.HTML);
        nameValue.setStyleName("valuelabel");
        nameValue.setReadOnly(true);
        protInfoLayout.addComponent(nameValue, 0, 3);
//        bodyLayout.addComponent(protInfoLayout);

        Label compOrederTitle = new Label("<b>Comparisons Order</b>");
        compOrederTitle.setContentMode(ContentMode.HTML);
        protInfoLayout.addComponent(compOrederTitle, 0, 4);
        protInfoLayout.setWidth("250px");

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

        protInfoLayout.addComponent(clusterKMeanLayout, 0, 6);

        HorizontalLayout exportBtnLayout = new HorizontalLayout();
        exportBtnLayout.setWidthUndefined();
        exportBtnLayout.setHeightUndefined();
        exportBtnLayout.setSpacing(true);

        protInfoLayout.addComponent(exportBtnLayout, 0, 7);
        Button exportChartBtn = new Button("");
        exportChartBtn.setStyleName(Reindeer.BUTTON_LINK);
        exportChartBtn.setWidth("30px");
        exportChartBtn.setHeight("30px");
        exportChartBtn.setPrimaryStyleName("exportpdfbtn");
        exportChartBtn.setDescription("Export Protein Information");
        exportBtnLayout.addComponent(exportChartBtn);
        StreamResource proteinInformationResource = createProteinsInformationResource();
        FileDownloader fileDownloader = new FileDownloader(proteinInformationResource);
        fileDownloader.extend(exportChartBtn);

        Button exportFullReportBtn = new Button("");
        exportFullReportBtn.setStyleName(Reindeer.BUTTON_LINK);
        exportFullReportBtn.setWidth("30px");
        exportFullReportBtn.setHeight("30px");
        exportFullReportBtn.setPrimaryStyleName("exportzipbtn");
        exportFullReportBtn.setDescription("Export Full Report");
        exportBtnLayout.addComponent(exportFullReportBtn);

        InfoPopupBtn info = new InfoPopupBtn("add text");
        info.setWidth("30px");
        info.setHeight("30px");
        exportBtnLayout.addComponent(info);

//        clusterKMeanLayout.setComponentAlignment(exportChartBtn, Alignment.MIDDLE_CENTER);
//        exportChartBtn.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//                
//            }
//        });
        StreamResource fullReportResource = createFullReportResource();
        FileDownloader fullReportDownloader = new FileDownloader(fullReportResource);
        fullReportDownloader.extend(exportFullReportBtn);

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
        topLeftSideLayout.setMargin(new MarginInfo(false, false, false, false));

        leftSideLayout.addComponent(topLeftSideLayout);
        topLeftSideLayout.addComponent(infoContainer);
        infoContainer.addComponent(protInfoLayout);
        infoContainer.setSpacing(true);

        VerticalLayout linechartContainerLayout = new VerticalLayout();
        linechartContainerLayout.setWidthUndefined();
        linechartContainerLayout.setHeightUndefined();
        linechartContainerLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        linechartContainerLayout.setSpacing(true);
        topLeftSideLayout.addComponent(linechartContainerLayout);

//        bottomLiftSide = new VerticalLayout();
//        bottomLiftSide.setWidth("100%");
//        bottomLiftSide.setHeight(520 + "px");
//        bottomLiftSide.setStyleName(Reindeer.LAYOUT_WHITE);
//        leftSideLayout.addComponent(bottomLiftSide);
        VerticalLayout rightSideLayout = new VerticalLayout();
        rightSideLayout.setWidth("100%");
        rightSideLayout.setHeightUndefined();
        rightSideLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        rightSideLayout.setSpacing(true);
        rightSideLayout.setMargin(new MarginInfo(true, false, false, true));
        this.addComponent(rightSideLayout);
        defaultLineChartContainer = new AbsoluteLayout();
        orderedLineChartContainer = new AbsoluteLayout();

        Label overviewLabel = new Label("<p style='margin-left :60px'>Protein Overview</p> ");
        overviewLabel.setContentMode(ContentMode.HTML);
        linechartContainerLayout.addComponent(overviewLabel);
        overviewLabel.setStyleName("subtitle");
        overviewLabel.setWidth("300px");
        VerticalLayout spacer = new VerticalLayout();
        spacer.setWidth("40px");
        spacer.setHeight("20px");
        spacer.setStyleName(Reindeer.LAYOUT_WHITE);
        linechartContainerLayout.addComponent(spacer);

        //init leftside components - linechart 
        linechartContainerLayout.addComponent(defaultLineChartContainer);
        defaultLineChartContainer.setWidth(width + "px");
        defaultLineChartContainer.setHeight(chartHeight + "px");

        linechartContainerLayout.addComponent(orderedLineChartContainer);
        orderedLineChartContainer.setWidth((width) + "px");
        orderedLineChartContainer.setHeight(chartHeight + "px");

        teststyle = proteinName.replace(" ", "_").replace(")", "_").replace("(", "_").replace(";", "_").toLowerCase().replace("#", "_").replace("?", "_").replace("[", "").replace("]", "").replace("/", "_").replace(":", "_").replace("'", "_").replace(".", "_") + "linechart";
        styles.add("." + teststyle + " {  background-image: url(" + defaultLineChartImgUrl + " );background-position:center; background-repeat: no-repeat; }");
        defaultLineChartContainer.setStyleName(teststyle);
        orderedLineChartContainer.setVisible(false);
        chartListener = new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                Component c = event.getClickedComponent();
                if (c instanceof SquaredDot) {
                    QuantDiseaseGroupsComparison gc = (QuantDiseaseGroupsComparison) ((SquaredDot) c).getParam("GroupsComparison");
                    studiesScatterChartsLayout.highlightComparison(gc, true);
                } else {
//                    studiesScatterChartsLayout.highlightComparison(null, false);
                }

            }
        };
        defaultLineChartContainer.addLayoutClickListener(chartListener);
        orderedLineChartContainer.addLayoutClickListener(chartListener);

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
                    defaultLineChartContainer.setStyleName(teststyle);
                    defaultLineChartContainer.setVisible(true);
                    orderedLineChartContainer.setVisible(false);
                    studiesScatterChartsLayout.orderComparisons(comparisonProteins);

                } else {
                    if (orderedLineChartImg.equalsIgnoreCase("")) {
                        //order the comparisons and proteins
                        TreeMap<String, DiseaseGroupsComparisonsProteinLayout> orderedCompProteins = new TreeMap<String, DiseaseGroupsComparisonsProteinLayout>();
                        LinkedHashSet<QuantDiseaseGroupsComparison> orederedComparisonSet = new LinkedHashSet<QuantDiseaseGroupsComparison>();
                        for (DiseaseGroupsComparisonsProteinLayout cp : comparisonProteins) {
                            if (cp.getSignificantTrindCategory() == -1) {
                                orderedCompProteins.put((2) + "-z" + cp.getComparison().getComparisonHeader(), cp);
                            } else {
                                orderedCompProteins.put((cp.getSignificantTrindCategory()) + "-" + cp.getComparison().getComparisonHeader(), cp);
                            }
                        }
                        ordComparisonProteins = new DiseaseGroupsComparisonsProteinLayout[orderedCompProteins.size()];
                        int i = 0;
                        for (String cpHeader : orderedCompProteins.keySet()) {
                            DiseaseGroupsComparisonsProteinLayout cp = orderedCompProteins.get(cpHeader);
                            ordComparisonProteins[i] = cp;
//                            if (cp == null) {
//                                orederedComparisonSet.add(null);
//                            } else {
                            orederedComparisonSet.add(cp.getComparison());
//                            }
                            i++;
                        }
                        for (QuantDiseaseGroupsComparison gv : selectedComparisonList) {
                            if (!orederedComparisonSet.contains(gv)) {
                                orederedComparisonSet.add(gv);
                            }

                        }

                        orderedChart = generateLineChart(ordComparisonProteins, orederedComparisonSet);//, (width - 100), height, orderedLineChartRenderingInfo);
                        orderedLineChartImg = generateChartImage(orderedChart, (width), chartHeight, orderedLineChartRenderingInfo, orderedLineChartContainer);
//                        studiesScatterChartsLayout.orderComparisons(ordComparisonProteins);
                    }
                    styles.add("." + teststyle + " {  background-image: url(" + orderedLineChartImg + " );background-position:center; background-repeat: no-repeat; }");
                    orderedLineChartContainer.setStyleName(teststyle);
                    defaultLineChartContainer.setVisible(false);
                    orderedLineChartContainer.setVisible(true);
                    studiesScatterChartsLayout.orderComparisons(ordComparisonProteins);
                    orderedPeptidesExportInfoSet = studiesScatterChartsLayout.getOrderedPeptidesExportInfoSet();
                }
            }
        });
        protInfoLayout.addComponent(orederingOptionGroup, 0, 5);

        VerticalLayout dsInfoPopupContainerLayout = new VerticalLayout();
        dsInfoPopupContainerLayout.setWidth((width) + "px");
        dsInfoPopupContainerLayout.setHeight(400 + "px");
        dsInfoPopupContainerLayout.setStyleName(Reindeer.LAYOUT_WHITE);

//        init rightside components 
        studiesScatterChartsLayout = new ProteinStudiesComparisonsContainerLayout(Quant_Central_Manager, comparisonProteins, selectedComparisonList, (widthValue - width), chartHeight, custTrend);
        rightSideLayout.addComponent(studiesScatterChartsLayout);
        defaultPeptidesExportInfoSet = studiesScatterChartsLayout.getDefaultPeptidesExportInfoSet();
//        studiesScatterChartsLayout.setWidth(width * 2 + "px");
        this.setExpandRatio(leftSideLayout, (width + 300));
        this.setExpandRatio(rightSideLayout, (widthValue - width));

    }
    private int custTrend = -1;

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
        this.setSpacing(false);
        int protinesInfoTabHeight = Page.getCurrent().getBrowserWindowHeight() - 200;
        this.setHeight(protinesInfoTabHeight + "px");
        this.setWidth(widthValue + "px");
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

        int labelHeight = 0;
        int labelLetterCounter = 0;
        for (QuantDiseaseGroupsComparison qdcomp : selectedComparisonList) {
            if ((qdcomp.getComparisonHeader().length() * 6) > labelHeight) {
                labelHeight = (qdcomp.getComparisonHeader().length());

            }
            labelLetterCounter += qdcomp.getComparisonHeader().length();
        }

        widthValue = widthValue - 300;
        width = ((widthValue) / 2) - 100;

        defaultChart = generateLineChart(comparisonProteins, selectedComparisonList);//, (width - 100), height, defaultLineChartRenderingInfo);

        if (verticalLabels) {
            chartHeight = 400 + (labelHeight *= 2.5);
        } else if (((double) labelLetterCounter / (double) width) < 0.4) {
            chartHeight = 400 + 20;
        } else {
            chartHeight = 400 + (labelHeight *= 2.1);

        }
        this.selectedComparisonListSize = selectedComparisonList.size();

        VerticalLayout infoContainer = new VerticalLayout();
        infoContainer.setWidth("250px");
        infoContainer.setHeight(350 + "px");
        infoContainer.setStyleName("borderlayout");

        GridLayout protInfoLayout = new GridLayout(2, 8);
        protInfoLayout.setMargin(false);
        protInfoLayout.setSpacing(true);
        protInfoLayout.setHeightUndefined();
        protInfoLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        Label accTitle = new Label("<b>Protein Accession</b>");
        accTitle.setContentMode(ContentMode.HTML);
        protInfoLayout.addComponent(accTitle, 0, 0);
        boolean isUniprot = Boolean.valueOf(proteinKey.split(",")[2].trim());
        CustomExternalLink acc;
        if (isUniprot) {
            acc = new CustomExternalLink(proteinAccession.toUpperCase(), "http://www.uniprot.org/uniprot/" + proteinAccession.toUpperCase());
        } else {
            acc = new CustomExternalLink(proteinAccession.toUpperCase(), null);
        }
        acc.setMargin(new MarginInfo(false, false, true, false));

        protInfoLayout.addComponent(acc, 0, 1);
        Label nameTitle = new Label("<b>Protein Name</b>");
        nameTitle.setContentMode(ContentMode.HTML);
        protInfoLayout.addComponent(nameTitle, 0, 2);

        Label nameValue = new Label("<textarea rows='4' cols='30' readonly>" + proteinName + "</textarea>");
        nameValue.setContentMode(ContentMode.HTML);
        nameValue.setStyleName("valuelabel");
        nameValue.setReadOnly(true);
        protInfoLayout.addComponent(nameValue, 0, 3);
//        bodyLayout.addComponent(protInfoLayout);

        Label compOrederTitle = new Label("<b>Comparisons Order</b>");
        compOrederTitle.setContentMode(ContentMode.HTML);
        protInfoLayout.addComponent(compOrederTitle, 0, 4);
        protInfoLayout.setWidth("250px");

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

        protInfoLayout.addComponent(clusterKMeanLayout, 0, 6);

        HorizontalLayout exportBtnLayout = new HorizontalLayout();
        exportBtnLayout.setWidthUndefined();
        exportBtnLayout.setHeightUndefined();
        exportBtnLayout.setSpacing(true);

        protInfoLayout.addComponent(exportBtnLayout, 0, 7);
        Button exportChartBtn = new Button("");
        exportChartBtn.setStyleName(Reindeer.BUTTON_LINK);
        exportChartBtn.setWidth("30px");
        exportChartBtn.setHeight("30px");
        exportChartBtn.setPrimaryStyleName("exportpdfbtn");
        exportChartBtn.setDescription("Export Protein Information");
        exportBtnLayout.addComponent(exportChartBtn);
        StreamResource proteinInformationResource = createProteinsInformationResource();
        FileDownloader fileDownloader = new FileDownloader(proteinInformationResource);
        fileDownloader.extend(exportChartBtn);

        Button exportFullReportBtn = new Button("");
        exportFullReportBtn.setStyleName(Reindeer.BUTTON_LINK);
        exportFullReportBtn.setWidth("30px");
        exportFullReportBtn.setHeight("30px");
        exportFullReportBtn.setPrimaryStyleName("exportzipbtn");
        exportFullReportBtn.setDescription("Export Full Report");
        exportBtnLayout.addComponent(exportFullReportBtn);

        InfoPopupBtn info = new InfoPopupBtn("add text");
        info.setWidth("30px");
        info.setHeight("30px");
        exportBtnLayout.addComponent(info);

//        clusterKMeanLayout.setComponentAlignment(exportChartBtn, Alignment.MIDDLE_CENTER);
//        exportChartBtn.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//                
//            }
//        });
        StreamResource fullReportResource = createFullReportResource();
        FileDownloader fullReportDownloader = new FileDownloader(fullReportResource);
        fullReportDownloader.extend(exportFullReportBtn);

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
        topLeftSideLayout.setMargin(new MarginInfo(false, false, false, false));

        leftSideLayout.addComponent(topLeftSideLayout);
        topLeftSideLayout.addComponent(infoContainer);
        infoContainer.addComponent(protInfoLayout);
        infoContainer.setSpacing(true);

        VerticalLayout linechartContainerLayout = new VerticalLayout();
        linechartContainerLayout.setWidthUndefined();
        linechartContainerLayout.setHeightUndefined();
        linechartContainerLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        linechartContainerLayout.setSpacing(true);
        topLeftSideLayout.addComponent(linechartContainerLayout);

//        bottomLiftSide = new VerticalLayout();
//        bottomLiftSide.setWidth("100%");
//        bottomLiftSide.setHeight(520 + "px");
//        bottomLiftSide.setStyleName(Reindeer.LAYOUT_WHITE);
//        leftSideLayout.addComponent(bottomLiftSide);
        VerticalLayout rightSideLayout = new VerticalLayout();
        rightSideLayout.setWidth("100%");
        rightSideLayout.setHeightUndefined();
        rightSideLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        rightSideLayout.setSpacing(true);
        rightSideLayout.setMargin(new MarginInfo(true, false, false, true));
        this.addComponent(rightSideLayout);
        this.setComponentAlignment(rightSideLayout, Alignment.TOP_RIGHT);
        defaultLineChartContainer = new AbsoluteLayout();
        orderedLineChartContainer = new AbsoluteLayout();

        Label overviewLabel = new Label("<p style='margin-left :60px'>Protein Overview</p> ");
        overviewLabel.setContentMode(ContentMode.HTML);
        linechartContainerLayout.addComponent(overviewLabel);
        overviewLabel.setStyleName("subtitle");
        overviewLabel.setWidth("300px");
        VerticalLayout spacer = new VerticalLayout();
        spacer.setWidth("40px");
        spacer.setHeight("20px");
        spacer.setStyleName(Reindeer.LAYOUT_WHITE);
        linechartContainerLayout.addComponent(spacer);

        //init leftside components - linechart 
        linechartContainerLayout.addComponent(defaultLineChartContainer);
        defaultLineChartContainer.setWidth((width) + "px");
        defaultLineChartContainer.setHeight(chartHeight + "px");

        linechartContainerLayout.addComponent(orderedLineChartContainer);
        orderedLineChartContainer.setWidth((width) + "px");
        orderedLineChartContainer.setHeight(chartHeight + "px");

        teststyle = proteinName.replace(" ", "_").replace(")", "_").replace("(", "_").replace(";", "_").toLowerCase().replace("#", "_").replace("?", "_").replace("[", "").replace("]", "").replace("/", "_").replace(":", "_").replace("'", "_").replace(".", "_") + "linechart";
        styles.add("." + teststyle + " {  background-image: url(" + defaultLineChartImgUrl + " );background-position:center; background-repeat: no-repeat; }");
        defaultLineChartContainer.setStyleName(teststyle);
        orderedLineChartContainer.setVisible(false);
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
        defaultLineChartContainer.addLayoutClickListener(chartListener);
        orderedLineChartContainer.addLayoutClickListener(chartListener);

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
                    defaultLineChartContainer.setStyleName(teststyle);
                    defaultLineChartContainer.setVisible(true);
                    orderedLineChartContainer.setVisible(false);
                    studiesScatterChartsLayout.orderComparisons(comparisonProteins);

                } else {
                    if (orderedLineChartImg.equalsIgnoreCase("")) {
                        //order the comparisons and proteins
                        TreeMap<String, DiseaseGroupsComparisonsProteinLayout> orderedCompProteins = new TreeMap<String, DiseaseGroupsComparisonsProteinLayout>();
                        LinkedHashSet<QuantDiseaseGroupsComparison> orederedComparisonSet = new LinkedHashSet<QuantDiseaseGroupsComparison>();
                        for (DiseaseGroupsComparisonsProteinLayout cp : comparisonProteins) {
                            if (cp.getSignificantTrindCategory() == -1) {
                                orderedCompProteins.put((2) + "-z" + cp.getComparison().getComparisonHeader(), cp);
                            } else {
                                orderedCompProteins.put((cp.getSignificantTrindCategory()) + "-" + cp.getComparison().getComparisonHeader(), cp);
                            }
                        }
                        ordComparisonProteins = new DiseaseGroupsComparisonsProteinLayout[orderedCompProteins.size()];
                        int i = 0;
                        for (String cpHeader : orderedCompProteins.keySet()) {
                            DiseaseGroupsComparisonsProteinLayout cp = orderedCompProteins.get(cpHeader);
                            ordComparisonProteins[i] = cp;
//                            if (cp == null) {
//                                orederedComparisonSet.add(null);
//                            } else {
                            orederedComparisonSet.add(cp.getComparison());
//                            }
                            i++;
                        }
                        for (QuantDiseaseGroupsComparison gv : selectedComparisonList) {
                            if (!orederedComparisonSet.contains(gv)) {
                                orederedComparisonSet.add(gv);
                            }

                        }

                        orderedChart = generateLineChart(ordComparisonProteins, orederedComparisonSet);//, (width - 100), height, orderedLineChartRenderingInfo);
                        orderedLineChartImg = generateChartImage(orderedChart, (width), chartHeight, orderedLineChartRenderingInfo, orderedLineChartContainer);
//                        studiesScatterChartsLayout.orderComparisons(ordComparisonProteins);
                    }
                    styles.add("." + teststyle + " {  background-image: url(" + orderedLineChartImg + " );background-position:center; background-repeat: no-repeat; }");
                    orderedLineChartContainer.setStyleName(teststyle);
                    defaultLineChartContainer.setVisible(false);
                    orderedLineChartContainer.setVisible(true);
                    studiesScatterChartsLayout.orderComparisons(ordComparisonProteins);
                    orderedPeptidesExportInfoSet = studiesScatterChartsLayout.getOrderedPeptidesExportInfoSet();
                }
            }
        });
        protInfoLayout.addComponent(orederingOptionGroup, 0, 5);

        VerticalLayout dsInfoPopupContainerLayout = new VerticalLayout();
        dsInfoPopupContainerLayout.setWidth((width) + "px");
        dsInfoPopupContainerLayout.setHeight(400 + "px");
        dsInfoPopupContainerLayout.setStyleName(Reindeer.LAYOUT_WHITE);

//        init rightside components 
        studiesScatterChartsLayout = new ProteinStudiesComparisonsContainerLayout(Quant_Central_Manager, comparisonProteins, selectedComparisonList, (widthValue - width), chartHeight, custTrend);
        rightSideLayout.addComponent(studiesScatterChartsLayout);
        defaultPeptidesExportInfoSet = studiesScatterChartsLayout.getDefaultPeptidesExportInfoSet();
//        studiesScatterChartsLayout.setWidth(width * 2 + "px");
        this.setExpandRatio(leftSideLayout, (width + 300));
        this.setExpandRatio(rightSideLayout, (widthValue - width));

    }

    private final Set<ProteinInformationDataForExport> defaultPeptidesExportInfoSet;
    private Set<ProteinInformationDataForExport> orderedPeptidesExportInfoSet;

    private DiseaseGroupsComparisonsProteinLayout[] inUseComparisonProteins;
    private boolean verticalLabels;

    private JFreeChart generateLineChart(DiseaseGroupsComparisonsProteinLayout[] comparisonProteins, Set<QuantDiseaseGroupsComparison> selectedComparisonList) {
        int upcounter = 0;
        int noValueprovidedcounter = 0;
        int midupcounter = 0;
        int notcounter = 0;
        int downcounter = 0;
        int middowncounter = 0;
        int counter = 0;
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
//            if (cp == null || cp.getSignificantTrindCategory() == -1) {
//                emptyCounter++;
//            } else if (cp.getSignificantTrindCategory() > 0) {
//                upcounter++;
//            } else if (cp.getSignificantCellValue() == 0) {
//                notcounter++;
//            } else if (cp.getSignificantCellValue() < 0) {
//                downcounter++;
//            }
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
                yLineValues[compIndex] = 2d;
                xEmptyValues[emptyIndex] = comparisonIndexer;
                yEmptyValues[emptyIndex] = 2d;
                emptyIndex++;
//                continue;
            } //            else {
            else if (cp.getSignificantTrindCategory() == 5) {
                yLineValues[compIndex] = 2d;
                xNoValueProvided[noValueProvidedIndex] = comparisonIndexer;
                yNoValueProvided[noValueProvidedIndex] = 2d;
                noValueProvidedIndex++;
            } else if (cp.getSignificantTrindCategory() == 4) {
                yLineValues[compIndex] = 4d;
                xUpValues[upIndex] = comparisonIndexer;
                yUpValues[upIndex] = 4d;
                upIndex++;
            } else if (cp.getSignificantTrindCategory() == 3) {
                xMidUpValues[midupIndex] = comparisonIndexer;
                yMidUpValues[midupIndex] = 3d;
                midupIndex++;
                yLineValues[compIndex] = 3d;
            } else if (cp.getSignificantTrindCategory() == 2) {
                yLineValues[compIndex] = 2d;
                xStabletValues[notIndex] = comparisonIndexer;
                yStableValues[notIndex] = 2d;
                notIndex++;
            } else if (cp.getSignificantTrindCategory() == 1) {
                yLineValues[compIndex] = 1d;
                xMidDownValues[middownIndex] = comparisonIndexer;
                yMidDownValues[middownIndex] = 1d;
                middownIndex++;
            } else if (cp.getSignificantTrindCategory() == 0) {
                yLineValues[compIndex] = 0d;
                xDownValues[downIndex] = comparisonIndexer;
                yDownValues[downIndex] = 0d;
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
        dataset.addSeries("stable", stablevalues);
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
        final Color[] labelsColor = new Color[]{new Color(80, 183, 71), Color.LIGHT_GRAY, new Color(1, 141, 244), Color.LIGHT_GRAY, Color.RED};
        final String[] tickLabels = new String[]{"Low", " ", "Stable", " ", "High"};
        SymbolAxis yAxis = new SymbolAxis(null, tickLabels) {

            int x = 0;

            @Override
            public Paint getTickLabelPaint() {
                if (x >= labelsColor.length) {
                    x = 0;
                }
                return labelsColor[x++];
            }

            @Override
            protected void drawGridBandsVertical(Graphics2D g2, Rectangle2D drawArea, Rectangle2D plotArea, boolean firstGridBandIsDark, List ticks) {
                List udatedTicksList = new ArrayList();
                for (Object tick : ticks) {
                    if (tick.toString().equalsIgnoreCase(tickLabels[custTrend])) {
                        udatedTicksList.add(tick);
                    }
                }
                int factor = (int) ((plotArea.getHeight() / 5) * 0.25);

                Rectangle2D up = new Rectangle((int) drawArea.getX(), (int) drawArea.getY() - factor, (int) drawArea.getWidth(), (int) drawArea.getHeight());
                Rectangle2D pa = new Rectangle((int) plotArea.getX(), (int) plotArea.getY() - factor, (int) plotArea.getWidth(), (int) plotArea.getHeight());

                super.drawGridBandsVertical(g2, up, pa, firstGridBandIsDark, udatedTicksList); //To change body of generated methods, choose Tools | Templates.
            }

        };

        yAxis.setTickLabelFont(font);
        xAxis.setGridBandsVisible(false);
        yAxis.setGridBandsVisible(false);

        yAxis.setAxisLinePaint(Color.WHITE);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.GRAY);
        Color[] dataColor = new Color[]{new Color(0, 153, 0), new Color(0, 229, 132), new Color(1, 141, 244), new Color(247, 119, 119), new Color(204, 0, 0), Color.decode("#b5babb"),new Color(204, 204, 204)};

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

        XYPlot xyplot = new XYPlot(dataset, xAxis, yAxis, renderer) {

            @Override
            public void drawRangeTickBands(Graphics2D g2, Rectangle2D dataArea, List ticks) {

                if (custTrend == -1) {
                    super.drawRangeTickBands(g2, dataArea, ticks);
                    return;

                }
                List udatedTicksList = new ArrayList();
                for (Object tick : ticks) {
                    if (tick.toString().equalsIgnoreCase(tickLabels[custTrend])) {
                        udatedTicksList.add(tick);
                    }
                }
                Rectangle2D up;
                int factor = (int) ((dataArea.getHeight() / 5) * 0.25);
                if (custTrend == 4) {
                    up = new Rectangle((int) dataArea.getX(), (int) dataArea.getY() + factor, (int) dataArea.getWidth(), (int) dataArea.getHeight());

                } else if (custTrend == 2) {
                    up = new Rectangle((int) dataArea.getX(), (int) dataArea.getY() - factor, (int) dataArea.getWidth(), (int) dataArea.getHeight());

                } else {
                    up = new Rectangle((int) dataArea.getX(), (int) dataArea.getY() - factor, (int) dataArea.getWidth(), (int) dataArea.getHeight());
                }

                super.drawRangeTickBands(g2, up, udatedTicksList); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public Paint getRangeTickBandPaint() {
                return super.getRangeTickBandPaint(); //To change body of generated methods, choose Tools | Templates.
            }

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

            private final Color[] labelsColor =     new Color[]{new Color(204, 0, 0),new Color(247, 119, 119),   new Color(1, 141, 244), new Color(0, 229, 132), new Color(0, 153, 0),Color.decode("#b5babb"),  Color.WHITE};
            private final Color[] labelsOutColor =  new Color[]{new Color(204, 0, 0),new Color(247, 119, 119),   new Color(1, 141, 244), new Color(0, 229, 132) ,new Color(0, 153, 0),  Color.GRAY,Color.GRAY ,  new Color(0, 229, 132)};

            private final Font font = new Font("Verdana", Font.PLAIN, 11);
            private final String[] labels = new String[]{"High  100%","High < 100%", "Stable", "Low < 100%", "Low  100%",  "No Quant. Info.", "No Data"};
            private final Shape[] shapes = new Shape[]{upArr, upArr,notRShape,downArr, downArr,  emptyRShape,  emptyRShape, square};

            @Override
            public LegendItemCollection getLegendItems() {
                LegendItemCollection legendItemCollection = new LegendItemCollection();
                for (int i = 0; i < labelsColor.length; i++) {
                    LegendItem item = new LegendItem(labels[i], "", "", "", shapes[i], labelsColor[i]) {

                        @Override
                        public boolean isShapeOutlineVisible() {
                            return true; //To change body of generated methods, choose Tools | Templates.
                        }

                    };

                    item.setOutlinePaint(labelsOutColor[i]);

//                    item.setLabelPaint(Color.GRAY);
                    item.setLabelFont(font);

                    legendItemCollection.add(item);

                }
                if (custTrend != -1) {
                    LegendItem item = new LegendItem("User Data", "", "", "", shapes[shapes.length - 1], customizedUserDataColor[custTrend]) {
                        @Override
                        public boolean isShapeOutlineVisible() {
                            return true; //To change body of generated methods, choose Tools | Templates.
                        }
                    };
                    legendItemCollection.add(item);

                }

                return legendItemCollection;//To change body of generated methods, choose Tools | Templates.
            }

        };
        if (custTrend != -1) {
            yAxis.setGridBandsVisible(true);
            if (custTrend == 4) {
                yAxis.setGridBandPaint(Color.WHITE);
                xyplot.setRangeTickBandPaint(customizedUserDataColor[4]);
            } else if (custTrend == 0) {
                yAxis.setGridBandPaint(customizedUserDataColor[0]);
                xyplot.setRangeTickBandPaint(Color.WHITE);
            } else if (custTrend == 2) {
                yAxis.setGridBandPaint(customizedUserDataColor[2]);
                xyplot.setRangeTickBandPaint(Color.WHITE);
            }

        } else {
            yAxis.setGridBandsVisible(false);
        }

//        xyplot.setRangeTickBandPaint(Color.WHITE);
        JFreeChart jFreeChart = new JFreeChart(null, new Font("Verdana", Font.PLAIN, 18), xyplot, true);
        jFreeChart.setBackgroundPaint(Color.WHITE);
        final XYPlot plot = jFreeChart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
//        plot.setRangeTickBandPaint(Color.ORANGE);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        plot.setOutlinePaint(Color.GRAY);
        jFreeChart.setBorderVisible(false);
        LegendTitle legend = jFreeChart.getLegend();
        legend.setVisible(true);
        legend.setMargin(20, 0, 20, 0);
//        legend.setBorder(1, 1, 1, 1);
        legend.setFrame(new BlockBorder(0, 0, 0, 0, Color.LIGHT_GRAY));
        legend.setHorizontalAlignment(HorizontalAlignment.RIGHT);

//        JfreeExporter exporter = new JfreeExporter();
//        exporter.writeChartToPDFFile(jFreeChart, 595, 842, "line" + teststyle + ".pdf");
        return jFreeChart;
    }

    private final String[] tooltipLabels = new String[]{"( Low <img src='VAADIN/themes/dario-theme/img/sdown.png' alt='Low'>" + " )", "( Low <img src='VAADIN/themes/dario-theme/img/sdown.png' alt='Low'>" + " )", "( Stable <img src='VAADIN/themes/dario-theme/img/snotreg.png' alt='Stable'>" + " )", " ( High <img src='VAADIN/themes/dario-theme/img/sup.png' alt='High'>" + " )", " ( High <img src='VAADIN/themes/dario-theme/img/sup.png' alt='High'>" + " )"};

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
                    String updatedHeader = groupCompTitle.split(" / ")[0].split("\n")[0] + " / " + groupCompTitle.split(" / ")[1].split("\n")[0] + " ( " + groupCompTitle.split(" / ")[1].split("\n")[1] + " )";

                    square.setDescription("<h2>" + updatedHeader + "" + tooltipLabels[trend] + "" + "</h2>" + "<h3>" + protLayout.toString() + "</h3>");
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

//                if (paramName.equalsIgnoreCase("GroupsComparison")) {
//                    MouseEvents.MouseOverListener mouseOverListener = new MouseEvents.MouseOverListener() {
//                        private final QuantDiseaseGroupsComparison gc = inUseComparisonProteins[((XYItemEntity) entity).getItem()].getComparison();
//
//                        @Override
//                        public void mouseOver() {
//                            studiesScatterChartsLayout.highlightComparison(gc, false);
//                        }
//                    };
//                    MouseEvents.MouseOutListener mouseOutListener = new MouseEvents.MouseOutListener() {
//                        @Override
//                        public void mouseOut() {
//                            studiesScatterChartsLayout.highlightComparison(null, false);
//                        }
//                    };
//                    final MouseEvents mouseEvents = MouseEvents.enableFor(square);
//                    mouseEvents.addMouseOutListener(mouseOutListener);
//                    mouseEvents.addMouseOverListener(mouseOverListener);
//                }
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
//        renderer.setSeriesShapesVisible(1, isFullChart);
//        renderer.setSeriesShapesVisible(2, isFullChart);
//        renderer.setSeriesShapesVisible(3, isFullChart);
        if (isFullChart) {

//             Color[] dataColor = new Color[]{new Color(0, 153, 0), new Color(0, 229, 132), new Color(1, 141, 244), new Color(255, 51, 51), new Color(204, 0, 0)};
//
//        renderer.setUseOutlinePaint(true);
//        renderer.setSeriesPaint(1,dataColor[4]);
//        renderer.setSeriesOutlinePaint(1, dataColor[4]);
//        
//          renderer.setSeriesPaint(2,dataColor[3]);
//        renderer.setSeriesOutlinePaint(2, dataColor[3]);
//       
//        renderer.setSeriesPaint(3, dataColor[2]);
//        renderer.setSeriesOutlinePaint(3, dataColor[2]);
//        
//        renderer.setSeriesPaint(4, dataColor[1]);
//        renderer.setSeriesOutlinePaint(4, dataColor[1]);
//        
//        renderer.setSeriesPaint(5, dataColor[0]);
//        renderer.setSeriesOutlinePaint(5,dataColor[0]);
//        
//        
//        
//        
//        renderer.setSeriesPaint(6, Color.WHITE);
//        renderer.setSeriesOutlinePaint(6, dataColor[2]);
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

            chart.getLegend().setVisible(true);

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
//            renderer.setSeriesShape(4, semptyRShape);
//            renderer.setSeriesShape(3, sleftArr);
//            renderer.setSeriesShape(2, snotRShape);
//            renderer.setSeriesShape(1, srightArr);
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

        if (orederingOptionGroup.getValue().toString().equalsIgnoreCase("Default order")) {
            if (defaultLineChartImgUrl.equalsIgnoreCase("")) {

                defaultLineChartImgUrl = this.generateChartImage(defaultChart, (width), chartHeight, defaultLineChartRenderingInfo, defaultLineChartContainer);
            }
            defaultLineChartContainer.setVisible(true);
            orderedLineChartContainer.setVisible(false);
            styles.add("." + teststyle + " {  background-image: url(" + defaultLineChartImgUrl + " );background-position:center; background-repeat: no-repeat; }");
            defaultLineChartContainer.setStyleName(teststyle);
        } else {
            if (orderedLineChartImg.equalsIgnoreCase("")) {
                orderedLineChartImg = this.generateChartImage(orderedChart, (width), chartHeight, orderedLineChartRenderingInfo, orderedLineChartContainer);
            }
            defaultLineChartContainer.setVisible(false);
            orderedLineChartContainer.setVisible(true);
            styles.add("." + teststyle + " {  background-image: url(" + orderedLineChartImg + " );background-position:center; background-repeat: no-repeat; }");
            orderedLineChartContainer.setStyleName(teststyle);
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
                if (orederingOptionGroup.getValue().toString().equalsIgnoreCase("Default order")) {
                    try {

                        set.add(defaultChart);
                        set.addAll(studiesScatterChartsLayout.getScatterCharts());
                        byte[] pdfFile = CSFPR_Handler.exportProteinsInfoCharts(set, "proteins_information_charts.pdf", proteinName, defaultPeptidesExportInfoSet);
                        return new ByteArrayInputStream(pdfFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }

                } else {
                    try {
                        set.add(orderedChart);
                        set.addAll(studiesScatterChartsLayout.getScatterCharts());
                        byte[] pdfFile = CSFPR_Handler.exportProteinsInfoCharts(set, "proteins_information_charts.pdf", proteinName, orderedPeptidesExportInfoSet);
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
                if (orederingOptionGroup.getValue().toString().equalsIgnoreCase("Default order")) {
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

                byte[] pdfFile = CSFPR_Handler.exportfullReportAsZip(chartsMap, "full_Report.pdf", proteinName, peptidesExportInfoSet);
                return new ByteArrayInputStream(pdfFile);
//               

            }
        }, "full_Report.pdf");
    }

}
