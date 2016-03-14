/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author yfa041
 */
public class ZoomUnit extends HorizontalLayout implements LayoutEvents.LayoutClickListener {
    
    private final VerticalLayout zoomInBtn, zoomOutBtn, resetZoomBtn;
    private int zoomLevel = 100;
    private Component zoomedLayout;
    
    public ZoomUnit() {
        this.setStyleName("zoompanel");
        zoomInBtn = new VerticalLayout();
        zoomInBtn.setWidth("32px");
        zoomInBtn.setHeight("32px");
        zoomInBtn.setStyleName("zoominbtn");
        zoomInBtn.setDescription("Zoom in");
        this.addComponent(zoomInBtn);
        
        zoomInBtn.addLayoutClickListener(ZoomUnit.this);
        resetZoomBtn = new VerticalLayout();
        resetZoomBtn.setWidth("32px");
        resetZoomBtn.setHeight("32px");
        resetZoomBtn.setStyleName("resetzoombtn");
        resetZoomBtn.setDescription("Reset zoom");
        
        this.addComponent(resetZoomBtn);
        zoomOutBtn = new VerticalLayout();
        zoomOutBtn.setWidth("32px");
        zoomOutBtn.setHeight("32px");
        zoomOutBtn.setStyleName("zoomoutbtn");
        
        this.addComponent(zoomOutBtn);
        zoomOutBtn.addLayoutClickListener(ZoomUnit.this);
        zoomOutBtn.setDescription("Zoom out");
        
        this.setVisible(false);
        
        resetZoomBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                zoomedLayout.removeStyleName("zoom" + zoomLevel);
                zoomLevel = 100;
                
            }
        });
        
    }
    
    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (zoomedLayout != null) {
            zoomedLayout.removeStyleName("zoom" + zoomLevel);
            if (event.getComponent().getStyleName().equalsIgnoreCase("zoominbtn")) {
                zoomLevel += 25;
                zoomLevel = Math.min(zoomLevel, 300);
                
            } else {
                zoomLevel -= 25;
                zoomLevel = Math.max(zoomLevel, 50);
            }
            System.out.println("at zoom style " + "zoom" + zoomLevel);
            zoomedLayout.addStyleName("zoom" + zoomLevel);
        }
    }
    
    public void setLayout(Component layout) {
        if (zoomedLayout != null) {
            zoomedLayout.removeStyleName("zoom" + zoomLevel);
            zoomLevel = 100;
        }
        if (layout == null) {
            this.setVisible(false);
        } else {
            this.setVisible(true);
        }
        this.zoomedLayout = layout;
        
    }
    
}
