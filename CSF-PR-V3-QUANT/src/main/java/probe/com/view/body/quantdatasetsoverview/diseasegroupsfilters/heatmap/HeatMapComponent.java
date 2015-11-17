/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.heatmap;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
import probe.com.model.beans.quant.QuantDSIndexes;

/**
 *
 * @author Yehia Farag
 */
public class HeatMapComponent extends VerticalLayout {

    private final GridLayout heatmapBody;
    private final GridLayout columnHeader;
    private final GridLayout rowHeader;
//    private final PatientsGroup[] patientsGroupArr;

    private HeaderCell[] columnCells;
    private HeaderCell[] rowCells;
    private final Map<String, HeatmapCell> comparisonsCellsMap = new LinkedHashMap<String, HeatmapCell>();
    private final Set<QuantDiseaseGroupsComparison> selectedDsList = new TreeSet<QuantDiseaseGroupsComparison>();
    private final Set<QuantDiseaseGroupsComparison> availableComparisonsList = new TreeSet<QuantDiseaseGroupsComparison>();

    private boolean singleSelection = true;
    private boolean selfSelection = false;
    private final int heatmapCellWidth;

    /**
     *
     * @return
     */
    public VerticalLayout getHideCompBtn() {
        return hideCompBtn;
    }

    private final VerticalLayout hideCompBtn;
    private final HorizontalLayout topLayout;
    private final HorizontalLayout bottomLayout;

    /**
     * @param heatmapCellWidth
     */
    public HeatMapComponent(int heatmapCellWidth) {
        this.setMargin(false);
        this.setSpacing(true);
        this.setWidth("100%");
        this.heatmapCellWidth = heatmapCellWidth;

        this.columnHeader = new GridLayout();
        this.rowHeader = new GridLayout();
        this.heatmapBody = new GridLayout();

        topLayout = new HorizontalLayout();
        topLayout.setHeight("150px");
        VerticalLayout spacer = new VerticalLayout();
        spacer.setWidth("150px");
        spacer.setHeight("150px");
        spacer.setStyleName(Reindeer.LAYOUT_WHITE);

        hideCompBtn = new VerticalLayout();
        hideCompBtn.setWidth("150px");
        hideCompBtn.setHeight("150px");
        hideCompBtn.setDescription("Hide Comparisons Table");
        hideCompBtn.setStyleName("matrixbtn");
        Label l = new Label("Hide Comparisons");
        hideCompBtn.addComponent(l);
        hideCompBtn.setComponentAlignment(l, Alignment.BOTTOM_CENTER);
        spacer.addComponent(hideCompBtn);
        spacer.setComponentAlignment(hideCompBtn, Alignment.MIDDLE_CENTER);
        hideCompBtn.setEnabled(false);

        topLayout.addComponent(spacer);
        topLayout.setSpacing(true);
        topLayout.addComponent(columnHeader);
        topLayout.setComponentAlignment(columnHeader, Alignment.TOP_LEFT);
        this.addComponent(topLayout);

        bottomLayout = new HorizontalLayout();
        rowHeader.setWidth("150px");
        rowHeader.setHeight("100%");
        bottomLayout.addComponent(rowHeader);
        bottomLayout.addComponent(heatmapBody);
        bottomLayout.setSpacing(true);
        bottomLayout.setComponentAlignment(heatmapBody, Alignment.MIDDLE_LEFT);
        this.addComponent(bottomLayout);

    }

    /**
     *
     * @param singleSelection
     */
    public void setSingleSelection(boolean singleSelection) {
        this.singleSelection = singleSelection;
    }

    /**
     *
     * @param rowsLbels
     * @param columnsLbels
     * @param values
     * @param maxDatasetValue
     */
    public void updateHeatMap(Set<String> rowsLbels, Set<String> columnsLbels, QuantDSIndexes[][] values, int maxDatasetValue) {
        if (rowsLbels.isEmpty() || columnsLbels.isEmpty()) {
            return;
        }
        updateHeatMapLayout(values, rowsLbels, columnsLbels, maxDatasetValue);

    }

    private final Set<HeatmapCell> selectedCells = new HashSet<HeatmapCell>();

    /**
     *
     * @param comparison
     * @param cell
     */
    protected void addSelectedDs(QuantDiseaseGroupsComparison comparison, HeatmapCell cell) {
        if (selectedDsList.isEmpty()) {
            //reset all cells to transpairent 
            for (HeatmapCell hmcell : comparisonsCellsMap.values()) {
                hmcell.unselect();
            }

        }
        for (HeaderCell header : columnCells) {
            header.unSelectCellStyle();
        }
        for (HeaderCell header : rowCells) {
            header.unSelectCellStyle();
        }
        if (singleSelection) {
            for (HeatmapCell tcell : selectedCells) {
                tcell.unselect();
            }
            selectedDsList.clear();
            selectedCells.clear();
        }

        this.selectedDsList.add(comparison);
        this.selectedCells.add(cell);
        String kI = cell.getComparison().getComparisonHeader();
        String[] k1Arr = kI.split(" vs ");
        String kII = k1Arr[1] + " vs " + k1Arr[0];
        HeatmapCell equalCall = comparisonsCellsMap.get(kII);
        equalCall.select();
//        this.selectedDsList.add(equalCall.getComparison());
        this.selectedCells.add(cell);
//        this.selectedDsList.add(equalCall.getComparison());
        this.selectedCells.add(equalCall);
        updateSelectionManagerIndexes();
    }

    /**
     *
     * @param selectedheader
     */
    protected void addRowSelectedDs(String selectedheader) {
        if (selectedDsList.isEmpty()) {
            //reset all cells to transpairent 
            for (HeatmapCell hmcell : comparisonsCellsMap.values()) {
                hmcell.unselect();
            }

        }
        if (singleSelection) {
            for (HeatmapCell tcell : selectedCells) {
                tcell.unselect();
            }
            selectedDsList.clear();
            selectedCells.clear();

            for (HeaderCell header : columnCells) {
                if (header.getValueLabel().equalsIgnoreCase(selectedheader)) {
                    for (HeatmapCell tcell : header.getIncludedCells()) {
                        tcell.select();
                    }
                    header.selectCellStyle();
                    selectedCells.addAll(header.getIncludedCells());

                    continue;
                }
                header.unSelectCellStyle();
            }
            for (HeaderCell header : rowCells) {
                if (header.getValueLabel().equalsIgnoreCase(selectedheader)) {
                    for (HeatmapCell tcell : header.getIncludedCells()) {
                        tcell.select();
                    }
                    header.selectCellStyle();
                    selectedCells.addAll(header.getIncludedCells());

                    for (QuantDiseaseGroupsComparison qdComp : header.getIncludedComparisons()) {
                        String kI = qdComp.getComparisonHeader();
                        if (!kI.startsWith(selectedheader.replace("<center>", "").replace("</center>", "").trim() + " vs ")) {
                            String[] k1Arr = kI.split(" vs ");
                            String kII = k1Arr[1] + " vs " + k1Arr[0];
                            System.out.println("before " + qdComp.getComparisonHeader());
                            qdComp.setComparisonHeader(kII);
                            System.out.println("after " + qdComp.getComparisonHeader());

                        }
                        selectedDsList.add(qdComp);

                    }

//                    selectedDsList.addAll(header.getIncludedComparisons());
                    continue;
                }
                header.unSelectCellStyle();
            }

        } else {

            for (HeaderCell header : columnCells) {
                if (header.getValueLabel().equalsIgnoreCase(selectedheader)) {
                    for (HeatmapCell tcell : header.getIncludedCells()) {
                        tcell.select();
                    }
                    header.selectCellStyle();
                    selectedCells.addAll(header.getIncludedCells());
                    for (QuantDiseaseGroupsComparison qdComp : header.getIncludedComparisons()) {
                        String kI = qdComp.getComparisonHeader();
                        if (!kI.startsWith(selectedheader.replace("<center>", "").replace("</center>", "").trim() + " vs ")) {
                            String[] k1Arr = kI.split(" vs ");
                            String kII = k1Arr[1] + " vs " + k1Arr[0];
                            qdComp.setComparisonHeader(kII);

                        }
                        selectedDsList.add(qdComp);

                    }
//                    selectedDsList.addAll(header.getIncludedComparisons());
                    break;
                }
            }
            for (HeaderCell header : rowCells) {
                if (header.getValueLabel().equalsIgnoreCase(selectedheader)) {
                    for (HeatmapCell tcell : header.getIncludedCells()) {
                        tcell.select();
                    }
                    header.selectCellStyle();
                    selectedCells.addAll(header.getIncludedCells());
                    break;
                }
            }

        }
//        Set<QuantDiseaseGroupsComparison> tempSelectedDsList = new HashSet<QuantDiseaseGroupsComparison>();
//        for (QuantDiseaseGroupsComparison gr : selectedDsList) {
//            String kI = gr.getComparisonHeader();
//            String[] k1Arr = kI.split(" vs ");
//            if (! k1Arr[0].contains(selectedheader)) {
//
//                gr.setComparisonHeader(k1Arr[1] + " vs " + k1Arr[0]);
//            }
//            tempSelectedDsList.add(gr);
//
//        }
//        selectedDsList.clear();
//        selectedDsList.addAll(tempSelectedDsList);
        updateSelectionManagerIndexes();
    }

    /**
     *
     * @param selectedDsList
     */
    public void updateDsCellSelection(Set<QuantDiseaseGroupsComparison> selectedDsList) {
        if (selectedDsList.isEmpty()) {
            hideCompBtn.setEnabled(false);
        } else {
            hideCompBtn.setEnabled(true);
        }

        if (selfSelection) {
            selfSelection = false;
            return;
        }
        for (HeaderCell header : columnCells) {
            header.unSelectCellStyle();
        }
        for (HeaderCell header : rowCells) {
            header.unSelectCellStyle();
        }
        List<HeatmapCell> localSelectedCells = new ArrayList<HeatmapCell>();
        localSelectedCells.addAll(this.selectedCells);
        for (QuantDiseaseGroupsComparison gr : this.selectedDsList) {
            String kI = gr.getComparisonHeader();
            String[] k1Arr = kI.split(" vs ");
            String kII = k1Arr[1] + " vs " + k1Arr[0];
            Set<String> keymap = new HashSet<String>();
            keymap.add(kI);
            keymap.add(kII);
            if (!selectedDsList.contains(gr) && !selectedDsList.contains(comparisonsCellsMap.get(kII).getComparison())) {

                for (HeatmapCell cell : selectedCells) {
                    String kI2 = cell.getComparison().getComparisonHeader();
                    String[] kI2Arr = kI2.split(" vs ");
                    String kII2 = kI2Arr[1] + " vs " + kI2Arr[0];
                    if (keymap.contains(kI2) && keymap.contains(kII2)) {
                        cell.unselect();
                        localSelectedCells.remove(cell);

                    }

                }

            }
        }
        this.selectedCells.clear();
        this.selectedCells.addAll(localSelectedCells);
        this.selectedDsList.clear();
        this.selectedDsList.addAll(selectedDsList);
        if (this.selectedDsList.isEmpty()) {
            //reset all cells to transpairent 
            for (HeatmapCell hmcell : comparisonsCellsMap.values()) {
                hmcell.initStyle();
            }

        }

    }

    /**
     *
     * @param comparison
     * @param cell
     */
    protected void unSelectDs(QuantDiseaseGroupsComparison comparison, HeatmapCell cell) {
        for (HeaderCell header : columnCells) {
            header.unSelectCellStyle();
        }
        for (HeaderCell header : rowCells) {
            header.unSelectCellStyle();
        }
        this.selectedDsList.remove(comparison);
        this.selectedCells.remove(cell);
        String kI = cell.getComparison().getComparisonHeader();
        String[] k1Arr = kI.split(" vs ");
        String kII = k1Arr[1] + " vs " + k1Arr[0];
        HeatmapCell equalCall = comparisonsCellsMap.get(kII);
        equalCall.unselect();
        this.selectedDsList.remove(equalCall.getComparison());
        this.selectedCells.remove(equalCall);
        updateSelectionManagerIndexes();
        if (selectedCells.isEmpty()) {
            //reset all cells 
            for (HeatmapCell hmcell : comparisonsCellsMap.values()) {
                hmcell.initStyle();
            }

        } else {
            cell.unselect();
            equalCall.unselect();

        }

    }

    /**
     *
     * @param selectedHeadercell
     */
    protected void removeRowSelectedDs(String selectedHeadercell) {

        for (HeaderCell header : columnCells) {
            if (header.getValueLabel().equalsIgnoreCase(selectedHeadercell)) {
                for (HeatmapCell tcell : header.getIncludedCells()) {
                    tcell.unselect();
                }
                header.unSelectCellStyle();
                selectedCells.removeAll(header.getIncludedCells());
                selectedDsList.removeAll(header.getIncludedComparisons());
                break;
            }
        }
        for (HeaderCell header : rowCells) {
            if (header.getValueLabel().equalsIgnoreCase(selectedHeadercell)) {
                for (HeatmapCell tcell : header.getIncludedCells()) {
                    tcell.unselect();
                }
                header.unSelectCellStyle();
                selectedCells.removeAll(header.getIncludedCells());
                selectedDsList.removeAll(header.getIncludedComparisons());
                break;
            }
        }
        updateSelectionManagerIndexes();
        if (selectedDsList.isEmpty()) {
            //reset all cells to transpairent 
            for (HeatmapCell hmcell : comparisonsCellsMap.values()) {
                hmcell.initStyle();
            }
        }

    }

    private void updateSelectionManagerIndexes() {
        selfSelection = true;
        Map<String, QuantDiseaseGroupsComparison> filteredComp = new LinkedHashMap<String, QuantDiseaseGroupsComparison>();
        for (QuantDiseaseGroupsComparison comp : selectedDsList) {
            String kI = comp.getComparisonHeader();
            String[] k1Arr = kI.split(" vs ");
            String kII = k1Arr[1] + " vs " + k1Arr[0];
            if (filteredComp.containsKey(kI) || filteredComp.containsKey(kII)) {
                continue;
            }
            filteredComp.put(kI, comp);
        }
        Set<QuantDiseaseGroupsComparison> filteredSelectedDsList = new LinkedHashSet<QuantDiseaseGroupsComparison>();
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

    private void updateHeatMapLayout(QuantDSIndexes[][] values, Set<String> rowheaders, Set<String> colheaders, int maxDatasetValue) {
        HeatmapColorGenerator hmColorGen = new HeatmapColorGenerator(maxDatasetValue, 0);
        availableComparisonsList.clear();
        columnHeader.removeAllComponents();
        rowHeader.removeAllComponents();
        heatmapBody.removeAllComponents();
        heatmapBody.setMargin(new MarginInfo(false, true, true, false));

        columnHeader.setColumns(colheaders.size());
        columnHeader.setRows(1);
        columnHeader.setWidth((colheaders.size() * heatmapCellWidth) + "px");
        columnHeader.setHeight("150px");
        columnCells = new HeaderCell[colheaders.size()];

        rowHeader.setColumns(1);
        rowHeader.setRows(rowheaders.size());
        rowHeader.setWidth("150px");
        rowHeader.setHeight((heatmapCellWidth * rowheaders.size()) + "px");
        rowCells = new HeaderCell[rowheaders.size()];

        heatmapBody.setColumns(colheaders.size());
        heatmapBody.setRows(rowheaders.size());
        heatmapBody.setWidth((colheaders.size() * heatmapCellWidth) + "px");
        heatmapBody.setHeight((heatmapCellWidth * rowheaders.size()) + "px");

//init col headers
        for (int i = 0; i < colheaders.size(); i++) {
            String la = colheaders.toArray()[i].toString();
            if (la.equalsIgnoreCase("")) {
                la = "Not Available";
            }
            HeaderCell headerCell = new HeaderCell(false, la, i, HeatMapComponent.this, heatmapCellWidth);
            columnHeader.addComponent(headerCell, i, 0);
            columnHeader.setComponentAlignment(headerCell, Alignment.MIDDLE_CENTER);
            columnCells[i] = headerCell;
            this.selectedDsList.clear();
            updateSelectionManagerIndexes();

        }

        //init row headers
        for (int i = 0; i < rowheaders.size(); i++) {
            String la = rowheaders.toArray()[i].toString();
            if (la.equalsIgnoreCase("")) {
                la = "Not Available";
            }
            HeaderCell headerCell = new HeaderCell(true, la, i, HeatMapComponent.this, heatmapCellWidth);
            rowHeader.addComponent(headerCell, 0, i);
            rowHeader.setComponentAlignment(headerCell, Alignment.MIDDLE_CENTER);
            rowCells[i] = headerCell;
        }

        for (int x = 0; x < values.length; x++) {
            for (int y = 0; y < values[x].length; y++) {
                String headerTitle = rowheaders.toArray()[x].toString() + " vs " + colheaders.toArray()[y].toString();
                double value = values[x][y].getValue();
                String color = "#EFF2FB";
                if (!rowheaders.toArray()[x].toString().equalsIgnoreCase(colheaders.toArray()[y].toString())) {
                    color = hmColorGen.getColor((float) value);
                }

                HeatmapCell cell = new HeatmapCell(value, color, values[x][y].getIndexes(), x, y, null, HeatMapComponent.this, headerTitle, heatmapCellWidth);
                comparisonsCellsMap.put(headerTitle, cell);
                heatmapBody.addComponent(cell, y, x);
                if (cell.getComparison().getDatasetIndexes().length > 0) {
                    columnCells[y].addComparison(cell.getComparison(), cell);
                    rowCells[x].addComparison(cell.getComparison(), cell);
                    if (!cell.getComparison().getComparisonHeader().trim().equalsIgnoreCase("vs")) {
                        availableComparisonsList.add(cell.getComparison());
                    }
                }

            }

        }
        Set<String> keymap = new HashSet<String>();
        for (QuantDiseaseGroupsComparison gr : this.availableComparisonsList) {
            String kI = gr.getComparisonHeader();
            String[] k1Arr = kI.split(" vs ");
            String kII = k1Arr[1] + " vs " + k1Arr[0];
            keymap.add(kI);
            keymap.add(kII);

        }
        activeSelectAll = keymap.size() <= 10;

    }

    /**
     *
     * @return
     */
    public boolean isActiveSelectAll() {
        return activeSelectAll;
    }
    private boolean activeSelectAll;

    /**
     *
     * @param col
     * @param row
     */
    public void highlightHeaders(int col, int row) {
        columnCells[col].heighlightCellStyle();
        rowCells[row].heighlightCellStyle();
    }

    /**
     *
     * @param col
     * @param row
     */
    public void resetHeadersStyle(int col, int row) {
        columnCells[col].resetCellStyle();
        rowCells[row].resetCellStyle();

    }

    /**
     *
     * @param col
     * @param row
     */
    public void selectHeadersStyle(int col, int row) {
        columnCells[col].resetCellStyle();
        rowCells[row].resetCellStyle();

    }

    /**
     *
     * @param col
     * @param row
     */
    public void unSelectHeadersStyle(int col, int row) {
        columnCells[col].resetCellStyle();
        rowCells[row].resetCellStyle();

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
//        if (!isActiveSelectAll()) {
//            return;
//        }

        for (HeatmapCell cell : comparisonsCellsMap.values()) {
            if (cell.getValue() != 0 && !cell.getComparison().getComparisonHeader().trim().equalsIgnoreCase("vs") && availableComparisonsList.contains(cell.getComparison())) {
                cell.select();
                selectedCells.add(cell);
            }
        }
        updateDsCellSelection(availableComparisonsList);
        selectedDsList.clear();
        selectedDsList.addAll(availableComparisonsList);
        updateSelectionManagerIndexes();
    }

    /**
     *
     */
    public void unselectAll() {
        selectedCells.clear();
        for (HeatmapCell cell : comparisonsCellsMap.values()) {
            cell.initStyle();
        }
        updateDsCellSelection(new HashSet<QuantDiseaseGroupsComparison>());
        selectedDsList.clear();
        selectedDsList.addAll(new HashSet<QuantDiseaseGroupsComparison>());
        updateSelectionManagerIndexes();

    }
    private boolean visibleComponent = true;

    public boolean isVisibleComponent() {
        return visibleComponent;
    }

    @Override
    public void setVisible(boolean visible) {
        visibleComponent = visible;
        bottomLayout.setVisible(visible);
        columnHeader.setVisible(visible);
        if (visible) {
            this.setWidth("100%");
        } else {
            this.setWidthUndefined();
        }

    }

}
