/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.peptidescontainer;

import com.vaadin.server.Page;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Layout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

/**
 *
 * @author Yehia Farag
 */
public class StudyInformationPopupComponent extends VerticalLayout {

    private final Window popupWindow;
    private final VerticalLayout popupBody;

    /**
     *
     * @param width
     * @param protName
     * @param url
     * @param comparisonHeader
     */
    public StudyInformationPopupComponent(int width, String protName, String url, String comparisonHeader) {

        int height = Page.getCurrent().getBrowserWindowHeight() - 100;
        
        popupBody = new VerticalLayout();
        popupBody.setWidth((width) + "px");
        popupBody.setHeightUndefined();
        popupBody.setStyleName(Reindeer.LAYOUT_WHITE);

        popupWindow = new Window() {

            @Override
            public void close() {
                popupWindow.setVisible(false);
            }

        };
        popupWindow.setCaption("<a href='" + url + "'target=\"_blank\"> " + protName + " <font size='2' color='#666'> (" + comparisonHeader + ")</font></a> ");
        popupWindow.setContent(popupBody);
        popupWindow.setWindowMode(WindowMode.NORMAL);
        popupWindow.setWidth((width + 40) + "px");
        popupWindow.setHeight((height) + "px");
        popupWindow.setVisible(false);
        popupWindow.setResizable(false);
        popupWindow.setClosable(false);
        popupWindow.setStyleName(Reindeer.WINDOW_LIGHT);
        popupWindow.setModal(true);
        popupWindow.setDraggable(true);

        UI.getCurrent().addWindow(popupWindow);
        popupWindow.setPositionX(30);
        popupWindow.setPositionY(40);

        popupWindow.setCaptionAsHtml(true);
        popupWindow.setClosable(true);

        popupBody.setMargin(true);
        popupBody.setSpacing(true);
    }

    public void updateContent(Layout componentsLayout) {
        this.popupBody.removeAllComponents();
        this.popupBody.addComponent(componentsLayout);
        popupWindow.setVisible(true);

    }
}
