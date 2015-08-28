package probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.kmeansclustering;

import com.vaadin.shared.ui.MultiSelectMode;
import com.vaadin.ui.Table;
import java.util.Set;
import probe.com.view.core.CustomExternalLink;

/**
 *
 * @author Yehia Farag
 */
public class KMeansClusteringTable extends Table {

    public KMeansClusteringTable(Set<String> protSelectionMap) {

        this.setSelectable(true);
     
        this.setColumnReorderingAllowed(true);
        this.setColumnCollapsingAllowed(true);
        this.setImmediate(true); // react at once when something is selected
        setWidth("100%");
        setHeight("300px");
//        this.setStyleName(Reindeer.TABLE_BORDERLESS);
//        addValueChangeListener(QuantProteinsComparisonsContainer.this);
        setMultiSelect(true);
        setMultiSelectMode(MultiSelectMode.DEFAULT);

        this.addContainerProperty("Index", Integer.class, null, "", null, Table.Align.RIGHT);
        this.addContainerProperty("Accession", CustomExternalLink.class, null, "Accession", null, Table.Align.LEFT);
        this.addContainerProperty("Name", String.class, null, "Name", null, Table.Align.LEFT);

//        this.setColumnWidth("Index", 47);
        this.setColumnWidth("Accession", 87);
   
//        this.setColumnWidth("Name", 187);
        int i = 0;
        for (String protKey : protSelectionMap) {

            String protAcc = protKey.replace("--", "").trim().split(",")[0];
            String protName = protKey.replace("--", "").trim().split(",")[1];
            
            CustomExternalLink acc = new CustomExternalLink(protAcc.toUpperCase(), "http://www.uniprot.org/uniprot/" + protAcc.toUpperCase());
            acc.setDescription("UniProt link for " + protAcc.toUpperCase());
            this.addItem(new Object[]{i,acc,protName}, i++);
        }

    }

}
