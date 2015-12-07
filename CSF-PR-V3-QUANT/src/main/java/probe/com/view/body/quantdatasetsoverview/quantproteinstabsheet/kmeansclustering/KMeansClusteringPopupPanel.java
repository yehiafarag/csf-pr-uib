package probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.kmeansclustering;

import com.vaadin.server.Page;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import probe.com.handlers.CSFPRHandler;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
import probe.com.selectionmanager.QuantCentralManager;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.DiseaseGroupsComparisonsProteinLayout;

/**
 *
 * @author Yehia Farag
 */
public class KMeansClusteringPopupPanel extends Window {

    private final VerticalLayout mainBodyLayout;
    private final CSFPRHandler CSFPR_Handler;

    public KMeansClusteringPopupPanel(QuantCentralManager Quant_Central_Manager, CSFPRHandler CSFPR_Handler, String proteinKey, String proteinName, String proteinAccession, Map<String, DiseaseGroupsComparisonsProteinLayout[]> protSelectionMap, Set<QuantDiseaseGroupsComparison> selectedComparisonList) {

        this.CSFPR_Handler = CSFPR_Handler;
        int height = Page.getCurrent().getBrowserWindowHeight() - 200;
        int pageWidth = Page.getCurrent().getBrowserWindowWidth() - 200;

        this.setWidth((pageWidth) + "px");
        this.setHeight((height) + "px");
        this.setVisible(true);
        this.setResizable(false);
        this.setClosable(false);
        this.setStyleName(Reindeer.WINDOW_LIGHT);
        this.setModal(true);
        this.setDraggable(false);
        this.setWindowMode(WindowMode.NORMAL);
        this.setCaptionAsHtml(true);
        this.setClosable(true);

        this.setCaption("K-Means Clustering for <a href='http://www.uniprot.org/uniprot/" + proteinAccession + "'target=\"_blank\"> (" + proteinName + ")</a>");

        this.mainBodyLayout = new VerticalLayout();
        height = height - 100;
        mainBodyLayout.setWidthUndefined();
        mainBodyLayout.setHeight(height + "px");
        mainBodyLayout.setSpacing(true);
        mainBodyLayout.setMargin(true);
        mainBodyLayout.setHeightUndefined();
        mainBodyLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        ProteinKMeansClusterLayout proteinKMeansClusterLayout = this.initKMeansClusteringLayout(Quant_Central_Manager, proteinKey, proteinName, proteinAccession, pageWidth, height, protSelectionMap, selectedComparisonList);
        mainBodyLayout.addComponent(proteinKMeansClusterLayout);
        mainBodyLayout.setComponentAlignment(proteinKMeansClusterLayout, Alignment.BOTTOM_CENTER);
        this.setContent(mainBodyLayout);
        UI.getCurrent().addWindow(KMeansClusteringPopupPanel.this);
        this.center();
    }

    @Override
    public void close() {
        this.setVisible(false);
    }

    private ProteinKMeansClusterLayout initKMeansClusteringLayout(QuantCentralManager Quant_Central_Manager, String proteinKey, String proteinName, String proteinAccession, int width, int height, Map<String, DiseaseGroupsComparisonsProteinLayout[]> protSelectionMap, Set<QuantDiseaseGroupsComparison> selectedComparisonList) {
        double[][] samples = new double[protSelectionMap.size()][selectedComparisonList.size()];
        String[] sampleIds = new String[protSelectionMap.size()];
        int x = 0;
        for (String key : protSelectionMap.keySet()) {
            sampleIds[x] = key;
            DiseaseGroupsComparisonsProteinLayout[] dGr = protSelectionMap.get(key);
            double[] sampleRow = new double[dGr.length];
            int y = 0;
            for (DiseaseGroupsComparisonsProteinLayout dBean : dGr) {
                if (dBean == null) {
                    sampleRow[y] = 0;

                } else {
                    sampleRow[y] = dBean.getSignificantCellValue();
                }
                y++;
            }
            samples[x] = sampleRow;
            x++;
        }
        int iterationNumber = Math.min(100, samples.length);
        ArrayList<String> proteinsKeysList = CSFPR_Handler.runKMeanClustering(samples, sampleIds, iterationNumber, proteinKey);
        int protSize= proteinsKeysList.size();
        while (protSize < 2 && iterationNumber >1) {
            iterationNumber = iterationNumber - (iterationNumber / 2);
            proteinsKeysList = CSFPR_Handler.runKMeanClustering(samples, sampleIds, iterationNumber, proteinKey);
            protSize = proteinsKeysList.size();
        }

        Map<String, DiseaseGroupsComparisonsProteinLayout[]> updatedProtSelectionMap = new LinkedHashMap<String, DiseaseGroupsComparisonsProteinLayout[]>();
        for (String key : proteinsKeysList) {
            if (protSelectionMap.containsKey(key)) {
                updatedProtSelectionMap.put(key, protSelectionMap.get(key));
            }

        }
        ProteinKMeansClusterLayout proteinKMeansClusterLayout = new ProteinKMeansClusterLayout(Quant_Central_Manager, proteinKey, proteinName, proteinAccession, width, height, updatedProtSelectionMap, selectedComparisonList);
        return proteinKMeansClusterLayout;

    }

}
