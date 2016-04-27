/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.logic.beans;

import java.util.Map;
import java.util.Set;

/**
 *
 * @author Yehia Farag
 */
public class QuantDatasetInitialInformationObject {

    private Map<Integer, QuantDatasetObject> quantDatasetsList;
    private boolean[] activeHeaders;
    private Set<String>diseaseCategories;

    public Set<String> getDiseaseCategories() {
        return diseaseCategories;
    }

    public void setDiseaseCategories(Set<String> diseaseCategories) {
        this.diseaseCategories = diseaseCategories;
    }

    /**
     *
     * @return
     */
    public Map<Integer, QuantDatasetObject> getQuantDatasetsList() {
        return quantDatasetsList;
    }

    /**
     *
     * @param quantDatasetsList
     */
    public void setQuantDatasetsList(Map<Integer, QuantDatasetObject> quantDatasetsList) {
        this.quantDatasetsList = quantDatasetsList;
    }

    /**
     *
     * @return
     */
    public boolean[] getActiveHeaders() {
        return activeHeaders;
    }

    /**
     *
     * @param activeHeaders
     */
    public void setActiveHeaders(boolean[] activeHeaders) {
        this.activeHeaders = activeHeaders;
    }
}
