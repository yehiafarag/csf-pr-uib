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
import no.uib.probe.csf.pr.touch.logic.beans.QuantDataset;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFListener;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFPR_Central_Manager;
import no.uib.probe.csf.pr.touch.view.components.DiseaseComparisonSelectionBubblechartComponent;
import no.uib.probe.csf.pr.touch.view.components.DiseaseComparisonHeatmapComponent;
import no.uib.probe.csf.pr.touch.view.components.LineChartProteinTableComponent;
import no.uib.probe.csf.pr.touch.view.components.PeptideViewComponent;
import no.uib.probe.csf.pr.touch.view.components.InitialDiseaseCategoriesComponent;
import no.uib.probe.csf.pr.touch.view.core.ImageContainerBtn;
import no.uib.probe.csf.pr.touch.view.core.InformationButton;
import no.uib.probe.csf.pr.touch.view.core.ViewControlPanel;

/**
 *
 * @author Yehia Farag
 *
 * This class represents the main quantitative data layout container the layout
 * this include the initial disease categories layout (O o O), the disease
 * comparison heat-map, the disease comparisons selection bubble chart, the
 * proteins table and selected protein layout (protein coverage, peptide
 * information, and studies line chart)
 *
 */
public class QuantDataLayoutContainer extends ViewControlPanel implements CSFListener {

    /*
     *Initial disease categories layout (O o O)
     */
    private final InitialDiseaseCategoriesComponent initialDiseaseCategoriesComponent;

    /*
     *The disease comparison heat-map container
     */
    private final VerticalLayout heatmapViewContainer;
    /*
     *The disease comparison heat-map right side control buttons container
     */
    private final VerticalLayout heatmapToolsContainer;
    /*
     *The disease comparison heat-map left side thumb image button
     */
    private final ImageContainerBtn heatmapBtn;
    /*
     *The disease comparison heat-map component
     */
    private DiseaseComparisonHeatmapComponent diseaseComparisonHeatmapComponent;

    /*
     *The disease comparisons selection bubble chart
     */
    private final VerticalLayout bubblechartViewContainer;
    /*
     *The disease comparisons selection bubble chart right side control buttons container
     */
    private final VerticalLayout bubblechartToolsContainer;

    /*
     *The disease comparison selection bubble chart left side thumb image button
     */
    private final ImageContainerBtn bubblechartBtn;
    /*
     *The disease comparison selection bubble chart component
     */
    private final DiseaseComparisonSelectionBubblechartComponent diseaseComparisonSelectionbubblechartComponent;

    /*
     *The disease comparisons selection proteins table
     */
    private final VerticalLayout linechartViewContainer;
    /*
     *The disease comparisons selection proteins table right side control buttons container
     */
    private final VerticalLayout linechartToolsContainer;
    /*
     *The disease comparison selection proteins table left side thumb image button
     */
    private final ImageContainerBtn linechartBtn;

    /*
     *The disease comparison selection proteins table component
     */
    private final LineChartProteinTableComponent lineChartProteinTableComponent;

    /*
     *The selected protein layout (protein coverage, peptide information, and studies line chart)
     */
    private final VerticalLayout peptidesViewContainer;
    /*
     *The selected protein layout (protein coverage, peptide information, and studies line chart) right side control buttons container
     */
    private final VerticalLayout peptidesToolsContainer;
    /*
     *The selected protein layout (protein coverage, peptide information, and studies line chart)  left side thumb image button
     */
    private final ImageContainerBtn peptideInfoBtn;

    /*
     *The quantative data handler to work as controller layer to interact between visulization and logic layer 
     */
    private final Data_Handler Data_handler;

    /*
     *The central manager for handling data across different visualizations and managing all users selections
     */
    private final CSFPR_Central_Manager CSFPR_Central_Manager;

    /*
     *The standered thumb image for left side components buttons, in case of no selection provided (null selection)
     */
    private final ThemeResource logoRes = new ThemeResource("img/logo.png");
    /*
     *The width of the component view panel (the middle panle between the left side and right side buttons wrappers)
     */
    private final int mainViewPanelWidth;
    /*
     *The height of the component view panel (the middle panle between the left side and right side buttons wrappers)
     */
    private final int mainViewPanelHeight;

    @Override
    public void selectionChanged(String type) {
        if (type.equalsIgnoreCase("quant_searching") || type.equalsIgnoreCase("quant_compare")) {
            //update initial layout
            initialDiseaseCategoriesComponent.updateData(CSFPR_Central_Manager.getQuantSearchSelection().getDiseaseCategoriesIdMap());
            //update heatmap
            CSFPR_Central_Manager.getSelectedComparisonsList();
            diseaseComparisonHeatmapComponent.unselectAll();
            Map<Integer, QuantDataset> tempFullComparison = new LinkedHashMap<>();
            for (int i : Data_handler.getFullQuantDsMap().keySet()) {
                if (CSFPR_Central_Manager.getQuantSearchSelection().getQuantDatasetIndexes().contains(i)) {
                    tempFullComparison.put(i, Data_handler.getFullQuantDsMap().get(i));
                }
            }

            diseaseComparisonHeatmapComponent.updateData(Data_handler.getRowLabels(), Data_handler.getColumnLabels(), Data_handler.getDiseaseGroupComparisonsSet(), tempFullComparison);

            if (type.equalsIgnoreCase("quant_compare")) {
                diseaseComparisonSelectionbubblechartComponent.setUserCustomizedComparison(CSFPR_Central_Manager.getQuantSearchSelection().getUserCustomizedComparison());
                lineChartProteinTableComponent.setUserCustomizedComparison(CSFPR_Central_Manager.getQuantSearchSelection().getUserCustomizedComparison());

            }
            diseaseComparisonHeatmapComponent.selectAll();
            updateCurrentLayout("proteintable");

        } else if (type.equalsIgnoreCase("reset_quant_searching")) {
            //update initial layout
            diseaseComparisonHeatmapComponent.unselectAll();
            heatmapBtn.updateIcon(logoRes);
            heatmapBtn.setEnabled(false);
            heatmapBtn.setReadOnly(true);
            diseaseComparisonSelectionbubblechartComponent.setUserCustomizedComparison(null);
            lineChartProteinTableComponent.setUserCustomizedComparison(null);
            initialDiseaseCategoriesComponent.updateData(Data_handler.getDiseaseCategorySet());
            updateCurrentLayout("initiallayout");
        }
        if (type.equalsIgnoreCase("comparisons_selection")) {

            Set<QuantDiseaseGroupsComparison> compList = CSFPR_Central_Manager.getSelectedComparisonsList();
            if (compList == null || compList.isEmpty()) {
                updateCurrentLayout("heatmap");
            }
        }

    }

    @Override
    public String getListenerId() {
        return this.getClass().getName();
    }

    /**
     * Constructor to initialize the main attributes (Data handler, and
     * selection manage )
     *
     * @param Data_handler
     * @param CSFPR_Central_Manager
     * @param width
     * @param height
     */
    public QuantDataLayoutContainer(final Data_Handler Data_handler, CSFPR_Central_Manager CSFPR_Central_Manager, int width, int height) {
        super(width, height);
        this.Data_handler = Data_handler;
        this.CSFPR_Central_Manager = CSFPR_Central_Manager;
        this.setMargin(false);
        this.addStyleName("slowslide");
        Collection<DiseaseCategoryObject> availableDiseaseCategory = Data_handler.getDiseaseCategorySet();
        final HorizontalLayout subBodyWrapper = new HorizontalLayout();
        subBodyWrapper.setWidthUndefined();
        subBodyWrapper.setHeightUndefined();

        initialDiseaseCategoriesComponent = new InitialDiseaseCategoriesComponent(availableDiseaseCategory, width - 20, height) {
            private String lastSelectedDisease = "";

            @Override
            public void resetSelection() {
                lastSelectedDisease = "";
            }

            @Override
            public void onSelectDiseaseCategory(String diseaseCategoryName) {
                defaultView();
                if (CSFPR_Central_Manager.getQuantSearchSelection() != null) {
                    if (lastSelectedDisease.equalsIgnoreCase(diseaseCategoryName)) {
                        return;
                    }
                    lastSelectedDisease = diseaseCategoryName;
                    Set<QuantDiseaseGroupsComparison> compList = CSFPR_Central_Manager.getSelectedComparisonsList();
                    diseaseComparisonHeatmapComponent.unselectAll();

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
                        diseaseComparisonHeatmapComponent.updateData(rowLabels, colLabels, updatedSet, Data_handler.getFullQuantDsMap());
                    } else {
                        diseaseComparisonHeatmapComponent.updateData(Data_handler.getRowLabels(), Data_handler.getColumnLabels(), Data_handler.getDiseaseGroupComparisonsSet(), Data_handler.getFullQuantDsMap());
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
        InformationButton info = new InformationButton("Info");

        info.setWidth(40, Unit.PIXELS);
        info.setHeight(40, Unit.PIXELS);

        this.addButton(initialDiseaseCategoriesComponent.getThumbImgLayout(), initialDiseaseCategoriesComponent, null, true);

        heatmapBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
            }
        };
        heatmapBtn.updateIcon(logoRes);

        heatmapBtn.setWidth(100, Unit.PIXELS);
        heatmapBtn.setHeight(100, Unit.PIXELS);

        mainViewPanelHeight = height;

        mainViewPanelWidth = width - 178;

        heatmapViewContainer = new VerticalLayout();
        heatmapViewContainer.setWidth(mainViewPanelWidth, Unit.PIXELS);
        heatmapViewContainer.setHeight(mainViewPanelHeight, Unit.PIXELS);

        heatmapToolsContainer = new VerticalLayout();
        heatmapBtn.setHasWrapper(true);
        heatmapBtn.setDescription("Disease Comparisons");
        this.addButton(heatmapBtn, heatmapViewContainer, heatmapToolsContainer, false);

        Data_handler.loadDiseaseCategory("All Diseases");

        diseaseComparisonHeatmapComponent = new DiseaseComparisonHeatmapComponent(CSFPR_Central_Manager, Data_handler, Data_handler.getDiseaseCategorySet(), mainViewPanelWidth, mainViewPanelHeight - 2, Data_handler.getActiveDataColumns()) {

            @Override
            public void updateIcon(String imageUrl) {
                heatmapBtn.updateIcon(new ExternalResource(imageUrl));
                heatmapBtn.setEnabled(true);
            }

            @Override
            public void updateCombinedGroups(Map<String, Map<String, String>> updatedGroupsNamesMap) {
                Data_handler.updateCombinedGroups(updatedGroupsNamesMap);
                diseaseComparisonHeatmapComponent.updateData(Data_handler.getRowLabels(), Data_handler.getColumnLabels(), Data_handler.getDiseaseGroupComparisonsSet(), Data_handler.getFullQuantDsMap());
            }

            @Override
            public void updateCSFSerumDatasets(boolean serumApplied, boolean csfApplied) {
                Data_handler.updateCSFSerumDatasets(serumApplied, csfApplied);
                diseaseComparisonHeatmapComponent.updateData(Data_handler.getRowLabels(), Data_handler.getColumnLabels(), Data_handler.getDiseaseGroupComparisonsSet(), Data_handler.getFullQuantDsMap());
            }

            @Override
            public void blinkIcon() {
                heatmapBtn.blink();
            }

        };

        heatmapViewContainer.addComponent(diseaseComparisonHeatmapComponent);
        heatmapViewContainer.setComponentAlignment(diseaseComparisonHeatmapComponent, Alignment.TOP_LEFT);

        heatmapToolsContainer.addComponent(diseaseComparisonHeatmapComponent.getHeatmapToolsContainer());
        heatmapToolsContainer.setComponentAlignment(diseaseComparisonHeatmapComponent.getHeatmapToolsContainer(), Alignment.TOP_RIGHT);

        bubblechartBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {

            }
        };
        bubblechartBtn.updateIcon(logoRes);
        bubblechartBtn.setDescription("Protein Overview");

        bubblechartBtn.setWidth(100, Unit.PIXELS);
        bubblechartBtn.setHeight(100, Unit.PIXELS);

        bubblechartBtn.setEnabled(false);

        bubblechartViewContainer = new VerticalLayout();
        bubblechartViewContainer.setWidth(mainViewPanelWidth, Unit.PIXELS);
        bubblechartViewContainer.setHeight(mainViewPanelHeight, Unit.PIXELS);

        bubblechartToolsContainer = new VerticalLayout();
        bubblechartBtn.setHasWrapper(true);
        this.addButton(bubblechartBtn, bubblechartViewContainer, bubblechartToolsContainer, false);

        linechartBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {

            }
        };
        linechartBtn.updateIcon(logoRes);
        linechartBtn.setDescription("Protein Table");

        linechartBtn.setWidth(100, Unit.PIXELS);
        linechartBtn.setHeight(100, Unit.PIXELS);

        linechartViewContainer = new VerticalLayout();
        linechartViewContainer.setWidth(mainViewPanelWidth, Unit.PIXELS);
        linechartViewContainer.setHeight(mainViewPanelHeight, Unit.PIXELS);
        linechartToolsContainer = new VerticalLayout();

        this.addButton(linechartBtn, linechartViewContainer, linechartToolsContainer, false);

        peptideInfoBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {

            }
        };
        peptideInfoBtn.setDescription("Protein Details");
        peptideInfoBtn.updateIcon(logoRes);

        peptideInfoBtn.setWidth(100, Unit.PIXELS);
        peptideInfoBtn.setHeight(100, Unit.PIXELS);

        peptidesViewContainer = new VerticalLayout();
        peptidesViewContainer.setWidth(mainViewPanelWidth, Unit.PIXELS);
        peptidesViewContainer.setHeight(mainViewPanelHeight, Unit.PIXELS);
        peptidesToolsContainer = new VerticalLayout();
        linechartBtn.setHasWrapper(true);
        peptideInfoBtn.setHasWrapper(true);
        this.addButton(peptideInfoBtn, peptidesViewContainer, peptidesToolsContainer, false);

        ///init bubble chart container
        diseaseComparisonSelectionbubblechartComponent = new DiseaseComparisonSelectionBubblechartComponent(CSFPR_Central_Manager, mainViewPanelWidth, mainViewPanelHeight - 2) {

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

            }

        };
        bubblechartViewContainer.addComponent(diseaseComparisonSelectionbubblechartComponent);
        bubblechartToolsContainer.addComponent(diseaseComparisonSelectionbubblechartComponent.getBubblechartToolsContainer());
        bubblechartToolsContainer.setComponentAlignment(diseaseComparisonSelectionbubblechartComponent.getBubblechartToolsContainer(), Alignment.TOP_RIGHT);

        lineChartProteinTableComponent = new LineChartProteinTableComponent(CSFPR_Central_Manager, mainViewPanelWidth, mainViewPanelHeight - 2, null) {

            @Override
            public void updateThumbIconRowNumber(int rowNumber, String url) {
                linechartBtn.setEnabled(true);
                linechartBtn.updateText(rowNumber + "");
                linechartBtn.updateIcon(new ExternalResource(url));

            }

        };
        linechartViewContainer.addComponent(lineChartProteinTableComponent);
        linechartToolsContainer.addComponent(lineChartProteinTableComponent.getProteinTableToolsContainer());
        linechartToolsContainer.setComponentAlignment(lineChartProteinTableComponent.getProteinTableToolsContainer(), Alignment.TOP_RIGHT);

        PeptideViewComponent peptideViewComponent = new PeptideViewComponent(CSFPR_Central_Manager, mainViewPanelWidth, mainViewPanelHeight - 2) {

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
        peptidesToolsContainer.addComponent(peptideViewComponent.getPeptideTableToolsContainer());
        peptidesToolsContainer.setComponentAlignment(peptideViewComponent.getPeptideTableToolsContainer(), Alignment.TOP_RIGHT);
        this.CSFPR_Central_Manager.registerListener(QuantDataLayoutContainer.this);
    }

    /**
     * set the main disease category in the data handler to update the current
     * active data in the logic layer
     *
     * @param DiseaseCategoryName
     */
    private void loadDiseaseCategory(String DiseaseCategoryName) {
        Data_handler.loadDiseaseCategory(DiseaseCategoryName);
        diseaseComparisonHeatmapComponent.updateData(Data_handler.getRowLabels(), Data_handler.getColumnLabels(), Data_handler.getDiseaseGroupComparisonsSet(), Data_handler.getFullQuantDsMap());

    }

}
