package probe.com.selectionmanager;

import com.vaadin.server.VaadinSession;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.DiseaseGroupsComparisonsProteinLayout;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
import probe.com.model.beans.quant.QuantDatasetObject;
import probe.com.view.core.DiseaseGroup;

/**
 *
 * @author Yehia Farag
 *
 * central selection manager for quant data that is responsible for quant data
 * layout interactivity
 */
public class DatasetExploringCentralSelectionManager {

    private final Map<Integer, QuantDatasetObject> fullQuantDatasetArr;
    private Map<Integer, QuantDatasetObject> filteredQuantDatasetArr = new LinkedHashMap<Integer, QuantDatasetObject>();
    private final Set<CSFFilter> registeredFilterSet = new HashSet<CSFFilter>();
    private final boolean[] activeFilters;
    private QuantDatasetObject[] selectedQuantDatasetIndexes;
    private Set<QuantDiseaseGroupsComparison> selectedDiseaseGroupsComparisonList;
    private Map<String, DiseaseGroupsComparisonsProteinLayout[]> quantProteinsLayoutSelectionMap;
    private String selectedProteinKey;

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

    /**
     *
     * @return
     */
    public Map<Integer, QuantDatasetObject> getFilteredQuantDatasetArr() {
        return filteredQuantDatasetArr;
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
     * @param quantDatasetsList
     * @param activeFilters
     */
    public DatasetExploringCentralSelectionManager(Map<Integer, QuantDatasetObject> quantDatasetsList, boolean[] activeFilters) {
        this.fullQuantDatasetArr = quantDatasetsList;
        this.activeFilters = activeFilters;

    }

    /**
     * update the current filtered dataset indexes
     *
     * @param datasetIndexes
     */
    private void updateFilteredDatasetList(int[] datasetIndexes) {

        if (datasetIndexes.length == 0) {
            filteredQuantDatasetArr = fullQuantDatasetArr;
            return;
        }
        filteredQuantDatasetArr.clear();
        for (int i : datasetIndexes) {
            filteredQuantDatasetArr.put(i, fullQuantDatasetArr.get(i));
        }

    }

    /**
     *
     * @return
     */
    public QuantDatasetObject[] getSelectedQuantDatasetIndexes() {
        return selectedQuantDatasetIndexes;
    }

    /**
     *
     * @param selectedComparisonList
     */
    public void updateSelectedComparisonList(Set<QuantDiseaseGroupsComparison> selectedComparisonList) {
        this.selectedDiseaseGroupsComparisonList = selectedComparisonList;

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
                this.SelectionChanged("Study_Selection");
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
     * set the selected Quant Disease Groups Comparison to the selection manager
     *
     * @param selectedDiseaseGroupsComparisonList
     */
    public void setDiseaseGroupsComparisonSelection(Set<QuantDiseaseGroupsComparison> selectedDiseaseGroupsComparisonList) {
        try {

            VaadinSession.getCurrent().getLockInstance().lock();
            this.selectedDiseaseGroupsComparisonList = selectedDiseaseGroupsComparisonList;
            this.SelectionChanged("Comparison_Selection");

        } finally {
            VaadinSession.getCurrent().getLockInstance().unlock();
        }

    }

    /**
     * set the selected Quant proteins to the selection manager
     *
     * @param protSelectionMap
     */
    public void setQuantProteinsSelectionLayout(Map<String, DiseaseGroupsComparisonsProteinLayout[]> protSelectionMap) {
        try {
            VaadinSession.getCurrent().getLockInstance().lock();
            this.quantProteinsLayoutSelectionMap = protSelectionMap;
            this.SelectionChanged("Quant_Proten_Selection");
            
        } catch(Exception exp){
            System.err.println("at error "+this.getClass().getName()+"  line 207  "+ exp.getLocalizedMessage());
        
        }finally {
            VaadinSession.getCurrent().getLockInstance().unlock();
        }

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
     * remove value upon un-select filter
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

    /**
     * update all registered filters
     */
    private void SelectionChanged(String type) {
        for (CSFFilter filter : registeredFilterSet) {
            filter.selectionChanged(type);
        }
    }

    /**
     *
     * @return map of quant proteins array that include the comparisons
     * information columns for each protein
     */
    public Map<String, DiseaseGroupsComparisonsProteinLayout[]> getQuantProteinsLayoutSelectionMap() {
        return quantProteinsLayoutSelectionMap;
    }

    /**
     * register filter to the selection manager
     *
     * @param iFilter instance of CSFFilter
     */
    public void registerFilter(final CSFFilter iFilter) {
        registeredFilterSet.add(iFilter);
    }

    /**
     * get the current filtered quant dataset list
     *
     * @return
     */
    public Map<Integer, QuantDatasetObject> getFilteredDatasetsList() {
        if (filteredQuantDatasetArr == null || filteredQuantDatasetArr.isEmpty()) {
            return fullQuantDatasetArr;
        }
        return filteredQuantDatasetArr;
    }

    /**
     * get all quant dataset list available
     *
     * @return
     */
    public Map<Integer, QuantDatasetObject> getFullQuantDatasetArr() {
        return fullQuantDatasetArr;
    }

    /**
     * get Selected disease groups comparison List
     *
     * @return
     */
    public Set<QuantDiseaseGroupsComparison> getSelectedDiseaseGroupsComparisonList() {
        return selectedDiseaseGroupsComparisonList;
    }
    
    /**
     * get Selected proteinKey 
     *
     * @return selected Protein key
     */
    public String getSelectedProteinKey() {
        return selectedProteinKey;
    }

    public void setSelectedProteinKey(String selectedProteinKey) {
        this.selectedProteinKey = selectedProteinKey;
        this.SelectionChanged("Quant_Proten_Tab_Selection");
    }

}
