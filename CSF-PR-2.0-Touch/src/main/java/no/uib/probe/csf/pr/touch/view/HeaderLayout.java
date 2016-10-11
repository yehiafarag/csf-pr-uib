
package no.uib.probe.csf.pr.touch.view;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 *
 * @author Yehia Farag
 * 
 * this class represents the header layout container (logo, titles, and sponsors)
 */
public class HeaderLayout extends HorizontalLayout{

    public HeaderLayout() {
         this.setWidth(100,Unit.PERCENTAGE);
        this.setHeight(40,Unit.PIXELS);
        this.setSpacing(true);
        //tile logo container
        HorizontalLayout logoTitleContainer = new HorizontalLayout();
//        logoTitleContainer.setStyleName("toplogo");
        this.addComponent(logoTitleContainer);
        this.setComponentAlignment(logoTitleContainer, Alignment.TOP_LEFT);
        VerticalLayout logo = new VerticalLayout();
        logo.setWidth(100,Unit.PERCENTAGE);
        logo.setHeight(30,Unit.PIXELS);
        Label cLabel = new Label("<a href=''  align='left' style='color:#ffffff;background-color: #4d749f;border-radius:4px;width:35px !important;padding-top: 5px !important;padding-right: 5px !important;padding-left: 5px !important;font-weight:bold;text-decoration:none;'>"
                + "<font SIZE='5.5' align='left' ; style='color:#ffffff; !important;font-weight:bold;text-decoration:none;word-wrap: break-word !important;'>"
                + "C"
                + "</font>"
                + "</a> "
                + "<a href='' align='left'  style='text-decoration:none;    margin-left: -2px;'>"
                + "<font  SIZE='5.5' align='left' ; style='margin-left:0px;color:#4d749f;font-weight:bold;text-decoration:none;word-wrap: break-word !important; '>"
                + "SF Proteome Resource"
                + "<font size='2'>"
                + " v2.0"
                + "</font>"
                + "</font>"
                + "</a>");
        cLabel.setContentMode(ContentMode.HTML);
        cLabel.setStyleName(Reindeer.LABEL_SMALL);
        cLabel.setWidth(100,Unit.PERCENTAGE);
        cLabel.setHeight(100,Unit.PERCENTAGE);
        logo.addComponent(cLabel);
        logo.setComponentAlignment(cLabel, Alignment.TOP_CENTER);
        logoTitleContainer.addComponent(logo);

    }
    
}
