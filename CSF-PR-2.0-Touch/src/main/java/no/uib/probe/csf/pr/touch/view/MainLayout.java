/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view;

import no.uib.probe.csf.pr.touch.view.bigscreen.CSFApplicationContainer;
import com.ejt.vaadin.sizereporter.ComponentResizeEvent;
import com.ejt.vaadin.sizereporter.SizeReporter;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 *
 * @author Yehia Farag
 *
 * this class represents the main HTML template for CSF-PR 2.0 touch
 */
public class MainLayout extends VerticalLayout {

    private final HorizontalLayout header;
    private final VerticalLayout body;
    private int windowHeight, windowWidth, bodyHeight;
    private int initalWidth, initalHeight;

    /**
     *
     * @param url
     * @param dbName
     * @param driver
     * @param filesURL
     * @param userName
     * @param password
     */
    public MainLayout(String url, String dbName, String driver, String userName, String password, String filesURL) {
        this.setWidth("100%");
        this.setHeight("100%");
        this.setSizeFull();
        this.setSpacing(true);
        this.setStyleName("whitelayout");
        windowHeight = Page.getCurrent().getBrowserWindowHeight();
        windowWidth = Page.getCurrent().getBrowserWindowWidth();
        bodyHeight = windowHeight - 60;

        //init header
        header = new HorizontalLayout();
        header.setWidth("100%");
        header.setHeight("60px");
        header.setSpacing(true);
        this.addComponent(header);

        //tile logo container
        HorizontalLayout logoTitleContainer = new HorizontalLayout();
        logoTitleContainer.setStyleName("toplogo");
        header.addComponent(logoTitleContainer);
        header.setComponentAlignment(logoTitleContainer, Alignment.MIDDLE_LEFT);
        VerticalLayout logo = new VerticalLayout();
        logo.setWidth("100%");
        logo.setHeight("30px");
        Label cLabel = new Label("<a href=''  align='left' style='color:#ffffff;background-color: #4d749f;border-radius:4px;width:35px !important;padding-top: 5px !important;padding-right: 5px !important;padding-left: 5px !important;font-weight:bold;text-decoration:none;'>"
                + "<font SIZE='5.5' align='left' ; style='color:#ffffff; !important;font-weight:bold;text-decoration:none;word-wrap: break-word !important;'>"
                + "C"
                + "</font>"
                + "</a> "
                + "<a href='' align='left'  style='text-decoration:none;    margin-left: -2px;'>"
                + "<font  SIZE='5.5' align='left' ; style='margin-left:0px;color:#4d749f;font-weight:bold;text-decoration:none;word-wrap: break-word !important; '>"
                + "SF Proteome Resource (CSF-PR)"
                + "<font size='2'>"
                + " v2.0"
                + "</font>"
                + "</font>"
                + "</a>");
        cLabel.setContentMode(ContentMode.HTML);
        cLabel.setStyleName(Reindeer.LABEL_SMALL);
        cLabel.setWidth("100%");
        cLabel.setHeight("100%");
        logo.addComponent(cLabel);
        logo.setComponentAlignment(cLabel, Alignment.MIDDLE_CENTER);
        logoTitleContainer.addComponent(logo);

        VerticalLayout rightHeaderLayout = new VerticalLayout();
        rightHeaderLayout.setStyleName("transparentlayout");

        rightHeaderLayout.setWidth("100%");

        header.addComponent(rightHeaderLayout);
        header.setComponentAlignment(rightHeaderLayout, Alignment.TOP_CENTER);
        HorizontalLayout linksIconsLayout = new HorizontalLayout();
        linksIconsLayout.setStyleName("transparentlayout");
        linksIconsLayout.addStyleName("margintop10");
        rightHeaderLayout.addComponent(linksIconsLayout);
        rightHeaderLayout.setComponentAlignment(linksIconsLayout, Alignment.MIDDLE_RIGHT);
        linksIconsLayout.setHeight("30px");
        Link probe_ico = new Link(null, new ExternalResource("http://www.uib.no/rg/probe"));
        probe_ico.setIcon(new ThemeResource("img/probe.png"));
        probe_ico.setTargetName("_blank");
        probe_ico.setWidth("237px");
        probe_ico.setHeight("58px");
        linksIconsLayout.addComponent(probe_ico);

        Link uib_ico = new Link(null, new ExternalResource("http://www.uib.no/"));
        uib_ico.setIcon(new ThemeResource("img/uib.png"));
        uib_ico.setTargetName("_blank");
        uib_ico.setWidth("87px");
        uib_ico.setHeight("58px");
        linksIconsLayout.addComponent(uib_ico);

        Link kgj_ico = new Link(null, new ExternalResource("http://www.stiftkgj.no/"));
        kgj_ico.setIcon(new ThemeResource("img/kgj.png"));
        kgj_ico.setTargetName("_blank");
        kgj_ico.setHeight("58px");
        linksIconsLayout.addComponent(kgj_ico);

        VerticalLayout rightSpacer = new VerticalLayout();
        rightSpacer.setWidth("40px");
        rightSpacer.setHeight("5px");

        linksIconsLayout.addComponent(rightSpacer);
        linksIconsLayout.setComponentAlignment(probe_ico, Alignment.MIDDLE_RIGHT);
        linksIconsLayout.setComponentAlignment(uib_ico, Alignment.MIDDLE_RIGHT);
        linksIconsLayout.setComponentAlignment(kgj_ico, Alignment.MIDDLE_RIGHT);
        linksIconsLayout.setComponentAlignment(rightSpacer, Alignment.MIDDLE_RIGHT);

        //heder is finished 
        //start body
        body = new VerticalLayout();
        body.setWidth(100, Unit.PERCENTAGE);
        body.setHeight(100, Unit.PERCENTAGE);
        this.addComponent(body);
        this.setComponentAlignment(body, Alignment.TOP_CENTER);
        final SizeReporter sizeReporter = new SizeReporter(this);

        windowHeight = Math.max(Page.getCurrent().getWebBrowser().getScreenHeight(), 800);
        windowWidth = Math.max(Page.getCurrent().getWebBrowser().getScreenWidth(), 800);

        sizeReporter.addResizeListener((ComponentResizeEvent event) -> {

            System.out.println("at size updated time to rezoom WH : " + UI.getCurrent().getWindows().iterator().next().getPositionX() + "    " + Page.getCurrent().getBrowserWindowHeight() + "   WW: " + Page.getCurrent().getBrowserWindowWidth() + "  browser width:  " + Page.getCurrent().getWebBrowser().getScreenWidth() + "  browser H:  " + Page.getCurrent().getWebBrowser().getScreenHeight());
            resizeScreen();
        });

        initalWidth = Page.getCurrent().getBrowserWindowWidth();
        initalHeight = Page.getCurrent().getBrowserWindowHeight();
        float headerRatio = 60f / (float) windowHeight;
        bodyHeight = initalHeight - 60;
        float bodyRatio = (float) bodyHeight / (float) initalHeight;
        this.setExpandRatio(header, headerRatio);
        this.setExpandRatio(body, bodyRatio);
        CSFApplicationContainer welcomePageContainerLayout = new CSFApplicationContainer(initalWidth, bodyHeight, url, dbName, driver, userName, password, filesURL);
        body.addComponent(welcomePageContainerLayout);

        resizeScreen();

    }
    String updatedZoomStyleName = "";

    /**
     * resize the layout on changing window size
     */
    private void resizeScreen() {

        int swindowHeight = Page.getCurrent().getBrowserWindowHeight();
        int swindowWidth = Page.getCurrent().getBrowserWindowWidth();
        boolean scaleOnH = false, scaleOnW = false;

        if (swindowHeight > swindowWidth) {
            scaleOnW = true;

        } else {
            scaleOnH = true;
        }
        int zoomLevel = 0;

        if (scaleOnH) {
            if (initalHeight > swindowHeight) {
                double ratio = (double) swindowHeight / (double) initalHeight;
                System.out.println(" scaleOnH zoom level " + ratio);
                zoomLevel = ((int) Math.round(ratio * 10.0));

            } else {
                zoomLevel = 10;
            }
        } else {
            if (initalWidth > swindowWidth) {
                double ratio = (double) swindowWidth / (double) initalWidth;
                System.out.println("scaleOnW zoom level " + ratio);
                zoomLevel = ((int) Math.round(ratio * 10.0));

            } else {
                zoomLevel = 10;
            }
        }

        this.removeStyleName(updatedZoomStyleName);
        updatedZoomStyleName = "zoom" + zoomLevel;
        System.out.println("zoom level " + zoomLevel);
        header.addStyleName(updatedZoomStyleName);
        body.setStyleName(updatedZoomStyleName);
//        UI.getCurrent().getWindows().iterator().next().addStyleName(updatedZoomStyleName);

    }

}
