/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.CoreLogic;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseCategoryObject;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseGroupComparison;
import no.uib.probe.csf.pr.touch.logic.beans.HeatMapHeaderCellInformationBean;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;
import no.uib.probe.csf.pr.touch.logic.beans.QuantProtein;
import no.uib.probe.csf.pr.touch.logic.dataset.DatasetUtility;
import no.uib.probe.csf.pr.touch.view.smallscreen.OverviewInfoBean;
import org.jsoup.helper.DataUtil;

/**
 *
 * @author Yehia Farag
 *
 * this class responsible for handling the datasets across the system the
 * handler interact with both visualization and logic layer
 *
 *
 */
public class Data_Handler implements Serializable {

    private final CoreLogic coreLogic;
    private final DatasetUtility Dataset_Util;

    /**
     *
     * @param url database url
     * @param dbName database name
     * @param driver database driver
     * @param userName database username
     * @param filesURL user folder url
     * @param password database password
     */
    public Data_Handler(String url, String dbName, String driver, String userName, String password, String filesURL) {

        this.coreLogic = new CoreLogic(url, dbName, driver, userName, password, filesURL);
        Dataset_Util = new DatasetUtility(coreLogic);

    }

    /**
     * this method responsible for getting the resource overview information
     *
     * @return OverviewInfoBean resource information bean
     */
    public OverviewInfoBean getResourceOverviewInformation() {
        return this.coreLogic.getResourceOverviewInformation();

    }

    /**
     * this method responsible for getting initial publication information
     *
     * @return list of publications available in the the resource
     */
    public List<Object[]> getPublicationList() {

        return this.coreLogic.getPublicationList();

    }

    /**
     * this method responsible for getting initial datasets information
     *
     * @return set of datasets information available in the the resource
     */
    public Set<QuantDatasetObject> getQuantDatasetList() {
        return this.coreLogic.getQuantDatasetList();

    }

    /**
     * Get the current available disease category list
     *
     * @return set of disease category objects that has all disease category
     * information and styling information
     */
    public Collection<DiseaseCategoryObject> getDiseaseCategorySet() {
        return Dataset_Util.getFullDiseaseCategorySet();
    }

    /**
     * this method responsible for loading and initialize all information for
     * the selected disease category
     *
     * @param diseaseCategory
     */
    public void loadDiseaseCategory(String diseaseCategory) {
        Dataset_Util.setMainDiseaseCategory(diseaseCategory);
    }

    /**
     * get active quantification pie charts filters (to hide them if they are
     * empty)
     *
     * @return boolean array for the active and not active pie chart filters
     * indexes
     */
    public Map<String, boolean[]> getActivePieChartQuantFilters() {
        return coreLogic.getActivePieChartQuantFilters();

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
        return coreLogic.getActivePieChartQuantFilters(searchQuantificationProtList);

    }

    /**
     * this method to get the disease group row labels for the current active
     * disease category
     *
     * @return active row labels category set
     */
    public LinkedHashSet<HeatMapHeaderCellInformationBean> getRowLabels() {
        return Dataset_Util.getRowLabels();

    }

    /**
     * this method to get the disease group column labels for the current active
     * disease category
     *
     * @return active column labels category set
     */
    public LinkedHashSet<HeatMapHeaderCellInformationBean> getColumnLabels() {
        return Dataset_Util.getColumnLabels();

    }

    /* this method to get the disease group comparisons  for the current active 
     * disease category
     *
     * @return active disease group comparisons
     */
    public Set<DiseaseGroupComparison> getDiseaseGroupComparisonsSet() {
        return Dataset_Util.getDiseaseGroupComparisonsSet();

    }

    /**
     * this method to get the full quant dataset map the current active disease
     * category
     *
     * @return map of quant dataset objects
     */
    public Map<Integer, QuantDatasetObject> getFullQuantDsMap() {
        return Dataset_Util.getFullQuantDsMap();
    }

    /**
     * this method to get the active data columns for the current active disease
     * category
     *
     * @return boolean array of active column
     */
    public boolean[] getActiveDataColumns() {

        return Dataset_Util.getActiveDataColumns();
    }

    /**
     * this method to update and combine disease sub groups based on user selection
     * 
     *
     * @param  updatedGroupsNamesMap updated disease sub group names
     */
    public void updateCombinedGroups(Map<String, Map<String, String>> updatedGroupsNamesMap) {
        Dataset_Util.updateCobinedGroups(updatedGroupsNamesMap);

    }
    
    
    /**
     * this method allow users to filter the datasets based on sample type (CSF
     * / Serum)
     *
     *
     * @param serumApplied show Serum datasets
     * @param csfApplied show CSF datasets
     */
     public void updateCSFSerumDatasets(boolean serumApplied, boolean csfApplied){
         Dataset_Util.updateCSFSerumDatasets(serumApplied, csfApplied);
     }

}
