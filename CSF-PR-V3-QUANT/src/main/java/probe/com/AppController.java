package probe.com;

import com.ejt.vaadin.sizereporter.ComponentResizeEvent;
import com.ejt.vaadin.sizereporter.ComponentResizeListener;
import com.ejt.vaadin.sizereporter.SizeReporter;
import com.vaadin.annotations.Theme;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import elemental.json.JsonArray;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.ServletContext;
import probe.com.handlers.CSFPRHandler;

/**
 * @author Yehia Farag
 *
 * The Application's "start point" class this class contains the context
 * parameters for database and local files folder
 */
@SuppressWarnings("serial")
@Theme("dario-theme")
public class AppController extends UI {

    private String dbURL, dbName, dbDriver, dbUserName, dbPassword, filesURL;
    private CSFPRHandler CSFPR_Handler;
    private CSFPRApplication application;

    /**
     * initialize the application context parameters
     *
     * @param request vaadinRequest
     *
     *
     */
    @Override
    protected void init(final VaadinRequest request) {
        //init param for DB
        ServletContext scx = VaadinServlet.getCurrent().getServletContext();
        dbURL = (scx.getInitParameter("url"));
        dbName = (scx.getInitParameter("dbName"));
        dbDriver = (scx.getInitParameter("driver"));
        dbUserName = (scx.getInitParameter("userName"));
        dbPassword = (scx.getInitParameter("password"));
        filesURL = scx.getInitParameter("filesURL");
        //init application  CSFPR_Handler)
        CSFPR_Handler = new CSFPRHandler(dbURL, dbName, dbDriver, dbUserName, dbPassword, filesURL);
        //init main layout
        application = new CSFPRApplication(CSFPR_Handler);
        this.getPage().setTitle("CSF Proteome Resource (CSF-PR)");
        setContent(application);
        
        
        
//         final SizeReporter reporter = new SizeReporter(application);
//                reporter.addResizeListener(new ComponentResizeListener() {
//                    @Override
//                    public void sizeChanged(ComponentResizeEvent event) {                       
//                        System.out.println("at app size event "+event.getHeight());
//                    }
//                });
//        
        
        
        
        
        
        
        
        
        this.addDetachListener(new DetachListener() {
            @Override
            public void detach(DetachEvent event) {
                System.out.println("######### Detached ##########");
            }
        });
        if (!Page.getCurrent().getWebBrowser().isIE()) {
            JavaScript.getCurrent().addFunction("aboutToClose", new JavaScriptFunction() {
                @Override
                public void call(JsonArray arguments) {
                    System.out.println("at system is closing the tab");
                    cleanUserFolder(new File(filesURL, VaadinSession.getCurrent().getSession().getId()));
                }
            });
            Page.getCurrent().getJavaScript().execute("window.onbeforeunload = function (e) { var e = e || window.event; aboutToClose(); return; };");
        } else {
            // System.out.println("its iE");            
            final Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("close everythings now :-D bye bye !");
                    cleanUserFolder(new File(filesURL, VaadinSession.getCurrent().getSession().getId()));
                    timer.cancel();
                    request.getWrappedSession().invalidate();
                }
            }, (5 * 60 * 1000 * 60));
            //maximum session time out is 5 hours
        }
        Page.getCurrent().addBrowserWindowResizeListener(new Page.BrowserWindowResizeListener() {

            @Override
            public void browserWindowResized(Page.BrowserWindowResizeEvent event) {
                if (checkSize) {
                    Page.getCurrent().reload();
                }
                checkSize = false;
                checkWindowSize();
            }
        });
        
        checkWindowSize();

    }

    private void checkWindowSize() {

        if (Page.getCurrent().getBrowserWindowWidth()< 368 || Page.getCurrent().getBrowserWindowHeight()< 403) {
            Notification.show("Opps.. Screen size is too small to use the system", Notification.Type.ERROR_MESSAGE);
            application.setVisible(false);
            checkSize = true;
        }
    }
    private boolean checkSize;

    /**
     * delete users files and perform clean over
     *
     * @param file folder or file
     */
    private void cleanUserFolder(File file) {
        if (file.isDirectory() && file.getName().equalsIgnoreCase("Resources")) {
            return;
        }
        if (file.isDirectory() && file.listFiles().length > 0) {
            for (File subFile : file.listFiles()) {
                cleanUserFolder(subFile);
            }
        } else if (!file.getName().equalsIgnoreCase("CSF_Files")) {
            file.delete();
        }

    }

}
