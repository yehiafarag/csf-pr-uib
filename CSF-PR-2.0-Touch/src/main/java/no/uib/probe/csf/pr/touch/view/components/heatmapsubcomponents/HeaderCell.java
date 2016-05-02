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
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;

/**
 * this class give the control on the header label style and events
 *
 * @author Yehia Farag
 */
public class HeaderCell extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private final int index;
    private String cellStyleName;
    private String selectStyle = "";
    private final Label valueLabel;

    /**
     *
     * @return
     */
    public List<HeatmapCell> getIncludedCells() {
        return includedCells;
    }

    /**
     *
     * @return
     */
    public Set<QuantDiseaseGroupsComparison> getIncludedComparisons() {
        return includedComparisons;
    }

    /**
     *
     * @return
     */
    public String getValueLabel() {
        return title;
    }
    private boolean selected = false;
    private Set<QuantDiseaseGroupsComparison> includedComparisons = new LinkedHashSet<QuantDiseaseGroupsComparison>();
    private List<HeatmapCell> includedCells = new ArrayList<HeatmapCell>();
    private HeatMapLayout parentcom;
    private String title;
    private String allStyle;
    private String fullName;
    private String color;

    /**
     *
     * @param rotate
     * @param title
     * @param index
     * @param parentcom
     * @param heatmapCellWidth
     * @param heatmapHeaderCellWidth
     * @param fullName
     */
    public HeaderCell(boolean rotate, String title, int index, HeatMapLayout parentcom, int headerWidth, int headerHeight, String fullName) {
        this.parentcom = parentcom;
        if (rotate) {
            this.addStyleName("rotateheader");
            this.setHeight(headerWidth + "px");
            this.setWidth(headerHeight + "px");
        } else {
            this.setWidth(headerWidth + "px");
            this.setHeight(headerHeight + "px");
        }
        this.title = title;
        this.addStyleName("hmheadercell");

        valueLabel = new Label();
        allStyle = "hm" + title.split("__")[2];
        valueLabel.setValue("<center><font>" + title.split("__")[0] + "</font></center>");
        valueLabel.setStyleName(allStyle);
        valueLabel.setWidth(100, Unit.PERCENTAGE);
        valueLabel.setHeight(100, Unit.PERCENTAGE);
        this.valueLabel.setContentMode(ContentMode.HTML);
        this.addComponent(valueLabel);
        this.setComponentAlignment(valueLabel, Alignment.TOP_CENTER);

        this.index = index;

//        this.addLayoutClickListener(HeaderCell.this);
        if (fullName == null) {
            this.fullName = title.split("__")[0];
        } else {
            this.fullName = fullName;
        }
        String combinedGroup = "";
        if (this.fullName.contains("*")) {
            combinedGroup = " - Combined disease groups";
        }
        this.setDescription(this.fullName + combinedGroup);

    }

    public String getColor() {
        return color;
    }

    /**
     *
     */
    public void heighlightCellStyle() {

//        valueLabel.setStyleName(cellStyleName + selectStyle + "_heighlightcell");
    }

    /**
     *
     */
    public void resetCellStyle() {
//        this.setStyleName(cellStyleName + selectStyle);

    }

    /**
     *
     */
    public void selectCellStyle() {
        selectStyle = "_selected";
//        this.setStyleName(cellStyleName + selectStyle);

    }

    /**
     *
     */
    public void unSelectCellStyle() {
        selectStyle = "";
//        this.setStyleName(cellStyleName);

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (selected) {
            selected = false;
//            parentcom.removeRowSelectedDs(title);

        } else {
            selected = true;
            parentcom.addRowSelectedDs(title);
        }
    }

    private boolean combinedHeader = false;

    /**
     *
     * @param groupComp
     * @param cell
     */
    public void addComparison(QuantDiseaseGroupsComparison groupComp, HeatmapCell cell) {
        this.includedComparisons.add(groupComp);
        if (!combinedHeader && cell.isCombinedHeader()) {
            combinedHeader = true;
            valueLabel.setValue("<center><font>" + title.split("\n")[0] + "</font></center>");
            this.setDescription(title.split("\n")[0] + " (" + "Combined group)");
        }
        if (!cell.isCombinedHeader()) {
            this.includedCells.add(cell);
        }
    }

}
