package no.uib.probe.csf.pr.touch.view.bigscreen;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.DataHandler;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;
import no.uib.probe.csf.pr.touch.view.LayoutViewManager;
import no.uib.probe.csf.pr.touch.view.bigscreen.quantlayoutcontainer.QuantDataLayoutContainer;
import no.uib.probe.csf.pr.touch.view.core.ScrollPanel;
import no.uib.probe.csf.pr.touch.view.smallscreen.OverviewInfoBean;
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
    private final DataHandler Data_handler;

    public CSFApplicationContainer(int pageWidth, int bodyHeight,String url, String dbName, String driver, String userName, String password, String filesURL) {
        this.setWidth("100%");
        this.setHeight("100%");
        this.setSpacing(true);
        this.View_Manager = new LayoutViewManager();
        this.Data_handler = new DataHandler(url, dbName, driver, userName, password, filesURL);
        int mainlayoutWidth = pageWidth - 0;
        int mainlayoutHeight = bodyHeight - 100;
        
        VerticalLayout bodyWrapper = new VerticalLayout();
        bodyWrapper.setHeightUndefined();
        bodyWrapper.setWidth("100%");
        this.addComponent(bodyWrapper);
        
        
        WelcomeLayoutComponents welcomeContent = initWelcomePageLayout(mainlayoutWidth, Data_handler.getResourceOverviewInformation(), Data_handler.getPublicationList(), Data_handler.getQuantDatasetList(), Data_handler.getDiseaseHashedColorMap());

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setHeight((mainlayoutHeight) + "px");
        mainLayout.setWidth(mainlayoutWidth + "px");
        mainLayout.addComponent(welcomeContent);

//
        this.welcomeLayoutPanel = new ScrollPanel(mainLayout, welcomeContent.getMiniLayout(), 0,"welcomeview");
        View_Manager.registerComponent(welcomeLayoutPanel);
        bodyWrapper.addComponent(welcomeLayoutPanel);
        bodyWrapper.setComponentAlignment(welcomeLayoutPanel, Alignment.TOP_RIGHT);
        welcomeLayoutPanel.setShowNavigationBtn(false);
        
        QuantDataLayoutContainer quantLayout = new QuantDataLayoutContainer(mainlayoutWidth,mainlayoutHeight);
        quantLayoutPanel = new SlidePanel(quantLayout,null,1,"quantview");
        View_Manager.registerComponent(quantLayoutPanel);
        quantLayoutPanel.setVisible(false);
        bodyWrapper.addComponent(quantLayoutPanel);
        bodyWrapper.setComponentAlignment(quantLayoutPanel, Alignment.TOP_LEFT);
        
        View_Manager.viewLayout("welcomeview");
        
        
        
        
        

    }

    private WelcomeLayoutComponents initWelcomePageLayout(int width, OverviewInfoBean overviewInfoBean, List<Object[]> publicationList, Set<QuantDatasetObject> dsObjects, Map<String, String> diseaseHashedColorMap) {
        WelcomeLayoutComponents welcomeContent = new WelcomeLayoutComponents(View_Manager,width, overviewInfoBean, publicationList, dsObjects, diseaseHashedColorMap);
        return welcomeContent;
    }

}
