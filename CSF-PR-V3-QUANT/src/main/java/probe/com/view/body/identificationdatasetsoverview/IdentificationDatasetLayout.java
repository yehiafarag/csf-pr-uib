/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.identificationdatasetsoverview;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.vaadin.actionbuttontextfield.ActionButtonTextField;
import org.vaadin.actionbuttontextfield.widgetset.client.ActionButtonType;
import probe.com.handlers.CSFPRHandler;
import probe.com.model.beans.identification.IdentificationProteinBean;
import probe.com.model.beans.identification.IdentificationPeptideBean;
import probe.com.model.beans.identification.StandardIdentificationFractionPlotProteinBean;
import probe.com.view.body.identificationlayoutcomponents.IdentificationGelFractionsLayout;
import probe.com.view.body.identificationlayoutcomponents.IdentificationPeptidesTableLayout;
import probe.com.view.body.identificationdatasetsoverview.identificationdataset.IdentificationProteinsTableLayout;
import probe.com.view.core.exporter.ExporterBtnsGenerator;
import probe.com.view.core.CustomExternalLink;
import probe.com.view.core.HideOnClickLayout;

/**
 *
 * @author Yehia Farag
 *
 * this class represent the identification dataset information layout (one row
 * in the available identification datasets list)
 */
public class IdentificationDatasetLayout extends VerticalLayout implements Serializable, Property.ValueChangeListener {

    private final CSFPRHandler handler;
    private final int datasetId;
    private final VerticalLayout container, fractionsLayout, peptidesLayout;
    private IdentificationProteinsTableLayout protTableLayout;
    private IdentificationPeptidesTableLayout peptideTableLayout;

    private Map<String, IdentificationProteinBean> proteinsList;
    private TreeMap<Integer, Object> selectionIndexes;
    private int nextIndex;

    /**
     *
     * @param handler
     * @param datasetId
     */
    public IdentificationDatasetLayout(CSFPRHandler handler, int datasetId) {
        this.setSizeFull();
        setMargin(true);
        this.handler = handler;
        this.setWidth("100%");
        int height = Page.getCurrent().getBrowserWindowHeight() - 100;
        this.setHeight(height + "px");
        
        container = new VerticalLayout();
        container.setHeightUndefined();
        container.setWidth("100%");
        this.addComponent(container);
        
        this.datasetId = datasetId;
        fractionsLayout = new VerticalLayout();
        peptidesLayout = new VerticalLayout();
        peptidesLayout.setMargin(true);
        buildMainLayout();

    }

    /**
     * initialize main proteins tab layout initialize drop down select dataset
     * list and add main value change listener to it
     */
    private void buildMainLayout() {

        //init dataset Details
        String infoText = "<p style='font-family:verdana;color:black;margin-left:20px;margin-right:20px;'>Select an experiment in the roll down menu on top to view all proteins identified in the selected experiment. Select a protein to see below all Peptides identified for the protein, and if the experiment was based on SDS-PAGE, the protein’s distribution in the gel is displayed under Fractions. To show information about the experiment, press Dataset Information.  Use the search box to navigate in the experiment selected.</p><p  style='font-family:verdana;color:black;margin-left:20px;margin-right:20px;'>Under Fractions, bar charts show the distribution of the selected protein across the fractions cut from the gel. Three charts show number of peptides, number of spectra and average precursor intensity. The fraction number represents the gel pieces cut from top to bottom. Protein standards <font color='#CDE1FF'>(light blue bars)</font> indicate the molecular weight range of each fraction. <font color='#79AFFF'>Darker blue bars</font> mark between which two standards the protein's theoretical mass suggests the protein should be found.";

        IdentificationDatasetInformationLayout datasetInfoLayout = new IdentificationDatasetInformationLayout(handler, datasetId, null);
        HideOnClickLayout dsLayout = new HideOnClickLayout(handler.getDataset(datasetId).getName(), datasetInfoLayout, null, infoText);
        dsLayout.setMargin(new MarginInfo(false, false, false, false));
        container.addComponent(dsLayout);
        //get proteins List
        proteinsList = handler.getIdentificationProteinsList(datasetId);

        protTableLayout = new IdentificationProteinsTableLayout(proteinsList, handler.getDataset(datasetId).getFractionsNumber(), handler.getDataset(datasetId).getNumberValidProt(), handler.getDataset(datasetId).getProteinsNumber());
        container.addComponent(protTableLayout);
        container.setComponentAlignment(protTableLayout, Alignment.TOP_LEFT);
        protTableLayout.getProteinTableComponent().addValueChangeListener(IdentificationDatasetLayout.this);
        protTableLayout.setListener(IdentificationDatasetLayout.this);

        fractionsLayout.setWidth("100%");
        container.addComponent(fractionsLayout);
        peptidesLayout.setWidth("100%");
        container.addComponent(peptidesLayout);
        selectionIndexes = handler.calculateIdentificationProteinsTableSearchIndexesSet(protTableLayout.getProteinTableComponent().getProtToIndexSearchingMap(), protTableLayout.getProteinTableComponent().getTableSearchMapIndex(), protTableLayout.getSearchField().getValue().toUpperCase().trim());
        protTableLayout.getProteinTableComponent().setCurrentPageFirstItemId(protTableLayout.getProteinTableComponent().getFirstIndex());
        protTableLayout.getProteinTableComponent().select(protTableLayout.getProteinTableComponent().getFirstIndex());
        protTableLayout.getProteinTableComponent().commit();
        ActionButtonTextField searchButtonTextField = ActionButtonTextField.extend(protTableLayout.getSearchField());
        searchButtonTextField.getState().type = ActionButtonType.ACTION_SEARCH;
        searchButtonTextField.addClickListener(new ActionButtonTextField.ClickListener() {
            @Override
            public void buttonClick(ActionButtonTextField.ClickEvent clickEvent) {
                selectionIndexes = handler.calculateIdentificationProteinsTableSearchIndexesSet(protTableLayout.getProteinTableComponent().getProtToIndexSearchingMap(), protTableLayout.getProteinTableComponent().getTableSearchMapIndex(), protTableLayout.getSearchField().getValue().toUpperCase().trim());
                if (!selectionIndexes.isEmpty()) {
                    if (selectionIndexes.size() > 1) {
                        protTableLayout.getNextSearch().setEnabled(true);
                        protTableLayout.getNextSearch().focus();
                    } else {
                        protTableLayout.getNextSearch().setEnabled(false);
                    }
                    protIndex = 1;
                    nextIndex = selectionIndexes.firstKey();
                    protTableLayout.getProtCounter().setValue("( " + (protIndex++) + " of " + selectionIndexes.size() + " )");
                    protTableLayout.getProteinTableComponent().setCurrentPageFirstItemId(selectionIndexes.get(nextIndex));
                    protTableLayout.getProteinTableComponent().select(selectionIndexes.get(nextIndex));
                    protTableLayout.getProteinTableComponent().commit();

                } else {
                    Notification.show("Not Exist");
                    protIndex = 1;
                }

            }
        });

        protTableLayout.getSearchField().addFocusListener(new FieldEvents.FocusListener() {
            @Override
            public void focus(FieldEvents.FocusEvent event) {
                protTableLayout.getNextSearch().setEnabled(false);
                protTableLayout.getProtCounter().setValue("");

                protIndex = 1;

            }
        });

        protTableLayout.getNextSearch().addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                nextIndex = selectionIndexes.higherKey(nextIndex);
                protTableLayout.getProteinTableComponent().setCurrentPageFirstItemId(selectionIndexes.get(nextIndex));
                protTableLayout.getProteinTableComponent().select(selectionIndexes.get(nextIndex));
                protTableLayout.getProteinTableComponent().commit();
                protTableLayout.getProtCounter().setValue("( " + (protIndex++) + " of " + selectionIndexes.size() + " )");
                if (nextIndex == selectionIndexes.lastKey()) {
                    nextIndex = selectionIndexes.firstKey() - 1;
                    protIndex = 1;
                }
            }
        });

    }

    private int protIndex = 1;
    private int proteinskey;
    private CustomExternalLink proteinLabel;

    @Override
    public void valueChange(Property.ValueChangeEvent event) {

        if (protTableLayout.getProteinTableComponent().getValue() != null) {
            proteinskey = (Integer) protTableLayout.getProteinTableComponent().getValue();
        } else {
            return;
        }

        fractionsLayout.removeAllComponents();
        peptidesLayout.removeAllComponents();
        if (proteinLabel != null) {
            proteinLabel.rePaintLable("black");
        }
        protTableLayout.setLastSelectedIndex(proteinskey);
        final Item item = protTableLayout.getProteinTableComponent().getItem(proteinskey);
        proteinLabel = (CustomExternalLink) item.getItemProperty("Accession").getValue();
        proteinLabel.rePaintLable("white");

        final String desc = (String) item.getItemProperty("Description").getValue();
        final String accession = item.getItemProperty("Accession").getValue().toString();
        final String otherAccession = (String) item.getItemProperty("Other Protein(s)").getValue();

        ExporterBtnsGenerator dataExporter = new ExporterBtnsGenerator(handler);

        VerticalLayout allPeptidesProteinsExportLayout = dataExporter.exportAllAvailablePeptidesForProtein(accession, otherAccession, desc, true, "Export Peptides from All Datasets for ( " + accession + " )");
        allPeptidesProteinsExportLayout.setDescription("Export CSF-PR Peptides for ( " + accession + " ) from All Datasets");

        VerticalLayout datasetProteinsExportLayout = dataExporter.exportDatasetProteins(datasetId, true, "Export All Proteins from Selected Dataset");
        datasetProteinsExportLayout.setDescription("Export All Proteins from ( " + handler.getDataset(datasetId).getName() + " ) Dataset");

        protTableLayout.setExpBtnProtAllPepTable(allPeptidesProteinsExportLayout, datasetProteinsExportLayout);

        if (proteinskey >= 0) {
            Map<Integer, IdentificationPeptideBean> peptideProteintList = handler.getIdentificationProteinPeptidesList(datasetId, accession, otherAccession);

            if (!peptideProteintList.isEmpty()) {
                int validPep = handler.countValidatedPeptidesNumber(peptideProteintList);
                if (peptideTableLayout != null) {
                    peptidesLayout.removeComponent(peptideTableLayout);
                }
                peptideTableLayout = new IdentificationPeptidesTableLayout(validPep, peptideProteintList.size(), desc, peptideProteintList, accession, handler.getDataset(datasetId).getName());

                peptideTableLayout.setHeight("" + protTableLayout.getHeight());
                peptidesLayout.setHeight("" + protTableLayout.getHeight());
                peptidesLayout.addComponent(peptideTableLayout);

                VerticalLayout proteinPeptidesExportLayout = dataExporter.exportPeptidesForProtein(datasetId, accession, otherAccession, desc, peptideProteintList, true, "Export Peptides from Selected Dataset for ( " + accession + " )");
                proteinPeptidesExportLayout.setDescription("Export Peptides from ( " + handler.getDataset(datasetId).getName() + " ) Dataset for ( " + accession + " )");
                peptideTableLayout.setExportingBtnForIdentificationPeptidesTable(proteinPeptidesExportLayout);

            }
            List<StandardIdentificationFractionPlotProteinBean> standerdProtList = handler.getStandardIdentificationFractionProteinsList(datasetId);

            int fractionsNumber = handler.getDataset(datasetId).getFractionsNumber();

            if (fractionsNumber == 0 || datasetId == 0 || standerdProtList == null || standerdProtList.isEmpty()) {
                fractionsLayout.removeAllComponents();
                if (protTableLayout.getProteinTableComponent() != null) {
                    protTableLayout.getProteinTableComponent().setHeight("267.5px");
                    protTableLayout.setProtTableHeight("267.5px");
                }
                if (peptideTableLayout.getPepTable() != null) {
                    peptideTableLayout.getPepTable().setHeight("267.5px");
                    peptideTableLayout.setPeptideTableHeight("267.5px");
                }
            } else {

                Map<Integer, IdentificationProteinBean> fractionsList = handler.getIdentificationProteinsGelFractionsList(datasetId, accession, otherAccession);

                if (fractionsList != null && !fractionsList.isEmpty()) {
                    double mw = 0.0;
                    try {
                        mw = Double.valueOf(item.getItemProperty("MW").toString());
                    } catch (NumberFormatException e) {
                        String str = item.getItemProperty("MW").toString();
                        String[] strArr = str.split(",");
                        if (strArr.length > 1) {
                            str = strArr[0] + "." + strArr[1];
                        }
                        mw = Double.valueOf(str);
                    }
                    IdentificationGelFractionsLayout gelFractionLayout = new IdentificationGelFractionsLayout(handler, accession, mw, fractionsList, standerdProtList, handler.getDataset(datasetId).getName());
                    gelFractionLayout.setMargin(new MarginInfo(false, false, false, true));
                    fractionsLayout.addComponent(gelFractionLayout);

                }

            }
        }
    }

}
