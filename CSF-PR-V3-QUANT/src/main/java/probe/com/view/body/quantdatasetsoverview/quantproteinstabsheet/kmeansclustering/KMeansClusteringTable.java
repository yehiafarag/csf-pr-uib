package probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.kmeansclustering;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MultiSelectMode;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import java.util.Set;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.DiseaseGroupsComparisonsProteinLayout;
import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.ProteinOverviewJFreeLineChartContainer;
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
//        if (proteinLabel != null) {
//            proteinLabel.rePaintLable("black");
//        }
        final Item item = this.getItem(proteinskey);
        proteinLabel = (CustomExternalLink) item.getItemProperty("Accession").getValue();
        proteinLabel.rePaintLable("white");
    }
    private final Button.ClickListener btnClickListener;

    public KMeansClusteringTable(Set<String> protSelectionMap) {

        this.setSelectable(true);
        this.setColumnReorderingAllowed(true);
        this.setColumnCollapsingAllowed(true);
        this.setImmediate(true); // react at once when something is selected
        setWidth("350px");
        setHeight("483px");
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
                HorizontalLayout protBodyLayout = generateProteinLayout(key, proteinNamenLabel, proteinAccsessionLabel, null, null);
                int height = Page.getCurrent().getBrowserWindowHeight() - 200;
                int pageWidth = Page.getCurrent().getBrowserWindowWidth()-200;

                final Window popupWindow = new Window() {

                    @Override
                    public void close() {
                        this.setVisible(false);
                    }

                };
                popupWindow.setContent(protBodyLayout);
                popupWindow.setWindowMode(WindowMode.NORMAL);
                popupWindow.setWidth((pageWidth) + "px");
                popupWindow.setHeight((height) + "px");
                popupWindow.setVisible(true);
                popupWindow.setResizable(false);
                popupWindow.setClosable(false);
                popupWindow.setStyleName(Reindeer.WINDOW_LIGHT);
                popupWindow.setModal(true);
                popupWindow.setDraggable(false);

                UI.getCurrent().addWindow(popupWindow);
                popupWindow.center();

                popupWindow.setCaptionAsHtml(true);
                popupWindow.setClosable(true);


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

    /**
     * initialize and generate quant proteins tab
     *
     * @param quantProteinName protein name
     * @param quantProteinAccession
     * @param diseaseGroupsComparisonsProteinArray
     * @param selectedDiseaseGroupsComparisonsList disease groups comparisons
     * list in case of searching mode
     */
    private HorizontalLayout generateProteinLayout(String proteinKey, String quantProteinName, String quantProteinAccession, DiseaseGroupsComparisonsProteinLayout[] diseaseGroupsComparisonsProteinArray, Set<QuantDiseaseGroupsComparison> selectedDiseaseGroupsComparisonsList) {
        HorizontalLayout bodyLayout = new HorizontalLayout();
//        Page page = Page.getCurrent();
//        int pageWidth = page.getBrowserWindowWidth();
        bodyLayout.setWidthUndefined();
        bodyLayout.setSpacing(true);
        bodyLayout.setMargin(true);
        bodyLayout.setHeightUndefined();
        bodyLayout.setStyleName(Reindeer.LAYOUT_BLUE);

//        ProteinOverviewJFreeLineChartContainer overallPlotLayout = new ProteinOverviewJFreeLineChartContainer(datasetExploringCentralSelectionManager, diseaseGroupsComparisonsProteinArray, selectedDiseaseGroupsComparisonsList, (pageWidth), quantProteinName, quantProteinAccession, false, proteinKey, mainHandler);
//        bodyLayout.addComponent(overallPlotLayout);
//        bodyLayout.setComponentAlignment(overallPlotLayout, Alignment.TOP_CENTER);
        return bodyLayout;

    }

}
