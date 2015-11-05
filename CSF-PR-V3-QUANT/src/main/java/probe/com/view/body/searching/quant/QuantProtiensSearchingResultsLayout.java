/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.searching.quant;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import java.util.Map;
import probe.com.selectionmanager.DatasetExploringCentralSelectionManager;
import probe.com.handlers.CSFPRHandler;
import probe.com.model.beans.quant.QuantDatasetInitialInformationObject;
import probe.com.model.beans.quant.QuantDatasetObject;
import probe.com.model.beans.quant.QuantProtein;
import probe.com.view.body.quantdatasetsoverview.DiseaseGroupsFiltersContainer;
import probe.com.view.body.quantdatasetsoverview.QuantProteinsComparisonsContainer;
import probe.com.view.core.HideOnClickLayout;
import probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.interactivepiechartfilters.StudiesPieChartFiltersContainerLayout;
import probe.com.view.body.quantdatasetsoverview.QuantProteinsTabsheetContainerLayout;

/**
 * This is the studies layout include publication heatmapFiltere and publication
 * table
 *
 * @author Yehia Farag
 */
public class QuantProtiensSearchingResultsLayout extends VerticalLayout {

    private final StudiesPieChartFiltersContainerLayout pieChartFiltersLayout;
//     private final DatasetsExplorerTreeLayout studiesExplorerTreeLayout;
//    private final FilterUtility filterUtility;
    private final DatasetExploringCentralSelectionManager exploringFiltersManager;
    private final QuantProteinsTabsheetContainerLayout proteinsLayout;
    private final QuantProteinsComparisonsContainer quantProteinsComparisonsContainer;

    /**
     *
     * @param handler
     * @param searchQuantificationProtList
     */
    public QuantProtiensSearchingResultsLayout(CSFPRHandler handler, List<QuantProtein> searchQuantificationProtList) {

//        filterUtility = new FilterUtility(handler);
        //get quant dataset arr
        Map<String, QuantDatasetInitialInformationObject> quantDatasetListObject = handler.getQuantDatasetInitialInformationObject(searchQuantificationProtList);
//        Map<Integer, QuantDatasetObject> quantDatasetArr = quantDatasetListObject.getQuantDatasetsList();

//        boolean[] activeHeaders = quantDatasetListObject.getActiveHeaders();
        //which fillters are exist
        Map<String, boolean[]> activeFiltersMap = handler.getActivePieChartQuantFilters(searchQuantificationProtList);
        //get active filters
        exploringFiltersManager = new DatasetExploringCentralSelectionManager(quantDatasetListObject, activeFiltersMap);//,filterUtility.getFullFilterList()
        this.setMargin(true);
        this.setSpacing(true);
        this.setWidth("100%");
        this.setHeightUndefined();
        // init piecharts filter
        pieChartFiltersLayout = new StudiesPieChartFiltersContainerLayout(exploringFiltersManager,handler);

        
        DiseaseGroupsFiltersContainer heatmapFilter = new DiseaseGroupsFiltersContainer(exploringFiltersManager,handler, searchQuantificationProtList);
        heatmapFilter.setWidth("100%");
        heatmapFilter.setMargin(new MarginInfo(false, false, true, false));

        HideOnClickLayout DatasetFilteringContainer = new HideOnClickLayout("Datasets", heatmapFilter, null);
        this.addComponent(DatasetFilteringContainer);
        DatasetFilteringContainer.setVisability(true);
        
        
        quantProteinsComparisonsContainer = new QuantProteinsComparisonsContainer(exploringFiltersManager, handler, searchQuantificationProtList);
         HideOnClickLayout comparisonsTableContainer = new HideOnClickLayout("Proteins", quantProteinsComparisonsContainer, null,Alignment.TOP_LEFT);
         int pageWidth = Page.getCurrent().getWebBrowser().getScreenWidth();
         int layoutWidth = (pageWidth - 70);
         quantProteinsComparisonsContainer.setLayoutWidth(layoutWidth);
        this.addComponent(comparisonsTableContainer);
        comparisonsTableContainer.setVisability(true);
        

        proteinsLayout = new QuantProteinsTabsheetContainerLayout(exploringFiltersManager, true,handler);
        HideOnClickLayout proteinsLevelLayout = new HideOnClickLayout("Proteins Information", proteinsLayout, null);
//        {
//            @Override
//            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
//                super.layoutClick(event); //To change body of generated methods, choose Tools | Templates.
////                proteinsLayout.redrawCharts();
//            }
//        };
        this.addComponent(proteinsLevelLayout);
        proteinsLevelLayout.setVisability(true);

        heatmapFilter.selectAllComparisons();
//
//        ExploreDatasetsTableLayout studiesTable = new ExploreDatasetsTableLayout(exploringFiltersManager, activeHeaders);
//        this.addComponent(studiesTable);
    }
}
