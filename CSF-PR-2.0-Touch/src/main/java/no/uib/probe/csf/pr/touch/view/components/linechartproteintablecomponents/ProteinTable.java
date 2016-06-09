/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.components.linechartproteintablecomponents;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
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
 */
public class ProteinTable extends VerticalLayout {

    private boolean selectedOnly = false;

    private final Map<Object, Object[]> tableItemsMap;
    private final Map<String, Integer> tableProteinsToIDMap;
    private final Map<Object, CheckBox> tableItemscheckboxMap;
    private final Set<ColumnHeaderLayout> columnHeaderSet;

    private final int availableProteinLayoutWidth;
    /* This set contains the ids of the "selected" items */
    private final Set<Object> selectedItemIds = new HashSet<>();
    private final ThemeResource checkedRes = new ThemeResource("img/checked.png");
    private final ThemeResource checkedAppliedRes = new ThemeResource("img/checked_applied.png");
    private final Table mainProteinTable;
    private final HorizontalLayout topComparisonsContainer;

    public void filterTable(Set<QuantComparisonProtein> selectedProteinsList) {
        selectedItemIds.clear();
        selectedProteinsList.stream().forEach((protein) -> {
            selectedItemIds.add(tableProteinsToIDMap.get(protein.getProteinAccession()));
        });
        selectedOnly = true;
        showSelectedOnly();

    }

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

    public ProteinTable(int width) {

        this.columnHeaderSet = new HashSet<>();
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

        this.mainProteinTable = new Table();
        this.addComponent(mainProteinTable);
        this.tableItemsMap = new LinkedHashMap<>();
        this.tableProteinsToIDMap = new HashMap<>();
        tableItemscheckboxMap = new HashMap<>();
        this.setWidth(100, Unit.PERCENTAGE);
        this.setHeightUndefined();
        mainProteinTable.setSelectable(true);
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

        mainProteinTable.setColumnIcon("selected", checkedRes);
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
            mainProteinTable.setColumnIcon("selected", checkedRes);
            for (Object itemId : tableItemsMap.keySet()) {
                mainProteinTable.addItem(tableItemsMap.get(itemId), itemId);
            }

        } else {
            mainProteinTable.setColumnIcon("selected", checkedAppliedRes);
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

    public void updateTableData(Set<QuantDiseaseGroupsComparison> selectedComparisonsList, Set<QuantComparisonProtein> selectedProteinsList) {

        mainProteinTable.setHeight(960, Unit.PIXELS);
        tableItemsMap.clear();
        tableProteinsToIDMap.clear();
        tableItemscheckboxMap.clear();
        mainProteinTable.removeAllItems();

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
            ProteinTrendLayout protTrendLayout = new ProteinTrendLayout(selectedComparisonsList, protein, availableProteinLayoutWidth);
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

    }

    private void updateComparisonsHeader(Set<QuantDiseaseGroupsComparison> selectedComparisonsList) {
        topComparisonsContainer.removeAllComponents();
        columnHeaderSet.clear();
        int index = 0;
        for (QuantDiseaseGroupsComparison comparison : selectedComparisonsList) {
            ColumnHeaderLayout comparisonLayout = new ColumnHeaderLayout(comparison, index) {

                @Override
                public void sort(boolean up, int index) {
                    sortOnComparison(up, index);
                }

            };
            index++;
            topComparisonsContainer.addComponent(comparisonLayout);
            columnHeaderSet.add(comparisonLayout);

        }

    }

}
