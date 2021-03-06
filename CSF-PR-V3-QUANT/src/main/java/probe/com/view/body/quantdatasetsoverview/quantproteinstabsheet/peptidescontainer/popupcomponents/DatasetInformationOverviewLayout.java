package probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.peptidescontainer.popupcomponents;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.Map;
import probe.com.model.beans.quant.QuantDatasetObject;
import probe.com.view.core.InformationField;

/**
 *
 * @author Yehia Farag
 */
public class DatasetInformationOverviewLayout extends VerticalLayout {

    private final Map<String, String> diseaseHashedColorMap;

    /**
     * @param width
     * @param diseaseHashedColorMap
     */
    public DatasetInformationOverviewLayout(int width, Map<String, String> diseaseHashedColorMap) {
        this.diseaseHashedColorMap = diseaseHashedColorMap;
        this.setWidth(width + "px");
        this.setHeightUndefined();
        this.setSpacing(true);
        this.setMargin(new MarginInfo(false, false, false, false));
        this.setStyleName(Reindeer.LAYOUT_WHITE);
        datasetInfoForm = initQuantDatasetInformationLayout(width);
        datasetInfoForm.setVisible(false);
        this.addComponent(datasetInfoForm);
        this.setComponentAlignment(datasetInfoForm, Alignment.MIDDLE_CENTER);

    }

    private final GridLayout datasetInfoForm;
    private InformationField uQuantProteinsNum, uQuantPeptidesNum,quantProteinsNum, quantPeptidesNum, diseaseCategory, pumedId, rawData, analyticalMethod, typeOfStudy, shotgunTargeted, enzyme, sampleType, technology, quantificationBasis, patientsGroup1Number, patientsGroup2Number, patientsGroup1, patientsGroup2, patientssubGroup1, patientsCommGroup1, patientssubGroup2, patientsCommGroup2, identifiedProteinsNumber, quantifiedProteinsNumber, sampleMatching, analyticalApproach, normalization_strategy;

    private GridLayout initQuantDatasetInformationLayout(int width) {

//        int width = 800;
        GridLayout datasetInfoFormLayout = new GridLayout(4, 7);
        datasetInfoFormLayout.setWidth(width + "px");
        datasetInfoFormLayout.setHeightUndefined();
        datasetInfoFormLayout.setSpacing(true);        
        datasetInfoFormLayout.setMargin(new MarginInfo(false, true, false, false));

        pumedId = new InformationField("Pumed Id");
        datasetInfoFormLayout.addComponent(pumedId, 0, 0);

        typeOfStudy = new InformationField("Type of Study");
        datasetInfoFormLayout.addComponent(typeOfStudy, 1, 0);

        analyticalApproach = new InformationField("Analytical Approach");
        datasetInfoFormLayout.addComponent(analyticalApproach, 2, 0);

        shotgunTargeted = new InformationField("Shotgun/Targeted");
        datasetInfoFormLayout.addComponent(shotgunTargeted, 3, 0);

        analyticalMethod = new InformationField("Analytical Method");
        datasetInfoFormLayout.addComponent(analyticalMethod, 0, 1);

        technology = new InformationField("Technology");
        datasetInfoFormLayout.addComponent(technology, 1, 1);

        sampleType = new InformationField("Sample Type");
        datasetInfoFormLayout.addComponent(sampleType, 2, 1);

        enzyme = new InformationField("Enzyme");
        datasetInfoFormLayout.addComponent(enzyme, 3, 1);

        quantificationBasis = new InformationField("Quantification Basis");
        datasetInfoFormLayout.addComponent(quantificationBasis, 0, 2);

        identifiedProteinsNumber = new InformationField("#Identified Proteins");
        datasetInfoFormLayout.addComponent(identifiedProteinsNumber, 1, 2);

        quantifiedProteinsNumber = new InformationField("#Quantified Proteins");
        datasetInfoFormLayout.addComponent(quantifiedProteinsNumber, 2, 2);

        sampleMatching = new InformationField("Sample Matching");
        datasetInfoFormLayout.addComponent(sampleMatching, 3, 2);

        patientsGroup1 = new InformationField("Patients Gr.I");
        datasetInfoFormLayout.addComponent(patientsGroup1, 0, 3);

        patientssubGroup1 = new InformationField("Patients Sub Gr.I");
        datasetInfoFormLayout.addComponent(patientssubGroup1, 1, 3);

        patientsGroup1Number = new InformationField("#Patients Gr.I");
        datasetInfoFormLayout.addComponent(patientsGroup1Number, 2, 3);

        patientsCommGroup1 = new InformationField("Patients Gr.I Comm.");
        datasetInfoFormLayout.addComponent(patientsCommGroup1, 3, 3);

        patientsGroup2 = new InformationField("Patients Gr.II");
        datasetInfoFormLayout.addComponent(patientsGroup2, 0, 4);

        patientssubGroup2 = new InformationField("Patients Sub Gr.II");
        datasetInfoFormLayout.addComponent(patientssubGroup2, 1, 4);

        patientsGroup2Number = new InformationField("#Patients Gr.II");
        datasetInfoFormLayout.addComponent(patientsGroup2Number, 2, 4);

        patientsCommGroup2 = new InformationField("Patients Gr.II Comm.");
        datasetInfoFormLayout.addComponent(patientsCommGroup2, 3, 4);

        normalization_strategy = new InformationField("Normalization Strategy");
        datasetInfoFormLayout.addComponent(normalization_strategy, 0, 5);

        rawData = new InformationField("Raw Data");
        datasetInfoFormLayout.addComponent(rawData, 1, 5);

        diseaseCategory = new InformationField("Disease Category");
        datasetInfoFormLayout.addComponent(diseaseCategory, 2, 5);

        quantProteinsNum = new InformationField("#Proteins");
        datasetInfoFormLayout.addComponent(quantProteinsNum, 3, 5);

        quantPeptidesNum = new InformationField("#Peptides");
        datasetInfoFormLayout.addComponent(quantPeptidesNum, 0, 6);
        
         uQuantProteinsNum = new InformationField("#Dataset Specific Proteins");
        datasetInfoFormLayout.addComponent(uQuantProteinsNum, 1, 6);
        
        uQuantPeptidesNum = new InformationField("#Dataset Specific Peptides");
        datasetInfoFormLayout.addComponent(uQuantPeptidesNum, 2, 6);
        
       

//        quantBasisComment = new InformationField("Quantification BasisComment");
//        datasetInfoFormLayout.addComponent(quantBasisComment, 1, 5);
        return datasetInfoFormLayout;
    }

    public void updateDatasetForm(QuantDatasetObject dataset) {
        if (dataset == null) {
            return;
        }
        pumedId.setValue(dataset.getPumedID(), "http://www.ncbi.nlm.nih.gov/pubmed/" + dataset.getPumedID());
        if (dataset.getRawDataUrl() == null || dataset.getRawDataUrl().equalsIgnoreCase("Raw Data Not Available")) {
            rawData.setValue("Not Available", null);
        } else {
            rawData.setValue(dataset.getRawDataUrl(), dataset.getRawDataUrl());
        }
        analyticalMethod.setValue(dataset.getAnalyticalMethod(), null);
        typeOfStudy.setValue(dataset.getTypeOfStudy(), null);
        shotgunTargeted.setValue(dataset.getShotgunTargeted(), null);
        enzyme.setValue(dataset.getEnzyme(), null);

        sampleType.setValue(dataset.getSampleType(), null);
        technology.setValue(dataset.getTechnology(), null);

        quantificationBasis.setValue(dataset.getQuantificationBasis(), null);
        patientsGroup1Number.setValue(dataset.getPatientsGroup1Number(), null);
        patientsGroup2Number.setValue(dataset.getPatientsGroup2Number(), null);
        patientsGroup1.setValue(dataset.getPatientsGroup1().split("\n")[0], null);
        patientsGroup2.setValue(dataset.getPatientsGroup2().split("\n")[0] + "", null);

        patientssubGroup1.setValue(dataset.getPatientsSubGroup1().split("\n")[0], null);
        patientsCommGroup1.setValue(dataset.getPatientsGroup1Comm(), null);

        patientssubGroup2.setValue(dataset.getPatientsSubGroup2().split("\n")[0], null);
        patientsCommGroup2.setValue(dataset.getPatientsGroup2Comm(), null);

        identifiedProteinsNumber.setValue(dataset.getIdentifiedProteinsNumber() + "", null);
        quantifiedProteinsNumber.setValue(dataset.getQuantifiedProteinsNumber() + "", null);

        sampleMatching.setValue(dataset.getSampleMatching() + "", null);
//        quantBasisComment.setValue(dataset.getQuantBasisComment() + "", null);
        analyticalApproach.setValue(dataset.getAnalyticalApproach() + "", null);
        normalization_strategy.setValue(dataset.getNormalizationStrategy(), null);

        String diseaseColor = this.diseaseHashedColorMap.get(dataset.getPatientsGroup1().split("\n")[1]);
        diseaseCategory.setValue("<font color='" + diseaseColor + "' style='font-weight: bold;'>" + dataset.getPatientsGroup1().split("\n")[1].replace("_", " ").replace("-", "'").replace("Disease", "") + "</font>", null);

        quantifiedProteinsNumber.setValue("" + dataset.getQuantifiedProteinsNumber(), null);

        quantPeptidesNum.setValue("" + dataset.getTotalPepNum() + "", null);
        quantProteinsNum.setValue("" + dataset.getTotalProtNum() + "", null);
        uQuantPeptidesNum.setValue(dataset.getUniqePepNum(), null);
        uQuantProteinsNum.setValue(dataset.getUniqueProtNum(), null);
        this.datasetInfoForm.setVisible(true);
    }

}
