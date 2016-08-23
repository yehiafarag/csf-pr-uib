package no.uib.probe.csf.pr.touch.view.components.heatmapsubcomponents;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import no.uib.probe.csf.pr.touch.view.components.datasetfilters.DatasetPieChartFiltersComponent;
import no.uib.probe.csf.pr.touch.view.components.datasetfilters.RecombineDiseaseGroupsCombonent;
import no.uib.probe.csf.pr.touch.view.components.datasetfilters.ReorderSelectGroupsFilter;
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

    private final DatasetPieChartFiltersComponent datasetPieChartFiltersBtn;
    private final RecombineDiseaseGroupsCombonent reconbineDiseaseGroupsFiltersBtn;
    private final ReorderSelectGroupsFilter reorderSelectBtn;
    private final HorizontalLayout bottomBtnContainer;
    private final SerumCsfFilter serumCsfFilter;
    private final VerticalLayout clearFilterBtn;
    private final ResizableTextLabel datasetCounterLabel;
    private final GridLayout btnsWrapper;

    public HeatmapFiltersContainerResizeControl(GridLayout btnsWrapper, DatasetPieChartFiltersComponent datasetPieChartFiltersBtn, RecombineDiseaseGroupsCombonent reconbineDiseaseGroupsFiltersBtn, ReorderSelectGroupsFilter reorderSelectBtn, HorizontalLayout bottomBtnContainer, SerumCsfFilter serumCsfFilter, VerticalLayout clearFilterBtn, ResizableTextLabel datasetCounterLabel) {
        this.btnsWrapper = btnsWrapper;
        this.datasetPieChartFiltersBtn = datasetPieChartFiltersBtn;
        this.reconbineDiseaseGroupsFiltersBtn = reconbineDiseaseGroupsFiltersBtn;
        this.reorderSelectBtn = reorderSelectBtn;
        this.bottomBtnContainer = bottomBtnContainer;
        this.serumCsfFilter = serumCsfFilter;
        this.clearFilterBtn = clearFilterBtn;
        this.datasetCounterLabel = datasetCounterLabel;

    }

    public GridLayout getFilterContainerLayout() {
        return this.btnsWrapper;
    }

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
