/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.logic.export.util;

import com.vaadin.ui.VerticalLayout;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantPeptide;

/**
 *
 * @author Yehia Farag
 */
public class StackedBarPeptideComponent extends VerticalLayout implements Comparable<StackedBarPeptideComponent> {

    private final Map<String, Object> param = new HashMap<String, Object>();
    private String defaultStyleShowAllMode;
    private boolean significant;
    private final String peptideKey;
    public String getPeptideKey() {
        return peptideKey;
    }

    /**
     *
     * @return
     */
    public int getLevel() {
        return level;
    }

    /**
     *
     * @param level
     */
    public void setLevel(int level) {
        this.level = level;
    }
    private int level = 0;

    private boolean overlapped;

    /**
     *
     * @return
     */
    public boolean isMerged() {
        return merged;
    }

    /**
     *
     * @param merged
     */
    public void setMerged(boolean merged) {
        this.merged = merged;
    }
    private boolean merged;
    private final Set<QuantPeptide> quantpeptideSet = new LinkedHashSet<QuantPeptide>();

    /**
     *
     * @return
     */
    public boolean isOverlapped() {
        return overlapped;
    }

    /**
     *
     * @param overlapped
     */
    public void setOverlapped(boolean overlapped) {
        this.overlapped = overlapped;
    }

    /**
     *
     * @return
     */
    public String getDefaultStyleShowAllMode() {
        return defaultStyleShowAllMode;
    }

    /**
     *
     * @param defaultStyleShowAllMode
     */
    public void setDefaultStyleShowAllMode(String defaultStyleShowAllMode) {
        this.defaultStyleShowAllMode = defaultStyleShowAllMode.replace("selected", "").trim();
        this.setStyleName(this.defaultStyleShowAllMode);
    }

    /**
     *
     * @return
     */
    public int getX0() {
        return x0;
    }

    /**
     *
     * @return
     */
    public int getWidthArea() {
        return widthArea;
    }

    public void setX0(int x0) {
        this.x0 = x0;
    }

    private  int x0;
    private final Integer widthArea;
    private final VerticalLayout ptmLayout = new VerticalLayout();
    private boolean ptmAvailable = false;

    /**
     *
     * @param x0
     * @param widthArea
     * @param peptideKey
     * @param peptideModification
     */
    public StackedBarPeptideComponent(int x0, int widthArea, String peptideKey, String peptideModification) {
        this.setHeight("15px");
        this.setWidth((widthArea) + "px");
        this.x0 = x0;
        this.widthArea = widthArea;
        this.peptideKey = peptideKey;
        if (peptideModification != null && !peptideModification.trim().equalsIgnoreCase("")) {
            ptmAvailable = true;
            ptmLayout.setStyleName("ptmglycosylation");
            ptmLayout.setWidth("10px");
            ptmLayout.setHeight("10px");
            ptmLayout.setDescription(peptideModification);
            ptmLayout.setVisible(false);
        }
    }

    public boolean isPtmAvailable() {
        return ptmAvailable;
    }

    public VerticalLayout getPtmLayout() {
        return ptmLayout;
    }

    /**
     *
     * @param key
     * @param value
     */
    public void setParam(String key, Object value) {
        param.put(key, value);
    }

    /**
     *
     * @param key
     * @return
     */
    public Object getParam(String key) {
        return param.get(key);
    }

    /**
     *
     * @param select
     */
    public void heighlight(Boolean select) {
        if (select == null) {
            this.setStyleName(defaultStyleShowAllMode);
        } else if (select) {
            this.setStyleName("selected" + defaultStyleShowAllMode);
        } else {
            this.setStyleName("unselected" + defaultStyleShowAllMode);
        }
    }

    /**
     *
     * @param qp
     */
    public void addQuantPeptide(QuantPeptide qp) {
        this.quantpeptideSet.add(qp);
    }

    /**
     *
     * @return
     */
    public Set<QuantPeptide> getQuantpeptideSet() {
        return quantpeptideSet;
    }

    @Override
    public int compareTo(StackedBarPeptideComponent o) {      
        return widthArea.compareTo(o.widthArea);
    }

    /**
     *
     * @return
     */
    public boolean isSignificant() {
        return significant;
    }

    /**
     *
     * @param significant
     */
    public void setSignificant(boolean significant) {
        this.significant = significant;
    }

}
