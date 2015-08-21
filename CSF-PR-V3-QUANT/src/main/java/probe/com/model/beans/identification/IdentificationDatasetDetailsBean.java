/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package probe.com.model.beans.identification;

import java.io.Serializable;

/**
 *
 * @author Yehia Farag
 */
public class IdentificationDatasetDetailsBean implements Serializable{
   
private String name,species, sampleType, sampleProcessing, instrumentType, fragMode;

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public String getSpecies() {
        return species;
    }

    /**
     *
     * @param species
     */
    public void setSpecies(String species) {
        this.species = species;
    }

    /**
     *
     * @return
     */
    public String getSampleType() {
        return sampleType;
    }

    /**
     *
     * @param sampleType
     */
    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    /**
     *
     * @return
     */
    public String getSampleProcessing() {
        return sampleProcessing;
    }

    /**
     *
     * @param sampleProcessing
     */
    public void setSampleProcessing(String sampleProcessing) {
        this.sampleProcessing = sampleProcessing;
    }

    /**
     *
     * @return
     */
    public String getInstrumentType() {
        return instrumentType;
    }

    /**
     *
     * @param instrumentType
     */
    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }

    /**
     *
     * @return
     */
    public String getFragMode() {
        return fragMode;
    }

    /**
     *
     * @param fragMode
     */
    public void setFragMode(String fragMode) {
        this.fragMode = fragMode;
    }
    
}
