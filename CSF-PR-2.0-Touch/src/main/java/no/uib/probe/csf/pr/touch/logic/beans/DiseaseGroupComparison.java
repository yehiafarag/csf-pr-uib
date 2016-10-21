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
    /*
     *Disease category name (MS, AD, PD..etc)
     */

    private String diseaseCategory;

    /*
     *Disease Style name at CSS file
     */
    private String diseaseStyleName;

    /*
     *The dataset index in the database
     */
    private int quantDatasetIndex;

    /*
     *The Disease main group I (not used in the current csf-pr-2.0)
     */
    private String diseaseMainGroupI;

    /*
     *The Disease main group II (not used in the current csf-pr-2.0)
     */
    private String diseaseMainGroupII;

    /*
     *The Disease sub group I (publication name)
     */
    private String originalDiseaseSubGroupI;
    /*
     *The Disease sub group II (publication name)
     */
    private String originalDiseaseSubGroupII;

    /*
     *The Disease sub group I (currently used in the system)
     */
    private String activeDiseaseSubGroupI;
    /*
     *The Disease sub group II (currently used in the system)
     */
    private String activeDiseaseSubGroupII;

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
    public void setDiseaseCategory(String DiseaseCategory) {
        this.diseaseCategory = DiseaseCategory;
    }

    /**
     * Get the disease sub group I (publication name)
     *
     * @return originalDiseaseSubGroupI
     */
    public String getOriginalDiseaseSubGroupI() {
        return originalDiseaseSubGroupI;
    }

    /**
     * Set the disease sub group I (publication name)
     *
     * @param originalDiseaseSubGroupI
     */
    public void setOriginalDiseaseSubGroupI(String originalDiseaseSubGroupI) {
        this.originalDiseaseSubGroupI = originalDiseaseSubGroupI;
    }

    /**
     * Get the disease sub group II (publication name)
     *
     * @return originalDiseaseSubGroupII
     */
    public String getOriginalDiseaseSubGroupII() {
        return originalDiseaseSubGroupII;
    }

    /**
     * Set the disease sub group II (publication name)
     *
     * @param originalDiseaseSubGroupII
     */
    public void setOriginalDiseaseSubGroupII(String originalDiseaseSubGroupII) {
        this.originalDiseaseSubGroupII = originalDiseaseSubGroupII;
    }

    /**
     * Get the disease main group I (not used in the current csf-pr-2.0)
     *
     * @return diseaseMainGroupI
     */
    public String getDiseaseMainGroupI() {
        return diseaseMainGroupI;
    }

    /**
     * Get he dataset index in the database
     *
     * @return quantDatasetIndex
     */
    public int getQuantDatasetIndex() {
        return quantDatasetIndex;
    }

    /**
     * Set the dataset index in the database
     *
     * @param quantDatasetIndex
     */
    public void setQuantDatasetIndex(int quantDatasetIndex) {
        this.quantDatasetIndex = quantDatasetIndex;
    }

    /**
     * Set the disease main group I (not used in the current csf-pr-2.0)
     *
     * @param diseaseMainGroupI
     */
    public void setDiseaseMainGroupI(String diseaseMainGroupI) {
        this.diseaseMainGroupI = diseaseMainGroupI;
    }

    /**
     * Get the disease main group II (not used in the current csf-pr-2.0)
     *
     * @return diseaseMainGroupII
     */
    public String getDiseaseMainGroupII() {
        return diseaseMainGroupII;
    }

    /**
     * Set the disease main group II (not used in the current csf-pr-2.0)
     *
     * @param diseaseMainGroupII
     */
    public void setDiseaseMainGroupII(String diseaseMainGroupII) {
        this.diseaseMainGroupII = diseaseMainGroupII;
    }

    /**
     * Get the Disease sub group II (currently used in the system)
     *
     * @return activeDiseaseSubGroupII
     */
    public String getActiveDiseaseSubGroupII() {
        return activeDiseaseSubGroupII;
    }

    /**
     * Set the Disease sub group II (currently used in the system)
     *
     * @param activeDiseaseSubGroupII
     */
    public void setActiveDiseaseSubGroupII(String activeDiseaseSubGroupII) {
        this.activeDiseaseSubGroupII = activeDiseaseSubGroupII;
    }

    /**
     * Get the Disease sub group I (currently used in the system)
     *
     * @return activeDiseaseSubGroupI
     */
    public String getActiveDiseaseSubGroupI() {
        return activeDiseaseSubGroupI;
    }

    /**
     * Set the Disease sub group I (currently used in the system)
     *
     * @param activeDiseaseSubGroupI
     */
    public void setActiveDiseaseSubGroupI(String activeDiseaseSubGroupI) {
        this.activeDiseaseSubGroupI = activeDiseaseSubGroupI;
    }

    /**
     * Check if it is same comparison (same or flipped)
     *
     * @param comparisonTitle subgroup name__disease category
     * @return yes/no
     */
    public boolean checkSameComparison(String comparisonTitle) {
        return comparisonTitle.equalsIgnoreCase(activeDiseaseSubGroupI + "__" + diseaseCategory) || comparisonTitle.equalsIgnoreCase(activeDiseaseSubGroupII + "__" + diseaseCategory);

    }

    /**
     * Get the comparison title
     *
     * @param key
     * @return
     */
    public String getValLabel(String key) {
        if (key.equalsIgnoreCase(activeDiseaseSubGroupI + "__" + diseaseCategory)) {
            return activeDiseaseSubGroupII + "__" + diseaseCategory;
        } else if (key.equalsIgnoreCase(activeDiseaseSubGroupII + "__" + diseaseCategory)) {
            return activeDiseaseSubGroupI + "__" + diseaseCategory;
        } else {
            return null;
        }

    }

    /**
     * Get Disease Style name at CSS file
     *
     * @return diseaseStyleName
     */
    public String getDiseaseStyleName() {
        return diseaseStyleName;
    }

    /**
     * Set Disease Style name at CSS file
     *
     * @param diseaseStyleName
     */
    public void setDiseaseStyleName(String diseaseStyleName) {
        this.diseaseStyleName = diseaseStyleName;
    }

}
