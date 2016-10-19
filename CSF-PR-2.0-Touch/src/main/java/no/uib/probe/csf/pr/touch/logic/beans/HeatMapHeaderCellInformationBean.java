package no.uib.probe.csf.pr.touch.logic.beans;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Yehia Farag
 *
 * this class provides all requires information for heat map cell
 */
public class HeatMapHeaderCellInformationBean implements Serializable {


    /*
     *Disease category name (MS, AD, PD..etc)
     */
    private String diseaseCategory;
    /*
     *The Disease sub group (currently used in the system)
     */
    private String diseaseGroupName;
    /*
     *Disease Style name at CSS file
     */
    private String diseaseStyleName;
    /*
     *Disease HTML Color code
     */
    private String diseaseColor;
    /*
     *Disease sub group full name
     */
    private String diseaseGroupFullName;
    /*
     *Disease sub group original name (from publication)
     */
    private String diseaseGroupOreginalName;

    /**
     * Get disease sub group original name (from publication)
     *
     * @return diseaseGroupOreginalName
     */
    public String getDiseaseGroupOreginalName() {
        return diseaseGroupOreginalName;
    }

    /**
     * Set disease sub group original name (from publication)
     *
     * @param diseaseGroupOreginalName
     */
    public void setDiseaseGroupOreginalName(String diseaseGroupOreginalName) {
        this.diseaseGroupOreginalName = diseaseGroupOreginalName;
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
     * Get disease sub group (currently used in the system)
     *
     * @return diseaseGroupName
     */
    public String getDiseaseGroupName() {
        return diseaseGroupName;
    }

    /**
     * Set disease sub group (currently used in the system)
     *
     * @param diseaseGroupName
     */
    public void setDiseaseGroupName(String diseaseGroupName) {
        this.diseaseGroupName = diseaseGroupName;
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
     * Get disease category HTML color code
     *
     * @return diseaseHashedColor
     */
    public String getDiseaseColor() {
        return diseaseColor;
    }

    /**
     * Set disease category HTML color code
     *
     * @param diseaseColor
     */
    public void setDiseaseColor(String diseaseColor) {
        this.diseaseColor = diseaseColor;
    }

    @Override
    public String toString() {
        return diseaseGroupName + "__" + diseaseCategory;
    }

    /**
     * Get disease sub group full name
     *
     * @return diseaseGroupFullName
     */
    public String getDiseaseGroupFullName() {
        if (diseaseGroupFullName == null) {
            return diseaseGroupName;
        }
        return diseaseGroupFullName;
    }

    /**
     * Set disease sub group full name
     *
     * @param diseaseGroupFullName
     */
    public void setDiseaseGroupFullName(String diseaseGroupFullName) {
        this.diseaseGroupFullName = diseaseGroupFullName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HeatMapHeaderCellInformationBean other = (HeatMapHeaderCellInformationBean) obj;
        if (!Objects.equals(this.diseaseCategory, other.diseaseCategory)) {
            return false;
        }
        return Objects.equals(this.diseaseGroupName, other.diseaseGroupName);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + Objects.hashCode(this.diseaseCategory);
        hash = 19 * hash + Objects.hashCode(this.diseaseGroupName);
        return hash;
    }

}
