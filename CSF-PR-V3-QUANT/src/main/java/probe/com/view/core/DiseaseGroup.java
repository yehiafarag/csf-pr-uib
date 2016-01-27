/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

/**
 *
 * @author Yehia Farag
 */
public class DiseaseGroup {

    private String patientsGroupI;
    private String patientsGroupII;
    private int quantDatasetIndex, originalDatasetIndex;

    private String patientsSubGroupI, patientsSubGroupII;

    private String patientsGroupILabel;
    private String patientsGroupIILabel;
    private String originalDiseaseSubGroupI, originalDiseaseSubGroupII;

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
    public String getPatientsGroupI() {
        return patientsGroupI;
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
     * @param patientsGroupI
     */
    public void setPatientsGroupI(String patientsGroupI) {
        this.patientsGroupI = patientsGroupI;
    }

    /**
     *
     * @return
     */
    public String getPatientsGroupII() {
        return patientsGroupII;
    }

    /**
     *
     * @param patientsGroupII
     */
    public void setPatientsGroupII(String patientsGroupII) {
        this.patientsGroupII = patientsGroupII;
    }

    /**
     *
     * @return
     */
    public String getPatientsSubGroupI() {
        return patientsSubGroupI;
    }

    /**
     *
     * @param patientsSubGroupI
     */
    public void setPatientsSubGroupI(String patientsSubGroupI) {
        this.patientsSubGroupI = patientsSubGroupI;
    }

    /**
     *
     * @return
     */
    public String getPatientsSubGroupII() {
        return patientsSubGroupII;
    }

    /**
     *
     * @param patientsSubGroupII
     */
    public void setPatientsSubGroupII(String patientsSubGroupII) {
        this.patientsSubGroupII = patientsSubGroupII;
    }

    /**
     *
     * @return
     */
    public String getPatientsGroupILabel() {
        return patientsGroupILabel;
    }

    /**
     *
     * @param patientsGroupILabel
     */
    public void setPatientsGroupILabel(String patientsGroupILabel) {
        this.patientsGroupILabel = patientsGroupILabel;
    }

    /**
     *
     * @return
     */
    public String getPatientsGroupIILabel() {
        return patientsGroupIILabel;
    }

    /**
     *
     * @param patientsGroupIILabel
     */
    public void setPatientsGroupIILabel(String patientsGroupIILabel) {
        this.patientsGroupIILabel = patientsGroupIILabel;
    }

    /**
     *
     * @param label
     * @return
     */
    public boolean checkLabel(String label) {
        if (label.equalsIgnoreCase(patientsGroupILabel) || label.equalsIgnoreCase(patientsGroupIILabel)) {
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
        if (key.equalsIgnoreCase(patientsGroupILabel)) {
            return patientsGroupIILabel;
        } else if (key.equalsIgnoreCase(patientsGroupIILabel)) {
            return patientsGroupILabel;
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

}
