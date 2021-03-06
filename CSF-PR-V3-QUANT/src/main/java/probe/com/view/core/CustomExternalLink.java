package probe.com.view.core;

import com.vaadin.shared.ui.label.ContentMode;
import java.io.Serializable;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Yehia Farag
 */
public class CustomExternalLink extends VerticalLayout implements Serializable, Comparable<Object> {

    private String link;
    private Label label;
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param link
     * @param url
     */
    public CustomExternalLink(String link, String url) {
        this.link = link;
        if (url != null) {
            url = url.replace("(UNREVIEWED)", "");
        }

        label = new Label();
        if (url != null) {
            label.setValue("<a href='" + url + "' target='_blank'>" + link.replace("(UNREVIEWED)", "<font style='color:#1D9DFF ;font-weight: bold;  font-style:italic;  text-decoration:none  !important;'>*</font>") + "</a>");
            label.setContentMode(ContentMode.HTML);
        } else {
            label.setValue(link);
        }

        label.setStyleName("externalLinkTableLabel");
        this.addComponent(label);

    }

    @Override
    public String toString() {
        return link;
    }

    @Override
    public int compareTo(Object myLink) {

        String compareLink = ((CustomExternalLink) myLink).getLink();
        //ascending order
        return (this.link.compareTo(compareLink));

    }

    /**
     *
     * @return
     */
    public String getLink() {
        return this.link;
    }

    /**
     *
     * @param color
     */
    public  void rePaintLable(final String color) {
        if (color.equalsIgnoreCase("black")) {
            label.setStyleName("externalLinkTableLabel");
        } else {
            label.setStyleName("externalLinkTableClickedLabel");
        }
//        synchronized (this) {
//            Thread t = new Thread(new Runnable() {
//                @Override
//                public void run() {
//
//                    removeAllComponents();
//                    Label label1 = new Label("<a href='" + url + "' target='_blank' style='color:" + color + "'>" + link + "</a>");
//                    label1.setContentMode(Label.CONTENT_XHTML);
//                    addComponent(label1);
//                }
//            });
//            t.start();
//            try {
//                t.join();
//                t.setPriority(Thread.MAX_PRIORITY);
//            } catch (InterruptedException iExp) {
//            }
//        }
    }
//    public String getStringLabelValue(){
//        return label.getValue();
//    }
}
