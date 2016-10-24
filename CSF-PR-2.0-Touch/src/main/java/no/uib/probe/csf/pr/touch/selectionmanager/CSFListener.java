package no.uib.probe.csf.pr.touch.selectionmanager;

/**
 *
 * @author Yehia Farag CSF Filter interface that has the important abstracted
 * methods
 */
public interface CSFListener {

    /**
     * Selection changed
     *
     * @param type of selection
     */
    public void selectionChanged(String type);

    /**
     * Get the listener id
     *
     * @return listener Id
     */
    public String getListenerId();

    

}
