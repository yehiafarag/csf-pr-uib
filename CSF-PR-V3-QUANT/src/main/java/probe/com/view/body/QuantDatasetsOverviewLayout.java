package probe.com.view.body;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import probe.com.selectionmanager.DatasetExploringCentralSelectionManager;
import probe.com.handlers.CSFPRHandler;
import probe.com.model.beans.quant.QuantProtein;
import probe.com.view.body.quantdatasetsoverview.DiseaseGroupsFiltersContainer;
import probe.com.view.body.quantdatasetsoverview.QuantProteinsComparisonsContainer;
import probe.com.view.body.quantdatasetsoverview.QuantDatasetsCombinedTableLayout;
import probe.com.view.core.HideOnClickLayout;
import probe.com.view.body.quantdatasetsoverview.QuantProteinsTabsheetContainerLayout;

/**
 * This is the studies layout include publication heatmapFiltere and publication
 * table
 *
 * @author Yehia Farag
 */
public class QuantDatasetsOverviewLayout extends VerticalLayout{

//    private final StudiesPieChartFiltersContainerLayout pieChartFiltersLayout;
//    private final QuantFilterUtility filterUtility;
    private final DatasetExploringCentralSelectionManager exploringFiltersManager;
    private final QuantProteinsTabsheetContainerLayout proteinsLayout;


    /**
     *
     * @param handler
     * @param searchingMode
     * @param searchQuantificationProtList
     */
    public QuantDatasetsOverviewLayout(CSFPRHandler handler, boolean searchingMode,List<QuantProtein> searchQuantificationProtList) {

        if (searchingMode) {

        }
//        filterUtility = new QuantFilterUtility(handler);
        exploringFiltersManager = new DatasetExploringCentralSelectionManager(handler.getQuantDatasetInitialInformationObject(), handler.getActivePieChartQuantFilters());//,filterUtility.getFullFilterList()
       

        this.setMargin(true);
        this.setSpacing(true);
        this.setWidth("100%");
        this.setHeightUndefined();
        
        if(exploringFiltersManager.getFullQuantDatasetMap() == null || exploringFiltersManager.getFullQuantDatasetMap().isEmpty())
        {
            Label noExpLable = new Label("<h4 style='font-family:verdana;color:black;font-weight:bold;'>Sorry No Dataset Availabe Now !</h4>");
            noExpLable.setContentMode(ContentMode.HTML);
            this.addComponent(noExpLable);
//            pieChartFiltersLayout = null;
            proteinsLayout = null;
            return;
        
        }
        // init piecharts filter
      

      
        DiseaseGroupsFiltersContainer heatmapFilter = new DiseaseGroupsFiltersContainer(exploringFiltersManager,handler,searchQuantificationProtList);
        heatmapFilter.setWidth("100%");
        heatmapFilter.setMargin(new MarginInfo(false, false, true, false));

        HideOnClickLayout comparisonLevelLayout = new HideOnClickLayout("Datasets", heatmapFilter, null);
        this.addComponent(comparisonLevelLayout);
        comparisonLevelLayout.setVisability(true);
        
         QuantProteinsComparisonsContainer quantProteinsComparisonsContainer = new QuantProteinsComparisonsContainer(exploringFiltersManager, handler, null);
         HideOnClickLayout comparisonsTableContainer = new HideOnClickLayout("Proteins", quantProteinsComparisonsContainer, null,Alignment.TOP_LEFT);
        
         int pageWidth = Page.getCurrent().getWebBrowser().getScreenWidth();
         int layoutWidth = (pageWidth - 70);
         quantProteinsComparisonsContainer.setLayoutWidth(layoutWidth);
        this.addComponent(comparisonsTableContainer);
        comparisonsTableContainer.setVisability(true);
        
        

        proteinsLayout = new QuantProteinsTabsheetContainerLayout(exploringFiltersManager,searchingMode,handler);
        HideOnClickLayout proteinsLevelLayout = new HideOnClickLayout("Proteins Information", proteinsLayout, null) {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                super.layoutClick(event);
            }

        };
        this.addComponent(proteinsLevelLayout);
        proteinsLevelLayout.setVisability(false);

        QuantDatasetsCombinedTableLayout quantStudiesTable = new QuantDatasetsCombinedTableLayout(exploringFiltersManager);
        if (searchingMode) {
        } else {
            this.addComponent(quantStudiesTable);
        }

    }

}
