/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.searching.id;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import probe.com.handlers.MainHandler;
import probe.com.model.beans.IdentificationProteinBean;
import probe.com.model.beans.PeptideBean;
import probe.com.model.beans.StandardProteinBean;
import probe.com.view.body.identificationlayoutcomponents.GelFractionsLayout;
import probe.com.view.body.identificationlayoutcomponents.PeptidesTableLayout;
import probe.com.view.core.exporter.ExporterBtnsGenerator;
import probe.com.view.core.CustomExternalLink;

/**
 *
 * @author Yehia Farag
 */
public class IdDataSearchingTabLayout extends VerticalLayout implements Serializable {

    private final VerticalLayout idSearchingResultsTableLayout, idPeptidesLayout, idFractionLayout;
    private PeptidesTableLayout idPeptideTableLayout;

    public IdDataSearchingTabLayout(Map<Integer, IdentificationProteinBean> searchIdentificationProtList, final MainHandler handler) {
        this.setMargin(true);
        this.setSpacing(true);
        idSearchingResultsTableLayout = new VerticalLayout();
        idSearchingResultsTableLayout.setHeight("100%");
        idFractionLayout = new VerticalLayout();
        idPeptidesLayout = new VerticalLayout();
        this.addComponent(idSearchingResultsTableLayout);
        this.addComponent(idFractionLayout);
        this.addComponent(idPeptidesLayout);

        final IdentificationSearchResultsTableLayout searcheResultsTableLayout = new IdentificationSearchResultsTableLayout(handler, handler.getDatasetDetailsList(), searchIdentificationProtList, false);
        idSearchingResultsTableLayout.addComponent(searcheResultsTableLayout);
        final ExporterBtnsGenerator dataExporter = new ExporterBtnsGenerator(handler);
        Property.ValueChangeListener listener = new Property.ValueChangeListener() {
            /*
             *the main listener for search table
             */
            private static final long serialVersionUID = 1L;
            private CustomExternalLink searchTableAccessionLable;
            private int key;

            /**
             * on select search table value initialize the peptides table and
             * fractions plots if exist * process
             *
             * @param event value change on search table selection
             */
            @Override
            public synchronized void valueChange(Property.ValueChangeEvent event) {

                if (searchTableAccessionLable != null) {
                    searchTableAccessionLable.rePaintLable("black");
                }

                if (searcheResultsTableLayout.getSearchTable().getValue() != null) {
                    key = (Integer) searcheResultsTableLayout.getSearchTable().getValue();
                } else {
                    return;
                }
                final Item item = searcheResultsTableLayout.getSearchTable().getItem(key);
                searchTableAccessionLable = (CustomExternalLink) item.getItemProperty("Accession").getValue();
                searchTableAccessionLable.rePaintLable("white");

                idPeptidesLayout.removeAllComponents();
                idFractionLayout.removeAllComponents();

                String datasetName = item.getItemProperty("Experiment").toString();
                String accession = item.getItemProperty("Accession").toString();
                String otherAccession = item.getItemProperty("Other Protein(s)").toString();
                String desc = item.getItemProperty("Description").toString();

                int datasetId = handler.setMainDatasetId(datasetName);

                int fractionNumber = handler.getDataset(datasetId).getFractionsNumber();
                if (datasetId != 0 && handler.getDataset(datasetId).getDatasetType() == 1) {

                    VerticalLayout allPeptidesProteinsExportLayout = dataExporter.exportAllAvailablePeptidesForProtein(accession, otherAccession, desc, true, "Export Peptides from All Datasets for ( " + accession + " )");
                    allPeptidesProteinsExportLayout.setDescription("Export CSF-PR Peptides for ( " + accession + " ) from All Datasets");
                    searcheResultsTableLayout.setExpBtnProtAllPepTable(allPeptidesProteinsExportLayout);
                    if (key >= 0) {
                        Map<Integer, PeptideBean> pepProtList = handler.getPeptidesProtList(datasetId, accession, otherAccession);
                        if (!pepProtList.isEmpty()) {
                            int validPep = handler.getValidatedPepNumber(pepProtList);
                            if (idPeptideTableLayout != null) {
                                idPeptidesLayout.removeComponent(idPeptideTableLayout);
                            }
                            idPeptideTableLayout = new PeptidesTableLayout(validPep, pepProtList.size(), desc, pepProtList, accession, handler.getDataset(datasetId).getName());
                            idPeptidesLayout.setMargin(false);
                            idPeptidesLayout.addComponent(idPeptideTableLayout);

                            VerticalLayout proteinPeptidesExportLayout = dataExporter.exportPeptidesForProtein(datasetId, accession, otherAccession, desc, pepProtList, true, "Export Peptides from Selected Dataset for ( " + accession + " )");
                            proteinPeptidesExportLayout.setDescription("Export Peptides from ( " + handler.getDataset(datasetId).getName() + " ) Dataset for ( " + accession + " )");

                            idPeptideTableLayout.setExpBtnPepTable(proteinPeptidesExportLayout);

                        }
                        List<StandardProteinBean> standerdProtList = handler.retrieveStandardProtPlotList(datasetId);//                          

                        if (fractionNumber == 0 || datasetId == 0 || standerdProtList == null || standerdProtList.isEmpty()) {
                            idFractionLayout.removeAllComponents();
                            if (searcheResultsTableLayout.getSearchTable() != null) {
                                searcheResultsTableLayout.getSearchTable().setHeight("267.5px");
                            }
                            if (idPeptideTableLayout.getPepTable() != null) {
                                idPeptideTableLayout.getPepTable().setHeight("267.5px");
                                idPeptideTableLayout.setPeptideTableHeight("267.5px");
                            }
                        } else {
                            Map<Integer, IdentificationProteinBean> fractionsList = handler.getProtGelFractionsList(datasetId, accession, otherAccession);

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

                                idFractionLayout.addComponent(new GelFractionsLayout(handler,accession, mw, fractionsList, standerdProtList, handler.getDataset(datasetId).getName()));

                            }
                        }
                    }
                }
            }

        };
        searcheResultsTableLayout.setListener(listener);
        searcheResultsTableLayout.getSearchTable().addValueChangeListener(listener);

    }

}
