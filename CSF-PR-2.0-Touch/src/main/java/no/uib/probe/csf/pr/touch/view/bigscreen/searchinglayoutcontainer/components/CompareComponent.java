/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.bigscreen.searchinglayoutcontainer.components;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import no.uib.probe.csf.pr.touch.Data_Handler;
import no.uib.probe.csf.pr.touch.database.Query;
import no.uib.probe.csf.pr.touch.logic.beans.QuantProtein;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFPR_Central_Manager;
import no.uib.probe.csf.pr.touch.selectionmanager.QuantSearchSelection;
import no.uib.probe.csf.pr.touch.view.core.BigBtn;
import no.uib.probe.csf.pr.touch.view.core.InformationButton;
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
    private PieChart proteinsChart;
    private final boolean smallScreen;
    private final VerticalLayout resultsLayout;
    private final HorizontalLayout middleLayout;

    public CompareComponent(final Data_Handler Data_handler, CSFPR_Central_Manager CSFPR_Central_Manager, boolean smallScreen) {
        super("Compare", "Compare to your data", "img/compare.png", smallScreen);
        this.smallScreen = smallScreen;
        this.Data_handler = Data_handler;
        this.CSFPR_Central_Manager = CSFPR_Central_Manager;
        VerticalLayout popupbodyLayout = new VerticalLayout();
        popupbodyLayout.setSpacing(true);
        popupbodyLayout.setWidth(100, Sizeable.Unit.PERCENTAGE);
        popupbodyLayout.setMargin(new MarginInfo(false, true, false, true));
        popupbodyLayout.addStyleName("searchpopup");
        comparePanel = new PopupWindow(popupbodyLayout, "Compare Data");

        int h1;
        if (smallScreen) {
            comparePanel.setHeight(comparePanel.getHeight() + 100, Unit.PIXELS);
            comparePanel.setWidth(comparePanel.getWidth() + 100, Unit.PIXELS);
            h1 = 340;
        } else {
            h1 = 460;//Math.min(((int) comparePanel.getHeight() / 2) - 30,460);
        }
        compareUnit = new ComparisonUnitComponent(Data_handler, h1, smallScreen) {

            @Override
            public void startComparing(Query query) {
                compareProteins(query);
            }

        };

        resultsLayout = new VerticalLayout();
        resultsLayout.addStyleName("roundedborder");
        resultsLayout.addStyleName("padding20");
        resultsLayout.addStyleName("whitelayout");

        resultsLayout.setWidth(100, Sizeable.Unit.PERCENTAGE);
        h1 = (int) comparePanel.getHeight() - h1 - 30 - 130;
        resultsLayout.setHeight(h1, Sizeable.Unit.PIXELS);

        resultsLayout.addStyleName("scrollable");
        quantCompareDataResult = new HorizontalLayout();

        resultsLayout.addComponent(quantCompareDataResult);
        resultsLayout.setComponentAlignment(quantCompareDataResult, Alignment.MIDDLE_CENTER);

        middleLayout = new HorizontalLayout();
        middleLayout.setHeight(29, Sizeable.Unit.PIXELS);
        middleLayout.setWidth(100, Sizeable.Unit.PERCENTAGE);
        resultsLabel = new Label("Search Results");
        resultsLabel.setStyleName(ValoTheme.LABEL_BOLD);
        resultsLabel.addStyleName("marginleft");
        middleLayout.addComponent(resultsLabel);
        middleLayout.setComponentAlignment(resultsLabel, Alignment.MIDDLE_LEFT);

        HorizontalLayout legendContainer = new HorizontalLayout();
        legendContainer.setSpacing(true);
        middleLayout.addComponent(legendContainer);
        middleLayout.setComponentAlignment(legendContainer, Alignment.TOP_RIGHT);

        TrendLegend legend2 = new TrendLegend("found_notfound");
        legendContainer.addComponent(legend2);
        legendContainer.setComponentAlignment(legend2, Alignment.MIDDLE_RIGHT);
        legend2.addStyleName("marginright");

        TrendLegend legend = new TrendLegend("diseaselegend");
        legendContainer.addComponent(legend);
        legendContainer.setComponentAlignment(legend, Alignment.MIDDLE_RIGHT);
        legend.addStyleName("marginright");

        controlBtnsLayout = new HorizontalLayout();
        controlBtnsLayout.addStyleName("roundedborder");
        controlBtnsLayout.addStyleName("padding20");
        controlBtnsLayout.addStyleName("whitelayout");
        controlBtnsLayout.setMargin(new MarginInfo(true, false, false, false));
        controlBtnsLayout.setWidth(100, Sizeable.Unit.PERCENTAGE);

        HorizontalLayout leftsideWrapper = new HorizontalLayout();
        controlBtnsLayout.addComponent(leftsideWrapper);
        controlBtnsLayout.setComponentAlignment(leftsideWrapper, Alignment.TOP_LEFT);
        leftsideWrapper.setSpacing(true);

        InformationButton info = new InformationButton("info", true);
        leftsideWrapper.addComponent(info);

        idDataResult = new VerticalLayout();
        idDataResult.addStyleName("marginleft");
        idDataResult.setWidth(600, Unit.PIXELS);

        leftsideWrapper.addComponent(idDataResult);

        controlBtnsLayout.addComponent(leftsideWrapper);

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

        Button resetSystemBtn = new Button("Hide data");
        resetSystemBtn.setStyleName(ValoTheme.BUTTON_SMALL);
        resetSystemBtn.setStyleName(ValoTheme.BUTTON_TINY);
        resetSystemBtn.setWidth(100, Sizeable.Unit.PIXELS);
        resetSystemBtn.setEnabled(false);
        btnsWrapper.addComponent(resetSystemBtn);
        btnsWrapper.setComponentAlignment(resetSystemBtn, Alignment.MIDDLE_CENTER);
        resetSystemBtn.setDescription("Hide user data ");
        resetSystemBtn.addClickListener((Button.ClickEvent event) -> {
            QuantSearchSelection selection = new QuantSearchSelection();
            selection.setUserCustComparison(null);
            CSFPR_Central_Manager.compareSelectionAction(selection);
            CompareComponent.this.loadQuantComparison();
            comparePanel.setVisible(false);
            this.setEnabled(false);
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
            resetSystemBtn.setEnabled(true);
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
        if (smallScreen) {
            resultsLayout.setVisible(false);
            middleLayout.setVisible(false);
            resultsLayout.setWidth(compareUnit.getWidth(),compareUnit.getWidthUnits());
            resultsLayout.setHeight(compareUnit.getHeight()-40,compareUnit.getHeightUnits());
        }

    }

    private void resetComparison() {
        compareUnit.reset();
        idDataResult.setVisible(false);
        quantCompareDataResult.removeAllComponents();
        loadDataBtn.setEnabled(false);
        if (smallScreen) {
            resultsLayout.setVisible(false);
            middleLayout.setVisible(false);
            compareUnit.setVisible(true);
           
        }

    }
    private PieChart datasetsChart;

    private void loadComparison() {
        Set<String> diseaseCategories = new HashSet<>();
        Set<String> proteinList = new HashSet<>();
        Set<Integer> datasetIds = new HashSet<>();
        QuantSearchSelection selection = new QuantSearchSelection();

        if (!datasetsChart.getSelectionSet().isEmpty() && !datasetsChart.getSelectionSet().contains("all")) {
            searchQuantificationProtList.stream().filter((protein) -> (datasetsChart.getSelectionSet().contains(protein.getDiseaseCategory()))).map((protein) -> {
                datasetIds.add(protein.getDsKey());
                return protein;
            }).forEach((protein) -> {
                proteinList.add(protein.getUniprotAccession());
                diseaseCategories.add(protein.getDiseaseCategory());
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
            diseaseCat = diseaseCategories.toArray()[0] + "";
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
        defaultText = defaultText.replace(",", "\n").replace(" ", "").trim();
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
        Integer[] proteinsHitsList = Data_handler.getQuantComparisonProteinList(searchQuantificationProtList, query.getSearchBy());;
        if (quantHitsList != null && searchQuantificationProtList != null) {
            Set<Integer> studiesSet;
            studiesSet = new HashSet<>();
            searchQuantificationProtList.stream().forEach((qp) -> {
                studiesSet.add(qp.getDsKey());
            });
            if (quantNotFound != null) {
                for (String s : quantNotFound.split(",")) {
                    filterKeywordSet.remove(s.trim());
                }
            } else {
                quantNotFound = "";
            }

            initQuantComparisonresults(quantHitsList, proteinsHitsList, quantNotFound.split(","), filterKeywordSet.size());
        }

        query.setSearchDataType("Identification Data");

        //searching id data
        String idSearchIdentificationProtList = Data_handler.searchIdentificationProtein(query);
        if (idSearchIdentificationProtList != null) {
            idDataResult.setVisible(true);
            idDataResult.removeAllComponents();
            Link idSearchingLink = new Link(idSearchIdentificationProtList, new ExternalResource(VaadinSession.getCurrent().getAttribute("csf_pr_Url") + "searchby:" + query.getSearchBy().replace(" ", "*") + "___searchkey:" + query.getSearchKeyWords().replace("\n", "__").replace(" ", "*")));
            idSearchingLink.setTargetName("_blank");
            idSearchingLink.setStyleName(ValoTheme.LINK_SMALL);
            idSearchingLink.setDescription("View protein id results in CSF-PR v1.0");
//            idSearchingLink.setWidth(defaultText);
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
    private void initQuantComparisonresults(Integer[] quantHitsList, Integer[] proteinsHitsList, String[] notFoundAcc, int found) {

        quantCompareDataResult.removeAllComponents();
        if (quantHitsList == null || quantHitsList.length == 0) {
            quantCompareDataResult.setVisible(false);
//            noresultsLabel.setVisible(true);
            loadDataBtn.setEnabled(false);
            return;
        }
        loadDataBtn.setEnabled(true);
        quantCompareDataResult.setVisible(true);

        PieChart notFoundChart = new PieChart(250, 200, "Found / Not Found", true) {

            @Override
            public void sliceClicked(Comparable sliceKey) {
                resetChart();
                if (sliceKey.toString().equalsIgnoreCase("Not Found")) {
                    if (notFoundAcc.length == 1 && notFoundAcc[0].trim().equalsIgnoreCase("")) {
                        return;
                    }
                    StreamResource proteinInformationResource = createProteinsExportResource(new HashSet<>(Arrays.asList(notFoundAcc)));
                    Page.getCurrent().open(proteinInformationResource, "_blank", false);

                } else if (sliceKey.toString().equalsIgnoreCase("Found")) {
                    if (filterKeywordSet.isEmpty()) {
                        return;
                    }
                    StreamResource proteinInformationResource = createProteinsExportResource(filterKeywordSet);
                    Page.getCurrent().open(proteinInformationResource, "_blank", false);

                }

            }

        };

        notFoundChart.getMiddleDountLayout().addStyleName("defaultcursor");
        notFoundChart.getMiddleDountLayout().setDescription("Click slice to export");
        notFoundChart.setDescription("Click slice to export");
        int notFoundLength = notFoundAcc.length;
        if (notFoundAcc.length == 1 && notFoundAcc[0].trim().equalsIgnoreCase("")) {
            notFoundLength = 0;
        }
        notFoundChart.initializeFilterData(new String[]{"Not Found", "Found"}, new Integer[]{notFoundLength, found, found + notFoundLength}, new Color[]{new Color(219, 169, 1), new Color(110, 177, 206),});
        notFoundChart.redrawChart();
        quantCompareDataResult.addComponent(notFoundChart);
        quantCompareDataResult.setComponentAlignment(notFoundChart, Alignment.MIDDLE_CENTER);

        proteinsChart = new PieChart(250, 200, "Proteins", true) {

            @Override
            public void sliceClicked(Comparable sliceKey) {
                datasetsChart.selectSlice(sliceKey);
            }

        };
        proteinsChart.initializeFilterData(items, proteinsHitsList, itemsColors);
        proteinsChart.redrawChart();
        quantCompareDataResult.addComponent(proteinsChart);
        quantCompareDataResult.setComponentAlignment(proteinsChart, Alignment.MIDDLE_CENTER);

        datasetsChart = new PieChart(250, 200, "Datasets ", true) {

            @Override
            public void sliceClicked(Comparable sliceKey) {
                proteinsChart.selectSlice(sliceKey);

            }

        };
        datasetsChart.initializeFilterData(items, quantHitsList, itemsColors);
        datasetsChart.redrawChart();
        quantCompareDataResult.addComponent(datasetsChart);
        quantCompareDataResult.setComponentAlignment(datasetsChart, Alignment.MIDDLE_CENTER);

        resultsLabel.setValue("Results Overview (" + quantHitsList[3] + "/" + (quantHitsList[3] + notFoundAcc.length) + ")");

        if (smallScreen) {
            resultsLayout.setVisible(true);
            middleLayout.setVisible(true);
            compareUnit.setVisible(false);
        }
        
    }

    @Override
    public void onClick() {
        comparePanel.setVisible(true);

    }

    public abstract void loadQuantComparison();

    private StreamResource createProteinsExportResource(Set<String> accessions) {
        return new StreamResource(() -> {
            byte[] csvFile = Data_handler.exportProteinsListToCSV(accessions);
            return new ByteArrayInputStream(csvFile);
        }, "Proteins_List.csv");
    }

}
