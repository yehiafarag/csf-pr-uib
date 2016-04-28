/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.bigscreen.popupwindows;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
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
           
            String btnName = "<font size=1 >"+obj[0].toString()+"</font><br/>"+obj[1].toString() + "<br/><font size=1 >" + obj[2].toString() + "</font><br/><font size=1 >#Proteins: " + obj[5].toString() /*+ "/" + obj[5].toString() + */+"   #Peptides: " + obj[7].toString() /*+ "/" + obj[7].toString() +*/+ "</font>";

            PopupInfoBtn publicationBtn = new PopupInfoBtn(btnName,obj[1].toString(),obj);
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

   

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        popupWindow.setVisible(true);
    }
}
