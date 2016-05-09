/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.components.datasetfilters;

import com.itextpdf.text.pdf.codec.Base64;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Image;
import com.vaadin.ui.VerticalLayout;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.PieSectionEntity;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

/**
 *
 * @author Yehia Farag
 */
public abstract class DatasetPieChartFilter extends VerticalLayout implements LayoutEvents.LayoutClickListener {

//    public Map<String, Color> getDefaultKeyColorMap() {
//        return defaultKeyColorMap;
//    }
    public JFreeChart getChart() {
        return chart;
    }

//    private final String defaultImgURL;
    private final Color selectedColor = new Color(59, 90, 122);

    private final Map<Comparable, Color> defaultKeyColorMap = new HashMap<>();
//    private final Page.Styles styles = Page.getCurrent().getStyles();
    private String[] labels;
    private int[] values;
    private final Map<Comparable, String> valuesMap = new HashMap<>();
    private final String filter_Id;
//    private final PieChartsSelectionManager Local_Filter_Manager;
    private final int width;
    private final int height;
//    private final Map<String, List<Integer>> dsIndexesMap;
    private final Map<Comparable, List<Integer>> inuseDsIndexesMap;
    private final HashSet<Integer> selectedDsIds = new HashSet<>();
    private final HashSet<Integer> fullDsIds = new HashSet<>();
    private final Set<Integer> availableDsIds = new LinkedHashSet<>();
    private Map<Comparable, PieChartSlice> chartData;
    private final Color[] defaultColors = new Color[]{new Color(110, 177, 206), new Color(219, 169, 1), new Color(213, 8, 8), new Color(4, 180, 95), new Color(174, 180, 4), new Color(10, 255, 14), new Color(244, 250, 88), new Color(255, 0, 64), new Color(246, 216, 206), new Color(189, 189, 189), new Color(255, 128, 0), Color.WHITE};

    private final Image chartBackgroundImg;

    /**
     *
     * @param filterId
     * @param filterIndex
     * @param Local_Filter_Manager
     * @param dsIndexesMap
     * @param filterWidth
     */
    public DatasetPieChartFilter(String filterTitle, String filterId, int filterIndex, int filterWidth, int filterHeight) {
        this.filter_Id = filterId;
        this.width = filterWidth;
        this.height = filterHeight;
        this.setWidth(width, Unit.PIXELS);
        this.setHeight(height, Unit.PIXELS);
        this.chartBackgroundImg = new Image();
        this.addLayoutClickListener(DatasetPieChartFilter.this);
        this.addStyleName("pointer");
        chartBackgroundImg.setWidth(100, Unit.PERCENTAGE);
        chartBackgroundImg.setHeight(100, Unit.PERCENTAGE);
        this.addComponent(chartBackgroundImg);
        this.initPieChart(filterTitle);
        this.redrawChart();

//        this.Local_Filter_Manager = Local_Filter_Manager;
//        this.dsIndexesMap = dsIndexesMap;
        this.inuseDsIndexesMap = new LinkedHashMap<>();
//        this.updateLabelsAndValues(null, false);
//        for (int z = 0; z < labels.length; z++) {
//            defaultKeyColorMap.put(labels[z], defaultColors[z]);
//        }
//        teststyle = "pieChartCssfilter_" + filterId;
//        styles.add("." + teststyle + " :focus { outline: none !important;}");
//        inUseImgURL = defaultImgURL;
//        redrawChart();
//        Local_Filter_Manager.registerLocalPieChartFilter(DatasetPieChartFilter.this);
    }

//    private void updateLabelsAndValues(Set<Integer> dsIndexes, boolean reset) {;
//        if (dsIndexes == null || dsIndexes.isEmpty()) {
//            this.labels = new String[inuseDsIndexesMap.size()];
//            this.values = new int[inuseDsIndexesMap.size()];
//            int x = 0;
//            int notAvailableIndex = -1;
//            for (String str : inuseDsIndexesMap.keySet()) {
//                if (str.equalsIgnoreCase("Not Available")) {
//                    notAvailableIndex = x;
//                }
//                labels[x] = str;
//                values[x] = inuseDsIndexesMap.get(str).size();
//                valuesMap.put(labels[x], values[x] + "");
//                
//                x++;
//            }
//            if (notAvailableIndex != -1) {
//                labels[notAvailableIndex] = labels[labels.length - 1];
//                labels[labels.length - 1] = "Not Available";
//                int notAvaiValue = values[notAvailableIndex];
//                values[notAvailableIndex] = values[values.length - 1];
//                values[values.length - 1] = notAvaiValue;
//                defaultColors[labels.length - 1] = Color.LIGHT_GRAY;
//            }
//
//        } else {
////            availableDsIds.addAll(dsIndexes);
//            Map<String, List<Integer>> filteredDssMap = new HashMap<String, List<Integer>>();
//            Map<String, Integer> filteredDsIndexesMap = new HashMap<String, Integer>();
//            for (int y : dsIndexes) {
//                for (String key : dsIndexesMap.keySet()) {
//                    if (dsIndexesMap.get(key).contains(y)) {
//                        if (!filteredDsIndexesMap.containsKey(key)) {
//                            filteredDsIndexesMap.put(key, 0);
//                            filteredDssMap.put(key, new ArrayList<Integer>());
//                        }
//                        int val = filteredDsIndexesMap.get(key);
//                        val++;
//                        List<Integer> dsIds = filteredDssMap.get(key);
//                        dsIds.add(y);
//                        filteredDsIndexesMap.put(key, val);
//                        filteredDssMap.put(key, dsIds);
//                    }
//                }
//
//            }
//
//            this.labels = new String[filteredDsIndexesMap.size()];
//            this.values = new int[filteredDsIndexesMap.size()];
//            int x = 0;
//            int notAvailableIndex = -1;
//
//            for (String str : filteredDsIndexesMap.keySet()) {
//                if (str.equalsIgnoreCase("Not Available")) {
//                    notAvailableIndex = x;
//                }
//                labels[x] = str;
//                values[x] = filteredDsIndexesMap.get(str);
//                valuesMap.put(labels[x], values[x] + "");
//                x++;
//            }
//            if (notAvailableIndex != -1) {
//                labels[notAvailableIndex] = labels[labels.length - 1];
//                labels[labels.length - 1] = "Not Available";
//                int notAvaiValue = values[notAvailableIndex];
//                values[notAvailableIndex] = values[values.length - 1];
//                values[values.length - 1] = notAvaiValue;
//            }
//            if (reset) {
//                inuseDsIndexesMap = filteredDssMap;
//            }
//
//        }
//
//    }
    /**
     *
     * @return
     */
    public String getFilter_Id() {
        return filter_Id;
    }

    private PiePlot plot;
    private JFreeChart chart;

    private void initPieChart(String title) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        plot = new PiePlot(dataset);
        plot.setNoDataMessage("No data available");
        plot.setCircular(true);
        plot.setLabelGap(0);
        plot.setLabelFont(new Font("Open Sans", Font.BOLD, 15));
        plot.setLabelGenerator(new PieSectionLabelGenerator() {

            @Override
            public String generateSectionLabel(PieDataset pd, Comparable cmprbl) {
                return valuesMap.get(cmprbl);
            }

            @Override
            public AttributedString generateAttributedSectionLabel(PieDataset pd, Comparable cmprbl) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        plot.setSimpleLabels(true);
        plot.setLabelBackgroundPaint(null);
        plot.setLabelShadowPaint(null);
        plot.setLabelPaint(Color.WHITE);
        plot.setLabelOutlinePaint(null);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setInteriorGap(0);
        plot.setShadowPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        plot.setBaseSectionOutlinePaint(Color.WHITE);
        plot.setSectionOutlinesVisible(true);
        plot.setBaseSectionOutlineStroke(new BasicStroke(1.2f));
        plot.setInteriorGap(0.05);
        plot.setIgnoreZeroValues(true);

        chart = new JFreeChart(plot);
        chart.setTitle(new TextTitle(title, new Font("Open Sans", Font.BOLD, 13)));
        chart.setBorderPaint(null);
        chart.setBackgroundPaint(null);
        chart.getLegend().setFrame(BlockBorder.NONE);
        chart.getLegend().setItemFont(new Font("Open Sans", Font.PLAIN, 12));

    }
    private final ChartRenderingInfo chartRenderingInfo = new ChartRenderingInfo();

    private String saveToFile(final JFreeChart chart, final double width, final double height) {
        byte imageData[];
        try {

            imageData = ChartUtilities.encodeAsPNG(chart.createBufferedImage((int) width, (int) height, chartRenderingInfo));
            String base64 = Base64.encodeBytes(imageData);
            base64 = "data:image/png;base64," + base64;
            return base64;
        } catch (IOException e) {
            System.err.println("at error " + e.getMessage());
        }
        return "";
    }

    /**
     * this method is responsible for initializing chart dataset
     *
     * @param chartData information required to update chart data
     */
    public void initializeFilterData(Map<Comparable, PieChartSlice> chartData) {
        this.chartData = chartData;
        fullDsIds.clear();
        DefaultPieDataset dataset = (DefaultPieDataset) plot.getDataset();
        dataset.clear();
        defaultKeyColorMap.clear();
        valuesMap.clear();
        int counter = 0;
        for (PieChartSlice slice : chartData.values()) {
            dataset.setValue(slice.getLabel(), slice.getValue());
            fullDsIds.addAll(slice.getDatasetIds());
            slice.setColor(defaultColors[counter++]);
            plot.setSectionPaint(slice.getLabel(), slice.getColor());
            valuesMap.put(slice.getLabel(), slice.getValue() + "");
            defaultKeyColorMap.put(slice.getLabel(), slice.getColor());
            inuseDsIndexesMap.put(slice.getLabel(), new ArrayList<>(slice.getDatasetIds()));
        }

        redrawChart();

    }

    public abstract void selectDatasets(Collection<Integer> datasetId, boolean selected);

    /**
     *
     */
    public final void redrawChart() {
        String imgUrl = saveToFile(chart, width, height);
        this.chartBackgroundImg.setSource(new ExternalResource(imgUrl));
    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        ChartEntity entity = chartRenderingInfo.getEntityCollection().getEntity(event.getRelativeX(), event.getRelativeY());

        if (entity != null && entity instanceof PieSectionEntity) {
            PieSectionEntity pieEnt = (PieSectionEntity) entity;
            selectSlice(pieEnt.getSectionKey());

        }
    }

    private void selectSlice(Comparable sliceKey) {

        if (plot.getExplodePercent(sliceKey) == 0.1) {
            plot.setExplodePercent(sliceKey, 0);
            plot.setSectionPaint(sliceKey, defaultKeyColorMap.get(sliceKey));
            selectedDsIds.removeAll(inuseDsIndexesMap.get(sliceKey));
        } else {
            plot.setExplodePercent(sliceKey, 0.1);
            plot.setSectionPaint(sliceKey, selectedColor);
            selectedDsIds.addAll(inuseDsIndexesMap.get(sliceKey));

        }
        selectDatasets(selectedDsIds, true);
        redrawChart();

    }

    public void localUpdate(Collection<Integer> datasetId) {

        inuseDsIndexesMap.clear();
        valuesMap.clear();
        DefaultPieDataset dataset = (DefaultPieDataset) plot.getDataset();
        dataset.clear();
        chartData.values().stream().forEach((slice) -> {
            int value = 0;
            List<Integer> idList = new ArrayList<>();
            value = datasetId.stream().filter((id) -> (slice.getDatasetIds().contains(id))).map((id) -> {
                idList.add(id);
                return id;
            }).map((_item) -> 1).reduce(value, Integer::sum);

            if (value > 0) {
                inuseDsIndexesMap.put(slice.getLabel(), idList);
                dataset.setValue(slice.getLabel(), value);
                valuesMap.put(slice.getLabel(), value + "");
            }
        });
        if (valuesMap.size() == 1) {
            this.removeLayoutClickListener(DatasetPieChartFilter.this);
        } else {
            this.addLayoutClickListener(DatasetPieChartFilter.this);
        }

        redrawChart();

    }

    /**
     *
     * @param width
     * @param height
     * @return
     */
    public String updatePieChart(int width, int height) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (int x = 0; x < labels.length; x++) {
            dataset.setValue(labels[x], new Double(values[x]));

        }
        plot.setDataset(dataset);
        String chartImgUrl = saveToFile(chart, width, height);
        return chartImgUrl;

    }

    /**
     *
     * @param dsIndexes
     */
    public void selectionChanged(Set<Integer> dsIndexes) {
//        updateLabelsAndValues(dsIndexes, false);
        availableDsIds.clear();
        availableDsIds.addAll(dsIndexes);
//        inUseImgURL = updatePieChart(width, height);
        redrawChart();

    }

    /**
     *
     * @return
     */
    public HashSet<Integer> getSelectedDsIds() {

        return selectedDsIds;
    }

    /**
     *
     * @param dsIndexes
     * @param reset
     */
    public void resetFilterWithUpdatedFilters(Set<Integer> dsIndexes, boolean reset) {
//        updateLabelsAndValues(dsIndexes, reset);
//        inUseImgURL = initPieChart(width, height);
        redrawChart();

    }

    /**
     *
     */
    public void resetFilterToClearState() {
//        inuseDsIndexesMap = dsIndexesMap;
//        updateLabelsAndValues(null, false);
//        inUseImgURL = initPieChart(width, height);
        redrawChart();

    }

    /**
     *
     */
    public void unselectFilter() {
        selectedDsIds.clear();
//        for (String sliceKey : inuseDsIndexesMap.keySet()) {
//            if (plot.getExplodePercent(sliceKey) == 0.1) {
//                plot.setExplodePercent(sliceKey, 0);
//                plot.setSectionPaint(sliceKey, defaultKeyColorMap.get(sliceKey));
//                selectedDsIds.removeAll(inuseDsIndexesMap.get(sliceKey));
//
//            }
//        }

    }

    public Set<Integer> getFinalFilterValue() {
        Set<Integer> finalFilterValue = new LinkedHashSet<Integer>(availableDsIds);
//        if (finalFilterValue.isEmpty()) {
//            for (List<Integer> l : inuseDsIndexesMap.values()) {
//                finalFilterValue.addAll(l);
//            }
//        }
        return finalFilterValue;

    }

}
