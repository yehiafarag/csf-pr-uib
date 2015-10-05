/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.model.beans.quant;

/**
 *
 * @author Yehia Farag
 */
public class QuantProtein {
    private String pumedID,uniprotAccession, uniprotProteinName, publicationAccNumber,publicationProteinName,rawDataAvailable,typeOfStudy,sampleType,patientGroupI,patientSubGroupI,patientGrIComment,patientGroupII,patientSubGroupII,patientGrIIComment,sampleMatching,normalizationStrategy,technology,analyticalApproach,enzyme,shotgunOrTargetedQquant,quantificationBasis,quantBasisComment,additionalComments;		
    private int quantifiedProteinsNumber,peptideIdNumb,quantifiedPeptidesNumber, patientsGroupINumber,patientsGroupIINumber,year,filesNum,IdentifiedProteinsNum, dsKey,protKey;    
    private String qPeptideKey,    peptideSequance,	peptideModification,modificationComment , stringFCValue,stringPValue,author,diseaseGroups,pvalueComment,pvalueSignificanceThreshold; 
private boolean uniprotAccSource;

    public boolean isUniprotAccSource() {
        return uniprotAccSource;
    }

    public void setUniprotAccSource(boolean uniprotAccSource) {
        this.uniprotAccSource = uniprotAccSource;
    }
    private String sequance;
   
    /**
     *
     * @return
     */
    public String getSequance() {
        return sequance;
    }

    /**
     *
     * @param sequance
     */
    public void setSequance(String sequance) {
        this.sequance = sequance;
    }

    /**
     *
     * @return
     */
    public int getDsKey() {
        return dsKey;
    }

  

    /**
     *
     * @return
     */
    public String getPvalueComment() {
        return pvalueComment;
    }

    /**
     *
     * @param pvalueComment
     */
    public void setPvalueComment(String pvalueComment) {
        this.pvalueComment = pvalueComment;
    }

    /**
     *
     * @return
     */
    public int getProtKey() {
        return protKey;
    }

    /**
     *
     * @param protKey
     */
    public void setProtKey(int protKey) {
        this.protKey = protKey;
    }

    /**
     *
     * @param dsKey
     */
    public void setDsKey(int dsKey) {
        this.dsKey = dsKey;
    }
    
    /**
     *
     * @return
     */
    public String getStringPValue() {
        return stringPValue;
    }

    /**
     *
     * @param stringPValue
     */
    public void setStringPValue(String stringPValue) {
        this.stringPValue = stringPValue;
    }
    private double pValue,rocAuc;
    private double fcPatientGroupIonPatientGroupII;
    private boolean peptideProt;

    /**
     *
     * @return
     */
    public int getYear() {
        return year;
    }

    /**
     *
     * @param year
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     *
     * @return
     */
    public int getFilesNum() {
        return filesNum;
    }

    /**
     *
     * @param filesNum
     */
    public void setFilesNum(int filesNum) {
        this.filesNum = filesNum;
    }

    /**
     *
     * @return
     */
    public int getIdentifiedProteinsNum() {
        return IdentifiedProteinsNum;
    }

    /**
     *
     * @param IdentifiedProteinsNum
     */
    public void setIdentifiedProteinsNum(int IdentifiedProteinsNum) {
        this.IdentifiedProteinsNum = IdentifiedProteinsNum;
    }

    /**
     *
     * @return
     */
    public String getAuthor() {
        return author;
    }

    /**
     *
     * @param author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     *
     * @return
     */
    public String getDiseaseGroups() {
        return diseaseGroups;
    }

    /**
     *
     * @param diseaseGroups
     */
    public void setDiseaseGroups(String diseaseGroups) {
        this.diseaseGroups = diseaseGroups;
    }

    /**
     *
     * @return
     */
    public String getStringFCValue() {
        return stringFCValue;
    }

    /**
     *
     * @param StringFC
     */
    public void setStringFCValue(String StringFC) {
        this.stringFCValue = StringFC;
    }

    /**
     *
     * @return
     */
    public boolean isPeptideProt() {
        return peptideProt;
    }

    /**
     *
     * @param peptideProt
     */
    public void setPeptideProt(boolean peptideProt) {
        this.peptideProt = peptideProt;
    }

    /**
     *
     * @return
     */
    public String getPumedID() {
        return pumedID;
    }

    /**
     *
     * @param pumedID
     */
    public void setPumedID(String pumedID) {
        this.pumedID = pumedID;
    }
        
    /**
     *
     * @return
     */
    public int getQuantifiedProteinsNumber() {
        return quantifiedProteinsNumber;
    }

    /**
     *
     * @param quantifiedProteinsNumber
     */
    public void setQuantifiedProteinsNumber(int quantifiedProteinsNumber) {
        this.quantifiedProteinsNumber = quantifiedProteinsNumber;
    }
    
    /**
     *
     * @return
     */
    public String getUniprotAccession() {
        return uniprotAccession;
    }

    /**
     *
     * @param uniprotAccession
     */
    public void setUniprotAccession(String uniprotAccession) {
        this.uniprotAccession = uniprotAccession;
    }

    /**
     *
     * @return
     */
    public String getUniprotProteinName() {
        return uniprotProteinName;
    }

    /**
     *
     * @param uniprotProteinName
     */
    public void setUniprotProteinName(String uniprotProteinName) {
        this.uniprotProteinName = uniprotProteinName;
    }
    
    /**
     *
     * @return
     */
    public String getPublicationAccNumber() {
        return publicationAccNumber;
    }

    /**
     *
     * @param publicationAccNumber
     */
    public void setPublicationAccNumber(String publicationAccNumber) {
        this.publicationAccNumber = publicationAccNumber;
    }

    /**
     *
     * @return
     */
    public String getPublicationProteinName() {
        return publicationProteinName;
    }

    /**
     *
     * @param publicationProteinName
     */
    public void setPublicationProteinName(String publicationProteinName) {
        this.publicationProteinName = publicationProteinName;
    }
    
    /**
     *
     * @return
     */
    public String getRawDataAvailable() {
        return rawDataAvailable;
    }

    /**
     *
     * @param rawDataAvailable
     */
    public void setRawDataAvailable(String rawDataAvailable) {
        this.rawDataAvailable = rawDataAvailable;
    }
    
    /**
     *
     * @return
     */
    public int getPeptideIdNumb() {
        return peptideIdNumb;
    }

    /**
     *
     * @param peptideIdNumb
     */
    public void setPeptideIdNumb(int peptideIdNumb) {
        this.peptideIdNumb = peptideIdNumb;
    }
    
    /**
     *
     * @return
     */
    public int getQuantifiedPeptidesNumber() {
        return quantifiedPeptidesNumber;
    }

    /**
     *
     * @param quantifiedPeptidesNumber
     */
    public void setQuantifiedPeptidesNumber(int quantifiedPeptidesNumber) {
        this.quantifiedPeptidesNumber = quantifiedPeptidesNumber;
    }
     
    //move to peptideProt?? 
    
    // dublicate in both??

    /**
     *
     * @return
     */
         public double getFcPatientGroupIonPatientGroupII() {
        return fcPatientGroupIonPatientGroupII;
    }

    /**
     *
     * @param fcPatientGroupIonPatientGroupII
     */
    public void setFcPatientGroupIonPatientGroupII(double fcPatientGroupIonPatientGroupII) {
        this.fcPatientGroupIonPatientGroupII = fcPatientGroupIonPatientGroupII;
    }
    
     //can dublicate in both??

    /**
     *
     * @return
     */
         public String getTypeOfStudy() {
        return typeOfStudy;
    }

    /**
     *
     * @param typeOfStudy
     */
    public void setTypeOfStudy(String typeOfStudy) {
        this.typeOfStudy = typeOfStudy;
    }

    /**
     *
     * @return
     */
    public String getSampleType() {
        return sampleType;
    }

    /**
     *
     * @param sampleType
     */
    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    /**
     *
     * @return
     */
    public int getPatientsGroupINumber() {
        return patientsGroupINumber;
    }

    /**
     *
     * @param patientsGroupINumber
     */
    public void setPatientsGroupINumber(int patientsGroupINumber) {
        this.patientsGroupINumber = patientsGroupINumber;
    }

    /**
     *
     * @return
     */
    public String getPatientGroupI() {
        return patientGroupI;
    }

    /**
     *
     * @param patientGroupI
     */
    public void setPatientGroupI(String patientGroupI) {
        this.patientGroupI = patientGroupI;
    }

    /**
     *
     * @return
     */
    public String getPatientSubGroupI() {
        return patientSubGroupI;
    }

    /**
     *
     * @param patientSubGroupI
     */
    public void setPatientSubGroupI(String patientSubGroupI) {
        this.patientSubGroupI = patientSubGroupI;
    }

    /**
     *
     * @return
     */
    public String getPatientGrIComment() {
        return patientGrIComment;
    }

    /**
     *
     * @param patientGrIComment
     */
    public void setPatientGrIComment(String patientGrIComment) {
        this.patientGrIComment = patientGrIComment;
    }

    /**
     *
     * @return
     */
    public int getPatientsGroupIINumber() {
        return patientsGroupIINumber;
    }

    /**
     *
     * @param patientsGroupIINumber
     */
    public void setPatientsGroupIINumber(int patientsGroupIINumber) {
        this.patientsGroupIINumber = patientsGroupIINumber;
    }
    
    /**
     *
     * @return
     */
    public String getPatientGroupII() {
        return patientGroupII;
    }

    /**
     *
     * @param patientGroupII
     */
    public void setPatientGroupII(String patientGroupII) {
        this.patientGroupII = patientGroupII;
    }

    /**
     *
     * @return
     */
    public String getPatientSubGroupII() {
        return patientSubGroupII;
    }

    /**
     *
     * @param patientSubGroupII
     */
    public void setPatientSubGroupII(String patientSubGroupII) {
        this.patientSubGroupII = patientSubGroupII;
    }

    /**
     *
     * @return
     */
    public String getPatientGrIIComment() {
        return patientGrIIComment;
    }

    /**
     *
     * @param patientGrIIComment
     */
    public void setPatientGrIIComment(String patientGrIIComment) {
        this.patientGrIIComment = patientGrIIComment;
    }

    /**
     *
     * @return
     */
    public String getSampleMatching() {
        return sampleMatching;
    }

    /**
     *
     * @param sampleMatching
     */
    public void setSampleMatching(String sampleMatching) {
        this.sampleMatching = sampleMatching;
    }

    /**
     *
     * @return
     */
    public String getNormalizationStrategy() {
        return normalizationStrategy;
    }

    /**
     *
     * @param normalizationStrategy
     */
    public void setNormalizationStrategy(String normalizationStrategy) {
        this.normalizationStrategy = normalizationStrategy;
    }

    /**
     *
     * @return
     */
    public double getpValue() {
        return pValue;
    }

    /**
     *
     * @param pValue
     */
    public void setpValue(double pValue) {
        this.pValue = pValue;
    }

    /**
     *
     * @return
     */
    public double getRocAuc() {
        return rocAuc;
    }

    /**
     *
     * @param rocAuc
     */
    public void setRocAuc(double rocAuc) {
        this.rocAuc = rocAuc;
    }

    /**
     *
     * @return
     */
    public String getTechnology() {
        return technology;
    }

    /**
     *
     * @param technology
     */
    public void setTechnology(String technology) {
        this.technology = technology;
    }

    /**
     *
     * @return
     */
    public String getAnalyticalApproach() {
        return analyticalApproach;
    }

    /**
     *
     * @param analyticalApproach
     */
    public void setAnalyticalApproach(String analyticalApproach) {
        this.analyticalApproach = analyticalApproach;
    }

    /**
     *
     * @return
     */
    public String getEnzyme() {
        return enzyme;
    }

    /**
     *
     * @param enzyme
     */
    public void setEnzyme(String enzyme) {
        this.enzyme = enzyme;
    }

    /**
     *
     * @return
     */
    public String getShotgunOrTargetedQquant() {
        return shotgunOrTargetedQquant;
    }

    /**
     *
     * @param shotgunOrTargetedQquant
     */
    public void setShotgunOrTargetedQquant(String shotgunOrTargetedQquant) {
        this.shotgunOrTargetedQquant = shotgunOrTargetedQquant;
    }
   
    /**
     *
     * @return
     */
    public String getQuantificationBasis() {
        return quantificationBasis;
    }

    /**
     *
     * @param quantificationBasis
     */
    public void setQuantificationBasis(String quantificationBasis) {
        this.quantificationBasis = quantificationBasis;
    }

    /**
     *
     * @return
     */
    public String getQuantBasisComment() {
        return quantBasisComment;
    }

    /**
     *
     * @param quantBasisComment
     */
    public void setQuantBasisComment(String quantBasisComment) {
        this.quantBasisComment = quantBasisComment;
    }

    /**
     *
     * @return
     */
    public String getAdditionalComments() {
        return additionalComments;
    }

    /**
     *
     * @param additionalComments
     */
    public void setAdditionalComments(String additionalComments) {
        this.additionalComments = additionalComments;
    }
    
    /**
     *
     * @return
     */
    public String getPeptideSequance() {
        return peptideSequance;
    }

    /**
     *
     * @param peptideSequance
     */
    public void setPeptideSequance(String peptideSequance) {
        this.peptideSequance = peptideSequance;
    }

    /**
     *
     * @return
     */
    public String getPeptideModification() {
        return peptideModification;
    }

    /**
     *
     * @param peptideModification
     */
    public void setPeptideModification(String peptideModification) {
        this.peptideModification = peptideModification;
    }

    /**
     *
     * @return
     */
    public String getModificationComment() {
        return modificationComment;
    }

    /**
     *
     * @param modificationComment
     */
    public void setModificationComment(String modificationComment) {
        this.modificationComment = modificationComment;
    }

    /**
     *
     * @return
     */
    public String getqPeptideKey() {
        return qPeptideKey;
    }

    /**
     *
     * @param qPeptideKey
     */
    public void setqPeptideKey(String qPeptideKey) {
        this.qPeptideKey = qPeptideKey;
    }

    public String getPvalueSignificanceThreshold() {
        return pvalueSignificanceThreshold;
    }

    public void setPvalueSignificanceThreshold(String pvalueSignificanceThreshold) {
        this.pvalueSignificanceThreshold = pvalueSignificanceThreshold;
    }
    
 
     
   

    

    

   

   



    
    

    
    

    

  
    


   
   

   
    
}
