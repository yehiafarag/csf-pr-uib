package probe.com.view;

import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import probe.com.view.body.AdminLayout;
import probe.com.view.body.IdentificationDatasetsLayout;
import probe.com.view.body.WelcomeLayout;
import probe.com.view.body.QuantDatasetsOverviewLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;
import java.io.Serializable;
import probe.com.handlers.CSFPRHandler;
import probe.com.view.body.ProteinsSearchingLayout;

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
    private VerticalLayout searchTabLayout, adminLayout, identificationDatasetsLayout, datasetsOverviewLayout;
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
    }

    /**
     * initialize body components layout
     */
    private void initBodyLayout(CSFPRHandler handler) {
//      Tab 1 content home page 
        welcomeLayout = new WelcomeLayout(adminIcon, handler);
        welcomeLayout.setWidth("100%");
        homeTab = mainTabSheet.addTab(welcomeLayout, "Home", null);

//      Tab 2 content quant dataset overview
        datasetsOverviewLayout = new VerticalLayout();
        datasetsOverviewLayout.setMargin(true);
        
        int height = Page.getCurrent().getBrowserWindowHeight()-100;
//        int width = Page.getCurrent().getBrowserWindowWidth()-15;
        datasetsOverviewLayout.setHeight(height+"px");
        datasetsOverviewLayout.setWidth("100%");
        datasetsOverviewLayout.setPrimaryStyleName("scrollable");
        mainTabSheet.addTab(datasetsOverviewLayout, "Quantitative Datasets Overview");

//      Tab 3 content       identificationDatasetsLayout    
        identificationDatasetsLayout = new VerticalLayout();
        identificationDatasetsLayout.setMargin(true);
        identificationDatasetsLayout.setHeight("100%");
        mainTabSheet.addTab(this.identificationDatasetsLayout, "Identification Datasets Overview");

//      Tab 4 content  searching proteins tab 
        searchTabLayout = new VerticalLayout();
        searchTabLayout.setMargin(true);
        ProteinsSearchingLayout searchingLayout = new ProteinsSearchingLayout(handler, mainTabSheet);
        this.searchTabLayout.addComponent(searchingLayout);
        TabSheet.Tab serchingTab = mainTabSheet.addTab(this.searchTabLayout, "Search");

//      Tab 5content hidden tab (login form)
        adminLayout = new VerticalLayout();
        adminLayout.setMargin(true);
        adminLayout.setHeight("100%");
        adminLayout.addComponent(new AdminLayout(handler));
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
        } else if (c.equals("Quantitative Datasets Overview")) {
            adminTab.setVisible(false);
            if (datasetOverviewTabLayout == null && handler != null) {
                datasetOverviewTabLayout = new QuantDatasetsOverviewLayout(handler, false, null);
                datasetsOverviewLayout.addComponent(datasetOverviewTabLayout);
            }

        } else if (c.equals("Identification Datasets Overview")) {
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
