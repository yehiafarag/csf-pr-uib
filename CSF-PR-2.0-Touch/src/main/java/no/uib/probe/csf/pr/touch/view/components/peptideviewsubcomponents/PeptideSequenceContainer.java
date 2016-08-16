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
import com.vaadin.ui.themes.Reindeer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import no.uib.probe.csf.pr.touch.view.core.StackedBarPeptideComponent;

/**
 *
 * @author Yehia Farag
 *
 * this class represents the protein sequence layout
 */
public class PeptideSequenceContainer extends AbsoluteLayout {

    private final AbsoluteLayout highPeptidesSequencesBar, lowPeptidesSequencesBar, coveragePeptidesSequencesBar, stablePeptidesSequencesBar;
    private final LinkedHashSet<StackedBarPeptideComponent> allPeptidesStackedBarComponentsMap;
    private final Set<VerticalLayout> ptmsLayoutMap = new HashSet<>();
    private final List< StackedBarPeptideComponent> stackedPeptides = new ArrayList<>();
    private boolean ptmAvailable = false;
    private final boolean smallScreen;
    private final String proteinName;

    public PeptideSequenceContainer(int width, LinkedHashSet<StackedBarPeptideComponent> allPeptidesStackedBarComponentsMap,boolean smallScreen,String proteinName) {
        this.allPeptidesStackedBarComponentsMap = allPeptidesStackedBarComponentsMap;
        this.setVisible(true);
        this.setWidth(width, Unit.PIXELS);
        this.smallScreen = smallScreen;
        this.proteinName=proteinName;
        

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

        initLayout();

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

    private void initLayout() {

        LinkedHashSet<StackedBarPeptideComponent> highSet = new LinkedHashSet<>();
        LinkedHashSet<StackedBarPeptideComponent> stableSet = new LinkedHashSet<>();
        allPeptidesStackedBarComponentsMap.stream().forEach((peptideLayout) -> {
            if (peptideLayout.getParam("trend").toString().equalsIgnoreCase("increased")) {
                highSet.add(peptideLayout);
            } else if (peptideLayout.getParam("trend").toString().equalsIgnoreCase("equal") || peptideLayout.getParam("trend").toString().equalsIgnoreCase("noquant")) {
                stableSet.add(peptideLayout);
            }
        });

        allPeptidesStackedBarComponentsMap.stream().filter((peptideLayout) -> (peptideLayout.getParam("trend").toString().equalsIgnoreCase("decreased"))).forEach((peptideLayout) -> {
            stableSet.add(peptideLayout);
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
        top += 5;
        this.setHeight(top, Unit.PIXELS);
        ptmAvailable = !ptmsLayoutMap.isEmpty();
        checkAndMerge();

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
                    peptidesComponentsContainer.addComponent(pepBarComp, "left: " + (pepBarComp.getX0() - 20) + "px; top: " + (top + 10) + "px;");
                    existedPeptides = true;
                    if (pepBarComp.isPtmAvailable()) {
                        if (flip) {
                            peptidesComponentsContainer.addComponent(pepBarComp.getPtmLayout(), "left: " + (pepBarComp.getX0() - 20 + (pepBarComp.getWidth() / 2) - 5) + "px; top: " + (top + 21) + "px;");
                        } else {
                            peptidesComponentsContainer.addComponent(pepBarComp.getPtmLayout(), "left: " + (pepBarComp.getX0() - 20 + (pepBarComp.getWidth() / 2) - 5) + "px; top: " + (top - 4) + "px;");
                        }
                        pepBarComp.getPtmLayout().setVisible(true);
                        ptmsLayoutMap.add(pepBarComp.getPtmLayout());
                    }

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

    private void checkAndMerge() {

        TreeMap<Integer, StackedBarPeptideComponent> finalUpdatedPeptidesCoverageMap = new TreeMap<>();
        TreeMap<Integer, StackedBarPeptideComponent> orderedCompoMap = new TreeMap<>();
        allPeptidesStackedBarComponentsMap.stream().forEach((peptideLayout) -> {
            if (orderedCompoMap.containsKey(peptideLayout.getX0())) {
                StackedBarPeptideComponent toReplaceComp = orderedCompoMap.remove(peptideLayout.getX0());
                if (toReplaceComp.getWidthArea() <= peptideLayout.getWidthArea()) {
                    orderedCompoMap.put(peptideLayout.getX0(), peptideLayout);
                } else {
                    orderedCompoMap.put(toReplaceComp.getX0(), toReplaceComp);
                }

            } else {
                orderedCompoMap.put(peptideLayout.getX0(), peptideLayout);
            }
        });

        if (orderedCompoMap.size() == 1) {
            StackedBarPeptideComponent peptideI = orderedCompoMap.firstEntry().getValue();
            finalUpdatedPeptidesCoverageMap.put(peptideI.getX0(), peptideI);
        } else {

            TreeMap<Integer, StackedBarPeptideComponent> refrenceOrderedCompoMap = new TreeMap<>(orderedCompoMap);

            while (true) {
                boolean merge = false;
                for (int keyI : orderedCompoMap.navigableKeySet()) {
                    StackedBarPeptideComponent peptideI = orderedCompoMap.get(keyI);
                    TreeMap<Integer, StackedBarPeptideComponent> comparableOrderedCompoMap = new TreeMap<>(refrenceOrderedCompoMap);
                    comparableOrderedCompoMap.remove(keyI);
                    for (int keyII : comparableOrderedCompoMap.navigableKeySet()) {
                        StackedBarPeptideComponent peptideII = comparableOrderedCompoMap.get(keyII);
                        if (((Integer) peptideII.getParam("start")) == ((Integer) peptideI.getParam("end") + 1) || (((Integer) peptideI.getParam("start")) == ((Integer) peptideII.getParam("end") + 1))) {
                           
                            int x0 = Math.min(peptideI.getX0(), peptideII.getX0());
                            int widthArea = peptideI.getWidthArea() + peptideII.getWidthArea();
                            String sequence;
                            if (peptideI.getX0() < peptideII.getX0()) {
                                sequence = peptideI.getParam("sequence").toString() + peptideII.getParam("sequence");
                            } else {
                                sequence = peptideII.getParam("sequence").toString() + peptideI.getParam("sequence");
                            }
                            StackedBarPeptideComponent updatedCoverComp = new StackedBarPeptideComponent(x0, widthArea, "", "",null,smallScreen,proteinName);
                            refrenceOrderedCompoMap.remove(keyI);
                            refrenceOrderedCompoMap.remove(keyII);
                            updatedCoverComp.setParam("sequence", sequence);
                            updatedCoverComp.setParam("start", Math.min(((Integer) peptideI.getParam("start")), ((Integer) peptideII.getParam("start"))));
                            updatedCoverComp.setParam("end", Math.max(((Integer) peptideI.getParam("end")), ((Integer) peptideII.getParam("end"))));
                            refrenceOrderedCompoMap.put(x0 + 10000, updatedCoverComp);
                            merge = true;
                            break;

                        } else if (((Integer) peptideII.getParam("start") > (Integer) peptideI.getParam("start")) && ((Integer) peptideII.getParam("end") > (Integer) peptideI.getParam("end")) && ((Integer) peptideII.getParam("start") < (Integer) peptideI.getParam("end"))) {
              
                            int x0 = Math.min(peptideI.getX0(), peptideII.getX0());
                            int widthArea;
                            String sequence;
                            if (peptideI.getX0() < peptideII.getX0()) {
                                sequence = peptideI.getParam("sequence").toString() + peptideII.getParam("sequence");
                                widthArea = peptideII.getWidthArea() + (peptideII.getX0() - peptideI.getX0());
                            } else {
                                sequence = peptideII.getParam("sequence").toString() + peptideI.getParam("sequence");
                                widthArea = peptideI.getWidthArea() + (peptideI.getX0() - peptideII.getX0());
                            }

                            StackedBarPeptideComponent updatedCoverComp = new StackedBarPeptideComponent(x0, widthArea, "", "",null,smallScreen,proteinName);
                            refrenceOrderedCompoMap.remove(keyI);
                            refrenceOrderedCompoMap.remove(keyII);
                            updatedCoverComp.setParam("sequence", sequence);
                            updatedCoverComp.setParam("start", Math.min(((Integer) peptideI.getParam("start")), ((Integer) peptideII.getParam("start"))));
                            updatedCoverComp.setParam("end", Math.max(((Integer) peptideI.getParam("end")), ((Integer) peptideII.getParam("end"))));
                            refrenceOrderedCompoMap.put(x0 + 10000, updatedCoverComp);
                            merge = true;
                            break;

                        } else if (((Integer) peptideII.getParam("start") > (Integer) peptideI.getParam("start")) && ((Integer) peptideII.getParam("end") <= (Integer) peptideI.getParam("end"))) {
                            int x0 = Math.min(peptideI.getX0(), peptideII.getX0());

                            int widthArea ;
                            String sequence;
                            if (peptideI.getParam("sequence").toString().contains(peptideII.getParam("sequence").toString())) {
                                widthArea = peptideI.getWidthArea();
                                sequence = peptideI.getParam("sequence").toString();
                            } else {
                                widthArea = peptideII.getWidthArea();
                                sequence = peptideII.getParam("sequence").toString();
                            }
                            StackedBarPeptideComponent updatedCoverComp = new StackedBarPeptideComponent(x0, widthArea, "", "",null,smallScreen,proteinName);
                            refrenceOrderedCompoMap.remove(keyI);
                            refrenceOrderedCompoMap.remove(keyII);
                            updatedCoverComp.setParam("sequence", sequence);
                            updatedCoverComp.setParam("start", Math.min(((Integer) peptideI.getParam("start")), ((Integer) peptideII.getParam("start"))));
                            updatedCoverComp.setParam("end", Math.max(((Integer) peptideI.getParam("end")), ((Integer) peptideII.getParam("end"))));
                            refrenceOrderedCompoMap.put(x0 + 10000, updatedCoverComp);
                            merge = true;
                            break;

                        }
                    }

                }
                if (merge) {
                    orderedCompoMap.clear();
                    orderedCompoMap.putAll(refrenceOrderedCompoMap);
                } else {
                    break;
                }

            }
            finalUpdatedPeptidesCoverageMap.putAll(refrenceOrderedCompoMap);

        }

        finalUpdatedPeptidesCoverageMap.values().stream().forEach((peptideLayout) -> {
            VerticalLayout coverageComp = new VerticalLayout();
            coverageComp.setStyleName("vdarkgray");
            coverageComp.setHeight(15, Unit.PIXELS);
            coverageComp.setWidth(peptideLayout.getWidth(), peptideLayout.getWidthUnits());
            coverageComp.setDescription("" + peptideLayout.getParam("start") + "-" + peptideLayout.getParam("sequence") + "-" + peptideLayout.getParam("end"));
            coveragePeptidesSequencesBar.addComponent(coverageComp, "left: " + (peptideLayout.getX0() - 20) + "px; top: " + (0) + "px;");
        });

    }

}
