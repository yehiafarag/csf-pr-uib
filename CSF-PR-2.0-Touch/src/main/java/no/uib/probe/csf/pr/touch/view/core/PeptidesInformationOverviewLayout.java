package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import no.uib.probe.csf.pr.touch.logic.beans.QuantPeptide;

/**
 *
 * @author Yehia Farag
 */
public class PeptidesInformationOverviewLayout extends VerticalLayout {

    private InformationField quantBasisComment, pepSequence, peptideModification, modificationComment, pValue, pValueComm, foldChange, roc, additionalComments, pvalueSignificanceThreshold, sequenceAnnotated, peptideCharge;
    private DecimalFormat df = null;
    private final VerticalLayout peptideForm;

    public PeptidesInformationOverviewLayout(int width, QuantPeptide quantPeptide) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        otherSymbols.setGroupingSeparator('.');
        df = new DecimalFormat("#.###", otherSymbols);
        this.setWidth(width, Unit.PIXELS);
        this.setHeightUndefined();
        this.setSpacing(true);
        peptideForm = initPeptidesForm();
        this.addComponent(peptideForm);
        this.updatePeptidesForm(quantPeptide);
    }

    private VerticalLayout initPeptidesForm() {
        VerticalLayout mainContainer = new VerticalLayout();
        mainContainer.setWidth(100, Unit.PERCENTAGE);
        mainContainer.setHeightUndefined();
        mainContainer.setSpacing(true);

        HorizontalLayout rowI = new HorizontalLayout();
        rowI.setWidth(100, Unit.PERCENTAGE);
        rowI.setHeightUndefined();
        mainContainer.addComponent(rowI);

        pepSequence = new InformationField("Peptide Sequence");
        rowI.addComponent(pepSequence);

        sequenceAnnotated = new InformationField("Sequence Annotated");
        rowI.addComponent(sequenceAnnotated);

        peptideModification = new InformationField("Peptide Modification");
        rowI.addComponent(peptideModification);

        modificationComment = new InformationField("Modification Comment");
        rowI.addComponent(modificationComment);

        HorizontalLayout rowII = new HorizontalLayout();
        rowII.setWidth(100, Unit.PERCENTAGE);
        rowII.setHeightUndefined();
        mainContainer.addComponent(rowII);
        foldChange = new InformationField("Fold Change");
        rowII.addComponent(foldChange);

        pValue = new InformationField("p-value");
        rowII.addComponent(pValue);

        pvalueSignificanceThreshold = new InformationField("p-value Threshold");
        rowII.addComponent(pvalueSignificanceThreshold);

        pValueComm = new InformationField("Statistical Comments");
        rowII.addComponent(pValueComm);

        HorizontalLayout rowIII = new HorizontalLayout();
        rowIII.setWidth(100, Unit.PERCENTAGE);
        rowIII.setHeightUndefined();
        mainContainer.addComponent(rowIII);
        quantBasisComment = new InformationField("Quantification Basis Comment");
        rowIII.addComponent(quantBasisComment);

        peptideCharge = new InformationField("Peptide Charge");
        rowIII.addComponent(peptideCharge);

        roc = new InformationField("ROC AUC");
        rowIII.addComponent(roc);

        additionalComments = new InformationField("Additional Comments");
        rowIII.addComponent(additionalComments);

        return mainContainer;
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

}
