
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Yehia Farag
 * 
 * this class represents label layout for disease groups
 */
public class DiseaseGroupLabel extends VerticalLayout{

    public DiseaseGroupLabel(String strLabel,String styleName) {
      
            this.setWidth(100,Unit.PERCENTAGE);
            this.setHeight(24,Unit.PIXELS);
            this.setStyleName(styleName);
            this.setMargin(new MarginInfo(false, true, false, false));
            this.setDescription(strLabel);
            Label label = new Label(strLabel);
            label.addStyleName("paddingleft20");
            this.addComponent(label);
            this.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
        
        
    }
    
}
