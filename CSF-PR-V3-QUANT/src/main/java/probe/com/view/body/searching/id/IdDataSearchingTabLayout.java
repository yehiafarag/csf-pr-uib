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
import probe.com.view.components.GelFractionsLayout;
import probe.com.view.components.IdentificationSearchResultsTableLayout;
import probe.com.view.components.PeptidesTableLayout;
import probe.com.view.core.CustomExportBtnLayout;
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

                int maindsId = handler.setMainDatasetId(datasetName);

                int fractionNumber = handler.getDataset(maindsId).getFractionsNumber();
                if (maindsId != 0 && handler.getDataset(maindsId).getDatasetType() == 1) {
                    CustomExportBtnLayout exportAllProteinPeptidesLayout = new CustomExportBtnLayout(handler, "allProtPep", maindsId, datasetName, accession, otherAccession, null, 0, null, null, null, desc);
                    PopupView exportAllProteinPeptidesPopup = new PopupView("Export Peptides from All Datasets for (" + accession + " )", exportAllProteinPeptidesLayout);
                    exportAllProteinPeptidesPopup.setDescription("Export CSF-PR Peptides for ( " + accession + " ) for All Available Datasets");
                    searcheResultsTableLayout.setExpBtnProtAllPepTable(exportAllProteinPeptidesPopup);// new PopupView("Export Proteins", (new CustomExportBtnLayout(handler, "prots",datasetId, datasetName, accession, otherAccession, datasetList, proteinsList, dataset.getFractionsNumber(), null,null))));
                    if (key >= 0) {

                        Map<Integer, PeptideBean> pepProtList = handler.getPeptidesProtList(maindsId, accession, otherAccession);

                        if (!pepProtList.isEmpty()) {
                            int validPep = handler.getValidatedPepNumber(pepProtList);
                            if (idPeptideTableLayout != null) {
                                idPeptidesLayout.removeComponent(idPeptideTableLayout);
                            }
                            idPeptideTableLayout = new PeptidesTableLayout(validPep, pepProtList.size(), desc, pepProtList, accession, handler.getDataset(maindsId).getName());
                            idPeptidesLayout.setMargin(false);
                            idPeptidesLayout.addComponent(idPeptideTableLayout);

                            CustomExportBtnLayout exportAllProteinsPeptidesLayout = new CustomExportBtnLayout(handler, "protPep", maindsId, handler.getDataset(maindsId).getName(), accession, otherAccession, null, 0, pepProtList, null, null, desc);
                            PopupView exportAllProteinsPeptidesPopup = new PopupView("Export Peptides from Selected Dataset for (" + accession + " )", exportAllProteinsPeptidesLayout);

                            exportAllProteinsPeptidesPopup.setDescription("Export Peptides from ( " + handler.getDataset(maindsId).getName() + " ) Dataset for ( " + accession + " )");
                            idPeptideTableLayout.setExpBtnPepTable(exportAllProteinsPeptidesPopup);

                        }
                        List<StandardProteinBean> standerdProtList = handler.retrieveStandardProtPlotList(maindsId);//                          

                        if (fractionNumber == 0 || maindsId == 0 || standerdProtList == null || standerdProtList.isEmpty()) {
                            idFractionLayout.removeAllComponents();
                            if (searcheResultsTableLayout.getSearchTable() != null) {
                                searcheResultsTableLayout.getSearchTable().setHeight("267.5px");
                            }
                            if (idPeptideTableLayout.getPepTable() != null) {
                                idPeptideTableLayout.getPepTable().setHeight("267.5px");
                                idPeptideTableLayout.setPeptideTableHeight("267.5px");
                            }
                        } else {
                            Map<Integer, IdentificationProteinBean> fractionsList = handler.getProtGelFractionsList(maindsId, accession, otherAccession);

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

                                idFractionLayout.addComponent(new GelFractionsLayout(accession, mw, fractionsList, standerdProtList, handler.getDataset(maindsId).getName()));

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
