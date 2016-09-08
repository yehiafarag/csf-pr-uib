/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.components.linechartproteintablecomponents;

import com.vaadin.data.Item;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantComparisonProtein;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.view.core.ColumnHeaderLayout;
import no.uib.probe.csf.pr.touch.view.core.ExternalLink;
import no.uib.probe.csf.pr.touch.view.core.ProteinTrendLayout;
import no.uib.probe.csf.pr.touch.view.core.RadioButton;
import no.uib.probe.csf.pr.touch.view.core.TrendSymbol;

/**
 *
 * @author Yehia Farag
 *
 * this table is made for exporting data
 */
public class ExportProteinTable extends VerticalLayout {

    private Table exportTable;

    public void setUserCustomizedComparison(QuantDiseaseGroupsComparison userCustomizedComparison) {
        this.userCustomizedComparison = userCustomizedComparison;
    }

    public ExportProteinTable() {
        this.setVisible(false);

    }

    public Table getExportTable() {
        return exportTable;
    }
    private QuantDiseaseGroupsComparison userCustomizedComparison;

    public void updateTableData(Set<QuantDiseaseGroupsComparison> selectedComparisonsList, Set<QuantComparisonProtein> selectedProteinsList,String sortingHeader,boolean upSort) {
        this.removeAllComponents();
        exportTable = new Table();
        this.addComponent(exportTable);
        exportTable.addContainerProperty("Index", Integer.class, null, "", null, Table.Align.RIGHT);
        exportTable.addContainerProperty("Accession", String.class, null, "Accession", null, Table.Align.LEFT);
        exportTable.addContainerProperty("Name", String.class, null, "Name", null, Table.Align.LEFT);

        Object[] objects;
        int i = 0;
        if (userCustomizedComparison != null) {
            objects = new Object[4 + selectedComparisonsList.size()];
            exportTable.addContainerProperty("userdata", Double.class, null, "User Data", null, Table.Align.CENTER);
        } else {
            objects = new Object[3 + selectedComparisonsList.size()];
        }
        for (QuantDiseaseGroupsComparison quantComp : selectedComparisonsList) {
            String compName = quantComp.getComparisonHeader().split(" / ")[0].split("__")[0]+" / "+quantComp.getComparisonHeader().split(" / ")[1].split("__")[0]+" ("+quantComp.getDiseaseCategory()+")";
            exportTable.addContainerProperty(quantComp.getComparisonHeader(), Double.class, null, compName, null, Table.Align.CENTER);

        }

        int protId = 0;
        for (QuantComparisonProtein protein : selectedProteinsList) {
            String accession = protein.getProteinAccession();//.replace("(unreviewed)", " (Unreviewed)");
            String name = protein.getProteinName();
            objects[1] = accession;
            objects[2] = name;

            if (userCustomizedComparison != null) {
                i = 4;
                if (userCustomizedComparison.getQuantComparisonProteinMap().containsKey(accession)) {
                    objects[3] = (userCustomizedComparison.getQuantComparisonProteinMap().get(accession).getOverallCellPercentValue());
                }

            } else {
                i = 3;
            }
            for (QuantDiseaseGroupsComparison quantComp : selectedComparisonsList) {
                  String key = "";
                    if (quantComp.getQuantComparisonProteinMap().containsKey("0_" + accession)) {
                        key = "0_" + accession;
                    } else if (quantComp.getQuantComparisonProteinMap().containsKey("1_" + accession)) {
                        key = "1_" + accession;
                    } else if (quantComp.getQuantComparisonProteinMap().containsKey("2_" + accession)) {
                        key = "2_" + accession;

                    } else if (quantComp.getQuantComparisonProteinMap().containsKey("3_" + accession)) {

                        key = "3_" + accession;
                    } else if (quantComp.getQuantComparisonProteinMap().containsKey("4_" + accession)) //                                    if (quantComp.getQuantComparisonProteinMap().containsValue(protien.getProtKey())) {
                    {
                        key = "4_" + accession;

                    }
                
                
                
                if (quantComp.getQuantComparisonProteinMap().containsKey(key)) {
                    objects[i] = quantComp.getQuantComparisonProteinMap().get(key).getOverallCellPercentValue();
                }
                i++;

            }
            objects[0]= protId+1;
            exportTable.addItem(objects, protId);
            protId++;

        }
        this.exportTable.sort(new String[]{sortingHeader}, new boolean[]{upSort});
        int indexing = 1;
        for (Object id
                : exportTable.getItemIds()) {
            Item item = exportTable.getItem(id);
            item.getItemProperty("Index").setValue(indexing);
            indexing++;
        }
      

    }

}
