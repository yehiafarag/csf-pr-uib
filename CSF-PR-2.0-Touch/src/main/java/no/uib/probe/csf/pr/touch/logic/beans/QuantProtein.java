package no.uib.probe.csf.pr.touch.logic.beans;

/**
 *
 * @author Yehia Farag
 *
 * This class represents quant protein object that contain all the protein
 * information
 */
public class QuantProtein implements Comparable<QuantProtein> {

    /*
     * Publication PubMed id
     */
    private String pubMedId;

    /*
     * Protein uniprot accession number
     */
    private String uniprotAccessionNumber;
    /*
     * Protein uniprot name
     */
    private String uniprotProteinName;
    /*
     * Protein publication accession number
     */
    private String publicationAccessionNumber;
    /*
     * Protein publication name
     */
    private String publicationProteinName;
    /*
     * Raw data available(available/not available)
     */
    private String rawDataAvailable;
    /*
     * Study type
     */
    private String typeOfStudy;
    /*
     * Sample type
     */
    private String sampleType;
    /*
     * The Disease main group I (not used in the current csf-pr-2.0)
     */
    private String diseaseMainGroupI;
    /*
     * The Disease sub group I (publication name)
     */
    private String originalDiseaseSubGroupI;
    /*
     *  The Disease main group I comments
     */
    private String diseaseMainGroupIComment;
    /*
     * The Disease main group II (not used in the current csf-pr-2.0)
     */
    private String diseaseMainGroupII;
    /*
     * The Disease sub group II (publication name)
     */
    private String originalDiseaseSubGroupII;
    /*
     *  The Disease main group II comments
     */
    private String diseaseMainGroupIIComment;
    /*
     * Sample matching
     */
    private String sampleMatching;
    /*
     * Normalization stratgy
     */
    private String normalizationStrategy;
    /*
     * Technology
     */
    private String technology;
    /*
     * Analytical approach
     */
    private String analyticalApproach;
    /*
     * Enzyme
     */
    private String enzyme;
    /*
     * Shotgun or targetedQquant study
     */
    private String shotgunOrTargetedQquant;
    /*
     * Protein quantification basis
     */
    private String quantificationBasis;
    /*
     * Protein quantification basis comments
     */
    private String quantBasisComment;
    /*
     * Protein additional comments
     */
    private String additionalComments;

    /*
     * Number of identified peptides
     */
    private int peptideIdNumb;
    /*
     * number of quantified peptides
     */
    private int quantifiedPeptidesNumber;
    /*
     * Disease main group I patients number
     */
    private int diseaseGroupIPatientsNumber;
    /*
     * Disease main group II patients number
     */
    private int diseaseGroupIIPatientsNumber;
    /*
     * Publication publishing year
     */
    private int year;

    /*
     * Unique parent dataset id (dataset index in database)
     */
    private int quantDatasetIndex;
    /*
     * Unique protein id (protein index in database)
     */
    private int protIndex;

    /*
     * Fold change as text (increased, decreased or equal)
     */
    private String string_fc_value;
    /*
     * Quantification p_value (Significant/not significant) 
     */
    private String string_p_value;
    /*
     * The publication author name
     */
    private String author;
    /*
     *  Peptide p_value comments
     */
    private String p_value_comments;
    /*
     * Quantification p_value significance threshold
     */
    private String pvalueSignificanceThreshold;
    /*
     * Disease category (MS,AD,PD...etc)
     */
    private String diseaseCategory;
    /*
     * Link to protein in UniProt
     */
    private String url;
    /*
     * The final accession (Uniprot or publication) will be used in the protein table
     */
    private String finalAccession;

    /*
     * The protein sequence imported from UniProt
     */
    private String sequence;

    /**
     * Get the final accession (Uniprot or publication) will be used in the
     * protein table
     *
     * @return finalAccession
     */
    public String getFinalAccession() {
        return finalAccession;
    }
    /*
     * Quantification pValue
     */
    private double p_value;

    /*
     * Receiver operating characteristic
     */
    private double roc_auc;
    /*
     * Fold change value (log2 value)
     */
    private double fc_value;

    /*
     * Protein has peptides data
     */
    private boolean peptideProt;

    /**
     * Set the final accession (Uniprot or publication) will be used in the
     * protein table
     *
     * @param finalAccession
     */
    public void setFinalAccession(String finalAccession) {
        this.finalAccession = finalAccession;
    }

    /**
     * Get disease category (MS,AD,PD...etc)
     *
     * @return diseaseCategory
     */
    public String getDiseaseCategory() {
        return diseaseCategory;
    }

    /**
     * Set disease category (MS,AD,PD...etc)
     *
     * @param diseaseCategory
     */
    public void setDiseaseCategory(String diseaseCategory) {
        this.diseaseCategory = diseaseCategory;
    }

    /**
     * get protein sequence
     *
     * @return sequence
     */
    public String getSequence() {
        return sequence;
    }

    /**
     * Set protein sequence
     *
     * @param sequence
     */
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    /**
     * Get he dataset index in the database
     *
     * @return quantDatasetIndex
     */
    public int getQuantDatasetIndex() {
        return quantDatasetIndex;
    }

    /**
     * Get quantification p_value comments
     *
     * @return p_value_comments
     */
    public String getP_value_comments() {
        return p_value_comments;
    }

    /**
     * Set quantification p_value comments
     *
     * @param p_value_comments
     */
    public void setP_value_comments(String p_value_comments) {
        this.p_value_comments = p_value_comments;
    }

    /**
     * Get unique protein id (protein index in database)
     *
     * @return protIndex
     */
    public int getProtIndex() {
        return protIndex;
    }

    /**
     * Set unique protein id (protein index in database)
     *
     * @param protIndex
     */
    public void setProtIndex(int protIndex) {
        this.protIndex = protIndex;
    }

    /**
     * Set the dataset index in the database
     *
     * @param quantDatasetIndex
     */
    public void setQuantDatasetIndex(int quantDatasetIndex) {
        this.quantDatasetIndex = quantDatasetIndex;
    }

    /**
     * Get quantification pValue (Significant/not significant)
     *
     * @return string_p_value
     */
    public String getString_p_value() {
        return string_p_value;
    }

    /**
     * Set quantification pValue (Significant/not significant)
     *
     * @param string_p_value
     */
    public void setString_p_value(String string_p_value) {
        this.string_p_value = string_p_value;
    }

    /**
     * Get the publication publishing year
     *
     * @return year
     */
    public int getYear() {
        return year;
    }

    /**
     * Set the publication publishing year
     *
     * @param year
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Get the publication author name
     *
     * @return author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Set the publication author name
     *
     * @param author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Get fold change as text (increased, decreased or equal)
     *
     * @return string_fc_value
     */
    public String getString_fc_value() {
        return string_fc_value;
    }

    /**
     * Set fold change as text (increased, decreased or equal)
     *
     * @param string_fc_value
     */
    public void setString_fc_value(String StringFC) {
        this.string_fc_value = StringFC;
    }

    /**
     * Is protein has peptides
     *
     * @return peptideProt
     */
    public boolean isPeptideProt() {
        return peptideProt;
    }

    /**
     * Set protein has peptides
     *
     * @param peptideProt
     */
    public void setPeptideProt(boolean peptideProt) {
        this.peptideProt = peptideProt;
    }

    /**
     * Get publication PubMed id
     *
     * @return QuantDataset
     */
    public String getPubMedId() {
        return pubMedId;
    }

    /**
     * Set publication PubMed id
     *
     * @param pubMedId
     */
    public void setPubMedId(String pubMedId) {
        this.pubMedId = pubMedId;
    }

    /**
     * Get uniprot protein accession number
     *
     * @return uniprotAccessionNumber
     */
    public String getUniprotAccessionNumber() {
        return uniprotAccessionNumber;
    }

    /**
     * Set uniprot protein accession number
     *
     * @param uniprotAccessionNumber
     */
    public void setUniprotAccessionNumber(String uniprotAccessionNumber) {
        this.uniprotAccessionNumber = uniprotAccessionNumber;
    }

    /**
     * Get uniprot protein name
     *
     * @return uniprotProteinName
     */
    public String getUniprotProteinName() {
        return uniprotProteinName;
    }

    /**
     * Set uniprot protein name
     *
     * @param uniprotProteinName
     */
    public void setUniprotProteinName(String uniprotProteinName) {
        this.uniprotProteinName = uniprotProteinName;
    }

    /**
     * Get publication (IPI..etc) protein accession number
     *
     * @return publicationAccessionNumber
     */
    public String getPublicationAccessionNumber() {
        return publicationAccessionNumber;
    }

    /**
     * Set publication (IPI..etc) protein accession number
     *
     * @param publicationAccessionNumber
     */
    public void setPublicationAccessionNumber(String publicationAccessionNumber) {
        this.publicationAccessionNumber = publicationAccessionNumber;
    }

    /**
     * Get publication protein name
     *
     * @return publicationProteinName
     */
    public String getPublicationProteinName() {
        return publicationProteinName;
    }

    /**
     * Set publication protein name
     *
     * @param publicationProteinName
     */
    public void setPublicationProteinName(String publicationProteinName) {
        this.publicationProteinName = publicationProteinName;
    }

    /**
     * Get raw data available(available/not available)
     *
     * @return rawDataAvailable
     */
    public String getRawDataAvailable() {
        return rawDataAvailable;
    }

    /**
     * Set raw data available
     *
     * @param rawDataAvailable
     */
    public void setRawDataAvailable(String rawDataAvailable) {
        this.rawDataAvailable = rawDataAvailable;
    }

    /**
     * Get number of identified peptides
     *
     * @return peptideIdNumb
     */
    public int getPeptideIdNumb() {
        return peptideIdNumb;
    }

    /**
     * Set number of identified peptides
     *
     * @param peptideIdNumb
     */
    public void setPeptideIdNumb(int peptideIdNumb) {
        this.peptideIdNumb = peptideIdNumb;
    }

    /**
     * Get number of quantified peptides
     *
     * @return quantifiedPeptidesNumber
     */
    public int getQuantifiedPeptidesNumber() {
        return quantifiedPeptidesNumber;
    }

    /**
     * Set number of quantified peptides
     *
     * @param quantifiedPeptidesNumber
     */
    public void setQuantifiedPeptidesNumber(int quantifiedPeptidesNumber) {
        this.quantifiedPeptidesNumber = quantifiedPeptidesNumber;
    }

    /**
     * Get fold change as text (increased, decreased or equal)
     *
     * @return string_fc_value
     */
    public double getFc_value() {
        return fc_value;
    }

    /**
     * Set fold change as text (increased, decreased or equal)
     *
     * @param string_fc_value
     */
    public void setFc_value(double fc_value) {
        this.fc_value = fc_value;
    }

    /**
     * Get study type
     *
     * @return typeOfStudy
     */
    public String getTypeOfStudy() {
        return typeOfStudy;
    }

    /**
     * Set study type
     *
     * @param typeOfStudy
     */
    public void setTypeOfStudy(String typeOfStudy) {
        this.typeOfStudy = typeOfStudy;
    }

    /**
     * Get sample type
     *
     * @return sampleType
     */
    public String getSampleType() {
        return sampleType;
    }

    /**
     * Set sample type
     *
     * @param sampleType
     */
    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    /**
     * Get disease main group I patients number
     *
     * @return diseaseGroupIPatientsNumber
     */
    public int getDiseaseGroupIPatientsNumber() {
        return diseaseGroupIPatientsNumber;
    }

    /**
     * Set disease main group I patients number
     *
     * @param diseaseGroupIPatientsNumber
     */
    public void setDiseaseGroupIPatientsNumber(int diseaseGroupIPatientsNumber) {
        this.diseaseGroupIPatientsNumber = diseaseGroupIPatientsNumber;
    }

    /**
     * Get the disease main group I (not used in the current csf-pr-2.0)
     *
     * @return diseaseMainGroupI
     */
    public String getDiseaseMainGroupI() {
        return diseaseMainGroupI;
    }

    /**
     * Set the disease main group I (not used in the current csf-pr-2.0)
     *
     * @param diseaseMainGroupI
     */
    public void setDiseaseMainGroupI(String diseaseMainGroupI) {
        this.diseaseMainGroupI = diseaseMainGroupI;
    }

    /**
     * Get the disease sub group I (publication name)
     *
     * @return originalDiseaseSubGroupI
     */
    public String getOriginalDiseaseSubGroupI() {
        return originalDiseaseSubGroupI;
    }

    /**
     * Set the disease sub group I (publication name)
     *
     * @param originalDiseaseSubGroupI
     */
    public void setOriginalDiseaseSubGroupI(String originalDiseaseSubGroupI) {
        this.originalDiseaseSubGroupI = originalDiseaseSubGroupI;
    }

    /**
     * Get disease main group I comments
     *
     * @return diseaseMainGroupIComment
     */
    public String getDiseaseMainGroupIComment() {
        return diseaseMainGroupIComment;
    }

    /**
     * Set disease main group I comments
     *
     * @param diseaseMainGroupIComment
     */
    public void setDiseaseMainGroupIComment(String diseaseMainGroupIComment) {
        this.diseaseMainGroupIComment = diseaseMainGroupIComment;
    }

    /**
     * Set Disease main group II patients number
     *
     * @return diseaseGroupIIPatientsNumber
     */
    public int getDiseaseGroupIIPatientsNumber() {
        return diseaseGroupIIPatientsNumber;
    }

    /**
     * Set Disease main group II patients number
     *
     * @param diseaseGroupIIPatientsNumber
     */
    public void setDiseaseGroupIIPatientsNumber(int diseaseGroupIIPatientsNumber) {
        this.diseaseGroupIIPatientsNumber = diseaseGroupIIPatientsNumber;
    }

    /**
     * Get the disease main group II (not used in the current csf-pr-2.0)
     *
     * @return diseaseMainGroupII
     */
    public String getDiseaseMainGroupII() {
        return diseaseMainGroupII;
    }

    /**
     * Set the disease main group II (not used in the current csf-pr-2.0)
     *
     * @param diseaseMainGroupII
     */
    public void setDiseaseMainGroupII(String diseaseMainGroupII) {
        this.diseaseMainGroupII = diseaseMainGroupII;
    }

    /**
     * Get the disease sub group II (publication name)
     *
     * @param originalDiseaseSubGroupII
     */
    public String getOriginalDiseaseSubGroupII() {
        return originalDiseaseSubGroupII;
    }

    /**
     * Set the disease sub group II (publication name)
     *
     * @param originalDiseaseSubGroupII
     */
    public void setOriginalDiseaseSubGroupII(String originalDiseaseSubGroupII) {
        this.originalDiseaseSubGroupII = originalDiseaseSubGroupII;
    }

    /**
     * Get disease main group II comments
     *
     * @return diseaseMainGroupIIComment
     */
    public String getDiseaseMainGroupIIComment() {
        return diseaseMainGroupIIComment;
    }

    /**
     * Set Disease main group II comments
     *
     * @param diseaseMainGroupIIComment
     */
    public void setDiseaseMainGroupIIComment(String diseaseMainGroupIIComment) {
        this.diseaseMainGroupIIComment = diseaseMainGroupIIComment;
    }

    /**
     * Get sample matching
     *
     * @return sampleMatching
     */
    public String getSampleMatching() {
        return sampleMatching;
    }

    /**
     * Set sample matching
     *
     * @param sampleMatching
     */
    public void setSampleMatching(String sampleMatching) {
        this.sampleMatching = sampleMatching;
    }

    /**
     * Get normalization strategy
     *
     * @return normalizationStrategy
     */
    public String getNormalizationStrategy() {
        return normalizationStrategy;
    }

    /**
     * Set normalization strategy
     *
     * @param normalizationStrategy
     */
    public void setNormalizationStrategy(String normalizationStrategy) {
        this.normalizationStrategy = normalizationStrategy;
    }

    /**
     * Get quantification pValue(actual value if available)
     *
     * @return p_value
     */
    public double getP_value() {
        return p_value;
    }

    /**
     * Set quantification pValue(actual value if available)
     *
     * @param p_value
     */
    public void setP_value(double p_value) {
        this.p_value = p_value;
    }

    /**
     * Get receiver operating characteristic value
     *
     * @return roc_auc
     */
    public double getRoc_auc() {
        return roc_auc;
    }

    /**
     * Set receiver operating characteristic value
     *
     * @param roc_auc
     */
    public void setRoc_auc(double roc_auc) {
        this.roc_auc = roc_auc;
    }

    /**
     * Get technology
     *
     * @return technology
     */
    public String getTechnology() {
        return technology;
    }

    /**
     * Set technology
     *
     * @param technology
     */
    public void setTechnology(String technology) {
        this.technology = technology;
    }

    /**
     * Get analytical approach
     *
     * @return analyticalApproach
     */
    public String getAnalyticalApproach() {
        return analyticalApproach;
    }

    /**
     * Set analytical approach
     *
     * @param analyticalApproach
     */
    public void setAnalyticalApproach(String analyticalApproach) {
        this.analyticalApproach = analyticalApproach;
    }

    /**
     * Get enzyme used
     *
     * @return enzyme
     */
    public String getEnzyme() {
        return enzyme;
    }

    /**
     * Set enzyme used
     *
     * @param enzyme
     */
    public void setEnzyme(String enzyme) {
        this.enzyme = enzyme;
    }

    /**
     * Get Shotgun / Targeted
     *
     * @return shotgunTargeted
     */
    public String getShotgunOrTargetedQquant() {
        return shotgunOrTargetedQquant;
    }

    /**
     * Set Shotgun / Targeted
     *
     * @param shotgunTargeted
     */
    public void setShotgunOrTargetedQquant(String shotgunOrTargetedQquant) {
        this.shotgunOrTargetedQquant = shotgunOrTargetedQquant;
    }

    /**
     * Get quantification basis
     *
     * @return quantificationBasis
     */
    public String getQuantificationBasis() {
        return quantificationBasis;
    }

    /**
     * Set quantification basis
     *
     * @param quantificationBasis
     */
    public void setQuantificationBasis(String quantificationBasis) {
        this.quantificationBasis = quantificationBasis;
    }

    /**
     * Get quantification basis comments (protein level) the quant bases
     * comments are different in protein and peptides level
     *
     * @return quantBasisComment
     */
    public String getQuantBasisComment() {
        return quantBasisComment;
    }

    /**
     * Set quantification basis comments (protein level) the quant bases
     * comments are different in protein and peptides level
     *
     * @param quantBasisComment
     */
    public void setQuantBasisComment(String quantBasisComment) {
        this.quantBasisComment = quantBasisComment;
    }

    /**
     * Get protein additional comments
     *
     * @return additionalComments
     */
    public String getAdditionalComments() {
        return additionalComments;
    }

    /**
     * Set protein additional comments
     *
     * @param additionalComments
     */
    public void setAdditionalComments(String additionalComments) {
        this.additionalComments = additionalComments;
    }

    /**
     * Get quantification pValue significance threshold
     *
     * @return pvalueSignificanceThreshold
     */
    public String getPvalueSignificanceThreshold() {
        return pvalueSignificanceThreshold;
    }

    /**
     * Set quantification pValue significance threshold
     *
     * @param pvalueSignificanceThreshold
     */
    public void setPvalueSignificanceThreshold(String pvalueSignificanceThreshold) {
        this.pvalueSignificanceThreshold = pvalueSignificanceThreshold;
    }

    @Override
    public int compareTo(QuantProtein o) {
        if ((this.getDiseaseGroupIPatientsNumber() + this.getDiseaseGroupIIPatientsNumber()) > (o.getDiseaseGroupIPatientsNumber() + o.getDiseaseGroupIIPatientsNumber())) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * Get link to protein in UniProt
     *
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Set link to protein in UniProt
     *
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

}
