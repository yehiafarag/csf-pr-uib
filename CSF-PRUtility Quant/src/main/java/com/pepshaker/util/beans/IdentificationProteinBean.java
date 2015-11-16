package com.pepshaker.util.beans;

import java.io.Serializable;

/**
 *
 * @author Yehia Farag
 */
public class IdentificationProteinBean implements Serializable,Comparable<IdentificationProteinBean> {

    /**
     *
     */
    public IdentificationProteinBean() {
    }

    /**
     *
     * @param pb
     */
    public IdentificationProteinBean(IdentificationProteinBean pb) {
        this.accession = pb.getAccession();
        this.Description = pb.getDescription();
        this.validated = pb.isValidated();
        this.otherProteins = pb.getOtherProteins();
        this.proteinInferenceClass = pb.getProteinInferenceClass();
        this.sequenceCoverage = pb.getSequenceCoverage();
        this.observableCoverage = pb.getObservableCoverage();
        this.confidentPtmSites = pb.getConfidentPtmSites();
        this.numberConfident = pb.getNumberConfident();
        this.otherPtmSites = pb.getOtherPtmSites();
        this.numberOfOther = pb.getNumberOfOther();
        this.numberValidatedPeptides = pb.getNumberValidatedPeptides();
        this.numberValidatedSpectra = pb.getNumberValidatedSpectra();
        this.emPai = pb.getEmPai();
        this.nsaf = pb.getNsaf();
        this.mw_kDa = pb.getMw_kDa();
        this.score = pb.getScore();
        this.confidence = pb.getConfidence();
        this.Starred = pb.isStarred();
        this.peptideFractionSpread_lower_range_kDa = pb.getPeptideFractionSpread_lower_range_kDa();
        this.peptideFractionSpread_upper_range_kDa = pb.getPeptideFractionSpread_upper_range_kDa();
        this.spectrumFractionSpread_lower_range_kDa = pb.getSpectrumFractionSpread_lower_range_kDa();
        this.spectrumFractionSpread_upper_range_kDa = pb.getSpectrumFractionSpread_upper_range_kDa();
        this.nonEnzymaticPeptides = pb.isNonEnzymaticPeptides();
        this.geneName = pb.getGeneName();
        this.chromosomeNumber = pb.getChromosomeNumber();
        this.numberPeptides = pb.getNumberPeptides();
        this.numberSpectra = pb.getNumberSpectra();
        this.expId = pb.getDatasetId();
        this.decoy = pb.getDecoy();
        this.protGroupId= pb.getProtGroupId();


    }
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String accession;
    private int expId;
    private String Description;
    private boolean validated;
    private int decoy;
    private String geneName;
    private String chromosomeNumber;
    private int protGroupId;
    private int frcationId;

    /**
     *
     * @return
     */
    public int getExpId() {
        return expId;
    }

    /**
     *
     * @param expId
     */
    public void setExpId(int expId) {
        this.expId = expId;
    }

    /**
     *
     * @return
     */
    public int getFrcationId() {
        return frcationId;
    }

    /**
     *
     * @param frcationId
     */
    public void setFrcationId(int frcationId) {
        this.frcationId = frcationId;
    }

    /**
     *
     * @return
     */
    public String getGeneName() {
        return geneName;
    }

    /**
     *
     * @param geneName
     */
    public void setGeneName(String geneName) {
        this.geneName = geneName;
    }

    /**
     *
     * @return
     */
    public String getChromosomeNumber() {
        return chromosomeNumber;
    }

    /**
     *
     * @param chromosomeNumber
     */
    public void setChromosomeNumber(String chromosomeNumber) {
        this.chromosomeNumber = chromosomeNumber;
    }

    /**
     *
     * @return
     */
    public boolean isValidated() {
        return validated;
    }

    /**
     *
     * @param validated
     */
    public void setValidated(boolean validated) {
        this.validated = validated;
    }
    private String otherProteins;
    private String proteinInferenceClass;
    private double sequenceCoverage;
    private double observableCoverage;
    private String confidentPtmSites;
    private String numberConfident;
    private String otherPtmSites;
    private String numberOfOther;
    private int numberValidatedPeptides;
    private int numberValidatedSpectra;
    private int numberSpectra;
    private int numberPeptides;
    private double emPai;
    private double nsaf;
    private double mw_kDa;
    private double score;
    private double confidence;
    private boolean Starred;
    private String peptideFractionSpread_lower_range_kDa;
    private String peptideFractionSpread_upper_range_kDa;
    private String spectrumFractionSpread_lower_range_kDa;
    private String spectrumFractionSpread_upper_range_kDa;
    private boolean nonEnzymaticPeptides;
    private int numberOfPeptidePerFraction;
    private int numberOfSpectraPerFraction;
    private double AveragePrecursorIntensityPerFraction;

    /**
     *
     * @param accession
     */
    public void setAccession(String accession) {
        this.accession = accession;
    }

    /**
     *
     * @return
     */
    public String getAccession() {
        return accession;
    }

    /**
     *
     * @param description
     */
    public void setDescription(String description) {
        Description = description;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return Description;
    }

    /**
     *
     * @param otherProteins
     */
    public void setOtherProteins(String otherProteins) {
        this.otherProteins = otherProteins;
    }

    /**
     *
     * @return
     */
    public String getOtherProteins() {
        return otherProteins;
    }

    /**
     *
     * @param proteinInferenceClass
     */
    public void setProteinInferenceClass(String proteinInferenceClass) {
        this.proteinInferenceClass = proteinInferenceClass;
    }

    /**
     *
     * @return
     */
    public String getProteinInferenceClass() {
        return proteinInferenceClass;
    }

    /**
     *
     * @param sequenceCoverage
     */
    public void setSequenceCoverage(double sequenceCoverage) {
        this.sequenceCoverage = sequenceCoverage;
    }

    /**
     *
     * @return
     */
    public double getSequenceCoverage() {
        return sequenceCoverage;
    }

    /**
     *
     * @param observableCoverage
     */
    public void setObservableCoverage(double observableCoverage) {
        this.observableCoverage = observableCoverage;
    }

    /**
     *
     * @return
     */
    public double getObservableCoverage() {
        return observableCoverage;
    }

    /**
     *
     * @param confidentPtmSites
     */
    public void setConfidentPtmSites(String confidentPtmSites) {
        this.confidentPtmSites = confidentPtmSites;
    }

    /**
     *
     * @return
     */
    public String getConfidentPtmSites() {
        return confidentPtmSites;
    }

    /**
     *
     * @param numberConfident
     */
    public void setNumberConfident(String numberConfident) {
        this.numberConfident = numberConfident;
    }

    /**
     *
     * @return
     */
    public String getNumberConfident() {
        return numberConfident;
    }

    /**
     *
     * @param otherPtmSites
     */
    public void setOtherPtmSites(String otherPtmSites) {
        this.otherPtmSites = otherPtmSites;
    }

    /**
     *
     * @return
     */
    public String getOtherPtmSites() {
        return otherPtmSites;
    }

    /**
     *
     * @param numberOfOther
     */
    public void setNumberOfOther(String numberOfOther) {
        this.numberOfOther = numberOfOther;
    }

    /**
     *
     * @return
     */
    public String getNumberOfOther() {
        return numberOfOther;
    }

    /**
     *
     * @param numberValidatedPeptides
     */
    public void setNumberValidatedPeptides(int numberValidatedPeptides) {
        this.numberValidatedPeptides = numberValidatedPeptides;
    }

    /**
     *
     * @return
     */
    public int getNumberValidatedPeptides() {
        return numberValidatedPeptides;
    }

    /**
     *
     * @param numberValidatedSpectra
     */
    public void setNumberValidatedSpectra(int numberValidatedSpectra) {
        this.numberValidatedSpectra = numberValidatedSpectra;
    }

    /**
     *
     * @return
     */
    public int getNumberValidatedSpectra() {
        return numberValidatedSpectra;
    }

    /**
     *
     * @param emPai
     */
    public void setEmPai(double emPai) {
        this.emPai = emPai;
    }

    /**
     *
     * @return
     */
    public double getEmPai() {
        return emPai;
    }

    /**
     *
     * @param nsaf
     */
    public void setNsaf(double nsaf) {
        this.nsaf = nsaf;
    }

    /**
     *
     * @return
     */
    public double getNsaf() {
        return nsaf;
    }

    /**
     *
     * @param mw_kDa
     */
    public void setMw_kDa(double mw_kDa) {
        this.mw_kDa = mw_kDa;
    }

    /**
     *
     * @return
     */
    public double getMw_kDa() {
        return mw_kDa;
    }

    /**
     *
     * @param score
     */
    public void setScore(double score) {
        this.score = score;
    }

    /**
     *
     * @return
     */
    public double getScore() {
        return score;
    }

    /**
     *
     * @param confidence
     */
    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    /**
     *
     * @return
     */
    public double getConfidence() {
        return confidence;
    }

    /**
     *
     * @param starred
     */
    public void setStarred(boolean starred) {
        Starred = starred;
    }

    /**
     *
     * @return
     */
    public boolean isStarred() {
        return Starred;
    }

    /**
     *
     * @param numberOfPeptidePerFraction
     */
    public void setNumberOfPeptidePerFraction(int numberOfPeptidePerFraction) {
        this.numberOfPeptidePerFraction = numberOfPeptidePerFraction;
    }

    /**
     *
     * @return
     */
    public int getNumberOfPeptidePerFraction() {
        return numberOfPeptidePerFraction;
    }

    /**
     *
     * @param numberOfSpectraPerFraction
     */
    public void setNumberOfSpectraPerFraction(int numberOfSpectraPerFraction) {
        this.numberOfSpectraPerFraction = numberOfSpectraPerFraction;
    }

    /**
     *
     * @return
     */
    public int getNumberOfSpectraPerFraction() {
        return numberOfSpectraPerFraction;
    }

    /**
     *
     * @param averagePrecursorIntensityPerFraction
     */
    public void setAveragePrecursorIntensityPerFraction(
            double averagePrecursorIntensityPerFraction) {
        AveragePrecursorIntensityPerFraction = averagePrecursorIntensityPerFraction;
    }

    /**
     *
     * @return
     */
    public double getAveragePrecursorIntensityPerFraction() {
        return AveragePrecursorIntensityPerFraction;
    }

    /**
     *
     * @return
     */
    public boolean isNonEnzymaticPeptides() {
        return nonEnzymaticPeptides;
    }

    /**
     *
     * @param nonEnzymaticPeptides
     */
    public void setNonEnzymaticPeptides(boolean nonEnzymaticPeptides) {
        this.nonEnzymaticPeptides = nonEnzymaticPeptides;
    }

    /**
     *
     * @return
     */
    public String getPeptideFractionSpread_lower_range_kDa() {
        return peptideFractionSpread_lower_range_kDa;
    }

    /**
     *
     * @param peptideFractionSpread_lower_range_kDa
     */
    public void setPeptideFractionSpread_lower_range_kDa(
            String peptideFractionSpread_lower_range_kDa) {
        this.peptideFractionSpread_lower_range_kDa = peptideFractionSpread_lower_range_kDa;
    }

    /**
     *
     * @return
     */
    public String getPeptideFractionSpread_upper_range_kDa() {
        return peptideFractionSpread_upper_range_kDa;
    }

    /**
     *
     * @param peptideFractionSpread_upper_range_kDa
     */
    public void setPeptideFractionSpread_upper_range_kDa(
            String peptideFractionSpread_upper_range_kDa) {
        this.peptideFractionSpread_upper_range_kDa = peptideFractionSpread_upper_range_kDa;
    }

    /**
     *
     * @return
     */
    public String getSpectrumFractionSpread_lower_range_kDa() {
        return spectrumFractionSpread_lower_range_kDa;
    }

    /**
     *
     * @param spectrumFractionSpread_lower_range_kDa
     */
    public void setSpectrumFractionSpread_lower_range_kDa(
            String spectrumFractionSpread_lower_range_kDa) {
        this.spectrumFractionSpread_lower_range_kDa = spectrumFractionSpread_lower_range_kDa;
    }

    /**
     *
     * @return
     */
    public String getSpectrumFractionSpread_upper_range_kDa() {
        return spectrumFractionSpread_upper_range_kDa;
    }

    /**
     *
     * @param spectrumFractionSpread_upper_range_kDa
     */
    public void setSpectrumFractionSpread_upper_range_kDa(
            String spectrumFractionSpread_upper_range_kDa) {
        this.spectrumFractionSpread_upper_range_kDa = spectrumFractionSpread_upper_range_kDa;
    }

    /**
     *
     * @return
     */
    public int getNumberSpectra() {
        return numberSpectra;
    }

    /**
     *
     * @param numberSpectra
     */
    public void setNumberSpectra(int numberSpectra) {
        this.numberSpectra = numberSpectra;
    }

    /**
     *
     * @return
     */
    public int getNumberPeptides() {
        return numberPeptides;
    }

    /**
     *
     * @param numberPeptides
     */
    public void setNumberPeptides(int numberPeptides) {
        this.numberPeptides = numberPeptides;
    }

    /**
     *
     * @return
     */
    public int getDecoy() {
        return decoy;
    }

    /**
     *
     * @param decoy
     */
    public void setDecoy(int decoy) {
        this.decoy = decoy;
    }

    /**
     *
     * @return
     */
    public int getDatasetId() {
        return expId;
    }

    /**
     *
     * @param expId
     */
    public void setDatasetId(int expId) {
        this.expId = expId;
    }

    /**
     *
     * @return
     */
    public int getProtGroupId() {
        return protGroupId;
    }

    /**
     *
     * @param protGroupId
     */
    public void setProtGroupId(int protGroupId) {
        this.protGroupId = protGroupId;
    }

    @Override
    public int compareTo(IdentificationProteinBean o) {
        if(this.nsaf > o.getNsaf())
            return 1;
        else
            return -1;
    
    }
}
