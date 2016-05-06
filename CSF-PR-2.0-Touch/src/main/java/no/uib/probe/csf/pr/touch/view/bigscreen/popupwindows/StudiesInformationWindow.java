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
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.Collection;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;
import no.uib.probe.csf.pr.touch.view.core.DatasetButtonsContainerLayout;

/**
 *
 * @author Yehia Farag
 *
 * this class represents study information popup window
 */
public class StudiesInformationWindow extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private final Window popupWindow;
    private final   DatasetButtonsContainerLayout studiesPopupLayout;

    public StudiesInformationWindow() {

        int height = Page.getCurrent().getBrowserWindowHeight() - 100;
        int width = Page.getCurrent().getBrowserWindowWidth() - 100;
        VerticalLayout popupBody = new VerticalLayout();
        popupBody.setWidth((width - 20) + "px");
        popupBody.setHeightUndefined();
        popupBody.setMargin(true);
        popupBody.setSpacing(true);

        popupWindow = new Window() {

            @Override
            public void close() {
                popupWindow.setVisible(false);

            }

            @Override
            public void setVisible(boolean visible) {

                if (visible) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }
                }
                super.setVisible(visible); //To change body of generated methods, choose Tools | Templates.
            }

        };

        popupWindow.setContent(popupBody);
        popupWindow.setWindowMode(WindowMode.NORMAL);
        popupWindow.setWidth((width) + "px");
        popupWindow.setHeight((height) + "px");
        popupWindow.setVisible(false);
        popupWindow.setResizable(false);
        popupWindow.setClosable(false);
        popupWindow.setModal(true);
        popupWindow.setDraggable(false);
        popupWindow.center();
        popupWindow.setCaption("<font color='gray' style='font-weight: bold;!important'>&nbsp;&nbsp;Datasets Information</font>");
        UI.getCurrent().addWindow(popupWindow);
        popupWindow.setCaptionAsHtml(true);
        popupWindow.setClosable(true);
        this.addLayoutClickListener(StudiesInformationWindow.this);

        studiesPopupLayout = new DatasetButtonsContainerLayout();
        popupBody.addComponent(studiesPopupLayout);
        popupBody.setComponentAlignment(studiesPopupLayout, Alignment.TOP_CENTER);

    }
    
    public void updateData(Collection<QuantDatasetObject> dsObjects){
        studiesPopupLayout.setInformationData(dsObjects);
    
    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        popupWindow.setVisible(true);
    }
    
    public void view(){
        popupWindow.setVisible(true);
    }
}
