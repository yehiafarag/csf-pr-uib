package no.uib.probe.csf.pr.touch.view.components;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseGroupComparison;
import no.uib.probe.csf.pr.touch.logic.beans.HeatMapHeaderCellInformationBean;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFFilter;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFPR_Central_Manager;
import no.uib.probe.csf.pr.touch.view.components.datasetfilters.DatasetPieChartFiltersComponent;
import no.uib.probe.csf.pr.touch.view.components.heatmapsubcomponents.HeatMapLayout;

/**
 *
 * @author Yehia Farag
 */
public abstract class HeatMapComponent extends VerticalLayout implements CSFFilter {

    private boolean selfselected = false;
    private final HeatMapLayout heatmapLayoutContainer;
    private final DatasetPieChartFiltersComponent datasetPieChartFiltersBtn;

    /**
     *
     * @param CSFPR_Central_Manager
     * @param mainbodyLayoutWidth mainbody layout width (the container)
     * @param mainbodyLayoutHeight mainbody layout height (the container)
     * @param activeColumnHeaders boolean array of active columns for dataset
     * table export
     */
    public HeatMapComponent(final CSFPR_Central_Manager CSFPR_Central_Manager, int mainbodyLayoutWidth, int mainbodyLayoutHeight, boolean[] activeColumnHeaders) {

        this.setWidth(mainbodyLayoutWidth, Unit.PIXELS);
        this.setHeight(mainbodyLayoutHeight, Unit.PIXELS);

        VerticalLayout bodyLayoutWrapper = new VerticalLayout();
        bodyLayoutWrapper.setWidth(100, Unit.PERCENTAGE);
        bodyLayoutWrapper.setHeightUndefined();

        this.addComponent(bodyLayoutWrapper);
        this.setComponentAlignment(bodyLayoutWrapper, Alignment.TOP_CENTER);

        //top filters layout
        VerticalLayout topFilterContainerLayout = new VerticalLayout();
        topFilterContainerLayout.setWidth(446, Unit.PIXELS);
        topFilterContainerLayout.setHeight(30, Unit.PIXELS);
        bodyLayoutWrapper.addComponent(topFilterContainerLayout);
        topFilterContainerLayout.setStyleName("slowscroll");

        HorizontalLayout popupBtnsLayout = new HorizontalLayout();
        popupBtnsLayout.setWidth(100, Unit.PERCENTAGE);
        popupBtnsLayout.setHeight(100, Unit.PERCENTAGE);
        popupBtnsLayout.setSpacing(true);
        topFilterContainerLayout.addComponent(popupBtnsLayout);

        datasetPieChartFiltersBtn = new DatasetPieChartFiltersComponent();

//        DatasetPieChartFiltersComponent pieChartFiltersLayout = new DatasetPieChartFiltersComponent();
        popupBtnsLayout.addComponent(datasetPieChartFiltersBtn);
        popupBtnsLayout.setComponentAlignment(datasetPieChartFiltersBtn, Alignment.MIDDLE_LEFT);

//        
        //init heatmap
        int availableHMHeight = mainbodyLayoutHeight - 100;
        heatmapLayoutContainer = new HeatMapLayout(mainbodyLayoutWidth, availableHMHeight, activeColumnHeaders) {
            @Override
            public void updateSelectionManager(Set<QuantDiseaseGroupsComparison> selectedDsList) {
//                CSFPR_Central_Manager.setDiseaseGroupsComparisonSelection(selectedDsList);
            }

            private boolean showFilters = true;

            @Override
            public void showHideFilters() {
                if (showFilters) {
                    topFilterContainerLayout.addStyleName("hidescrolllayout");
                    topFilterContainerLayout.addStyleName("absoluteposition");
                    showFilters = false;
                } else {
                    showFilters = true;
                    topFilterContainerLayout.removeStyleName("hidescrolllayout");
                    topFilterContainerLayout.removeStyleName("absoluteposition");
                }

            }

        };
        bodyLayoutWrapper.addComponent(heatmapLayoutContainer);

    }

    /**
     *
     * @param type
     */
    @Override
    public void selectionChanged(String type) {
        if (selfselected) {
            selfselected = false;
            return;
        }
        if (type.equalsIgnoreCase("HeatMap_Update_level") || type.equalsIgnoreCase("Pie_Chart_Selection")) {
//            this.updateHeatmap(CSFPR_Central_Manager.getSelectedHeatMapRows(), CSFPR_Central_Manager.getSelectedHeatMapColumns(), CSFPR_Central_Manager.getDiseaseGroupsArray());
//            unselectAll();
        } else if (type.equalsIgnoreCase("Comparison_Selection")) {
//            this.updateCellSelection(CSFPR_Central_Manager.getSelectedDiseaseGroupsComparisonList());

        } else if (type.equalsIgnoreCase("Reset_Disease_Groups_Level")) {
//            unselectAll();
//             this.updateHeatmap(CSFPR_Central_Manager.getSelectedHeatMapRows(), CSFPR_Central_Manager.getSelectedHeatMapColumns(), CSFPR_Central_Manager.getDiseaseGroupsArray());

        }
    }

    /**
     *
     * @return
     */
    @Override
    public String getFilterId() {
        return this.getClass().getName();
    }

    /**
     *
     * @param value
     */
    @Override
    public void removeFilterValue(String value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @param rowheaders
     * @param colheaders
     * @param patientsGroupComparisonsSet
     * @param fullQuantDsMap
     */
    public void updateData(LinkedHashSet<HeatMapHeaderCellInformationBean> rowheaders, LinkedHashSet<HeatMapHeaderCellInformationBean> colheaders, Set<DiseaseGroupComparison> patientsGroupComparisonsSet, Map<Integer, QuantDatasetObject> fullQuantDsMap) {

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }

        heatmapLayoutContainer.updateData(rowheaders, colheaders, patientsGroupComparisonsSet, fullQuantDsMap);
        datasetPieChartFiltersBtn.updateQuantDatasetMap(fullQuantDsMap);
        updateIcon(heatmapLayoutContainer.getHMThumbImg());
    }

    public abstract void updateIcon(String imageUrl);

}
