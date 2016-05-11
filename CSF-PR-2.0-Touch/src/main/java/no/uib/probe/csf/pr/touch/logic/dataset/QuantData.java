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

    private LinkedHashSet<HeatMapHeaderCellInformationBean> activeRowIds, oreginalRowIds;
    private LinkedHashSet<HeatMapHeaderCellInformationBean> activeColumnIds, oreginalColumnIds;

    public LinkedHashSet<HeatMapHeaderCellInformationBean> getOreginalRowIds() {
        return oreginalRowIds;
    }

    public void setOreginalRowIds(LinkedHashSet<HeatMapHeaderCellInformationBean> oreginalRowIds) {
        this.oreginalRowIds = oreginalRowIds;
    }

    public LinkedHashSet<HeatMapHeaderCellInformationBean> getOreginalColumnIds() {
        return oreginalColumnIds;
    }

    public void setOreginalColumnIds(LinkedHashSet<HeatMapHeaderCellInformationBean> oreginalColumnIds) {
        this.oreginalColumnIds = oreginalColumnIds;
    }
    private String diseaseCategory;
    private Set<DiseaseGroupComparison> diseaseComparisonSet;
    private boolean[] activeHeaders;

    public LinkedHashSet<HeatMapHeaderCellInformationBean> getActiveRowIds() {
        if (activeRowIds == null) {
            return oreginalRowIds;
        }
        return activeRowIds;
    }

    public void setActiveRowIds(LinkedHashSet<HeatMapHeaderCellInformationBean> activeRowIds) {
        this.activeRowIds = activeRowIds;
    }

    public LinkedHashSet<HeatMapHeaderCellInformationBean> getActiveColumnIds() {
        if (activeColumnIds == null) {
            return oreginalColumnIds;
        }
        return activeColumnIds;
    }

    public void setActiveColumnIds(LinkedHashSet<HeatMapHeaderCellInformationBean> activeColumnIds) {
        this.activeColumnIds = activeColumnIds;
    }

    public Set<DiseaseGroupComparison> getDiseaseGroupComparisonsSet() {
        return diseaseComparisonSet;
    }

    public void setDiseaseComparisonSet(Set<DiseaseGroupComparison> diseaseComparisonSet) {
        this.diseaseComparisonSet = diseaseComparisonSet;
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
