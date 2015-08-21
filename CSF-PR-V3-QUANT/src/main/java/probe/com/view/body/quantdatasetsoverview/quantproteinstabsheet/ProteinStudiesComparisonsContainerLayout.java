
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

import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.ComparisonProtein;
import probe.com.model.beans.quant.QuantGroupsComparison;
import probe.com.selectionmanager.DatasetExploringCentralSelectionManager;

/**
 *
 * @author Yehia Farag
 */
public class ProteinStudiesComparisonsContainerLayout extends VerticalLayout {

    private final Map<QuantGroupsComparison, ProteinComparisonScatterPlotLayout> compLayoutMap = new LinkedHashMap<QuantGroupsComparison, ProteinComparisonScatterPlotLayout>();
    private final GridLayout mainbodyLayout;

    /**
     *
     * @param proteinsComparisonsArr
     * @param selectedComparisonList
     * @param selectionManager
     * @param width
     * @param searchingMode
     */
    public ProteinStudiesComparisonsContainerLayout(ComparisonProtein[] proteinsComparisonsArr, Set<QuantGroupsComparison> selectedComparisonList, final DatasetExploringCentralSelectionManager selectionManager, int width,boolean searchingMode) {
        setStyleName(Reindeer.LAYOUT_WHITE);
        this.setWidth("100%");
        this.setHeightUndefined();
        mainbodyLayout = new GridLayout(1, selectedComparisonList.size() + 1);
        this.addComponent(mainbodyLayout);
        mainbodyLayout.setWidthUndefined();
        mainbodyLayout.setHeightUndefined();
        int rowIndex = 0;
        for (ComparisonProtein cprot : proteinsComparisonsArr) {
            if (cprot == null) {
                QuantGroupsComparison gc = (QuantGroupsComparison) selectedComparisonList.toArray()[rowIndex];
                cprot = new ComparisonProtein(width, gc, -1);
                continue;
            }
            ProteinComparisonScatterPlotLayout protCompLayout = new ProteinComparisonScatterPlotLayout(cprot, width, selectionManager,searchingMode);
            mainbodyLayout.addComponent(protCompLayout, 0, rowIndex);
            mainbodyLayout.setComponentAlignment(protCompLayout, Alignment.MIDDLE_CENTER);
            compLayoutMap.put(cprot.getComparison(), protCompLayout);

            final ComparisonProtein gc = cprot;
            LayoutEvents.LayoutClickListener closeListener = new LayoutEvents.LayoutClickListener() {

                private final QuantGroupsComparison localComparison = gc.getComparison();

                @Override
                public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                    Set<QuantGroupsComparison> selectedComparisonList = selectionManager.getSelectedComparisonList();
                    selectedComparisonList.remove(localComparison);
                    selectionManager.setComparisonSelection(selectedComparisonList);
                }
            };
            protCompLayout.getCloseBtn().addLayoutClickListener(closeListener);
            rowIndex++;
        }
    }

    /**
     *
     * @param ordComparisonProteins
     */
    public void orderComparisons(ComparisonProtein[] ordComparisonProteins) {
        mainbodyLayout.removeAllComponents();
        int rowIndex = 0;
        for (final ComparisonProtein cp : ordComparisonProteins) {
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
    public void highlightComparison(QuantGroupsComparison groupComp, boolean clicked) {
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
     * @param groupComp
     * @return
     */
    public String getComparisonChart(QuantGroupsComparison groupComp) {
        ProteinComparisonScatterPlotLayout layout = compLayoutMap.get(groupComp);
        if (layout == null) {
            return "";
        }
        return layout.getUrl();
    }

    /**
     *
     */
    public void redrawCharts() {
        for (ProteinComparisonScatterPlotLayout layout : compLayoutMap.values()) {
            layout.redrawChart();
        }
    }
}
