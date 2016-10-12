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
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseGroupComparison;
import no.uib.probe.csf.pr.touch.logic.beans.HeatMapHeaderCellInformationBean;
import no.uib.probe.csf.pr.touch.view.core.InformationButton;
import no.uib.probe.csf.pr.touch.view.core.PopupWindowFrameWithFunctionsBtns;
import no.uib.probe.csf.pr.touch.view.core.SortableLayoutContainer;
import org.vaadin.teemu.switchui.Switch;

/**
 *
 * @author Yehia Farag
 *
 * this class is responsible for updating heat-map rows and columns order as
 * well as allowing user to hide disease groups
 */
public abstract class ReorderSelectGroupsFilter extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private final PopupWindowFrameWithFunctionsBtns popupWindow;
    private final HorizontalLayout diseaseGroupsContaioner;
    private final VerticalLayout popupBody;
    private LinkedHashSet<Integer> studiesIndexes;
    private final SortableLayoutContainer groupILayout, groupIILayout;
    private Set<DiseaseGroupComparison> patientsGroupComparisonsSet;

    private final int screenWidth = Math.min(Page.getCurrent().getBrowserWindowWidth(), 1000);
    private final int screenHeight = Math.min(Page.getCurrent().getBrowserWindowHeight(), 800);

    /**
     *
     */
    public ReorderSelectGroupsFilter(boolean smallScreen) {
        //init icon
        this.setStyleName("filterbtn");
        Image icon = new Image();
        icon.setSource(new ThemeResource("img/sort-select.png"));
        this.addComponent(icon);
        icon.setWidth(100, Unit.PERCENTAGE);
        icon.setHeight(100, Unit.PERCENTAGE);
        this.addLayoutClickListener(ReorderSelectGroupsFilter.this);
        this.setDescription("Reorder and select disease groups");

        //init window layout 
        VerticalLayout frame = new VerticalLayout();
        popupBody = new VerticalLayout();
        frame.addComponent(popupBody);
        popupBody.setWidth(100, Unit.PERCENTAGE);
        popupBody.setSpacing(true);
        HorizontalLayout btnsFrame = new HorizontalLayout();
        popupWindow = new PopupWindowFrameWithFunctionsBtns("Disease Groups", frame, btnsFrame);
        popupWindow.setFrameWidth(screenWidth);
        
        
        diseaseGroupsContaioner = new HorizontalLayout();
        popupBody.addComponent(diseaseGroupsContaioner);
        popupBody.setComponentAlignment(diseaseGroupsContaioner, Alignment.TOP_CENTER);

        diseaseGroupsContaioner.setStyleName("whitelayout");
        diseaseGroupsContaioner.setWidth(100, Unit.PERCENTAGE);
        diseaseGroupsContaioner.setSpacing(true);

        int h;
//        if (!smallScreen) {
//            popupBody.addStyleName("padding20");
//            popupBody.setMargin(true);
            h = 250;

            popupBody.setHeight(screenHeight - 180, Unit.PIXELS);
//        } else {
//            popupBody.setMargin(new MarginInfo(false, true, false, true));
//            h = 220;
//
//            popupBody.setHeight(screenHeight - 220, Unit.PIXELS);
//        }
        groupILayout = new SortableLayoutContainer("Disease Group A", (screenHeight - h));
        groupIILayout = new SortableLayoutContainer("Disease Group B", (screenHeight - h));
        

        HorizontalLayout leftsideWrapper = new HorizontalLayout();
        btnsFrame.addComponent(leftsideWrapper);
        btnsFrame.setComponentAlignment(leftsideWrapper, Alignment.TOP_LEFT);
        leftsideWrapper.setSpacing(true);

        InformationButton info = new InformationButton("The disease groups shown and the order of these groups can be controlled by dragging and dropping the groups in the table, and by selecting only the groups to display. When the wanted order is achieved click the \"Apply\" button.", true);
        leftsideWrapper.addComponent(info);

        HorizontalLayout bottomContainert = new HorizontalLayout();
        bottomContainert.setWidth(100, Unit.PERCENTAGE);
        bottomContainert.setHeight(100, Unit.PERCENTAGE);
//        bottomContainert.setMargin(new MarginInfo(false, true, false, true));

        btnsFrame.addComponent(bottomContainert);
        btnsFrame.setComponentAlignment(bottomContainert, Alignment.BOTTOM_CENTER);

        String sortString = "Sort - drag & drop";
        String selectString = "Select to filter datasets";
        Label commentLabel = new Label(sortString);
        commentLabel.setStyleName(ValoTheme.LABEL_SMALL);
        commentLabel.addStyleName(ValoTheme.LABEL_TINY);

        Switch selectSortSwichBtn = new Switch();
        selectSortSwichBtn.setDescription("Sort / Select disease groups");
        selectSortSwichBtn.setValue(Boolean.TRUE);
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
        leftsideWrapper.addComponent(selectSortSwichBtn);
        leftsideWrapper.setComponentAlignment(selectSortSwichBtn, Alignment.MIDDLE_LEFT);

        leftsideWrapper.addComponent(commentLabel);
        leftsideWrapper.setComponentAlignment(commentLabel, Alignment.MIDDLE_LEFT);

        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.setSpacing(true);

        Button resetFiltersBtn = new Button("Reset");
        resetFiltersBtn.setStyleName(ValoTheme.BUTTON_TINY);
        btnLayout.addComponent(resetFiltersBtn);

        resetFiltersBtn.setDescription("Reset all groups to default");
        resetFiltersBtn.addClickListener((Button.ClickEvent event) -> {
            groupILayout.resetToDefault();
            groupIILayout.resetToDefault();
        });
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyleName(ValoTheme.BUTTON_TINY);
        btnLayout.addComponent(cancelBtn);
        cancelBtn.addClickListener((Button.ClickEvent event) -> {
            popupWindow.view();
            groupILayout.resetToDefault();
            groupIILayout.resetToDefault();

        });

        Button applyFilters = new Button("Apply");
        applyFilters.setDescription("Update the datasets");
        applyFilters.setStyleName(ValoTheme.BUTTON_TINY);

        btnLayout.addComponent(applyFilters);
        applyFilters.addClickListener((Button.ClickEvent event) -> {
            ReorderSelectGroupsFilter.this.updateSystem(groupILayout.getSortedSet(), groupIILayout.getSortedSet());
            popupWindow.view();
        });

        bottomContainert.addComponent(btnLayout);
        bottomContainert.setComponentAlignment(btnLayout, Alignment.TOP_RIGHT);

        diseaseGroupsContaioner.addComponent(groupILayout);

        diseaseGroupsContaioner.addComponent(groupIILayout);

        Property.ValueChangeListener selectionChangeListenet = (Property.ValueChangeEvent event) -> {
            if (groupILayout.isSingleSelected()) {
                groupIILayout.setEnableSelection(true);
            } else {
                groupIILayout.selectAndHideUnselected(null, false);
                return;
            }

            Set<HeatMapHeaderCellInformationBean> updatedGroupIISet = filterPatGroup2List(groupILayout.getSelectionSet());
            groupIILayout.selectAndHideUnselected(updatedGroupIISet, false);
        };
        groupILayout.addSelectionValueChangeListener(selectionChangeListenet);

    }

    private final Map<String, HeatMapHeaderCellInformationBean> fullCellInfoMap = new HashMap<>();

    /**
     * Update the data with disease groups name and information
     *
     * @param rowHeaders
     * @param colHeaders
     * @param patientsGroupComparisonsSet
     */
    public void updateData(LinkedHashSet<HeatMapHeaderCellInformationBean> rowHeaders, LinkedHashSet<HeatMapHeaderCellInformationBean> colHeaders, Set<DiseaseGroupComparison> patientsGroupComparisonsSet) {

        this.patientsGroupComparisonsSet = patientsGroupComparisonsSet;
        fullCellInfoMap.clear();
        rowHeaders.stream().forEach((cell) -> {
            fullCellInfoMap.put(cell.getDiseaseGroupName() + "__" + cell.getDiseaseCategory(), cell);
        });
        colHeaders.stream().forEach((cell) -> {
            fullCellInfoMap.put(cell.getDiseaseGroupName() + "__" + cell.getDiseaseCategory(), cell);
        });
        groupILayout.updateData(rowHeaders);
        groupIILayout.updateData(colHeaders);

        popupBody.setHeight(groupILayout.getFinalHeight() + 70, Unit.PIXELS);
        popupWindow.setFrameHeight((int)groupILayout.getFinalHeight() + 250 - 35);

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        popupWindow.view();
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

    /**
     * Update the heat-map based on user selection and actions
     *
     * @param rowHeaders
     * @param colHeaders
     */
    public abstract void updateSystem(LinkedHashSet<HeatMapHeaderCellInformationBean> rowHeaders, LinkedHashSet<HeatMapHeaderCellInformationBean> colHeaders);

    public void resizeFilter(double resizeFactor) {
        this.setWidth((int) (25 * resizeFactor), Unit.PIXELS);
        this.setHeight((int) (25 * resizeFactor), Unit.PIXELS);
    }
}
