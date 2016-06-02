/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.components.linechartproteintablecomponents;

import com.vaadin.data.Property;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Table;
import java.util.HashSet;
import java.util.LinkedHashMap;
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

    private boolean showSelectedOnly = false;

    private final Map<Object, Object[]> tableItemsMap;
    
    private final int availableProteinLayoutWidth; 

    public ProteinTable(int width) {

        this.tableItemsMap = new LinkedHashMap<>();
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

        /* This set contains the ids of the "selected" items */
        final Set<Object> selectedItemIds = new HashSet<>();
        /* This checkbox reflects the contents of the selectedItemIds set */
        this.addGeneratedColumn("selected", (Table source, final Object itemId, Object columnId) -> {
            boolean selected = selectedItemIds.contains(itemId);
            /* When the chekboc value changes, add/remove the itemId from the selectedItemIds set */
            final CheckBox cb = new CheckBox("", selected);
            cb.addValueChangeListener((Property.ValueChangeEvent event) -> {
                if (showSelectedOnly) {
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

        ThemeResource checkedRes = new ThemeResource("img/checked.png");
        ThemeResource checkedAppliedRes = new ThemeResource("img/checked_applied.png");
        this.setColumnIcon("selected", checkedRes);
        this.setColumnHeader("selected", "Only Checked");
        this.setColumnAlignment("selected", Table.Align.CENTER);
        this.setColumnWidth("selected", 70);
        this.setColumnWidth("Index", 47);
        this.setColumnWidth("Accession", 87);
        this.setColumnWidth("Name", 187);
        availableProteinLayoutWidth = width-71-47-87-187-4;
         this.setColumnWidth("Comparisons Overview", availableProteinLayoutWidth);

        this.addHeaderClickListener((Table.HeaderClickEvent event) -> {
            if (event.getPropertyId().toString().equalsIgnoreCase("selected")) {
                this.removeAllItems();
                if (showSelectedOnly) {
                    showSelectedOnly = false;
                    this.setColumnIcon("selected", checkedRes);
                    for (Object itemId : tableItemsMap.keySet()) {
                        this.addItem(tableItemsMap.get(itemId), itemId);
                    }

                } else {
                    showSelectedOnly = true;
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
        });

        this.addColumnResizeListener((Table.ColumnResizeEvent event) -> {
            this.setColumnWidth(event.getPropertyId(), event.getPreviousWidth());
        });

    }

    public void updateTableData(Set<QuantDiseaseGroupsComparison> selectedComparisonsList, Set<QuantComparisonProtein> selectedProteinsList) {
        
        this.setHeight(100, Unit.PERCENTAGE);
        tableItemsMap.clear();
        this.removeAllItems();
        if(selectedProteinsList==null)
            return;
//          ExternalLink link = new ExternalLink("google", new ExternalResource("www.google.com"));
//        link.setTargetName("_blank");
//        link.setPrimaryStyleName("tablelink");
//        
//        Link link2 = new Link("google", new ExternalResource("www.google.com"));
//        link2.setTargetName("_blank");
//        link2.setPrimaryStyleName("tablelink");
//        
//        tableItemsMap.put(4, new Object[]{1, link, "Yehia Farag", new ProteinTrendLayout()});
//        tableItemsMap.put(5, new Object[]{2, link2, "Yehia Farag", new ProteinTrendLayout()});
//        quantProteinTable.addItem(tableItemsMap.get(4), 4);
//        quantProteinTable.addItem(tableItemsMap.get(5), 5);
        int protId=0;
        for (QuantComparisonProtein protein : selectedProteinsList) {
            String accession = protein.getProteinAccession();
            String name = protein.getProteinName();
            String url = protein.getUrl();
            if(url==null)
                url="";
            ExternalLink accessionObject = new ExternalLink(accession, new ExternalResource(url));
            ExternalLink nameObject = new ExternalLink(name, new ExternalResource(url));
            ProteinTrendLayout protTrendLayout = new ProteinTrendLayout(selectedComparisonsList,protein,availableProteinLayoutWidth);
            tableItemsMap.put(protId, new Object[]{protId+1, accessionObject, nameObject, protTrendLayout});
            this.addItem(tableItemsMap.get(protId),protId);
            protId++;

        }

    }

}
