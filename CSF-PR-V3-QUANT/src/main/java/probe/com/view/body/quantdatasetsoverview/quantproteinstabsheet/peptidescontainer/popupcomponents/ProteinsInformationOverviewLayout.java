/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.peptidescontainer.popupcomponents;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import probe.com.model.beans.quant.QuantProtein;
import probe.com.view.core.InformationField;

/**
 *
 * @author yfa041
 */
public class ProteinsInformationOverviewLayout extends VerticalLayout {

    private DecimalFormat df = null;

    /**
     * @param width
     */
    public ProteinsInformationOverviewLayout(int width) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        otherSymbols.setGroupingSeparator('.');
        df = new DecimalFormat("#.###", otherSymbols);
        this.setWidth(width + "px");
        this.setHeightUndefined();
        this.setSpacing(true);
        this.setMargin(new MarginInfo(false, false, false, false));
        this.setStyleName(Reindeer.LAYOUT_WHITE);
        proteinsForm = initProteinsForm(width);
        proteinsForm.setVisible(false);
        this.addComponent(proteinsForm);

    }

    private final GridLayout proteinsForm;
    private InformationField quantBasisComment,accsession, name, quantPeptidesNumber, idPeptidesNumber, pValue, pValueComm, foldChange, roc, additionalComments, pvalueSignificanceThreshold;

    private GridLayout initProteinsForm(int width) {
        GridLayout proteinsFormLayout = new GridLayout(4, 5);
        proteinsFormLayout.setWidth(width + "px");
        proteinsFormLayout.setHeightUndefined();
        accsession = new InformationField("Accession");
        proteinsFormLayout.addComponent(accsession, 0, 0);

        name = new InformationField("Protein Name");
        proteinsFormLayout.addComponent(name, 1, 0);

        foldChange = new InformationField("Fold Change");
        proteinsFormLayout.addComponent(foldChange, 2, 0);

        roc = new InformationField("ROC AUC");
        proteinsFormLayout.addComponent(roc, 3, 0);

        pValue = new InformationField("p-value");
        proteinsFormLayout.addComponent(pValue, 0, 1);

        pvalueSignificanceThreshold = new InformationField("p-value Threshold");
        proteinsFormLayout.addComponent(pvalueSignificanceThreshold, 1, 1);

        pValueComm = new InformationField("Statistical Comments");
        proteinsFormLayout.addComponent(pValueComm, 2, 1);

        quantPeptidesNumber = new InformationField("#Quant Peptides");
        proteinsFormLayout.addComponent(quantPeptidesNumber, 3, 1);

        idPeptidesNumber = new InformationField("#Identified Peptides");
        proteinsFormLayout.addComponent(idPeptidesNumber, 0, 2);

        additionalComments = new InformationField("Additional Comments");
        proteinsFormLayout.addComponent(additionalComments, 1, 2);
        
         quantBasisComment = new InformationField("Quantification Basis Comment");
        proteinsFormLayout.addComponent(quantBasisComment, 2, 2);
        

        return proteinsFormLayout;
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
