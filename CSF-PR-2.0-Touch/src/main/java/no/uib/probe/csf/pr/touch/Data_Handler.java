/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.CoreLogic;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseCategoryObject;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetInitialInformationObject;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;
import no.uib.probe.csf.pr.touch.logic.beans.QuantProtein;
import no.uib.probe.csf.pr.touch.view.smallscreen.OverviewInfoBean;

/**
 *
 * @author Yehia Farag
 *
 * this class responsible for handling the datasets across the system the
 * handler interact with both visualization and logic layer
 *
 *
 */
public class Data_Handler implements Serializable {

    private final CoreLogic coreLogic;
    private final Map<String, String> diseaseHashedColorMap = new HashMap<String, String>();
    private final Set<DiseaseCategoryObject> fullDiseaseCategorySet;
    private Set<DiseaseCategoryObject> inUseDiseaseCategorySet;
    private final Map<String, Map<String, String>> default_DiseaseCat_DiseaseGroupMap;
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

    private final Map<String, QuantDatasetInitialInformationObject> quantDatasetInitialInformationObject;
    private final Map<String, String> diseaseStyleMap = new HashMap<String, String>();

    public Map<String, QuantDatasetInitialInformationObject> getQuantDatasetInitialInformationObject() {
        return quantDatasetInitialInformationObject;
    }

    public Map<String, String> getDiseaseStyleMap() {
        return diseaseStyleMap;
    }

    /**
     *
     * @param url database url
     * @param dbName database name
     * @param driver database driver
     * @param userName database username
     * @param filesURL user folder url
     * @param password database password
     */
    public Data_Handler(String url, String dbName, String driver, String userName, String password, String filesURL) {
        this.coreLogic = new CoreLogic(url, dbName, driver, userName, password, filesURL);
        this.fullDiseaseCategorySet = coreLogic.getDiseaseCategorySet();
        this.inUseDiseaseCategorySet = fullDiseaseCategorySet;

        this.quantDatasetInitialInformationObject = coreLogic.getQuantDatasetInitialInformationObject();

        diseaseHashedColorMap.put("Multiple_Sclerosis_Disease", "#A52A2A");
        diseaseHashedColorMap.put("Alzheimer-s_Disease", "#4b7865");
        diseaseHashedColorMap.put("Parkinson-s_Disease", "#74716E");
        diseaseHashedColorMap.put("Amyotrophic_Lateral_Sclerosis_Disease", "#7D0725");
        diseaseHashedColorMap.put("UserData", "#8210B0");

        default_DiseaseCat_DiseaseGroupMap = new LinkedHashMap<>();
        quantDatasetInitialInformationObject.keySet().stream().filter((str) -> !(str.equalsIgnoreCase("All"))).forEach((str) -> {
            Set<String> diseaseGroupsName = coreLogic.getDiseaseGroupNameMap(str);

            Map<String, String> diseaseGroupMap = new LinkedHashMap<>();
            for (int i = 0; i < oreginalNames.split("\n").length; i++) {
                if (!diseaseGroupsName.contains(oreginalNames.split("\n")[i])) {
                    continue;
                }
                diseaseGroupMap.put(oreginalNames.split("\n")[i], suggestNames.split("\n")[i]);
            }
            default_DiseaseCat_DiseaseGroupMap.put(str, diseaseGroupMap);
        });

        diseaseStyleMap.put("Parkinson-s_Disease", "pdLabel");
        diseaseStyleMap.put("Alzheimer-s_Disease", "adLabel");
        diseaseStyleMap.put("Amyotrophic_Lateral_Sclerosis_Disease", "alsLabel");
        diseaseStyleMap.put("Multiple_Sclerosis_Disease", "msLabel");
    }

    /**
     * this method responsible for getting the resource overview information
     *
     * @return OverviewInfoBean resource information bean
     */
    public OverviewInfoBean getResourceOverviewInformation() {
        return this.coreLogic.getResourceOverviewInformation();

    }

    /**
     * this method responsible for getting initial publication information
     *
     * @return list of publications available in the the resource
     */
    public List<Object[]> getPublicationList() {

        return this.coreLogic.getPublicationList();

    }

    /**
     * this method responsible for getting initial datasets information
     *
     * @return set of datasets information available in the the resource
     */
    public Set<QuantDatasetObject> getQuantDatasetList() {
        return this.coreLogic.getQuantDatasetList();

    }

    /**
     * this method responsible for map of disease # colors for disease labels
     *
     * @return map of disease names and colors
     */
    public Map<String, String> getDiseaseHashedColorMap() {
        return diseaseHashedColorMap;
    }

    /**
     * Get the current available disease category list
     *
     * @return set of disease category objects that has all disease category
     * information and styling information
     */
    public Set<DiseaseCategoryObject> getDiseaseCategorySet() {
        return inUseDiseaseCategorySet;
    }

    /**
     * this method responsible for loading and initialize all information for
     * the selected disease category
     *
     * @param diseaseCategory
     */
    public void loadDiseaseCategory(String diseaseCategory) {

    }

    /**
     * get active quantification pie charts filters (to hide them if they are
     * empty)
     *
     * @return boolean array for the active and not active pie chart filters
     * indexes
     */
    public Map<String, boolean[]> getActivePieChartQuantFilters() {
        return coreLogic.getActivePieChartQuantFilters();

    }

    /**
     * get active quantification pie charts filters within quant searching
     * proteins results (to hide them if they are empty)
     *
     * @param searchQuantificationProtList
     * @return boolean array for the active and not active pie chart filters
     * indexes
     */
    public Map<String, boolean[]> getActivePieChartQuantFilters(List<QuantProtein> searchQuantificationProtList) {
        return coreLogic.getActivePieChartQuantFilters(searchQuantificationProtList);

    }

    public Map<String, Map<String, String>> getDefault_DiseaseCat_DiseaseGroupMap() {
        return default_DiseaseCat_DiseaseGroupMap;
    }

}
