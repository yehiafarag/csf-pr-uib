/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.client.ui.Icon;
import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author YEhia Farag
 *
 * this class represents slide left panel
 */
public class SlideLeftPanel extends VerticalLayout implements LayoutEvents.LayoutClickListener{

    private final String maxWidth, minWidth;
    private boolean show;

    public SlideLeftPanel(int maxWidth, int minWidth, Icon icon) {
        this.maxWidth = maxWidth + "%";
        this.minWidth = minWidth + "%";
        this.setHeight("100%");
        this.setStyleName("leftslidepanel");
        
        Button closeBtn = new Button();
        closeBtn.addClickListener((Button.ClickEvent event) -> {          
                setShowPanel(!show);            
        });
        SlideLeftPanel.this.setShowPanel(true);
//        this.addComponent(closeBtn);
        this.addLayoutClickListener(SlideLeftPanel.this);

    }

    public final void setShowPanel(boolean show) {
        if (show) {
            this.setWidth(maxWidth);
        } else {
            this.setWidth(minWidth);
        }
        this.show=show;

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
         setShowPanel(!show);    
    }

}
