package probe.com.selectionmanager;

import com.vaadin.server.VaadinSession;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jfree.chart.JFreeChart;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.DiseaseGroupsComparisonsProteinLayout;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
import probe.com.model.beans.quant.QuantDatasetObject;

/**
 *
 * @author Yehia Farag
 *
 * central selection manager for quant data that is responsible for quant data
 * layout interactivity
 */
public class StudiesSelectionManager implements Serializable {

    private final Set<CSFFilter> registeredFilterSet = new LinkedHashSet<CSFFilter>();
    private QuantDatasetObject[] selectedQuantDatasetIndexes;
    private Set<QuantDiseaseGroupsComparison> selectedDiseaseGroupsComparisonList;
    private Map<String, DiseaseGroupsComparisonsProteinLayout[]> quantProteinsLayoutSelectionMap;
    private String selectedProteinKey;

//    private List<Integer> selectedDataset = null;
//    public List<Integer> getSelectedDataset() {
//        return selectedDataset;
//    }
//
//    /**
//     *
//     * @param selectedDataset
//     */
//    public void setSelectedDataset(List<Integer> selectedDataset) {
//        this.selectedDataset = selectedDataset;
//    }
    /*
     *
     */
    public StudiesSelectionManager() {
    }

    /**
     *
     * @return
     */
    public QuantDatasetObject[] getSelectedQuantDatasetIndexes() {
        return selectedQuantDatasetIndexes;
    }

    /**
     *
     * @param selectedComparisonList
     */
    public void updateSelectedComparisonList(Set<QuantDiseaseGroupsComparison> selectedComparisonList) {
        this.selectedDiseaseGroupsComparisonList = selectedComparisonList;

    }

  

    /**
     *
     * @return
     */
    public CSFFilterSelection getFilterSelection() {
        return filterSelection;
    }
    private CSFFilterSelection filterSelection;

    /**
     *
     * @param selection
     */
    public void setStudyLevelFilterSelection(CSFFilterSelection selection) {
        try {
            filterSelection = selection;
            if (selection.getType().equalsIgnoreCase("Disease_Groups_Level")) {
                this.SelectionChanged(selection.getType());
            } else {
                this.SelectionChanged("Study_Selection");
            }

        } finally {

        }

    }

    /**
     * set the selected Quant Disease Groups Comparison to the selection manager
     *
     * @param selectedDiseaseGroupsComparisonList
     */
    public void setDiseaseGroupsComparisonSelection(Set<QuantDiseaseGroupsComparison> selectedDiseaseGroupsComparisonList) {
        try {

            VaadinSession.getCurrent().getLockInstance().lock();
            this.selectedDiseaseGroupsComparisonList = selectedDiseaseGroupsComparisonList;
            this.SelectionChanged("Comparison_Selection");

        } finally {
            VaadinSession.getCurrent().getLockInstance().unlock();
        }

    }
    private boolean significantOnly = false;

    /**
     * set the selected Quant Disease Groups Comparison to the selection manager
     *
     * @param significantOnly
     */
    public void updateSignificantOnlySelection(boolean significantOnly) {
        try {

            VaadinSession.getCurrent().getLockInstance().lock();
            this.significantOnly = significantOnly;

            for (CSFFilter filter : registeredFilterSet) {
                if (!filter.getFilterId().equalsIgnoreCase("HeatMapFilter")) {
                    filter.selectionChanged("Comparison_Selection");
                }
            }

        } finally {
            VaadinSession.getCurrent().getLockInstance().unlock();
        }

    }

    /**
     * set the selected Quant proteins to the selection manager
     *
     * @param protSelectionMap
     */
    public void setQuantProteinsSelectionLayout(Map<String, DiseaseGroupsComparisonsProteinLayout[]> protSelectionMap) {

        this.quantProteinsLayoutSelectionMap = protSelectionMap;

    }

    public void selectionQuantProteinsSelectionLayoutChanged() {
        try {
            VaadinSession.getCurrent().getLockInstance().lock();
            this.SelectionChanged("Quant_Proten_Selection");

        } catch (Exception exp) {
            exp.printStackTrace();
            System.err.println("at error " + this.getClass().getName() + "  line 2261  " + exp.getLocalizedMessage());

        } finally {
            VaadinSession.getCurrent().getLockInstance().unlock();
        }

    }

    private Set<String> protSelectionSet;

    public String getSelectedComparisonHeader() {
        return selectedComparisonHeader;
    }
    private String selectedComparisonHeader;

    /**
     * set the selected Quant proteins to the selection manager
     *
     * @param protSelectionSet
     * @param selectedComparisonHeader
     */
    public void setQuantProteinsSelection(Set<String> protSelectionSet, String selectedComparisonHeader) {
        try {
            VaadinSession.getCurrent().getLockInstance().lock();
            this.protSelectionSet = protSelectionSet;
            this.selectedComparisonHeader = selectedComparisonHeader;
            this.SelectionChanged("Protens_Selection");

        } catch (Exception exp) {
            exp.printStackTrace();
            System.err.println("at error " + this.getClass().getName() + "  line 291  " + exp.getLocalizedMessage());

        } finally {
            VaadinSession.getCurrent().getLockInstance().unlock();
        }

    }

    /**
     * remove value upon un-select filter
     *
     * @param filterId
     * @param filterValue
     */
    public void removeFilterValue(String filterId, String filterValue) {
        for (CSFFilter filter : registeredFilterSet) {
            if (filter.getFilterId().equalsIgnoreCase(filterId)) {
                filter.removeFilterValue(filterValue);
                break;
            }

        }

    }

    public Set<String> getProtSelectionSet() {
        return protSelectionSet;
    }

    /**
     * update all registered filters
     */
    private void SelectionChanged(String type) {
        for (CSFFilter filter : registeredFilterSet) {
            filter.selectionChanged(type);
        }

    }

    /**
     *
     * @return map of quant proteins array that include the comparisons
     * information columns for each protein
     */
    public Map<String, DiseaseGroupsComparisonsProteinLayout[]> getQuantProteinsLayoutSelectionMap() {
//        quantProteinsLayoutSelectionMap.clear();
        return quantProteinsLayoutSelectionMap;
    }

    /**
     * register filter to the selection manager
     *
     * @param iFilter instance of CSFFilter
     */
    public void registerFilter(final CSFFilter iFilter) {
        registeredFilterSet.add(iFilter);
    }

    /**
     * get Selected disease groups comparison List
     *
     * @return
     */
    public Set<QuantDiseaseGroupsComparison> getSelectedDiseaseGroupsComparisonList() {
        return selectedDiseaseGroupsComparisonList;
    }

    /**
     * get Selected proteinKey
     *
     * @return selected Protein key
     */
    public String getSelectedProteinKey() {
        return selectedProteinKey;
    }

    public void setSelectedProteinKey(String selectedProteinKey) {
        this.selectedProteinKey = selectedProteinKey;
        this.SelectionChanged("Quant_Proten_Tab_Selection");
    }

    public boolean isSignificantOnly() {
        return significantOnly;
    }

    public Set<JFreeChart> getStudiesOverviewPieChart() {
        return studiesOverviewPieChart;
    }

    public void setStudiesOverviewPieChart(Set<JFreeChart> studiesOverviewPieChart) {
        this.studiesOverviewPieChart = studiesOverviewPieChart;
    }

    private Set<JFreeChart> studiesOverviewPieChart = new LinkedHashSet<JFreeChart>();
    private final Set<JFreeChart> proteinsOverviewBubbleChart = new LinkedHashSet<JFreeChart>();

    public Set<JFreeChart> getProteinsOverviewBubbleChart() {
        return proteinsOverviewBubbleChart;
    }

    public void setProteinsOverviewBubbleChart(JFreeChart proteinsOverviewBubbleChart) {
        this.proteinsOverviewBubbleChart.clear();
        this.proteinsOverviewBubbleChart.add(proteinsOverviewBubbleChart);
    }

}
