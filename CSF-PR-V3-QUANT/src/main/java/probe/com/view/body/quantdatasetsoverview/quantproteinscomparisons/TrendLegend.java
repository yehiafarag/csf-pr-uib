/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
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
        this.setSpacing(true);
         if (type.equalsIgnoreCase("bubblechart")) {
            String[] labels = new String[]{"High   100%","High < 100%", "Stable", "Low < 100%","Low   100%","No Quant. Info."};
            String[] styleName = new String[]{"legendred100", "legendredless100", "legendblue","legendgreenless100","legendgreen100","legendgray"};
            this.setSpacing(true);
            this.setColumns(6);
            this.setRows(1);
            this.setMargin(new MarginInfo(false, true, false, false));
            for (int i = 0; i < styleName.length; i++) {
                HorizontalLayout item = generateItemLabel(labels[i], styleName[i]);
                this.addComponent(item, i, 0);
                this.setComponentAlignment(item, Alignment.MIDDLE_CENTER);

            }
        
         }
            else if (type.equalsIgnoreCase("table")) {
            String[] labels = new String[]{"High", "Stable", "Low","No Quant. Info.","Not Available"};
            String[] styleName = new String[]{"redlayout", "lightbluelayout", "greenlayout","novaluelayout","empty"};
            this.setSpacing(true);
            this.setColumns(5);
            this.setRows(1);
            this.setMargin(new MarginInfo(false, true, false, false));
            for (int i = 0; i < styleName.length; i++) {
                HorizontalLayout item = generateItemLabel(labels[i], styleName[i]);
                this.addComponent(item, i, 0);
                this.setComponentAlignment(item, Alignment.MIDDLE_CENTER);

            }
        }else if(type.equalsIgnoreCase("ministackedpeptidessequence")){
            String[] labels = new String[]{"High","High-not sign.", "Stable","Low", "Low-not sign.","No Quant. Info."};
            String[] styleName = new String[]{ "redlayout","notsigredstackedlayout", "lightbluelayout","greenlayout",  "notsiggreenstackedlayout", "novaluelayout"};
            this.setSpacing(false);
            this.setRows(2);
            this.setColumns(3);
            this.setMargin(new MarginInfo(false, false, false, false));
           
            int colCounter = 0;
            int rowCounter = 0;
            for (int i = 0; i < styleName.length; i++) {
                HorizontalLayout item = generateItemLabel(labels[i], styleName[i]);
                this.addComponent(item,colCounter++,rowCounter);
                if(colCounter==3){
                    colCounter=0;
                    rowCounter++;
                }
                this.setComponentAlignment(item, Alignment.TOP_LEFT);
               

            }

        }else {
            String[] labels = new String[]{"High", "High,not sign.", "Stable", "Low,not sign.", "Low","No Quant. Info."};
            String[] styleName = new String[]{"redlayout", "notsigredstackedlayout", "lightbluelayout", "notsiggreenstackedlayout", "greenlayout","novaluelayout"};
            this.setSpacing(true);
            this.setRows(1);
            this.setColumns(6);
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
