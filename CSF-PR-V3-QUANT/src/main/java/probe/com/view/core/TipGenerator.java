/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.shared.communication.SharedState;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.DesignContext;
import java.io.Serializable;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Set;
import org.jsoup.nodes.Element;

/**
 *
 * @author Yehia Farag
 */
public class TipGenerator implements LayoutEvents.LayoutClickListener, Serializable {

    private final Set<Tips> tipsList = new HashSet<Tips>();

    public Tips generateTip(String tipsInfo, int x,int y) {
        Tips tip = new Tips(tipsInfo, "tip"+tipsList.size(),x,y);
        tipsList.add(tip);
        return tip;

    }

    public VerticalLayout generateTipsBtn() {
        VerticalLayout tipsIcon = new VerticalLayout();
        tipsIcon.setWidth("16px");
        tipsIcon.setHeight("16px");
        tipsIcon.setDescription("Show tips");
        tipsIcon.setStyleName("tipbtn");
        tipsIcon.addLayoutClickListener(this);
        return tipsIcon;

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
       showTips();
    }
    
    public void showTips(){
     for (Tips tip : tipsList) {
//            if (tip.isAttached()) {
                tip.showTip();
//            }
        }
    
    }

}
