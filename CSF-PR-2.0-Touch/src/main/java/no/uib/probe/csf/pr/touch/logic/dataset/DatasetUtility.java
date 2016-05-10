/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import no.uib.probe.csf.pr.touch.logic.CoreLogic;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseCategoryObject;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseGroupComparison;
import no.uib.probe.csf.pr.touch.logic.beans.HeatMapHeaderCellInformationBean;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetInitialInformationObject;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;

/**
 *
 * @author Yehia Farag
 *
 * this class responsible for maintaining the active dataset do all calculations
 * this class interact with Data_Handler and CoreLogic
 *
 */
public class DatasetUtility implements Serializable {

    private final CoreLogic Core_Logic;
    private final Map<String, QuantData> quantDataMap;
    private final Map<String, DiseaseCategoryObject> fullDiseaseCategoryMap;
    private final Map<String, QuantDatasetInitialInformationObject> quantDatasetInitialInformationObject;
    private final Map<String, Map<String, String>> default_DiseaseCat_DiseaseGroupMap;
    private String userDiseaseGroupA = "VeryHårdToExistByChanceøæå", userDiseaseGroupB = "VeryHårdToExistByChanceøæå";
    private final Map<String, String> diseaseGroupFullNameMap;

    private final List<String> msReindexMap;
    private final List<String> adReindexMap;
    private final List<String> pdReindexMap;

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

    public DatasetUtility(CoreLogic Core_Logic) {
        this.Core_Logic = Core_Logic;
        quantDataMap = new LinkedHashMap<>();
        this.fullDiseaseCategoryMap = Core_Logic.getDiseaseCategorySet();
        this.quantDatasetInitialInformationObject = Core_Logic.getQuantDatasetInitialInformationObject();

        default_DiseaseCat_DiseaseGroupMap = new LinkedHashMap<>();
        quantDatasetInitialInformationObject.keySet().stream().filter((str) -> !(str.equalsIgnoreCase("All"))).forEach((str) -> {
            Set<String> diseaseGroupsName = Core_Logic.getDiseaseGroupNameMap(str);

            Map<String, String> diseaseGroupMap = new LinkedHashMap<>();
            for (int i = 0; i < oreginalNames.split("\n").length; i++) {
                if (!diseaseGroupsName.contains(oreginalNames.split("\n")[i])) {
                    continue;
                }
                diseaseGroupMap.put(oreginalNames.split("\n")[i], suggestNames.split("\n")[i]);
            }
            default_DiseaseCat_DiseaseGroupMap.put(str, diseaseGroupMap);
        });
        this.diseaseGroupFullNameMap = Core_Logic.getDiseaseGroupsFullNameMap();

        msReindexMap = new ArrayList<>();
        msReindexMap.add("RRMS__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("CDMS__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("PMS__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("SPMS__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("Progressive MS__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("CDMS__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("CIS-MS__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("CIS-MS(CIS)__Multiple Sclerosis__multiplesclerosisstyle");
        msReindexMap.add("CIS-CIS__Multiple Sclerosis__multiplesclerosisstyle");
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
        
        for(DiseaseCategoryObject diseaseCategory:fullDiseaseCategoryMap.values()){
            if(diseaseCategory.getDiseaseCategory().equalsIgnoreCase("All Diseases"))
                continue;
            diseaseCategory.setDiseaseSubGroups(default_DiseaseCat_DiseaseGroupMap.get(diseaseCategory.getDiseaseCategory()).keySet());            
        
        }
        

    }
    
    private void updateDiseaseCategorySubGroups(){
    
    
    
    
    }

    /**
     * this method to get set of available disease categories that has all
     * disease category information
     *
     * @return disease category set
     */
    public Collection<DiseaseCategoryObject> getFullDiseaseCategorySet() {
        return fullDiseaseCategoryMap.values();
    }

    private QuantData activeData;

    public void setMainDiseaseCategory(String diseaseCategory) {
        if (quantDataMap.containsKey(diseaseCategory)) {
            activeData = quantDataMap.get(diseaseCategory);
        } else {
            QuantDatasetInitialInformationObject initQuantData = quantDatasetInitialInformationObject.get(diseaseCategory);
            activeData = updateDiseaseGroups(initQuantData.getQuantDatasetsList(), diseaseCategory);
            activeData.setDiseaseCategory(diseaseCategory);
            activeData.setActiveHeaders(initQuantData.getActiveHeaders());
            quantDataMap.put(diseaseCategory, activeData);

        }

    }

    /**
     * this method to get the disease group row labels for the current active
     * disease category
     *
     * @return active row labels category set
     */
    public LinkedHashSet<HeatMapHeaderCellInformationBean> getRowLabels() {
        return activeData.getActiveRowIds();

    }

    /**
     * this method to get the disease group column labels for the current active
     * disease category
     *
     * @return active column labels category set
     */
    public LinkedHashSet<HeatMapHeaderCellInformationBean> getColumnLabels() {
        return activeData.getActiveColumnIds();

    }

    /**
     * this method to get the disease group comparisons for the current active
     * disease category
     *
     * @return active disease group comparisons
     */
    public Set<DiseaseGroupComparison> getDiseaseGroupComparisonsSet() {
        return activeData.getDiseaseGroupComparisonsSet();

    }

    private QuantData updateDiseaseGroups(Map<Integer, QuantDatasetObject> quantDSArr, String diseaseCategory) {
        QuantData quantData = new QuantData();
        String[] diseaseGroupsI = new String[quantDSArr.size()];
        String[] diseaseGroupsII = new String[quantDSArr.size()];
        Set<DiseaseGroupComparison> diseaseComparisonSet = new LinkedHashSet<>();
        int i = 0;
        for (QuantDatasetObject ds : quantDSArr.values()) {
            if (ds == null) {
                continue;
            }
            DiseaseGroupComparison diseaseGroup = new DiseaseGroupComparison();
            diseaseGroup.setDiseaseCategory(ds.getDiseaseCategory());
            diseaseGroup.setDiseaseStyleName(ds.getDiseaseStyleName());
            String pgI = ds.getPatientsGroup1();
            diseaseGroup.setPatientsGroupI(pgI);
            diseaseGroup.setOriginalDiseaseSubGroupI(ds.getPatientsSubGroup1());
            String subpgI = ds.getPatientsSubGroup1();
            diseaseGroup.setPatientsSubGroupI(subpgI);

            String label1;
            if (pgI.equalsIgnoreCase("Not Available") || pgI.equalsIgnoreCase("control")) {
                pgI = "";
            }
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
            diseaseGroup.setOriginalDiseaseSubGroupII(ds.getPatientsSubGroup2());
            String subpgII = ds.getPatientsSubGroup2();
            diseaseGroup.setPatientsSubGroupII(subpgII);
            if (!subpgII.equalsIgnoreCase("") && !subpgII.equalsIgnoreCase("Not Available")) {
                pgII = subpgII;
            }
            label2 = pgII;
            diseaseGroup.setPatientsGroupIILabel(label2);

            diseaseGroupsI[i] = label1 + "__" + diseaseGroup.getDiseaseCategory() + "__" + diseaseGroup.getDiseaseStyleName();
            diseaseGroupsII[i] = label2 + "__" + diseaseGroup.getDiseaseCategory() + "__" + diseaseGroup.getDiseaseStyleName();
            diseaseGroup.setQuantDatasetIndex(ds.getDsKey());
            diseaseGroup.setOriginalDatasetIndex(ds.getDsKey());
            diseaseComparisonSet.add(diseaseGroup);
            i++;
        }
        if (diseaseGroupsI == null || diseaseGroupsII == null) {
            System.out.println("error at 85 class " + this.getClass().getName() + "   ");
            return null;
        }
        String[] pgArr = sortGroups(merge(diseaseGroupsI, diseaseGroupsII),diseaseCategory);

        LinkedHashSet<HeatMapHeaderCellInformationBean> selectedHeatMapRows = new LinkedHashSet<>();

        for (String str : pgArr) {            
            if (!str.equalsIgnoreCase("") && !str.contains(userDiseaseGroupB)) {
                HeatMapHeaderCellInformationBean headerCellInfo = new HeatMapHeaderCellInformationBean();
                headerCellInfo.setDiseaseGroupName(str.split("__")[0]);
                headerCellInfo.setDiseaseCategory(str.split("__")[1]);
                headerCellInfo.setDiseaseStyleName(str.split("__")[2]);
                headerCellInfo.setDiseaseColor(fullDiseaseCategoryMap.get(headerCellInfo.getDiseaseCategory()).getDiseaseHashedColor());
                selectedHeatMapRows.add(headerCellInfo);
            }
        }
        LinkedHashSet<HeatMapHeaderCellInformationBean> selectedHeatMapColumns = new LinkedHashSet<>();
        for (String str : pgArr) {
            if (!str.equalsIgnoreCase("") && !str.contains(userDiseaseGroupA)) {
                HeatMapHeaderCellInformationBean headerCellInfo = new HeatMapHeaderCellInformationBean();
                headerCellInfo.setDiseaseGroupName(str.split("__")[0]);
                headerCellInfo.setDiseaseCategory(str.split("__")[1]);
                headerCellInfo.setDiseaseStyleName(str.split("__")[2]);
                headerCellInfo.setDiseaseColor(fullDiseaseCategoryMap.get(headerCellInfo.getDiseaseCategory()).getDiseaseHashedColor());
                headerCellInfo.setDiseaseGroupFullName(diseaseGroupFullNameMap.get(headerCellInfo.getDiseaseGroupName()));
                selectedHeatMapColumns.add(headerCellInfo);
            }
        }

//        
        quantData.setActiveColumnIds(selectedHeatMapColumns);
        quantData.setActiveRowIds(selectedHeatMapRows);
        quantData.setDiseaseGroupArry(diseaseComparisonSet);
        return quantData;

    }

    private String[] sortGroups(String[] quantData, String diseaseCategory) {
        String[] sortedData = new String[quantData.length];
        List<String> quantDataSet = new ArrayList<>(Arrays.asList(quantData));
        int count = 0;

        if (diseaseCategory.equalsIgnoreCase("Multiple Sclerosis")) {

            for (String str : msReindexMap) {
                if (quantDataSet.contains(str)) {
                    sortedData[count] = str;
                    System.out.println("at str "+str);
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
            for (String str : msReindexMap) {
                if (quantDataSet.contains(str)) {
                    sortedData[count] = str;
                    quantDataSet.remove(str);
                    count++;
                }
            }
            for (String str : quantDataSet) {
                if (str.contains("Multiple_Sclerosis_Disease")) {
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
                if (str.contains("Parkinson-s_Disease")) {
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
                if (str.contains("Alzheimer-s_Disease")) {
                    sortedData[count] = str;
                    count++;
                }

            }
            quantDataSet.clear();
            quantDataSet.addAll(Arrays.asList(sortedData));

        }

        return sortedData;

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
     * this method to get the full quant dataset map the current active disease
     * category
     *
     * @return map of quant dataset objects
     */
    public Map<Integer, QuantDatasetObject> getFullQuantDsMap() {
        return quantDatasetInitialInformationObject.get(activeData.getDiseaseCategory()).getQuantDatasetsList();

    }

    /**
     * this method to get the active data columns for the current active disease
     * category
     *
     * @return boolean array of active column
     */
    public boolean[] getActiveDataColumns() {

        return activeData.getActiveHeaders();
    }

}
