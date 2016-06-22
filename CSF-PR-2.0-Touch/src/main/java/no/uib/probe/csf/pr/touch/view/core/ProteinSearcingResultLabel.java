/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.awt.Color;
import java.util.Set;

/**
 *
 * @author Yehia Farag
 */
public class ProteinSearcingResultLabel extends HorizontalLayout implements LayoutEvents.LayoutClickListener {
    
    private final PopupView chartPanel;
    private final PieChart chart;
    private boolean initChart = true;
    private boolean selected = false;
    private final String proteinKey;

    public String getProteinKey() {
        return proteinKey;
    }
   
    
    public ProteinSearcingResultLabel(String title, String[] items, Integer[] values, Color[] colors) {
        this.setWidth(250, Unit.PIXELS);
        this.setHeight(40, Unit.PIXELS);
        this.addStyleName("searchingresultslabel");
        this.setDescription(title.split("__")[1]);
        this.proteinKey = title.split("__")[0];
        Label proteinNameLabel = new Label(title.split("__")[1]);
        proteinNameLabel.addStyleName(ValoTheme.LABEL_TINY);
        proteinNameLabel.addStyleName(ValoTheme.LABEL_SMALL);
        proteinNameLabel.setWidth(90, Unit.PERCENTAGE);
        this.addComponent(proteinNameLabel);
        this.addLayoutClickListener(ProteinSearcingResultLabel.this);
        
        VerticalLayout popupbodyLayout = new VerticalLayout();
        popupbodyLayout.setSpacing(true);
        popupbodyLayout.setWidth(270, Unit.PIXELS);
        popupbodyLayout.setMargin(new MarginInfo(false, false, false, false));
        popupbodyLayout.addStyleName("border");
        
        CloseButton closePopup = new CloseButton();
        closePopup.setWidth(10, Unit.PIXELS);
        closePopup.setHeight(10, Unit.PIXELS);
        popupbodyLayout.addComponent(closePopup);
        popupbodyLayout.setComponentAlignment(closePopup, Alignment.TOP_RIGHT);
        closePopup.addStyleName("translateleft10");
        
        chartPanel = new PopupView(null, popupbodyLayout) {
            
            @Override
            public void setPopupVisible(boolean visible) {
                setSelected(!chart.getSelectionSet().isEmpty());
                this.setVisible(visible);
                super.setPopupVisible(visible); //To change body of generated methods, choose Tools | Templates.
            }
            
        };
        closePopup.addLayoutClickListener((LayoutEvents.LayoutClickEvent event) -> {
            chartPanel.setPopupVisible(false);
        });
        chartPanel.setVisible(false);
        chartPanel.setCaptionAsHtml(true);
        chartPanel.setHideOnMouseOut(false);
        this.addComponent(chartPanel);
        
        chart = new PieChart(250, 200, title.split("__")[1]);
        chart.initializeFilterData(items, values, colors);
        popupbodyLayout.addComponent(chart);
        popupbodyLayout.setComponentAlignment(chart, Alignment.MIDDLE_CENTER);
    }
    
    public Set<String> getSelectionSet() {
        return chart.getSelectionSet();
    }


    public void setSelected(boolean selected) {
        if (selected) {
            this.addStyleName("selected");
        } else {
            this.removeStyleName("selected");
        }
        
    }
    
    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (initChart) {
            initChart = false;
            chart.redrawChart();
        }
        chartPanel.setPopupVisible(true);
    }
    
}
