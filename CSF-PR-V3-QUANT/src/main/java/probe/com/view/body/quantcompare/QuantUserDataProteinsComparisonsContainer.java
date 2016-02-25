package probe.com.view.body.quantcompare;

import com.ejt.vaadin.sizereporter.ComponentResizeEvent;
import com.ejt.vaadin.sizereporter.ComponentResizeListener;
import com.ejt.vaadin.sizereporter.SizeReporter;
import com.vaadin.addon.tableexport.CsvExport;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.LayoutEvents;
import com.vaadin.event.ShortcutListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.MultiSelectMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
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
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
import probe.com.model.beans.quant.QuantProtein;
import probe.com.selectionmanager.CSFFilter;
import probe.com.selectionmanager.QuantCentralManager;
import probe.com.view.body.quantdatasetsoverview.QuantProteinsComparisonsContainer;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.DiseaseGroupsComparisonsProteinLayout;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.TrendLegend;
import probe.com.view.core.CustomExternalLink;

/**
 * @author Yehia Farag
 *
 * This class represents the top right side from the quant data tab the
 * container contains the quantification proteins table, searching field, table
 * buttons and the overview bar charts.
 *
 */
public class QuantUserDataProteinsComparisonsContainer extends VerticalLayout implements LayoutEvents.LayoutClickListener, CSFFilter, Property.ValueChangeListener {

    private final QuantCentralManager Quant_Central_Manager;
    private Table groupsComparisonProteinsTable;
    private final HorizontalLayout topLayout, bottomLayout, columnLabelContainer;
    private final GridLayout searchingFieldLayout;
    private final TextField searchField;
    private final VerticalLayout searchingBtn;
    private final Button resetTableBtn;
    private final Label protCounterLabel;
    private int width = 0;
    private final OptionGroup hideUniqueProteinsOption;
    private final VerticalLayout tableLayout;
    private String sortComparisonTableColumn;
    private final Set<QuantDiseaseGroupsComparison> diseaseGroupsComparisonsMap = new HashSet<QuantDiseaseGroupsComparison>();
    private final Map<String, String> quantAccessionMap = new HashMap<String, String>();
    private QuantDiseaseGroupsComparison[] quantDiseaseGroupsComparisonArr = new QuantDiseaseGroupsComparison[]{};
    private boolean selfselection = false;
    private Map<String, DiseaseGroupsComparisonsProteinLayout[]> diseaseGroupsComparisonsProteinsMap;
    private Map<String, Boolean> uniprotProteinsMap;
    private final Set<Object> proteinskeys = new HashSet<Object>();
    private final TreeMap<Integer, CustomExternalLink> lastSelectedProts = new TreeMap<Integer, CustomExternalLink>();
    private final Label noProtLabel = new Label("<h4 style='font-family:verdana;color:#8A0808;font-weight:bold;'>\t \t Select comparisons  first!</h4>");
    private final Map<String, Set<Integer>> isoProtMap = new LinkedHashMap<String, Set<Integer>>();
    private final Map<String, Object> accssionToItemIdMap = new HashMap<String, Object>();
    private boolean selectedProteinsSubSet = false;
    private boolean headerFilterSubSet = false;
    private final QuantDiseaseGroupsComparison userCustomizedComparison;
    private final LinkedHashMap<String, DiseaseGroupsComparisonsProteinLayout[]> lastSelectedProteinsMap = new LinkedHashMap<String, DiseaseGroupsComparisonsProteinLayout[]>();
    private final Set<Object> selectedProtId;

    private final Map<String, Boolean> activeHeaderFiltersMap = new LinkedHashMap<String, Boolean>();
    private final Map<String, Set<Object>> appliedHeaderFiltersMap = new LinkedHashMap<String, Set<Object>>();
    private final Map<String, Item> fullAccessionTableItemMap = new HashMap<String, Item>();
    private final Map<String, Set<String>> comparisonToAccessionMap;
    private Set<String> selectedQuantAccessionsSubSet;

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
            if (((quantDiseaseGroupsComparisonArr.length + 1) * 400) > (width - 360)) {
                useRatio = true;
                int persWidth = (int) (100.0 - (16.0 * 100.0 / (double) width));
                columnLabelContainer.setWidth(persWidth + "%");
                int contWid = (persWidth * (width - 380) / 100);
                columnWidth = contWid / quantDiseaseGroupsComparisonArr.length;

            } else {
                useRatio = false;
                columnLabelContainer.setWidth(((quantDiseaseGroupsComparisonArr.length + 1) * 400) + "px");
            }
        } else {//ok
            columnLabelContainer.setWidth(((quantDiseaseGroupsComparisonArr.length + 1) * 400) + "px");
        }

        topLayout.setExpandRatio(searchingFieldLayout, ratio);
        topLayout.setExpandRatio(columnLabelContainer, (1f - ratio));
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
        if (userCustomizedComparison != null) {

            for (DiseaseGroupsComparisonsProteinLayout cp : userCustomizedComparison.getComparProtsMap().values()) {
                if (cp != null) {
                    cp.updateWidth(columnWidth);
                }

            }

        }

    }
    private int resizedCounter = 0;

    /**
     *
     * @param Quant_Central_Manager
     * @param csfprHandler
     * @param searchQuantificationProtList
     * @param userCustomizedComparison
     */
    public QuantUserDataProteinsComparisonsContainer(QuantCentralManager Quant_Central_Manager, final CSFPRHandler csfprHandler, List<QuantProtein> searchQuantificationProtList, QuantDiseaseGroupsComparison userCustomizedComparison) {
        this.selectedProtId = new HashSet<Object>();
        this.Quant_Central_Manager = Quant_Central_Manager;
        this.Quant_Central_Manager.registerStudySelectionListener(QuantUserDataProteinsComparisonsContainer.this);
        this.userCustomizedComparison = userCustomizedComparison;
        this.setWidth("100%");
        this.setHeightUndefined();
        this.setStyleName(Reindeer.LAYOUT_WHITE);
        this.setMargin(false);
        this.setSpacing(true);
        final SizeReporter sizeReporter = new SizeReporter(this);
        sizeReporter.addResizeListener(new ComponentResizeListener() {
            @Override
            public void sizeChanged(ComponentResizeEvent event) {
                if (resizedCounter == 1) {
                    UI.getCurrent().scrollIntoView(QuantUserDataProteinsComparisonsContainer.this);
                    sizeReporter.removeResizeListener(this);
                }

                resizedCounter++;
            }
        });
        this.comparisonToAccessionMap = new HashMap<String, Set<String>>();
        this.noProtLabel.setContentMode(ContentMode.HTML);
        noProtLabel.setWidth("400px");
        noProtLabel.setHeight("40px");
        this.addComponent(noProtLabel);
        this.setComponentAlignment(noProtLabel, Alignment.TOP_LEFT);
        this.setVisible(true);

        topLayout = new HorizontalLayout();
        topLayout.setVisible(false);
        topLayout.setWidth("100%");
        topLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        topLayout.setSpacing(false);
        searchingFieldLayout = new GridLayout();
        searchingFieldLayout.setSpacing(true);
        searchingFieldLayout.setHeightUndefined();
        searchingFieldLayout.setColumns(2);
        searchingFieldLayout.setRows(2);
        searchingFieldLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        searchingFieldLayout.setColumnExpandRatio(0, 0.4f);
        searchingFieldLayout.setColumnExpandRatio(1, 0.6f);

        columnLabelContainer = new HorizontalLayout();
        columnLabelContainer.setHeight("30px");

        int persWidth = (int) (100.0 - (16.0 * 100.0 / (double) width));
        columnLabelContainer.setWidth(persWidth + "%");
        columnLabelContainer.setStyleName(Reindeer.LAYOUT_WHITE);
        topLayout.addComponent(searchingFieldLayout);
        topLayout.setComponentAlignment(searchingFieldLayout, Alignment.BOTTOM_LEFT);

        topLayout.addComponent(columnLabelContainer);
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
        searchField.setTextChangeTimeout(1500);
        final Button b = new Button();
        searchField.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.LAZY);
        searchField.addTextChangeListener(new FieldEvents.TextChangeListener() {

            @Override
            public void textChange(FieldEvents.TextChangeEvent event) {
                String text = event.getText();
                if (text == null || text.trim().equalsIgnoreCase("")) {
                    resetTableBtn.click();
                } else {
                    searchForProtiens(text);
                }
            }
        });

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

        resetTableBtn = new Button("Reset Table");
        resetTableBtn.setStyleName(Reindeer.BUTTON_LINK);
        resetTableBtn.setWidth("76px");
        resetTableBtn.setHeight("24px");

        this.addComponent(topLayout);

        tableLayout = new VerticalLayout();
        updateComparisonsTable();
        groupsComparisonProteinsTable.setVisible(false);
        this.addComponent(tableLayout);
        this.setComponentAlignment(tableLayout, Alignment.MIDDLE_CENTER);
        tableLayout.addComponent(groupsComparisonProteinsTable);
        tableLayout.setComponentAlignment(groupsComparisonProteinsTable, Alignment.MIDDLE_CENTER);

        //add searching listeners 
        searchingBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {

                if (searchField.getValue().trim().equalsIgnoreCase("")) {
                    resetTableBtn.click();
                } else {
                    searchForProtiens(searchField.getValue());
                }

            }
        });

        b.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (searchField.getValue().trim().equalsIgnoreCase("")) {
                    resetTableBtn.click();
                } else {
                    searchForProtiens(searchField.getValue());
                }
            }
        });
        searchField.addShortcutListener(new Button.ClickShortcut(b, ShortcutListener.KeyCode.ENTER));
        resetTableBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                searchField.clear();
                if (selectedProteinsSubSet) {
                    filterTable(selectedQuantAccessionsSubSet, quantDiseaseGroupsComparisonArr, sortComparisonTableColumn, false);
                } else {
                    updateTableData(quantDiseaseGroupsComparisonArr);
                }
                localSelectAccessions(QuantUserDataProteinsComparisonsContainer.this.Quant_Central_Manager.getQuantProteinsLayoutSelectionMap().keySet());
                updateProtCountLabel(groupsComparisonProteinsTable.getItemIds().size());

            }
        });
        bottomLayout = new HorizontalLayout();
        bottomLayout.setHeight("100%");
        bottomLayout.setStyleName(Reindeer.LAYOUT_WHITE);

        hideUniqueProteinsOption = new OptionGroup();
        bottomLayout.addComponent(hideUniqueProteinsOption);
        bottomLayout.setComponentAlignment(hideUniqueProteinsOption, Alignment.TOP_LEFT);
        hideUniqueProteinsOption.setWidth("150px");
        hideUniqueProteinsOption.setNullSelectionAllowed(true); // user can not 'unselect'
        hideUniqueProteinsOption.setMultiSelect(true);

        hideUniqueProteinsOption.addItem("Available in all comparisons only");
        hideUniqueProteinsOption.addStyleName("horizontal");
        hideUniqueProteinsOption.addValueChangeListener(new Property.ValueChangeListener() {
            private final Set<String> protAccssions = new HashSet<String>();
            private final HashMap<Object, Item> tableItems = new HashMap<Object, Item>();

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (hideUniqueProteinsOption.getValue().toString().equalsIgnoreCase("[Available in all comparisons only]")) {
                    protAccssions.clear();
                    tableItems.clear();
                    HashSet<Object> itemIds = new HashSet<Object>(groupsComparisonProteinsTable.getItemIds());
                    for (Object id : itemIds) {
                        Item item = groupsComparisonProteinsTable.getItem(id);
                        tableItems.put(id, item);
                        protAccssions.add(item.getItemProperty("Accession").toString());

                    }
                    filterTable(protAccssions, quantDiseaseGroupsComparisonArr, sortComparisonTableColumn, true);

                } else {

                    filterTable(protAccssions, quantDiseaseGroupsComparisonArr, sortComparisonTableColumn, false);
                }
                updateProtCountLabel(groupsComparisonProteinsTable.getItemIds().size());
            }
        });

        HorizontalLayout rightBottomLayout = new HorizontalLayout();
        rightBottomLayout.setWidthUndefined();
        rightBottomLayout.setSpacing(true);
        bottomLayout.addComponent(rightBottomLayout);
        bottomLayout.setComponentAlignment(rightBottomLayout, Alignment.TOP_RIGHT);

        TrendLegend tableLegendLayout = new TrendLegend("table");
        rightBottomLayout.addComponent(tableLegendLayout);
        rightBottomLayout.setComponentAlignment(tableLegendLayout, Alignment.MIDDLE_CENTER);

        
          
        VerticalLayout removeAllFiltersBtn = new VerticalLayout();
        removeAllFiltersBtn.setStyleName("clearfiltersbtn");
        rightBottomLayout.addComponent(removeAllFiltersBtn);
        rightBottomLayout.setComponentAlignment(removeAllFiltersBtn, Alignment.TOP_LEFT);
        removeAllFiltersBtn.setDescription("Select all data");
        removeAllFiltersBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                clearAllFilters();
            }
        });
          Button exportTableBtn = new Button("");
        exportTableBtn.setHeight("24px");
        exportTableBtn.setWidth("24px");
        exportTableBtn.setPrimaryStyleName("exportxslbtn");
        rightBottomLayout.addComponent(exportTableBtn);
        rightBottomLayout.setComponentAlignment(exportTableBtn, Alignment.TOP_RIGHT);
        exportTableBtn.setDescription("Export table data");
        rightBottomLayout.setHeight("100%");
        rightBottomLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        exportTableBtn.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {

                Map<String, String> idMap = new HashMap<String, String>();
                for (QuantDiseaseGroupsComparison comp : quantDiseaseGroupsComparisonArr) {
                    String preHeader = groupsComparisonProteinsTable.getColumnHeader(comp.getComparisonHeader());
                    groupsComparisonProteinsTable.setColumnHeader(comp.getComparisonHeader(), comp.getComparisonHeader() + " " + preHeader);
                    idMap.put(comp.getComparisonHeader(), preHeader);
                }

                ExcelExport csvExport = new ExcelExport(groupsComparisonProteinsTable, "CSF-PR  Quant Comparisons Proteins");
                csvExport.setReportTitle("CSF-PR / Quant Comparisons / Proteins ");
                csvExport.setExportFileName("CSF-PR - Quant Comparisons - Proteins" + ".xls");
                csvExport.setMimeType(CsvExport.EXCEL_MIME_TYPE);
                csvExport.setDisplayTotals(false);
                csvExport.setExcelFormatOfProperty("Index", "#0;[Red] #0");
                csvExport.export();

                for (QuantDiseaseGroupsComparison comp : quantDiseaseGroupsComparisonArr) {
                    String preHeader = idMap.get(comp.getComparisonHeader());
                    groupsComparisonProteinsTable.setColumnHeader(comp.getComparisonHeader(), preHeader);
                    idMap.put(comp.getComparisonHeader(), preHeader);
                }

            }
        });
      

        VerticalLayout selectAllBtn = new VerticalLayout();
        selectAllBtn.setStyleName("selectallbtn");
        rightBottomLayout.addComponent(selectAllBtn);
        rightBottomLayout.setComponentAlignment(selectAllBtn, Alignment.TOP_LEFT);
        selectAllBtn.setDescription("Select all data");
        selectAllBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                selectAll();
            }
        });

        VerticalLayout unselectAllBtn = new VerticalLayout();
        unselectAllBtn.setStyleName("unselectallbtn");
        rightBottomLayout.addComponent(unselectAllBtn);
        rightBottomLayout.setComponentAlignment(unselectAllBtn, Alignment.TOP_LEFT);
        unselectAllBtn.setDescription("Unselect all data");
        unselectAllBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                unSelectAll();
            }
        });

      
        bottomLayout.setVisible(false);
        this.addComponent(bottomLayout);

    }

    private void searchForProtiens(String keyword) {

        hideUniqueProteinsOption.unselect("Available in all comparisons only");
        Set<String> subAccList = getSearchingProteinsList(keyword);

        if (subAccList.isEmpty()) {
            Notification.show("Not available");
            return;
        } else {
            filterTable(subAccList, quantDiseaseGroupsComparisonArr, sortComparisonTableColumn, false);
        }
        localSelectAccessions(QuantUserDataProteinsComparisonsContainer.this.Quant_Central_Manager.getQuantProteinsLayoutSelectionMap().keySet());
        updateProtCountLabel(subAccList.size());
    }

    /**
     * event on the central selection manager
     *
     * @param type
     */
    @Override
    public void selectionChanged(String type) {

        if (type.equalsIgnoreCase("Comparison_Selection")) {

            hideUniqueProteinsOption.unselect("Available in all comparisons only");
            Set<String> selectedQuantAccessionsSet;
            Set<QuantDiseaseGroupsComparison> selectedComparisonList = Quant_Central_Manager.getSelectedDiseaseGroupsComparisonList();
            QuantDiseaseGroupsComparison[] tempDiseaseGroupsComparisonsArray = new QuantDiseaseGroupsComparison[selectedComparisonList.size()];
            int u = 0;
            diseaseGroupsComparisonsMap.clear();

            for (QuantDiseaseGroupsComparison diseaseGroupsComparison : selectedComparisonList) {
                tempDiseaseGroupsComparisonsArray[u] = diseaseGroupsComparison;
                diseaseGroupsComparisonsMap.add(diseaseGroupsComparison);
                u++;
            }
            quantDiseaseGroupsComparisonArr = tempDiseaseGroupsComparisonsArray;
            diseaseGroupsComparisonsMap.clear();
            diseaseGroupsComparisonsMap.addAll(Arrays.asList(quantDiseaseGroupsComparisonArr));

            if (diseaseGroupsComparisonsMap.isEmpty()) {
                topLayout.setVisible(false);
                groupsComparisonProteinsTable.setVisible(false);
                bottomLayout.setVisible(false);
                noProtLabel.setVisible(true);
                searchField.clear();
                groupsComparisonProteinsTable.setValue(null);
                return;
            } else {
                if (Quant_Central_Manager.getQuantProteinsLayoutSelectionMap() != null) {
                    selectedQuantAccessionsSet = Quant_Central_Manager.getQuantProteinsLayoutSelectionMap().keySet();
                } else {
                    selectedQuantAccessionsSet = new HashSet<String>();
                }
                topLayout.setVisible(true);
                groupsComparisonProteinsTable.setVisible(true);
                bottomLayout.setVisible(true);
                noProtLabel.setVisible(false);
                this.updateTableData(quantDiseaseGroupsComparisonArr);
                this.localSelectAccessions(selectedQuantAccessionsSet);
                this.updateSelectionManager();

            }
            filterTableSelection(sortComparisonTableColumn, true);
        } //        
        else if (type.equalsIgnoreCase("Quant_Proten_Selection") && !selfselection) {

            selfselection = false;
            Set<String> selectedQuantAccessionsSet = Quant_Central_Manager.getQuantProteinsLayoutSelectionMap().keySet();
            localSelectAccessions(selectedQuantAccessionsSet);

        } else if (type.equalsIgnoreCase("Protens_Selection") && !selfselection) {

            selfselection = false;
            if (Quant_Central_Manager.getSelectedComparisonHeader() != null && !Quant_Central_Manager.getSelectedComparisonHeader().equalsIgnoreCase("")) {
                sortComparisonTableColumn = Quant_Central_Manager.getSelectedComparisonHeader();
            }
            selectedQuantAccessionsSubSet = Quant_Central_Manager.getProtSelectionSet();
            if (selectedQuantAccessionsSubSet.isEmpty()) {
                selectedProteinsSubSet = false;
                resetTableBtn.click();
            } else {
                selectedProteinsSubSet = true;
                filterTable(selectedQuantAccessionsSubSet, quantDiseaseGroupsComparisonArr, sortComparisonTableColumn, false);

            }
            filterTableSelection(sortComparisonTableColumn, selectedProteinsSubSet);
            updateProtCountLabel(groupsComparisonProteinsTable.getItemIds().size());
        } else if (selfselection) {
            selfselection = false;
        }
        updateProtCountLabel(groupsComparisonProteinsTable.getItemIds().size());
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
     * Remove (un select) filter from central selection manager (not apply to
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
    private void updateTableData(QuantDiseaseGroupsComparison[] quantDiseaseGroupsComparisonArr) {
        isoProtMap.clear();
        accssionToItemIdMap.clear();
        quantAccessionMap.clear();
        fullAccessionTableItemMap.clear();
        comparisonToAccessionMap.clear();
        appliedHeaderFiltersMap.clear();

        this.groupsComparisonProteinsTable.removeValueChangeListener(this);
        this.columnLabelContainer.removeAllComponents();
        updateComparisonsTable();

        boolean useRatio = false;
        int columnWidth = 400;
        if (quantDiseaseGroupsComparisonArr.length + 1 > 1) {
            if (((quantDiseaseGroupsComparisonArr.length + 1) * 400) > (width - 360)) {
                useRatio = true;
                int persWidth = (int) (100.0 - (16.0 * 100.0 / (double) width));
                columnLabelContainer.setWidth(persWidth + "%");
                int contWid = (persWidth * (width - 360) / 100);
                columnWidth = contWid / (quantDiseaseGroupsComparisonArr.length + 1);

            } else {
                columnLabelContainer.setWidth(((quantDiseaseGroupsComparisonArr.length + 1) * 400) + "px");
                useRatio = false;
            }
        } else {
            columnLabelContainer.setWidth(((quantDiseaseGroupsComparisonArr.length + 1) * 400) + "px");
        }
        float ratio = 360f / (float) width;
        topLayout.setExpandRatio(searchingFieldLayout, ratio);
        topLayout.setExpandRatio(columnLabelContainer, (1f - ratio));

        this.groupsComparisonProteinsTable.addContainerProperty("Index", Integer.class, null, "", null, Table.Align.RIGHT);
        this.groupsComparisonProteinsTable.addContainerProperty("Accession", CustomExternalLink.class, null, "Accession", null, Table.Align.LEFT);
        this.groupsComparisonProteinsTable.addContainerProperty("Name", String.class, null, "Name", null, Table.Align.LEFT);

        HorizontalLayout userCustColumnTitleLayout = generateColumnHeaderLayout(userCustomizedComparison, columnWidth);
        this.columnLabelContainer.addComponent(userCustColumnTitleLayout);
        this.columnLabelContainer.setComponentAlignment(userCustColumnTitleLayout, Alignment.BOTTOM_LEFT);

        this.groupsComparisonProteinsTable.addContainerProperty(userCustomizedComparison.getComparisonHeader(), DiseaseGroupsComparisonsProteinLayout.class, null, userCustomizedComparison.getComparisonHeader() + " (#Proteins: " + userCustomizedComparison.getComparProtsMap().size() + "/" + userCustomizedComparison.getDatasetIndexes().length + ")", null, Table.Align.CENTER);
        this.groupsComparisonProteinsTable.setColumnWidth(userCustomizedComparison.getComparisonHeader(), 100);

        diseaseGroupsComparisonsProteinsMap = new HashMap<String, DiseaseGroupsComparisonsProteinLayout[]>();
        uniprotProteinsMap = new HashMap<String, Boolean>();

        Map<String, DiseaseGroupsComparisonsProteinLayout> userProteinsList = userCustomizedComparison.getComparProtsMap();

        Set<String> usercomparisonProtAccessionsSet = new HashSet<String>();

        for (String key2 : userProteinsList.keySet()) {
            DiseaseGroupsComparisonsProteinLayout prot = userProteinsList.get(key2);
            usercomparisonProtAccessionsSet.add(prot.getProteinAccssionNumber());

        }
        comparisonToAccessionMap.put(userCustomizedComparison.getComparisonHeader(), usercomparisonProtAccessionsSet);

        for (int compIndex = 0; compIndex < quantDiseaseGroupsComparisonArr.length; compIndex++) {
            QuantDiseaseGroupsComparison comparison = quantDiseaseGroupsComparisonArr[compIndex];
            HorizontalLayout columnTitleLayout = generateColumnHeaderLayout(comparison, columnWidth);
            this.columnLabelContainer.addComponent(columnTitleLayout);
            this.columnLabelContainer.setComponentAlignment(columnTitleLayout, Alignment.BOTTOM_LEFT);

            this.groupsComparisonProteinsTable.addContainerProperty(comparison.getComparisonHeader(), DiseaseGroupsComparisonsProteinLayout.class, null, comparison.getComparisonHeader() + " (#Proteins: " + comparison.getComparProtsMap().size() + "/" + comparison.getDatasetIndexes().length + ")", null, Table.Align.CENTER);
            this.groupsComparisonProteinsTable.setColumnWidth(comparison.getComparisonHeader(), 100);

            Map<String, DiseaseGroupsComparisonsProteinLayout> protList = comparison.getComparProtsMap();
            int protCounter = 0;
            Set<String> comparisonProtAccessionsSet = new HashSet<String>();

            for (String key2 : protList.keySet()) {
                DiseaseGroupsComparisonsProteinLayout prot = protList.get(key2);
                comparisonProtAccessionsSet.add(prot.getProteinAccssionNumber());
                boolean uniprotAvailable = true;
                if (Quant_Central_Manager.isSignificantOnly() && (prot.getSignificantTrindCategory() == 2 || prot.getSignificantTrindCategory() == 5)) {
                    continue;
                }
                String protAcc = prot.getProteinAccssionNumber().toLowerCase().trim();

                String key = ("--" + protAcc.toLowerCase().trim() + "," + prot.getProtName().trim()).trim();
                if (prot.getUrl() == null) {
                    uniprotAvailable = false;
                }
                uniprotProteinsMap.put(key, uniprotAvailable);
                quantAccessionMap.put(key, prot.getProteinAccssionNumber());
                if (!diseaseGroupsComparisonsProteinsMap.containsKey(key)) {
                    diseaseGroupsComparisonsProteinsMap.put(key, new DiseaseGroupsComparisonsProteinLayout[quantDiseaseGroupsComparisonArr.length]);
                }
                DiseaseGroupsComparisonsProteinLayout[] tCompArr = diseaseGroupsComparisonsProteinsMap.get(key);
                tCompArr[compIndex] = prot;
                diseaseGroupsComparisonsProteinsMap.put(key, tCompArr);
                protCounter++;
            }

            comparisonToAccessionMap.put(comparison.getComparisonHeader(), comparisonProtAccessionsSet);
            this.groupsComparisonProteinsTable.setColumnHeader(comparison.getComparisonHeader(), " (" + protCounter + ((protCounter == 1) ? " Protein / " : " Proteins / ") + comparison.getDatasetIndexes().length + ((comparison.getDatasetIndexes().length == 1) ? " Study)" : " Studies)"));
            String header = comparison.getComparisonHeader();
            String updatedHeader = header.split(" / ")[0].split("\n")[0] + " / " + header.split(" / ")[1].split("\n")[0] + " ( " + header.split(" / ")[1].split("\n")[1] + " )";

            columnTitleLayout.setDescription("<h3>" + updatedHeader + "</h3><center><h4>(" + protCounter + ((protCounter == 1) ? " Protein / " : " Proteins / ") + comparison.getDatasetIndexes().length + ((comparison.getDatasetIndexes().length == 1) ? " Study)" : " Studies)</h4></center>"));
        }

        int index = 0;
        Set<Object> localSelectedProtId = new HashSet<Object>();
        for (String key : diseaseGroupsComparisonsProteinsMap.keySet()) {
            if (key.replace("--", "").trim().split(",").length == 1) {
            }
            int i = 0;
            String protAcc = key.replace("--", "").trim().split(",")[0];
            String protURL = "http://www.uniprot.org/uniprot/" + protAcc.toUpperCase();
            String tooltip = "UniProt link for " + protAcc.toUpperCase();
            if (!uniprotProteinsMap.get(key)) {
                tooltip = "UniProt Not Available ";
                protURL = null;

            }

            String protName = key.replace("--", "").trim().split(",")[1];
            CustomExternalLink acc = new CustomExternalLink(protAcc.toUpperCase(), protURL);
            acc.setDescription(tooltip);
            Object[] tableRow = new Object[4 + quantDiseaseGroupsComparisonArr.length];

            tableRow[i++] = index;
            tableRow[i++] = acc;
            tableRow[i++] = protName;
            userCustomizedComparison.getComparProtsMap().get(acc.toString()).updateWidth(columnWidth);
            userCustomizedComparison.getComparProtsMap().get(acc.toString()).setCustomizedUserData(true);
            tableRow[i++] = userCustomizedComparison.getComparProtsMap().get(acc.toString());
            for (QuantDiseaseGroupsComparison cg : quantDiseaseGroupsComparisonArr) {
                DiseaseGroupsComparisonsProteinLayout cp = diseaseGroupsComparisonsProteinsMap.get(key)[i - 4];
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
            this.accssionToItemIdMap.put(tableRow[1].toString(), index);
            this.fullAccessionTableItemMap.put(tableRow[1].toString(), groupsComparisonProteinsTable.getItem(index));
            index++;
        }
        String header = userCustomizedComparison.getComparisonHeader();
        String updatedHeader = header.split(" / ")[0].split("\n")[0] + " / " + header.split(" / ")[1].split("\n")[0];// + " ( " + header.split(" / ")[1].split("\n")[1] + " )";

        userCustColumnTitleLayout.setDescription("<h3>" + updatedHeader + "</h3><center><h4>(" + groupsComparisonProteinsTable.size() + ((groupsComparisonProteinsTable.size() == 1) ? " Protein / " : " Proteins / ") + userCustomizedComparison.getDatasetIndexes().length + ((userCustomizedComparison.getDatasetIndexes().length == 1) ? " Study)" : " Studies)</h4></center>"));
        sortComparisonTableColumn = userCustomizedComparison.getComparisonHeader();
        this.groupsComparisonProteinsTable.sort(new String[]{sortComparisonTableColumn}, new boolean[]{false});
        this.groupsComparisonProteinsTable.setSortAscending(false);
        int indexing = 1;

        for (Object id : this.groupsComparisonProteinsTable.getItemIds()) {
            Item item = this.groupsComparisonProteinsTable.getItem(id);
            item.getItemProperty("Index").setValue(indexing);
            DiseaseGroupsComparisonsProteinLayout protCompLayout = (DiseaseGroupsComparisonsProteinLayout) item.getItemProperty(userCustomizedComparison.getComparisonHeader()).getValue();
            if (protCompLayout != null) {
                protCompLayout.setTableItemId(id);
                protCompLayout.addLayoutClickListener(QuantUserDataProteinsComparisonsContainer.this);
            }

            for (QuantDiseaseGroupsComparison cg : quantDiseaseGroupsComparisonArr) {

                protCompLayout = (DiseaseGroupsComparisonsProteinLayout) item.getItemProperty(cg.getComparisonHeader()).getValue();
                if (protCompLayout != null) {
                    protCompLayout.setTableItemId(id);
                    protCompLayout.addLayoutClickListener(QuantUserDataProteinsComparisonsContainer.this);
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

        this.groupsComparisonProteinsTable.addValueChangeListener(this);

//        if (!localSelectedProtId.isEmpty()) {
//            groupsComparisonProteinsTable.setValue(localSelectedProtId);
//        } else {
//            groupsComparisonProteinsTable.setValue(null);
//        }
        this.updateProtCountLabel(diseaseGroupsComparisonsProteinsMap.size());

        //add to iso map
        for (Object itemId : groupsComparisonProteinsTable.getItemIds()) {

            final Item item = groupsComparisonProteinsTable.getItem(itemId);
            String accession = item.getItemProperty("Accession").getValue().toString();
            String isoProtKey;
            if (accession.contains("-")) {
                isoProtKey = (accession.split("-")[0]);
            } else {
                isoProtKey = accession;
            }
            if (!isoProtMap.containsKey(isoProtKey)) {

                isoProtMap.put(isoProtKey, new LinkedHashSet<Integer>());
            }
            Set<Integer> set = isoProtMap.get(isoProtKey);
            set.add((Integer) itemId);
            isoProtMap.put(isoProtKey, set);

        }
//        if (groupsComparisonProteinsTable.getItemIds().size() == 1) {
//            Object itemId = groupsComparisonProteinsTable.getItemIds().toArray()[0];
//            localSelectedProtId.add(itemId);
//            groupsComparisonProteinsTable.setValue(localSelectedProtId);
//
//        }

    }

    /**
     * filter and update the quant proteins table
     *
     * @param comparisonProteinsArray query keyword
     * @param sortCompColumnName the sortable column
     * @return list of found quant proteins
     */
    private void filterTable(Set<String> accessions, QuantDiseaseGroupsComparison[] comparisonProteinsArray, String sortCompColumnName, boolean hideUnique) {
        isoProtMap.clear();
        accssionToItemIdMap.clear();
        groupsComparisonProteinsTable.removeValueChangeListener(QuantUserDataProteinsComparisonsContainer.this);
        this.groupsComparisonProteinsTable.removeAllItems();

        Map<String, DiseaseGroupsComparisonsProteinLayout[]> filteredDiseaseGroupsComparisonsProteinsMap = new HashMap<String, DiseaseGroupsComparisonsProteinLayout[]>();
        for (int compIndex = 0; compIndex < comparisonProteinsArray.length; compIndex++) {
            QuantDiseaseGroupsComparison comp = comparisonProteinsArray[compIndex];
            Map<String, DiseaseGroupsComparisonsProteinLayout> protList = comp.getComparProtsMap();
            for (String key2 : protList.keySet()) {
                DiseaseGroupsComparisonsProteinLayout prot = protList.get(key2);
                if (!accessions.contains(prot.getProteinAccssionNumber())) {
                    continue;
                }
                if (Quant_Central_Manager.isSignificantOnly() && (prot.getSignificantTrindCategory() == 2 || prot.getSignificantTrindCategory() == 5)) {
                    continue;
                }
                String key = ("--" + prot.getProteinAccssionNumber().toLowerCase().trim() + "," + prot.getProtName().trim()).trim();

                if (!filteredDiseaseGroupsComparisonsProteinsMap.containsKey(key)) {
                    filteredDiseaseGroupsComparisonsProteinsMap.put(key, new DiseaseGroupsComparisonsProteinLayout[comparisonProteinsArray.length]);
                }
                DiseaseGroupsComparisonsProteinLayout[] tCompArr = filteredDiseaseGroupsComparisonsProteinsMap.get(key);
                tCompArr[compIndex] = prot;
                filteredDiseaseGroupsComparisonsProteinsMap.put(key, tCompArr);
            }
        }

        int index = 0;
        for (String key : filteredDiseaseGroupsComparisonsProteinsMap.keySet()) {
            int i = 0;
            String protAcc = key.replace("--", "").trim().split(",")[0];
            String protURL = "http://www.uniprot.org/uniprot/" + protAcc.toUpperCase();
            String tooltip = "UniProt link for " + protAcc.toUpperCase();

            if (!uniprotProteinsMap.get(key)) {
                tooltip = "UniProt Not Available ";
                protURL = null;
            }

            String protName = key.replace("--", "").trim().split(",")[1];
            CustomExternalLink acc = new CustomExternalLink(protAcc.toUpperCase(), protURL);
            acc.setDescription(tooltip);
            Object[] tableRow = new Object[4 + comparisonProteinsArray.length];
            tableRow[i++] = index;
            tableRow[i++] = acc;
            tableRow[i++] = protName;
            tableRow[i++] = userCustomizedComparison.getComparProtsMap().get(acc.toString());

            boolean checkUnique = false;
            for (QuantDiseaseGroupsComparison cg : comparisonProteinsArray) {
                DiseaseGroupsComparisonsProteinLayout cp = filteredDiseaseGroupsComparisonsProteinsMap.get(key)[i - 4];
                if (cp == null) {
                    if (hideUnique) {
                        checkUnique = true;
                        break;
                    }
                    tableRow[i] = null;
                } else {

                    cp.updateLabelLayout();
                    tableRow[i] = cp;
                }
                i++;
            }
            if (checkUnique) {
                continue;
            }
            this.groupsComparisonProteinsTable.addItem(tableRow, index);
            this.accssionToItemIdMap.put(tableRow[1].toString(), index);
            index++;
        }

        initianSort(sortCompColumnName);
//        
        groupsComparisonProteinsTable.addValueChangeListener(QuantUserDataProteinsComparisonsContainer.this);

        for (Object itemId : groupsComparisonProteinsTable.getItemIds()) {

            final Item item = groupsComparisonProteinsTable.getItem(itemId);
            String accession = item.getItemProperty("Accession").getValue().toString();
            String isoProtKey;
            if (accession.contains("-")) {
                isoProtKey = (accession.split("-")[0]);
            } else {
                isoProtKey = accession;

            }
            if (!isoProtMap.containsKey(isoProtKey)) {

                isoProtMap.put(isoProtKey, new LinkedHashSet<Integer>());
            }
            Set<Integer> set = isoProtMap.get(isoProtKey);
            set.add((Integer) itemId);
            isoProtMap.put(isoProtKey, set);
        }
//        Set<Object> localSelectedProtId = new HashSet<Object>();
//
//        if (!groupsComparisonProteinsTable.getItemIds().isEmpty()) {
//            Object itemId = groupsComparisonProteinsTable.getItemIds().toArray()[0];
//            localSelectedProtId.add(itemId);
//            localSelectItemID(localSelectedProtId);
//            updateSelectionManager();
//            groupsComparisonProteinsTable.setValue(localSelectedProtId);
//
//        }
    }

    private void initianSort(String sortCompColumnName) {
        if (sortCompColumnName.equalsIgnoreCase("")) {
            sortCompColumnName = ((QuantDiseaseGroupsComparison) quantDiseaseGroupsComparisonArr[0]).getComparisonHeader();
        }

        if (quantDiseaseGroupsComparisonArr.length > 0) {
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
                    protCompLayout.addLayoutClickListener(QuantUserDataProteinsComparisonsContainer.this);
                }
            }
            indexing++;
        }

    }

    /**
     * searching for proteins using name or accessions within the quant
     * comparisons table
     *
     * @param keyword query keyword
     * @return list of found quant proteins
     */
    private Set<String> getSearchingProteinsList(String keyword) {
        Set<String> subAccessionMap = new HashSet<String>();
        for (String key : quantAccessionMap.keySet()) {
            if (key.trim().toLowerCase().contains(keyword.toLowerCase().trim())) {
                if (selectedProteinsSubSet && selectedQuantAccessionsSubSet.contains(quantAccessionMap.get(key))) {
                    subAccessionMap.add(quantAccessionMap.get(key));
                } else if (!selectedProteinsSubSet) {
                    subAccessionMap.add(quantAccessionMap.get(key));
                }
            }
        }
        return subAccessionMap;
    }

    /**
     * Initialize the comparisons barcharts
     *
     * @param comparison Quant Disease Groups Comparison
     * @param width the column width
     * @param searchingMode the searching mode important for labeling the bars
     */
    private HorizontalLayout generateColumnHeaderLayout(final QuantDiseaseGroupsComparison comparison, int width) {

        HorizontalLayout titleLayout = new HorizontalLayout();
        titleLayout.setWidth(width + "px");
        titleLayout.setHeight("20px");
        String header = comparison.getComparisonHeader();
        String updatedHeader = header.split(" / ")[0].split("\n")[0] + " / " + header.split(" / ")[1].split("\n")[0];// " ( " + header.split(" / ")[1].split("\n")[1] + " )";

        Label label = new Label();
        label.setStyleName("comparisonHeaders");
        label.setContentMode(ContentMode.HTML);
        label.setWidth("90%");

        HorizontalLayout btnsLayout = new HorizontalLayout();
        btnsLayout.setSpacing(true);
        btnsLayout.setMargin(new MarginInfo(false, true, false, false));

        VerticalLayout filterIcon = generateDropDownFilterLayout(comparison.getComparisonHeader());
        btnsLayout.addComponent(filterIcon);

        VerticalLayout closeCompariosonBtn = new VerticalLayout();
        closeCompariosonBtn.setWidth("10px");
        closeCompariosonBtn.setHeight("10px");
        closeCompariosonBtn.setStyleName("closebtn");
        if (comparison.getDatasetIndexes()[0] == -1) {
            closeCompariosonBtn.setEnabled(false);
            titleLayout.setStyleName("usercustdatatitle");
            label.setValue(updatedHeader);
        } else {
            String diseaseColor = this.Quant_Central_Manager.getDiseaseHashedColor(header.split(" / ")[1].split("\n")[1]);

            label.setValue("<font color='" + diseaseColor + "' style='font-weight: bold;'>" + updatedHeader + "</font>");
        }

        closeCompariosonBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            private final QuantDiseaseGroupsComparison localComparison = comparison;

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {

                Set<QuantDiseaseGroupsComparison> selectedComparisonList = Quant_Central_Manager.getSelectedDiseaseGroupsComparisonList();
                selectedComparisonList.remove(localComparison);
                Quant_Central_Manager.setDiseaseGroupsComparisonSelection(selectedComparisonList);
            }
        });

        btnsLayout.addComponent(closeCompariosonBtn);

        titleLayout.addComponent(label);
        titleLayout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
        titleLayout.addComponent(btnsLayout);
        titleLayout.setComponentAlignment(btnsLayout, Alignment.MIDDLE_LEFT);
        titleLayout.setExpandRatio(label, width - 42);
        titleLayout.setExpandRatio(btnsLayout, 42);

        return titleLayout;

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
        lastSelectedProteinsMap.clear();
        groupsComparisonProteinsTable.setValue(null);
    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        if (!lastSelectedProts.isEmpty()) {
            for (CustomExternalLink uniprot : lastSelectedProts.values()) {
                uniprot.rePaintLable("black");
            }
        }
        lastSelectedProts.clear();
        proteinskeys.clear();

        if (groupsComparisonProteinsTable != null && (groupsComparisonProteinsTable.getValue() != null) && !((Set) groupsComparisonProteinsTable.getValue()).isEmpty()) {

            for (Object i : ((Set) groupsComparisonProteinsTable.getValue())) {

                final Item item = groupsComparisonProteinsTable.getItem(i);
                String acc = item.getItemProperty("Accession").getValue().toString();
                if (acc.contains("-")) {
                    acc = acc.split("-")[0];
                }
                proteinskeys.addAll(isoProtMap.get(acc));
            }
            localSelectItemID(proteinskeys);
        }

        for (Object proteinskey : proteinskeys) {
            final Item item = groupsComparisonProteinsTable.getItem(proteinskey);
            CustomExternalLink lastSelectedProt = (CustomExternalLink) item.getItemProperty("Accession").getValue();
            selectedProtId.add(lastSelectedProt);
            Integer index = (Integer) item.getItemProperty("Index").getValue();
            lastSelectedProt.rePaintLable("white");
            lastSelectedProts.put(index, lastSelectedProt);
        }
        this.updateSelectionManager();
    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (event.getClickedComponent() == null || event.getClickedComponent().getParent() == null) {
            return;
        }
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

    private void updateComparisonsTable() {

        if (groupsComparisonProteinsTable != null) {
            groupsComparisonProteinsTable.removeAllItems();
            groupsComparisonProteinsTable.clear();
            tableLayout.removeComponent(groupsComparisonProteinsTable);

        }
        groupsComparisonProteinsTable = new Table();
        groupsComparisonProteinsTable.setSelectable(true);
        groupsComparisonProteinsTable.setColumnReorderingAllowed(false);
        groupsComparisonProteinsTable.setColumnCollapsingAllowed(true);
        groupsComparisonProteinsTable.setImmediate(true); // react at once when something is selected
        groupsComparisonProteinsTable.setWidth("100%");
        groupsComparisonProteinsTable.setHeight("400px");
        groupsComparisonProteinsTable.setMultiSelect(true);
        groupsComparisonProteinsTable.setMultiSelectMode(MultiSelectMode.DEFAULT);
        groupsComparisonProteinsTable.setDragMode(Table.TableDragMode.MULTIROW);
        groupsComparisonProteinsTable.addColumnResizeListener(new Table.ColumnResizeListener() {
            @Override
            public void columnResize(Table.ColumnResizeEvent event) {
                groupsComparisonProteinsTable.setColumnWidth(event.getPropertyId(), event.getPreviousWidth());
            }
        });
        tableLayout.addComponent(groupsComparisonProteinsTable);
        tableLayout.setComponentAlignment(groupsComparisonProteinsTable, Alignment.MIDDLE_CENTER);
    }

    private void localSelectAccessions(Set<String> selectedAcccessions) {
        groupsComparisonProteinsTable.removeValueChangeListener(this);

        Set<Object> itemIds = new HashSet<Object>();
        for (String str : selectedAcccessions) {
            if (accssionToItemIdMap.containsKey(str.split(",")[0].replace("--", "").toUpperCase())) {
                itemIds.add(accssionToItemIdMap.get(str.split(",")[0].replace("--", "").toUpperCase()));
            }
        }
        groupsComparisonProteinsTable.setValue(itemIds);

        if (!lastSelectedProts.isEmpty()) {
            for (CustomExternalLink uniprot : lastSelectedProts.values()) {
                uniprot.rePaintLable("black");
            }
        }
        lastSelectedProts.clear();

        for (Object itemId : (Set) groupsComparisonProteinsTable.getValue()) {
            final Item item = groupsComparisonProteinsTable.getItem(itemId);
            CustomExternalLink lastSelectedProt = (CustomExternalLink) item.getItemProperty("Accession").getValue();
            selectedProtId.add(lastSelectedProt);
            Integer index = (Integer) item.getItemProperty("Index").getValue();
            lastSelectedProt.rePaintLable("white");
            lastSelectedProts.put(index, lastSelectedProt);

        }

        if (!itemIds.isEmpty()) {
            Object itemID = ((Set) groupsComparisonProteinsTable.getValue()).toArray()[0];
            groupsComparisonProteinsTable.setCurrentPageFirstItemId(itemID);
            groupsComparisonProteinsTable.setCurrentPageFirstItemIndex(groupsComparisonProteinsTable.getCurrentPageFirstItemIndex());
        }
        groupsComparisonProteinsTable.addValueChangeListener(this);
    }

    private void localSelectItemID(Set<Object> itemIds) {
        if (itemIds == null || itemIds.isEmpty()) {
            return;
        }
        groupsComparisonProteinsTable.removeValueChangeListener(this);
        groupsComparisonProteinsTable.setValue(itemIds);
        if (!lastSelectedProts.isEmpty()) {
            for (CustomExternalLink uniprot : lastSelectedProts.values()) {
                uniprot.rePaintLable("black");
            }
        }
        lastSelectedProts.clear();
        for (Object itemId : (Set) groupsComparisonProteinsTable.getValue()) {
            final Item item = groupsComparisonProteinsTable.getItem(itemId);
            CustomExternalLink lastSelectedProt = (CustomExternalLink) item.getItemProperty("Accession").getValue();
            selectedProtId.add(lastSelectedProt);
            Integer index = (Integer) item.getItemProperty("Index").getValue();
            lastSelectedProt.rePaintLable("white");
            lastSelectedProts.put(index, lastSelectedProt);

        }
        groupsComparisonProteinsTable.addValueChangeListener(this);
    }

    private void updateSelectionManager() {
        List<String> accessions = new ArrayList<String>();
        for (int index : lastSelectedProts.keySet()) {
            CustomExternalLink str = lastSelectedProts.get(index);
            accessions.add(str.toString());
        }
        lastSelectedProteinsMap.clear();
        for (String accession : accessions) {
            for (int compIndex = 0; compIndex < quantDiseaseGroupsComparisonArr.length; compIndex++) {
                QuantDiseaseGroupsComparison comp = quantDiseaseGroupsComparisonArr[compIndex];
                Map<String, DiseaseGroupsComparisonsProteinLayout> protList = comp.getComparProtsMap();
                for (DiseaseGroupsComparisonsProteinLayout prot : protList.values()) {
                    if (prot.getProteinAccssionNumber().equalsIgnoreCase(accession)) {
                        String key = ("--" + prot.getProteinAccssionNumber().toLowerCase().trim() + "," + prot.getProtName().trim()).trim();
                        key = key + "," + uniprotProteinsMap.get(key);
                        if (!lastSelectedProteinsMap.containsKey(key)) {
                            lastSelectedProteinsMap.put(key, new DiseaseGroupsComparisonsProteinLayout[quantDiseaseGroupsComparisonArr.length]);
                        }
                        DiseaseGroupsComparisonsProteinLayout[] tCompArr = lastSelectedProteinsMap.get(key);
                        tCompArr[compIndex] = prot;
                        lastSelectedProteinsMap.put(key, tCompArr);
                    }
                }
            }
        }
        selfselection = true;
        Quant_Central_Manager.setQuantProteinsSelectionLayout(lastSelectedProteinsMap);
        Quant_Central_Manager.selectionQuantProteinsSelectionLayoutChanged();
    }

    String[] trendCatArray = new String[]{"Low   100%", "Low < 100%", "Stable", "High < 100%", "High   100%", "No Quant Information"};

    private VerticalLayout generateDropDownFilterLayout(final String header) {

        VerticalLayout popupbodyLayout = new VerticalLayout();
        popupbodyLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        popupbodyLayout.setSpacing(true);
        popupbodyLayout.setWidth("200px");
        popupbodyLayout.setMargin(true);

        HorizontalLayout popupTopLayout = new HorizontalLayout();
        popupbodyLayout.addComponent(popupTopLayout);
        popupTopLayout.setWidth("100%");

        Label headerLabel = new Label("<b>Select Filter</b>");
        headerLabel.setContentMode(ContentMode.HTML);
        popupTopLayout.addComponent(headerLabel);

        VerticalLayout closePopupBtn = new VerticalLayout();
        closePopupBtn.setWidth("19px");
        closePopupBtn.setHeight("15px");
        closePopupBtn.setStyleName("closewindowbtn");
        popupTopLayout.addComponent(closePopupBtn);
        popupTopLayout.setComponentAlignment(closePopupBtn, Alignment.TOP_RIGHT);

        final OptionGroup tableHeaderFilterOptions = new OptionGroup();
        popupbodyLayout.addComponent(tableHeaderFilterOptions);
        tableHeaderFilterOptions.setWidth("100%");
        tableHeaderFilterOptions.setDescription("Select to filter the table");
        tableHeaderFilterOptions.addItem(trendCatArray[4]);
        tableHeaderFilterOptions.addItem(trendCatArray[3]);
        tableHeaderFilterOptions.addItem(trendCatArray[2]);
        tableHeaderFilterOptions.addItem(trendCatArray[1]);
        tableHeaderFilterOptions.addItem(trendCatArray[0]);
        tableHeaderFilterOptions.addItem(trendCatArray[5]);

        tableHeaderFilterOptions.setStyleName("diseaseselectionlist");
        tableHeaderFilterOptions.setNullSelectionAllowed(true);
        tableHeaderFilterOptions.setImmediate(true);
        tableHeaderFilterOptions.setNewItemsAllowed(false);
        tableHeaderFilterOptions.setMultiSelect(true);
        activeHeaderFiltersMap.put(header, Boolean.FALSE);
        appliedHeaderFiltersMap.put(header, new HashSet<Object>());

        final VerticalLayout headerFilterIconWrapper = new VerticalLayout();
        headerFilterIconWrapper.setHeight("15px");
        headerFilterIconWrapper.setWidth("15px");
        headerFilterIconWrapper.setSpacing(true);
        headerFilterIconWrapper.setStyleName("unselectedfilter");
//
//        final VerticalLayout selectedIcon = new VerticalLayout();
//        selectedIcon.setStyleName(Reindeer.LAYOUT_WHITE);
//        selectedIcon.setWidth("15px");
//        selectedIcon.setHeight("15px");
//        selectedIcon.setEnabled(true);
//        headerFilterWrapper.addComponent(selectedIcon);
//        headerFilterWrapper.setComponentAlignment(selectedIcon, Alignment.MIDDLE_LEFT);

        final String noFilterMessage = "No Filters Applied";

        final PopupView popupBtn = new PopupView(null, popupbodyLayout);
        popupBtn.setVisible(true);
        headerFilterIconWrapper.addComponent(popupBtn);
        popupBtn.setDescription(noFilterMessage);
        popupBtn.setCaptionAsHtml(true);
        popupBtn.setHideOnMouseOut(false);

        closePopupBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                popupBtn.setPopupVisible(false);
            }
        });

        Property.ValueChangeListener listener = new Property.ValueChangeListener() {

            private final String comparisonHeader = header;
            private final VerticalLayout localFilterIcon = headerFilterIconWrapper;

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Set<Object> headersSet = new HashSet<Object>((Set<Object>) event.getProperty().getValue());

                if (!headersSet.isEmpty()) {
                    activeHeaderFiltersMap.put(comparisonHeader, Boolean.TRUE);
                    localFilterIcon.setStyleName("selectedfilter");
                    appliedHeaderFiltersMap.put(comparisonHeader, (Set<Object>) event.getProperty().getValue());
                } else {
                    activeHeaderFiltersMap.put(comparisonHeader, Boolean.FALSE);
                    localFilterIcon.setStyleName("unselectedfilter");
                    appliedHeaderFiltersMap.put(comparisonHeader, new HashSet<Object>());
                }

                filterTableSelection(comparisonHeader, false);
                headerFilterIconWrapper.setDescription("" + event.getProperty().getValue());
                sortComparisonTableColumn = comparisonHeader;

            }
        };
        tableHeaderFilterOptions.addValueChangeListener(listener);
        headerFilterIconWrapper.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                popupBtn.setPopupVisible(true);
            }
        });

        return headerFilterIconWrapper;
    }

    private void filterTableSelection(String filterComparisonHeader, boolean notifi) {

        Set<String> subAccessionMap = new HashSet<String>();
        //check if any header Filtered applied
        for (boolean comparisonHeader : activeHeaderFiltersMap.values()) {
            if (comparisonHeader) {
                headerFilterSubSet = true;

                break;
            }
            headerFilterSubSet = false;

        }

        //reset if no filters selected
        if (!headerFilterSubSet) {
            resetTableBtn.click();
            return;
        }
        if (notifi) {
            Notification.show("Remember", "filters are applied", Notification.Type.TRAY_NOTIFICATION);
        }
        //check if there is an exteranl accession filtering applied
        if (selectedProteinsSubSet) {
            subAccessionMap.addAll(selectedQuantAccessionsSubSet);
        } else {
            subAccessionMap.addAll(fullAccessionTableItemMap.keySet());
        }
        //start filtering the list 
        int compIndex = 0;
        for (String comparisonHeader : appliedHeaderFiltersMap.keySet()) {
            if (appliedHeaderFiltersMap.get(comparisonHeader).isEmpty()) {
                compIndex++;
                continue;

            }

            //remove non exist prot in comparison
            Set<String> subAccessionFilterMap = new HashSet<String>(subAccessionMap);
            for (String protAcc : subAccessionFilterMap) {
                if (!comparisonToAccessionMap.get(comparisonHeader).contains(protAcc)) {
                    subAccessionMap.remove(protAcc);
                }
            }

            //user data case
            QuantDiseaseGroupsComparison comparison;
            if (compIndex == 0) {
                comparison = userCustomizedComparison;
                compIndex++;
            } else {
                comparison = quantDiseaseGroupsComparisonArr[compIndex++];
            }

            for (String protKeys : comparison.getComparProtsMap().keySet()) {
                DiseaseGroupsComparisonsProteinLayout prot = comparison.getComparProtsMap().get(protKeys);
                int trend = prot.getSignificantTrindCategory();

                if (!appliedHeaderFiltersMap.get(comparisonHeader).contains(trendCatArray[trend])) {
                    subAccessionMap.remove(prot.getProteinAccssionNumber());

                }

            }

        }
        filterTable(subAccessionMap, quantDiseaseGroupsComparisonArr, filterComparisonHeader, false);
        updateProtCountLabel(groupsComparisonProteinsTable.getItemIds().size());
    }

    private void clearAllFilters() {

        for (String comparisonHeader : activeHeaderFiltersMap.keySet()) {
            activeHeaderFiltersMap.put(comparisonHeader, false);
            appliedHeaderFiltersMap.put(comparisonHeader, new HashSet<Object>());

        }
        filterTableSelection(sortComparisonTableColumn, false);
    }

}
