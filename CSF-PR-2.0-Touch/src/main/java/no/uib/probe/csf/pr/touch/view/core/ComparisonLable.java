/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.logic.beans.QuantProtein;

/**
 *
 * @author Yehia Farag
 *
 * this class represents the label for comparison name
 */
public abstract class ComparisonLable extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private final Object itemId;
    private final PopupView datasetInfoPopup;

    private final Accordion datalayout;

    public ComparisonLable(QuantDiseaseGroupsComparison comparison, Object itemId, QuantProtein quantProtein, QuantDatasetObject qds) {
        this.itemId = itemId;
        this.setWidth(100, Unit.PERCENTAGE);

        this.setHeightUndefined();
        this.setSpacing(true);
        this.addLayoutClickListener(ComparisonLable.this);
        String[] headerI = comparison.getComparisonHeader().replace("__" + comparison.getDiseaseCategory(), "").split(" / ");
        String diseaseColor = comparison.getDiseaseCategoryColor();

        Label labelI = new Label("<font  style=' color:" + diseaseColor + "'>" + headerI[0] + "</font>");
        labelI.setWidth(100, Unit.PERCENTAGE);
        labelI.addStyleName(ValoTheme.LABEL_SMALL);
        labelI.addStyleName(ValoTheme.LABEL_TINY);
        labelI.setHeight(15, Unit.PIXELS);
        labelI.addStyleName("overflowtext");
        labelI.setContentMode(ContentMode.HTML);
        this.addComponent(labelI);

        VerticalLayout spacer = new VerticalLayout();
        spacer.setStyleName(ValoTheme.LAYOUT_WELL);
        spacer.setWidth(100, Unit.PERCENTAGE);
        spacer.setHeight(2, Unit.PIXELS);
        spacer.setMargin(new MarginInfo(false, true, false, true));
        this.addComponent(spacer);
        this.setComponentAlignment(spacer, Alignment.MIDDLE_CENTER);

        Label labelII = new Label("<font  style='color:" + diseaseColor + "'>" + headerI[1] + "</font>");
        labelII.setHeight(15, Unit.PIXELS);
        labelII.setWidth(100, Unit.PERCENTAGE);
        labelII.addStyleName("overflowtext");
        labelII.addStyleName(ValoTheme.LABEL_SMALL);
        labelII.addStyleName(ValoTheme.LABEL_TINY);
        labelII.setContentMode(ContentMode.HTML);
        this.addComponent(labelII);
        this.setDescription(comparison.getComparisonFullName());

        VerticalLayout popupbodyLayout = new VerticalLayout();
        popupbodyLayout.setSpacing(true);
        popupbodyLayout.setWidth(1000, Unit.PIXELS);
        popupbodyLayout.setMargin(new MarginInfo(false, false, true, false));
        popupbodyLayout.addStyleName("border");

        HorizontalLayout headerIContainer = new HorizontalLayout();
        headerIContainer.setWidth(100, Unit.PERCENTAGE);
        popupbodyLayout.addComponent(headerIContainer);
        CloseButton closePopup = new CloseButton();
        closePopup.setWidth(10, Unit.PIXELS);
        closePopup.setHeight(10, Unit.PIXELS);
        headerIContainer.addComponent(closePopup);
        headerIContainer.setComponentAlignment(closePopup, Alignment.TOP_RIGHT);
        closePopup.addStyleName("translateleft10");

        VerticalLayout accrWrapper = new VerticalLayout();
        accrWrapper.setHeight(880, Unit.PIXELS);
        accrWrapper.setWidth(100, Unit.PERCENTAGE);
        popupbodyLayout.addComponent(accrWrapper);
        datalayout = new Accordion();
        accrWrapper.addComponent(datalayout);
        datalayout.setTabCaptionsAsHtml(true);
        datalayout.setWidth(100, Unit.PERCENTAGE);

//
//        Label titleI = new Label("Protein");
//        titleI.setStyleName(ValoTheme.LABEL_BOLD);
//        headerIContainer.addComponent(titleI);
        ProteinsInformationOverviewLayout proteinInfoLayout = new ProteinsInformationOverviewLayout(980);
        proteinInfoLayout.updateProteinsForm(quantProtein, quantProtein.getUniprotAccession(), null, quantProtein.getUniprotProteinName());
        datalayout.addTab(proteinInfoLayout, "<b>Protein</b>");

//        Label titleII = new Label("Dataset");
//        titleII.setStyleName(ValoTheme.LABEL_BOLD);
//        popupbodyLayout.addComponent(titleII);
        DatasetInformationOverviewLayout dsOverview = new DatasetInformationOverviewLayout(qds);
        dsOverview.getDatasetInfoForm().setWidth(980, Unit.PIXELS);
        datalayout.addTab(dsOverview.getDatasetInfoForm(), "<b>Dataset</b>");

//        popupbodyLayout.addComponent(dsOverview.getDatasetInfoForm());
        datasetInfoPopup = new PopupView(null, popupbodyLayout) {

            @Override
            public void setPopupVisible(boolean visible) {
                this.setVisible(visible);
                super.setPopupVisible(visible); //To change body of generated methods, choose Tools | Templates.
            }

        };
        closePopup.addLayoutClickListener((LayoutEvents.LayoutClickEvent event) -> {
            datasetInfoPopup.setPopupVisible(false);
        });
        datasetInfoPopup.setVisible(false);
        datasetInfoPopup.setCaptionAsHtml(true);
        datasetInfoPopup.setHideOnMouseOut(false);
        this.addComponent(datasetInfoPopup);
        datasetInfoPopup.addStyleName("margin20");

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        datasetInfoPopup.setPopupVisible(true);
//        select(itemId);
    }

    public abstract void select(Object itemId);

}
