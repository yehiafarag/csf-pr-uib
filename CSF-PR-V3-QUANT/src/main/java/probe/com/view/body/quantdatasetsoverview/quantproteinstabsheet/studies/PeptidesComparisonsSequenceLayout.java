/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.studies;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import probe.com.model.beans.quant.QuantDatasetObject;
import probe.com.model.beans.quant.QuantPeptide;
import probe.com.model.beans.quant.QuantProtein;
import probe.com.model.util.vaadintoimageutil.peptideslayout.StudyInfoData;
import probe.com.selectionmanager.QuantCentralManager;
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
    private Map<String, StudyInfoData> studiesMap;

    /**
     *
     * @return
     */
    public VerticalLayout getCloseBtn() {
        return closeBtn;
    }

    private final QuantCentralManager Quant_Central_Manager;
    private final Map<String, PeptidesInformationOverviewLayout> peptidesInfoLayoutDSIndexMap = new HashMap<String, PeptidesInformationOverviewLayout>();
    private final Map<Integer, StudyPopupLayout> dsToStudyLayoutMap = new HashMap<Integer, StudyPopupLayout>();

    private boolean hasPTM = false;

    /**
     *
     * @param cprot
     * @param width
     * @param Quant_Central_Manager
     */
    public PeptidesComparisonsSequenceLayout(QuantCentralManager Quant_Central_Manager, final DiseaseGroupsComparisonsProteinLayout cprot, int width) {
        this.studiesMap = new LinkedHashMap<String, StudyInfoData>();
        this.Quant_Central_Manager = Quant_Central_Manager;
        this.setColumns(4);
        this.setRows(3);
        this.setWidthUndefined();
        this.setSpacing(true);
        this.setMargin(new MarginInfo(false, false, true, false));
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

        GridLayout proteinSequenceComparisonsContainer = new GridLayout(2, cprot.getComparison().getDatasetIndexes().length);
        this.addComponent(proteinSequenceComparisonsContainer, 1, 1);
        coverageWidth = (width - 100 - 180);
        VerticalLayout bottomSpacer = new VerticalLayout();
        bottomSpacer.setWidth((width - 100) + "px");
        bottomSpacer.setHeight("10px");
        bottomSpacer.setStyleName("dottedline");
        this.addComponent(bottomSpacer, 1, 2);

        proteinSequenceComparisonsContainer.setWidthUndefined();
        proteinSequenceComparisonsContainer.setHeightUndefined();
        proteinSequenceComparisonsContainer.setStyleName(Reindeer.LAYOUT_WHITE);
        proteinSequenceComparisonsContainer.setSpacing(true);

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
            StudyInfoData exportData = new StudyInfoData();
            exportData.setCoverageWidth(coverageWidth);
            Label studyTitle = new Label();//"Study " + (count + 1));
            studyTitle.setStyleName("peptideslayoutlabel");
            studyTitle.setHeightUndefined();
            studyTitle.setWidth("200px");

            Label iconTitle = new Label("#Patients (" + (quantProtein.getPatientsGroupIINumber() + quantProtein.getPatientsGroupINumber()) + ")");
            exportData.setSubTitle(iconTitle.getValue());
            iconTitle.setStyleName("peptideslayoutlabel");
            iconTitle.setHeightUndefined();
            if (quantProtein.getStringPValue().equalsIgnoreCase("Not Significant") || quantProtein.getStringFCValue().equalsIgnoreCase("Not regulated")) {
                iconTitle.setStyleName("notregicon");
                exportData.setTrend(0);
            } else if (quantProtein.getStringFCValue().equalsIgnoreCase("Decreased")) {
                iconTitle.setStyleName("downarricon");
                exportData.setTrend(-1);
            } else {
                exportData.setTrend(1);
                iconTitle.setStyleName("uparricon");
            }
            iconTitle.setDescription(cprot.getProteinAccssionNumber() + " : #Patients (" + (quantProtein.getPatientsGroupIINumber() + quantProtein.getPatientsGroupINumber()) + ")  " + quantProtein.getStringFCValue() + " " + quantProtein.getStringPValue() + "");

            VerticalLayout labelContainer = new VerticalLayout();
            labelContainer.addComponent(studyTitle);

            labelContainer.addComponent(iconTitle);

            proteinSequenceComparisonsContainer.addComponent(labelContainer, 0, count);
            proteinSequenceComparisonsContainer.setComponentAlignment(labelContainer, Alignment.TOP_CENTER);

            Map<Integer, ComparisonDetailsBean> patientGroupsNumToDsIdMap = new HashMap<Integer, ComparisonDetailsBean>();
            ComparisonDetailsBean pGr = new ComparisonDetailsBean();
            patientGroupsNumToDsIdMap.put((quantProtein.getPatientsGroupIINumber() + quantProtein.getPatientsGroupINumber()), pGr);
            QuantDatasetObject ds;

            ds = Quant_Central_Manager.getFullQuantDatasetMap().get(quantProtein.getDsKey());

            StudyPopupLayout study = new StudyPopupLayout(panelWidth, quantProtein, ds, cprot.getProteinAccssionNumber(), cprot.getUrl(), cprot.getProtName());
            Set<QuantDatasetObject> qdsSet = new HashSet<QuantDatasetObject>();
            qdsSet.add(ds);
            study.setInformationData(qdsSet, cprot);
            dsToStudyLayoutMap.put(ds.getDsKey(), study);

            labelContainer.addLayoutClickListener(studyListener);
            labelContainer.setData(ds.getDsKey());
            studyTitle.setValue("(" + (count + 1) + ")" + ds.getAuthor());
            exportData.setTitle(ds.getAuthor());

            if (dsQuantPepMap.get(quantProtein.getDsKey()) == null) {
                Label noPeptidesInfoLabel = new Label("No Peptides Information Available ");
                noPeptidesInfoLabel.setHeightUndefined();
                noPeptidesInfoLabel.setStyleName("peptideslayoutlabel");
                VerticalLayout labelValueContainer = new VerticalLayout();
                labelValueContainer.addComponent(noPeptidesInfoLabel);
                labelValueContainer.addLayoutClickListener(studyListener);
                labelValueContainer.setData(ds.getDsKey());

                proteinSequenceComparisonsContainer.addComponent(labelValueContainer, 1, count);
                proteinSequenceComparisonsContainer.setComponentAlignment(labelValueContainer, Alignment.TOP_CENTER);
                count++;
                studiesMap.put((count + 1) + ds.getAuthor(), exportData);
                continue;
            }

            String key = "-" + quantProtein.getDsKey() + "-" + cprot.getProteinAccssionNumber() + "-";
            PeptidesInformationOverviewLayout peptideInfoLayout = new PeptidesInformationOverviewLayout(cprot.getSequence(), dsQuantPepMap.get(quantProtein.getDsKey()), coverageWidth, true, studyListener, ds.getDsKey());
            exportData.setPeptidesInfoList(peptideInfoLayout.getStackedPeptides());
            exportData.setLevelsNumber(peptideInfoLayout.getLevel());
            hasPTM = peptideInfoLayout.isHasPTM();
            peptidesInfoLayoutDSIndexMap.put(key, peptideInfoLayout);
            proteinSequenceComparisonsContainer.addComponent(peptideInfoLayout, 1, count);
            count++;
            studiesMap.put((count + 1) + ds.getAuthor(), exportData);

        }

        comparisonTitle.setValue(cprot.getComparison().getComparisonHeader() + " (#Studies " + count + "/" + cprot.getComparison().getDatasetIndexes().length + ")");
    }

    /**
     *
     * @return
     */
    public String getComparisonTitleValue() {
        return comparisonTitle.getValue();
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

    public Map<String, StudyInfoData> getStudiesMap() {
        return studiesMap;
    }
}
