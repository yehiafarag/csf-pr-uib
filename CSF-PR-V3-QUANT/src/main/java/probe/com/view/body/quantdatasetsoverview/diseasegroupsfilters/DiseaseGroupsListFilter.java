/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters;

import com.vaadin.data.Property;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import probe.com.model.beans.quant.QuantDatasetObject;
import probe.com.selectionmanager.CSFFilter;
import probe.com.selectionmanager.CSFFilterSelection;
import probe.com.selectionmanager.QuantCentralManager;
import probe.com.view.core.ListSelectDatasetExplorerFilter;
import probe.com.view.core.DiseaseGroup;

/**
 *
 * @author Yehia Farag
 */
public class DiseaseGroupsListFilter extends VerticalLayout implements CSFFilter, Button.ClickListener {

    private final ListSelectDatasetExplorerFilter patientGroupIFilter, patientGroupIIFilter;
    private String[] patGr1, patGr2;
    private DiseaseGroup[] patientsGroupArr;
    private final Set<String> diseaseGroupsSet;
    private final Button diseaseGroupsFilterBtn;
    private final Window popupWindow;
    private int[] studiesIndexes;
    private final LinkedHashSet<String> selectedRows = new LinkedHashSet<String>();
    private final LinkedHashSet<String> selectedColumns = new LinkedHashSet<String>();
    private final QuantCentralManager Quant_Central_Manager;
    private boolean selfselected = false;
    private Property.ValueChangeListener disGrIListener, disGrIIListener;
    private boolean updateManager = false;

    /**
     *
     * @return
     */
    public Set<String> getDiseaseGroupsSet() {
        return diseaseGroupsSet;
    }

    /**
     *
     * @return
     */
    public Button getDiseaseGroupsFilterBtn() {
        return diseaseGroupsFilterBtn;
    }

    /**
     *
     * @param type
     */
    @Override
    public void selectionChanged(String type) {
        if (selfselected) {
            selfselected = false;
            return;
        }
        if (type.equalsIgnoreCase("Disease_Groups_Level")) {
            this.updatePatientGroups(Quant_Central_Manager.getFilteredDatasetsList());
            if (patGr1 == null || patGr2 == null) {
                System.out.println("error at 85 class " + this.getClass().getName() + "   ");
                return;
            }
            String[] pgArr = merge(patGr1, patGr2);

            selectedRows.clear();
            for (String str : pgArr) {
                if (!str.equalsIgnoreCase("")) {
                    selectedRows.add(str);
                }
            }
            patientGroupIFilter.updateList(selectedRows);
            selectedColumns.clear();
            for (String str : pgArr) {
                if (!str.equalsIgnoreCase("")) {
                    selectedColumns.add(str);
                }
            }
            patientGroupIIFilter.updateList(selectedColumns);
            patientGroupIIFilter.setEnabled(false);
            Set<DiseaseGroup> p = filterPGR2(selectedRows, selectedColumns);
            studiesIndexes = new int[p.size()];
            int i = 0;
            for (DiseaseGroup pg : p) {
                studiesIndexes[i] = pg.getOriginalDatasetIndex();
                i++;
            }
            updateSelectionManager(null);
        } else if (type.equalsIgnoreCase("Reset_Disease_Groups_Level")) {
            this.updatePatientGroups(Quant_Central_Manager.getFullQuantDatasetMap());
            String[] pgArr = merge(patGr1, patGr2);
            selectedRows.clear();
            for (String str : pgArr) {
                if (!str.equalsIgnoreCase("")) {
                    selectedRows.add(str);
                }
            }
            patientGroupIFilter.updateList(selectedRows);
            patientGroupIIFilter.updateList(selectedRows);
            patientGroupIIFilter.setEnabled(false);
            selectedColumns.clear();
            selectedColumns.addAll(selectedRows);
            Set<DiseaseGroup> p = filterPGR2(selectedRows, selectedColumns);
            studiesIndexes = new int[p.size()];
            int i = 0;
            for (DiseaseGroup pg : p) {
                studiesIndexes[i] = pg.getOriginalDatasetIndex();
                i++;
            }
            updateSelectionManager(null);
        }
    }

    /**
     *
     * @return
     */
    @Override
    public String getFilterId() {
        return DiseaseGroupsListFilter.class.getName();
    }

    /**
     *
     * @param value
     */
    @Override
    public void removeFilterValue(String value) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @param Quant_Central_Manager
     */
    public DiseaseGroupsListFilter(final QuantCentralManager Quant_Central_Manager) {
        this.Quant_Central_Manager = Quant_Central_Manager;
        this.setWidth("450px");
//        this.Quant_Central_Manager.registerFilterListener(DiseaseGroupsListFilter.this);

//        this.updatePatientGroups(Quant_Central_Manager.getFilteredDatasetsList());
//        String[] pgArr = merge(patGr1, patGr2);
        this.patientGroupIFilter = new ListSelectDatasetExplorerFilter(1, "Disease Group I", Quant_Central_Manager.getSelectedHeatMapRows());
        initGroupsIFilter();

        this.patientGroupIIFilter = new ListSelectDatasetExplorerFilter(2, "Disease Group II", Quant_Central_Manager.getSelectedHeatMapColumns());
        initGroupsIIFilter();
        diseaseGroupsSet = new LinkedHashSet<String>();
        diseaseGroupsSet.addAll(Quant_Central_Manager.getSelectedHeatMapRows());
        
        this.addComponent(patientGroupIIFilter);

        diseaseGroupsFilterBtn = new Button("Disease Groups");
        diseaseGroupsFilterBtn.setStyleName(Reindeer.BUTTON_LINK);
        diseaseGroupsFilterBtn.addClickListener(DiseaseGroupsListFilter.this);

        VerticalLayout popupBody = new VerticalLayout();

        VerticalLayout filtersConatinerLayout = new VerticalLayout();
        filtersConatinerLayout.setSpacing(true);
        filtersConatinerLayout.setWidth("500px");
        filtersConatinerLayout.setHeightUndefined();

        popupBody.addComponent(filtersConatinerLayout);

        HorizontalLayout btnLayout = new HorizontalLayout();
        popupBody.addComponent(btnLayout);
        btnLayout.setSpacing(true);
        btnLayout.setMargin(new MarginInfo(false, false, true, true));

        Button applyFilters = new Button("Apply");
        applyFilters.setDescription("Apply the selected filters");
        applyFilters.setPrimaryStyleName("resetbtn");
        applyFilters.setWidth("50px");
        applyFilters.setHeight("24px");

        btnLayout.addComponent(applyFilters);
        applyFilters.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                popupWindow.close();
            }
        });

        Button resetFiltersBtn = new Button("Reset");
        resetFiltersBtn.setPrimaryStyleName("resetbtn");
        btnLayout.addComponent(resetFiltersBtn);
        resetFiltersBtn.setWidth("50px");
        resetFiltersBtn.setHeight("24px");

        resetFiltersBtn.setDescription("Reset all applied filters");
        resetFiltersBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Quant_Central_Manager.resetFiltersListener();
            }
        });

        popupWindow = new Window() {
            @Override
            public void close() {
                if (updateManager) {
                    updateSelectionManager(studiesIndexes);
                }

                popupWindow.setVisible(false);

            }
        };
        popupWindow.setContent(popupBody);
        popupWindow.setWindowMode(WindowMode.NORMAL);
        popupWindow.setWidth((540) + "px");
        popupWindow.setHeight((500) + "px");
        popupWindow.setVisible(false);
        popupWindow.setResizable(false);
        popupWindow.setClosable(false);
        popupWindow.setStyleName(Reindeer.WINDOW_LIGHT);
        popupWindow.setModal(true);
        popupWindow.setDraggable(false);
        popupWindow.center();
        popupWindow.setCaption("&nbsp;&nbsp;Disease Groups Comparisons");

        UI.getCurrent().addWindow(popupWindow);
        popupWindow.center();

        popupWindow.setCaptionAsHtml(true);
        popupWindow.setClosable(true);

        this.setHeightUndefined();
        filtersConatinerLayout.addComponent(DiseaseGroupsListFilter.this);
        filtersConatinerLayout.setComponentAlignment(DiseaseGroupsListFilter.this, Alignment.BOTTOM_CENTER);
//        Quant_Central_Manager.setSelectedHeatMapRows(selectedRows);
//        Quant_Central_Manager.setSelectedHeatMapColumns(selectedColumns);

    }

    /**
     * this method update the labels value for the groups selection list
     *
     * @param quantDSArr array of the quant datasets
     */
    private void updatePatientGroups(Map<Integer, QuantDatasetObject> quantDSArr) {

        patGr1 = new String[quantDSArr.size()];
        patGr2 = new String[quantDSArr.size()];
        patientsGroupArr = new DiseaseGroup[quantDSArr.size()];
        int i = 0;
        for (QuantDatasetObject ds : quantDSArr.values()) {
            if (ds == null) {
                continue;
            }
            DiseaseGroup pg = new DiseaseGroup();
            String pgI = ds.getPatientsGroup1();
            pg.setPatientsGroupI(pgI);
            String label1;
            if (pgI.equalsIgnoreCase("Not Available") || pgI.equalsIgnoreCase("control")) {
                pgI = "";
            }
            String subpgI = ds.getPatientsSubGroup1();
            pg.setPatientsSubGroupI(subpgI);
            if (!subpgI.equalsIgnoreCase("") && !subpgI.equalsIgnoreCase("Not Available")) {
                pgI = subpgI;
            }
            label1 = pgI;
            pg.setPatientsGroupILabel(label1);

            String pgII = ds.getPatientsGroup2();
            pg.setPatientsGroupII(pgII);
            String label2;
            if (pgII.equalsIgnoreCase("Not Available") || pgII.equalsIgnoreCase("control")) {
                pgII = "";
            }
            String subpgII = ds.getPatientsSubGroup2();
            pg.setPatientsSubGroupII(subpgII);
            if (!subpgII.equalsIgnoreCase("") && !subpgII.equalsIgnoreCase("Not Available")) {
                pgII = subpgII;
            }
            label2 = pgII;
            pg.setPatientsGroupIILabel(label2);
            patientsGroupArr[i] = pg;
            patGr1[i] = label1;
            patGr2[i] = label2;
            pg.setQuantDatasetIndex(i);
            pg.setOriginalDatasetIndex(ds.getDsKey());
            i++;
        }

    }

    private String[] merge(String[] arr1, String[] arr2) {
        String[] newArr = new String[arr1.length + arr2.length];
        int i = 0;
        for (String str : arr1) {
            newArr[i] = str;
            i++;
        }
        for (String str : arr2) {
            newArr[i] = str;
            i++;
        }

        Arrays.sort(newArr);
        return newArr;

    }

    private void updateSelectionManager(int[] datasetIndexes) {
//        selfselected = true;
//        if (datasetIndexes != null) {
//            Quant_Central_Manager.applyFilters(new CSFFilterSelection("Disease_Groups_Level", datasetIndexes, "", new HashSet<String>()));
//        }
//        Quant_Central_Manager.setHeatMapLevelSelection(selectedRows, selectedColumns, patientsGroupArr);
    }

    private void initGroupsIFilter() {
        patientGroupIFilter.getList().setHeight("150px");
        patientGroupIFilter.getList().setWidth("380px");
        patientGroupIFilter.setMargin(new MarginInfo(true, false, true, false));
        this.addComponent(patientGroupIFilter);
        this.setComponentAlignment(patientGroupIFilter, Alignment.MIDDLE_CENTER);
        disGrIListener = new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                selectedRows.clear();
                boolean enable = true;
                for (Object id : patientGroupIFilter.getList().getItemIds().toArray()) {
                    if (patientGroupIFilter.getList().isSelected(id.toString())) {
                        selectedRows.add(id.toString());
                    }
                }
                if (selectedRows.isEmpty()) {
                    enable = false;
                    updateManager = false;
                    for (Object id : patientGroupIFilter.getList().getItemIds().toArray()) {
                        selectedRows.add(id.toString());
                    }

                } else {
                    updateManager = true;
                }
                selectedColumns.clear();
                Set<DiseaseGroup> p = filterPatGroup2List(selectedRows);
                studiesIndexes = new int[p.size()];
                int i = 0;
                for (DiseaseGroup pg : p) {
                    studiesIndexes[i] = pg.getOriginalDatasetIndex();
                    i++;
                }
                for (Object id : patientGroupIIFilter.getList().getItemIds().toArray()) {
                    selectedColumns.add(id.toString());

                }
                patientGroupIIFilter.setEnabled(enable);

            }
        };
        patientGroupIFilter.addValueChangeListener(disGrIListener);
        for (Object id : patientGroupIFilter.getList().getItemIds().toArray()) {
            selectedRows.add(id.toString());
        }

    }

    private void initGroupsIIFilter() {
        patientGroupIIFilter.getList().setHeight("150px");
        patientGroupIIFilter.getList().setWidth("380px");
        patientGroupIIFilter.setEnabled(false);
        patientGroupIIFilter.setMargin(new MarginInfo(false, false, true, false));
        this.addComponent(patientGroupIIFilter);
        this.setComponentAlignment(patientGroupIIFilter, Alignment.MIDDLE_CENTER);
        disGrIIListener = new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                selectedRows.clear();
                for (Object id : patientGroupIFilter.getList().getItemIds().toArray()) {
                    if (patientGroupIFilter.getList().isSelected(id.toString())) {
                        selectedRows.add(id.toString());
                    }
                }
                selectedColumns.clear();
                for (Object id : patientGroupIIFilter.getList().getItemIds().toArray()) {
                    if (patientGroupIIFilter.getList().isSelected(id.toString())) {
                        selectedColumns.add(id.toString());
                    }
                }
                if (selectedColumns.isEmpty()) {
                    for (Object id : patientGroupIIFilter.getList().getItemIds().toArray()) {
                        selectedColumns.add(id.toString());
                    }

                }
                Set<DiseaseGroup> p = filterPGR2(selectedRows, selectedColumns);
                studiesIndexes = new int[p.size()];
                int i = 0;
                for (DiseaseGroup pg : p) {
                    studiesIndexes[i] = pg.getOriginalDatasetIndex();
                    i++;
                }
            }
        };
        patientGroupIIFilter.addValueChangeListener(disGrIIListener);
        for (Object id : patientGroupIIFilter.getList().getItemIds().toArray()) {
            selectedColumns.add(id.toString());
        }
    }

    private Set<DiseaseGroup> filterPatGroup2List(Set<String> sel1) {
        Set<DiseaseGroup> tempPatientsGroup = new HashSet<DiseaseGroup>();
        Set<String> labels = new TreeSet<String>();

        for (DiseaseGroup pg : patientsGroupArr) {
            for (String label : sel1) {
                if (pg.checkLabel(label)) {
                    tempPatientsGroup.add(pg);
                    labels.add(pg.getValLabel(label));
                }
            }
        }

        patientGroupIIFilter.updateList(labels);
        return tempPatientsGroup;
    }

    private Set<DiseaseGroup> filterPGR2(Set<String> L1, Set<String> L2) {
        Set<DiseaseGroup> tempPatientsGroup = new HashSet<DiseaseGroup>();

        for (DiseaseGroup pg : patientsGroupArr) {
            boolean breakfor = false;
            for (String label : L1) {
                if (pg.checkLabel(label)) {
                    for (String label2 : L2) {
                        if (pg.checkLabel(label2)) {
                            tempPatientsGroup.add(pg);
                            breakfor = true;
                            break;
                        }
                    }
                    if (breakfor) {
                        break;
                    }
                }
            }
        }

        return tempPatientsGroup;

    }

    /**
     *
     * @return
     */
    public Set<String> getSelectedRows() {
        return selectedRows;
    }

    /**
     *
     * @return
     */
    public Set<String> getSelectedColumns() {
        return selectedColumns;
    }

//    public QuantDSIndexes[][] getValues() {
//        return values;
//    }
//    public int getMaxDatasetNumber() {
//        return maxDatasetNumber;
//    }
//    private int maxDatasetNumber;
//    public void selectionChanged(QuantDatasetObject[] dsArr, String type) {
//        System.out.println("type at list fileter "+type);
//        if (type.equalsIgnoreCase("Disease_Groups_Level")) {
//
//            this.updatePatientGroups(dsArr);
//            String[] pgArr = merge(patGr1, patGr2);
//            selectedRows.clear();
//            for (String str : pgArr) {
//                if (!str.equalsIgnoreCase("")) {
//                    selectedRows.add(str);
//                }
//            }
//            patientGroupIFilter.updateList(selectedRows);
//            selectedColumns.clear();
//
//            for (Object id : patientGroupIIFilter.getList().getItemIds().toArray()) {
//                selectedColumns.add(id.toString());
//
//            }
//            patientGroupIIFilter.updateList(selectedColumns);
//            patientGroupIIFilter.setEnabled(false);
//            Set<PatientsGroup> p = filterPGR2(selectedRows, selectedColumns);
//            int[] indexes = new int[p.size()];
//            int i = 0;
//            for (DiseaseGroup pg : p) {
//                indexes[i] = pg.getOriginalDatasetIndex();
//                i++;
//            }
//
////            parent.updateHeatmap(selectedRows, sel2, indexes, patientsGroupArr);
////           values = calcHeatMapMatrix(selectedRows, selectedColumns);
////            heatMap.updateHeatMap(selectedRows, selectedColumns, values, maxDatasetNumber);
//
//        } else if (type.equalsIgnoreCase("Reset_Disease_Groups_Level")) {
//            this.updatePatientGroups(dsArr);
//            String[] pgArr = merge(patGr1, patGr2);
//            selectedRows.clear();
//            for (String str : pgArr) {
//                if (!str.equalsIgnoreCase("")) {
//                    selectedRows.add(str);
//                }
//            }
//            patientGroupIFilter.updateList(selectedRows);
//            patientGroupIIFilter.updateList(selectedRows);
//            patientGroupIIFilter.setEnabled(false);
//            selectedColumns.clear();
//            selectedColumns.addAll(selectedRows);
//            Set<PatientsGroup> p = filterPGR2(selectedRows, selectedColumns);
//            int[] indexes = new int[p.size()];
//            int i = 0;
//            for (DiseaseGroup pg : p) {
//                indexes[i] = pg.getOriginalDatasetIndex();
//                i++;
//            }
////            parent.updateHeatmap(selectedRows, selectedColumns, indexes, patientsGroupArr);
//
//        } else if (type.equalsIgnoreCase("Comparison_Selection")) {
////            parent.updateDsCellSelection();
////            heatMap.updateDsCellSelection(exploringFiltersManager.getSelectedComparisonList());
////            if (exploringFiltersManager.getSelectedComparisonList().isEmpty()) {
////                viewComparisonHeatmap(true);
////            }
//
//        }
//
//    }
    /**
     *
     * @return
     */
    public DiseaseGroup[] getPatientsGroupArr() {
        return patientsGroupArr;
    }

    /**
     *
     * @return
     */
    public int[] getStudiesIndexes() {
        return studiesIndexes;
    }

    @Override
    public void buttonClick(Button.ClickEvent arg0) {
        popupWindow.setVisible(!popupWindow.isVisible());
    }

}
