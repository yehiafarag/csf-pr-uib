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

    public String generateHeatmap(String[] rows, String[] columns, String[][] data) {

        JPanel heatmapPanelLayout = new JPanel();
        heatmapPanelLayout.setLayout(null);
        heatmapPanelLayout.setVisible(true);
        
        int width = (columns.length + 1) * 50;
        int height = (rows.length + 1) * 50;
        heatmapPanelLayout.setSize(width, height);
        JPanel cornerCell = initCell("#ffffff", 0, 0);
        int x = 50;
        int y = 0;
        heatmapPanelLayout.add(cornerCell);

        for (String headerCell : columns) {
            JPanel cell = initCell(headerCell, x, y);
            x += 50;
            heatmapPanelLayout.add(cell);

        }
        y = 50;
        for (String headerCell : rows) {
            JPanel cell = initCell(headerCell, 0, y);
            y += 50;
            heatmapPanelLayout.add(cell);

        }
        x = 50;
        y = 50;
        for (String[] row : data) {
            for (String color : row) {
                JPanel cell = initCell(color, x, y);
                heatmapPanelLayout.add(cell);
                x += 50;
            }
            x = 50;
            y += 50;
        }

        BufferedImage image = new BufferedImage(width+10, height+10, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setPaint(Color.WHITE);
        graphics.setBackground(Color.WHITE);
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
        cell.setSize(50, 50);
        Color c;
        if (color.contains("#")) {
            c = Color.decode(color);
        } else if (color.toLowerCase().contains("rgb")) {
            String rgb = color.toLowerCase().replace("rgb", "").replace("(", "").replace(")", "").replace(" ", "");
            String[] stringRGBArr = rgb.split(",");
            c = new Color(Integer.valueOf(stringRGBArr[0]), Integer.valueOf(stringRGBArr[1]), Integer.valueOf(stringRGBArr[2]));
        } else {
            c = Color.RED;
        }

        cell.setBackground(c);
        cell.setOpaque(true);
        cell.setLocation(x, y);
        cell.setBorder(new LineBorder(Color.WHITE));
        return cell;

    }

}
