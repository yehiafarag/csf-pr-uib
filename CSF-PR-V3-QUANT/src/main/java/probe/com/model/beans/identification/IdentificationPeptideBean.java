package probe.com.model.beans.identification;

import java.io.Serializable;

/**
 *
 * @author Yehia Farag
 */
public class IdentificationPeptideBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String protein;
    private String otherProteins;
    private String peptideProteins;
    private String otherProteinDescriptions;
    private String peptideProteinsDescriptions;
    private String aaBefore;
    private String sequence;
    private String aaAfter;
    private String peptideStart;
    private String fixedModification;
    private String peptideEnd;
    private String variableModification;
    private String locationConfidence;
    private String precursorCharges;
    private int numberOfValidatedSpectra;
    private double score;
    private double confidence;
    private int peptideId;
    private String proteinInference;
    private String sequenceTagged;
    private boolean enzymatic;
    private double validated;
    private int decoy;
    private boolean starred;
    private Boolean deamidationAndGlycopattern;
    private String glycopatternPositions;
    private Boolean likelyNotGlycosite;
    private String mainProtDesc;

    /**
     *
     * @return
     */
    public String getProtein() {
        return protein;
    }

    /**
     *
     * @param protein
     */
    public void setProtein(String protein) {
        this.protein = protein;
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
     * @param otherProteins
     */
    public void setOtherProteins(String otherProteins) {
        this.otherProteins = otherProteins;
    }

    /**
     *
     * @return
     */
    public String getPeptideProteins() {
        return peptideProteins;
    }

    /**
     *
     * @param peptideProteins
     */
    public void setPeptideProteins(String peptideProteins) {
        this.peptideProteins = peptideProteins;
    }

    /**
     *
     * @return
     */
    public String getOtherProteinDescriptions() {
        return otherProteinDescriptions;
    }

    /**
     *
     * @param otherProteinDescriptions
     */
    public void setOtherProteinDescriptions(String otherProteinDescriptions) {
        this.otherProteinDescriptions = otherProteinDescriptions;
    }

    /**
     *
     * @return
     */
    public String getPeptideProteinsDescriptions() {
        return peptideProteinsDescriptions;
    }

    /**
     *
     * @param peptideProteinsDescriptions
     */
    public void setPeptideProteinsDescriptions(
            String peptideProteinsDescriptions) {
        this.peptideProteinsDescriptions = peptideProteinsDescriptions;
    }

    /**
     *
     * @return
     */
    public String getAaBefore() {
        return aaBefore;
    }

    /**
     *
     * @param aaBefore
     */
    public void setAaBefore(String aaBefore) {
        this.aaBefore = aaBefore;
    }

    /**
     *
     * @return
     */
    public String getSequence() {
        return sequence;
    }

    /**
     *
     * @param sequence
     */
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    /**
     *
     * @return
     */
    public String getAaAfter() {
        return aaAfter;
    }

    /**
     *
     * @param aaAfter
     */
    public void setAaAfter(String aaAfter) {
        this.aaAfter = aaAfter;
    }

    /**
     *
     * @return
     */
    public String getPeptideStart() {
        return peptideStart;
    }

    /**
     *
     * @param peptideStart
     */
    public void setPeptideStart(String peptideStart) {
        this.peptideStart = peptideStart;
    }

    /**
     *
     * @return
     */
    public int getPeptideId() {
        return peptideId;
    }

    /**
     *
     * @param peptideId
     */
    public void setPeptideId(int peptideId) {
        this.peptideId = peptideId;
    }

    /**
     *
     * @return
     */
    public int getNumberOfValidatedSpectra() {
        return numberOfValidatedSpectra;
    }

    /**
     *
     * @param numberOfValidatedSpectra
     */
    public void setNumberOfValidatedSpectra(int numberOfValidatedSpectra) {
        this.numberOfValidatedSpectra = numberOfValidatedSpectra;
    }

    /**
     *
     * @return
     */
    public String getLocationConfidence() {
        return locationConfidence;
    }

    /**
     *
     * @param locationConfidence
     */
    public void setLocationConfidence(String locationConfidence) {
        this.locationConfidence = locationConfidence;
    }

    /**
     *
     * @return
     */
    public String getVariableModification() {
        return variableModification;
    }

    /**
     *
     * @param variableModification
     */
    public void setVariableModification(String variableModification) {
        this.variableModification = variableModification;
    }

    /**
     *
     * @return
     */
    public String getPeptideEnd() {
        return peptideEnd;
    }

    /**
     *
     * @param peptideEnd
     */
    public void setPeptideEnd(String peptideEnd) {
        this.peptideEnd = peptideEnd;
    }

    /**
     *
     * @return
     */
    public String getPrecursorCharges() {
        return precursorCharges;
    }

    /**
     *
     * @param precursorCharges
     */
    public void setPrecursorCharges(String precursorCharges) {
        this.precursorCharges = precursorCharges;
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
     * @param score
     */
    public void setScore(double score) {
        this.score = score;
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
     * @param confidence
     */
    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    /**
     *
     * @return
     */
    public String getFixedModification() {
        return fixedModification;
    }

    /**
     *
     * @param fixedModification
     */
    public void setFixedModification(String fixedModification) {
        this.fixedModification = fixedModification;
    }

    /**
     *
     * @return
     */
    public String getProteinInference() {
        return proteinInference;
    }

    /**
     *
     * @param proteinInference
     */
    public void setProteinInference(String proteinInference) {
        this.proteinInference = proteinInference;
    }

    /**
     *
     * @return
     */
    public String getSequenceTagged() {
        return sequenceTagged;
    }

    /**
     *
     * @param sequenceTagged
     */
    public void setSequenceTagged(String sequenceTagged) {
        this.sequenceTagged = sequenceTagged;
    }

    /**
     *
     * @return
     */
    public boolean isEnzymatic() {
        return enzymatic;
    }

    /**
     *
     * @param enzymatic
     */
    public void setEnzymatic(boolean enzymatic) {
        this.enzymatic = enzymatic;
    }

    /**
     *
     * @return
     */
    public double getValidated() {
        return validated;
    }

    /**
     *
     * @param validated
     */
    public void setValidated(double validated) {
        this.validated = validated;
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
    public Boolean getDeamidationAndGlycopattern() {
        return deamidationAndGlycopattern;
    }

    /**
     *
     * @param deamidationAndGlycopattern
     */
    public void setDeamidationAndGlycopattern(Boolean deamidationAndGlycopattern) {
        this.deamidationAndGlycopattern = deamidationAndGlycopattern;
    }

    /**
     *
     * @return
     */
    public Boolean isLikelyNotGlycopeptide() {
        return likelyNotGlycosite;
    }

    /**
     *
     * @param likelyNotGlycosite
     */
    public void setLikelyNotGlycosite(Boolean likelyNotGlycosite) {
        this.likelyNotGlycosite = likelyNotGlycosite;
    }

    /**
     *
     * @return
     */
    public boolean isStarred() {
        return starred;
    }

    /**
     *
     * @param starred
     */
    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    /**
     *
     * @return
     */
    public Boolean isDeamidationAndGlycopattern() {
        return deamidationAndGlycopattern;
    }

    /**
     *
     * @param deamidationAndGlycopattern
     */
    public void setDeamidationAndGlycopattern(boolean deamidationAndGlycopattern) {
        this.deamidationAndGlycopattern = deamidationAndGlycopattern;
    }

    /**
     *
     * @return
     */
    public String getGlycopatternPositions() {
        return glycopatternPositions;
    }

    /**
     *
     * @param glycopatternPositions
     */
    public void setGlycopatternPositions(String glycopatternPositions) {
        this.glycopatternPositions = glycopatternPositions;
    }

    /**
     *
     * @return
     */
    public String getMainProtDesc() {
        return mainProtDesc;
    }

    /**
     *
     * @param mainProtDesc
     */
    public void setMainProtDesc(String mainProtDesc) {
        this.mainProtDesc = mainProtDesc;
    }
}
