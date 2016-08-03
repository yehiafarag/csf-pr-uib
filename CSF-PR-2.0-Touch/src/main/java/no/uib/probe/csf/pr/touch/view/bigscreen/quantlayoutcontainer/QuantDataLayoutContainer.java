package no.uib.probe.csf.pr.touch.view.bigscreen.quantlayoutcontainer;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.Data_Handler;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseCategoryObject;
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
        if (type.equalsIgnoreCase("quant_searching")) {

            bubblechartComponent.addCustmisedUserDataCompariosn(null);
            lineChartProteinTableComponent.setUserCustomizedComparison(null);
            quantInitialLayout.updateSelection(CSFPR_Central_Manager.getQuantSearchSelection().getDiseaseCategory());
            heatmapComponent.showSerumDs();
            heatmapComponent.selectComparisonsByID(CSFPR_Central_Manager.getQuantSearchSelection().getDatasetIds());
            this.updateCurrentLayout("proteintable");
            lineChartProteinTableComponent.filterSearchSelection(CSFPR_Central_Manager.getQuantSearchSelection().getKeyWords());

        } else if (type.equalsIgnoreCase("quant_compare")) {
             bubblechartComponent.addCustmisedUserDataCompariosn(CSFPR_Central_Manager.getQuantSearchSelection().getUserCustComparison());
            lineChartProteinTableComponent.setUserCustomizedComparison(CSFPR_Central_Manager.getQuantSearchSelection().getUserCustComparison());
            if(CSFPR_Central_Manager.getQuantSearchSelection().getUserCustComparison()==null){
             heatmapComponent.unselectAll();
              quantInitialLayout.updateSelection("All Diseases");
              this.updateCurrentLayout("heatmap");
            
                return;
            }
            
           
            quantInitialLayout.updateSelection(CSFPR_Central_Manager.getQuantSearchSelection().getDiseaseCategory());
            heatmapComponent.showSerumDs();
            heatmapComponent.selectComparisonsByID(CSFPR_Central_Manager.getQuantSearchSelection().getDatasetIds());
            this.updateCurrentLayout("proteintable");
            lineChartProteinTableComponent.filterSearchSelection(CSFPR_Central_Manager.getQuantSearchSelection().getKeyWords());

        } else  if (type.equalsIgnoreCase("comparisons_selection")) {
            

            Set<QuantDiseaseGroupsComparison> compList = CSFPR_Central_Manager.getSelectedComparisonsList();
            if (compList == null || compList.isEmpty()) {
                System.out.println("at update view to default");
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

    public QuantDataLayoutContainer(final Data_Handler Data_handler, CSFPR_Central_Manager CSFPR_Central_Manager, int width, int height) {
        super(width, height);
        this.Data_handler = Data_handler;
        this.CSFPR_Central_Manager = CSFPR_Central_Manager;
        this.setMargin(false);
        this.addStyleName("slowslide");

        Collection<DiseaseCategoryObject> availableDiseaseCategory = Data_handler.getDiseaseCategorySet();
        final HorizontalLayout subBodyWrapper = new HorizontalLayout();
        subBodyWrapper.setWidthUndefined();
//        subBodyWrapper.setHeight((height - 200), Unit.PIXELS);
        subBodyWrapper.setHeightUndefined();

        height = height - 42;
        quantInitialLayout = new QuantInitialLayout(availableDiseaseCategory, width - 20, height) {
            private String lastSelectedDisease = "";

            @Override
            public void onClick(String diseaseCategoryName) {
                defaultView();
                if (!lastSelectedDisease.equalsIgnoreCase(diseaseCategoryName)) {
                    lastSelectedDisease = diseaseCategoryName;
                    //do functions
                    //load dataset
                    loadDiseaseCategory(diseaseCategoryName);
                }

            }

        };
        VerticalLayout controlBtnsContainer  = new VerticalLayout();
        controlBtnsContainer.setHeightUndefined();
        controlBtnsContainer.setWidthUndefined();
        controlBtnsContainer.setSpacing(true);
        InformationButton info = new InformationButton("Info", false);
        info.setWidth(40,Unit.PIXELS);
        info.setHeight(40,Unit.PIXELS);
        controlBtnsContainer.addComponent(info);
        controlBtnsContainer.setVisible(false);
        
        this.setInitialLayout(quantInitialLayout.getMiniLayout(), quantInitialLayout, controlBtnsContainer);

        heatmapBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                if (this.isEnabled()) {
                    processFunction("heatmap");
                }
            }
        };
        heatmapBtn.updateIcon(logoRes);
        heatmapBtn.setWidth(100, Unit.PIXELS);
        heatmapBtn.setHeight(100, Unit.PIXELS);

        mainViewPanelHeight = height;
        mainViewPanelWidth = width - 220;
        heatmapViewContainer = new VerticalLayout();
        heatmapViewContainer.setWidth(mainViewPanelWidth, Unit.PIXELS);
        heatmapToolsContainer = new VerticalLayout();
        heatmapBtn.setHasWrapper(true);
        this.addButton(heatmapBtn, heatmapViewContainer, heatmapToolsContainer, true);

        Data_handler.loadDiseaseCategory("All Diseases");

        heatmapComponent = new HeatMapComponent(CSFPR_Central_Manager, Data_handler, Data_handler.getDiseaseCategorySet(), mainViewPanelWidth, mainViewPanelHeight, Data_handler.getActiveDataColumns()) {

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
        heatmapViewContainer.addComponent(heatmapComponent);
        heatmapViewContainer.setComponentAlignment(heatmapComponent, Alignment.MIDDLE_CENTER);
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
        bubblechartBtn.setWidth(100, Unit.PIXELS);
        bubblechartBtn.setHeight(100, Unit.PIXELS);
        bubblechartBtn.setEnabled(false);

        bubblechartViewContainer = new VerticalLayout();
        bubblechartViewContainer.setWidth(mainViewPanelWidth, Unit.PIXELS);
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
        linechartBtn.setWidth(100, Unit.PIXELS);
        linechartBtn.setHeight(100, Unit.PIXELS);

        linechartViewContainer = new VerticalLayout();
        linechartViewContainer.setWidth(mainViewPanelWidth, Unit.PIXELS);
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
        peptideInfoBtn.setWidth(100, Unit.PIXELS);
        peptideInfoBtn.setHeight(100, Unit.PIXELS);

        peptidesViewContainer = new VerticalLayout();
        peptidesViewContainer.setWidth(mainViewPanelWidth, Unit.PIXELS);
        peptidesToolsContainer = new VerticalLayout();
        linechartBtn.setHasWrapper(true);
        peptideInfoBtn.setHasWrapper(true);
        this.addButton(peptideInfoBtn, peptidesViewContainer, peptidesToolsContainer, false);

        ///init bubble chart container
        bubblechartComponent = new BubbleChartComponent(CSFPR_Central_Manager, mainViewPanelWidth, mainViewPanelHeight) {

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
                linechartBtn.setEnabled(true);
                linechartBtn.updateIcon(new ThemeResource("img/table.png"));

            }

        };
        bubblechartViewContainer.addComponent(bubblechartComponent);
        bubblechartToolsContainer.addComponent(bubblechartComponent.getControlBtnsContainer());
        bubblechartToolsContainer.setComponentAlignment(bubblechartComponent.getControlBtnsContainer(), Alignment.TOP_RIGHT);

        lineChartProteinTableComponent = new LineChartProteinTableComponent(CSFPR_Central_Manager, mainViewPanelWidth, mainViewPanelHeight, null) {

            @Override
            public void updateRowNumber(int rowNumber,String url) {
                linechartBtn.updateText(rowNumber + "");
                linechartBtn.updateIcon(new ExternalResource(url));
            }

        };
        linechartViewContainer.addComponent(lineChartProteinTableComponent);
        linechartToolsContainer.addComponent(lineChartProteinTableComponent.getControlBtnsContainer());
        linechartToolsContainer.setComponentAlignment(lineChartProteinTableComponent.getControlBtnsContainer(), Alignment.TOP_RIGHT);

        PeptideViewComponent peptideViewComponent = new PeptideViewComponent(CSFPR_Central_Manager, mainViewPanelWidth, mainViewPanelHeight) {

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
