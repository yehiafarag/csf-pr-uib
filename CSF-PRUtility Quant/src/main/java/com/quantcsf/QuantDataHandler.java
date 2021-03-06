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
    private final Map<String, Boolean> activePublications = new HashMap<String, Boolean>();

    public QuantDataHandler() {

//        QuantDatasetObject qa7 = new QuantDatasetObject();
//        qa7.setYear(2011);
//        qa7.setFilesNumber(10);
//        qa7.setAnalyticalMethod("Disease Group I");
//        qa7.setAuthor("Li, Yun, et al.");
//        authormap.put("21445879", qa7);
//        
//        //Differential expression of complement proteins in cerebrospinal fluid from active multiple sclerosis patients.
//
//
//        QuantDatasetObject qa8 = new QuantDatasetObject();
//        qa8.setYear(2014);
//        qa8.setFilesNumber(10);
//        qa8.setAnalyticalMethod("Disease Group I");
//        qa8.setAuthor("Liguori, Maria, et al.");
//        authormap.put("25098164", qa8);
//        
//        //Proteomic profiling in multiple sclerosis clinical courses reveals potential biomarkers of neurodegeneration.
//
//
//        QuantDatasetObject qa9 = new QuantDatasetObject();
//        qa9.setYear(2010);
//        qa9.setFilesNumber(10);
//        qa9.setAnalyticalMethod("Disease Group I");
//        qa9.setAuthor("Ottervald, Jan, et al.");
//        authormap.put("20093204", qa9);
//        // Multiple sclerosis: Identification and clinical evaluation of novel CSF biomarkers.
//        
//
//        QuantDatasetObject qa1 = new QuantDatasetObject();
//        qa1.setYear(2010);
//        qa1.setFilesNumber(10);
//        qa1.setAnalyticalMethod("Disease Group I");
//        qa1.setAuthor("Comabella, Manuel, et al.");
//        authormap.put("20237129", qa1);
//        //Cerebrospinal fluid chitinase 3-like 1 levels are associated with conversion to multiple sclerosis.
//
//        QuantDatasetObject qa2 = new QuantDatasetObject();
//        qa2.setYear(2010);
//        qa2.setAuthor("Harris, Violaine K., et al.");
//        qa2.setFilesNumber(15);
//        qa2.setAnalyticalMethod("Disease Group II");
//        authormap.put("20600910", qa2);
//        //Bri2-23 is a potential cerebrospinal fluid biomarker in multiple sclerosis.
//        
//        
//
//        QuantDatasetObject qa3 = new QuantDatasetObject();
//        qa3.setYear(2012);
//        qa3.setFilesNumber(13);
//        qa3.setAnalyticalMethod("Disease Group II");
//        qa3.setAuthor("Dhaunchak, Ajit Singh, et al.");
//        authormap.put("22473675", qa3);
//        //Implication of perturbed axoglial apparatus in early pediatric multiple sclerosis.
//
//
//        QuantDatasetObject qa4 = new QuantDatasetObject();
//        qa4.setYear(2012);
//        qa4.setFilesNumber(14);
//        qa4.setAnalyticalMethod("Disease Group I");
//        qa4.setAuthor("Jia, Yan, et al.");
//        authormap.put("22846148", qa4);
//        //Development of protein biomarkers in cerebrospinal fluid for secondary progressive multiple sclerosis using selected reaction monitoring mass spectrometry (SRM-MS).
//
//        QuantDatasetObject qa5 = new QuantDatasetObject();
//        qa5.setYear(2013);
//        qa5.setFilesNumber(5);
//        qa5.setAnalyticalMethod("Disease Group III");
//        qa5.setAuthor("Kroksveen, Ann C., et al.");
//        authormap.put("23059536", qa5);
//        //Discovery and initial verification of differentially abundant proteins between multiple sclerosis patients and controls using iTRAQ and SID-SRM.
//        
//        
//
//        QuantDatasetObject qa6 = new QuantDatasetObject();
//        qa6.setYear(2012);
//        qa6.setFilesNumber(16);
//        qa6.setAuthor("Kroksveen, Ann C., et al.");
//        qa6.setAnalyticalMethod("Disease Group II");
//        authormap.put("23278663", qa6);
//        //Cerebrospinal fluid proteome comparison between multiple sclerosis patients and controls.
//
//        QuantDatasetObject qa11 = new QuantDatasetObject();
//        qa11.setYear(2014);
//        qa11.setFilesNumber(10);
//        qa11.setAnalyticalMethod("Disease Group I");
//        qa11.setAuthor("Cant�, Ester, et al.");
//        authormap.put("25406498", qa11);
//        //Validation of semaphorin 7A and ala-?-his-dipeptidase as biomarkers associated with the conversion from clinically isolated syndrome to multiple sclerosis.
//
//        QuantDatasetObject qa12 = new QuantDatasetObject();
//        qa12.setYear(2015);
//        qa12.setFilesNumber(10);
//        qa12.setAnalyticalMethod("Disease Group I");
//        qa12.setAuthor("Hinsinger, G., et al.");
//        authormap.put("25698171", qa12);
//        //Chitinase 3-like proteins as diagnostic and prognostic biomarkers of multiple sclerosis.
//        
//        
//
//        QuantDatasetObject qa13 = new QuantDatasetObject();
//        qa13.setYear(2013);
//        qa13.setFilesNumber(10);
//        qa13.setAnalyticalMethod("Disease Group I");
//        qa13.setAuthor("Stoop, Marcel P., et al");
//        authormap.put("23339689", qa13);
//
//        //Effects of natalizumab treatment on the cerebrospinal fluid proteome of multiple sclerosis patients.
//        
//        
////        QuantDatasetObject qa10 = new QuantDatasetObject();
////        qa10.setYear(2012);
////        qa10.setFilesNumber(10);
////        qa10.setAnalyticalMethod("Disease Group I");
////        qa10.setAuthor("Kroksveen, A. C., et al.");
////        authormap.put("23278663", qa10);
//
//        QuantDatasetObject qa14 = new QuantDatasetObject();
//        qa14.setYear(2015);
//        qa14.setFilesNumber(10);
//        qa14.setAnalyticalMethod("Disease Group I");
//        qa14.setAuthor("Kroksveen, Ann C., et al.");
//        authormap.put("26152395", qa14);
//        //Quantitative proteomics suggests decrease in the secretogranin-1 cerebrospinal fluid levels during the disease course of multiple sclerosis.
//
////        QuantDatasetObject qa15 = new QuantDatasetObject();
////        qa15.setYear(2015);
////        qa15.setFilesNumber(10);
////        qa15.setAnalyticalMethod("Disease Group I");
////        qa15.setAuthor("Astrid, et al.");
////        authormap.put("99999999", qa15);
//
//        QuantDatasetObject qa16 = new QuantDatasetObject();
//        qa16.setYear(2014);
//        qa16.setFilesNumber(10);
//        qa16.setAnalyticalMethod("Disease Group I");
//        qa16.setAuthor("Wildsmith, Kristin R., et al.");
//        authormap.put("24902845", qa16);
//        //Identification of longitudinally dynamic biomarkers in Alzheimer's disease cerebrospinal fluid by targeted proteomics.
//
//        QuantDatasetObject qa17 = new QuantDatasetObject();
//        qa17.setYear(2015);
//        qa17.setFilesNumber(10);
//        qa17.setAnalyticalMethod("Disease Group I");
//        qa17.setAuthor("Barucker, Christian, et al.");
//        authormap.put("25318543", qa17);
//        //Alzheimer amyloid peptide a?42 regulates gene expression of transcription and growth factors.
//
//        QuantDatasetObject qa18 = new QuantDatasetObject();
//        qa18.setYear(2013);
//        qa18.setFilesNumber(10);
//        qa18.setAnalyticalMethod("Disease Group I");
//        qa18.setAuthor("Schutzer, Steven E., et al.");
//        authormap.put("24039694", qa18);
//        //Gray matter is targeted in first-attack multiple sclerosis.
//
//        QuantDatasetObject qa19 = new QuantDatasetObject();
//        qa19.setYear(2015);
//        qa19.setFilesNumber(10);
//        qa19.setAnalyticalMethod("Disease Group I");
//        qa19.setAuthor("Shi, Min, et al.");
//        authormap.put("25556233", qa19);
//
//        //Cerebrospinal fluid peptides as potential Parkinson disease biomarkers: a staged pipeline for discovery and validation.
//        
//        
//        
//        QuantDatasetObject qa20 = new QuantDatasetObject();
//        qa20.setYear(2015);
//        qa20.setFilesNumber(10);
//        qa20.setAnalyticalMethod("Disease Group I");
//        qa20.setAuthor("Borr�s, Eva, et al.");
//        authormap.put("26552840", qa20);
//        //Protein-Based Classifier to Predict Conversion from Clinically Isolated Syndrome to Multiple Sclerosis.
//
//        QuantDatasetObject qa21 = new QuantDatasetObject();
//        qa21.setYear(2015);
//        qa21.setFilesNumber(10);
//        qa21.setAnalyticalMethod("Disease Group I");
//        qa21.setAuthor("Spellman, Daniel S., et al.");
//        authormap.put("25676562", qa21);
//        //Development and evaluation of a multiplexed mass spectrometry based assay for measuring candidate peptide biomarkers in Alzheimer's Disease Neuroimaging Initiative (ADNI) CSF
//
//        QuantDatasetObject qa22 = new QuantDatasetObject();
//        qa22.setYear(2015);
//        qa22.setFilesNumber(10);
//        qa22.setAnalyticalMethod("Disease Group I");
//        qa22.setAuthor("JAO2015");
//        authormap.put("JAO2015", qa22);
//
//        QuantDatasetObject qa23 = new QuantDatasetObject();
//        qa23.setYear(2012);
//        qa23.setFilesNumber(10);
//        qa23.setAnalyticalMethod("Disease Group I");
//        qa23.setAuthor("Lehnert, Stefan, et al.");
//        authormap.put("22327139", qa23);
//        //iTRAQ and multiple reaction monitoring as proteomic tools for biomarker search in cerebrospinal fluid of patients with Parkinson's disease dementia.
//        
//        
//
//        QuantDatasetObject qa24 = new QuantDatasetObject();
//        qa24.setYear(2015);
//        qa24.setFilesNumber(10);
//        qa24.setAnalyticalMethod("Disease Group I");
//        qa24.setAuthor("Collins, Mahlon A., et al.");
//        authormap.put("26401960", qa24);
//        //Label-Free LC-MS/MS Proteomic Analysis of Cerebrospinal Fluid Identifies Protein/Pathway Alterations and Candidate Biomarkers for Amyotrophic Lateral Sclerosis.
//
//        QuantDatasetObject qa25 = new QuantDatasetObject();
//        qa25.setYear(2013);
//        qa25.setFilesNumber(10);
//        qa25.setAnalyticalMethod("Disease Group I");
//        qa25.setAuthor("Varghese, Anu Mary, et al.");
//        authormap.put("24295388", qa25);
//        //Chitotriosidase - a putative biomarker for sporadic amyotrophic lateral sclerosis.
//        
//        
//        QuantDatasetObject qa26 = new QuantDatasetObject();
//        qa26.setYear(2015);
//        qa26.setFilesNumber(10);
//        qa26.setAnalyticalMethod("Disease Group I");
//        qa26.setAuthor("Heywood, Wendy E., et al.");
//        authormap.put("26627638", qa26);
//        //Identification of novel CSF biomarkers for neurodegeneration and their validation by a high-throughput multiplexed targeted proteomic assay.
    }

    public Map<String, Boolean> getActivePublications() {
        return activePublications;
    }

    public Map<String, QuantProtein> getProteinAccSequanceMap() {
        return proteinAccSequanceMap;
    }
    private Map<String, QuantProtein> unrevProteinAccMap;
    private Map<String, QuantProtein> proteinAccSequanceMap;

    @SuppressWarnings("CallToPrintStackTrace")
    public List<QuantProtein> readCSVQuantFile(String path, String sequanceFilePath, String unreviewFilePath) {

        File sequanceFile = new File(sequanceFilePath);

        File dataFile = new File(path);
        List<QuantProtein> QuantProtList = new ArrayList<QuantProtein>();
        QuantProtein quantProt = new QuantProtein();
        try {

            FileReader sequanceReader = new FileReader(sequanceFile);
            BufferedReader sequanceBufRdr = new BufferedReader(sequanceReader);

            proteinAccSequanceMap = new HashMap<String, QuantProtein>();
            String seqline;

            int row = 1;
            while ((seqline = sequanceBufRdr.readLine()) != null && !seqline.trim().equalsIgnoreCase("") && row < 1000000000) {

                if ((seqline.startsWith(">sp|") || seqline.startsWith(">tr|"))) {
                    if (quantProt.getUniprotAccession() != null) {
                        if (!proteinAccSequanceMap.containsKey(quantProt.getUniprotAccession().trim().toLowerCase())) {
                            proteinAccSequanceMap.put(quantProt.getUniprotAccession().trim().toLowerCase(), quantProt);
                        }
                    }
                    quantProt = new QuantProtein();
                    String[] seqArr = seqline.replace("|", "----").split("----");
                    quantProt.setUniprotAccession(seqArr[1].replace(" ", "").trim());
                    String secSide = seqArr[2].split("OS=")[0].trim();
                    String protName = secSide.replace(secSide.split(" ")[0], "") + " (" + secSide.split(" ")[0] + ")";
                    quantProt.setUniprotProteinName(protName.trim());

                } else {
                    if (quantProt.getSequance() != null) {
                        quantProt.setSequance(quantProt.getSequance() + seqline);
                    } else {
                        quantProt.setSequance(seqline);
                    }
                }

//                
            }
            if (!proteinAccSequanceMap.containsKey(quantProt.getUniprotAccession().trim().toLowerCase())) {
                proteinAccSequanceMap.put(quantProt.getUniprotAccession().trim().toLowerCase(), quantProt);
            }

            sequanceBufRdr.close();

            FileReader unreviewFileReader = new FileReader(unreviewFilePath);
            BufferedReader unreviewFileBufRdr = new BufferedReader(unreviewFileReader);

            unrevProteinAccMap = new HashMap<String, QuantProtein>();
            row = 1;
            while ((seqline = unreviewFileBufRdr.readLine()) != null && !seqline.trim().equalsIgnoreCase("") && row < 1000000000) {
                if ((seqline.startsWith(">sp|") || seqline.startsWith(">tr|"))) {
                    if (quantProt.getUniprotAccession() != null) {
                        if (!unrevProteinAccMap.containsKey(quantProt.getUniprotAccession().trim().toLowerCase())) {
                            unrevProteinAccMap.put(quantProt.getUniprotAccession().trim().toLowerCase(), quantProt);
                        }
                    }
                    quantProt = new QuantProtein();
                    String[] seqArr = seqline.replace("|", "----").split("----");
                    quantProt.setUniprotAccession(seqArr[1].replace(" ", "").trim());
                    String secSide = seqArr[2].split("OS=")[0].trim();
                    String protName = secSide.replace(secSide.split(" ")[0], "") + " (" + secSide.split(" ")[0] + ")";
                    quantProt.setUniprotProteinName(protName.trim());
                } else {
                    quantProt.setSequance(quantProt.getSequance() + seqline);

                }
//                
            }
            if (!unrevProteinAccMap.containsKey(quantProt.getUniprotAccession().trim().toLowerCase())) {
                unrevProteinAccMap.put(quantProt.getUniprotAccession().trim().toLowerCase(), quantProt);
            }

            unreviewFileBufRdr.close();

            FileReader fr = new FileReader(dataFile);
            BufferedReader bufRdr = new BufferedReader(fr);
            String header = bufRdr.readLine();
            int i = 0;
            String[] headerArr = header.split(",");
            for (String col : headerArr) {
                i++;
            }

            int index;
            row = 1;
            String line;
            while ((line = bufRdr.readLine()) != null && row < 1000000000) {

                index = 0;
                QuantProtein qProt = new QuantProtein();
                String[] rowArr = line.replace("�", "").split(",");//
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
                    return null;
                }
                activePublications.put(qProt.getPumedID(), Boolean.TRUE);
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
                    qProt.setUniprotAccession("");
                    index++;
                } else {
                    qProt.setUniprotAccession(updatedRowArr[index++].trim());
                }

                if (proteinAccSequanceMap.containsKey(qProt.getUniprotAccession().toLowerCase())) {
                    qProt.setSequance(proteinAccSequanceMap.get(qProt.getUniprotAccession().toLowerCase()).getSequance());
                    qProt.setUniprotProteinName(proteinAccSequanceMap.get(qProt.getUniprotAccession().toLowerCase()).getUniprotProteinName());
                } else if (unrevProteinAccMap.containsKey(qProt.getUniprotAccession().toLowerCase())) {
                    qProt.setSequance(unrevProteinAccMap.get(qProt.getUniprotAccession().toLowerCase()).getSequance());
                    qProt.setUniprotProteinName(unrevProteinAccMap.get(qProt.getUniprotAccession().toLowerCase()).getUniprotProteinName());
                } else {
                    qProt.setSequance(" ");
                    qProt.setUniprotProteinName("");
                }
                index++;
                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setPublicationAccNumber(" ");
                    index++;
                } else {
                    qProt.setPublicationAccNumber((updatedRowArr[index++]).trim());
                }
//
                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setPublicationProteinName(" ");
                    index++;
                } else {
                    qProt.setPublicationProteinName(updatedRowArr[index++]);
                }
                if (qProt.getUniprotAccession().equals("") && proteinAccSequanceMap.containsKey(qProt.getPublicationAccNumber().toLowerCase())) {
                    qProt.setUniprotAccession(qProt.getPublicationAccNumber());
                    qProt.setSequance(proteinAccSequanceMap.get(qProt.getPublicationAccNumber().toLowerCase()).getSequance());
                    qProt.setUniprotProteinName(proteinAccSequanceMap.get(qProt.getPublicationAccNumber().toLowerCase()).getUniprotProteinName());
                } else if (qProt.getUniprotAccession().equals("")  && unrevProteinAccMap.containsKey(qProt.getPublicationAccNumber().toLowerCase())) {
                    qProt.setSequance(unrevProteinAccMap.get(qProt.getPublicationAccNumber().toLowerCase()).getSequance());
                    qProt.setUniprotProteinName(unrevProteinAccMap.get(qProt.getPublicationAccNumber().toLowerCase()).getUniprotProteinName());
                } 
                
                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("Peptide")) {
                    qProt.setPeptideObject(true);

                } else {
                    qProt.setPeptideObject(false);
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
                    String dieseaseGroup = updatedRowArr[index++];
                    if (dieseaseGroup.trim().equalsIgnoreCase("AD")) {
                        dieseaseGroup = "Alzheimer's";
                    } else if (dieseaseGroup.trim().equalsIgnoreCase("PD") || dieseaseGroup.trim().equalsIgnoreCase("Parkinson")) {
                        dieseaseGroup = "Parkinson's";
                    } else if (dieseaseGroup.trim().equalsIgnoreCase("ALS")) {
//                        dieseaseGroup = "Amyotrophic Lateral Sclerosis";
                    }

                    qProt.setPatientGroupI(dieseaseGroup);
                }
//
                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setPatientSubGroupI(qProt.getPatientGroupI());
                    index++;
                } else {
                    String dieseaseSubGroup = updatedRowArr[index++];
                    if (dieseaseSubGroup.trim().equalsIgnoreCase("AD")) {
                        dieseaseSubGroup = "Alzheimer's";
                    } else if (dieseaseSubGroup.trim().equalsIgnoreCase("PD") || dieseaseSubGroup.trim().equalsIgnoreCase("Parkinson")) {
                        dieseaseSubGroup = "Parkinson's";
                    } else if (dieseaseSubGroup.trim().equalsIgnoreCase("ALS")) {
//                        dieseaseSubGroup = "Amyotrophic Lateral Sclerosis";
                    }

                    qProt.setPatientSubGroupI(dieseaseSubGroup);
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
                    String dieseaseGroup = updatedRowArr[index++];
                    if (dieseaseGroup.trim().equalsIgnoreCase("AD")) {
                        dieseaseGroup = "Alzheimer's";
                    } else if (dieseaseGroup.trim().equalsIgnoreCase("PD") || dieseaseGroup.trim().equalsIgnoreCase("Parkinson")) {
                        dieseaseGroup = "Parkinson's";
                    } else if (dieseaseGroup.trim().equalsIgnoreCase("ALS")) {
//                        dieseaseGroup = "Amyotrophic Lateral Sclerosis";
                    }

                    qProt.setPatientGroupII(dieseaseGroup.replace("Controls", "Control"));
                }

                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setPatientSubGroupII(qProt.getPatientGroupII());
                    index++;
                } else {
                    String dieseaseSubGroup = updatedRowArr[index++];
                    if (dieseaseSubGroup.trim().equalsIgnoreCase("AD")) {
                        dieseaseSubGroup = "Alzheimer's";
                    } else if (dieseaseSubGroup.trim().equalsIgnoreCase("PD") || dieseaseSubGroup.trim().equalsIgnoreCase("Parkinson")) {
                        dieseaseSubGroup = "Parkinson's";
                    } else if (dieseaseSubGroup.trim().equalsIgnoreCase("ALS")) {
//                        dieseaseSubGroup = "Amyotrophic Lateral Sclerosis";
                    }

                    qProt.setPatientSubGroupII(dieseaseSubGroup);
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
                        qProt.setFcPatientGroupIonPatientGroupII(Double.valueOf(updatedRowArr[index].replace(",", ".").replace("?", "-")));
                    } catch (NumberFormatException exp) {
                        qProt.setFcPatientGroupIonPatientGroupII(-1000000000.0);
                        qProt.setStringFCValue(updatedRowArr[index]);
                    }
                } else {
                    qProt.setFcPatientGroupIonPatientGroupII(-1000000000.0);
                    qProt.setStringFCValue("Not Provided");
                }
                index++;

                if (!updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    try {
                        qProt.setLogFC(Double.valueOf(updatedRowArr[index].replace(",", ".").replace("?", "-")));
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
                            if (qProt.getStringFCValue() == null) {
                                qProt.setStringFCValue("Not Provided");
                            }
                        }
                    } finally {
                        index++;
                    }
                } else {
                    qProt.setLogFC(-1000000000.0);
                    if (qProt.getStringFCValue() == null) {
                        qProt.setStringFCValue("Not Provided");
                    }
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
                    qProt.setRocAuc(Double.valueOf(updatedRowArr[index].replace(",", ".").replace("?", "-")));
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
                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setDiseaseCategory(" ");
                    index++;
                } else {
                    String diseaseCat = updatedRowArr[index++];
                    if (diseaseCat.trim().equalsIgnoreCase("MS")) {
                        diseaseCat = "Multiple Sclerosis";
                    } else if (diseaseCat.trim().equalsIgnoreCase("AD")) {
                        diseaseCat = "Alzheimer's";
                    } else if (diseaseCat.trim().equalsIgnoreCase("PD") || diseaseCat.trim().equalsIgnoreCase("Parkinson")) {
                        diseaseCat = "Parkinson's";
                    } else if (diseaseCat.trim().equalsIgnoreCase("ALS")) {
                        diseaseCat = "Amyotrophic Lateral Sclerosis";
                    }

                    qProt.setDiseaseCategory(diseaseCat);
                }
                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setDiseaseCategory(" ");
                    index++;
                } else {
                    String pooledSample = updatedRowArr[index++];
                    boolean pooles = false;
                    if (pooledSample.trim().equalsIgnoreCase("Yes")) {
                        pooles = true;
                    }
                    qProt.setPooledSample(pooles);
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

                String pepKey = qProt.getPumedID() + "_" + qProt.getStudyKey() + "_" + qProt.getUniprotAccession() + "_" + qProt.getPublicationAccNumber() + "_" + qProt.getTypeOfStudy() + "_" + qProt.getSampleType() + "_" + qProt.getTechnology() + "_" + qProt.getAnalyticalApproach() + "_" + qProt.getAnalyticalMethod() + "_" + qProt.getPatientsGroupINumber() + "_" + qProt.getPatientsGroupIINumber() + "_" + qProt.getPatientSubGroupI() + "_" + qProt.getPatientSubGroupII() + "_" + qProt.getDiseaseCategory();
                qProt.setQuantPeptideKey(pepKey);
                QuantProtList.add(qProt);
                if (qProt.getUniprotAccession().trim().equalsIgnoreCase("")) {
                    System.out.println("at the error line is " + qProt.getUniprotAccession() + "  " + proteinAccSequanceMap.containsKey(qProt.getPublicationAccNumber().toLowerCase())+"  "+unrevProteinAccMap.containsKey(qProt.getPublicationAccNumber().toLowerCase())+"  "+line);
                }

                row++;
            } //    
            bufRdr.close();

            List<QuantProtein> updatedQuantProtList = new ArrayList<QuantProtein>();
            for (QuantProtein quantProtein : QuantProtList) {
                if (unrevProteinAccMap.containsKey(quantProtein.getUniprotAccession().toLowerCase())) {
                    quantProtein.setUniprotAccession(quantProtein.getUniprotAccession() + "(unreviewed)");

                }

                updatedQuantProtList.add(quantProtein);

            }
            QuantProtList.clear();
            QuantProtList.addAll(updatedQuantProtList);

        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("ssleeping error " + ex.getMessage());
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            System.out.println("error in " + ex);
        } catch (Exception e) {
            e.printStackTrace();
        }

        proteinAccSequanceMap.putAll(unrevProteinAccMap);
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
        String operator = "op";
        try {
            pValue = pValue.replace(",", ".").replace("?", "-");

            double signThresholdValue = -1;
            if (significanceThreshold == null || significanceThreshold.trim().equalsIgnoreCase("")) {
                if (pValue.contains(">")) {
                    significanceThreshold = pValue.replace(">", "<");
                    operator = "<";
                    signThresholdValue = Double.valueOf(significanceThreshold.replace("<", "").replace("�", "").replace("�", "").replace("�", "").trim());
                } else if (pValue.contains("<")) {
                    significanceThreshold = pValue;
                    operator = "<";
                    signThresholdValue = Double.valueOf(significanceThreshold.replace("<", "").replace("�", "").replace("�", "").replace("�", "").trim());

                } else {
                    significanceThreshold = "defined by CSF-Pr at 0.05";
                    signThresholdValue = 0.05;
                    operator = "<";
                }

            } else if (significanceThreshold.contains("<=")) {
                significanceThreshold = significanceThreshold.replace(",", ".").replace("?", "-");
                signThresholdValue = Double.valueOf(significanceThreshold.replace("<=", ",").replace("�", "").replace("�", "").split(",")[1].replace("�", "").trim());
                operator = "<=";
            } else if (significanceThreshold.contains("<")) {
                significanceThreshold = significanceThreshold.replace(",", ".").replace("?", "-");
                signThresholdValue = Double.valueOf(significanceThreshold.split("<")[1].trim());
                operator = "<";
            } else {
                significanceThreshold = significanceThreshold.replace(",", ".").replace("?", "-");
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
                } else if (Double.valueOf(pValue.trim()) >= signThresholdValue) {
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

//        if(prot.getStringPValue()== null || prot.getStringPValue().trim().equalsIgnoreCase(""))
//        System.out.println("operator "+operator+"  "+significanceThreshold+"    "+pValue + "    String p Value "+prot.getUniprotAccession()+"  "+"  "+ prot.getStudyKey()+"   "+ prot.getPatientSubGroupI());
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
                QuantProtein quantProtein = new QuantProtein();
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

    public void updatePublicationMap(List<Object[]> pulicationsList) {
        authormap.clear();
        activePublications.clear();
        for (Object[] publication : pulicationsList) {
            QuantDatasetObject qa = new QuantDatasetObject();
            qa.setYear((Integer.valueOf(publication[2].toString().trim())));
            qa.setFilesNumber(10);
            qa.setAnalyticalMethod("Disease Group I");
            qa.setAuthor(publication[1].toString());
            authormap.put(publication[0].toString(), qa);
            activePublications.put(publication[0].toString(), Boolean.FALSE);

        }

    }

    public Map<String, String> readDiseaseGroupsFullNameFile(String filePath) {
        Map<String, String> diseaseGroupsNamingMap = new HashMap<String, String>();

        File dataFile = new File(filePath);

        try {

            FileReader fr = new FileReader(dataFile);
            BufferedReader bufRdr = new BufferedReader(fr);
            bufRdr.readLine();
            String line;
            while ((line = bufRdr.readLine()) != null) {

                String[] lineArr = line.split(";");
                if (lineArr.length > 1) {
                    diseaseGroupsNamingMap.put(lineArr[0], lineArr[1]);

                } else {
                    diseaseGroupsNamingMap.put(lineArr[0], lineArr[0]);

                }

            }
        } catch (Exception exp) {

            exp.printStackTrace();
        }

        return diseaseGroupsNamingMap;
    }

//       StringBuilder valuesBuilder = new StringBuilder();
//       for(String key:diseaseGroupsNamingMap.keySet()){
//       valuesBuilder.append("('").append(key).append("','").append(diseaseGroupsNamingMap.get(key)).append("'),");
//       
//       }
//       String valueStr = valuesBuilder.toString().substring(0, valuesBuilder.length()-2);
//        System.out.println("valueStr "+valueStr);
//       
//    String insertStat = "INSERT INTO `defin_disease_groups` (`min`, `full`) VALUES "+valueStr;
//    
////    
//     + "(' MS', ' Multiple sclerosis'),\n"
//                        + "(' CIS', ' Clinically isolated syndrome'),\n"
//                        + "(' OND', ' Other neurological disorders'),\n"
//                        + "(' ONID', ' Other inflammatory neurological disorders'),\n"
//                        + "(' RRMS', ' Relapsing remitting multiple sclerosis'),\n"
//                        + "(' SPMS', ' Secondary progressive multiple sclerosis'),\n"
//                        + "(' PMS', 'progressive multiple sclerosis'),\n"
//                        + "(' CIS-MS', ' Clinically isolated syndrome, with conversion to multiple sclerosis'),\n"
//                        + "('CIS-CIS', 'Clinically isolated syndrome, without conversion to multiple sclerosis'),\n"
//                        + "(' CIS-MS/CIS', ' Clinically isolated syndrome, with and without conversion to multiple sclerosis'),\n"
//                        + "(' AD', 'Alzheimer''s disease'),\n"
//                        + "(' MCI', ' Mild cognitive impairment'),\n"
//                        + "(' RRMS a/ Natalizumab', ' Relapsing remitting multiple sclerosis after natalizumab'),\n"
//                        + "(' SPMS a/Lamotrigine', ' Secondary progressive multiple sclerosis after lamotrigine'),\n"
//                        + "(' OIND + OND ', ' Other inflammatory neurological disorders + Other neurological disorders');";
//    
//    
//            
//            }
    public QuantProtein initQuantProteinFromQuantPeptide(QuantProtein quantPeptide) {
        QuantProtein quantProtein = new QuantProtein();
        quantProtein.setAdditionalComments(quantPeptide.getAdditionalComments());
        quantProtein.setAnalyticalApproach(quantPeptide.getAnalyticalApproach());
        quantProtein.setAnalyticalMethod(quantPeptide.getAnalyticalMethod());
        quantProtein.setAuthor(quantPeptide.getAuthor());
        quantProtein.setDiseaseCategory(quantPeptide.getDiseaseCategory());
        quantProtein.setDiseaseGroups(quantPeptide.getDiseaseGroups());
        quantProtein.setDsKey(quantPeptide.getDsKey());
        quantProtein.setEnzyme(quantPeptide.getEnzyme());
        quantProtein.setFcPatientGroupIonPatientGroupII(quantPeptide.getFcPatientGroupIonPatientGroupII());
        quantProtein.setFilesNum(quantPeptide.getFilesNum());
        quantProtein.setIdentifiedProteinsNum(quantPeptide.getIdentifiedProteinsNum());
        quantProtein.setLogFC(quantPeptide.getLogFC());
        quantProtein.setModificationComment(quantPeptide.getModificationComment());
        quantProtein.setNormalizationStrategy(quantPeptide.getNormalizationStrategy());
        quantProtein.setPatientGrIComment(quantPeptide.getPatientGrIComment());
        quantProtein.setPatientGrIIComment(quantPeptide.getPatientGrIIComment());
        quantProtein.setPatientGroupI(quantPeptide.getPatientGroupI());
        quantProtein.setPatientGroupII(quantPeptide.getPatientGroupII());
        quantProtein.setPatientSubGroupI(quantPeptide.getPatientSubGroupI());
        quantProtein.setPatientSubGroupII(quantPeptide.getPatientSubGroupII());
        quantProtein.setPatientsGroupIINumber(quantPeptide.getPatientsGroupIINumber());
        quantProtein.setPatientsGroupINumber(quantPeptide.getPatientsGroupINumber());
        quantProtein.setPeptideCharge(quantPeptide.getPeptideCharge());
        quantProtein.setPeptideIdNumb(quantPeptide.getPeptideIdNumb());
        quantProtein.setPeptideModification(quantPeptide.getPeptideModification());
        quantProtein.setPeptideObject(false);
        quantProtein.setPeptideSequance(quantPeptide.getPeptideSequance());
        quantProtein.setPeptideSequenceAnnotated(quantPeptide.getPeptideSequenceAnnotated());
        quantProtein.setPooledSample(quantPeptide.isPooledSample());
        quantProtein.setProtKey(quantPeptide.getProtKey());
        quantProtein.setPublicationAccNumber(quantPeptide.getPublicationAccNumber());
        quantProtein.setPublicationProteinName(quantPeptide.getPublicationProteinName());
        quantProtein.setPumedID(quantPeptide.getPumedID());
        quantProtein.setPvalueComment(quantPeptide.getPvalueComment());
        quantProtein.setQuantBasisComment(quantPeptide.getQuantBasisComment());
        String quantPeptideKey = quantPeptide.getQuantPeptideKey();
        quantProtein.setQuantPeptideKey(quantPeptideKey);
        quantProtein.setQuantificationBasis(quantPeptide.getQuantificationBasis());
        quantProtein.setQuantifiedPeptidesNumber(quantPeptide.getQuantifiedPeptidesNumber());
        quantProtein.setQuantifiedProteinsNumber(quantPeptide.getQuantifiedProteinsNumber());
        quantProtein.setRawDataAvailable(quantPeptide.getRawDataAvailable());
        quantProtein.setRocAuc(quantPeptide.getRocAuc());
        quantProtein.setSampleMatching(quantPeptide.getSampleMatching());
        quantProtein.setSampleType(quantPeptide.getSampleType());
        String sequence = " ";
        quantProtein.setSequance(sequence);
        quantProtein.setShotgunOrTargetedQquant(quantPeptide.getShotgunOrTargetedQquant());
        quantProtein.setSignificanceThreshold(quantPeptide.getSignificanceThreshold());
        quantProtein.setStringFCValue(quantPeptide.getStringFCValue());
        quantProtein.setStringPValue(quantPeptide.getStringPValue());
        quantProtein.setStudyKey(quantPeptide.getStudyKey());
        quantProtein.setTechnology(quantPeptide.getTechnology());
        quantProtein.setTypeOfStudy(quantPeptide.getTypeOfStudy());
        quantProtein.setUniprotAccession(quantPeptide.getUniprotAccession());
        quantProtein.setUniprotProteinName(quantPeptide.getUniprotProteinName());
        quantProtein.setYear(quantPeptide.getYear());
        quantProtein.setpValue(quantPeptide.getpValue());
        if (proteinAccSequanceMap.containsKey(quantProtein.getUniprotAccession().toLowerCase())) {
            quantProtein.setSequance(proteinAccSequanceMap.get(quantProtein.getUniprotAccession().toLowerCase()).getSequance());
            quantProtein.setUniprotProteinName(proteinAccSequanceMap.get(quantProtein.getUniprotAccession().toLowerCase()).getUniprotProteinName());
        } else if (unrevProteinAccMap.containsKey(quantProtein.getUniprotAccession().toLowerCase())) {
            quantProtein.setSequance(unrevProteinAccMap.get(quantProtein.getUniprotAccession().toLowerCase()).getSequance());
            quantProtein.setUniprotProteinName(unrevProteinAccMap.get(quantProtein.getUniprotAccession().toLowerCase()).getUniprotProteinName());
        }
        return quantProtein;

    }
}
