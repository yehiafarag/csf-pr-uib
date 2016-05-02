/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.logic.dataset;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseGroupComparison;

/**
 *
 * @author Yehia Farag
 */
public class QuantData implements Serializable {

    private LinkedHashSet<String> activeRowIds;
    private LinkedHashSet<String> activeColumnIds;
    private String diseaseCategory;
    Set<DiseaseGroupComparison>diseaseGroupArry;

    public LinkedHashSet<String> getActiveRowIds() {
        return activeRowIds;
    }

    public void setActiveRowIds(LinkedHashSet<String> activeRowIds) {
        this.activeRowIds = activeRowIds;
    }

    public LinkedHashSet<String> getActiveColumnIds() {
        return activeColumnIds;
    }

    public void setActiveColumnIds(LinkedHashSet<String> activeColumnIds) {
        this.activeColumnIds = activeColumnIds;
    }

    public Set<DiseaseGroupComparison> getDiseaseGroupComparisonsSet() {
        return diseaseGroupArry;
    }

    public void setDiseaseGroupArry(Set<DiseaseGroupComparison> diseaseGroupArry) {
        this.diseaseGroupArry = diseaseGroupArry;
    }

    

    public String getDiseaseCategory() {
        return diseaseCategory;
    }

    public void setDiseaseCategory(String diseaseCategory) {
        this.diseaseCategory = diseaseCategory;
    }

}
