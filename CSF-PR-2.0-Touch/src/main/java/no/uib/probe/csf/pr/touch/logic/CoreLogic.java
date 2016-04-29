package no.uib.probe.csf.pr.touch.logic;

import java.awt.Color;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.database.DataBaseLayer;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseCategoryObject;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetInitialInformationObject;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;
import no.uib.probe.csf.pr.touch.logic.beans.QuantProtein;
import no.uib.probe.csf.pr.touch.view.smallscreen.OverviewInfoBean;

/**
 *
 * @author Yehia Farag
 *
 * this class contains the logic layer (main computing code) this class interact
 * with both the data handler and the data access layer
 */
public class CoreLogic implements Serializable {

    private final DataBaseLayer database;
    private final Map<String, String> diseaseCategoryStyles = new HashMap<>();
    private final Map<String, String> diseaseColorMap = new HashMap<>();

    public CoreLogic(String url, String dbName, String driver, String userName, String password, String filesURL) {
        database = new DataBaseLayer(url, dbName, driver, userName, password);
        diseaseCategoryStyles.put("Multiple Sclerosis", "multiplesclerosisstyle");
        diseaseCategoryStyles.put("Alzheimer's", "alzheimerstyle");
        diseaseCategoryStyles.put("Parkinson's", "parkinsonstyle");

        diseaseColorMap.put("Multiple Sclerosis", "#A52A2A");
        diseaseColorMap.put("Alzheimer's", "#4b7865");
        diseaseColorMap.put("Parkinson's", "#74716E");

    }

    /**
     * this method responsible for getting the resource overview information
     *
     * @return OverviewInfoBean resource information bean
     */
    public OverviewInfoBean getResourceOverviewInformation() {
        return database.getResourceOverviewInformation();

    }

    /**
     * this method responsible for getting initial publication information
     *
     * @return list of publications available in the the resource
     */
    public List<Object[]> getPublicationList() {

        return database.getPublicationList();

    }

    /**
     * this method responsible for getting initial datasets information
     *
     * @return set of datasets information available in the the resource
     */
    public Set<QuantDatasetObject> getQuantDatasetList() {
        return database.getQuantDatasetList();

    }

    /**
     * Get the current available disease category list
     *
     * @return set of disease category objects that has all disease category
     * information and styling information
     */
    public Set<DiseaseCategoryObject> getDiseaseCategorySet() {

        int total = 0;
        Set<DiseaseCategoryObject> availableDiseaseCategory = database.getDiseaseCategorySet();
        for (DiseaseCategoryObject diseaseCategoryObject : availableDiseaseCategory) {
            diseaseCategoryObject.setDiseaseStyleName(diseaseCategoryStyles.get(diseaseCategoryObject.getDiseaseName()));
            diseaseCategoryObject.setDiseaseHashedColor(diseaseColorMap.get(diseaseCategoryObject.getDiseaseName()));
            diseaseCategoryObject.setDiseaseAwtColor(Color.decode(diseaseCategoryObject.getDiseaseHashedColor()));
            total += diseaseCategoryObject.getDatasetNumber();

        }
        DiseaseCategoryObject diseaseCategoryObject = new DiseaseCategoryObject();
        diseaseCategoryObject.setDiseaseName("All Diseases");
        diseaseCategoryObject.setDatasetNumber(total);
        diseaseCategoryObject.setDiseaseStyleName("alldiseasestyle");
        diseaseCategoryObject.setDiseaseHashedColor("#7E7E7E");
        diseaseCategoryObject.setDiseaseAwtColor(Color.decode("#7E7E7E"));
        availableDiseaseCategory.add(diseaseCategoryObject);

        return availableDiseaseCategory;
    }

    /**
     * get available quantification datasets initial information object that
     * contains the available datasets list and the active columns (to hide them
     * if they are empty)
     *
     * @return QuantDatasetInitialInformationObject
     */
    public Map<String, QuantDatasetInitialInformationObject> getQuantDatasetInitialInformationObject() {
        Map<String, QuantDatasetInitialInformationObject> quantStudyInitInfoMap = database.getQuantDatasetInitialInformationObject();

        boolean[] activeHeaders = new boolean[27];
        Set<String> diseaseCategories = new LinkedHashSet<>();
        QuantDatasetInitialInformationObject allDatasetObject = new QuantDatasetInitialInformationObject();
        Map<Integer, QuantDatasetObject> updatedQuantDatasetObjectMap = new LinkedHashMap<>();

        for (String disease_category : quantStudyInitInfoMap.keySet()) {
            QuantDatasetInitialInformationObject datasetObject = quantStudyInitInfoMap.get(disease_category);
            boolean[] dataactiveHeader = datasetObject.getActiveHeaders();
            int counter = 0;
            for (boolean active : dataactiveHeader) {
                if (!activeHeaders[counter] && active) {
                    activeHeaders[counter] = true;
                }
                counter++;
            }
            updatedQuantDatasetObjectMap.putAll(datasetObject.getQuantDatasetsList());
            diseaseCategories.addAll(datasetObject.getDiseaseCategories());

        }
        allDatasetObject.setQuantDatasetsList(updatedQuantDatasetObjectMap);
        allDatasetObject.setActiveHeaders(activeHeaders);
        allDatasetObject.setDiseaseCategories(diseaseCategories);
        quantStudyInitInfoMap.put("All", allDatasetObject);

        return quantStudyInitInfoMap;

    }

    /**
     * get active quantification pie charts filters (to hide them if they are
     * empty)
     *
     * @return boolean array for the active and not active pie chart filters
     * indexes
     */
    public Map<String, boolean[]> getActivePieChartQuantFilters() {
        return database.getActivePieChartQuantFilters();

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
        return database.getActivePieChartQuantFilters(searchQuantificationProtList);

    }

    /**
     * Get set of disease groups names for special disease category
     *
     * @param diseaseCat
     * @return map of the short and long diseases names
     */
    public Set<String> getDiseaseGroupNameMap(String diseaseCat) {
        return database.getDiseaseGroupNameMap(diseaseCat);

    }

}
