package no.uib.probe.csf.pr.touch.logic.beans;

import java.util.Map;
import java.util.Set;

/**
 *
 * @author Yehia Farag
 *
 * This class represents initial information object that have an overview of the
 * resource data
 */
public class InitialInformationObject {

    /*
     *Map of quant datasets object map to its index in the database
     */
    private Map<Integer, QuantDataset> quantDatasetsList;
    /*
     *Array of active dataset pie charts filters  in the system using their index
     *0:Year
     *1:Study Type
     *2:Sample Matching
     *3:Technology
     *4:Analytical Approach
     *5:Shotgun/Targeted
     */
    private boolean[] activeDatasetPieChartsFilters;

    /*
     *Disease category name (MS, AD, PD..etc)
     */
    private Set<String> diseaseCategories;

    /**
     * Get disease categories list (MS,AD,PD...etc)
     *
     * @return diseaseCategories
     */
    public Set<String> getDiseaseCategories() {
        return diseaseCategories;
    }

    /**
     * Set disease categories list (MS,AD,PD...etc)
     *
     * @param diseaseCategories
     */
    public void setDiseaseCategories(Set<String> diseaseCategories) {
        this.diseaseCategories = diseaseCategories;
    }

    /**
     * Get map of quant datasets object map to its index in the database
     *
     * @return quantDatasetsList
     */
    public Map<Integer, QuantDataset> getQuantDatasetsList() {
        return quantDatasetsList;
    }

    /**
     * Set map of quant datasets object map to its index in the database
     *
     * @param quantDatasetsList
     */
    public void setQuantDatasetsList(Map<Integer, QuantDataset> quantDatasetsList) {
        this.quantDatasetsList = quantDatasetsList;
    }

    /**
     * Get array of active dataset pie charts filters in the system using their
     * index 0:Year 1:Study Type 2:Sample Matching 3:Technology 4:Analytical
     * Approach 5:Shotgun/Targeted
     *
     * @return
     */
    public boolean[] getActiveDatasetPieChartsFilters() {
        return activeDatasetPieChartsFilters;
    }

    /**
     * Set array of active dataset pie charts filters in the system using their
     * index 0:Year 1:Study Type 2:Sample Matching 3:Technology 4:Analytical
     * Approach 5:Shotgun/Targeted
     *
     * @param activeDatasetPieChartsFilters
     */
    public void setActiveDatasetPieChartsFilters(boolean[] activeDatasetPieChartsFilters) {
        this.activeDatasetPieChartsFilters = activeDatasetPieChartsFilters;
    }
}
