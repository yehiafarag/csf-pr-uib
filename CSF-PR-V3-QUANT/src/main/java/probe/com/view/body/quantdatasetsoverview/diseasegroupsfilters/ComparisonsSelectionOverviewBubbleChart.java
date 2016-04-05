/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
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
import java.util.logging.Level;
import java.util.logging.Logger;
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
import probe.com.handlers.CSFPRHandler;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
import probe.com.model.beans.quant.QuantProtein;
import probe.com.selectionmanager.CSFFilter;
import probe.com.selectionmanager.QuantCentralManager;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.DiseaseGroupsComparisonsProteinLayout;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.TrendLegend;
import probe.com.view.core.InfoPopupBtn;
import probe.com.view.core.jfreeutil.SquaredDot;

/*
 * @author Yehia Farag
 */
public class ComparisonsSelectionOverviewBubbleChart extends VerticalLayout implements CSFFilter, LayoutEvents.LayoutClickListener {

    private String defaultImgURL = "";
    private final ChartRenderingInfo chartRenderingInfo = new ChartRenderingInfo();
    private final QuantCentralManager Quant_Central_Manager;
    private int width;
    private final int height;
    private final CSFPRHandler CSFPR_Handler;
    private final VerticalLayout emptySelectionLayout;
    private JFreeChart chart;
    private final AbsoluteLayout chartLayout = new AbsoluteLayout();
     private final AbsoluteLayout chartLayoutContainer = new AbsoluteLayout();
    private final HorizontalLayout bottomLayout = new HorizontalLayout();
    private final HorizontalLayout topLayout = new HorizontalLayout();
    private final Button exportPdfBtn;
    private final List<QuantProtein> searchQuantificationProtList;
    private boolean isNewImge = true;
    private byte imageData[];
    private final String[] tooltipLabels = new String[]{"", " Decreased <img src='VAADIN/themes/dario-theme/img/greendot.png' alt='Decreased'>" + " ", " Decreased <img src='VAADIN/themes/dario-theme/img/lgreendot.png' alt='Decreased'>" + " ", " Equal <img src='VAADIN/themes/dario-theme/img/bluedot.png' alt='Equal'>" + " ", "  Increased <img src='VAADIN/themes/dario-theme/img/lreddot.png' alt='Increased'>" + " ", "  Increased <img src='VAADIN/themes/dario-theme/img/reddot.png' alt='Increased'>" + " ", ""};
    private final Map<String, Color> diseaseColorMap = new HashMap<String, Color>();
    private QuantDiseaseGroupsComparison userCustomizedComparison;
    private Color stableColor;
    private boolean activeMultiSelect = false;
    private final Image chartImage = new Image();

    public void updateSize(int updatedWidth, int height) {
        if (updatedWidth < 810) {
            this.setVisible(false);
        } else {
            this.setVisible(true);
        }
        height = 600;
        width = updatedWidth;
        this.setWidth(updatedWidth + "px");

        this.chartLayout.setWidth(updatedWidth + "px");
        emptySelectionLayout.setWidth(updatedWidth + "px");
        this.chartLayout.setHeight(height + "px");
          this.chartLayoutContainer.setWidth(updatedWidth + "px");
        this.chartLayoutContainer.setHeight(height + "px");
        if (chart == null) {
            return;
        }
        defaultImgURL = saveToFile(chart, updatedWidth, height);

        this.redrawChart();

    }

    private final Map<String, double[]> tooltipsProtNumberMap = new HashMap<String, double[]>();
    private final  GroupSwichBtn groupSwichBtn;

    public ComparisonsSelectionOverviewBubbleChart(final QuantCentralManager Quant_Central_Manager, final CSFPRHandler CSFPR_Handler, int chartWidth, int chartHeight, Set<QuantDiseaseGroupsComparison> selectedComparisonList, List<QuantProtein> searchQuantificationProtList) {

        userDataCounter = 0;
        this.searchQuantificationProtList = searchQuantificationProtList;
        Map<String, String> diseaseHashedColorMap = Quant_Central_Manager.getDiseaseHashedColorMap();
        for (String str : diseaseHashedColorMap.keySet()) {
            diseaseColorMap.put(str, Color.decode(diseaseHashedColorMap.get(str)));
        }
        this.width = chartWidth;
        this.height = 600;
        this.CSFPR_Handler = CSFPR_Handler;
        this.setWidth(width + "px");
        this.setHeightUndefined();
        this.Quant_Central_Manager = Quant_Central_Manager;
        this.Quant_Central_Manager.registerStudySelectionListener(ComparisonsSelectionOverviewBubbleChart.this);
        this.setSpacing(true);

        //init toplayout
        topLayout.setHeight(30 + "px");
        topLayout.setSpacing(true);
        topLayout.setMargin(new MarginInfo(false, false, true, false));
        this.addComponent(topLayout);

        Label overviewLabel = new Label("<font style='margin-left :50px;'>Overview</font> ");
        overviewLabel.setContentMode(ContentMode.HTML);
        topLayout.addComponent(overviewLabel);
        overviewLabel.setStyleName("subtitle");
        overviewLabel.setWidth("120px");

        InfoPopupBtn info = new InfoPopupBtn("The bubble chart give an overview for the proteins existed in the selected comparisons.<br/>The diameter of the bubble represents the number of the proteins in the selected comparison and the color represents the trend<br/>");
        info.setWidth("16px");
        info.setHeight("16px");
        topLayout.addComponent(info);
        this.topLayout.setVisible(false);

        //end of toplayout
        //init chartlayout
        
        this.chartLayoutContainer.setVisible(false);
        this.addComponent(chartLayoutContainer);
        chartLayoutContainer.setWidth(width + "px");
        chartLayoutContainer.setHeight(height + "px");
        
        
        chartLayoutContainer.addComponent(chartImage,"left: " + 0 + "px; top: " + 0 + "px;");
        chartLayoutContainer.addComponent(chartLayout,"left: " + 0 + "px; top: " + 0 + "px;");
        chartLayout.setWidth(width + "px");
        chartLayout.setHeight(height + "px");
        chartLayout.addLayoutClickListener(ComparisonsSelectionOverviewBubbleChart.this);

        //end of chartlayout
        //init bottomlayout 
        bottomLayout.setWidth("100%");
        this.addComponent(bottomLayout);
        this.setComponentAlignment(bottomLayout, Alignment.BOTTOM_RIGHT);
        bottomLayout.setVisible(false);
        HorizontalLayout btnContainerLayout = new HorizontalLayout();
        btnContainerLayout.setSpacing(true);
//        btnContainerLayout.setMargin(new MarginInfo(false, false, false, false));
        btnContainerLayout.setWidthUndefined();
        btnContainerLayout.setHeightUndefined();
//        btnContainerLayout.addStyleName("leftspacer");
        bottomLayout.addComponent(btnContainerLayout);
        bottomLayout.setComponentAlignment(btnContainerLayout, Alignment.TOP_RIGHT);

        TrendLegend legendLayout = new TrendLegend("bubblechart");
        legendLayout.setWidthUndefined();
        legendLayout.setHeight("24px");
        btnContainerLayout.addComponent(legendLayout);
        btnContainerLayout.setComponentAlignment(legendLayout, Alignment.TOP_RIGHT);
//        btnContainerLayout.setExpandRatio(legendLayout, 600);
//        btnContainerLayout.setExpandRatio(btnContainerLayout, 210);

//         VerticalLayout stableBtnWrapper = new VerticalLayout();
////        stableBtnWrapper.setWidth("64px");
//        HorizontalLayout stableBtn = new HorizontalLayout();
//        stableBtnWrapper.addComponent(stableBtn);
//        stableBtnWrapper.setComponentAlignment(stableBtn, Alignment.TOP_LEFT);
//        btnContainerLayout.addComponent(stableBtnWrapper);
        
         groupSwichBtn = new GroupSwichBtn(Quant_Central_Manager,searchQuantificationProtList);
         btnContainerLayout.addComponent(groupSwichBtn);
        
        
        final VerticalLayout appliedIcon = new VerticalLayout();
        appliedIcon.setStyleName("appliedicon");
        appliedIcon.setWidth("24px");
        appliedIcon.setHeight("24px");
        appliedIcon.setDescription("Hide stable proteins");
        btnContainerLayout.addComponent(appliedIcon);
//        stableBtn.setStyleName("stablebtn");
//        stableBtn.setHeight("24px");
//        Label stableLabel = new Label("Equal");
//        stableLabel.setWidth("44px");
//        stableBtn.addComponent(stableLabel);

        appliedIcon.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                if (appliedIcon.getStyleName().equalsIgnoreCase("appliedicon")) {
                    appliedIcon.setStyleName("unappliedicon");
                    Quant_Central_Manager.updateSignificantOnlySelection(true);
                    appliedIcon.setDescription("Show stable proteins");
                } else {
                    appliedIcon.setStyleName("appliedicon");
                    Quant_Central_Manager.updateSignificantOnlySelection(false);
                    appliedIcon.setDescription("Hide stable proteins");
                }
            }
        });

        exportPdfBtn = new Button("");
        exportPdfBtn.setWidth("24px");
        exportPdfBtn.setHeight("24px");
        exportPdfBtn.setPrimaryStyleName("exportpdfbtn");
        exportPdfBtn.setDescription("Export chart image");
        StreamResource myResource = createResource();
        FileDownloader fileDownloader = new FileDownloader(myResource);
        fileDownloader.extend(exportPdfBtn);
        btnContainerLayout.addComponent(exportPdfBtn);

        VerticalLayout unselectAllBtn = new VerticalLayout();
        unselectAllBtn.setStyleName("unselectallbtn");
        btnContainerLayout.addComponent(unselectAllBtn);
        btnContainerLayout.setComponentAlignment(unselectAllBtn, Alignment.TOP_LEFT);
        unselectAllBtn.setDescription("Clear selection");
        unselectAllBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                Quant_Central_Manager.setBubbleChartQuantProteinsSelection(new HashSet<String>(), "");
                resetChart();

            }
        });

        final VerticalLayout selectMultiBtn = new VerticalLayout();
        selectMultiBtn.setStyleName("selectmultibtn");
        btnContainerLayout.addComponent(selectMultiBtn);
        btnContainerLayout.setComponentAlignment(selectMultiBtn, Alignment.TOP_LEFT);
        selectMultiBtn.setDescription("Multiple selection");
        selectMultiBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                if (selectMultiBtn.getStyleName().equalsIgnoreCase("selectmultiselectedbtn")) {
                    selectMultiBtn.setStyleName("selectmultibtn");
                    activeMultiSelect = false;

                } else {
                    selectMultiBtn.setStyleName("selectmultiselectedbtn");
                    activeMultiSelect = true;

                }
            }
        });

        //end of btns layout
        //init empty layout
        emptySelectionLayout = new VerticalLayout();
        this.addComponent(emptySelectionLayout);
        emptySelectionLayout.setWidth(100 + "%");
        emptySelectionLayout.setHeightUndefined();

        VerticalLayout spacer = new VerticalLayout();
        spacer.setHeight("100px");
        spacer.setWidth("10px");
        spacer.setStyleName(Reindeer.LAYOUT_WHITE);
        emptySelectionLayout.addComponent(spacer);        
        emptySelectionLayout.setComponentAlignment(spacer, Alignment.BOTTOM_RIGHT);

        Label startLabel = new Label("<center><h2 style='color:gray;'><b>Select comparison from the table</b></h2></center>");
        startLabel.setContentMode(ContentMode.HTML);

        emptySelectionLayout.addComponent(startLabel);
        emptySelectionLayout.setComponentAlignment(startLabel, Alignment.MIDDLE_CENTER);

        Image handleft = new Image();
        handleft.setSource(new ThemeResource("img/handleft.png"));
        emptySelectionLayout.addComponent(handleft);
        emptySelectionLayout.setComponentAlignment(handleft, Alignment.MIDDLE_CENTER);

        //init bubble chart
    }

    private final int userDataCounter;

    public ComparisonsSelectionOverviewBubbleChart(final QuantCentralManager Quant_Central_Manager, final CSFPRHandler CSFPR_Handler, int chartWidth, int chartHeight, Set<QuantDiseaseGroupsComparison> selectedComparisonList, List<QuantProtein> searchQuantificationProtList, QuantDiseaseGroupsComparison userCustomizedComparison) {
        this.userCustomizedComparison = userCustomizedComparison;
        userDataCounter = 1;
        this.searchQuantificationProtList = searchQuantificationProtList;
        Map<String, String> diseaseHashedColorMap = Quant_Central_Manager.getDiseaseHashedColorMap();
        for (String str : diseaseHashedColorMap.keySet()) {
            diseaseColorMap.put(str, Color.decode(diseaseHashedColorMap.get(str)));
        }
        this.width = chartWidth;
        this.height = 600;
        this.CSFPR_Handler = CSFPR_Handler;
        this.setWidth(width + "px");
        this.setHeightUndefined();
        this.Quant_Central_Manager = Quant_Central_Manager;
        this.Quant_Central_Manager.registerStudySelectionListener(ComparisonsSelectionOverviewBubbleChart.this);
        this.setSpacing(true);

        //init toplayout
        topLayout.setHeight(30 + "px");
        topLayout.setSpacing(true);
        topLayout.setMargin(new MarginInfo(false, false, true, false));
        this.addComponent(topLayout);

        Label overviewLabel = new Label("<font style='margin-left :50px;'>Overview</font> ");
        overviewLabel.setContentMode(ContentMode.HTML);
        topLayout.addComponent(overviewLabel);
        overviewLabel.setStyleName("subtitle");
        overviewLabel.setWidth("120px");

        InfoPopupBtn info = new InfoPopupBtn("The bubble chart give an overview for the proteins existed in the selected comparisons.<br/>The diameter of the bubble represents the number of the proteins in the selected comparison and the color represents the trend<br/>");
        info.setWidth("16px");
        info.setHeight("16px");
        topLayout.addComponent(info);

        //end of toplayout
        //init chartlayout
        this.chartLayoutContainer.setVisible(false);
        this.addComponent(chartLayoutContainer);
        
        
        this.addComponent(chartLayoutContainer);
        chartLayoutContainer.setWidth(width + "px");
        chartLayoutContainer.setHeight(height + "px");
        
        
        chartLayoutContainer.addComponent(chartImage,"left: " + 0 + "px; top: " + 0 + "px;");
        chartLayoutContainer.addComponent(chartLayout,"left: " + 0 + "px; top: " + 0 + "px;");
        chartLayout.setWidth(width + "px");
        chartLayout.setHeight(height + "px");
        chartLayout.addLayoutClickListener(ComparisonsSelectionOverviewBubbleChart.this);
       

        //end of chartlayout
        //init bottomlayout 
        bottomLayout.setWidth("100%");
        this.addComponent(bottomLayout);
        this.setComponentAlignment(bottomLayout, Alignment.BOTTOM_RIGHT);
        bottomLayout.setVisible(false);
        HorizontalLayout btnContainerLayout = new HorizontalLayout();
        btnContainerLayout.setSpacing(true);
//        btnContainerLayout.setMargin(new MarginInfo(false, false, false, false));
        btnContainerLayout.setWidthUndefined();
        btnContainerLayout.setHeightUndefined();
//        btnContainerLayout.addStyleName("leftspacer");
        bottomLayout.addComponent(btnContainerLayout);
        bottomLayout.setComponentAlignment(btnContainerLayout, Alignment.TOP_RIGHT);

        TrendLegend legendLayout = new TrendLegend("bubblechart");
        legendLayout.setWidthUndefined();
        legendLayout.setHeight("24px");
        btnContainerLayout.addComponent(legendLayout);
        btnContainerLayout.setComponentAlignment(legendLayout, Alignment.TOP_RIGHT);
//        btnContainerLayout.setExpandRatio(legendLayout, 600);
//        btnContainerLayout.setExpandRatio(btnContainerLayout, 210);

//         VerticalLayout stableBtnWrapper = new VerticalLayout();
////        stableBtnWrapper.setWidth("64px");
//        HorizontalLayout stableBtn = new HorizontalLayout();
//        stableBtnWrapper.addComponent(stableBtn);
//        stableBtnWrapper.setComponentAlignment(stableBtn, Alignment.TOP_LEFT);
//        btnContainerLayout.addComponent(stableBtnWrapper);
        
        groupSwichBtn = new GroupSwichBtn(Quant_Central_Manager,searchQuantificationProtList);
         btnContainerLayout.addComponent(groupSwichBtn);
        final VerticalLayout appliedIcon = new VerticalLayout();
        appliedIcon.setStyleName("appliedicon");
        appliedIcon.setWidth("24px");
        appliedIcon.setHeight("24px");
        appliedIcon.setDescription("Hide stable proteins");
        btnContainerLayout.addComponent(appliedIcon);
//        stableBtn.setStyleName("stablebtn");
//        stableBtn.setHeight("24px");
//        Label stableLabel = new Label("Equal");
//        stableLabel.setWidth("44px");
//        stableBtn.addComponent(stableLabel);

        appliedIcon.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                if (appliedIcon.getStyleName().equalsIgnoreCase("appliedicon")) {
                    appliedIcon.setStyleName("unappliedicon");
                    Quant_Central_Manager.updateSignificantOnlySelection(true);
                    appliedIcon.setDescription("Show stable proteins");
                } else {
                    appliedIcon.setStyleName("appliedicon");
                    Quant_Central_Manager.updateSignificantOnlySelection(false);
                    appliedIcon.setDescription("Hide stable proteins");
                }
            }
        });

        exportPdfBtn = new Button("");
        exportPdfBtn.setWidth("24px");
        exportPdfBtn.setHeight("24px");
        exportPdfBtn.setPrimaryStyleName("exportpdfbtn");
        exportPdfBtn.setDescription("Export chart image");
        StreamResource myResource = createResource();
        FileDownloader fileDownloader = new FileDownloader(myResource);
        fileDownloader.extend(exportPdfBtn);
        btnContainerLayout.addComponent(exportPdfBtn);

        VerticalLayout unselectAllBtn = new VerticalLayout();
        unselectAllBtn.setStyleName("unselectallbtn");
        btnContainerLayout.addComponent(unselectAllBtn);
        btnContainerLayout.setComponentAlignment(unselectAllBtn, Alignment.TOP_LEFT);
        unselectAllBtn.setDescription("Clear selection");
        unselectAllBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                Quant_Central_Manager.setBubbleChartQuantProteinsSelection(new HashSet<String>(), "");
                resetChart();

            }
        });

        final VerticalLayout selectMultiBtn = new VerticalLayout();
        selectMultiBtn.setStyleName("selectmultibtn");
        btnContainerLayout.addComponent(selectMultiBtn);
        btnContainerLayout.setComponentAlignment(selectMultiBtn, Alignment.TOP_LEFT);
        selectMultiBtn.setDescription("Multiple selection");
        selectMultiBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                if (selectMultiBtn.getStyleName().equalsIgnoreCase("selectmultiselectedbtn")) {
                    selectMultiBtn.setStyleName("selectmultibtn");
                    activeMultiSelect = false;

                } else {
                    selectMultiBtn.setStyleName("selectmultiselectedbtn");
                    activeMultiSelect = true;

                }
            }
        });

        //end of btns layout
        //init empty layout
        emptySelectionLayout = new VerticalLayout();
        this.addComponent(emptySelectionLayout);
        emptySelectionLayout.setWidth(width + "px");
        emptySelectionLayout.setHeightUndefined();

        VerticalLayout spacer = new VerticalLayout();
        spacer.setHeight("100px");
        spacer.setWidth("10px");
        spacer.setStyleName(Reindeer.LAYOUT_WHITE);
        emptySelectionLayout.addComponent(spacer);
        emptySelectionLayout.setComponentAlignment(spacer, Alignment.BOTTOM_RIGHT);

        Label startLabel = new Label("<center><h2 style='color:gray;'><b>Select comparison from the table</b></h2></center>");
        startLabel.setContentMode(ContentMode.HTML);

        emptySelectionLayout.addComponent(startLabel);
        emptySelectionLayout.setComponentAlignment(startLabel, Alignment.MIDDLE_CENTER);

        Image handleft = new Image();
        handleft.setSource(new ThemeResource("img/handleft.png"));
        emptySelectionLayout.addComponent(handleft);
        emptySelectionLayout.setComponentAlignment(handleft, Alignment.MIDDLE_CENTER);

    }

    private StreamResource createResource() {
        return new StreamResource(new StreamSource() {
            @Override
            @SuppressWarnings("CallToPrintStackTrace")
            public InputStream getStream() {
                try {
                    byte[] pdfFile = CSFPR_Handler.exportBubbleChartAsPdf(chart, "bubblechart_comparisons_selection.pdf", "Overview",(int)chartLayoutContainer.getWidth(),(int)chartLayoutContainer.getHeight());
                    return new ByteArrayInputStream(pdfFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }
        }, "bubblechart_comparisons_selection.pdf");
    }

    private JFreeChart updateBubbleChartChart(Set<QuantDiseaseGroupsComparison> selectedComparisonList) {

        if (userCustomizedComparison != null) {
            return updateBubbleChartChartWithCustUserData(selectedComparisonList);
        }
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

                    if (qp.getSignificantTrindCategory() == 2 || qp.getSignificantTrindCategory() == 5) {
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
        Color[] dataColor = new Color[]{Color.WHITE, new Color(0, 153, 0), new Color(0, 229, 132), stableColor, new Color(247, 119, 119), new Color(204, 0, 0), Color.WHITE};

          double[] yAxisValueI = new double[]{0,0,0,0,0,0,0};
          double[] xAxisValueI = new double[]{0,0,0,0,0,0,0};
          double[] widthValueI = new double[]{0,0,0,0,0,0,0};
          double[][] seriesValuesI = {yAxisValueI, xAxisValueI, widthValueI}; 
          seriousColorMap.put(0, new Color[]{Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE});
          defaultxyzdataset.addSeries("   ", seriesValuesI);
        
        
        for (QuantDiseaseGroupsComparison qc : selectedComparisonList) {

            double[] tempWidthValue = new double[8];
            if (qc.getComparProtsMap() == null) {
                continue;
            }

            for (String key : qc.getComparProtsMap().keySet()) {
                qc.getComparProtsMap().get(key).updateLabelLayout();

                if (significantOnly && (qc.getComparProtsMap().get(key).getSignificantTrindCategory() == 2 || qc.getComparProtsMap().get(key).getSignificantTrindCategory() == 5)) {
                    tempWidthValue[3] = 0;
                    tempWidthValue[6] = 0;
                } else {
                    tempWidthValue[qc.getComparProtsMap().get(key).getSignificantTrindCategory() + 1] = tempWidthValue[qc.getComparProtsMap().get(key).getSignificantTrindCategory() + 1] + 1;
                }
            }

            if (tempWidthValue[3] > 0 && tempWidthValue[3] >= 0) {
                stableColor = new Color(1, 141, 244);

            } else {
                stableColor = Color.decode("#b5babb");

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
                    yAxisValue[length] = counter+1;
                    widthValue[length] = tempWidthValue[x];
                    serColorArr[length] = dataColor[x];
                    length++;
                }

            }

            if (length == 1 && selectedComparisonList.size() == 1) {
                widthValue[0] = 1;
            }
            seriousColorMap.put(counter+1, serColorArr);

            double[][] seriesValues = {yAxisValue, xAxisValue, widthValue};
            defaultxyzdataset.addSeries(qc.getComparisonHeader(), seriesValues);
            counter++;
        }
           double[] yAxisValueII = new double[0];
            double[] xAxisValueII = new double[0];
            double[] widthValueII = new double[0];
              seriousColorMap.put(counter+1, new Color[]{});
             double[][] seriesValuesII = {yAxisValueII, xAxisValueII, widthValueII};            
         defaultxyzdataset.addSeries(" ", seriesValuesII);
        

        final Color[] labelsColor = new Color[]{Color.LIGHT_GRAY, new Color(80, 183, 71), Color.LIGHT_GRAY, new Color(1, 141, 244), Color.LIGHT_GRAY, new Color(204, 0, 0), Color.LIGHT_GRAY};
        Font font = new Font("Verdana", Font.BOLD, 13);
        SymbolAxis yAxis = new SymbolAxis(null, new String[]{"  ", "Decreased", " ", "Equal", " ", "Increased", "  "}) {

            int i = 0;

            @Override
            public RectangleInsets getTickLabelInsets() {
//                System.out.println("at ---- super.getTickLabelInsets() " + super.getTickLabelInsets());
//                if (i == 0) {
//                    i++;
//                    return new RectangleInsets(-5, -5, 0, 0);
//                }else                   
//                
                return super.getTickLabelInsets(); //To change body of generated methods, choose Tools | Templates.
            }

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

        String[] xAxisLabels = new String[selectedComparisonList.size()+2];
        int x = 0;
        xAxisLabels[x]="";
        int maxLength = -1;
        //init labels color

        final Color[] diseaseGroupslabelsColor = new Color[selectedComparisonList.size()+2];
        diseaseGroupslabelsColor[x]=Color.WHITE;
        x++;
        
        for (QuantDiseaseGroupsComparison comp : selectedComparisonList) {
            String header = comp.getComparisonHeader();
            String updatedHeader = header.split(" / ")[0].split("\n")[0] + " / " + header.split(" / ")[1].split("\n")[0] + "";

            xAxisLabels[x] = updatedHeader + " (" + comp.getDatasetIndexes().length + ")    ";
            if (xAxisLabels[x].length() > maxLength) {
                maxLength = xAxisLabels[x].length();
            }
            diseaseGroupslabelsColor[x] = diseaseColorMap.get(header.split(" / ")[0].split("\n")[1]);
            x++;

        }
         xAxisLabels[x]="";
         diseaseGroupslabelsColor[x]=Color.WHITE;

        SymbolAxis xAxis;
        final boolean finalNum;
        finalNum = maxLength > 30 && selectedComparisonList.size() > 4;

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
        xAxis.setUpperBound(diseaseGroupslabelsColor.length-1);

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

//            private final Color[] labelsColor = new Color[]{new Color(0, 153, 0), new Color(0, 229, 132), new Color(1, 141, 244), new Color(255, 51, 51), new Color(204, 0, 0), Color.decode("#b5babb")};
//
//            private final Font font = new Font("Verdana", Font.PLAIN, 12);
//            private final String[] labels = new String[]{"Decreased 100%", "Decreased <100% ", "Equal", " Increased <100%", "Increased 100%", "No Quant. Info."};
//
//            @Override
//            public LegendItemCollection getLegendItems() {
//                LegendItemCollection legendItemCollection = new LegendItemCollection();
//                for (int i = 0; i < labelsColor.length; i++) {
//                    LegendItem item = new LegendItem(labels[i], labelsColor[i]);
//                    item.setLabelFont(font);
//
//                    legendItemCollection.add(item);
//
//                }
//
//                return legendItemCollection;//To change body of generated methods, choose Tools | Templates.
//            }
        };

        JFreeChart generatedChart = new JFreeChart(xyplot) {

        };

        xyplot.setOutlineVisible(
                false);
        LegendTitle legend = generatedChart.getLegend();

        legend.setVisible(
                false);
//        legend.setMargin(20, 0, 0, 0);
////        legend.setBorder(1, 1, 1, 1);
//        legend.setFrame(new BlockBorder(1, 0, 1, 0, Color.LIGHT_GRAY));

//        generatedChart.removeLegend();
//        xyplot.setForegroundAlpha(0.65F);
        xyplot.setBackgroundPaint(Color.WHITE);

        generatedChart.setBackgroundPaint(Color.WHITE);

        generatedChart.setPadding(
                new RectangleInsets(0, 0, 0, 0));
        Quant_Central_Manager.setProteinsOverviewBubbleChart(generatedChart);
//        exporter.writeChartToPDFFile(generatedChart, 595, 842, "bublechart.pdf");
        return generatedChart;

    }

    private JFreeChart updateBubbleChartChartWithCustUserData(Set<QuantDiseaseGroupsComparison> selectedComparisonList) {

        tooltipsProtNumberMap.clear();
        DefaultXYZDataset defaultxyzdataset = new DefaultXYZDataset();
        int counter = 0;
        int upper = -1;
        boolean significantOnly = this.Quant_Central_Manager.isSignificantOnly();

        if (userCustomizedComparison.getComparProtsMap().size() > upper) {
            upper = userCustomizedComparison.getComparProtsMap().size();
        }

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
        Color[] dataColor = new Color[]{Color.WHITE, new Color(0, 153, 0), new Color(0, 229, 132), stableColor, new Color(247, 119, 119), new Color(204, 0, 0), Color.WHITE};       
        double[] tempWidthValue = new double[8];
        
        
         double[] yAxisValueI = new double[]{0,0,0,0,0,0,0};
          double[] xAxisValueI = new double[]{0,0,0,0,0,0,0};
          double[] widthValueI = new double[]{0,0,0,0,0,0,0};
          double[][] seriesValuesI = {yAxisValueI, xAxisValueI, widthValueI}; 
          seriousColorMap.put(0, new Color[]{Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE});
          defaultxyzdataset.addSeries("   ", seriesValuesI);

        for (String key : userCustomizedComparison.getComparProtsMap().keySet()) {
            userCustomizedComparison.getComparProtsMap().get(key).updateLabelLayout();

            {
                tempWidthValue[userCustomizedComparison.getComparProtsMap().get(key).getSignificantTrindCategory() + 1] = tempWidthValue[userCustomizedComparison.getComparProtsMap().get(key).getSignificantTrindCategory() + 1] + 1;
            }
        }
        int length = 0;
        if (upper < 10) {
            upper = 10;
        }
        double[] tooltipNumbess = new double[tempWidthValue.length];
        System.arraycopy(tempWidthValue, 0, tooltipNumbess, 0, tempWidthValue.length);
        this.tooltipsProtNumberMap.put(userCustomizedComparison.getComparisonHeader(), tooltipNumbess);
        for (int z = 0; z < tempWidthValue.length; z++) {
            if (tempWidthValue[z] > 0) {
                tempWidthValue[z] = scaleValues(tempWidthValue[z], upper, 2.5, 0.05);//Math.max(tempWidthValue[z] * 1.5 / upper, 0.05);
                length++;
            }

        }
        double[] yAxisValue = new double[length];
        double[] xAxisValue = new double[length];
        double[] widthValue = new double[length];
        Color[] serColorArr = new Color[length];
        length = 0;

        for (int z = 0; z < tempWidthValue.length; z++) {
            if (tempWidthValue[z] > 0) {
                xAxisValue[length] = z;
                yAxisValue[length] = counter+1;
                widthValue[length] = tempWidthValue[z];
                serColorArr[length] = dataColor[z];
                length++;
            }

        }

        if (length == 1 && selectedComparisonList.size() == 1) {
            widthValue[0] = 1;
        }
        seriousColorMap.put(++counter , serColorArr);

        double[][] seriesValues = {yAxisValue, xAxisValue, widthValue};
        defaultxyzdataset.addSeries(userCustomizedComparison.getComparisonHeader(), seriesValues);

        for (QuantDiseaseGroupsComparison qc : selectedComparisonList) {

            tempWidthValue = new double[8];
            if (qc.getComparProtsMap() == null) {
                continue;
            }

            for (String key : qc.getComparProtsMap().keySet()) {
                qc.getComparProtsMap().get(key).updateLabelLayout();

                if (significantOnly && (qc.getComparProtsMap().get(key).getSignificantTrindCategory() == 2 || qc.getComparProtsMap().get(key).getSignificantTrindCategory() == 5)) {
                    tempWidthValue[3] = 0;
                    tempWidthValue[6] = 0;
                } else {
                    tempWidthValue[qc.getComparProtsMap().get(key).getSignificantTrindCategory() + 1] = tempWidthValue[qc.getComparProtsMap().get(key).getSignificantTrindCategory() + 1] + 1;
                }
            }
            if (tempWidthValue[3] > 0 && tempWidthValue[6] >= 0) {
                stableColor = new Color(1, 141, 244);

            } else {
                stableColor = Color.decode("#b5babb");
            }
            tempWidthValue[3] = tempWidthValue[3] + tempWidthValue[6];
            tempWidthValue[6] = 0;
            dataColor[3] = stableColor;
            length = 0;
            if (upper < 10) {
                upper = 10;
            }
            tooltipNumbess = new double[tempWidthValue.length];
            System.arraycopy(tempWidthValue, 0, tooltipNumbess, 0, tempWidthValue.length);
            this.tooltipsProtNumberMap.put(qc.getComparisonHeader(), tooltipNumbess);
            for (int x = 0; x < tempWidthValue.length; x++) {
                if (tempWidthValue[x] > 0) {
                    tempWidthValue[x] = scaleValues(tempWidthValue[x], upper, 2.5, 0.05);//Math.max(tempWidthValue[z] * 1.5 / upper, 0.05);
                    length++;
                }

            }
            yAxisValue = new double[length];
            xAxisValue = new double[length];
            widthValue = new double[length];
            serColorArr = new Color[length];
            length = 0;

            for (int x = 0; x < tempWidthValue.length; x++) {
                if (tempWidthValue[x] > 0) {
                    xAxisValue[length] = x;
                    yAxisValue[length] = counter+1;
                    widthValue[length] = tempWidthValue[x];
                    serColorArr[length] = dataColor[x];
                    length++;
                }

            }

            if (length == 1 && selectedComparisonList.size() == 1) {
                widthValue[0] = 1;
            }
            seriousColorMap.put(counter+1, serColorArr);

            seriesValues = new double[][]{yAxisValue, xAxisValue, widthValue};
            defaultxyzdataset.addSeries(qc.getComparisonHeader(), seriesValues);

            counter++;
        }
        
        double[] yAxisValueII = new double[0];
            double[] xAxisValueII = new double[0];
            double[] widthValueII = new double[0];
              seriousColorMap.put(counter+1, new Color[]{});
             double[][] seriesValuesII = {yAxisValueII, xAxisValueII, widthValueII};            
         defaultxyzdataset.addSeries(" ", seriesValuesII);

        final Color[] labelsColor = new Color[]{Color.WHITE, new Color(80, 183, 71), Color.LIGHT_GRAY, new Color(1, 141, 244), Color.LIGHT_GRAY, new Color(204, 0, 0), Color.WHITE};
        Font font = new Font("Verdana", Font.BOLD, 13);
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

        String[] xAxisLabels = new String[selectedComparisonList.size() + 3];
        xAxisLabels[0]="  ";
        final Color[] diseaseGroupslabelsColor = new Color[selectedComparisonList.size() + 3];
        int x = 0;
        int maxLength = -1;
        //init labels color

        String updatedHeader = userCustomizedComparison.getComparisonHeader().split(" / ")[0].split("\n")[0] + " / " + userCustomizedComparison.getComparisonHeader().split(" / ")[1].split("\n")[0] + "";
        diseaseGroupslabelsColor[0]=Color.WHITE;
        diseaseGroupslabelsColor[x+1] = diseaseColorMap.get("UserData");
        xAxisLabels[x+1] = updatedHeader + " (" + userCustomizedComparison.getDatasetIndexes().length + ")    ";
        if (xAxisLabels[x+1].length() > maxLength) {
            maxLength = xAxisLabels[++x].length();
        }

        for (QuantDiseaseGroupsComparison comp : selectedComparisonList) {
            String header = comp.getComparisonHeader();
            updatedHeader = header.split(" / ")[0].split("\n")[0] + " / " + header.split(" / ")[1].split("\n")[0] + "";

            xAxisLabels[x+1] = updatedHeader + " (" + comp.getDatasetIndexes().length + ")    ";
            if (xAxisLabels[x+1].length() > maxLength) {
                maxLength = xAxisLabels[x+1].length();
            }
            diseaseGroupslabelsColor[x+1] = diseaseColorMap.get(header.split(" / ")[0].split("\n")[1]);
            x++;

        }
        xAxisLabels[x+1]="   ";

        SymbolAxis xAxis;
        final boolean finalNum;
        finalNum = maxLength > 30 && selectedComparisonList.size() > 4;

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
         xAxis.setAutoRangeStickyZero(true);
        xAxis.setTickMarksVisible(false);
        xAxis.setUpperBound(diseaseGroupslabelsColor.length-1);
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

//            private final Color[] labelsColor = new Color[]{new Color(0, 153, 0), new Color(0, 229, 132), new Color(1, 141, 244), new Color(255, 51, 51), new Color(204, 0, 0), Color.decode("#b5babb")};
//
//            private final Font font = new Font("Verdana", Font.PLAIN, 12);
//            private final String[] labels = new String[]{"Decreased 100%", "Decreased <100% ", "Equal", " Increased <100%", "Increased 100%", "No Quant. Info."};
//
//            @Override
//            public LegendItemCollection getLegendItems() {
//                LegendItemCollection legendItemCollection = new LegendItemCollection();
//                for (int i = 0; i < labelsColor.length; i++) {
//                    LegendItem item = new LegendItem(labels[i], labelsColor[i]);
//                    item.setLabelFont(font);
//                    legendItemCollection.add(item);
//
//                }
//
//                return legendItemCollection;//To change body of generated methods, choose Tools | Templates.
//            }
        };

        JFreeChart generatedChart = new JFreeChart(xyplot) {

        };
        xyplot.setOutlineVisible(false);
        LegendTitle legend = generatedChart.getLegend();
        legend.setVisible(false);
        xyplot.setBackgroundPaint(Color.WHITE);
        generatedChart.setBackgroundPaint(Color.WHITE);
        generatedChart.setPadding(new RectangleInsets(0, 0, 0, 0));
        Quant_Central_Manager.setProteinsOverviewBubbleChart(generatedChart);
        return generatedChart;

    }

    private String saveToFile(final JFreeChart chart, final double width, final double height) {
        isNewImge = true;
        Set<SquaredDot> set = new TreeSet<SquaredDot>();
        Set<SquaredDot> updatedselectedComponents = new HashSet<SquaredDot>();

        try {
            if (width < 1 || height < 1) {
                return "";
            }

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
                    square.setWidth((finalWidth + 2) + "px");
                    square.setHeight((finalHeight + 2) + "px");
                    if (selectedComparisonList == null || selectedComparisonList.isEmpty()) {
                        return "";
                    }
                    QuantDiseaseGroupsComparison comparison;
                    if (userCustomizedComparison != null && catEnt.getSeriesIndex() == 0) {
                        continue;
                    }
                    else if(userCustomizedComparison != null && catEnt.getSeriesIndex()== 1){
                        comparison = userCustomizedComparison;
                    } 
                    else {
                        comparison = ((QuantDiseaseGroupsComparison) selectedComparisonList.toArray()[catEnt.getSeriesIndex()-1 - userDataCounter]);
                    }

                    String header = comparison.getComparisonHeader();
                    String updatedHeader = header.split(" / ")[0].split("\n")[0] + " / " + header.split(" / ")[1].split("\n")[0] + " - " + header.split(" / ")[1].split("\n")[1].replace("_", " ").replace("-", "'").replace("Disease", "") + "";
                    int itemNumber = (int) ((XYItemEntity) entity).getDataset().getYValue(((XYItemEntity) entity).getSeriesIndex(), ((XYItemEntity) entity).getItem());

                    square.setDescription(updatedHeader + "<br/>#Proteins " + (int) tooltipsProtNumberMap.get(header)[itemNumber] + " " + tooltipLabels[itemNumber]);
                    double categIndex = (double) itemNumber;
                    int seriesIndex = ((XYItemEntity) entity).getSeriesIndex();
                    square.setParam("seriesIndex", seriesIndex);
                    square.setParam("categIndex", categIndex);

                    if (!lastselectedComponents.isEmpty()) {
                        square.unselect();
                        for (SquaredDot lastselectedComponent : lastselectedComponents) {
                            if (lastselectedComponent != null && categIndex == (Double) lastselectedComponent.getParam("categIndex") && seriesIndex == (Integer) lastselectedComponent.getParam("seriesIndex")) {
                                square.select();
                                updatedselectedComponents.add(square);
                                break;
                            }
                        }

                    }

                    square.setParam("position", "left: " + (smallX - 1) + "px; top: " + (smallY - 1) + "px;");
                    set.add(square);
                }

            }
            lastselectedComponents.clear();
            lastselectedComponents.addAll(updatedselectedComponents);
            for (SquaredDot square : set) {
                chartLayout.addComponent(square, square.getParam("position").toString());
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

        chartImage.setSource(new ExternalResource(defaultImgURL));

    }

    private Set<QuantDiseaseGroupsComparison> selectedComparisonList;

    @Override
    public void selectionChanged(String type) {
        if (type.equalsIgnoreCase("Comparison_Selection")) {
             lastselectedComponents.clear();
             resetSelection();
            selectedComparisonList = this.Quant_Central_Manager.getUpdatedSelectedDiseaseGroupsComparisonListProteins(searchQuantificationProtList);
            

            if (selectedComparisonList.isEmpty()) {
                emptySelectionLayout.setVisible(true);
                chartLayout.removeAllComponents();
                chartLayoutContainer.setVisible(false);
                topLayout.setVisible(false);
                bottomLayout.setVisible(false);
                this.addComponent(emptySelectionLayout);
                defaultImgURL = "";
            } else {
                emptySelectionLayout.setVisible(false);

                bottomLayout.setVisible(true);
                topLayout.setVisible(true);
                chartLayoutContainer.setVisible(true);
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
        double logMax = (Math.log(max) / Math.log(2));
        double logValue = (Math.log(linearValue + 1) / Math.log(2));
        logValue = (logValue * 2 / logMax) + lowerLimit;
        return logValue;
    }

    private void resetSelection() {
        Quant_Central_Manager.setBubbleChartQuantProteinsSelection(new HashSet<String>(), "");
        resetChart();

    }

    private final Set<SquaredDot> lastselectedComponents = new HashSet<SquaredDot>();

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {

        if (event.getClickedComponent() == null) {
            resetSelection();
            return;
        }

        if (event.getClickedComponent() instanceof SquaredDot) {

            Iterator<Component> itr = chartLayout.iterator();
            while (itr.hasNext()) {
                SquaredDot tsquare = (SquaredDot) itr.next();
                tsquare.unselect();
            }

            SquaredDot square = (SquaredDot) event.getClickedComponent();
            String selectedheader = "";

            if (activeMultiSelect) {
                if (lastselectedComponents.contains(square)) {
                    lastselectedComponents.remove(square);
                } else {
                    lastselectedComponents.add(square);
                }
                if (lastselectedComponents.isEmpty()) {
                    resetSelection();
                    return;
                }

            } else {

                if (lastselectedComponents.contains(square)) {
                    resetSelection();
                    return;
                }
                lastselectedComponents.clear();
                lastselectedComponents.add(square);

            }

            Set<String> selectionMap = new HashSet<String>();
            for (SquaredDot selectedCom : lastselectedComponents) {

                selectedCom.select();
                double trendIndex = (Double) selectedCom.getParam("categIndex")-1;
                int seriousIndex = (Integer) selectedCom.getParam("seriesIndex")-1;
                QuantDiseaseGroupsComparison comparison;
                if (userCustomizedComparison != null && seriousIndex == 0) {
                    comparison = userCustomizedComparison;
                    for (String key : comparison.getComparProtsMap().keySet()) {
                        DiseaseGroupsComparisonsProteinLayout compProt = comparison.getComparProtsMap().get(key);
                        if (compProt.getSignificantTrindCategory() == trendIndex) {
                            selectionMap.add(key);
                        }
                    }

                } else {
                    comparison = (QuantDiseaseGroupsComparison) Quant_Central_Manager.getSelectedDiseaseGroupsComparisonList().toArray()[seriousIndex - userDataCounter];
                   
                    for (String key : comparison.getComparProtsMap().keySet()) {
                        DiseaseGroupsComparisonsProteinLayout compProt = comparison.getComparProtsMap().get(key);
                        if (compProt.getSignificantTrindCategory() == trendIndex) {
                            selectionMap.add(key.split("_")[1]);

                        }

                    } 
                }
                if (selectedCom == square) {
                    selectedheader = comparison.getComparisonHeader();
                }

            }
            Quant_Central_Manager.setBubbleChartQuantProteinsSelection(selectionMap, selectedheader);

        } else {
            resetSelection();

        }
    }

    private void resetChart() {
        lastselectedComponents.clear();
        Iterator<Component> itr = chartLayout.iterator();
        while (itr.hasNext()) {
            SquaredDot tsquare = (SquaredDot) itr.next();
            tsquare.reset();

        }

    }

    public String getChartThumbImage() {
        if (chart == null) {
            return null;
        }
        XYPlot xyplot = chart.getXYPlot();
        xyplot.getDomainAxis().setVisible(false);
        xyplot.getRangeAxis().setVisible(false);
        chart.setBorderVisible(true);
        chart.setBorderPaint(Color.LIGHT_GRAY);
        String base64 = "";
        try {
            base64 = Base64.encodeBase64String(ChartUtilities.encodeAsPNG(chart.createBufferedImage(200, 200)));
        } catch (IOException ex) {
            Logger.getLogger(ComparisonsSelectionOverviewBubbleChart.class.getName()).log(Level.SEVERE, null, ex);
        }

        base64 = "data:image/png;base64," + base64;

        chart.setBorderVisible(false);
        xyplot.getDomainAxis().setVisible(true);
        xyplot.getRangeAxis().setVisible(true);

//        styles.add(".matrixbtn  { background-image: url(" + base64 + " )!important;}");
        return base64;

    }

}
