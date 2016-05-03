package no.uib.probe.csf.pr.touch.view.components.heatmapsubcomponents;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
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

    private GridLayout heatmapBody;
    private GridLayout columnHeader;
    private GridLayout rowHeader;

    private final Set<HeatmapCell> selectedCells = new HashSet<>();

    private HeaderCell[] columnCells;
    private HeaderCell[] rowCells;
    private Map<String, HeatmapCell> comparisonsCellsMap = new LinkedHashMap<>();
    private Set<QuantDiseaseGroupsComparison> selectedDsList = new LinkedHashSet<QuantDiseaseGroupsComparison>();
    private Set<QuantDiseaseGroupsComparison> availableComparisonsList = new LinkedHashSet<QuantDiseaseGroupsComparison>();

    private boolean singleSelection = true;
    private boolean selfSelection = false;
    private int heatmapCellWidth;
    private int heatmapCellHeight;
    private int heatmapHeaderCellWidth;

    //heatmap swing data
    private String[] rowsColors;
    private String[] columnsColors;
    private String[][] dataColors;

    private VerticalLayout hideCompBtn;

    private Image icon;
    private Label hideShowBtnLabel;
    private ThemeResource defaultResource;

    private int availableHMHeight, availableHMWidth;

    private HorizontalLayout topLayout;
    private HorizontalLayout bottomLayout, columnCategoryHeadersContainer;
    private VerticalLayout spacer;

    private VerticalLayout rawCategoryHeadersContainer;
    private VerticalLayout diseaseGroupsRowsLabels;
    private HorizontalLayout diseaseGroupsColumnsLabels;

    private int maxDatasetNumber;
    private QuantDSIndexes[][] values;
//    private GridLayout heatmapBody;

    /*
     *  
     */
    public HeatMapLayout(int heatMapContainerWidth, int availableHMHeight) {

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
        this.spacer.setWidth("174px");
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
        columnCells= new HeaderCell[colheaders.size()];
        for (int i = 0; i < colheaders.size(); i++) {
            String title = colheaders.toArray()[i].toString();
            if (title.equalsIgnoreCase("")) {
                title = "Not Available";
            }
            HeaderCell headerCell = new HeaderCell(rotate, title, i, HeatMapLayout.this, colHeaderW, colHeaderH, "diseaseFullNameMap.get(title.split(n)[0])");
            heatmapBody.addComponent(headerCell, i + 1, 0);
            heatmapBody.setComponentAlignment(headerCell, Alignment.MIDDLE_CENTER);
            columnCells[i]=headerCell;
        }

        //init row headers
        rowCells = new HeaderCell[rowheaders.size()];
        for (int i = 0; i < rowheaders.size(); i++) {
            String la = rowheaders.toArray()[i].toString();
            if (la.equalsIgnoreCase("")) {
                la = "Not Available";
            }
            HeaderCell headerCell = new HeaderCell(false, la, i, HeatMapLayout.this, 150, rowHeaderH, "diseaseFullNameMap.get(la.split(n)[0])");
            heatmapBody.addComponent(headerCell, 0, i + 1);
            heatmapBody.setComponentAlignment(headerCell, Alignment.MIDDLE_CENTER);
            rowCells[i]=headerCell;
            
        }

        //init data
        calcHeatMapMatrix(rowheaders, colheaders, patientsGroupComparisonsSet);
        HeatmapColorGenerator hmColorGen = new HeatmapColorGenerator(maxDatasetNumber, 0);
        comparisonsCellsMap.clear();
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
                    public void selectData(QuantDiseaseGroupsComparison comparison, HeatmapCell cell) {
                        addSelectedDs(comparison, cell);
                    }

                    @Override
                    public void unSelectData(QuantDiseaseGroupsComparison comparison, HeatmapCell cell) {
                        unSelectDs(comparison, cell);
                    }

                };
                comparisonsCellsMap.put(headerTitle, cell);
                heatmapBody.addComponent(cell, y + 1, x + 1);
//                dataColors[x][y] = color;
                if (cell.getComparison().getDatasetIndexes().length > 0) {
                    columnCells[y].addComparison(cell.getComparison(), cell);
                    rowCells[x].addComparison(cell.getComparison(), cell);
                    if (!cell.getComparison().getOreginalComparisonHeader().trim().equalsIgnoreCase("/")) {
                        availableComparisonsList.add(cell.getComparison());
                    }
                }

            }

        }

//        availableComparisonsList.clear();
//        columnHeader.removeAllComponents();
//        rowHeader.removeAllComponents();
//        heatmapBody.removeAllComponents();
//        heatmapBody.setMargin(new MarginInfo(false, true, true, false));
//        columnHeader.setColumns(colheaders.size());
//        columnHeader.setRows(1);
//        columnHeader.setWidth((colheaders.size() * heatmapCellWidth) + "px");
//        columnHeader.setHeight(heatmapHeaderCellWidth + "px");
//       
//
//        rowHeader.setColumns(1);
//        rowHeader.setRows(rowheaders.size());
//        rowHeader.setWidth(heatmapHeaderCellWidth + "px");
//        rowHeader.setHeight((heatmapCellWidth * rowheaders.size()) + "px");
//        rowCells = new HeaderCell[rowheaders.size()];
//        rowsColors = new String[rowheaders.size()];
//
//        heatmapBody.setColumns(colheaders.size());
//        heatmapBody.setRows(rowheaders.size());
//        dataColors = new String[rowheaders.size()][colheaders.size()];
//        heatmapBody.setWidth((colheaders.size() * heatmapCellWidth) + "px");
//        heatmapBody.setHeight((heatmapCellWidth * rowheaders.size()) + "px");
//
////init col headers
//        for (int i = 0; i < colheaders.size(); i++) {
//            String title = colheaders.toArray()[i].toString();
//            if (title.equalsIgnoreCase("")) {
//                title = "Not Available";
//            }
//            HeaderCell headerCell = new HeaderCell(false, title, i, HeatMapLayout.this, heatmapCellWidth, heatmapHeaderCellWidth, diseaseFullNameMap.get(title.split("\n")[0]));
//            columnHeader.addComponent(headerCell, i, 0);
//            columnHeader.setComponentAlignment(headerCell, Alignment.MIDDLE_CENTER);
//            columnCells[i] = headerCell;
//            columnsColors[i] = headerCell.getColor();
//
//        }
//        //init row headers
//        for (int i = 0; i < rowheaders.size(); i++) {
//            String la = rowheaders.toArray()[i].toString();
//            if (la.equalsIgnoreCase("")) {
//                la = "Not Available";
//            }
//            HeaderCell headerCell = new HeaderCell(true, la, i, HeatMapLayout.this, heatmapCellWidth, heatmapHeaderCellWidth, diseaseFullNameMap.get(la.split("\n")[0]));
//            rowHeader.addComponent(headerCell, 0, i);
//            rowHeader.setComponentAlignment(headerCell, Alignment.MIDDLE_CENTER);
//            rowCells[i] = headerCell;
//            rowsColors[i] = headerCell.getColor();
//        }
//
//        for (int x = 0; x < values.length; x++) {
//            for (int y = 0; y < values[x].length; y++) {
//                String headerTitle = rowheaders.toArray()[x].toString() + " / " + colheaders.toArray()[y].toString();
//                double value = values[x][y].getValue();
//                String color = "#EFF2FB";
//                if (!rowheaders.toArray()[x].toString().equalsIgnoreCase(colheaders.toArray()[y].toString())) {
//                    color = hmColorGen.getColor((float) value);
//                }
//
//                int[] dsIndexes = values[x][y].getIndexes();
//                Set<String> pubCounter = new HashSet<String>();
//                for (int i : dsIndexes) {
//                    QuantDatasetObject ds = fullDsMap.get(i);
//                    pubCounter.add(ds.getPumedID());
//
//                }
//
//                String grI = headerTitle.split("/")[0].split("\n")[0];
//                String grII = headerTitle.split("/")[1].split("\n")[0].trim();
//                if (diseaseFullNameMap.containsKey(grI)) {
//                    grI = diseaseFullNameMap.get(grI);
//                }
//                if (diseaseFullNameMap.containsKey(grII)) {
//                    grII = diseaseFullNameMap.get(grII);
//                }
//                String fullCompName = grI + " / " + grII + " - " + headerTitle.split("/")[0].split("\n")[1].replace(grII, headerTitle).replace("_", " ").replace("Disease", "");
//
//                HeatmapCell cell = new HeatmapCell(value, color, dsIndexes, x, y, null, HeatMapLayout.this, headerTitle, heatmapCellWidth, pubCounter.size(), fullCompName);
//                comparisonsCellsMap.put(headerTitle, cell);
//                heatmapBody.addComponent(cell, y, x);
//                dataColors[x][y] = color;
//                if (cell.getComparison().getDatasetIndexes().length > 0) {
//                    columnCells[y].addComparison(cell.getComparison(), cell);
//                    rowCells[x].addComparison(cell.getComparison(), cell);
//                    if (!cell.getComparison().getOreginalComparisonHeader().trim().equalsIgnoreCase("/")) {
//                        availableComparisonsList.add(cell.getComparison());
//                    }
//                }
//
//            }
//
//        }
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

//    private Set<Integer> calcDsNumbers(String PGI, String PGII, Set<DiseaseGroupComparison> patientsGroupArr) {
//        Set<Integer> indexes = new HashSet<Integer>();
//        for (DiseaseGroupComparison pg : patientsGroupArr) {
//            System.out.println(" PGI "+PGI+"  "+PGII+"   "+pg.getPatientsGroupILabel()+"  "+pg.getPatientsGroupIILabel());
//            if (pg.checkLabel(PGI)) {
//                if (pg.getValLabel(PGI).equalsIgnoreCase(PGII)) {
//                    indexes.add(pg.getOriginalDatasetIndex());
//                }
//
//            }
//
//        }
//        return indexes;
//    }
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

    /**
     *
     * @param comparison
     * @param cell
     */
    protected void addSelectedDs(QuantDiseaseGroupsComparison comparison, HeatmapCell cell) {
        if (selectedDsList.isEmpty()) {
            //reset all cells to transpairent
            comparisonsCellsMap.values().stream().forEach((hmcell) -> {
                hmcell.unselect();
            });

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
            equalCall.select();
        } else {
            System.out.println("at equal was null " + kII);
        }
        this.selectedCells.add(cell);
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
                        tcell.unselect();
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
                        tcell.select();
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
                        tcell.select();
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
//                hmcell.initStyle();
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
            comparisonsCellsMap.values().stream().forEach((hmcell) -> {
                hmcell.initialState();
            });

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
//                hmcell.initStyle();
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
        updateHideShowThumbImg(null);
        updateShowHideBtnLabel(null);
        for (HeatmapCell cell : comparisonsCellsMap.values()) {
//            cell.initStyle();
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
//        columnContainer.setVisible(visible);
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
