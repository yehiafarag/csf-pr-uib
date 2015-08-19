package com.helperunits;

import java.io.Serializable;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class CustomExternalLink extends VerticalLayout implements Serializable, Comparable<Object> {

    private String link;
    private String url;
    private Label label;
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param link and url
     */
    public CustomExternalLink(String link, String url) {
        this.link = link;
        this.url = url;
        label = new Label("<a href='" + url + "' target='_blank' style='color:#000000;'>" + link + "</a>");
        label.setContentMode(Label.CONTENT_XHTML);
        this.addComponent(label);



    }

    public String toString() {
        return link;
    }

    public int compareTo(Object myLink) {

        String compareLink = ((CustomExternalLink) myLink).getLink();

        //ascending order
        return (this.link.compareTo(compareLink));

        //descending order
        //return compareQuantity - this.quantity;

    }

    public String getLink() {
        return this.link;
    }

    public synchronized void rePaintLable(final String color) {
        synchronized (this) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        
                        removeAllComponents();                        
                        Label label1 = new Label("<a href='" + url + "' target='_blank' style='color:" + color + "'>" + link + "</a>");
                        label1.setContentMode(Label.CONTENT_XHTML);
                        addComponent(label1);
                    }
                });

                //t.setPriority(Thread.MAX_PRIORITY);
                t.start();
            
        }


    }
}