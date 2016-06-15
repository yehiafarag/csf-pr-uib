/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.components;

import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFListener;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFPR_Central_Manager;
import no.uib.probe.csf.pr.touch.view.core.ImageContainerBtn;
import no.uib.probe.csf.pr.touch.view.core.StudiesLineChart;
import no.uib.probe.csf.pr.touch.view.core.TrendLegend;

/**
 *
 * @author Yehia Farag
 *
 * this class represents the peptide information layout the layout show overall
 * trend line-chart and detailed studies line chart for the selected protein the
 * second component consist of table of comparisons and peptides sequences for
 * each protein
 */
public abstract class PeptideViewComponent extends VerticalLayout implements CSFListener {

    private final CSFPR_Central_Manager CSFPR_Central_Manager;
    private final VerticalLayout controlBtnsContainer;

    private StudiesLineChart lineChart;

    public VerticalLayout getControlBtnsContainer() {
        return controlBtnsContainer;
    }

    public abstract void updateIcon(Resource iconResource);

    public PeptideViewComponent(CSFPR_Central_Manager CSFPR_Central_Manager, int width, int height, QuantDiseaseGroupsComparison userCustomizedComparison) {
        this.CSFPR_Central_Manager = CSFPR_Central_Manager;
        this.setWidth(100, Unit.PERCENTAGE);
        this.setHeight(height, Unit.PIXELS);
        VerticalLayout mainBodyContainer = new VerticalLayout();
        mainBodyContainer.setSpacing(true);
        mainBodyContainer.setWidth(100, Unit.PERCENTAGE);
        mainBodyContainer.setHeightUndefined();
        this.addComponent(mainBodyContainer);

        TrendLegend legendLayout = new TrendLegend("linechart");
        legendLayout.setWidthUndefined();
        legendLayout.setHeight(24, Unit.PIXELS);
        mainBodyContainer.addComponent(legendLayout);
        mainBodyContainer.setComponentAlignment(legendLayout, Alignment.MIDDLE_RIGHT);

        VerticalLayout topLayout = new VerticalLayout();
        width = width - 50;
        topLayout.setWidth(width, Unit.PIXELS);
        topLayout.setHeight(500, Unit.PIXELS);
        topLayout.addStyleName("roundedborder");
        topLayout.addStyleName("whitelayout");

        mainBodyContainer.addComponent(topLayout);
        mainBodyContainer.setComponentAlignment(topLayout, Alignment.TOP_CENTER);
        lineChart = new StudiesLineChart(width - 50, 450);

        topLayout.addComponent(lineChart);

        VerticalLayout bottomLayout = new VerticalLayout();
        bottomLayout.setWidth(width, Unit.PIXELS);
        bottomLayout.setHeight(500, Unit.PIXELS);
        bottomLayout.addStyleName("roundedborder");
        bottomLayout.addStyleName("whitelayout");

        mainBodyContainer.addComponent(bottomLayout);
        mainBodyContainer.setComponentAlignment(bottomLayout, Alignment.MIDDLE_CENTER);

        //end of toplayout
        //start chart layout
//        VerticalLayout tableLayoutFrame = new VerticalLayout();
//        height = height - 44;
//        
//        int tableHeight = height;
//        width = width - 50;
//        tableLayoutFrame.setWidth(width, Unit.PIXELS);
//        tableLayoutFrame.setHeight(tableHeight, Unit.PIXELS);
//        tableLayoutFrame.addStyleName("roundedborder");
//        tableLayoutFrame.addStyleName("whitelayout");
//        bodyContainer.addComponent(tableLayoutFrame);
//        bodyContainer.setComponentAlignment(tableLayoutFrame, Alignment.MIDDLE_CENTER);
//        height = height - 40;
//        width = width - 60;
        CSFPR_Central_Manager.registerListener(PeptideViewComponent.this);

        //init side control btns layout 
        controlBtnsContainer = new VerticalLayout();
        controlBtnsContainer.setHeightUndefined();
        controlBtnsContainer.setWidthUndefined();
        controlBtnsContainer.setSpacing(true);
        final Resource comparisonDsRes = new ThemeResource("img/comparisons-ds.png");
        final Resource dsComparisonRes = new ThemeResource("img/ds-comparisons.png");

        final ImageContainerBtn resizeDsOnPatientNumbersBtn = new ImageContainerBtn() {
            private boolean resize = false;

            @Override
            public void onClick() {
                resize = !resize;
                lineChart.setResizeDetailedStudies(resize);
            }

        };

        ImageContainerBtn dsComparisonsSwichBtn = new ImageContainerBtn() {
            private boolean showDetailedDs = false;

            @Override
            public void onClick() {
                if (showDetailedDs) {
                    showDetailedDs = false;
                    this.updateIcon(comparisonDsRes);
                    resizeDsOnPatientNumbersBtn.setEnabled(false);
                } else {
                    showDetailedDs = true;
                    this.updateIcon(dsComparisonRes);
                    resizeDsOnPatientNumbersBtn.setEnabled(true);

                }
                lineChart.viewDetailedStudies(showDetailedDs);
            }

        };

        dsComparisonsSwichBtn.setHeight(90, Unit.PIXELS);
        dsComparisonsSwichBtn.setWidth(45, Unit.PIXELS);
        dsComparisonsSwichBtn.updateIcon(comparisonDsRes);
        dsComparisonsSwichBtn.setEnabled(true);
        controlBtnsContainer.addComponent(dsComparisonsSwichBtn);
        controlBtnsContainer.setComponentAlignment(dsComparisonsSwichBtn, Alignment.MIDDLE_CENTER);
        dsComparisonsSwichBtn.setDescription("show comparisons / Datasets");

        resizeDsOnPatientNumbersBtn.setHeight(45, Unit.PIXELS);
        resizeDsOnPatientNumbersBtn.setWidth(45, Unit.PIXELS);
        resizeDsOnPatientNumbersBtn.updateIcon(new ThemeResource("img/resize.png"));
        resizeDsOnPatientNumbersBtn.addStyleName("smallimg");
        resizeDsOnPatientNumbersBtn.setEnabled(false);
        controlBtnsContainer.addComponent(resizeDsOnPatientNumbersBtn);
        controlBtnsContainer.setComponentAlignment(resizeDsOnPatientNumbersBtn, Alignment.MIDDLE_CENTER);
        resizeDsOnPatientNumbersBtn.setDescription("Resize dataset symbols based on patients number");

        final Resource trendOrderRes = new ThemeResource("img/orderedtrend.png");
        ImageContainerBtn orderByTrendBtn = new ImageContainerBtn() {

            private boolean defaultTrend = true;

            @Override
            public void onClick() {
                if (defaultTrend) {
                    defaultTrend = false;
                       this.addStyleName("selectmultiselectedbtn");
                      
                } else {
                    defaultTrend = true;
                     this.removeStyleName("selectmultiselectedbtn");
                } 
                lineChart.trendOrder(!defaultTrend);

            }

        };

        orderByTrendBtn.setHeight(45, Unit.PIXELS);
        orderByTrendBtn.setWidth(45, Unit.PIXELS);
        orderByTrendBtn.updateIcon(trendOrderRes);
        orderByTrendBtn.setEnabled(true);
        controlBtnsContainer.addComponent(orderByTrendBtn);
        controlBtnsContainer.setComponentAlignment(orderByTrendBtn, Alignment.MIDDLE_CENTER);
        orderByTrendBtn.setDescription("Order dataset by trend");

    }

    @Override
    public void selectionChanged(String type) {

        if (type.equalsIgnoreCase("peptide_selection")) {
            updateData(CSFPR_Central_Manager.getSelectedComparisonsList(), CSFPR_Central_Manager.getSelectedProteinAccession());

        }

    }

    private void updateData(Set<QuantDiseaseGroupsComparison> selectedComparisonsList, String proteinKey) {
        lineChart.updateData(selectedComparisonsList, proteinKey);
        updateIcon(lineChart.generateThumbImg());

    }

    @Override
    public String getFilterId() {
        return "peptideComponent";
    }

    @Override
    public void removeFilterValue(String value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
   

}
