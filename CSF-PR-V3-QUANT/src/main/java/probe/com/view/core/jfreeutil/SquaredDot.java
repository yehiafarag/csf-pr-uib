/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core.jfreeutil;

import com.vaadin.ui.Label;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Yehia Farag
 */
public class SquaredDot extends Label {

    private final Map<String, Object> param = new HashMap<String, Object>();

    /**
     *
     */
    public SquaredDot() {
        this.setValue(" ");
        this.setStyleName("transparentcomp");

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


}
