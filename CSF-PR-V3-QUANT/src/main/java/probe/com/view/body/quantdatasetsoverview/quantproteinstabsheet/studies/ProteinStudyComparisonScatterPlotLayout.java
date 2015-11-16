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
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.SymbolicXYItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.SeriesRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.TextAnchor;
import org.jfree.util.ShapeUtilities;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.DiseaseGroupsComparisonsProteinLayout;
import probe.com.model.beans.quant.QuantDatasetObject;
import probe.com.selectionmanager.CSFFilterSelection;
import probe.com.selectionmanager.DatasetExploringCentralSelectionManager;
import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.peptidescontainer.ComparisonDetailsBean;
import probe.com.view.core.jfreeutil.SquaredDot;

/**
 *
 * @author Yehia Farag
 */
public class ProteinStudyComparisonScatterPlotLayout extends GridLayout {

    private final Label comparisonTitle;
    private final VerticalLayout closeBtn;
    private final AbsoluteLayout ProteinScatterPlotContainer;
    private final int imgWidth;
    private final Map<String, QuantDatasetObject> dsKeyDatasetMap = new HashMap<String, QuantDatasetObject>();

    ;

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
    private PeptidesStackedBarChartsControler studyPopupLayoutManager;
    private final int width;
    private JFreeChart scatterPlot;

    /**
     *
     * @param cp
     * @param width
     * @param exploringFiltersManagerinst
     */
    public ProteinStudyComparisonScatterPlotLayout(final DiseaseGroupsComparisonsProteinLayout cp, int width, DatasetExploringCentralSelectionManager exploringFiltersManagerinst) {
        this.exploringFiltersManager = exploringFiltersManagerinst;
        this.setColumns(4);
        this.setRows(2);
        this.width = width;
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

        String styleString = "_" + cp.getProteinAccssionNumber()+"_" + cp.getComparison().getComparisonHeader();
        teststyle = styleString.replace(" ", "_").replace("+", "_").replace(")", "_").replace("(", "_").toLowerCase().replace(" ", "_").replace(")", "_").replace("(", "_").toLowerCase().replace("+", "_").replace("/", "_") + "_scatterplot";
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
                    studyPopupLayoutManager.updateSelectedProteinInformation(pGrNumber, trend, dsObjects, cp);
                }
            }
        });

    }

    public PeptidesStackedBarChartsControler getStudyPopupLayoutManager() {
        return studyPopupLayoutManager;
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

        XYSeries downSerII = new XYSeries(3);
        XYSeries notSerII = new XYSeries(4);
        XYSeries upSerII = new XYSeries(5);

//        XYSeries plusSeries = new XYSeries(6);
        double downCounter = 1;
        double notCounter = 3;
        double upCounter = 5;

        patientGroupsNumToDsIdMap.clear();

        final Map<Integer, int[]> paTGrNumbtrendMap = new HashMap<Integer, int[]>();
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
                upSerII.add(upCounter, i);
//                plusSeries.add(upCounter, i);
//                upSer.add(upCounter, ((double) i + 0.45));
//                upSer.add(upCounter, (double) i + 0.85);

            } //            else if ((values[2] > 2)) {
            //                upSer.add(upCounter, i);
            ////                 upSer.add(upCounter, (double) i + 0.45);
            //                upSerII.add(upCounter, i);
            //                plusSeries.add(upCounter, i);
            //
            ////                plusSeries.add(upCounter,  (double) i + 0.85);
            //            }
            else if ((values[2] == 1)) {
                upSer.add(upCounter, i);
            }
            if ((values[1] == 1)) {
//                notSer.add(notCounter, ((double) i + 0.45));
                notSer.add(notCounter, i);
//                notSer.add(notCounter, (double) i + 0.85);
//                plusSeries.add(notCounter,  (double) i + 0.45);
            } else if ((values[1] > 1)) {
                notSer.add(notCounter, i);
                notSerII.add(notCounter, i);
//                plusSeries.add(notCounter, (double) i);
            }
//            else if ((values[1] > 2)) {
////                 downSer.add(downCounter, ((double) i + 0.45));
//                notSer.add(notCounter, i);
//                notSer.add(notCounter, i);
////                downSer.add(downCounter, (double) i + 0.85);
//                plusSeries.add(notCounter, (double) i);
//            }
            if ((values[0] > 1)) {
//                downSer.add(downCounter, ((double) i + 0.45));
                downSer.add(downCounter, i);
                downSerII.add(downCounter, i);

//                downSer.add(downCounter, (double) i + 0.85);
//                plusSeries.add(downCounter, (double) i);
            } //            else if ((values[0] == 2)) {
            ////                 downSer.add(downCounter, ((double) i + 0.45));
            //                downSer.add(downCounter, i);
            //                downSerII.add(downCounter, i);
            ////                downSer.add(downCounter, (double) i + 0.85);
            ////                plusSeries.add(downCounter,  (double) i + 0.45);
            //            } 
            else if ((values[0] == 1)) {
                downSer.add(downCounter, i);
            }

        }

        dataset.addSeries(downSer);
        dataset.addSeries(notSer);
        dataset.addSeries(upSer);
        dataset.addSeries(downSerII);
        dataset.addSeries(notSerII);
        dataset.addSeries(upSerII);
//        dataset.addSeries(plusSeries);
        final String[] labels = new String[]{" ", ("Low (" + cp.getSignificantDown() + ")"), " ", ("Stable (" + cp.getNotProvided() + ")"), " ", ("High (" + cp.getSignificantUp() + ")"), ""};
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
        Font f = new Font("Verdana", Font.PLAIN, 11);
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
        JFreeChart tempScatterPlot = new JFreeChart(plot);
        tempScatterPlot.setBackgroundPaint(Color.WHITE);
        tempScatterPlot.getLegend().setVisible(false);
        Color c = new Color(242, 242, 242);
        plot.setDomainAxis(domainAxis);
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.setOutlinePaint(Color.GRAY);
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
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
        renderer.setUseOutlinePaint(true);

        Color c0 = new Color(80, 183, 71);
        renderer.setSeriesPaint(0, c0);
        renderer.setSeriesOutlinePaint(0, Color.WHITE);

        Color c1 = new Color(1, 141, 244);
        renderer.setSeriesPaint(1, c1);
        renderer.setSeriesOutlinePaint(1, Color.WHITE);

        Color c2 = new Color(204, 0, 0);
        renderer.setSeriesPaint(2, c2);
        renderer.setSeriesOutlinePaint(2, Color.WHITE);

        renderer.setSeriesPaint(3, new Color(150, 212, 145));
        renderer.setSeriesOutlinePaint(3, new Color(150, 212, 145));

        renderer.setSeriesPaint(4, new Color(103, 187, 248));
        renderer.setSeriesOutlinePaint(4, new Color(103, 187, 248));

        renderer.setSeriesPaint(5, new Color(224, 102, 102));
        renderer.setSeriesOutlinePaint(5, new Color(224, 102, 102));

//        renderer.setSeriesPaint(6, Color.BLACK);
//        renderer.setSeriesOutlinePaint(6, Color.BLACK);
        Shape downArr = ShapeUtilities.createDownTriangle(7f);
        Shape notRShape = ShapeUtilities.createDiamond(7f);
        Shape upArr = ShapeUtilities.createUpTriangle(7);

        Shape downArrII = ShapeUtilities.createTranslatedShape(ShapeUtilities.createDownTriangle(6f), 5, -5);
        Shape notRShapeII = ShapeUtilities.createTranslatedShape(ShapeUtilities.createDiamond(6f), 0, -7);
        Shape upArrII = ShapeUtilities.createTranslatedShape(ShapeUtilities.createUpTriangle(6f), 4, -4);

//        Shape plus = ShapeUtilities.createTranslatedShape(ShapeUtilities.createRegularCross(3f, 0.4f), 11, -7);
        renderer.setSeriesShape(0, downArr);
        renderer.setSeriesShape(1, notRShape);
        renderer.setSeriesShape(2, upArr);

        renderer.setSeriesShape(3, downArrII);
        renderer.setSeriesShape(4, notRShapeII);
        renderer.setSeriesShape(5, upArrII);
//       renderer.setSeriesShape(6, plus);

        renderer.setBaseItemLabelsVisible(true);
        renderer.setBaseItemLabelGenerator(new SymbolicXYItemLabelGenerator() {
            private final int[] indexer = new int[]{0, 0, 0, 1, 0, 2};

            @Override
            public String generateLabel(XYDataset dataset, int series, int category) {
                if (series > 2) {
                    int patNumber = (int) dataset.getYValue(series, category);
                    int trend = (int) dataset.getXValue(series, category);
                    return "\t   " + paTGrNumbtrendMap.get(patNumber)[indexer[trend]];

                }

                return super.generateLabel(dataset, series, category); //To change body of generated methods, choose Tools | Templates.
            }

        });
        ItemLabelPosition position = new ItemLabelPosition(
                ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT,
                TextAnchor.TOP_LEFT, 0.0);
        renderer.setSeriesPositiveItemLabelPosition(3, position);
        renderer.setSeriesPositiveItemLabelPosition(4, position);
        renderer.setSeriesPositiveItemLabelPosition(5, position);

        renderer.setBaseItemLabelFont(f);

        tempScatterPlot.setBorderVisible(false);

        plot.setSeriesRenderingOrder(SeriesRenderingOrder.REVERSE);

        plot.setBackgroundPaint(c);

        heighlightedScatterPlottImgUrl = saveToFile(tempScatterPlot, w, h, defaultScatterPlotRenderingInfo);

        plot.setBackgroundPaint(Color.WHITE);
        defaultScatterPlottImgUrl = saveToFile(tempScatterPlot, w, h, defaultScatterPlotRenderingInfo);

        TextTitle title = new TextTitle(comparisonTitle.getValue(), f);

        scatterPlot = new JFreeChart(plot);
        scatterPlot.setTitle(title);

        scatterPlot.setBorderVisible(false);
        scatterPlot.setBackgroundPaint(Color.WHITE);
        scatterPlot.getLegend().setVisible(false);
        dsKeyDatasetMap.clear();
        for (int i = 0; i < defaultScatterPlotRenderingInfo.getEntityCollection().getEntityCount(); i++) {
            final ChartEntity entity = defaultScatterPlotRenderingInfo.getEntityCollection().getEntity(i);
            if (entity instanceof XYItemEntity) {

                int x = ((XYItemEntity) entity).getSeriesIndex();
                int y = ((XYItemEntity) entity).getItem();

                if (((XYItemEntity) entity).getDataset().getYValue(x, y) > (int) ((XYItemEntity) entity).getDataset().getYValue(x, y)) {
                    continue;
                }
                if (((XYItemEntity) entity).getSeriesIndex() > 2) {

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

                    sb.append("<h3>").append((exploringFiltersManager.getFullQuantDatasetMap().get(dsId)).getAuthor()).append(" (").append((exploringFiltersManager.getFullQuantDatasetMap().get(dsId)).getYear()).append(")<h3/>");
                    sb.append("<p></p>");
                    ds = exploringFiltersManager.getFullQuantDatasetMap().get(dsId);

                    dsKeyDatasetMap.put("-" + dsId + "-" + comparisonProtein.getProteinAccssionNumber() + "-", ds);
                }
                String tooltip = sb.toString().substring(0, sb.toString().length() - 7);
                SquaredDot square = new SquaredDot("squared");
                if (paTGrNumbtrendMap.get(patGrNumber)[trend] > 1) {
                    square.setWidth(20 + "px");
                    square.setHeight(15 + "px");
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
            if (this.getParent().isVisible()) {
                this.getUI().scrollIntoView(this.ProteinScatterPlotContainer);
            }

        }

    }

    /**
     *
     */
    public void redrawChart() {
        if (defaultScatterPlottImgUrl == null) {
            this.generateScatterplotchart(comparisonProtein, imgWidth, 150);
            studyPopupLayoutManager = new PeptidesStackedBarChartsControler(width, patientGroupsNumToDsIdMap, comparisonProtein.getProteinAccssionNumber(), comparisonProtein.getProtName(), comparisonProtein.getUrl(), comparisonProtein.getComparison().getComparisonHeader(), comparisonProtein.getDsQuantProteinsMap(), dsKeyDatasetMap);
        }
        styles.add("." + teststyle + " {  background-image: url(" + defaultScatterPlottImgUrl + " );background-position:center; background-repeat: no-repeat; }");

    }

    public JFreeChart getScatterPlot() {
        return scatterPlot;
    }

}
