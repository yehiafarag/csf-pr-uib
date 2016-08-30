/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Yehia Farag
 */
public abstract class RadioButton extends VerticalLayout implements LayoutEvents.LayoutClickListener{

    private final Object itemId;
 
    public RadioButton(Object itemId) {
        this.itemId=itemId;
        this.setWidth(15,Unit.PIXELS);
         this.setHeight(15,Unit.PIXELS);
         this.addLayoutClickListener(RadioButton.this);
         this.setStyleName("table_radio_btn");
         
    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        selectItem(itemId);
        
    }
    
    public abstract void selectItem(Object itemId);
    
    
    
    
}
