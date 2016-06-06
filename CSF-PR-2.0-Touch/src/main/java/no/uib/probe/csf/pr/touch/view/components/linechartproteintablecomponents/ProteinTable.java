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
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Table;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantComparisonProtein;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.view.core.ExternalLink;
import no.uib.probe.csf.pr.touch.view.core.ProteinTrendLayout;

/**
 *
 * @author Yehia Farag
 */
public class ProteinTable extends Table {

    private boolean selectedOnly = false;

    private final Map<Object, Object[]> tableItemsMap;
    private final Map<String, Integer> tableProteinsToIDMap;
    private final Map<Object, CheckBox> tableItemscheckboxMap;

    private final int availableProteinLayoutWidth;
    /* This set contains the ids of the "selected" items */
    private final Set<Object> selectedItemIds = new HashSet<>();
    private final ThemeResource checkedRes = new ThemeResource("img/checked.png");
    private final ThemeResource checkedAppliedRes = new ThemeResource("img/checked_applied.png");

    public void filterTable(Set<QuantComparisonProtein> selectedProteinsList) {
        selectedItemIds.clear();
        for (QuantComparisonProtein protein : selectedProteinsList) {
            selectedItemIds.add(tableProteinsToIDMap.get(protein.getProteinAccession()));
//            tableItemscheckboxMap.get().setValue(true);
        }
        selectedOnly = true;
        showSelectedOnly();

    }

    public ProteinTable(int width) {

        this.tableItemsMap = new LinkedHashMap<>();
        this.tableProteinsToIDMap = new HashMap<>();
        tableItemscheckboxMap = new HashMap<>();
        this.setWidth(100, Unit.PERCENTAGE);
        this.setHeightUndefined();
        this.setSelectable(true);
        this.setColumnReorderingAllowed(false);
        this.setColumnCollapsingAllowed(false);
        this.setImmediate(true); // react at once when something is selected
        this.setMultiSelect(false);

        this.addContainerProperty("Index", Integer.class, null, "", null, Table.Align.RIGHT);
        this.addContainerProperty("Accession", ExternalLink.class, null, "Accession", null, Table.Align.LEFT);
        this.addContainerProperty("Name", ExternalLink.class, null, "Name", null, Table.Align.LEFT);
        this.addContainerProperty("Comparisons Overview", ProteinTrendLayout.class, null, "Comparisons Overview", null, Table.Align.LEFT);

        /* This checkbox reflects the contents of the selectedItemIds set */
        this.addGeneratedColumn("selected", (Table source, final Object itemId, Object columnId) -> {
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

        this.setColumnIcon("selected", checkedRes);
        this.setColumnHeader("selected", "Only Checked");
        this.setColumnAlignment("selected", Table.Align.CENTER);
        this.setColumnWidth("selected", 70);
        this.setColumnWidth("Index", 47);
        this.setColumnWidth("Accession", 87);
        this.setColumnWidth("Name", 187);
        availableProteinLayoutWidth = width - 71 - 47 - 87 - 187 - 10;
        this.setColumnWidth("Comparisons Overview", availableProteinLayoutWidth);

        this.addHeaderClickListener((Table.HeaderClickEvent event) -> {
            if (event.getPropertyId().toString().equalsIgnoreCase("selected")) {
                if (selectedItemIds.isEmpty()) {
                    return;
                }
                selectedOnly = !selectedOnly;
                showSelectedOnly();

            }
        });

        this.addColumnResizeListener((Table.ColumnResizeEvent event) -> {
            this.setColumnWidth(event.getPropertyId(), event.getPreviousWidth());
        });
        
        this.sort(new String[]{"Comparisons Overview"
        }, new boolean[]{false
        }
        );

        this.setSortAscending(false);
      

    }

    private void showSelectedOnly() {
        this.removeAllItems();
        if (!selectedOnly) {
            this.setColumnIcon("selected", checkedRes);
            for (Object itemId : tableItemsMap.keySet()) {
                this.addItem(tableItemsMap.get(itemId), itemId);
            }

        } else {
            this.setColumnIcon("selected", checkedAppliedRes);
            if (!selectedItemIds.isEmpty()) {
                for (Object itemId : selectedItemIds) {
                    this.addItem(tableItemsMap.get(itemId), itemId);
                }
            } else {
                for (Object itemId : tableItemsMap.keySet()) {
                    this.addItem(tableItemsMap.get(itemId), itemId);
                }
            }

        }

    }

    public void updateTableData(Set<QuantDiseaseGroupsComparison> selectedComparisonsList, Set<QuantComparisonProtein> selectedProteinsList) {

        this.setHeight(100, Unit.PERCENTAGE);
        tableItemsMap.clear();
        tableProteinsToIDMap.clear();
        tableItemscheckboxMap.clear();
        this.removeAllItems();

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
            this.addItem(tableItemsMap.get(protId), protId);
            tableProteinsToIDMap.put(accession, protId);
            protId++;

        }
          int indexing = 1;
        for (Object id: this.getItemIds()) {
            Item item = this.getItem(id);
            item.getItemProperty("Index").setValue(indexing);
            indexing++;
        }

    }

}
