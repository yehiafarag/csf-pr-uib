package probe.com.view.core.exporter;

import com.vaadin.addon.tableexport.CsvExport;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import java.io.Serializable;
import com.vaadin.ui.themes.Reindeer;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import probe.com.handlers.MainHandler;
import probe.com.model.beans.DatasetDetailsBean;
import probe.com.model.beans.PeptideBean;
import probe.com.model.beans.IdentificationProteinBean;
import probe.com.view.body.identificationlayoutcomponents.PeptideTable;
import probe.com.view.body.identificationdatasetsoverview.identificationdataset.IdentificationProteinsTableComponent;
import probe.com.view.body.searching.id.IdentificationSearchResultsTable;
/**
 *
 * @author Yehia Farag
 */
public class ExporterBtnsGenerator implements Serializable {

    private final MainHandler handler;
    private CsvExport csvExport = null;
    private ExcelExport excelExport = null;

    public ExporterBtnsGenerator(MainHandler handler) {
        this.handler = handler;

    }

    public VerticalLayout exportDatasetProteins(int datasetId, boolean linkBtn, String btnName) {
        final String datasetName = handler.getDataset(datasetId).getName();
        final Map<String, IdentificationProteinBean> proteinsList = handler.retriveProteinsList(datasetId);
        final int fractionNumber = handler.getDataset(datasetId).getFractionsNumber();
        String tupdatedExpName = datasetName;
        if (datasetName != null) {
            tupdatedExpName = datasetName.replaceAll("[-+.^:,/]", " ");
        }
        final String updatedDatasetName = tupdatedExpName;

        final ExportUnit protExportLayout = new ExportUnit();

        Button exportButton = new Button(btnName);
        if (linkBtn) {
            exportButton.setStyleName(Reindeer.BUTTON_LINK);
        } else {
            exportButton.setStyleName(Reindeer.BUTTON_SMALL);
        }
        final VerticalLayout buttonBody = new VerticalLayout();
        buttonBody.addComponent(exportButton);
        final PopupView exportAllDatasetProteinsPopup = new PopupView(null, protExportLayout);
        buttonBody.addComponent(exportAllDatasetProteinsPopup);
        buttonBody.setComponentAlignment(exportAllDatasetProteinsPopup, Alignment.TOP_RIGHT);

        exportButton.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                exportAllDatasetProteinsPopup.setPopupVisible(true);

            }
        });

        protExportLayout.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                String exportGroupValue = protExportLayout.getExportGroupValue();
                String typeGroupValue = protExportLayout.getTypeGroupValue();
                if (handler.checkFileAvailable("CSF-PR - " + datasetName + " - " + typeGroupValue + " - Proteins." + exportGroupValue)) {
                    String fileURL = handler.getFileUrl("CSF-PR - " + datasetName + " - " + typeGroupValue + " - Proteins." + exportGroupValue);
                    Resource res = new FileResource(new File(fileURL));
                    Page.getCurrent().open(res, null, false);
                    return;
                }

                IdentificationProteinsTableComponent protTable;
                if (typeGroupValue.equalsIgnoreCase("Validated Only")) {
                    Map<String, IdentificationProteinBean> vProteinsList = new HashMap<String, IdentificationProteinBean>();
                    for (String key : proteinsList.keySet()) {
                        IdentificationProteinBean pb = proteinsList.get(key);
                        if (pb.isValidated()) {
                            vProteinsList.put(key, pb);
                        }
                    }
                    protTable = new IdentificationProteinsTableComponent(vProteinsList, fractionNumber);
                } else {
                    protTable = new IdentificationProteinsTableComponent(proteinsList, fractionNumber);
                }
                protTable.setVisible(false);
                buttonBody.addComponent(protTable);
                if (exportGroupValue.equalsIgnoreCase("csv")) {
                    exportCsv(protTable, "Proteins", datasetName, updatedDatasetName, null, typeGroupValue);
                } else {
                    exportXls(protTable, "Proteins", datasetName, updatedDatasetName, null, typeGroupValue);
                }

            }
        });

        return buttonBody;

    }

    public VerticalLayout exportDatasetPeptides(final int datasetId, boolean linkBtn, String btnName) {
        final String datasetName = handler.getDataset(datasetId).getName();

        String tupdatedExpName = datasetName;
        if (datasetName != null) {
            tupdatedExpName = datasetName.replaceAll("[-+.^:,/]", " ");
        }
        final String updatedDatasetName = tupdatedExpName;
        final ExportUnit peptidesExportLayout = new ExportUnit();
        Button exportButton = new Button(btnName);
        if (linkBtn) {
            exportButton.setStyleName(Reindeer.BUTTON_LINK);
        } else {
            exportButton.setStyleName(Reindeer.BUTTON_SMALL);
        }
        final VerticalLayout buttonBody = new VerticalLayout();
        buttonBody.addComponent(exportButton);
        final PopupView exportAllDatasetPeptidesPopup = new PopupView(null, peptidesExportLayout);
        buttonBody.addComponent(exportAllDatasetPeptidesPopup);
        buttonBody.setComponentAlignment(exportAllDatasetPeptidesPopup, Alignment.TOP_RIGHT);

        exportButton.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                exportAllDatasetPeptidesPopup.setPopupVisible(true);

            }
        });

        peptidesExportLayout.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                String exportGroupValue = peptidesExportLayout.getExportGroupValue();
                String typeGroupValue = peptidesExportLayout.getTypeGroupValue();

                if (handler.checkFileAvailable("CSF-PR - " + datasetName + " - All - " + typeGroupValue + " - Peptides." + exportGroupValue)) {
                    String fileURL = handler.getFileUrl("CSF-PR - " + datasetName + " - All - " + typeGroupValue + " - Peptides." + exportGroupValue);
                    FileResource res = new FileResource(new File(fileURL));
                    Page.getCurrent().open(res, null, true);
                    return;
                }

                if (typeGroupValue.equalsIgnoreCase("Validated Only")) {
                    handler.exportPeptidesToFile(datasetId, true, datasetName, typeGroupValue, exportGroupValue);
                } else if (typeGroupValue.equalsIgnoreCase("All")) {
                    handler.exportPeptidesToFile(datasetId, false, datasetName, typeGroupValue, exportGroupValue);
                }
                if (handler.checkFileAvailable("CSF-PR - " + datasetName + " - All - " + typeGroupValue + " - Peptides." + exportGroupValue)) {
                    String fileURL = handler.getFileUrl("CSF-PR - " + datasetName + " - All - " + typeGroupValue + " - Peptides." + exportGroupValue);
                    Resource res = new FileResource(new File(fileURL));
                    Page.getCurrent().open(res, null, false);
                    System.out.println("use new exporter");
                    return;
                }

                Map<Integer, PeptideBean> peptidesList;
                if (typeGroupValue.equalsIgnoreCase("Validated Only")) {
                    peptidesList = handler.getPeptidesList(datasetId, true);
                } else {
                    peptidesList = handler.getPeptidesList(datasetId, false);
                }

                PeptideTable pepTable = new PeptideTable(peptidesList, null, true, null);
                pepTable.setVisible(false);
                buttonBody.addComponent(pepTable);
                if (exportGroupValue.equalsIgnoreCase("csv")) {
                    csvExport = new CsvExport(pepTable, "CSF-PR  " + updatedDatasetName + "   All Peptides");
                    csvExport.setReportTitle("CSF-PR / " + datasetName + " / All Peptides");
                    csvExport.setExportFileName("CSF-PR - " + datasetName + " - All - " + typeGroupValue + " - Peptides." + "csv");
                    csvExport.setMimeType(CsvExport.CSV_MIME_TYPE);
                    csvExport.setDisplayTotals(false);
                    csvExport.export();

                } else {
                    excelExport = new ExcelExport(pepTable, "CSF-PR   " + updatedDatasetName + "   All Peptides");
                    excelExport.setReportTitle("CSF-PR / " + datasetName + " / All Peptides");
                    excelExport.setExportFileName("CSF-PR - " + datasetName + " - All - " + typeGroupValue + " - Peptides." + "xls");
                    excelExport.setMimeType(ExcelExport.EXCEL_MIME_TYPE);
                    excelExport.setDisplayTotals(false);
                    excelExport.export();
                }

            }
        });

        return buttonBody;

    }

    public VerticalLayout exportAllAvailablePeptidesForProtein(final String accession, final String otherAccessions, final String proteinsDescription, boolean linkBtn, String btnName) {

        final ExportUnit peptidesExportLayout = new ExportUnit();
        Button exportButton = new Button(btnName);
        if (linkBtn) {
            exportButton.setStyleName(Reindeer.BUTTON_LINK);
        } else {
            exportButton.setStyleName(Reindeer.BUTTON_SMALL);
        }
        final VerticalLayout buttonBody = new VerticalLayout();
        buttonBody.addComponent(exportButton);
        final PopupView exportAllDatasetPeptidesPopup = new PopupView(null, peptidesExportLayout);
        buttonBody.addComponent(exportAllDatasetPeptidesPopup);
        buttonBody.setComponentAlignment(exportAllDatasetPeptidesPopup, Alignment.TOP_RIGHT);

        exportButton.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                exportAllDatasetPeptidesPopup.setPopupVisible(true);

            }
        });

        peptidesExportLayout.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                String exportGroupValue = peptidesExportLayout.getExportGroupValue();
                String typeGroupValue = peptidesExportLayout.getTypeGroupValue();

                Map<String, PeptideTable> peptidesTableList = null;
                if (typeGroupValue.equalsIgnoreCase("Validated Only")) {
                    peptidesTableList = handler.getProteinPeptidesAllDatasets(accession, otherAccessions, true);
                } else if (typeGroupValue.equalsIgnoreCase("All")) {
                    peptidesTableList = handler.getProteinPeptidesAllDatasets(accession, otherAccessions, false);
                }
                System.out.println("prep size " + peptidesTableList.size());
                if (exportGroupValue.equalsIgnoreCase("csv")) {
//                    exportAllPepCsv(pepList, accession);
                    int index = 0;
                    for (String key : peptidesTableList.keySet()) {
                        PeptideTable peptidesTable = peptidesTableList.get(key);
                        buttonBody.addComponent(peptidesTable);
                        if (index == 0) {
                            csvExport = new CsvExport(peptidesTable, ("CSF-PR " + " " + accession + " Peptides"));
                            csvExport.setReportTitle("CSF-PR / " + key + " / " + accession + " / Peptides");
                            csvExport.setExportFileName("CSF-PR / " + accession + " / Peptides.csv");
                            csvExport.setMimeType(CsvExport.CSV_MIME_TYPE);
                            csvExport.setDisplayTotals(false);
                            csvExport.convertTable();
                            index++;
                        } else {
                            csvExport.setReportTitle("CSF-PR / " + key + " / " + accession + " / Peptides");
                            csvExport.setDisplayTotals(false);
                            csvExport.setRowHeaders(false);
                            csvExport.setNextTable(peptidesTable, key.replaceAll("[-+.^:,/]", " "));
                            csvExport.setDisplayTotals(false);
                            csvExport.convertTable();
                        }
                        index++;
                    }
                    csvExport.setMimeType(CsvExport.CSV_MIME_TYPE);
                    csvExport.setDisplayTotals(false);
                    csvExport.export();

                } else {
                    int index = 0;
                    for (String key : peptidesTableList.keySet()) {
                        PeptideTable peptidesTable = peptidesTableList.get(key);
                        buttonBody.addComponent(peptidesTable);
                        if (index == 0) {
                            excelExport = new ExcelExport(peptidesTable, "CSF-PR  " + "  " + accession + "  Peptides");
                            excelExport.setReportTitle("CSF-PR / " + key + " / " + accession + " / Peptides");
                            excelExport.setExportFileName("CSF-PR / " + accession + " / Peptides.xls");
                            excelExport.setMimeType(CsvExport.EXCEL_MIME_TYPE);
                            excelExport.setDisplayTotals(false);
                            excelExport.convertTable();
                            index++;
                        } else {
                            excelExport.setReportTitle("CSF-PR / " + key + " / " + accession + " / Peptides");
                            excelExport.setDisplayTotals(false);
                            excelExport.setRowHeaders(false);
                            excelExport.setNextTable(peptidesTable, "CSF-PR  " + key.replaceAll("[-+.^:,/]", " ") + "  " + accession + "   Peptides");
                            excelExport.setDisplayTotals(false);
                            excelExport.convertTable();
                        }
                        index++;
                    }
                    excelExport.setMimeType(CsvExport.EXCEL_MIME_TYPE);
                    excelExport.setDisplayTotals(false);
                    excelExport.export();
                }

            }
        });

        return buttonBody;

    }

    public VerticalLayout exportPeptidesForProtein(int datasetId, final String accession, final String otherAccessions, final String mainProtDesc, final Map<Integer, PeptideBean> peptidesList, boolean linkBtn, String btnName) {
        final String datasetName = handler.getDataset(datasetId).getName();

        String tupdatedExpName = datasetName;
        if (datasetName != null) {
            tupdatedExpName = datasetName.replaceAll("[-+.^:,/]", " ");
        }
        final String updatedDatasetName = tupdatedExpName;
        final ExportUnit peptidesExportLayout = new ExportUnit();
        Button exportButton = new Button(btnName);
        if (linkBtn) {
            exportButton.setStyleName(Reindeer.BUTTON_LINK);
        } else {
            exportButton.setStyleName(Reindeer.BUTTON_SMALL);
        }
        final VerticalLayout buttonBody = new VerticalLayout();
        buttonBody.addComponent(exportButton);
        final PopupView exportAllDatasetPeptidesPopup = new PopupView(null, peptidesExportLayout);
        buttonBody.addComponent(exportAllDatasetPeptidesPopup);
        buttonBody.setComponentAlignment(exportAllDatasetPeptidesPopup, Alignment.TOP_RIGHT);

        exportButton.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                exportAllDatasetPeptidesPopup.setPopupVisible(true);

            }
        });

        peptidesExportLayout.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                String exportGroupValue = peptidesExportLayout.getExportGroupValue();
                String typeGroupValue = peptidesExportLayout.getTypeGroupValue();

                Map<Integer, PeptideBean> vPeptidesList;
                if (typeGroupValue.equalsIgnoreCase("Validated Only")) {
                    vPeptidesList = getVpeptideList(peptidesList);
                } else {
                    vPeptidesList = new HashMap<Integer, PeptideBean>();
                    vPeptidesList.putAll(peptidesList);
                }
                PeptideTable pepTable = new PeptideTable(vPeptidesList, null, false, mainProtDesc);
                pepTable.setVisible(false);
                buttonBody.addComponent(pepTable);
                if (exportGroupValue.equalsIgnoreCase("csv")) {
                    csvExport = new CsvExport(pepTable, "CSF-PR " + updatedDatasetName + "  " + accession + "  Peptides");
                    csvExport.setReportTitle("CSF-PR / " + datasetName + " / " + accession + " / Peptides");
                    csvExport.setExportFileName("CSF-PR / " + datasetName + " / " + accession + " / Peptides.csv");
                    csvExport.setMimeType(CsvExport.CSV_MIME_TYPE);
                    csvExport.setDisplayTotals(false);
                    csvExport.export();
                } else {
                    excelExport = new ExcelExport(pepTable, "CSF-PR   " + updatedDatasetName + "   " + accession + "   Peptides");
                    excelExport.setReportTitle("CSF-PR / " + datasetName + " / " + accession + " / Peptides");
                    excelExport.setExportFileName("CSF-PR / " + datasetName + " / " + accession + " / Peptides.xls");
                    excelExport.setMimeType(ExcelExport.EXCEL_MIME_TYPE);
                    excelExport.setDisplayTotals(false);
                    excelExport.export();
                }
            }
        });

        return buttonBody;

    }

    public VerticalLayout exportProteinFractions(final String datasetName, final String accession, final Table fractionTable, boolean linkBtn, String btnName) {

        String tupdatedExpName = datasetName;
        if (datasetName != null) {
            tupdatedExpName = datasetName.replaceAll("[-+.^:,/]", " ");
        }
        final String updatedDatasetName = tupdatedExpName;
        final ExportUnit fractionsExportLayout = new ExportUnit();
        fractionsExportLayout.typeGroupOptionVisible(false);
        Button exportButton = new Button(btnName);
        if (linkBtn) {
            exportButton.setStyleName(Reindeer.BUTTON_LINK);
        } else {
            exportButton.setStyleName(Reindeer.BUTTON_SMALL);
        }
        final VerticalLayout buttonBody = new VerticalLayout();
        buttonBody.addComponent(exportButton);
        final PopupView exportAllDatasetPeptidesPopup = new PopupView(null, fractionsExportLayout);
        buttonBody.addComponent(exportAllDatasetPeptidesPopup);
        buttonBody.setComponentAlignment(exportAllDatasetPeptidesPopup, Alignment.TOP_RIGHT);

        exportButton.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                exportAllDatasetPeptidesPopup.setPopupVisible(true);

            }
        });

        fractionsExportLayout.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                String exportGroupValue = fractionsExportLayout.getExportGroupValue();

                if (exportGroupValue.equalsIgnoreCase("csv")) {
                    csvExport = new CsvExport(fractionTable, "CSF-PR   " + updatedDatasetName + "   " + accession + "   Fractions");
                    csvExport.setReportTitle("CSF-PR / " + datasetName + " / " + accession + " / Fractions");
                    csvExport.setExportFileName("CSF-PR / " + datasetName + " / " + accession + " / Fractions.csv");
                    csvExport.setMimeType(CsvExport.CSV_MIME_TYPE);
                    csvExport.setDisplayTotals(false);
                    csvExport.export();
                } else {
                    excelExport = new ExcelExport(fractionTable, "CSF-PR   " + updatedDatasetName + "   " + accession + "   Fractions");
                    excelExport.setReportTitle("CSF-PR / " + datasetName + " / " + accession + " / Fractions");
                    excelExport.setExportFileName("CSF-PR / " + datasetName + " / " + accession + " / Fractions.xls");
                    excelExport.setMimeType(ExcelExport.EXCEL_MIME_TYPE);
                    excelExport.setDisplayTotals(false);
                    excelExport.export();
                }
            }
        });

        return buttonBody;

    }

    public VerticalLayout exportSearchingResults(final Map<Integer, IdentificationProteinBean> fullDatasetProtList, boolean linkBtn, String btnName) {

        final ExportUnit peptidesExportLayout = new ExportUnit();
        Button exportButton = new Button(btnName);
        if (linkBtn) {
            exportButton.setStyleName(Reindeer.BUTTON_LINK);
        } else {
            exportButton.setStyleName(Reindeer.BUTTON_SMALL);
        }
        final VerticalLayout buttonBody = new VerticalLayout();
        buttonBody.addComponent(exportButton);
        final PopupView exportAllDatasetPeptidesPopup = new PopupView(null, peptidesExportLayout);
        buttonBody.addComponent(exportAllDatasetPeptidesPopup);
        buttonBody.setComponentAlignment(exportAllDatasetPeptidesPopup, Alignment.TOP_RIGHT);

        exportButton.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                exportAllDatasetPeptidesPopup.setPopupVisible(true);

            }
        });

        peptidesExportLayout.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                String exportGroupValue = peptidesExportLayout.getExportGroupValue();
                String typeGroupValue = peptidesExportLayout.getTypeGroupValue();

                Map<Integer, IdentificationProteinBean> tempFullExpProtList = null;
                if (typeGroupValue.equalsIgnoreCase("Validated Only")) {
                    tempFullExpProtList = getVprotList(fullDatasetProtList);
                } else if (typeGroupValue.equalsIgnoreCase("All")) {
                    tempFullExpProtList = fullDatasetProtList;
                }
                Map<Integer, DatasetDetailsBean> datasetDetailsList = handler.getDatasetDetailsList();
                IdentificationSearchResultsTable searcheResultsTable = new IdentificationSearchResultsTable(datasetDetailsList, tempFullExpProtList);
                searcheResultsTable.setVisible(false);
                buttonBody.addComponent(searcheResultsTable);
                if (exportGroupValue.equalsIgnoreCase("csv")) {
                    csvExport = new CsvExport(searcheResultsTable, "CSF-PR   Search Results");
                    csvExport.setReportTitle(" CSF-PR / Search Results");
                    csvExport.setExportFileName(" CSF-PR / Search Results.csv");
                    csvExport.setMimeType(CsvExport.CSV_MIME_TYPE);
                    csvExport.setDisplayTotals(false);
                    csvExport.export();
                } else {
                    excelExport = new ExcelExport(searcheResultsTable, "CSF-PR   Search Results");
                    excelExport.setReportTitle(" CSF-PR / Search Results");
                    excelExport.setExportFileName(" CSF-PR / Search Results.xls");
                    excelExport.setMimeType(ExcelExport.EXCEL_MIME_TYPE);
                    excelExport.setDisplayTotals(false);
                    excelExport.export();
                }

            }
        });

        return buttonBody;

    }

    private void exportCsv(Table t, String type, String datasetName, String updatedName, String accession, String validated) {
        if (type.equalsIgnoreCase("Proteins")) {
            csvExport = new CsvExport(t, "CSF-PR   " + updatedName + " Proteins");
            csvExport.setReportTitle("CSF-PR / " + datasetName + " / Proteins / ");
            csvExport.setExportFileName("CSF-PR - " + datasetName + " - " + validated + " - Proteins" + ".csv");
        } else if (type.equalsIgnoreCase("Peptides")) {
            csvExport = new CsvExport(t, "CSF-PR   " + updatedName + "   " + accession + "   Peptides   ");
            csvExport.setReportTitle("CSF-PR / " + datasetName + " / " + accession + " / Peptides / ");
            csvExport.setExportFileName("CSF-PR / " + datasetName + " / " + accession + " / Peptides / " + ".csv");
        } else if (type.equalsIgnoreCase("Fractions")) {
            csvExport = new CsvExport(t, ("CSF-PR   " + updatedName + "   " + accession + "   Fractions "));
            csvExport.setReportTitle("CSF-PR / " + datasetName + " / " + accession + " / Fractions ");
            csvExport.setExportFileName("CSF-PR / " + datasetName + " / " + accession + " / Fractions .csv");
        }

        csvExport.setMimeType(CsvExport.CSV_MIME_TYPE);
        csvExport.setDisplayTotals(false);
        csvExport.export();
    }

    private void exportXls(Table table, String type, String datasetName, String updatedName, String accession, String validated) {
        if (type.equalsIgnoreCase("Proteins")) {
            excelExport = new ExcelExport(table, ("CSF-PR  " + updatedName + "  Proteins  "));
            excelExport.setReportTitle("CSF-PR / " + datasetName + " / Proteins  ");
            excelExport.setExportFileName("CSF-PR - " + datasetName + " - " + validated + " - Proteins" + ".xls");
        } else if (type.equalsIgnoreCase("Peptides")) {
            excelExport = new ExcelExport(table, ("CSF-PR  " + updatedName + "  " + accession + "  Peptides  "));
            excelExport.setReportTitle("CSF-PR / " + datasetName + " / " + accession + " / Peptides");
            excelExport.setExportFileName("CSF-PR / " + datasetName + " / " + accession + " / Peptides " + ".csv");
        } else if (type.equalsIgnoreCase("Fractions")) {
            excelExport = new ExcelExport(table, ("CSF-PR  " + updatedName + "  " + accession + "  Fractions "));
            excelExport.setReportTitle("CSF-PR / " + datasetName + " / " + accession + " / Fractions ");
            csvExport.setExportFileName("CSF-PR / " + datasetName + " / " + accession + " / Fractions .xls");
        }
        excelExport.setMimeType(ExcelExport.EXCEL_MIME_TYPE);
        excelExport.setDisplayTotals(false);
        excelExport.export();
    }

    private Map<Integer, PeptideBean> getVpeptideList(Map<Integer, PeptideBean> peptideList) {
        Map<Integer, PeptideBean> vPeptideList = new HashMap<Integer, PeptideBean>();
        for (int key : peptideList.keySet()) {
            PeptideBean pb = peptideList.get(key);
            if (pb.getValidated() == 1) {
                vPeptideList.put(key, pb);
            }
        }
        return vPeptideList;

    }

    private Map<Integer, IdentificationProteinBean> getVprotList(Map<Integer, IdentificationProteinBean> protList) {
        Map<Integer, IdentificationProteinBean> vPeptideList = new HashMap<Integer, IdentificationProteinBean>();
        for (int key : protList.keySet()) {
            IdentificationProteinBean pb = protList.get(key);
            if (pb.isValidated()) {
                vPeptideList.put(key, pb);
            }
        }
        return vPeptideList;

    }
}
