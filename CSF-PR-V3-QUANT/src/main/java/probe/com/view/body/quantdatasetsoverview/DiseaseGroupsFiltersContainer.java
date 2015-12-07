package probe.com.view.body.quantdatasetsoverview;

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
import probe.com.selectionmanager.StudiesSelectionManager;
import probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.DiseaseGroupsListFilter;

import probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.HeatMapFilter;
import probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.ComparisonsSelectionOverviewBubbleChart;
import probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.StudiesTablePopupBtn;
import probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.interactivepiechartfilters.StudiesPieChartFiltersContainerLayout;

/**
 * This class represents the top filters layout for exploring datasets tab the
 * class include the the patients group comparison lists, filtering heat-map and
 * pie chart filters
 *
 * @author Yehia Farag
 */
public class DiseaseGroupsFiltersContainer extends GridLayout implements CSFFilter {

    private final DiseaseGroupsListFilter diseaseGroupsListFilter;
    private final HeatMapFilter diseaseGroupsHeatmapFilter;

    private final HorizontalLayout btnsLayout;

    private int heatmapW;
    private int initLayoutWidth;//, heatmapW;
    private float initialLayoutRatio;
    private float heatmapRatio;
    private int pageWidth;
    private final VerticalLayout pieChartFiltersBtnLayout;
    private final int heatmapCellWidth = 45;
    private final HorizontalLayout topLeftLayout;
    private final HorizontalLayout topRightLayout;

    /**
     *
     * @param Quant_Central_Manager
     * @param CSFPR_Handler
     * @param searchQuantificationProtList
     */
    public DiseaseGroupsFiltersContainer(final QuantCentralManager Quant_Central_Manager, final CSFPRHandler CSFPR_Handler, List<QuantProtein> searchQuantificationProtList) {
        pageWidth = Page.getCurrent().getWebBrowser().getScreenWidth();
        this.setWidth(pageWidth + "px");
        this.setHeightUndefined();
        this.setSpacing(true);
        this.setColumns(1);
        this.setMargin(false);
        this.setRows(4);

        StudiesPieChartFiltersContainerLayout pieChartFiltersLayout = new StudiesPieChartFiltersContainerLayout(Quant_Central_Manager, CSFPR_Handler);
        pieChartFiltersBtnLayout = new VerticalLayout();
        pieChartFiltersBtnLayout.addComponent(pieChartFiltersLayout.getPieChartFiltersBtn());

        diseaseGroupsListFilter = new DiseaseGroupsListFilter(Quant_Central_Manager);

        Set<String> diseaseGroupsSet = diseaseGroupsListFilter.getDiseaseGroupsSet();
//        heatmapW = Math.max((156 + (heatmapCellWidth * diseaseGroupsSet.size())), 700);

        final HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidthUndefined();
        this.addComponent(topLayout, 0, 0);
        this.setComponentAlignment(topLayout, Alignment.TOP_LEFT);

        topLeftLayout = new HorizontalLayout();
//        topLeftLayout.setWidth((heatmapW) + "px");
        topLeftLayout.setHeight("30px");
        topLeftLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        topLayout.addComponent(topLeftLayout);
        topLeftLayout.setSpacing(true);
        topLeftLayout.setMargin(new MarginInfo(false, false, false, true));
        topLayout.setComponentAlignment(topLeftLayout, Alignment.TOP_LEFT);

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
            diseaseTypeSelectionList.setItemCaption(i++, disease);

        }

        final HorizontalLayout cont = new HorizontalLayout();
        cont.setWidthUndefined();
        cont.setSpacing(true);

        topLeftLayout.addComponent(cont);
        topLeftLayout.setComponentAlignment(cont, Alignment.TOP_LEFT);

        Label title = new Label("Disease Category");
        title.setStyleName(Reindeer.LABEL_SMALL);
        cont.addComponent(title);

        cont.setComponentAlignment(title, Alignment.MIDDLE_CENTER);
        final Label counterLabel = new Label("( " + Quant_Central_Manager.getCurrentDsNumber() + " / " + Quant_Central_Manager.getTotalDsNumber() + " )");
        diseaseTypeSelectionList.setWidth("150px");
        diseaseTypeSelectionList.setNullSelectionAllowed(false);
        diseaseTypeSelectionList.setValue("Multiple Sclerosis");
        diseaseTypeSelectionList.setImmediate(true);
        cont.addComponent(diseaseTypeSelectionList);
        cont.setComponentAlignment(diseaseTypeSelectionList, Alignment.TOP_LEFT);
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

        final int layoutWidth = (pageWidth - 70);
        cont.addComponent(counterLabel);
        cont.setComponentAlignment(counterLabel, Alignment.MIDDLE_CENTER);

        final HorizontalLayout middleLayout = new HorizontalLayout();
        middleLayout.setWidth(layoutWidth + "px");
        middleLayout.setHeightUndefined();

        this.addComponent(middleLayout, 0, 1);
        this.setComponentAlignment(middleLayout, Alignment.TOP_LEFT);
        this.resizeLayout(diseaseGroupsSet.size());

        int heatmapH = heatmapW + 20;
        diseaseGroupsHeatmapFilter = new HeatMapFilter(Quant_Central_Manager, heatmapW, diseaseGroupsSet, diseaseGroupsSet, diseaseGroupsListFilter.getPatientsGroupArr(), heatmapCellWidth);
        diseaseGroupsHeatmapFilter.setHeight(Math.max(heatmapH, 720) + "px");
        diseaseGroupsHeatmapFilter.setSingleSelection(false);
        middleLayout.addComponent(diseaseGroupsHeatmapFilter);
        middleLayout.setComponentAlignment(diseaseGroupsHeatmapFilter, Alignment.TOP_LEFT);

        final ComparisonsSelectionOverviewBubbleChart selectionOverviewBubbleChart = new ComparisonsSelectionOverviewBubbleChart(Quant_Central_Manager, CSFPR_Handler, initLayoutWidth, heatmapH, new LinkedHashSet<QuantDiseaseGroupsComparison>(), searchQuantificationProtList);
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

        Button diseaseGroupsFilterBtn = diseaseGroupsListFilter.getDiseaseGroupsFilterBtn();
        diseaseGroupsFilterBtn.setHeight("24px");

        leftBottomBtnLayout.addComponent(diseaseGroupsFilterBtn);
        leftBottomBtnLayout.setComponentAlignment(diseaseGroupsFilterBtn, Alignment.MIDDLE_LEFT);

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

        final HorizontalLayout rightBottomBtnLayout = new HorizontalLayout();
        rightBottomBtnLayout.setHeightUndefined();
        rightBottomBtnLayout.setWidthUndefined();
        rightBottomBtnLayout.setSpacing(true);

        StudiesTablePopupBtn showStudiesBtn = new StudiesTablePopupBtn(Quant_Central_Manager);
        rightBottomBtnLayout.addComponent(showStudiesBtn);
        rightBottomBtnLayout.setComponentAlignment(showStudiesBtn, Alignment.MIDDLE_LEFT);

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
                cont.setVisible(!cont.isVisible());

//               
                if (!btnsLayout.isVisible()) {
                    selectionOverviewBubbleChart.updateSize(pageWidth - 250);
                    middleLayout.setExpandRatio(diseaseGroupsHeatmapFilter, 250);
                    middleLayout.setExpandRatio(selectionOverviewBubbleChart, (pageWidth - 250));
                    middleLayout.setWidthUndefined();
                } else {
                    selectionOverviewBubbleChart.updateSize(initLayoutWidth);
                    middleLayout.setExpandRatio(diseaseGroupsHeatmapFilter, heatmapW);
                    middleLayout.setExpandRatio(selectionOverviewBubbleChart, initLayoutWidth);
                    middleLayout.setWidth(layoutWidth + "px");
                    resizeLayout(diseaseGroupsListFilter.getDiseaseGroupsSet().size());

                }
            }
        };

        diseaseGroupsHeatmapFilter.addHideHeatmapBtnListener(hideShowCompTableListener);
        Quant_Central_Manager.registerFilterListener(DiseaseGroupsFiltersContainer.this);

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
        if (type.equalsIgnoreCase("Disease_Category_Selection")) {
//            pieChartFiltersBtnLayout.removeAllComponents();
//            StudiesPieChartFiltersContainerLayout pieChartFiltersLayout = new StudiesPieChartFiltersContainerLayout(centralFiltersSelectionManager, CSFPR_Handler);
////            pieChartFiltersBtnLayout.addComponent(pieChartFiltersLayout.getPieChartFiltersBtn());

        }
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
        heatmapW = Math.max((156 + (heatmapCellWidth * diseaseGroupsSize)), 700);
        topLeftLayout.setWidth((heatmapW) + "px");
        topRightLayout.setWidth((pageWidth - heatmapW - 70) + "px");
        initLayoutWidth = (pageWidth - heatmapW - 300);
        heatmapRatio = (float) heatmapW / (float) (pageWidth);
        initialLayoutRatio = (float) (initLayoutWidth) / (float) (pageWidth);
        int heatmapH = heatmapW + 20;
        if (diseaseGroupsHeatmapFilter != null) {
            diseaseGroupsHeatmapFilter.setWidth(heatmapW + "px");
            diseaseGroupsHeatmapFilter.setHeight(heatmapH + "px");
            btnsLayout.setWidth(heatmapW + "px");
        }

    }

}
