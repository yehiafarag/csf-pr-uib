/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.selectionmanager;

import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author Yehia Farag
 */
public class CSFFilterSelection implements Serializable {

    private final String filterId;
    private  Set<String> values;
    private boolean active;
    private int filterIndex;
    private String filterColor;
    private final String type;
    private  int[] datasetIndex;
    private Set<Integer> datasetIndexesSet;

    /**
     *
     * @param filterId
     * @param values
     * @param active
     * @param filterIndex
     * @param filterColor
     * @param type
     * @param datasetIndex
     */
    public CSFFilterSelection(String filterId, Set<String> values, boolean active, int filterIndex, String filterColor, String type, int[] datasetIndex) {
        this.filterId = filterId;
        this.values = values;
        this.active = active;
        this.filterIndex = filterIndex;
        this.filterColor = filterColor;
        this.type = type;
        this.datasetIndex = datasetIndex;

    }

    /**
     *
     * @param type
     * @param datasetIndex
     * @param filterId
     * @param values
     */
    public CSFFilterSelection(String type, int[] datasetIndex, String filterId, Set<String> values) {
        this.type = type;
        this.datasetIndex = datasetIndex;
        this.filterId = filterId;
        this.values = values;

    }
    
    /**
     *
     * @param filterId
     * @param type
     * @param datasetIndexesSet
     */
    public CSFFilterSelection(String filterId,String type, Set<Integer> datasetIndexesSet ) {
        this.type = type;
        this.datasetIndexesSet = datasetIndexesSet;
        this.filterId = filterId;

    }

    /**
     *
     * @return
     */
    public String getFilterId() {
        return filterId;
    }

    /**
     *
     * @return
     */
    public Set<Integer> getDatasetIndexesSet() {
        return datasetIndexesSet;
    }

    /**
     *
     * @return
     */
    public Set<String> getValues() {
        return values;
    }

    /**
     *
     * @return
     */
    public int[] getDatasetIndexes() {
        return datasetIndex;
    }

    /**
     *
     * @return
     */
    public boolean isActive() {
        return active;
    }

    /**
     *
     * @return
     */
    public int getFilterIndex() {
        return filterIndex;
    }

    /**
     *
     * @return
     */
    public String getType() {
        return type;
    }

}
