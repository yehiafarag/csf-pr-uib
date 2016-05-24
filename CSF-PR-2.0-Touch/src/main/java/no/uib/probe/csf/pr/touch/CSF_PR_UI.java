package no.uib.probe.csf.pr.touch;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import javax.servlet.ServletContext;
import no.uib.probe.csf.pr.touch.view.MainLayout;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
@Widgetset("no.uib.probe.csf.pr.touch.CSF_PR_Widgetset")
public class CSF_PR_UI extends UI {

     private String dbURL, dbName, dbDriver, dbUserName, dbPassword, filesURL;
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
        
          this.setWidth(100,Unit.PERCENTAGE);
        this.setHeight(100,Unit.PERCENTAGE);
        
        
        VerticalLayout appWrapper = new VerticalLayout();
        appWrapper.setWidth(100,Unit.PERCENTAGE);
        appWrapper.setHeight(100,Unit.PERCENTAGE);
        appWrapper.setStyleName("bluelayout");
        setContent(appWrapper);
      
        
        final MainLayout layout = new MainLayout(dbURL, dbName, dbDriver, dbUserName, dbPassword, filesURL);
        
        
//        final TextField name = new TextField();
//        name.setCaption("Type your name here:");
//
//        Button button = new Button("Click Me");
//        button.addClickListener( e -> {
//            layout.addComponent(new Label("Thanks " + name.getValue() 
//                    + ", it works!"));
//        });
//        
//        layout.addComponents(name, button);
//        layout.setMargin(true);
//        layout.setSpacing(true);
        
        appWrapper.addComponent(layout);
        appWrapper.setComponentAlignment(layout, Alignment.MIDDLE_CENTER);
//        layout.addStyleName("zoom6");
    }

//    @WebServlet(urlPatterns = "/*", name = "CSF_PR_UIServlet", asyncSupported = true)
//    @VaadinServletConfiguration(ui = CSF_PR_UI.class, productionMode = false)
//    public static class CSF_PR_UIServlet extends VaadinServlet {
//    }
}
