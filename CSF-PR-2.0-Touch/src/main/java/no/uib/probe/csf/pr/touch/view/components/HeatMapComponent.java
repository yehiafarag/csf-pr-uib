package no.uib.probe.csf.pr.touch.view.components;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.Data_Handler;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseCategoryObject;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseGroupComparison;
import no.uib.probe.csf.pr.touch.logic.beans.HeatMapHeaderCellInformationBean;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFListener;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFPR_Central_Manager;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFSelection;
import no.uib.probe.csf.pr.touch.view.components.datasetfilters.DatasetPieChartFiltersComponent;
import no.uib.probe.csf.pr.touch.view.components.datasetfilters.RecombineDiseaseGroupsCombonent;
import no.uib.probe.csf.pr.touch.view.components.datasetfilters.ReorderSelectGroupsFilter;
import no.uib.probe.csf.pr.touch.view.components.datasetfilters.SerumCsfFilter;
import no.uib.probe.csf.pr.touch.view.components.heatmapsubcomponents.HeatMapLayout;
import no.uib.probe.csf.pr.touch.view.core.InfoPopupBtn;

/**
 *
 * @author Yehia Farag
 */
public abstract class HeatMapComponent extends VerticalLayout implements CSFListener {

    private final HeatMapLayout heatmapLayoutContainer;
    private final DatasetPieChartFiltersComponent datasetPieChartFiltersBtn;
    private final RecombineDiseaseGroupsCombonent reconbineDiseaseGroupsFiltersBtn;

    private final LinkedHashSet<HeatMapHeaderCellInformationBean> rowheaders, colheaders;
    private final Set<DiseaseGroupComparison> patientsGroupComparisonsSet;
    private final Map<Integer, QuantDatasetObject> fullQuantDsMap, filteredQuantDsMap;

    private final Label datasetCounterLabel;
    private final SerumCsfFilter serumCsfFilter;
    private final ReorderSelectGroupsFilter reorderSelectBtn;
    private final CSFPR_Central_Manager CSFPR_Central_Manager;
    private final VerticalLayout heatmapToolBtnContainer;

    private final Data_Handler Data_Handler;

    /**
     *
     * @param CSFPR_Central_Manager
     * @param Data_Handler
     * @param diseaseCategorySet
     * @param mainbodyLayoutWidth mainbody layout width (the container)
     * @param mainbodyLayoutHeight mainbody layout height (the container)
     * @param activeColumnHeaders boolean array of active columns for dataset
     * table exportu
     */
    public HeatMapComponent(final CSFPR_Central_Manager CSFPR_Central_Manager, Data_Handler Data_Handler, Collection<DiseaseCategoryObject> diseaseCategorySet, int mainbodyLayoutWidth, int mainbodyLayoutHeight, boolean[] activeColumnHeaders) {

        this.CSFPR_Central_Manager = CSFPR_Central_Manager;
        this.Data_Handler = Data_Handler;

        this.setWidth(mainbodyLayoutWidth, Unit.PIXELS);
        this.setHeight(mainbodyLayoutHeight, Unit.PIXELS);

        //init data structure
        this.rowheaders = new LinkedHashSet<>();
        this.colheaders = new LinkedHashSet<>();
        this.filteredQuantDsMap = new LinkedHashMap<>();
        this.fullQuantDsMap = new LinkedHashMap<>();
        this.patientsGroupComparisonsSet = new LinkedHashSet<>();

        VerticalLayout bodyLayoutWrapper = new VerticalLayout();
        bodyLayoutWrapper.setWidth(100, Unit.PERCENTAGE);
        bodyLayoutWrapper.setHeightUndefined();

        this.addComponent(bodyLayoutWrapper);
        this.setComponentAlignment(bodyLayoutWrapper, Alignment.MIDDLE_CENTER);

        GridLayout btnsWrapper = new GridLayout(3, 3);
        btnsWrapper.setColumnExpandRatio(0, 25);
        btnsWrapper.setColumnExpandRatio(1, 50);
        btnsWrapper.setColumnExpandRatio(2, 25);
        btnsWrapper.setSpacing(false);

        datasetPieChartFiltersBtn = new DatasetPieChartFiltersComponent() {
            @Override
            public void updateSystem(Set<Integer> selectedDatasetIds) {
                updateSystemComponents(selectedDatasetIds);
            }

            @Override
            public Map<Integer, QuantDatasetObject> updatedDatasets() {
                return filteredQuantDsMap;
            }

        };
        btnsWrapper.addComponent(datasetPieChartFiltersBtn, 0, 1);
        btnsWrapper.setComponentAlignment(datasetPieChartFiltersBtn, Alignment.MIDDLE_LEFT);

        reconbineDiseaseGroupsFiltersBtn = new RecombineDiseaseGroupsCombonent(diseaseCategorySet) {

            @Override
            public void updateSystem(Map<String, Map<String, String>> updatedGroupsNamesMap) {
                updateCombinedGroups(updatedGroupsNamesMap);
            }

        };
        HorizontalLayout bottomBtnContainer = new HorizontalLayout();
        bottomBtnContainer.setSpacing(false);
        bottomBtnContainer.setWidth(53, Unit.PIXELS);
        bottomBtnContainer.addComponent(reconbineDiseaseGroupsFiltersBtn);
        bottomBtnContainer.setComponentAlignment(reconbineDiseaseGroupsFiltersBtn, Alignment.TOP_LEFT);

        reorderSelectBtn = new ReorderSelectGroupsFilter() {

            @Override
            public void updateSystem(LinkedHashSet<HeatMapHeaderCellInformationBean> rowHeaders, LinkedHashSet<HeatMapHeaderCellInformationBean> colHeaders) {
                heatmapLayoutContainer.updateData(rowHeaders, colHeaders, patientsGroupComparisonsSet, fullQuantDsMap);
            }

        };
        bottomBtnContainer.addComponent(reorderSelectBtn);
        bottomBtnContainer.setComponentAlignment(reorderSelectBtn, Alignment.TOP_LEFT);

        btnsWrapper.addComponent(bottomBtnContainer, 1, 2);
        btnsWrapper.setComponentAlignment(bottomBtnContainer, Alignment.TOP_CENTER);

        serumCsfFilter = new SerumCsfFilter() {

            @Override
            public void updateSystem(boolean serumApplied, boolean csfApplied) {
                Map<Integer, QuantDatasetObject> updatedDsIds = new LinkedHashMap<>();
                for (int id : fullQuantDsMap.keySet()) {
                    if (serumApplied && fullQuantDsMap.get(id).getSampleType().equalsIgnoreCase("Serum")) {
                        updatedDsIds.put(id, fullQuantDsMap.get(id));
                    } else if (csfApplied && fullQuantDsMap.get(id).getSampleType().equalsIgnoreCase("CSf")) {
                        updatedDsIds.put(id, fullQuantDsMap.get(id));
                    }
                }

                updateSystemComponents(datasetPieChartFiltersBtn.checkAndFilter(updatedDsIds));
            }
        };
        btnsWrapper.addComponent(serumCsfFilter, 1, 0);
        btnsWrapper.setComponentAlignment(serumCsfFilter, Alignment.BOTTOM_CENTER);
        VerticalLayout clearFilterBtn = new VerticalLayout();
        clearFilterBtn.setDescription("Clear filters");
        clearFilterBtn.setWidth(25, Unit.PIXELS);
        clearFilterBtn.setHeight(25, Unit.PIXELS);
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
                heatmapLayoutContainer.updateData(rowheaders, colheaders, patientsGroupComparisonsSet, fullQuantDsMap);
                serumCsfFilter.resetFilter();
            }
        });
        btnsWrapper.addComponent(clearFilterBtn, 2, 1);
        btnsWrapper.setComponentAlignment(clearFilterBtn, Alignment.MIDDLE_RIGHT);
        datasetCounterLabel = new Label();
        datasetCounterLabel.setDescription("#Datasets");
        datasetCounterLabel.setHeight(25, Unit.PIXELS);
        btnsWrapper.addComponent(datasetCounterLabel, 1, 1);
        datasetCounterLabel.setContentMode(ContentMode.HTML);
        btnsWrapper.setComponentAlignment(datasetCounterLabel, Alignment.MIDDLE_CENTER);
        datasetCounterLabel.setStyleName("filterbtn");
        datasetCounterLabel.addStyleName("defaultcursor");
        InfoPopupBtn info = new InfoPopupBtn("infoText");
        info.setWidth(25, Unit.PIXELS);
        info.setHeight(25, Unit.PIXELS);

        //init heatmap
        int availableHMHeight = mainbodyLayoutHeight - 85;
        heatmapLayoutContainer = new HeatMapLayout(mainbodyLayoutWidth, availableHMHeight, activeColumnHeaders, btnsWrapper) {
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

                HeatMapComponent.this.selfselection = true;
                CSFSelection selection = new CSFSelection("comparisons_selection", getFilterId(), selectedQuantComparisonsList, null);
                CSFPR_Central_Manager.selectionAction(selection);
            }

            @Override
            public void updateHMThumb(String imgUrl, int datasetNumber, int deactivated, Map<QuantDiseaseGroupsComparison, QuantDiseaseGroupsComparison> equalComparisonMap) {
                datasetCounterLabel.setValue("<center>" + datasetNumber + "/" + fullQuantDsMap.size() + "</center>");
                if (deactivated > 0) {
                    datasetCounterLabel.setDescription("#Datasets<br/>#Not active datasets: " + deactivated);
                } else {
                    datasetCounterLabel.setDescription("#Datasets");
                }
                HeatMapComponent.this.updateIcon(imgUrl);

                CSFPR_Central_Manager.setEqualComparisonMap(equalComparisonMap);
            }

        };
        bodyLayoutWrapper.addComponent(heatmapLayoutContainer);
        bodyLayoutWrapper.setComponentAlignment(heatmapLayoutContainer, Alignment.TOP_CENTER);
        this.heatmapToolBtnContainer = heatmapLayoutContainer.getControlBtnsContainer();

        CSFPR_Central_Manager.registerListener(HeatMapComponent.this);

    }

    /**
     *
     * @param rowheaders
     * @param colheaders
     * @param patientsGroupComparisonsSet
     * @param fullQuantDsMap
     */
    public void updateData(LinkedHashSet<HeatMapHeaderCellInformationBean> rowheaders, LinkedHashSet<HeatMapHeaderCellInformationBean> colheaders, Set<DiseaseGroupComparison> patientsGroupComparisonsSet, Map<Integer, QuantDatasetObject> fullQuantDsMap) {

        int waiting = fullQuantDsMap.size() * 10;
        try {
            Thread.sleep(waiting);
        } catch (InterruptedException e) {
        }
        this.fullQuantDsMap.clear();
        this.fullQuantDsMap.putAll(fullQuantDsMap);
        heatmapLayoutContainer.updateData(rowheaders, colheaders, patientsGroupComparisonsSet, fullQuantDsMap);

        this.rowheaders.clear();
        this.rowheaders.addAll(rowheaders);
        this.colheaders.clear();
        this.colheaders.addAll(colheaders);
        this.patientsGroupComparisonsSet.clear();
        this.patientsGroupComparisonsSet.addAll(patientsGroupComparisonsSet);

        this.filteredQuantDsMap.clear();
        this.filteredQuantDsMap.putAll(fullQuantDsMap);

        serumCsfFilter.resetFilter();

        reorderSelectBtn.updateData(rowheaders, colheaders, patientsGroupComparisonsSet);

    }

    private void updateSystemComponents(Set<Integer> datasetIds) {

        filteredQuantDsMap.clear();
        fullQuantDsMap.keySet().stream().filter((datasetId) -> (datasetIds.contains(datasetId))).forEach((datasetId) -> {
            filteredQuantDsMap.put(datasetId, fullQuantDsMap.get(datasetId));
        });
        heatmapLayoutContainer.filterHeatMap(filteredQuantDsMap);

    }

    private boolean selfselection = false;

    /**
     *
     * @param type
     */
    @Override
    public void selectionChanged(String type) {
        if (type.equalsIgnoreCase("comparisons_selection_update")) {
            Set<QuantDiseaseGroupsComparison> selectedQuantComparisonsList = CSFPR_Central_Manager.getSelectedComparisonsList();
            Data_Handler.updateComparisonQuantProteins(selectedQuantComparisonsList);
            selfselection = true;
            CSFSelection selection = new CSFSelection("comparisons_selection", getFilterId(), selectedQuantComparisonsList, null);
            CSFPR_Central_Manager.selectionAction(selection);

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

        if (type.equalsIgnoreCase("quant_searching")) {
//            heatmapLayoutContainer.selectComparisonsByID(CSFPR_Central_Manager.getQuantSearchSelection().getDatasetIds());

        }

    }

    /**
     * Reset selection on dataset layout no selection manager update
     */
    public void selectComparisonsByID(Set<Integer> comparisonsToSelect) {
        heatmapLayoutContainer.selectComparisonsByID(CSFPR_Central_Manager.getQuantSearchSelection().getDatasetIds());
    }

    public void showSerumDs() {
        serumCsfFilter.getNoSerumOptionBtn().removeStyleName("unapplied");
        serumCsfFilter.updateSystem(true, true);

    }

    /**
     *
     * @return
     */
    @Override
    public String getFilterId() {
        return this.getClass().getName();
    }

    /**
     *
     * @param value
     */
    @Override
    public void removeFilterValue(String value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * this method to update and combine disease sub groups based on user
     * selection
     *
     *
     * @param updatedGroupsNamesMap updated disease sub group names
     */
    public abstract void updateCombinedGroups(Map<String, Map<String, String>> updatedGroupsNamesMap);

    /**
     * this method allow users to filter the datasets based on sample type (CSF
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
     * get side buttons container that has all the control buttons
     *
     * @return
     */
    public VerticalLayout getHeatmapToolBtnContainer() {
        return heatmapToolBtnContainer;
    }

}
