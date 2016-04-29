package no.uib.probe.csf.pr.touch.view.components;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.VerticalLayout;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseGroup;
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
    private  HeatMapLayout heatMap;
    private boolean selfselected = false;
//    private final CSFPR_Central_Manager CSFPR_Central_Manager;
    private  int heatmapW;
//    private final Map<String, String> diseaseFullNameMap;

    /**
     *
     * @param CSFPR_Central_Manager
     * @param  mainbodyLayoutWidth mainbody layout width (the container)
     * @param mainbodyLayoutHeight mainbody layout height (the container)
     */
    public HeatMapComponent(final CSFPR_Central_Manager CSFPR_Central_Manager, int mainbodyLayoutWidth, int mainbodyLayoutHeight) {
        
        this.setWidth(mainbodyLayoutWidth,Unit.PIXELS);
        this.setHeight(mainbodyLayoutHeight,Unit.PIXELS);
        this.setMargin(true);
        
        VerticalLayout bodyLayoutWrapper = new VerticalLayout();
        bodyLayoutWrapper.setWidth(100,Unit.PERCENTAGE);
        bodyLayoutWrapper.setHeightUndefined();
        bodyLayoutWrapper.setMargin(true);
        bodyLayoutWrapper.setSpacing(true);
        
        this.addComponent(bodyLayoutWrapper);
        
        //top filters layout
        VerticalLayout topFilterContainerLayout = new VerticalLayout();
        topFilterContainerLayout.setWidth(446,Unit.PIXELS);
        topFilterContainerLayout.setHeight(30,Unit.PIXELS);
        topFilterContainerLayout.setStyleName("blacklayout");
        bodyLayoutWrapper.addComponent(topFilterContainerLayout);
        
        
        final LinkedHashSet<String> diseaseGroupsRowSet = CSFPR_Central_Manager.getSelectedHeatMapRows(); //diseaseGroupsListFilter.getDiseaseGroupsSet();
        final LinkedHashSet<String> diseaseGroupsColSet = CSFPR_Central_Manager.getSelectedHeatMapColumns(); //diseaseGroupsListFilter.getDiseaseGroupsSet();
         DiseaseGroup[] patientsGroupArr = CSFPR_Central_Manager.getDiseaseGroupsArray();
        
        //init heatmap
        
        int availableHMHeight= mainbodyLayoutHeight-40-40-150;        
         int heatmapCellWidth = Math.min(50,(availableHMHeight/40));        
        VerticalLayout heatMapContainer = new VerticalLayout();
        heatMapContainer.setHeight((heatmapCellWidth*40)+150,Unit.PIXELS);
        heatMapContainer.setWidth((heatmapCellWidth*40)+150,Unit.PIXELS);
        System.out.println("at hm width "+ heatMapContainer.getWidth()+"  "+heatMapContainer.getHeight() + "  "+heatmapCellWidth);
        heatMapContainer.setStyleName("bluelayout");
        bodyLayoutWrapper.addComponent(heatMapContainer);
        
        
        
      
//      
//       int heatmapHeaderCellWidth; 
//       Map<String, String > diseaseFullNameMap;
//       
//       
//       
//        this.setWidth(mainbodyLayoutWidth + "px");
//        this.heatmapW = mainbodyLayoutWidth;
//        this.setHeight("100%");
//        this.setStyleName(Reindeer.LAYOUT_WHITE);
//        this.CSFPR_Central_Manager = CSFPR_Central_Manager;
//
        heatMap = new HeatMapLayout(heatmapCellWidth, 150, CSFPR_Central_Manager.getDiseaseStyleMap()) {
            @Override
            public void updateSelectionManager(Set<QuantDiseaseGroupsComparison> selectedDsList) {

//                CSFPR_Central_Manager.setDiseaseGroupsComparisonSelection(selectedDsList);
            }
        };
        heatMapContainer.addComponent(heatMap);
//        this.setComponentAlignment(heatMap, Alignment.TOP_LEFT);
        this.calcHeatMapMatrix(diseaseGroupsRowSet, diseaseGroupsColSet, patientsGroupArr);
        Map<Integer, QuantDatasetObject> fullDsMap = CSFPR_Central_Manager.getFullQuantDatasetMap();
        heatMap.updateHeatMap(diseaseGroupsRowSet, diseaseGroupsColSet, values, maxDatasetNumber, diseaseFullNameMap, fullDsMap);
////        CSFPR_Central_Manager.registerFilterListener(HeatMapComponent.this);
////        CSFPR_Central_Manager.registerStudySelectionListener(HeatMapComponent.this);
//        this.diseaseFullNameMap = diseaseFullNameMap;
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
     * @param patientsGroupArr
     */
    private void updateHeatmap(LinkedHashSet<String> rowheaders, LinkedHashSet<String> colheaders, DiseaseGroup[] patientsGroupArr) {

        this.calcHeatMapMatrix(rowheaders, colheaders, patientsGroupArr);
//        Map<Integer, QuantDatasetObject> fullDsMap = CSFPR_Central_Manager.getFullQuantDatasetMap();
//        heatMap.updateHeatMap(rowheaders, colheaders, values, maxDatasetNumber, diseaseFullNameMap, fullDsMap);

    }

    private void calcHeatMapMatrix(LinkedHashSet<String> rowheaders, LinkedHashSet<String> colheaders, DiseaseGroup[] patientsGroupArr) {
        maxDatasetNumber = -1;

        values = new QuantDSIndexes[rowheaders.size()][colheaders.size()];
        for (int x = 0; x < rowheaders.size(); x++) {
            for (int y = 0; y < colheaders.size(); y++) {
                Set<Integer> value = calcDsNumbers(rowheaders.toArray()[x].toString(), colheaders.toArray()[y].toString(), patientsGroupArr);
                int z = 0;
                int[] indexes = new int[value.size()];
                for (int i : value) {
                    indexes[z] = i;
                    z++;
                }
                QuantDSIndexes qDataset = new QuantDSIndexes();
                qDataset.setValue(value.size());
                qDataset.setIndexes(indexes);
                values[x][y] = qDataset;
                if (value.size() > maxDatasetNumber) {
                    maxDatasetNumber = value.size();
                }
            }

        }
    }

    private Set<Integer> calcDsNumbers(String PGI, String PGII, DiseaseGroup[] patientsGroupArr) {
        Set<Integer> indexes = new HashSet<Integer>();
        for (DiseaseGroup pg : patientsGroupArr) {
            if (pg.checkLabel(PGI)) {
                if (pg.getValLabel(PGI).equalsIgnoreCase(PGII)) {
                    indexes.add(pg.getOriginalDatasetIndex());
                }

            }

        }
        return indexes;
    }

//    /**
//     *
//     * @param selectedRows
//     * @param selectedColumns
//     */
//    public void updateHeatmap(Set<String> selectedRows, Set<String> selectedColumns) {
//        Map<Integer, QuantDatasetObject> fullDsMap = CSFPR_Central_Manager.getFullQuantDatasetMap();
//        heatMap.updateHeatMap(selectedRows, selectedColumns, values, maxDatasetNumber, diseaseFullNameMap, fullDsMap);
//    }
    /**
     *
     * @param singleSelection
     */
    public void setSingleSelection(boolean singleSelection) {
        heatMap.setSingleSelection(singleSelection);
    }

    /**
     *
     * @param listener
     */
    public void addHideHeatmapBtnListener(LayoutEvents.LayoutClickListener listener) {
        heatMap.getHideCompBtn().addLayoutClickListener(listener);
    }

    public void updateHideComparisonThumbBtn(String imgUrl, Boolean show) {
        heatMap.updateHideShowThumbImg(imgUrl);
        heatMap.updateShowHideBtnLabel(show);

    }

    /**
     *
     * @param selectedComparisonList
     */
    public void updateCellSelection(Set<QuantDiseaseGroupsComparison> selectedComparisonList) {
        if (selectedComparisonList.isEmpty()) {
            heatMap.setVisible(true);

        }
        heatMap.updateDsCellSelection(selectedComparisonList);
    }

    /**
     *
     */
    public void selectAll() {
        heatMap.selectAll();

    }

    /**
     *
     */
    public void unselectAll() {
        heatMap.unselectAll();

    }

    /**
     *
     * @return
     */
    public boolean isActiveSelectAll() {
        return heatMap.isActiveSelectAll();
    }

    public boolean isVisibleComponent() {
        return heatMap.isVisibleComponent();
    }

    @Override
    public void setVisible(boolean visible) {
        heatMap.setVisible(visible);
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
        return heatMap.getHideCompBtn();
    }

    public void showCompBtn(boolean show) {
        heatMap.showCompBtn(show);
    }

}
