package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Yehia Farag
 */
public class InformationField extends VerticalLayout {

    private final Link valueLabel;
    private final  Label titleLabel ;

    /**
     *
     * @param title
     */
    public InformationField(String title) {

        this.setHeight(60,Unit.PIXELS);
        this.setWidth(100,Unit.PERCENTAGE);
        this.setSpacing(true);
        this.setMargin(new MarginInfo(false, false, true, false));
        titleLabel = new Label(title);
        this.addComponent(titleLabel);
        titleLabel.setStyleName("caption");
        titleLabel.setHeight("100%");
        titleLabel.setWidth(200,Unit.PIXELS);
        this.setComponentAlignment(titleLabel, Alignment.TOP_LEFT);
        valueLabel = new Link();

        valueLabel.setStyleName("link");
//        valueLabel.setStyleName("valuelabel");
        valueLabel.setTargetName("_blank");
        valueLabel.setCaptionAsHtml(true);
        valueLabel.setHeight("100%");
        valueLabel.setWidth("90%");

        this.addComponent(valueLabel);
        this.setComponentAlignment(valueLabel, Alignment.TOP_LEFT);

    }

    /**
     *
     * @param object
     * @param urlAddress
     */
    public void setValue(Object object, String urlAddress) {

        if (object instanceof Number && urlAddress == null) {
            valueLabel.setCaption("<p style='text-align: left ;width: 100px;   line-height: 1px;'>" + object + "</p>");
            valueLabel.setCaptionAsHtml(true);
            valueLabel.setStyleName("valuelabel");
//            valueLabel.setWidth("100%");

        } else if (object.toString().contains("/") && !object.toString().contains("</") && !object.toString().contains("/MS") && object.toString().toCharArray().length < 25) {
            valueLabel.setCaption("<p style='text-align: left ;width:140px;   line-height: 1px;'>" + object + "</p>");
            valueLabel.setCaptionAsHtml(true);
            valueLabel.setStyleName("valuelabel");

        } else {
            String stringValue = object.toString();

            if (stringValue == null || stringValue.trim().equalsIgnoreCase("") || stringValue.equalsIgnoreCase("-1") || stringValue.equalsIgnoreCase("Not Available")) {
//            this.setVisible(false);
                valueLabel.setCaption("<font style='text-align: center ;   line-height: 1px;'>------------</font>");
                valueLabel.setStyleName("valuelabel");
                return;
            }
            if (stringValue.contains("Increase")) {
                valueLabel.setCaption(stringValue);
                valueLabel.setStyleName("redvaluelabel");
                return;

            }
            if (stringValue.contains("Decrease")) {
                valueLabel.setCaption(stringValue);
                valueLabel.setStyleName("greenvaluelabel");
                return;

            }
            if (stringValue.contains("Significant (") && !stringValue.contains("Not Significant (")) {
                valueLabel.setCaption(stringValue);
                valueLabel.setStyleName("bluevaluelabel");
                return;

            }
            if (stringValue.contains("</font>")) {
                valueLabel.setCaption(stringValue);
            } else if (stringValue.toCharArray().length > 100) {
                valueLabel.setCaption("<textarea cols='100' rows='5'  readonly>" + stringValue + "</textarea>");
                valueLabel.setCaptionAsHtml(true);
                int h=Math.min(((stringValue.toCharArray().length /100)*20),70);
                valueLabel.setHeight(h,Unit.PIXELS);
                titleLabel.setHeight(20,Unit.PIXELS);
                this.setSpacing(false);
                this.setHeightUndefined();
            } else {
                valueLabel.setCaption(stringValue);
            }
            if (urlAddress != null) {
                valueLabel.setStyleName("link");
                valueLabel.addStyleName("valuelink");
                valueLabel.addStyleName("pointer");
                valueLabel.setResource(new ExternalResource(urlAddress));
            } else {
                valueLabel.setStyleName("valuelabel");
            }
        }
//        this.setVisible(true);
    }
}
