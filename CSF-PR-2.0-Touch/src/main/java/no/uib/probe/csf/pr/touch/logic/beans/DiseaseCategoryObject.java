/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.logic.beans;

import java.awt.Color;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Yehia Farag
 * 
 * this class represent the information required for the disease category like color, name..etc
 */
public class DiseaseCategoryObject implements Serializable{
    
    private String diseaseCategory;
    private Color diseaseAwtColor;
    private String diseaseHashedColor;
    private String diseaseStyleName;
    private int datasetNumber;
    private Map<String,String> diseaseSubGroups;

    
    
    
    
    
    
    
    public Map<String,String> getDiseaseSubGroups() {
        return diseaseSubGroups;
    }

    public void setDiseaseSubGroups(Map<String,String> diseaseSubGroups) {
        this.diseaseSubGroups = diseaseSubGroups;
    }

    public int getDatasetNumber() {
        return datasetNumber;
    }

    public void setDatasetNumber(int datasetNumber) {
        this.datasetNumber = datasetNumber;
    }

    public String getDiseaseStyleName() {
        return diseaseStyleName;
    }

    public void setDiseaseStyleName(String diseaseStyleName) {
        this.diseaseStyleName = diseaseStyleName;
    }

    public String getDiseaseCategory() {
        return diseaseCategory;
    }

    public void setDiseaseCategory(String diseaseCategory) {
        this.diseaseCategory = diseaseCategory;
    }

    public Color getDiseaseAwtColor() {
        return diseaseAwtColor;
    }

    public void setDiseaseAwtColor(Color diseaseAwtColor) {
        this.diseaseAwtColor = diseaseAwtColor;
    }

    public String getDiseaseHashedColor() {
        return diseaseHashedColor;
    }

    public void setDiseaseHashedColor(String diseaseHashedColor) {
        this.diseaseHashedColor = diseaseHashedColor;
    }

    @Override
    public String toString() {
        return this.diseaseCategory;
    }
    
}
