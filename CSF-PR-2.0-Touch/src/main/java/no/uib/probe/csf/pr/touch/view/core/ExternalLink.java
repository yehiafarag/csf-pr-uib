
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.server.Resource;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.Link;

/**
 *
 * @author Yehia Farag
 * 
 * this class represents external link object
 */
public class ExternalLink extends Link{

    
    public ExternalLink(String caption, Resource resource) {
        super(caption, resource);
        this.setTargetName("_blank");
        this.setPrimaryStyleName("tablelink");
    }
    
}
