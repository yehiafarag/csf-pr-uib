package no.uib.probe.csf.pr.touch.logic.beans;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author Yehia Farag
 *
 * This class is used for re-indexing datasets in the heat-map layout
 *
 */
public class QuantDSIndexes implements Serializable {

    /*
     *Datasetset index value
     */
    private int value;
    /*
     *Datasets map (dataset index in the database to dataset object)
     */
    private Map<Integer, QuantDataset> datasetMap;

    /**
     * Get datasets map (dataset index in the database to dataset object)
     *
     * @return datasetMap
     */
    public Map<Integer, QuantDataset> getDatasetMap() {
        return datasetMap;
    }

    /**
     * Set datasets map (dataset index in the database to dataset object)
     *
     * @param datasetMap
     */
    public void setDatasetMap(Map<Integer, QuantDataset> datasetMap) {
        this.datasetMap = datasetMap;
    }

    /**
     * Get indexing value
     *
     * @return value
     */
    public int getValue() {
        return value;
    }

    /**
     * Set indexing value
     *
     * @param value
     */
    public void setValue(int value) {
        this.value = value;
    }
}
