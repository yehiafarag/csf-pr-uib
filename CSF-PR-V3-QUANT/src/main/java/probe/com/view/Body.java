package probe.com.view;

import probe.com.view.body.AdminLayout;
import probe.com.view.body.UpdatedProjectsLayout;
import probe.com.view.body.ProjectsLayout;
import probe.com.view.body.SearchLayout_t;
import probe.com.view.body.WelcomeLayout;
import probe.com.view.body.QuantDatasetsOverviewLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;
import java.io.Serializable;
import probe.com.handlers.MainHandler;
import probe.com.view.body.QuantDatasetSearchingLayout;

/**
 *
 * @author Yehia Farag the main tables and charts layout
 */
public class Body extends VerticalLayout implements TabSheet.SelectedTabChangeListener, Button.ClickListener, Serializable {

    private final Button adminIcon;
    private TabSheet.Tab homeTab, adminTab;//tabs for Experiments Editor,Proteins, Search
    private final TabSheet mainTabSheet;//tab sheet for first menu (Experiments Editor,Proteins, Search)
    private VerticalLayout searchLayout, adminLayout, updatedProjectsLayout;
    private WelcomeLayout welcomeLayout;
    private ProjectsLayout projectsLayoutComponent;
    private QuantDatasetsOverviewLayout datasetOverviewTabLayout;
    private final VerticalLayout datasetsOverviewLayout;
    private final MainHandler handler;

    /**
     * initialize body layout
     *
     * @param handler main dataset handler
     *
     *
     */
    public Body(MainHandler handler) {
        this.setWidth("100%");
        this.handler = handler;

        mainTabSheet = new TabSheet();
        this.addComponent(mainTabSheet);
        mainTabSheet.setHeight("100%");
        mainTabSheet.setWidth("100%");

        adminIcon = this.initAdminIcoBtn();

//        Tab 3 content
        datasetsOverviewLayout = new VerticalLayout();
        datasetsOverviewLayout.setMargin(true);
        datasetsOverviewLayout.setWidth("100%");

        initBodyLayout();
    }

    /**
     * initialize body components layout
     */
    private void initBodyLayout() {
//        Tab 1 content
        welcomeLayout = new WelcomeLayout(adminIcon);
        welcomeLayout.setWidth("100%");
//        Tab 2 content        
        searchLayout = new VerticalLayout();
        searchLayout.setMargin(true);
        
        
        QuantDatasetSearchingLayout searchingLayout = new QuantDatasetSearchingLayout(handler,mainTabSheet);
         this.searchLayout.addComponent(searchingLayout);
         
//        SearchLayout_t searchLayoutComponent = new SearchLayout_t(handler);
//        this.searchLayout.addComponent(searchLayoutComponent);
       

//           
        VerticalLayout studiesLayout2 = new VerticalLayout();
        studiesLayout2.setMargin(true);
//         studiesLayoutComponent2 = new StudiesExploringLayout2(handler);
//        studiesLayout2.addComponent(studiesLayoutComponent2);

//                    Tab 4 content
        VerticalLayout disaseRelatedProteinDisplayLayout = new VerticalLayout();
        disaseRelatedProteinDisplayLayout.setMargin(true);
       

//        to be removed
        projectsLayoutComponent = new ProjectsLayout(handler);

//        Tap 5 contetent (updated projects)
        updatedProjectsLayout = new VerticalLayout();
        updatedProjectsLayout.setMargin(true);
        updatedProjectsLayout.setHeight("100%");
        updatedProjectsLayout.addComponent(new UpdatedProjectsLayout(handler, mainTabSheet));//(handler));
        
        
         homeTab = mainTabSheet.addTab(welcomeLayout, "Home", null);
        

//      Tab 6 login form
        adminLayout = new VerticalLayout();
        adminLayout.setMargin(true);
        adminLayout.setHeight("100%");
        adminLayout.addComponent(new AdminLayout(handler));

      
        
        mainTabSheet.addTab(datasetsOverviewLayout, "Quantitative Datasets Overview");

//        mainTabSheet.addTab(disaseRelatedProteinDisplayLayout, "Disase Reltated Proteins Display");
        adminTab = mainTabSheet.addTab(adminLayout, "Dataset Editor (Require Sign In)", null);

        //tobe removed 
        mainTabSheet.addTab(this.updatedProjectsLayout, "Identification Datasets Overview");
//        mainTabSheet.addTab(projectsLayoutComponent, "Projects", null);
//       mainTabSheet.addTab(studiesLayout2, "Temp Overview of included studies");
        
        VerticalLayout testLayout =new VerticalLayout();
           testLayout.setMargin(true);
        testLayout.setHeight("100%");
        
//        StudiesChartsLayout testChart = new StudiesChartsLayout();
//        testLayout.addComponent(testChart);
//          mainTabSheet.addTab(testLayout, "Test");
        
         
        mainTabSheet.addTab(this.searchLayout, "Search");
        
        
        mainTabSheet.addSelectedTabChangeListener(this);
        mainTabSheet.setSelectedTab(homeTab);
        mainTabSheet.markAsDirty();
        adminTab.setVisible(false);

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
            if (datasetOverviewTabLayout == null) //            datasetsOverviewLayout.removeAllComponents();
            {
                datasetOverviewTabLayout = new QuantDatasetsOverviewLayout(handler,false);
                datasetsOverviewLayout.addComponent(datasetOverviewTabLayout);
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
