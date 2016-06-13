/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import java.util.HashSet;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;

/**
 *
 * @author Yehia Farag
 *
 * this class represents column header that have main sorting functions
 */
public abstract class ColumnHeaderLayout extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private final VerticalLayout sortingBtn;
    private final ColumnFilterPopupBtn filterBtn;

    private final int index;

    public ColumnHeaderLayout(QuantDiseaseGroupsComparison comparison, int index) {
        this.setSizeFull();
        this.index = index;

        VerticalLayout headerFrame = new VerticalLayout();
        headerFrame.setHeight(100, Unit.PERCENTAGE);
        headerFrame.setWidthUndefined();
        this.addComponent(headerFrame);
        this.setComponentAlignment(headerFrame, Alignment.TOP_CENTER);

        filterBtn = new ColumnFilterPopupBtn() {

            @Override
            public void dropComparison() {
                ColumnHeaderLayout.this.dropComparison(comparison);
            }

            @Override
            public void filterTable(Set<Object> filtersSet) {
                ColumnHeaderLayout.this.filterTable(comparison, index, filtersSet);
            }

        };
        filterBtn.setWidth(20, Unit.PIXELS);
        filterBtn.setHeight(100, Unit.PERCENTAGE);
        filterBtn.setStyleName(comparison.getDiseaseCategoryStyle());
        filterBtn.addStyleName("unselectedfilter");
        headerFrame.addComponent(filterBtn);
        headerFrame.setComponentAlignment(filterBtn, Alignment.TOP_CENTER);
        filterBtn.setVisible(false);

        sortingBtn = new VerticalLayout();

        sortingBtn.setWidth(20, Unit.PIXELS);
        sortingBtn.setHeight(100, Unit.PERCENTAGE);
        sortingBtn.setStyleName(comparison.getDiseaseCategoryStyle());
        sortingBtn.setDescription(comparison.getComparisonHeader().split(" / ")[0].split("__")[0] + " / " + comparison.getComparisonHeader().split(" / ")[1].split("__")[0]);
        headerFrame.addComponent(sortingBtn);
        headerFrame.setComponentAlignment(sortingBtn, Alignment.TOP_CENTER);
        sortingBtn.addLayoutClickListener(ColumnHeaderLayout.this);
        sortingBtn.addStyleName("sortdown");
        sortingBtn.addStyleName("unselected");

    }

    public void swichBtns() {
        filterBtn.setVisible(!filterBtn.isVisible());
        sortingBtn.setVisible(!sortingBtn.isVisible());
        if (sortingBtn.getStyleName().contains("blinkII")) {
            sortingBtn.removeStyleName("blinkII");
            sortingBtn.addStyleName("blink");
        } else {
            sortingBtn.removeStyleName("blinkI");
            sortingBtn.addStyleName("blinkII");
        }

        if (filterBtn.getStyleName().contains("blinkII")) {
            filterBtn.removeStyleName("blinkII");
            filterBtn.addStyleName("blink");
        } else {
            filterBtn.removeStyleName("blinkI");
            filterBtn.addStyleName("blinkII");
        }

    }

    public void setAsDefault() {
        sortingBtn.removeStyleName("unselected");
        sortedUp = true;
    }
    private Boolean sortedUp;

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (sortedUp == null || sortedUp) {
            sortingBtn.addStyleName("sortdown");
            sortedUp = false;

        } else if (!sortedUp) {
            sortingBtn.removeStyleName("sortdown");
            sortingBtn.addStyleName("sortup");
            sortedUp = true;
        }
        sortingBtn.removeStyleName("unselected");
        sort(sortedUp, index);
    }

    public void noSort() {
        sortingBtn.addStyleName("sortdown");
        sortingBtn.removeStyleName("sortup");
        sortingBtn.addStyleName("unselected");
        sortedUp = null;
    }

    public void noFilter() {
        filterBtn.addStyleName("unselectedfilter");
        filterBtn.removeStyleName("selectedfilter");
    }

    public abstract void sort(boolean up, int index);

    public abstract void dropComparison(QuantDiseaseGroupsComparison comparison);

    public abstract void filterTable(QuantDiseaseGroupsComparison comparison, int comparisonIndex, Set<Object> filterSet);

}