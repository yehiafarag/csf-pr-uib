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
 *
 * this class represents a dataset interactive filter
 */
public abstract class DatasetPieChartFilter extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    public JFreeChart getChart() {
        return chart;
    }

    private final Color selectedColor = new Color(59, 90, 122);
    private final Map<Comparable, Color> defaultKeyColorMap = new HashMap<>();
    private final Map<Comparable, String> valuesMap = new HashMap<>();
    private final String filter_Id;
    private final int width;
    private final int height;
    private final Map<Comparable, List<Integer>> inuseDsIndexesMap;
    private final HashSet<Integer> selectedDsIds = new HashSet<>();
    private final HashSet<Integer> fullDsIds = new HashSet<>();
    private final Set<Integer> availableDsIds = new LinkedHashSet<>();
    private final Map<Comparable, PieChartSlice> chartData;
    private final Color[] defaultColors = new Color[]{new Color(110, 177, 206), new Color(219, 169, 1), new Color(213, 8, 8), new Color(4, 180, 95), new Color(174, 180, 4), new Color(10, 255, 14), new Color(244, 250, 88), new Color(255, 0, 64), new Color(246, 216, 206), new Color(189, 189, 189), new Color(255, 128, 0), Color.WHITE};
    private PiePlot plot;
    private JFreeChart chart;
    private final ChartRenderingInfo chartRenderingInfo = new ChartRenderingInfo();
    private final Image chartBackgroundImg;

    /**
     *
     * @param filterTitle
     * @param filterId
     * @param filterIndex
     * @param filterHeight
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
        this.inuseDsIndexesMap = new LinkedHashMap<>();
        this.chartData = new HashMap<>();
    }

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
        this.chartData.clear();
        this.chartData.putAll(chartData);
        fullDsIds.clear();
        reset();
    }

    /**
     * this method responsible for the selection action the method to be
     * implemented in the container to maintain pie-chart interactivity
     *
     * @param noselection
     */
    public abstract void selectDatasets(boolean noselection);

    /**
     *
     */
    private void redrawChart() {
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
            selectedDsIds.removeAll(chartData.get(sliceKey).getDatasetIds());
        } else {
            plot.setExplodePercent(sliceKey, 0.1);
            plot.setSectionPaint(sliceKey, selectedColor);
            selectedDsIds.addAll(chartData.get(sliceKey).getDatasetIds());

        }
        selectDatasets(selectedDsIds.isEmpty());
        redrawChart();

    }

    public boolean isActiveFilter() {

        return !selectedDsIds.isEmpty();
    }

    public void localUpdate(Collection<Integer> datasetId, boolean single) {

        if (single && !selectedDsIds.isEmpty()) {
            datasetId.clear();
            datasetId.addAll(fullDsIds);

        }
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
            inuseDsIndexesMap.put(slice.getLabel(), idList);
            dataset.setValue(slice.getLabel(), value);
            valuesMap.put(slice.getLabel(), value + "");
        });

        redrawChart();
    }

    /**
     *
     * @return
     */
    public HashSet<Integer> getSelectedDsIds() {
        if (selectedDsIds.isEmpty()) {
            return fullDsIds;
        }
        return selectedDsIds;
    }

    public void reset() {
        DefaultPieDataset dataset = (DefaultPieDataset) plot.getDataset();
        dataset.clear();
        defaultKeyColorMap.clear();
        valuesMap.clear();
        inuseDsIndexesMap.clear();
        selectedDsIds.clear();
        Map<Comparable, PieChartSlice> tchartData = new LinkedHashMap<>();
        int counter = 0;
        for (PieChartSlice slice : chartData.values()) {
            if (slice.getLabel().toString().trim().equals("")) {
                slice.setLabel("Not Available");
                slice.setColor(Color.LIGHT_GRAY);

            } else {
                slice.setColor(defaultColors[counter++]);
            }
            dataset.setValue(slice.getLabel(), slice.getValue());
            fullDsIds.addAll(slice.getDatasetIds());
            plot.setSectionPaint(slice.getLabel(), slice.getColor());
            valuesMap.put(slice.getLabel(), slice.getValue() + "");
            defaultKeyColorMap.put(slice.getLabel(), slice.getColor());
            inuseDsIndexesMap.put(slice.getLabel(), new ArrayList<>(slice.getDatasetIds()));
            tchartData.put(slice.getLabel(), slice);
            plot.setExplodePercent(slice.getLabel(), 0);
        }
        this.chartData.clear();
        this.chartData.putAll(tchartData);
        redrawChart();

    }
}
