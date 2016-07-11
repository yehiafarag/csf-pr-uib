/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.components.heatmapsubcomponents;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.VerticalLayout;
import java.util.Map;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;
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
//    private final Label valueLabel;
    private boolean selected = false;
    private boolean combinedHeader = false;
    private final String cellColor;

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
     * @param cellColor
     * @param dsIndexes
     * @param rowLabelIndex
     * @param colLabelIndex
     * @param tooltipLayout
     * @param updatedComparisonTitile
     * @param heatmapCellWidth
     * @param publicationsNumber
     */
    public HeatmapCell(double value, final String cellColor,String diseaseCategoryColor, Map<Integer,QuantDatasetObject> datasetMap, final int rowLabelIndex, final int colLabelIndex, VerticalLayout tooltipLayout, int heatmapCellWidth, int publicationsNumber, String updatedComparisonTitile, String fullCompTitle,String oreginalComparisonTitle, String diseaseCategory,String diseaseCategoryStyle) {

        
        this.colLabelIndex = colLabelIndex;
        this.rowLabelIndex = rowLabelIndex;
        
        this.value = value;

        this.diseaseCategory = diseaseCategory;
        this.comparison = new QuantDiseaseGroupsComparison();
        comparison.setDiseaseCategory(diseaseCategory);
        comparison.setComparisonFullName(fullCompTitle);
        comparison.setComparisonHeader(updatedComparisonTitile);
        comparison.setOreginalComparisonHeader(oreginalComparisonTitle);
        comparison.setDiseaseCategoryColor(diseaseCategoryColor);
        comparison.setDatasetMap(datasetMap);
        comparison.setDiseaseCategoryStyle(diseaseCategoryStyle);
        strValue = "";
        pointer = "default";
        this.cellColor = cellColor;
        if (cellColor.equalsIgnoreCase("#EFF2FB") && value != 0) {
            strValue = ((int) value) + "";
            this.updateLabel(strValue);
            this.setDescription("Same type comparison ( " + fullCompTitle.replace("__" + diseaseCategory, "") + ")<br/>Disease: " + diseaseCategory + "<br/>#Datasets: " + strValue +"<br/>#Publications: " + publicationsNumber);
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

        if (value > 0 && !cellColor.equalsIgnoreCase("#EFF2FB")) {
            this.setDescription(fullCompTitle.replace("__" + diseaseCategory, "") + "<br/>Disease: " + diseaseCategory + " <br/>#Datasets: " + strValue +"<br/>#Publications: " + publicationsNumber);
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
        this.strValue = strValue;
        

//        valueLabel.setValue("<center><div  style='background-color:" + cellColor + "; background-position: center;cursor:" + pointer + "; '>" + strValue + "</div><center>");

    }

    public abstract void selectData(HeatmapCell cell);

    public abstract void unSelectData(HeatmapCell cell);

    public String getDiseaseCategory() {
        return diseaseCategory;
    }

    public String getCellColor() {
        return cellColor;
    }

}
