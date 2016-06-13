/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.components;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFListener;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFPR_Central_Manager;
import no.uib.probe.csf.pr.touch.view.core.TrendLegend;

/**
 *
 * @author Yehia Farag
 *
 * this class represents the peptide information layout the layout show overall
 * trend line-chart and detailed studies line chart for the selected protein the
 * second component consist of table of comparisons and peptides sequences for
 * each protein
 */
public class PeptideViewComponent extends VerticalLayout implements CSFListener{

    private final CSFPR_Central_Manager CSFPR_Central_Manager;
    private final VerticalLayout controlBtnsContainer;

    public VerticalLayout getControlBtnsContainer() {
        return controlBtnsContainer;
    }

    public PeptideViewComponent(CSFPR_Central_Manager CSFPR_Central_Manager, int width, int height, QuantDiseaseGroupsComparison userCustomizedComparison) {
        this.CSFPR_Central_Manager = CSFPR_Central_Manager;
        this.setWidth(100, Unit.PERCENTAGE);
        this.setHeight(height, Unit.PIXELS);
        VerticalLayout mainBodyContainer=new VerticalLayout();
        mainBodyContainer.setSpacing(true);
        mainBodyContainer.setWidth(100,Unit.PERCENTAGE);
        mainBodyContainer.setHeightUndefined();        
        this.addComponent(mainBodyContainer);
        
          
        TrendLegend legendLayout = new TrendLegend("linechart");
        legendLayout.setWidthUndefined();
        legendLayout.setHeight(24, Unit.PIXELS);
        mainBodyContainer.addComponent(legendLayout);
        mainBodyContainer.setComponentAlignment(legendLayout, Alignment.MIDDLE_RIGHT);
        
        
        VerticalLayout topLayout = new VerticalLayout();
        width=width-50;
        topLayout.setWidth(width,Unit.PIXELS);
        topLayout.setHeight(500,Unit.PIXELS);
        topLayout.addStyleName("roundedborder");
        topLayout.addStyleName("whitelayout");
        
        mainBodyContainer.addComponent(topLayout);
        mainBodyContainer.setComponentAlignment(topLayout, Alignment.MIDDLE_CENTER);
        
        
        
        
        VerticalLayout bottomLayout = new VerticalLayout();
        bottomLayout.setWidth(width,Unit.PIXELS);
        bottomLayout.setHeight(500,Unit.PIXELS);
        bottomLayout.addStyleName("roundedborder");
        bottomLayout.addStyleName("whitelayout");
        
        mainBodyContainer.addComponent(bottomLayout);
        mainBodyContainer.setComponentAlignment(bottomLayout, Alignment.MIDDLE_CENTER);
        
        
        
        
      

        //end of toplayout
        //start chart layout
//        VerticalLayout tableLayoutFrame = new VerticalLayout();
//        height = height - 44;
//        
//        int tableHeight = height;
//        width = width - 50;
//        tableLayoutFrame.setWidth(width, Unit.PIXELS);
//        tableLayoutFrame.setHeight(tableHeight, Unit.PIXELS);
//        tableLayoutFrame.addStyleName("roundedborder");
//        tableLayoutFrame.addStyleName("whitelayout");
//        bodyContainer.addComponent(tableLayoutFrame);
//        bodyContainer.setComponentAlignment(tableLayoutFrame, Alignment.MIDDLE_CENTER);
//        height = height - 40;
//        width = width - 60;
        
        
        
        
        
        
        

        //init side control btns layout 
        controlBtnsContainer = new VerticalLayout();
        controlBtnsContainer.setHeightUndefined();
        controlBtnsContainer.setWidthUndefined();
        controlBtnsContainer.setSpacing(true);
    }

    @Override
    public void selectionChanged(String type) {
        
        
        
        
        
    }

    @Override
    public String getFilterId() {
        return "peptideComponent";
    }

    @Override
    public void removeFilterValue(String value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    
    
    
    
    
    
    
    
    
    

}
