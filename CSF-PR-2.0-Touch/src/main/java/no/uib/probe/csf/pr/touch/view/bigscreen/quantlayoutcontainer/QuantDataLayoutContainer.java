/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.bigscreen.quantlayoutcontainer;

import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import no.uib.probe.csf.pr.touch.view.core.ImageContainerBtn;

/**
 *
 * @author Yehia Farag
 * 
 * this class represents the main quant data layout container
 * the layout is a slide panel layout 
 * 
 */
public class QuantDataLayoutContainer extends VerticalLayout{

    private final VerticalLayout sideButtonsContainer;
     private final HorizontalLayout quantBodyWrapper;
     private final VerticalLayout mainComponetViewPanel;
    public QuantDataLayoutContainer(int width, int height) {
        this.setWidth(width+"px");
        this.setHeight(height+"px");
        
        quantBodyWrapper = new HorizontalLayout();
        quantBodyWrapper.setWidthUndefined();
        quantBodyWrapper.setHeight("100%");
        quantBodyWrapper.setWidthUndefined();
        quantBodyWrapper.setSpacing(true);
        this.addComponent(quantBodyWrapper);
        sideButtonsContainer = new VerticalLayout();
        sideButtonsContainer.setWidth("150px");
        
        sideButtonsContainer.setSpacing(true);
        sideButtonsContainer.setMargin(new MarginInfo(true, false, false, true));
        
        quantBodyWrapper.addComponent(sideButtonsContainer);
        
        ImageContainerBtn heatmapBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                System.out.println("show heatmap");
            }
        };
            heatmapBtn.updateIcon(new ThemeResource("img/logo.png"));
            sideButtonsContainer.addComponent(heatmapBtn);
        
            
            ImageContainerBtn bubblechartBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                System.out.println("show heatmap");
            }
        };
            bubblechartBtn.updateIcon(new ThemeResource("img/logo.png"));
            sideButtonsContainer.addComponent(bubblechartBtn);
        
            ImageContainerBtn tableBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                System.out.println("show heatmap");
            }
        };
            tableBtn.updateIcon(new ThemeResource("img/logo.png"));
            sideButtonsContainer.addComponent(tableBtn);
            
            
            ImageContainerBtn linechartBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                System.out.println("show heatmap");
            }
        };
            linechartBtn.updateIcon(new ThemeResource("img/logo.png"));
            sideButtonsContainer.addComponent(linechartBtn);
            
            ImageContainerBtn peptideInfoBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                System.out.println("show heatmap");
            }
        };
            peptideInfoBtn.updateIcon(new ThemeResource("img/logo.png"));
            sideButtonsContainer.addComponent(peptideInfoBtn);
            
            
           mainComponetViewPanel = new VerticalLayout();
           quantBodyWrapper.addComponent(mainComponetViewPanel);
           mainComponetViewPanel.setWidth((width-200)+"px");
           mainComponetViewPanel.setHeight("100%");
           mainComponetViewPanel.setStyleName("blacklayout");
            
        
    }

   
    
}
