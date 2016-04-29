package no.uib.probe.csf.pr.touch.selectionmanager;

/**
 *
 * @author Yehia Farag 
 * CSF Filter interface that has the important abstracted
 * methods
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
