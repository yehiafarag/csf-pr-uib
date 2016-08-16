/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import no.uib.probe.csf.pr.touch.logic.beans.QuantProtein;

/**
 *
 * @author Yehia Farag
 *
 * this class represents the protein information layout
 */
public class ProteinsInformationOverviewLayout extends VerticalLayout {

    private DecimalFormat df = null;

    /**
     * @param width
     */
    public ProteinsInformationOverviewLayout() {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        otherSymbols.setGroupingSeparator('.');
        df = new DecimalFormat("#.###", otherSymbols);
        this.setWidth(100, Unit.PERCENTAGE);
        this.setHeightUndefined();
        this.setSpacing(true);
        this.setMargin(false);
        this.setStyleName("whitelayout");
        proteinsForm = initProteinsForm();
        proteinsForm.setVisible(false);
        this.addComponent(proteinsForm);

    }

    private final VerticalLayout proteinsForm;
    private InformationField quantBasisComment, accsession, name, quantPeptidesNumber, idPeptidesNumber, pValue, pValueComm, foldChange, roc, additionalComments, pvalueSignificanceThreshold;

    private VerticalLayout initProteinsForm() {
        VerticalLayout mainContainer = new VerticalLayout();
        mainContainer.setWidth(100, Unit.PERCENTAGE);
        mainContainer.setHeightUndefined();
        mainContainer.setSpacing(true);

        HorizontalLayout rowI = new HorizontalLayout();
        rowI.setWidth(100, Unit.PERCENTAGE);
        rowI.setHeightUndefined();
        mainContainer.addComponent(rowI);
//        proteinsFormLayout.setHeightUndefined();
        accsession = new InformationField("Accession");
        rowI.addComponent(accsession);

        name = new InformationField("Protein Name");
        rowI.addComponent(name);

        quantPeptidesNumber = new InformationField("#Quant Peptides");
        rowI.addComponent(quantPeptidesNumber);

        idPeptidesNumber = new InformationField("#Identified Peptides");
        rowI.addComponent(idPeptidesNumber);

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

        roc = new InformationField("ROC AUC");
        rowIII.addComponent(roc);

        quantBasisComment = new InformationField("Quantification Basis Comment");
        rowIII.addComponent(quantBasisComment);

        additionalComments = new InformationField("Additional Comments");
        rowIII.addComponent(additionalComments);

        rowIII.addComponent(new VerticalLayout());

        return mainContainer;
    }

    public void updateProteinsForm(QuantProtein quantProtein, String accession, String url, String protName) {

        accsession.setValue(accession, url);
        name.setValue(protName, url);
        pvalueSignificanceThreshold.setVisible(true);
        additionalComments.setVisible(true);

        pvalueSignificanceThreshold.setValue(quantProtein.getPvalueSignificanceThreshold(), null);
        additionalComments.setValue(quantProtein.getAdditionalComments(), null);

        String pval;
        if (quantProtein.getStringPValue() != null && !quantProtein.getStringPValue().equalsIgnoreCase("") && !quantProtein.getStringPValue().equalsIgnoreCase("Not Available")) {
            pval = quantProtein.getStringPValue() + " ";
        } else {
            pval = "          ";
        }
        if (quantProtein.getpValue() != -1000000000) {
            pval += "(" + df.format(quantProtein.getpValue()) + ")";
        }
        pValue.setValue(pval, null);

        pValueComm.setValue(quantProtein.getPvalueComment(), null);

        String strFoldChange;
        if (quantProtein.getStringFCValue() != null && !quantProtein.getStringFCValue().equalsIgnoreCase("") && !quantProtein.getStringFCValue().equalsIgnoreCase("Not Available")) {
            strFoldChange = quantProtein.getStringFCValue() + " ";
            if (strFoldChange.trim().equalsIgnoreCase("Not regulated")) {
                strFoldChange = "No Change ";
            }
        } else {
            strFoldChange = "          ";
        }
        if (quantProtein.getFcPatientGroupIonPatientGroupII() != -1000000000) {
            strFoldChange += "(" + df.format(quantProtein.getFcPatientGroupIonPatientGroupII()) + ")";
        }
        foldChange.setValue(strFoldChange, null);
        String rocv;
        if (quantProtein.getRocAuc() != -1000000000) {
            rocv = df.format(quantProtein.getRocAuc()) + "";
        } else {
            rocv = "";
        }

        this.roc.setValue(rocv, null);

        int quantPepNumber = quantProtein.getQuantifiedPeptidesNumber();
        if (quantPepNumber > 0) {
            quantPeptidesNumber.setValue(quantPepNumber, null);
        }
        quantBasisComment.setValue(quantProtein.getQuantBasisComment() + "", null);

        this.proteinsForm.setVisible(true);
    }

}
