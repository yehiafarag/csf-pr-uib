/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.selectionmanager;

import java.awt.Color;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.jfree.chart.JFreeChart;
import probe.com.handlers.CSFPRHandler;
import probe.com.model.beans.quant.QuantDatasetObject;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
import probe.com.model.beans.quant.QuantProtein;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.DiseaseGroupsComparisonsProteinLayout;
import probe.com.view.core.DiseaseGroup;

/**
 *
 * @author Yehia Farag
 *
 * The central selection manager to manage all selections
 */
public class QuantCentralManager implements Serializable {

    private final StudiesFilterManager Studies_Filter_Manager;
    private final StudiesSelectionManager Studies_Selection_Manager;

    private final CSFPRHandler CSFPR_Handler;
    private final Map<String, String> diseaseHashedColorMap;
     private final Map<String, String> diseaseStyleMap;

    public Map<String, String> getDiseaseStyleMap() {
        return diseaseStyleMap;
    }

    public String getDiseaseHashedColor(String diseaseName) {
        return diseaseHashedColorMap.get(diseaseName);
    }

    private final Map<String, Map<String, String>> default_DiseaseCat_DiseaseGroupMap;
    private final Map<String, Set<String>> diseaseGroupsHeaderToOregenalDiseaseGroupsNames;

    public QuantCentralManager(CSFPRHandler CSFPR_Handler) {
        this.CSFPR_Handler = CSFPR_Handler;
        this.diseaseHashedColorMap = CSFPR_Handler.getDiseaseHashedColorMap();
       

        Studies_Filter_Manager = new StudiesFilterManager(CSFPR_Handler.getQuantDatasetInitialInformationObject(), CSFPR_Handler.getActivePieChartQuantFilters(), CSFPR_Handler.getDefault_DiseaseCat_DiseaseGroupMap());//,filterUtility.getFullFilterList()
        Studies_Selection_Manager = new StudiesSelectionManager();
        default_DiseaseCat_DiseaseGroupMap = Studies_Filter_Manager.getDefault_DiseaseCat_DiseaseGroupMap();
        diseaseGroupsHeaderToOregenalDiseaseGroupsNames = Studies_Filter_Manager.getDiseaseGroupsHeaderToOregenalDiseaseGroupsNames();
        diseaseStyleMap = CSFPR_Handler.getDiseaseStyleMap();

    }

    public QuantCentralManager(CSFPRHandler CSFPR_Handler, List<QuantProtein> searchQuantificationProtList) {
        this.CSFPR_Handler = CSFPR_Handler;
        this.diseaseHashedColorMap = CSFPR_Handler.getDiseaseHashedColorMap();

        Studies_Filter_Manager = new StudiesFilterManager(CSFPR_Handler.getQuantDatasetInitialInformationObject(searchQuantificationProtList), CSFPR_Handler.getActivePieChartQuantFilters(searchQuantificationProtList), CSFPR_Handler.getDefault_DiseaseCat_DiseaseGroupMap());//,filterUtility.getFullFilterList()
        Studies_Selection_Manager = new StudiesSelectionManager();

        default_DiseaseCat_DiseaseGroupMap = Studies_Filter_Manager.getDefault_DiseaseCat_DiseaseGroupMap();
        diseaseGroupsHeaderToOregenalDiseaseGroupsNames = Studies_Filter_Manager.getDiseaseGroupsHeaderToOregenalDiseaseGroupsNames();
        diseaseStyleMap = CSFPR_Handler.getDiseaseStyleMap(); ;
    }

    public QuantCentralManager(CSFPRHandler CSFPR_Handler, List<QuantProtein> searchQuantificationProtList, QuantDiseaseGroupsComparison userCustomizedComparison) {
        this.CSFPR_Handler = CSFPR_Handler;
        this.diseaseHashedColorMap = CSFPR_Handler.getDiseaseHashedColorMap();

        Studies_Filter_Manager = new StudiesFilterManager(CSFPR_Handler.getQuantDatasetInitialInformationObject(searchQuantificationProtList), CSFPR_Handler.getActivePieChartQuantFilters(searchQuantificationProtList), userCustomizedComparison, CSFPR_Handler.getDefault_DiseaseCat_DiseaseGroupMap());//,filterUtility.getFullFilterList()
        Studies_Selection_Manager = new StudiesSelectionManager();

        default_DiseaseCat_DiseaseGroupMap = Studies_Filter_Manager.getDefault_DiseaseCat_DiseaseGroupMap();
        diseaseGroupsHeaderToOregenalDiseaseGroupsNames = Studies_Filter_Manager.getDiseaseGroupsHeaderToOregenalDiseaseGroupsNames();
        diseaseStyleMap  = CSFPR_Handler.getDiseaseStyleMap();
    }

    public void setNoSerum(boolean noSerum) {
        Studies_Filter_Manager.setNoSerum(noSerum);
        Studies_Filter_Manager.resetFilters();
    }

    /**
     * get all quant dataset list available
     *
     * @return
     */
    public Map<Integer, QuantDatasetObject> getFullQuantDatasetMap() {
        return Studies_Filter_Manager.getFullQuantDatasetMap();
    }

    public Map<String, Map<String, String>> getDefault_DiseaseCat_DiseaseGroupMap() {
        return default_DiseaseCat_DiseaseGroupMap;
    }

    /**
     * register filter to the selection manager
     *
     * @param iFilter instance of CSFFilter
     */
    public void registerFilterListener(final CSFFilter iFilter) {
        Studies_Filter_Manager.registerFilter(iFilter);
    }

    /**
     * register filter to the selection manager
     *
     * @param iFilter instance of CSFFilter
     */
    public void registerStudySelectionListener(final CSFFilter iFilter) {
        Studies_Selection_Manager.registerFilter(iFilter);
    }

//    public List<Integer> getSelectedDataset() {
//        return Studies_Selection_Manager.getSelectedDataset();
//    }
//
//    /**
//     *
//     * @param selectedDataset
//     */
//    public void setSelectedDataset(List<Integer> selectedDataset) {
//        this.Studies_Selection_Manager.setSelectedDataset(selectedDataset);
//    }
    /**
     *
     * @return
     */
    public QuantDatasetObject[] getSelectedQuantDatasetIndexes() {
        return Studies_Selection_Manager.getSelectedQuantDatasetIndexes();
    }

    /**
     *
     * @param selectedComparisonList
     */
    public void updateSelectedComparisonList(Set<QuantDiseaseGroupsComparison> selectedComparisonList) {
        this.Studies_Selection_Manager.updateSelectedComparisonList(selectedComparisonList);

    }

    /**
     *
     * @return
     */
    public CSFFilterSelection getStudyListenerSelection() {
        return Studies_Selection_Manager.getFilterSelection();
    }

    /**
     *
     * @param selection
     */
    public void setStudySelectionListenerLevel(CSFFilterSelection selection) {
        Studies_Selection_Manager.setStudyLevelFilterSelection(selection);

    }

    /**
     * set the selected Quant Disease Groups Comparison to the selection manager
     *
     * @param selectedDiseaseGroupsComparisonList
     */
    public void setDiseaseGroupsComparisonSelection(Set<QuantDiseaseGroupsComparison> selectedDiseaseGroupsComparisonList) {
        Studies_Selection_Manager.setDiseaseGroupsComparisonSelection(selectedDiseaseGroupsComparisonList);
    }

    /**
     * set the selected Quant Disease Groups Comparison to the selection manager
     *
     * @param significantOnly
     */
    public void updateSignificantOnlySelection(boolean significantOnly) {
        Studies_Selection_Manager.updateSignificantOnlySelection(significantOnly);
    }

    /**
     * set the selected Quant proteins to the selection manager
     *
     * @param protSelectionMap
     */
    public void setQuantProteinsSelectionLayout(Map<String, DiseaseGroupsComparisonsProteinLayout[]> protSelectionMap) {

        this.Studies_Selection_Manager.setQuantProteinsSelectionLayout(protSelectionMap);

    }

    public void selectionQuantProteinsSelectionLayoutChanged() {
        Studies_Selection_Manager.selectionQuantProteinsSelectionLayoutChanged();
    }

    public String getSelectedComparisonHeader() {
        return Studies_Selection_Manager.getSelectedComparisonHeader();
    }

    /**
     * set the selected Quant proteins to the selection manager
     *
     * @param protSelectionSet
     * @param selectedComparisonHeader
     */
    public void setQuantProteinsSelection(Set<String> protSelectionSet, String selectedComparisonHeader) {
        Studies_Selection_Manager.setQuantProteinsSelection(protSelectionSet, selectedComparisonHeader);

    }

    /**
     * remove value upon un-select filter
     *
     * @param filterId
     * @param filterValue
     */
    public void removeFilterValue(String filterId, String filterValue) {
        Studies_Selection_Manager.removeFilterValue(filterId, filterValue);

    }

    public Set<String> getProtSelectionSet() {
        return Studies_Selection_Manager.getProtSelectionSet();
    }

    /**
     *
     * @return map of quant proteins array that include the comparisons
     * information columns for each protein
     */
    public Map<String, DiseaseGroupsComparisonsProteinLayout[]> getQuantProteinsLayoutSelectionMap() {
        return Studies_Selection_Manager.getQuantProteinsLayoutSelectionMap();
    }

    /**
     * get Selected disease groups comparison List
     *
     * @return
     */
    public Set<QuantDiseaseGroupsComparison> getSelectedDiseaseGroupsComparisonList() {
        return Studies_Selection_Manager.getSelectedDiseaseGroupsComparisonList();
    }

    /**
     * get Selected disease groups comparison List
     *
     * @param searchQuantificationProtList
     * @return
     */
    public Set<QuantDiseaseGroupsComparison> getUpdatedSelectedDiseaseGroupsComparisonListProteins(List<QuantProtein> searchQuantificationProtList) {
        Set<QuantDiseaseGroupsComparison> selectedComparisonList = Studies_Selection_Manager.getSelectedDiseaseGroupsComparisonList();
        Iterator<QuantDiseaseGroupsComparison> itr = selectedComparisonList.iterator();
        while (itr.hasNext()) {
            if (itr.next().getComparProtsMap() == null) {//               
                    selectedComparisonList = CSFPR_Handler.getComparisonProtList(selectedComparisonList, searchQuantificationProtList, diseaseGroupsHeaderToOregenalDiseaseGroupsNames);
                break;
            }
        }

        return selectedComparisonList;
    }

//    private Set<QuantDiseaseGroupsComparison> resetGroupingNames(Set<QuantDiseaseGroupsComparison> selectedComparisonList){
//            Set<QuantDiseaseGroupsComparison> resetedGroupsNamesList = new LinkedHashSet<QuantDiseaseGroupsComparison>();
//            for(QuantDiseaseGroupsComparison qdc : selectedComparisonList )
//            {
//                System.err.println("at reseted "+qdc.getComparisonHeader() );
//                String header = 
////                qdc.setComparisonHeader(null);
//                resetedGroupsNamesList.add(qdc);
//            }
//            return resetedGroupsNamesList;
//    
//    
//    }
//    
//    private Set<QuantDiseaseGroupsComparison> restoreGroupingNames(Set<QuantDiseaseGroupsComparison> selectedComparisonList){
//        Set<QuantDiseaseGroupsComparison> restoredGroupsNamesList = new LinkedHashSet<QuantDiseaseGroupsComparison>();
//            for(QuantDiseaseGroupsComparison qdc : selectedComparisonList )
//            {
//                System.err.println("at restored "+qdc.getComparisonHeader() );
////                qdc.setComparisonHeader(null);
//                restoredGroupsNamesList.add(qdc);
//            }
//            return restoredGroupsNamesList;
//    
//    }
//    
    public Map<String, String> getDiseaseHashedColorMap() {
        return diseaseHashedColorMap;
    }

    /**
     * get Selected proteinKey
     *
     * @return selected Protein key
     */
    public String getSelectedProteinKey() {
        return Studies_Selection_Manager.getSelectedProteinKey();
    }

    public void setSelectedProteinKey(String selectedProteinKey) {
        Studies_Selection_Manager.setSelectedProteinKey(selectedProteinKey);
    }

    public boolean isSignificantOnly() {
        return Studies_Selection_Manager.isSignificantOnly();
    }

    public Set<JFreeChart> getProteinsOverviewBubbleChart() {
        return Studies_Selection_Manager.getProteinsOverviewBubbleChart();
    }

    public void setProteinsOverviewBubbleChart(JFreeChart proteinsOverviewBubbleChart) {
        Studies_Selection_Manager.setProteinsOverviewBubbleChart(proteinsOverviewBubbleChart);
    }

    // filter selection manager
    public Set<JFreeChart> getStudiesOverviewPieChart() {
        return Studies_Filter_Manager.getStudiesOverviewPieChart();
    }

    public void setStudiesOverviewPieChart(Set<JFreeChart> studiesOverviewPieChart) {
        Studies_Filter_Manager.setStudiesOverviewPieChart(studiesOverviewPieChart);
    }

    public boolean[] getActiveHeader() {
        return Studies_Filter_Manager.getActiveHeader();
    }

    /**
     * set selected heat map selected Rows values
     *
     * @param selectedHeatMapRows set of heat map selected rows values
     *
     */
    public void setSelectedHeatMapRows(LinkedHashSet<String> selectedHeatMapRows) {
        Studies_Filter_Manager.setSelectedHeatMapRows(selectedHeatMapRows);
    }

    /**
     * set selected heat map selected columns values
     *
     * @param selectedHeatMapColumns set of heat map selected columns values
     */
    public void setSelectedHeatMapColumns(LinkedHashSet<String> selectedHeatMapColumns) {
        Studies_Filter_Manager.setSelectedHeatMapColumns(selectedHeatMapColumns);
    }

    /**
     *
     * @return
     */
    public Map<Integer, QuantDatasetObject> getFilteredQuantDatasetArr() {
        return Studies_Filter_Manager.getFilteredQuantDatasetArr();
    }

    public Set<String> getDiseaseCategorySet() {
        return Studies_Filter_Manager.getDiseaseCategorySet();
    }

    /**
     *
     * @param selection
     */
    public void applyFilters(CSFFilterSelection selection) {
        Studies_Filter_Manager.applyFilters(selection);

    }

    /**
     *
     * @return
     */
    public CSFFilterSelection getFilterListenerSelection() {
        return Studies_Filter_Manager.getFilterSelection();
    }

    /**
     * reset all disease groups filters
     */
    public void resetFiltersListener() {
        Studies_Filter_Manager.resetFilters();

    }

    /**
     * get current active quant filters
     *
     * @return
     */
    public boolean[] getActiveFilters() {
        return Studies_Filter_Manager.getActiveFilters();
    }

    public Map<Integer, QuantDatasetObject> getFilteredDatasetsList() {

        return Studies_Filter_Manager.getFilteredDatasetsList();
    }

    public void changeDiseaseCategory(String diseaseCategory) {
        Studies_Filter_Manager.changeDiseaseCategory(diseaseCategory);

    }

    public void updateDiseaseGroupsNames(Map<String, Map<String, String>> updatedGroupsNamesMap) {
        Studies_Filter_Manager.updateDiseaseGroupsNames(updatedGroupsNamesMap);
        
    }

    public String getInUseDiseaseName() {
        return Studies_Filter_Manager.getInUseDiseaseName();
    }

    public int getTotalDsNumber() {
        return Studies_Filter_Manager.getTotalDsNumber();
    }

    public int getCurrentDsNumber() {
        return Studies_Filter_Manager.getCurrentDsNumber();
    }

    /**
     *
     * @param selectedRows
     * @param selectedColumns
     * @param diseaseGroupsArr
     */
    public void setHeatMapLevelSelection(LinkedHashSet<String> selectedRows, LinkedHashSet<String> selectedColumns, DiseaseGroup[] diseaseGroupsArr) {
        Studies_Filter_Manager.setHeatMapLevelSelection(selectedRows, selectedColumns, diseaseGroupsArr);

    }

    /**
     * get selected heat map rows
     *
     * @return set of heat map selected rows values
     */
    public LinkedHashSet<String> getSelectedHeatMapRows() {
        return Studies_Filter_Manager.getSelectedHeatMapRows();
    }

    /**
     * get elected Disease Group
     *
     * @return array of current selected Disease Group
     */
    public DiseaseGroup[] getDiseaseGroupsArray() {
        return Studies_Filter_Manager.getDiseaseGroupsArray();
    }

    /**
     * get selected heat map selected columns values
     *
     * @return set of heat map selected columns values
     */
    public LinkedHashSet<String> getSelectedHeatMapColumns() {
        return Studies_Filter_Manager.getSelectedHeatMapColumns();
    }

//    public DiseaseGroup[] getDiseaseGroupsArr() {
//        return Studies_Filter_Manager.getDiseaseGroupsArr();
//    }
}
