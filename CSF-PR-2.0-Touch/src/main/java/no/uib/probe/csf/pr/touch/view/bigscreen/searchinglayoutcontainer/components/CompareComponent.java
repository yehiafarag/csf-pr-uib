/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.bigscreen.searchinglayoutcontainer.components;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.awt.Color;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import no.uib.probe.csf.pr.touch.Data_Handler;
import no.uib.probe.csf.pr.touch.database.Query;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.logic.beans.QuantProtein;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFPR_Central_Manager;
import no.uib.probe.csf.pr.touch.selectionmanager.QuantSearchSelection;
import no.uib.probe.csf.pr.touch.view.core.BigBtn;
import no.uib.probe.csf.pr.touch.view.core.PieChart;
import no.uib.probe.csf.pr.touch.view.core.PopupWindow;
import no.uib.probe.csf.pr.touch.view.core.TrendLegend;

/**
 *
 * @author Yehia Farag
 *
 * this class represents comparison layout users can compare their own data with
 * the resource data
 */
public abstract class CompareComponent extends BigBtn {

    private final PopupWindow comparePanel;
    private List<QuantProtein> searchQuantificationProtList;
    private final HorizontalLayout quantCompareDataResult;
    private final VerticalLayout idDataResult;
    private final Data_Handler Data_handler;
    private final Label resultsLabel;
    private final HorizontalLayout controlBtnsLayout;
    private final Button loadDataBtn;
    private final ComparisonUnitComponent compareUnit;
    private final CSFPR_Central_Manager CSFPR_Central_Manager;

    public CompareComponent(final Data_Handler Data_handler, CSFPR_Central_Manager CSFPR_Central_Manager) {
        super("Compare", "Compare your quantified with the available data.", "img/compare.png");
        this.Data_handler = Data_handler;
        this.CSFPR_Central_Manager = CSFPR_Central_Manager;
        VerticalLayout popupbodyLayout = new VerticalLayout();
        popupbodyLayout.setSpacing(true);
        popupbodyLayout.setWidth(100, Sizeable.Unit.PERCENTAGE);
        popupbodyLayout.setMargin(new MarginInfo(true, true, false, true));
        popupbodyLayout.addStyleName("searchpopup");
        comparePanel = new PopupWindow(popupbodyLayout, "Compare Data");

        compareUnit = new ComparisonUnitComponent(Data_handler) {

            @Override
            public void startComparing(Query query) {
                compareProteins(query);
            }

        };

        VerticalLayout resultsLayout = new VerticalLayout();
        resultsLayout.addStyleName("roundedborder");
        resultsLayout.addStyleName("whitelayout");

        resultsLayout.setWidth(100, Sizeable.Unit.PERCENTAGE);
        resultsLayout.setHeight(254, Sizeable.Unit.PIXELS);
        resultsLayout.addStyleName("scrollable");
        quantCompareDataResult = new HorizontalLayout();

        resultsLayout.addComponent(quantCompareDataResult);
        resultsLayout.setComponentAlignment(quantCompareDataResult, Alignment.MIDDLE_CENTER);
        HorizontalLayout middleLayout = new HorizontalLayout();
        middleLayout.setHeight(29, Sizeable.Unit.PIXELS);
        middleLayout.setWidth(100, Sizeable.Unit.PERCENTAGE);
        resultsLabel = new Label("Search Results");
        resultsLabel.setStyleName(ValoTheme.LABEL_BOLD);
        middleLayout.addComponent(resultsLabel);
        middleLayout.setComponentAlignment(resultsLabel, Alignment.MIDDLE_LEFT);

        TrendLegend legend = new TrendLegend("diseaselegend");
        middleLayout.addComponent(legend);
        middleLayout.setComponentAlignment(legend, Alignment.MIDDLE_RIGHT);

        controlBtnsLayout = new HorizontalLayout();
        controlBtnsLayout.addStyleName("roundedborder");
        controlBtnsLayout.addStyleName("whitelayout");
        controlBtnsLayout.setMargin(new MarginInfo(true, false, false, false));
        controlBtnsLayout.setWidth(100, Sizeable.Unit.PERCENTAGE);
        idDataResult = new VerticalLayout();
        controlBtnsLayout.addComponent(idDataResult);

        HorizontalLayout btnsWrapper = new HorizontalLayout();
        controlBtnsLayout.addComponent(btnsWrapper);
        controlBtnsLayout.setComponentAlignment(btnsWrapper, Alignment.TOP_RIGHT);
        btnsWrapper.setSpacing(true);

        Button resetBtn = new Button("Reset");
        resetBtn.setStyleName(ValoTheme.BUTTON_SMALL);
        resetBtn.setStyleName(ValoTheme.BUTTON_TINY);
        resetBtn.setWidth(60, Sizeable.Unit.PIXELS);
        resetBtn.setEnabled(true);
        btnsWrapper.addComponent(resetBtn);
        btnsWrapper.setComponentAlignment(resetBtn, Alignment.MIDDLE_CENTER);
        resetBtn.setDescription("Reset searching");
        resetBtn.addClickListener((Button.ClickEvent event) -> {
            resetComparison();
        });

        loadDataBtn = new Button("Load");
        loadDataBtn.setStyleName(ValoTheme.BUTTON_SMALL);
        loadDataBtn.setStyleName(ValoTheme.BUTTON_TINY);
        loadDataBtn.setWidth(60, Sizeable.Unit.PIXELS);
        loadDataBtn.setEnabled(false);
        btnsWrapper.addComponent(loadDataBtn);
        btnsWrapper.setComponentAlignment(loadDataBtn, Alignment.MIDDLE_CENTER);
        loadDataBtn.setDescription("Load data");
        loadDataBtn.addClickListener((Button.ClickEvent event) -> {
            loadComparison();
        });

        popupbodyLayout.addComponent(compareUnit);
        popupbodyLayout.setExpandRatio(compareUnit, 540f);
        popupbodyLayout.setSpacing(true);
        popupbodyLayout.addComponent(middleLayout);
        popupbodyLayout.setExpandRatio(middleLayout, 29f);
        popupbodyLayout.addComponent(resultsLayout);
        popupbodyLayout.setExpandRatio(resultsLayout, 254);
        popupbodyLayout.addComponent(controlBtnsLayout);
        popupbodyLayout.setExpandRatio(controlBtnsLayout, 50);

    }

    private void resetComparison() {
        compareUnit.reset();
        idDataResult.setVisible(false);
        quantCompareDataResult.removeAllComponents();
        loadDataBtn.setEnabled(false);

    }
    private PieChart foundChart;

    private void loadComparison() {
        Set<String> diseaseCategories = new HashSet<>();
        Set<String> proteinList = new HashSet<>();
        Set<Integer> datasetIds = new HashSet<>();
        QuantSearchSelection selection = new QuantSearchSelection();

        if (!foundChart.getSelectionSet().isEmpty() && !foundChart.getSelectionSet().contains("all")) {
             searchQuantificationProtList.stream().filter((protein) -> (foundChart.getSelectionSet().contains(protein.getDiseaseCategory()))).map((protein) -> {
                datasetIds.add(protein.getDsKey());
                return protein;
            }).forEach((protein) -> {
                proteinList.add(protein.getUniprotAccession());
            });
            selection.setKeyWords(proteinList);
            
         

        } else {
              searchQuantificationProtList.stream().forEach((protein) -> {               
                datasetIds.add(protein.getDsKey());
                diseaseCategories.add(protein.getDiseaseCategory());
            });
        }

        String diseaseCat;
        if (diseaseCategories.size() == 1 && diseaseCategories.toArray()[0].toString().equalsIgnoreCase("all") || diseaseCategories.size() > 1) {
            diseaseCat = "All Diseases";
        } else {
            diseaseCat = diseaseCategories.toArray()[0].toString();
        }
        selection.setKeyWords(filterKeywordSet);
        comparePanel.close();//
        selection.setDiseaseCategory(diseaseCat);
        selection.setDatasetIds(datasetIds);
        selection.setUserCustComparison(compareUnit.getUserCustomizedComparison()); 
        CSFPR_Central_Manager.compareSelectionAction(selection);
        loadQuantComparison();

    }
    private Set<String> filterKeywordSet;

    private void compareProteins(Query query) {
        query.setValidatedProteins(false);
        query.setSearchDataset("");

        //searching quant data
        String defaultText = query.getSearchKeyWords();
        defaultText = defaultText.replace(",", "\n").trim();
        filterKeywordSet = new LinkedHashSet<>();
        filterKeywordSet.addAll(Arrays.asList(defaultText.split("\n")));
        defaultText = "";
        defaultText = filterKeywordSet.stream().map((str) -> str + "\n").reduce(defaultText, String::concat);
        query.setSearchKeyWords(defaultText);
        //searching quant data
        query.setSearchDataType("Quantification Data");
        searchQuantificationProtList = Data_handler.searchQuantificationProtein(query, true);

        String quantNotFound = Data_handler.filterQuantSearchingKeywords(searchQuantificationProtList, query.getSearchKeyWords(), query.getSearchBy());

        Integer[] quantHitsList = Data_handler.getQuantComparisonHitsList(searchQuantificationProtList, query.getSearchBy());

        if (quantHitsList != null && searchQuantificationProtList != null) {
            Set<Integer> studiesSet = new HashSet<>();
            searchQuantificationProtList.stream().forEach((qp) -> {
                studiesSet.add(qp.getDsKey());
            });
            initQuantComparisonresults(quantHitsList, quantNotFound.split(", "), studiesSet.size());
        }
        if (quantNotFound != null) {
            for (String s : quantNotFound.split(",")) {
                filterKeywordSet.remove(s.trim());
            }
        }

        query.setSearchDataType("Identification Data");

        //searching id data
        String idSearchIdentificationProtList = Data_handler.searchIdentificationProtein(query);
        if (idSearchIdentificationProtList != null) {
            idDataResult.setVisible(true);
            idDataResult.removeAllComponents();
            Link idSearchingLink = new Link(idSearchIdentificationProtList, new ExternalResource("http://localhost:8084/CSF-PR-ID/searchby:" + query.getSearchBy().replace(" ", "*") + "___searchkey:" + query.getSearchKeyWords().replace("\n", "__").replace(" ", "*")));
            idSearchingLink.setTargetName("_blank");
            idSearchingLink.setStyleName(ValoTheme.LINK_SMALL);
            idSearchingLink.setDescription("View protein id results in CSF-PR v1.0");
            idDataResult.addComponent(idSearchingLink);

        } else {
            idDataResult.setVisible(false);
        }

    }
    private final String[] items = new String[]{"Alzheimer's", "Multiple Sclerosis", "Parkinson's"};
    private final Color[] itemsColors = new Color[]{Color.decode("#4b7865"), Color.decode("#A52A2A"), Color.decode("#74716E")};

    /**
     * initialize quant searching results layout
     *
     * @param quantHitsList map of hits and main protein title
     * @param searchBy the searching by method
     * @param totalProtNum total number of hits
     * @param keywords the keywords used for the searching
     *
     */
    private void initQuantComparisonresults(Integer[] quantHitsList, String[] notFoundAcc, int datasetNumber) {

        quantCompareDataResult.removeAllComponents();
        if (quantHitsList == null || quantHitsList.length == 0) {
            quantCompareDataResult.setVisible(false);
//            noresultsLabel.setVisible(true);
            loadDataBtn.setEnabled(false);
            return;
        }
        loadDataBtn.setEnabled(true);
        quantCompareDataResult.setVisible(true);

        foundChart = new PieChart(250, 200, "Proteins Found ", true);
        foundChart.initializeFilterData(items, quantHitsList, itemsColors);
        foundChart.redrawChart();
        quantCompareDataResult.addComponent(foundChart);
        quantCompareDataResult.setComponentAlignment(foundChart, Alignment.MIDDLE_CENTER);

        PieChart notFoundChart = new PieChart(250, 200, "Proteins Not Found", false);
        notFoundChart.initializeFilterData(new String[]{"Not Found"}, new Integer[]{notFoundAcc.length, notFoundAcc.length}, new Color[]{new Color(110, 177, 206)});
        notFoundChart.redrawChart();
        quantCompareDataResult.addComponent(notFoundChart);
        quantCompareDataResult.setComponentAlignment(notFoundChart, Alignment.MIDDLE_CENTER);

        notFoundChart.addLayoutClickListener((LayoutEvents.LayoutClickEvent event) -> {
            for (String str : notFoundAcc) {
                System.out.println("at str "+str);

            }
        });

        resultsLabel.setValue("Results Overview (" + quantHitsList[3] + "/" + (quantHitsList[3] + notFoundAcc.length) + ")");

    }

    @Override
    public void onClick() {
        comparePanel.setVisible(true);

    }

    public abstract void loadQuantComparison();

}
