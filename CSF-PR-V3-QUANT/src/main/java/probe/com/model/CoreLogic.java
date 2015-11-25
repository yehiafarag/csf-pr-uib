package probe.com.model;

import com.vaadin.server.VaadinSession;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import probe.com.dal.DataAccess;
import probe.com.dal.Query;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.DiseaseGroupsComparisonsProteinLayout;
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
import probe.com.model.util.KMeansClustering;

import org.jfree.chart.JFreeChart;
import probe.com.model.beans.OverviewInfoBean;
import probe.com.model.util.vaadintoimageutil.peptideslayout.ProteinInformationDataForExport;
import probe.com.view.core.exporter.ImageToExport;
//import org.apache.batik.svggen.SVGGraphics2D;
//import org.apache.batik.dom.svg.SVGDOMImplementation;
//import org.apache.batik.dom.svg12.SVG12DOMImplementation;
//
//import org.apache.batik.transcoder.TranscoderInput;
//import org.apache.batik.transcoder.TranscoderOutput;

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
    private final String userFolderUrl;
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
        this.filesURL = filesURL;
//        this.userFolderUrl = filesURL + "/" + VaadinSession.getCurrent().getSession().getId();
        File csfFolder = new File(filesURL, VaadinSession.getCurrent().getSession().getId());
        csfFolder.mkdir();
        this.userFolderUrl = csfFolder.getAbsolutePath();

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
        if (identificationDatasetList == null) {
            identificationDatasetList = getIdentificationDatasetList();
        }
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
    public Map<String, QuantDatasetInitialInformationObject> getQuantDatasetInitialInformationObject() {
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
    public Map<String, QuantDatasetInitialInformationObject> getQuantDatasetInitialInformationObject(List<QuantProtein> searchQuantificationProtList) {
        return da.getQuantDatasetInitialInformationObject(searchQuantificationProtList);

    }

    /**
     * get active quantification pie charts filters (to hide them if they are
     * empty)
     *
     * @return boolean array for the active and not active pie chart filters
     * indexes
     */
    public Map<String, boolean[]> getActivePieChartQuantFilters() {
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
    public Map<String, boolean[]> getActivePieChartQuantFilters(List<QuantProtein> searchQuantificationProtList) {
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
                System.out.println("prot.getUniprotProteinName().trim() " + prot.getUniprotProteinName().trim());
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

    public byte[] exportProteinsInfoCharts(Set<JFreeChart> component, String fileName, String title) {
        return exporter.exportProteinsInfoCharts(component, fileName, userFolderUrl, title);
//          
//         "";//url + userFolder.getName() + "/" + pdfFile.getName();

    }
      public byte[] exportStudiesInformationPieCharts(Set<JFreeChart> component, String fileName, String title) {
        return exporter.exportStudiesInformationPieCharts(component, fileName, userFolderUrl, title);
//          
//         "";//url + userFolder.getName() + "/" + pdfFile.getName();

    }

    public byte[] exportBubbleChartAsPdf(JFreeChart chart, String fileName, String title) {
        return exporter.exportBubbleChartAsPdf(chart, fileName, userFolderUrl, title);
    }

    public byte[] exportfullReportAsZip( Map<String,Set<JFreeChart>> chartsMap, String fileName,String title,Set<ProteinInformationDataForExport> peptidesSet) {
        return exporter.exportfullReportAsZip(chartsMap, fileName, userFolderUrl,title,peptidesSet);
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
    public Map<String, Integer> getIdentificationHitsList(Map<Integer, IdentificationProteinBean> identificationProteinsList, String searchBy, String mainProt) {
        final TreeSet<String> usedKeys = new TreeSet<String>();
        for (String key : mainProt.split("\n")) {
            if (key.trim().length() > 3) {
                usedKeys.add(key);
            }
        }

        Map<String, Integer> idHitsList = new TreeMap<String, Integer>();
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
        if (!idHitsList.keySet().toArray()[0].toString().startsWith(usedKeys.pollFirst())) {
            Map<String, Integer> revIdHitsList = new TreeMap<String, Integer>(Collections.reverseOrder());
            revIdHitsList.putAll(idHitsList);
            return revIdHitsList;
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
//    public Set<QuantDiseaseGroupsComparison> getComparisonProtList1(Set<QuantDiseaseGroupsComparison> selectedComparisonList, List<QuantProtein> searchQuantificationProtList) {
//
//        Set<QuantDiseaseGroupsComparison> updatedSelectedComparisonList = new LinkedHashSet<QuantDiseaseGroupsComparison>();
//        for (QuantDiseaseGroupsComparison comparison : selectedComparisonList) {
//            Set<QuantProtein> comparisonProtMap = new HashSet<QuantProtein>();
//            if (searchQuantificationProtList != null) {
//                for (int dsID : comparison.getDatasetIndexes()) {
//                    for (QuantProtein qprot : searchQuantificationProtList) {
//                        if (qprot.getDsKey() == (dsID)) {
//                            comparisonProtMap.add(qprot);
//                        }
//                    }
//
//                }
//            } else {
//                Object[] iArr = new Object[comparison.getDatasetIndexes().length];
//                for (int x = 0; x < iArr.length; x++) {
//                    iArr[x] = comparison.getDatasetIndexes()[x];
//                }
//                comparisonProtMap.addAll(da.getQuantificationProteins(iArr));
//
//            }
//            Object[] iArr = new Object[comparison.getDatasetIndexes().length];
//            for (int x = 0; x < iArr.length; x++) {
//                iArr[x] = comparison.getDatasetIndexes()[x];
//            }
//            Map<String, Set<QuantPeptide>> comparisonPeptideMap = (da.getQuantificationPeptides(iArr));
//
//            Map<String, DiseaseGroupsComparisonsProteinLayout> comparProtList = new HashMap<String, DiseaseGroupsComparisonsProteinLayout>();
//            String pGrI = comparison.getComparisonHeader().split("vs")[0].trim();
//            String pGrII = comparison.getComparisonHeader().split("vs")[1].trim();
//
//            for (QuantProtein quant : comparisonProtMap) {
//                boolean inverted = false;
//                String protAcc = quant.getUniprotAccession();
//                if (protAcc.equalsIgnoreCase("") || protAcc.equalsIgnoreCase("Not Available") || protAcc.equalsIgnoreCase("Entry Deleted") || protAcc.equalsIgnoreCase("Entry Demerged") || protAcc.equalsIgnoreCase("NOT RETRIEVED") || protAcc.equalsIgnoreCase("DELETED")) {
//                    protAcc = quant.getPublicationAccNumber();
//
//                }
//                if (!comparProtList.containsKey(protAcc)) {
//                    DiseaseGroupsComparisonsProteinLayout comProt = new DiseaseGroupsComparisonsProteinLayout(comparison.getDatasetIndexes().length, comparison, quant.getProtKey());
//                    comProt.setQuantPeptidesList(new HashSet<QuantPeptide>());
//                    comparProtList.put(protAcc, comProt);
//                }
//
//                DiseaseGroupsComparisonsProteinLayout comProt = comparProtList.get(protAcc);
//
//                boolean significantPValue = true;
//                if (quant.getStringPValue().equalsIgnoreCase("Not Significant") || quant.getStringPValue().equalsIgnoreCase("Not Available")) {
//                    significantPValue = false;
//
//                }
//                
//                
//                
//                
//                
//                 System.out.println(" "+pGrI+"   "+quant.getPatientGroupI()+"   ---  "+quant.getPatientSubGroupI()+"  " );
//
//                if ((pGrI.equalsIgnoreCase(quant.getPatientGroupI()) || pGrI.equalsIgnoreCase(quant.getPatientSubGroupI())) && (pGrII.equalsIgnoreCase(quant.getPatientGroupII()) || pGrII.equalsIgnoreCase(quant.getPatientSubGroupII()))) {
//                    if (quant.getStringFCValue().equalsIgnoreCase("Decreased") || quant.getStringFCValue().equalsIgnoreCase("Decrease")) {
//                        comProt.addDown((quant.getPatientsGroupINumber() + quant.getPatientsGroupIINumber()), quant.getDsKey(), significantPValue);
//                    } else if (quant.getStringFCValue().equalsIgnoreCase("Increased")||quant.getStringFCValue().equalsIgnoreCase("Increase")) {
//                        comProt.addUp((quant.getPatientsGroupINumber() + quant.getPatientsGroupIINumber()), quant.getDsKey(), significantPValue);
//                    } else if (quant.getStringFCValue().equalsIgnoreCase("Not Provided")) {
//                        comProt.addNotProvided((quant.getPatientsGroupINumber() + quant.getPatientsGroupIINumber()), quant.getDsKey());
//                    } else if (quant.getStringFCValue().equalsIgnoreCase("Not Regulated")) {
//                        comProt.addNotProvided((quant.getPatientsGroupINumber() + quant.getPatientsGroupIINumber()), quant.getDsKey());
////                        comProt.addNotReg((quant.getPatientsGroupINumber() + quant.getPatientsGroupIINumber()), quant.getDsKey());
//                    }
//
//                } else if((pGrII.equalsIgnoreCase(quant.getPatientGroupI()) || pGrII.equalsIgnoreCase(quant.getPatientSubGroupI())) && (pGrI.equalsIgnoreCase(quant.getPatientGroupII()) || pGrI.equalsIgnoreCase(quant.getPatientSubGroupII()))) {
//                    System.out.println("now invert");
//                    inverted = true;
//
//                    if (quant.getStringFCValue().equalsIgnoreCase("Decreased") || quant.getStringFCValue().equalsIgnoreCase("Decrease")) {
//                        comProt.addUp((quant.getPatientsGroupINumber() + quant.getPatientsGroupIINumber()), quant.getDsKey(), significantPValue);
//                    } else if (quant.getStringFCValue().equalsIgnoreCase("Increased")||quant.getStringFCValue().equalsIgnoreCase("Increase") ) {
//                        comProt.addDown((quant.getPatientsGroupINumber() + quant.getPatientsGroupIINumber()), quant.getDsKey(), significantPValue);
//                    } else if (quant.getStringFCValue().equalsIgnoreCase("Not Provided")) {
//                        comProt.addNotProvided((quant.getPatientsGroupINumber() + quant.getPatientsGroupIINumber()), quant.getDsKey());
//                    } else if (quant.getStringFCValue().equalsIgnoreCase("Not Regulated")) {
//                        comProt.addNotProvided((quant.getPatientsGroupINumber() + quant.getPatientsGroupIINumber()), quant.getDsKey());
////                        comProt.addNotReg((quant.getPatientsGroupINumber() + quant.getPatientsGroupIINumber()), quant.getDsKey());
//                    }
//
//                }
//                else
//                {
//                
//                    System.out.println("waw error");
//                }
//                String uniprotAcc = quant.getUniprotAccession();
////                comProt.setUniProtAcc(uniprotAcc);
//                String protName;
//                String accession;
//                String url;
//
//                if (uniprotAcc.trim().equalsIgnoreCase("") || uniprotAcc.equalsIgnoreCase("Not Available") || uniprotAcc.equalsIgnoreCase("Entry Deleted") || uniprotAcc.equalsIgnoreCase("Entry Demerged") || uniprotAcc.equalsIgnoreCase("NOT RETRIEVED") || uniprotAcc.equalsIgnoreCase("DELETED")) {
//                    protName = quant.getPublicationProteinName();
//                    accession = quant.getPublicationAccNumber();
//                    url = null;
//
//                } else {
//                    protName = quant.getUniprotProteinName();
//                    accession = quant.getUniprotAccession();
//                    url = "http://www.uniprot.org/uniprot/" + protAcc.toUpperCase();
//                }
//                if (protName == null || protName.trim().equalsIgnoreCase("")) {
//                    System.out.println("another error --- " + accession);
//                }
//
//                
//                comProt.setProtName(protName);
//                comProt.setProteinAccssionNumber(accession);
//                comProt.setUrl(url);
//             
//                comProt.setSequence(quant.getSequence());
//
//                Set<QuantPeptide> quantPeptidesList = comProt.getQuantPeptidesList();
//                for (String key : comparisonPeptideMap.keySet()) {
//
//                    if (key.equalsIgnoreCase("_" + (quant.getProtKey()) + "__" + quant.getDsKey() + "__")) {
//                        if (inverted) {
//                            Set<QuantPeptide> updatedQuantPeptidesList = new HashSet<QuantPeptide>();
//                            for (QuantPeptide quantPeptide : comparisonPeptideMap.get(key)) {
//                                if (quantPeptide.getString_fc_value().equalsIgnoreCase("Increased")|| quant.getStringFCValue().equalsIgnoreCase("Increase")) {
//
//                                    quantPeptide.setString_fc_value("Decreased");
//
//                                } else if (quantPeptide.getString_fc_value().equalsIgnoreCase("Decreased")||quant.getStringFCValue().equalsIgnoreCase("Decrease")) {
//                                    quantPeptide.setString_fc_value("Increased");
//
//                                }
//                                if (quantPeptide.getFc_value() != -1000000000.0) {
//                                    quantPeptide.setFc_value(1.0 / quantPeptide.getFc_value());
//                                }
//                                updatedQuantPeptidesList.add(quantPeptide);
//                            }
//                            quantPeptidesList.addAll(updatedQuantPeptidesList);
//
//                        }
//
//                        quantPeptidesList.addAll(comparisonPeptideMap.get(key));
//                    }
//                }
//
//                Map<String, QuantProtein> dsQuantProteinsMap = comProt.getDsQuantProteinsMap();
//                if (!dsQuantProteinsMap.containsKey("-" + quant.getDsKey() + "-" + comProt.getProteinAccssionNumber() + "-")) {
//                    if (inverted) {
//                        if (quant.getStringFCValue().equalsIgnoreCase("Increased")||quant.getStringFCValue().equalsIgnoreCase("Increase")) {
//
//                            quant.setStringFCValue("Decreased");
//
//                        } else if (quant.getStringFCValue().equalsIgnoreCase("Decreased")||quant.getStringFCValue().equalsIgnoreCase("Decrease")) {
//                            quant.setStringFCValue("Increased");
//
//                        }
//                        if (quant.getFcPatientGroupIonPatientGroupII() != -1000000000.0) {
//                            quant.setFcPatientGroupIonPatientGroupII(1.0 / quant.getFcPatientGroupIonPatientGroupII());
//                        }
//                        String pgI = quant.getPatientGroupII();
//                        String pSubGI = quant.getPatientSubGroupII();
//                        String pGrIComm = quant.getPatientGrIIComment();
//                        int pGrINum = quant.getPatientsGroupIINumber();
//
//                        quant.setPatientGroupII(quant.getPatientGroupI());
//                        quant.setPatientGrIIComment(quant.getPatientGrIComment());
//                        quant.setPatientSubGroupII(quant.getPatientSubGroupI());
//                        quant.setPatientsGroupIINumber(quant.getPatientsGroupINumber());
//
//                        quant.setPatientGroupI(pgI);
//                        quant.setPatientGrIComment(pSubGI);
//                        quant.setPatientSubGroupI(pGrIComm);
//                        quant.setPatientsGroupINumber(pGrINum);
//
//                    }
////                    if (searchQuantificationProtList != null) {
////                        dsQuantProteinsMap.put("-" + (quant.getDsKey() - 1) + "-" + comProt.getProteinAccssionNumber() + "-", quant);
////                    } else {
//                    dsQuantProteinsMap.put("-" + quant.getDsKey() + "-" + comProt.getProteinAccssionNumber() + "-", quant);
////                    }
//                } else {
////                    System.out.println("at major error in data dublicated keys " + ("-" + quant.getDsKey() + "-" + comProt.getProteinAccssionNumber() + "-"));
////                    dsQuantProteinsMap.remove("-" + quant.getDsKey() + "-" + comProt.getProteinAccssionNumber() + "-");
//                    continue;
//                }
//
//                comProt.setDsQuantProteinsMap(dsQuantProteinsMap);
//                comProt.setQuantPeptidesList(quantPeptidesList);
//
//                comparProtList.put(protAcc, comProt);
//
//            }
//
//            //init pep for prot
//            //sort the protiens map
//            Map<String, DiseaseGroupsComparisonsProteinLayout> sortedcomparProtList = new TreeMap<String, DiseaseGroupsComparisonsProteinLayout>(Collections.reverseOrder());
//            for (String Key : comparProtList.keySet()) {
//                DiseaseGroupsComparisonsProteinLayout temp = comparProtList.get(Key);
//                sortedcomparProtList.put((temp.getSignificantUp() + "_" + Key), temp);
//            }
//
//            comparison.setComparProtsMap(sortedcomparProtList);
//            updatedSelectedComparisonList.add(comparison);
//
//        }
//
//        return updatedSelectedComparisonList;
//
//    }
//    public Set<QuantDiseaseGroupsComparison> getComparisonProtList2(Set<QuantDiseaseGroupsComparison> selectedComparisonList, List<QuantProtein> searchQuantificationProtList) {
//
//        Set<QuantDiseaseGroupsComparison> updatedSelectedComparisonList = new LinkedHashSet<QuantDiseaseGroupsComparison>();
//        Set<QuantProtein> fullComparisonProtMap = new HashSet<QuantProtein>();
////        Map<QuantDiseaseGroupsComparison, Set<QuantProtein>> comparisonsToProtMap = new HashMap<QuantDiseaseGroupsComparison, Set<QuantProtein>>();
//        Map<Integer, QuantDiseaseGroupsComparison> dsIndexToComparisonsMap = new HashMap<Integer, QuantDiseaseGroupsComparison>();
//        Map<String, QuantDiseaseGroupsComparison> headerToComparisonsMap = new HashMap<String, QuantDiseaseGroupsComparison>();
//
//        Set<Integer> dsIdsList = new HashSet<Integer>();
//
//        if (searchQuantificationProtList != null) {
//            for (QuantDiseaseGroupsComparison comparison : selectedComparisonList) {
//                for (int dsID : comparison.getDatasetIndexes()) {
//                    for (QuantProtein qprot : searchQuantificationProtList) {
//                        if (qprot.getDsKey() == (dsID)) {
//                            fullComparisonProtMap.add(qprot);
//                            dsIdsList.add(dsID);
//                            dsIndexToComparisonsMap.put(dsID, comparison);
//                        }
//                    }
//                }
//            }
//        } else {
//            for (QuantDiseaseGroupsComparison comparison : selectedComparisonList) {
//
//                for (int dsID : comparison.getDatasetIndexes()) {
//                    dsIdsList.add(dsID);
//                    dsIndexToComparisonsMap.put(dsID, comparison);
//                }
//
//            }
//            fullComparisonProtMap.addAll(da.getQuantificationProteins(dsIdsList.toArray()));
//        }
//
//        Map<String, Set<QuantPeptide>> fullComparisonPeptideMap = (da.getQuantificationPeptides(dsIdsList.toArray()));
//        // distribute the prots and peptides on the comparisons
//        for (QuantProtein quantProtein : fullComparisonProtMap) {
//            QuantDiseaseGroupsComparison comparison = dsIndexToComparisonsMap.get(quantProtein.getDsKey());
//            if (headerToComparisonsMap.containsKey(comparison.getComparisonHeader())) {
//                comparison = headerToComparisonsMap.get(comparison.getComparisonHeader());
//            }
//            if (comparison.getComparProtsMap() == null) {
//                Map<String, DiseaseGroupsComparisonsProteinLayout> comparProtList = new HashMap<String, DiseaseGroupsComparisonsProteinLayout>();
//                comparison.setComparProtsMap(comparProtList);
//            }
//            String pGrI = comparison.getComparisonHeader().split("vs")[0].trim();
//            String pGrII = comparison.getComparisonHeader().split("vs")[1].trim();
//
//            boolean inverted = false;
//            String protAcc = quantProtein.getUniprotAccession();
//            if (protAcc.equalsIgnoreCase("") || protAcc.equalsIgnoreCase("Not Available") || protAcc.equalsIgnoreCase("Entry Deleted") || protAcc.equalsIgnoreCase("Entry Demerged") || protAcc.equalsIgnoreCase("NOT RETRIEVED") || protAcc.equalsIgnoreCase("DELETED")) {
//                protAcc = quantProtein.getPublicationAccNumber();
//
//            }
//            Map<String, DiseaseGroupsComparisonsProteinLayout> comparProtList = comparison.getComparProtsMap();
//            if (!comparProtList.containsKey(protAcc)) {
//                DiseaseGroupsComparisonsProteinLayout comProt = new DiseaseGroupsComparisonsProteinLayout(comparison.getDatasetIndexes().length, comparison, quantProtein.getProtKey());
//                comProt.setQuantPeptidesList(new HashSet<QuantPeptide>());
//                comparProtList.put(protAcc, comProt);
//            }
//
//            DiseaseGroupsComparisonsProteinLayout comProt = comparProtList.get(protAcc);
//
//            boolean significantPValue = true;
//            if (quantProtein.getStringPValue().equalsIgnoreCase("Not Significant") || quantProtein.getStringPValue().equalsIgnoreCase("Not Available")) {
//                significantPValue = false;
//
//            }
//
//            if ((pGrI.equalsIgnoreCase(quantProtein.getPatientGroupI()) || pGrI.equalsIgnoreCase(quantProtein.getPatientSubGroupI())) && (pGrII.equalsIgnoreCase(quantProtein.getPatientGroupII()) || pGrII.equalsIgnoreCase(quantProtein.getPatientSubGroupII()))) {
//                if (quantProtein.getStringFCValue().equalsIgnoreCase("Decreased")||quantProtein.getStringFCValue().equalsIgnoreCase("Decrease")) {
//                    comProt.addDown((quantProtein.getPatientsGroupINumber() + quantProtein.getPatientsGroupIINumber()), quantProtein.getDsKey(), significantPValue);
//                } else if (quantProtein.getStringFCValue().equalsIgnoreCase("Increased")||quantProtein.getStringFCValue().equalsIgnoreCase("Increase")) {
//                    comProt.addUp((quantProtein.getPatientsGroupINumber() + quantProtein.getPatientsGroupIINumber()), quantProtein.getDsKey(), significantPValue);
//                } else if (quantProtein.getStringFCValue().equalsIgnoreCase("Not Provided")) {
//                    comProt.addNotProvided((quantProtein.getPatientsGroupINumber() + quantProtein.getPatientsGroupIINumber()), quantProtein.getDsKey());
//                } else if (quantProtein.getStringFCValue().equalsIgnoreCase("Not Regulated")) {
//                    comProt.addNotProvided((quantProtein.getPatientsGroupINumber() + quantProtein.getPatientsGroupIINumber()), quantProtein.getDsKey());
//                }
//
//            } else {
//                inverted = true;
//
//                if (quantProtein.getStringFCValue().equalsIgnoreCase("Decreased")||quantProtein.getStringFCValue().equalsIgnoreCase("Decrease")) {
//                    comProt.addUp((quantProtein.getPatientsGroupINumber() + quantProtein.getPatientsGroupIINumber()), quantProtein.getDsKey(), significantPValue);
//                } else if (quantProtein.getStringFCValue().equalsIgnoreCase("Increased")||quantProtein.getStringFCValue().equalsIgnoreCase("Increase")) {
//                    comProt.addDown((quantProtein.getPatientsGroupINumber() + quantProtein.getPatientsGroupIINumber()), quantProtein.getDsKey(), significantPValue);
//                } else if (quantProtein.getStringFCValue().equalsIgnoreCase("Not Provided")) {
//                    comProt.addNotProvided((quantProtein.getPatientsGroupINumber() + quantProtein.getPatientsGroupIINumber()), quantProtein.getDsKey());
//                } else if (quantProtein.getStringFCValue().equalsIgnoreCase("Not Regulated")) {
//                    comProt.addNotProvided((quantProtein.getPatientsGroupINumber() + quantProtein.getPatientsGroupIINumber()), quantProtein.getDsKey());
//                }
//
//            }
//            String uniprotAcc = quantProtein.getUniprotAccession();
//            String protName;
//            String accession;
//            String url;
//
//            if (uniprotAcc.equalsIgnoreCase("") || uniprotAcc.equalsIgnoreCase("Not Available") || uniprotAcc.equalsIgnoreCase("Entry Deleted") || uniprotAcc.equalsIgnoreCase("Entry Demerged") || uniprotAcc.equalsIgnoreCase("NOT RETRIEVED") || uniprotAcc.equalsIgnoreCase("DELETED")) {
//                protName = quantProtein.getPublicationProteinName();
//                accession = quantProtein.getPublicationAccNumber();
//                url = null;
//
//            } else {
//                protName = quantProtein.getUniprotProteinName();
//                accession = quantProtein.getUniprotAccession();
//                url = "http://www.uniprot.org/uniprot/" + protAcc.toUpperCase();
//            }
//
//            comProt.setProtName(protName);
//            comProt.setProteinAccssionNumber(accession);
//            comProt.setUrl(url);
//
//            comProt.setSequence(quantProtein.getSequence());
//
//            Map<String, QuantProtein> dsQuantProteinsMap = comProt.getDsQuantProteinsMap();
//            if (!dsQuantProteinsMap.containsKey("-" + quantProtein.getDsKey() + "-" + comProt.getProteinAccssionNumber() + "-")) {
//                if (inverted) {
//                    if (quantProtein.getStringFCValue().equalsIgnoreCase("Increased")||quantProtein.getStringFCValue().equalsIgnoreCase("Increase")) {
//
//                        quantProtein.setStringFCValue("Decreased");
//
//                    } else if (quantProtein.getStringFCValue().equalsIgnoreCase("Decreased")||quantProtein.getStringFCValue().equalsIgnoreCase("Decrease")) {
//                        quantProtein.setStringFCValue("Increased");
//
//                    }
//                    if (quantProtein.getFcPatientGroupIonPatientGroupII() != -1000000000.0) {
//                        quantProtein.setFcPatientGroupIonPatientGroupII(1.0 / quantProtein.getFcPatientGroupIonPatientGroupII());
//                    }
//                    String pgI = quantProtein.getPatientGroupII();
//                    String pSubGI = quantProtein.getPatientSubGroupII();
//                    String pGrIComm = quantProtein.getPatientGrIIComment();
//                    int pGrINum = quantProtein.getPatientsGroupIINumber();
//
//                    quantProtein.setPatientGroupII(quantProtein.getPatientGroupI());
//                    quantProtein.setPatientGrIIComment(quantProtein.getPatientGrIComment());
//                    quantProtein.setPatientSubGroupII(quantProtein.getPatientSubGroupI());
//                    quantProtein.setPatientsGroupIINumber(quantProtein.getPatientsGroupINumber());
//
//                    quantProtein.setPatientGroupI(pgI);
//                    quantProtein.setPatientGrIComment(pSubGI);
//                    quantProtein.setPatientSubGroupI(pGrIComm);
//                    quantProtein.setPatientsGroupINumber(pGrINum);
//
//                }
//                dsQuantProteinsMap.put("-" + quantProtein.getDsKey() + "-" + comProt.getProteinAccssionNumber() + "-", quantProtein);
//            } else {
//                System.out.println("at major error in data dublicated keys " + ("-" + quantProtein.getDsKey() + "-" + comProt.getProteinAccssionNumber() + "-"));
//                continue;
//            }
//            comProt.setDsQuantProteinsMap(dsQuantProteinsMap);
//            Set<QuantPeptide> quantPeptidesList = comProt.getQuantPeptidesList();
//            Set<QuantPeptide> updatedQuantPeptidesList = new HashSet<QuantPeptide>();
//            for (String key : fullComparisonPeptideMap.keySet()) {
//                if (key.equalsIgnoreCase("_" + (quantProtein.getProtKey()) + "__" + quantProtein.getDsKey() + "__")) {
//                    if (inverted) {
//                        for (QuantPeptide quantPeptide : fullComparisonPeptideMap.get(key)) {
//                            if (quantPeptide.getString_fc_value().equalsIgnoreCase("Increased")||quantPeptide.getString_fc_value().equalsIgnoreCase("Increase")) {
//
//                                quantPeptide.setString_fc_value("Decreased");
//
//                            } else if (quantPeptide.getString_fc_value().equalsIgnoreCase("Decreased")||quantPeptide.getString_fc_value().equalsIgnoreCase("Decrease")) {
//                                quantPeptide.setString_fc_value("Increased");
//
//                            }
//                            if (quantPeptide.getFc_value() != -1000000000.0) {
//                                quantPeptide.setFc_value(1.0 / quantPeptide.getFc_value());
//                            }
//                            updatedQuantPeptidesList.add(quantPeptide);
//                        }
//                        quantPeptidesList.addAll(updatedQuantPeptidesList);
//                    }
//                    quantPeptidesList.addAll(fullComparisonPeptideMap.get(key));
//                }
//            }
//            comProt.setQuantPeptidesList(quantPeptidesList);
//            comparProtList.put(protAcc, comProt);
//            comparison.setComparProtsMap(comparProtList);
//            dsIndexToComparisonsMap.put(quantProtein.getDsKey(), comparison);
//            System.out.println("at add comparison " + headerToComparisonsMap.containsKey(comparison.getComparisonHeader()) + "  ---------   " + headerToComparisonsMap.put(comparison.getComparisonHeader(), comparison));
//        }
//        Map<String, DiseaseGroupsComparisonsProteinLayout> sortedcomparProtList = new TreeMap<String, DiseaseGroupsComparisonsProteinLayout>(Collections.reverseOrder());
//
//        for (QuantDiseaseGroupsComparison comparison : headerToComparisonsMap.values()) {
//            Map<String, DiseaseGroupsComparisonsProteinLayout> comparProtList = comparison.getComparProtsMap();
//            for (String Key : comparProtList.keySet()) {
//                DiseaseGroupsComparisonsProteinLayout temp = comparProtList.get(Key);
//                sortedcomparProtList.put((temp.getSignificantUp() + "_" + Key), temp);
//            }
//            comparison.setComparProtsMap(sortedcomparProtList);
//            updatedSelectedComparisonList.add(comparison);
//
//        }
//        System.out.println("at indexis are: " + updatedSelectedComparisonList.size());
//        return updatedSelectedComparisonList;
//
//    }
    /**
     * get quant proteins layout for comparison table
     *
     * @param selectedComparisonList selected groups comparison list
     * @param searchQuantificationProtList searching results (null allowed here)
     *
     * @return updated Selected Comparison List
     */
    public Set<QuantDiseaseGroupsComparison> getComparisonProtList(Set<QuantDiseaseGroupsComparison> selectedComparisonList, List<QuantProtein> searchQuantificationProtList) {

        Set<QuantDiseaseGroupsComparison> updatedSelectedComparisonList = new LinkedHashSet<QuantDiseaseGroupsComparison>();
        Set<QuantProtein> fullComparisonProtMap = new HashSet<QuantProtein>();
//        Map<QuantDiseaseGroupsComparison, Set<QuantProtein>> comparisonsToProtMap = new HashMap<QuantDiseaseGroupsComparison, Set<QuantProtein>>();
        Map<Integer, QuantDiseaseGroupsComparison> dsIndexToComparisonsMap = new HashMap<Integer, QuantDiseaseGroupsComparison>();

        Set<Integer> dsIdsList = new HashSet<Integer>();

        if (searchQuantificationProtList != null) {
            for (QuantDiseaseGroupsComparison comparison : selectedComparisonList) {
                for (int dsID : comparison.getDatasetIndexes()) {
                    for (QuantProtein qprot : searchQuantificationProtList) {
                        if (qprot.getDsKey() == (dsID)) {
                            fullComparisonProtMap.add(qprot);
                            dsIdsList.add(dsID);
                            dsIndexToComparisonsMap.put(dsID, comparison);
                        }
                    }
                }
            }
        } else {
            for (QuantDiseaseGroupsComparison comparison : selectedComparisonList) {

                for (int dsID : comparison.getDatasetIndexes()) {
                    dsIdsList.add(dsID);
                    dsIndexToComparisonsMap.put(dsID, comparison);
                }

            }
            fullComparisonProtMap.addAll(da.getQuantificationProteins(dsIdsList.toArray()));
        }

        Map<String, Set<QuantPeptide>> fullComparisonPeptideMap = (da.getQuantificationPeptides(dsIdsList.toArray()));

        Map<String, Set<QuantProtein>> fullComparisonToProtMap = new HashMap<String, Set<QuantProtein>>();
        for (QuantProtein qprot : fullComparisonProtMap) {
            String compKey = dsIndexToComparisonsMap.get(qprot.getDsKey()).getComparisonHeader();
            if (!fullComparisonToProtMap.containsKey(compKey)) {
                Set<QuantProtein> comparisonProtMap = new HashSet<QuantProtein>();
                fullComparisonToProtMap.put(compKey, comparisonProtMap);

            }
            Set<QuantProtein> comparisonProtMap = fullComparisonToProtMap.get(compKey);
            comparisonProtMap.add(qprot);
            fullComparisonToProtMap.put(compKey, comparisonProtMap);

        }
//         for (String qpeptideAccession : fullComparisonPeptideMap.keySet()) {
////            String compKey = dsIndexToComparisonsMap.get(qprot.getDsKey()).getComparisonHeader();
////            if (!fullComparisonToProtMap.containsKey(compKey)) {
////                Set<QuantProtein> comparisonProtMap = new HashSet<QuantProtein>();
////                fullComparisonToProtMap.put(compKey, comparisonProtMap);
////
////            }
////            Set<QuantProtein> comparisonProtMap = fullComparisonToProtMap.get(compKey);
////            comparisonProtMap.add(qprot);
////            fullComparisonToProtMap.put(compKey, comparisonProtMap);
//
//        }

        for (QuantDiseaseGroupsComparison comparison : selectedComparisonList) {
            Set<QuantProtein> comparisonProtMap = fullComparisonToProtMap.get(comparison.getComparisonHeader());

//            Object[] iArr = new Object[comparison.getDatasetIndexes().length];
//            for (int x = 0; x < iArr.length; x++) {
//                iArr[x] = comparison.getDatasetIndexes()[x];
//            }
//            Map<String, Set<QuantPeptide>> comparisonPeptideMap = (da.getQuantificationPeptides(iArr));
            Map<String, DiseaseGroupsComparisonsProteinLayout> comparProtList = new HashMap<String, DiseaseGroupsComparisonsProteinLayout>();
            String pGrI = comparison.getComparisonHeader().split("vs")[0].trim();
            String pGrII = comparison.getComparisonHeader().split("vs")[1].trim();

            for (QuantProtein quant : comparisonProtMap) {
                boolean inverted = false;
                String protAcc = quant.getUniprotAccession();
                if (protAcc.equalsIgnoreCase("") || protAcc.equalsIgnoreCase("Not Available") || protAcc.equalsIgnoreCase("Entry Deleted") || protAcc.equalsIgnoreCase("Entry Demerged") || protAcc.equalsIgnoreCase("NOT RETRIEVED") || protAcc.equalsIgnoreCase("DELETED")) {
                    protAcc = quant.getPublicationAccNumber();

                }
                if (!comparProtList.containsKey(protAcc)) {
                    DiseaseGroupsComparisonsProteinLayout comProt = new DiseaseGroupsComparisonsProteinLayout(comparison.getDatasetIndexes().length, comparison, quant.getProtKey());
                    comProt.setQuantPeptidesList(new HashSet<QuantPeptide>());
                    comparProtList.put(protAcc, comProt);
                }

                DiseaseGroupsComparisonsProteinLayout comProt = comparProtList.get(protAcc);

                boolean significantPValue = true;
                if (quant.getStringPValue().equalsIgnoreCase("Not Significant") || quant.getStringPValue().equalsIgnoreCase("Not Available")) {
                    significantPValue = false;

                }
                if ((pGrI.equalsIgnoreCase(quant.getPatientGroupI()) || pGrI.equalsIgnoreCase(quant.getPatientSubGroupI())) && (pGrII.equalsIgnoreCase(quant.getPatientGroupII()) || pGrII.equalsIgnoreCase(quant.getPatientSubGroupII()))) {
                    if (quant.getStringFCValue().equalsIgnoreCase("Decreased") || quant.getStringFCValue().equalsIgnoreCase("Decrease")) {
                        comProt.addDown((quant.getPatientsGroupINumber() + quant.getPatientsGroupIINumber()), quant.getDsKey(), significantPValue);
                    } else if (quant.getStringFCValue().equalsIgnoreCase("Increased") || quant.getStringFCValue().equalsIgnoreCase("Increase")) {
                        comProt.addUp((quant.getPatientsGroupINumber() + quant.getPatientsGroupIINumber()), quant.getDsKey(), significantPValue);
                    } else if (quant.getStringFCValue().equalsIgnoreCase("Not Provided")) {
                        comProt.addNotProvided((quant.getPatientsGroupINumber() + quant.getPatientsGroupIINumber()), quant.getDsKey());
                    } else if (quant.getStringFCValue().equalsIgnoreCase("Not Regulated")) {
                        comProt.addNotProvided((quant.getPatientsGroupINumber() + quant.getPatientsGroupIINumber()), quant.getDsKey());
//                        comProt.addNotReg((quant.getPatientsGroupINumber() + quant.getPatientsGroupIINumber()), quant.getDsKey());
                    }

                } else {
                    inverted = true;

                    if (quant.getStringFCValue().equalsIgnoreCase("Decreased") || quant.getStringFCValue().equalsIgnoreCase("Decrease")) {
                        comProt.addUp((quant.getPatientsGroupINumber() + quant.getPatientsGroupIINumber()), quant.getDsKey(), significantPValue);
                    } else if (quant.getStringFCValue().equalsIgnoreCase("Increased") || quant.getStringFCValue().equalsIgnoreCase("Increase")) {
                        comProt.addDown((quant.getPatientsGroupINumber() + quant.getPatientsGroupIINumber()), quant.getDsKey(), significantPValue);
                    } else if (quant.getStringFCValue().equalsIgnoreCase("Not Provided")) {
                        comProt.addNotProvided((quant.getPatientsGroupINumber() + quant.getPatientsGroupIINumber()), quant.getDsKey());
                    } else if (quant.getStringFCValue().equalsIgnoreCase("Not Regulated")) {
                        comProt.addNotProvided((quant.getPatientsGroupINumber() + quant.getPatientsGroupIINumber()), quant.getDsKey());
//                        comProt.addNotReg((quant.getPatientsGroupINumber() + quant.getPatientsGroupIINumber()), quant.getDsKey());
                    }

                }
                String uniprotAcc = quant.getUniprotAccession();
                String protName;
                String accession;
                String url;

                if (uniprotAcc.trim().equalsIgnoreCase("") || uniprotAcc.equalsIgnoreCase("Not Available") || uniprotAcc.equalsIgnoreCase("Entry Deleted") || uniprotAcc.equalsIgnoreCase("Entry Demerged") || uniprotAcc.equalsIgnoreCase("NOT RETRIEVED") || uniprotAcc.equalsIgnoreCase("DELETED")) {
                    protName = quant.getPublicationProteinName();
                    accession = quant.getPublicationAccNumber();
                    url = null;

                } else {
                    protName = quant.getUniprotProteinName();
                    accession = quant.getUniprotAccession();
                    url = "http://www.uniprot.org/uniprot/" + protAcc.toUpperCase();
                }
                if (protName.trim().equalsIgnoreCase("")) {
                    protName = quant.getPublicationProteinName();
                }

                comProt.setProtName(protName);
                comProt.setProteinAccssionNumber(accession);
                comProt.setUrl(url);

                comProt.setSequence(quant.getSequence());

                Set<QuantPeptide> quantPeptidesList = comProt.getQuantPeptidesList();
                for (String key : fullComparisonPeptideMap.keySet()) {

                    if (key.equalsIgnoreCase("_" + (quant.getProtKey()) + "__" + quant.getDsKey() + "__")) {

                        if (inverted) {
                            Set<QuantPeptide> updatedQuantPeptidesList = new HashSet<QuantPeptide>();
                            for (QuantPeptide quantPeptide : fullComparisonPeptideMap.get(key)) {

                                if (quantPeptide.getString_fc_value().equalsIgnoreCase("Increased") || quantPeptide.getString_fc_value().equalsIgnoreCase("Increase")) {

                                    quantPeptide.setString_fc_value("Decreased");

                                } else if (quantPeptide.getString_fc_value().equalsIgnoreCase("Decreased") || quantPeptide.getString_fc_value().equalsIgnoreCase("Decrease")) {
                                    quantPeptide.setString_fc_value("Increased");

                                }
                                if (quantPeptide.getFc_value() != -1000000000.0) {
                                    quantPeptide.setFc_value((1.0 / quantPeptide.getFc_value()) * -1);
                                }

                                updatedQuantPeptidesList.add(quantPeptide);
                            }
                            quantPeptidesList.addAll(updatedQuantPeptidesList);

                        } else {

                            quantPeptidesList.addAll(fullComparisonPeptideMap.get(key));

                        }

                    }
                }

                Map<String, QuantProtein> dsQuantProteinsMap = comProt.getDsQuantProteinsMap();
                if (!dsQuantProteinsMap.containsKey("-" + quant.getDsKey() + "-" + comProt.getProteinAccssionNumber() + "-")) {
                    if (inverted) {
                        if (quant.getStringFCValue().equalsIgnoreCase("Increased") || quant.getStringFCValue().equalsIgnoreCase("Increase")) {

                            quant.setStringFCValue("Decreased");

                        } else if (quant.getStringFCValue().equalsIgnoreCase("Decreased") || quant.getStringFCValue().equalsIgnoreCase("Decrease")) {
                            quant.setStringFCValue("Increased");

                        }
                        if (quant.getFcPatientGroupIonPatientGroupII() != -1000000000.0) {
                            quant.setFcPatientGroupIonPatientGroupII(1.0 / quant.getFcPatientGroupIonPatientGroupII() * -1);
                        }
                        String pgI = quant.getPatientGroupII();
                        String pSubGI = quant.getPatientSubGroupII();
                        String pGrIComm = quant.getPatientGrIIComment();
                        int pGrINum = quant.getPatientsGroupIINumber();

                        quant.setPatientGroupII(quant.getPatientGroupI());
                        quant.setPatientGrIIComment(quant.getPatientGrIComment());
                        quant.setPatientSubGroupII(quant.getPatientSubGroupI());
                        quant.setPatientsGroupIINumber(quant.getPatientsGroupINumber());

                        quant.setPatientGroupI(pgI);
                        quant.setPatientGrIComment(pSubGI);
                        quant.setPatientSubGroupI(pGrIComm);
                        quant.setPatientsGroupINumber(pGrINum);

                    }
//                    if (searchQuantificationProtList != null) {
//                        dsQuantProteinsMap.put("-" + (quant.getDsKey() - 1) + "-" + comProt.getProteinAccssionNumber() + "-", quant);
//                    } else {
                    dsQuantProteinsMap.put("-" + quant.getDsKey() + "-" + comProt.getProteinAccssionNumber() + "-", quant);
//                    }
                } else {
                    System.out.println("at major error in data dublicated keys " + ("-" + quant.getDsKey() + "-" + comProt.getProteinAccssionNumber() + "-"));
//                    dsQuantProteinsMap.remove("-" + quant.getDsKey() + "-" + comProt.getProteinAccssionNumber() + "-");
                    continue;
                }

                comProt.setDsQuantProteinsMap(dsQuantProteinsMap);
                comProt.setQuantPeptidesList(quantPeptidesList);

                comparProtList.put(protAcc, comProt);

            }

            //init pep for prot
            //sort the protiens map
            Map<String, DiseaseGroupsComparisonsProteinLayout> sortedcomparProtList = new TreeMap<String, DiseaseGroupsComparisonsProteinLayout>(Collections.reverseOrder());
            for (String Key : comparProtList.keySet()) {
                DiseaseGroupsComparisonsProteinLayout temp = comparProtList.get(Key);
                sortedcomparProtList.put((temp.getSignificantUp() + "_" + Key), temp);
            }

            comparison.setComparProtsMap(sortedcomparProtList);
            updatedSelectedComparisonList.add(comparison);

        }
        return updatedSelectedComparisonList;

    }

    /**
     * k-means clustering for protein
     *
     * @param samples the samples
     * @param sampleIds the sample identifiers
     * @param numClusters the number of clusters
     * @param proteinKey the protein name
     * @return list of samplesId
     */
    public ArrayList<String> runKMeanClustering(double[][] samples, String[] sampleIds, int numClusters, String proteinKey) {
        KMeansClustering kMeansClutering = new KMeansClustering(samples, sampleIds, numClusters);
        // exectute the clustering
        kMeansClutering.kMeanCluster();
        int z = kMeansClutering.getClusters(proteinKey);
        ArrayList<String> clusterinList = kMeansClutering.getClusterMembers(z);
        if (clusterinList.contains(proteinKey)) {
            int index = clusterinList.indexOf(proteinKey);
            String tempProt = clusterinList.get(0);
            clusterinList.set(0, proteinKey);
            clusterinList.set(index, tempProt);

        }

        return clusterinList;

    }

    public OverviewInfoBean getResourceOverviewInformation() {
        return this.da.getResourceOverviewInformation();

    }

}
