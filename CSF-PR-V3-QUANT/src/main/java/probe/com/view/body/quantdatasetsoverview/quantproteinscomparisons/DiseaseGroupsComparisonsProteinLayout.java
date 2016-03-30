/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
import probe.com.model.beans.quant.QuantPeptide;
import probe.com.model.beans.quant.QuantProtein;

/**
 *
 * @author Yehia Farag
 */
public class DiseaseGroupsComparisonsProteinLayout extends HorizontalLayout implements Serializable, Comparable<DiseaseGroupsComparisonsProteinLayout> {
    
    private String proteinAccssionNumber;
    private String protName;
    private final int uniqueId;
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    private String url;
    
    private int significantTrindCategory;
    
    public Object getTableItemId() {
        return tableItemId;
    }
    
    public void setTableItemId(Object tableItemId) {
        this.tableItemId = tableItemId;
    }
    private Object tableItemId;

    /**
     *
     * @return
     */
    public String getSequence() {
        return sequence;
    }

    /**
     *
     * @param sequence
     */
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
    private String sequence;
    private Set<QuantPeptide> quantPeptidesList;
    private final Map<String, List<Integer>> patientsNumToTrindMap = new HashMap<String, List<Integer>>();
    private final Map<String, List<Integer>> patientsNumToDSIDMap = new HashMap<String, List<Integer>>();
    private Map<String, QuantProtein> dsQuantProteinsMap = new HashMap<String, QuantProtein>();
    
    public Map<String, QuantProtein> getDsQuantProteinsMap() {
        return dsQuantProteinsMap;
    }
    
    public void setDsQuantProteinsMap(Map<String, QuantProtein> dsQuantProteinsMap) {
        this.dsQuantProteinsMap = dsQuantProteinsMap;
    }

    /**
     *
     * @return
     */
    public Map<String, List<Integer>> getPatientsNumToTrindMap() {
        return patientsNumToTrindMap;
    }

    /**
     *
     * @return
     */
    public QuantDiseaseGroupsComparison getComparison() {
        return comparison;
    }
    
    private final QuantDiseaseGroupsComparison comparison;

    /**
     *
     * @return
     */
    public String getProtName() {
        return protName;
    }

    /**
     *
     * @param protName
     */
    public void setProtName(String protName) {
        this.protName = protName;
    }
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

    /**
     *
     * @param total
     * @param comparison
     * @param uniqueId
     */
    public DiseaseGroupsComparisonsProteinLayout(int total, QuantDiseaseGroupsComparison comparison, int uniqueId) {
        this.comparison = comparison;
        this.uniqueId = uniqueId;
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        otherSymbols.setGroupingSeparator('.');
        df = new DecimalFormat("#.#", otherSymbols);
        this.total = total;
        upLayout = new VerticalLayout();
        downLayout = new VerticalLayout();
        emptyLayout = new VerticalLayout();
        stableLayout = new VerticalLayout();
        noValueProvidedLayout = new VerticalLayout();
        patientsNumToTrindMap.put("up", new ArrayList<Integer>());
        patientsNumToTrindMap.put("stable", new ArrayList<Integer>());
        patientsNumToTrindMap.put("down", new ArrayList<Integer>());
        patientsNumToDSIDMap.put("up", new ArrayList<Integer>());
        patientsNumToDSIDMap.put("stable", new ArrayList<Integer>());
        patientsNumToDSIDMap.put("down", new ArrayList<Integer>());
        patientsNumToTrindMap.put("noValueProvided", new ArrayList<Integer>());
        patientsNumToDSIDMap.put("noValueProvided", new ArrayList<Integer>());
        
        initLabelLayout();
    }
    
    public void setSignificantTrindCategory(int significantTrindCategory) {
        this.significantTrindCategory = significantTrindCategory;
    }
    
    final VerticalLayout upLayout, downLayout, emptyLayout, stableLayout, noValueProvidedLayout;
    
    private void initLabelLayout() {
        this.setWidth("100%");
        this.setHeight("20px");
        this.setStyleName("pointer");
        this.setSpacing(false);
        this.setMargin(false);
        
        downLabel = new Label();
        downLabel.setWidth("50px");
        downLabel.setHeight("15px");
        downLabel.setContentMode(ContentMode.HTML);
        this.addComponent(downLabel);
        this.setComponentAlignment(downLabel, Alignment.TOP_RIGHT);
        
        downLayout.setHeight("15px");
        downLayout.setStyleName("greenlayout");
        this.addComponent(downLayout);
        this.setComponentAlignment(downLayout, Alignment.MIDDLE_CENTER);

        //        stableLayout.setWidth("100%");
        stableLayout.setHeight("15px");
        stableLayout.setStyleName("lightbluelayout");
        this.addComponent(stableLayout);
        this.setComponentAlignment(stableLayout, Alignment.MIDDLE_CENTER);
        noValueProvidedLayout.setHeight("15px");
        noValueProvidedLayout.setStyleName("novaluelayout");//"empty"
        this.addComponent(noValueProvidedLayout);
        this.setComponentAlignment(noValueProvidedLayout, Alignment.MIDDLE_CENTER);
        emptyLayout.setHeight("15px");
        emptyLayout.setStyleName("empty");//"empty"
        this.addComponent(emptyLayout);
        this.setComponentAlignment(emptyLayout, Alignment.MIDDLE_CENTER);
        
        upLayout.setHeight("15px");
        upLayout.setStyleName("redlayout");
        this.addComponent(upLayout);
        this.setComponentAlignment(upLayout, Alignment.MIDDLE_CENTER);
        upLayout.setCaptionAsHtml(true);
        
        upLabel = new Label();
        upLabel.setContentMode(ContentMode.HTML);
        upLabel.setWidth("50px");
        upLabel.setHeight("15px");
        this.addComponent(upLabel);
        this.setComponentAlignment(upLabel, Alignment.TOP_LEFT);
        
    }

    /**
     *
     * @return
     */
    public double getSignificantCellValue() {
        return cellValue;
    }

    /**
     *
     * @return
     */
    public String getKey() {
        return key;
    }

    /**
     *
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     *
     * @return
     */
    public String getProteinAccssionNumber() {
        return proteinAccssionNumber;
    }

    /**
     *
     * @param proteinAccssionNumber
     */
    public void setProteinAccssionNumber(String proteinAccssionNumber) {
        this.proteinAccssionNumber = proteinAccssionNumber;
    }

    /**
     *
     * @return
     */
    public int getSignificantUp() {
        return highSignificant;
    }
    int counter = 0;

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

    /**
     *
     * @return
     */
    public int getSignificantDown() {
        return lowSignificant;
    }
    
    public int getNoValueprovided() {
        return noValueprovided;
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
     * @return
     */
    public int getStable() {
        return stable;
    }

    /**
     *
     * @param patNumber
     * @param dsID
     */
    public void addStable(int patNumber, int dsID) {
        penalty += 0.5;
        this.stable += 1;
        List<Integer> notRegList = this.patientsNumToTrindMap.get("stable");
        notRegList.add(patNumber);
        this.patientsNumToTrindMap.put("stable", notRegList);
        List<Integer> notRegDsList = this.patientsNumToDSIDMap.get("stable");
        notRegDsList.add(dsID);
        this.patientsNumToDSIDMap.put("stable", notRegDsList);
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
    
    private Double v1 = 0.0;
    
    @Override
    public int compareTo(DiseaseGroupsComparisonsProteinLayout t) {
        
        if (highSignificant.intValue() == lowSignificant.intValue()) {
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
        Double v2 = null;
        if (t.highSignificant.intValue() == t.lowSignificant.intValue()) {
            v2 = t.trendValue;
        } else if (t.trendValue > 0) {
            double factor = t.penalty;
            v2 = t.trendValue - factor;
            v2 = Math.max(v2, 0) + ((double) (t.highSignificant - t.lowSignificant) / 10.0);
        } else {
            double factor = t.penalty;
            v2 = t.trendValue + factor;
            v2 = Math.min(v2, 0) + ((double) (t.highSignificant - t.lowSignificant) / 10.0);
        }
        return (v1).compareTo(v2);
        
    }
    private String fullDownLabelValue = "";
    private String fullUpLabelValue = "";
    private boolean updated = false;
    private double overallCellPercentValue;

    /**
     *
     */
    public void updateLabelLayout() {
        if (updated) {
            return;
        }
        int dcounter = 0;
        int subTotal = this.total;
        fullDownLabelValue = "<font style='float: right;'><strong> " + df.format(((double) lowSignificant / (double) subTotal) * 100.0) + "% &#8595; </strong>&nbsp;</font>";
        downLabel.setValue(fullDownLabelValue);
        fullUpLabelValue = "<font style='float: left;'><strong>&nbsp;&#8593; " + df.format(((double) highSignificant / (double) subTotal) * 100.0) + "%</strong></font>";
        upLabel.setValue(fullUpLabelValue);
        
        Double v1;
        if (highSignificant.intValue() == lowSignificant.intValue()) {
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
        this.setExpandRatio(emptyLayout, ((float) (subTotal - dcounter) / subTotal));
        String overall;
        
        int existStudiesNumber = lowSignificant + highSignificant + stable;
        
        if (cellValue > 0) {
            overall = "High (" + cellValue + ")";
            if (stable > 0 || lowSignificant > 0) {
                significantTrindCategory = 3;
                overallCellPercentValue = Math.max((((double) (highSignificant - lowSignificant)) / (double) existStudiesNumber) * 100.0, 5.0);
            } else {
                significantTrindCategory = 4;
                overallCellPercentValue = 100;
            }
        } else if (cellValue == 0) {
            overall = "Stable (" + cellValue + ")";
            significantTrindCategory = 2;
            overallCellPercentValue = 0;
        } else {
            overall = "Low (" + cellValue + ")";
            if (stable > 0 || highSignificant > 0) {
                significantTrindCategory = 1;
                overallCellPercentValue = Math.max((((double) (lowSignificant - highSignificant)) / (double) existStudiesNumber) * 100.0, 5.0);
                overallCellPercentValue = -1 * overallCellPercentValue;
            } else {
                significantTrindCategory = 0;
                overallCellPercentValue = -100;
            }
        }
        if (stable == 0 && highSignificant == 0 && lowSignificant == 0 && noValueprovided > 0) {
            significantTrindCategory = 5;
            overallCellPercentValue = 0;
        }

        //calculate overallCellPercentValue for linechart
//        if (this.getStyleName().equalsIgnoreCase("customizedproteinsLayout")) {
//            if (highSignificant > 0) {
//                this.setDescription("High");
//            } else if (lowSignificant > 0) {
//                this.setDescription("Low");
//            } else {
//                this.setDescription("Stable");
//            }
//        } else {
            this.setDescription("Protein value: "+overallCellPercentValue + "%<br/>#Low: " + lowSignificant + "<br/>#Stable : " + stable + "<br/>#High: " + highSignificant + "<br/>Overall trend " + overall);
//        }
        
    }
    
    public double getOverallCellPercentValue() {
        return overallCellPercentValue;
    }
    
    @Override
    public String toString() {
        return ("Low: " + lowSignificant + (lowSignificant == 1 ? " dataset" : " datasets") + "  -  Stable : " + stable + (stable == 1 ? " dataset" : " datasets") + " -  High: " + highSignificant + (highSignificant == 1 ? " dataset" : " datasets"));//"Low: " + lowSignificant +" ( "+ df.format(((double) lowSignificant / (double) total) * 100.0)+ "% )  /  Stable : " + stableSignificant + " /  High: " + highSignificant+" ( "+ df.format(((double) highSignificant / (double) total) * 100.0)+ "% )";

    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public int getDSID(int x, int y) {
        if (x == 0) {
            return patientsNumToDSIDMap.get("up").get(y);
        } else if (x == 1) {
            return patientsNumToDSIDMap.get("stable").get(y);
        } else if (x == 2) {
            return patientsNumToDSIDMap.get("down").get(y);
        } else if (x == 3) {
            return patientsNumToDSIDMap.get("noValueProvided").get(y);
        } else {
            return -1;
        }
        
    }
    
    public List<Integer> getRegulationDsList(int regulation) {
        if (regulation == 2) {
            return patientsNumToDSIDMap.get("up");
        } else if (regulation == 1) {
            return patientsNumToDSIDMap.get("stable");
        } else if (regulation == 0) {
            return patientsNumToDSIDMap.get("down");
        } else if (regulation == 3) {
            return patientsNumToDSIDMap.get("noValueProvided");
        } else {
            return null;
        }
        
    }

    /**
     *
     * @return
     */
    public Set<QuantPeptide> getQuantPeptidesList() {
        return quantPeptidesList;
    }

    /**
     *
     * @param quantPeptidesList
     */
    public void setQuantPeptidesList(Set<QuantPeptide> quantPeptidesList) {
        this.quantPeptidesList = quantPeptidesList;
    }

    /**
     *
     * @return
     */
    public int getUniqueId() {
        return uniqueId;
    }

    /**
     *
     * @return
     */
    public int getSignificantTrindCategory() {
        return significantTrindCategory;
    }

    /**
     *
     * @param width
     */
    public void updateWidth(int width) {
        width = width - 20;
        this.setWidth(width + "px");
        float freeArea = width;
        int subTotal = this.total;
        
        if (width > 200) {
            downLabel.setWidth("80px");
            upLabel.setWidth("80px");
            freeArea = width - 160;
            downLabel.setVisible(true);
            upLabel.setVisible(true);
        } else if (width > 140) {
            downLabel.setWidth("50px");
            upLabel.setWidth("50px");
            freeArea = width - 100;
            downLabel.setVisible(true);
            upLabel.setVisible(true);
        } else {
            downLabel.setVisible(false);
            upLabel.setVisible(false);
            
        }
        if (((float) highSignificant / subTotal) <= 0.0) {
            upLayout.setVisible(false);
        } else {
            float upWidth = ((float) highSignificant / (float) subTotal) * freeArea;
            upLayout.setWidth(upWidth + "px");
        }
        if (((float) noValueprovided / subTotal) <= 0.0) {
            noValueProvidedLayout.setVisible(false);
        } else {
            float noValueProvidedWidth = ((float) noValueprovided / (float) subTotal) * freeArea;
            noValueProvidedLayout.setWidth(noValueProvidedWidth + "px");
        }
        if (((float) stable / subTotal) <= 0.0) {
            stableLayout.setVisible(false);
        } else {
            float notProvidedWidth = ((float) stable / (float) subTotal) * freeArea;
            stableLayout.setWidth(notProvidedWidth + "px");
            
        }
        if (((float) lowSignificant / subTotal) <= 0.0) {
            downLayout.setVisible(false);
        } else {
            float downWidth = ((float) lowSignificant / (float) subTotal) * freeArea;
            downLayout.setWidth(downWidth + "px");
        }
        
    }
    
    public void setCustomizedUserData(boolean isCustomized) {
        if (isCustomized) {
            this.setStyleName("customizedproteinsLayout");
            if (highSignificant > 0) {
                this.setDescription("High");
            } else if (lowSignificant > 0) {
                this.setDescription("Low");
            } else {
                this.setDescription("Stable");
            }
            
        } else {
            this.setStyleName("pointer");
        }
        
    }
    
}
