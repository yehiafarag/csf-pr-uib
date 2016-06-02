package no.uib.probe.csf.pr.touch.selectionmanager;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantComparisonProtein;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;

/**
 *
 * @author Yehia Farag
 *
 * This class represents the central manager that is responsible for handling
 * data across different visualizations and managing all users selections
 */
public class CSFPR_Central_Manager implements Serializable {

    private final LinkedHashMap<String, CSFListener> Listeners_Map;
    private Set<QuantDiseaseGroupsComparison> selectedComparisonsList;

    public Set<QuantComparisonProtein> getSelectedProteinsList() {
        return selectedProteinsList;
    }
    private  Set<QuantComparisonProtein> selectedProteinsList;
    private Map<QuantDiseaseGroupsComparison, QuantDiseaseGroupsComparison> equalComparisonMap;

    public CSFPR_Central_Manager() {
        Listeners_Map = new LinkedHashMap<>();
    }

    public void setEqualComparisonMap(Map<QuantDiseaseGroupsComparison, QuantDiseaseGroupsComparison> equalComparisonMap) {
        this.equalComparisonMap = equalComparisonMap;
    }

    /**
     * register new listener
     *
     * @param listener listener component
     */
    public void registerListener(CSFListener listener) {
        Listeners_Map.put(listener.getFilterId(), listener);

    }

    public Map<QuantDiseaseGroupsComparison, QuantDiseaseGroupsComparison> getEqualComparisonMap() {
        return equalComparisonMap;
    }

    /**
     * remove registered listener
     *
     * @param listener listener component
     */
    public void unregisterListener(CSFListener listener) {
        Listeners_Map.remove(listener.getFilterId(), listener);

    }

    /**
     * selection in registered component
     *
     * @param selection selection
     */
    public void selectionAction(CSFSelection selection) {
        this.selectedComparisonsList = selection.getSelectedComparisonsList();
        this.selectedProteinsList=selection.getSelectedProteinsList();
        SelectionChanged(selection.getType());

    }

    /**
     * Get current comparisons selection
     *
     * @return selected comparisons list
     */
    public Set<QuantDiseaseGroupsComparison> getSelectedComparisonsList() {
        return selectedComparisonsList;
    }

    /**
     *
     * @param type selection type
     */
    private void SelectionChanged(String type) {
        for (CSFListener filter : Listeners_Map.values()) {
            filter.selectionChanged(type);
        }

    }

}
