/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.logic.beans;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author Yehia Farag
 * 
 */
public class QuantDSIndexes implements Serializable{

    private int value;
    private Map<Integer,QuantDatasetObject> datasetMap;

    public Map<Integer, QuantDatasetObject> getDatasetMap() {
        return datasetMap;
    }

    public void setDatasetMap(Map<Integer, QuantDatasetObject> datasetMap) {
        this.datasetMap = datasetMap;
    }

   

    /**
     *
     * @return
     */
    public int getValue() {
        return value;
    }

    /**
     *
     * @param value
     */
    public void setValue(int value) {
        this.value = value;
    }
}
