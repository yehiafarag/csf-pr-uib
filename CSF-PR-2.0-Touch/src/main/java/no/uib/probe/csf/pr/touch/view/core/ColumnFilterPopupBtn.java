/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.HashSet;
import java.util.Set;
import org.vaadin.teemu.VaadinIcons;

/**
 *
 * @author Yehia Farag
 *
 * this class allow users to filter the columns of the protein table or drop one
 * comparison
 *
 */
public abstract class ColumnFilterPopupBtn extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private final PopupView filterPopupLayout;

    public ColumnFilterPopupBtn() {

        this.addStyleName("unselectedfilter");

        VerticalLayout popupbodyLayout = new VerticalLayout();
        popupbodyLayout.setSpacing(true);
        popupbodyLayout.setWidth(200, Unit.PIXELS);
        popupbodyLayout.setMargin(new MarginInfo(false, false, true, true));
        popupbodyLayout.addStyleName("border");

        CloseButton closePopup = new CloseButton();
        closePopup.setWidth(10, Unit.PIXELS);
        closePopup.setHeight(10, Unit.PIXELS);
        popupbodyLayout.addComponent(closePopup);
        popupbodyLayout.setComponentAlignment(closePopup, Alignment.TOP_RIGHT);
        closePopup.addStyleName("translateleft10");

        final OptionGroup tableHeaderFilterOptions = new OptionGroup("Select Filter");
        popupbodyLayout.addComponent(tableHeaderFilterOptions);
        tableHeaderFilterOptions.setWidth(100, Unit.PERCENTAGE);
        tableHeaderFilterOptions.setStyleName(ValoTheme.OPTIONGROUP_SMALL);
        tableHeaderFilterOptions.setDescription("Select to filter the table");
        tableHeaderFilterOptions.addItem(4);
        tableHeaderFilterOptions.setItemCaption(4, "Increased 100%");
        tableHeaderFilterOptions.addItem(3);
        tableHeaderFilterOptions.setItemCaption(3, "Increased < 100%");
        tableHeaderFilterOptions.addItem(2);
        tableHeaderFilterOptions.setItemCaption(2, "Equal");
        tableHeaderFilterOptions.addItem(1);
        tableHeaderFilterOptions.setItemCaption(1, "Decreased < 100%");
        tableHeaderFilterOptions.addItem(0);
        tableHeaderFilterOptions.setItemCaption(0, "Decreased 100%");
        tableHeaderFilterOptions.addItem(5);
        tableHeaderFilterOptions.setItemCaption(5, "No Quant Information");
        tableHeaderFilterOptions.setNullSelectionAllowed(true);
        tableHeaderFilterOptions.setImmediate(true);
        tableHeaderFilterOptions.setNewItemsAllowed(false);
        tableHeaderFilterOptions.setMultiSelect(true);

        HorizontalLayout labelContainer = new HorizontalLayout();
        labelContainer.setSpacing(true);
        Label icon = new Label();
        icon.setIcon(VaadinIcons.CLOSE_CIRCLE);
       
        
        labelContainer.addComponent(icon);
        Label headerLabel = new Label("Drop comparison");
        headerLabel.addStyleName("marginleft");
        headerLabel.setStyleName(ValoTheme.LABEL_BOLD);
        headerLabel.addStyleName(ValoTheme.LABEL_SMALL);
        labelContainer.addStyleName("pointer");
        labelContainer.addComponent(headerLabel);
        popupbodyLayout.addComponent(labelContainer);
     

        filterPopupLayout = new PopupView(null, popupbodyLayout) {

            @Override
            public void setPopupVisible(boolean visible) {
                this.setVisible(visible);
                super.setPopupVisible(visible); //To change body of generated methods, choose Tools | Templates.
            }

        };
        
           labelContainer.addLayoutClickListener((LayoutEvents.LayoutClickEvent event) -> {    
            filterPopupLayout.setPopupVisible(false);            
            ColumnFilterPopupBtn.this.dropComparison();
        });
        
        closePopup.addLayoutClickListener((LayoutEvents.LayoutClickEvent event) -> {
            filterPopupLayout.setPopupVisible(false);
        });
        filterPopupLayout.setVisible(false);
        filterPopupLayout.setCaptionAsHtml(true);
        filterPopupLayout.setHideOnMouseOut(false);
        filterPopupLayout.addStyleName("margintop");
        this.addComponent(filterPopupLayout);
        Property.ValueChangeListener listener = (Property.ValueChangeEvent event) -> {
            Set<Object> headersSet = new HashSet<>((Set<Object>) event.getProperty().getValue());
            if (headersSet.isEmpty()) {
                this.addStyleName("unselectedfilter");
                this.removeStyleName("selectedfilter");

            } else {
                this.addStyleName("selectedfilter");
                this.removeStyleName("unselectedfilter");
            }
            ColumnFilterPopupBtn.this.filterTable(headersSet);

        };
        tableHeaderFilterOptions.addValueChangeListener(listener);
        this.addLayoutClickListener(ColumnFilterPopupBtn.this);

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        filterPopupLayout.setPopupVisible(true);
    }

    public abstract void dropComparison();

    public abstract void filterTable(Set<Object> headersSet);

}
