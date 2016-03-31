package probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.studies;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import probe.com.model.beans.quant.QuantDatasetObject;
import probe.com.model.beans.quant.QuantPeptide;
import probe.com.model.beans.quant.QuantProtein;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.DiseaseGroupsComparisonsProteinLayout;
import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.peptidescontainer.popupcomponents.DatasetInformationOverviewLayout;
import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.peptidescontainer.popupcomponents.PeptidesInformationOverviewLayout;
import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.peptidescontainer.popupcomponents.ProteinsInformationOverviewLayout;
import probe.com.view.core.HideOnClickLayout;

/**
 *
 * @author Yehia Farag
 */
public class StudyPopupLayout extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private final VerticalLayout bottomLayout, peptidesInformationContainer, proteinsInformationContainer, datasetsInformationContainer;
    private final GridLayout topLayout;
    private final HideOnClickLayout peptideProteinInfoHideShow;
    private VerticalLayout lastSelectedBtn;
    private final Map<String, PeptidesInformationOverviewLayout> peptidesInfoLayoutDSIndexMap = new HashMap<String, PeptidesInformationOverviewLayout>();
    private final Map<String, ProteinsInformationOverviewLayout> proteinInfoLayoutDSIndexMap = new HashMap<String, ProteinsInformationOverviewLayout>();
    private final Map<String, DatasetInformationOverviewLayout> datasetInfoLayoutDSIndexMap = new HashMap<String, DatasetInformationOverviewLayout>();
    private final Map<String, QuantProtein> datasetQuantProteinsMap;
    private final Map<String, QuantDatasetObject> datasetIdDsObjectProteinsMap;
    private final String accession, url, name;
     private final Map<String,String> diseaseHashedColorMap;

    public StudyPopupLayout(int width, Map<String, QuantProtein> datasetQuantProteinsMap, Map<String, QuantDatasetObject> datasetIdDsObjectProteinsMap, String accession, String url, String name,Map<String,String> diseaseHashedColorMap) {
        this.diseaseHashedColorMap=diseaseHashedColorMap;
        this.accession = accession;
        this.url = url;
        this.name = name;
        this.datasetQuantProteinsMap = datasetQuantProteinsMap;
        this.datasetIdDsObjectProteinsMap = datasetIdDsObjectProteinsMap;
        this.setWidthUndefined();
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
        bottomLayout.setStyleName(Reindeer.LAYOUT_WHITE);

        peptidesInformationContainer = this.initInformationContainer(width);
        proteinsInformationContainer = this.initInformationContainer(width);
        datasetsInformationContainer = this.initInformationContainer(width);
        String infoText = "Select an experiment in the roll down menu on top to view all proteins identified in the selected experiment. Select a protein to see below all Peptides identified for the protein, and if the experiment was based on SDS-PAGE, the protein’s distribution in the gel is displayed under Fractions. To show information about the experiment, press Dataset Information.  Use the search box to navigate in the experiment selected.</p><p  style='font-family:verdana;color:black;margin-left:20px;margin-right:20px;'>Under Fractions, bar charts show the distribution of the selected protein across the fractions cut from the gel. Three charts show number of peptides, number of spectra and average precursor intensity. The fraction number represents the gel pieces cut from top to bottom. Protein standards <font color='#CDE1FF'>(light blue bars)</font> indicate the molecular weight range of each fraction. <font color='#79AFFF'>Darker blue bars</font> mark between which two standards the protein's theoretical mass suggests the protein should be found.";

        peptideProteinInfoHideShow = new HideOnClickLayout("Peptides Information", peptidesInformationContainer, null, Alignment.TOP_CENTER, infoText,null);
        peptideProteinInfoHideShow.setHeightUndefined();
        bottomLayout.addComponent(peptideProteinInfoHideShow);
        peptideProteinInfoHideShow.setVisability(true);

        HideOnClickLayout proteinInfoHideShow = new HideOnClickLayout("Protein Information", proteinsInformationContainer, null, Alignment.TOP_CENTER, infoText,null);
        proteinInfoHideShow.setHeightUndefined();
        bottomLayout.addComponent(proteinInfoHideShow);
        proteinInfoHideShow.setVisability(true);

        HideOnClickLayout datasetInfoLayout = new HideOnClickLayout("Dataset Information", datasetsInformationContainer, null, infoText,null);
        datasetInfoLayout.setMargin(new MarginInfo(false, false, false, false));
        bottomLayout.addComponent(datasetInfoLayout);
        datasetInfoLayout.setVisability(false);
        bottomLayout.setVisible(false);

    }

    public StudyPopupLayout(int width, QuantProtein datasetQuantProtein, QuantDatasetObject qds, String accession, String url, String name, Map<String,String> diseaseHashedColorMap) {

        this.diseaseHashedColorMap=diseaseHashedColorMap;
        this.accession = accession;
        this.url = url;
        this.name = name;
        this.setWidthUndefined();
        this.setHeightUndefined();
        this.setStyleName(Reindeer.LAYOUT_WHITE);
        this.setMargin(false);
        this.setSpacing(true);
        int colNum = Math.max(1, width / 200);

        topLayout = new GridLayout(colNum, 3);
        topLayout.setStyleName(Reindeer.LAYOUT_WHITE);

        topLayout.setHeightUndefined();
        topLayout.setSpacing(true);

        this.datasetQuantProteinsMap = new LinkedHashMap<String, QuantProtein>();
        datasetQuantProteinsMap.put("_-_" + datasetQuantProtein.getDsKey() + "_-_" + accession + "_-_", datasetQuantProtein);
        this.datasetIdDsObjectProteinsMap = new HashMap<String, QuantDatasetObject>();
        datasetIdDsObjectProteinsMap.put("_-_" + datasetQuantProtein.getDsKey() + "_-_" + accession + "_-_", qds);

        bottomLayout = new VerticalLayout();
        bottomLayout.setSpacing(true);
        this.addComponent(topLayout);
        bottomLayout.setMargin(new MarginInfo(true, false, false, false));
        this.addComponent(bottomLayout);
        bottomLayout.setWidth((width) + "px");
//        bottomLayout.setHeight(height+"px");
        bottomLayout.setStyleName(Reindeer.LAYOUT_WHITE);

        peptidesInformationContainer = this.initInformationContainer(width);
        proteinsInformationContainer = this.initInformationContainer(width);
        datasetsInformationContainer = this.initInformationContainer(width);
        String infoText = "Select an experiment in the roll down menu on top to view all proteins identified in the selected experiment. Select a protein to see below all Peptides identified for the protein, and if the experiment was based on SDS-PAGE, the protein’s distribution in the gel is displayed under Fractions. To show information about the experiment, press Dataset Information.  Use the search box to navigate in the experiment selected.</p><p  style='font-family:verdana;color:black;margin-left:20px;margin-right:20px;'>Under Fractions, bar charts show the distribution of the selected protein across the fractions cut from the gel. Three charts show number of peptides, number of spectra and average precursor intensity. The fraction number represents the gel pieces cut from top to bottom. Protein standards <font color='#CDE1FF'>(light blue bars)</font> indicate the molecular weight range of each fraction. <font color='#79AFFF'>Darker blue bars</font> mark between which two standards the protein's theoretical mass suggests the protein should be found.";

        peptideProteinInfoHideShow = new HideOnClickLayout("Peptides Information", peptidesInformationContainer, null, Alignment.TOP_CENTER, infoText,null);
        peptideProteinInfoHideShow.setHeightUndefined();
        bottomLayout.addComponent(peptideProteinInfoHideShow);
        peptideProteinInfoHideShow.setVisability(true);

        HideOnClickLayout proteinInfoHideShow = new HideOnClickLayout("Protein Information", proteinsInformationContainer, null, Alignment.TOP_CENTER, infoText,null);
        proteinInfoHideShow.setHeightUndefined();
        bottomLayout.addComponent(proteinInfoHideShow);
        proteinInfoHideShow.setVisability(true);

        HideOnClickLayout datasetInfoLayout = new HideOnClickLayout("Dataset Information", datasetsInformationContainer, null, infoText,null);
        datasetInfoLayout.setMargin(new MarginInfo(false, false, false, false));
        bottomLayout.addComponent(datasetInfoLayout);
        datasetInfoLayout.setVisability(false);
        bottomLayout.setVisible(false);

    }

    private VerticalLayout generateBtn(int dsKey, String protAccession, String btnName) {
        VerticalLayout btn = new VerticalLayout();
        btn.addLayoutClickListener(this);
        btn.setHeight("50px");
        btn.setWidth("200px");
        Label btnLabel = new Label(btnName);
        btn.addComponent(btnLabel);
        btn.setComponentAlignment(btnLabel, Alignment.MIDDLE_CENTER);
        btn.setStyleName("tabbtn");
        btn.setData("_-_" + dsKey + "_-_" + protAccession + "_-_");

        return btn;
    }

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
            lastSelectedBtn = null;
            return;
        }
        this.selectStudyBtn(selectedBtn);
    }

    private void selectStudyBtn(VerticalLayout selectedBtn) {

        lastSelectedBtn = selectedBtn;
        lastSelectedBtn.setStyleName("selectedtabbtn");
        updatePeptideInfoLayout(lastSelectedBtn.getData().toString());
        updateProteinDatasetInfoLayout(lastSelectedBtn.getData().toString());
        bottomLayout.setVisible(true);

    }

    public void setInformationData(Set<QuantDatasetObject> dsObjects, DiseaseGroupsComparisonsProteinLayout cp) {
        topLayout.removeAllComponents();

        int colcounter = 0;
        int rowcounter = 0;
        for (QuantDatasetObject quantDS : dsObjects) {
            VerticalLayout btn = this.generateBtn(quantDS.getDsKey(), cp.getProteinAccssionNumber(), quantDS.getAuthor() + " (" + quantDS.getYear() + ")");

            topLayout.addComponent(btn, colcounter++, rowcounter);
            if (colcounter >= topLayout.getColumns()) {
                colcounter = 0;
                rowcounter++;

            }

        }

        int subWidth = (int) ((float) bottomLayout.getWidth() - 30);
        this.initPopupLayoutLayout(cp, subWidth);
        this.selectStudyBtn((VerticalLayout) topLayout.getComponent(0, 0));

    }

    private void initPopupLayoutLayout(DiseaseGroupsComparisonsProteinLayout cp, int subWidth) {
        peptidesInfoLayoutDSIndexMap.clear();
        proteinInfoLayoutDSIndexMap.clear();
        datasetInfoLayoutDSIndexMap.clear();

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
            String key = "_-_" + dsID + "_-_" + cp.getProteinAccssionNumber() + "_-_";
            PeptidesInformationOverviewLayout peptideInfoLayout = new PeptidesInformationOverviewLayout(cp.getSequence(), dsQuantPepMap.get(dsID), subWidth, false, null, 0);
            peptidesInfoLayoutDSIndexMap.put(key, peptideInfoLayout);
        }
        for (String key : cp.getDsQuantProteinsMap().keySet()) {
            ProteinsInformationOverviewLayout proteinInfoLayout = new ProteinsInformationOverviewLayout(subWidth);
            DatasetInformationOverviewLayout datasetInfoLayout = new DatasetInformationOverviewLayout(subWidth,diseaseHashedColorMap);
            proteinInfoLayoutDSIndexMap.put(key, proteinInfoLayout);
            datasetInfoLayoutDSIndexMap.put(key, datasetInfoLayout);
        }

    }

    private VerticalLayout initInformationContainer(int width) {
        VerticalLayout generatedPeptidesInformationContainer = new VerticalLayout();
        generatedPeptidesInformationContainer.setWidth(width - 10 + "px");
        generatedPeptidesInformationContainer.setHeightUndefined();
        return generatedPeptidesInformationContainer;

    }

    private void updatePeptideInfoLayout(String protStudyKey) {
        peptidesInformationContainer.removeAllComponents();
        for (String key : this.peptidesInfoLayoutDSIndexMap.keySet()) {
            if (key.equalsIgnoreCase(protStudyKey)) {
                peptidesInformationContainer.addComponent(peptidesInfoLayoutDSIndexMap.get(key));
                peptidesInformationContainer.setComponentAlignment(peptidesInfoLayoutDSIndexMap.get(key), Alignment.TOP_CENTER);
                peptideProteinInfoHideShow.setVisible(true);
                break;

            }

        }
        if (peptidesInformationContainer.getComponentCount() == 0) {
            peptideProteinInfoHideShow.setVisible(false);
        }
    }

    private void updateProteinDatasetInfoLayout(String protStudyKey) {
        proteinsInformationContainer.removeAllComponents();
        datasetsInformationContainer.removeAllComponents();
        for (String key : this.proteinInfoLayoutDSIndexMap.keySet()) {
            if (key.equalsIgnoreCase(protStudyKey)) {
                proteinInfoLayoutDSIndexMap.get(key).updateProteinsForm(datasetQuantProteinsMap.get(key), accession, url, name);
                proteinsInformationContainer.addComponent(proteinInfoLayoutDSIndexMap.get(key));
                proteinsInformationContainer.setComponentAlignment(proteinInfoLayoutDSIndexMap.get(key), Alignment.TOP_CENTER);
                datasetInfoLayoutDSIndexMap.get(key).updateDatasetForm(datasetIdDsObjectProteinsMap.get(key));
                datasetsInformationContainer.addComponent(datasetInfoLayoutDSIndexMap.get(key));
                datasetsInformationContainer.setComponentAlignment(datasetInfoLayoutDSIndexMap.get(key), Alignment.TOP_CENTER);
                break;
            }
        }
    }
}
