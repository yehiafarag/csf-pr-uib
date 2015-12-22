/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters;

import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import probe.com.model.beans.quant.QuantDatasetObject;
import probe.com.selectionmanager.CSFFilterSelection;
import probe.com.selectionmanager.QuantCentralManager;
import probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.popupreordergroups.SortableLayoutContainer;
import probe.com.view.core.DiseaseGroup;
import probe.com.view.core.ToggleBtn;

/**
 *
 * @author Yehia Farag
 */
public class PopupReorderGroupsLayout extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private final Window popupWindow;
    private final QuantCentralManager Quant_Central_Manager;
    private final VerticalLayout popupBodyLayout;
    private final SortableLayoutContainer sortableDiseaseGroupI, sortableDiseaseGroupII;
    private LinkedHashSet<String> rowHeaders, colHeaders;
    private DiseaseGroup[] patientsGroupArr;
    private LinkedHashSet<Integer> studiesIndexes;

    public PopupReorderGroupsLayout(QuantCentralManager Quant_Central_Manager) {
        this.setStyleName("reordergroupsbtn");
        this.setDescription("Reorder All Disease Groups Comparisons");
        this.Quant_Central_Manager = Quant_Central_Manager;
        this.addLayoutClickListener(PopupReorderGroupsLayout.this);
        this.popupBodyLayout = new VerticalLayout();
        VerticalLayout windowLayout = new VerticalLayout();
        popupWindow = new Window() {
            @Override
            public void close() {

                popupWindow.setVisible(false);
            }
        };
        popupWindow.setContent(windowLayout);
        windowLayout.addComponent(popupBodyLayout);
        windowLayout.setComponentAlignment(popupBodyLayout, Alignment.MIDDLE_CENTER);
        int h = 600;
        int w = 700;
        if (Page.getCurrent().getBrowserWindowHeight() < 700) {
            h = Page.getCurrent().getBrowserWindowHeight();
        }
        if (Page.getCurrent().getBrowserWindowWidth() < 700) {
            w = Page.getCurrent().getBrowserWindowWidth();
        }

        popupBodyLayout.setWidth((w - 50) + "px");
        popupBodyLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        popupBodyLayout.setHeightUndefined();//(h - 50) + "px");
        popupWindow.setWindowMode(WindowMode.NORMAL);
        popupWindow.setWidth(w + "px");
        popupWindow.setHeight(h + "px");
        popupWindow.setVisible(false);
        popupWindow.setResizable(false);
        popupWindow.setClosable(true);
        popupWindow.setStyleName(Reindeer.WINDOW_LIGHT);
        popupWindow.setModal(true);
        popupWindow.setDraggable(false);
        popupWindow.setCaption("&nbsp;&nbsp;Disease Groups");
        popupWindow.setCaptionAsHtml(true);

        UI.getCurrent().addWindow(popupWindow);
        popupWindow.center();
        int subH = (h - 150);
        colHeaders = Quant_Central_Manager.getSelectedHeatMapRows();
        rowHeaders = Quant_Central_Manager.getSelectedHeatMapRows();

        Map<Integer, QuantDatasetObject> quantDSArr = Quant_Central_Manager.getFilteredDatasetsList();
        patientsGroupArr = new DiseaseGroup[quantDSArr.size()];
        int i = 0;
        for (QuantDatasetObject ds : quantDSArr.values()) {
            if (ds == null) {
                continue;
            }
            DiseaseGroup pg = new DiseaseGroup();
            String pgI = ds.getPatientsGroup1();
            pg.setPatientsGroupI(pgI);
            String label1;
            if (pgI.equalsIgnoreCase("Not Available") || pgI.equalsIgnoreCase("control")) {
                pgI = "";
            }
            String subpgI = ds.getPatientsSubGroup1();
            pg.setPatientsSubGroupI(subpgI);
            if (!subpgI.equalsIgnoreCase("") && !subpgI.equalsIgnoreCase("Not Available")) {
                pgI = subpgI;
            }
            label1 = pgI;
            pg.setPatientsGroupILabel(label1);

            String pgII = ds.getPatientsGroup2();
            pg.setPatientsGroupII(pgII);
            String label2;
            if (pgII.equalsIgnoreCase("Not Available") || pgII.equalsIgnoreCase("control")) {
                pgII = "";
            }
            String subpgII = ds.getPatientsSubGroup2();
            pg.setPatientsSubGroupII(subpgII);
            if (!subpgII.equalsIgnoreCase("") && !subpgII.equalsIgnoreCase("Not Available")) {
                pgII = subpgII;
            }
            label2 = pgII;
            pg.setPatientsGroupIILabel(label2);
            patientsGroupArr[i] = pg;
            pg.setQuantDatasetIndex(i);
            pg.setOriginalDatasetIndex(ds.getDsKey());
            i++;
        }

        this.sortableDiseaseGroupI = new SortableLayoutContainer((w - 50), subH, " Disease Group A", rowHeaders);
        this.sortableDiseaseGroupII = new SortableLayoutContainer((w - 50), subH, " Disease Group B", colHeaders);
        this.initPopupBody((w - 50));

    }

    private void updateSelectionManager(LinkedHashSet<Integer> datasetIndexes) {

        if (datasetIndexes != null) {

            int[] indexes = new int[datasetIndexes.size()];
            int i = 0;
            for (int id : datasetIndexes) {
                indexes[i] = id;
                i++;
            }
            Quant_Central_Manager.applyFilters(new CSFFilterSelection("Disease_Groups_Level", indexes, "", new HashSet<String>()));
        }
        Quant_Central_Manager.setHeatMapLevelSelection(rowHeaders, colHeaders, patientsGroupArr);
    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        colHeaders = Quant_Central_Manager.getSelectedHeatMapRows();
        rowHeaders = Quant_Central_Manager.getSelectedHeatMapRows();
        patientsGroupArr = Quant_Central_Manager.getDiseaseGroupsArray();

//        Map<Integer, QuantDatasetObject> quantDSArr = Quant_Central_Manager.getFilteredDatasetsList();
//        patientsGroupArr = new DiseaseGroup[quantDSArr.size()];
//        int i = 0;
//        for (QuantDatasetObject ds : quantDSArr.values()) {
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
//            patientsGroupArr[i] = pg;
//            pg.setQuantDatasetIndex(i);
//            pg.setOriginalDatasetIndex(ds.getDsKey());
//            i++;
//        }
        sortableDiseaseGroupI.updateLists(rowHeaders);

        popupWindow.setVisible(true);
    }

    private void initPopupBody(int w) {
        HorizontalLayout mainContainer = new HorizontalLayout();
        mainContainer.setStyleName(Reindeer.LAYOUT_WHITE);
        mainContainer.setSpacing(true);
        mainContainer.setWidth(w + "px");

        mainContainer.setHeightUndefined();
        mainContainer.setMargin(new MarginInfo(true, false, false, false));

        mainContainer.addComponent(sortableDiseaseGroupI);
        mainContainer.setComponentAlignment(sortableDiseaseGroupI, Alignment.TOP_LEFT);

        mainContainer.addComponent(sortableDiseaseGroupII);
        mainContainer.setComponentAlignment(sortableDiseaseGroupII, Alignment.TOP_RIGHT);

        Property.ValueChangeListener selectionChangeListenet = new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {

                if (sortableDiseaseGroupI.isSingleSelected()) {
                    sortableDiseaseGroupII.setEnableSelection(true);
                } else {
                    sortableDiseaseGroupII.initLists(colHeaders);
                    sortableDiseaseGroupII.setEnableSelection(false);
                    return;
                }

                Set<String> updatedGroupIISet = filterPatGroup2List(sortableDiseaseGroupI.getSelectionSet());

                sortableDiseaseGroupII.initLists(updatedGroupIISet);

            }
        };
        sortableDiseaseGroupI.addSelectionValueChangeListener(selectionChangeListenet);

        popupBodyLayout.addComponent(mainContainer);
        popupBodyLayout.setComponentAlignment(mainContainer, Alignment.TOP_LEFT);

        HorizontalLayout bottomLayout = new HorizontalLayout();
        bottomLayout.setWidth("100%");
        bottomLayout.setMargin(new MarginInfo(true, false, false, false));

        popupBodyLayout.addComponent(bottomLayout);
        int width = w - 200;
        ToggleBtn sortSelectToggleBtn = new ToggleBtn("Sort Groups ", "Select Groups ", "*Sort – Drag & drop", "*Select to filter studies", width);
        bottomLayout.addComponent(sortSelectToggleBtn);//commentLabel
        bottomLayout.setComponentAlignment(sortSelectToggleBtn, Alignment.MIDDLE_LEFT);//commentLabel
        bottomLayout.setExpandRatio(sortSelectToggleBtn, w);
        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.setSpacing(true);
        LayoutEvents.LayoutClickListener toggleListener = new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                if (event.getComponent().getStyleName().equalsIgnoreCase("toggleleft")) {
                    sortableDiseaseGroupI.setLayoutMode("sort");
                    sortableDiseaseGroupII.setEnabled(true);
                    sortableDiseaseGroupII.setLayoutMode("sort");

                } else {
                    sortableDiseaseGroupI.setLayoutMode("select");
                    sortableDiseaseGroupII.setEnableSelection(false);
                    sortableDiseaseGroupII.setLayoutMode("select");
                    if (sortableDiseaseGroupI.isSingleSelected()) {
                        sortableDiseaseGroupII.setEnableSelection(true);
                    }

                }

            }
        };
        sortSelectToggleBtn.addLayoutClickListener(toggleListener);

        btnLayout.setWidthUndefined();
        bottomLayout.addComponent(btnLayout);
        bottomLayout.setComponentAlignment(btnLayout, Alignment.TOP_RIGHT);
        bottomLayout.setExpandRatio(btnLayout, 250);
        Button applyFilters = new Button("Apply");
        applyFilters.setDescription("Apply the updates");
        applyFilters.setPrimaryStyleName("resetbtn");
        applyFilters.setWidth("50px");
        applyFilters.setHeight("24px");

        btnLayout.addComponent(applyFilters);
        applyFilters.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                rowHeaders = sortableDiseaseGroupI.getSortedSet();
                colHeaders = sortableDiseaseGroupII.getSortedSet();

                updateSelectionManager(studiesIndexes);
                popupWindow.close();
            }
        });

        Button cancelFiltersBtn = new Button("Cancel");
        cancelFiltersBtn.setPrimaryStyleName("resetbtn");
        btnLayout.addComponent(cancelFiltersBtn);
        cancelFiltersBtn.setWidth("50px");
        cancelFiltersBtn.setHeight("24px");

        cancelFiltersBtn.setDescription("Reset all applied filters");
        cancelFiltersBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                popupWindow.close();
            }
        });
        Button resetFiltersBtn = new Button("Reset");
        resetFiltersBtn.setPrimaryStyleName("resetbtn");
        btnLayout.addComponent(resetFiltersBtn);
        resetFiltersBtn.setWidth("50px");
        resetFiltersBtn.setHeight("24px");

        resetFiltersBtn.setDescription("Reset all groups");
        resetFiltersBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Quant_Central_Manager.resetFiltersListener();
                rowHeaders = Quant_Central_Manager.getSelectedHeatMapRows();
                colHeaders = Quant_Central_Manager.getSelectedHeatMapColumns();
                sortableDiseaseGroupI.initLists(rowHeaders);
                sortableDiseaseGroupII.initLists(colHeaders);

            }
        });

    }

    private Set<String> filterPatGroup2List(Set<String> sel1) {
        Set<String> labels = new LinkedHashSet<String>();
        studiesIndexes = new LinkedHashSet<Integer>();
        for (DiseaseGroup pg : patientsGroupArr) {
            for (String label : sel1) {
                if (pg.checkLabel(label)) {
                    labels.add(pg.getValLabel(label));
                    studiesIndexes.add(pg.getOriginalDatasetIndex());
                }
            }
        }

        return labels;
    }

}
