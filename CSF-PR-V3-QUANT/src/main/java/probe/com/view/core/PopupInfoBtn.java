/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Yehia Farag
 *
 * this class represent pupup button layout
 */
public class PopupInfoBtn extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private final PopupView pupupPanel;

    public PopupInfoBtn(VerticalLayout pupupLayout, String btnName) {
//        String btnName = data[0] + " (" + data[1] + ")<br/><font size=1 >#Proteins: " + data[3] + "    #peptides: " + data[4] + "</font>";
        this.addLayoutClickListener(PopupInfoBtn.this);
        this.setHeight("80px");
        this.setWidth("200px");
        Label btnLabel = new Label(btnName);
        btnLabel.setContentMode(ContentMode.HTML);
        this.addComponent(btnLabel);
        this.setComponentAlignment(btnLabel, Alignment.MIDDLE_CENTER);
        this.setStyleName("tabbtn");

        //add popup for testing 
        VerticalLayout infoPopup = initPopupLayout(pupupLayout, btnName);
        pupupPanel = new PopupView(null, infoPopup);
        pupupPanel.setWidth("2px");
        pupupPanel.setHeight("2px");
        this.addComponent(pupupPanel);
        this.setComponentAlignment(pupupPanel, Alignment.BOTTOM_RIGHT);
        pupupPanel.setVisible(true);
        pupupPanel.setPopupVisible(false);
        pupupPanel.setHideOnMouseOut(false);
      
        this.setExpandRatio(btnLabel, 0.99f);
        this.setExpandRatio(pupupPanel, 0.01f);

    }

    private VerticalLayout initPopupLayout(VerticalLayout popupLayout, String header) {
         VerticalLayout popupBodyWrapper = new VerticalLayout();
        popupBodyWrapper.setWidth("100%");
        popupBodyWrapper.setHeight("100%");
        VerticalLayout popupBodyLayout = new VerticalLayout();
        popupBodyLayout.setWidth((popupLayout.getWidth()+2)+"px");
        popupBodyLayout.setHeightUndefined();
        popupBodyLayout.setStyleName("pupupbody");
        popupBodyLayout.setSpacing(true);
        HorizontalLayout titleLayout = new HorizontalLayout();
        titleLayout.setWidthUndefined();
        titleLayout.setHeight("40px");

        popupBodyLayout.addComponent(titleLayout);
        popupBodyLayout.addLayoutClickListener(this);

        Label label = new Label("<b>"+header+"</b>");
        label.setStyleName("comparisonHeaders");
        label.setContentMode(ContentMode.HTML);
        label.setWidth((popupLayout.getWidth()-17)+"px");
        titleLayout.addComponent(label);

        VerticalLayout closeBtn = new VerticalLayout();
        closeBtn.setWidth("16px");
        closeBtn.setHeight("16px");
        closeBtn.setStyleName("closepanelbtn");
        closeBtn.addLayoutClickListener(this);
        titleLayout.addComponent(closeBtn);
        titleLayout.setComponentAlignment(closeBtn, Alignment.TOP_RIGHT);

        popupLayout.setMargin(true);
        popupLayout.setSpacing(true);
        popupBodyLayout.addComponent(popupLayout);
        popupBodyWrapper.addComponent(popupBodyLayout);
        popupBodyWrapper.setComponentAlignment(popupBodyLayout, Alignment.MIDDLE_CENTER);

        return popupBodyWrapper;

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        pupupPanel.setPopupVisible(!pupupPanel.isPopupVisible());
    }

}
