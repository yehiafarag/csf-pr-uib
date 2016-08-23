/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.selectionmanager;

import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;

/**
 *
 * @author Yehia Farag
 *
 * this class represents quant searching selection the class has all the
 * information required for view selection in quant data layout
 */
public class QuantSearchSelection {

    private String diseaseCategory;
    private Set<Integer> datasetIds;
    private Set<String> keyWords;
    private Set<QuantDiseaseGroupsComparison> selectedComparisonsList;
    private Set<String> selectedProteinsList;
    private Map<String, Set<Integer>> diseaseCategoriesIdMap;

    public QuantDiseaseGroupsComparison getUserCustComparison() {
        return userCustComparison;
    }

    public void setUserCustComparison(QuantDiseaseGroupsComparison userCustComparison) {
        this.userCustComparison = userCustComparison;
    }
    private QuantDiseaseGroupsComparison userCustComparison;

    public Set<String> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(Set<String> keyWords) {
        this.keyWords = keyWords;
    }

    public String getDiseaseCategory() {
        return diseaseCategory;
    }

    public void setDiseaseCategory(String diseaseCategory) {
        this.diseaseCategory = diseaseCategory;
    }

    public Set<Integer> getDatasetIds() {
        return datasetIds;
    }

    public void setDatasetIds(Set<Integer> datasetIds) {
        this.datasetIds = datasetIds;
    }

    public Set<QuantDiseaseGroupsComparison> getSelectedComparisonsList() {
        return selectedComparisonsList;
    }

    public void setSelectedComparisonsList(Set<QuantDiseaseGroupsComparison> selectedComparisonsList) {
        this.selectedComparisonsList = selectedComparisonsList;
    }

    public Set<String> getSelectedProteinsList() {
        return selectedProteinsList;
    }

    public void setSelectedProteinsList(Set<String> selectedProteinsList) {
        this.selectedProteinsList = selectedProteinsList;
    }

    public Map<String, Set<Integer>> getDiseaseCategoriesIdMap() {
        return diseaseCategoriesIdMap;
    }

    public void setDiseaseCategoriesIdMap(Map<String, Set<Integer>> diseaseCategoriesIdMap) {
        this.diseaseCategoriesIdMap = diseaseCategoriesIdMap;
    }

}
