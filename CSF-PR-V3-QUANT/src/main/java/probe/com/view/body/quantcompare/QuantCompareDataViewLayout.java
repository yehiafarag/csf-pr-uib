/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantcompare;

import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import probe.com.handlers.CSFPRHandler;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
import probe.com.model.beans.quant.QuantProtein;
import probe.com.selectionmanager.QuantCentralManager;
import probe.com.view.body.quantdatasetsoverview.DiseaseGroupsFiltersContainer;
import probe.com.view.body.quantdatasetsoverview.QuantProteinsTabsheetContainerLayout;
import probe.com.view.core.HideOnClickLayout;

/**
 *
 * @author YEhia Farag
 */
public class QuantCompareDataViewLayout extends VerticalLayout {

    private final QuantCentralManager Quant_Central_Manager;
    private final QuantProteinsTabsheetContainerLayout proteinsLayout;
    private final QuantProteinsComparisonsContainer quantProteinsComparisonsContainer;

    /**
     *
     * @param CSFPR_Handler
     * @param searchQuantificationProtList
     * @param userCustomizedComparison
     */
    public QuantCompareDataViewLayout(CSFPRHandler CSFPR_Handler, List<QuantProtein> searchQuantificationProtList,QuantDiseaseGroupsComparison userCustomizedComparison) {

        Quant_Central_Manager = new QuantCentralManager(CSFPR_Handler, searchQuantificationProtList);//,filterUtility.getFullFilterList()
        this.setMargin(true);
        this.setSpacing(true);
        this.setWidth("100%");
        this.setHeightUndefined();

        DiseaseGroupsFiltersContainer heatmapFilter = new DiseaseGroupsFiltersContainer(Quant_Central_Manager, CSFPR_Handler, searchQuantificationProtList);
        heatmapFilter.setWidth("100%");
        heatmapFilter.setMargin(new MarginInfo(false, false, true, false));
        String infoText = "Select an experiment in the roll down menu on top to view all proteins identified in the selected experiment. Select a protein to see below all Peptides identified for the protein, and if the experiment was based on SDS-PAGE, the protein’s distribution in the gel is displayed under Fractions. To show information about the experiment, press Dataset Information.  Use the search box to navigate in the experiment selected.</p><p  style='font-family:verdana;color:black;margin-left:20px;margin-right:20px;'>Under Fractions, bar charts show the distribution of the selected protein across the fractions cut from the gel. Three charts show number of peptides, number of spectra and average precursor intensity. The fraction number represents the gel pieces cut from top to bottom. Protein standards <font color='#CDE1FF'>(light blue bars)</font> indicate the molecular weight range of each fraction. <font color='#79AFFF'>Darker blue bars</font> mark between which two standards the protein's theoretical mass suggests the protein should be found.";

        HideOnClickLayout DatasetFilteringContainer = new HideOnClickLayout("Datasets", heatmapFilter, null, infoText, CSFPR_Handler.getTipsGenerator().generateTipsBtn());
        this.addComponent(DatasetFilteringContainer);
        DatasetFilteringContainer.setVisability(true);

        quantProteinsComparisonsContainer = new QuantProteinsComparisonsContainer(Quant_Central_Manager, CSFPR_Handler, searchQuantificationProtList,userCustomizedComparison);

        HideOnClickLayout comparisonsTableContainer = new HideOnClickLayout("Proteins", quantProteinsComparisonsContainer, null, Alignment.TOP_LEFT, infoText, null);
        int pageWidth = Page.getCurrent().getWebBrowser().getScreenWidth();
        int layoutWidth = (pageWidth - 70);
        quantProteinsComparisonsContainer.setLayoutWidth(layoutWidth);
        this.addComponent(comparisonsTableContainer);
        comparisonsTableContainer.setVisability(true);

        proteinsLayout = new QuantProteinsTabsheetContainerLayout(Quant_Central_Manager, true, CSFPR_Handler);
        HideOnClickLayout proteinsLevelLayout = new HideOnClickLayout("Proteins Information", proteinsLayout, null, infoText, null);

        this.addComponent(proteinsLevelLayout);
        proteinsLevelLayout.setVisability(true);
        heatmapFilter.selectAllComparisons();

       
    }

}
