package no.uib.probe.csf.pr.touch.view.components.linechartproteintablecomponents;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantComparisonProtein;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.view.core.ColumnHeaderLayout;
import no.uib.probe.csf.pr.touch.view.core.ExternalLink;
import no.uib.probe.csf.pr.touch.view.core.ProteinTrendLayout;

/**
 *
 * @author Yehia Farag
 *
 * this class represents quant protein table container
 */
public abstract class ProteinTable extends VerticalLayout implements Property.ValueChangeListener {
    
    private boolean selectedOnly = false;
    
    private final Map<Object, Object[]> tableItemsMap;
    private final Map<String, Integer> tableProteinsToIDMap;
    private final Map<Object, CheckBox> tableItemscheckboxMap;
    private final Set<ColumnHeaderLayout> columnHeaderSet;
    
    private final int availableProteinLayoutWidth;
    /* This set contains the ids of the "selected" items */
    private final Set<Object> selectedItemIds = new HashSet<>();
    private final Table mainProteinTable;
    private final HorizontalLayout topComparisonsContainer;
    private final Map<QuantDiseaseGroupsComparison, Set<Object>> filtersMap;

    /**
     * filter table items to view only user protein selection
     *
     * @param selectedProteinsList
     */
    public void filterTable(Set<QuantComparisonProtein> selectedProteinsList) {
        selectedItemIds.clear();
        selectedProteinsList.stream().forEach((protein) -> {
            selectedItemIds.add(tableProteinsToIDMap.get(protein.getProteinAccession()));
        });
        selectedOnly = true;
        showSelectedOnly();
        
    }

    /**
     * Remove all applied columns filters
     */
    public void clearColumnFilters() {
        filterTableSelection(null, 1, null);
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
        mainProteinTable.sort(new String[]{"Comparisons Overview"}, new boolean[]{upSort});
        
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
        };
        this.mainProteinTable.addValueChangeListener(ProteinTable.this);
        this.mainProteinTable.addStyleName(ValoTheme.TABLE_SMALL);
        this.mainProteinTable.setHeight(height - 22, Unit.PIXELS);
        this.addComponent(mainProteinTable);
        this.tableItemsMap = new LinkedHashMap<>();
        this.tableProteinsToIDMap = new HashMap<>();
        tableItemscheckboxMap = new HashMap<>();
        
        mainProteinTable.setSelectable(true);
        mainProteinTable.setSortEnabled(false);
        mainProteinTable.setColumnReorderingAllowed(false);
        mainProteinTable.setColumnCollapsingAllowed(false);
        mainProteinTable.setImmediate(true); // react at once when something is selected
        mainProteinTable.setMultiSelect(false);
        
        mainProteinTable.addContainerProperty("Index", Integer.class, null, "", null, Table.Align.RIGHT);
        mainProteinTable.addContainerProperty("Accession", ExternalLink.class, null, "Accession", null, Table.Align.LEFT);
        mainProteinTable.addContainerProperty("Name", ExternalLink.class, null, "Name", null, Table.Align.LEFT);
        mainProteinTable.addContainerProperty("Comparisons Overview", ProteinTrendLayout.class, null, "Comparisons Overview", null, Table.Align.LEFT);

        /* This checkbox reflects the contents of the selectedItemIds set */
        mainProteinTable.addGeneratedColumn("selected", (Table source, final Object itemId, Object columnId) -> {
            boolean selected = selectedItemIds.contains(itemId);
            /* When the chekboc value changes, add/remove the itemId from the selectedItemIds set */
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

//        mainProteinTable.setColumnIcon("selected", checkedRes);
        mainProteinTable.setColumnHeader("selected", "Only Checked");
        mainProteinTable.setColumnAlignment("selected", Table.Align.CENTER);
        mainProteinTable.setColumnWidth("selected", 70);
        mainProteinTable.setColumnWidth("Index", 47);
        mainProteinTable.setColumnWidth("Accession", 87);
        mainProteinTable.setColumnWidth("Name", 187);
        availableProteinLayoutWidth = width - 71 - 47 - 87 - 187 - 10;
        topComparisonsContainer.setWidth(availableProteinLayoutWidth, Unit.PIXELS);
        mainProteinTable.setColumnWidth("Comparisons Overview", availableProteinLayoutWidth);
        
        mainProteinTable.addHeaderClickListener((Table.HeaderClickEvent event) -> {
            if (event.getPropertyId().toString().equalsIgnoreCase("selected")) {
                if (selectedItemIds.isEmpty()) {
                    return;
                }
                selectedOnly = !selectedOnly;
                showSelectedOnly();
                
            }
        });
        
        mainProteinTable.addColumnResizeListener((Table.ColumnResizeEvent event) -> {
            mainProteinTable.setColumnWidth(event.getPropertyId(), event.getPreviousWidth());
        });
        
        mainProteinTable.sort(new String[]{"Comparisons Overview"}, new boolean[]{false});
        
        mainProteinTable.setSortAscending(false);
        
    }
    
    private void showSelectedOnly() {
        mainProteinTable.removeAllItems();
        if (!selectedOnly) {
            mainProteinTable.setColumnHeader("selected", "Checked");
//            mainProteinTable.setColumnIcon("selected", checkedRes);
            for (Object itemId : tableItemsMap.keySet()) {
                mainProteinTable.addItem(tableItemsMap.get(itemId), itemId);
            }
            
        } else {
//            mainProteinTable.setColumnIcon("selected", checkedAppliedRes);
            mainProteinTable.setColumnHeader("selected", "All");
            if (!selectedItemIds.isEmpty()) {
                for (Object itemId : selectedItemIds) {
                    mainProteinTable.addItem(tableItemsMap.get(itemId), itemId);
                }
            } else {
                for (Object itemId : tableItemsMap.keySet()) {
                    mainProteinTable.addItem(tableItemsMap.get(itemId), itemId);
                }
            }
            
        }
        
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
            String accession = protein.getProteinAccession();
            String name = protein.getProteinName();
            String url = protein.getUrl();
            if (url == null) {
                url = "";
            }
            ExternalLink accessionObject = new ExternalLink(accession, new ExternalResource(url));
            ExternalLink nameObject = new ExternalLink(name, new ExternalResource(url));
            ProteinTrendLayout protTrendLayout = new ProteinTrendLayout(selectedComparisonsList, protein, availableProteinLayoutWidth, protId) {
                
                @Override
                public void selectTableItem(Object itemId) {
                    if (mainProteinTable.getValue() == itemId) {
                        mainProteinTable.unselect(itemId);
                    } else {
                        mainProteinTable.select(itemId);
                    }
                }
                
            };
            tableItemsMap.put(protId, new Object[]{protId + 1, accessionObject, nameObject, protTrendLayout});
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
        
    }
    
    private void updateComparisonsHeader(Set<QuantDiseaseGroupsComparison> selectedComparisonsList) {
        topComparisonsContainer.removeAllComponents();
        columnHeaderSet.clear();
        int index = 0;
        ColumnHeaderLayout comparisonLayout = null;
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
                    filterTableSelection(comparison, comparisonIndex, filterSet);
                }
                
            };
            filtersMap.put(comparison, null);
            index++;
            topComparisonsContainer.addComponent(comparisonLayout);
            columnHeaderSet.add(comparisonLayout);
            
        }
        if (comparisonLayout != null) {
            comparisonLayout.setAsDefault();
        }
    }

    /**
     * Drop comparison (un select comparison)
     *
     * @param index
     */
    public abstract void dropComparison(QuantDiseaseGroupsComparison index);
    
    private boolean isFiltered = false;
    
    private void filterTableSelection(QuantDiseaseGroupsComparison comparison, int comparisonIndex, Set<Object> filters) {
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
            selectProtein(null);
            return;
        }
        String value = null;
        ExternalLink link = (ExternalLink) mainProteinTable.getItem(event.getProperty().getValue()).getItemProperty("Accession").getValue();
        if (link != null) {
            value = link.getCaption();
        }
        selectProtein(value);
        
    }
    
    public abstract void selectProtein(String selectedProtein);
}
