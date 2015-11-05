
package probe.com.model.beans;

import java.io.Serializable;

/**
 *
 * @author yfa041
 * this class represents overall view object that contains all information needed for the welcome page 
 */
public class OverviewInfoBean implements Serializable{
    
    private int numberOfIdPublication;
    private int numberOfIdStudies;
    private int numberOfIdProteins;
    private int numberOfIdPeptides;
    private int numberOfQuantPublication;
    private int numberOfQuantStudies;
    private int numberOfQuantProteins; 
    private int numberOfQuantPeptides;

    public int getNumberOfIdPublication() {
        return numberOfIdPublication;
    }

    public void setNumberOfIdPublication(int numberOfIdPublication) {
        this.numberOfIdPublication = numberOfIdPublication;
    }

    public int getNumberOfIdStudies() {
        return numberOfIdStudies;
    }

    public void setNumberOfIdStudies(int numberOfIdStudies) {
        this.numberOfIdStudies = numberOfIdStudies;
    }

    public int getNumberOfIdProteins() {
        return numberOfIdProteins;
    }

    public void setNumberOfIdProteins(int numberOfIdProteins) {
        this.numberOfIdProteins = numberOfIdProteins;
    }

    public int getNumberOfIdPeptides() {
        return numberOfIdPeptides;
    }

    public void setNumberOfIdPeptides(int numberOfIdPeptides) {
        this.numberOfIdPeptides = numberOfIdPeptides;
    }

    public int getNumberOfQuantPublication() {
        return numberOfQuantPublication;
    }

    public void setNumberOfQuantPublication(int numberOfQuantPublication) {
        this.numberOfQuantPublication = numberOfQuantPublication;
    }

    public int getNumberOfQuantStudies() {
        return numberOfQuantStudies;
    }

    public void setNumberOfQuantStudies(int numberOfQuantStudies) {
        this.numberOfQuantStudies = numberOfQuantStudies;
    }

    public int getNumberOfQuantProteins() {
        return numberOfQuantProteins;
    }

    public void setNumberOfQuantProteins(int numberOfQuantProteins) {
        this.numberOfQuantProteins = numberOfQuantProteins;
    }

    public int getNumberOfQuantPeptides() {
        return numberOfQuantPeptides;
    }

    public void setNumberOfQuantPeptides(int numberOfQuantPeptides) {
        this.numberOfQuantPeptides = numberOfQuantPeptides;
    }
    
    
}
