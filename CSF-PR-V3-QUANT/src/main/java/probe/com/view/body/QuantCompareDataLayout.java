/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body;

import com.vaadin.data.Property;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import probe.com.handlers.CSFPRHandler;
import probe.com.model.beans.quant.QuantDatasetInitialInformationObject;
import probe.com.model.beans.quant.QuantDatasetObject;
import probe.com.view.core.HideOnClickLayout;

/**
 *
 * @author Yehia Farag
 *
 * This Class represents Searching and Compare user's quant dataset with csf-pr
 * v2 quant data
 */
public class QuantCompareDataLayout extends VerticalLayout implements Button.ClickListener {

    private final CSFPRHandler CSFPR_Handler;
    private final HideOnClickLayout selectDiseaseGroupsContainer, proteinsDataCaptureLayout;
    private final ComboBox diseaseGroupsListA = new ComboBox("Disease Groups A");
    private final ComboBox diseaseGroupsListB = new ComboBox("Disease Groups B");
    private final Set<String> diseaseGroupNames;
    private final TextArea highTextArea = new TextArea();
    private final TextArea lowTextArea = new TextArea();
    private final TextArea stableTextArea = new TextArea();

    public QuantCompareDataLayout(CSFPRHandler CSFPR_Handler) {
        this.setWidth("100%");
        this.setHeightUndefined();
        this.setStyleName(Reindeer.LAYOUT_WHITE);
        this.setSpacing(true);
        this.CSFPR_Handler = CSFPR_Handler;
        Map<String, QuantDatasetInitialInformationObject> quantDatasetInitialInformationObjectMap = CSFPR_Handler.getQuantDatasetInitialInformationObject();
        diseaseGroupNames = new TreeSet<String>();
        for (String diseaseCategory : quantDatasetInitialInformationObjectMap.keySet()) {
            QuantDatasetInitialInformationObject quantDatasetInitialInformationObject = quantDatasetInitialInformationObjectMap.get(diseaseCategory);
            for (QuantDatasetObject qdsObject : quantDatasetInitialInformationObject.getQuantDatasetsList().values()) {
                diseaseGroupNames.add(qdsObject.getPatientsSubGroup1());
                diseaseGroupNames.add(qdsObject.getPatientsSubGroup2());
            }

        }

        //select or enter new disease groups layout 
        selectDiseaseGroupsContainer = initSelectEnterDatasetDiseaseGroups();
        selectDiseaseGroupsContainer.setVisability(true);
        selectDiseaseGroupsContainer.setReadOnly(true);
        this.addComponent(selectDiseaseGroupsContainer);

        proteinsDataCaptureLayout = initProteinsDataCapture();
        proteinsDataCaptureLayout.setEnabled(false);
        this.addComponent(proteinsDataCaptureLayout);
        this.resetLists();

    }

    private HideOnClickLayout initSelectEnterDatasetDiseaseGroups() {
        VerticalLayout selectDiseaseGroupsMainLayout = new VerticalLayout();
        VerticalLayout miniSelectDiseaseGroupsLayout = new VerticalLayout();
        miniSelectDiseaseGroupsLayout.setStyleName("diseasegroupselectionresult");
        final Label miniselectionResultsLabel = new Label();
        miniSelectDiseaseGroupsLayout.addComponent(miniselectionResultsLabel);
        miniselectionResultsLabel.setValue("     (Group A / Group B)");

        HorizontalLayout selectionResultsContainer = new HorizontalLayout();
        selectionResultsContainer.setStyleName("diseasegroupselectionresult");
        selectDiseaseGroupsMainLayout.setSpacing(true);
        selectDiseaseGroupsMainLayout.setMargin(true);

        int width = 400;
        if (Page.getCurrent().getBrowserWindowWidth() < 800) {
            width = Page.getCurrent().getBrowserWindowWidth() / 2;
        }
        String containerWidth = ((width * 2) + 10) + "px";
        selectionResultsContainer.setWidth(containerWidth);
        selectDiseaseGroupsMainLayout.setWidth(containerWidth);

        final Label selectionResultsLabel = new Label();
        selectionResultsContainer.addComponent(selectionResultsLabel);
        selectionResultsLabel.setValue("Group A / Group B");

        Property.ValueChangeListener diseaseGroupsListListener = new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (diseaseGroupsListA.getValue() != null && diseaseGroupsListB.getValue() != null) {
                    selectionResultsLabel.setValue(diseaseGroupsListA.getValue().toString() + " / " + diseaseGroupsListB.getValue().toString());
                    miniselectionResultsLabel.setValue("     (" + diseaseGroupsListA.getValue().toString() + " / " + diseaseGroupsListB.getValue().toString() + ")");
                }
            }
        };

        HorizontalLayout diseaseGroupsListsContainer = new HorizontalLayout();
        selectDiseaseGroupsMainLayout.addComponent(diseaseGroupsListsContainer);
        diseaseGroupsListsContainer.setWidth(containerWidth);

//        diseaseGroupsListA.setRows(20);
        diseaseGroupsListA.setStyleName("diseasegrouplist");
        diseaseGroupsListA.setNullSelectionAllowed(false);

        diseaseGroupsListA.setImmediate(true);
//        diseaseGroupsListA.setMultiSelect(false);
        diseaseGroupsListA.setNewItemsAllowed(true);
        diseaseGroupsListA.setWidth(width, Unit.PIXELS);

//        diseaseGroupsListB.setRows(20);
        diseaseGroupsListB.setStyleName("diseasegrouplist");
        diseaseGroupsListB.setNullSelectionAllowed(false);

        diseaseGroupsListB.setImmediate(true);
//        diseaseGroupsListB.setMultiSelect(false);
        diseaseGroupsListB.setNewItemsAllowed(true);
        diseaseGroupsListB.setWidth(width, Unit.PIXELS);

        selectDiseaseGroupsMainLayout.addComponent(selectionResultsContainer);

        diseaseGroupsListsContainer.addComponent(diseaseGroupsListA);
        diseaseGroupsListsContainer.addComponent(diseaseGroupsListB);
        diseaseGroupsListsContainer.setComponentAlignment(diseaseGroupsListB, Alignment.TOP_RIGHT);

        diseaseGroupsListA.addValueChangeListener(diseaseGroupsListListener);
        diseaseGroupsListB.addValueChangeListener(diseaseGroupsListListener);

        Button nextBtn = new Button("Next >>");
        nextBtn.setStyleName(Reindeer.BUTTON_SMALL);
        selectionResultsContainer.addComponent(nextBtn);
        nextBtn.setId("diseaseGroupSelectionBtn");
        selectionResultsContainer.setComponentAlignment(nextBtn, Alignment.MIDDLE_RIGHT);
        nextBtn.addClickListener(this);

        diseaseGroupsListA.setNewItemHandler(new AbstractSelect.NewItemHandler() {

            @Override
            public void addNewItem(String newItemCaption) {
                diseaseGroupsListA.addItem(newItemCaption);
                diseaseGroupsListA.select(newItemCaption);
            }
        });

        diseaseGroupsListB.setNewItemHandler(new AbstractSelect.NewItemHandler() {

            @Override
            public void addNewItem(String newItemCaption) {
                diseaseGroupsListB.addItem(newItemCaption);
                 diseaseGroupsListB.select(newItemCaption);
            }
        });

        return new HideOnClickLayout("Select/Enter Dataset Disease Groups", selectDiseaseGroupsMainLayout, miniSelectDiseaseGroupsLayout, Alignment.TOP_LEFT, null, null);

    }

    private HideOnClickLayout initProteinsDataCapture() {
        VerticalLayout selectDiseaseGroupsMainLayout = new VerticalLayout();
        selectDiseaseGroupsMainLayout.setWidth("600px");
        GridLayout insertProteinsLayout = new GridLayout(3, 3);
        selectDiseaseGroupsMainLayout.addComponent(insertProteinsLayout);
        insertProteinsLayout.setSpacing(true);
        insertProteinsLayout.setMargin(new MarginInfo(false, false, false, true));
        insertProteinsLayout.setWidth(600 + "px");

        Label highLabel = new Label("<font color='#cc0000'>High</font>");
        highLabel.setWidth("40px");
        highLabel.setContentMode(ContentMode.HTML);
        Label stableLabel = new Label("<font color='#018df4'>Stable</font>");
        stableLabel.setWidth("50px");
        stableLabel.setContentMode(ContentMode.HTML);
        Label lowLabel = new Label("<font color='#009900'>Low</font>");
        lowLabel.setWidth("40px");
        lowLabel.setContentMode(ContentMode.HTML);
        insertProteinsLayout.addComponent(highLabel, 0, 0);
        insertProteinsLayout.setComponentAlignment(highLabel, Alignment.MIDDLE_CENTER);
        insertProteinsLayout.addComponent(stableLabel, 1, 0);
        insertProteinsLayout.setComponentAlignment(stableLabel, Alignment.MIDDLE_CENTER);
        insertProteinsLayout.addComponent(lowLabel, 2, 0);
        insertProteinsLayout.setComponentAlignment(lowLabel, Alignment.MIDDLE_CENTER);

        highTextArea.setWidth("100%");
        highTextArea.setHeight("200px");
        insertProteinsLayout.addComponent(highTextArea, 0, 1);
        insertProteinsLayout.setComponentAlignment(highTextArea, Alignment.MIDDLE_CENTER);

        stableTextArea.setWidth("100%");
        stableTextArea.setHeight("200px");
        insertProteinsLayout.addComponent(stableTextArea, 1, 1);
        insertProteinsLayout.setComponentAlignment(stableTextArea, Alignment.MIDDLE_CENTER);

        lowTextArea.setWidth("100%");
        lowTextArea.setHeight("200px");
        insertProteinsLayout.addComponent(lowTextArea, 2, 1);
        insertProteinsLayout.setComponentAlignment(lowTextArea, Alignment.MIDDLE_CENTER);

        final Button clear1 = new Button("Clear");
        clear1.setStyleName(Reindeer.BUTTON_SMALL);
        insertProteinsLayout.addComponent(clear1, 0, 2);
        insertProteinsLayout.setComponentAlignment(clear1, Alignment.MIDDLE_LEFT);
        clear1.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                highTextArea.clear();
            }
        });

        final Button clear2 = new Button("Clear");
        clear2.setStyleName(Reindeer.BUTTON_SMALL);
        insertProteinsLayout.addComponent(clear2, 1, 2);
        insertProteinsLayout.setComponentAlignment(clear2, Alignment.MIDDLE_LEFT);
        clear2.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                stableTextArea.clear();
            }
        });

        final Button clear3 = new Button("Clear");
        clear3.setStyleName(Reindeer.BUTTON_SMALL);
        insertProteinsLayout.addComponent(clear3, 2, 2);
        insertProteinsLayout.setComponentAlignment(clear3, Alignment.MIDDLE_LEFT);
        clear3.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                lowTextArea.clear();
            }
        });
        Label errorLabel = new Label();
        selectDiseaseGroupsMainLayout.addComponent(errorLabel);
        HorizontalLayout btnsLayout = new HorizontalLayout();
        btnsLayout.setSpacing(true);
        btnsLayout.setMargin(new MarginInfo(true, false, false, false));

        selectDiseaseGroupsMainLayout.addComponent(btnsLayout);
        selectDiseaseGroupsMainLayout.setComponentAlignment(btnsLayout, Alignment.MIDDLE_RIGHT);

        Button resetBtn = new Button("Reset");
        resetBtn.setStyleName(Reindeer.BUTTON_SMALL);
        btnsLayout.addComponent(resetBtn);
        resetBtn.setId("resetBtn");
        resetBtn.addClickListener(this);

        Button compareBtn = new Button("Compare");
        compareBtn.setStyleName(Reindeer.BUTTON_SMALL);
        btnsLayout.addComponent(compareBtn);
        compareBtn.setId("compareBtn");
        compareBtn.addClickListener(this);

        return new HideOnClickLayout("Insert Uniprot Proteins Accessions", selectDiseaseGroupsMainLayout, null, Alignment.TOP_LEFT, null, null);
    }

    private void resetLists() {
        diseaseGroupsListA.removeAllItems();
        diseaseGroupsListB.removeAllItems();
        diseaseGroupsListA.addItem("Group A");
        diseaseGroupsListB.addItem("Group B");
        for (String str : diseaseGroupNames) {
            diseaseGroupsListB.addItem(str);
            diseaseGroupsListA.addItem(str);
        }
        diseaseGroupsListA.select("Group A");
        diseaseGroupsListB.select("Group B");

    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton().getId().equalsIgnoreCase("diseaseGroupSelectionBtn")) {
            selectDiseaseGroupsContainer.setVisability(false);
            proteinsDataCaptureLayout.setEnabled(true);
            proteinsDataCaptureLayout.setVisability(true);

        } else if (event.getButton().getId().equalsIgnoreCase("resetBtn")) {
            selectDiseaseGroupsContainer.setVisability(true);
            proteinsDataCaptureLayout.setEnabled(false);
            proteinsDataCaptureLayout.setVisability(false);
            highTextArea.clear();
            lowTextArea.clear();
            stableTextArea.clear();
            this.resetLists();
            UI.getCurrent().setScrollTop(0);

        } else if (event.getButton().getId().equalsIgnoreCase("compareBtn")) {
            startComparingProcess();
        }

    }

    private void startComparingProcess() {
        String highAccessions = highTextArea.getValue();
        String[] highAccssionArr = highAccessions.replaceAll("\n", ",").replace("\t", ",").replace(";", ",").replace(" ", "").split(",");
        Set<String> highSet = new LinkedHashSet<String>();
        highSet.addAll(Arrays.asList(highAccssionArr));

        String stableAccessions = stableTextArea.getValue();
        String[] stableAccssionArr = stableAccessions.replaceAll("\n", ",").replace("\t", ",").replace(";", ",").replace(" ", "").split(",");
        Set<String> stableSet = new LinkedHashSet<String>();
        stableSet.addAll(Arrays.asList(stableAccssionArr));

        String lowAccessions = lowTextArea.getValue();
        String[] lowAccssionArr = lowAccessions.replaceAll("\n", ",").replace("\t", ",").replace(";", ",").replace(" ", "").split(",");
        Set<String> lowSet = new LinkedHashSet<String>();
        lowSet.addAll(Arrays.asList(lowAccssionArr));

        Set<String> totalSet = new LinkedHashSet<String>();
        totalSet.addAll(lowSet);
        totalSet.addAll(highSet);
        totalSet.addAll(stableSet);

        if (totalSet.size() != (lowSet.size() + stableSet.size() + highSet.size())) {
            System.out.println("at -************* doublication happend");

        }

    }

}
