package probe.com.view.body.quantdatasetsoverview;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
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
import probe.com.handlers.CSFPRHandler;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.DiseaseGroupsComparisonsProteinLayout;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
import probe.com.selectionmanager.CSFFilter;
import probe.com.selectionmanager.DatasetExploringCentralSelectionManager;
import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.ProteinOverviewJFreeLineChartContainer;
import probe.com.view.core.HideOnClickLayout;

/**
 *
 * @author Yehia Farag
 *
 * this class represents the proteins tab sheet in the quant data overview (the
 * lower layout)
 */
public class QuantProteinsTabsheetContainerLayout extends VerticalLayout implements CSFFilter, TabSheet.SelectedTabChangeListener {

    private final boolean searchingMode;
    private final LinkedHashMap<String, DiseaseGroupsComparisonsProteinLayout[]> protSelectionMap = new LinkedHashMap<String, DiseaseGroupsComparisonsProteinLayout[]>();
    private boolean selfSelection = false;
    private final Map<Component, String> protSelectionTabMap = new HashMap<Component, String>();
    private final Map<String, Tab> protSelectionKeyTabMap = new HashMap<String, Tab>();
    private boolean initScroll = true;
    private final DatasetExploringCentralSelectionManager datasetExploringCentralSelectionManager;
    private TabSheet proteinsTabsheet;
    private Label compOrederTitle;
    private final Map<Integer, Tab> tabIndexMap = new HashMap<Integer, Tab>();
    private Label noProtLabel = new Label("<h4 style='font-family:verdana;color:red;font-weight:bold;'>\t \t Select proteins to show their information !</h4>");
    private final CSFPRHandler mainHandler;
    private final Map<Integer, ProteinOverviewJFreeLineChartContainer> tabLayoutIndexMap = new HashMap<Integer, ProteinOverviewJFreeLineChartContainer>();

    /**
     * central selection manager event
     *
     * @param type
     */
    @Override
    public void selectionChanged(String type) {
        if (type.equalsIgnoreCase("Quant_Proten_Selection") && !selfSelection) {

            if (proteinsTabsheet != null && proteinsTabsheet.getComponentCount() < 20) {
                proteinsTabsheet.removeAllComponents();
            } else {
                this.initTabsheet();
            }
            protSelectionMap.clear();
            protSelectionTabMap.clear();
            protSelectionKeyTabMap.clear();
            protSelectionMap.putAll(datasetExploringCentralSelectionManager.getQuantProteinsLayoutSelectionMap());
            if (protSelectionMap.isEmpty()) {
                noProtLabel.setVisible(true);
                proteinsTabsheet.setVisible(false);
                return;
            }
            noProtLabel.setVisible(false);
            Set<QuantDiseaseGroupsComparison> selectedComparisonList = datasetExploringCentralSelectionManager.getSelectedDiseaseGroupsComparisonList();
            proteinsTabsheet.setVisible(true);
            Page.getCurrent().getStyles().add(".v-tabsheet-tabs .v-icon{"
                    + "    width:" + selectedComparisonList.size() * 15 + "px !important;"
                    + "    height:35px !important;"
                    + "    color:blue;"
                    + "}");

            int tabCounter = 0;
            tabIndexMap.clear();
            tabLayoutIndexMap.clear();
            for (final String key : protSelectionMap.keySet()) {
                String protAcc = key.replace("--", "").trim().split(",")[0].toUpperCase();
                String protName = key.replace("--", "").trim().split(",")[1];
                HorizontalLayout vlo = generateProtTab(key, protName, protAcc, protSelectionMap.get(key), selectedComparisonList);
                final Tab t1;
                t1 = proteinsTabsheet.addTab(vlo);
                t1.setCaption(protName);
                t1.setClosable(true);
                t1.setStyleName("tabwithcharticon");
                ProteinOverviewJFreeLineChartContainer tabLayout = (ProteinOverviewJFreeLineChartContainer) vlo.getComponent(0);
                tabLayoutIndexMap.put(tabCounter, tabLayout);
                if (tabCounter < 30) {
                    t1.setIcon(new ExternalResource(tabLayout.getThumbChart()));
                }
                vlo.setData(tabCounter);
                protSelectionTabMap.put(vlo, key);//      
                tabIndexMap.put(tabCounter, t1);
                protSelectionKeyTabMap.put(key, t1);
                tabCounter++;

            }

            if (initScroll) {
                initScroll = false;
                if (this.getUI() != null && compOrederTitle != null) {
                    this.getUI().scrollIntoView(compOrederTitle);
                }
            }

        } else if (type.equalsIgnoreCase("Quant_Proten_Tab_Selection") && !selfSelection) {
            String selectedProteinKey = datasetExploringCentralSelectionManager.getSelectedProteinKey();
            Tab tab = protSelectionKeyTabMap.get(selectedProteinKey);
            if (tab != null) {
                Integer index = (Integer) ((HorizontalLayout) tab.getComponent()).getData();
                proteinsTabsheet.setSelectedTab(index);
                proteinsTabsheet.focus();
                
            } else {

            }

        } else {
//            redrawCharts();
            selfSelection = false;
        }

//        for(int x=0;x<proteinsTabsheet.getComponentCount();x++){
//        }
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
     * @param mainHandler 
     */
    public QuantProteinsTabsheetContainerLayout(final DatasetExploringCentralSelectionManager datasetExploringCentralSelectionManager, boolean searchingMode, CSFPRHandler mainHandler) {
        this.searchingMode = searchingMode;
        this.mainHandler = mainHandler;
        this.datasetExploringCentralSelectionManager = datasetExploringCentralSelectionManager;
        this.datasetExploringCentralSelectionManager.registerFilter(QuantProteinsTabsheetContainerLayout.this);
        this.setHeight("100%");
        this.setMargin(new MarginInfo(true, false, false, false));
        this.noProtLabel.setContentMode(ContentMode.HTML);
        noProtLabel.setWidth("400px");
        noProtLabel.setHeight("40px");
        this.addComponent(noProtLabel);
        this.setComponentAlignment(noProtLabel, Alignment.TOP_LEFT);

//        proteinsTabsheet = new TabSheet() {
//
//        };
//        proteinsTabsheet.setCaptionAsHtml(true);
//        proteinsTabsheet.setHeightUndefined();
//        proteinsTabsheet.setStyleName(Reindeer.TABSHEET_MINIMAL);
//        proteinsTabsheet.setVisible(false);
//        proteinsTabsheet.addStyleName("hideoverflow");
//        proteinsTabsheet.addSelectedTabChangeListener(QuantProteinsTabsheetContainerLayout.this);
//        proteinsTabsheet.setCloseHandler(new TabSheet.CloseHandler() {
//            @Override
//            public void onTabClose(TabSheet tabsheet, Component tabContent) {
//                protSelectionMap.remove(protSelectionTabMap.get(tabContent));
//                if (protSelectionMap.isEmpty()) {
//                    proteinsTabsheet.setVisible(false);
//                }
//
//                datasetExploringCentralSelectionManager.setQuantProteinsSelectionLayout(new LinkedHashMap<String, DiseaseGroupsComparisonsProteinLayout[]>(protSelectionMap));
//            }
//        });
//        this.addComponent(proteinsTabsheet);
//        this.initTabsheet();
        this.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            private int layoutCounter = 30;

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                if (event.getClickedComponent() == null || event.getClickedComponent().getPrimaryStyleName() == null) {
                    return;
                }
                int targetedLocation = Page.getCurrent().getBrowserWindowWidth() - event.getClientX();
                if (layoutCounter < protSelectionMap.size() && event.getClickedComponent().getPrimaryStyleName().trim().equalsIgnoreCase("v-tabsheet") && (targetedLocation > 55 && targetedLocation < 101) && (event.getRelativeY() > 44 && event.getRelativeY() < 60)) {
                    tabIndexMap.get(layoutCounter).setIcon(new ExternalResource(tabLayoutIndexMap.get(layoutCounter++).getThumbChart()));

                }
            }
        });
    }

    private void initTabsheet() {
        if (proteinsTabsheet != null) {
            this.removeComponent(proteinsTabsheet);
//            proteinsTabsheet.detach();
        }

        proteinsTabsheet = new TabSheet() {

        };
        proteinsTabsheet.setCaptionAsHtml(true);
        proteinsTabsheet.setHeightUndefined();
        proteinsTabsheet.setStyleName(Reindeer.TABSHEET_MINIMAL);
        proteinsTabsheet.setVisible(false);
        proteinsTabsheet.addStyleName("hideoverflow");
        proteinsTabsheet.setImmediate(true);
        proteinsTabsheet.addSelectedTabChangeListener(QuantProteinsTabsheetContainerLayout.this);
        proteinsTabsheet.setCloseHandler(new TabSheet.CloseHandler() {
            @Override
            public void onTabClose(TabSheet tabsheet, Component tabContent) {
                protSelectionMap.remove(protSelectionTabMap.get(tabContent));
                if (protSelectionMap.isEmpty()) {
                    proteinsTabsheet.setVisible(false);
                }

                datasetExploringCentralSelectionManager.setQuantProteinsSelectionLayout(new LinkedHashMap<String, DiseaseGroupsComparisonsProteinLayout[]>(protSelectionMap));
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
    private HorizontalLayout generateProtTab(String proteinKey, String quantProteinName, String quantProteinAccession, DiseaseGroupsComparisonsProteinLayout[] diseaseGroupsComparisonsProteinArray, Set<QuantDiseaseGroupsComparison> selectedDiseaseGroupsComparisonsList) {
        HorizontalLayout bodyLayout = new HorizontalLayout();
        Page page = Page.getCurrent();
        int pageWidth = page.getBrowserWindowWidth();
        bodyLayout.setWidthUndefined();
        bodyLayout.setSpacing(true);
        bodyLayout.setMargin(true);
        bodyLayout.setHeightUndefined();
        bodyLayout.setStyleName(Reindeer.LAYOUT_WHITE);

        ProteinOverviewJFreeLineChartContainer overallPlotLayout = new ProteinOverviewJFreeLineChartContainer(datasetExploringCentralSelectionManager, diseaseGroupsComparisonsProteinArray, selectedDiseaseGroupsComparisonsList, (pageWidth), quantProteinName, quantProteinAccession, searchingMode, proteinKey, mainHandler);
        bodyLayout.addComponent(overallPlotLayout);
        bodyLayout.setComponentAlignment(overallPlotLayout, Alignment.TOP_CENTER);
        return bodyLayout;

    }

    /**
     * redraw quant bar charts
     */
    public void redrawCharts() {
        if (proteinsTabsheet == null || proteinsTabsheet.getSelectedTab() == null) {
            ((HideOnClickLayout) this.getParent()).setVisability(false);
            this.noProtLabel.setVisible(true);
            return;
        }
        this.noProtLabel.setVisible(false);
        ((HideOnClickLayout) this.getParent()).setVisability(true);
        HorizontalLayout selectedTab = (HorizontalLayout) proteinsTabsheet.getSelectedTab();
        ProteinOverviewJFreeLineChartContainer overallPlotLayout = (ProteinOverviewJFreeLineChartContainer) selectedTab.getComponent(0);
        overallPlotLayout.redrawCharts();
    }

    @Override
    public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
        redrawCharts();
        Integer index = (Integer) ((HorizontalLayout) event.getTabSheet().getSelectedTab()).getData();
        if (index != null) {
            {
                for (int x = index - 10; x < index + 10; x++) {
                    if (tabIndexMap.get(x) != null && tabIndexMap.get(x).getIcon() == null) {
                        tabIndexMap.get(x).setIcon(new ExternalResource(tabLayoutIndexMap.get(x).getThumbChart()));
                    }
                }

            }

            
        }

    }

}
