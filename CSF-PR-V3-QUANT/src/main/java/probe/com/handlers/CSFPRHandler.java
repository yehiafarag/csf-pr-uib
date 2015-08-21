package probe.com.handlers;

import java.sql.SQLException;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import probe.com.dal.Query;
import probe.com.model.CoreLogic;
import probe.com.model.beans.identification.IdentificationDatasetBean;
import probe.com.model.beans.identification.IdentificationDatasetDetailsBean;
import probe.com.model.beans.identification.IdentificationFractionBean;
import probe.com.model.beans.quant.QuantGroupsComparison;
import probe.com.model.beans.identification.IdentificationPeptideBean;
import probe.com.model.beans.identification.IdentificationProteinBean;
import probe.com.model.beans.quant.QuantProtein;
import probe.com.model.beans.identification.StandardIdentificationFractionPlotProteinBean;
import probe.com.model.beans.quant.QuantDatasetInitialInformationObject;
import probe.com.view.body.identificationlayoutcomponents.IdentificationPeptideTable;

/**
 * @author Yehia Farag this class represent the main handler for the
 * application, it works as intermediate layer between visualization layer and
 * logic layer through the main handler responsible for initializing both the
 * logic layer and authenticator handler
 */
public class CSFPRHandler implements Serializable {

    private static final long serialVersionUID = 1L;
    private final CoreLogic logicLayer;
    private final AuthenticatorHandler authenticatorHandler;

    /**
     *
     * @param url database url
     * @param dbName database name
     * @param driver database driver
     * @param userName database username
     * @param password database password
     * @param filesURL url to the locl file system folder
     */
    public CSFPRHandler(String url, String dbName, String driver, String userName, String password, String filesURL) {
        logicLayer = new CoreLogic(url, dbName, driver, userName, password, filesURL);
        authenticatorHandler = new AuthenticatorHandler(url, dbName, driver, userName, password);
    }

    /**
     * get the identification datasets names required for initializing drop down
     * select list
     *
     * @return datasetNamesList
     */
    public TreeMap<Integer, String> getIdentificationDatasetNamesList() {
        return logicLayer.getIdentificationDatasetNamesList();
    }

    /**
     * read and store identification datasets files in the database
     *
     * @param file the dataset file
     * @param MIMEType the file type (txt or xls)
     * @param idDataset dataset bean (in case of update existing dataset)
     * @return test boolean
     * @exception IOException
     * @exception SQLException
     */
    public boolean handelIdentificationDatasetFile(File file, String MIMEType, IdentificationDatasetBean idDataset) throws IOException, SQLException {
        boolean test = logicLayer.handelIdentificationDatasetFile(file, MIMEType, idDataset);
        return test;

    }

    /**
     * get the available id datasets
     *
     * @return datasetsList
     */
    public Map<Integer, IdentificationDatasetBean> getIdentificationDatasetList() {
        return logicLayer.getIdentificationDatasetList();
    }

    /**
     * get datasetIndex List to be removed in the future this function to
     * re-arrange the already stored datasets in the database and return the
     * dataset id
     *
     * @return datasetIndexList
     */
    public Map<Integer, Integer> getIdentificationDatasetIndexList() {
        return logicLayer.getIdentificationDatasetIndexList();
    }

    /**
     * get identification proteins map for a selected dataset
     *
     * @param identificationDatasetId
     * @return Identification proteinsList
     */
    public Map<String, IdentificationProteinBean> getIdentificationProteinsList(int identificationDatasetId) {
        Map<String, IdentificationProteinBean> idProtList = logicLayer.getIdentificationProteinsList(identificationDatasetId);
        return idProtList;
    }

    /**
     * set the selected Identification dataset as main dataset in the logic
     * layer
     *
     * @param datasetId
     */
    public void setMainIdentificationDataset(int datasetId) {
        logicLayer.setMainIdentificationDataset(datasetId);
    }

    /**
     * set the selected dataset as main dataset in the logic layer
     *
     * @param datasetString
     * @return datasetId
     */
    public int setMainIdentificationDatasetId(String datasetString) {
        return logicLayer.setMainIdentificationDataset(datasetString);
    }

    /**
     * get the main identification dataset id
     *
     * @return mainDataset dataset id
     */
    public int getMainDatasetId() {
        return logicLayer.getMainIdentificationDatasetId();
    }

    /**
     * get selected Identification dataset object
     *
     * @param identificationDatasetId
     * @return dataset
     */
    public IdentificationDatasetBean getDataset(int identificationDatasetId) {
        IdentificationDatasetBean idDataset = logicLayer.getDataset(identificationDatasetId);
        return idDataset;
    }

    /**
     * get identification dataset peptides list (for specific dataset)
     *
     * @param datasetId
     * @param validated get validated peptides only
     * @return identification dataset peptide List
     */
    public Map<Integer, IdentificationPeptideBean> getAllIdentificationDatasetPeptidesList(int datasetId, boolean validated) {
        return logicLayer.getAllIdentificationDatasetPeptidesList(datasetId, validated);
    }

    /**
     * check if exporting file is available in the local file system
     * (CSFPRFolder)
     *
     * @param fileName
     * @return test boolean (available or not available)
     */
    public boolean checkFileAvailable(String fileName) {
        return logicLayer.checkFileAvailable(fileName);
    }

    /**
     * get the exporting file absolute pass
     *
     * @param fileName
     * @return url string absolute path to the file
     */
    public String getFileUrl(String fileName) {
        return logicLayer.getFileUrl(fileName);
    }

    /**
     * get identification dataset peptides list for specific dataset(valid
     * peptides or all peptides)
     *
     * @param identificationDatasetId
     * @param validated validated peptides (true/false)
     * @return dataset peptide List
     */
    public Map<Integer, IdentificationPeptideBean> getIdentificationPeptidesList(int identificationDatasetId, boolean validated) {
        return logicLayer.getAllIdentificationDatasetPeptidesList(identificationDatasetId, validated);
    }

    /**
     * get identification dataset peptides number for specific dataset (valid
     * peptides or all peptides)
     *
     * @param datasetId
     * @param validated validated peptides (true/false)
     * @return dataset peptide List
     */
    public int getIdentificationDatasetPeptidesNumber(int datasetId, boolean validated) {
        return logicLayer.getIdentificationDatasetPeptidesNumber(datasetId, validated);
    }

    /**
     * get identification peptides list for selected protein in selected dataset
     *
     * @param datasetId
     * @param accession
     * @param otherAccession
     * @return peptides list for the selected protein group in the selected
     * dataset
     */
    public Map<Integer, IdentificationPeptideBean> getIdentificationProteinPeptidesList(int datasetId, String accession, String otherAccession) {
        Map<Integer, IdentificationPeptideBean> identificationProteinPeptidesList = logicLayer.getIdentificationProteinPeptidesList(datasetId, accession, otherAccession);
        return identificationProteinPeptidesList;
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
        return logicLayer.getIdentificationProteinsGelFractionsList(datasetId, accession, otherAccession);

    }

    /**
     * get identification proteins fractions average list
     *
     * @param accession
     * @param identificationFractionsList
     * @param identificationDatasetId
     * @return dataset peptide List
     */
    public Map<Integer, IdentificationProteinBean> getProteinFractionAvgList(String accession, Map<Integer, IdentificationFractionBean> identificationFractionsList, int identificationDatasetId) {

        return logicLayer.getIdentificationProteinFractionAvgList(accession, identificationFractionsList, identificationDatasetId);
    }

    /**
     * search for identification proteins
     *
     * @param query query object that has all query information
     * @return identificationProtein list
     */
    public Map<Integer, IdentificationProteinBean> searchIdentificationProtein(Query query) {
        return logicLayer.searchIdentficationProtein(query);

    }

    /**
     * retrieve standard proteins data for fraction plot
     *
     * @param identificationDatasetId
     * @return standard Identification fraction Proteins List
     */
    public List<StandardIdentificationFractionPlotProteinBean> getStandardIdentificationFractionProteinsList(int identificationDatasetId) {
        return logicLayer.getStandardIdentificationFractionProteinsList(identificationDatasetId);
    }

    /**
     * retrieve standard proteins data for fraction plot
     *
     * @param identificationDataset
     * @return test boolean
     */
    public boolean updatedatasetData(IdentificationDatasetBean identificationDataset) {
        boolean test = logicLayer.updateDatasetData(identificationDataset);
        return test;

    }

    /**
     * get Authenticator Handler instance
     *
     * @return authenticatorHandler
     */
    public AuthenticatorHandler getAuthenticatorHandler() {
        return authenticatorHandler;
    }

    /**
     * get dataset details list that has basic information for datasets
     *
     * @return datasetDetailsList
     */
    public Map<Integer, IdentificationDatasetDetailsBean> getDatasetDetailsList() {
        Map<Integer, IdentificationDatasetDetailsBean> datasetDetailsList = logicLayer.getDatasetDetailsList();
        return datasetDetailsList;

    }

    /**
     * calculate the number of validated peptides within the giving list
     *
     * @param identificationPeptidesList identification peptide list
     * @return number of validated peptides
     */
    public int countValidatedPeptidesNumber(Map<Integer, IdentificationPeptideBean> identificationPeptidesList) {
        int count = logicLayer.countValidatedPeptidesNumber(identificationPeptidesList);
        return count;
    }

    /**
     * calculate the identification proteins table search indexesSet for the
     * searching in identification proteins table
     *
     * @param protToIndexSearchingMap proteins information supported in the
     * search (accession, other accession, and description) mapped to protein
     * index in the table
     * @param searchMapIndex
     * @param keySearch the keyword used for searching
     * @return the found proteins indexes tree map
     */
    public TreeMap<Integer, Object> calculateIdentificationProteinsTableSearchIndexesSet(Map<String, Integer> protToIndexSearchingMap, Map<String, Integer> searchMapIndex, String keySearch) {
        TreeMap<Integer, Object> indexesTreeMap = logicLayer.calculateIdentificationProteinsTableSearchIndexesSet(protToIndexSearchingMap, searchMapIndex, keySearch);
        return indexesTreeMap;
    }

    /**
     * calculate and get the validated identification proteins within the giving
     * list
     *
     * @param identificationProteinsList identification peptide list
     * @return list of validated identification proteins list
     */
    public Map<Integer, IdentificationProteinBean> getValidatedProteinsList(Map<Integer, IdentificationProteinBean> identificationProteinsList) {
        Map<Integer, IdentificationProteinBean> vProteinsList = logicLayer.getValidatedProteinsList(identificationProteinsList);
        return vProteinsList;

    }

    /**
     * this function to be use for csv peptides exporting with large datasets
     *
     * @param datasetId
     * @param validated boolean the peptides type
     * @param datasetName
     * @param dataType validated/all
     * @param exportFileType csv or xls
     */
    public void exportPeptidesToFile(int datasetId, boolean validated, String datasetName, String dataType, String exportFileType) {
        logicLayer.exportPeptidesToFile(datasetId, validated, datasetName, dataType, exportFileType);

    }

    /**
     * this function to filter the identification search results based on
     * keywords and detect the not found keywords
     *
     * @param identificationProteinsList list of found proteins
     * @param SearchingKeys keyword used for searching
     * @param searchBy searching method (accession,proteins name, or peptide
     * sequence )
     * @return not found keywords within the searching list
     */
    public String filterIdSearchingKeywords(Map<Integer, IdentificationProteinBean> identificationProteinsList, String SearchingKeys, String searchBy) {
        return logicLayer.filterIdSearchingKeywords(identificationProteinsList, SearchingKeys, searchBy);

    }

    /**
     * this function to get the identification hits list from the searching
     * results and group the common proteins in separated lists
     *
     * @param identificationProteinsList list of found proteins
     * @param searchBy searching method (accession,proteins name, or peptide
     * sequence )
     * @return list of identification hits results
     */
    public Map<String, Integer> getIdHitsList(Map<Integer, IdentificationProteinBean> identificationProteinsList, String searchBy) {
        return logicLayer.getIdHitsList(identificationProteinsList, searchBy);

    }

    /**
     * ------- Quant Data--------------------------->
     */
    /**
     * read and store Quant data files in the database
     *
     * @param file the dataset file
     * @param MIMEType the file type (txt or xls)
     * @return test boolean
     * @exception IOException
     * @exception SQLException
     */
    public boolean handelQuantDataFile(File file, String MIMEType) throws IOException, SQLException {
        boolean test = logicLayer.handelQuantDataFile(file, MIMEType);
        return test;

    }

    /**
     * search for quantification proteins
     *
     * @param query query object that has all query information
     * @return quant proteins list
     */
    public List<QuantProtein> searchQuantificationProtein(Query query) {
        return logicLayer.searchQuantificationProteins(query);

    }

    /**
     * get available quantification datasets initial information object that
     * contains the available datasets list and the active columns (to hide them
     * if they are empty)
     *
     * @return QuantDatasetInitialInformationObject
     */
    public QuantDatasetInitialInformationObject getQuantDatasetInitialInformationObject() {
        return logicLayer.getQuantDatasetInitialInformationObject();

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
        return logicLayer.getQuantDatasetInitialInformationObject(searchQuantificationProtList);

    }

    /**
     * get active quantification pie charts filters (to hide them if they are
     * empty)
     *
     * @return boolean array for the active and not active pie chart filters
     * indexes
     */
    public boolean[] getActivePieChartQuantFilters() {
        return logicLayer.getActivePieChartQuantFilters();

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
        return logicLayer.getActivePieChartQuantFilters(searchQuantificationProtList);

    }

    /**
     * this function to filter the quant search results based on keywords and
     * detect the not found keywords
     *
     * @param quantProteinstList list of found proteins
     * @param SearchingKeys keyword used for searching
     * @param searchBy searching method (accession,proteins name, or peptide
     * sequence )
     * @return not found keywords within the searching list
     */
    public String filterQuantSearchingKeywords(List<QuantProtein> quantProteinstList, String SearchingKeys, String searchBy) {

        return logicLayer.filterQuantSearchingKeywords(quantProteinstList, SearchingKeys, searchBy);

    }

    /**
     * this function to get the quant hits list from the searching results and
     * group the common proteins in separated lists
     *
     * @param quantProteinsList list of found proteins
     * @param searchBy searching method (accession,proteins name, or peptide
     * sequence )
     * @return list of quant hits results
     */
    public Map<String, Integer> getQuantHitsList(List<QuantProtein> quantProteinsList, String searchBy) {
        return logicLayer.getQuantHitsList(quantProteinsList, searchBy);

    }

    /**
     * get quant proteins layout for comparison table
     *
     * @param selectedComparisonList selected groups comparison list
     * @param searchQuantificationProtList quant proteins searching list (equal
     * to null if it is not searching layout)
     *
     * @return updated Selected Comparison set
     */
    public Set<QuantGroupsComparison> getComparisonProtList(Set<QuantGroupsComparison> selectedComparisonList, List<QuantProtein> searchQuantificationProtList) {
        return logicLayer.getComparisonProtList(selectedComparisonList, searchQuantificationProtList);

    }

}
