/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.welcomelayout;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import probe.com.model.beans.quant.QuantDatasetObject;
import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.peptidescontainer.popupcomponents.DatasetInformationOverviewLayout;
import probe.com.view.core.StudyPopupLayout;

/**
 *
 * @author yfa041
 */
public class StudiesInformationWindow extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private final Window popupWindow;

    public StudiesInformationWindow(Set<QuantDatasetObject> dsObjects,Map<String, String> diseaseHashedColorMap) {

        int height = Page.getCurrent().getBrowserWindowHeight() - 100;
        int width = Page.getCurrent().getBrowserWindowWidth() - 100;
        VerticalLayout popupBody = new VerticalLayout();
        popupBody.setWidth((width-20) + "px");
        popupBody.setHeightUndefined();
        popupBody.setStyleName(Reindeer.LAYOUT_WHITE);
        popupBody.setMargin(true);
        popupBody.setSpacing(true);

        popupWindow = new Window() {

            @Override
            public void close() {
                popupWindow.setVisible(false);

            }

        };

        popupWindow.setContent(popupBody);
        popupWindow.setWindowMode(WindowMode.NORMAL);
        popupWindow.setWidth((width) + "px");
        popupWindow.setHeight((height) + "px");
        popupWindow.setVisible(false);
        popupWindow.setResizable(false);
        popupWindow.setClosable(false);
        popupWindow.setStyleName(Reindeer.WINDOW_LIGHT);
        popupWindow.setModal(true);
        popupWindow.setDraggable(false);
        popupWindow.center();

        popupWindow.setCaption("&nbsp;&nbsp;Publication Information");

        UI.getCurrent().addWindow(popupWindow);
        popupWindow.center();

        popupWindow.setCaptionAsHtml(true);
        popupWindow.setClosable(true);
        this.addLayoutClickListener(StudiesInformationWindow.this);
        
        
         Map<Integer, DatasetInformationOverviewLayout> datasetInfoLayoutDSIndexMap = new HashMap<Integer, DatasetInformationOverviewLayout>();
        for (QuantDatasetObject quantDs : dsObjects) {
                DatasetInformationOverviewLayout datasetInfoLayout = new DatasetInformationOverviewLayout(width-40,diseaseHashedColorMap);
                datasetInfoLayout.updateDatasetForm(quantDs);
                datasetInfoLayout.setWidth(width-40+"px");
                datasetInfoLayoutDSIndexMap.put(quantDs.getDsKey(), datasetInfoLayout);

            }
        

        StudyPopupLayout studiesPopupLayout = new StudyPopupLayout(datasetInfoLayoutDSIndexMap);
       
        popupBody.addComponent(studiesPopupLayout);
        popupBody.setComponentAlignment(studiesPopupLayout, Alignment.TOP_CENTER); 
        studiesPopupLayout.setInformationData(dsObjects);

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        popupWindow.setVisible(true);
    }

}
