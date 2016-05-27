package no.uib.probe.csf.pr.touch.view.bigscreen.quantlayoutcontainer;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import java.util.Collection;
import java.util.Map;
import no.uib.probe.csf.pr.touch.Data_Handler;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseCategoryObject;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFPR_Central_Manager;
import no.uib.probe.csf.pr.touch.view.components.BubbleChartComponent;
import no.uib.probe.csf.pr.touch.view.components.HeatMapComponent;
import no.uib.probe.csf.pr.touch.view.components.QuantInitialLayout;
import no.uib.probe.csf.pr.touch.view.core.ImageContainerBtn;
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

    private final VerticalLayout heatmapViewContainer, bubblechartViewContainer;
    private final ImageContainerBtn heatmapBtn, bubblechartBtn, tableBtn, linechartBtn, peptideInfoBtn;
    private final Data_Handler Data_handler;
    private final CSFPR_Central_Manager CSFPR_Central_Manager;

    private HeatMapComponent heatmapComponent;

    private final int mainViewPanelWidth;
    private final int mainViewPanelHeight;
    private final QuantInitialLayout quantInitialLayout;

    public QuantDataLayoutContainer(final Data_Handler Data_handler, int width, int height) {
        super(width, height);
        this.Data_handler = Data_handler;
        CSFPR_Central_Manager = new CSFPR_Central_Manager();
        this.setMargin(true);
        this.addStyleName("slowslide");

        Collection<DiseaseCategoryObject> availableDiseaseCategory = Data_handler.getDiseaseCategorySet();
        final HorizontalLayout subBodyWrapper = new HorizontalLayout();
        subBodyWrapper.setWidthUndefined();
//        subBodyWrapper.setHeight((height - 200), Unit.PIXELS);
        subBodyWrapper.setHeightUndefined();

        height=height-42;
        quantInitialLayout = new QuantInitialLayout(availableDiseaseCategory, width, height) {

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
        this.setInitialLayout(quantInitialLayout.getMiniLayout(), quantInitialLayout);

        heatmapBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                processFunction("heatmap");
            }
        };
        heatmapBtn.updateIcon(new ThemeResource("img/logo.png"));
        heatmapBtn.setWidth(100, Unit.PIXELS);
        heatmapBtn.setHeight(100, Unit.PIXELS);

        mainViewPanelHeight = height;
        mainViewPanelWidth = width - 220;
        heatmapViewContainer = new VerticalLayout();
        heatmapViewContainer.setWidth(mainViewPanelWidth, Unit.PIXELS);
        this.addButton(heatmapBtn, heatmapViewContainer, true);

        bubblechartBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                processFunction("bubblechart");
            }
        };
        bubblechartBtn.updateIcon(new ThemeResource("img/logo.png"));
        bubblechartBtn.setWidth(100, Unit.PIXELS);
        bubblechartBtn.setHeight(100, Unit.PIXELS);

        bubblechartViewContainer = new VerticalLayout();
        bubblechartViewContainer.setWidth(mainViewPanelWidth, Unit.PIXELS);

        this.addButton(bubblechartBtn, bubblechartViewContainer, false);

        tableBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                processFunction("proteintable");
            }
        };
        tableBtn.updateIcon(new ThemeResource("img/logo.png"));
        tableBtn.setWidth(100, Unit.PIXELS);
        tableBtn.setHeight(100, Unit.PIXELS);
        this.addButton(tableBtn, new VerticalLayout(), false);

        linechartBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                processFunction("linechart");
            }
        };
        linechartBtn.updateIcon(new ThemeResource("img/logo.png"));
        linechartBtn.setWidth(100, Unit.PIXELS);
        linechartBtn.setHeight(100, Unit.PIXELS);
        this.addButton(linechartBtn, new VerticalLayout(), false);

        peptideInfoBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                processFunction("peptidelayout");
            }
        };
        peptideInfoBtn.updateIcon(new ThemeResource("img/logo.png"));
        peptideInfoBtn.setWidth(100, Unit.PIXELS);
        peptideInfoBtn.setHeight(100, Unit.PIXELS);
        this.addButton(peptideInfoBtn, new VerticalLayout(), false);

        ///init bubble chart container
        BubbleChartComponent bubblechartComponent = new BubbleChartComponent(CSFPR_Central_Manager, mainViewPanelWidth, mainViewPanelHeight, null) {

            @Override
            public void updateIcon(String imageUrl) {
                if (imageUrl == null) {
                    bubblechartBtn.updateIcon(new ThemeResource("img/logo.png"));
                    bubblechartBtn.setEnabled(false);
                    return;
                }
                bubblechartBtn.setEnabled(true);
                bubblechartBtn.updateIcon(new ExternalResource(imageUrl));
            }

        };
        bubblechartViewContainer.addComponent(bubblechartComponent);
    }

    private void processFunction(String btnId) {
        switch (btnId) {

            case "heatmap":
                break;

        }

    }

    private void loadDiseaseCategory(String DiseaseCategoryName) {
        Data_handler.loadDiseaseCategory(DiseaseCategoryName);
        if (heatmapComponent == null) {
            heatmapComponent = new HeatMapComponent(CSFPR_Central_Manager, Data_handler, Data_handler.getDiseaseCategorySet(), mainViewPanelWidth, mainViewPanelHeight, Data_handler.getActiveDataColumns()) {

                @Override
                public void updateIcon(String imageUrl) {
                    heatmapBtn.updateIcon(new ExternalResource(imageUrl));
                    bubblechartBtn.setEnabled(true);
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
             heatmapViewContainer.setComponentAlignment(heatmapComponent,Alignment.MIDDLE_CENTER);

        }
        heatmapComponent.updateData(Data_handler.getRowLabels(), Data_handler.getColumnLabels(), Data_handler.getDiseaseGroupComparisonsSet(), Data_handler.getFullQuantDsMap());

    }

}
