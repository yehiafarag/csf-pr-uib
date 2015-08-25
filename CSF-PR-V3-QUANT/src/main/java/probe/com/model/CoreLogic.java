package probe.com.model;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import probe.com.dal.DataAccess;
import probe.com.dal.Query;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.DiiseaseGroupsComparisonsProtein;
import probe.com.model.beans.identification.IdentificationDatasetBean;
import probe.com.model.beans.identification.IdentificationDatasetDetailsBean;
import probe.com.model.beans.identification.IdentificationFractionBean;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
import probe.com.model.beans.identification.IdentificationPeptideBean;
import probe.com.model.beans.identification.IdentificationProteinBean;
import probe.com.model.beans.quant.QuantProtein;
import probe.com.model.beans.identification.StandardIdentificationFractionPlotProteinBean;
import probe.com.model.beans.quant.QuantDatasetInitialInformationObject;
import probe.com.model.beans.quant.QuantPeptide;
import probe.com.model.util.FileExporter;
import probe.com.model.util.FilesReader;

/**
 * @author Yehia Farag
 */
public class CoreLogic implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final DataAccess da;
    private int mainDatasetId;
    private final String filesURL;
    private final TreeMap<Integer, String> identificationDatasetNamesList = new TreeMap<Integer, String>();//for dropdown select list
    private Map<Integer, IdentificationDatasetBean> identificationDatasetList;
    private final Map<Integer, Integer> datasetIndex = new HashMap<Integer, Integer>();
    private final FileExporter exporter = new FileExporter();

    /**
     * constructor
     *
     * @param url
     * @param dbName
     * @param driver
     * @param userName
     * @param password
     * @param filesURL
     */
    public CoreLogic(String url, String dbName, String driver, String userName, String password, String filesURL) {
        da = new DataAccess(url, dbName, driver, userName, password);

        //just to arrange the already stored datasets -->> to be removed in the future
        datasetIndex.put(8, 1);
        datasetIndex.put(14, 2);
        datasetIndex.put(4, 3);
        datasetIndex.put(17, 4);
        datasetIndex.put(15, 5);
        datasetIndex.put(16, 6);
        datasetIndex.put(9, 7);
//        identificationDatasetList = da.getIdentificationDatasetsList();
        this.filesURL = filesURL;
    }

    /**
     * get the datasets names required for initializing drop down select list
     *
     * @return identificationDatasetNamesList
     */
    public TreeMap<Integer, String> getIdentificationDatasetNamesList() {
        if (identificationDatasetList == null) {
            identificationDatasetList = getIdentificationDatasetList();
        }
        for (int datasetkey : identificationDatasetList.keySet()) {
            //for re-indexing the stored datasets, to be removed in the future
            if (datasetIndex.containsKey(datasetkey)) {
                IdentificationDatasetBean dataset = identificationDatasetList.get(datasetkey);
                identificationDatasetNamesList.put(datasetIndex.get(datasetkey), "\t" + dataset.getName());

            } else {
                IdentificationDatasetBean dataset = identificationDatasetList.get(datasetkey);
                identificationDatasetNamesList.put(datasetkey, "\t" + dataset.getName());
                datasetIndex.put(datasetkey, datasetkey);
            }
        }
        return identificationDatasetNamesList;

    }

    /**
     * read and store datasets files in the database
     *
     * @param file the dataset file
     * @param MIMEType the file type (txt or xls)
     * @param dataset dataset bean (in case of update existing dataset)
     * @return test boolean
     * @exception IOException
     * @exception SQLException
     */
    public boolean handelIdentificationDatasetFile(File file, String MIMEType, IdentificationDatasetBean dataset) throws IOException, SQLException {

        boolean test = false;

        if (dataset.getDatasetFile() == -100)//Standard plot file
        {
            test = da.updateIdentificationStandardPlotProteins(dataset);

        }

        return test;

    }

    /**
     * read and store Quant data files in the database
     *
     * @param file the dataset file
     * @param MIMEType the file type (csv)
     * @return test boolean
     * @exception IOException
     * @exception SQLException
     */
    public boolean handelQuantDataFile(File file, String MIMEType) throws IOException, SQLException {
        FilesReader fr = new FilesReader();
        boolean test;
        List<QuantProtein> quantProtList = fr.readCSVQuantFile(file);
        test = da.storeQuantProt(quantProtList);

        return test;

    }

    /**
     * get the available datasets
     *
     * @return datasetsList
     */
    public Map<Integer, IdentificationDatasetBean> getIdentificationDatasetList() {
        if (identificationDatasetList == null || identificationDatasetList.isEmpty()) {
            identificationDatasetList = da.getIdentificationDatasetsList();
        }
        return identificationDatasetList;
    }

    /**
     * get selected dataset
     *
     * @param datasetId
     * @return dataset
     */
    public IdentificationDatasetBean getDataset(int datasetId) {
        IdentificationDatasetBean dataset = identificationDatasetList.get(datasetId);
        if (dataset == null) {
            dataset = da.retriveIdentficationDataset(datasetId);
            identificationDatasetList.put(datasetId, dataset);
        }
        return dataset;
    }

    /**
     * get proteins map for especial identification dataset
     *
     * @param datasetId
     * @return proteinsList
     */
    public Map<String, IdentificationProteinBean> getIdentificationProteinsList(int datasetId) {
        return da.getIdentificationProteinsList(datasetId);
    }

    /**
     * check if exporting file is available in export folder
     *
     * @param fileName
     * @return test boolean (available or not available)
     */
    public boolean checkFileAvailable(String fileName) {
        File f = new File(filesURL, fileName);
        boolean exist = f.exists();
        return exist;
    }

    /**
     * check if exporting file is available in export folder
     *
     * @param fileName
     * @return url string path to the file
     */
    public String getFileUrl(String fileName) {
        File f = new File(filesURL, fileName);
        String path = f.getPath();
        return path;
    }

    /**
     * get dataset peptides list (valid peptides or all peptides)
     *
     * @param datasetId
     * @param validated validated peptides (true/false)
     * @return dataset peptide List
     */
    public Map<Integer, IdentificationPeptideBean> getAllIdentificationDatasetPeptidesList(int datasetId, boolean validated) {

        return da.getAllIdentificationDatasetPeptidesList(datasetId, validated);
    }

    /**
     * get dataset peptides number (valid peptides or all peptides)
     *
     * @param datasetId
     * @param validated validated peptides (true/false)
     * @return dataset peptide List
     */
    public int getIdentificationDatasetPeptidesNumber(int datasetId, boolean validated) {

        return da.getAllIdentificationDatasetPeptidesNumber(datasetId, validated);
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
        return da.getIdentificationProteinsGelFractionsList(datasetId, accession, otherAccession);
    }

    /**
     * search for proteins by accession keywords
     *
     * @param accession array of query words
     * @param datasetId
     * @param validatedOnly only validated proteins results
     * @return datasetProtList
     */
    private Map<Integer, IdentificationProteinBean> searchProteinByAccession(String accession, int datasetId, boolean validatedOnly) {
        Map<Integer, IdentificationProteinBean> protDatasetpList = da.searchIdentificationProteinByAccession(accession, datasetId, validatedOnly);
        return protDatasetpList;
    }

    /**
     * get peptides list for selected protein in selected dataset
     *
     * @param datasetId
     * @param accession
     * @param otherAccession
     * @return peptides list for the selected protein group in the selected
     * dataset
     */
    public Map<Integer, IdentificationPeptideBean> getIdentificationProteinPeptidesList(int datasetId, String accession, String otherAccession) {
        Map<Integer, IdentificationPeptideBean> peptidesProtList = da.getIdentificationPeptidesList(accession, otherAccession, datasetId);
        return peptidesProtList;
    }

    /**
     * get identification proteins fractions average list
     *
     * @param accession
     * @param identificationFractionsList
     * @param identificationDatasetId
     * @return dataset peptide List
     */
    public Map<Integer, IdentificationProteinBean> getIdentificationProteinFractionAvgList(String accession, Map<Integer, IdentificationFractionBean> identificationFractionsList, int identificationDatasetId) {
        Map<Integer, IdentificationProteinBean> getIdentificationProteinFractionAvgList = new TreeMap<Integer, IdentificationProteinBean>();
        Map<Integer, IdentificationFractionBean> treeIdentificationFractList = new TreeMap<Integer, IdentificationFractionBean>();
        if (identificationFractionsList == null) {
            identificationFractionsList = getProteinFractionList(accession, identificationDatasetId);
        }
        treeIdentificationFractList.putAll(identificationFractionsList);
        for (int key : treeIdentificationFractList.keySet()) {
            IdentificationFractionBean fb = identificationFractionsList.get(key);
            if (fb.getProteinList().containsKey(accession)) {
                getIdentificationProteinFractionAvgList.put(fb.getFractionIndex(), fb.getProteinList().get(accession));
            }
        }
        return getIdentificationProteinFractionAvgList;
    }

    /**
     * get proteins fractions average list
     *
     * @param accession
     * @param datasetId
     * @return dataset peptide List
     */
    public Map<Integer, IdentificationFractionBean> getProteinFractionList(String accession,
            int datasetId) {
        Map<Integer, IdentificationFractionBean> protionFractList = da.getIdentificationProteinFractionList(accession, datasetId);
        return protionFractList;
    }

    /**
     * retrieve standard proteins data for fraction plot
     *
     * @param datasetId
     * @return
     */
    public List<StandardIdentificationFractionPlotProteinBean> getStandardIdentificationFractionProteinsList(int datasetId) {
        if (identificationDatasetList.get(datasetId).getStanderdPlotProt() != null) {
            return identificationDatasetList.get(datasetId).getStanderdPlotProt();
        } else {
            return da.getStandardIdentificationFractionProteinsList(datasetId);
        }
//        getMainDataset().setStanderdPlotProt(standardPlotList);
    }

    /**
     * retrieve standard proteins data for fraction plot
     *
     * @param dataset
     * @return test boolean
     */
    public boolean updateDatasetData(IdentificationDatasetBean dataset) {
        boolean test = da.updateIdentificationDatasetInformation(dataset);
        return test;

    }

    /**
     * get datasetIndex List to be removed in the future this function to
     * re-arrange the already stored datasets in the database and return the
     * dataset id
     *
     * @return datasetIndexList
     */
    public Map<Integer, Integer> getIdentificationDatasetIndexList() {
        return datasetIndex;
    }

    /**
     * get the main dataset
     *
     * @return mainDataset
     */
    public int getMainIdentificationDatasetId() {
        return mainDatasetId;
    }

    /**
     * get identification dataset id using dataset name
     *
     * @param datasetString
     * @return identification dataset id
     */
    public int getIdentificationDatasetKey(String datasetString) {
        for (int key1 : identificationDatasetNamesList.keySet()) {
            if (datasetString.equalsIgnoreCase(identificationDatasetNamesList.get(key1))) {
                for (int k : datasetIndex.keySet()) {
                    int value = datasetIndex.get(k);
                    if (value == key1) {
                        return k;
                    }
                }
                return datasetIndex.get(key1);
            }
        }
        return 0;
    }

    /**
     * set the main dataset selected by user
     *
     * @param datasetString string from drop down select list
     * @return datasetId
     */
    public int setMainIdentificationDataset(String datasetString) {
        if (identificationDatasetNamesList.isEmpty()) {
            getIdentificationDatasetNamesList();
        }

        for (int tempDatasetIndex : identificationDatasetNamesList.keySet()) {
            if (datasetString.trim().equalsIgnoreCase(identificationDatasetNamesList.get(tempDatasetIndex).trim())) {

                for (int k : datasetIndex.keySet()) {
                    int value = datasetIndex.get(k);
                    if (value == tempDatasetIndex) {
                        CoreLogic.this.setMainIdentificationDataset(k);
                        return k;
                    }
                }
                CoreLogic.this.setMainIdentificationDataset(tempDatasetIndex);
                return tempDatasetIndex;
            }
        }
        return 0;
    }

    /**
     * set the selected dataset as main dataset in the logic layer
     *
     * @param datasetId
     */
    public void setMainIdentificationDataset(int datasetId) {
        this.mainDatasetId = datasetId;
    }

    /**
     * get dataset details list that has basic information for datasets
     *
     * @return datasetDetailsList
     */
    public Map<Integer, IdentificationDatasetDetailsBean> getIdentificationDatasetDetailsList() {
        Map<Integer, IdentificationDatasetDetailsBean> datasetDetailsList = new HashMap<Integer, IdentificationDatasetDetailsBean>();
        for (IdentificationDatasetBean dataset : identificationDatasetList.values()) {
            IdentificationDatasetDetailsBean datasetDetails = new IdentificationDatasetDetailsBean();
            datasetDetails.setName(dataset.getName());
            datasetDetails.setFragMode(dataset.getFragMode());
            datasetDetails.setInstrumentType(dataset.getInstrumentType());
            datasetDetails.setSampleType(dataset.getSampleType());
            datasetDetails.setSampleProcessing(dataset.getSampleProcessing());
            datasetDetails.setSpecies(dataset.getSpecies());
            datasetDetailsList.put(dataset.getDatasetId(), datasetDetails);

        }
        return datasetDetailsList;

    }

    /**
     * calculate the number of validated peptides within the giving list
     *
     * @param identificationPeptidesList identification peptide list
     * @return number of validated peptides
     */
    public int countValidatedPeptidesNumber(Map<Integer, IdentificationPeptideBean> identificationPeptidesList) {
        int count = 0;
        for (IdentificationPeptideBean pb : identificationPeptidesList.values()) {
            if (pb.getValidated() == 1.0) {
                count++;
            }
        }
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
     * @return number of validated peptides
     */
    public TreeMap<Integer, Object> calculateIdentificationProteinsTableSearchIndexesSet(Map<String, Integer> protToIndexSearchingMap, Map<String, Integer> searchMapIndex, String keySearch) {
        TreeMap<Integer, Object> treeSet = new TreeMap<Integer, Object>();
        for (String key : protToIndexSearchingMap.keySet()) {
            if (key.contains(keySearch)) {
                treeSet.put(searchMapIndex.get(key), protToIndexSearchingMap.get(key));
            }
        }

        return treeSet;
    }

    /**
     * calculate and get the validated identification proteins within the giving
     * list
     *
     * @param identificationProteinsList identification peptide list
     * @return list of validated identification proteins list
     */
    public Map<Integer, IdentificationProteinBean> getValidatedIdentificationProteinsList(Map<Integer, IdentificationProteinBean> identificationProteinsList) {
        Map<Integer, IdentificationProteinBean> vProteinsList = new HashMap<Integer, IdentificationProteinBean>();
        for (int str : identificationProteinsList.keySet()) {
            IdentificationProteinBean pb = identificationProteinsList.get(str);
            if (pb.isValidated()) {
                vProteinsList.put(str, pb);
            }

        }
        return vProteinsList;

    }

    /**
     * get available quantification datasets initial information object that
     * contains the available datasets list and the active columns (to hide them
     * if they are empty)
     *
     * @return QuantDatasetInitialInformationObject
     */
    public QuantDatasetInitialInformationObject getQuantDatasetInitialInformationObject() {
        return da.getQuantDatasetInitialInformationObject();

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
        return da.getQuantDatasetInitialInformationObject(searchQuantificationProtList);

    }

    /**
     * get active quantification pie charts filters (to hide them if they are
     * empty)
     *
     * @return boolean array for the active and not active pie chart filters
     * indexes
     */
    public boolean[] getActivePieChartQuantFilters() {
        return da.getActivePieChartQuantFilters();

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
        return da.getActivePieChartQuantFilters(searchQuantificationProtList);

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
        if (quantProteinstList == null || quantProteinstList.isEmpty()) {
            return SearchingKeys;
        }
        HashSet<String> usedKeys = new HashSet<String>();
        HashSet<String> tUsedKeys = new HashSet<String>();
        for (String key : SearchingKeys.split("\n")) {
            if (key.trim().length() > 3) {
                usedKeys.add(key.toUpperCase());
            }
        }
        tUsedKeys.addAll(usedKeys);
        for (QuantProtein pb : quantProteinstList) {
            if (searchBy.equals("Protein Accession")) {
                if (usedKeys.contains(pb.getUniprotAccession())) {
                    usedKeys.remove(pb.getUniprotAccession());
                } else if (usedKeys.contains(pb.getPublicationAccNumber())) {
                    usedKeys.remove(pb.getPublicationAccNumber());
                }

                if (usedKeys.isEmpty()) {
                    return "";
                }
            } else if (searchBy.equals("Protein Name")) {
                for (String key : tUsedKeys) {
                    if (pb.getUniprotProteinName().contains(key.toUpperCase())) {
                        usedKeys.remove(key);
                    } else if (pb.getPublicationProteinName().contains(key.toUpperCase())) {
                        usedKeys.remove(key);
                    }
                    if (usedKeys.isEmpty()) {
                        return "";
                    }
                }

            } else {
                return "";
            }
        }
        return usedKeys.toString().replace("[", "").replace("]", "");

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
        Map<String, Integer> quantHitsList = new HashMap<String, Integer>();
        if (quantProteinsList == null || quantProteinsList.isEmpty()) {
            return quantHitsList;
        }
        String key;

        for (QuantProtein prot : quantProteinsList) {

            if (searchBy.equalsIgnoreCase("Protein Accession")/* ||*/) {
                key = prot.getUniprotAccession().trim() + "__" + prot.getUniprotProteinName().trim();
            } else {
                key = prot.getUniprotProteinName().trim();

            }

            if (!quantHitsList.containsKey(key)) {
                quantHitsList.put(key, 0);
            }
            int value = quantHitsList.get(key);
            value++;
            quantHitsList.put(key, value);

        }
        return quantHitsList;

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
        Map<Integer, IdentificationPeptideBean> allPeptides = getAllIdentificationDatasetPeptidesList(datasetId, validated);
        if (exportFileType.equalsIgnoreCase("csv")) {
            exporter.expotIdentificationPeptidesToCSV(allPeptides, datasetName, dataType, filesURL);
        } else {
            exporter.expotIdentificationPeptidesToXLS(allPeptides, datasetName, dataType, filesURL);
        }
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
    public String filterIdentificationSearchingKeywords(Map<Integer, IdentificationProteinBean> identificationProteinsList, String SearchingKeys, String searchBy) {
        if (identificationProteinsList == null || identificationProteinsList.isEmpty()) {
            return SearchingKeys;
        }
        HashSet<String> usedKeys = new HashSet<String>();
        HashSet<String> tUsedKeys = new HashSet<String>();
        for (String key : SearchingKeys.split("\n")) {
            if (key.trim().length() > 3) {
                usedKeys.add(key.toUpperCase());
            }
        }
        tUsedKeys.addAll(usedKeys);
        for (IdentificationProteinBean pb : identificationProteinsList.values()) {
            if (searchBy.equals("Protein Accession")) {
                if (usedKeys.contains(pb.getAccession())) {
                    usedKeys.remove(pb.getAccession());
                }
                if (usedKeys.isEmpty()) {
                    return "";
                }
            } else if (searchBy.equals("Protein Name")) {
                for (String key : tUsedKeys) {
                    if (pb.getDescription().contains(key.toUpperCase())) {
                        usedKeys.remove(key);
                    }
                    if (usedKeys.isEmpty()) {
                        return "";
                    }
                }

            } else {
                return "";
            }
        }
        return usedKeys.toString().replace("[", "").replace("]", "");

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
    public Map<String, Integer> getIdentificationHitsList(Map<Integer, IdentificationProteinBean> identificationProteinsList, String searchBy) {
        Map<String, Integer> idHitsList = new HashMap<String, Integer>();
        if (identificationProteinsList == null || identificationProteinsList.isEmpty()) {
            return idHitsList;
        }
        String key;

        for (IdentificationProteinBean prot : identificationProteinsList.values()) {

            if (searchBy.equalsIgnoreCase("Protein Accession")) {
                key = prot.getAccession().trim() + "__" + prot.getOtherProteins().trim() + "__" + prot.getDescription().trim();
            } else {
                key = prot.getDescription().trim();
            }

            if (!idHitsList.containsKey(key)) {
                idHitsList.put(key, 0);
            }
            int value = idHitsList.get(key);
            value++;
            idHitsList.put(key, value);

        }
        return idHitsList;

    }


    /*             *********************************************************8       */
    /**
     * search for proteins by description keywords
     *
     * @param query query words
     * @return datasetProteinsSearchList
     */
    public Map<Integer, IdentificationProteinBean> searchIdentficationProtein(Query query) {

        Map<Integer, IdentificationProteinBean> datasetProteinsSearchList = new HashMap<Integer, IdentificationProteinBean>();
        if (query.getSearchDataType().equals("Identification Data")) {
            if (query.getSearchDataset() == null || query.getSearchDataset().isEmpty())//search in all identification datasets
            {
                if (query.getSearchBy().equalsIgnoreCase("Protein Accession"))//"Protein Name" "Peptide Sequence"
                {
                    return da.searchIdentificationProteinAllDatasetsByAccession(query.getSearchKeyWords(), query.isValidatedProteins());
                } else if (query.getSearchBy().equalsIgnoreCase("Protein Name")) {
                    return da.searchIdentificationProteinAllDatasetsByName(query.getSearchKeyWords(), query.isValidatedProteins());

                } else if (query.getSearchBy().equalsIgnoreCase("Peptide Sequence")) {
                    return da.SearchIdentificationProteinAllDatasetsByPeptideSequence(query.getSearchKeyWords(), query.isValidatedProteins());
                }

            } else {
                int tempDatasetIndex = -1;
                for (IdentificationDatasetBean ds : identificationDatasetList.values()) {
                    if (ds.getName().trim().equalsIgnoreCase(query.getSearchDataset().trim())) {
                        tempDatasetIndex = ds.getDatasetId();
                        System.out.println("dataset Index " + tempDatasetIndex);
                        break;
                    }
                }
                if (query.getSearchBy().equalsIgnoreCase("Protein Accession"))//"Protein Name" "Peptide Sequence"
                {
                    return da.searchIdentificationProteinByAccession(query.getSearchKeyWords(), tempDatasetIndex, query.isValidatedProteins());
                } else if (query.getSearchBy().equalsIgnoreCase("Protein Name")) {
                    return da.searchIdentificationProteinByName(query.getSearchKeyWords(), tempDatasetIndex, query.isValidatedProteins());

                } else if (query.getSearchBy().equalsIgnoreCase("Peptide Sequence")) {

                    return da.SearchIdentificationProteinByPeptideSequence(query.getSearchKeyWords(), tempDatasetIndex, query.isValidatedProteins());
                }

            }

        }

        return datasetProteinsSearchList;

    }

    /**
     * search for proteins by description keywords
     *
     * @param query query words
     * @return datasetProteinsSearchList
     */
    public List<QuantProtein> searchQuantificationProteins(Query query) {

        if (query.getSearchDataType().equals("Quantification Data")) {
            List<QuantProtein> datasetQuantificationProteinsSearchList = da.searchQuantificationProteins(query);
            return datasetQuantificationProteinsSearchList;
        }
        return null;

    }

    /**
     * get quant proteins layout for comparison table
     *
     * @param selectedComparisonList selected groups comparison list
     * @param searchQuantificationProtList searching results (null allowed here)
     *
     * @return updated Selected Comparison List
     */
    public Set<QuantDiseaseGroupsComparison> getComparisonProtList(Set<QuantDiseaseGroupsComparison> selectedComparisonList, List<QuantProtein> searchQuantificationProtList) {

        Set<QuantDiseaseGroupsComparison> updatedSelectedComparisonList = new HashSet<QuantDiseaseGroupsComparison>();
        for (QuantDiseaseGroupsComparison comparison : selectedComparisonList) {
            Set<QuantProtein> comparisonProtMap = new HashSet<QuantProtein>();
            Map<String, Set<QuantPeptide>> comparisonPeptideMap = new HashMap<String, Set<QuantPeptide>>();
//
//            for (int dsID : comparison.getDatasetIndexes()) {
//
//                if (searchQuantificationProtList != null) {
//                    for (QuantProtein qprot : searchQuantificationProtList) {
//                        if (qprot.getDsKey() == (dsID + 1)) {
//                            comparisonProtMap.add(qprot);
//                        }
//                    }
//
//                    
//
//                } else {
//                    comparisonProtMap.addAll(da.getQuantificationProteins(dsID));
//                }
////                comparisonPeptideMap.putAll(da.getQuantificationPeptides(comparison.getDatasetIndexes()));
//
//            }
            if (searchQuantificationProtList != null) {
                for (int dsID : comparison.getDatasetIndexes()) {

                    for (QuantProtein qprot : searchQuantificationProtList) {
                        if (qprot.getDsKey() == (dsID + 1)) {
                            comparisonProtMap.add(qprot);
                        }
                    }

                }
            } else {
                comparisonProtMap.addAll(da.getQuantificationProteins(comparison.getDatasetIndexes()));
            }

        

        comparisonPeptideMap.putAll(da.getQuantificationPeptides(comparison.getDatasetIndexes()));

        Map<String, DiiseaseGroupsComparisonsProtein> comparProtList = new HashMap<String, DiiseaseGroupsComparisonsProtein>();
        for (QuantProtein quant : comparisonProtMap) {

            {

                if (!comparProtList.containsKey(quant.getUniprotAccession())) {
                    comparProtList.put(quant.getUniprotAccession(), new DiiseaseGroupsComparisonsProtein(comparison.getDatasetIndexes().length, comparison, quant.getProtKey()));
                }
                DiiseaseGroupsComparisonsProtein comProt = comparProtList.get(quant.getUniprotAccession());
                if (quant.getStringFCValue().equalsIgnoreCase("Decreased")) {
                    comProt.addDown(1, (quant.getPatientsGroupINumber() + quant.getPatientsGroupIINumber()), quant.getDsKey());
                } else if (quant.getStringFCValue().equalsIgnoreCase("Increased")) {
                    comProt.addUp(1, (quant.getPatientsGroupINumber() + quant.getPatientsGroupIINumber()), quant.getDsKey());
                } else if (quant.getStringFCValue().equalsIgnoreCase("Not Provided")) {
                    comProt.addNotProvided(1, (quant.getPatientsGroupINumber() + quant.getPatientsGroupIINumber()), quant.getDsKey());
                } else if (quant.getStringFCValue().equalsIgnoreCase("Not Regulated")) {
                    comProt.addNotReg(1, (quant.getPatientsGroupINumber() + quant.getPatientsGroupIINumber()), quant.getDsKey());
                }
                comProt.setUniProtAccess(quant.getUniprotAccession());
                String protName = quant.getUniprotProteinName();
                if (protName == null || protName.equalsIgnoreCase("Not Available")) {
                    protName = quant.getPublicationProteinName();
                }
                comProt.setProtName(protName);
                comProt.setSequance(quant.getSequance());
                comparProtList.put(quant.getUniprotAccession(), comProt);

                Set<QuantPeptide> quantPeptidesList = new HashSet<QuantPeptide>();
                for (String key : comparisonPeptideMap.keySet()) {
                    if (key.contains("_" + comProt.getUniProtAccess() + "_")) {
                        quantPeptidesList.addAll(comparisonPeptideMap.get(key));
                    }
                }
                comProt.setQuantPeptidesList(quantPeptidesList);
            }
        }

            //init pep for prot
        //sort the protiens map
        Map<String, DiiseaseGroupsComparisonsProtein> sortedcomparProtList = new TreeMap<String, DiiseaseGroupsComparisonsProtein>(Collections.reverseOrder());
        for (String Key : comparProtList.keySet()) {
            DiiseaseGroupsComparisonsProtein temp = comparProtList.get(Key);
            sortedcomparProtList.put((temp.getUp() + "_" + Key), temp);
        }

        comparison.setComparProtsMap(sortedcomparProtList);
        updatedSelectedComparisonList.add(comparison);

    }

    return updatedSelectedComparisonList ;

}

}
