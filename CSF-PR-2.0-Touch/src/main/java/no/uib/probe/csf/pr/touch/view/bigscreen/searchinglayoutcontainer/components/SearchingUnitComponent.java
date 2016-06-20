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
    private final OptionGroup searchByOptionGroup;
    private final Query query;
    private final Label errorLabel;

    public Query getQuery() {
        return query;
    }

    public SearchingUnitComponent() {
        this.setWidth(100, Unit.PERCENTAGE);
        this.setHeightUndefined();
        this.setStyleName("whitelayout");
        this.setSpacing(true);

        searchingArea = new TextArea();
        searchingArea.setWidth(50, Unit.PERCENTAGE);
        searchingArea.setHeight(100, Unit.PIXELS);
        searchingArea.setStyleName(ValoTheme.TEXTAREA_ALIGN_CENTER);
        searchingArea.setInputPrompt("Use one key-word per line and choose the search by option");
        searchingArea.setRequired(false);
        searchingArea.setRequiredError("Check the used keywords");
        this.addComponent(searchingArea);
        this.setComponentAlignment(searchingArea, Alignment.TOP_CENTER);

        HorizontalLayout btnsLayoutContainer = new HorizontalLayout();
//        btnsLayoutContainer.setMargin(new MarginInfo(false, true, false, true));
        btnsLayoutContainer.setWidth(50, Unit.PERCENTAGE);
        btnsLayoutContainer.setHeight(100, Unit.PIXELS);
        this.addComponent(btnsLayoutContainer);
        this.setComponentAlignment(btnsLayoutContainer, Alignment.TOP_CENTER);

        searchByOptionGroup = new OptionGroup();
        searchByOptionGroup.addItem("Protein Accession");
        searchByOptionGroup.addItem("Protein Name");
        searchByOptionGroup.addItem("Peptide Sequence");
        searchByOptionGroup.setStyleName(ValoTheme.OPTIONGROUP_SMALL);
        btnsLayoutContainer.addComponent(searchByOptionGroup);
        searchByOptionGroup.setRequired(false);
        searchByOptionGroup.setRequiredError("You have to select an option");

        Button searchingBtn = new Button("Search");
        searchingBtn.setStyleName(ValoTheme.BUTTON_SMALL);
        searchingBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnsLayoutContainer.addComponent(searchingBtn);
        btnsLayoutContainer.setComponentAlignment(searchingBtn, Alignment.TOP_RIGHT);
        searchingBtn.addClickListener(SearchingUnitComponent.this);

        errorLabel = new Label("error");
        errorLabel.setStyleName(ValoTheme.LABEL_FAILURE);
        errorLabel.setVisible(false);
        this.addComponent(errorLabel);
        errorLabel.setWidth(50, Unit.PERCENTAGE);
        this.setComponentAlignment(errorLabel, Alignment.MIDDLE_CENTER);

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
            search(query);

        } else {
            errorLabel.setValue("Check keywords and searching by option");
            errorLabel.setVisible(true);
        }

    }
    
    
    
    
    public abstract void search(Query query);

}
