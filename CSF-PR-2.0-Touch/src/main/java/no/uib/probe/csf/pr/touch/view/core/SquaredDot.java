/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.ui.Label;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Yehia Farag
 */
public class SquaredDot extends Label implements Comparable<Label> {

    private final Map<String, Object> param = new HashMap<String, Object>();
//    private final String defaultStyleName;

    /**
     *
     * @param type
     */
    public SquaredDot(String type) {
        this.setValue("  ");
        this.setStyleName("transparent");
        if (type.equalsIgnoreCase("cycle")) {
            this.addStyleName("cycle");
        }

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

    public void select() {
//        if (this.getWidth() < 21) {
            this.addStyleName("selecttransparent");
//            return;
//
//        }
//        this.setStyleName("selected_" + defaultStyleName);

    }

    public void unselect() {
        this.removeStyleName("selecttransparent");
//        if (this.getWidth() < 21) {
            
//            this.setStyleName("unselect_heighlightedsquare");
//            return;
//
//        }
//        this.setStyleName("unselected_" + defaultStyleName);
    }

    public void reset() {
//        this.setStyleName(defaultStyleName);
    }

    @Override
    public int compareTo(Label other) {
        if (this.getWidth() > other.getWidth()) {
            return -1;
        } else {
            return 1;
        }
    }
//
//    @Override
//    public String toString() {
//        return "SquaredDot{" + "param=" + param + ", defaultStyleName=" + defaultStyleName + '}';
//    }

}
