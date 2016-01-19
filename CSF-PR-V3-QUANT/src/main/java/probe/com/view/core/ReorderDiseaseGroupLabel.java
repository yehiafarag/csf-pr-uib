/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author yfa041
 */
public class ReorderDiseaseGroupLabel extends VerticalLayout{

    public ReorderDiseaseGroupLabel(int itemWidth, String strLabel,String styleName) {
      
            this.setWidth(itemWidth + "px");
            this.setHeight("30px");
            this.setStyleName("rowItem");
            this.addStyleName("rowItem_"+styleName);
            this.setDescription(strLabel);
            Label label = new Label("<font color='#ffffff'>"+strLabel.split("\n")[0]+"</font>");
            label.setContentMode(ContentMode.HTML);
//            label.setStyleName(styleName);
            this.addComponent(label);
        
        
    }
    
}
