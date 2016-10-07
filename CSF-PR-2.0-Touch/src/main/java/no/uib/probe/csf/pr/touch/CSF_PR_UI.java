package no.uib.probe.csf.pr.touch;

import com.ejt.vaadin.sizereporter.ComponentResizeEvent;
import com.ejt.vaadin.sizereporter.SizeReporter;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
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
    private final Window noti = new Window();
    private boolean init = true, toReload = false;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        noti.setWidth(60, Unit.PERCENTAGE);
        noti.setHeight(20, Unit.PERCENTAGE);
        noti.setClosable(false);
        noti.setModal(true);
        noti.setDraggable(false);
        noti.setResizable(false);
        noti.center();
        noti.setVisible(false);
        UI.getCurrent().addWindow(noti);
        VerticalLayout container = new VerticalLayout();
        container.setSizeFull();
        noti.addStyleName("notification");
        noti.setWindowMode(WindowMode.NORMAL);

        Label message = new Label("Use landscape screen orientation (a bigger screen is recommended)");
//         message.addStyleName(ValoTheme.LABEL_SPINNER);
        message.addStyleName(ValoTheme.LABEL_H2);
        message.setWidth(70, Unit.PERCENTAGE);
        container.addComponent(message);
        container.setComponentAlignment(message, Alignment.MIDDLE_CENTER);
        noti.setContent(container);

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
        if (toReload) {
            return;
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
////
////        JavaScript.getCurrent().addFunction("aboutToClose", (JsonArray arguments) -> {
//////            System.out.println("at system is closing the tab   " + vaadinRequest.getContextPath());
//////            Page.getCurrent().open(vaadinRequest.getContextPath(), "");
////                    
////        });
//        Page.getCurrent().getJavaScript().execute("window.onbeforeunload = function (e) { var e = e || window.event; aboutToClose(); return; };");
//        this.getWindows().iterator().next().addCloseListener(new Window.CloseListener() {
//
//            @Override
//            public void windowClose(Window.CloseEvent e) {
//               
//                Notification.show("wndows are closing");
//            }
//        });

    }

    /**
     * resize the layout on changing window size
     */
    private void resizeScreen() {
        windowHeight = Math.max(615, Page.getCurrent().getBrowserWindowHeight() - 20);// Math.max(Page.getCurrent().getBrowserWindowHeight(), 1080);
        windowWidth = Math.max(1004, Page.getCurrent().getBrowserWindowWidth() - 20);//Math.max(Page.getCurrent().getBrowserWindowWidth(), 1920);
        if (Page.getCurrent().getWebBrowser().isTouchDevice() && Page.getCurrent().getBrowserWindowHeight() > Page.getCurrent().getBrowserWindowWidth()) {
            noti.setVisible(true);
            toReload = true;
        } else {           
            noti.setVisible(false);
            if (layout == null && toReload) {
                Page.getCurrent().reload();
            } 
            toReload = false;
            for (Window w : getWindows()) {
                w.center();
            }
        }

    }

}
