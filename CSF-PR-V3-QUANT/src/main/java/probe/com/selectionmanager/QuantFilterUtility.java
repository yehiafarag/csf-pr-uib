package probe.com.selectionmanager;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import probe.com.handlers.CSFPRHandler;
import probe.com.model.beans.quant.QuantDatasetObject;
import probe.com.model.beans.quant.QuantDatasetInitialInformationObject;

/**
 *
 * @author Yehia Farag
 */
public class QuantFilterUtility implements Serializable {

//    private final boolean[] activeCombinedQuantTableHeaders;
//    private final boolean[] activeQuantFilters;
//    private final Set<String>  diseaseCategories;

    public Set<String> getDiseaseCategories() {
        return quantDatasetListObject.keySet();
    }

//    private final Map<Integer, QuantDatasetObject> quantDatasetMap;
    private final  Map<String, QuantDatasetInitialInformationObject> quantDatasetListObject;

    /**
     *
     * @param quantHandler
     */
    public QuantFilterUtility(CSFPRHandler quantHandler) {
           quantDatasetListObject = quantHandler.getQuantDatasetInitialInformationObject();
//        if (quantDatasetListObject == null) {
//            this.quantDatasetMap = null;
//            activeCombinedQuantTableHeaders = null;
//            diseaseCategories= null;
//        } else {
//            this.quantDatasetMap = quantDatasetListObject.getQuantDatasetsList(); 
//            activeCombinedQuantTableHeaders = quantDatasetListObject.getActiveHeaders();
//            diseaseCategories = quantDatasetListObject.getDiseaseCategories();
//        }
      
        //which fillters are exist
//        activeQuantFilters = quantHandler.getActivePieChartQuantFilters();

    }

    /**
     * active header for for data columns to display
     *
     * @param key
     * @return
     */
    public boolean[] getActiveCombinedQuantTableHeaders(String key) {
        return quantDatasetListObject.get(key).getActiveHeaders();
    }

    /**
     * get current active pie-chart filters
     *
     * @return array of active pie-chart filters
     */
//    public boolean[] getActiveFilters() {
//        return activeQuantFilters;
//    }

    /**
     * get available quant datasets
     *
     * @param key
     * @return map of quant datasets with quant data set ids as keys
     */
    public Map<Integer, QuantDatasetObject> getQuantDatasetMap(String key) {
        return quantDatasetListObject.get(key).getQuantDatasetsList();
    }

}
