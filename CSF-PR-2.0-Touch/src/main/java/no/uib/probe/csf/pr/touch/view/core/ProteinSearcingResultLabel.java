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

/**
 *
 * @author Yehia Farag
 */
public class ProteinSearcingResultLabel extends HorizontalLayout implements LayoutEvents.LayoutClickListener {

    private final PopupView chartPanel;
    private final PieChart chart;
    private boolean initChart = true;

    public ProteinSearcingResultLabel(String title, String[] items, Integer[] values, Color[] colors) {
        this.setWidthUndefined();
        this.setHeight(50, Unit.PIXELS);
        Label proteinNameLabel = new Label(title);
        proteinNameLabel.addStyleName(ValoTheme.LABEL_TINY);
        proteinNameLabel.addStyleName(ValoTheme.LABEL_SMALL);
        proteinNameLabel.setWidth(250, Unit.PIXELS);
        this.addComponent(proteinNameLabel);
        this.addLayoutClickListener(ProteinSearcingResultLabel.this);

        VerticalLayout popupbodyLayout = new VerticalLayout();
        popupbodyLayout.setSpacing(true);
        popupbodyLayout.setWidth(260, Unit.PIXELS);
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

        chart = new PieChart(250, 200, title);
        chart.initializeFilterData(items, values, colors);
        popupbodyLayout.addComponent(chart);
         popupbodyLayout.setComponentAlignment(chart,Alignment.MIDDLE_CENTER);
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
