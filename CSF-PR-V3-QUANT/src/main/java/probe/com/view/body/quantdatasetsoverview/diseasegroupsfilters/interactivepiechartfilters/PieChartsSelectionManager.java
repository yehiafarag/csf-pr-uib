/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.interactivepiechartfilters;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import probe.com.model.beans.quant.QuantDatasetObject;
import probe.com.selectionmanager.CSFFilter;
import probe.com.selectionmanager.CSFFilterSelection;
import probe.com.selectionmanager.QuantCentralManager;

/**
 *
 * @author Yehia Farag
 */
public class PieChartsSelectionManager implements Serializable, CSFFilter {

    private boolean externalSelectionChanged;

    /**
     *
     * @param type
     */
    @Override
    public void selectionChanged(String type) {

        if (type.equalsIgnoreCase("Reorder_Selection")) {
//            if (selfselection) {
//                selfselection = false;
//                return;
//            }
            externalSelectionChanged = true;
            selectedDsIds.clear();
            for (QuantDatasetObject qDs : Quant_Central_Manager.getFilteredQuantDatasetArr().values()) {
                if (qDs == null) {
                    continue;
                }
                selectedDsIds.add(qDs.getDsKey());
            }

        } else if (type.equalsIgnoreCase("Reset_Disease_Groups_Level")) {
            if (selfselection) {
                selfselection = false;
                return;
            }
            selectedDsIds.clear();
            resetToInitState();
            unselectAll();

        }

    }
    private boolean selfselection;

    /**
     *
     * @param selfselection
     */
    public void applyFilterSelectionToCentralManager(boolean selfselection) {
        this.selfselection = selfselection;
        if (toResetCentral) {
            toResetCentral = false;
            selectedDsIds.clear();
            resetToInitState();
            unselectAll();
            return;

        }
        Set<Integer>finalFilterValue = new LinkedHashSet<Integer>();
         for (JfreeDivaPieChartFilter ifilter : registeredFilters) {
            finalFilterValue.addAll(ifilter.getFinalFilterValue());
            break;
            
        }

        int[] dsIds = new int[finalFilterValue.size()];
        for (int i = 0; i < finalFilterValue.size(); i++) {
            dsIds[i] = (Integer) finalFilterValue.toArray()[i];
        }
       
        Quant_Central_Manager.applyFilters(new CSFFilterSelection("Pie_Chart_Selection", dsIds, Filter_ID, null));

    }

    private final Set<JfreeDivaPieChartFilter> registeredFilters = new HashSet<JfreeDivaPieChartFilter>();
    private final String Filter_ID = "interactiveChartsFilter";

    private final Map<String, String> titleMap = new HashMap<String, String>();
    private final QuantCentralManager Quant_Central_Manager;

    /**
     *
     * @param Quant_Central_Manager
     */
    public PieChartsSelectionManager(QuantCentralManager Quant_Central_Manager) {
        Quant_Central_Manager.registerFilterListener(PieChartsSelectionManager.this);
        this.Quant_Central_Manager = Quant_Central_Manager;
        titleMap.put("diseaeGroups", "Disease Groups");
        titleMap.put("rawDataUrl", "Raw Data");
        titleMap.put("year", "Year");
        titleMap.put("typeOfStudy", "Study Type");
        titleMap.put("sampleType", "Sample Type");
        titleMap.put("sampleMatching", "Sample Matching");
        titleMap.put("technology", "Technology");
        titleMap.put("analyticalApproach", "Analytical Approach");
        titleMap.put("enzyme", "Enzyme");
        titleMap.put("shotgunTargeted", "Shotgun/Targeted");
        titleMap.put("quantificationBasis", "Quantification Basis");
    }

    /**
     *
     * @param filter
     */
    public void registerLocalPieChartFilter(JfreeDivaPieChartFilter filter) {
        this.registeredFilters.add(filter);
    }

    /**
     *
     * @return
     */
    @Override
    public String getFilterId() {
        return Filter_ID;
    }

    /**
     *
     * @param value
     */
    @Override
    public void removeFilterValue(String value) {
    }

    /**
     *
     * @param filterId
     * @return
     */
    public String getFilterTitle(String filterId) {
        if (titleMap.containsKey(filterId)) {
            return titleMap.get(filterId);
        } else {
            return filterId;
        }

    }

    private final Set<Integer> selectedDsIds = new LinkedHashSet<Integer>();
    private final Set<Integer> unselectedDsIds = new LinkedHashSet<Integer>();

    /**
     *
     * @param filterId
     */
    public void jfreeUpdateChartsFilters(String filterId) {
        toResetCentral = false;
        this.updateDsIndexSelection();
        this.updatePieChartCharts(filterId);

    }

    private void updatePieChartCharts(String filterId) {

        for (JfreeDivaPieChartFilter ifilter : registeredFilters) {
            if (ifilter.getFilter_Id().equalsIgnoreCase(filterId)) {
                continue;
            }
            ifilter.selectionChanged(selectedDsIds);
        }
    }

    /**
     *
     */
    public void resetPieChartCharts() {
//        selectedDsIds.clear();
//            for (QuantDatasetObject qDs : Quant_Central_Manager.getFilteredQuantDatasetArr().values()) {
//                if (qDs == null) {
//                    continue;
//                }
//                selectedDsIds.add(qDs.getDsKey());
//            }
//            System.out.println("at externalSelectionChanged "+externalSelectionChanged);

        if (externalSelectionChanged) {

            externalSelectionChanged = false;
            for (JfreeDivaPieChartFilter ifilter : registeredFilters) {
                ifilter.resetFilterWithUpdatedFilters(selectedDsIds, true);
            }

        }

    }
    private boolean toResetCentral;

    /**
     *
     */
    public void resetToInitState() {
        selectedDsIds.clear();
        toResetCentral = true;
        for (JfreeDivaPieChartFilter ifilter : registeredFilters) {
            ifilter.resetFilterToClearState();
        }

    }

    /**
     *
     */
    public void unselectAll() {
        for (JfreeDivaPieChartFilter ifilter : registeredFilters) {
            ifilter.unselectFilter();
        }

        Set<Integer> tempSelectedDsIds = new LinkedHashSet<Integer>();
        for (QuantDatasetObject qDs : Quant_Central_Manager.getFilteredQuantDatasetArr().values()) {
            tempSelectedDsIds.add(qDs.getDsKey());
        }
        tempSelectedDsIds.removeAll(selectedDsIds);
        unselectedDsIds.addAll(selectedDsIds);
        if (tempSelectedDsIds.isEmpty()) {
            selectedDsIds.clear();
        } else {
            selectedDsIds.addAll(tempSelectedDsIds);
        }

        updatePieChartCharts("");
    }

    /**
     *
     */
    public void updateDsIndexSelection() {
        unselectedDsIds.addAll(selectedDsIds);
        selectedDsIds.clear();

        Set<JfreeDivaPieChartFilter> activeFilterSet = new HashSet<JfreeDivaPieChartFilter>();
        Set<Integer> activeSelectedDsIds = new LinkedHashSet<Integer>();
        for (JfreeDivaPieChartFilter ifilter : registeredFilters) {
            if (!ifilter.getSelectedDsIds().isEmpty()) {
                activeFilterSet.add(ifilter);
                activeSelectedDsIds.addAll(ifilter.getSelectedDsIds());
            }
        }
        selectedDsIds.addAll(activeSelectedDsIds);
        unselectedDsIds.removeAll(activeSelectedDsIds);
        for (int dsId : activeSelectedDsIds) {
            boolean check;
            for (JfreeDivaPieChartFilter iFilter : activeFilterSet) {
                check = iFilter.getSelectedDsIds().contains(dsId);
                if (!check) {
                    selectedDsIds.remove(dsId);
                    unselectedDsIds.add(dsId);
                    break;
                }
            }

        }

    }

    /**
     *
     */
    public void resetCentralSelectionManager() {
        selfselection = true;
        Quant_Central_Manager.resetFiltersListener();

    }
}
