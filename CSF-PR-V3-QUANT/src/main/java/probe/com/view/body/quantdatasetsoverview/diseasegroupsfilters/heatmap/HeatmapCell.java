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
public class HeatmapCell extends VerticalLayout implements LayoutEvents.LayoutClickListener{

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
    private final MouseEvents.MouseOverListener mouseOverListener;
    private final MouseEvents.MouseOutListener mouseOutListener;
    private String strValue;
    private String pointer;
    private String defaultStyle = "heatmapcoloredcell";
    private final Label valueLabel;
    private boolean selected =false;

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
     */
    public HeatmapCell(double value, final String color, int[] dsIndexes, final int rowLabelIndex, final int colLabelIndex, VerticalLayout tooltipLayout, HeatMapComponent parentcom, String groupCompTitle) {
      
        this.colLabelIndex = colLabelIndex;
        this.rowLabelIndex = rowLabelIndex;
        this.valueLabel = new Label();
        this.value = value;
       
        this.parent = parentcom;
        this.setWidth("50px");
        this.setHeight("50px");
        strValue = "";
        pointer = "default";
        this.comparison = new QuantDiseaseGroupsComparison();
        comparison.setComparisonHeader(groupCompTitle);
        comparison.setDatasetIndexes(dsIndexes);
        mouseOverListener = new MouseEvents.MouseOverListener() {

            @Override
            public void mouseOver() {

                
                parent.highlightHeaders(colLabelIndex, rowLabelIndex);
//                setValue("<div style='background-color: " + heighlightColor + " ;height:50px;width:50px; cursor:" + pointer + "; '><b>" + strValue + "</b></div>");

            }
        };

        mouseOutListener = new MouseEvents.MouseOutListener() {

            @Override
            public void mouseOut() {
                if (selected) {
                    return;
                }
                parent.resetHeadersStyle(colLabelIndex, rowLabelIndex);
//                setValue("<div style='background-color:" + color + ";height:50px;width:50px; cursor:" + pointer + "; '><b>" + strValue + "</b></div>");
            }
        };

        if (value != 0) {
            strValue = ((int) value) + "";
            pointer = "pointer";
            final MouseEvents mouseEvents = MouseEvents.enableFor(valueLabel);
            mouseEvents.addMouseOutListener(mouseOutListener);
            mouseEvents.addMouseOverListener(mouseOverListener);
            this.addLayoutClickListener(HeatmapCell.this);


        }
        valueLabel.setContentMode(ContentMode.HTML);
        valueLabel.setPrimaryStyleName(defaultStyle);
        valueLabel.setValue("<div style='background-color:" + color + "; background-position: center;height:46px;width:46px; cursor:" + pointer + "; '><b>" + strValue + "</b></div>");
        this.addComponent(valueLabel);
        this.setComponentAlignment(valueLabel,Alignment.MIDDLE_CENTER);
       
    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (defaultStyle.equalsIgnoreCase("selectedheatmapcoloredcell")) {
            parent.removeSelectedDs(this.comparison,this);
             parent.resetHeadersStyle(colLabelIndex, rowLabelIndex);
            unselect();
        } else {
//            selected = true;
            parent.addSelectedDs(this.comparison,this);
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

}
