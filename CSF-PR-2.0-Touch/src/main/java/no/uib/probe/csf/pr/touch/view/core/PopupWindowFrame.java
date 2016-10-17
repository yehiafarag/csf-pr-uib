/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Yehia Farag this class represent a popup window with frame and no
 * functions button layout
 */
public class PopupWindowFrame{

    private final PopupWindow popupWindow;
    private final AbstractOrderedLayout popupBody;

    public PopupWindowFrame(String title, AbstractOrderedLayout popupBody) {
        popupBody.setMargin(false);
        popupBody.setSpacing(true);
        popupBody.addStyleName("roundedborder");
        popupBody.addStyleName("whitelayout");
        popupBody.addStyleName("padding20");
        popupBody.addStyleName("scrollable");
        popupBody.addStyleName("margin");

        this.popupBody = popupBody;

        VerticalLayout frame = new VerticalLayout();
        frame.setWidth(100, Unit.PERCENTAGE);
        frame.setHeight(100, Unit.PERCENTAGE);
        frame.setSpacing(true);
        frame.addComponent(popupBody);
        popupWindow = new PopupWindow(frame, title) {

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

        popupBody.setWidth(popupWindow.getWidth() - 24, Unit.PIXELS);

    }

    public void setFrameHeight(int height) {

        popupWindow.setHeight(Math.max(height, 61), Unit.PIXELS);
        popupBody.setHeight(popupWindow.getHeight() - 60, Unit.PIXELS);

    }

    public void view() {
        popupWindow.setVisible(true);
    }

    public int getFrameWidth() {
        return (int) popupWindow.getWidth();
    }

    public void setFrameWidth(int width) {
        popupWindow.setWidth(width-20, Unit.PIXELS);
        popupBody.setWidth(popupWindow.getWidth() - 24, Unit.PIXELS);
    }

}
