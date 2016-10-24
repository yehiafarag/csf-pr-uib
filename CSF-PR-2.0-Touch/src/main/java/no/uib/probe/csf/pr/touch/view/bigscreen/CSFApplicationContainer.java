package no.uib.probe.csf.pr.touch.view.bigscreen;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import no.uib.probe.csf.pr.touch.Data_Handler;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFPR_Central_Manager;
import no.uib.probe.csf.pr.touch.view.LayoutViewManager;
import no.uib.probe.csf.pr.touch.view.bigscreen.quantlayoutcontainer.QuantDataLayoutContainer;
import no.uib.probe.csf.pr.touch.view.core.ScrollPanel;
import no.uib.probe.csf.pr.touch.view.bigscreen.welcomepagecontainer.WelcomeLayoutComponents;
import no.uib.probe.csf.pr.touch.view.core.BusyTask;
import no.uib.probe.csf.pr.touch.view.core.SlidePanel;

/**
 *
 * @author Yehia Farag
 *
 * this class represents the welcome page HTML template the class includes
 * buttons for starting the main functions
 *
 */
public class CSFApplicationContainer extends VerticalLayout {

    /*
     *The welcome page container panel
     */
    private final ScrollPanel welcomeLayoutPanel;

    /*
     *The quantative overview layout container panel
     */
    private final SlidePanel quantLayoutPanel;

    /*
     *The layout manager to control the different visulizations 
     */
    private final LayoutViewManager View_Manager;

    /*
     *The quantative data handler to work as controller layer to interact between visulization and logic layer 
     */
    private final Data_Handler Data_handler;

    /**
     * Constructor the initialize the main attributes (Database connection data
     * and layout width and heights)
     *
     * @param pageWidth
     * @param pageHeight
     * @param url
     * @param dbName
     * @param driver
     * @param userName
     * @param password
     * @param filesURL
     */
    public CSFApplicationContainer(int pageWidth, int pageHeight, String url, String dbName, String driver, String userName, String password, String filesURL) {
        this.setWidth(pageWidth, Unit.PIXELS);
        this.setHeight(pageHeight, Unit.PIXELS);
        this.setStyleName("whitelayout");
        this.setSpacing(false);

        BusyTask busyTask = new BusyTask();
        this.View_Manager = new LayoutViewManager(busyTask);
        this.Data_handler = new Data_Handler(url, dbName, driver, userName, password, filesURL);
        CSFPR_Central_Manager CSFPR_Central_Manager = new CSFPR_Central_Manager(busyTask);

        int mainlayoutWidth = pageWidth;
        int mainlayoutHeight = pageHeight;

        VerticalLayout bodyWrapper = new VerticalLayout();
        bodyWrapper.setHeightUndefined();
        bodyWrapper.setWidth(100, Unit.PERCENTAGE);
        this.addComponent(bodyWrapper);

        WelcomeLayoutComponents welcomeContent = new WelcomeLayoutComponents(Data_handler, CSFPR_Central_Manager, View_Manager, mainlayoutWidth, mainlayoutHeight, Data_handler.getResourceOverviewInformation(), Data_handler.getPublicationList(), Data_handler.getQuantDatasetList());

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setHeight(pageHeight, Unit.PIXELS);
        mainLayout.setWidth(pageWidth, Unit.PIXELS);
        mainLayout.setStyleName("whitelayout");
        mainLayout.addComponent(welcomeContent);
        mainLayout.setComponentAlignment(welcomeContent, Alignment.TOP_CENTER);

        this.welcomeLayoutPanel = new ScrollPanel(mainLayout, welcomeContent.getTopRightThumbBtnsLayoutContainer(), 0, "welcomeview");
        View_Manager.registerComponent(welcomeLayoutPanel);
        bodyWrapper.addComponent(welcomeLayoutPanel);
        bodyWrapper.setComponentAlignment(welcomeLayoutPanel, Alignment.TOP_RIGHT);

        QuantDataLayoutContainer quantLayout = new QuantDataLayoutContainer(Data_handler, CSFPR_Central_Manager, mainlayoutWidth, mainlayoutHeight);

        quantLayoutPanel = new SlidePanel(quantLayout, null, 1, "quantview") {

            @Override
            public void setVisible(boolean visible) {
                super.setVisible(visible);
                if (this.getParent() != null) {
                    this.getParent().setVisible(visible);
                }
            }

        };
        quantLayoutPanel.setHeight(mainlayoutHeight, Unit.PIXELS);
        View_Manager.registerComponent(quantLayoutPanel);

        quantLayoutPanel.setShowNavigationBtn(false);

        Panel quantPanelContaner = new Panel(quantLayoutPanel);// 
        quantLayoutPanel.setVisible(false);
        quantPanelContaner.setWidth(mainlayoutWidth, Unit.PIXELS);
        quantPanelContaner.setHeight(pageHeight, Unit.PIXELS);
        quantPanelContaner.setStyleName(ValoTheme.PANEL_BORDERLESS);

        bodyWrapper.addComponent(quantPanelContaner);
        bodyWrapper.setComponentAlignment(quantPanelContaner, Alignment.TOP_LEFT);

        View_Manager.viewLayout("welcomeview");

    }

}
