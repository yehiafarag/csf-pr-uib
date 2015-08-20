package probe.com.view.body;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.VerticalLayout;
import probe.com.selectionmanager.DatasetExploringCentralSelectionManager;
import probe.com.handlers.MainHandler;
import probe.com.selectionmanager.FilterUtility;
import probe.com.view.body.quantdatasetsoverview.DiseaseGroupsFiltersContainer;
import probe.com.view.body.quantdatasetsoverview.QuantProteinsComparisonsContainer;
import probe.com.view.body.quantdatasetsoverview.QuantDatasetsTableLayout;
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
//     private final DatasetsExplorerTreeLayout studiesExplorerTreeLayout;
    private final FilterUtility filterUtility;
    private final DatasetExploringCentralSelectionManager exploringFiltersManager;
    private final QuantProteinsTabsheetContainerLayout proteinsLayout;
    private final QuantProteinsComparisonsContainer compTableLayout;

    public QuantDatasetsOverviewLayout(MainHandler handler, boolean searchingMode) {

        if (searchingMode) {

        }
        filterUtility = new FilterUtility(handler);
        exploringFiltersManager = new DatasetExploringCentralSelectionManager(filterUtility.getQuantDatasetArr(), filterUtility.getActiveFilters());//,filterUtility.getFullFilterList()

        this.setMargin(true);
        this.setSpacing(true);
        this.setWidth("100%");
        this.setHeightUndefined();
        // init piecharts filter
        pieChartFiltersLayout = new StudiesPieChartFiltersContainerLayout(exploringFiltersManager);

        compTableLayout = new QuantProteinsComparisonsContainer(exploringFiltersManager, handler, null);
        DiseaseGroupsFiltersContainer heatmapFilter = new DiseaseGroupsFiltersContainer(exploringFiltersManager, pieChartFiltersLayout.getPieChartFiltersBtn(), compTableLayout);
        heatmapFilter.setWidth("100%");
        heatmapFilter.setMargin(new MarginInfo(false, false, true, false));

        HideOnClickLayout comparisonLevelLayout = new HideOnClickLayout("Datasets", heatmapFilter, null);
        this.addComponent(comparisonLevelLayout);
        comparisonLevelLayout.setVisability(true);

        proteinsLayout = new QuantProteinsTabsheetContainerLayout(exploringFiltersManager,searchingMode);
        HideOnClickLayout proteinsLevelLayout = new HideOnClickLayout("Proteins", proteinsLayout, null) {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                super.layoutClick(event);
                proteinsLayout.redrawCharts();
            }

        };
        this.addComponent(proteinsLevelLayout);
        proteinsLevelLayout.setVisability(true);

        QuantDatasetsTableLayout quantStudiesTable = new QuantDatasetsTableLayout(exploringFiltersManager, filterUtility.getActiveHeaders());
        if (searchingMode) {
        } else {
            this.addComponent(quantStudiesTable);
        }

    }

}
