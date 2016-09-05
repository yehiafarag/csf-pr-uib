/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch;

import java.io.Serializable;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.HeatMapHeaderCellInformationBean;
import no.uib.probe.csf.pr.touch.view.components.heatmapsubcomponents.HeatMapImgGenerator;

/**
 *
 * @author Yehia Farag
 *
 * this class represents csf-pr exporting layer that is responsible for
 * exporting high resolution images and tables
 */
public class DataExporter implements Serializable {

    private Set<HeatMapHeaderCellInformationBean>heatmapRows;
    private Set<HeatMapHeaderCellInformationBean>heatmapColumns;
    private String[][] heatmapData;
    private final int width = 595;
    private final int height = 842;
    private final HeatMapImgGenerator heatmapGenerator = new HeatMapImgGenerator();

    /**
     * Export current datasets heat-map into image in pdf file
     */
    public void exportHeatmap( boolean resetRowLabels, boolean restColumnLabels) {
       String str =  heatmapGenerator.generateHeatmap(heatmapRows, heatmapColumns, heatmapData, width, height, resetRowLabels, restColumnLabels, true);
        System.out.println("at str "+str);
    }

    public void setHeatmapRows(Set<HeatMapHeaderCellInformationBean> heatmapRows) {
        this.heatmapRows = heatmapRows;
    }

    public void setHeatmapColumns(Set<HeatMapHeaderCellInformationBean>heatmapColumns) {
        this.heatmapColumns = heatmapColumns;
    }

    public void setHeatmapData(String[][] heatmapData) {
        this.heatmapData = heatmapData;
    }

}
