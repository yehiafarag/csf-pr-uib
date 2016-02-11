package probe.com.handlers;

import com.vaadin.ui.Panel;
import java.sql.SQLException;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.jfree.chart.JFreeChart;
import probe.com.dal.Query;
import probe.com.model.CoreLogic;
import probe.com.model.beans.OverviewInfoBean;
import probe.com.model.beans.identification.IdentificationDatasetBean;
import probe.com.model.beans.identification.IdentificationDatasetDetailsBean;
import probe.com.model.beans.identification.IdentificationFractionBean;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
import probe.com.model.beans.identification.IdentificationPeptideBean;
import probe.com.model.beans.identification.IdentificationProteinBean;
import probe.com.model.beans.quant.QuantProtein;
import probe.com.model.beans.identification.StandardIdentificationFractionPlotProteinBean;
import probe.com.model.beans.quant.QuantDatasetInitialInformationObject;
import probe.com.model.beans.quant.QuantDatasetObject;
import probe.com.model.util.vaadintoimageutil.peptideslayout.ProteinInformationDataForExport;
import probe.com.view.HeaderLayout;
import probe.com.view.core.TipGenerator;

/**
 * @author Yehia Farag
 *
 * This class represent the main handler for the application, it works as
 * intermediate layer between visualization layer and logic layer through the
 * main handler responsible for initializing both the logic layer and
 * authenticator handler
 */
public class CSFPRHandler implements Serializable {

    private static final long serialVersionUID = 1L;
    private final CoreLogic logicLayer;
    private final AuthenticatorHandler authenticatorHandler;
    private final Map<String, String> diseaseFullNameMap;
    private final Map<String, Map<String, String>> default_DiseaseCat_DiseaseGroupMap;
    private final Map<String, QuantDatasetInitialInformationObject> quantDatasetInitialInformationObject;
    private final Map<String, String> diseaseHashedColorMap = new HashMap<String, String>();
    private final Map<String, String> diseaseStyleMap = new HashMap<String, String>();

    public Map<String, String> getDiseaseStyleMap() {
        return diseaseStyleMap;
    }

    private final String suggestNames = "Alzheimer's\n"
            + "CIS\n"
            + "CIS-CIS\n"
            + "CIS-MS\n"
            + "Healthy\n"
            + "Healthy\n"
            + "MCI\n"
            + "MCI-MCI\n"
            + "MCI-Alzheimer's\n"
            + "MS treated\n"
            + "MS treated\n"
            + "Neurological\n"
            + "Neurological\n"
            + "Neurological\n"
            + "Neurological\n"
            + "Neurological\n"
            + "Non Alzheimer's\n"
            + "Non Demented\n"
            + "Non Neurodeg.\n"
            + "Parkinson's\n"
            + "PDD\n"
            + "Progressive MS\n"
            + "Progressive MS\n"
            + "RRMS";
    private final String oreginalNames = "Alzheimer's\n"
            + "CIS-MS(CIS)\n"
            + "CIS-CIS\n"
            + "CIS-MS\n"
            + "Aged healthy\n"
            + "Healthy controls\n"
            + "MCI\n"
            + "MCI nonprogressors\n"
            + "MCI progressors\n"
            + "RRMS Nataliz.\n"
            + "SPMS Lamotri.\n"
            + "Non MS\n"
            + "OIND\n"
            + "OIND + OND\n"
            + "OND\n"
            + "Sympt. controls\n"
            + "Aged controls\n"
            + "NDC\n"
            + "Non-neurodeg.\n"
            + "Parkinson's\n"
            + "PDD\n"
            + "PMS\n"
            + "SPMS\n"
            + "RRMS";

    public TipGenerator getTipsGenerator() {
        return tipsGenerator;
    }
    private final TipGenerator tipsGenerator = new TipGenerator();

    /**
     * create the main handler for all users requests
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
        this.diseaseFullNameMap = logicLayer.getDiseaseFullNameMap();
        this.quantDatasetInitialInformationObject = logicLayer.getQuantDatasetInitialInformationObject();
        default_DiseaseCat_DiseaseGroupMap = new LinkedHashMap<String, Map<String, String>>();
        for (String str : quantDatasetInitialInformationObject.keySet()) {
            if (str.equalsIgnoreCase("All")) {
                continue;
            }
            Set<String> diseaseGroupsName = logicLayer.getDiseaseGroupNameMap(str);

            Map<String, String> diseaseGroupMap = new LinkedHashMap<String, String>();
            for (int i = 0; i < oreginalNames.split("\n").length; i++) {
                if (!diseaseGroupsName.contains(oreginalNames.split("\n")[i])) {
                    continue;
                }
                diseaseGroupMap.put(oreginalNames.split("\n")[i], suggestNames.split("\n")[i]);
            }
            default_DiseaseCat_DiseaseGroupMap.put(str, diseaseGroupMap);
        }
        diseaseHashedColorMap.put("Multiple_Sclerosis_Disease", "#A52A2A");
        diseaseHashedColorMap.put("Alzheimer-s_Disease", "#4b7865");
        diseaseHashedColorMap.put("Parkinson-s_Disease", "#74716E");
        diseaseHashedColorMap.put("Amyotrophic_Lateral_Sclerosis_Disease", "#7D0725");
        diseaseHashedColorMap.put("UserData", "#8210B0");
        diseaseStyleMap.put("Parkinson-s_Disease", "pdLabel");
        diseaseStyleMap.put("Alzheimer-s_Disease", "adLabel");
        diseaseStyleMap.put("Amyotrophic_Lateral_Sclerosis_Disease", "alsLabel");
        diseaseStyleMap.put("Multiple_Sclerosis_Disease", "msLabel");

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
     * @param idDataset identification dataset bean (in case of update existing
     * dataset)
     * @return test boolean
     * @exception IOException
     * @exception SQLException
     */
    public boolean handelIdentificationDatasetFile(File file, String MIMEType, IdentificationDatasetBean idDataset) throws IOException, SQLException {
        return logicLayer.handelIdentificationDatasetFile(file, MIMEType, idDataset);
    }

    /**
     * get the available id datasets
     *
     * @return identification datasetsList
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

    public Map<String, String> getDiseaseFullNameMap() {
        return diseaseFullNameMap;
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
    public Map<Integer, IdentificationDatasetDetailsBean> getIdentificationDatasetDetailsList() {
        Map<Integer, IdentificationDatasetDetailsBean> datasetDetailsList = logicLayer.getIdentificationDatasetDetailsList();
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
    public Map<Integer, IdentificationProteinBean> getValidatedIdentificationProteinsList(Map<Integer, IdentificationProteinBean> identificationProteinsList) {
        Map<Integer, IdentificationProteinBean> vProteinsList = logicLayer.getValidatedIdentificationProteinsList(identificationProteinsList);
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
    public void exportIdentificationPeptidesToFile(int datasetId, boolean validated, String datasetName, String dataType, String exportFileType) {
        logicLayer.exportIdentificationPeptidesToFile(datasetId, validated, datasetName, dataType, exportFileType);

    }

    public byte[] exportProteinsInfoCharts(Set<JFreeChart> component, String fileName, String title, Set<ProteinInformationDataForExport> peptidesSet) {
        return logicLayer.exportProteinsInfoCharts(component, fileName, title, peptidesSet);

    }

    public byte[] exportStudiesInformationPieCharts(Set<JFreeChart> component, String fileName, String title) {
        return logicLayer.exportStudiesInformationPieCharts(component, fileName, title);

    }

    public byte[] exportBubbleChartAsPdf(JFreeChart chart, String fileName, String title) {
        return logicLayer.exportBubbleChartAsPdf(chart, fileName, title);
    }

    public byte[] exportfullReportAsZip(Map<String, Set<JFreeChart>> chartsMap, String fileName, String title, Set<ProteinInformationDataForExport> peptidesSet) {
        return logicLayer.exportfullReportAsZip(chartsMap, fileName, title, peptidesSet);
//          
//         "";//url + userFolder.getName() + "/" + pdfFile.getName();

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
        return logicLayer.filterIdentificationSearchingKeywords(identificationProteinsList, SearchingKeys, searchBy);

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
    public Map<String, Integer> getIdHitsList(Map<Integer, IdentificationProteinBean> identificationProteinsList, String searchBy, String mainProt) {
        return logicLayer.getIdentificationHitsList(identificationProteinsList, searchBy, mainProt);

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
     * @param toCompare
     * @return quant proteins list
     */
    public List<QuantProtein> searchQuantificationProtein(Query query, boolean toCompare) {
        return logicLayer.searchQuantificationProteins(query, toCompare);

    }

    /**
     * get available quantification datasets initial information object that
     * contains the available datasets list and the active columns (to hide them
     * if they are empty)
     *
     * @return QuantDatasetInitialInformationObject
     */
    public Map<String, QuantDatasetInitialInformationObject> getQuantDatasetInitialInformationObject() {
        return quantDatasetInitialInformationObject;

    }

    /**
     * get available quantification datasets initial information object within
     * quant searching proteins results that contains the available datasets
     * list and the active columns (to hide them if they are empty)
     *
     * @param searchQuantificationProtList
     * @return QuantDatasetInitialInformationObject
     */
    public Map<String, QuantDatasetInitialInformationObject> getQuantDatasetInitialInformationObject(List<QuantProtein> searchQuantificationProtList) {
        return logicLayer.getQuantDatasetInitialInformationObject(searchQuantificationProtList);

    }

    /**
     * get active quantification pie charts filters (to hide them if they are
     * empty)
     *
     * @return boolean array for the active and not active pie chart filters
     * indexes
     */
    public Map<String, boolean[]> getActivePieChartQuantFilters() {
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
    public Map<String, boolean[]> getActivePieChartQuantFilters(List<QuantProtein> searchQuantificationProtList) {
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
    public Set<QuantDiseaseGroupsComparison> getComparisonProtList(Set<QuantDiseaseGroupsComparison> selectedComparisonList, List<QuantProtein> searchQuantificationProtList, Map<String, Set<String>> diseaseGroupsHeaderToOregenalDiseaseGroupsNames) {

        return logicLayer.getComparisonProtList(selectedComparisonList, searchQuantificationProtList, diseaseGroupsHeaderToOregenalDiseaseGroupsNames);

    }

    public QuantDiseaseGroupsComparison initUserCustomizedComparison(String diseaseGroupI, String diseaseGroupII, Set<String> highAcc, Set<String> stableAcc, Set<String> lowAcc) {
        return logicLayer.initUserCustomizedComparison(diseaseGroupI, diseaseGroupII, highAcc, stableAcc, lowAcc);

    }

    /**
     * k-means clustering for protein
     *
     * @param samples the samples
     * @param sampleIds the sample identifiers
     * @param numClusters the number of clusters
     * @param proteinKey protein key
     * @return list of clustered proteins
     */
    public ArrayList<String> runKMeanClustering(double[][] samples, String[] sampleIds, int numClusters, String proteinKey) {
        return this.logicLayer.runKMeanClustering(samples, sampleIds, numClusters, proteinKey);
    }

    public OverviewInfoBean getResourceOverviewInformation() {
        return this.logicLayer.getResourceOverviewInformation();

    }

    public Map<String, Map<String, String>> getDefault_DiseaseCat_DiseaseGroupMap() {
        return default_DiseaseCat_DiseaseGroupMap;
    }

    private HeaderLayout header;

    public void controlHeaderHeights(int height) {
        header.setHeight(height + "px");

    }

    public float getHeaderHeight() {
        return header.getHeaderHeight();
    }

    public void setHeader(HeaderLayout header) {
        this.header = header;
    }

    public List<Object[]> getPublicationList() {

        return this.logicLayer.getPublicationList();

    }

    public Set<QuantDatasetObject> getQuantDatasetList() {
        return this.logicLayer.getQuantDatasetList();

    }

    public Map<String, String> getDiseaseHashedColorMap() {
        return diseaseHashedColorMap;
    }


   

}
