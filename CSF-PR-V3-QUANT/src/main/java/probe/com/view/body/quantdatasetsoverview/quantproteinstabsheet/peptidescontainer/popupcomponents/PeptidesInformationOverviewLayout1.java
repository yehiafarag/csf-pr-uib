package probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.peptidescontainer.popupcomponents;

import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import probe.com.model.beans.quant.QuantPeptide;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.TrendLegend;
import probe.com.view.core.InformationField;
import probe.com.view.core.jfreeutil.StackedBarPeptideComponent;

/**
 *
 * @author Yehia Farag
 */
public class PeptidesInformationOverviewLayout1 extends VerticalLayout {

    private boolean noPeptide = true;
    private DecimalFormat df = null;
    private AbsoluteLayout sigProtSeqBar, allPepProtSeqBar;
    private AbsoluteLayout allPeptidesContainer, significantPeptidesContainer;
    private int significantPeptidesNumber = 0;
    private int totalPeptidesNumber = 0;
    private OptionGroup showSigneficantPeptidesOnly;
    private final Set<VerticalLayout> ptmsLayoutMap = new HashSet<VerticalLayout>();
    private LayoutEvents.LayoutClickListener studyListener;
    private Label noselectionLabel = new Label("<h4 style='font-family:verdana;color:#8A0808;font-weight:bold;'>\t \t Select peptide to show information!</h4>");
    private boolean hasPTM = false;

    public int getLevel() {
        return level;
    }

    /**
     *
     * @param sequence
     * @param quantPepSet
     * @param width
     * @param minMode
     * @param listener
     * @param dsID
     */
    public PeptidesInformationOverviewLayout1(String sequence, Set<QuantPeptide> quantPepSet, int width, final boolean minMode, LayoutEvents.LayoutClickListener listener, final int dsID) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        otherSymbols.setGroupingSeparator('.');
        df = new DecimalFormat("#.###", otherSymbols);
        this.setWidth(width + "px");
        this.setHeightUndefined();
        this.setSpacing(true);
        this.setMargin(new MarginInfo(false, false, false, false));
        this.setStyleName(Reindeer.LAYOUT_WHITE);

        //init peptides form layout  
        if (!minMode) {
            peptideForm = initPeptidesForm(width);
            peptideForm.setVisible(false);
        } else {
            studyListener = listener;
        }

        if (!sequence.equalsIgnoreCase("Not Available") && !sequence.equalsIgnoreCase("")) {
            //init containers
            allPeptidesContainer = new AbsoluteLayout();
            allPeptidesContainer.setVisible(true);
            allPeptidesContainer.setStyleName(Reindeer.LAYOUT_WHITE);
            allPeptidesContainer.setWidth((width) + "px");
            allPeptidesContainer.setHeightUndefined();
            this.addComponent(allPeptidesContainer);

            //init protein bar
            allPepProtSeqBar = new AbsoluteLayout();
            allPepProtSeqBar.setWidth(width-100 + "px");
            allPepProtSeqBar.setHeight("15px");
            allPepProtSeqBar.setStyleName("lightgraylayout");
            allPeptidesContainer.addComponent(allPepProtSeqBar, "left: " + (50) + "px; top: " + (10) + "px;");
            
            
            

            VerticalLayout nTerminalEdge = new VerticalLayout();
            nTerminalEdge.setWidth("50px");
            nTerminalEdge.setHeight("10px");
            nTerminalEdge.setStyleName("ntermlayout");
            
            allPeptidesContainer.addComponent(nTerminalEdge, "left: " + (0) + "px; top: " + (7.5) + "px;");
            
             VerticalLayout cTermanalEdge = new VerticalLayout();
            cTermanalEdge.setWidth("50px");
            cTermanalEdge.setHeight("10px");
            cTermanalEdge.setStyleName("ctermlayout");
            allPeptidesContainer.addComponent(cTermanalEdge, "left: " + (width-50) + "px; top: " + (7.5) + "px;");
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            significantPeptidesContainer = new AbsoluteLayout();
            significantPeptidesContainer.setVisible(false);
            significantPeptidesContainer.setWidth(width + "px");
            significantPeptidesContainer.setHeightUndefined();

            sigProtSeqBar = new AbsoluteLayout();
            sigProtSeqBar.setWidth(width-100 + "px");
            sigProtSeqBar.setHeight("15px");
            sigProtSeqBar.setStyleName("lightgraylayout");
            significantPeptidesContainer.addComponent(sigProtSeqBar, "left: " + (50) + "px; top: " + (10) + "px;");
            
            
            
              VerticalLayout nTerminalSigEdge = new VerticalLayout();
            nTerminalSigEdge.setWidth("50px");
            nTerminalSigEdge.setHeight("10px");
            nTerminalSigEdge.setStyleName("ntermlayout");
            
            significantPeptidesContainer.addComponent(nTerminalSigEdge, "left: " + (0) + "px; top: " + (7.5) + "px;");
            
             VerticalLayout cTermanalSigEdge = new VerticalLayout();
            cTermanalSigEdge.setWidth("50px");
            cTermanalSigEdge.setHeight("10px");
            cTermanalSigEdge.setStyleName("ctermlayout");
            significantPeptidesContainer.addComponent(cTermanalSigEdge, "left: " + (width-50) + "px; top: " + (7.5) + "px;");
            
            
            
            
            
            
            
            

            HorizontalLayout topLayout = new HorizontalLayout();
            topLayout.setWidth("100%");
            this.addComponent(topLayout);

            TrendLegend legend = new TrendLegend("fullsequencelegend");
//            topLayout.addComponent(legend);

            showSigneficantPeptidesOnly = new OptionGroup();
            topLayout.addComponent(showSigneficantPeptidesOnly);
            topLayout.setComponentAlignment(showSigneficantPeptidesOnly, Alignment.TOP_RIGHT);
            showSigneficantPeptidesOnly.setWidth("135px");
            showSigneficantPeptidesOnly.setNullSelectionAllowed(true); // user can not 'unselect'
            showSigneficantPeptidesOnly.setMultiSelect(true);

            showSigneficantPeptidesOnly.addItem("Significant");
            showSigneficantPeptidesOnly.addItem("PTMs");

            showSigneficantPeptidesOnly.addStyleName("horizontal");
            showSigneficantPeptidesOnly.setVisible(false);

            this.addComponent(significantPeptidesContainer);

            this.addComponent(legend);
            this.setComponentAlignment(legend, Alignment.MIDDLE_RIGHT);

            if (minMode) {
                significantPeptidesContainer.addLayoutClickListener(studyListener);
                significantPeptidesContainer.setData(dsID);
                allPeptidesContainer.addLayoutClickListener(studyListener);
                allPepProtSeqBar.setStyleName("selectablelightgraylayout");
                allPeptidesContainer.setData(dsID);
                sigProtSeqBar.setStyleName("selectablelightgraylayout");
                legend.setVisible(false);

            }
            //init barchart components
            final LinkedHashSet<StackedBarPeptideComponent> allPeptidesStackedBarComponentsMap = this.initAllBarChartComponents(false, width-100, sequence, quantPepSet, true, minMode);
            final LinkedHashSet<StackedBarPeptideComponent> significantPeptidesStackedBarComponentsMap = this.initAllBarChartComponents(true, width-100, sequence, quantPepSet, false, minMode);

//             ptmAllPeptidesOptions = new Options();
//            ptmAllPeptidesDiagram = new NetworkDiagram(ptmAllPeptidesOptions);
            ptmsLayoutMap.clear();
            //sort peptides based on sequence length
            this.initPeptidesStackedBarComponentsLayout(allPeptidesStackedBarComponentsMap, allPeptidesContainer, allPeptidesContainer);
            this.initPeptidesStackedBarComponentsLayout(significantPeptidesStackedBarComponentsMap, significantPeptidesContainer, significantPeptidesContainer);
            showSigneficantPeptidesOnly.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent event) {
                    for (StackedBarPeptideComponent sbar : allPeptidesStackedBarComponentsMap) {
                        sbar.heighlight(null);

                    }
                    for (StackedBarPeptideComponent sbar : significantPeptidesStackedBarComponentsMap) {
                        sbar.heighlight(null);

                    }
                    lastselectedPeptideComp = null;
                    showSignificantRegulationOnly(showSigneficantPeptidesOnly.getValue().toString().contains("Significant"));
                    showPtms(showSigneficantPeptidesOnly.getValue().toString().contains("PTMs"));

                }
            });

        }
        if (!minMode) {
            noselectionLabel.setContentMode(ContentMode.HTML);
            this.addComponent(peptideForm);
            this.addComponent(noselectionLabel);
            showSigneficantPeptidesOnly.setVisible(true);

        }

        showSigneficantPeptidesOnly.setItemEnabled("PTMs", false);
        for (VerticalLayout ptmLayout : ptmsLayoutMap) {
            if (ptmLayout.getStyleName().equalsIgnoreCase("ptmglycosylation")) {
                showSigneficantPeptidesOnly.setItemEnabled("PTMs", true);
                Set<String> ids = new HashSet<String>();
                ids.add("PTMs");
                showSigneficantPeptidesOnly.setValue(ids);
                hasPTM = true;
                break;
            }
        }

    }

    public void showSignificantRegulationOnly(boolean sigOnly) {
        if (lastselectedPeptideComp != null) {
            lastselectedPeptideComp.heighlight(false);
            lastselectedPeptideComp = null;
        }
        if (peptideForm != null) {
            noselectionLabel.setVisible(true);
            peptideForm.setVisible(false);
        }

        if (sigOnly) {
            allPeptidesContainer.setVisible(false);
            significantPeptidesContainer.setVisible(true);
//                        if (significantPeptidesContainer.getComponentCount() == 1) {
////                           
//
//                        }

        } else {

            allPeptidesContainer.setVisible(true);
            significantPeptidesContainer.setVisible(false);

        }

    }

    public void showPtms(boolean show) {

        for (VerticalLayout ptmLayout : ptmsLayoutMap) {
            ptmLayout.setVisible(show);
        }
    }

    /**
     *
     * @return
     */
    public boolean isNoPeptide() {
        return noPeptide;
    }
    private GridLayout peptideForm;
    private StackedBarPeptideComponent lastselectedPeptideComp;
    private InformationField quantBasisComment, pepSequence, peptideModification, modificationComment, pValue, pValueComm, foldChange, roc, additionalComments, pvalueSignificanceThreshold, sequenceAnnotated, peptideCharge;

    private GridLayout initPeptidesForm(int width) {
        GridLayout peptideFormLayout = new GridLayout(4, 5);
        peptideFormLayout.setWidth(width + "px");
        peptideFormLayout.setHeightUndefined();
        pepSequence = new InformationField("Peptide Sequence");
        peptideFormLayout.addComponent(pepSequence, 0, 0);

        sequenceAnnotated = new InformationField("Sequence Annotated");
        peptideFormLayout.addComponent(sequenceAnnotated, 1, 0);

        peptideModification = new InformationField("Peptide Modification");
        peptideFormLayout.addComponent(peptideModification, 2, 0);

        modificationComment = new InformationField("Modification Comment");
        peptideFormLayout.addComponent(modificationComment, 3, 0);

        foldChange = new InformationField("Fold Change");
        peptideFormLayout.addComponent(foldChange, 0, 1);

        pValue = new InformationField("p-value");
        peptideFormLayout.addComponent(pValue, 1, 1);

        pvalueSignificanceThreshold = new InformationField("p-value Threshold");
        peptideFormLayout.addComponent(pvalueSignificanceThreshold, 2, 1);

        pValueComm = new InformationField("Statistical Comments");
        peptideFormLayout.addComponent(pValueComm, 3, 1);

        quantBasisComment = new InformationField("Quantification Basis Comment");
        peptideFormLayout.addComponent(quantBasisComment, 0, 2);

        peptideCharge = new InformationField("Peptide Charge");
        peptideFormLayout.addComponent(peptideCharge, 1, 2);

        roc = new InformationField("ROC AUC");
        peptideFormLayout.addComponent(roc, 2, 2);

        additionalComments = new InformationField("Additional Comments");
        peptideFormLayout.addComponent(additionalComments, 3, 2);

        return peptideFormLayout;
    }

    private void updatePeptidesForm(QuantPeptide peptide) {

        pepSequence.setValue(peptide.getPeptideSequence(), null);
        sequenceAnnotated.setValue(peptide.getSequenceAnnotated(), null);
        peptideModification.setValue(peptide.getPeptideModification(), null);
        modificationComment.setValue(peptide.getModification_comment(), null);

        pvalueSignificanceThreshold.setValue(peptide.getPvalueSignificanceThreshold(), null);
        additionalComments.setValue(peptide.getAdditionalComments(), null);

        String pval;
        if (peptide.getString_p_value() != null && !peptide.getString_p_value().equalsIgnoreCase("") && !peptide.getString_p_value().equalsIgnoreCase("Not Available")) {
            pval = peptide.getString_p_value() + " ";
        } else {
            pval = "          ";
        }
        if (peptide.getP_value() != -1000000000) {
            pval += "(" + df.format(peptide.getP_value()) + ")";
        }
        pValue.setValue(pval, null);

        pValueComm.setValue(peptide.getP_value_comments(), null);

        String foldChane;
        if (peptide.getString_fc_value() != null && !peptide.getString_fc_value().equalsIgnoreCase("") && !peptide.getString_fc_value().equalsIgnoreCase("Not Available")) {
            foldChane = peptide.getString_fc_value() + " ";
            if (foldChane.trim().equalsIgnoreCase("Not regulated")) {
                foldChane = "No Change ";
            }
        } else {
            foldChane = "          ";
        }
        if (peptide.getFc_value() != -1000000000) {
            foldChane += "(" + df.format(peptide.getFc_value()) + ")";
        }
        foldChange.setValue(foldChane, null);
        String rocv;
        if (peptide.getRoc_auc() != -1000000000) {
            rocv = df.format(peptide.getRoc_auc()) + "";
        } else {
            rocv = "";
        }

        this.roc.setValue(rocv, null);

        String peptideChargeValue;
        if (peptide.getPeptideCharge() != -1) {
            peptideChargeValue = peptide.getPeptideCharge() + "";
        } else {
            peptideChargeValue = "";
        }
        this.peptideCharge.setValue(peptideChargeValue, null);

        quantBasisComment.setValue(peptide.getQuantBasisComment() + "", null);

    }

    private boolean checkIntersect(StackedBarPeptideComponent smallXComp, StackedBarPeptideComponent bigXComp) {
        int area = smallXComp.getX0() + smallXComp.getWidthArea();
        return bigXComp.getX0() < area;

    }

    private LinkedHashSet<StackedBarPeptideComponent> initAllBarChartComponents(boolean significatOnly, int width, String sequence, Set<QuantPeptide> quantPepSet, boolean count, boolean minMode) {

        final LinkedHashSet<StackedBarPeptideComponent> barComponentMap = new LinkedHashSet<StackedBarPeptideComponent>();

        double charW = (double) width / (double) sequence.length();
        for (QuantPeptide quantPeptide : quantPepSet) {
            if (quantPeptide.getPeptideSequence().equalsIgnoreCase("not available")) {
                continue;
            }
            noPeptide = false;

            String peptideSequence = quantPeptide.getPeptideSequence().trim();
            if (peptideSequence.contains(".")) {
                peptideSequence = quantPeptide.getPeptideSequence().replace(".", "").trim().substring(1, quantPeptide.getPeptideSequence().length() - 2);
            }
            double peptideLayoutWidth = Math.max(peptideSequence.length() * charW, 7);
            int x0 = (int) (sequence.split(peptideSequence)[0].length() * charW)+50;
            if (!significatOnly) {
                StackedBarPeptideComponent peptideStackedBarComponent = new StackedBarPeptideComponent(x0, (int) (peptideLayoutWidth - 2), quantPeptide.getUniqueId() + "", quantPeptide.getPeptideModification());
                peptideStackedBarComponent.setWidth(peptideLayoutWidth - 2 + "px");
                peptideStackedBarComponent.setDescription("Sequence: " + quantPeptide.getPeptideSequence());
                peptideStackedBarComponent.setParam("peptide", quantPeptide);
                peptideStackedBarComponent.setParam("sequence", quantPeptide.getPeptideSequence());
                if (count) {
                    this.totalPeptidesNumber++;
                }
                if (quantPeptide.getString_p_value().trim().equalsIgnoreCase("")) {
                    peptideStackedBarComponent.setSignificant(false);
                    peptideStackedBarComponent.setDefaultStyleShowAllMode("graystackedlayout");

                } else if (quantPeptide.getString_p_value().equalsIgnoreCase("Significant")) {
                    peptideStackedBarComponent.setSignificant(true);
                    if (quantPeptide.getString_fc_value().equalsIgnoreCase("Increased") || quantPeptide.getString_fc_value().equalsIgnoreCase("Increase") || quantPeptide.getString_fc_value().equalsIgnoreCase("Up")) {
                        peptideStackedBarComponent.setDefaultStyleShowAllMode("redstackedlayout");
                    } else if (quantPeptide.getString_fc_value() == null || quantPeptide.getString_fc_value().equalsIgnoreCase("") || quantPeptide.getString_fc_value().trim().equalsIgnoreCase("Not Provided")) {
                        peptideStackedBarComponent.setDefaultStyleShowAllMode("lightbluestackedlayout");
                    } else {
                        peptideStackedBarComponent.setDefaultStyleShowAllMode("greenstackedlayout");

                    }

                } else {
                    peptideStackedBarComponent.setSignificant(false);
                    if (quantPeptide.getString_fc_value().equalsIgnoreCase("Increased") || quantPeptide.getString_fc_value().equalsIgnoreCase("Increase") || quantPeptide.getString_fc_value().equalsIgnoreCase("Up")) {
                        peptideStackedBarComponent.setDefaultStyleShowAllMode("midredstackedlayout");
                    } else if (quantPeptide.getString_fc_value() == null || quantPeptide.getString_fc_value().equalsIgnoreCase("") || quantPeptide.getString_fc_value().trim().equalsIgnoreCase("Not Provided")) {
                        peptideStackedBarComponent.setDefaultStyleShowAllMode("lightbluestackedlayout");
                    } else {
                        peptideStackedBarComponent.setDefaultStyleShowAllMode("midgreenstackedlayout");

                    }
                }

                //add listener
                if (!minMode) {
                    peptideStackedBarComponent.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

                        private boolean firstSelection = true;

                        @Override
                        public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                            Component pepComp = event.getComponent();
                            if (firstSelection) {
                                for (StackedBarPeptideComponent sbar : barComponentMap) {
                                    sbar.heighlight(false);

                                }
                                firstSelection = false;
                                lastselectedPeptideComp = (StackedBarPeptideComponent) pepComp;
                                lastselectedPeptideComp.heighlight(true);
                                QuantPeptide peptide = (QuantPeptide) lastselectedPeptideComp.getParam("peptide");
                                updatePeptidesForm(peptide);
                                peptideForm.setVisible(true);
                                noselectionLabel.setVisible(false);
                                return;

                            }

                            if (pepComp instanceof StackedBarPeptideComponent) {
                                StackedBarPeptideComponent pepBarComp = (StackedBarPeptideComponent) pepComp;
                                if (lastselectedPeptideComp == pepBarComp) {//                             
                                    lastselectedPeptideComp = null;
                                    for (StackedBarPeptideComponent sbar : barComponentMap) {
                                        sbar.heighlight(null);
                                    }
                                    firstSelection = true;
                                    peptideForm.setVisible(false);
                                    noselectionLabel.setVisible(true);
                                    return;
                                }

                                if (lastselectedPeptideComp != null) {
                                    lastselectedPeptideComp.heighlight(false);
                                }

                                pepBarComp.heighlight(true);
                                lastselectedPeptideComp = pepBarComp;
                                QuantPeptide peptide = (QuantPeptide) pepBarComp.getParam("peptide");
                                updatePeptidesForm(peptide);

                                peptideForm.setVisible(true);
                                noselectionLabel.setVisible(false);

                            }
                        }
                    });
                }
                barComponentMap.add(peptideStackedBarComponent);

            } else {
                if (quantPeptide.getString_p_value().equalsIgnoreCase("Significant")) {
                    significantPeptidesNumber++;
                    StackedBarPeptideComponent peptideStackedBarComponent = new StackedBarPeptideComponent(x0, (int) (peptideLayoutWidth - 2), quantPeptide.getUniqueId() + "", quantPeptide.getPeptideModification());
                    peptideStackedBarComponent.setSignificant(true);
                    peptideStackedBarComponent.setWidth(peptideLayoutWidth - 2 + "px");
                    peptideStackedBarComponent.setDescription("Sequence: " + quantPeptide.getPeptideSequence());//                
                    peptideStackedBarComponent.setParam("peptide", quantPeptide);
                    peptideStackedBarComponent.setParam("sequence", quantPeptide.getPeptideSequence());

                    if (quantPeptide.getString_fc_value().equalsIgnoreCase("Increased") || quantPeptide.getString_fc_value().equalsIgnoreCase("Up")) {
                        peptideStackedBarComponent.setDefaultStyleShowAllMode("redstackedlayout");
                    } else if (quantPeptide.getString_fc_value() == null || quantPeptide.getString_fc_value().equalsIgnoreCase("") || quantPeptide.getString_fc_value().trim().equalsIgnoreCase("Not Provided")) {
                        peptideStackedBarComponent.setDefaultStyleShowAllMode("lightbluestackedlayout");
                    } else {
                        peptideStackedBarComponent.setDefaultStyleShowAllMode("greenstackedlayout");

                    }

                    if (!minMode) {
                        peptideStackedBarComponent.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
                            private boolean firstSelection = true;

                            @Override
                            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                                Component pepComp = event.getComponent();
//                               
                                if (firstSelection) {
                                    for (StackedBarPeptideComponent sbar : barComponentMap) {

                                        sbar.heighlight(false);

                                    }
                                    firstSelection = false;
                                    lastselectedPeptideComp = (StackedBarPeptideComponent) pepComp;
                                    lastselectedPeptideComp.heighlight(true);
                                    QuantPeptide peptide = (QuantPeptide) lastselectedPeptideComp.getParam("peptide");
                                    updatePeptidesForm(peptide);
                                    peptideForm.setVisible(true);
                                    noselectionLabel.setVisible(false);
                                    return;

                                }

                                if (pepComp instanceof StackedBarPeptideComponent) {
                                    StackedBarPeptideComponent pepBarComp = (StackedBarPeptideComponent) pepComp;
                                    if (lastselectedPeptideComp == pepBarComp) {
                                        peptideForm.setVisible(false);
                                        noselectionLabel.setVisible(true);
                                        lastselectedPeptideComp = null;
                                        for (StackedBarPeptideComponent sbar : barComponentMap) {
                                            sbar.heighlight(null);
                                        }
                                        firstSelection = true;
                                        return;
                                    }
                                    if (lastselectedPeptideComp != null) {
                                        lastselectedPeptideComp.heighlight(false);
                                    }
                                    pepBarComp.heighlight(true);
                                    lastselectedPeptideComp = pepBarComp;
                                    QuantPeptide peptide = (QuantPeptide) pepBarComp.getParam("peptide");
                                    updatePeptidesForm(peptide);
                                    peptideForm.setVisible(true);
                                    noselectionLabel.setVisible(false);
                                }

                            }
                        });
                    }
                    barComponentMap.add(peptideStackedBarComponent);
                }
            }

        }
        final LinkedHashSet<StackedBarPeptideComponent> updatedBarComponentMap = new LinkedHashSet<StackedBarPeptideComponent>();
        for (StackedBarPeptideComponent sbar : barComponentMap) {
            sbar.heighlight(null);
            updatedBarComponentMap.add(sbar);
        }
        return updatedBarComponentMap;
    }

    private List< StackedBarPeptideComponent> stackedPeptides = new ArrayList<StackedBarPeptideComponent>();

    private int level = 1;

    private void initPeptidesStackedBarComponentsLayout(LinkedHashSet<StackedBarPeptideComponent> stackedBarComponents, AbsoluteLayout peptidesComponentsContainer, AbsoluteLayout peptidesContainer) {
        int top = 0;
        List< StackedBarPeptideComponent> initLevel = new ArrayList<StackedBarPeptideComponent>(stackedBarComponents);
        List< StackedBarPeptideComponent> updatedLevel = new ArrayList<StackedBarPeptideComponent>(stackedBarComponents);
        List< StackedBarPeptideComponent> nextLevel = new ArrayList<StackedBarPeptideComponent>();

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
                    peptidesComponentsContainer.addComponent(pepBarComp, "left: " + pepBarComp.getX0() + "px; top: " + (top + 10) + "px;");
                    if (pepBarComp.isPtmAvailable()) {
                        peptidesContainer.addComponent(pepBarComp.getPtmLayout(), "left: " + (pepBarComp.getX0() + (pepBarComp.getWidth() / 2) - 5) + "px; top: " + (top - 4) + "px;");
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
        top = top + 30;
        peptidesComponentsContainer.setHeight(Math.max(40, top) + "px");

    }

    public List<StackedBarPeptideComponent> getStackedPeptides() {
        return stackedPeptides;
    }

    /**
     *
     * @return
     */
    public int getSignificantPeptidesNumber() {
        return significantPeptidesNumber;
    }

    /**
     *
     * @return
     */
    public int getTotalPeptidesNumber() {
        return totalPeptidesNumber;
    }

    public boolean isHasPTM() {
        return hasPTM;
    }

}
