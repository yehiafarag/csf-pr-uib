
package no.uib.probe.csf.pr.touch.database;

import java.io.Serializable;

/**
 *
 * @author Yehia Farag
 *
 * This class represents a query bean that has all query information
 */
public class Query implements Serializable {

    private String searchDataType, searchDataset, searchBy, searchKeyWords;
    private boolean validatedProteins;

    /**
     * Return the requested dataset name
     *
     * @return
     */
    public String getSearchDataset() {
        return searchDataset;
    }

    /**
     * Set the dataset id
     *
     * @param searchDataset
     */
    public void setSearchDataset(String searchDataset) {
        this.searchDataset = searchDataset;
    }

    /**
     * Get searching by method (accession, peptide sequence..etc)
     *
     * @return searching method name
     */
    public String getSearchBy() {
        return searchBy;
    }

    /**
     * Set searching by method (accession, peptide sequence..etc)
     *
     * @param searchBy
     */
    public void setSearchBy(String searchBy) {
        this.searchBy = searchBy;
    }

    /**
     * Get searching data type (Id data, quant data)
     *
     * @return
     */
    public String getSearchDataType() {
        return searchDataType;
    }

    /**
     * set searching data type (Id data, quant data)
     *
     * @param searchDataType
     */
    public void setSearchDataType(String searchDataType) {
        this.searchDataType = searchDataType;
    }

    /**
     * Get searching keywords
     *
     * @return
     */
    public String getSearchKeyWords() {
        return searchKeyWords;
    }

    /**
     * Set searching keywords
     *
     * @param searchKeyWords
     */
    public void setSearchKeyWords(String searchKeyWords) {
        this.searchKeyWords = searchKeyWords;
    }

    /**
     * Is searching for validated proteins only
     *
     * @return
     */
    public boolean isValidatedProteins() {
        return validatedProteins;
    }

    /**
     * Set searching for validated proteins only
     *
     * @param validatedProteins
     */
    public void setValidatedProteins(boolean validatedProteins) {
        this.validatedProteins = validatedProteins;
    }

}
