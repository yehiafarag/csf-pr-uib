package probe.com.view.body.quantdatasetsoverview;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.DiiseaseGroupsComparisonsProtein;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
import probe.com.selectionmanager.CSFFilter;
import probe.com.selectionmanager.DatasetExploringCentralSelectionManager;
import probe.com.view.core.CustomExternalLink;
import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.ProteinOverviewJFreeLineChartContainer;

/**
 *
 * @author Yehia Farag
 *
 * this class represents the proteins tab sheet in the quant data overview (the
 * lower layout)
 */
public class QuantProteinsTabsheetContainerLayout extends VerticalLayout implements CSFFilter, TabSheet.SelectedTabChangeListener {

    private final boolean searchingMode;
    private final LinkedHashMap<String, DiiseaseGroupsComparisonsProtein[]> protSelectionMap = new LinkedHashMap<String, DiiseaseGroupsComparisonsProtein[]>();
    private boolean selfSelection = false;
    private final Map<Component, String> protSelectionTabMap = new HashMap<Component, String>();
    private boolean initScroll = true;
    private final DatasetExploringCentralSelectionManager datasetExploringCentralSelectionManager;
    private final TabSheet proteinsTabsheet;
    private Label compOrederTitle;

    /**
     * central selection manager event
     *
     * @param type
     */
    @Override
    public void selectionChanged(String type) {
        if (type.equalsIgnoreCase("Quant_Proten_Selection") && !selfSelection) {

            proteinsTabsheet.removeAllComponents();
            protSelectionMap.clear();
            protSelectionTabMap.clear();
            protSelectionMap.putAll(datasetExploringCentralSelectionManager.getQuantProteinsLayoutSelectionMap());
            if (protSelectionMap.isEmpty()) {
                proteinsTabsheet.setVisible(false);
                return;
            }
            Set<QuantDiseaseGroupsComparison> selectedComparisonList = datasetExploringCentralSelectionManager.getSelectedDiseaseGroupsComparisonList();
            proteinsTabsheet.setVisible(true);
            Page.getCurrent().getStyles().add(".v-tabsheet-tabs .v-icon{"
                    + "    width:" + selectedComparisonList.size() * 15 + "px !important;"
                    + "    height:35px !important;"
                    + "    color:blue;"
                    + "}");

            for (final String key : protSelectionMap.keySet()) {
                String protAcc = key.replace("--", "").trim().split(",")[0].toUpperCase();
                String protName = key.replace("--", "").trim().split(",")[1];
                HorizontalLayout vlo = generateProtTab(protName, protAcc, protSelectionMap.get(key), selectedComparisonList);
                final Tab t1;
                t1 = proteinsTabsheet.addTab(vlo);
                t1.setCaption(protName);
                t1.setClosable(true);
                t1.setStyleName("tabwithcharticon");
                t1.setIcon(new ExternalResource(((ProteinOverviewJFreeLineChartContainer) vlo.getComponent(1)).getThumbChart()));
                protSelectionTabMap.put(vlo, key);//                
                redrawCharts();

            }
            if (initScroll) {
                initScroll = false;
                if (this.getUI() != null) {
                    this.getUI().scrollIntoView(compOrederTitle);
                }
            }

        } else {
            selfSelection = false;
        }

    }

    /**
     * get filter name
     *
     * @return filter id
     */
    @Override
    public String getFilterId() {
        return "quantProteinsLayout";
    }

    /**
     * remove filter values from the central selection manager (not apply to
     * this class)
     *
     * @param value
     */
    @Override
    public void removeFilterValue(String value) {
    }

    /**
     *
     * @param datasetExploringCentralSelectionManager
     * @param searchingMode
     */
    public QuantProteinsTabsheetContainerLayout(final DatasetExploringCentralSelectionManager datasetExploringCentralSelectionManager, boolean searchingMode) {
        this.searchingMode = searchingMode;
        this.datasetExploringCentralSelectionManager = datasetExploringCentralSelectionManager;
        this.datasetExploringCentralSelectionManager.registerFilter(QuantProteinsTabsheetContainerLayout.this);
        this.setHeight("100%");
        this.setMargin(new MarginInfo(true, false, false, false));
        proteinsTabsheet = new TabSheet();
        proteinsTabsheet.setCaptionAsHtml(true);
        proteinsTabsheet.setHeightUndefined();
        proteinsTabsheet.setStyleName(Reindeer.TABSHEET_MINIMAL);
        proteinsTabsheet.setVisible(false);
        proteinsTabsheet.addStyleName("hideoverflow");
        proteinsTabsheet.addSelectedTabChangeListener(QuantProteinsTabsheetContainerLayout.this);
        proteinsTabsheet.setCloseHandler(new TabSheet.CloseHandler() {
            @Override
            public void onTabClose(TabSheet tabsheet, Component tabContent) {
                protSelectionMap.remove(protSelectionTabMap.get(tabContent));
                if (protSelectionMap.isEmpty()) {
                    proteinsTabsheet.setVisible(false);
                }

                datasetExploringCentralSelectionManager.setQuantProteinsSelectionLayout(new LinkedHashMap<String, DiiseaseGroupsComparisonsProtein[]>(protSelectionMap));
            }
        });
        this.addComponent(proteinsTabsheet);
    }

    /**
     * initialize and generate quant proteins tab
     *
     * @param quantProteinName protein name
     * @param quantProteinAccession
     * @param diseaseGroupsComparisonsProteinArray
     * @param selectedDiseaseGroupsComparisonsList disease groups comparisons
     * list in case of searching mode
     */
    private HorizontalLayout generateProtTab(String quantProteinName, String quantProteinAccession, DiiseaseGroupsComparisonsProtein[] diseaseGroupsComparisonsProteinArray, Set<QuantDiseaseGroupsComparison> selectedDiseaseGroupsComparisonsList) {
        HorizontalLayout bodyLayout = new HorizontalLayout();
        Page page = Page.getCurrent();
        int pageWidth = page.getBrowserWindowWidth();
        bodyLayout.setWidthUndefined();
        bodyLayout.setSpacing(true);
        bodyLayout.setMargin(true);
        bodyLayout.setHeightUndefined();
        bodyLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        GridLayout protInfoLayout = new GridLayout(1, 6);
        protInfoLayout.setMargin(true);
        protInfoLayout.setSpacing(true);

        Label accTitle = new Label("<b>Protein Accession</b>");
        accTitle.setContentMode(ContentMode.HTML);
        protInfoLayout.addComponent(accTitle, 0, 0);
        CustomExternalLink acc = new CustomExternalLink(quantProteinAccession.toUpperCase(), "http://www.uniprot.org/uniprot/" + quantProteinAccession.toUpperCase());
        protInfoLayout.addComponent(acc, 0, 1);
        Label nameTitle = new Label("<b>Protein Name</b>");
        nameTitle.setContentMode(ContentMode.HTML);
        protInfoLayout.addComponent(nameTitle, 0, 2);

        Label nameValue = new Label("<textarea rows='4' cols='20' readonly>" + quantProteinName + "</textarea>");
        nameValue.setContentMode(ContentMode.HTML);
        nameValue.setStyleName("valuelabel");
        nameValue.setReadOnly(true);
        protInfoLayout.addComponent(nameValue, 0, 3);
        protInfoLayout.setWidth("150px");
        bodyLayout.addComponent(protInfoLayout);

        compOrederTitle = new Label("<b>Comparisons Order</b>");
        compOrederTitle.setContentMode(ContentMode.HTML);
        protInfoLayout.addComponent(compOrederTitle, 0, 4);
        protInfoLayout.setWidth("250px");
        int width = (pageWidth - 300);

        ProteinOverviewJFreeLineChartContainer overallPlotLayout = new ProteinOverviewJFreeLineChartContainer(datasetExploringCentralSelectionManager, diseaseGroupsComparisonsProteinArray, selectedDiseaseGroupsComparisonsList, (width), quantProteinName, searchingMode);
        protInfoLayout.addComponent(overallPlotLayout.getOrederingOptionGroup(), 0, 5);

        bodyLayout.addComponent(overallPlotLayout);
        bodyLayout.setComponentAlignment(overallPlotLayout, Alignment.TOP_CENTER);

        return bodyLayout;

    }

    /**
     * redraw quant bar charts
     */
    public void redrawCharts() {
        if (proteinsTabsheet.getSelectedTab() == null) {
            return;
        }
        HorizontalLayout selectedTab = (HorizontalLayout) proteinsTabsheet.getSelectedTab();
        ProteinOverviewJFreeLineChartContainer overallPlotLayout = (ProteinOverviewJFreeLineChartContainer) selectedTab.getComponent(1);
        overallPlotLayout.redrawCharts();
    }

    @Override
    public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
        redrawCharts();
        
    }

}
