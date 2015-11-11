package probe.com.selectionmanager;

import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import probe.com.model.beans.quant.QuantDatasetInitialInformationObject;
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
public class DatasetExploringCentralSelectionManager implements Serializable {

    private Map<Integer, QuantDatasetObject> fullQuantDatasetMap;
    private Map<Integer, QuantDatasetObject> filteredQuantDatasetArr = new LinkedHashMap<Integer, QuantDatasetObject>();
    private final Set<CSFFilter> registeredFilterSet = new LinkedHashSet<CSFFilter>();
    private final Map<String, boolean[]> activeFilterMap;
    private boolean[] activeFilters;

    public boolean[] getActiveHeader() {
        return activeHeader;
    }
    private boolean[] activeHeader;
    private QuantDatasetObject[] selectedQuantDatasetIndexes;
    private Set<QuantDiseaseGroupsComparison> selectedDiseaseGroupsComparisonList;
    private Map<String, DiseaseGroupsComparisonsProteinLayout[]> quantProteinsLayoutSelectionMap;
    private String selectedProteinKey;
    private Set<String> diseaseCategorySet;
    
    private int totalDsNumber,currentDsNumber;

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
    public List<Integer> getSelectedDataset() {
        return selectedDataset;
    }

    /**
     *
     * @param selectedDataset
     */
    public void setSelectedDataset(List<Integer> selectedDataset) {
        this.selectedDataset = selectedDataset;
    }

    public Set<String> getDiseaseCategorySet() {
        return diseaseCategorySet;
    }

    private List<Integer> selectedDataset = null;
    private final Map<String, QuantDatasetInitialInformationObject> quantDatasetListObject;

    /**
     *
     * @param quantDatasetListObject
     * @param activeFilterMap
     */
    public DatasetExploringCentralSelectionManager(Map<String, QuantDatasetInitialInformationObject> quantDatasetListObject, Map<String, boolean[]> activeFilterMap) {
        this.quantDatasetListObject = quantDatasetListObject;
        String key = quantDatasetListObject.keySet().iterator().next();
        this.totalDsNumber=0;
                for(String k:quantDatasetListObject.keySet()){
                totalDsNumber+=quantDatasetListObject.get(k).getQuantDatasetsList().size();
                
                
                }
                    
        this.fullQuantDatasetMap = quantDatasetListObject.get(key).getQuantDatasetsList();
        this.currentDsNumber = fullQuantDatasetMap.size();
        this.activeFilterMap = activeFilterMap;
        this.activeFilters = activeFilterMap.get(key);
        this.activeHeader = quantDatasetListObject.get(key).getActiveHeaders();
        this.diseaseCategorySet = quantDatasetListObject.keySet();

    }

    /**
     * update the current filtered dataset indexes
     *
     * @param datasetIndexes
     */
    private void updateFilteredDatasetList(int[] datasetIndexes) {

        if (datasetIndexes.length == 0) {
            filteredQuantDatasetArr = fullQuantDatasetMap;
            return;
        }
        filteredQuantDatasetArr.clear();
        for (int i : datasetIndexes) {
            filteredQuantDatasetArr.put(i, fullQuantDatasetMap.get(i));
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
    private boolean significantOnly = false;

    /**
     * set the selected Quant Disease Groups Comparison to the selection manager
     *
     * @param significantOnly
     */
    public void updateSignificantOnlySelection(boolean significantOnly) {
        try {

            VaadinSession.getCurrent().getLockInstance().lock();
            this.significantOnly = significantOnly;

            for (CSFFilter filter : registeredFilterSet) {
                if (!filter.getFilterId().equalsIgnoreCase("HeatMapFilter")) {
                    filter.selectionChanged("Comparison_Selection");
                }
            }

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

        } catch (Exception exp) {
            exp.printStackTrace();
            System.err.println("at error " + this.getClass().getName() + "  line 2261  " + exp.getLocalizedMessage());

        } finally {
            VaadinSession.getCurrent().getLockInstance().unlock();
        }

    }

    private Set<String> protSelectionSet;

    public String getSelectedComparisonHeader() {
        return selectedComparisonHeader;
    }
    private String selectedComparisonHeader;

    /**
     * set the selected Quant proteins to the selection manager
     *
     * @param protSelectionSet
     * @param selectedComparisonHeader
     */
    public void setQuantProteinsSelection(Set<String> protSelectionSet, String selectedComparisonHeader) {
        try {
            VaadinSession.getCurrent().getLockInstance().lock();
            this.protSelectionSet = protSelectionSet;
            this.selectedComparisonHeader = selectedComparisonHeader;
            this.SelectionChanged("Protens_Selection");

        } catch (Exception exp) {
            exp.printStackTrace();
            System.err.println("at error " + this.getClass().getName() + "  line 291  " + exp.getLocalizedMessage());

        } finally {
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

    public Set<String> getProtSelectionSet() {
        return protSelectionSet;
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
            return fullQuantDatasetMap;
        }
        return filteredQuantDatasetArr;
    }

    /**
     * get all quant dataset list available
     *
     * @return
     */
    public Map<Integer, QuantDatasetObject> getFullQuantDatasetMap() {
        return fullQuantDatasetMap;
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

    public boolean isSignificantOnly() {
        return significantOnly;
    }

    public void exportFullReport() {

    }

    public void changeDiseaseCategory(String diseaseCategory) {
        this.fullQuantDatasetMap = quantDatasetListObject.get(diseaseCategory).getQuantDatasetsList();
        this.filteredQuantDatasetArr.clear();
        this.diseaseCategorySet = quantDatasetListObject.get(diseaseCategory).getDiseaseCategories();
        this.activeHeader = quantDatasetListObject.get(diseaseCategory).getActiveHeaders();
        this.activeFilters = activeFilterMap.get(diseaseCategory);
        this.currentDsNumber = fullQuantDatasetMap.size();
//        this.SelectionChanged("Disease_Category_Selection");

    }

    public int getTotalDsNumber() {
        return totalDsNumber;
    }

    public int getCurrentDsNumber() {
        return currentDsNumber;
    }

}
