/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Layout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 *
 * @author Yehia Farag
 *
 * this class represents pop up window container
 */
public class PopupWindow extends Window {

    private boolean lazyLoading = false;
    private final VerticalLayout mainFrame;
    private final int height = Math.max(Page.getCurrent().getBrowserWindowHeight() - 50, 1);
    private final int width = Math.max(Page.getCurrent().getBrowserWindowWidth() - 50, 1);

    public PopupWindow(Layout layout, String title) {

        mainFrame = new VerticalLayout();
        mainFrame.setWidth(100, Unit.PERCENTAGE);
        mainFrame.setHeight(100,Unit.PERCENTAGE);
        setContent(mainFrame);
        mainFrame.addStyleName("mainviewport");

        mainFrame.addComponent(layout);
        mainFrame.setComponentAlignment(layout, Alignment.MIDDLE_CENTER);

        setWindowMode(WindowMode.NORMAL);
        setWidth((width), Unit.PIXELS);
        PopupWindow.this.setHeight((height), Unit.PIXELS);
        PopupWindow.this.setVisible(false);
        setResizable(false);
        setModal(true);
        setDraggable(false);
        this.addStyleName("hideoverflow");
        center();
        setCaption("<font color='gray' style='font-weight: bold;!important'>&nbsp;&nbsp;" + title + "</font>");
        UI.getCurrent().addWindow(PopupWindow.this);
        setCaptionAsHtml(true);
        setClosable(true);
    }

    @Override
    public void close() {
        this.setVisible(false);

    }

    @Override
    public void setVisible(boolean visible) {

        if (visible && lazyLoading) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
        }
//         this.mainFrame.addStyleName(VaadinSession.getCurrent().getAttribute("zoomStyle")+"");
        super.setVisible(visible); //To change body of generated methods, choose Tools | Templates.
    }

    public void setLazyLoading(boolean lazyLoading) {
        this.lazyLoading = lazyLoading;
    }

    @Override
    public void setHeight(float height, Unit unit) {
        super.setHeight(Math.min(height, this.height), unit); //To change body of generated methods, choose Tools | Templates.
//        if(mainFrame!=null)
//        mainFrame.setHeight(super.getHeight()-40, unit);
        center();
        this.markAsDirty();
    }

}
