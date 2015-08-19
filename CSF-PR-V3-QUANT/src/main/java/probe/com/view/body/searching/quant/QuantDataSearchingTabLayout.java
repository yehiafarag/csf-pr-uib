/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.searching.quant;

import com.vaadin.ui.VerticalLayout;
import java.io.Serializable;
import java.util.List;
import probe.com.handlers.MainHandler;
import probe.com.model.beans.QuantProtein;

/**
 *
 * @author Yehia Farag
 */
public class QuantDataSearchingTabLayout extends VerticalLayout implements Serializable {

    public QuantDataSearchingTabLayout(List<QuantProtein> searchQuantificationProtList, MainHandler handler) {      
        QuantProtiensSearchingResultsLayout datasetOverviewTabLayout = new QuantProtiensSearchingResultsLayout(handler, searchQuantificationProtList);
        this.addComponent(datasetOverviewTabLayout);

    }

}
