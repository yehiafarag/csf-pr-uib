package probe.com.dal;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import probe.com.model.beans.IdentificationDataset;
import probe.com.model.beans.FractionBean;
import probe.com.model.beans.PeptideBean;
import probe.com.model.beans.IdentificationProteinBean;
import probe.com.model.beans.QuantProtein;
import probe.com.model.beans.StandardProteinBean;
import probe.com.model.beans.QuantDatasetListObject;
import probe.com.model.beans.QuantPeptide;

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
    public Map<Integer, IdentificationDataset> getDatasets()//get dataset list
    {
        Map<Integer, IdentificationDataset> datasestList = db.getDatasets();
        return datasestList;
    }

    /**
     * get selected dataset
     *
     * @param datasetId
     * @return dataset
     */
    public IdentificationDataset getDataset(int datasetId) {
        IdentificationDataset dataset = db.getStoredDataset(datasetId);
        return dataset;
    }

   

//    public boolean updatePeptideFile(IdentificationDataset dataset) {
//
//        boolean test = db.setPeptideFile(dataset.getPeptideList());
//        return test;
//    }

    /**
     * get proteins map for especial dataset
     *
     * @param datasetId
     * @return proteinsList
     */
    public Map<String, IdentificationProteinBean> getProteinsList(int datasetId) {
        Map<String, IdentificationProteinBean> proteinsList = db.getDatasetProteinsList(datasetId);
        return proteinsList;
    }

    /**
     * get dataset peptides list
     *
     * @param datasetId
     * @return dataset peptide List
     */
    public Map<Integer, PeptideBean> getPeptidesList(int datasetId,boolean valid) {
        Map<Integer, PeptideBean> peptidesList = db.getDatasetPeptidesList(datasetId,valid);   
        
        return peptidesList;
    }
    
    public int getAllDatasetPeptidesNumber(int datasetId, boolean validated) {

   return db.getAllDatasetPeptidesNumber(datasetId, validated);
    }
    
   
    /**
     * get fraction list for selected dataset
     *
     * @param datasetId
     * @return fractions  list for the selected dataset
     */

    public Map<Integer, IdentificationProteinBean> getProtGelFractionsList(int datasetId,String accession,String otherAccession) {
        Map<Integer, IdentificationProteinBean> fractionsProtList = db.getProtGelFractionsList(datasetId,accession, otherAccession);
        return fractionsProtList;

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
    public Map<Integer, IdentificationProteinBean>  searchProteinByAccession(String accession, int datasetId,boolean validatedOnly) {

//        String[] queryWordsArr = accession.split("\n");
//        StringBuilder sb = new StringBuilder();
//        for (int x = 0; x < queryWordsArr.length; x++) {
//            if (x > 0) {
//                sb.append(" AND ");
//            }
//            sb.append("prot_key` LIKE(?) ");
//
//        }
        Map<Integer, IdentificationProteinBean> datasetProteinsSearchingList  = db.searchProteinByAccession(accession, datasetId,validatedOnly);
        return datasetProteinsSearchingList ;
    }
    
     /**
     * search for proteins by accession keywords
     *
     * @param accession array of query words
     * @param validatedOnly only validated proteins results
     * @return dataset Proteins Searching List
     */
    public Map<Integer, IdentificationProteinBean>  searchIdentificationProteinAllDatasetsByAccession(String accession,boolean validatedOnly) {

         String[] queryWordsArr = accession.split("\n");
        Set<String> searchSet = new HashSet<String>();
        for (String str : queryWordsArr) {
            if (str.trim().length() > 3) {
                searchSet.add(str.trim());
            }
        }
        Map<Integer, IdentificationProteinBean> datasetProteinsSearchingList  = db.searchIdentificationProteinAllDatasetsByAccession(searchSet,validatedOnly);
  
        return datasetProteinsSearchingList ;
    }
     /**
     * search for proteins by accession keywords
     *
     * @param query   query words
     * @return dataset Proteins Searching List
     */
    public  List<QuantProtein>   searchQuantificationProteins(Query query) {

        List<QuantProtein> datasetProteinsSearchingList = db.searchQuantificationProteins(query);

        return datasetProteinsSearchingList;
    }
    
     public QuantDatasetListObject getQuantDatasetListObject(){
        return db.getQuantDatasetListObject();
   
    
    }
      public QuantDatasetListObject getQuantDatasetListObject(List<QuantProtein> searchQuantificationProtList){
        return db.getQuantDatasetListObject(searchQuantificationProtList);
   
    
    }
     public boolean[] getActiveFilters(){
     return db.getActiveFilters();
     
     }
      public boolean[] getActiveFilters(List<QuantProtein> searchQuantificationProtList){
     return db.getActiveFilters(searchQuantificationProtList);
     
     }
    

     /**
     * get peptides list form giving ids
     *
     * @param peptideIds peptides IDs
     * @return peptides list 
     */
    public Map<Integer, PeptideBean> getPeptidesList(String accession,String otherAcc,int datasetId) {
        Map<Integer, PeptideBean> peptidesProtList = db.getPeptidesList(accession, otherAcc, datasetId);
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
    public Map<Integer, IdentificationProteinBean> searchProteinByName(String protSearchKeyword, int datasetId,boolean validatedOnly) {
        Map<Integer, IdentificationProteinBean> proteinsList = db.searchProteinByName(protSearchKeyword, datasetId,validatedOnly);
        return proteinsList;
    }
    
     /**
     * search for proteins by protein description keywords
     *
     * @param protSearchKeyword  array of query words
     * @param datasetId dataset Id
     * @param validatedOnly only validated proteins results
     * @return datasetProteinsSearchList
     */
    public Map<Integer, IdentificationProteinBean> searchProteinAllDatasetsByName(String protSearchKeyword,boolean validatedOnly) {
        Map<Integer, IdentificationProteinBean> proteinsList = db.searchProteinAllDatasetsByName(protSearchKeyword,validatedOnly);
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
//    public Map<Integer,IdentificationProteinBean> searchProteinByPeptideSequence(String peptideSequenceKeyword, int datasetId,boolean validatedOnly) {
//
//        Map<Integer,IdentificationProteinBean> proteinsList = db.searchProteinByPeptideSequence(peptideSequenceKeyword, datasetId, validatedOnly);
//        return proteinsList;
//    }
    /**
     * search for proteins by peptide sequence keywords
     *
     * @param peptideSequenceKeyword  array of query words
     * @param validatedOnly only validated proteins results
     * @return datasetProteinsSearchList
     */
    public Map<Integer,IdentificationProteinBean> SearchProteinAllDatasetsByPeptideSequence(String peptideSequenceKeyword,boolean validatedOnly) {

        Map<Integer,IdentificationProteinBean> proteinsList = db.SearchProteinAllDatasetsByPeptideSequence(peptideSequenceKeyword, validatedOnly);
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
    public Map<Integer,IdentificationProteinBean> SearchProteinByPeptideSequence(String peptideSequenceKeyword,int datasetId,boolean validatedOnly) {

        Map<Integer,IdentificationProteinBean> proteinsList = db.SearchProteinByPeptideSequence(peptideSequenceKeyword,datasetId, validatedOnly);
        return proteinsList;
    }

    /**
     * update fraction range data in the database
     *
     * @param dataset
     *
     * @return test boolean successful process
     */
    public boolean updateFractionRange(IdentificationDataset dataset) {
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
    public boolean setStandardPlotProt(IdentificationDataset dataset) {
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
    public boolean updateStandardPlotProt(IdentificationDataset dataset) {
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
     public boolean updateDatasetData(IdentificationDataset dataset)
     {
           boolean test = db.updateDatasetData(dataset);
            return test;
     
     }
     public void runOnceToUpdateDatabase(){
//         db.singleuseUpdateDb();
     
     }
       public  boolean storeQuantProt(List<QuantProtein> qProtList){
         return db.storeQuantProt(qProtList);
     
     
     }
       public  Set<QuantProtein>   getQuantificationProteins(int dsUnique) {
//update peptides table
//        for (int x = 1; x < 38; x++) {
//            Set<QuantProtein>  datasetProteinsSearchingList = db.getQuantificationProteins(x);   
//            db.updateQuantificationPeptides(datasetProteinsSearchingList);
//        }
//       
//        
      Set<QuantProtein>  datasetProteinsSearchingList = db.getQuantificationProteins(dsUnique);   
        return datasetProteinsSearchingList;
    }
         public  Set<QuantProtein>   getQuantificationProteins(int dsUnique,List<QuantProtein> searchQuantificationProtList) {
      Set<QuantProtein>  datasetProteinsSearchingList = db.getQuantificationProteins(dsUnique,searchQuantificationProtList);   
        return datasetProteinsSearchingList;
    }
       
       

        public   Map<String,Set<QuantPeptide>>  getQuantificationPeptides(int dsUnique) {

       Map<String,Set<QuantPeptide>>  datasetProteinsSearchingList = db.getQuantificationPeptides(dsUnique);

        return datasetProteinsSearchingList;
    }
}
