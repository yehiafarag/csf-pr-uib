package probe.com.model.util;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.DefaultFontMapper;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import javax.swing.JLabel;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.ui.RectangleEdge;
import org.mozilla.javascript.tools.debugger.Dim;
import probe.com.model.beans.identification.IdentificationPeptideBean;
import probe.com.model.util.vaadintoimageutil.peptideslayout.PeptidesSequenceContainer;
import probe.com.model.util.vaadintoimageutil.peptideslayout.ProteinInformationDataForExport;

/**
 *
 * @author Yehia Farag represent the data exporter class
 */
public class FileExporter implements Serializable {

    /**
     * this function to be use for csv peptides exporting with large datasets
     *
     * @param allPeptides peptides to be exported
     * @param datasetName
     * @param dataType validated/all
     * @param path for the file csf-pr file system
     */
    public void expotIdentificationPeptidesToCSV(Map<Integer, IdentificationPeptideBean> allPeptides, String datasetName, String dataType, String path) {
        File csvText = new File(path, "CSF-PR - " + datasetName + " - All - " + dataType + " - Peptides.csv");// "CSF-PR - " + datasetName + " - All - " + dataType + "- Peptides" + ".csv");
        PrintWriter out1 = null;
        try {
            if (csvText.exists()) {
                return;
            }
            csvText.createNewFile();
            FileWriter outFile = new FileWriter(csvText, true);
            out1 = new PrintWriter(outFile);
            String title = "CSF-PR / " + datasetName + " / " + dataType + " Peptides";
            out1.append(title);
            out1.append('\n');
            String header = " ,PI,Peptide Protein(s),Peptide Prot. Descrip.,Sequence,AA Before,AA After,Start,End,#Spectra,Other Protein(s),Other Prot Descrip.,Variable Modification,Location Confidence,Precursor Charge(s),Enzymatic,Sequence Annotated,Glycopeptide,Glyco Position(s),Confidence,Validated";
            String[] headerArr = header.split(",");
            int index = 0;
            for (String str : headerArr) {
                out1.append(str);
                if (index != headerArr.length - 1) {
                    out1.append(',');
                }
                index++;

            }
            out1.append('\n');

            index = 0;
            for (IdentificationPeptideBean pb : allPeptides.values()) {
                out1.append("" + index++);
                out1.append(',');
                out1.append(pb.getProteinInference());
                out1.append(',');
                out1.append(pb.getPeptideProteins().replaceAll(",", ";"));
                out1.append(',');
                out1.append(pb.getPeptideProteinsDescriptions().replaceAll(",", ";"));
                out1.append(',');
                out1.append(pb.getSequence());
                out1.append(',');
                out1.append(pb.getAaBefore());
                out1.append(',');
                out1.append(pb.getAaAfter());
                out1.append(',');
                out1.append(pb.getPeptideStart());
                out1.append(',');
                out1.append(pb.getPeptideEnd());
                out1.append(',');
                out1.append("" + pb.getNumberOfValidatedSpectra());
                out1.append(',');
                out1.append(pb.getOtherProteins().replaceAll(",", ";"));
                out1.append(',');
                out1.append(pb.getOtherProteinDescriptions().replaceAll(",", ";"));
                out1.append(',');
                out1.append(pb.getVariableModification().replaceAll(",", ";"));
                out1.append(',');
                out1.append(pb.getLocationConfidence().replaceAll(",", ";"));
                out1.append(',');
                out1.append(pb.getPrecursorCharges().replaceAll(",", ";"));
                out1.append(',');
                out1.append("" + pb.isEnzymatic());
                out1.append(',');
                out1.append(pb.getSequenceTagged());
                out1.append(',');
                out1.append(("" + pb.isDeamidationAndGlycopattern()).toUpperCase());
                out1.append(',');
                if (pb.getGlycopatternPositions() != null) {
                    out1.append("" + pb.getGlycopatternPositions());
                } else {
                    out1.append("");
                }
                out1.append(',');
                int x = (int) pb.getConfidence();
                out1.append("" + x);
                out1.append(',');
                if (pb.getValidated() == 1.0) {
                    out1.append("true");
                } else {
                    out1.append("false");
                }
                out1.append('\n');

            }

            out1.flush();
            out1.close();
        } catch (Exception exp) {
            System.err.println(exp.getLocalizedMessage());
            if (out1 != null) {
                out1.flush();
                out1.close();
            }
        }
    }

    /**
     * this function to be use for xls peptides exporting with large datasets
     *
     * @param allPeptides peptides to be exported
     * @param datasetName
     * @param dataType validated/all
     * @param path for the file csf-pr file system
     */
    public void expotIdentificationPeptidesToXLS(Map<Integer, IdentificationPeptideBean> allPeptides, String datasetName, String dataType, String path) {
        File xlsText = new File(path, "CSF-PR-" + datasetName + "-All-" + dataType + "-Peptides.xls");// "CSF-PR - " + datasetName + " - All - " + dataType + "- Peptides" + ".csv");

        try {
            if (xlsText.exists()) {
                return;
            }

            FileOutputStream fileOut = new FileOutputStream(xlsText);
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet();
            //title
            String title = "CSF-PR / " + datasetName + " / " + dataType + " Peptides";
            HSSFRow titleRow = worksheet.createRow(0);
            HSSFCell cellA1 = titleRow.createCell(0);
            cellA1.setCellValue(title);
            //header
            String header = " ,PI,Peptide Protein(s),Peptide Prot. Descrip.,Sequence,AA Before,AA After,Start,End,#Spectra,Other Protein(s),Other Prot Descrip.,Variable Modification,Location Confidence,Precursor Charge(s),Enzymatic,Sequence Annotated,Glycopeptide,Glyco Position(s),Confidence,Validated";
            String[] headerArr = header.split(",");
            HSSFRow headerRow = worksheet.createRow(1);
            int index = 0;
            for (String str : headerArr) {
                HSSFCell cell = headerRow.createCell(index);
                cell.setCellValue(str);
                index++;

            }
            //data

            index = 0;
            int counter = 0;
            for (IdentificationPeptideBean pb : allPeptides.values()) {
                if (index > 65533) {
                    worksheet = workbook.createSheet();
                    index = 0;
                    HSSFRow headerRow2 = worksheet.createRow(0);
                    for (String str : headerArr) {
                        HSSFCell cell = headerRow2.createCell(index);
                        cell.setCellValue(str);
                        index++;
                    }
                    index = -1;
                }
                HSSFRow peptideRow = worksheet.createRow((int) index + 2);
                HSSFCell cell0 = peptideRow.createCell(counter++);
                cell0.setCellValue(index);
                HSSFCell cell1 = peptideRow.createCell(1);
                cell1.setCellValue(pb.getProteinInference());
                HSSFCell cell2 = peptideRow.createCell(2);
                cell2.setCellValue(pb.getPeptideProteins().replaceAll(",", ";"));
                HSSFCell cell3 = peptideRow.createCell(3);
                cell3.setCellValue(pb.getPeptideProteinsDescriptions().replaceAll(",", ";"));
                HSSFCell cell4 = peptideRow.createCell(4);
                cell4.setCellValue(pb.getSequence());
                HSSFCell cell5 = peptideRow.createCell(5);
                cell5.setCellValue(pb.getAaBefore());
                HSSFCell cell6 = peptideRow.createCell(6);
                cell6.setCellValue(pb.getAaAfter());
                HSSFCell cell7 = peptideRow.createCell(7);
                cell7.setCellValue(pb.getPeptideStart());
                HSSFCell cell8 = peptideRow.createCell(8);
                cell8.setCellValue(pb.getPeptideEnd());
                HSSFCell cell9 = peptideRow.createCell(9);
                cell9.setCellValue(pb.getNumberOfValidatedSpectra());
                HSSFCell cell10 = peptideRow.createCell(10);
                cell10.setCellValue(pb.getOtherProteins().replaceAll(",", ";"));
                HSSFCell cell11 = peptideRow.createCell(11);
                cell11.setCellValue(pb.getOtherProteinDescriptions().replaceAll(",", ";"));
                HSSFCell cell12 = peptideRow.createCell(12);
                cell12.setCellValue(pb.getVariableModification().replaceAll(",", ";"));
                HSSFCell cell13 = peptideRow.createCell(13);
                cell13.setCellValue(pb.getLocationConfidence().replaceAll(",", ";"));
                HSSFCell cell14 = peptideRow.createCell(14);
                cell14.setCellValue(pb.getPrecursorCharges().replaceAll(",", ";"));
                HSSFCell cell15 = peptideRow.createCell(15);
                cell15.setCellValue(pb.isEnzymatic());
                HSSFCell cell16 = peptideRow.createCell(16);
                cell16.setCellValue(pb.getSequenceTagged());
                HSSFCell cell17 = peptideRow.createCell(17);
                cell17.setCellValue(pb.isDeamidationAndGlycopattern());
                HSSFCell cell18 = peptideRow.createCell(18);
                if (pb.getGlycopatternPositions() != null) {
                    cell18.setCellValue(pb.getGlycopatternPositions());
                } else {
                    cell18.setCellValue("");
                }
                int x = (int) pb.getConfidence();
                HSSFCell cell19 = peptideRow.createCell(19);
                cell19.setCellValue(x);

                HSSFCell cell20 = peptideRow.createCell(20);

                if (pb.getValidated() == 1.0) {
                    cell20.setCellValue("TRUE");
                } else {
                    cell20.setCellValue("FALSE");
                }
                index++;

            }

            workbook.write(fileOut);
//            fileOut.flush();
            fileOut.close();
        } catch (Exception exp) {
            exp.printStackTrace();
            System.err.println(exp.getMessage());
        }

    }

    public byte[] exportStudiesInformationPieCharts(Set<JFreeChart> chartsSet, String fileName, String url, String title) {
        int width = 595;
        int height = 842;
        int startx = 32;
        int starty = 47;

        try {

            File csfFolder = new File(url);
            csfFolder.mkdir();

            File file = new File(url + "/" + fileName);
            if (file.exists()) {
                file.delete();
            }
            Document document = new Document();

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));

            document.open();
            document.newPage();

            PdfContentByte contentByte = writer.getDirectContent();
            PdfTemplate template;

            Graphics2D g2d;

            int counter = 0;
            template = contentByte.createTemplate(width, height);
            g2d = template.createGraphics(width, height, new DefaultFontMapper(), true, 1);
            g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

            JLabel headerLabel = new JLabel();
            headerLabel.setBackground(new java.awt.Color(255, 255, 255));
            headerLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            headerLabel.setText("Datasets");
            headerLabel.setSize(width, 37);
            headerLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
            headerLabel.paint(g2d);

            Font font = new Font("Verdana", Font.PLAIN, 11);
            Font subfont = new Font("Verdana", Font.PLAIN, 10);

            Font resetTitleFont = new Font("Verdana", Font.BOLD, 13);
            Font resetFont = new Font("Verdana", Font.PLAIN, 12);

            for (JFreeChart chart : chartsSet) {
                chart.getTitle().setFont(font);
                chart.getLegend().setItemFont(subfont);
                Rectangle2D rect2d = new Rectangle2D.Double(startx, starty, 249, 250);
                chart.draw(g2d, rect2d);

                startx += 249 + 33;
                if (counter == 1 || counter == 3) {
                    startx = 32;
                    starty += 15 + 250;
                }
                chart.getTitle().setFont(resetTitleFont);
                chart.getLegend().setItemFont(resetFont);
                counter++;

            }

            g2d.dispose();
            contentByte.addTemplate(template, 0, 0);

            document.close();
            byte fileData[] = IOUtils.toByteArray(new FileInputStream(file));
            return fileData;
        } catch (DocumentException exp) {
            System.err.println("at error 444 " + this.getClass().getName() + "  " + exp.getMessage());
        } catch (IOException exp) {
            System.err.println("at error 446 " + this.getClass().getName() + "  " + exp.getMessage());
        }
        return null;

    }

    public byte[] exportBubbleChartAsPdf(JFreeChart chart, String fileName, String url, String title, int w, int h) {

        Font font = new Font("Verdana", Font.PLAIN, 12);

        try {
            File csfFolder = new File(url);
            csfFolder.mkdir();

            File file = new File(url + "/" + fileName);
            if (file.exists()) {
                file.delete();
                System.out.println("file deleted");
            }

            int bcw = Integer.valueOf(chart.getXYPlot().getNoDataMessage().split(",")[0]);
            int bch = Integer.valueOf(chart.getXYPlot().getNoDataMessage().split(",")[1]);

            Document document = new Document(new Rectangle(bcw + 60, bch + 100));
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));

            document.open();
            document.newPage();
            PdfContentByte contentByte = writer.getDirectContent();
            PdfTemplate template;
            template = contentByte.createTemplate(bcw + 60, bch + 100);
            document.newPage();
            Graphics2D g2d = template.createGraphics(bcw + 60, bch + 100, new DefaultFontMapper());
            g2d.translate(32, 10);
            JLabel headerII = generateTitleLabel("Overview", bcw);
            headerII.paint(g2d);
            g2d.translate(0, 50);
            chart.getPlot().setNotify(true);
            Rectangle2D rect2d = new Rectangle2D.Double(0, 0, bcw, bch);
            ((XYPlot) chart.getPlot()).getDomainAxis().setTickLabelFont(font);
            chart.draw(g2d, rect2d);
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

    public byte[] exportProteinsInfoCharts(Set<JFreeChart> chartsSet, String fileName, String url, String title, Set<ProteinInformationDataForExport> peptidesSet, int w, int h) {
        int width = w + 60;//595;
        int height = 842;
        int startx = 20;

        try {

            File csfFolder = new File(url);
            csfFolder.mkdir();

            File file = new File(url + "/" + fileName);
            if (file.exists()) {
                file.delete();
                System.out.println("file deleted");
            }
            Document document = new Document(new Rectangle(width, height));

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));

            document.open();
            document.newPage();

            PdfContentByte contentByte = writer.getDirectContent();
            PdfTemplate template;

            Graphics2D g2d;
            int starty = 200;
            int counter = 0;

            template = contentByte.createTemplate(width, height);
            g2d = template.createGraphics(width, height, new DefaultFontMapper(), true, 1);
            Rectangle2D rect2d;
            boolean newpage = false;
            Phrase header = new Phrase(40, "Protein Overview", new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18f, com.itextpdf.text.Font.NORMAL, BaseColor.GRAY));// new Font("Verdana", Font.PLAIN, 14));                

            document.add(header);

            Phrase headerI;
            for (JFreeChart chart : chartsSet) {
                if (newpage) {
                    newpage = false;
                    document.newPage();
                }

                if (counter == 0) {

                    TextTitle textTitle = new TextTitle(title, new Font("Verdana", Font.PLAIN, 12));
                    textTitle.setMargin(10, 0, 20, 0);
                    textTitle.setPaint(Color.DARK_GRAY);
                    chart.setTitle(textTitle);
                    rect2d = new Rectangle2D.Double(startx, starty, w, h);
                    chart.draw(g2d, rect2d);
                    g2d.dispose();
                    contentByte.addTemplate(template, 0, 0);
                    template = contentByte.createTemplate(width, height);
                    g2d = template.createGraphics(width, height, new DefaultFontMapper(), true, 1);
                    document.newPage();
                    headerI = new Phrase(40, "Datasets Details", new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18f, com.itextpdf.text.Font.NORMAL, BaseColor.GRAY));// new Font("Verdana", Font.PLAIN, 14));                
                    document.add(headerI);

//                    starty += h + 20;
                    starty = 120;
                    counter++;
                    continue;

                } else {
                    rect2d = new Rectangle2D.Double(30, starty, w, 150);
                    starty += 170;

                }
                chart.draw(g2d, rect2d);
                counter++;
                if (starty > 600) {
                    g2d.dispose();
                    contentByte.addTemplate(template, 0, 0);
                    newpage = true;
                    template = contentByte.createTemplate(width, height);
                    g2d = template.createGraphics(width, height, new DefaultFontMapper(), true, 1);
                    starty = 70;

                }

            }
            g2d.dispose();
            contentByte.addTemplate(template, 0, 0);

            //start sequence coverage       
            document.newPage();
            Phrase headerII = new Phrase(160, "Peptides Details (Sequence Coverage)", new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18f, com.itextpdf.text.Font.NORMAL, BaseColor.GRAY));// new Font("Verdana", Font.PLAIN, 14));    
            document.add(headerII);

            int availableSpace = height - 10 - 37;
            for (ProteinInformationDataForExport peptidesInfo : peptidesSet) {

                PeptidesSequenceContainer peptidesSequenceContainer = new PeptidesSequenceContainer(peptidesInfo, csfFolder.getParent(), width);
                starty = peptidesSequenceContainer.getCurrentHeight() + 10;
                if (starty <= availableSpace) {
                    Image jpepImg = Image.getInstance(peptidesSequenceContainer.toImg(), null);
                    jpepImg.setDpi(360, 360);
                    jpepImg.scalePercent(100);
                    jpepImg.setCompressionLevel(0);
                    jpepImg.scalePercent(90);
                    document.add(jpepImg);
//                   

                } else {
//                  
                    document.newPage();
                    Image jpepImg = Image.getInstance(peptidesSequenceContainer.toImg(), null);
                    jpepImg.setDpi(360, 360);
                    jpepImg.scalePercent(90);
                    jpepImg.setCompressionLevel(0);
                    document.add(jpepImg);
                }

            }

            document.close();
            byte fileData[] = IOUtils.toByteArray(new FileInputStream(file));
            return fileData;
        } catch (DocumentException exp) {
            System.err.println("at error 367 " + this.getClass().getName() + " -- " + exp.getMessage());
        } catch (IOException exp) {
            System.err.println("at error 369 " + this.getClass().getName() + " -- " + exp.getMessage());
        }

//          
        return null;

    }

    public byte[] exportfullReportAsZip(Map<String, Set<JFreeChart>> chartsMap, String fileName, String url, String title, Set<ProteinInformationDataForExport> peptidesSet) {
        int width = 595;
        int height = 842;
        int startx;
        int starty = 0;

        try {

            File csfFolder = new File(url);
            if (!csfFolder.exists()) {
                csfFolder.mkdir();
            }

            File file = new File(csfFolder, fileName);
            if (file.exists()) {
                file.delete();
                System.out.println("file deleted");
            }
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            document.newPage();
            PdfContentByte contentByte = writer.getDirectContent();

            PdfTemplate template;
            Graphics2D g2d;
            template = contentByte.createTemplate(width, height);
            g2d = template.createGraphics(width, height, true, 1);

            JLabel reportTiltleLabel = new JLabel();
            reportTiltleLabel.setBackground(new java.awt.Color(255, 255, 255));
            reportTiltleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            reportTiltleLabel.setText("CSF-PR v2.0 Report");
            reportTiltleLabel.setSize(width, 35);
            reportTiltleLabel.setFont(new Font("Verdana", Font.BOLD, 16));
            reportTiltleLabel.paint(g2d);
            starty += 35;

            Phrase headerI = new Phrase(70, "Datasets Overview", new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 13f, com.itextpdf.text.Font.NORMAL, BaseColor.GRAY));// new Font("Verdana", Font.PLAIN, 14));                
            document.add(headerI);
            g2d.translate(32, starty);
            startx = 0;

            Font font = new Font("Verdana", Font.PLAIN, 11);
            Font subfont = new Font("Verdana", Font.PLAIN, 10);
            Font resetTitleFont = new Font("Verdana", Font.BOLD, 13);
            Font resetFont = new Font("Verdana", Font.PLAIN, 12);
            int counter = 0;
            for (JFreeChart chart : chartsMap.get("StudiesPieCharts")) {
                chart.getTitle().setFont(font);
                chart.getLegend().setItemFont(subfont);
                Rectangle2D rect2d = new Rectangle2D.Double(startx, starty, 249, 239);
                chart.draw(g2d, rect2d);

                startx += 249 + 33;
                if (counter == 1 || counter == 3) {
                    startx = 0;
                    starty += 15 + 239;
                }
                chart.getTitle().setFont(resetTitleFont);
                chart.getLegend().setItemFont(resetFont);
                counter++;

            }

            g2d.dispose();
            contentByte.addTemplate(template, 0, 0);
            for (JFreeChart chart : chartsMap.get("proteinsOverviewBubbleChart")) {
                starty = 0;

                int bcw = Integer.valueOf(chart.getXYPlot().getNoDataMessage().split(",")[0]);
                int bch = Integer.valueOf(chart.getXYPlot().getNoDataMessage().split(",")[1]);

                document.setPageSize(new Rectangle(bcw + 60, bch + 100));
                template = contentByte.createTemplate(bcw + 60, bch + 100);
                document.newPage();
                g2d = template.createGraphics(bcw + 60, bch + 100, new DefaultFontMapper());
                g2d.translate(32, 10);
                JLabel headerII = generateTitleLabel("Overview", bcw);
                headerII.paint(g2d);
                starty += 50;
                g2d.translate(0, starty);
                chart.getPlot().setNotify(true);
                Rectangle2D rect2d = new Rectangle2D.Double(0, 0, bcw, bch);
                ((XYPlot) chart.getPlot()).getDomainAxis().setTickLabelFont(font);
                chart.draw(g2d, rect2d);
                g2d.dispose();
                contentByte.addTemplate(template, 0, 0);
            }

            //selected protein overview line chart chart
            startx = 0;
            starty = 10;
            Rectangle2D rect2d;
            boolean newpage = false;
            counter = 0;
            int pageWidth = 505;
            for (JFreeChart chart : chartsMap.get("proteinInformationCharts")) {
                if (newpage) {
                    newpage = false;
                    document.setPageSize(new Rectangle(pageWidth, height));
                    document.newPage();
                    g2d.translate(32, 10);
                }

                if (counter == 0) {
                    int bcw = Integer.valueOf(chart.getXYPlot().getNoDataMessage().split(",")[0]);
                    int bch = Integer.valueOf(chart.getXYPlot().getNoDataMessage().split(",")[1]);
                    final JLabel proteinInformationOverviewLabel = generateTitleLabel("Selected Protein Information - " + title.replace("(", "_;_").split("_;_")[0] + " ", bcw);
                    document.setPageSize(new Rectangle(bcw + 60, bch + 100));
                    document.newPage();
                    template = contentByte.createTemplate(bcw + 60, bch + 100);
                    g2d = template.createGraphics(bcw + 60, bch + 100, new DefaultFontMapper());
                    g2d.translate(32, starty);
                    starty += 50;
                    proteinInformationOverviewLabel.paint(g2d);
                    rect2d = new Rectangle2D.Double(startx, starty, bcw, bch);
                    starty += bch + 20;
                    ((XYPlot) chart.getPlot()).getDomainAxis().setTickLabelFont(font);
                    chart.draw(g2d, rect2d);
                    g2d.dispose();
                    contentByte.addTemplate(template, 0, 0);
                    newpage = false;
                    pageWidth = bcw + 60;
                    template = contentByte.createTemplate(pageWidth, height);
                    g2d = template.createGraphics(pageWidth, height, new DefaultFontMapper(), true, 1);
                    document.setPageSize(new Rectangle(pageWidth, height));
                    document.newPage();
                    g2d.translate(32, 10);
                    final JLabel datasetformationOverviewLabel = generateTitleLabel("Datasets Details", bcw);
                    datasetformationOverviewLabel.paint(g2d);
                    starty = 30;
                    startx = 32;
                    g2d.translate(0, starty);
                    counter++;
                    continue;

                } else {
                    rect2d = new Rectangle2D.Double(0, starty, pageWidth - 60, 150);
                    starty += 170;
                    chart.draw(g2d, rect2d);
                }

                counter++;

                if (starty > 600) {
                    g2d.dispose();
                    contentByte.addTemplate(template, 0, 0);
                    newpage = true;
                    template = contentByte.createTemplate(pageWidth, height);
                    g2d = template.createGraphics(pageWidth, height, new DefaultFontMapper());
                    starty = 30;
                    startx = 32;

                }

            }

            g2d.dispose();
            contentByte.addTemplate(template, 0, 0);

            /// peptides sequences
            template = contentByte.createTemplate(pageWidth, height);
            g2d = template.createGraphics(pageWidth, height, new DefaultFontMapper());
            document.newPage();
            g2d.translate(32, 0);

            JLabel peptidesOverviewHeaderLabel = generateTitleLabel("Peptides Details (Sequence Coverage)", pageWidth);
            peptidesOverviewHeaderLabel.paint(g2d);
            g2d.translate(0, 32);
            int availableSpace = height - 10 - 37;

            for (ProteinInformationDataForExport peptidesInfo : peptidesSet) {
                PeptidesSequenceContainer peptidesSequenceContainer = new PeptidesSequenceContainer(peptidesInfo, csfFolder.getParent(), pageWidth);
                starty = peptidesSequenceContainer.getCurrentHeight() + 10;
                if (starty <= availableSpace) {
                    Image jpepImg = Image.getInstance(peptidesSequenceContainer.toImg(), null);
                    jpepImg.setDpi(360, 360);
                    jpepImg.scalePercent(100);
                    jpepImg.setCompressionLevel(0);
                    jpepImg.scalePercent(90);
                    document.add(jpepImg);
                } else {
                    document.newPage();
                    Image jpepImg = Image.getInstance(peptidesSequenceContainer.toImg(), null);
                    jpepImg.setDpi(360, 360);
                    jpepImg.scalePercent(90);
                    jpepImg.setCompressionLevel(0);
                    document.add(jpepImg);
                }
            }

            g2d.dispose();
            contentByte.addTemplate(template, 0, 0);
            document.close();
            byte fileData[] = IOUtils.toByteArray(new FileInputStream(file));
            return fileData;
        } catch (DocumentException exp) {
            System.err.println("at error 633 " + this.getClass().getName() + " -- " + exp.getMessage());
        } catch (IOException exp) {
            System.err.println("at error 635 " + this.getClass().getName() + " -- " + exp.getMessage());
        }
        return null;

    }

    private JLabel generateTitleLabel(String text, int width) {

        final JLabel TitleLabel = new JLabel();
        TitleLabel.setBackground(new java.awt.Color(255, 255, 255));
        TitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        TitleLabel.setText(text);
        TitleLabel.setSize(width, 37);
        TitleLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
        TitleLabel.setBackground(new java.awt.Color(255, 255, 255));
        TitleLabel.setOpaque(true);
        TitleLabel.setForeground(Color.GRAY);
        return TitleLabel;

    }

}
