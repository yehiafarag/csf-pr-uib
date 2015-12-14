/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.model.util.vaadintoimageutil.peptideslayout;

import com.itextpdf.text.pdf.codec.Base64;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
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
public class PeptidesSequenceContainer extends JPanel {

    private final int width = 563; //842 - 64;//595 
    private int currentHeight;
    private double resizeFactor = 1;

    public PeptidesSequenceContainer(ProteinInformationDataForExport proteinInfoData, String resourcesPath) {

        Map<String, Color> peptidesColorMap = new HashMap<String, Color>();
        peptidesColorMap.put("redstackedlayout", Color.decode("#cc0000"));
        peptidesColorMap.put("midredstackedlayout", Color.decode("#FF7F7F"));
        peptidesColorMap.put("lightbluestackedlayout", new Color(1, 141, 244));
        peptidesColorMap.put("midgreenstackedlayout", Color.decode("#D0F5A9"));
        peptidesColorMap.put("greenstackedlayout", Color.decode("#009900"));
         peptidesColorMap.put("graystackedlayout", Color.decode("#737373"));
        this.setBackground(Color.WHITE);
        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        File res = new File(resourcesPath, "Resources");
        JLabel comparisonsProteinHeaderLabel = new JLabel();
        comparisonsProteinHeaderLabel.setBackground(Color.WHITE);
        comparisonsProteinHeaderLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        comparisonsProteinHeaderLabel.setText(proteinInfoData.getComparisonsTitle());
        comparisonsProteinHeaderLabel.setFont(new Font("Verdana", Font.PLAIN, 11));
        comparisonsProteinHeaderLabel.setSize(width, 30);
        comparisonsProteinHeaderLabel.setLocation(0, 10);
        currentHeight = 40;
        this.setSize(width, currentHeight);
        this.add(comparisonsProteinHeaderLabel);
//        comparisonsProteinHeaderLabel.paint(g2d);
//        g2d.translate(0, 30);

        for (String key : proteinInfoData.getStudies().keySet()) {
            StudyInfoData info = proteinInfoData.getStudies().get(key);
            JPanel protSeqContainerLayout = new JPanel() {
                @Override
                public boolean isOptimizedDrawingEnabled() {
                    return false;
                }
            };
            protSeqContainerLayout.setBackground(Color.WHITE);
            int studyHeight = 35 + ((Math.max(info.getLevelsNumber(), 1) - 1) * 30);
            protSeqContainerLayout.setSize(width, studyHeight);

            protSeqContainerLayout.setLayout(new FlowLayout(FlowLayout.LEFT));
            int labelWidth = 145;//width - info.getCoverageWidth();
            resizeFactor = 380.0 / (double) info.getCoverageWidth();
            JLabel studyLable1 = initSubLabel(info.getTitle(), labelWidth);
            studyLable1.setLocation(0, 10);
            protSeqContainerLayout.add(studyLable1);
            File iconResFile;
            switch (info.getTrend()) {
                case -1:
                    iconResFile = new File(res, "down.png");
                    break;
                case 1:
                    iconResFile = new File(res, "up.png");
                    break;
                default:
                    iconResFile = new File(res, "notreg.png");
                    break;
            }
            try {
                Image image = ImageIO.read(iconResFile);
                ImageIcon icon = PeptidesSequenceContainer.this.createImageIcon(image, "");
                JLabel studyLable2 = initSubLabel(info.getSubTitle(), labelWidth);
                protSeqContainerLayout.add(studyLable2);
                studyLable2.setIcon(icon);
                studyLable2.setIconTextGap(5);
                studyLable2.setLocation(0, 25);
            } catch (IOException ex) {
                Logger.getLogger(PeptidesSequenceContainer.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (info.getPeptidesInfoList() == null) {
                JLabel noInfoLabel = initSubLabel("No Peptide Information Available", 200);
                noInfoLabel.setLocation(155, 10);
                protSeqContainerLayout.add(noInfoLabel);

            }

            if (info.getPeptidesInfoList() != null) {
                int y = 20;
                Map<String, StackedBarPeptideComponent> filteredSet = new LinkedHashMap<String, StackedBarPeptideComponent>();
                for (StackedBarPeptideComponent peptide : info.getPeptidesInfoList()) {
                    String localKey = peptide.getPeptideKey();
                    if (filteredSet.containsKey(localKey)) {
                        continue;
                    }
                    filteredSet.put(localKey, peptide);

                }
                for (StackedBarPeptideComponent peptide : filteredSet.values()) {
                    int step = y + (peptide.getLevel() * 30);
                    int x = labelWidth + (int) ((double) peptide.getX0() * resizeFactor);
                    JLabel peptideComponent = initPeptideComponet(peptidesColorMap.get(peptide.getStyleName()), (int) (peptide.getWidth() * resizeFactor), x, step);

                    protSeqContainerLayout.add(peptideComponent);
                    if (peptide.isPtmAvailable()) {
                        JLabel ptm = initPTMComponent(res, x + (int) (peptideComponent.getSize().getWidth() / 2.0) - 5, step - 10, peptide.getPtmLayout().getStyleName());
                        protSeqContainerLayout.add(ptm);
                    }
//                    if (currentHeight < (step + 15)) {
//                        currentHeight = step + 15;
//                    }
                }
                JPanel protSeqLayout = new JPanel();
                protSeqLayout.setBackground(new Color(242, 242, 242));
                protSeqLayout.setBorder(new LineBorder(new Color(211, 211, 211)));
                protSeqLayout.setLocation(labelWidth, 20);
                protSeqLayout.setSize((int) ((double) info.getCoverageWidth() * resizeFactor), 15);
                protSeqContainerLayout.add(protSeqLayout);

            }
            protSeqContainerLayout.setLocation(0, currentHeight);
            this.currentHeight += studyHeight + 10;
            this.setSize(width, currentHeight);
            this.add(protSeqContainerLayout);

//            protSeqContainerLayout.paint(g2d);
//            g2d.translate(0, currentHeight);
        }

    }

    public int getCurrentHeight() {
        return currentHeight;
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
        lable.setSize(width, 10);
        return lable;

    }

    private JLabel initPeptideComponet(Color c, int w, int xlocation, int top) {
        JLabel peptideComponent = new JLabel();
        peptideComponent.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LineBorder border;
//        if (w > 3) {
            border = new LineBorder(Color.DARK_GRAY, 1);

//        }else{
//         border = new LineBorder(c, 1);
//        }
        peptideComponent.setBorder(border);
        peptideComponent.setBackground(c);
        peptideComponent.setOpaque(true);
        peptideComponent.setSize(w, 15);
        peptideComponent.setLocation(xlocation, top);

        return peptideComponent;
    }

    private JLabel initPTMComponent(File res, int xLocation, int yLocation, String ptmType) {

        File iconFile = null;
        if (ptmType.equalsIgnoreCase("ptmglycosylation")) {
            System.out.println("res " + res.exists());
            iconFile = new File(res, ptmType + ".png");

        }

        JLabel PTMComponent = new JLabel();
        PTMComponent.setSize(10, 10);
        try {
            Image image = ImageIO.read(iconFile);
            ImageIcon icon = PeptidesSequenceContainer.this.createImageIcon(image, "");

            PTMComponent.setIcon(icon);
            PTMComponent.setIconTextGap(0);
            PTMComponent.setLocation(xLocation, yLocation);
        } catch (IOException ex) {
            Logger.getLogger(PeptidesSequenceContainer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return PTMComponent;

    }

}
