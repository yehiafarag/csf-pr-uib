package probe.com.view.body;

import com.ejt.vaadin.sizereporter.ComponentResizeEvent;
import com.ejt.vaadin.sizereporter.ComponentResizeListener;
import com.ejt.vaadin.sizereporter.SizeReporter;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import probe.com.selectionmanager.DatasetExploringCentralSelectionManager;
import probe.com.handlers.CSFPRHandler;
import probe.com.model.beans.quant.QuantProtein;
import probe.com.view.body.quantdatasetsoverview.DiseaseGroupsFiltersContainer;
import probe.com.view.body.quantdatasetsoverview.QuantProteinsComparisonsContainer;
import probe.com.view.body.quantdatasetsoverview.QuantDatasetsfullStudiesTableLayout;
import probe.com.view.core.HideOnClickLayout;
import probe.com.view.body.quantdatasetsoverview.QuantProteinsTabsheetContainerLayout;

/**
 * This is the studies layout include publication heatmapFiltere and publication
 * table
 *
 * @author Yehia Farag
 */
public class QuantDatasetsOverviewLayout extends VerticalLayout {

//    private final StudiesPieChartFiltersContainerLayout pieChartFiltersLayout;
//    private final QuantFilterUtility filterUtility;
    private final DatasetExploringCentralSelectionManager exploringFiltersManager;
    private final QuantProteinsTabsheetContainerLayout proteinsLayout;
    private int resizedCounter = 0;

    /**
     *
     * @param handler
     * @param searchingMode
     * @param searchQuantificationProtList
     */
    public QuantDatasetsOverviewLayout(CSFPRHandler handler, boolean searchingMode, List<QuantProtein> searchQuantificationProtList) {

      
        exploringFiltersManager = new DatasetExploringCentralSelectionManager(handler.getQuantDatasetInitialInformationObject(), handler.getActivePieChartQuantFilters());//,filterUtility.getFullFilterList()

        this.setMargin(true);
        this.setSpacing(true);
        this.setWidth("100%");
        this.setHeightUndefined();

        if (exploringFiltersManager.getFullQuantDatasetMap() == null || exploringFiltersManager.getFullQuantDatasetMap().isEmpty()) {
            Label noExpLable = new Label("<h4 style='font-family:verdana;color:black;font-weight:bold;'>Sorry No Dataset Availabe Now !</h4>");
            noExpLable.setContentMode(ContentMode.HTML);
            this.addComponent(noExpLable);
//            pieChartFiltersLayout = null;
            proteinsLayout = null;
            return;

        }
        // init piecharts filter
        exploringFiltersManager.changeDiseaseCategory("Multiple Sclerosis");
        
        

        DiseaseGroupsFiltersContainer heatmapFilter = new DiseaseGroupsFiltersContainer(exploringFiltersManager, handler, searchQuantificationProtList);
        heatmapFilter.setWidth("100%");
        heatmapFilter.setMargin(new MarginInfo(false, false, true, false));
        String infoText = "Select a disease category (Multiple Sclerosis, Alzheimer, etc)<img href='img/disease_category.png' alt='disease category'> in the roll down menu on top to view all available  patients group comparisons on the interactive heat-map <img href='img/hideshow.png' alt='heat-map'> that belong to the selected disease . Select single or multiple comparisons from the heatmap to show the overall proteins information on the bubble chart and proteins information table.";

        HideOnClickLayout comparisonLevelLayout = new HideOnClickLayout("Datasets", heatmapFilter, null, infoText);
        this.addComponent(comparisonLevelLayout);
        comparisonLevelLayout.setVisability(true);

        QuantProteinsComparisonsContainer quantProteinsComparisonsContainer = new QuantProteinsComparisonsContainer(exploringFiltersManager, handler, null);

        HideOnClickLayout comparisonsTableContainer = new HideOnClickLayout("Proteins", quantProteinsComparisonsContainer, null, Alignment.TOP_LEFT, infoText);

        int pageWidth = Page.getCurrent().getWebBrowser().getScreenWidth();
        int layoutWidth = (pageWidth - 70);
        quantProteinsComparisonsContainer.setLayoutWidth(layoutWidth);
        this.addComponent(comparisonsTableContainer);
        comparisonsTableContainer.setVisability(true);

        proteinsLayout = new QuantProteinsTabsheetContainerLayout(exploringFiltersManager, searchingMode, handler);
        HideOnClickLayout proteinsLevelLayout = new HideOnClickLayout("Proteins Information", proteinsLayout, null, infoText) {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                super.layoutClick(event);
            }

        };
        this.addComponent(proteinsLevelLayout);

        SizeReporter sizeReporter = new SizeReporter(proteinsLevelLayout);
        sizeReporter.addResizeListener(new ComponentResizeListener() {
            @Override
            public void sizeChanged(ComponentResizeEvent event) {

                if (resizedCounter == 3) {
                    UI.getCurrent().setScrollTop(1000);
                }

                resizedCounter++;
            }
        });
        proteinsLevelLayout.setVisability(false);

        QuantDatasetsfullStudiesTableLayout quantStudiesTable = new QuantDatasetsfullStudiesTableLayout(exploringFiltersManager);
        if (searchingMode) {
        } else {
            this.addComponent(quantStudiesTable);
            quantStudiesTable.setWidth(layoutWidth+"px");
        }

    }

}
