/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.searching;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;
import probe.com.view.core.CustomErrorLabel;

/**
 *
 * @author Yehia Farag this class represents the top layout in the searching tab
 * the class responsible for extracting searching keywords and validate it
 */
public class SearchingUnitLayout extends HorizontalLayout implements Serializable {

    private final SearchingAreaFilter searchingAreaFilter;
    private final SearchingFiltersControl filtersController;
    private String defaultText = "Please use one key-word per line and choose the search options";
    private Label errorLabelI;
    private CustomErrorLabel errorLabelII;

    public SearchingUnitLayout(Button.ClickListener searchingBtnClickListener) {
        this.setStyleName(Reindeer.LAYOUT_WHITE);
        this.setSpacing(true);
        this.setMargin(new MarginInfo(true, false, false, false));
        this.setWidth("100%");

        VerticalLayout leftPanel = new VerticalLayout();
        this.addComponent(leftPanel);
        VerticalLayout rightPanel = new VerticalLayout();
        this.addComponent(rightPanel);

        filtersController = new SearchingFiltersControl();

        //init left panel 
        searchingAreaFilter = new SearchingAreaFilter(filtersController, defaultText);
        leftPanel.addComponent(searchingAreaFilter);
        searchingAreaFilter.addSearchingClickListener(searchingBtnClickListener);

        //init right panel
        errorLabelI = new Label("<h4 Style='color:red;'>Please Enter Valid Key Word (at least 4 letters for each keyword)</h4>");
        errorLabelI.setContentMode(ContentMode.HTML);
        errorLabelI.setHeight("30px");
        rightPanel.addComponent(errorLabelI);
        rightPanel.setComponentAlignment(errorLabelI, Alignment.TOP_LEFT);
        errorLabelI.setVisible(false);
        errorLabelII = new CustomErrorLabel();
        rightPanel.addComponent(errorLabelII);
        rightPanel.setComponentAlignment(errorLabelII, Alignment.TOP_LEFT);
        errorLabelII.setVisible(false);

    }

    public boolean isValidQuery() {
        errorLabelI.setVisible(false);
        errorLabelII.setVisible(false);
        filtersController.setSearchKeyWords(searchingAreaFilter.getSearchingKeyWords());
        if (!filtersController.isValidQuery()) {
            errorLabelI.setVisible(true);
            searchingAreaFilter.SearchFieldFocus();
        } else {
            errorLabelI.setVisible(false);
        }
        return filtersController.isValidQuery();
    }

    public String getSearchingKeywords() {
        return filtersController.getSearchKeyWords();
    }

    public String getSearchingByValue() {
        return searchingAreaFilter.getSearchingByValue();
    }

    public void setSearchingFieldValue(String value) {
        searchingAreaFilter.setSearchingFieldValue(value);
    }

    public void updateErrorLabelIIValue(String value) {
        if (!value.equalsIgnoreCase("")) {
            errorLabelII.updateErrot(value);
            errorLabelII.setVisible(true);
        } else {
            errorLabelII.setVisible(false);
        }

    }
}
