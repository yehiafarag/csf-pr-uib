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
    private final String cellStyleName;
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
    private final Set<QuantDiseaseGroupsComparison> includedComparisons = new LinkedHashSet<QuantDiseaseGroupsComparison>();
    private final List<HeatmapCell> includedCells = new ArrayList<HeatmapCell>();
    private final HeatMapLayout parentcom;
    private final String title;
    private final String allStyle;
    private final String fullName;
    private final String color;

    /**
     *
     * @param rowHeader
     * @param title
     * @param index
     * @param parentcom
     * @param heatmapCellWidth
     * @param heatmapHeaderCellWidth
     * @param fullName
     */
    public HeaderCell(boolean rowHeader, String title, int index, HeatMapLayout parentcom, int heatmapCellWidth, int heatmapHeaderCellWidth, String fullName) {
        this.parentcom = parentcom;
        valueLabel = new Label();
        this.title = title;
        allStyle = title.split("\n")[1].toLowerCase().replace("-s", "").replace("_disease", "").replace("_", "");
        valueLabel.setValue("<center><font>" + title.split("\n")[0] + "</font></center>");
        if (allStyle.equalsIgnoreCase("multiplesclerosis")) {
            color = "#A52A2A";
        } else if (allStyle.equalsIgnoreCase("alzheimer")) {
            color = "#4b7865";
        } else {
            color = "#74716E";
        }

        if (rowHeader) {
            this.cellStyleName = "hmrowlabel";
        } else {
            this.cellStyleName = "hmcolumnlabel";
        }
        valueLabel.setStyleName(allStyle + cellStyleName);
        valueLabel.setWidth((heatmapHeaderCellWidth - 4) + "px");
        valueLabel.setHeight((heatmapCellWidth - 4) + "px");
        this.setStyleName(cellStyleName);
        this.setWidth(heatmapHeaderCellWidth + "px");
        this.setHeight(heatmapCellWidth + "px");
        this.valueLabel.setContentMode(ContentMode.HTML);
        this.index = index;

        this.addComponent(valueLabel);
        this.setComponentAlignment(valueLabel, Alignment.BOTTOM_CENTER);
        this.addLayoutClickListener(HeaderCell.this);
        if (fullName == null) {
            this.fullName = title.split("\n")[0].replace("_", "").replace("-", ",") ;
        } else {
            this.fullName = fullName;
        }
        String combinedGroup = "";
        if (this.fullName.contains("*")) {
            combinedGroup = " - Combined disease groups";
        }
        this.setDescription(this.fullName+ combinedGroup);

    }

    public String getColor() {
        return color;
    }

    /**
     *
     */
    public void heighlightCellStyle() {

        valueLabel.setStyleName(cellStyleName + selectStyle + "_heighlightcell");

    }

    /**
     *
     */
    public void resetCellStyle() {
        this.setStyleName(cellStyleName + selectStyle);

    }

    /**
     *
     */
    public void selectCellStyle() {
        selectStyle = "_selected";
        this.setStyleName(cellStyleName + selectStyle);

    }

    /**
     *
     */
    public void unSelectCellStyle() {
        selectStyle = "";
        this.setStyleName(cellStyleName);

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (selected) {
            selected = false;
            parentcom.removeRowSelectedDs(title);

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
