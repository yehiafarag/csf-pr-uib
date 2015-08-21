/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.model.beans.quant;

import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.ComparisonProtein;
import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author Yehia Farag
 */
public class QuantGroupsComparison implements Serializable, Comparable<QuantGroupsComparison>{
    private String comparisonHeader;
    private int[] datasetIndexes;
    private Map<String, ComparisonProtein> comparProtsMap;

    /**
     *
     * @return
     */
    public String getComparisonHeader() {
        return comparisonHeader;
    }

    /**
     *
     * @param comparisonHeader
     */
    public void setComparisonHeader(String comparisonHeader) {
        this.comparisonHeader = comparisonHeader;
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
    public Map<String, ComparisonProtein>  getComparProtsMap() {
        return comparProtsMap;
    }

    /**
     *
     * @param comparProtsMap
     */
    public void setComparProtsMap(Map<String, ComparisonProtein>  comparProtsMap) {
        this.comparProtsMap = comparProtsMap;
    }

    @Override
    public int compareTo(QuantGroupsComparison t) {
        return this.comparisonHeader.compareTo(t.comparisonHeader);
    }

   
    
}
