/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.studies;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import probe.com.model.beans.quant.QuantDatasetObject;
import probe.com.model.beans.quant.QuantPeptide;
import probe.com.model.beans.quant.QuantProtein;
import probe.com.selectionmanager.DatasetExploringCentralSelectionManager;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.DiseaseGroupsComparisonsProteinLayout;
import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.peptidescontainer.ComparisonDetailsBean;
import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.peptidescontainer.StudyInformationPopupComponent;
import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.peptidescontainer.popupcomponents.PeptidesInformationOverviewLayout;

/**
 *
 * @author yfa041
 */
public class PeptidesComparisonsSequenceLayout extends GridLayout {

    private final Label comparisonTitle;
    private final VerticalLayout closeBtn;
//    private final AbsoluteLayout ProteinScatterPlotContainer;
    private final int coverageWidth;
//    private Map<String, QuantDatasetObject> dsKeyDatasetMap = new HashMap<String, QuantDatasetObject>();

    ;

    /**
     *
     * @return
     */
    public VerticalLayout getCloseBtn() {
        return closeBtn;
    }

    private final DatasetExploringCentralSelectionManager exploringFiltersManager;
    private final Map<String, PeptidesInformationOverviewLayout> peptidesInfoLayoutDSIndexMap = new HashMap<String, PeptidesInformationOverviewLayout>();
    private final Map<Integer, StudyPopupLayout> dsToStudyLayoutMap = new HashMap<Integer, StudyPopupLayout>();

    private boolean hasPTM= false;
    /**
     *
     * @param cprot
     * @param width
     * @param exploringFiltersManagerinst
     */
    public PeptidesComparisonsSequenceLayout(final DiseaseGroupsComparisonsProteinLayout cprot, int width, DatasetExploringCentralSelectionManager exploringFiltersManagerinst) {

        this.exploringFiltersManager = exploringFiltersManagerinst;
        this.setColumns(4);
        this.setRows(3);
        this.setWidthUndefined();
        this.setSpacing(true);
        this.setMargin(new MarginInfo(false, false, true, false));
//        HorizontalLayout topLayout = new HorizontalLayout();
//        topLayout.setWidth("100%");
//        topLayout.setHeight("20px");
//        topLayout.setStyleName(Reindeer.LAYOUT_WHITE);
//        int numb = cprot.getSignificantDown() + cprot.getNotProvided() + cprot.getNotReg() + cprot.getSignificantUp();
        comparisonTitle = new Label();
        comparisonTitle.setContentMode(ContentMode.HTML);
        comparisonTitle.setStyleName("custChartLabelHeader");
        comparisonTitle.setWidth((width - 70) + "px");
        this.addComponent(comparisonTitle, 1, 0);
        this.setComponentAlignment(comparisonTitle, Alignment.TOP_LEFT);

        closeBtn = new VerticalLayout();
        closeBtn.setWidth("20px");
        closeBtn.setHeight("20px");
        closeBtn.setStyleName("closebtn");
        this.addComponent(closeBtn, 2, 0);
        this.setComponentAlignment(closeBtn, Alignment.TOP_RIGHT);

        GridLayout proteinSequanceComparisonsContainer = new GridLayout(2, cprot.getComparison().getDatasetIndexes().length);
        this.addComponent(proteinSequanceComparisonsContainer, 1, 1);
        coverageWidth = (width - 100 - 100);
        VerticalLayout bottomSpacer = new VerticalLayout();
        bottomSpacer.setWidth((width - 100) + "px");
        bottomSpacer.setHeight("10px");
        bottomSpacer.setStyleName("dottedline");
        this.addComponent(bottomSpacer, 1, 2);

        proteinSequanceComparisonsContainer.setWidthUndefined();
        proteinSequanceComparisonsContainer.setHeightUndefined();
        proteinSequanceComparisonsContainer.setStyleName(Reindeer.LAYOUT_WHITE);
        proteinSequanceComparisonsContainer.setSpacing(true);

        Map<Integer, Set<QuantPeptide>> dsQuantPepMap = new HashMap<Integer, Set<QuantPeptide>>();
        for (QuantPeptide quantPep : cprot.getQuantPeptidesList()) {
            if (!dsQuantPepMap.containsKey(quantPep.getDsKey())) {
                Set<QuantPeptide> subList = new HashSet<QuantPeptide>();
                dsQuantPepMap.put(quantPep.getDsKey(), subList);
            }
            Set<QuantPeptide> subList = dsQuantPepMap.get(quantPep.getDsKey());
            subList.add(quantPep);
            dsQuantPepMap.put(quantPep.getDsKey(), subList);
        }

        int count = 0;

        int panelWidth = Page.getCurrent().getBrowserWindowWidth() - width - 100;
        final StudyInformationPopupComponent studyInformationPopupPanel = new StudyInformationPopupComponent(panelWidth, cprot.getProtName(), cprot.getUrl(), cprot.getComparison().getComparisonHeader());
        studyInformationPopupPanel.setVisible(false);

        LayoutEvents.LayoutClickListener studyListener = new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                Integer dsId;
                if (event.getComponent() instanceof AbsoluteLayout) {
                    dsId = (Integer) ((AbsoluteLayout) event.getComponent()).getData();
                } else {
                    dsId = (Integer) ((VerticalLayout) event.getComponent()).getData();
                }
                studyInformationPopupPanel.updateContent(dsToStudyLayoutMap.get(dsId));
            }
        };
        TreeSet<QuantProtein> orderSet = new TreeSet<QuantProtein>(cprot.getDsQuantProteinsMap().values());
        for (QuantProtein quantProtein : orderSet) {
            Label studyTitle = new Label("Study " + (count + 1));
            studyTitle.setStyleName("peptideslayoutlabel");
            studyTitle.setHeightUndefined();

            Label iconTitle = new Label("#Patients (" + (quantProtein.getPatientsGroupIINumber() + quantProtein.getPatientsGroupINumber()) + ")");
            iconTitle.setStyleName("peptideslayoutlabel");
            iconTitle.setHeightUndefined();
            if (quantProtein.getStringPValue().equalsIgnoreCase("Not Significant") || quantProtein.getStringFCValue().equalsIgnoreCase("Not regulated")) {
                iconTitle.setStyleName("notregicon");
            } else if (quantProtein.getStringFCValue().equalsIgnoreCase("Decreased")) {
                iconTitle.setStyleName("downarricon");
            } else {
                iconTitle.setStyleName("uparricon");
            }
            iconTitle.setDescription(cprot.getProteinAccssionNumber() + " : #Patients (" + (quantProtein.getPatientsGroupIINumber() + quantProtein.getPatientsGroupINumber()) + ")  " + quantProtein.getStringFCValue() + " " + quantProtein.getStringPValue() + "");

//            VerticalLayout spacer = new VerticalLayout();
//            spacer.setHeight("35px");
//            spacer.setWidth("50px");
//            spacer.setStyleName(Reindeer.LAYOUT_WHITE);
            VerticalLayout labelContainer = new VerticalLayout();
//            labelConta|iner.addComponent(spacer);
            labelContainer.addComponent(studyTitle);

            labelContainer.addComponent(iconTitle);

            proteinSequanceComparisonsContainer.addComponent(labelContainer, 0, count);
            proteinSequanceComparisonsContainer.setComponentAlignment(labelContainer, Alignment.TOP_CENTER);

            Map<Integer, ComparisonDetailsBean> patientGroupsNumToDsIdMap = new HashMap<Integer, ComparisonDetailsBean>();
            ComparisonDetailsBean pGr = new ComparisonDetailsBean();
            patientGroupsNumToDsIdMap.put((quantProtein.getPatientsGroupIINumber() + quantProtein.getPatientsGroupINumber()), pGr);
            QuantDatasetObject ds;

            ds = exploringFiltersManager.getFullQuantDatasetMap().get(quantProtein.getDsKey());

            StudyPopupLayout study = new StudyPopupLayout(panelWidth, quantProtein, ds, cprot.getProteinAccssionNumber(), cprot.getUrl(), cprot.getProtName());
            Set<QuantDatasetObject> qdsSet = new HashSet<QuantDatasetObject>();
            qdsSet.add(ds);
            study.setInformationData(qdsSet, cprot);
            dsToStudyLayoutMap.put(ds.getDsKey(), study);

            labelContainer.addLayoutClickListener(studyListener);
            labelContainer.setData(ds.getDsKey());

            if (dsQuantPepMap.get(quantProtein.getDsKey()) == null) {
                Label noPeptidesInfoLabel = new Label("No Peptides Information Available ");
                noPeptidesInfoLabel.setHeightUndefined();
                noPeptidesInfoLabel.setStyleName("peptideslayoutlabel");
//                VerticalLayout spacer2 = new VerticalLayout();
//                spacer2.setHeight("35px");
//                spacer2.setWidth("50px");
//                spacer2.setStyleName(Reindeer.LAYOUT_WHITE);

                VerticalLayout labelValueContainer = new VerticalLayout();
//                labelValueContainer.addComponent(spacer2);
                labelValueContainer.addComponent(noPeptidesInfoLabel);
                labelValueContainer.addLayoutClickListener(studyListener);
                labelValueContainer.setData(ds.getDsKey());

                proteinSequanceComparisonsContainer.addComponent(labelValueContainer, 1, count);
                proteinSequanceComparisonsContainer.setComponentAlignment(labelValueContainer, Alignment.TOP_CENTER);
                count++;
                continue;
            }

            String key = "-" + quantProtein.getDsKey() + "-" + cprot.getProteinAccssionNumber() + "-";
            PeptidesInformationOverviewLayout peptideInfoLayout = new PeptidesInformationOverviewLayout(cprot.getSequance(), dsQuantPepMap.get(quantProtein.getDsKey()), coverageWidth, true, studyListener, ds.getDsKey());
            hasPTM = peptideInfoLayout.isHasPTM();
            peptidesInfoLayoutDSIndexMap.put(key, peptideInfoLayout);
            proteinSequanceComparisonsContainer.addComponent(peptideInfoLayout, 1, count);
            count++;

        }

        comparisonTitle.setValue(cprot.getComparison().getComparisonHeader() + " (#Studies " + count + "/" + cprot.getComparison().getDatasetIndexes().length + ")");
//        ProteinScatterPlotContainer = new AbsoluteLayout();
//
//        this.addComponent(ProteinScatterPlotContainer, 1, 1);
//        ProteinScatterPlotContainer.setWidth(coverageWidth + "px");
//        ProteinScatterPlotContainer.setHeight(150 + "px");
//        teststyle = "_" + cprot.getProteinAccssionNumber().replace(" ", "_").replace(")", "_").replace("(", "_").toLowerCase() + "_" + cprot.getComparison().getComparisonHeader().replace(" ", "_").replace(")", "_").replace("(", "_").toLowerCase().replace("/", "_") + "_scatterplot";
//        styles.add("." + teststyle + " {  background-image: url(" + defaultScatterPlottImgUrl + " );background-position:center; background-repeat: no-repeat; }");
//        ProteinScatterPlotContainer.setStyleName(teststyle);
//        ProteinScatterPlotContainer.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
//            @Override
//            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
//
//                if (event.getClickedComponent() instanceof SquaredDot) {
//                    SquaredDot dot = (SquaredDot) event.getClickedComponent();
//                    int trend = (Integer) dot.getParam("trend");
//                    int pGrNumber = (Integer) dot.getParam("pGrNumber");
//                    exploringFiltersManager.setSelectedDataset(patientGroupsNumToDsIdMap.get(pGrNumber).getRegulatedList(trend));
//                    int[] dssArr = new int[patientGroupsNumToDsIdMap.get(pGrNumber).getRegulatedList(trend).size()];
//                    for (int x = 0; x < dssArr.length; x++) {
//                        dssArr[x] = patientGroupsNumToDsIdMap.get(pGrNumber).getRegulatedList(trend).get(x);
//                    }
//                    exploringFiltersManager.setStudyLevelFilterSelection(new CSFFilterSelection("Study_Selection", dssArr, "scatter", null));//  
//                    Set<QuantDatasetObject> dsObjects = new HashSet<QuantDatasetObject>();
//                    for (int dsId : dssArr) {
//                        dsObjects.add(exploringFiltersManager.getFullQuantDatasetMap().get(dsId));
//                    }
//                    studyPopupLayoutManager.updateSelectedProteinInformation(pGrNumber, trend, dsObjects, cprot);
//                }
//            }
//        });
    }

    /**
     *
     * @return
     */
    public Label getComparisonTitle() {
        return comparisonTitle;
    }
     public boolean isHasPTM() {
        return hasPTM;
    }
    private boolean isclicked;

    /**
     *
     * @param heighlight
     * @param clicked
     */
    public void highlight(boolean heighlight, boolean clicked) {

        if (heighlight) {
            this.setStyleName("highlightlayout");

        } else if (!heighlight) {
            if (!isclicked) {
                this.setStyleName(Reindeer.LAYOUT_WHITE);
//                styles.add("." + teststyle + " {  background-image: url(" + defaultScatterPlottImgUrl + " );background-position:center; background-repeat: no-repeat; }");
            } else {
                isclicked = false;
            }
        }
        if (clicked) {
            isclicked = true;
            if (this.getParent().isVisible()) {
                this.getUI().scrollIntoView(this);
            }
        }

    }

    public void showPTM(boolean show) {
        for (PeptidesInformationOverviewLayout peptideInfoLayout : peptidesInfoLayoutDSIndexMap.values()) {
            peptideInfoLayout.showPtms(show);

        }

    }

    public void showSignificantRegulationOnly(boolean show) {
        for (PeptidesInformationOverviewLayout peptideInfoLayout : peptidesInfoLayoutDSIndexMap.values()) {
            peptideInfoLayout.showSignificantRegulationOnly(show);

        }

    }
}
