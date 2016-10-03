package no.uib.probe.csf.pr.touch.view.bigscreen;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import no.uib.probe.csf.pr.touch.Data_Handler;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFPR_Central_Manager;
import no.uib.probe.csf.pr.touch.view.LayoutViewManager;
import no.uib.probe.csf.pr.touch.view.bigscreen.quantlayoutcontainer.QuantDataLayoutContainer;
import no.uib.probe.csf.pr.touch.view.core.ScrollPanel;
import no.uib.probe.csf.pr.touch.view.bigscreen.welcomepagecontainer.WelcomeLayoutComponents;
import no.uib.probe.csf.pr.touch.view.core.BusyTask;
import no.uib.probe.csf.pr.touch.view.core.SlidePanel;
import no.uib.probe.csf.pr.touch.view.core.ZoomControler;

/**
 *
 * @author Yehia Farag
 *
 * this class represents the welcome page HTML template the class includes
 * buttons for starting the main functions
 *
 */
public class CSFApplicationContainer extends VerticalLayout {

    private final ScrollPanel welcomeLayoutPanel;
    private final SlidePanel quantLayoutPanel;
    private final LayoutViewManager View_Manager;
    private final Data_Handler Data_handler;
    private final ZoomControler zoomApp;

    public CSFApplicationContainer(int pageWidth, int pageHeight, String url, String dbName, String driver, String userName, String password, String filesURL) {
        this.setWidth(pageWidth, Unit.PIXELS);
        this.setHeight(pageHeight, Unit.PIXELS);
        this.setStyleName("whitelayout");
        this.setSpacing(false);

        BusyTask busyTask = new BusyTask();
        this.View_Manager = new LayoutViewManager(busyTask);
        this.Data_handler = new Data_Handler(url, dbName, driver, userName, password, filesURL);
        CSFPR_Central_Manager CSFPR_Central_Manager = new CSFPR_Central_Manager(busyTask);

        /////to remove as soon 
//        Set<QuantPeptide> unmappedPeptideSet = Data_handler.getUnmappedPeptideSet();
//        System.out.println("at peptide list size " + unmappedPeptideSet.size());
//        Table t2 = new Table();
//
//        t2.addContainerProperty(
//                "Index2", Integer.class, null, "Index", null, Table.Align.RIGHT);
//        t2.addContainerProperty(
//                "Uniprot Acc", String.class, null, "Uniprot Acc", null, Table.Align.LEFT);
//        t2.addContainerProperty(
//                "Disease Category2", String.class, null, "Uniprot Name", null, Table.Align.LEFT);
//        t2.addContainerProperty(
//                "Disease Comparisons2", String.class, null, "Publication Acc", null, Table.Align.LEFT);
//        t2.addContainerProperty(
//                "Author2", String.class, null, "Publication Name", null, Table.Align.LEFT);
//        t2.addContainerProperty(
//                "Peptide Sequence", String.class, null);
//        t2.addContainerProperty(
//                "Sequence Annotated", String.class, null);
//        t2.addContainerProperty(
//                "Peptide Modification", String.class, null);
//        t2.addContainerProperty(
//                "Modification Comment", String.class, null);
//
//        t2.addContainerProperty(
//                "Fold Change2", String.class, null, "Fold Change", null, Table.Align.LEFT);
//        t2.addContainerProperty(
//                "p-value2", String.class, null, "p-value", null, Table.Align.LEFT);
//        t2.addContainerProperty(
//                "p-value Threshold2", String.class, null, "p-value Threshold", null, Table.Align.LEFT);
//        t2.addContainerProperty(
//                "Statistical Comments2", String.class, null, "Statistical Comments", null, Table.Align.LEFT);
//
//        t2.addContainerProperty(
//                "Quantification Basis Comment2", String.class, null, "Quantification Basis Comment", null, Table.Align.LEFT);
//        t2.addContainerProperty(
//                "Peptide Charge2", String.class, null, "Peptide Charge", null, Table.Align.LEFT);
//        t2.addContainerProperty(
//                "ROC AUC2", String.class, null, "ROC AUC", null, Table.Align.LEFT);
//        t2.addContainerProperty(
//                "Additional Comments2", String.class, null, "Additional Comments", null, Table.Align.LEFT);
//
//        t2.setVisible(false);
//        this.addComponent(t2);
//        int index2 = 0;
//        for (QuantPeptide quantPeptide : unmappedPeptideSet) {
//
//            t2.addItem(new Object[]{(index2 + 1), quantPeptide.getUniprotAcc(), quantPeptide.getUniprotName(), quantPeptide.getPublicationAcc(), quantPeptide.getPublicationName(), quantPeptide.getPeptideSequence(), quantPeptide.getSequenceAnnotated(), quantPeptide.getPeptideModification(), quantPeptide.getModification_comment(), quantPeptide.getString_fc_value(), quantPeptide.getString_p_value(), quantPeptide.getPvalueSignificanceThreshold(), quantPeptide.getP_value_comments(), quantPeptide.getQuantBasisComment(), quantPeptide.getPeptideCharge() + "", quantPeptide.getRoc_auc() + "", quantPeptide.getAdditionalComments()}, index2++);
//        }
//        ExcelExport csvExport = new ExcelExport(t2, "un-mapped peptides");
//
//        csvExport.setReportTitle(
//                "CSF-PR / un-mapped peptides");
//        csvExport.setExportFileName(
//                "CSF-PR - un-mapped peptides.xls");
//        csvExport.setMimeType(ExcelExport.EXCEL_MIME_TYPE);
//
//        csvExport.setDisplayTotals(
//                false);
//        csvExport.setExcelFormatOfProperty(
//                "Index", "0");
//        csvExport.export();

        //--------------------------------------------------------------
        int mainlayoutWidth = pageWidth;
        int mainlayoutHeight = pageHeight;

        VerticalLayout bodyWrapper = new VerticalLayout();
        bodyWrapper.setHeightUndefined();
//        bodyWrapper.setStyleName("lightgraylayout");
        bodyWrapper.setWidth(100, Unit.PERCENTAGE);
        this.addComponent(bodyWrapper);

        WelcomeLayoutComponents welcomeContent = new WelcomeLayoutComponents(Data_handler, CSFPR_Central_Manager, View_Manager, mainlayoutWidth, mainlayoutHeight, Data_handler.getResourceOverviewInformation(), Data_handler.getPublicationList(), Data_handler.getQuantDatasetList());

        zoomApp = welcomeContent.getZoomApp();
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setHeight(pageHeight, Unit.PIXELS);
        mainLayout.setWidth(pageWidth, Unit.PIXELS);
        mainLayout.setStyleName("whitelayout");
        mainLayout.addComponent(welcomeContent);
        mainLayout.setComponentAlignment(welcomeContent, Alignment.TOP_CENTER);

        this.welcomeLayoutPanel = new ScrollPanel(mainLayout, welcomeContent.getMiniLayout(), 0, "welcomeview");
        View_Manager.registerComponent(welcomeLayoutPanel);
        bodyWrapper.addComponent(welcomeLayoutPanel);
        bodyWrapper.setComponentAlignment(welcomeLayoutPanel, Alignment.TOP_RIGHT);

        QuantDataLayoutContainer quantLayout = new QuantDataLayoutContainer(Data_handler, CSFPR_Central_Manager, mainlayoutWidth, mainlayoutHeight);

        quantLayoutPanel = new SlidePanel(quantLayout, null, 1, "quantview") {

            @Override
            public void setVisible(boolean visible) {
                super.setVisible(visible); //To change body of generated methods, choose Tools | Templates.
                if (this.getParent() != null) {
                    this.getParent().setVisible(visible);
                }
            }

        };
        quantLayoutPanel.setHeight(mainlayoutHeight, Unit.PIXELS);
        View_Manager.registerComponent(quantLayoutPanel);

        quantLayoutPanel.setShowNavigationBtn(false);

        welcomeContent.addMainZoomComponents(quantLayoutPanel);

        Panel quantPanelContaner = new Panel(quantLayoutPanel);// 
        quantLayoutPanel.setVisible(false);
        quantPanelContaner.setWidth(mainlayoutWidth, Unit.PIXELS);
        quantPanelContaner.setHeight(pageHeight, Unit.PIXELS);
        quantPanelContaner.setStyleName(ValoTheme.PANEL_BORDERLESS);

        bodyWrapper.addComponent(quantPanelContaner);
        bodyWrapper.setComponentAlignment(quantPanelContaner, Alignment.TOP_LEFT);

        View_Manager.viewLayout("welcomeview");

    }

    public ZoomControler getZoomApp() {
        return zoomApp;
    }

}
