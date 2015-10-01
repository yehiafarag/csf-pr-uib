package probe.com.dal;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
     * constructor
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
     * create database tables if not exist
     *
     * @return test boolean (successful creation)
     *
     */
//    public boolean createTable() {
//
////        boolean test = db.createTables();
//        return test;
//
//    }

    /**
     * remove dataset from the database
     *
     * @param datasetId
     * @return boolean successful process
     */
    public boolean removeIdentificationDataset(int datasetId) {
        boolean test = db.removeIdentificationDataset(datasetId);
        return test;
    }

    /**
     * get the available identification datasets
     *
     * @return identification datasetsList
     */
    public Map<Integer, IdentificationDatasetBean> getIdentificationDatasetsList()//get dataset list
    {
        Map<Integer, IdentificationDatasetBean> datasestList = db.getIdentificationDatasetsList();
        return datasestList;
    }

    /**
     * get selected identification dataset
     *
     * @param datasetId
     * @return dataset
     */
    public IdentificationDatasetBean retriveIdentficationDataset(int datasetId) {
        IdentificationDatasetBean dataset = db.retriveIdentficationDataset(datasetId);
        return dataset;
    }

    /**
     * get identification proteins map for especial identification dataset
     *
     * @param datasetId
     * @return proteinsList
     */
    public Map<String, IdentificationProteinBean> getIdentificationProteinsList(int datasetId) {
        Map<String, IdentificationProteinBean> proteinsList = db.getIdentificationProteinsList(datasetId);
        return proteinsList;
    }

    /**
     * get dataset identification peptides list
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
     * get all peptides number for specific dataset
     * @param datasetId
     * @param validated
     * @return identification peptides number
     */
    public int getAllIdentificationDatasetPeptidesNumber(int datasetId, boolean validated) {

        return db.getAllIdentificationDatasetPeptidesNumber(datasetId, validated);
    }

    /**
     * get identification dataset fractions list
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
     * search for identification proteins by accession keywords
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
     * search for proteins by accession keywords
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
     * search for proteins by accession keywords
     *
     * @param query query words
     * @return dataset Proteins Searching List
     */
    public List<QuantProtein> searchQuantificationProteins(Query query) {

        List<QuantProtein> datasetProteinsSearchingList = db.searchQuantificationProteins(query);

        return datasetProteinsSearchingList;
    }

    /**
     * get available quantification datasets initial information object that
     * contains the available datasets list and the active columns (to hide them
     * if they are empty)
     *
     * @return QuantDatasetInitialInformationObject
     */
    public QuantDatasetInitialInformationObject getQuantDatasetInitialInformationObject() {
        return db.getQuantDatasetInitialInformationObject();

    }

    /**
     * get available quantification datasets initial information object within
     * quant searching proteins results that contains the available datasets
     * list and the active columns (to hide them if they are empty)
     *
     * @param searchQuantificationProtList
     * @return QuantDatasetInitialInformationObject
     */
    public QuantDatasetInitialInformationObject getQuantDatasetInitialInformationObject(List<QuantProtein> searchQuantificationProtList) {
        return db.getQuantDatasetListObject(searchQuantificationProtList);

    }

    /**
     * get active quantification pie charts filters (to hide them if they are
     * empty)
     *
     * @return boolean array for the active and not active pie chart filters
     * indexes
     */
    public boolean[] getActivePieChartQuantFilters() {
        return db.getActivePieChartQuantFilters();

    }

    /**
     * get active quantification pie charts filters within quant searching
     * proteins results (to hide them if they are empty)
     *
     * @param searchQuantificationProtList
     * @return boolean array for the active and not active pie chart filters
     * indexes
     */
    public boolean[] getActivePieChartQuantFilters(List<QuantProtein> searchQuantificationProtList) {
        return db.getActivePieChartQuantFilters(searchQuantificationProtList);

    }

    /**
     * get identification peptides list form giving ids
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
     * get proteins fractions average list
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
     * search for identification proteins by protein description keywords
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
     * search for identification proteins by protein description keywords
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
     * search for identification proteins by peptide sequence keywords
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
     * search for proteins by peptide sequence keywords
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
     *update  standard plot files in the database
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
     * retrieve standard proteins data for fraction plot
     *
     * @param datasetId
     * @return
     */
    public List<StandardIdentificationFractionPlotProteinBean> getStandardIdentificationFractionProteinsList(int datasetId) {

        List<StandardIdentificationFractionPlotProteinBean> standardPlotList = db.getStandardIdentificationFractionProteinsList(datasetId);
        return standardPlotList;
    }

    /**
     * update identification dataset information
     *
     * @param dataset updated dataset object
     * @return test boolean
     */
    public boolean updateIdentificationDatasetInformation(IdentificationDatasetBean dataset) {
        boolean test = db.updateIdentificationDatasetInformation(dataset);
        return test;

    }

    /**
     * run only once to update the db :to be removed
     */
    @Deprecated
    public void runOnceToUpdateDatabase() {
//         db.singleuseUpdateDb();

    }

    /**
     * store quant proteins list to database
     *
     * @param quantProeinstList
     * @return is successful process
     */
    public boolean storeQuantProt(List<QuantProtein> quantProeinstList) {
        return db.storeQuantProt(quantProeinstList);

    }

    /**
     * get quant proteins list for quant dataset
     *
     * @param quantDatasetId
     * @return quant proteins list
     */
    public Set<QuantProtein> getQuantificationProteins(int quantDatasetId) {
        Set<QuantProtein> datasetProteinsSearchingList = db.getQuantificationProteins(quantDatasetId);
        return datasetProteinsSearchingList;
    }
    
     /**
     * get quant proteins list for a number of quant datasets
     *
     * @param quantDatasetIds
     * @return quant proteins list
     */
    public Set<QuantProtein> getQuantificationProteins(int[] quantDatasetIds) {
        Set<QuantProtein> datasetProteinsSearchingList = db.getQuantificationProteins(quantDatasetIds);
        return datasetProteinsSearchingList;
    }

    /**
     * get quant peptides list for specific quant dataset
     *
     * @param quantDatasetId
     * @return quant peptides list
     */
    public Map<String, Set<QuantPeptide>> getQuantificationPeptides(int quantDatasetId) {

        Map<String, Set<QuantPeptide>> datasetProteinsSearchingList = db.getQuantificationPeptides(quantDatasetId);

        return datasetProteinsSearchingList;
    }
    
     /**
     * get all quant peptides list for  quant dataset list
     *
     * @param quantDatasetIds
     * @return quant peptides list
     */
    public Map<String, Set<QuantPeptide>> getQuantificationPeptides(int[] quantDatasetIds) {
        Map<String, Set<QuantPeptide>> datasetProteinsSearchingList = db.getQuantificationPeptides(quantDatasetIds);
        return datasetProteinsSearchingList;
    }
}
