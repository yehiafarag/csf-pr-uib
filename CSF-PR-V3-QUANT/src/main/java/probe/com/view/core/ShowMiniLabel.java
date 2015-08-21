/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Label;
import java.io.Serializable;

/**
 *
 * @author y-mok_000
 */
public class ShowMiniLabel extends Label implements Serializable {

    boolean show = false;

    /**
     *
     */
    public ShowMiniLabel() {
        updateIcon(show);
    }

    /**
     *
     * @param v
     */
    public ShowMiniLabel(boolean v) {
        updateIcon(v);
    }

    /**
     *
     * @param show
     */
    public final void updateIcon(boolean show) {
        if (show) {
            show = false;
            setIcon(new ThemeResource("img/down-mini.jpg"));//setIcon(new ExternalResource("https://fbcdn-sphotos-f-a.akamaihd.net/hphotos-ak-prn2/1185892_173022819547985_676735970_n.jpg"));
          
            
        } else {
            show = true;
            setIcon(new ThemeResource("img/right-mini.jpg"));//new ExternalResource("https://fbcdn-sphotos-d-a.akamaihd.net/hphotos-ak-prn2/1175716_173022812881319_804705945_n.jpg"));


        }
    }
}