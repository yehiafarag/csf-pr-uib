package no.uib.probe.csf.pr.touch;

import com.ejt.vaadin.sizereporter.ComponentResizeEvent;
import com.ejt.vaadin.sizereporter.SizeReporter;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Notification;
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
        String csf_pr_Url = scx.getInitParameter("csf-pr-id");
        VaadinSession.getCurrent().setAttribute("csf_pr_Url", csf_pr_Url);

        this.getPage().setTitle("CSF Proteome Resource (CSF-PR) v2.0");

        this.setWidth(100, Unit.PERCENTAGE);
        this.setHeight(100, Unit.PERCENTAGE);

        windowHeight = Page.getCurrent().getBrowserWindowHeight();// Math.max(Page.getCurrent().getBrowserWindowHeight(), 1080);
        windowWidth = Page.getCurrent().getBrowserWindowWidth();//Math.max(Page.getCurrent().getBrowserWindowWidth(), 1920);
        if (windowHeight < 720) {
            Notification.show("Screen is too small",Notification.Type.ERROR_MESSAGE);
            
            System.out.println("it is small window height "+720);
            windowHeight = 720;
            return;
        }
        if (windowWidth < 1000) {
              Notification.show("Screen is too small",Notification.Type.ERROR_MESSAGE);
            windowWidth = 1000;
            System.out.println("it is small window width "+windowWidth);
             return;
        }

//        windowWidth = 1000;//640;
//        windowHeight = 720;//480;
        VerticalLayout appWrapper = new VerticalLayout();
        appWrapper.setWidth(100, Unit.PERCENTAGE);
        appWrapper.setHeight(100, Unit.PERCENTAGE);
        appWrapper.setStyleName("whitelayout");

        setContent(appWrapper);

        layout = new MainLayout(dbURL, dbName, dbDriver, dbUserName, dbPassword, filesURL, windowWidth, windowHeight);
        appWrapper.addComponent(layout);
        appWrapper.setComponentAlignment(layout, Alignment.MIDDLE_CENTER);

        final SizeReporter sizeReporter = new SizeReporter(appWrapper);

        sizeReporter.addResizeListener((ComponentResizeEvent event) -> {
            resizeScreen();
        });
        resizeScreen();

    }

    String updatedZoomStyleName = "";

    /**
     * resize the layout on changing window size
     */
    private void resizeScreen() {

//        int swindowHeight = Page.getCurrent().getBrowserWindowHeight();
//        int swindowWidth = Page.getCurrent().getBrowserWindowWidth();
//        boolean scaleOnH = false;
//        if (swindowWidth == windowWidth && swindowHeight == windowHeight && updatedZoomStyleName.equalsIgnoreCase("")) {
//            return;
//        }
//        if (swindowHeight < swindowWidth) {
//            scaleOnH = true;
//        }
//        int zoomLevel = 0;
//        if (scaleOnH) {
//            double ratio = (double) swindowHeight / (double) windowHeight;
//            zoomLevel = ((int) Math.round(ratio * 10.0));
//        } else {
//            double ratio = (double) swindowWidth / (double) windowWidth;
//            zoomLevel = ((int) Math.round(ratio * 10.0));
//
//        }
//        zoomLevel = Math.max(zoomLevel, 4);
//        zoomLevel = Math.min(zoomLevel, 20);
//
//        layout.removeStyleName(updatedZoomStyleName);
//        updatedZoomStyleName = "zoom" + zoomLevel;
//        layout.setStyleName(updatedZoomStyleName);
//
//        VaadinSession.getCurrent().setAttribute("zoomStyle", updatedZoomStyleName);
//        System.out.println("window h " + windowHeight + "   window w " + windowWidth + " zoom " + updatedZoomStyleName);
//
    }

}
//
//        layout.setStyleName("zoomapp");
//    }
//
//    String updatedZoomStyleName = "";
//    private int lastResizedHeight, lastResizedWidth;
//
//    /**
//     * resize the layout on changing window size
//     */
//    private void resizeScreen() {
//
//        int scaleWidth = Page.getCurrent().getWebBrowser().getScreenWidth();
//        int scaleHeight = Page.getCurrent().getWebBrowser().getScreenHeight()-200;
//        boolean scaleOnH = true;
//        if (scaleHeight > scaleWidth) {
//            scaleOnH = false;
//        }
//
//        int swindowHeight = Page.getCurrent().getBrowserWindowHeight();
//        int swindowWidth = Page.getCurrent().getBrowserWindowWidth();
//         double ratio=1;
//        if(swindowHeight <scaleHeight && swindowWidth<=scaleWidth) //scale on both
//        {
//            if(scaleOnH){
//                ratio=scaleHeight/swindowHeight;
//            }else{
//                ratio= scaleWidth/swindowWidth;
//            
//             }
//        
//        }
//        
//
////        if (lastResizedWidth != swindowWidth && lastResizedHeight != swindowHeight) {
////            if (swindowWidth < swindowHeight) {
//////                int  updatedWidth = swindowWidth;
////                swindowHeight = windowHeight * swindowWidth / windowWidth;
////
////            } else {
////                swindowWidth = swindowHeight * windowWidth / windowHeight;
////            }
////
////        } else if (lastResizedWidth != swindowWidth) {
////
////            swindowHeight = windowHeight * swindowWidth / windowWidth;
////
////        } else {
////            swindowWidth = swindowHeight * windowWidth / windowHeight;
////
////        }
//
////        boolean scaleOnH = false;
////        if (swindowWidth == windowWidth && swindowHeight == windowHeight && updatedZoomStyleName.equalsIgnoreCase("")) {
////            return;
////        }
////        if (swindowHeight < swindowWidth) {
////        scaleOnH = true;
//////        }
////        int zoomLevel = 0;
////        double ratio;
////        if (scaleOnH) {
////
////            ratio = (double) swindowHeight / (double) windowHeight;
////            zoomLevel = ((int) Math.round(ratio * 10.0));
////        } else {
////            ratio = (double) swindowWidth / (double) windowWidth;
////            zoomLevel = ((int) Math.round(ratio * 10.0));
////
////        }
////
////        zoomLevel = Math.max(zoomLevel, 4);
////        zoomLevel = Math.min(zoomLevel, 20);
////        System.out.println("zoom level is   " + zoomLevel + "   " + ratio + "    ");
//
//        String cssData = ".zoomapp{zoom:" + ratio + " ; }";
//        this.getPage().getStyles().add(cssData);
//        lastResizedHeight = swindowHeight;
//        lastResizedWidth = swindowWidth;
////        layout.removeStyleName(updatedZoomStyleName);
////        updatedZoomStyleName = cssData;
//
//    }

//}
