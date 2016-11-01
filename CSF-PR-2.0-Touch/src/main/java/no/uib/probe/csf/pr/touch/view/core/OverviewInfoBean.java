package no.uib.probe.csf.pr.touch.view.core;

import java.io.Serializable;

/**
 * This class represents overall view object that contains all information
 * needed for the resource overview panel in the welcome page.
 *
 * @author Yehia Farag
 */
public class OverviewInfoBean implements Serializable {

    /**
     * Number of identification publications CSF-PRv1.0
     */
    private int numberOfIdPublication;
    /**
     * Number of identification studies CSF-PRv1.0
     */
    private int numberOfIdStudies;
    /**
     * Number of identification proteins CSF-PRv1.0
     */
    private int numberOfIdProteins;
    /**
     * Number of identification peptides CSF-PRv1.0
     */
    private int numberOfIdPeptides;
    /**
     * Number of quantification publications CSF-PRv2.0
     */
    private int numberOfQuantPublication;
    /**
     * Number of quantification datasets CSF-PRv2.0
     */
    private int numberOfQuantDatasets;
    /**
     * Number of quantification proteins CSF-PRv2.0
     */
    private int numberOfQuantProteins;
    /**
     * Number of quantification peptides CSF-PRv2.0
     */
    private int numberOfQuantPeptides;

    /**
     * Get number of identification publications CSF-PRv1.0
     *
     * @return numberOfIdPublication
     */
    public int getNumberOfIdPublication() {
        return numberOfIdPublication;
    }

    /**
     * Set number of identification publications CSF-PRv1.0.
     *
     * @param numberOfIdPublication number if id publications.
     */
    public void setNumberOfIdPublication(int numberOfIdPublication) {
        this.numberOfIdPublication = numberOfIdPublication;
    }

    /**
     * Get number of identification studies CSF-PRv1.0
     *
     * @return number of id studies
     */
    public int getNumberOfIdStudies() {
        return numberOfIdStudies;
    }

    /**
     * Set number of identification studies CSF-PRv1.0
     *
     * @param numberOfIdStudies number if id studies.
     */
    public void setNumberOfIdStudies(int numberOfIdStudies) {
        this.numberOfIdStudies = numberOfIdStudies;
    }

    /**
     * Get number of identification proteins CSF-PRv1.0
     *
     * @return numberOfIdProteins number of identification proteins included in
     * the system
     */
    public int getNumberOfIdProteins() {
        return numberOfIdProteins;
    }

    /**
     * Set number of identification proteins CSF-PRv1.0
     *
     * @param numberOfIdProteins number of identification proteins included in
     * the system
     */
    public void setNumberOfIdProteins(int numberOfIdProteins) {
        this.numberOfIdProteins = numberOfIdProteins;
    }

    /**
     * Get number of identification peptides CSF-PRv1.0
     *
     * @return numberOfIdPeptides number of identification peptides included in
     * the system
     */
    public int getNumberOfIdPeptides() {
        return numberOfIdPeptides;
    }

    /**
     * Set number of identification peptides CSF-PRv1.0
     *
     * @param numberOfIdPeptides Number of identification peptides included in
     * the system
     */
    public void setNumberOfIdPeptides(int numberOfIdPeptides) {
        this.numberOfIdPeptides = numberOfIdPeptides;
    }

    /**
     * Get number of quantification publications CSF-PRv2.0
     *
     * @return numberOfQuantPublication number of quant publications
     */
    public int getNumberOfQuantPublication() {
        return numberOfQuantPublication;
    }

    /**
     * Set number of quantification publications CSF-PRv2.0
     *
     * @param numberOfQuantPublication Number quant of publications.
     */
    public void setNumberOfQuantPublication(int numberOfQuantPublication) {
        this.numberOfQuantPublication = numberOfQuantPublication;
    }

    /**
     * Get number of quantification datasets CSF-PRv2.0
     *
     * @return numberOfQuantDatasets Number quant of datasets.
     */
    public int getNumberOfQuantDatasets() {
        return numberOfQuantDatasets;
    }

    /**
     * Set number of quantification datasets CSF-PRv2.0
     *
     * @param numberOfQuantDatasets Number quant of datasets.
     */
    public void setNumberOfQuantDatasets(int numberOfQuantDatasets) {
        this.numberOfQuantDatasets = numberOfQuantDatasets;
    }

    /**
     * Get number of quantification proteins CSF-PRv2.0
     *
     * @return numberOfQuantProteins Number quant of proteins.
     */
    public int getNumberOfQuantProteins() {
        return numberOfQuantProteins;
    }

    /**
     * Set number of quantification proteins CSF-PRv2.0
     *
     * @param numberOfQuantProteins Number quant of proteins.
     */
    public void setNumberOfQuantProteins(int numberOfQuantProteins) {
        this.numberOfQuantProteins = numberOfQuantProteins;
    }

    /**
     * Get number of quantification peptides CSF-PRv2.0
     *
     * @return numberOfQuantPeptides Number quant of peptides.
     */
    public int getNumberOfQuantPeptides() {
        return numberOfQuantPeptides;
    }

    /**
     * Set number of quantification peptides CSF-PRv2.0
     *
     * @param numberOfQuantPeptides Number quant of peptides.
     */
    public void setNumberOfQuantPeptides(int numberOfQuantPeptides) {
        this.numberOfQuantPeptides = numberOfQuantPeptides;
    }

}
