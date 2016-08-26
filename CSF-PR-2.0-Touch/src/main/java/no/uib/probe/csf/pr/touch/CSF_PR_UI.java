package no.uib.probe.csf.pr.touch;

import com.ejt.vaadin.sizereporter.ComponentResizeEvent;
import com.ejt.vaadin.sizereporter.SizeReporter;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import elemental.json.JsonArray;
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
    private MainLayout layout;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        //init param for DB

//        if (Page.getCurrent().getWebBrowser().getScreenWidth() > 900) {
//            Page.getCurrent().getJavaScript().execute("document.head.innerHTML +='<meta name=\"viewport\" content=\"maximum-scale = 1.0\">'");
//        }
        ServletContext scx = VaadinServlet.getCurrent().getServletContext();
        dbURL = (scx.getInitParameter("url"));
        dbName = (scx.getInitParameter("dbName"));
        dbDriver = (scx.getInitParameter("driver"));
        dbUserName = (scx.getInitParameter("userName"));
        dbPassword = (scx.getInitParameter("password"));
        filesURL = scx.getInitParameter("filesURL");
        String csf_pr_Url = scx.getInitParameter("csf-pr-id");
        VaadinSession.getCurrent().setAttribute("csf_pr_Url", csf_pr_Url);

        this.getPage().setTitle("CSF Proteome Resource (CSF-PR) v2.0");

        this.setWidth(100, Unit.PERCENTAGE);
        this.setHeight(100, Unit.PERCENTAGE);
        final SizeReporter sizeReporter = new SizeReporter(this);
        sizeReporter.addResizeListener((ComponentResizeEvent event) -> {
            resizeScreen();
        });
        resizeScreen();

        if (Page.getCurrent().getWebBrowser().isTouchDevice()) {
            if (windowHeight > windowWidth) {
                Notification.show("Use landscape screen orientation");
                return;
            }

        }
        VerticalLayout appWrapper = new VerticalLayout();
        appWrapper.setWidth(100, Unit.PERCENTAGE);
        appWrapper.setHeight(100, Unit.PERCENTAGE);
        appWrapper.setStyleName("whitelayout");
        appWrapper.addStyleName("border");
        appWrapper.addStyleName("scrollable");

        setContent(appWrapper);

        layout = new MainLayout(dbURL, dbName, dbDriver, dbUserName, dbPassword, filesURL, windowWidth, windowHeight);
        appWrapper.addComponent(layout);
        appWrapper.setComponentAlignment(layout, Alignment.MIDDLE_CENTER);

        JavaScript.getCurrent().addFunction("aboutToClose", (JsonArray arguments) -> {
//            System.out.println("at system is closing the tab   " + vaadinRequest.getContextPath());
//            Page.getCurrent().open(vaadinRequest.getContextPath(), "");
//                    Notification.show(" notifi", "dont go ", Notification.Type.ASSISTIVE_NOTIFICATION);
//                    Page.getCurrent().open(vaadinRequest.getRemoteAddr(),"");

//                    cleanUserFolder(new File(filesURL, VaadinSession.getCurrent().getSession().getId()));
        });
        Page.getCurrent().getJavaScript().execute("window.onbeforeunload = function (e) { var e = e || window.event; aboutToClose(); return; };");

    }

    /**
     * resize the layout on changing window size
     */
    private void resizeScreen() {
        windowHeight = Page.getCurrent().getBrowserWindowHeight()-20;// Math.max(Page.getCurrent().getBrowserWindowHeight(), 1080);
        windowWidth = Page.getCurrent().getBrowserWindowWidth()-20;//Math.max(Page.getCurrent().getBrowserWindowWidth(), 1920);
        if (windowHeight < 427 || windowWidth < 980) {
            windowHeight = 427;
            windowWidth = 980;
            
//            Notification.show("Screen is too small current screen resolution (" + windowWidth + "x" + windowHeight + ")", Notification.Type.ERROR_MESSAGE);
            return;
        }
        for (Window w : getWindows()) {
            w.center();
        }
    }

}
