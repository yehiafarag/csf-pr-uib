/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 * @author Yehia Farag
 *
 * this class represents Information (?) button that provide users with the
 * required information
 */
public class InformationButton extends ImageContainerBtn {

    private final PopupView popupLayout;
    private final VerticalLayout popupBodyLayout;

    public InformationButton(String infoText, boolean small) {
        updateIcon(new ThemeResource("img/help.png"));
        if (small) {
            this.setHeight(30, Unit.PIXELS);
            this.setWidth(30, Unit.PIXELS);
            this.addStyleName("midimg");
        } else {
            this.setHeight(45, Unit.PIXELS);
            this.setWidth(45, Unit.PIXELS);
           this.addStyleName("smallimg");
        }
 
       
        this.setDescription("click to view information");
        this.setEnabled(true);
        this.setReadOnly(false);

        popupBodyLayout = new VerticalLayout();
        VerticalLayout mainBody = new VerticalLayout();
        
//        mainBody.setWidth(450, Unit.PIXELS);
        mainBody.setHeightUndefined();
        mainBody.setStyleName("border");
        mainBody.addStyleName("infoLayout");

        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth(100, Unit.PERCENTAGE);
        mainBody.addComponent(topLayout);

        Label infoHeaderLabel = new Label("Information");
        infoHeaderLabel.setStyleName(ValoTheme.LABEL_H3);
//        infoHeaderLabel.addStyleName(ValoTheme.LABEL_BOLD);
        topLayout.addComponent(infoHeaderLabel);

        Label infoLable = new Label(infoText);
        infoLable.setContentMode(ContentMode.HTML);
        infoLable.setWidth(100, Unit.PERCENTAGE);
        mainBody.addComponent(infoLable);
        popupBodyLayout.addComponent(mainBody);

        CloseButton closeBtn = new CloseButton();
        topLayout.addComponent(closeBtn);
        topLayout.setComponentAlignment(closeBtn, Alignment.TOP_RIGHT);
        popupLayout = new PopupView(null, popupBodyLayout) {

            @Override
            public void setPopupVisible(boolean visible) {
                super.setPopupVisible(visible);
                this.setVisible(visible);
            }

        };
        popupLayout.setVisible(false);
        this.addComponent(popupLayout);
        this.popupLayout.setHideOnMouseOut(false);

        closeBtn.addLayoutClickListener((LayoutEvents.LayoutClickEvent event) -> {
            popupLayout.setPopupVisible(false);
        });
        popupLayout.addStyleName("infoLayout");

    }

    @Override
    public void onClick() {
        popupLayout.setPopupVisible(true);
    }

}
