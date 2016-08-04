package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.absolutelayout.AbsoluteLayoutState;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
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
public abstract class ProteinTrendLayout extends AbsoluteLayout implements Comparable<ProteinTrendLayout>, LayoutEvents.LayoutClickListener {

    private final String proteinKey;
    private final Set<QuantDiseaseGroupsComparison> selectedComparisonsList;

    private int initSparkline = 0;
    private final int width;
    private QuantComparisonProtein sortableProtein;
    private final VerticalLayout maxMinBtn;
    private final VerticalLayout sparkLineContainer;
    private final Object itemId;
    private int custTrend = -1;

    public LineChart getSparkLine() {
        return sparkLine;
    }

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

    public ProteinTrendLayout(Set<QuantDiseaseGroupsComparison> selectedComparisonsList, QuantComparisonProtein selectedProtein, int width, Object itemId, boolean draw) {
        this.itemId = itemId;
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

        sparkLineContainer = new VerticalLayout();
        sparkLineContainer.setSizeFull();
        this.addComponent(sparkLineContainer, "left: " + 0 + "px; top: " + 0 + "px;");
        sparkLineContainer.addLayoutClickListener(ProteinTrendLayout.this);

        VerticalLayout resizeIconLayout = new VerticalLayout();
        resizeIconLayout.setWidth(20, Unit.PERCENTAGE);
        resizeIconLayout.setHeight(20, Unit.PIXELS);
        this.addComponent(resizeIconLayout, "left: " + (width-25) + "px; top: " + 0 + "px;");

        maxMinBtn = new VerticalLayout();
        maxMinBtn.setWidth(20, Unit.PIXELS);
        maxMinBtn.setHeight(20, Unit.PIXELS);
        maxMinBtn.setMargin(new MarginInfo(false, false, false, false));
        maxMinBtn.setStyleName("maxmizebtn");
        maxMinBtn.setDescription("Click to maximize");

        resizeIconLayout.addComponent(maxMinBtn);
        resizeIconLayout.setComponentAlignment(maxMinBtn, Alignment.TOP_RIGHT);

        maxMinBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            private boolean max = false;

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                if (max) {
                    setHeight(100, Unit.PIXELS);
                    sparkLine.minimize();
                    maxMinBtn.setStyleName("maxmizebtn");
                    max = false;
                } else {
                    maxMinBtn.setStyleName("minmizebtn");
                    setHeight(500, Unit.PIXELS);
                    sparkLine.maxmize();
                    max = true;

                }

            }
        });
        if (draw) {
            sparkLine = new LineChart(width, 500);
            sparkLine.updateData(selectedComparisonsList, proteinKey, custTrend);
            sparkLineContainer.addComponent(sparkLine);
        }

    }

    public void updateCustTrend(int userTrend) {
        if (sparkLine != null) {
            sparkLine.updateUrserTrend(userTrend);
        }
        this.custTrend = userTrend;

    }

    private LineChart sparkLine;

    @Override
    protected AbsoluteLayoutState getState() {
        if (initSparkline == 6 && sparkLine == null) {
            initSparkline++;
            sparkLine = new LineChart(width, 500);
            sparkLine.updateData(selectedComparisonsList, proteinKey, custTrend);
            sparkLineContainer.addComponent(sparkLine);
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

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        selectTableItem(itemId);
    }

    public abstract void selectTableItem(Object itemId);

}
