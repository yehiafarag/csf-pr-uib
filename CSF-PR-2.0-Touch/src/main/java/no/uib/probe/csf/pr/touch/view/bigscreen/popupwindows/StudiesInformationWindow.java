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
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;
import no.uib.probe.csf.pr.touch.view.core.DatasetButtonsContainerLayout;
import no.uib.probe.csf.pr.touch.view.core.PopupWindow;

/**
 *
 * @author Yehia Farag
 *
 * this class represents study information popup window
 */
public abstract class StudiesInformationWindow extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private final PopupWindow popupWindow;
    private final DatasetButtonsContainerLayout studiesPopupLayout;
    private List<Object[]> publicationList;
    private final TabSheet tab;
    private final DatasetButtonsContainerLayout publicationPopupLayout;

    public StudiesInformationWindow(List<Object[]> publicationList, boolean smallScreen) {

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
        this.setHeight(10, Unit.PIXELS);

        tab = new TabSheet();
        tab.setHeight(100.0f, Unit.PERCENTAGE);
        tab.setWidth(100.0f, Unit.PERCENTAGE);
        tab.addStyleName(ValoTheme.TABSHEET_FRAMED);
        tab.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        tab.addStyleName("transparentframe");

        popupBody.addComponent(tab);
        popupBody.setComponentAlignment(tab, Alignment.TOP_CENTER);

        studiesPopupLayout = new DatasetButtonsContainerLayout((int) popupWindow.getWidth(), smallScreen);

        tab.addTab(studiesPopupLayout, "Datasets");
        publicationPopupLayout = new DatasetButtonsContainerLayout((int) popupWindow.getWidth(), smallScreen);
        tab.addTab(publicationPopupLayout, "Publications");
        if (publicationList == null) {
            this.publicationList = publicationList;
            return;
        }
        publicationPopupLayout.setPublicationData(publicationList);
        tab.addSelectedTabChangeListener((TabSheet.SelectedTabChangeEvent event) -> {
            if (event.getTabSheet().getTabPosition((tab.getTab(tab.getSelectedTab()))) == 0) {
                System.out.println("at selected tab is dataset");
//                popupWindow.setHeight(studiesPopupLayout.getLayoutHeight()+100, Unit.PIXELS);
            } else {
//                popupWindow.setHeight(publicationPopupLayout.getLayoutHeight()+100, Unit.PIXELS);
            }

        });
//        popupWindow.setHeight(studiesPopupLayout.getLayoutHeight()+100, Unit.PIXELS);

    }

    public void updateData(Collection<QuantDatasetObject> dsObjects) {
        studiesPopupLayout.setInformationData(dsObjects);

        if (publicationList == null) {
            Set<String> publicationMap = new LinkedHashSet<>();
            for (QuantDatasetObject quantDS : dsObjects) {

                publicationMap.add(quantDS.getPumedID());

            }
            publicationPopupLayout.setPublicationData(updatePublications(publicationMap));
        }

    }

    public abstract List<Object[]> updatePublications(Set<String> pumedId);

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        popupWindow.setVisible(true);
    }

    public void view() {
        popupWindow.setVisible(true);
    }
}
