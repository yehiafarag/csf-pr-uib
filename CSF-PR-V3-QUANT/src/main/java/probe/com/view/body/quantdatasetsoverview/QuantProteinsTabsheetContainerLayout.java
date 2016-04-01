package probe.com.view.body.quantdatasetsoverview;

import com.ejt.vaadin.sizereporter.ComponentResizeEvent;
import com.ejt.vaadin.sizereporter.ComponentResizeListener;
import com.ejt.vaadin.sizereporter.SizeReporter;
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
import com.vaadin.ui.UI;
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
import probe.com.selectionmanager.QuantCentralManager;
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
    private final QuantCentralManager Quant_Central_Manager;
    private TabSheet proteinsTabsheet;
    private Label compOrederTitle;
    private final Map<Integer, Tab> tabIndexMap = new HashMap<Integer, Tab>();
    private Label noProtLabel = new Label("<h4 style='font-family:verdana;color:#8A0808;font-weight:bold;'>\t \t Select proteins to show their information !</h4>");
    private final CSFPRHandler CSFPR_Handler;
    private final Map<Integer, ProteinOverviewJFreeLineChartContainer> tabLayoutIndexMap = new HashMap<Integer, ProteinOverviewJFreeLineChartContainer>();

    private boolean externalSelection = false;
    private Map<String, Integer> customizedTrendMap;
    private final boolean showCustTrend;

    private final int pageWidth = Page.getCurrent().getBrowserWindowWidth();

    /**
     * central selection manager event
     *
     * @param type
     */
    @Override
    public void selectionChanged(String type) {
      
        if (type.equalsIgnoreCase("Comparison_Selection")) {
            Set<QuantDiseaseGroupsComparison> selectedComparisonList = Quant_Central_Manager.getSelectedDiseaseGroupsComparisonList();
            if (selectedComparisonList.isEmpty() && proteinsTabsheet != null) {
                proteinsTabsheet.removeAllComponents();
                protSelectionMap.clear();
                protSelectionTabMap.clear();
                protSelectionKeyTabMap.clear();
                noProtLabel.setVisible(true);
                proteinsTabsheet.setVisible(false);
            }
        }
        if (type.equalsIgnoreCase("Quant_Table_Protein_Selection") ||(type.equalsIgnoreCase("Tab_Protein_Selection"))) {

            if (lastSelectedTab != null) {
                externalSelection = true;
            }
            if (proteinsTabsheet != null && proteinsTabsheet.getComponentCount() < 20) {
                proteinsTabsheet.removeAllComponents();
            } else {
                this.initTabsheet();
            }
            protSelectionMap.clear();
            protSelectionTabMap.clear();
            protSelectionKeyTabMap.clear();
            protSelectionMap.putAll(Quant_Central_Manager.getQuantProteinsLayoutSelectionMap());
            if (protSelectionMap.isEmpty()) {
                noProtLabel.setVisible(true);
                proteinsTabsheet.setVisible(false);
                return;
            }
            noProtLabel.setVisible(false);
            Set<QuantDiseaseGroupsComparison> selectedComparisonList = Quant_Central_Manager.getSelectedDiseaseGroupsComparisonList();
            proteinsTabsheet.setVisible(true);
            Page.getCurrent().getStyles().add(".v-tabsheet-tabs .v-icon{"
                    + "    width:" + ((selectedComparisonList.size() * 16)+15) + "px !important;"
                    + "    height:35px !important;"
                    + "    color:blue !important;"
                    + "     margin-left:-2px !important;"
                    + "}");

            int tabCounter = 0;
            tabIndexMap.clear();
            tabLayoutIndexMap.clear();
            for (final String key : protSelectionMap.keySet()) {
                String protAcc = key.replace("--", "").trim().split(",")[0];
                String protName = key.replace("--", "").trim().split(",")[1];
                HorizontalLayout vlo;
                if (showCustTrend) {
                    vlo = generateProtTab(key, protName, protAcc, protSelectionMap.get(key), selectedComparisonList, customizedTrendMap.get(protAcc));
                } else {
                    vlo = generateProtTab(key, protName, protAcc, protSelectionMap.get(key), selectedComparisonList);
                }
                final Tab t1;
                t1 = proteinsTabsheet.addTab(vlo);

                t1.setCaption(protName.replace("(", "__").split("__")[0].trim()+"");
                t1.setClosable(true);
               
                t1.setId(key);
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
            if (lastSelectedTab != null) {
                externalSelection = false;
                proteinsTabsheet.setSelectedTab(protSelectionKeyTabMap.get(lastSelectedTab.getId()));

            }

            if (initScroll) {
                initScroll = false;
                if (this.getUI() != null && compOrederTitle != null) {
                    this.getUI().scrollIntoView(compOrederTitle);
                }
            }

        } 
//        else if (type.equalsIgnoreCase("Quant_Table_Protein_Selection")||(type.equalsIgnoreCase("Tab_Protein_Selection")) && !selfSelection) {
//            String selectedProteinKey = Quant_Central_Manager.getSelectedProteinKey();
//            Tab tab = protSelectionKeyTabMap.get(selectedProteinKey);
//            if (tab != null) {
//                Integer index = (Integer) ((HorizontalLayout) tab.getComponent()).getData();
//                proteinsTabsheet.setSelectedTab(index);
////                this.getUI().scrollIntoView(proteinsTabsheet);
//
//            } else {
//
//            }
//        }
        else {
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
     * @param mainHandler
     */
    public QuantProteinsTabsheetContainerLayout(final QuantCentralManager datasetExploringCentralSelectionManager, boolean searchingMode, CSFPRHandler mainHandler) {
        this.showCustTrend = false;
        this.searchingMode = searchingMode;
        this.CSFPR_Handler = mainHandler;

        this.Quant_Central_Manager = datasetExploringCentralSelectionManager;
        this.Quant_Central_Manager.registerStudySelectionListener(QuantProteinsTabsheetContainerLayout.this);
        this.setHeightUndefined();
        this.setMargin(new MarginInfo(false, false, false, false));
        this.noProtLabel.setContentMode(ContentMode.HTML);
        noProtLabel.setWidth("400px");
        noProtLabel.setHeight("40px");
        this.addComponent(noProtLabel);
        this.setComponentAlignment(noProtLabel, Alignment.TOP_LEFT);
        final SizeReporter sizeReporter = new SizeReporter(QuantProteinsTabsheetContainerLayout.this);
        sizeReporter.addResizeListener(new ComponentResizeListener() {
            int resizedCounter;

            @Override
            public void sizeChanged(ComponentResizeEvent event) {
                if (resizedCounter == 2) {
                    UI.getCurrent().scrollIntoView(QuantProteinsTabsheetContainerLayout.this);
                    sizeReporter.removeResizeListener(this);
                }
                resizedCounter++;
            }
        });

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

    /**
     *
     * @param datasetExploringCentralSelectionManager
     * @param searchingMode
     * @param mainHandler
     * @param customizedTrendMap
     */
    public QuantProteinsTabsheetContainerLayout(final QuantCentralManager datasetExploringCentralSelectionManager, boolean searchingMode, CSFPRHandler mainHandler, Map<String, Integer> customizedTrendMap) {
        this.showCustTrend = true;
        this.customizedTrendMap = customizedTrendMap;
        this.searchingMode = searchingMode;
        this.CSFPR_Handler = mainHandler;

        this.Quant_Central_Manager = datasetExploringCentralSelectionManager;
        this.Quant_Central_Manager.registerStudySelectionListener(QuantProteinsTabsheetContainerLayout.this);

        this.setHeightUndefined();
        this.setMargin(new MarginInfo(false, false, false, false));
        this.noProtLabel.setContentMode(ContentMode.HTML);
        noProtLabel.setWidth("400px");
        noProtLabel.setHeight("40px");
        this.addComponent(noProtLabel);
        this.setComponentAlignment(noProtLabel, Alignment.TOP_LEFT);
        final SizeReporter sizeReporter = new SizeReporter(QuantProteinsTabsheetContainerLayout.this);
        sizeReporter.addResizeListener(new ComponentResizeListener() {
            int resizedCounter;

            @Override
            public void sizeChanged(ComponentResizeEvent event) {
                if (resizedCounter == 2) {
                    UI.getCurrent().scrollIntoView(QuantProteinsTabsheetContainerLayout.this);
                    sizeReporter.removeResizeListener(this);
                }
                resizedCounter++;
            }
        });
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

    private boolean init = true;

    private void initTabsheet() {
        if (proteinsTabsheet != null) {
            this.removeComponent(proteinsTabsheet);
        }
        proteinsTabsheet = new TabSheet();
        proteinsTabsheet.setCaptionAsHtml(true);
        proteinsTabsheet.setHeightUndefined();
        proteinsTabsheet.setStyleName(Reindeer.TABSHEET_MINIMAL);
        proteinsTabsheet.setVisible(false);
        proteinsTabsheet.setImmediate(true);
        proteinsTabsheet.addSelectedTabChangeListener(QuantProteinsTabsheetContainerLayout.this);
        proteinsTabsheet.setCloseHandler(new TabSheet.CloseHandler() {
            @Override
            public void onTabClose(TabSheet tabsheet, Component tabContent) {
                protSelectionMap.remove(protSelectionTabMap.get(tabContent));
                if (protSelectionMap.isEmpty()) {
                    proteinsTabsheet.setVisible(false);
                }

                Quant_Central_Manager.setQuantProteinsSelectionLayout(new LinkedHashMap<String, DiseaseGroupsComparisonsProteinLayout[]>(protSelectionMap));
                Quant_Central_Manager.QuantProteinsTableSelectionChanged("Tab_Protein_Selection");
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
        bodyLayout.setWidth("100%");
        bodyLayout.setSpacing(true);
        bodyLayout.setMargin(new MarginInfo(true, false, false, false));
        bodyLayout.setHeightUndefined();
        bodyLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        ProteinOverviewJFreeLineChartContainer overallPlotLayout = new ProteinOverviewJFreeLineChartContainer(Quant_Central_Manager, CSFPR_Handler, diseaseGroupsComparisonsProteinArray, selectedDiseaseGroupsComparisonsList, (pageWidth), quantProteinName, quantProteinAccession, searchingMode, proteinKey);
        bodyLayout.addComponent(overallPlotLayout);
        bodyLayout.setComponentAlignment(overallPlotLayout, Alignment.TOP_CENTER);
        return bodyLayout;

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
    private HorizontalLayout generateProtTab(String proteinKey, String quantProteinName, String quantProteinAccession, DiseaseGroupsComparisonsProteinLayout[] diseaseGroupsComparisonsProteinArray, Set<QuantDiseaseGroupsComparison> selectedDiseaseGroupsComparisonsList, int custTrend) {
        HorizontalLayout bodyLayout = new HorizontalLayout();
        bodyLayout.setWidth("100%");
        bodyLayout.setSpacing(true);
        bodyLayout.setMargin(new MarginInfo(true, false, false, false));
        bodyLayout.setHeightUndefined();
        bodyLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        ProteinOverviewJFreeLineChartContainer overallPlotLayout = new ProteinOverviewJFreeLineChartContainer(Quant_Central_Manager, CSFPR_Handler, diseaseGroupsComparisonsProteinArray, selectedDiseaseGroupsComparisonsList, (pageWidth), quantProteinName, quantProteinAccession, searchingMode, proteinKey, custTrend);
        bodyLayout.addComponent(overallPlotLayout);
        bodyLayout.setComponentAlignment(overallPlotLayout, Alignment.TOP_CENTER);
        return bodyLayout;

    }

    /**
     * redraw quant bar charts
     */
    public void redrawCharts() {
        try {
            if (proteinsTabsheet == null || proteinsTabsheet.getSelectedTab() == null) {
//                ((HideOnClickLayout) this.getParent()).setVisability(true);
                this.noProtLabel.setVisible(true);
                this.setHeightUndefined();
                return;
            }
            this.noProtLabel.setVisible(false);
            ((HideOnClickLayout) this.getParent()).setVisability(true);
            HorizontalLayout selectedTab = (HorizontalLayout) proteinsTabsheet.getSelectedTab();
            ProteinOverviewJFreeLineChartContainer overallPlotLayout = (ProteinOverviewJFreeLineChartContainer) selectedTab.getComponent(0);
            overallPlotLayout.redrawCharts();
            this.setHeight((overallPlotLayout.getChartHeight() + 200) + "px");
            if (init) {
                init = false;
//                UI.getCurrent().scrollIntoView(QuantProteinsTabsheetContainerLayout.this);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error at line 300 " + this.getClass().getName() + "  " + e.getMessage());
        }
    }
    private Tab lastSelectedTab = null;

    @Override
    public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
        redrawCharts();
        Integer index = (Integer) ((HorizontalLayout) event.getTabSheet().getSelectedTab()).getData();
        if (index != null) {
            int mincounter = Math.max(0, index - 10);
            int maxcounter = Math.min(index + 10, tabIndexMap.size());
            if (index < 10) {
                for (int x = mincounter; x < maxcounter; x++) {
                    if (tabIndexMap.get(x) != null && tabIndexMap.get(x).getIcon() == null) {
                        tabIndexMap.get(x).setIcon(new ExternalResource(tabLayoutIndexMap.get(x).getThumbChart()));
                    }
                }
            }

        }
        if (!externalSelection) {
            lastSelectedTab = event.getTabSheet().getTab(event.getTabSheet().getSelectedTab());
        }
    }

}
