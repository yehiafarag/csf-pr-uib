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
import com.vaadin.ui.Button.ClickListener;
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
import probe.com.selectionmanager.CSFFilter;
import probe.com.selectionmanager.CSFFilterSelection;
import probe.com.selectionmanager.QuantCentralManager;
import probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.popupreordergroups.SortableLayoutContainer;
import probe.com.view.core.DiseaseGroup;
import probe.com.view.core.Tips;
import probe.com.view.core.ToggleBtn;

/**
 *
 * @author Yehia Farag
 */
public class PopupReorderGroupsLayout extends VerticalLayout implements CSFFilter, LayoutEvents.LayoutClickListener {

    private final Window popupWindow;
    private final QuantCentralManager Quant_Central_Manager;
    private final VerticalLayout popupBodyLayout;
    private SortableLayoutContainer sortableDiseaseGroupI, sortableDiseaseGroupII;
    private LinkedHashSet<String> rowHeaders, colHeaders;
    private DiseaseGroup[] patientsGroupArr;
    private LinkedHashSet<Integer> studiesIndexes;

    @Override
    public void selectionChanged(String type) {
        if (type.equalsIgnoreCase("Pie_Chart_Selection")) {
            rowHeaders = Quant_Central_Manager.getSelectedHeatMapRows();
            sortableDiseaseGroupI.initLists(rowHeaders);
        } else if (type.equalsIgnoreCase("Reset_Disease_Groups_Level")) {
            colHeaders = Quant_Central_Manager.getSelectedHeatMapRows();
            rowHeaders = Quant_Central_Manager.getSelectedHeatMapRows();
            Map<Integer, QuantDatasetObject> quantDSArr = Quant_Central_Manager.getFilteredDatasetsList();
            initPopupLayout(rowHeaders, colHeaders, quantDSArr);
            sortableDiseaseGroupI.initLists(rowHeaders);

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

    public PopupReorderGroupsLayout(QuantCentralManager Quant_Central_Manager) {
     
        
        this.setStyleName("sortandselect");
        this.setDescription("Reorder and select disease groups");
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

        popupWindow.setVisible(false);
        popupWindow.setResizable(false);
        popupWindow.setClosable(true);
        popupWindow.setStyleName(Reindeer.WINDOW_LIGHT);
        popupWindow.setModal(true);
        popupWindow.setDraggable(false);
        popupWindow.setCaption("<font color='gray' style='font-weight: bold;!important'>&nbsp;&nbsp;Disease Groups</font>");
        popupWindow.setCaptionAsHtml(true);
        popupBodyLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        popupBodyLayout.setHeightUndefined();//(h - 50) + "px");
        popupWindow.setWindowMode(WindowMode.NORMAL);

        UI.getCurrent().addWindow(popupWindow);
        popupWindow.center();

        colHeaders = Quant_Central_Manager.getSelectedHeatMapRows();
        rowHeaders = Quant_Central_Manager.getSelectedHeatMapRows();
       
        
        
        Map<Integer, QuantDatasetObject> quantDSArr = Quant_Central_Manager.getFilteredDatasetsList();
       
        
        
        
        
       patientsGroupArr =  Quant_Central_Manager.getDiseaseGroupsArray();
        initPopupLayout(rowHeaders, colHeaders, quantDSArr);
//        this.Quant_Central_Manager.setHeatMapLevelSelection(rowHeaders, colHeaders, patientsGroupArr);
        this.Quant_Central_Manager.registerFilterListener(PopupReorderGroupsLayout.this);

    }

    private void initPopupLayout(LinkedHashSet<String> rowHeaders, LinkedHashSet<String> colHeaders, Map<Integer, QuantDatasetObject> quantDSArr) {
        popupBodyLayout.removeAllComponents();
        patientsGroupArr = new DiseaseGroup[quantDSArr.size()];
        int maxLabelWidth = -1;
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
            if (label1.split("\n")[0].length() > maxLabelWidth) {
                maxLabelWidth = label1.length();
            }
            if (label2.split("\n")[0].length() > maxLabelWidth) {
                maxLabelWidth = label2.length();
            }

            patientsGroupArr[i] = pg;
            pg.setQuantDatasetIndex(i);
            pg.setOriginalDatasetIndex(ds.getDsKey());
            i++;
        }

        int h = (Math.max(rowHeaders.size(), colHeaders.size()) * 27) + 150;
        int w = (maxLabelWidth * 10 * 2) + 72 + 50;
        if (Page.getCurrent().getBrowserWindowHeight() - 280 < h) {
            h = Page.getCurrent().getBrowserWindowHeight() - 280;
        }
        if (Page.getCurrent().getBrowserWindowWidth() < w) {
            w = Page.getCurrent().getBrowserWindowWidth();
        }

        popupBodyLayout.setWidth((w - 50) + "px");

        popupWindow.setWidth(w + "px");
        popupWindow.setHeight(h + "px");
        int subH = (h - 150);
        
       patientsGroupArr =  Quant_Central_Manager.getDiseaseGroupsArray();

        this.sortableDiseaseGroupI = new SortableLayoutContainer((w - 50), subH, " Disease Group A", rowHeaders, Quant_Central_Manager.getDiseaseStyleMap());
        this.sortableDiseaseGroupII = new SortableLayoutContainer((w - 50), subH, " Disease Group B", colHeaders, Quant_Central_Manager.getDiseaseStyleMap());
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
            Quant_Central_Manager.applyFilters(new CSFFilterSelection("Reorder_Selection", indexes, "", new HashSet<String>()));
        }
        
////       patientsGroupArr =  Quant_Central_Manager.getDiseaseGroupsArray();
        Quant_Central_Manager.setHeatMapLevelSelection(rowHeaders, colHeaders, patientsGroupArr);

    }

    

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        System.out.println("at layout clicked ");
           colHeaders = Quant_Central_Manager.getSelectedHeatMapColumns();
        rowHeaders = Quant_Central_Manager.getSelectedHeatMapRows();
        patientsGroupArr = Quant_Central_Manager.getDiseaseGroupsArray();
//        Map<Integer, QuantDatasetObject> quantDSArr = Quant_Central_Manager.getFilteredDatasetsList();
//        initPopupLayout(rowHeaders, colHeaders, quantDSArr);

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
        sortableDiseaseGroupII.selectAndHideUnselected(colHeaders, true);

        popupWindow.setVisible(true);
        popupWindow.center();
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
                    sortableDiseaseGroupII.selectAndHideUnselected(null, false);
//                    sortableDiseaseGroupII.setEnableSelection(false);
                    return;
                }

                Set<String> updatedGroupIISet = filterPatGroup2List(sortableDiseaseGroupI.getSelectionSet());
                sortableDiseaseGroupII.selectAndHideUnselected(updatedGroupIISet, false);

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
        ToggleBtn sortSelectToggleBtn = new ToggleBtn("Sort Groups ", "Select Groups ", "Sort – drag & drop", "*Select to filter datasets", width);
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
