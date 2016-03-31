
package probe.com.view.body.identificationlayoutcomponents;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import probe.com.handlers.CSFPRHandler;
import probe.com.model.beans.identification.IdentificationProteinBean;
import probe.com.model.beans.identification.StandardIdentificationFractionPlotProteinBean;
import probe.com.view.core.exporter.ExporterBtnsGenerator;
import probe.com.view.core.ShowLabel;

/**
 *
 * @author Yehia Farag
 * 
 * this class represents the identification proteins fractions layout
 */
public class IdentificationGelFractionsLayout extends VerticalLayout implements Serializable {

    private VerticalLayout mainLayout;
    private ShowLabel show;
    private boolean stat;
    private final VerticalLayout exportFracLayout = new VerticalLayout();

    /**
     *
     * @param handler
     * @param accession
     * @param molecularWeight
     * @param proteinFractionAvgList
     * @param standardProtPlotList
     * @param datasetName
     */
    public IdentificationGelFractionsLayout(final CSFPRHandler handler, final String accession, final double molecularWeight, Map<Integer, IdentificationProteinBean> proteinFractionAvgList, List<StandardIdentificationFractionPlotProteinBean> standardProtPlotList, final String datasetName) {
        this.setSpacing(false);
        this.setWidth("100%");
        this.setMargin(new MarginInfo(false, false, false, false));

        final HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setHeight("45px");
        headerLayout.setSpacing(true);

        final HorizontalLayout clickableheaderLayout = new HorizontalLayout();
        clickableheaderLayout.setHeight("45px");
        clickableheaderLayout.setSpacing(true);
        headerLayout.addComponent(clickableheaderLayout);
        headerLayout.setComponentAlignment(clickableheaderLayout, Alignment.BOTTOM_LEFT);

        show = new ShowLabel(true);
        clickableheaderLayout.addComponent(show);
        clickableheaderLayout.setComponentAlignment(show, Alignment.BOTTOM_LEFT);

        stat = true;

        Label fractionLabel = new Label("<h4 style='font-family:verdana;color:black;'>Fractions (Protein: " + accession + "  MW: " + molecularWeight + " kDa)</h4>");
        fractionLabel.setContentMode(ContentMode.HTML);
        fractionLabel.setHeight("45px");
        clickableheaderLayout.addComponent(fractionLabel);
        clickableheaderLayout.setComponentAlignment(fractionLabel, Alignment.TOP_RIGHT);
        this.addComponent(headerLayout);

        mainLayout = new VerticalLayout();
        this.addComponent(mainLayout);

        IdentificationFractionPlotLayout plotsLayout = new IdentificationFractionPlotLayout(proteinFractionAvgList, molecularWeight, standardProtPlotList);
        mainLayout.addComponent(plotsLayout);
        mainLayout.setComponentAlignment(plotsLayout, Alignment.MIDDLE_CENTER);

        HorizontalLayout lowerLayout = new HorizontalLayout();
        lowerLayout.setWidth("100%");
        lowerLayout.setHeight("25px");
        lowerLayout.setMargin((new MarginInfo(false, true, false, true)));
        lowerLayout.setSpacing(false);

        mainLayout.addComponent(lowerLayout);
        mainLayout.setComponentAlignment(lowerLayout, Alignment.TOP_CENTER);

        exportFracLayout.setWidth("300px");
        lowerLayout.addComponent(exportFracLayout);
        lowerLayout.setComponentAlignment(exportFracLayout, Alignment.MIDDLE_RIGHT);
        lowerLayout.setExpandRatio(exportFracLayout, 0.1f);
        final Table fractTable = getFractionTable(proteinFractionAvgList);
        fractTable.setVisible(false);
        this.addComponent(fractTable);

        ExporterBtnsGenerator dataExporter = new ExporterBtnsGenerator(handler);
        VerticalLayout proteinFractionsExportLayout = dataExporter.exportProteinFractions(datasetName, accession, fractTable, true, "Export fractions from selected dataset for ( " + accession + " )");
        proteinFractionsExportLayout.setDescription("Export fractions from ( " + datasetName + " ) dataset for ( " + accession + " )");
        exportFracLayout.addComponent(proteinFractionsExportLayout);
        exportFracLayout.setMargin(new MarginInfo(false, true, false, false));
        exportFracLayout.setComponentAlignment(proteinFractionsExportLayout, Alignment.BOTTOM_RIGHT);

        clickableheaderLayout.addLayoutClickListener(new com.vaadin.event.LayoutEvents.LayoutClickListener() {
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

    }

    
    /**
     * this is a hidden fraction table that used for exporting data
     * @param proteinFractionAvgList
     * return fractions information table
     *
     */
    private Table getFractionTable(Map<Integer, IdentificationProteinBean> proteinFractionAvgList) {
        Table table = new Table();
        table.setStyleName(Reindeer.TABLE_STRONG + " " + Reindeer.TABLE_BORDERLESS);
        table.setHeight("150px");
        table.setWidth("100%");
        table.setSelectable(true);
        table.setColumnReorderingAllowed(true);
        table.setColumnCollapsingAllowed(true);
        table.setImmediate(true); // react at once when something is selected
        table.addContainerProperty("Fraction Index", Integer.class, null, "Fraction Index", null, Align.CENTER);
        table.addContainerProperty("# Peptides ", Integer.class, null, "# Peptides ", null, Align.CENTER);
        table.addContainerProperty("# Spectra ", Integer.class, null, "# Spectra", null, Align.CENTER);
        table.addContainerProperty("Average Precursor Intensity", Double.class, null, "Average Precursor Intensity", null, com.vaadin.ui.Table.ALIGN_CENTER);
        /* Add a few items in the table. */
        int x = 0;
        for (int index : proteinFractionAvgList.keySet()) {
            IdentificationProteinBean pb = proteinFractionAvgList.get(index);
            table.addItem(new Object[]{index, pb.getNumberOfPeptidePerFraction(), pb.getNumberOfSpectraPerFraction(), pb.getAveragePrecursorIntensityPerFraction()}, x + 1);
            x++;
        }
        for (Object propertyId : table.getSortableContainerPropertyIds()) {
            table.setColumnExpandRatio(propertyId.toString(), 1.0f);
        }
        return table;
    }

}
