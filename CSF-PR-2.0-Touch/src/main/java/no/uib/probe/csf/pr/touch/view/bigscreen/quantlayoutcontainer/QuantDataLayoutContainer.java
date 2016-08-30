package no.uib.probe.csf.pr.touch.view.bigscreen.quantlayoutcontainer;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.Data_Handler;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseCategoryObject;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseGroupComparison;
import no.uib.probe.csf.pr.touch.logic.beans.HeatMapHeaderCellInformationBean;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFListener;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFPR_Central_Manager;
import no.uib.probe.csf.pr.touch.view.components.BubbleChartComponent;
import no.uib.probe.csf.pr.touch.view.components.HeatMapComponent;
import no.uib.probe.csf.pr.touch.view.components.LineChartProteinTableComponent;
import no.uib.probe.csf.pr.touch.view.components.PeptideViewComponent;
import no.uib.probe.csf.pr.touch.view.components.QuantInitialLayout;
import no.uib.probe.csf.pr.touch.view.core.ImageContainerBtn;
import no.uib.probe.csf.pr.touch.view.core.InformationButton;
import no.uib.probe.csf.pr.touch.view.core.ViewControlPanel;

/**
 *
 * @author Yehia Farag
 *
 * this class represents the main quant data layout container the layout is a
 * slide panel layout
 *
 */
public class QuantDataLayoutContainer extends ViewControlPanel implements CSFListener {

    private final VerticalLayout heatmapViewContainer, heatmapToolsContainer, bubblechartViewContainer, bubblechartToolsContainer, linechartViewContainer, linechartToolsContainer, peptidesViewContainer, peptidesToolsContainer;
    private final ImageContainerBtn heatmapBtn, bubblechartBtn, linechartBtn, peptideInfoBtn;
    private final Data_Handler Data_handler;
    private final CSFPR_Central_Manager CSFPR_Central_Manager;
    private final ThemeResource logoRes = new ThemeResource("img/logo.png");
    private final BubbleChartComponent bubblechartComponent;

    private HeatMapComponent heatmapComponent;

    private final int mainViewPanelWidth;
    private final int mainViewPanelHeight;
    private final QuantInitialLayout quantInitialLayout;
    private final LineChartProteinTableComponent lineChartProteinTableComponent;

    @Override
    public void selectionChanged(String type) {
        if (type.equalsIgnoreCase("quant_searching") || type.equalsIgnoreCase("quant_compare")) {

            //reset main quant data in handler
            //done
            //update initial layout
            quantInitialLayout.updateData(CSFPR_Central_Manager.getQuantSearchSelection().getDiseaseCategoriesIdMap());
            //update heatmap
            Set<QuantDiseaseGroupsComparison> compList = CSFPR_Central_Manager.getSelectedComparisonsList();
            heatmapComponent.unselectAll();
            Map<Integer, QuantDatasetObject> tempFullComparison =new LinkedHashMap<>();
            for(int i:Data_handler.getFullQuantDsMap().keySet()){
                if(CSFPR_Central_Manager.getQuantSearchSelection().getDatasetIds().contains(i))
                    tempFullComparison.put(i, Data_handler.getFullQuantDsMap().get(i));
            }
            
            heatmapComponent.updateData(Data_handler.getRowLabels(), Data_handler.getColumnLabels(), Data_handler.getDiseaseGroupComparisonsSet(),tempFullComparison );
            if (compList == null || compList.isEmpty()) {
                updateCurrentLayout("heatmap");

            }

            //
//            bubblechartComponent.addCustmisedUserDataCompariosn(null);
//            lineChartProteinTableComponent.setUserCustomizedComparison(null);
//            quantInitialLayout.updateSelection(CSFPR_Central_Manager.getQuantSearchSelection().getDiseaseCategory());
//            heatmapComponent.showSerumDs();
//            heatmapComponent.selectComparisonsByID(CSFPR_Central_Manager.getQuantSearchSelection().getDatasetIds());
//            this.updateCurrentLayout("proteintable");
//            lineChartProteinTableComponent.filterSearchSelection(CSFPR_Central_Manager.getQuantSearchSelection().getKeyWords());
        } else if (type.equalsIgnoreCase("reset_quant_searching")) {
            //update initial layout
            heatmapComponent.unselectAll();
            heatmapBtn.updateIcon(logoRes);
            heatmapBtn.setEnabled(false);
            heatmapBtn.setReadOnly(true);
//              quantInitialLayout.resetThumbBtn();
            quantInitialLayout.updateData(Data_handler.getDiseaseCategorySet());
            updateCurrentLayout("initiallayout");
            
            
            
            
            

            //reset heatmap and unselect all
        } 
        if (type.equalsIgnoreCase("quant_compare")) {
            
            
            //reset main quant data in handler
            //done
            //update initial layout
//            quantInitialLayout.updateData(CSFPR_Central_Manager.getQuantSearchSelection().getDiseaseCategoriesIdMap());
//
//            //update heatmap
//            Set<QuantDiseaseGroupsComparison> compList = CSFPR_Central_Manager.getSelectedComparisonsList();
//            heatmapComponent.unselectAll();
//             heatmapComponent.showSerumDs();
//             heatmapComponent.updateData(Data_handler.getRowLabels(), Data_handler.getColumnLabels(), Data_handler.getDiseaseGroupComparisonsSet(), Data_handler.getFullQuantDsMap());
//            if (compList == null || compList.isEmpty()) {
//                updateCurrentLayout("heatmap");
//
//            }            
            bubblechartComponent.addCustmisedUserDataCompariosn(CSFPR_Central_Manager.getQuantSearchSelection().getUserCustComparison());
            lineChartProteinTableComponent.setUserCustomizedComparison(CSFPR_Central_Manager.getQuantSearchSelection().getUserCustComparison());
//            if (CSFPR_Central_Manager.getQuantSearchSelection().getUserCustComparison() == null) {
//                heatmapComponent.unselectAll();
//                quantInitialLayout.updateSelection("All Diseases");
//                this.updateCurrentLayout("heatmap");
//
//                return;
//            }
////
//////            quantInitialLayout.updateSelection(CSFPR_Central_Manager.getQuantSearchSelection().getDiseaseCategory());
////           
////            heatmapComponent.selectComparisonsByID(CSFPR_Central_Manager.getQuantSearchSelection().getDatasetIds());
//            this.updateCurrentLayout("proteintable");
//            lineChartProteinTableComponent.filterSearchSelection(CSFPR_Central_Manager.getQuantSearchSelection().getKeyWords());

        } else if (type.equalsIgnoreCase("comparisons_selection")) {

            Set<QuantDiseaseGroupsComparison> compList = CSFPR_Central_Manager.getSelectedComparisonsList();
            if (compList == null || compList.isEmpty()) {
                updateCurrentLayout("heatmap");

            }
        }

    }

    @Override
    public String getFilterId() {
        return this.getClass().getName();
    }

    @Override
    public void removeFilterValue(String value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private final boolean smallScreen;

    public QuantDataLayoutContainer(final Data_Handler Data_handler, CSFPR_Central_Manager CSFPR_Central_Manager, int width, int height) {
        super(width, height);
        smallScreen = height <= 720;
        this.Data_handler = Data_handler;
        this.CSFPR_Central_Manager = CSFPR_Central_Manager;
        this.setMargin(false);
        this.addStyleName("slowslide");
        Collection<DiseaseCategoryObject> availableDiseaseCategory = Data_handler.getDiseaseCategorySet();
        final HorizontalLayout subBodyWrapper = new HorizontalLayout();
        subBodyWrapper.setWidthUndefined();
        subBodyWrapper.setHeightUndefined();
        
        
        quantInitialLayout = new QuantInitialLayout(availableDiseaseCategory, width - 20, height, smallScreen) {
            private String lastSelectedDisease = "";

            @Override
            public void onClick(String diseaseCategoryName) {
                defaultView();
                if (CSFPR_Central_Manager.getQuantSearchSelection() != null) {
                    if (lastSelectedDisease.equalsIgnoreCase(diseaseCategoryName)) {
                        return;
                    }
                    lastSelectedDisease = diseaseCategoryName;
                    Set<QuantDiseaseGroupsComparison> compList = CSFPR_Central_Manager.getSelectedComparisonsList();
                    heatmapComponent.unselectAll();

                    if (!diseaseCategoryName.equalsIgnoreCase("All Diseases")) {

                        Set<DiseaseGroupComparison> updatedSet = new LinkedHashSet<>();
                        for (DiseaseGroupComparison dcat : Data_handler.getDiseaseGroupComparisonsSet()) {
                            if (dcat.getDiseaseCategory().equalsIgnoreCase(diseaseCategoryName)) {
                                updatedSet.add(dcat);
                            }
                        }
                        LinkedHashSet<HeatMapHeaderCellInformationBean> rowLabels = new LinkedHashSet<>();
                        LinkedHashSet<HeatMapHeaderCellInformationBean> colLabels = new LinkedHashSet<>();
                        for (HeatMapHeaderCellInformationBean row : Data_handler.getRowLabels()) {
                            if (row.getDiseaseCategory().equalsIgnoreCase(lastSelectedDisease)) {
                                rowLabels.add(row);
                            }

                        }
                        for (HeatMapHeaderCellInformationBean col : Data_handler.getColumnLabels()) {
                            if (col.getDiseaseCategory().equalsIgnoreCase(lastSelectedDisease)) {
                                colLabels.add(col);
                            }

                        }
                        heatmapComponent.updateData(rowLabels, colLabels, updatedSet, Data_handler.getFullQuantDsMap());
                    } else {
                        heatmapComponent.updateData(Data_handler.getRowLabels(), Data_handler.getColumnLabels(), Data_handler.getDiseaseGroupComparisonsSet(), Data_handler.getFullQuantDsMap());
                    }
                    if (compList == null || compList.isEmpty()) {
                        updateCurrentLayout("heatmap");

                    }

                } else if (!lastSelectedDisease.equalsIgnoreCase(diseaseCategoryName)) {
                    lastSelectedDisease = diseaseCategoryName;
                    //do functions
                    //load dataset
                    loadDiseaseCategory(diseaseCategoryName);
                }

            }

        };
        VerticalLayout controlBtnsContainer = new VerticalLayout();
        controlBtnsContainer.setHeightUndefined();
        controlBtnsContainer.setWidthUndefined();
        controlBtnsContainer.setSpacing(true);
        InformationButton info = new InformationButton("Info", false);
        if (smallScreen) {
            info.setWidth(25, Unit.PIXELS);
            info.setHeight(25, Unit.PIXELS);
            info.removeStyleName("smallimg");
        } else {

            info.setWidth(40, Unit.PIXELS);
            info.setHeight(40, Unit.PIXELS);
        }

        controlBtnsContainer.addComponent(info);
        controlBtnsContainer.setVisible(false);

//        this.setInitialLayout(quantInitialLayout.getMiniLayout(), quantInitialLayout, controlBtnsContainer);
        
        this.addButton(quantInitialLayout.getMiniLayout(), quantInitialLayout, controlBtnsContainer, true);

        heatmapBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                if (this.isEnabled()) {
                    processFunction("heatmap");
                }
            }
        };
        heatmapBtn.updateIcon(logoRes);
        if (smallScreen) {
            heatmapBtn.setWidth(60, Unit.PIXELS);
            heatmapBtn.setHeight(60, Unit.PIXELS);
        } else {
            heatmapBtn.setWidth(100, Unit.PIXELS);
            heatmapBtn.setHeight(100, Unit.PIXELS);
        }

        mainViewPanelHeight = height;
        if (smallScreen) {
            mainViewPanelWidth = width - 118;
        } else {
            mainViewPanelWidth = width-178 ;
        }
        heatmapViewContainer = new VerticalLayout();
        heatmapViewContainer.setWidth(mainViewPanelWidth, Unit.PIXELS);
        heatmapViewContainer.setHeight(mainViewPanelHeight,Unit.PIXELS);
       
        heatmapToolsContainer = new VerticalLayout();
        heatmapBtn.setHasWrapper(true);
        this.addButton(heatmapBtn, heatmapViewContainer, heatmapToolsContainer, false);

        Data_handler.loadDiseaseCategory("All Diseases");

        heatmapComponent = new HeatMapComponent(CSFPR_Central_Manager, Data_handler, Data_handler.getDiseaseCategorySet(), mainViewPanelWidth, mainViewPanelHeight-2, Data_handler.getActiveDataColumns(), smallScreen) {

            @Override
            public void updateIcon(String imageUrl) {
                heatmapBtn.updateIcon(new ExternalResource(imageUrl));
                heatmapBtn.setEnabled(true);
            }

            @Override
            public void updateCombinedGroups(Map<String, Map<String, String>> updatedGroupsNamesMap) {
                Data_handler.updateCombinedGroups(updatedGroupsNamesMap);
                heatmapComponent.updateData(Data_handler.getRowLabels(), Data_handler.getColumnLabels(), Data_handler.getDiseaseGroupComparisonsSet(), Data_handler.getFullQuantDsMap());
            }

            @Override
            public void updateCSFSerumDatasets(boolean serumApplied, boolean csfApplied) {
                Data_handler.updateCSFSerumDatasets(serumApplied, csfApplied);
                heatmapComponent.updateData(Data_handler.getRowLabels(), Data_handler.getColumnLabels(), Data_handler.getDiseaseGroupComparisonsSet(), Data_handler.getFullQuantDsMap());
            }

            @Override
            public void blinkIcon() {
                heatmapBtn.blink();
            }

        };
//        VerticalLayout testlayout = new VerticalLayout();
//        testlayout.setStyleName("bluelayout");
//        testlayout.setWidth(mainViewPanelWidth + 100, mainViewPanelHeight);
        
        heatmapViewContainer.addComponent(heatmapComponent);
        heatmapViewContainer.setComponentAlignment(heatmapComponent, Alignment.TOP_LEFT);
        
        heatmapToolsContainer.addComponent(heatmapComponent.getHeatmapToolBtnContainer());
        heatmapToolsContainer.setComponentAlignment(heatmapComponent.getHeatmapToolBtnContainer(), Alignment.TOP_RIGHT);

        bubblechartBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                if (this.isEnabled()) {
                    processFunction("bubblechart");
                }
            }
        };
        bubblechartBtn.updateIcon(logoRes);

        if (smallScreen) {
            bubblechartBtn.setWidth(60, Unit.PIXELS);
            bubblechartBtn.setHeight(60, Unit.PIXELS);
        } else {

            bubblechartBtn.setWidth(100, Unit.PIXELS);
            bubblechartBtn.setHeight(100, Unit.PIXELS);
        }
        bubblechartBtn.setEnabled(false);

        bubblechartViewContainer = new VerticalLayout();
        bubblechartViewContainer.setWidth(mainViewPanelWidth, Unit.PIXELS);
        bubblechartViewContainer.setHeight(mainViewPanelHeight,Unit.PIXELS);
        
        
        
        bubblechartToolsContainer = new VerticalLayout();
        bubblechartBtn.setHasWrapper(true);
        this.addButton(bubblechartBtn, bubblechartViewContainer, bubblechartToolsContainer, false);

        linechartBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                if (this.isEnabled()) {
                    processFunction("linechart");
                }
            }
        };
        linechartBtn.updateIcon(logoRes);

        if (smallScreen) {
            linechartBtn.setWidth(60, Unit.PIXELS);
            linechartBtn.setHeight(60, Unit.PIXELS);
        } else {
            linechartBtn.setWidth(100, Unit.PIXELS);
            linechartBtn.setHeight(100, Unit.PIXELS);
        }

        linechartViewContainer = new VerticalLayout();
         linechartViewContainer.setWidth(mainViewPanelWidth, Unit.PIXELS);
        linechartViewContainer.setHeight(mainViewPanelHeight,Unit.PIXELS);
        linechartToolsContainer = new VerticalLayout();

        this.addButton(linechartBtn, linechartViewContainer, linechartToolsContainer, false);

        peptideInfoBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                if (this.isEnabled()) {
                    processFunction("peptidelayout");
                }
            }
        };
        peptideInfoBtn.updateIcon(logoRes);
        if (smallScreen) {
            peptideInfoBtn.setWidth(60, Unit.PIXELS);
            peptideInfoBtn.setHeight(60, Unit.PIXELS);
        } else {
            peptideInfoBtn.setWidth(100, Unit.PIXELS);
            peptideInfoBtn.setHeight(100, Unit.PIXELS);
        }

        peptidesViewContainer = new VerticalLayout();
         peptidesViewContainer.setWidth(mainViewPanelWidth, Unit.PIXELS);
        peptidesViewContainer.setHeight(mainViewPanelHeight,Unit.PIXELS);
        peptidesToolsContainer = new VerticalLayout();
        linechartBtn.setHasWrapper(true);
        peptideInfoBtn.setHasWrapper(true);
        this.addButton(peptideInfoBtn, peptidesViewContainer, peptidesToolsContainer, false);

        ///init bubble chart container
        bubblechartComponent = new BubbleChartComponent(CSFPR_Central_Manager, mainViewPanelWidth, mainViewPanelHeight-2, smallScreen) {

            @Override
            public void updateIcon(String imageUrl) {
                if (imageUrl == null) {
                    bubblechartBtn.updateIcon(logoRes);
                    linechartBtn.updateIcon(logoRes);
                    bubblechartBtn.setEnabled(false);
                    linechartBtn.setEnabled(false);
                    return;
                }
                bubblechartBtn.setEnabled(true);
                bubblechartBtn.updateIcon(new ExternalResource(imageUrl));
//                
//                linechartBtn.updateIcon(new ThemeResource("img/table.png"));

            }

        };
        bubblechartViewContainer.addComponent(bubblechartComponent);
        bubblechartToolsContainer.addComponent(bubblechartComponent.getControlBtnsContainer());
        bubblechartToolsContainer.setComponentAlignment(bubblechartComponent.getControlBtnsContainer(), Alignment.TOP_RIGHT);

        lineChartProteinTableComponent = new LineChartProteinTableComponent(CSFPR_Central_Manager, mainViewPanelWidth, mainViewPanelHeight-2, null, smallScreen) {

            @Override
            public void updateRowNumber(int rowNumber, String url) {
                linechartBtn.setEnabled(true);
                linechartBtn.updateText(rowNumber + "");
                linechartBtn.updateIcon(new ExternalResource(url));

            }

        };
        linechartViewContainer.addComponent(lineChartProteinTableComponent);
        linechartToolsContainer.addComponent(lineChartProteinTableComponent.getControlBtnsContainer());
        linechartToolsContainer.setComponentAlignment(lineChartProteinTableComponent.getControlBtnsContainer(), Alignment.TOP_RIGHT);

        PeptideViewComponent peptideViewComponent = new PeptideViewComponent(CSFPR_Central_Manager, mainViewPanelWidth, mainViewPanelHeight-2, smallScreen) {

            @Override
            public void updateIcon(Resource iconResource) {
                if (iconResource == null) {
                    peptideInfoBtn.setEnabled(false);
                    peptideInfoBtn.updateIcon(logoRes);
                } else {
                    peptideInfoBtn.setEnabled(true);
                    peptideInfoBtn.updateIcon(iconResource);
                }
            }

        };
        peptidesViewContainer.addComponent(peptideViewComponent);
        peptidesToolsContainer.addComponent(peptideViewComponent.getControlBtnsContainer());
        peptidesToolsContainer.setComponentAlignment(peptideViewComponent.getControlBtnsContainer(), Alignment.TOP_RIGHT);
        this.CSFPR_Central_Manager.registerListener(QuantDataLayoutContainer.this);

    }

    private void processFunction(String btnId) {
        switch (btnId) {

            case "heatmap":
                break;

        }

    }

    private void loadDiseaseCategory(String DiseaseCategoryName) {
        Data_handler.loadDiseaseCategory(DiseaseCategoryName);
        heatmapComponent.updateData(Data_handler.getRowLabels(), Data_handler.getColumnLabels(), Data_handler.getDiseaseGroupComparisonsSet(), Data_handler.getFullQuantDsMap());

    }

}
