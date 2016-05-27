package no.uib.probe.csf.pr.touch;

import com.ejt.vaadin.sizereporter.ComponentResizeEvent;
import com.ejt.vaadin.sizereporter.SizeReporter;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import javax.servlet.ServletContext;
import no.uib.probe.csf.pr.touch.view.MainLayout;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of a html page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("mytheme")
@Widgetset("no.uib.probe.csf.pr.touch.CSF_PR_Widgetset")
public class CSF_PR_UI extends UI {

    private String dbURL, dbName, dbDriver, dbUserName, dbPassword, filesURL;

    private int windowHeight, windowWidth;
    MainLayout layout;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        //init param for DB
        ServletContext scx = VaadinServlet.getCurrent().getServletContext();
        dbURL = (scx.getInitParameter("url"));
        dbName = (scx.getInitParameter("dbName"));
        dbDriver = (scx.getInitParameter("driver"));
        dbUserName = (scx.getInitParameter("userName"));
        dbPassword = (scx.getInitParameter("password"));
        filesURL = scx.getInitParameter("filesURL");

        this.setWidth(100, Unit.PERCENTAGE);
        this.setHeight(100, Unit.PERCENTAGE);

        windowHeight = 1080;// Math.max(Page.getCurrent().getWebBrowser().getScreenHeight(), 1080);
        windowWidth = 1920;// Math.max(Page.getCurrent().getWebBrowser().getScreenWidth(), 1920);

        VerticalLayout appWrapper = new VerticalLayout();
        appWrapper.setWidth(100, Unit.PERCENTAGE);
        appWrapper.setHeight(100, Unit.PERCENTAGE);
        appWrapper.setStyleName("whitelayout");
        appWrapper.addStyleName("scrollable");
        setContent(appWrapper);

        layout = new MainLayout(dbURL, dbName, dbDriver, dbUserName, dbPassword, filesURL, windowWidth, windowHeight);

        appWrapper.addComponent(layout);
        appWrapper.setComponentAlignment(layout, Alignment.TOP_CENTER);

        final SizeReporter sizeReporter = new SizeReporter(appWrapper);

        sizeReporter.addResizeListener((ComponentResizeEvent event) -> {
            resizeScreen();
        });
    }

    String updatedZoomStyleName = "";

    /**
     * resize the layout on changing window size
     */
    private void resizeScreen() {

        int swindowHeight = Page.getCurrent().getBrowserWindowHeight();
        int swindowWidth = Page.getCurrent().getBrowserWindowWidth();
        boolean scaleOnH = false;
        if (swindowWidth == windowWidth && swindowHeight == windowHeight && updatedZoomStyleName.equalsIgnoreCase("")) {
            return;
        }
        if (swindowHeight < swindowWidth) {
            scaleOnH = true;
        }
        int zoomLevel = 0;
        if (scaleOnH) {
            double ratio = (double) swindowHeight / (double) windowHeight;
            zoomLevel = ((int) Math.round(ratio * 10.0));
        } else {
            double ratio = (double) swindowWidth / (double) windowWidth;
            zoomLevel = ((int) Math.round(ratio * 10.0));

        }
        zoomLevel = Math.max(zoomLevel, 4);
        zoomLevel = Math.min(zoomLevel, 20);

        System.out.println("at zoom level " + zoomLevel);
        layout.removeStyleName(updatedZoomStyleName);
        updatedZoomStyleName = "zoom" + zoomLevel;
        layout.setStyleName(updatedZoomStyleName);

    }

}
