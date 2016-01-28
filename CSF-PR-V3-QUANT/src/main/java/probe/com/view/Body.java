package probe.com.view;

import com.ejt.vaadin.sizereporter.ComponentResizeEvent;
import com.ejt.vaadin.sizereporter.ComponentResizeListener;
import com.ejt.vaadin.sizereporter.SizeReporter;
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
import com.vaadin.ui.UI;
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
    private final CSFPRHandler CSFPR_Handler;
    private IdentificationDatasetsLayout identificationDatasetsTabLayout;
    private ProteinsSearchingLayout searchingLayout;
    private  QuantCompareDataLayout quantCompareDataTabLayout ;

    /**
     * initialize body layout
     *
     * @param CSFPR_Handler main dataset CSFPR_Handler
     *
     *
     */
    public Body(CSFPRHandler CSFPR_Handler) {
        this.setWidth("100%");
        this.CSFPR_Handler = CSFPR_Handler;
        mainTabSheet = new TabSheet();
        this.addComponent(mainTabSheet);
        mainTabSheet.setHeight("100%");
        mainTabSheet.setWidth("100%");
        adminIcon = this.initAdminIcoBtn();
        initBodyLayout(CSFPR_Handler);

//        ProcessBuilder pb = new ProcessBuilder("C:\\Program Files\\Java\\jdk1.8.0_45\\bin\\java.exe", "-jar", "CSFPR.Utility-0.2.jar");
//        pb.directory(new File("D:\\csf-pr-runing"));
//        try {
//            Process p = pb.start();
//        } catch (IOException ex) {
//            System.out.println("error "+ex.getMessage());
//        }
    }
    private int bodyHeight;
    private int pageHeight;

    /**
     * initialize body components layout
     */
    private void initBodyLayout(final CSFPRHandler CSFPR_Handler) {
//      Tab 1 content home page 
        welcomeLayout = new WelcomeLayout(adminIcon, CSFPR_Handler);
        welcomeLayout.setWidth("100%");
        homeTab = mainTabSheet.addTab(welcomeLayout, "Home", null);

//      Tab 2 content quant dataset overview
        datasetsOverviewLayout = new VerticalLayout();
        datasetsOverviewLayout.setMargin(true);

        pageHeight = Page.getCurrent().getBrowserWindowHeight();
        bodyHeight = (pageHeight - 100);
//        
//        int width = Page.getCurrent().getBrowserWindowWidth()-15;
        datasetsOverviewLayout.setHeight(bodyHeight + "px");
        datasetsOverviewLayout.setWidth("100%");
        datasetsOverviewLayout.setPrimaryStyleName("scrollable");
        mainTabSheet.addTab(datasetsOverviewLayout, "Quantitative Studies");

//      Tab 3 content       identificationDatasetsLayout    
        identificationDatasetsLayout = new VerticalLayout();
        identificationDatasetsLayout.setMargin(true);
//        identificationDatasetsLayout.setHeight("100%");
        identificationDatasetsLayout.setHeight(bodyHeight + "px");
        identificationDatasetsLayout.setPrimaryStyleName("scrollable");
        mainTabSheet.addTab(this.identificationDatasetsLayout, "Identification Datasets");

//      Tab 4 content  searching proteins tab 
        searchTabLayout = new VerticalLayout();
        searchTabLayout.setMargin(true);
        searchTabLayout.setHeight(bodyHeight + "px");
        searchTabLayout.setPrimaryStyleName("scrollable");
        TabSheet.Tab serchingTab = mainTabSheet.addTab(this.searchTabLayout, "Search");

        compareLayout = new VerticalLayout();
        compareLayout.setMargin(true);
        compareLayout.setHeight(bodyHeight + "px");
        compareLayout.setWidth("100%");
        compareLayout.setPrimaryStyleName("scrollable");
        mainTabSheet.addTab(compareLayout, "Compare");

//      Tab 5content hidden tab (login form)
        adminLayout = new VerticalLayout();
        adminLayout.setMargin(true);
        adminLayout.setHeight("100%");
        adminLayout.setHeight(bodyHeight + "px");
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

    private int sizeControl = 0;

    @Override
    public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {

        String c = mainTabSheet.getTab(event.getTabSheet().getSelectedTab()).getCaption();
        if (c.equals("Dataset Editor (Require Sign In)")) {
            adminTab.setVisible(false);
            sizeControl = 0;
        } else if (c.equals("Proteins")) {
            adminTab.setVisible(false);
        } else if (c.equals("Search")) {
            adminTab.setVisible(false);
            if (searchingLayout == null && CSFPR_Handler != null) {
                searchingLayout = new ProteinsSearchingLayout(CSFPR_Handler, mainTabSheet);
                this.searchTabLayout.addComponent(searchingLayout);
                searchingLayout.setHeightUndefined();

                final SizeReporter reporter = new SizeReporter(searchTabLayout);
                reporter.addResizeListener(new ComponentResizeListener() {
                    @Override
                    public void sizeChanged(ComponentResizeEvent event) {
                        System.out.println("at size changed");
                        sizeControl = event.getHeight();
                        if ((sizeControl + 60) <= (pageHeight)) {
                            return;
                        }
                        CSFPR_Handler.controlHeaderHeights(0);
                        searchingLayout.setHeight((bodyHeight + 60) + "px");
                    }
                });
                boolean horizontal = false;
                InviewExtension extension = new InviewExtensionImpl(searchTabLayout, searchingLayout, horizontal);
                extension.addExitListener(new InviewExtension.ExitListener() {
                    @Override
                    public void onExit(InviewExtension.ExitEvent event) {
                        if ((sizeControl + 60) <= (pageHeight)) {
                            return;
                        }
                        CSFPR_Handler.controlHeaderHeights(0);
                        searchingLayout.setHeight((bodyHeight + 60) + "px");
                    }
                });
                extension.addEnteredListener(new InviewExtension.EnteredListener() {
                    private boolean init = true;

                    @Override
                    public void onEntered(InviewExtension.EnteredEvent event) {
                        if (init) {
                            init = false;
                            return;
                        }
                        CSFPR_Handler.controlHeaderHeights(60);
                        searchingLayout.setHeight((bodyHeight) + "px");
                    }
                });
            }

        } else if (c.equals("Quantitative Studies")) {
            adminTab.setVisible(false);
            if (datasetOverviewTabLayout == null && CSFPR_Handler != null) {

                datasetOverviewTabLayout = new QuantDatasetsOverviewLayout(CSFPR_Handler, false, null);
                datasetsOverviewLayout.addComponent(datasetOverviewTabLayout);
                final SizeReporter reporter = new SizeReporter(datasetOverviewTabLayout);
                reporter.addResizeListener(new ComponentResizeListener() {
                    @Override
                    public void sizeChanged(ComponentResizeEvent event) {
                        sizeControl = event.getHeight();
                        if ((sizeControl + 60) <= (pageHeight)) {
                            return;
                        }
                        CSFPR_Handler.controlHeaderHeights(0);
                        datasetsOverviewLayout.setHeight((bodyHeight + 60) + "px");
                    }
                });
                boolean horizontal = false;
                InviewExtension extension = new InviewExtensionImpl(datasetOverviewTabLayout, datasetsOverviewLayout, horizontal);
                extension.addExitListener(new InviewExtension.ExitListener() {
                    @Override
                    public void onExit(InviewExtension.ExitEvent event) {
                        if ((sizeControl + 60) <= (pageHeight)) {
                            return;
                        }
                        CSFPR_Handler.controlHeaderHeights(0);
                        datasetsOverviewLayout.setHeight((bodyHeight + 60) + "px");
                    }
                });
                extension.addEnteredListener(new InviewExtension.EnteredListener() {
                    private boolean init = true;

                    @Override
                    public void onEntered(InviewExtension.EnteredEvent event) {
                        if (init) {
                            init = false;
                            return;
                        }
                        CSFPR_Handler.controlHeaderHeights(60);
                        datasetsOverviewLayout.setHeight((bodyHeight) + "px");
                    }
                });
            }

        } else if (c.equals("Identification Datasets")) {
            adminTab.setVisible(false);
            sizeControl = 0;
            if (identificationDatasetsTabLayout == null) {
                identificationDatasetsTabLayout = new IdentificationDatasetsLayout(CSFPR_Handler, mainTabSheet);
                identificationDatasetsLayout.addComponent(identificationDatasetsTabLayout);
                final SizeReporter reporter = new SizeReporter(identificationDatasetsTabLayout);
                reporter.addResizeListener(new ComponentResizeListener() {
                    @Override
                    public void sizeChanged(ComponentResizeEvent event) {
                        sizeControl = event.getHeight();
                        if ((sizeControl + 60) <= (pageHeight)) {
                            return;
                        }
                        CSFPR_Handler.controlHeaderHeights(0);
                        identificationDatasetsLayout.setHeight((bodyHeight + 60) + "px");
                    }
                });
                boolean horizontal = false;
                InviewExtension extension = new InviewExtensionImpl(identificationDatasetsTabLayout, identificationDatasetsLayout, horizontal);
                extension.addExitListener(new InviewExtension.ExitListener() {
                    @Override
                    public void onExit(InviewExtension.ExitEvent event) {
                        if ((sizeControl + 60) <= (pageHeight)) {
                            return;
                        }
                        CSFPR_Handler.controlHeaderHeights(0);
                        identificationDatasetsLayout.setHeight((bodyHeight + 60) + "px");
                    }
                });
                extension.addEnteredListener(new InviewExtension.EnteredListener() {
                    private boolean init = true;

                    @Override
                    public void onEntered(InviewExtension.EnteredEvent event) {
                        if (init) {
                            init = false;
                            return;
                        }
                        CSFPR_Handler.controlHeaderHeights(60);
                        identificationDatasetsLayout.setHeight((bodyHeight) + "px");
                    }
                });

            }

        } else if (c.equals("Compare")) {
            adminTab.setVisible(false);
            
            if(quantCompareDataTabLayout== null){
            quantCompareDataTabLayout = new QuantCompareDataLayout(CSFPR_Handler);
            compareLayout.addComponent(quantCompareDataTabLayout);
            compareLayout.setComponentAlignment(quantCompareDataTabLayout, Alignment.TOP_CENTER);
            
              final SizeReporter reporter = new SizeReporter(quantCompareDataTabLayout);
                reporter.addResizeListener(new ComponentResizeListener() {
                    @Override
                    public void sizeChanged(ComponentResizeEvent event) {
                        sizeControl = event.getHeight();
                        if ((sizeControl + 60) <= (pageHeight)) {
                            return;
                        }
                        CSFPR_Handler.controlHeaderHeights(0);
                        compareLayout.setHeight((bodyHeight + 60) + "px");
                    }
                });
                boolean horizontal = false;
                InviewExtension extension = new InviewExtensionImpl(quantCompareDataTabLayout, compareLayout, horizontal);
                extension.addExitListener(new InviewExtension.ExitListener() {
                    @Override
                    public void onExit(InviewExtension.ExitEvent event) {
                        if ((sizeControl + 60) <= (pageHeight)) {
                            return;
                        }
                        CSFPR_Handler.controlHeaderHeights(0);
                        compareLayout.setHeight((bodyHeight + 60) + "px");
                    }
                });
                extension.addEnteredListener(new InviewExtension.EnteredListener() {
                    private boolean init = true;

                    @Override
                    public void onEntered(InviewExtension.EnteredEvent event) {
                        if (init) {
                            init = false;
                            return;
                        }
                        CSFPR_Handler.controlHeaderHeights(60);
                        compareLayout.setHeight((bodyHeight) + "px");
                    }
                });
            
            
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
