/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import probe.com.view.core.ClosableFilterLabel;

/**
 *
 * @author Yehia Farag
 */
public class TableLegend extends HorizontalLayout{

    public TableLegend() {
        String[] labels = new String[]{"High","Stable","Low"};
        String[] styleName = new String[]{"redlayout","lightbluelayout","greenlayout"};
        this.setSpacing(true);
        this.setMargin(new MarginInfo(false, true, false, false));
        for(int i=0;i<styleName.length;i++){
            HorizontalLayout item = generateItemLabel(labels[i], styleName[i]);
            this.addComponent(item);
            this.setComponentAlignment(item, Alignment.MIDDLE_CENTER);
        
        }
        
    }
    
    private HorizontalLayout generateItemLabel(String label,String style){
    
        HorizontalLayout labelLayout = new HorizontalLayout();
        labelLayout.setSpacing(true);
        labelLayout.setHeight("20px");
          VerticalLayout icon = new VerticalLayout();
        icon.setWidth("10px");
        icon.setHeight("10px");
        icon.setStyleName(style);
        labelLayout.addComponent(icon);
        labelLayout.setComponentAlignment(icon,Alignment.MIDDLE_LEFT);
        Label l = new Label("<font size='2' face='Verdana'>"+label+"</font>");
        l.setContentMode(ContentMode.HTML);
        labelLayout.addComponent(l);
        
      
        
        return labelLayout;
        
    }
    
}
