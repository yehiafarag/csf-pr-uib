/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body;

import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;

/**
 *
 * @author Yehia Farag
 *
 * static layout has main project information and access to admin login
 */
public class WelcomeLayout extends VerticalLayout implements Serializable {

    /**
     * initialize body layout
     *
     * @param adminIcon the access button for admin Layout
     *
     *
     */
    public WelcomeLayout(Button adminIcon) {

        int fullWidth = Page.getCurrent().getBrowserWindowWidth();
        this.setWidth(fullWidth + "px");
        VerticalLayout mainBodyHLayout = new VerticalLayout();
        mainBodyHLayout.setWidth("100%");
        this.addComponent(mainBodyHLayout);
        this.setHeight((Page.getCurrent().getBrowserWindowHeight() - 100) + "px");

        HorizontalLayout mainBody = new HorizontalLayout();
        mainBody.setWidthUndefined();
        mainBody.setHeightUndefined();
        mainBodyHLayout.addComponent(mainBody);
        mainBody.setStyleName("bottomborder");

        VerticalLayout leftLayout = new VerticalLayout();
        leftLayout.setWidth("300px");
        leftLayout.setHeightUndefined();
        leftLayout.setMargin(true);
        leftLayout.setStyleName("framelayout");
        leftLayout.setSpacing(true);
        leftLayout.setMargin(new MarginInfo(true, true, true, true));
        mainBody.addComponent(leftLayout);
        mainBody.setComponentAlignment(leftLayout, Alignment.TOP_LEFT);

        int layoutWidth = (fullWidth - 300);
        HorizontalLayout rightPanel = new HorizontalLayout();
        rightPanel.setWidth(layoutWidth + "px");
        mainBody.addComponent(rightPanel);

        VerticalLayout bottomLayout = new VerticalLayout();
        bottomLayout.setWidth("100%");
        bottomLayout.setSpacing(true);
        bottomLayout.setMargin(new MarginInfo(true, true, true, true));
        mainBodyHLayout.addComponent(bottomLayout);
        mainBodyHLayout.setComponentAlignment(bottomLayout, Alignment.BOTTOM_CENTER);

        VerticalLayout middleLayout = new VerticalLayout();
        middleLayout.setWidth("100%");
        middleLayout.setMargin(new MarginInfo(true, true, true, false));
        rightPanel.addComponent(middleLayout);

        VerticalLayout rightLayout = new VerticalLayout();
        rightLayout.setWidth("100%");
        rightPanel.addComponent(rightLayout);

        Label infoLable = new Label("<h2 style='margin-left:40px;font-family:Verdana;'>Welcome to CSF Proteome Resource (CSF-PR)</h2>");
        infoLable.setContentMode(ContentMode.HTML);
        middleLayout.addComponent(infoLable);
        middleLayout.setComponentAlignment(infoLable, Alignment.MIDDLE_LEFT);

        Label para_1 = new Label("<p align='justify' Style='margin-left:40px;color:#585858;'><font size=\"2\">CSF Proteome Resource (CSF-PR) is an online repository of mass spectrometry based proteomics "
                + "experiments on human cerebrospinal fluid (CSF). CSF is in direct contact with the central nervous"
                + "system (CNS) and can give indications about the state of the CNS.This is particularly relevant for "
                + "neurodegenerative diseases, such as Multiple Sclerosis, where CSF would be a natural place to look "
                + "for disease biomarkers.</font></p>");

        para_1.setContentMode(ContentMode.HTML);
        middleLayout.addComponent(para_1);

        Label para_2 = new Label("<p align='justify' Style='margin-left:40px;color:#585858;'><font size=\"2\">The data can be viewed by selecting individual experiments or by searching for key words (protein "
                + "name/accession number or peptide sequence) across all experiments. For GeLC-MS experiments the "
                + "distribution of the identified proteins in the gel is also displayed.</font></p>");

        para_2.setContentMode(ContentMode.HTML);
        middleLayout.addComponent(para_2);

        ThemeResource img1 = new ThemeResource("img/overview.jpg");
        Link fullOverviewImgLink = new Link(null, new ThemeResource("img/fulloverview.jpg"));
//        fullOverviewImgLink.setIcon(img1);
        fullOverviewImgLink.setStyleName("fulloverview");
        fullOverviewImgLink.setTargetName("_blank");
        fullOverviewImgLink.setWidth("100%");
        fullOverviewImgLink.setHeight((400) + "px");
        rightLayout.addComponent(fullOverviewImgLink);
        rightLayout.setMargin(new MarginInfo(true, true, true, true));
        rightLayout.setComponentAlignment(fullOverviewImgLink, Alignment.MIDDLE_CENTER);

        Label para_3 = new Label("<p align='justify' Style='margin-left:40px;color:#585858;'><font size=\"2\">CSF-PR is being developed by the <a Style='color:#585858;' href='http://www.uib.no/rg/probe' target=\"_blank\">Proteomics Unit</a> at the<a Style='color:#585858;' href='http://www.uib.no/biomedisin/en' target=\"_blank\"> Department of Biomedicine at the University of Bergen</a>, Norway, in close collaboration with <a Style='color:#585858;' href='http://haukeland.no/en/OmOss/Avdelinger/ms/Sider/om-oss.aspx' target=\"_blank\">The Norwegian Multiple Sclerosis Competence Centre</a>, Haukeland University Hospital, Bergen, Norway.</font></p>");
        para_3.setContentMode(ContentMode.HTML);
        middleLayout.addComponent(para_3);

        Label para_4 = new Label("<p align='justify' Style='margin-left:40px;color:#585858;'><font size=\"2\">See also: <a Style='color:#585858;' href='http://www.mcponline.org/content/13/11/3152.full.pdf+html' target=\"_blank\">Guldbrandsen et al.: In-depth Characterization of the Cerebrospinal Fluid (CSF) Proteome Displayed Through the CSF Proteome Resource (CSF-PR). Mol Cell Proteomics. 2014.</a></font></p>");
        para_4.setContentMode(ContentMode.HTML);
        middleLayout.addComponent(para_4);

        bottomLayout.addComponent(adminIcon);
        bottomLayout.setComponentAlignment(adminIcon, Alignment.BOTTOM_RIGHT);
        bottomLayout.setExpandRatio(adminIcon, 0.05f);

        // the stat layout
        Label statLabel = new Label("<h1>Resource  Status</h1>");
        statLabel.setContentMode(ContentMode.HTML);
        statLabel.setWidth("100%");
        statLabel.setHeight("50px");
        leftLayout.addComponent(statLabel);

        Label idStatLabel = new Label("<h2>Identification Data</h2>");
        idStatLabel.setContentMode(ContentMode.HTML);
        leftLayout.addComponent(idStatLabel);

        Label sub1IdStatLabel = new Label("<h3>#Publications:(20)</h3>");
        sub1IdStatLabel.setContentMode(ContentMode.HTML);
        leftLayout.addComponent(sub1IdStatLabel);

        Label sub2IdStatLabel = new Label("<h3>#Studies:(50)</h3>");
        sub2IdStatLabel.setContentMode(ContentMode.HTML);
        leftLayout.addComponent(sub2IdStatLabel);

        Label sub3IdStatLabel = new Label("<h3>#Identified Proteins:(50000)</h3>");
        sub3IdStatLabel.setContentMode(ContentMode.HTML);
        leftLayout.addComponent(sub3IdStatLabel);

        Label sub4IdStatLabel = new Label("<h3>#Identified Peptides:(50)</h3>");
        sub4IdStatLabel.setContentMode(ContentMode.HTML);
        leftLayout.addComponent(sub4IdStatLabel);

        Label quantStatLabel = new Label("<h2>Quantitative  Data</h2>");
        quantStatLabel.setContentMode(ContentMode.HTML);
        leftLayout.addComponent(quantStatLabel);

        Label sub1quantStatLabel = new Label("<h3>#Publications:(20)</h3>");
        sub1quantStatLabel.setContentMode(ContentMode.HTML);
        leftLayout.addComponent(sub1quantStatLabel);

        Label sub2quantStatLabel = new Label("<h3>#Studies:(50)</h3>");
        sub2quantStatLabel.setContentMode(ContentMode.HTML);
        leftLayout.addComponent(sub2quantStatLabel);

        Label sub3quantStatLabel = new Label("<h3>#Quantified  Proteins:(50000)</h3>");
        sub3quantStatLabel.setContentMode(ContentMode.HTML);
        leftLayout.addComponent(sub3quantStatLabel);

        Label sub4quantStatLabel = new Label("<h3>#Quantified Peptides:(50)</h3>");
        sub4quantStatLabel.setContentMode(ContentMode.HTML);
        leftLayout.addComponent(sub4quantStatLabel);

//        
//        VerticalLayout idStatLayout = new VerticalLayout();
//        leftLayout.addComponent(idStatLayout);
//        leftLayout.setComponentAlignment(idStatLayout, Alignment.MIDDLE_CENTER);
//        idStatLayout.setWidth("90%");
//        idStatLayout.setHeight("450px");
//        idStatLayout.setStyleName(Reindeer.LAYOUT_BLUE);
//        
//        Label idLabel = new Label("Protein identification data");
//        idLabel.setStyleName(Reindeer.LABEL_H2);
//        idStatLayout.addComponent(idLabel);
//        
//        
//        
//        
//        
//        
//        VerticalLayout quantStatLayout = new VerticalLayout();
//        leftLayout.addComponent(quantStatLayout);
//        leftLayout.setComponentAlignment(quantStatLayout, Alignment.MIDDLE_CENTER);
//        quantStatLayout.setWidth("90%");
//        quantStatLayout.setHeight("450px");
//        quantStatLayout.setStyleName(Reindeer.LAYOUT_BLUE);
//        
    }
}
