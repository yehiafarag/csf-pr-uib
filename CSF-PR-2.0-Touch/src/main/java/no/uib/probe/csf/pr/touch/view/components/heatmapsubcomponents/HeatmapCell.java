/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.components.heatmapsubcomponents;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;

/**
 * This class represents heat map cell
 *
 * @author yehia Farag
 */
public abstract class HeatmapCell extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private final double value;
    private final int rowLabelIndex;
    private final int colLabelIndex;

    /**
     *
     * @return
     */
    public double getValue() {
        return value;
    }
    private String strValue;
    private String pointer;
    private final Label valueLabel;
    private boolean selected = false;
    private boolean combinedHeader = false;
    private final String color;

    /**
     *
     * @return
     */
    public QuantDiseaseGroupsComparison getComparison() {
        return comparison;
    }

    public boolean isCombinedHeader() {
        return combinedHeader;
    }

    private final QuantDiseaseGroupsComparison comparison;
    private final String diseaseCategory;

    /**
     *
     * @param value
     * @param color
     * @param dsIndexes
     * @param rowLabelIndex
     * @param colLabelIndex
     * @param tooltipLayout
     * @param groupCompTitle
     * @param heatmapCellWidth
     * @param publicationsNumber
     */
    public HeatmapCell(double value, final String color, int[] dsIndexes, final int rowLabelIndex, final int colLabelIndex, VerticalLayout tooltipLayout, String groupCompTitle, int heatmapCellWidth, int publicationsNumber, String fullCompTitle, String diseaseCategory) {

        
        this.colLabelIndex = colLabelIndex;
        this.rowLabelIndex = rowLabelIndex;
        this.setStyleName("hmbodycell");
        this.value = value;
        this.setWidth(heatmapCellWidth, Unit.PIXELS);
        this.setHeight(30, Unit.PIXELS);

        this.diseaseCategory = diseaseCategory;
        this.comparison = new QuantDiseaseGroupsComparison();
        comparison.setComparisonFullName(fullCompTitle);
        comparison.setComparisonHeader(groupCompTitle);
        comparison.setOreginalComparisonHeader(groupCompTitle);
        comparison.setRgbStringColor(color);
        comparison.setDatasetIndexes(dsIndexes);
        this.valueLabel = new Label();
        valueLabel.setWidth(100, Unit.PERCENTAGE);
        valueLabel.setHeight(100, Unit.PERCENTAGE);
        this.addComponent(valueLabel);
        this.setComponentAlignment(valueLabel, Alignment.MIDDLE_CENTER);
        valueLabel.setContentMode(ContentMode.HTML);
        strValue = "";
        pointer = "default";
        this.color = color;
        if (color.equalsIgnoreCase("#EFF2FB") && value != 0) {
            strValue = ((int) value) + "";
            this.updateLabel(strValue);
            this.setDescription("<h4>Same type comparison ( " + fullCompTitle.replace("__" + diseaseCategory, "") + ")</h4><h4>Disease: " + diseaseCategory + "</h4> <h4 style='font-size:14px;line-height:100%;font-weight: normal; '>" + strValue + (value == 1 ? " Dataset" : " Datasets") + " </h4><h4 style='font-size:14px;line-height:100%;font-weight: normal; '>" + publicationsNumber + (publicationsNumber == 1 ? " Publication" : " Publications") + " </h4>");
            comparison.setComparisonHeader(" / ");
            comparison.setOreginalComparisonHeader(" / ");
            combinedHeader = true;

        } else if (value != 0) {
            strValue = ((int) value) + "";
            pointer = "pointer";
            this.addLayoutClickListener(HeatmapCell.this);
            this.updateLabel(strValue);

        } else {
            this.updateLabel(strValue);
        }

        if (value > 0 && !color.equalsIgnoreCase("#EFF2FB")) {
            this.setDescription("<h4>   " + fullCompTitle.replace("__" + diseaseCategory, "") + "</h4><h4> Disease: " + diseaseCategory + " </h4>  <h4 style='font-size:11px;line-height:100%;font-weight: normal; '>" + strValue + (value == 1 ? " Dataset" : " Datasets") + " </h4><h4 style='font-size:11px;line-height:100%;font-weight: normal; '>" + publicationsNumber + (publicationsNumber == 1 ? " Publication" : " Publications") + " </h4>");
        }
    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (selected) {
            this.unselect();
            unSelectData(this);

        } else {
            this.select();
            selectData(this);
        }
    }

    /**
     *
     */
    public void unselect() {
        selected = false;
        this.removeStyleName("hmselectedcell");
        this.addStyleName("hmunselectedcell");

    }

    /**
     *
     */
    public void select() {
        selected = true;
        this.removeStyleName("hmunselectedcell");
        this.addStyleName("hmselectedcell");
    }

    public void initialState() {
        selected = false;
        this.removeStyleName("hmunselectedcell");
        this.removeStyleName("hmselectedcell");

    }

    public final void updateLabel(String strValue) {

        valueLabel.setValue("<center><div  style='background-color:" + color + "; background-position: center;cursor:" + pointer + "; '>" + strValue + "</div><center>");

    }

    public abstract void selectData(HeatmapCell cell);

    public abstract void unSelectData(HeatmapCell cell);

    public String getDiseaseCategory() {
        return diseaseCategory;
    }

    public String getColor() {
        return color;
    }

}
