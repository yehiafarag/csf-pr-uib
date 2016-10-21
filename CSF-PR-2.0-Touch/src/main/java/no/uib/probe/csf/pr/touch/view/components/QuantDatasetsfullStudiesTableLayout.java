package no.uib.probe.csf.pr.touch.view.components;

import com.vaadin.data.Item;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.Reindeer;
import java.util.Locale;
import java.util.Map;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDataset;

/**
 *
 * @author Yehia Farag
 *
 * quant dataset combined table layout
 */
public class QuantDatasetsfullStudiesTableLayout extends Table{

    private int[] dsIndexes;
    /**
     *
     * @param activeColumnHeaders
     */
    public QuantDatasetsfullStudiesTableLayout(boolean[] activeColumnHeaders) {
//        this.setStyleName(Reindeer.LAYOUT_WHITE);
        initCombinedQuantDatasetTable(activeColumnHeaders);
//        this.setHeight("420px");
//        this.setWidth("100%");
    }

    /**
     * initialize the main quant dataset table
     *
     * @param activeColumnHeaders the visualized columns in the table
     */
    private void initCombinedQuantDatasetTable(boolean[] activeColumnHeaders) {
        this.setSelectable(true);
        this.setLocale(Locale.UK);
        this.setVisible(false);
        this.setColumnReorderingAllowed(true);
        this.setColumnCollapsingAllowed(true);
        this.setDragMode(Table.TableDragMode.NONE);
        this.setMultiSelect(false);//        
        this.setImmediate(true); // react at once when something is selected
        this.setWidth("100%");
        this.addContainerProperty("Index", Integer.class, null, "Index", null, Table.Align.RIGHT);
        this.addContainerProperty("Author", String.class, null);
        this.setColumnCollapsed("Author", false);
        this.addContainerProperty("Year", String.class, null, "Year", null, Table.Align.RIGHT);
        this.setColumnCollapsed("Year", false);
        this.addContainerProperty("#Identified Proteins", Integer.class, null, "#Identified Proteins", null, Table.Align.RIGHT);
        this.setColumnCollapsed("#Identified Proteins", !activeColumnHeaders[2]);
        this.addContainerProperty("#Quantified Proteins", Integer.class, null, "#Quantified Proteins", null, Table.Align.RIGHT);
        this.setColumnCollapsed("#Quantified Proteins", !activeColumnHeaders[3]);

        this.addContainerProperty("Analytical Method", String.class, null);
        this.setColumnCollapsed("Analytical Method", !activeColumnHeaders[4]);

        this.addContainerProperty("Raw Data", String.class, null);
        this.setColumnCollapsed("Raw Data", !activeColumnHeaders[5]);
//        this.addContainerProperty("#Files", Integer.class, null, "#Files", null, Table.Align.RIGHT);
//        this.setColumnCollapsed("#Files", !activeColumnHeaders[6]);
        this.addContainerProperty("typeOfStudy", String.class, null, "Study Type", null, Table.Align.LEFT);
        this.setColumnCollapsed("typeOfStudy", !activeColumnHeaders[7]);

        this.addContainerProperty("sampleType", String.class, null, "Sample Type", null, Table.Align.LEFT);
        this.setColumnCollapsed("sampleType", !activeColumnHeaders[8]);

        this.addContainerProperty("sampleMatching", String.class, null, "Sample Matching", null, Table.Align.LEFT);
        this.setColumnCollapsed("sampleMatching", !activeColumnHeaders[9]);

        this.addContainerProperty("shotgunTargeted", String.class, null, "Shotgun/Targeted", null, Table.Align.LEFT);
        this.setColumnCollapsed("shotgunTargeted", !activeColumnHeaders[10]);

        this.addContainerProperty("technology", String.class, null, "Technology", null, Table.Align.LEFT);
        this.setColumnCollapsed("technology", !activeColumnHeaders[11]);

        this.addContainerProperty("analyticalApproach", String.class, null, "Analytical Approach", null, Table.Align.LEFT);
        this.setColumnCollapsed("analyticalApproach", !activeColumnHeaders[12]);

        this.addContainerProperty("enzyme", String.class, null, "Enzyme", null, Table.Align.LEFT);
        this.setColumnCollapsed("enzyme", !activeColumnHeaders[13]);

        this.addContainerProperty("quantificationBasis", String.class, null, "Quant Basis", null, Table.Align.LEFT);
        this.setColumnCollapsed("quantificationBasis", !activeColumnHeaders[14]);

        this.addContainerProperty("quantBasisComment", String.class, null, "Quant Basis Comment", null, Table.Align.LEFT);
        this.setColumnCollapsed("quantBasisComment", !activeColumnHeaders[15]);

        this.addContainerProperty("normalization_strategy", String.class, null, "Normalization Strategy", null, Table.Align.LEFT);
        this.setColumnCollapsed("normalization_strategy", !activeColumnHeaders[16]);

        this.addContainerProperty("PumedID", String.class, null);
        this.setColumnCollapsed("PumedID", !activeColumnHeaders[17]);

        this.addContainerProperty("patientsGroup1", String.class, null, "Patients Gr.I", null, Table.Align.RIGHT);
        this.setColumnCollapsed("patientsGroup1", !activeColumnHeaders[18]);

        this.addContainerProperty("patientsGroup1Number", Integer.class, null, "#Patients Gr.I", null, Table.Align.RIGHT);
        this.setColumnCollapsed("patientsGroup1Number", !activeColumnHeaders[19]);

        this.addContainerProperty("patientsGroup1Comm", String.class, null, "Patients Gr.I Comm", null, Table.Align.LEFT);
        this.setColumnCollapsed("patientsGroup1Comm", !activeColumnHeaders[20]);

        this.addContainerProperty("patientsSubGroup1", String.class, null, "Patients Sub-Gr.I", null, Table.Align.RIGHT);
        this.setColumnCollapsed("patientsSubGroup1", !activeColumnHeaders[21]);

        this.addContainerProperty("patientsGroup2", String.class, null, "Patients Gr.II", null, Table.Align.RIGHT);
        this.setColumnCollapsed("patientsGroup2", !activeColumnHeaders[22]);

        this.addContainerProperty("patientsGroup2Number", Integer.class, null, "#Patients Gr.II", null, Table.Align.RIGHT);
        this.setColumnCollapsed("patientsGroup2Number", !activeColumnHeaders[23]);

        this.addContainerProperty("patientsGroup2Comm", String.class, null, "Patients Gr.II Comm", null, Table.Align.LEFT);
        this.setColumnCollapsed("patientsGroup2Comm", !activeColumnHeaders[24]);

        this.addContainerProperty("patientsSubGroup2", String.class, null, "Patients Sub-Gr.II", null, Table.Align.RIGHT);
        this.setColumnCollapsed("patientsSubGroup2", !activeColumnHeaders[25]);

        this.addContainerProperty("additionalComments", String.class, null, "Comments", null, Table.Align.RIGHT);
        this.setColumnCollapsed("additionalComments", !activeColumnHeaders[26]);


    }

    /**
     * update table items
     *
     * @param updatedStudiesList
     */
    public void updateCombinedQuantDatasetTableRecords(Map<Integer, QuantDataset> updatedStudiesList) {
        dsIndexes = new int[updatedStudiesList.size()];
        this.removeAllItems();
        int index = 0;

        for (QuantDataset pb : updatedStudiesList.values()) {
            if (pb == null) {
                continue;
            }
            String rawDatalink;
            if (pb.getRawDataUrl().equalsIgnoreCase("Raw Data Not Available")) {
                rawDatalink = "Not available";
            } else {
                rawDatalink = "Available";
            }
            Integer quantProtNum = pb.getQuantifiedProteinsNumber();
            if (quantProtNum == -1) {
                quantProtNum = null;
            }

            Integer patGr1Num = pb.getDiseaseMainGroup1Number();
            if (patGr1Num == -1) {
                patGr1Num = null;
            }
            Integer patGr2Num = pb.getDiseaseMainGroup2Number();
            if (patGr2Num == -1) {
                patGr2Num = null;
            }
            Integer idNumber = pb.getIdentifiedProteinsNumber();
            if (idNumber == -1) {
                idNumber = null;
            }

            String pumedID = pb.getPubMedId();
            this.addItem(new Object[]{index, pb.getAuthor(), pb.getYear() + "", idNumber, quantProtNum, pb.getAnalyticalMethod(), rawDatalink,pb.getTypeOfStudy(), pb.getSampleType(), pb.getSampleMatching(), pb.getShotgunTargeted(), pb.getTechnology(), pb.getAnalyticalApproach(), pb.getEnzyme(), pb.getQuantificationBasis(), pb.getQuantBasisComment(), pb.getNormalizationStrategy(), pumedID, pb.getDiseaseMainGroupI().split("\n")[0], patGr1Num, pb.getDiseaseMainGroup1Comm(), pb.getDiseaseSubGroup1().split("\n")[0], pb.getDiseaseMainGroup2().split("\n")[0], patGr2Num, pb.getDiseaseMainGroup2Comm(), pb.getDiseaseSubGroup2().split("\n")[0], pb.getAdditionalcomments()}, index);
            dsIndexes[index] = pb.getQuantDatasetIndex();
            index++;
        }
        this.sort(new Object[]{"Year","Author"}, new boolean[]{false,true});
        this.setSortAscending(false);
        int indexing = 1;

        for (Object id : this.getItemIds()) {
            Item item = this.getItem(id);
            item.getItemProperty("Index").setValue(indexing);            
            indexing++;
        }

    }
   

}
