/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import java.io.Serializable;

/**
 *
 * @author Yehia Farag
 */
public class HideOnClickLayout extends VerticalLayout implements Serializable, LayoutEvents.LayoutClickListener {
    
    private final Label titleLabel;
    private final HorizontalLayout titleLayout;
    private final ShowLabel show;
    private final Component fullBodyLayout;
    private final AbstractOrderedLayout miniBodyLayout;
    private final InfoPopupBtn info;

    /**
     *
     * @param title
     * @param fullBodyLayout
     * @param miniBodyLayout
     * @param infoText
     */
    public HideOnClickLayout(String title, Layout fullBodyLayout, AbstractOrderedLayout miniBodyLayout, String infoText,VerticalLayout tipsIcon) {
        this.setMargin(new MarginInfo(false, false, false, false));
        this.setWidth("100%");
        this.fullBodyLayout = fullBodyLayout;
        this.fullBodyLayout.setVisible(false);
        this.miniBodyLayout = miniBodyLayout;
        
        titleLayout = new HorizontalLayout();
        titleLayout.setHeight("30px");
        titleLayout.setWidth("100%");
        titleLayout.setSpacing(true);
        HorizontalLayout titleHeaderLayout = new HorizontalLayout();
        titleHeaderLayout.setWidthUndefined();
        titleHeaderLayout.setSpacing(true);
        
        show = new ShowLabel();
        show.setHeight("20px");
        titleHeaderLayout.addComponent(show);
        titleHeaderLayout.setComponentAlignment(show, Alignment.BOTTOM_LEFT);
        
        titleLabel = new Label(title);
        titleLabel.setContentMode(ContentMode.HTML);
        
        titleLabel.setHeight("20px");
        
        titleHeaderLayout.addComponent(titleLabel);
        titleHeaderLayout.setComponentAlignment(titleLabel, Alignment.TOP_LEFT);
        titleHeaderLayout.addLayoutClickListener(HideOnClickLayout.this);
        
        VerticalLayout titleHeaderContainer = new VerticalLayout(titleHeaderLayout);
        titleHeaderContainer.setWidthUndefined();
        
        titleLayout.addComponent(titleHeaderContainer);
        
        info = new InfoPopupBtn(infoText);
        if (infoText != null && !infoText.trim().equalsIgnoreCase("")) {
            titleHeaderLayout.addComponent(info);
            titleLabel.setStyleName("filterShowLabel");
        } else {
            titleLabel.setStyleName("normalheader");
        }
        this.addComponent(titleLayout);
        this.addComponent(this.fullBodyLayout);
        this.setComponentAlignment(this.fullBodyLayout, Alignment.MIDDLE_CENTER);
        if (miniBodyLayout != null) {
            titleLayout.addComponent(this.miniBodyLayout);
            titleLayout.setComponentAlignment(this.miniBodyLayout, Alignment.BOTTOM_LEFT);
            titleLayout.setExpandRatio(this.miniBodyLayout, 5);
            titleLayout.setExpandRatio(titleHeaderContainer, 1);
            miniBodyLayout.addLayoutClickListener(HideOnClickLayout.this);
        }
        if (tipsIcon!=null) {          
            titleHeaderLayout.addComponent(tipsIcon);         
            
        }
    }

    /**
     *
     * @param title
     * @param fullBodyLayout
     * @param miniBodyLayout
     * @param align
     * @param infoText
     */
    public HideOnClickLayout(String title, Component fullBodyLayout, AbstractOrderedLayout miniBodyLayout, Alignment align, String infoText,VerticalLayout tipsIcon) {
        this.setMargin(new MarginInfo(false, false, false, false));
        this.setWidth("100%");
        this.fullBodyLayout = fullBodyLayout;
        this.fullBodyLayout.setVisible(false);
        this.miniBodyLayout = miniBodyLayout;
        
        titleLayout = new HorizontalLayout();
        titleLayout.setHeight("30px");
        titleLayout.setSpacing(true);        
        
        HorizontalLayout titleHeaderLayout = new HorizontalLayout();
        titleHeaderLayout.setWidthUndefined();
        titleHeaderLayout.setSpacing(true);
        
        show = new ShowLabel();
        show.setHeight("20px");
        titleHeaderLayout.addComponent(show);
        titleHeaderLayout.setComponentAlignment(show, Alignment.BOTTOM_LEFT);
        
        
        titleLabel = new Label(title);
        titleLabel.setContentMode(ContentMode.HTML);
        
        titleLabel.setHeight("20px");
        
        titleHeaderLayout.addComponent(titleLabel);
        titleHeaderLayout.setComponentAlignment(titleLabel, Alignment.TOP_LEFT);
        titleHeaderLayout.addLayoutClickListener(HideOnClickLayout.this);
        
        VerticalLayout titleHeaderContainer = new VerticalLayout(titleHeaderLayout);
        titleHeaderContainer.setWidthUndefined();
        
        titleLayout.addComponent(titleHeaderContainer);
        
        info = new InfoPopupBtn(infoText);
        if (infoText != null && !infoText.trim().equalsIgnoreCase("")) {
            titleLayout.addComponent(info);
            titleLabel.setStyleName("filterShowLabel");
        } else {
            titleLabel.setStyleName("normalheader");
        }
        this.addComponent(titleLayout);
        this.addComponent(this.fullBodyLayout);
        this.setComponentAlignment(this.fullBodyLayout, align);
        if (miniBodyLayout != null) {
            titleLayout.addComponent(this.miniBodyLayout);
            titleLayout.setComponentAlignment(this.miniBodyLayout, Alignment.TOP_LEFT);
            titleLayout.setExpandRatio(this.miniBodyLayout, 5);
            titleLayout.setExpandRatio(titleHeaderContainer, 1);
            miniBodyLayout.addLayoutClickListener(HideOnClickLayout.this);
        }
        if (tipsIcon != null) {         
            titleHeaderLayout.addComponent(tipsIcon);          
            
        }
        
    }
    
    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (event.getClickedComponent() instanceof InfoPopupBtn || event.getClickedComponent() instanceof VerticalLayout) {
            return;
        }
        if (fullBodyLayout.isVisible()) {
            this.hideLayout();
            
        } else {
            this.showLayout();
        }
        
    }
    
    private void showLayout() {
        show.updateIcon(true);
        fullBodyLayout.setVisible(true);
        if (miniBodyLayout != null) {
            this.miniBodyLayout.setVisible(false);
        }
    }

    /**
     *
     */
    public final void hideLayout() {
        show.updateIcon(false);
        fullBodyLayout.setVisible(false);
        if (miniBodyLayout != null) {
            this.miniBodyLayout.setVisible(true);
        }
    }

    /**
     *
     * @return
     */
    public boolean isVisability() {
        return fullBodyLayout.isVisible();
    }

    /**
     *
     * @param test
     */
    public void setVisability(boolean test) {
        if (test) {
            this.showLayout();
        } else {
            this.hideLayout();
        }
    }

    /**
     *
     * @param label
     */
    public void updateTitleLabel(String label) {
        
        titleLabel.setValue(label);
    }
    
}
