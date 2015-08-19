package probe.com.view.body.searching;

import com.vaadin.event.FieldEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;
import probe.com.view.core.OptionGroupFilter;
import probe.com.view.core.TextAreaFilter;

/**
 *
 * @author Yehia Farag
 */
public class SearchingAreaFilter extends VerticalLayout implements Serializable {

    private final TextAreaFilter searchFieldFilter;
    private final OptionGroupFilter searchbyGroup;
    private final SearchingFiltersControl searchingFiltersControl;
    private final Button searchingBtn = new Button("");

    public SearchingAreaFilter(SearchingFiltersControl filtersController, String defaultText) {
        this.setHeight("100%");
        this.setSpacing(true);
        this.searchingFiltersControl = filtersController;
        searchFieldFilter = new TextAreaFilter(this.searchingFiltersControl, 1, "Search Keywords", defaultText);
        this.addComponent(searchFieldFilter);

        searchFieldFilter.addFocusListener(new FieldEvents.FocusListener() {
            @Override
            public void focus(FieldEvents.FocusEvent event) {
                searchbyGroup.getOptionGroup().setEnabled(true);
                if (searchbyGroup.getOptionGroup().getValue() == null && searchbyGroup.isEnabled()) {
                    searchbyGroup.getOptionGroup().select("Protein Accession");
                }
            }
        });
        searchFieldFilter.addBlurListener(new FieldEvents.BlurListener() {

            @Override
            public void blur(FieldEvents.BlurEvent event) {
                if (searchFieldFilter.getValue().trim().equals("")) {
                    searchbyGroup.getOptionGroup().setEnabled(false);
                    searchbyGroup.getOptionGroup().select(null);
                } else {
                    searchbyGroup.getOptionGroup().setEnabled(true);
                    if (searchbyGroup.getOptionGroup().getValue() == null) {
                        searchbyGroup.getOptionGroup().select("Protein Accession");
                    }
                }
            }
        });

        searchFieldFilter.getFilterBtn().getCloseBtn().addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                searchFieldFilter.setValue("");
                searchbyGroup.getOptionGroup().select(null);
                searchbyGroup.getOptionGroup().setEnabled(false);

            }

        });

        //init search by layout
        HorizontalLayout searchbyLayout = new HorizontalLayout();
        this.addComponent(searchbyLayout);

        searchbyGroup = new OptionGroupFilter(filtersController, "Search By:", 4, true);
        searchbyLayout.addComponent(searchbyGroup);
        searchbyGroup.setWidth("330px");
        searchbyGroup.setDescription("Please Select Search Method");
        searchbyGroup.getOptionGroup().setEnabled(false);
        searchbyGroup.getOptionGroup().setNullSelectionAllowed(true);

        // Use the single selection mode.
        searchbyGroup.getOptionGroup().addItem("Protein Accession");
        searchbyGroup.getOptionGroup().addItem("Protein Name");
        searchbyGroup.getOptionGroup().addItem("Peptide Sequence");

        searchbyGroup.getFilterBtn().getCloseBtn().addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                searchFieldFilter.setValue("");
                searchbyGroup.getOptionGroup().select(null);
                searchbyGroup.getOptionGroup().setEnabled(false);

            }
        });

        searchingBtn.setStyleName(Reindeer.BUTTON_LINK);
        searchingBtn.setIcon(new ThemeResource("img/searchBtn.png"));
        searchbyLayout.addComponent(searchingBtn);
        searchbyLayout.setComponentAlignment(searchingBtn, Alignment.MIDDLE_CENTER);
    }

    public void SearchFieldFocus() {
        searchFieldFilter.focus();
    }

    public void addSearchingClickListener(Button.ClickListener searchingBtnClickListener) {
        searchingBtn.addClickListener(searchingBtnClickListener);
    }
    public String getSearchingKeyWords(){
        return searchFieldFilter.getValue();
    
    }
    public String getSearchingByValue(){
        return (String)searchbyGroup.getOptionGroup().getValue();
    }
    
    public void setSearchingFieldValue(String value){
    
    searchFieldFilter.setValue(value);
    }

}
