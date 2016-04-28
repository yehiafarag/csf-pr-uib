/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;

/**
 *
 * @author Yehia Farag
 *
 * this class represent pupup button layout
 */
public class PopupInfoBtn extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private final PopupView pupupPanel;

    public PopupInfoBtn(QuantDatasetObject quantDS, String btnName, String publicationAuthor, String diseaseHashedColor) {
        this.addLayoutClickListener(PopupInfoBtn.this);
        this.setHeight("80px");
        this.setWidth("200px");
        Label btnLabel = new Label(btnName);
        btnLabel.setContentMode(ContentMode.HTML);
        this.addComponent(btnLabel);
        this.setComponentAlignment(btnLabel, Alignment.MIDDLE_CENTER);
        this.setStyleName("tabbtn");

        DatasetInformationOverviewLayout infoLayout = new DatasetInformationOverviewLayout(quantDS, diseaseHashedColor);
        VerticalLayout infoPopup = initPopupLayout(infoLayout, publicationAuthor);
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

   
    public PopupInfoBtn(String btnName, String publicationAuthor, Object[] publicationData) {
        this.addLayoutClickListener(PopupInfoBtn.this);
        this.setHeight("80px");
        this.setWidth("200px");
        Label btnLabel = new Label(btnName);
        btnLabel.setContentMode(ContentMode.HTML);
        this.addComponent(btnLabel);
        this.setComponentAlignment(btnLabel, Alignment.MIDDLE_CENTER);
        this.setStyleName("tabbtn");

        //add popup for testing 
        VerticalLayout infoLayout = initPublication(publicationData);
        VerticalLayout infoPopup = initPopupLayout(infoLayout, publicationAuthor);
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

    private VerticalLayout initPublication(Object[] publicationData) {
        VerticalLayout infoLayout = initPublicationLayout(publicationData);
        return infoLayout;

    }

    private VerticalLayout initPopupLayout(VerticalLayout infoLayout, String header) {

        VerticalLayout popupBodyWrapper = new VerticalLayout();
        popupBodyWrapper.setWidth(infoLayout.getWidth()+100,infoLayout.getWidthUnits());
        popupBodyWrapper.setHeight(infoLayout.getHeight()+100,infoLayout.getHeightUnits());       
        
        VerticalLayout popupBodyLayout = new VerticalLayout();
        popupBodyLayout.setWidth(100+ "%");
        popupBodyLayout.setHeightUndefined();
        popupBodyLayout.setStyleName("pupupbody");
        popupBodyLayout.setSpacing(true);
        popupBodyWrapper.addComponent(popupBodyLayout);
        popupBodyWrapper.setComponentAlignment(popupBodyLayout, Alignment.TOP_CENTER);
        
        
        HorizontalLayout titleLayout = new HorizontalLayout();
        titleLayout.setWidthUndefined();
        titleLayout.setHeight("40px");
//
        popupBodyLayout.addComponent(titleLayout);
        popupBodyLayout.addLayoutClickListener(this);

        Label label = new Label("<b>&nbsp;&nbsp;" + header + "</b>");
        label.setContentMode(ContentMode.HTML);
        label.setWidth((infoLayout.getWidth() - 17) + "px");
        titleLayout.addComponent(label);

        CloseButton closeBtn = new CloseButton();
        closeBtn.addLayoutClickListener(this);
        titleLayout.addComponent(closeBtn);
        titleLayout.setComponentAlignment(closeBtn, Alignment.TOP_RIGHT);
//
//        infoLayout.setMargin(true);
//        infoLayout.setSpacing(true);
        popupBodyLayout.addComponent(infoLayout);


        return popupBodyWrapper;

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        pupupPanel.setPopupVisible(!pupupPanel.isPopupVisible());
    }

    private VerticalLayout initPublicationLayout(Object[] publicationData) {

        VerticalLayout publicationlayout = new VerticalLayout();
        publicationlayout.setWidth("500px");
        publicationlayout.setHeightUndefined();
        publicationlayout.setSpacing(true);
        publicationlayout.setMargin(new MarginInfo(false, false, false, false));
        publicationlayout.setStyleName("publicationstyle");

        Label pubmedIdLabel = new Label("<h5>Pubmed Id: <a href='http://www.ncbi.nlm.nih.gov/pubmed/" + publicationData[0].toString() + "' target='_blank'  ><font style:'text-decoration:underline !important;'>" + publicationData[0].toString() + "</font></a></h5>");
        pubmedIdLabel.setContentMode(ContentMode.HTML);
        publicationlayout.addComponent(pubmedIdLabel);
        pubmedIdLabel.setHeight("40px");

        Label proteinsNumLabel = new Label("<h5>#Proteins: " + publicationData[5].toString() + "</h5>");
        proteinsNumLabel.setDescription("Number of publication proteins " + publicationData[4].toString());
        proteinsNumLabel.setContentMode(ContentMode.HTML);
        publicationlayout.addComponent(proteinsNumLabel);
        proteinsNumLabel.setHeight("40px");

        Label uproteinsNumLabel = new Label("<h5>#Publication Specific Proteins: " + publicationData[4].toString() + "</h5>");
        uproteinsNumLabel.setDescription("Number of publication specific proteins " + publicationData[4].toString());
        uproteinsNumLabel.setContentMode(ContentMode.HTML);
        publicationlayout.addComponent(uproteinsNumLabel);
        uproteinsNumLabel.setHeight("40px");

        Label PeptidesNumLabel = new Label("<h5>#Peptides: " + publicationData[7].toString() + "</h5>");
        PeptidesNumLabel.setContentMode(ContentMode.HTML);
        publicationlayout.addComponent(PeptidesNumLabel);
        PeptidesNumLabel.setDescription("Number of publication peptides " + publicationData[7].toString());
        PeptidesNumLabel.setHeight("40px");

        Label uPeptidesNumLabel = new Label("<h5>#Publication Specific Peptides: " + publicationData[6].toString() + "</h5>");
        uPeptidesNumLabel.setContentMode(ContentMode.HTML);
        publicationlayout.addComponent(uPeptidesNumLabel);
        uPeptidesNumLabel.setDescription("Number of publication specific  peptides " + publicationData[6].toString());
        uPeptidesNumLabel.setHeight("40px");

        Label titleLabel = new Label("<textarea rows='5' cols='52' readonly >" + publicationData[3].toString() + "</textarea>");
        titleLabel.setContentMode(ContentMode.HTML);
        publicationlayout.addComponent(titleLabel);
        titleLabel.setHeight("100px");

        return publicationlayout;

    }

}
