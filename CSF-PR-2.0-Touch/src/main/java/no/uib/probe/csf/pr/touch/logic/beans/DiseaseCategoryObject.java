package no.uib.probe.csf.pr.touch.logic.beans;

import java.awt.Color;
import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author Yehia Farag
 *
 * This class represent the information required for the disease category like
 * color, name..etc
 */
public class DiseaseCategoryObject implements Serializable {

    /*
     *Disease category name (MS, AD, PD..etc)
     */
    private String diseaseCategory;
    /*
     *Disease AWT Color to be used for generating JFree Charts
     */
    private Color diseaseAwtColor;
    /*
     *Disease HTML Color code
     */
    private String diseaseHashedColor;
    /*
     *Disease Style name at CSS file
     */
    private String diseaseStyleName;
    /*
     *Number of datasets for this category
     */
    private int datasetNumber;

    /*
     *Disease sub group name map (oreginal to updated)
     */
    private Map<String, String> diseaseSubGroups;

    /*
     *Disease sub group full name map (short to full name) (used for disease sub groups label tooltips in heatmap)
     */
    private Map<String, String> diseaseSubGroupsToFullName;

    /**
     * Get disease sub group short name to full name map
     *
     * @return map of disease groups name
     */
    public Map<String, String> getDiseaseSubGroupsToFullName() {
        return diseaseSubGroupsToFullName;
    }

    /**
     * Set disease sub group short name to full name map
     *
     * @param diseaseSubGroupsToFullName
     */
    public void setDiseaseSubGroupsToFullName(Map<String, String> diseaseSubGroupsToFullName) {
        this.diseaseSubGroupsToFullName = diseaseSubGroupsToFullName;
    }

    /**
     * Get disease sub group updated name to original name map
     *
     * @return diseaseSubGroups map
     */
    public Map<String, String> getDiseaseSubGroups() {
        return diseaseSubGroups;
    }

    /**
     * Set disease sub group updated name to orIginal name map
     *
     * @param diseaseSubGroups
     */
    public void setDiseaseSubGroups(Map<String, String> diseaseSubGroups) {
        this.diseaseSubGroups = diseaseSubGroups;
    }

    /**
     * Get included dataset number for this disease category
     *
     * @return datasetNumber
     */
    public int getDatasetNumber() {
        return datasetNumber;
    }

    /**
     * Set included dataset number for this disease category
     *
     * @param datasetNumber
     */
    public void setDatasetNumber(int datasetNumber) {
        this.datasetNumber = datasetNumber;
    }

    /**
     * Get disease style name (CSS name)
     *
     * @return diseaseStyleName
     */
    public String getDiseaseStyleName() {
        return diseaseStyleName;
    }

    /**
     * Set disease style name (CSS name)
     *
     * @param diseaseStyleName
     */
    public void setDiseaseStyleName(String diseaseStyleName) {
        this.diseaseStyleName = diseaseStyleName;
    }

    /**
     * Get disease category (MS,AD,PD...etc)
     *
     * @return diseaseCategory
     */
    public String getDiseaseCategory() {
        return diseaseCategory;
    }

    /**
     * Set disease category (MS,AD,PD...etc)
     *
     * @param diseaseCategory
     */
    public void setDiseaseCategory(String diseaseCategory) {
        this.diseaseCategory = diseaseCategory;
    }

    /**
     *
     * @return
     */
    public Color getDiseaseAwtColor() {
        return diseaseAwtColor;
    }

    /**
     * Set disease category AWT color (required for JFree Chart)
     *
     * @param diseaseAwtColor
     */
    public void setDiseaseAwtColor(Color diseaseAwtColor) {
        this.diseaseAwtColor = diseaseAwtColor;
    }

    /**
     * Get disease category HTML color code
     *
     * @return diseaseHashedColor
     */
    public String getDiseaseHashedColor() {
        return diseaseHashedColor;
    }

    /**
     * Set disease category HTML color code
     *
     * @param diseaseHashedColor
     */
    public void setDiseaseHashedColor(String diseaseHashedColor) {
        this.diseaseHashedColor = diseaseHashedColor;
    }

    /**
     * Override toString method to return disease category name
     */
    @Override
    public String toString() {
        return this.diseaseCategory;
    }

}
