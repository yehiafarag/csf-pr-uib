package no.uib.probe.csf.pr.touch.view.components.heatmapsubcomponents;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import no.uib.probe.csf.pr.touch.view.components.datasetfilters.DatasetPieChartFiltersComponent;
import no.uib.probe.csf.pr.touch.view.components.datasetfilters.RecombineDiseaseGroupsCombonent;
import no.uib.probe.csf.pr.touch.view.components.datasetfilters.ReorderSelectGroupsComponent;
import no.uib.probe.csf.pr.touch.view.components.datasetfilters.SerumCsfFilter;
import no.uib.probe.csf.pr.touch.view.core.ResizableTextLabel;

/**
 *
 * @author Yehia Farag
 *
 * this class represents the corner cell in the heat map that contain the
 * dataset filters
 */
public class HeatmapFiltersContainerResizeControl {

    /**
     * The dataset filter container with the interactive management system for
     * different interactive pie-chart filters
     *
     */
    private final DatasetPieChartFiltersComponent datasetPieChartFiltersBtn;
    /**
     * The disease sub group rename and re-combine into new customized disease
     * sub-groups
     *
     */
    private final RecombineDiseaseGroupsCombonent reconbineDiseaseGroupsFiltersBtn;
    /**
     * This button is responsible for updating heat-map rows and columns order
     * as well as allowing user to hide disease groups
     *
     */
    private final ReorderSelectGroupsComponent reorderSelectBtn;
    /**
     * This layout contains 2 filters CSF filter allows user to view CSF
     * datasets Serum filter allows user to view Serum datasets users need to
     * select at least on of the filters
     *
     */
    private final SerumCsfFilter serumCsfFilter;
    /**
     * This button is responsible for remove and reset all applied filters
     *
     */
    private final VerticalLayout clearFilterBtn;
    /**
     * This Label for number of datasets included in the heat map
     *
     */
    private final ResizableTextLabel datasetCounterLabel;

    /**
     * This is the bottom container for recombine group btn and sort and select
     * button
     *
     */
    private final HorizontalLayout bottomBtnContainer;

    /**
     * This is the main container layout
     *
     */
    private final GridLayout btnsWrapper;

    /**
     * Constructor to initialize the main attributes
     *
     * @param btnsWrapper
     * @param datasetPieChartFiltersBtn
     * @param reconbineDiseaseGroupsFiltersBtn
     * @param reorderSelectBtn
     * @param bottomBtnContainer
     * @param serumCsfFilter
     * @param clearFilterBtn
     * @param datasetCounterLabel
     */
    public HeatmapFiltersContainerResizeControl(GridLayout btnsWrapper, DatasetPieChartFiltersComponent datasetPieChartFiltersBtn, RecombineDiseaseGroupsCombonent reconbineDiseaseGroupsFiltersBtn, ReorderSelectGroupsComponent reorderSelectBtn, HorizontalLayout bottomBtnContainer, SerumCsfFilter serumCsfFilter, VerticalLayout clearFilterBtn, ResizableTextLabel datasetCounterLabel) {
        this.btnsWrapper = btnsWrapper;
        this.datasetPieChartFiltersBtn = datasetPieChartFiltersBtn;
        this.reconbineDiseaseGroupsFiltersBtn = reconbineDiseaseGroupsFiltersBtn;
        this.reorderSelectBtn = reorderSelectBtn;
        this.bottomBtnContainer = bottomBtnContainer;
        this.serumCsfFilter = serumCsfFilter;
        this.clearFilterBtn = clearFilterBtn;
        this.datasetCounterLabel = datasetCounterLabel;

    }

    /**
     * Get the main layout container that is used inside the corner cell
     *
     * @return btnsWrapper
     */
    public GridLayout getFilterContainerLayout() {
        return this.btnsWrapper;
    }

    /**
     * Resize the container and sub components (the filters buttons) based on
     * the parent container size
     *
     * @param resizeFactor
     */
    public void resizeFilters(double resizeFactor) {
        btnsWrapper.setWidth((int) (115 * resizeFactor), Sizeable.Unit.PIXELS);
        btnsWrapper.setHeight((int) (100 * resizeFactor), Sizeable.Unit.PIXELS);
        this.datasetPieChartFiltersBtn.resizeFilter(resizeFactor);
        this.datasetCounterLabel.setHeight((int) (25 * resizeFactor), Sizeable.Unit.PIXELS);
        this.clearFilterBtn.setWidth((int) (25 * resizeFactor), Sizeable.Unit.PIXELS);
        this.clearFilterBtn.setHeight((int) (25 * resizeFactor), Sizeable.Unit.PIXELS);
        this.serumCsfFilter.resizeFilter(resizeFactor);

        bottomBtnContainer.setWidth((int) (53 * resizeFactor), Sizeable.Unit.PIXELS);
        this.reconbineDiseaseGroupsFiltersBtn.resizeFilter(resizeFactor);
        this.reorderSelectBtn.resizeFilter(resizeFactor);
    }

}
