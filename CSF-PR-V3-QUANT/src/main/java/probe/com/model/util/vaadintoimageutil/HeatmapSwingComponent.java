/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.model.util.vaadintoimageutil;

import com.itextpdf.text.pdf.codec.Base64;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import org.jfree.chart.encoders.ImageEncoder;
import org.jfree.chart.encoders.ImageEncoderFactory;
import org.jfree.chart.encoders.ImageFormat;

/**
 *
 * @author yfa041
 */
public class HeatmapSwingComponent {

    public String generateHeatmap(String[] rows, String[] columns, String[][] data) {

        JPanel heatmapPanelLayout = new JPanel();
        heatmapPanelLayout.setLayout(null);
        heatmapPanelLayout.setVisible(true);
        heatmapPanelLayout.setBorder(new LineBorder(Color.BLACK));
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

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setPaint(Color.WHITE);
        heatmapPanelLayout.paint(graphics);
//        super.paint(graphics);
        byte[] imageData = null;

        try {

            ImageEncoder in = ImageEncoderFactory.newInstance(ImageFormat.PNG, new Float(0.084666f));
            imageData = in.encode(image);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        String base64 = Base64.encodeBytes(imageData);
        base64 = "data:image/png;base64," + base64;
        return base64;
//
//        JFrame frame = new JFrame();
//        frame.setSize(1000, 1000);
//        frame.add(heatmapPanelLayout);
//        frame.setVisible(true);

//        return "";
    }

    private JPanel initCell(String color, int x, int y) {
        JPanel cell = new JPanel();
        cell.setSize(50, 50);
        cell.setBackground(Color.decode(color));
        cell.setOpaque(true);
        cell.setLocation(x, y);
        cell.setBorder(new LineBorder(Color.WHITE));
        return cell;

    }

    public static void main(String[] args) {

        HeatmapSwingComponent testComp = new HeatmapSwingComponent();
        String[] rows = new String[]{"#80FF00", "#80FF00", "#80FF00", "#80FF00", "#80FF00"};
        String[] columns = new String[]{"#FE2E2E", "#FE2E2E", "#FE2E2E", "#FE2E2E", "#FE2E2E"};
        String[][] data = new String[rows.length][columns.length];
        String[] row1 = new String[]{"#FFFFFF", "#2E64FE", "#FFFFFF", "#2E64FE", "#FFFFFF"};
        String[] row2 = new String[]{"#FFFFFF", "#2E64FE", "#FFFFFF", "#2E64FE", "#FFFFFF"};
        String[] row3 = new String[]{"#FFFFFF", "#2E64FE", "#FFFFFF", "#2E64FE", "#FFFFFF"};
        String[] row4 = new String[]{"#FFFFFF", "#2E64FE", "#FFFFFF", "#2E64FE", "#FFFFFF"};
        String[] row5 = new String[]{"#FFFFFF", "#2E64FE", "#FFFFFF", "#2E64FE", "#FFFFFF"};
        data[0] = row1;
        data[1] = row2;
        data[2] = row3;
        data[3] = row4;
        data[4] = row5;
        testComp.generateHeatmap(rows, columns, data);

    }
}
