/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.components.datasetfilters;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;
import org.jfree.chart.JFreeChart;

/**
 *
 * @author Yehia Farag
 */
public class DatasetPieChartFiltersComponent extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private final Window popupWindow;
    private final GridLayout popupBody;
//     private JfreeExporter exporter = new JfreeExporter();
//    private final PieChartsSelectionManager internalSelectionManager;
//    private PopupInteractiveDSFiltersLayout pieChartFiltersBtn;
    private final Set<JFreeChart> chartSet = new LinkedHashSet<JFreeChart>();
    private final Map<String, Map<Comparable, PieChartSlice>> fullFiltersData = new LinkedHashMap<>();
    private final Map<String, DatasetPieChartFilter> filtersSet = new LinkedHashMap<>();
    private final Set<Integer> fullDatasetIds;
    private final Set<Integer> selectedDatasetIds;

    private Map<Integer, QuantDatasetObject> quantDatasetMap;

    public DatasetPieChartFiltersComponent(/*QuantCentralManager Quant_Central_Manager, final CSFPRHandler handler*/) {

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
        popupBody = new GridLayout(4, 4);

        int layoutHeight = Page.getCurrent().getBrowserWindowHeight() - 200;
        int layoutWidth = Page.getCurrent().getBrowserWindowWidth() - 200;

        popupBody.setWidth(100, Unit.PERCENTAGE);
        popupBody.setHeight(100, Unit.PERCENTAGE);
        popupBody.setSpacing(true);

        popupWindow = new Window() {

            @Override
            public void close() {
                popupWindow.setVisible(false);

            }

        };

        popupWindow.setContent(this.popupBody);
        popupWindow.setWindowMode(WindowMode.NORMAL);

        popupWindow.setWidth((layoutWidth), Unit.PIXELS);
        popupWindow.setHeight((layoutHeight), Unit.PIXELS);
        popupWindow.setVisible(false);
        popupWindow.setResizable(false);
        popupWindow.setClosable(false);
        popupWindow.setModal(true);
        popupWindow.setDraggable(false);
        popupWindow.setCaption("<font color='gray' style='font-weight: bold;!important'>&nbsp;&nbsp;Dataset Explorer</font>");

        UI.getCurrent().addWindow(popupWindow);
        popupWindow.center();
        popupWindow.setCaptionAsHtml(true);
        popupWindow.setClosable(true);

        //init datasetructure
        this.fullDatasetIds = new LinkedHashSet<>();
        this.selectedDatasetIds = new LinkedHashSet<>();

        //init pie-chart filters
        this.initIntercativeDatasetFilters(layoutWidth, layoutHeight);

//        boolean[] activeFilters = Quant_Central_Manager.getActiveFilters();
//        Map<Integer, QuantDatasetObject> quantDatasetArr = Quant_Central_Manager.getFilteredDatasetsList();
//
//        internalSelectionManager = new PieChartsSelectionManager(Quant_Central_Manager);
//        if (quantDatasetArr == null) {
//            return;
//        }
//        int colCounter = 0;
//        int rowCounter = 0;
//        this.chartSet.clear();
//        for (int x = 0; x < activeFilters.length; x++) {
//            String filterId = "";
//            if (activeFilters[x]) {
//                Map<String, List<Integer>> dsIndexesMap = new HashMap<String, List<Integer>>();
////                List<Object> valueSet = new ArrayList<Object>();
//                switch (x) {
//                    case 0:
////                        filterId = "identifiedProteinsNumber";
////                        for (QuantDatasetObject pb : quantDatasetArr) {
////                            if (pb == null) {
////                                continue;
////                            }
////                            int value = pb.getIdentifiedProteinsNumber();
////                            valueSet.add(value);
////                        }
//                        break;
//
//                    case 1:
////                        filterId = "quantifiedProteinsNumber";
////                        for (QuantDatasetObject pb : quantDatasetArr) {
////                            if (pb == null) {
////                                continue;
////                            }
////                            int value = pb.getQuantifiedProteinsNumber();
////                            valueSet.add(value);
////
////                        }
//                        break;
//
//                    case 2:
////                        filterId = "analyticalMethod";
////                        for (QuantDatasetObject pb : quantDatasetArr.values()) {
////                            if (pb == null) {
////                                continue;
////                            }
////                            String value = pb.getAnalyticalMethod();
////                            valueSet.add(value);
////
////                        }
//                        break;
//
//                    case 3:
////                        filterId = "rawDataUrl";
////                        for (QuantDatasetObject pb : quantDatasetArr.values()) {
////
////                            if (pb == null) {
////                                continue;
////                            }
////                            if (!dsIndexesMap.containsKey(pb.getRawDataUrl())) {
////                                List<Integer> list = new ArrayList<Integer>();
////                                dsIndexesMap.put(pb.getRawDataUrl(), list);
////
////                            }
////                            List<Integer> list = dsIndexesMap.get(pb.getRawDataUrl());
////                            list.add(pb.getUniqId());
////                            dsIndexesMap.put(pb.getRawDataUrl(), list);
////                            valueSet.add(pb.getRawDataUrl());
////
////                        }
//                        break;
//                    case 4:
//
//                        filterId = "year";
//                        for (QuantDatasetObject pb : quantDatasetArr.values()) {
//                            if (pb == null) {
//                                continue;
//                            }
//                            if (!dsIndexesMap.containsKey(pb.getYear() + "")) {
//                                List<Integer> list = new ArrayList<Integer>();
//                                dsIndexesMap.put(pb.getYear() + "", list);
//
//                            }
//                            List<Integer> list = dsIndexesMap.get(pb.getYear() + "");
//                            list.add(pb.getDsKey());
//                            dsIndexesMap.put(pb.getYear() + "", list);
//                            int value = pb.getYear();
////                            valueSet.add(value);
//                        }
//                        break;
//                    case 5:
//                        filterId = "typeOfStudy";
//                        for (QuantDatasetObject pb : quantDatasetArr.values()) {
//                            if (pb == null) {
//                                continue;
//                            }
//                            if (!dsIndexesMap.containsKey(pb.getTypeOfStudy())) {
//                                List<Integer> list = new ArrayList<Integer>();
//                                dsIndexesMap.put(pb.getTypeOfStudy(), list);
//
//                            }
//                            if (pb.getTypeOfStudy().trim().equalsIgnoreCase("")) {
//                                pb.setTypeOfStudy("Not Available");
//                            }
//                            List<Integer> list = dsIndexesMap.get(pb.getTypeOfStudy());
//                            list.add(pb.getDsKey());
//                            dsIndexesMap.put(pb.getTypeOfStudy(), list);
//                            String value = pb.getTypeOfStudy();
////                            valueSet.add(value);
//                        }
//                        break;
//                    case 6:
//                        filterId = "sampleType";
//                        for (QuantDatasetObject pb : quantDatasetArr.values()) {
//                            if (pb == null) {
//                                continue;
//                            }
//                            if (pb.getSampleType().trim().equalsIgnoreCase("")) {
//                                pb.setSampleType("Not Available");
//                            }
//                            if (!dsIndexesMap.containsKey(pb.getSampleType())) {
//                                List<Integer> list = new ArrayList<Integer>();
//                                dsIndexesMap.put(pb.getSampleType(), list);
//
//                            }
//                            List<Integer> list = dsIndexesMap.get(pb.getSampleType());
//                            list.add(pb.getDsKey());
//                            dsIndexesMap.put(pb.getSampleType(), list);
//                            String value = pb.getSampleType();
////                            valueSet.add(value);
//                        }
//                        break;
//                    case 7:
//                        filterId = "sampleMatching";
//                        for (QuantDatasetObject pb : quantDatasetArr.values()) {
//                            if (pb == null) {
//                                continue;
//                            }
//                            if (pb.getSampleMatching().trim().equalsIgnoreCase("")) {
//                                pb.setSampleMatching("Not Available");
//                            }
//                            if (!dsIndexesMap.containsKey(pb.getSampleMatching())) {
//                                List<Integer> list = new ArrayList<Integer>();
//                                dsIndexesMap.put(pb.getSampleMatching(), list);
//
//                            }
//                            List<Integer> list = dsIndexesMap.get(pb.getSampleMatching());
//                            list.add(pb.getDsKey());
//                            dsIndexesMap.put(pb.getSampleMatching(), list);
//
//                            String value = pb.getSampleMatching();
////                            valueSet.add(value);
//                        }
//                        break;
//                    case 8:
//                        filterId = "technology";
//                        for (QuantDatasetObject pb : quantDatasetArr.values()) {
//                            if (pb == null) {
//                                continue;
//                            }
//                            String value = pb.getTechnology();
//                            if (value == null || value.equalsIgnoreCase("")) {
//                                value = "Not Available";
//                            }
//                            if (!dsIndexesMap.containsKey(value)) {
//                                List<Integer> list = new ArrayList<Integer>();
//                                dsIndexesMap.put(value, list);
//
//                            }
//                            List<Integer> list = dsIndexesMap.get(value);
//                            list.add(pb.getDsKey());
//                            dsIndexesMap.put(value, list);
////                            valueSet.add(value);
//                        }
//                        break;
//                    case 9:
//
//                        filterId = "analyticalApproach";
//                        for (QuantDatasetObject pb : quantDatasetArr.values()) {
//                            if (pb == null) {
//                                continue;
//                            }
//                            String value = pb.getAnalyticalApproach();
//                            if (value == null || value.trim().equalsIgnoreCase("")) {
//                                pb.setAnalyticalApproach("Not Available");
//                                value = "Not Available";
//                            }
//                            if (!dsIndexesMap.containsKey(value)) {
//                                List<Integer> list = new ArrayList<Integer>();
//                                dsIndexesMap.put(value, list);
//
//                            }
//                            List<Integer> list = dsIndexesMap.get(pb.getAnalyticalApproach());
//                            list.add(pb.getDsKey());
//                            dsIndexesMap.put(value, list);
////                            valueSet.add(value);
//                        }
//                        break;
//                    case 10:
//                        filterId = "enzyme";
//                        for (QuantDatasetObject pb : quantDatasetArr.values()) {
//                            if (pb == null) {
//                                continue;
//                            }
//                            String value = pb.getEnzyme();
//                            if (value == null || value.trim().equalsIgnoreCase("")) {
//                                value = "Not Available";
//                                pb.setEnzyme(value);
//                            }
//                            if (!dsIndexesMap.containsKey(value)) {
//                                List<Integer> list = new ArrayList<Integer>();
//                                dsIndexesMap.put(value, list);
//
//                            }
//                            List<Integer> list = dsIndexesMap.get(value);
//                            list.add(pb.getDsKey());
//                            dsIndexesMap.put(value, list);
////                            valueSet.add(value);
//                        }
//                        break;
//                    case 11:
//                        filterId = "shotgunTargeted";
//                        for (QuantDatasetObject pb : quantDatasetArr.values()) {
//                            if (pb == null) {
//                                continue;
//                            }
//                            String value = pb.getShotgunTargeted();
//                            if (value == null || value.trim().equalsIgnoreCase("")) {
//                                value = "Not Available";
//                                pb.setShotgunTargeted(value);
//                            }
//                            if (!dsIndexesMap.containsKey(value)) {
//                                List<Integer> list = new ArrayList<Integer>();
//                                dsIndexesMap.put(value, list);
//
//                            }
//                            List<Integer> list = dsIndexesMap.get(value);
//                            list.add(pb.getDsKey());
//                            dsIndexesMap.put(value, list);
////                            valueSet.add(value);
//                        }
//                        break;
//
//                    case 12:
////                        filterId = "quantificationBasis";
////                        for (QuantDatasetObject pb : quantDatasetArr.values()) {
////                            if (pb == null) {
////                                continue;
////                            }
////                            String value = pb.getQuantificationBasis();
////                            if (value == null || value.trim().equalsIgnoreCase("")) {
////                                value = "Not Available";
////                                pb.setQuantificationBasis(value);
////                            }
////                            if (!dsIndexesMap.containsKey(value)) {
////                                List<Integer> list = new ArrayList<Integer>();
////                                dsIndexesMap.put(value, list);
////
////                            }
////                            List<Integer> list = dsIndexesMap.get(value);
////                            list.add(pb.getDsKey());
////                            dsIndexesMap.put(value, list);
////                            valueSet.add(value);
////                        }
//                        break;
//                    case 13:
//                        filterId = "quantBasisComment";
//                        for (QuantDatasetObject pb : quantDatasetArr.values()) {
//                            if (pb == null) {
//                                continue;
//                            }
//                            String value = pb.getQuantBasisComment();
//                            if (value == null || value.trim().equalsIgnoreCase("")) {
//                                value = "Not Available";
//                                pb.setQuantBasisComment(value);
//                            }
//                            if (!dsIndexesMap.containsKey(value)) {
//                                List<Integer> list = new ArrayList<Integer>();
//                                dsIndexesMap.put(value, list);
//
//                            }
//                            List<Integer> list = dsIndexesMap.get(value);
//                            list.add(pb.getDsKey());
//                            dsIndexesMap.put(value, list);
////                            valueSet.add(value);
//                        }
//                        break;
//                    case 14:
////                        for (QuantDatasetObject pb : QuantDatasetListObject) {
////                            int value = pb.getQuantifiedProteinsNumber();
////                            valueSet.add(value);
////                        }
//                        break;
//                    case 15:
////                        for (QuantDatasetObject pb : QuantDatasetListObject) {
////                            int value = pb.getPatientsGroup1Number();
////                            valueSet.add(value);
////                        }
//                        break;
//                    case 16:
////                        for (QuantDatasetObject pb : QuantDatasetListObject) {
////                            int value = pb.getPatientsGroup2Number();
////                            valueSet.add(value);
////                        }
//                        break;
//                    case 17:
////                        for (QuantDatasetObject pb : QuantDatasetListObject) {
////                            String value = pb.getNormalizationStrategy();
////                            valueSet.add(value);
////                        }
//                        break;
//
//                }
////                if (!valueSet.isEmpty()) {
//                //do we need valueSet;;
//                DatasetPieChartFilter iFilter = new DatasetPieChartFilter(filterId, x, internalSelectionManager, dsIndexesMap, filterWidth);
//                chartSet.add(iFilter.getChart());
////                    fullFilterList.put(filterId, valueSet);
//                this.addComponent(iFilter, colCounter++, rowCounter);
//                this.setComponentAlignment(iFilter, Alignment.MIDDLE_CENTER);
//                if (colCounter == 3) {
//                    colCounter = 0;
//                    rowCounter++;
//                }
//            }
//
////            }
//        }
//        Quant_Central_Manager.setStudiesOverviewPieChart(chartSet);
//        HorizontalLayout btnLayout = new HorizontalLayout();
//        btnLayout.setHeight("23px");
//        btnLayout.setWidthUndefined();
//        btnLayout.setSpacing(true);
//        btnLayout.setStyleName(Reindeer.LAYOUT_WHITE);
//        if (colCounter == 3) {
//            this.addComponent(btnLayout, 2, ++rowCounter);
//        } else {
//            this.addComponent(btnLayout, 2, rowCounter);
//        }
//
//        this.setComponentAlignment(btnLayout, Alignment.MIDDLE_CENTER);
//        Button applyFilters = new Button("Apply");
//        applyFilters.setDescription("Apply the selected filters");
//        applyFilters.setPrimaryStyleName("resetbtn");
//        applyFilters.setWidth("50px");
//        applyFilters.setHeight("24px");
//
//        btnLayout.addComponent(applyFilters);
//        applyFilters.addClickListener(new Button.ClickListener() {
//
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//                pieChartFiltersBtn.closePupupWindow();
//            }
//        });
//
////        Button unselectAllBtn = new Button("Unselect All");
////        unselectAllBtn.setStyleName(Reindeer.BUTTON_SMALL);
////        btnLayout.addComponent(unselectAllBtn);
////        unselectAllBtn.addClickListener(new Button.ClickListener() {
////
////            @Override
////            public void buttonClick(Button.ClickEvent event) {
////
////                internalSelectionManager.unselectAll();
////
////            }
////        });
////        
//        Button unselectAllBtn = new Button("Clear");
//        unselectAllBtn.setPrimaryStyleName("resetbtn");
//        unselectAllBtn.setWidth("50px");
//        unselectAllBtn.setHeight("24px");
//        btnLayout.addComponent(unselectAllBtn);
//        btnLayout.setComponentAlignment(unselectAllBtn, Alignment.TOP_LEFT);
//        unselectAllBtn.setDescription("Clear All Selections");
//        unselectAllBtn.addClickListener(new Button.ClickListener() {
//
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//                internalSelectionManager.unselectAll();
//            }
//        });
//
//        Button resetFiltersBtn = new Button("Reset");
//        resetFiltersBtn.setPrimaryStyleName("resetbtn");
//        resetFiltersBtn.setWidth("50px");
//        resetFiltersBtn.setHeight("24px");
//        btnLayout.addComponent(resetFiltersBtn);
//        resetFiltersBtn.setDescription("Reset all applied filters");
//        resetFiltersBtn.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//                internalSelectionManager.resetToInitState();
//                internalSelectionManager.resetCentralSelectionManager();
//            }
//        });
//
//        Button exportChartsBtn = new Button("");
//
//        exportChartsBtn.setWidth("24px");
//        exportChartsBtn.setHeight("24px");
//        exportChartsBtn.setPrimaryStyleName("exportpdfbtn");
//        btnLayout.addComponent(exportChartsBtn);
//        exportChartsBtn.setDescription("Export all charts filters as pdf file");
////        exportChartsBtn.addClickListener(new Button.ClickListener() {
////            @Override
////            public void buttonClick(Button.ClickEvent event) {
////               String url = handler.exportImgAsPdf(chartSet, "piechart_filters.pdf");
////                FileResource res = new FileResource(new File(url));
////                Page.getCurrent().open(res, "_blank", true);
////            }
////        });
//
//        StreamResource myResource = createResource(handler);
//        FileDownloader fileDownloader = new FileDownloader(myResource);
//        fileDownloader.extend(exportChartsBtn);
//        pieChartFiltersBtn = new PopupInteractiveDSFiltersLayout(this);
    }

    private void initIntercativeDatasetFilters(int bodyWidth, int bodyHeight) {
        int filterHeight = (bodyHeight - 200) / 3;
        int filterWidth = (bodyWidth - 200) / 3;
        filtersSet.clear();
        DatasetPieChartFilter yearFilter = initPieChartFilter("Year", "yearFilter", 0, filterWidth, filterHeight);
        this.popupBody.addComponent(yearFilter, 0, 0);
        filtersSet.put("yearFilter", yearFilter);

        DatasetPieChartFilter studyTypeFilter = initPieChartFilter("Study Type", "studyTypeFilter", 1, filterWidth, filterHeight);
        this.popupBody.addComponent(studyTypeFilter, 1, 0);
        filtersSet.put("studyTypeFilter", studyTypeFilter);

        DatasetPieChartFilter sampleMatchingFilter = initPieChartFilter("Sample Matching", "sampleMatchingFilter", 1, filterWidth, filterHeight);
        this.popupBody.addComponent(sampleMatchingFilter, 2, 0);
        filtersSet.put("sampleMatchingFilter", sampleMatchingFilter);

        DatasetPieChartFilter technologyFilter = initPieChartFilter("Technology", "technologyFilter", 1, filterWidth, filterHeight);
        this.popupBody.addComponent(technologyFilter, 0, 1);
        filtersSet.put("technologyFilter", technologyFilter);

        DatasetPieChartFilter analyticalApproachFilter = initPieChartFilter("Analytical Approach", "analyticalApproachFilter", 1, filterWidth, filterHeight);
        this.popupBody.addComponent(analyticalApproachFilter, 1, 1);
        filtersSet.put("analyticalApproachFilter", analyticalApproachFilter);

        DatasetPieChartFilter shotgunTargetedFilter = initPieChartFilter("Shotgun/Targeted", "shotgunTargetedFilter", 1, filterWidth, filterHeight);
        this.popupBody.addComponent(shotgunTargetedFilter, 2, 1);
        filtersSet.put("shotgunTargetedFilter", shotgunTargetedFilter);

    }

    
    Map<String, Set<Integer>>filterSelectionRegistration = new HashMap<>();
    
    private DatasetPieChartFilter initPieChartFilter(String title, String filterId, int index, int width, int height) {
        DatasetPieChartFilter filter = new DatasetPieChartFilter(title, filterId, index, width, height) {

            @Override
            public void selectDatasets(Collection<Integer> datasetId, boolean selected) {
                selectedDatasetIds.clear();
//                if (selected) {
                    selectedDatasetIds.addAll(datasetId);
//                } else {
//                    selectedDatasetIds.removeAll(datasetId);
//                }
                updateFilters(datasetId, filterId);
            }

        };
        return filter;

    }

    private void updateFilters(Collection<Integer> datasetIds, String filterId) {
        if (selectedDatasetIds.isEmpty()) {
            selectedDatasetIds.addAll(fullDatasetIds);
        }
//        for (DatasetPieChartFilter filter : filtersSet.values()) {
//            filter.localUpdate(selectedDatasetIds);
//
//        }

        filtersSet.keySet().stream().filter((keyFilterId) -> !(keyFilterId.equalsIgnoreCase(filterId))).map((keyFilterId) -> filtersSet.get(keyFilterId)).forEach((filter) -> {
            filter.localUpdate(selectedDatasetIds);
        });
    }
    
    private void chackFilterSelections(Collection<Integer> datasetIds){
        List<Integer>dominantList = new ArrayList<>(datasetIds);
        for (DatasetPieChartFilter filter : filtersSet.values()) {
            if(!filter.getSelectedDsIds().isEmpty()){
                System.out.println("at "+filter.getFilter_Id()+"   "+filter.getSelectedDsIds());
            }
            

        }
    
    
    
    }

    public void updateQuantDatasetMap(Map<Integer, QuantDatasetObject> quantDatasetMap) {
        this.quantDatasetMap = quantDatasetMap;
        updatePieChartSliceSet();

    }

    private void updatePieChartSliceSet() {
        fullFiltersData.clear();
        fullDatasetIds.clear();
        Map<Comparable, PieChartSlice> yearMap = new TreeMap<>();
        Map<Comparable, PieChartSlice> studyTypeMap = new TreeMap<>();
        Map<Comparable, PieChartSlice> sampleMatchingMap = new TreeMap<>();
        Map<Comparable, PieChartSlice> technologyMap = new TreeMap<>();
        Map<Comparable, PieChartSlice> analyticalApproachMap = new TreeMap<>();
        Map<Comparable, PieChartSlice> shotgunTargetedMap = new TreeMap<>();

        for (QuantDatasetObject dataset : quantDatasetMap.values()) {
            fullDatasetIds.add(dataset.getDsKey());
            if (!yearMap.containsKey(dataset.getYear())) {
                PieChartSlice slice = new PieChartSlice();
                slice.setLabel(dataset.getYear());
                yearMap.put(dataset.getYear(), slice);
            }
            PieChartSlice yearSlice = yearMap.get(dataset.getYear());
            yearSlice.setDatasetIds(dataset.getDsKey());
            yearMap.put(dataset.getYear(), yearSlice);

            if (!studyTypeMap.containsKey(dataset.getTypeOfStudy())) {
                PieChartSlice slice = new PieChartSlice();
                slice.setLabel(dataset.getTypeOfStudy());
                studyTypeMap.put(dataset.getTypeOfStudy(), slice);
            }
            PieChartSlice studyTypeSlice = studyTypeMap.get(dataset.getTypeOfStudy());
            studyTypeSlice.setDatasetIds(dataset.getDsKey());
            studyTypeMap.put(dataset.getTypeOfStudy(), studyTypeSlice);

            if (!sampleMatchingMap.containsKey(dataset.getSampleMatching())) {
                PieChartSlice slice = new PieChartSlice();
                slice.setLabel(dataset.getSampleMatching());
                sampleMatchingMap.put(dataset.getSampleMatching(), slice);
            }
            PieChartSlice sampleMachingSlice = sampleMatchingMap.get(dataset.getSampleMatching());
            sampleMachingSlice.setDatasetIds(dataset.getDsKey());
            sampleMatchingMap.put(dataset.getSampleMatching(), sampleMachingSlice);

            if (!technologyMap.containsKey(dataset.getTechnology())) {
                PieChartSlice slice = new PieChartSlice();
                slice.setLabel(dataset.getTechnology());
                technologyMap.put(dataset.getTechnology(), slice);
            }
            PieChartSlice technologySlice = technologyMap.get(dataset.getTechnology());
            technologySlice.setDatasetIds(dataset.getDsKey());
            technologyMap.put(dataset.getTechnology(), technologySlice);

            if (!analyticalApproachMap.containsKey(dataset.getAnalyticalApproach())) {
                PieChartSlice slice = new PieChartSlice();
                slice.setLabel(dataset.getAnalyticalApproach());
                analyticalApproachMap.put(dataset.getAnalyticalApproach(), slice);
            }
            PieChartSlice analyticalApproachSlice = analyticalApproachMap.get(dataset.getAnalyticalApproach());
            analyticalApproachSlice.setDatasetIds(dataset.getDsKey());
            analyticalApproachMap.put(dataset.getAnalyticalApproach(), analyticalApproachSlice);

            if (!shotgunTargetedMap.containsKey(dataset.getShotgunTargeted())) {
                PieChartSlice slice = new PieChartSlice();
                slice.setLabel(dataset.getShotgunTargeted());
                shotgunTargetedMap.put(dataset.getShotgunTargeted(), slice);
            }
            PieChartSlice shotgunTargetedSlice = shotgunTargetedMap.get(dataset.getShotgunTargeted());
            shotgunTargetedSlice.setDatasetIds(dataset.getDsKey());
            shotgunTargetedMap.put(dataset.getShotgunTargeted(), shotgunTargetedSlice);

        }

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

    /**
     *
     * @return
     */
    //    public PopupInteractiveDSFiltersLayout getPieChartFiltersBtn() {
    //        return pieChartFiltersBtn;
    //    }
    /**
     *
     */
    public void updatePieChartCharts() {

//        internalSelectionManager.resetPieChartCharts();
    }

    /**
     *
     * @param selfSelection
     */
    public void updateSelectionManager(boolean selfSelection) {
//        internalSelectionManager.applyFilterSelectionToCentralManager(selfSelection);

    }

//    private StreamResource createResource(final CSFPRHandler handler) {
//        return new StreamResource(new StreamResource.StreamSource() {
//            @Override
//            @SuppressWarnings("CallToPrintStackTrace")
//            public InputStream getStream() {
//
////                BufferedImage bi = chart.createBufferedImage(width, height, chartRenderingInfo);
//                try {
//
//                    byte[] pdfFile = handler.exportStudiesInformationPieCharts(chartSet, "piechart_filters.pdf", "Datasets");
//                    return new ByteArrayInputStream(pdfFile);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return null;
//                }
//
//            }
//        }, "piechart_filters.pdf");
//    }
    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        popupWindow.setVisible(true);
    }
}
