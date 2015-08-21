package probe.com;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import javax.servlet.ServletContext;
import probe.com.handlers.CSFPRHandler;


/**
 *@author Yehia Farag
 * The Application's "start point" class
 * this class contains the context parameters for database 
 * and local files folder 
 */
@SuppressWarnings("serial")
@Theme("dario-theme")
public class AppController extends UI {

  
    private String dbURL, dbName, dbDriver, dbUserName, dbPassword, filesURL;
    private CSFPRHandler handler;

    /**
     * initialize the application context parameters
     *
     * @param request vaadinRequest
     *
     *
     */
    @Override
    protected void init(VaadinRequest request) {
        //init param for DB
        ServletContext scx = VaadinServlet.getCurrent().getServletContext();
        dbURL = (scx.getInitParameter("url"));
        dbName = (scx.getInitParameter("dbName"));
        dbDriver = (scx.getInitParameter("driver"));
        dbUserName = (scx.getInitParameter("userName"));
        dbPassword = (scx.getInitParameter("password"));
        filesURL = scx.getInitParameter("filesURL");
        //init application  handler)
        handler = new CSFPRHandler(dbURL, dbName, dbDriver, dbUserName, dbPassword, filesURL);
        //init main layout
        CSFPRApplication application = new CSFPRApplication(handler);
        this.getPage().setTitle("CSF Proteome Resource (CSF-PR)");
        setContent(application);
    }
    
   

}
