package no.uib.probe.csf.pr.touch.view.components;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseGroupComparison;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDSIndexes;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFFilter;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFPR_Central_Manager;
import no.uib.probe.csf.pr.touch.view.components.heatmapsubcomponents.HeatMapLayout;
//import probe.com.selectionmanager.QuantCentralManager;

/**
 *
 * @author Yehia Farag
 */
public class HeatMapComponent extends VerticalLayout implements CSFFilter {

    private int maxDatasetNumber;
    private QuantDSIndexes[][] values;
    private boolean selfselected = false;
    private int heatmapW;
    private final HeatMapLayout heatmapLayoutContainer;

    /**
     *
     * @param CSFPR_Central_Manager
     * @param mainbodyLayoutWidth mainbody layout width (the container)
     * @param mainbodyLayoutHeight mainbody layout height (the container)
     */
    public HeatMapComponent(final CSFPR_Central_Manager CSFPR_Central_Manager, int mainbodyLayoutWidth, int mainbodyLayoutHeight) {

        this.setWidth(mainbodyLayoutWidth, Unit.PIXELS);
        this.setHeight(mainbodyLayoutHeight, Unit.PIXELS);
        this.setMargin(false);

        VerticalLayout bodyLayoutWrapper = new VerticalLayout();
        bodyLayoutWrapper.setWidth(100, Unit.PERCENTAGE);
        bodyLayoutWrapper.setHeightUndefined();
        bodyLayoutWrapper.setMargin(false);
        bodyLayoutWrapper.setSpacing(true);

        this.addComponent(bodyLayoutWrapper);

        //top filters layout
        VerticalLayout topFilterContainerLayout = new VerticalLayout();
        topFilterContainerLayout.setWidth(446, Unit.PIXELS);
        topFilterContainerLayout.setHeight(30, Unit.PIXELS);
        topFilterContainerLayout.setStyleName("blacklayout");
        bodyLayoutWrapper.addComponent(topFilterContainerLayout);


        //init heatmap
        
        int availableHMHeight = mainbodyLayoutHeight - 100; 
        heatmapLayoutContainer = new HeatMapLayout(mainbodyLayoutWidth,availableHMHeight) {
            @Override
            public void updateSelectionManager(Set<QuantDiseaseGroupsComparison> selectedDsList) {

//                CSFPR_Central_Manager.setDiseaseGroupsComparisonSelection(selectedDsList);
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
            unselectAll();
        } else if (type.equalsIgnoreCase("Comparison_Selection")) {
//            this.updateCellSelection(CSFPR_Central_Manager.getSelectedDiseaseGroupsComparisonList());

        } else if (type.equalsIgnoreCase("Reset_Disease_Groups_Level")) {
            unselectAll();
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
     */
    public void updateData(LinkedHashSet<String> rowheaders, LinkedHashSet<String> colheaders, Set<DiseaseGroupComparison>patientsGroupComparisonsSet) {


        heatmapLayoutContainer.updateData(rowheaders, colheaders,patientsGroupComparisonsSet);

    }

   
    

//    /**
//     *
//     * @param selectedRows
//     * @param selectedColumns
//     */
//    public void updateHeatmap(Set<String> selectedRows, Set<String> selectedColumns) {
//        Map<Integer, QuantDatasetObject> fullDsMap = CSFPR_Central_Manager.getFullQuantDatasetMap();
//        heatMap.updateData(selectedRows, selectedColumns, values, maxDatasetNumber, diseaseFullNameMap, fullDsMap);
//    }
    /**
     *
     * @param singleSelection
     */
    public void setSingleSelection(boolean singleSelection) {
//        heatMap.setSingleSelection(singleSelection);
    }

    /**
     *
     * @param listener
     */
    public void addHideHeatmapBtnListener(LayoutEvents.LayoutClickListener listener) {
//        heatMap.getHideCompBtn().addLayoutClickListener(listener);
    }

    public void updateHideComparisonThumbBtn(String imgUrl, Boolean show) {
//        heatMap.updateHideShowThumbImg(imgUrl);
//        heatMap.updateShowHideBtnLabel(show);

    }

    /**
     *
     * @param selectedComparisonList
     */
    public void updateCellSelection(Set<QuantDiseaseGroupsComparison> selectedComparisonList) {
        if (selectedComparisonList.isEmpty()) {
//            heatMap.setVisible(true);

        }
//        heatMap.updateDsCellSelection(selectedComparisonList);
    }

    /**
     *
     */
    public void selectAll() {
//        heatMap.selectAll();

    }

    /**
     *
     */
    public void unselectAll() {
//        heatMap.unselectAll();

    }

    /**
     *
     * @return
     */
    public boolean isActiveSelectAll() {
//        return heatMap.isActiveSelectAll();
        return true;
    }

    public boolean isVisibleComponent() {
//        return heatMap.isVisibleComponent();
        return true;
    }

    @Override
    public void setVisible(boolean visible) {
//        heatMap.setVisible(visible);
        if (visible) {
            this.setWidth(heatmapW + "px");
            this.setHeight("100%");

        } else {

            this.setWidthUndefined();
            this.setHeightUndefined();
        }

    }

    /**
     *
     * @return
     */
    public VerticalLayout getHideCompBtn() {
//        return heatMap.getHideCompBtn();
        return null;
    }

    public void showCompBtn(boolean show) {
//        heatMap.showCompBtn(show);
    }

}
