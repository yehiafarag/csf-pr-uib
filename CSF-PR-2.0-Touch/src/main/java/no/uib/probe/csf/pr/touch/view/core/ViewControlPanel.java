/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import java.util.HashMap;
import java.util.TreeMap;

/**
 *
 * @author Yehia Farag
 *
 * this class represents a view panel with side control buttons the panel
 * support one view in time
 */
public class ViewControlPanel extends HorizontalLayout implements LayoutEvents.LayoutClickListener {

    private final VerticalLayout leftSideContainer;
    private final VerticalLayout mainViewContainer;
    private final HorizontalLayout mainLayoutWrapper;
    private final TreeMap<Integer, AbstractOrderedLayout> layoutControlMap;
    private LayoutEvents.LayoutClickListener listener;

    public ViewControlPanel(int bodyWidth, int bodyHeight) {

        this.setWidth(bodyWidth, Unit.PIXELS);
        this.setHeight(bodyHeight, Unit.PIXELS);
        this.layoutControlMap = new TreeMap<>();

        this.mainLayoutWrapper = new HorizontalLayout();
        mainLayoutWrapper.setHeight(bodyHeight, Unit.PIXELS);
        mainLayoutWrapper.setWidthUndefined();

        this.addComponent(mainLayoutWrapper);

        VerticalLayout leftSideContainerWrapper = new VerticalLayout();
        leftSideContainerWrapper.setHeight(100, Unit.PERCENTAGE);
        leftSideContainerWrapper.setWidth(150, Unit.PIXELS);
        leftSideContainerWrapper.setSpacing(true);
        mainLayoutWrapper.addComponent(leftSideContainerWrapper);

        leftSideContainer = new VerticalLayout();
        leftSideContainer.setWidth(150, Unit.PIXELS);
        leftSideContainer.setHeightUndefined();
        leftSideContainer.setSpacing(true);
        leftSideContainerWrapper.addComponent(leftSideContainer);
        leftSideContainerWrapper.setComponentAlignment(leftSideContainer, Alignment.TOP_CENTER);

        mainViewContainer = new VerticalLayout();
        mainLayoutWrapper.addComponent(mainViewContainer);
        mainViewContainer.setWidthUndefined();
        this.addStyleName("slowslide");

    }

    public void setInitialLayout(AbstractOrderedLayout Btn, AbstractOrderedLayout mainViewLayout) {
//        mainViewLayout.addStyleName("slowslide");
        this.removeComponent(mainLayoutWrapper);
        leftSideContainer.addComponent(Btn);
        Btn.addLayoutClickListener(this);
        layoutControlMap.put(leftSideContainer.getComponentIndex(Btn), mainViewLayout);
        this.addComponent(mainViewLayout);
        this.setComponentAlignment(mainViewLayout, Alignment.TOP_CENTER);
        listener = (LayoutEvents.LayoutClickEvent event) -> {
            mainViewLayout.removeLayoutClickListener(listener);
            mainViewLayout.setWidth(mainViewLayout.getWidth() - 200, Unit.PIXELS);
            mainViewLayout.addStyleName("hideslidelayout");

            this.removeComponent(mainViewLayout);
            mainViewContainer.addComponent(mainViewLayout);
            if (currentView != null) {
                currentView.removeStyleName("hideslidelayout");
            } else {
                layoutControlMap.lastEntry().getValue().removeStyleName("hideslidelayout");

            }

            this.addComponent(mainLayoutWrapper);
        };
        mainViewLayout.addLayoutClickListener(listener);

    }

    public void addButton(AbstractOrderedLayout Btn, AbstractOrderedLayout mainViewLayout, boolean isDefault) {

        leftSideContainer.addComponent(Btn);
        Btn.addLayoutClickListener(this);
        Btn.setEnabled(true);
        Btn.setReadOnly(false);
        Btn.setResponsive(true);
        layoutControlMap.put(leftSideContainer.getComponentIndex(Btn), mainViewLayout);
        mainViewContainer.addComponent(mainViewLayout);
        mainViewLayout.addStyleName("slowslide");
        mainViewLayout.addStyleName("hideslidelayout");
        if (isDefault) {
            defaultView = mainViewLayout;
        }

    }

    private AbstractOrderedLayout currentView;
    private AbstractOrderedLayout defaultView;

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        AbstractOrderedLayout mainViewLayout = layoutControlMap.get(leftSideContainer.getComponentIndex(event.getComponent()));
        if (currentView == null) {
            currentView = mainViewLayout;
        } else if (mainViewLayout != currentView) {
            currentView.addStyleName("hideslidelayout");
            currentView = mainViewLayout;
        }
        mainViewLayout.removeStyleName("hideslidelayout");

    }

    public void defaultView() {
        if (defaultView == null) {
            return;
        }
        if (currentView == null) {
            currentView = defaultView;
        } else if (defaultView != currentView) {
            currentView.addStyleName("hideslidelayout");
            currentView = defaultView;
        }
        defaultView.removeStyleName("hideslidelayout");

    }

}
