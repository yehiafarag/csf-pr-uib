/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview;

import com.vaadin.addon.tableexport.CsvExport;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.MultiSelectMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import probe.com.handlers.CSFPRHandler;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.DiseaseGroupsComparisonsProteinLayout;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
import probe.com.model.beans.quant.QuantProtein;
import probe.com.selectionmanager.CSFFilter;
import probe.com.selectionmanager.DatasetExploringCentralSelectionManager;
import probe.com.view.core.CustomExternalLink;
import probe.com.view.core.jfreeutil.SquaredDot;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.ComparisonChartContainer;

/**
 *
 * @author Yehia Farag
 *
 * this class represents the top right side from the quant data tab the
 * container contains the quantification proteins table, searching field, table
 * buttons and the overview bar charts
 */
public class QuantProteinsComparisonsContainer extends VerticalLayout implements LayoutEvents.LayoutClickListener, CSFFilter, Property.ValueChangeListener {

    private final DatasetExploringCentralSelectionManager selectionManager;
    private final Table groupsComparisonProteinsTable;
    private final CSFPRHandler handler;
    private final HorizontalLayout topLayout, chartsLayoutContainer, bottomLayout;
    private final GridLayout searchingFieldLayout;
    private final TextField searchField;
    private final VerticalLayout searchingBtn;
    private final Button resetSearchBtn;
    private final VerticalLayout startLayout;
    private final VerticalLayout hideDiseaseGroupsComparisonsHeatmapBtn;
    private final Label protCounterLabel;
    private int width = 0;
    private final OptionGroup hideUniqueProteinsOption;
    private String sortComparisonTableColumn;
    private final List<QuantProtein> searchQuantificationProtList;
    private final Set<QuantDiseaseGroupsComparison> diseaseGroupsComparisonsMap = new HashSet<QuantDiseaseGroupsComparison>();
    private final Map<String, String> quantAccessionMap = new HashMap<String, String>();
    private QuantDiseaseGroupsComparison[] quantDiseaseGroupsComparisonArr = new QuantDiseaseGroupsComparison[]{};
    private boolean selfselection = false;
    private boolean externalSelection = false;
    private final Set<ComparisonChartContainer> quantBarchartSet = new HashSet<ComparisonChartContainer>();
    private ComparisonChartContainer lastHeighlitedChart;
    private Map<String, DiseaseGroupsComparisonsProteinLayout[]> diseaseGroupsComparisonsProteinsMap;
    private final Set<Integer> proteinskeys = new HashSet<Integer>();
    private final TreeMap<Integer, CustomExternalLink> lastSelectedProts = new TreeMap<Integer, CustomExternalLink>();

    /**
     * update the layout width based on the available space (on hide/show
     * disease groups comparisons heat-map filters )
     *
     * @param width the update layout width
     */
    public void setLayoutWidth(int width) {
        this.width = width;
        this.setWidth(width + "px");
        this.bottomLayout.setWidth(width + "px");
        float ratio = 360f / (float) width;
        int columnWidth = 400;
        boolean useRatio = false;
        if (quantDiseaseGroupsComparisonArr.length > 1) {
            if ((quantDiseaseGroupsComparisonArr.length * 400) > (width - 360)) {
                useRatio = true;
                int persWidth = (int) (100.0 - (16.0 * 100.0 / (double) width));
                chartsLayoutContainer.setWidth(persWidth + "%");
                int contWid = (persWidth * (width - 360) / 100);
                columnWidth = contWid / quantDiseaseGroupsComparisonArr.length;

            } else {
                useRatio = false;
                chartsLayoutContainer.setWidth((quantDiseaseGroupsComparisonArr.length * 400) + "px");
            }
        } else {//ok
            chartsLayoutContainer.setWidth((quantDiseaseGroupsComparisonArr.length * 400) + "px");
        }

        topLayout.setExpandRatio(searchingFieldLayout, ratio);
        topLayout.setExpandRatio(chartsLayoutContainer, (1f - ratio));
        resizeCharts(columnWidth);
        resizeTable(useRatio, ratio);
        updateQuantProteinsSparkLineLabels(columnWidth);

    }

    /**
     * update the spark line label width based on the available column space
     *
     * @param columnWidth the update layout width
     */
    private void updateQuantProteinsSparkLineLabels(int columnWidth) {
        if (diseaseGroupsComparisonsProteinsMap != null) {
            for (DiseaseGroupsComparisonsProteinLayout[] cpArr : diseaseGroupsComparisonsProteinsMap.values()) {
                for (DiseaseGroupsComparisonsProteinLayout cp : cpArr) {
                    if (cp != null) {
                        cp.updateWidth(columnWidth);
                    }

                }

            }

        }

    }

    /**
     *
     * @param datasetExploringCentralSelectionManager
     * @param csfprHandler
     * @param searchQuantificationProtList
     */
    public QuantProteinsComparisonsContainer(DatasetExploringCentralSelectionManager datasetExploringCentralSelectionManager, final CSFPRHandler csfprHandler, List<QuantProtein> searchQuantificationProtList) {
        this.selectionManager = datasetExploringCentralSelectionManager;
        this.searchQuantificationProtList = searchQuantificationProtList;
        datasetExploringCentralSelectionManager.registerFilter(QuantProteinsComparisonsContainer.this);
        this.handler = csfprHandler;
        this.setWidth("100%");
        this.setHeightUndefined();
        this.setStyleName(Reindeer.LAYOUT_WHITE);
        this.setMargin(false);
        this.setSpacing(true);

        startLayout = new VerticalLayout();
        this.addComponent(startLayout);
        startLayout.setWidth("100%");
        startLayout.setHeightUndefined();

        VerticalLayout spacer = new VerticalLayout();
        spacer.setHeight("250px");
        spacer.setWidth("100%");
        spacer.setStyleName(Reindeer.LAYOUT_WHITE);
        startLayout.addComponent(spacer);

        Label startLabel = new Label("<center><h2 style='color:gray;'><b>Select comparison from the table</b></h2></center>");
        startLabel.setContentMode(ContentMode.HTML);

        startLayout.addComponent(startLabel);
        startLayout.setComponentAlignment(startLabel, Alignment.MIDDLE_CENTER);

        Image handleft = new Image();
        handleft.setSource(new ThemeResource("img/handleft.png"));
        startLayout.addComponent(handleft);
        startLayout.setComponentAlignment(handleft, Alignment.MIDDLE_CENTER);

        topLayout = new HorizontalLayout();
        topLayout.setVisible(false);
        topLayout.setHeight("250px");
        topLayout.setWidth("100%");
        topLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        topLayout.setSpacing(false);
        searchingFieldLayout = new GridLayout();
        searchingFieldLayout.setSpacing(true);
        searchingFieldLayout.setHeight("250px");
        searchingFieldLayout.setColumns(2);
        searchingFieldLayout.setRows(2);
        searchingFieldLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        searchingFieldLayout.setColumnExpandRatio(0, 0.4f);
        searchingFieldLayout.setColumnExpandRatio(1, 0.6f);

        chartsLayoutContainer = new HorizontalLayout();
        chartsLayoutContainer.setHeight("250px");
        int persWidth = (int) (100.0 - (16.0 * 100.0 / (double) width));
        chartsLayoutContainer.setWidth(persWidth + "%");
        chartsLayoutContainer.setStyleName(Reindeer.LAYOUT_WHITE);

        topLayout.addComponent(searchingFieldLayout);
        topLayout.addComponent(chartsLayoutContainer);
        // add searching field to spacer
        //allow search in 
        VerticalLayout topSpacer = new VerticalLayout();
        topSpacer.setHeight("180px");
        topSpacer.setWidth("50%");
        searchingFieldLayout.addComponent(topSpacer, 1, 0);
        searchingFieldLayout.setComponentAlignment(topSpacer, Alignment.TOP_LEFT);
        topSpacer.setStyleName(Reindeer.LAYOUT_BLUE);
        topSpacer.setMargin(new MarginInfo(false, false, false, false));

        //hide show comp table 
        hideDiseaseGroupsComparisonsHeatmapBtn = new VerticalLayout();
        hideDiseaseGroupsComparisonsHeatmapBtn.setMargin(new MarginInfo(false, false, false, false));
        hideDiseaseGroupsComparisonsHeatmapBtn.setWidth("150px");
        hideDiseaseGroupsComparisonsHeatmapBtn.setHeight("150px");
        hideDiseaseGroupsComparisonsHeatmapBtn.setVisible(false);
        hideDiseaseGroupsComparisonsHeatmapBtn.setDescription("Show Comparison Table");
        hideDiseaseGroupsComparisonsHeatmapBtn.setStyleName("matrixbtn");
        Label l = new Label("Show Comparsions");
        hideDiseaseGroupsComparisonsHeatmapBtn.addComponent(l);
        hideDiseaseGroupsComparisonsHeatmapBtn.setComponentAlignment(l, Alignment.BOTTOM_CENTER);

        searchingFieldLayout.addComponent(hideDiseaseGroupsComparisonsHeatmapBtn, 0, 0);
        searchingFieldLayout.setComponentAlignment(hideDiseaseGroupsComparisonsHeatmapBtn, Alignment.TOP_LEFT);

        HorizontalLayout searchFieldContainerLayout = new HorizontalLayout();
        searchFieldContainerLayout.setWidthUndefined();
        searchFieldContainerLayout.setSpacing(true);
        searchField = new TextField();
        searchField.setDescription("Search Proteins By Name or Accession");
        searchField.setImmediate(true);
        searchField.setWidth("100%");
        searchField.setHeight("24px");
        searchField.setInputPrompt("Search...");
        searchFieldContainerLayout.addComponent(searchField);

        searchingBtn = new VerticalLayout();
        searchingBtn.setWidth("30px");
        searchingBtn.setHeight("24px");
        searchingBtn.setStyleName("tablesearchingbtn");
        searchFieldContainerLayout.addComponent(searchingBtn);
        searchFieldContainerLayout.setComponentAlignment(searchingBtn, Alignment.MIDDLE_CENTER);
        searchingFieldLayout.addComponent(searchFieldContainerLayout, 0, 1);
        searchingFieldLayout.setComponentAlignment(searchFieldContainerLayout, Alignment.BOTTOM_LEFT);
        protCounterLabel = new Label("");
        protCounterLabel.setWidth("100%");
        protCounterLabel.setHeight("24px");
        protCounterLabel.setContentMode(ContentMode.HTML);
        searchingFieldLayout.addComponent(protCounterLabel, 1, 1);
        searchingFieldLayout.setComponentAlignment(protCounterLabel, Alignment.BOTTOM_LEFT);

        resetSearchBtn = new Button("Reset Table");
        resetSearchBtn.setStyleName(Reindeer.BUTTON_LINK);
        resetSearchBtn.setWidth("76px");
        resetSearchBtn.setHeight("24px");

        this.addComponent(topLayout);
        groupsComparisonProteinsTable = new Table();
        groupsComparisonProteinsTable.setVisible(false);
        this.addComponent(groupsComparisonProteinsTable);
        this.setComponentAlignment(groupsComparisonProteinsTable, Alignment.MIDDLE_CENTER);
        this.groupsComparisonProteinsTable.setSelectable(true);
        this.groupsComparisonProteinsTable.setColumnReorderingAllowed(true);
        this.groupsComparisonProteinsTable.setColumnCollapsingAllowed(true);
        this.groupsComparisonProteinsTable.setImmediate(true); // react at once when something is selected
        groupsComparisonProteinsTable.setWidth("100%");
        groupsComparisonProteinsTable.setHeight("400px");
        groupsComparisonProteinsTable.addValueChangeListener(QuantProteinsComparisonsContainer.this);
        groupsComparisonProteinsTable.setMultiSelect(true);
        groupsComparisonProteinsTable.setMultiSelectMode(MultiSelectMode.DEFAULT);
        //add searching listeners 
        searchingBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                hideUniqueProteinsOption.unselect("Hide unique proteins");
                Set<String> subAccList = searchProteins(searchField.getValue());
                if (subAccList.isEmpty()) {
                    Notification.show("Not available");
                    return;
                } else {
                    filterTable(subAccList, quantDiseaseGroupsComparisonArr, sortComparisonTableColumn);
                    resetAllCharts();
                }
                updateProtCountLabel(subAccList.size());

            }
        });

        Button b = new Button();
        b.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                hideUniqueProteinsOption.unselect("Hide unique proteins");
                Set<String> subAccList = searchProteins(searchField.getValue());
                if (subAccList.isEmpty()) {
                    Notification.show("Not available");
                    return;
                } else {
                    filterTable(subAccList, quantDiseaseGroupsComparisonArr, sortComparisonTableColumn);
                    resetAllCharts();
                }
                updateProtCountLabel(subAccList.size());
            }
        });
        searchField.addShortcutListener(new Button.ClickShortcut(b, ShortcutListener.KeyCode.ENTER));
        resetSearchBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                searchField.clear();
                updateTableData(quantDiseaseGroupsComparisonArr, null);
                updateProtCountLabel(quantAccessionMap.size());
            }
        });
        bottomLayout = new HorizontalLayout();
        bottomLayout.setHeight("100%");
        bottomLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        HorizontalLayout leftBottomLayout = new HorizontalLayout();
        leftBottomLayout.setWidthUndefined();
        bottomLayout.addComponent(leftBottomLayout);
        bottomLayout.setComponentAlignment(leftBottomLayout, Alignment.TOP_LEFT);

        hideUniqueProteinsOption = new OptionGroup();
        leftBottomLayout.addComponent(hideUniqueProteinsOption);
        leftBottomLayout.setComponentAlignment(hideUniqueProteinsOption, Alignment.TOP_LEFT);
        hideUniqueProteinsOption.setWidth("150px");
        hideUniqueProteinsOption.setNullSelectionAllowed(true); // user can not 'unselect'
        hideUniqueProteinsOption.setMultiSelect(true);

        hideUniqueProteinsOption.addItem("Hide unique proteins");
        hideUniqueProteinsOption.addStyleName("horizontal");
        hideUniqueProteinsOption.addValueChangeListener(new Property.ValueChangeListener() {
            private final Set<String> protAccssions = new HashSet<String>();
            private final HashMap<Object, Item> tableItems = new HashMap<Object, Item>();

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (hideUniqueProteinsOption.getValue().toString().equalsIgnoreCase("[Hide unique proteins]")) {
                    protAccssions.clear();
                    tableItems.clear();
                    HashSet<Object> itemIds = new HashSet<Object>(groupsComparisonProteinsTable.getItemIds());
                    for (Object id : itemIds) {

                        Item item = groupsComparisonProteinsTable.getItem(id);
                        tableItems.put(id, item);
                        protAccssions.add(item.getItemProperty("Accession").toString());
                        for (QuantDiseaseGroupsComparison gc : quantDiseaseGroupsComparisonArr) {
                            if (item.getItemProperty(gc.getComparisonHeader()).getValue() == null) {
                                groupsComparisonProteinsTable.removeItem(id);
                                break;
                            }
                        }
                    }

                } else {

                    filterTable(protAccssions, quantDiseaseGroupsComparisonArr, sortComparisonTableColumn);
                }
                updateProtCountLabel(groupsComparisonProteinsTable.getItemIds().size());
            }
        });

        Button selectAllBtn = new Button("Select all");
        selectAllBtn.setHeight("30px");
        selectAllBtn.setWidth("60px");

        selectAllBtn.setStyleName(Reindeer.BUTTON_LINK);

        leftBottomLayout.addComponent(selectAllBtn);
        leftBottomLayout.setComponentAlignment(selectAllBtn, Alignment.TOP_LEFT);
        selectAllBtn.setDescription("Select all data");

        selectAllBtn.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                selectAll();
            }
        });
        Button unSelectAllBtn = new Button("Unselect all");
        unSelectAllBtn.setHeight("30px");
        unSelectAllBtn.setWidth("74px");

        unSelectAllBtn.setStyleName(Reindeer.BUTTON_LINK);

        leftBottomLayout.addComponent(unSelectAllBtn);
        leftBottomLayout.setComponentAlignment(unSelectAllBtn, Alignment.TOP_LEFT);
        unSelectAllBtn.setDescription("Unselect all data");

        unSelectAllBtn.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                unSelectAll();
            }
        });

        leftBottomLayout.addComponent(resetSearchBtn);
        leftBottomLayout.setComponentAlignment(resetSearchBtn, Alignment.TOP_LEFT);

        Button exportTableBtn = new Button("Export Table");
        exportTableBtn.setHeight("30px");
        exportTableBtn.setWidth("83px");
        exportTableBtn.setStyleName(Reindeer.BUTTON_LINK);
        bottomLayout.addComponent(exportTableBtn);
        bottomLayout.setComponentAlignment(exportTableBtn, Alignment.TOP_RIGHT);
        exportTableBtn.setDescription("Export table data");
        bottomLayout.setHeight("100%");
        bottomLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        exportTableBtn.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                CsvExport csvExport = new CsvExport(groupsComparisonProteinsTable, "CSF-PR  Quant Comparisons Proteins");
                csvExport.setReportTitle("CSF-PR / Quant Comparisons / Proteins ");
                csvExport.setExportFileName("CSF-PR - Quant Comparisons - Proteins" + ".csv");
                csvExport.setMimeType(CsvExport.CSV_MIME_TYPE);
                csvExport.setDisplayTotals(false);
                csvExport.export();

            }
        });
        bottomLayout.setVisible(false);

    }

    /**
     * get lower layout (control buttons layout) of the quant proteins table
     *
     * @return bottom layout (buttons container)
     */
    public HorizontalLayout getBottomLayout() {
        return bottomLayout;
    }

    /**
     *
     * @return hide Disease Groups Comparisons Heat-map button
     */
    public VerticalLayout getHideDiseaseGroupsComparisonsHeatmapBtn() {
        return hideDiseaseGroupsComparisonsHeatmapBtn;
    }

    /**
     * event on the central selection manager
     *
     * @param type
     */
    @Override
    public void selectionChanged(String type) {
        if (type.equalsIgnoreCase("Comparison_Selection")) {
            Set<String> selectedQuantAccessionsSet = null;
            Set<QuantDiseaseGroupsComparison> selectedComparisonList = selectionManager.getSelectedDiseaseGroupsComparisonList();
            Set<QuantDiseaseGroupsComparison> newComparisons = new HashSet<QuantDiseaseGroupsComparison>();
            Set<QuantDiseaseGroupsComparison> removingComparisons = new HashSet<QuantDiseaseGroupsComparison>();
            for (QuantDiseaseGroupsComparison comparison : selectedComparisonList) {
                if (!diseaseGroupsComparisonsMap.contains(comparison)) {
                    newComparisons.add(comparison);
                }
            }
            for (QuantDiseaseGroupsComparison comparison : diseaseGroupsComparisonsMap) {
                if (!selectedComparisonList.contains(comparison)) {
                    removingComparisons.add(comparison);
                }
            }
            newComparisons = handler.getComparisonProtList(newComparisons, searchQuantificationProtList);
            for (QuantDiseaseGroupsComparison comparison : removingComparisons) {
                diseaseGroupsComparisonsMap.remove(comparison);
            }
            QuantDiseaseGroupsComparison[] tempDiseaseGroupsComparisonsArray = new QuantDiseaseGroupsComparison[diseaseGroupsComparisonsMap.size() + newComparisons.size()];
            int u = 0;
            for (QuantDiseaseGroupsComparison diseaseGroupsComparison : quantDiseaseGroupsComparisonArr) {
                if (diseaseGroupsComparisonsMap.contains(diseaseGroupsComparison)) {
                    tempDiseaseGroupsComparisonsArray[u] = diseaseGroupsComparison;
                    u++;
                }
            }
            for (QuantDiseaseGroupsComparison comparison : newComparisons) {
                tempDiseaseGroupsComparisonsArray[u] = comparison;
                u++;
            }
            quantDiseaseGroupsComparisonArr = tempDiseaseGroupsComparisonsArray;
            diseaseGroupsComparisonsMap.clear();
            diseaseGroupsComparisonsMap.addAll(Arrays.asList(quantDiseaseGroupsComparisonArr));
            if (diseaseGroupsComparisonsMap.isEmpty()) {
                startLayout.setVisible(true);
                topLayout.setVisible(false);
                groupsComparisonProteinsTable.setVisible(false);
                bottomLayout.setVisible(false);
                searchField.clear();
            } else {
                if (selectionManager.getQuantProteinsLayoutSelectionMap() != null) {
                    selectedQuantAccessionsSet = selectionManager.getQuantProteinsLayoutSelectionMap().keySet();
                } else {
                    selectedQuantAccessionsSet = new HashSet<String>();
                }
                startLayout.setVisible(false);
                topLayout.setVisible(true);
                groupsComparisonProteinsTable.setVisible(true);
                bottomLayout.setVisible(true);
            }
            selectionManager.updateSelectedComparisonList(new LinkedHashSet<QuantDiseaseGroupsComparison>(Arrays.asList(quantDiseaseGroupsComparisonArr)));
            updateTableData(quantDiseaseGroupsComparisonArr, selectedQuantAccessionsSet);
            updateProtCountLabel(quantAccessionMap.size());

        } else if (type.equalsIgnoreCase("Quant_Proten_Selection") && !selfselection) {
            externalSelection = true;
            selfselection = false;
            Set<String> selectedQuantAccessionsSet = selectionManager.getQuantProteinsLayoutSelectionMap().keySet();
            if (selectedQuantAccessionsSet.isEmpty()) {
                updateTableData(quantDiseaseGroupsComparisonArr, null);
            } else {
                updateTableData(quantDiseaseGroupsComparisonArr, selectedQuantAccessionsSet);
            }

        } else if (selfselection) {
            selfselection = false;
        }

    }

    /**
     * update the quant protein table counter label
     *
     * @param number the current updated number
     */
    private void updateProtCountLabel(int number) {
        protCounterLabel.setValue("<b>(" + number + "/" + quantAccessionMap.size() + ")</b>");

    }

    /**
     * get filter id
     *
     * @return filter name
     */
    @Override
    public String getFilterId() {
        return "comparisonTable";
    }

    /**
     * remove (un select) filter from central selection manager (not apply to
     * this class)
     *
     * @param value
     */
    @Override
    public void removeFilterValue(String value) {
    }

    /**
     * update table items
     *
     * @param quantDiseaseGroupsComparisonArr
     * @param selectedQuantAccessionsSet
     */
    private void updateTableData(QuantDiseaseGroupsComparison[] quantDiseaseGroupsComparisonArr, Set<String> selectedQuantAccessionsSet) {
        this.chartsLayoutContainer.removeAllComponents();
        this.groupsComparisonProteinsTable.removeAllItems();
        quantBarchartSet.clear();
        quantAccessionMap.clear();
        boolean useRatio = false;
        int columnWidth = 400;
        if (quantDiseaseGroupsComparisonArr.length > 1) {
            if ((quantDiseaseGroupsComparisonArr.length * 400) > (width - 360)) {
                useRatio = true;
                int persWidth = (int) (100.0 - (16.0 * 100.0 / (double) width));
                chartsLayoutContainer.setWidth(persWidth + "%");
                int contWid = (persWidth * (width - 360) / 100);
                columnWidth = contWid / quantDiseaseGroupsComparisonArr.length;

            } else {
                chartsLayoutContainer.setWidth((quantDiseaseGroupsComparisonArr.length * 400) + "px");
                useRatio = false;
            }
        } else {
            chartsLayoutContainer.setWidth((quantDiseaseGroupsComparisonArr.length * 400) + "px");
        }
        float ratio = 360f / (float) width;
        topLayout.setExpandRatio(searchingFieldLayout, ratio);
        topLayout.setExpandRatio(chartsLayoutContainer, (1f - ratio));
        List<Object> arr = new ArrayList<Object>();
        arr.addAll(groupsComparisonProteinsTable.getContainerPropertyIds());
        for (Object col : arr) {
            groupsComparisonProteinsTable.removeContainerProperty(col);
        }
        this.groupsComparisonProteinsTable.addContainerProperty("Index", Integer.class, null, "", null, Table.Align.RIGHT);
        this.groupsComparisonProteinsTable.addContainerProperty("Accession", CustomExternalLink.class, null, "Accession", null, Table.Align.LEFT);
        this.groupsComparisonProteinsTable.addContainerProperty("Name", String.class, null, "Name", null, Table.Align.LEFT);

        diseaseGroupsComparisonsProteinsMap = new HashMap<String, DiseaseGroupsComparisonsProteinLayout[]>();
        for (int compIndex = 0; compIndex < quantDiseaseGroupsComparisonArr.length; compIndex++) {
            QuantDiseaseGroupsComparison comp = quantDiseaseGroupsComparisonArr[compIndex];
            ComparisonChartContainer chartPlot = generateColumnBarChart(comp, columnWidth, (searchQuantificationProtList != null));
            this.chartsLayoutContainer.addComponent(chartPlot);
            this.chartsLayoutContainer.setComponentAlignment(chartPlot, Alignment.MIDDLE_CENTER);
            this.groupsComparisonProteinsTable.addContainerProperty(comp.getComparisonHeader(), DiseaseGroupsComparisonsProteinLayout.class, null, comp.getComparisonHeader() + " (#Proteins: " + comp.getComparProtsMap().size() + ")", null, Table.Align.CENTER);
            groupsComparisonProteinsTable.setColumnWidth(comp.getComparisonHeader(), 100);

            Map<String, DiseaseGroupsComparisonsProteinLayout> protList = comp.getComparProtsMap();

            for (String key2 : protList.keySet()) {
                DiseaseGroupsComparisonsProteinLayout prot = protList.get(key2);
                quantAccessionMap.put(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim(), prot.getUniProtAccess());
                if (!diseaseGroupsComparisonsProteinsMap.containsKey(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim())) {
                    diseaseGroupsComparisonsProteinsMap.put(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim(), new DiseaseGroupsComparisonsProteinLayout[quantDiseaseGroupsComparisonArr.length]);
                }
                DiseaseGroupsComparisonsProteinLayout[] tCompArr = diseaseGroupsComparisonsProteinsMap.get(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim());
                tCompArr[compIndex] = prot;
                diseaseGroupsComparisonsProteinsMap.put(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim(), tCompArr);
            }
        }
        int index = 0;
        Set<Object> selectedProtId = new HashSet<Object>();
        for (String protAccName : diseaseGroupsComparisonsProteinsMap.keySet()) {
            int i = 0;
            String protAcc = protAccName.replace("--", "").trim().split(",")[0];
            String protName = protAccName.replace("--", "").trim().split(",")[1];
            Object[] tableRow = new Object[3 + quantDiseaseGroupsComparisonArr.length];
            CustomExternalLink acc = new CustomExternalLink(protAcc.toUpperCase(), "http://www.uniprot.org/uniprot/" + protAcc.toUpperCase());
            acc.setDescription("UniProt link for " + protAcc.toUpperCase());

            tableRow[i++] = index;
            tableRow[i++] = acc;
            tableRow[i++] = protName;
            for (QuantDiseaseGroupsComparison cg : quantDiseaseGroupsComparisonArr) {
                DiseaseGroupsComparisonsProteinLayout cp = diseaseGroupsComparisonsProteinsMap.get(protAccName)[i - 3];
                if (cp == null) {
                    tableRow[i] = null;
                } else {
                    cp.updateWidth(columnWidth);
                    cp.updateLabelLayout();
                    tableRow[i] = cp;
                }
                i++;
            }
            this.groupsComparisonProteinsTable.addItem(tableRow, index);
            if (selectedQuantAccessionsSet != null && selectedQuantAccessionsSet.contains(protAccName)) {
                selectedProtId.add(index);
            }
            index++;
        }
        if (quantDiseaseGroupsComparisonArr.length > 0) {
            sortComparisonTableColumn = ((QuantDiseaseGroupsComparison) quantDiseaseGroupsComparisonArr[quantDiseaseGroupsComparisonArr.length - 1]).getComparisonHeader();
        }
        this.groupsComparisonProteinsTable.sort(new String[]{sortComparisonTableColumn}, new boolean[]{false});
        this.groupsComparisonProteinsTable.setSortAscending(false);
        int indexing = 1;
        for (Object id : this.groupsComparisonProteinsTable.getItemIds()) {
            Item item = this.groupsComparisonProteinsTable.getItem(id);
            item.getItemProperty("Index").setValue(indexing);

            for (QuantDiseaseGroupsComparison cg : quantDiseaseGroupsComparisonArr) {

                DiseaseGroupsComparisonsProteinLayout protCompLayout = (DiseaseGroupsComparisonsProteinLayout) item.getItemProperty(cg.getComparisonHeader()).getValue();
                if (protCompLayout != null) {
                    protCompLayout.setTableItemId(id);
                    protCompLayout.addLayoutClickListener(QuantProteinsComparisonsContainer.this);
                }
            }
            indexing++;
        }
        this.resizeTable(useRatio, ratio);
        groupsComparisonProteinsTable.addHeaderClickListener(new Table.HeaderClickListener() {
            @Override
            public void headerClick(Table.HeaderClickEvent event) {
                sortComparisonTableColumn = event.getPropertyId().toString();
            }
        });
        if (!selectedProtId.isEmpty()) {
            groupsComparisonProteinsTable.setValue(selectedProtId);
        } else {
            groupsComparisonProteinsTable.setValue(null);
        }
        if (groupsComparisonProteinsTable.getItemIds().size() == 1) {
            Object itemId = groupsComparisonProteinsTable.getItemIds().toArray()[0];
            selectedProtId.add(itemId);
            groupsComparisonProteinsTable.setValue(selectedProtId);

        }
    }

    /**
     * update table size based on the available space
     *
     * @param useRatio use ratio or pixels
     * @param ratio the column ration
     */
    private void resizeTable(boolean useRatio, float ratio) {
        groupsComparisonProteinsTable.setColumnWidth("Index", 47);
        groupsComparisonProteinsTable.setColumnWidth("Accession", 87);
        groupsComparisonProteinsTable.setColumnWidth("Name", 187);
        if ((groupsComparisonProteinsTable.getSortableContainerPropertyIds().size() - 3) > 1 && useRatio) {
            float factor = (1f - ratio) / ((float) groupsComparisonProteinsTable.getSortableContainerPropertyIds().size() - 3);
            for (Object propertyId : groupsComparisonProteinsTable.getSortableContainerPropertyIds()) {
                if (propertyId.toString().equalsIgnoreCase("Index") || propertyId.toString().equalsIgnoreCase("Accession") || propertyId.toString().equalsIgnoreCase("Name")) {
                    continue;
                }
                groupsComparisonProteinsTable.setColumnExpandRatio(propertyId, factor);
            }

        } else {
            for (Object propertyId : groupsComparisonProteinsTable.getSortableContainerPropertyIds()) {
                if (propertyId.toString().equalsIgnoreCase("Index") || propertyId.toString().equalsIgnoreCase("Accession") || propertyId.toString().equalsIgnoreCase("Name")) {
                    continue;
                }
                groupsComparisonProteinsTable.setColumnWidth(propertyId, 387);
            }
        }

    }

    /**
     * initialize the comparisons barcharts
     *
     * @param comparison Quant Disease Groups Comparison
     * @param width the column width
     * @param searchingMode the searching mode important for labeling the bars
     */
    private ComparisonChartContainer generateColumnBarChart(final QuantDiseaseGroupsComparison comparison, int width, boolean searchingMode) {
        final ComparisonChartContainer chart = new ComparisonChartContainer(comparison, width, searchingMode);
        LayoutEvents.LayoutClickListener chartDataClickHandler = new LayoutEvents.LayoutClickListener() {
            private final Map<Integer, Set<String>> localCompProtMap = chart.getCompProtMap();
            private final String compName = comparison.getComparisonHeader();
            private final ComparisonChartContainer localchart = chart;

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                Component c = event.getClickedComponent();
                if (c instanceof SquaredDot) {
                    if (lastHeighlitedChart != null) {
                        lastHeighlitedChart.setHeghlighted(false);
                    }
                    int i = (Integer) ((SquaredDot) c).getParam("barIndex");
                    localchart.setHeghlighted(true);
                    lastHeighlitedChart = localchart;
                    sortComparisonTableColumn = compName;
                    filterTable(localCompProtMap.get(i), quantDiseaseGroupsComparisonArr, sortComparisonTableColumn);
                    updateProtCountLabel(localCompProtMap.get(i).size());
                    localchart.heighLightBar(i);
                    updateChartsWithSelectedChartColumn(sortComparisonTableColumn, new HashSet<String>(localCompProtMap.get(i)));
                }
            }
        };
        chart.addChartListener(chartDataClickHandler);

        LayoutEvents.LayoutClickListener closeListener = new LayoutEvents.LayoutClickListener() {

            private final QuantDiseaseGroupsComparison localComparison = comparison;

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                Set<QuantDiseaseGroupsComparison> selectedComparisonList = selectionManager.getSelectedDiseaseGroupsComparisonList();
                selectedComparisonList.remove(localComparison);
                selectionManager.setDiseaseGroupsComparisonSelection(selectedComparisonList);
            }
        };
        chart.addCloseListiner(closeListener);
        quantBarchartSet.add(chart);
        return chart;

    }

    /**
     * searching for proteins using name or accessions within the quant
     * comparisons table
     *
     * @param keyword query keyword
     * @return list of found quant proteins
     */
    private Set<String> searchProteins(String keyword) {
        Set<String> subAccessionMap = new HashSet<String>();
        for (String key : quantAccessionMap.keySet()) {
            if (key.trim().contains(keyword.toLowerCase().trim())) {
                subAccessionMap.add(quantAccessionMap.get(key));
            }
        }
        return subAccessionMap;
    }

    /**
     * filter and update the quant proteins table
     *
     * @param comparisonProteinsArray query keyword
     * @param sortCompColumnName the sortable column
     * @return list of found quant proteins
     */
    private void filterTable(Set<String> accessions, QuantDiseaseGroupsComparison[] comparisonProteinsArray, String sortCompColumnName) {
        groupsComparisonProteinsTable.removeValueChangeListener(QuantProteinsComparisonsContainer.this);
        this.groupsComparisonProteinsTable.removeAllItems();
        diseaseGroupsComparisonsProteinsMap = new HashMap<String, DiseaseGroupsComparisonsProteinLayout[]>();
        for (int compIndex = 0; compIndex < comparisonProteinsArray.length; compIndex++) {
            QuantDiseaseGroupsComparison comp = comparisonProteinsArray[compIndex];
            Map<String, DiseaseGroupsComparisonsProteinLayout> protList = comp.getComparProtsMap();
            for (String key2 : protList.keySet()) {
                DiseaseGroupsComparisonsProteinLayout prot = protList.get(key2);
                if (!accessions.contains(prot.getUniProtAccess())) {
                    continue;
                }

                if (!diseaseGroupsComparisonsProteinsMap.containsKey(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim())) {
                    diseaseGroupsComparisonsProteinsMap.put(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim(), new DiseaseGroupsComparisonsProteinLayout[comparisonProteinsArray.length]);
                }
                DiseaseGroupsComparisonsProteinLayout[] tCompArr = diseaseGroupsComparisonsProteinsMap.get(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim());
                tCompArr[compIndex] = prot;
                diseaseGroupsComparisonsProteinsMap.put(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim(), tCompArr);
            }
        }

        int index = 0;
        for (String protAccName : diseaseGroupsComparisonsProteinsMap.keySet()) {
            int i = 0;
            String protAcc = protAccName.replace("--", "").trim().split(",")[0];
            String protName = protAccName.replace("--", "").trim().split(",")[1];
            Object[] tableRow = new Object[3 + comparisonProteinsArray.length];
            CustomExternalLink acc = new CustomExternalLink(protAcc.toUpperCase(), "http://www.uniprot.org/uniprot/" + protAcc.toUpperCase());
            acc.setDescription("UniProt link for " + protAcc.toUpperCase());

            tableRow[i++] = index;
            tableRow[i++] = acc;
            tableRow[i++] = protName;
            for (QuantDiseaseGroupsComparison cg : comparisonProteinsArray) {
                DiseaseGroupsComparisonsProteinLayout cp = diseaseGroupsComparisonsProteinsMap.get(protAccName)[i - 3];
                if (cp == null) {
                    tableRow[i] = null;
                } else {

                    cp.updateLabelLayout();
                    tableRow[i] = cp;
                }
                i++;
            }
            this.groupsComparisonProteinsTable.addItem(tableRow, index);
            index++;
        }
        if (sortCompColumnName.equalsIgnoreCase("")) {
            sortCompColumnName = ((QuantDiseaseGroupsComparison) comparisonProteinsArray[0]).getComparisonHeader();
        }

        if (comparisonProteinsArray.length > 0) {
            this.groupsComparisonProteinsTable.sort(new String[]{sortCompColumnName}, new boolean[]{false});
        }
        this.groupsComparisonProteinsTable.setSortAscending(false);
        int indexing = 1;
        for (Object id : this.groupsComparisonProteinsTable.getItemIds()) {
            Item item = this.groupsComparisonProteinsTable.getItem(id);
            item.getItemProperty("Index").setValue(indexing);
            for (QuantDiseaseGroupsComparison cg : quantDiseaseGroupsComparisonArr) {
                DiseaseGroupsComparisonsProteinLayout protCompLayout = (DiseaseGroupsComparisonsProteinLayout) item.getItemProperty(cg.getComparisonHeader()).getValue();
                if (protCompLayout != null) {
                    protCompLayout.setTableItemId(id);
                    protCompLayout.addLayoutClickListener(QuantProteinsComparisonsContainer.this);
                }
            }
            indexing++;
        }
        Set<Object> selectedProtId = new HashSet<Object>();

        groupsComparisonProteinsTable.addValueChangeListener(QuantProteinsComparisonsContainer.this);
        if (groupsComparisonProteinsTable.getItemIds().size() == 1) {
            Object itemId = groupsComparisonProteinsTable.getItemIds().toArray()[0];
            selectedProtId.add(itemId);
            groupsComparisonProteinsTable.setValue(selectedProtId);

        }
    }

    /**
     * select all data in the visualized quant comparison table
     */
    private void selectAll() {
        groupsComparisonProteinsTable.setValue(groupsComparisonProteinsTable.getItemIds());

    }

    /**
     *
     * un select all data in the visualized quant comparison table
     */
    private void unSelectAll() {
        groupsComparisonProteinsTable.setValue(null);

    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        if (groupsComparisonProteinsTable.getValue() != null) {
            proteinskeys.clear();
            proteinskeys.addAll((Set) groupsComparisonProteinsTable.getValue());
        } else {
            proteinskeys.clear();
        }
        if (!lastSelectedProts.isEmpty()) {
            for (CustomExternalLink uniprot : lastSelectedProts.values()) {
                uniprot.rePaintLable("black");
            }
        }
        lastSelectedProts.clear();
        for (int proteinskey : proteinskeys) {

            final Item item = groupsComparisonProteinsTable.getItem(proteinskey);
            CustomExternalLink lastSelectedProt = (CustomExternalLink) item.getItemProperty("Accession").getValue();
            Integer index = (Integer) item.getItemProperty("Index").getValue();
            lastSelectedProt.rePaintLable("white");
            lastSelectedProts.put(index, lastSelectedProt);
        }
        List<String> accessions = new ArrayList<String>();
        for (int index : lastSelectedProts.keySet()) {
            CustomExternalLink str = lastSelectedProts.get(index);
            accessions.add(str.toString());
        }
        this.updateChartsWithTableSelectedProteins(new HashSet<String>(accessions));
        LinkedHashMap<String, DiseaseGroupsComparisonsProteinLayout[]> protSelectionMap = new LinkedHashMap<String, DiseaseGroupsComparisonsProteinLayout[]>();
        for (String accession : accessions) {
            for (int compIndex = 0; compIndex < quantDiseaseGroupsComparisonArr.length; compIndex++) {
                QuantDiseaseGroupsComparison comp = quantDiseaseGroupsComparisonArr[compIndex];
                Map<String, DiseaseGroupsComparisonsProteinLayout> protList = comp.getComparProtsMap();
                for (DiseaseGroupsComparisonsProteinLayout prot : protList.values()) {
                    if (prot.getUniProtAccess().equalsIgnoreCase(accession)) {
                        if (!protSelectionMap.containsKey(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim())) {
                            protSelectionMap.put(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim(), new DiseaseGroupsComparisonsProteinLayout[quantDiseaseGroupsComparisonArr.length]);
                        }
                        DiseaseGroupsComparisonsProteinLayout[] tCompArr = protSelectionMap.get(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim());
                        tCompArr[compIndex] = prot;
                        protSelectionMap.put(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim(), tCompArr);

                    }
                }

            }
        }

        if (externalSelection) {
            externalSelection = false;
        } else {
            selfselection = true;
            selectionManager.setQuantProteinsSelectionLayout(protSelectionMap);
        }

    }

    /**
     * update the quant bar charts labels
     *
     * @param accessions selected accessions
     *
     */
    private void updateChartsWithTableSelectedProteins(Set<String> accessions) {

        for (ComparisonChartContainer chart : quantBarchartSet) {
            chart.updateExternalSelection(accessions);
        }

    }

    /**
     * update the quant bar charts labels with selected barchart
     *
     * @param selectedComparisonHeader selected comparison header
     * @param accessions selected accessions
     *
     */
    private void updateChartsWithSelectedChartColumn(String selectedComparisonHeader, Set<String> accessions) {

        for (ComparisonChartContainer chart : quantBarchartSet) {
            if (chart.getComparisonHeader().equalsIgnoreCase(selectedComparisonHeader)) {
                chart.updateChartsWithSelectedChartColumn(accessions, false);
            } else {
                chart.updateChartsWithSelectedChartColumn(accessions, true);
            }
        }

    }

    /**
     * reset all barcharts to initial state
     *
     */
    private void resetAllCharts() {
        for (ComparisonChartContainer chart : quantBarchartSet) {
            chart.reset();
        }

    }

    /**
     * update the barcharts width based on the table columns width
     *
     * @param width the update layout width
     */
    public void resizeCharts(int width) {
        for (ComparisonChartContainer chart : quantBarchartSet) {
            chart.resizeChart(width);
        }

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        DiseaseGroupsComparisonsProteinLayout protCompLayout = (DiseaseGroupsComparisonsProteinLayout) event.getClickedComponent().getParent();
        if (protCompLayout == null) {
            return;
        }
        Set<Object> itemsIds = new HashSet<Object>();
        if (event.isCtrlKey() || event.isShiftKey()) {
            itemsIds.addAll((Set) groupsComparisonProteinsTable.getValue());

        }
        itemsIds.add(protCompLayout.getTableItemId());
        groupsComparisonProteinsTable.setValue(itemsIds);
    }

}
