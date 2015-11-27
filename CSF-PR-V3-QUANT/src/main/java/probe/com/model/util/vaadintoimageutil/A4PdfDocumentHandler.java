/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.model.util.vaadintoimageutil;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.DefaultFontMapper;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.Spring.height;
import static javax.swing.Spring.width;
import static org.apache.poi.hssf.usermodel.HeaderFooter.file;

/**
 *
 * @author yfa041
 */
public class A4PdfDocumentHandler implements Serializable {

    Document document;
    PdfTemplate template;
    private Graphics2D g2d;
    PdfContentByte contentByte;
    PdfWriter writer;
    int width, height;
    private int leftToPrint;

    public final void setPageSize(Rectangle pageSize) {
        if (pageSize == PageSize.A4) {
            document.setPageSize(pageSize);
            width = 595;
            height = 842;

        } else {
            document.setPageSize(pageSize);
            height = 595;
            width = 842;
        }
    }

    public A4PdfDocumentHandler(Rectangle pageSize, File exportingFile) {
        if (pageSize == null) {
            document = new Document();
            width = 595;
            height = 842;

        } else {
            setPageSize(pageSize);
        }

        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(exportingFile));
        } catch (DocumentException ex) {
            Logger.getLogger(A4PdfDocumentHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(A4PdfDocumentHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        document.open();
        document.newPage();
        contentByte = writer.getDirectContent();

        startNewPage();
    }

    public final void startNewPage() {

        template = contentByte.createTemplate(width, height);
        g2d = template.createGraphics(width, height, new DefaultFontMapper());
        leftToPrint = height;
    }

    public Graphics2D getG2d() {
        return g2d;
    }

    public void setNextUsedHeight(int usedHeight) {
        leftToPrint = leftToPrint - usedHeight;
        if (leftToPrint < 0) {
            closePage();
            startNewPage();
        }

    }

    public void closePage() {
        g2d.dispose();
        contentByte.addTemplate(template, 0, 0);
    }

    public void closePDFDocument() throws DocumentException{
        closePage();
        this.document.close();

    }
}
