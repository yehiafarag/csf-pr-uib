/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.logic.beans;

import com.vaadin.ui.Label;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Yehia Farag
 *
 * This class contains all quant comparison protein information
 */
public class QuantComparisonProtein implements Serializable {

    private String proteinAccssionNumber;
    private String protName;
    private final int uniqueId;
    private String sequence;
    private Set<QuantPeptide> quantPeptidesList;
    
    
    
    
    

    public Set<QuantPeptide> getQuantPeptidesList() {
        return quantPeptidesList;
    }

    public void setQuantPeptidesList(Set<QuantPeptide> quantPeptidesList) {
        this.quantPeptidesList = quantPeptidesList;
    }
    private final Map<String, List<Integer>> patientsNumToTrindMap = new HashMap<String, List<Integer>>();
    private final Map<String, List<Integer>> patientsNumToDSIDMap = new HashMap<String, List<Integer>>();
    private Map<String, QuantProtein> dsQuantProteinsMap = new HashMap<String, QuantProtein>();
    private Integer highSignificant = 0;
    private Integer lowSignificant = 0;
    private int stableSignificant = 0;
    private int noValueprovided = 0;
    private int stable = 0;
    private double penalty = 0.0;
    private String key;
    private Label upLabel;
    private Label downLabel;
    private final int total;
    private final DecimalFormat df;
    private Double trendValue = 0.0;
    private double cellValue;
    private final QuantDiseaseGroupsComparison quantComparison;
     private boolean updated = false;
     private String url;
     private double overallCellPercentValue;

    public void setDsQuantProteinsMap(Map<String, QuantProtein> dsQuantProteinsMap) {
        this.dsQuantProteinsMap = dsQuantProteinsMap;
    }

    public Map<String, QuantProtein> getDsQuantProteinsMap() {
        return dsQuantProteinsMap;
    }

    public String getProteinAccssionNumber() {
        return proteinAccssionNumber;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public int getSignificantTrindCategory() {
        return significantTrindCategory;
    }

    private int significantTrindCategory;

    public QuantComparisonProtein(int total, QuantDiseaseGroupsComparison quantComparison, int uniqueId) {
        this.quantComparison = quantComparison;
        this.uniqueId = uniqueId;
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        otherSymbols.setGroupingSeparator('.');
        df = new DecimalFormat("#.#", otherSymbols);
        this.total = total;

        patientsNumToTrindMap.put("up", new ArrayList<>());
        patientsNumToTrindMap.put("equal", new ArrayList<>());
        patientsNumToTrindMap.put("down", new ArrayList<>());
        patientsNumToDSIDMap.put("up", new ArrayList<>());
        patientsNumToDSIDMap.put("equal", new ArrayList<>());
        patientsNumToDSIDMap.put("down", new ArrayList<>());
        patientsNumToTrindMap.put("noValueProvided", new ArrayList<>());
        patientsNumToDSIDMap.put("noValueProvided", new ArrayList<>());
    }
    
    /**
     *
     * @param patientsNumber
     * @param dsID
     * @param significant
     */
    public void addDown(int patientsNumber, int dsID, boolean significant) {

        if (significant) {
            trendValue -= (double) 1;
            this.lowSignificant += 1;
            List<Integer> downList = this.patientsNumToTrindMap.get("down");
            downList.add(patientsNumber);
            this.patientsNumToTrindMap.put("down", downList);

            List<Integer> downDsList = this.patientsNumToDSIDMap.get("down");
            downDsList.add(dsID);
            this.patientsNumToDSIDMap.put("down", downDsList);

        } else {

            addStable(patientsNumber, dsID);
        }
    }
    /**
     *
     * @param patNumber
     * @param dsID
     */
    public void addStable(int patNumber, int dsID) {
        penalty += 0.5;
        this.stable += 1;
        List<Integer> notRegList = this.patientsNumToTrindMap.get("equal");
        notRegList.add(patNumber);
        this.patientsNumToTrindMap.put("equal", notRegList);
        List<Integer> notRegDsList = this.patientsNumToDSIDMap.get("equal");
        notRegDsList.add(dsID);
        this.patientsNumToDSIDMap.put("equal", notRegDsList);
    }
     /**
     *
     * @param patientsNumber
     * @param dsID
     * @param significant
     */
    public void addUp(int patientsNumber, int dsID, boolean significant) {

        if (significant) {
            trendValue += (double) 1;
            this.highSignificant += 1;
            List<Integer> upList = this.patientsNumToTrindMap.get("up");
            upList.add(patientsNumber);
            this.patientsNumToTrindMap.put("up", upList);

            List<Integer> upDsList = this.patientsNumToDSIDMap.get("up");
            upDsList.add(dsID);
            this.patientsNumToDSIDMap.put("up", upDsList);

        } else {
            addStable(patientsNumber, dsID);
        }

    }

    public void setProteinAccssionNumber(String proteinAccssionNumber) {
        this.proteinAccssionNumber = proteinAccssionNumber;
    }

    public void setProtName(String protName) {
        this.protName = protName;
    }
     /**
     *
     * @param patientsNumber
     * @param dsID
     */
    public void addNoValueProvided(int patientsNumber, int dsID) {

        this.noValueprovided += 1;
        List<Integer> noValueProvidedList = this.patientsNumToTrindMap.get("noValueProvided");
        noValueProvidedList.add(patientsNumber);
        this.patientsNumToTrindMap.put("noValueProvided", noValueProvidedList);

        List<Integer> noValueProvidedDsList = this.patientsNumToDSIDMap.get("noValueProvided");
        noValueProvidedDsList.add(dsID);
        this.patientsNumToDSIDMap.put("noValueProvided", noValueProvidedDsList);

    }

    public void setUrl(String url) {
        this.url = url;
    }
     /**
     *
     * @return
     */
    public int getSignificantUp() {
        return highSignificant;
    }

    /**
     *
     */
    public void finalizeQuantData() {
        if (updated) {
            return;
        }

        int dcounter = 0;
        int subTotal = this.total;     

        Double v1;
        if (highSignificant == lowSignificant.intValue()) {
            v1 = trendValue;
        } else if (trendValue > 0) {
            double factor = penalty;
            v1 = trendValue - factor;
            v1 = Math.max(v1, 0) + ((double) (highSignificant - lowSignificant) / 10.0);
        } else {
            double factor = penalty;
            v1 = trendValue + factor;
            v1 = Math.min(v1, 0) + ((double) (highSignificant - lowSignificant) / 10.0);
        }
        if (v1 > 0) {
            cellValue = Math.min(v1, 1);
        } else if (v1 < 0) {
            cellValue = Math.max(v1, -1);
        }
        int existStudiesNumber = lowSignificant + highSignificant + stable;
        if (cellValue > 0) {           
            if (stable > 0 || lowSignificant > 0) {
                significantTrindCategory = 3;
                overallCellPercentValue = Math.max((((double) (highSignificant - lowSignificant)) / (double) existStudiesNumber) * 100.0, 5.0);

            } else {
                significantTrindCategory = 4;
                overallCellPercentValue = 100;
            }

//            trendataLayoutWrapper.setExpandRatio(stableLayoutWrapper, ((float) (subTotal - dcounter) / subTotal));

        } else if (cellValue == 0) {
//            overall = "Equal (" + cellValue + ")";
            significantTrindCategory = 2;
            overallCellPercentValue = 0;
//            updateComponentLocation(emptyLayout, stableLayout, noValueProvidedLayout);
//            if (!downLayout.isVisible() && !upLayout.isVisible()) {
//                if (subTotal == stable) {
//                    trendataLayoutWrapper.setExpandRatio(stableLayoutWrapper, ((float) (subTotal - dcounter) / subTotal));
//                } else {
//                    trendataLayoutWrapper.setExpandRatio(stableLayoutWrapper, ((float) (subTotal - dcounter) / subTotal));
//                    emptyLayout.setVisible(false);
//                    stableLayoutWrapper.setStyleName("empty");
//
//                }
//            } else {
//                trendataLayoutWrapper.setExpandRatio(stableLayoutWrapper, ((float) (subTotal - dcounter) / subTotal));
//
//            }

        } else {
//            updateComponentLocation(stableLayout, noValueProvidedLayout, emptyLayout);
//            overall = "Decreased (" + cellValue + ")";
            if (stable > 0 || highSignificant > 0) {
                significantTrindCategory = 1;
                overallCellPercentValue = Math.max((((double) (lowSignificant - highSignificant)) / (double) existStudiesNumber) * 100.0, 5.0);
                overallCellPercentValue = -1 * overallCellPercentValue;
            } else {
                significantTrindCategory = 0;
                overallCellPercentValue = -100;
            }
//            trendataLayoutWrapper.setExpandRatio(stableLayoutWrapper, ((float) (subTotal - dcounter) / subTotal) + 1);
        }
        if (stable == 0 && highSignificant == 0 && lowSignificant == 0 && noValueprovided > 0) {
            significantTrindCategory = 5;
            overallCellPercentValue = 0;
        }
//        if ((((double) highSignificant / (double) subTotal) * 100.0) < 100.0) {
//            upLayout.addStyleName("marginleft");
//
//        }
//        if ((((double) lowSignificant / (double) subTotal) * 100.0) < 100.0) {
//            downLayout.addStyleName("marginright");
//
//        }
//
//        this.setDescription("Protein value: " + overallCellPercentValue + "%<br/>#Decreased: " + lowSignificant + "<br/>#Equal : " + stable + "<br/>#Increased: " + highSignificant + "<br/>Overall trend " + overall);

    }
}
