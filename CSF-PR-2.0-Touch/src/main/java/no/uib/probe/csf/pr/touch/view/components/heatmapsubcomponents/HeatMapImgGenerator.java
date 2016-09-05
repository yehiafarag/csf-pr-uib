/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.components.heatmapsubcomponents;

import com.itextpdf.text.pdf.codec.Base64;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import no.uib.probe.csf.pr.touch.logic.beans.HeatMapHeaderCellInformationBean;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.CachedImageHandlerBase64Encoder;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.jfree.chart.encoders.ImageEncoder;
import org.jfree.chart.encoders.ImageEncoderFactory;
import org.jfree.chart.encoders.ImageFormat;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

/**
 *
 * @author Yehia Farag
 */
public class HeatMapImgGenerator {

    private final Color transparent = new Color(255, 255, 255, 0);
    private final Color white = new Color(255, 255, 255);
    private int imageWidth;
    private int imageHeight;
    private int cellWidth;
    private double resizeFactor;
    private final Map<String, Rectangle> headerLabelMap = new LinkedHashMap<>();
    private boolean printLabels;

    public Map<String, Rectangle> getHeaderLabelMap() {
        return headerLabelMap;
    }

    public String generateHeatmap(Set<HeatMapHeaderCellInformationBean> rows, Set<HeatMapHeaderCellInformationBean> columns, String[][] data, int heatmapWidth, int heatmaHeight, boolean resetRowLabels, boolean restColumnLabels, boolean printLabels) {
        this.printLabels = printLabels;
        headerLabelMap.clear();
//        int panelHeight = heatmaHeight;
//        if (heatmapWidth < heatmaHeight) {
//            panelHeight = heatmapWidth;
//        }

        cellWidth = 20;//Math.min(((panelHeight - 150) / columns.size()), 25);

        JPanel heatmapPanelLayout = new JPanel();

        heatmapPanelLayout.setLayout(null);
        heatmapPanelLayout.setVisible(true);
        heatmapPanelLayout.setBackground(transparent);

        imageWidth = (columns.size() * cellWidth) + 150;
        imageHeight = (rows.size() * cellWidth) + 150;
        cellWidth = 20;//
        resizeFactor = 1;// (double) imageWidth / (double) panelWidth;

        cellWidth = (int) (20 * resizeFactor);
        heatmapPanelLayout.setSize(imageWidth, imageHeight);
        JPanel cornerCell = initCell("transparent", 0, 0, fullBorder);
        cornerCell.setSize((int) (150 * resizeFactor), (int) (150 * resizeFactor));
        heatmapPanelLayout.add(cornerCell);
        int x = (int) (150 * resizeFactor);
        int y = (int) (20 * resizeFactor);

        for (HeatMapHeaderCellInformationBean bean : columns) {

            headerLabelMap.put("col__" + bean.getDiseaseCategory(), new Rectangle(0, 0, 0, 0));

        }
        for (HeatMapHeaderCellInformationBean bean : rows) {

            headerLabelMap.put("row__" + bean.getDiseaseCategory(), new Rectangle(0, 0, 0, 0));

        }

        for (HeatMapHeaderCellInformationBean headerCell : columns) {

            if (!restColumnLabels) {
                Rectangle rectangle = headerLabelMap.get("col__" + headerCell.getDiseaseCategory());
                rectangle.setBounds(0, 0, (int) rectangle.getWidth() + cellWidth, (int) (20 * resizeFactor));
            }
            JPanel cell = initCell(headerCell.getDiseaseColor(), x, y, fullBorder);
            cell.setSize(cellWidth, (int) (130 * resizeFactor));
            JComponent label2 = initLabel(headerCell.getDiseaseGroupName(), cell.getSize().width, cell.getSize().height, true, cell.getBackground(), fullBorder);
            cell.add(label2);
            x += cellWidth;
            heatmapPanelLayout.add(cell);
        }

        x = (int) (150 * resizeFactor);
        int topLabelContainerWidth = (columns.size() * cellWidth);
        int countLimit = columns.size() / 3;
        int counter = 0;

        for (String dCat : headerLabelMap.keySet()) {

            HeatMapHeaderCellInformationBean bean = null;
            for (HeatMapHeaderCellInformationBean tbean : columns) {
                if (("col__" + tbean.getDiseaseCategory()).equalsIgnoreCase(dCat)) {
                    bean = tbean;
                    break;

                }
            }
            if (restColumnLabels && bean != null) {
                int width = countLimit * cellWidth;
                topLabelContainerWidth = topLabelContainerWidth - width;
                if (imageWidth != 0 && counter == headerLabelMap.size() - 1) {
                    width += topLabelContainerWidth;
                }

                JComponent label = initLabel(dCat, width, (int) (20 * resizeFactor), false, Color.decode(bean.getDiseaseColor()), fullBorder);
                label.setLocation(x, 0);
                heatmapPanelLayout.add(label);
                x += label.getSize().width;
                counter++;

            } else if (bean != null) {
                JComponent label = initLabel(dCat, headerLabelMap.get(dCat).width, (int) (20 * resizeFactor), false, Color.decode(bean.getDiseaseColor()), fullBorder);
                label.setLocation(x, 0);
                heatmapPanelLayout.add(label);
                x += headerLabelMap.get(dCat).width;
            }

        }

//        headerLabelMap.clear();
//        for (HeatMapHeaderCellInformationBean bean : rows) {
//
//            headerLabelMap.put(bean.getDiseaseCategory(), new Rectangle(0, 0, 0, 0));
//
//        }
        y = (int) (150 * resizeFactor);

        for (HeatMapHeaderCellInformationBean headerCell : rows) {

            if (!resetRowLabels) {

                Rectangle rectangle = headerLabelMap.get("row__" + headerCell.getDiseaseCategory());
                rectangle.setBounds(0, 0, (int) (20 * resizeFactor), (int) rectangle.getHeight() + cellWidth);
            }

            JPanel cell = initCell(headerCell.getDiseaseColor(), (int) (20 * resizeFactor), y, fullBorder);
            cell.setSize((int) (130 * resizeFactor), cellWidth);
            JComponent label2 = initLabel(headerCell.getDiseaseGroupName(), cell.getSize().width, cell.getSize().height, false, cell.getBackground(), fullBorder);
            cell.add(label2);
            y += cellWidth;
            heatmapPanelLayout.add(cell);

        }

        y = (int) (150 * resizeFactor);
        topLabelContainerWidth = (rows.size() * cellWidth);
        countLimit = rows.size() / 3;
        counter = 0;

        for (String dCat : headerLabelMap.keySet()) {

            HeatMapHeaderCellInformationBean bean = null;
            for (HeatMapHeaderCellInformationBean tbean : rows) {
                if (("row__" + tbean.getDiseaseCategory()).equalsIgnoreCase(dCat)) {
                    bean = tbean;
                    break;

                }
            };
            if (resetRowLabels && bean != null) {
                int width = countLimit * cellWidth;
                topLabelContainerWidth = topLabelContainerWidth - width;
                if (imageHeight != 0 && counter == headerLabelMap.size() - 1) {
                    width += topLabelContainerWidth;
                }

                JComponent label = initLabel(dCat, (int) (20 * resizeFactor), width, true, Color.decode(bean.getDiseaseColor()), fullBorder);
                label.setLocation(0, y);
                heatmapPanelLayout.add(label);
                y += label.getSize().height;
                counter++;

            } else if (bean != null) {
                JComponent label = initLabel(dCat, (int) (20 * resizeFactor), headerLabelMap.get(dCat).height, true, Color.decode(bean.getDiseaseColor()), fullBorder);
                label.setLocation(0, y);
                heatmapPanelLayout.add(label);
                y += headerLabelMap.get(dCat).height;
            }

        }

        x = (int) (150 * resizeFactor);
        y = (int) (150 * resizeFactor);
        int row1 = 0, col = 0;
        for (String[] row : data) {

            for (String color : row) {

//                System.out.println("at x "+row1+"  y "+col+" color "+color);
                JPanel cell = initCell(color.split("__")[0], x, y, fullBorder);
                cell.setOpaque(true);
                JComponent label2 = initLabel(color.split("__")[1], cell.getSize().width, cell.getSize().height, false, cell.getBackground(), fullBorder);
                cell.add(label2);
                x += cellWidth;
                heatmapPanelLayout.add(cell);
                col++;
            }
            col = 0;
            row1++;
            x = (int) (150 * resizeFactor);
            y += cellWidth;
        }

        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

//        graphics.setPaint(transparent);
//        graphics.setBackground(transparent);
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

    public int getPanelHeight() {
        return imageHeight;
    }

//    private BufferedImage generateSVG(JPanel myCanvas, int w, int h) {
//        Writer out = null;
//        String svgNS = "http://www.w3.org/2000/svg";
//        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
//        Document myFactory = domImpl.createDocument(svgNS, "svg", null);
//        SVGGeneratorContext ctx
//                = SVGGeneratorContext.createDefault(myFactory);
//        CachedImageHandlerBase64Encoder cachedImageHandlerBase64Encoder = new CachedImageHandlerBase64Encoder();
//        ctx.setGenericImageHandler(cachedImageHandlerBase64Encoder);
//        SVGGraphics2D svgGenerator = new SVGGraphics2D(ctx, false);
//        myCanvas.paintComponents(svgGenerator);
//        return cachedImageHandlerBase64Encoder.buildBufferedImage(new Dimension(w, h));
//    }

    private final Border fullBorder = new LineBorder(new Color(220, 224, 224), 1);

    private JPanel initCell(String color, int x, int y, Border border) {
        JPanel cell = new JPanel(new FlowLayout(FlowLayout.CENTER));
        cell.setSize(cellWidth, cellWidth);
        Color c;

        if (color == null) {
            c = white;
//            cell.setBorder(new LineBorder(Color.LIGHT_GRAY));
        } else if (color.equalsIgnoreCase("RGB(255,255,255)")) {
            c = white;
//            cell.setBorder(new LineBorder(Color.LIGHT_GRAY));
        } else if (color.contains("#")) {
            c = Color.decode(color);
//            cell.setBorder(new LineBorder(Color.LIGHT_GRAY));
        } else if (color.toLowerCase().contains("rgb")) {
            String rgb = color.toLowerCase().replace("rgb", "").replace("(", "").replace(")", "").replace(" ", "");
            String[] stringRGBArr = rgb.split(",");
            c = new Color(Integer.valueOf(stringRGBArr[0]), Integer.valueOf(stringRGBArr[1]), Integer.valueOf(stringRGBArr[2]));
//            cell.setBorder(new LineBorder(Color.LIGHT_GRAY));
        } else {
            c = white;

//            cell.setBorder(new LineBorder(c));
        }

        cell.setBackground(c);
        cell.setBorder(border);
        cell.setOpaque(true);
        cell.setLocation(x, y);

        return cell;

    }

    boolean one = false;

    private JComponent initLabel(String text, int w, int h, boolean rotate, Color background, Border border) {
        JLabel label = new JLabel("");
        if (this.printLabels) {
            label.setText(text);
            label.setFont(new Font("Open Sans", Font.PLAIN, (int) (15 * resizeFactor)));
            label.setVerticalTextPosition(JLabel.CENTER);
            label.setHorizontalTextPosition(JLabel.CENTER);
            label.setForeground(Color.WHITE);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);
        }
        label.setSize(w, h);
        label.setBackground(background);
        label.setBorder(border);
        label.setOpaque(true);

        if (rotate) {
 if (this.printLabels) {
            label.setVerticalTextPosition(JLabel.TOP);
                label.setHorizontalTextPosition(JLabel.CENTER);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setVerticalAlignment(JLabel.TOP);
            }
            label.setSize(h, w);

            RotatedJPanel rotPanel = new RotatedJPanel();
            rotPanel.setSize(h, h);
            rotPanel.add(label);
            JPanel panel = rotPanel.getRotatedJPanel();
            panel.setOpaque(false);
            panel.setSize(w, h);
            rotPanel.setBackground(Color.blue);
            rotPanel.setSize(h, h);
            rotPanel.add(label);
            return (panel);
        }

        return label;

    }

//    public static void main(String[] args) {
//        JFrame frame = new JFrame("Rotation Test");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        JLabel label = new JLabel("kokowawaaaaaaaaaaaaaaaaaaaaaa");
//        label.setVerticalTextPosition(JLabel.TOP);
//        label.setHorizontalTextPosition(JLabel.CENTER);
//        label.setForeground(Color.black);
//        label.setHorizontalAlignment(JLabel.CENTER);
//        label.setVerticalAlignment(JLabel.TOP);
//        label.setSize(150, 150);
//
//        RotatedJPanel rotPanel = new RotatedJPanel();
//        rotPanel.setSize(150, 150);
//        rotPanel.add(label);
//        JPanel panel = rotPanel.getRotatedJPanel();
//        panel.setBackground(Color.ORANGE);
//        panel.setOpaque(true);
//
//        frame.setSize(350, 350);
//        frame.add(panel);
//        frame.setVisible(true);
//    }
//
//    private JPanel rotateLabel(JLabel label) {
//
//        JPanel rotatedPanel = new JPanel() {
//
//            @Override
//            public Dimension getPreferredSize() {
//                return new Dimension(label.getWidth(), label.getHeight());
//            }
//
//            @Override
//            protected void paintComponent(Graphics g) {
//                super.paintComponent(g);
//                Graphics2D g2 = (Graphics2D) g;
//                g2.translate(0, 0);
//                g2.rotate(Math.PI * 3 / 2, 0, 25);
//
////                g2.drawImage(label.createVolatileImage(label.getWidth(), label.getHeight()), 0, 0, null);
//            }
//        };
//        rotatedPanel.add(label);
//        rotatedPanel.setSize(new Dimension(label.getWidth(), label.getHeight()));
////        rotatedPanel.setBackground(Color.BLUE);
//        rotatedPanel.setBorder(testBorder);
//
//        rotatedPanel.setOpaque(false);
//        return rotatedPanel;
//
//    }
    public int getPanelWidth() {
        return imageWidth;
    }

    public double getResizeFactor() {
        return resizeFactor;
    }

}
