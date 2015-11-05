/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
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
        Label infoLable = new Label("<div style='border:1px outset black;text-align:justify;text-justify:inter-word;'><h3 style='font-family:verdana;color:black;font-weight:bold;margin-left:20px;margin-right:20px;'>Information</h3><p  style='font-family:verdana;color:black;margin-left:20px;margin-right:20px;'>Select an experiment in the roll down menu on top to view all proteins identified in the selected experiment. Select a protein to see below all Peptides identified for the protein, and if the experiment was based on SDS-PAGE, the protein’s distribution in the gel is displayed under Fractions. To show information about the experiment, press Dataset Information.  Use the search box to navigate in the experiment selected.</p><p  style='font-family:verdana;color:black;margin-left:20px;margin-right:20px;'>Under Fractions, bar charts show the distribution of the selected protein across the fractions cut from the gel. Three charts show number of peptides, number of spectra and average precursor intensity. The fraction number represents the gel pieces cut from top to bottom. Protein standards <font color='#CDE1FF'>(light blue bars)</font> indicate the molecular weight range of each fraction. <font color='#79AFFF'>Darker blue bars</font> mark between which two standards the protein's theoretical mass suggests the protein should be found.</p></div>");
        infoLable.setContentMode(ContentMode.HTML);
        infoLable.setWidth("450px");
        popupBodyLayout.addComponent(infoLable);
        popupLayout = new PopupView("", popupBodyLayout);
        this.setStyleName("infoicon");
        this.setWidth("16px");
        this.setHeight("16px");
        this.setDescription("Information");
        this.addLayoutClickListener(InfoPopupBtn.this);
        this.addComponent(popupLayout);
        this.popupLayout.setHideOnMouseOut(false);
        
        
    }
    
    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        popupLayout.setPopupVisible(true);
    }
    
}
