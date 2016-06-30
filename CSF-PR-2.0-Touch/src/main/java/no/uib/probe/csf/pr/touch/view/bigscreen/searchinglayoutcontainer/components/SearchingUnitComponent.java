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

    public Query getQuery() {
        return query;
    }

    public void reset() {
        searchingArea.clear();
        searchByOptionGroup.setValue("Protein Name");
        errorLabel.setVisible(false);
        searchingArea.setRequired(false);
        searchByOptionGroup.setRequired(false);
        resetSearching();

    }
    public abstract void resetSearching();

    public SearchingUnitComponent() {
        this.setWidth(100, Unit.PERCENTAGE);
        this.setHeight(290, Unit.PIXELS);
        this.setStyleName("whitelayout");
        this.addStyleName("roundedborder");
        this.addStyleName("padding20");

        this.setSpacing(true);

        VerticalLayout frame = new VerticalLayout();
        frame.setWidth(100, Unit.PERCENTAGE);
        frame.setHeightUndefined();
        frame.setSpacing(true);
//        this.setHeightUndefined();
        this.addComponent(frame);
        this.setComponentAlignment(frame, Alignment.TOP_CENTER);

        searchingArea = new TextArea();
        searchingArea.setWidth(50, Unit.PERCENTAGE);
        searchingArea.setHeight(100, Unit.PIXELS);
        searchingArea.setStyleName(ValoTheme.TEXTAREA_ALIGN_CENTER);
        searchingArea.setInputPrompt("Use one key-word per line and choose the search by option");
        searchingArea.setRequired(false);
        searchingArea.setRequiredError("Check the used keywords");
        frame.addComponent(searchingArea);
        frame.setComponentAlignment(searchingArea, Alignment.TOP_CENTER);

        HorizontalLayout btnsLayoutContainer = new HorizontalLayout();
//        btnsLayoutContainer.setMargin(new MarginInfo(false, true, false, true));
        btnsLayoutContainer.setWidth(50, Unit.PERCENTAGE);
        btnsLayoutContainer.setHeight(100, Unit.PIXELS);
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
        
        
        HorizontalLayout btnWrapper = new HorizontalLayout();
        btnWrapper.setSpacing(true);
          btnsLayoutContainer.addComponent(btnWrapper);
        btnsLayoutContainer.setComponentAlignment(btnWrapper, Alignment.TOP_RIGHT);
        
         Button clearBtn = new Button("Clear");
        clearBtn.setStyleName(ValoTheme.BUTTON_SMALL);
        clearBtn.setDescription("Reset searching");
        clearBtn.addStyleName("margintop");
        btnWrapper.addComponent(clearBtn);
        btnWrapper.setComponentAlignment(clearBtn, Alignment.TOP_RIGHT);
        clearBtn.addClickListener((Button.ClickEvent event) -> {
            SearchingUnitComponent.this.reset();
        });

        Button searchingBtn = new Button("Search");
        searchingBtn.setStyleName(ValoTheme.BUTTON_SMALL);
        searchingBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
        searchingBtn.addStyleName("margintop");
        btnWrapper.addComponent(searchingBtn);
        btnWrapper.setComponentAlignment(searchingBtn, Alignment.TOP_RIGHT);
        searchingBtn.addClickListener(SearchingUnitComponent.this);

        errorLabel = new Label("error");
        errorLabel.setStyleName(ValoTheme.LABEL_FAILURE);
        errorLabel.setVisible(false);
        frame.addComponent(errorLabel);
        frame.setComponentAlignment(errorLabel, Alignment.MIDDLE_CENTER);
        errorLabel.setWidth(50, Unit.PERCENTAGE);

        query = new Query();

    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        errorLabel.setVisible(false);
        searchingArea.setRequired(true);
        searchingArea.commit();

        searchByOptionGroup.setRequired(true);
        searchByOptionGroup.commit();

        if (searchingArea.isValid() && searchByOptionGroup.isValid()) {
            searchingArea.setRequired(false);
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
            query.setDiseaseCategorys((Set<Object>)diseaseCategoryOption.getValue());
            search(query);

        } else {
            errorLabel.setValue("Check keywords and searching by option");
            errorLabel.setVisible(true);
        }

    }

    public abstract void search(Query query);

    public OptionGroup getDiseaseCategoryOption() {
        return diseaseCategoryOption;
    }

}