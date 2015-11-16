package probe.com.model.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.DefaultFontMapper;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.fop.svg.PDFTranscoder;
import org.jfree.chart.JFreeChart;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.svg.SVGDocument;

/**
 *
 * @author yfa041
 */
public class JfreeExporter {

    public void writeChartToPDFFile(JFreeChart chart, int width, int height, String fileNAme) {
        PdfWriter writer;
        Document document = new Document(PageSize.A4_LANDSCAPE);
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(fileNAme));
            document.open();
            PdfContentByte contentByte = writer.getDirectContent();
            PdfTemplate template = contentByte.createTemplate(width, height);
            Graphics2D g2d = template.createGraphics(width, height, new DefaultFontMapper());
            Rectangle2D rect2d = new Rectangle2D.Double(0, 0, width, height);
            chart.draw(g2d, rect2d);
            g2d.dispose();
            contentByte.addTemplate(template, 0, 0);

        } catch (Exception exp) {
            exp.printStackTrace();
        }
        document.close();

    }

   

   
    public String exportClusteringImgAsPdf(File userFolder, String url, String textFileName, BufferedImage upperTreeBImg, BufferedImage sideTreeBImg, BufferedImage heatMapImg, BufferedImage interactiveColumnImg, boolean clustColumn) {
        try {
            int totalWidth = sideTreeBImg.getWidth() + heatMapImg.getWidth() + interactiveColumnImg.getWidth() + 10;
            int totalHeight = 0;
            totalHeight = sideTreeBImg.getHeight() + 10;
            if (clustColumn) {
                totalHeight += upperTreeBImg.getHeight();
            }
            // Get a SVGDOMImplementation and create an XML document
            DOMImplementation domImpl = new SVGDOMImplementation();
            String svgNS = "http://www.w3.org/2000/svg";
            SVGDocument svgDocument = (SVGDocument) domImpl.createDocument(svgNS, "svg", null);
            // Create an instance of the SVG Generator
            SVGGraphics2D svgGenerator = new SVGGraphics2D(svgDocument);
            svgGenerator.setSVGCanvasSize(new Dimension(totalWidth, totalHeight));
            svgGenerator.drawImage(upperTreeBImg, sideTreeBImg.getWidth() + 5, 5, null);
            svgGenerator.drawImage(sideTreeBImg, 5, upperTreeBImg.getHeight() + 5, null);
            svgGenerator.drawImage(heatMapImg, sideTreeBImg.getWidth() + 5, upperTreeBImg.getHeight() + 5, null);
            svgGenerator.drawImage(interactiveColumnImg, sideTreeBImg.getWidth() + heatMapImg.getWidth() + 5, upperTreeBImg.getHeight() + 5, null);

            File pdfFile = new File(userFolder, textFileName + ".pdf");
            if (!pdfFile.exists()) {
                pdfFile.createNewFile();
            }
            // write the svg file
            File svgFile = new File(pdfFile.getAbsolutePath() + ".temp");

            OutputStream outputStream = new FileOutputStream(svgFile);
            BufferedOutputStream bos = new BufferedOutputStream(outputStream);
            OutputStreamWriter out = new OutputStreamWriter(bos, "UTF-8");
            svgGenerator.stream(out, true /* use css */);
            outputStream.flush();
            outputStream.close();
            bos.close();

            String svgURI = svgFile.toURI().toString();
            TranscoderInput svgInputFile = new TranscoderInput(svgURI);

            OutputStream outstream = new FileOutputStream(pdfFile);
            bos = new BufferedOutputStream(outstream);
            TranscoderOutput output = new TranscoderOutput(bos);

            // write as pdf
            Transcoder pdfTranscoder = new PDFTranscoder();
            pdfTranscoder.addTranscodingHint(PDFTranscoder.KEY_PIXEL_UNIT_TO_MILLIMETER, 0.084666f);
            pdfTranscoder.transcode(svgInputFile, output);

            outstream.flush();
            outstream.close();
            bos.close();
            System.gc();

            return url + userFolder.getName() + "/" + pdfFile.getName();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

}
