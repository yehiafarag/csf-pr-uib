/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.welcomelayout;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import java.util.List;
import org.dussan.vaadin.dcharts.options.Grid;
import probe.com.view.core.HideOnClickLayout;

/**
 *
 * @author Yehia Farag
 */
public class PublicationsInformationWindow extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private final Window popupWindow;

    public PublicationsInformationWindow(List<Object[]> publicationList) {

        int height = Page.getCurrent().getBrowserWindowHeight() - 100;
        int width = Page.getCurrent().getBrowserWindowWidth() - 100;
        VerticalLayout popupBody = new VerticalLayout();
        popupBody.setWidth((width) + "px");
        popupBody.setHeightUndefined();
        popupBody.setStyleName(Reindeer.LAYOUT_WHITE);
        popupBody.setMargin(true);
        popupBody.setSpacing(true);

        popupWindow = new Window() {

            @Override
            public void close() {
                popupWindow.setVisible(false);

            }

        };

        popupWindow.setContent(popupBody);
        popupWindow.setWindowMode(WindowMode.NORMAL);
        popupWindow.setWidth((width + 40) + "px");
        popupWindow.setHeight((height) + "px");
        popupWindow.setVisible(false);
        popupWindow.setResizable(false);
        popupWindow.setClosable(false);
        popupWindow.setStyleName(Reindeer.WINDOW_LIGHT);
        popupWindow.setModal(true);
        popupWindow.setDraggable(false);
        popupWindow.center();

        popupWindow.setCaption("&nbsp;&nbsp;Publication Information");

        UI.getCurrent().addWindow(popupWindow);
        popupWindow.setPositionX(30);
        popupWindow.setPositionY(40);

        popupWindow.setCaptionAsHtml(true);
        popupWindow.setClosable(true);

        GridLayout publicationContainer = new GridLayout();
        publicationContainer.setWidth("100%");
        publicationContainer.setSpacing(true);
        publicationContainer.setMargin(true);
        popupBody.addComponent(publicationContainer);
        int maxNump = height / 130;
        int columnNum = 0;
        int counter = 1;
        while (true) {
            if ((publicationList.size() / counter) < maxNump) {
                columnNum = counter;
                break;
            }
            counter++;

        }
        publicationContainer.setColumns(columnNum);
        publicationContainer.setRows(publicationList.size());

        this.addLayoutClickListener(PublicationsInformationWindow.this);
        int row = 0;
        int col = 0;

        for (Object[] obj : publicationList) {
            HideOnClickLayout publicationLayout = initPublicationLayout(obj);
            publicationContainer.addComponent(publicationLayout, col++, row);
            publicationContainer.setComponentAlignment(publicationLayout, Alignment.TOP_CENTER);
            if (row == 0 && col == 1) {
                publicationLayout.setVisability(true);
            }
            if (col >= columnNum) {
                col = 0;
                row++;
            }
        }

    }

    private HideOnClickLayout initPublicationLayout(Object[] publicationData) {

        VerticalLayout publicationlayout = new VerticalLayout();
        publicationlayout.setWidthUndefined();
        publicationlayout.setHeightUndefined();
        publicationlayout.setSpacing(false);
        publicationlayout.setMargin(new MarginInfo(false, false, true, false));
        publicationlayout.setStyleName("publicationstyle");

//        Label authorLabel = new Label("<a style='hight=30px' href='http://www.ncbi.nlm.nih.gov/pubmed/' target='_blank' ><h4>" + publicationData[1].toString() + " (" + publicationData[2].toString() + ")</h4></a>");//, new ExternalResource("http://www.ncbi.nlm.nih.gov/pubmed/"));
////        authorLabel.setTargetName("_blank");
//        authorLabel.setContentMode(ContentMode.HTML);
//        publicationlayout.addComponent(authorLabel);
//        authorLabel.setHeight("30px");
        Label pubmedIdLabel = new Label("<h5>Pubmed Id: <a href='http://www.ncbi.nlm.nih.gov/pubmed/" + publicationData[0].toString() + "' target='_blank'  ><font style:'text-decoration:underline !important;'>" + publicationData[0].toString() + "</font></a></h5>");
//        PeptidesNumLabel.setTargetName("_blank");
        pubmedIdLabel.setContentMode(ContentMode.HTML);
        publicationlayout.addComponent(pubmedIdLabel);
        pubmedIdLabel.setHeight("30px");

        HorizontalLayout protInfoLayout = new HorizontalLayout();
        protInfoLayout.setWidth("545px");

        protInfoLayout.setSpacing(true);
        publicationlayout.addComponent(protInfoLayout);

        Label proteinsNumLabel = new Label("<h5>#Proteins:<center style:'text-decoration:none !important;'>" + publicationData[4].toString() + "/" + publicationData[5].toString() + "</center> </h5>");
        proteinsNumLabel.setDescription("Number of publication proteins " + publicationData[4].toString() + "<br/>Number of unique proteins " + publicationData[5].toString());
//        PeptidesNumLabel.setTargetName("_blank");
        proteinsNumLabel.setContentMode(ContentMode.HTML);
        protInfoLayout.addComponent(proteinsNumLabel);
//        proteinsNumLabel.setHeight("30px");

        Label PeptidesNumLabel = new Label("<h5>#Peptides:<center style:'text-decoration:none !important;'>" + publicationData[6].toString() + "/" + publicationData[7].toString() + "</center> </h5>");
//        PeptidesNumLabel.setTargetName("_blank");
        PeptidesNumLabel.setContentMode(ContentMode.HTML);
        protInfoLayout.addComponent(PeptidesNumLabel);
        PeptidesNumLabel.setDescription("Number of publication peptides " + publicationData[6].toString() + "<br/>Number of unique peptides " + publicationData[7].toString());
//        PeptidesNumLabel.setWidth("545px");

        Label titleLabel = new Label("<textarea rows='4' cols='60' readonly >" + publicationData[3].toString() + "</textarea>");
        titleLabel.setContentMode(ContentMode.HTML);
        publicationlayout.addComponent(titleLabel);

        VerticalLayout miniLayout = new VerticalLayout();
        miniLayout.setWidth("100%");
        Label miniInfoLabel = new Label("<h5>#Proteins: " + publicationData[5].toString() + "  |  #Peptides: " + publicationData[7].toString() + "</h5>");
        miniInfoLabel.setContentMode(ContentMode.HTML);
        miniInfoLabel.setWidth("250px");
        miniLayout.addComponent(miniInfoLabel);
        miniLayout.setComponentAlignment(miniInfoLabel,Alignment.TOP_RIGHT);
        miniLayout.setStyleName("minipublicationstyle");
        HideOnClickLayout hideOnClick = new HideOnClickLayout(publicationData[1].toString() + " (" + publicationData[2].toString() + ")", publicationlayout, miniLayout, null, null);

        return hideOnClick;

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        popupWindow.setVisible(true);
    }
}
