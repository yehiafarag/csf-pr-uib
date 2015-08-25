package probe.com.model.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import probe.com.model.beans.identification.IdentificationDatasetBean;
import probe.com.model.beans.identification.IdentificationFractionBean;
import probe.com.model.beans.identification.IdentificationPeptideBean;
import probe.com.model.beans.identification.IdentificationProteinBean;
import probe.com.model.beans.quant.QuantProtein;
import probe.com.model.beans.identification.StandardIdentificationFractionPlotProteinBean;

/**
 *
 * @author Yehia Farag reader for uploaded files
 */
public class FilesReader implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final FileValidator fileValidator = new FileValidator();

    /**
     * read the identification text files in order to store the data in the
     * dataset
     *
     * @param file
     * @param MIMEType
     * @param identificationDatasetBean
     * @return updated identificationDatasetBean
     * @throws IOException
     * @throws SQLException
     */
    @SuppressWarnings({"resource", "UnnecessaryBoxing"})
    public IdentificationDatasetBean readIdentificationTextFile(File file, String MIMEType, IdentificationDatasetBean identificationDatasetBean) throws IOException, SQLException//method to extract data from proteins files to store them in database
    {
        //if excel file
        //else text file
        int fileType;
        String[] strArr = null;
        String[] lineArr = null;
        Map<Integer, IdentificationFractionBean> fractionRanges = null;
        BufferedReader bufRdr = null;
        String line = null;
        int row = 0;

        if (MIMEType.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") || MIMEType.equalsIgnoreCase("application/octet-stream")) {
            RangeReader msReader = new RangeReader();
            try {
                FileInputStream myInput = new FileInputStream(file);
                lineArr = msReader.readRangeFile(myInput);
                if (lineArr[0].contains("Plot Gel")) {
                    strArr = lineArr[1].split("\t");
                } else {
                    strArr = lineArr[0].split("\t");
                }

            } catch (Exception e) {
                System.err.println("at error " + this.getClass().getName() + "  at line 69 " + e.getLocalizedMessage());
            }
        } else {
            FileReader fileReader = new FileReader(file);
            bufRdr = new BufferedReader(fileReader);
            line = bufRdr.readLine();
            if (MIMEType.equals("text/plain")) {
                strArr = line.split("\t");
            }
        }

        Map<Integer, IdentificationFractionBean> fractionsList = new HashMap<Integer, IdentificationFractionBean>();
        Map<Integer, IdentificationPeptideBean> peptideList = null;
        Map<String, IdentificationPeptideBean> gPeptideList = new HashMap<String, IdentificationPeptideBean>();

        fileType = fileValidator.validateFile(strArr, MIMEType);//check if the file type and  file format 
        identificationDatasetBean.setExpFile(fileType);
        Map<String, IdentificationProteinBean> proteinList = new HashMap<String, IdentificationProteinBean>();//use only in case of protein files
        if (fileType == -1)//wrong file 
        {
            return null;
        } else if (fileType == -7 && bufRdr != null)//glyco file
        {
            IdentificationPeptideBean pb;
            try {
                while ((line = bufRdr.readLine()) != null && row < 1000) {

                    pb = new IdentificationPeptideBean();
                    strArr = line.split("\t");
                    if (strArr.length > 24 && strArr[24] != null && !strArr[24].equals("")) {
                        pb.setDeamidationAndGlycopattern(Boolean.valueOf(strArr[24]));
                    } else {
                        pb.setDeamidationAndGlycopattern(false);
                    }
                    if (strArr.length > 25 && strArr[25] != null && !strArr[25].equals("")) {
                        pb.setGlycopatternPositions((strArr[25]));
                    } else {
                        pb.setGlycopatternPositions("");
                    }
                    pb.setLikelyNotGlycosite(false);
                    String key = "[" + strArr[0].trim() + "][" + strArr[9].trim() + "]";
                    gPeptideList.put(key, pb);
                }

            } catch (Exception e) {
                System.err.println("at error " + this.getClass() + "  line 114   " + e.getLocalizedMessage());
            }
            identificationDatasetBean.setgPeptideList(gPeptideList);
            return identificationDatasetBean;

        } else if (fileType == -2)//peptide file
        {
            peptideList = new HashMap<Integer, IdentificationPeptideBean>();
            identificationDatasetBean.setExpFile(fileType);
        } else if (fileType == -100 && lineArr != null)//Standard plot file
        {
            try {
                List<StandardIdentificationFractionPlotProteinBean> standerdPlotProt = new ArrayList<StandardIdentificationFractionPlotProteinBean>();
                for (int x = 2; x < lineArr.length; x++) {
                    String str = lineArr[x].trim();
                    if (str != null && (!str.equals(""))) {
                        StandardIdentificationFractionPlotProteinBean spb = new StandardIdentificationFractionPlotProteinBean();
                        spb.setMW_kDa(Double.valueOf(str.trim().split("\t")[0]));
                        spb.setName(str.split("\t")[1]);
                        double d = (Double.valueOf(str.split("\t")[2]));
                        spb.setLowerFraction((int) d);
                        d = (Double.valueOf(str.split("\t")[3]));
                        spb.setUpperFraction((int) d);
                        spb.setColor(str.split("\t")[4]);
                        standerdPlotProt.add(spb);
                    }
                }
                identificationDatasetBean.setStanderdPlotProt(standerdPlotProt);
            } catch (Exception e) {
                System.err.println("at error " + this.getClass() + "  line 143   " + e.getLocalizedMessage());
            }

            return identificationDatasetBean;
        } else if (fileType == 0)//Protein file
        {
        } else if (fileType == -5 && lineArr != null)//fraction range file
        {
            fractionRanges = new TreeMap<Integer, IdentificationFractionBean>();
            for (int x = 1; x < lineArr.length; x++) {
                String str = lineArr[x];
                IdentificationFractionBean fb = new IdentificationFractionBean();

                double keyDuble = Double.parseDouble(str.split("\t")[0]);
                int key = (int) keyDuble;
                fb.setFractionIndex(key);
                fractionRanges.put(key, fb);
            }
            return identificationDatasetBean;
        } else {
            if (fileType > 0) {
                identificationDatasetBean.setFractionsNumber(fileType);//file type in case of protein fraction file return the number of fractions
                //create a number of fractions for the experiment
                for (int x = 0; x < fileType; x++) {
                    IdentificationFractionBean fb = new IdentificationFractionBean();
                    Map<String, IdentificationProteinBean> temProteinList = new HashMap<String, IdentificationProteinBean>();
                    fb.setProteinList(temProteinList);
                    fb.setFractionIndex(x + 1);
                    fractionsList.put((x), fb);
                }
            }
        }
        int inedxId = 0;
        if (bufRdr == null) {
            return null;
        }
        while (fileType != -5 && fileType != -7 && fileType != -100 && (line = bufRdr.readLine()) != null && row < 1000)//loop to fill the protein beans and add it to fraction list
        {
            strArr = line.split("\t");

            IdentificationProteinBean identificationProteinBean = null;
            if (fileType != -2) {
                identificationProteinBean = new IdentificationProteinBean();
                identificationProteinBean.setAccession(strArr[0]);
                identificationProteinBean.setOtherProteins(strArr[1]);
                identificationProteinBean.setProteinInferenceClass(strArr[2]);
                identificationProteinBean.setDescription(strArr[3]);
            }

            if (fileType == -2) {

                IdentificationPeptideBean pb = new IdentificationPeptideBean();
                pb.setProtein(strArr[0]);
                pb.setOtherProteins(strArr[1]);

                pb.setPeptideProteins(strArr[2]);
                //set identificationProteinBean desc 3
                pb.setOtherProteinDescriptions(strArr[4]);

                pb.setPeptideProteinsDescriptions(strArr[5]);
                pb.setProteinInference(strArr[6].toUpperCase());
                pb.setAaBefore(strArr[7]);
                pb.setSequence(strArr[8]);

                pb.setSequenceTagged(strArr[9].toUpperCase());

                pb.setAaAfter(strArr[10]);

                pb.setEnzymatic(Boolean.valueOf(strArr[11].toUpperCase()));

                pb.setPeptideStart((strArr[12]));
                pb.setPeptideEnd((strArr[13]));
                pb.setFixedModification(strArr[14]);
                pb.setVariableModification(strArr[15]);
                pb.setLocationConfidence(strArr[16]);
                pb.setPrecursorCharges(strArr[17]);
                if (!strArr[18].equals("")) {
                    pb.setNumberOfValidatedSpectra(Integer.valueOf(strArr[18]));
                }
                if (!strArr[19].equals("")) {
                    pb.setScore(Double.valueOf(strArr[19]));
                }
                if (!strArr[20].equals("")) {
                    try {
                        pb.setConfidence(Double.valueOf(strArr[20]));
                    } catch (NumberFormatException nfe) {
                        pb.setConfidence(Double.valueOf(0.0));

                    }
                }
                if (!strArr[21].equals("")) {
                    pb.setValidated(Double.valueOf(strArr[21]));
                }
                pb.setDecoy(Integer.valueOf(strArr[22]));
                pb.setStarred(Boolean.valueOf(strArr[23]));
                if (strArr.length == 24) {
                } else {
                    if (strArr[24] != null) {
                        pb.setDeamidationAndGlycopattern(Boolean.valueOf(strArr[24]));
                    }
                    if (strArr[25] != null) {
                        pb.setGlycopatternPositions(strArr[25]);
                    }
                }
                if (pb.getDecoy() == 0 && peptideList != null) {
                    pb.setPeptideId(inedxId);
                    peptideList.put(pb.getPeptideId(), pb);
                    inedxId++;
                } else {
                }

            } else if (fileType == 0 && identificationProteinBean != null) //Protein file
            {
                identificationProteinBean.setSequenceCoverage(Double.valueOf(strArr[4]));
                identificationProteinBean.setObservableCoverage(Double.valueOf(strArr[5]));
                identificationProteinBean.setNonEnzymaticPeptides(Boolean.valueOf(strArr[6]));
                identificationProteinBean.setConfidentPtmSites(strArr[7]);
                identificationProteinBean.setNumberConfident(strArr[8]);
                identificationProteinBean.setOtherPtmSites(strArr[9]);
                identificationProteinBean.setNumberOfOther(strArr[10]);
                identificationProteinBean.setNumberValidatedPeptides(Integer.valueOf(strArr[11]));
                identificationProteinBean.setNumberValidatedSpectra(Integer.valueOf(strArr[12]));
                identificationProteinBean.setEmPai(Double.valueOf(strArr[13]));
                identificationProteinBean.setNsaf(Double.valueOf(strArr[14]));
                identificationProteinBean.setMw_kDa(Double.valueOf(strArr[15]));
                identificationProteinBean.setScore(Double.valueOf(strArr[16]));
                identificationProteinBean.setConfidence(Double.valueOf(strArr[17]));
                identificationProteinBean.setStarred(Boolean.valueOf(strArr[18]));
                proteinList.put(identificationProteinBean.getAccession(), identificationProteinBean);
            } else //Protein fraction file
            {
                IdentificationProteinBean tempProt;
                if (identificationProteinBean == null) {
                    return null;
                }

                identificationProteinBean.setStarred(Boolean.valueOf(strArr[strArr.length - 1]));
                identificationProteinBean.setSpectrumFractionSpread_upper_range_kDa(strArr[strArr.length - 3]);
                identificationProteinBean.setSpectrumFractionSpread_lower_range_kDa(strArr[strArr.length - 4]);
                identificationProteinBean.setPeptideFractionSpread_upper_range_kDa(strArr[strArr.length - 5]);
                identificationProteinBean.setPeptideFractionSpread_lower_range_kDa(strArr[strArr.length - 6]);
                for (int x = 0; x < fileType; x++) {

                    tempProt = new IdentificationProteinBean();
                    tempProt.setAccession(identificationProteinBean.getAccession());
                    tempProt.setOtherProteins(identificationProteinBean.getOtherProteins());
                    tempProt.setProteinInferenceClass(identificationProteinBean.getProteinInferenceClass());
                    tempProt.setDescription(identificationProteinBean.getDescription());
                    tempProt.setStarred(identificationProteinBean.isStarred());
                    tempProt.setSpectrumFractionSpread_upper_range_kDa(identificationProteinBean.getSpectrumFractionSpread_upper_range_kDa());

                    tempProt.setPeptideFractionSpread_lower_range_kDa(identificationProteinBean.getPeptideFractionSpread_lower_range_kDa());

                    tempProt.setSpectrumFractionSpread_lower_range_kDa(identificationProteinBean.getSpectrumFractionSpread_lower_range_kDa());

                    tempProt.setPeptideFractionSpread_upper_range_kDa(identificationProteinBean.getPeptideFractionSpread_upper_range_kDa());

                    try {
                        tempProt.setNumberOfPeptidePerFraction(Integer.valueOf(strArr[(9 + x)]));

                    } catch (NumberFormatException e) {
                        double d = Double.valueOf(strArr[(9 + x)]);
                        tempProt.setNumberOfPeptidePerFraction((int) d);
                    }

                    try {
                        tempProt.setNumberOfSpectraPerFraction(Integer.valueOf(strArr[(9 + x + fileType)]));
                    } catch (NumberFormatException e) {
                        double d = Double.valueOf(strArr[(9 + x + fileType)]);
                        tempProt.setNumberOfSpectraPerFraction((int) d);
                    }
                    tempProt.setAveragePrecursorIntensityPerFraction(Double.valueOf(strArr[(9 + x + fileType + fileType)]));
                    IdentificationFractionBean temFb = fractionsList.get(x);
                    Map<String, IdentificationProteinBean> temProteinList = temFb.getProteinList();
                    temProteinList.put(tempProt.getAccession(), tempProt);
                    temFb.setProteinList(temProteinList);
                    fractionsList.put(x, temFb);

                }
            }

        }
        if (fileType == -7) {
            identificationDatasetBean.setgPeptideList(gPeptideList);
            return identificationDatasetBean;

        }

        if (peptideList != null && !peptideList.isEmpty()) {
            peptideList = this.addSharedPeptides(peptideList);
            identificationDatasetBean.setPeptidesNumber(this.getNumValidatedIdentificationPeptides(peptideList));
        }
        if (fractionRanges == null && fractionsList.isEmpty()) {
            identificationDatasetBean.setProteinsNumber(proteinList.size());
        }
        return identificationDatasetBean;
    }

    /**
     * extract the shared peptides data and add them as separated peptides
     *
     * @param fullIdentificationPeptideList
     *
     * @return list of shared peptides
     * @throws IOException
     * @throws SQLException
     */
    private Map<Integer, IdentificationPeptideBean> addSharedPeptides(Map<Integer, IdentificationPeptideBean> fullIdentificationPeptideList) {
        int index = fullIdentificationPeptideList.size() + 1;
        Map<Integer, IdentificationPeptideBean> updatedPeptideList = new HashMap<Integer, IdentificationPeptideBean>();
        updatedPeptideList.putAll(fullIdentificationPeptideList);
        for (IdentificationPeptideBean identificationPeptideBean : fullIdentificationPeptideList.values()) {
            if (identificationPeptideBean.getProtein().trim().equalsIgnoreCase("shared peptide")) {

                String str = identificationPeptideBean.getPeptideProteins();

                String[] strArr = str.split(",");
                for (String newProt : strArr) {
                    IdentificationPeptideBean tempIdentificationPeptideBean = new IdentificationPeptideBean();
                    tempIdentificationPeptideBean.setAaAfter(identificationPeptideBean.getAaAfter());
                    tempIdentificationPeptideBean.setAaBefore(identificationPeptideBean.getAaBefore());
                    tempIdentificationPeptideBean.setConfidence(identificationPeptideBean.getConfidence());
                    tempIdentificationPeptideBean.setFixedModification(identificationPeptideBean.getFixedModification());
                    tempIdentificationPeptideBean.setLocationConfidence(identificationPeptideBean.getLocationConfidence());
                    tempIdentificationPeptideBean.setNumberOfValidatedSpectra(identificationPeptideBean.getNumberOfValidatedSpectra());
                    tempIdentificationPeptideBean.setOtherProteinDescriptions(identificationPeptideBean.getOtherProteinDescriptions());
                    tempIdentificationPeptideBean.setPeptideEnd(identificationPeptideBean.getPeptideEnd());
                    tempIdentificationPeptideBean.setOtherProteins(identificationPeptideBean.getOtherProteins());
                    tempIdentificationPeptideBean.setPeptideId(index);
                    tempIdentificationPeptideBean.setPeptideProteins(identificationPeptideBean.getPeptideProteins());
                    tempIdentificationPeptideBean.setPeptideProteinsDescriptions(identificationPeptideBean.getPeptideProteinsDescriptions());
                    tempIdentificationPeptideBean.setPeptideStart(identificationPeptideBean.getPeptideStart());
                    tempIdentificationPeptideBean.setPrecursorCharges(identificationPeptideBean.getPrecursorCharges());
                    tempIdentificationPeptideBean.setProtein(newProt.trim());
                    tempIdentificationPeptideBean.setScore(identificationPeptideBean.getScore());
                    tempIdentificationPeptideBean.setSequence(identificationPeptideBean.getSequence());
                    tempIdentificationPeptideBean.setVariableModification(identificationPeptideBean.getVariableModification());
                    updatedPeptideList.put(index, tempIdentificationPeptideBean);
                    index++;

                }
            }
        }
        fullIdentificationPeptideList.clear();
        fullIdentificationPeptideList.putAll(updatedPeptideList);
        return updatedPeptideList;
    }

    /**
     * count the validated identification peptides within a giving list
     *
     * @param peptideList
     * @return number of identification validated peptides
     */
    private int getNumValidatedIdentificationPeptides(Map<Integer, IdentificationPeptideBean> peptideList) {
        int validatedPeptideCounter = 0;
        for (IdentificationPeptideBean pb : peptideList.values()) {
            if (pb.getValidated() == 1.0) {
                ++validatedPeptideCounter;
            }

        }
        return validatedPeptideCounter;

    }

    /**
     * read CSV file for quant combined data
     *
     * @param dataFile
     * @return
     */
    public List<QuantProtein> readCSVQuantFile(File dataFile) {
        List<QuantProtein> QuantProtList = new ArrayList<QuantProtein>();

        try {

            FileReader fr = new FileReader(dataFile);
            BufferedReader bufRdr = new BufferedReader(fr);
            String header = bufRdr.readLine();
            String[] headerArr = header.split(",");
            int index = 1;
            for (String str : headerArr) {
                System.out.println(index++ + " " + str);
            }

            int row = 1;
            String line;
            while ((line = bufRdr.readLine()) != null && row < 1000000000) {

                index = 0;
                QuantProtein qProt = new QuantProtein();
                String[] rowArr = line.split(",");

                String[] updatedRowArr = new String[headerArr.length];
                if (rowArr.length < headerArr.length) {
                    System.arraycopy(rowArr, 0, updatedRowArr, 0, rowArr.length);
                } else {
                    updatedRowArr = rowArr;
                }
                qProt.setPumedID(updatedRowArr[index++]);
                if (!updatedRowArr[index].equalsIgnoreCase("")) {
                    qProt.setQuantifiedProteinsNumber(Integer.valueOf(updatedRowArr[index]));
                } else {
                    qProt.setQuantifiedProteinsNumber(-1000000000);
                }
                index++;
                qProt.setUniprotAccession(updatedRowArr[index++]);
                qProt.setUniprotProteinName(updatedRowArr[index++]);
                qProt.setPublicationAccNumber(updatedRowArr[index++]);
                qProt.setPublicationProteinName(updatedRowArr[index++]);
                qProt.setRawDataAvailable(updatedRowArr[index++]);

                if (!updatedRowArr[index].equalsIgnoreCase("")) {

                    qProt.setPeptideIdNumb(Integer.valueOf(updatedRowArr[index]));
                } else {
                    qProt.setPeptideIdNumb(-1000000000);
                }
                index++;
                if (!updatedRowArr[index].equalsIgnoreCase("")) {
                    qProt.setQuantifiedPeptidesNumber(Integer.valueOf(updatedRowArr[index++]));
                } else {
                    qProt.setQuantifiedPeptidesNumber(-1000000000);
                    index++;
                }
                //fill peptides 
                if (!updatedRowArr[index].equalsIgnoreCase("")) { //peptide sequance 
                    qProt.setPeptideProt(true);
                } else {
                    qProt.setPeptideProt(false);
                }
                qProt.setPeptideSequance(updatedRowArr[index++]);
                qProt.setPeptideModification(updatedRowArr[index++]);
                qProt.setModificationComment(updatedRowArr[index++]);
                qProt.setTypeOfStudy(updatedRowArr[index++]);
                qProt.setSampleType(updatedRowArr[index++]);

                if (!updatedRowArr[index].equalsIgnoreCase("")) {
                    qProt.setPatientsGroupINumber(Integer.valueOf(updatedRowArr[index++]));
                } else {
                    qProt.setPatientsGroupINumber(-1000000000);
                    index++;
                }
                qProt.setPatientGroupI(updatedRowArr[index++]);
                qProt.setPatientSubGroupI(updatedRowArr[index++]);
                qProt.setPatientGrIComment(updatedRowArr[index++]);

                if (!updatedRowArr[index].equalsIgnoreCase("")) {
                    qProt.setPatientsGroupIINumber(Integer.valueOf(updatedRowArr[index++]));
                } else {
                    qProt.setPatientsGroupIINumber(-1000000000);
                    index++;
                }
                qProt.setPatientGroupII(updatedRowArr[index++]);
                qProt.setPatientSubGroupII(updatedRowArr[index++]);
                qProt.setPatientGrIIComment(updatedRowArr[index++]);

                qProt.setSampleMatching(updatedRowArr[index++]);
                qProt.setNormalizationStrategy(updatedRowArr[index++]);
                if (!updatedRowArr[index].equalsIgnoreCase("")) {
                    try {
                        qProt.setFcPatientGroupIonPatientGroupII(Double.valueOf(updatedRowArr[index]));
                        if (qProt.getFcPatientGroupIonPatientGroupII() > 0) {
                            qProt.setStringFCValue("Increased");
                        } else {
                            qProt.setStringFCValue("Decreased");
                        }
                    } catch (NumberFormatException exp) {
                        qProt.setFcPatientGroupIonPatientGroupII(-1000000000.0);
                        qProt.setStringFCValue(updatedRowArr[index]);
                    } finally {
                        index++;
                    }
                } else {
                    qProt.setFcPatientGroupIonPatientGroupII(-1000000000.0);
                    qProt.setStringFCValue(updatedRowArr[index++]);
                }

                if (!updatedRowArr[index].equalsIgnoreCase("")) {
                    try {
                        qProt.setpValue(Double.valueOf(updatedRowArr[index]));

                    } catch (NumberFormatException exp) {
                        qProt.setpValue(-1000000000.0);
                    } finally {
                        qProt.setStringPValue(updatedRowArr[index]);
                        index++;
                    }
                } else {
                    qProt.setpValue(-1000000000.0);
                    qProt.setStringPValue(updatedRowArr[index++]);
                }

                if (!updatedRowArr[index].equalsIgnoreCase("")) {
                    qProt.setRocAuc(Double.valueOf(updatedRowArr[index++]));
                } else {
                    qProt.setRocAuc(-1000000000.0);
                    index++;
                }
                qProt.setTechnology(updatedRowArr[index++]);
                qProt.setAnalyticalApproach(updatedRowArr[index++]);
                qProt.setEnzyme(updatedRowArr[index++]);
                qProt.setShotgunOrTargetedQquant(updatedRowArr[index++]);
                qProt.setQuantificationBasis(updatedRowArr[index++]);
                qProt.setQuantBasisComment(updatedRowArr[index++]);
                qProt.setAdditionalComments(updatedRowArr[index++]);

                if (qProt.isPeptideProt()) {
                    String pepKey = qProt.getPumedID() + "_" + qProt.getUniprotAccession() + "_" + qProt.getTypeOfStudy() + "_" + qProt.getAnalyticalApproach();
                    qProt.setqPeptideKey(pepKey);
                } else {
                    qProt.setqPeptideKey("");

                }
                QuantProtList.add(qProt);

            }
            System.out.println("index is " + index);
            bufRdr.close();
        } catch (IOException exp) {
            System.err.println("at error " + this.getClass() + "  line 565   " + exp.getLocalizedMessage());
        } catch (NumberFormatException exp) {
            System.err.println("at error " + this.getClass() + "  line 567   " + exp.getLocalizedMessage());
        }

        return QuantProtList;

    }
}
