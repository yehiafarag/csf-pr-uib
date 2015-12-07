package probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.kmeansclustering;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.Map;
import java.util.Set;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
import probe.com.selectionmanager.QuantCentralManager;
import probe.com.selectionmanager.StudiesSelectionManager;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.DiseaseGroupsComparisonsProteinLayout;

/**
 *
 * @author Yehia Farag
 *
 * k-means clustering layout for selected protein
 */
public class ProteinKMeansClusterLayout extends VerticalLayout implements Property.ValueChangeListener {

//    private final Label kMeansClusteringTitle;
//    private final VerticalLayout closeBtn;
    private final KMeansClusteringLineChart proteinKMeansClusteringLineChart;
    private final int imgWidth;
    private final KMeansClusteringTable kMeansClusteringTable;
    
    public ProteinKMeansClusterLayout(QuantCentralManager Quant_Central_Manager,String proteinKey, String proteinName, String proteinAccession, int width, int height, Map<String, DiseaseGroupsComparisonsProteinLayout[]> protSelectionMap, Set<QuantDiseaseGroupsComparison> selectedComparisonList) {
        
        this.setSpacing(true);
        this.setMargin(new MarginInfo(false, false, false, false));
        
        this.setHeight(height + "px");
        this.setStyleName(Reindeer.LAYOUT_WHITE);
        height = height - 100;        
        HorizontalLayout kMeansComponentContainer = new HorizontalLayout();
        kMeansComponentContainer.setWidthUndefined();
        kMeansComponentContainer.setHeight(height + "px");
        this.addComponent(kMeansComponentContainer);
        this.setComponentAlignment(kMeansComponentContainer,Alignment.MIDDLE_CENTER);
        
        int tableWidth = width * 1 / 3;
        kMeansClusteringTable = new KMeansClusteringTable(Quant_Central_Manager,protSelectionMap.keySet(), tableWidth, height);
        kMeansComponentContainer.addComponent(kMeansClusteringTable);
        kMeansClusteringTable.addValueChangeListener(ProteinKMeansClusterLayout.this);
        imgWidth = (width * 2 / 3);
        proteinKMeansClusteringLineChart = new KMeansClusteringLineChart(imgWidth, height, proteinKey, proteinAccession, proteinName, protSelectionMap, selectedComparisonList);
        kMeansComponentContainer.addComponent(proteinKMeansClusteringLineChart);


    }
    
    private int proteinskey;
    
    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        if (kMeansClusteringTable.getValue() != null) {
            proteinskey = (Integer) kMeansClusteringTable.getValue();
        }        
        
        final Item item = kMeansClusteringTable.getItem(proteinskey);
        String proteinAccsessionLabel = item.getItemProperty("Accession").getValue().toString();
        String proteinNamenLabel = item.getItemProperty("Name").getValue().toString();
        String key = ("--" + proteinAccsessionLabel.toLowerCase().trim() + "," + proteinNamenLabel.toLowerCase().trim()).toLowerCase().trim();
        proteinKMeansClusteringLineChart.updateChartImage(key);
        
    }
    
}
