/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core.jfreeutil;

import com.vaadin.ui.VerticalLayout;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import probe.com.model.beans.quant.QuantPeptide;

/**
 *
 * @author Yehia Farag
 */
public class StackedBarPeptideComponent extends VerticalLayout implements Comparable<StackedBarPeptideComponent> {

    private final Map<String, Object> param = new HashMap<String, Object>();
    private String defaultStyleShowAllMode;
    private boolean significant;

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
        this.defaultStyleShowAllMode = defaultStyleShowAllMode.replace("selected", "").trim();;
         this.setStyleName(defaultStyleShowAllMode);
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

    private final int x0;
    private final Integer widthArea;

    /**
     *
     * @param x0
     * @param widthArea
     */
    public StackedBarPeptideComponent(int x0, int widthArea) {
        this.setHeight("15px");
        this.setWidth((widthArea + 2) + "px");
        this.x0 = x0;
        this.widthArea = widthArea;
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
    public void heighlight(boolean select) {
        if (select) {
            this.setStyleName("selected"+defaultStyleShowAllMode);
        } else {
            this.setStyleName(defaultStyleShowAllMode);
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
