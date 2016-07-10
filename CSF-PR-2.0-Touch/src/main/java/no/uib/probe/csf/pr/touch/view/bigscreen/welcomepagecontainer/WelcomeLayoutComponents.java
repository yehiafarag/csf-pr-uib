package no.uib.probe.csf.pr.touch.view.bigscreen.welcomepagecontainer;

import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import no.uib.probe.csf.pr.touch.Data_Handler;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFPR_Central_Manager;
import no.uib.probe.csf.pr.touch.view.HeaderLayout;
import no.uib.probe.csf.pr.touch.view.LayoutViewManager;
import no.uib.probe.csf.pr.touch.view.bigscreen.popupwindows.StudiesInformationWindow;
import no.uib.probe.csf.pr.touch.view.bigscreen.searchinglayoutcontainer.components.CompareComponent;
import no.uib.probe.csf.pr.touch.view.bigscreen.searchinglayoutcontainer.components.SearchingComponent;
import no.uib.probe.csf.pr.touch.view.core.BigBtn;
import no.uib.probe.csf.pr.touch.view.core.ZoomControler;
import no.uib.probe.csf.pr.touch.view.smallscreen.OverviewInfoBean;

/**
 *
 * @author Yehia Farag
 *
 * This class represents welcome page main components and buttons welcome
 * message resource overview information
 */
public class WelcomeLayoutComponents extends VerticalLayout implements Serializable {

    private final HorizontalLayout miniLayout;
    private final ZoomControler zoomApp;

    public HorizontalLayout getMiniLayout() {
        return miniLayout;
    }

    /**
     * initialize body layout
     *
     * @param View_Manager view manager to control the current application view
     * @param overviewInfoBean resource overview information
     * @param publicationList list of available publications
     * @param dsObjects list of dataset information
     * @param diseaseHashedColorMap map of disease names and colors for disease
     * labels
     * @param bodyWidth current screen with
     *
     *
     */
    public WelcomeLayoutComponents(final Data_Handler Data_handler, CSFPR_Central_Manager CSFPR_Central_Manager, final LayoutViewManager View_Manager, int bodyWidth, int bodyHeight, OverviewInfoBean overviewInfoBean, List<Object[]> publicationList, Set<QuantDatasetObject> dsObjects) {
        this.setWidth(100, Unit.PERCENTAGE);
        this.setHeight(100, Unit.PERCENTAGE);
        this.addStyleName("hideoverflow");
        HeaderLayout header = new HeaderLayout();
        this.addComponent(header);
        float headerRatio = 65f / (float) bodyHeight;
        int bottomHeight = bodyHeight - 65;
        float bodyRatio = (float) bottomHeight / (float) bodyHeight;
        this.setExpandRatio(header, headerRatio);

        int rightPanelWidth = Math.min(1020, (bodyWidth - 220));
        HorizontalLayout mainBodyHLayout = new HorizontalLayout();
        mainBodyHLayout.setWidthUndefined();
        this.addComponent(mainBodyHLayout);
        this.setExpandRatio(mainBodyHLayout, bodyRatio);

        //init left panel (Resource overview)
        VerticalLayout leftPanelWrapper = new VerticalLayout();
        leftPanelWrapper.setWidth(160,Unit.PIXELS);
        leftPanelWrapper.setHeightUndefined();
//        leftPanelWrapper.setStyleName("framelayout");
        mainBodyHLayout.addComponent(leftPanelWrapper);

        // the stat layout
        Label statLabel = new Label("Resource  Status");
        statLabel.setStyleName(ValoTheme.LABEL_H3);
        statLabel.addStyleName(ValoTheme.LABEL_BOLD);
        statLabel.setWidthUndefined();
        statLabel.setHeightUndefined();
        leftPanelWrapper.addComponent(statLabel);

        Label quantStatLabel = new Label("Quantitative  Data");
        quantStatLabel.setHeight(20,Unit.PIXELS);
        quantStatLabel.addStyleName(ValoTheme.LABEL_BOLD);
        quantStatLabel.addStyleName(ValoTheme.LABEL_H4);
        quantStatLabel.addStyleName("marginleft");
        leftPanelWrapper.addComponent(quantStatLabel);

        GridLayout subQuantStatLayout = new GridLayout(2, 4);
        subQuantStatLayout.addStyleName("marginleft");
        subQuantStatLayout.setMargin(new MarginInfo(false, false, true, false));
        subQuantStatLayout.setWidth(100,Unit.PERCENTAGE);
        leftPanelWrapper.addComponent(subQuantStatLayout);

//        PublicationsInformationWindow sub1quantStatLabelWrapper = new PublicationsInformationWindow(publicationList);
//        Label sub1quantStatLabel = new Label("#Publications");
//        sub1quantStatLabel.setStyleName("link");
//        sub1quantStatLabel.addStyleName(ValoTheme.LABEL_SMALL);
//        sub1quantStatLabelWrapper.addComponent(sub1quantStatLabel);
//        subQuantStatLayout.addComponent(sub1quantStatLabelWrapper, 0, 0);//
//        sub1quantStatLabelWrapper.setDescription("Click to view publication information");
//
//        Label sub1QuantStatValue = new Label(overviewInfoBean.getNumberOfQuantPublication()+"");
//        sub1QuantStatValue.addStyleName(ValoTheme.LABEL_SMALL);
//        subQuantStatLayout.addComponent(sub1QuantStatValue, 1, 0);
//        subQuantStatLayout.setComponentAlignment(sub1QuantStatValue, Alignment.MIDDLE_RIGHT);

//        Set<QuantDatasetObject> dsObjects = CSFPR_Handler.getQuantDatasetList();
//
        StudiesInformationWindow sub2quantStatLabelWrapper = new StudiesInformationWindow(publicationList);
        sub2quantStatLabelWrapper.updateData(dsObjects);
        subQuantStatLayout.addComponent(sub2quantStatLabelWrapper, 0, 1);
        sub2quantStatLabelWrapper.setDescription("Click to view datasets information");
        Label sub2quantStatLabel = new Label("#Datasets");
         sub2quantStatLabel.setStyleName("link");
        sub2quantStatLabel.addStyleName(ValoTheme.LABEL_SMALL);
        sub2quantStatLabelWrapper.addComponent(sub2quantStatLabel);

        Label sub2QuantStatValue = new Label(overviewInfoBean.getNumberOfQuantStudies() + "");
        sub2QuantStatValue.addStyleName(ValoTheme.LABEL_SMALL);
        subQuantStatLayout.addComponent(sub2QuantStatValue, 1, 1);
        subQuantStatLayout.setComponentAlignment(sub2QuantStatValue, Alignment.MIDDLE_RIGHT);

        Label sub3quantStatLabel = new Label("#Proteins");
        sub3quantStatLabel.addStyleName(ValoTheme.LABEL_SMALL);
        subQuantStatLayout.addComponent(sub3quantStatLabel, 0, 2);

        Label sub3QuantStatValue = new Label("" + overviewInfoBean.getNumberOfQuantProteins());
        sub3QuantStatValue.addStyleName(ValoTheme.LABEL_SMALL);
        subQuantStatLayout.addComponent(sub3QuantStatValue, 1, 2);
        subQuantStatLayout.setComponentAlignment(sub3QuantStatValue, Alignment.MIDDLE_RIGHT);

        Label sub4quantStatLabel = new Label("#Peptides");
        sub4quantStatLabel.addStyleName(ValoTheme.LABEL_SMALL);
        subQuantStatLayout.addComponent(sub4quantStatLabel, 0, 3);

        Label sub4QuantStatValue = new Label("" + overviewInfoBean.getNumberOfQuantPeptides());
        sub4QuantStatValue.addStyleName(ValoTheme.LABEL_SMALL);
        subQuantStatLayout.addComponent(sub4QuantStatValue, 1, 3);
        subQuantStatLayout.setComponentAlignment(sub4QuantStatValue, Alignment.MIDDLE_RIGHT);

        subQuantStatLayout.setColumnExpandRatio(0, 2);
        subQuantStatLayout.setColumnExpandRatio(1, 1);

        Label idStatLabel = new Label("Identification Data");
        idStatLabel.setHeight(20,Unit.PIXELS);
        idStatLabel.addStyleName(ValoTheme.LABEL_BOLD);
        idStatLabel.addStyleName(ValoTheme.LABEL_H4);
        idStatLabel.addStyleName("marginleft");
        idStatLabel.addStyleName("margintop");
        
        leftPanelWrapper.addComponent(idStatLabel);

        GridLayout subIdStatLayout = new GridLayout(2, 4);
        subIdStatLayout.addStyleName("marginleft");
        subIdStatLayout.setWidth(100,Unit.PERCENTAGE);
        subIdStatLayout.setSpacing(true);
        leftPanelWrapper.addComponent(subIdStatLayout);

        Label sub2IdStatLabel = new Label("#Datasets");
        sub2IdStatLabel.addStyleName(ValoTheme.LABEL_SMALL);
        subIdStatLayout.addComponent(sub2IdStatLabel, 0, 1);
        subIdStatLayout.setColumnExpandRatio(0, 2);
        subIdStatLayout.setColumnExpandRatio(1, 1);

        Label sub2IdStatValue = new Label("" + overviewInfoBean.getNumberOfIdStudies());
        sub2IdStatValue.addStyleName(ValoTheme.LABEL_SMALL);
        subIdStatLayout.addComponent(sub2IdStatValue, 1, 1);
        subIdStatLayout.setComponentAlignment(sub2IdStatValue, Alignment.MIDDLE_RIGHT);

        Label sub3IdStatLabel = new Label("#Proteins");
        sub3IdStatLabel.addStyleName(ValoTheme.LABEL_SMALL);
        subIdStatLayout.addComponent(sub3IdStatLabel, 0, 2);

        Label sub3IdStatValue = new Label("" + overviewInfoBean.getNumberOfIdProteins());
        sub3IdStatValue.addStyleName(ValoTheme.LABEL_SMALL);
        subIdStatLayout.addComponent(sub3IdStatValue, 1, 2);
        subIdStatLayout.setComponentAlignment(sub3IdStatValue, Alignment.MIDDLE_RIGHT);

        Label sub4IdStatLabel = new Label("#Peptides");
        sub4IdStatLabel.addStyleName(ValoTheme.LABEL_SMALL);
        subIdStatLayout.addComponent(sub4IdStatLabel, 0, 3);

        Label sub4IdStatValue = new Label("" + overviewInfoBean.getNumberOfIdPeptides() );
        sub4IdStatValue.addStyleName(ValoTheme.LABEL_SMALL);
        subIdStatLayout.addComponent(sub4IdStatValue, 1, 3);
        subIdStatLayout.setComponentAlignment(sub4IdStatValue, Alignment.MIDDLE_RIGHT);

        //end of left panel
        //init spacer
        VerticalLayout spacer = new VerticalLayout();
        spacer.setHeight(100, Unit.PERCENTAGE);
        spacer.setWidth(10, Unit.PIXELS);
        spacer.setStyleName("spacer");
        mainBodyHLayout.addComponent(spacer);

        //end of spacer
        //init rightlayout top
        VerticalLayout rightPanelWrapper = new VerticalLayout();
        rightPanelWrapper.setWidth(rightPanelWidth, Unit.PIXELS);
        rightPanelWrapper.setHeightUndefined();
        rightPanelWrapper.setStyleName("framelayout");
        mainBodyHLayout.addComponent(rightPanelWrapper);

        Label infoLable = new Label("<h1>Welcome to The CSF Proteome Resource (CSF-PR)</h1>");
        infoLable.setHeight(50, Unit.PIXELS);
        infoLable.setContentMode(ContentMode.HTML);
        rightPanelWrapper.addComponent(infoLable);

        Label para_1 = new Label("<p style='font-size: 14px;color: black;line-height: 20px;'>As CSF is in direct contact with the central nervous system (CNS) it can provide indications about the state of the CNS. This is particularly relevant for neurodegenerative diseases, such as Multiple Sclerosis, Alzheimer's and Parkinson's, where the CSF would be a natural place to look for disease biomarkers.<br/><br/><font style='font-size: 14px;color: black;line-height: 20px;'>CSF Proteome Resource (CSF-PR) v2.0 is an online repository of mass spectrometry based proteomics "
                + "experiments on human cerebrospinal fluid (CSF). </font></p>");
        para_1.setContentMode(ContentMode.HTML);
        rightPanelWrapper.addComponent(para_1);
        para_1.setWidth(rightPanelWidth, Unit.PIXELS);

        HorizontalLayout rightPanel = new HorizontalLayout();
        rightPanel.setWidth(100, Unit.PERCENTAGE);
        rightPanelWrapper.addComponent(rightPanel);

        VerticalLayout middleLayout = new VerticalLayout();
        middleLayout.setWidth(100, Unit.PERCENTAGE);
        middleLayout.setMargin(false);
        rightPanel.addComponent(middleLayout);

        GridLayout middlePanelServicesLayout = new GridLayout(2, 2);
        middlePanelServicesLayout.setSpacing(true);
        middlePanelServicesLayout.setMargin(new MarginInfo(true, false, true, false));
        middleLayout.addComponent(middlePanelServicesLayout);
        

        BigBtn quantDatasetBtn = new BigBtn("Quantification", "Browse quantitative data", "img/scatter_plot_applied.png") {

            @Override
            public void onClick() {
                View_Manager.viewLayout("quantview");
            }
        };
        quantDatasetBtn.getThumbBtn().setDescription("Click to browse protein quantitative data");
        middlePanelServicesLayout.addComponent(quantDatasetBtn, 0, 0);
        BigBtn idDatasetBtn = new BigBtn("Identification", "Browse identification data", "img/bar-chart.png") {

            @Override
            public void onClick() {
                Page.getCurrent().open(VaadinSession.getCurrent().getAttribute("csf_pr_Url").toString(), "_blank");//setLocation("http://129.177.231.63/csf-pr-v1.0");
                
            }
        };
        
        idDatasetBtn.getThumbBtn().setDescription("Click to browse protein identification data");
        
        middlePanelServicesLayout.addComponent(idDatasetBtn, 0, 1);
        
        
        
        
        SearchingComponent searchingDatasetBtn = new SearchingComponent(Data_handler,CSFPR_Central_Manager){

            @Override
            public void loadQuantSearching() {
                 View_Manager.viewLayout("quantview");
            }
        
        
        };
          searchingDatasetBtn.getThumbBtn().setDescription("Click to search quantified and identified protein data");
        middlePanelServicesLayout.addComponent(searchingDatasetBtn, 1, 0);

        CompareComponent compareBtn = new CompareComponent(Data_handler, CSFPR_Central_Manager){

             @Override
            public void loadQuantComparison() {
                 View_Manager.viewLayout("quantview");
            }
            
            
        };
        compareBtn.getThumbBtn().setDescription("Click to compare with your own protein quantification data");
        middlePanelServicesLayout.addComponent(compareBtn, 1, 1);

        BigBtn homeBtn = new BigBtn("", "", "img/home-o.png") {

            @Override
            public void onClick() {
                View_Manager.viewLayout("welcomeview");
            }
        };
        this.addComponent(homeBtn);
        homeBtn.setVisible(false);
        homeBtn.getThumbBtn().setDescription("Home page");

        Label para_3 = new Label("<p align='justify' Style='font-size: 12px;color: black;/* font-weight: bold; */line-height: 20px;'><font>CSF-PR is developed by the <a class='link' href='http://www.uib.no/rg/probe' target='_blank'>Proteomics Unit</a> at the<a class='link' href='http://www.uib.no/biomedisin/en' target='_blank'> Department of Biomedicine at the University of Bergen, Norway</a>, in close collaboration with <a class='link' href='http://haukeland.no/en/OmOss/Avdelinger/ms/Sider/om-oss.aspx' target='_blank'>The Norwegian Multiple Sclerosis Competence Centre, Haukeland University Hospital, Bergen, Norway.</a></font><br/><br/><font>CSF-PR publications:<br/><a class='link' href='http://www.mcponline.org/content/13/11/3152.full.pdf+html' target='_blank'>- Guldbrandsen et al.: In-depth Characterization of the Cerebrospinal Fluid (CSF) Proteome Displayed Through the CSF Proteome Resource (CSF-PR). Mol Cell Proteomics. 2014.</a><br/>- Guldbrandsen et al.: CSF-PR 2.0: your guide to quantitative cerebrospinal fluid mass spectrometry data. <i>(in preparation)</i>.</font></p>");
        para_3.setContentMode(ContentMode.HTML);
        middleLayout.addComponent(para_3);
        middleLayout.setComponentAlignment(para_3, Alignment.BOTTOM_LEFT);

        //init mini layout
        zoomApp = new ZoomControler(false);
        zoomApp.setWidth(40, Unit.PIXELS);
        VerticalLayout miniLayoutContainer = new VerticalLayout(homeBtn.getThumbBtn(), quantDatasetBtn.getThumbBtn(), idDatasetBtn.getThumbBtn(), searchingDatasetBtn.getThumbBtn(), compareBtn.getThumbBtn(), zoomApp);
        miniLayout = new HorizontalLayout(miniLayoutContainer);
        miniLayout.addStyleName("zoomborder");
        miniLayoutContainer.setSpacing(true);
//        
    }

    public void addMainZoomComponents(Component component) {

        zoomApp.addZoomableComponent(component);
    }
}
