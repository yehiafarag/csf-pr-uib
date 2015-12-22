/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csf.handler;

import com.csf.DAL.DAL;
import com.pepshaker.util.beans.ExperimentBean;
import com.quantcsf.QuantDataHandler;
import com.quantcsf.beans.QuantDatasetObject;
import com.quantcsf.beans.QuantPeptide;
import com.quantcsf.beans.QuantProtein;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
/*
 * @author Yehia Farag
 */

public class Handler {

    private final DAL dal;
    private final QuantDataHandler qDataHandler;

    public Handler(String url, String dbName, String driver, String userName, String password) throws SQLException {
        dal = new DAL(url, dbName, driver, userName, password);
        qDataHandler = new QuantDataHandler();
    }

    public boolean handelPeptideShakerProject(ExperimentBean exp) {
        boolean test = false;
        int expId = dal.storeExperiment(exp);
        exp.setExpId(expId);
        if (!exp.getProteinList().isEmpty()) {
            test = dal.storeProteinsList(exp);
        }
        if (!exp.getPeptideList().isEmpty()) {
            test = dal.storePeptidesList(exp);
        }
        if (!exp.getFractionsList().isEmpty()) {
            test = dal.storeFractionsList(exp);
        }
        System.gc();
        return test;
    }

    public boolean checkName(String name) throws SQLException {
        boolean test = dal.checkName(name);
        return test;
    }

    public boolean exportDataBase(String mysqldumpUrl, String sqlFileUrl) {
        return dal.exportDataBase(mysqldumpUrl, sqlFileUrl);
    }

    public boolean restoreDB(String sqlFileUrl, String mysqldumpUrl) {
        return dal.restoreDB(sqlFileUrl, mysqldumpUrl);
    }

    public boolean handelQuantPubData(String quantDataFilePath, String sequanceFilePath) {

        //1.read file
        List<QuantProtein> qProtList = qDataHandler.readCSVQuantFile(quantDataFilePath, sequanceFilePath);
//        2.store full data
        dal.storeCombinedQuantProtTable(qProtList);
//        3.update dataset table
        dal.storeQuantDatasets();
        //handel quant peptideProtein     
        Set<QuantDatasetObject> datasetsList = dal.getQuantDatasetListObject();
        List<QuantProtein> updatedQuantProtList = this.handleQuantData(datasetsList, qProtList);
//        //store quant protiens
        Object[] maps = dal.storeQuantitiveProteins(updatedQuantProtList);
        Map<String, Integer> peptideKeyToProteinIndexMap = (Map<String, Integer>) maps[0];
        Map<Integer, Integer> protKeyToDsIndexMap = (Map<Integer, Integer>) maps[1];

        List<QuantProtein> peptidesList = handelQuantPeptides(qProtList, peptideKeyToProteinIndexMap, protKeyToDsIndexMap);
        //store quant peptides
        dal.storeQuantitivePeptides(peptidesList);

        return true;
    }

//    public boolean handelProtSequanceData(String path) {
//        System.out.println("file path " + path);
//
//        //1.read file 
////         Map<String,QuantProtein> protSeqMap=qDataHandler.readSequanceFile(path);
////        //2.store full data
////        bool ean test  = dal.updateProtSequances(protSeqMap);
////        //3.update dataset table
////        dal.storeQuantDatasets();
////        //handel quant peptideProtein     
////        Set<QuantDatasetObject> datasetsList = dal.getQuantDatasetListObject();
////        qProtList = this.handleQuantData(datasetsList,qProtList);
////        int protIndex = dal.getCurrentProtIndex();
////        List<QuantPeptide> peptidesList = handelQuantPeptides(qProtList,protIndex);        
////        
////        //foldchange
////        
////        defineProtFoldChange(qProtList, peptidesList);
////        
////        
////        //store quant protiens
////        dal.storeQuantitiveProteins(qProtList);
////        
////        //store quant peptides
////        dal.storeQuantitivePeptides(peptidesList);
////        
////        System.out.println("final peptideProtein list updated (should be smaller)  " + qProtList.size() + "  prer " + peptidesList.size());
//        return true;
//    }
    private List<QuantProtein> handelQuantPeptides(List<QuantProtein> fullQuantProtList, Map<String, Integer> peptideKeyToProteinIndexMap, Map<Integer, Integer> protKeyToDsIndexMap) {
//        Map<String, QuantPeptide> peptides = new HashMap<String, QuantPeptide>();
        List<QuantProtein> peptidesList = new ArrayList<QuantProtein>();
//        Set<String>KeyCounter = new HashSet<String>();
//        List<QuantProtein> updatedQuantProtList = new ArrayList<QuantProtein>();
        Map<String, QuantProtein> potentialPeptidesList = new HashMap<String, QuantProtein>();
        Set<String> currentPeptidesList = new HashSet<String>();

        for (QuantProtein peptideProtein : fullQuantProtList) {

            if (!peptideProtein.isPeptideProtein()) {
                if (peptideProtein.getPeptideSequance() != null && !peptideProtein.getPeptideSequance().trim().equalsIgnoreCase("") && !peptideProtein.getPeptideSequance().trim().equalsIgnoreCase("Not Available")) {
                    peptideProtein.setProtKey(peptideKeyToProteinIndexMap.get(peptideProtein.getQuantPeptideKey()));
                    peptideProtein.setDsKey(protKeyToDsIndexMap.get(peptideProtein.getProtKey()));
                    if (potentialPeptidesList.containsKey(peptideProtein.getQuantPeptideKey())) //                        System.out.println("this is error 2 proteins has peptide info "+ peptideProtein.getPumedID()+"  "+ peptideProtein.getProtKey()+"  "+ peptideProtein.getStudyKey()+"   "+peptideProtein.getUniprotAccession());
                    {
                        System.out.println("error for the key " + peptideProtein.getQuantPeptideKey());
                    } else {
                        potentialPeptidesList.put(peptideProtein.getQuantPeptideKey(), peptideProtein);
                    }
//                    System.out.println("added new potential added "+ peptideProtein.getProtKey());
                }
                continue;
            }

            if (peptideKeyToProteinIndexMap.containsKey(peptideProtein.getQuantPeptideKey())) {
                peptideProtein.setProtKey(peptideKeyToProteinIndexMap.get(peptideProtein.getQuantPeptideKey()));
                peptideProtein.setDsKey(protKeyToDsIndexMap.get(peptideProtein.getProtKey()));
//                KeyCounter.add(peptideProtein.getqPeptideKey());

            } else {
                System.out.println("not related to any protein peptides keys is " + peptideProtein.getQuantPeptideKey());
            }
            if (peptideProtein.getUniprotAccession().equalsIgnoreCase("") || peptideProtein.getUniprotAccession().equalsIgnoreCase("Not Available") || peptideProtein.getUniprotAccession().equalsIgnoreCase("Entry Deleted") || peptideProtein.getUniprotAccession().equalsIgnoreCase("Entry Demerged") || peptideProtein.getUniprotAccession().equalsIgnoreCase("NOT RETRIEVED") || peptideProtein.getUniprotAccession().equalsIgnoreCase("DELETED")) {
                peptideProtein.setUniprotAccession(peptideProtein.getPublicationAccNumber());
            }
            currentPeptidesList.add(peptideProtein.getQuantPeptideKey());
            peptidesList.add(peptideProtein);

//            
//
//            QuantPeptide pep = new QuantPeptide();
//            pep.setDsKey(peptideProtein.getDsKey());
//            pep.setFc(peptideProtein.getStringFCValue());
//            pep.setPvalue(peptideProtein.getpValue());
//            pep.setRoc(peptideProtein.getRocAuc());
//            pep.setFcPatientGroupIonPatientGroupII(peptideProtein.getFcPatientGroupIonPatientGroupII());
//            pep.setModificationComment(peptideProtein.getModificationComment());
//            pep.setPeptideModification(peptideProtein.getPeptideModification());
//            pep.setPeptideSequance(peptideProtein.getPeptideSequance());
//            pep.setStrPvalue(peptideProtein.getStringPValue());
//            pep.setPvalueComment(peptideProtein.getPvalueComment());
//
//            String acc = peptideProtein.getUniprotAccession();
//            if (acc.equalsIgnoreCase("Not Available")) {
//                acc = peptideProtein.getPublicationAccNumber();
//            }
//            if (!proteins.containsKey(peptideProtein.getDsKey() + "-" + acc)) {
//                pep.setProtKey(protIndex);
//                peptideProtein.setProtKey(protIndex);
//                proteins.put(peptideProtein.getDsKey() + "-" + acc, peptideProtein);
//                protIndex++;
//
//            } else {
//                pep.setProtKey(proteins.get(peptideProtein.getDsKey() + "-" + acc).getProtKey());
//            }
//            peptidesList.add(pep);
        }

        for (String peptideKey : potentialPeptidesList.keySet()) {
            if (!currentPeptidesList.contains(peptideKey)) {
                QuantProtein quantPeptide = potentialPeptidesList.get(peptideKey);
                quantPeptide.setPeptideProtein(true);
                peptidesList.add(quantPeptide);

            } else {

            }

        }

//        fullQuantProtList.clear();
//        for (QuantProtein qprot : proteins.values()) {
//            fullQuantProtList.add(qprot);
//
//        }
//        Set<String> ids = new HashSet<String>();
//        for( QuantPeptide pep  :peptidesList)
//            ids.add(pep.getProtKey()+"");
//
//        System.out.println("at fullQuantProtList " + fullQuantProtList.size() + "   -- filtered to " + proteins.size() + "   peptides are " + peptidesList.size()+"  ids size "+ids.size());
//        
//        ready to store in db
        return peptidesList;
    }

    private List<QuantProtein> handleQuantData(Set<QuantDatasetObject> dss, List<QuantProtein> qProtList) {

        List<QuantProtein> updatedQuantProtList = new ArrayList<QuantProtein>();
        for (QuantProtein qp : qProtList) {
//            if (qp.isPeptideProtein()) {
//                continue;
//            }
            for (QuantDatasetObject ds : dss) {

                if (!ds.getPumedID().equalsIgnoreCase(qp.getPumedID())) {
                    continue;
                }

                if (!ds.getAuthor().equalsIgnoreCase(qp.getAuthor())) {
                    continue;
                }
                if (!ds.getDiseaseCategory().equalsIgnoreCase(qp.getDiseaseCategory())) {
                    continue;
                }

//                if (!ds.getAdditionalcomments().equalsIgnoreCase(qp.getAdditionalComments())) {
//                    continue;
//                }
//                if (ds.getFilesNumber() != (qp.getFilesNum())) {
//                    continue;
//                }
                if (ds.getIdentifiedProteinsNumber() != (qp.getIdentifiedProteinsNum())) {
                    continue;
                }
                if (ds.getQuantifiedProteinsNumber() != (qp.getQuantifiedProteinsNumber())) {
                    continue;
                }

//                if (!ds.getDiseaseGroups().equalsIgnoreCase(qp.getDiseaseGroups())) {
//                    continue;
//                }
                if (!ds.getRawDataUrl().equalsIgnoreCase(qp.getRawDataAvailable())) {

                    continue;
                }

                if (!ds.getTypeOfStudy().equalsIgnoreCase(qp.getTypeOfStudy())) {
                    continue;
                }

                if (ds.getYear() != (qp.getYear())) {

                    continue;
                }

                if (!ds.getSampleType().equalsIgnoreCase(qp.getSampleType())) {
                    continue;
                }
                if (!ds.getSampleMatching().equalsIgnoreCase(qp.getSampleMatching())) {

                    continue;
                }
                if (!ds.getTechnology().equalsIgnoreCase(qp.getTechnology())) {

                    continue;
                }

                if (!ds.getAnalyticalApproach().replace("-", " ").equalsIgnoreCase(qp.getAnalyticalApproach().replace("-", " "))) {

                    continue;
                }
                if (!ds.getEnzyme().equalsIgnoreCase(qp.getEnzyme())) {

                    continue;
                }
                if (!ds.getShotgunTargeted().equalsIgnoreCase(qp.getShotgunOrTargetedQquant())) {

                    continue;
                }
                if (!ds.getQuantificationBasis().equalsIgnoreCase(qp.getQuantificationBasis())) {

                    continue;
                }

                if (!ds.getQuantBasisComment().equalsIgnoreCase(qp.getQuantBasisComment())) {
                    continue;
                }
                if (!ds.getPatientsGroup1().equalsIgnoreCase(qp.getPatientGroupI())) {

                    continue;
                }
                if (!ds.getNormalizationStrategy().equalsIgnoreCase(qp.getNormalizationStrategy())) {

                    continue;
                }

                if (!ds.getPatientsGroup1Comm().equalsIgnoreCase(qp.getPatientGrIComment())) {

                    continue;
                }
                if (!ds.getPatientsSubGroup1().equalsIgnoreCase(qp.getPatientSubGroupI())) {
                    continue;
                }

                if (!ds.getPatientsGroup2().equalsIgnoreCase(qp.getPatientGroupII())) {

                    continue;
                }

                if (!ds.getPatientsGroup2Comm().equalsIgnoreCase(
                        qp.getPatientGrIIComment())) {
                    continue;
                }
                if (!ds.getPatientsSubGroup2().equalsIgnoreCase(qp.getPatientSubGroupII())) {

                    continue;
                }
                if (ds.getPatientsGroup1Number() != (qp.getPatientsGroupINumber())) {

                    continue;
                }
                if (ds.getPatientsGroup2Number() != (qp.getPatientsGroupIINumber())) {

                    continue;
                }
                qp.setDsKey(ds.getUniqId());
                ds.addQuantProt(qp);
                updatedQuantProtList.add(qp);
                break;

            }

        }
        return updatedQuantProtList;

    }

    private void defineProtFoldChange(List<QuantProtein> qProtList, List<QuantPeptide> quantPepList) {
        for (QuantProtein prot : qProtList) {
            List<Integer> calcList = new ArrayList<Integer>();
            for (QuantPeptide pep : quantPepList) {
                if ((pep.getDsKey() == prot.getDsKey()) && pep.getProtKey() == prot.getProtKey()) {
                    if (pep.getFc().equalsIgnoreCase("Not Provided")) {
                        calcList.add(0);
                    } else if (pep.getFc().equalsIgnoreCase("Increased")) {
                        calcList.add(1);
                    } else if (pep.getFc().equalsIgnoreCase("Decreased")) {
                        calcList.add(-1);
                    }
                }

            }
            int fc = 0;
            for (int i : calcList) {
                fc += i;
            }
            if (fc == 0) {
                prot.setStringFCValue("Not Provided");
            } else if (fc > 0) {
                prot.setStringFCValue("Increased");
            } else {
                prot.setStringFCValue("Decreased");
            }

        }

    }

    public void correctProtInfo() {
        dal.correctProtInfo();

    }
}
