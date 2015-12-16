/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import java.util.Set;
import probe.com.selectionmanager.QuantCentralManager;
import probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.popupreordergroups.SortableLayoutContainer;

/**
 *
 * @author Yehia Farag
 */
public class PopupReorderGroupsLayout extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private final Window popupWindow;
    private final QuantCentralManager Quant_Central_Manager;
    private final VerticalLayout popupBodyLayout;
    private final SortableLayoutContainer sortableDiseaseGroupI, sortableDiseaseGroupII;
    private Set<String> rowHeaders,colHeaders;
//    private final int itemWidth;

    public PopupReorderGroupsLayout(QuantCentralManager Quant_Central_Manager) {
        this.setStyleName("reordergroupsbtn");
        this.setDescription("Reorder All Disease Groups Comparisons");
        this.Quant_Central_Manager = Quant_Central_Manager;
        this.addLayoutClickListener(PopupReorderGroupsLayout.this);
        this.popupBodyLayout = new VerticalLayout();
        VerticalLayout windowLayout = new VerticalLayout();
        popupWindow = new Window() {
            @Override
            public void close() {
                popupWindow.setVisible(false);
            }
        };
        popupWindow.setContent(windowLayout);
        windowLayout.addComponent(popupBodyLayout);
        windowLayout.setComponentAlignment(popupBodyLayout, Alignment.MIDDLE_CENTER);
        int h = 500;
        int w = 700;
        if (Page.getCurrent().getBrowserWindowHeight() <700) {
            h = Page.getCurrent().getBrowserWindowHeight();
        }
        if (Page.getCurrent().getBrowserWindowWidth() < 700) {
            w = Page.getCurrent().getBrowserWindowWidth();
        }

        popupBodyLayout.setWidth((w - 50) + "px");
        popupBodyLayout.setHeightUndefined();//(h - 50) + "px");
        popupWindow.setWindowMode(WindowMode.NORMAL);
        popupWindow.setWidth(w + "px");
        popupWindow.setHeight(h + "px");
        popupWindow.setVisible(false);
        popupWindow.setResizable(false);
        popupWindow.setClosable(true);
        popupWindow.setStyleName(Reindeer.WINDOW_LIGHT);
        popupWindow.setModal(true);
        popupWindow.setDraggable(false);
        popupWindow.setCaption("&nbsp;&nbsp;Disease Groups Reorder");
        popupWindow.setCaptionAsHtml(true);

        UI.getCurrent().addWindow(popupWindow);
        popupWindow.center();
         int subH = (h-150) ;
        this.sortableDiseaseGroupI = new SortableLayoutContainer((w - 50), subH," Disease Group I");
        this.sortableDiseaseGroupII = new SortableLayoutContainer((w - 50), subH," Disease Group II");
        this.initPopupBody((w - 50));
    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        colHeaders = Quant_Central_Manager.getSelectedHeatMapColumns();
        rowHeaders = Quant_Central_Manager.getSelectedHeatMapRows();
        sortableDiseaseGroupI.updateListes(rowHeaders);
        sortableDiseaseGroupII.updateListes(colHeaders);
        popupWindow.setVisible(true);
    }

    private void initPopupBody(int w) {
        HorizontalLayout mainContainer = new HorizontalLayout();
        mainContainer.setStyleName(Reindeer.LAYOUT_WHITE);
        mainContainer.setSpacing(true);
        mainContainer.setWidth(w + "px");
       
        mainContainer.setHeightUndefined();
        mainContainer.setMargin(new MarginInfo(true, true, true, true));
        
        mainContainer.addComponent(sortableDiseaseGroupI);
        mainContainer.setComponentAlignment(sortableDiseaseGroupI, Alignment.TOP_LEFT);

        mainContainer.addComponent(sortableDiseaseGroupII);
        mainContainer.setComponentAlignment(sortableDiseaseGroupII, Alignment.TOP_LEFT);

        popupBodyLayout.addComponent(mainContainer);
        popupBodyLayout.setComponentAlignment(mainContainer, Alignment.TOP_LEFT);
        
        HorizontalLayout bottomLayout = new HorizontalLayout();
        bottomLayout.setWidth("100%");
        bottomLayout.setMargin(new MarginInfo(true, false, false, false));
        
        
        popupBodyLayout.addComponent(bottomLayout);
        Label commentLabel = new Label("*Sort – Drag & drop");
        bottomLayout.addComponent(commentLabel);
        bottomLayout.setComponentAlignment(commentLabel, Alignment.TOP_LEFT);
        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.setSpacing(true);
        
        btnLayout.setWidthUndefined();
        bottomLayout.addComponent(btnLayout);
        bottomLayout.setComponentAlignment(btnLayout, Alignment.TOP_RIGHT);
        Button applyFilters = new Button("Apply");
        applyFilters.setDescription("Reorder disease groups");
        applyFilters.setPrimaryStyleName("resetbtn");
        applyFilters.setWidth("50px");
        applyFilters.setHeight("24px");

        btnLayout.addComponent(applyFilters);
        applyFilters.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                popupWindow.close();
            }
        });

        Button resetFiltersBtn = new Button("Cancel");
        resetFiltersBtn.setPrimaryStyleName("resetbtn");
        btnLayout.addComponent(resetFiltersBtn);
        resetFiltersBtn.setWidth("50px");
        resetFiltersBtn.setHeight("24px");

        resetFiltersBtn.setDescription("Reset all applied filters");
        resetFiltersBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                popupWindow.close();
            }
        });

    }
    
    
    

   

}
