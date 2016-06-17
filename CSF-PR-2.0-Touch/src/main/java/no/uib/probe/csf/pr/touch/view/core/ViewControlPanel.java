/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
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
    private final HorizontalLayout mainViewContainer;
    private final HorizontalLayout mainLayoutWrapper;
    private final VerticalLayout toolBtnContainer;
    private final TreeMap<Integer, AbstractOrderedLayout> layoutControlMap;
    private LayoutEvents.LayoutClickListener listener;

    /**
     *
     * @param bodyWidth
     * @param bodyHeight
     */
    public ViewControlPanel(int bodyWidth, int bodyHeight) {

        this.setWidth(bodyWidth, Unit.PIXELS);
        this.setHeight(bodyHeight, Unit.PIXELS);
        this.addStyleName("slowslide");
        this.layoutControlMap = new TreeMap<>();

        this.mainLayoutWrapper = new HorizontalLayout();
        mainLayoutWrapper.setHeight(bodyHeight, Unit.PIXELS);
        mainLayoutWrapper.setWidthUndefined();
        this.mainLayoutWrapper.addStyleName("slowslide");

        this.addComponent(mainLayoutWrapper);

        VerticalLayout leftSideContainerWrapper = new VerticalLayout();
        leftSideContainerWrapper.setHeight(100, Unit.PERCENTAGE);
        leftSideContainerWrapper.setWidth(120, Unit.PIXELS);
        leftSideContainerWrapper.setSpacing(true);
        mainLayoutWrapper.addComponent(leftSideContainerWrapper);

        leftSideContainer = new VerticalLayout();
        leftSideContainer.setWidth(120, Unit.PIXELS);
        leftSideContainer.setHeightUndefined();
        leftSideContainer.setSpacing(true);
        leftSideContainerWrapper.addComponent(leftSideContainer);
        leftSideContainerWrapper.setComponentAlignment(leftSideContainer, Alignment.TOP_CENTER);

        mainViewContainer = new HorizontalLayout();
        mainLayoutWrapper.addComponent(mainViewContainer);
        mainViewContainer.setWidthUndefined();
        mainViewContainer.setHeightUndefined();
        mainViewContainer.addStyleName("mainviewport");

        toolBtnContainer = new VerticalLayout();
        toolBtnContainer.setStyleName("sidebtnsmenue");
        toolBtnContainer.setHeightUndefined();
        toolBtnContainer.setWidth(100, Unit.PIXELS);
        mainLayoutWrapper.addComponent(toolBtnContainer);
        mainLayoutWrapper.setComponentAlignment(toolBtnContainer, Alignment.BOTTOM_RIGHT);

    }

    /**
     * Add initial layout include side button, main layout, and control buttons
     *
     * @param Btn
     * @param mainViewLayout
     * @param toolBtnsLayout
     */
    public void setInitialLayout(AbstractOrderedLayout Btn, AbstractOrderedLayout mainViewLayout, AbstractOrderedLayout toolBtnsLayout) {
        this.removeComponent(mainLayoutWrapper);
        VerticalLayout btnWrapper = new VerticalLayout(Btn);
        btnWrapper.addStyleName("sidebtnwrapper");
        btnWrapper.addStyleName("unselectedbtn");
        btnWrapper.setWidth(100, Unit.PERCENTAGE);
        leftSideContainer.addComponent(btnWrapper);
        btnWrapper.addLayoutClickListener(this);

        layoutControlMap.put(leftSideContainer.getComponentIndex(btnWrapper), mainViewLayout);
        this.addComponent(mainViewLayout);
        this.setComponentAlignment(mainViewLayout, Alignment.MIDDLE_CENTER);
        listener = (LayoutEvents.LayoutClickEvent event) -> {

            if (event.getClickedComponent() == null || event.getComponent().getStyleName().trim().equalsIgnoreCase("")) {
                System.out.println("return ");
                return;
            }

            mainViewLayout.removeStyleName("hidelayout");
            mainViewLayout.removeLayoutClickListener(listener);
            mainViewLayout.setWidth(mainViewLayout.getWidth() - 200, Unit.PIXELS);
            mainViewLayout.addStyleName("hideslidelayout");

            this.removeComponent(mainViewLayout);
            mainViewContainer.addComponent(mainViewLayout);
            mainViewContainer.setComponentAlignment(mainViewLayout, Alignment.TOP_CENTER);
            if (currentView != null) {
                currentView.removeStyleName("hideslidelayout");
                currentBtn.removeStyleName("unselectedbtn");
            } else {
                layoutControlMap.lastEntry().getValue().removeStyleName("hideslidelayout");

            }

            this.addComponent(mainLayoutWrapper);
        };
        mainViewLayout.addLayoutClickListener(listener);
        if (toolBtnsLayout != null) {
            this.toolBtnContainer.addComponent(toolBtnsLayout);
            this.toolBtnContainer.setComponentAlignment(toolBtnsLayout, Alignment.MIDDLE_RIGHT);
            mainViewLayout.setData(toolBtnsLayout);
        } else {
            mainViewLayout.setData(new VerticalLayout());

        }

    }

    /**
     * Add layout include side button, main layout, control buttons, and if the
     * layout is default or not
     *
     * @param Btn
     * @param mainViewLayout
     * @param toolBtnsLayout
     * @param isDefault
     */
    public void addButton(AbstractOrderedLayout Btn, AbstractOrderedLayout mainViewLayout, AbstractOrderedLayout toolBtnsLayout, boolean isDefault) {

        VerticalLayout btnWrapper = new VerticalLayout(Btn);
        btnWrapper.addStyleName("sidebtnwrapper");
        btnWrapper.addStyleName("unselectedbtn");
        btnWrapper.setWidth(100, Unit.PERCENTAGE);
        leftSideContainer.addComponent(btnWrapper);
        btnWrapper.addLayoutClickListener(this);
        Btn.setEnabled(false);
        Btn.setReadOnly(false);
        Btn.setResponsive(true);

        layoutControlMap.put(leftSideContainer.getComponentIndex(btnWrapper), mainViewLayout);
        mainViewContainer.addComponent(mainViewLayout);
        mainViewContainer.setComponentAlignment(mainViewLayout, Alignment.TOP_CENTER);
        mainViewLayout.addStyleName("slowslide");
        mainViewLayout.addStyleName("hideslidelayout");

        if (toolBtnsLayout != null) {
            this.toolBtnContainer.addComponent(toolBtnsLayout);
            this.toolBtnContainer.setComponentAlignment(toolBtnsLayout, Alignment.MIDDLE_RIGHT);
            mainViewLayout.setData(toolBtnsLayout);
            toolBtnsLayout.setVisible(false);
        } else {
            mainViewLayout.setData(new VerticalLayout());
        }
        if (isDefault) {
            defaultView = mainViewLayout;
            defaultBtn = btnWrapper;
        }

    }

    private AbstractOrderedLayout currentView;
    private Component currentBtn, defaultBtn;
    private AbstractOrderedLayout defaultView;

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {         
        AbstractOrderedLayout mainViewLayout = layoutControlMap.get(leftSideContainer.getComponentIndex(event.getComponent()));

        if (currentView == null) {
            currentView = mainViewLayout;
            event.getComponent().removeStyleName("unselectedbtn");
            currentBtn = event.getComponent();
            ((AbstractOrderedLayout) mainViewLayout.getData()).setVisible(true);
        } else if (mainViewLayout != currentView) {
            currentView.addStyleName("hideslidelayout");
            ((AbstractOrderedLayout) currentView.getData()).setVisible(false);

            currentBtn.addStyleName("unselectedbtn");

            currentView = mainViewLayout;
            currentBtn = event.getComponent();
            ((AbstractOrderedLayout) mainViewLayout.getData()).setVisible(true);
        }
        mainViewLayout.removeStyleName("hideslidelayout");
        event.getComponent().removeStyleName("unselectedbtn");
        ((AbstractOrderedLayout) mainViewLayout.getData()).setVisible(true);

    }

    /**
     * Reset to the default layout view
     */
    public void defaultView() {
        if (defaultView == null) {
            return;
        }
        if (currentView == null) {
            currentView = defaultView;
            currentBtn = defaultBtn;
        } else if (defaultView != currentView) {
            currentView.addStyleName("hideslidelayout");
            currentBtn.addStyleName("unselectedbtn");
            ((AbstractOrderedLayout) currentView.getData()).setVisible(false);
            currentView = defaultView;
            currentBtn = defaultBtn;
        }
        defaultView.removeStyleName("hideslidelayout");
        defaultBtn.removeStyleName("unselectedbtn");
        ((AbstractOrderedLayout) defaultView.getData()).setVisible(true);

    }

}
