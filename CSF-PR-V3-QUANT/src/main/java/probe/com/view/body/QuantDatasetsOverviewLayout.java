package probe.com.view.body;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import probe.com.selectionmanager.DatasetExploringCentralSelectionManager;
import probe.com.handlers.CSFPRHandler;
import probe.com.selectionmanager.QuantFilterUtility;
import probe.com.view.body.quantdatasetsoverview.DiseaseGroupsFiltersContainer;
import probe.com.view.body.quantdatasetsoverview.QuantProteinsComparisonsContainer;
import probe.com.view.body.quantdatasetsoverview.QuantDatasetsCombinedTableLayout;
import probe.com.view.core.HideOnClickLayout;
import probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.interactivepiechartfilters.StudiesPieChartFiltersContainerLayout;
import probe.com.view.body.quantdatasetsoverview.QuantProteinsTabsheetContainerLayout;

/**
 * This is the studies layout include publication heatmapFiltere and publication
 * table
 *
 * @author Yehia Farag
 */
public class QuantDatasetsOverviewLayout extends VerticalLayout {

    private final StudiesPieChartFiltersContainerLayout pieChartFiltersLayout;
    private final QuantFilterUtility filterUtility;
    private final DatasetExploringCentralSelectionManager exploringFiltersManager;
    private final QuantProteinsTabsheetContainerLayout proteinsLayout;
    private final QuantProteinsComparisonsContainer compTableLayout;

    /**
     *
     * @param handler
     * @param searchingMode
     */
    public QuantDatasetsOverviewLayout(CSFPRHandler handler, boolean searchingMode) {

        if (searchingMode) {

        }
        filterUtility = new QuantFilterUtility(handler);
        exploringFiltersManager = new DatasetExploringCentralSelectionManager(filterUtility.getQuantDatasetArr(), filterUtility.getActiveFilters());//,filterUtility.getFullFilterList()
       

        this.setMargin(true);
        this.setSpacing(true);
        this.setWidth("100%");
        this.setHeightUndefined(); if(exploringFiltersManager.getFullQuantDatasetArr() == null || exploringFiltersManager.getFullQuantDatasetArr().isEmpty())
        {
            Label noExpLable = new Label("<h4 style='font-family:verdana;color:black;font-weight:bold;'>Sorry No Dataset Availabe Now !</h4>");
            noExpLable.setContentMode(ContentMode.HTML);
            this.addComponent(noExpLable);
            pieChartFiltersLayout = null;
            proteinsLayout = null;
            compTableLayout=null;
            return;
        
        }
        // init piecharts filter
        pieChartFiltersLayout = new StudiesPieChartFiltersContainerLayout(exploringFiltersManager);

        compTableLayout = new QuantProteinsComparisonsContainer(exploringFiltersManager, handler, null);
        DiseaseGroupsFiltersContainer heatmapFilter = new DiseaseGroupsFiltersContainer(exploringFiltersManager, pieChartFiltersLayout.getPieChartFiltersBtn(), compTableLayout);
        heatmapFilter.setWidth("100%");
        heatmapFilter.setMargin(new MarginInfo(false, false, true, false));

        HideOnClickLayout comparisonLevelLayout = new HideOnClickLayout("Datasets", heatmapFilter, null);
        this.addComponent(comparisonLevelLayout);
        comparisonLevelLayout.setVisability(true);

        proteinsLayout = new QuantProteinsTabsheetContainerLayout(exploringFiltersManager,searchingMode,handler);
        HideOnClickLayout proteinsLevelLayout = new HideOnClickLayout("Proteins Information", proteinsLayout, null) {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                super.layoutClick(event);
//                proteinsLayout.redrawCharts();
            }

        };
        this.addComponent(proteinsLevelLayout);
        proteinsLevelLayout.setVisability(false);

        QuantDatasetsCombinedTableLayout quantStudiesTable = new QuantDatasetsCombinedTableLayout(exploringFiltersManager, filterUtility.getActiveCombinedQuantTableHeaders());
        if (searchingMode) {
        } else {
            this.addComponent(quantStudiesTable);
        }

    }

}
