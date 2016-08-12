/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.bigscreen.searchinglayoutcontainer.components;

import com.vaadin.data.Property;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.Data_Handler;
import no.uib.probe.csf.pr.touch.database.Query;
import no.uib.probe.csf.pr.touch.logic.beans.QuantComparisonProtein;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDatasetObject;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.view.core.TextAreaComponent;

/**
 *
 * @author Yehia Farag
 *
 * This Class represents compare user's quant dataset with csf-pr v2 quant data
 */
public abstract class ComparisonUnitComponent extends VerticalLayout implements Button.ClickListener {

    private final ComboBox diseaseGroupsListA, diseaseGroupsListB;
    private final Set<String> diseaseGroupNames;
    private final Label errorLabel;
    private final QuantDiseaseGroupsComparison userCustomizedComparison;
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

    public boolean isSorterRows() {
        return sorterRows;
    }

    public boolean isSortColumns() {
        return sortColumns;
    }
    private boolean sorterRows, sortColumns;
    private final TextAreaComponent textBoxI, textBoxII, textBoxIII;

    public ComparisonUnitComponent(Data_Handler CSFPR_Handler, int height, boolean smallScreen) {
        this.setWidth(100, Unit.PERCENTAGE);
        this.setHeight(height, Unit.PIXELS);
        this.setStyleName("whitelayout");
        this.addStyleName("roundedborder");
        this.addStyleName("padding20");

        VerticalLayout frame = new VerticalLayout();
        frame.setWidth(80, Unit.PERCENTAGE);
        frame.setSpacing(true);
        this.addComponent(frame);
        this.setComponentAlignment(frame, Alignment.TOP_CENTER);

        Label title1 = new Label("1. Select/Enter Dataset Disease Groups");
        title1.setStyleName(ValoTheme.LABEL_BOLD);
        if (!smallScreen) {
            frame.addComponent(title1);
        }

        HorizontalLayout subFrame = new HorizontalLayout();
        subFrame.setWidth(100, Unit.PERCENTAGE);
        frame.addComponent(subFrame);
        subFrame.addStyleName("margintop");

        this.diseaseGroupsListA = new ComboBox("<b>Disease Groups A</b>");
        subFrame.addComponent(diseaseGroupsListA);
        diseaseGroupsListA.setWidth(90, Unit.PERCENTAGE);
        diseaseGroupsListA.setStyleName(ValoTheme.COMBOBOX_SMALL);
        diseaseGroupsListA.setStyleName(ValoTheme.COMBOBOX_TINY);
        diseaseGroupsListA.setCaptionAsHtml(true);
        diseaseGroupsListA.setNullSelectionAllowed(true);
        diseaseGroupsListA.setImmediate(true);
        diseaseGroupsListA.setNewItemsAllowed(true);
        diseaseGroupsListA.setInputPrompt("Select or enter new disease group name");
        diseaseGroupsListA.setRequiredError("Select or enter new disease group name");
        diseaseGroupsListA.setPageLength(30);

        this.diseaseGroupsListB = new ComboBox("<b>Disease Groups B</b>");
        subFrame.addComponent(diseaseGroupsListB);
        subFrame.setComponentAlignment(diseaseGroupsListB, Alignment.TOP_RIGHT);
        diseaseGroupsListB.setWidth(90, Unit.PERCENTAGE);
        diseaseGroupsListB.setStyleName(ValoTheme.COMBOBOX_SMALL);
        diseaseGroupsListB.setStyleName(ValoTheme.COMBOBOX_TINY);
        diseaseGroupsListB.setCaptionAsHtml(true);
        diseaseGroupsListB.setImmediate(true);
        diseaseGroupsListB.setNewItemsAllowed(true);
        diseaseGroupsListB.setInputPrompt("Select or enter new disease group name");
        diseaseGroupsListB.setRequiredError("Select or enter new disease group name");
        diseaseGroupsListB.setNullSelectionAllowed(true);
        diseaseGroupsListB.setPageLength(30);

        Label selectionResultsLabel = new Label("Selection:");
        selectionResultsLabel.setStyleName(ValoTheme.LABEL_TINY);
        selectionResultsLabel.addStyleName(ValoTheme.LABEL_SMALL);
        frame.addComponent(selectionResultsLabel);

        diseaseGroupNames = CSFPR_Handler.getFullDiseaseGroupNameSet();
        diseaseGroupsListA.addItem("Group A");
        diseaseGroupsListB.addItem("Group B");
        for (String str : diseaseGroupNames) {
            diseaseGroupsListA.addItem(str);
            diseaseGroupsListB.addItem(str);
        }

        Property.ValueChangeListener diseaseGroupsListListener = (Property.ValueChangeEvent event) -> {
            String value = "Selection:   ";
            if (diseaseGroupsListA.getValue() != null) {
                diseaseGroupsListA.setRequired(false);
                value += diseaseGroupsListA.getValue().toString();
                sorterRows = diseaseGroupNames.contains(diseaseGroupsListA.getValue().toString().trim());
            }

            if (diseaseGroupsListB.getValue() != null) {
                diseaseGroupsListB.setRequired(false);
                value += " / " + diseaseGroupsListB.getValue().toString();
                sortColumns = diseaseGroupNames.contains(diseaseGroupsListB.getValue().toString().trim());
//                    reset();
            }
            selectionResultsLabel.setValue(value);
            if ((diseaseGroupsListA.getValue() != null) && (diseaseGroupsListB.getValue() != null)) {
                if (diseaseGroupsListA.getValue().toString().trim().equalsIgnoreCase(diseaseGroupsListB.getValue().toString().trim())) {
                    sorterRows = sortColumns = false;
                }
            }
        };

        diseaseGroupsListA.addValueChangeListener(diseaseGroupsListListener);
        diseaseGroupsListB.addValueChangeListener(diseaseGroupsListListener);
        diseaseGroupsListA.setNewItemHandler((String newItemCaption) -> {
            diseaseGroupsListA.addItem(newItemCaption);
            diseaseGroupsListA.select(newItemCaption);
        });

        diseaseGroupsListB.setNewItemHandler((String newItemCaption) -> {
            diseaseGroupsListB.addItem(newItemCaption);
            diseaseGroupsListB.select(newItemCaption);
        });

        Label title2 = new Label("2. Insert UniProt Proteins Accessions");
        title2.setStyleName(ValoTheme.LABEL_BOLD);
        title2.addStyleName("margintop");
        if (!smallScreen) {
            frame.addComponent(title2);
        }

        HorizontalLayout textAreaContainer = new HorizontalLayout();
        textAreaContainer.setWidth(100, Unit.PERCENTAGE);
        textAreaContainer.setSpacing(true);
        frame.addComponent(textAreaContainer);
        int h;
        if (smallScreen) {
            h = 70;
        } else {
            h = 150;
        }

        textBoxI = new TextAreaComponent("<font color='#cc0000'>&nbsp;Increased</font>", h);
        textAreaContainer.addComponent(textBoxI);

        textBoxII = new TextAreaComponent("<font color='#018df4'>&nbsp;Equal</font>", h);
        textAreaContainer.addComponent(textBoxII);

        textBoxIII = new TextAreaComponent("<font color='#009900'>&nbsp;Decreased</font>", h);
        textAreaContainer.addComponent(textBoxIII);

        HorizontalLayout bottomLayout = new HorizontalLayout();
        bottomLayout.setWidth(100, Unit.PERCENTAGE);
        frame.addComponent(bottomLayout);

        errorLabel = new Label() {

            @Override
            public void setValue(String newStringValue) {
                if (newStringValue == null || newStringValue.trim().equalsIgnoreCase("")) {
                    this.setVisible(false);
                } else {
                    this.setVisible(true);
                }
                super.setValue(newStringValue); //To change body of generated methods, choose Tools | Templates.
            }

        };
        errorLabel.setStyleName(ValoTheme.LABEL_FAILURE);
        errorLabel.setVisible(false);
        errorLabel.setWidth(100, Unit.PERCENTAGE);
        bottomLayout.addComponent(errorLabel);
        bottomLayout.setComponentAlignment(errorLabel, Alignment.MIDDLE_CENTER);

        HorizontalLayout btnsLayout = new HorizontalLayout();
        btnsLayout.setWidthUndefined();
        btnsLayout.setSpacing(true);
        bottomLayout.addComponent(btnsLayout);
        bottomLayout.setComponentAlignment(btnsLayout, Alignment.TOP_RIGHT);

        Button sampleBtn = new Button("Sample");

        sampleBtn.setStyleName(ValoTheme.BUTTON_SMALL);
        sampleBtn.addStyleName(ValoTheme.BUTTON_TINY);
        sampleBtn.addStyleName("margintop");
        btnsLayout.addComponent(sampleBtn);
        btnsLayout.setComponentAlignment(sampleBtn, Alignment.TOP_RIGHT);
        sampleBtn.addClickListener(ComparisonUnitComponent.this);

        Button compareBtn = new Button("Compare");
        compareBtn.setStyleName(ValoTheme.BUTTON_SMALL);
        compareBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
        compareBtn.addStyleName("margintop");
        btnsLayout.addComponent(compareBtn);
        btnsLayout.setComponentAlignment(compareBtn, Alignment.TOP_RIGHT);
        compareBtn.addClickListener(ComparisonUnitComponent.this);

        sampleBtn.addClickListener((Button.ClickEvent event) -> {
            ComparisonUnitComponent.this.reset();
            textBoxI.setText(highAcc);
            textBoxII.setText(stableAcc);
            textBoxIII.setText(lowAcc);
            diseaseGroupsListA.select("Group A");
            diseaseGroupsListB.select("Group B");
            compareBtn.focus();
        });

        userCustomizedComparison = new QuantDiseaseGroupsComparison();
    }

    public void reset() {
        errorLabel.setValue("");
        textBoxI.reset();
        textBoxII.reset();
        textBoxIII.reset();
        diseaseGroupsListA.setValue(null);
        diseaseGroupsListB.setValue(null);

    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        diseaseGroupsListA.setRequired(true);
        diseaseGroupsListA.commit();
        diseaseGroupsListB.setRequired(true);
        diseaseGroupsListB.commit();
        if (!diseaseGroupsListA.isValid() || !diseaseGroupsListB.isValid()) {
            errorLabel.setValue("Select or enter new disease group name first");
            return;
        }
        diseaseGroupsListA.setRequired(false);
        diseaseGroupsListB.setRequired(false);

        String highAccessions = textBoxI.getText();
        Set<String> highSet = new LinkedHashSet<>();
        if (highAccessions != null && !highAccessions.trim().equalsIgnoreCase("")) {
            String[] highAccssionArr = highAccessions.replace("\n", ",").replace("\t", ",").replace(";", ",").replace(" ", "").split(",");
            highSet.addAll(Arrays.asList(highAccssionArr));
        }

        String stableAccessions = textBoxII.getText();
        Set<String> stableSet = new LinkedHashSet<>();
        if (stableAccessions != null && !stableAccessions.trim().equalsIgnoreCase("")) {
            String[] stableAccssionArr = stableAccessions.replace("\n", ",").replace("\t", ",").replace(";", ",").replace(" ", "").split(",");
            stableSet.addAll(Arrays.asList(stableAccssionArr));
        }

        String lowAccessions = textBoxIII.getText();
        Set<String> lowSet = new LinkedHashSet<>();
        if (lowAccessions != null && !lowAccessions.trim().equalsIgnoreCase("")) {
            String[] lowAccssionArr = lowAccessions.replace("\n", ",").replace("\t", ",").replace(";", ",").replace(" ", "").split(",");
            lowSet.addAll(Arrays.asList(lowAccssionArr));
        }

        Set<String> totalSet = new LinkedHashSet<>();
        totalSet.addAll(lowSet);
        totalSet.addAll(highSet);
        totalSet.addAll(stableSet);
        if (totalSet.isEmpty()) {
            errorLabel.setValue("Error - You need to add one or more records to compare");
            return;
        }
        errorLabel.setValue("");
//init user comparison
        userCustomizedComparison.setDiseaseCategory("user");

        String userCompHeader = "User Data - " + this.diseaseGroupsListA.getValue().toString().trim() + " / " + this.diseaseGroupsListB.getValue().toString().trim();

        userCustomizedComparison.setComparisonFullName(userCompHeader);
        Map<Integer, QuantDatasetObject> dsMap = new HashMap<>();
        dsMap.put(-1, null);
        userCustomizedComparison.setDatasetMap(dsMap);
        userCustomizedComparison.setComparisonHeader(userCompHeader);

        Map<String, QuantComparisonProtein> comparProtList = new LinkedHashMap<>();
        int index = -1;

        Set<QuantComparisonProtein> highProtSet = new HashSet<>();
        Set<QuantComparisonProtein> stableProtSet = new HashSet<>();
        Set<QuantComparisonProtein> lowProthSet = new HashSet<>();

        for (String str : textBoxI.getText().split("\n")) {
            str = str.trim();
            if (str.trim().equals("")) {
                continue;
            }
            QuantComparisonProtein comProt = new QuantComparisonProtein(1, userCustomizedComparison, index--);
            comProt.addUp(-1, -1, true);
            comProt.setProteinAccession(str);
            comProt.finalizeQuantData();
            comparProtList.put(str, comProt);
            highProtSet.add(comProt);
        }
        for (String str : textBoxII.getText().split("\n")) {
            if (str.trim().equals("")) {
                continue;
            }
            QuantComparisonProtein comProt = new QuantComparisonProtein(1, userCustomizedComparison, index--);
            comProt.addStable(-1, -1);
            comProt.setProteinAccession(str);
            comProt.finalizeQuantData();
            comparProtList.put(str, comProt);
            stableProtSet.add(comProt);
        }
        for (String str : textBoxIII.getText().split("\n")) {
            if (str.trim().equals("")) {
                continue;
            }
            QuantComparisonProtein comProt = new QuantComparisonProtein(1, userCustomizedComparison, index--);
            comProt.addDown(-1, -1, true);
            comProt.setProteinAccession(str);
            comProt.finalizeQuantData();
            comparProtList.put(str, comProt);
            lowProthSet.add(comProt);
        }

        Map<Integer, Set<QuantComparisonProtein>> proteinsByTrendMap = new HashMap<>();
        proteinsByTrendMap.put(0, lowProthSet);
        proteinsByTrendMap.put(1, new HashSet<>());
        proteinsByTrendMap.put(2, stableProtSet);
        proteinsByTrendMap.put(3, new HashSet<>());
        proteinsByTrendMap.put(4, highProtSet);
        proteinsByTrendMap.put(5, new HashSet<>());
        userCustomizedComparison.setProteinsByTrendMap(proteinsByTrendMap);
        userCustomizedComparison.setQuantComparisonProteinMap(comparProtList);
        userCustomizedComparison.setOreginalComparisonHeader(userCompHeader);
        userCustomizedComparison.setDiseaseCategoryColor("#8210B0");
        userCustomizedComparison.setDiseaseCategoryStyle("user");
        userCustomizedComparison.setUseCustomRowHeaderToSort(sorterRows);
        userCustomizedComparison.setUseCustomColumnHeaderToSort(sortColumns);

        //start query
        Query query = new Query();
        query.setSearchBy("Protein Accession");
        query.setSearchDataType("Quantification Data");
        query.setSearchKeyWords(totalSet.toString().replace("[", "").replace("]", "").replace(" ", "").replace(",", "\n"));

        startComparing(query);
    }

    public QuantDiseaseGroupsComparison getUserCustomizedComparison() {
        return userCustomizedComparison;
    }

    public abstract void startComparing(Query query);

}
