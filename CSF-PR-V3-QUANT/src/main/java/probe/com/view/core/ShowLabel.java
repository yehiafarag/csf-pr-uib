/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Label;
import java.io.Serializable;

/**
 *
 * @author Yehia Farag
 */
public final class ShowLabel extends Label implements Serializable {

    boolean show = false;

    /**
     *
     */
    public ShowLabel() {
        updateIcon(show);
    }

    /**
     *
     * @param v
     */
    public ShowLabel(boolean v) {
        updateIcon(v);
    }

    /**
     *
     * @param show
     */
    public void updateIcon(boolean show) {
        if (show) {
            show = false;
            setIcon(new ThemeResource("img/down.jpg"));//setIcon(new ExternalResource("https://fbcdn-sphotos-f-a.akamaihd.net/hphotos-ak-prn2/1185892_173022819547985_676735970_n.jpg"));expand-arrows.png
          
            
        } else {
            show = true;
            setIcon(new ThemeResource("img/right.jpg"));//new ExternalResource("https://fbcdn-sphotos-d-a.akamaihd.net/hphotos-ak-prn2/1175716_173022812881319_804705945_n.jpg"));  img/hide-arrows.png
          


        }
    }
}
