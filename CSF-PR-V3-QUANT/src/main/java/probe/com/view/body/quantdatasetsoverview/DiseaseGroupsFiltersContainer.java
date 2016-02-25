package probe.com.view.body.quantdatasetsoverview;

import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import probe.com.handlers.CSFPRHandler;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
import probe.com.model.beans.quant.QuantProtein;
import probe.com.selectionmanager.CSFFilter;
import probe.com.selectionmanager.QuantCentralManager;

import probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.HeatMapFilter;
import probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.ComparisonsSelectionOverviewBubbleChart;
import probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.PopupRecombineDiseaseGroups;
import probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.PopupReorderGroupsLayout;
import probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.StudiesInformationPopupBtn;
import probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.interactivepiechartfilters.StudiesPieChartFiltersContainerLayout;
import probe.com.view.core.Tips;

/**
 * This class represents the top filters layout for exploring datasets tab the
 * class include the the patients group comparison lists, filtering heat-map and
 * pie chart filters
 *
 * @author Yehia Farag
 */
public class DiseaseGroupsFiltersContainer extends HorizontalLayout implements CSFFilter {

//    private final DiseaseGroupsListFilter diseaseGroupsListFilter;
    private final HeatMapFilter heatmapFilterComponent;

    private final HorizontalLayout btnsLayout;

    private int leftPanelWidth;
    private int rightPanelWidth;//, heatmapW;
    private float rightPanelExpandingRatio;
    private float leftPanelExpendingRatio;
    private int pageWidth;
    private final VerticalLayout pieChartFiltersBtnWrapper;
    private final int heatmapCellWidth = 30;
    private final int heatmapHeaderCellWidth = 135;
    private final QuantCentralManager Quant_Central_Manager;
    private final ComparisonsSelectionOverviewBubbleChart selectionOverviewBubbleChart;
//    private final HorizontalLayout middleLayout;
    private final int mainContainerWidth;
    private final int standeredChartHeight;
    private final HorizontalLayout diseaseCategorySelectLayout;
    private final HorizontalLayout rightBottomBtnLayout;
    private final PopupReorderGroupsLayout reorderGroups;
    private final Label counterLabel;

    private final VerticalLayout rightContainerLayout, leftContainerLayout;

    /**
     *
     * @param Quant_Central_Manager
     * @param CSFPR_Handler
     * @param searchQuantificationProtList
     * @param userCustomizedComparison
     */
    public DiseaseGroupsFiltersContainer(final QuantCentralManager Quant_Central_Manager, final CSFPRHandler CSFPR_Handler, List<QuantProtein> searchQuantificationProtList, QuantDiseaseGroupsComparison userCustomizedComparison) {
        pageWidth = Page.getCurrent().getWebBrowser().getScreenWidth();
        this.setWidth(pageWidth + "px");
        this.setHeightUndefined();
        this.setSpacing(true);
        this.setMargin(false);
        this.Quant_Central_Manager = Quant_Central_Manager;
        final LinkedHashSet<String> diseaseGroupsRowSet = Quant_Central_Manager.getSelectedHeatMapRows(); //diseaseGroupsListFilter.getDiseaseGroupsSet();
        final LinkedHashSet<String> diseaseGroupsColSet = Quant_Central_Manager.getSelectedHeatMapColumns(); //diseaseGroupsListFilter.getDiseaseGroupsSet();

        leftContainerLayout = new VerticalLayout();
        rightContainerLayout = new VerticalLayout();

        this.addComponent(leftContainerLayout);
        this.addComponent(rightContainerLayout);

        //init left panel 
        leftContainerLayout.setSpacing(true);
        Set<String> diseaseSet = Quant_Central_Manager.getDiseaseCategorySet();
        NativeSelect diseaseTypeSelectionList = new NativeSelect();
        diseaseTypeSelectionList.setDescription("Select disease category");

        for (String disease : diseaseSet) {
            diseaseTypeSelectionList.addItem(disease);
            diseaseTypeSelectionList.setItemCaption(disease, (disease));

        }

        diseaseCategorySelectLayout = new HorizontalLayout();
        diseaseCategorySelectLayout.setWidthUndefined();
        diseaseCategorySelectLayout.setSpacing(true);
        diseaseCategorySelectLayout.setMargin(new MarginInfo(false, false, true, false));

        Label title = new Label("Disease Category");
        title.setStyleName(Reindeer.LABEL_SMALL);
        diseaseCategorySelectLayout.addComponent(title);

        diseaseCategorySelectLayout.setComponentAlignment(title, Alignment.MIDDLE_CENTER);
        counterLabel = new Label("( " + Quant_Central_Manager.getCurrentDsNumber() + " / " + Quant_Central_Manager.getTotalDsNumber() + " )");
        diseaseTypeSelectionList.setWidth("200px");
        diseaseTypeSelectionList.setNullSelectionAllowed(false);
        diseaseTypeSelectionList.setValue(Quant_Central_Manager.getInUseDiseaseName());
        diseaseTypeSelectionList.setImmediate(true);
        diseaseCategorySelectLayout.addComponent(diseaseTypeSelectionList);
        diseaseCategorySelectLayout.setComponentAlignment(diseaseTypeSelectionList, Alignment.TOP_LEFT);
        diseaseTypeSelectionList.setStyleName("diseaseselectionlist");
        diseaseTypeSelectionList.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Quant_Central_Manager.changeDiseaseCategory(event.getProperty().getValue().toString());
                Quant_Central_Manager.resetFiltersListener();
                pieChartFiltersBtnWrapper.removeAllComponents();
                StudiesPieChartFiltersContainerLayout pieChartFiltersLayout = new StudiesPieChartFiltersContainerLayout(Quant_Central_Manager, CSFPR_Handler);
                pieChartFiltersBtnWrapper.addComponent(pieChartFiltersLayout.getPieChartFiltersBtn());
                counterLabel.setValue("( " + Quant_Central_Manager.getCurrentDsNumber() + " / " + Quant_Central_Manager.getTotalDsNumber() + " )");

            }
        });

        diseaseCategorySelectLayout.addComponent(counterLabel);
        diseaseCategorySelectLayout.setComponentAlignment(counterLabel, Alignment.MIDDLE_CENTER);

        leftContainerLayout.addComponent(diseaseCategorySelectLayout);
        leftContainerLayout.setComponentAlignment(diseaseCategorySelectLayout, Alignment.TOP_LEFT);

        this.resizeLayout(Math.max(diseaseGroupsColSet.size(), diseaseGroupsRowSet.size()));

        int heatmapH = Math.max((heatmapHeaderCellWidth + 20 + 12 + (heatmapCellWidth * Math.max(diseaseGroupsColSet.size(), diseaseGroupsRowSet.size()))), 500) + 20;
        standeredChartHeight = Math.max(heatmapH + 68, 520);
        heatmapFilterComponent = new HeatMapFilter(Quant_Central_Manager, leftPanelWidth, diseaseGroupsRowSet, diseaseGroupsColSet, Quant_Central_Manager.getDiseaseGroupsArray(), heatmapCellWidth, heatmapHeaderCellWidth, CSFPR_Handler.getDiseaseFullNameMap());
        heatmapFilterComponent.setHeight(Math.max(heatmapH + 10, 500) + "px");
        heatmapFilterComponent.setSingleSelection(false);
        leftContainerLayout.addComponent(heatmapFilterComponent);
        leftContainerLayout.setComponentAlignment(heatmapFilterComponent, Alignment.TOP_LEFT);

        //init heatmap filters buttons 
        btnsLayout = new HorizontalLayout();
        btnsLayout.setWidth(leftPanelWidth + "px");
        btnsLayout.setHeight("24px");
        btnsLayout.setSpacing(true);
        this.leftContainerLayout.addComponent(btnsLayout);

        HorizontalLayout updatedFiltersContainer = new HorizontalLayout();
        updatedFiltersContainer.setStyleName("filtercontainer");
        updatedFiltersContainer.setWidth("80px");
        btnsLayout.addComponent(updatedFiltersContainer);
        btnsLayout.setComponentAlignment(updatedFiltersContainer, Alignment.MIDDLE_LEFT);
        btnsLayout.setExpandRatio(updatedFiltersContainer, 500f / leftPanelWidth);
        btnsLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        

        Label filterBtns = new Label("Filters");
        filterBtns.setHeight("25px");
        filterBtns.setDescription("Show filters");
        filterBtns.setStyleName("showfilter");
        filterBtns.setWidth("80px");
        updatedFiltersContainer.addComponent(filterBtns);

//        VerticalLayout angelRightLabel = new VerticalLayout();
//        angelRightLabel.setHeight("25px");
//        angelRightLabel.setDescription("Show filters");
//        angelRightLabel.setStyleName("showfilter");
//        angelRightLabel.setWidth("25px");
//        updatedFiltersContainer.addComponent(angelRightLabel);
//        
        
        
        

        HorizontalLayout leftBottomBtnLayout = new HorizontalLayout();
        leftBottomBtnLayout.setWidthUndefined();
        leftBottomBtnLayout.setSpacing(true);
        leftBottomBtnLayout.setHeightUndefined();
        
//        btnsLayout.addComponent(leftBottomBtnLayout);
//        btnsLayout.setComponentAlignment(leftBottomBtnLayout, Alignment.MIDDLE_LEFT);
//        btnsLayout.setExpandRatio(leftBottomBtnLayout, 500f / leftPanelWidth);
        btnsLayout.setStyleName(Reindeer.LAYOUT_WHITE);

        Label filterTitle = new Label("Filters:");
        filterTitle.setHeight("24px");
        filterTitle.setDescription("Available filters");
        filterTitle.setStyleName("filtercaption");
        filterTitle.setWidth("50px");
        leftBottomBtnLayout.addComponent(filterTitle);

        StudiesPieChartFiltersContainerLayout pieChartFiltersLayout = new StudiesPieChartFiltersContainerLayout(Quant_Central_Manager, CSFPR_Handler);

        pieChartFiltersBtnWrapper = new VerticalLayout();
        pieChartFiltersBtnWrapper.addComponent(pieChartFiltersLayout.getPieChartFiltersBtn());
        leftBottomBtnLayout.addComponent(pieChartFiltersBtnWrapper);
        leftBottomBtnLayout.setComponentAlignment(pieChartFiltersBtnWrapper, Alignment.MIDDLE_LEFT);

        reorderGroups = new PopupReorderGroupsLayout(Quant_Central_Manager);
//        updatedFiltersContainer.addComponent(reorderGroups);
//        updatedFiltersContainer.setComponentAlignment(reorderGroups, Alignment.TOP_LEFT);

        PopupRecombineDiseaseGroups recombineGroups = new PopupRecombineDiseaseGroups(Quant_Central_Manager);
//        updatedFiltersContainer.addComponent(recombineGroups);
//        updatedFiltersContainer.setComponentAlignment(recombineGroups, Alignment.TOP_LEFT);
//
        Button clearFilterBtn = new Button("Reset");
        clearFilterBtn.setHeight("24px");
        clearFilterBtn.setStyleName(Reindeer.BUTTON_LINK);
        clearFilterBtn.addStyleName("heatmapbtns");
//        updatedFiltersContainer.addComponent(clearFilterBtn);
//        updatedFiltersContainer.setComponentAlignment(clearFilterBtn, Alignment.MIDDLE_LEFT);
        clearFilterBtn.setDescription("Reset all applied filters");

        clearFilterBtn.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Quant_Central_Manager.resetFiltersListener();
            }

        });
        final OptionGroup noSerumOption = new OptionGroup();
//        updatedFiltersContainer.addComponent(noSerumOption);
//        updatedFiltersContainer.setComponentAlignment(noSerumOption, Alignment.MIDDLE_LEFT);
        noSerumOption.setWidth("80px");
        noSerumOption.setHeight("24px");
        noSerumOption.setNullSelectionAllowed(true); // user can not 'unselect'
        noSerumOption.setMultiSelect(true);

        noSerumOption.addItem("Serum Studies");
        noSerumOption.addStyleName("horizontal");
        noSerumOption.addStyleName("heatmapbtns");
        noSerumOption.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Quant_Central_Manager.setNoSerum(!noSerumOption.getValue().toString().equalsIgnoreCase("[Serum Studies]"));
                Quant_Central_Manager.resetFiltersListener();
                pieChartFiltersBtnWrapper.removeAllComponents();
                StudiesPieChartFiltersContainerLayout pieChartFiltersLayout = new StudiesPieChartFiltersContainerLayout(Quant_Central_Manager, CSFPR_Handler);
                pieChartFiltersBtnWrapper.addComponent(pieChartFiltersLayout.getPieChartFiltersBtn());
                counterLabel.setValue("( " + Quant_Central_Manager.getCurrentDsNumber() + " / " + Quant_Central_Manager.getTotalDsNumber() + " )");

            }

        });
//        
//         int y = Page.getCurrent().getBrowserWindowHeight() - Math.max(heatmapH, 710) - 200;
//        Tips tips = CSFPR_Handler.getTipsGenerator().generateTip("Remeber you can sort and select the disease groups using <u>Sort and Select</u> feature", 180, y);
//        leftBottomBtnLayout.addComponent(tips);
//        leftBottomBtnLayout.setComponentAlignment(tips, Alignment.TOP_LEFT);

        rightBottomBtnLayout = new HorizontalLayout();
        rightBottomBtnLayout.setHeightUndefined();
        rightBottomBtnLayout.setWidthUndefined();
        rightBottomBtnLayout.setSpacing(true);
        btnsLayout.addComponent(rightBottomBtnLayout);
        btnsLayout.setComponentAlignment(rightBottomBtnLayout, Alignment.TOP_RIGHT);

        StudiesInformationPopupBtn showStudiesBtn = new StudiesInformationPopupBtn(Quant_Central_Manager);
        showStudiesBtn.addStyleName("heatmapbtns-long");
        rightBottomBtnLayout.addComponent(showStudiesBtn);
        rightBottomBtnLayout.setComponentAlignment(showStudiesBtn, Alignment.TOP_LEFT);

        Button exportTableBtn = new Button("");
        exportTableBtn.setHeight("23px");
        exportTableBtn.setWidth("23px");
        exportTableBtn.setPrimaryStyleName("exportxslbtn");
        exportTableBtn.addStyleName("heatmapbtns");
        rightBottomBtnLayout.addComponent(exportTableBtn);
        rightBottomBtnLayout.setComponentAlignment(exportTableBtn, Alignment.BOTTOM_RIGHT);
        exportTableBtn.setDescription("Export all studies data");
        final QuantDatasetsfullStudiesTableLayout quantStudiesTable = new QuantDatasetsfullStudiesTableLayout(Quant_Central_Manager);
        rightBottomBtnLayout.addComponent(quantStudiesTable);

        exportTableBtn.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                ExcelExport csvExport = new ExcelExport(quantStudiesTable, "CSF-PR  Quant Studies Information");
                csvExport.setReportTitle("CSF-PR / Quant Studies Information ");
                csvExport.setExportFileName("CSF-PR - Quant Studies Information" + ".xls");
                csvExport.setMimeType(ExcelExport.EXCEL_MIME_TYPE);
                csvExport.setDisplayTotals(false);
                csvExport.export();

            }
        });

        VerticalLayout selectAllBtn = new VerticalLayout();
        selectAllBtn.setStyleName("selectallbtn");
        rightBottomBtnLayout.addComponent(selectAllBtn);
        rightBottomBtnLayout.setComponentAlignment(selectAllBtn, Alignment.TOP_LEFT);
        selectAllBtn.setDescription("Select All Disease Groups Comparisons");
        selectAllBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                heatmapFilterComponent.selectAll();
            }
        });

        VerticalLayout unselectAllBtn = new VerticalLayout();
        unselectAllBtn.setStyleName("unselectallbtn");
        rightBottomBtnLayout.addComponent(unselectAllBtn);
        rightBottomBtnLayout.setComponentAlignment(unselectAllBtn, Alignment.TOP_LEFT);
        unselectAllBtn.setDescription("Unselect All Disease Groups Comparisons");
        unselectAllBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                heatmapFilterComponent.unselectAll();
//                resizeLayout(diseaseGroupsSet.size());
            }
        });

        final VerticalLayout selectMultiBtn = new VerticalLayout();
        selectMultiBtn.setStyleName("selectmultiselectedbtn");
        rightBottomBtnLayout.addComponent(selectMultiBtn);
        rightBottomBtnLayout.setComponentAlignment(selectMultiBtn, Alignment.TOP_LEFT);
        selectMultiBtn.setDescription("Multiple selection");
        selectMultiBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                if (selectMultiBtn.getStyleName().equalsIgnoreCase("selectmultiselectedbtn")) {
                    heatmapFilterComponent.setSingleSelection(true);
                    selectMultiBtn.setStyleName("selectmultibtn");

                } else {
                    heatmapFilterComponent.setSingleSelection(false);
                    selectMultiBtn.setStyleName("selectmultiselectedbtn");

                }
            }
        });

        LayoutEvents.LayoutClickListener hideShowCompTableListener = new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                heatmapFilterComponent.setVisible(!heatmapFilterComponent.isVisibleComponent());
                rightBottomBtnLayout.setVisible(!rightBottomBtnLayout.isVisible());
                btnsLayout.setVisible(!btnsLayout.isVisible());
                diseaseCategorySelectLayout.setVisible(!diseaseCategorySelectLayout.isVisible());

//               
                if (!btnsLayout.isVisible()) {
                    selectionOverviewBubbleChart.updateSize(pageWidth - 250, Math.min(leftPanelWidth, standeredChartHeight));
                    DiseaseGroupsFiltersContainer.this.setExpandRatio(leftContainerLayout, 170);
                    DiseaseGroupsFiltersContainer.this.setExpandRatio(rightContainerLayout, (pageWidth - 170));
//                    middleLayout.setWidthUndefined();
                } else {
                    selectionOverviewBubbleChart.updateSize(rightPanelWidth, Math.min(leftPanelWidth + 20, standeredChartHeight));
                    DiseaseGroupsFiltersContainer.this.setExpandRatio(leftContainerLayout, leftPanelWidth);
                    DiseaseGroupsFiltersContainer.this.setExpandRatio(rightContainerLayout, rightPanelWidth);
//                    middleLayout.setWidth(layoutWidth + "px");
                    resizeLayout(Quant_Central_Manager.getSelectedHeatMapRows().size());

                }
            }
        };

        heatmapFilterComponent.addHideHeatmapBtnListener(hideShowCompTableListener);

        ///init right conatainer layout 
        mainContainerWidth = (pageWidth - 70);
        if (userCustomizedComparison != null) {
            selectionOverviewBubbleChart = new ComparisonsSelectionOverviewBubbleChart(Quant_Central_Manager, CSFPR_Handler, rightPanelWidth, standeredChartHeight, new LinkedHashSet<QuantDiseaseGroupsComparison>(), searchQuantificationProtList, userCustomizedComparison);
        } else {
            selectionOverviewBubbleChart = new ComparisonsSelectionOverviewBubbleChart(Quant_Central_Manager, CSFPR_Handler, rightPanelWidth, standeredChartHeight, new LinkedHashSet<QuantDiseaseGroupsComparison>(), searchQuantificationProtList);
        }
        rightContainerLayout.addComponent(selectionOverviewBubbleChart);

//        final HorizontalLayout topLayout = new HorizontalLayout();
//        topLayout.setWidthUndefined();
//        topLeftLayout = new HorizontalLayout();
//        topLeftLayout.setHeight("30px");
//        topLeftLayout.setStyleName(Reindeer.LAYOUT_WHITE);
//        topLayout.addComponent(topLeftLayout);
//        topLayout.setComponentAlignment(topLeftLayout, Alignment.TOP_LEFT);
//        topLeftLayout.setSpacing(true);
//        topLeftLayout.setMargin(new MarginInfo(false, false, false, true));
//        topRightLayout = new HorizontalLayout();
////        topRightLayout.setWidth((pageWidth - heatmapW - 70) + "px");
//        topRightLayout.setStyleName(Reindeer.LAYOUT_WHITE);
//        topLayout.addComponent(topRightLayout);
//        topLayout.setComponentAlignment(topRightLayout, Alignment.TOP_RIGHT);
//        middleLayout.addComponent(selectionOverviewBubbleChart);
//        middleLayout.setComponentAlignment(selectionOverviewBubbleChart, Alignment.TOP_CENTER);
//        topRightLayout.addComponent(selectionOverviewBubbleChart.getBtnsLayout());
//        topRightLayout.setComponentAlignment(selectionOverviewBubbleChart.getBtnsLayout(), Alignment.TOP_RIGHT);
//        System.out.println("at heatmapRatio " + leftPanelExpendingRatio);
        this.setExpandRatio(leftContainerLayout, leftPanelExpendingRatio);
        this.setExpandRatio(rightContainerLayout, rightPanelExpandingRatio);
        Quant_Central_Manager.registerStudySelectionListener(DiseaseGroupsFiltersContainer.this);
        Quant_Central_Manager.registerFilterListener(DiseaseGroupsFiltersContainer.this);
        heatmapFilterComponent.unselectAll();
    }

    /**
     * select all available disease groups comparisons
     */
    public void selectAllComparisons() {
//        if (diseaseGroupsHeatmapFilter.isActiveSelectAll()) {
        heatmapFilterComponent.selectAll();
//        }
    }

    /**
     *
     * @param type
     */
    @Override
    public void selectionChanged(String type) {
        if ((type.equalsIgnoreCase("Comparison_Selection") && Quant_Central_Manager.getSelectedDiseaseGroupsComparisonList().isEmpty()) || type.equalsIgnoreCase("Reset_Disease_Groups_Level") || type.equalsIgnoreCase("Pie_Chart_Selection")) {

            btnsLayout.setVisible(true);
            diseaseCategorySelectLayout.setVisible(true);
            rightBottomBtnLayout.setVisible(true);
            selectionOverviewBubbleChart.updateSize(rightPanelWidth, Math.min(leftPanelWidth + 20, standeredChartHeight));
            DiseaseGroupsFiltersContainer.this.setExpandRatio(leftContainerLayout, leftPanelWidth);
            DiseaseGroupsFiltersContainer.this.setExpandRatio(rightContainerLayout, rightPanelWidth);
//                    middleLayout.setWidth(layoutWidth + "px");
//            middleLayout.setWidth(layoutWidth + "px");
            resizeLayout(Quant_Central_Manager.getSelectedHeatMapColumns().size());

//                resizeLayout(diseaseGroupsListFilter.getDiseaseGroupsSet().size());
//                UI.getCurrent().setScrollTop(10);
        } else if (type.equalsIgnoreCase("HeatMap_Update_level")) {
            selectAllComparisons();

        }
        counterLabel.setValue("( " + Quant_Central_Manager.getCurrentDsNumber() + " / " + Quant_Central_Manager.getTotalDsNumber() + " )");
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

    private void resizeLayout(int diseaseGroupsSize) {
        leftPanelWidth = Math.max((heatmapHeaderCellWidth + 20 + 12 + (heatmapCellWidth * diseaseGroupsSize)), 710);
        leftContainerLayout.setWidth((leftPanelWidth) + "px");
        rightContainerLayout.setWidth((pageWidth - leftPanelWidth - 70) + "px");
        rightPanelWidth = (pageWidth - leftPanelWidth - 100);
        leftPanelExpendingRatio = (float) leftPanelWidth / (float) (pageWidth + 70);
        rightPanelExpandingRatio = Math.min((float) (rightPanelWidth) / (float) (pageWidth - 70), (0.9f - leftPanelExpendingRatio));
        int heatmapH = Math.max((heatmapHeaderCellWidth + 20 + 12 + (heatmapCellWidth * diseaseGroupsSize)), 500) + 20;
        if (heatmapFilterComponent != null) {
            heatmapFilterComponent.setWidth(leftPanelWidth + "px");
            heatmapFilterComponent.setHeight(heatmapH + "px");
            btnsLayout.setWidth(leftPanelWidth + "px");
        }

    }
//    public void popupSortAndSelectPanel(){  
//        tips.showTip();
//    }

}
