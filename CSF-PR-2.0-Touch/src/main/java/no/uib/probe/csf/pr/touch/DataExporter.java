/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import javax.swing.JLabel;
import javax.swing.JPanel;
import no.uib.probe.csf.pr.touch.logic.beans.HeatMapHeaderCellInformationBean;
import no.uib.probe.csf.pr.touch.view.components.datasetfilters.DatasetPieChartFilter;
import no.uib.probe.csf.pr.touch.view.components.heatmapsubcomponents.HeatMapImgGenerator;
import org.apache.commons.io.IOUtils;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

/**
 *
 * @author Yehia Farag
 *
 * this class represents csf-pr exporting layer that is responsible for
 * exporting high resolution images and tables
 */
public class DataExporter implements Serializable {

    private Set<HeatMapHeaderCellInformationBean> heatmapRows;
    private Set<HeatMapHeaderCellInformationBean> heatmapColumns;
    private String[][] heatmapData;
    private final int width = 595;
    private final int height = 842;
    private final HeatMapImgGenerator heatmapGenerator = new HeatMapImgGenerator();

    /**
     * Export current datasets heat-map into image in pdf file
     */
    public byte[] exportHeatmap(boolean resetRowLabels, boolean restColumnLabels) {
        heatmapGenerator.generateHeatmap(heatmapRows, heatmapColumns, heatmapData, width, height, resetRowLabels, restColumnLabels, true);
        JPanel heatmapPanel = heatmapGenerator.getHeatmapPanelLayout();
        Font font = new Font("Helvetica Neue", Font.PLAIN, 12);

        try {
//            File csfFolder = new File(url);
//            csfFolder.mkdir();

            File file = new File("heatmap");
            if (file.exists()) {
                file.delete();
                System.out.println("file deleted");
            } else {
                file.createNewFile();
            }

            int bcw = heatmapPanel.getWidth();
            int bch = heatmapPanel.getHeight() + 50;

            Document document = new Document(new Rectangle(bcw + 60, bch + 100));
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));

            document.open();
            document.newPage();
            PdfContentByte contentByte = writer.getDirectContent();
            PdfTemplate template;
            template = contentByte.createTemplate(bcw + 60, bch + 100);
            document.newPage();
            Graphics2D g2d = template.createGraphics(bcw + 60, bch + 100);
            g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2d.translate(32, 10);
            JLabel headerII = new JLabel("Datasets Heatmap");
            headerII.setFont(font);
            headerII.setForeground(Color.red);
            headerII.paint(g2d);
            g2d.translate(0, 50);
            heatmapPanel.print(g2d);
            g2d.dispose();
            contentByte.addTemplate(template, 0, 0);
            document.close();
            byte fileData[] = IOUtils.toByteArray(new FileInputStream(file));
            return fileData;
        } catch (DocumentException exp) {
            System.err.println("at error 512 " + this.getClass().getName() + " -- " + exp.getMessage());
        } catch (IOException exp) {
            System.err.println("at error 515 " + this.getClass().getName() + " -- " + exp.getMessage());
        }
        return null;
    }

    public void setHeatmapRows(Set<HeatMapHeaderCellInformationBean> heatmapRows) {
        this.heatmapRows = heatmapRows;
    }

    public void setHeatmapColumns(Set<HeatMapHeaderCellInformationBean> heatmapColumns) {
        this.heatmapColumns = heatmapColumns;
    }

    public void setHeatmapData(String[][] heatmapData) {
        this.heatmapData = heatmapData;
    }

    public byte[] exportPieCharts(Collection<DatasetPieChartFilter> pieChartsSet) {
        Font font = new Font("Helvetica, Arial", Font.PLAIN, 12);

        try {

            JPanel container = new JPanel();
            container.setLayout(null);
            container.setVisible(true);
            container.setBackground(Color.WHITE);
            container.setSize(new Dimension(height, width));

            File file = new File("piecharts");
            if (file.exists()) {
                file.delete();
                System.out.println("file deleted");
            } else {
                file.createNewFile();
            }

            JLabel header = new JLabel("Datasets Heatmap");
            header.setFont(font);
            header.setForeground(Color.GRAY);
            header.setSize(height, 37);
            header.setLocation(0, 10);
            header.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            header.setOpaque(false);
            header.setVisible(true);
            container.add(header);

            int x = 20;
            int y = 50;
            int count = 0;

            for (DatasetPieChartFilter chratWrapper : pieChartsSet) {
                ChartPanel chart = new ChartPanel(chratWrapper.getChart());
                chart.setSize(new Dimension((int) chratWrapper.getWidth(), (int) chratWrapper.getHeight()));
                chart.setBackground(Color.WHITE);
                chart.setLocation(x, y);
                container.add(chart);
                x += 270;
                if (count == 2) {
                    x = 20;
                    y += 270;
                }
                count++;

            }
            Document document = new Document(new Rectangle(height, width));
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));

            document.open();
            document.newPage();
            PdfContentByte contentByte = writer.getDirectContent();
            PdfTemplate template;
            template = contentByte.createTemplate(height, width);
            document.newPage();
            Graphics2D g2d = template.createGraphics(height, width);
            g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
//            g2d.translate(32, 10);

            container.print(g2d);
            g2d.dispose();
            contentByte.addTemplate(template, 0, 0);
            document.close();
            byte fileData[] = IOUtils.toByteArray(new FileInputStream(file));
            return fileData;
        } catch (DocumentException exp) {
            System.err.println("at error 512 " + this.getClass().getName() + " -- " + exp.getMessage());
        } catch (IOException exp) {
            System.err.println("at error 515 " + this.getClass().getName() + " -- " + exp.getMessage());
        }

        return null;
    }

    public byte[] exportBubbleChart(JFreeChart bubbleChart) {
        Font font = new Font("Helvetica, Arial", Font.PLAIN, 12);

        try {

            JPanel container = new JPanel();
            container.setLayout(null);
            container.setVisible(true);
            container.setBackground(Color.WHITE);
            container.setSize(new Dimension(height, width));

            File file = new File("bubblechart");
            if (file.exists()) {
                file.delete();
                System.out.println("file deleted");
            } else {
                file.createNewFile();
            }

            JLabel header = new JLabel("Overview Chart");
            header.setFont(font);
            header.setForeground(Color.GRAY);
            header.setSize(height, 37);
            header.setLocation(0, 10);
            header.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            header.setOpaque(false);
            header.setVisible(true);
            container.add(header);

         
            ChartPanel chart = new ChartPanel(bubbleChart);
            chart.setSize(new Dimension(height - 20, width - 100));
            chart.setBackground(Color.WHITE);
            chart.setLocation(10, 50);
            container.add(chart);
            

            Document document = new Document(new Rectangle(height, width));
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));

            document.open();
            document.newPage();
            PdfContentByte contentByte = writer.getDirectContent();
            PdfTemplate template;
            template = contentByte.createTemplate(height, width);
            document.newPage();
            Graphics2D g2d = template.createGraphics(height, width);
            g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
//            g2d.translate(32, 10);

            container.print(g2d);
            g2d.dispose();
            contentByte.addTemplate(template, 0, 0);
            document.close();
            byte fileData[] = IOUtils.toByteArray(new FileInputStream(file));
            return fileData;
        } catch (DocumentException exp) {
            System.err.println("at error 512 " + this.getClass().getName() + " -- " + exp.getMessage());
        } catch (IOException exp) {
            System.err.println("at error 515 " + this.getClass().getName() + " -- " + exp.getMessage());
        }

        return null;
    }

}
