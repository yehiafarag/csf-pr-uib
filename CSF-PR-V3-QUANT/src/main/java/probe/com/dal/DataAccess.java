package probe.com.dal;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import probe.com.model.beans.OverviewInfoBean;

import probe.com.model.beans.identification.IdentificationDatasetBean;
import probe.com.model.beans.identification.IdentificationFractionBean;
import probe.com.model.beans.identification.IdentificationPeptideBean;
import probe.com.model.beans.identification.IdentificationProteinBean;
import probe.com.model.beans.quant.QuantProtein;
import probe.com.model.beans.identification.StandardIdentificationFractionPlotProteinBean;
import probe.com.model.beans.quant.QuantDatasetInitialInformationObject;
import probe.com.model.beans.quant.QuantPeptide;

/**
 *
 * @author Yehia Farag
 */
public class DataAccess implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -7011020617952045934L;
    private final DataBase db;

    /**
     * Initialize data access layer
     *
     * @param url
     * @param dbName
     * @param driver
     * @param userName
     * @param password
     */
    public DataAccess(String url, String dbName, String driver, String userName, String password) {
        db = new DataBase(url, dbName, driver, userName, password);
    }

    /**
     * Remove dataset from the database
     *
     * @param datasetId
     * @return boolean successful process
     */
    public boolean removeIdentificationDataset(int datasetId) {
        boolean test = db.removeIdentificationDataset(datasetId);
        return test;
    }

    /**
     * Get the available identification datasets
     *
     * @return identification datasetsList
     */
    public Map<Integer, IdentificationDatasetBean> getIdentificationDatasetsList()//get dataset list
    {
        Map<Integer, IdentificationDatasetBean> datasestList = db.getIdentificationDatasetsList();
        return datasestList;
    }

    /**
     * Get selected identification dataset
     *
     * @param datasetId
     * @return dataset
     */
    public IdentificationDatasetBean retriveIdentficationDataset(int datasetId) {
        IdentificationDatasetBean dataset = db.retriveIdentficationDataset(datasetId);
        return dataset;
    }

    /**
     * Get identification proteins map for especial identification dataset
     *
     * @param datasetId
     * @return proteinsList
     */
    public Map<String, IdentificationProteinBean> getIdentificationProteinsList(int datasetId) {
        Map<String, IdentificationProteinBean> proteinsList = db.getIdentificationProteinsList(datasetId);
        return proteinsList;
    }

    /**
     * Get dataset identification peptides list
     *
     * @param datasetId
     * @param valid
     * @return dataset peptide List
     */
    public Map<Integer, IdentificationPeptideBean> getAllIdentificationDatasetPeptidesList(int datasetId, boolean valid) {
        Map<Integer, IdentificationPeptideBean> identificationPeptidesList = db.getAllIdentificationDatasetPeptidesList(datasetId, valid);
        return identificationPeptidesList;
    }

    /**
     * Get all peptides number for specific dataset
     *
     * @param datasetId
     * @param validated
     * @return identification peptides number
     */
    public int getAllIdentificationDatasetPeptidesNumber(int datasetId, boolean validated) {

        return db.getAllIdentificationDatasetPeptidesNumber(datasetId, validated);
    }

    /**
     * Get identification dataset fractions list
     *
     * @param datasetId
     * @param accession
     * @param otherAccession
     * @return fractions list for the selected dataset
     */
    public Map<Integer, IdentificationProteinBean> getIdentificationProteinsGelFractionsList(int datasetId, String accession, String otherAccession) {
        Map<Integer, IdentificationProteinBean> fractionsProtList = db.getIdentificationProteinsGelFractionsList(datasetId, accession, otherAccession);
        return fractionsProtList;

    }

    /**
     * Search for identification proteins by accession keywords
     *
     * @param accession array of query words
     * @param datasetId
     * @param validatedOnly only validated proteins results
     * @return dataset Proteins Searching List
     */
    public Map<Integer, IdentificationProteinBean> searchIdentificationProteinByAccession(String accession, int datasetId, boolean validatedOnly) {
        Map<Integer, IdentificationProteinBean> datasetProteinsSearchingList = db.searchIdentificationProteinByAccession(accession, datasetId, validatedOnly);
        return datasetProteinsSearchingList;
    }

    /**
     * Search for proteins by accession keywords
     *
     * @param accession array of query words
     * @param validatedOnly only validated proteins results
     * @return dataset Proteins Searching List
     */
    public Map<Integer, IdentificationProteinBean> searchIdentificationProteinAllDatasetsByAccession(String accession, boolean validatedOnly) {
        String[] queryWordsArr = accession.split("\n");
        Set<String> searchSet = new HashSet<String>();
        for (String str : queryWordsArr) {
            if (str.trim().length() > 3) {
                searchSet.add(str.trim());
            }
        }
        Map<Integer, IdentificationProteinBean> datasetProteinsSearchingList = db.searchIdentificationProteinAllDatasetsByAccession(searchSet, validatedOnly);

        return datasetProteinsSearchingList;
    }

    /**
     * Search for proteins by accession keywords
     *
     * @param query query words
     * @param toCompare
     * @return dataset Proteins Searching List
     */
    public List<QuantProtein> searchQuantificationProteins(Query query, boolean toCompare) {

        List<QuantProtein> datasetProteinsSearchingList = db.searchQuantificationProteins(query,toCompare);

        return datasetProteinsSearchingList;
    }

    /**
     * Get available quantification datasets initial information object that
     * contains the available datasets list and the active columns (to hide them
     * if they are empty)
     *
     * @return QuantDatasetInitialInformationObject
     */
    public Map<String, QuantDatasetInitialInformationObject> getQuantDatasetInitialInformationObject() {
        return db.getQuantDatasetInitialInformationObject();

    }

    /**
     * Get available quantification datasets initial information object within
     * quant searching proteins results that contains the available datasets
     * list and the active columns (to hide them if they are empty)
     *
     * @param searchQuantificationProtList
     * @return QuantDatasetInitialInformationObject
     */
    public Map<String, QuantDatasetInitialInformationObject> getQuantDatasetInitialInformationObject(List<QuantProtein> searchQuantificationProtList) {
        return db.getQuantDatasetListObject(searchQuantificationProtList);

    }

    /**
     * Get active quantification pie charts filters (to hide them if they are
     * empty)
     *
     * @return boolean array for the active and not active pie chart filters
     * indexes
     */
    public Map<String, boolean[]> getActivePieChartQuantFilters() {
        return db.getActivePieChartQuantFilters();

    }

    /**
     * Get active quantification pie charts filters within quant searching
     * proteins results (to hide them if they are empty)
     *
     * @param searchQuantificationProtList
     * @return boolean array for the active and not active pie chart filters
     * indexes
     */
    public Map<String, boolean[]> getActivePieChartQuantFilters(List<QuantProtein> searchQuantificationProtList) {
        return db.getActivePieChartQuantFilters(searchQuantificationProtList);

    }

    /**
     * Get identification peptides list form giving ids
     *
     * @param accession
     * @param otherAcc
     * @param datasetId
     * @return peptides list
     */
    public Map<Integer, IdentificationPeptideBean> getIdentificationPeptidesList(String accession, String otherAcc, int datasetId) {
        Map<Integer, IdentificationPeptideBean> peptidesProtList = db.getIdentificationPeptidesList(accession, otherAcc, datasetId);
        return peptidesProtList;
    }

    /**
     * Get proteins fractions average list
     *
     * @param accession
     * @param datasetId
     * @return dataset peptide List
     */
    public Map<Integer, IdentificationFractionBean> getIdentificationProteinFractionList(String accession, int datasetId) {
        Map<Integer, IdentificationFractionBean> protionFractList = db.getIdentificationProteinFractionList(accession, datasetId);
        return protionFractList;
    }

    /**
     * Search for identification proteins by protein description keywords
     *
     * @param protSearchKeyword array of query words
     * @param datasetId dataset Id
     * @param validatedOnly only validated proteins results
     * @return datasetProteinsSearchList
     */
    public Map<Integer, IdentificationProteinBean> searchIdentificationProteinByName(String protSearchKeyword, int datasetId, boolean validatedOnly) {
        Map<Integer, IdentificationProteinBean> proteinsList = db.searchIdentificationProteinByName(protSearchKeyword, datasetId, validatedOnly);
        return proteinsList;
    }

    /**
     * Search for identification proteins by protein description keywords
     *
     * @param protSearchKeyword array of query words
     * @param validatedOnly only validated proteins results
     * @return datasetProteinsSearchList
     */
    public Map<Integer, IdentificationProteinBean> searchIdentificationProteinAllDatasetsByName(String protSearchKeyword, boolean validatedOnly) {
        Map<Integer, IdentificationProteinBean> proteinsList = db.searchIdentificationProteinAllDatasetsByName(protSearchKeyword, validatedOnly);
        return proteinsList;
    }

    /**
     * Search for identification proteins by peptide sequence keywords
     *
     * @param peptideSequenceKeyword array of query words
     * @param validatedOnly only validated proteins results
     * @return datasetProteinsSearchList
     */
    public Map<Integer, IdentificationProteinBean> SearchIdentificationProteinAllDatasetsByPeptideSequence(String peptideSequenceKeyword, boolean validatedOnly) {

        Map<Integer, IdentificationProteinBean> proteinsList = db.SearchIdentificationProteinAllDatasetsByPeptideSequence(peptideSequenceKeyword, validatedOnly);
        return proteinsList;
    }

    /**
     * Search for proteins by peptide sequence keywords
     *
     * @param peptideSequenceKeyword array of query words
     * @param datasetId dataset Id
     * @param validatedOnly only validated proteins results
     * @return datasetProteinsSearchList
     */
    public Map<Integer, IdentificationProteinBean> SearchIdentificationProteinByPeptideSequence(String peptideSequenceKeyword, int datasetId, boolean validatedOnly) {

        Map<Integer, IdentificationProteinBean> proteinsList = db.SearchIdentificationProteinByPeptideSequence(peptideSequenceKeyword, datasetId, validatedOnly);
        return proteinsList;
    }

    /**
     * Update standard plot files in the database
     *
     *
     * @param dataset dataset bean (in case of update existing dataset)
     * @return test boolean
     */
    public boolean updateIdentificationStandardPlotProteins(IdentificationDatasetBean dataset) {
        boolean test = db.updateIdentificationStandardPlotProteins(dataset);
        return test;
    }

    /**
     * Retrieve standard proteins data for fraction plot
     *
     * @param datasetId
     * @return
     */
    public List<StandardIdentificationFractionPlotProteinBean> getStandardIdentificationFractionProteinsList(int datasetId) {

        List<StandardIdentificationFractionPlotProteinBean> standardPlotList = db.getStandardIdentificationFractionProteinsList(datasetId);
        return standardPlotList;
    }

    /**
     * Update identification dataset information
     *
     * @param dataset updated dataset object
     * @return test boolean
     */
    public boolean updateIdentificationDatasetInformation(IdentificationDatasetBean dataset) {
        boolean test = db.updateIdentificationDatasetInformation(dataset);
        return test;

    }

    /**
     * Store quant proteins list to database
     *
     * @param quantProeinstList
     * @return is successful process
     */
    public boolean storeQuantProt(List<QuantProtein> quantProeinstList) {
        return db.storeQuantProt(quantProeinstList);

    }

    /**
     * Get quant proteins list for quant dataset
     *
     * @param quantDatasetId
     * @return quant proteins list
     */
    public Set<QuantProtein> getQuantificationProteins(int quantDatasetId) {
        Set<QuantProtein> datasetProteinsSearchingList = db.getQuantificationProteins(quantDatasetId);
        return datasetProteinsSearchingList;
    }

    /**
     * Get quant proteins list for a number of quant datasets
     *
     * @param quantDatasetIds
     * @return quant proteins list
     */
    public Set<QuantProtein> getQuantificationProteins(Object[] quantDatasetIds) {
        Set<QuantProtein> datasetProteinsSearchingList = db.getQuantificationProteins(quantDatasetIds);
        return datasetProteinsSearchingList;
    }

    /**
     * Get quant peptides list for specific quant dataset
     *
     * @param quantDatasetId
     * @return quant peptides list
     */
    public Map<String, Set<QuantPeptide>> getQuantificationPeptides(int quantDatasetId) {

        Map<String, Set<QuantPeptide>> datasetProteinsSearchingList = db.getQuantificationPeptides(quantDatasetId);

        return datasetProteinsSearchingList;
    }

    /**
     * Get all quant peptides list for quant dataset list
     *
     * @param quantDatasetIds
     * @return quant peptides list
     */
    public Map<String, Set<QuantPeptide>> getQuantificationPeptides(Object[] quantDatasetIds) {
        Map<String, Set<QuantPeptide>> datasetProteinsSearchingList = db.getQuantificationPeptides(quantDatasetIds);
        return datasetProteinsSearchingList;
    }

    /**
     * Get all required informations for the resource statues
     *
     * @return OverviewInfoBean
     */
    public OverviewInfoBean getResourceOverviewInformation() {
        return this.db.getResourceOverviewInformation();

    }
    /**
     * Get map for full disease name
     *
     * @return map of the short and long diseases names
     */
    public Map<String, String> getDiseaseFullNameMap() {
        return db.getDiseaseFullNameMap();
    }

     /**
     * Get set of disease groups names for special disease category
     *
     * @param diseaseCat
     * @return map of the short and long diseases names
     */
    public Set<String> getDiseaseGroupNameMap(String diseaseCat) {
        return db.getDiseaseGroupNameMap(diseaseCat);
        
    }
    
}
