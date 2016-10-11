package no.uib.probe.csf.pr.touch.view.components.datasetfilters;

import com.itextpdf.text.pdf.codec.Base64;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
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
import java.util.List;
import java.util.Map;
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
public abstract class DatasetPieChartFilter extends AbsoluteLayout implements LayoutEvents.LayoutClickListener {

    public JFreeChart getChart() {
        return chart;
    }

    private final Color selectedColor = Color.decode("#197de1");
    private final Map<Comparable, Color> defaultKeyColorMap = new HashMap<>();
    private final Map<Comparable, Color> selectedKeyColorMap = new HashMap<>();
    private final Map<Comparable, String> valuesMap = new HashMap<>();
    private final int width;
    private final int height;
    private final Map<Comparable, List<Integer>> inuseDsIndexesMap;
    private final HashSet<Integer> selectedDsIds = new HashSet<>();
    private final HashSet<Integer> fullDsIds = new HashSet<>();
    private final Map<Comparable, PieChartSlice> chartData;
    private final Color[] defaultColors = new Color[]{new Color(110, 177, 206), new Color(219, 169, 1), new Color(213, 8, 8), new Color(4, 180, 95), new Color(174, 180, 4), new Color(10, 255, 14), new Color(244, 250, 88), new Color(255, 0, 64), new Color(246, 216, 206), new Color(189, 189, 189), new Color(255, 128, 0), Color.WHITE};
    private PiePlot plot;
    private JFreeChart chart;
    private final ChartRenderingInfo chartRenderingInfo = new ChartRenderingInfo();
    private final Image chartBackgroundImg;
    private final VerticalLayout middleDountLayout;

    private final Label selectAllLabel;
    private final boolean smallScreen;

    /**
     *
     * @param filterTitle
     * @param filterId
     * @param filterIndex
     * @param filterHeight
     * @param filterWidth
     */
    public DatasetPieChartFilter(String filterTitle, String filterId, int filterIndex, int filterWidth, int filterHeight, boolean smallScreen) {
        this.width = filterWidth;
        this.height = filterHeight;
        smallScreen=false;
        this.smallScreen = smallScreen;

        this.setWidth(width, Unit.PIXELS);
        this.setHeight(height, Unit.PIXELS);
        this.chartBackgroundImg = new Image();
        this.addLayoutClickListener(DatasetPieChartFilter.this);
        this.addStyleName("pointer");
        chartBackgroundImg.setWidth(100, Unit.PERCENTAGE);
        chartBackgroundImg.setHeight(100, Unit.PERCENTAGE);
        this.addComponent(chartBackgroundImg, "left: 0px; top: 0px");
        middleDountLayout = new VerticalLayout();

        if (smallScreen) {
            middleDountLayout.setVisible(false);
        } else {
            middleDountLayout.setWidth(120, Unit.PIXELS);
            middleDountLayout.setHeight(120, Unit.PIXELS);
        }

        middleDountLayout.setStyleName("middledountchart");

        selectAllLabel = new Label();
        selectAllLabel.setWidth(30, Unit.PIXELS);
        selectAllLabel.setStyleName(ValoTheme.LABEL_TINY);
        selectAllLabel.addStyleName(ValoTheme.LABEL_SMALL);
        middleDountLayout.addComponent(selectAllLabel);
        middleDountLayout.setComponentAlignment(selectAllLabel, Alignment.MIDDLE_CENTER);

        this.initPieChart(filterTitle);
        this.redrawChart();
        this.inuseDsIndexesMap = new LinkedHashMap<>();
        this.chartData = new LinkedHashMap<>();
    }

    private void initPieChart(String title) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        plot = new PiePlot(dataset);
        plot.setNoDataMessage("No data available");
        plot.setCircular(true);
        plot.setLabelGap(0);
        plot.setLabelFont(new Font("Helvetica Neue", Font.PLAIN, 13));
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
        plot.setSimpleLabels(false);
        plot.setLabelBackgroundPaint(null);
        plot.setLabelShadowPaint(null);
        plot.setLabelPaint(Color.GRAY);
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
        TextTitle textTitle = new TextTitle(title, new Font("Helvetica Neue", Font.PLAIN, 13));
        textTitle.setPadding(25, 0, 1, 0);
        if (smallScreen) {
            chart.setTitle(new TextTitle(title, new Font("Helvetica Neue", Font.PLAIN, 10)));
            chart.getLegend().setItemFont(new Font("Helvetica Neue", Font.PLAIN, 9));
        } else {
            chart.setTitle(textTitle); 
            chart.getLegend().setItemFont(new Font("Helvetica Neue", Font.PLAIN, 12));
        }
        chart.setBorderPaint(null);
        chart.setBackgroundPaint(null);
        chart.getLegend().setFrame(BlockBorder.NONE);
        

    }

    private String saveToFile(final JFreeChart chart, double width, double height) {
        byte imageData[];
        try {
            if (smallScreen) {
                width = Math.max(width, 121);
                height = Math.max(height, 121);
            } else {
                width = Math.max(width, 250);
                height = Math.max(height, 250);
            }

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
        int coundDs = 0;
        coundDs = chartData.values().stream().map((slice) -> slice.getDatasetIds().size()).reduce(coundDs, Integer::sum);
        this.selectAllLabel.setValue(coundDs + "");
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
        this.removeComponent(middleDountLayout);
        this.addComponent(middleDountLayout, "left: " + ((chartRenderingInfo.getPlotInfo().getDataArea().getCenterX()) - 60) + "px; top: " + (chartRenderingInfo.getPlotInfo().getDataArea().getCenterY() - 60) + "px");

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (event.getClickedComponent() instanceof VerticalLayout || event.getClickedComponent() instanceof Label) {
            return;
        }

        ChartEntity entity = chartRenderingInfo.getEntityCollection().getEntity(event.getRelativeX(), event.getRelativeY());

        if (entity != null && entity instanceof PieSectionEntity) {
            PieSectionEntity pieEnt = (PieSectionEntity) entity;
            selectSlice(pieEnt.getSectionKey());

        }
    }

    private void selectSlice(Comparable sliceKey) {

        if (plot.getSectionOutlinePaint(sliceKey) == selectedColor) {
//            plot.setExplodePercent(sliceKey, 0);
            plot.setSectionOutlinePaint(sliceKey, null);
            plot.setSectionPaint(sliceKey, defaultKeyColorMap.get(sliceKey));
            selectedDsIds.removeAll(chartData.get(sliceKey).getDatasetIds());
        } else {
//            plot.setExplodePercent(sliceKey, 0.1);
            plot.setSectionOutlinePaint(sliceKey, selectedColor);
            plot.setSectionPaint(sliceKey, selectedKeyColorMap.get(sliceKey));
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
        this.selectAllLabel.setValue(datasetId.size() + "");
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
        selectedKeyColorMap.clear();
        valuesMap.clear();
        inuseDsIndexesMap.clear();
        selectedDsIds.clear();

        Map<Comparable, PieChartSlice> tchartData = new LinkedHashMap<>();
        int counter = 0;
        int coundDs = 0;
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
            plot.setSectionOutlinePaint(slice.getLabel(), null);
            valuesMap.put(slice.getLabel(), slice.getValue() + "");
            defaultKeyColorMap.put(slice.getLabel(), slice.getColor());
            selectedKeyColorMap.put(slice.getLabel(), slice.getColor().darker());
            coundDs += slice.getDatasetIds().size();
            inuseDsIndexesMap.put(slice.getLabel(), new ArrayList<>(slice.getDatasetIds()));
            tchartData.put(slice.getLabel(), slice);
            plot.setExplodePercent(slice.getLabel(), 0);

        }
        this.selectAllLabel.setValue(coundDs + "");
        this.chartData.clear();
        this.chartData.putAll(tchartData);
        redrawChart();

    }
}
