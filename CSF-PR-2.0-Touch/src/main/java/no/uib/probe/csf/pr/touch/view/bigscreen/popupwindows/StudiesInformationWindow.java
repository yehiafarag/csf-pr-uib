/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.bigscreen.popupwindows;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;
import no.uib.probe.csf.pr.touch.view.core.DatasetInformationOverviewLayout;
import no.uib.probe.csf.pr.touch.view.core.StudyPopupLayout;

/**
 *
 * @author Yehia Farag
 * 
 * this class represents study information popup window
 */
public class StudiesInformationWindow extends VerticalLayout implements LayoutEvents.LayoutClickListener {
 private final Window popupWindow;

    public StudiesInformationWindow(Set<QuantDatasetObject> dsObjects,Map<String, String> diseaseHashedColorMap) {

        int height = Page.getCurrent().getBrowserWindowHeight() - 100;
        int width = Page.getCurrent().getBrowserWindowWidth() - 100;
        VerticalLayout popupBody = new VerticalLayout();
        popupBody.setWidth((width-20) + "px");
        popupBody.setHeightUndefined();
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
        popupWindow.setModal(true);
        popupWindow.setDraggable(false);
        popupWindow.center();

        popupWindow.setCaption("<font color='gray' style='font-weight: bold;!important'>&nbsp;&nbsp;Datasets Information</font>");

        UI.getCurrent().addWindow(popupWindow);
        popupWindow.center();

        popupWindow.setCaptionAsHtml(true);
        popupWindow.setClosable(true);
        this.addLayoutClickListener(StudiesInformationWindow.this);
        
        
         Map<Integer, DatasetInformationOverviewLayout> datasetInfoLayoutDSIndexMap = new HashMap<>();
         dsObjects.stream().forEach((quantDs) -> {
             DatasetInformationOverviewLayout datasetInfoLayout = new DatasetInformationOverviewLayout(width-40,diseaseHashedColorMap);
             datasetInfoLayout.updateDatasetForm(quantDs);
             datasetInfoLayoutDSIndexMap.put(quantDs.getDsKey(), datasetInfoLayout);
     });
        

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