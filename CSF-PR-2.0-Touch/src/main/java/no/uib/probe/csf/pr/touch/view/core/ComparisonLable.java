/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;

/**
 *
 * @author Yehia Farag
 *
 * this class represents the label for comparison name
 */
public abstract class ComparisonLable extends VerticalLayout implements LayoutEvents.LayoutClickListener{
    private final Object itemId;
    public ComparisonLable(QuantDiseaseGroupsComparison comparison,Object itemId) {
        this.itemId=itemId;
        this.setWidth(100, Unit.PERCENTAGE);
        
        this.setHeightUndefined();
        this.setSpacing(true);
        this.addLayoutClickListener(ComparisonLable.this);
        String[] headerI = comparison.getComparisonHeader().replace("__" + comparison.getDiseaseCategory(), "").split(" / ");
        String diseaseColor = comparison.getDiseaseCategoryColor();
        
        
        Label labelI = new Label("<font  style=' color:" + diseaseColor + "'>" + headerI[0] + "</font>");
        labelI.setWidth(100,Unit.PERCENTAGE);
        labelI.addStyleName(ValoTheme.LABEL_SMALL);
         labelI.addStyleName(ValoTheme.LABEL_TINY);
         labelI.setHeight(15,Unit.PIXELS);
         labelI.addStyleName("overflowtext");
         labelI.setContentMode(ContentMode.HTML);
         this.addComponent(labelI);
         
          VerticalLayout spacer = new VerticalLayout();
        spacer.setStyleName(ValoTheme.LAYOUT_WELL);
        spacer.setWidth(100,Unit.PERCENTAGE);
        spacer.setHeight(2,Unit.PIXELS);
        spacer.setMargin(new MarginInfo(false, true, false, true));
         this.addComponent(spacer);
         this.setComponentAlignment(spacer,Alignment.MIDDLE_CENTER);
        
        Label labelII = new Label("<font  style='color:" + diseaseColor + "'>" + headerI[1] + "</font>");   
         labelII.setHeight(15,Unit.PIXELS);
        labelII.setWidth(100,Unit.PERCENTAGE);
        labelII.addStyleName("overflowtext");
        labelII.addStyleName(ValoTheme.LABEL_SMALL);
         labelII.addStyleName(ValoTheme.LABEL_TINY);
        labelII.setContentMode(ContentMode.HTML);       
        this.addComponent(labelII);
        this.setDescription(comparison.getComparisonFullName());
    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        select(itemId);
    }
    
    public  abstract void select(Object itemId);
    
    
}
