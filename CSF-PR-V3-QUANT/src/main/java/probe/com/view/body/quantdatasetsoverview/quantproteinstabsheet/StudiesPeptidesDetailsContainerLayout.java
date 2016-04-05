package probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet;

import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.studies.ProteinStudyComparisonScatterPlotLayout;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import java.util.HashMap;
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
    private final GridLayout mainStudiesLayout, mainPeptidesLayout, popupStudyContainer;
    ;
    private final Panel studiesPanel, peptidesPanel;
    private final QuantCentralManager Quant_Central_Manager;
    private final int width;
    private final DiseaseGroupsComparisonsProteinLayout[] diiseaseGroupsComparisonsProteinArr;
//    private final OptionGroup studiesPeptidesSwich = new OptionGroup();
//    private final OptionGroup showSigneficantPeptidesOnly;
    private final Set<ProteinInformationDataForExport> defaultPeptidesExportInfoSet, orderedPeptidesExportInfoSet;
    private final Map<String, ProteinInformationDataForExport> proteinInformationDataForExportMap;
    private final int custTrend;
    private final HorizontalLayout peptidesBtnWrapper, studiesBtnWrapper;
    private final TabSheet studiesPeptidesTabsheet;
//    private final HorizontalLayout  peptidesControlBtnsLayout;
    private final VerticalLayout showPTMBtn, significantPeptidesBtn;

    public Set<ProteinInformationDataForExport> getOrderedPeptidesExportInfoSet() {
        return orderedPeptidesExportInfoSet;
    }

    /**
     *
     * @param proteinsComparisonsArr
     * @param selectedComparisonList
     * @param Quant_Central_Manager
     * @param width
     * @param height
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
        this.width = width - 30;

        HorizontalLayout tapsheetLayoutContainer = new HorizontalLayout();
        tapsheetLayoutContainer.setHeight(height + "px");
        tapsheetLayoutContainer.setWidth("100%");
        tapsheetLayoutContainer.setStyleName(Reindeer.LAYOUT_WHITE);
        this.addComponent(tapsheetLayoutContainer);

        studiesPeptidesTabsheet = new TabSheet();
        tapsheetLayoutContainer.addComponent(studiesPeptidesTabsheet);
        tapsheetLayoutContainer.setComponentAlignment(studiesPeptidesTabsheet, Alignment.TOP_LEFT);
        studiesPeptidesTabsheet.setStyleName(Reindeer.TABSHEET_MINIMAL);
        studiesPeptidesTabsheet.setWidth(100 + "%");
        studiesPeptidesTabsheet.setHeight(height + "px");

        mainStudiesLayout = new GridLayout(1, selectedComparisonList.size() + 1);
        mainStudiesLayout.setMargin(new MarginInfo(true, false, false, false));
        mainStudiesLayout.setWidth("100%");
        mainStudiesLayout.setHeightUndefined();

        studiesPanel = new Panel(mainStudiesLayout);
        studiesPanel.setStyleName(Reindeer.PANEL_LIGHT);
        studiesPanel.setHeight(100 + "%");
        studiesPeptidesTabsheet.addTab(studiesPanel, "Datasets");

        //end of studies scatter plot container
        //init peptides layout container
        popupStudyContainer = new GridLayout(2, selectedComparisonList.size() + 1);
        popupStudyContainer.setMargin(new MarginInfo(true, false, false, false));
        popupStudyContainer.setWidth((100) + "%");
        popupStudyContainer.setHeightUndefined();

        mainPeptidesLayout = new GridLayout(1, selectedComparisonList.size() + 1);
        mainPeptidesLayout.setMargin(new MarginInfo(true, false, false, false));
        mainPeptidesLayout.setWidth((100) + "%");
        mainPeptidesLayout.setHeightUndefined();

        peptidesPanel = new Panel(mainPeptidesLayout);
        peptidesPanel.setStyleName(Reindeer.PANEL_LIGHT);
        peptidesPanel.setHeight((100) + "%");
        studiesPeptidesTabsheet.addTab(peptidesPanel, "Peptides");

        // end of peptides layout
        //init bottom layout 
        //init overlayout (legend) 
        HorizontalLayout bottomLayout = new HorizontalLayout();
        bottomLayout.setHeight("24px");
        bottomLayout.setWidth("100%");
        this.addComponent(bottomLayout);

        peptidesBtnWrapper = new HorizontalLayout();
        peptidesBtnWrapper.setWidthUndefined();
        peptidesBtnWrapper.setStyleName(Reindeer.LAYOUT_WHITE);
        peptidesBtnWrapper.setVisible(false);
        bottomLayout.addComponent(peptidesBtnWrapper);
        bottomLayout.setComponentAlignment(peptidesBtnWrapper, Alignment.BOTTOM_RIGHT);
        peptidesBtnWrapper.setSpacing(true);

        TrendLegend legend = new TrendLegend("ministackedpeptidessequence");

        legend.setSpacing(true);
        peptidesBtnWrapper.addComponent(legend);

        significantPeptidesBtn = new VerticalLayout();
        significantPeptidesBtn.setWidth("24px");
        significantPeptidesBtn.setHeight("24px");
        significantPeptidesBtn.setStyleName("allpeptidesseqbtn");
        significantPeptidesBtn.setDescription("Show all peptides (significant, not significant and stable)");
        significantPeptidesBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                if (significantPeptidesBtn.getStyleName().equalsIgnoreCase("allpeptidesseqbtn")) {
                    significantPeptidesBtn.setStyleName("sigpeptidesseqbtn");
                    significantPeptidesBtn.setDescription("Show significant peptides only");
                    showSignificantRegulationOnly(true);
                } else {
                    significantPeptidesBtn.setStyleName("allpeptidesseqbtn");
                    showSignificantRegulationOnly(false);
                    significantPeptidesBtn.setDescription("Show all peptides (significant, not significant and stable)");
                }
            }
        });
        peptidesBtnWrapper.addComponent(significantPeptidesBtn);

        showPTMBtn = new VerticalLayout();
        showPTMBtn.setWidth("24px");
        showPTMBtn.setHeight("24px");
        showPTMBtn.setStyleName("ptm_selected_btn");
        showPTMBtn.setDescription("Show/hide PTM");
        showPTMBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                if (showPTMBtn.getStyleName().equalsIgnoreCase("ptm_selected_btn")) {
                    showPTMBtn.setStyleName("ptm_unselected_btn");
                    showPTM(false);
                } else {
                    showPTMBtn.setStyleName("ptm_selected_btn");
                    showPTM(true);
                }
            }
        });
        peptidesBtnWrapper.addComponent(showPTMBtn);

        studiesBtnWrapper = new HorizontalLayout();
        studiesBtnWrapper.setWidthUndefined();
        studiesBtnWrapper.setStyleName(Reindeer.LAYOUT_WHITE);
        studiesBtnWrapper.setVisible(true);
        studiesBtnWrapper.setSpacing(true);
        bottomLayout.addComponent(studiesBtnWrapper);
        bottomLayout.setComponentAlignment(studiesBtnWrapper, Alignment.BOTTOM_RIGHT);

        TrendLegend studieslegend = new TrendLegend("miniscatterpeptidessequence");
        studieslegend.setSpacing(true);
        studiesBtnWrapper.addComponent(studieslegend);

        final VerticalLayout maxmizeView = new VerticalLayout();
        maxmizeView.setWidth("24px");
        maxmizeView.setHeight("24px");
        maxmizeView.setStyleName("maxmizebtn");
        maxmizeView.setDescription("Show all charts in popup panel");
        maxmizeView.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                popupStudiesChart();
            }
        });
        studiesBtnWrapper.addComponent(maxmizeView);

        studiesPeptidesTabsheet.addSelectedTabChangeListener(new TabSheet.SelectedTabChangeListener() {

            @Override
            public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
                String c = studiesPeptidesTabsheet.getTab(event.getTabSheet().getSelectedTab()).getCaption();
                if (c.equalsIgnoreCase("Peptides")) {
                    peptidesBtnWrapper.setVisible(true);
                    studiesBtnWrapper.setVisible(false);
                } else {
                    peptidesBtnWrapper.setVisible(false);
                    studiesBtnWrapper.setVisible(true);
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
        mainPeptidesLayout.removeAllComponents();

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
//            mainStudiesLayout.addComponent(studiesOverviewTopSpacer, 0, 0);
            int rowIndex = 1;
            int rowIndexII = 0;
            int colIndexII = 0;

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
                        event.getComponent().getParent().setVisible(false);
                        popupStudyContainer.removeComponent(event.getComponent().getParent());                        
                        if (popupStudyContainer.getComponentCount() == 0 && fullStudiesPopupWindow != null) {
                            Quant_Central_Manager.getSelectedDiseaseGroupsComparisonList();
                            UI.getCurrent().removeWindow(fullStudiesPopupWindow);
                        }
                        

                    }
                };
                protCompLayout.getCloseBtn().addLayoutClickListener(closeListener);
                ProteinStudyComparisonScatterPlotLayout protCompLayoutII = new ProteinStudyComparisonScatterPlotLayout(Quant_Central_Manager, cprot, width, custTrend);
                popupStudyContainer.addComponent(protCompLayoutII, colIndexII++, rowIndexII);
                protCompLayoutII.getCloseBtn().addLayoutClickListener(closeListener);
                protCompLayoutII.redrawChart();
                if (colIndexII == 2) {
                    colIndexII = 0;
                    rowIndexII++;

                }

                rowIndex++;
            }

        }
        for (ProteinStudyComparisonScatterPlotLayout layout : studyCompLayoutMap.values()) {
            layout.redrawChart();
        }
        if (peptideCompLayoutMap.isEmpty()) {
//            mainPeptidesLayout.addComponent(peptidesControlBtnsLayout, 0, 0);
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

            showPTMBtn.setEnabled(false);
            showPTMBtn.setStyleName("ptm_selected_btn");
            for (PeptidesComparisonsSequenceLayout protCompLayout : peptideCompLayoutMap.values()) {
                if (protCompLayout.isHasPTM()) {
                    showPTMBtn.setEnabled(true);
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

    private Window fullStudiesPopupWindow;
    private VerticalLayout popupBody;

    private void popupStudiesChart() {

        if (fullStudiesPopupWindow == null) {
            int height = Page.getCurrent().getBrowserWindowHeight() - 100;
            int width = Page.getCurrent().getBrowserWindowWidth() - 100;
            popupBody = new VerticalLayout();
            popupBody.setWidth((width) + "px");
            popupBody.setHeightUndefined();
            popupBody.setStyleName(Reindeer.LAYOUT_WHITE);
            fullStudiesPopupWindow = new Window() {

                @Override
                public void close() {
                    fullStudiesPopupWindow.setVisible(false);
                }

            };
            fullStudiesPopupWindow.setCaption("<font color='gray' style='font-weight: bold;!important'><a href='" + "google.com" + "'target=\"_blank\"> " + "protein name" + " </a></font>");
            fullStudiesPopupWindow.setContent(popupBody);
            fullStudiesPopupWindow.setWindowMode(WindowMode.NORMAL);
            fullStudiesPopupWindow.setWidth((width + 40) + "px");
            fullStudiesPopupWindow.setHeight((height) + "px");
            fullStudiesPopupWindow.setVisible(false);
            fullStudiesPopupWindow.setResizable(false);
            fullStudiesPopupWindow.setClosable(false);
            fullStudiesPopupWindow.setStyleName(Reindeer.WINDOW_LIGHT);
            fullStudiesPopupWindow.setModal(true);
            fullStudiesPopupWindow.setDraggable(true);
            UI.getCurrent().addWindow(fullStudiesPopupWindow);
            
            fullStudiesPopupWindow.setPositionX(30);
            fullStudiesPopupWindow.setPositionY(40);
            fullStudiesPopupWindow.setCaptionAsHtml(true);
            fullStudiesPopupWindow.setClosable(true);
            popupBody.setMargin(true);
            popupBody.setSpacing(true);
            popupBody.addComponent(popupStudyContainer);
        }

        fullStudiesPopupWindow.setVisible(true);

    }
}
