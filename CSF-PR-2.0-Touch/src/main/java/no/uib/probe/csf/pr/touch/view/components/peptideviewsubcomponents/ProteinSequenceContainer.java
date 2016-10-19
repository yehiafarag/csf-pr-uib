package no.uib.probe.csf.pr.touch.view.components.peptideviewsubcomponents;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantPeptide;
import no.uib.probe.csf.pr.touch.view.core.StackedBarPeptideComponent;

/**
 *
 * @author Yehia Farag
 *
 * this class represents protein sequence with peptides coverage
 *
 */
public class ProteinSequenceContainer extends VerticalLayout {

    private VerticalLayout allPeptidesLayout, significantPeptidesLayout;
    private List< StackedBarPeptideComponent> stackedPeptides;
    private PeptideSequenceContainer allPeptidesComponent, significantPeptidesComponent;
    private double correctedWidth = 0;
    private int totalPeptidesNumber = 0;
    private final String sequence;
    private final boolean smallScreen;
    private final String proteinName;
    private final Set<QuantPeptide> quantPepSet;
    private final  int dsID;
    

    public String getSequence() {
        return sequence;
    }

    public Set<QuantPeptide> getQuantPepSet() {
        return quantPepSet;
    }

    public int getDsID() {
        return dsID;
    }

    public String getProteinName() {
        return proteinName;
    }
    
    
    

    public ProteinSequenceContainer(String sequence, Set<QuantPeptide> quantPepSet, int proteinSequenceContainerWidth, final int dsID,boolean smallScreen,String proteinName) {
        this.setWidth(100, Unit.PERCENTAGE);
        this.addStyleName("roundedborder");
        this.addStyleName("padding20");
        this.addStyleName("whitelayout");
        this.quantPepSet=quantPepSet;
        this.smallScreen=smallScreen;
        this.proteinName=proteinName;
        this.sequence = sequence;
        this.dsID=dsID;
        Label noPeptideAvailable = new Label("Peptide information not available");
        noPeptideAvailable.addStyleName(ValoTheme.LABEL_TINY);
        Set<QuantPeptide> filteredQuantPepSet = new LinkedHashSet<>();
        if (sequence == null || sequence.trim().equalsIgnoreCase("") || quantPepSet == null || quantPepSet.isEmpty()) {
            //no peptides available

            this.addComponent(noPeptideAvailable);

        } else {
            quantPepSet.stream().filter((peptide) -> (peptide.getDsKey() == dsID)).forEach((peptide) -> {
                filteredQuantPepSet.add(peptide);
            });
            if (filteredQuantPepSet.isEmpty()) {
                this.addComponent(noPeptideAvailable);
                return;

            }

            allPeptidesLayout = new VerticalLayout();
            this.addComponent(allPeptidesLayout);
            significantPeptidesLayout = new VerticalLayout();
            this.addComponent(significantPeptidesLayout);
            stackedPeptides = new ArrayList<>();
            final LinkedHashSet<StackedBarPeptideComponent> allPeptidesStackedBarComponentsMap = this.initAllBarChartComponents(false, proteinSequenceContainerWidth - 160, sequence, filteredQuantPepSet, true);

            stackedPeptides.addAll(allPeptidesStackedBarComponentsMap);
            allPeptidesComponent = new PeptideSequenceContainer((int) correctedWidth + 40, allPeptidesStackedBarComponentsMap,smallScreen,proteinName,sequence);
            allPeptidesLayout.addComponent(allPeptidesComponent);
            allPeptidesLayout.setComponentAlignment(allPeptidesComponent, Alignment.MIDDLE_CENTER);
            final LinkedHashSet<StackedBarPeptideComponent> significantPeptidesStackedBarComponentsMap = this.initAllBarChartComponents(true, proteinSequenceContainerWidth - 160, sequence, filteredQuantPepSet, false);
            significantPeptidesComponent = new PeptideSequenceContainer((int) correctedWidth + 40, significantPeptidesStackedBarComponentsMap,smallScreen,proteinName,sequence);
            significantPeptidesLayout.addComponent(significantPeptidesComponent);
            significantPeptidesLayout.setComponentAlignment(significantPeptidesComponent, Alignment.MIDDLE_CENTER);
            significantPeptidesLayout.setVisible(false);

        }

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
                StackedBarPeptideComponent peptideStackedBarComponent = new StackedBarPeptideComponent(x0, (int) (peptideLayoutWidth), quantPeptide.getUniqueId() + "", quantPeptide.getPeptideModification(), quantPeptide,smallScreen,proteinName);
                peptideStackedBarComponent.setWidth((int) peptideLayoutWidth, Unit.PIXELS);
                if (sequence.startsWith("ZZZZZZZZ")) {
                    peptideStackedBarComponent.setDescription("" + 1 + "~" + quantPeptide.getPeptideSequence() + "~" + peptideSequence.length() + "");
                } else {
                    peptideStackedBarComponent.setDescription("" + start + "~" + quantPeptide.getPeptideSequence() + "~" + end + "");
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

                    StackedBarPeptideComponent peptideStackedBarComponent = new StackedBarPeptideComponent(x0, (int) (peptideLayoutWidth), quantPeptide.getUniqueId() + "", quantPeptide.getPeptideModification(), quantPeptide,smallScreen,proteinName);
                    peptideStackedBarComponent.setSignificant(true);
                    peptideStackedBarComponent.setWidth((int) peptideLayoutWidth, Unit.PIXELS);
                    if (sequence.startsWith("ZZZZZZZZ")) {
                        peptideStackedBarComponent.setDescription("" + 0 + "~" + quantPeptide.getPeptideSequence() + "~" + peptideSequence.length() + "");
                    } else {
                        peptideStackedBarComponent.setDescription("" + start + "~" + quantPeptide.getPeptideSequence() + "~" + end + "");
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
