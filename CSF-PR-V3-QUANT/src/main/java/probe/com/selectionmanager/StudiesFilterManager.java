/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.selectionmanager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.jfree.chart.JFreeChart;
import probe.com.model.beans.quant.QuantDatasetInitialInformationObject;
import probe.com.model.beans.quant.QuantDatasetObject;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
import probe.com.view.core.DiseaseGroup;

/**
 *
 * @author Yehia Farag
 *
 * This class responsible for quant studies filters interactivity
 */
public class StudiesFilterManager implements Serializable {

    private boolean[] activeHeader;
    private final Map<String, QuantDatasetInitialInformationObject> quantDatasetListObject;
    private int totalDsNumber, currentDsNumber;
    private Map<Integer, QuantDatasetObject> inUsefullQuantDatasetMap;
    private Map<Integer, QuantDatasetObject> noSerumQuantDatasetMap;
    private Map<String, Map<Integer, QuantDatasetObject>> noSerumDiseaseCategory;
    private Map<Integer, QuantDatasetObject> fullQuantDatasetMap;
    private final Map<String, boolean[]> activeFilterMap;
    private boolean[] activeFilters;
    private Map<Integer, QuantDatasetObject> filteredQuantDatasetArr = new LinkedHashMap<Integer, QuantDatasetObject>();
    private Set<String> diseaseCategorySet;
    private final LinkedHashMap<String, CSFFilter> registeredFilterSet = new LinkedHashMap<String, CSFFilter>();

    private final Map<String, Map<String, String>> default_DiseaseCat_DiseaseGroupMap;
    private final Map<String, Map<String, String>> inuse_DiseaseCat_DiseaseGroupMap;

    private CSFFilterSelection filterSelection;
//    private LinkedHashSet<String> selectedHeatMapRows;
//    private LinkedHashSet<String> selectedHeatMapColumns;
//    private DiseaseGroup[] diseaseGroupsArr;
    private final LinkedHashSet<String> selectedHeatMapRows = new LinkedHashSet<String>();
    private final LinkedHashSet<String> selectedHeatMapColumns = new LinkedHashSet<String>();
    private String[] diseaseGroupsI, diseaseGroupsII;
    private final Map<Integer, DiseaseGroup> fullDiseaseGroupMap;
    private final Map<Integer, DiseaseGroup> selectedDiseaseGroupMap;
    private final Map<String, Set<String>> diseaseGroupsHeaderToOregenalDiseaseGroupsNames = new LinkedHashMap<String, Set<String>>();

    public boolean isNoSerum() {
        return noSerum;
    }

    public DiseaseGroup[] getDiseaseGroupsArray() {
        DiseaseGroup[] diseaseGroupArr = new DiseaseGroup[selectedDiseaseGroupMap.size()];
        int indexer = 0;
        for (DiseaseGroup dg : selectedDiseaseGroupMap.values()) {

            diseaseGroupArr[indexer++] = dg;
        }
        currentDsNumber = diseaseGroupArr.length;
        return diseaseGroupArr;
    }

    public void setNoSerum(boolean noSerum) {
        this.noSerum = noSerum;
        if (noSerum) {
            inUsefullQuantDatasetMap = noSerumQuantDatasetMap;

        } else {
            inUsefullQuantDatasetMap = fullQuantDatasetMap;
        }
        this.currentDsNumber = inUsefullQuantDatasetMap.size();
        this.updateFilteredList();

    }

    private void updateFilteredList() {
        Map<Integer, QuantDatasetObject> updatedFilteredQuantDatasetArr = new LinkedHashMap<Integer, QuantDatasetObject>();

        for (int i : filteredQuantDatasetArr.keySet()) {
            if (inUsefullQuantDatasetMap.containsKey(i)) {
                updatedFilteredQuantDatasetArr.put(i, filteredQuantDatasetArr.get(i));
            }

        }
        filteredQuantDatasetArr.clear();
        filteredQuantDatasetArr.putAll(updatedFilteredQuantDatasetArr);

    }
    private boolean noSerum;

    private Set<JFreeChart> studiesOverviewPieChart = new LinkedHashSet<JFreeChart>();

    public StudiesFilterManager(Map<String, QuantDatasetInitialInformationObject> quantDatasetListObject, Map<String, boolean[]> activeFilterMap, Map<String, Map<String, String>> default_DiseaseCat_DiseaseGroupMap) {

        this.default_DiseaseCat_DiseaseGroupMap = default_DiseaseCat_DiseaseGroupMap;
        this.fullDiseaseGroupMap = new LinkedHashMap<Integer, DiseaseGroup>();
        this.selectedDiseaseGroupMap = new LinkedHashMap<Integer, DiseaseGroup>();
        this.quantDatasetListObject = quantDatasetListObject;

        String key = "Multiple Sclerosis";//quantDatasetListObject.keySet().iterator().next();
        this.totalDsNumber = quantDatasetListObject.get("All").getQuantDatasetsList().size();

        noSerumDiseaseCategory = new HashMap<String, Map<Integer, QuantDatasetObject>>();
        for (String diseaseCat : quantDatasetListObject.keySet()) {
            noSerumQuantDatasetMap = new HashMap<Integer, QuantDatasetObject>();
            this.fullQuantDatasetMap = quantDatasetListObject.get(diseaseCat).getQuantDatasetsList();
            for (int intKey : fullQuantDatasetMap.keySet()) {

                if (!fullQuantDatasetMap.get(intKey).getSampleType().equalsIgnoreCase("Serum")) {
                    noSerumQuantDatasetMap.put(intKey, fullQuantDatasetMap.get(intKey));

                }

            }
            noSerumDiseaseCategory.put(diseaseCat, noSerumQuantDatasetMap);

        }
        inUseDiseaseName = key;
        noSerum = true;

        this.fullQuantDatasetMap = quantDatasetListObject.get(key).getQuantDatasetsList();
        this.noSerumQuantDatasetMap = noSerumDiseaseCategory.get(key);
        this.inUsefullQuantDatasetMap = this.noSerumQuantDatasetMap;
        this.currentDsNumber = inUsefullQuantDatasetMap.size();
        this.activeFilterMap = activeFilterMap;
        this.activeFilters = activeFilterMap.get(key);
        this.activeHeader = quantDatasetListObject.get(key).getActiveHeaders();
        this.diseaseCategorySet = quantDatasetListObject.keySet();

        inuse_DiseaseCat_DiseaseGroupMap = new LinkedHashMap<String, Map<String, String>>(default_DiseaseCat_DiseaseGroupMap);
        this.updateRowsAndColumns("Reset_Disease_Groups_Level");

    }
    private String userDiseaseGroupA = "VeryHårdToExistByChanceøæå", userDiseaseGroupB = "VeryHårdToExistByChanceøæå";

    public StudiesFilterManager(Map<String, QuantDatasetInitialInformationObject> quantDatasetListObject, Map<String, boolean[]> activeFilterMap, QuantDiseaseGroupsComparison userCustomizedComparison, Map<String, Map<String, String>> default_DiseaseCat_DiseaseGroupMap) {

        this.default_DiseaseCat_DiseaseGroupMap = default_DiseaseCat_DiseaseGroupMap;
        this.fullDiseaseGroupMap = new LinkedHashMap<Integer, DiseaseGroup>();
        this.selectedDiseaseGroupMap = new LinkedHashMap<Integer, DiseaseGroup>();
        this.quantDatasetListObject = quantDatasetListObject;

        String key = "Multiple Sclerosis";//quantDatasetListObject.keySet().iterator().next();
        this.totalDsNumber = quantDatasetListObject.get("All").getQuantDatasetsList().size();

        noSerumDiseaseCategory = new HashMap<String, Map<Integer, QuantDatasetObject>>();
        for (String diseaseCat : quantDatasetListObject.keySet()) {
            noSerumQuantDatasetMap = new HashMap<Integer, QuantDatasetObject>();
            this.fullQuantDatasetMap = quantDatasetListObject.get(diseaseCat).getQuantDatasetsList();
            for (int intKey : fullQuantDatasetMap.keySet()) {

                if (!fullQuantDatasetMap.get(intKey).getSampleType().equalsIgnoreCase("Serum")) {
                    noSerumQuantDatasetMap.put(intKey, fullQuantDatasetMap.get(intKey));

                }

            }
            noSerumDiseaseCategory.put(diseaseCat, noSerumQuantDatasetMap);

        }
        inUseDiseaseName = key;
        noSerum = true;

        this.fullQuantDatasetMap = quantDatasetListObject.get(key).getQuantDatasetsList();
        this.noSerumQuantDatasetMap = noSerumDiseaseCategory.get(key);
        this.inUsefullQuantDatasetMap = this.noSerumQuantDatasetMap;
        this.currentDsNumber = inUsefullQuantDatasetMap.size();
        this.activeFilterMap = activeFilterMap;
        this.activeFilters = activeFilterMap.get(key);
        this.activeHeader = quantDatasetListObject.get(key).getActiveHeaders();
        this.diseaseCategorySet = quantDatasetListObject.keySet();
        if (userCustomizedComparison.isUseCustomRowHeaderToSort()) {
            userDiseaseGroupA = userCustomizedComparison.getComparisonHeader().split(" / ")[0].replace("User Data - ", "").trim();
        }
        if (userCustomizedComparison.isUseCustomColumnHeaderToSort()) {
            userDiseaseGroupB = userCustomizedComparison.getComparisonHeader().split(" / ")[1].replace("\n", "").trim();
        }

        inuse_DiseaseCat_DiseaseGroupMap = new LinkedHashMap<String, Map<String, String>>(default_DiseaseCat_DiseaseGroupMap);
        this.updateRowsAndColumns("Reset_Disease_Groups_Level");

    }

    private void updateRowsAndColumns(String type) {
        if (type.equalsIgnoreCase("Disease_Groups_Level")) {
            this.updateDiseaseGroups(getFilteredDatasetsList());

            if (diseaseGroupsI == null || diseaseGroupsII == null) {
                System.out.println("error at 85 class " + this.getClass().getName() + "   ");
                return;
            }
            String[] pgArr = merge(diseaseGroupsI, diseaseGroupsII);

            selectedHeatMapRows.clear();
            for (String str : pgArr) {
                if (!str.equalsIgnoreCase("") && !str.contains(userDiseaseGroupB)) {
                    selectedHeatMapRows.add(str);
                }
            }
            selectedHeatMapColumns.clear();
            for (String str : pgArr) {
                if (!str.equalsIgnoreCase("") && !str.contains(userDiseaseGroupA)) {
                    selectedHeatMapColumns.add(str);
                }
            }

        } else if (type.equalsIgnoreCase("Reset_Disease_Groups_Level")) {
            this.updateDiseaseGroups(getFullQuantDatasetMap());

            String[] pgArr = merge(diseaseGroupsI, diseaseGroupsII);
//            Arrays.sort(pgArr);
            selectedHeatMapRows.clear();
            for (String str : pgArr) {
                if (!str.equalsIgnoreCase("") && !str.contains(userDiseaseGroupB)) {
                    selectedHeatMapRows.add(str);
                }
            }
            selectedHeatMapColumns.clear();
            for (String str : pgArr) {
                if (!str.equalsIgnoreCase("") && !str.contains(userDiseaseGroupA)) {
                    selectedHeatMapColumns.add(str);
                }
            }

//            selectedHeatMapColumns.clear();
//            selectedHeatMapColumns.addAll(selectedHeatMapRows);
        }

    }

    private void updateDiseaseGroups(Map<Integer, QuantDatasetObject> quantDSArr) {

        diseaseGroupsI = new String[quantDSArr.size()];
        diseaseGroupsII = new String[quantDSArr.size()];
        fullDiseaseGroupMap.clear();
        diseaseGroupsHeaderToOregenalDiseaseGroupsNames.clear();
        int i = 0;
        for (QuantDatasetObject ds : quantDSArr.values()) {
            if (ds == null) {
                continue;
            }

            Map<String, String> inuseRegroupName = inuse_DiseaseCat_DiseaseGroupMap.get(ds.getDiseaseCategory());
            DiseaseGroup diseaseGroup = new DiseaseGroup();
            String pgI = ds.getPatientsGroup1();
            diseaseGroup.setPatientsGroupI(pgI);

            String label1;
            if (pgI.equalsIgnoreCase("Not Available") || pgI.equalsIgnoreCase("control")) {
                pgI = "";
            }
            if (inuseRegroupName.get(ds.getPatientsSubGroup1().split("\n")[0].trim()) == null) {
                inuseRegroupName.put(ds.getPatientsSubGroup1().split("\n")[0].trim(), ds.getPatientsSubGroup1().split("\n")[0].trim());

            }
            diseaseGroup.setOriginalDiseaseSubGroupI(ds.getPatientsSubGroup1());
            String subpgI = ds.getPatientsSubGroup1().replace(ds.getPatientsSubGroup1().split("\n")[0], inuseRegroupName.get(ds.getPatientsSubGroup1().split("\n")[0].trim()));//+"\n"+ds.getPatientsSubGroup1().split("\n")[1].trim();
            diseaseGroup.setPatientsSubGroupI(subpgI);
            if (!subpgI.equalsIgnoreCase("") && !subpgI.equalsIgnoreCase("Not Available")) {
                pgI = subpgI;
            }
            label1 = pgI;
            diseaseGroup.setPatientsGroupILabel(label1);

            String pgII = ds.getPatientsGroup2();
            diseaseGroup.setPatientsGroupII(pgII);
            String label2;
            if (pgII.equalsIgnoreCase("Not Available") || pgII.equalsIgnoreCase("control")) {
                pgII = "";
            }

            if (inuseRegroupName.get(ds.getPatientsSubGroup2().split("\n")[0].trim()) == null) {
                System.out.println("at ds.getPatientsSubGroup2() " + ds.getPatientsSubGroup2().split("\n")[0].trim() + "" + inuseRegroupName + "  ");
                inuseRegroupName.put(ds.getPatientsSubGroup2().split("\n")[0].trim(), ds.getPatientsSubGroup2().split("\n")[0].trim());

            }
            diseaseGroup.setOriginalDiseaseSubGroupII(ds.getPatientsSubGroup2());
            String subpgII = ds.getPatientsSubGroup2().replace(ds.getPatientsSubGroup2().split("\n")[0], inuseRegroupName.get(ds.getPatientsSubGroup2().split("\n")[0].trim()));
            diseaseGroup.setPatientsSubGroupII(subpgII);
            if (!subpgII.equalsIgnoreCase("") && !subpgII.equalsIgnoreCase("Not Available")) {
                pgII = subpgII;
            }
            label2 = pgII;
            diseaseGroup.setPatientsGroupIILabel(label2);
            fullDiseaseGroupMap.put(ds.getDsKey(), diseaseGroup);
            if (!diseaseGroupsHeaderToOregenalDiseaseGroupsNames.containsKey(diseaseGroup.getPatientsSubGroupI())) //            fullDiseaseGroupMap[i] = diseaseGroup;
            {
                diseaseGroupsHeaderToOregenalDiseaseGroupsNames.put(diseaseGroup.getPatientsSubGroupI(), new LinkedHashSet<String>());

            }
            if (!diseaseGroupsHeaderToOregenalDiseaseGroupsNames.containsKey(diseaseGroup.getPatientsSubGroupII())) //            fullDiseaseGroupMap[i] = diseaseGroup;
            {
                diseaseGroupsHeaderToOregenalDiseaseGroupsNames.put(diseaseGroup.getPatientsSubGroupII(), new LinkedHashSet<String>());

            }
            Set<String> groupsNamesSet = diseaseGroupsHeaderToOregenalDiseaseGroupsNames.get(diseaseGroup.getPatientsSubGroupI());
            groupsNamesSet.add(ds.getPatientsSubGroup1());
            diseaseGroupsHeaderToOregenalDiseaseGroupsNames.put(diseaseGroup.getPatientsSubGroupI(), groupsNamesSet);
            groupsNamesSet = diseaseGroupsHeaderToOregenalDiseaseGroupsNames.get(diseaseGroup.getPatientsSubGroupII());
            groupsNamesSet.add(ds.getPatientsSubGroup2());
            diseaseGroupsHeaderToOregenalDiseaseGroupsNames.put(diseaseGroup.getPatientsSubGroupII(), groupsNamesSet);

            diseaseGroupsI[i] = label1;
            diseaseGroupsII[i] = label2;
            diseaseGroup.setQuantDatasetIndex(ds.getDsKey());
            diseaseGroup.setOriginalDatasetIndex(ds.getDsKey());

            i++;
        }
        selectedDiseaseGroupMap.clear();
        selectedDiseaseGroupMap.putAll(fullDiseaseGroupMap);

    }

    private String[] merge(String[] arr1, String[] arr2) {
        String[] newArr = new String[arr1.length + arr2.length];

        int i = 0;
        for (String str : arr1) {
            newArr[i] = str;
            i++;
        }
        for (String str : arr2) {
            newArr[i] = str;
            i++;
        }

        Arrays.sort(newArr);

        Map<String, ArrayList<String>> sortOnGroupMap = new LinkedHashMap<String, ArrayList<String>>();
        for (int x = newArr.length - 1; x >= 0; x--) {
            String s = newArr[x];
            String diseaseCat = s.split("\n")[1];
            if (!sortOnGroupMap.containsKey(diseaseCat)) {
                sortOnGroupMap.put(diseaseCat, new ArrayList<String>());
            }
            ArrayList<String> set = sortOnGroupMap.get(diseaseCat);
            set.add(s);
            sortOnGroupMap.put(diseaseCat, set);

        }
        String[] sortedArr = new String[newArr.length];
        int index = 0;
        for (String diseaseCat : sortOnGroupMap.keySet()) {
            ArrayList<String> set = sortOnGroupMap.get(diseaseCat);
            Collections.sort(set);
            for (String fullName : set) {
                sortedArr[index] = fullName;
                index++;

            }
        }

        return sortedArr;

    }

    public Map<String, Set<String>> getDiseaseGroupsHeaderToOregenalDiseaseGroupsNames() {
        return diseaseGroupsHeaderToOregenalDiseaseGroupsNames;
    }

    public boolean[] getActiveHeader() {
        return activeHeader;
    }

    public Map<String, Map<String, String>> getDefault_DiseaseCat_DiseaseGroupMap() {
        return inuse_DiseaseCat_DiseaseGroupMap;
    }

    /**
     * update the current filtered dataset indexes
     *
     * @param datasetIndexes
     */
    private void updateFilteredDatasetList(int[] datasetIndexes) {

        if (datasetIndexes.length == 0) {
            filteredQuantDatasetArr = inUsefullQuantDatasetMap;
            return;
        }
        resetHeatmapRowsAndColumn();
        filteredQuantDatasetArr.clear();
        Set<String> tColLab = new HashSet<String>();
        Set<String> tRowLab = new HashSet<String>();
        selectedDiseaseGroupMap.clear();

        for (int i : datasetIndexes) {

            QuantDatasetObject quantDS = inUsefullQuantDatasetMap.get(i);
            filteredQuantDatasetArr.put(i, quantDS);

            if (fullDiseaseGroupMap.containsKey(i)) {
                DiseaseGroup dg = fullDiseaseGroupMap.get(i);
                tColLab.add(dg.getPatientsSubGroupI());
                tColLab.add(dg.getPatientsSubGroupII());
                tRowLab.add(dg.getPatientsSubGroupI());
                tRowLab.add(dg.getPatientsSubGroupII());
                selectedDiseaseGroupMap.put(i, dg);
            }

        }

        LinkedHashSet<String> tSelectedColLab = new LinkedHashSet<String>();
        LinkedHashSet<String> tSelectedRowLab = new LinkedHashSet<String>();
        for (String str : selectedHeatMapRows) {
            if (tRowLab.contains(str) && !str.contains(userDiseaseGroupB)) {
                tSelectedRowLab.add(str);

            }

        }

        for (String str : selectedHeatMapColumns) {
            if (tColLab.contains(str) && !str.contains(userDiseaseGroupA)) {
                tSelectedColLab.add(str);
            }

        }

        selectedHeatMapColumns.clear();
        selectedHeatMapColumns.addAll(tSelectedColLab);
        selectedHeatMapRows.clear();
        selectedHeatMapRows.addAll(tSelectedRowLab);

    }

    /**
     *
     * @return
     */
    public Map<Integer, QuantDatasetObject> getFilteredQuantDatasetArr() {
        return filteredQuantDatasetArr;
    }

    public Set<String> getDiseaseCategorySet() {
        return diseaseCategorySet;
    }

    /**
     *
     * @param selection
     */
    public void applyFilters(CSFFilterSelection selection) {
        filterSelection = selection;
        updateFilteredDatasetList(selection.getDatasetIndexes());
        this.updateDiseaseGroups(getFilteredDatasetsList());
        this.updateDiseaseGroups(getFullQuantDatasetMap());
        this.SelectionChanged(selection.getType());

//        }
    }

    /**
     *
     * @return
     */
    public CSFFilterSelection getFilterSelection() {
        return filterSelection;
    }

    /**
     * reset all disease groups filters
     */
    public void resetFilters() {
        filteredQuantDatasetArr.clear();
        this.updateDiseaseGroups(getFullQuantDatasetMap());
        resetHeatmapRowsAndColumn();
        this.SelectionChanged("Reset_Disease_Groups_Level");

    }

    private void resetHeatmapRowsAndColumn() {

        String[] pgArr = merge(diseaseGroupsI, diseaseGroupsII);
        selectedHeatMapRows.clear();
        for (String str : pgArr) {
            if (!str.equalsIgnoreCase("")) {
                selectedHeatMapRows.add(str);
            }
        }
        selectedHeatMapColumns.clear();
        selectedHeatMapColumns.addAll(selectedHeatMapRows);

    }

    /**
     * get current active quant filters
     *
     * @return
     */
    public boolean[] getActiveFilters() {
        return activeFilters;
    }

    /**
     * update all registered filters
     */
    private void SelectionChanged(String type) {
        this.updateRowsAndColumns(type);
        for (CSFFilter filter : registeredFilterSet.values()) {
            filter.selectionChanged(type);
        }

    }

    /**
     * get the current filtered quant dataset list
     *
     * @return
     */
    public Map<Integer, QuantDatasetObject> getFilteredDatasetsList() {
        if (filteredQuantDatasetArr == null || filteredQuantDatasetArr.isEmpty()) {
            return inUsefullQuantDatasetMap;
        }
        return filteredQuantDatasetArr;
    }

    /**
     * get all quant dataset list available
     *
     * @return
     */
    public Map<Integer, QuantDatasetObject> getFullQuantDatasetMap() {
        return inUsefullQuantDatasetMap;
    }

    private String inUseDiseaseName;

    public String getInUseDiseaseName() {
        return inUseDiseaseName;
    }

    public void changeDiseaseCategory(String diseaseCategory) {
        this.fullQuantDatasetMap = quantDatasetListObject.get(diseaseCategory).getQuantDatasetsList();
        this.filteredQuantDatasetArr.clear();
        this.diseaseCategorySet = quantDatasetListObject.get(diseaseCategory).getDiseaseCategories();
        this.activeHeader = quantDatasetListObject.get(diseaseCategory).getActiveHeaders();
        this.activeFilters = activeFilterMap.get(diseaseCategory);
        this.currentDsNumber = inUsefullQuantDatasetMap.size();
        inUseDiseaseName = diseaseCategory;
        this.noSerumQuantDatasetMap = noSerumDiseaseCategory.get(diseaseCategory);
        setNoSerum(noSerum);
//        this.SelectionChanged("Disease_Category_Selection");

    }

    public void updateDiseaseGroupsNames(Map<String, Map<String, String>> updatedGroupsNamesMap) {
        inuse_DiseaseCat_DiseaseGroupMap.clear();
        inuse_DiseaseCat_DiseaseGroupMap.putAll(updatedGroupsNamesMap);
        this.updateRowsAndColumns("Reset_Disease_Groups_Level");
//          this.SelectionChanged("Reorder_Selection");
//        System.out.println("update updateDiseaseGroupsNames");
        this.resetFilters();

    }

    public int getTotalDsNumber() {
        return totalDsNumber;
    }

    public int getCurrentDsNumber() {
        return currentDsNumber;
    }

    /**
     * register filter to the selection manager
     *
     * @param iFilter instance of CSFFilter
     */
    public void registerFilter(final CSFFilter iFilter) {
        registeredFilterSet.put(iFilter.getFilterId(), iFilter);
    }

    /**
     *
     * @param selectedRows
     * @param selectedColumns
     * @param diseaseGroupsArr
     */
    public void setHeatMapLevelSelection(LinkedHashSet<String> selectedRows, LinkedHashSet<String> selectedColumns, DiseaseGroup[] diseaseGroupsArr) {
//        this.fullDiseaseGroupMap = diseaseGroupsArr;
        fullDiseaseGroupMap.clear();
//        int indexer = 0;
        for (DiseaseGroup dg : diseaseGroupsArr) {
            fullDiseaseGroupMap.put(dg.getQuantDatasetIndex(), dg);
        }
        this.selectedHeatMapRows.clear();
        this.selectedHeatMapRows.addAll(selectedRows);
        this.selectedHeatMapColumns.clear();
        this.selectedHeatMapColumns.addAll(selectedColumns);
        this.currentDsNumber = fullDiseaseGroupMap.size();

        this.updateDiseaseGroups(getFilteredDatasetsList());
        this.updateDiseaseGroups(getFullQuantDatasetMap());

        this.SelectionChanged("HeatMap_Update_level");

    }

    /**
     * get selected heat map rows
     *
     * @return set of heat map selected rows values
     */
    public LinkedHashSet<String> getSelectedHeatMapRows() {
        return selectedHeatMapRows;
    }

    /**
     * get selected heat map selected columns values
     *
     * @return set of heat map selected columns values
     */
    public LinkedHashSet<String> getSelectedHeatMapColumns() {
        return selectedHeatMapColumns;
    }

    /**
     * get elected Disease Group
     *
     * @return array of current selected Disease Group
     */
//    public DiseaseGroup[] getDiseaseGroupsArr() {
//        return diseaseGroupsArr;
//    }
    public Set<JFreeChart> getStudiesOverviewPieChart() {
        return studiesOverviewPieChart;
    }

    public void setStudiesOverviewPieChart(Set<JFreeChart> studiesOverviewPieChart) {
        this.studiesOverviewPieChart = studiesOverviewPieChart;
    }

    /**
     * set selected heat map selected Rows values
     *
     * @param selectedHeatMapRows set of heat map selected rows values
     *
     */
    public void setSelectedHeatMapRows(LinkedHashSet<String> selectedHeatMapRows) {
        this.selectedHeatMapRows.clear();
        this.selectedHeatMapRows.addAll(selectedHeatMapRows);
    }

    /**
     * set selected heat map selected columns values
     *
     * @param selectedHeatMapColumns set of heat map selected columns values
     */
    public void setSelectedHeatMapColumns(LinkedHashSet<String> selectedHeatMapColumns) {
        this.selectedHeatMapColumns.clear();
        this.selectedHeatMapColumns.addAll(selectedHeatMapColumns);
    }

}
