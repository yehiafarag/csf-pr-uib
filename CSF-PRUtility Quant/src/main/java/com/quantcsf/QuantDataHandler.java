package com.quantcsf;

import com.quantcsf.beans.QuantDatasetObject;
import com.quantcsf.beans.QuantProtein;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Yehia Farag
 */
public class QuantDataHandler {

    private final Map<String, QuantDatasetObject> authormap = new HashMap<String, QuantDatasetObject>();

    public QuantDataHandler() {

        QuantDatasetObject qa7 = new QuantDatasetObject();
        qa7.setYear(2011);
        qa7.setFilesNumber(10);
        qa7.setAnalyticalMethod("Disease Group I");
        qa7.setAuthor("Li, Yun, et al.");
        authormap.put("21445879", qa7);

        QuantDatasetObject qa8 = new QuantDatasetObject();
        qa8.setYear(2014);
        qa8.setFilesNumber(10);
        qa8.setAnalyticalMethod("Disease Group I");
        qa8.setAuthor("Liguori, Maria, et al.");
        authormap.put("25098164", qa8);

        QuantDatasetObject qa9 = new QuantDatasetObject();
        qa9.setYear(2010);
        qa9.setFilesNumber(10);
        qa9.setAnalyticalMethod("Disease Group I");
        qa9.setAuthor("Ottervald, Jan, et al");
        authormap.put("20093204", qa9);

        QuantDatasetObject qa1 = new QuantDatasetObject();
        qa1.setYear(2010);
        qa1.setFilesNumber(10);
        qa1.setAnalyticalMethod("Disease Group I");
        qa1.setAuthor("Comabella, Manuel, et al.");
        authormap.put("20237129", qa1);

        QuantDatasetObject qa2 = new QuantDatasetObject();
        qa2.setYear(2010);
        qa2.setAuthor("Harris, Violaine K., et al.");
        qa2.setFilesNumber(15);
        qa2.setAnalyticalMethod("Disease Group II");
        authormap.put("20600910", qa2);

        QuantDatasetObject qa3 = new QuantDatasetObject();
        qa3.setYear(2012);
        qa3.setFilesNumber(13);
        qa3.setAnalyticalMethod("Disease Group II");
        qa3.setAuthor("Dhaunchak, Ajit Singh, et al.");
        authormap.put("22473675", qa3);

        QuantDatasetObject qa4 = new QuantDatasetObject();
        qa4.setYear(2012);
        qa4.setFilesNumber(14);
        qa4.setAnalyticalMethod("Disease Group I");
        qa4.setAuthor("Jia, Yan et al.");
        authormap.put("22846148", qa4);

        QuantDatasetObject qa5 = new QuantDatasetObject();
        qa5.setYear(2013);
        qa5.setFilesNumber(5);
        qa5.setAnalyticalMethod("Disease Group III");
        qa5.setAuthor("Kroksveen, Ann C., et al.");
        authormap.put("23059536", qa5);

        QuantDatasetObject qa6 = new QuantDatasetObject();
        qa6.setYear(2012);
        qa6.setFilesNumber(16);
        qa6.setAuthor("Kroksveen, Ann C., et al.");
        qa6.setAnalyticalMethod("Disease Group II");
        authormap.put("23278663", qa6);

        QuantDatasetObject qa11 = new QuantDatasetObject();
        qa11.setYear(2014);
        qa11.setFilesNumber(10);
        qa11.setAnalyticalMethod("Disease Group I");
        qa11.setAuthor("Cantó, Ester, et al.");
        authormap.put("25406498", qa11);

        QuantDatasetObject qa12 = new QuantDatasetObject();
        qa12.setYear(2015);
        qa12.setFilesNumber(10);
        qa12.setAnalyticalMethod("Disease Group I");
        qa12.setAuthor("Hinsinger, G., et al.");
        authormap.put("25698171", qa12);

        QuantDatasetObject qa13 = new QuantDatasetObject();
        qa13.setYear(2013);
        qa13.setFilesNumber(10);
        qa13.setAnalyticalMethod("Disease Group I");
        qa13.setAuthor("Stoop, Marcel P., et al");
        authormap.put("23339689", qa13);

        QuantDatasetObject qa10 = new QuantDatasetObject();
        qa10.setYear(2012);
        qa10.setFilesNumber(10);
        qa10.setAnalyticalMethod("Disease Group I");
        qa10.setAuthor("Kroksveen, A. C., et al.");
        authormap.put("23278663", qa10);

        QuantDatasetObject qa14 = new QuantDatasetObject();
        qa14.setYear(2015);
        qa14.setFilesNumber(10);
        qa14.setAnalyticalMethod("Disease Group I");
        qa14.setAuthor("Kroksveen, Ann C., et al.");
        authormap.put("26152395", qa14);
        
        
        QuantDatasetObject qa15 = new QuantDatasetObject();
        qa15.setYear(2015);
        qa15.setFilesNumber(10);
        qa15.setAnalyticalMethod("Disease Group I");
        qa15.setAuthor("Astrid et al.");
        authormap.put("99999999", qa15);
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public List<QuantProtein> readCSVQuantFile(String path, String sequanceFilePath) {

        File sequanceFile = new File(sequanceFilePath);

        File dataFile = new File(path);
        List<QuantProtein> QuantProtList = new ArrayList<QuantProtein>();

        try {

            FileReader sequanceReader = new FileReader(sequanceFile);
            BufferedReader sequanceBufRdr = new BufferedReader(sequanceReader);
            sequanceBufRdr.readLine();
            Map<String, String> proteinAccSequanceMap = new HashMap<String, String>();
             Map<String, String> proteinAccNameMap = new HashMap<String, String>();
            String seqline;
            int row = 1;
            while ((seqline = sequanceBufRdr.readLine()) != null && row < 1000000000) {
                String[] seqArr = seqline.split("\t");
                if (proteinAccSequanceMap.containsKey(seqArr[0].replace(" ", "").toLowerCase())) {
                    continue;
                }
                if (seqArr.length == 3) {
                    proteinAccSequanceMap.put(seqArr[0].replace(" ", "").toLowerCase(), seqArr[2]);
                } else {
                    proteinAccSequanceMap.put(seqArr[0].replace(" ", "").toLowerCase(), " ");
                }
            }

            sequanceBufRdr.close();

            FileReader fr = new FileReader(dataFile);
            BufferedReader bufRdr = new BufferedReader(fr);
            String header = bufRdr.readLine();
            int i = 0;
            System.out.println("------------------------------");
            String[] headerArr = header.split(";");
            for (String col : headerArr) {
                System.out.println(i + " -- " + col);
                i++;
            }
            System.out.println("------------------------------");

            int index = 1;
            row = 1;
            String line;
            while ((line = bufRdr.readLine()) != null && row < 1000000000) {

                index = 0;
                QuantProtein qProt = new QuantProtein();
                String[] rowArr = line.split(";");

//
                String[] updatedRowArr = new String[headerArr.length];
                if (rowArr.length < headerArr.length) {
                    System.arraycopy(rowArr, 0, updatedRowArr, 0, rowArr.length);
                } else {
                    updatedRowArr = rowArr;
                }
                qProt.setPumedID(updatedRowArr[index++].trim().toUpperCase());
                qProt.setStudyKey(updatedRowArr[index++].trim().toUpperCase());
                if (!authormap.containsKey(qProt.getPumedID())) {
                    System.out.println("at error " + qProt.getPumedID());
                }
                qProt.setAuthor(authormap.get(qProt.getPumedID()).getAuthor());
                qProt.setYear(authormap.get(qProt.getPumedID()).getYear());
                qProt.setFilesNum(authormap.get(qProt.getPumedID()).getFilesNumber());

                if (!updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setQuantifiedProteinsNumber(Integer.valueOf(updatedRowArr[index]));
                } else {
                    qProt.setQuantifiedProteinsNumber(-1);
                }
                index++;
//
                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {// || updatedRowArr[index].trim().equalsIgnoreCase("Not retrieved")|| updatedRowArr[index].trim().equalsIgnoreCase("ENTRY DELETED")|| updatedRowArr[index].trim().equalsIgnoreCase("Entry Demerged")  ) {
                    qProt.setUniprotAccession(" ");
                    index++;
                } else {
                    qProt.setUniprotAccession(updatedRowArr[index++]);
                }
                if (proteinAccSequanceMap.containsKey(qProt.getUniprotAccession().toLowerCase())) {
                    qProt.setSequance(proteinAccSequanceMap.get(qProt.getUniprotAccession().toLowerCase()));

                } else {
                    qProt.setSequance(" ");
                }
                //
                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setUniprotProteinName(" ");
                    index++;
                } else {
                    qProt.setUniprotProteinName(updatedRowArr[index++]);
                }
//
                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setPublicationAccNumber(" ");
                    index++;
                } else {
                    qProt.setPublicationAccNumber(updatedRowArr[index++]);
                }
//
                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setPublicationProteinName(" ");
                    index++;
                } else {
                    qProt.setPublicationProteinName(updatedRowArr[index++]);
                }
                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("Peptide")) {
                    qProt.setPeptideProtein(true);

                } else {
                    qProt.setPeptideProtein(false);
                }
                index++;

                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setRawDataAvailable("Raw Data Not Available");

                } else {
                    if (updatedRowArr[index].equalsIgnoreCase("Yes")) {
                        qProt.setRawDataAvailable("Raw Data Available");
                    } else {
                        qProt.setRawDataAvailable("Raw Data Not Available");
                    }

                }
                index++;

                if (!updatedRowArr[index].trim().equalsIgnoreCase("")) {

                    try {
                        qProt.setPeptideIdNumb(Integer.valueOf(updatedRowArr[index]));
                    } catch (NumberFormatException exp) {
                        System.out.println("row : " + row);
                        exp.printStackTrace();
                        System.out.println("line is " + line);
                        System.out.println("------------------------------              -----------------------------------------            ------------------------------------------");
                        qProt.setPeptideIdNumb(-1);
                    }
                } else {
                    qProt.setPeptideIdNumb(-1);
                }
                index++;
                if (!updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setQuantifiedPeptidesNumber(Integer.valueOf(updatedRowArr[index++]));
                } else {
                    qProt.setQuantifiedPeptidesNumber(-1);
                    index++;
                }
                //fill peptides 
                if (updatedRowArr[index] != null && !updatedRowArr[index].trim().equalsIgnoreCase("")) { //peptide sequance 
                    qProt.setPeptideCharge(Integer.valueOf(updatedRowArr[index]));
                } else {
                    qProt.setPeptideCharge(-1);
                }
                index++;
                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setPeptideSequance(" ");
                    index++;
                } else {
                    qProt.setPeptideSequance(updatedRowArr[index++]);
                }
                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setPeptideSequenceAnnotated(" ");
                    index++;
                } else {
                    qProt.setPeptideSequenceAnnotated(updatedRowArr[index++]);
                }
//
                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setPeptideModification(" ");
                    index++;
                } else {
                    qProt.setPeptideModification(updatedRowArr[index++]);
                }
//
                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setModificationComment(" ");
                    index++;
                } else {
                    qProt.setModificationComment(updatedRowArr[index++]);
                }
//
                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setTypeOfStudy(" ");
                    index++;
                } else {
                    qProt.setTypeOfStudy(updatedRowArr[index++]);
                }
//
                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setSampleType(" ");
                    index++;
                } else {
                    qProt.setSampleType(updatedRowArr[index++]);
                }
//
                if (!updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    try {
                        qProt.setPatientsGroupINumber(Integer.valueOf(updatedRowArr[index++]));
                    } catch (NumberFormatException exp) {
                        System.out.println("---->> " + line);
                        for (String str : line.split(";")) {
                            System.out.println("--- --- " + str);
                        }
                        exp.printStackTrace();
                    }
                } else {
                    qProt.setPatientsGroupINumber(-1);
                    index++;
                }

                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setPatientGroupI(" ");
                    index++;
                } else {
                    qProt.setPatientGroupI(updatedRowArr[index++]);
                }
//
                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setPatientSubGroupI(" ");
                    index++;
                } else {
                    qProt.setPatientSubGroupI(updatedRowArr[index++]);
                }

                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setPatientGrIComment(" ");
                    index++;
                } else {
                    qProt.setPatientGrIComment(updatedRowArr[index++]);
                }

                if (!updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setPatientsGroupIINumber(Integer.valueOf(updatedRowArr[index++]));
                } else {
                    qProt.setPatientsGroupIINumber(-1);
                    index++;
                }
                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setPatientGroupII(" ");
                    index++;
                } else {
                    qProt.setPatientGroupII(updatedRowArr[index++].replace("Controls", "CONTROL"));
                }

                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setPatientSubGroupII(" ");
                    index++;
                } else {
                    qProt.setPatientSubGroupII(updatedRowArr[index++]);
                }

                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setPatientGrIIComment(" ");
                    index++;
                } else {
                    qProt.setPatientGrIIComment(updatedRowArr[index++]);
                }
//
                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setSampleMatching(" ");
                    index++;
                } else {
                    qProt.setSampleMatching(updatedRowArr[index++]);
                }

                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setNormalizationStrategy(" ");
                    index++;
                } else {
                    qProt.setNormalizationStrategy(updatedRowArr[index++]);
                }

                if (!updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    try {
                        qProt.setFcPatientGroupIonPatientGroupII(Double.valueOf(updatedRowArr[index].replace(",", ".")));
//                        if (qProt.getFcPatientGroupIonPatientGroupII() > 0) {
//                            qProt.setStringFCValue("Increased");
//                        } else {
//                            qProt.setStringFCValue("Decreased");
//                        }
                    } catch (NumberFormatException exp) {

                        qProt.setFcPatientGroupIonPatientGroupII(-1000000000.0);
                        qProt.setStringFCValue(updatedRowArr[index]);
//                        if (!updatedRowArr[index].trim().isEmpty()) {
//                            qProt.setStringFCValue(updatedRowArr[index].trim());
//                        } else {
//                            qProt.setStringFCValue("Not Provided");
//                        }
                    }
                } else {
                    qProt.setFcPatientGroupIonPatientGroupII(-1000000000.0);
//                    qProt.setStringFCValue(updatedRowArr[index++]);
                }
                index++;

                if (!updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    try {
                        qProt.setLogFC(Double.valueOf(updatedRowArr[index].replace(",", ".")));
                        if (qProt.getLogFC() > 0) {
                            qProt.setStringFCValue("Increased");
                        } else {
                            qProt.setStringFCValue("Decreased");
                        }
                    } catch (NumberFormatException exp) {
                        qProt.setLogFC(-1000000000.0);
                        if (!updatedRowArr[index].trim().isEmpty()) {
                            qProt.setStringFCValue(updatedRowArr[index].trim());

                        } else {
                            qProt.setStringFCValue("Not Regulated");
                        }
                    } finally {
                        index++;
                    }
                } else {
                    qProt.setLogFC(-1000000000.0);
                    qProt.setStringFCValue("Not Regulated");
                    index++;

                }
                //pvalue
                if (!updatedRowArr[index].trim().trim().equalsIgnoreCase("")) {
                    qProt = definePValue(qProt, updatedRowArr[index++], updatedRowArr[index++], updatedRowArr[index++]);

                } else {
                    qProt.setpValue(-1000000000.0);
                    qProt.setStringPValue(" ");
                    qProt.setPvalueComment(" ");
                    qProt.setSignificanceThreshold(" ");
                    index = index + 3;
                }

                if (!updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setRocAuc(Double.valueOf(updatedRowArr[index].replace(",", ".")));
                    index++;
                } else {
                    qProt.setRocAuc(-1000000000.0);
                    index++;
                }

                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setTechnology(" ");
                    index++;
                } else {
                    qProt.setTechnology(updatedRowArr[index++]);
                }
                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setAnalyticalMethod(" ");
                    index++;
                } else {
                    qProt.setAnalyticalMethod(updatedRowArr[index++]);
                }

                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setAnalyticalApproach(" ");
                    index++;
                } else {
                    qProt.setAnalyticalApproach(updatedRowArr[index++]);
                }
                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setShotgunOrTargetedQquant(" ");
                    index++;
                } else {
                    qProt.setShotgunOrTargetedQquant(updatedRowArr[index++]);
                }

                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setEnzyme(" ");
                    index++;
                } else {
                    qProt.setEnzyme(updatedRowArr[index++]);
                }

                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setQuantificationBasis(" ");
                    index++;
                } else {
                    qProt.setQuantificationBasis(updatedRowArr[index++]);
                }
                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setQuantBasisComment(" ");
                    index++;
                } else {
                    qProt.setQuantBasisComment(updatedRowArr[index++]);
                }
                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setAdditionalComments(" ");
                    index++;
                } else {
                    qProt.setAdditionalComments(updatedRowArr[index++]);
                }

//                if (qProt.isPeptideProtein()) {
               
//                } else {
//                    qProt.setqPeptideKey("");
//
//                }

                if ((!qProt.getPatientGroupI().equalsIgnoreCase(" ")) && qProt.getPatientSubGroupI().equalsIgnoreCase(" ")) {
                    qProt.setPatientGroupI(qProt.getPatientGroupI() + " (Undefined)");

                }
                if ((!qProt.getPatientGroupII().equalsIgnoreCase(" ")) && qProt.getPatientSubGroupII().equalsIgnoreCase(" ")) {
                    qProt.setPatientGroupII(qProt.getPatientGroupII() + " (Undefined)");
                }
                qProt.setIdentifiedProteinsNum(-1); 
                
                String pepKey = qProt.getPumedID()+ "_" + qProt.getStudyKey()+ "_" + qProt.getUniprotAccession() + "_" + qProt.getPublicationAccNumber() + "_" + qProt.getTypeOfStudy() + "_" + qProt.getSampleType()+ "_" + qProt.getTechnology()+ "_" + qProt.getAnalyticalApproach()+ "_" + qProt.getAnalyticalMethod()+ "_" + qProt.getPatientsGroupINumber()+ "_" + qProt.getPatientsGroupIINumber()+"_"+qProt.getPatientSubGroupI()+"_"+qProt.getPatientSubGroupII();
                qProt.setQuantPeptideKey(pepKey);
                QuantProtList.add(qProt);
                row++;
            } //    
            bufRdr.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("ssleeping error " + ex.getMessage());
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            System.out.println("error in " + ex);
        }

        return QuantProtList;

    }

//    private QuantProtein defineFoldChange(QuantProtein prot, String pValue) {
//        try {
//            if (pValue.contains(">")) {
//                prot.setStringPValue("Not Significant");
//                prot.setPvalueComment("Threshold defined at " + pValue.trim().substring(1));
//                prot.setpValue(-1000000000.0);
//
//            } else if (pValue.contains("<")) {
//                prot.setStringPValue("Significant");
//                prot.setPvalueComment("Threshold defined at " + pValue.trim().substring(1));
//                prot.setpValue(-1000000000.0);
//            } else if (Double.valueOf(pValue) < 0.05) {
//                prot.setStringPValue("Significant");
//                prot.setPvalueComment("Threshold defined by CSF-Pr at 0.05");
//                prot.setpValue(Double.valueOf(pValue));
//            } else if (Double.valueOf(pValue) > 0.05) {
//                prot.setStringPValue("Not Significant");
//                prot.setPvalueComment("Threshold defined by CSF-Pr at 0.05");
//                prot.setpValue(Double.valueOf(pValue));
//            }
//
//        } catch (NumberFormatException exp) {
//            prot.setpValue(-1000000000.0);
//            prot.setStringPValue("Not Available");
//            prot.setPvalueComment("Not Available");
//
//        }
////        System.out.println("values  "+prot.getpValue()+"  "+prot.getStringPValue()+"  "+prot.getPvalueComment());
//
//        return prot;
//    }
    private QuantProtein definePValue(QuantProtein prot, String pValue, String significanceThreshold, String pvalueComment) {
        try {
            pValue = pValue.replace(",", ".");
            String operator;

            double signThresholdValue = -1;
            if (significanceThreshold == null || significanceThreshold.trim().equalsIgnoreCase("")) {
                if (pValue.contains(">")) {
                    significanceThreshold = pValue.replace(">", "<");
                    operator = "<";
                    signThresholdValue= Double.valueOf(significanceThreshold.replace("<", "").replace("ÿ", "").replace("Ê", "").replace(" ", "").trim());
                } else if (pValue.contains("<")) {
                    significanceThreshold = pValue;
                    operator = "<";                    
                    signThresholdValue= Double.valueOf(significanceThreshold.replace("<", "").replace("ÿ", "").replace("Ê", "").replace(" ", "").trim());

                } else {
                    significanceThreshold = "defined by CSF-Pr at 0.05";
                    signThresholdValue = 0.05;
                    operator = "<";
                }

            } else if (significanceThreshold.contains("<=")) {
                significanceThreshold = significanceThreshold.replace(",", ".");
                signThresholdValue = Double.valueOf(significanceThreshold.replace("<=", ",").replace("ÿ", "").replace("Ê", "").split(",")[1].replace(" ", "").trim());
                operator = "<=";
            } else if (significanceThreshold.contains("<")) {
                significanceThreshold = significanceThreshold.replace(",", ".");
                signThresholdValue = Double.valueOf(significanceThreshold.split("<")[1].trim());
                operator = "<";
            } else {
                significanceThreshold = significanceThreshold.replace(",", ".");
                signThresholdValue = Double.valueOf(significanceThreshold.trim());
                operator = "<";

            }
            
            
            

            if (pValue.contains(">")) {
                prot.setStringPValue("Not Significant");
                prot.setSignificanceThreshold(significanceThreshold);
                prot.setpValue(-1000000000.0);
            } else if (pValue.contains("<")) {
                prot.setStringPValue("Significant");
                prot.setSignificanceThreshold(significanceThreshold);
                prot.setpValue(-1000000000.0);
            } else if (pValue.trim().equalsIgnoreCase("Significant")) {
                prot.setStringPValue("Significant");
                prot.setSignificanceThreshold(significanceThreshold);
                prot.setpValue(-1000000000.0);

            } else if (pValue.trim().equalsIgnoreCase("Not Significant")) {
                prot.setStringPValue("Not Significant");
                prot.setSignificanceThreshold(significanceThreshold);
                prot.setpValue(-1000000000.0);

            } else if (operator.equalsIgnoreCase("<=")) {
                if (Double.valueOf(pValue.trim()) <= signThresholdValue) {
                    prot.setStringPValue("Significant");
                    prot.setpValue(Double.valueOf(pValue.trim()));
                    prot.setSignificanceThreshold(significanceThreshold);
                } else if (Double.valueOf(pValue.trim()) >= signThresholdValue) {
                    prot.setStringPValue("Not Significant");
                    prot.setpValue(Double.valueOf(pValue.trim()));
                    prot.setSignificanceThreshold(significanceThreshold);
                }

            } else if (operator.equalsIgnoreCase("<")) {
                if (Double.valueOf(pValue.trim()) < signThresholdValue) {
                    prot.setStringPValue("Significant");
                    prot.setpValue(Double.valueOf(pValue.trim()));
                    prot.setSignificanceThreshold(significanceThreshold);
                } else if (Double.valueOf(pValue.trim()) > signThresholdValue) {
                    prot.setStringPValue("Not Significant");
                    prot.setpValue(Double.valueOf(pValue.trim()));
                    prot.setSignificanceThreshold(significanceThreshold);
                }

            }

        } catch (NumberFormatException exp) {
            prot.setpValue(-1000000000.0);
            prot.setStringPValue(" ");
            System.err.println(" error line 604 " + this.getClass().getName() + "   " + exp);
        }
        prot.setPvalueComment(pvalueComment);

        return prot;
    }

    public Map<String, QuantProtein> readSequanceFile(String path) {

        File dataFile = new File(path);
        String lastKey = "";
        Map<String, QuantProtein> protSeqMap = new HashMap<String, QuantProtein>();
        try {

            FileReader fr = new FileReader(dataFile);
            BufferedReader bufRdr = new BufferedReader(fr);
            String header = bufRdr.readLine();
            String[] headerArr = header.split("\t");
            System.out.println(header + "  headers " + headerArr[0]);
            String line;
            while ((line = bufRdr.readLine()) != null) {
                QuantProtein quantProtein =new QuantProtein();
                String[] lineArr = line.split("\t");
                if (lineArr.length > 1) {
                    quantProtein.setSequance(lineArr[1].trim());
                    protSeqMap.put(lineArr[0].trim(), quantProtein);
                    lastKey = lineArr[0];
                } else {
                    String str = protSeqMap.get(lastKey) + lineArr[0].trim();
                      quantProtein.setSequance(str);
                    protSeqMap.put(lastKey, quantProtein);

                }

            }
        } catch (Exception exp) {

            exp.printStackTrace();
        }
        System.out.println("size " + protSeqMap.size());
        return protSeqMap;
    }

}
