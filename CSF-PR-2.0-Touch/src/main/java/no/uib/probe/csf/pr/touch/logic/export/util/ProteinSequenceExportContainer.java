/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.logic.export.util;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JLabel;
import javax.swing.JPanel;
import no.uib.probe.csf.pr.touch.logic.beans.QuantPeptide;
import no.uib.probe.csf.pr.touch.view.components.peptideviewsubcomponents.PeptideSequenceContainer;
import no.uib.probe.csf.pr.touch.view.core.StackedBarPeptideComponent;

/**
 *
 * @author yfa041
 */
public class ProteinSequenceExportContainer extends JPanel{

    private VerticalLayout allPeptidesLayout, significantPeptidesLayout;
    private List< StackedBarPeptideComponent> stackedPeptides;
    private PeptideSequenceContainer allPeptidesComponent, significantPeptidesComponent;
    private double correctedWidth = 0;
    private int totalPeptidesNumber = 0;
    private  String sequence;
    private  String proteinName;

    public String getSequence() {
        return sequence;
    }
    
    
    

    public ProteinSequenceExportContainer(String sequence, Set<QuantPeptide> quantPepSet, int proteinSequenceContainerWidth, final int dsID,String proteinName) {
        this.setSize(proteinSequenceContainerWidth,100);
        this.setBackground(Color.RED);
        this.proteinName=proteinName;
        this.sequence = sequence;
        JLabel noPeptideAvailable = new JLabel("Peptide information is not available");
//        noPeptideAvailable.addStyleName(ValoTheme.LABEL_TINY);
//        Set<QuantPeptide> filteredQuantPepSet = new LinkedHashSet<>();
//        if (sequence == null || sequence.trim().equalsIgnoreCase("") || quantPepSet == null || quantPepSet.isEmpty()) {
//            //no peptides available
//
//            this.add(noPeptideAvailable);
//
//        } else {
//            quantPepSet.stream().filter((peptide) -> (peptide.getDsKey() == dsID)).forEach((peptide) -> {
//                filteredQuantPepSet.add(peptide);
//            });
//            if (filteredQuantPepSet.isEmpty()) {
//                this.add(noPeptideAvailable);
//                return;
//
//            }
//
//            allPeptidesLayout = new VerticalLayout();
//            this.addC(allPeptidesLayout);
//            significantPeptidesLayout = new VerticalLayout();
//            this.addComponent(significantPeptidesLayout);
//            stackedPeptides = new ArrayList<>();
//            final LinkedHashSet<StackedBarPeptideComponent> allPeptidesStackedBarComponentsMap = this.initAllBarChartComponents(false, proteinSequenceContainerWidth - 160, sequence, filteredQuantPepSet, true);
//
//            stackedPeptides.addAll(allPeptidesStackedBarComponentsMap);
//            allPeptidesComponent = new PeptideSequenceContainer((int) correctedWidth + 40, allPeptidesStackedBarComponentsMap,smallScreen,proteinName);
//            allPeptidesLayout.addComponent(allPeptidesComponent);
//            allPeptidesLayout.setComponentAlignment(allPeptidesComponent, Alignment.MIDDLE_CENTER);
//            final LinkedHashSet<StackedBarPeptideComponent> significantPeptidesStackedBarComponentsMap = this.initAllBarChartComponents(true, proteinSequenceContainerWidth - 160, sequence, filteredQuantPepSet, false);
//            significantPeptidesComponent = new PeptideSequenceContainer((int) correctedWidth + 40, significantPeptidesStackedBarComponentsMap,smallScreen,proteinName);
//            significantPeptidesLayout.addComponent(significantPeptidesComponent);
//            significantPeptidesLayout.setComponentAlignment(significantPeptidesComponent, Alignment.MIDDLE_CENTER);
//            significantPeptidesLayout.setVisible(false);

//        }

    }

    private LinkedHashSet<StackedBarPeptideComponent> initAllBarChartComponents(boolean significatOnly, int width, String sequence, Set<QuantPeptide> quantPepSet, boolean count) {

        final LinkedHashSet<StackedBarPeptideComponent> barComponentMap = new LinkedHashSet<>();

        correctedWidth = width;
        double charW = (double) ((double) width / (double) sequence.length());
        quantPepSet.stream().filter((quantPeptide) -> !(quantPeptide.getPeptideSequence().equalsIgnoreCase("not available"))).map((quantPeptide) -> {

            return quantPeptide;
        }).forEach((quantPeptide) -> {
            String peptideSequence = quantPeptide.getPeptideSequence().trim();
            if (peptideSequence.contains(".")) {
                peptideSequence = quantPeptide.getPeptideSequence().replace(".", "").trim().substring(1, quantPeptide.getPeptideSequence().length() - 2);
            }
            double peptideLayoutWidth = Math.round(peptideSequence.length() * charW);
            int start = sequence.split(peptideSequence)[0].length() + 1;
            int end = sequence.split(peptideSequence)[0].length() + peptideSequence.length();

            int x0 = (int) Math.round((start * charW));
            if ((x0 + peptideLayoutWidth) > correctedWidth) {
                correctedWidth = correctedWidth + ((x0 + peptideLayoutWidth) - correctedWidth);
            }

            if (!significatOnly) {
                StackedBarPeptideComponent peptideStackedBarComponent = new StackedBarPeptideComponent(x0, (int) (peptideLayoutWidth), quantPeptide.getUniqueId() + "", quantPeptide.getPeptideModification(), quantPeptide,false,proteinName);
                peptideStackedBarComponent.setWidth((int) peptideLayoutWidth, Sizeable.Unit.PIXELS);
                if (sequence.startsWith("ZZZZZZZZ")) {
                    peptideStackedBarComponent.setDescription("" + 0 + "-" + quantPeptide.getPeptideSequence() + "-" + peptideSequence.length() + "");
                } else {
                    peptideStackedBarComponent.setDescription("" + start + "-" + quantPeptide.getPeptideSequence() + "-" + end + "");
                }
                peptideStackedBarComponent.setParam("peptide", quantPeptide);
                peptideStackedBarComponent.setParam("sequence", quantPeptide.getPeptideSequence());
                peptideStackedBarComponent.setParam("start", start);
                peptideStackedBarComponent.setParam("end", end);
                if (count) {
                    this.totalPeptidesNumber++;
                }
                if (quantPeptide.getString_p_value().trim().equalsIgnoreCase("")) {
                    peptideStackedBarComponent.setSignificant(false);
                    peptideStackedBarComponent.setDefaultStyleShowAllMode("graystackedlayout");
                    peptideStackedBarComponent.setParam("trend", "noquant");

                } else if (quantPeptide.getString_p_value().equalsIgnoreCase("Significant")) {
                    peptideStackedBarComponent.setSignificant(true);
                    if (quantPeptide.getString_fc_value().equalsIgnoreCase("Increased") || quantPeptide.getString_fc_value().equalsIgnoreCase("Increase") || quantPeptide.getString_fc_value().equalsIgnoreCase("Up")) {
                        peptideStackedBarComponent.setDefaultStyleShowAllMode("redstackedlayout");
                        peptideStackedBarComponent.setParam("trend", "increased");
                    } else if (quantPeptide.getString_fc_value() == null || quantPeptide.getString_fc_value().equalsIgnoreCase("") || quantPeptide.getString_fc_value().trim().equalsIgnoreCase("Not Provided")) {
                        peptideStackedBarComponent.setDefaultStyleShowAllMode("lightbluestackedlayout");
                        peptideStackedBarComponent.setParam("trend", "equal");
                    } else {
                        peptideStackedBarComponent.setDefaultStyleShowAllMode("greenstackedlayout");
                        peptideStackedBarComponent.setParam("trend", "decreased");

                    }

                } else {
                    peptideStackedBarComponent.setSignificant(false);
                    if (quantPeptide.getString_fc_value().equalsIgnoreCase("Increased") || quantPeptide.getString_fc_value().equalsIgnoreCase("Increase") || quantPeptide.getString_fc_value().equalsIgnoreCase("Up")) {
                        peptideStackedBarComponent.setDefaultStyleShowAllMode("midredstackedlayout");
                        peptideStackedBarComponent.setParam("trend", "increased");
                    } else if (quantPeptide.getString_fc_value() == null || quantPeptide.getString_fc_value().equalsIgnoreCase("") || quantPeptide.getString_fc_value().trim().equalsIgnoreCase("Not Provided")) {
                        peptideStackedBarComponent.setDefaultStyleShowAllMode("lightbluestackedlayout");
                        peptideStackedBarComponent.setParam("trend", "equal");
                    } else {
                        peptideStackedBarComponent.setDefaultStyleShowAllMode("midgreenstackedlayout");
                        peptideStackedBarComponent.setParam("trend", "decreased");

                    }
                }
                barComponentMap.add(peptideStackedBarComponent);

            } else {
                if (quantPeptide.getString_p_value().equalsIgnoreCase("Significant")) {

                    StackedBarPeptideComponent peptideStackedBarComponent = new StackedBarPeptideComponent(x0, (int) (peptideLayoutWidth), quantPeptide.getUniqueId() + "", quantPeptide.getPeptideModification(), quantPeptide,false,proteinName);
                    peptideStackedBarComponent.setSignificant(true);
                    peptideStackedBarComponent.setWidth((int) peptideLayoutWidth, Sizeable.Unit.PIXELS);
                    if (sequence.startsWith("ZZZZZZZZ")) {
                        peptideStackedBarComponent.setDescription("" + 0 + "-" + quantPeptide.getPeptideSequence() + "-" + peptideSequence.length() + "");
                    } else {
                        peptideStackedBarComponent.setDescription("" + start + "-" + quantPeptide.getPeptideSequence() + "-" + end + "");
                    }
                    peptideStackedBarComponent.setParam("peptide", quantPeptide);
                    peptideStackedBarComponent.setParam("sequence", quantPeptide.getPeptideSequence());
                    peptideStackedBarComponent.setParam("start", start);
                    peptideStackedBarComponent.setParam("end", end);

                    if (quantPeptide.getString_fc_value().equalsIgnoreCase("Increased") || quantPeptide.getString_fc_value().equalsIgnoreCase("Up")) {
                        peptideStackedBarComponent.setDefaultStyleShowAllMode("redstackedlayout");
                        peptideStackedBarComponent.setParam("trend", "increased");
                    } else if (quantPeptide.getString_fc_value() == null || quantPeptide.getString_fc_value().equalsIgnoreCase("") || quantPeptide.getString_fc_value().trim().equalsIgnoreCase("Not Provided")) {
                        peptideStackedBarComponent.setDefaultStyleShowAllMode("lightbluestackedlayout");
                        peptideStackedBarComponent.setParam("trend", "equal");
                    } else {
                        peptideStackedBarComponent.setDefaultStyleShowAllMode("greenstackedlayout");
                        peptideStackedBarComponent.setParam("trend", "decreased");

                    }

                    barComponentMap.add(peptideStackedBarComponent);
                }
            }
        });
        final LinkedHashSet<StackedBarPeptideComponent> updatedBarComponentMap = new LinkedHashSet<>();
        barComponentMap.stream().map((sbar) -> {
            sbar.heighlight(null);
            return sbar;
        }).forEach((sbar) -> {
            updatedBarComponentMap.add(sbar);
        });
        return updatedBarComponentMap;
    }

    public void setShowNotSignificantPeptides(boolean showNotSignificantpeptide) {
        if (allPeptidesLayout == null) {
            return;
        }
        allPeptidesLayout.setVisible(showNotSignificantpeptide);
        significantPeptidesLayout.setVisible(!showNotSignificantpeptide);
    }

}
