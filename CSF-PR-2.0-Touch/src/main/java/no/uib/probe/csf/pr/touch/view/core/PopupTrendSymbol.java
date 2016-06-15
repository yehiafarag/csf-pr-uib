/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Yehia Farag
 *
 * this trend symbol support popup layout with information
 */
public class PopupTrendSymbol extends TrendSymbol implements LayoutEvents.LayoutClickListener {

    private final PopupView popupView;

    public PopupTrendSymbol(int trend) {
        super(trend);

        VerticalLayout popupbodyLayout = new VerticalLayout();
        popupbodyLayout.setWidth(200,Unit.PIXELS);
        popupbodyLayout.setHeight(200,Unit.PIXELS);
        popupbodyLayout.setStyleName("lightgraylayout");
        popupView = new PopupView(null, popupbodyLayout) {

            @Override
            public void setPopupVisible(boolean visible) {
                this.setVisible(visible);
                super.setPopupVisible(visible); //To change body of generated methods, choose Tools | Templates.
            }

        };
        this.addComponent(popupView);
        this.popupView.setPopupVisible(false);
        this.addLayoutClickListener(PopupTrendSymbol.this);

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        System.out.println("att ---------------------------------------- click it");
        popupView.setPopupVisible(true);
    }
    
    public  void setScale(double scale, int pNumber){
        
        int zoomLevel = (int)Math.round(scale*10.0);
        
        System.out.println("at scale is "+ scale+" zoom "+zoomLevel+"   pn "+pNumber );
        this.addStyleName("zoom"+zoomLevel);
        
    }

}
