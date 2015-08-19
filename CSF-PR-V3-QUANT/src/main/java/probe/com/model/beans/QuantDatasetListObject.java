/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.model.beans;

import java.util.Map;
import java.util.Set;

/**
 *
 * @author Yehia Farag
 */
public class QuantDatasetListObject {
    private  Map<Integer,QuantDatasetObject> quantDatasetsList;
        private boolean[] activeHeaders;
        private int[] indexer;

    public  Map<Integer,QuantDatasetObject> getQuantDatasetsList() {
        return quantDatasetsList;
    }

    public void setQuantDatasetsList( Map<Integer,QuantDatasetObject> quantDatasetsList) {
        this.quantDatasetsList = quantDatasetsList;
    }

    public int[] getIndexer() {
        return indexer;
    }

    public void setIndexer(int[] indexer) {
        this.indexer = indexer;
    }
//
//    public  Map<Integer,QuantDatasetObject> getQuantDatasetList() {
//        return quantDatasetsList;
//    }
//
//    public void setQuantDatasetList( Map<Integer,QuantDatasetObject> quantDatasetsList) {
//        this.quantDatasetsList = quantDatasetsList;
//    }

    public boolean[] getActiveHeaders() {
        return activeHeaders;
    }

    public void setActiveHeaders(boolean[] activeHeaders) {
        this.activeHeaders = activeHeaders;
    }
}
