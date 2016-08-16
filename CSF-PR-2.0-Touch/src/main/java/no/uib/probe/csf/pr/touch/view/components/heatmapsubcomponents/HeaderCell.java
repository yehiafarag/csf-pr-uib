/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.components.heatmapsubcomponents;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
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
public abstract class HeaderCell extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private final int index;
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
        return title + "__" + diseaseCategory;
    }
    private boolean selected = false;
    private final Set<QuantDiseaseGroupsComparison> includedComparisons = new LinkedHashSet<>();
    private final List<HeatmapCell> includedCells;
    private final String title;
//    private final String fullName;
    private String color;
    private final String diseaseCategory;
    private final boolean rotate;

    /**
     *
     * @param rotate
     * @param title
     * @param diseaseStyle
     * @param index
     * @param headerWidth
     * @param headerHeight
     * @param fullDiseaseGroupName
     */
    public HeaderCell(String title, int index, String fullDiseaseGroupName, String diseaseCategory, boolean rotate) {
        this.includedCells = new ArrayList<>();
        this.diseaseCategory = diseaseCategory;
        this.rotate = rotate;
        this.title = title;

        valueLabel = new Label();

        String allStyle = "hm" + diseaseCategory.toLowerCase().replace("'", "").replace(" ", "") + "style";

        valueLabel.setValue(title);
        valueLabel.setStyleName(allStyle);
        valueLabel.setWidth(100, Unit.PERCENTAGE);
        valueLabel.setHeight(100, Unit.PERCENTAGE);
        if (rotate) {
            valueLabel.addStyleName("rotateheader");
        }
        valueLabel.addStyleName(ValoTheme.LABEL_SMALL);
        valueLabel.addStyleName(ValoTheme.LABEL_TINY);
        this.addComponent(valueLabel);
        this.setComponentAlignment(valueLabel, Alignment.TOP_CENTER);

        this.index = index;
        String fullName;
        this.addLayoutClickListener(HeaderCell.this);
        if (fullDiseaseGroupName == null) {
            fullName = title;
        } else {
            fullName = fullDiseaseGroupName;
        }
        String combinedGroup = "";
        if (fullName.contains("*")) {
            combinedGroup = " - Combined disease groups";
        }
        this.setDescription(fullName + combinedGroup);

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (selected) {
            selected = false;
            unselect();
            unSelectData(getValueLabel());
        } else {
            selected = true;
            select();
            selectData(getValueLabel());
        }
    }

    public void select() {
        this.addStyleName("hmselectedcell");

    }

    public void unselect() {

        this.removeStyleName("hmselectedcell");

    }

    public String getColor() {
        return color;
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
            this.setDescription(title + " (" + "Combined group)");
        }
        if (!cell.isCombinedHeader()) {
            this.includedCells.add(cell);
        }
    }

    public abstract void selectData(String cellheader);

    public abstract void unSelectData(String cellHeader);

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled) {
            this.removeStyleName("disabled");
        } else {
            this.addStyleName("disabled");
        }
    }

    @Override
    public void setHeight(float height, Unit unit) {
        super.setHeight(height, unit); //To change body of generated methods, choose Tools | Templates.

        if (rotate) {
            valueLabel.setWidth(height - 2, unit);
        } else {
            if (height < 15) {
                valueLabel.addStyleName("xxsmallfont");
            } else if (height >= 15 && height < 20) {
                valueLabel.addStyleName("xsmallfont");
            } else {
                valueLabel.addStyleName("smallfont");
            }
        }
    }

    @Override
    public void setWidth(float width, Unit unit) {
        super.setWidth(width, unit); //To change body of generated methods, choose Tools | Templates
        if (rotate) {
             if (width < 15) {
            valueLabel.addStyleName("xxsmallfont");
        } else if (width >= 15 && width < 20) {
            valueLabel.addStyleName("xsmallfont");
        } else {
            valueLabel.addStyleName("smallfont");
        }
            valueLabel.setHeight(width - 2, unit);
        }
    }

}
