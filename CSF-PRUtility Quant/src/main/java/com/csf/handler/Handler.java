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
import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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

//        this.updateActivePublications(qDataHandler.getActivePublications(), qProtList);
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
//        //store quant protiens
        Object[] maps = dal.storeQuantitiveProteins(updatedQuantProtList);
        System.out.println("done with protein table");
        Map<String, Integer> peptideKeyToProteinIndexMap = (Map<String, Integer>) maps[0];
        Map<Integer, Integer> protKeyToDsIndexMap = (Map<Integer, Integer>) maps[1];

        List<QuantProtein> peptidesList = handelQuantPeptides(filteredQuantProteinsList, peptideKeyToProteinIndexMap, protKeyToDsIndexMap);
        //store quant peptides
        dal.storeQuantitivePeptides(peptidesList);
        System.out.println("done with peptides table");
        this.updateQuantStudies(datasetsList, qProtList);
        System.out.println("update poublication");
          this.updateActivePublications(qDataHandler.getActivePublications(), qProtList);

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
////        filteredQuantProteinsList = this.handleQuantData(datasetsList,filteredQuantProteinsList);
////        int protIndex = dal.getCurrentProtIndex();
////        List<QuantPeptide> peptidesList = handelQuantPeptides(filteredQuantProteinsList,protIndex);        
////        
////        //foldchange
////        
////        defineProtFoldChange(filteredQuantProteinsList, peptidesList);
////        
////        
////        //store quant protiens
////        dal.storeQuantitiveProteins(filteredQuantProteinsList);
////        
////        //store quant peptides
////        dal.storeQuantitivePeptides(peptidesList);
////        
////        System.out.println("final peptideProtein list updated (should be smaller)  " + filteredQuantProteinsList.size() + "  prer " + peptidesList.size());
//        return true;
//    }
    private List<QuantProtein> filterQuantProteins(List<QuantProtein> qProtList) {
        Map<String, QuantProtein> updatedFilteredProteinsList = new HashMap<String, QuantProtein>();
        Map<String, QuantProtein> updatedFullFilteredProteinsList = new HashMap<String, QuantProtein>();
        List<QuantProtein> updatedFilteredList = new ArrayList<QuantProtein>();
        Set<String> cleaningSet = new HashSet<String>();
        List<QuantProtein> astridList = new ArrayList<QuantProtein>();
        for (QuantProtein quantProt : qProtList) {
            if (quantProt.isPeptideProtein()) {
                continue;
            }
            String key = quantProt.getPumedID() + "_" + quantProt.getStudyKey() + "_" + quantProt.getQuantifiedProteinsNumber() + "_" + quantProt.getUniprotAccession() + "_" + quantProt.getPublicationAccNumber() + "_" +  quantProt.getRawDataAvailable() + "_" + quantProt.getTypeOfStudy() + "_" + quantProt.getSampleType() + "_" + quantProt.getPatientsGroupINumber() + "_" + quantProt.getPatientsGroupIINumber() + "_" + quantProt.getPatientGrIComment() + "_" + quantProt.getPatientGrIIComment() + "_" + quantProt.getPatientGroupI() + "_" + quantProt.getPatientGroupII() + "_" + quantProt.getPatientSubGroupI() + "_" + quantProt.getPatientSubGroupII() + "_" + quantProt.getNormalizationStrategy() + "_" + quantProt.getTechnology() + "_" + quantProt.getAnalyticalApproach() + "_" + quantProt.getAnalyticalMethod() + "_" + quantProt.getShotgunOrTargetedQquant() + "_" + quantProt.getEnzyme() + "_" + quantProt.getQuantificationBasis() + "_" + quantProt.getDiseaseCategory();
            String key2 = quantProt.getPumedID() + "_" + quantProt.getStudyKey() + "_" + quantProt.getQuantifiedProteinsNumber() + "_" + quantProt.getUniprotAccession() + "_" + quantProt.getPublicationAccNumber()  + "_" + quantProt.getRawDataAvailable() + "_" + quantProt.getTypeOfStudy() + "_" + quantProt.getSampleType() + "_" + quantProt.getPatientsGroupINumber() + "_" + quantProt.getPatientsGroupIINumber() + "_" + quantProt.getPatientGrIComment() + "_" + quantProt.getPatientGrIIComment() + "_" + quantProt.getPatientGroupI() + "_" + quantProt.getPatientGroupII() + "_" + quantProt.getPatientSubGroupI() + "_" + quantProt.getPatientSubGroupII() + "_" + quantProt.getNormalizationStrategy() + "_" + quantProt.getTechnology() + "_" + quantProt.getAnalyticalApproach() + "_" + quantProt.getAnalyticalMethod() + "_" + quantProt.getShotgunOrTargetedQquant() + "_" + quantProt.getEnzyme() + "_" + quantProt.getDiseaseCategory();

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

        for (QuantProtein quantProt : qProtList) {
            if (quantProt.isPeptideProtein()) {
                String key = quantProt.getPumedID() + "_" + quantProt.getStudyKey() + "_" + quantProt.getQuantifiedProteinsNumber() + "_" + quantProt.getUniprotAccession() + "_" + quantProt.getPublicationAccNumber() + "_" + quantProt.getRawDataAvailable() + "_" + quantProt.getTypeOfStudy() + "_" + quantProt.getSampleType() + "_" + quantProt.getPatientsGroupINumber() + "_" + quantProt.getPatientsGroupIINumber() + "_" + quantProt.getPatientGrIComment() + "_" + quantProt.getPatientGrIIComment() + "_" + quantProt.getPatientGroupI() + "_" + quantProt.getPatientGroupII() + "_" + quantProt.getPatientSubGroupI() + "_" + quantProt.getPatientSubGroupII() + "_" + quantProt.getNormalizationStrategy() + "_" + quantProt.getTechnology() + "_" + quantProt.getAnalyticalApproach() + "_" + quantProt.getAnalyticalMethod() + "_" + quantProt.getShotgunOrTargetedQquant() + "_" + quantProt.getEnzyme() + "_" + quantProt.getDiseaseCategory();
                if (updatedFullFilteredProteinsList.containsKey(key)) {
                    updatedFilteredList.add(quantProt);

                } else {
                    System.out.println("astrid file --- >>  " + key);
                    astridList.add(quantProt);

                }
            }
        }
        updatedFilteredList.addAll(updatedFilteredProteinsList.values());
        System.out.println("the diffrent in size is " + (qProtList.size() - updatedFilteredList.size()));
        File xlsText = new File("D:\\", "CSF-PR-V2 - Ignored Proteins List.xls");// "CSF-PR - " + datasetName + " - All - " + dataType + "- Peptides" + ".csv");

        try {
            xlsText.createNewFile();

            FileOutputStream fileOut = new FileOutputStream(xlsText);
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet();
            //title
            String title = "CSF-PR-V2 - Ignored Proteins List";
            HSSFRow titleRow = worksheet.createRow(0);
            HSSFCell cellA1 = titleRow.createCell(0);
            cellA1.setCellValue(title);
            //header
            String header = "index, PumedID,Study key,# Quantified proteins,acc nu. uniprot/Nextprot,Protein name from uniprot,acc number from publication,Protein name from publication,Protein or Peptide";
            String[] headerArr = header.split(",");
            HSSFRow headerRow = worksheet.createRow(1);
            int index = 0;
            for (String str : headerArr) {
                HSSFCell cell = headerRow.createCell(index);
                cell.setCellValue(str);
                index++;

            }
            //data

            index = 0;
            int counter = 0;
            for (QuantProtein quantProt : astridList) {
                if (index > 65533) {
                    worksheet = workbook.createSheet();
                    index = 0;
                    HSSFRow headerRow2 = worksheet.createRow(0);
                    for (String str : headerArr) {
                        HSSFCell cell = headerRow2.createCell(index);
                        cell.setCellValue(str);
                        index++;
                    }
                    index = -1;
                }
                HSSFRow peptideRow = worksheet.createRow((int) index + 2);
                HSSFCell cell0 = peptideRow.createCell(counter++);
                cell0.setCellValue(index);
                HSSFCell cell1 = peptideRow.createCell(1);
                cell1.setCellValue(quantProt.getPumedID());
                HSSFCell cell2 = peptideRow.createCell(2);
                cell2.setCellValue(quantProt.getStudyKey());
                HSSFCell cell3 = peptideRow.createCell(3);
                cell3.setCellValue(quantProt.getQuantifiedProteinsNumber());
                HSSFCell cell4 = peptideRow.createCell(4);
                cell4.setCellValue(quantProt.getUniprotAccession());
                HSSFCell cell5 = peptideRow.createCell(5);
                cell5.setCellValue(quantProt.getUniprotProteinName());
                HSSFCell cell6 = peptideRow.createCell(6);
                cell6.setCellValue(quantProt.getPublicationAccNumber());
                HSSFCell cell7 = peptideRow.createCell(7);
                cell7.setCellValue(quantProt.getPublicationProteinName());
                HSSFCell cell8 = peptideRow.createCell(8);
                cell8.setCellValue(quantProt.isPeptideProtein());
//                HSSFCell cell9 = peptideRow.createCell(9);
//                cell9.setCellValue(pb.getNumberOfValidatedSpectra());
//                HSSFCell cell10 = peptideRow.createCell(10);
//                cell10.setCellValue(pb.getOtherProteins().replaceAll(",", ";"));
//                HSSFCell cell11 = peptideRow.createCell(11);
//                cell11.setCellValue(pb.getOtherProteinDescriptions().replaceAll(",", ";"));
//                HSSFCell cell12 = peptideRow.createCell(12);
//                cell12.setCellValue(pb.getVariableModification().replaceAll(",", ";"));
//                HSSFCell cell13 = peptideRow.createCell(13);
//                cell13.setCellValue(pb.getLocationConfidence().replaceAll(",", ";"));
//                HSSFCell cell14 = peptideRow.createCell(14);
//                cell14.setCellValue(pb.getPrecursorCharges().replaceAll(",", ";"));
//                HSSFCell cell15 = peptideRow.createCell(15);
//                cell15.setCellValue(pb.isEnzymatic());
//                HSSFCell cell16 = peptideRow.createCell(16);
//                cell16.setCellValue(pb.getSequenceTagged());
//                HSSFCell cell17 = peptideRow.createCell(17);
//                cell17.setCellValue(pb.isDeamidationAndGlycopattern());
//                HSSFCell cell18 = peptideRow.createCell(18);
//                if (pb.getGlycopatternPositions() != null) {
//                    cell18.setCellValue(pb.getGlycopatternPositions());
//                } else {
//                    cell18.setCellValue("");
//                }
//                int x = (int) pb.getConfidence();
//                HSSFCell cell19 = peptideRow.createCell(19);
//                cell19.setCellValue(x);
//
//                HSSFCell cell20 = peptideRow.createCell(20);
//
//                if (pb.getValidated() == 1.0) {
//                    cell20.setCellValue("TRUE");
//                } else {
//                    cell20.setCellValue("FALSE");
//                }
                index++;

            }

            workbook.write(fileOut);
//            fileOut.flush();
            fileOut.close();
        } catch (Exception exp) {
            exp.printStackTrace();
            System.err.println(exp.getMessage());
        }

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
            if (peptideProtein.getUniprotAccession().equalsIgnoreCase("") || peptideProtein.getUniprotAccession().equalsIgnoreCase("Not Available") || peptideProtein.getUniprotAccession().equalsIgnoreCase("Entry Deleted") || peptideProtein.getUniprotAccession().equalsIgnoreCase("Entry Demerged") || peptideProtein.getUniprotAccession().equalsIgnoreCase("NOT RETRIEVED") || peptideProtein.getUniprotAccession().equalsIgnoreCase("DELETED") || peptideProtein.getUniprotAccession().equalsIgnoreCase("UNREVIEWED")) {
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
//            String peptide = peptideProtein.getUniprotAccession();
//            if (peptide.equalsIgnoreCase("Not Available")) {
//                peptide = peptideProtein.getPublicationAccNumber();
//            }
//            if (!proteins.containsKey(peptideProtein.getDsKey() + "-" + peptide)) {
//                pep.setProtKey(protIndex);
//                peptideProtein.setProtKey(protIndex);
//                proteins.put(peptideProtein.getDsKey() + "-" + peptide, peptideProtein);
//                protIndex++;
//
//            } else {
//                pep.setProtKey(proteins.get(peptideProtein.getDsKey() + "-" + peptide).getProtKey());
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
        for (QuantProtein qp : qProtList) {

            if (qp.isPeptideProtein()) {
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
                fullStudyAccessionMap.put(qp.getDsKey(), accSet);
            }

        }

        for (QuantDatasetObject dataset : datasetsList) {
            Object[] puplicationData;// = new Object[5];

            puplicationData = new Object[5];
            puplicationData[0] = dataset.getUniqId();
            puplicationData[1] = fullStudyAccessionMap.get(dataset.getUniqId()).size();
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

            if (qp.isPeptideProtein()) {
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

        dal.updateActivePublications(publicationUpdatingMap);

    }

}
