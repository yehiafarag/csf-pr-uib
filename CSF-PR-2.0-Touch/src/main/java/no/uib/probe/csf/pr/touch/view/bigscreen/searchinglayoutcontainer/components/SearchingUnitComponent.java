/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.bigscreen.searchinglayoutcontainer.components;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Set;
import no.uib.probe.csf.pr.touch.database.Query;

/**
 *
 * @author Yehia Farag
 *
 * this class represents the main searching unit responsible for data entry and
 * searching filters
 */
public abstract class SearchingUnitComponent extends VerticalLayout implements Button.ClickListener {

    private final TextArea searchingArea;
    private final OptionGroup searchByOptionGroup, diseaseCategoryOption;
    private final Query query;
    private final Label errorLabel;
    private final boolean smallScreen;

    public Query getQuery() {
        return query;
    }

    public void reset() {
        searchingArea.clear();
        searchByOptionGroup.setValue("Protein Name");
        errorLabel.setVisible(false);
//        searchingArea.setRequired(false);
        searchByOptionGroup.setRequired(false);
        resetSearching();
        removeStyleName("resizeto120");

    }

    public abstract void resetSearching();

    public SearchingUnitComponent(int height,int width, boolean smallScreen) {
        this.setWidth(width, Unit.PIXELS);
        this.setHeight(height, Unit.PIXELS);
        this.smallScreen = smallScreen;
         this.addStyleName("roundedborder");
        this.addStyleName("whitelayout");
        this.addStyleName("padding20");
        this.addStyleName("scrollable");
        this.addStyleName("margin");
        this.setSpacing(true);

        VerticalLayout frame = new VerticalLayout();
        frame.setWidth(100, Unit.PERCENTAGE);
        frame.setHeightUndefined();
//        frame.setSpacing(true);
//        this.setHeightUndefined();
        this.addComponent(frame);

        searchingArea = new TextArea();
        searchingArea.setWidth(90, Unit.PERCENTAGE);
        if (smallScreen) {
            searchingArea.setHeight(40, Unit.PIXELS);
        } else {
            searchingArea.setHeight(100, Unit.PIXELS);
        }
        searchingArea.setStyleName(ValoTheme.TEXTAREA_ALIGN_CENTER);
        searchingArea.setInputPrompt("Use one key-word per line and choose the search by option");
        searchingArea.addStyleName("maxwidth600");
        frame.addComponent(searchingArea);
        frame.setComponentAlignment(searchingArea, Alignment.TOP_CENTER);

        HorizontalLayout btnsLayoutContainer = new HorizontalLayout();
//        btnsLayoutContainer.setMargin(new MarginInfo(false, true, false, true));
        btnsLayoutContainer.setWidth(90, Unit.PERCENTAGE);
        btnsLayoutContainer.addStyleName("maxwidth600");
        btnsLayoutContainer.setHeight(70, Unit.PIXELS);
        frame.addComponent(btnsLayoutContainer);
        frame.setComponentAlignment(btnsLayoutContainer, Alignment.TOP_CENTER);

        searchByOptionGroup = new OptionGroup();
        searchByOptionGroup.addItem("Protein Accession");
        searchByOptionGroup.addItem("Protein Name");
        searchByOptionGroup.addItem("Peptide Sequence");
        searchByOptionGroup.select("Protein Name");
        searchByOptionGroup.setStyleName(ValoTheme.OPTIONGROUP_SMALL);
        btnsLayoutContainer.addComponent(searchByOptionGroup);
        searchByOptionGroup.setRequired(false);
        searchByOptionGroup.setRequiredError("You have to select an option");

        diseaseCategoryOption = new OptionGroup();
        diseaseCategoryOption.addItem("Alzheimer's");
        diseaseCategoryOption.addItem("Multiple Sclerosis");
        diseaseCategoryOption.addItem("Parkinson's");
        diseaseCategoryOption.setStyleName(ValoTheme.OPTIONGROUP_SMALL);
        diseaseCategoryOption.setNullSelectionAllowed(true);
        btnsLayoutContainer.addComponent(diseaseCategoryOption);
        diseaseCategoryOption.setRequired(false);
        diseaseCategoryOption.setMultiSelect(true);

        VerticalLayout rightBtnContainer = new VerticalLayout();
        rightBtnContainer.setSizeFull();
           btnsLayoutContainer.addComponent(rightBtnContainer);
        btnsLayoutContainer.setComponentAlignment(rightBtnContainer, Alignment.TOP_RIGHT);
        
        
        HorizontalLayout btnWrapper = new HorizontalLayout();
        btnWrapper.setSpacing(true);
        rightBtnContainer.addComponent(btnWrapper);
        rightBtnContainer.setComponentAlignment(btnWrapper, Alignment.TOP_RIGHT);

        
        
        Button sampleBtn = new Button("Load Example Data");
        sampleBtn.setStyleName(ValoTheme.BUTTON_LINK);
        sampleBtn.addStyleName(ValoTheme.BUTTON_TINY);
        sampleBtn.addStyleName("nomargin");
        sampleBtn.addStyleName("nopadding");
        sampleBtn.addStyleName("margintop");
        sampleBtn.setDescription("Load example data");
        btnWrapper.addComponent(sampleBtn);
        btnWrapper.setComponentAlignment(sampleBtn, Alignment.BOTTOM_LEFT);

        sampleBtn.addClickListener((Button.ClickEvent event) -> {
            SearchingUnitComponent.this.reset();
            diseaseCategoryOption.select("Alzheimer's");
            diseaseCategoryOption.select("Multiple Sclerosis");
            searchByOptionGroup.select("Protein Name");
            searchingArea.setValue("serum albumin\n"
                    + "hemoglobin\n"
                    + "albumin\n"
                    + "random keyword");

        });
        
         VerticalLayout spacer = new VerticalLayout();
        spacer.setWidth(2, Unit.PIXELS);
        spacer.setHeight(10, Unit.PIXELS);
        btnWrapper.addComponent(spacer);
        btnWrapper.setComponentAlignment(spacer, Alignment.TOP_CENTER);

        
        Button searchingBtn = new Button("Search");
        searchingBtn.setStyleName(ValoTheme.BUTTON_SMALL);
        searchingBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
        searchingBtn.addStyleName("margintop");
        btnWrapper.addComponent(searchingBtn);
        btnWrapper.setComponentAlignment(searchingBtn, Alignment.TOP_RIGHT);
        searchingBtn.addClickListener(SearchingUnitComponent.this);

        HorizontalLayout bottomLayout = new HorizontalLayout();
        frame.addComponent(bottomLayout);
        frame.setComponentAlignment(bottomLayout, Alignment.TOP_CENTER);
        bottomLayout.setWidth(90, Unit.PERCENTAGE);
        bottomLayout.addStyleName("maxwidth600");

        HorizontalLayout bottomLayoutWrapper = new HorizontalLayout();
        bottomLayoutWrapper.setSpacing(true);
        bottomLayout.addComponent(bottomLayoutWrapper);
        bottomLayout.setComponentAlignment(bottomLayoutWrapper, Alignment.TOP_LEFT);

        errorLabel = new Label("error");
        errorLabel.setStyleName(ValoTheme.LABEL_FAILURE);
        errorLabel.addStyleName("smallerrorlabel");
        errorLabel.setVisible(false);
        rightBtnContainer.addComponent(errorLabel);
        rightBtnContainer.setComponentAlignment(errorLabel, Alignment.MIDDLE_CENTER);

//        if (smallScreen) {
//            errorLabel.setHeight(40, Unit.PIXELS);
//        }
        query = new Query();

    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        errorLabel.setVisible(false);
//        searchingArea.setRequired(true);
//        searchingArea.commit();

        searchByOptionGroup.setRequired(true);
        searchByOptionGroup.commit();

        if (searchingArea.getValue() != null && !searchingArea.getValue().trim().equalsIgnoreCase("") && searchByOptionGroup.isValid()) {
//            searchingArea.setRequired(false);
            searchByOptionGroup.setRequired(false);
            String searchKeyWords = searchingArea.getValue();

            String updatedSearchingKey = searchKeyWords.replace("\n", ",");
            String ser = "";

            String[] keyWordArr = updatedSearchingKey.trim().split(",");
            for (String str : keyWordArr) {
                if (str.trim().length() == 0) {
                    continue;
                }
                if (str.trim().length() < 4) {
                    if (keyWordArr.length == 1) {
                        errorLabel.setValue("Not valid keywords");
                        errorLabel.setVisible(true);
                        return;
                    }
                    ser += " -" + str.replace(" -", "") + " -\n";
                    continue;
                }
                ser += str.trim() + "\n";
            }
            searchKeyWords = ser;
            query.setSearchKeyWords(searchKeyWords);
            query.setSearchBy(searchByOptionGroup.getValue().toString());
            query.setDiseaseCategorys((Set<Object>) diseaseCategoryOption.getValue());
            search(query);
            if (smallScreen) {
                this.addStyleName("resizeto120");
            }

        } else {
            this.removeStyleName("resizeto120");
            errorLabel.setValue("Check keywords and searching by option");
            errorLabel.addStyleName("nomargin");
            errorLabel.setVisible(true);
        }

    }

    public abstract void search(Query query);

    public OptionGroup getDiseaseCategoryOption() {
        return diseaseCategoryOption;
    }

}
