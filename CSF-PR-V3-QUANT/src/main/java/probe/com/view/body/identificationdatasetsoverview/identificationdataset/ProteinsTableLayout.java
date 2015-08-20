/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.identificationdatasetsoverview.identificationdataset;

import com.vaadin.data.Property;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import probe.com.model.beans.IdentificationProteinBean;
import probe.com.view.core.TableResizeSet;

/**
 *
 * @author Yehia Farag
 */
public class ProteinsTableLayout extends VerticalLayout implements Serializable {

    private String protTableHeight = "160px";
    private Label protCounterLabel;

    /**
     * get protein table height in string pixels
     *
     * @return protein table height
     */
    public String getProtTableHeight() {
        return protTableHeight;
    }

    /**
     * set protein table height in string pixels
     *
     * @param protSize  protein table height
 */
    public void setProtTableHeight(String protSize) {
        this.protTableHeight = protSize;
    }
    private IdentificationProteinsTableComponent protTable, currentVisibleTable;
    private final VerticalLayout exportAllPepLayout = new VerticalLayout();
    private VerticalLayout protTableLayout = new VerticalLayout();
    private VerticalLayout protLabelLayout = new VerticalLayout();
    private VerticalLayout allAvailablePeptidesForProteinLayout;
    private final HorizontalLayout topLayout = new HorizontalLayout();
    private final VerticalLayout exportProtLayout = new VerticalLayout();
    private VerticalLayout datasetProteinsTable;
    private Property.ValueChangeListener listener;
    private IdentificationProteinsTableComponent validProteinsTable;
    private Button nextSearchBtn;
    private int lastSelectedIndex;

    /**
     * get protein table search field
     *
     * @return table search field for protein table
     */
    public TextField getSearchField() {
        return searchField;
    }
    private TextField searchField;

    /**
     * initialize protein table layout
     *
     * @param proteinsList list of proteins beans
     * @param fractionNumber number of fractions for the dataset
     * @param validProtNumber the number of valid proteins
     * @param datasetProteinsNumber number of all proteins in the dataset
     */
    public ProteinsTableLayout(final Map<String, IdentificationProteinBean> proteinsList, final int fractionNumber, final int validProtNumber, final int datasetProteinsNumber) {
       
        this.setWidth("100%");
        this.setSpacing(true);
        this.setMargin(true);
        this.setStyleName(Reindeer.LAYOUT_WHITE);
        this.addComponent(topLayout);

        topLayout.setWidth("100%");
        topLayout.setMargin(false);
        topLayout.setSpacing(false);
        topLayout.addComponent(protLabelLayout);
        topLayout.setComponentAlignment(protLabelLayout, Alignment.TOP_LEFT);

        topLayout.setExpandRatio(protLabelLayout, 0.15f);

        Label protTableHeaderLabel = new Label("<h4 style='font-family:verdana;color:black;font-weight:bold;'>Protein Groups (" + validProtNumber + ")</h4>");
        protTableHeaderLabel.setContentMode(ContentMode.HTML);
        protTableHeaderLabel.setHeight("40px");
        protLabelLayout.addComponent(protTableHeaderLabel);

        //allow search in 
        searchField = new TextField(" ");
        searchField.setDescription("Search Proteins By Name or Accession");

        topLayout.addComponent(searchField);
        topLayout.setComponentAlignment(searchField, Alignment.TOP_RIGHT);
        topLayout.setExpandRatio(searchField, 0.47f);
        nextSearchBtn = new Button();
        nextSearchBtn.setDescription("Next Result");
        nextSearchBtn.setStyleName(Reindeer.BUTTON_LINK);
        nextSearchBtn.setIcon(new ThemeResource("img/next.gif"));
        nextSearchBtn.setWidth("20px");
        nextSearchBtn.setHeight("23px");
        nextSearchBtn.setEnabled(false);

        topLayout.addComponent(nextSearchBtn);
        topLayout.setExpandRatio(nextSearchBtn, 0.03f);
        topLayout.setComponentAlignment(nextSearchBtn, Alignment.BOTTOM_LEFT);

        protCounterLabel = new Label("");
        protCounterLabel.setWidth("150px");
        protCounterLabel.setHeight("23px");
        topLayout.addComponent(protCounterLabel);
        topLayout.setExpandRatio(protCounterLabel, 0.08f);
        topLayout.setComponentAlignment(protCounterLabel, Alignment.BOTTOM_LEFT);

        searchField.setImmediate(true);
        searchField.setWidth("150px");
        searchField.setHeight("23px");

        this.addComponent(protTableLayout);
        this.setComponentAlignment(protTableLayout, Alignment.MIDDLE_CENTER);
        Map<String, IdentificationProteinBean> vProteinsList = getValidatedList(proteinsList);

        validProteinsTable = new IdentificationProteinsTableComponent(vProteinsList, fractionNumber);
        protTableLayout.addComponent(validProteinsTable);
        currentVisibleTable = validProteinsTable;

        HorizontalLayout underTableLayout = new HorizontalLayout();
        underTableLayout.setWidth("100%");
        underTableLayout.setHeight("25px");
        this.addComponent(underTableLayout);
        this.setComponentAlignment(underTableLayout, Alignment.TOP_CENTER);

        HorizontalLayout lowerLeftLayout = new HorizontalLayout();
        lowerLeftLayout.setSpacing(true);
        underTableLayout.addComponent(lowerLeftLayout);
        lowerLeftLayout.setMargin(new MarginInfo(false, false, false, false));
        underTableLayout.setComponentAlignment(lowerLeftLayout, Alignment.MIDDLE_LEFT);

        HorizontalLayout lowerRightLayout = new HorizontalLayout();
        lowerRightLayout.setSpacing(true);
        lowerRightLayout.setWidth("550px");
        underTableLayout.addComponent(lowerRightLayout);
        underTableLayout.setComponentAlignment(lowerRightLayout, Alignment.BOTTOM_RIGHT);
        underTableLayout.setExpandRatio(lowerRightLayout, 0.7f);

        final OptionGroup selectionType = new OptionGroup();
        selectionType.setMultiSelect(true);
        selectionType.addItem("\t\tShow Validated Proteins Only");
        selectionType.select("\t\tShow Validated Proteins Only");

        selectionType.setHeight("15px");
        lowerLeftLayout.addComponent(selectionType);
        lowerLeftLayout.setComponentAlignment(selectionType, Alignment.BOTTOM_LEFT);

        final TableResizeSet trs1 = new TableResizeSet(validProteinsTable, protTableHeight);//resize tables 
        lowerLeftLayout.addComponent(trs1);
        lowerLeftLayout.setComponentAlignment(trs1, Alignment.BOTTOM_LEFT);

        exportAllPepLayout.setWidth("250px");
        lowerRightLayout.addComponent(exportAllPepLayout);
        lowerRightLayout.setComponentAlignment(exportAllPepLayout, Alignment.BOTTOM_RIGHT);

        exportProtLayout.setWidth("230px");
        lowerRightLayout.addComponent(exportProtLayout);
        lowerRightLayout.setComponentAlignment(exportProtLayout, Alignment.BOTTOM_RIGHT);

        selectionType.setImmediate(true);
        selectionType.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                searchField.setValue("");
                nextSearchBtn.setEnabled(false);
                protCounterLabel.setValue("");

                if (selectionType.isSelected("\t\tShow Validated Proteins Only")) {
                    protLabelLayout.removeAllComponents();
                    Label protLabel = new Label("<h4 style='font-family:verdana;color:black;font-weight:bold;'>Protein Groups (" + validProtNumber + ")</h4>");
                    protLabel.setContentMode(ContentMode.HTML);
                    protLabel.setHeight("40px");
                    protLabelLayout.addComponent(protLabel);
                    protTableLayout.removeAllComponents();
                    protTableLayout.addComponent(validProteinsTable);
                    trs1.setTable(validProteinsTable);
                    validProteinsTable.setHeight(protTable.getHeight() + "");
                    protTable.removeValueChangeListener(listener);
                    validProteinsTable.addValueChangeListener(listener);
                    validProteinsTable.select(lastSelectedIndex);
//                    validProteinsTable.commit();
//                    currentVisibleTable = validProteinsTable;

                } else {
                    protLabelLayout.removeAllComponents();
                    Label protLabel = new Label("<h4 style='font-family:verdana;color:black;font-weight:bold;'>Protein Groups (" + validProtNumber + "/" + datasetProteinsNumber + ")</h4>");
                    protLabel.setContentMode(ContentMode.HTML);
                    protLabel.setHeight("40px");
                    protTable = new IdentificationProteinsTableComponent(proteinsList, fractionNumber);
                    protLabelLayout.addComponent(protLabel);
                    protTableLayout.removeAllComponents();
                    protTableLayout.addComponent(protTable);
                    trs1.setTable(protTable);
                    currentVisibleTable = protTable;
                    validProteinsTable.removeValueChangeListener(listener);
                    protTable.addValueChangeListener(listener);
//                    protTable.select(protTable.get);
//                    protTable.commit();
                }
            }
        });
    }

     /**
 *get current visible protein table
 * @return currentVisibleTable 
 * 
 */
    public IdentificationProteinsTableComponent getProteinTableComponent() {
        return currentVisibleTable;
    }

     /**
 * get the popup view for export all peptides for the selected proteins 
 * 
 * @return datasetBtnProtAllPepTablePopup
 */
    public VerticalLayout getDatasetBtnProtAllPepTable() {
        return allAvailablePeptidesForProteinLayout;
    }

      /**
 * set the popup view for export all peptides for the selected proteins 
 * 
 * @param  allPeptidesAvailableForProteinLayout
 * @param datasetProteinsLayout 
 */
    public void setExpBtnProtAllPepTable(VerticalLayout allPeptidesAvailableForProteinLayout, VerticalLayout datasetProteinsLayout) {
        this.allAvailablePeptidesForProteinLayout = allPeptidesAvailableForProteinLayout;
        this.datasetProteinsTable = datasetProteinsLayout;
        updateExportLayouts();

    }

      /**
 * update the layout for exporting buttons
 * and reset the fields
 */
    private void updateExportLayouts() {
        exportAllPepLayout.removeAllComponents();
        exportAllPepLayout.addComponent(allAvailablePeptidesForProteinLayout);
        exportAllPepLayout.setComponentAlignment(allAvailablePeptidesForProteinLayout, Alignment.MIDDLE_LEFT);
        exportProtLayout.removeAllComponents();
        exportProtLayout.addComponent(datasetProteinsTable);
        exportProtLayout.setComponentAlignment(datasetProteinsTable, Alignment.MIDDLE_LEFT);
    }

      /**
 * add value change listener for the proteins table
 * @param listener value change listener
 */
    public void setListener(Property.ValueChangeListener listener) {
        this.listener = listener;
    }

      /**
 * get nextSearchBtn button 
 * @return  nextSearchBtn button
 */
    public Button getNextSearch() {
        return nextSearchBtn;
    }

    /**
 * get label contains the found proteins numbers 
 * @return  protCounterLabel 
 */
    public Label getProtCounter() {
        return protCounterLabel;
    }

     /**
 * get valid proteins list (filter the proteins list)
 * @param proteinsList 
 * @return  validProteinsList 
 */
    private Map<String, IdentificationProteinBean> getValidatedList(Map<String, IdentificationProteinBean> proteinsList) {
        Map<String, IdentificationProteinBean> validProteinsList = new HashMap<String, IdentificationProteinBean>();
        for (String str : proteinsList.keySet()) {
            IdentificationProteinBean pb = proteinsList.get(str);
            if (pb.isValidated()) {
                validProteinsList.put(str, pb);
            }
        }
        return validProteinsList;
    }

    public void setLastSelectedIndex(int lastSelectedIndex) {
        this.lastSelectedIndex = lastSelectedIndex;
    }
}
