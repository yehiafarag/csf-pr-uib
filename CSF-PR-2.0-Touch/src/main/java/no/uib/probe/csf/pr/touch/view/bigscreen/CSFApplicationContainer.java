package no.uib.probe.csf.pr.touch.view.bigscreen;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import no.uib.probe.csf.pr.touch.Data_Handler;
import no.uib.probe.csf.pr.touch.view.LayoutViewManager;
import no.uib.probe.csf.pr.touch.view.bigscreen.quantlayoutcontainer.QuantDataLayoutContainer;
import no.uib.probe.csf.pr.touch.view.core.ScrollPanel;
import no.uib.probe.csf.pr.touch.view.bigscreen.welcomepagecontainer.WelcomeLayoutComponents;
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

    private final ScrollPanel welcomeLayoutPanel;
    private final SlidePanel quantLayoutPanel;
    private final LayoutViewManager View_Manager;
    private final Data_Handler Data_handler;

    public CSFApplicationContainer(int pageWidth, int pageHeight, String url, String dbName, String driver, String userName, String password, String filesURL) {
        this.setWidth(100, Unit.PERCENTAGE);
        this.setHeight(100, Unit.PERCENTAGE);
        this.setSpacing(true);
        this.View_Manager = new LayoutViewManager();
        this.Data_handler = new Data_Handler(url, dbName, driver, userName, password, filesURL);
        int mainlayoutWidth = pageWidth-10 ;
        int mainlayoutHeight = pageHeight - 20;

        VerticalLayout bodyWrapper = new VerticalLayout();
        bodyWrapper.setHeightUndefined();
        bodyWrapper.setWidth(100, Unit.PERCENTAGE);
        this.addComponent(bodyWrapper);

        WelcomeLayoutComponents welcomeContent = new WelcomeLayoutComponents(View_Manager, mainlayoutWidth, mainlayoutHeight, Data_handler.getResourceOverviewInformation(), Data_handler.getPublicationList(), Data_handler.getQuantDatasetList());

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setHeight(mainlayoutHeight, Unit.PIXELS);
        mainLayout.setWidth(mainlayoutWidth, Unit.PIXELS);
        mainLayout.addComponent(welcomeContent);

        this.welcomeLayoutPanel = new ScrollPanel(mainLayout, welcomeContent.getMiniLayout(), 0, "welcomeview");
        View_Manager.registerComponent(welcomeLayoutPanel);
        bodyWrapper.addComponent(welcomeLayoutPanel);
        bodyWrapper.setComponentAlignment(welcomeLayoutPanel, Alignment.TOP_RIGHT);
        welcomeLayoutPanel.setShowNavigationBtn(false);

        QuantDataLayoutContainer quantLayout = new QuantDataLayoutContainer(Data_handler, mainlayoutWidth - 32, mainlayoutHeight - 2);

        quantLayoutPanel = new SlidePanel(quantLayout, null, 1, "quantview") {

            @Override
            public void setVisible(boolean visible) {
                super.setVisible(visible); //To change body of generated methods, choose Tools | Templates.
                if (this.getParent() != null) {
                    this.getParent().setVisible(visible);
                }
            }

        };
        quantLayoutPanel.setHeight(mainlayoutHeight, Unit.PIXELS);
        View_Manager.registerComponent(quantLayoutPanel);
       
        quantLayoutPanel.setShowNavigationBtn(false);

        welcomeContent.addMainZoomComponents(quantLayoutPanel);

        Panel quantPanelContaner = new Panel(quantLayoutPanel);// 
        quantLayoutPanel.setVisible(false);
        quantPanelContaner.setWidth(mainlayoutWidth , Unit.PIXELS);
        quantPanelContaner.setHeight(mainlayoutHeight, Unit.PIXELS);
        quantPanelContaner.setStyleName(ValoTheme.PANEL_BORDERLESS);

        bodyWrapper.addComponent(quantPanelContaner);
        bodyWrapper.setComponentAlignment(quantPanelContaner, Alignment.TOP_LEFT);
        View_Manager.viewLayout("welcomeview");
        
      
        
        
        
        
        

    }

}