package probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.kmeansclustering;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.shared.ui.MultiSelectMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.Reindeer;
import java.util.Set;
import probe.com.selectionmanager.QuantCentralManager;
import probe.com.view.core.CustomExternalLink;

/**
 *
 * @author Yehia Farag
 */
public class KMeansClusteringTable extends Table implements Property.ValueChangeListener {

    private CustomExternalLink proteinLabel;
    private int proteinskey;

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        if (proteinLabel != null) {
            proteinLabel.rePaintLable("black");
        }
        if (this.getValue() != null) {
            proteinskey = (Integer) this.getValue();
        } else {
            return;
        }
        final Item item = this.getItem(proteinskey);
        proteinLabel = (CustomExternalLink) item.getItemProperty("Accession").getValue();
        proteinLabel.rePaintLable("white");
    }
    private final Button.ClickListener btnClickListener;

    public KMeansClusteringTable(final QuantCentralManager  Quant_Central_Manager,Set<String> protSelectionMap,int tableWidth,int tableHeight) {

        this.setSelectable(true);
        this.setColumnReorderingAllowed(true);
        this.setColumnCollapsingAllowed(true);
        this.setImmediate(true); // react at once when something is selected
        setWidth(tableWidth+"px");
        setHeight(tableHeight+"px");
        setMultiSelect(false);
        setMultiSelectMode(MultiSelectMode.DEFAULT);

        btnClickListener = new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                int protIndex = (Integer) event.getButton().getData();
                final Item item = getItem(protIndex);
                String proteinAccsessionLabel = item.getItemProperty("Accession").getValue().toString();
                String proteinNamenLabel = item.getItemProperty("Name").getValue().toString();
                String key = ("--" + proteinAccsessionLabel.toLowerCase().trim() + "," + proteinNamenLabel.toLowerCase().trim()).toLowerCase().trim();
                Quant_Central_Manager.setSelectedProteinKey(key);
                event.getButton().getParent().getParent().getParent().getParent().getParent().setVisible(false);
 
            }
        };

        this.addContainerProperty("Index", Integer.class, null, "", null, Table.Align.RIGHT);
        this.addContainerProperty("Accession", CustomExternalLink.class, null, "Accession", null, Table.Align.LEFT);
        this.addContainerProperty("Name", String.class, null, "Name", null, Table.Align.LEFT);
        this.addContainerProperty("Load", Button.class, null, "", null, Table.Align.CENTER);
        
        
//        this.setColumnExpandRatio(proteinskey, SIZE_UNDEFINED);

        int i = 0;
        for (String protKey : protSelectionMap) {
            String protAcc = protKey.replace("--", "").trim().split(",")[0];
            String protName = protKey.replace("--", "").trim().split(",")[1];

            CustomExternalLink acc = new CustomExternalLink(protAcc.toUpperCase(), "http://www.uniprot.org/uniprot/" + protAcc.toUpperCase());
            acc.setDescription("UniProt link for " + protAcc.toUpperCase());
            Button btn = new Button("Load");
            btn.setStyleName(Reindeer.BUTTON_SMALL);
            btn.setData(i);
            btn.addClickListener(btnClickListener);
            this.addItem(new Object[]{i, acc, protName, btn}, i++);
            this.addValueChangeListener(KMeansClusteringTable.this);
        }
        this.setColumnExpandRatio("Accession", 87);
        this.setColumnExpandRatio("Load",60);
        this.setColumnExpandRatio("Index", 40);
        this.setColumnExpandRatio("Name", 100);

    }
}
