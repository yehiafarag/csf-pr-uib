package probe.com.selectionmanager;

import java.io.Serializable;
import java.util.Map;
import probe.com.handlers.CSFPRHandler;
import probe.com.model.beans.quant.QuantDatasetObject;
import probe.com.model.beans.quant.QuantDatasetInitialInformationObject;

/**
 *
 * @author Yehia Farag
 */
public class QuantFilterUtility implements Serializable {

    private final boolean[] activeCombinedQuantTableHeaders;
    private final boolean[] activeQuantFilters;

    private final Map<Integer, QuantDatasetObject> quantDatasetMap;

    /**
     *
     * @param quantHandler
     */
    public QuantFilterUtility(CSFPRHandler quantHandler) {
        QuantDatasetInitialInformationObject quantDatasetListObject = quantHandler.getQuantDatasetInitialInformationObject();
        if (quantDatasetListObject == null) {
            this.quantDatasetMap = null;
            activeCombinedQuantTableHeaders = null;
        } else {
            this.quantDatasetMap = quantDatasetListObject.getQuantDatasetsList(); 
            activeCombinedQuantTableHeaders = quantDatasetListObject.getActiveHeaders();
        }
      
        //which fillters are exist
        activeQuantFilters = quantHandler.getActivePieChartQuantFilters();

    }

    /**
     * active header for for data columns to display
     *
     * @return
     */
    public boolean[] getActiveCombinedQuantTableHeaders() {
        return activeCombinedQuantTableHeaders;
    }

    /**
     * get current active pie-chart filters
     *
     * @return array of active pie-chart filters
     */
    public boolean[] getActiveFilters() {
        return activeQuantFilters;
    }

    /**
     * get available quant datasets
     *
     * @return map of quant datasets with quant data set ids as keys
     */
    public Map<Integer, QuantDatasetObject> getQuantDatasetMap() {
        return quantDatasetMap;
    }

}