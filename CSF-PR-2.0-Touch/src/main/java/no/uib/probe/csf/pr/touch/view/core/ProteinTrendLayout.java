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
 * This class represents protein trend line chart(spark line ) required for
 * quant protein table.
 *
 * @author Yehia Farag
 */
public abstract class ProteinTrendLayout extends AbsoluteLayout implements Comparable<ProteinTrendLayout>, LayoutEvents.LayoutClickListener {

    private final String proteinKey;
    private final Set<QuantDiseaseGroupsComparison> selectedComparisonsList;
    private AbsoluteLayout chartComponentsLayout;

    public AbsoluteLayout getChartComponentsLayout() {
        return chartComponentsLayout;
    }

    private int initSparkline = 0;
    private final int width;
    private QuantComparisonProtein sortableProtein;
    private final VerticalLayout maxMinBtn;
    private final VerticalLayout sparkLineContainer;
    private final Object itemId;
    private int custTrend = -1;

    public int getChartWidth() {
        return width - 10;
    }
    private final boolean smallScreen;

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
        } else if (comp.getQuantComparisonProteinMap().containsKey("3_" + proteinKey)) {
            this.sortableProtein = comp.getQuantComparisonProteinMap().get("3_" + proteinKey);
        } else if (comp.getQuantComparisonProteinMap().containsKey("4_" + proteinKey)) {
            this.sortableProtein = comp.getQuantComparisonProteinMap().get("4_" + proteinKey);
        } else {
            sortableProtein = null;
        }
    }

    public ProteinTrendLayout(Set<QuantDiseaseGroupsComparison> selectedComparisonsList, QuantComparisonProtein selectedProtein, int width, Object itemId, boolean draw, boolean smallScreen) {
        this.itemId = itemId;
        this.smallScreen = smallScreen;
        this.selectedComparisonsList = selectedComparisonsList;
        proteinKey = selectedProtein.getProteinAccession();
        QuantDiseaseGroupsComparison comp = (QuantDiseaseGroupsComparison) selectedComparisonsList.toArray()[selectedComparisonsList.size() - 1];
        if (comp.getQuantComparisonProteinMap().containsKey("0_" + proteinKey)) {
            this.sortableProtein = comp.getQuantComparisonProteinMap().get("0_" + proteinKey);
        } else if (comp.getQuantComparisonProteinMap().containsKey("1_" + proteinKey)) {
            this.sortableProtein = comp.getQuantComparisonProteinMap().get("1_" + proteinKey);
        } else if (comp.getQuantComparisonProteinMap().containsKey("2_" + proteinKey)) {
            this.sortableProtein = comp.getQuantComparisonProteinMap().get("2_" + proteinKey);
        } else if (comp.getQuantComparisonProteinMap().containsKey("3_" + proteinKey)) {
            this.sortableProtein = comp.getQuantComparisonProteinMap().get("3_" + proteinKey);
        } else if (comp.getQuantComparisonProteinMap().containsKey("4_" + proteinKey)) {
            this.sortableProtein = comp.getQuantComparisonProteinMap().get("4_" + proteinKey);
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
        this.addComponent(resizeIconLayout, "left: " + (width - 25) + "px; top: " + 0 + "px;");

        maxMinBtn = new VerticalLayout();
        maxMinBtn.setWidth(20, Unit.PIXELS);
        maxMinBtn.setHeight(20, Unit.PIXELS);
        maxMinBtn.setMargin(new MarginInfo(false, false, false, false));
        maxMinBtn.setStyleName("maxmizebtn");
        maxMinBtn.setDescription("Click to maximize");

        resizeIconLayout.addComponent(maxMinBtn);
        resizeIconLayout.setComponentAlignment(maxMinBtn, Alignment.TOP_RIGHT);
        maxMinBtn.addLayoutClickListener((LayoutEvents.LayoutClickEvent event) -> {
            ProteinTrendLayout.this.maxmize();
        });

        if (draw) {

            sparkLine = new LineChart(width, 500);
            sparkLine.updateData(selectedComparisonsList, proteinKey, custTrend);
            sparkLineContainer.addComponent(sparkLine);
            chartComponentsLayout = sparkLine.getChartComponentsLayout();
        }

    }

    private boolean max = false;

    public boolean isMax() {
        return max;
    }

    public void maxmize() {
        if (sparkLine == null || smallScreen) {
            return;
        }
        if (max) {
            setHeight(100, Unit.PIXELS);
            sparkLine.minimize(false);
            maxMinBtn.setStyleName("maxmizebtn");
            max = false;
        } else {
            maxMinBtn.setStyleName("minmizebtn");
            setHeight(500, Unit.PIXELS);
            sparkLine.maxmize();
            max = true;

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
        Double v2;
        if (sortableProtein == null) {
            v1 = 0.0;
        } else {
            v1 = sortableProtein.getOverallCellPercentValue();
        }
        QuantComparisonProtein o = t.sortableProtein;
        if (o == null) {
            v2 = 0.0;
        } else {
            v2 = t.sortableProtein.getOverallCellPercentValue();
        }

        return (v1).compareTo(v2);

//        if (sortableProtein.getSignificantlyIncreasedNumber() == sortableProtein.getSignificantlyDecreasedNumber()) {
//            v1 = sortableProtein.getTrendValue();
//        } else if (sortableProtein.getTrendValue() > 0) {
//            double factor = sortableProtein.getPenalty();
//            v1 = sortableProtein.getTrendValue() - factor;
//            v1 = Math.max(v1, 0) + ((double) (sortableProtein.getSignificantlyIncreasedNumber() - sortableProtein.getSignificantlyDecreasedNumber()) / 10.0);
//        } else {
//            double factor = sortableProtein.getPenalty();
//            v1 = sortableProtein.getTrendValue() + factor;
//            v1 = Math.min(v1, 0) + ((double) (sortableProtein.getSignificantlyIncreasedNumber() - sortableProtein.getSignificantlyDecreasedNumber()) / 10.0);
//        }
//        Double v2;
//        if (o.getSignificantlyIncreasedNumber() == o.getSignificantlyDecreasedNumber()) {
//            v2 = o.getTrendValue();
//        } else if (o.getTrendValue() > 0) {
//            double factor = o.getPenalty();
//            v2 = o.getTrendValue() - factor;
//            v2 = Math.max(v2, 0) + ((double) (o.getSignificantlyIncreasedNumber() - o.getSignificantlyDecreasedNumber()) / 10.0);
//        } else {
//            double factor = o.getPenalty();
//            v2 = o.getTrendValue() + factor;
//            v2 = Math.min(v2, 0) + ((double) (o.getSignificantlyIncreasedNumber() - o.getSignificantlyDecreasedNumber()) / 10.0);
//        }
//        return (v1).compareTo(v2);
    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        selectTableItem(itemId);
    }

    public abstract void selectTableItem(Object itemId);

}
