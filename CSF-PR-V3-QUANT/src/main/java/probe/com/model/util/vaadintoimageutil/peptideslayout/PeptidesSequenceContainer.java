/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.model.util.vaadintoimageutil.peptideslayout;

import com.itextpdf.text.pdf.codec.Base64;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import org.jfree.chart.encoders.ImageEncoder;
import org.jfree.chart.encoders.ImageEncoderFactory;
import org.jfree.chart.encoders.ImageFormat;
import probe.com.view.core.jfreeutil.StackedBarPeptideComponent;

/**
 *
 * @author yfa041
 */
public class PeptidesSequenceContainer {

    private final int width = 842 - 64;//595 
    private int height = 0;

    public PeptidesSequenceContainer(ProteinInformationDataForExport proteinInfoData, Graphics2D g2d, String resourcesPath) {

        JLabel comparisonsProteinHeaderLabel = new JLabel();
        comparisonsProteinHeaderLabel.setBackground(new java.awt.Color(255, 255, 255));
        comparisonsProteinHeaderLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        comparisonsProteinHeaderLabel.setText(proteinInfoData.getComparisonsTitle());
        comparisonsProteinHeaderLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
        comparisonsProteinHeaderLabel.setSize(width, 30);
        comparisonsProteinHeaderLabel.paint(g2d);
        g2d.translate(0, 30);
        height = 60;
        Map<String, Color> peptidesColorMap = new HashMap<String, Color>();
        peptidesColorMap.put("redstackedlayout", Color.decode("#cc0000"));
        peptidesColorMap.put("midredstackedlayout", Color.decode("#FF7F7F"));
        peptidesColorMap.put("lightbluestackedlayout", new Color(1, 141, 244));
        peptidesColorMap.put("midgreenstackedlayout", Color.decode("#D0F5A9"));
        peptidesColorMap.put("greenstackedlayout", Color.decode("#009900"));

        Color[] colors = new Color[]{Color.RED, Color.BLUE, Color.RED, Color.GREEN};

        for (String key : proteinInfoData.getStudies().keySet()) {
            StudyInfoData info = proteinInfoData.getStudies().get(key);
            JPanel protSeqContainerLayout = new JPanel() {
                @Override
                public boolean isOptimizedDrawingEnabled() {
                    return true;
                }
            };
            protSeqContainerLayout.setBackground(Color.WHITE);
            protSeqContainerLayout.setSize(width, 50 + (info.getLevelsNumber()-1 * 40));
            protSeqContainerLayout.setLayout(new FlowLayout(FlowLayout.LEFT));

            int w = width - info.getCoverageWidth();
            JLabel studyLable1 = initSubLabel(info.getTitle(), w);
            studyLable1.setLocation(5, 5);
            protSeqContainerLayout.add(studyLable1);

            resourcesPath = "D:\\CSF_Files\\Resources";
            File iconResFile;
            switch (info.getTrend()) {
                case -1:
                    iconResFile = new File(resourcesPath, "down.png");
                    break;
                case 1:
                    iconResFile = new File(resourcesPath, "up.png");
                    break;
                default:
                    iconResFile = new File(resourcesPath, "notreg.png");
                    break;
            }
            try {
                Image image = ImageIO.read(iconResFile);
                ImageIcon icon = PeptidesSequenceContainer.this.createImageIcon(image, "");
                JLabel studyLable2 = initSubLabel(info.getSubTitle(), w);
                protSeqContainerLayout.add(studyLable2);
                studyLable2.setIcon(icon);
                studyLable2.setIconTextGap(5);
                studyLable2.setLocation(5, 30);
            } catch (IOException ex) {
                Logger.getLogger(PeptidesSequenceContainer.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (info.getPeptidesInfoList() != null) {
                int y = 20;
                for (StackedBarPeptideComponent peptide : info.getPeptidesInfoList()) {

                    JLabel peptideComponent = initPeptideComponet(peptidesColorMap.get(peptide.getStyleName()), (int) peptide.getWidth(), w + peptide.getX0(), (y + (peptide.getLevel()*40) ));
                    protSeqContainerLayout.add(peptideComponent);
                   
                    
                }
                JPanel protSeqLayout = new JPanel();
                protSeqLayout.setBackground(new Color(242, 242, 242));
                protSeqLayout.setBorder(new LineBorder(new Color(211, 211, 211)));
                protSeqLayout.setLocation(w, 20);
                protSeqLayout.setSize(info.getCoverageWidth(), 20);
                protSeqContainerLayout.add(protSeqLayout);
                protSeqContainerLayout.paint(g2d);
            }

//            JLabel ptm = initPTMComponent(resourcesPath, w + 20 + 100 - 5);
//            protSeqContainerLayout.add(ptm);
            g2d.translate(0, height);

        }

    }

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     *
     * @param img
     * @param description
     * @return
     */
    protected ImageIcon createImageIcon(Image img,
            String description) {
//        java.net.URL imgURL = getClass().getResource(path);
        if (img != null) {
            return new ImageIcon(img, description);
        } else {
            System.err.println("Couldn't find file: ");
            return null;
        }
    }

    private final ImageEncoder in = ImageEncoderFactory.newInstance(ImageFormat.PNG, new Float(0.084666f));

    public String toImage(BufferedImage image) {
        byte[] imageData = null;

        try {

            imageData = in.encode(image);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        String base64 = Base64.encodeBytes(imageData);
        base64 = "data:image/png;base64," + base64;
        return base64;

    }
    private final Font font = new Font("Verdana", Font.PLAIN, 9);

    private JLabel initSubLabel(String str, int width) {
        JLabel lable = new JLabel(str);
        lable.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lable.setFont(font);
        lable.setSize(width, 20);
        return lable;

    }

    private JLabel initPeptideComponet(Color c, int w, int xlocation, int top) {
        JLabel peptideComponent = new JLabel();
        peptideComponent.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        peptideComponent.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        peptideComponent.setLayout(new BorderLayout(1, 1));
        peptideComponent.setBackground(c);
        peptideComponent.setOpaque(true);
        peptideComponent.setSize(w, 20);
        peptideComponent.setLocation(xlocation, top);
        return peptideComponent;
    }

    private JLabel initPTMComponent(String resourcesPath, int xLocation) {
        File res = new File(resourcesPath, "orange_ptm.png");
        JLabel PTMComponent = new JLabel();
        PTMComponent.setSize(10, 10);
        try {
            Image image = ImageIO.read(res);
            ImageIcon icon = PeptidesSequenceContainer.this.createImageIcon(image, "");

            PTMComponent.setIcon(icon);
            PTMComponent.setIconTextGap(0);
            PTMComponent.setLocation(xLocation, 9);
        } catch (IOException ex) {
            Logger.getLogger(PeptidesSequenceContainer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return PTMComponent;

    }

}
