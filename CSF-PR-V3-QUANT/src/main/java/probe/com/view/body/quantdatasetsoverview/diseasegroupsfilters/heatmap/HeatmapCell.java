/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.heatmap;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.marcus.MouseEvents;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;

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
    private final HeatMapComponent parent;
//    private final MouseEvents.MouseOverListener mouseOverListener;
//    private final MouseEvents.MouseOutListener mouseOutListener;
    private String strValue;
    private String pointer;
    private String defaultStyle = "initheatmapcoloredcell";
    private final Label valueLabel;
    private boolean selected = false;

    /**
     *
     * @return
     */
    public QuantDiseaseGroupsComparison getComparison() {
        return comparison;
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
     */
    public HeatmapCell(double value, final String color, int[] dsIndexes, final int rowLabelIndex, final int colLabelIndex, VerticalLayout tooltipLayout, HeatMapComponent parentcom, String groupCompTitle, int heatmapCellWidth, int publicationsNumber) {

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
        comparison.setComparisonHeader(groupCompTitle);
        comparison.setRgbStringColor(color);
        comparison.setDatasetIndexes(dsIndexes);
//        mouseOverListener = new MouseEvents.MouseOverListener() {
//
//            @Override
//            public void mouseOver() {
//
//                parent.highlightHeaders(colLabelIndex, rowLabelIndex);
////                setValue("<div style='background-color: " + heighlightColor + " ;height:50px;width:50px; cursor:" + pointer + "; '><b>" + strValue + "</b></div>");
//
//            }
//        };
//
//        mouseOutListener = new MouseEvents.MouseOutListener() {
//
//            @Override
//            public void mouseOut() {
//                if (selected) {
//                    return;
//                }
//                parent.resetHeadersStyle(colLabelIndex, rowLabelIndex);
////                setValue("<div style='background-color:" + color + ";height:50px;width:50px; cursor:" + pointer + "; '><b>" + strValue + "</b></div>");
//            }
//        };

        if (value != 0) {
            strValue = ((int) value) + "";
            pointer = "pointer";
//            final MouseEvents mouseEvents = MouseEvents.enableFor(valueLabel);
//            mouseEvents.addMouseOutListener(mouseOutListener);
//            mouseEvents.addMouseOverListener(mouseOverListener);
            this.addLayoutClickListener(HeatmapCell.this);

        }
        valueLabel.setContentMode(ContentMode.HTML);
        valueLabel.setPrimaryStyleName(defaultStyle);
        valueLabel.setValue("<div  align='center' style='background-color:" + color + "; background-position: center;height:" + (heatmapCellWidth - 4) + "px;width:" + (heatmapCellWidth - 4) + "px; cursor:" + pointer + "; '>" + strValue + "</div>");
        this.addComponent(valueLabel);
        this.setComponentAlignment(valueLabel, Alignment.MIDDLE_CENTER);
        if (value > 0) {
        String updatedHeader = groupCompTitle.split(" / ")[0].split("\n")[0] + " / " + groupCompTitle.split(" / ")[1].split("\n")[0] + " ( " + groupCompTitle.split(" / ")[1].split("\n")[1] + " )";

            this.setDescription("<h3>"+updatedHeader+"</h3><h3 style='font-size:14px;line-height:100%;font-weight: normal; '>" + strValue + (value == 1 ? " study" : " studies") + " </h3><h3 style='font-size:14px;line-height:100%;font-weight: normal; '>" + publicationsNumber + (publicationsNumber == 1 ? " publication" : " publications") + " </h3>");
        }

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (defaultStyle.equalsIgnoreCase("selectedheatmapcoloredcell")) {
            parent.unSelectDs(this.comparison, this);
            parent.resetHeadersStyle(colLabelIndex, rowLabelIndex);
//            unselect();
        } else {
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
     */
    public void select() {
//        selected = false;
//        parent.resetHeadersStyle(colLabelIndex, rowLabelIndex);
        defaultStyle = "selectedheatmapcoloredcell";
        valueLabel.setPrimaryStyleName(defaultStyle);

    }

    public void initStyle() {
        defaultStyle = "initheatmapcoloredcell";
        valueLabel.setPrimaryStyleName("initheatmapcoloredcell");

    }

}
