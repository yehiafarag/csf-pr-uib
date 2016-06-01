/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.event.FieldEvents;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 * @author Yehia Farag
 *
 * this class represents searching field layout
 */
public abstract class SearchingField extends HorizontalLayout {
    private final Label searchingCommentLabel;
    public SearchingField() {
        this.setSpacing(true);
        this.setHeightUndefined();
        
        HorizontalLayout searchFieldContainerLayout = new HorizontalLayout();
        searchFieldContainerLayout.setWidthUndefined();
        searchFieldContainerLayout.setHeight(100,Unit.PERCENTAGE);
        searchFieldContainerLayout.setSpacing(true);
        TextField searchField = new TextField();
        searchField.setDescription("Search proteins by name or accession");
        searchField.setImmediate(true);
        searchField.setWidth(100, Unit.PERCENTAGE);
        searchField.setHeight(90, Unit.PERCENTAGE);
        searchField.setInputPrompt("Search...");
        searchFieldContainerLayout.addComponent(searchField);
        searchField.setTextChangeTimeout(1500);
        searchField.setStyleName(ValoTheme.TEXTFIELD_SMALL);
        searchField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        final Button b = new Button();
        searchField.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.LAZY);
        
        searchField.addShortcutListener(new Button.ClickShortcut(b, ShortcutListener.KeyCode.ENTER));
        
        VerticalLayout searchingBtn = new VerticalLayout();
        searchingBtn.setWidth(30,Unit.PIXELS);
        searchingBtn.setHeight(90, Unit.PERCENTAGE);
        searchingBtn.setStyleName("tablesearchingbtn");
        searchFieldContainerLayout.addComponent(searchingBtn);
        searchFieldContainerLayout.setComponentAlignment(searchingBtn, Alignment.TOP_CENTER);
        this.addComponent(searchFieldContainerLayout);
        this.setComponentAlignment(searchFieldContainerLayout, Alignment.TOP_CENTER);
        searchingCommentLabel = new Label(" ");
        searchingCommentLabel.setWidth(100,Unit.PERCENTAGE);
        searchingCommentLabel.setHeight(23, Unit.PIXELS);
        searchingCommentLabel.addStyleName(ValoTheme.LABEL_BOLD);
        searchingCommentLabel.addStyleName(ValoTheme.LABEL_SMALL);
        searchingCommentLabel.addStyleName(ValoTheme.LABEL_TINY);
        this.addComponent(searchingCommentLabel);
        this.setComponentAlignment(searchingCommentLabel, Alignment.TOP_CENTER);
        
        
        searchField.addTextChangeListener((FieldEvents.TextChangeEvent event) -> {
            SearchingField.this.textChanged(event.getText());
        });
         b.addClickListener((Button.ClickEvent event) -> {
              SearchingField.this.textChanged(searchField.getValue());
        });
        
        
        
    }
    
    public abstract  void textChanged(String text);
    
    public void updateLabel(String text){
        this.searchingCommentLabel.setValue(text);
    }
    
}
