package probe.com.view.body.quantdatasetsoverview;

import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
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
import probe.com.view.core.NotificationComponent;

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

    private int leftPanelWidth, leftPanelHeight, heatmapH;
    private int rightPanelWidth;//, heatmapW;
    private float rightPanelExpandingRatio;
    private float leftPanelExpendingRatio;
    private int pageWidth;
    private final VerticalLayout pieChartFiltersBtnWrapper;
    private final int heatmapCellWidth = 30;
    private final int heatmapHeaderCellWidth = 135;
    private final QuantCentralManager Quant_Central_Manager;
    private final ComparisonsSelectionOverviewBubbleChart selectionOverviewBubbleChart;
    private final int rightPanelHeight;
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
        pageWidth = Page.getCurrent().getBrowserWindowWidth();
        this.setHeightUndefined();
        this.setSpacing(true);
        this.setMargin(false);
        this.Quant_Central_Manager = Quant_Central_Manager;
        final LinkedHashSet<String> diseaseGroupsRowSet = Quant_Central_Manager.getSelectedHeatMapRows(); //diseaseGroupsListFilter.getDiseaseGroupsSet();
        final LinkedHashSet<String> diseaseGroupsColSet = Quant_Central_Manager.getSelectedHeatMapColumns(); //diseaseGroupsListFilter.getDiseaseGroupsSet();

        leftContainerLayout = new VerticalLayout();
        rightContainerLayout = new VerticalLayout();
        rightContainerLayout.setVisible(false);

        this.addComponent(leftContainerLayout);
        this.addComponent(rightContainerLayout);

        //init left panel 
        leftContainerLayout.setSpacing(true);

        final VerticalLayout diseaseCategorySelectLayoutWrapper = new VerticalLayout();
        leftContainerLayout.addComponent(diseaseCategorySelectLayoutWrapper);
        leftContainerLayout.setComponentAlignment(diseaseCategorySelectLayoutWrapper, Alignment.TOP_LEFT);
        diseaseCategorySelectLayoutWrapper.setWidth("100%");
        diseaseCategorySelectLayoutWrapper.setHeight("110px");
        diseaseCategorySelectLayoutWrapper.setStyleName(Reindeer.LAYOUT_WHITE);

        diseaseCategorySelectLayout = new HorizontalLayout();
        diseaseCategorySelectLayoutWrapper.addComponent(diseaseCategorySelectLayout);
        diseaseCategorySelectLayout.setWidthUndefined();
        diseaseCategorySelectLayout.setSpacing(true);
        diseaseCategorySelectLayout.setMargin(new MarginInfo(false, false, false, false));

        Label title = new Label("Disease Category");
        title.setStyleName(Reindeer.LABEL_SMALL);
        diseaseCategorySelectLayout.addComponent(title);
        diseaseCategorySelectLayout.setComponentAlignment(title, Alignment.MIDDLE_CENTER);

        Set<String> diseaseSet = Quant_Central_Manager.getDiseaseCategorySet();

        final VerticalLayout listLayoutWrapper = new VerticalLayout();
        diseaseCategorySelectLayout.addComponent(listLayoutWrapper);
        diseaseCategorySelectLayout.setComponentAlignment(listLayoutWrapper, Alignment.TOP_LEFT);

        final NativeSelect diseaseTypeSelectionList = new NativeSelect();
        diseaseTypeSelectionList.setVisible(false);
        diseaseTypeSelectionList.setDescription("Select disease category");

        for (String disease : diseaseSet) {
            diseaseTypeSelectionList.addItem(disease);
            diseaseTypeSelectionList.setItemCaption(disease, (disease));
            diseaseTypeSelectionList.setItemIcon(disease, new ThemeResource("img/ms_icon.png"));

        }

        diseaseTypeSelectionList.setWidth("200px");
        diseaseTypeSelectionList.setNullSelectionAllowed(false);
        diseaseTypeSelectionList.setValue(Quant_Central_Manager.getInUseDiseaseName());
        diseaseTypeSelectionList.setImmediate(true);
        listLayoutWrapper.addComponent(diseaseTypeSelectionList);

        diseaseTypeSelectionList.setStyleName("diseaseselectionlist");
        diseaseTypeSelectionList.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Quant_Central_Manager.changeDiseaseCategory(event.getProperty().getValue().toString());
                Quant_Central_Manager.resetFiltersListener();
                pieChartFiltersBtnWrapper.removeAllComponents();
                StudiesPieChartFiltersContainerLayout pieChartFiltersLayout = new StudiesPieChartFiltersContainerLayout(Quant_Central_Manager, CSFPR_Handler);
                pieChartFiltersBtnWrapper.addComponent(pieChartFiltersLayout.getPieChartFiltersBtn());
                counterLabel.setValue("<center style='text-align: center;'>( " + Quant_Central_Manager.getCurrentDsNumber() + " / " + Quant_Central_Manager.getTotalDsNumber() + " )</center>");
                counterLabel.setDescription("#Datasets: " + Quant_Central_Manager.getCurrentDsNumber() + "<br/>#Total datasets: " + Quant_Central_Manager.getTotalDsNumber());

            }
        });

//        final ListSelect initialDiseaseTypeSelectionList = new ListSelect(null);
//        initialDiseaseTypeSelectionList.setDescription("Select disease category");
//        initialDiseaseTypeSelectionList.setStyleName("initialquantselectionlist");
//        for (String disease : diseaseSet) {
//            initialDiseaseTypeSelectionList.addItem(disease);           
//            initialDiseaseTypeSelectionList.setItemIcon(disease, new ExternalResource("http://www.rgbstock.com/cache1nhWh4/users/x/xy/xymonau/300/2dyVZtK.jpg"));
//            initialDiseaseTypeSelectionList.setItemCaption(disease, (disease));
//
//        }
//        initialDiseaseTypeSelectionList.setCaptionAsHtml(true);
//        initialDiseaseTypeSelectionList.setWidth("200px");
//        initialDiseaseTypeSelectionList.setHeight("100px");
//        initialDiseaseTypeSelectionList.setNullSelectionAllowed(false);
//        initialDiseaseTypeSelectionList.setImmediate(true);
        final HorizontalLayout filterLabelBtnWrpper = new HorizontalLayout();
        filterLabelBtnWrpper.setVisible(false);
        leftContainerLayout.setHeight("100px");

        final VerticalLayout filtersContainerLayout = new VerticalLayout();

//        initialDiseaseTypeSelectionList.addValueChangeListener(new Property.ValueChangeListener() {
//            @Override
//            public void valueChange(Property.ValueChangeEvent event) {
//                initialDiseaseTypeSelectionList.setVisible(false);
//                diseaseTypeSelectionList.setVisible(true);
//                counterLabel.setVisible(true);
//                rightContainerLayout.setVisible(true);
//                btnsLayout.setVisible(true);
//                heatmapFilterComponent.setVisible(true);
//                diseaseCategorySelectLayoutWrapper.setHeight("23px");
//                filterLabelBtnWrpper.setVisible(true);
//                heatmapFilterComponent.getHideCompBtn().setVisible(true);
//                filtersContainerLayout.setVisible(true);
//                leftContainerLayout.setHeightUndefined();
//                heatmapFilterComponent.showCompBtn(true);
//                if (!event.getProperty().getValue().toString().equalsIgnoreCase("Multiple Sclerosis")) {
//                    diseaseTypeSelectionList.select(event.getProperty().getValue().toString());
//                }
//            }
//        });
        LayoutEvents.LayoutClickListener listener = new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                listLayoutWrapper.getComponent(1).setVisible(false);
                diseaseTypeSelectionList.setVisible(true);
                counterLabel.setVisible(true);
                rightContainerLayout.setVisible(true);
                btnsLayout.setVisible(true);
                heatmapFilterComponent.setVisible(true);
                diseaseCategorySelectLayoutWrapper.setHeight("23px");
                filterLabelBtnWrpper.setVisible(true);
                heatmapFilterComponent.getHideCompBtn().setVisible(true);
                filtersContainerLayout.setVisible(true);
                leftContainerLayout.setHeightUndefined();
                heatmapFilterComponent.showCompBtn(true);
                String data = ((HorizontalLayout) event.getComponent()).getData().toString();
                if (!data.equalsIgnoreCase("Multiple Sclerosis")) {
                    diseaseTypeSelectionList.select(data);
                }
                Quant_Central_Manager.showNotifications("Quantitative Datasets");

            }
        };
        VerticalLayout selectLayout = initDiseaseCatLayout(diseaseSet, listener);
        listLayoutWrapper.addComponent(selectLayout);
//        listLayoutWrapper.addComponent(initialDiseaseTypeSelectionList);

        counterLabel = new Label("<center style='text-align: center;'>( " + Quant_Central_Manager.getCurrentDsNumber() + " / " + Quant_Central_Manager.getTotalDsNumber() + " )</center>");
        counterLabel.setVisible(false);
        counterLabel.setDescription("#Datasets: " + Quant_Central_Manager.getCurrentDsNumber() + "<br/>#Total datasets: " + Quant_Central_Manager.getTotalDsNumber());
        diseaseCategorySelectLayout.addComponent(counterLabel);
        diseaseCategorySelectLayout.setComponentAlignment(counterLabel, Alignment.MIDDLE_CENTER);
        counterLabel.setWidth("60px");
        counterLabel.setContentMode(ContentMode.HTML);

        diseaseCategorySelectLayout.addComponent(filterLabelBtnWrpper);
        diseaseCategorySelectLayout.setComponentAlignment(filterLabelBtnWrpper, Alignment.MIDDLE_RIGHT);

        final Label filterLabelBtn = new Label("Filters");
        filterLabelBtn.setHeight("22px");
        filterLabelBtn.setWidth("20px");
        filterLabelBtn.setDescription("Show filters");
        filterLabelBtn.setStyleName("showfilterbtnlabel");
        filterLabelBtn.setContentMode(ContentMode.HTML);
        filterLabelBtnWrpper.addComponent(filterLabelBtn);

       NotificationComponent notification = Quant_Central_Manager.insertNoftfication("Quantitative Datasets", "Remember you can sort and select disease groups using (Reorder and select disease groups filter) <img src='VAADIN/themes/dario-theme/img/sort-select.png' height='25px' width='25' alt='Reorder and select' Align='center'/> ",470 , 115, "sortandselectnotification");
       filterLabelBtnWrpper.addComponent(notification);
        HorizontalLayout popupBtnsLayout = new HorizontalLayout();
        popupBtnsLayout.setWidth("446px");
        popupBtnsLayout.setHeight("30px");
        popupBtnsLayout.setSpacing(true);
        popupBtnsLayout.setStyleName("filtercontainer");

        filtersContainerLayout.addComponent(popupBtnsLayout);
        filtersContainerLayout.setHeight("30px");
        filtersContainerLayout.setStyleName("filtercontainer2");
        filtersContainerLayout.setVisible(false);

        leftContainerLayout.addComponent(filtersContainerLayout);
        leftContainerLayout.setComponentAlignment(filtersContainerLayout, Alignment.MIDDLE_LEFT);

        filtersContainerLayout.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                if (event.getClickedComponent() == null || !event.getClickedComponent().getStyleName().equalsIgnoreCase("filtercontainer")) {
                    return;
                }

                filtersContainerLayout.setHeight("0px");
                filterLabelBtn.setDescription("Show filters");
                filterLabelBtn.setStyleName("showfilterbtnlabel");
            }
        });

        filterLabelBtnWrpper.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                if (filterLabelBtn.getDescription().equalsIgnoreCase("Show filters")) {
                    filtersContainerLayout.setHeight("30px");
                    filterLabelBtn.setDescription("Hide filters");
                    filterLabelBtn.setStyleName("hidefilterbtnlabel");
                } else {
                    filtersContainerLayout.setHeight("0px");
                    filterLabelBtn.setDescription("Show filters");
                    filterLabelBtn.setStyleName("showfilterbtnlabel");

                }

            }
        });

        StudiesPieChartFiltersContainerLayout pieChartFiltersLayout = new StudiesPieChartFiltersContainerLayout(Quant_Central_Manager, CSFPR_Handler);
        pieChartFiltersBtnWrapper = new VerticalLayout();
        pieChartFiltersBtnWrapper.addComponent(pieChartFiltersLayout.getPieChartFiltersBtn());
        popupBtnsLayout.addComponent(pieChartFiltersBtnWrapper);
        popupBtnsLayout.setComponentAlignment(pieChartFiltersBtnWrapper, Alignment.MIDDLE_LEFT);

        reorderGroups = new PopupReorderGroupsLayout(Quant_Central_Manager);
        popupBtnsLayout.addComponent(reorderGroups);
        popupBtnsLayout.setComponentAlignment(reorderGroups, Alignment.TOP_LEFT);

        PopupRecombineDiseaseGroups recombineGroups = new PopupRecombineDiseaseGroups(Quant_Central_Manager);
        popupBtnsLayout.addComponent(recombineGroups);
        popupBtnsLayout.setComponentAlignment(recombineGroups, Alignment.TOP_LEFT);

//        final OptionGroup noSerumOption = new OptionGroup();
//        noSerumOption.setDescription("Include serum studies");
//        popupBtnsLayout.addComponent(noSerumOption);
//        popupBtnsLayout.setComponentAlignment(noSerumOption, Alignment.MIDDLE_LEFT);
//        noSerumOption.setWidth("25px");
//        noSerumOption.setHeight("24px");
//        noSerumOption.setNullSelectionAllowed(true); // user can not 'unselect'
//        noSerumOption.setMultiSelect(true);
//        noSerumOption.addItem("Serum Studies");
//        noSerumOption.addStyleName("horizontal");
//        noSerumOption.addStyleName("heatmapbtns");
//        noSerumOption.setItemCaption("Serum Studies", "Se");
//        noSerumOption.addValueChangeListener(new Property.ValueChangeListener() {
//            @Override
//            public void valueChange(Property.ValueChangeEvent event) {
//                Quant_Central_Manager.setHideSerum(!noSerumOption.getValue().toString().equalsIgnoreCase("[Serum Studies]"));
//                Quant_Central_Manager.resetFiltersListener();
//                pieChartFiltersBtnWrapper.removeAllComponents();
//                StudiesPieChartFiltersContainerLayout pieChartFiltersLayout = new StudiesPieChartFiltersContainerLayout(Quant_Central_Manager, CSFPR_Handler);
//                pieChartFiltersBtnWrapper.addComponent(pieChartFiltersLayout.getPieChartFiltersBtn());
//                counterLabel.setValue("( " + Quant_Central_Manager.getCurrentDsNumber() + " / " + Quant_Central_Manager.getTotalDsNumber() + " )");
//
//            }
//
//        });
        final VerticalLayout noSerumOption = new VerticalLayout();
        noSerumOption.setDescription("Include serum datasets");
        popupBtnsLayout.addComponent(noSerumOption);
        popupBtnsLayout.setComponentAlignment(noSerumOption, Alignment.MIDDLE_LEFT);
        noSerumOption.setWidth("40px");
        noSerumOption.setHeight("24px");
        noSerumOption.setStyleName("enableserumunselected");
        final VerticalLayout noCSFOption = new VerticalLayout();
        noSerumOption.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                if (noCSFOption.getStyleName().equalsIgnoreCase("enablecsfunselected") && noSerumOption.getStyleName().equalsIgnoreCase("enableserumselected")) {
                    Notification.show("You can not hide both CSF and serum datasets", Notification.Type.TRAY_NOTIFICATION);
                    return;

                }

                if (noSerumOption.getStyleName().equalsIgnoreCase("enableserumunselected")) {
                    boolean success = Quant_Central_Manager.setHideSerum(false);
                    if (success) {
                        noSerumOption.setStyleName("enableserumselected");
                    } else {
                        Notification.show("No serum datasets available for the selected disease", Notification.Type.TRAY_NOTIFICATION);
                        return;
                    }
                } else {
                    boolean success = Quant_Central_Manager.setHideSerum(true);
                    if (success) {
                        noSerumOption.setStyleName("enableserumunselected");

                    } else {
                        Notification.show("No serum datasets available for the selected disease", Notification.Type.TRAY_NOTIFICATION);
                        return;
                    }

                }
                Quant_Central_Manager.resetFiltersListener();
                pieChartFiltersBtnWrapper.removeAllComponents();
                StudiesPieChartFiltersContainerLayout pieChartFiltersLayout = new StudiesPieChartFiltersContainerLayout(Quant_Central_Manager, CSFPR_Handler);
                pieChartFiltersBtnWrapper.addComponent(pieChartFiltersLayout.getPieChartFiltersBtn());
                counterLabel.setValue("<center style='text-align: center;'>( " + Quant_Central_Manager.getCurrentDsNumber() + " / " + Quant_Central_Manager.getTotalDsNumber() + " )</center>");
                counterLabel.setDescription("#Datasets: " + Quant_Central_Manager.getCurrentDsNumber() + "<br/>#Total datasets: " + Quant_Central_Manager.getTotalDsNumber());
            }
        });

        noCSFOption.setDescription("Include CSF datasets");
        popupBtnsLayout.addComponent(noCSFOption);
        popupBtnsLayout.setComponentAlignment(noCSFOption, Alignment.MIDDLE_LEFT);
        noCSFOption.setWidth("40px");
        noCSFOption.setHeight("24px");
        noCSFOption.setStyleName("enablecsfselected");

        noCSFOption.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                if (noCSFOption.getStyleName().equalsIgnoreCase("enablecsfselected") && noSerumOption.getStyleName().equalsIgnoreCase("enableserumunselected")) {
                    Notification.show("You can not hide both CSF and serum datasets", Notification.Type.TRAY_NOTIFICATION);
                    return;

                }

                if (noCSFOption.getStyleName().equalsIgnoreCase("enablecsfunselected")) {

                    boolean success = Quant_Central_Manager.setHideCSF(false);
                    if (success) {
                        noCSFOption.setStyleName("enablecsfselected");
                    } else {
                        Notification.show("No serum datasets available for the selected disease", Notification.Type.TRAY_NOTIFICATION);
                        return;
                    }
                } else {

                    boolean success = Quant_Central_Manager.setHideCSF(true);
                    if (success) {
                        noCSFOption.setStyleName("enablecsfunselected");
                    } else {
                        Notification.show("No serum datasets available for the selected disease", Notification.Type.TRAY_NOTIFICATION);
                        return;
                    }
                }
                Quant_Central_Manager.resetFiltersListener();
                pieChartFiltersBtnWrapper.removeAllComponents();
                StudiesPieChartFiltersContainerLayout pieChartFiltersLayout = new StudiesPieChartFiltersContainerLayout(Quant_Central_Manager, CSFPR_Handler);
                pieChartFiltersBtnWrapper.addComponent(pieChartFiltersLayout.getPieChartFiltersBtn());
                counterLabel.setValue("<center style='text-align: center;'>( " + Quant_Central_Manager.getCurrentDsNumber() + " / " + Quant_Central_Manager.getTotalDsNumber() + " )</center>");
                counterLabel.setDescription("#Datasets: " + Quant_Central_Manager.getCurrentDsNumber() + "<br/>#Total datasets: " + Quant_Central_Manager.getTotalDsNumber());
            }
        });

        VerticalLayout clearFilterBtn = new VerticalLayout();
        clearFilterBtn.setHeight("24px");
        clearFilterBtn.setWidth("40px");
        clearFilterBtn.setStyleName("clearfilters");
        popupBtnsLayout.addComponent(clearFilterBtn);
        popupBtnsLayout.setComponentAlignment(clearFilterBtn, Alignment.MIDDLE_LEFT);
        clearFilterBtn.setDescription("Reset all applied filters");

        clearFilterBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                Quant_Central_Manager.resetFiltersListener();
            }
        });

        VerticalLayout hideFiltersBtn = new VerticalLayout();
        hideFiltersBtn.setHeight("24px");
        hideFiltersBtn.setWidth("40px");
        hideFiltersBtn.setStyleName("hidefilterslayout");
        popupBtnsLayout.addComponent(hideFiltersBtn);
        popupBtnsLayout.setComponentAlignment(hideFiltersBtn, Alignment.MIDDLE_RIGHT);
        hideFiltersBtn.setDescription("Hide filters layout");

        hideFiltersBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                filtersContainerLayout.setHeight("0px");
                filterLabelBtn.setDescription("Show filters");
                filterLabelBtn.setStyleName("showfilterbtnlabel");
//                filterLabelBtn.setStyleName("showfilter");
            }
        });

//        
//         int y = Page.getCurrent().getBrowserWindowHeight() - Math.max(heatmapH, 710) - 200;
//        Tips tips = CSFPR_Handler.getTipsGenerator().generateTip("Remeber you can sort and select the disease groups using <u>Sort and Select</u> feature", 180, y);
//        leftBottomBtnLayout.addComponent(tips);
//        leftBottomBtnLayout.setComponentAlignment(tips, Alignment.TOP_LEFT);
        this.resizeLayout(diseaseGroupsColSet.size(), diseaseGroupsRowSet.size());
//        int heatmapH = Math.max((heatmapHeaderCellWidth + 20 + 12 + (heatmapCellWidth * Math.max(diseaseGroupsColSet.size(), diseaseGroupsRowSet.size()))), 500) + 20;
        rightPanelHeight = leftPanelHeight;  //Math.max(heatmapH + 68, 520);
        heatmapFilterComponent = new HeatMapFilter(Quant_Central_Manager, leftPanelWidth, diseaseGroupsRowSet, diseaseGroupsColSet, Quant_Central_Manager.getDiseaseGroupsArray(), heatmapCellWidth, heatmapHeaderCellWidth, CSFPR_Handler.getDiseaseFullNameMap());
        heatmapFilterComponent.setVisible(false);
        heatmapFilterComponent.setHeight(heatmapH + "px");
        heatmapFilterComponent.setSingleSelection(false);
        leftContainerLayout.addComponent(heatmapFilterComponent);
        leftContainerLayout.setComponentAlignment(heatmapFilterComponent, Alignment.TOP_LEFT);

        //init heatmap filters buttons 
        btnsLayout = new HorizontalLayout();
        btnsLayout.setVisible(false);

        btnsLayout.setWidth(leftPanelWidth + "px");
        leftContainerLayout.setWidth(leftPanelWidth + "px");
        btnsLayout.setHeight("24px");
        btnsLayout.setSpacing(true);
        this.leftContainerLayout.addComponent(btnsLayout);
        btnsLayout.setMargin(new MarginInfo(false, false, true, false));//        
        btnsLayout.setStyleName(Reindeer.LAYOUT_WHITE);

        Label commentLabel = new Label("<b>*</b> Multiple groups combined in one group");
        commentLabel.setStyleName(Reindeer.LABEL_SMALL);
        commentLabel.setContentMode(ContentMode.HTML);
        btnsLayout.addComponent(commentLabel);
        rightBottomBtnLayout = new HorizontalLayout();
        rightBottomBtnLayout.setHeightUndefined();
        rightBottomBtnLayout.setWidthUndefined();
        rightBottomBtnLayout.setSpacing(true);
        btnsLayout.addComponent(rightBottomBtnLayout);
        btnsLayout.setComponentAlignment(rightBottomBtnLayout, Alignment.TOP_RIGHT);

        StudiesInformationPopupBtn showStudiesBtn = new StudiesInformationPopupBtn(Quant_Central_Manager);
        rightBottomBtnLayout.addComponent(showStudiesBtn);
        rightBottomBtnLayout.setComponentAlignment(showStudiesBtn, Alignment.TOP_LEFT);

        Button exportTableBtn = new Button("");
        exportTableBtn.setHeight("23px");
        exportTableBtn.setWidth("23px");
        exportTableBtn.setPrimaryStyleName("exportxslbtn");
//        exportTableBtn.addStyleName("heatmapbtns");
        rightBottomBtnLayout.addComponent(exportTableBtn);
        rightBottomBtnLayout.setComponentAlignment(exportTableBtn, Alignment.BOTTOM_RIGHT);
        exportTableBtn.setDescription("Export all dataset data");
        final QuantDatasetsfullStudiesTableLayout quantStudiesTable = new QuantDatasetsfullStudiesTableLayout(Quant_Central_Manager);
        rightBottomBtnLayout.addComponent(quantStudiesTable);

        exportTableBtn.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                ExcelExport csvExport = new ExcelExport(quantStudiesTable, "CSF-PR  Quant Datasets Information");
                csvExport.setReportTitle("CSF-PR / Quant Datasets Information ");
                csvExport.setExportFileName("CSF-PR - Quant Datasets Information" + ".xls");
                csvExport.setMimeType(ExcelExport.EXCEL_MIME_TYPE);
                csvExport.setDisplayTotals(false);

                csvExport.setDateDataFormat("0");
                csvExport.setExcelFormatOfProperty("Index", "0");
                csvExport.setExcelFormatOfProperty("#Quantified Proteins", "0");
                csvExport.setExcelFormatOfProperty("patientsGroup2Number", "0");
                csvExport.setExcelFormatOfProperty("#patientsGroup1Number", "0");

                csvExport.export();

            }
        });

        VerticalLayout selectAllBtn = new VerticalLayout();
        selectAllBtn.setStyleName("selectallbtn");
        rightBottomBtnLayout.addComponent(selectAllBtn);
        rightBottomBtnLayout.setComponentAlignment(selectAllBtn, Alignment.TOP_LEFT);
        selectAllBtn.setDescription("Select all disease group comparisons");
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
        unselectAllBtn.setDescription("Unselect all disease group comparisons");
        unselectAllBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                heatmapFilterComponent.unselectAll();
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
                filtersContainerLayout.setVisible(!filtersContainerLayout.isVisible());
                diseaseCategorySelectLayout.setVisible(!diseaseCategorySelectLayout.isVisible());// 

                if (!btnsLayout.isVisible()) {
                    diseaseCategorySelectLayoutWrapper.setHeight("25px");
                    selectionOverviewBubbleChart.updateSize(pageWidth - 250, rightPanelHeight);
                    leftContainerLayout.setWidth((170) + "px");
                    rightContainerLayout.setWidth((pageWidth - 170 - 70) + "px");
                    DiseaseGroupsFiltersContainer.this.setExpandRatio(leftContainerLayout, 170);
                    DiseaseGroupsFiltersContainer.this.setExpandRatio(rightContainerLayout, (pageWidth - 170));
                    heatmapFilterComponent.updateHideComparisonThumbBtn("defaultResource", false);

                } else {
                    diseaseCategorySelectLayoutWrapper.setHeight("23px");
                    selectionOverviewBubbleChart.updateSize(rightPanelWidth, rightPanelHeight);
                    DiseaseGroupsFiltersContainer.this.setExpandRatio(leftContainerLayout, leftPanelWidth);
                    DiseaseGroupsFiltersContainer.this.setExpandRatio(rightContainerLayout, rightPanelWidth);
                    resizeLayout(Quant_Central_Manager.getSelectedHeatMapColumns().size(), Quant_Central_Manager.getSelectedHeatMapRows().size());
                    heatmapFilterComponent.updateHideComparisonThumbBtn(selectionOverviewBubbleChart.getChartThumbImage(), true);

                }
            }
        };

        heatmapFilterComponent.addHideHeatmapBtnListener(hideShowCompTableListener);

        ///init right conatainer layout 
//        mainContainerWidth = (pageWidth - 70);
        rightContainerLayout.setStyleName("slowresizelayout");
        if (userCustomizedComparison != null) {
            selectionOverviewBubbleChart = new ComparisonsSelectionOverviewBubbleChart(Quant_Central_Manager, CSFPR_Handler, rightPanelWidth, rightPanelHeight, new LinkedHashSet<QuantDiseaseGroupsComparison>(), searchQuantificationProtList, userCustomizedComparison);
        } else {
            selectionOverviewBubbleChart = new ComparisonsSelectionOverviewBubbleChart(Quant_Central_Manager, CSFPR_Handler, rightPanelWidth, rightPanelHeight, new LinkedHashSet<QuantDiseaseGroupsComparison>(), searchQuantificationProtList);
        }
//        selectionOverviewBubbleChart.addStyleName("slowresizelayout");
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
//        this.setExpandRatio(leftContainerLayout, leftPanelExpendingRatio);
//        this.setExpandRatio(rightContainerLayout, rightPanelExpandingRatio);
        Quant_Central_Manager.registerStudySelectionListener(DiseaseGroupsFiltersContainer.this);
        Quant_Central_Manager.registerFilterListener(DiseaseGroupsFiltersContainer.this);
        heatmapFilterComponent.unselectAll();

        btnsLayout.setVisible(false);
        filtersContainerLayout.setVisible(false);
        heatmapFilterComponent.setVisible(false);
        heatmapFilterComponent.showCompBtn(false);
        notification.Notifi();
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
            resizeLayout(Quant_Central_Manager.getSelectedHeatMapColumns().size(), Quant_Central_Manager.getSelectedHeatMapRows().size());
            selectionOverviewBubbleChart.updateSize(rightPanelWidth, rightPanelHeight);
            DiseaseGroupsFiltersContainer.this.setExpandRatio(leftContainerLayout, leftPanelWidth);
            DiseaseGroupsFiltersContainer.this.setExpandRatio(rightContainerLayout, rightPanelWidth);
            heatmapFilterComponent.updateHideComparisonThumbBtn(null, null);
            if (DiseaseGroupsFiltersContainer.this.isAttached()) {
                UI.getCurrent().scrollIntoView(counterLabel);
            }
            counterLabel.setValue("<center style='text-align: center;'>( " + Quant_Central_Manager.getCurrentDsNumber() + " / " + Quant_Central_Manager.getTotalDsNumber() + " )</center>");
            counterLabel.setDescription("#Datasets: " + Quant_Central_Manager.getCurrentDsNumber() + "<br/>#Total datasets: " + Quant_Central_Manager.getTotalDsNumber());
            return;
        } else if (type.equalsIgnoreCase("HeatMap_Update_level")) {
            selectAllComparisons();
        }
        counterLabel.setValue("<center style='text-align: center;'>( " + Quant_Central_Manager.getCurrentDsNumber() + " / " + Quant_Central_Manager.getTotalDsNumber() + " )</center>");
        counterLabel.setDescription("#Datasets: " + Quant_Central_Manager.getCurrentDsNumber() + "<br/>#Total datasets: " + Quant_Central_Manager.getTotalDsNumber());
        heatmapFilterComponent.updateHideComparisonThumbBtn(selectionOverviewBubbleChart.getChartThumbImage(), true);
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

    private void resizeLayout(int diseaseGroupsColumnNumber, int diseaseGroupsRowsNumber) {
        leftPanelWidth = Math.max((heatmapHeaderCellWidth + 20 + 12 + (heatmapCellWidth * diseaseGroupsColumnNumber)), 446);
        leftContainerLayout.setWidth((leftPanelWidth) + "px");
        rightContainerLayout.setWidth((pageWidth - leftPanelWidth - 70) + "px");
        rightPanelWidth = (pageWidth - leftPanelWidth - 100);
        leftPanelExpendingRatio = (float) leftPanelWidth / (float) (pageWidth + 70);
        rightPanelExpandingRatio = Math.min((float) (rightPanelWidth) / (float) (pageWidth - 70), (0.9f - leftPanelExpendingRatio));
        heatmapH = 20 + heatmapHeaderCellWidth + 14 + (heatmapCellWidth * diseaseGroupsRowsNumber);
        leftPanelHeight = 50 + 7 + heatmapH + 7 + 25;
        if (heatmapFilterComponent != null) {
            heatmapFilterComponent.setWidth(leftPanelWidth + "px");
            heatmapFilterComponent.setHeight(heatmapH + "px");
            btnsLayout.setWidth(leftPanelWidth + "px");
        }

    }
    private final String[] icons = new String[]{"img/ms_icon.png", "img/ad_icon.png", "img/pd_icon.png", "img/all_icon.png"};

    private VerticalLayout initDiseaseCatLayout(Set<String> diseaseSet, LayoutEvents.LayoutClickListener listener) {
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setWidth("200px");
        mainLayout.setHeight("100px");
        mainLayout.setStyleName("initialquantselectionlist");
        mainLayout.addStyleName("show");
        int index = 0;
        for (String str : diseaseSet) {
            HorizontalLayout labelBody = new HorizontalLayout();
            labelBody.setWidth("100%");
            labelBody.setHeight("100%");
            labelBody.addStyleName("click");
            labelBody.setData(str);
            labelBody.addLayoutClickListener(listener);
            Image icon = new Image(null, new ThemeResource(icons[index++]));
            labelBody.addComponent(icon);

            labelBody.setComponentAlignment(icon, Alignment.MIDDLE_LEFT);
            labelBody.setExpandRatio(icon, 10);
            mainLayout.addComponent(labelBody);

            Label l = new Label(str);
            labelBody.addComponent(l);
            labelBody.setComponentAlignment(l, Alignment.MIDDLE_LEFT);
            labelBody.setExpandRatio(l, 90);

        }

        return mainLayout;
    }

}
