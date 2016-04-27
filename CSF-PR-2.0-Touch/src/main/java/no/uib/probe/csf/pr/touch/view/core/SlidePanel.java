/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author YEhia Farag
 *
 * this class represents slide left panel
 */
public class SlidePanel extends HorizontalLayout implements LayoutEvents.LayoutClickListener {

    private boolean show = true;
    private final Layout mainLayout, miniLayout;
    private final VerticalLayout navigationBtn;

    /**
     * Slide touch friendly panel
     *
     * @param mainLayout main panel components
     * @param miniLayout mini layout on close panel
     * @param orientation the slide direction 0 slide to left any other value
     * slide to right
     */
    public SlidePanel(Layout mainLayout, Layout miniLayout, int orientation) {
        this.mainLayout = mainLayout;
        navigationBtn = new VerticalLayout();
        navigationBtn.setHeight("30px");
        navigationBtn.setWidth("30px");

        this.miniLayout = miniLayout;
        miniLayout.addStyleName("slowslide");
        miniLayout.addStyleName("hideslidelayout");
        mainLayout.addStyleName("slowslide");
        if (orientation == 0) {
            this.addComponent(navigationBtn);

            navigationBtn.setStyleName("leftslidebtn");
            this.addComponent(miniLayout);
            this.addComponent(mainLayout);

        } else {
            navigationBtn.setStyleName("rightslidebtn");
            this.addComponent(mainLayout);
            this.addComponent(miniLayout);
            this.addComponent(navigationBtn);
        }
        this.setComponentAlignment(navigationBtn, Alignment.MIDDLE_CENTER);
        this.setHeight("100%");
        this.setStyleName("slidepanel");
        this.navigationBtn.addLayoutClickListener(SlidePanel.this);
        this.setWidthUndefined();

    }

    /**
     * show full panel (Main layout)
     *
     * @param show boolean
     */
    public final void setShowPanel(boolean show) {
        if (show) {
            this.navigationBtn.removeStyleName("transformslidebtn");
            this.mainLayout.removeStyleName("hideslidelayout");
            this.miniLayout.addStyleName("hideslidelayout");
        } else {
            this.navigationBtn.addStyleName("transformslidebtn");
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
    public void setShowNavigationBtn(boolean  show){
        this.navigationBtn.setVisible(show);
    }

}
