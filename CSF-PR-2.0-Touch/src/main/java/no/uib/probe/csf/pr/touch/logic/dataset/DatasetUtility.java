/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.logic.dataset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.CoreLogic;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseCategoryObject;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseGroupComparison;
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
    private final Set<DiseaseCategoryObject> fullDiseaseCategorySet;
    private final Map<String, QuantDatasetInitialInformationObject> quantDatasetInitialInformationObject;
    private final Map<String, Map<String, String>> default_DiseaseCat_DiseaseGroupMap;
    private String userDiseaseGroupA = "VeryHårdToExistByChanceøæå", userDiseaseGroupB = "VeryHårdToExistByChanceøæå";
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
        this.fullDiseaseCategorySet = Core_Logic.getDiseaseCategorySet();
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

    }

    /**
     * this method to get set of available disease categories that has all
     * disease category information
     *
     * @return disease category set
     */
    public Set<DiseaseCategoryObject> getFullDiseaseCategorySet() {
        return fullDiseaseCategorySet;
    }

    private QuantData activeData;

    public void setMainDiseaseCategory(String diseaseCategory) {
        if (quantDataMap.containsKey(diseaseCategory)) {
            activeData = quantDataMap.get(diseaseCategory);
        } else {

            QuantDatasetInitialInformationObject initQuantData = quantDatasetInitialInformationObject.get(diseaseCategory);
            activeData = updateDiseaseGroups(initQuantData.getQuantDatasetsList());
            activeData.setDiseaseCategory(diseaseCategory);
            quantDataMap.put(diseaseCategory, activeData);

        }

    }

    /**
     * this method to get the disease group row labels for the current active 
     * disease category
     *
     * @return active row labels category set
     */
    public LinkedHashSet<String> getRowLabels() {
        return activeData.getActiveRowIds();

    }

    /**
     * this method to get the disease group column labels for the current active 
     * disease category
     *
     * @return active column labels category set
     */
    public LinkedHashSet<String> getColumnLabels() {
        return activeData.getActiveColumnIds();

    }
    /**
     * this method to get the disease group comparisons  for the current active 
     * disease category
     *
     * @return active disease group comparisons
     */
    public Set<DiseaseGroupComparison> getDiseaseGroupComparisonsSet() {
        return activeData.getDiseaseGroupComparisonsSet();

    }

    private QuantData updateDiseaseGroups(Map<Integer, QuantDatasetObject> quantDSArr) {
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

            diseaseGroupsI[i] = label1 + "__" + diseaseGroup.getDiseaseCategory()+"__"+diseaseGroup.getDiseaseStyleName();
            diseaseGroupsII[i] = label2 + "__" + diseaseGroup.getDiseaseCategory()+"__"+diseaseGroup.getDiseaseStyleName();
            diseaseGroup.setQuantDatasetIndex(ds.getDsKey());
            diseaseGroup.setOriginalDatasetIndex(ds.getDsKey());
            diseaseComparisonSet.add(diseaseGroup);
            i++;
        }
        if (diseaseGroupsI == null || diseaseGroupsII == null) {
            System.out.println("error at 85 class " + this.getClass().getName() + "   ");
            return null;
        }
        String[] pgArr = merge(diseaseGroupsI, diseaseGroupsII);

        LinkedHashSet<String> selectedHeatMapRows = new LinkedHashSet<>();

        for (String str : pgArr) {
            if (!str.equalsIgnoreCase("") && !str.contains(userDiseaseGroupB)) {
                selectedHeatMapRows.add(str);
            }
        }
        LinkedHashSet<String> selectedHeatMapColumns = new LinkedHashSet<>();
        for (String str : pgArr) {
            if (!str.equalsIgnoreCase("") && !str.contains(userDiseaseGroupA)) {
                selectedHeatMapColumns.add(str);
            }
        }
        quantData.setActiveColumnIds(selectedHeatMapColumns);
        quantData.setActiveRowIds(selectedHeatMapRows);
        quantData.setDiseaseGroupArry(diseaseComparisonSet);
        return quantData;

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

}
