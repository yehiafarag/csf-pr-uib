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
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;

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
    private final HeatMapComponent parentcom;
    private final String title;
    private final String allStyle;
    private final String fullName;

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
    public HeaderCell(boolean rowHeader, String title, int index, HeatMapComponent parentcom, int heatmapCellWidth, int heatmapHeaderCellWidth, String fullName) {
        this.parentcom = parentcom;
        valueLabel = new Label();
        this.title = title;

//        if (all) {
        allStyle = title.split("\n")[1].toLowerCase().replace(" ", "").replace("'s", "");
//            valueLabel.setValue("<center><font>" + title.split("\n")[0] + "</font></br><font size='1' color='#003e99'>(" + title.split("\n")[1] + ")</font></center>");

//        }
//    else {
//            allStyle = "";
        valueLabel.setValue("<center><font>" + title.split("\n")[0] + "</font></center>");

//        }
        if (rowHeader) {
            this.cellStyleName = "hmrowlabel";
//            this.setStyleName("hmrowlabel");
        } else {
            this.cellStyleName = "hmcolumnlabel";
//            this.setStyleName("hmcolumnlabel");
        }
        valueLabel.setStyleName(allStyle + cellStyleName);
//        super("<b>" + title + "</b>");
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
            this.fullName = title;
        } else {
            this.fullName = fullName;
        }

        this.setDescription(title.split("\n")[0]);

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
//            unSelectCellStyle();
            parentcom.removeRowSelectedDs(title);

        } else {
            selected = true;
//            selectCellStyle();
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
            valueLabel.setValue("<center><font>" + title.split("\n")[0] + "*</font></center>");
            this.setDescription(title.split("\n")[0] + " (" + "Combined group)");
        }
        this.includedCells.add(cell);
    }

}
