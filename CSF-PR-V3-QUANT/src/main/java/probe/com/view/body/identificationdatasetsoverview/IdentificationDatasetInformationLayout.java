
package probe.com.view.body.identificationdatasetsoverview;

import com.vaadin.server.Page;
import probe.com.view.core.exporter.ExporterBtnsGenerator;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;
import probe.com.handlers.CSFPRHandler;

/**
 *
 * @author Yehia Farag
 */
public class IdentificationDatasetInformationLayout extends VerticalLayout implements Serializable, Button.ClickListener {

    private final VerticalLayout miniLayout = new VerticalLayout();
    private final int datasetId;
    private final TabSheet mainTabSheet;
    private final CSFPRHandler handler;
    private Tab identificationLayoutTab = null;
    private IdentificationDatasetLayout datasetLayout;

    /**
     *
     * @param handler
     * @param datasetId
     * @param mainTabSheet
     */
    public IdentificationDatasetInformationLayout(final CSFPRHandler handler, final int datasetId, TabSheet mainTabSheet) {
        
        this.datasetId = datasetId;
        this.mainTabSheet = mainTabSheet;
        this.handler = handler;
        this.setSpacing(true);
        this.setMargin(true);
        this.setSizeFull();
        miniLayout.setSpacing(true);
        miniLayout.setMargin(false);
        miniLayout.setWidth("100%");
        HorizontalLayout miniLabelsGroupILayout = new HorizontalLayout();
        miniLabelsGroupILayout.setSpacing(true);
        miniLabelsGroupILayout.setMargin(new MarginInfo(false, false, false, false));
        miniLayout.addComponent(miniLabelsGroupILayout);
        miniLayout.setComponentAlignment(miniLabelsGroupILayout, Alignment.MIDDLE_CENTER);

        miniLabelsGroupILayout.addComponent(this.generateMiniLabel("Instrument Type:", handler.getDataset(datasetId).getInstrumentType()));
        miniLabelsGroupILayout.addComponent(this.generateMiniLabel("Species:", handler.getDataset(datasetId).getSpecies()));
        HorizontalLayout miniProtNumberLayout = this.generateMiniLabel("Proteins Number:", "" + handler.getDataset(datasetId).getNumberValidProt());
        miniProtNumberLayout.setDescription("Number of validated proteins");
        miniLabelsGroupILayout.addComponent(miniProtNumberLayout);
        miniLabelsGroupILayout.addComponent(this.generateMiniLabel("Sample Processing:", handler.getDataset(datasetId).getSampleProcessing()));

        HorizontalLayout miniLabelsGroupIILayout = new HorizontalLayout();
        miniLabelsGroupIILayout.setSpacing(true);

        miniLabelsGroupIILayout.setMargin(new MarginInfo(false, false, false, false));
        miniLayout.addComponent(miniLabelsGroupIILayout);
        miniLayout.setComponentAlignment(miniLabelsGroupIILayout, Alignment.MIDDLE_CENTER);

        miniLabelsGroupIILayout.addComponent(this.generateMiniLabel("Sample Type:", handler.getDataset(datasetId).getSampleType()));

        miniLabelsGroupIILayout.addComponent(this.generateMiniLabel("Frag Mode:", handler.getDataset(datasetId).getFragMode()));
        HorizontalLayout miniPepNumberLayout = this.generateMiniLabel("Peptides Number:", "" + handler.getDataset(datasetId).getPeptidesNumber());
        miniPepNumberLayout.setDescription("Number of validated peptides");
        miniLabelsGroupIILayout.addComponent(miniPepNumberLayout);
        miniLabelsGroupIILayout.addComponent(this.generateMiniLabel("Gel Fraction Numbers:", "" + handler.getDataset(datasetId).getFractionsNumber()));

        HorizontalLayout topLabelsGroupILayout = new HorizontalLayout();
        topLabelsGroupILayout.setSpacing(true);
        topLabelsGroupILayout.setMargin(new MarginInfo(false, true, false, false));
        this.addComponent(topLabelsGroupILayout);
        this.addComponent(topLabelsGroupILayout);

        topLabelsGroupILayout.addComponent(this.generateLabel("Dataset  Name: ", handler.getDataset(datasetId).getName()));
        topLabelsGroupILayout.addComponent(this.generateLabel("Uploaded By:", handler.getDataset(datasetId).getUploadedByName()));
        topLabelsGroupILayout.addComponent(this.generateLabel("E-mail:", handler.getDataset(datasetId).getEmail().toLowerCase()));
        topLabelsGroupILayout.addComponent(new VerticalLayout());
        topLabelsGroupILayout.setExpandRatio(topLabelsGroupILayout.getComponent(0), 3);
        topLabelsGroupILayout.setExpandRatio(topLabelsGroupILayout.getComponent(1), 2);

        topLabelsGroupILayout.setExpandRatio(topLabelsGroupILayout.getComponent(2), 2);

        topLabelsGroupILayout.setExpandRatio(topLabelsGroupILayout.getComponent(3), 0.5f);

        this.addComponent(generateLabel("Description:", handler.getDataset(datasetId).getDescription()));

        final HorizontalLayout bottomLabelsGroupILayout = new HorizontalLayout();
        bottomLabelsGroupILayout.setSpacing(true);
        bottomLabelsGroupILayout.setMargin(new MarginInfo(false, true, false, false));
        this.addComponent(bottomLabelsGroupILayout);

        bottomLabelsGroupILayout.addComponent(this.generateLabel("Instrument Type:", handler.getDataset(datasetId).getInstrumentType()));
        bottomLabelsGroupILayout.addComponent(this.generateLabel("Species:", handler.getDataset(datasetId).getSpecies()));
        HorizontalLayout protNumberLayout = this.generateLabel("Proteins Number:", "" + handler.getDataset(datasetId).getNumberValidProt());
        protNumberLayout.setDescription("Number of validated proteins");
        bottomLabelsGroupILayout.addComponent(protNumberLayout);
       
        
          
        ExporterBtnsGenerator dataExporter = new ExporterBtnsGenerator(handler);
        VerticalLayout datasetProteinsExportLayout = dataExporter.exportDatasetProteins(datasetId, false, "Export Proteins");
        this.addComponent(datasetProteinsExportLayout);
        datasetProteinsExportLayout.setWidth("100px");
        bottomLabelsGroupILayout.addComponent(datasetProteinsExportLayout);
        
        
        

        final HorizontalLayout bottomLabelsGroupIILayout = new HorizontalLayout();
        bottomLabelsGroupIILayout.setSpacing(true);

        bottomLabelsGroupIILayout.setMargin(new MarginInfo(false, true, false, false));
        this.addComponent(bottomLabelsGroupIILayout);

        bottomLabelsGroupIILayout.addComponent(this.generateLabel("Sample Processing:", handler.getDataset(datasetId).getSampleProcessing()));
        bottomLabelsGroupIILayout.addComponent(this.generateLabel("Frag Mode:", handler.getDataset(datasetId).getFragMode()));
        HorizontalLayout pepNumberLayout = this.generateLabel("Peptides Number:", "" + handler.getDataset(datasetId).getPeptidesNumber());
        pepNumberLayout.setDescription("Number of validated peptides");
        bottomLabelsGroupIILayout.addComponent(pepNumberLayout);
        
        
  
        VerticalLayout datasetPeptidesExportLayout = dataExporter.exportDatasetPeptides(datasetId, false, "Export Peptides");
        datasetPeptidesExportLayout.setWidth("100px");
        bottomLabelsGroupIILayout.addComponent(datasetPeptidesExportLayout);
        

        HorizontalLayout bottomLabelsGroupIIILayout = new HorizontalLayout();
        bottomLabelsGroupIIILayout.setSpacing(true);

        bottomLabelsGroupIIILayout.setMargin(new MarginInfo(false, true, false, false));
        this.addComponent(bottomLabelsGroupIIILayout);
        bottomLabelsGroupIIILayout.addComponent(this.generateLabel("Sample Type:", handler.getDataset(datasetId).getSampleType()));

        String href ;
        if (handler.getDataset(datasetId).getPublicationLink().equalsIgnoreCase("NOT AVAILABLE") || handler.getDataset(datasetId).getPublicationLink().equalsIgnoreCase("")) {

            bottomLabelsGroupIIILayout.addComponent(this.generateLabel("Publication Link:", "Not Available"));

        } else {
            href = handler.getDataset(datasetId).getPublicationLink().toLowerCase();
            if ((!href.contains("http://")) && (!href.contains("https://"))) {
                href = "http://" + href;
            }

            bottomLabelsGroupIIILayout.addComponent(this.generateLabel("Publication Link:", "<a href='" + href + "'  target='_blank'>Click to Visit</a>"));
        }
        bottomLabelsGroupIIILayout.addComponent(this.generateLabel("Gel Fraction Numbers:", "" + handler.getDataset(datasetId).getFractionsNumber()));

        if (mainTabSheet != null) {
            Button loadDatasetBtn = new Button("Load Dataset");
            loadDatasetBtn.setStyleName(Reindeer.BUTTON_SMALL);
            loadDatasetBtn.setWidth("105px");
            loadDatasetBtn.addClickListener(IdentificationDatasetInformationLayout.this);
            bottomLabelsGroupIIILayout.addComponent(loadDatasetBtn);
        } else {
            bottomLabelsGroupIIILayout.addComponent(new VerticalLayout());
        }

        topLabelsGroupILayout.setWidth("100%");
        bottomLabelsGroupILayout.setWidth("100%");
        bottomLabelsGroupILayout.setExpandRatio(bottomLabelsGroupILayout.getComponent(0), 3);
        bottomLabelsGroupILayout.setExpandRatio(bottomLabelsGroupILayout.getComponent(1), 2);
        bottomLabelsGroupILayout.setExpandRatio(bottomLabelsGroupILayout.getComponent(2), 2);
        bottomLabelsGroupILayout.setExpandRatio(datasetProteinsExportLayout, 0.5f);

        bottomLabelsGroupIILayout.setWidth("100%");
        bottomLabelsGroupIILayout.setExpandRatio(bottomLabelsGroupIILayout.getComponent(0), 3);
        bottomLabelsGroupIILayout.setExpandRatio(bottomLabelsGroupIILayout.getComponent(1), 2);
        bottomLabelsGroupIILayout.setExpandRatio(bottomLabelsGroupIILayout.getComponent(2), 2);
        bottomLabelsGroupIILayout.setExpandRatio(datasetPeptidesExportLayout, 0.5f);

        bottomLabelsGroupIIILayout.setWidth("100%");
        bottomLabelsGroupIIILayout.setExpandRatio(bottomLabelsGroupIIILayout.getComponent(0), 3);
        bottomLabelsGroupIIILayout.setExpandRatio(bottomLabelsGroupIIILayout.getComponent(1), 2);
        bottomLabelsGroupIIILayout.setExpandRatio(bottomLabelsGroupIIILayout.getComponent(2), 2);
        bottomLabelsGroupIIILayout.setExpandRatio(bottomLabelsGroupIIILayout.getComponent(3), 0.5f);

        miniLabelsGroupILayout.setExpandRatio(miniLabelsGroupILayout.getComponent(0), 1);
        miniLabelsGroupILayout.setExpandRatio(miniLabelsGroupILayout.getComponent(1), 1);
        miniLabelsGroupILayout.setExpandRatio(miniLabelsGroupILayout.getComponent(2), 1);
        miniLabelsGroupILayout.setExpandRatio(miniLabelsGroupILayout.getComponent(3), 3);

        miniLabelsGroupIILayout.setExpandRatio(miniLabelsGroupIILayout.getComponent(0), 1);
        miniLabelsGroupIILayout.setExpandRatio(miniLabelsGroupIILayout.getComponent(1), 1);
        miniLabelsGroupIILayout.setExpandRatio(miniLabelsGroupIILayout.getComponent(2), 1);
        miniLabelsGroupIILayout.setExpandRatio(miniLabelsGroupIILayout.getComponent(3), 3);
        
    }

    private Label generateTitleLabel(String headerTitle) {
        Label titleLabel = new Label(headerTitle);
        titleLabel.setContentMode(ContentMode.HTML);
        titleLabel.setStyleName("datasetInfoHeaders");
        return titleLabel;

    }

    private Label generateValueLabel(String value) {

        if (value.length() > 300) {
            value = "<textarea readonly>" + value + "</textarea>";

        }
        int w =Page.getCurrent().getWebBrowser().getScreenWidth()-(Page.getCurrent().getWebBrowser().getScreenWidth()/4);
        Page.getCurrent().getStyles().add(".datasetInfoValues textArea{ width:"+ (w)+"px;}");

        Label valueLabel = new Label(value);

        valueLabel.setContentMode(ContentMode.HTML);
        valueLabel.setStyleName("datasetInfoValues");
        return valueLabel;

    }

    private HorizontalLayout generateLabel(String title, String value) {
        Label datasetNameHeader = generateTitleLabel(title);
        Label datasetNameValue = generateValueLabel(value);
        HorizontalLayout datasetNameLayout = new HorizontalLayout();
        datasetNameLayout.addComponent(datasetNameHeader);
        datasetNameLayout.addComponent(datasetNameValue);
        datasetNameLayout.setSpacing(true);
        return datasetNameLayout;

    }

    private HorizontalLayout generateMiniLabel(String title, String value) {
        Label titleLabel = new Label(title);
        titleLabel.setContentMode(ContentMode.HTML);
        titleLabel.setStyleName("miniDatasetInfoHeaders");

        Label valueLabel = new Label(value);
        valueLabel.setContentMode(ContentMode.HTML);
        valueLabel.setStyleName("miniDatasetInfoValues");

        HorizontalLayout datasetlabelLayout = new HorizontalLayout();
        datasetlabelLayout.addComponent(titleLabel);
        datasetlabelLayout.addComponent(valueLabel);
        datasetlabelLayout.setSpacing(false);
        return datasetlabelLayout;

    }

    /**
     *
     * @return
     */
    public VerticalLayout getMiniLayout() {
        return miniLayout;
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (datasetLayout == null) {            
            datasetLayout = new IdentificationDatasetLayout(handler, datasetId);
        }
        if (identificationLayoutTab == null || mainTabSheet.getTabPosition(identificationLayoutTab) < 0) {
            identificationLayoutTab = mainTabSheet.addTab(datasetLayout, handler.getDataset(datasetId).getName(), null);
            identificationLayoutTab.setClosable(true);
        }
        mainTabSheet.setSelectedTab(identificationLayoutTab);

    }

}
