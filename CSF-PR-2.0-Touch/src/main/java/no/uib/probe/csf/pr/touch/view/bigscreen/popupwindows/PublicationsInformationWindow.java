/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.bigscreen.popupwindows;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import java.util.List;
import no.uib.probe.csf.pr.touch.view.core.PopupInfoBtn;

/**
 *
 * @author Yehia Farag
 */
public class PublicationsInformationWindow extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private final Window popupWindow;

    public PublicationsInformationWindow(List<Object[]> publicationList) {

        int height = Page.getCurrent().getBrowserWindowHeight() - 100;
        int width = Page.getCurrent().getBrowserWindowWidth() - 100;
        int columnNum = width / 250;
        width = columnNum * 250;
        VerticalLayout popupBodyWrapper = new VerticalLayout();
        popupBodyWrapper.setWidth("100%");
        popupBodyWrapper.setHeight("100%");
        VerticalLayout popupBody = new VerticalLayout();
        popupBody.setWidth((width) + "px");
        popupBody.setHeightUndefined();
        popupBody.setStyleName("whitelayout");
        popupBody.setMargin(true);
        popupBody.setSpacing(true);
        popupBodyWrapper.addComponent(popupBody);
        popupBodyWrapper.setComponentAlignment(popupBody, Alignment.TOP_CENTER);

        popupWindow = new Window() {

            @Override
            public void close() {
                popupWindow.setVisible(false);

            }

        };

        popupWindow.setContent(popupBodyWrapper);
        popupWindow.setWindowMode(WindowMode.NORMAL);

        popupWindow.setWidth((width + 22) + "px");

        popupWindow.setVisible(false);
        popupWindow.setResizable(false);
        popupWindow.setClosable(false);
        popupWindow.setModal(true);
        popupWindow.setDraggable(false);
        popupWindow.setCaption("<font color='gray' style='font-weight: bold;!important'>&nbsp;&nbsp;Publication Information</font>");

        UI.getCurrent().addWindow(popupWindow);
        popupWindow.center();

        popupWindow.setCaptionAsHtml(true);
        popupWindow.setClosable(true);

        GridLayout publicationContainer = new GridLayout();
        publicationContainer.setWidth("100%");
        publicationContainer.setSpacing(true);
        publicationContainer.setMargin(false);
        popupBody.addComponent(publicationContainer);

        publicationContainer.setColumns(columnNum);
        publicationContainer.setRows(publicationList.size());

        this.addLayoutClickListener(PublicationsInformationWindow.this);
        int row = 0;
        int col = 0;

        for (Object[] obj : publicationList) {
            VerticalLayout publicationLayout = initPublicationLayout(obj);
            String btnName = "<font size=1 >"+obj[0].toString()+"</font><br/>"+obj[1].toString() + "<br/><font size=1 >" + obj[2].toString() + "</font><br/><font size=1 >#Proteins: " + obj[5].toString() /*+ "/" + obj[5].toString() + */+"   #Peptides: " + obj[7].toString() /*+ "/" + obj[7].toString() +*/+ "</font>";

            PopupInfoBtn publicationBtn = new PopupInfoBtn(publicationLayout, btnName,obj[1].toString());
            publicationContainer.addComponent(publicationBtn, col++, row);
            publicationContainer.setComponentAlignment(publicationBtn, Alignment.TOP_CENTER);
            if (col >= columnNum) {
                row++;
                col = 0;

            }
        }
        height = Math.min((++row*85)+200,height);
        popupWindow.setHeight((height) + "px");

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

        Label proteinsNumLabel = new Label("<h5>#Proteins: "+ publicationData[5].toString() + "</h5>");
        proteinsNumLabel.setDescription("Number of publication proteins " + publicationData[4].toString());
        proteinsNumLabel.setContentMode(ContentMode.HTML);
        publicationlayout.addComponent(proteinsNumLabel);
        proteinsNumLabel.setHeight("40px");        
        
        Label uproteinsNumLabel = new Label("<h5>#Publication Specific Proteins: " + publicationData[4].toString() + "</h5>");
        uproteinsNumLabel.setDescription("Number of publication specific proteins " + publicationData[4].toString());
        uproteinsNumLabel.setContentMode(ContentMode.HTML);
        publicationlayout.addComponent(uproteinsNumLabel);
        uproteinsNumLabel.setHeight("40px");        

        Label PeptidesNumLabel = new Label("<h5>#Peptides: " +publicationData[7].toString() + "</h5>");
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

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        popupWindow.setVisible(true);
    }
}
