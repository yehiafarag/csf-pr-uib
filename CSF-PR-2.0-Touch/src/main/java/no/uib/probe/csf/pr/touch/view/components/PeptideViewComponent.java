/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.components;

import com.itextpdf.text.pdf.codec.Base64;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFListener;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFPR_Central_Manager;
import no.uib.probe.csf.pr.touch.view.components.peptideviewsubcomponents.PeptideSequenceLayoutTable;
import no.uib.probe.csf.pr.touch.view.core.ImageContainerBtn;
import no.uib.probe.csf.pr.touch.view.components.peptideviewsubcomponents.StudiesLineChart;
import no.uib.probe.csf.pr.touch.view.core.InformationButton;
import no.uib.probe.csf.pr.touch.view.core.TrendLegend;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.encoders.ImageEncoder;
import org.jfree.chart.encoders.ImageEncoderFactory;
import org.jfree.chart.encoders.ImageFormat;

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
    private final ImageContainerBtn checkUncheckBtn;

    private StudiesLineChart lineChart;

    public VerticalLayout getControlBtnsContainer() {
        return controlBtnsContainer;
    }

    private Resource generateThumbImg() {
        String seq = peptideSequenceTableLayout.getSequence();
        Set<String> peptides = peptideSequenceTableLayout.getPeptides();
    final Border fullBorder = new LineBorder(Color.GRAY);
    Border peptideBorder = new LineBorder(Color.decode("#1d69b4").darker());
        JPanel proteinSequencePanel = new JPanel();
        proteinSequencePanel.setLayout(null);
        proteinSequencePanel.setSize(100, 100);
        proteinSequencePanel.setBackground(Color.WHITE);

        ChartPanel lineChartPanel = new ChartPanel(lineChart.generateThumbChart());
        lineChartPanel.setSize(100, 50);
        lineChartPanel.setLocation(0, 0);
        lineChartPanel.setOpaque(true);
        proteinSequencePanel.add(lineChartPanel);
//        lineChartPanel.setBorder(fullBorder);

        JPanel coveragePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        coveragePanel.setSize(100, 20);
        coveragePanel.setBackground(new Color(242, 242, 242));
        coveragePanel.setLocation(0, 65);
    
        coveragePanel.setBorder(fullBorder);
        coveragePanel.setOpaque(true);
        proteinSequencePanel.add(coveragePanel);

        if (seq != null && !seq.equalsIgnoreCase("") && peptides != null && !peptides.isEmpty()) {
            double charSize = 100.0 / (double) seq.length();
            for (String pep : peptides) {
                JPanel peptide = new JPanel();
                peptide.setSize((int) (pep.length() * charSize), 18);
                peptide.setBackground( Color.decode("#1d69b4"));
                peptide.setBorder(peptideBorder);
                peptide.setLocation((int) (seq.split(pep)[0].length() * charSize), 1);
                peptide.setOpaque(true);
                coveragePanel.add(peptide);

            }

        }

        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        graphics.setPaint(Color.WHITE);
        graphics.setBackground(Color.WHITE);

        proteinSequencePanel.paint(graphics);
        byte[] imageData = null;

        try {

            ImageEncoder in = ImageEncoderFactory.newInstance(ImageFormat.PNG, 1);
            imageData = in.encode(image);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        String base64 = Base64.encodeBytes(imageData);
        base64 = "data:image/png;base64," + base64;

        return new ExternalResource(base64);
    }

    public abstract void updateIcon(Resource iconResource);
    private VerticalLayout legendLayout;

    public PeptideViewComponent(CSFPR_Central_Manager CSFPR_Central_Manager, int width, int height,boolean smallScreen) {
        this.CSFPR_Central_Manager = CSFPR_Central_Manager;
        this.setWidth(100, Unit.PERCENTAGE);
        this.setHeight(height, Unit.PIXELS);
         if (!smallScreen) {
            this.setMargin(new MarginInfo(false, false, false, true));
        }
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
        proteinNameLabel.addStyleName("overflowtext");
        proteinNameLabel.addStyleName("leftaligntext");
        proteinNameLabel.setHeight(24, Unit.PIXELS);
        labelLegendContaner.addComponent(proteinNameLabel);

        legendLayout = new VerticalLayout();
        legendLayout.setWidthUndefined();
        legendLayout.setHeightUndefined();

        labelLegendContaner.addComponent(legendLayout);
        labelLegendContaner.setComponentAlignment(legendLayout, Alignment.TOP_RIGHT);
          if (width / 2 < 700) {
            legendLayout.addStyleName("showonhover");
        }
//        TrendLegend legendLayoutComponent = new TrendLegend("linechart");
//        legendLayoutComponent.setWidthUndefined();
//        legendLayoutComponent.setHeight(24, Unit.PIXELS);
//        legendLayout.addComponent(legendLayoutComponent);
        int componentHeight = (height - 80) / 2;

        VerticalLayout topLayout = new VerticalLayout();
        width = width - 50;
        topLayout.setWidth(width, Unit.PIXELS);
        topLayout.setHeight(componentHeight, Unit.PIXELS);
        topLayout.addStyleName("roundedborder");
        topLayout.addStyleName("padding20");
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
        bottomLayout.addStyleName("padding20");
        bottomLayout.addStyleName("whitelayout");

        mainBodyContainer.addComponent(bottomLayout);
        mainBodyContainer.setComponentAlignment(bottomLayout, Alignment.MIDDLE_CENTER);

        peptideSequenceTableLayout = new PeptideSequenceLayoutTable(width - 50, (componentHeight - 50),smallScreen);
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

          if (smallScreen) {
            resizeDsOnPatientNumbersBtn.setWidth(25, Unit.PIXELS);
            resizeDsOnPatientNumbersBtn.setHeight(25,Unit.PIXELS);
            resizeDsOnPatientNumbersBtn.removeStyleName("smallimg");
        }
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

          if (smallScreen) {
            dsComparisonsSwichBtn.setWidth(25, Unit.PIXELS);
            dsComparisonsSwichBtn.setHeight(50,Unit.PIXELS);
            dsComparisonsSwichBtn.removeStyleName("smallimg");
        }else{
        dsComparisonsSwichBtn.setHeight(80, Unit.PIXELS);
        dsComparisonsSwichBtn.setWidth(40, Unit.PIXELS);
          }
        dsComparisonsSwichBtn.updateIcon(comparisonDsRes);
        dsComparisonsSwichBtn.setEnabled(true);
        dsComparisonsSwichBtn.addStyleName("pointer");
        controlBtnsContainer.addComponent(dsComparisonsSwichBtn);
        controlBtnsContainer.setComponentAlignment(dsComparisonsSwichBtn, Alignment.MIDDLE_CENTER);
        dsComparisonsSwichBtn.setDescription("Show/hide individual datasets");

          if (smallScreen) {
            resizeDsOnPatientNumbersBtn.setWidth(25, Unit.PIXELS);
            resizeDsOnPatientNumbersBtn.setHeight(25,Unit.PIXELS);
            resizeDsOnPatientNumbersBtn.removeStyleName("smallimg");
        }else{
        resizeDsOnPatientNumbersBtn.setHeight(40, Unit.PIXELS);
        resizeDsOnPatientNumbersBtn.setWidth(40, Unit.PIXELS);
          }
        resizeDsOnPatientNumbersBtn.updateIcon(new ThemeResource("img/resize_1.png"));
//        resizeDsOnPatientNumbersBtn.addStyleName("smallimg");
        resizeDsOnPatientNumbersBtn.setEnabled(false);
        resizeDsOnPatientNumbersBtn.addStyleName("pointer");
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
                peptideSequenceTableLayout.sortTable(lineChart.getOrderedComparisonList(!defaultTrend));

                this.setEnabled(true);
                PeptideViewComponent.this.updateIcon(generateThumbImg());

            }

        };

          if (smallScreen) {
            orderByTrendBtn.setWidth(25, Unit.PIXELS);
            orderByTrendBtn.setHeight(25,Unit.PIXELS);
            orderByTrendBtn.removeStyleName("smallimg");
        }else{
        orderByTrendBtn.setHeight(40, Unit.PIXELS);
          orderByTrendBtn.setWidth(40, Unit.PIXELS);
          }
        orderByTrendBtn.addStyleName("pointer");
        
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

          if (smallScreen) {
            showSigOnlyBtn.setWidth(25, Unit.PIXELS);
            showSigOnlyBtn.setHeight(25,Unit.PIXELS);
            showSigOnlyBtn.removeStyleName("smallimg");
        }else{
        showSigOnlyBtn.setHeight(40, Unit.PIXELS);
        showSigOnlyBtn.setWidth(40, Unit.PIXELS);
          }
        showSigOnlyBtn.addStyleName("pointer");
        showSigOnlyBtn.updateIcon(new ThemeResource("img/showSig.png"));
        this.addStyleName("selectmultiselectedbtn");
        showSigOnlyBtn.setEnabled(true);
        controlBtnsContainer.addComponent(showSigOnlyBtn);
        controlBtnsContainer.setComponentAlignment(showSigOnlyBtn, Alignment.MIDDLE_CENTER);
        showSigOnlyBtn.setDescription("Show / hide not significant and stable peptides)");
        controlBtnsContainer.setEnabled(true);

        ThemeResource checkedApplied = new ThemeResource("img/check-square.png");
        ThemeResource checkedUnApplied = new ThemeResource("img/check-square-o.png");

        checkUncheckBtn = new ImageContainerBtn() {

            private boolean enabled = true;

            @Override
            public void onClick() {
//                quantProteinTable.clearColumnFilters();
                if (enabled) {
                    enabled = false;

                    this.updateIcon(checkedApplied);
                } else {
                    enabled = true;
                    this.updateIcon(checkedUnApplied);
                }
                peptideSequenceTableLayout.hideCheckedColumn(enabled);
            }

            @Override
            public void reset() {
                enabled = true;
                this.updateIcon(checkedUnApplied);
                peptideSequenceTableLayout.hideCheckedColumn(enabled);

            }
        };
        checkUncheckBtn.setEnabled(true);

          if (smallScreen) {
            checkUncheckBtn.setWidth(25, Unit.PIXELS);
            checkUncheckBtn.setHeight(25,Unit.PIXELS);
            checkUncheckBtn.removeStyleName("smallimg");
        }else{
        checkUncheckBtn.setHeight(40, Unit.PIXELS);
        checkUncheckBtn.setWidth(40, Unit.PIXELS);
          }
        checkUncheckBtn.addStyleName("pointer");
        checkUncheckBtn.updateIcon(checkedUnApplied);
        controlBtnsContainer.addComponent(checkUncheckBtn);
        controlBtnsContainer.setComponentAlignment(checkUncheckBtn, Alignment.MIDDLE_CENTER);
        checkUncheckBtn.setDescription("Show/hide checked column");

        InformationButton info = new InformationButton("Info", false);
        controlBtnsContainer.addComponent(info);
          if (smallScreen) {
            info.setWidth(25, Unit.PIXELS);
            info.setHeight(25,Unit.PIXELS);
            info.removeStyleName("smallimg");
        }

    }

    @Override
    public void selectionChanged(String type) {

        if (type.equalsIgnoreCase("peptide_selection")) {
            if (CSFPR_Central_Manager.getSelectedProteinAccession() == null) {
                updateIcon(null);
            } else {
                updateData(CSFPR_Central_Manager.getSelectedComparisonsList(), CSFPR_Central_Manager.getSelectedProteinAccession(), CSFPR_Central_Manager.getCustProteinSelectionTrend());
            }

        } else {
            updateIcon(null);

        }
        checkUncheckBtn.reset();

    }

    private void updateData(Set<QuantDiseaseGroupsComparison> selectedComparisonsList, String proteinKey, int custTrend) {
        if (custTrend != -1) {
            legendLayout.removeAllComponents();
            TrendLegend legendLayoutComponent = new TrendLegend(custTrend);
            legendLayoutComponent.setWidthUndefined();
            legendLayoutComponent.setHeight(24, Unit.PIXELS);
            legendLayout.addComponent(legendLayoutComponent);
           

        } else {
            legendLayout.removeAllComponents();
            TrendLegend legendLayoutComponent = new TrendLegend("linechart");
            legendLayoutComponent.setWidthUndefined();
            legendLayoutComponent.setHeight(24, Unit.PIXELS);
            legendLayout.addComponent(legendLayoutComponent);

        }

        lineChart.updateData(selectedComparisonsList, proteinKey, custTrend);

        peptideSequenceTableLayout.updateTableData(selectedComparisonsList, proteinKey);
        proteinNameLabel.setValue(lineChart.getProteinName());
        proteinNameLabel.setDescription(lineChart.getProteinName());
        updateIcon(generateThumbImg());

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
