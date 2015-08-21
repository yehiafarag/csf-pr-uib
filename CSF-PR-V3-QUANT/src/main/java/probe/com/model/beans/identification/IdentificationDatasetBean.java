package probe.com.model.beans.identification;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Yehia Farag
 */
public class IdentificationDatasetBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int expFile;//on upload file to get file type
    private int datasetId;
    private List<StandardIdentificationFractionPlotProteinBean> standerdPlotProt;
    private int peptidesInclude;
    private String name, description;
    private int fractionsNumber;
    private String species, sampleType, sampleProcessing, instrumentType, fragMode, UploadedByName, email, publicationLink;
    private int proteinsNumber, peptidesNumber, expType;
    private int numberValidProt;
    private List<Integer> fractionIds;
    private Set<Integer> peptidesIds;
    private Map<String, IdentificationPeptideBean> gPeptideList;

    /**
     *
     * @param datasetId
     */
    public void setDatasetId(int datasetId) {
        this.datasetId = datasetId;
    }

    /**
     *
     * @return
     */
    public int getDatasetId() {
        return datasetId;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param fractionsNumber
     */
    public void setFractionsNumber(int fractionsNumber) {
        this.fractionsNumber = fractionsNumber;
    }

    /**
     *
     * @return
     */
    public int getFractionsNumber() {
        return fractionsNumber;
    }

    /**
     *
     * @param expFile
     */
    public void setExpFile(int expFile) {
        this.expFile = expFile;
    }

    /**
     *
     * @return
     */
    public int getDatasetFile() {
        return expFile;
    }

    /**
     *
     * @return
     */
    public int getPeptidesInclude() {
        return peptidesInclude;
    }

    /**
     *
     * @param peptidesInclude
     */
    public void setPeptidesInclude(int peptidesInclude) {
        this.peptidesInclude = peptidesInclude;
    }

    /**
     *
     * @return
     */
    public String getSpecies() {
        return species;
    }

    /**
     *
     * @param species
     */
    public void setSpecies(String species) {
        this.species = species;
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
    public String getSampleProcessing() {
        return sampleProcessing;
    }

    /**
     *
     * @param sampleProcessing
     */
    public void setSampleProcessing(String sampleProcessing) {
        this.sampleProcessing = sampleProcessing;
    }

    /**
     *
     * @return
     */
    public String getInstrumentType() {
        return instrumentType;
    }

    /**
     *
     * @param instrumentType
     */
    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }

    /**
     *
     * @return
     */
    public String getFragMode() {
        return fragMode;
    }

    /**
     *
     * @param fragMode
     */
    public void setFragMode(String fragMode) {
        this.fragMode = fragMode;
    }

    /**
     *
     * @return
     */
    public String getUploadedByName() {
        return UploadedByName;
    }

    /**
     *
     * @param uploadedByName
     */
    public void setUploadedByName(String uploadedByName) {
        UploadedByName = uploadedByName;
    }

    /**
     *
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     */
    public String getPublicationLink() {
        return publicationLink;
    }

    /**
     *
     * @param publicationLink
     */
    public void setPublicationLink(String publicationLink) {
        this.publicationLink = publicationLink;
    }

    /**
     *
     * @return
     */
    public int getProteinsNumber() {
        return proteinsNumber;
    }

    /**
     *
     * @param proteinsNumber
     */
    public void setProteinsNumber(int proteinsNumber) {
        this.proteinsNumber = proteinsNumber;
    }

    /**
     *
     * @return
     */
    public int getPeptidesNumber() {
        return peptidesNumber;
    }

    /**
     *
     * @param peptidesNumber
     */
    public void setPeptidesNumber(int peptidesNumber) {
        this.peptidesNumber = peptidesNumber;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     */
    public List<Integer> getFractionIds() {
        return fractionIds;
    }

    /**
     *
     * @param fractionIds
     */
    public void setFractionIds(List<Integer> fractionIds) {
        this.fractionIds = fractionIds;
    }

    /**
     *
     * @return
     */
    public Set<Integer> getPeptidesIds() {
        return peptidesIds;
    }

    /**
     *
     * @param peptidesIds
     */
    public void setPeptidesIds(Set<Integer> peptidesIds) {
        this.peptidesIds = peptidesIds;
    }

    /**
     *
     * @return
     */
    public int getDatasetType() {
        return expType;
    }

    /**
     *
     * @param expType
     */
    public void setDatasetType(int expType) {
        this.expType = expType;
    }

    /**
     *
     * @return
     */
    public int getNumberValidProt() {
        return numberValidProt;
    }

    /**
     *
     * @param numberValidProt
     */
    public void setNumberValidProt(int numberValidProt) {
        this.numberValidProt = numberValidProt;
    }

    /**
     *
     * @return
     */
    public List<StandardIdentificationFractionPlotProteinBean> getStanderdPlotProt() {
        return standerdPlotProt;
    }

    /**
     *
     * @param standerdPlotProt
     */
    public void setStanderdPlotProt(List<StandardIdentificationFractionPlotProteinBean> standerdPlotProt) {
        this.standerdPlotProt = standerdPlotProt;
    }

    /**
     *
     * @return
     */
    public Map<String, IdentificationPeptideBean> getgPeptideList() {
        return gPeptideList;
    }

    /**
     *
     * @param gPeptideList
     */
    public void setgPeptideList(Map<String, IdentificationPeptideBean> gPeptideList) {
        this.gPeptideList = gPeptideList;
    }
}
