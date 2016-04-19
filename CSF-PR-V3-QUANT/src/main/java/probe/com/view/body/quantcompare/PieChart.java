/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantcompare;

import com.vaadin.addon.tableexport.CsvExport;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.text.AttributedString;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.PieSectionEntity;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;

/**
 *
 * @author yfa041
 */
public class PieChart extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private final String[] labels;
    private final Double[] values;
    private final Map<String, Color> defaultKeyColorMap = new HashMap<String, Color>();
    private final Map<String, String> valuesMap = new HashMap<String, String>();
    private final DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
    private DecimalFormat df = null;
    private final Image chartImg = new Image();
    private final PopupView popupLayout;
    private final VerticalLayout popupBody = new VerticalLayout();
    private final String notfound;

    public PieChart(String title, double full, double found, final String notfound) {

        this.setWidth(200 + "px");
        this.setHeight(200 + "px");
        defaultKeyColorMap.put("Found", new Color(110, 177, 206));
        defaultKeyColorMap.put("Not found", new Color(219, 169, 1));
        otherSymbols.setGroupingSeparator('.');
        this.setStyleName("click");

        labels = new String[]{"Found", "Not found"};

        double foundPercent = ((found / full) * 100.0);
        df = new DecimalFormat("#.##", otherSymbols);
        valuesMap.put("Found", ((int) found) + " (" + df.format(foundPercent) + "%)");
        values = new Double[]{foundPercent, 100.0 - foundPercent};
        valuesMap.put("Not found", ((int) (full - found)) + " (" + df.format(100.0 - foundPercent) + "%)");

        String defaultImgURL = initPieChart(200, 200, title);
        chartImg.setSource(new ExternalResource(defaultImgURL));
        this.addComponent(chartImg);
        this.addLayoutClickListener(PieChart.this);

        popupLayout = new PopupView(null, popupBody);
        popupLayout.setHideOnMouseOut(false);
        popupBody.setWidth("300px");
        popupBody.setStyleName(Reindeer.LAYOUT_WHITE);
        popupBody.setHeightUndefined();
        this.addComponent(popupLayout);
        this.notfound = notfound.replace(" ", "").replace(",", "/n");

        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.setHeight("20px");

        Label header = new Label("<b>Not Found (New Proteins)</b>");
        header.setStyleName(Reindeer.LABEL_SMALL);
        topLayout.addComponent(header);
        header.setContentMode(ContentMode.HTML);

        VerticalLayout closeBtn = new VerticalLayout();
        closeBtn.setWidth("10px");
        closeBtn.setHeight("10px");
        closeBtn.setStyleName("closebtn");
        topLayout.addComponent(closeBtn);
        topLayout.setComponentAlignment(closeBtn, Alignment.TOP_RIGHT);
        closeBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                popupLayout.setPopupVisible(false);
            }
        });
        popupBody.addComponent(topLayout);
        popupBody.addComponent(textArea);
        textArea.setWidth("100%");
        textArea.setHeight("150px");
        textArea.setValue(this.notfound);
        textArea.setReadOnly(true);
        popupBody.setSpacing(true);

        HorizontalLayout bottomLayout = new HorizontalLayout();
        bottomLayout.setWidth("100%");
        bottomLayout.setHeight("40px");
        bottomLayout.setMargin(new MarginInfo(false, true, true, true));
        popupBody.addComponent(bottomLayout);

        Button exportTableBtn = new Button("");
        exportTableBtn.setHeight("24px");
        exportTableBtn.setWidth("24px");
        exportTableBtn.setPrimaryStyleName("exportxslbtn");
        exportTableBtn.setDescription("Export table data");
        exportTableBtn.addClickListener(new Button.ClickListener() {

            private Table table;

            @Override
            public void buttonClick(Button.ClickEvent event) {

                if (table == null) {
                    table = new Table();
                    table.addContainerProperty("Index", Integer.class, null, "", null, Table.Align.RIGHT);
                    table.addContainerProperty("Accession", String.class, Table.Align.CENTER);
                    table.setVisible(false);
                    addComponent(table);
                    int i = 1;
                    for (String str : notfound.replace(" ", "").replace(",", "\n").split("\n")) {
                        table.addItem(new Object[]{i, str}, i++);
                    }

                } 

                ExcelExport csvExport = new ExcelExport(table, "Not found protein accessions (New proteins)");
//                csvExport.setReportTitle("CSF-PR /  Not found protein accessions (New proteins) ");
                csvExport.setExportFileName("CSF-PR - Not found protein accessions" + ".xls");
                csvExport.setMimeType(CsvExport.EXCEL_MIME_TYPE);
                csvExport.setDisplayTotals(false);
                csvExport.setExcelFormatOfProperty("Index", "#0;[Red] #0");
                csvExport.export();

                

            }
        });

        bottomLayout.addComponent(exportTableBtn);
        bottomLayout.setComponentAlignment(exportTableBtn, Alignment.MIDDLE_RIGHT);

    }
    private final TextArea textArea = new TextArea();

    private String initPieChart(int width, int height, String title) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (int x = 0; x < labels.length; x++) {
            dataset.setValue(labels[x], values[x]);

        }
        PiePlot plot = new PiePlot(dataset);
        plot.setNoDataMessage("No data available");
        plot.setCircular(true);

        plot.setLabelGap(0);

        plot.setLabelFont(new Font("Verdana", Font.BOLD, 10));
        plot.setLabelGenerator(new PieSectionLabelGenerator() {

            @Override
            public String generateSectionLabel(PieDataset pd, Comparable cmprbl) {
                return valuesMap.get(cmprbl.toString());
            }

            @Override
            public AttributedString generateAttributedSectionLabel(PieDataset pd, Comparable cmprbl) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        plot.setSimpleLabels(true);

        plot.setLabelBackgroundPaint(null);
        plot.setLabelShadowPaint(null);
        plot.setLabelPaint(Color.WHITE);
        plot.setLabelOutlinePaint(null);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setInteriorGap(0);
        plot.setShadowPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        plot.setBaseSectionOutlinePaint(Color.WHITE);
        plot.setSectionOutlinesVisible(true);
        plot.setBaseSectionOutlineStroke(new BasicStroke(1.2f));
        plot.setInteriorGap(0.05);
        for (String label : labels) {
            plot.setSectionPaint(label, defaultKeyColorMap.get(label));
        }

        JFreeChart chart = new JFreeChart(plot);
//        chart.setTitle(new TextTitle(title, new Font("Verdana", Font.BOLD, 13)));
        chart.setBorderPaint(null);
        chart.setBackgroundPaint(null);
        chart.getLegend().setFrame(BlockBorder.NONE);
        chart.getLegend().setItemFont(new Font("Verdana", Font.PLAIN, 10));
        String imgUrl = saveToFile(chart, width, height);

        return imgUrl;

    }
    private final ChartRenderingInfo chartRenderingInfo = new ChartRenderingInfo();

    private String saveToFile(final JFreeChart chart, final double width, final double height) {
        byte imageData[];
        try {

            imageData = ChartUtilities.encodeAsPNG(chart.createBufferedImage((int) width, (int) height, chartRenderingInfo));
            String base64 = Base64.encodeBase64String(imageData);
            base64 = "data:image/png;base64," + base64;
            return base64;
        } catch (IOException e) {
            System.err.println("at error " + e.getMessage());
        }
        return "";
    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        int x = event.getRelativeX();
        int y = event.getRelativeY();
        ChartEntity entity = chartRenderingInfo.getEntityCollection().getEntity(x, y);
        if (entity instanceof PieSectionEntity) {
            PieSectionEntity pieEnt = (PieSectionEntity) entity;
            updatePopupAndShow(pieEnt.getSectionKey().toString());
        }

    }

    private void updatePopupAndShow(String section) {
        if (section.equalsIgnoreCase("Found")) {
            UI.getCurrent().setScrollTop(300);
        } else {

            popupLayout.setPopupVisible(true);
        }
    }

}
