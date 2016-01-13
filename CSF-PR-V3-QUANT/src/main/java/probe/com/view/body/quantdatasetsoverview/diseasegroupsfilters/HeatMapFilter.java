package probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters;

import probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.heatmap.HeatMapComponent;
import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
import probe.com.model.beans.quant.QuantDSIndexes;
import probe.com.model.beans.quant.QuantDatasetObject;
import probe.com.selectionmanager.CSFFilter;
import probe.com.selectionmanager.QuantCentralManager;
import probe.com.view.core.DiseaseGroup;

/**
 *
 * @author Yehia Farag
 */
public class HeatMapFilter extends VerticalLayout implements CSFFilter {

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
        if (type.equalsIgnoreCase("HeatMap_Update_level")|| type.equalsIgnoreCase("Pie_Chart_Selection")) {
            this.updateHeatmap(Quant_Central_Manager.getSelectedHeatMapRows(), Quant_Central_Manager.getSelectedHeatMapColumns(), Quant_Central_Manager.getDiseaseGroupsArray(),Quant_Central_Manager.getInUseDiseaseName().equalsIgnoreCase("all"));
        } else if (type.equalsIgnoreCase("Comparison_Selection")) {
            this.updateCellSelection(Quant_Central_Manager.getSelectedDiseaseGroupsComparisonList());

        }
        else if (type.equalsIgnoreCase("Reset_Disease_Groups_Level")) {
            this.updateHeatmap(Quant_Central_Manager.getSelectedHeatMapRows(), Quant_Central_Manager.getSelectedHeatMapColumns(), Quant_Central_Manager.getDiseaseGroupsArray(),Quant_Central_Manager.getInUseDiseaseName().equalsIgnoreCase("all"));
            unselectAll();

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

    private int maxDatasetNumber;
    private QuantDSIndexes[][] values;
    private final HeatMapComponent heatMap;
    private boolean selfselected = false;
    private final QuantCentralManager Quant_Central_Manager;
    private final int heatmapW;
    private final Map<String, String> diseaseFullNameMap;

    /**
     *
     * @param Quant_Central_Manager
     * @param heatmapW
     * @param rowheaders
     * @param colheaders
     * @param patientsGroupArr
     * @param heatmapCellWidth
     * @param diseaseFullNameMap
     */
    public HeatMapFilter(final QuantCentralManager Quant_Central_Manager, int heatmapW, LinkedHashSet<String> rowheaders, LinkedHashSet<String> colheaders, DiseaseGroup[] patientsGroupArr, int heatmapCellWidth, Map<String, String> diseaseFullNameMap) {

        this.setWidth(heatmapW + "px");
        this.heatmapW = heatmapW;
        this.setHeight("100%");
        this.setStyleName(Reindeer.LAYOUT_WHITE);
        this.Quant_Central_Manager = Quant_Central_Manager;

        heatMap = new HeatMapComponent(heatmapCellWidth) {
            @Override
            public void updateSelectionManager(Set<QuantDiseaseGroupsComparison> selectedDsList) {

                Quant_Central_Manager.setDiseaseGroupsComparisonSelection(selectedDsList);
            }
        };
        this.addComponent(heatMap);
        this.setComponentAlignment(heatMap, Alignment.TOP_LEFT);
        this.calcHeatMapMatrix(rowheaders, colheaders, patientsGroupArr);
        Map<Integer, QuantDatasetObject> fullDsMap = Quant_Central_Manager.getFullQuantDatasetMap();
        heatMap.updateHeatMap(rowheaders, colheaders, values, maxDatasetNumber, diseaseFullNameMap, fullDsMap,(Quant_Central_Manager.getInUseDiseaseName().equalsIgnoreCase("all")));
        Quant_Central_Manager.registerFilterListener(HeatMapFilter.this);
        Quant_Central_Manager.registerStudySelectionListener(HeatMapFilter.this);
        this.diseaseFullNameMap = diseaseFullNameMap;
    }

    /**
     *
     * @param rowheaders
     * @param colheaders
     * @param patientsGroupArr
     */
    private void updateHeatmap(LinkedHashSet<String> rowheaders, LinkedHashSet<String> colheaders, DiseaseGroup[] patientsGroupArr,boolean all) {

        this.calcHeatMapMatrix(rowheaders, colheaders, patientsGroupArr);
        Map<Integer, QuantDatasetObject> fullDsMap = Quant_Central_Manager.getFullQuantDatasetMap();
        heatMap.updateHeatMap(rowheaders, colheaders, values, maxDatasetNumber, diseaseFullNameMap, fullDsMap,all);
//        rowheaders.addAll(colheaders);

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
//        Map<Integer, QuantDatasetObject> fullDsMap = Quant_Central_Manager.getFullQuantDatasetMap();
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

}
