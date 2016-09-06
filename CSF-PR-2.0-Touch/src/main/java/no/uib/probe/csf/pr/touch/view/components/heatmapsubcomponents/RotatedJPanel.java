/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.components.heatmapsubcomponents;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author yfa041
 */
public class RotatedJPanel extends JPanel {

    private BufferedImage componentImg;

    private GraphicsConfiguration getDefaultConfiguration() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        return gd.getDefaultConfiguration();
    }

    private void updateComponentImg() {

        BufferedImage img = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D)img.getGraphics();        
        this.paint(g2d);
//        g2d.dispose();
        componentImg = createFlipped(img);

    }

    private BufferedImage createFlipped(BufferedImage image) {

        double rotationRequired = Math.toRadians(270);
        double locationX = image.getWidth() / 2;
        double locationY = image.getHeight() / 2;
        AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

        AffineTransform at = new AffineTransform();
        at.concatenate(AffineTransform.getScaleInstance(1, -1));
        at.concatenate(AffineTransform.getTranslateInstance(0, -image.getHeight()));

//        ((Graphics2D) image.getGraphics()).setBackground(Color.GREEN);
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = newImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.drawImage(op.filter(image, null), 0, 0, null);
        return newImage;
    }

    public JPanel getRotatedJPanel() {
//        this.setBackground(new Color(255, 255, 255));
//        Graphics2D g2d = (Graphics2D) getGraphics();
//        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2d.setComposite(AlphaComposite.getInstance(
//                AlphaComposite.SRC_OVER, 0.1f));

        updateComponentImg();
        JPanel panel = new JPanel() {

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);

//                Graphics2D g2d =(Graphics2D)g;
////                 g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING,   RenderingHints.VALUE_ANTIALIAS_ON);
//        g2d.setComposite(AlphaComposite.getInstance(
//            AlphaComposite.SRC_OVER, 0.1f));
//        g2d.setColor(new Color(255, 255, 255, 50));
                g.drawImage(componentImg, 0, 0, this);
            }

        };
        panel.setSize(this.getWidth(), this.getHeight());
        panel.setLocation(this.getLocation());
        panel.setOpaque(false);
        return panel;

    }
}
