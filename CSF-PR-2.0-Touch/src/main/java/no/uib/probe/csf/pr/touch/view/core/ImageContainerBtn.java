/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

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

    /**
     *
     */
    public ImageContainerBtn() {
        img = new Image();
        img.setWidth(100, Unit.PERCENTAGE);
        img.setHeight(100, Unit.PERCENTAGE);
        this.addComponent(img, "left: " + (0) + "px; top: " + (0) + "px;");

        VerticalLayout labelcontainer = new VerticalLayout();
        labelcontainer.setWidth(100, Unit.PERCENTAGE);
        labelcontainer.setHeight(100, Unit.PERCENTAGE);
        this.addComponent(labelcontainer, "left: " + (0) + "px; top: " + (0) + "px;");
        text = new Label() {

            @Override
            public void setValue(String newStringValue) {
                if (newStringValue == null || newStringValue.equalsIgnoreCase("")) {
                    this.setVisible(false);
                } else {
                    this.setVisible(true);
                }
                super.setValue(newStringValue); //To change body of generated methods, choose Tools | Templates.
            }

        };
        text.addStyleName("bubbleframe");
        labelcontainer.addComponent(text);
        text.setWidthUndefined();
        labelcontainer.setComponentAlignment(text, Alignment.MIDDLE_CENTER);
        text.setContentMode(ContentMode.HTML);

        this.setStyleName("bigbtn");
        this.addStyleName("pointer");
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
        if (imgResource == null) {
            img.setVisible(false);
        } else {
            img.setSource(imgResource);
            img.setVisible(true);
        }
    }

    /**
     *
     * this method responsible for updating button text
     *
     * @param textStr
     */
    public void updateText(String textStr) {
        text.setValue(textStr);
    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        this.removeStyleName("orangeBorder");

        onClick();
    }

    /**
     *
     * @param hasWrapper
     */
    public void setHasWrapper(boolean hasWrapper) {
        this.hasWrapper = hasWrapper;
    }
    private boolean hasWrapper = false;

    /**
     *
     */
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

    /**
     *
     */
    public abstract void onClick();

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled) {
            this.removeStyleName("unapplied");
            this.blink();

        } else {
            this.addStyleName("unapplied");
            text.setValue("");
        }
        super.setEnabled(enabled); //To change body of generated methods, choose Tools | Templates.
        if (this.getParent() != null && hasWrapper) {
            this.getParent().setEnabled(enabled);
        }
        this.setVisible(enabled);
    }

    /**
     * to be override
     */
    public void reset() {
    }

}
