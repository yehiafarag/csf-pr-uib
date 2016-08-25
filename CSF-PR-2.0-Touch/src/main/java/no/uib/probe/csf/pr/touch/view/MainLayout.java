package no.uib.probe.csf.pr.touch.view;

import com.vaadin.server.Page;
import no.uib.probe.csf.pr.touch.view.bigscreen.CSFApplicationContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import no.uib.probe.csf.pr.touch.view.core.ZoomControler;

/**
 *
 * @author Yehia Farag
 *
 * this class represents the main HTML template for CSF-PR 2.0 touch
 */
public class MainLayout extends VerticalLayout {

    private final VerticalLayout body;
    private final ZoomControler zoomApp;

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

        this.setWidth(windowWidth, Unit.PIXELS);
        this.setHeight(windowHeight, Unit.PIXELS);
        
        
        this.setSpacing(true);
        this.setStyleName("whitelayout");
        windowHeight=windowHeight-40;
        windowWidth=windowWidth-40;

       

        //heder is finished 
        //start body
        body = new VerticalLayout();
        body.setWidth(100, Unit.PERCENTAGE);
        body.setHeight(100, Unit.PERCENTAGE);
        this.addComponent(body);
        this.setComponentAlignment(body, Alignment.MIDDLE_CENTER);
        
        CSFApplicationContainer welcomePageContainerLayout = new CSFApplicationContainer(windowWidth, windowHeight, url, dbName, driver, userName, password, filesURL);
        body.addComponent(welcomePageContainerLayout);
        body.setComponentAlignment(welcomePageContainerLayout,Alignment.MIDDLE_CENTER);
        
        zoomApp = welcomePageContainerLayout.getZoomApp();
        
        body.addComponent(zoomApp);
        
        
        
        
      
        
    }

}
