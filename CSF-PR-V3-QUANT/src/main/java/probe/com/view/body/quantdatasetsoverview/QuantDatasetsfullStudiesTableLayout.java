package probe.com.view.body.quantdatasetsoverview;

import com.vaadin.addon.tableexport.CsvExport;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import probe.com.selectionmanager.CSFFilter;
import probe.com.selectionmanager.DatasetExploringCentralSelectionManager;
import probe.com.model.beans.quant.QuantDatasetObject;
import probe.com.view.core.CSFTable;
import probe.com.view.core.CustomExternalLink;

/**
 *
 * @author Yehia Farag
 *
 * quant dataset combined table layout
 */
public class QuantDatasetsfullStudiesTableLayout extends VerticalLayout implements CSFFilter, Property.ValueChangeListener {

    private final DatasetExploringCentralSelectionManager datasetExploringCentralSelectionManager;
    private final String filter_id = "datasetsTable";
    private final Table datasetsTable;
    private CustomExternalLink pumedLabel;
    private int pumedkey;
    private final CSFTable tableLayout;
    private final int totalStudiesNumber;
    private int[] dsIndexes;

    /**
     *
     * @param datasetExploringCentralSelectionManager
     */
    public QuantDatasetsfullStudiesTableLayout(DatasetExploringCentralSelectionManager datasetExploringCentralSelectionManager) {
        this.datasetExploringCentralSelectionManager = datasetExploringCentralSelectionManager;
        datasetExploringCentralSelectionManager.registerFilter(QuantDatasetsfullStudiesTableLayout.this);
        this.datasetsTable = new Table();
        this.totalStudiesNumber = datasetExploringCentralSelectionManager.getFilteredDatasetsList().size();

        this.setStyleName(Reindeer.LAYOUT_WHITE);
        HorizontalLayout rightBottomLayout = new HorizontalLayout();
        Button exportTableBtn = new Button("");
        exportTableBtn.setHeight("24px");
        exportTableBtn.setWidth("24px");
        exportTableBtn.setPrimaryStyleName("exportxslbtn");
        rightBottomLayout.addComponent(exportTableBtn);
        rightBottomLayout.setComponentAlignment(exportTableBtn, Alignment.BOTTOM_RIGHT);
//        rightBottomLayout.setMargin(new MarginInfo(true, false, false, false));
        exportTableBtn.setDescription("Export table data");

        rightBottomLayout.setHeight("30px");
        rightBottomLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        exportTableBtn.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                CsvExport csvExport = new CsvExport(datasetsTable, "CSF-PR  Quant Studies Information");
                csvExport.setReportTitle("CSF-PR / Quant Studies Information ");
                csvExport.setExportFileName("CSF-PR - Quant Studies Information" + ".csv");
                csvExport.setMimeType(CsvExport.CSV_MIME_TYPE);
                csvExport.setDisplayTotals(false);
                csvExport.export();

            }
        });
        tableLayout = new CSFTable(datasetsTable, "Studies Table", totalStudiesNumber + "/" + totalStudiesNumber, null, null, rightBottomLayout);
        initCombinedQuantDatasetTable(datasetExploringCentralSelectionManager.getActiveHeader());
        this.addComponent(tableLayout);
        this.setWidth("100%");
        tableLayout.setShowTable(false);
    }

    /**
     * initialize the main quant dataset table
     *
     * @param activeColumnHeaders the visualized columns in the table
     */
    private void initCombinedQuantDatasetTable(boolean[] activeColumnHeaders) {
        datasetsTable.setSelectable(true);
        datasetsTable.setLocale(Locale.UK);
        datasetsTable.setColumnReorderingAllowed(true);
        datasetsTable.setColumnCollapsingAllowed(true);
        datasetsTable.setDragMode(Table.TableDragMode.NONE);
        datasetsTable.setMultiSelect(false);//        
        datasetsTable.setImmediate(true); // react at once when something is selected
        datasetsTable.setWidth("100%");
        datasetsTable.addContainerProperty("Index", Integer.class, null, "", null, Table.Align.RIGHT);
        datasetsTable.addContainerProperty("Author", String.class, null);
        datasetsTable.setColumnCollapsed("Author", false);
        datasetsTable.addContainerProperty("Year", String.class, null, "Year", null, Table.Align.RIGHT);
        datasetsTable.setColumnCollapsed("Year", false);
        datasetsTable.addContainerProperty("#Identified Proteins", Integer.class, null, "#Identified Proteins", null, Table.Align.RIGHT);
        datasetsTable.setColumnCollapsed("#Identified Proteins", !activeColumnHeaders[2]);
        datasetsTable.addContainerProperty("#Quantified Proteins ", Integer.class, null, "#Quantified Proteins", null, Table.Align.RIGHT);
        datasetsTable.setColumnCollapsed("#Quantified Proteins", !activeColumnHeaders[3]);

        datasetsTable.addContainerProperty("Analytical Method", String.class, null);
        datasetsTable.setColumnCollapsed("Analytical Method", !activeColumnHeaders[4]);

        datasetsTable.addContainerProperty("Raw Data", String.class, null);
        datasetsTable.setColumnCollapsed("Raw Data", !activeColumnHeaders[5]);
        datasetsTable.addContainerProperty("#Files", Integer.class, null, "#Files", null, Table.Align.RIGHT);
        datasetsTable.setColumnCollapsed("#Files", !activeColumnHeaders[6]);
        datasetsTable.addContainerProperty("typeOfStudy", String.class, null, "Study Type", null, Table.Align.LEFT);
        datasetsTable.setColumnCollapsed("typeOfStudy", !activeColumnHeaders[7]);

        datasetsTable.addContainerProperty("sampleType", String.class, null, "Sample Type", null, Table.Align.LEFT);
        datasetsTable.setColumnCollapsed("sampleType", !activeColumnHeaders[8]);

        datasetsTable.addContainerProperty("sampleMatching", String.class, null, "Sample Matching", null, Table.Align.LEFT);
        datasetsTable.setColumnCollapsed("sampleMatching", !activeColumnHeaders[9]);

        datasetsTable.addContainerProperty("shotgunTargeted", String.class, null, "Shotgun/Targeted", null, Table.Align.LEFT);
        datasetsTable.setColumnCollapsed("shotgunTargeted", !activeColumnHeaders[10]);

        datasetsTable.addContainerProperty("technology", String.class, null, "Technology", null, Table.Align.LEFT);
        datasetsTable.setColumnCollapsed("technology", !activeColumnHeaders[11]);

        datasetsTable.addContainerProperty("analyticalApproach", String.class, null, "Analytical Approach", null, Table.Align.LEFT);
        datasetsTable.setColumnCollapsed("analyticalApproach", !activeColumnHeaders[12]);

        datasetsTable.addContainerProperty("enzyme", String.class, null, "Enzyme", null, Table.Align.LEFT);
        datasetsTable.setColumnCollapsed("enzyme", !activeColumnHeaders[13]);

        datasetsTable.addContainerProperty("quantificationBasis", String.class, null, "Quant Basis", null, Table.Align.LEFT);
        datasetsTable.setColumnCollapsed("quantificationBasis", !activeColumnHeaders[14]);

        datasetsTable.addContainerProperty("quantBasisComment", String.class, null, "Quant Basis Comment", null, Table.Align.LEFT);
        datasetsTable.setColumnCollapsed("quantBasisComment", !activeColumnHeaders[15]);

        datasetsTable.addContainerProperty("normalization_strategy", String.class, null, "Normalization Strategy", null, Table.Align.LEFT);
        datasetsTable.setColumnCollapsed("normalization_strategy", !activeColumnHeaders[16]);

        datasetsTable.addContainerProperty("PumedID", CustomExternalLink.class, null);
        datasetsTable.setColumnCollapsed("PumedID", !activeColumnHeaders[17]);

        datasetsTable.addContainerProperty("patientsGroup1", String.class, null, "Patients Gr.I", null, Table.Align.RIGHT);
        datasetsTable.setColumnCollapsed("patientsGroup1", !activeColumnHeaders[18]);

        datasetsTable.addContainerProperty("patientsGroup1Number", Integer.class, null, "#Patients Gr.I", null, Table.Align.RIGHT);
        datasetsTable.setColumnCollapsed("patientsGroup1Number", !activeColumnHeaders[19]);

        datasetsTable.addContainerProperty("patientsGroup1Comm", String.class, null, "Patients Gr.I Comm", null, Table.Align.LEFT);
        datasetsTable.setColumnCollapsed("patientsGroup1Comm", !activeColumnHeaders[20]);

        datasetsTable.addContainerProperty("patientsSubGroup1", String.class, null, "Patients Sub-Gr.I", null, Table.Align.RIGHT);
        datasetsTable.setColumnCollapsed("patientsSubGroup1", !activeColumnHeaders[21]);

        datasetsTable.addContainerProperty("patientsGroup2", String.class, null, "Patients Gr.II", null, Table.Align.RIGHT);
        datasetsTable.setColumnCollapsed("patientsGroup2", !activeColumnHeaders[22]);

        datasetsTable.addContainerProperty("patientsGroup2Number", Integer.class, null, "#Patients Gr.II", null, Table.Align.RIGHT);
        datasetsTable.setColumnCollapsed("patientsGroup2Number", !activeColumnHeaders[23]);

        datasetsTable.addContainerProperty("patientsGroup2Comm", String.class, null, "Patients Gr.II Comm", null, Table.Align.LEFT);
        datasetsTable.setColumnCollapsed("patientsGroup2Comm", !activeColumnHeaders[24]);

        datasetsTable.addContainerProperty("patientsSubGroup2", String.class, null, "Patients Sub-Gr.II", null, Table.Align.RIGHT);
        datasetsTable.setColumnCollapsed("patientsSubGroup2", !activeColumnHeaders[25]);

        datasetsTable.addContainerProperty("additionalComments", String.class, null, "Comments", null, Table.Align.RIGHT);
        datasetsTable.setColumnCollapsed("additionalComments", !activeColumnHeaders[26]);

        datasetsTable.addValueChangeListener(this);

        selectionChanged("Disease_Groups_Level");

    }

    /**
     * update table items
     *
     * @param datasetExploringCentralSelectionManager
     * @param activeHeaders
     */
    private void updateCombinedQuantDatasetTableRecords(Map<Integer, QuantDatasetObject> updatedStudiesList) {
        dsIndexes = new int[updatedStudiesList.size()];
        datasetsTable.removeAllItems();
        tableLayout.updateCounter(updatedStudiesList.size() + "/" + totalStudiesNumber);
        int index = 0;

        for (QuantDatasetObject pb : updatedStudiesList.values()) {
            if (pb == null) {
                continue;
            }
            String rawDatalink;
            if (pb.getRawDataUrl().equalsIgnoreCase("Raw Data Not Available")) {
                rawDatalink = "Not available";
            } else {
                rawDatalink = "Available";
//                rawDatalink = new CustomExternalLink("Get Raw Data", pb.getRawDataUrl());
//                rawDatalink.setDescription("Link To Raw Data");
            }
            Integer quantProtNum = pb.getQuantifiedProteinsNumber();
            if (quantProtNum == -1) {
                quantProtNum = null;
            }

            Integer patGr1Num = pb.getPatientsGroup1Number();
            if (patGr1Num == -1) {
                patGr1Num = null;
            }
            Integer patGr2Num = pb.getPatientsGroup2Number();
            if (patGr2Num == -1) {
                patGr2Num = null;
            }

            CustomExternalLink pumedID = new CustomExternalLink(pb.getPumedID(), "http://www.ncbi.nlm.nih.gov/pubmed/" + pb.getPumedID());
            datasetsTable.addItem(new Object[]{index, pb.getAuthor(), pb.getYear() + "", pb.getIdentifiedProteinsNumber(), quantProtNum, pb.getAnalyticalMethod(), rawDatalink, pb.getFilesNumber(), pb.getTypeOfStudy(), pb.getSampleType(), pb.getSampleMatching(), pb.getShotgunTargeted(), pb.getTechnology(), pb.getAnalyticalApproach(), pb.getEnzyme(), pb.getQuantificationBasis(), pb.getQuantBasisComment(), pb.getNormalizationStrategy(), pumedID, pb.getPatientsGroup1(), patGr1Num, pb.getPatientsGroup1Comm(), pb.getPatientsSubGroup1(), pb.getPatientsGroup2(), patGr2Num, pb.getPatientsGroup2Comm(), pb.getPatientsSubGroup2(), pb.getAdditionalcomments()}, index);
            dsIndexes[index] = pb.getDsKey();
            index++;
        }
        datasetsTable.sort(new Object[]{"Year"}, new boolean[]{false});

    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        if (datasetsTable.getValue() != null) {
            pumedkey = (Integer) datasetsTable.getValue();
        } else {
            if (pumedLabel != null) {
                pumedLabel.rePaintLable("black");

            }
            return;
        }
        if (pumedLabel != null) {
            pumedLabel.rePaintLable("black");

        }
//        if (rawDataLabel != null) {
//            rawDataLabel.rePaintLable("black");
//
//        }
        final Item item = datasetsTable.getItem(pumedkey);
        pumedLabel = (CustomExternalLink) item.getItemProperty("PumedID").getValue();
//        rawDataLabel = (CustomExternalLink) item.getItemProperty("Raw Data").getValue();
        pumedLabel.rePaintLable("white");
//        if (rawDataLabel != null) {
//            rawDataLabel.rePaintLable("white");
//        }
//        int dsIndex = dsIndexes[(Integer) item.getItemProperty("Index").getValue()];
//        datasetExploringCentralSelectionManager.setSelectedDataset(new ArrayList<Integer>(dsIndex));
//        datasetExploringCentralSelectionManager.setStudyLevelFilterSelection(new CSFFilterSelection("Study_Selection", new int[]{dsIndex}, filter_id, null));

    }

    /**
     * event from the central selection manager
     *
     * @param type
     */
    @Override
    public void selectionChanged(String type) {
        if (type.equalsIgnoreCase("Disease_Groups_Level")) {
            updateCombinedQuantDatasetTableRecords(datasetExploringCentralSelectionManager.getFilteredDatasetsList());
        } else if (type.equalsIgnoreCase("Study_Selection")) {
            Map<Integer, QuantDatasetObject> temp = new LinkedHashMap<Integer, QuantDatasetObject>();
            List<Integer> datasetIds = datasetExploringCentralSelectionManager.getSelectedDataset();
            int i = 0;
            for (int datasetId : datasetIds) {
                for (; i < dsIndexes.length; i++) {
                    if (dsIndexes[i] == datasetId) {
                        datasetsTable.select(i);
                        break;
                    }

                }
                QuantDatasetObject qds = datasetExploringCentralSelectionManager.getFullQuantDatasetMap().get(datasetId);

                temp.put(i, qds);
            }

            updateCombinedQuantDatasetTableRecords(temp);
        }
    }

    /**
     * get filter id
     *
     * @return string filter id
     */
    @Override
    public String getFilterId() {
        return filter_id;
    }

    /**
     * remove(un select) filter in the central selection manager (not apply to
     * this class)
     *
     * @param value
     */
    @Override
    public void removeFilterValue(String value) {
    }

}
