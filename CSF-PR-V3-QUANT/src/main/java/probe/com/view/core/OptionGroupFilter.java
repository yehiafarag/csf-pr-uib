/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.OptionGroup;
import java.io.Serializable;
import probe.com.view.body.searching.SearchingFiltersControl;
//import probe.com.view.components.searchingFiltersControl;
//
/**
 *
 * @author Yehia Farag
 */
public class OptionGroupFilter extends HorizontalLayout implements Property.ValueChangeListener, Button.ClickListener,Serializable {

    private final SearchingFiltersControl searchingFiltersControl;
    private final ClosableFilterLabel filterBtn ;//= new HashMap<String, ClosableFilterLabel>();
    private final int filterId;
    private final String filterTitle;
    private String fieldValue;
    private final OptionGroup optionGroup;
    private final FilterConfirmLabel filterConfirmLabel;

    /**
     *
     * @return
     */
    public String getFieldValue() {
        return fieldValue;
    }

    /**
     *
     * @param value
     */
    public void setFieldValue(String value) {
        this.fieldValue = value;
    }

    /**
     *
     * @param control
     * @param filterTitle
     * @param filterIds
     * @param closable
     */
    public OptionGroupFilter(SearchingFiltersControl control,String filterTitle,int filterIds, boolean closable) {
        this.searchingFiltersControl = control;
        this.filterTitle = filterTitle;
        this.fieldValue = filterTitle;
        this.filterId = filterIds;
        this.setSpacing(true);  
        
        
        
        optionGroup = new OptionGroup();
        this.addComponent(optionGroup);
        optionGroup.setNullSelectionAllowed(false); // user can not 'unselect'
        optionGroup.setMultiSelect(false);
        optionGroup.addValueChangeListener(OptionGroupFilter.this);
         filterConfirmLabel = new FilterConfirmLabel();
        
       
        this.addComponent(filterConfirmLabel);
        
         filterBtn = new ClosableFilterLabel(filterTitle,"",filterId, closable);
         filterBtn.getCloseBtn().addClickListener(OptionGroupFilter.this);
         
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (optionGroup.isNullSelectionAllowed()) {
            optionGroup.select(null);
            optionGroup.unselect(event.getButton().getCaption());
        } else {
            event.getButton().setIcon(null);
        }
    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {

       filterConfirmLabel.setVisible(false);
        for (Object id : optionGroup.getItemIds().toArray()) {
            searchingFiltersControl.removeFilter(filterTitle+","+id.toString());
        }
      
        if (event.getProperty().getValue() instanceof String) {
            if (optionGroup.getItemIds().contains(event.getProperty().toString())) {
                handelFilter(event.getProperty().toString());
            }
        } else {
            for (Object id : optionGroup.getItemIds().toArray()) {
                if (optionGroup.isSelected(id.toString())) {
                    handelFilter(id.toString());
                    filterConfirmLabel.setVisible(true);

                }
            }

        }

    }

    private void handelFilter(String valueKey) {

        searchingFiltersControl.removeFilter(filterBtn.getCaption());
        if(valueKey!= null && !valueKey.trim().equalsIgnoreCase(""))
        {
            filterBtn.setValue(valueKey);
            searchingFiltersControl.addFilter(filterBtn);
        }
    }

    /**
     *
     * @return
     */
    public ClosableFilterLabel getFilterBtn() {
        return filterBtn;
    }

    /**
     *
     * @return
     */
    public OptionGroup getOptionGroup() {
        return optionGroup;
    }
    

    

}
