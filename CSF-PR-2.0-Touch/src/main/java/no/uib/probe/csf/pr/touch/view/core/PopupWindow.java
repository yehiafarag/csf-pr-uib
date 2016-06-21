/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.server.Page;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
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

    private boolean lazyLoading=false;
    public PopupWindow(AbstractOrderedLayout layout,String title) {

        int height = Page.getCurrent().getBrowserWindowHeight() - 100;
        int width = Page.getCurrent().getBrowserWindowWidth() - 100;
       
        VerticalLayout mainFrame = new VerticalLayout();
        mainFrame.setSizeFull();        
        setContent(mainFrame);
        mainFrame.addStyleName("mainviewport");
        
        mainFrame.addComponent(layout);
        mainFrame.setComponentAlignment(layout, Alignment.TOP_CENTER);
        
        setWindowMode(WindowMode.NORMAL);
        setWidth((width),Unit.PIXELS);
        setHeight((height),Unit.PIXELS);
        PopupWindow.this.setVisible(false);
        setResizable(false);
        setClosable(false);
        setModal(true);
        setDraggable(false);
        center();
        setCaption("<font color='gray' style='font-weight: bold;!important'>&nbsp;&nbsp;"+title+"</font>");
        UI.getCurrent().addWindow(this);
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
        super.setVisible(visible); //To change body of generated methods, choose Tools | Templates.
    }

    public void setLazyLoading(boolean lazyLoading) {
        this.lazyLoading = lazyLoading;
    }
}
