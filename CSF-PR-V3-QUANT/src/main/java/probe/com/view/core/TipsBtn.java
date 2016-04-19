/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.VerticalLayout;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import probe.com.selectionmanager.QuantCentralManager;

/**
 *
 * @author yfa041
 */
public class TipsBtn extends  VerticalLayout implements LayoutEvents.LayoutClickListener, Serializable {

    private final String tabName;
    private final QuantCentralManager Quant_Central_manager;
    public TipsBtn(String tabName,QuantCentralManager Quant_Central_manager) { 
        this.setWidth("16px");
        this.setHeight("16px");
        this.setDescription("Show tips");
        this.setStyleName("tipbtn");
        this.addLayoutClickListener(TipsBtn.this);
        this.tabName=tabName;
        this.Quant_Central_manager=Quant_Central_manager;
    }


  

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
       showTips();
    }
    
    public void showTips(){
        Quant_Central_manager.showNotifications(tabName);
    
    }

}

