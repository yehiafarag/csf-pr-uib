package no.uib.probe.csf.pr.touch.logic.beans;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Yehia Farag
 *
 * this class represents disease comparison with all its information
 */
public class QuantDiseaseGroupsComparison implements Serializable, Comparable<QuantDiseaseGroupsComparison> {

    private String comparisonHeader;
    private String oreginalComparisonHeader;
    private Map<Integer,QuantDatasetObject> datasetMap;
    private String diseaseCategoryColor;
    private boolean switchable = true;

    public String getDiseaseCategoryStyle() {
        return diseaseCategoryStyle;
    }

    public void setDiseaseCategoryStyle(String diseaseCategoryStyle) {
        this.diseaseCategoryStyle = diseaseCategoryStyle;
    }
     private String diseaseCategoryStyle;
    private boolean useCustomRowHeaderToSort;

    public Map<Integer, QuantDatasetObject> getDatasetMap() {
        return datasetMap;
    }

    public void setDatasetMap(Map<Integer, QuantDatasetObject> datasetMap) {
        this.datasetMap = datasetMap;
    }
    private boolean useCustomColumnHeaderToSort;
    private String comparisonFullName;
    private String diseaseCategory;

    private Map<String, QuantComparisonProtein> quantComparisonProteinMap;
    private Map<Integer,Set<QuantComparisonProtein>>proteinsByTrendMap; 

    public Map<Integer, Set<QuantComparisonProtein>> getProteinsByTrendMap() {
        return proteinsByTrendMap;
    }

    public void setProteinsByTrendMap(Map<Integer, Set<QuantComparisonProtein>> proteinsByTrendMap) {
        this.proteinsByTrendMap = proteinsByTrendMap;
    }

    public String getDiseaseCategory() {
        return diseaseCategory;
    }

    public void setDiseaseCategory(String diseaseCategory) {
        this.diseaseCategory = diseaseCategory;
    }

    public void setComparisonFullName(String comparisonFullName) {
        this.comparisonFullName = comparisonFullName;
    }

    public QuantDiseaseGroupsComparison() {
    }

    public String getComparisonFullName() {
        return comparisonFullName;
    }
//
//    public void switchComparison() {
//        comparisonHeader = comparisonHeader.split(" / ")[1] + " / " + comparisonHeader.split(" / ")[0];
//        comparisonFullName = comparisonFullName.split(" / ")[1] + " / " + comparisonFullName.split(" / ")[0];
//
//    }

    public String getDiseaseCategoryColor() {
        return diseaseCategoryColor;
    }

    public void setDiseaseCategoryColor(String diseaseCategoryColor) {
        this.diseaseCategoryColor = diseaseCategoryColor;
    }

    /**
     *
     * @return
     */
    public String getComparisonHeader() {
        return comparisonHeader;
    }

    public String getOreginalComparisonHeader() {

        return oreginalComparisonHeader;
    }

    public void resetComparisonHeader() {
        if (!comparisonHeader.equalsIgnoreCase(oreginalComparisonHeader)) {
            comparisonHeader = oreginalComparisonHeader;
//            quantComparisonProteinMap = null;
        }

    }

    /**
     *
     * @param comparisonHeader
     */
    public void setComparisonHeader(String comparisonHeader) {
        this.comparisonHeader = comparisonHeader;

    }

    public void setOreginalComparisonHeader(String oreginalComparisonHeader) {
        this.oreginalComparisonHeader = oreginalComparisonHeader;

    }

 
    /**
     *
     * @return
     */
    public Map<String, QuantComparisonProtein> getQuantComparisonProteinMap() {
        return quantComparisonProteinMap;
    }

    /**
     *
     * @param quantComparisonProteinMap
     */
    public void setQuantComparisonProteinMap(Map<String, QuantComparisonProtein> quantComparisonProteinMap) {
        this.quantComparisonProteinMap = quantComparisonProteinMap;
    }

    @Override
    public int compareTo(QuantDiseaseGroupsComparison t) {
        return this.comparisonHeader.compareTo(t.comparisonHeader);
    }

    public boolean isUseCustomRowHeaderToSort() {
        return useCustomRowHeaderToSort;
    }

    public void setUseCustomRowHeaderToSort(boolean useCustomRowHeaderToSort) {
        this.useCustomRowHeaderToSort = useCustomRowHeaderToSort;
    }

    public boolean isUseCustomColumnHeaderToSort() {
        return useCustomColumnHeaderToSort;
    }

    public void setUseCustomColumnHeaderToSort(boolean useCustomColumnHeaderToSort) {
        this.useCustomColumnHeaderToSort = useCustomColumnHeaderToSort;
    }

//    private void swichProteinsTrend() {
//
//        for(QuantComparisonProtein prot :quantComparisonProteinMap.values()){
//            prot.
//        
//        
//        }
//    }

    public boolean isSwitchable() {
        return switchable;
    }

    public void setSwitchable(boolean switchable) {
        this.switchable = switchable;
    }

    @Override
    public int hashCode() {
        return super.hashCode(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final QuantDiseaseGroupsComparison other = (QuantDiseaseGroupsComparison) obj;
        if (!Objects.equals(this.comparisonHeader, other.comparisonHeader)) {
            return false;
        }
        return true;
    }
    
    
    

}
