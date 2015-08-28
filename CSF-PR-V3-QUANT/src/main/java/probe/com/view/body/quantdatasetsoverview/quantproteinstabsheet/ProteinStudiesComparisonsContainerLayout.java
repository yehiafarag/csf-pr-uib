
package probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet;

import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.studies.ProteinComparisonScatterPlotLayout;
import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.DiseaseGroupsComparisonsProtein;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
import probe.com.selectionmanager.DatasetExploringCentralSelectionManager;

/**
 *
 * @author Yehia Farag
 */
public class ProteinStudiesComparisonsContainerLayout extends VerticalLayout {

    private final Map<QuantDiseaseGroupsComparison, ProteinComparisonScatterPlotLayout> compLayoutMap = new LinkedHashMap<QuantDiseaseGroupsComparison, ProteinComparisonScatterPlotLayout>();
    private final GridLayout mainbodyLayout;
    private final DatasetExploringCentralSelectionManager datasetExploringCentralSelectionManager;
    private final  int width;
    private final DiseaseGroupsComparisonsProtein[] diiseaseGroupsComparisonsProteinArr;
    private final Set<QuantDiseaseGroupsComparison> selectedComparisonList;
    private final boolean searchingMode;

    /**
     *
     * @param proteinsComparisonsArr
     * @param selectedComparisonList
     * @param datasetExploringCentralSelectionManager
     * @param width
     * @param searchingMode
     */
    public ProteinStudiesComparisonsContainerLayout(DiseaseGroupsComparisonsProtein[] proteinsComparisonsArr, Set<QuantDiseaseGroupsComparison> selectedComparisonList, final DatasetExploringCentralSelectionManager datasetExploringCentralSelectionManager, int width,boolean searchingMode) {
        setStyleName(Reindeer.LAYOUT_WHITE);
        this.setWidth("100%");
        this.setHeightUndefined();
        this.datasetExploringCentralSelectionManager = datasetExploringCentralSelectionManager;
        this.searchingMode=searchingMode;
        mainbodyLayout = new GridLayout(1, selectedComparisonList.size() + 1);
        this.addComponent(mainbodyLayout);
        mainbodyLayout.setWidthUndefined();
        mainbodyLayout.setHeightUndefined();
        this.width=width;
        this.diiseaseGroupsComparisonsProteinArr=proteinsComparisonsArr;
        this.selectedComparisonList=selectedComparisonList;
//        int rowIndex = 0;
//        for (DiseaseGroupsComparisonsProtein cprot : proteinsComparisonsArr) {
//            if (cprot == null) {
//                QuantDiseaseGroupsComparison gc = (QuantDiseaseGroupsComparison) selectedComparisonList.toArray()[rowIndex];
//                cprot = new DiseaseGroupsComparisonsProtein(width, gc, -1);
//                continue;
//            }
//            ProteinComparisonScatterPlotLayout protCompLayout = new ProteinComparisonScatterPlotLayout(cprot, width, datasetExploringCentralSelectionManager,searchingMode);
//            mainbodyLayout.addComponent(protCompLayout, 0, rowIndex);
//            mainbodyLayout.setComponentAlignment(protCompLayout, Alignment.MIDDLE_CENTER);
//            compLayoutMap.put(cprot.getComparison(), protCompLayout);
//
//            final DiseaseGroupsComparisonsProtein gc = cprot;
//            LayoutEvents.LayoutClickListener closeListener = new LayoutEvents.LayoutClickListener() {
//
//                private final QuantDiseaseGroupsComparison localComparison = gc.getComparison();
//
//                @Override
//                public void layoutClick(LayoutEvents.LayoutClickEvent event) {
//                    Set<QuantDiseaseGroupsComparison> selectedComparisonList = datasetExploringCentralSelectionManager.getSelectedDiseaseGroupsComparisonList();
//                    selectedComparisonList.remove(localComparison);
//                    datasetExploringCentralSelectionManager.setDiseaseGroupsComparisonSelection(selectedComparisonList);
//                }
//            };
//            protCompLayout.getCloseBtn().addLayoutClickListener(closeListener);
//            rowIndex++;
//        }
    }

    /**
     *
     * @param ordComparisonProteins
     */
    public void orderComparisons(DiseaseGroupsComparisonsProtein[] ordComparisonProteins) {
        mainbodyLayout.removeAllComponents();
        int rowIndex = 0;
        for (final DiseaseGroupsComparisonsProtein cp : ordComparisonProteins) {
            if (cp == null) {
                continue;
            }
            ProteinComparisonScatterPlotLayout protCompLayout = compLayoutMap.get(cp.getComparison());
            mainbodyLayout.addComponent(protCompLayout, 0, rowIndex);
            mainbodyLayout.setComponentAlignment(protCompLayout, Alignment.MIDDLE_CENTER);
            rowIndex++;
        }
    }
    private ProteinComparisonScatterPlotLayout lastheighlitedlayout;

    /**
     *
     * @param groupComp
     * @param clicked
     */
    public void highlightComparison(QuantDiseaseGroupsComparison groupComp, boolean clicked) {
        if (lastheighlitedlayout != null) {
            lastheighlitedlayout.highlight(false, clicked);
        }
        ProteinComparisonScatterPlotLayout layout = compLayoutMap.get(groupComp);
        if (layout == null) {
            return;
        }
        layout.highlight(true, clicked);
        lastheighlitedlayout = layout;
    }

  

    /**
     *
     */
    public void redrawCharts() {
        
        if(compLayoutMap.isEmpty()){
          int rowIndex = 0;
        for (DiseaseGroupsComparisonsProtein cprot : diiseaseGroupsComparisonsProteinArr) {
            if (cprot == null) {
                QuantDiseaseGroupsComparison gc = (QuantDiseaseGroupsComparison) selectedComparisonList.toArray()[rowIndex];
                cprot = new DiseaseGroupsComparisonsProtein(width, gc, -1);
                continue;
            }
            ProteinComparisonScatterPlotLayout protCompLayout = new ProteinComparisonScatterPlotLayout(cprot, width, datasetExploringCentralSelectionManager,searchingMode);
            mainbodyLayout.addComponent(protCompLayout, 0, rowIndex);
            mainbodyLayout.setComponentAlignment(protCompLayout, Alignment.MIDDLE_CENTER);
            compLayoutMap.put(cprot.getComparison(), protCompLayout);

            final DiseaseGroupsComparisonsProtein gc = cprot;
            LayoutEvents.LayoutClickListener closeListener = new LayoutEvents.LayoutClickListener() {

                private final QuantDiseaseGroupsComparison localComparison = gc.getComparison();

                @Override
                public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                    Set<QuantDiseaseGroupsComparison> selectedComparisonList = datasetExploringCentralSelectionManager.getSelectedDiseaseGroupsComparisonList();
                    selectedComparisonList.remove(localComparison);
                    datasetExploringCentralSelectionManager.setDiseaseGroupsComparisonSelection(selectedComparisonList);
                }
            };
            protCompLayout.getCloseBtn().addLayoutClickListener(closeListener);
            rowIndex++;
        }
        
        }
        for (ProteinComparisonScatterPlotLayout layout : compLayoutMap.values()) {
            
            layout.redrawChart();
        }
    }
}
