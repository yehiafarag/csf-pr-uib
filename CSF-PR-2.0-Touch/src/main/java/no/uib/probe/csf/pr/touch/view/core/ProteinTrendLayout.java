package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.absolutelayout.AbsoluteLayoutState;
import com.vaadin.ui.AbsoluteLayout;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantComparisonProtein;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;

/**
 *
 * @author Yehia Farag
 *
 * this class represents protein trend (spark line ) required for quant protein
 * table
 */
public class ProteinTrendLayout extends AbsoluteLayout implements Comparable<ProteinTrendLayout> {

    private final String proteinKey;
    private final Set<QuantDiseaseGroupsComparison> selectedComparisonsList;

    private int initSparkline = 0;
    private final int width;
    private QuantComparisonProtein sortableProtein;

    public void setSortableColumnIndex(int comparisonIndex) {
        QuantDiseaseGroupsComparison comp = (QuantDiseaseGroupsComparison) selectedComparisonsList.toArray()[comparisonIndex];
        if (comp.getQuantComparisonProteinMap().containsKey("0_" + proteinKey)) {
            this.sortableProtein = comp.getQuantComparisonProteinMap().get("0_" + proteinKey);
        } else if (comp.getQuantComparisonProteinMap().containsKey("1_" + proteinKey)) {
            this.sortableProtein = comp.getQuantComparisonProteinMap().get("1_" + proteinKey);
        } else if (comp.getQuantComparisonProteinMap().containsKey("2_" + proteinKey)) {
            this.sortableProtein = comp.getQuantComparisonProteinMap().get("2_" + proteinKey);
        } else {
            sortableProtein = null;
        }
    }
    public int getComparisonTrend(int comparisonIndex){
        if(sparkLine== null){
         return 6;
        }
        return sparkLine.getComparisonTrend(comparisonIndex);
    
    }

    public ProteinTrendLayout(Set<QuantDiseaseGroupsComparison> selectedComparisonsList, QuantComparisonProtein selectedProtein, int width) {
        this.selectedComparisonsList = selectedComparisonsList;
        proteinKey = selectedProtein.getProteinAccession();
        QuantDiseaseGroupsComparison comp = (QuantDiseaseGroupsComparison) selectedComparisonsList.toArray()[selectedComparisonsList.size() - 1];
        if (comp.getQuantComparisonProteinMap().containsKey("0_" + proteinKey)) {
            this.sortableProtein = comp.getQuantComparisonProteinMap().get("0_" + proteinKey);
        } else if (comp.getQuantComparisonProteinMap().containsKey("1_" + proteinKey)) {
            this.sortableProtein = comp.getQuantComparisonProteinMap().get("1_" + proteinKey);
        } else if (comp.getQuantComparisonProteinMap().containsKey("2_" + proteinKey)) {
            this.sortableProtein = comp.getQuantComparisonProteinMap().get("2_" + proteinKey);
        }
        this.width = width;

        width = width - 10;
        this.setWidth(width, Unit.PIXELS);
        this.setHeight(100, Unit.PIXELS);
        this.addStyleName("slowslide");
        this.setStyleName("proteintrendcell");

        this.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            private boolean max = false;

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                if (max) {
                    setHeight(100, Unit.PIXELS);
                    sparkLine.minimize();
                    max = false;
                } else {
                    setHeight(500, Unit.PIXELS);
                    sparkLine.maxmize();
                    max = true;

                }

            }
        });

    }

    private LineChart sparkLine;

    @Override
    protected AbsoluteLayoutState getState() {
        if (initSparkline == 6 && sparkLine == null) {
            initSparkline++;
            sparkLine = new LineChart(width, 500);
            sparkLine.updateData(selectedComparisonsList, proteinKey);
            this.addComponent(sparkLine);
        }
        initSparkline++;
        return super.getState();
    }
    private Double v1 = 0.0;

    @Override
    public int compareTo(ProteinTrendLayout t) {
        if (sortableProtein == null) {

            return -1;
        }
        QuantComparisonProtein o = t.sortableProtein;
        if (o == null) {
            return 1;
        }

        if (sortableProtein.getHighSignificant() == sortableProtein.getLowSignificant()) {
            v1 = sortableProtein.getTrendValue();
        } else if (sortableProtein.getTrendValue() > 0) {
            double factor = sortableProtein.getPenalty();
            v1 = sortableProtein.getTrendValue() - factor;
            v1 = Math.max(v1, 0) + ((double) (sortableProtein.getHighSignificant() - sortableProtein.getLowSignificant()) / 10.0);
        } else {
            double factor = sortableProtein.getPenalty();
            v1 = sortableProtein.getTrendValue() + factor;
            v1 = Math.min(v1, 0) + ((double) (sortableProtein.getHighSignificant() - sortableProtein.getLowSignificant()) / 10.0);
        }
        Double v2;
        if (o.getHighSignificant() == o.getLowSignificant()) {
            v2 = o.getTrendValue();
        } else if (o.getTrendValue() > 0) {
            double factor = o.getPenalty();
            v2 = o.getTrendValue() - factor;
            v2 = Math.max(v2, 0) + ((double) (o.getHighSignificant() - o.getLowSignificant()) / 10.0);
        } else {
            double factor = o.getPenalty();
            v2 = o.getTrendValue() + factor;
            v2 = Math.min(v2, 0) + ((double) (o.getHighSignificant() - o.getLowSignificant()) / 10.0);
        }
        return (v1).compareTo(v2);

    }

}
