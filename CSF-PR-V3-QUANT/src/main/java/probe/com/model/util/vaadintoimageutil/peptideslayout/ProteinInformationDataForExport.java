/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.model.util.vaadintoimageutil.peptideslayout;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author yfa041
 */
public class ProteinInformationDataForExport implements Serializable{
    private String sequence;
    private String comparisonsTitle ;
    private Map<String,StudyInfoData> studies;

    public String getComparisonsTitle() {
        return comparisonsTitle;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public Map<String, StudyInfoData> getStudies() {
        return studies;
    }

    public void setStudies(Map<String, StudyInfoData> studies) {
        this.studies = studies;
    }

    public void setComparisonsTitle(String comparisonsTitle) {
        this.comparisonsTitle = comparisonsTitle;
    }

    
}
