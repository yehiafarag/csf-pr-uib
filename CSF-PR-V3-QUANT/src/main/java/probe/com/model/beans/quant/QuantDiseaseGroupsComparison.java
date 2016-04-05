/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.model.beans.quant;

import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.DiseaseGroupsComparisonsProteinLayout;
import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author Yehia Farag
 */
public class QuantDiseaseGroupsComparison implements Serializable, Comparable<QuantDiseaseGroupsComparison> {

    private String comparisonHeader;
    private String oreginalComparisonHeader;
    private int[] datasetIndexes;
    private Map<String, DiseaseGroupsComparisonsProteinLayout> comparProtsMap;
    private String rgbStringColor;
    private boolean useCustomRowHeaderToSort;
    private boolean useCustomColumnHeaderToSort;

    public void switchComparison() {
        comparisonHeader = comparisonHeader.split(" / ")[1] + " / " + comparisonHeader.split(" / ")[0];
        comparProtsMap = null;

    }

    public String getRgbStringColor() {
        return rgbStringColor;
    }

    public void setRgbStringColor(String rgbStringColor) {
        this.rgbStringColor = rgbStringColor;
    }

    /**
     *
     * @return
     */
    public String getComparisonHeader() {
        return comparisonHeader;
    }

    public String getOreginalComparisonHeader() {

        return oreginalComparisonHeader;
    }

    public void resetComparisonHeader() {
        if (!comparisonHeader.equalsIgnoreCase(oreginalComparisonHeader)) {
            comparisonHeader = oreginalComparisonHeader;
            comparProtsMap = null;
        }
        

    }

    /**
     *
     * @param comparisonHeader
     */
    public void setComparisonHeader(String comparisonHeader) {
        this.comparisonHeader = comparisonHeader;

    }

    public void setOreginalComparisonHeader(String oreginalComparisonHeader) {
        this.oreginalComparisonHeader = oreginalComparisonHeader;

    }

    /**
     *
     * @return
     */
    public int[] getDatasetIndexes() {
        return datasetIndexes;
    }

    /**
     *
     * @param datasetIndexes
     */
    public void setDatasetIndexes(int[] datasetIndexes) {
        this.datasetIndexes = datasetIndexes;
    }

    /**
     *
     * @return
     */
    public Map<String, DiseaseGroupsComparisonsProteinLayout> getComparProtsMap() {
        return comparProtsMap;
    }

    /**
     *
     * @param comparProtsMap
     */
    public void setComparProtsMap(Map<String, DiseaseGroupsComparisonsProteinLayout> comparProtsMap) {
        this.comparProtsMap = comparProtsMap;
    }

    @Override
    public int compareTo(QuantDiseaseGroupsComparison t) {
        return this.comparisonHeader.compareTo(t.comparisonHeader);
    }

    @Override
    public String toString() {

        return comparisonHeader + " -- " + comparProtsMap.size();
    }

    public boolean isUseCustomRowHeaderToSort() {
        return useCustomRowHeaderToSort;
    }

    public void setUseCustomRowHeaderToSort(boolean useCustomRowHeaderToSort) {
        this.useCustomRowHeaderToSort = useCustomRowHeaderToSort;
    }

    public boolean isUseCustomColumnHeaderToSort() {
        return useCustomColumnHeaderToSort;
    }

    public void setUseCustomColumnHeaderToSort(boolean useCustomColumnHeaderToSort) {
        this.useCustomColumnHeaderToSort = useCustomColumnHeaderToSort;
    }

}
