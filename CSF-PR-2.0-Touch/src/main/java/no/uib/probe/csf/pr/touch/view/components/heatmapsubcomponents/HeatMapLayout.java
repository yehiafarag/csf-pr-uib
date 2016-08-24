package no.uib.probe.csf.pr.touch.view.components.heatmapsubcomponents;

import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseGroupComparison;
import no.uib.probe.csf.pr.touch.logic.beans.HeatMapHeaderCellInformationBean;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDSIndexes;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.view.bigscreen.popupwindows.StudiesInformationPopupBtn;
import no.uib.probe.csf.pr.touch.view.components.QuantDatasetsfullStudiesTableLayout;
import no.uib.probe.csf.pr.touch.view.core.ImageContainerBtn;
import no.uib.probe.csf.pr.touch.view.core.InformationButton;

/**
 *
 * @author Yehia Farag this class represents Layout represents the main body for
 * the heat map
 */
public abstract class HeatMapLayout extends VerticalLayout {

//    private final GridLayout heatmapBody;
    private final Set<HeatmapCell> selectedCells = new HashSet<>();

    private HeaderCell[] columnHeaderCells;
    private HeaderCell[] rowHeaderCells;
    private final Map<String, HeatmapCell> comparisonsCellsMap = new LinkedHashMap<>();
    private final Set<QuantDiseaseGroupsComparison> selectedDsList;
    private final Set<QuantDiseaseGroupsComparison> availableComparisonsList;

    private boolean singleSelection = false;
    private int heatmapCellWidth;
    private int heatmapCellHeight;

    //heatmap swing data
    private String[] rowsColors;
//    private String[] rowLabelColors;
    private String[] columnsColors;
//    private String[] columnsLabelColors;
    private String[][] dataValuesColors;

    private VerticalLayout hideCompBtn;

    private final Set<Integer> currentDsIds;

//    private Image icon;
//    private Label hideShowBtnLabel;
//    private ThemeResource defaultResource;
//    private final Image heatMapImg;
    private final AbsoluteLayout heatmapComponentContainer;
    private final AbsoluteLayout heatmapPanel;

    private final int availableHMHeight, availableHMWidth;

//    private final HorizontalLayout topLayout;
//    private final HorizontalLayout bottomLayout, columnCategoryHeadersContainer;
//    private final VerticalLayout spacer;
//    private final VerticalLayout rowCategoryHeadersContainer;
//    private final VerticalLayout diseaseGroupsRowsLabels;
//    private final HorizontalLayout diseaseGroupsColumnsLabels;
    private final Map<Integer, QuantDatasetObject> updatedDatasetMap;

    private final HorizontalLayout controlsLayout;
    private int maxDatasetNumber;
    private QuantDSIndexes[][] values;
    private final VerticalLayout cornerCell;

    private Set<HeatMapHeaderCellInformationBean> rowheadersSet, colheadersSet;
//    private final VerticalLayout heatMapLayoutWrapper;
    private final VerticalLayout controlBtnsContainer;

    private final Map<QuantDiseaseGroupsComparison, QuantDiseaseGroupsComparison> equalComparisonMap;
//    private final ZoomControler zoomControler;
    private final HeatmapFiltersContainerResizeControl filterResizeController;


    /*
     *  
     */
    public HeatMapLayout(int heatMapContainerWidth, int availableHMHeight, boolean[] activeColumnHeaders, HeatmapFiltersContainerResizeControl filterResizeController, boolean smallScreen) {
        this.filterResizeController = filterResizeController;
        this.availableComparisonsList = new LinkedHashSet<>();
        this.updatedDatasetMap = new LinkedHashMap<>();
        this.selectedDsList = new LinkedHashSet<>();
        this.currentDsIds = new LinkedHashSet<>();

        this.setWidthUndefined();
        this.setHeightUndefined();
        this.setSpacing(false);
        this.addStyleName("whitelayout");
        this.addStyleName("roundedborder");

//        this.heatMapLayoutWrapper = new VerticalLayout();
//        this.heatMapLayoutWrapper.setWidthUndefined();
//        this.heatMapLayoutWrapper.setHeightUndefined();
        this.heatmapPanel = new AbsoluteLayout();
        this.addComponent(heatmapPanel);
        heatmapPanel.setStyleName("lightgray");

//        this.heatMapImg = new Image();
//        heatMapImg.setSizeFull();
//        this.heatmapPanel.addComponent(heatMapImg, "left: 0px; top: 0px");
        this.heatmapComponentContainer = new AbsoluteLayout();
        heatmapComponentContainer.setSizeFull();
        this.heatmapPanel.addComponent(heatmapComponentContainer, "left: " + 0 + "px; top: " + 0 + "px");

//        this.addComponent(heatMapLayoutWrapper);
        this.availableHMHeight = availableHMHeight - 30;
        this.availableHMWidth = heatMapContainerWidth;
//        String maxBodyHightStyle = ".maxbodyheight{ max-height:" + (availableHMHeight) + "px !important;"
//                + "max-width:" + heatMapContainerWidth + "px !important;}"
//                + ".v-panel-content-maxbodyheight {max-height:" + (availableHMHeight - 2) + "px !important;"
//                + "max-width:" + heatMapContainerWidth + "px !important;}";
//        Page.getCurrent().getStyles().add(maxBodyHightStyle);

//        topLayout = new HorizontalLayout();
//        topLayout.setWidthUndefined();
//        topLayout.setHeight(24, Unit.PIXELS);
//        topLayout.setSpacing(false);
//        heatMapLayoutWrapper.addComponent(topLayout);
//        spacer = new VerticalLayout();
//        this.spacer.setHeight(100, Unit.PERCENTAGE);
//        this.spacer.setWidth(175, Unit.PIXELS);
//        topLayout.addComponent(spacer);
//        zoomControler = new ZoomControler(true);
//        columnCategoryHeadersContainer = new HorizontalLayout();
//        this.columnCategoryHeadersContainer.setHeight(100, Unit.PERCENTAGE);
//        topLayout.addComponent(columnCategoryHeadersContainer);
//        diseaseGroupsColumnsLabels = new HorizontalLayout();
//        diseaseGroupsColumnsLabels.setWidth(10, Unit.PIXELS);
//        diseaseGroupsColumnsLabels.setHeight(100, Unit.PERCENTAGE);
//        columnCategoryHeadersContainer.addComponent(diseaseGroupsColumnsLabels);
//
//        bottomLayout = new HorizontalLayout();
//        bottomLayout.setWidthUndefined();
//        bottomLayout.setSpacing(false);
//        heatMapLayoutWrapper.addComponent(bottomLayout);
//        rowCategoryHeadersContainer = new VerticalLayout();
//        this.rowCategoryHeadersContainer.setWidth(24, Unit.PIXELS);
//        bottomLayout.addComponent(rowCategoryHeadersContainer);
//        bottomLayout.setComponentAlignment(rowCategoryHeadersContainer, Alignment.BOTTOM_CENTER);
//        diseaseGroupsRowsLabels = new VerticalLayout();
//        diseaseGroupsRowsLabels.setHeight(10, Unit.PIXELS);
//        diseaseGroupsRowsLabels.setWidth(24, Unit.PIXELS);
//        rowCategoryHeadersContainer.addComponent(diseaseGroupsRowsLabels);
//        heatmapBody = new GridLayout();
//        this.heatmapBody.setStyleName("heatmapbody");
//        bottomLayout.addComponent(heatmapBody);
        //heatmap controllers
        //init heatmap filters buttons 
        controlsLayout = new HorizontalLayout();
//        heatmapPanel.setStyleName("bluelayout");
        controlsLayout.setVisible(true);
//        controlsLayout.setStyleName("hmbottom");
//        controlsLayout.setHeight(15, Unit.PIXELS);
        controlsLayout.setWidth(100, Unit.PERCENTAGE);
        controlsLayout.setHeightUndefined();

        controlsLayout.setSpacing(true);
        this.addComponent(controlsLayout);

        Label commentLabel = new Label("<b>*</b> Multiple groups");
        commentLabel.setStyleName(ValoTheme.LABEL_SMALL);
        commentLabel.addStyleName(ValoTheme.LABEL_TINY);
        commentLabel.setContentMode(ContentMode.HTML);
        if (smallScreen) {
            commentLabel.addStyleName("nomargin");
        }
        controlsLayout.addComponent(commentLabel);
        controlsLayout.setComponentAlignment(commentLabel, Alignment.TOP_LEFT);
//        controlsLayout.setExpandRatio(commentLabel, 0.6f);

        controlBtnsContainer = new VerticalLayout();
        controlBtnsContainer.setHeightUndefined();
        controlBtnsContainer.setWidthUndefined();
        controlBtnsContainer.setSpacing(true);
//        controlsLayout.addComponent(controlBtnsContainer);
//        controlsLayout.setComponentAlignment(controlBtnsContainer, Alignment.BOTTOM_RIGHT);
//        controlsLayout.setExpandRatio(commentLabel, 0.4f);

        final StudiesInformationPopupBtn showStudiesBtn = new StudiesInformationPopupBtn(smallScreen);
        controlBtnsContainer.addComponent(showStudiesBtn);
        controlBtnsContainer.setComponentAlignment(showStudiesBtn, Alignment.MIDDLE_CENTER);
        showStudiesBtn.addLayoutClickListener((LayoutEvents.LayoutClickEvent event) -> {
            showStudiesBtn.updateData(updatedDatasetMap.values());
            showStudiesBtn.view();
        });

        if (smallScreen) {
            showStudiesBtn.setWidth(25, Unit.PIXELS);
            showStudiesBtn.setHeight(25, Unit.PIXELS);
            showStudiesBtn.removeStyleName("smallimg");
        }

        final QuantDatasetsfullStudiesTableLayout quantStudiesTable = new QuantDatasetsfullStudiesTableLayout(activeColumnHeaders);
        controlBtnsContainer.addComponent(quantStudiesTable);
        ImageContainerBtn exportTableBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                quantStudiesTable.updateCombinedQuantDatasetTableRecords(updatedDatasetMap);
                ExcelExport csvExport = new ExcelExport(quantStudiesTable, "CSF-PR  Quant Datasets Information");
                csvExport.setReportTitle("CSF-PR / Quant Datasets Information ");
                csvExport.setExportFileName("CSF-PR - Quant Datasets Information" + ".xls");
                csvExport.setMimeType(ExcelExport.EXCEL_MIME_TYPE);
                csvExport.setDisplayTotals(false);

                csvExport.setDateDataFormat("0");
                csvExport.setExcelFormatOfProperty("Index", "0");
                csvExport.setExcelFormatOfProperty("#Quantified Proteins", "0");
                csvExport.setExcelFormatOfProperty("patientsGroup2Number", "0");
                csvExport.setExcelFormatOfProperty("#patientsGroup1Number", "0");

                csvExport.export();
            }

        };
        if (smallScreen) {
            exportTableBtn.setWidth(25, Unit.PIXELS);
            exportTableBtn.setHeight(25, Unit.PIXELS);
            exportTableBtn.removeStyleName("smallimg");
        } else {
            exportTableBtn.setHeight(40, Unit.PIXELS);
            exportTableBtn.setWidth(40, Unit.PIXELS);
        }
        exportTableBtn.updateIcon(new ThemeResource("img/xls-text-o-2.png"));
        exportTableBtn.setEnabled(true);
//        exportTableBtn.setPrimaryStyleName("exportxslbtn");
        controlBtnsContainer.addComponent(exportTableBtn);
        controlBtnsContainer.setComponentAlignment(exportTableBtn, Alignment.MIDDLE_CENTER);
        exportTableBtn.setDescription("Export all dataset data");

        ImageContainerBtn selectAllBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                selectAll();
            }

        };
        selectAllBtn.updateIcon(new ThemeResource("img/grid-small.png"));
        selectAllBtn.setEnabled(true);
        selectAllBtn.addStyleName("smallimg");

        if (smallScreen) {
            selectAllBtn.setWidth(25, Unit.PIXELS);
            selectAllBtn.setHeight(25, Unit.PIXELS);
            selectAllBtn.removeStyleName("smallimg");
        } else {
            selectAllBtn.setWidth(40, Unit.PIXELS);
            selectAllBtn.setHeight(40, Unit.PIXELS);
        }
        controlBtnsContainer.addComponent(selectAllBtn);
        controlBtnsContainer.setComponentAlignment(selectAllBtn, Alignment.MIDDLE_CENTER);
        selectAllBtn.setDescription("Select all disease group comparisons");

        ImageContainerBtn unselectAllBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                unselectAll();
            }

        };
        unselectAllBtn.updateIcon(new ThemeResource("img/grid-small-o.png"));
        unselectAllBtn.setEnabled(true);

        if (smallScreen) {
            unselectAllBtn.setWidth(25, Unit.PIXELS);
            unselectAllBtn.setHeight(25, Unit.PIXELS);
        } else {
            unselectAllBtn.setWidth(40, Unit.PIXELS);
            unselectAllBtn.setHeight(40, Unit.PIXELS);
            unselectAllBtn.addStyleName("smallimg");
        }

        controlBtnsContainer.addComponent(unselectAllBtn);
        controlBtnsContainer.setComponentAlignment(unselectAllBtn, Alignment.MIDDLE_CENTER);
        unselectAllBtn.setDescription("Unselect all disease group comparisons");

        final ImageContainerBtn selectMultiBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
                if (this.getStyleName().contains("selectmultiselectedbtn")) {
                    singleSelection = true;
                    this.removeStyleName("selectmultiselectedbtn");

                } else {
                    singleSelection = false;
                    this.addStyleName("selectmultiselectedbtn");

                }
            }

        };
        selectMultiBtn.addStyleName("selectmultiselectedbtn");
        selectMultiBtn.addStyleName("smallimg");
        controlBtnsContainer.addComponent(selectMultiBtn);
        controlBtnsContainer.setComponentAlignment(selectMultiBtn, Alignment.MIDDLE_CENTER);
        selectMultiBtn.setDescription("Multiple selection");
        selectMultiBtn.updateIcon(new ThemeResource("img/grid-small-multi.png"));
        selectMultiBtn.setEnabled(true);
        if (smallScreen) {
            selectMultiBtn.setWidth(25, Unit.PIXELS);
            selectMultiBtn.setHeight(25, Unit.PIXELS);
            selectMultiBtn.removeStyleName("smallimg");
        } else {
            selectMultiBtn.setWidth(40, Unit.PIXELS);
            selectMultiBtn.setHeight(40, Unit.PIXELS);
        }

        cornerCell = new VerticalLayout();
        cornerCell.setWidth(150, Unit.PIXELS);
        cornerCell.setHeight(150, Unit.PIXELS);
        cornerCell.setStyleName("whitelayout");
        if (filterResizeController != null) {

            cornerCell.addComponent(filterResizeController.getFilterContainerLayout());
            cornerCell.setComponentAlignment(filterResizeController.getFilterContainerLayout(), Alignment.MIDDLE_CENTER);
            filterResizeController.getFilterContainerLayout().addStyleName("heatmapcorner");
        }

        InformationButton info = new InformationButton("The disease group comparison table provides an overview of the number of datasets available for each comparison. Hover over a given cell to get additional details about the comparison. Selecting one or more cells in the table will display the corresponding protein details. To filter the data use the options in the upper left corner.", false);
        controlBtnsContainer.addComponent(info);
        if (smallScreen) {
            info.setWidth(25, Unit.PIXELS);
            info.setHeight(25, Unit.PIXELS);
            info.removeStyleName("smallimg");
        }

//        zoomControler.addZoomableComponent(heatMapLayoutWrapper);
        equalComparisonMap = new HashMap<>();
    }

    public VerticalLayout getControlBtnsContainer() {
        return controlBtnsContainer;
    }

    public Set<Integer> getCurrentDsIds() {
        return currentDsIds;
    }

    private int zoomLevel = 10;

    /**
     * this method responsible for updating the heatmap layout
     *
     *
     * @param rowsLbels
     * @param columnsLbels
     * @param patientsGroupComparisonsSet
     * @param fullQuantDsMap
     */
    public void updateData(Set<HeatMapHeaderCellInformationBean> rowsLbels, Set<HeatMapHeaderCellInformationBean> columnsLbels, Set<DiseaseGroupComparison> patientsGroupComparisonsSet, Map<Integer, QuantDatasetObject> fullQuantDsMap) {

        unselectAll();
        equalComparisonMap.clear();
        updatedDatasetMap.clear();
        currentDsIds.clear();
        updatedDatasetMap.putAll(fullQuantDsMap);

        for (DiseaseGroupComparison ds : patientsGroupComparisonsSet) {
            currentDsIds.add(ds.getOriginalDatasetIndex());
        }
        updateHeatMapLayout(rowsLbels, columnsLbels, patientsGroupComparisonsSet, fullQuantDsMap);
        updateHMThumb(this.getHMThumbImg(), patientsGroupComparisonsSet.size(), 0, equalComparisonMap);
    }

    /**
     * this method responsible for filtering the heatmap based on filters
     * selection
     *
     *
     * @param datasets
     */
//    public void filterHeatMap(Map<Integer, QuantDatasetObject> datasets, Set<DiseaseGroupComparison> patientsGroupComparisonsSet) {
//        unselectAll();
//
//        Set<HeatMapHeaderCellInformationBean> localRows = new LinkedHashSet<>(),localColumns = new LinkedHashSet<>();
//
//        for (int key : datasets.keySet()) {
//            QuantDatasetObject dataset = datasets.get(key);
//            for (HeatMapHeaderCellInformationBean rowHeader : rowheadersSet) {
//                if (dataset.getUpdatedDiseaseGroupI().equalsIgnoreCase(rowHeader.getDiseaseGroupName()) || dataset.getUpdatedDiseaseGroupII().equalsIgnoreCase(rowHeader.getDiseaseGroupName())) {
//                    localRows.add(rowHeader);
//                    break;
//                }
//
//            }
//            
//             for (HeatMapHeaderCellInformationBean columnHeader : colheadersSet) {
//                if (dataset.getUpdatedDiseaseGroupI().equalsIgnoreCase(columnHeader.getDiseaseGroupName()) || dataset.getUpdatedDiseaseGroupII().equalsIgnoreCase(columnHeader.getDiseaseGroupName())) {
//                    localColumns.add(columnHeader);
//                    break;
//                }
//
//            }
//
//        }
//        
//           updateDiseaseHeadersLabel(localRows, localColumns);
//
////        
////        
////        
////        Set<String> headers = new LinkedHashSet<>();
////        Set<String> comparisonTitlesMap = new LinkedHashSet<>();
////        Map<String, Integer> valueMap = new LinkedHashMap<>();
////        int deactivated = 0;
////        Set<HeatMapHeaderCellInformationBean> localRowHeadersSet = new LinkedHashSet<>(), localColumnHeadersSet = new LinkedHashSet<>();
////        datasets.values().stream().forEach((dataset) -> {
////            String grI = dataset.getUpdatedDiseaseGroupI() + "__" + dataset.getDiseaseCategory();
////            String grII = dataset.getUpdatedDiseaseGroupII() + "__" + dataset.getDiseaseCategory();
////            comparisonTitlesMap.add(grI + " / " + grII);
////            comparisonTitlesMap.add(grII + " / " + grI);
////            headers.add(grI);
////            headers.add(grII);
////            if (!valueMap.containsKey(grI + " / " + grII)) {
////                valueMap.put(grI + " / " + grII, 0);
////                valueMap.put(grII + " / " + grI, 0);
////            }
////            valueMap.put(grI + " / " + grII, valueMap.get(grI + " / " + grII) + 1);
////            valueMap.put(grII + " / " + grI, valueMap.get(grII + " / " + grI) + 1);
////        });
////
////        Set<Integer> activeColumn = new LinkedHashSet<>();
////        Set<Integer> activeRows = new LinkedHashSet<>();
////        update column
////        int counter = 0;
////        for (HeaderCell header : columnHeaderCells) {
////            if (headers.contains(header.getValueLabel())) {
////                header.setVisible(true);
////                activeColumn.add(counter);
////                localColumnHeadersSet.add((HeatMapHeaderCellInformationBean) this.colheadersSet.toArray()[counter]);
////            } else {
////                header.setVisible(false);
////            }
////            counter++;
////        }
////        update rows
////        counter = 0;
////        for (HeaderCell header : rowHeaderCells) {
////            if (headers.contains(header.getValueLabel())) {
////                header.setVisible(true);
////                activeRows.add(counter);
////                localRowHeadersSet.add((HeatMapHeaderCellInformationBean) this.rowheadersSet.toArray()[counter]);
////            } else {
////                header.setVisible(false);
////            }
////            counter++;
////
////        }
////        
////        updateHeatMapLayout(localRowHeadersSet, localColumnHeadersSet, patientsGroupComparisonsSet,  updatedDatasetMap);
////        this.updateData(localRowHeadersSet, localColumnHeadersSet, null, datasets);
////
////        //updateBody
////        for (int row = 0; row < cellTable.length; row++) {
////            HeatmapCell[] cellRow = cellTable[row];
////            for (int col = 0; col < cellRow.length; col++) {
////                HeatmapCell cell = cellRow[col];
////                if (cell == null) {
////                    continue;
////                }
////                if (activeRows.contains(row) && activeColumn.contains(col)) {
////                    if (cell.getValue() == 0) {
////                        cell.setVisible(true);
////                    } else if (comparisonTitlesMap.contains(cell.getComparison().getComparisonHeader())) {
////                        cell.setVisible(true);
////                        cell.updateLabel(valueMap.get(cell.getComparison().getComparisonHeader()) + "");
////
////                    } else {
////                        deactivated += cell.getComparison().getDatasetMap().size();
////                        cell.setVisible(true);
////                    }
////
////                } else {
////                    cell.setVisible(false);
////                }
////
////            }
////
////        }
////        
////        
////        
////        //update thumb  data
////        activeRows.remove(0);
////        activeColumn.remove(0);
////        rowsColors = new String[activeRows.size()];
////        rowLabelColors = new String[activeRows.size()];
////        columnsColors = new String[activeColumn.size()];
////        columnsLabelColors = new String[activeColumn.size()];
////        int r = 0;
////        for (int i : activeRows) {
////
////            rowsColors[r++] = ((HeatMapHeaderCellInformationBean) rowheadersSet.toArray()[i - 1]).getDiseaseColor();
////
////        }
////        r = 0;
////        for (int i : activeColumn) {
////            columnsColors[r++] = ((HeatMapHeaderCellInformationBean) colheadersSet.toArray()[i - 1]).getDiseaseColor();
////        }
////
////        int rowCount = 0, ColumnCount = 0;
////        for (int x : activeRows) {
////            for (int y : activeColumn) {
////                HeatmapCell cell = (HeatmapCell) cellTable[x][y];
////                if (cell.isVisible()) {
////                    dataValuesColors[rowCount][ColumnCount++] = cell.getCellColor() + "__" + ((int) cell.getValue() == 0 ? " " : "" + (int) cell.getValue());
////                } else {
////                    dataValuesColors[rowCount][ColumnCount++] = "#BDBDBD" + "__ ";
////                }
////
////            }
////            rowCount++;
////            ColumnCount = 0;
////        }
////
////        //update labels        
//////        this.columnCategoryHeadersContainer.setWidth(heatmapCellWidth * localColumnHeadersSet.size(), Unit.PIXELS);
//////        this.rowCategoryHeadersContainer.setHeight(heatmapCellHeight * localRowHeadersSet.size(), Unit.PIXELS);
////        equalComparisonMap.clear();
////        comparisonsCellsMap.values().stream().forEach((cell) -> {
////
////            String kI = cell.getComparison().getComparisonHeader();
////            String[] k1Arr = kI.split(" / ");
////            String kII = k1Arr[1] + " / " + k1Arr[0];
////            if (comparisonsCellsMap.containsKey(kII)) {
////                if (cell.isVisible() && comparisonsCellsMap.get(kII).isVisible()) {
////                    equalComparisonMap.put(cell.getComparison(), comparisonsCellsMap.get(kII).getComparison());
////                    equalComparisonMap.put(comparisonsCellsMap.get(kII).getComparison(), cell.getComparison());
////                }
////
////            }
////        });
////        updateDiseaseHeadersLabel(localRowHeadersSet, localColumnHeadersSet);
//////        this.filterHeatMap(datasets);
////        updateHMThumb(getHMThumbImg(), datasets.size(), deactivated, equalComparisonMap);
//    }
//    
    private HeatmapCell[][] cellTable;

    private void updateHeatMapLayout(Set<HeatMapHeaderCellInformationBean> rowheaders, Set<HeatMapHeaderCellInformationBean> colheaders, Set<DiseaseGroupComparison> patientsGroupComparisonsSet, Map<Integer, QuantDatasetObject> fullQuantDsMap) {//, Map<String, String> diseaseFullNameMap, ) {

        this.rowheadersSet = rowheaders;
        this.colheadersSet = colheaders;

        updateDiseaseHeadersLabel(rowheaders, colheaders);
        cellTable = new HeatmapCell[rowheaders.size()][colheaders.size()];

        //init columnHeaders  
        columnHeaderCells = new HeaderCell[colheaders.size()];
        columnsColors = new String[colheaders.size()];
//        columnsLabelColors = new String[colheaders.size()];
        for (int i = 0; i < colheaders.size(); i++) {
            HeatMapHeaderCellInformationBean cellInfo = (HeatMapHeaderCellInformationBean) colheaders.toArray()[i];
            String title = cellInfo.getDiseaseGroupName();
            if (title.equalsIgnoreCase("")) {
                title = "Not Available";
            }
            HeaderCell headerCell = new HeaderCell(title, i, cellInfo.getDiseaseGroupFullName(), cellInfo.getDiseaseCategory(), true) {

                @Override
                public void selectData(String valueLabel) {
                    addRowSelectedDs(valueLabel);
                }

                @Override
                public void unSelectData(String valueLabel) {
                    removeRowSelectedDs(valueLabel);
                }
            };
            columnsColors[i] = cellInfo.getDiseaseColor();
//            cellTable[0][i]=headerCell;
//            heatmapBody.addComponent(headerCell, i + 1, 0);
//            heatmapBody.setComponentAlignment(headerCell, Alignment.MIDDLE_CENTER);
            columnHeaderCells[i] = headerCell;
        }

        //init row headers
        rowHeaderCells = new HeaderCell[rowheaders.size()];
        rowsColors = new String[rowheaders.size()];
//        rowLabelColors = new String[rowheaders.size()];
        for (int i = 0; i < rowheaders.size(); i++) {
            HeatMapHeaderCellInformationBean cellInfo = (HeatMapHeaderCellInformationBean) rowheaders.toArray()[i];
            String title = cellInfo.getDiseaseGroupName();
            if (title.equalsIgnoreCase("")) {
                title = "Not Available";
            }
            HeaderCell headerCell = new HeaderCell(title, i, cellInfo.getDiseaseGroupFullName(), cellInfo.getDiseaseCategory(), false) {

                @Override
                public void selectData(String valueLabel) {
                    addRowSelectedDs(valueLabel);
                }

                @Override
                public void unSelectData(String valueLabel) {
                    removeRowSelectedDs(valueLabel);
                }
            };

//            heatmapBody.addComponent(headerCell, 0, i + 1);
//            heatmapBody.setComponentAlignment(headerCell, Alignment.MIDDLE_CENTER);
            rowHeaderCells[i] = headerCell;
            rowsColors[i] = cellInfo.getDiseaseColor();

        }

        //init data
        calcHeatMapMatrix(rowheaders, colheaders, patientsGroupComparisonsSet, fullQuantDsMap);
        HeatmapColorGenerator hmColorGen = new HeatmapColorGenerator(maxDatasetNumber, 0);
        comparisonsCellsMap.clear();

        dataValuesColors = new String[rowheaders.size()][colheaders.size()];
        for (int x = 0; x < values.length; x++) {
            for (int y = 0; y < values[x].length; y++) {
                HeatMapHeaderCellInformationBean grI = (HeatMapHeaderCellInformationBean) rowheaders.toArray()[x];
                HeatMapHeaderCellInformationBean grII = (HeatMapHeaderCellInformationBean) colheaders.toArray()[y];
                String updatedComparisonTitle = grI.toString() + " / " + grII.toString();
                double value = values[x][y].getValue();
                String color = "#EFF2FB";
                if (!rowheaders.toArray()[x].toString().equalsIgnoreCase(colheaders.toArray()[y].toString())) {
                    color = hmColorGen.getColor((float) value);
                }

                Map<Integer, QuantDatasetObject> datasetMap = values[x][y].getDatasetMap();
                Set<String> pubCounter = new HashSet<>();
                for (QuantDatasetObject ds : datasetMap.values()) {
                    pubCounter.add(ds.getPumedID());

                }

                String fullComparisonTitle = grI.getDiseaseGroupFullName() + " / " + grII.getDiseaseGroupFullName();
                String orginalComparisonName = grI.getDiseaseGroupOreginalName() + " / " + grII.getDiseaseGroupOreginalName();
                HeatmapCell cell = new HeatmapCell(value, color, grI.getDiseaseColor(), datasetMap, x, y, null, heatmapCellWidth, pubCounter.size(), updatedComparisonTitle, fullComparisonTitle, orginalComparisonName, ((HeatMapHeaderCellInformationBean) rowheaders.toArray()[x]).getDiseaseCategory(), ((HeatMapHeaderCellInformationBean) rowheaders.toArray()[x]).getDiseaseStyleName()) {

                    @Override
                    public void selectData(HeatmapCell cell) {
                        addSelectedDs(cell);
                    }

                    @Override
                    public void unSelectData(HeatmapCell cell) {
                        removerSelectedDs(cell);
                    }

                };
                comparisonsCellsMap.put(updatedComparisonTitle, cell);

                cellTable[x][y] = cell;
                dataValuesColors[x][y] = color + "__" + ((int) cell.getValue() == 0 ? " " : "" + (int) cell.getValue());
                if (cell.getComparison().getDatasetMap().size() > 0) {
                    columnHeaderCells[y].addComparison(cell.getComparison(), cell);
                    rowHeaderCells[x].addComparison(cell.getComparison(), cell);
                    if (!cell.getComparison().getOreginalComparisonHeader().trim().equalsIgnoreCase("/")) {
                        availableComparisonsList.add(cell.getComparison());
                    }
                }

            }

        }
        equalComparisonMap.clear();
        for (HeatmapCell cell : comparisonsCellsMap.values()) {
            String kI = cell.getComparison().getComparisonHeader();
            String[] k1Arr = kI.split(" / ");
            if (k1Arr.length == 0) {
                continue;
            }
            String kII = k1Arr[1] + " / " + k1Arr[0];
            if (comparisonsCellsMap.containsKey(kII)) {
                equalComparisonMap.put(cell.getComparison(), comparisonsCellsMap.get(kII).getComparison());
                equalComparisonMap.put(comparisonsCellsMap.get(kII).getComparison(), cell.getComparison());
            }

        }
//        heatmapPanel.setHeightUndefined();

//        comparisonsCellsMap.values().stream().forEach((cell) -> {
//            String kI = cell.getComparison().getComparisonHeader();
//            String[] k1Arr = kI.split(" / ");
//            if (k1Arr.length == 1) {
//               continue;
//            }
//             String kII = k1Arr[1] + " / " + k1Arr[0];
//            if (comparisonsCellsMap.containsKey(kII)) {
//                equalComparisonMap.put(cell.getComparison(), comparisonsCellsMap.get(kII).getComparison());
//                equalComparisonMap.put(comparisonsCellsMap.get(kII).getComparison(), cell.getComparison());
//            }
//        });
    }

    /**
     * this method responsible for updating heat map layout upon user selection
     * and update the selection manager
     *
     * @param cell
     */
    protected void addSelectedDs(HeatmapCell cell) {
        if (selectedDsList.isEmpty()) {
            comparisonsCellsMap.values().stream().forEach((hmcell) -> {
                hmcell.unselect();
            });

        }
        for (HeaderCell header : columnHeaderCells) {
            header.unselect();
        }
        for (HeaderCell header : rowHeaderCells) {
            header.unselect();
        }
        if (singleSelection) {
            selectedCells.stream().forEach((tcell) -> {
                tcell.unselect();
            });
            selectedDsList.clear();
            selectedCells.clear();
        }

        this.selectedDsList.add(cell.getComparison());
        String kI = cell.getComparison().getComparisonHeader();
        String[] k1Arr = kI.split(" / ");
        String kII = k1Arr[1] + " / " + k1Arr[0];
        HeatmapCell equalCall = comparisonsCellsMap.get(kII);
        if (equalCall != null) {
            equalCall.select();
            this.selectedCells.add(equalCall);
            equalCall.getComparison().setSwitchable(true);
            cell.getComparison().setSwitchable(true);
        } else {
            cell.getComparison().setSwitchable(false);
        }
        cell.select();
        this.selectedCells.add(cell);

        updateSelectionManagerIndexes();

    }

    /**
     * this method is responsible for un-select comparison and update heat map
     * layout and update selection manager
     *
     * @param cell
     */
    protected void removerSelectedDs(HeatmapCell cell) {
        for (HeaderCell header : columnHeaderCells) {
            header.unselect();
        }
        for (HeaderCell header : rowHeaderCells) {
            header.unselect();
        }
        this.selectedDsList.remove(cell.getComparison());
        this.selectedCells.remove(cell);
        String kI = cell.getComparison().getComparisonHeader();
        String[] k1Arr = kI.split(" / ");
        String kII = k1Arr[1] + " / " + k1Arr[0];
        HeatmapCell equalCall = comparisonsCellsMap.get(kII);
        if (equalCall != null) {
            equalCall.unselect();
            this.selectedDsList.remove(equalCall.getComparison());
            this.selectedCells.remove(equalCall);
        } else {
            System.out.println("at equal cell was null " + kII);
            equalCall = cell;
        }
        updateSelectionManagerIndexes();
        if (selectedCells.isEmpty()) {
            comparisonsCellsMap.values().stream().forEach((hmcell) -> {
                hmcell.initialState();
            });

        } else {
            cell.unselect();
            equalCall.unselect();

        }

    }

    /**
     * this method is responsible for selecting a row or column and update heat
     * map layout and update selection manager
     *
     * @param selectedheader
     */
    protected void addRowSelectedDs(String selectedheader) {
        if (selectedDsList.isEmpty()) {
            //reset all cells to transpairent
            comparisonsCellsMap.values().stream().forEach((hmcell) -> {
                hmcell.unselect();
            });

        }
        if (singleSelection) {
            selectedCells.stream().forEach((tcell) -> {
                tcell.unselect();
            });
            selectedDsList.clear();
            selectedCells.clear();

            for (HeaderCell header : columnHeaderCells) {

                if (header.getValueLabel().equalsIgnoreCase(selectedheader)) {
                    header.getIncludedCells().stream().forEach((tcell) -> {
                        tcell.select();

                        String kI = tcell.getComparison().getComparisonHeader();
                        String[] k1Arr = kI.split(" / ");
                        String kII = k1Arr[1] + " / " + k1Arr[0];
                        HeatmapCell equalCall = comparisonsCellsMap.get(kII);
                        if (equalCall != null) {
                            equalCall.unselect();
                            this.selectedDsList.remove(equalCall.getComparison());
                            this.selectedCells.remove(equalCall);
                        } else {
                            System.out.println("at equal cell was null " + kII);
//                            equalCall = cell;
                        }

                        if (equalCall != null) {
                            equalCall.select();
                            this.selectedCells.add(equalCall);
                            equalCall.getComparison().setSwitchable(true);
                            tcell.getComparison().setSwitchable(true);
                        } else {
                            tcell.getComparison().setSwitchable(false);
                        }

                    });
                    header.select();
                    selectedCells.addAll(header.getIncludedCells());
                    header.getIncludedComparisons().stream().forEach((qdComp) -> {
                        String kI = qdComp.getOreginalComparisonHeader();
                        if (kI.startsWith(selectedheader.split("__")[0] + " / ")) {
                            selectedDsList.add(qdComp);
                        }
                    });

                    continue;
                }
                header.unselect();
            }
            for (HeaderCell header : rowHeaderCells) {
                if (header.getValueLabel().equalsIgnoreCase(selectedheader)) {
                    header.getIncludedCells().stream().forEach((tcell) -> {
                        tcell.select();

                        String kI = tcell.getComparison().getComparisonHeader();
                        String[] k1Arr = kI.split(" / ");
                        String kII = k1Arr[1] + " / " + k1Arr[0];
                        HeatmapCell equalCall = comparisonsCellsMap.get(kII);
                        if (equalCall != null) {
                            equalCall.unselect();
                            this.selectedDsList.remove(equalCall.getComparison());
                            this.selectedCells.remove(equalCall);
                        } else {
                            System.out.println("at equal cell was null " + kII);
//                            equalCall = cell;
                        }

                        if (equalCall != null) {
                            equalCall.select();
                            this.selectedCells.add(equalCall);
                            equalCall.getComparison().setSwitchable(true);
                            tcell.getComparison().setSwitchable(true);
                        } else {
                            tcell.getComparison().setSwitchable(false);
                        }

                    });
                    header.select();
                    selectedCells.addAll(header.getIncludedCells());
                    header.getIncludedComparisons().stream().forEach((qdComp) -> {
                        String kI = qdComp.getOreginalComparisonHeader();
                        if (kI.startsWith(selectedheader.split("__")[0] + " / ")) {
                            selectedDsList.add(qdComp);
                        }
                    });

                    continue;
                }
                header.unselect();
            }

        } else {

            for (HeaderCell header : columnHeaderCells) {
                if (header.getValueLabel().equalsIgnoreCase(selectedheader)) {
                    header.getIncludedCells().stream().forEach((tcell) -> {
                        tcell.select();
                        String kI = tcell.getComparison().getComparisonHeader();
                        String[] k1Arr = kI.split(" / ");
                        String kII = k1Arr[1] + " / " + k1Arr[0];
                        HeatmapCell equalCall = comparisonsCellsMap.get(kII);
                        if (equalCall != null) {
                            equalCall.unselect();
                            this.selectedDsList.remove(equalCall.getComparison());
                            this.selectedCells.remove(equalCall);
                        } else {
                            System.out.println("at equal cell was null " + kII);
//                            equalCall = cell;
                        }

                        if (equalCall != null) {
                            equalCall.select();
                            this.selectedCells.add(equalCall);
                            equalCall.getComparison().setSwitchable(true);
                            tcell.getComparison().setSwitchable(true);
                        } else {
                            tcell.getComparison().setSwitchable(false);
                        }

                    });
                    header.select();
                    selectedCells.addAll(header.getIncludedCells());
                    header.getIncludedComparisons().stream().forEach((qdComp) -> {
                        String kI = qdComp.getOreginalComparisonHeader();
                        if (kI.startsWith(selectedheader.split("__")[0] + " / ")) {
                            selectedDsList.add(qdComp);
                        }
                    });
                    break;
                }
            }
            for (HeaderCell header : rowHeaderCells) {
                if (header.getValueLabel().equalsIgnoreCase(selectedheader)) {
                    header.getIncludedCells().stream().forEach((tcell) -> {
                        tcell.select();

                        String kI = tcell.getComparison().getComparisonHeader();
                        String[] k1Arr = kI.split(" / ");
                        String kII = k1Arr[1] + " / " + k1Arr[0];
                        HeatmapCell equalCall = comparisonsCellsMap.get(kII);
                        if (equalCall != null) {
                            equalCall.unselect();
                            this.selectedDsList.remove(equalCall.getComparison());
                            this.selectedCells.remove(equalCall);
                        } else {
                            System.out.println("at equal cell was null " + kII);
//                            equalCall = cell;
                        }

                        if (equalCall != null) {
                            equalCall.select();
                            this.selectedCells.add(equalCall);
                            equalCall.getComparison().setSwitchable(true);
                            tcell.getComparison().setSwitchable(true);
                        } else {
                            tcell.getComparison().setSwitchable(false);
                        }

                    });
                    header.select();
                    selectedCells.addAll(header.getIncludedCells());
                    header.getIncludedComparisons().stream().forEach((qdComp) -> {
                        String kI = qdComp.getOreginalComparisonHeader();
                        if (kI.startsWith(selectedheader.split("__")[0] + " / ")) {
                            selectedDsList.add(qdComp);
                        }
                    });
                    break;
                }
            }

        }
        updateSelectionManagerIndexes();
    }

    /**
     * This method responsible for un-selecting a row or column and update heat
     * map layout and update selection manager
     *
     * @param selectedHeadercell
     */
    protected void removeRowSelectedDs(String selectedHeadercell) {

        for (HeaderCell header : columnHeaderCells) {
            if (header.getValueLabel().equalsIgnoreCase(selectedHeadercell)) {
                header.getIncludedCells().stream().forEach((tcell) -> {
                    tcell.unselect();
                });
                header.unselect();
                selectedCells.removeAll(header.getIncludedCells());
                selectedDsList.removeAll(header.getIncludedComparisons());
                break;
            }
        }
        for (HeaderCell header : rowHeaderCells) {
            if (header.getValueLabel().equalsIgnoreCase(selectedHeadercell)) {
                header.getIncludedCells().stream().forEach((tcell) -> {
                    tcell.unselect();
                });
                header.unselect();
                selectedCells.removeAll(header.getIncludedCells());
                selectedDsList.removeAll(header.getIncludedComparisons());
                break;
            }
        }
        updateSelectionManagerIndexes();
        if (selectedDsList.isEmpty()) {
//            reset all cells to inital state
            comparisonsCellsMap.values().stream().forEach((hmcell) -> {
                hmcell.initialState();
            });
        }

    }

    private VerticalLayout initDiseaseGroupLabel(String dName_dStyle, int itemsNumb, boolean rotate) {

        VerticalLayout diseaseLabelContainer = new VerticalLayout();
        Label label = new Label("<center><font  color='#ffffff'>" + dName_dStyle.split("__")[0] + "</font></center>");
        label.setContentMode(ContentMode.HTML);
        diseaseLabelContainer.setStyleName(dName_dStyle.split("__")[1]);
        label.addStyleName("hidetextoverflow");

        if (rotate) {
            diseaseLabelContainer.setHeight((itemsNumb * heatmapCellHeight), Unit.PIXELS);
            diseaseLabelContainer.setWidth(100, Unit.PERCENTAGE);
            VerticalLayout rotateContainer = new VerticalLayout();
            rotateContainer.setWidth((itemsNumb * heatmapCellHeight), Unit.PIXELS);
            rotateContainer.setHeight(100, Unit.PERCENTAGE);
            diseaseLabelContainer.addComponent(rotateContainer);
            rotateContainer.addStyleName("rotateheader");
            rotateContainer.addComponent(label);
        } else {
            diseaseLabelContainer.addComponent(label);
            diseaseLabelContainer.setWidth((itemsNumb * (heatmapCellWidth)) - 1, Unit.PIXELS);
            diseaseLabelContainer.setHeight(100, Unit.PERCENTAGE);

        }
        diseaseLabelContainer.setDescription(dName_dStyle.split("__")[0]);
        diseaseLabelContainer.addStyleName("hmheaderlabel");

        return diseaseLabelContainer;

    }
    private boolean resetRows = false;
    private boolean resetcolumns = false;

    private void updateDiseaseHeadersLabel(Set<HeatMapHeaderCellInformationBean> rowheaders, Set<HeatMapHeaderCellInformationBean> colheaders) {

        Map<String, TreeSet<Integer>> diseaseRowsMap = new LinkedHashMap<>();
        int index = 0;
        for (HeatMapHeaderCellInformationBean header : rowheaders) {
            String dName_dStyle = header.getDiseaseCategory() + "__" + header.getDiseaseStyleName();
            if (!diseaseRowsMap.containsKey(dName_dStyle)) {
                diseaseRowsMap.put(dName_dStyle, new TreeSet<>());
            }
            TreeSet<Integer> rowIndex = diseaseRowsMap.get(dName_dStyle);
            rowIndex.add(index);
            diseaseRowsMap.put(dName_dStyle, rowIndex);
            index++;

        }

        diseaseRowsMap.keySet().stream().map((dName) -> diseaseRowsMap.get(dName)).forEach((rowIndex) -> {
            int y = rowIndex.first();

            for (int x : rowIndex) {
                if (x == y) {
                    y++;
                } else {
                    resetRows = true;
                    break;

                }

            }
        });

        Map<String, TreeSet<Integer>> diseaseColumnMap = new LinkedHashMap<>();
        index = 0;

        for (HeatMapHeaderCellInformationBean header : colheaders) {
            String dName_dStyle = header.getDiseaseCategory() + "__" + header.getDiseaseStyleName();
            if (!diseaseColumnMap.containsKey(dName_dStyle)) {
                diseaseColumnMap.put(dName_dStyle, new TreeSet<>());
            }
            TreeSet<Integer> colIndex = diseaseColumnMap.get(dName_dStyle);
            colIndex.add(index);
            diseaseColumnMap.put(dName_dStyle, colIndex);
            index++;

        }

        for (String dName : diseaseColumnMap.keySet()) {
            TreeSet<Integer> rowIndex = diseaseColumnMap.get(dName);
            int y = rowIndex.first();
            for (int x : rowIndex) {
                if (x == y) {
                    y++;
                } else {
                    resetcolumns = true;
                    break;

                }

            }

        }

//        diseaseGroupsRowsLabels.setHeightUndefined();
//        if (resetRows) {
//
//            int itemNum = (rowcounter / diseaseRowsMap.size());
//            int rem = (rowcounter % diseaseRowsMap.size());
//            int x=0;
//            for (String dName_dStyle : diseaseRowsMap.keySet()) {
//                rowLabelColors[x]=dName_dStyle;
////                VerticalLayout diseaseLableContainer = initDiseaseGroupLabel(dName_dStyle, itemNum + rem, true);
////                diseaseGroupsRowsLabels.addComponent(diseaseLableContainer);
//                rem = 0;
//            }
//
//        } else {
//
//            diseaseRowsMap.keySet().stream().map((dName_dStyle) -> {
//                int itemNumb = diseaseRowsMap.get(dName_dStyle).last() - diseaseRowsMap.get(dName_dStyle).first() + 1;
//                VerticalLayout diseaseLableContainer = initDiseaseGroupLabel(dName_dStyle, itemNumb, true);
//                return diseaseLableContainer;
//            }).forEach((diseaseLableContainer) -> {
//                diseaseGroupsRowsLabels.addComponent(diseaseLableContainer);
//            });
//        }
//
//        diseaseGroupsColumnsLabels.setWidthUndefined();
//
//        if (resetcolumns) {
//
//            int itemNum = (columnCounter / diseaseColumnMap.size());
//            int rem = (columnCounter % diseaseColumnMap.size());
//            for (String dName_dStyle : diseaseColumnMap.keySet()) {
//                VerticalLayout diseaseLableContainer = initDiseaseGroupLabel(dName_dStyle, itemNum + rem, false);
//                diseaseGroupsColumnsLabels.addComponent(diseaseLableContainer);
//                rem = 0;
//            }
//
//        } else {
//
//            diseaseColumnMap.keySet().stream().map((dName_dStyle) -> {
//                int itemNumb = diseaseColumnMap.get(dName_dStyle).last() - diseaseColumnMap.get(dName_dStyle).first() + 1;
//                VerticalLayout diseaseLableContainer = initDiseaseGroupLabel(dName_dStyle, itemNumb, false);
//                return diseaseLableContainer;
//            }).forEach((diseaseLableContainer) -> {
//                diseaseGroupsColumnsLabels.addComponent(diseaseLableContainer);
//                diseaseGroupsColumnsLabels.setComponentAlignment(diseaseLableContainer, Alignment.TOP_CENTER);
//            });
//        }
    }

    private void calcHeatMapMatrix(Set<HeatMapHeaderCellInformationBean> rowheaders, Set<HeatMapHeaderCellInformationBean> colheaders, Set<DiseaseGroupComparison> patientsGroupComparisonsSet, Map<Integer, QuantDatasetObject> fullQuantDsMap) {
        maxDatasetNumber = -1;

        values = new QuantDSIndexes[rowheaders.size()][colheaders.size()];
        for (int x = 0; x < rowheaders.size(); x++) {
            for (int y = 0; y < colheaders.size(); y++) {
                Set<Integer> value = calcDsNumbers(rowheaders.toArray()[x].toString(), colheaders.toArray()[y].toString(), patientsGroupComparisonsSet);
//                int z = 0;
                Map<Integer, QuantDatasetObject> datasetMap = new LinkedHashMap<>();
                int[] indexes = new int[value.size()];
                for (int i : value) {
                    datasetMap.put(i, fullQuantDsMap.get(i));
//                    indexes[z] = i;
//                    z++;
                }
                QuantDSIndexes qDataset = new QuantDSIndexes();
                qDataset.setValue(value.size());
                qDataset.setDatasetMap(datasetMap);
                values[x][y] = qDataset;
                if (value.size() > maxDatasetNumber) {
                    maxDatasetNumber = value.size();
                }
            }

        }
    }

    private Set<Integer> calcDsNumbers(String PGI, String PGII, Set<DiseaseGroupComparison> patientsGroupComparisonsSet) {
        Set<Integer> indexes = new HashSet<>();
        patientsGroupComparisonsSet.stream().filter((pg) -> (pg.checkLabel(PGI))).filter((pg) -> (pg.getValLabel(PGI).equalsIgnoreCase(PGII))).forEach((pg) -> {
            indexes.add(pg.getOriginalDatasetIndex());
        });
        return indexes;
    }

    /**
     *
     * @return
     */
    public VerticalLayout getHideCompBtn() {
        return hideCompBtn;
    }
//
//    public void showCompBtn(boolean show) {
////        topLayout.setVisible(show);
//
//    }

    private void updateSelectionManagerIndexes() {
        Map<String, QuantDiseaseGroupsComparison> filteredComp = new LinkedHashMap<>();
        selectedDsList.stream().forEach((comp) -> {
            String kI = comp.getComparisonHeader();
            String[] k1Arr = kI.split(" / ");
            String kII = k1Arr[1] + " / " + k1Arr[0];
            if (!(filteredComp.containsKey(kI) || filteredComp.containsKey(kII))) {
                filteredComp.put(kI, comp);

            }
        });
        Set<QuantDiseaseGroupsComparison> filteredSelectedDsList = new LinkedHashSet<>();
        filteredSelectedDsList.addAll(filteredComp.values());
        updateSelectionManager(filteredSelectedDsList);
    }

    /**
     *
     * @return
     */
    public Set<QuantDiseaseGroupsComparison> getAvailableComparisonsList() {
        return availableComparisonsList;
    }

    /**
     *
     * @param selectedDsList
     */
    public void updateSelectionManager(Set<QuantDiseaseGroupsComparison> selectedDsList) {
        ///to be overided
    }

    /**
     *
     */
    public void selectAll() {
        comparisonsCellsMap.values().stream().filter((cell) -> (cell.isVisible() && cell.getValue() != 0 && !cell.getComparison().getOreginalComparisonHeader().trim().equalsIgnoreCase("/") && availableComparisonsList.contains(cell.getComparison()))).map((cell) -> {

            cell.select();
            return cell;
        }).forEach((cell) -> {
            String kI = cell.getComparison().getComparisonHeader();
            String[] k1Arr = kI.split(" / ");
            String kII = k1Arr[1] + " / " + k1Arr[0];
            HeatmapCell equalCall = comparisonsCellsMap.get(kII);
            if (equalCall != null) {
                equalCall.select();
                cell.getComparison().setSwitchable(true);
                equalCall.getComparison().setSwitchable(true);
            } else {
                cell.getComparison().setSwitchable(false);
            }
            selectedCells.add(cell);
            selectedDsList.add(cell.getComparison());
        });
        updateSelectionManagerIndexes();
    }

    /**
     *
     */
    public void unselectAll() {
        clearSelection();
        updateSelectionManagerIndexes();

    }

    /**
     * Reset selection on dataset layout no selection manager update
     */
    public void clearSelection() {
        if (columnHeaderCells == null || colheadersSet.isEmpty()) {
            return;
        }
        for (HeaderCell header : columnHeaderCells) {
            header.unselect();
        }
        for (HeaderCell header : rowHeaderCells) {
            header.unselect();
        }

        selectedCells.clear();
        comparisonsCellsMap.values().stream().forEach((cell) -> {
            cell.initialState();
        });
        selectedDsList.clear();
    }

    /**
     * Reset selection on dataset layout no selection manager update
     */
    public void selectComparisons(Set<QuantDiseaseGroupsComparison> comparisonsToSelect) {
        selectedCells.clear();
        selectedDsList.clear();
        comparisonsCellsMap.values().stream().forEach((cell) -> {
            if (comparisonsToSelect.contains(cell.getComparison())) {
                this.selectedDsList.add(cell.getComparison());
                this.selectedCells.add(cell);
                String kI = cell.getComparison().getComparisonHeader();
                String[] k1Arr = kI.split(" / ");
                String kII = k1Arr[1] + " / " + k1Arr[0];
                HeatmapCell equalCall = comparisonsCellsMap.get(kII);
                if (equalCall != null) {
                    equalCall.select();
                    this.selectedCells.add(equalCall);
                    cell.getComparison().setSwitchable(true);
                    equalCall.getComparison().setSwitchable(true);
                } else {
                    cell.getComparison().setSwitchable(false);
                }
                cell.select();
                this.selectedCells.add(cell);

            }

        });
//        selectedDsList.addAll(comparisonsToSelect);

    }

    /**
     * Reset selection on dataset layout no selection manager update
     */
    public void selectComparisonsByID(Set<Integer> comparisonsToSelect) {
        Set<QuantDiseaseGroupsComparison> comparisons = new LinkedHashSet<>();
        comparisonsCellsMap.values().stream().forEach((cell) -> {
            for (int dsId : comparisonsToSelect) {
                if (cell.getComparison().getDatasetMap().containsKey(dsId)) {
                    comparisons.add(cell.getComparison());
                }
            }

            cell.unselect();
        });

        Map<String, QuantDiseaseGroupsComparison> filteringMap = new HashMap<>();
        comparisons.stream().forEach((comparison) -> {
            String kI = comparison.getComparisonHeader();
            String[] k1Arr = kI.split(" / ");
            String kII = k1Arr[1] + " / " + k1Arr[0];
            if (!(filteringMap.containsKey(kII) || filteringMap.containsKey(kI))) {
                filteringMap.put(kI, comparison);
            }
        });
        comparisons.clear();
        comparisons.addAll(filteringMap.values());
        selectComparisons(comparisons);
        updateSelectionManager(selectedDsList);

    }

    private final HeatMapImgGenerator gen = new HeatMapImgGenerator();

    private String getHMThumbImg() {
//        String imgUrl = gen.generateHeatmap(rowsColors, columnsColors, dataValuesColors, availableHMWidth, availableHMHeight,resetRows,resetcolumns);
        String imgUrl = gen.generateHeatmap(rowheadersSet, colheadersSet, dataValuesColors, availableHMWidth, availableHMHeight, resetRows, resetcolumns);
        heatmapPanel.setWidth(gen.getPanelWidth(), Unit.PIXELS);
        heatmapPanel.setHeight(gen.getPanelWidth(), Unit.PIXELS);
//        heatMapImg.setSource(new ExternalResource(imgUrl));
        updateHeatmapComponents();

        return gen.generateHeatmap(rowheadersSet, colheadersSet, dataValuesColors, 100, 100, resetRows, resetcolumns);

//        return imgUrl;
    }

    private String lastPaddingStyle = "";

    private void selectDiseaseCategory(String diseaseCategoryName) {

        if (singleSelection) {
            selectedCells.stream().forEach((tcell) -> {
                tcell.unselect();
            });
            selectedDsList.clear();
            selectedCells.clear();

        }
        for (HeaderCell rowHeaderCell : rowHeaderCells) {
            if (rowHeaderCell.getDiseaseCategory().equalsIgnoreCase(diseaseCategoryName)) {
                rowHeaderCell.select();
            } else if (singleSelection) {
                rowHeaderCell.unselect();
            }

        }
        for (HeaderCell colHeaderCell : columnHeaderCells) {
            if (colHeaderCell.getDiseaseCategory().equalsIgnoreCase(diseaseCategoryName)) {
                colHeaderCell.select();
            } else if (singleSelection) {
                colHeaderCell.unselect();
            }

        }

        comparisonsCellsMap.values().stream().filter((cell) -> (cell.getDiseaseCategory().equalsIgnoreCase(diseaseCategoryName) && cell.isVisible() && cell.getValue() != 0 && !cell.getComparison().getOreginalComparisonHeader().trim().equalsIgnoreCase("/") && availableComparisonsList.contains(cell.getComparison()))).map((cell) -> {

            cell.select();
            return cell;
        }).forEach((cell) -> {
            String kI = cell.getComparison().getComparisonHeader();
            String[] k1Arr = kI.split(" / ");
            String kII = k1Arr[1] + " / " + k1Arr[0];
            HeatmapCell equalCall = comparisonsCellsMap.get(kII);
            if (equalCall != null) {
                equalCall.select();
                cell.getComparison().setSwitchable(true);
                equalCall.getComparison().setSwitchable(true);
            } else {
                cell.getComparison().setSwitchable(false);
            }
            selectedCells.add(cell);
            selectedDsList.add(cell.getComparison());
        });
        updateSelectionManagerIndexes();

    }

    private void updateHeatmapComponents() {
        int calcWidth = 0, calcHeight = 0;
        this.heatmapComponentContainer.removeAllComponents();
        cornerCell.setWidth((int) (180 * gen.getResizeFactor()), Unit.PIXELS);
        cornerCell.setHeight(cornerCell.getWidth(), cornerCell.getWidthUnits());

        double resizeFilterBtn = 1;
        if (cornerCell.getWidth() < 115) {
            resizeFilterBtn = cornerCell.getWidth() / 115;

        }
        filterResizeController.resizeFilters(resizeFilterBtn);

        this.heatmapComponentContainer.addComponent(cornerCell, "left: 0px; top: 0px");

        Map<String, Rectangle> headerLabelMap = gen.getHeaderLabelMap();
        int i = (int) cornerCell.getWidth(), j = (int) cornerCell.getHeight();
        for (String dcat : headerLabelMap.keySet()) {
            String title = dcat.split("__")[1];

            if (dcat.contains("col__")) {

                HeaderCell headerLabel = new HeaderCell(title, 0, title, title, false) {

                    @Override
                    public void selectData(String diseaseCategoryName) {
                        selectDiseaseCategory(diseaseCategoryName.split("__")[0]);
                        this.removeStyleName("hmselectedcell");
                    }

                    @Override
                    public void unSelectData(String cellHeader) {

//                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                };
                headerLabel.addStyleName("pointer");
                headerLabel.setWidth((int) (headerLabelMap.get(dcat).width - 2), Unit.PIXELS);
                headerLabel.setHeight((int) (headerLabelMap.get(dcat).height), Unit.PIXELS);

                this.heatmapComponentContainer.addComponent(headerLabel, "left: " + (i + 1) + "px; top: " + 0 + "px");
                i += headerLabel.getWidth() + 2;

            } else {
                HeaderCell headerLabel = new HeaderCell(title, 0, title, title, true) {

                    @Override
                    public void selectData(String diseaseCategoryName) {
                        selectDiseaseCategory(diseaseCategoryName.split("__")[0]);
                        this.removeStyleName("hmselectedcell");
                    }

                    @Override
                    public void unSelectData(String cellHeader) {
                    }
                };

                headerLabel.addStyleName("pointer");
                headerLabel.addStyleName("topbottomborder");
                headerLabel.setWidth((int) (headerLabelMap.get(dcat).width) + 2, Unit.PIXELS);
                headerLabel.setHeight((int) (headerLabelMap.get(dcat).height), Unit.PIXELS);
                this.heatmapComponentContainer.addComponent(headerLabel, "left: " + (0) + "px; top: " + (j) + "px");
                j += headerLabel.getHeight();

            }
        }

        int x = (int) (180 * gen.getResizeFactor());
        int y = (int) (30 * gen.getResizeFactor());
        int cellWidth = (int) (30 * gen.getResizeFactor());

        for (HeaderCell headerCell : columnHeaderCells) {
//            VerticalLayout hcell = new VerticalLayout();
            headerCell.setHeight((int) (150 * gen.getResizeFactor()), Unit.PIXELS);
            headerCell.setWidth(cellWidth, Unit.PIXELS);
            headerCell.setStyleName("heatmapcell");

            this.heatmapComponentContainer.addComponent(headerCell, "left: " + x + "px; top: " + y + "px");
            x += cellWidth;

        }
        calcWidth += x;
        y = (int) (180 * gen.getResizeFactor());
        x = (int) (30 * gen.getResizeFactor());
        for (HeaderCell headerCell : rowHeaderCells) {
//            VerticalLayout hcell = new VerticalLayout();
            headerCell.setWidth((int) (150 * gen.getResizeFactor()), Unit.PIXELS);
            headerCell.setHeight(cellWidth, Unit.PIXELS);
            headerCell.setStyleName("heatmapcell");

            this.heatmapComponentContainer.addComponent(headerCell, "left: " + x + "px; top: " + y + "px");
            y += cellWidth;
//           
        }
        calcHeight += y;

        x = (int) (180 * gen.getResizeFactor());
        y = (int) (180 * gen.getResizeFactor());
        for (HeatmapCell[] row : cellTable) {
            for (HeatmapCell cell : row) {
//                VerticalLayout cell = new VerticalLayout();
                cell.setWidth(cellWidth, Unit.PIXELS);
                cell.setHeight(cellWidth, Unit.PIXELS);
                cell.setStyleName("heatmapcell");
                this.heatmapComponentContainer.addComponent(cell, "left: " + x + "px; top: " + y + "px");
                x += cellWidth;
            }
            x = (int) (180 * gen.getResizeFactor());
            y += cellWidth;
        }

        heatmapPanel.setHeight(calcHeight, Unit.PIXELS);
        heatmapPanel.setWidth(calcWidth, Unit.PIXELS);
        int padding = availableHMHeight - calcHeight - 18;
        this.removeStyleName(lastPaddingStyle);
        if (padding <= 0) {
            lastPaddingStyle = "padding2";
        } else if (padding <= 5) {
            lastPaddingStyle = "padding5";
        } else if (padding > 5 && padding < 10) {
            lastPaddingStyle = "padding10";
        } else {
            lastPaddingStyle = "padding15";
        }
        this.addStyleName(lastPaddingStyle);

    }

//    public abstract void showHideFilters();
    /**
     * this method to update heatmap Thumb button on updating the heatmap thumb
     * image and number of datasets
     */
    public abstract void updateHMThumb(String imgUrl, int datasetNumber, int deactivatedDatasetNumber, Map<QuantDiseaseGroupsComparison, QuantDiseaseGroupsComparison> equalComparisonMap);

}
