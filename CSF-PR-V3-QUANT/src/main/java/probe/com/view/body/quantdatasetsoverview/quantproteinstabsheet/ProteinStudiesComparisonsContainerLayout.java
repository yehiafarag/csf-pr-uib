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
import probe.com.selectionmanager.DatasetExploringCentralSelectionManager;
import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.studies.PeptidesComparisonsSequenceLayout;
import probe.com.view.core.InfoPopupBtn;

/**
 *
 * @author Yehia Farag
 */
public class ProteinStudiesComparisonsContainerLayout extends VerticalLayout {

    private final Map<QuantDiseaseGroupsComparison, ProteinStudyComparisonScatterPlotLayout> studyCompLayoutMap = new LinkedHashMap<QuantDiseaseGroupsComparison, ProteinStudyComparisonScatterPlotLayout>();
    private final Map<QuantDiseaseGroupsComparison, PeptidesComparisonsSequenceLayout> peptideCompLayoutMap = new LinkedHashMap<QuantDiseaseGroupsComparison, PeptidesComparisonsSequenceLayout>();
    private final GridLayout mainStudiesLayout, mainPeptidesLayout;
    private final DatasetExploringCentralSelectionManager datasetExploringCentralSelectionManager;
    private final int width;
    private final DiseaseGroupsComparisonsProteinLayout[] diiseaseGroupsComparisonsProteinArr;
    private final OptionGroup studiesPeptidesSwich = new OptionGroup();
    private final OptionGroup showSigneficantPeptidesOnly;
    private final Set<ProteinInformationDataForExport> defaultPeptidesExportInfoSet,orderedPeptidesExportInfoSet;
    private final Map<String, ProteinInformationDataForExport> proteinInformationDataForExportMap;

    public Set<ProteinInformationDataForExport> getOrderedPeptidesExportInfoSet() {
        return orderedPeptidesExportInfoSet;
    }

    /**
     *
     * @param proteinsComparisonsArr
     * @param selectedComparisonList
     * @param datasetExploringCentralSelectionManager
     * @param width
     */
    public ProteinStudiesComparisonsContainerLayout(DiseaseGroupsComparisonsProteinLayout[] proteinsComparisonsArr, Set<QuantDiseaseGroupsComparison> selectedComparisonList, final DatasetExploringCentralSelectionManager datasetExploringCentralSelectionManager, int width) {
        setStyleName(Reindeer.LAYOUT_WHITE);
        this.setWidth("100%");
        this.setHeightUndefined();
        this.setSpacing(true);
        this.datasetExploringCentralSelectionManager = datasetExploringCentralSelectionManager;
        studiesPeptidesSwich.setWidth("180px");
        studiesPeptidesSwich.setNullSelectionAllowed(false); // user can not 'unselect'
        studiesPeptidesSwich.setMultiSelect(false);
        studiesPeptidesSwich.addItem("Studies");
        studiesPeptidesSwich.addItem("Peptides");
        studiesPeptidesSwich.setValue("Studies");
        studiesPeptidesSwich.addStyleName("specialhorizontal");

        studiesPeptidesSwich.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (studiesPeptidesSwich.getValue().toString().equalsIgnoreCase("Studies")) {
                    mainStudiesLayout.setVisible(true);
                    mainPeptidesLayout.setVisible(false);
                    showSigneficantPeptidesOnly.setVisible(false);

                } else {
                    mainStudiesLayout.setVisible(false);
                    mainPeptidesLayout.setVisible(true);
                    showSigneficantPeptidesOnly.setVisible(true);
                }
            }
        });

        showSigneficantPeptidesOnly = new OptionGroup();
        showSigneficantPeptidesOnly.setWidth("135px");
        showSigneficantPeptidesOnly.setNullSelectionAllowed(true); // user can not 'unselect'
        showSigneficantPeptidesOnly.setMultiSelect(true);

        showSigneficantPeptidesOnly.addItem("Significant");
        showSigneficantPeptidesOnly.addItem("PTMs");

        showSigneficantPeptidesOnly.addStyleName("horizontal");
        showSigneficantPeptidesOnly.setVisible(false);

        showSigneficantPeptidesOnly.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                showSignificantRegulationOnly(showSigneficantPeptidesOnly.getValue().toString().contains("Significant"));
                showPTM(showSigneficantPeptidesOnly.getValue().toString().contains("PTMs"));
            }
        });
        HorizontalLayout topPanelLayout = new HorizontalLayout();
        topPanelLayout.setWidth((width - 40) + "px");
        topPanelLayout.setHeight("40px");
        topPanelLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        this.addComponent(topPanelLayout);

        VerticalLayout topLeftLayout = new VerticalLayout();
        topLeftLayout.setWidthUndefined();
        topLeftLayout.setHeightUndefined();
        topLeftLayout.setSpacing(false);
        topLeftLayout.setMargin(new MarginInfo(false, false, false, false));
        topPanelLayout.addComponent(topLeftLayout);
        topPanelLayout.setExpandRatio(topLeftLayout, 70);

        HorizontalLayout upperTopLeftLayout = new HorizontalLayout();
        upperTopLeftLayout.setWidthUndefined();
        upperTopLeftLayout.setHeightUndefined();
        upperTopLeftLayout.setSpacing(true);
        topLeftLayout.addComponent(upperTopLeftLayout);

        Label overviewLabel = new Label("Overview &nbsp;&nbsp;");
        overviewLabel.setContentMode(ContentMode.HTML);
        upperTopLeftLayout.addComponent(overviewLabel);
        overviewLabel.setStyleName("subtitle");

        upperTopLeftLayout.addComponent(studiesPeptidesSwich);
        upperTopLeftLayout.setComponentAlignment(studiesPeptidesSwich, Alignment.TOP_LEFT);

        HorizontalLayout lowerLeftLayout = new HorizontalLayout();
        lowerLeftLayout.setHeightUndefined();
        lowerLeftLayout.setWidth("430px");
        lowerLeftLayout.setStyleName("lowerlink");
        lowerLeftLayout.addComponent(showSigneficantPeptidesOnly);
        lowerLeftLayout.setComponentAlignment(showSigneficantPeptidesOnly, Alignment.MIDDLE_RIGHT);
        topLeftLayout.addComponent(lowerLeftLayout);

        InfoPopupBtn info = new InfoPopupBtn("add text");
        info.setWidth("20px");
        info.setHeight("20px");
        topPanelLayout.addComponent(info);
        topPanelLayout.setComponentAlignment(info, Alignment.TOP_RIGHT);
         topPanelLayout.setExpandRatio(topLeftLayout, 30);

//        topPanelLayout.addComponent(showSigneficantPeptidesOnly);
//        topPanelLayout.setComponentAlignment(showSigneficantPeptidesOnly, Alignment.MIDDLE_RIGHT);
        mainStudiesLayout = new GridLayout(1, selectedComparisonList.size() + 1);
        this.addComponent(mainStudiesLayout);
        mainStudiesLayout.setWidthUndefined();
        mainStudiesLayout.setHeightUndefined();
        mainPeptidesLayout = new GridLayout(1, selectedComparisonList.size() + 1);
        this.addComponent(mainPeptidesLayout);
        mainPeptidesLayout.setWidthUndefined();
        mainPeptidesLayout.setHeightUndefined();
        mainPeptidesLayout.setVisible(false);

        this.width = width;
        this.diiseaseGroupsComparisonsProteinArr = proteinsComparisonsArr;
        defaultPeptidesExportInfoSet = new LinkedHashSet<ProteinInformationDataForExport>();
        orderedPeptidesExportInfoSet = new LinkedHashSet<ProteinInformationDataForExport>();
        proteinInformationDataForExportMap = new HashMap<String, ProteinInformationDataForExport>();

    }

    /**
     *
     * @param ordComparisonProteins
     */
    public void orderComparisons(DiseaseGroupsComparisonsProteinLayout[] ordComparisonProteins) {

        mainStudiesLayout.removeAllComponents();
        mainPeptidesLayout.removeAllComponents();
        int rowIndex = 0;
        for (final DiseaseGroupsComparisonsProteinLayout cp : ordComparisonProteins) {
            if (cp == null || cp.getSignificantTrindCategory() == -1) {
                continue;
            }
            ProteinStudyComparisonScatterPlotLayout studyCompLayout = studyCompLayoutMap.get(cp.getComparison());
            mainStudiesLayout.addComponent(studyCompLayout, 0, rowIndex);
            mainStudiesLayout.setComponentAlignment(studyCompLayout, Alignment.MIDDLE_CENTER);

            PeptidesComparisonsSequenceLayout peptideCompLayout = peptideCompLayoutMap.get(cp.getComparison());
            mainPeptidesLayout.addComponent(peptideCompLayout, 0, rowIndex);
            mainPeptidesLayout.setComponentAlignment(peptideCompLayout, Alignment.MIDDLE_CENTER);
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
            lastHeighlitedStudyLayout.highlight(false, clicked);
        }
        if (lastHeighlitedPeptideLayout != null) {
            lastHeighlitedPeptideLayout.highlight(false, clicked);
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
            int rowIndex = 0;
            for (DiseaseGroupsComparisonsProteinLayout cprot : diiseaseGroupsComparisonsProteinArr) {
                if (cprot == null || cprot.getSignificantTrindCategory() == -1) {
//                    QuantDiseaseGroupsComparison gc = (QuantDiseaseGroupsComparison) selectedComparisonList.toArray()[rowIndex];
//                    cprot = new DiseaseGroupsComparisonsProteinLayout(width, gc, -1);
                    continue;
                }
                ProteinStudyComparisonScatterPlotLayout protCompLayout = new ProteinStudyComparisonScatterPlotLayout(cprot, width, datasetExploringCentralSelectionManager);
                mainStudiesLayout.addComponent(protCompLayout, 0, rowIndex);
                mainStudiesLayout.setComponentAlignment(protCompLayout, Alignment.MIDDLE_CENTER);
                studyCompLayoutMap.put(cprot.getComparison(), protCompLayout);

                final DiseaseGroupsComparisonsProteinLayout gc = cprot;
                LayoutEvents.LayoutClickListener closeListener = new LayoutEvents.LayoutClickListener() {

                    private final QuantDiseaseGroupsComparison localComparison = gc.getComparison();

                    @Override
                    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                        Set<QuantDiseaseGroupsComparison> selectedComparisonList = datasetExploringCentralSelectionManager.getSelectedDiseaseGroupsComparisonList();
                        selectedComparisonList.remove(localComparison);
                        datasetExploringCentralSelectionManager.setDiseaseGroupsComparisonSelection(selectedComparisonList);
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
            defaultPeptidesExportInfoSet.clear();
            int rowIndex = 0;
            for (DiseaseGroupsComparisonsProteinLayout cprot : diiseaseGroupsComparisonsProteinArr) {
                if (cprot == null || cprot.getSignificantTrindCategory() == -1) {
//                    QuantDiseaseGroupsComparison gc = (QuantDiseaseGroupsComparison) selectedComparisonList.toArray()[rowIndex];
//                    cprot = new DiseaseGroupsComparisonsProteinLayout(width, gc, -1);
                    continue;
                }

                PeptidesComparisonsSequenceLayout protCompLayout = new PeptidesComparisonsSequenceLayout(cprot, width, datasetExploringCentralSelectionManager);
                mainPeptidesLayout.addComponent(protCompLayout, 0, rowIndex);
                mainPeptidesLayout.setComponentAlignment(protCompLayout, Alignment.MIDDLE_CENTER);
                peptideCompLayoutMap.put(cprot.getComparison(), protCompLayout);

                final DiseaseGroupsComparisonsProteinLayout gc = cprot;
                LayoutEvents.LayoutClickListener closeListener = new LayoutEvents.LayoutClickListener() {

                    private final QuantDiseaseGroupsComparison localComparison = gc.getComparison();

                    @Override
                    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                        Set<QuantDiseaseGroupsComparison> selectedComparisonList = datasetExploringCentralSelectionManager.getSelectedDiseaseGroupsComparisonList();
                        selectedComparisonList.remove(localComparison);
                        datasetExploringCentralSelectionManager.setDiseaseGroupsComparisonSelection(selectedComparisonList);
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
