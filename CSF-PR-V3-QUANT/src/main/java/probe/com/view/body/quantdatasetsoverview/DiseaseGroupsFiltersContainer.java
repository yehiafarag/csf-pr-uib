package probe.com.view.body.quantdatasetsoverview;

import com.vaadin.data.Item;
import probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.PopupInteractiveDSFiltersLayout;
import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.themes.Reindeer;
import java.util.HashSet;
import java.util.Set;
import probe.com.handlers.CSFPRHandler;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
import probe.com.selectionmanager.CSFFilter;
import probe.com.selectionmanager.DatasetExploringCentralSelectionManager;
import probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.DiseaseGroupsListFilter;

import probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.HeatMapFilter;
import probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.ComparisonsSelectionOverviewBubbleChart;

/**
 * This class represents the top filters layout for exploring datasets tab the
 * class include the the patients group comparison lists, filtering heat-map and
 * pie chart filters
 *
 * @author Yehia Farag
 */
public class DiseaseGroupsFiltersContainer extends GridLayout implements CSFFilter {

    private final OptionGroup optionGroup;
    private final DiseaseGroupsListFilter diseaseGroupsListFilter;
    private final HeatMapFilter diseaseGroupsHeatmapFilter;

    private final HorizontalLayout btnsLayout;
    private final OptionGroup significantProteinsOnlyOption;
  
    private final int initLayoutWidth, heatmapW;
    private final float initialLayoutRatio;
    private final float heatmapRatio;
    private final Button selectAllFilterBtn;
    private int pageWidth;
    private final CSFPRHandler handler;

    /**
     *
     * @param exploringFiltersManager
     * @param handler 
     * @param pieChartFiltersBtn
     */
    public DiseaseGroupsFiltersContainer(final DatasetExploringCentralSelectionManager exploringFiltersManager,CSFPRHandler handler, PopupInteractiveDSFiltersLayout pieChartFiltersBtn) {
        pageWidth = Page.getCurrent().getWebBrowser().getScreenWidth();
        this.handler=handler;
        this.setWidth(pageWidth + "px");
        this.setHeightUndefined();
        this.setSpacing(true);
        this.setColumns(1);
        this.setMargin(false);
        this.setRows(4);

      

        int heatmapCellWidth =45;
        diseaseGroupsListFilter = new DiseaseGroupsListFilter(exploringFiltersManager);
        Set<String> diseaseGroupsSet = diseaseGroupsListFilter.getDiseaseGroupsSet();
        heatmapW = (156 + (heatmapCellWidth * diseaseGroupsSet.size()));

        final int layoutWidth = (pageWidth - 70);

        final HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth(layoutWidth + "px");
        topLayout.setHeightUndefined();

        this.addComponent(topLayout, 0, 0);
        this.setComponentAlignment(topLayout, Alignment.TOP_LEFT);
//               
        initLayoutWidth = (pageWidth - heatmapW - 300);
        heatmapRatio = (float) heatmapW / (float) (pageWidth);
        initialLayoutRatio = (float) (initLayoutWidth) / (float) (pageWidth);
        int heatmapH = heatmapW + 20;

        diseaseGroupsHeatmapFilter = new HeatMapFilter(exploringFiltersManager, heatmapW, diseaseGroupsSet, diseaseGroupsSet, diseaseGroupsListFilter.getPatientsGroupArr(),heatmapCellWidth);
        diseaseGroupsHeatmapFilter.setHeight(heatmapH + "px");
        topLayout.addComponent(diseaseGroupsHeatmapFilter);
        topLayout.setComponentAlignment(diseaseGroupsHeatmapFilter, Alignment.TOP_LEFT);
        
        final ComparisonsSelectionOverviewBubbleChart selectionOverviewBubbleChart = new ComparisonsSelectionOverviewBubbleChart(exploringFiltersManager,handler,initLayoutWidth, heatmapH,new HashSet<QuantDiseaseGroupsComparison>());
        topLayout.addComponent(selectionOverviewBubbleChart);
        topLayout.setComponentAlignment(selectionOverviewBubbleChart, Alignment.TOP_LEFT);

        
//        topLayout.setComponentAlignment(comparisonsTableLayout.getInitialLayout(), Alignment.TOP_CENTER);
        topLayout.setExpandRatio(diseaseGroupsHeatmapFilter, heatmapRatio);
        topLayout.setExpandRatio(selectionOverviewBubbleChart, initialLayoutRatio);

        btnsLayout = new HorizontalLayout();
        btnsLayout.setWidth("100%");
        btnsLayout.setSpacing(true);
        Button diseaseGroupsFilterBtn = diseaseGroupsListFilter.getDiseaseGroupsFilterBtn();
        diseaseGroupsFilterBtn.setHeight("30px");
        btnsLayout.addComponent(diseaseGroupsFilterBtn);
        btnsLayout.setComponentAlignment(diseaseGroupsFilterBtn, Alignment.TOP_CENTER);

        Button clearFilterBtn = new Button("Reset");
        clearFilterBtn.setHeight("30px");
        clearFilterBtn.setStyleName(Reindeer.BUTTON_LINK);
        btnsLayout.addComponent(clearFilterBtn);
        btnsLayout.setComponentAlignment(clearFilterBtn, Alignment.TOP_LEFT);
        clearFilterBtn.setDescription("Reset all applied filters");
        btnsLayout.setWidthUndefined();
        btnsLayout.setHeight("50px");
        clearFilterBtn.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                exploringFiltersManager.resetFilters();
            }

        });

        selectAllFilterBtn = new Button("Select All");
        selectAllFilterBtn.setHeight("30px");
        selectAllFilterBtn.setStyleName(Reindeer.BUTTON_LINK);
        btnsLayout.addComponent(selectAllFilterBtn);
        btnsLayout.setComponentAlignment(selectAllFilterBtn, Alignment.TOP_LEFT);
        selectAllFilterBtn.setDescription("Select All Disease Groups Comparisons");
        selectAllFilterBtn.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                diseaseGroupsHeatmapFilter.selectAll();
            }
        });
//        selectAllFilterBtn.setEnabled(diseaseGroupsHeatmapFilter.isActiveSelectAll());

        final Button unSelectAllFilterBtn = new Button("Unselect All");
        unSelectAllFilterBtn.setHeight("30px");
        unSelectAllFilterBtn.setStyleName(Reindeer.BUTTON_LINK);
        btnsLayout.addComponent(unSelectAllFilterBtn);
        btnsLayout.setComponentAlignment(unSelectAllFilterBtn, Alignment.TOP_LEFT);
        unSelectAllFilterBtn.setDescription("Unselect All Disease Groups Comparisons");
        unSelectAllFilterBtn.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {

                diseaseGroupsHeatmapFilter.unselectAll();
            }

        });

        this.optionGroup = new OptionGroup();
        btnsLayout.addComponent(optionGroup);
        btnsLayout.setComponentAlignment(optionGroup, Alignment.TOP_CENTER);

        optionGroup.setWidth("250px");
        optionGroup.setNullSelectionAllowed(false); // user can not 'unselect'
        optionGroup.setMultiSelect(false);

        optionGroup.addItem("Single selection");
        optionGroup.addItem("Multiple selection");
        optionGroup.setValue("Single selection");
        optionGroup.addStyleName("horizontal");
        optionGroup.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (optionGroup.getValue().toString().equalsIgnoreCase("Single selection")) {
                    diseaseGroupsHeatmapFilter.setSingleSelection(true);
                } else {
                    diseaseGroupsHeatmapFilter.setSingleSelection(false);
                }
            }
        });
        
         significantProteinsOnlyOption = new OptionGroup();
        btnsLayout.addComponent(significantProteinsOnlyOption);
        btnsLayout.setComponentAlignment(significantProteinsOnlyOption, Alignment.TOP_CENTER);
        significantProteinsOnlyOption.setWidth("130px");
//        significantProteinsOnlyOption.setHeight("40px");
        significantProteinsOnlyOption.setNullSelectionAllowed(true); // user can not 'unselect'
        significantProteinsOnlyOption.setMultiSelect(true);

        significantProteinsOnlyOption.addItem("Significant Only");
        significantProteinsOnlyOption.addStyleName("horizontal");
        significantProteinsOnlyOption.addValueChangeListener(new Property.ValueChangeListener() {


            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                    exploringFiltersManager.updateSignificantOnlySelection(significantProteinsOnlyOption.getValue().toString().equalsIgnoreCase("[Significant Only]"));               
            
            }
                  
            
        });
        
        
        btnsLayout.addComponent(pieChartFiltersBtn);
        btnsLayout.setComponentAlignment(pieChartFiltersBtn, Alignment.TOP_RIGHT);

        this.addComponent(btnsLayout, 0, 1);

       
        LayoutEvents.LayoutClickListener hideShowCompTableListener = new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                diseaseGroupsHeatmapFilter.setVisible(!diseaseGroupsHeatmapFilter.isVisibleComponent());
                btnsLayout.setVisible(!btnsLayout.isVisible());
                if (!btnsLayout.isVisible()) {
                    selectionOverviewBubbleChart.updateSize(pageWidth - 250);
                    topLayout.setExpandRatio(diseaseGroupsHeatmapFilter, 250);
                    topLayout.setExpandRatio(selectionOverviewBubbleChart, (pageWidth - 250));
                    topLayout.setWidthUndefined();
                } else {
                     selectionOverviewBubbleChart.updateSize(initLayoutWidth);
                    topLayout.setExpandRatio(diseaseGroupsHeatmapFilter, heatmapW);
                    topLayout.setExpandRatio(selectionOverviewBubbleChart, initLayoutWidth);
                    topLayout.setWidth(layoutWidth + "px");

                }
            }
        };

        diseaseGroupsHeatmapFilter.addHideHeatmapBtnListener(hideShowCompTableListener);
        exploringFiltersManager.registerFilter(DiseaseGroupsFiltersContainer.this);

    }

    /**
     * select all available disease groups comparisons
     */
    public void selectAllComparisons() {
        if (diseaseGroupsHeatmapFilter.isActiveSelectAll()) {
            selectAllFilterBtn.click();
        }
    }

    /**
     *
     * @param type
     */
    @Override
    public void selectionChanged(String type) {
//        selectAllFilterBtn.setEnabled(diseaseGroupsHeatmapFilter.isActiveSelectAll());

    }

    /**
     * get filter id
     *
     * @return string filter id
     */
    @Override
    public String getFilterId() {
        return "DiseaseGroupsFilter";
    }

    /**
     * remove filter from selection manager (not apply for this class)
     *
     * @param value
     */
    @Override
    public void removeFilterValue(String value) {
    }

}
