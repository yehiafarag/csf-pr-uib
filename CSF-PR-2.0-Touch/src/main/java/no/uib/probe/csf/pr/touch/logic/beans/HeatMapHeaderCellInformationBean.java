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

    private String diseaseCategory;
    private String diseaseGroupName;
    private String diseaseStyleName;
    private String diseaseColor;
    private String diseaseGroupFullName;
    private String diseaseGroupOreginalName;

    public String getDiseaseGroupOreginalName() {
        return diseaseGroupOreginalName;
    }

    public void setDiseaseGroupOreginalName(String diseaseGroupOreginalName) {
        this.diseaseGroupOreginalName = diseaseGroupOreginalName;
    }

    public String getDiseaseCategory() {
        return diseaseCategory;
    }

    public void setDiseaseCategory(String diseaseCategory) {
        this.diseaseCategory = diseaseCategory;
    }

    public String getDiseaseGroupName() {
        return diseaseGroupName;
    }

    public void setDiseaseGroupName(String diseaseGroupName) {
        this.diseaseGroupName = diseaseGroupName;
    }

    public String getDiseaseStyleName() {
        return diseaseStyleName;
    }

    public void setDiseaseStyleName(String diseaseStyleName) {
        this.diseaseStyleName = diseaseStyleName;
    }

    public String getDiseaseColor() {
        return diseaseColor;
    }

    public void setDiseaseColor(String diseaseColor) {
        this.diseaseColor = diseaseColor;
    }

    @Override
    public String toString() {
        return diseaseGroupName + "__" + diseaseCategory;
    }

    public String getDiseaseGroupFullName() {
        if (diseaseGroupFullName == null) {
            return diseaseGroupName;
        }
        return diseaseGroupFullName;
    }

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
        if (!Objects.equals(this.diseaseGroupName, other.diseaseGroupName)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + Objects.hashCode(this.diseaseCategory);
        hash = 19 * hash + Objects.hashCode(this.diseaseGroupName);
        return hash;
    }

}
