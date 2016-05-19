package no.uib.probe.csf.pr.touch.selectionmanager;

import java.io.Serializable;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;

/**
 *
 * @author Yehia Farag Selection class is made to work together with the csf
 * central selection manager
 */
public class CSFSelection implements Serializable {

    private final String filter_Id;
    private final Set<QuantDiseaseGroupsComparison> selectedComparisonsList;
    private final String type;

    /**
     *
     * @param type
     * @param selectedDsList
     * @param filter_Id
     */
    public CSFSelection(String type, String filter_Id, Set<QuantDiseaseGroupsComparison> selectedComparisonsList) {
        this.type = type;
        this.filter_Id = filter_Id;
        this.selectedComparisonsList = selectedComparisonsList;

    }

    /**
     * get the filter id
     *
     * @return filter id
     */
    public String getFilter_Id() {
        return filter_Id;
    }

    /**
     * get selected comparisons
     *
     * @return selectedComparisonsList
     */
    public Set<QuantDiseaseGroupsComparison> getSelectedComparisonsList() {
        return selectedComparisonsList;
    }

    /**
     * get type of selection
     *
     * @return selection type
     */
    public String getType() {
        return type;
    }

}
