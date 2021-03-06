/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csf.DAL;

import com.pepshaker.util.beans.ExperimentBean;
import com.pepshaker.util.beans.ProteinBean;
import com.quantcsf.beans.QuantDatasetObject;
import com.quantcsf.beans.QuantProtein;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Yehia Farag
 */
public class DAL {

    private final DB database;

    public DAL(String url, String dbName, String driver, String userName, String password) throws SQLException {
        database = new DB(url, dbName, driver, userName, password);
        database.createTables();
    }

    public int storeExperiment(ExperimentBean exp) {
        int expId = database.setupExperiment(exp);
        return expId;
    }

    public boolean storeProteinsList(ExperimentBean exp) {
        for (String key : exp.getProteinList().keySet()) {
            ProteinBean pb = exp.getProteinList().get(key);
            database.insertProteinExper(exp.getExpId(), pb, pb.getAccession() + "," + pb.getOtherProteins());
        }

        return true;
    }

    public boolean storePeptidesList(ExperimentBean exp) {
        boolean test = database.updatePeptideFile(exp);
        return test;
    }

    public boolean storeFractionsList(ExperimentBean exp) {
        boolean test = database.insertFractions(exp);
        return test;
    }

    public boolean checkName(String name) throws SQLException {
        boolean test = database.checkName(name);
        return test;

    }

    public boolean exportDataBase(String mysqldumpUrl, String sqlFileUrl) {
        return database.exportDataBase(mysqldumpUrl, sqlFileUrl);
//        database.getIdentificationProteinsList();
//        database.updateIdentificationProteinsList(sqlFileUrl);

    }

    public boolean restoreDB(String sqlFileUrl, String mysqldumpUrl) {
        return database.restoreDB(sqlFileUrl, mysqldumpUrl);
    }

    ///quant data store 
    public boolean storeCombinedQuantProtTable(List<QuantProtein> qProtList) {
        return database.storeCombinedQuantProtTable(qProtList);

    }

    //quant data store 
    public boolean updateProtSequances(Map<String, String> protSeqMap) {
        return database.updateProtSequances(protSeqMap);
    }

    public void storeQuantDatasets() {
        database.storeQuantDatasets();
    }

    public Object[] storeQuantitiveProteins(List<QuantProtein> qProtList) {
        return database.storeQuantitiveProteins(qProtList);
    }

    public void storeQuantitivePeptides(List<QuantProtein> qPeptidestList) {
        database.storeQuantitivePeptides(qPeptidestList);
    }

    public int getCurrentProtIndex() {
        return database.getCurrentProtIndex();
    }

    public Set<QuantDatasetObject> getQuantDatasetListObject() {
        return database.getQuantDatasetListObject();
    }

    public void correctProtInfo() {
        database.correctProtInfo();
    }

    public boolean updateDiseaseGroupsFullName(Map<String, String> diseaseGroupsNamingMap) {
        return database.updateDiseaseGroupsFullName(diseaseGroupsNamingMap);

    }

    public boolean insertPublication(String pubmedid, String author, String year, String title) {
        return database.insertPublication(pubmedid, author, year, title);

    }
      public List<Object []> getPublications() {
          return database.getPublications();
      }
      public void updateActivePublications(  Map<String, Object[]> publicationUpdatingMap){
      database.updateActivePublications(publicationUpdatingMap);
      
      
      }
       public void updateQuantStudies(  Map<Integer, Object[]> publicationUpdatingMap){
      database.updateQuantStudies(publicationUpdatingMap);
      
      
      }

}
