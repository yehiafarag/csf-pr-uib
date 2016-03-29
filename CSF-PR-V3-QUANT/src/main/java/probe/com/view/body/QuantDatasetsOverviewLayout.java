package probe.com.view.body;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.List;
import probe.com.handlers.CSFPRHandler;
import probe.com.model.beans.quant.QuantProtein;
import probe.com.selectionmanager.QuantCentralManager;
import probe.com.view.body.quantdatasetsoverview.DiseaseGroupsFiltersContainer;
import probe.com.view.body.quantdatasetsoverview.QuantProteinsComparisonsContainer;
import probe.com.view.core.HideOnClickLayout;
import probe.com.view.body.quantdatasetsoverview.QuantProteinsTabsheetContainerLayout;

/**
 * This is the studies layout include publication heatmapFiltere and publication
 * table
 *
 * @author Yehia Farag
 */
public class QuantDatasetsOverviewLayout extends VerticalLayout {

//    private final StudiesSelectionManager Studies_Selection_Manager;
    private final QuantCentralManager Quant_Central_Manager;
    private final QuantProteinsTabsheetContainerLayout proteinsLayout;
    private final VerticalLayout topLabelMarker;

    public VerticalLayout getTopLabelMarker() {
        return topLabelMarker;
    }

    /**
     *
     * @param CSFPR_Handler
     * @param searchingMode
     * @param searchQuantificationProtList
     */
    public QuantDatasetsOverviewLayout(CSFPRHandler CSFPR_Handler, boolean searchingMode, List<QuantProtein> searchQuantificationProtList) {

        topLabelMarker = new VerticalLayout();
        this.addComponent(topLabelMarker);
        this.setExpandRatio(topLabelMarker, 0.01f);
        topLabelMarker.setHeight("10px");
        topLabelMarker.setWidth("100%");
        topLabelMarker.setStyleName(Reindeer.LAYOUT_WHITE);  
        


        if (CSFPR_Handler.getQuantDatasetInitialInformationObject().isEmpty()) {
            Label noExpLable = new Label("<h4 style='font-family:verdana;color:black;font-weight:bold;'>Sorry No Dataset Availabe Now !</h4>");
            noExpLable.setContentMode(ContentMode.HTML);
            this.addComponent(noExpLable);
            proteinsLayout = null;
            Quant_Central_Manager = null;
            return;
        }

        Quant_Central_Manager = new QuantCentralManager(CSFPR_Handler);//,filterUtility.getFullFilterList()

        this.setMargin(true);
        this.setSpacing(true);
        this.setWidth("100%");
        this.setHeightUndefined();

        // init piecharts filter
        DiseaseGroupsFiltersContainer heatmapFilter = new DiseaseGroupsFiltersContainer(Quant_Central_Manager, CSFPR_Handler, searchQuantificationProtList, null);
        heatmapFilter.setWidth("100%");
        heatmapFilter.setMargin(new MarginInfo(false, false, true, false));
        String infoText = "Select a disease category (Multiple Sclerosis, Alzheimer, etc)<img src='VAADIN/themes/dario-theme/img/1.png'  alt='disease category' Align='center'> in the roll down menu on top to view all available  patients group comparisons on the interactive heat-map <img src='VAADIN/themes/dario-theme/img/2.png' alt='heat-map'  Align='center'> that belong to the selected disease . Select single or multiple comparisons from the heatmap to show the overall proteins information on the bubble chart and proteins information table.</br>Users can use more filters by clicking on the diffrent available filters <img src='VAADIN/themes/dario-theme/img/4.png' alt='filter'  Align='center'> ";

        HideOnClickLayout comparisonLevelLayout = new HideOnClickLayout("Datasets", heatmapFilter, null, infoText, CSFPR_Handler.getTipsGenerator().generateTipsBtn());

        this.addComponent(comparisonLevelLayout);
        comparisonLevelLayout.setVisability(true);

        QuantProteinsComparisonsContainer quantProteinsComparisonsContainer = new QuantProteinsComparisonsContainer(Quant_Central_Manager, CSFPR_Handler, null);

        HideOnClickLayout comparisonsTableContainer = new HideOnClickLayout("Proteins", quantProteinsComparisonsContainer, null, Alignment.TOP_LEFT, infoText, null);

        int pageWidth = Page.getCurrent().getBrowserWindowWidth();
        int layoutWidth = (pageWidth - 70);
        quantProteinsComparisonsContainer.setLayoutWidth(layoutWidth);
        this.addComponent(comparisonsTableContainer);
        comparisonsTableContainer.setVisability(true);

        proteinsLayout = new QuantProteinsTabsheetContainerLayout(Quant_Central_Manager, searchingMode, CSFPR_Handler);
        HideOnClickLayout proteinsLevelLayout = new HideOnClickLayout("Proteins Information", proteinsLayout, null, Alignment.TOP_LEFT, infoText, null) {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                super.layoutClick(event);
            }
        };
        proteinsLevelLayout.setVisability(true);
        this.addComponent(proteinsLevelLayout);

    }

}
