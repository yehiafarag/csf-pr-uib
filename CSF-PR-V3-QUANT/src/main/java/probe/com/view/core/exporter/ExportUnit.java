/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core.exporter;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 *
 * @author Yehia Farag
 */
public class ExportUnit extends VerticalLayout {

    private final OptionGroup typeGroup, exportGroup;
    private final HorizontalLayout topLayout, bottomLayout;
    private final Button exportBtn;

    /**
     *
     */
    public ExportUnit() {

        topLayout = new HorizontalLayout();
        bottomLayout = new HorizontalLayout();

        // init layout
        this.addStyleName(Reindeer.LAYOUT_BLUE);
        this.setHeight("120px");
        this.setWidth("200px");
        this.setSpacing(true);
        this.setMargin(false);

        topLayout.setWidth("100%");
        topLayout.setHeight("80px");
        topLayout.setMargin(true);
        this.addComponent(topLayout);
        this.setComponentAlignment(topLayout, Alignment.TOP_CENTER);

        bottomLayout.setWidth("100%");
        bottomLayout.setHeight("40px");
        bottomLayout.setMargin(true);
        this.addComponent(bottomLayout);
        this.setComponentAlignment(bottomLayout, Alignment.MIDDLE_CENTER);

        typeGroup = new OptionGroup("");
        // Use the single selection mode.
        typeGroup.setMultiSelect(false);
        typeGroup.addItem("Validated Only");
        typeGroup.addItem("All");
        typeGroup.select("Validated Only");
        topLayout.addComponent(typeGroup);
        topLayout.setComponentAlignment(typeGroup, Alignment.MIDDLE_CENTER);
        topLayout.setExpandRatio(typeGroup, 0.5f);

        exportGroup = new OptionGroup(""); // Use the single selection mode.       
        exportGroup.setMultiSelect(false);
        exportGroup.addItem("csv");
        exportGroup.addItem("xls");
        exportGroup.select("csv");
        topLayout.addComponent(exportGroup);
        topLayout.setComponentAlignment(exportGroup, Alignment.MIDDLE_CENTER);
        topLayout.setExpandRatio(exportGroup, 0.5f);

        exportBtn = new Button("Export");
        exportBtn.setStyleName(Reindeer.BUTTON_SMALL);
        bottomLayout.addComponent(exportBtn);
        bottomLayout.setComponentAlignment(exportBtn, Alignment.BOTTOM_CENTER);

    }

    /**
     *
     * @param clickListener
     */
    public void addClickListener(Button.ClickListener clickListener) {
        exportBtn.addClickListener(clickListener);

    }

    /**
     *
     * @return
     */
    public String getTypeGroupValue() {
        return typeGroup.getValue().toString();
    }

    /**
     *
     * @return
     */
    public String getExportGroupValue() {
        return exportGroup.getValue().toString();
    }

    /**
     *
     * @param visible
     */
    public void typeGroupOptionVisible(boolean visible) {
        this.typeGroup.setVisible(visible);

    }

}
