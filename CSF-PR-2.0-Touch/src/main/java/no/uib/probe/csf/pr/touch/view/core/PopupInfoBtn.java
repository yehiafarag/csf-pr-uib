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

    private final PopupWindowFrame popupPanel;

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
        popupPanel = new PopupWindowFrame(publicationAuthor, new VerticalLayout(infoLayout));

        popupPanel.setFrameHeight(710);

        this.setExpandRatio(btnLabel, 0.99f);

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

        popupPanel = new PopupWindowFrame(publicationAuthor, new VerticalLayout(infoLayout));
        popupPanel.setFrameWidth(600);
        popupPanel.setFrameHeight(409);

        this.setExpandRatio(btnLabel, 0.99f);

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        popupPanel.view();
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
