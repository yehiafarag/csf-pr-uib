package no.uib.probe.csf.pr.touch.view.bigscreen.searchinglayoutcontainer.components;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
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
import com.vaadin.ui.PopupView;
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
import no.uib.probe.csf.pr.touch.selectionmanager.CSFListener;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFPR_Central_Manager;
import no.uib.probe.csf.pr.touch.selectionmanager.QuantSearchSelection;
import no.uib.probe.csf.pr.touch.view.core.BigBtn;
import no.uib.probe.csf.pr.touch.view.core.CloseButton;
import no.uib.probe.csf.pr.touch.view.core.InformationButton;
import no.uib.probe.csf.pr.touch.view.core.PieChart;
import no.uib.probe.csf.pr.touch.view.core.PopupWindow;
import no.uib.probe.csf.pr.touch.view.core.ProteinSearcingResultLabel;
import no.uib.probe.csf.pr.touch.view.core.TrendLegend;

/**
 *
 * This class represents quantitative searching window where users can search
 * for protein data using protein name or accession or peptide sequence, the
 * class is a button with pop-up panel layout
 *
 * @author Yehia Farag
 *
 */
public abstract class SearchingComponent extends BigBtn {

    /**
     * The main searching pop-up window (the pop up container) .
     */
    private final PopupWindow searchingPanel;
    /**
     * List of quantitative proteins found in searching.
     */
    private List<QuantProtein> searchQuantificationProtList;
    /**
     * The quant results container layout.
     */
    private final GridLayout quantDataResult;
    /**
     * The id results container layout (the id link container for CSF-PR 1.0).
     */
    private final VerticalLayout idDataResult;
    /**
     * The bottom buttons container it contains also the link to CSF-PR 1.0 id
     * results.
     */
    private final HorizontalLayout controlBtnsLayout;
    /**
     * The button is responsible for invoking the show results in system where
     * the user can visualize his data and compare it with the CSF-PR 2.0
     * results.
     */
    private final Button loadDataBtn;
    /**
     * The searching unit layout has the input data fields for user data.
     */
    private final SearchingUnitComponent searchingUnit;
    /**
     * The central selection manager for handling data across different
     * visualizations and managing all users selections.
     */
    private final CSFPR_Central_Manager CSFPR_Central_Manager;
    /**
     * The results layout is the container for quant data results (the pie
     * charts).
     */
    private final VerticalLayout resultsLayout;
    /**
     * The middle layout has the results label and legend.
     */
    private final HorizontalLayout middleLayout;
    /**
     * The overview pie chart result container.
     */
    private final VerticalLayout overviewResults;
    /**
     * The no results available label
     */
    private final Label noresultsLabel;
    /**
     * The quantitative data handler to work as controller layer to interact
     * between visualization and logic layer .
     */
    private final Data_Handler Data_handler;
    /**
     * The default no results message.
     */
    private final String noresultMessage = "No results found";
    /**
     * The quant data results label (results and hits number).
     */
    private final Label resultsLabel;
    /**
     * The quant proteins pie-chart or labels results container.
     */
    private final HorizontalLayout quantResultWrapping;
    /**
     * The size of screen is used to switch the data visualization mode between
     * normal and compact mode.
     */
    private final boolean smallScreen;
    /**
     * Set of keywords to avoid duplicating the keywords.
     */
    private Set<String> filterKeywordSet;
    /**
     * Array of disease category names.
     */
    private final String[] diseaseCategoryNames = new String[]{"Alzheimer's", "Multiple Sclerosis", "Parkinson's"};
    /**
     * Array of disease category AWT colors required for JFree charts.
     */
    private final Color[] diseaseCategoryColors = new Color[]{Color.decode("#4b7865"), Color.decode("#A52A2A"), Color.decode("#74716E")};

    /**
     * Constructor to initialize the main attributes (data handler and selection
     * manager)
     *
     * @param Data_handler The quantitative data handler.
     * @param CSFPR_Central_Manager The central selection manager
     */
    public SearchingComponent(final Data_Handler Data_handler, CSFPR_Central_Manager CSFPR_Central_Manager) {
        super("Search", "Search protein data", "img/search.png");
        this.smallScreen = Page.getCurrent().getBrowserWindowHeight() <= 940;
        this.Data_handler = Data_handler;
        this.CSFPR_Central_Manager = CSFPR_Central_Manager;
        VerticalLayout popupbodyLayout = new VerticalLayout();
        popupbodyLayout.setSpacing(false);
        popupbodyLayout.setWidth(100, Unit.PERCENTAGE);
        popupbodyLayout.setHeight(100, Unit.PERCENTAGE);
        popupbodyLayout.setMargin(new MarginInfo(false, false, false, false));
        searchingPanel = new PopupWindow(popupbodyLayout, "Search");
        int h1 = 225;
        if (this.smallScreen) {
            searchingPanel.setHeight(Page.getCurrent().getBrowserWindowHeight(), Unit.PIXELS);
            searchingPanel.setWidth(Page.getCurrent().getBrowserWindowWidth() - 20, Unit.PIXELS);
        }
        searchingUnit = new SearchingUnitComponent((int) searchingPanel.getWidth() - 24, h1) {

            @Override
            public void resetSearching() {
                SearchingComponent.this.resetSearch();
            }

            @Override
            public void search(Query query) {
                searchProteins(query);
            }

        };
        popupbodyLayout.addComponent(searchingUnit);
        popupbodyLayout.setExpandRatio(searchingUnit, h1 + 10);

        middleLayout = new HorizontalLayout();
        middleLayout.setMargin(new MarginInfo(false, true));
        middleLayout.setHeight(29, Unit.PIXELS);
        middleLayout.setWidth(100, Unit.PERCENTAGE);
        resultsLabel = new Label("Search Results");
        resultsLabel.setStyleName(ValoTheme.LABEL_BOLD);
        middleLayout.addComponent(resultsLabel);
        middleLayout.setExpandRatio(resultsLabel, 190);
        middleLayout.setComponentAlignment(resultsLabel, Alignment.BOTTOM_LEFT);
        HorizontalLayout legendContainer = new HorizontalLayout();
        legendContainer.setSpacing(true);
        middleLayout.addComponent(legendContainer);
        legendContainer.setSpacing(true);
        middleLayout.addComponent(legendContainer);
        middleLayout.setComponentAlignment(legendContainer, Alignment.MIDDLE_RIGHT);

        TrendLegend legend2 = new TrendLegend("found_notfound");

        TrendLegend legend = new TrendLegend("diseaselegend");

        popupbodyLayout.addComponent(middleLayout);
        popupbodyLayout.setExpandRatio(middleLayout, 30f);

        resultsLayout = new VerticalLayout();
        resultsLayout.addStyleName("roundedborder");
        resultsLayout.addStyleName("whitelayout");
        resultsLayout.addStyleName("padding20");
        resultsLayout.addStyleName("marginleft");
        resultsLayout.addStyleName("marginbottom");
        resultsLayout.addStyleName("scrollable");
        resultsLayout.setWidth(searchingUnit.getWidth(), Unit.PIXELS);
        resultsLayout.setHeight(Math.max(searchingPanel.getHeight() - 30 - 10 - h1 - 30 - 10 - 50 - 10 - 10, 1), Unit.PIXELS);
        popupbodyLayout.addComponent(resultsLayout);
        popupbodyLayout.setExpandRatio(resultsLayout, resultsLayout.getHeight() + 10);

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

        controlBtnsLayout = new HorizontalLayout();
        controlBtnsLayout.addStyleName("roundedborder");
        controlBtnsLayout.addStyleName("whitelayout");
        controlBtnsLayout.addStyleName("padding10");
        controlBtnsLayout.addStyleName("marginleft");
        controlBtnsLayout.addStyleName("marginbottom");
        controlBtnsLayout.setHeight(50, Unit.PIXELS);
        controlBtnsLayout.setWidth(searchingUnit.getWidth(), Unit.PIXELS);

        HorizontalLayout btnsWrapper = new HorizontalLayout();
        controlBtnsLayout.addComponent(btnsWrapper);
        controlBtnsLayout.setComponentAlignment(btnsWrapper, Alignment.TOP_LEFT);
        controlBtnsLayout.setExpandRatio(btnsWrapper, controlBtnsLayout.getWidth() - 130);
        btnsWrapper.setSpacing(true);

        InformationButton info = new InformationButton("Searching allows the user to locate a specific protein or a group of proteins. Input the search text at the top, select the input type and the disease category, and click \"Search\". A graphical overview of the results will be displayed at the bottom. You can either load all the results or select a subset via the charts before loading.", true);
        btnsWrapper.addComponent(info);

        idDataResult = new VerticalLayout();
        idDataResult.addStyleName("marginleft");
        btnsWrapper.addComponent(idDataResult);

        HorizontalLayout rightBtnWrapper = new HorizontalLayout();
        rightBtnWrapper.setSizeUndefined();
        rightBtnWrapper.setSpacing(true);
        controlBtnsLayout.addComponent(rightBtnWrapper);
        controlBtnsLayout.setComponentAlignment(rightBtnWrapper, Alignment.MIDDLE_RIGHT);
        controlBtnsLayout.setExpandRatio(rightBtnWrapper, 130);
        Button resetBtn = new Button("Reset");
        resetBtn.setStyleName(ValoTheme.BUTTON_SMALL);
        resetBtn.setStyleName(ValoTheme.BUTTON_TINY);
        resetBtn.setWidth(60, Unit.PIXELS);
        resetBtn.setEnabled(true);
        rightBtnWrapper.addComponent(resetBtn);
        rightBtnWrapper.setComponentAlignment(resetBtn, Alignment.MIDDLE_RIGHT);
        resetBtn.setDescription("Load data");
        resetBtn.addClickListener((Button.ClickEvent event) -> {
            resetSearch();
            searchingUnit.reset();
            CSFPR_Central_Manager.resetSearchSelection();
        });

        loadDataBtn = new Button("Load");
        loadDataBtn.setStyleName(ValoTheme.BUTTON_SMALL);
        loadDataBtn.setStyleName(ValoTheme.BUTTON_TINY);
        loadDataBtn.setWidth(60, Unit.PIXELS);
        loadDataBtn.setEnabled(false);
        rightBtnWrapper.addComponent(loadDataBtn);
        rightBtnWrapper.setComponentAlignment(loadDataBtn, Alignment.MIDDLE_RIGHT);
        loadDataBtn.setDescription("Load data");
        loadDataBtn.addClickListener((Button.ClickEvent event) -> {
            loadSearching();
        });

        popupbodyLayout.addComponent(controlBtnsLayout);
        popupbodyLayout.setExpandRatio(controlBtnsLayout, 70);
        CSFPR_Central_Manager.registerListener(new CSFListener() {

            @Override
            public void selectionChanged(String type) {
                if (type.equalsIgnoreCase("reset_quant_searching")) {
                    resetSearch();
                }
            }

            @Override
            public String getListenerId() {
                return "searching_component";
            }

        });
        if (this.smallScreen) {
            resultsLayout.setVisible(false);
            middleLayout.setVisible(false);
            searchingPanel.setHeight((10 + Math.max(searchingPanel.getHeight() - 30 - 10 - 30 - 50 - 10 - 10, 1) + 30 + 50 + 10 + 30), Unit.PIXELS);

            resultsLayout.setWidth(searchingUnit.getWidth(), searchingUnit.getWidthUnits());
            resultsLayout.setHeight(Math.max(searchingPanel.getHeight() - 30 - 10 - 30 - 50 - 10 - 10, 1), searchingUnit.getHeightUnits());
            popupbodyLayout.setExpandRatio(resultsLayout, resultsLayout.getHeight());

            searchingUnit.setHeight(resultsLayout.getHeight() + 20, resultsLayout.getHeightUnits());
            popupbodyLayout.setExpandRatio(searchingUnit, searchingUnit.getHeight());

            popupbodyLayout.setHeight((searchingPanel.getHeight() - 30), Unit.PIXELS);

        } else {
            popupbodyLayout.setHeight(10 + h1 + 30 + resultsLayout.getHeight() + 20 + 50 + 10, Unit.PIXELS);
        }
        middleLayout.setExpandRatio(legendContainer, searchingPanel.getWidth() - 190);
        if (searchingPanel.getWidth() - 190 < 436) {
            middleLayout.removeComponent(legendContainer);
            CloseButton closeBtn = new CloseButton();
            VerticalLayout legendPopup = new VerticalLayout();
            legendPopup.addComponent(closeBtn);
            legendPopup.setExpandRatio(closeBtn, 2);
            Set<Component> set = new LinkedHashSet<>();
            Iterator<Component> itr = legend2.iterator();
            while (itr.hasNext()) {
                set.add(itr.next());
            }
            VerticalLayout spacer = new VerticalLayout();
            spacer.setHeight(15, Unit.PIXELS);
            spacer.setWidth(20, Unit.PIXELS);
            set.add(spacer);
            Iterator<Component> itr2 = legend.iterator();
            while (itr2.hasNext()) {
                set.add(itr2.next());
            }

            for (Component c : set) {
                legendPopup.addComponent(c);
                legendPopup.setExpandRatio(c, c.getHeight() + 5);
            }
            legend2.setSpacing(true);
            legendPopup.setWidth(150, Unit.PIXELS);
            legendPopup.setHeight(100, Unit.PIXELS);
            final PopupView popup = new PopupView("Legend", legendPopup);
            legendPopup.addStyleName("compactlegend");
            popup.setHideOnMouseOut(false);
            closeBtn.addLayoutClickListener((LayoutEvents.LayoutClickEvent event) -> {
                popup.setPopupVisible(false);

            });
            middleLayout.addComponent(popup);
            middleLayout.setComponentAlignment(popup, Alignment.MIDDLE_RIGHT);
            middleLayout.setExpandRatio(popup, searchingPanel.getWidth() - 190);
        } else {
            middleLayout.setExpandRatio(legendContainer, searchingPanel.getWidth() - 190);
            legendContainer.addComponent(legend2);
            legendContainer.setComponentAlignment(legend2, Alignment.MIDDLE_RIGHT);
            legend2.addStyleName("marginright");

            legendContainer.addComponent(legend);
            legendContainer.setComponentAlignment(legend, Alignment.MIDDLE_RIGHT);
            legend.addStyleName("marginright");

        }

    }

    /**
     * Reset comparison layout by clearing all input and results fields and
     * reset the system to default.
     */
    private void resetSearch() {
        resultsLabel.setValue("Search Results");
        idDataResult.setVisible(false);
        noresultsLabel.setVisible(false);
        quantDataResult.removeAllComponents();
        overviewResults.removeAllComponents();
        loadDataBtn.setEnabled(false);
        if (smallScreen) {
            resultsLayout.setVisible(false);
            middleLayout.setVisible(false);
            searchingUnit.setVisible(true);

        }

    }

    /**
     * Load and invoke searching mode in the system to visualize the searching
     * results data in the system.
     */
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
        Map<String, Set<Integer>> diseaseCategoriesIdMap = new HashMap<>();
        Set<String> proteinAccession = new HashSet<>();
        QuantSearchSelection selection = new QuantSearchSelection();
        if (noSelection) {
            selection.setKeyWords(filterKeywordSet);
            searchQuantificationProtList.stream().forEach((protein) -> {
                datasetIds.add(protein.getQuantDatasetIndex());
                diseaseCategories.add(protein.getDiseaseCategory());
                proteinAccession.add(protein.getFinalAccession());

                if (!diseaseCategoriesIdMap.containsKey(protein.getDiseaseCategory())) {
                    diseaseCategoriesIdMap.put(protein.getDiseaseCategory(), new HashSet<>());
                }
                Set<Integer> datasetIdSet = diseaseCategoriesIdMap.get(protein.getDiseaseCategory());
                datasetIdSet.add(protein.getQuantDatasetIndex());
                diseaseCategoriesIdMap.put(protein.getDiseaseCategory(), datasetIdSet);

            });

        } else {
            selection.setKeyWords(proteinList.keySet());
            searchQuantificationProtList.stream().filter((protein) -> (proteinList.keySet().contains(protein.getFinalAccession()) && (proteinList.get(protein.getFinalAccession()).contains("all") || proteinList.get(protein.getFinalAccession()).contains(protein.getDiseaseCategory())))).forEach((protein) -> {

                datasetIds.add(protein.getQuantDatasetIndex());
                proteinAccession.add(protein.getFinalAccession());
                if (!diseaseCategoriesIdMap.containsKey(protein.getDiseaseCategory())) {
                    diseaseCategoriesIdMap.put(protein.getDiseaseCategory(), new HashSet<>());
                }
                Set<Integer> datasetIdSet = diseaseCategoriesIdMap.get(protein.getDiseaseCategory());
                datasetIdSet.add(protein.getQuantDatasetIndex());
                diseaseCategoriesIdMap.put(protein.getDiseaseCategory(), datasetIdSet);

            });
        }

        String diseaseCat;
        if (diseaseCategories.size() == 1 && diseaseCategories.toArray()[0].toString().equalsIgnoreCase("all") || diseaseCategories.size() > 1) {
            diseaseCat = "All Diseases";
        } else {
            diseaseCat = diseaseCategories.toArray()[0].toString();
        }
        searchingPanel.close();
        selection.setDiseaseCategoriesIdMap(diseaseCategoriesIdMap);
        selection.setDiseaseCategory(diseaseCat);
        selection.setQuantDatasetIndexes(datasetIds);
        selection.setSelectedProteinsList(proteinAccession);
        Data_handler.switchToSearchingMode(selection);
        CSFPR_Central_Manager.resetSearchSelection();
        loadQuantSearching();
        CSFPR_Central_Manager.searchSelectionAction(selection);

    }

    /**
     * Perform data query for user input data against the database
     *
     * @param query constructed query from user input data
     */
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
            if (quantNotFound != null) {
                initProteinsQuantDataLayout(quantHitsList, quantNotFound.split(","), filterKeywordSet.size());
            }
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
            idSearchingLink.addStyleName("smalllink");
            idSearchingLink.setDescription("View protein id results in CSF-PR v1.0");
            idSearchingLink.setWidth(100, Unit.PERCENTAGE);
            idDataResult.addComponent(idSearchingLink);

        } else {
            idDataResult.setVisible(false);
        }

        if (smallScreen) {
            resultsLayout.setVisible(true);
            middleLayout.setVisible(true);
            searchingUnit.setVisible(false);
        }

    }

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
        notFoundChart.getMiddleDonutLayout().addStyleName("defaultcursor");
        notFoundChart.getMiddleDonutLayout().setDescription("Click slice to export");
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
                chart.initializeFilterData(diseaseCategoryNames, quantHitsList.get(proteinName), diseaseCategoryColors);
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

                ProteinSearcingResultLabel chart = new ProteinSearcingResultLabel(proteinName, diseaseCategoryNames, quantHitsList.get(proteinName), diseaseCategoryColors);
                quantDataResult.addComponent(chart, col++, row);
                quantDataResult.setComponentAlignment(chart, Alignment.MIDDLE_CENTER);
                if (col == maxColNum) {
                    col = 0;
                    row++;

                }
            }

        }
        resultsLabel.setValue("Search Results (" + quantHitsList.size() + ")");

    }

    /**
     * On click view the searching panel.
     */
    @Override
    public void onClick() {
        searchingPanel.setVisible(true);

    }

    /**
     * Load and invoke searching mode in the system to visualize the searching
     * results data in the system
     */
    public abstract void loadQuantSearching();

    /**
     * Create and initialize not found proteins file that has proteins
     * accessions list.
     *
     * @param accessions set of not found protein accessions
     * @return a StreamResource for protein exporting file
     */
    private StreamResource createProteinsExportResource(Set<String> accessions) {
        return new StreamResource(() -> {
            byte[] csvFile = Data_handler.exportProteinsListToCSV(accessions);
            return new ByteArrayInputStream(csvFile);
        }, "Proteins_List.csv");
    }
}
