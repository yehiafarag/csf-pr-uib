package no.uib.probe.csf.pr.touch.view.components.linechartproteintablecomponents;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.DataBoundTransferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.And;
import com.vaadin.event.dd.acceptcriteria.SourceIs;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbstractSelect.AbstractSelectTargetDetails;
import com.vaadin.ui.AbstractSelect.AcceptItem;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.JPanel;
import no.uib.probe.csf.pr.touch.logic.beans.QuantComparisonProtein;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.view.core.ColumnHeaderLayout;
import no.uib.probe.csf.pr.touch.view.core.ExternalLink;
import no.uib.probe.csf.pr.touch.view.core.ProteinTrendLayout;
import no.uib.probe.csf.pr.touch.view.core.RadioButton;
import no.uib.probe.csf.pr.touch.view.core.TrendSymbol;
import org.apache.commons.codec.binary.Base64;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.encoders.ImageEncoder;
import org.jfree.chart.encoders.ImageEncoderFactory;
import org.jfree.chart.encoders.ImageFormat;

/**
 *
 * @author Yehia Farag
 *
 * this class represents quant protein table container
 */
public abstract class ProteinTable extends VerticalLayout implements Property.ValueChangeListener {

    private boolean selectedOnly = false;

    private final Map<Object, Object[]> tableItemsMap;
    private final Map<Object, Object[]> activeTableItemsMap;
    private final Map<String, Integer> tableProteinsToIDMap;
    private final Map<Object, CheckBox> tableItemscheckboxMap;
    private final Set<ColumnHeaderLayout> columnHeaderSet;

    private int availableProteinLayoutWidth;
    /* This set contains the ids of the "selected" items */
    private final Set<Object> selectedItemIds = new HashSet<>();
    private Table mainProteinTable;
    private final HorizontalLayout topComparisonsContainer;
    private final Map<QuantDiseaseGroupsComparison, Set<Object>> filtersMap;
//
//    private ThemeResource showAllRes = new ThemeResource("img/show_selected.png");
//
//    private ThemeResource showSelectedeRes = new ThemeResource("img/show_all.png");
    private boolean hideCheckedColumn = false;

    public final void hideCheckedColumn(boolean hide) {

//        this.hideCheckedColumn = hide;
//        mainProteinTable.setColumnCollapsed("emptyselection", !hide);
//        mainProteinTable.setColumnCollapsed("selected", hide);
//        mainProteinTable.markAsDirty();
//        mainProteinTable.setWidth(100, Unit.PERCENTAGE);
    }
    private ColumnHeaderLayout custUserComparisonSortingLayout;
    private final VerticalLayout userSortingHeaderWrapper;

    public void setUserCustomizedComparison(QuantDiseaseGroupsComparison userCustomizedComparison) {
        if (columnHeaderSet.contains(this.custUserComparisonSortingLayout)) {
            columnHeaderSet.remove(this.custUserComparisonSortingLayout);
        }
        this.userCustomizedComparison = userCustomizedComparison;
        userSortingHeaderWrapper.removeAllComponents();

        if (userCustomizedComparison != null) {

            mainProteinTable = initMainTable(height, width, true);

            userCustomizedComparison.getQuantComparisonProteinMap().keySet().stream().forEach((accession) -> {
                Object itemId = tableProteinsToIDMap.get(accession);
                if (tableItemsMap.containsKey(itemId)) {
                    Object[] items = tableItemsMap.get(itemId);
                    ProteinTrendLayout protTrendLayout = (ProteinTrendLayout) items[5];
                    if (protTrendLayout != null) {
                        protTrendLayout.updateCustTrend(userCustomizedComparison.getQuantComparisonProteinMap().get(accession).getSignificantTrindCategory());
                    }
                }
            });
            activeTableItemsMap.clear();
            activeTableItemsMap.putAll(tableItemsMap);

            custUserComparisonSortingLayout = new ColumnHeaderLayout(userCustomizedComparison, -1) {

                @Override
                public void sort(boolean up, int index) {
                    sortOnComparison(up, index);
                }

                @Override
                public void dropComparison(QuantDiseaseGroupsComparison comparison) {

                    ProteinTable.this.dropComparison(comparison);
                }

                @Override
                public void filterTable(QuantDiseaseGroupsComparison comparison, int comparisonIndex, Set<Object> filterSet) {
                    filterTableSelection(comparison, filterSet);
                }

            };
            this.userSortingHeaderWrapper.addComponent(custUserComparisonSortingLayout);
        } else {
            mainProteinTable = initMainTable(height, width, true);
        }
    }

    private QuantDiseaseGroupsComparison userCustomizedComparison;

    /**
     * filter table items to view only user protein selection
     *
     * @param selectedProteinsList
     */
    public void filterTableItem(Set<QuantComparisonProtein> selectedProteinsList) {
        Set<Object> filteredItemIds = new HashSet<>();
        selectedItemIds.clear();
        activeTableItemsMap.clear();

        selectedProteinsList.stream().forEach((protein) -> {
            if (tableProteinsToIDMap.containsKey(protein.getProteinAccession())) {
                filteredItemIds.add(tableProteinsToIDMap.get(protein.getProteinAccession()));
            } else {
                System.out.println("at not exist acc " + protein.getProteinAccession());
            }

        });
        mainProteinTable.removeAllItems();
//        mainProteinTable.setColumnHeader("selected", "Click to show checked rows only");
        for (Object itemId : filteredItemIds) {
            mainProteinTable.addItem(tableItemsMap.get(itemId), itemId);
            activeTableItemsMap.put(itemId, tableItemsMap.get(itemId));
        }

        int indexing = 1;
        for (Object id : mainProteinTable.getItemIds()) {
            Item item = mainProteinTable.getItem(id);
            item.getItemProperty("Index").setValue(indexing);
            indexing++;
        }
        updateRowNumber(mainProteinTable.getItemIds().size(), generateThumbImg());

    }

    private String generateThumbImg() {
//        JFreeChart lineChart = null;
//        
//        int dsIndex = 1;
//        for (Object[] objArr : tableItemsMap.values()) {
//            ProteinTrendLayout trend = (ProteinTrendLayout) objArr[3];
//            if (trend.getSparkLine() != null) {
//                
//                if (lineChart == null) {
//                    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
//                    renderer.setSeriesPaint(0, new Color(169, 208, 245));
//                    lineChart = new JFreeChart(new XYPlot(trend.getSparkLine().getLineChart().getXYPlot().getDataset(), new SymbolAxis(null, ((SymbolAxis) trend.getSparkLine().getLineChart().getXYPlot().getDomainAxis()).getSymbols()), trend.getSparkLine().getLineChart().getXYPlot().getRangeAxis(), renderer));
//                    lineChart.setBackgroundPaint(Color.WHITE);
//                    final XYPlot plot = lineChart.getXYPlot();
//                    plot.setDomainGridlinesVisible(false);
//                    plot.setRangeGridlinesVisible(false);
//                    plot.setOutlineVisible(false);
//                    lineChart.setBorderVisible(false);
//                    renderer.setSeriesShapesVisible(0, false);
//                    lineChart.setPadding(new RectangleInsets(0, 0, 0, 0));
//                    LegendTitle legend = lineChart.getLegend();
//                    legend.setVisible(false);
//                    lineChart.getXYPlot().getDomainAxis().setTickLabelsVisible(false);
//                    lineChart.getXYPlot().getDomainAxis().setAxisLineVisible(true);
//                    lineChart.getXYPlot().getDomainAxis().setTickMarksVisible(false);
//                    lineChart.getXYPlot().getDomainAxis().setVisible(true);
//
////                      lineChart.getXYPlot().setRangeTickBandPaint(Color.WHITE);
//                    lineChart.getXYPlot().getRangeAxis().setTickLabelsVisible(false);
//                    lineChart.getXYPlot().getRangeAxis().setAxisLineVisible(true);
//                    lineChart.getXYPlot().getRangeAxis().setVisible(true);
//                    lineChart.getXYPlot().setOutlineVisible(false);
//                    
//                    lineChart.getXYPlot().setDomainGridlinesVisible(false);
//                    
//                }
//                lineChart.getXYPlot().setDataset(dsIndex++, trend.getSparkLine().getDataset());
//                if (dsIndex > 10) {
//                    break;
//                }
//                
//            }
//            
//        }
//        if (lineChart != null) {
//            
//            return this.getChartImage(lineChart, 100, 100);
//            
//        }
//        final Border fullBorder = new LineBorder(Color.GRAY);
        JPanel proteinSequencePanel = new JPanel();
        proteinSequencePanel.setLayout(null);
        proteinSequencePanel.setSize(100, 100);
        proteinSequencePanel.setBackground(Color.WHITE);
        int dsIndex = 1;
        int y = 0;
        for (Object[] objArr : tableItemsMap.values()) {
            ProteinTrendLayout trend = (ProteinTrendLayout) objArr[5];
            if (trend.getSparkLine() != null) {

                ChartPanel lineChartPanel = new ChartPanel(trend.getSparkLine().generateThumbChart());
                lineChartPanel.setSize(100, 25);
                lineChartPanel.setLocation(0, y);
                lineChartPanel.setOpaque(true);
                proteinSequencePanel.add(lineChartPanel);
                y += 25;
//                if (lineChart == null) {
//                    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
//                    renderer.setSeriesPaint(0, new Color(169, 208, 245));
//                    lineChart = new JFreeChart(new XYPlot(trend.getSparkLine().getLineChart().getXYPlot().getDataset(), new SymbolAxis(null, ((SymbolAxis) trend.getSparkLine().getLineChart().getXYPlot().getDomainAxis()).getSymbols()), trend.getSparkLine().getLineChart().getXYPlot().getRangeAxis(), renderer));
//                    lineChart.setBackgroundPaint(Color.WHITE);
//                    final XYPlot plot = lineChart.getXYPlot();
//                    plot.setDomainGridlinesVisible(false);
//                    plot.setRangeGridlinesVisible(false);
//                    plot.setOutlineVisible(false);
//                    lineChart.setBorderVisible(false);
//                    renderer.setSeriesShapesVisible(0, false);
//                    lineChart.setPadding(new RectangleInsets(0, 0, 0, 0));
//                    LegendTitle legend = lineChart.getLegend();
//                    legend.setVisible(false);
//                    lineChart.getXYPlot().getDomainAxis().setTickLabelsVisible(false);
//                    lineChart.getXYPlot().getDomainAxis().setAxisLineVisible(true);
//                    lineChart.getXYPlot().getDomainAxis().setTickMarksVisible(false);
//                    lineChart.getXYPlot().getDomainAxis().setVisible(true);
//
////                      lineChart.getXYPlot().setRangeTickBandPaint(Color.WHITE);
//                    lineChart.getXYPlot().getRangeAxis().setTickLabelsVisible(false);
//                    lineChart.getXYPlot().getRangeAxis().setAxisLineVisible(true);
//                    lineChart.getXYPlot().getRangeAxis().setVisible(true);
//                    lineChart.getXYPlot().setOutlineVisible(false);
//                    
//                    lineChart.getXYPlot().setDomainGridlinesVisible(false);
//                    
//                }
//                lineChart.getXYPlot().setDataset(dsIndex++, trend.getSparkLine().getDataset());
                if (dsIndex > 4) {
                    break;
                }

            }
        }

        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        graphics.setPaint(Color.WHITE);
        graphics.setBackground(Color.WHITE);

        proteinSequencePanel.paint(graphics);
        byte[] imageData = null;

        try {

            ImageEncoder in = ImageEncoderFactory.newInstance(ImageFormat.PNG, 1);
            imageData = in.encode(image);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        String base64 = "data:image/png;base64," + com.itextpdf.text.pdf.codec.Base64.encodeBytes(imageData);
        return base64;

//        return "";
    }

    private String getChartImage(JFreeChart chart, int width, int height) {
        if (chart == null) {
            return null;
        }

        String base64 = "";
        try {

            base64 = "data:image/png;base64," + Base64.encodeBase64String(ChartUtilities.encodeAsPNG(chart.createBufferedImage((int) width, (int) height)));

        } catch (IOException ex) {
            System.err.println("at error " + this.getClass() + " line 536 " + ex.getLocalizedMessage());
        }
        return base64;

    }

    public int getRowsNumber() {
        return this.mainProteinTable.getItemIds().size();
    }

    public void filterViewItemTable(Set<QuantComparisonProtein> selectedProteinsList) {
        selectedItemIds.clear();
        this.inUseSselectedProteinsList = selectedProteinsList;
        selectedProteinsList.stream().forEach((protein) -> {
            selectedItemIds.add(tableProteinsToIDMap.get(protein.getProteinAccession()));
        });
        selectedOnly = true;
        showSelectedOnly();
        if (selectedProteinsList.isEmpty()) {
            Notification.show("No results found");
        }

    }

    /**
     * Remove all applied columns filters
     */
    public void clearColumnFilters() {
        filterTableSelection(null, null);
        columnHeaderSet.stream().forEach((comparisonLayout) -> {
            comparisonLayout.noFilter();
        });
        if (userCustomizedComparison != null) {
            custUserComparisonSortingLayout.setAsDefault();
            custUserComparisonSortingLayout.sort(false, -1);
        }
    }

    /**
     * update sorting buttons to filter buttons
     */
    public void switchHeaderBtns() {

        columnHeaderSet.stream().forEach((comparisonLayout) -> {
            comparisonLayout.swichBtns();
        });

    }

    private String sortingHeader;

    public String getSortingHeader() {
        return sortingHeader;
    }

    public boolean isUpSort() {
        return upSort;
    }
    private boolean upSort;

    /**
     * Sort table based on specific comparison
     *
     * @param upSort
     * @param comparisonIndex
     */
    public void sortOnComparison(boolean upSort, int comparisonIndex) {
        mainProteinTable.addStyleName("hidesortingicon");
        this.upSort = upSort;
        if (comparisonIndex == -1) {
            mainProteinTable.sort(new String[]{"userdata"}, new boolean[]{upSort});
            this.sortingHeader = "userdata";
            int indexing = 1;
            for (Object id : mainProteinTable.getItemIds()) {
                Item item = mainProteinTable.getItem(id);
                item.getItemProperty("Index").setValue(indexing);
                indexing++;
            }
            return;

        }

        int index = 0;
        for (ColumnHeaderLayout comparisonLayout : columnHeaderSet) {
            if (index == comparisonIndex) {
                index++;
                continue;
            }
            comparisonLayout.noSort();
            index++;
        }
        tableItemsMap.values().stream().map((arr) -> (ProteinTrendLayout) arr[5]).forEach((protTrendLayout) -> {
            protTrendLayout.setSortableColumnIndex(comparisonIndex);

        });
        this.mainProteinTable.removeValueChangeListener(ProteinTable.this);
        mainProteinTable.removeAllItems();
        for (Object object : tableItemsMap.keySet()) {
            mainProteinTable.addItem(tableItemsMap.get(object), object);
        }
        this.sortingHeader = ((QuantDiseaseGroupsComparison) this.selectedComparisonsList.toArray()[comparisonIndex]).getComparisonHeader();

        mainProteinTable.sort(new String[]{"Comparisons Overview"}, new boolean[]{upSort});
        int indexing = 1;
        for (Object id : mainProteinTable.getItemIds()) {
            Item item = mainProteinTable.getItem(id);
            item.getItemProperty("Index").setValue(indexing);
            indexing++;
        }

        hideCheckedColumn(hideCheckedColumn);
        this.mainProteinTable.addValueChangeListener(ProteinTable.this);

    }
    private final boolean smallScreen;
    private final VerticalLayout spacer;
    private final VerticalLayout tableWarpper;
    private int height, width;

    /**
     *
     * @param width
     * @param height
     */
    public ProteinTable(int width, int height, boolean smallScreen) {

        this.setWidth(100, Unit.PERCENTAGE);
        this.setHeightUndefined();
        this.width = width;
        this.height = height;
        this.smallScreen = smallScreen;

        this.columnHeaderSet = new LinkedHashSet<>();
        this.filtersMap = new LinkedHashMap<>();

        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidthUndefined();
        topLayout.setHeight(20, Unit.PIXELS);
        spacer = new VerticalLayout();
        spacer.setHeight(100, Unit.PERCENTAGE);

        topLayout.addComponent(spacer);
        this.userSortingHeaderWrapper = new VerticalLayout();
        userSortingHeaderWrapper.setHeight(20, Unit.PIXELS);
        userSortingHeaderWrapper.addStyleName("bluelayout");
//        userSortingHeaderWrapper.setHeight(100,Unit.PERCENTAGE);
        topLayout.addComponent(userSortingHeaderWrapper);
        topLayout.setComponentAlignment(userSortingHeaderWrapper, Alignment.TOP_RIGHT);
        topComparisonsContainer = new HorizontalLayout();
        topComparisonsContainer.setHeight(100, Unit.PERCENTAGE);
        topComparisonsContainer.setStyleName("spacing");
        topComparisonsContainer.addStyleName("toptablelayout");
        topLayout.addComponent(topComparisonsContainer);

        this.addComponent(topLayout);
        tableWarpper = new VerticalLayout();
        tableWarpper.setWidth(100, Unit.PERCENTAGE);
        this.addComponent(tableWarpper);

        this.tableItemsMap = new LinkedHashMap<>();
        this.activeTableItemsMap = new LinkedHashMap<>();
        this.tableProteinsToIDMap = new HashMap<>();
        tableItemscheckboxMap = new HashMap<>();

        this.mainProteinTable = initMainTable(height, width, false);
//        new Table() ;
//        mainProteinTable.setCacheRate(1);   
//        this.mainProteinTable.addValueChangeListener(ProteinTable.this);
//        this.mainProteinTable.addStyleName(ValoTheme.TABLE_SMALL);
//        this.mainProteinTable.setHeight(height - 22, Unit.PIXELS);
//        this.addComponent(mainProteinTable);
//        this.tableItemsMap = new LinkedHashMap<>();
//        this.activeTableItemsMap = new LinkedHashMap<>();
//        this.tableProteinsToIDMap = new HashMap<>();
//        tableItemscheckboxMap = new HashMap<>();
//        mainProteinTable.setSelectable(true);
//        mainProteinTable.setSortEnabled(false);
//        mainProteinTable.setColumnReorderingAllowed(false);
//
//        mainProteinTable.setColumnCollapsingAllowed(true);
//        mainProteinTable.setImmediate(true); // react at once when something is selected
//        mainProteinTable.setMultiSelect(false);
//
//        mainProteinTable.addContainerProperty("Index", Integer.class, null, "", null, Table.Align.RIGHT);
//        mainProteinTable.addContainerProperty("selectedRow", RadioButton.class, null, "", null, Table.Align.CENTER);
//        mainProteinTable.addContainerProperty("Accession", ExternalLink.class, null, "Accession", null, Table.Align.LEFT);
//        mainProteinTable.addContainerProperty("Name", String.class, null, "Name", null, Table.Align.LEFT);
//        mainProteinTable.addContainerProperty("userdata", Double.class, null, "User Data", null, Table.Align.CENTER);
//
//        mainProteinTable.addContainerProperty("Comparisons Overview", ProteinTrendLayout.class, null, "", null, Table.Align.LEFT);
//        mainProteinTable.setColumnCollapsed("userdata", true);
//
//        mainProteinTable.setColumnWidth("selectedRow", 30);
//        mainProteinTable.setColumnWidth("Index", 47);
//        mainProteinTable.setColumnWidth("Accession", 87);
//        mainProteinTable.setColumnWidth("Name", 187);
//
//        mainProteinTable.setColumnWidth("userdata", 30);
//
//        availableProteinLayoutWidth = width - 48 - 87 - 187 - 40;//- 47
//        topComparisonsContainer.setWidth(availableProteinLayoutWidth, Unit.PIXELS);
//        mainProteinTable.setColumnWidth("Comparisons Overview", availableProteinLayoutWidth);
//
//        mainProteinTable.addHeaderClickListener((Table.HeaderClickEvent event) -> {
//            if (event.getPropertyId() == null) {
//                return;
//
//            }
//            if (event.getPropertyId().toString().equalsIgnoreCase("selected")) {
//                if (selectedItemIds.isEmpty()) {
//                    return;
//                }
//                selectedOnly = !selectedOnly;
//                showSelectedOnly();
//
//            } else if (!event.getPropertyId().toString().equalsIgnoreCase("Comparisons Overview") && !event.getPropertyId().toString().equalsIgnoreCase("Index")) {//
//                mainProteinTable.removeStyleName("hidesortingicon");
//                mainProteinTable.setSortEnabled(true);
//
//                if (mainProteinTable.getSortContainerPropertyId() == null || !mainProteinTable.getSortContainerPropertyId().toString().equalsIgnoreCase(event.getPropertyId().toString())) {
//                    mainProteinTable.sort(new String[]{event.getPropertyId().toString()}, new boolean[]{false});
//                } else {
//                    mainProteinTable.sort(new String[]{event.getPropertyId().toString()}, new boolean[]{!mainProteinTable.isSortAscending()});
//
//                }
//                mainProteinTable.setSortEnabled(false);
////
//                int indexing = 1;
//                for (Object id : mainProteinTable.getItemIds()) {
//                    Item item = mainProteinTable.getItem(id);
//                    item.getItemProperty("Index").setValue(indexing);
//                    indexing++;
//                }
//                hideCheckedColumn(hideCheckedColumn);
//                for (ColumnHeaderLayout columnHeader : columnHeaderSet) {
//                    columnHeader.reset();
//                    columnHeader.noSort();
//                }
////
//            }
//
//        });
//        mainProteinTable.addColumnResizeListener((Table.ColumnResizeEvent event) -> {
//            mainProteinTable.setColumnWidth(event.getPropertyId(), event.getPreviousWidth());
//        });

    }

    private Table initMainTable(int height, int width, boolean showUserData) {
        tableWarpper.removeAllComponents();
        this.mainProteinTable = new Table();

        int userDataColumnWidth;
        if (showUserData) {
            userDataColumnWidth = 47;
        } else {
            userDataColumnWidth = 0;
        }

        mainProteinTable.setCacheRate(1);
        this.mainProteinTable.addValueChangeListener(ProteinTable.this);
        this.mainProteinTable.addStyleName(ValoTheme.TABLE_SMALL);
        this.mainProteinTable.setHeight(height, Unit.PIXELS);
        mainProteinTable.setStyleName(ValoTheme.TABLE_BORDERLESS);
        this.mainProteinTable.addStyleName("proteintablestyle");
        tableWarpper.addComponent(mainProteinTable);

        mainProteinTable.setSelectable(true);
        mainProteinTable.setSortEnabled(false);
        mainProteinTable.setColumnReorderingAllowed(false);

        mainProteinTable.setColumnCollapsingAllowed(true);
        mainProteinTable.setImmediate(true); // react at once when something is selected
        mainProteinTable.setMultiSelect(false);

        mainProteinTable.addContainerProperty("Index", Integer.class, null, "", null, Table.Align.RIGHT);
        mainProteinTable.addContainerProperty("selectedRow", RadioButton.class, null, "", null, Table.Align.CENTER);
        mainProteinTable.addContainerProperty("Accession", ExternalLink.class, null, "Accession", null, Table.Align.CENTER);
        mainProteinTable.addContainerProperty("Name", String.class, null, "Name", null, Table.Align.LEFT);
        mainProteinTable.addContainerProperty("userdata", TrendSymbol.class, null, " ", null, Table.Align.CENTER);

        mainProteinTable.addContainerProperty("Comparisons Overview", ProteinTrendLayout.class, null, "", null, Table.Align.LEFT);
        mainProteinTable.setColumnCollapsed("userdata", showUserData);

        mainProteinTable.setColumnWidth("selectedRow", 30);
        mainProteinTable.setColumnWidth("Index", 47);
        mainProteinTable.setColumnWidth("Accession", 87);
        mainProteinTable.setColumnWidth("Name", 187);
        mainProteinTable.setColumnWidth("userdata", userDataColumnWidth);

        spacer.setWidth((355), Unit.PIXELS);
        userSortingHeaderWrapper.setWidth(userDataColumnWidth, Unit.PIXELS);
        availableProteinLayoutWidth = width - 48 - 87 - 187 - 30 - userDataColumnWidth;//- 47
        topComparisonsContainer.setWidth(availableProteinLayoutWidth, Unit.PIXELS);
        mainProteinTable.setColumnWidth("Comparisons Overview", availableProteinLayoutWidth);

        mainProteinTable.addHeaderClickListener((Table.HeaderClickEvent event) -> {
            if (event.getPropertyId() == null) {
                return;

            }
            if (event.getPropertyId().toString().equalsIgnoreCase("selected")) {
                if (selectedItemIds.isEmpty()) {
                    return;
                }
                selectedOnly = !selectedOnly;
                showSelectedOnly();

            } else if (!event.getPropertyId().toString().equalsIgnoreCase("Comparisons Overview") && !event.getPropertyId().toString().equalsIgnoreCase("Index") && !event.getPropertyId().toString().equalsIgnoreCase("userdata")) {//
                mainProteinTable.removeStyleName("hidesortingicon");
                mainProteinTable.setSortEnabled(true);

                if (mainProteinTable.getSortContainerPropertyId() == null || !mainProteinTable.getSortContainerPropertyId().toString().equalsIgnoreCase(event.getPropertyId().toString())) {
                    mainProteinTable.sort(new String[]{event.getPropertyId().toString()}, new boolean[]{false});
                } else {
                    mainProteinTable.sort(new String[]{event.getPropertyId().toString()}, new boolean[]{!mainProteinTable.isSortAscending()});

                }
                mainProteinTable.setSortEnabled(false);
//
                int indexing = 1;
                for (Object id : mainProteinTable.getItemIds()) {
                    Item item = mainProteinTable.getItem(id);
                    item.getItemProperty("Index").setValue(indexing);
                    indexing++;
                }
                hideCheckedColumn(hideCheckedColumn);
                for (ColumnHeaderLayout columnHeader : columnHeaderSet) {
                    columnHeader.reset();
                    columnHeader.noSort();
                }
//
            }

        });
        mainProteinTable.addColumnResizeListener((Table.ColumnResizeEvent event) -> {
            mainProteinTable.setColumnWidth(event.getPropertyId(), event.getPreviousWidth());
        });
        return mainProteinTable;

    }

    private void showSelectedOnly() {
        mainProteinTable.removeAllItems();
//        mainProteinTable.setColumnCollapsed("emptyselection", true);
//        if (!selectedOnly) {
//            mainProteinTable.setColumnHeader("selected", " Click to show checked rows only");
//            mainProteinTable.setColumnIcon("selected", showSelectedeRes);

//            mainProteinTable.setColumnIcon("selected", checkedRes);
        for (Object itemId : selectedItemIds) {
            if (tableItemsMap.containsKey(itemId)) {
                mainProteinTable.addItem(tableItemsMap.get(itemId), itemId);
            }
//                else {
//                    System.out.println("at not found item id " + itemId);
//                }
        }

//        } else {
////            mainProteinTable.setColumnIcon("selected", checkedAppliedRes);
////            mainProteinTable.setColumnHeader("selected", " Click to show all rows");
////            mainProteinTable.setColumnIcon("selected", showAllRes);
//
//            if (!selectedItemIds.isEmpty()) {
//                for (Object itemId : selectedItemIds) {
//                    if (tableItemsMap.containsKey(itemId)) {
//                        mainProteinTable.addItem(tableItemsMap.get(itemId), itemId);
//                    }
//                }
//            } else {
//                for (Object itemId : activeTableItemsMap.keySet()) {
//                    if (tableItemsMap.containsKey(itemId)) {
//                        mainProteinTable.addItem(tableItemsMap.get(itemId), itemId);
//                    }
//                }
//            }
//        }
//        mainProteinTable.setSortEnabled(true);
//        mainProteinTable.setSortAscending(false);
        int indexing = 1;
        for (Object id : mainProteinTable.getItemIds()) {
            Item item = mainProteinTable.getItem(id);
            item.getItemProperty("Index").setValue(indexing);
            indexing++;
        }
        updateRowNumber(mainProteinTable.getItemIds().size(), generateThumbImg());
//        mainProteinTable.setColumnWidth("selected", 47);

    }

    private Set<QuantDiseaseGroupsComparison> selectedComparisonsList;
    private Set<QuantComparisonProtein> inUseSselectedProteinsList;
    private Set<QuantComparisonProtein> fullSselectedProteinsList;

    public Set<QuantDiseaseGroupsComparison> getSelectedComparisonsList() {
        return selectedComparisonsList;
    }

    public Set<QuantComparisonProtein> getSelectedProteinsList() {
        return inUseSselectedProteinsList;
    }

    /**
     * update table selection based on user comparison selection
     *
     * @param selectedComparisonsList
     * @param selectedProteinsList
     */
    public void updateTableData(Set<QuantDiseaseGroupsComparison> selectedComparisonsList, Set<QuantComparisonProtein> selectedProteinsList) {

        tableItemsMap.clear();
        tableProteinsToIDMap.clear();
        tableItemscheckboxMap.clear();
        this.mainProteinTable.removeValueChangeListener(ProteinTable.this);
        mainProteinTable.removeAllItems();

        filtersMap.clear();

        if (userCustomizedComparison != null) {
            spacer.removeAllComponents();
            if (mainProteinTable.isColumnCollapsed("userdata")) {
                mainProteinTable.setColumnCollapsed("userdata", false);
//                mainProteinTable.setColumnWidth("userdata", 30);
//                availableProteinLayoutWidth = availableProteinLayoutWidth - 30;
                mainProteinTable.setColumnWidth("Comparisons Overview", availableProteinLayoutWidth);
                topComparisonsContainer.setWidth(availableProteinLayoutWidth, Unit.PIXELS);

            }

        } else {
            if (!mainProteinTable.isColumnCollapsed("userdata")) {
                mainProteinTable.setColumnCollapsed("userdata", true);
//                availableProteinLayoutWidth = availableProteinLayoutWidth + 30;
                mainProteinTable.setColumnWidth("Comparisons Overview", availableProteinLayoutWidth);
                topComparisonsContainer.setWidth(availableProteinLayoutWidth, Unit.PIXELS);

            }

        }

        this.fullSselectedProteinsList = selectedProteinsList;
        this.inUseSselectedProteinsList = fullSselectedProteinsList;
        int protId = 0;
        for (QuantComparisonProtein protein : selectedProteinsList) {
            String accession = protein.getProteinAccession();//.replace("(unreviewed)", " (Unreviewed)");
            String name = protein.getProteinName();
            String url = protein.getUrl();

            String description = "Click to view in UniProt";
            if (url == null) {
                url = "";
                description = "UniProt information is not available";
            }
            url = url.replace("(UNREVIEWED)", "");
            ExternalLink accessionObject = new ExternalLink(accession, new ExternalResource(url));
            accessionObject.setDescription(description);
//            ExternalLink nameObject = new ExternalLink(name, new ExternalResource(url));
            this.selectedComparisonsList = selectedComparisonsList;
            ProteinTrendLayout protTrendLayout = new ProteinTrendLayout(selectedComparisonsList, protein, availableProteinLayoutWidth, protId, (protId < 10), smallScreen) {

                @Override
                public void selectTableItem(Object itemId) {
                    if (mainProteinTable.getValue() == itemId) {
                        mainProteinTable.unselect(itemId);
                    } else {
                        mainProteinTable.select(itemId);
                    }
                }

            };
            if (userCustomizedComparison != null) {
                if (userCustomizedComparison.getQuantComparisonProteinMap().containsKey(accession)) {
                    protTrendLayout.updateCustTrend(userCustomizedComparison.getQuantComparisonProteinMap().get(accession).getSignificantTrindCategory());
                }

            }
            RadioButton btn = new RadioButton(protId) {

                @Override
                public void selectItem(Object itemId) {
                    if (mainProteinTable.getValue() != null && mainProteinTable.getValue().equals(itemId)) {
                        mainProteinTable.unselect(itemId);
                    } else {
                        mainProteinTable.select(itemId);
                    }
                }
            };

            if (userCustomizedComparison == null || userCustomizedComparison.getQuantComparisonProteinMap()==null) {
                tableItemsMap.put(protId, new Object[]{protId + 1, btn, accessionObject, name, null, protTrendLayout});
            } else {
                int trend;
                if (userCustomizedComparison.getQuantComparisonProteinMap().get(accession).getOverallCellPercentValue() == 100.0) {
                    trend = 0;
                } else if (userCustomizedComparison.getQuantComparisonProteinMap().get(accession).getOverallCellPercentValue() == 0) {
                    trend = 3;
                } else {
                    trend = 4;
                }
                TrendSymbol trendSymbol = new TrendSymbol(trend);
                trendSymbol.setTrend((int) userCustomizedComparison.getQuantComparisonProteinMap().get(accession).getOverallCellPercentValue());
                trendSymbol.setWidth(12, Unit.PIXELS);
                trendSymbol.setHeight(12, Unit.PIXELS);
                trendSymbol.setDescription("" + userCustomizedComparison.getQuantComparisonProteinMap().get(accession).getSignificantTrindCategory());
                tableItemsMap.put(protId, new Object[]{protId + 1, btn, accessionObject, name, trendSymbol, protTrendLayout});
            }
            activeTableItemsMap.put(protId, tableItemsMap.get(protId));
            mainProteinTable.addItem(tableItemsMap.get(protId), protId);
            tableProteinsToIDMap.put(accession, protId);
            protId++;

        }
        updateComparisonsHeader(selectedComparisonsList);
        ColumnHeaderLayout defaultSorting;
        if (userCustomizedComparison != null) {
            defaultSorting = custUserComparisonSortingLayout;
            defaultSorting.sort(false, -1);
        } else {
            defaultSorting = columnHeaderSet.iterator().next();
            defaultSorting.sort(false, 0);
        }

        defaultSorting.setAsDefault();

        mainProteinTable.setSortEnabled(
                false);
        int indexing = 1;
        for (Object id
                : mainProteinTable.getItemIds()) {
            Item item = mainProteinTable.getItem(id);
            item.getItemProperty("Index").setValue(indexing);
            indexing++;
        }

        this.mainProteinTable.addValueChangeListener(ProteinTable.this);

        updateRowNumber(mainProteinTable.getItemIds().size(), generateThumbImg());
//        hideCheckedColumn(true);
        mainProteinTable.refreshRowCache();
        mainProteinTable.setWidthUndefined();

    }

    private void updateComparisonsHeader(Set<QuantDiseaseGroupsComparison> selectedComparisonsList) {
        topComparisonsContainer.removeAllComponents();
        columnHeaderSet.clear();
        int index = 0;
        ColumnHeaderLayout comparisonLayout;

        for (QuantDiseaseGroupsComparison comparison : selectedComparisonsList) {
            comparisonLayout = new ColumnHeaderLayout(comparison, index) {

                @Override
                public void sort(boolean up, int index) {
                    sortOnComparison(up, index);
                }

                @Override
                public void dropComparison(QuantDiseaseGroupsComparison comparison) {

                    ProteinTable.this.dropComparison(comparison);
                }

                @Override
                public void filterTable(QuantDiseaseGroupsComparison comparison, int comparisonIndex, Set<Object> filterSet) {
                    filterTableSelection(comparison, filterSet);
                }

            };
            filtersMap.put(comparison, null);
            index++;
            topComparisonsContainer.addComponent(comparisonLayout);
            columnHeaderSet.add(comparisonLayout);

        }
        if (userCustomizedComparison != null) {
            columnHeaderSet.add(custUserComparisonSortingLayout);
        }
    }

    /**
     * Drop comparison (un select comparison)
     *
     * @param index
     */
    public abstract void dropComparison(QuantDiseaseGroupsComparison index);

    private boolean isFiltered = false;

    private void filterTableSelection(QuantDiseaseGroupsComparison comparison, Set<Object> filters) {

        if (filters == null || filters.isEmpty()) {
            filters = null;
        }
        if (comparison == null) {
            filtersMap.keySet().stream().forEach((com) -> {
                filtersMap.put(com, null);
            });
            if (isFiltered) {
                isFiltered = false;
            } else {
                return;
            }
        } else {

            filtersMap.remove(comparison);
            filtersMap.put(comparison, filters);
            isFiltered = true;

        }
        Set<String> filteredProteinsList = new LinkedHashSet<>(this.tableProteinsToIDMap.keySet());

        for (QuantDiseaseGroupsComparison i : filtersMap.keySet()) {

            if (filtersMap.get(i) != null) {
                filteredProteinsList = filter(filteredProteinsList, i, filtersMap.get(i));

            }

        }

        mainProteinTable.removeAllItems();
        for (String accession : filteredProteinsList) {
            Object itemId = tableProteinsToIDMap.get(accession);
            Object[] items = tableItemsMap.get(itemId);
            mainProteinTable.addItem(items, itemId);

        }
        if (mainProteinTable.getItemIds().size() == tableItemsMap.size()) {
            isFiltered = false;
        }
        inUseSselectedProteinsList = new LinkedHashSet<>();
        for (QuantComparisonProtein prot : fullSselectedProteinsList) {
            if (filteredProteinsList.contains(prot.getProteinAccession())) {
                inUseSselectedProteinsList.add(prot);
            }

        }
        int indexing = 1;
        for (Object id
                : mainProteinTable.getItemIds()) {
            Item item = mainProteinTable.getItem(id);
            item.getItemProperty("Index").setValue(indexing);
            indexing++;
        }
        updateRowNumber(mainProteinTable.getItemIds().size(), generateThumbImg());

    }

    private Set<String> filter(Set<String> proteinsList, QuantDiseaseGroupsComparison comparison, Set<Object> filters) {
        if (filters == null || filters.isEmpty()) {
            return proteinsList;
        }
        Set<String> updatedProteinsList = new LinkedHashSet<>();
        filters.stream().map((filter) -> (comparison.getProteinsByTrendMap().get((Integer) filter))).forEach((tempList) -> {
            tempList.stream().filter((protein) -> (proteinsList.contains(protein.getProteinAccession()))).forEach((protein) -> {
                updatedProteinsList.add(protein.getProteinAccession());
            });
        });
        return updatedProteinsList;

    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        if (event.getProperty().getValue() == null) {
            selectProtein(null, -1);
            return;
        }
        String value = null;
        ExternalLink link = (ExternalLink) mainProteinTable.getItem(event.getProperty().getValue()).getItemProperty("Accession").getValue();
        if (link != null) {
            value = link.getCaption();
        }
        if (userCustomizedComparison != null) {
            if (userCustomizedComparison.getQuantComparisonProteinMap().containsKey(value)) {
                selectProtein(value, userCustomizedComparison.getQuantComparisonProteinMap().get(value).getSignificantTrindCategory());
                return;
            }

        }
        selectProtein(value, -1);

    }

    public abstract void selectProtein(String selectedProtein, int custTrend);

    public abstract void updateRowNumber(int rowNumber, String URl);
}
