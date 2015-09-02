package probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.Map;
import java.util.Set;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.DiseaseGroupsComparisonsProteinLayout;
import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.kmeansclustering.KMeansClusteringLineChart;
import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.kmeansclustering.KMeansClusteringTable;
import probe.com.view.core.CustomExternalLink;

/**
 *
 * @author Yehia Farag
 *
 * k-means clustering layout for selected protein
 */
public class ProteinKMeansClusterLayout extends VerticalLayout implements Property.ValueChangeListener {

    private final Label kMeansClusteringTitle;
    private final VerticalLayout closeBtn;
    private final KMeansClusteringLineChart proteinKMeansClusteringLineChart;
    private final int imgWidth;
    private final KMeansClusteringTable kMeansClusteringTable;

    public ProteinKMeansClusterLayout(String proteinKey, String proteinName, String proteinAccession, int width, Map<String, DiseaseGroupsComparisonsProteinLayout[]> protSelectionMap, Set<QuantDiseaseGroupsComparison> selectedComparisonList) {

        this.setSpacing(true);
        this.setMargin(new MarginInfo(false, false, true, false));
        this.setHeightUndefined();
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidthUndefined();
        topLayout.setHeight("20px");
        topLayout.setStyleName(Reindeer.LAYOUT_WHITE);

        kMeansClusteringTitle = new Label("K-means clustering for ( " + proteinName + " )");
        kMeansClusteringTitle.setContentMode(ContentMode.HTML);
        kMeansClusteringTitle.setStyleName("custChartLabelHeader");
        kMeansClusteringTitle.setWidth((width - 70) + "px");
        topLayout.addComponent(kMeansClusteringTitle);
        topLayout.setComponentAlignment(kMeansClusteringTitle, Alignment.TOP_LEFT);

        closeBtn = new VerticalLayout();
        closeBtn.setWidth("20px");
        closeBtn.setHeight("20px");
        closeBtn.setStyleName("closebtn");
        topLayout.addComponent(closeBtn);
        topLayout.setComponentAlignment(closeBtn, Alignment.TOP_RIGHT);

        this.addComponent(topLayout);

        HorizontalLayout kMeansComponentContainer = new HorizontalLayout();
        kMeansComponentContainer.setWidthUndefined();
        kMeansComponentContainer.setHeight("500px");

        this.addComponent(kMeansComponentContainer);

        kMeansClusteringTable = new KMeansClusteringTable(protSelectionMap.keySet());
        kMeansComponentContainer.addComponent(kMeansClusteringTable);
        kMeansClusteringTable.addValueChangeListener(ProteinKMeansClusterLayout.this);

        imgWidth = (width - 300);
        proteinKMeansClusteringLineChart = new KMeansClusteringLineChart(imgWidth, proteinKey, proteinAccession, proteinName, protSelectionMap, selectedComparisonList);
        kMeansComponentContainer.addComponent(proteinKMeansClusteringLineChart);

//        proteinKMeansClusteringLineChart.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
//            @Override
//            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
////                final ChartEntity entity = defaultScatterPlotRenderingInfo.getEntityCollection().getEntity(event.getRelativeX(), event.getRelativeY());
////                QuantDatasetObject ds = null;
////                if (entity.getShapeType().equalsIgnoreCase("rect") && event.getClickedComponent() instanceof SquaredDot) {
////                    ds = (QuantDatasetObject) ((SquaredDot) event.getClickedComponent()).getParam("QuantDatasetObject");
////
////                } else {
////                    if (entity instanceof XYItemEntity) {
////                        int x = ((XYItemEntity) entity).getSeriesIndex();
////                        int y = ((XYItemEntity) entity).getItem();
////
////                        if (searchingMode) {
////                            ds = exploringFiltersManager.getFullQuantDatasetArr().get(comparisonProtein.getDSID(x, y) - 1);
////                        } else {
////                            ds = exploringFiltersManager.getFullQuantDatasetArr().get(comparisonProtein.getDSID(x, y));
////                        }
////
////                    }
////                }
////                if (ds == null) {
////                    return;
////                }
////                exploringFiltersManager.setSelectedDataset(ds.getUniqId());
////                exploringFiltersManager.setStudyLevelFilterSelection(new CSFFilterSelection("Study_Selection", new int[]{ds.getUniqId()}, "scatter", null));//               
////                peptidesOverviewLayoutManager.updateSelectedProteinInformation(ds.getUniqId(), ds, cp.getUniProtAccess());
////
////            }
//        });
        closeBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                setVisible(false);
            }
        });

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
