/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 *
 * @author Yehia Farag
 */
public class BusyTask extends Window {

    public BusyTask() {
        this.setWidth(100, Unit.PERCENTAGE);
        this.setHeight(100, Unit.PERCENTAGE);
        this.setModal(true);
        this.setClosable(false);
        this.setDraggable(false);
        this.setVisible(false);
        this.setResizable(false);
        VerticalLayout vlo = new VerticalLayout();
        vlo.setWidth(10, Unit.PIXELS);
        vlo.setHeight(10, Unit.PIXELS);

        this.setContent(vlo);
        this.setWindowMode(WindowMode.NORMAL);
        this.addStyleName("busytaskwindow");

        this.setCaption(null);

        this.center();

        UI.getCurrent().addWindow(BusyTask.this);

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

}
