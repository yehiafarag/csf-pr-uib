
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
        this.setHeight(60,Unit.PIXELS);
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

        VerticalLayout rightHeaderLayout = new VerticalLayout();
        rightHeaderLayout.setStyleName("transparentlayout");

        rightHeaderLayout.setWidth(100,Unit.PERCENTAGE);

        this.addComponent(rightHeaderLayout);
        this.setComponentAlignment(rightHeaderLayout, Alignment.TOP_RIGHT);
        HorizontalLayout linksIconsLayout = new HorizontalLayout();
        linksIconsLayout.setStyleName("transparentlayout");
//        linksIconsLayout.addStyleName("margintop10");
//        rightHeaderLayout.addComponent(linksIconsLayout);
//        rightHeaderLayout.setComponentAlignment(linksIconsLayout, Alignment.MIDDLE_RIGHT);
        linksIconsLayout.setHeight(30,Unit.PIXELS);
        Link probe_ico = new Link(null, new ExternalResource("http://www.uib.no/rg/probe"));
        probe_ico.setIcon(new ThemeResource("img/probe.png"));
        probe_ico.setTargetName("_blank");
        probe_ico.setWidth(237,Unit.PIXELS);
        probe_ico.setHeight(58,Unit.PIXELS);
        linksIconsLayout.addComponent(probe_ico);

        Link uib_ico = new Link(null, new ExternalResource("http://www.uib.no/"));
        uib_ico.setIcon(new ThemeResource("img/uib.png"));
        uib_ico.setTargetName("_blank");
        uib_ico.setWidth(87,Unit.PIXELS);
        uib_ico.setHeight(58,Unit.PIXELS);
        linksIconsLayout.addComponent(uib_ico);

        Link kgj_ico = new Link(null, new ExternalResource("http://www.helse-bergen.no/en/OmOss/Avdelinger/ms/Sider/om-oss.aspx"));
        kgj_ico.setIcon(new ThemeResource("img/kgj.png"));
        kgj_ico.setTargetName("_blank");
        kgj_ico.setHeight(58,Unit.PIXELS);
        linksIconsLayout.addComponent(kgj_ico);

//        VerticalLayout rightSpacer = new VerticalLayout();
//        rightSpacer.setWidth(40,Unit.PIXELS);
//        rightSpacer.setHeight(5,Unit.PIXELS);

//        linksIconsLayout.addComponent(rightSpacer);
        linksIconsLayout.setComponentAlignment(probe_ico, Alignment.MIDDLE_RIGHT);
        linksIconsLayout.setComponentAlignment(uib_ico, Alignment.MIDDLE_RIGHT);
        linksIconsLayout.setComponentAlignment(kgj_ico, Alignment.MIDDLE_RIGHT);
//        linksIconsLayout.setComponentAlignment(rightSpacer, Alignment.MIDDLE_RIGHT);
    }
    
}
