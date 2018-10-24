package probe.com.dal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


import probe.com.model.beans.DatasetBean;
import probe.com.model.beans.FractionBean;
import probe.com.model.beans.PeptideBean;
import probe.com.model.beans.ProteinBean;
import probe.com.model.beans.StandardProteinBean;

public class DataAccess implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -7011020617952045934L;
    private final DataBase db;

     /**
     * @param url database url
     * @param dbName database name
     * @param driver database driver
     * @param userName database username
     * @param password database password
     *
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
    public boolean createTable() {

        boolean test = db.createTables();
        return test;


    }


    /**
     * remove dataset from the database
     *
     * @param datasetId
     * @return boolean successful process
     */
    public boolean removeDataset(int datasetId) {
        boolean test = db.removeDataset(datasetId);
        return test;
    }

    /**
     * get the available datasets
     *
     * @return datasetsList 
     */
    public Map<Integer, DatasetBean> getDatasets()//get dataset list
    {
        Map<Integer, DatasetBean> datasestList = db.getDatasets();
        return datasestList;
    }

    /**
     * get selected dataset
     *
     * @param datasetId
     * @return dataset
     */
    public DatasetBean getDataset(int datasetId) {
        DatasetBean dataset = db.getStoredDataset(datasetId);
        return dataset;
    }

   

    public boolean updatePeptideFile(DatasetBean dataset) {

        boolean test = db.setPeptideFile(dataset.getPeptideList());
        return test;
    }

    /**
     * get proteins map for especial dataset
     *
     * @param datasetId
     * @return proteinsList
     */
    public Map<String, ProteinBean> getProteinsList(int datasetId) {
        Map<String, ProteinBean> proteinsList = db.getDatasetProteinsList(datasetId);
        return proteinsList;
    }

    /**
     * get dataset peptides list
     *
     * @param datasetId
     * @return dataset peptide List
     */
    public Map<Integer, PeptideBean> getPeptidesList(int datasetId) {
        Map<Integer, PeptideBean> peptidesList = db.getDatasetPeptidesList(datasetId);   
        
        return peptidesList;
    }
    
   
    /**
     * get fraction list for selected dataset
     *
     * @param datasetId
     * @return fractions  list for the selected dataset
     */

    public Map<Integer, FractionBean> getFractionsList(int datasetId) {
        Map<Integer, FractionBean> fractionsList = db.getFractionsList(datasetId);
        return fractionsList;

    }

    ///new v-2
     /**
     * search for proteins by accession keywords
     *
     * @param accession array of query words
     * @param datasetId  
     * @param validatedOnly only validated proteins results
     * @return dataset Proteins Searching List
     */
    public Map<Integer, Map<Integer, ProteinBean>>   searchProteinByAccession(Set<String> accession,boolean validatedOnly) {
//          if(accession.size()>300){
//              int index = 300;
//              ArrayList ts = new ArrayList(accession);
//              ts.subList(index, index)
//              ts.subSet(accession., ts)
//              HashSet<String> tempHashSet = new HashSet<String>(accession.stream().);
//        
//        
//        
//        }

        Map<Integer, Map<Integer, ProteinBean>>  datasetProteinsSearchingList  = db.searchIdentificationProteinAllDatasetsByAccession(accession,validatedOnly);
        return datasetProteinsSearchingList ;
    }
    
    

     /**
     * get peptides list form giving ids
     *
     * @param peptideIds peptides IDs
     * @return peptides list 
     */
    public Map<Integer, PeptideBean> getPeptidesList(Set<Integer> peptideIds) {

        Map<Integer, PeptideBean> peptidesProtList = db.getPeptidesList(peptideIds);
        return peptidesProtList;
    }

    /**
     * get proteins fractions average list
     *
     * @param accession  
     * @param datasetId
     * @return dataset peptide List
     */ 
    public Map<Integer, FractionBean> getProteinFractionList(String accession, int datasetId) {
        Map<Integer, FractionBean> protionFractList = db.getProteinFractionList(accession, datasetId);
        return protionFractList;
    }

    /**
     * search for proteins by protein description keywords
     *
     * @param protSearchKeyword  array of query words
     * @param datasetId dataset Id
     * @param validatedOnly only validated proteins results
     * @return datasetProteinsSearchList
     */
    public Map<Integer, ProteinBean> searchProteinByName(String protSearchKeyword, int datasetId,boolean validatedOnly) {
        Map<Integer, ProteinBean> proteinsList = db.searchProteinByName(protSearchKeyword, datasetId,validatedOnly);
        return proteinsList;
    }

     /**
     * search for proteins by peptide sequence keywords
     *
     * @param peptideSequenceKeyword  array of query words
     * @param datasetId dataset Id
     * @param validatedOnly only validated proteins results
     * @return datasetProteinsSearchList
     */
    public Map<Integer,ProteinBean> searchProteinByPeptideSequence(String peptideSequenceKeyword, int datasetId,boolean validatedOnly) {

        Map<Integer,ProteinBean> proteinsList = db.searchProteinByPeptideSequence(peptideSequenceKeyword, datasetId, validatedOnly);
        return proteinsList;
    }

    /**
     * update fraction range data in the database
     *
     * @param dataset
     *
     * @return test boolean successful process
     */
    public boolean updateFractionRange(DatasetBean dataset) {
        boolean test = db.updateFractionRange(dataset);
        return test;
    }

     /**
     * get peptides data for a database using peptides ids
     *
     * @param peptideIds
     * @return list of peptides
     */
    public Map<Integer, PeptideBean> getPeptidesList(List<Integer> peptideIds) {
        Map<Integer, PeptideBean> peptidesProtList = db.getPeptidesList(peptideIds);
        return peptidesProtList;
    }

    /**
     * get peptides id list for selected protein in selected dataset
     *
     * @param datasetId
     * @param accession
     * @return peptides id list for the selected protein group in the selected
     * dataset
     */
    public Set<Integer> getDatasetProteinsPeptidesIds(int datasetId, String accession) {
        Set<Integer> datasetProPepIds = db.getDatasetPepProIds(datasetId, accession);

        return datasetProPepIds;
    }

    /**
     * store standard plot data in the database
     *
     * 
     * @param dataset dataset bean (in case of update existing dataset)
     * @return test boolean
     */
    public boolean setStandardPlotProt(DatasetBean dataset) {
        boolean test= false;
          List<StandardProteinBean> standardPlotList = db.getStandardProtPlotList(dataset.getDatasetId());
          if(!standardPlotList.isEmpty())
          {
              test = db.removeStandarPlot(dataset.getDatasetId());              
          }
        return test;
    }

     /**
     * read and store standard plot files in the database
     *
     * 
     * @param dataset dataset bean (in case of update existing dataset)
     * @return test boolean
     */
    public boolean updateStandardPlotProt(DatasetBean dataset) {
        boolean test = db.updateStandardPlotProt(dataset);
        return test;
    }

     /**
     * retrieve standard proteins data for fraction plot
     * @param datasetId
     * @return standardPlotList
     */
    public List<StandardProteinBean> getStandardProtPlotList(int datasetId) {

        List<StandardProteinBean> standardPlotList = db.getStandardProtPlotList(datasetId);
        return standardPlotList;
    }
    
    /**
     * retrieve standard  proteins data for fraction plot
     * @param dataset 
     * @return test boolean
     */
     public boolean updateDatasetData(DatasetBean dataset)
     {
           boolean test = db.updateDatasetData(dataset);
            return test;
     
     }
}
