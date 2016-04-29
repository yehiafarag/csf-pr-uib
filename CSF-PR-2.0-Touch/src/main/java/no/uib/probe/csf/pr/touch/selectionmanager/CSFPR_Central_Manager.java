package no.uib.probe.csf.pr.touch.selectionmanager;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Map;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseGroup;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetInitialInformationObject;

/**
 *
 * @author Yehia Farag
 *
 * This class represents the central manager that is responsible for handling
 * data across different visualizations and managing all users selections
 */
public class CSFPR_Central_Manager implements Serializable {

    private final DatasetFilterManager Dataset_Filter_manager;
    private final DatasetSelectionManager Dataset_Selection_manager;

    public CSFPR_Central_Manager(Map<String, QuantDatasetInitialInformationObject> quantDatasetListObject, Map<String, boolean[]> activeFilterMap, Map<String, Map<String, String>> default_DiseaseCat_DiseaseGroupMap,Map<String, String> diseaseStyleMap) {
        Dataset_Filter_manager = new DatasetFilterManager(quantDatasetListObject, activeFilterMap, default_DiseaseCat_DiseaseGroupMap);
        Dataset_Selection_manager = new DatasetSelectionManager();
        this.diseaseStyleMap=diseaseStyleMap;

    }
    
    /**
     * get selected heat map rows
     *
     * @return set of heat map selected rows values
     */
    public LinkedHashSet<String> getSelectedHeatMapRows() {
        return Dataset_Filter_manager.getSelectedHeatMapRows();
    }
    
     /**
     * get selected heat map selected columns values
     *
     * @return set of heat map selected columns values
     */
    public LinkedHashSet<String> getSelectedHeatMapColumns() {
        return Dataset_Filter_manager.getSelectedHeatMapColumns();
    }
    
    /**
     * get elected Disease Group
     *
     * @return array of current selected Disease Group
     */
    public DiseaseGroup[] getDiseaseGroupsArray() {
        return Dataset_Filter_manager.getDiseaseGroupsArray();
    }
    private final Map<String, String> diseaseStyleMap;
     public Map<String, String> getDiseaseStyleMap() {
        return diseaseStyleMap;
    }


}
