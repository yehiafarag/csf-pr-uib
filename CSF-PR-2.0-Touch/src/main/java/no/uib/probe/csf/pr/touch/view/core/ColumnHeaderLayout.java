package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;

/**
 * This class represents comparison column header button that allow the users to
 * Sort/filter the proteins table.
 *
 * @author Yehia Farag
 */
public abstract class ColumnHeaderLayout extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    /**
     * The main sorting button.
     */
    private final VerticalLayout sortingBtn;
    /**
     * The main filter button.
     */
    private final ColumnFilterPopupBtn filterBtn;

    /**
     * The button index (same as comparison index).
     */
    private final int index;

    /**
     * Constructor to initialize the main attributes.
     *
     * @param comparison The main quant comparison.
     * @param index The comparison index.
     */
    public ColumnHeaderLayout(QuantDiseaseGroupsComparison comparison, int index) {
        this.setWidth(15, Unit.PIXELS);
        this.setHeight(15, Unit.PIXELS);
        this.index = index;
        VerticalLayout headerFrame = new VerticalLayout();
        headerFrame.setHeight(100, Unit.PERCENTAGE);
        headerFrame.setWidthUndefined();
        this.addComponent(headerFrame);
        this.setComponentAlignment(headerFrame, Alignment.TOP_CENTER);
        filterBtn = new ColumnFilterPopupBtn((index == -1)) {
            @Override
            public void dropComparison() {
                ColumnHeaderLayout.this.dropComparison(comparison);
            }

            @Override
            public void filterTable(Set<Object> filtersSet) {
                ColumnHeaderLayout.this.filterTable(comparison, index, filtersSet);
            }

        };
        filterBtn.setWidth(20, Unit.PIXELS);
        filterBtn.setHeight(100, Unit.PERCENTAGE);
        filterBtn.setStyleName(comparison.getDiseaseCategoryStyle());
        filterBtn.addStyleName("unselectedfilter");
        filterBtn.setVisible(false);
        String[] gr = comparison.getComparisonFullName().replace("__" + comparison.getDiseaseCategory(), "").split(" / ");
        String updatedHeader = ("Numerator: " + gr[0] + "<br/>Denominator: " + gr[1] + "<br/>Disease: " + comparison.getDiseaseCategory());
        filterBtn.setDescription(updatedHeader);
        headerFrame.addComponent(filterBtn);
        headerFrame.setComponentAlignment(filterBtn, Alignment.TOP_CENTER);

        sortingBtn = new VerticalLayout();
        sortingBtn.setWidth(20, Unit.PIXELS);
        sortingBtn.setHeight(100, Unit.PERCENTAGE);
        sortingBtn.setStyleName(comparison.getDiseaseCategoryStyle());
        sortingBtn.setDescription(updatedHeader);
        sortingBtn.addLayoutClickListener(ColumnHeaderLayout.this);
        sortingBtn.addStyleName("sortdown");
        sortingBtn.addStyleName("unselected");
        headerFrame.addComponent(sortingBtn);
        headerFrame.setComponentAlignment(sortingBtn, Alignment.TOP_CENTER);
    }

    /**
     * Reset the Sorting and Filtering buttons.
     */
    public void reset() {
        filterBtn.reset();
        filterBtn.setVisible(false);
        sortingBtn.setVisible(true);
        if (sortingBtn.getStyleName().contains("blinkII")) {
            sortingBtn.removeStyleName("blinkII");
            sortingBtn.addStyleName("blink");
        } else {
            sortingBtn.removeStyleName("blinkI");
            sortingBtn.addStyleName("blinkII");
        }

    }

    /**
     * Switch between Sorting / Filtering buttons.
     */
    public void swichBtns() {
        filterBtn.setVisible(!filterBtn.isVisible());
        sortingBtn.setVisible(!sortingBtn.isVisible());
        if (sortingBtn.getStyleName().contains("blinkII")) {
            sortingBtn.removeStyleName("blinkII");
            sortingBtn.addStyleName("blink");
        } else {
            sortingBtn.removeStyleName("blinkI");
            sortingBtn.addStyleName("blinkII");
        }

        if (filterBtn.getStyleName().contains("blinkII")) {
            filterBtn.removeStyleName("blinkII");
            filterBtn.addStyleName("blink");
        } else {
            filterBtn.removeStyleName("blinkI");
            filterBtn.addStyleName("blinkII");
        }

    }

    /**
     * Set this component as default sorting component.
     */
    public void setAsDefault() {
        sortingBtn.removeStyleName("unselected");
        sortedUp = false;
    }
    private Boolean sortedUp;

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (sortedUp == null || sortedUp) {
            sortingBtn.addStyleName("sortdown");
            sortedUp = false;

        } else if (!sortedUp) {
            sortingBtn.removeStyleName("sortdown");
            sortingBtn.addStyleName("sortup");
            sortedUp = true;
        }
        sortingBtn.removeStyleName("unselected");
        sort(sortedUp, index);
    }

    /**
     * Reset sorting layout.
     */
    public void noSort() {
        sortingBtn.addStyleName("sortdown");
        sortingBtn.removeStyleName("sortup");
        sortingBtn.addStyleName("unselected");
        sortedUp = null;
    }

    /**
     * Remove all applied filters and reset filters selection option.
     */
    public void noFilter() {
        filterBtn.reset();
        filterBtn.addStyleName("unselectedfilter");
        filterBtn.removeStyleName("selectedfilter");
    }

    /**
     * Sort protein table based on the selected comparison and the selected
     * direction.
     *
     * @param up
     * @param index
     */
    public abstract void sort(boolean up, int index);

    /**
     * Un-select this comparison.
     *
     * @param comparison
     */
    public abstract void dropComparison(QuantDiseaseGroupsComparison comparison);

    /**
     * Filter table based on selected filters.
     *
     * @param comparison The quant disease comparison.
     * @param comparisonIndex The comparison index.
     * @param filterSet The applied filters set.
     */
    public abstract void filterTable(QuantDiseaseGroupsComparison comparison, int comparisonIndex, Set<Object> filterSet);

}
