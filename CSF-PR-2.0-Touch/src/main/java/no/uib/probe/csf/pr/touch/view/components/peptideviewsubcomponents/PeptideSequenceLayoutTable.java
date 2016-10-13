/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.components.peptideviewsubcomponents;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.CheckBox;
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
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.logic.beans.QuantPeptide;
import no.uib.probe.csf.pr.touch.logic.beans.QuantProtein;
import no.uib.probe.csf.pr.touch.view.core.ExternalLink;
import no.uib.probe.csf.pr.touch.view.core.ComparisonLable;
import no.uib.probe.csf.pr.touch.view.core.TrendSymbol;

/**
 *
 * @author Yehia Farag
 *
 * this class represents peptides table sequence table the component is part of
 * peptide view tab
 */
public class PeptideSequenceLayoutTable extends VerticalLayout {

    private final Table peptideSequenceTable;
    private final Set<Object> selectedItemIds = new HashSet<>();
    private final Map<Object, CheckBox> tableItemscheckboxMap;
    private boolean selectedOnly = false;
    private final Map<Object, Object[]> tableItemsMap;
    private final Map<String, Object> comparisonToItemId;
    private final Set<ProteinSequenceContainer> proteinSeqSet;
    int proteinSequenceContainerWidth;


    public final void hideCheckedColumn(boolean hide) {

//        this.hideCheckedColumn = hide;
//        peptideSequenceTable.setColumnCollapsed("emptyselection", !hide);
//        peptideSequenceTable.setColumnCollapsed("selected", hide);
//        peptideSequenceTable.markAsDirtyRecursive();
//        peptideSequenceTable.setWidth(100, Unit.PERCENTAGE);
//         peptideSequenceTable.setWidthUndefined();
    }

    public int getRowsNumber() {
        return this.peptideSequenceTable.getItemIds().size();
    }

    private final boolean smallScreen;

    public PeptideSequenceLayoutTable(int width, int height, boolean smallScreen) {
//        height-=20;
        this.setWidth(width, Unit.PIXELS);
        this.setHeight(height, Unit.PIXELS);
        this.smallScreen = smallScreen;
//        this.addStyleName("paddingtop20");

        tableItemscheckboxMap = new HashMap<>();
        this.tableItemsMap = new LinkedHashMap<>();
        comparisonToItemId = new HashMap<>();
        proteinSeqSet = new HashSet<>();

        peptideSequenceTable = new Table();
        peptideSequenceTable.setWidthUndefined();
        peptideSequenceTable.setStyleName(ValoTheme.TABLE_COMPACT);
        this.peptideSequenceTable.addStyleName("peptidetablestyle");
        peptideSequenceTable.setHeight(height, Unit.PIXELS);
        this.addComponent(peptideSequenceTable);
        peptideSequenceTable.addStyleName("smallicons");
        this.peptideSequenceTable.addStyleName(ValoTheme.TABLE_SMALL);

        peptideSequenceTable.setSelectable(false);
        peptideSequenceTable.setSortEnabled(false);
        peptideSequenceTable.setColumnReorderingAllowed(false);
        peptideSequenceTable.setColumnCollapsingAllowed(true);
        peptideSequenceTable.setImmediate(true); // react at once when something is selected
        peptideSequenceTable.setMultiSelect(false);

        peptideSequenceTable.addContainerProperty("Index", Integer.class, null, "", null, Table.Align.RIGHT);
        peptideSequenceTable.addContainerProperty("trend", TrendSymbol.class, null, "", null, Table.Align.CENTER);
        peptideSequenceTable.addContainerProperty("Comparison", ComparisonLable.class, null, "Comparison", null, Table.Align.LEFT);
        peptideSequenceTable.addContainerProperty("Publication", ExternalLink.class, null, "Publication", null, Table.Align.LEFT);

        peptideSequenceTable.addContainerProperty("patientsNumber", Integer.class, null, "#Patients",new ThemeResource("img/p-numb.png"), Table.Align.RIGHT);
        peptideSequenceTable.addContainerProperty("PeptideSequence", ProteinSequenceContainer.class, null, "Protein Coverage", null, Table.Align.CENTER);
//        peptideSequenceTable.addContainerProperty("emptyselection", String.class, null, "", null, Table.Align.LEFT);
//        peptideSequenceTable.setColumnCollapsed("emptyselection", false);
//        /* This checkbox reflects the contents of the selectedItemIds set */
//        peptideSequenceTable.addGeneratedColumn("selected", (Table source, final Object itemId, Object columnId) -> {
//            boolean selected = selectedItemIds.contains(itemId);
//            /* When the chekboc value changes, add/remove the itemId from the selectedItemIds set */
//            final CheckBox cb = new CheckBox("", selected);
//            tableItemscheckboxMap.put(itemId, cb);
//            cb.addValueChangeListener((Property.ValueChangeEvent event) -> {
//                if (selectedOnly) {
//                    cb.setValue(true);
//                    return;
//                }
//                if (selectedItemIds.contains(itemId)) {
//                    selectedItemIds.remove(itemId);
//                } else {
//                    selectedItemIds.add(itemId);
//                }
//            });
//            return cb;
//        });

//        peptideSequenceTable.setColumnIcon("selected", showSelectedeRes);
//        peptideSequenceTable.setColumnHeader("selected", " Click to show checked rows only");
//        peptideSequenceTable.setColumnAlignment("selected", Table.Align.CENTER);
//        peptideSequenceTable.setColumnWidth("selected", 47);
//        peptideSequenceTable.setColumnCollapsed("selected", true);
//        peptideSequenceTable.setColumnWidth("emptyselection", 47);
        peptideSequenceTable.setColumnWidth("trend", 47);
        peptideSequenceTable.setColumnWidth("Index", 47);

        peptideSequenceTable.setColumnWidth("patientsNumber", 47);
        peptideSequenceTable.setColumnWidth("Comparison", 100);
        peptideSequenceTable.setColumnWidth("Publication", 175);
        proteinSequenceContainerWidth = width - 47 - 47 - 47 - 87 - 187 - 10 - 25;//- 47
        peptideSequenceTable.setColumnWidth("PeptideSequence", proteinSequenceContainerWidth);
        peptideSequenceTable.addHeaderClickListener((Table.HeaderClickEvent event) -> {
            if (event.getPropertyId().toString().equalsIgnoreCase("selected")) {
                if (selectedItemIds.isEmpty()) {
                    return;
                }
                selectedOnly = !selectedOnly;
                showSelectedOnly();

            }
        });

        peptideSequenceTable.addColumnResizeListener((Table.ColumnResizeEvent event) -> {
            peptideSequenceTable.setColumnWidth(event.getPropertyId(), event.getPreviousWidth());
        });
    }

    private void showSelectedOnly() {
        peptideSequenceTable.removeAllItems();
//        peptideSequenceTable.setColumnCollapsed("emptyselection", true);
        if (!selectedOnly) {
//            peptideSequenceTable.setColumnHeader("selected", " Click to show checked rows only");
//            peptideSequenceTable.setColumnIcon("selected", showSelectedeRes);

            for (Object itemId : tableItemsMap.keySet()) {
                peptideSequenceTable.addItem(tableItemsMap.get(itemId), itemId);
            }

        } else {
//            peptideSequenceTable.setColumnHeader("selected", " Click to show all rows");
//            peptideSequenceTable.setColumnIcon("selected", showAllRes);
            if (!selectedItemIds.isEmpty()) {
                for (Object itemId : selectedItemIds) {
                    peptideSequenceTable.addItem(tableItemsMap.get(itemId), itemId);
                }
            } else {
                for (Object itemId : tableItemsMap.keySet()) {
                    peptideSequenceTable.addItem(tableItemsMap.get(itemId), itemId);
                }
            }

        }
//        peptideSequenceTable.setColumnWidth("selected", 47);

    }

    private String sequence = "";
    private final Set<String> peptides = new LinkedHashSet<>();

    public void updateTableData(Set<QuantDiseaseGroupsComparison> selectedComparisonsList, String proteinKey) {
        peptideSequenceTable.removeAllItems();
        tableItemsMap.clear();
        proteinSeqSet.clear();
        peptides.clear();
        int objectId = 0;
        sequence = "";
        String keyI = 0 + "_" + proteinKey;
        String keyII = 1 + "_" + proteinKey;
        String keyIII = 2 + "_" + proteinKey;

        String keyIV = 3 + "_" + proteinKey;
        String keyV = 4 + "_" + proteinKey;

        String key;

        for (QuantDiseaseGroupsComparison comparison : selectedComparisonsList) {
            key = "";
            if (comparison.getQuantComparisonProteinMap().containsKey(keyI)) {
                key = keyI;

            } else if (comparison.getQuantComparisonProteinMap().containsKey(keyII)) {
                key = keyII;

            } else if (comparison.getQuantComparisonProteinMap().containsKey(keyIII)) {
                key = keyIII;

            } else if (comparison.getQuantComparisonProteinMap().containsKey(keyIV)) {
                key = keyIV;

            } else if (comparison.getQuantComparisonProteinMap().containsKey(keyV)) {
                key = keyV;

            }
            if (!comparison.getQuantComparisonProteinMap().containsKey(key)) {

                continue;
            }

            QuantComparisonProtein protein = comparison.getQuantComparisonProteinMap().get(key);
            if (sequence == null || sequence.equalsIgnoreCase("")) {
                sequence = protein.getSequence().trim();
            }
            for (QuantPeptide pep : protein.getQuantPeptidesList()) {
                peptides.add(pep.getPeptideSequence());
            }

            if (sequence.equalsIgnoreCase("") && !peptides.isEmpty()) {
                Object[] pepStrObjArr = peptides.toArray();
                for (Object pepStrObjArr1 : pepStrObjArr) {
                    String a = (String) pepStrObjArr1;
                    for (Object pepStrObjArr2 : pepStrObjArr) {
                        String b = (String) pepStrObjArr2;
                        if (!a.equalsIgnoreCase(b) && a.contains(b)) {
                            peptides.remove(b);
                        } else if (!a.equalsIgnoreCase(b) && b.contains(a)) {
                            peptides.remove(a);
                        }
                    }
                }
                sequence = "ZZZZZZZZ" + peptides.toString().replace("[", "").replace(" ", "").replace(",", "").replace("]", "") + "ZZZZZZZZ";

            }

            for (QuantProtein quantProt : protein.getDsQuantProteinsMap().values()) {

                int trend = 2;
                if (quantProt.getStringPValue().equalsIgnoreCase("Significant")) {
                    if (quantProt.getStringFCValue().equalsIgnoreCase("Increased")) {
                        trend = 0;
                    } else if (quantProt.getStringFCValue().equalsIgnoreCase("Decreased")) {
                        trend = 4;
                    }

                }

                TrendSymbol symbol = new TrendSymbol(trend);
                symbol.addStyleName("pointer");
                symbol.setWidth(12, Unit.PIXELS);
                symbol.setHeight(12, Unit.PIXELS);

                QuantDatasetObject ds = comparison.getDatasetMap().get(quantProt.getDsKey());
                String title = "<font size='2'>" + ds.getAuthor() + "<br/>(" + ds.getYear() + ")</font> ";
                ExternalLink publicationLink = new ExternalLink(title, new ExternalResource("http://www.ncbi.nlm.nih.gov/pubmed/" + ds.getPumedID()));
                publicationLink.setDescription("View publication " + title);
                publicationLink.setWidth(100, Unit.PERCENTAGE);
                publicationLink.setCaptionAsHtml(true);
                publicationLink.addStyleName(ValoTheme.LABEL_SMALL);
                publicationLink.addStyleName(ValoTheme.LABEL_TINY);
                publicationLink.addStyleName("overflowtext");

                ComparisonLable comparisonLabelObject = new ComparisonLable(comparison, objectId, quantProt, ds, smallScreen) {

                    @Override
                    public void select(Object itemId) {
                        if (peptideSequenceTable.getValue() == itemId) {
                            peptideSequenceTable.unselect(itemId);
                        } else {
                            peptideSequenceTable.select(itemId);
                        }
                    }

                };
                symbol.addLayoutClickListener((LayoutEvents.LayoutClickEvent event) -> {
                    comparisonLabelObject.openComparisonPopup();
                });
                symbol.setDescription(comparisonLabelObject.getDescription());

                String protName = quantProt.getUniprotProteinName().trim();
                if (protName.equalsIgnoreCase("")) {
                    protName = quantProt.getPublicationProteinName();
                }
                ProteinSequenceContainer proteinCoverageLayout = new ProteinSequenceContainer(sequence, protein.getQuantPeptidesList(), proteinSequenceContainerWidth, quantProt.getDsKey(), smallScreen, protName);
                proteinSeqSet.add(proteinCoverageLayout);
                tableItemsMap.put(objectId, new Object[]{objectId + 1, symbol, comparisonLabelObject, publicationLink, ds.getPatientsGroup1Number() + ds.getPatientsGroup2Number(), proteinCoverageLayout});
                peptideSequenceTable.addItem(tableItemsMap.get(objectId), objectId);
                comparisonToItemId.put(comparison.getComparisonHeader() + "__" + quantProt.getDsKey(), objectId);
                objectId++;

            }

//           
        }
        hideCheckedColumn(true);

        int indexing = 1;
        for (Object id : peptideSequenceTable.getItemIds()) {
            Item item = peptideSequenceTable.getItem(id);
            item.getItemProperty("Index").setValue(indexing);
            indexing++;
        }

    }

    public Set<ProteinSequenceContainer> getProteinSeqSet() {
        return proteinSeqSet;
    }

    public void sortTable(Set<QuantDiseaseGroupsComparison> selectedComparisonsList) {
        peptideSequenceTable.removeAllItems();
        Map<Object, Object[]> sortedTableItemsMap = new LinkedHashMap<>();
        for (QuantDiseaseGroupsComparison comparison : selectedComparisonsList) {
            for (QuantDatasetObject ds : comparison.getDatasetMap().values()) {
                if (comparisonToItemId.containsKey(comparison.getComparisonHeader() + "__" + ds.getDsKey())) {
                    Object itemId = comparisonToItemId.get(comparison.getComparisonHeader() + "__" + ds.getDsKey());
                    if (tableItemsMap.containsKey(itemId)) {
                        peptideSequenceTable.addItem(tableItemsMap.get(itemId), itemId);
                        sortedTableItemsMap.put(itemId, tableItemsMap.get(itemId));
                    }

                }
            }

        }
        tableItemsMap.clear();
        tableItemsMap.putAll(sortedTableItemsMap);
        selectedItemIds.clear();
        selectedOnly = false;
        showSelectedOnly();
        int indexing = 1;
        for (Object id : peptideSequenceTable.getItemIds()) {
            Item item = peptideSequenceTable.getItem(id);
            item.getItemProperty("Index").setValue(indexing);
            indexing++;
        }
//        
    }

    public void select(QuantDiseaseGroupsComparison comparison, int dsKey) {
        selectedItemIds.clear();
        if (comparison == null) {
            selectedOnly = false;
            showSelectedOnly();
            return;
        }

        if (dsKey == -100) {
            comparison.getDatasetMap().keySet().stream().filter((key) -> (comparisonToItemId.containsKey(comparison.getComparisonHeader() + "__" + key))).forEach((key) -> {
                selectedItemIds.add(comparisonToItemId.get(comparison.getComparisonHeader() + "__" + key));
            });

        } else {
            if (comparisonToItemId.containsKey(comparison.getComparisonHeader() + "__" + dsKey)) {
                selectedItemIds.add(comparisonToItemId.get(comparison.getComparisonHeader() + "__" + dsKey));
            }

        }
        selectedOnly = !selectedItemIds.isEmpty();
        showSelectedOnly();
        int indexing = 1;
        for (Object id : peptideSequenceTable.getItemIds()) {
            Item item = peptideSequenceTable.getItem(id);
            item.getItemProperty("Index").setValue(indexing);
            indexing++;
        }
    }

    public void showNotSignificantPeptides(boolean showNotSigPeptides) {

        proteinSeqSet.stream().forEach((proteinCoverageLayout) -> {
            proteinCoverageLayout.setShowNotSignificantPeptides(showNotSigPeptides);
        });

    }

    public String getSequence() {
        return sequence;
    }

    public Set<String> getPeptides() {
        return peptides;
    }

}
