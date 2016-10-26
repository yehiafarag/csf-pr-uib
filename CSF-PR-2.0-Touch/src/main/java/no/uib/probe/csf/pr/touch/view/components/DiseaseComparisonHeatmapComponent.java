package no.uib.probe.csf.pr.touch.view.components;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.VerticalLayout;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.Data_Handler;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseCategoryObject;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseGroupComparison;
import no.uib.probe.csf.pr.touch.logic.beans.HeatMapHeaderCellInformationBean;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDataset;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFListener;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFPR_Central_Manager;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFSelection;
import no.uib.probe.csf.pr.touch.view.components.datasetfilters.DatasetPieChartFiltersComponent;
import no.uib.probe.csf.pr.touch.view.components.datasetfilters.RecombineDiseaseGroupsCombonent;
import no.uib.probe.csf.pr.touch.view.components.datasetfilters.ReorderSelectGroupsComponent;
import no.uib.probe.csf.pr.touch.view.components.datasetfilters.SerumCsfFilter;
import no.uib.probe.csf.pr.touch.view.components.heatmapsubcomponents.HeatMapLayout;
import no.uib.probe.csf.pr.touch.view.components.heatmapsubcomponents.HeatmapFiltersContainerResizeControl;
import no.uib.probe.csf.pr.touch.view.core.ResizableTextLabel;

/**
 *
 * @author Yehia Farag
 *
 * This class represent the disease comparisons heat map component the class
 * allow users to select different disease comparisons
 */
public abstract class DiseaseComparisonHeatmapComponent extends VerticalLayout implements CSFListener {

    /*
     *The main heat map layout container
     */
    private final HeatMapLayout heatmapLayoutContainer;
    /*
     *The wrapper of dataset pie-chart filters component (represented as button with pop up layout)
     */
    private final DatasetPieChartFiltersComponent datasetPieChartFiltersComponent;
    /*
     *The wrapper of dataset recombine disease groups component (represented as button with pop up layout)
     */
    private final RecombineDiseaseGroupsCombonent reconbineDiseaseGroupsFiltersComponent;

    /*
     *The wrapper of Serum/CSF datasets filter component (represented as 2 buttons)
     */
    private final SerumCsfFilter serumCsfFilter;
    /*
     *The wrapper of dataset Select/Reortder disease groups component (represented as button with pop up layout)
     */
    private final ReorderSelectGroupsComponent reorderSelectComponent;
    /*
     *The wrapper of dataset counter label to show the number of current datasets represented in the heat-map
     */
    private final ResizableTextLabel datasetCounterLabel;

    /*
     *The disease comparison heat-map right side control buttons container
     */
    private final VerticalLayout heatmapToolsContainer;

    /*
     *The set of heatmap headers cell information objects required for initialize the row headers
     */
    private final LinkedHashSet<HeatMapHeaderCellInformationBean> rowheadersSet;
    /*
     *The set of heatmap headers cell information objects required for initialize the column headers
     */
    private final LinkedHashSet<HeatMapHeaderCellInformationBean> colheadersSet;

    /*
     *The set of  disease group comparison that has all comparison information
     */
    private final Set<DiseaseGroupComparison> patientsGroupComparisonsSet;
    /*
     *The map of  full quant dataset map no filters applied
     */
    private final Map<Integer, QuantDataset> fullQuantDsMap;
    /*
     *The map of  quant datasets after different filters applied
     */
    private final Map<Integer, QuantDataset> filteredQuantDsMap;

    /*
     *The quantative data handler to work as controller layer to interact between visulization and logic layer 
     */
    private final Data_Handler Data_handler;

    /*
     *The central manager for handling data across different visualizations and managing all users selections
     */
    private final CSFPR_Central_Manager CSFPR_Central_Manager;

    /**
     * Constructor to initialize the main attributes (Data handler, and
     * selection manage ..etc)
     *
     * @param CSFPR_Central_Manager
     * @param Data_Handler
     * @param diseaseCategorySet
     * @param mainbodyLayoutWidth main body layout width (the container)
     * @param mainbodyLayoutHeight main body layout height (the container)
     * @param activeColumnHeaders boolean array of active columns for dataset
     * table export
     */
    public DiseaseComparisonHeatmapComponent(final CSFPR_Central_Manager CSFPR_Central_Manager, Data_Handler Data_Handler, Collection<DiseaseCategoryObject> diseaseCategorySet, int mainbodyLayoutWidth, int mainbodyLayoutHeight, boolean[] activeColumnHeaders) {

        this.CSFPR_Central_Manager = CSFPR_Central_Manager;
        this.Data_handler = Data_Handler;

        this.setWidth(mainbodyLayoutWidth, Unit.PIXELS);
        this.setHeight(mainbodyLayoutHeight, Unit.PIXELS);

        //init data structure
        this.rowheadersSet = new LinkedHashSet<>();
        this.colheadersSet = new LinkedHashSet<>();
        this.filteredQuantDsMap = new LinkedHashMap<>();
        this.fullQuantDsMap = new LinkedHashMap<>();
        this.patientsGroupComparisonsSet = new LinkedHashSet<>();

        VerticalLayout bodyLayoutWrapper = new VerticalLayout();
        bodyLayoutWrapper.setWidth(100, Unit.PERCENTAGE);
        bodyLayoutWrapper.setHeight(100, Unit.PERCENTAGE);

        this.addComponent(bodyLayoutWrapper);
        this.setComponentAlignment(bodyLayoutWrapper, Alignment.TOP_LEFT);

        GridLayout btnsWrapper = new GridLayout(3, 3);
        btnsWrapper.setColumnExpandRatio(0, 25);
        btnsWrapper.setColumnExpandRatio(1, 50);
        btnsWrapper.setColumnExpandRatio(2, 25);
        btnsWrapper.setSpacing(false);

        datasetPieChartFiltersComponent = new DatasetPieChartFiltersComponent() {
            @Override
            public void updateSystem(Set<Integer> selectedDatasetIds) {
                updateSystemComponents(selectedDatasetIds);

            }

            @Override
            public Map<Integer, QuantDataset> updatedDatasets() {
                if (CSFPR_Central_Manager.getQuantSearchSelection() != null) {
                    Map<Integer, QuantDataset> tempFilterMap = new LinkedHashMap<>();
                    for (int id : filteredQuantDsMap.keySet()) {
                        if (heatmapLayoutContainer.getCurrentDsIds().contains(id)) {
                            tempFilterMap.put(id, filteredQuantDsMap.get(id));
                        }
                    }
                    return tempFilterMap;

                }

                return filteredQuantDsMap;
            }

        };
        btnsWrapper.addComponent(datasetPieChartFiltersComponent, 0, 1);
        btnsWrapper.setComponentAlignment(datasetPieChartFiltersComponent, Alignment.MIDDLE_LEFT);

        reconbineDiseaseGroupsFiltersComponent = new RecombineDiseaseGroupsCombonent(diseaseCategorySet, false) {

            @Override
            public void updateSystem(Map<String, Map<String, String>> updatedGroupsNamesMap) {
                updateCombinedGroups(updatedGroupsNamesMap);
                reorderSelectComponent.updateData(rowheadersSet, colheadersSet, patientsGroupComparisonsSet);
            }

        };
        HorizontalLayout bottomBtnContainer = new HorizontalLayout();
        bottomBtnContainer.setSpacing(false);

        bottomBtnContainer.addComponent(reconbineDiseaseGroupsFiltersComponent);
        bottomBtnContainer.setComponentAlignment(reconbineDiseaseGroupsFiltersComponent, Alignment.TOP_LEFT);

        reorderSelectComponent = new ReorderSelectGroupsComponent(false) {

            @Override
            public void updateSystem(LinkedHashSet<HeatMapHeaderCellInformationBean> rowHeaders, LinkedHashSet<HeatMapHeaderCellInformationBean> colHeaders) {
                datasetPieChartFiltersComponent.resetFilters();
                heatmapLayoutContainer.updateData(rowHeaders, colHeaders, patientsGroupComparisonsSet, fullQuantDsMap);
                
            }

        };
        bottomBtnContainer.addComponent(reorderSelectComponent);
        bottomBtnContainer.setComponentAlignment(reorderSelectComponent, Alignment.TOP_LEFT);

        btnsWrapper.addComponent(bottomBtnContainer, 1, 2);
        btnsWrapper.setComponentAlignment(bottomBtnContainer, Alignment.TOP_CENTER);

        serumCsfFilter = new SerumCsfFilter() {

            @Override
            public void updateSystem(boolean serumApplied, boolean csfApplied) {
                Map<Integer, QuantDataset> updatedDsIds = new LinkedHashMap<>();
                for (int id : fullQuantDsMap.keySet()) {
                    if (serumApplied && fullQuantDsMap.get(id).getSampleType().equalsIgnoreCase("Serum")) {
                        updatedDsIds.put(id, fullQuantDsMap.get(id));
                    } else if (csfApplied && fullQuantDsMap.get(id).getSampleType().equalsIgnoreCase("CSf")) {
                        updatedDsIds.put(id, fullQuantDsMap.get(id));
                    }
                }

                updateSystemComponents(datasetPieChartFiltersComponent.checkAndFilter(updatedDsIds));
                reorderSelectComponent.updateData(rowheadersSet, colheadersSet, patientsGroupComparisonsSet);
            }
        };

        btnsWrapper.addComponent(serumCsfFilter, 1, 0);
        btnsWrapper.setComponentAlignment(serumCsfFilter, Alignment.BOTTOM_CENTER);
        VerticalLayout clearFilterBtn = new VerticalLayout();
        clearFilterBtn.setDescription("Clear filters");
        clearFilterBtn.setStyleName("filterbtn");
        Image icon = new Image();
        icon.setSource(new ThemeResource("img/filter_clear.png"));
        clearFilterBtn.addComponent(icon);
        icon.setWidth(100, Unit.PERCENTAGE);
        icon.setHeight(100, Unit.PERCENTAGE);
        clearFilterBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                filteredQuantDsMap.clear();
                filteredQuantDsMap.putAll(fullQuantDsMap);
                heatmapLayoutContainer.updateData(rowheadersSet, colheadersSet, patientsGroupComparisonsSet, fullQuantDsMap);
                serumCsfFilter.resetFilter();
            }
        });
        btnsWrapper.addComponent(clearFilterBtn, 2, 1);
        btnsWrapper.setComponentAlignment(clearFilterBtn, Alignment.MIDDLE_RIGHT);

        datasetCounterLabel = new ResizableTextLabel();
        datasetCounterLabel.setDescription("#Datasets");

        btnsWrapper.addComponent(datasetCounterLabel, 1, 1);
        btnsWrapper.setComponentAlignment(datasetCounterLabel, Alignment.MIDDLE_CENTER);
        datasetCounterLabel.setStyleName("filterbtn");
        datasetCounterLabel.addStyleName("defaultcursor");

        HeatmapFiltersContainerResizeControl filterSizeController = new HeatmapFiltersContainerResizeControl(btnsWrapper, datasetPieChartFiltersComponent, reconbineDiseaseGroupsFiltersComponent, reorderSelectComponent, bottomBtnContainer, serumCsfFilter, clearFilterBtn, datasetCounterLabel);

        int availableHMHeight = mainbodyLayoutHeight;

        heatmapLayoutContainer = new HeatMapLayout(mainbodyLayoutWidth, availableHMHeight, activeColumnHeaders, filterSizeController, CSFPR_Central_Manager.getFullPublicationList()) {
            @Override
            public void updateSelectionManager(Set<QuantDiseaseGroupsComparison> selectedQuantComparisonsList) {

                if (selectedQuantComparisonsList != null && !selectedQuantComparisonsList.isEmpty()) {
                    while (true) {
                        Data_Handler.updateComparisonQuantProteins(selectedQuantComparisonsList);
                        boolean valid = true;
                        for (QuantDiseaseGroupsComparison comparsion : selectedQuantComparisonsList) {
                            if (comparsion.getQuantComparisonProteinMap() == null) {
                                valid = false;
                                break;
                            }

                        }
                        if (valid) {
                            break;
                        }
                    }

                }

                DiseaseComparisonHeatmapComponent.this.selfselection = true;
                CSFSelection selection = new CSFSelection("comparisons_selection", getListenerId(), selectedQuantComparisonsList, null);
                CSFPR_Central_Manager.setSelection(selection);
            }

            @Override
            public void updateHMThumb(String imgUrl, int datasetNumber, int deactivated, Map<QuantDiseaseGroupsComparison, QuantDiseaseGroupsComparison> equalComparisonMap) {
                datasetCounterLabel.setValue(datasetNumber + "/" + fullQuantDsMap.size());
                if (deactivated > 0) {
                    datasetCounterLabel.setDescription("#Datasets<br/>#Not active datasets: " + deactivated);
                } else {
                    datasetCounterLabel.setDescription("#Datasets");
                }
                DiseaseComparisonHeatmapComponent.this.updateIcon(imgUrl);

                CSFPR_Central_Manager.setEqualComparisonMap(equalComparisonMap);
            }

        };
        bodyLayoutWrapper.addComponent(heatmapLayoutContainer);
        bodyLayoutWrapper.setComponentAlignment(heatmapLayoutContainer, Alignment.MIDDLE_CENTER);
        this.heatmapToolsContainer = heatmapLayoutContainer.getHeatmapToolsContainer();

        CSFPR_Central_Manager.registerListener(DiseaseComparisonHeatmapComponent.this);

    }

    /**
     * Update the heat map layout
     *
     * @param rowheaders
     * @param colheaders
     * @param patientsGroupComparisonsSet
     * @param fullQuantDsMap
     */
    public void updateData(LinkedHashSet<HeatMapHeaderCellInformationBean> rowheaders, LinkedHashSet<HeatMapHeaderCellInformationBean> colheaders, Set<DiseaseGroupComparison> patientsGroupComparisonsSet, Map<Integer, QuantDataset> fullQuantDsMap) {

        int waiting = fullQuantDsMap.size() * 10;
        try {
            Thread.sleep(waiting);
        } catch (InterruptedException e) {
        }
        this.fullQuantDsMap.clear();
        this.fullQuantDsMap.putAll(fullQuantDsMap);
//        heatmapLayoutContainer.updateData(rowheadersSet, colheadersSet, patientsGroupComparisonsSet, fullQuantDsMap);

//        for (HeatMapHeaderCellInformationBean rowheader : rowheadersSet) {
//           
//        }
        this.rowheadersSet.clear();
        this.rowheadersSet.addAll(rowheaders);
        this.colheadersSet.clear();
        this.colheadersSet.addAll(colheaders);
        this.patientsGroupComparisonsSet.clear();
        this.patientsGroupComparisonsSet.addAll(patientsGroupComparisonsSet);

        this.filteredQuantDsMap.clear();
        this.filteredQuantDsMap.putAll(fullQuantDsMap);

        serumCsfFilter.resetFilter();
        reorderSelectComponent.updateData(rowheaders, colheaders, patientsGroupComparisonsSet);

    }

    /**
     * Update the CSF-PR system based on user selection
     *
     * @param datasetIds selected dataset indexes
     */
    private void updateSystemComponents(Set<Integer> datasetIds) {

        filteredQuantDsMap.clear();
        fullQuantDsMap.keySet().stream().filter((datasetId) -> (datasetIds.contains(datasetId))).forEach((datasetId) -> {
            filteredQuantDsMap.put(datasetId, fullQuantDsMap.get(datasetId));
        });
        Set<HeatMapHeaderCellInformationBean> localRows = new LinkedHashSet<>(), localColumns = new LinkedHashSet<>();
        Set<DiseaseGroupComparison> filteredPatientsGroupComparisonsSet = new LinkedHashSet<>();

        for (HeatMapHeaderCellInformationBean rowHeader : rowheadersSet) {
            for (int key : filteredQuantDsMap.keySet()) {
                QuantDataset dataset = filteredQuantDsMap.get(key);
                if ((dataset.getActiveDiseaseSubGroupI().equalsIgnoreCase(rowHeader.getDiseaseGroupName()) || dataset.getActiveDiseaseSubGroupII().equalsIgnoreCase(rowHeader.getDiseaseGroupName())) && (rowHeader.getDiseaseCategory().equalsIgnoreCase(dataset.getDiseaseCategory()))) {
                    localRows.add(rowHeader);

                }
            }

        }

        colheadersSet.stream().forEach((rowHeader) -> {
            for (Iterator<Integer> it = filteredQuantDsMap.keySet().iterator(); it.hasNext();) {
                int key = it.next();
                QuantDataset dataset = filteredQuantDsMap.get(key);
                if (((dataset.getActiveDiseaseSubGroupI().equalsIgnoreCase(rowHeader.getDiseaseGroupName()) || dataset.getActiveDiseaseSubGroupII().equalsIgnoreCase(rowHeader.getDiseaseGroupName()))) && (rowHeader.getDiseaseCategory().equalsIgnoreCase(dataset.getDiseaseCategory()))) {
                    localColumns.add(rowHeader);

                }
            }
        });

        for (int key : filteredQuantDsMap.keySet()) {
            patientsGroupComparisonsSet.stream().filter((patientsGroupComparisons) -> (patientsGroupComparisons.getQuantDatasetIndex() == key)).forEach((patientsGroupComparisons) -> {
                filteredPatientsGroupComparisonsSet.add(patientsGroupComparisons);
            });
        }
        heatmapLayoutContainer.updateData(localRows, localColumns, filteredPatientsGroupComparisonsSet, filteredQuantDsMap);

    }

    private boolean selfselection = false;

    /**
     * Selection changed in the selection manager
     *
     * @param type type of selection
     */
    @Override
    public void selectionChanged(String type) {
        if (type.equalsIgnoreCase("comparisons_selection_update")) {
            Set<QuantDiseaseGroupsComparison> selectedQuantComparisonsList = CSFPR_Central_Manager.getSelectedComparisonsList();
            Data_handler.updateComparisonQuantProteins(selectedQuantComparisonsList);
            selfselection = true;
            CSFSelection selection = new CSFSelection("comparisons_selection", getListenerId(), selectedQuantComparisonsList, null);
            CSFPR_Central_Manager.setSelection(selection);

        }

        if (type.equalsIgnoreCase("comparisons_selection")) {
            if (selfselection) {
                selfselection = false;
                return;

            }

            Set<QuantDiseaseGroupsComparison> compList = CSFPR_Central_Manager.getSelectedComparisonsList();
            if (compList == null || compList.isEmpty()) {
                heatmapLayoutContainer.clearSelection();

            } else {
                heatmapLayoutContainer.selectComparisons(compList);
            }
        }

    }

    /**
     * Show serum datasets (unselected by default)
     */
    public void showSerumDs() {
        serumCsfFilter.getNoSerumOptionBtn().removeStyleName("unapplied");
        serumCsfFilter.updateSystem(true, true);

    }

    /**
     * Get the listener id
     *
     * @return listener id
     */
    @Override
    public String getListenerId() {
        return this.getClass().getName();
    }


    /**
     *Un-select all datasets in the heat-map
     */
    public void unselectAll() {
        this.heatmapLayoutContainer.unselectAll();
    }

     /**
     *Select all datasets in the heat-map
     */
    public void selectAll() {
        this.heatmapLayoutContainer.selectAll();
    }

    /**
     * This method to update and combine disease sub groups based on user
     * selection
     *
     *
     * @param updatedGroupsNamesMap updated disease sub group names
     */
    public abstract void updateCombinedGroups(Map<String, Map<String, String>> updatedGroupsNamesMap);

    /**
     * This method allow users to filter the datasets based on sample type (CSF
     * / Serum)
     *
     *
     * @param serumApplied show Serum datasets
     * @param csfApplied show CSF datasets
     */
    public abstract void updateCSFSerumDatasets(boolean serumApplied, boolean csfApplied);

    /**
     * Update the main button image with the current heat map image
     *
     * @param imageUrl
     */
    public abstract void updateIcon(String imageUrl);

    /**
     * Blinking the main button
     */
    public abstract void blinkIcon();

    /**
     * Get side buttons container that has all the heat map control buttons
     *
     * @return heatmapToolsContainer
     */
    public VerticalLayout getHeatmapToolsContainer() {
        return heatmapToolsContainer;
    }

}
