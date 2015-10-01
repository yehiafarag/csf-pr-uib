/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.DefaultFontMapper;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.FileOutputStream;
import org.jfree.chart.JFreeChart;

/**
 *
 * @author yfa041
 */
public class JfreeExporter {
    
    public void writeChartToPDFFile(JFreeChart chart, int width,int height,String fileNAme){
//        PdfWriter writer = null;
//        Document document =new Document();
//        try{
//        writer = PdfWriter.getInstance(document, new FileOutputStream("D:\\astrid_export\\"+fileNAme));
//        document.open();
//        PdfContentByte contentByte = writer.getDirectContent();
//            PdfTemplate template = contentByte.createTemplate(width, height);
//            Graphics2D g2d = template.createGraphics(width, height, new DefaultFontMapper());
//            Rectangle2D rect2d = new Rectangle2D.Double(0, 0, width, height);
//            chart.draw(g2d, rect2d);
//            g2d.dispose();
//            contentByte.addTemplate(template, 0,0);
//        
//        }catch(Exception exp){
//            exp.printStackTrace();
//        }
//        document.close();
//    
    
    }
    
}
