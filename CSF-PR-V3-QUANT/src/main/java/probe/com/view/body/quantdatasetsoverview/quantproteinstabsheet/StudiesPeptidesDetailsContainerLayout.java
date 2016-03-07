package probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet;

import com.vaadin.data.Property;
import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.studies.ProteinStudyComparisonScatterPlotLayout;
import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.jfree.chart.JFreeChart;

import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.DiseaseGroupsComparisonsProteinLayout;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
import probe.com.model.util.vaadintoimageutil.peptideslayout.ProteinInformationDataForExport;
import probe.com.selectionmanager.QuantCentralManager;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.TrendLegend;
import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.studies.PeptidesComparisonsSequenceLayout;
import probe.com.view.core.InfoPopupBtn;

/**
 *
 * @author Yehia Farag
 */
public class StudiesPeptidesDetailsContainerLayout extends VerticalLayout {

    private final Map<QuantDiseaseGroupsComparison, ProteinStudyComparisonScatterPlotLayout> studyCompLayoutMap = new LinkedHashMap<QuantDiseaseGroupsComparison, ProteinStudyComparisonScatterPlotLayout>();
    private final Map<QuantDiseaseGroupsComparison, PeptidesComparisonsSequenceLayout> peptideCompLayoutMap = new LinkedHashMap<QuantDiseaseGroupsComparison, PeptidesComparisonsSequenceLayout>();
    private final GridLayout mainStudiesLayout, mainPeptidesLayout;
    private final Panel studiesPanel, peptidesPanel;
    private final QuantCentralManager Quant_Central_Manager;
    private final int width;
    private final DiseaseGroupsComparisonsProteinLayout[] diiseaseGroupsComparisonsProteinArr;
//    private final OptionGroup studiesPeptidesSwich = new OptionGroup();
    private final OptionGroup showSigneficantPeptidesOnly;
    private final Set<ProteinInformationDataForExport> defaultPeptidesExportInfoSet, orderedPeptidesExportInfoSet;
    private final Map<String, ProteinInformationDataForExport> proteinInformationDataForExportMap;
    private final int custTrend;
    private final TrendLegend legend;
    private final TabSheet studiesPeptidesTabsheet;
    private final HorizontalLayout studiesOverviewTopSpacer, peptidesControlBtnsLayout;

    public Set<ProteinInformationDataForExport> getOrderedPeptidesExportInfoSet() {
        return orderedPeptidesExportInfoSet;
    }

    /**
     *
     * @param proteinsComparisonsArr
     * @param selectedComparisonList
     * @param Quant_Central_Manager
     * @param width
     * @param custTrend
     */
    public StudiesPeptidesDetailsContainerLayout(final QuantCentralManager Quant_Central_Manager, DiseaseGroupsComparisonsProteinLayout[] proteinsComparisonsArr, Set<QuantDiseaseGroupsComparison> selectedComparisonList, int width, int height, int custTrend) {
        this.diiseaseGroupsComparisonsProteinArr = proteinsComparisonsArr;
        this.defaultPeptidesExportInfoSet = new LinkedHashSet<ProteinInformationDataForExport>();
        this.orderedPeptidesExportInfoSet = new LinkedHashSet<ProteinInformationDataForExport>();
        this.proteinInformationDataForExportMap = new HashMap<String, ProteinInformationDataForExport>();
        this.custTrend = custTrend;
        this.setStyleName(Reindeer.LAYOUT_WHITE);
        this.setWidth("100%");
        this.setHeightUndefined();
        this.setSpacing(true);
        this.Quant_Central_Manager = Quant_Central_Manager;
        this.setMargin(new MarginInfo(false, false, false, true));

        HorizontalLayout topLayout = new HorizontalLayout();
        this.addComponent(topLayout);
        topLayout.setSpacing(true);

        Label overviewLabel = new Label("<font>Details </font> ");

        overviewLabel.setContentMode(ContentMode.HTML);
        topLayout.addComponent(overviewLabel);
        overviewLabel.setStyleName("subtitle");
        overviewLabel.setWidth("100%");
        overviewLabel.setHeight("22px");

        InfoPopupBtn info = new InfoPopupBtn("add text");
        info.setWidth("16px");
        info.setHeight("16px");
        topLayout.addComponent(info);
        //end of titlr layout  panel
        //init tab layout
        width = width - 100;
        this.width = width;

        HorizontalLayout tapsheetLayoutContainer = new HorizontalLayout();
        tapsheetLayoutContainer.setHeight(height + "px");
        tapsheetLayoutContainer.setStyleName(Reindeer.LAYOUT_WHITE);
        this.addComponent(tapsheetLayoutContainer);

        studiesPeptidesTabsheet = new TabSheet();
        tapsheetLayoutContainer.addComponent(studiesPeptidesTabsheet);
        tapsheetLayoutContainer.setComponentAlignment(studiesPeptidesTabsheet, Alignment.TOP_LEFT);
        studiesPeptidesTabsheet.setStyleName(Reindeer.TABSHEET_MINIMAL);
        studiesPeptidesTabsheet.setWidth((width) + "px");
        studiesPeptidesTabsheet.setHeight(100 + "%");

        mainStudiesLayout = new GridLayout(1, selectedComparisonList.size() + 1);
        mainStudiesLayout.setMargin(new MarginInfo(true, false, false, false));
        mainStudiesLayout.setWidth("100%");
        mainStudiesLayout.setHeightUndefined();

        studiesPanel = new Panel(mainStudiesLayout);
        studiesPanel.setStyleName(Reindeer.PANEL_LIGHT);
        studiesPanel.setHeight((height - 40) + "px");
        studiesPeptidesTabsheet.addTab(studiesPanel, "Studies");

        studiesOverviewTopSpacer = new HorizontalLayout();
        studiesOverviewTopSpacer.setStyleName(Reindeer.LAYOUT_WHITE);
        studiesOverviewTopSpacer.setHeight("40px");
        studiesOverviewTopSpacer.setWidth("20px");

        //end of studies scatter plot container
        //init overlayout (legend) 
        HorizontalLayout overTabBtnsLayout = new HorizontalLayout();
        overTabBtnsLayout.setHeight("40px");
        overTabBtnsLayout.setStyleName("overlayout");
        tapsheetLayoutContainer.addComponent(overTabBtnsLayout);
        tapsheetLayoutContainer.setComponentAlignment(overTabBtnsLayout, Alignment.TOP_LEFT);
        legend = new TrendLegend("ministackedpeptidessequence");
        legend.setWidth("600px");
        VerticalLayout wrapper = new VerticalLayout();
        wrapper.setWidth("600px");
        wrapper.setHeight("20px");
        wrapper.setStyleName(Reindeer.LAYOUT_WHITE);
        overTabBtnsLayout.addComponent(wrapper);
        legend.setVisible(false);
        wrapper.addComponent(legend);

        //init peptides layout container
        mainPeptidesLayout = new GridLayout(1, selectedComparisonList.size() + 1);
        mainPeptidesLayout.setMargin(new MarginInfo(true, false, false, false));
        mainPeptidesLayout.setWidth((100) + "%");
        mainPeptidesLayout.setHeightUndefined();

        peptidesPanel = new Panel(mainPeptidesLayout);
        peptidesPanel.setStyleName(Reindeer.PANEL_LIGHT);
        peptidesPanel.setHeight((height - 40) + "px");
        studiesPeptidesTabsheet.addTab(peptidesPanel, "Peptides");

        peptidesControlBtnsLayout = new HorizontalLayout();
        peptidesControlBtnsLayout.setHeight("40px");
        peptidesControlBtnsLayout.setWidth(100 + "%");
        peptidesControlBtnsLayout.setStyleName("lowerlink");
        peptidesControlBtnsLayout.addStyleName(Reindeer.LAYOUT_BLUE);

        VerticalLayout vlo = new VerticalLayout();
        vlo.setWidth("425px");
        vlo.setHeight("40px");
        peptidesControlBtnsLayout.addComponent(vlo);

        showSigneficantPeptidesOnly = new OptionGroup();
        showSigneficantPeptidesOnly.setWidth("135px");
        showSigneficantPeptidesOnly.setNullSelectionAllowed(true); // user can not 'unselect'
        showSigneficantPeptidesOnly.setMultiSelect(true);

        showSigneficantPeptidesOnly.addItem("Significant");
        showSigneficantPeptidesOnly.addItem("PTMs");

        showSigneficantPeptidesOnly.addStyleName("horizontal");
        showSigneficantPeptidesOnly.setVisible(true);

        showSigneficantPeptidesOnly.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                showSignificantRegulationOnly(showSigneficantPeptidesOnly.getValue().toString().contains("Significant"));
                showPTM(showSigneficantPeptidesOnly.getValue().toString().contains("PTMs"));
            }
        });
        vlo.addComponent(showSigneficantPeptidesOnly);
        vlo.setComponentAlignment(showSigneficantPeptidesOnly, Alignment.TOP_RIGHT);

        studiesPeptidesTabsheet.addSelectedTabChangeListener(new TabSheet.SelectedTabChangeListener() {

            @Override
            public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
                String c = studiesPeptidesTabsheet.getTab(event.getTabSheet().getSelectedTab()).getCaption();
                if (c.equalsIgnoreCase("Peptides")) {
                    legend.setVisible(true);
                } else {
                    legend.setVisible(false);
                }
            }
        });

    }

    /**
     *
     * @param ordComparisonProteins
     */
    public void orderComparisons(DiseaseGroupsComparisonsProteinLayout[] ordComparisonProteins) {

        mainStudiesLayout.removeAllComponents();
        mainStudiesLayout.addComponent(studiesOverviewTopSpacer, 0, 0);
        mainPeptidesLayout.removeAllComponents();
        mainPeptidesLayout.addComponent(peptidesControlBtnsLayout, 0, 0);

        int rowIndex = 1;

        for (final DiseaseGroupsComparisonsProteinLayout cp : ordComparisonProteins) {
            if (cp == null || cp.getSignificantTrindCategory() == -1) {
                continue;
            }
            ProteinStudyComparisonScatterPlotLayout studyCompLayout = studyCompLayoutMap.get(cp.getComparison());
            mainStudiesLayout.addComponent(studyCompLayout, 0, rowIndex);
            mainStudiesLayout.setComponentAlignment(studyCompLayout, Alignment.MIDDLE_LEFT);

            PeptidesComparisonsSequenceLayout peptideCompLayout = peptideCompLayoutMap.get(cp.getComparison());
            mainPeptidesLayout.addComponent(peptideCompLayout, 0, rowIndex);
            mainPeptidesLayout.setComponentAlignment(peptideCompLayout, Alignment.MIDDLE_LEFT);
            orderedPeptidesExportInfoSet.add(proteinInformationDataForExportMap.get(cp.getComparison().getComparisonHeader()));

            rowIndex++;
        }
    }
    private ProteinStudyComparisonScatterPlotLayout lastHeighlitedStudyLayout;
    private PeptidesComparisonsSequenceLayout lastHeighlitedPeptideLayout;

    /**
     *
     * @param groupComp
     * @param clicked
     */
    public void highlightComparison(QuantDiseaseGroupsComparison groupComp, boolean clicked) {
        if (lastHeighlitedStudyLayout != null) {
            lastHeighlitedStudyLayout.highlight(false, false);
        }
        if (lastHeighlitedPeptideLayout != null) {
            lastHeighlitedPeptideLayout.highlight(false, false);
        }
        ProteinStudyComparisonScatterPlotLayout studyLayout = studyCompLayoutMap.get(groupComp);
        PeptidesComparisonsSequenceLayout peptideLayout = peptideCompLayoutMap.get(groupComp);
        if (studyLayout == null) {
            return;
        }
        peptideLayout.highlight(true, clicked);
        studyLayout.highlight(true, clicked);
        lastHeighlitedStudyLayout = studyLayout;
        lastHeighlitedPeptideLayout = peptideLayout;
    }

    /**
     *
     */
    public void redrawCharts() {

        if (studyCompLayoutMap.isEmpty()) {
            mainStudiesLayout.addComponent(studiesOverviewTopSpacer, 0, 0);
            int rowIndex = 1;
            for (DiseaseGroupsComparisonsProteinLayout cprot : diiseaseGroupsComparisonsProteinArr) {
                if (cprot == null || cprot.getSignificantTrindCategory() == -1) {
                    continue;
                }
                ProteinStudyComparisonScatterPlotLayout protCompLayout = new ProteinStudyComparisonScatterPlotLayout(Quant_Central_Manager, cprot, width, custTrend);
                mainStudiesLayout.addComponent(protCompLayout, 0, rowIndex);
                mainStudiesLayout.setComponentAlignment(protCompLayout, Alignment.MIDDLE_LEFT);
                studyCompLayoutMap.put(cprot.getComparison(), protCompLayout);

                final DiseaseGroupsComparisonsProteinLayout gc = cprot;
                LayoutEvents.LayoutClickListener closeListener = new LayoutEvents.LayoutClickListener() {

                    private final QuantDiseaseGroupsComparison localComparison = gc.getComparison();

                    @Override
                    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                        Set<QuantDiseaseGroupsComparison> selectedComparisonList = Quant_Central_Manager.getSelectedDiseaseGroupsComparisonList();
                        selectedComparisonList.remove(localComparison);
                        Quant_Central_Manager.setDiseaseGroupsComparisonSelection(selectedComparisonList);
                    }
                };
                protCompLayout.getCloseBtn().addLayoutClickListener(closeListener);
                rowIndex++;
            }

        }
        for (ProteinStudyComparisonScatterPlotLayout layout : studyCompLayoutMap.values()) {

            layout.redrawChart();
        }
        if (peptideCompLayoutMap.isEmpty()) {
            mainPeptidesLayout.addComponent(peptidesControlBtnsLayout, 0, 0);
            defaultPeptidesExportInfoSet.clear();
            int rowIndex = 1;
            for (DiseaseGroupsComparisonsProteinLayout cprot : diiseaseGroupsComparisonsProteinArr) {
                if (cprot == null || cprot.getSignificantTrindCategory() == -1) {
                    continue;
                }

                PeptidesComparisonsSequenceLayout protCompLayout = new PeptidesComparisonsSequenceLayout(Quant_Central_Manager, cprot, width);
                mainPeptidesLayout.addComponent(protCompLayout, 0, rowIndex);
                mainPeptidesLayout.setComponentAlignment(protCompLayout, Alignment.MIDDLE_LEFT);
                peptideCompLayoutMap.put(cprot.getComparison(), protCompLayout);

                final DiseaseGroupsComparisonsProteinLayout gc = cprot;
                LayoutEvents.LayoutClickListener closeListener = new LayoutEvents.LayoutClickListener() {

                    private final QuantDiseaseGroupsComparison localComparison = gc.getComparison();

                    @Override
                    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                        Set<QuantDiseaseGroupsComparison> selectedComparisonList = Quant_Central_Manager.getSelectedDiseaseGroupsComparisonList();
                        selectedComparisonList.remove(localComparison);
                        Quant_Central_Manager.setDiseaseGroupsComparisonSelection(selectedComparisonList);
                    }
                };
                protCompLayout.getCloseBtn().addLayoutClickListener(closeListener);

                ProteinInformationDataForExport peptideInfo = new ProteinInformationDataForExport();
                peptideInfo.setComparisonsTitle(protCompLayout.getComparisonTitleValue());
                peptideInfo.setStudies(protCompLayout.getStudiesMap());
                defaultPeptidesExportInfoSet.add(peptideInfo);
                proteinInformationDataForExportMap.put(cprot.getComparison().getComparisonHeader(), peptideInfo);

                rowIndex++;
            }

            showSigneficantPeptidesOnly.setItemEnabled("PTMs", false);
            for (PeptidesComparisonsSequenceLayout protCompLayout : peptideCompLayoutMap.values()) {
                if (protCompLayout.isHasPTM()) {
                    showSigneficantPeptidesOnly.setItemEnabled("PTMs", true);
                    Set<String> ids = new HashSet<String>();
                    ids.add("PTMs");
                    showSigneficantPeptidesOnly.setValue(ids);
                    break;
                }
            }

        }

    }

    private void showPTM(boolean show) {
        for (PeptidesComparisonsSequenceLayout protCompLayout : peptideCompLayoutMap.values()) {
            protCompLayout.showPTM(show);
        }

    }

    private void showSignificantRegulationOnly(boolean show) {
        for (PeptidesComparisonsSequenceLayout protCompLayout : peptideCompLayoutMap.values()) {
            protCompLayout.showSignificantRegulationOnly(show);
        }

    }

    public Set<JFreeChart> getScatterCharts() {
        Set<JFreeChart> scatterSet = new LinkedHashSet<JFreeChart>();
        Iterator<Component> itr = mainStudiesLayout.iterator();
        while (itr.hasNext()) {
            Component comp = itr.next();
            if (comp instanceof ProteinStudyComparisonScatterPlotLayout) {
                ProteinStudyComparisonScatterPlotLayout psctPlot = (ProteinStudyComparisonScatterPlotLayout) comp;
                scatterSet.add(psctPlot.getScatterPlot());

            }

        }

        return scatterSet;
    }

    public Set<ProteinInformationDataForExport> getDefaultPeptidesExportInfoSet() {
        return defaultPeptidesExportInfoSet;
    }
}