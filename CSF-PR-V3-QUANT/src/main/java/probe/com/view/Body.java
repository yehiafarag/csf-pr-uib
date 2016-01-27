package probe.com.view;

import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import probe.com.view.body.AdminLayout;
import probe.com.view.body.IdentificationDatasetsLayout;
import probe.com.view.body.WelcomeLayout;
import probe.com.view.body.QuantDatasetsOverviewLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.Runo;
import eu.dusse.vaadin.waypoints.InviewExtension;
import eu.dusse.vaadin.waypoints.InviewExtensionImpl;
import java.io.Serializable;
import probe.com.handlers.CSFPRHandler;
import probe.com.view.body.ProteinsSearchingLayout;
import probe.com.view.body.QuantCompareDataLayout;

/**
 *
 * @author Yehia Farag
 *
 * the main body layout of csf-pr web application
 */
public class Body extends VerticalLayout implements TabSheet.SelectedTabChangeListener, Button.ClickListener, Serializable {

    private final Button adminIcon;
    private TabSheet.Tab homeTab, adminTab;//tabs for Experiments Editor,Proteins, Search
    private final TabSheet mainTabSheet;//tab sheet for first menu (Experiments Editor,Proteins, Search)
    private VerticalLayout searchTabLayout, adminLayout, identificationDatasetsLayout, datasetsOverviewLayout, compareLayout;
    private WelcomeLayout welcomeLayout;
    private QuantDatasetsOverviewLayout datasetOverviewTabLayout;
    private final CSFPRHandler handler;
    private IdentificationDatasetsLayout identificationDatasetsTabLayout;

    /**
     * initialize body layout
     *
     * @param handler main dataset handler
     *
     *
     */
    public Body(CSFPRHandler handler) {
        this.setWidth("100%");
        this.handler = handler;
        mainTabSheet = new TabSheet();
        this.addComponent(mainTabSheet);
        mainTabSheet.setHeight("100%");
        mainTabSheet.setWidth("100%");
        adminIcon = this.initAdminIcoBtn();
        initBodyLayout(handler);

//        ProcessBuilder pb = new ProcessBuilder("C:\\Program Files\\Java\\jdk1.8.0_45\\bin\\java.exe", "-jar", "CSFPR.Utility-0.2.jar");
//        pb.directory(new File("D:\\csf-pr-runing"));
//        try {
//            Process p = pb.start();
//        } catch (IOException ex) {
//            System.out.println("error "+ex.getMessage());
//        }
    }

    /**
     * initialize body components layout
     */
    private void initBodyLayout(CSFPRHandler CSFPR_Handler) {
//      Tab 1 content home page 
        welcomeLayout = new WelcomeLayout(adminIcon, CSFPR_Handler);
        welcomeLayout.setWidth("100%");
        homeTab = mainTabSheet.addTab(welcomeLayout, "Home", null);

//      Tab 2 content quant dataset overview
        datasetsOverviewLayout = new VerticalLayout();
        datasetsOverviewLayout.setMargin(true);

        int height = Page.getCurrent().getBrowserWindowHeight() - 100;
//        int width = Page.getCurrent().getBrowserWindowWidth()-15;
        datasetsOverviewLayout.setHeight(height + "px");
        datasetsOverviewLayout.setWidth("100%");
        datasetsOverviewLayout.setPrimaryStyleName("scrollable");
        mainTabSheet.addTab(datasetsOverviewLayout, "Quantitative Studies");

//      Tab 3 content       identificationDatasetsLayout    
        identificationDatasetsLayout = new VerticalLayout();
        identificationDatasetsLayout.setMargin(true);
//        identificationDatasetsLayout.setHeight("100%");
        identificationDatasetsLayout.setHeight(height + "px");
//        identificationDatasetsLayout.setPrimaryStyleName("scrollable");
        mainTabSheet.addTab(this.identificationDatasetsLayout, "Identification Datasets");

//      Tab 4 content  searching proteins tab 
        searchTabLayout = new VerticalLayout();
        searchTabLayout.setMargin(true);
//        searchTabLayout.setPrimaryStyleName("scrollable");

        ProteinsSearchingLayout searchingLayout = new ProteinsSearchingLayout(CSFPR_Handler, mainTabSheet);
        this.searchTabLayout.addComponent(searchingLayout);
        searchTabLayout.setHeight(height + "px");
        TabSheet.Tab serchingTab = mainTabSheet.addTab(this.searchTabLayout, "Search");

        compareLayout = new VerticalLayout();
        compareLayout.setMargin(true);
        compareLayout.setHeight(height + "px");
        compareLayout.setWidth("100%");
        compareLayout.setPrimaryStyleName("scrollable");
        mainTabSheet.addTab(compareLayout, "Compare");

        QuantCompareDataLayout quantCompareDataLayout = new QuantCompareDataLayout(CSFPR_Handler);
        compareLayout.addComponent(quantCompareDataLayout);
        compareLayout.setComponentAlignment(quantCompareDataLayout, Alignment.TOP_CENTER);

//      Tab 5content hidden tab (login form)
        adminLayout = new VerticalLayout();
        adminLayout.setMargin(true);
        adminLayout.setHeight("100%");
        adminLayout.setHeight(height + "px");
//        adminLayout.setPrimaryStyleName("scrollable");
        adminLayout.addComponent(new AdminLayout(CSFPR_Handler));
        adminTab = mainTabSheet.addTab(adminLayout, "Dataset Editor (Require Sign In)", null);

        mainTabSheet.addSelectedTabChangeListener(this);

        adminTab.setVisible(false);

        VaadinRequest request = VaadinService.getCurrentRequest();
        if (!request.getPathInfo().trim().endsWith("/")) {
            mainTabSheet.setSelectedTab(serchingTab);
        } else {
            mainTabSheet.setSelectedTab(homeTab);
        }
        mainTabSheet.markAsDirty();

    }

    @Override
    public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
        String c = mainTabSheet.getTab(event.getTabSheet().getSelectedTab()).getCaption();
        if (c.equals("Dataset Editor (Require Sign In)")) {
            adminTab.setVisible(false);
        } else if (c.equals("Proteins")) {
            adminTab.setVisible(false);
        } else if (c.equals("Search")) {
            adminTab.setVisible(false);
        } else if (c.equals("Quantitative Studies")) {
            adminTab.setVisible(false);
            if (datasetOverviewTabLayout == null && handler != null) {
              

                  
                datasetOverviewTabLayout = new QuantDatasetsOverviewLayout(handler, false, null);
                datasetsOverviewLayout.addComponent(datasetOverviewTabLayout);

                boolean horizontal = false;
                InviewExtension extension = new InviewExtensionImpl(datasetOverviewTabLayout, datasetsOverviewLayout, horizontal);
                extension.addEnterListener(new InviewExtension.EnterListener() {
                    @Override
                    public void onEnter(InviewExtension.EnterEvent event) {
                        System.out.println("at scroll in " + event.getDirection().getDirection());
                        // is fired when You scroll into 'yourComponent'
                    }
                    
                });  
                 extension.addExitListener(new InviewExtension.ExitListener() {

                    @Override
                    public void onExit(InviewExtension.ExitEvent event) {
                         System.out.println("at scroll out  " + event.getDirection().getDirection());
                    }
                });
                extension.addEnteredListener(new InviewExtension.EnteredListener() {

                    @Override
                    public void onEntered(InviewExtension.EnteredEvent event) {
                         System.out.println("at scroll through  " + event.toString());
                    }
                });
                
                
           

            }

        } else if (c.equals("Identification Datasets")) {
            adminTab.setVisible(false);
            if (identificationDatasetsTabLayout == null) {
                identificationDatasetsTabLayout = new IdentificationDatasetsLayout(handler, mainTabSheet);
                identificationDatasetsLayout.addComponent(identificationDatasetsTabLayout);

            }

        }
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        mainTabSheet.markAsDirty();
    }

    private Button initAdminIcoBtn() {
        Button b = new Button("(Admin Login)");
        b.setStyleName(Runo.BUTTON_LINK);
        b.setDescription("Dataset Editor (Require Sign In)");
        Button.ClickListener adminClickListener = new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                adminTab.setVisible(true);
                mainTabSheet.setSelectedTab(adminTab);
                mainTabSheet.markAsDirty();
                adminTab.setCaption("");
            }
        };
        b.addClickListener(adminClickListener);
        return b;
    }

}
