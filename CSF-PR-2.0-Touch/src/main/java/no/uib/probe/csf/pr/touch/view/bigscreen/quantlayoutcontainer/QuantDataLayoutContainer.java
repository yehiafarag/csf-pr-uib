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

/**
 *
 * @author Yehia Farag
 *
 * this class represents the main quant data layout container the layout is a
 * slide panel layout
 *
 */
public class QuantDataLayoutContainer extends VerticalLayout {

    private final VerticalLayout sideButtonsContainer;
    private final VerticalLayout quantBodyWrapper;
    private final VerticalLayout mainComponetViewPanel;
    private final ImageContainerBtn heatmapBtn, bubblechartBtn, tableBtn, linechartBtn, peptideInfoBtn;
    private final Data_Handler Data_handler;
    private final CSFPR_Central_Manager CSFPR_Central_Manager;
    private final ScrollPanel initialLayoutPanel;
    private HeatMapComponent heatmapComponent;

    private int mainViewPanelWidth;
    private int mainViewPanelHeight;
    public QuantDataLayoutContainer(final Data_Handler Data_handler, int width, int height) {
        this.Data_handler = Data_handler;
        CSFPR_Central_Manager = new CSFPR_Central_Manager(Data_handler.getQuantDatasetInitialInformationObject(),Data_handler.getActivePieChartQuantFilters(),Data_handler.getDefault_DiseaseCat_DiseaseGroupMap(),Data_handler.getDiseaseStyleMap());
        this.setWidth(width + "px");
        this.setHeight(height + "px");

        quantBodyWrapper = new VerticalLayout();
        quantBodyWrapper.setWidthUndefined();
        quantBodyWrapper.setHeightUndefined();
        quantBodyWrapper.setWidthUndefined();
        quantBodyWrapper.setSpacing(true);
        this.addComponent(quantBodyWrapper);

        Set<DiseaseCategoryObject> availableDiseaseCategory = Data_handler.getDiseaseCategorySet();
        QuantInitialLayout quantInitialLayout = new QuantInitialLayout(availableDiseaseCategory, width, height - 100) {

            private String lastSelectedDisease = "";
            private boolean showPanel = true;

            @Override
            public void onClick(String diseaseCategoryName) {
                if (!lastSelectedDisease.equalsIgnoreCase(diseaseCategoryName)) {
                    lastSelectedDisease = diseaseCategoryName;
                    //do functions
                    //load dataset
                    loadDiseaseCategory(diseaseCategoryName);
                    

                }
                initialLayoutPanel.setShowPanel(!showPanel);
                showPanel = !showPanel;

            }

        };

        initialLayoutPanel = new ScrollPanel(quantInitialLayout, quantInitialLayout.getMiniLayout(), 0, "diseasecategoryselectionset");
        initialLayoutPanel.setShowNavigationBtn(false);
        initialLayoutPanel.setShowPanel(true);
        quantBodyWrapper.addComponent(initialLayoutPanel);
        quantBodyWrapper.setComponentAlignment(initialLayoutPanel, Alignment.TOP_LEFT);

        HorizontalLayout subBodyWrapper = new HorizontalLayout();
        subBodyWrapper.setWidthUndefined();
        subBodyWrapper.setHeight((height - 200), Unit.PIXELS);
        quantBodyWrapper.addComponent(subBodyWrapper);
        quantBodyWrapper.setComponentAlignment(subBodyWrapper, Alignment.TOP_LEFT);

        sideButtonsContainer = new VerticalLayout();
        sideButtonsContainer.setWidth("150px");

        sideButtonsContainer.setSpacing(true);
        sideButtonsContainer.setMargin(new MarginInfo(true, false, false, true));
        sideButtonsContainer.setVisible(true);

        subBodyWrapper.addComponent(sideButtonsContainer);
        subBodyWrapper.setComponentAlignment(sideButtonsContainer, Alignment.MIDDLE_CENTER);

        heatmapBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                processFunction("heatmap");
            }
        };
        heatmapBtn.updateIcon(new ThemeResource("img/logo.png"));
        sideButtonsContainer.addComponent(heatmapBtn);

        bubblechartBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                processFunction("bubblechart");
            }
        };
        bubblechartBtn.updateIcon(new ThemeResource("img/logo.png"));
        sideButtonsContainer.addComponent(bubblechartBtn);

        tableBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                processFunction("proteintable");
            }
        };
        tableBtn.updateIcon(new ThemeResource("img/logo.png"));
        sideButtonsContainer.addComponent(tableBtn);

        linechartBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                processFunction("linechart");
            }
        };
        linechartBtn.updateIcon(new ThemeResource("img/logo.png"));
        sideButtonsContainer.addComponent(linechartBtn);

        peptideInfoBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                processFunction("peptidelayout");
            }
        };
        peptideInfoBtn.updateIcon(new ThemeResource("img/logo.png"));
        sideButtonsContainer.addComponent(peptideInfoBtn);

        mainViewPanelHeight = height-200;
        mainViewPanelWidth=width-200;
        mainComponetViewPanel = new VerticalLayout();
        subBodyWrapper.addComponent(mainComponetViewPanel);
        mainComponetViewPanel.setWidth((mainViewPanelWidth) + "px");
        mainComponetViewPanel.setHeight("100%");

//        mainComponetViewPanel.addComponent(quantInitialLayout);
    }

    private void processFunction(String btnId) {
        switch (btnId) {

            case "heatmap":
                bubblechartBtn.blink();
                break;

        }

    }
    
    
    private void loadDiseaseCategory(String DiseaseCategoryName){
        Data_handler.loadDiseaseCategory(DiseaseCategoryName);
        if(heatmapComponent == null){
            heatmapComponent= new HeatMapComponent(CSFPR_Central_Manager,mainViewPanelWidth, mainViewPanelHeight);        
            mainComponetViewPanel.addComponent(heatmapComponent);
        
        }
    
    }

}
