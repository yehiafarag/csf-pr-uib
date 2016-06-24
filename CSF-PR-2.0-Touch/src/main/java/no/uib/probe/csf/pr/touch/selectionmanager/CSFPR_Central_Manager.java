package no.uib.probe.csf.pr.touch.selectionmanager;

import com.vaadin.server.VaadinSession;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantComparisonProtein;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.view.core.BusyTask;

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
    private Set<QuantComparisonProtein> selectedProteinsList;
    private Map<QuantDiseaseGroupsComparison, QuantDiseaseGroupsComparison> equalComparisonMap;
    private String selectedProteinAccession;
    private final BusyTask busyTask;

    ;

    public CSFPR_Central_Manager(BusyTask busyTask) {
        this.busyTask = busyTask;
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

    public String getSelectedProteinAccession() {
        return selectedProteinAccession;
    }

    /**
     * quantSearchSelection in registered component
     *
     * @param selection quantSearchSelection
     */
    public void selectionAction(CSFSelection selection) {
        if (selection.getType().equalsIgnoreCase("peptide_selection")) {
            selectedProteinAccession = selection.getSelectedProteinAccession();
        } else {
            this.selectedComparisonsList = selection.getSelectedComparisonsList();
            this.selectedProteinsList = selection.getSelectedProteinsList();
            if (selectedComparisonsList.size() > 5) {
                busyTask.setVisible(true);
            }
        }
        SelectionChanged(selection.getType());

    }

    /**
     * Get current comparisons quantSearchSelection
     *
     * @return selected comparisons list
     */
    public Set<QuantDiseaseGroupsComparison> getSelectedComparisonsList() {
        return selectedComparisonsList;
    }

    /**
     *
     * @param type quantSearchSelection type
     */
    private void SelectionChanged(String type) {
        try {
            VaadinSession.getCurrent().getLockInstance().lock();
            Listeners_Map.values().stream().forEach((filter) -> {
                filter.selectionChanged(type);
            });
        } finally {
            VaadinSession.getCurrent().getLockInstance().unlock();
            busyTask.setVisible(false);
        }

    }
    
    
    private QuantSearchSelection quantSearchSelection;
    
    /**
     * quant Search Selection in registered component
     *
     * @param selection quantSearchSelection
     */
    public void searchSelectionAction(QuantSearchSelection selection) {
        this.quantSearchSelection= selection;
        SelectionChanged("quant_searching");

    }
    
    
      
    /**
     * quant Comparison Selection in registered component
     *
     * @param selection quantSearchSelection
     */
    public void compareSelectionAction(QuantSearchSelection selection) {
        this.quantSearchSelection= selection;
        SelectionChanged("quant_compare");

    }

    

    public QuantSearchSelection getQuantSearchSelection() {
        return quantSearchSelection;
    }

}
