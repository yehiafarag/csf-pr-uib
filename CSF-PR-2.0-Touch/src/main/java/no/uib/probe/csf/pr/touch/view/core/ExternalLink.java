package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.ui.Link;

/**
 * This class represents link object with external resource.
 *
 * @author Yehia Farag
 */
public class ExternalLink extends Link implements Comparable<ExternalLink> {

    /**
     * The link caption.
     */
    private final String caption;

    /**
     * Compare to based on the text caption.
     *
     * @param externalLink another external link object.
     *
     */
    @Override
    public int compareTo(ExternalLink externalLink) {
        return (this.caption.compareTo(externalLink.caption));
    }

    /**
     * Constructor to initialize main attributes.
     *
     * @param caption The link caption
     * @param resource The link resource.
     */
    public ExternalLink(String caption, ExternalResource resource) {
        super(caption.replace("(unreviewed)", "&#x2A;").replace("(Unreviewed)", "&#x2A;").replace("(Entry Deleted)", "&#x2A;").replace("(Entry deleted)", "&#x2A;").replace("(Deleted)", "&#x2A;").replace("(Not retrieved)", "&#x2A;"), resource);
        super.setCaptionAsHtml(true);
        this.caption = caption;
        this.setPrimaryStyleName("tablelink");
        if (!caption.startsWith("IPI") && resource != null && !resource.getURL().equalsIgnoreCase("")) {
            this.setTargetName("_blank");
            this.addStyleName("underline");
        } else {
            super.setEnabled(false);
            
        }
    }

    /**
     * Override to return the text caption which is used for table exporting.
     *
     * @return the link caption.
     */
    @Override
    public String toString() {
        return caption;
    }

}
