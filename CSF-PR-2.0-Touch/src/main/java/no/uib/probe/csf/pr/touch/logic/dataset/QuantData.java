package no.uib.probe.csf.pr.touch.logic.dataset;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseGroupComparison;
import no.uib.probe.csf.pr.touch.logic.beans.HeatMapHeaderCellInformationBean;

/**
 *
 * @author Yehia Farag
 *
 * This class contain all quant data information required in the visualization
 * layout
 */
public class QuantData implements Serializable {

    /**
     * List of active(current) heat map row header cells with all information
     * required for selecting and rendering the heat map
     */
    private LinkedHashSet<HeatMapHeaderCellInformationBean> activeRowIds;
    /**
     * List of original heat map row header cells with all information required
     * for selecting and rendering the heat map
     */
    private LinkedHashSet<HeatMapHeaderCellInformationBean> oreginalRowIds;
    /**
     * List of active(current) heat map column header cells with all information
     * required for selecting and rendering the heat map
     */
    private LinkedHashSet<HeatMapHeaderCellInformationBean> activeColumnIds;
    /**
     * List of original heat map column header cells with all information
     * required for selecting and rendering the heat map
     */
    private LinkedHashSet<HeatMapHeaderCellInformationBean> oreginalColumnIds;

    /*
     *Disease category name (MS, AD, PD..etc)
     */
    private String diseaseCategory;

    /*
     *List of disease group comparisons objects
     */
    private Set<DiseaseGroupComparison> diseaseComparisonSet;
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

    /**
     *Get list of original heat map row header cells with all information required
     * for selecting and rendering the heat map
     * @return oreginalRowIds
     */
    public LinkedHashSet<HeatMapHeaderCellInformationBean> getOreginalRowIds() {
        return oreginalRowIds;
    }

    /**
     *Set list of original heat map row header cells with all information required
     * for selecting and rendering the heat map
     * @param oreginalRowIds
     */
    public void setOreginalRowIds(LinkedHashSet<HeatMapHeaderCellInformationBean> oreginalRowIds) {
        this.oreginalRowIds = oreginalRowIds;
    }

    /**
     *Get list of original heat map column header cells with all information
     * required for selecting and rendering the heat map
     * @return oreginalColumnIds
     */
    public LinkedHashSet<HeatMapHeaderCellInformationBean> getOreginalColumnIds() {
        return oreginalColumnIds;
    }

    /**
     *Set list of original heat map column header cells with all information
     * required for selecting and rendering the heat map
     * @param oreginalColumnIds
     */
    public void setOreginalColumnIds(LinkedHashSet<HeatMapHeaderCellInformationBean> oreginalColumnIds) {
        this.oreginalColumnIds = oreginalColumnIds;
    }

    /**
     *Get list of active(current) heat map row header cells with all information
     * required for selecting and rendering the heat map
     * @return activeRowIds
     */
    public LinkedHashSet<HeatMapHeaderCellInformationBean> getActiveRowIds() {
        if (activeRowIds == null) {
            return oreginalRowIds;
        }
        return activeRowIds;
    }

    /**
     *Set list of active(current) heat map row header cells with all information
     * required for selecting and rendering the heat map
     * @param activeRowIds
     */
    public void setActiveRowIds(LinkedHashSet<HeatMapHeaderCellInformationBean> activeRowIds) {
        this.activeRowIds = activeRowIds;
    }

    /**
     *Get list of active(current) heat map column header cells with all information
     * required for selecting and rendering the heat map
     * @return activeColumnIds
     */
    public LinkedHashSet<HeatMapHeaderCellInformationBean> getActiveColumnIds() {
        if (activeColumnIds == null) {
            return oreginalColumnIds;
        }
        return activeColumnIds;
    }

    /**
     *Set list of active(current) heat map column header cells with all information
     * required for selecting and rendering the heat map
     * @param activeColumnIds
     */
    public void setActiveColumnIds(LinkedHashSet<HeatMapHeaderCellInformationBean> activeColumnIds) {
        this.activeColumnIds = activeColumnIds;
    }

    /**
     *Get list of disease group comparisons objects
     * @return diseaseComparisonSet
     */
    public Set<DiseaseGroupComparison> getDiseaseGroupComparisonsSet() {
        return diseaseComparisonSet;
    }

    /**
     *Set list of disease group comparisons objects
     * @param diseaseComparisonSet
     */
    public void setDiseaseComparisonSet(Set<DiseaseGroupComparison> diseaseComparisonSet) {
        this.diseaseComparisonSet = diseaseComparisonSet;
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
