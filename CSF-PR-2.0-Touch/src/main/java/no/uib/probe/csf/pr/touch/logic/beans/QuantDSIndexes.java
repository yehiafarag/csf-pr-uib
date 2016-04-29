/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.logic.beans;

import java.io.Serializable;

/**
 *
 * @author Yehia Farag
 * 
 */
public class QuantDSIndexes implements Serializable{

    private int [] indexes;
    private int value;

    /**
     *
     * @return
     */
    public int[] getIndexes() {
        return indexes;
    }

    /**
     *
     * @param indexes
     */
    public void setIndexes(int[] indexes) {
        this.indexes = indexes;
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
