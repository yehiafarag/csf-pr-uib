/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.dal;

import java.io.Serializable;

/**
 *
 * @author Yehia Farag
 */
public class Query implements Serializable{
    private String searchDataType,searchDataset,searchBy,searchKeyWords;
    private boolean validatedProteins;

    /**
     *
     * @return
     */
    public String getSearchDataset() {
        return searchDataset;
    }

    /**
     *
     * @param searchDataset
     */
    public void setSearchDataset(String searchDataset) {
        this.searchDataset = searchDataset;
    }

    /**
     *
     * @return
     */
    public String getSearchBy() {
        return searchBy;
    }

    /**
     *
     * @param searchBy
     */
    public void setSearchBy(String searchBy) {
        this.searchBy = searchBy;
    }

    /**
     *
     * @return
     */
    public String getSearchDataType() {
        return searchDataType;
    }

    /**
     *
     * @param searchDataType
     */
    public void setSearchDataType(String searchDataType) {
        this.searchDataType = searchDataType;
    }

    /**
     *
     * @return
     */
    public String getSearchKeyWords() {
        return searchKeyWords;
    }

    /**
     *
     * @param searchKeyWords
     */
    public void setSearchKeyWords(String searchKeyWords) {
        this.searchKeyWords = searchKeyWords;
    }

    /**
     *
     * @return
     */
    public boolean isValidatedProteins() {
        return validatedProteins;
    }

    /**
     *
     * @param validatedProteins
     */
    public void setValidatedProteins(boolean validatedProteins) {
        this.validatedProteins = validatedProteins;
    }
    
}
