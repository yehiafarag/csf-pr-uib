/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.components.datasetfilters;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseGroupComparison;
import no.uib.probe.csf.pr.touch.logic.beans.HeatMapHeaderCellInformationBean;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFFilter;
import no.uib.probe.csf.pr.touch.view.core.SortableLayoutContainer;
import org.vaadin.teemu.switchui.Switch;

/**
 *
 * @author Yehia Farag
 */
public class ReorderSelectGroupsFilter extends VerticalLayout implements CSFFilter, LayoutEvents.LayoutClickListener {

    private final Window popupWindow;
    private final HorizontalLayout diseaseGroupsContaioner;
    VerticalLayout mainBody;
//    private SortableLayoutContainer sortableDiseaseGroupI, sortableDiseaseGroupII;
    private LinkedHashSet<String> rowHeaders, colHeaders;
//    private DiseaseGroup[] patientsGroupArr;
    private LinkedHashSet<Integer> studiesIndexes;
    private final SortableLayoutContainer groupILayout, groupIILayout;
    private Set<DiseaseGroupComparison> patientsGroupComparisonsSet;

    public ReorderSelectGroupsFilter() {

        //init icon
        this.setWidth(25, Unit.PIXELS);
        this.setHeight(25, Unit.PIXELS);
        this.setStyleName("filterbtn");
        Image icon = new Image();
        icon.setSource(new ThemeResource("img/sort-select.png"));
        this.addComponent(icon);
        icon.setWidth(100, Unit.PERCENTAGE);
        icon.setHeight(100, Unit.PERCENTAGE);
        this.addLayoutClickListener(ReorderSelectGroupsFilter.this);
        this.setDescription("Reorder and select disease groups");

        this.mainBody = new VerticalLayout();
        mainBody.setHeightUndefined();
        mainBody.setWidth(100, Unit.PERCENTAGE);
        popupWindow = new Window() {
            @Override
            public void close() {

                popupWindow.setVisible(false);
            }
        };
        popupWindow.setContent(mainBody);

        popupWindow.setVisible(false);
        popupWindow.setResizable(false);
        popupWindow.setClosable(true);
        popupWindow.setModal(true);
        popupWindow.setDraggable(false);
        popupWindow.setCaption("<font color='gray' style='font-weight: bold;!important'>&nbsp;&nbsp;Disease Groups</font>");
        popupWindow.setCaptionAsHtml(true);
        popupWindow.setWindowMode(WindowMode.NORMAL);
        UI.getCurrent().addWindow(popupWindow);
        popupWindow.center();
        int width = Math.min(Page.getCurrent().getBrowserWindowWidth(), 800);
        int height = Math.min(Page.getCurrent().getBrowserWindowHeight(), 800);

        popupWindow.setWidth(width, Unit.PIXELS);
        popupWindow.setHeight(height, Unit.PIXELS);
        diseaseGroupsContaioner = new HorizontalLayout();

        mainBody.addComponent(diseaseGroupsContaioner);
        mainBody.setComponentAlignment(diseaseGroupsContaioner, Alignment.TOP_CENTER);

        diseaseGroupsContaioner.setStyleName("whitelayout");
        diseaseGroupsContaioner.setHeight((height - 100), Unit.PIXELS);
        diseaseGroupsContaioner.setWidth(100, Unit.PERCENTAGE);
        diseaseGroupsContaioner.setSpacing(true);

        groupILayout = new SortableLayoutContainer("Disease Group A", (height - 100));
        groupIILayout = new SortableLayoutContainer("Disease Group B", (height - 100));

        HorizontalLayout bottomContainert = new HorizontalLayout();
        bottomContainert.setWidth(100, Unit.PERCENTAGE);
        bottomContainert.setHeight(50, Unit.PIXELS);
        bottomContainert.setMargin(new MarginInfo(false, true, false, true));

        mainBody.addComponent(bottomContainert);
        mainBody.setComponentAlignment(bottomContainert, Alignment.BOTTOM_CENTER);

        String sortString = "Sort - drag & drop";
        String selectString = "Select to filter datasets";
        Label commentLabel = new Label(sortString);
        commentLabel.setStyleName(ValoTheme.LABEL_SMALL);
        commentLabel.addStyleName(ValoTheme.LABEL_TINY);
        bottomContainert.addComponent(commentLabel);
        bottomContainert.setComponentAlignment(commentLabel, Alignment.TOP_LEFT);

        Switch selectSortSwichBtn = new Switch();
        selectSortSwichBtn.setDescription("Sort / Select disease groups");
        selectSortSwichBtn.addValueChangeListener((ValueChangeEvent event) -> {
            if (selectSortSwichBtn.getValue()) {

                groupILayout.setLayoutMode("sort");
                groupIILayout.setEnabled(true);
                groupIILayout.setLayoutMode("sort");
                commentLabel.setValue(sortString);

            } else {
                commentLabel.setValue(selectString);
                groupILayout.setLayoutMode("select");
                groupIILayout.setEnableSelection(false);
                groupIILayout.setLayoutMode("select");
                if (groupILayout.isSingleSelected()) {
                    groupIILayout.setEnableSelection(true);
                }
            }
        });

        selectSortSwichBtn.setImmediate(true);
        bottomContainert.addComponent(selectSortSwichBtn);
        bottomContainert.setComponentAlignment(selectSortSwichBtn, Alignment.TOP_CENTER);

        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.setSpacing(true);

        Button resetFiltersBtn = new Button("Reset");
        resetFiltersBtn.setStyleName(ValoTheme.BUTTON_TINY);
        btnLayout.addComponent(resetFiltersBtn);

        resetFiltersBtn.setDescription("Reset all groups to default");
        resetFiltersBtn.addClickListener((Button.ClickEvent event) -> {
//            resetToDefault();
        });
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyleName(ValoTheme.BUTTON_TINY);
        btnLayout.addComponent(cancelBtn);
        cancelBtn.addClickListener((Button.ClickEvent event) -> {
//            resetToPublicationsNames();
        });

        Button applyFilters = new Button("Update");
        applyFilters.setDescription("Update the datasets");
        applyFilters.setStyleName(ValoTheme.BUTTON_TINY);

        btnLayout.addComponent(applyFilters);
        applyFilters.addClickListener((Button.ClickEvent event) -> {
//            updateGroups();
        });

        bottomContainert.addComponent(btnLayout);
        bottomContainert.setComponentAlignment(btnLayout, Alignment.TOP_RIGHT);

        diseaseGroupsContaioner.addComponent(groupILayout);

        diseaseGroupsContaioner.addComponent(groupIILayout);

        Property.ValueChangeListener selectionChangeListenet = new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {

                if (groupILayout.isSingleSelected()) {
                    groupIILayout.setEnableSelection(true);
                } else {
                    groupIILayout.selectAndHideUnselected(null, false);
//                    sortableDiseaseGroupII.setEnableSelection(false);
                    return;
                }

                Set<HeatMapHeaderCellInformationBean> updatedGroupIISet = filterPatGroup2List(groupILayout.getSelectionSet());
                groupIILayout.selectAndHideUnselected(updatedGroupIISet, false);

            }
        };
        groupILayout.addSelectionValueChangeListener(selectionChangeListenet);

//        colHeaders = Quant_Central_Manager.getSelectedHeatMapRows();
//        rowHeaders = Quant_Central_Manager.getSelectedHeatMapRows();
//        Map<Integer, QuantDatasetObject> quantDSArr = Quant_Central_Manager.getFilteredDatasetsList();
//       patientsGroupArr =  Quant_Central_Manager.getDiseaseGroupsArray();
//        initPopupLayout(rowHeaders, colHeaders, quantDSArr);
//        this.Quant_Central_Manager.setHeatMapLevelSelection(rowHeaders, colHeaders, patientsGroupArr);
//        this.Quant_Central_Manager.registerFilterListener(ReorderSelectGroupsFilter.this);
    }

    @Override
    public void selectionChanged(String type) {
        if (type.equalsIgnoreCase("Pie_Chart_Selection")) {
//            rowHeaders = Quant_Central_Manager.getSelectedHeatMapRows();
//            sortableDiseaseGroupI.initLists(rowHeaders);
        } else if (type.equalsIgnoreCase("Reset_Disease_Groups_Level")) {
//            colHeaders = Quant_Central_Manager.getSelectedHeatMapRows();
//            rowHeaders = Quant_Central_Manager.getSelectedHeatMapRows();
//            Map<Integer, QuantDatasetObject> quantDSArr = Quant_Central_Manager.getFilteredDatasetsList();
//            initPopupLayout(rowHeaders, colHeaders, quantDSArr);
//            sortableDiseaseGroupI.initLists(rowHeaders);

        }
    }

    @Override
    public String getFilterId() {

        return this.getClass().getName();
    }

    @Override
    public void removeFilterValue(String value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private final Map<String, HeatMapHeaderCellInformationBean> fullCellInfoMap = new HashMap<>();

    public void updateData(LinkedHashSet<HeatMapHeaderCellInformationBean> rowHeaders, LinkedHashSet<HeatMapHeaderCellInformationBean> colHeaders, Set<DiseaseGroupComparison> patientsGroupComparisonsSet) {
        groupILayout.updateData(rowHeaders);
        groupIILayout.updateData(colHeaders);
        this.patientsGroupComparisonsSet = patientsGroupComparisonsSet;
        fullCellInfoMap.clear();
        rowHeaders.stream().forEach((cell) -> {
            fullCellInfoMap.put(cell.getDiseaseGroupName() + "__" + cell.getDiseaseCategory(), cell);
        });
        colHeaders.stream().forEach((cell) -> {
            fullCellInfoMap.put(cell.getDiseaseGroupName() + "__" + cell.getDiseaseCategory(), cell);
        });////        diseaseGroupsContaioner.removeAllComponents();
//        DiseaseGroupComparison[] patientsGroupArr = new DiseaseGroupComparison[quantDSMap.size()];
////        int maxLabelWidth = -1;
//        int i = 0;
//        for (QuantDatasetObject ds : quantDSMap.values()) {
//            if (ds == null) {
//                continue;
//            }
//            DiseaseGroup pg = new DiseaseGroup();
//            String pgI = ds.getPatientsGroup1();
//            pg.setPatientsGroupI(pgI);
//            String label1;
//            if (pgI.equalsIgnoreCase("Not Available") || pgI.equalsIgnoreCase("control")) {
//                pgI = "";
//            }
//            String subpgI = ds.getPatientsSubGroup1();
//            pg.setPatientsSubGroupI(subpgI);
//            if (!subpgI.equalsIgnoreCase("") && !subpgI.equalsIgnoreCase("Not Available")) {
//                pgI = subpgI;
//            }
//            label1 = pgI;
//            pg.setPatientsGroupILabel(label1);
//
//            String pgII = ds.getPatientsGroup2();
//            pg.setPatientsGroupII(pgII);
//            String label2;
//            if (pgII.equalsIgnoreCase("Not Available") || pgII.equalsIgnoreCase("control")) {
//                pgII = "";
//            }
//            String subpgII = ds.getPatientsSubGroup2();
//            pg.setPatientsSubGroupII(subpgII);
//            if (!subpgII.equalsIgnoreCase("") && !subpgII.equalsIgnoreCase("Not Available")) {
//                pgII = subpgII;
//            }
//            label2 = pgII;
//            pg.setPatientsGroupIILabel(label2);
//            if (label1.split("\n")[0].length() > maxLabelWidth) {
//                maxLabelWidth = label1.length();
//            }
//            if (label2.split("\n")[0].length() > maxLabelWidth) {
//                maxLabelWidth = label2.length();
//            }
//
//            patientsGroupArr[i] = pg;
//            pg.setQuantDatasetIndex(i);
//            pg.setOriginalDatasetIndex(ds.getDsKey());
//            i++;
//        }
//
//        int h = (Math.max(rowHeaders.size(), colHeaders.size()) * 27) + 150;
//        int w = (maxLabelWidth * 10 * 2) + 72 + 50;
//        if (Page.getCurrent().getBrowserWindowHeight() - 280 < h) {
//            h = Page.getCurrent().getBrowserWindowHeight() - 280;
//        }
//        if (Page.getCurrent().getBrowserWindowWidth() < w) {
//            w = Page.getCurrent().getBrowserWindowWidth();
//        }
//
//        diseaseGroupsContaioner.setWidth((w - 50) + "px");
//
//        popupWindow.setWidth(w + "px");
//        popupWindow.setHeight(h + "px");
//        int subH = (h - 150);
//
//       patientsGroupArr =  Quant_Central_Manager.getDiseaseGroupsArray();
//
//        this.sortableDiseaseGroupI = new SortableLayoutContainer((w - 50), subH, " Disease Group A", rowHeaders, Quant_Central_Manager.getDiseaseStyleMap());
//        this.sortableDiseaseGroupII = new SortableLayoutContainer((w - 50), subH, " Disease Group B", colHeaders, Quant_Central_Manager.getDiseaseStyleMap());
//        this.initPopupBody((w - 50));
    }

    private void updateSelectionManager(LinkedHashSet<Integer> datasetIndexes) {

//        if (datasetIndexes != null) {
//
//            int[] indexes = new int[datasetIndexes.size()];
//            int i = 0;
//            for (int id : datasetIndexes) {
//                indexes[i] = id;
//                i++;
//            }
//            Quant_Central_Manager.applyFilters(new CSFFilterSelection("Reorder_Selection", indexes, "", new HashSet<String>()));
//        }
//        
//////       patientsGroupArr =  Quant_Central_Manager.getDiseaseGroupsArray();
//        Quant_Central_Manager.setHeatMapLevelSelection(rowHeaders, colHeaders, patientsGroupArr);
    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
//        System.out.println("at layout clicked ");
//           colHeaders = Quant_Central_Manager.getSelectedHeatMapColumns();
//        rowHeaders = Quant_Central_Manager.getSelectedHeatMapRows();
//        patientsGroupArr = Quant_Central_Manager.getDiseaseGroupsArray();
////        Map<Integer, QuantDatasetObject> quantDSArr = Quant_Central_Manager.getFilteredDatasetsList();
////        initPopupLayout(rowHeaders, colHeaders, quantDSArr);
//
////        Map<Integer, QuantDatasetObject> quantDSArr = Quant_Central_Manager.getFilteredDatasetsList();
////        patientsGroupArr = new DiseaseGroup[quantDSArr.size()];
////        int i = 0;
////        for (QuantDatasetObject ds : quantDSArr.values()) {
////            if (ds == null) {
////                continue;
////            }
////            DiseaseGroup pg = new DiseaseGroup();
////            String pgI = ds.getPatientsGroup1();
////            pg.setPatientsGroupI(pgI);
////            String label1;
////            if (pgI.equalsIgnoreCase("Not Available") || pgI.equalsIgnoreCase("control")) {
////                pgI = "";
////            }
////            String subpgI = ds.getPatientsSubGroup1();
////            pg.setPatientsSubGroupI(subpgI);
////            if (!subpgI.equalsIgnoreCase("") && !subpgI.equalsIgnoreCase("Not Available")) {
////                pgI = subpgI;
////            }
////            label1 = pgI;
////            pg.setPatientsGroupILabel(label1);
////
////            String pgII = ds.getPatientsGroup2();
////            pg.setPatientsGroupII(pgII);
////            String label2;
////            if (pgII.equalsIgnoreCase("Not Available") || pgII.equalsIgnoreCase("control")) {
////                pgII = "";
////            }
////            String subpgII = ds.getPatientsSubGroup2();
////            pg.setPatientsSubGroupII(subpgII);
////            if (!subpgII.equalsIgnoreCase("") && !subpgII.equalsIgnoreCase("Not Available")) {
////                pgII = subpgII;
////            }
////            label2 = pgII;
////            pg.setPatientsGroupIILabel(label2);
////            patientsGroupArr[i] = pg;
////            pg.setQuantDatasetIndex(i);
////            pg.setOriginalDatasetIndex(ds.getDsKey());
////            i++;
////        }
//        sortableDiseaseGroupI.updateLists(rowHeaders);
//        sortableDiseaseGroupII.selectAndHideUnselected(colHeaders, true);

        popupWindow.setVisible(true);
        popupWindow.center();
    }

    private void initPopupBody(int w) {
//        HorizontalLayout mainContainer = new HorizontalLayout();
//        mainContainer.setStyleName(Reindeer.LAYOUT_WHITE);
//        mainContainer.setSpacing(true);
//        mainContainer.setWidth(w + "px");
//
//        mainContainer.setHeightUndefined();
//        mainContainer.setMargin(new MarginInfo(true, false, false, false));
//
//        mainContainer.addComponent(sortableDiseaseGroupI);
//        mainContainer.setComponentAlignment(sortableDiseaseGroupI, Alignment.TOP_LEFT);
//
//        mainContainer.addComponent(sortableDiseaseGroupII);
//        mainContainer.setComponentAlignment(sortableDiseaseGroupII, Alignment.TOP_RIGHT);
////
//        Property.ValueChangeListener selectionChangeListenet = new Property.ValueChangeListener() {
//
//            @Override
//            public void valueChange(Property.ValueChangeEvent event) {
//
//                if (sortableDiseaseGroupI.isSingleSelected()) {
//                    sortableDiseaseGroupII.setEnableSelection(true);
//                } else {
//                    sortableDiseaseGroupII.selectAndHideUnselected(null, false);
////                    sortableDiseaseGroupII.setEnableSelection(false);
//                    return;
//                }
//
//                Set<String> updatedGroupIISet = filterPatGroup2List(sortableDiseaseGroupI.getSelectionSet());
//                sortableDiseaseGroupII.selectAndHideUnselected(updatedGroupIISet, false);
//
//            }
//        };
//        sortableDiseaseGroupI.addSelectionValueChangeListener(selectionChangeListenet);
//
//        diseaseGroupsContaioner.addComponent(mainContainer);
//        diseaseGroupsContaioner.setComponentAlignment(mainContainer, Alignment.TOP_LEFT);
//
//        HorizontalLayout bottomLayout = new HorizontalLayout();
//        bottomLayout.setWidth("100%");
//        bottomLayout.setMargin(new MarginInfo(true, false, false, false));
//
//        diseaseGroupsContaioner.addComponent(bottomLayout);
//        int width = w - 200;
//        ToggleBtn sortSelectToggleBtn = new ToggleBtn("Sort Groups ", "Select Groups ", "Sort – drag & drop", "*Select to filter datasets", width);
//        bottomLayout.addComponent(sortSelectToggleBtn);//commentLabel
//        bottomLayout.setComponentAlignment(sortSelectToggleBtn, Alignment.MIDDLE_LEFT);//commentLabel
//        bottomLayout.setExpandRatio(sortSelectToggleBtn, w);
//        HorizontalLayout btnLayout = new HorizontalLayout();
//        btnLayout.setSpacing(true);
//        LayoutEvents.LayoutClickListener toggleListener = new LayoutEvents.LayoutClickListener() {
//
//            @Override
//            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
//                if (event.getComponent().getStyleName().equalsIgnoreCase("toggleleft")) {
//                    sortableDiseaseGroupI.setLayoutMode("sort");
//                    sortableDiseaseGroupII.setEnabled(true);
//                    sortableDiseaseGroupII.setLayoutMode("sort");
//
//                } else {
//                    sortableDiseaseGroupI.setLayoutMode("select");
//                    sortableDiseaseGroupII.setEnableSelection(false);
//                    sortableDiseaseGroupII.setLayoutMode("select");
//                    if (sortableDiseaseGroupI.isSingleSelected()) {
//                        sortableDiseaseGroupII.setEnableSelection(true);
//                    }
//
//                }
//
//            }
//        };
//        sortSelectToggleBtn.addLayoutClickListener(toggleListener);
//
//        btnLayout.setWidthUndefined();
//        bottomLayout.addComponent(btnLayout);
//        bottomLayout.setComponentAlignment(btnLayout, Alignment.TOP_RIGHT);
//        bottomLayout.setExpandRatio(btnLayout, 250);
//        Button applyFilters = new Button("Apply");
//        applyFilters.setDescription("Apply the updates");
//        applyFilters.setPrimaryStyleName("resetbtn");
//        applyFilters.setWidth("50px");
//        applyFilters.setHeight("24px");
//
//        btnLayout.addComponent(applyFilters);
//        applyFilters.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//                rowHeaders = sortableDiseaseGroupI.getSortedSet();
//                colHeaders = sortableDiseaseGroupII.getSortedSet();
//                updateSelectionManager(studiesIndexes);
//                popupWindow.close();
//            }
//        });
//
//        Button cancelFiltersBtn = new Button("Cancel");
//        cancelFiltersBtn.setPrimaryStyleName("resetbtn");
//        btnLayout.addComponent(cancelFiltersBtn);
//        cancelFiltersBtn.setWidth("50px");
//        cancelFiltersBtn.setHeight("24px");
//
//        cancelFiltersBtn.setDescription("Reset all applied filters");
//        cancelFiltersBtn.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//                popupWindow.close();
//            }
//        });
//        Button resetFiltersBtn = new Button("Reset");
//        resetFiltersBtn.setPrimaryStyleName("resetbtn");
//        btnLayout.addComponent(resetFiltersBtn);
//        resetFiltersBtn.setWidth("50px");
//        resetFiltersBtn.setHeight("24px");
//
//        resetFiltersBtn.setDescription("Reset all groups");
//        resetFiltersBtn.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//                Quant_Central_Manager.resetFiltersListener();
//                rowHeaders = Quant_Central_Manager.getSelectedHeatMapRows();
//                colHeaders = Quant_Central_Manager.getSelectedHeatMapColumns();
//                sortableDiseaseGroupI.initLists(rowHeaders);
//                sortableDiseaseGroupII.initLists(colHeaders);
//
//            }
//        });

    }

    private Set<HeatMapHeaderCellInformationBean> filterPatGroup2List(Set<HeatMapHeaderCellInformationBean> sel1) {
        Set<HeatMapHeaderCellInformationBean> labels = new LinkedHashSet<>();
        studiesIndexes = new LinkedHashSet<>();
        patientsGroupComparisonsSet.stream().forEach((pg) -> {
            sel1.stream().filter((label) -> (pg.checkLabel(label.getDiseaseGroupName() + "__" + label.getDiseaseCategory()))).map((label) -> {
                labels.add(fullCellInfoMap.get(pg.getValLabel(label.getDiseaseGroupName() + "__" + label.getDiseaseCategory())));
                return label;
            }).forEach((_item) -> {
                studiesIndexes.add(pg.getOriginalDatasetIndex());
            });
        });

        return labels;
    }

}
