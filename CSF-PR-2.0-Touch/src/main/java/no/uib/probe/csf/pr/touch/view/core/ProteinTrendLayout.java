package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ServerRpcManager;
import com.vaadin.shared.AbstractComponentState;
import com.vaadin.shared.communication.ClientRpc;
import com.vaadin.shared.communication.SharedState;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.absolutelayout.AbsoluteLayoutState;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.declarative.DesignContext;
import java.util.EventObject;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantComparisonProtein;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import org.jsoup.nodes.Element;

/**
 *
 * @author Yehia Farag
 *
 * this class represents protein trend (spark line ) required for quant protein
 * table
 */
public class ProteinTrendLayout extends AbsoluteLayout {

    private final String proteinKey;
    private final Set<QuantDiseaseGroupsComparison> selectedComparisonsList;

    private int initSparkline = 0;
    private int width;

    public ProteinTrendLayout(Set<QuantDiseaseGroupsComparison> selectedComparisonsList, QuantComparisonProtein selectedProtein, int width) {
        this.selectedComparisonsList = selectedComparisonsList;
        proteinKey = selectedProtein.getProteinAccession();
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

    private  LineChart sparkLine ;
    
    @Override
    protected AbsoluteLayoutState getState() {
        if (initSparkline==6 && sparkLine==null) {            
             initSparkline++;
            sparkLine = new LineChart(width, 500);
            sparkLine.updateData(selectedComparisonsList, proteinKey);            
            this.addComponent(sparkLine);
        }
        initSparkline++;
        return super.getState();
    }

}
