/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.bigscreen.popupwindows;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Collection;
import java.util.List;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;
import no.uib.probe.csf.pr.touch.view.core.DatasetButtonsContainerLayout;
import no.uib.probe.csf.pr.touch.view.core.PopupWindow;

/**
 *
 * @author Yehia Farag
 *
 * this class represents study information popup window
 */
public class StudiesInformationWindow extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private final PopupWindow popupWindow;
    private final DatasetButtonsContainerLayout studiesPopupLayout;

    public StudiesInformationWindow(List<Object[]> publicationList) {

        VerticalLayout popupBody = new VerticalLayout();
        popupBody.setWidth(100, Unit.PERCENTAGE);
        popupBody.setHeight(100, Unit.PERCENTAGE);

        popupBody.setMargin(false);
        popupBody.setSpacing(true);
        popupBody.addStyleName("roundedborder");
        popupBody.addStyleName("whitelayout");
        popupBody.addStyleName("padding20");
        VerticalLayout frame = new VerticalLayout();
        frame.setWidth(99, Unit.PERCENTAGE);
        frame.setHeight(99, Unit.PERCENTAGE);
        frame.setSpacing(true);
        frame.addComponent(popupBody);
        String title = "Datasets and Publications";
        if (publicationList == null) {
            title = "Datasets";
        }

        popupWindow = new PopupWindow(frame, title) {

            @Override
            public void close() {
                popupWindow.setVisible(false);

            }

            @Override
            public void setVisible(boolean visible) {

                if (visible) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }
                }
                super.setVisible(visible); //To change body of generated methods, choose Tools | Templates.
            }

        };

//        int availableHeight = (int) popupWindow.getHeight();
//        popupBody.setHeight(availableHeight, Unit.PIXELS);
        this.addLayoutClickListener(StudiesInformationWindow.this);

        TabSheet tab = new TabSheet();
        tab.setHeight(100.0f, Unit.PERCENTAGE);
        tab.setWidth(100.0f, Unit.PERCENTAGE);
        tab.addStyleName(ValoTheme.TABSHEET_FRAMED);
        tab.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        tab.addStyleName("transparentframe");

        popupBody.addComponent(tab);
        popupBody.setComponentAlignment(tab, Alignment.TOP_CENTER);

        studiesPopupLayout = new DatasetButtonsContainerLayout((int) popupWindow.getWidth());

        tab.addTab(studiesPopupLayout, "Datasets");

        if (publicationList == null) {
            return;
        }

        DatasetButtonsContainerLayout publicationPopupLayout = new DatasetButtonsContainerLayout((int) popupWindow.getWidth());
        publicationPopupLayout.setPublicationData(publicationList);
        tab.addTab(publicationPopupLayout, "Publications");

    }

    public void updateData(Collection<QuantDatasetObject> dsObjects) {
        studiesPopupLayout.setInformationData(dsObjects);

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        popupWindow.setVisible(true);
    }

    public void view() {
        popupWindow.setVisible(true);
    }
}
