package probe.com.view.core;

import com.vaadin.server.ExternalResource;
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

    /**
     *
     * @param title
     */
    public InformationField(String title) {

        this.setHeight("60px");
        this.setSpacing(true);
        this.setMargin(true);
        Label titleLabel = new Label(title);
        this.addComponent(titleLabel);
        titleLabel.setStyleName("caption");
        this.setComponentAlignment(titleLabel, Alignment.TOP_LEFT);
        valueLabel = new Link();

        valueLabel.setStyleName("valuelabel");
        valueLabel.setTargetName("_blank");

//        valueLabel.setContentMode(ContentMode.HTML);
        this.addComponent(valueLabel);
        this.setComponentAlignment(valueLabel, Alignment.TOP_LEFT);

    }

    /**
     *
     * @param stringValue
     * @param urlAddress
     */
    public void setValue(Object object, String urlAddress) {
        if (object instanceof Number) {
            valueLabel.setCaption("<p style='text-align: right ;width: 100px;  float: right; line-height: 1px;'>"+object+"</p>");
            valueLabel.setCaptionAsHtml(true);
            valueLabel.setStyleName("valuelabel");
            
        } else {
            String stringValue = object.toString();

            if (stringValue == null || stringValue.trim().equalsIgnoreCase("") || stringValue.equalsIgnoreCase("-1")) {
//            this.setVisible(false);
                valueLabel.setCaption("");
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

            if (stringValue.toCharArray().length > 25) {
                valueLabel.setCaption("<textarea rows='3' cols='25' readonly>" + stringValue + "</textarea>");
                valueLabel.setCaptionAsHtml(true);
//            this.setHeight("100px");
            } else {
                valueLabel.setCaption(stringValue);
            }
            if (urlAddress != null) {
                valueLabel.setStyleName("valuelink");
                valueLabel.setResource(new ExternalResource(urlAddress));
            } else {
                valueLabel.setStyleName("valuelabel");
            }
        }
//        this.setVisible(true);
    }
}
