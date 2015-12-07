/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.interactivepiechartfilters;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jfree.chart.JFreeChart;
import probe.com.handlers.CSFPRHandler;
import probe.com.model.beans.quant.QuantDatasetObject;
import probe.com.selectionmanager.QuantCentralManager;
import probe.com.selectionmanager.StudiesFilterManager;
import probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.PopupInteractiveDSFiltersLayout;

/**
 *
 * @author Yehia Farag
 */
public class StudiesPieChartFiltersContainerLayout extends GridLayout {

//     private JfreeExporter exporter = new JfreeExporter();
    private final PieChartsSelectionManager internalSelectionManager;
    private PopupInteractiveDSFiltersLayout pieChartFiltersBtn;
    private final Set<JFreeChart> chartSet = new LinkedHashSet<JFreeChart>();

    /**
     *
     * @param Quant_Central_Manager
     * @param handler
     */
    public StudiesPieChartFiltersContainerLayout(QuantCentralManager Quant_Central_Manager, final CSFPRHandler handler) {

        int layoutHeight = Page.getCurrent().getBrowserWindowHeight() - 200;
        int layoutWidth = Page.getCurrent().getBrowserWindowWidth() - 200;
        this.setWidth(layoutWidth + "px");
        this.setHeight(layoutHeight + "px");
        int filterWidth = layoutWidth / 3;
        this.setSpacing(true);
        boolean[] activeFilters = Quant_Central_Manager.getActiveFilters();
        Map<Integer, QuantDatasetObject> quantDatasetArr = Quant_Central_Manager.getFilteredDatasetsList();

        internalSelectionManager = new PieChartsSelectionManager(Quant_Central_Manager);
        if (quantDatasetArr == null) {
            return;
        }
        this.setRows(4);
        this.setColumns(4);
        int colCounter = 0;
        int rowCounter = 0;
        this.chartSet.clear();
        for (int x = 0; x < activeFilters.length; x++) {
            String filterId = "";
            if (activeFilters[x]) {
                Map<String, List<Integer>> dsIndexesMap = new HashMap<String, List<Integer>>();
                List<Object> valueSet = new ArrayList<Object>();
                switch (x) {
                    case 0:
//                        filterId = "identifiedProteinsNumber";
//                        for (QuantDatasetObject pb : quantDatasetArr) {
//                            if (pb == null) {
//                                continue;
//                            }
//                            int value = pb.getIdentifiedProteinsNumber();
//                            valueSet.add(value);
//                        }
                        break;

                    case 1:
//                        filterId = "quantifiedProteinsNumber";
//                        for (QuantDatasetObject pb : quantDatasetArr) {
//                            if (pb == null) {
//                                continue;
//                            }
//                            int value = pb.getQuantifiedProteinsNumber();
//                            valueSet.add(value);
//
//                        }
                        break;

                    case 2:
//                        filterId = "analyticalMethod";
//                        for (QuantDatasetObject pb : quantDatasetArr.values()) {
//                            if (pb == null) {
//                                continue;
//                            }
//                            String value = pb.getAnalyticalMethod();
//                            valueSet.add(value);
//
//                        }
                        break;

                    case 3:
//                        filterId = "rawDataUrl";
//                        for (QuantDatasetObject pb : quantDatasetArr.values()) {
//
//                            if (pb == null) {
//                                continue;
//                            }
//                            if (!dsIndexesMap.containsKey(pb.getRawDataUrl())) {
//                                List<Integer> list = new ArrayList<Integer>();
//                                dsIndexesMap.put(pb.getRawDataUrl(), list);
//
//                            }
//                            List<Integer> list = dsIndexesMap.get(pb.getRawDataUrl());
//                            list.add(pb.getUniqId());
//                            dsIndexesMap.put(pb.getRawDataUrl(), list);
//                            valueSet.add(pb.getRawDataUrl());
//
//                        }
                        break;
                    case 4:

                        filterId = "year";
                        for (QuantDatasetObject pb : quantDatasetArr.values()) {
                            if (pb == null) {
                                continue;
                            }
                            if (!dsIndexesMap.containsKey(pb.getYear() + "")) {
                                List<Integer> list = new ArrayList<Integer>();
                                dsIndexesMap.put(pb.getYear() + "", list);

                            }
                            List<Integer> list = dsIndexesMap.get(pb.getYear() + "");
                            list.add(pb.getDsKey());
                            dsIndexesMap.put(pb.getYear() + "", list);
                            int value = pb.getYear();
                            valueSet.add(value);
                        }
                        break;
                    case 5:
                        filterId = "typeOfStudy";
                        for (QuantDatasetObject pb : quantDatasetArr.values()) {
                            if (pb == null) {
                                continue;
                            }
                            if (!dsIndexesMap.containsKey(pb.getTypeOfStudy())) {
                                List<Integer> list = new ArrayList<Integer>();
                                dsIndexesMap.put(pb.getTypeOfStudy(), list);

                            }
                            if (pb.getTypeOfStudy().trim().equalsIgnoreCase("")) {
                                pb.setTypeOfStudy("Not Available");
                            }
                            List<Integer> list = dsIndexesMap.get(pb.getTypeOfStudy());
                            list.add(pb.getDsKey());
                            dsIndexesMap.put(pb.getTypeOfStudy(), list);
                            String value = pb.getTypeOfStudy();
                            valueSet.add(value);
                        }
                        break;
                    case 6:
                        filterId = "sampleType";
                        for (QuantDatasetObject pb : quantDatasetArr.values()) {
                            if (pb == null) {
                                continue;
                            }
                            if (pb.getSampleType().trim().equalsIgnoreCase("")) {
                                pb.setSampleType("Not Available");
                            }
                            if (!dsIndexesMap.containsKey(pb.getSampleType())) {
                                List<Integer> list = new ArrayList<Integer>();
                                dsIndexesMap.put(pb.getSampleType(), list);

                            }
                            List<Integer> list = dsIndexesMap.get(pb.getSampleType());
                            list.add(pb.getDsKey());
                            dsIndexesMap.put(pb.getSampleType(), list);
                            String value = pb.getSampleType();
                            valueSet.add(value);
                        }
                        break;
                    case 7:
                        filterId = "sampleMatching";
                        for (QuantDatasetObject pb : quantDatasetArr.values()) {
                            if (pb == null) {
                                continue;
                            }
                            if (pb.getSampleMatching().trim().equalsIgnoreCase("")) {
                                pb.setSampleMatching("Not Available");
                            }
                            if (!dsIndexesMap.containsKey(pb.getSampleMatching())) {
                                List<Integer> list = new ArrayList<Integer>();
                                dsIndexesMap.put(pb.getSampleMatching(), list);

                            }
                            List<Integer> list = dsIndexesMap.get(pb.getSampleMatching());
                            list.add(pb.getDsKey());
                            dsIndexesMap.put(pb.getSampleMatching(), list);

                            String value = pb.getSampleMatching();
                            valueSet.add(value);
                        }
                        break;
                    case 8:
                        filterId = "technology";
                        for (QuantDatasetObject pb : quantDatasetArr.values()) {
                            if (pb == null) {
                                continue;
                            }
                            String value = pb.getTechnology();
                            if (value == null || value.equalsIgnoreCase("")) {
                                value = "Not Available";
                            }
                            if (!dsIndexesMap.containsKey(value)) {
                                List<Integer> list = new ArrayList<Integer>();
                                dsIndexesMap.put(value, list);

                            }
                            List<Integer> list = dsIndexesMap.get(value);
                            list.add(pb.getDsKey());
                            dsIndexesMap.put(value, list);
                            valueSet.add(value);
                        }
                        break;
                    case 9:

                        filterId = "analyticalApproach";
                        for (QuantDatasetObject pb : quantDatasetArr.values()) {
                            if (pb == null) {
                                continue;
                            }
                            String value = pb.getAnalyticalApproach();
                            if (value == null || value.trim().equalsIgnoreCase("")) {
                                pb.setAnalyticalApproach("Not Available");
                                value = "Not Available";
                            }
                            if (!dsIndexesMap.containsKey(value)) {
                                List<Integer> list = new ArrayList<Integer>();
                                dsIndexesMap.put(value, list);

                            }
                            List<Integer> list = dsIndexesMap.get(pb.getAnalyticalApproach());
                            list.add(pb.getDsKey());
                            dsIndexesMap.put(value, list);
                            valueSet.add(value);
                        }
                        break;
                    case 10:
                        filterId = "enzyme";
                        for (QuantDatasetObject pb : quantDatasetArr.values()) {
                            if (pb == null) {
                                continue;
                            }
                            String value = pb.getEnzyme();
                            if (value == null || value.trim().equalsIgnoreCase("")) {
                                value = "Not Available";
                                pb.setEnzyme(value);
                            }
                            if (!dsIndexesMap.containsKey(value)) {
                                List<Integer> list = new ArrayList<Integer>();
                                dsIndexesMap.put(value, list);

                            }
                            List<Integer> list = dsIndexesMap.get(value);
                            list.add(pb.getDsKey());
                            dsIndexesMap.put(value, list);
                            valueSet.add(value);
                        }
                        break;
                    case 11:
                        filterId = "shotgunTargeted";
                        for (QuantDatasetObject pb : quantDatasetArr.values()) {
                            if (pb == null) {
                                continue;
                            }
                            String value = pb.getShotgunTargeted();
                            if (value == null || value.trim().equalsIgnoreCase("")) {
                                value = "Not Available";
                                pb.setShotgunTargeted(value);
                            }
                            if (!dsIndexesMap.containsKey(value)) {
                                List<Integer> list = new ArrayList<Integer>();
                                dsIndexesMap.put(value, list);

                            }
                            List<Integer> list = dsIndexesMap.get(value);
                            list.add(pb.getDsKey());
                            dsIndexesMap.put(value, list);
                            valueSet.add(value);
                        }
                        break;

                    case 12:
//                        filterId = "quantificationBasis";
//                        for (QuantDatasetObject pb : quantDatasetArr.values()) {
//                            if (pb == null) {
//                                continue;
//                            }
//                            String value = pb.getQuantificationBasis();
//                            if (value == null || value.trim().equalsIgnoreCase("")) {
//                                value = "Not Available";
//                                pb.setQuantificationBasis(value);
//                            }
//                            if (!dsIndexesMap.containsKey(value)) {
//                                List<Integer> list = new ArrayList<Integer>();
//                                dsIndexesMap.put(value, list);
//
//                            }
//                            List<Integer> list = dsIndexesMap.get(value);
//                            list.add(pb.getDsKey());
//                            dsIndexesMap.put(value, list);
//                            valueSet.add(value);
//                        }
                        break;
                    case 13:
                        filterId = "quantBasisComment";
                        for (QuantDatasetObject pb : quantDatasetArr.values()) {
                            if (pb == null) {
                                continue;
                            }
                            String value = pb.getQuantBasisComment();
                            if (value == null || value.trim().equalsIgnoreCase("")) {
                                value = "Not Available";
                                pb.setQuantBasisComment(value);
                            }
                            if (!dsIndexesMap.containsKey(value)) {
                                List<Integer> list = new ArrayList<Integer>();
                                dsIndexesMap.put(value, list);

                            }
                            List<Integer> list = dsIndexesMap.get(value);
                            list.add(pb.getDsKey());
                            dsIndexesMap.put(value, list);
                            valueSet.add(value);
                        }
                        break;
                    case 14:
//                        for (QuantDatasetObject pb : QuantDatasetListObject) {
//                            int value = pb.getQuantifiedProteinsNumber();
//                            valueSet.add(value);
//                        }
                        break;
                    case 15:
//                        for (QuantDatasetObject pb : QuantDatasetListObject) {
//                            int value = pb.getPatientsGroup1Number();
//                            valueSet.add(value);
//                        }
                        break;
                    case 16:
//                        for (QuantDatasetObject pb : QuantDatasetListObject) {
//                            int value = pb.getPatientsGroup2Number();
//                            valueSet.add(value);
//                        }
                        break;
                    case 17:
//                        for (QuantDatasetObject pb : QuantDatasetListObject) {
//                            String value = pb.getNormalizationStrategy();
//                            valueSet.add(value);
//                        }
                        break;

                }
//                if (!valueSet.isEmpty()) {
                //do we need valueSet;;
                JfreeDivaPieChartFilter iFilter = new JfreeDivaPieChartFilter(filterId, x, internalSelectionManager, dsIndexesMap, filterWidth);
                chartSet.add(iFilter.getChart());
//                    fullFilterList.put(filterId, valueSet);
                this.addComponent(iFilter, colCounter++, rowCounter);
                this.setComponentAlignment(iFilter, Alignment.MIDDLE_CENTER);
                if (colCounter == 3) {
                    colCounter = 0;
                    rowCounter++;
                }
            }

//            }
        }
        Quant_Central_Manager.setStudiesOverviewPieChart(chartSet);
        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.setHeight("23px");
        btnLayout.setWidthUndefined();
        btnLayout.setSpacing(true);
        btnLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        if (colCounter == 3) {
            this.addComponent(btnLayout, 2, ++rowCounter);
        } else {
            this.addComponent(btnLayout, 2, rowCounter);
        }

        this.setComponentAlignment(btnLayout, Alignment.MIDDLE_CENTER);
        Button applyFilters = new Button("Apply");
        applyFilters.setDescription("Apply the selected filters");
        applyFilters.setPrimaryStyleName("resetbtn");
        applyFilters.setWidth("50px");
        applyFilters.setHeight("24px");

        btnLayout.addComponent(applyFilters);
        applyFilters.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                pieChartFiltersBtn.closePupupWindow();
            }
        });

//        Button unselectAllBtn = new Button("Unselect All");
//        unselectAllBtn.setStyleName(Reindeer.BUTTON_SMALL);
//        btnLayout.addComponent(unselectAllBtn);
//        unselectAllBtn.addClickListener(new Button.ClickListener() {
//
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//
//                internalSelectionManager.unselectAll();
//
//            }
//        });
//        
        VerticalLayout unselectAllBtn = new VerticalLayout();
        unselectAllBtn.setStyleName("unselectallbtn");
        btnLayout.addComponent(unselectAllBtn);
        btnLayout.setComponentAlignment(unselectAllBtn, Alignment.TOP_LEFT);
        unselectAllBtn.setDescription("Unselect All Filters");
        unselectAllBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                internalSelectionManager.unselectAll();
            }
        });

        Button resetFiltersBtn = new Button("Reset");
        resetFiltersBtn.setPrimaryStyleName("resetbtn");
        resetFiltersBtn.setWidth("50px");
        resetFiltersBtn.setHeight("24px");
        btnLayout.addComponent(resetFiltersBtn);
        resetFiltersBtn.setDescription("Reset all applied filters");
        resetFiltersBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                internalSelectionManager.resetToInitState();
                internalSelectionManager.resetCentralSelectionManager();
            }
        });

        Button exportChartsBtn = new Button("");

        exportChartsBtn.setWidth("24px");
        exportChartsBtn.setHeight("24px");
        exportChartsBtn.setPrimaryStyleName("exportpdfbtn");
        btnLayout.addComponent(exportChartsBtn);
        exportChartsBtn.setDescription("Export all charts filters as pdf file");
//        exportChartsBtn.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//               String url = handler.exportImgAsPdf(chartSet, "piechart_filters.pdf");
//                FileResource res = new FileResource(new File(url));
//                Page.getCurrent().open(res, "_blank", true);
//            }
//        });

        StreamResource myResource = createResource(handler);
        FileDownloader fileDownloader = new FileDownloader(myResource);
        fileDownloader.extend(exportChartsBtn);
        pieChartFiltersBtn = new PopupInteractiveDSFiltersLayout(this);
    }

    /**
     *
     * @return
     */
    public PopupInteractiveDSFiltersLayout getPieChartFiltersBtn() {
        return pieChartFiltersBtn;
    }

    /**
     *
     */
    public void updatePieChartCharts() {
        internalSelectionManager.resetPieChartCharts();

    }

    /**
     *
     * @param selfSelection
     */
    public void updateSelectionManager(boolean selfSelection) {
        internalSelectionManager.applyFilterSelectionToCentralManager(selfSelection);

    }

    private StreamResource createResource(final CSFPRHandler handler) {
        return new StreamResource(new StreamResource.StreamSource() {
            @Override
            @SuppressWarnings("CallToPrintStackTrace")
            public InputStream getStream() {

//                BufferedImage bi = chart.createBufferedImage(width, height, chartRenderingInfo);
                try {

                    byte[] pdfFile = handler.exportStudiesInformationPieCharts(chartSet, "piechart_filters.pdf", "Studies Overview");
                    return new ByteArrayInputStream(pdfFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }
        }, "piechart_filters.pdf");
    }

}
