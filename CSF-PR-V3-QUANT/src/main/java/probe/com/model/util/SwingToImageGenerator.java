/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.model.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.DefaultFontMapper;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JComponent;
import org.apache.commons.codec.binary.Base64;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.encoders.ImageEncoder;
import org.jfree.chart.encoders.ImageEncoderFactory;
import org.jfree.chart.encoders.ImageFormat;

/**
 *
 * @author yfa041
 */
public class SwingToImageGenerator {

    public String generateHeatMap(JComponent component) {

        BufferedImage heatMapImg = new BufferedImage((component.getWidth()), (component.getHeight()), BufferedImage.TYPE_INT_ARGB);
        Graphics g = heatMapImg.getGraphics();
        component.paint(g);
        return generateEncodedImg(heatMapImg);
    }

    private String generateEncodedImg(BufferedImage image) {
        String base64 = "";
        try {
            ImageEncoder in = ImageEncoderFactory.newInstance(ImageFormat.PNG, 0);
            byte[] imageData = in.encode(image);
            base64 = Base64.encodeBase64String(imageData);
            base64 = "data:image/png;base64," + base64;
            System.gc();
        } catch (IOException exp) {
            System.err.println(exp.getLocalizedMessage());
        }
        return base64;
    }
    
    

}
