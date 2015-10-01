package probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import java.util.Map;
import java.util.Set;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;

/**
 * this class is the container for the comparison bar chart the class developed
 * using JfreeChart and DiVA concept this class support both comparisons
 * selection and table data selection
 *
 * @author Yehia Farag
 */
public class ComparisonChartContainer extends HorizontalLayout{

    private final  JFreeBarchartDivaWrapper chart ;
    private final VerticalLayout closeCompariosonBtn;
    private final QuantDiseaseGroupsComparison comparison; 
    
    /**
     *
     * @return
     */
    public Map<Integer, Set<String>> getCompProtMap() {
        return chart.getCompProtMap();
    }

    /**
     *
     * @param closeListener
     */
    public void addCloseListiner(LayoutEvents.LayoutClickListener closeListener) {

        closeCompariosonBtn.addLayoutClickListener(closeListener);

    }

    /**
     *
     * @param chartListener
     */
    public void addChartListener(LayoutEvents.LayoutClickListener chartListener) {
        this.chart.addLayoutClickListener(chartListener);

    }

    /**
     *
     * @param comparison
     * @param width
     * @param searchingMode
     */
    public ComparisonChartContainer(QuantDiseaseGroupsComparison comparison, int width,boolean searchingMode) {
        
        this.setStyleName("lightborder");
        this.setWidthUndefined();
        this.setHeight("250px");
        this.setMargin(false);       
        width = width - 26;
        this.comparison=comparison;        
        chart = new JFreeBarchartDivaWrapper(width, comparison,searchingMode);
        this.addComponent(chart);
        closeCompariosonBtn = new VerticalLayout();
        closeCompariosonBtn.setWidth("20px");
        closeCompariosonBtn.setHeight("20px");
        closeCompariosonBtn.setStyleName("closebtn");
        this.addComponent(closeCompariosonBtn);
    }

    /**
     *
     * @param accessions
     */
    public void updateExternalSelection(Set<String> accessions) {
        chart.updateExternalTableSelection(accessions);
    }

    /**
     *
     * @param accessions
     * @param reset
     */
    public void updateChartsWithSelectedChartColumn(Set<String> accessions,boolean reset){
        chart.updateChartsWithSelectedChartColumn(accessions,reset);
    }

    /**
     *
     * @param width
     */
    public void resizeChart(int width) {
        width = width-26;
        chart.resize(width);
    }

    /**
     *
     */
    public void reset(){
        chart.reset();
    
    }

    /**
     *
     * @return
     */
    public String getComparisonHeader() {
        return comparison.getComparisonHeader();

    }

    /**
     *
     * @param hl
     */
    public void setHeghlighted(boolean hl) {
        if (hl) {
            this.setStyleName("lightselectedborder");
        } else {
            this.setStyleName("lightborder");
        }

    }

    /**
     *
     * @param barIndex
     */
    public void heighLightBar(int barIndex) {
        chart.heighLightBar(barIndex);

    }

}
