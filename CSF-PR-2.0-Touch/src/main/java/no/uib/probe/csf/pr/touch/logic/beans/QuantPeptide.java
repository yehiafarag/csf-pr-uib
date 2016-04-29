/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.logic.beans;

import java.io.Serializable;

/**
 * 
 *
 * @author Yehia Farag
 */
public class QuantPeptide implements Serializable {

    private int uniqueId;
    private int protIndex;
    private int DsKey;
    private String peptideSequence;
    private String sequenceAnnotated;
    private String additionalComments,pvalueSignificanceThreshold;
    private int peptideCharge;
    private String string_p_value;
    private String peptideModification;
    private String modification_comment;
    private String quantBasisComment;
     private String peptideSignature;

    public String getPeptideSignature() {
        return peptideSignature;
    }

    public void setPeptideSignature(String peptideSignature) {
        this.peptideSignature = peptideSignature;
    }

    public String getQuantBasisComment() {
        return quantBasisComment;
    }

    public void setQuantBasisComment(String quantBasisComment) {
        this.quantBasisComment = quantBasisComment;
    }

    public String getSequenceAnnotated() {
        return sequenceAnnotated;
    }
    
   
    public void setSequenceAnnotated(String sequenceAnnotated) {
        this.sequenceAnnotated = sequenceAnnotated;
    }

    public String getAdditionalComments() {
        return additionalComments;
    }

    public void setAdditionalComments(String additionalComments) {
        this.additionalComments = additionalComments;
    }

    public String getPvalueSignificanceThreshold() {
        return pvalueSignificanceThreshold;
    }

    public void setPvalueSignificanceThreshold(String pvalueSignificanceThreshold) {
        this.pvalueSignificanceThreshold = pvalueSignificanceThreshold;
    }

    public int getPeptideCharge() {
        return peptideCharge;
    }

    public void setPeptideCharge(int peptideCharge) {
        this.peptideCharge = peptideCharge;
    }
    private String p_value_comments;

    /**
     *
     * @return
     */
    public String getUniprotProteinAccession() {
        return uniprotProteinAccession;
    }

    /**
     *
     * @param uniprotProteinAccession
     */
    public void setUniprotProteinAccession(String uniprotProteinAccession) {
        this.uniprotProteinAccession = uniprotProteinAccession;
    }
    private double p_value;
    private String string_fc_value;
    private String uniprotProteinAccession;

    /**
     *
     * @return
     */
    public int getUniqueId() {
        return uniqueId;
    }

    /**
     *
     * @param uniqueId
     */
    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    /**
     *
     * @return
     */
    public int getProtIndex() {
        return protIndex;
    }

    /**
     *
     * @param protIndex
     */
    public void setProtIndex(int protIndex) {
        this.protIndex = protIndex;
    }

    /**
     *
     * @return
     */
    public int getDsKey() {
        return DsKey;
    }

    /**
     *
     * @param DsKey
     */
    public void setDsKey(int DsKey) {
        this.DsKey = DsKey;
    }

    /**
     *
     * @return
     */
    public String getPeptideSequence() {
        return peptideSequence;
    }

    /**
     *
     * @param peptideSequence
     */
    public void setPeptideSequence(String peptideSequence) {
        this.peptideSequence = peptideSequence;
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
    public String getModification_comment() {
        return modification_comment;
    }

    /**
     *
     * @param modification_comment
     */
    public void setModification_comment(String modification_comment) {
        this.modification_comment = modification_comment;
    }

    /**
     *
     * @return
     */
    public String getString_fc_value() {
        return string_fc_value;
    }

    /**
     *
     * @param string_fc_value
     */
    public void setString_fc_value(String string_fc_value) {
        this.string_fc_value = string_fc_value;
    }

    /**
     *
     * @return
     */
    public String getString_p_value() {
        return string_p_value;
    }

    /**
     *
     * @param string_p_value
     */
    public void setString_p_value(String string_p_value) {
        this.string_p_value = string_p_value;
    }

    /**
     *
     * @return
     */
    public String getP_value_comments() {
        return p_value_comments;
    }

    /**
     *
     * @param p_value_comments
     */
    public void setP_value_comments(String p_value_comments) {
        this.p_value_comments = p_value_comments;
    }
    double roc_auc, fc_value;

    /**
     *
     * @return
     */
    public double getP_value() {
        return p_value;
    }

    /**
     *
     * @param p_value
     */
    public void setP_value(double p_value) {
        this.p_value = p_value;
    }

    /**
     *
     * @return
     */
    public double getRoc_auc() {
        return roc_auc;
    }

    /**
     *
     * @param roc_auc
     */
    public void setRoc_auc(double roc_auc) {
        this.roc_auc = roc_auc;
    }

    /**
     *
     * @return
     */
    public double getFc_value() {
        return fc_value;
    }

    /**
     *
     * @param fc_value
     */
    public void setFc_value(double fc_value) {
        this.fc_value = fc_value;
    }
    
    @Override
    public String toString() {
        return "prot index "+protIndex+" ----- ds "+DsKey+" ----- pepsequence  " + peptideSequence;
    }
    
    

}
