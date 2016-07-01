/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.components.heatmapsubcomponents;

import com.itextpdf.text.pdf.codec.Base64;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import org.jfree.chart.encoders.ImageEncoder;
import org.jfree.chart.encoders.ImageEncoderFactory;
import org.jfree.chart.encoders.ImageFormat;

/**
 *
 * @author Yehia Farag
 */
public class HeatMapImgGenerator {

    private final Color transparent = new Color(255, 255, 255, 0);
    private final Color white = new Color(255, 255, 255);
    private int imageWidth;
    private  int cellWidth;
    private double resizeFactor;

    public String generateHeatmap(String[] rows, String[] columns, String[][] data, int heatmapWidth, int heatmaHeight) {
        int panelHeight = heatmaHeight;
        if (heatmapWidth <heatmaHeight) {
            panelHeight = heatmapWidth;
        }
        
         cellWidth = Math.min(((panelHeight-170)/columns.length),30);
        

        JPanel heatmapPanelLayout = new JPanel();
        heatmapPanelLayout.setLayout(null);
        heatmapPanelLayout.setVisible(true);
        heatmapPanelLayout.setBackground(white);

   
        imageWidth = (columns.length * cellWidth) + 170;
              cellWidth = 30;//
        int panelWidth = (columns.length * cellWidth) + 170;
        System.out.println("at cell width "+cellWidth+"  "+rows.length+"   h "+panelWidth+"  available h "+heatmaHeight +"   "+((panelHeight-170)/columns.length));
       
        resizeFactor=(double)imageWidth/(double)panelWidth;
        
        cellWidth=(int)(30*resizeFactor);
        panelWidth=(int)(panelWidth*resizeFactor);
        
        
        
        int height = (rows.length * cellWidth) + (int)(170*resizeFactor);
        heatmapPanelLayout.setSize(panelWidth, height);
        JPanel cornerCell = initCell("transparent", 0, 0);
        cornerCell.setSize((int)(170*resizeFactor), (int)(170*resizeFactor));
        heatmapPanelLayout.add(cornerCell);
        int x = (int)(170*resizeFactor);
        int y = (int)(20*resizeFactor);
        

        for (String headerCell : columns) {
            JPanel cell = initCell(headerCell, x, y);
            cell.setSize(cellWidth, (int)(150*resizeFactor));
            x += cellWidth;
            heatmapPanelLayout.add(cell);

        }
        y = (int)(170*resizeFactor);
        for (String headerCell : rows) {
            JPanel cell = initCell(headerCell, (int)(20*resizeFactor), y);
            cell.setSize((int)(150*resizeFactor), cellWidth);
            y += cellWidth;
            heatmapPanelLayout.add(cell);

        }
        x = (int)(170*resizeFactor);
        y = (int)(170*resizeFactor);
        for (String[] row : data) {
            for (String color : row) {
                JPanel cell = initCell(color, x, y);
                heatmapPanelLayout.add(cell);
                x += cellWidth;
            }
            x = (int)(170*resizeFactor);
            y += cellWidth;
        }

        BufferedImage image = new BufferedImage(panelWidth, panelWidth , BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setPaint(transparent);
        graphics.setBackground(transparent);
        heatmapPanelLayout.paint(graphics);
        byte[] imageData = null;

        try {

            ImageEncoder in = ImageEncoderFactory.newInstance(ImageFormat.PNG, 1);
            imageData = in.encode(image);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        String base64 = Base64.encodeBytes(imageData);
        base64 = "data:image/png;base64," + base64;
        return base64;

    }

    private JPanel initCell(String color, int x, int y) {
        JPanel cell = new JPanel();
        cell.setSize(cellWidth, cellWidth);
        Color c;
        if (color == null) {
            c = white;
            cell.setBorder(new LineBorder(Color.LIGHT_GRAY));
        } else if (color.equalsIgnoreCase("RGB(255,255,255)")) {
            c = white;
            cell.setBorder(new LineBorder(Color.LIGHT_GRAY));
        } else if (color.contains("#")) {
            c = Color.decode(color);
            cell.setBorder(new LineBorder(Color.LIGHT_GRAY));
        } else if (color.toLowerCase().contains("rgb")) {
            String rgb = color.toLowerCase().replace("rgb", "").replace("(", "").replace(")", "").replace(" ", "");
            String[] stringRGBArr = rgb.split(",");
            c = new Color(Integer.valueOf(stringRGBArr[0]), Integer.valueOf(stringRGBArr[1]), Integer.valueOf(stringRGBArr[2]));
            cell.setBorder(new LineBorder(Color.LIGHT_GRAY));
        } else {
            c = white;
            cell.setBorder(new LineBorder(c));
        }
      

        cell.setBackground(c);
        cell.setOpaque(true);
        cell.setLocation(x, y);
       
        return cell;

    }

    public int getPanelWidth() {
        return imageWidth;
    }

    public double getResizeFactor() {
        return resizeFactor;
    }

}
