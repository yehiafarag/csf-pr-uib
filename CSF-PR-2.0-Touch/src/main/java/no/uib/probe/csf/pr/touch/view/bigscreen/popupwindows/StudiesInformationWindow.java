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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDataset;
import no.uib.probe.csf.pr.touch.view.core.DatasetButtonsContainerLayout;
import no.uib.probe.csf.pr.touch.view.core.PopupWindow;
import no.uib.probe.csf.pr.touch.view.core.PopupWindowFrame;

/**
 *
 * @author Yehia Farag
 *
 * this class represents study information popup window
 */
public abstract class StudiesInformationWindow extends VerticalLayout implements LayoutEvents.LayoutClickListener{

    private final DatasetButtonsContainerLayout studiesPopupLayout;
    private List<Object[]> publicationList;
    private final TabSheet tab;
    private final DatasetButtonsContainerLayout publicationPopupLayout;
    private final  VerticalLayout popupBody;
    
    private final PopupWindowFrame popupWindowBtn;

    public StudiesInformationWindow(List<Object[]> publicationList, boolean smallScreen) {

        popupBody = new VerticalLayout();
        popupWindowBtn = new PopupWindowFrame("Datasets and Publications", popupBody);

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
        

        studiesPopupLayout = new DatasetButtonsContainerLayout((int) popupWindowBtn.getFrameWidth(), smallScreen);
        if (studiesPopupLayout.getColNumber() == 1) {
            popupWindowBtn.setFrameWidth(260);
        }

        tab.addTab(studiesPopupLayout, "Datasets");
        publicationPopupLayout = new DatasetButtonsContainerLayout((int) popupWindowBtn.getFrameWidth(), smallScreen);
        tab.addTab(publicationPopupLayout, "Publications");
         tab.addSelectedTabChangeListener((TabSheet.SelectedTabChangeEvent event) -> {
            if (event.getTabSheet().getTabPosition((tab.getTab(tab.getSelectedTab()))) == 0) {
                popupWindowBtn.setFrameHeight(193 + (studiesPopupLayout.getRowcounter() * 100));
               } else {
                   popupWindowBtn.setFrameHeight(193 + (publicationPopupLayout.getRowcounter() * 100));
            }
        });
        
        if (publicationList == null) {
            this.publicationList = publicationList;
            return;
        }
        publicationPopupLayout.setPublicationData(publicationList);

    }

    public void updateData(Collection<QuantDataset> dsObjects) {
        studiesPopupLayout.setInformationData(dsObjects);

        if (publicationList == null) {
            Set<String> publicationMap = new LinkedHashSet<>();
            for (QuantDataset quantDS : dsObjects) {
                publicationMap.add(quantDS.getPubMedId());

            }
            publicationPopupLayout.setPublicationData(updatePublications(publicationMap));
        }
        popupWindowBtn.setFrameHeight(173 + (studiesPopupLayout.getRowcounter() * 100));

    }

    public abstract List<Object[]> updatePublications(Set<String> pumedId);

    public void view() {
        popupWindowBtn.view();
    }
     @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
         view();
    }
}
