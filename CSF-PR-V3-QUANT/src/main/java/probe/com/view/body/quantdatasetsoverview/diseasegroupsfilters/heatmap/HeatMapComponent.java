/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.heatmap;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
import probe.com.model.beans.quant.QuantDSIndexes;
import probe.com.model.beans.quant.QuantDatasetObject;

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
    private final Set<QuantDiseaseGroupsComparison> selectedDsList = new LinkedHashSet<QuantDiseaseGroupsComparison>();
    private final Set<QuantDiseaseGroupsComparison> availableComparisonsList = new LinkedHashSet<QuantDiseaseGroupsComparison>();

    private boolean singleSelection = true;
    private boolean selfSelection = false;
    private final int heatmapCellWidth;
    private final int heatmapHeaderCellWidth;

    private final VerticalLayout diseaseGroupsRowsLabels;
    private final HorizontalLayout diseaseGroupsColumnsLabels;

    private final Map<String, String> diseaseStyleMap;

    //heatmap swing data
    private String[] rowsColors;
    private String[] columnsColors;
    private String[][] dataColors;

    /**
     *
     * @return
     */
    public VerticalLayout getHideCompBtn() {
        return hideCompBtn;
    }

    public void showCompBtn(boolean show) {
        topLayout.setVisible(show);

    }

    private final VerticalLayout hideCompBtn, spacer;
    private final HorizontalLayout topLayout;
    private final HorizontalLayout bottomLayout;
    private final VerticalLayout columnContainer;
    private final Image icon;
    private final Label hideShowBtnLabel;
    private final ThemeResource defaultResource;

    /**
     * @param heatmapCellWidth
     * @param heatmapHeaderCellWidth
     * @param diseaseStyleMap
     */
    public HeatMapComponent(int heatmapCellWidth, int heatmapHeaderCellWidth, Map<String, String> diseaseStyleMap) {

        this.diseaseStyleMap = diseaseStyleMap;

        this.setMargin(false);
        this.setSpacing(true);
        this.setWidth("100%");
        this.heatmapCellWidth = heatmapCellWidth;
        this.heatmapHeaderCellWidth = heatmapHeaderCellWidth;

        columnContainer = new VerticalLayout();
        columnContainer.setWidth("100%");
        columnContainer.setSpacing(true);

        diseaseGroupsColumnsLabels = new HorizontalLayout();
        diseaseGroupsColumnsLabels.setWidth("10px");
        diseaseGroupsColumnsLabels.setHeight("20px");
        diseaseGroupsColumnsLabels.setStyleName(Reindeer.LAYOUT_BLUE);
        columnContainer.addComponent(diseaseGroupsColumnsLabels);

        this.columnHeader = new GridLayout();
        columnContainer.addComponent(columnHeader);
        columnContainer.setComponentAlignment(columnHeader, Alignment.TOP_LEFT);
        this.rowHeader = new GridLayout();
        this.heatmapBody = new GridLayout();
        heatmapBody.setStyleName("hmbackground");

        topLayout = new HorizontalLayout();
        topLayout.setHeight((heatmapHeaderCellWidth + 24) + "px");
        topLayout.setSpacing(false);
        spacer = new VerticalLayout();
        spacer.setWidth((heatmapHeaderCellWidth + 25) + "px");
        spacer.setHeight((heatmapHeaderCellWidth + 24) + "px");
        spacer.setStyleName(Reindeer.LAYOUT_WHITE);

        hideCompBtn = new VerticalLayout();
        hideCompBtn.setSpacing(true);
        hideCompBtn.setWidth((heatmapHeaderCellWidth + 25) + "px");
//        hideCompBtn.setHeight((heatmapHeaderCellWidth) + "px");
        hideCompBtn.setStyleName("matrixbtn");

        icon = new Image();
        defaultResource = new ThemeResource("img/logo.png");

        icon.setSource(defaultResource);
        hideCompBtn.setDescription("Expand chart and hide comparisons table");
        hideCompBtn.addComponent(icon);
        hideCompBtn.setComponentAlignment(icon, Alignment.MIDDLE_CENTER);
        icon.setHeight((heatmapHeaderCellWidth - 20 + 10) + "px");
        hideShowBtnLabel = new Label("Expand Chart");
        hideShowBtnLabel.setHeight((20) + "px");
        hideCompBtn.addComponent(hideShowBtnLabel);
        hideCompBtn.setComponentAlignment(hideShowBtnLabel, Alignment.BOTTOM_CENTER);

        spacer.addComponent(hideCompBtn);
        spacer.setComponentAlignment(hideCompBtn, Alignment.MIDDLE_CENTER);
        hideCompBtn.setEnabled(false);

        topLayout.addComponent(spacer);
        topLayout.setComponentAlignment(spacer, Alignment.BOTTOM_LEFT);
        topLayout.setSpacing(true);
        topLayout.addComponent(columnContainer);
        topLayout.setComponentAlignment(columnContainer, Alignment.TOP_LEFT);
        this.addComponent(topLayout);

        bottomLayout = new HorizontalLayout();
        diseaseGroupsRowsLabels = new VerticalLayout();
        diseaseGroupsRowsLabels.setHeight("10px");
        diseaseGroupsRowsLabels.setWidth("20px");
        diseaseGroupsRowsLabels.setStyleName(Reindeer.LAYOUT_BLUE);
        bottomLayout.addComponent(diseaseGroupsRowsLabels);

        rowHeader.setWidth(heatmapHeaderCellWidth + "px");
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
     * @param diseaseFullNameMap
     * @param fullDsMap
     */
    public void updateHeatMap(Set<String> rowsLbels, Set<String> columnsLbels, QuantDSIndexes[][] values, int maxDatasetValue, Map<String, String> diseaseFullNameMap, Map<Integer, QuantDatasetObject> fullDsMap) {
        if (rowsLbels.isEmpty() || columnsLbels.isEmpty()) {
            return;
        }
        updateHeatMapLayout(values, rowsLbels, columnsLbels, maxDatasetValue, diseaseFullNameMap, fullDsMap);

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
        String kI = cell.getComparison().getOreginalComparisonHeader();
        String[] k1Arr = kI.split(" / ");
        String kII = k1Arr[1] + " / " + k1Arr[0];
        HeatmapCell equalCall = comparisonsCellsMap.get(kII);
        if (equalCall != null) {
            equalCall.select(false);
        } else {
            System.out.println("at equal was null " + kII);
        }
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
                        tcell.select(false);
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
                        tcell.select(true);
                    }
                    header.selectCellStyle();
                    selectedCells.addAll(header.getIncludedCells());

                    for (QuantDiseaseGroupsComparison qdComp : header.getIncludedComparisons()) {
                        String kI = qdComp.getOreginalComparisonHeader();
                        if (!kI.startsWith(selectedheader.replace("<center>", "").replace("</center>", "").trim() + " / ")) {
                            String[] k1Arr = kI.split(" / ");
                            String kII = k1Arr[1] + " / " + k1Arr[0];
                            qdComp.setComparisonHeader(kII);

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
                        tcell.select(false);
                    }
                    header.selectCellStyle();
                    selectedCells.addAll(header.getIncludedCells());
                    for (QuantDiseaseGroupsComparison qdComp : header.getIncludedComparisons()) {
                        String kI = qdComp.getOreginalComparisonHeader();
                        if (!kI.startsWith(selectedheader.replace("<center>", "").replace("</center>", "").trim() + " / ")) {
//                            String[] k1Arr = kI.split(" / ");
//                            String kII = k1Arr[1] + " / " + k1Arr[0];
//                            qdComp.setComparisonHeader(kII);

                        } else {
                            selectedDsList.add(qdComp);
                        }

                    }
//                    selectedDsList.addAll(header.getIncludedComparisons());
                    break;
                }
            }
            for (HeaderCell header : rowCells) {
                if (header.getValueLabel().equalsIgnoreCase(selectedheader)) {
                    for (HeatmapCell tcell : header.getIncludedCells()) {
                        tcell.select(true);
                    }
                    header.selectCellStyle();
                    selectedCells.addAll(header.getIncludedCells());
                    for (QuantDiseaseGroupsComparison qdComp : header.getIncludedComparisons()) {
                        String kI = qdComp.getOreginalComparisonHeader();
                        if (!kI.startsWith(selectedheader.replace("<center>", "").replace("</center>", "").trim() + " / ")) {
//                            String[] k1Arr = kI.split(" / ");
//                            String kII = k1Arr[1] + " / " + k1Arr[0];
//                            qdComp.setComparisonHeader(kII);

                        } else {
                            selectedDsList.add(qdComp);
                        }

                    }

                    break;
                }
            }

        }
//        Set<QuantDiseaseGroupsComparison> tempSelectedDsList = new HashSet<QuantDiseaseGroupsComparison>();
//        for (QuantDiseaseGroupsComparison gr : selectedDsList) {
//            String kI = gr.getOreginalComparisonHeader();
//            String[] k1Arr = kI.split("/");
//            if (! k1Arr[0].contains(selectedheader)) {
//
//                gr.setComparisonHeader(k1Arr[1] + "/" + k1Arr[0]);
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
            icon.setSource(defaultResource);
            hideShowBtnLabel.setValue("CSF-PR v2.0");
            hideCompBtn.setEnabled(false);

        } else {
            hideCompBtn.setEnabled(true);
        }

        if (selfSelection) {
            selfSelection = false;
            return;
        }
        if (columnCells == null) {
            System.out.println("at header = null is null");
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
            String kI = gr.getOreginalComparisonHeader();
            String[] k1Arr = kI.split(" / ");
            String kII = k1Arr[1] + " / " + k1Arr[0];
            Set<String> keymap = new HashSet<String>();
            keymap.add(kI);
            keymap.add(kII);

            if (!selectedDsList.contains(gr) && (comparisonsCellsMap.get(kII) != null && !selectedDsList.contains(comparisonsCellsMap.get(kII).getComparison()))) {

                for (HeatmapCell cell : selectedCells) {
                    String kI2 = cell.getComparison().getOreginalComparisonHeader();
                    String[] kI2Arr = kI2.split(" / ");
                    String kII2 = kI2Arr[1] + " / " + kI2Arr[0];
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
        String kI = cell.getComparison().getOreginalComparisonHeader();
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
            String kI = comp.getOreginalComparisonHeader();
            String[] k1Arr = kI.split(" / ");
            String kII = k1Arr[1] + " / " + k1Arr[0];
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

    private VerticalLayout initDiseaseGroupLabel(String dName, int itemsNumb, boolean row) {
        VerticalLayout diseaseLabelContainer = new VerticalLayout();

        Label label = new Label("<center><font  color='#ffffff'>" + dName.replace("_", " ").replace("-", "'").replace("Disease", "") + "</font></center>");
        label.setContentMode(ContentMode.HTML);

        if (row) {
            diseaseLabelContainer.setHeight((itemsNumb * heatmapCellWidth) + "px");
            diseaseLabelContainer.setWidth("20px");
            VerticalLayout rotateContainer = new VerticalLayout();

            rotateContainer.setWidth((itemsNumb * heatmapCellWidth) + "px");
            rotateContainer.setHeight("20px");
            diseaseLabelContainer.addComponent(rotateContainer);

            rotateContainer.setStyleName("row_" + diseaseStyleMap.get(dName));
            rotateContainer.addComponent(label);
        } else {
            diseaseLabelContainer.addComponent(label);
            diseaseLabelContainer.setWidth((itemsNumb * heatmapCellWidth) + "px");
            diseaseLabelContainer.setHeight("20px");
            diseaseLabelContainer.setStyleName(diseaseStyleMap.get(dName));
        }
        diseaseLabelContainer.setDescription(dName.replace("_", " ").replace("-", "'"));

        return diseaseLabelContainer;

    }

    private void updateDiseaseHeadersLabel(Set<String> rowheaders, Set<String> colheaders) {

        this.diseaseGroupsRowsLabels.removeAllComponents();
        this.diseaseGroupsColumnsLabels.removeAllComponents();
        Map<String, TreeSet<Integer>> diseaseRowsMap = new LinkedHashMap<String, TreeSet<Integer>>();
        int index = 0;
        for (String header : rowheaders) {
            String dName = header.split("\n")[1];
            if (!diseaseRowsMap.containsKey(dName)) {
                diseaseRowsMap.put(dName, new TreeSet<Integer>());
            }
            TreeSet<Integer> rowIndex = diseaseRowsMap.get(dName);
            rowIndex.add(index);
            diseaseRowsMap.put(dName, rowIndex);
            index++;

        }
        boolean resetRows = false;
        int rowcounter = 0;
        Map<String, Integer> diseaseRowsSize = new LinkedHashMap<String, Integer>();
        for (String dName : diseaseRowsMap.keySet()) {
            TreeSet<Integer> rowIndex = diseaseRowsMap.get(dName);
            int y = rowIndex.first();

            int counter = 0;
            for (int x : rowIndex) {
                if (x == y) {
                    y++;
                    counter++;
                } else {
                    resetRows = true;

                }
                rowcounter++;

            }
            diseaseRowsSize.put(dName, counter);

        }

        Map<String, TreeSet<Integer>> diseaseColumnMap = new LinkedHashMap<String, TreeSet<Integer>>();
        index = 0;

        for (String header : colheaders) {
            String dName = header.split("\n")[1];
            if (!diseaseColumnMap.containsKey(dName)) {
                diseaseColumnMap.put(dName, new TreeSet<Integer>());
            }
            TreeSet<Integer> colIndex = diseaseColumnMap.get(dName);
            colIndex.add(index);
            diseaseColumnMap.put(dName, colIndex);
            index++;

        }
        boolean resetcolumns = false;
        int columnCounter = 0;
        Map<String, Integer> diseaseColumnSize = new LinkedHashMap<String, Integer>();
        for (String dName : diseaseColumnMap.keySet()) {
            TreeSet<Integer> rowIndex = diseaseColumnMap.get(dName);
            int y = rowIndex.first();
            int counter = 0;
            for (int x : rowIndex) {
                if (x == y) {
                    y++;
                    counter++;
                } else {
                    resetcolumns = true;

                }
                columnCounter++;

            }
            diseaseColumnSize.put(dName, counter);

        }

        diseaseGroupsRowsLabels.setHeightUndefined();
        if (resetRows) {

            int itemNum = (rowcounter / diseaseRowsMap.size());
            int rem = (rowcounter % diseaseRowsMap.size());
            for (String dName : diseaseRowsMap.keySet()) {
                VerticalLayout diseaseLableContainer = initDiseaseGroupLabel(dName, itemNum + rem, true);
                diseaseGroupsRowsLabels.addComponent(diseaseLableContainer);
                rem = 0;
            }

        } else {

            for (String dName : diseaseRowsMap.keySet()) {
                int itemNumb = diseaseRowsMap.get(dName).last() - diseaseRowsMap.get(dName).first() + 1;
                VerticalLayout diseaseLableContainer = initDiseaseGroupLabel(dName, itemNumb, true);
                diseaseGroupsRowsLabels.addComponent(diseaseLableContainer);
            }
        }

        diseaseGroupsColumnsLabels.setWidthUndefined();

        if (resetcolumns) {

            int itemNum = (columnCounter / diseaseColumnMap.size());
            int rem = (columnCounter % diseaseColumnMap.size());
            for (String dName : diseaseColumnMap.keySet()) {
                VerticalLayout diseaseLableContainer = initDiseaseGroupLabel(dName, itemNum + rem, false);
                diseaseGroupsColumnsLabels.addComponent(diseaseLableContainer);
                rem = 0;
            }

        } else {

            for (String dName : diseaseColumnMap.keySet()) {
                int itemNumb = diseaseColumnMap.get(dName).last() - diseaseColumnMap.get(dName).first() + 1;
                VerticalLayout diseaseLableContainer = initDiseaseGroupLabel(dName, itemNumb, false);
                diseaseGroupsColumnsLabels.addComponent(diseaseLableContainer);
            }
        }

    }

    private void updateHeatMapLayout(QuantDSIndexes[][] values, Set<String> rowheaders, Set<String> colheaders, int maxDatasetValue, Map<String, String> diseaseFullNameMap, Map<Integer, QuantDatasetObject> fullDsMap) {

        updateDiseaseHeadersLabel(rowheaders, colheaders);

        HeatmapColorGenerator hmColorGen = new HeatmapColorGenerator(maxDatasetValue, 0);
        availableComparisonsList.clear();
        columnHeader.removeAllComponents();
        rowHeader.removeAllComponents();
        heatmapBody.removeAllComponents();
        heatmapBody.setMargin(new MarginInfo(false, true, true, false));

        columnHeader.setColumns(colheaders.size());
        columnHeader.setRows(1);
        columnHeader.setWidth((colheaders.size() * heatmapCellWidth) + "px");
        columnHeader.setHeight(heatmapHeaderCellWidth + "px");
        columnCells = new HeaderCell[colheaders.size()];
        columnsColors = new String[colheaders.size()];

        rowHeader.setColumns(1);
        rowHeader.setRows(rowheaders.size());
        rowHeader.setWidth(heatmapHeaderCellWidth + "px");
        rowHeader.setHeight((heatmapCellWidth * rowheaders.size()) + "px");
        rowCells = new HeaderCell[rowheaders.size()];
        rowsColors = new String[rowheaders.size()];

        heatmapBody.setColumns(colheaders.size());
        heatmapBody.setRows(rowheaders.size());
        dataColors = new String[rowheaders.size()][colheaders.size()];
        heatmapBody.setWidth((colheaders.size() * heatmapCellWidth) + "px");
        heatmapBody.setHeight((heatmapCellWidth * rowheaders.size()) + "px");

//init col headers
        for (int i = 0; i < colheaders.size(); i++) {
            String title = colheaders.toArray()[i].toString();
            if (title.equalsIgnoreCase("")) {
                title = "Not Available";
            }
            HeaderCell headerCell = new HeaderCell(false, title, i, HeatMapComponent.this, heatmapCellWidth, heatmapHeaderCellWidth, diseaseFullNameMap.get(title.split("\n")[0]));
            columnHeader.addComponent(headerCell, i, 0);
            columnHeader.setComponentAlignment(headerCell, Alignment.MIDDLE_CENTER);
            columnCells[i] = headerCell;
            columnsColors[i] = headerCell.getColor();

        }
        //init row headers
        for (int i = 0; i < rowheaders.size(); i++) {
            String la = rowheaders.toArray()[i].toString();
            if (la.equalsIgnoreCase("")) {
                la = "Not Available";
            }
            HeaderCell headerCell = new HeaderCell(true, la, i, HeatMapComponent.this, heatmapCellWidth, heatmapHeaderCellWidth, diseaseFullNameMap.get(la.split("\n")[0]));
            rowHeader.addComponent(headerCell, 0, i);
            rowHeader.setComponentAlignment(headerCell, Alignment.MIDDLE_CENTER);
            rowCells[i] = headerCell;
            rowsColors[i] = headerCell.getColor();
        }

        for (int x = 0; x < values.length; x++) {
            for (int y = 0; y < values[x].length; y++) {
                String headerTitle = rowheaders.toArray()[x].toString() + " / " + colheaders.toArray()[y].toString();
                double value = values[x][y].getValue();
                String color = "#EFF2FB";
                if (!rowheaders.toArray()[x].toString().equalsIgnoreCase(colheaders.toArray()[y].toString())) {
                    color = hmColorGen.getColor((float) value);
                }

                int[] dsIndexes = values[x][y].getIndexes();
                Set<String> pubCounter = new HashSet<String>();
                for (int i : dsIndexes) {
                    QuantDatasetObject ds = fullDsMap.get(i);
                    pubCounter.add(ds.getPumedID());

                }
             
                String grI = headerTitle.split("/")[0].split("\n")[0];
                String grII = headerTitle.split("/")[1].split("\n")[0].trim();
                if (diseaseFullNameMap.containsKey(grI)) {
                    grI = diseaseFullNameMap.get(grI);
                }
                 if (diseaseFullNameMap.containsKey(grII)) {
                    grII = diseaseFullNameMap.get(grII);
                }
                    String fullCompName = grI+" / "+grII+" - "+headerTitle.split("/")[0].split("\n")[1].replace(grII, headerTitle).replace("_", " ").replace("Disease", "");

                HeatmapCell cell = new HeatmapCell(value, color, dsIndexes, x, y, null, HeatMapComponent.this, headerTitle, heatmapCellWidth, pubCounter.size(), fullCompName);
                comparisonsCellsMap.put(headerTitle, cell);
                heatmapBody.addComponent(cell, y, x);
                dataColors[x][y] = color;
                if (cell.getComparison().getDatasetIndexes().length > 0) {
                    columnCells[y].addComparison(cell.getComparison(), cell);
                    rowCells[x].addComparison(cell.getComparison(), cell);
                    if (!cell.getComparison().getOreginalComparisonHeader().trim().equalsIgnoreCase("/")) {
                        availableComparisonsList.add(cell.getComparison());
                    }
                }

            }

        }

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
            if (cell.getValue() != 0 && !cell.getComparison().getOreginalComparisonHeader().trim().equalsIgnoreCase("/") && availableComparisonsList.contains(cell.getComparison())) {
                cell.select(true);
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
        updateHideShowThumbImg(null);
        updateShowHideBtnLabel(null);
        for (HeatmapCell cell : comparisonsCellsMap.values()) {
            cell.initStyle();
        }
        updateDsCellSelection(new HashSet<QuantDiseaseGroupsComparison>());
        selectedDsList.clear();
//        selectedDsList.addAll(new HashSet<QuantDiseaseGroupsComparison>());
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
        columnContainer.setVisible(visible);
        if (visible) {
            this.setWidth("100%");
        } else {
            this.setWidthUndefined();
        }

    }
    private final HeatMapImgGenerator gen = new HeatMapImgGenerator();

    public void updateHideShowThumbImg(String imgUrl) {
        if (imgUrl == null) {
            icon.setSource(defaultResource);
            hideShowBtnLabel.setValue("CSF-PR v2.0");
            return;
        }
        if (imgUrl.equalsIgnoreCase("defaultResource")) {

            String url = gen.generateHeatmap(rowsColors, columnsColors, dataColors);
            icon.setSource(new ExternalResource(url));
//            icon.setSource(defaultResource);
        } else {
            icon.setSource(new ExternalResource(imgUrl));
        }
    }

    public void updateShowHideBtnLabel(Boolean show) {

        if (show == null) {

            hideShowBtnLabel.setValue("");
        } else if (show) {
            hideShowBtnLabel.setValue("Expand Chart");
        } else {
            hideShowBtnLabel.setValue("Show Comparisons");
        }

    }

}
