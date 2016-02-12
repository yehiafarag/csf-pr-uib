package probe.com.view.body;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
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

    private final CSFPRHandler CSFPR_Handler;
    private final VerticalLayout topLabelMarker;

    public VerticalLayout getTopLabelMarker() {
        return topLabelMarker;
    }

    /**
     *
     * @param CSFPR_Handler
     * @param mainTabSheet
     */
    public IdentificationDatasetsLayout(CSFPRHandler CSFPR_Handler, TabSheet mainTabSheet) {
        this.CSFPR_Handler = CSFPR_Handler;
        topLabelMarker = new VerticalLayout();
        this.addComponent(topLabelMarker);
        this.setExpandRatio(topLabelMarker, 0.01f);
        topLabelMarker.setHeight("10px");
        topLabelMarker.setWidth("20px");
        topLabelMarker.setStyleName(Reindeer.LAYOUT_WHITE);

        this.setSpacing(true);
        this.setMargin(true);

        //no id data available
        if (CSFPR_Handler.getIdentificationDatasetList() == null || CSFPR_Handler.getIdentificationDatasetList().isEmpty()) {
            Label noExpLable = new Label("<h4 style='font-family:verdana;color:black;font-weight:bold;'>Sorry No Dataset Availabe Now !</h4>");
            noExpLable.setContentMode(ContentMode.HTML);
            this.addComponent(noExpLable);
        } else {
            Map<Integer, IdentificationDatasetDetailsBean> dsList = CSFPR_Handler.getIdentificationDatasetDetailsList();
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

        IdentificationDatasetInformationLayout IdentificationDatasetInfoLayout = new IdentificationDatasetInformationLayout(CSFPR_Handler, dsId, mainTabSheet);
        HideOnClickLayout dsLayout = new HideOnClickLayout(identificationDataset.getName(), IdentificationDatasetInfoLayout, IdentificationDatasetInfoLayout.getMiniLayout(), "", null);
        dsLayout.setMargin(new MarginInfo(false, false, true, false));
        return dsLayout;
    }

}
