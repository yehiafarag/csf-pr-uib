/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.logic.beans;

import java.io.Serializable;

/**
 *
 * @author Yehia Farag
 *
 * this class represents disease group comparison that has all comparison
 * information
 */
public class DiseaseGroupComparison implements Serializable {

    private String DiseaseCategory;

    private String diseaseStyleName;

    public String getDiseaseCategory() {
        return DiseaseCategory;
    }

    public void setDiseaseCategory(String DiseaseCategory) {
        this.DiseaseCategory = DiseaseCategory;
    }

    private int quantDatasetIndex, originalDatasetIndex;

    private String diseaseMainGroupI, diseaseMainGroupII;
    private String originalDiseaseSubGroupI, originalDiseaseSubGroupII;
    private String activeDiseaseSubGroupI, activeDiseaseSubGroupII;

    public String getOriginalDiseaseSubGroupI() {
        return originalDiseaseSubGroupI;
    }

    public void setOriginalDiseaseSubGroupI(String originalDiseaseSubGroupI) {
        this.originalDiseaseSubGroupI = originalDiseaseSubGroupI;
    }

    public String getOriginalDiseaseSubGroupII() {
        return originalDiseaseSubGroupII;
    }

    public void setOriginalDiseaseSubGroupII(String originalDiseaseSubGroupII) {
        this.originalDiseaseSubGroupII = originalDiseaseSubGroupII;
    }

    /**
     *
     * @return
     */
    public String getDiseaseMainGroupI() {
        return diseaseMainGroupI;
    }

    /**
     *
     * @return
     */
    public int getQuantDatasetIndex() {
        return quantDatasetIndex;
    }

    /**
     *
     * @param quantDatasetIndex
     */
    public void setQuantDatasetIndex(int quantDatasetIndex) {
        this.quantDatasetIndex = quantDatasetIndex;
    }

    /**
     *
     * @param diseaseMainGroupI
     */
    public void setDiseaseMainGroupI(String diseaseMainGroupI) {
        this.diseaseMainGroupI = diseaseMainGroupI;
    }

    /**
     *
     * @return
     */
    public String getDiseaseMainGroupII() {
        return diseaseMainGroupII;
    }

    /**
     *
     * @param diseaseMainGroupII
     */
    public void setDiseaseMainGroupII(String diseaseMainGroupII) {
        this.diseaseMainGroupII = diseaseMainGroupII;
    }

    public String getActiveDiseaseSubGroupII() {
        return activeDiseaseSubGroupII;
    }

    public void setActiveDiseaseSubGroupII(String activeDiseaseSubGroupII) {
        this.activeDiseaseSubGroupII = activeDiseaseSubGroupII;
    }

    /**
     *
     * @return
     */
    public String getActiveDiseaseSubGroupI() {
        return activeDiseaseSubGroupI;
    }

    /**
     *
     * @param activeDiseaseSubGroupI
     */
    public void setActiveDiseaseSubGroupI(String activeDiseaseSubGroupI) {
        this.activeDiseaseSubGroupI = activeDiseaseSubGroupI;
    }

    /**
     *
     * @param label
     * @return
     */
    public boolean checkLabel(String label) {
        if (label.equalsIgnoreCase(activeDiseaseSubGroupI) || label.equalsIgnoreCase(activeDiseaseSubGroupII)) {
            return true;
        }
        return false;

    }

    /**
     *
     * @param key
     * @return
     */
    public String getValLabel(String key) {
        if (key.equalsIgnoreCase(activeDiseaseSubGroupI)) {
            return activeDiseaseSubGroupII;
        } else if (key.equalsIgnoreCase(activeDiseaseSubGroupII)) {
            return activeDiseaseSubGroupI;
        } else {
            return null;
        }

    }

    /**
     *
     * @return
     */
    public int getOriginalDatasetIndex() {
        return originalDatasetIndex;
    }

    /**
     *
     * @param originalDatasetIndex
     */
    public void setOriginalDatasetIndex(int originalDatasetIndex) {
        this.originalDatasetIndex = originalDatasetIndex;
    }

    public String getDiseaseStyleName() {
        return diseaseStyleName;
    }

    public void setDiseaseStyleName(String diseaseStyleName) {
        this.diseaseStyleName = diseaseStyleName;
    }

}
