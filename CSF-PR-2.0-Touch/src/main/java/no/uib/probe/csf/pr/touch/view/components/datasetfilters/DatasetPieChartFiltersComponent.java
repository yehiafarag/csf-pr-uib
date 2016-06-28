package no.uib.probe.csf.pr.touch.view.components.datasetfilters;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;
import no.uib.probe.csf.pr.touch.view.core.ImageContainerBtn;
import no.uib.probe.csf.pr.touch.view.core.PopupWindow;

/**
 *
 * @author Yehia Farag
 *
 * this class represents the dataset filter container with the interactive
 * management system for different interactive pie-chart filters
 */
public abstract class DatasetPieChartFiltersComponent extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private final PopupWindow popupWindow;
    private final GridLayout popupBody;
    private final Map<String, Map<Comparable, PieChartSlice>> fullFiltersData = new LinkedHashMap<>();
    private final Map<String, DatasetPieChartFilter> filtersSet = new LinkedHashMap<>();
    private final Map<Integer, Boolean> activeDatasetMap;
    private Map<Integer, QuantDatasetObject> quantDatasetMap;
    private boolean singlefilter;

    public DatasetPieChartFiltersComponent() {

        //init icon
        this.setWidth(25, Unit.PIXELS);
        this.setHeight(25, Unit.PIXELS);
        this.setStyleName("filterbtn");
        Image icon = new Image();
        icon.setSource(new ThemeResource("img/pie-chart.png"));
        this.addComponent(icon);
        icon.setWidth(100, Unit.PERCENTAGE);
        icon.setHeight(100, Unit.PERCENTAGE);
        this.addLayoutClickListener(DatasetPieChartFiltersComponent.this);

        //init window layout 
        VerticalLayout frame = new VerticalLayout();
        frame.setWidth(99, Unit.PERCENTAGE);
        frame.setSpacing(true);
        popupBody = new GridLayout(4, 4);
        frame.addComponent(popupBody);

        popupBody.addStyleName("roundedborder");
        popupBody.addStyleName("whitelayout");

        popupBody.setWidth(100, Unit.PERCENTAGE);
        popupBody.setHeight(700, Unit.PIXELS);
        popupBody.setSpacing(true);
        popupBody.setMargin(true);

        popupWindow = new PopupWindow(frame, "Dataset Explorer") {

            @Override
            public void close() {
                updateSystem(filterSelectionUnit());
                popupWindow.setVisible(false);

            }

        };
        popupWindow.setWidth(1100, Unit.PIXELS);
        popupWindow.setHeight(850, Unit.PIXELS);


        //init datasetructure
        activeDatasetMap = new HashMap<>();
        //init pie-chart filters
        this.initIntercativeDatasetFilters();

        VerticalLayout btnsFrame = new VerticalLayout();
        btnsFrame.setWidth(100, Unit.PERCENTAGE);
        btnsFrame.addStyleName("roundedborder");
        btnsFrame.addStyleName("margintop");
        btnsFrame.addStyleName("whitelayout");
        frame.addComponent(btnsFrame);

        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.setWidthUndefined();
        btnLayout.setSpacing(true);
        btnsFrame.addComponent(btnLayout);
        btnsFrame.setComponentAlignment(btnLayout, Alignment.MIDDLE_RIGHT);

//        this.popupBody.addComponent(btnLayout, 2, 2);
//        this.popupBody.setComponentAlignment(btnLayout, Alignment.MIDDLE_CENTER);
        Button applyFilters = new Button("Apply");
        applyFilters.setDescription("Apply the selected filters");
        applyFilters.setStyleName(ValoTheme.BUTTON_TINY);

        btnLayout.addComponent(applyFilters);
        applyFilters.addClickListener((Button.ClickEvent event) -> {
            popupWindow.close();
        });

        Button unselectAllBtn = new Button("Clear");
        unselectAllBtn.setStyleName(ValoTheme.BUTTON_TINY);
        btnLayout.addComponent(unselectAllBtn);
        btnLayout.setComponentAlignment(unselectAllBtn, Alignment.TOP_LEFT);
        unselectAllBtn.setDescription("Clear All Selections");
        unselectAllBtn.addClickListener((Button.ClickEvent event) -> {
            singlefilter = false;
            resetFilters();
        });

        Button resetFiltersBtn = new Button("Reset");
        resetFiltersBtn.setStyleName(ValoTheme.BUTTON_TINY);
        btnLayout.addComponent(resetFiltersBtn);
        resetFiltersBtn.setDescription("Reset all applied filters");
        resetFiltersBtn.addClickListener((Button.ClickEvent event) -> {
//                internalSelectionManager.resetToInitState();
//                internalSelectionManager.resetCentralSelectionManager();
        });
        
        
        ImageContainerBtn exportPdfBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        };

        exportPdfBtn.setHeight(28, Unit.PIXELS);
        exportPdfBtn.setWidth(28, Unit.PIXELS);
        exportPdfBtn.updateIcon(new ThemeResource("img/pdf-text-o.png"));
        exportPdfBtn.setEnabled(true);
        btnLayout.addComponent(exportPdfBtn);
        btnLayout.setComponentAlignment(exportPdfBtn, Alignment.MIDDLE_CENTER);
        exportPdfBtn.setDescription("Export all charts filters as pdf file");

//        VerticalLayout exportChartsBtn = new VerticalLayout();
//        exportChartsBtn.setStyleName("exportpdfbtn");
//        btnLayout.addComponent(exportChartsBtn);
//        exportChartsBtn.setDescription("Export all charts filters as pdf file");
//        exportChartsBtn.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//               String url = handler.exportImgAsPdf(chartSet, "piechart_filters.pdf");
//                FileResource res = new FileResource(new File(url));
//                Page.getCurrent().open(res, "_blank", true);
//            }
//        });

//        StreamResource myResource = createResource(handler);
//        FileDownloader fileDownloader = new FileDownloader(myResource);
//        fileDownloader.extend(exportChartsBtn);
    }

    private void initIntercativeDatasetFilters() {
        int filterHeight = 300;//(bodyHeight - 200) / 3;
        int filterWidth = 300;//(bodyWidth - 200) / 3;
        filtersSet.clear();
        DatasetPieChartFilter yearFilter = initPieChartFilter("Year", "yearFilter", 0, filterWidth, filterHeight);
        this.popupBody.addComponent(yearFilter, 0, 0);
        this.popupBody.setComponentAlignment(yearFilter, Alignment.TOP_LEFT);
        filtersSet.put("yearFilter", yearFilter);

        DatasetPieChartFilter studyTypeFilter = initPieChartFilter("Study Type", "studyTypeFilter", 1, filterWidth, filterHeight);
        this.popupBody.addComponent(studyTypeFilter, 1, 0);
        this.popupBody.setComponentAlignment(studyTypeFilter, Alignment.TOP_CENTER);
        filtersSet.put("studyTypeFilter", studyTypeFilter);

        DatasetPieChartFilter sampleMatchingFilter = initPieChartFilter("Sample Matching", "sampleMatchingFilter", 1, filterWidth, filterHeight);
        this.popupBody.addComponent(sampleMatchingFilter, 2, 0);
        this.popupBody.setComponentAlignment(sampleMatchingFilter, Alignment.TOP_RIGHT);
        filtersSet.put("sampleMatchingFilter", sampleMatchingFilter);

        DatasetPieChartFilter technologyFilter = initPieChartFilter("Technology", "technologyFilter", 1, filterWidth, filterHeight);
        this.popupBody.addComponent(technologyFilter, 0, 1);
        this.popupBody.setComponentAlignment(technologyFilter, Alignment.BOTTOM_LEFT);
        filtersSet.put("technologyFilter", technologyFilter);

        DatasetPieChartFilter analyticalApproachFilter = initPieChartFilter("Analytical Approach", "analyticalApproachFilter", 1, filterWidth, filterHeight);
        this.popupBody.addComponent(analyticalApproachFilter, 1, 1);
        this.popupBody.setComponentAlignment(analyticalApproachFilter, Alignment.BOTTOM_CENTER);
        filtersSet.put("analyticalApproachFilter", analyticalApproachFilter);

        DatasetPieChartFilter shotgunTargetedFilter = initPieChartFilter("Shotgun/Targeted", "shotgunTargetedFilter", 1, filterWidth, filterHeight);
        this.popupBody.addComponent(shotgunTargetedFilter, 2, 1);
        this.popupBody.setComponentAlignment(shotgunTargetedFilter, Alignment.BOTTOM_RIGHT);
        filtersSet.put("shotgunTargetedFilter", shotgunTargetedFilter);

    }

    /**
     * this method responsible for updating the main datasets across all
     * pie-charts the update happened mainly on changing disease category
     *
     * @param quantDatasetMap
     */
    private void updateQuantDatasetMap(Map<Integer, QuantDatasetObject> quantDatasetMap) {
        Set<Integer> finalSelectedIds = filterSelectionUnit();
        if (finalSelectedIds.size() == quantDatasetMap.size() && finalSelectedIds.containsAll(quantDatasetMap.keySet())) {
            return;
        }
        this.quantDatasetMap = quantDatasetMap;
        updatePieChartSliceSet();

    }

    public void updateFullDatasetIds() {

    }

    private void updatePieChartSliceSet() {
        fullFiltersData.clear();
        activeDatasetMap.clear();
        Map<Comparable, PieChartSlice> yearMap = new TreeMap<>();
        Map<Comparable, PieChartSlice> studyTypeMap = new TreeMap<>();
        Map<Comparable, PieChartSlice> sampleMatchingMap = new TreeMap<>();
        Map<Comparable, PieChartSlice> technologyMap = new TreeMap<>();
        Map<Comparable, PieChartSlice> analyticalApproachMap = new TreeMap<>();
        Map<Comparable, PieChartSlice> shotgunTargetedMap = new TreeMap<>();

        quantDatasetMap.values().stream().map((dataset) -> {
            activeDatasetMap.put(dataset.getDsKey(), Boolean.TRUE);
            return dataset;
        }).map((dataset) -> {
            if (!yearMap.containsKey(dataset.getYear())) {
                PieChartSlice slice = new PieChartSlice();
                slice.setLabel(dataset.getYear());
                yearMap.put(dataset.getYear(), slice);
            }
            return dataset;
        }).map((dataset) -> {
            PieChartSlice yearSlice = yearMap.get(dataset.getYear());
            yearSlice.setDatasetIds(dataset.getDsKey());
            yearMap.put(dataset.getYear(), yearSlice);
            return dataset;
        }).map((dataset) -> {
            if (!studyTypeMap.containsKey(dataset.getTypeOfStudy())) {
                PieChartSlice slice = new PieChartSlice();
                slice.setLabel(dataset.getTypeOfStudy());
                studyTypeMap.put(dataset.getTypeOfStudy(), slice);
            }
            return dataset;
        }).map((dataset) -> {
            PieChartSlice studyTypeSlice = studyTypeMap.get(dataset.getTypeOfStudy());
            studyTypeSlice.setDatasetIds(dataset.getDsKey());
            studyTypeMap.put(dataset.getTypeOfStudy(), studyTypeSlice);
            return dataset;
        }).map((dataset) -> {
            if (!sampleMatchingMap.containsKey(dataset.getSampleMatching())) {
                PieChartSlice slice = new PieChartSlice();
                slice.setLabel(dataset.getSampleMatching());
                sampleMatchingMap.put(dataset.getSampleMatching(), slice);
            }
            return dataset;
        }).map((dataset) -> {
            PieChartSlice sampleMachingSlice = sampleMatchingMap.get(dataset.getSampleMatching());
            sampleMachingSlice.setDatasetIds(dataset.getDsKey());
            sampleMatchingMap.put(dataset.getSampleMatching(), sampleMachingSlice);
            return dataset;
        }).map((dataset) -> {
            if (!technologyMap.containsKey(dataset.getTechnology())) {
                PieChartSlice slice = new PieChartSlice();
                slice.setLabel(dataset.getTechnology());
                technologyMap.put(dataset.getTechnology(), slice);
            }
            return dataset;
        }).map((dataset) -> {
            PieChartSlice technologySlice = technologyMap.get(dataset.getTechnology());
            technologySlice.setDatasetIds(dataset.getDsKey());
            technologyMap.put(dataset.getTechnology(), technologySlice);
            return dataset;
        }).map((dataset) -> {
            if (!analyticalApproachMap.containsKey(dataset.getAnalyticalApproach())) {
                PieChartSlice slice = new PieChartSlice();
                slice.setLabel(dataset.getAnalyticalApproach());
                analyticalApproachMap.put(dataset.getAnalyticalApproach(), slice);
            }
            return dataset;
        }).map((dataset) -> {
            PieChartSlice analyticalApproachSlice = analyticalApproachMap.get(dataset.getAnalyticalApproach());
            analyticalApproachSlice.setDatasetIds(dataset.getDsKey());
            analyticalApproachMap.put(dataset.getAnalyticalApproach(), analyticalApproachSlice);
            return dataset;
        }).map((dataset) -> {
            if (!shotgunTargetedMap.containsKey(dataset.getShotgunTargeted())) {
                PieChartSlice slice = new PieChartSlice();
                slice.setLabel(dataset.getShotgunTargeted());
                shotgunTargetedMap.put(dataset.getShotgunTargeted(), slice);
            }
            return dataset;
        }).forEach((dataset) -> {
            PieChartSlice shotgunTargetedSlice = shotgunTargetedMap.get(dataset.getShotgunTargeted());
            shotgunTargetedSlice.setDatasetIds(dataset.getDsKey());
            shotgunTargetedMap.put(dataset.getShotgunTargeted(), shotgunTargetedSlice);
        });

        filtersSet.get("yearFilter").initializeFilterData(yearMap);
        fullFiltersData.put("yearFilter", yearMap);

        filtersSet.get("studyTypeFilter").initializeFilterData(studyTypeMap);
        fullFiltersData.put("studyTypeFilter", studyTypeMap);

        filtersSet.get("sampleMatchingFilter").initializeFilterData(sampleMatchingMap);
        fullFiltersData.put("sampleMatchingFilter", sampleMatchingMap);

        filtersSet.get("technologyFilter").initializeFilterData(technologyMap);
        fullFiltersData.put("technologyFilter", technologyMap);

        filtersSet.get("analyticalApproachFilter").initializeFilterData(analyticalApproachMap);
        fullFiltersData.put("analyticalApproachFilter", analyticalApproachMap);

        filtersSet.get("shotgunTargetedFilter").initializeFilterData(shotgunTargetedMap);
        fullFiltersData.put("shotgunTargetedFilter", shotgunTargetedMap);

    }

    private DatasetPieChartFilter initPieChartFilter(String title, String filterId, int index, int width, int height) {
        DatasetPieChartFilter filter = new DatasetPieChartFilter(title, filterId, index, width, height) {
            @Override
            public void selectDatasets(boolean noselection) {
                Set<Integer> finalSelectionIds = filterSelectionUnit();
                if (noselection) {
                    updateFilters(finalSelectionIds, "");
                } else {
                    updateFilters(finalSelectionIds, filterId);
                }
            }
        };
        return filter;
    }

    private Set<Integer> filterSelectionUnit() {
        singlefilter = false;
        Map<Integer, Integer> dsCounter = new HashMap<>();
        int counter = 0;
        for (DatasetPieChartFilter filter : filtersSet.values()) {
            if (filter.isActiveFilter()) {
                counter++;
            }

            filter.getSelectedDsIds().stream().map((id) -> {
                if (!dsCounter.containsKey(id)) {
                    dsCounter.put(id, 0);
                }
                return id;
            }).forEach((id) -> {
                dsCounter.put(id, dsCounter.get(id) + 1);
            });

        }
        Set<Integer> finalSelectionIds = new HashSet<>();
        dsCounter.keySet().stream().filter((dsId) -> (dsCounter.get(dsId) == 6)).forEach((dsId) -> {
            finalSelectionIds.add(dsId);
        });
        if (counter == 1) {
            singlefilter = true;
        }
        return finalSelectionIds;

    }

    private void updateFilters(Collection<Integer> datasetIds, String filterId) {
        filtersSet.keySet().stream().filter((keyFilterId) -> !(keyFilterId.equalsIgnoreCase(filterId))).map((keyFilterId) -> filtersSet.get(keyFilterId)).forEach((filter) -> {
            filter.localUpdate(datasetIds, singlefilter);
        });
    }

    private void resetFilters() {
        filtersSet.values().stream().forEach((filter) -> {
            filter.reset();
        });

    }

    /**
     * Update the system with the current selected dataset ids
     *
     * @param selectedDatasetIds
     */
    public abstract void updateSystem(Set<Integer> selectedDatasetIds);

    /**
     * Update the system with the current selected dataset ids
     *
     * @return get the set of filtered datasets
     */
    public abstract Map<Integer, QuantDatasetObject> updatedDatasets();

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        this.updateQuantDatasetMap(this.updatedDatasets());
        popupWindow.setVisible(true);
    }

    public Set<Integer> checkAndFilter(Map<Integer, QuantDatasetObject> quantDatasetToFilter) {
        this.updateQuantDatasetMap(quantDatasetToFilter);
        return filterSelectionUnit();

    }
}
