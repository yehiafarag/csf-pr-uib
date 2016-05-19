
package no.uib.probe.csf.pr.touch.view.components;

import com.vaadin.ui.VerticalLayout;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFListener;

/**
 *
 * @author YEhia Farag
 * 
 * this class is represents bubble-chart for comparisons overview
 * 
 */
public class BubbleChartComponent extends VerticalLayout implements CSFListener{

    public BubbleChartComponent() {
        this.setWidth(100,Unit.PERCENTAGE);
        this.setHeight(400,Unit.PIXELS);
        this.setStyleName("lightgraylayout");
    }

    @Override
    public void selectionChanged(String type) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getFilterId() {
      return "bubble_chart_listener";
    }

    @Override
    public void removeFilterValue(String value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
