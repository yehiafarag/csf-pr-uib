package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDataset;

/**
 *
 * @author Yehia Farag
 */
public class DatasetInformationOverviewLayout extends VerticalLayout {

    private final String diseaseHashedColor;
    private final boolean smallScreen;

    /**
     * @param quantDs
     * @param diseaseHashedColor hashed color for disease
     */
    public DatasetInformationOverviewLayout(QuantDataset quantDs, boolean smallScreen) {
        this.diseaseHashedColor = quantDs.getDiseaseHashedColor();
//        int width = Page.getCurrent().getBrowserWindowWidth() - 320;
//        int height = Page.getCurrent().getBrowserWindowHeight() - 320;
//        this.setWidth(width ,Unit.PIXELS);
//        this.setHeight(height ,Unit.PIXELS);
        this.smallScreen = smallScreen;
        this.setWidth(100, Unit.PERCENTAGE);
        this.setHeight(100, Unit.PERCENTAGE);
        this.addStyleName("scrollable");
        if (!smallScreen) {
            this.setSpacing(true);
        }

        datasetInfoForm = initQuantDatasetInformationLayout();
        datasetInfoForm.setVisible(true);
        this.addComponent(datasetInfoForm);
        this.setComponentAlignment(datasetInfoForm, Alignment.TOP_LEFT);
        this.updateDatasetForm(quantDs);

    }

    public VerticalLayout getDatasetInfoForm() {
        return datasetInfoForm;
    }

    private final VerticalLayout datasetInfoForm;
    private InformationField  quantBasisComment, uQuantProteinsNum, uQuantPeptidesNum, quantProteinsNum, quantPeptidesNum, diseaseCategory, pumedId, rawData, analyticalMethod, typeOfStudy, shotgunTargeted, enzyme, sampleType, technology, quantificationBasis, patientsGroup1Number, patientsGroup2Number, patientsGroup1, patientsGroup2, patientssubGroup1, patientsCommGroup1, patientssubGroup2, patientsCommGroup2, identifiedProteinsNumber, quantifiedProteinsNumber, sampleMatching, analyticalApproach, normalization_strategy;

    private VerticalLayout initQuantDatasetInformationLayout() {

        VerticalLayout mainContainer = new VerticalLayout();
        mainContainer.setWidth(100, Unit.PERCENTAGE);
        mainContainer.setHeightUndefined();
        mainContainer.addStyleName("scrollable");
        int h = 80;
//        if (smallScreen) {
//            h = 80;
//        }

        mainContainer.setSpacing(true);

        HorizontalLayout rowI = new HorizontalLayout();
        rowI.setWidth(100, Unit.PERCENTAGE);
        rowI.setHeight(h, Unit.PIXELS);
        mainContainer.addComponent(rowI);
//        mainContainer.setExpandRatio(rowI, 10);

        pumedId = new InformationField("PubMed Id");
        rowI.addComponent(pumedId);

        typeOfStudy = new InformationField("Type of Study");
        rowI.addComponent(typeOfStudy);

        analyticalApproach = new InformationField("Analytical Approach");
        rowI.addComponent(analyticalApproach);

        shotgunTargeted = new InformationField("Shotgun/Targeted");
        rowI.addComponent(shotgunTargeted);

        HorizontalLayout rowII = new HorizontalLayout();
        rowII.setWidth(100, Unit.PERCENTAGE);
        rowII.setHeight(h, Unit.PIXELS);
        mainContainer.addComponent(rowII);
//         mainContainer.setExpandRatio(rowII, 10);

        analyticalMethod = new InformationField("Analytical Method");
        rowII.addComponent(analyticalMethod);

        technology = new InformationField("Technology");
        rowII.addComponent(technology);

        sampleType = new InformationField("Sample Type");
        rowII.addComponent(sampleType);

        enzyme = new InformationField("Enzyme");
        rowII.addComponent(enzyme);

        HorizontalLayout rowIII = new HorizontalLayout();
        rowIII.setWidth(100, Unit.PERCENTAGE);
        rowIII.setHeight(h, Unit.PIXELS);
        mainContainer.addComponent(rowIII);
//         mainContainer.setExpandRatio(rowI, 10);

        quantificationBasis = new InformationField("Quantification Basis");
        rowIII.addComponent(quantificationBasis);
        
        
        quantBasisComment = new InformationField("Quantification Basis Comment");
        rowIII.addComponent(quantBasisComment);
        

        identifiedProteinsNumber = new InformationField("#Identified Proteins");
        rowIII.addComponent(identifiedProteinsNumber);

        quantifiedProteinsNumber = new InformationField("#Quantified Proteins");
        rowIII.addComponent(quantifiedProteinsNumber);

        

        HorizontalLayout rowIV = new HorizontalLayout();
        rowIV.setWidth(100, Unit.PERCENTAGE);
//        if (smallScreen) {
            rowIV.setHeight(h, Unit.PIXELS);
//        }
        mainContainer.addComponent(rowIV);
//
        patientsGroup1 = new InformationField("Patients Gr.I");
        rowIV.addComponent(patientsGroup1);
//
        patientssubGroup1 = new InformationField("Patients Sub Gr.I");
        rowIV.addComponent(patientssubGroup1);
//
        patientsGroup1Number = new InformationField("#Patients Gr.I");
        rowIV.addComponent(patientsGroup1Number);
//
        patientsCommGroup1 = new InformationField("Patients Gr.I Comm.");
        patientsCommGroup1.setSmallScreen(smallScreen);
        rowIV.addComponent(patientsCommGroup1);

        HorizontalLayout rowV = new HorizontalLayout();
        rowV.setWidth(100, Unit.PERCENTAGE);
//        if (smallScreen) {
            rowV.setHeight(h, Unit.PIXELS);
//        } else {
//            rowV.setHeightUndefined();
//        }
        mainContainer.addComponent(rowV);
//
        patientsGroup2 = new InformationField("Patients Gr.II");
        rowV.addComponent(patientsGroup2);

        patientssubGroup2 = new InformationField("Patients Sub Gr.II");
        rowV.addComponent(patientssubGroup2);

        patientsGroup2Number = new InformationField("#Patients Gr.II");
        rowV.addComponent(patientsGroup2Number);

        patientsCommGroup2 = new InformationField("Patients Gr.II Comm.");
        patientsCommGroup2.setSmallScreen(smallScreen);
        rowV.addComponent(patientsCommGroup2);

        HorizontalLayout rowVI = new HorizontalLayout();
        rowVI.setWidth(100, Unit.PERCENTAGE);
//        if (smallScreen) {
            rowVI.setHeight(h, Unit.PIXELS);
//        } else {
//            rowVI.setHeightUndefined();
//        }
        mainContainer.addComponent(rowVI);

        
        sampleMatching = new InformationField("Sample Matching");
        rowVI.addComponent(sampleMatching);
        normalization_strategy = new InformationField("Normalization Strategy");
        normalization_strategy.setSmallScreen(smallScreen);
        rowVI.addComponent(normalization_strategy);

        rawData = new InformationField("Raw Data");
        rowVI.addComponent(rawData);

        diseaseCategory = new InformationField("Disease Category");
        rowVI.addComponent(diseaseCategory);

       
//

        HorizontalLayout rowVII = new HorizontalLayout();
        rowVII.setWidth(100, Unit.PERCENTAGE);
//        if (smallScreen) {
            rowVII.setHeight(h, Unit.PIXELS);
//        } else {
//            rowVII.setHeightUndefined();
//        } 
        quantProteinsNum = new InformationField("#Proteins");
        rowVII.addComponent(quantProteinsNum);
        mainContainer.addComponent(rowVII);

        quantPeptidesNum = new InformationField("#Peptides");
        rowVII.addComponent(quantPeptidesNum);

        uQuantProteinsNum = new InformationField("#Dataset Specific Proteins");
        rowVII.addComponent(uQuantProteinsNum);

        uQuantPeptidesNum = new InformationField("#Dataset Specific Peptides");
        rowVII.addComponent(uQuantPeptidesNum);

//        InformationField emptyField = new InformationField("");
//        rowVII.addComponent(emptyField);

        return mainContainer;
    }

    private void updateDatasetForm(QuantDataset quantDs) {
        if (quantDs == null) {
            return;
        }
        pumedId.setValue(quantDs.getPubMedId(), "http://www.ncbi.nlm.nih.gov/pubmed/" + quantDs.getPubMedId());
        if (quantDs.getRawDataUrl() == null || quantDs.getRawDataUrl().equalsIgnoreCase("Raw Data Not Available")) {
            rawData.setValue("Not Available", null);
        } else {
            rawData.setValue(quantDs.getRawDataUrl(), null);
        }
        analyticalMethod.setValue(quantDs.getAnalyticalMethod(), null);
        typeOfStudy.setValue(quantDs.getTypeOfStudy(), null);
        shotgunTargeted.setValue(quantDs.getShotgunTargeted(), null);
        enzyme.setValue(quantDs.getEnzyme(), null);

        sampleType.setValue(quantDs.getSampleType(), null);
        technology.setValue(quantDs.getTechnology(), null);

        quantificationBasis.setValue(quantDs.getQuantificationBasis(), null);
        patientsGroup1Number.setValue(quantDs.getDiseaseMainGroup1Number(), null);
        patientsGroup2Number.setValue(quantDs.getDiseaseMainGroup2Number(), null);
        patientsGroup1.setValue(quantDs.getDiseaseMainGroupI(), null);
        patientsGroup2.setValue(quantDs.getDiseaseMainGroup2() + "", null);

        patientssubGroup1.setValue(quantDs.getDiseaseSubGroup1(), null);
        patientsCommGroup1.setValue(quantDs.getDiseaseMainGroup1Comm(), null);

        patientssubGroup2.setValue(quantDs.getDiseaseSubGroup2(), null);
        patientsCommGroup2.setValue(quantDs.getDiseaseMainGroup2Comm(), null);

        identifiedProteinsNumber.setValue(quantDs.getIdentifiedProteinsNumber() + "", null);
        quantifiedProteinsNumber.setValue(quantDs.getQuantifiedProteinsNumber() + "", null);

        sampleMatching.setValue(quantDs.getSampleMatching() + "", null);
        quantBasisComment.setValue(quantDs.getQuantBasisComment() + "", null);
        analyticalApproach.setValue(quantDs.getAnalyticalApproach() + "", null);
        normalization_strategy.setValue(quantDs.getNormalizationStrategy(), null);

        diseaseCategory.setValue("<font color='" + diseaseHashedColor + "' style='font-weight: bold;'>" + quantDs.getDiseaseCategory() + "</font>", null);

        quantifiedProteinsNumber.setValue("" + quantDs.getQuantifiedProteinsNumber(), null);

        quantPeptidesNum.setValue("" + quantDs.getTotalPepNum() + "", null);
        quantProteinsNum.setValue("" + quantDs.getTotalProtNum() + "", null);
        uQuantPeptidesNum.setValue(quantDs.getUniqePepNum(), null);
        uQuantProteinsNum.setValue(quantDs.getUniqueProtNum(), null);
        this.datasetInfoForm.setVisible(true);
    }

}
