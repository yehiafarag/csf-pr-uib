/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.components.datasetfilters;

import java.awt.Color;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Yehia Farag
 *
 * this class contains information required for pie-chart slice
 */
public class PieChartSlice implements Serializable {

    private Comparable label;
    private double value;
    private Color color;
    private Set<Integer> datasetIds = new HashSet<>();

    public Comparable getLabel() {
        return label;
    }

    public void setLabel(Comparable label) {
        this.label = label;
    }

    public int getValue() {
        return datasetIds.size();
    }


    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Set<Integer> getDatasetIds() {
        return datasetIds;
    }

    public void setDatasetIds(int datasetId) {
        this.datasetIds.add(datasetId);
    }
}
