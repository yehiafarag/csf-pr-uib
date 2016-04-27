package no.uib.probe.csf.pr.touch.view.bigscreen;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.DataHandler;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;
import no.uib.probe.csf.pr.touch.view.ViewManager;
import no.uib.probe.csf.pr.touch.view.core.ScrollPanel;
import no.uib.probe.csf.pr.touch.view.smallscreen.OverviewInfoBean;
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

    private final ScrollPanel welcomeLayout;
    private final ViewManager View_Manager;
    private final DataHandler Data_handler;

    public WelcomePageContainer(int pageWidth, int bodyHeight,String url, String dbName, String driver, String userName, String password, String filesURL) {
        this.setWidth("100%");
        this.setHeight("100%");
        this.setSpacing(true);
        this.View_Manager = new ViewManager();
        this.Data_handler = new DataHandler(url, dbName, driver, userName, password, filesURL);
        int mainlayoutWidth = pageWidth - 0;
        WelcomeLayoutComponents welcomeContent = initWelcomePageLayout(mainlayoutWidth, Data_handler.getResourceOverviewInformation(), Data_handler.getPublicationList(), Data_handler.getQuantDatasetList(), Data_handler.getDiseaseHashedColorMap());

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setHeight((bodyHeight - 100) + "px");
        mainLayout.setWidth(mainlayoutWidth + "px");
        mainLayout.addComponent(welcomeContent);

//
        this.welcomeLayout = new ScrollPanel(mainLayout, welcomeContent.getMiniLayout(), 0);
        this.addComponent(welcomeLayout);
        this.setComponentAlignment(welcomeLayout, Alignment.TOP_RIGHT);

    }

    private WelcomeLayoutComponents initWelcomePageLayout(int width, OverviewInfoBean overviewInfoBean, List<Object[]> publicationList, Set<QuantDatasetObject> dsObjects, Map<String, String> diseaseHashedColorMap) {
        WelcomeLayoutComponents welcomeContent = new WelcomeLayoutComponents(width, overviewInfoBean, publicationList, dsObjects, diseaseHashedColorMap);
        return welcomeContent;
    }

}
