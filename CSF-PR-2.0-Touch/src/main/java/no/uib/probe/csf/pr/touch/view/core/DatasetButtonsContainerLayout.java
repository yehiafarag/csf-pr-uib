/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.server.Page;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.VerticalLayout;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;

/**
 *
 * @author Yehia Farag
 *
 * this class represents study information popup window
 */
public class DatasetButtonsContainerLayout extends VerticalLayout {

    private final GridLayout btnsContainer;

    private void setInformationData(Set<QuantDatasetObject> dsObjects) {
        btnsContainer.removeAllComponents();
        int rowNumb = Math.max(1, ((dsObjects.size() / btnsContainer.getColumns()) + 1));
        btnsContainer.setRows(rowNumb);
        if (rowNumb == 1) {
            btnsContainer.setWidthUndefined();
        } else {
            btnsContainer.setWidth("100%");
        }

        int colcounter = 0;
        int rowcounter = 0;
        for (QuantDatasetObject quantDS : dsObjects) {
            String btnName = "<font size=1 >" + quantDS.getPumedID() + "</font><br/>" + quantDS.getAuthor() + "<br/><font size=1 >" + quantDS.getYear() + "</font><br/><font size=1 >#Proteins: " + quantDS.getTotalProtNum() + "   #peptides: " + quantDS.getTotalPepNum() + "</font>";

            PopupInfoBtn btn = new PopupInfoBtn(quantDS, btnName, quantDS.getAuthor());
            btnsContainer.addComponent(btn, colcounter++, rowcounter);
            if (colcounter >= btnsContainer.getColumns()) {
                colcounter = 0;
                rowcounter++;

            }

        }

    }

    public DatasetButtonsContainerLayout(Set<QuantDatasetObject> dsObjects) {
        this.setWidth("100%");
        this.setHeightUndefined();
        this.setMargin(false);
        this.setSpacing(true);
        
        int width = Page.getCurrent().getBrowserWindowWidth() * 90 / 100;
        int colNum = Math.max(1, width / 200);
        btnsContainer = new GridLayout();
        btnsContainer.setWidth("100%");
        btnsContainer.setColumns(colNum);
        btnsContainer.setStyleName("whitelayout");
        btnsContainer.setHeightUndefined();
        btnsContainer.setSpacing(true);
        this.addComponent(btnsContainer);
        this.setInformationData(dsObjects);

    }

//    public void updateDatasetInfoLayoutDSIndexMap(Map<Integer, DatasetInformationOverviewLayout> datasetInfoLayoutDSIndexMap) {
//        this.datasetInfoLayoutDSIndexMap = datasetInfoLayoutDSIndexMap;
//
//    }
}
