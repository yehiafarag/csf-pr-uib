/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.studies;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.codec.binary.Base64;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.DiseaseGroupsComparisonsProteinLayout;
import probe.com.model.beans.quant.QuantDatasetObject;
import probe.com.selectionmanager.CSFFilterSelection;
import probe.com.selectionmanager.DatasetExploringCentralSelectionManager;
import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.peptidescontainer.ComparisonDetailsBean;
import probe.com.view.core.JfreeExporter;
import probe.com.view.core.jfreeutil.SquaredDot;

/**
 *
 * @author Yehia Farag
 */
public class ProteinComparisonScatterPlotLayout extends GridLayout {

    private final Label comparisonTitle;
    private final VerticalLayout closeBtn;
    private final AbsoluteLayout ProteinScatterPlotContainer;
    private final int imgWidth;
    private Map<String,QuantDatasetObject> dsKeyDatasetMap = new HashMap<String, QuantDatasetObject>();;

    /**
     *
     * @return
     */
    public VerticalLayout getCloseBtn() {
        return closeBtn;
    }

    private String defaultScatterPlottImgUrl, heighlightedScatterPlottImgUrl;
    private final ChartRenderingInfo defaultScatterPlotRenderingInfo = new ChartRenderingInfo();
    private final DatasetExploringCentralSelectionManager exploringFiltersManager;
    private final String teststyle;
    private final Page.Styles styles = Page.getCurrent().getStyles();
    private final DiseaseGroupsComparisonsProteinLayout comparisonProtein;
    private  PeptidesStackedBarChartsControler studyPopupLayoutManager;
    private final boolean searchingMode;
      private final int width;

    /**
     *
     * @param cp
     * @param width
     * @param exploringFiltersManagerinst
     * @param searchingMode
     */
    public ProteinComparisonScatterPlotLayout(final DiseaseGroupsComparisonsProteinLayout cp, int width, DatasetExploringCentralSelectionManager exploringFiltersManagerinst, final boolean searchingMode) {
        this.searchingMode = searchingMode;
        this.exploringFiltersManager = exploringFiltersManagerinst;
        this.setColumns(4); 
        this.setRows(2);
        this.width=width;
        this.setSpacing(true);
        this.setMargin(new MarginInfo(false, false, true, false));
        this.comparisonProtein = cp;
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidthUndefined();
        topLayout.setHeight("20px");
        topLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        int numb = cp.getSignificantDown() + cp.getNotProvided() + cp.getNotReg() + cp.getSignificantUp();

        comparisonTitle = new Label(cp.getComparison().getComparisonHeader() + " (#Studies " + numb + "/" + cp.getComparison().getDatasetIndexes().length + ")");
        comparisonTitle.setContentMode(ContentMode.HTML);
        comparisonTitle.setStyleName("custChartLabelHeader");
        comparisonTitle.setWidth((width - 70) + "px");
        this.addComponent(comparisonTitle, 1, 0);
        this.setComponentAlignment(comparisonTitle, Alignment.TOP_LEFT);

        closeBtn = new VerticalLayout();
        closeBtn.setWidth("20px");
        closeBtn.setHeight("20px");
        closeBtn.setStyleName("closebtn");
        this.addComponent(closeBtn, 2, 0);
        this.setComponentAlignment(closeBtn, Alignment.TOP_RIGHT);

        imgWidth = (width - 70);
        ProteinScatterPlotContainer = new AbsoluteLayout();

        this.addComponent(ProteinScatterPlotContainer, 1, 1);
        ProteinScatterPlotContainer.setWidth(imgWidth + "px");
        ProteinScatterPlotContainer.setHeight(150 + "px");

        
        teststyle = "_"+cp.getProteinAccssionNumber().replace(" ", "_").replace(")", "_").replace("(", "_").toLowerCase() + "_" + cp.getComparison().getComparisonHeader().replace(" ", "_").replace(")", "_").replace("(", "_").toLowerCase().replace("/", "_") + "_scatterplot";
        styles.add("." + teststyle + " {  background-image: url(" + defaultScatterPlottImgUrl + " );background-position:center; background-repeat: no-repeat; }");
        ProteinScatterPlotContainer.setStyleName(teststyle);
        ProteinScatterPlotContainer.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {

                if (event.getClickedComponent() instanceof SquaredDot) {
                    SquaredDot dot = (SquaredDot) event.getClickedComponent();
                    int trend = (Integer) dot.getParam("trend");
                    int pGrNumber = (Integer) dot.getParam("pGrNumber");
                    exploringFiltersManager.setSelectedDataset(patientGroupsNumToDsIdMap.get(pGrNumber).getRegulatedList(trend));
                    int[] dssArr = new int[patientGroupsNumToDsIdMap.get(pGrNumber).getRegulatedList(trend).size()];
                    for (int x = 0; x < dssArr.length; x++) {                        
                        dssArr[x] = patientGroupsNumToDsIdMap.get(pGrNumber).getRegulatedList(trend).get(x);
                    }
                    exploringFiltersManager.setStudyLevelFilterSelection(new CSFFilterSelection("Study_Selection", dssArr, "scatter", null));//  
                   Set<QuantDatasetObject> dsObjects = new HashSet<QuantDatasetObject>();
                    for (int dsId : dssArr) {
                        dsObjects.add(exploringFiltersManager.getFullQuantDatasetMap().get(dsId));
                    }
                    studyPopupLayoutManager.updateSelectedProteinInformation(pGrNumber,trend,dsObjects,cp);
                }
            }
        });
        
    }

    /**
     *
     * @return
     */
    public Label getComparisonTitle() {
        return comparisonTitle;
    }
    private Map<Integer, ComparisonDetailsBean> patientGroupsNumToDsIdMap = new HashMap<Integer, ComparisonDetailsBean>();

    /**
     * Creates a sample jFreeChart.
     *
     * @param dataset the dataset.
     *
     * @return The jFreeChart.
     */
    private void generateScatterplotchart(DiseaseGroupsComparisonsProteinLayout cp, int w, int h) {

        final XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries downSer = new XYSeries(0);
        XYSeries notSer = new XYSeries(1);
        XYSeries upSer = new XYSeries(2);
        double downCounter = 1;
        double notCounter = 3;
        double upCounter = 5;

        patientGroupsNumToDsIdMap.clear();

        Map<Integer, int[]> paTGrNumbtrendMap = new HashMap<Integer, int[]>();
        for (String protTrend : cp.getPatientsNumToTrindMap().keySet()) {
            List<Integer> patNums = cp.getPatientsNumToTrindMap().get(protTrend);
            int coun = 0;
            for (int i : patNums) {
                if (!patientGroupsNumToDsIdMap.containsKey(i)) {
                    ComparisonDetailsBean pGr = new ComparisonDetailsBean();
                    patientGroupsNumToDsIdMap.put(i, pGr);

                }
                if (!paTGrNumbtrendMap.containsKey(i)) {
                    int[] values = new int[3];
                    paTGrNumbtrendMap.put(i, values);
                }

                int[] values = paTGrNumbtrendMap.get(i);
                ComparisonDetailsBean pGr = patientGroupsNumToDsIdMap.get(i);
                if (protTrend.equalsIgnoreCase("up")) {
                    values[2] = values[2] + 1;

                    pGr.addUpRegulated(cp.getDSID(0, coun));

                } else if (protTrend.equalsIgnoreCase("down")) {
                    values[0] = values[0] + 1;
                    pGr.addDownRegulated(cp.getDSID(2, coun));
                } else {
                    values[1] = values[1] + 1;
                    pGr.addNotRegulated(cp.getDSID(1, coun));
                }
                paTGrNumbtrendMap.put(i, values);
                patientGroupsNumToDsIdMap.put(i, pGr);
                coun++;
            }

        }

        for (int i : paTGrNumbtrendMap.keySet()) {
            int[] values = paTGrNumbtrendMap.get(i);
            if ((values[2] > 1)) {
                upSer.add(upCounter, i);
                upSer.add(upCounter, ((double) i + 0.45));
                upSer.add(upCounter, (double) i + 0.85);
            } else if ((values[2] == 1)) {
                upSer.add(upCounter, i);
            }
            if ((values[1] > 1)) {
                notSer.add(notCounter, ((double) i + 0.45));
                notSer.add(notCounter, i);
                notSer.add(notCounter, (double) i + 0.85);
            } else if ((values[1] == 1)) {
                notSer.add(notCounter, i);
            }
            if ((values[0] > 1)) {
                downSer.add(downCounter, ((double) i + 0.45));
                downSer.add(downCounter, i);
                downSer.add(downCounter, (double) i + 0.85);
            } else if ((values[0] == 1)) {
                downSer.add(downCounter, i);
            }

        }
        dataset.addSeries(upSer);
        dataset.addSeries(notSer);
        dataset.addSeries(downSer);
        final String[] labels = new String[]{" ", ("Down Regulated (" + cp.getSignificantDown() + ")"), " ", ("Not Regulated (" + cp.getNotProvided() + ")"), " ", ("Up Regulated (" + cp.getSignificantUp() + ")"), ""};
        final Color[] labelsColor = new Color[]{Color.LIGHT_GRAY, new Color(80, 183, 71), Color.LIGHT_GRAY, new Color(1, 141, 244), Color.LIGHT_GRAY, Color.RED, Color.LIGHT_GRAY};
        final SymbolAxis domainAxis = new SymbolAxis("X", labels) {
            int x = 0;

            @Override
            public Paint getTickLabelPaint() {
                if (x >= labels.length) {
                    x = 0;
                }
                return labelsColor[x++];
            }

        };
        domainAxis.setAutoRangeIncludesZero(false);
        Font f = new Font("Verdana", Font.PLAIN, 10);
        domainAxis.setTickLabelFont(f);
        domainAxis.setAutoRange(false);
        domainAxis.setLabel(null);
        domainAxis.setGridBandsVisible(false);
        String xTile = "#Patients";

        JFreeChart jFreeChart = ChartFactory.createScatterPlot(null,
                null, // domain axis label
                null, // range axis label
                dataset, // data
                PlotOrientation.HORIZONTAL, // orientation
                false, // include legend
                false, // tooltips?
                false // URLs?
        );
        XYPlot plot1 = (XYPlot) jFreeChart.getPlot();
        XYPlot plot = new XYPlot(dataset, plot1.getDomainAxis(), plot1.getRangeAxis(), plot1.getRenderer()) {
            @Override
            protected void drawDomainGridlines(Graphics2D g2, Rectangle2D dataArea, List ticks) {
                super.drawDomainGridlines(g2, dataArea, ticks); //To change body of generated methods, choose Tools | Templates.
            }
            private int x = 0;

            @Override
            public Paint getDomainGridlinePaint() {
                if (x >= labels.length) {
                    x = 0;
                }
                if (x == 1 || x == 3 || x == 5) {
                    x++;
                    return Color.WHITE;
                } else {
                    x++;
                    return super.getDomainGridlinePaint(); //To change body of generated methods, choose Tools | Templates.
                }
            }
        };
        plot.setOrientation(PlotOrientation.HORIZONTAL);
        JFreeChart scatter = new JFreeChart(plot);
        scatter.setBackgroundPaint(Color.WHITE);
        scatter.getLegend().setVisible(false);
        Color c = new Color(242, 242, 242);
        plot.setDomainAxis(domainAxis);
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.setOutlinePaint(Color.GRAY);
        XYItemRenderer renderer = plot.getRenderer();
        ValueAxis va = plot.getDomainAxis();
        va.setAutoRange(false);
        va.setMinorTickCount(0);
        va.setVisible(true);

        plot.getRangeAxis().setRange(0, 100);
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setTickUnit(new NumberTickUnit(10));
        rangeAxis.setLabel(xTile);
        rangeAxis.setLabelFont(f);
        rangeAxis.setLabelPaint(Color.GRAY);

        va.setRange(0, 6);
        plot.setBackgroundPaint(Color.WHITE);

        renderer.setSeriesPaint(2, new Color(80, 183, 71));
        renderer.setSeriesPaint(1, new Color(1, 141, 244));
        renderer.setSeriesPaint(0, new Color(204, 0, 0));
        Shape notRShape = ShapeUtilities.createDiamond(5f);
        Shape leftArr = ShapeUtilities.createDownTriangle(5f);
        Shape rightArr = ShapeUtilities.createUpTriangle(5f);
        renderer.setSeriesShape(2, leftArr);
        renderer.setSeriesShape(1, notRShape);
        renderer.setSeriesShape(0, rightArr);
        scatter.setBorderVisible(false);

        JfreeExporter exporter = new JfreeExporter();
        exporter.writeChartToPDFFile(scatter, 595, 842, "scatter 1" + teststyle + ".pdf");
        defaultScatterPlottImgUrl = saveToFile(scatter, w, h, defaultScatterPlotRenderingInfo);
        dsKeyDatasetMap.clear();
        for (int i = 0; i < defaultScatterPlotRenderingInfo.getEntityCollection().getEntityCount(); i++) {
            final ChartEntity entity = defaultScatterPlotRenderingInfo.getEntityCollection().getEntity(i);
            if (entity instanceof XYItemEntity) {

                int x = ((XYItemEntity) entity).getSeriesIndex();
                int y = ((XYItemEntity) entity).getItem();

                if (((XYItemEntity) entity).getDataset().getYValue(x, y) > (int) ((XYItemEntity) entity).getDataset().getYValue(x, y)) {
                    continue;
                }

                String[] arr = ((XYItemEntity) entity).getShapeCoords().split(",");
                int xSer = Integer.valueOf(arr[0]);
                int ySer = Integer.valueOf(arr[1]);
                int ySerEnd = Integer.valueOf(arr[3]);
                int patGrNumber = (int) ((XYItemEntity) entity).getDataset().getYValue(x, y);
                int trend = Integer.valueOf(((XYItemEntity) entity).getDataset().getSeriesKey(((XYItemEntity) entity).getSeriesIndex()).toString());

                ComparisonDetailsBean cpDetails = patientGroupsNumToDsIdMap.get(patGrNumber);
                List<Integer> dsList = cpDetails.getRegulatedList(trend);
                StringBuilder sb = new StringBuilder();
                
         
                
                for (int dsId : dsList) {
                     QuantDatasetObject ds;
                     if (searchingMode) {

                        sb.append("<h3>").append((exploringFiltersManager.getFullQuantDatasetMap().get(dsId - 1)).getAuthor()).append(" (").append((exploringFiltersManager.getFullQuantDatasetMap().get(dsId - 1)).getYear()).append(")<h3/>");
                        sb.append("<p></p>");
                         ds = exploringFiltersManager.getFullQuantDatasetMap().get(dsId - 1);
                       

                    } else {
                        sb.append("<h3>").append((exploringFiltersManager.getFullQuantDatasetMap().get(dsId)).getAuthor()).append(" (").append((exploringFiltersManager.getFullQuantDatasetMap().get(dsId)).getYear()).append(")<h3/>");
                        sb.append("<p></p>");
                        ds = exploringFiltersManager.getFullQuantDatasetMap().get(dsId);

                    }
                    dsKeyDatasetMap.put("-"+dsId+"-"+comparisonProtein.getProteinAccssionNumber()+"-", ds);
                }
                String tooltip = sb.toString().substring(0, sb.toString().length() - 7);
                SquaredDot square = new SquaredDot("squared");
                if (paTGrNumbtrendMap.get(patGrNumber)[trend] > 1) {
                    square.setWidth(15 + "px");
                    square.setHeight(10 + "px");
                } else {
                    square.setWidth(10 + "px");
                    square.setHeight(10 + "px");
                }
                square.setDescription(tooltip);
                square.setParam("trend", trend);
                square.setParam("pGrNumber", patGrNumber);
                int top = (ySer - 4);
                if (ySer > ySerEnd) {
                    top = ySerEnd - 3;
                }
                ProteinScatterPlotContainer.addComponent(square, "left: " + (xSer - 5) + "px; top: " + top + "px;");
            }
        }
        plot.setBackgroundPaint(c);
        exporter.writeChartToPDFFile(jFreeChart, 595, 842, "scatter selected" + teststyle + ".pdf");
        heighlightedScatterPlottImgUrl = saveToFile(scatter, w, h, defaultScatterPlotRenderingInfo);
    }

    private String saveToFile(final JFreeChart chart, final double width, final double height, ChartRenderingInfo chartRenderingInfo) {
        byte imageData[];
        try {
            imageData = ChartUtilities.encodeAsPNG(chart.createBufferedImage((int) width, (int) height, chartRenderingInfo));
            String base64 = Base64.encodeBase64String(imageData);
            base64 = "data:image/png;base64," + base64;
            return base64;
        } catch (IOException e) {
            System.err.println("at error " + e.getMessage());
        }
        return "";

    }
    private boolean isclicked;

    /**
     *
     * @param heighlight
     * @param clicked
     */
    public void highlight(boolean heighlight, boolean clicked) {

        if (heighlight) {
            styles.add("." + teststyle + " {  background-image: url(" + heighlightedScatterPlottImgUrl + " );background-position:center; background-repeat: no-repeat; }");

        } else if (!heighlight) {
            if (!isclicked) {
                styles.add("." + teststyle + " {  background-image: url(" + defaultScatterPlottImgUrl + " );background-position:center; background-repeat: no-repeat; }");
            } else {
                isclicked = false;
            }
        }
        if (clicked) {
            isclicked = true;
            this.getUI().scrollIntoView(this.ProteinScatterPlotContainer);
        }

    }

    /**
     *
     */
    public void redrawChart() {
        if (defaultScatterPlottImgUrl == null) {
            this.generateScatterplotchart(comparisonProtein, imgWidth, 150);
            studyPopupLayoutManager = new PeptidesStackedBarChartsControler(width, patientGroupsNumToDsIdMap,comparisonProtein.getProteinAccssionNumber(),comparisonProtein.getProtName(),comparisonProtein.getUrl(),comparisonProtein.getComparison().getComparisonHeader(),comparisonProtein.getDsQuantProteinsMap(),dsKeyDatasetMap);
        }
        styles.add("." + teststyle + " {  background-image: url(" + defaultScatterPlottImgUrl + " );background-position:center; background-repeat: no-repeat; }");

    }

}
