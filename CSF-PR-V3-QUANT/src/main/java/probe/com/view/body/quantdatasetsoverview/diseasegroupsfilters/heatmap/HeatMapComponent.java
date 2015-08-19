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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import probe.com.model.beans.GroupsComparison;
import probe.com.model.beans.QuantDSIndexes;

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
    private final Map<String, HeatmapCell> comparisonsCellsMap = new HashMap<String, HeatmapCell>();
    private final Set<GroupsComparison> selectedDsList = new HashSet<GroupsComparison>();
    private final Set<GroupsComparison> availableComparisonsList = new HashSet<GroupsComparison>();

    private boolean singleSelection = true;
    private boolean selfSelection = false;

    public VerticalLayout getHideCompBtn() {
        return hideCompBtn;
    }

    private final VerticalLayout hideCompBtn;

    public HeatMapComponent() {
        this.setMargin(false);
        this.setSpacing(true);
        this.setWidth("100%");
        this.columnHeader = new GridLayout();
        this.rowHeader = new GridLayout();
        this.heatmapBody = new GridLayout();

        HorizontalLayout h1Layout = new HorizontalLayout();
        VerticalLayout spacer = new VerticalLayout();
        spacer.setWidth("150px");
        spacer.setHeight("150px");
        spacer.setStyleName(Reindeer.LAYOUT_WHITE);

        hideCompBtn = new VerticalLayout();
        hideCompBtn.setWidth("150px");
        hideCompBtn.setHeight("150px");
        hideCompBtn.setDescription("Hide Comparison Table");
        hideCompBtn.setStyleName("matrixbtn");
        Label l = new Label("Hide Comparsions");
        hideCompBtn.addComponent(l);
        hideCompBtn.setComponentAlignment(l, Alignment.BOTTOM_CENTER);
        spacer.addComponent(hideCompBtn);
        spacer.setComponentAlignment(hideCompBtn, Alignment.MIDDLE_CENTER);
        hideCompBtn.setEnabled(false);

        h1Layout.addComponent(spacer);
        h1Layout.setSpacing(true);
        h1Layout.addComponent(columnHeader);
        h1Layout.setComponentAlignment(columnHeader, Alignment.TOP_LEFT);
        this.addComponent(h1Layout);

        final HorizontalLayout h2Layout = new HorizontalLayout();
        h2Layout.addComponent(rowHeader);
        h2Layout.addComponent(heatmapBody);
        h2Layout.setSpacing(true);
        h2Layout.setComponentAlignment(heatmapBody, Alignment.MIDDLE_LEFT);
        this.addComponent(h2Layout);

    }

    public void setSingleSelection(boolean singleSelection) {
        this.singleSelection = singleSelection;
    }

    public void updateHeatMap(Set<String> rowsLbels, Set<String> columnsLbels, QuantDSIndexes[][] values, int maxDatasetValue) {
        if (rowsLbels.isEmpty() || columnsLbels.isEmpty()) {
            return;
        }
        updateHeatMapLayout(values, rowsLbels, columnsLbels, maxDatasetValue);

    }

    private final List<HeatmapCell> selectedCells = new ArrayList<HeatmapCell>();

    protected void addSelectedDs(GroupsComparison comparison, HeatmapCell cell) {
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
        this.selectedDsList.add(comparison);
        this.selectedCells.add(cell);
        this.selectedDsList.add(equalCall.getComparison());
        this.selectedCells.add(equalCall);
        updateSelectionManagerIndexes();
    }

    protected void addRowSelectedDs(String selectedheader) {
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
                    selectedDsList.addAll(header.getIncludedComparisons());
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
                    selectedDsList.addAll(header.getIncludedComparisons());
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

        updateSelectionManagerIndexes();
    }

    public void updateDsCellSelection(Set<GroupsComparison> selectedDsList) {
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
        for (GroupsComparison gr : this.selectedDsList) {
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

    }

    protected void removeSelectedDs(GroupsComparison comparison, HeatmapCell cell) {
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

    }

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

    }

    private void updateSelectionManagerIndexes() {
        //filter datasets
        selfSelection = true;
        Map<String, GroupsComparison> filteredComp = new HashMap<String, GroupsComparison>();
        for (GroupsComparison comp : selectedDsList) {
            String kI = comp.getComparisonHeader();
            String[] k1Arr = kI.split(" vs ");
            String kII = k1Arr[1] + " vs " + k1Arr[0];
            if (filteredComp.containsKey(kI) || filteredComp.containsKey(kII)) {
                continue;
            }
            filteredComp.put(kI, comp);

        }
        Set<GroupsComparison> filteredSelectedDsList = new HashSet<GroupsComparison>();
        filteredSelectedDsList.addAll(filteredComp.values());

        updateSelectionManager(filteredSelectedDsList);

    }

    public Set<GroupsComparison> getAvailableComparisonsList() {
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
        columnHeader.setWidth((colheaders.size() * 50) + "px");
        columnHeader.setHeight("150px");
        columnCells = new HeaderCell[colheaders.size()];

        rowHeader.setColumns(1);
        rowHeader.setRows(rowheaders.size());
        rowHeader.setWidth("150px");
        rowHeader.setHeight((50 * rowheaders.size()) + "px");
        rowCells = new HeaderCell[rowheaders.size()];

        heatmapBody.setColumns(colheaders.size());
        heatmapBody.setRows(rowheaders.size());
        heatmapBody.setWidth((colheaders.size() * 50) + "px");
        heatmapBody.setHeight((50 * rowheaders.size()) + "px");

//init col headers
        for (int i = 0; i < colheaders.size(); i++) {
            String la = colheaders.toArray()[i].toString();
            if (la.equalsIgnoreCase("")) {
                la = "Not Available";
            }
            HeaderCell headerCell = new HeaderCell(false, la, i, HeatMapComponent.this);
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
            HeaderCell headerCell = new HeaderCell(true, la, i, HeatMapComponent.this);
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

                HeatmapCell cell = new HeatmapCell(value, color, values[x][y].getIndexes(), x, y, null, HeatMapComponent.this, headerTitle);
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
        for (GroupsComparison gr : this.availableComparisonsList) {
            String kI = gr.getComparisonHeader();
            String[] k1Arr = kI.split(" vs ");
            String kII = k1Arr[1] + " vs " + k1Arr[0];
            keymap.add(kI);
            keymap.add(kII);

        }
        activeSelectAll = keymap.size() <= 10;

    }

    public boolean isActiveSelectAll() {
        return activeSelectAll;
    }
    private boolean activeSelectAll;

    public void highlightHeaders(int col, int row) {
        columnCells[col].heighlightCellStyle();
        rowCells[row].heighlightCellStyle();
    }

    public void resetHeadersStyle(int col, int row) {
        columnCells[col].resetCellStyle();
        rowCells[row].resetCellStyle();

    }

    public void selectHeadersStyle(int col, int row) {
        columnCells[col].resetCellStyle();
        rowCells[row].resetCellStyle();

    }

    public void unSelectHeadersStyle(int col, int row) {
        columnCells[col].resetCellStyle();
        rowCells[row].resetCellStyle();

    }

    public void updateSelectionManager(Set<GroupsComparison> selectedDsList) {
        ///to be overided
    }

    public void selectAll() {
        if (!isActiveSelectAll()) {
            return;
        }

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
    
    public void unselectAll() {
        selectedCells.clear();
        for (HeatmapCell cell : comparisonsCellsMap.values()) {
            cell.unselect();
        }
        updateDsCellSelection(new HashSet<GroupsComparison>());
        selectedDsList.clear();
        selectedDsList.addAll(new HashSet<GroupsComparison>());
        updateSelectionManagerIndexes();

    }

}
