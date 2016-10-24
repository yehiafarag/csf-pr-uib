package no.uib.probe.csf.pr.touch.selectionmanager;

import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;

/**
 *
 * @author Yehia Farag
 *
 * This class represents quant searching selection object, the class has all the
 * information required for view selection in quant data layout
 */
public class QuantSearchSelection {

    /*
     *Disease category name (MS, AD, PD..etc)
     */
    private String diseaseCategory;
    /*
     *Set of the found dataset indexes in the database
     */
    private Set<Integer> quantDatasetIndexes;
    /*
     *Set of the keywords used for the searching query  
     */
    private Set<String> keyWords;
    /*
     *List of selected comparisons to be updated based on user selection for comparisons across the system
     */
    private Set<QuantDiseaseGroupsComparison> selectedComparisonsList;
    /*
     *List of selected proteins to be updated based on user selection for comparisons across the system
     */
    private Set<String> selectedProteinsList;
    /*
     *Map of disease category to found dataset ids
     */
    private Map<String, Set<Integer>> diseaseCategoriesIdMap;

    /*
     *Customized comparison based on user input data in quant comparison layout
     */
    private QuantDiseaseGroupsComparison userCustComparison;

    /**
     * Get customized comparison based on user input data in quant comparison
     * layout
     *
     * @return userCustComparison
     */
    public QuantDiseaseGroupsComparison getUserCustComparison() {
        return userCustComparison;
    }

    /**
     * Set customized comparison based on user input data in quant comparison
     * layout
     *
     * @param userCustComparison
     */
    public void setUserCustComparison(QuantDiseaseGroupsComparison userCustComparison) {
        this.userCustComparison = userCustComparison;
    }

    /**
     * Get set of the keywords used for the searching query
     *
     * @return keyWords
     */
    public Set<String> getKeyWords() {
        return keyWords;
    }

    /**
     * Set set of the keywords used for the searching query
     *
     * @param keyWords
     */
    public void setKeyWords(Set<String> keyWords) {
        this.keyWords = keyWords;
    }

    /**
     * Get disease category (MS,AD,PD...etc)
     *
     * @return diseaseCategory
     */
    public String getDiseaseCategory() {
        return diseaseCategory;
    }

    /**
     * Set disease category (MS,AD,PD...etc)
     *
     * @param diseaseCategory
     */
    public void setDiseaseCategory(String diseaseCategory) {
        this.diseaseCategory = diseaseCategory;
    }

    /**
     * Get set of the found dataset indexes in the database
     *
     * @return quantDatasetIndexes
     */
    public Set<Integer> getQuantDatasetIndexes() {
        return quantDatasetIndexes;
    }

    /**
     * Set set of the found dataset indexes in the database
     *
     * @param quantDatasetIndexes
     */
    public void setQuantDatasetIndexes(Set<Integer> quantDatasetIndexes) {
        this.quantDatasetIndexes = quantDatasetIndexes;
    }

    /**
     * Get list of selected comparisons to be updated based on user selection
     * for comparisons across the system
     *
     * @return selected comparisons list
     */
    public Set<QuantDiseaseGroupsComparison> getSelectedComparisonsList() {
        return selectedComparisonsList;
    }

    /**
     * Set list of selected comparisons to be updated based on user selection
     * for comparisons across the system
     *
     * @param selectedComparisonsList selected comparisons list
     */
    public void setSelectedComparisonsList(Set<QuantDiseaseGroupsComparison> selectedComparisonsList) {
        this.selectedComparisonsList = selectedComparisonsList;
    }

    /**
     * Get list of selected proteins to be updated based on user selection for
     * comparisons across the system
     *
     * @return selectedProteinsList
     */
    public Set<String> getSelectedProteinsList() {
        return selectedProteinsList;
    }

    /**
     * Set list of selected proteins to be updated based on user selection for
     * comparisons across the system
     *
     * @param selectedProteinsList
     */
    public void setSelectedProteinsList(Set<String> selectedProteinsList) {
        this.selectedProteinsList = selectedProteinsList;
    }

    /**
     * Get map of disease category to found dataset ids
     *
     * @return diseaseCategoriesIdMap
     */
    public Map<String, Set<Integer>> getDiseaseCategoriesIdMap() {
        return diseaseCategoriesIdMap;
    }

    /**
     * Set map of disease category to found dataset ids
     *
     * @param diseaseCategoriesIdMap
     */
    public void setDiseaseCategoriesIdMap(Map<String, Set<Integer>> diseaseCategoriesIdMap) {
        this.diseaseCategoriesIdMap = diseaseCategoriesIdMap;
    }

}
