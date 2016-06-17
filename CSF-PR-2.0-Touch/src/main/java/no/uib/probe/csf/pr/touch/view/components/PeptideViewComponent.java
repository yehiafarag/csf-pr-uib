/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.components;

import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFListener;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFPR_Central_Manager;
import no.uib.probe.csf.pr.touch.view.components.peptideviewsubcomponents.PeptideSequenceLayoutTable;
import no.uib.probe.csf.pr.touch.view.core.ImageContainerBtn;
import no.uib.probe.csf.pr.touch.view.components.peptideviewsubcomponents.StudiesLineChart;
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
    private boolean showDetailedDs = false;
    private final PeptideSequenceLayoutTable peptideSequenceTableLayout;
    private final Label proteinNameLabel;

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

        HorizontalLayout labelLegendContaner = new HorizontalLayout();
        labelLegendContaner.setWidth(100, Unit.PERCENTAGE);
        mainBodyContainer.addComponent(labelLegendContaner);
        labelLegendContaner.setMargin(new MarginInfo(false, false, false, true));
        mainBodyContainer.setComponentAlignment(labelLegendContaner, Alignment.MIDDLE_LEFT);

        proteinNameLabel = new Label();
        proteinNameLabel.setStyleName(ValoTheme.LABEL_BOLD);
        proteinNameLabel.setHeight(24, Unit.PIXELS);
        labelLegendContaner.addComponent(proteinNameLabel);

        TrendLegend legendLayout = new TrendLegend("linechart");
        legendLayout.setWidthUndefined();
        legendLayout.setHeight(24, Unit.PIXELS);
        labelLegendContaner.addComponent(legendLayout);
        labelLegendContaner.setComponentAlignment(legendLayout, Alignment.TOP_RIGHT);
        int componentHeight = (height - 80) / 2;

        VerticalLayout topLayout = new VerticalLayout();
        width = width - 50;
        topLayout.setWidth(width, Unit.PIXELS);
        topLayout.setHeight(componentHeight, Unit.PIXELS);
        topLayout.addStyleName("roundedborder");
        topLayout.addStyleName("whitelayout");

        mainBodyContainer.addComponent(topLayout);
        mainBodyContainer.setComponentAlignment(topLayout, Alignment.TOP_CENTER);
        lineChart = new StudiesLineChart(width - 50, (componentHeight - 50)) {

            @Override
            public void select(QuantDiseaseGroupsComparison comparison, int dsKey) {
                peptideSequenceTableLayout.select(comparison, dsKey);
            }

        };

        topLayout.addComponent(lineChart);

        TrendLegend sequenceLegendLayout = new TrendLegend("ministackedpeptidessequence");
        sequenceLegendLayout.setWidthUndefined();
        sequenceLegendLayout.setHeight(24, Unit.PIXELS);
        mainBodyContainer.addComponent(sequenceLegendLayout);
        mainBodyContainer.setComponentAlignment(sequenceLegendLayout, Alignment.MIDDLE_RIGHT);

        VerticalLayout bottomLayout = new VerticalLayout();
        bottomLayout.setWidth(width, Unit.PIXELS);
        bottomLayout.setHeight(componentHeight, Unit.PIXELS);
        bottomLayout.addStyleName("roundedborder");
        bottomLayout.addStyleName("whitelayout");

        mainBodyContainer.addComponent(bottomLayout);
        mainBodyContainer.setComponentAlignment(bottomLayout, Alignment.MIDDLE_CENTER);

        peptideSequenceTableLayout = new PeptideSequenceLayoutTable(width - 50, (componentHeight - 50));
        bottomLayout.addComponent(peptideSequenceTableLayout);
        bottomLayout.setComponentAlignment(peptideSequenceTableLayout, Alignment.MIDDLE_CENTER);

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
        resizeDsOnPatientNumbersBtn.updateIcon(new ThemeResource("img/resize_1.png"));
//        resizeDsOnPatientNumbersBtn.addStyleName("smallimg");
        resizeDsOnPatientNumbersBtn.setEnabled(false);
        controlBtnsContainer.addComponent(resizeDsOnPatientNumbersBtn);
        controlBtnsContainer.setComponentAlignment(resizeDsOnPatientNumbersBtn, Alignment.MIDDLE_CENTER);
        resizeDsOnPatientNumbersBtn.setDescription("Resize dataset symbols based on number of patients");

        final Resource trendOrderRes = new ThemeResource("img/orderedtrend.png");
        ImageContainerBtn orderByTrendBtn = new ImageContainerBtn() {

            private boolean defaultTrend = true;

            @Override
            public void onClick() {
                dsComparisonsSwichBtn.updateIcon(comparisonDsRes);
                resizeDsOnPatientNumbersBtn.setEnabled(false);
                showDetailedDs = false;

                if (defaultTrend) {
                    defaultTrend = false;
                    this.addStyleName("selectmultiselectedbtn");

                } else {
                    defaultTrend = true;
                    this.removeStyleName("selectmultiselectedbtn");
                }
                lineChart.trendOrder(!defaultTrend);
                PeptideViewComponent.this.updateIcon(lineChart.generateThumbImg());
                peptideSequenceTableLayout.sortTable(lineChart.getOrderedComparisonList(!defaultTrend));
                this.setEnabled(true);

            }

        };

        orderByTrendBtn.setHeight(45, Unit.PIXELS);
        orderByTrendBtn.setWidth(45, Unit.PIXELS);
        orderByTrendBtn.updateIcon(trendOrderRes);
        orderByTrendBtn.setEnabled(true);
        controlBtnsContainer.addComponent(orderByTrendBtn);
        controlBtnsContainer.setComponentAlignment(orderByTrendBtn, Alignment.MIDDLE_CENTER);
        orderByTrendBtn.setDescription("Order dataset by trend");

        ImageContainerBtn showSigOnlyBtn = new ImageContainerBtn() {

            private boolean showNotSigPeptides = true;

            @Override
            public void onClick() {
                if (showNotSigPeptides) {
                    showNotSigPeptides = false;
                    this.removeStyleName("selectmultiselectedbtn");

                } else {
                    showNotSigPeptides = true;
                    this.addStyleName("selectmultiselectedbtn");
                }

                peptideSequenceTableLayout.showNotSignificantPeptides(showNotSigPeptides);

            }

        };

        showSigOnlyBtn.setHeight(45, Unit.PIXELS);
        showSigOnlyBtn.setWidth(45, Unit.PIXELS);
        showSigOnlyBtn.updateIcon(new ThemeResource("img/showSig.png"));
        this.addStyleName("selectmultiselectedbtn");
        showSigOnlyBtn.setEnabled(true);
        controlBtnsContainer.addComponent(showSigOnlyBtn);
        controlBtnsContainer.setComponentAlignment(showSigOnlyBtn, Alignment.MIDDLE_CENTER);
        showSigOnlyBtn.setDescription("Show / hide not significant and stable peptides)");
        controlBtnsContainer.setEnabled(true);

    }

    @Override
    public void selectionChanged(String type) {

        if (type.equalsIgnoreCase("peptide_selection")) {
            if (CSFPR_Central_Manager.getSelectedProteinAccession() == null) {
                updateIcon(null);
            } else {
                updateData(CSFPR_Central_Manager.getSelectedComparisonsList(), CSFPR_Central_Manager.getSelectedProteinAccession());
            }

        } else {
            updateIcon(null);

        }

    }

    private void updateData(Set<QuantDiseaseGroupsComparison> selectedComparisonsList, String proteinKey) {

        lineChart.updateData(selectedComparisonsList, proteinKey);
        updateIcon(lineChart.generateThumbImg());
        peptideSequenceTableLayout.updateTableData(selectedComparisonsList, proteinKey);
        proteinNameLabel.setValue(lineChart.getProteinName());

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
