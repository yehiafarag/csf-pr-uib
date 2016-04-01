/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import probe.com.handlers.CSFPRHandler;
import probe.com.model.beans.OverviewInfoBean;
import probe.com.model.beans.quant.QuantDatasetObject;
import probe.com.view.body.welcomelayout.PublicationsInformationWindow;
import probe.com.view.body.welcomelayout.StudiesInformationWindow;

/**
 *
 * @author Yehia Farag
 *
 * static layout has main project information and access to admin login
 */
public class WelcomeLayout extends Panel implements Serializable {

    /**
     * initialize body layout
     *
     * @param adminIcon the access button for admin Layout
     * @param CSFPR_Handler
     * @param mainTabSheet
     *
     *
     */
    public WelcomeLayout(Button adminIcon, CSFPRHandler CSFPR_Handler, final TabSheet mainTabSheet) {

        int fullWidth = Page.getCurrent().getBrowserWindowWidth();
        int fullHeight = Page.getCurrent().getBrowserWindowHeight() - 100;
        this.setWidth(100 + "%");
        this.setHeight(fullHeight + "px");
        this.setStyleName("bottomborder");
        HorizontalLayout mainBodyHLayout = new HorizontalLayout();
        mainBodyHLayout.setWidthUndefined();
        this.setContent(mainBodyHLayout);

        VerticalLayout leftPanelWrapper = new VerticalLayout();
        leftPanelWrapper.setWidth("220px");
        leftPanelWrapper.setHeight("515px");
        mainBodyHLayout.addComponent(leftPanelWrapper);

        VerticalLayout spacer = new VerticalLayout();
        spacer.setHeight("30px");
        spacer.setWidth("10px");
        mainBodyHLayout.addComponent(spacer);

        VerticalLayout rightPanelWrapper = new VerticalLayout();
        rightPanelWrapper.setWidth("1020px");
        rightPanelWrapper.setHeight("515px");
        mainBodyHLayout.addComponent(rightPanelWrapper);

        HorizontalLayout mainBody = new HorizontalLayout();
        mainBody.setWidthUndefined();
        mainBody.setHeightUndefined();
        leftPanelWrapper.addComponent(mainBody);
        mainBody.setStyleName("bottomborder");

        VerticalLayout leftLayout = new VerticalLayout();
        leftLayout.setWidth("220px");
        leftLayout.setHeightUndefined();
        leftLayout.setMargin(true);
        leftLayout.setStyleName("framelayout");
        leftLayout.setSpacing(true);
        leftLayout.setMargin(new MarginInfo(true, true, true, true));
        mainBody.addComponent(leftLayout);
        mainBody.setComponentAlignment(leftLayout, Alignment.TOP_LEFT);

        int layoutWidth = (fullWidth - 220);
        HorizontalLayout rightPanel = new HorizontalLayout();
        rightPanel.setWidth(layoutWidth + "px");
        rightPanelWrapper.addComponent(rightPanel);

        VerticalLayout bottomLayout = new VerticalLayout();
        bottomLayout.setWidth("100%");
        bottomLayout.setSpacing(true);
        bottomLayout.setMargin(new MarginInfo(true, true, true, true));
        mainBodyHLayout.addComponent(bottomLayout);
        mainBodyHLayout.setComponentAlignment(bottomLayout, Alignment.BOTTOM_CENTER);

        VerticalLayout middleLayout = new VerticalLayout();
        middleLayout.setWidth("60%");
        middleLayout.setMargin(new MarginInfo(true, true, true, true));
        rightPanel.addComponent(middleLayout);

//        VerticalLayout rightLayout = new VerticalLayout();
//        rightLayout.setWidth("100%");
//        rightPanel.addComponent(rightLayout);
        Label infoLable = new Label("<h2 style='font-family:Verdana;'>Welcome to CSF Proteome Resource (CSF-PR)</h2>");
        infoLable.setContentMode(ContentMode.HTML);
        middleLayout.addComponent(infoLable);
        middleLayout.setComponentAlignment(infoLable, Alignment.MIDDLE_LEFT);

        Label para_1 = new Label("<p align='justify' Style='color:#585858;'><font size=\"2\">CSF Proteome Resource (CSF-PR)v2.0 is an online repository of mass spectrometry based proteomics "
                + "experiments on human cerebrospinal fluid (CSF). CSF is in direct contact with the central nervous"
                + "system (CNS) and can give indications about the state of the CNS.This is particularly relevant for "
                + "neurodegenerative diseases, such as Multiple Sclerosis, where CSF would be a natural place to look"
                + "for disease biomarkers.</font></p>");
        para_1.setContentMode(ContentMode.HTML);
        middleLayout.addComponent(para_1);

//        Label para_2 = new Label("<p align='justify' Style='margin-left:40px;color:#585858;'><font size=\"2\">The data can be viewed by selecting individual experiments or by searching for key words (protein "
//                + "name/accession number or peptide sequence) across all experiments. For GeLC-MS experiments the "
//                + "distribution of the identified proteins in the gel is also displayed.</font></p>");
//
//        para_2.setContentMode(ContentMode.HTML);
//        middleLayout.addComponent(para_2);
//        
//         
        GridLayout middlePanelServicesLayout = new GridLayout(2, 2);
        int labelWidth = (layoutWidth / 4) - 130;
        middlePanelServicesLayout.setStyleName("btnsiconcontainer");
        middlePanelServicesLayout.setSpacing(true);
        middleLayout.addComponent(middlePanelServicesLayout);

        HorizontalLayout quantDatasetBtnContainer = new HorizontalLayout();
        quantDatasetBtnContainer.setWidth("100%");
        middlePanelServicesLayout.addComponent(quantDatasetBtnContainer, 0, 0);
        quantDatasetBtnContainer.setMargin(new MarginInfo(true, true, true, false));
        quantDatasetBtnContainer.setSpacing(true);
        quantDatasetBtnContainer.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                mainTabSheet.setSelectedTab(1);
                mainTabSheet.markAsDirty();
            }
        });

        Image quantDatasetBtnIcon = new Image();
        quantDatasetBtnIcon.setWidth("70px");
        quantDatasetBtnIcon.setHeight("70px");
        quantDatasetBtnIcon.setSource(new ThemeResource("img/scatter_plot_applied.png"));
        quantDatasetBtnContainer.addComponent(quantDatasetBtnIcon);

        Label quantDatasetBtnLabel = new Label("<br/><b>Quantitative Dataset</b><br/><font size='1'>Brows quantitative data available in CSF-PR v2.0.</font>");
        quantDatasetBtnLabel.setContentMode(ContentMode.HTML);
        quantDatasetBtnContainer.addComponent(quantDatasetBtnLabel);
        quantDatasetBtnLabel.setWidth(labelWidth + "px");

        HorizontalLayout idDatasetBtnContainer = new HorizontalLayout();
        idDatasetBtnContainer.setWidth("100%");
        middlePanelServicesLayout.addComponent(idDatasetBtnContainer, 0, 1);
        idDatasetBtnContainer.setMargin(new MarginInfo(true, true, true, false));
        idDatasetBtnContainer.setSpacing(true);
        idDatasetBtnContainer.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                mainTabSheet.setSelectedTab(2);
                mainTabSheet.markAsDirty();
            }
        });

        Image idDatasetBtnIcon = new Image();
        idDatasetBtnIcon.setWidth("70px");
        idDatasetBtnIcon.setHeight("70px");
        idDatasetBtnIcon.setSource(new ThemeResource("img/bar-chart.png"));
        idDatasetBtnContainer.addComponent(idDatasetBtnIcon);

        Label idDatasetBtnLabel = new Label("<br/><b>Identification Dataset</b><br/><font size='1'>Brows identification data available in CSF-PR v2.0.</font>");
        idDatasetBtnLabel.setContentMode(ContentMode.HTML);
        idDatasetBtnContainer.addComponent(idDatasetBtnLabel);
        idDatasetBtnLabel.setWidth(labelWidth + "px");

        HorizontalLayout searchingBtnContainer = new HorizontalLayout();
        searchingBtnContainer.setWidth("100%");
        middlePanelServicesLayout.addComponent(searchingBtnContainer, 1, 0);
        searchingBtnContainer.setMargin(new MarginInfo(true, true, true, false));
        searchingBtnContainer.setSpacing(true);
        searchingBtnContainer.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                mainTabSheet.setSelectedTab(3);
                mainTabSheet.markAsDirty();
            }
        });

        Image searchingDatasetBtnIcon = new Image();
        searchingDatasetBtnIcon.setSource(new ThemeResource("img/search.png"));
        searchingDatasetBtnIcon.setWidth("70px");
        searchingDatasetBtnIcon.setHeight("70px");
        searchingBtnContainer.addComponent(searchingDatasetBtnIcon);

        Label searchingDatasetBtnLabel = new Label("<br/><b>Search</b><br/><font size='1'>Search quantitative and  identification proteins using protein's name, accessions and peptides sequences.</font>");
        searchingDatasetBtnLabel.setContentMode(ContentMode.HTML);
        searchingBtnContainer.addComponent(searchingDatasetBtnLabel);
        searchingDatasetBtnLabel.setWidth(labelWidth + "px");

        HorizontalLayout compareBtnContainer = new HorizontalLayout();
        compareBtnContainer.setWidth("100%");
        middlePanelServicesLayout.addComponent(compareBtnContainer, 1, 1);
        compareBtnContainer.setMargin(new MarginInfo(true, true, true, false));
        compareBtnContainer.setSpacing(true);
        compareBtnContainer.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                mainTabSheet.setSelectedTab(4);
                mainTabSheet.markAsDirty();
            }
        });
        Image compareDatasetBtnIcon = new Image();
        compareDatasetBtnIcon.setWidth("70px");
        compareDatasetBtnIcon.setHeight("70px");
        compareDatasetBtnIcon.setSource(new ThemeResource("img/compare.png"));
        compareBtnContainer.addComponent(compareDatasetBtnIcon);

        Label compareDatasetBtnLabel = new Label("<br/><b>Compare</b><br/><font size='1'>Compare your quantitative protein information with the available data in CSF-PR v2.0.</font>");
        compareDatasetBtnLabel.setContentMode(ContentMode.HTML);
        compareBtnContainer.addComponent(compareDatasetBtnLabel);
        compareDatasetBtnLabel.setWidth(labelWidth + "px");

//        VerticalLayout spacer = new VerticalLayout();
//        spacer.setHeight("30px");
//        spacer.setWidth("10px");
//          middleLayout.addComponent(spacer);
        Label para_3 = new Label("<p align='justify' Style='color:#585858;'><font size=\"1\">CSF-PR v2.0 is being developed by the <a Style='color:#585858;' href='http://www.uib.no/rg/probe' target=\"_blank\">Proteomics Unit</a> at the<a Style='color:#585858;' href='http://www.uib.no/biomedisin/en' target=\"_blank\"> Department of Biomedicine at the University of Bergen</a>, Norway, in close collaboration with <a Style='color:#585858;' href='http://haukeland.no/en/OmOss/Avdelinger/ms/Sider/om-oss.aspx' target=\"_blank\">The Norwegian Multiple Sclerosis Competence Centre</a>, Haukeland University Hospital, Bergen, Norway.</font></p>");
        para_3.setContentMode(ContentMode.HTML);
        middleLayout.addComponent(para_3);
        middleLayout.setComponentAlignment(para_3, Alignment.BOTTOM_LEFT);

        Label para_4 = new Label("<p align='justify' Style=';color:#585858;'><font size=\"1\">See also: <a Style='color:#585858;' href='http://www.mcponline.org/content/13/11/3152.full.pdf+html' target=\"_blank\">Guldbrandsen et al.: In-depth Characterization of the Cerebrospinal Fluid (CSF) Proteome Displayed Through the CSF Proteome Resource (CSF-PR). Mol Cell Proteomics. 2014.</a></font></p>");
        para_4.setContentMode(ContentMode.HTML);
        middleLayout.addComponent(para_4);
        middleLayout.setComponentAlignment(para_4, Alignment.BOTTOM_LEFT);
//        

//        Link fullOverviewImgLink = new Link(null, new ThemeResource("img/fulloverview.jpg"));
//        fullOverviewImgLink.setStyleName("fulloverview");
//        fullOverviewImgLink.setTargetName("_blank");
//        fullOverviewImgLink.setWidth("100%");
//        fullOverviewImgLink.setHeight((400) + "px");
//        rightLayout.addComponent(fullOverviewImgLink);
//        rightLayout.setMargin(new MarginInfo(true, true, true, true));
//        rightLayout.setComponentAlignment(fullOverviewImgLink, Alignment.MIDDLE_CENTER);
//        
        
        
        //admin lyout
//        bottomLayout.addComponent(adminIcon);
//        bottomLayout.setComponentAlignment(adminIcon, Alignment.BOTTOM_RIGHT);
//        bottomLayout.setExpandRatio(adminIcon, 0.05f);

        // the stat layout
        OverviewInfoBean infoBean = CSFPR_Handler.getResourceOverviewInformation();

        Label statLabel = new Label("<center><h1>Resource  Status</h1></center>");
        statLabel.setContentMode(ContentMode.HTML);
        statLabel.setWidth("100%");
        statLabel.setHeight("50px");
        leftLayout.addComponent(statLabel);

        Label quantStatLabel = new Label("<h2>Quantitative  Data</h2>");
        quantStatLabel.setContentMode(ContentMode.HTML);
        leftLayout.addComponent(quantStatLabel);

        GridLayout subQuantStatLayout = new GridLayout(2, 4);
        subQuantStatLayout.setWidth("100%");
        leftLayout.addComponent(subQuantStatLayout);

        List<Object[]> publicationList = CSFPR_Handler.getPublicationList();

        PublicationsInformationWindow sub1quantStatLabelWrapper = new PublicationsInformationWindow(publicationList);
        Label sub1quantStatLabel = new Label("<h3 style='text-decoration: underline;cursor: pointer;'>#Publications</h3>");
        sub1quantStatLabel.setContentMode(ContentMode.HTML);
        sub1quantStatLabelWrapper.addComponent(sub1quantStatLabel);
        subQuantStatLayout.addComponent(sub1quantStatLabelWrapper, 0, 0);

        sub1quantStatLabelWrapper.setDescription("Click to view publications information");

        Label sub1QuantStatValue = new Label("<h4 style='text-align: right;' >" + infoBean.getNumberOfQuantPublication() + "</h4>");
        sub1QuantStatValue.setContentMode(ContentMode.HTML);
        subQuantStatLayout.addComponent(sub1QuantStatValue, 1, 0);
        subQuantStatLayout.setComponentAlignment(sub1QuantStatValue, Alignment.MIDDLE_RIGHT);

        Set<QuantDatasetObject> dsObjects = CSFPR_Handler.getQuantDatasetList();

        StudiesInformationWindow sub2quantStatLabelWrapper = new StudiesInformationWindow(dsObjects, CSFPR_Handler.getDiseaseHashedColorMap());
        subQuantStatLayout.addComponent(sub2quantStatLabelWrapper, 0, 1);
        sub2quantStatLabelWrapper.setDescription("Click to view datasets information");

        Label sub2quantStatLabel = new Label("<h3 style='text-decoration: underline;cursor: pointer;'>#Datasets</h3>");
        sub2quantStatLabel.setContentMode(ContentMode.HTML);
        sub2quantStatLabelWrapper.addComponent(sub2quantStatLabel);

        Label sub2QuantStatValue = new Label("<h4 style='text-align: right;'>" + infoBean.getNumberOfQuantStudies() + "</h4>");
        sub2QuantStatValue.setContentMode(ContentMode.HTML);
        subQuantStatLayout.addComponent(sub2QuantStatValue, 1, 1);
        subQuantStatLayout.setComponentAlignment(sub2QuantStatValue, Alignment.MIDDLE_RIGHT);

        Label sub3quantStatLabel = new Label("<h3>#Proteins</h3>");
        sub3quantStatLabel.setContentMode(ContentMode.HTML);
        subQuantStatLayout.addComponent(sub3quantStatLabel, 0, 2);

        Label sub3QuantStatValue = new Label("<h4 style='text-align: right;'>" + infoBean.getNumberOfQuantProteins() + "</h4>");
        sub3QuantStatValue.setContentMode(ContentMode.HTML);
        subQuantStatLayout.addComponent(sub3QuantStatValue, 1, 2);
        subQuantStatLayout.setComponentAlignment(sub3QuantStatValue, Alignment.MIDDLE_RIGHT);

        Label sub4quantStatLabel = new Label("<h3>#Peptides</h3>");
        sub4quantStatLabel.setContentMode(ContentMode.HTML);
        subQuantStatLayout.addComponent(sub4quantStatLabel, 0, 3);

        Label sub4QuantStatValue = new Label("<h4 style='text-align: right;'>" + infoBean.getNumberOfQuantPeptides() + "</h4>");
        sub4QuantStatValue.setContentMode(ContentMode.HTML);
        subQuantStatLayout.addComponent(sub4QuantStatValue, 1, 3);
        subQuantStatLayout.setComponentAlignment(sub4QuantStatValue, Alignment.MIDDLE_RIGHT);

        subQuantStatLayout.setColumnExpandRatio(0, 2);
        subQuantStatLayout.setColumnExpandRatio(1, 1);

        Label idStatLabel = new Label("<h2>Identification Data</h2>");
        idStatLabel.setContentMode(ContentMode.HTML);
        leftLayout.addComponent(idStatLabel);

        GridLayout subIdStatLayout = new GridLayout(2, 4);
        subIdStatLayout.setWidth("100%");
        leftLayout.addComponent(subIdStatLayout);

        Label sub2IdStatLabel = new Label("<h3>#Datasets</h3>");
        sub2IdStatLabel.setContentMode(ContentMode.HTML);
        subIdStatLayout.addComponent(sub2IdStatLabel, 0, 1);
        subIdStatLayout.setColumnExpandRatio(0, 2);
        subIdStatLayout.setColumnExpandRatio(1, 1);

        Label sub2IdStatValue = new Label("<h4 style='text-align: right;'>" + infoBean.getNumberOfIdStudies() + "</h4>");
        sub2IdStatValue.setContentMode(ContentMode.HTML);
        subIdStatLayout.addComponent(sub2IdStatValue, 1, 1);
        subIdStatLayout.setComponentAlignment(sub2IdStatValue, Alignment.MIDDLE_RIGHT);

        Label sub3IdStatLabel = new Label("<h3>#Proteins</h3>");
        sub3IdStatLabel.setContentMode(ContentMode.HTML);
        subIdStatLayout.addComponent(sub3IdStatLabel, 0, 2);

        Label sub3IdStatValue = new Label("<h4 style='text-align: right;'>" + infoBean.getNumberOfIdProteins() + "</h4>");
        sub3IdStatValue.setContentMode(ContentMode.HTML);
        subIdStatLayout.addComponent(sub3IdStatValue, 1, 2);
        subIdStatLayout.setComponentAlignment(sub3IdStatValue, Alignment.MIDDLE_RIGHT);

        Label sub4IdStatLabel = new Label("<h3>#Peptides</h3>");
        sub4IdStatLabel.setContentMode(ContentMode.HTML);
        subIdStatLayout.addComponent(sub4IdStatLabel, 0, 3);

        Label sub4IdStatValue = new Label("<h4 style='text-align: right;'>" + infoBean.getNumberOfIdPeptides() + "</h4>");
        sub4IdStatValue.setContentMode(ContentMode.HTML);
        subIdStatLayout.addComponent(sub4IdStatValue, 1, 3);
        subIdStatLayout.setComponentAlignment(sub4IdStatValue, Alignment.MIDDLE_RIGHT);
//        
    }
}
