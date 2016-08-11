package no.uib.probe.csf.pr.touch.view.components.linechartproteintablecomponents;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
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
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import no.uib.probe.csf.pr.touch.logic.beans.QuantComparisonProtein;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.view.core.ColumnHeaderLayout;
import no.uib.probe.csf.pr.touch.view.core.ExternalLink;
import no.uib.probe.csf.pr.touch.view.core.ProteinTrendLayout;
import no.uib.probe.csf.pr.touch.view.core.ScrollingTable;
import no.uib.probe.csf.pr.touch.view.core.ScrollingTableScrollListener;
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

    private final int availableProteinLayoutWidth;
    /* This set contains the ids of the "selected" items */
    private final Set<Object> selectedItemIds = new HashSet<>();
    private final Table mainProteinTable;
    private final HorizontalLayout topComparisonsContainer;
    private final Map<QuantDiseaseGroupsComparison, Set<Object>> filtersMap;

    private ThemeResource showAllRes = new ThemeResource("img/show_selected.png");

    private ThemeResource showSelectedeRes = new ThemeResource("img/show_all.png");
    private boolean hideCheckedColumn = false;

    public final void hideCheckedColumn(boolean hide) {

        this.hideCheckedColumn = hide;
        mainProteinTable.setColumnCollapsed("emptyselection", !hide);
        mainProteinTable.setColumnCollapsed("selected", hide);
        mainProteinTable.markAsDirty();
        mainProteinTable.setWidth(100, Unit.PERCENTAGE);
    }

    public void setUserCustomizedComparison(QuantDiseaseGroupsComparison userCustomizedComparison) {
        this.userCustomizedComparison = userCustomizedComparison;
        if (userCustomizedComparison != null) {
            userCustomizedComparison.getQuantComparisonProteinMap().keySet().stream().forEach((accession) -> {
                Object itemId = tableProteinsToIDMap.get(accession);
                if (tableItemsMap.containsKey(itemId)) {
                    Object[] items = tableItemsMap.get(itemId);
                    ProteinTrendLayout protTrendLayout = (ProteinTrendLayout) items[3];
                    if (protTrendLayout != null) {
                        protTrendLayout.updateCustTrend(userCustomizedComparison.getQuantComparisonProteinMap().get(accession).getSignificantTrindCategory());
                    }
                }
            });
            activeTableItemsMap.clear();
            activeTableItemsMap.putAll(tableItemsMap);
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
        mainProteinTable.setColumnHeader("selected", "Click to show checked rows only");
        for (Object itemId : filteredItemIds) {
            mainProteinTable.addItem(tableItemsMap.get(itemId), itemId);
            activeTableItemsMap.put(itemId, tableItemsMap.get(itemId));
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
        final Border fullBorder = new LineBorder(Color.GRAY);
        JPanel proteinSequencePanel = new JPanel();
        proteinSequencePanel.setLayout(null);
        proteinSequencePanel.setSize(100, 100);
        proteinSequencePanel.setBackground(Color.WHITE);
        int dsIndex = 1;
        int y = 0;
        for (Object[] objArr : tableItemsMap.values()) {
            ProteinTrendLayout trend = (ProteinTrendLayout) objArr[3];
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

        String base64 = com.itextpdf.text.pdf.codec.Base64.encodeBytes(imageData);
        return base64 = "data:image/png;base64," + base64;

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

    public void filterViewItemTable(Set<QuantComparisonProtein> selectedProteinsList) {
        selectedItemIds.clear();
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
    }

    /**
     * update sorting buttons to filter buttons
     */
    public void switchHeaderBtns() {

        columnHeaderSet.stream().forEach((comparisonLayout) -> {
            comparisonLayout.swichBtns();
        });

    }

    /**
     * Sort table based on specific comparison
     *
     * @param upSort
     * @param comparisonIndex
     */
    public void sortOnComparison(boolean upSort, int comparisonIndex) {

        int index = 0;
        for (ColumnHeaderLayout comparisonLayout : columnHeaderSet) {
            if (index == comparisonIndex) {
                index++;
                continue;
            }
            comparisonLayout.noSort();
            index++;
        }
        tableItemsMap.values().stream().map((arr) -> (ProteinTrendLayout) arr[3]).forEach((protTrendLayout) -> {
            protTrendLayout.setSortableColumnIndex(comparisonIndex);

        });
        this.mainProteinTable.removeValueChangeListener(ProteinTable.this);
        mainProteinTable.removeAllItems();
        for (Object object : tableItemsMap.keySet()) {
            mainProteinTable.addItem(tableItemsMap.get(object), object);
        }

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

    /**
     *
     * @param width
     * @param height
     */
    public ProteinTable(int width, int height) {

        this.setWidth(100, Unit.PERCENTAGE);
        this.setHeightUndefined();

        this.columnHeaderSet = new LinkedHashSet<>();
        this.filtersMap = new LinkedHashMap<>();

        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidthUndefined();
        topLayout.setHeight(20, Unit.PIXELS);
        VerticalLayout spacer = new VerticalLayout();
        spacer.setHeight(100, Unit.PERCENTAGE);
        spacer.setWidth(325, Unit.PIXELS);
        topLayout.addComponent(spacer);

        topComparisonsContainer = new HorizontalLayout();
        topComparisonsContainer.setHeight(100, Unit.PERCENTAGE);
        topComparisonsContainer.setStyleName("spacing");
        topLayout.addComponent(topComparisonsContainer);

        this.addComponent(topLayout);

        this.mainProteinTable = new Table() {
//            int count = 0;
////            @Override
////            protected void registerComponent(Component component) {
////                
////
////                if (getCurrentPageFirstItemId() != null) {
////                    Object[] items = tableItemsMap.get(getCurrentPageFirstItemId());
////                    ProteinTrendLayout protTrendLayout = (ProteinTrendLayout) items[3];
////                    if (!protTrendLayout.isMax()) {
////                        protTrendLayout.maxmize();
////                        markAsDirty();
////                    }
////                }
////
////                super.registerComponent(component); //To change body of generated methods, choose Tools | Templates.
////            }
//            boolean reserCounter = true;
//            int tableitemmapsize = -1;
//
//            @Override
//            protected void refreshRenderedCells() {
//                if (tableItemsMap == null) {
//                    super.refreshRenderedCells();
//                    return;
//                }
//
//                
//                if (tableitemmapsize != tableItemsMap.size()) {
//                    tableitemmapsize = tableItemsMap.size();
//                    count = 0;
//                    return;
//                }
//               
//
//                System.out.println("at ------------------------------- count is " + count++ + "    " + tableItemsMap.size());
//                if (getCurrentPageFirstItemId() != null) {
//                    Object[] items = tableItemsMap.get(getCurrentPageFirstItemId());
//                    ProteinTrendLayout protTrendLayout = (ProteinTrendLayout) items[3];
//                    if (!protTrendLayout.isMax()) {
//                        protTrendLayout.maxmize();
////                        setCurrentPageFirstItemId(getCurrentPageFirstItemId());
//                        markAsDirty();
//
//                    }
//
//                }
//                super.refreshRenderedCells(); //To change body of generated methods, choose Tools | Templates.
//            }

        };
//        mainProteinTable.setBuffered(true);
        mainProteinTable.setCacheRate(1);

//        this.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
//            
//            @Override
//            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
//                
//                System.out.println("at context clicked "+(event.getRelativeX()+"  "+event.getRelativeY()+"  width is "+ width+"  scroll is "+ (width-20)+"  scroll comp "+ event.getChildComponent())+"   maxmize index "+mainProteinTable.getCurrentPageFirstItemIndex()+" --- "+mainProteinTable.getCurrentPageFirstItemId());
//            }
//        });
//        mainProteinTable.addScrollListener(new ScrollingTableScrollListener() {
//
//            @Override
//            public void doTableScroll() {
//                Notification.show("You are scrolling!\nYou can add your own behavior here!");
//            }
//        });
        this.mainProteinTable.addValueChangeListener(ProteinTable.this);
        this.mainProteinTable.addStyleName(ValoTheme.TABLE_SMALL);
        this.mainProteinTable.setHeight(height - 22, Unit.PIXELS);
//        int pageLength = ((int) mainProteinTable.getHeight() - 510) / 111;
//
//        mainProteinTable.setPageLength(Math.max(1, pageLength + 2));
//        System.out.println("at ------------------------------------------------>>   page length is " + pageLength + "   " + mainProteinTable.getPageLength());
        this.addComponent(mainProteinTable);
        this.tableItemsMap = new LinkedHashMap<>();
        this.activeTableItemsMap = new LinkedHashMap<>();
        this.tableProteinsToIDMap = new HashMap<>();
        tableItemscheckboxMap = new HashMap<>();

        mainProteinTable.setSelectable(true);
        mainProteinTable.setSortEnabled(false);
        mainProteinTable.setColumnReorderingAllowed(false);
        mainProteinTable.setColumnCollapsingAllowed(true);
        mainProteinTable.setImmediate(true); // react at once when something is selected
        mainProteinTable.setMultiSelect(false);

        mainProteinTable.addContainerProperty("Index", Integer.class, null, "", null, Table.Align.RIGHT);
        mainProteinTable.addContainerProperty("Accession", ExternalLink.class, null, "Accession", null, Table.Align.LEFT);
        mainProteinTable.addContainerProperty("Name", String.class, null, "Name", null, Table.Align.LEFT);
        mainProteinTable.addContainerProperty("Comparisons Overview", ProteinTrendLayout.class, null, "Comparisons Overview", null, Table.Align.LEFT);
        mainProteinTable.addContainerProperty("emptyselection", String.class, null, "", null, Table.Align.LEFT);
        mainProteinTable.setColumnCollapsed("emptyselection", false);

        /* This checkbox reflects the contents of the filteredItemIds set */
        mainProteinTable.addGeneratedColumn("selected", (Table source, final Object itemId, Object columnId) -> {
            boolean selected = selectedItemIds.contains(itemId);
            /* When the chekboc value changes, add/remove the itemId from the filteredItemIds set */
            final CheckBox cb = new CheckBox("", selected);
            tableItemscheckboxMap.put(itemId, cb);
            cb.addValueChangeListener((Property.ValueChangeEvent event) -> {
                if (selectedOnly) {
                    cb.setValue(true);
                    return;
                }
                if (selectedItemIds.contains(itemId)) {
                    selectedItemIds.remove(itemId);
                } else {
                    selectedItemIds.add(itemId);
                }
            });
            return cb;
        });

        mainProteinTable.setColumnIcon("selected", showSelectedeRes);
        mainProteinTable.setColumnCollapsed("selected", false);
        mainProteinTable.setColumnHeader("selected", " Click to show checked rows only");
        mainProteinTable.setColumnAlignment("selected", Table.Align.CENTER);
        mainProteinTable.setColumnWidth("selected", 47);
        mainProteinTable.setColumnWidth("emptyselection", 47);
        mainProteinTable.setColumnWidth("Index", 47);
        mainProteinTable.setColumnWidth("Accession", 87);
        mainProteinTable.setColumnWidth("Name", 187);
        availableProteinLayoutWidth = width - 48 - 47 - 87 - 187 - 25;
        topComparisonsContainer.setWidth(availableProteinLayoutWidth, Unit.PIXELS);
        mainProteinTable.setColumnWidth("Comparisons Overview", availableProteinLayoutWidth);

        mainProteinTable.addHeaderClickListener((Table.HeaderClickEvent event) -> {
            if (event.getPropertyId() == null) {
                System.out.println("at header is null");
                return;

            }
            if (event.getPropertyId().toString().equalsIgnoreCase("selected")) {
                if (selectedItemIds.isEmpty()) {
                    return;
                }
                selectedOnly = !selectedOnly;
                showSelectedOnly();

            } else if (!event.getPropertyId().toString().equalsIgnoreCase("Comparisons Overview") && !event.getPropertyId().toString().equalsIgnoreCase("Index")) {//
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

//        mainProteinTable.sort(new String[]{"Index"}, new boolean[]{false});
//        mainProteinTable.setSortAscending(false);
    }

    private void showSelectedOnly() {
        mainProteinTable.removeAllItems();
        mainProteinTable.setColumnCollapsed("emptyselection", true);
        if (!selectedOnly) {
            mainProteinTable.setColumnHeader("selected", " Click to show checked rows only");
            mainProteinTable.setColumnIcon("selected", showSelectedeRes);

//            mainProteinTable.setColumnIcon("selected", checkedRes);
            for (Object itemId : activeTableItemsMap.keySet()) {
                mainProteinTable.addItem(tableItemsMap.get(itemId), itemId);
            }

        } else {
//            mainProteinTable.setColumnIcon("selected", checkedAppliedRes);
            mainProteinTable.setColumnHeader("selected", " Click to show all rows");
            mainProteinTable.setColumnIcon("selected", showAllRes);

            if (!selectedItemIds.isEmpty()) {
                for (Object itemId : selectedItemIds) {
                    mainProteinTable.addItem(tableItemsMap.get(itemId), itemId);
                }
            } else {
                for (Object itemId : activeTableItemsMap.keySet()) {
                    mainProteinTable.addItem(tableItemsMap.get(itemId), itemId);
                }
            }

        }
        updateRowNumber(mainProteinTable.getItemIds().size(), generateThumbImg());
        mainProteinTable.setColumnWidth("selected", 47);

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
            ProteinTrendLayout protTrendLayout = new ProteinTrendLayout(selectedComparisonsList, protein, availableProteinLayoutWidth, protId, (protId < 10)) {

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

            tableItemsMap.put(protId, new Object[]{protId + 1, accessionObject, name, protTrendLayout, ""});
            activeTableItemsMap.put(protId, tableItemsMap.get(protId));
            mainProteinTable.addItem(tableItemsMap.get(protId), protId);
            tableProteinsToIDMap.put(accession, protId);
            protId++;

        }
        int indexing = 1;
        for (Object id : mainProteinTable.getItemIds()) {
            Item item = mainProteinTable.getItem(id);
            item.getItemProperty("Index").setValue(indexing);

            indexing++;
        }

        updateComparisonsHeader(selectedComparisonsList);
        this.mainProteinTable.addValueChangeListener(ProteinTable.this);
        updateRowNumber(mainProteinTable.getItemIds().size(), generateThumbImg());

        hideCheckedColumn(true);
        Object[] items = tableItemsMap.get(mainProteinTable.getCurrentPageFirstItemId());
        ProteinTrendLayout protTrendLayout = (ProteinTrendLayout) items[3];
        protTrendLayout.maxmize();

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
//        if (comparisonLayout != null) {
//            comparisonLayout.setAsDefault();
//        }
    }

    /**
     * Drop comparison (un select comparison)
     *
     * @param index
     */
    public abstract void dropComparison(QuantDiseaseGroupsComparison index);

    private boolean isFiltered = false;

    private void filterTableSelection(QuantDiseaseGroupsComparison comparison, Set<Object> filters) {
        System.out.println("at filter table selection table");

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
        updateRowNumber(mainProteinTable.getItemIds().size(), generateThumbImg());

    }

    private Set<String> filter(Set<String> proteinsList, QuantDiseaseGroupsComparison comparison, Set<Object> filters) {
        System.out.println("at filter just table");
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
