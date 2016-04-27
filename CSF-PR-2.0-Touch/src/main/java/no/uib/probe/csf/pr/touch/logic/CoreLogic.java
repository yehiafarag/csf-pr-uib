package no.uib.probe.csf.pr.touch.logic;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import no.uib.probe.csf.pr.touch.database.DataBaseLayer;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;
import no.uib.probe.csf.pr.touch.view.smallscreen.OverviewInfoBean;

/**
 *
 * @author Yehia Farag
 *
 * this class contains the logic layer (main computing code) this class interact
 * with both the data handler and the data access layer
 */
public class CoreLogic implements Serializable{
    
     private final DataBaseLayer database;

    public CoreLogic(String url, String dbName, String driver, String userName, String password, String filesURL) {
        database  = new DataBaseLayer(url, dbName, driver, userName, password);
    }

    
    
    
    /**
     * this method responsible for getting the resource overview information
     *
     * @return OverviewInfoBean resource information bean
     */
    public OverviewInfoBean getResourceOverviewInformation() {
        return database.getResourceOverviewInformation();

    }
      /**
     * this method responsible for getting initial publication information
     *
     * @return list of publications available in the the resource 
     */
     public List<Object[]> getPublicationList() {

        return database.getPublicationList();

    }

     /**
     * this method responsible for getting initial datasets information
     *
     * @return set of datasets information  available in the the resource 
     */
    public Set<QuantDatasetObject> getQuantDatasetList() {
        return database.getQuantDatasetList();

    }
    

}
