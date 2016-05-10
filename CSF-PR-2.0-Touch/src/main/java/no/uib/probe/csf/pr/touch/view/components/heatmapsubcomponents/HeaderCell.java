/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.components.heatmapsubcomponents;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ClientMethodInvocation;
import com.vaadin.server.ErrorHandler;
import com.vaadin.server.Extension;
import com.vaadin.server.Resource;
import com.vaadin.server.ServerRpcManager;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.communication.ClientRpc;
import com.vaadin.shared.communication.ServerRpc;
import com.vaadin.shared.communication.SharedState;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import elemental.json.JsonObject;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
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
        return title;
    }
    private boolean selected = false;
    private final Set<QuantDiseaseGroupsComparison> includedComparisons = new LinkedHashSet<>();
    private final List<HeatmapCell> includedCells;
    private final String title;
    private final String fullName;
    private String color;

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
    public HeaderCell(boolean rotate, String title, String diseaseStyle, int index, int headerWidth, int headerHeight, String fullDiseaseGroupName) {
        this.includedCells = new ArrayList<>();
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
        String allStyle = "hm" + diseaseStyle;
        valueLabel.setValue("<center><font>" + title + "</font></center>");
        valueLabel.setStyleName(allStyle);
        valueLabel.setWidth(100, Unit.PERCENTAGE);
        valueLabel.setHeight(100, Unit.PERCENTAGE);
        this.valueLabel.setContentMode(ContentMode.HTML);
        this.addComponent(valueLabel);
        this.setComponentAlignment(valueLabel, Alignment.TOP_CENTER);
        
        this.index = index;
        
        this.addLayoutClickListener(HeaderCell.this);
        if (fullDiseaseGroupName == null) {
            this.fullName = title;
        } else {
            this.fullName = fullDiseaseGroupName;
        }
        String combinedGroup = "";
        if (this.fullName.contains("*")) {
            combinedGroup = " - Combined disease groups";
        }
        this.setDescription(this.fullName + combinedGroup);
        
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
            valueLabel.setValue("<center><font>" + title + "</font></center>");
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
    
}
