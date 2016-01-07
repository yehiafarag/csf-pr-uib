/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;



import com.nextfour.datetimelabel.client.datetimelabel.DateTimeLabelState;

/**
 * DateTimeLabel is a simple Vaadin add-on which extends normal Vaadin Label and
 * displays a date/time on it and periodically updates it. A client side timer
 * is used in updating. The date/time formatter and the update interval can be
 * configured.
 * 
 * @author Teemu Peltola
 */


/**
 *
 * @author yfa041
 */
public class LableTimer extends com.vaadin.ui.Label {
    /**
     * Create new DateTimeLabel with default date formatting string and a
     * refresh period of 1000 ms. Example: Thu Feb 06 15:41:39 GMT+200 2014
     */
    public LableTimer() {
        super();
    }

    /**
     * Create new DateTimeLabel with custom refresh interval and date / time
     * formatter
     * 
     * @param refreshIntervalMs
     *            Refresh interval, in ms.
     * @param dateFormat
     *            Format date/time displayed in Label. Uses GWT <a
     *            href="http://bit.ly/1fycsNo">DateTimeFormat</a>
     * 
     */
    public LableTimer(int refreshIntervalMs, String dateFormat) {
        super();
        getState().refreshIntervalMs = refreshIntervalMs;
        getState().dateFormat = dateFormat;
    }

    /**
     * Get current refresh interval
     * 
     * @return The current refresh interval, in ms.
     */
    public int getRefreshIntervalMs() {
        return getState().refreshIntervalMs;
    }

    /**
     * Set current refresh interval
     * 
     * @param refreshIntervalMs
     *            New refresh interval, in ms.
     */
    public void setRefreshIntervalMs(int refreshIntervalMs) {
        getState().refreshIntervalMs = refreshIntervalMs;
    }

    /**
     * Get current date format string
     * 
     * @return Current format string, see GWT <a
     *         href="http://bit.ly/1fycsNo">DateTimeFormat</a>
     */
    public String getDateFormat() {
        return getState().dateFormat;
    }

    /**
     * Set current date format string
     * 
     * @param dateFormat
     *            New date format string, see GWT <a
     *            href="http://bit.ly/1fycsNo">DateTimeFormat</a>
     */
    public void setDateFormat(String dateFormat) {
        getState().dateFormat = dateFormat;
    }

    @Override
    protected DateTimeLabelState getState() {
        return (DateTimeLabelState) super.getState();
    }

    @Override
    public void addValueChangeListener(ValueChangeListener listener) {
        super.addValueChangeListener(listener); //To change body of generated methods, choose Tools | Templates.
    }
    
   
}