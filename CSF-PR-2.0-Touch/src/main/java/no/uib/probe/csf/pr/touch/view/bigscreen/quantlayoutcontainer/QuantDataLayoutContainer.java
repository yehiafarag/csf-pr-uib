/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.bigscreen.quantlayoutcontainer;

import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import java.util.Set;
import no.uib.probe.csf.pr.touch.Data_Handler;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseCategoryObject;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFPR_Central_Manager;
import no.uib.probe.csf.pr.touch.view.components.HeatMapComponent;
import no.uib.probe.csf.pr.touch.view.components.QuantInitialLayout;
import no.uib.probe.csf.pr.touch.view.core.ImageContainerBtn;
import no.uib.probe.csf.pr.touch.view.core.ScrollPanel;
import no.uib.probe.csf.pr.touch.view.core.SlidePanel;
import no.uib.probe.csf.pr.touch.view.core.ViewControlPanel;

/**
 *
 * @author Yehia Farag
 *
 * this class represents the main quant data layout container the layout is a
 * slide panel layout
 *
 */
public class QuantDataLayoutContainer extends ViewControlPanel {

//    private final VerticalLayout sideButtonsContainer;
//    private final HorizontalLayout quantBodyWrapper;
    private final VerticalLayout heatmapViewContainer;
    private final ImageContainerBtn heatmapBtn, bubblechartBtn, tableBtn, linechartBtn, peptideInfoBtn;
    private final Data_Handler Data_handler;
    private final CSFPR_Central_Manager CSFPR_Central_Manager;
//    private final SlidePanel initialLayoutPanel;
    
    
    
    
    private HeatMapComponent heatmapComponent;

    private final int mainViewPanelWidth;
    private final int mainViewPanelHeight;
    private final QuantInitialLayout quantInitialLayout;

    public QuantDataLayoutContainer(final Data_Handler Data_handler, int width, int height) {
        super(width, height);
        this.Data_handler = Data_handler;
        CSFPR_Central_Manager = new CSFPR_Central_Manager();        
        this.setMargin(true);
        

//        quantBodyWrapper = new HorizontalLayout();
//        quantBodyWrapper.setWidthUndefined();
//        quantBodyWrapper.setHeightUndefined();
//        quantBodyWrapper.setWidthUndefined();
//        quantBodyWrapper.setSpacing(false);
//        this.addComponent(quantBodyWrapper);

        Set<DiseaseCategoryObject> availableDiseaseCategory = Data_handler.getDiseaseCategorySet();  
        final HorizontalLayout subBodyWrapper = new HorizontalLayout();
        subBodyWrapper.setWidthUndefined();
        subBodyWrapper.setHeight((height - 200), Unit.PIXELS);
        
        quantInitialLayout = new QuantInitialLayout(availableDiseaseCategory, width, height) {

            private String lastSelectedDisease = "";

            @Override
            public void onClick(String diseaseCategoryName) {
                if (!lastSelectedDisease.equalsIgnoreCase(diseaseCategoryName)) {
                    lastSelectedDisease = diseaseCategoryName;
                    //do functions
                    //load dataset
                    loadDiseaseCategory(diseaseCategoryName);
                }
            }

        };
//        initialLayoutPanel = new SlidePanel(quantInitialLayout, null, 0, "diseasecategoryselectionset");
//        initialLayoutPanel.setShowNavigationBtn(false);
//        initialLayoutPanel.setShowPanel(true);
//        quantBodyWrapper.addComponent(initialLayoutPanel);
//        quantBodyWrapper.setComponentAlignment(initialLayoutPanel, Alignment.MIDDLE_LEFT);

//      
//        quantBodyWrapper.addComponent(subBodyWrapper);
//        quantBodyWrapper.setComponentAlignment(subBodyWrapper, Alignment.MIDDLE_LEFT);
//        subBodyWrapper.setMargin(new MarginInfo(true, false, false, false));
//
//        VerticalLayout sideButtonsContainerWrapper = new VerticalLayout();
//        sideButtonsContainerWrapper.setWidth(150, Unit.PIXELS);
//        sideButtonsContainerWrapper.setHeight(height, Unit.PIXELS);
//        subBodyWrapper.addComponent(sideButtonsContainerWrapper);
//        subBodyWrapper.setComponentAlignment(sideButtonsContainerWrapper, Alignment.TOP_CENTER);

//        sideButtonsContainer = new VerticalLayout();
//        sideButtonsContainer.setWidth("150px");
//
//        sideButtonsContainer.setSpacing(true);
//        sideButtonsContainer.setMargin(new MarginInfo(false, false, false, true));
//        sideButtonsContainer.setVisible(true);
//
//        sideButtonsContainerWrapper.addComponent(sideButtonsContainer);
//        sideButtonsContainerWrapper.setComponentAlignment(sideButtonsContainer, Alignment.TOP_CENTER);

        this.setInitialLayout(quantInitialLayout.getMiniLayout(),quantInitialLayout);

        heatmapBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                processFunction("heatmap");
            }
        };
        heatmapBtn.updateIcon(new ThemeResource("img/logo.png"));
        
        mainViewPanelHeight = height;
        mainViewPanelWidth = width - 200;
        heatmapViewContainer = new VerticalLayout();
        heatmapViewContainer.setWidth(mainViewPanelWidth,Unit.PIXELS);
        this.addButton(heatmapBtn,heatmapViewContainer,true);

        bubblechartBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                processFunction("bubblechart");
            }
        };
        bubblechartBtn.updateIcon(new ThemeResource("img/logo.png"));
//        sideButtonsContainer.addComponent(bubblechartBtn);

        tableBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                processFunction("proteintable");
            }
        };
        tableBtn.updateIcon(new ThemeResource("img/logo.png"));
//        sideButtonsContainer.addComponent(tableBtn);

        linechartBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                processFunction("linechart");
            }
        };
        linechartBtn.updateIcon(new ThemeResource("img/logo.png"));
//        sideButtonsContainer.addComponent(linechartBtn);

        peptideInfoBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                processFunction("peptidelayout");
            }
        };
        peptideInfoBtn.updateIcon(new ThemeResource("img/logo.png"));
//        sideButtonsContainer.addComponent(peptideInfoBtn);

     
//        mainComponetViewPanel = new VerticalLayout();
//        subBodyWrapper.addComponent(mainComponetViewPanel);
//        subBodyWrapper.setComponentAlignment(mainComponetViewPanel, Alignment.MIDDLE_CENTER);
//        mainComponetViewPanel.setWidth((mainViewPanelWidth), Unit.PIXELS);
//        mainComponetViewPanel.setHeight(height, Unit.PIXELS);

    }

    private void processFunction(String btnId) {
        switch (btnId) {

            case "heatmap":
                bubblechartBtn.blink();
                break;

        }

    }

    private void loadDiseaseCategory(String DiseaseCategoryName) {
        Data_handler.loadDiseaseCategory(DiseaseCategoryName);
        if (heatmapComponent == null) {
            heatmapComponent = new HeatMapComponent(CSFPR_Central_Manager, mainViewPanelWidth, mainViewPanelHeight,heatmapBtn);
            heatmapViewContainer.addComponent(heatmapComponent);
//            mainComponetViewPanel.setComponentAlignment(heatmapComponent,Alignment.MIDDLE_CENTER);

        }
        heatmapComponent.updateData(Data_handler.getRowLabels(), Data_handler.getColumnLabels(), Data_handler.getDiseaseGroupComparisonsSet(), Data_handler.getFullQuantDsMap());

    }

}
