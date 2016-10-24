package no.uib.probe.csf.pr.touch.view;

import no.uib.probe.csf.pr.touch.view.bigscreen.CSFApplicationContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Yehia Farag
 *
 * This class represents the main HTML template for CSF-PR 2.0 touch (the main
 * layout container)
 */
public class MainLayout extends VerticalLayout {
    /**
     * Constructor to initialize the main attributes (Database connection data)
     *
     * @param url
     * @param dbName
     * @param driver
     * @param filesURL
     * @param userName
     * @param password
     */
    public MainLayout(String url, String dbName, String driver, String userName, String password, String filesURL, int windowWidth, int windowHeight) {

        this.setWidth(windowWidth, Unit.PIXELS);
        this.setHeight(windowHeight, Unit.PIXELS);

        this.setSpacing(true);
        this.setStyleName("whitelayout");
        windowHeight = windowHeight - 10;
        windowWidth = windowWidth - 10;

        CSFApplicationContainer welcomePageContainerLayout = new CSFApplicationContainer(windowWidth, windowHeight, url, dbName, driver, userName, password, filesURL);
        this.addComponent(welcomePageContainerLayout);
        this.setComponentAlignment(welcomePageContainerLayout, Alignment.TOP_CENTER);

    }

}
