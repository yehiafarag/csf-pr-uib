package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import no.uib.probe.csf.pr.touch.logic.beans.QuantComparisonProtein;
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

    public int getComparisonIndex() {
        return index;
    }

    /**
     * Constructor to initialize the main attributes.
     *
     * @param comparison The main quant comparison.
     * @param index The comparison index.
     */
    public ColumnHeaderLayout(QuantDiseaseGroupsComparison comparison, int index) {
        ColumnHeaderLayout.this.setWidth(15, Unit.PIXELS);
        ColumnHeaderLayout.this.setHeight(15, Unit.PIXELS);
        this.index = index;
        VerticalLayout headerFrame = new VerticalLayout();
        headerFrame.setHeight(100, Unit.PERCENTAGE);
        headerFrame.setWidthUndefined();
        ColumnHeaderLayout.this.addComponent(headerFrame);
        ColumnHeaderLayout.this.setComponentAlignment(headerFrame, Alignment.TOP_CENTER);

        filterBtn = new ColumnFilterPopupBtn((index == -1)) {
            @Override
            public void dropComparison() {
                ColumnHeaderLayout.this.dropComparison(comparison);
            }

            @Override
            public void filterTable(Set<Object> filtersSet,boolean unselectfilter) {
                ColumnHeaderLayout.this.filterTable(comparison, index, filtersSet,unselectfilter);
            }

            @Override
            public void filterTable(double min, double max,boolean unselectfilter) {
               ColumnHeaderLayout.this.filterTable(comparison, index, min,max,unselectfilter);
            }

            @Override
            public void filterTable(long minStudiesNumber,long maxStudiesNumber,boolean unselectfilter) {
            ColumnHeaderLayout.this.filterTable(comparison,index, minStudiesNumber,maxStudiesNumber,unselectfilter);    
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

        filterBtn.updateChartsData(initRangeData(comparison),initStudeisNumberData(comparison));

    }

    private double[][] initRangeData(QuantDiseaseGroupsComparison comparison) {
        Map<Double, Integer> dataMap = new TreeMap<>();
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        for (QuantComparisonProtein quant : comparison.getQuantComparisonProteinMap().values()) {
            double value = quant.getOverallCellPercentValue();
            if (!dataMap.containsKey(value)) {
                dataMap.put(value, 0);
            }
            dataMap.put(value, (dataMap.get(value) + 1));
            if (dataMap.get(value) >= max) {
                max = dataMap.get(value);
            }
            if (dataMap.get(value) <= min) {
                min = dataMap.get(value);
            }

        }
        double[][] data = new double[dataMap.size()][2];
        int i = 0;
        for (double key : dataMap.keySet()) {
            data[i] = new double[]{key, scaleValues((double) dataMap.get(key),max, min)};
            i++;
        }
//        data[0] = new double[]{-100, 50};
//        data[1] = new double[]{-80, 30};
//        data[2] = new double[]{-60, 55};
//        data[3] = new double[]{-50, 10};
//        data[4] = new double[]{-20, 2};
//        data[5] = new double[]{0, 80};
//        data[6] = new double[]{30, 20};
//        data[7] = new double[]{60, 11};
//        data[8] = new double[]{70, 17};
//        data[9] = new double[]{100, 75};
        return data;
    }
    
     private double[][] initStudeisNumberData(QuantDiseaseGroupsComparison comparison) {
        Map<Integer, Double> dataMap = new TreeMap<>();
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        for (QuantComparisonProtein quant : comparison.getQuantComparisonProteinMap().values()) {
            int value = quant.getDsQuantProteinsMap().size();
            if(!dataMap.containsKey(value)){
            dataMap.put(value, 0.0);
            }
             dataMap.put(value, dataMap.get(value)+1.0);          
            if (dataMap.get(value) >= max) {
                max = dataMap.get(value);
            }
            if (dataMap.get(value) <= min) {
                min = dataMap.get(value);
            }

        }
        double[][] data = new double[dataMap.size()][2];
        int i = 0;
        for (int key : dataMap.keySet()) {
            data[i] = new double[]{key, scaleValues((double) dataMap.get(key),max, min)};
            i++;
        }
        return data;
    }


    /**
     * Converts the value from linear scale to log scale; The log scale numbers
     * are limited by the range of the type float; The linear scale numbers can
     * be any double value.
     *
     * @param linearValue the value to be converted to log scale
     * @param max The upper limit number for the input numbers
     * @param lowerLimit the lower limit for the input numbers
     * @return the value in log scale
     */
    private double scaleValues(double linearValue, double max, double lowerLimit) {
        double logMax = (Math.log(max) / Math.log(2));
        double logValue = (Math.log(linearValue + 1) / Math.log(2));
        logValue = (logValue * 2 / logMax) + lowerLimit;
        return logValue;
    }

    /**
     * Sort ascending.
     */
    private Boolean sortedUp;

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

//    public void updateFilterData(double[][] data) {
//        filterBtn.updateChartsData(data);
//    }

    /**
     * Sort protein table based on the selected comparison and the selected
     * direction.
     *
     * @param up Sort ascending.
     * @param index Comparison index
     */
    public abstract void sort(boolean up, int index);

    /**
     * Un-select this comparison.
     *
     * @param comparison Quant disease comparison
     */
    public abstract void dropComparison(QuantDiseaseGroupsComparison comparison);

    /**
     * Filter table based on selected filters.
     *
     * @param comparison The quant disease comparison.
     * @param comparisonIndex The comparison index.
     * @param filterSet The applied filters set.
     */
    public abstract void filterTable(QuantDiseaseGroupsComparison comparison, int comparisonIndex, Set<Object> filterSet,boolean unselectfilter);
    
    /**
     * Filter table based on range filters.
     *
     * @param comparison The quant disease comparison.
     * @param comparisonIndex The comparison index.
     * @param filterSet The applied filters set.
     */
    public abstract void filterTable(QuantDiseaseGroupsComparison comparison, int comparisonIndex, double min,double max,boolean unselectfilter);
    
     /**
     * Filter table based on range filters.
     *
     * @param comparison The quant disease comparison.
     * @param comparisonIndex The comparison index.
     * @param filterSet The applied filters set.
     */
    public abstract void filterTable(QuantDiseaseGroupsComparison comparison, int comparisonIndex,long min,long max,boolean unselectfilter);

}
