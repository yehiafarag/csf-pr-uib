package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.HashSet;
import java.util.Set;
import org.vaadin.teemu.VaadinIcons;

/**
 * This class allow users to filter the columns of the protein table or drop one
 * comparison.
 *
 * @author Yehia Farag
 */
public abstract class ColumnFilterPopupBtn extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    /**
     * The main pop-up window.
     */
    private final PopupView filterPopupLayout;
    /**
     * Different options available to filter or drop the dataset.
     */
    private final OptionGroup tableHeaderFilterOptions;
    /**
     * Main layout click listener.
     */
    private final Property.ValueChangeListener listener;

    /**
     * Constructor to initialize the main attributes.
     *
     * @param quantCustomizedComparison The comparison is user customized
     * comparison.
     */
    public ColumnFilterPopupBtn(boolean quantCustomizedComparison) {
        this.addStyleName("unselectedfilter");

        VerticalLayout popupbodyLayout = new VerticalLayout();
        popupbodyLayout.setSpacing(true);
        popupbodyLayout.setWidth(300, Unit.PIXELS);
        popupbodyLayout.setMargin(new MarginInfo(false, false, false, true));
        popupbodyLayout.addStyleName("border");

        CloseButton closePopup = new CloseButton();
        closePopup.setWidth(10, Unit.PIXELS);
        closePopup.setHeight(10, Unit.PIXELS);
        popupbodyLayout.addComponent(closePopup);
        popupbodyLayout.setComponentAlignment(closePopup, Alignment.TOP_RIGHT);
        closePopup.addStyleName("translateleft10");

        TabSheet tabsheetContainer = new TabSheet();
        tabsheetContainer.setSizeFull();
        tabsheetContainer.setStyleName(ValoTheme.TABSHEET_COMPACT_TABBAR);
        popupbodyLayout.addComponent(tabsheetContainer);
        tableHeaderFilterOptions = new OptionGroup();
        tabsheetContainer.addTab(tableHeaderFilterOptions, "Category");
//        popupbodyLayout.addComponent(tableHeaderFilterOptions);
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
        tableHeaderFilterOptions.setItemEnabled(3, !quantCustomizedComparison);
        tableHeaderFilterOptions.setItemEnabled(1, !quantCustomizedComparison);
        tableHeaderFilterOptions.setItemEnabled(5, !quantCustomizedComparison);

        //add range filter
        
        RangeComponent studyRange = new RangeComponent("Decreased 100%", -100.0, "Increased 100%", 100.0);
        
      
        tabsheetContainer.addTab(studyRange, "Trend (%)");

        //add study number range
        tabsheetContainer.addTab(new VerticalLayout(), "#Study");

        filterPopupLayout = new PopupView(null, popupbodyLayout) {
            @Override
            public void setPopupVisible(boolean visible) {
                this.setVisible(visible);
                super.setPopupVisible(visible);
            }
        };

        if (!quantCustomizedComparison) {
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
            labelContainer.addLayoutClickListener((LayoutEvents.LayoutClickEvent event) -> {
                filterPopupLayout.setPopupVisible(false);
                ColumnFilterPopupBtn.this.dropComparison();
            });
        }

        closePopup.addLayoutClickListener((LayoutEvents.LayoutClickEvent event) -> {
            filterPopupLayout.setPopupVisible(false);
        });
        filterPopupLayout.setVisible(false);
        filterPopupLayout.setCaptionAsHtml(true);
        filterPopupLayout.setHideOnMouseOut(false);
        filterPopupLayout.addStyleName("margintop");
        this.addComponent(filterPopupLayout);
        listener = (Property.ValueChangeEvent event) -> {
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

    /**
     * Layout click to show the filter pop-up layout.
     *
     * @param event show filter layout.
     */
    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        filterPopupLayout.setPopupVisible(true);
    }

    /**
     * Reset filter and remove all applied filters.
     */
    public void reset() {
        tableHeaderFilterOptions.removeValueChangeListener(listener);
        tableHeaderFilterOptions.setValue(null);
        tableHeaderFilterOptions.addValueChangeListener(listener);
    }

    /**
     * Drop comparison upon user drop comparison selection.
     */
    public abstract void dropComparison();

    /**
     * Filter table based on selected filters.
     *
     * @param selectedFiltersSet Set of user selected filters.
     */
    public abstract void filterTable(Set<Object> selectedFiltersSet);

}
