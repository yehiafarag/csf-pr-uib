package no.uib.probe.csf.pr.touch.logic.beans;

import java.io.Serializable;

/**
 * This class represents disease group comparison that has all comparison
 * information
 *
 * @author Yehia Farag
 */
public class DiseaseGroupComparison implements Serializable {

    /**
     * Disease category name (MS, AD, PD..etc).
     */

    private String diseaseCategory;

    /**
     * Disease Style name at CSS file.
     */
    private String diseaseStyleName;

    /**
     * The dataset index in the database.
     */
    private int quantDatasetIndex;

    /**
     * The Disease main group I (not used in the current CSF-PR 2.0).
     */
    private String diseaseMainGroupI;

    /**
     * The Disease main group II (not used in the current CSF-PR 2.0).
     */
    private String diseaseMainGroupII;

    /**
     * The Disease sub group I (publication name).
     */
    private String originalDiseaseSubGroupI;
    /**
     * The Disease sub group II (publication name).
     */
    private String originalDiseaseSubGroupII;

    /**
     * The Disease sub group I (currently used in the system).
     */
    private String activeDiseaseSubGroupI;
    /**
     * The Disease sub group II (currently used in the system).
     */
    private String activeDiseaseSubGroupII;

    /**
     * Get disease category (MS,AD,PD...etc).
     *
     * @return diseaseCategory disease category name
     */
    public String getDiseaseCategory() {
        return diseaseCategory;
    }

    /**
     * Set disease category (MS,AD,PD...etc)
     *
     * @param diseaseCategory disease category name.
     */
    public void setDiseaseCategory(String diseaseCategory) {
        this.diseaseCategory = diseaseCategory;
    }

    /**
     * Get the disease sub group I (publication name)
     *
     * @return originalDiseaseSubGroupI publication disease sub-group 1.
     */
    public String getOriginalDiseaseSubGroupI() {
        return originalDiseaseSubGroupI;
    }

    /**
     * Set the disease sub group I (publication name)
     *
     * @param originalDiseaseSubGroupI publication disease sub-group 1.
     */
    public void setOriginalDiseaseSubGroupI(String originalDiseaseSubGroupI) {
        this.originalDiseaseSubGroupI = originalDiseaseSubGroupI;
    }

    /**
     * Get the disease sub group II (publication name)
     *
     * @return originalDiseaseSubGroupII publication disease sub-group 2.
     */
    public String getOriginalDiseaseSubGroupII() {
        return originalDiseaseSubGroupII;
    }

    /**
     * Set the disease sub group II (publication name)
     *
     * @param originalDiseaseSubGroupII publication disease sub-group 2.
     */
    public void setOriginalDiseaseSubGroupII(String originalDiseaseSubGroupII) {
        this.originalDiseaseSubGroupII = originalDiseaseSubGroupII;
    }

    /**
     * Get the disease main group I (not used in the current CSF-PR 2.0)
     *
     * @return diseaseMainGroupI Main disease group 1.
     */
    public String getDiseaseMainGroupI() {
        return diseaseMainGroupI;
    }

    /**
     * Get he dataset index in the database
     *
     * @return quantDatasetIndex dataset index in the database.
     */
    public int getQuantDatasetIndex() {
        return quantDatasetIndex;
    }

    /**
     * Set the dataset index in the database
     *
     * @param quantDatasetIndex Dataset index in the database.
     */
    public void setQuantDatasetIndex(int quantDatasetIndex) {
        this.quantDatasetIndex = quantDatasetIndex;
    }

    /**
     * Set the disease main group I (not used in the current CSF-PR 2.0)
     *
     * @param diseaseMainGroupI Main disease group 1.
     */
    public void setDiseaseMainGroupI(String diseaseMainGroupI) {
        this.diseaseMainGroupI = diseaseMainGroupI;
    }

    /**
     * Get the disease main group II (not used in the current CSF-PR 2.0)
     *
     * @return diseaseMainGroupII Main disease group 2.
     */
    public String getDiseaseMainGroupII() {
        return diseaseMainGroupII;
    }

    /**
     * Set the disease main group II (not used in the current CSF-PR 2.0)
     *
     * @param diseaseMainGroupII Main disease group 2.
     */
    public void setDiseaseMainGroupII(String diseaseMainGroupII) {
        this.diseaseMainGroupII = diseaseMainGroupII;
    }

    /**
     * Get the current disease sub group II name (currently used in the system)
     *
     * @return activeDiseaseSubGroupII Current name for disease sub-group 2.
     */
    public String getActiveDiseaseSubGroupII() {
        return activeDiseaseSubGroupII;
    }

    /**
     * Set the current disease sub group II name (currently used in the system)
     *
     * @param activeDiseaseSubGroupII Current name for disease sub-group 2.
     */
    public void setActiveDiseaseSubGroupII(String activeDiseaseSubGroupII) {
        this.activeDiseaseSubGroupII = activeDiseaseSubGroupII;
    }

    /**
     * Get the current disease sub group I name (currently used in the system)
     *
     * @return activeDiseaseSubGroupI Current name for disease sub-group 1.
     */
    public String getActiveDiseaseSubGroupI() {
        return activeDiseaseSubGroupI;
    }

    /**
     * Set the current disease sub group I name (currently used in the system)
     *
     * @param activeDiseaseSubGroupI Current name for disease sub-group 1.
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
     * @param key disease sub group name(currently used in the system)
     * @return the current update disease group with disease category.
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
     * Get disease Style name at CSS file
     *
     * @return diseaseStyleName CSS disease style name.
     */
    public String getDiseaseStyleName() {
        return diseaseStyleName;
    }

    /**
     * Set Disease Style name at CSS file
     *
     * @param diseaseStyleName CSS disease style name.
     */
    public void setDiseaseStyleName(String diseaseStyleName) {
        this.diseaseStyleName = diseaseStyleName;
    }

}
