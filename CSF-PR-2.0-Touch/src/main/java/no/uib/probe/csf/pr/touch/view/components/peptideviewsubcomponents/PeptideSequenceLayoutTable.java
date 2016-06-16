/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.components.peptideviewsubcomponents;

import com.vaadin.data.Property;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantComparisonProtein;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
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

    public PeptideSequenceLayoutTable(int width, int height) {
        this.setWidth(width, Unit.PIXELS);
        this.setHeight(height, Unit.PIXELS);

        tableItemscheckboxMap = new HashMap<>();
        this.tableItemsMap = new LinkedHashMap<>();
        comparisonToItemId = new HashMap<>();
        proteinSeqSet = new HashSet<>();

        peptideSequenceTable = new Table();
        peptideSequenceTable.setWidthUndefined();
        peptideSequenceTable.setHeight(100, Unit.PERCENTAGE);
        this.addComponent(peptideSequenceTable);

        peptideSequenceTable.setSelectable(true);
        peptideSequenceTable.setSortEnabled(false);
        peptideSequenceTable.setColumnReorderingAllowed(false);
        peptideSequenceTable.setColumnCollapsingAllowed(false);
        peptideSequenceTable.setImmediate(true); // react at once when something is selected
        peptideSequenceTable.setMultiSelect(false);

        peptideSequenceTable.addContainerProperty("Index", TrendSymbol.class, null, "", null, Table.Align.CENTER);
        peptideSequenceTable.addContainerProperty("Comparison", ComparisonLable.class, null, "Comparison", null, Table.Align.LEFT);
        peptideSequenceTable.addContainerProperty("Publication", ExternalLink.class, null, "Publication", null, Table.Align.LEFT);
        peptideSequenceTable.addContainerProperty("PeptideSequence", ProteinSequenceContainer.class, null, "Peptide Sequence", null, Table.Align.CENTER);

        /* This checkbox reflects the contents of the selectedItemIds set */
        peptideSequenceTable.addGeneratedColumn("selected", (Table source, final Object itemId, Object columnId) -> {
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
        peptideSequenceTable.setColumnHeader("selected", "Only Checked");
        peptideSequenceTable.setColumnAlignment("selected", Table.Align.CENTER);
        peptideSequenceTable.setColumnWidth("selected", 70);
        peptideSequenceTable.setColumnWidth("Index", 47);
        peptideSequenceTable.setColumnWidth("Comparison", 100);
        peptideSequenceTable.setColumnWidth("Publication", 175);
        proteinSequenceContainerWidth = width - 71 - 47 - 87 - 187 - 10;
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

//        peptideSequenceTable.sort(new String[]{"PeptideSequence"}, new boolean[]{false});
//        peptideSequenceTable.setSortAscending(false);
    }

    private void showSelectedOnly() {
        peptideSequenceTable.removeAllItems();
        if (!selectedOnly) {
            peptideSequenceTable.setColumnHeader("selected", "Checked");
            for (Object itemId : tableItemsMap.keySet()) {
                peptideSequenceTable.addItem(tableItemsMap.get(itemId), itemId);
            }

        } else {
            peptideSequenceTable.setColumnHeader("selected", "All");
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

    }

    public void updateTableData(Set<QuantDiseaseGroupsComparison> selectedComparisonsList, String proteinKey) {
        peptideSequenceTable.removeAllItems();
        tableItemsMap.clear();
        proteinSeqSet.clear();
        int objectId = 0;
        String keyI = 0 + "_" + proteinKey;
        String keyII = 1 + "_" + proteinKey;
        String keyIII = 2 + "_" + proteinKey;
        String key;

        for (QuantDiseaseGroupsComparison comparison : selectedComparisonsList) {
            key = "";
            if (comparison.getQuantComparisonProteinMap().containsKey(keyI)) {
                key = keyI;

            } else if (comparison.getQuantComparisonProteinMap().containsKey(keyII)) {
                key = keyII;

            } else if (comparison.getQuantComparisonProteinMap().containsKey(keyIII)) {
                key = keyIII;

            }
            if (!comparison.getQuantComparisonProteinMap().containsKey(key)) {

                continue;
            }

            QuantComparisonProtein protein = comparison.getQuantComparisonProteinMap().get(key);

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
                symbol.setWidth(12, Unit.PIXELS);
                symbol.setHeight(12, Unit.PIXELS);
                QuantDatasetObject ds = comparison.getDatasetMap().get(quantProt.getDsKey());
                String title = ds.getAuthor() + "<br/>(" + ds.getYear() + ") ";
                ExternalLink publicationLink = new ExternalLink(title, new ExternalResource("http://www.ncbi.nlm.nih.gov/pubmed/" + ds.getPumedID()));
                publicationLink.setDescription(title);
                publicationLink.setWidth(100, Unit.PERCENTAGE);
                publicationLink.setCaptionAsHtml(true);
                publicationLink.addStyleName(ValoTheme.LABEL_SMALL);
                publicationLink.addStyleName(ValoTheme.LABEL_TINY);
                publicationLink.addStyleName("overflowtext");

                ComparisonLable comparisonLabelObject = new ComparisonLable(comparison, objectId) {

                    @Override
                    public void select(Object itemId) {
                        if (peptideSequenceTable.getValue() == itemId) {
                            peptideSequenceTable.unselect(itemId);
                        } else {
                            peptideSequenceTable.select(itemId);
                        }
                    }

                };

                ProteinSequenceContainer proteinCoverageLayout = new ProteinSequenceContainer(quantProt.getSequence(), protein.getQuantPeptidesList(), proteinSequenceContainerWidth, quantProt.getDsKey());
                proteinSeqSet.add(proteinCoverageLayout);
                tableItemsMap.put(objectId, new Object[]{symbol, comparisonLabelObject, publicationLink, proteinCoverageLayout});
                peptideSequenceTable.addItem(tableItemsMap.get(objectId), objectId);
                comparisonToItemId.put(comparison.getComparisonHeader() + "__" + quantProt.getDsKey(), objectId);
                objectId++;

            }

//           
        }

    }

    public void sortTable(Set<QuantDiseaseGroupsComparison> selectedComparisonsList) {
        peptideSequenceTable.removeAllItems();
        for (QuantDiseaseGroupsComparison comparison : selectedComparisonsList) {
            for (int key : comparison.getDatasetMap().keySet()) {
                if (comparisonToItemId.containsKey(comparison.getComparisonHeader() + "__" + key)) {
                    Object itemId = comparisonToItemId.get(comparison.getComparisonHeader() + "__" + key);
                    peptideSequenceTable.addItem(tableItemsMap.get(itemId), itemId);
                }
            }

        }
        selectedItemIds.clear();
        selectedOnly = false;
        showSelectedOnly();

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
    }

    public void showNotSignificantPeptides(boolean showNotSigPeptides) {

        proteinSeqSet.stream().forEach((proteinCoverageLayout) -> {
            proteinCoverageLayout.setShowNotSignificantPeptides(showNotSigPeptides);
        });

    }

}
