/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Yehia Farag
 *
 * this class represents button with big image the button will support refresh
 * images on fly
 *
 */
public abstract class ImageContainerBtn extends VerticalLayout implements LayoutEvents.LayoutClickListener {
    
    private final Image img;
    
    public ImageContainerBtn() {
        img = new Image();
        
        img.setWidth(100, Unit.PERCENTAGE);
        img.setHeight(100, Unit.PERCENTAGE);
        this.addComponent(img);
        this.setComponentAlignment(img, Alignment.MIDDLE_CENTER);
        
        this.setStyleName("bigbtn");
        this.addLayoutClickListener(ImageContainerBtn.this);
        this.setReadOnly(true);
        this.setEnabled(false);
        
    }

    /**
     *
     * this method responsible for updating button image
     *
     * @param imgResource
     */
    public void updateIcon(Resource imgResource) {     
        img.setSource(imgResource);
        blink();
    }
    
    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {        
        
        onClick();
    }
    
    public void blink() {        
        
        if (img.getStyleName().contains("blinkII")) {
            img.removeStyleName("blinkII");
            img.addStyleName("blink");
        }else{
        img.removeStyleName("blinkI");
            img.addStyleName("blinkII");
        }
        
    } 
    
    public abstract void onClick();
    
}
