/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.components.peptideviewsubcomponents;

import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import no.uib.probe.csf.pr.touch.view.core.InformationButton;
import no.uib.probe.csf.pr.touch.view.core.StackedBarPeptideComponent;

/**
 *
 * @author Yehia Farag
 *
 * this class represents the protein sequence layout
 */
public class PeptideSequenceContainer extends AbsoluteLayout {

    private final AbsoluteLayout highPeptidesSequencesBar, lowPeptidesSequencesBar, coveragePeptidesSequencesBar, stablePeptidesSequencesBar, unMappedPeptidesSequencesBar;
    private final LinkedHashSet<StackedBarPeptideComponent> allPeptidesStackedBarComponentsMap;
    private final Set<VerticalLayout> ptmsLayoutMap = new HashSet<>();
    private final List< StackedBarPeptideComponent> stackedPeptides = new ArrayList<>();
    private boolean ptmAvailable = false;
    private final boolean smallScreen;
    private final String proteinName;

    public PeptideSequenceContainer(int width, LinkedHashSet<StackedBarPeptideComponent> allPeptidesStackedBarComponentsMap, boolean smallScreen, String proteinName, String sequence) {
        this.allPeptidesStackedBarComponentsMap = allPeptidesStackedBarComponentsMap;
        this.setVisible(true);
        this.setWidth(width, Unit.PIXELS);
        this.smallScreen = smallScreen;
        this.proteinName = proteinName;

        highPeptidesSequencesBar = new AbsoluteLayout();
        highPeptidesSequencesBar.setWidth((width - 40), Unit.PIXELS);

        highPeptidesSequencesBar.setStyleName("flipvertically");
        this.addComponent(highPeptidesSequencesBar, "left: " + (20) + "px; top: " + (0) + "px;");

        coveragePeptidesSequencesBar = new AbsoluteLayout();
        coveragePeptidesSequencesBar.setWidth((width - 40), Unit.PIXELS);
        coveragePeptidesSequencesBar.setHeight(17, Unit.PIXELS);
        coveragePeptidesSequencesBar.setStyleName("sequencecontainer");

        stablePeptidesSequencesBar = new AbsoluteLayout();
        stablePeptidesSequencesBar.setWidth((width - 40), Unit.PIXELS);
        stablePeptidesSequencesBar.setHeight(15, Unit.PIXELS);

        lowPeptidesSequencesBar = new AbsoluteLayout();
        lowPeptidesSequencesBar.setWidth((width - 40), Unit.PIXELS);

        unMappedPeptidesSequencesBar = new AbsoluteLayout();
        unMappedPeptidesSequencesBar.setWidth((width - 40), Unit.PIXELS);
        unMappedPeptidesSequencesBar.addStyleName("unmappedpeptidescontainer");

        initLayout(sequence);

    }

    private void addTerminalLabels(int top, int width) {

        VerticalLayout nTerminalEdge = new VerticalLayout();
        nTerminalEdge.setWidth(19, Unit.PIXELS);
        nTerminalEdge.setHeight(15, Unit.PIXELS);
        nTerminalEdge.setStyleName("terminal");
        Label nLabel = new Label("N");
        nLabel.setWidth(10, Unit.PIXELS);
        nLabel.setStyleName("ntermlayout");
        nTerminalEdge.addComponent(nLabel);

        this.addComponent(nTerminalEdge, "left: " + (0) + "px; top: " + (top) + "px;");

        VerticalLayout cTerminalEdge = new VerticalLayout();
        cTerminalEdge.setWidth(20, Unit.PIXELS);
        cTerminalEdge.setHeight(15, Unit.PIXELS);
        cTerminalEdge.setStyleName("terminal");
        Label cLabel = new Label("C");
        cLabel.setStyleName("ctermlayout");
        cTerminalEdge.addComponent(cLabel);
        cLabel.setWidth(10, Unit.PIXELS);
        cTerminalEdge.setComponentAlignment(cLabel, Alignment.TOP_RIGHT);
        this.addComponent(cTerminalEdge, "left: " + (width - 21) + "px; top: " + (top) + "px;");

    }

    private void initLayout(String sequence) {

        LinkedHashSet<StackedBarPeptideComponent> unMappedSet = new LinkedHashSet<>();
        LinkedHashSet<StackedBarPeptideComponent> highSet = new LinkedHashSet<>();
        LinkedHashSet<StackedBarPeptideComponent> stableSet = new LinkedHashSet<>();
        StringBuilder tempSequence = new StringBuilder();
        allPeptidesStackedBarComponentsMap.stream().forEach((peptideLayout) -> {
            String pepSeq = peptideLayout.getParam("sequence").toString();
            if (!sequence.contains(pepSeq)) {
                unMappedSet.add(peptideLayout);
                peptideLayout.setDescription("" + 1 + "-" + pepSeq + "-" + pepSeq.length() + "");
                tempSequence.append(pepSeq);
            } else if (peptideLayout.getParam("trend").toString().equalsIgnoreCase("increased")) {
                highSet.add(peptideLayout);
            } else if (peptideLayout.getParam("trend").toString().equalsIgnoreCase("equal") || peptideLayout.getParam("trend").toString().equalsIgnoreCase("noquant")) {
                stableSet.add(peptideLayout);
            }
        });

        allPeptidesStackedBarComponentsMap.stream().filter((peptideLayout) -> (peptideLayout.getParam("trend").toString().equalsIgnoreCase("decreased"))).forEach((peptideLayout) -> {
            if (!unMappedSet.contains(peptideLayout)) {
                stableSet.add(peptideLayout);
            }
        });

        ptmsLayoutMap.clear();
        int top = 0;
        if (!highSet.isEmpty()) {
            initPeptidesStackedBarComponentsLayout(highSet, highPeptidesSequencesBar, true);
            top = (int) highPeptidesSequencesBar.getHeight() - 5;
        }
        this.addComponent(coveragePeptidesSequencesBar, "left: " + (19) + "px; top: " + (top) + "px;");
        this.addTerminalLabels(top, (int) this.getWidth());
        top += 12;

        if (!stableSet.isEmpty()) {
            initPeptidesStackedBarComponentsLayout(stableSet, stablePeptidesSequencesBar, false);
            this.addComponent(stablePeptidesSequencesBar, "left: " + (20) + "px; top: " + (top) + "px;");
            top += stablePeptidesSequencesBar.getHeight();
        }

        if (!unMappedSet.isEmpty()) {

            int left = 5;
            int topLocation = 5;
            unMappedPeptidesSequencesBar.setHeight(20, Unit.PIXELS);

            InformationButton info = new InformationButton("Note that the protein sequence for this protein has been recently altered in UniProt. The following peptide sequences can therefore no longer be mapped to the current canonical protein sequence.", false);

            info.updateIcon(null);
            info.setWidth(55, Unit.PIXELS);
            Label titleLabel = new Label("Unmapped:");
            titleLabel.addStyleName("underline");
            info.addComponent(titleLabel);
            unMappedPeptidesSequencesBar.addComponent(info, "left: " + (left) + "px; top: " + topLocation + "px;");
            left += 60;
            topLocation = 3;
            for (StackedBarPeptideComponent peptide : unMappedSet) {
                unMappedPeptidesSequencesBar.addComponent(peptide, "left: " + (left) + "px; top: " + topLocation + "px;");
                left += peptide.getWidthArea() + 5;
                if (left >= unMappedPeptidesSequencesBar.getWidth()) {
                    topLocation += 12;
                    left = 65;

                }

            }
            unMappedPeptidesSequencesBar.setHeight(unMappedPeptidesSequencesBar.getHeight() + topLocation, Unit.PIXELS);
            this.addComponent(unMappedPeptidesSequencesBar, "left: " + (20) + "px; top: " + (top) + "px;");
            top += unMappedPeptidesSequencesBar.getHeight();

        }

        top += 5;
        this.setHeight(top, Unit.PIXELS);
        ptmAvailable = !ptmsLayoutMap.isEmpty();
        checkAndMerge(sequence);

    }

    public boolean isPtmAvailable() {
        return ptmAvailable;
    }

    public void showPtms(boolean show) {
        ptmsLayoutMap.stream().forEach((ptmLayout) -> {
            ptmLayout.setVisible(show);
        });

    }

    private void initPeptidesStackedBarComponentsLayout(LinkedHashSet<StackedBarPeptideComponent> stackedBarComponents, AbsoluteLayout peptidesComponentsContainer, boolean flip) {
        int top = 0;
        List< StackedBarPeptideComponent> initLevel = new ArrayList<>(stackedBarComponents);
        List< StackedBarPeptideComponent> updatedLevel = new ArrayList<>(stackedBarComponents);
        List< StackedBarPeptideComponent> nextLevel = new ArrayList<>();

        boolean existedPeptides = false;
        boolean intersect = true;
        while (intersect) {
            intersect = false;
            for (int x = 0; x < initLevel.size() && initLevel.size() > 1; x++) {
                StackedBarPeptideComponent pepBarComp = (StackedBarPeptideComponent) initLevel.get(x);
                for (int y = 0; y < initLevel.size(); y++) {
                    if (y <= x) {
                        continue;
                    }
                    StackedBarPeptideComponent pepBarComp2 = (StackedBarPeptideComponent) initLevel.get(y);
                    boolean check;
                    if (pepBarComp.getX0() > pepBarComp2.getX0()) {
                        check = checkIntersect(pepBarComp2, pepBarComp);
                    } else {
                        check = checkIntersect(pepBarComp, pepBarComp2);
                    }
                    if (check) {
                        intersect = true;
                        if (pepBarComp.getWidthArea() > pepBarComp2.getWidthArea()) {
                            updatedLevel.remove(y);
                            pepBarComp2.setLevel(pepBarComp2.getLevel() + 1);
                            nextLevel.add(pepBarComp2);
                        } else if (pepBarComp.getWidthArea() == pepBarComp2.getWidthArea()) {
                            if (!pepBarComp2.isSignificant()) {
                                updatedLevel.remove(y);
                                pepBarComp2.setLevel(pepBarComp2.getLevel() + 1);
                                nextLevel.add(pepBarComp2);
                            } else {
                                updatedLevel.remove(x);
                                pepBarComp.setLevel(pepBarComp.getLevel() + 1);
                                nextLevel.add(pepBarComp);

                            }

                        } else {
                            updatedLevel.remove(x);
                            pepBarComp.setLevel(pepBarComp.getLevel() + 1);
                            nextLevel.add(pepBarComp);
                        }
                        initLevel.clear();
                        initLevel.addAll(updatedLevel);

                        break;
                    }

                }
                if (intersect) {
                    break;
                }

            }

            if (!intersect) {
                for (StackedBarPeptideComponent pepBarComp : updatedLevel) {
                    peptidesComponentsContainer.addComponent(pepBarComp, "left: " + Math.max((pepBarComp.getX0() - 20), 0) + "px; top: " + (top + 10) + "px;");

                    existedPeptides = true;
                    if (pepBarComp.isPtmAvailable()) {
                        if (flip) {
                            peptidesComponentsContainer.addComponent(pepBarComp.getPtmLayout(), "left: " + Math.max((pepBarComp.getX0() - 20 + (pepBarComp.getWidth() / 2) - 5), 0) + "px; top: " + (top + 21) + "px;");
                        } else {
                            peptidesComponentsContainer.addComponent(pepBarComp.getPtmLayout(), "left: " + Math.max((pepBarComp.getX0() - 20 + (pepBarComp.getWidth() / 2) - 5), 0) + "px; top: " + (top - 4) + "px;");
                        }
                        pepBarComp.getPtmLayout().setVisible(true);
                        ptmsLayoutMap.add(pepBarComp.getPtmLayout());
                    }
                    pepBarComp.setX0(Math.max((pepBarComp.getX0() - 20), 0));

                }
                updatedLevel.clear();
                updatedLevel.addAll(initLevel);
                initLevel.clear();

            }
            if (!intersect && !nextLevel.isEmpty()) {

                initLevel.clear();
                updatedLevel.clear();
                initLevel.addAll(nextLevel);
                updatedLevel.addAll(nextLevel);
                nextLevel.clear();
                intersect = true;
                top = top + 20;
            }

        }
        if (stackedPeptides.isEmpty()) {
            stackedPeptides.addAll(stackedBarComponents);
        }
        if (existedPeptides) {
            top = top + 40;
            peptidesComponentsContainer.setHeight(Math.max(40, top), Unit.PIXELS);
        } else {
            peptidesComponentsContainer.setHeight(0, Unit.PIXELS);
        }

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

    private void checkAndMerge(String sequence) {

        int[] location = new int[sequence.length()];
        int[] startPostionOnLayout = new int[sequence.length()];
        int[] endPostionOnLayout = new int[sequence.length()];
        allPeptidesStackedBarComponentsMap.stream().forEach((peptideLayout) -> {

            String pepSeq = peptideLayout.getParam("sequence").toString();
            if (sequence.contains(pepSeq))  {
                int start = sequence.split(pepSeq)[0].length() + 1;
                int end = start + pepSeq.length() - 1;
                if (endPostionOnLayout[start - 2] == 0 ||(endPostionOnLayout[start - 2] > 0 && endPostionOnLayout[start - 2] < end)) {
                    endPostionOnLayout[start - 2] = peptideLayout.getX0() + peptideLayout.getWidthArea();
                }
                for (int x = start - 2; x < end; x++) {
                    location[x] = location[x] + 1;
                    startPostionOnLayout[x] = peptideLayout.getX0();

                }
            }
        });
        for (int i = 0; i < location.length; i++) {
            if ((i == 0 && location[i] > 0) || (i > 0 && location[i] > 0 && location[i - 1] == 0)) {
                int endPep = i;
                for (; endPep < location.length; endPep++) {
                    if (location[endPep] == 0) {
                        break;
                    }

                }
                int widthArea = 0;
                for (int w = i; w < endPep; w++) {
                    widthArea = Math.max(endPostionOnLayout[w], widthArea);
                }
                VerticalLayout coverageComp = new VerticalLayout();
                coverageComp.setStyleName("vdarkgray");
                coverageComp.setHeight(15, Unit.PIXELS);

                coverageComp.setWidth((widthArea - startPostionOnLayout[i]), Unit.PIXELS);
                coverageComp.setDescription("" + (i + 2) + "-" + sequence.substring((i + 2), endPep) + "-" + endPep);
                coveragePeptidesSequencesBar.addComponent(coverageComp, "left: " + startPostionOnLayout[i] + "px; top: " + (0) + "px;");

            }

        }

    }

}
