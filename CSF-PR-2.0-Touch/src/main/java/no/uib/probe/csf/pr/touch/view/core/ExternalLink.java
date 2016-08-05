
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.server.Resource;
import com.vaadin.ui.Link;

/**
 *
 * @author Yehia Farag
 * 
 * this class represents external link object
 */
public class ExternalLink extends Link implements Comparable<ExternalLink>{

    @Override
    public int compareTo(ExternalLink o) {
        return (this.caption.compareTo(o.caption));
    }
    
    private final String caption;

    public ExternalLink(String caption, Resource resource) {
        super(caption, resource);
        this.caption = caption;
        this.setPrimaryStyleName("tablelink");
        if (!caption.startsWith("IPI")) {
            this.setTargetName("_blank");            
            this.addStyleName("link");
        } else {
            super.setEnabled(false);
        }
    }

    @Override
    public String toString() {
        System.out.println("to string is called");
        return caption; //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
