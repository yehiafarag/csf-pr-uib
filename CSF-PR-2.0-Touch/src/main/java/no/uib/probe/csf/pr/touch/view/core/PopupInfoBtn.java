/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;

/**
 *
 * @author Yehia Farag
 *
 * this class represent pupup button layout
 */
public class PopupInfoBtn extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private final PopupWindow popupPanel;

    public PopupInfoBtn(QuantDatasetObject quantDS, String btnName, String publicationAuthor, boolean smallScreen) {
        this.addLayoutClickListener(PopupInfoBtn.this);
        this.setHeight("80px");
        this.setWidth("200px");
        this.setDescription("Click to view dataset information");
        Label btnLabel = new Label(btnName);
        btnLabel.setContentMode(ContentMode.HTML);
        this.addComponent(btnLabel);
        this.setComponentAlignment(btnLabel, Alignment.MIDDLE_CENTER);
        this.setStyleName("tabbtn");

        this.addStyleName("margintop");
        this.addStyleName("marginbottom");

        DatasetInformationOverviewLayout infoLayout = new DatasetInformationOverviewLayout(quantDS, smallScreen);
        VerticalLayout infoPopup = initPopupLayout(infoLayout,smallScreen,650);

        popupPanel = new PopupWindow(infoPopup, publicationAuthor);

        if (smallScreen) {
//            popupPanel.setHeight(Math.max(popupPanel.getHeight(), 500), Unit.PIXELS);
            popupPanel.setWidth(99,Unit.PERCENTAGE);
            popupPanel.setHeight(99,Unit.PERCENTAGE);
        } else {
            popupPanel.setHeight(710, Unit.PIXELS);
        }

//        popupPanel = new PopupView(null, infoPopup);
//        popupPanel.setWidth("2px");
//        popupPanel.setHeight("2px");
//        this.addComponent(popupPanel);
//        this.setComponentAlignment(popupPanel, Alignment.BOTTOM_RIGHT);
//        popupPanel.setVisible(true);
//        popupPanel.setPopupVisible(false);
//        popupPanel.setHideOnMouseOut(false);
        this.setExpandRatio(btnLabel, 0.99f);
//        this.setExpandRatio(popupPanel, 0.01f);

    }

    public PopupInfoBtn(String btnName, String publicationAuthor, Object[] publicationData, boolean smallScreen) {
        this.addLayoutClickListener(PopupInfoBtn.this);
        this.setDescription("Click to view publication information");
        this.setHeight(80, Unit.PIXELS);
        this.setWidth(200, Unit.PIXELS);
        Label btnLabel = new Label(btnName);
        btnLabel.setContentMode(ContentMode.HTML);
        this.addComponent(btnLabel);
        this.setComponentAlignment(btnLabel, Alignment.MIDDLE_CENTER);
        this.setStyleName("tabbtn");
        this.addStyleName("margintop");
        this.addStyleName("marginbottom");
        

        VerticalLayout infoLayout = initPublicationLayout(publicationData);
        VerticalLayout infoPopup = initPopupLayout(infoLayout,smallScreen,349);

        popupPanel = new PopupWindow(infoPopup, publicationAuthor);
        popupPanel.setWidth(600, Unit.PIXELS);
        popupPanel.setHeight(409, Unit.PIXELS);

        this.setExpandRatio(btnLabel, 0.99f);

    }

    private VerticalLayout initPopupLayout(VerticalLayout infoLayout, boolean smallScreen,int h) {

//        VerticalLayout popupBodyWrapper = new VerticalLayout();
//        popupBodyWrapper.setWidth(infoLayout.getWidth()+100,infoLayout.getWidthUnits());
//        popupBodyWrapper.setHeight(infoLayout.getHeight()+100,infoLayout.getHeightUnits());       
//        
        VerticalLayout popupBodyLayout = new VerticalLayout();
        if (smallScreen) {

            popupBodyLayout.setWidth(100, Unit.PERCENTAGE);
            popupBodyLayout.setHeight(100, Unit.PERCENTAGE);
        } else {
            popupBodyLayout.setWidth(99, Unit.PERCENTAGE);
            popupBodyLayout.setHeight(h, Unit.PIXELS);
            popupBodyLayout.addStyleName("padding20");
            popupBodyLayout.setSpacing(true);
        }
        popupBodyLayout.setStyleName("pupupbody");
        popupBodyLayout.addStyleName("roundedborder");
        popupBodyLayout.addStyleName("margintop");
        popupBodyLayout.addStyleName("marginbottom");

//        popupBodyWrapper.addComponent(popupBodyLayout);
//        popupBodyWrapper.setComponentAlignment(popupBodyLayout, Alignment.TOP_CENTER);
//        popupBodyLayout.addLayoutClickListener(this);
        popupBodyLayout.addComponent(infoLayout);

        return popupBodyLayout;

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        popupPanel.setVisible(!popupPanel.isVisible());
    }

    private VerticalLayout initPublicationLayout(Object[] publicationData) {

        VerticalLayout publicationlayout = new VerticalLayout();
        publicationlayout.setWidth("500px");
        publicationlayout.setHeightUndefined();
        publicationlayout.setSpacing(true);
        publicationlayout.setStyleName("publicationstyle");

        Label pubmedIdLabel = new Label("<h5>PubMed Id: <a class='link' href='http://www.ncbi.nlm.nih.gov/pubmed/" + publicationData[0].toString() + "' target='_blank'  >" + publicationData[0].toString() + "</a></h5>");

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

        Label titleLabel = new Label("<textarea rows='5' cols='72' readonly >" + publicationData[3].toString() + "</textarea>");
        titleLabel.setContentMode(ContentMode.HTML);
        publicationlayout.addComponent(titleLabel);
        titleLabel.setHeight("100px");

        return publicationlayout;

    }

}
