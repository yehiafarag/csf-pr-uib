/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.model.util.vaadintoimageutil.peptideslayout;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author yfa041
 */
public class RotatedJPanel extends JPanel {

    private BufferedImage componentImg;

    private BufferedImage rotate(BufferedImage image, double angle) {
        double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
        int w = image.getWidth(), h = image.getHeight();
        int neww = (int) Math.floor(w * cos + h * sin), newh = (int) Math.floor(h * cos + w * sin);
        GraphicsConfiguration gc = getDefaultConfiguration();
        BufferedImage result = gc.createCompatibleImage(neww, newh, Transparency.TRANSLUCENT);
        Graphics2D g = result.createGraphics();
        g.translate((neww - w) / 2, (newh - h) / 2);
        g.rotate(angle, w / 2, h / 2);
        g.drawRenderedImage(image, null);
        g.dispose();
        return result;
    }

    private GraphicsConfiguration getDefaultConfiguration() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        return gd.getDefaultConfiguration();
    }

    private void updateComponentImg() {

        BufferedImage img = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        this.paint(g);
        g.dispose();
        componentImg =  createFlipped(img);
    }
    
     private  BufferedImage createFlipped(BufferedImage image)
    {
        AffineTransform at = new AffineTransform();
        at.concatenate(AffineTransform.getScaleInstance(1, -1));
        at.concatenate(AffineTransform.getTranslateInstance(0, -image.getHeight()));
        return createTransformed(image, at);
    }
      private  BufferedImage createTransformed(
        BufferedImage image, AffineTransform at)
    {
        BufferedImage newImage = new BufferedImage(
            image.getWidth(), image.getHeight(),
            BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        
        g.transform(at);
        g.drawImage(image, 0, 0, null);
        g.dispose();
      
        return newImage;
    }


    public JPanel getRotatedJPanel() {
        updateComponentImg();
        JPanel panel = new JPanel() {

            @Override
            public void paintComponent(Graphics g) { 
                super.paintComponent(g);
                g.drawImage(componentImg, 0, 0, this);
            }

        };
        panel.setSize(this.getWidth(), this.getHeight());
        panel.setLocation(this.getLocation());
        return panel;

    }
}
