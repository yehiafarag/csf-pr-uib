package no.uib.probe.csf.pr.touch.view.components.heatmapsubcomponents;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseGroupComparison;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDSIndexes;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;

/**
 *
 * @author Yehia Farag this class represents Layout represents the main body for
 * the heatmap
 */
public class HeatMapLayout extends VerticalLayout {

    private final GridLayout heatmapBody;

    private final Set<HeatmapCell> selectedCells = new HashSet<>();

    private HeaderCell[] columnHeaderCells;
    private HeaderCell[] rowHeaderCells;
    private final Map<String, HeatmapCell> comparisonsCellsMap = new LinkedHashMap<>();
    private final Set<QuantDiseaseGroupsComparison> selectedDsList;
    private final Set<QuantDiseaseGroupsComparison> availableComparisonsList;

    private boolean singleSelection = true;
    private int heatmapCellWidth;
    private int heatmapCellHeight;

    //heatmap swing data
    private String[] rowsColors;
    private String[] columnsColors;
    private String[][] dataColors;

    private VerticalLayout hideCompBtn;

    private Image icon;
    private Label hideShowBtnLabel;
    private ThemeResource defaultResource;

    private final int availableHMHeight, availableHMWidth;

    private final HorizontalLayout topLayout;
    private final  HorizontalLayout bottomLayout, columnCategoryHeadersContainer;
    private final  VerticalLayout spacer;

    private final  VerticalLayout rawCategoryHeadersContainer;
    private final  VerticalLayout diseaseGroupsRowsLabels;
    private final  HorizontalLayout diseaseGroupsColumnsLabels;

    private int maxDatasetNumber;
    private QuantDSIndexes[][] values;
    /*
     *  
     */
    public HeatMapLayout(int heatMapContainerWidth, int availableHMHeight) {
        this.availableComparisonsList = new LinkedHashSet<>();
        this.selectedDsList = new LinkedHashSet<>();

        this.setWidthUndefined();
        this.setHeightUndefined();

        this.availableHMHeight = availableHMHeight;
        this.availableHMWidth = heatMapContainerWidth;

        topLayout = new HorizontalLayout();
        topLayout.setWidthUndefined();
        topLayout.setHeight(24, Unit.PIXELS);
        topLayout.setSpacing(false);
        this.addComponent(topLayout);

        spacer = new VerticalLayout();
        this.spacer.setHeight(100, Unit.PERCENTAGE);
        this.spacer.setWidth("175px");
        topLayout.addComponent(spacer);

        columnCategoryHeadersContainer = new HorizontalLayout();
        this.columnCategoryHeadersContainer.setHeight(100, Unit.PERCENTAGE);
        topLayout.addComponent(columnCategoryHeadersContainer);
        diseaseGroupsColumnsLabels = new HorizontalLayout();
        diseaseGroupsColumnsLabels.setWidth("10px");
        diseaseGroupsColumnsLabels.setHeight("100%");
        columnCategoryHeadersContainer.addComponent(diseaseGroupsColumnsLabels);
        bottomLayout = new HorizontalLayout();
        bottomLayout.setWidthUndefined();
        bottomLayout.setSpacing(false);
        this.addComponent(bottomLayout);

        rawCategoryHeadersContainer = new VerticalLayout();
        this.rawCategoryHeadersContainer.setWidth("24px");
        bottomLayout.addComponent(rawCategoryHeadersContainer);
        bottomLayout.setComponentAlignment(rawCategoryHeadersContainer, Alignment.BOTTOM_CENTER);

        diseaseGroupsRowsLabels = new VerticalLayout();
        diseaseGroupsRowsLabels.setHeight("10px");
        diseaseGroupsRowsLabels.setWidth("24px");
        rawCategoryHeadersContainer.addComponent(diseaseGroupsRowsLabels);

        heatmapBody = new GridLayout();
        this.heatmapBody.setStyleName("heatmapbody");
        bottomLayout.addComponent(heatmapBody);

    }

    /**
     * this method responsible for updating the heatmap layout
     *
     *
     * @param rowsLbels
     * @param columnsLbels
     * @param patientsGroupComparisonsSet
     * @param fullQuantDsMap
     */
    public void updateData(Set<String> rowsLbels, Set<String> columnsLbels, Set<DiseaseGroupComparison> patientsGroupComparisonsSet, Map<Integer, QuantDatasetObject> fullQuantDsMap) {

        int heatmapTableWidth = availableHMWidth - 22;
        int heatmapTableHeight = availableHMHeight - 22;
        int colHeaderHeight = 30;
        int colHeaderWidth = 150;

        boolean rotate = false;
        if (columnsLbels.size() * 150 > heatmapTableWidth) {
            rotate = true;
            colHeaderHeight = 150;
            colHeaderWidth = 30;

        }

        heatmapCellHeight = Math.min((heatmapTableHeight - colHeaderHeight) / rowsLbels.size(), 30);
        int rowHeaderHeight = heatmapCellHeight;
        heatmapCellWidth = colHeaderWidth;

        this.columnCategoryHeadersContainer.setWidth(colHeaderWidth * columnsLbels.size(), Unit.PIXELS);
        this.rawCategoryHeadersContainer.setHeight(rowHeaderHeight * rowsLbels.size(), Unit.PIXELS);

        updateHeatMapLayout(rowsLbels, columnsLbels, patientsGroupComparisonsSet, rotate, colHeaderWidth, colHeaderHeight, rowHeaderHeight, fullQuantDsMap);
    }

    private void updateHeatMapLayout(Set<String> rowheaders, Set<String> colheaders, Set<DiseaseGroupComparison> patientsGroupComparisonsSet, boolean rotate, int colHeaderW, int colHeaderH, int rowHeaderH, Map<Integer, QuantDatasetObject> fullQuantDsMap) {//, Map<String, String> diseaseFullNameMap, ) {

        updateDiseaseHeadersLabel(rowheaders, colheaders);
        this.heatmapBody.removeAllComponents();
        heatmapBody.setColumns(colheaders.size() + 1);
        heatmapBody.setRows(rowheaders.size() + 1);
        VerticalLayout cornerCell = new VerticalLayout();
        cornerCell.setWidth(150, Unit.PIXELS);
        cornerCell.setHeight(colHeaderH, Unit.PIXELS);

        cornerCell.setStyleName("whitelayout");
        heatmapBody.addComponent(cornerCell, 0, 0);

        //init columnHeaders  
        columnHeaderCells = new HeaderCell[colheaders.size()];
         
         columnsColors = new String[colheaders.size()];
        for (int i = 0; i < colheaders.size(); i++) {
            String title = colheaders.toArray()[i].toString();
            if (title.equalsIgnoreCase("")) {
                title = "Not Available";
            }
            HeaderCell headerCell = new HeaderCell(rotate, title, i, colHeaderW, colHeaderH, "diseaseFullNameMap.get(title.split(n)[0])") {

                @Override
                public void selectData(String valueLabel) {
                    addRowSelectedDs(valueLabel);
                }

                @Override
                public void unSelectData(String valueLabel) {
                    removeRowSelectedDs(valueLabel);
                }
            };
            columnsColors[i]= "#666"; 

            heatmapBody.addComponent(headerCell, i + 1, 0);
            heatmapBody.setComponentAlignment(headerCell, Alignment.MIDDLE_CENTER);
            columnHeaderCells[i] = headerCell;
        }

        //init row headers
        rowHeaderCells = new HeaderCell[rowheaders.size()];
         rowsColors=new String[rowheaders.size()];
        for (int i = 0; i < rowheaders.size(); i++) {
            String la = rowheaders.toArray()[i].toString();
            if (la.equalsIgnoreCase("")) {
                la = "Not Available";
            }
            HeaderCell headerCell = new HeaderCell(false, la, i, 150, rowHeaderH, "diseaseFullNameMap.get(la.split(n)[0])") {

                @Override
                public void selectData(String valueLabel) {
                    addRowSelectedDs(valueLabel);
                }

                @Override
                public void unSelectData(String valueLabel) {
                    removeRowSelectedDs(valueLabel);
                }
            };

            heatmapBody.addComponent(headerCell, 0, i + 1);
            heatmapBody.setComponentAlignment(headerCell, Alignment.MIDDLE_CENTER);
            rowHeaderCells[i] = headerCell;
            rowsColors[i]="#000";

        }

        //init data
        calcHeatMapMatrix(rowheaders, colheaders, patientsGroupComparisonsSet);
        HeatmapColorGenerator hmColorGen = new HeatmapColorGenerator(maxDatasetNumber, 0);
        comparisonsCellsMap.clear();
        
        dataColors = new String[rowheaders.size()][colheaders.size()];
        for (int x = 0; x < values.length; x++) {
            for (int y = 0; y < values[x].length; y++) {
                String grI = rowheaders.toArray()[x].toString().split("__")[0];
                String grII = colheaders.toArray()[y].toString().split("__")[0];
                String headerTitle = grI + " / " + grII;
                double value = values[x][y].getValue();
                String color = "#EFF2FB";
                if (!rowheaders.toArray()[x].toString().equalsIgnoreCase(colheaders.toArray()[y].toString())) {
                    color = hmColorGen.getColor((float) value);
                }

                int[] dsIndexes = values[x][y].getIndexes();
                Set<String> pubCounter = new HashSet<>();
                for (int i : dsIndexes) {
                    QuantDatasetObject ds = fullQuantDsMap.get(i);
                    pubCounter.add(ds.getPumedID());

                }

//                if (diseaseFullNameMap.containsKey(grI)) {
//                    grI = diseaseFullNameMap.get(grI);
//                }
//                if (diseaseFullNameMap.containsKey(grII)) {
//                    grII = diseaseFullNameMap.get(grII);
//                }
                String fullCompName = grI + " / " + grII + " - " + rowheaders.toArray()[x].toString().split("__")[1];

                HeatmapCell cell = new HeatmapCell(value, color, dsIndexes, x, y, null, headerTitle, heatmapCellWidth, pubCounter.size(), fullCompName) {

                    @Override
                    public void selectData(HeatmapCell cell) {
                        addSelectedDs(cell);
                    }

                    @Override
                    public void unSelectData(HeatmapCell cell) {
                        removerSelectedDs(cell);
                    }

                };
                comparisonsCellsMap.put(headerTitle, cell);
                heatmapBody.addComponent(cell, y + 1, x + 1);
                dataColors[x][y] = color;
                if (cell.getComparison().getDatasetIndexes().length > 0) {
                    columnHeaderCells[y].addComparison(cell.getComparison(), cell);
                    rowHeaderCells[x].addComparison(cell.getComparison(), cell);
                    if (!cell.getComparison().getOreginalComparisonHeader().trim().equalsIgnoreCase("/")) {
                        availableComparisonsList.add(cell.getComparison());
                    }
                }

            }

        }
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
        this.selectedCells.add(cell);
        String kI = cell.getComparison().getOreginalComparisonHeader();
        String[] k1Arr = kI.split(" / ");
        String kII = k1Arr[1] + " / " + k1Arr[0];
        HeatmapCell equalCall = comparisonsCellsMap.get(kII);
        if (equalCall != null) {
            equalCall.select();
        } else {
            System.out.println("at equal was null " + kII);
        }
        cell.select();
        this.selectedCells.add(cell);
        this.selectedCells.add(equalCall);
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
            comparisonsCellsMap.values().stream().forEach((hmcell) -> {
                hmcell.initialState();
            });

        } else {
            cell.unselect();
            equalCall.unselect();

        }

    }

    /**
     * this method is responsible for selecting a row or column and update heat map
     * layout and update selection manager
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
                    });
                    header.select();
                    selectedCells.addAll(header.getIncludedCells());

                    continue;
                }
                header.unselect();
            }
            for (HeaderCell header : rowHeaderCells) {
                if (header.getValueLabel().equalsIgnoreCase(selectedheader)) {
                    header.getIncludedCells().stream().forEach((tcell) -> {
                        tcell.select();
                    });
                    header.select();
                    selectedCells.addAll(header.getIncludedCells());

                    continue;
                }
                header.unselect();
            }

        } else {

            for (HeaderCell header : columnHeaderCells) {
                if (header.getValueLabel().equalsIgnoreCase(selectedheader)) {
                    header.getIncludedCells().stream().forEach((tcell) -> {
                        tcell.select();
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
     * This method responsible for un-selecting a row or column and update heat map
     * layout and update selection manager
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

        if (rotate) {
            diseaseLabelContainer.setHeight((itemsNumb * heatmapCellHeight) - 1, Unit.PIXELS);
            diseaseLabelContainer.setWidth("100%");
            VerticalLayout rotateContainer = new VerticalLayout();
            rotateContainer.setWidth((itemsNumb * heatmapCellHeight) - 1, Unit.PIXELS);
            rotateContainer.setHeight("100%");
            diseaseLabelContainer.addComponent(rotateContainer);
            rotateContainer.addStyleName("rotateheader");
            rotateContainer.addComponent(label);
        } else {
            diseaseLabelContainer.addComponent(label);
            diseaseLabelContainer.setWidth((itemsNumb * (heatmapCellWidth)) - 2, Unit.PIXELS);
            diseaseLabelContainer.setHeight("100%");

        }
        diseaseLabelContainer.setDescription(dName_dStyle.split("__")[0]);

        return diseaseLabelContainer;

    }

    private void updateDiseaseHeadersLabel(Set<String> rowheaders, Set<String> colheaders) {

        this.diseaseGroupsRowsLabels.removeAllComponents();
        this.diseaseGroupsColumnsLabels.removeAllComponents();
        Map<String, TreeSet<Integer>> diseaseRowsMap = new LinkedHashMap<>();
        int index = 0;
        for (String header : rowheaders) {
            String dName_dStyle = header.split("__")[1] + "__" + header.split("__")[2];
            if (!diseaseRowsMap.containsKey(dName_dStyle)) {
                diseaseRowsMap.put(dName_dStyle, new TreeSet<>());
            }
            TreeSet<Integer> rowIndex = diseaseRowsMap.get(dName_dStyle);
            rowIndex.add(index);
            diseaseRowsMap.put(dName_dStyle, rowIndex);
            index++;

        }
        boolean resetRows = false;
        int rowcounter = 0;
        Map<String, Integer> diseaseRowsSize;
        diseaseRowsSize = new LinkedHashMap<>();
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

        Map<String, TreeSet<Integer>> diseaseColumnMap = new LinkedHashMap<>();
        index = 0;

        for (String header : colheaders) {
            String dName_dStyle = header.split("__")[1] + "__" + header.split("__")[2];
            if (!diseaseColumnMap.containsKey(dName_dStyle)) {
                diseaseColumnMap.put(dName_dStyle, new TreeSet<>());
            }
            TreeSet<Integer> colIndex = diseaseColumnMap.get(dName_dStyle);
            colIndex.add(index);
            diseaseColumnMap.put(dName_dStyle, colIndex);
            index++;

        }
        boolean resetcolumns = false;
        int columnCounter = 0;
        Map<String, Integer> diseaseColumnSize;
        diseaseColumnSize = new LinkedHashMap<>();
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
            for (String dName_dStyle : diseaseRowsMap.keySet()) {
                VerticalLayout diseaseLableContainer = initDiseaseGroupLabel(dName_dStyle, itemNum + rem, true);
                diseaseGroupsRowsLabels.addComponent(diseaseLableContainer);
                rem = 0;
            }

        } else {

            diseaseRowsMap.keySet().stream().map((dName_dStyle) -> {
                int itemNumb = diseaseRowsMap.get(dName_dStyle).last() - diseaseRowsMap.get(dName_dStyle).first() + 1;
                VerticalLayout diseaseLableContainer = initDiseaseGroupLabel(dName_dStyle, itemNumb, true);
                return diseaseLableContainer;
            }).forEach((diseaseLableContainer) -> {
                diseaseGroupsRowsLabels.addComponent(diseaseLableContainer);
            });
        }

        diseaseGroupsColumnsLabels.setWidthUndefined();

        if (resetcolumns) {

            int itemNum = (columnCounter / diseaseColumnMap.size());
            int rem = (columnCounter % diseaseColumnMap.size());
            for (String dName_dStyle : diseaseColumnMap.keySet()) {
                VerticalLayout diseaseLableContainer = initDiseaseGroupLabel(dName_dStyle, itemNum + rem, false);
                diseaseGroupsColumnsLabels.addComponent(diseaseLableContainer);
                rem = 0;
            }

        } else {

            diseaseColumnMap.keySet().stream().map((dName_dStyle) -> {
                int itemNumb = diseaseColumnMap.get(dName_dStyle).last() - diseaseColumnMap.get(dName_dStyle).first() + 1;
                VerticalLayout diseaseLableContainer = initDiseaseGroupLabel(dName_dStyle, itemNumb, false);
                return diseaseLableContainer;
            }).forEach((diseaseLableContainer) -> {
                diseaseGroupsColumnsLabels.addComponent(diseaseLableContainer);
            });
        }

    }

    private void calcHeatMapMatrix(Set<String> rowheaders, Set<String> colheaders, Set<DiseaseGroupComparison> patientsGroupComparisonsSet) {
        maxDatasetNumber = -1;

        values = new QuantDSIndexes[rowheaders.size()][colheaders.size()];
        for (int x = 0; x < rowheaders.size(); x++) {
            for (int y = 0; y < colheaders.size(); y++) {
                Set<Integer> value = calcDsNumbers(rowheaders.toArray()[x].toString().split("__")[0], colheaders.toArray()[y].toString().split("__")[0], patientsGroupComparisonsSet);
                int z = 0;
                int[] indexes = new int[value.size()];
                for (int i : value) {
                    indexes[z] = i;
                    z++;
                }
                QuantDSIndexes qDataset = new QuantDSIndexes();
                qDataset.setValue(value.size());
                qDataset.setIndexes(indexes);
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

    public void showCompBtn(boolean show) {
        topLayout.setVisible(show);

    }

    /**
     *
     * @param singleSelection
     */
    public void setSingleSelection(boolean singleSelection) {
        this.singleSelection = singleSelection;
    }

//    /**
//     *
//     * @param selectedDsList
//     */
//    public void updateDsCellSelection(Set<QuantDiseaseGroupsComparison> selectedDsList) {
//        if (selectedDsList.isEmpty()) {
//            icon.setSource(defaultResource);
//            hideShowBtnLabel.setValue("CSF-PR v2.0");
//            hideCompBtn.setEnabled(false);
//
//        } else {
//            hideCompBtn.setEnabled(true);
//        }
//
//        if (selfSelection) {
//            selfSelection = false;
//            return;
//        }
//        if (columnHeaderCells == null) {
//            System.out.println("at header = null is null");
//        }
//        for (HeaderCell header : columnHeaderCells) {
//
//            header.unselect();
//        }
//        for (HeaderCell header : rowHeaderCells) {
//            header.unselect();
//        }
//        List<HeatmapCell> localSelectedCells = new ArrayList<HeatmapCell>();
//        localSelectedCells.addAll(this.selectedCells);
//        for (QuantDiseaseGroupsComparison gr : this.selectedDsList) {
//            String kI = gr.getOreginalComparisonHeader();
//            String[] k1Arr = kI.split(" / ");
//            String kII = k1Arr[1] + " / " + k1Arr[0];
//            Set<String> keymap = new HashSet<String>();
//            keymap.add(kI);
//            keymap.add(kII);
//
//            if (!selectedDsList.contains(gr) && (comparisonsCellsMap.get(kII) != null && !selectedDsList.contains(comparisonsCellsMap.get(kII).getComparison()))) {
//
//                for (HeatmapCell cell : selectedCells) {
//                    String kI2 = cell.getComparison().getOreginalComparisonHeader();
//                    String[] kI2Arr = kI2.split(" / ");
//                    String kII2 = kI2Arr[1] + " / " + kI2Arr[0];
//                    if (keymap.contains(kI2) && keymap.contains(kII2)) {
//                        cell.unselect();
//                        localSelectedCells.remove(cell);
//
//                    }
//
//                }
//
//            }
//        }
//        this.selectedCells.clear();
//        this.selectedCells.addAll(localSelectedCells);
//        this.selectedDsList.clear();
//        this.selectedDsList.addAll(selectedDsList);
//        if (this.selectedDsList.isEmpty()) {
//            //reset all cells to transpairent 
//            for (HeatmapCell hmcell : comparisonsCellsMap.values()) {
////                hmcell.initStyle();
//            }
//
//        }
//
//    }

    private void updateSelectionManagerIndexes() {
        Map<String, QuantDiseaseGroupsComparison> filteredComp = new LinkedHashMap<>();
        selectedDsList.stream().forEach((comp) -> {
            String kI = comp.getOreginalComparisonHeader();
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
        comparisonsCellsMap.values().stream().filter((cell) -> (cell.getValue() != 0 && !cell.getComparison().getOreginalComparisonHeader().trim().equalsIgnoreCase("/") && availableComparisonsList.contains(cell.getComparison()))).map((cell) -> {
            cell.select();
            return cell;
        }).forEach((cell) -> {
            selectedCells.add(cell);
            selectedDsList.add(cell.getComparison());
        });
        updateSelectionManagerIndexes();
    }

    /**
     *
     */
    public void unselectAll() {
        selectedCells.clear();
        comparisonsCellsMap.values().stream().forEach((cell) -> {
            cell.initialState();
        });
        selectedDsList.clear();
        updateSelectionManagerIndexes();

    }
    
    private final HeatMapImgGenerator gen = new HeatMapImgGenerator();

    public String getHMThumbImg() {        
            return gen.generateHeatmap(rowsColors, columnsColors, dataColors);
           
    }

}
