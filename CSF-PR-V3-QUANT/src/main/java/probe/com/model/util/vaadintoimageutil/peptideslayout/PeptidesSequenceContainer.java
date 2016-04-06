/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.model.util.vaadintoimageutil.peptideslayout;

import com.vaadin.ui.AbsoluteLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import probe.com.view.core.jfreeutil.StackedBarPeptideComponent;

/**
 *
 * @author yfa041
 */
public class PeptidesSequenceContainer extends JPanel {

    private int width = 563; //842 - 64;//595 
    private int currentHeight;
    private double resizeFactor = 1;
    private final Map<String, Color> peptidesColorMap;

    public PeptidesSequenceContainer(ProteinInformationDataForExport proteinInfoData, String resourcesPath, int w) {

        width = w;
        peptidesColorMap = new HashMap<String, Color>();
        peptidesColorMap.put("redstackedlayout", Color.decode("#cc0000"));
        peptidesColorMap.put("midredstackedlayout", Color.decode("#FF7F7F"));
        peptidesColorMap.put("lightbluestackedlayout", new Color(1, 141, 244));
        peptidesColorMap.put("midgreenstackedlayout", Color.decode("#D0F5A9"));
        peptidesColorMap.put("greenstackedlayout", Color.decode("#009900"));
        peptidesColorMap.put("graystackedlayout", Color.decode("#737373"));
        this.setBackground(Color.WHITE);
        this.setLayout(null);

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

        for (String key : proteinInfoData.getStudies().keySet()) {
            StudyInfoData info = proteinInfoData.getStudies().get(key);
            JPanel protSeqContainerLayout = new JPanel() {
                @Override
                public boolean isOptimizedDrawingEnabled() {
                    return false;
                }
            };
            protSeqContainerLayout.setBackground(Color.WHITE);
            int studyHeight = 35;// + ((Math.max(info.getLevelsNumber(), 1) - 1) * 30);
            protSeqContainerLayout.setSize(width, studyHeight);
            protSeqContainerLayout.setLayout(null);
            int labelWidth = 145;//width - info.getCoverageWidth();
            resizeFactor = (width - 210) / (double) info.getCoverageWidth();
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
//
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
                //init peptides panel
                JPanel seqContainer = initPeptidePanel(filteredSet.values(), (int) ((double) info.getCoverageWidth() * resizeFactor), res);
                seqContainer.setLocation(labelWidth, y);
                protSeqContainerLayout.add(seqContainer);
                studyHeight += seqContainer.getHeight();
                protSeqContainerLayout.setSize(width, studyHeight);
//                y += seqContainer.getHeight();
//                studyHeight = studyHeight-35+seqContainer.getHeight();
                System.out.println("at protSeqContainerLayout " + protSeqContainerLayout.getHeight());

//
            }
            protSeqContainerLayout.setLocation(0, currentHeight);
            this.currentHeight += studyHeight + 10;
            this.setSize(width, currentHeight);
            this.add(protSeqContainerLayout);

//            protSeqContainerLayout.paint(g2d);
//            g2d.translate(0, currentHeight);
        }

    }

    private JPanel initPeptidePanel(Collection<StackedBarPeptideComponent> peptides, int w, File res) {
        JPanel seqContainerLayout = new JPanel();
        seqContainerLayout.setBackground(Color.WHITE);
        int studyHeight = 0;
        seqContainerLayout.setLayout(null);

        JPanel highPeptidesSequencesBar = new JPanel();
        highPeptidesSequencesBar.setLayout(null);
        highPeptidesSequencesBar.setLocation(50, 0);
//        seqContainerLayout.add(highPeptidesSequencesBar);

        JPanel coveragePeptidesSequencesBar = new JPanel();
        coveragePeptidesSequencesBar.setLayout(null);
        coveragePeptidesSequencesBar.setLocation(50, 0);
        coveragePeptidesSequencesBar.setSize(w-100,15);

        JPanel stablePeptidesSequencesBar = new JPanel();
        stablePeptidesSequencesBar.setLayout(null);
        stablePeptidesSequencesBar.setLocation(50, 0);
        stablePeptidesSequencesBar.add(highPeptidesSequencesBar);

        JPanel lowPeptidesSequencesBar = new JPanel();
        lowPeptidesSequencesBar.setLayout(null);
        lowPeptidesSequencesBar.setLocation(50, 0);
        lowPeptidesSequencesBar.add(highPeptidesSequencesBar);

        LinkedHashSet<StackedBarPeptideComponent> highSet = new LinkedHashSet<StackedBarPeptideComponent>();
        LinkedHashSet<StackedBarPeptideComponent> stableSet = new LinkedHashSet<StackedBarPeptideComponent>();
        for (StackedBarPeptideComponent peptideLayout : peptides) {
            if (peptideLayout.getParam("trend").toString().equalsIgnoreCase("increased")) {
                highSet.add(peptideLayout);
            } else if (peptideLayout.getParam("trend").toString().equalsIgnoreCase("equal") || peptideLayout.getParam("trend").toString().equalsIgnoreCase("noquant")) {
                stableSet.add(peptideLayout);
            }

        }

        for (StackedBarPeptideComponent peptideLayout : peptides) {

            if (peptideLayout.getParam("trend").toString().equalsIgnoreCase("decreased")) {
                stableSet.add(peptideLayout);
            }

        }

//        ptmsLayoutMap.clear();
        int y = 0;
//        if (!highSet.isEmpty()) {
//            initPeptidesStackedBarComponentsLayout(highSet, highPeptidesSequencesBar, coveragePeptidesSequencesBar, true,w-100);
//            y = (int) highPeptidesSequencesBar.getHeight() +10;
//            highPeptidesSequencesBar.setLocation(50, 0);
//            seqContainerLayout.add(highPeptidesSequencesBar);
//            studyHeight+= y;
//            
//
//        }
        coveragePeptidesSequencesBar.setLocation(50,y);
        coveragePeptidesSequencesBar.add(highPeptidesSequencesBar);
        JLabel nTerminalLabel = new JLabel("N-");
        nTerminalLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nTerminalLabel.setFont(new Font("Verdana", Font.PLAIN, 11));
        nTerminalLabel.setSize(50, 15);
        nTerminalLabel.setLocation(0, y);
        nTerminalLabel.setForeground(Color.lightGray);
        seqContainerLayout.add(nTerminalLabel);

        JLabel cTerminalLabel = new JLabel("-C");
        cTerminalLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cTerminalLabel.setFont(new Font("Verdana", Font.PLAIN, 11));
        cTerminalLabel.setForeground(Color.lightGray);
        cTerminalLabel.setSize(50, 15);
        cTerminalLabel.setLocation(w - 50, y);
        seqContainerLayout.add(cTerminalLabel);

        y += 20;
        studyHeight += 20;

//        this.addComponent(coveragePeptidesSequencesBar, "left: " + (19) + "px; top: " + (top) + "px;");
//        this.addTerminalLabels(top, (int) this.getWidth());
//        top += 12;
//
//        if (!stableSet.isEmpty()) {
//            initPeptidesStackedBarComponentsLayout(stableSet, stablePeptidesSequencesBar, false);
//            this.addComponent(stablePeptidesSequencesBar, "left: " + (20) + "px; top: " + (top) + "px;");
//            top += stablePeptidesSequencesBar.getHeight();
//        }
//
//        top += 5;
//        this.setHeight(top + "px");
//        ptmAvailable = !ptmsLayoutMap.isEmpty();
//        int y = 0;
//        for (StackedBarPeptideComponent peptide : peptides) {
//
//            int step = y + (peptide.getLevel() * 30);
//            int x = 50 + (int) ((double) peptide.getX0() * resizeFactor);
//            JLabel peptideComponent = initPeptideComponet(peptidesColorMap.get(peptide.getStyleName()), (int) (peptide.getWidth() * resizeFactor), x, step);
//
//            seqContainerLayout.add(peptideComponent);
//            if (peptide.isPtmAvailable()) {
//                JLabel ptm = initPTMComponent(res, x + (int) (peptideComponent.getSize().getWidth() / 2.0) - 5, step - 10, peptide.getPtmLayout().getStyleName());
//                seqContainerLayout.add(ptm);
//            }
//            System.out.println("peptide level " + peptide.getLevel() + "  peptide.getX0() " + peptide.getX0() + "  peptide.getStyleName() " + peptide.getStyleName() + "  peptide.getWidth() " + peptide.getWidth());
//        }
//        JPanel protSeqLayout = new JPanel();
//        protSeqLayout.setBackground(new Color(242, 242, 242));
//        Border b = new LineBorder(new Color(211, 211, 211));
//        protSeqLayout.setBorder(b);
//        protSeqLayout.setLocation(50, y);
//        protSeqLayout.setSize(w - 100, 15);
//        seqContainerLayout.add(protSeqLayout);
//        y += 20;
//        studyHeight += 20;
//
//        JPanel protSeqLayout2 = new JPanel();
//        protSeqLayout2.setBackground(new Color(242, 242, 242));
////        Border b = new LineBorder(new Color(211, 211, 211));
//        protSeqLayout2.setBorder(b);
//        protSeqLayout2.setLocation(50, y);
//        protSeqLayout2.setSize(w - 100, 15);
//        seqContainerLayout.add(protSeqLayout2);

       

//        JPanel protSeqLayout3 = new JPanel();
//        protSeqLayout3.setBackground(new Color(242, 242, 242));
////        Border b = new LineBorder(new Color(211, 211, 211));
//        protSeqLayout3.setBorder(b);
//        protSeqLayout3.setLocation(50, y);
//        protSeqLayout3.setSize(w - 100, 15);
//        seqContainerLayout.add(protSeqLayout3);
//
//        y += 20;
//        studyHeight += 20;
        seqContainerLayout.setSize(w, studyHeight);

        return seqContainerLayout;

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

//    private final ImageEncoder in = ImageEncoderFactory.newInstance(ImageFormat.PNG, new Float(0.084666f));
//    public String toImage(BufferedImage image) {
//        byte[] imageData = null;
//
//        try {
//
//            imageData = in.encode(image);
//        } catch (Exception e) {
//            System.out.println(e.getLocalizedMessage());
//        }
//
//        String base64 = Base64.encodeBytes(imageData);
//        base64 = "data:image/png;base64," + base64;
//        return base64;
//
//    }
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

    public static void main(String[] args) {
        ProteinInformationDataForExport dummyData = new ProteinInformationDataForExport();
        dummyData.setSequence("MKEASGVGGGHSPMCPPHCHMPPGPAGEWPGATVQQPRQRAPTALLQPDAAGPGGGGVSA\n"
                + "GVAVLGACPSASEGVLPRPPGRSAPQPPEYPGRPDMAVPGWSAMVRSRLTATSTSQATDH\n"
                + "TFLQKSHYHHGDHPSYAKPRLPLPVFTVRHYAGTVTYQVHKFLNRNRDQLDPAVVEMLGQ\n"
                + "SQLQLVGSLFQEAEPQSRGGRGRPTLASRFQQALEDLIARLGRSHVYFIQCLTPNPGKLP\n"
                + "GLFDVGHVTEQLHQAAILEAVGTRSANFPVRVPFEAFLASFQALGSEGQEDLSDREKCGA\n"
                + "VLSQVLGAESPLYHLGATKVLLQEQGWQRLEELRDQQRSQALVDLHRSFHTCISRQRVLP\n"
                + "RMQARMRGFQARKRYLRRRAALGQLNTILLVAQPLLQRRQRLQLGRWQGWHSSERALERV\n"
                + "PSMELGRLEIPAELAVMLKTAESHRDALAGSITECLPPEVPARPSLTLPADIDLFPFSSF\n"
                + "VAIGFQEPSLPRPGQPLAKPLTQLDGDNPQRALDINKVMLRLLGDGSLESWQRQIMGAYL\n"
                + "VRQGQCWPGLRNELFSQLVAQLWQNPDEQQSQRGWALMAVLLSAFPPLPVLQKPLLKFVS\n"
                + "DQAPRGMAALCQHKLLGALEQSQLASGATRAHPPTQLEWLAGWRRGRMALDVFTFSEECY\n"
                + "SAEVESWTTGEQLAGWILQSRGLEAPPRGWSVSLHSRDAWQDLAGCDFVLDLISQTEDLG\n"
                + "DPARPRSYPITPLGSAEAIPLAPGIQAPSLPPGPPPGPAPTLPSRDHTGEVQRSGSLDGF\n"
                + "LDQIFQPVISSGLSDLEQSWALSSRMKGGGAIGPTQQGYPMVYPGMIQMPAYQPGMVPAP\n"
                + "MPMMPAMGTVPAMPAMVVPPQPPLPSLDAGQLAVQQQNFIQQQALILAQQMTAQAMSLSL\n"
                + "EQQMQQRQQQARASEAASQASPSAVTSKPRKPPTPPEKPQRDLGSEGGCLRETSEEAEDR\n"
                + "PYQPKSFQQKRNYFQRMGQPQITVRTMKPPAKVHIPQGEAQEEEEEEEEEEEQEEQEVET\n"
                + "RAVPSPPPPPIVKKPLKQGGAKAPKEAEAEPAKETAAKGHGQGPAQGRGTVVRSSDSKPK\n"
                + "RPQPSREIGNIIRMYQSRPGPVPVPVQPSRPPKAFLRKIDPKDEALAKLGINGAHSSPPM\n"
                + "LSPSPGKGPPPAVAPRPKAPLQLGPSSSIKEKQGPLLDLFGQKLPIAHTPPPPPAPPLPL\n"
                + "PEDPGTLSAERRCLTQPVEDQGVSTQLLAPSGSVCFSYTGTPWKLFLRKEVFYPRENFSH\n"
                + "PYYLRLLCEQILRDTFSESCIRISQNERRKMKDLLGGLEVDLDSLTTTEDSVKKRIVVAA\n"
                + "RDNWANYFSRFFPVSGESGSDVQLLAVSHRGLRLLKVTQGPGLRPDQLKILCSYSFAEVL\n"
                + "GVECRGGSTLELSLKSEQLVLHTARARAIEALVELFLNELKKDSGYVIALRSYITDNCSL\n"
                + "LSFHRGDLIKLLPVATLEPGWQFGSAGGRSGLFPADIVQPAAAPDFSFSKEQRSGWHKGQ\n"
                + "LSNGEPGLARWDRASEVRKMGEGQAEARPA");

        dummyData.setComparisonsTitle("SPMS / Healthy* (#Datasets 1/1)");

        Map studiesMap = new LinkedHashMap<String, StudyInfoData>();
        StudyInfoData exportData = new StudyInfoData();
        exportData.setCoverageWidth(610);
        exportData.setSubTitle("#Patients (32)");
        exportData.setTrend(-1);
        exportData.setTitle("Jia, Yan, et al.");
        exportData.setLevelsNumber(1);
        StackedBarPeptideComponent peptide = new StackedBarPeptideComponent(292, 4, "key", "");
        peptide.setX0(292);
        peptide.setLevel(0);
        peptide.setWidth(4 + "px");
        peptide.setStyleName("greenstackedlayout");
        peptide.setParam(("trend"),("increased"));
        List<StackedBarPeptideComponent> l = new ArrayList<StackedBarPeptideComponent>();
        l.add(peptide);
        exportData.setPeptidesInfoList(l);

        studiesMap.put("" + 1, exportData);

        dummyData.setStudies(studiesMap);

        PeptidesSequenceContainer con = new PeptidesSequenceContainer(dummyData, "D:\\CSF_Files", 1500);
        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setSize(1500, 1500);
        frame.add(con);

    }

    private void initPeptidesStackedBarComponentsLayout(LinkedHashSet<StackedBarPeptideComponent> stackedBarComponents, JPanel peptidesComponentsContainer, JPanel coverageContainer, boolean flip, int w) {



        peptidesComponentsContainer.setBackground(Color.CYAN);
        Border b = new LineBorder(new Color(211, 211, 211));
        peptidesComponentsContainer.setBorder(b);
        peptidesComponentsContainer.setSize(w, 15);








//        int top = 0;
//        List< StackedBarPeptideComponent> initLevel = new ArrayList<StackedBarPeptideComponent>(stackedBarComponents);
//        List< StackedBarPeptideComponent> updatedLevel = new ArrayList<StackedBarPeptideComponent>(stackedBarComponents);
//        List< StackedBarPeptideComponent> nextLevel = new ArrayList<StackedBarPeptideComponent>();
//
//        boolean existedPeptides = false;
//        boolean intersect = true;
//        while (intersect) {
//            intersect = false;
//            for (int x = 0; x < initLevel.size() && initLevel.size() > 1; x++) {
//                StackedBarPeptideComponent pepBarComp = (StackedBarPeptideComponent) initLevel.get(x);
//                for (int y = 0; y < initLevel.size(); y++) {
//                    if (y <= x) {
//                        continue;
//                    }
//                    StackedBarPeptideComponent pepBarComp2 = (StackedBarPeptideComponent) initLevel.get(y);
//                    boolean check;
//                    if (pepBarComp.getX0() > pepBarComp2.getX0()) {
//                        check = checkIntersect(pepBarComp2, pepBarComp);
//                    } else {
//                        check = checkIntersect(pepBarComp, pepBarComp2);
//                    }
//                    if (check) {
//                        intersect = true;
//                        if (pepBarComp.getWidthArea() > pepBarComp2.getWidthArea()) {
//                            updatedLevel.remove(y);
//                            pepBarComp2.setLevel(pepBarComp2.getLevel() + 1);
//                            nextLevel.add(pepBarComp2);
//                        } else if (pepBarComp.getWidthArea() == pepBarComp2.getWidthArea()) {
//                            if (!pepBarComp2.isSignificant()) {
//                                updatedLevel.remove(y);
//                                pepBarComp2.setLevel(pepBarComp2.getLevel() + 1);
//                                nextLevel.add(pepBarComp2);
//                            } else {
//                                updatedLevel.remove(x);
//                                pepBarComp.setLevel(pepBarComp.getLevel() + 1);
//                                nextLevel.add(pepBarComp);
//
//                            }
//
//                        } else {
//                            updatedLevel.remove(x);
//                            pepBarComp.setLevel(pepBarComp.getLevel() + 1);
//                            nextLevel.add(pepBarComp);
//                        }
//                        initLevel.clear();
//                        initLevel.addAll(updatedLevel);
//
//                        break;
//                    }
//
//                }
//                if (intersect) {
//                    break;
//                }
//
//            }
//
//            if (!intersect) {
//                for (StackedBarPeptideComponent pepBarComp : updatedLevel) {
//                    peptidesComponentsContainer.addComponent(pepBarComp, "left: " + (pepBarComp.getX0() - 20) + "px; top: " + (top + 10) + "px;");
//                    existedPeptides = true;
//                    if (pepBarComp.isPtmAvailable()) {
//                        if (flip) {
//                            peptidesComponentsContainer.addComponent(pepBarComp.getPtmLayout(), "left: " + (pepBarComp.getX0() - 20 + (pepBarComp.getWidth() / 2) - 5) + "px; top: " + (top + 21) + "px;");
//                        } else {
//                            peptidesComponentsContainer.addComponent(pepBarComp.getPtmLayout(), "left: " + (pepBarComp.getX0() - 20 + (pepBarComp.getWidth() / 2) - 5) + "px; top: " + (top - 4) + "px;");
//                        }
//                        pepBarComp.getPtmLayout().setVisible(true);
//                        ptmsLayoutMap.add(pepBarComp.getPtmLayout());
//                    }
//
//                }
//                updatedLevel.clear();
//                updatedLevel.addAll(initLevel);
//                initLevel.clear();
//
//            }
//            if (!intersect && !nextLevel.isEmpty()) {
//
//                initLevel.clear();
//                updatedLevel.clear();
//                initLevel.addAll(nextLevel);
//                updatedLevel.addAll(nextLevel);
//                nextLevel.clear();
//                intersect = true;
//                top = top + 20;
//            }
//
//        }
//        if (stackedPeptides.isEmpty()) {
//            stackedPeptides.addAll(stackedBarComponents);
//        }
//        if (existedPeptides) {
//            top = top + 40;
//            peptidesComponentsContainer.setHeight(Math.max(40, top) + "px");
//        } else {
//            peptidesComponentsContainer.setHeight("0px");
//        }

    }

    private boolean checkIntersect(StackedBarPeptideComponent smallXComp, StackedBarPeptideComponent bigXComp) {
        int area = smallXComp.getX0() + smallXComp.getWidthArea() - 1;
        boolean test = bigXComp.getX0() <= area;
        int endSmall = (Integer) smallXComp.getParam("end");
        int startBig = (Integer) bigXComp.getParam("start");

        if (test) {

            if (startBig > endSmall) {
                bigXComp.setX0(area + 1);
                return false;
            }

        }
        if (!test) {

            if (endSmall + 1 == startBig) {
                bigXComp.setX0(area + 1);
            }

        }

        return test;

    }
//

}
