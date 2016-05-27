package no.uib.probe.csf.pr.touch.view;

import no.uib.probe.csf.pr.touch.view.bigscreen.CSFApplicationContainer;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Yehia Farag
 *
 * this class represents the main HTML template for CSF-PR 2.0 touch
 */
public class MainLayout extends VerticalLayout {

    private final HeaderLayout header;
    private final VerticalLayout body;
    private int windowHeight, windowWidth;
//    private int initalWidth, initalHeight;

    /**
     *
     * @param url
     * @param dbName
     * @param driver
     * @param filesURL
     * @param userName
     * @param password
     */
    public MainLayout(String url, String dbName, String driver, String userName, String password, String filesURL,int windowWidth ,int windowHeight) {

        this.setSpacing(true);
        this.setStyleName("whitelayout");
        this.windowHeight =windowHeight;
        this.windowWidth = windowWidth;

        this.setWidth(windowWidth, Unit.PIXELS);
        this.setHeight(windowHeight, Unit.PIXELS);
        header = new HeaderLayout();

        //heder is finished 
        //start body
        body = new VerticalLayout();
        body.setWidth(100, Unit.PERCENTAGE);
        body.setHeight(100, Unit.PERCENTAGE);
        this.addComponent(body);
        this.setComponentAlignment(body, Alignment.TOP_CENTER);
        
        CSFApplicationContainer welcomePageContainerLayout = new CSFApplicationContainer(windowWidth, windowHeight, url, dbName, driver, userName, password, filesURL);
        body.addComponent(welcomePageContainerLayout);
    }

}
