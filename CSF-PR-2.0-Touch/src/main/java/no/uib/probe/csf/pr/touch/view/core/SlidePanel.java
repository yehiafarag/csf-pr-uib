/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Yehia Farag
 *
 * this class represents slide left panel
 */
public class SlidePanel extends HorizontalLayout implements LayoutEvents.LayoutClickListener, ControllingView {

    @Override
    public void resizeComponent(int width, int height) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getViewId() {
        return viewId;
    }

    @Override
    public void view() {
        setShowPanel(true);
    }

    @Override
    public void hide() {
        setVisible(false);
    }

    private boolean show = true;
    private final Layout mainLayout, miniLayout;
    private final String viewId;

    /**
     * Slide touch friendly panel
     *
     * @param mainLayout main panel components
     * @param miniLayout mini layout on close panel
     * @param orientation the slide direction 0 slide to left any other value
     * slide to right
     */
    public SlidePanel(Layout mainLayout, Layout miniLayout, int orientation, String viewId) {
        this.mainLayout = mainLayout;
        this.viewId = viewId;
        if (miniLayout == null) {
            miniLayout = new VerticalLayout();
        }
        this.miniLayout = miniLayout;
        miniLayout.addStyleName("slowslide");
        miniLayout.addStyleName("hideslidelayout");
        mainLayout.addStyleName("slowslide");
        if (orientation == 0) {
            this.addComponent(miniLayout);
            this.addComponent(mainLayout);

        } else {
            this.addComponent(mainLayout);
            this.addComponent(miniLayout);
        }
        this.setHeight(100,Unit.PERCENTAGE);
        this.setStyleName("slidepanel");
        this.setWidthUndefined();

    }

    /**
     * show full panel (Main layout)
     *
     * @param show boolean
     */
    public final void setShowPanel(boolean show) {
        if (show) {
            this.setVisible(true);
            this.mainLayout.removeStyleName("hideslidelayout");
            this.miniLayout.addStyleName("hideslidelayout");
        } else {
            this.mainLayout.addStyleName("hideslidelayout");
            this.miniLayout.removeStyleName("hideslidelayout");
        }
        this.show = show;

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        setShowPanel(!show);
    }

    /**
     * show full panel (Main layout)
     *
     * @param show boolean
     */
    public void setShowNavigationBtn(boolean show) {
    }

}
