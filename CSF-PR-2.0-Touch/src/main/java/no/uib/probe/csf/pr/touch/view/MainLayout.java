
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

    private final HeaderLayout header;
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
       
        this.setSpacing(true);
        this.setStyleName("whitelayout");
        windowHeight = Page.getCurrent().getBrowserWindowHeight();
        windowWidth = Page.getCurrent().getBrowserWindowWidth();
        bodyHeight = windowHeight ;//- 60;
        this.setWidth(windowWidth,Unit.PIXELS);
        this.setHeight(windowHeight,Unit.PIXELS);
//        this.setSizeFull();
        //init header
        header = new HeaderLayout();
       
//        this.addComponent(header);

        

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
        float headerRatio = 65f / (float) windowHeight;
        bodyHeight = initalHeight;// - 65;
        float bodyRatio = (float) bodyHeight / (float) initalHeight;
//        this.setExpandRatio(header, headerRatio);
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
