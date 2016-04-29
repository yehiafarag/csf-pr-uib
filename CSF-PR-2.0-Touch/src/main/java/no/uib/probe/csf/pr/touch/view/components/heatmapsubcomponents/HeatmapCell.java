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
public class HeatmapCell extends VerticalLayout implements LayoutEvents.LayoutClickListener {

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
    private final HeatMapLayout parent;
//    private final MouseEvents.MouseOverListener mouseOverListener;
//    private final MouseEvents.MouseOutListener mouseOutListener;
    private String strValue;
    private String pointer;
    private String defaultStyle = "initheatmapcoloredcell";
    private final Label valueLabel;
    private boolean selected = false;
    private boolean combinedHeader = false;

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

    /**
     *
     * @param value
     * @param color
     * @param dsIndexes
     * @param rowLabelIndex
     * @param colLabelIndex
     * @param tooltipLayout
     * @param parentcom
     * @param groupCompTitle
     * @param heatmapCellWidth
     * @param publicationsNumber
     */
    public HeatmapCell(double value, final String color, int[] dsIndexes, final int rowLabelIndex, final int colLabelIndex, VerticalLayout tooltipLayout, HeatMapLayout parentcom, String groupCompTitle, int heatmapCellWidth, int publicationsNumber, String fullCompTitle) {

        this.colLabelIndex = colLabelIndex;
        this.rowLabelIndex = rowLabelIndex;
        this.valueLabel = new Label();
        this.value = value;

        this.parent = parentcom;
        this.setWidth(heatmapCellWidth + "px");
        this.setHeight(heatmapCellWidth + "px");
        strValue = "";
        pointer = "default";
        this.comparison = new QuantDiseaseGroupsComparison();
        comparison.setComparisonFullName(fullCompTitle);
        comparison.setComparisonHeader(groupCompTitle);
        comparison.setOreginalComparisonHeader(groupCompTitle);
        comparison.setRgbStringColor(color);
        comparison.setDatasetIndexes(dsIndexes);
        if (color.equalsIgnoreCase("#EFF2FB") && value != 0) {
            strValue = ((int) value) + "";
            String updatedHeader = groupCompTitle.split(" / ")[0].split("\n")[0] + " / " + groupCompTitle.split(" / ")[1].split("\n")[0] + " ( " + groupCompTitle.split(" / ")[1].split("\n")[1].replace("_", " ").replace("-", "'").replace("Disease", "") + " )";
            valueLabel.setValue("<center><div  style='background-color:" + color + "; background-position: center;height:" + (heatmapCellWidth - 4) + "px;width:" + (heatmapCellWidth - 4) + "px; cursor:" + pointer + "; '> <font Color='#4d749f'>(" + strValue + ")</font></div><center>");

            this.setDescription("<h3>Same type comparison ( " + updatedHeader + " )</h3><h3 style='font-size:14px;line-height:100%;font-weight: normal; '>" + strValue + (value == 1 ? " Dataset" : " Datasets") + " </h3><h3 style='font-size:14px;line-height:100%;font-weight: normal; '>" + publicationsNumber + (publicationsNumber == 1 ? " Publication" : " Publications") + " </h3>");
            comparison.setComparisonHeader(" / ");
            comparison.setOreginalComparisonHeader(" / ");
            combinedHeader = true;

        } else if (value != 0) {
            strValue = ((int) value) + "";
            pointer = "pointer";
//            final MouseEvents mouseEvents = MouseEvents.enableFor(valueLabel);
//            mouseEvents.addMouseOutListener(mouseOutListener);
//            mouseEvents.addMouseOverListener(mouseOverListener);
            this.addLayoutClickListener(HeatmapCell.this);
            valueLabel.setValue("<center><div  style='background-color:" + color + "; background-position: center;height:" + (heatmapCellWidth - 4) + "px;width:" + (heatmapCellWidth - 4) + "px; cursor:" + pointer + "; '>" + strValue + "</div><center>");

        } else {
            valueLabel.setValue("<center><div  style='background-color:" + color + "; background-position: center;height:" + (heatmapCellWidth - 4) + "px;width:" + (heatmapCellWidth - 4) + "px; cursor:" + pointer + "; '> " + strValue + "</div><center>");

        }
        valueLabel.setContentMode(ContentMode.HTML);
        valueLabel.setPrimaryStyleName(defaultStyle);
        this.addComponent(valueLabel);
        this.setComponentAlignment(valueLabel, Alignment.MIDDLE_CENTER);
        if (value > 0 && !color.equalsIgnoreCase("#EFF2FB")) {
            String updatedHeader = groupCompTitle.split(" / ")[0].split("\n")[0] + " / " + groupCompTitle.split(" / ")[1].split("\n")[0] + " - " + groupCompTitle.split(" / ")[1].split("\n")[1].replace("_", " ").replace("-", "'").replace("Disease", "") + "";

            this.setDescription("<h4>" + updatedHeader + "</h4><h4 style='font-size:11px;line-height:100%;font-weight: normal; '>" + strValue + (value == 1 ? " Dataset" : " Datasets") + " </h4><h4 style='font-size:11px;line-height:100%;font-weight: normal; '>" + publicationsNumber + (publicationsNumber == 1 ? " Publication" : " Publications") + " </h4>");
        }

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (defaultStyle.equalsIgnoreCase("selectedheatmapcoloredcell")) {
            parent.unSelectDs(this.comparison, this);
            parent.resetHeadersStyle(colLabelIndex, rowLabelIndex);
//            unselect();
        } else {
         comparison.resetComparisonHeader();
//            selected = true;
            parent.addSelectedDs(this.comparison, this);
            defaultStyle = "selectedheatmapcoloredcell";
            valueLabel.setPrimaryStyleName(defaultStyle);
        }
    }

    /**
     *
     */
    public void unselect() {
//        selected = false;
//        parent.resetHeadersStyle(colLabelIndex, rowLabelIndex);
        defaultStyle = "heatmapcoloredcell";
        valueLabel.setPrimaryStyleName(defaultStyle);

    }

    /**
     *
     * @param groupA
     */
    public void select(boolean groupA) {
//        selected = false;
//        parent.resetHeadersStyle(colLabelIndex, rowLabelIndex);
        defaultStyle = "selectedheatmapcoloredcell";
        valueLabel.setPrimaryStyleName(defaultStyle);
        if (groupA) {
        } else {

        }

    }

    public void initStyle() {
        defaultStyle = "initheatmapcoloredcell";
        valueLabel.setPrimaryStyleName("initheatmapcoloredcell");

    }

}
