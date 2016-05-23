package no.uib.probe.csf.pr.touch.view.bigscreen;

import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.Data_Handler;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;
import no.uib.probe.csf.pr.touch.view.LayoutViewManager;
import no.uib.probe.csf.pr.touch.view.bigscreen.quantlayoutcontainer.QuantDataLayoutContainer;
import no.uib.probe.csf.pr.touch.view.core.ScrollPanel;
import no.uib.probe.csf.pr.touch.view.smallscreen.OverviewInfoBean;
import no.uib.probe.csf.pr.touch.view.bigscreen.welcomepagecontainer.WelcomeLayoutComponents;
import no.uib.probe.csf.pr.touch.view.core.SlidePanel;
import no.uib.probe.csf.pr.touch.view.core.ZoomControler;

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
        this.setWidth(100,Unit.PERCENTAGE);
        this.setHeight(100,Unit.PERCENTAGE);
        this.setSpacing(true);
        this.View_Manager = new LayoutViewManager();
        this.Data_handler = new Data_Handler(url, dbName, driver, userName, password, filesURL);
        int mainlayoutWidth = pageWidth - 00;
        int mainlayoutHeight = pageHeight - 20;

        VerticalLayout bodyWrapper = new VerticalLayout();
        bodyWrapper.setHeightUndefined();
        bodyWrapper.setWidth(100,Unit.PERCENTAGE);
        this.addComponent(bodyWrapper);

        WelcomeLayoutComponents welcomeContent = new WelcomeLayoutComponents(View_Manager, mainlayoutWidth, Data_handler.getResourceOverviewInformation(), Data_handler.getPublicationList(), Data_handler.getQuantDatasetList());

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setHeight(mainlayoutHeight, Unit.PIXELS);
        mainLayout.setWidth(mainlayoutWidth, Unit.PIXELS);
        mainLayout.addComponent(welcomeContent);

        
//         String maxBodyHightStyle = ".maxappsize{ max-height:" + (mainlayoutHeight) + "px !important;"
//                + "max-width:" + (mainlayoutWidth) + "px !important;"
//                 + "overflow:auto !important;}"
//                ;
//        Page.getCurrent().getStyles().add(maxBodyHightStyle);
       

        this.welcomeLayoutPanel = new ScrollPanel(mainLayout, welcomeContent.getMiniLayout(), 0, "welcomeview");
        View_Manager.registerComponent(welcomeLayoutPanel);
        bodyWrapper.addComponent(welcomeLayoutPanel);
        bodyWrapper.setComponentAlignment(welcomeLayoutPanel, Alignment.TOP_RIGHT);
        welcomeLayoutPanel.setShowNavigationBtn(false);

        QuantDataLayoutContainer quantLayout = new QuantDataLayoutContainer(Data_handler, mainlayoutWidth-2, mainlayoutHeight-2);
        quantLayoutPanel = new SlidePanel(quantLayout, null, 1, "quantview");
        quantLayoutPanel.setHeight(mainlayoutHeight,Unit.PIXELS);
        View_Manager.registerComponent(quantLayoutPanel);
        quantLayoutPanel.setVisible(false);
        quantLayoutPanel.setShowNavigationBtn(false);
       
        welcomeContent.addMainZoomComponents(quantLayoutPanel);
        
        Panel quantPanelContaner = new Panel(quantLayoutPanel);//
        quantPanelContaner.setWidth(100,Unit.PERCENTAGE);
        quantPanelContaner.setHeight(mainlayoutHeight, Unit.PIXELS);
        quantPanelContaner.setStyleName(ValoTheme.PANEL_BORDERLESS);
        
         bodyWrapper.addComponent(quantPanelContaner);
        bodyWrapper.setComponentAlignment(quantPanelContaner, Alignment.TOP_LEFT);
//        quantPanelContaner.setStyleName("maxappsize");
        View_Manager.viewLayout("welcomeview");

    }

}
