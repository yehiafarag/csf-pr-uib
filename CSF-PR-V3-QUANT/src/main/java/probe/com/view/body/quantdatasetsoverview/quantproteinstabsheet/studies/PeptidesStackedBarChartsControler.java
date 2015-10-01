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
     */
    public PeptidesStackedBarChartsControler(int width, Map<Integer, ComparisonDetailsBean> patientGroupsNumToDsIdMap ,String protName, String url,String comparisonHeader) {

        int panelWidth = Page.getCurrent().getBrowserWindowWidth() - width - 100;
       
        studyInformationPopupPanel = new StudyInformationPopupComponent(panelWidth,protName,url,comparisonHeader);
        studyInformationPopupPanel.setVisible(false);
        int counter = 0;
        for (int pateintGrNum : patientGroupsNumToDsIdMap.keySet()) {
            while (counter < 3) {
                if (!patientGroupsNumToDsIdMap.get(pateintGrNum).getRegulatedList(counter).isEmpty()) {
                   StudyPopupLayout studyLayout = new StudyPopupLayout(panelWidth);
//                    PeptideSequanceLocationOverview peptideStackedBarChart = new PeptideSequanceLocationOverview(new HashSet<Integer>(patientGroupsNumToDsIdMap.get(pateintGrNum).getRegulatedList(counter)), subWidth);
                    studyPopupLayoutDSIndexMap.put("-" + pateintGrNum + "-" + counter + "-", studyLayout);                    
                }
                counter++;

            }
            counter = 0;

        }

        //init leftside components        
//        Map<Integer, Set<QuantPeptide>> dsQuantPepMap = new HashMap<Integer, Set<QuantPeptide>>();
//         for (QuantPeptide quantPep : cp.getQuantPeptidesList()) { 
//             System.out.println("peptide key is "+quantPep.getDsKey());
//            if (!dsQuantPepMap.containsKey(quantPep.getDsKey())) {
//                Set<QuantPeptide> subList = new HashSet<QuantPeptide>();
//                dsQuantPepMap.put(quantPep.getDsKey(), subList);
//            }
//            Set<QuantPeptide> subList = dsQuantPepMap.get(quantPep.getDsKey());
//            subList.add(quantPep);
//            dsQuantPepMap.put(quantPep.getDsKey(), subList);
//        }
//        int counter = 0;
//        for (int pateintGrNum : patientGroupsNumToDsIdMap.keySet()) {
//            int subWidth = (int) ((double) panelWidth * 0.9);
//            while (counter < 3) {
//                if (!patientGroupsNumToDsIdMap.get(pateintGrNum).getRegulatedList(counter).isEmpty()) {
//                    PeptideSequanceLocationOverview peptideStackedBarChart = new PeptideSequanceLocationOverview(new HashSet<Integer>(patientGroupsNumToDsIdMap.get(pateintGrNum).getRegulatedList(counter)), subWidth);
//                    studyPopupLayoutDSIndexMap.put("" + counter + "-" + pateintGrNum + "_" + counter + "-", peptideStackedBarChart);
//                    counter++;
//                }
//
//            }
//            counter = 0;
//        }

    }

    /**
     *
     * @param dsIndex
     * @param dataset
     * @param protAcc
     */
    public void updateSelectedProteinInformation(int pateintGrNum, int trend, Set<QuantDatasetObject> dsObjects,DiseaseGroupsComparisonsProteinLayout cp) {
//        int dsIndex = dsIndexs[0];
        String pGrkey = "-" + pateintGrNum + "-" + trend + "-";
        for (String key : studyPopupLayoutDSIndexMap.keySet()) {
            if (key.contains(pGrkey)) {
                StudyPopupLayout popupPanel = studyPopupLayoutDSIndexMap.get(key);
                if (popupPanel == null) {
                    Notification.show("No Information  available at this bar");
                } else {
//                    String peptidesNumber = bar.getSignificantPeptidesNumber() + "/" + bar.getTotalPeptidesNumber() + "";
//                    studyInformationPopupPanel.updateForm(datasets.iterator().next(), bar, peptidesNumber, protAcc);
                    popupPanel.setInformationData(dsObjects,cp);
                    studyInformationPopupPanel.updateContent(popupPanel);
                }
                return;
            }

        }

        Notification.show("No Information  available");
    }

}
