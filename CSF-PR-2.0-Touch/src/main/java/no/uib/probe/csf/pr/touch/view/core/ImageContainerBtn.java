/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Resource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;

/**
 *
 * @author Yehia Farag
 *
 * this class represents button with big image the button will support refresh
 * images on fly
 *
 */
public abstract class ImageContainerBtn extends AbsoluteLayout implements LayoutEvents.LayoutClickListener {

    private final Image img;
    private final Label text;

    public ImageContainerBtn() {
        img = new Image();
        img.setWidth(100, Unit.PERCENTAGE);
        img.setHeight(100, Unit.PERCENTAGE);
        this.addComponent(img,"left: " + (0) + "px; top: " + (0) + "px;");
        
         text = new Label();
         text.setWidth(100,Unit.PERCENTAGE);
         text.setHeight(100,Unit.PERCENTAGE);
         this.addComponent(text, "left: " + (0) + "px; top: " + (0) + "px;");

        this.setStyleName("bigbtn");
        this.addLayoutClickListener(ImageContainerBtn.this);
        this.setReadOnly(true);
        ImageContainerBtn.this.setEnabled(false);

    }

    /**
     *
     * this method responsible for updating button image
     *
     * @param imgResource
     */
    public void updateIcon(Resource imgResource) {
        img.setSource(imgResource);
    }
    
     /**
     *
     * this method responsible for updating button text
     *
     * @param imgResource
     */
    public void updateText(String textStr) {
        text.setValue(textStr);
    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        this.removeStyleName("orangeBorder");

        onClick();
    }

    public void setHasWrapper(boolean hasWrapper) {
        this.hasWrapper = hasWrapper;
    }
    private boolean hasWrapper=false; 

    public void blink() {
        this.addStyleName("orangeBorder");
        if (img.getStyleName().contains("blinkII")) {
            img.removeStyleName("blinkII");
            img.addStyleName("blink");
        } else {
            img.removeStyleName("blinkI");
            img.addStyleName("blinkII");
        }
    }

    public abstract void onClick();

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled) {
            this.removeStyleName("unapplied");
            this.blink();

        } else {
            this.addStyleName("unapplied");
        }
        super.setEnabled(enabled); //To change body of generated methods, choose Tools | Templates.
        if (this.getParent() != null && hasWrapper) {
            this.getParent().setEnabled(enabled);
        }
    }

}
