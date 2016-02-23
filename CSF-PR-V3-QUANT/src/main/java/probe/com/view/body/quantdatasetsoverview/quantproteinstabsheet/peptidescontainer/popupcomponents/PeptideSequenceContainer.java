/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.peptidescontainer.popupcomponents;

import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import probe.com.view.core.jfreeutil.StackedBarPeptideComponent;

/**
 *
 * @author yfa041
 */
public class PeptideSequenceContainer extends AbsoluteLayout {

    private final AbsoluteLayout highPeptidesSequencesBar, lowPeptidesSequencesBar, coveragePeptidesSequencesBar, stablePeptidesSequencesBar;
    private final LinkedHashSet<StackedBarPeptideComponent> allPeptidesStackedBarComponentsMap;
    private final Set<VerticalLayout> ptmsLayoutMap = new HashSet<VerticalLayout>();
    private List< StackedBarPeptideComponent> stackedPeptides = new ArrayList<StackedBarPeptideComponent>();

    private int level = 1;

    public PeptideSequenceContainer(int width, LinkedHashSet<StackedBarPeptideComponent> allPeptidesStackedBarComponentsMap) {
        System.out.println("at allPeptidesStackedBarComponentsMap size " + allPeptidesStackedBarComponentsMap.size());

        this.allPeptidesStackedBarComponentsMap = allPeptidesStackedBarComponentsMap;
        this.setVisible(true);
        this.setStyleName(Reindeer.LAYOUT_WHITE);
        this.setWidth((width) + "px");

        highPeptidesSequencesBar = new AbsoluteLayout();
        highPeptidesSequencesBar.setWidth(width - 50 + "px");
//        highPeptidesSequencesBar.setHeight("15px");
//        highPeptidesSequencesBar.setStyleName("lightgraylayout");

        highPeptidesSequencesBar.setStyleName("flipvertically");
        this.addComponent(highPeptidesSequencesBar, "left: " + (25) + "px; top: " + (0) + "px;");

        coveragePeptidesSequencesBar = new AbsoluteLayout();
        coveragePeptidesSequencesBar.setWidth(width - 50 + "px");
        coveragePeptidesSequencesBar.setHeight("17px");
        coveragePeptidesSequencesBar.setStyleName("lightgraylayout");

        stablePeptidesSequencesBar = new AbsoluteLayout();
        stablePeptidesSequencesBar.setWidth(width - 50 + "px");
        stablePeptidesSequencesBar.setHeight("15px");
//        lowPeptidesSequencesBar.setStyleName("lightgraylayout");

        lowPeptidesSequencesBar = new AbsoluteLayout();
        lowPeptidesSequencesBar.setWidth(width - 50 + "px");
//        lowPeptidesSequencesBar.setHeight("15px");
//        lowPeptidesSequencesBar.setStyleName("lightgraylayout");
//        this.addComponent(lowPeptidesSequencesBar, "left: " + (50) + "px; top: " + (10) + "px;");

        initLayout();

    }

    private void addTerminalLabels(int top, int width) {

        VerticalLayout nTerminalEdge = new VerticalLayout();
        nTerminalEdge.setWidth("25px");
        nTerminalEdge.setHeight("15px");
        nTerminalEdge.setStyleName("terminal");

        this.addComponent(nTerminalEdge, "left: " + (0) + "px; top: " + (top) + "px;");

        VerticalLayout cTermanalEdge = new VerticalLayout();
        cTermanalEdge.setWidth("25px");
        cTermanalEdge.setHeight("15px");
        cTermanalEdge.setStyleName("terminal");
        this.addComponent(cTermanalEdge, "left: " + (width - 25) + "px; top: " + (top) + "px;");

    }

    private void initLayout() {

        LinkedHashSet<StackedBarPeptideComponent> highSet = new LinkedHashSet<StackedBarPeptideComponent>();
        LinkedHashSet<StackedBarPeptideComponent> lowSet = new LinkedHashSet<StackedBarPeptideComponent>();
        LinkedHashSet<StackedBarPeptideComponent> stableSet = new LinkedHashSet<StackedBarPeptideComponent>();
        for (StackedBarPeptideComponent peptideLayout : allPeptidesStackedBarComponentsMap) {
            if (peptideLayout.getParam("trend").toString().equalsIgnoreCase("high")) {
                highSet.add(peptideLayout);
            } else if (peptideLayout.getParam("trend").toString().equalsIgnoreCase("stable")) {
                stableSet.add(peptideLayout);
            } else if (peptideLayout.getParam("trend").toString().equalsIgnoreCase("low")) {
                lowSet.add(peptideLayout);
            }
            VerticalLayout coverageComp = new VerticalLayout();
            coverageComp.setStyleName("vdarkgray");
            coverageComp.setHeight("15px");
            coverageComp.setWidth(peptideLayout.getWidth(), peptideLayout.getWidthUnits());
            coveragePeptidesSequencesBar.addComponent(coverageComp, "left: " + (peptideLayout.getX0() - 25) + "px; top: " + (0) + "px;");

        }
        ptmsLayoutMap.clear();
        int top = 0;
        if (!highSet.isEmpty()) {
            initPeptidesStackedBarComponentsLayout(highSet, highPeptidesSequencesBar, true);
            top = (int) highPeptidesSequencesBar.getHeight();
        }
        this.addComponent(coveragePeptidesSequencesBar, "left: " + (24) + "px; top: " + (top) + "px;");
        this.addTerminalLabels(top, (int) this.getWidth());
        top += 17;

        if (!stableSet.isEmpty()) {
            initPeptidesStackedBarComponentsLayout(stableSet, stablePeptidesSequencesBar, false);
            this.addComponent(stablePeptidesSequencesBar, "left: " + (25) + "px; top: " + (top) + "px;");
            top += stablePeptidesSequencesBar.getHeight();
        }
        if (!lowSet.isEmpty()) {
            initPeptidesStackedBarComponentsLayout(lowSet, lowPeptidesSequencesBar, false);
            this.addComponent(lowPeptidesSequencesBar, "left: " + (25) + "px; top: " + (top) + "px;");
            top += lowPeptidesSequencesBar.getHeight() + 5;
        }

        this.setHeight(top + "px");

    }

    private void initPeptidesStackedBarComponentsLayout(LinkedHashSet<StackedBarPeptideComponent> stackedBarComponents, AbsoluteLayout peptidesComponentsContainer, boolean flip) {
        int top = 0;
        List< StackedBarPeptideComponent> initLevel = new ArrayList<StackedBarPeptideComponent>(stackedBarComponents);
        List< StackedBarPeptideComponent> updatedLevel = new ArrayList<StackedBarPeptideComponent>(stackedBarComponents);
        List< StackedBarPeptideComponent> nextLevel = new ArrayList<StackedBarPeptideComponent>();

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
                    peptidesComponentsContainer.addComponent(pepBarComp, "left: " + (pepBarComp.getX0() - 25) + "px; top: " + (top + 10) + "px;");
                    existedPeptides = true;
                    if (pepBarComp.isPtmAvailable()) {
                        if (flip) {
                            peptidesComponentsContainer.addComponent(pepBarComp.getPtmLayout(), "left: " + (pepBarComp.getX0() - 25 + (pepBarComp.getWidth() / 2) - 5) + "px; top: " + (top + 21) + "px;");
                        } else {
                            peptidesComponentsContainer.addComponent(pepBarComp.getPtmLayout(), "left: " + (pepBarComp.getX0() - 25 + (pepBarComp.getWidth() / 2) - 5) + "px; top: " + (top - 4) + "px;");
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
                level++;
            }

        }
        if (stackedPeptides.isEmpty()) {
            stackedPeptides.addAll(stackedBarComponents);
        }
        if (existedPeptides) {
            top = top + 40;
            peptidesComponentsContainer.setHeight(Math.max(40, top) + "px");
        } else {
            peptidesComponentsContainer.setHeight("0px");
        }

    }

    private boolean checkIntersect(StackedBarPeptideComponent smallXComp, StackedBarPeptideComponent bigXComp) {
        int area = smallXComp.getX0() + smallXComp.getWidthArea();
        return bigXComp.getX0() < area;

    }

}
