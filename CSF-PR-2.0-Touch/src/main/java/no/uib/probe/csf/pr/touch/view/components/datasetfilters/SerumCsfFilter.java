/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.components.datasetfilters;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Yehia Farag
 *
 * this class contains 2 filters CSF filter allows user to view CSF datasets
 * Serum filter allows user to view Serum datasets users need to select at least
 * on of the filters
 */
public abstract class SerumCsfFilter extends HorizontalLayout implements LayoutEvents.LayoutClickListener {

    public SerumCsfFilter() {
        this.setWidthUndefined();
        this.setHeightUndefined();
        this.setSpacing(true);

        final VerticalLayout noCSFOptionBtn = new VerticalLayout();
        noCSFOptionBtn.setDescription("Include CSF datasets");
        this.addComponent(noCSFOptionBtn);
        this.setComponentAlignment(noCSFOptionBtn, Alignment.TOP_LEFT);

        noCSFOptionBtn.setWidth(25, Unit.PIXELS);
        noCSFOptionBtn.setHeight(25, Unit.PIXELS);
        noCSFOptionBtn.setStyleName("filterbtn");
        Image noCSFIcon = new Image();
        noCSFIcon.setSource(new ThemeResource("img/bluedrop.png"));
        noCSFOptionBtn.addComponent(noCSFIcon);
        noCSFOptionBtn.setComponentAlignment(noCSFIcon, Alignment.TOP_CENTER);
        noCSFIcon.setHeight(100, Unit.PERCENTAGE);
        noCSFOptionBtn.addStyleName("applied");
        noCSFOptionBtn.addLayoutClickListener(SerumCsfFilter.this);

        noCSFOptionBtn.setData("csfBtn");

        final VerticalLayout noSerumOptionBtn = new VerticalLayout();
        noSerumOptionBtn.setDescription("Include serum datasets");
        this.addComponent(noSerumOptionBtn);
        this.setComponentAlignment(noSerumOptionBtn, Alignment.TOP_LEFT);

        noSerumOptionBtn.setWidth(25, Unit.PIXELS);
        noSerumOptionBtn.setHeight(25, Unit.PIXELS);
        noSerumOptionBtn.setStyleName("filterbtn");
        Image noSerumIcon = new Image();
        noSerumIcon.setSource(new ThemeResource("img/reddrop.png"));
        noSerumOptionBtn.addComponent(noSerumIcon);
        noSerumOptionBtn.setComponentAlignment(noSerumIcon, Alignment.TOP_CENTER);
        noSerumIcon.setHeight(100, Unit.PERCENTAGE);
        noSerumOptionBtn.addStyleName("unapplied");
        noSerumOptionBtn.addLayoutClickListener(SerumCsfFilter.this);
        noSerumOptionBtn.setData("serumBtn");

    }

    private boolean serumApplied = false;
    private boolean csfApplied = true;

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {

        VerticalLayout clickedBtn = (VerticalLayout) event.getComponent();
        if (clickedBtn.getData().toString().equalsIgnoreCase("csfBtn")) {
            if (csfApplied && !serumApplied) {
                Notification.show("You can not hide both CSF and serum datasets", Notification.Type.WARNING_MESSAGE);
            } else if (csfApplied && serumApplied) {
                csfApplied = false;
                clickedBtn.removeStyleName("applied");
                clickedBtn.addStyleName("unapplied");
                //update system
                updateSystem(serumApplied, csfApplied);

            } else if (!csfApplied) {
                csfApplied = true;
                clickedBtn.removeStyleName("unapplied");
                clickedBtn.addStyleName("applied");
                //update system

                updateSystem(serumApplied, csfApplied);
            }

        } else {
            if (!csfApplied && serumApplied) {
                Notification.show("You can not hide both CSF and serum datasets", Notification.Type.WARNING_MESSAGE);
            } else if (csfApplied && serumApplied) {
                serumApplied = false;
                clickedBtn.removeStyleName("applied");
                clickedBtn.addStyleName("unapplied");
                //update system

                updateSystem(serumApplied, csfApplied);

            } else if (!serumApplied) {
                serumApplied = true;
                clickedBtn.removeStyleName("unapplied");
                clickedBtn.addStyleName("applied");
                //update system

                updateSystem(serumApplied, csfApplied);
            }

        }

//        if (().getStyleName().contains("unapplied")) {
//            ((VerticalLayout) event.getComponent()).removeStyleName("unapplied");
//            ((VerticalLayout) event.getComponent()).addStyleName("applied");
//        } else {
//
//            ((VerticalLayout) event.getComponent()).removeStyleName("applied");
//            ((VerticalLayout) event.getComponent()).addStyleName("unapplied");
//        }
//
//        if (noCSFOption.getStyleName().equalsIgnoreCase("enablecsfunselected") && noSerumOption.getStyleName().equalsIgnoreCase("enableserumselected")) {
//            Notification.show("You can not hide both CSF and serum datasets", Notification.Type.TRAY_NOTIFICATION);
//            return;
//
//        }
//
//        if (noSerumOption.getStyleName().equalsIgnoreCase("enableserumunselected")) {
//            boolean success = Quant_Central_Manager.setHideSerum(false);
//            if (success) {
//                noSerumOption.setStyleName("enableserumselected");
//            } else {
//                Notification.show("No serum datasets available for the selected disease", Notification.Type.TRAY_NOTIFICATION);
//                return;
//            }
//        } else {
//            boolean success = Quant_Central_Manager.setHideSerum(true);
//            if (success) {
//                noSerumOption.setStyleName("enableserumunselected");
//
//            } else {
//                Notification.show("No serum datasets available for the selected disease", Notification.Type.TRAY_NOTIFICATION);
//                return;
//            }
//
//        }
    }

    public abstract void updateSystem(boolean serumApplied, boolean csfApplied);

}
