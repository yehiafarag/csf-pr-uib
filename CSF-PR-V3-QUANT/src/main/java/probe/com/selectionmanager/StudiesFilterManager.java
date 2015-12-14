/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.selectionmanager;

import com.google.gwt.dom.client.Style;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.jfree.chart.JFreeChart;
import probe.com.model.beans.quant.QuantDatasetInitialInformationObject;
import probe.com.model.beans.quant.QuantDatasetObject;
import probe.com.view.core.DiseaseGroup;

/**
 *
 * @author Yehia Farag
 *
 * This class responsible for quant studies filters interactivity
 */
public class StudiesFilterManager implements Serializable {

    private boolean[] activeHeader;
    private final Map<String, QuantDatasetInitialInformationObject> quantDatasetListObject;
    private int totalDsNumber, currentDsNumber;
    private Map<Integer, QuantDatasetObject> inUsefullQuantDatasetMap;
    private Map<Integer, QuantDatasetObject> noSerumQuantDatasetMap;
    private Map<Integer, QuantDatasetObject> fullQuantDatasetMap;
    private final Map<String, boolean[]> activeFilterMap;
    private boolean[] activeFilters;
    private Map<Integer, QuantDatasetObject> filteredQuantDatasetArr = new LinkedHashMap<Integer, QuantDatasetObject>();
    private Set<String> diseaseCategorySet;
    private final LinkedHashMap<String, CSFFilter> registeredFilterSet = new LinkedHashMap<String, CSFFilter>();

    private CSFFilterSelection filterSelection;
    private Set<String> selectedHeatMapRows;
    private Set<String> selectedHeatMapColumns;
    private DiseaseGroup[] diseaseGroupsArr;

    public boolean isNoSerum() {
        return noSerum;
    }

    public void setNoSerum(boolean noSerum) {
        this.noSerum = noSerum;
        if (noSerum) {
            inUsefullQuantDatasetMap = noSerumQuantDatasetMap;
        } else {
            inUsefullQuantDatasetMap = fullQuantDatasetMap;
        }
         this.currentDsNumber = inUsefullQuantDatasetMap.size();
        resetFilters();
    }
    private boolean noSerum;

    private Set<JFreeChart> studiesOverviewPieChart = new LinkedHashSet<JFreeChart>();

    public StudiesFilterManager(Map<String, QuantDatasetInitialInformationObject> quantDatasetListObject, Map<String, boolean[]> activeFilterMap) {

        this.quantDatasetListObject = quantDatasetListObject;
        String key = "Multiple Sclerosis";//quantDatasetListObject.keySet().iterator().next();

        this.totalDsNumber = 0;
        for (String k : quantDatasetListObject.keySet()) {
            totalDsNumber += quantDatasetListObject.get(k).getQuantDatasetsList().size();

        }

        this.fullQuantDatasetMap = quantDatasetListObject.get(key).getQuantDatasetsList();
        noSerumQuantDatasetMap = new HashMap<Integer, QuantDatasetObject>();
        for (int intKey : fullQuantDatasetMap.keySet()) {
            if (!fullQuantDatasetMap.get(intKey).getSampleType().equalsIgnoreCase("Serum")) {
                noSerumQuantDatasetMap.put(intKey, fullQuantDatasetMap.get(intKey));

            }

        }
        inUsefullQuantDatasetMap = noSerumQuantDatasetMap;
        this.currentDsNumber = inUsefullQuantDatasetMap.size();
        this.activeFilterMap = activeFilterMap;
        this.activeFilters = activeFilterMap.get(key);
        this.activeHeader = quantDatasetListObject.get(key).getActiveHeaders();
        this.diseaseCategorySet = quantDatasetListObject.keySet();
    }

    public boolean[] getActiveHeader() {
        return activeHeader;
    }

    /**
     * update the current filtered dataset indexes
     *
     * @param datasetIndexes
     */
    private void updateFilteredDatasetList(int[] datasetIndexes) {

        if (datasetIndexes.length == 0) {
            filteredQuantDatasetArr = inUsefullQuantDatasetMap;
            return;
        }
        filteredQuantDatasetArr.clear();
        for (int i : datasetIndexes) {
            filteredQuantDatasetArr.put(i, inUsefullQuantDatasetMap.get(i));
        }

    }

    /**
     *
     * @return
     */
    public Map<Integer, QuantDatasetObject> getFilteredQuantDatasetArr() {
        return filteredQuantDatasetArr;
    }

    public Set<String> getDiseaseCategorySet() {
        return diseaseCategorySet;
    }

    /**
     *
     * @param selection
     */
    public void applyFilters(CSFFilterSelection selection) {

        filterSelection = selection;
        if (selection.getType().equalsIgnoreCase("Disease_Groups_Level")) {

            updateFilteredDatasetList(selection.getDatasetIndexes());
            this.SelectionChanged(selection.getType());
        }

    }

    /**
     *
     * @return
     */
    public CSFFilterSelection getFilterSelection() {
        return filterSelection;
    }

    /**
     * reset all disease groups filters
     */
    public void resetFilters() {
        filteredQuantDatasetArr.clear();
        this.SelectionChanged("Reset_Disease_Groups_Level");

    }

    /**
     * get current active quant filters
     *
     * @return
     */
    public boolean[] getActiveFilters() {
        return activeFilters;
    }

    /**
     * update all registered filters
     */
    private void SelectionChanged(String type) {
        for (CSFFilter filter : registeredFilterSet.values()) {
            filter.selectionChanged(type);
        }

    }

    /**
     * get the current filtered quant dataset list
     *
     * @return
     */
    public Map<Integer, QuantDatasetObject> getFilteredDatasetsList() {
        if (filteredQuantDatasetArr == null || filteredQuantDatasetArr.isEmpty()) {
            return inUsefullQuantDatasetMap;
        }
        return filteredQuantDatasetArr;
    }

    /**
     * get all quant dataset list available
     *
     * @return
     */
    public Map<Integer, QuantDatasetObject> getFullQuantDatasetMap() {
        return inUsefullQuantDatasetMap;
    }

    public void changeDiseaseCategory(String diseaseCategory) {
        this.inUsefullQuantDatasetMap = quantDatasetListObject.get(diseaseCategory).getQuantDatasetsList();
        this.filteredQuantDatasetArr.clear();
        this.diseaseCategorySet = quantDatasetListObject.get(diseaseCategory).getDiseaseCategories();
        this.activeHeader = quantDatasetListObject.get(diseaseCategory).getActiveHeaders();
        this.activeFilters = activeFilterMap.get(diseaseCategory);
        this.currentDsNumber = inUsefullQuantDatasetMap.size();
//        this.SelectionChanged("Disease_Category_Selection");

    }

    public int getTotalDsNumber() {
        return totalDsNumber;
    }

    public int getCurrentDsNumber() {
        return currentDsNumber;
    }

    /**
     * register filter to the selection manager
     *
     * @param iFilter instance of CSFFilter
     */
    public void registerFilter(final CSFFilter iFilter) {
        registeredFilterSet.put(iFilter.getFilterId(), iFilter);
    }

    /**
     *
     * @param selectedRows
     * @param selectedColumns
     * @param diseaseGroupsArr
     */
    public void setHeatMapLevelSelection(Set<String> selectedRows, Set<String> selectedColumns, DiseaseGroup[] diseaseGroupsArr) {
        this.diseaseGroupsArr = diseaseGroupsArr;
        this.selectedHeatMapRows = selectedRows;
        this.selectedHeatMapColumns = selectedColumns;
        this.SelectionChanged("HeatMap_Update_level");

    }

    /**
     * get selected heat map rows
     *
     * @return set of heat map selected rows values
     */
    public Set<String> getSelectedHeatMapRows() {
        return selectedHeatMapRows;
    }

    /**
     * get selected heat map selected columns values
     *
     * @return set of heat map selected columns values
     */
    public Set<String> getSelectedHeatMapColumns() {
        return selectedHeatMapColumns;
    }

    /**
     * get elected Disease Group
     *
     * @return array of current selected Disease Group
     */
    public DiseaseGroup[] getDiseaseGroupsArr() {
        return diseaseGroupsArr;
    }

    public Set<JFreeChart> getStudiesOverviewPieChart() {
        return studiesOverviewPieChart;
    }

    public void setStudiesOverviewPieChart(Set<JFreeChart> studiesOverviewPieChart) {
        this.studiesOverviewPieChart = studiesOverviewPieChart;
    }

}
