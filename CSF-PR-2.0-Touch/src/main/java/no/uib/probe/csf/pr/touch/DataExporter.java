/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch;

import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Random;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import no.uib.probe.csf.pr.touch.logic.export.util.ProteinSequenceExportContainer;
import no.uib.probe.csf.pr.touch.logic.beans.HeatMapHeaderCellInformationBean;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.logic.export.util.LinechartToExport;
import no.uib.probe.csf.pr.touch.view.components.datasetfilters.DatasetPieChartFilter;
import no.uib.probe.csf.pr.touch.view.components.heatmapsubcomponents.HeatMapImgGenerator;
import no.uib.probe.csf.pr.touch.view.components.peptideviewsubcomponents.ProteinSequenceContainer;
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

    public byte[] exportStudiesProteinCoverageCharts(Set<QuantDiseaseGroupsComparison> selectedComparisonsList, String proteinKey, int custTrend) {

        try {

            File file = new File("linechartcoverageprotein" + (10000000.0 * Math.random()));
            if (file.exists()) {
                file.delete();
                System.out.println("file deleted");
            } else {
                file.createNewFile();
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

            LinechartToExport chartPanel = new LinechartToExport(height, width-50,selectedComparisonsList, proteinKey, custTrend);
            chartPanel.print(g2d);
//          
//            Font font = new Font("Helvetica, Arial", Font.PLAIN, 12);
//
//            JPanel container = new JPanel();
//            container.setLayout(null);
//            container.setVisible(true);
//            container.setBackground(Color.WHITE);
//            int width = img.getWidth() + 20;
//            int y = 10;
//            int x = 0;
//            container.setSize(new Dimension(width, height));
//
//            JLabel header = new JLabel("Overview Chart");
//            header.setFont(font);
//            header.setForeground(Color.GRAY);
//            header.setSize(width, 37);
//            header.setLocation(x, y);
//            header.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
//            header.setOpaque(false);
//            header.setVisible(true);
//            container.add(header);
//            x += 10;
//            y += 40;
//
//            JLabel label = new JLabel(new ImageIcon(img));
//            label.setLocation(x, y);
//            label.setSize(img.getWidth(), img.getHeight());
//            label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
//            label.setOpaque(true);
//            label.setVisible(true);
//            container.add(label);
//
//            y += img.getHeight();
//
//            JLabel peptidesOverviewHeaderLabel = new JLabel("Peptides Details (Sequence Coverage)");
//            peptidesOverviewHeaderLabel.setFont(font);
//            peptidesOverviewHeaderLabel.setForeground(Color.GRAY);
//            peptidesOverviewHeaderLabel.setSize(width, 37);
//            peptidesOverviewHeaderLabel.setLocation(0, y);
//            peptidesOverviewHeaderLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
//            peptidesOverviewHeaderLabel.setOpaque(false);
//            peptidesOverviewHeaderLabel.setVisible(true);
//            container.add(peptidesOverviewHeaderLabel);
//
//            y += 40;
//
//            document.newPage();
//            g2d = template.createGraphics(width, height);
//            x = 10;
//            y = 10;
//
//            for (ProteinSequenceContainer peptidesInfo : proteinSeqSet) {
//
//                ProteinSequenceExportContainer coverage = new ProteinSequenceExportContainer(peptidesInfo.getSequence(), peptidesInfo.getQuantPepSet(), width - 20, peptidesInfo.getDsID(), peptidesInfo.getProteinName());
//                coverage.setLocation(x, y);
//                y += coverage.getHeight() + 10;
//                coverage.print(g2d);
//                g2d.translate(0, y);
//
////                PeptidesSequenceContainer peptidesSequenceContainer = new PeptidesSequenceContainer(peptidesInfo, csfFolder.getParent(), pageWidth);
////                starty = peptidesSequenceContainer.getCurrentHeight() + 10;
////                if (starty <= availableSpace) {
////                    Image jpepImg = Image.getInstance(peptidesSequenceContainer.toImg(), null);
////                    jpepImg.setDpi(360, 360);
////                    jpepImg.scalePercent(100);
////                    jpepImg.setCompressionLevel(0);
////                    jpepImg.scalePercent(90);
////                    document.add(jpepImg);
////                } else {
////                   
////                    Image jpepImg = Image.getInstance(peptidesSequenceContainer.toImg(), null);
////                    jpepImg.setDpi(360, 360);
////                    jpepImg.scalePercent(90);
////                    jpepImg.setCompressionLevel(0);
////                    document.add(jpepImg);
////                }
//            }

            g2d.dispose();
            contentByte.addTemplate(template, 0, 0);

            /// peptides sequences
//            template = contentByte.createTemplate(width - 20, height);
//            g2d = template.createGraphics(width, height);
//            document.newPage();
//            g2d.translate(32, 0);
//
//            int availableSpace = height - 10 - 37;

//            for (ProteinInformationDataForExport peptidesInfo : peptidesSet) {
//                PeptidesSequenceContainer peptidesSequenceContainer = new PeptidesSequenceContainer(peptidesInfo, csfFolder.getParent(), pageWidth);
//                starty = peptidesSequenceContainer.getCurrentHeight() + 10;
//                if (starty <= availableSpace) {
//                    Image jpepImg = Image.getInstance(peptidesSequenceContainer.toImg(), null);
//                    jpepImg.setDpi(360, 360);
//                    jpepImg.scalePercent(100);
//                    jpepImg.setCompressionLevel(0);
//                    jpepImg.scalePercent(90);
//                    document.add(jpepImg);
//                } else {
//                    document.newPage();
//                    Image jpepImg = Image.getInstance(peptidesSequenceContainer.toImg(), null);
//                    jpepImg.setDpi(360, 360);
//                    jpepImg.scalePercent(90);
//                    jpepImg.setCompressionLevel(0);
//                    document.add(jpepImg);
//                }
//            }
//            g2d.dispose();
//            contentByte.addTemplate(template, 0, 0);
            document.close();

//            ChartPanel chart = new ChartPanel(lineChart);
//            chart.setSize(new Dimension(width - 100, 500));
//            chart.setBackground(Color.WHITE);
//            chart.setLocation(10, 50);
//            container.add(chart);
//            g2d.dispose();
//            contentByte.addTemplate(template, 0, 0);
//            document.close();
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
