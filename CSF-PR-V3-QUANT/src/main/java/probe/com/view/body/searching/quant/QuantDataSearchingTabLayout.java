/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.searching.quant;

import com.vaadin.ui.VerticalLayout;
import java.io.Serializable;
import java.util.List;
import probe.com.handlers.CSFPRHandler;
import probe.com.model.beans.quant.QuantProtein;

/**
 *
 * @author Yehia Farag
 */
public class QuantDataSearchingTabLayout extends VerticalLayout implements Serializable {

    /**
     *
     * @param searchQuantificationProtList
     * @param CSFPR_Handler
     */
    public QuantDataSearchingTabLayout(List<QuantProtein> searchQuantificationProtList, CSFPRHandler CSFPR_Handler) {      
        QuantProtiensSearchingResultsLayout datasetOverviewTabLayout = new QuantProtiensSearchingResultsLayout(CSFPR_Handler, searchQuantificationProtList);
         this.setMargin(true);
        this.setWidth("100%");
        this.addComponent(datasetOverviewTabLayout);

    }

}
