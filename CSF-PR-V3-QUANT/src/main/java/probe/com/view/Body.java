package probe.com.view;

import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import probe.com.view.body.AdminLayout;
import probe.com.view.body.IdentificationDatasetsLayout;
import probe.com.view.body.WelcomeLayout;
import probe.com.view.body.QuantDatasetsOverviewLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;
import eu.dusse.vaadin.waypoints.InviewExtension;
import eu.dusse.vaadin.waypoints.InviewExtensionImpl;
import java.io.Serializable;
import probe.com.handlers.CSFPRHandler;
import probe.com.view.body.ProteinsSearchingLayout;
import probe.com.view.body.QuantCompareDataLayout;
import probe.com.view.core.chart4j.VennDiagramContainer;

/**
 *
 * @author Yehia Farag
 *
 * the main body layout of csf-pr web application
 */
public class Body extends VerticalLayout implements TabSheet.SelectedTabChangeListener, Serializable {

    private final Button adminIcon;
    private TabSheet.Tab homeTab, adminTab;//tabs for Experiments Editor,Proteins, Search
    private final TabSheet mainTabSheet;//tab sheet for first menu (Experiments Editor,Proteins, Search)
    private VerticalLayout adminLayout;
    private WelcomeLayout welcomeLayout;
    private Panel quantDatasetsOverviewPanel, identificationDatasetsOverviewPanel, compareUserDataPanel, searchDataPanel;
    private QuantDatasetsOverviewLayout datasetOverviewTabLayout;
    private final CSFPRHandler CSFPR_Handler;
    private IdentificationDatasetsLayout identificationDatasetsTabLayout;
    private ProteinsSearchingLayout searchingLayout;
    private QuantCompareDataLayout quantCompareDataTabLayout;

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
        welcomeLayout.setHeight("100%");
        homeTab = mainTabSheet.addTab(welcomeLayout, "Home", null);

//      Tab 2 content quant dataset overview
        quantDatasetsOverviewPanel = new Panel();

        pageHeight = Page.getCurrent().getBrowserWindowHeight();
        bodyHeight = (pageHeight - 100);

        quantDatasetsOverviewPanel.setHeight(bodyHeight + "px");
        quantDatasetsOverviewPanel.setWidth("100%");
        mainTabSheet.addTab(quantDatasetsOverviewPanel, "Quantitative Datasets");

//      Tab 3 content       identificationDatasetsOverviewPanel    
        identificationDatasetsOverviewPanel = new Panel();
        identificationDatasetsOverviewPanel.setHeight(bodyHeight + "px");
        identificationDatasetsOverviewPanel.setWidth("100%");
        mainTabSheet.addTab(this.identificationDatasetsOverviewPanel, "Identification Datasets");

//      Tab 4 content  searching proteins tab 
        searchDataPanel = new Panel();
        searchDataPanel.setHeight(bodyHeight + "px");
        TabSheet.Tab serchingTab = mainTabSheet.addTab(this.searchDataPanel, "Search");

        compareUserDataPanel = new Panel();
        compareUserDataPanel.setHeight(bodyHeight + "px");
        compareUserDataPanel.setWidth("100%");
        mainTabSheet.addTab(compareUserDataPanel, "Compare");

//      Tab 5content hidden tab (login form)
        adminLayout = new VerticalLayout();
        adminLayout.setMargin(true);
        adminLayout.setHeight("100%");
        adminLayout.setHeight(bodyHeight + "px");
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

        //        int width = Page.getCurrent().getBrowserWindowWidth()-15;
        VerticalLayout test = new VerticalLayout();
        test.setHeight(bodyHeight + "px");
        test.setWidth("100%");
        test.setPrimaryStyleName("scrollable");
        VennDiagramContainer vennDiagram = new VennDiagramContainer();
        test.addComponent(vennDiagram);
        mainTabSheet.addTab(test, "Test");

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
                this.searchDataPanel.setContent(searchingLayout);
                searchingLayout.setHeightUndefined();

                boolean horizontal = false;
                InviewExtension extension = new InviewExtensionImpl(searchingLayout.getTopLabelMarker(), quantDatasetsOverviewPanel, horizontal);
                extension.addExitListener(new InviewExtension.ExitListener() {
                    @Override
                    public void onExit(InviewExtension.ExitEvent event) {
                        if ((sizeControl + 100) <= (pageHeight)) {
                            return;
                        }
                        CSFPR_Handler.controlHeaderHeights(0);
                        searchDataPanel.setHeight((bodyHeight + 60) + "px");
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
                        if ((sizeControl + 100) <= (pageHeight)) {
                            return;
                        }
                        CSFPR_Handler.controlHeaderHeights(60);
                        searchDataPanel.setHeight((bodyHeight) + "px");
                    }
                });
                extension.addEnterListener(new InviewExtension.EnterListener() {

                    @Override
                    public void onEnter(InviewExtension.EnterEvent event) {
                        if ((sizeControl + 100) <= (pageHeight)) {
                            return;
                        }
                        CSFPR_Handler.controlHeaderHeights(60);
                        searchDataPanel.setHeight((bodyHeight) + "px");
                    }
                });
                extension.addExitedListener(new InviewExtension.ExitedListener() {

                    @Override
                    public void onExited(InviewExtension.ExitedEvent event) {
                        if ((sizeControl + 100) <= (pageHeight)) {
                            return;
                        }
                        CSFPR_Handler.controlHeaderHeights(0);
                        searchDataPanel.setHeight((bodyHeight + 60) + "px");
                    }
                });
            }
            CSFPR_Handler.controlHeaderHeights(60);
            searchDataPanel.setHeight((bodyHeight) + "px");

            CSFPR_Handler.setZoomedLayout(searchingLayout);

        } else if (c.equals("Quantitative Datasets")) {
            adminTab.setVisible(false);

            if (datasetOverviewTabLayout == null && CSFPR_Handler != null) {

                datasetOverviewTabLayout = new QuantDatasetsOverviewLayout(CSFPR_Handler, false, null);
                quantDatasetsOverviewPanel.setContent(datasetOverviewTabLayout);

                boolean horizontal = false;
                InviewExtension extension = new InviewExtensionImpl(datasetOverviewTabLayout.getTopLabelMarker(), quantDatasetsOverviewPanel, horizontal);
                extension.addExitListener(new InviewExtension.ExitListener() {
                    @Override
                    public void onExit(InviewExtension.ExitEvent event) {
                        if ((sizeControl + 100) <= (pageHeight)) {
                            return;
                        }
                        CSFPR_Handler.controlHeaderHeights(0);
                        quantDatasetsOverviewPanel.setHeight((bodyHeight + 60) + "px");
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
                        quantDatasetsOverviewPanel.setHeight((bodyHeight) + "px");
                    }
                });
                extension.addEnterListener(new InviewExtension.EnterListener() {

                    @Override
                    public void onEnter(InviewExtension.EnterEvent event) {
                        CSFPR_Handler.controlHeaderHeights(60);
                        quantDatasetsOverviewPanel.setHeight((bodyHeight) + "px");
                    }
                });
                extension.addExitedListener(new InviewExtension.ExitedListener() {

                    @Override
                    public void onExited(InviewExtension.ExitedEvent event) {
                        CSFPR_Handler.controlHeaderHeights(0);
                        quantDatasetsOverviewPanel.setHeight((bodyHeight + 60) + "px");
                    }
                });

            }
            CSFPR_Handler.setZoomedLayout(datasetOverviewTabLayout);
            CSFPR_Handler.controlHeaderHeights(60);
            quantDatasetsOverviewPanel.setHeight((bodyHeight) + "px");

        } else if (c.equals("Identification Datasets")) {
            adminTab.setVisible(false);

            sizeControl = 0;
            if (identificationDatasetsTabLayout == null) {
                identificationDatasetsTabLayout = new IdentificationDatasetsLayout(CSFPR_Handler, mainTabSheet);
                identificationDatasetsOverviewPanel.setContent(identificationDatasetsTabLayout);

//                boolean horizontal = false;
//                InviewExtension extension = new InviewExtensionImpl(identificationDatasetsTabLayout.getTopLabelMarker(), identificationDatasetsOverviewPanel, horizontal);
//                extension.addExitListener(new InviewExtension.ExitListener() {
//                    @Override
//                    public void onExit(InviewExtension.ExitEvent event) {
//                        if ((sizeControl + 60) <= (pageHeight)) {
//                            return;
//                        }
//                        CSFPR_Handler.controlHeaderHeights(0);
//                        identificationDatasetsOverviewPanel.setHeight((bodyHeight + 60) + "px");
//                    }
//                });
//                extension.addEnteredListener(new InviewExtension.EnteredListener() {
//                    private boolean init = true;
//
//                    @Override
//                    public void onEntered(InviewExtension.EnteredEvent event) {
//                        if (init) {
//                            init = false;
//                            return;
//                        }
//                          if ((sizeControl + 60) <= (pageHeight)) {
//                            return;
//                        }
//                        CSFPR_Handler.controlHeaderHeights(60);
//                        identificationDatasetsOverviewPanel.setHeight((bodyHeight) + "px");
//                    }
//                });
//                extension.addEnterListener(new InviewExtension.EnterListener() {
//
//                    @Override
//                    public void onEnter(InviewExtension.EnterEvent event) {
//                          if ((sizeControl + 60) <= (pageHeight)) {
//                            return;
//                        }
//                        CSFPR_Handler.controlHeaderHeights(60);
//                        identificationDatasetsOverviewPanel.setHeight((bodyHeight) + "px");
//                    }
//                });
//                extension.addExitedListener(new InviewExtension.ExitedListener() {
//
//                    @Override
//                    public void onExited(InviewExtension.ExitedEvent event) {
//                          if ((sizeControl + 60) <= (pageHeight)) {
//                            return;
//                        }
//                        CSFPR_Handler.controlHeaderHeights(0);
//                        identificationDatasetsOverviewPanel.setHeight((bodyHeight + 60) + "px");
//                    }
//                });
//
            }
            CSFPR_Handler.controlHeaderHeights(60);
            identificationDatasetsOverviewPanel.setHeight((bodyHeight) + "px");
            CSFPR_Handler.setZoomedLayout(identificationDatasetsTabLayout);

        } else if (c.equals("Compare")) {
            adminTab.setVisible(false);

            if (quantCompareDataTabLayout == null) {
                quantCompareDataTabLayout = new QuantCompareDataLayout(CSFPR_Handler);
                compareUserDataPanel.setContent(quantCompareDataTabLayout);

                boolean horizontal = false;
                InviewExtension extension = new InviewExtensionImpl(quantCompareDataTabLayout.getTopLabelMarker(), compareUserDataPanel, horizontal);
                extension.addExitListener(new InviewExtension.ExitListener() {
                    @Override
                    public void onExit(InviewExtension.ExitEvent event) {
//                        System.out.println("at on exeted and gone "+event.getDirection()+"----- "+quantDatasetsOverviewPanel.getScrollTop());
                        if ((sizeControl + 60) <= (pageHeight)) {
                            return;
                        }
                        CSFPR_Handler.controlHeaderHeights(0);
                        compareUserDataPanel.setHeight((bodyHeight + 60) + "px");
                    }
                });
                extension.addEnteredListener(new InviewExtension.EnteredListener() {
                    private boolean init = true;

                    @Override
                    public void onEntered(InviewExtension.EnteredEvent event) {
//                        System.out.println("at on entered --- already----- "+quantDatasetsOverviewPanel.getScrollTop());
                        if (init) {
                            init = false;
                            return;
                        }
                        CSFPR_Handler.controlHeaderHeights(60);
                        compareUserDataPanel.setHeight((bodyHeight) + "px");
                    }
                });
                extension.addEnterListener(new InviewExtension.EnterListener() {

                    @Override
                    public void onEnter(InviewExtension.EnterEvent event) {
                        CSFPR_Handler.controlHeaderHeights(60);
                        compareUserDataPanel.setHeight((bodyHeight) + "px");
//                        System.out.println("at on enter just ----- "+quantDatasetsOverviewPanel.getScrollTop());
                    }
                });
                extension.addExitedListener(new InviewExtension.ExitedListener() {

                    @Override
                    public void onExited(InviewExtension.ExitedEvent event) {
//                        System.out.println("at on exeted and gone----- "+quantDatasetsOverviewPanel.getScrollTop());
                        CSFPR_Handler.controlHeaderHeights(0);
                        compareUserDataPanel.setHeight((bodyHeight + 60) + "px");
                    }
                });

            }
            CSFPR_Handler.controlHeaderHeights(60);
            compareUserDataPanel.setHeight((bodyHeight) + "px");
            CSFPR_Handler.setZoomedLayout(quantCompareDataTabLayout);

        } else {
            CSFPR_Handler.controlHeaderHeights(60);
            CSFPR_Handler.setZoomedLayout(null);
        }

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
