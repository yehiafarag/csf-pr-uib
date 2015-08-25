package probe.com.view.body.identificationlayoutcomponents;

import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import probe.com.model.beans.identification.IdentificationPeptideBean;
import probe.com.view.core.ShowLabel;
import probe.com.view.core.TableResizeSet;

/**
 *
 * @author Yehia Farag
 * 
 * this class represents the identification peptides table container
 * this container contains the identification peptides table, table resize buttons  and export buttons
 */
public class IdentificationPeptidesTableLayout extends VerticalLayout implements Serializable {

    private String peptideTableHeight = "160px";
    private TableResizeSet trs;
    private IdentificationPeptideTable peptideTable;
    private VerticalLayout mainLayout;
    private ShowLabel show;
    private boolean stat;
    private VerticalLayout pepTableLayout = new VerticalLayout();
    private final VerticalLayout exportPepLayout = new VerticalLayout();
    private VerticalLayout exportBtnForIdentificationPeptideTable;
    private IdentificationPeptideTable validPeptideTable, currentTable;

    /**
     * get the current active identification peptides table 
     * @return identification peptides table
     */
    public IdentificationPeptideTable getPepTable() {
        return currentTable;
    }

    /**
     *
     * @param validatedPeptidesNumber
     * @param totalPeptidesNumber
     * @param proteinDescription
     * @param identificationProteinPeptidesList
     * @param accession
     * @param datasetName
     */
    public IdentificationPeptidesTableLayout(final int validatedPeptidesNumber, final int totalPeptidesNumber, final String proteinDescription, final Map<Integer, IdentificationPeptideBean> identificationProteinPeptidesList, final String accession, final String datasetName) {
        MarginInfo m = new MarginInfo(false, false, true, false);
        this.setMargin(m);
        this.setSpacing(false);
        this.setWidth("100%");

        final HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setHeight("45px");
        headerLayout.setSpacing(true);
        show = new ShowLabel(true);
        headerLayout.addComponent(show);
        headerLayout.setComponentAlignment(show, Alignment.BOTTOM_LEFT);
        stat = true;

        final Label pepLabel = new Label("<h4 style='font-family:verdana;color:black;'>Peptides (" + validatedPeptidesNumber + ") " + proteinDescription + "</h4>");

        pepLabel.setContentMode(ContentMode.HTML);
        pepLabel.setHeight("45px");
        headerLayout.addComponent(pepLabel);
        headerLayout.setComponentAlignment(pepLabel, Alignment.TOP_RIGHT);

        this.addComponent(headerLayout);
        mainLayout = new VerticalLayout();
        mainLayout.setWidth("100%");
        this.addComponent(mainLayout);
        mainLayout.addComponent(pepTableLayout);
        mainLayout.setComponentAlignment(pepTableLayout, Alignment.MIDDLE_CENTER);

        Map<Integer, IdentificationPeptideBean> vPepProtList = getValidatedList(identificationProteinPeptidesList);

        validPeptideTable = new IdentificationPeptideTable(vPepProtList, null, false, proteinDescription);
        pepTableLayout.addComponent(validPeptideTable);
        if (trs != null) {
            peptideTableHeight = trs.getCurrentSize();
        }
        validPeptideTable.setHeight(peptideTableHeight);
        currentTable = validPeptideTable;
        HorizontalLayout lowerLayout = new HorizontalLayout();
        lowerLayout.setWidth("100%");
        lowerLayout.setHeight("25px");
        lowerLayout.setSpacing(false);
        mainLayout.addComponent(lowerLayout);
        mainLayout.setComponentAlignment(lowerLayout, Alignment.TOP_CENTER);

        HorizontalLayout lowerLeftLayout = new HorizontalLayout();
        lowerLayout.addComponent(lowerLeftLayout);
        lowerLeftLayout.setSpacing(true);
        lowerLeftLayout.setMargin(new MarginInfo(false, false, false, false));
        lowerLayout.setComponentAlignment(lowerLeftLayout, Alignment.MIDDLE_LEFT);

        HorizontalLayout lowerRightLayout = new HorizontalLayout();

        lowerRightLayout.setWidth("450px");
        lowerLayout.addComponent(lowerRightLayout);
        lowerLayout.setComponentAlignment(lowerRightLayout, Alignment.BOTTOM_RIGHT);

        final OptionGroup selectionType = new OptionGroup();
        selectionType.setMultiSelect(true);
        selectionType.addItem("\t\tShow Validated Peptides Only");
        selectionType.select("\t\tShow Validated Peptides Only");
        selectionType.setHeight("15px");
        lowerLeftLayout.addComponent(selectionType);
        lowerLeftLayout.setComponentAlignment(selectionType, Alignment.BOTTOM_LEFT);

        final TableResizeSet trs1 = new TableResizeSet(validPeptideTable, peptideTableHeight);//resize tables 
        lowerLeftLayout.addComponent(trs1);
        lowerLeftLayout.setComponentAlignment(trs1, Alignment.BOTTOM_LEFT);

        exportPepLayout.setWidth("300px");
        lowerRightLayout.addComponent(exportPepLayout);
        lowerRightLayout.setComponentAlignment(exportPepLayout, Alignment.BOTTOM_RIGHT);

        mainLayout.setSpacing(true);

        headerLayout.addLayoutClickListener(new com.vaadin.event.LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {

                if (stat) {
                    stat = false;
                    show.updateIcon(false);
                    mainLayout.setVisible(false);
                } else {
                    stat = true;
                    show.updateIcon(true);
                    mainLayout.setVisible(true);
                }
            }
        });
        selectionType.setImmediate(true);
        selectionType.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (selectionType.isSelected("\t\tShow Validated Peptides Only")) {

                    headerLayout.removeAllComponents();
                    headerLayout.addComponent(show);
                    headerLayout.setComponentAlignment(show, Alignment.BOTTOM_LEFT);

                    pepLabel.setContentMode(ContentMode.HTML);
                    pepLabel.setHeight("45px");
                    headerLayout.addComponent(pepLabel);
                    headerLayout.setComponentAlignment(pepLabel, Alignment.TOP_RIGHT);

                    pepTableLayout.removeAllComponents();
                    pepTableLayout.addComponent(validPeptideTable);
                    trs1.setTable(validPeptideTable);
                    validPeptideTable.setHeight(peptideTable.getHeight() + "");

                } else {
                    headerLayout.removeAllComponents();
                    headerLayout.addComponent(show);
                    headerLayout.setComponentAlignment(show, Alignment.BOTTOM_LEFT);
                    Label pepLabel = new Label("<h4 style='font-family:verdana;color:black;'>Peptides (" + validatedPeptidesNumber + "/" + totalPeptidesNumber + ") " + proteinDescription + "</h4>");
                    pepLabel.setContentMode(ContentMode.HTML);
                    headerLayout.addComponent(pepLabel);
                    headerLayout.setComponentAlignment(pepLabel, Alignment.TOP_RIGHT);

                    pepTableLayout.removeAllComponents();
                    peptideTable = new IdentificationPeptideTable(identificationProteinPeptidesList, null, false, proteinDescription);
                    pepTableLayout.addComponent(peptideTable);
                    trs1.setTable(peptideTable);
                    peptideTable.setHeight(validPeptideTable.getHeight() + "");
                    currentTable = peptideTable;

                }
            }

        });

    }

    private Map<Integer, IdentificationPeptideBean> getValidatedList(Map<Integer, IdentificationPeptideBean> pepProtList) {
        Map<Integer, IdentificationPeptideBean> vPepList = new HashMap<Integer, IdentificationPeptideBean>();
        for (int key : pepProtList.keySet()) {
            IdentificationPeptideBean pb = pepProtList.get(key);
            if (pb.getValidated() == 1) {
                vPepList.put(key, pb);
            }

        }
        return vPepList;

    }

   
    /**
     * update the current visualized identification peptides table height
     * @param peptideTableHeight
     */
    public void setPeptideTableHeight(String peptideTableHeight) {
        this.peptideTableHeight = peptideTableHeight;
    }

   


    /**
     * add exporting button to the current identification peptides table layout
     * @param expBtnPepPepTable
     */
    public void setExportingBtnForIdentificationPeptidesTable(VerticalLayout expBtnPepPepTable) {
        this.exportBtnForIdentificationPeptideTable = expBtnPepPepTable;
        exportPepLayout.removeAllComponents();
        exportPepLayout.addComponent(exportBtnForIdentificationPeptideTable);
        exportPepLayout.setComponentAlignment(exportBtnForIdentificationPeptideTable, Alignment.MIDDLE_LEFT);

    }

   
}
