/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;

/**
 *
 * @author Yehia Farag
 *
 * this class represents column header that have main sorting functions
 */
public abstract class ColumnHeaderLayout extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private final VerticalLayout comparisonLabel;
    private final int index;

    public ColumnHeaderLayout(QuantDiseaseGroupsComparison comparison, int index) {
        this.setSizeFull();
        this.index = index;
        comparisonLabel = new VerticalLayout();
        comparisonLabel.setWidth(20, Unit.PIXELS);
        comparisonLabel.setHeight(100, Unit.PERCENTAGE);
        comparisonLabel.setStyleName(comparison.getDiseaseCategoryStyle());
        comparisonLabel.setDescription(comparison.getComparisonHeader().split(" / ")[0].split("__")[0] + " / " + comparison.getComparisonHeader().split(" / ")[1].split("__")[0]);
        this.addComponent(comparisonLabel);
        this.setComponentAlignment(comparisonLabel, Alignment.TOP_CENTER);

        comparisonLabel.addLayoutClickListener(ColumnHeaderLayout.this);

    }
    private Boolean sortedUp;

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (sortedUp == null || sortedUp) {
            comparisonLabel.addStyleName("sortdown");
            sortedUp = false;

        } else if (!sortedUp) {
            comparisonLabel.removeStyleName("sortdown");
            comparisonLabel.addStyleName("sortup");
            sortedUp = true;
        }
        sort(sortedUp, index);
    }

    public void noSort() {       
        System.out.println("at no sort "+index);
        comparisonLabel.removeStyleName("sortdown");
        comparisonLabel.removeStyleName("sortup");
        sortedUp = null;
    }

    public abstract void sort(boolean up, int index);

}
