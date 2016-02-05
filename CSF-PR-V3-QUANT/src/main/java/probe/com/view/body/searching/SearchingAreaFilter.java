package probe.com.view.body.searching;

import com.vaadin.event.FieldEvents;
import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
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
    private final VerticalLayout searchingBtn = new VerticalLayout();
    private final Button searchingWorkingBtn = new Button();

    /**
     *
     * @param filtersController
     * @param defaultText
     */
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

        searchingBtn.setStyleName("searchbtn");
        searchingBtn.setWidth("100px");
        searchingBtn.setHeight("32px");
//        searchingBtn.setIcon(new ThemeResource("img/searchBtn.png"));
        searchbyLayout.addComponent(searchingBtn);
        searchbyLayout.setComponentAlignment(searchingBtn, Alignment.MIDDLE_CENTER);
        searchbyLayout.addComponent(searchingWorkingBtn);
        searchbyLayout.setComponentAlignment(searchingWorkingBtn, Alignment.MIDDLE_CENTER);
        searchingWorkingBtn.setVisible(false);

        searchingBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                searchingWorkingBtn.click();
            }
        });

    }

    /**
     *
     */
    public void SearchFieldFocus() {
        searchFieldFilter.focus();
    }

    /**
     *
     * @param searchingBtnClickListener
     */
    public void addSearchingClickListener(Button.ClickListener searchingBtnClickListener) {
        searchingWorkingBtn.addClickListener(searchingBtnClickListener);
    }

    /**
     *
     * @return
     */
    public String getSearchingKeyWords() {
        return searchFieldFilter.getValue();

    }

    /**
     *
     * @return
     */
    public String getSearchingByValue() {
        return (String) searchbyGroup.getOptionGroup().getValue();
    }

    /**
     *
     * @param value
     */
    public void setSearchingFieldValue(String value) {

        searchFieldFilter.setValue(value);
    }

    /**
     *
     * @param value
     */
    public void setSearchingByValue(String value) {

        searchbyGroup.getOptionGroup().setValue(value);
    }

    public void startSearching() {
        searchingWorkingBtn.click();
    }

}
