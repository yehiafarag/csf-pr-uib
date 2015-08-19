package probe.com.view.body.quantdatasetsoverview;

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
import java.util.Set;
import probe.com.selectionmanager.CSFFilter;
import probe.com.selectionmanager.DatasetExploringCentralSelectionManager;
import probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.DiseaseGroupsListFilter;

import probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.HeatMapFilter;

/**
 * this class represents the top filters layout for exploring datasets tab the
 * class include the the patients group comparison lists, filtering heat-map and
 * pie chart filters
 *
 * @author Yehia Farag
 */
public class DiseaseGroupsFiltersContainer extends GridLayout implements CSFFilter {
    
    private final DatasetExploringCentralSelectionManager exploringFiltersManager;
    
    private final OptionGroup optionGroup;
    private final DiseaseGroupsListFilter diseaseGroupsListFilter;
    private final HeatMapFilter diseaseGroupsHeatmapFilter;
    
    private final HorizontalLayout btnsLayout;
    private final QuantProteinsComparisonsContainer compTable;
    private final int tableWidth, heatmapW;
    private final float tableRatio;
    private final float heatmapRatio;
    private final Button selectAllFilterBtn;
    int pageWidth;
    
    public DiseaseGroupsFiltersContainer(final DatasetExploringCentralSelectionManager exploringFiltersManager, PopupInteractiveDSFiltersLayout pieChartFiltersBtn, final QuantProteinsComparisonsContainer compTable) {
        pageWidth = Page.getCurrent().getWebBrowser().getScreenWidth();
//        this.setHeight("100%");
        this.setWidth(pageWidth + "px");
        this.setHeightUndefined();
        this.setSpacing(true);
        this.setColumns(2);
        this.setMargin(false);
        this.setRows(2);
        
        this.exploringFiltersManager = exploringFiltersManager;
        this.compTable = compTable;
        
        diseaseGroupsListFilter = new DiseaseGroupsListFilter(exploringFiltersManager);
        Set<String> diseaseGroupsSet = diseaseGroupsListFilter.getDiseaseGroupsSet();
//        heatmapW = 156 + (50 * diseaseGroupsSet.size());
        heatmapW = Math.max((156 + (50 * diseaseGroupsSet.size())), 580);
        tableWidth = (pageWidth - heatmapW - 150);
        heatmapRatio = (float) heatmapW / (float) (pageWidth);
        tableRatio = (float) (tableWidth) / (float) (pageWidth);
        int heatmapH = 660;
        
        diseaseGroupsHeatmapFilter = new HeatMapFilter(exploringFiltersManager, heatmapW, diseaseGroupsSet, diseaseGroupsSet, diseaseGroupsListFilter.getPatientsGroupArr());
        diseaseGroupsHeatmapFilter.setHeight(heatmapH + "px");
        
        btnsLayout = new HorizontalLayout();
        btnsLayout.setWidth("100%");
        btnsLayout.setSpacing(true);
        btnsLayout.addComponent(diseaseGroupsListFilter.getDiseaseGroupsFilterBtn());
        btnsLayout.setComponentAlignment(diseaseGroupsListFilter.getDiseaseGroupsFilterBtn(), Alignment.MIDDLE_CENTER);
        
        Button clearFilterBtn = new Button("Reset");
        clearFilterBtn.setHeight("30px");
        clearFilterBtn.setStyleName(Reindeer.BUTTON_LINK);
        btnsLayout.addComponent(clearFilterBtn);
        btnsLayout.setComponentAlignment(clearFilterBtn, Alignment.TOP_LEFT);
        clearFilterBtn.setDescription("Reset all applied filters");
        btnsLayout.setWidthUndefined();
        btnsLayout.setHeight("100%");
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
        selectAllFilterBtn.setDescription("Select All Comparisons");
//        btnsLayout.setWidthUndefined();
//        btnsLayout.setHeight("100%");
        selectAllFilterBtn.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                diseaseGroupsHeatmapFilter.selectAll();
            }
        });
        selectAllFilterBtn.setEnabled(diseaseGroupsHeatmapFilter.isActiveSelectAll());
        
        final Button unSelectAllFilterBtn = new Button("Unselect All");
        unSelectAllFilterBtn.setHeight("30px");
        unSelectAllFilterBtn.setStyleName(Reindeer.BUTTON_LINK);
        btnsLayout.addComponent(unSelectAllFilterBtn);
        btnsLayout.setComponentAlignment(unSelectAllFilterBtn, Alignment.TOP_LEFT);
        unSelectAllFilterBtn.setDescription("Unselect All Comparisons");
//        btnsLayout.setWidthUndefined();
//        btnsLayout.setHeight("100%");
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
        btnsLayout.addComponent(pieChartFiltersBtn);
        btnsLayout.setComponentAlignment(pieChartFiltersBtn, Alignment.TOP_RIGHT);
        
        this.addComponent(diseaseGroupsHeatmapFilter, 0, 0);
        this.setComponentAlignment(diseaseGroupsHeatmapFilter, Alignment.TOP_LEFT);
        
        this.addComponent(btnsLayout, 0, 1);
        
        this.addComponent(compTable, 1, 0);
        this.setComponentAlignment(compTable, Alignment.TOP_CENTER);
        
        compTable.setLayoutWidth(tableWidth);
        this.setColumnExpandRatio(0, heatmapW);
        this.setColumnExpandRatio(1, tableWidth);
        this.addComponent(compTable.getBottomLayout(), 1, 1);
        this.setComponentAlignment(compTable.getBottomLayout(), Alignment.TOP_CENTER);
        LayoutEvents.LayoutClickListener hideShowCompTableListener = new LayoutEvents.LayoutClickListener() {
            
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                
                compTable.getHideCompariosonTableBtn().setVisible(diseaseGroupsHeatmapFilter.isVisible());
                diseaseGroupsHeatmapFilter.setVisible(!diseaseGroupsHeatmapFilter.isVisible());
                btnsLayout.setVisible(!btnsLayout.isVisible());
                if (!btnsLayout.isVisible()) {
                    compTable.setLayoutWidth((pageWidth - 100));
                    setColumnExpandRatio(0, 0f);
                    setColumnExpandRatio(1, 1f);
                } else {
                    compTable.setLayoutWidth(tableWidth);
                    setColumnExpandRatio(0, heatmapW);
                    setColumnExpandRatio(1, tableWidth);
                    
                }
            }
        };
        
        compTable.getHideCompariosonTableBtn().addLayoutClickListener(hideShowCompTableListener);
        diseaseGroupsHeatmapFilter.addHideHeatmapBtnListener(hideShowCompTableListener);
        
        exploringFiltersManager.registerFilter(DiseaseGroupsFiltersContainer.this);
//        selectAllFilterBtn.click();

    }
    
    public void selectAllComparisons() {
        if (diseaseGroupsHeatmapFilter.isActiveSelectAll()) {
            selectAllFilterBtn.click();
        }
    }
    
    @Override
    public void selectionChanged(String type) {
        selectAllFilterBtn.setEnabled(diseaseGroupsHeatmapFilter.isActiveSelectAll());
//        if (type.equalsIgnoreCase("ComparisonSelection")) {
//            if (exploringFiltersManager.getSelectedComparisonList().isEmpty()) {
//                viewComparisonHeatmap(true);
//            }
//
//        }

    }
    
    private void viewComparisonHeatmap(boolean view) {
        diseaseGroupsHeatmapFilter.setVisible(view);
        btnsLayout.setVisible(view);
        if (view) {
            compTable.setLayoutWidth(tableWidth);
            setColumnExpandRatio(0, heatmapRatio);
            setColumnExpandRatio(1, tableRatio);
            
        } else {
            compTable.setLayoutWidth((tableWidth + heatmapW));
            setColumnExpandRatio(0, 0f);
            setColumnExpandRatio(1, 1f);
            
        }
        
    }
    
    @Override
    public String getFilterId() {
        return "DiseaseGroupsFilter";
    }
    
    @Override
    public void removeFilterValue(String value) {
    }
    
}
