package no.uib.probe.csf.pr.touch.view.bigscreen.welcomepagecontainer;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;
import no.uib.probe.csf.pr.touch.view.LayoutViewManager;
import no.uib.probe.csf.pr.touch.view.bigscreen.popupwindows.PublicationsInformationWindow;
import no.uib.probe.csf.pr.touch.view.bigscreen.popupwindows.StudiesInformationWindow;
import no.uib.probe.csf.pr.touch.view.core.BigBtn;
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
    private final List<Object[]> publicationList;

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
     * @param diseaseHashedColorMap map of disease names and colors for disease labels
     * @param screenWidth current screen with
     *
     *
     */
    public WelcomeLayoutComponents(final LayoutViewManager View_Manager,int screenWidth,OverviewInfoBean overviewInfoBean,List<Object[]> publicationList,Set<QuantDatasetObject> dsObjects,Map<String, String> diseaseHashedColorMap ) {
        this.setWidth("100%");
        this.setHeight("100%");
        

        int rightPanelWidth = Math.min(1020, (screenWidth - 220));
        HorizontalLayout mainBodyHLayout = new HorizontalLayout();
        mainBodyHLayout.setWidthUndefined();
        this.addComponent(mainBodyHLayout);

        //init left panel (Resource overview)
        VerticalLayout leftPanelWrapper = new VerticalLayout();
        leftPanelWrapper.setWidthUndefined();
        leftPanelWrapper.setHeightUndefined();
        leftPanelWrapper.setStyleName("framelayout");
        mainBodyHLayout.addComponent(leftPanelWrapper);

        // the stat layout
        Label statLabel = new Label("<center><h1>Resource  Status</h1></center>");
        statLabel.setContentMode(ContentMode.HTML);
        statLabel.setWidthUndefined();
        statLabel.setHeight("50px");
        leftPanelWrapper.addComponent(statLabel);

        Label quantStatLabel = new Label("<h2>Quantitative  Data</h2>");
        quantStatLabel.setContentMode(ContentMode.HTML);
        leftPanelWrapper.addComponent(quantStatLabel);

        GridLayout subQuantStatLayout = new GridLayout(2, 4);
        subQuantStatLayout.setWidthUndefined();
        leftPanelWrapper.addComponent(subQuantStatLayout);

        PublicationsInformationWindow sub1quantStatLabelWrapper = new PublicationsInformationWindow(publicationList);
        Label sub1quantStatLabel = new Label("<h3 style='text-decoration: underline;cursor: pointer;'>#Publications</h3>");
        sub1quantStatLabel.setContentMode(ContentMode.HTML);
        sub1quantStatLabelWrapper.addComponent(sub1quantStatLabel);
        subQuantStatLayout.addComponent(sub1quantStatLabelWrapper, 0, 0);//
        sub1quantStatLabelWrapper.setDescription("Click to view publication information");

        Label sub1QuantStatValue = new Label("<h4 style='text-align: right;' >" + overviewInfoBean.getNumberOfQuantPublication()+ "</h4>");
        sub1QuantStatValue.setContentMode(ContentMode.HTML);
        subQuantStatLayout.addComponent(sub1QuantStatValue, 1, 0);
        subQuantStatLayout.setComponentAlignment(sub1QuantStatValue, Alignment.MIDDLE_RIGHT);

//        Set<QuantDatasetObject> dsObjects = CSFPR_Handler.getQuantDatasetList();
//
        StudiesInformationWindow sub2quantStatLabelWrapper = new StudiesInformationWindow(dsObjects, diseaseHashedColorMap);
        subQuantStatLayout.addComponent(sub2quantStatLabelWrapper, 0, 1);
        sub2quantStatLabelWrapper.setDescription("Click to view datasets information");
        Label sub2quantStatLabel = new Label("<h3 style='text-decoration: underline;cursor: pointer;'>#Datasets</h3>");
        sub2quantStatLabel.setContentMode(ContentMode.HTML);
        sub2quantStatLabelWrapper.addComponent(sub2quantStatLabel);

        Label sub2QuantStatValue = new Label("<h4 style='text-align: right;'>" + overviewInfoBean.getNumberOfQuantStudies() + "</h4>");
        sub2QuantStatValue.setContentMode(ContentMode.HTML);
        subQuantStatLayout.addComponent(sub2QuantStatValue, 1, 1);
        subQuantStatLayout.setComponentAlignment(sub2QuantStatValue, Alignment.MIDDLE_RIGHT);

        Label sub3quantStatLabel = new Label("<h3>#Proteins</h3>");
        sub3quantStatLabel.setContentMode(ContentMode.HTML);
        subQuantStatLayout.addComponent(sub3quantStatLabel, 0, 2);

        Label sub3QuantStatValue = new Label("<h4 style='text-align: right;'>" + overviewInfoBean.getNumberOfQuantProteins() + "</h4>");
        sub3QuantStatValue.setContentMode(ContentMode.HTML);
        subQuantStatLayout.addComponent(sub3QuantStatValue, 1, 2);
        subQuantStatLayout.setComponentAlignment(sub3QuantStatValue, Alignment.MIDDLE_RIGHT);

        Label sub4quantStatLabel = new Label("<h3>#Peptides</h3>");
        sub4quantStatLabel.setContentMode(ContentMode.HTML);
        subQuantStatLayout.addComponent(sub4quantStatLabel, 0, 3);

        Label sub4QuantStatValue = new Label("<h4 style='text-align: right;'>" + overviewInfoBean.getNumberOfQuantPeptides() + "</h4>");
        sub4QuantStatValue.setContentMode(ContentMode.HTML);
        subQuantStatLayout.addComponent(sub4QuantStatValue, 1, 3);
        subQuantStatLayout.setComponentAlignment(sub4QuantStatValue, Alignment.MIDDLE_RIGHT);

        subQuantStatLayout.setColumnExpandRatio(0, 2);
        subQuantStatLayout.setColumnExpandRatio(1, 1);

        Label idStatLabel = new Label("<h2>Identification Data</h2>");
        idStatLabel.setContentMode(ContentMode.HTML);
        leftPanelWrapper.addComponent(idStatLabel);

        GridLayout subIdStatLayout = new GridLayout(2, 4);
        subIdStatLayout.setWidthUndefined();
        subIdStatLayout.setSpacing(true);
        leftPanelWrapper.addComponent(subIdStatLayout);

        Label sub2IdStatLabel = new Label("<h3>#Datasets</h3>");
        sub2IdStatLabel.setContentMode(ContentMode.HTML);
        subIdStatLayout.addComponent(sub2IdStatLabel, 0, 1);
        subIdStatLayout.setColumnExpandRatio(0, 2);
        subIdStatLayout.setColumnExpandRatio(1, 1);

        Label sub2IdStatValue = new Label("<h4 style='text-align: right;'>" + overviewInfoBean.getNumberOfIdStudies() + "</h4>");
        sub2IdStatValue.setContentMode(ContentMode.HTML);
        subIdStatLayout.addComponent(sub2IdStatValue, 1, 1);
        subIdStatLayout.setComponentAlignment(sub2IdStatValue, Alignment.MIDDLE_RIGHT);

        Label sub3IdStatLabel = new Label("<h3>#Proteins</h3>");
        sub3IdStatLabel.setContentMode(ContentMode.HTML);
        subIdStatLayout.addComponent(sub3IdStatLabel, 0, 2);

        Label sub3IdStatValue = new Label("<h4 style='text-align: right;'>" + overviewInfoBean.getNumberOfIdProteins()+ "</h4>");
        sub3IdStatValue.setContentMode(ContentMode.HTML);
        subIdStatLayout.addComponent(sub3IdStatValue, 1, 2);
        subIdStatLayout.setComponentAlignment(sub3IdStatValue, Alignment.MIDDLE_RIGHT);

        Label sub4IdStatLabel = new Label("<h3>#Peptides</h3>");
        sub4IdStatLabel.setContentMode(ContentMode.HTML);
        subIdStatLayout.addComponent(sub4IdStatLabel, 0, 3);

        Label sub4IdStatValue = new Label("<h4 style='text-align: right;'>" + overviewInfoBean.getNumberOfIdPeptides()+ "</h4>");
        sub4IdStatValue.setContentMode(ContentMode.HTML);
        subIdStatLayout.addComponent(sub4IdStatValue, 1, 3);
        subIdStatLayout.setComponentAlignment(sub4IdStatValue, Alignment.MIDDLE_RIGHT);

        //end of left panel
        //init spacer
        VerticalLayout spacer = new VerticalLayout();
        spacer.setHeight("100%");
        spacer.setWidth("10px");
        spacer.setStyleName("spacer");
        mainBodyHLayout.addComponent(spacer);

        //end of spacer
        //init rightlayout top
        VerticalLayout rightPanelWrapper = new VerticalLayout();
        rightPanelWrapper.setWidth(rightPanelWidth + "px");
        rightPanelWrapper.setHeightUndefined();
        rightPanelWrapper.setStyleName("framelayout");
        mainBodyHLayout.addComponent(rightPanelWrapper);

        Label infoLable = new Label("<h1>Welcome to CSF Proteome Resource (CSF-PR)</h1>");
        infoLable.setHeight("50px");
        infoLable.setContentMode(ContentMode.HTML);
        rightPanelWrapper.addComponent(infoLable);

        Label para_1 = new Label("<p style='font-size: 14px;color: black;/* font-weight: bold; */line-height: 30px;'>CSF Proteome Resource (CSF-PR)v2.0 is an online repository of mass spectrometry based proteomics"
                + "experiments on human cerebrospinal fluid (CSF). CSF is in direct contact with the central nervous"
                + "system (CNS) and can give indications about the state of the CNS.This is particularly relevant for "
                + "neurodegenerative diseases, such as Multiple Sclerosis, where CSF would be a natural place to look"
                + "for disease biomarkers.</p>");
        para_1.setContentMode(ContentMode.HTML);
        rightPanelWrapper.addComponent(para_1);
        para_1.setWidth(rightPanelWidth+"px");

        HorizontalLayout rightPanel = new HorizontalLayout();
        rightPanel.setWidth(100 + "%");
        rightPanelWrapper.addComponent(rightPanel);

        
        
        
        
//        VerticalLayout bottomLayout = new VerticalLayout();
//        bottomLayout.setWidth("100%");
//        bottomLayout.setSpacing(true);
//        bottomLayout.setMargin(false);
//        mainBodyHLayout.addComponent(bottomLayout);
//        mainBodyHLayout.setComponentAlignment(bottomLayout, Alignment.BOTTOM_CENTER);

        VerticalLayout middleLayout = new VerticalLayout();
        middleLayout.setWidth("100%");
        middleLayout.setMargin(false);
        rightPanel.addComponent(middleLayout);
     
        GridLayout middlePanelServicesLayout = new GridLayout(2, 2);
        middlePanelServicesLayout.setSpacing(true);
        middlePanelServicesLayout.setMargin(new MarginInfo(true, false, true, false));
        middleLayout.addComponent(middlePanelServicesLayout);

        BigBtn quantDatasetBtn = new BigBtn("Quantitative Dataset", "Brows quantitative data.", "img/scatter_plot_applied.png") {

            @Override
            public void onClick() {
                View_Manager.viewLayout("quantview");
            }
        };
        middlePanelServicesLayout.addComponent(quantDatasetBtn, 0, 0);

        BigBtn idDatasetBtn = new BigBtn("Identification Dataset", "Brows identification data.", "img/bar-chart.png") {

            @Override
            public void onClick() {
                View_Manager.viewLayout("idview");
            }
        };
        middlePanelServicesLayout.addComponent(idDatasetBtn, 0, 1);
        BigBtn searchingDatasetBtn = new BigBtn("Search", "Search quantitative and  identification proteins.", "img/search.png") {

            @Override
            public void onClick() {
                View_Manager.viewLayout("searchingview");
            }
        };

        middlePanelServicesLayout.addComponent(searchingDatasetBtn, 1, 0);

        BigBtn compareBtn = new BigBtn("Compare", "Compare your quantified with the available data.","img/compare.png" ) {
            
            @Override
            public void onClick() {
                View_Manager.viewLayout("compareview");
            }
        };
        middlePanelServicesLayout.addComponent(compareBtn, 1, 1);
        
        
        BigBtn homeBtn = new BigBtn("", "", "img/home-o.png") {

            @Override
            public void onClick() {
                View_Manager.viewLayout("welcomeview");
            }
        };
        this.addComponent(homeBtn);
        homeBtn.setVisible(false);
       
        Label para_3 = new Label("<p align='justify' Style='font-size: 12px;color: black;/* font-weight: bold; */line-height: 20px;'><font>CSF-PR v2.0 is being developed by the <a Style='color:#141414;' href='http://www.uib.no/rg/probe' target='_blank'>Proteomics Unit</a> at the<a Style='color:#141414;' href='http://www.uib.no/biomedisin/en' target='_blank'> Department of Biomedicine at the University of Bergen</a>, Norway, in close collaboration with <a Style='color:#141414;' href='http://haukeland.no/en/OmOss/Avdelinger/ms/Sider/om-oss.aspx' target='_blank'>The Norwegian Multiple Sclerosis Competence Centre</a>, Haukeland University Hospital, Bergen, Norway.</font><br/><font>See also: <a Style='color:#141414;' href='http://www.mcponline.org/content/13/11/3152.full.pdf+html' target='_blank'>Guldbrandsen et al.: In-depth Characterization of the Cerebrospinal Fluid (CSF) Proteome Displayed Through the CSF Proteome Resource (CSF-PR). Mol Cell Proteomics. 2014.</a></font></p>");
        para_3.setContentMode(ContentMode.HTML);
        middleLayout.addComponent(para_3);
        middleLayout.setComponentAlignment(para_3, Alignment.BOTTOM_LEFT);

        //init mini layout
        miniLayout = new HorizontalLayout(quantDatasetBtn.getThumbBtn(),idDatasetBtn.getThumbBtn(),searchingDatasetBtn.getThumbBtn(),compareBtn.getThumbBtn(),homeBtn.getThumbBtn());
        miniLayout.setSpacing(true);
        miniLayout.setMargin(new MarginInfo(false));
//        
        this.publicationList = publicationList;
    }
}
