package no.uib.probe.csf.pr.touch.logic.beans;

import java.io.Serializable;

/**
 * @author Yehia Farag
 *
 * This class represents quant peptide object that contain all the peptide
 * information
 */
public class QuantPeptide implements Serializable {

    /*
     * Unique peptide id (peptide index in database)
     */
    private int uniqueId;
    /*
     * Unique parent protein id (protein index in database)
     */
    private int protIndex;
    /*
     * Unique parent dataset id (dataset index in database)
     */
    private int quantDatasetIndex;
    /*
     * Peptide sequence
     */
    private String peptideSequence;
    /*
     * Peptide annotated  sequence
     */
    private String sequenceAnnotated;
    /*
     * Peptide comments 
     */
    private String additionalComments;

    /*
     * Quantification pValue significance threshold
     */
    private String pvalueSignificanceThreshold;
    /*
     * Quantification peptide charge
     */
    private int peptideCharge;
    /*
     * Quantification pValue (Significant/not significant) 
     */
    private String string_p_value;
    /*
     * Peptide modification
     */
    private String peptideModification;
    /*
     * Peptide modification comments
     */
    private String modification_comment;
    /*
     * Peptide quantification basis comments
     */
    private String quantBasisComment;

    /*
     * Peptide quantification basis
     */
    private String quantificationBasis;
    /*
     * Peptide unique signuture
     */
    private String peptideSignature;

    /*
     * Peptide pValue comments
     */
    private String p_value_comments;

    /*
     * Parent protein uniprot accession number
     */
    private String uniprotAccessionNumber;
    /*
     * Parent protein publication accession number
     */
    private String publicationAccessionNumber;
    /*
     * Parent protein uniprot name
     */
    private String uniprotProteinName;
    /*
     * Parent protein publication name
     */
    private String publicationProteinName;
    /*
     * Quantification pValue
     */
    private double p_value;
    /*
     * Fold change as text (increased, decreased or equal)
     */
    private String string_fc_value;
    
    /*
     * Receiver operating characteristic
     */
    private double roc_auc;
    /*
     * Fold change value (log2 value)
     */
    private double fc_value;

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
     * Get peptide signature
     *
     * "__" + proteinIndex+ "__" + datasetIndex + "__"
     *
     * @return peptideSignature
     */
    public String getPeptideSignature() {
        return peptideSignature;
    }

    /**
     * Set peptide signature
     *
     * "__" + proteinIndex+ "__" + datasetIndex + "__"
     *
     * @param peptideSignature
     */
    public void setPeptideSignature(String peptideSignature) {
        this.peptideSignature = peptideSignature;
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
     * Get peptide annotated sequence
     *
     * @return sequenceAnnotated
     */
    public String getSequenceAnnotated() {
        return sequenceAnnotated;
    }

    /**
     * Set peptide annotated sequence
     *
     * @param sequenceAnnotated
     */
    public void setSequenceAnnotated(String sequenceAnnotated) {
        this.sequenceAnnotated = sequenceAnnotated;
    }

    /**
     * Get peptide additional comments
     *
     * @return additionalComments
     */
    public String getAdditionalComments() {
        return additionalComments;
    }

    /**
     * Set peptide additional comments
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

    /**
     * Get quantified peptide charge
     *
     * @return peptideCharge
     */
    public int getPeptideCharge() {
        return peptideCharge;
    }

    /**
     * Set quantified peptide charge
     *
     * @param peptideCharge
     */
    public void setPeptideCharge(int peptideCharge) {
        this.peptideCharge = peptideCharge;
    }

    /**
     * Get unique peptide id (peptide index in database)
     *
     * @return uniqueId
     */
    public int getUniqueId() {
        return uniqueId;
    }

    /**
     * Set unique peptide id (peptide index in database)
     *
     * @param uniqueId
     */
    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    /**
     * Get unique parent protein id (protein index in database)
     *
     * @return protIndex
     */
    public int getProtIndex() {
        return protIndex;
    }

    /**
     * Set unique parent protein id (protein index in database)
     *
     * @param protIndex
     */
    public void setProtIndex(int protIndex) {
        this.protIndex = protIndex;
    }

    /**
     * Get unique parent dataset id (dataset index in database)
     *
     * @return quantDatasetIndex
     */
    public int getQuantDatasetIndex() {
        return quantDatasetIndex;
    }

    /**
     * Set unique parent dataset id (dataset index in database)
     *
     * @param quantDatasetIndex
     */
    public void setQuantDatasetIndex(int quantDatasetIndex) {
        this.quantDatasetIndex = quantDatasetIndex;
    }

    /**
     * get peptide sequence
     *
     * @return peptideSequence
     */
    public String getPeptideSequence() {
        return peptideSequence;
    }

    /**
     * Set peptide sequence
     *
     * @param peptideSequence
     */
    public void setPeptideSequence(String peptideSequence) {
        this.peptideSequence = peptideSequence;
    }

    /**
     * Get peptide modification
     *
     * @return peptideModification
     */
    public String getPeptideModification() {
        return peptideModification;
    }

    /**
     * Set peptide modification
     *
     * @param peptideModification
     */
    public void setPeptideModification(String peptideModification) {
        this.peptideModification = peptideModification;
    }

    /**
     * Get peptide modification comments
     *
     * @return modification_comment
     */
    public String getModification_comment() {
        return modification_comment;
    }

    /**
     * Set peptide modification comments
     *
     * @param modification_comment
     */
    public void setModification_comment(String modification_comment) {
        this.modification_comment = modification_comment;
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
    public void setString_fc_value(String string_fc_value) {
        this.string_fc_value = string_fc_value;
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
     * Get quantification pValue comments
     *
     * @return p_value_comments
     */
    public String getP_value_comments() {
        return p_value_comments;
    }

    /**
     * Set quantification pValue comments
     *
     * @param p_value_comments
     */
    public void setP_value_comments(String p_value_comments) {
        this.p_value_comments = p_value_comments;
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
     *Get receiver operating characteristic value
     * @return roc_auc
     */
    public double getRoc_auc() {
        return roc_auc;
    }

    /**
     *Set receiver operating characteristic value
     * @param roc_auc
     */
    public void setRoc_auc(double roc_auc) {
        this.roc_auc = roc_auc;
    }

    /**
     *Get fold change value (log2 value)
     * @return fc_value
     */
    public double getFc_value() {
        return fc_value;
    }

    /**
     *Set fold change value (log2 value)
     * @param fc_value
     */
    public void setFc_value(double fc_value) {
        this.fc_value = fc_value;
    }

    @Override
    public String toString() {
        return "prot index " + protIndex + " ----- ds " + quantDatasetIndex + " ----- pepsequence  " + peptideSequence;
    }

}
