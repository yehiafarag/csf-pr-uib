/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.selectionmanager;

/**
 *
 * @author Yehia Farag
 */
public interface CSFFilter {

    /**
     *
     * @param type
     */
    public void selectionChanged(String type);    

    /**
     *
     * @return
     */
    public String getFilterId();

    /**
     *
     * @param value
     */
    public void removeFilterValue(String value);
    
}
