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
import no.uib.probe.csf.pr.touch.logic.beans.HeatMapHeaderCellInformationBean;

/**
 *
 * @author Yehia Farag
 */
public class QuantData implements Serializable {

    private LinkedHashSet<HeatMapHeaderCellInformationBean> activeRowIds;
    private LinkedHashSet<HeatMapHeaderCellInformationBean> activeColumnIds;
    private String diseaseCategory;
    private Set<DiseaseGroupComparison> diseaseGroupArry;
    private boolean[] activeHeaders;

    

    public LinkedHashSet<HeatMapHeaderCellInformationBean> getActiveRowIds() {
        return activeRowIds;
    }

    public void setActiveRowIds(LinkedHashSet<HeatMapHeaderCellInformationBean> activeRowIds) {
        this.activeRowIds = activeRowIds;
    }

    public LinkedHashSet<HeatMapHeaderCellInformationBean> getActiveColumnIds() {
        return activeColumnIds;
    }

    public void setActiveColumnIds(LinkedHashSet<HeatMapHeaderCellInformationBean> activeColumnIds) {
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

    public boolean[] getActiveHeaders() {
        return activeHeaders;
    }

    public void setActiveHeaders(boolean[] activeHeaders) {
        this.activeHeaders = activeHeaders;
    }

}
