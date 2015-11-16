/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author yfa041
 */
public class InfoPopupBtn extends VerticalLayout implements LayoutEvents.LayoutClickListener {
    
    VerticalLayout popupBodyLayout = new VerticalLayout();
    PopupView popupLayout;
    
    public InfoPopupBtn(String infoText) {
        HorizontalLayout topLayout = new HorizontalLayout();
        VerticalLayout mainBody = new VerticalLayout();
        mainBody.setWidth("450px");
        mainBody.addComponent(topLayout);
        Label infoHeaderLabel = new Label("<h3 style='font-family:verdana;color:black;font-weight:bold;margin-left:20px;margin-right:20px;'>Information</h3>");
        infoHeaderLabel.setContentMode(ContentMode.HTML);
        topLayout.addComponent(infoHeaderLabel);
        topLayout.setWidth("100%");
        Label infoLable = new Label("<div style='text-align:justify;text-justify:inter-word;'><p  style='font-family:verdana;color:black;margin-left:20px;margin-right:20px;'>"+infoText+"</p></div>");
        infoLable.setContentMode(ContentMode.HTML);
        infoLable.setWidth("450px");
        mainBody.addComponent(infoLable);
        popupBodyLayout.addComponent(mainBody);
        mainBody.setStyleName("popupmainbody");
        VerticalLayout closeBtn = new VerticalLayout();
        closeBtn.setWidth("16px");
        closeBtn.setHeight("16px");
        closeBtn.setStyleName("defaultclosebtn");
        topLayout.addComponent(closeBtn);
        topLayout.setComponentAlignment(closeBtn, Alignment.TOP_RIGHT);
        popupLayout = new PopupView("", popupBodyLayout);
        this.setStyleName("infoicon");
        this.setWidth("20px");
        this.setHeight("20px");
        this.setDescription("Information");
        this.addLayoutClickListener(InfoPopupBtn.this);
        this.addComponent(popupLayout);
        this.popupLayout.setHideOnMouseOut(false);
        
        closeBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                popupLayout.setPopupVisible(false);
            }
        });
        
    }
    
    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        popupLayout.setPopupVisible(true);
    }
    
}
