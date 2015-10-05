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
    public String getSequance() {
        return sequance;
    }

    /**
     *
     * @param sequance
     */
    public void setSequance(String sequance) {
        this.sequance = sequance;
    }
    private String sequance;
    private Set<QuantPeptide> quantPeptidesList;
    private final Map<String, List<Integer>> patientsNumToTrindMap = new HashMap<String, List<Integer>>();
    private final Map<String, List<Integer>> patientsNumToDSIDMap = new HashMap<String, List<Integer>>();
    private Map<String, QuantProtein> dsQuantProteinsMap = new HashMap<String, QuantProtein>();

    public Map<String, QuantProtein> getDsQuantProteinsMap() {
        return dsQuantProteinsMap;
    }

    public void setDsQuantProteinsMap(Map<String,QuantProtein> dsQuantProteinsMap) {
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
    private Integer upSignificant = 0;
    private Integer downSignificant = 0;
    private int notRegSignificant = 0;
    private int notProvided = 0;
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
//        this.upReg=upReg;
        this.total = total;
        upLayout = new VerticalLayout();
        downLayout = new VerticalLayout();
        notRegLayout = new VerticalLayout();
        notProvidedLayout = new VerticalLayout();
        patientsNumToTrindMap.put("up", new ArrayList<Integer>());
        patientsNumToTrindMap.put("notReg", new ArrayList<Integer>());
        patientsNumToTrindMap.put("down", new ArrayList<Integer>());
        patientsNumToDSIDMap.put("up", new ArrayList<Integer>());
        patientsNumToDSIDMap.put("notReg", new ArrayList<Integer>());
        patientsNumToDSIDMap.put("down", new ArrayList<Integer>());        
         

        initLabelLayout();
    }

    final VerticalLayout upLayout, downLayout, notRegLayout, notProvidedLayout;

    private void initLabelLayout() {
        this.setWidth("100%");
        this.setHeight("20px");
        this.setSpacing(false);
        this.setMargin(false);

        downLabel = new Label();
        downLabel.setWidth("50px");
        downLabel.setHeight("20px");
        downLabel.setContentMode(ContentMode.HTML);
        this.addComponent(downLabel);
        this.setComponentAlignment(downLabel, Alignment.TOP_RIGHT);

        downLayout.setHeight("15px");
        downLayout.setStyleName("greenlayout");
        this.addComponent(downLayout);
        this.setComponentAlignment(downLayout, Alignment.MIDDLE_CENTER);

        //        notProvidedLayout.setWidth("100%");
        notProvidedLayout.setHeight("15px");
        notProvidedLayout.setStyleName("lightbluelayout");
        this.addComponent(notProvidedLayout);
        this.setComponentAlignment(notProvidedLayout, Alignment.MIDDLE_CENTER);
//
        notRegLayout.setHeight("15px");
        notRegLayout.setStyleName("empty");//"empty"
        this.addComponent(notRegLayout);
        this.setComponentAlignment(notRegLayout, Alignment.MIDDLE_CENTER);

        upLayout.setHeight("15px");
        upLayout.setStyleName("redlayout");
        this.addComponent(upLayout);
        this.setComponentAlignment(upLayout, Alignment.MIDDLE_CENTER);
        upLayout.setCaptionAsHtml(true);

        upLabel = new Label();
        upLabel.setContentMode(ContentMode.HTML);
        upLabel.setWidth("50px");
        upLabel.setHeight("20px");
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
        return upSignificant;
    }
    int counter = 0;

   
    /**
     *
     * @param patientsNumber
     * @param dsID
     */
    public void addUp(int patientsNumber, int dsID, boolean significant) {

        if (significant) {
            trendValue += (double) 1;
            this.upSignificant += 1;
            List<Integer> upList = this.patientsNumToTrindMap.get("up");
            upList.add(patientsNumber);
            this.patientsNumToTrindMap.put("up", upList);

            List<Integer> upDsList = this.patientsNumToDSIDMap.get("up");
            upDsList.add(dsID);
            this.patientsNumToDSIDMap.put("up", upDsList);

        } else {
            addNotProvided(patientsNumber, dsID);
//            this.upNotSignificant = this.upNotSignificant + 1;
//            List<Integer> upList = this.patientsNumToTrindMap.get("upNotSig");
//            upList.add(patientsNumber);
//            this.patientsNumToTrindMap.put("upNotSig", upList);
//
//            List<Integer> upDsList = this.patientsNumToDSIDMap.get("upNotSig");
//            upDsList.add(dsID);
//            this.patientsNumToDSIDMap.put("upNotSig", upDsList);
        }

    }

    /**
     *
     * @return
     */
    public int getSignificantDown() {
        return downSignificant;
    }

    /**
     *
     * @param down
     * @param patientsNumber
     * @param dsID
     */
    public void addDown(int patientsNumber, int dsID, boolean significant) {
       
        
        if (significant) {
            trendValue -= (double) 1;
        this.downSignificant += 1;
        List<Integer> downList = this.patientsNumToTrindMap.get("down");
        downList.add(patientsNumber);
        this.patientsNumToTrindMap.put("down", downList);

        List<Integer> downDsList = this.patientsNumToDSIDMap.get("down");
        downDsList.add(dsID);
        this.patientsNumToDSIDMap.put("down", downDsList);

        } else {

            addNotProvided(patientsNumber, dsID);
//            this.downNotSignificant = this.downNotSignificant + 1;
//            List<Integer> upList = this.patientsNumToTrindMap.get("downNotSig");
//            upList.add(patientsNumber);
//            this.patientsNumToTrindMap.put("downNotSig", upList);
//
//            List<Integer> list = this.patientsNumToDSIDMap.get("downNotSig");
//           list.add(dsID);
//            this.patientsNumToDSIDMap.put("downNotSig", list);
        }
    }

    /**
     *
     * @return
     */
    public int getNotReg() {
        return notRegSignificant;
    }

    /**
     *
     * @param notReg
     * @param patientsNumber
     * @param dsID
     */
    public void addNotReg( int patientsNumber, int dsID) {
       
        
//        if (significant) {
            penalty += 0.5;
        this.notRegSignificant += 1;
        List<Integer> notRegList = this.patientsNumToTrindMap.get("notReg");
        notRegList.add(patientsNumber);
        this.patientsNumToTrindMap.put("notReg", notRegList);

        List<Integer> notRegDsList = this.patientsNumToDSIDMap.get("notReg");
        notRegDsList.add(dsID);
        this.patientsNumToDSIDMap.put("notReg", notRegDsList);

//        } 
//        else {
//
//            this.notRegNotSignificant = this.notRegNotSignificant + 1;
//            List<Integer> list = this.patientsNumToTrindMap.get("notRegNotSig");
//            list.add(patientsNumber);
//            this.patientsNumToTrindMap.put("notRegNotSig", list);
//
//            List<Integer> list2 = this.patientsNumToDSIDMap.get("notRegNotSig");
//           list2.add(dsID);
//            this.patientsNumToDSIDMap.put("notRegNotSig", list2);
//        }
    }

    /**
     *
     * @return
     */
    public int getNotProvided() {
//        trendValue-=0.5;
        return notProvided;
    }

    /**
     *
     * @param notProvided
     * @param patNumber
     * @param dsID
     */
    public void addNotProvided(int patNumber, int dsID) {
//        trendValue-=0.5;
        penalty += 0.5;
        this.notProvided += 1;
        List<Integer> notRegList = this.patientsNumToTrindMap.get("notReg");
        notRegList.add(patNumber);
        this.patientsNumToTrindMap.put("notReg", notRegList);

        List<Integer> notRegDsList = this.patientsNumToDSIDMap.get("notReg");
        notRegDsList.add(dsID);
        this.patientsNumToDSIDMap.put("notReg", notRegDsList);
    }

    @Override
    public int compareTo(DiseaseGroupsComparisonsProteinLayout t) {
        Double v1 = null;
        if (upSignificant.intValue() == downSignificant.intValue()) {
            v1 = trendValue;
        } else if (trendValue > 0) {
            double factor = penalty;
            v1 = trendValue - factor;
            v1 = Math.max(v1, 0) + ((double) (upSignificant - downSignificant) / 10.0);
        } else {
            double factor = penalty;
            v1 = trendValue + factor;
            v1 = Math.min(v1, 0) + ((double) (upSignificant - downSignificant) / 10.0);
        }
        Double v2 = null;
        if (t.upSignificant.intValue() == t.downSignificant.intValue()) {
            v2 = t.trendValue;
        } else if (t.trendValue > 0) {
            double factor = t.penalty;
            v2 = t.trendValue - factor;
            v2 = Math.max(v2, 0) + ((double) (t.upSignificant - t.downSignificant) / 10.0);
        } else {
            double factor = t.penalty;
            v2 = t.trendValue + factor;
            v2 = Math.min(v2, 0) + ((double) (t.upSignificant - t.downSignificant) / 10.0);
        }
        return (v1).compareTo(v2);

    }
    private String fullDownLabelValue = "";
    private String fullUpLabelValue = "";
    private boolean updated = false;

    /**
     *
     */
    public void updateLabelLayout() {
        if (updated) {
            return;
        }
        int dcounter = 0;
//        this.setExpandRatio(downLabel, ((float) 1.5 / total));
//        this.setExpandRatio(upLabel, ((float) 1.5 / total));
        int subTotal = this.total;
        fullDownLabelValue = "<p style='text-align: right;line-height:0.1'><strong> " + df.format(((double) downSignificant / (double) subTotal) * 100.0) + "% &#8595; </strong>&nbsp;</p>";
        downLabel.setValue(fullDownLabelValue);
        fullUpLabelValue = "<p style='text-align: left;line-height:0.1'><strong>&nbsp;&#8593; " + df.format(((double) upSignificant / (double) subTotal) * 100.0) + "%</strong></p>";
        upLabel.setValue(fullUpLabelValue);
//        if (((float) upSignificant / subTotal) <= 0.0) {
//            upLayout.setVisible(false);
//        } 
//        else {
//            dcounter += upSignificant;
//            this.setExpandRatio(upLayout, ((float) upSignificant / subTotal));
//        }
//        if (((float) notProvided / subTotal) <= 0.0) {
//            notProvidedLayout.setVisible(false);
//        } 
//        else {
//            dcounter += notProvided;
//            this.setExpandRatio(notProvidedLayout, ((float) notProvided / subTotal));
//        }
//        if (((float) downSignificant / subTotal) <= 0.0) {
//            downLayout.setVisible(false);
//        } 
//        else {
//            dcounter += downSignificant;
//            this.setExpandRatio(downLayout, ((float) downSignificant / subTotal));
//        }

        Double v1 = null;
        if (upSignificant.intValue() == downSignificant.intValue()) {
            v1 = trendValue;
        } else if (trendValue > 0) {
            double factor = penalty;
            v1 = trendValue - factor;
            v1 = Math.max(v1, 0) + ((double) (upSignificant - downSignificant) / 10.0);
        } else {
            double factor = penalty;
            v1 = trendValue + factor;
            v1 = Math.min(v1, 0) + ((double) (upSignificant - downSignificant) / 10.0);
        }
        if (v1 > 0) {
            cellValue = Math.min(v1, 1);
        } else if (v1 < 0) {
            cellValue = Math.max(v1, -1);
        }        
        this.setExpandRatio(notRegLayout, ((float) (subTotal - dcounter) / subTotal));
        String overall;
        if (cellValue > 0) {
            overall = "Up Regulated (" + cellValue + ")";
            if (notProvided > 0 || notRegSignificant > 0 || downSignificant > 0) {
                significantTrindCategory = 3;
            } else {
                significantTrindCategory = 4;
            }
        } else if (cellValue == 0) {
            overall = "Not Regulated (" + cellValue + ")";
            significantTrindCategory = 2;
        } else {
            overall = "Down Regulated (" + cellValue + ")";
            if (notProvided > 0 || notRegSignificant > 0 || upSignificant > 0) {
                significantTrindCategory = 1;
            } else {
                significantTrindCategory = 0;
            }
        }
        this.setDescription("Down Regulated: " + downSignificant + "  /  Not Regulated : " + notRegSignificant + " /  Up Regulated: " + upSignificant + " Overall Trend " + overall);

    }

    @Override
    public String toString() {
        if (cellValue > 0) {
            return "Up Regulated (" + cellValue + ")";
        } else if (cellValue == 0) {
            return "Not Regulated (" + cellValue + ")";
        } else {
            return "Down Regulated (" + cellValue + ")";
        }
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
            return patientsNumToDSIDMap.get("notReg").get(y);
        } else if (x == 2) {
            return patientsNumToDSIDMap.get("down").get(y);
        } else {
            return -1;
        }

    }
    
    
    public List<Integer> getRegulationDsList(int regulation){
        if (regulation == 2) {
            return patientsNumToDSIDMap.get("up");
        } else if (regulation == 1) {
            return patientsNumToDSIDMap.get("notReg");
        } else if (regulation == 0) {
            return patientsNumToDSIDMap.get("down");
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
        this.setWidth(width + "px");
        float freeArea = width;
        int subTotal = this.total;
//        float labelRatio= 80.0f/(float)width;
//         this.setExpandRatio(downLabel, labelRatio);
//        this.setExpandRatio(upLabel, labelRatio);

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
        if (((float) upSignificant / subTotal) <= 0.0) {
            upLayout.setVisible(false);
        } else {
            float upWidth = ((float) upSignificant / (float) subTotal) * freeArea;
            upLayout.setWidth(upWidth + "px");
        }
        if (((float) notProvided / subTotal) <= 0.0) {
            notProvidedLayout.setVisible(false);
        } else {
            float notProvidedWidth = ((float) notProvided / (float) subTotal) * freeArea;
            notProvidedLayout.setWidth(notProvidedWidth + "px");

        }
        if (((float) downSignificant / subTotal) <= 0.0) {
            downLayout.setVisible(false);
        } else {
            float downWidth = ((float) downSignificant / (float) subTotal) * freeArea;
            downLayout.setWidth(downWidth + "px");
        }

    }

}
