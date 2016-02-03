/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.studies;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.DiseaseGroupsComparisonsProteinLayout;
import probe.com.model.beans.quant.QuantDatasetObject;
import probe.com.model.beans.quant.QuantProtein;
import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.peptidescontainer.ComparisonDetailsBean;
import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.peptidescontainer.StudyInformationPopupComponent;

/**
 *
 * @author Yehia Farag
 */
public class PeptidesStackedBarChartsControler {

    private final Map<String, StudyPopupLayout> studyPopupLayoutDSIndexMap = new HashMap<String, StudyPopupLayout>();
    private final StudyInformationPopupComponent studyInformationPopupPanel;

    /**
     *
     * @param width
     * @param patientGroupsNumToDsIdMap
     * @param protAccsession
     * @param protName
     * @param url
     * @param comparisonHeader
     * @param datasetQuantProteinsMap
     * @param datasetIdDsObjectProteinsMap
     */
    public PeptidesStackedBarChartsControler(int width, Map<Integer, ComparisonDetailsBean> patientGroupsNumToDsIdMap, String protAccsession, String protName, String url, String comparisonHeader, Map<String, QuantProtein> datasetQuantProteinsMap, Map<String, QuantDatasetObject> datasetIdDsObjectProteinsMap,Map<String,String> diseaseHashedColorMap) {

        int panelWidth = Page.getCurrent().getBrowserWindowWidth() - width - 100;

        studyInformationPopupPanel = new StudyInformationPopupComponent(panelWidth, protName, url, comparisonHeader);
        studyInformationPopupPanel.setVisible(false);
        int counter = 0;
        for (int pateintGrNum : patientGroupsNumToDsIdMap.keySet()) {
            while (counter < 4) {
                if (!patientGroupsNumToDsIdMap.get(pateintGrNum).getRegulatedList(counter).isEmpty()) {
                    StudyPopupLayout studyLayout = new StudyPopupLayout(panelWidth, datasetQuantProteinsMap, datasetIdDsObjectProteinsMap, protAccsession, url, protName,diseaseHashedColorMap);
                    studyPopupLayoutDSIndexMap.put("_-_" + pateintGrNum + "_-_" + counter + "_-_", studyLayout);
                }
                counter++;

            }
            counter = 0;

        }

    }

    /**
     *
     * @param pateintGrNum
     * @param trend
     * @param dsObjects
     * @param cp
     */
    public void updateSelectedProteinInformation(int pateintGrNum, int trend, Set<QuantDatasetObject> dsObjects, DiseaseGroupsComparisonsProteinLayout cp) {

        String pGrkey = "_-_" + pateintGrNum + "_-_" + trend + "_-_";
        System.out.println("at studyPopupLayoutDSIndexMap.keySet() "+studyPopupLayoutDSIndexMap.keySet());
        for (String key : studyPopupLayoutDSIndexMap.keySet()) {
            if (key.contains(pGrkey)) {
                StudyPopupLayout popupPanel = studyPopupLayoutDSIndexMap.get(key);
                if (popupPanel == null) {
                    Notification.show("No Information  available at this bar");
                } else {
                    popupPanel.setInformationData(dsObjects, cp);
                    studyInformationPopupPanel.updateContent(popupPanel);
                }
                return;
            }

        }

        Notification.show("No Information  available");
    }

}
