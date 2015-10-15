package probe.com;

import com.vaadin.annotations.Theme;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.UI;
import elemental.json.JsonArray;
import java.io.File;
import javax.servlet.ServletContext;
import probe.com.handlers.CSFPRHandler;

/**
 * @author Yehia Farag The Application's "start point" class this class contains
 * the context parameters for database and local files folder
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

        this.addDetachListener(new DetachListener() {
            public void detach(DetachEvent event) {
                System.out.println("######### Detached ##########");
            }
        });

        if (!Page.getCurrent().getWebBrowser().isIE()) {

            JavaScript.getCurrent().addFunction("aboutToClose", new JavaScriptFunction() {
                @Override
                public void call(JsonArray arguments) {
                    System.out.println("at system is closing the tab");                   
                    deleteFiles(new File(filesURL));
                }
            });

            Page.getCurrent().getJavaScript().execute("window.onbeforeunload = function (e) { var e = e || window.event; aboutToClose(); return; };");
        }

    }

    private void deleteFiles(File f) {
        if (f.isDirectory() && f.listFiles().length > 0) {
            for (File subFile : f.listFiles()) {
                deleteFiles(subFile);

            }

        } else if (!f.getName().equalsIgnoreCase("CSF_Files")) {
            f.delete();
        }

    }

    @Override
    public void doInit(VaadinRequest request, int uiId, String embedId) {
        super.doInit(request, uiId, embedId); //To change body of generated methods, choose Tools | Templates.
    }

}
