/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters;

import com.vaadin.data.Property;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import probe.com.selectionmanager.QuantCentralManager;
import probe.com.view.core.DiseaseGroupLabel;

/**
 *
 * @author Yehia Farag
 */
public class PopupRecombineDiseaseGroups extends Button implements ClickListener {

    private final Window popupWindow;
    private final QuantCentralManager Quant_Central_Manager;
    private final VerticalLayout popupBodyLayout;
    private final Map<String, Map<String, String>> default_DiseaseCat_DiseaseGroupMap;
    private final NativeSelect diseaseTypeSelectionList;

    public PopupRecombineDiseaseGroups(QuantCentralManager Quant_Central_Manager) {
        super("Recombine Groups");
        diseaseStyleMap.put("Parkinson's", "pdLabel");
        diseaseStyleMap.put("Alzheimer's", "adLabel");
        diseaseStyleMap.put("Amyotrophic Lateral Sclerosis", "alsLabel");
        diseaseStyleMap.put("Multiple Sclerosis", "msLabel");

        default_DiseaseCat_DiseaseGroupMap = new LinkedHashMap<String, Map<String, String>>(Quant_Central_Manager.getDefault_DiseaseCat_DiseaseGroupMap());

        this.setStyleName(Reindeer.BUTTON_LINK);
        this.setDescription("Recombine Disease Groups");
        this.Quant_Central_Manager = Quant_Central_Manager;
        this.addClickListener(PopupRecombineDiseaseGroups.this);
        this.popupBodyLayout = new VerticalLayout();
        VerticalLayout windowLayout = new VerticalLayout();
        popupWindow = new Window() {
            @Override
            public void close() {

                popupWindow.setVisible(false);
            }
        };
        popupWindow.setContent(windowLayout);
        windowLayout.addComponent(popupBodyLayout);
        windowLayout.setComponentAlignment(popupBodyLayout, Alignment.MIDDLE_CENTER);

        popupWindow.setVisible(false);
        popupWindow.setResizable(false);
        popupWindow.setClosable(true);
        popupWindow.setStyleName(Reindeer.WINDOW_LIGHT);
        popupWindow.setModal(true);
        popupWindow.setDraggable(false);
        popupWindow.setCaption("&nbsp;&nbsp;Recombine Disease Groups");
        popupWindow.setCaptionAsHtml(true);
        popupBodyLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        popupBodyLayout.setHeightUndefined();//(h - 50) + "px");
        popupWindow.setWindowMode(WindowMode.NORMAL);
        diseaseTypeSelectionList = new NativeSelect();
        this.initPopupLayout();

        UI.getCurrent().addWindow(popupWindow);
        popupWindow.center();

    }

    private final Map<String, String> diseaseStyleMap = new HashMap<String, String>();

    private void initPopupLayout() {

        int h = 0;//(default_DiseaseCat_DiseaseGroupMap.size() * 33) + 300;
        for (Map<String, String> m : default_DiseaseCat_DiseaseGroupMap.values()) {
            if (h < m.size()) {
                h = m.size();
            }
        }
        h = (h * 26) + 200;
        int w = 700;
        if (Page.getCurrent().getBrowserWindowHeight() - 280 < h) {
            h = Page.getCurrent().getBrowserWindowHeight() - 280;
        }
        if (Page.getCurrent().getBrowserWindowWidth() < w) {
            w = Page.getCurrent().getBrowserWindowWidth();
        }

        popupWindow.setWidth(w + "px");
        popupWindow.setHeight(h + "px");

        popupBodyLayout.setWidth((w - 50) + "px");

        Set<String> diseaseSet = Quant_Central_Manager.getDiseaseCategorySet();

        diseaseTypeSelectionList.setDescription("Select disease category");

        for (String disease : diseaseSet) {
            diseaseTypeSelectionList.addItem(disease);
            diseaseTypeSelectionList.setItemCaption(disease, (disease));

        }

        HorizontalLayout diseaseCategorySelectLayout = new HorizontalLayout();
        diseaseCategorySelectLayout.setWidthUndefined();
        diseaseCategorySelectLayout.setHeight("50px");
        diseaseCategorySelectLayout.setSpacing(true);
        diseaseCategorySelectLayout.setMargin(true);

        popupBodyLayout.addComponent(diseaseCategorySelectLayout);
        popupBodyLayout.setComponentAlignment(diseaseCategorySelectLayout, Alignment.TOP_LEFT);

        Label title = new Label("Disease Category");
        title.setStyleName(Reindeer.LABEL_SMALL);
        diseaseCategorySelectLayout.addComponent(title);

        diseaseCategorySelectLayout.setComponentAlignment(title, Alignment.BOTTOM_CENTER);
        diseaseTypeSelectionList.setWidth("200px");
        diseaseTypeSelectionList.setNullSelectionAllowed(false);
        diseaseTypeSelectionList.setValue("All");
        diseaseTypeSelectionList.setImmediate(true);
        diseaseCategorySelectLayout.addComponent(diseaseTypeSelectionList);
        diseaseCategorySelectLayout.setComponentAlignment(diseaseTypeSelectionList, Alignment.TOP_LEFT);
        diseaseTypeSelectionList.setStyleName("diseaseselectionlist");
        diseaseTypeSelectionList.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                boolean showAll = false;
                String value = event.getProperty().getValue().toString();
                if (value.equalsIgnoreCase("All")) {
                    showAll = true;
                }
                for (String dName : diseaseGroupsGridLayoutMap.keySet()) {
                    if (dName.equalsIgnoreCase(value) || showAll) {
                        diseaseGroupsGridLayoutMap.get(dName).setVisible(true);
                    } else {
                        diseaseGroupsGridLayoutMap.get(dName).setVisible(false);
                    }
                }

            }
        });

        VerticalLayout diseaseGroupsNamesContainer = new VerticalLayout();
        diseaseGroupsNamesContainer.setWidth("100%");
        diseaseGroupsNamesContainer.setHeightUndefined();
        popupBodyLayout.addComponent(diseaseGroupsNamesContainer);
        diseaseGroupsNamesContainer.setStyleName(Reindeer.LAYOUT_WHITE);
        GridLayout diseaseNamesHeader = new GridLayout(2, 1);
        diseaseNamesHeader.setWidth("100%");
        diseaseNamesHeader.setHeightUndefined();
        diseaseNamesHeader.setSpacing(true);
        diseaseNamesHeader.setMargin(new MarginInfo(true, false, true, false));
        diseaseGroupsNamesContainer.addComponent(diseaseNamesHeader);
        Label col1Label = new Label("Group Name");
        diseaseNamesHeader.addComponent(col1Label, 0, 0);
        col1Label.setStyleName(Reindeer.LABEL_SMALL);

        Label col2Label = new Label("Suggested Name");
        diseaseNamesHeader.addComponent(col2Label, 1, 0);
        col2Label.setStyleName(Reindeer.LABEL_SMALL);

        Panel diseaseGroupsNamesFrame = new Panel();
        diseaseGroupsNamesFrame.setWidth("100%");
        diseaseGroupsNamesFrame.setHeight((h - 200) + "px");
        diseaseGroupsNamesContainer.addComponent(diseaseGroupsNamesFrame);
        diseaseGroupsNamesFrame.setStyleName(Reindeer.PANEL_LIGHT);

        VerticalLayout diseaseNamesUpdateContainerLayout = new VerticalLayout();
        for (String diseaseCategory : diseaseSet) {
            if (diseaseCategory.equalsIgnoreCase("All")) {
                continue;
            }

            HorizontalLayout diseaseNamesUpdateContainer = initDiseaseNamesUpdateContainer(diseaseCategory);
            diseaseNamesUpdateContainerLayout.addComponent(diseaseNamesUpdateContainer);
            diseaseGroupsGridLayoutMap.put(diseaseCategory, diseaseNamesUpdateContainer);
        }
        diseaseGroupsNamesFrame.setContent(diseaseNamesUpdateContainerLayout);

        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.setMargin(true);
        btnLayout.setSpacing(true);

       
        Button resetFiltersBtn = new Button("Reset");
        resetFiltersBtn.setPrimaryStyleName("resetbtn");
        btnLayout.addComponent(resetFiltersBtn);
        resetFiltersBtn.setWidth("50px");
        resetFiltersBtn.setHeight("24px");

        resetFiltersBtn.setDescription("Reset group names to default");
        resetFiltersBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                resetToDefault();
            }
        }); 
         Button resetToOriginalBtn = new Button("Publications Names");
        resetToOriginalBtn.setPrimaryStyleName("resetbtn");
        btnLayout.addComponent(resetToOriginalBtn);
        resetToOriginalBtn.setWidth("150px");
        resetToOriginalBtn.setHeight("24px");

        resetToOriginalBtn.setDescription("Reset group names to original publication names");
        resetToOriginalBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                resetToPublicationsNames();
            }
        });

        
        
        Button applyFilters = new Button("Update");
        applyFilters.setDescription("Update disease groups with the selected names");
        applyFilters.setPrimaryStyleName("resetbtn");
        applyFilters.setWidth("50px");
        applyFilters.setHeight("24px");

        btnLayout.addComponent(applyFilters);
        applyFilters.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                updateGroups();

            }
        });


        popupBodyLayout.addComponent(btnLayout);
        popupBodyLayout.setComponentAlignment(btnLayout, Alignment.MIDDLE_RIGHT);
        resetToDefault();

    }
    private final Map<String, HorizontalLayout> diseaseGroupsGridLayoutMap = new HashMap<String, HorizontalLayout>();
    private final Map<String, Map<String, ComboBox>> diseaseGroupsSelectionListMap = new HashMap<String, Map<String, ComboBox>>();

    private HorizontalLayout initDiseaseNamesUpdateContainer(String diseaseCategory) {
        GridLayout diseaseNamesUpdateContainer = new GridLayout(2, (default_DiseaseCat_DiseaseGroupMap.get(diseaseCategory).size() * 2));
        diseaseNamesUpdateContainer.setWidth("100%");
        diseaseNamesUpdateContainer.setHeightUndefined();
        diseaseNamesUpdateContainer.setSpacing(false);
        diseaseNamesUpdateContainer.setMargin(new MarginInfo(false, false, false, false));

        int widthCalc = 0;
        int row = 0;
        int col = 0;
        Map<String, ComboBox> diseaseGroupNameToListMap = new LinkedHashMap<String, ComboBox>();
        for (String diseaseGroupName : default_DiseaseCat_DiseaseGroupMap.get(diseaseCategory).keySet()) {
//            if(!diseaseGroupName.contains(diseaseCategory))
//                continue;
            diseaseNamesUpdateContainer.addComponent(generateLabel(diseaseGroupName, diseaseCategory), col, row);
            ComboBox list = generateLabelList(diseaseCategory);
            diseaseNamesUpdateContainer.addComponent(list, col + 1, row);
            diseaseGroupNameToListMap.put(diseaseGroupName, list);

            col = 0;
            row++;

            VerticalLayout spacer1 = new VerticalLayout();
            spacer1.setHeight("2px");
            spacer1.setWidth("300px");
            spacer1.setStyleName(Reindeer.LAYOUT_WHITE);
            diseaseNamesUpdateContainer.addComponent(spacer1, col, row);
            VerticalLayout spacer2 = new VerticalLayout();
            spacer2.setHeight("2px");
            spacer2.setWidth("300px");
            spacer2.setStyleName(Reindeer.LAYOUT_WHITE);//"lightgraylayout");
            diseaseNamesUpdateContainer.addComponent(spacer2, col + 1, row);

            col = 0;
            row++;
            widthCalc += 26;

        }
        diseaseGroupsSelectionListMap.put(diseaseCategory, diseaseGroupNameToListMap);

//        widthCalc-=26;
        VerticalLayout diseaseLabelContainer = new VerticalLayout();

        Label label = new Label("<center><font  color='#ffffff'>" + diseaseCategory + "</font></center>");
        label.setContentMode(ContentMode.HTML);
        diseaseLabelContainer.setHeight(widthCalc + "px");
        diseaseLabelContainer.setWidth("20px");
        VerticalLayout rotateContainer = new VerticalLayout();

        rotateContainer.setWidth(widthCalc + "px");
        rotateContainer.setHeight("20px");
        diseaseLabelContainer.addComponent(rotateContainer);

        rotateContainer.setStyleName("row_" + diseaseStyleMap.get(diseaseCategory));
        rotateContainer.addComponent(label);
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);
        layout.addComponent(diseaseLabelContainer);
        layout.addComponent(diseaseNamesUpdateContainer);

        return layout;
    }

    private VerticalLayout generateLabel(String strLabel, String diseaseCategory) {
        DiseaseGroupLabel container = new DiseaseGroupLabel(300, strLabel, diseaseStyleMap.get(diseaseCategory));
        container.setHeight("24px");
        return container;
    }

    private ComboBox generateLabelList(String diseaseCategory) {
        final ComboBox diseaseGroupsList = new ComboBox();
        diseaseGroupsList.setStyleName("diseasegrouplist");
        diseaseGroupsList.setNullSelectionAllowed(false);
        diseaseGroupsList.setImmediate(true);
        diseaseGroupsList.setNewItemsAllowed(true);
        diseaseGroupsList.setWidth(300, Unit.PIXELS);
//        diseaseGroupsList.setScrollToSelectedItem(true);
        diseaseGroupsList.setPageLength(500);

        for (String name : default_DiseaseCat_DiseaseGroupMap.get(diseaseCategory).values()) {
            diseaseGroupsList.addItem(name);
            diseaseGroupsList.select(name);
        }

        diseaseGroupsList.setNewItemHandler(new AbstractSelect.NewItemHandler() {

            @Override
            public void addNewItem(String newItemCaption) {
                diseaseGroupsList.addItem(newItemCaption);
                diseaseGroupsList.select(newItemCaption);
            }
        });
        diseaseGroupsList.setHeight("24px");

        return diseaseGroupsList;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        popupWindow.setVisible(true);
    }

    private void resetToDefault() {

//       
        String selectedDiseaseKey = diseaseTypeSelectionList.getValue().toString().trim();

        if (selectedDiseaseKey.equalsIgnoreCase("All")) {
            for (String diseaseKey : diseaseGroupsSelectionListMap.keySet()) {
                Map<String, ComboBox> diseaseGroupNameToListMap = diseaseGroupsSelectionListMap.get(diseaseKey);
                for (String key : diseaseGroupNameToListMap.keySet()) {
                    ComboBox list = diseaseGroupNameToListMap.get(key);
                    list.select(default_DiseaseCat_DiseaseGroupMap.get(diseaseKey).get(key));
                }
            }
        } else {
            Map<String, ComboBox> diseaseGroupNameToListMap = diseaseGroupsSelectionListMap.get(selectedDiseaseKey);
            for (String key : diseaseGroupNameToListMap.keySet()) {
                ComboBox list = diseaseGroupNameToListMap.get(key);
                list.select(default_DiseaseCat_DiseaseGroupMap.get(selectedDiseaseKey).get(key));
            }

        }

//            System.out.println("at default_DiseaseCat_DiseaseGroupMap " + default_DiseaseCat_DiseaseGroupMap.containsKey(diseaseKey) + "  " + diseaseKey);
//        
    }

    private void updateGroups() {
        Map<String, Map<String, String>> updatedGroupsNamesMap = new HashMap<String, Map<String, String>>();
        for (String diseaseKey : diseaseGroupsSelectionListMap.keySet()) {
             Map<String, ComboBox> diseaseGroupNameToListMap = diseaseGroupsSelectionListMap.get(diseaseKey);
             Map<String, String> updatedDiseaseGroupsMappingName = new LinkedHashMap<String, String>();
                for (String key : diseaseGroupNameToListMap.keySet()) {
                    ComboBox list = diseaseGroupNameToListMap.get(key);
                    updatedDiseaseGroupsMappingName.put(key, list.getValue().toString().trim());
                    
                }
             updatedGroupsNamesMap.put(diseaseKey,updatedDiseaseGroupsMappingName );

        }
        
        Quant_Central_Manager.updateDiseaseGroupsNames(updatedGroupsNamesMap);
        popupWindow.close();

    }
    
    
    private void resetToPublicationsNames() {
        Map<String, Map<String, String>> updatedGroupsNamesMap = new HashMap<String, Map<String, String>>();
        for (String diseaseKey : diseaseGroupsSelectionListMap.keySet()) {
             Map<String, ComboBox> diseaseGroupNameToListMap = diseaseGroupsSelectionListMap.get(diseaseKey);
             Map<String, String> updatedDiseaseGroupsMappingName = new LinkedHashMap<String, String>();
                for (String key : diseaseGroupNameToListMap.keySet()) {
                    ComboBox list = diseaseGroupNameToListMap.get(key);
                    list.addItem(key);
                    updatedDiseaseGroupsMappingName.put(key, key);
                    
                }
             updatedGroupsNamesMap.put(diseaseKey,updatedDiseaseGroupsMappingName );

        }
        
        Quant_Central_Manager.updateDiseaseGroupsNames(updatedGroupsNamesMap);
        popupWindow.close();

    }

}
