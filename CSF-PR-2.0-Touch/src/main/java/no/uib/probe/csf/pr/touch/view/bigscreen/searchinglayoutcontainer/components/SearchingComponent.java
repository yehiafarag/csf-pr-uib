package no.uib.probe.csf.pr.touch.view.bigscreen.searchinglayoutcontainer.components;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
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
import no.uib.probe.csf.pr.touch.view.core.ProteinSearcingResultLabel;
import no.uib.probe.csf.pr.touch.view.core.TrendLegend;

/**
 *
 * @author Yehia Farag
 *
 * this class represents searching components
 */
public abstract class SearchingComponent extends BigBtn {

    private final PopupWindow searchingPanel;
    private List<QuantProtein> searchQuantificationProtList;
    private final GridLayout quantDataResult;
    private final VerticalLayout idDataResult, overviewResults;
    private final Label noresultsLabel;
    private final Data_Handler Data_handler;
    private final String noresultMessage = "No results found";
    private final Label resultsLabel;
    private final HorizontalLayout controlBtnsLayout;
    private final Button loadDataBtn;
    private final SearchingUnitComponent searchingUnit;
    private final CSFPR_Central_Manager CSFPR_Central_Manager;
    private final VerticalLayout resultsLayout;
    private final HorizontalLayout quantResultWrapping;
    private int h1;
    private final boolean smallScreen;

    public SearchingComponent(final Data_Handler Data_handler, CSFPR_Central_Manager CSFPR_Central_Manager, boolean smallScreen) {
        super("Search", "Search protein data", "img/search.png", smallScreen);
        this.smallScreen=smallScreen;
        this.Data_handler = Data_handler;
        this.CSFPR_Central_Manager = CSFPR_Central_Manager;
        VerticalLayout popupbodyLayout = new VerticalLayout();
        popupbodyLayout.setSpacing(true);
        popupbodyLayout.setWidth(100, Unit.PERCENTAGE);
        popupbodyLayout.setMargin(new MarginInfo(false, true, false, true));
        popupbodyLayout.addStyleName("searchpopup");
        searchingPanel = new PopupWindow(popupbodyLayout, "Search");

        if (smallScreen) {
            searchingPanel.setHeight(searchingPanel.getHeight() + 100, Unit.PIXELS);
            searchingPanel.setWidth(searchingPanel.getWidth() + 100, Unit.PIXELS);
            h1 = 190;
        } else {
            h1 = Math.min(((int) searchingPanel.getHeight() / 2) - 30, 260);
        }
        searchingUnit = new SearchingUnitComponent(h1, smallScreen) {

            @Override
            public void resetSearching() {
                SearchingComponent.this.resetSearch();
            }

            @Override
            public void search(Query query) {
                searchProteins(query);
            }

        };

        resultsLayout = new VerticalLayout();
        resultsLayout.addStyleName("roundedborder");
        resultsLayout.addStyleName("whitelayout");
        resultsLayout.addStyleName("padding20");

        resultsLayout.setWidth(100, Unit.PERCENTAGE);
//        Panel searchingResults = new Panel(resultsLayout);
//        searchingResults.setStyleName(ValoTheme.PANEL_BORDERLESS);
//        searchingResults.setWidth(100, Unit.PERCENTAGE);
        h1 = (int) searchingPanel.getHeight() - h1 - 30 - 160;
        resultsLayout.setHeight(h1, Unit.PIXELS);
        resultsLayout.addStyleName("scrollable");

        quantResultWrapping = new HorizontalLayout();
        quantResultWrapping.setWidthUndefined();
        quantResultWrapping.setSpacing(true);

        resultsLayout.addComponent(quantResultWrapping);

        overviewResults = new VerticalLayout();

        quantResultWrapping.addComponent(overviewResults);
        quantResultWrapping.setComponentAlignment(overviewResults, Alignment.TOP_LEFT);

        quantDataResult = new GridLayout();

        quantResultWrapping.addComponent(quantDataResult);
        quantResultWrapping.setComponentAlignment(quantDataResult, Alignment.MIDDLE_CENTER);

        noresultsLabel = new Label(noresultMessage);
        noresultsLabel.setStyleName(ValoTheme.LABEL_BOLD);
        noresultsLabel.setVisible(false);
        resultsLayout.addComponent(noresultsLabel);
        resultsLayout.setComponentAlignment(noresultsLabel, Alignment.MIDDLE_CENTER);

        HorizontalLayout middleLayout = new HorizontalLayout();
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
        controlBtnsLayout.addStyleName("padding10");
        controlBtnsLayout.addStyleName("whitelayout");
        controlBtnsLayout.setMargin(new MarginInfo(true, false, false, false));
        controlBtnsLayout.setWidth(100, Unit.PERCENTAGE);

        HorizontalLayout btnsWrapper = new HorizontalLayout();
        controlBtnsLayout.addComponent(btnsWrapper);
        controlBtnsLayout.setComponentAlignment(btnsWrapper, Alignment.TOP_LEFT);
        btnsWrapper.setSpacing(true);

        InformationButton info = new InformationButton("Searching allows the user to locate a specific protein or a group of proteins. Input the search text at the top, select the input type and the disease category, and click \"Search\". A graphical overview of the results will be displayed at the bottom. You can either load all the results or select a subset via the charts before loading.", true);
        btnsWrapper.addComponent(info);

        idDataResult = new VerticalLayout();
        idDataResult.addStyleName("marginleft");
        btnsWrapper.addComponent(idDataResult);

//        Button resetBtn = new Button("Reset");
//        resetBtn.setStyleName(ValoTheme.BUTTON_SMALL);
//        resetBtn.setStyleName(ValoTheme.BUTTON_TINY);
//        resetBtn.setWidth(60, Unit.PIXELS);
//        resetBtn.setEnabled(true);
//        btnsWrapper.addComponent(resetBtn);
//        btnsWrapper.setComponentAlignment(resetBtn, Alignment.MIDDLE_CENTER);
//        resetBtn.setDescription("Reset searching");
//        resetBtn.addClickListener((Button.ClickEvent event) -> {
//            resetSearch();
//        });
        loadDataBtn = new Button("Load");
        loadDataBtn.setStyleName(ValoTheme.BUTTON_SMALL);
        loadDataBtn.setStyleName(ValoTheme.BUTTON_TINY);
        loadDataBtn.setWidth(60, Unit.PIXELS);
        loadDataBtn.setEnabled(false);
        controlBtnsLayout.addComponent(loadDataBtn);
        controlBtnsLayout.setComponentAlignment(loadDataBtn, Alignment.MIDDLE_RIGHT);
        loadDataBtn.setDescription("Load data");
        loadDataBtn.addClickListener((Button.ClickEvent event) -> {
            loadSearching();
        });

        popupbodyLayout.addComponent(searchingUnit);
        popupbodyLayout.setExpandRatio(searchingUnit, 290f);
        popupbodyLayout.setSpacing(true);
        popupbodyLayout.addComponent(middleLayout);
        popupbodyLayout.setExpandRatio(middleLayout, 29f);
        popupbodyLayout.addComponent(resultsLayout);
        popupbodyLayout.setExpandRatio(resultsLayout, 524);
        popupbodyLayout.addComponent(controlBtnsLayout);
        popupbodyLayout.setExpandRatio(controlBtnsLayout, 50);

    }

    private void resetSearch() {
        resultsLabel.setValue("Search Results");
        idDataResult.setVisible(false);
        noresultsLabel.setVisible(false);
        quantDataResult.removeAllComponents();
        overviewResults.removeAllComponents();
        loadDataBtn.setEnabled(false);
        if (smallScreen) {
            resultsLayout.setHeight(h1, Unit.PIXELS);
        }

    }

    private void loadSearching() {
        Iterator<Component> itr = quantDataResult.iterator();
        Set<String> diseaseCategories = new HashSet<>();
        Map<String, Set<String>> proteinList = new HashMap<>();
        boolean noSelection = true;
        while (itr.hasNext()) {
            Component comp = itr.next();
            if (comp instanceof PieChart) {
                PieChart pieChartComponent = (PieChart) comp;
                if (!pieChartComponent.getSelectionSet().isEmpty()) {
                    noSelection = false;
                    diseaseCategories.addAll(pieChartComponent.getSelectionSet());
                    proteinList.put(pieChartComponent.getData().toString(), pieChartComponent.getSelectionSet());
                }

            } else {
                ProteinSearcingResultLabel proteinLabelComponent = (ProteinSearcingResultLabel) comp;
                if (!proteinLabelComponent.getSelectionSet().isEmpty()) {
                    noSelection = false;
                    diseaseCategories.addAll(proteinLabelComponent.getSelectionSet());
                    proteinList.put(proteinLabelComponent.getProteinKey(), proteinLabelComponent.getSelectionSet());
                }
            }

        }

        Set<Integer> datasetIds = new HashSet<>();
        QuantSearchSelection selection = new QuantSearchSelection();
        if (noSelection) {
            selection.setKeyWords(filterKeywordSet);
            searchQuantificationProtList.stream().forEach((protein) -> {
                datasetIds.add(protein.getDsKey());
                diseaseCategories.add(protein.getDiseaseCategory());
            });

        } else {
            selection.setKeyWords(proteinList.keySet());
            searchQuantificationProtList.stream().filter((protein) -> (proteinList.keySet().contains(protein.getFinalAccession()) && (proteinList.get(protein.getFinalAccession()).contains("all") || proteinList.get(protein.getFinalAccession()).contains(protein.getDiseaseCategory())))).forEach((protein) -> {

                datasetIds.add(protein.getDsKey());

            });
        }

        String diseaseCat;
        if (diseaseCategories.size() == 1 && diseaseCategories.toArray()[0].toString().equalsIgnoreCase("all") || diseaseCategories.size() > 1) {
            diseaseCat = "All Diseases";
        } else {
            diseaseCat = diseaseCategories.toArray()[0].toString();
        }
        searchingPanel.close();

        selection.setDiseaseCategory(diseaseCat);
        selection.setDatasetIds(datasetIds);

        CSFPR_Central_Manager.searchSelectionAction(selection);
        loadQuantSearching();

    }
    private Set<String> filterKeywordSet;

    private void searchProteins(Query query) {
        query.setValidatedProteins(false);
        query.setSearchDataset("");

        //searching quant data
        String defaultText = query.getSearchKeyWords();
        defaultText = defaultText.replace(",", "\n").trim().toUpperCase();
        filterKeywordSet = new LinkedHashSet<>();
        filterKeywordSet.addAll(Arrays.asList(defaultText.split("\n")));

        defaultText = "";
        defaultText = filterKeywordSet.stream().map((str) -> str + "\n").reduce(defaultText, String::concat);
        query.setSearchKeyWords(defaultText);
        //searching quant data
        query.setSearchDataType("Quantification Data");

        searchQuantificationProtList = Data_handler.searchQuantificationProtein(query, false);

        String quantNotFound = Data_handler.filterQuantSearchingKeywords(searchQuantificationProtList, query.getSearchKeyWords(), query.getSearchBy());
        Map<String, Integer[]> quantHitsList = Data_handler.getQuantHitsList(searchQuantificationProtList, query.getSearchBy());
        if (quantNotFound != null) {
            for (String s : quantNotFound.split(",")) {
                filterKeywordSet.remove(s.trim());
            }
        }
        if (quantHitsList != null && searchQuantificationProtList != null) {
//            Set<Integer> studiesSet = new HashSet<>();
//            searchQuantificationProtList.stream().forEach((qp) -> {
//                studiesSet.add(qp.getDsKey());
//            });
//            initQuantComparisonresults(quantHitsList,  quantNotFound.split(","), filterKeywordSet.size());

            initProteinsQuantDataLayout(quantHitsList, quantNotFound.split(","), filterKeywordSet.size());
        }
//        if (quantNotFound != null && !quantNotFound.trim().equalsIgnoreCase("")) {
//            noresultsLabel.setValue(noresultMessage + " for (" + quantNotFound + ")");
//            noresultsLabel.setVisible(true);
//
//        }

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
            idSearchingLink.setWidth(100, Unit.PERCENTAGE);
            idDataResult.addComponent(idSearchingLink);

        } else {
            idDataResult.setVisible(false);
        }

//        quantSearchingErrorLabel.updateErrot(quantNotFound);
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
    private void initProteinsQuantDataLayout(Map<String, Integer[]> quantHitsList, String[] notFoundAcc, int found) {

        quantDataResult.removeAllComponents();
        overviewResults.removeAllComponents();
        if (quantHitsList == null || quantHitsList.isEmpty()) {
            quantDataResult.setVisible(false);
            noresultsLabel.setVisible(true);
            loadDataBtn.setEnabled(false);
            return;
        }

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
        notFoundChart.setDescription("Click slice to export");
        int notFoundLength = notFoundAcc.length;
        if (notFoundAcc.length == 1 && notFoundAcc[0].trim().equalsIgnoreCase("")) {
            notFoundLength = 0;
        }
        notFoundChart.initializeFilterData(new String[]{"Not Found", "Found"}, new Integer[]{notFoundLength, found, found + notFoundLength}, new Color[]{new Color(219, 169, 1), new Color(110, 177, 206),});
        notFoundChart.redrawChart();
        notFoundChart.redrawChart();
        notFoundChart.getMiddleDountLayout().addStyleName("defaultcursor");
        notFoundChart.getMiddleDountLayout().setDescription("Click slice to export");
        notFoundChart.setDescription("Click slice to export");

        loadDataBtn.setEnabled(true);
        int availableWidth = (int) searchingPanel.getWidth() - 100;
        quantDataResult.setVisible(true);
        noresultsLabel.setVisible(false);
        int maxColNum = Math.max(availableWidth / 250, 1);

        if (quantHitsList.size() <= maxColNum * 2) {
            resultsLayout.setComponentAlignment(quantResultWrapping, Alignment.MIDDLE_CENTER);
            quantDataResult.addComponent(notFoundChart, 0, 0);
            quantDataResult.setComponentAlignment(notFoundChart, Alignment.MIDDLE_CENTER);
            quantDataResult.removeStyleName("marginleft");
            overviewResults.setVisible(false);
            quantDataResult.setColumns(maxColNum);
            quantDataResult.setRows(3);
            int col = 1;
            int row = 0;
            for (String proteinName : quantHitsList.keySet()) {
                PieChart chart = new PieChart(250, 200, proteinName.split("__")[1], true) {

                    @Override
                    public void sliceClicked(Comparable sliceKey) {
                    }

                };
                chart.initializeFilterData(items, quantHitsList.get(proteinName), itemsColors);
                chart.redrawChart();
                chart.setData(proteinName.split("__")[0]);
                quantDataResult.addComponent(chart, col++, row);
                if (col == maxColNum) {
                    col = 0;
                    row++;
                }
            }

        } else {
            quantDataResult.addStyleName("marginleft");
            resultsLayout.setComponentAlignment(quantResultWrapping, Alignment.MIDDLE_LEFT);
            maxColNum = Math.max((availableWidth - 270) / 250, 1);
            overviewResults.setVisible(true);
            overviewResults.addComponent(notFoundChart);
            overviewResults.setComponentAlignment(notFoundChart, Alignment.MIDDLE_CENTER);

            quantDataResult.setColumns(maxColNum);
            quantDataResult.setRows(1000);
            quantDataResult.setHideEmptyRowsAndColumns(true);
            int col = 0;
            int row = 0;
            for (String proteinName : quantHitsList.keySet()) {

                ProteinSearcingResultLabel chart = new ProteinSearcingResultLabel(proteinName, items, quantHitsList.get(proteinName), itemsColors);
                quantDataResult.addComponent(chart, col++, row);
                quantDataResult.setComponentAlignment(chart, Alignment.MIDDLE_CENTER);
//                chart.redrawChart();
                if (col == maxColNum) {
                    col = 0;
                    row++;

                }
            }

        }
        resultsLabel.setValue("Search Results (" + quantHitsList.size() + ")");
        if (smallScreen) {
            int h2 = h1 + 130;
            resultsLayout.setHeight(h2, Unit.PIXELS);
        }
    }

    @Override
    public void onClick() {
        searchingPanel.setVisible(true);

    }

    private StreamResource createProteinsExportResource(Set<String> accessions) {
        return new StreamResource(() -> {
            byte[] csvFile = Data_handler.exportProteinsListToCSV(accessions);
            return new ByteArrayInputStream(csvFile);
        }, "Proteins_List.csv");
    }

    public abstract void loadQuantSearching();

}
