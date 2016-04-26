package no.uib.probe.csf.pr.touch.view;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import no.uib.probe.csf.pr.touch.view.core.SlideLeftPanel;
import no.uib.probe.csf.pr.touch.view.welcomepagecontainer.WelcomeLayoutComponents;

/**
 *
 * @author Yehia Farag
 *
 * this class represents the welcome page HTML template the class includes
 * buttons for starting the main functions
 *
 */
public class WelcomePageContainer extends VerticalLayout {
//
//    private final MenuTestView  wlcomeSideMenu = new MenuTestView ();

    private SlideLeftPanel welcomeLayout ;
    public WelcomePageContainer() {
        this.setWidth("100%");
        this.setHeight("100%");
        this.setSpacing(true);
        this.welcomeLayout = new SlideLeftPanel(100,10, null);
        this.addComponent(welcomeLayout);
        this.setComponentAlignment(welcomeLayout, Alignment.TOP_RIGHT);
        
        WelcomeLayoutComponents welcomeContent = new WelcomeLayoutComponents();
        welcomeLayout.addComponent(welcomeContent);
                
        

    }


}
