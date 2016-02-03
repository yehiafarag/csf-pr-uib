/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body;

import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
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
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import probe.com.dal.Query;
import probe.com.handlers.CSFPRHandler;
import probe.com.model.beans.quant.QuantDatasetInitialInformationObject;
import probe.com.model.beans.quant.QuantDatasetObject;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
import probe.com.model.beans.quant.QuantProtein;
import probe.com.view.body.quantcompare.QuantCompareDataViewLayout;
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
    private final VerticalLayout selectDiseaseGroupsContainer, proteinsDataCaptureLayout;
    private final ComboBox diseaseGroupsListA = new ComboBox("Disease Groups A");
    private final ComboBox diseaseGroupsListB = new ComboBox("Disease Groups B");
    private final Set<String> diseaseGroupNames;
    private final TextArea highTextArea = new TextArea();
    private final TextArea lowTextArea = new TextArea();
    private final TextArea stableTextArea = new TextArea();
    private Button compareBtn;
//    private Button nextBtn;
    private final VerticalLayout errorLayout = new VerticalLayout();
    private final Button notFoundErrorBtn;
    private final TextArea errorTextArea = new TextArea();
    private final VerticalLayout resultsLayout = new VerticalLayout();
    private final Label miniselectionResultsLabel;
    private final HideOnClickLayout userDataLayoutContainer;
    private final String highAcc = "P00450\n"
            + "P02774\n"
            + "P02647\n"
            + "P36222\n"
            + "P00747\n"
            + "P04004\n"
            + "P06727\n"
            + "P01876\n"
            + "P01834\n"
            + "P01871\n"
            + "P01842\n"
            + "P01011\n"
            + "P27169\n"
            + "P08185\n"
            + "P00738\n"
            + "P05546\n"
            + "P08697";

    private final String stableAcc = "";
    private final String lowAcc = "O75326\n"
            + "Q96KN2\n"
            + "Q96GW7\n"
            + "P13521\n"
            + "P07602\n"
            + "P04216";

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
                diseaseGroupNames.add(qdsObject.getPatientsSubGroup1().split("\n")[0].trim());
                diseaseGroupNames.add(qdsObject.getPatientsSubGroup2().split("\n")[0].trim());
            }

        }

        int width = 400;
        if (Page.getCurrent().getBrowserWindowWidth() < 800) {
            width = Page.getCurrent().getBrowserWindowWidth() / 2;
        }

        errorLayout.setStyleName("compareerror");
        errorLayout.setWidth("200px");
        errorLayout.setHeight("20px");
        errorLayout.setSpacing(true);
        errorLayout.setMargin(new MarginInfo(false, false, false, true));

        notFoundErrorBtn = new Button();
        notFoundErrorBtn.setCaptionAsHtml(true);
        notFoundErrorBtn.setStyleName(Reindeer.BUTTON_LINK);
        errorLayout.addComponent(notFoundErrorBtn);

        errorLayout.addComponent(errorTextArea);
        errorTextArea.setWidth("302px");
        errorTextArea.setHeight("200px");
        errorTextArea.setReadOnly(true);

        notFoundErrorBtn.addClickListener(QuantCompareDataLayout.this);
        notFoundErrorBtn.setId("notfounderrorbtn");

        VerticalLayout userDataLayout = new VerticalLayout();
        userDataLayout.setSpacing(true);

        VerticalLayout miniSelectDiseaseGroupsLayout = new VerticalLayout();
        miniSelectDiseaseGroupsLayout.setStyleName("diseasegroupselectionresult");
        miniselectionResultsLabel = new Label();
        miniSelectDiseaseGroupsLayout.addComponent(miniselectionResultsLabel);

        userDataLayoutContainer = new HideOnClickLayout("User Data", userDataLayout, miniSelectDiseaseGroupsLayout, "info data", null);
        userDataLayoutContainer.setMargin(new MarginInfo(false, false, false, true));
        userDataLayoutContainer.setVisability(true);
        this.addComponent(userDataLayoutContainer);

        HorizontalLayout topUserDataLayout = new HorizontalLayout();
        userDataLayout.addComponent(topUserDataLayout);
        //select or enter new disease groups layout 
        selectDiseaseGroupsContainer = initSelectEnterDatasetDiseaseGroups(width);
//        selectDiseaseGroupsContainer.setVisability(true);
        selectDiseaseGroupsContainer.setReadOnly(true);
        topUserDataLayout.addComponent(selectDiseaseGroupsContainer);
        topUserDataLayout.addComponent(errorLayout);

        proteinsDataCaptureLayout = initProteinsDataCapture(width);
        userDataLayout.addComponent(proteinsDataCaptureLayout);

        this.addComponent(resultsLayout);

    }

    private boolean useRowSorter, useColumnSorter;

    private VerticalLayout initSelectEnterDatasetDiseaseGroups(int width) {
        VerticalLayout selectDiseaseGroupsMainLayout = new VerticalLayout();
        selectDiseaseGroupsMainLayout.setMargin(new MarginInfo(true, false, false, true));
        selectDiseaseGroupsMainLayout.setSpacing(true);

        Label titleLabel = new Label("1. Select/Enter Dataset Disease Groups");
        titleLabel.setContentMode(ContentMode.HTML);
        titleLabel.setStyleName("normalheader");
        titleLabel.setHeight("20px");
        selectDiseaseGroupsMainLayout.addComponent(titleLabel);
        selectDiseaseGroupsMainLayout.setComponentAlignment(titleLabel, Alignment.TOP_LEFT);

        HorizontalLayout selectionResultsContainer = new HorizontalLayout();
//        selectionResultsContainer.setStyleName("diseasegroupselectionresult");

        String containerWidth = ((width * 2) + 10) + "px";
        selectionResultsContainer.setWidth(containerWidth);
        selectDiseaseGroupsMainLayout.setWidth(containerWidth);

        final Label selectionResultsLabel = new Label();
        selectionResultsContainer.addComponent(selectionResultsLabel);
        selectionResultsLabel.setStyleName(Reindeer.LABEL_SMALL);
        selectionResultsLabel.setValue("Selection:   Group A / Group B");
        miniselectionResultsLabel.setValue("( Group A  / Group B )");

        Property.ValueChangeListener diseaseGroupsListListener = new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (diseaseGroupsListA.getValue() != null && diseaseGroupsListB.getValue() != null) {
                    selectionResultsLabel.setValue("Selection:   " + diseaseGroupsListA.getValue().toString() + " / " + diseaseGroupsListB.getValue().toString());
                    miniselectionResultsLabel.setValue("     (" + diseaseGroupsListA.getValue().toString() + " / " + diseaseGroupsListB.getValue().toString() + ")");
                    useRowSorter = diseaseGroupNames.contains(diseaseGroupsListA.getValue().toString().trim());
                    useColumnSorter = diseaseGroupNames.contains(diseaseGroupsListB.getValue().toString().trim());
                    if (diseaseGroupsListA.getValue().toString().trim().equalsIgnoreCase(diseaseGroupsListB.getValue().toString().trim())) {
                        useRowSorter = useColumnSorter = false;
                    }
//                    reset();

                }
            }
        };

        HorizontalLayout diseaseGroupsListsContainer = new HorizontalLayout();
        selectDiseaseGroupsMainLayout.addComponent(diseaseGroupsListsContainer);
        diseaseGroupsListsContainer.setWidth(containerWidth);

        diseaseGroupsListA.setStyleName("diseasegrouplist");
        diseaseGroupsListA.setNullSelectionAllowed(false);
        diseaseGroupsListA.setImmediate(true);
        diseaseGroupsListA.setNewItemsAllowed(true);
        diseaseGroupsListA.setWidth(width, Unit.PIXELS);

        diseaseGroupsListB.setStyleName("diseasegrouplist");
        diseaseGroupsListB.setNullSelectionAllowed(false);

        diseaseGroupsListB.setImmediate(true);
        diseaseGroupsListB.setNewItemsAllowed(true);
        diseaseGroupsListB.setWidth(width, Unit.PIXELS);

        selectDiseaseGroupsMainLayout.addComponent(selectionResultsContainer);

        diseaseGroupsListsContainer.addComponent(diseaseGroupsListA);
        diseaseGroupsListsContainer.addComponent(diseaseGroupsListB);
        diseaseGroupsListsContainer.setComponentAlignment(diseaseGroupsListB, Alignment.TOP_RIGHT);

        this.resetLists();

        diseaseGroupsListA.addValueChangeListener(diseaseGroupsListListener);
        diseaseGroupsListB.addValueChangeListener(diseaseGroupsListListener);

//        nextBtn = new Button("Next >>");
//        nextBtn.setStyleName(Reindeer.BUTTON_SMALL);
//        selectionResultsContainer.addComponent(nextBtn);
//        nextBtn.setId("diseaseGroupSelectionBtn");
//        selectionResultsContainer.setComponentAlignment(nextBtn, Alignment.MIDDLE_RIGHT);
//        nextBtn.addClickListener(this);
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

        return selectDiseaseGroupsMainLayout;//new HideOnClickLayout("Select/Enter Dataset Disease Groups", proteinsDataCapturingMainLayout, miniSelectDiseaseGroupsLayout, Alignment.TOP_LEFT, null, null);

    }

    private VerticalLayout initProteinsDataCapture(int width) {
        VerticalLayout proteinsDataCapturingMainLayout = new VerticalLayout();
        proteinsDataCapturingMainLayout.setSpacing(true);
        proteinsDataCapturingMainLayout.setMargin(true);
        String containerWidth = ((width * 2) + 10) + "px";
        proteinsDataCapturingMainLayout.setWidth(containerWidth);
        proteinsDataCapturingMainLayout.setSpacing(true);

        Label titleLabel = new Label("2. Insert UniProt Proteins Accessions");
        titleLabel.setContentMode(ContentMode.HTML);
        titleLabel.setStyleName("normalheader");
        titleLabel.setHeight("20px");
        proteinsDataCapturingMainLayout.addComponent(titleLabel);
        proteinsDataCapturingMainLayout.setComponentAlignment(titleLabel, Alignment.TOP_LEFT);

        GridLayout insertProteinsLayout = new GridLayout(3, 3);
        proteinsDataCapturingMainLayout.addComponent(insertProteinsLayout);
        insertProteinsLayout.setSpacing(true);
        insertProteinsLayout.setMargin(new MarginInfo(false, false, false, true));
        insertProteinsLayout.setWidth(containerWidth);

        HorizontalLayout hlo1 = new HorizontalLayout();
        hlo1.setWidth("100%");
        hlo1.setMargin(new MarginInfo(false, true, false, true));
        Label highLabel = new Label("<font color='#cc0000'>High</font>");
        highLabel.setWidth("40px");
        highLabel.setContentMode(ContentMode.HTML);
        hlo1.addComponent(highLabel);

        HorizontalLayout hlo2 = new HorizontalLayout();
        hlo2.setWidth("100%");
        hlo2.setMargin(new MarginInfo(false, true, false, true));
        Label stableLabel = new Label("<font color='#018df4'>Stable</font>");
        stableLabel.setWidth("50px");
        stableLabel.setContentMode(ContentMode.HTML);
        hlo2.addComponent(stableLabel);

        HorizontalLayout hlo3 = new HorizontalLayout();
        hlo3.setWidth("100%");
        hlo3.setMargin(new MarginInfo(false, true, false, true));
        Label lowLabel = new Label("<font color='#009900'>Low</font>");
        lowLabel.setWidth("40px");
        lowLabel.setContentMode(ContentMode.HTML);
        hlo3.addComponent(lowLabel);
        insertProteinsLayout.addComponent(hlo1, 0, 0);
        insertProteinsLayout.setComponentAlignment(hlo1, Alignment.MIDDLE_CENTER);
        insertProteinsLayout.addComponent(hlo2, 1, 0);
        insertProteinsLayout.setComponentAlignment(hlo2, Alignment.MIDDLE_CENTER);
        insertProteinsLayout.addComponent(hlo3, 2, 0);
        insertProteinsLayout.setComponentAlignment(hlo3, Alignment.MIDDLE_CENTER);

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

        VerticalLayout clear1 = new VerticalLayout();
        hlo1.addComponent(clear1);
        clear1.setDescription("Clear field");
        clear1.setStyleName("clearfieldbtn");
        clear1.setWidth("25px");
        clear1.setHeight("25px");
//        insertProteinsLayout.addComponent(clear1, 0, 2);
        hlo1.setComponentAlignment(clear1, Alignment.MIDDLE_RIGHT);
        clear1.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                highTextArea.clear();
            }
        });

        VerticalLayout clear2 = new VerticalLayout();
        hlo2.addComponent(clear2);
        clear2.setDescription("Clear field");
        clear2.setStyleName("clearfieldbtn");
        clear2.setWidth("25px");
        clear2.setHeight("25px");

        clear2.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                stableTextArea.clear();
            }
        });
//        insertProteinsLayout.addComponent(clear2, 1, 2);
        hlo2.setComponentAlignment(clear2, Alignment.MIDDLE_RIGHT);

        VerticalLayout clear3 = new VerticalLayout();
        hlo3.addComponent(clear3);
        clear3.setDescription("Clear field");
        clear3.setStyleName("clearfieldbtn");
        clear3.setWidth("25px");
        clear3.setHeight("25px");

        clear3.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                lowTextArea.clear();
            }
        });
        hlo3.setComponentAlignment(clear3, Alignment.MIDDLE_RIGHT);
        Label errorLabel = new Label();
        proteinsDataCapturingMainLayout.addComponent(errorLabel);
        HorizontalLayout btnsLayout = new HorizontalLayout();
        btnsLayout.setSpacing(true);
        btnsLayout.setMargin(new MarginInfo(true, false, false, false));

        proteinsDataCapturingMainLayout.addComponent(btnsLayout);
        proteinsDataCapturingMainLayout.setComponentAlignment(btnsLayout, Alignment.MIDDLE_RIGHT);

        Button resetBtn = new Button("Reset");
        resetBtn.setStyleName(Reindeer.BUTTON_SMALL);
        btnsLayout.addComponent(resetBtn);
        resetBtn.setId("resetBtn");
        resetBtn.addClickListener(this);

        compareBtn = new Button("Compare");
        compareBtn.setStyleName(Reindeer.BUTTON_SMALL);
        btnsLayout.addComponent(compareBtn);
        compareBtn.setId("compareBtn");
        compareBtn.addClickListener(this);

        highTextArea.setValue(highAcc);
        lowTextArea.setValue(lowAcc);
        stableTextArea.setValue(stableAcc);

        return proteinsDataCapturingMainLayout;
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

//        diseaseGroupsListB.select("CIS-CIS");
//        diseaseGroupsListA.select("CIS-MS");
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton().getId().equalsIgnoreCase("resetBtn")) {
            this.reset();
            this.resetLists();
            UI.getCurrent().setScrollTop(0);
        } else if (event.getButton().getId().equalsIgnoreCase("compareBtn")) {
            startComparingProcess();
        } else if (event.getButton().getId().equalsIgnoreCase("notfounderrorbtn")) {
            System.out.println("download not found list");
        }

    }

    private void reset() {

        notFoundErrorBtn.setCaption("");
        notFoundErrorBtn.setVisible(false);
        resultsLayout.removeAllComponents();
        errorTextArea.setReadOnly(false);
        errorTextArea.clear();
        errorTextArea.setReadOnly(true);
        highTextArea.clear();
        lowTextArea.clear();
        stableTextArea.clear();

    }

    private void startComparingProcess() {
        resultsLayout.removeAllComponents();
        errorTextArea.setReadOnly(false);
        errorTextArea.clear();
        errorTextArea.setReadOnly(true);
        String highAccessions = highTextArea.getValue();
        Set<String> highSet = new LinkedHashSet<String>();
        if (highAccessions != null && !highAccessions.trim().equalsIgnoreCase("")) {
            String[] highAccssionArr = highAccessions.replace("\n", ",").replace("\t", ",").replace(";", ",").replace(" ", "").split(",");
            highSet.addAll(Arrays.asList(highAccssionArr));
        }

        String stableAccessions = stableTextArea.getValue();
        Set<String> stableSet = new LinkedHashSet<String>();
        if (stableAccessions != null && !stableAccessions.trim().equalsIgnoreCase("")) {
            String[] stableAccssionArr = stableAccessions.replace("\n", ",").replace("\t", ",").replace(";", ",").replace(" ", "").split(",");
            stableSet.addAll(Arrays.asList(stableAccssionArr));
        }

        String lowAccessions = lowTextArea.getValue();
        Set<String> lowSet = new LinkedHashSet<String>();
        if (lowAccessions != null && !lowAccessions.trim().equalsIgnoreCase("")) {
            String[] lowAccssionArr = lowAccessions.replace("\n", ",").replace("\t", ",").replace(";", ",").replace(" ", "").split(",");
            lowSet.addAll(Arrays.asList(lowAccssionArr));
        }

        Set<String> totalSet = new LinkedHashSet<String>();
        totalSet.addAll(lowSet);
        totalSet.addAll(highSet);
        totalSet.addAll(stableSet);
        if (totalSet.isEmpty()) {
            notFoundErrorBtn.setVisible(true);
            notFoundErrorBtn.setCaption("<font style='font-size:12px ; color:red;'>Error - You need to add one or more records to compare</font>");

            return;
        }

        Query query = new Query();
        query.setSearchBy("Protein Accession");
        query.setSearchDataType("Quantification Data");
        query.setSearchKeyWords(totalSet.toString().replace("[", "").replace("]", "").replace(" ", "").replace(",", "\n"));
        List<QuantProtein> searchQuantificationProtList = CSFPR_Handler.searchQuantificationProtein(query, true);

        String quantNotFound = CSFPR_Handler.filterQuantSearchingKeywords(searchQuantificationProtList, query.getSearchKeyWords(), query.getSearchBy());
        notFoundErrorBtn.setCaption("#Missing Records - " + quantNotFound.replace(" ", "").split(",").length);
        notFoundErrorBtn.setVisible(true);
        errorTextArea.setReadOnly(false);
        errorTextArea.setValue(quantNotFound.replace(",", "\n"));
        errorTextArea.setReadOnly(true);
        if (searchQuantificationProtList.isEmpty()) {
            return;
        }

        QuantDiseaseGroupsComparison userCustomizedComparison = CSFPR_Handler.initUserCustomizedComparison(diseaseGroupsListA.getValue().toString().trim(), diseaseGroupsListB.getValue().toString().trim(), highSet, stableSet, lowSet);
        userCustomizedComparison.setUseCustomRowHeaderToSort(useRowSorter);
        userCustomizedComparison.setUseCustomColumnHeaderToSort(useColumnSorter);
        QuantCompareDataViewLayout quantCompareDataViewLayout = new QuantCompareDataViewLayout(CSFPR_Handler, searchQuantificationProtList, userCustomizedComparison);
        resultsLayout.addComponent(quantCompareDataViewLayout);

        userDataLayoutContainer.setVisability(false);

    }

}
