package probe.com.view.body;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import java.io.Serializable;
import java.util.Map;
import probe.com.handlers.CSFPRHandler;
import probe.com.model.beans.identification.IdentificationDatasetDetailsBean;
import probe.com.view.body.identificationdatasetsoverview.IdentificationDatasetInformationLayout;
import probe.com.view.core.HideOnClickLayout;

/**
 *
 * @author Yehia Farag
 *
 * main layout for identification tab
 */
public class IdentificationDatasetsLayout extends VerticalLayout implements Serializable {

    private final CSFPRHandler csfprHandler;

    /**
     *
     * @param csfprHandler
     * @param mainTabSheet
     */
    public IdentificationDatasetsLayout(CSFPRHandler csfprHandler, TabSheet mainTabSheet) {
        this.csfprHandler = csfprHandler;
        this.setSpacing(true);
        this.setMargin(true);

        //no id data available
        if (csfprHandler.getIdentificationDatasetList() == null || csfprHandler.getIdentificationDatasetList().isEmpty()) {
            Label noExpLable = new Label("<h4 style='font-family:verdana;color:black;font-weight:bold;'>Sorry No Dataset Availabe Now !</h4>");
            noExpLable.setContentMode(ContentMode.HTML);
            this.addComponent(noExpLable);
        } else {
            Map<Integer, IdentificationDatasetDetailsBean> dsList = csfprHandler.getIdentificationDatasetDetailsList();
            for (int x : dsList.keySet()) {
                HideOnClickLayout dslayout = initIdentificationDatasetRowLayout(x, dsList.get(x), mainTabSheet);
                this.addComponent(dslayout);
                Label spacer = new Label();
                spacer.setContentMode(ContentMode.HTML);
                spacer.setStyleName("spacer");
                this.addComponent(spacer);

            }
        }

    }

    /**
     * initialize the identification datasets list layout
     *
     * @param dsId dataset id
     * @param identificationDataset
     * @param mainTabSheet
     */
    private HideOnClickLayout initIdentificationDatasetRowLayout(int dsId, IdentificationDatasetDetailsBean identificationDataset, TabSheet mainTabSheet) {
         String infoText="Select an experiment in the roll down menu on top to view all proteins identified in the selected experiment. Select a protein to see below all Peptides identified for the protein, and if the experiment was based on SDS-PAGE, the protein’s distribution in the gel is displayed under Fractions. To show information about the experiment, press Dataset Information.  Use the search box to navigate in the experiment selected.</p><p  style='font-family:verdana;color:black;margin-left:20px;margin-right:20px;'>Under Fractions, bar charts show the distribution of the selected protein across the fractions cut from the gel. Three charts show number of peptides, number of spectra and average precursor intensity. The fraction number represents the gel pieces cut from top to bottom. Protein standards <font color='#CDE1FF'>(light blue bars)</font> indicate the molecular weight range of each fraction. <font color='#79AFFF'>Darker blue bars</font> mark between which two standards the protein's theoretical mass suggests the protein should be found.";

        IdentificationDatasetInformationLayout IdentificationDatasetInfoLayout = new IdentificationDatasetInformationLayout(csfprHandler, dsId, mainTabSheet);
        HideOnClickLayout dsLayout = new HideOnClickLayout(identificationDataset.getName(), IdentificationDatasetInfoLayout, IdentificationDatasetInfoLayout.getMiniLayout(),infoText);
        dsLayout.setMargin(new MarginInfo(false, false, true, false));
        return dsLayout;
    }

}
