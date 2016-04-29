package no.uib.probe.csf.pr.touch.selectionmanager;

import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author Yehia Farag Selection class is made to work together with the csf
 * central selection manager
 */
public class CSFFilterSelection implements Serializable {

    private final String filterId;
    private final Set<String> values;
    private int filterIndex;
    private final String type;
    private final int[] datasetIndex;

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
     * get the filter id
     *
     * @return filter id
     */
    public String getFilterId() {
        return filterId;
    }

    /**
     * get selected filter values
     * @return set of selected values
     */
    public Set<String> getValues() {
        return values;
    }

     /**
     * get the selected quant dataset indexes
     *
     * @return array of selected quant dataset ids 
     */
    public int[] getDatasetIndexes() {
        return datasetIndex;
    }

     /**
     * the index of the filter in the standard  order
     * @return filter index
     */
    public int getFilterIndex() {
        return filterIndex;
    }

    /**
     * get type of selection 
     * 1. Disease_Groups_Level 
     * 2. Comparison_Filter
     * 3. DS_Selection
     * 4. Study_Selection
     * 5. Filter
     * @return selection type
     */
    public String getType() {
        return type;
    }

}
