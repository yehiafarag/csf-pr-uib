package probe.com.model.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.DefaultFontMapper;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.title.TextTitle;
import probe.com.model.beans.identification.IdentificationPeptideBean;
import probe.com.view.core.exporter.ImageToExport;

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
        File xlsText = new File(path, "CSF-PR - " + datasetName + " - All - " + dataType + " - Peptides.xls");// "CSF-PR - " + datasetName + " - All - " + dataType + "- Peptides" + ".csv");

        try {
            if (xlsText.exists()) {
                return;
            }

            FileOutputStream fileOut = new FileOutputStream(xlsText);
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("POI Worksheet");
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
            for (IdentificationPeptideBean pb : allPeptides.values()) {
                HSSFRow peptideRow = worksheet.createRow(index + 2);
                HSSFCell cell0 = peptideRow.createCell(0);
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
            fileOut.flush();
            fileOut.close();
        } catch (Exception exp) {
            System.err.println(exp.getMessage());
        }

    }

    public byte[] exportProteinsInfoCharts(Set<JFreeChart> chartsSet, String fileName, String url, String title) {
        int width = 595;
        int height = 842;
        int startx = 30;

        try {

            File csfFolder = new File(url);
            csfFolder.mkdir();

            File file = new File(url + "/" + fileName);
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
            int starty = 0;
            int counter = 0;

            template = contentByte.createTemplate(width, height);
            g2d = template.createGraphics(width, height, new DefaultFontMapper());
            Rectangle2D rect2d;
            boolean newpage = false;
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
                    int labelHeight = 0;
                    for (String str : ((SymbolAxis) chart.getXYPlot().getDomainAxis()).getSymbols()) {
                        if ((str.length() * 6) > labelHeight) {
                            labelHeight = (str.length() * 6);
                        }
                    }
                    int chartHeight = 400 + labelHeight;
                    rect2d = new Rectangle2D.Double(startx, starty, 505, chartHeight);
                    starty += chartHeight + 20;

                } else {
                    rect2d = new Rectangle2D.Double(30, starty, 505, 250);
                    starty += 270;

                }
                chart.draw(g2d, rect2d);

                counter++;

                if (starty > 600) {
                    g2d.dispose();
                    contentByte.addTemplate(template, 0, 0);
                    newpage = true;
                    template = contentByte.createTemplate(width, height);
                    g2d = template.createGraphics(width, height, new DefaultFontMapper());
                    starty = 30;

                }

            }

//            } 
//            else if (chartsSet.size() > 1) {
//                template = contentByte.createTemplate(width, height);
//                g2d = template.createGraphics(width, height, new DefaultFontMapper());
//                for (JFreeChart chart : chartsSet) {
//                    Rectangle2D rect2d = new Rectangle2D.Double(startx, starty, 250, 250);
//                    chart.draw(g2d, rect2d);
//                    startx += 250 + 50;
//                    if (counter == 1 || counter == 3) {
//                        startx = 0;
//                        starty += 275;
//                    }
//                    counter++;
//
//                }
//            } 
//            else {
//                System.out.println("the targeted chart");
//                template = contentByte.createTemplate(width, height);
//                g2d = template.createGraphics(width, height, new DefaultFontMapper());
//
//                for (JFreeChart chart : chartsSet) {
//
//                    Rectangle2D rect2d = new Rectangle2D.Double(10, 200, 550, 750);
//                    chart.draw(g2d, rect2d);
//
//                }
//
//            }
            g2d.dispose();
            contentByte.addTemplate(template, 0, 0);

            document.close();
            byte fileData[] = IOUtils.toByteArray(new FileInputStream(file));
            return fileData;
        } catch (DocumentException exp) {
            System.err.println("at error 367 " + this.getClass().getName() + " -- " + exp.getMessage());
        } catch (IOException exp) {
            System.err.println("at error 369 " + this.getClass().getName() + " -- " + exp.getMessage());
        }

//          
        return null;//url + userFolder.getName() + "/" + pdfFile.getName();

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
            g2d = template.createGraphics(width, height, new DefaultFontMapper());

            JLabel headerLabel = new JLabel();
            headerLabel.setBackground(new java.awt.Color(255, 255, 255));
            headerLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            headerLabel.setText("Studies Overview");
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

//          
        return null;//url + userFolder.getName() + "/" + pdfFile.getName();

    }

    public byte[] exportBubbleChartAsPdf(JFreeChart chart, String fileName, String url, String title) {
        int width = 842;
        int height = 595;
        Font font = new Font("Verdana", Font.PLAIN, 12);
        chart.getXYPlot().getDomainAxis().setTickLabelFont(font);
        chart.getXYPlot().getRangeAxis().setTickLabelFont(font);

        TextTitle textTitle = new TextTitle(title, font);
        textTitle.setMargin(10, 0, 20, 0);
        textTitle.setPaint(Color.DARK_GRAY);

        chart.setTitle(textTitle);
        try {
            File csfFolder = new File(url);
            csfFolder.mkdir();

            File file = new File(url + "/" + fileName);
            if (file.exists()) {
                file.delete();
                System.out.println("file deleted");
            }
            Document document = new Document(PageSize.A4.rotate());

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));

            document.open();
            document.newPage();

            PdfContentByte contentByte = writer.getDirectContent();
            PdfTemplate template;

            Graphics2D g2d;

            template = contentByte.createTemplate(width, height);
            g2d = template.createGraphics(width, height, new DefaultFontMapper());

            Rectangle2D rect2d = new Rectangle2D.Double(10, 10, width - 20, height - 20);
            chart.draw(g2d, rect2d);

//            System.out.println(VaadinService.getCurrent().getClassLoader().getResource("legend1.png").toURI());
//            File res = new File(csfFolder.getParent(), "Resources");
//            System.out.println("file exise " + res.exists() + "   " + csfFolder.getParent());
//            File legendImageFile = new File(res, "legend1.png");
//            if (legendImageFile.exists()) {
//                g2d.drawImage(ImageIO.read(legendImageFile), 10, height - 90, null);
//            }
            g2d.dispose();
            contentByte.addTemplate(template, 0, 0);
//            document.add(legend);
            document.close();
            byte fileData[] = IOUtils.toByteArray(new FileInputStream(file));
//            String base64 = Base64.encodeBase64String(fileData.);
//            base64 = "data:image/png;base64," + base64;

            return fileData;
        } catch (DocumentException exp) {
            System.err.println("at error 512 " + this.getClass().getName() + " -- " + exp.getMessage());
        } catch (IOException exp) {
            System.err.println("at error 515 " + this.getClass().getName() + " -- " + exp.getMessage());
        }

//          
        return null;//url + userFolder.getName() + "/" + pdfFile.getName();

    }

    public byte[] exportfullReportAsZip(Map<String, Set<JFreeChart>> chartsMap, String fileName, String url, String title, ImageToExport img) {
        int width = 595;
        int height = 842;
        int startx = 32;
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
            g2d = template.createGraphics(width, height, new DefaultFontMapper());

            JLabel reportTiltleLabel = new JLabel();
            reportTiltleLabel.setBackground(new java.awt.Color(255, 255, 255));
            reportTiltleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            reportTiltleLabel.setText("CSF-PR V2.0 Report");
            reportTiltleLabel.setSize(width, 38);
            reportTiltleLabel.setFont(new Font("Verdana", Font.BOLD, 13));
            reportTiltleLabel.paint(g2d);
            starty += 38;

            JLabel studiesPieCharTheaderLabel = new JLabel();
            studiesPieCharTheaderLabel.setBackground(new java.awt.Color(255, 255, 255));
            studiesPieCharTheaderLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            studiesPieCharTheaderLabel.setText("Studies Overview");
            studiesPieCharTheaderLabel.setSize(width - 30, 37);

            studiesPieCharTheaderLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
            g2d.translate(32, starty);
            studiesPieCharTheaderLabel.paint(g2d);
//            starty += 37;
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

            document.setPageSize(PageSize.A4.rotate());
            template = contentByte.createTemplate(height, width);
            g2d = template.createGraphics(height, width, new DefaultFontMapper());
            document.newPage();

            //bubble chart
            starty = 0;
            startx = 0;

            JLabel proteinsOverviewLabel = new JLabel();
            proteinsOverviewLabel.setBackground(new java.awt.Color(255, 255, 255));
            proteinsOverviewLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            proteinsOverviewLabel.setText("Proteins Overview");
            proteinsOverviewLabel.setSize(height, 37);
            proteinsOverviewLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
            g2d.translate(32, starty);
            proteinsOverviewLabel.paint(g2d);

            for (JFreeChart chart : chartsMap.get("proteinsOverviewBubbleChart")) {

                Rectangle2D rect2d = new Rectangle2D.Double(0, 37, height - 64, width - 60);
                chart.draw(g2d, rect2d);
            }
            g2d.dispose();
            contentByte.addTemplate(template, 0, 0);

            //selected protein overview chart
            document.setPageSize(PageSize.A4);
            template = contentByte.createTemplate(width, height);
            g2d = template.createGraphics(width, height, new DefaultFontMapper());
            document.newPage();

            starty = 0;
            startx = 0;

            JLabel proteinInformationOverviewLabel = new JLabel();
            proteinInformationOverviewLabel.setBackground(new java.awt.Color(255, 255, 255));
            proteinInformationOverviewLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            proteinInformationOverviewLabel.setText("Selected Protein Information ( " + title + " )");
            proteinInformationOverviewLabel.setSize(width, 37);
            proteinInformationOverviewLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
            g2d.translate(32, starty);
            proteinInformationOverviewLabel.paint(g2d);
            starty = 37;
            Rectangle2D rect2d;
            boolean newpage = false;
            counter = 0;
            for (JFreeChart chart : chartsMap.get("proteinInformationCharts")) {
                if (newpage) {
                    newpage = false;
                    document.newPage();
                }

                if (counter == 0) {
//
//                    TextTitle textTitle = new TextTitle(title, new Font("Verdana", Font.PLAIN, 12));
//                    textTitle.setMargin(10, 0, 20, 0);
//                    textTitle.setPaint(Color.DARK_GRAY);
//                    chart.setTitle(textTitle);
                    int labelHeight = 30;
                    if (chart.getXYPlot().getDomainAxis().isVerticalTickLabels()) {
                        for (String str : ((SymbolAxis) chart.getXYPlot().getDomainAxis()).getSymbols()) {
                            if ((str.length() * 6) > labelHeight) {
                                labelHeight = (str.length() * 6);
                            }
                        }
                    }

                    int chartHeight = 400 + labelHeight;
                    rect2d = new Rectangle2D.Double(startx, starty, 505, chartHeight);
                    starty += chartHeight + 20;

                } else {
                    rect2d = new Rectangle2D.Double(0, starty, 505, 250);
                    starty += 270;

                }
                chart.draw(g2d, rect2d);

                counter++;

                if (starty > 600) {
                    g2d.dispose();
                    contentByte.addTemplate(template, 0, 0);
                    newpage = true;
                    template = contentByte.createTemplate(width, height);
                    g2d = template.createGraphics(width, height, new DefaultFontMapper());
                    starty = 30;
                    startx = 32;

                }

            }
            g2d.dispose();
            contentByte.addTemplate(template, 0, 0);
//            ByteArrayInputStream bin = new ByteArrayInputStream(img.getImgByteArr());
//            BufferedImage i = ImageIO.read(bin);
//
//            document.setPageSize(PageSize.A4);
//            template = contentByte.createTemplate(width, height);
//            g2d = template.createGraphics(width, height, new DefaultFontMapper());
//            document.newPage();
//
//            g2d.translate(32, 20);
//
//            i = i.getSubimage(0, 0, img.getWidth(), img.getHeight());
//            g2d.drawImage((Image)i,0,0,width-50,width-50, null);
//            g2d.dispose();
//            contentByte.addTemplate(template, 0, 0);
            document.close();
            byte fileData[] = IOUtils.toByteArray(new FileInputStream(file));

            return fileData;
        } catch (DocumentException exp) {
            System.err.println("at error 633 " + this.getClass().getName() + " -- " + exp.getMessage());
        } catch (IOException exp) {
            System.err.println("at error 635 " + this.getClass().getName() + " -- " + exp.getMessage());
        }

//          
        return null;//url + userFolder.getName() + "/" + pdfFile.getName();

    }

}
