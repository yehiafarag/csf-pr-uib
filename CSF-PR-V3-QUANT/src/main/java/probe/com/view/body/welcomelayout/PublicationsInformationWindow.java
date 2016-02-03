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
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import java.util.List;
import org.dussan.vaadin.dcharts.options.Grid;

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

        popupBody.setMargin(true);
        popupBody.setSpacing(true);

        GridLayout publicationContainer = new GridLayout();
        publicationContainer.setWidth("100%");
        publicationContainer.setSpacing(true);
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
            VerticalLayout publicationLayout = initPublicationLayout(obj);
            publicationContainer.addComponent(publicationLayout, col++, row);
             publicationContainer.setComponentAlignment(publicationLayout, Alignment.MIDDLE_CENTER);
            if (col >= columnNum) {
                col = 0;
                row++;
            }
        }

    }

    private VerticalLayout initPublicationLayout(Object[] publicationData) {

        VerticalLayout publicationlayout = new VerticalLayout();
        publicationlayout.setWidthUndefined();
        publicationlayout.setHeightUndefined();
        publicationlayout.setSpacing(false);
        publicationlayout.setMargin(new MarginInfo(true, false, false, false));
        publicationlayout.setStyleName("publicationstyle");

        Label authorLabel = new Label("<a style='hight=30px' href='http://www.ncbi.nlm.nih.gov/pubmed/' target='_blank' ><h4>Farag Y. et al. (2015)</h4></a>");//, new ExternalResource("http://www.ncbi.nlm.nih.gov/pubmed/"));
//        authorLabel.setTargetName("_blank");
        authorLabel.setContentMode(ContentMode.HTML);
        publicationlayout.addComponent(authorLabel);
        authorLabel.setHeight("30px");

        Label pubmedIdLabel = new Label("<h5>Pubmed Id: <a href='http://www.ncbi.nlm.nih.gov/pubmed/' target='_blank'  ><font style:'text-decoration:underline !important;'>222333444</font></a></h5>");
//        pubmedIdLabel.setTargetName("_blank");
        pubmedIdLabel.setContentMode(ContentMode.HTML);
        publicationlayout.addComponent(pubmedIdLabel);
        pubmedIdLabel.setHeight("30px");

        Label titleLabel = new Label("<textarea rows='4' cols='60' readonly > totojwdjnhsmn igawsdasndmn ajdkajsdksjad jsdkjkdsjksudcvfs</textarea>");
        titleLabel.setContentMode(ContentMode.HTML);
        publicationlayout.addComponent(titleLabel);

        return publicationlayout;

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        popupWindow.setVisible(true);
    }
}
