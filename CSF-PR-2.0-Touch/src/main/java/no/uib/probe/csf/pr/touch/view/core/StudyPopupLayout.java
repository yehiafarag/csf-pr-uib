/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.server.Page;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;

/**
 *
 * @author Yehia Farag
 * 
 * this class represents study information popup window
 */
public class StudyPopupLayout extends VerticalLayout {

   
    private final GridLayout topLayout;
    private Map<Integer, DatasetInformationOverviewLayout> datasetInfoLayoutDSIndexMap;

    public void setInformationData(Set<QuantDatasetObject> dsObjects) {
        topLayout.removeAllComponents();
        int rowNumb = Math.max(1, ((dsObjects.size() / topLayout.getColumns()) + 1));
        topLayout.setRows(rowNumb);
        if (rowNumb == 1) {
            topLayout.setWidthUndefined();
        } else {
            topLayout.setWidth("100%");
        }

        int colcounter = 0;
        int rowcounter = 0;
        for (QuantDatasetObject quantDS : dsObjects) {
           String btnName =  "<font size=1 >"+quantDS.getPumedID()+"</font><br/>"+quantDS.getAuthor() + "<br/><font size=1 >" + quantDS.getYear() + "</font><br/><font size=1 >#Proteins: "+quantDS.getTotalProtNum()+"   #peptides: "+quantDS.getTotalPepNum()+"</font>";
            PopupInfoBtn btn = new PopupInfoBtn(datasetInfoLayoutDSIndexMap.get(quantDS.getDsKey()), btnName,quantDS.getAuthor());
            topLayout.addComponent(btn, colcounter++, rowcounter);
            if (colcounter >= topLayout.getColumns()) {
                colcounter = 0;
                rowcounter++;

            }

        }

    }

    public StudyPopupLayout(Map<Integer, DatasetInformationOverviewLayout> datasetInfoLayoutDSIndexMap) {
        this.datasetInfoLayoutDSIndexMap = datasetInfoLayoutDSIndexMap;
        this.setWidth("100%");
        this.setHeightUndefined();
        this.setMargin(false);
        this.setSpacing(true);
        int width = Page.getCurrent().getBrowserWindowWidth() * 90 / 100;
        int colNum = Math.max(1, width / 200);
        topLayout = new GridLayout();
        topLayout.setWidth("100%");
        topLayout.setColumns(colNum);
        topLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        topLayout.setHeightUndefined();
        topLayout.setSpacing(true);
        this.addComponent(topLayout);

    }


    public void updateDatasetInfoLayoutDSIndexMap(Map<Integer, DatasetInformationOverviewLayout> datasetInfoLayoutDSIndexMap) {
        this.datasetInfoLayoutDSIndexMap = datasetInfoLayoutDSIndexMap;

    }
}
