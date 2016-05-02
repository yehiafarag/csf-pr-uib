package no.uib.probe.csf.pr.touch.selectionmanager;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Map;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseGroupComparison;
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

    public CSFPR_Central_Manager() {
        Dataset_Filter_manager = new DatasetFilterManager();
        Dataset_Selection_manager = new DatasetSelectionManager();

    }
    
    /**
     * get selected heat map rows
     *
     * @return set of heat map selected rows values
     */
//    public LinkedHashSet<String> getSelectedHeatMapRows() {
//        return Dataset_Filter_manager.getSelectedHeatMapRows();
//    }
    
     /**
     * get selected heat map selected columns values
     *
     * @return set of heat map selected columns values
     */
//    public LinkedHashSet<String> getSelectedHeatMapColumns() {
//        return Dataset_Filter_manager.getSelectedHeatMapColumns();
//    }
//    
    /**
     * get selected Disease Group
     *
     * @return array of current selected Disease Group
     */
//    public DiseaseGroupComparison[] getDiseaseGroupsArray() {
//        return Dataset_Filter_manager.getDiseaseGroupsArray();
//    }
//    private final Map<String, String> diseaseStyleMap;
//     public Map<String, String> getDiseaseStyleMap() {
//        return diseaseStyleMap;
//    }


}
