package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Image;
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
public class ProteinTrendLayout extends AbsoluteLayout {

    private final Image chartImg;
    private final AbsoluteLayout chartComponentsLayout;

    public ProteinTrendLayout(Set<QuantDiseaseGroupsComparison> selectedComparisonsList, QuantComparisonProtein selectedProtein, int width) {
        width = width - 10;
        this.setWidth(width, Unit.PIXELS);
        this.setHeight(100, Unit.PIXELS);
        chartImg = new Image();
        chartImg.setWidth(100, Unit.PERCENTAGE);
        chartImg.setHeight(100, Unit.PERCENTAGE);
        this.addComponent(chartImg, "left: " + 0 + "px; top: " + 0 + "px;");

        chartComponentsLayout = new AbsoluteLayout();
        chartComponentsLayout.setWidth(100, Unit.PERCENTAGE);
        chartComponentsLayout.setHeight(100, Unit.PERCENTAGE);
        this.addComponent(chartComponentsLayout, "left: " + 0 + "px; top: " + 0 + "px;");

        this.setStyleName("proteintrendcell");
        int trend = 5;
        int col = 0;

        for (QuantDiseaseGroupsComparison comparison : selectedComparisonsList) {
            String keyI = 0 + "_" + selectedProtein.getProteinAccession();
            String keyII = 0 + "_" + selectedProtein.getProteinAccession();
            if (comparison.getQuantComparisonProteinMap().containsKey(keyI)) {
                trend = comparison.getQuantComparisonProteinMap().get(keyI).getSignificantTrindCategory();
            } else if (comparison.getQuantComparisonProteinMap().containsKey(keyII)) {
                trend = comparison.getQuantComparisonProteinMap().get(keyII).getSignificantTrindCategory();
            }

            System.out.println("at tesnd  " + trend + "comparison " + comparison.getComparisonHeader());
            TrendSymbol symbol = new TrendSymbol(trend);
            symbol.setWidth(8, Unit.PIXELS);
            symbol.setHeight(8, Unit.PIXELS);
            if (trend == 5) {
                trend = 2;
            }
//            this.addComponent(symbol,col++,trend);
//            this.setComponentAlignment(symbol, Alignment.MIDDLE_CENTER);

        }
        this.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            private boolean max = false;

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                if (max) {                  
                    setHeight(100, Unit.PIXELS);
                    max=false;
                }else{
                    setHeight(500, Unit.PIXELS);
                max=true;
                
                }
                
            }
        });

    }

}
