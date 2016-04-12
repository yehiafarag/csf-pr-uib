/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters;

import com.vaadin.event.LayoutEvents;
import probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.interactivepiechartfilters.StudiesPieChartFiltersContainerLayout;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

/**
 *
 * @author Yehia Farag
 */
public class PopupInteractiveDSFiltersLayout extends VerticalLayout implements LayoutEvents.LayoutClickListener {

//    private PopupView container;
    private final Window popupWindow;
    private final StudiesPieChartFiltersContainerLayout interactivePieChartFiltersContainerLayout;

    /**
     *
     */
    public void closePupupWindow() {
        this.popupWindow.close();
    }

    /**
     *
     * @param interactivePieChartFiltersContainerLayout
     */
    public PopupInteractiveDSFiltersLayout(final StudiesPieChartFiltersContainerLayout interactivePieChartFiltersContainerLayout) {
        VerticalLayout datasetExplorerFiltersIcon = new VerticalLayout();
//        datasetExplorerFiltersIcon.setWidth("45px");
//        datasetExplorerFiltersIcon.setHeight("24px");
//        Label btnTitle = new Label("Studies");
//        datasetExplorerFiltersIcon.addComponent(btnTitle);
        datasetExplorerFiltersIcon.setStyleName("studyexplorer");
        datasetExplorerFiltersIcon.setDescription("Dataset expolorer filter");
        this.addComponent(datasetExplorerFiltersIcon);
        this.setComponentAlignment(datasetExplorerFiltersIcon, Alignment.BOTTOM_LEFT);
        datasetExplorerFiltersIcon.addLayoutClickListener(PopupInteractiveDSFiltersLayout.this);
        this.setHeightUndefined();

        int height = Page.getCurrent().getBrowserWindowHeight() - 100;
        int width = Page.getCurrent().getBrowserWindowWidth() - 100;
        VerticalLayout popupBody = new VerticalLayout();
        popupBody.setWidth((width) + "px");
        popupBody.setHeightUndefined();
        popupBody.setStyleName(Reindeer.LAYOUT_WHITE);

        this.interactivePieChartFiltersContainerLayout = interactivePieChartFiltersContainerLayout;
        popupWindow = new Window() {

            @Override
            public void close() {
                interactivePieChartFiltersContainerLayout.updateSelectionManager(true);
                popupWindow.setVisible(false);

            }

        };
        popupWindow.setContent(popupBody);
        popupWindow.setWindowMode(WindowMode.NORMAL);
        popupWindow.setWidth((width + 40) + "px");
        popupWindow.setHeight((height) + "px");
        popupWindow.setVisible(false);
        popupWindow.setResizable(false);
        popupWindow.setClosable(false);
        popupWindow.setStyleName(Reindeer.WINDOW_LIGHT);
        popupWindow.setModal(true);
        popupWindow.setDraggable(false);
        popupWindow.center();

        popupWindow.setCaption("<font color='gray' style='font-weight: bold;!important'>&nbsp;&nbsp;Dataset Explorer Filters</font>");

        UI.getCurrent().addWindow(popupWindow);
        popupWindow.setPositionX(30);
        popupWindow.setPositionY(40);

        popupWindow.setCaptionAsHtml(true);
        popupWindow.setClosable(true);

        popupBody.setMargin(true);
        popupBody.setSpacing(true);

        popupBody.addComponent(interactivePieChartFiltersContainerLayout);

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        interactivePieChartFiltersContainerLayout.updatePieChartCharts();
        popupWindow.setVisible(true);
    }

}
