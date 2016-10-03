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
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.logic.beans.QuantProtein;
import no.uib.probe.csf.pr.touch.view.bigscreen.popupwindows.StudiesInformationWindow;

/**
 *
 * @author Yehia Farag
 *
 * this class represents the label for comparison name
 */
public abstract class ComparisonLable extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private final PopupWindow popupWindow;
//    private final DatasetButtonsContainerLayout studiesPopupLayout;

//    private final PopupView datasetInfoPopup;
//
//    private final Accordion datalayout;
    public ComparisonLable(QuantDiseaseGroupsComparison comparison, Object itemId, QuantProtein quantProtein, QuantDatasetObject qds, boolean smallScreen) {

        this.setWidth(100, Unit.PERCENTAGE);

        this.setHeightUndefined();
        this.setSpacing(false);
        this.addStyleName("pointer");
        this.addLayoutClickListener(ComparisonLable.this);
        String[] headerI = comparison.getComparisonHeader().replace("__" + comparison.getDiseaseCategory(), "").split(" / ");
        String diseaseColor = comparison.getDiseaseCategoryColor();

        Label labelI = new Label("<font  style=' color:" + diseaseColor + "'>" + headerI[0] + "</font>");
        labelI.setWidth(100, Unit.PERCENTAGE);
        labelI.addStyleName(ValoTheme.LABEL_SMALL);
        labelI.addStyleName(ValoTheme.LABEL_TINY);
//        labelI.setHeight(15, Unit.PIXELS);
        labelI.addStyleName("overflowtext");
        labelI.addStyleName("nomargin");
//        labelI.addStyleName("alignmiddle");
        labelI.setContentMode(ContentMode.HTML);
        this.addComponent(labelI);
        this.setComponentAlignment(labelI, Alignment.BOTTOM_CENTER);

        VerticalLayout spacer = new VerticalLayout();
        spacer.setStyleName(ValoTheme.LAYOUT_WELL);
        spacer.setWidth(100, Unit.PERCENTAGE);
        spacer.setHeight(2, Unit.PIXELS);
        spacer.setMargin(new MarginInfo(false, true, false, true));
        spacer.addStyleName("nomargin");
        spacer.addStyleName("alignmiddle");
        spacer.addStyleName("margintop-5");
        this.addComponent(spacer);
        this.setComponentAlignment(spacer, Alignment.MIDDLE_CENTER);

        Label labelII = new Label("<font  style='color:" + diseaseColor + "'>" + headerI[1] + "</font>");
//        labelII.setHeight(15, Unit.PIXELS);
        labelII.setWidth(100, Unit.PERCENTAGE);
        labelII.addStyleName("overflowtext");
        labelII.addStyleName("nomargin");
//        labelII.addStyleName("alignmiddle");
        labelII.addStyleName(ValoTheme.LABEL_SMALL);
        labelII.addStyleName(ValoTheme.LABEL_TINY);
        labelII.setContentMode(ContentMode.HTML);
        this.addComponent(labelII);
        this.setComponentAlignment(labelII, Alignment.TOP_CENTER);
        
        String[] gr = comparison.getComparisonFullName().replace("__" + comparison.getDiseaseCategory(), "").split(" / ");
                String updatedHeader = ("Numerator: " + gr[0] + "<br/>Denominator: " + gr[1]+ "<br/>Disease: " + comparison.getDiseaseCategory());  
        this.setDescription("View comparison details <br/>" +updatedHeader);

        VerticalLayout popupBody = new VerticalLayout();
        popupBody.setWidth(100, Unit.PERCENTAGE);
        popupBody.setHeight(100, Unit.PERCENTAGE);

        popupBody.setMargin(false);
        popupBody.setSpacing(true);
        popupBody.addStyleName("roundedborder");
        popupBody.addStyleName("whitelayout");
        if (smallScreen) {
            popupBody.addStyleName("padding2");
        } else {
            popupBody.addStyleName("padding20");
        }
        VerticalLayout frame = new VerticalLayout();
        frame.setWidth(99, Unit.PERCENTAGE);
        frame.setHeight(99, Unit.PERCENTAGE);
        frame.setSpacing(true);
        frame.addComponent(popupBody);
        String protName = quantProtein.getUniprotProteinName().trim();
        if (protName.equalsIgnoreCase("")) {
            protName = quantProtein.getPublicationProteinName();
        }
        String title = "Dataset and Protein Information (" + protName + ")";

        popupWindow = new PopupWindow(frame, title) {

            @Override
            public void close() {
                popupWindow.setVisible(false);

            }

            @Override
            public void setVisible(boolean visible) {

                if (visible) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }
                }
                super.setVisible(visible); //To change body of generated methods, choose Tools | Templates.
            }

        };
        if (smallScreen) {
            popupWindow.setWidth(popupWindow.getWidth() + 100, popupWindow.getWidthUnits());

            popupWindow.setHeight(popupWindow.getHeight() + 100, popupWindow.getHeightUnits());

        }

        TabSheet tab = new TabSheet();
        tab.setHeight(98.0f, Unit.PERCENTAGE);
        tab.setWidth(100.0f, Unit.PERCENTAGE);
        tab.addStyleName(ValoTheme.TABSHEET_FRAMED);
        tab.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        tab.addStyleName("transparentframe");
        popupBody.addComponent(tab);
        popupBody.setComponentAlignment(tab, Alignment.TOP_CENTER);

//        VerticalLayout popupbodyLayout = new VerticalLayout();
//        popupbodyLayout.setSpacing(true);
//        popupbodyLayout.setWidth(100, Unit.PERCENTAGE);
//        popupbodyLayout.setMargin(new MarginInfo(false, false, true, false));
//        popupbodyLayout.addStyleName("border");
//
//        HorizontalLayout headerIContainer = new HorizontalLayout();
//        headerIContainer.setWidth(100, Unit.PERCENTAGE);
//        popupbodyLayout.addComponent(headerIContainer);
//        CloseButton closePopup = new CloseButton();
//        closePopup.setWidth(10, Unit.PIXELS);
//        closePopup.setHeight(10, Unit.PIXELS);
//        headerIContainer.addComponent(closePopup);
//        headerIContainer.setComponentAlignment(closePopup, Alignment.TOP_RIGHT);
//        closePopup.addStyleName("translateleft10");
//
//        VerticalLayout accrWrapper = new VerticalLayout();
//        accrWrapper.setHeight(880, Unit.PIXELS);
//        accrWrapper.setWidth(100, Unit.PERCENTAGE);
//        popupbodyLayout.addComponent(accrWrapper);
//        datalayout = new Accordion();
//        accrWrapper.addComponent(datalayout);
//        datalayout.setTabCaptionsAsHtml(true);
//        datalayout.setWidth(100, Unit.PERCENTAGE);
//
//        Label titleI = new Label("Protein");
//        titleI.setStyleName(ValoTheme.LABEL_BOLD);
//        headerIContainer.addComponent(titleI);
        ProteinsInformationOverviewLayout proteinInfoLayout = new ProteinsInformationOverviewLayout();
        VerticalLayout protInfoPopup = initPopupLayout(proteinInfoLayout, smallScreen);
        proteinInfoLayout.updateProteinsForm(quantProtein, quantProtein.getUniprotAccession(), quantProtein.getUrl(), quantProtein.getUniprotProteinName());
        tab.addTab(protInfoPopup, "Protein");

//        Label titleII = new Label("Dataset");
//        titleII.setStyleName(ValoTheme.LABEL_BOLD);
//        popupbodyLayout.addComponent(titleII);
        DatasetInformationOverviewLayout dsOverview = new DatasetInformationOverviewLayout(qds, smallScreen);
        VerticalLayout infoPopup = initPopupLayout(dsOverview, smallScreen);
//        dsOverview.getDatasetInfoForm().setWidth(100, Unit.PERCENTAGE);
        tab.addTab(infoPopup, "Dataset");

//        popupbodyLayout.addComponent(dsOverview.getDatasetInfoForm());
//        datasetInfoPopup = new PopupView(null, popupbodyLayout) {
//
//            @Override
//            public void setPopupVisible(boolean visible) {
//                this.setVisible(visible);
//                super.setPopupVisible(visible); //To change body of generated methods, choose Tools | Templates.
//            }
//
//        };
//        closePopup.addLayoutClickListener((LayoutEvents.LayoutClickEvent event) -> {
//            datasetInfoPopup.setPopupVisible(false);
//        });
//        datasetInfoPopup.setVisible(false);
//        datasetInfoPopup.setCaptionAsHtml(true);
//        datasetInfoPopup.setHideOnMouseOut(false);
//        this.addComponent(datasetInfoPopup);
//        datasetInfoPopup.addStyleName("margin20");
    }

    private VerticalLayout initPopupLayout(VerticalLayout infoLayout, boolean smallScreen) {

//        VerticalLayout popupBodyWrapper = new VerticalLayout();
//        popupBodyWrapper.setWidth(infoLayout.getWidth()+100,infoLayout.getWidthUnits());
//        popupBodyWrapper.setHeight(infoLayout.getHeight()+100,infoLayout.getHeightUnits());       
//        
        VerticalLayout popupBodyLayout = new VerticalLayout();
        if (smallScreen) {
            popupBodyLayout.setWidth(100, Unit.PERCENTAGE);
            popupBodyLayout.setHeight(100, Unit.PERCENTAGE);
        } else {
            popupBodyLayout.setWidth(99, Unit.PERCENTAGE);
            popupBodyLayout.setHeight(99, Unit.PERCENTAGE);
            popupBodyLayout.addStyleName("padding20");
            popupBodyLayout.setSpacing(true);
        }
        popupBodyLayout.setStyleName("pupupbody");
//        popupBodyLayout.addStyleName("roundedborder");

//        popupBodyWrapper.addComponent(popupBodyLayout);
//        popupBodyWrapper.setComponentAlignment(popupBodyLayout, Alignment.TOP_CENTER);
//        popupBodyLayout.addLayoutClickListener(this);
        popupBodyLayout.addComponent(infoLayout);

        return popupBodyLayout;

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
//        datasetInfoPopup.setPopupVisible(true);
//        select(itemId);
        popupWindow.setVisible(true);
    }

    public void openComparisonPopup() {
        popupWindow.setVisible(true);

    }

    public abstract void select(Object itemId);

}
