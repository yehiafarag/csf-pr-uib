/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.studies;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import probe.com.model.beans.quant.QuantDatasetObject;
import probe.com.model.beans.quant.QuantPeptide;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.DiseaseGroupsComparisonsProteinLayout;
import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.peptidescontainer.PeptideSequanceLocationOverview;
import probe.com.view.core.HideOnClickLayout;
import probe.com.view.core.InformationField;

/**
 *
 * @author yfa041
 */
public class StudyPopupLayout extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private final Map<VerticalLayout, VerticalLayout> tabLayoutMap = new HashMap<VerticalLayout, VerticalLayout>();
    private final VerticalLayout bottomLayout, peptidesInformationContainer;
    private final GridLayout topLayout;
    private final HideOnClickLayout peptideProteinInfoHideShow;

    public StudyPopupLayout(int width) {

        this.setWidthUndefined();
        int height = Page.getCurrent().getBrowserWindowHeight() - 200;
        this.setHeightUndefined();
        this.setStyleName(Reindeer.LAYOUT_WHITE);
        this.setMargin(false);
        this.setSpacing(true);
        int colNum = Math.max(1, width / 200);

        topLayout = new GridLayout(colNum, 3);
        topLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        
        topLayout.setHeightUndefined();
        topLayout.setSpacing(true);

        bottomLayout = new VerticalLayout();
        bottomLayout.setSpacing(true);
        this.addComponent(topLayout);
        bottomLayout.setMargin(new MarginInfo(true, false, false, false));
        this.addComponent(bottomLayout);
        bottomLayout.setWidth((width) + "px");
//        bottomLayout.setHeight(height+"px");
        bottomLayout.setStyleName("borderlayout");

        peptidesInformationContainer = this.initPeptidesInformationContainer(width);

        peptideProteinInfoHideShow = new HideOnClickLayout("Peptides Information", peptidesInformationContainer, null,Alignment.TOP_CENTER);
        peptideProteinInfoHideShow.setHeightUndefined();
        bottomLayout.addComponent(peptideProteinInfoHideShow);
        peptideProteinInfoHideShow.setVisability(true);

        VerticalLayout proteinsInformationContainer = this.initPeptidesInformationContainer(width);

        HideOnClickLayout proteinInfoHideShow = new HideOnClickLayout("Proteins Information", proteinsInformationContainer, null);

        bottomLayout.addComponent(proteinInfoHideShow);
        proteinInfoHideShow.setVisability(true);

        GridLayout datasetLayout = initQuantDatasetInformationLayout();
        HideOnClickLayout datasetInfoLayout = new HideOnClickLayout("Dataset Information", datasetLayout, null);
        datasetInfoLayout.setMargin(new MarginInfo(false, false, false, false));
        bottomLayout.addComponent(datasetInfoLayout);
        datasetInfoLayout.setVisability(true);

        bottomLayout.setVisible(false);

//        TabSheet studiesTabsheet = new TabSheet();
//        studiesTabsheet.addTab(new VerticalLayout(),"tab1");
//        studiesTabsheet.addTab(new VerticalLayout(),"tab2");
//        studiesTabsheet.addTab(new VerticalLayout(),"tab3");
//        studiesTabsheet.addTab(new VerticalLayout(),"tab4");
//       
//        this.addComponent(studiesTabsheet);
    }

    private VerticalLayout generateBtn(int dsKey,String protAccession, String btnName) {
        VerticalLayout btn = new VerticalLayout();
        btn.addLayoutClickListener(this);
        btn.setHeight("50px");
        btn.setWidth("200px");
        Label btnLabel = new Label(btnName);
        btn.addComponent(btnLabel);
        btn.setComponentAlignment(btnLabel, Alignment.MIDDLE_CENTER);
        btn.setStyleName("tabbtn");
        btn.setData("-" + dsKey + "-"+protAccession+"-");

        return btn;
    }

    private VerticalLayout lastSelectedBtn;

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {

        if (lastSelectedBtn != null) {
            lastSelectedBtn.setStyleName("tabbtn");
        }
        VerticalLayout selectedBtn;
        if (event.getClickedComponent() == null) {
            selectedBtn = (VerticalLayout) event.getComponent();

        } else if (event.getClickedComponent() instanceof Label) {
            selectedBtn = (VerticalLayout) event.getClickedComponent().getParent();
        } else {
            selectedBtn = (VerticalLayout) event.getClickedComponent();
        }
        if (selectedBtn == lastSelectedBtn) {
            bottomLayout.setVisible(false);
            lastSelectedBtn=null;
            return;
        }
        this.selectStudyBtn(selectedBtn);
    }
    
    private void selectStudyBtn(VerticalLayout selectedBtn){
     

        lastSelectedBtn = selectedBtn;
        lastSelectedBtn.setStyleName("selectedtabbtn");
        updatePeptideLayout(lastSelectedBtn.getData().toString());


        
//                update prot info
//                        update datasetinfo
        bottomLayout.setVisible(true);
    
    }

    public void setInformationData(Set<QuantDatasetObject> dsObjects, DiseaseGroupsComparisonsProteinLayout cp) {
        tabLayoutMap.clear();
        topLayout.removeAllComponents();

        int colcounter = 0;
        int rowcounter = 0;
        for (QuantDatasetObject quantDS : dsObjects) {
            VerticalLayout btn = this.generateBtn(quantDS.getDsKey(),cp.getProteinAccssionNumber(), quantDS.getAuthor() + " (" + quantDS.getYear() + ")");
            
            topLayout.addComponent(btn, colcounter++, rowcounter);
            if (colcounter >= topLayout.getColumns()) {
                colcounter = 0;
                rowcounter++;

            }

        }
        
        int subWidth = (int) ((float) bottomLayout.getWidth() * 0.9);
        this.initPeptideCoverageLayout(cp, subWidth);        
        this.selectStudyBtn((VerticalLayout)topLayout.getComponent(0, 0));

    }
    private final Map<String, PeptideSequanceLocationOverview> stackedBarChartCompDSIndexMap = new HashMap<String, PeptideSequanceLocationOverview>();

    private void initPeptideCoverageLayout(DiseaseGroupsComparisonsProteinLayout cp, int subWidth) {
        stackedBarChartCompDSIndexMap.clear();
       
        Map<Integer, Set<QuantPeptide>> dsQuantPepMap = new HashMap<Integer, Set<QuantPeptide>>();
        for (QuantPeptide quantPep : cp.getQuantPeptidesList()) {
           
            if (!dsQuantPepMap.containsKey(quantPep.getDsKey())) {
                Set<QuantPeptide> subList = new HashSet<QuantPeptide>();
                dsQuantPepMap.put(quantPep.getDsKey(), subList);
            }
            Set<QuantPeptide> subList = dsQuantPepMap.get(quantPep.getDsKey());
            subList.add(quantPep);
              dsQuantPepMap.put(quantPep.getDsKey(), subList);
        } 
        
      
        for (int dsID : dsQuantPepMap.keySet()) {      
            PeptideSequanceLocationOverview peptideStackedBarChart = new PeptideSequanceLocationOverview(cp.getSequance(), dsQuantPepMap.get(dsID), subWidth);
            stackedBarChartCompDSIndexMap.put("-" + dsID + "-"+cp.getProteinAccssionNumber()+"-", peptideStackedBarChart);
        }

    }

    private InformationField pumedId, rawData, analyticalMethod, typeOfStudy, shotgunTargeted, enzyme, sampleType, technology, quantificationBasis, patientsGroup1Number, patientsGroup2Number, patientsGroup1, patientsGroup2, patientssubGroup1, patientsCommGroup1, patientssubGroup2, patientsCommGroup2, identifiedProteinsNumber, quantifiedProteinsNumber, sampleMatching, quantBasisComment, analyticalApproach, normalization_strategy;

    private VerticalLayout initPeptidesInformationContainer(int width) {
        VerticalLayout generatedPeptidesInformationContainer = new VerticalLayout();
        generatedPeptidesInformationContainer.setWidth(width - 10 + "px");
        generatedPeptidesInformationContainer.setHeightUndefined();//300+"px");

        return generatedPeptidesInformationContainer;

    }

    private GridLayout initQuantDatasetInformationLayout() {

        GridLayout mainBody = new GridLayout(4, 7);
        mainBody.setSpacing(true);
        mainBody.setMargin(false);

        pumedId = new InformationField("Pumed Id");
        mainBody.addComponent(pumedId, 0, 0);

        rawData = new InformationField("Raw Data");
        mainBody.addComponent(rawData, 1, 0);

        analyticalMethod = new InformationField("Analytical Method");
        mainBody.addComponent(analyticalMethod, 2, 0);

        typeOfStudy = new InformationField("Type of Study");
        mainBody.addComponent(typeOfStudy, 3, 0);

        shotgunTargeted = new InformationField("Shotgun/Targeted");
        mainBody.addComponent(shotgunTargeted, 0, 1);

        enzyme = new InformationField("Enzyme");
        mainBody.addComponent(enzyme, 1, 1);

        sampleType = new InformationField("Sample Type");
        mainBody.addComponent(sampleType, 2, 1);

        technology = new InformationField("Technology");
        mainBody.addComponent(technology, 3, 1);

        patientsGroup1 = new InformationField("Patients Gr.I");
        mainBody.addComponent(patientsGroup1, 0, 2);

        patientssubGroup1 = new InformationField("Patients Sub Gr.I");
        mainBody.addComponent(patientssubGroup1, 1, 2);

        patientsGroup1Number = new InformationField("#Patients Gr.I");
        mainBody.addComponent(patientsGroup1Number, 2, 2);

        patientsCommGroup1 = new InformationField("Patients Gr.I Comm.");
        mainBody.addComponent(patientsCommGroup1, 3, 2);

        patientsGroup2 = new InformationField("Patients Gr.II");
        mainBody.addComponent(patientsGroup2, 0, 3);

        patientssubGroup2 = new InformationField("Patients Sub Gr.II");
        mainBody.addComponent(patientssubGroup2, 1, 3);

        patientsGroup2Number = new InformationField("#Patients Gr.II");
        mainBody.addComponent(patientsGroup2Number, 2, 3);

        patientsCommGroup2 = new InformationField("Patients Gr.II Comm.");
        mainBody.addComponent(patientsCommGroup2, 3, 3);

        quantificationBasis = new InformationField("Quantification Basis");
        mainBody.addComponent(quantificationBasis, 0, 4);

        identifiedProteinsNumber = new InformationField("# Identified Proteins");
        mainBody.addComponent(identifiedProteinsNumber, 1, 4);

        quantifiedProteinsNumber = new InformationField("# Quantified Proteins");
        mainBody.addComponent(quantifiedProteinsNumber, 2, 4);

        sampleMatching = new InformationField("Sample Matching");
        mainBody.addComponent(sampleMatching, 3, 4);

        analyticalApproach = new InformationField("Analytical Approach");
        mainBody.addComponent(analyticalApproach, 0, 5);

        quantBasisComment = new InformationField("Quantification BasisComment");
        mainBody.addComponent(quantBasisComment, 1, 5);
//
        normalization_strategy = new InformationField("Normalization Strategy");
        mainBody.addComponent(normalization_strategy, 2, 5);

        return mainBody;
    }

    private void updatePeptideLayout(String protStudyKey) {
        peptidesInformationContainer.removeAllComponents();
       for (String key : this.stackedBarChartCompDSIndexMap.keySet()) {
            if (key.equalsIgnoreCase(protStudyKey)) {                
                peptidesInformationContainer.addComponent(stackedBarChartCompDSIndexMap.get(key));
                peptidesInformationContainer.setComponentAlignment(stackedBarChartCompDSIndexMap.get(key),Alignment.TOP_CENTER);
                 peptideProteinInfoHideShow.setVisible(true);
                break;

            }

        }
       if(peptidesInformationContainer.getComponentCount() ==0)
             peptideProteinInfoHideShow.setVisible(false);
    }

}
