/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.model.beans.quant;

import java.util.Map;

/**
 *
 * @author Yehia Farag
 */
public class QuantDatasetInitialInformationObject {
    private  Map<Integer,QuantDatasetObject> quantDatasetsList;
        private boolean[] activeHeaders;

    /**
     *
     * @return
     */
    public  Map<Integer,QuantDatasetObject> getQuantDatasetsList() {
        return quantDatasetsList;
    }

    /**
     *
     * @param quantDatasetsList
     */
    public void setQuantDatasetsList( Map<Integer,QuantDatasetObject> quantDatasetsList) {
        this.quantDatasetsList = quantDatasetsList;
    }

    /**
     *
     * @return
     */
    public boolean[] getActiveHeaders() {
        return activeHeaders;
    }

    /**
     *
     * @param activeHeaders
     */
    public void setActiveHeaders(boolean[] activeHeaders) {
        this.activeHeaders = activeHeaders;
    }
}
