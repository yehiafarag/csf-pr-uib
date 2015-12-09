/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Yehia Farag
 */
public class TrendLegend extends GridLayout {

    public TrendLegend(String type) {
        if (type.equalsIgnoreCase("table")) {
            String[] labels = new String[]{"High", "Stable", "Low"};
            String[] styleName = new String[]{"redlayout", "lightbluelayout", "greenlayout"};
            this.setSpacing(true);
            this.setColumns(3);
            this.setRows(1);
            this.setMargin(new MarginInfo(false, true, false, false));
            for (int i = 0; i < styleName.length; i++) {
                HorizontalLayout item = generateItemLabel(labels[i], styleName[i]);
                this.addComponent(item, i, 0);
                this.setComponentAlignment(item, Alignment.MIDDLE_CENTER);

            }
        }else if(type.equalsIgnoreCase("ministackedpeptidessequence")){
            String[] labels = new String[]{"High,not sign.", "Low, not sign."};
            String[] styleName = new String[]{ "notsigredstackedlayout",  "notsiggreenstackedlayout"};
            this.setSpacing(false);
            this.setRows(2);
            this.setColumns(1);
            this.setMargin(new MarginInfo(false, false, false, false));
            int rowCounter=0;
            int colCounter = 0;
            for (int i = 0; i < styleName.length; i++) {
                HorizontalLayout item = generateItemLabel(labels[i], styleName[i]);
                this.addComponent(item,colCounter,rowCounter++);
                this.setComponentAlignment(item, Alignment.TOP_LEFT);
               

            }

        }else {
            String[] labels = new String[]{"High", "High,not sign.", "Stable", "Low,not sign.", "Low"};
            String[] styleName = new String[]{"redlayout", "notsigredstackedlayout", "lightbluelayout", "notsiggreenstackedlayout", "greenlayout"};
            this.setSpacing(true);
            this.setRows(1);
            this.setColumns(5);
            this.setMargin(new MarginInfo(false, false, false, false));
          
            for (int i = 0; i < styleName.length; i++) {
                HorizontalLayout item = generateItemLabel(labels[i], styleName[i]);
                this.addComponent(item,i,0);
                this.setComponentAlignment(item, Alignment.MIDDLE_CENTER);
                
                

            }

        }

    }

    private HorizontalLayout generateItemLabel(String label, String style) {

        HorizontalLayout labelLayout = new HorizontalLayout();
        labelLayout.setSpacing(true);
        labelLayout.setHeight("20px");
        VerticalLayout icon = new VerticalLayout();
        icon.setWidth("10px");
        icon.setHeight("10px");
        icon.setStyleName(style);
        labelLayout.addComponent(icon);
        labelLayout.setComponentAlignment(icon, Alignment.MIDDLE_LEFT);
        Label l = new Label("<font size='2' face='Verdana'>" + label + "</font>");
        l.setContentMode(ContentMode.HTML);
        labelLayout.addComponent(l);

        return labelLayout;

    }

}
