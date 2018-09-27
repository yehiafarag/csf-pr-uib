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
import java.util.LinkedHashSet;
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

    public boolean handelQuantPubData(String quantDataFilePath, String sequanceFilePath, String unreviewFilePath) {

        qDataHandler.updatePublicationMap(getPublications());
        //1.read file
        List<QuantProtein> qProtList = qDataHandler.readCSVQuantFile(quantDataFilePath, sequanceFilePath, unreviewFilePath);
        //filter the quant proteins list 
        List<QuantProtein> filteredQuantProteinsList = filterQuantProteins(qProtList);
//        2.store full data
        dal.storeCombinedQuantProtTable(filteredQuantProteinsList);
        System.out.println("done with full table");
//        3.update dataset table
        dal.storeQuantDatasets();
        System.out.println("done with ds table");
        //handel quant peptideProtein     
        Set<QuantDatasetObject> datasetsList = dal.getQuantDatasetListObject();

        List<QuantProtein> updatedQuantProtList = this.handleQuantData(datasetsList, filteredQuantProteinsList);
        //store quant protiens
        Object[] maps = dal.storeQuantitiveProteins(updatedQuantProtList);
        System.out.println("done with protein table");
        Map<String, Integer> peptideKeyToProteinIndexMap = (Map<String, Integer>) maps[0];
        Map<Integer, Integer> protKeyToDsIndexMap = (Map<Integer, Integer>) maps[1];

        List<QuantProtein> peptidesList = handelQuantPeptides(filteredQuantProteinsList, peptideKeyToProteinIndexMap, protKeyToDsIndexMap);
//        store quant peptides
        dal.storeQuantitivePeptides(peptidesList);
        System.out.println("done with peptides table");
        this.updateQuantStudies(datasetsList, qProtList);
        System.out.println("update poublication");
        this.updateActivePublications(qDataHandler.getActivePublications(), qProtList);

        return true;
    }

    private List<QuantProtein> filterQuantProteins(List<QuantProtein> qProtList) {
        Map<String, QuantProtein> updatedFilteredProteinsList = new HashMap<String, QuantProtein>();
        Map<String, QuantProtein> updatedFullFilteredProteinsList = new HashMap<String, QuantProtein>();
        List<QuantProtein> updatedFilteredList = new ArrayList<QuantProtein>();
        Set<String> proteinsToAdd = new LinkedHashSet<String>();
        Set<String> cleaningSet = new HashSet<String>();
        List<QuantProtein> astridList = new ArrayList<QuantProtein>();
        for (QuantProtein quantProt : qProtList) {
            if (quantProt.isPeptideObject()) {
                continue;
            }
            String key = quantProt.getPumedID() + "_" + quantProt.getStudyKey() + "_" + quantProt.getQuantifiedProteinsNumber() + "_" + quantProt.getUniprotAccession() + "_" + quantProt.getPublicationAccNumber() + "_" + quantProt.getRawDataAvailable() + "_" + quantProt.getTypeOfStudy() + "_" + quantProt.getSampleType() + "_" + quantProt.getPatientsGroupINumber() + "_" + quantProt.getPatientsGroupIINumber() + "_" + quantProt.getPatientGrIComment() + "_" + quantProt.getPatientGrIIComment() + "_" + quantProt.getPatientGroupI() + "_" + quantProt.getPatientGroupII() + "_" + quantProt.getPatientSubGroupI() + "_" + quantProt.getPatientSubGroupII() + "_" + quantProt.getNormalizationStrategy() + "_" + quantProt.getTechnology() + "_" + quantProt.getAnalyticalApproach() + "_" + quantProt.getAnalyticalMethod() + "_" + quantProt.getShotgunOrTargetedQquant() + "_" + quantProt.getEnzyme() + "_" + quantProt.getQuantificationBasis() + "_" + quantProt.getDiseaseCategory();
            String key2 = quantProt.getPumedID() + "_" + quantProt.getStudyKey() + "_" + quantProt.getQuantifiedProteinsNumber() + "_" + quantProt.getUniprotAccession() + "_" + quantProt.getPublicationAccNumber() + "_" + quantProt.getRawDataAvailable() + "_" + quantProt.getTypeOfStudy() + "_" + quantProt.getSampleType() + "_" + quantProt.getPatientsGroupINumber() + "_" + quantProt.getPatientsGroupIINumber() + "_" + quantProt.getPatientGrIComment() + "_" + quantProt.getPatientGrIIComment() + "_" + quantProt.getPatientGroupI() + "_" + quantProt.getPatientGroupII() + "_" + quantProt.getPatientSubGroupI() + "_" + quantProt.getPatientSubGroupII() + "_" + quantProt.getNormalizationStrategy() + "_" + quantProt.getTechnology() + "_" + quantProt.getAnalyticalApproach() + "_" + quantProt.getAnalyticalMethod() + "_" + quantProt.getShotgunOrTargetedQquant() + "_" + quantProt.getEnzyme() + "_" + quantProt.getDiseaseCategory();

            if (!updatedFilteredProteinsList.containsKey(key) && !cleaningSet.contains(key)) {
                updatedFilteredProteinsList.put(key, quantProt);
                updatedFullFilteredProteinsList.put(key2, quantProt);

            } else {
                astridList.add(quantProt);
                updatedFilteredProteinsList.remove(key);
                updatedFullFilteredProteinsList.remove(key2);
                cleaningSet.add(key);
            }

        }

        for (QuantProtein quantPeptide : qProtList) {
            if (quantPeptide.isPeptideObject()) {
                String key2 = quantPeptide.getPumedID() + "_" + quantPeptide.getStudyKey() + "_" + quantPeptide.getQuantifiedProteinsNumber() + "_" + quantPeptide.getUniprotAccession() + "_" + quantPeptide.getPublicationAccNumber() + "_" + quantPeptide.getRawDataAvailable() + "_" + quantPeptide.getTypeOfStudy() + "_" + quantPeptide.getSampleType() + "_" + quantPeptide.getPatientsGroupINumber() + "_" + quantPeptide.getPatientsGroupIINumber() + "_" + quantPeptide.getPatientGrIComment() + "_" + quantPeptide.getPatientGrIIComment() + "_" + quantPeptide.getPatientGroupI() + "_" + quantPeptide.getPatientGroupII() + "_" + quantPeptide.getPatientSubGroupI() + "_" + quantPeptide.getPatientSubGroupII() + "_" + quantPeptide.getNormalizationStrategy() + "_" + quantPeptide.getTechnology() + "_" + quantPeptide.getAnalyticalApproach() + "_" + quantPeptide.getAnalyticalMethod() + "_" + quantPeptide.getShotgunOrTargetedQquant() + "_" + quantPeptide.getEnzyme() + "_" + quantPeptide.getDiseaseCategory();
                if (updatedFullFilteredProteinsList.containsKey(key2)) {
                    updatedFilteredList.add(quantPeptide);

                } else {//peptide without proteins
                    QuantProtein quantProt = qDataHandler.initQuantProteinFromQuantPeptide(quantPeptide);

                    String tkey = quantProt.getPumedID() + "_" + quantProt.getStudyKey() + "_" + quantProt.getQuantifiedProteinsNumber() + "_" + quantProt.getUniprotAccession() + "_" + quantProt.getPublicationAccNumber() + "_" + quantProt.getRawDataAvailable() + "_" + quantProt.getTypeOfStudy() + "_" + quantProt.getSampleType() + "_" + quantProt.getPatientsGroupINumber() + "_" + quantProt.getPatientsGroupIINumber() + "_" + quantProt.getPatientGrIComment() + "_" + quantProt.getPatientGrIIComment() + "_" + quantProt.getPatientGroupI() + "_" + quantProt.getPatientGroupII() + "_" + quantProt.getPatientSubGroupI() + "_" + quantProt.getPatientSubGroupII() + "_" + quantProt.getNormalizationStrategy() + "_" + quantProt.getTechnology() + "_" + quantProt.getAnalyticalApproach() + "_" + quantProt.getAnalyticalMethod() + "_" + quantProt.getShotgunOrTargetedQquant() + "_" + quantProt.getEnzyme() + "_" + quantProt.getQuantificationBasis() + "_" + quantProt.getDiseaseCategory();
                    String tkey2 = quantProt.getPumedID() + "_" + quantProt.getStudyKey() + "_" + quantProt.getQuantifiedProteinsNumber() + "_" + quantProt.getUniprotAccession() + "_" + quantProt.getPublicationAccNumber() + "_" + quantProt.getRawDataAvailable() + "_" + quantProt.getTypeOfStudy() + "_" + quantProt.getSampleType() + "_" + quantProt.getPatientsGroupINumber() + "_" + quantProt.getPatientsGroupIINumber() + "_" + quantProt.getPatientGrIComment() + "_" + quantProt.getPatientGrIIComment() + "_" + quantProt.getPatientGroupI() + "_" + quantProt.getPatientGroupII() + "_" + quantProt.getPatientSubGroupI() + "_" + quantProt.getPatientSubGroupII() + "_" + quantProt.getNormalizationStrategy() + "_" + quantProt.getTechnology() + "_" + quantProt.getAnalyticalApproach() + "_" + quantProt.getAnalyticalMethod() + "_" + quantProt.getShotgunOrTargetedQquant() + "_" + quantProt.getEnzyme() + "_" + quantProt.getDiseaseCategory();

                    if (!updatedFilteredProteinsList.containsKey(tkey) && !cleaningSet.contains(tkey)) {
                        updatedFilteredProteinsList.put(tkey, quantProt);
                        updatedFullFilteredProteinsList.put(tkey2, quantProt);

                    } else {
                        astridList.add(quantProt);
                        updatedFilteredProteinsList.remove(tkey);
                        updatedFullFilteredProteinsList.remove(tkey2);
                        cleaningSet.add(tkey);
                    }
//                    System.out.println("astrid file --- >>  " + key);
                    astridList.add(quantPeptide);
                    proteinsToAdd.add(quantPeptide.getPumedID() + "_" + quantPeptide.getStudyKey() + "_" + quantPeptide.getUniprotAccession());

                }
            }
        }
        updatedFilteredList.addAll(updatedFilteredProteinsList.values());
        System.out.println("the diffrent in size is " + (qProtList.size() - updatedFilteredList.size()) + "    prot number " + proteinsToAdd.size());
        return updatedFilteredList;
    }

    private List<QuantProtein> handelQuantPeptides(List<QuantProtein> fullQuantProtList, Map<String, Integer> peptideKeyToProteinIndexMap, Map<Integer, Integer> protKeyToDsIndexMap) {
//        Map<String, QuantPeptide> peptides = new HashMap<String, QuantPeptide>();
        List<QuantProtein> peptidesList = new ArrayList<QuantProtein>();
//        Set<String>KeyCounter = new HashSet<String>();
//        List<QuantProtein> updatedQuantProtList = new ArrayList<QuantProtein>();
        Map<String, QuantProtein> potentialPeptidesList = new HashMap<String, QuantProtein>();
        Set<String> currentPeptidesList = new HashSet<String>();

        for (QuantProtein peptideProtein : fullQuantProtList) {

            if (!peptideProtein.isPeptideObject()) {
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
            if (peptideProtein.getUniprotAccession().equalsIgnoreCase("") || peptideProtein.getUniprotAccession().equalsIgnoreCase("Not Available") || peptideProtein.getUniprotAccession().equalsIgnoreCase("Entry Deleted") || peptideProtein.getUniprotAccession().equalsIgnoreCase("Entry Demerged") || peptideProtein.getUniprotAccession().equalsIgnoreCase("NOT RETRIEVED") || peptideProtein.getUniprotAccession().equalsIgnoreCase("DELETED") || peptideProtein.getUniprotAccession().equalsIgnoreCase("UNREVIEWED")) {
                peptideProtein.setUniprotAccession(peptideProtein.getPublicationAccNumber());
            }
            currentPeptidesList.add(peptideProtein.getQuantPeptideKey());
            peptidesList.add(peptideProtein);

//Map<String, QuantProtein> newQuantProteinsList = new LinkedHashMap<String, QuantProtein>();
//        for (QuantProtein quantPeptide : astridList) {
//               String key = quantPeptide.getPumedID() + "_" + quantPeptide.getStudyKey() + "_" + quantPeptide.getQuantifiedProteinsNumber() + "_" + quantPeptide.getUniprotAccession() + "_" + quantPeptide.getPublicationAccNumber() + "_" + quantPeptide.getRawDataAvailable() + "_" + quantPeptide.getTypeOfStudy() + "_" + quantPeptide.getSampleType() + "_" + quantPeptide.getPatientsGroupINumber() + "_" + quantPeptide.getPatientsGroupIINumber() + "_" + quantPeptide.getPatientGrIComment() + "_" + quantPeptide.getPatientGrIIComment() + "_" + quantPeptide.getPatientGroupI() + "_" + quantPeptide.getPatientGroupII() + "_" + quantPeptide.getPatientSubGroupI() + "_" + quantPeptide.getPatientSubGroupII() + "_" + quantPeptide.getNormalizationStrategy() + "_" + quantPeptide.getTechnology() + "_" + quantPeptide.getAnalyticalApproach() + "_" + quantPeptide.getAnalyticalMethod() + "_" + quantPeptide.getShotgunOrTargetedQquant() + "_" + quantPeptide.getEnzyme() + "_" + quantPeptide.getQuantificationBasis() + "_" + quantPeptide.getDiseaseCategory();
//            String key2 = quantPeptide.getPumedID() + "_" + quantPeptide.getStudyKey() + "_" + quantPeptide.getQuantifiedProteinsNumber() + "_" + quantPeptide.getUniprotAccession() + "_" + quantPeptide.getPublicationAccNumber() + "_" + quantPeptide.getRawDataAvailable() + "_" + quantPeptide.getTypeOfStudy() + "_" + quantPeptide.getSampleType() + "_" + quantPeptide.getPatientsGroupINumber() + "_" + quantPeptide.getPatientsGroupIINumber() + "_" + quantPeptide.getPatientGrIComment() + "_" + quantPeptide.getPatientGrIIComment() + "_" + quantPeptide.getPatientGroupI() + "_" + quantPeptide.getPatientGroupII() + "_" + quantPeptide.getPatientSubGroupI() + "_" + quantPeptide.getPatientSubGroupII() + "_" + quantPeptide.getNormalizationStrategy() + "_" + quantPeptide.getTechnology() + "_" + quantPeptide.getAnalyticalApproach() + "_" + quantPeptide.getAnalyticalMethod() + "_" + quantPeptide.getShotgunOrTargetedQquant() + "_" + quantPeptide.getEnzyme() + "_" + quantPeptide.getDiseaseCategory();
////            String key2=quantPeptide.getQuantPeptideKey();
////            if(quantPeptide.getDiseaseCategory().equalsIgnoreCase("Alzheimer's"))
////                System.out.println("com.csf.handler.Handler.filterQuantProteins()------- " +key );
//            
//            if (!newQuantProteinsList.containsKey(key2)) {
//                QuantProtein newProtein = new QuantProtein();
//                newProtein.setAdditionalComments(quantPeptide.getAdditionalComments());
//                newProtein.setUniprotProteinName(quantPeptide.getUniprotProteinName());
//                newProtein.setPublicationProteinName(quantPeptide.getPublicationProteinName());
//                newProtein.setAuthor(quantPeptide.getAuthor());
//                newProtein.setIdentifiedProteinsNum(quantPeptide.getIdentifiedProteinsNum());
//                newProtein.setQuantifiedProteinsNumber(quantPeptide.getQuantifiedProteinsNumber());
//                newProtein.setPumedID(quantPeptide.getPumedID());
//                newProtein.setStudyKey(quantPeptide.getStudyKey());
//                newProtein.setQuantifiedPeptidesNumber(quantPeptide.getQuantifiedPeptidesNumber());
//                newProtein.setUniprotAccession(quantPeptide.getUniprotAccession());
//                newProtein.setPublicationAccNumber(quantPeptide.getPublicationAccNumber());
//                newProtein.setRawDataAvailable(quantPeptide.getRawDataAvailable());
//                newProtein.setTypeOfStudy(quantPeptide.getTypeOfStudy());
//                newProtein.setSampleType(quantPeptide.getSampleType());
//                newProtein.setPatientsGroupINumber(quantPeptide.getPatientsGroupINumber());
//                newProtein.setPatientsGroupIINumber(quantPeptide.getPatientsGroupIINumber());
//                newProtein.setPatientGrIComment(quantPeptide.getPatientGrIComment());
//                newProtein.setPatientGrIIComment(quantPeptide.getPatientGrIIComment());
//                newProtein.setPatientGroupI(quantPeptide.getPatientGroupI());
//                newProtein.setPatientGroupII(quantPeptide.getPatientGroupII());
//                newProtein.setPatientSubGroupI(quantPeptide.getPatientSubGroupI());
//                newProtein.setPatientSubGroupII(quantPeptide.getPatientSubGroupII());
//                newProtein.setNormalizationStrategy(quantPeptide.getNormalizationStrategy());
//                newProtein.setTechnology(quantPeptide.getTechnology());
//                newProtein.setAnalyticalApproach(quantPeptide.getAnalyticalApproach());
//                newProtein.setAnalyticalMethod(quantPeptide.getAnalyticalMethod());
//                newProtein.setShotgunOrTargetedQquant(quantPeptide.getShotgunOrTargetedQquant());
//                newProtein.setEnzyme(quantPeptide.getEnzyme());
//                newProtein.setDiseaseCategory(quantPeptide.getDiseaseCategory());
//                newProtein.setPooledSample(quantPeptide.isPooledSample());
//                newProtein.setPeptideProtein(false);
//                newProtein.setPeptideSequance("");
//                newProtein.setSampleMatching(quantPeptide.getSampleMatching());
//                newProtein.setQuantificationBasis(quantPeptide.getQuantificationBasis());
//                newProtein.setQuantBasisComment(" ");
//                newProtein.setpValue(-1000000000.0);
//                newProtein.setStringPValue(" ");
//                newProtein.setPvalueComment(" ");
//                newProtein.setSignificanceThreshold(" ");
//                newProtein.setPeptideSequenceAnnotated(" ");
//                newProtein.setPeptideModification(" ");
//                newProtein.setModificationComment(" ");
//                newProtein.setFcPatientGroupIonPatientGroupII(-1000000000.0);
//                newProtein.setAuthor(quantPeptide.getAuthor());
//                newProtein.setYear(quantPeptide.getYear());
//               
//                newProtein.setStringFCValue("Not Provided");
//                String pepKey = newProtein.getPumedID() + "_" + newProtein.getStudyKey() + "_" + newProtein.getUniprotAccession() + "_" + newProtein.getPublicationAccNumber() + "_" + newProtein.getTypeOfStudy() + "_" + newProtein.getSampleType() + "_" + newProtein.getTechnology() + "_" + newProtein.getAnalyticalApproach() + "_" + newProtein.getAnalyticalMethod() + "_" + newProtein.getPatientsGroupINumber() + "_" + newProtein.getPatientsGroupIINumber() + "_" + newProtein.getPatientSubGroupI() + "_" + newProtein.getPatientSubGroupII() + "_" + newProtein.getDiseaseCategory();
//                newProtein.setQuantPeptideKey(pepKey);
//                
//                if (qDataHandler.getProteinAccSequanceMap().get(newProtein.getUniprotAccession().toLowerCase()) != null) {
//                    newProtein.setSequance(qDataHandler.getProteinAccSequanceMap().get(newProtein.getUniprotAccession().toLowerCase()).getSequance());
//                } else {
//                    newProtein.setSequance(" ");
//                }
//                newQuantProteinsList.put(key2, newProtein);
//               
//            }
//            
//        }
        }

        for (String peptideKey : potentialPeptidesList.keySet()) {
            if (!currentPeptidesList.contains(peptideKey)) {
                QuantProtein quantPeptide = potentialPeptidesList.get(peptideKey);
                quantPeptide.setPeptideObject(true);
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
//        for( QuantPeptide quantPeptide  :peptidesList)
//            ids.add(quantPeptide.getProtKey()+"");
//
//        System.out.println("at fullQuantProtList " + fullQuantProtList.size() + "   -- filtered to " + proteins.size() + "   peptides are " + peptidesList.size()+"  ids size "+ids.size());
//        
//        ready to store in db
        return peptidesList;
    }

    private List<QuantProtein> handleQuantData(Set<QuantDatasetObject> dss, List<QuantProtein> qProtList) {

        List<QuantProtein> updatedQuantProtList = new ArrayList<QuantProtein>();
        for (QuantProtein qp : qProtList) {

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

//                if (!ds.getQuantBasisComment().equalsIgnoreCase(qp.getQuantBasisComment())) {
//                    continue;
//                }
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

    public boolean updateDiseaseGroupsFullName(String diseaseGroupsNamingFilePath) {
        Map<String, String> diseaseGroupsNamingMap = qDataHandler.readDiseaseGroupsFullNameFile(diseaseGroupsNamingFilePath);
        if (diseaseGroupsNamingMap.isEmpty()) {
            return false;
        }

        return dal.updateDiseaseGroupsFullName(diseaseGroupsNamingMap);
    }

    public boolean insertPublication(String pubmedid, String author, String year, String title) {
        return dal.insertPublication(pubmedid, author, year, title);

    }

    public List<Object[]> getPublications() {
        return dal.getPublications();
    }

    public void updateQuantStudies(Set<QuantDatasetObject> datasetsList, List<QuantProtein> qProtList) {
        Map<Integer, Object[]> studyUpdatingMap = new HashMap<Integer, Object[]>();
        Map<Integer, Set<String>> fullStudyAccessionMap = new HashMap<Integer, Set<String>>();
        Map<Integer, Set<String>> fullStudyPeptideMap = new HashMap<Integer, Set<String>>();
        System.out.println("at quant proteins list size " + qProtList.size() + "   ds " + datasetsList.size());
        int i = 0;
        for (QuantProtein qp : qProtList) {
            if (qp.isPeptideObject()) {
                if (!fullStudyPeptideMap.containsKey(qp.getDsKey())) {
                    fullStudyPeptideMap.put(qp.getDsKey(), new HashSet<String>());
                }
                Set<String> peptideSet = fullStudyPeptideMap.get(qp.getDsKey());
                peptideSet.add(qp.getPeptideSequance());
                fullStudyPeptideMap.put(qp.getDsKey(), peptideSet);
            } else {
                if (!fullStudyAccessionMap.containsKey(qp.getDsKey())) {
                    fullStudyAccessionMap.put(qp.getDsKey(), new HashSet<String>());
                }
                Set<String> accSet = fullStudyAccessionMap.get(qp.getDsKey());

                if ((qp.getUniprotAccession().equalsIgnoreCase("") || qp.getUniprotAccession().equalsIgnoreCase("Not Available") || qp.getUniprotAccession().equalsIgnoreCase("Entry Deleted") || qp.getUniprotAccession().equalsIgnoreCase("Entry Demerged") || qp.getUniprotAccession().equalsIgnoreCase("NOT RETRIEVED") || qp.getUniprotAccession().equalsIgnoreCase("DELETED") || qp.getUniprotAccession().equalsIgnoreCase("UNREVIEWED"))) {
                    accSet.add(qp.getPublicationAccNumber().trim());

                } else if (qp.getUniprotAccession() != null && !qp.getUniprotAccession().trim().equalsIgnoreCase("")) {
                    accSet.add(qp.getUniprotAccession().trim());
                } else {
                    accSet.add(qp.getPublicationAccNumber().trim());
                }
//                fullStudyAccessionMap.put(qp.getDsKey(), accSet);
            }

        }

        int counter = 0;
        for (QuantDatasetObject dataset : datasetsList) {
            counter++;
            Object[] puplicationData;// = new Object[5];
            puplicationData = new Object[5];
            puplicationData[0] = dataset.getUniqId();
            if (fullStudyAccessionMap.containsKey(dataset.getUniqId())) {
                puplicationData[1] = fullStudyAccessionMap.get(dataset.getUniqId()).size();
            } else {
                System.out.println("there is error in publication no accession available " + dataset.getPumedID() + "  " + dataset.getUniqId() + "   " + fullStudyAccessionMap.keySet());
                puplicationData[1] = 0;
            }
            int uniqueQunter = 0;
            for (String acc : fullStudyAccessionMap.get(dataset.getUniqId())) {
                boolean unique = true;
                for (int otherDsId : fullStudyAccessionMap.keySet()) {
                    if (otherDsId == dataset.getUniqId()) {
                        continue;
                    }
                    if (fullStudyAccessionMap.get(otherDsId).contains(acc.trim())) {
                        unique = false;
                        break;

                    }

                }
                if (unique) {
                    uniqueQunter++;
                }

            }

            puplicationData[2] = uniqueQunter;
            if (fullStudyPeptideMap.get(dataset.getUniqId()) != null) {
                puplicationData[3] = fullStudyPeptideMap.get(dataset.getUniqId()).size();
                uniqueQunter = 0;
                try {
                    for (String peptide : fullStudyPeptideMap.get(dataset.getUniqId())) {
                        boolean unique = true;
                        for (int otherDsId : fullStudyPeptideMap.keySet()) {
                            if (otherDsId == (dataset.getUniqId())) {
                                continue;
                            }
                            if (fullStudyPeptideMap.get(otherDsId).contains(peptide)) {
                                unique = false;
                                break;

                            }

                        }
                        if (unique) {
                            uniqueQunter++;
                        }

                    }

                    puplicationData[4] = uniqueQunter;
                } catch (Exception e) {
                    e.printStackTrace();
                    puplicationData[3] = 0;
                    puplicationData[4] = 0;
                }
            } else {
                puplicationData[3] = 0;
                puplicationData[4] = 0;

            }
            studyUpdatingMap.put(dataset.getUniqId(), puplicationData);

        }
        dal.updateQuantStudies(studyUpdatingMap);

    }

    private void updateActivePublications(Map<String, Boolean> activePublications, List<QuantProtein> qProtList) {
        Map<String, Object[]> publicationUpdatingMap = new HashMap<String, Object[]>();
        Map<String, Set<String>> fullPublicationAccessionMap = new HashMap<String, Set<String>>();
        Map<String, Set<String>> fullPublicationPeptideMap = new HashMap<String, Set<String>>();
        for (QuantProtein qp : qProtList) {

            if (qp.isPeptideObject()) {
                if (!fullPublicationPeptideMap.containsKey(qp.getPumedID().trim())) {
                    fullPublicationPeptideMap.put(qp.getPumedID().trim(), new HashSet<String>());

                }
                Set<String> peptideSet = fullPublicationPeptideMap.get(qp.getPumedID().trim());

                peptideSet.add(qp.getPeptideSequance());

                fullPublicationPeptideMap.put(qp.getPumedID().trim(), peptideSet);
            } else {
                if (!fullPublicationAccessionMap.containsKey(qp.getPumedID().trim())) {
                    fullPublicationAccessionMap.put(qp.getPumedID().trim(), new HashSet<String>());

                }
                Set<String> accSet = fullPublicationAccessionMap.get(qp.getPumedID().trim());

                if ((qp.getUniprotAccession().equalsIgnoreCase("") || qp.getUniprotAccession().equalsIgnoreCase("Not Available") || qp.getUniprotAccession().equalsIgnoreCase("Entry Deleted") || qp.getUniprotAccession().equalsIgnoreCase("Entry Demerged") || qp.getUniprotAccession().equalsIgnoreCase("NOT RETRIEVED") || qp.getUniprotAccession().equalsIgnoreCase("DELETED") || qp.getUniprotAccession().equalsIgnoreCase("UNREVIEWED"))) {
                    accSet.add(qp.getPublicationAccNumber().trim());

                } else if (qp.getUniprotAccession() != null && !qp.getUniprotAccession().trim().equalsIgnoreCase("")) {
                    accSet.add(qp.getUniprotAccession().trim());
                } else {
                    accSet.add(qp.getPublicationAccNumber().trim());
                }
                fullPublicationAccessionMap.put(qp.getPumedID().trim(), accSet);
            }

        }

        for (String pubKey : activePublications.keySet()) {
            Object[] puplicationData;// = new Object[5];
            if (!activePublications.get(pubKey)) {
                puplicationData = new Object[]{false, 0, 0, 0, 0};

                publicationUpdatingMap.put(pubKey, puplicationData);
                continue;

            }
            puplicationData = new Object[5];
            puplicationData[0] = true;
            puplicationData[1] = fullPublicationAccessionMap.get(pubKey).size();
            int uniqueQunter = 0;
            for (String acc : fullPublicationAccessionMap.get(pubKey)) {
                boolean unique = true;
                for (String otherPubId : fullPublicationAccessionMap.keySet()) {
                    if (otherPubId.equalsIgnoreCase(pubKey)) {
                        continue;
                    }
                    if (fullPublicationAccessionMap.get(otherPubId).contains(acc.trim())) {
                        unique = false;
                        break;

                    }

                }
                if (unique) {
                    uniqueQunter++;
                }

            }
            puplicationData[2] = uniqueQunter;
            if (fullPublicationPeptideMap.get(pubKey) != null) {
                puplicationData[3] = fullPublicationPeptideMap.get(pubKey).size();

                uniqueQunter = 0;

                for (String peptide : fullPublicationPeptideMap.get(pubKey)) {
                    boolean unique = true;
                    for (String otherPubId : fullPublicationPeptideMap.keySet()) {
                        if (otherPubId.equalsIgnoreCase(pubKey)) {
                            continue;
                        }
                        if (fullPublicationPeptideMap.get(otherPubId).contains(peptide)) {
                            unique = false;
                            break;

                        }

                    }
                    if (unique) {
                        uniqueQunter++;
                    }

                }

                puplicationData[4] = uniqueQunter;
            } else {
                puplicationData[3] = 0;
                puplicationData[4] = 0;

            }
            publicationUpdatingMap.put(pubKey, puplicationData);

        }
        System.out.println("ready to update active publications " + publicationUpdatingMap.keySet());
        dal.updateActivePublications(publicationUpdatingMap);

    }

}
