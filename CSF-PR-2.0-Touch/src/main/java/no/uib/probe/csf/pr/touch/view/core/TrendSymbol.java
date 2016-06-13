/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.VerticalLayout;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Yehia Farag
 *
 * this class represents Arrow up layout used in protein trend line chart and
 * spark line in quant protein table
 */
public class TrendSymbol extends VerticalLayout implements LayoutEvents.LayoutClickListener {
    
    private final HashMap<String, Object> paramMap;
    private final HashSet<TrendSymbol> subComponents;
    
    public TrendSymbol(int trend) {
        paramMap = new HashMap<>();
        subComponents = new HashSet<>();
        this.addLayoutClickListener(TrendSymbol.this);
        switch (trend) {
            case 0:
                this.setStyleName("arrow-up100");
                break;
            case 1:
                this.setStyleName("arrow-upless100");
                break;
            case 2:
                this.setStyleName("diamond");
                break;
            case 3:
                this.setStyleName("arrow-downless100");
                
                break;
            case 4:
                this.setStyleName("arrow-down100");
                
                break;
            case 5:
                this.setStyleName("darkgraydiamond");
                break;
            case 6:
                this.setStyleName("graydiamond");
                break;
            
        }
    }
    
    public void addParam(String name, Object value) {
        paramMap.put(name, value);
        
    }
    
    public Object getParam(String paramName) {
        return paramMap.get(paramName);
    }
    
    public void addSubComponent(TrendSymbol subComponent) {
        this.subComponents.add(subComponent);
    }
    
    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (this.getStyleName().contains("unapplied")) {
            this.removeStyleName("unapplied");
            for (TrendSymbol sub : subComponents) {
                sub.setVisible(false);
                
            }
            
        }else if(!subComponents.isEmpty()){
            this.addStyleName("unapplied");
             for (TrendSymbol sub : subComponents) {
                sub.setVisible(true);
                
            }
        
        }
    }
    
}
