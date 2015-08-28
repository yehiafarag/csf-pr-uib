package probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.LinkedHashMap;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import probe.com.model.beans.quant.QuantDatasetObject;
import probe.com.selectionmanager.CSFFilterSelection;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.DiseaseGroupsComparisonsProtein;
import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.kmeansclustering.KMeansClusteringTable;
import probe.com.view.core.jfreeutil.SquaredDot;

/**
 *
 * @author Yehia Farag
 *
 * k-means clustering layout for selected protein
 */
public class ProteinKMeansClusterLayout extends VerticalLayout {

    private final Label kMeansClusteringTitle;
    private final VerticalLayout closeBtn;
    private final AbsoluteLayout ProteinKMeansClusteringPlotContainer;
    private final int imgWidth;
     private final String teststyle;
    private final Page.Styles styles = Page.getCurrent().getStyles();
    private String defaultKMeansClusteringImgUrl;

    public ProteinKMeansClusterLayout(String proteinName,String proteinAccession,int width,LinkedHashMap<String, DiseaseGroupsComparisonsProtein[]> protSelectionMap) {

//        this.setColumns(4);
//        this.setRows(2);
        this.setSpacing(true);
        this.setMargin(new MarginInfo(false, false, true, false));
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidthUndefined();
        topLayout.setHeight("20px");
        topLayout.setStyleName(Reindeer.LAYOUT_WHITE);

        kMeansClusteringTitle = new Label("K-means clustering for ( "+proteinName+" )");
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
        kMeansComponentContainer.setHeight("100%");
        
        this.addComponent(kMeansComponentContainer);
        
        
        KMeansClusteringTable kMeansClusteringTable= new KMeansClusteringTable(protSelectionMap.keySet());
        kMeansComponentContainer.addComponent(kMeansClusteringTable);
        
        
        
        
        

        imgWidth = (width - 70);
        ProteinKMeansClusteringPlotContainer = new AbsoluteLayout();
//        this.generateScatterplotchart(cp, imgWidth, 150);

//        kMeansComponentContainer.addComponent(ProteinKMeansClusteringPlotContainer);
        ProteinKMeansClusteringPlotContainer.setWidth(imgWidth + "px");
        ProteinKMeansClusteringPlotContainer.setHeight(150 + "px");

        teststyle = proteinAccession.replace(" ", "_").replace(")", "_").replace("(", "_").toLowerCase() + "_" + proteinName.replace(" ", "_").replace(")", "_").replace("(", "_").toLowerCase() + "_kmeansclusteringplot";
        
        styles.add("." + teststyle + " {  background-image: url(" + defaultKMeansClusteringImgUrl + " );background-position:center; background-repeat: no-repeat; }");
        ProteinKMeansClusteringPlotContainer.setStyleName(teststyle);
        
        
        
//        ProteinKMeansClusteringPlotContainer.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
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
        closeBtn.addLayoutClickListener(new  LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                setVisible(false);
            }
        });

    }

}
