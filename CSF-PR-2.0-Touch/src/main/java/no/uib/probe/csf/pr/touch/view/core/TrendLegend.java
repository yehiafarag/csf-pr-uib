/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 * @author Yehia Farag
 */
public class TrendLegend extends GridLayout {

    public TrendLegend(String type) {
        this.setSpacing(true);
         if (type.equalsIgnoreCase("miniscatterpeptidessequence")) {
            String[] labels = new String[]{"Increased", "Equal", "Decreased ", "No Quant. Info."};
            String[] styleName = new String[]{"legendtared100", "legendbluedm", "legendgreenta100", "legendgraydm"};
            this.setSpacing(true);
            this.setColumns(4);
            this.setRows(1);
            for (int i = 0; i < styleName.length; i++) {
                HorizontalLayout item = generateItemLabel(labels[i], styleName[i]);
                this.addComponent(item, i, 0);
                this.setComponentAlignment(item, Alignment.MIDDLE_CENTER);

            }

        } 
         else if (type.equalsIgnoreCase("linechart")) {
            String[] labels = new String[]{"Increased   100%", "Increased < 100%", "Equal", "Decreased < 100%", "Decreased   100%", "No Quant. Info.", "No Data"};
            String[] styleName = new String[]{"legendtared100", "legendredtaless100", "legendbluedm", "legendgreentaless100", "legendgreenta100", "legendgraydm", "legendemptygraydm"};
            this.setSpacing(true);
            this.setColumns(7);
            this.setRows(1);
            this.setMargin(new MarginInfo(false, true, false, false));
            for (int i = 0; i < styleName.length; i++) {
                HorizontalLayout item = generateItemLabel(labels[i], styleName[i]);
                this.addComponent(item, i, 0);
                this.setComponentAlignment(item, Alignment.MIDDLE_CENTER);

            }

        } else if (type.equalsIgnoreCase("bubblechart")) {
            String[] labels = new String[]{"Increased   100%", "Increased < 100%", "Equal", "Decreased < 100%", "Decreased   100%", "No Quant. Info."};
            String[] styleName = new String[]{"legendred100", "legendredless100", "legendblue", "legendgreenless100", "legendgreen100", "legendgray"};
            this.setSpacing(true);
            this.setColumns(6);
            this.setRows(1);
            this.setMargin(new MarginInfo(false, true, false, false));
            for (int i = 0; i < styleName.length; i++) {
                HorizontalLayout item = generateItemLabel(labels[i], styleName[i]);
                this.addComponent(item, i, 0);
                this.setComponentAlignment(item, Alignment.MIDDLE_CENTER);

            }

        } else if (type.equalsIgnoreCase("table")) {
            String[] labels = new String[]{"Increased", "Equal", "Decreased", "No Quant. Info.", "Not Available"};
            String[] styleName = new String[]{"redlayout", "lightbluelayout", "greenlayout", "novaluelayout", "empty"};
            this.setSpacing(true);
            this.setColumns(5);
            this.setRows(1);
            this.setMargin(new MarginInfo(false, true, false, false));
            for (int i = 0; i < styleName.length; i++) {
                HorizontalLayout item = generateItemLabel(labels[i], styleName[i]);
                this.addComponent(item, i, 0);
                this.setComponentAlignment(item, Alignment.MIDDLE_CENTER);

            }
        } else if (type.equalsIgnoreCase("ministackedpeptidessequence")) {
            String[] labels = new String[]{"Increased", "Increased-not sign.", "Equal", "Decreased", "Decreased-not sign.", "No Quant. Info.","PTM"};
            String[] styleName = new String[]{"redlayout", "notsigredstackedlayout", "lightbluelayout", "greenlayout", "notsiggreenstackedlayout", "novaluelayout","ptmglycosylation"};
            this.setSpacing(false);
            this.setRows(1);
            this.setColumns(7);
            this.setMargin(new MarginInfo(false, false, false, false));

            int colCounter = 0;
            int rowCounter = 0;
            for (int i = 0; i < styleName.length; i++) {
                HorizontalLayout item = generateItemLabel(labels[i], styleName[i]);
                this.addComponent(item, colCounter++, rowCounter);
//                if (colCounter == 3) {
//                    colCounter = 0;
//                    rowCounter++;
//                }
                this.setComponentAlignment(item, Alignment.TOP_LEFT);

            }

        } else {
            String[] labels = new String[]{"Increased", "Increased-not sign.", "Equal", "Decreased-not sign.", "Decreased", "No Quant. Info.","PTM"};
            String[] styleName = new String[]{"redlayout", "notsigredstackedlayout", "lightbluelayout", "notsiggreenstackedlayout", "greenlayout", "novaluelayout","ptmglycosylation"};
            this.setSpacing(true);
            this.setRows(1);
            this.setColumns(7);
            this.setMargin(new MarginInfo(false, false, false, false));

            for (int i = 0; i < styleName.length; i++) {
                HorizontalLayout item = generateItemLabel(labels[i], styleName[i]);
                this.addComponent(item, i, 0);
                this.setComponentAlignment(item, Alignment.MIDDLE_CENTER);

            }

        }

    }

    private HorizontalLayout generateItemLabel(String label, String style) {

        HorizontalLayout labelLayout = new HorizontalLayout();
        labelLayout.setSpacing(true);
        labelLayout.setHeightUndefined();
        VerticalLayout icon = new VerticalLayout();
        icon.setWidth("10px");
        icon.setHeight("10px");
        icon.setStyleName(style);
        labelLayout.addComponent(icon);
        Label l = new Label( label  );
        labelLayout.addComponent(l);
        l.setStyleName(ValoTheme.LABEL_BOLD);
        l.addStyleName(ValoTheme.LABEL_SMALL);
        l.addStyleName(ValoTheme.LABEL_TINY);

        return labelLayout;

    }

    public TrendLegend(int userTrend) {
        String[] labels = new String[]{"Increased   100%", "Increased < 100%", "Equal", "Decreased < 100%", "Decreased   100%", "No Quant. Info.", "No Data"};
        String[] styleName = new String[]{"legendtared100", "legendredtaless100", "legendbluedm", "legendgreentaless100", "legendgreenta100", "legendgraydm", "legendemptygraydm"};
        this.setSpacing(true);
        this.setColumns(8);
        this.setRows(1);
        this.setMargin(new MarginInfo(false, true, false, false));
        for (int i = 0; i < styleName.length; i++) {
            HorizontalLayout item = generateItemLabel(labels[i], styleName[i]);
            this.addComponent(item, i, 0);
            this.setComponentAlignment(item, Alignment.MIDDLE_CENTER);

        }
        HorizontalLayout item;
        if (userTrend == 0 ||userTrend == 1) { 
            item = generateItemLabel("User Data", "custuserdown");
            
        } else if (userTrend == 2) {
            item = generateItemLabel("User Data", "custuserstable");
        } else {
           item = generateItemLabel("User Data", "custuserup");
        }
        this.addComponent(item, 7, 0);
        this.setComponentAlignment(item, Alignment.MIDDLE_CENTER);

    }

}
