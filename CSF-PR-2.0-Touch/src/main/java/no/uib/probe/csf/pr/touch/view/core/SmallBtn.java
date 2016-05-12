/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Image;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Yehia Farag
 */
public class SmallBtn extends VerticalLayout {

    public SmallBtn(Resource iconResource) {

        this.setWidth(25, Unit.PIXELS);
        this.setHeight(25, Unit.PIXELS);
        this.setStyleName("filterbtn");
        Image icon = new Image();
        icon.setSource(iconResource);
        this.addComponent(icon);
        icon.setWidth(100, Unit.PERCENTAGE);
        icon.setHeight(100, Unit.PERCENTAGE);
        
        
        
    }

}
