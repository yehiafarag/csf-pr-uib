/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.CoreLogic;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;
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
public class DataHandler implements Serializable {

    private final CoreLogic coreLogic;
    private final Map<String, String> diseaseHashedColorMap = new HashMap<String, String>();

    /**
     *
     * @param url  database url
     * @param dbName database name
     * @param driver database driver
     * @param userName database username
     * @param filesURL user folder url
     * @param password database password
     */
    public DataHandler(String url, String dbName, String driver, String userName, String password, String filesURL) {
        this.coreLogic = new CoreLogic(url, dbName, driver, userName, password, filesURL);
        diseaseHashedColorMap.put("Multiple_Sclerosis_Disease", "#A52A2A");
        diseaseHashedColorMap.put("Alzheimer-s_Disease", "#4b7865");
        diseaseHashedColorMap.put("Parkinson-s_Disease", "#74716E");
        diseaseHashedColorMap.put("Amyotrophic_Lateral_Sclerosis_Disease", "#7D0725");
        diseaseHashedColorMap.put("UserData", "#8210B0");
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

}
