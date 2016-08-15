/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import java.util.HashSet;

/**
 *
 * @author Yehia Farag
 *
 * this layout contain zoomIn/out and reset zoom
 */
public class ZoomControler extends HorizontalLayout implements LayoutEvents.LayoutClickListener {

    private final SmallBtn resetZoomBtn;
    private final HashSet<Component> zoomableComponentsSet;
    private int zoomLevel = 10;
    private int defaultZoomLevel = 10;

    public ZoomControler(boolean isHorizontal, boolean smallScreen) {
        this.zoomableComponentsSet = new HashSet<>();
        this.setWidthUndefined();
        this.setHeightUndefined();

        this.setSpacing(true);

        SmallBtn zoomInBtn = new SmallBtn(new ThemeResource("img/search-plus.png"));
        zoomInBtn.setData("zoomIn");
        zoomInBtn.setDescription("Click to zoom in");
        zoomInBtn.addLayoutClickListener(ZoomControler.this);
        resetZoomBtn = new SmallBtn(new ThemeResource("img/resetzoom.png")) {
//            @Override
//            public void onClick() {
//                zoomLevel = defaultZoomLevel;
//                updateZoomLevel(defaultZoomLevel);
//            }

        };
        resetZoomBtn.addLayoutClickListener((LayoutEvents.LayoutClickEvent event) -> {
            zoomLevel = defaultZoomLevel;
            updateZoomLevel(defaultZoomLevel);
        });
        resetZoomBtn.setDescription("Click to reset zoom");
//        resetZoomBtn.updateIcon();
//        resetZoomBtn.setWidth(100, Unit.PERCENTAGE);
//        resetZoomBtn.setHeight(100, Unit.PERCENTAGE);
//        resetZoomBtn.setReadOnly(false);
//        resetZoomBtn.setEnabled(true);
//        this.setStyleName("paddingimg");

        SmallBtn zoomOutBtn = new SmallBtn(new ThemeResource("img/search-minus.png"));
        zoomOutBtn.addLayoutClickListener(ZoomControler.this);
        zoomOutBtn.setDescription("Click to zoom out");
        zoomOutBtn.setData("zoomOut");

        if (isHorizontal) {
            this.addComponent(zoomInBtn);
            this.setComponentAlignment(zoomInBtn, Alignment.TOP_CENTER);
            this.addComponent(zoomOutBtn);
            this.setComponentAlignment(zoomOutBtn, Alignment.TOP_CENTER);
        } else {
            VerticalLayout verticalContainer = new VerticalLayout();

            verticalContainer.setSizeFull();
            verticalContainer.setSpacing(true);

            zoomInBtn.addStyleName(zoomStyleName);
            verticalContainer.addComponent(zoomInBtn);
            verticalContainer.setComponentAlignment(zoomInBtn, Alignment.TOP_CENTER);
            verticalContainer.addComponent(zoomOutBtn);
            verticalContainer.setComponentAlignment(zoomOutBtn, Alignment.TOP_CENTER);
            if (smallScreen) {
                zoomInBtn.setWidth(25, Unit.PIXELS);
                zoomInBtn.setHeight(25, Unit.PIXELS);
                zoomOutBtn.setWidth(25, Unit.PIXELS);
                zoomOutBtn.setHeight(25, Unit.PIXELS);
                resetZoomBtn.setWidth(25, Unit.PIXELS);
                resetZoomBtn.setHeight(25, Unit.PIXELS);
            } else {
                this.setStyleName("midimg");
//                resetZoomBtn.addStyleName("midimg");
                zoomInBtn.setWidth(40, Unit.PIXELS);
                zoomInBtn.setHeight(40, Unit.PIXELS);
                resetZoomBtn.setWidth(40, Unit.PIXELS);
                resetZoomBtn.setHeight(40, Unit.PIXELS);
                zoomOutBtn.setWidth(40, Unit.PIXELS);
                zoomOutBtn.setHeight(40, Unit.PIXELS);
            }
            verticalContainer.addComponent(resetZoomBtn);
            verticalContainer.setComponentAlignment(resetZoomBtn, Alignment.TOP_CENTER);
            resetZoomBtn.setVisible(false);

            this.addComponent(verticalContainer);

        }

    }

//    public AbsoluteLayout getResetZoomBtn() {
//        return resetZoomBtn;
//    }

    public void addZoomableComponent(Component component) {
        zoomableComponentsSet.add(component);

    }

    public void setDefaultZoomLevel(int defaultZoomLevel) {
        this.defaultZoomLevel = defaultZoomLevel;
        this.zoomLevel = defaultZoomLevel;
        updateZoomLevel(defaultZoomLevel);
    }

    private String zoomStyleName = "";

    private void updateZoomLevel(int zoomLevel) {
        String updatedZoomStyleName = "zoom" + zoomLevel;
        zoomableComponentsSet.stream().map((component) -> {
            component.removeStyleName(zoomStyleName);
            return component;
        }).forEach((component) -> {
            component.addStyleName(updatedZoomStyleName);
        });
        zoomStyleName = updatedZoomStyleName;
        if (zoomLevel == defaultZoomLevel) {
            resetZoomBtn.setVisible(false);
        } else {
            resetZoomBtn.setVisible(true);
        }

    }

    public void removeZoomableComponent(Component component) {
        zoomableComponentsSet.remove(component);

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (((VerticalLayout) event.getComponent()).getData().toString().equalsIgnoreCase("zoomIn")) {
            if (zoomLevel < 10) {
                zoomLevel += 1;
            } else {
                zoomLevel += 5;
            }
            zoomLevel = Math.min(zoomLevel, 30);

        } else {
            if (zoomLevel <= 10) {
                zoomLevel -= 1;
            } else {
                zoomLevel -= 5;
            }
            zoomLevel = Math.max(zoomLevel, 0);
        }
        updateZoomLevel(zoomLevel);
    }

}
