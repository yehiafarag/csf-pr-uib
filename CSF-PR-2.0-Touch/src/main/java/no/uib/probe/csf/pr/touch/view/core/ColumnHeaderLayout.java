/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.shared.ui.label.ContentMode;
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
public class ColumnHeaderLayout extends VerticalLayout {

    public ColumnHeaderLayout(QuantDiseaseGroupsComparison comparison) {
        this.setSizeFull();
        this.setStyleName("hideoverflowtext");
        this.addStyleName("border");

        HorizontalLayout labelFrame = new HorizontalLayout();
        this.addComponent(labelFrame);
        labelFrame.setWidthUndefined();
        Label comparisonLabel = new Label("<font size='2' color='" + comparison.getDiseaseCategoryColor() + "'style='font-weight: bold;'>" + comparison.getComparisonHeader().split(" / ")[0].split("__")[0] + "<br>" + comparison.getComparisonHeader().split(" / ")[1].split("__")[0] + "</font>");
        comparisonLabel.setContentMode(ContentMode.HTML);
//        comparisonLabel.setStyleName("hideoverflowtext");
        comparisonLabel.setSizeFull();
        labelFrame.addComponent(comparisonLabel);

        VerticalLayout buttonsContainer = new VerticalLayout();
        buttonsContainer.setWidth(20, Unit.PIXELS);
        buttonsContainer.setHeight(100, Unit.PERCENTAGE);
        buttonsContainer.setStyleName("lightbluelayout");
        labelFrame.addComponent(buttonsContainer);

    }

}
