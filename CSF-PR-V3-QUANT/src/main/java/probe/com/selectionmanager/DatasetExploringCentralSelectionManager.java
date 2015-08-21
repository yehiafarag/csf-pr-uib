package probe.com.selectionmanager;

import com.vaadin.server.VaadinSession;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.ComparisonProtein;
import probe.com.model.beans.quant.QuantGroupsComparison;
import probe.com.model.beans.quant.QuantDatasetObject;
import probe.com.view.core.DiseaseGroup;

/**
 *
 * @author Yehia Farag
 */
public class DatasetExploringCentralSelectionManager {

    /**
     *
     * @return
     */
    public Set<String> getSelectedHeatMapRows() {
        return selectedHeatMapRows;
    }

    /**
     *
     * @return
     */
    public Set<String> getSelectedHeatMapColumns() {
        return selectedHeatMapColumns;
    }

    /**
     *
     * @return
     */
    public DiseaseGroup[] getDiseaseGroupsArr() {
        return diseaseGroupsArr;
    }

    /**
     *
     */
    public enum Selection_Type {

        /**
         *
         */
        Disease_Groups_Level,

        /**
         *
         */
        Reset_Disease_Groups_Level,

        /**
         *
         */
        HeatMap_Update_level

    }
    private final Map<Integer,QuantDatasetObject> fullDatasetArr;
    private Map<Integer,QuantDatasetObject> filteredDatasetArr = new LinkedHashMap<Integer,QuantDatasetObject>();

    private final Set<CSFFilter> registeredFilterSet = new HashSet<CSFFilter>();

    private final boolean[] activeFilters;

    /**
     *
     * @return
     */
    public Map<Integer,QuantDatasetObject> getFilteredDatasetArr() {
        return filteredDatasetArr;
    }

    /**
     *
     * @return
     */
    public int getSelectedDataset() {
        return selectedDataset;
    }

    /**
     *
     * @param selectedDataset
     */
    public void setSelectedDataset(int selectedDataset) {
        this.selectedDataset = selectedDataset;
    }

    private int selectedDataset = -1;

    /**
     *
     * @param datasetsList
     * @param activeFilters
     */
    public DatasetExploringCentralSelectionManager(Map<Integer,QuantDatasetObject> datasetsList, boolean[] activeFilters) {
        this.fullDatasetArr = datasetsList;
        this.activeFilters = activeFilters;
       
    }

    private void updateFilteredDatasetList(int[] datasetIndexes) {
        
        
        if (datasetIndexes.length == 0) {
            filteredDatasetArr = fullDatasetArr;
            return;
        }

        filteredDatasetArr.clear();
//        filteredDatasetArr = new QuantDatasetObject[datasetIndexes.length];
//        int z = 0;

        for (int i : datasetIndexes) {
            filteredDatasetArr.put(i, fullDatasetArr.get(i));

        }

    }

    private QuantDatasetObject[] selectedDSIndexes;

    /**
     *
     * @return
     */
    public QuantDatasetObject[] getSelectedDSIndexes() {
        return selectedDSIndexes;
    }

    private Set<QuantGroupsComparison> selectedComparisonList;

    /**
     *
     * @param selectedComparisonList
     */
    public void updateSelectedComparisonList(Set<QuantGroupsComparison> selectedComparisonList) {
        this.selectedComparisonList = selectedComparisonList;

    }

    /**
     *
     * @return
     */
    public CSFFilterSelection getFilterSelection() {
        return filterSelection;
    }
    private CSFFilterSelection filterSelection;

    /**
     *
     * @param selection
     */
    public void setStudyLevelFilterSelection(CSFFilterSelection selection) {
        try {
            filterSelection = selection;
            if (selection.getType().equalsIgnoreCase("Disease_Groups_Level")) {
                updateFilteredDatasetList(selection.getDatasetIndexes());
                this.SelectionChanged(selection.getType());
            } else {
                this.SelectionChanged("StudySelection");
            }

        } finally {

        }

    }

    private Set<String> selectedHeatMapRows;
    private Set<String> selectedHeatMapColumns;
    private DiseaseGroup[] diseaseGroupsArr;

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
     *
     * @param selectedComparisonList
     */
    public void setComparisonSelection(Set<QuantGroupsComparison> selectedComparisonList) {
        try {

            VaadinSession.getCurrent().getLockInstance().lock();
            this.selectedComparisonList = selectedComparisonList;
            this.SelectionChanged("ComparisonSelection");

        } finally {
            VaadinSession.getCurrent().getLockInstance().unlock();
        }

    }
    private Map<String, ComparisonProtein[]> protSelectionMap;

    /**
     *
     * @param protSelectionMap
     */
    public void setProteinsSelection(Map<String, ComparisonProtein[]> protSelectionMap) {
        try {

            VaadinSession.getCurrent().getLockInstance().lock();
            this.protSelectionMap = protSelectionMap;
            this.SelectionChanged("quantProtSelection");

        } finally {
            VaadinSession.getCurrent().getLockInstance().unlock();
        }

    }

    /**
     *
     */
    public void resetFilters() {
        filteredDatasetArr.clear();
        this.SelectionChanged("Reset_Disease_Groups_Level");

    }

    /**
     *
     * @return
     */
    public boolean[] getActiveFilters() {
        return activeFilters;
    }

    /**
     *
     * @param filterId
     * @param filterValue
     */
    public void removeFilterValue(String filterId, String filterValue) {
        for (CSFFilter filter : registeredFilterSet) {
            if (filter.getFilterId().equalsIgnoreCase(filterId)) {
                filter.removeFilterValue(filterValue);
                break;
            }

        }

    }

    private void SelectionChanged(String type) {
        for (CSFFilter filter : registeredFilterSet) {
            filter.selectionChanged(type);
        }
    }

    /**
     *
     * @return
     */
    public Map<String, ComparisonProtein[]> getProtSelectionMap() {
        return protSelectionMap;
    }

    /**
     *
     * @param iFilter
     */
    public void registerFilter(final CSFFilter iFilter) {
        registeredFilterSet.add(iFilter);
    }

    /**
     *
     * @return
     */
    public Map<Integer,QuantDatasetObject> getFilteredDatasetsList() {
        if (filteredDatasetArr == null || filteredDatasetArr.isEmpty()) {
            return fullDatasetArr;
        }
        return filteredDatasetArr;
    }

    /**
     *
     * @return
     */
    public Map<Integer,QuantDatasetObject> getFullDatasetArr() {
        return fullDatasetArr;
    }

    /**
     *
     * @return
     */
    public Set<QuantGroupsComparison> getSelectedComparisonList() {
        return selectedComparisonList;
    }

}
