package no.uib.probe.csf.pr.touch.logic.dataset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import no.uib.probe.csf.pr.touch.logic.CoreLogic;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseCategoryObject;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseGroupComparison;
import no.uib.probe.csf.pr.touch.logic.beans.HeatMapHeaderCellInformationBean;
import no.uib.probe.csf.pr.touch.logic.beans.InitialInformationObject;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDataset;
import no.uib.probe.csf.pr.touch.selectionmanager.QuantSearchSelection;

/**
 * This class responsible for maintaining the active dataset do all calculations
 * this class interact with Data_Handler and CoreLogic.
 *
 * @author Yehia Farag
 */
public class DatasetUtility implements Serializable {

    /**
     * Map of disease category (MS,AD,PD..etc)to quant data included for each of
     * these diseases.
     */
    private final Map<String, QuantData> quantDataMap;
    /**
     * Map of disease category (MS,AD,PD..etc)to DiseaseCategoryObject that has
     * all disease category information.
     */
    private final LinkedHashMap<String, DiseaseCategoryObject> fullDiseaseCategoryMap;
    /**
     * Map of disease category (MS,AD,PD..etc)to initial information object that
     * has initial information for each disease category.
     */
    private final Map<String, InitialInformationObject> quantDatasetInitialInformationObject;
    /**
     * Map of disease category (MS,AD,PD..etc)to the initial disease categories
     * updated by CSF-PR 2.0 administrator.
     */
    private Map<String, Map<String, String>> default_DiseaseCat_DiseaseGroupMap;
    /**
     * The current used map of disease category (MS,AD,PD..etc) information.
     */
    private Map<String, Map<String, String>> inUse_DiseaseCat_DiseaseGroupMap;
    /**
     * Map of disease category (MS,AD,PD..etc)to original publication
     * information.
     */
    private Map<String, Map<String, String>> oreginal_DiseaseCat_DiseaseGroupMap;
    /**
     * Disease group A created with random word.
     */
    private final String userDiseaseGroupA = "VeryHårdToExistByChanceøæå";
    /**
     * Disease group B created with random word.
     */
    private final String userDiseaseGroupB = "VeryHårdToExistByChanceøæå";
    /**
     * Map of short to full disease sub-group names map.
     */
    private final Map<String, String> diseaseGroupFullNameMap;
    /**
     * Re-indexing map for MS disease used to sort disease groups in the
     * heat-map.
     */
    private final List<String> msReindexMap;
    /**
     * Re-index map for AD disease used to sort disease groups in the heat-map.
     */
    private final List<String> adReindexMap;
    /**
     * Re-index map for PD disease used to sort disease groups in the heat-map.
     */
    private final List<String> pdReindexMap;
    /**
     * Suggested sub-groups name as default (updated by CSF-PR 2.0).
     */
    private final String suggestNames = "Alzheimer's\n"
            + "CIS-MS(CIS)\n"
            + "CIS-CIS\n"
            + "CIS-MS\n"
            + "Healthy*\n"
            + "Healthy*\n"
            + "MCI\n"
            + "MCI nonprogressors\n"
            + "MCI progressors\n"
            + "RRMS Nataliz.\n"
            + "SPMS Lamotri.\n"
            + "Non MS\n"
            + "OIND\n"
            + "OIND + OND\n"
            + "OND\n"
            + "Sympt. controls\n"
            + "Aged controls\n"
            + "NDC\n"
            + "Non-neurodeg.\n"
            + "Parkinson's\n"
            + "PDD\n"
            + "PMS\n"
            + "SPMS\n"
            + "RRMS";

    /**
     * Original disease sub-groups names (publication names).
     */
    private final String oreginalNames = "Alzheimer's\n"
            + "CIS-MS(CIS)\n"
            + "CIS-CIS\n"
            + "CIS-MS\n"
            + "Aged healthy\n"
            + "Healthy controls\n"
            + "MCI\n"
            + "MCI nonprogressors\n"
            + "MCI progressors\n"
            + "RRMS Nataliz.\n"
            + "SPMS Lamotri.\n"
            + "Non MS\n"
            + "OIND\n"
            + "OIND + OND\n"
            + "OND\n"
            + "Sympt. controls\n"
            + "Aged controls\n"
            + "NDC\n"
            + "Non-neurodeg.\n"
            + "Parkinson's\n"
            + "PDD\n"
            + "PMS\n"
            + "SPMS\n"
            + "RRMS";

    /**
     * Current active quant data object that interact in the system.
     */
    private QuantData activeData;
    /**
     * The system is in initializing state.
     */
    private boolean init = true;

    /**
     * Constructor to initialize the main attributes
     *
     * @param Core_Logic main logic layer.
     */
    public DatasetUtility(CoreLogic Core_Logic) {
        quantDataMap = new LinkedHashMap<>();
        this.fullDiseaseCategoryMap = Core_Logic.getDiseaseCategorySet();
        this.quantDatasetInitialInformationObject = Core_Logic.getQuantDatasetInitialInformationObject();
        default_DiseaseCat_DiseaseGroupMap = new LinkedHashMap<>();
        oreginal_DiseaseCat_DiseaseGroupMap = new LinkedHashMap<>();
        quantDatasetInitialInformationObject.keySet().stream().filter((str) -> !(str.equalsIgnoreCase("All Diseases"))).forEach((str) -> {
            Set<String> diseaseGroupsName = Core_Logic.getDiseaseGroupNameMap(str);

            Map<String, String> diseaseGroupMap = new LinkedHashMap<>();
            Map<String, String> oreginalDiseaseGroupMap = new LinkedHashMap<>();
            diseaseGroupsName.stream().forEach((diseaseGroupName) -> {
                oreginalDiseaseGroupMap.put(diseaseGroupName, diseaseGroupName);
                diseaseGroupMap.put(diseaseGroupName, diseaseGroupName);
            });

            for (int i = 0; i < oreginalNames.split("\n").length; i++) {
                if (!diseaseGroupsName.contains(oreginalNames.split("\n")[i])) {
                    continue;
                }
                diseaseGroupMap.put(oreginalNames.split("\n")[i], suggestNames.split("\n")[i]);
            }
            default_DiseaseCat_DiseaseGroupMap.put(str, diseaseGroupMap);
            oreginal_DiseaseCat_DiseaseGroupMap.put(str, oreginalDiseaseGroupMap);
        });

        this.diseaseGroupFullNameMap = Core_Logic.getDiseaseGroupsFullNameMap();
        inUse_DiseaseCat_DiseaseGroupMap = default_DiseaseCat_DiseaseGroupMap;
        msReindexMap = new ArrayList<>();
        msReindexMap.add("RRMS__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("CDMS__Multiple Sclerosis__multiplesclerosisstyle");
        
         msReindexMap.add("CIS-MS__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("CIS-MS(CIS)__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("CIS-CIS__Multiple Sclerosis__multiplesclerosisstyle");
        
        
        msReindexMap.add("PMS__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("SPMS__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("Progressive MS__Multiple Sclerosis__multiplesclerosisstyle");
        
        
        
        
//        msReindexMap.add("CDMS__Multiple Sclerosis__multiplesclerosisstyle");
       
        msReindexMap.add("RRMS Nataliz.__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("SPMS Lamotri.__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("OIND__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("OIND + OND__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("OND__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("Sympt. controls__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("Non MS__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("CIS__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("MS treated__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("Neurological__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("Healthy*__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("Healthy controls__Multiple Sclerosis__multiplesclerosisstyle");

        pdReindexMap = new ArrayList<>();
        pdReindexMap.add("Parkinson's__Parkinson's__parkinsonstyle");
        pdReindexMap.add("PDD__Parkinson's__parkinsonstyle");
        pdReindexMap.add("Alzheimer's__Parkinson's__parkinsonstyle");
        pdReindexMap.add("NDC__Parkinson's__parkinsonstyle");
        pdReindexMap.add("Non Demented__Parkinson's__parkinsonstyle");
        pdReindexMap.add("Non-neurodeg.__Parkinson's__parkinsonstyle");
        pdReindexMap.add("Non Neurodeg.__Parkinson's__parkinsonstyle");
        pdReindexMap.add("Healthy*__Parkinson's__parkinsonstyle");
        pdReindexMap.add("Healthy controls__Parkinson's__parkinsonstyle");

        adReindexMap = new ArrayList<>();
        adReindexMap.add("Alzheimer's__Alzheimer's__alzheimerstyle");
        adReindexMap.add("LBD__Alzheimer's__alzheimerstyle");
        adReindexMap.add("MCI__Alzheimer's__alzheimerstyle");
        adReindexMap.add("MCI progressors__Alzheimer's__alzheimerstyle");
        adReindexMap.add("MCI nonprogressors__Alzheimer's__alzheimerstyle");
        adReindexMap.add("MCI__Alzheimer's__alzheimerstyle");
        adReindexMap.add("MCI-MCI__Alzheimer's__alzheimerstyle");
        adReindexMap.add("Parkinson's__Alzheimer's__alzheimerstyle");
        adReindexMap.add("Non Alzheimer's\n" + "Alzheimer-s_Disease");
        adReindexMap.add("Non Neurodeg.__Alzheimer's__alzheimerstyle");
        adReindexMap.add("Non-neurodeg.__Alzheimer's__alzheimerstyle");
        adReindexMap.add("Aged controls__Alzheimer's__alzheimerstyle");
        adReindexMap.add("Aged healthy__Alzheimer's__alzheimerstyle");
        adReindexMap.add("Healthy*__Alzheimer's__alzheimerstyle");
        adReindexMap.add("Healthy controls__Alzheimer's__alzheimerstyle");

    }

    /**
     * Get set of available disease categories that has all disease category
     * information
     *
     * @return fullDiseaseCategoryMap values - disease category set
     */
    public Collection<DiseaseCategoryObject> getFullDiseaseCategorySet() {
        return fullDiseaseCategoryMap.values();
    }

    /**
     * Set main disease category (MS,AD,PD or all)based on user selection
     *
     * @param mainDiseaseCategory Disease category name
     */
    public void setMainDiseaseCategory(String mainDiseaseCategory) {
        if (init) {
            init = false;
            LinkedHashSet<HeatMapHeaderCellInformationBean> oreginalColumnIds = new LinkedHashSet<>();
            LinkedHashSet<HeatMapHeaderCellInformationBean> oreginalRowIds = new LinkedHashSet<>();
            LinkedHashSet<HeatMapHeaderCellInformationBean> activeColumnIds = new LinkedHashSet<>();
            LinkedHashSet<HeatMapHeaderCellInformationBean> activeRowIds = new LinkedHashSet<>();
            Set<DiseaseGroupComparison> diseaseComparisonSet = new LinkedHashSet<>();
            QuantData allQuantData = new QuantData();
            for (String diseaseCategoryname : quantDatasetInitialInformationObject.keySet()) {
                InitialInformationObject initQuantData = quantDatasetInitialInformationObject.get(diseaseCategoryname);
                updateQuantDatasetsList(diseaseCategoryname, initQuantData.getQuantDatasetsMap());
                if (diseaseCategoryname.equalsIgnoreCase("All Diseases")) {
                    continue;
                }

                QuantData quantData = updateDiseaseGroups(initQuantData.getQuantDatasetsMap(), diseaseCategoryname);

                quantData.setDiseaseCategory(diseaseCategoryname);
                quantData.setActiveDatasetPieChartsFilters(initQuantData.getActiveDatasetPieChartsFilters());
                updateGroupsNames(diseaseCategoryname, quantData);
                quantDataMap.put(diseaseCategoryname, quantData);
                oreginalColumnIds.addAll(quantData.getOreginalColumnIds());
                oreginalRowIds.addAll(quantData.getOreginalRowIds());
                activeColumnIds.addAll(quantData.getActiveColumnIds());
                activeRowIds.addAll(quantData.getActiveRowIds());
                diseaseComparisonSet.addAll(quantData.getDiseaseGroupComparisonsSet());
                allQuantData.setActiveDatasetPieChartsFilters(quantData.getActiveDatasetPieChartsFilters());

            }
            allQuantData.setOreginalColumnIds(oreginalColumnIds);
            allQuantData.setOreginalRowIds(oreginalRowIds);
            allQuantData.setActiveColumnIds(activeColumnIds);
            allQuantData.setActiveRowIds(activeRowIds);
            allQuantData.setDiseaseComparisonSet(diseaseComparisonSet);
            allQuantData.setDiseaseCategory("All Diseases");
            quantDataMap.put("All Diseases", allQuantData);

            default_DiseaseCat_DiseaseGroupMap = sortDiseaseCategoryGroups(default_DiseaseCat_DiseaseGroupMap);
            oreginal_DiseaseCat_DiseaseGroupMap = sortDiseaseCategoryGroups(oreginal_DiseaseCat_DiseaseGroupMap);
            inUse_DiseaseCat_DiseaseGroupMap = default_DiseaseCat_DiseaseGroupMap;
            fullDiseaseCategoryMap.values().stream().filter((diseaseCategory) -> !(diseaseCategory.getDiseaseCategory().equalsIgnoreCase("All Diseases"))).forEach((diseaseCategory) -> {
                diseaseCategory.setDiseaseSubGroups(inUse_DiseaseCat_DiseaseGroupMap.get(diseaseCategory.getDiseaseCategory()));
                diseaseCategory.setDiseaseSubGroupsToFullName(diseaseGroupFullNameMap);
            });

        }

        if (quantDataMap.containsKey(mainDiseaseCategory)) {
            activeData = quantDataMap.get(mainDiseaseCategory);
        } else {
            InitialInformationObject initQuantData = quantDatasetInitialInformationObject.get(mainDiseaseCategory);
            activeData = updateDiseaseGroups(initQuantData.getQuantDatasetsMap(), mainDiseaseCategory);
            activeData.setDiseaseCategory(mainDiseaseCategory);
            activeData.setActiveDatasetPieChartsFilters(initQuantData.getActiveDatasetPieChartsFilters());
            updateGroupsNames(mainDiseaseCategory, activeData);
            quantDataMap.put(mainDiseaseCategory, activeData);
            updateQuantDatasetsList(mainDiseaseCategory, initQuantData.getQuantDatasetsMap());

        }

    }

    /**
     * Get the disease group row labels for the current active disease category
     *
     * @return active row labels category set
     */
    public LinkedHashSet<HeatMapHeaderCellInformationBean> getRowLabels() {
        return activeData.getActiveRowIds();
    }

    /**
     * Get the disease group column labels for the current active disease
     * category
     *
     * @return active column labels category set
     */
    public LinkedHashSet<HeatMapHeaderCellInformationBean> getColumnLabels() {
        return activeData.getActiveColumnIds();

    }

    /**
     * Get the disease group comparisons for the current active disease category
     *
     * @return active disease group comparisons
     */
    public Set<DiseaseGroupComparison> getDiseaseGroupComparisonsSet() {
        return activeData.getDiseaseGroupComparisonsSet();

    }

    /**
     * Update heat-map column and row labels and disease comparison set
     *
     * @param quantDSArr Array of datasets.
     * @param diseaseCategory Disease category name.
     * @return updated quantData object for the disease category
     */
    private QuantData updateDiseaseGroups(Map<Integer, QuantDataset> quantDSArr, String diseaseCategory) {
        QuantData quantData = new QuantData();

        String[] diseaseGroupsI = new String[quantDSArr.size()];
        String[] diseaseGroupsII = new String[quantDSArr.size()];
        Set<DiseaseGroupComparison> diseaseComparisonSet = new LinkedHashSet<>();
        int i = 0;
        for (QuantDataset ds : quantDSArr.values()) {
            if (ds == null) {
                continue;
            }
            DiseaseGroupComparison diseaseGroup = new DiseaseGroupComparison();
            diseaseGroup.setDiseaseCategory(ds.getDiseaseCategory());
            diseaseGroup.setDiseaseStyleName(ds.getDiseaseStyleName());
            String pgI = ds.getDiseaseMainGroupI();
            diseaseGroup.setDiseaseMainGroupI(pgI);

            String subpgI = ds.getDiseaseSubGroup1();
            diseaseGroup.setOriginalDiseaseSubGroupI(subpgI);

            String label1;
            if (pgI.equalsIgnoreCase("Not Available") || pgI.equalsIgnoreCase("control")) {
                pgI = "";
            }
            if (!subpgI.equalsIgnoreCase("") && !subpgI.equalsIgnoreCase("Not Available")) {
                pgI = subpgI;
            }
            label1 = pgI;
            diseaseGroup.setActiveDiseaseSubGroupI(label1);
            String pgII = ds.getDiseaseMainGroup2();
            diseaseGroup.setDiseaseMainGroupII(pgII);
            String subpgII = ds.getDiseaseSubGroup2();
            diseaseGroup.setOriginalDiseaseSubGroupII(subpgII);
            String label2;
            if (pgII.equalsIgnoreCase("Not Available") || pgII.equalsIgnoreCase("control")) {
                pgII = "";
            }

            if (!subpgII.equalsIgnoreCase("") && !subpgII.equalsIgnoreCase("Not Available")) {
                pgII = subpgII;
            }
            label2 = pgII;
            diseaseGroup.setActiveDiseaseSubGroupII(label2);

            diseaseGroupsI[i] = label1 + "__" + diseaseGroup.getDiseaseCategory() + "__" + diseaseGroup.getDiseaseStyleName();
            diseaseGroupsII[i] = label2 + "__" + diseaseGroup.getDiseaseCategory() + "__" + diseaseGroup.getDiseaseStyleName();
            diseaseGroup.setQuantDatasetIndex(ds.getQuantDatasetIndex());
            diseaseComparisonSet.add(diseaseGroup);
            i++;
        }
        if (diseaseGroupsI == null || diseaseGroupsII == null) {
            System.out.println("error at 85 class " + this.getClass().getName() + "   ");
            return null;
        }
        String[] pgArr = sortGroups(merge(diseaseGroupsI, diseaseGroupsII), diseaseCategory);

        LinkedHashSet<HeatMapHeaderCellInformationBean> selectedHeatMapRows = new LinkedHashSet<>();

        for (String str : pgArr) {
            if (!str.equalsIgnoreCase("") && !str.contains(userDiseaseGroupB)) {
                HeatMapHeaderCellInformationBean headerCellInfo = new HeatMapHeaderCellInformationBean();
                headerCellInfo.setDiseaseGroupName(str.split("__")[0]);
                headerCellInfo.setDiseaseGroupOreginalName(str.split("__")[0]);
                headerCellInfo.setDiseaseCategory(str.split("__")[1]);
                headerCellInfo.setDiseaseStyleName(str.split("__")[2]);
                headerCellInfo.setDiseaseCategory(str.split("__")[1]);
                headerCellInfo.setDiseaseHashedColor(fullDiseaseCategoryMap.get(headerCellInfo.getDiseaseCategory()).getDiseaseHashedColor());
                headerCellInfo.setDiseaseGroupFullName(diseaseGroupFullNameMap.get(headerCellInfo.getDiseaseGroupName()));
                selectedHeatMapRows.add(headerCellInfo);
            }
        }
        LinkedHashSet<HeatMapHeaderCellInformationBean> selectedHeatMapColumns = new LinkedHashSet<>();
        for (String str : pgArr) {
            if (!str.equalsIgnoreCase("") && !str.contains(userDiseaseGroupA)) {
                HeatMapHeaderCellInformationBean headerCellInfo = new HeatMapHeaderCellInformationBean();
                headerCellInfo.setDiseaseGroupName(str.split("__")[0]);
                headerCellInfo.setDiseaseGroupOreginalName(str.split("__")[0]);
                headerCellInfo.setDiseaseCategory(str.split("__")[1]);
                headerCellInfo.setDiseaseStyleName(str.split("__")[2]);
                headerCellInfo.setDiseaseHashedColor(fullDiseaseCategoryMap.get(headerCellInfo.getDiseaseCategory()).getDiseaseHashedColor());
                headerCellInfo.setDiseaseGroupFullName(diseaseGroupFullNameMap.get(headerCellInfo.getDiseaseGroupName()));
                selectedHeatMapColumns.add(headerCellInfo);
            }
        }

//        
        quantData.setOreginalColumnIds(selectedHeatMapColumns);
        quantData.setOreginalRowIds(selectedHeatMapRows);
        quantData.setDiseaseComparisonSet(diseaseComparisonSet);
        return quantData;

    }

    /**
     * Sort the groups to be ready to view in the heat map
     *
     * @param subGroupNamesArray Array of sub-group names
     * @param diseaseCategory Disease category name.
     * @return updated array of string for the disease category labels (columns
     * and rows)
     */
    private String[] sortGroups(String[] subGroupNamesArray, String diseaseCategory) {
        String[] sortedData = new String[subGroupNamesArray.length];
        List<String> quantDataSet = new ArrayList<>(Arrays.asList(subGroupNamesArray));
        int count = 0;

        if (diseaseCategory.equalsIgnoreCase("Multiple Sclerosis")) {

            for (String str : msReindexMap) {
                if (quantDataSet.contains(str)) {
                    sortedData[count] = str;
                    quantDataSet.remove(str);
                    count++;
                }
            }
            for (String str : quantDataSet) {
                sortedData[count] = str;
                count++;
            }
            quantDataSet.clear();
            quantDataSet.addAll(Arrays.asList(sortedData));
        } else if (diseaseCategory.equalsIgnoreCase("Alzheimer's")) {
            for (String str : adReindexMap) {
                if (quantDataSet.contains(str)) {
                    sortedData[count] = str;
                    quantDataSet.remove(str);
                    count++;
                }
            }
            for (String str : quantDataSet) {
                sortedData[count] = str;
                count++;
            }
            quantDataSet.clear();
            quantDataSet.addAll(Arrays.asList(sortedData));
        } else if (diseaseCategory.equalsIgnoreCase("Parkinson's")) {
            for (String str : pdReindexMap) {
                if (quantDataSet.contains(str)) {
                    sortedData[count] = str;
                    quantDataSet.remove(str);
                    count++;
                }
            }
            for (String str : quantDataSet) {
                sortedData[count] = str;
                count++;
            }
            quantDataSet.clear();
            quantDataSet.addAll(Arrays.asList(sortedData));
        } else {
            //All Diseases

            for (String str : msReindexMap) {
                if (quantDataSet.contains(str)) {
                    sortedData[count] = str;
                    quantDataSet.remove(str);
                    count++;
                }
            }
            for (String str : quantDataSet) {
                if (str.contains("Multiple Sclerosis")) {
                    sortedData[count] = str;
                    count++;
                }

            }

            for (String str : pdReindexMap) {
                if (quantDataSet.contains(str)) {
                    sortedData[count] = str;
                    quantDataSet.remove(str);
                    count++;
                }
            }
            for (String str : quantDataSet) {
                if (str.contains("Parkinson's")) {
                    sortedData[count] = str;
                    count++;
                }

            }
            for (String str : adReindexMap) {
                if (quantDataSet.contains(str)) {
                    sortedData[count] = str;
                    quantDataSet.remove(str);
                    count++;
                }
            }
            for (String str : quantDataSet) {
                if (str.contains("Alzheimer's")) {
                    sortedData[count] = str;
                    count++;
                }

            }
            quantDataSet.clear();
            quantDataSet.addAll(Arrays.asList(sortedData));

        }

        return sortedData;

    }

    /**
     * Merge two string array into one array and remove doubled data then
     * sorting the data after that
     *
     * @param arr1 Disease sub-group I names array
     * @param arr2Disease sub-group II names array
     * @return sorted string array
     */
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

        Map<String, ArrayList<String>> sortOnGroupMap = new LinkedHashMap<>();
        for (int x = newArr.length - 1; x >= 0; x--) {
            String s = newArr[x];
            String diseaseCat = s.split("__")[1];
            if (!sortOnGroupMap.containsKey(diseaseCat)) {
                sortOnGroupMap.put(diseaseCat, new ArrayList<>());
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

    /**
     * Get the full quant dataset map the current active disease category
     *
     * @return map of quant dataset objects
     */
    public Map<Integer, QuantDataset> getFullQuantDsMap() {
        return quantDatasetInitialInformationObject.get(activeData.getDiseaseCategory()).getQuantDatasetsMap();

    }

    /**
     * Get the active data columns for the current active disease category
     *
     * @return boolean array of active column
     */
    public boolean[] getActiveDataColumns() {

        return activeData.getActiveDatasetPieChartsFilters();
    }

    /**
     * Update and combine disease sub groups based on user selection
     *
     *
     * @param updatedGroupsNamesMap updated disease sub group names
     */
    public void updateCobinedGroups(Map<String, Map<String, String>> updatedGroupsNamesMap) {
        //if null reset to publication 
        if (updatedGroupsNamesMap == null) {
            this.inUse_DiseaseCat_DiseaseGroupMap = oreginal_DiseaseCat_DiseaseGroupMap;
        } else if (updatedGroupsNamesMap.isEmpty()) {
            this.inUse_DiseaseCat_DiseaseGroupMap = this.default_DiseaseCat_DiseaseGroupMap;
        } else {
            this.inUse_DiseaseCat_DiseaseGroupMap = sortDiseaseCategoryGroups(updatedGroupsNamesMap);
        }
        quantDataMap.keySet().stream().forEach((diseaseCategory) -> {
            updateGroupsNames(diseaseCategory, quantDataMap.get(diseaseCategory));
            InitialInformationObject initQuantData = quantDatasetInitialInformationObject.get(diseaseCategory);
            updateQuantDatasetsList(diseaseCategory, initQuantData.getQuantDatasetsMap());
        });

    }

    /**
     * Update disease sub-group names list based in user selection
     *
     * @param diseaseCategoryName Disease category name
     * @param quantDatasetsList Map of quant datasets
     */
    private void updateQuantDatasetsList(String diseaseCategoryName, Map<Integer, QuantDataset> quantDatasetsList) {

        if (!inUse_DiseaseCat_DiseaseGroupMap.containsKey(diseaseCategoryName)) {
            return;

        }
        Map<String, String> updatedNamesMap = inUse_DiseaseCat_DiseaseGroupMap.get(diseaseCategoryName);
        quantDatasetsList.values().stream().map((quantDataset) -> {
            quantDataset.setActiveDiseaseSubGroupI(updatedNamesMap.get(quantDataset.getDiseaseSubGroup1()));
            return quantDataset;
        }).forEach((quantDataset) -> {
            quantDataset.setActiveDiseaseSubGroupII(updatedNamesMap.get(quantDataset.getDiseaseSubGroup2()));
        });

    }

    /**
     * Update disease sub-group names based in user selection
     *
     * @param diseaseCategoryName Disease category name
     * @param updatingData Quant data contains all quant data information
     * required in the visualization layout.
     */
    private void updateGroupsNames(String diseaseCategoryName, QuantData updatingData) {
        if (!inUse_DiseaseCat_DiseaseGroupMap.containsKey(diseaseCategoryName)) {
            return;

        }
        Map<String, String> updatedNamesMap = inUse_DiseaseCat_DiseaseGroupMap.get(diseaseCategoryName);
        LinkedHashSet<HeatMapHeaderCellInformationBean> activeColumnIds = new LinkedHashSet<>();
        LinkedHashSet<HeatMapHeaderCellInformationBean> activeRowIds = new LinkedHashSet<>();
        updatingData.getOreginalColumnIds().stream().filter((header) -> !(!updatedNamesMap.containsKey(header.getDiseaseGroupOreginalName()))).forEach((header) -> {
            header.setDiseaseGroupName(updatedNamesMap.get(header.getDiseaseGroupOreginalName()));
            activeColumnIds.add(header);

        });
        updatingData.getOreginalRowIds().stream().filter((header) -> !(!updatedNamesMap.containsKey(header.getDiseaseGroupOreginalName()))).forEach((header) -> {
            header.setDiseaseGroupName(updatedNamesMap.get(header.getDiseaseGroupOreginalName()));
            activeRowIds.add(header);
        });
        Set<DiseaseGroupComparison> diseaseComparisonSet = updatingData.getDiseaseGroupComparisonsSet();
        Set<DiseaseGroupComparison> updatedDiseaseComparisonSet = new LinkedHashSet<>();
        diseaseComparisonSet.stream().map((comparison) -> {
            if (updatedNamesMap.containsKey(comparison.getOriginalDiseaseSubGroupI())) {
                comparison.setActiveDiseaseSubGroupI(updatedNamesMap.get(comparison.getOriginalDiseaseSubGroupI()));
            }
            return comparison;
        }).map((comparison) -> {
            if (updatedNamesMap.containsKey(comparison.getOriginalDiseaseSubGroupII())) {
                comparison.setActiveDiseaseSubGroupII(updatedNamesMap.get(comparison.getOriginalDiseaseSubGroupII()));
            }
            return comparison;
        }).forEach((comparison) -> {
            updatedDiseaseComparisonSet.add(comparison);
        });

        updatingData.setDiseaseComparisonSet(updatedDiseaseComparisonSet);
        updatingData.setActiveColumnIds(activeColumnIds);
        updatingData.setActiveRowIds(activeRowIds);

    }

    /**
     * Sort disease sub-group names for disease categories based on sorting maps
     *
     * @param mapToSort Map of disease category with its own disease sub-group
     * name map.
     */
    private Map<String, Map<String, String>> sortDiseaseCategoryGroups(Map<String, Map<String, String>> mapToSort) {
        Map<String, Map<String, String>> sortedMap = new LinkedHashMap<>();

        mapToSort.keySet().stream().forEach((diseaseCategory) -> {
            QuantData quantData = quantDataMap.get(diseaseCategory);

            Map<String, String> subGroupMap = mapToSort.get(diseaseCategory);
            Map<String, String> sortedSubGroupMap = new LinkedHashMap<>();
            quantData.getOreginalRowIds().stream().forEach((group) -> {
                sortedSubGroupMap.put(group.getDiseaseGroupOreginalName(), subGroupMap.get(group.getDiseaseGroupOreginalName()));
            });
            sortedMap.put(diseaseCategory, sortedSubGroupMap);
        });

        Map<String, String> sortedAllSubGroupMap = new LinkedHashMap<>();
        sortedMap.values().stream().forEach((subGroupMap) -> {
            sortedAllSubGroupMap.putAll(subGroupMap);
        });
        sortedMap.put("All Diseases", sortedAllSubGroupMap);
        return sortedMap;
    }

    /**
     * Get full disease sub groups name list
     *
     * @return set of disease sub group name
     */
    public Set<String> getFullDiseaseGroupNameSet() {
        Set<String> fulldiseaseGroupsNameSet = new TreeSet<>();
        oreginal_DiseaseCat_DiseaseGroupMap.keySet().stream().forEach((key) -> {
            fulldiseaseGroupsNameSet.addAll(oreginal_DiseaseCat_DiseaseGroupMap.get(key).keySet());
        });
        return fulldiseaseGroupsNameSet;

    }

    /**
     * Get the current used map of disease category (MS,AD,PD..etc) information
     *
     * @return inUse_DiseaseCat_DiseaseGroupMap
     */
    public Map<String, Map<String, String>> getInUse_DiseaseCat_DiseaseGroupMap() {
        return inUse_DiseaseCat_DiseaseGroupMap;
    }

    /**
     * Activate searching layout sub-data handler
     *
     * @param searchSelection search selection data
     */
    public void switchToSearchingMode(QuantSearchSelection searchSelection) {

        QuantData refData = quantDataMap.get(searchSelection.getDiseaseCategory());
        QuantData searchingData = new QuantData();
        searchingData.setDiseaseCategory(searchSelection.getDiseaseCategory());
        Set<DiseaseGroupComparison> diseaseComparisonSet = new LinkedHashSet<>();

        for (DiseaseGroupComparison diseaseGroupComparison : refData.getDiseaseGroupComparisonsSet()) {
            if (searchSelection.getQuantDatasetIndexes().contains(diseaseGroupComparison.getQuantDatasetIndex())) {
                diseaseComparisonSet.add(diseaseGroupComparison);
            }

        }
        searchingData.setDiseaseComparisonSet(diseaseComparisonSet);
        searchingData.setActiveColumnIds(refData.getActiveColumnIds());
        searchingData.setActiveRowIds(refData.getActiveRowIds());
        searchingData.setActiveDatasetPieChartsFilters(refData.getActiveDatasetPieChartsFilters());
        searchingData.setOreginalColumnIds(refData.getOreginalColumnIds());
        searchingData.setOreginalRowIds(refData.getOreginalRowIds());
        activeData = searchingData;

    }

}
