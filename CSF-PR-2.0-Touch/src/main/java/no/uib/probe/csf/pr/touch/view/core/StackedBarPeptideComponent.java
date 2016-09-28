/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantPeptide;

/**
 *
 * @author Yehia Farag
 */
public class StackedBarPeptideComponent extends VerticalLayout implements Comparable<StackedBarPeptideComponent>, LayoutEvents.LayoutClickListener {

    private final Map<String, Object> param = new HashMap<>();
    private String defaultStyleShowAllMode;
    private boolean significant;
    private final String peptideKey;

    public String getPeptideKey() {
        return peptideKey;
    }

    /**
     *
     * @return
     */
    public int getLevel() {
        return level;
    }

    /**
     *
     * @param level
     */
    public void setLevel(int level) {
        this.level = level;
    }
    private int level = 0;

    private boolean overlapped;

    /**
     *
     * @return
     */
    public boolean isMerged() {
        return merged;
    }

    /**
     *
     * @param merged
     */
    public void setMerged(boolean merged) {
        this.merged = merged;
    }
    private boolean merged;
    private final Set<QuantPeptide> quantpeptideSet = new LinkedHashSet<>();

    /**
     *
     * @return
     */
    public boolean isOverlapped() {
        return overlapped;
    }

    /**
     *
     * @param overlapped
     */
    public void setOverlapped(boolean overlapped) {
        this.overlapped = overlapped;
    }

    /**
     *
     * @return
     */
    public String getDefaultStyleShowAllMode() {
        return defaultStyleShowAllMode;
    }

    /**
     *
     * @param defaultStyleShowAllMode
     */
    public void setDefaultStyleShowAllMode(String defaultStyleShowAllMode) {
        this.defaultStyleShowAllMode = defaultStyleShowAllMode.replace("selected", "").trim();
        this.setStyleName(this.defaultStyleShowAllMode);
    }

    /**
     *
     * @return
     */
    public int getX0() {
        return x0;
    }

    /**
     *
     * @return
     */
    public int getWidthArea() {
        return widthArea;
    }

    public void setX0(int x0) {
        this.x0 = x0;
    }

    private int x0;
    private final Integer widthArea;
    private final VerticalLayout ptmLayout = new VerticalLayout();
    private boolean ptmAvailable = false;

    private PopupWindow popupWindow;

    /**
     *
     * @param x0
     * @param widthArea
     * @param peptideKey
     * @param peptideModification
     */
    public StackedBarPeptideComponent(int x0, int widthArea, String peptideKey, String peptideModification, QuantPeptide quantPeptide, boolean smallScreen, String proteinName) {
        this.setHeight(15, Unit.PIXELS);
        this.setWidth((widthArea), Unit.PIXELS);
        this.x0 = x0;
        this.widthArea = widthArea;
        setParam("width", widthArea);
        this.peptideKey = peptideKey;
        if (peptideModification != null && !peptideModification.trim().equalsIgnoreCase("")) {
            ptmAvailable = true;
            ptmLayout.setStyleName("ptmglycosylation");
            ptmLayout.setWidth(10, Unit.PIXELS);
            ptmLayout.setHeight(10, Unit.PIXELS);
            ptmLayout.setDescription(peptideModification);
            ptmLayout.setVisible(false);
        }
        if (quantPeptide != null) {

            VerticalLayout popupBody = new VerticalLayout();
            popupBody.setWidth(100, Unit.PERCENTAGE);
            popupBody.setHeight(100, Unit.PERCENTAGE);

            popupBody.setMargin(false);
            popupBody.setSpacing(true);
            popupBody.addStyleName("roundedborder");
            popupBody.addStyleName("whitelayout");
//        if (smallScreen) {
//            popupBody.addStyleName("padding2");
//        } else {
            popupBody.addStyleName("padding20");
//        }
            VerticalLayout frame = new VerticalLayout();
            frame.setWidth(99, Unit.PERCENTAGE);
            frame.setHeight(99, Unit.PERCENTAGE);
            frame.setSpacing(true);
            frame.addComponent(popupBody);
            String title = "Peptide Information (" + proteinName.trim() + ")";

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
            } else {
                popupWindow.setHeight(500, popupWindow.getHeightUnits());

            }

            PeptidesInformationOverviewLayout peptideInfo = new PeptidesInformationOverviewLayout(quantPeptide);

            popupBody.addComponent(peptideInfo);
            popupBody.setComponentAlignment(peptideInfo, Alignment.TOP_CENTER);

//            VerticalLayout popupbodyLayout = new VerticalLayout();
//            popupbodyLayout.setSpacing(true);
//            popupbodyLayout.setWidth(1000, Unit.PIXELS);
//            popupbodyLayout.setMargin(new MarginInfo(false, false, true, true));
//            popupbodyLayout.addStyleName("border");
//
//            HorizontalLayout headerIContainer = new HorizontalLayout();
//            headerIContainer.setWidth(100, Unit.PERCENTAGE);
//
//            Label titleI = new Label("Peptides");
//            titleI.setStyleName(ValoTheme.LABEL_BOLD);
//            headerIContainer.addComponent(titleI);
//
//            popupbodyLayout.addComponent(headerIContainer);
//
//            CloseButton closePopup = new CloseButton();
//            closePopup.setWidth(10, Unit.PIXELS);
//            closePopup.setHeight(10, Unit.PIXELS);
//            headerIContainer.addComponent(closePopup);
//            headerIContainer.setComponentAlignment(closePopup, Alignment.TOP_RIGHT);
//            closePopup.addStyleName("translateleft10");
//
//           
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            datasetInfoPopup = new PopupView(null, popupbodyLayout) {
//
//                @Override
//                public void setPopupVisible(boolean visible) {
//                    this.setVisible(visible);
//                    super.setPopupVisible(visible); //To change body of generated methods, choose Tools | Templates.
//                }
//
//            };
//            closePopup.addLayoutClickListener((LayoutEvents.LayoutClickEvent event) -> {
//                datasetInfoPopup.setPopupVisible(false);
//            });
//            datasetInfoPopup.setVisible(false);
//            datasetInfoPopup.setCaptionAsHtml(true);
//            datasetInfoPopup.setHideOnMouseOut(false);
//            this.addComponent(datasetInfoPopup);
            this.addLayoutClickListener(StackedBarPeptideComponent.this);
        }

    }

    public boolean isPtmAvailable() {
        return ptmAvailable;
    }

    public VerticalLayout getPtmLayout() {
        return ptmLayout;
    }

    /**
     *
     * @param key
     * @param value
     */
    public void setParam(String key, Object value) {
        param.put(key, value);
    }

    /**
     *
     * @param key
     * @return
     */
    public Object getParam(String key) {
        return param.get(key);
    }

    /**
     *
     * @param select
     */
    public void heighlight(Boolean select) {
        if (select == null) {
            this.setStyleName(defaultStyleShowAllMode);
        } else if (select) {
            this.setStyleName("selected" + defaultStyleShowAllMode);
        } else {
            this.setStyleName("unselected" + defaultStyleShowAllMode);
        }
    }

    /**
     *
     * @param qp
     */
    public void addQuantPeptide(QuantPeptide qp) {
        this.quantpeptideSet.add(qp);
    }

    /**
     *
     * @return
     */
    public Set<QuantPeptide> getQuantpeptideSet() {
        return quantpeptideSet;
    }

    @Override
    public int compareTo(StackedBarPeptideComponent o) {
        return widthArea.compareTo(o.widthArea);
    }

    /**
     *
     * @return
     */
    public boolean isSignificant() {
        return significant;
    }

    /**
     *
     * @param significant
     */
    public void setSignificant(boolean significant) {
        this.significant = significant;
    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        popupWindow.setVisible(true);
    }

}
