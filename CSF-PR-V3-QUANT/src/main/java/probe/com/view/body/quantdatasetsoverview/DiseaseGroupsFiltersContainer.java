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
public class DiseaseGroupsFiltersContainer extends GridLayout implements CSFFilter {

//    private final DiseaseGroupsListFilter diseaseGroupsListFilter;
    private final HeatMapFilter diseaseGroupsHeatmapFilter;

    private final HorizontalLayout btnsLayout;

    private int heatmapW;
    private int initLayoutWidth;//, heatmapW;
    private float initialLayoutRatio;
    private float heatmapRatio;
    private int pageWidth;
    private final VerticalLayout pieChartFiltersBtnLayout;
    private final int heatmapCellWidth = 30;
    private final int heatmapHeaderCellWidth = 135;
    private final HorizontalLayout topLeftLayout;
    private final HorizontalLayout topRightLayout;
    private final QuantCentralManager Quant_Central_Manager;
    private final ComparisonsSelectionOverviewBubbleChart selectionOverviewBubbleChart;
    private final HorizontalLayout middleLayout;
    private final int layoutWidth;
    private final int standeredChartHeight;
    private final HorizontalLayout diseaseCategorySelectLayout;
    private final HorizontalLayout rightBottomBtnLayout;
    private final PopupReorderGroupsLayout reorderGroups;
    private final Label counterLabel;

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
        this.setColumns(1);
        this.setMargin(false);
        this.setRows(4);
        this.Quant_Central_Manager = Quant_Central_Manager;

        StudiesPieChartFiltersContainerLayout pieChartFiltersLayout = new StudiesPieChartFiltersContainerLayout(Quant_Central_Manager, CSFPR_Handler);
        pieChartFiltersBtnLayout = new VerticalLayout();
        pieChartFiltersBtnLayout.setHeight("24px");
        pieChartFiltersBtnLayout.addComponent(pieChartFiltersLayout.getPieChartFiltersBtn());

//        diseaseGroupsListFilter = new DiseaseGroupsListFilter(Quant_Central_Manager);
        final LinkedHashSet<String> diseaseGroupsRowSet = Quant_Central_Manager.getSelectedHeatMapRows(); //diseaseGroupsListFilter.getDiseaseGroupsSet();
        final LinkedHashSet<String> diseaseGroupsColSet = Quant_Central_Manager.getSelectedHeatMapColumns(); //diseaseGroupsListFilter.getDiseaseGroupsSet();
//        heatmapW = Math.max((156 + (heatmapCellWidth * diseaseGroupsSet.size())), 700);

        final HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidthUndefined();
        this.addComponent(topLayout, 0, 0);
        this.setComponentAlignment(topLayout, Alignment.TOP_LEFT);

        topLeftLayout = new HorizontalLayout();
        topLeftLayout.setHeight("30px");
        topLeftLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        topLayout.addComponent(topLeftLayout);
        topLayout.setComponentAlignment(topLeftLayout, Alignment.TOP_LEFT);
        topLeftLayout.setSpacing(true);
        topLeftLayout.setMargin(new MarginInfo(false, false, false, true));

        topRightLayout = new HorizontalLayout();
//        topRightLayout.setWidth((pageWidth - heatmapW - 70) + "px");
        topRightLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        topLayout.addComponent(topRightLayout);
        topLayout.setComponentAlignment(topRightLayout, Alignment.TOP_RIGHT);

        Set<String> diseaseSet = Quant_Central_Manager.getDiseaseCategorySet();
        NativeSelect diseaseTypeSelectionList = new NativeSelect();
        diseaseTypeSelectionList.setDescription("Select disease category");

        int i = 0;
        for (String disease : diseaseSet) {
            diseaseTypeSelectionList.addItem(disease);
            diseaseTypeSelectionList.setItemCaption(disease, (disease));

        }

        diseaseCategorySelectLayout = new HorizontalLayout();
        diseaseCategorySelectLayout.setWidthUndefined();
        diseaseCategorySelectLayout.setSpacing(true);

        topLeftLayout.addComponent(diseaseCategorySelectLayout);
        topLeftLayout.setComponentAlignment(diseaseCategorySelectLayout, Alignment.TOP_LEFT);

        Label title = new Label("Disease Category");
        title.setStyleName(Reindeer.LABEL_SMALL);
        diseaseCategorySelectLayout.addComponent(title);

        diseaseCategorySelectLayout.setComponentAlignment(title, Alignment.MIDDLE_CENTER);
        counterLabel = new Label("( " + Quant_Central_Manager.getCurrentDsNumber() + " / " + Quant_Central_Manager.getTotalDsNumber() + " )");
        diseaseTypeSelectionList.setWidth("200px");
        diseaseTypeSelectionList.setNullSelectionAllowed(false);
        diseaseTypeSelectionList.setValue("Multiple Sclerosis");
        diseaseTypeSelectionList.setImmediate(true);
        diseaseCategorySelectLayout.addComponent(diseaseTypeSelectionList);
        diseaseCategorySelectLayout.setComponentAlignment(diseaseTypeSelectionList, Alignment.TOP_LEFT);
        diseaseTypeSelectionList.setStyleName("diseaseselectionlist");
        diseaseTypeSelectionList.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Quant_Central_Manager.changeDiseaseCategory(event.getProperty().getValue().toString());
                Quant_Central_Manager.resetFiltersListener();
                pieChartFiltersBtnLayout.removeAllComponents();
                StudiesPieChartFiltersContainerLayout pieChartFiltersLayout = new StudiesPieChartFiltersContainerLayout(Quant_Central_Manager, CSFPR_Handler);
                pieChartFiltersBtnLayout.addComponent(pieChartFiltersLayout.getPieChartFiltersBtn());
                counterLabel.setValue("( " + Quant_Central_Manager.getCurrentDsNumber() + " / " + Quant_Central_Manager.getTotalDsNumber() + " )");

            }
        });

        layoutWidth = (pageWidth - 70);
        diseaseCategorySelectLayout.addComponent(counterLabel);
        diseaseCategorySelectLayout.setComponentAlignment(counterLabel, Alignment.MIDDLE_CENTER);

        middleLayout = new HorizontalLayout();
        middleLayout.setWidth(layoutWidth + "px");
        middleLayout.setHeightUndefined();

        this.addComponent(middleLayout, 0, 1);
        this.setComponentAlignment(middleLayout, Alignment.TOP_LEFT);
        this.resizeLayout(Math.max(diseaseGroupsColSet.size(), diseaseGroupsRowSet.size()));

        int heatmapH = heatmapW + 10;
        standeredChartHeight = heatmapH;

//        System.out.println("at error "+diseaseGroupsSet.size()+"    "+ Quant_Central_Manager.getDiseaseGroupsArr().length+ );
        diseaseGroupsHeatmapFilter = new HeatMapFilter(Quant_Central_Manager, heatmapW, diseaseGroupsRowSet, diseaseGroupsColSet, Quant_Central_Manager.getDiseaseGroupsArray(), heatmapCellWidth, heatmapHeaderCellWidth, CSFPR_Handler.getDiseaseFullNameMap());
        diseaseGroupsHeatmapFilter.setHeight(Math.max(heatmapH, 700) + "px");
        diseaseGroupsHeatmapFilter.setSingleSelection(false);
        middleLayout.addComponent(diseaseGroupsHeatmapFilter);
        middleLayout.setComponentAlignment(diseaseGroupsHeatmapFilter, Alignment.TOP_LEFT);

        if (userCustomizedComparison != null) {
            selectionOverviewBubbleChart = new ComparisonsSelectionOverviewBubbleChart(Quant_Central_Manager, CSFPR_Handler, initLayoutWidth, standeredChartHeight, new LinkedHashSet<QuantDiseaseGroupsComparison>(), searchQuantificationProtList, userCustomizedComparison);
        } else {
            selectionOverviewBubbleChart = new ComparisonsSelectionOverviewBubbleChart(Quant_Central_Manager, CSFPR_Handler, initLayoutWidth, standeredChartHeight, new LinkedHashSet<QuantDiseaseGroupsComparison>(), searchQuantificationProtList);
        }

        middleLayout.addComponent(selectionOverviewBubbleChart);
        middleLayout.setComponentAlignment(selectionOverviewBubbleChart, Alignment.TOP_LEFT);

        topRightLayout.addComponent(selectionOverviewBubbleChart.getBtnsLayout());
        topRightLayout.setComponentAlignment(selectionOverviewBubbleChart.getBtnsLayout(), Alignment.TOP_RIGHT);
        middleLayout.setExpandRatio(diseaseGroupsHeatmapFilter, heatmapRatio);
        middleLayout.setExpandRatio(selectionOverviewBubbleChart, initialLayoutRatio);

        btnsLayout = new HorizontalLayout();
        btnsLayout.setWidth(heatmapW + "px");
        btnsLayout.setHeight("24px");

        btnsLayout.setSpacing(true);

        HorizontalLayout leftBottomBtnLayout = new HorizontalLayout();
        leftBottomBtnLayout.setWidthUndefined();
        leftBottomBtnLayout.setSpacing(true);
        leftBottomBtnLayout.setHeightUndefined();
        btnsLayout.addComponent(leftBottomBtnLayout);
        btnsLayout.setComponentAlignment(leftBottomBtnLayout, Alignment.MIDDLE_LEFT);

        Label filterTitle = new Label("Filters:");
        filterTitle.setHeight("24px");
        filterTitle.setDescription("Available filters");
        filterTitle.setStyleName("filtercaption");
        leftBottomBtnLayout.addComponent(filterTitle);
        filterTitle.setWidth("50px");

        leftBottomBtnLayout.addComponent(pieChartFiltersBtnLayout);
        leftBottomBtnLayout.setComponentAlignment(pieChartFiltersBtnLayout, Alignment.MIDDLE_LEFT);

//        Button diseaseGroupsFilterBtn = diseaseGroupsListFilter.getDiseaseGroupsFilterBtn();
//        diseaseGroupsFilterBtn.setHeight("24px");
//        leftBottomBtnLayout.addComponent(diseaseGroupsFilterBtn);
//        leftBottomBtnLayout.setComponentAlignment(diseaseGroupsFilterBtn, Alignment.MIDDLE_LEFT);
        reorderGroups = new PopupReorderGroupsLayout(Quant_Central_Manager);
        leftBottomBtnLayout.addComponent(reorderGroups);
        leftBottomBtnLayout.setComponentAlignment(reorderGroups, Alignment.TOP_LEFT);

        Button clearFilterBtn = new Button("Reset");
        clearFilterBtn.setHeight("24px");
        clearFilterBtn.setStyleName(Reindeer.BUTTON_LINK);
        leftBottomBtnLayout.addComponent(clearFilterBtn);
        leftBottomBtnLayout.setComponentAlignment(clearFilterBtn, Alignment.MIDDLE_LEFT);
        clearFilterBtn.setDescription("Reset all applied filters");

        clearFilterBtn.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Quant_Central_Manager.resetFiltersListener();
            }

        });
        final OptionGroup noSerumOption = new OptionGroup();
        leftBottomBtnLayout.addComponent(noSerumOption);
        leftBottomBtnLayout.setComponentAlignment(noSerumOption, Alignment.MIDDLE_LEFT);
        noSerumOption.setWidth("80px");
        noSerumOption.setHeight("24px");
        noSerumOption.setNullSelectionAllowed(true); // user can not 'unselect'
        noSerumOption.setMultiSelect(true);

        noSerumOption.addItem("Serum Studies");
        noSerumOption.addStyleName("horizontal");
        noSerumOption.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Quant_Central_Manager.setNoSerum(!noSerumOption.getValue().toString().equalsIgnoreCase("[Serum Studies]"));
                Quant_Central_Manager.resetFiltersListener();
                pieChartFiltersBtnLayout.removeAllComponents();
                StudiesPieChartFiltersContainerLayout pieChartFiltersLayout = new StudiesPieChartFiltersContainerLayout(Quant_Central_Manager, CSFPR_Handler);
                pieChartFiltersBtnLayout.addComponent(pieChartFiltersLayout.getPieChartFiltersBtn());
                counterLabel.setValue("( " + Quant_Central_Manager.getCurrentDsNumber() + " / " + Quant_Central_Manager.getTotalDsNumber() + " )");

            }

        });

        rightBottomBtnLayout = new HorizontalLayout();
        rightBottomBtnLayout.setHeightUndefined();
        rightBottomBtnLayout.setWidthUndefined();
        rightBottomBtnLayout.setSpacing(true);

        StudiesInformationPopupBtn showStudiesBtn = new StudiesInformationPopupBtn(Quant_Central_Manager);
        rightBottomBtnLayout.addComponent(showStudiesBtn);
        rightBottomBtnLayout.setComponentAlignment(showStudiesBtn, Alignment.MIDDLE_LEFT);

        Button exportTableBtn = new Button("");
        exportTableBtn.setHeight("24px");
        exportTableBtn.setWidth("24px");
        exportTableBtn.setPrimaryStyleName("exportxslbtn");
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
                diseaseGroupsHeatmapFilter.selectAll();
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
                diseaseGroupsHeatmapFilter.unselectAll();
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
                    diseaseGroupsHeatmapFilter.setSingleSelection(true);
                    selectMultiBtn.setStyleName("selectmultibtn");

                } else {
                    diseaseGroupsHeatmapFilter.setSingleSelection(false);
                    selectMultiBtn.setStyleName("selectmultiselectedbtn");

                }
            }
        });

        btnsLayout.addComponent(rightBottomBtnLayout);
        btnsLayout.setComponentAlignment(rightBottomBtnLayout, Alignment.TOP_RIGHT);

        this.addComponent(btnsLayout, 0, 2);

        LayoutEvents.LayoutClickListener hideShowCompTableListener = new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                diseaseGroupsHeatmapFilter.setVisible(!diseaseGroupsHeatmapFilter.isVisibleComponent());
                rightBottomBtnLayout.setVisible(!rightBottomBtnLayout.isVisible());
                btnsLayout.setVisible(!btnsLayout.isVisible());
                diseaseCategorySelectLayout.setVisible(!diseaseCategorySelectLayout.isVisible());

//               
                if (!btnsLayout.isVisible()) {
                    selectionOverviewBubbleChart.updateSize(pageWidth - 250, Math.min(heatmapW + 20, standeredChartHeight));
                    middleLayout.setExpandRatio(diseaseGroupsHeatmapFilter, 250);
                    middleLayout.setExpandRatio(selectionOverviewBubbleChart, (pageWidth - 250));
                    middleLayout.setWidthUndefined();
                } else {
                    selectionOverviewBubbleChart.updateSize(initLayoutWidth, Math.min(heatmapW + 20, standeredChartHeight));
                    middleLayout.setExpandRatio(diseaseGroupsHeatmapFilter, heatmapW - 70);
                    middleLayout.setExpandRatio(selectionOverviewBubbleChart, initLayoutWidth + 70);
                    middleLayout.setWidth(layoutWidth + "px");
                    resizeLayout(Quant_Central_Manager.getSelectedHeatMapRows().size());

                }
            }
        };
        int y = Page.getCurrent().getBrowserWindowHeight() - Math.max(heatmapH, 700) - 200;
        Tips tips = CSFPR_Handler.getTipsGenerator().generateTip("Remeber you can sort and select the disease groups using <u>Sort and Select</u> feature", 180, y);
        leftBottomBtnLayout.addComponent(tips);
        leftBottomBtnLayout.setComponentAlignment(tips, Alignment.TOP_LEFT);

        diseaseGroupsHeatmapFilter.addHideHeatmapBtnListener(hideShowCompTableListener);
        Quant_Central_Manager.registerStudySelectionListener(DiseaseGroupsFiltersContainer.this);
        Quant_Central_Manager.registerFilterListener(DiseaseGroupsFiltersContainer.this);
        diseaseGroupsHeatmapFilter.unselectAll();
    }

    /**
     * select all available disease groups comparisons
     */
    public void selectAllComparisons() {
//        if (diseaseGroupsHeatmapFilter.isActiveSelectAll()) {
        diseaseGroupsHeatmapFilter.selectAll();
//        }
    }

    /**
     *
     * @param type
     */
    @Override
    public void selectionChanged(String type) {
        if ((type.equalsIgnoreCase("Comparison_Selection") && Quant_Central_Manager.getSelectedDiseaseGroupsComparisonList().isEmpty()) || type.equalsIgnoreCase("Reset_Disease_Groups_Level") || type.equalsIgnoreCase("Pie_Chart_Selection")) {
            selectionOverviewBubbleChart.updateSize(initLayoutWidth, Math.min(heatmapW + 20, standeredChartHeight));
            btnsLayout.setVisible(true);
            diseaseCategorySelectLayout.setVisible(true);
            rightBottomBtnLayout.setVisible(true);
            middleLayout.setExpandRatio(diseaseGroupsHeatmapFilter, heatmapW - 70);
            middleLayout.setExpandRatio(selectionOverviewBubbleChart, initLayoutWidth + 70);
            middleLayout.setWidth(layoutWidth + "px");
            resizeLayout(Quant_Central_Manager.getSelectedHeatMapRows().size());

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
        heatmapW = Math.max((heatmapHeaderCellWidth + 20 + 12 + (heatmapCellWidth * diseaseGroupsSize)), 700);
        topLeftLayout.setWidth((heatmapW) + "px");
        topRightLayout.setWidth((pageWidth - heatmapW - 70) + "px");
        initLayoutWidth = (pageWidth - heatmapW - 300);
        heatmapRatio = (float) heatmapW / (float) (pageWidth + 70);
        initialLayoutRatio = (float) (initLayoutWidth) / (float) (pageWidth - 70);
        int heatmapH = heatmapW + 20;
        if (diseaseGroupsHeatmapFilter != null) {
            diseaseGroupsHeatmapFilter.setWidth(heatmapW + "px");
            diseaseGroupsHeatmapFilter.setHeight(heatmapH + "px");
            btnsLayout.setWidth(heatmapW + "px");
        }

    }
//    public void popupSortAndSelectPanel(){  
//        tips.showTip();
//    }

}
