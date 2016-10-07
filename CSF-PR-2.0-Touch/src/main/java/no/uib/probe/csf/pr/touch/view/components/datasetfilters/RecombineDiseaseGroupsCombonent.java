package no.uib.probe.csf.pr.touch.view.components.datasetfilters;

import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseCategoryObject;
import no.uib.probe.csf.pr.touch.view.core.DiseaseGroupLabel;
import no.uib.probe.csf.pr.touch.view.core.InformationButton;
import no.uib.probe.csf.pr.touch.view.core.PopupWindow;

/**
 *
 * @author Yehia Farag
 *
 * this class represents disease sub group rename and re-combine
 */
public abstract class RecombineDiseaseGroupsCombonent extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private final PopupWindow popupWindow;
    private final VerticalLayout popupBodyLayout;
    private final ComboBox diseaseTypeSelectionList;
    private final Map<String, Integer> captionAstrMap;

    private final int screenWidth = Math.min(Page.getCurrent().getBrowserWindowWidth(), 1000);
    private final int screenHeight = Math.min(Page.getCurrent().getBrowserWindowHeight(), 800);
    private final int maxHeight;
    private Panel diseaseGroupsNamesPanel;

    public RecombineDiseaseGroupsCombonent(Collection<DiseaseCategoryObject> diseaseCategorySet, boolean smallScreen) {

        maxHeight = screenHeight - 220;
        this.setDescription("Recombine disease groups");
        this.setStyleName("filterbtn");
        Image icon = new Image();
        icon.setStyleName("combinegroupbtn");
        icon.setSource(new ThemeResource("img/connect-o.png"));
        this.addComponent(icon);
        this.setComponentAlignment(icon, Alignment.TOP_CENTER);
        icon.setWidth(100, Unit.PERCENTAGE);
        icon.setHeight(100, Unit.PERCENTAGE);
        this.addLayoutClickListener(RecombineDiseaseGroupsCombonent.this);

        //init window layout 
        VerticalLayout frame = new VerticalLayout();
        frame.setWidth(99, Unit.PERCENTAGE);
        frame.setSpacing(true);
        popupBodyLayout = new VerticalLayout();
        frame.addComponent(popupBodyLayout);

        popupBodyLayout.setWidth(100, Unit.PERCENTAGE);
//        popupBodyLayout.setHeight(screenHeight - 100, Unit.PIXELS);
        popupBodyLayout.setSpacing(true);
        popupBodyLayout.setMargin(true);

        popupBodyLayout.addStyleName("roundedborder");
        popupBodyLayout.addStyleName("padding20");
        popupBodyLayout.addStyleName("whitelayout");
        popupBodyLayout.addStyleName("margintop");

        popupWindow = new PopupWindow(frame, "Recombine Disease Groups") {
            @Override
            public void close() {

                popupWindow.setVisible(false);
            }
        };
        popupWindow.setWidth(screenWidth, Unit.PIXELS);

        diseaseTypeSelectionList = new ComboBox();

        captionAstrMap = new HashMap<>();
        diseaseGroupsNamesPanel = new Panel();
        diseaseGroupsNamesPanel.setWidth(100, Unit.PERCENTAGE);
        diseaseGroupsNamesPanel.setStyleName(ValoTheme.PANEL_BORDERLESS);
        initPopupLayout(diseaseCategorySet, smallScreen);

        HorizontalLayout btnsFrame = new HorizontalLayout();
        btnsFrame.setWidth(100, Unit.PERCENTAGE);
       btnsFrame.addStyleName("roundedborder");
        btnsFrame.addStyleName("padding10");
        btnsFrame.addStyleName("whitelayout");
        btnsFrame.setMargin(new MarginInfo(true, false, false, false));
        btnsFrame.setWidth(100, Unit.PERCENTAGE);
        btnsFrame.addStyleName("margintop");
         btnsFrame.addStyleName("marginbottom");
        btnsFrame.setHeight(50, Unit.PIXELS);
        btnsFrame.addStyleName("padding20");
        frame.addComponent(btnsFrame);

        HorizontalLayout leftsideWrapper = new HorizontalLayout();
        btnsFrame.addComponent(leftsideWrapper);
        btnsFrame.setComponentAlignment(leftsideWrapper, Alignment.TOP_LEFT);
        leftsideWrapper.setSpacing(true);

        InformationButton info = new InformationButton("The disease group names can be altered by the user or reset to the names used in the original publications. If the same name is used for more than one group those groups will be merged into a single group in the other displays. After altering the names click the \"Update\" button.", true);
        leftsideWrapper.addComponent(info);

        HorizontalLayout bottomContainert = new HorizontalLayout();
        bottomContainert.setWidth(100, Unit.PERCENTAGE);
        bottomContainert.setHeight(100, Unit.PERCENTAGE);
//        bottomContainert.setMargin(new MarginInfo(false, true, false, true));

        HorizontalLayout btnLayout = new HorizontalLayout();

        btnsFrame.addComponent(btnLayout);
        btnsFrame.setComponentAlignment(btnLayout, Alignment.TOP_RIGHT);
        btnLayout.setSpacing(true);

        Button resetFiltersBtn = new Button("Suggested Groups");
        resetFiltersBtn.setStyleName(ValoTheme.BUTTON_TINY);
        btnLayout.addComponent(resetFiltersBtn);

        resetFiltersBtn.setDescription("Use CSF-PR suggested group names");
        resetFiltersBtn.addClickListener((Button.ClickEvent event) -> {
            resetToDefault();
        });
        Button resetToOriginalBtn = new Button("Publication Names");
        resetToOriginalBtn.setStyleName(ValoTheme.BUTTON_TINY);
        btnLayout.addComponent(resetToOriginalBtn);

        resetToOriginalBtn.setDescription("Reset group names to original publication names");
        resetToOriginalBtn.addClickListener((Button.ClickEvent event) -> {
            resetToPublicationsNames();
        });

        Button applyFilters = new Button("Apply");
        applyFilters.setDescription("Update disease groups with the selected names");
        applyFilters.setStyleName(ValoTheme.BUTTON_TINY);

        btnLayout.addComponent(applyFilters);
        applyFilters.addClickListener((Button.ClickEvent event) -> {
            updateGroups();
        });

//        popupBodyLayout.addComponent(btnLayout);
//        popupBodyLayout.setComponentAlignment(btnLayout, Alignment.MIDDLE_RIGHT);
    }

    private void initPopupLayout(Collection<DiseaseCategoryObject> diseaseCategorySet, boolean smallScreen) {

//        popupWindow.setWidth(width, Unit.PIXELS);
//        popupWindow.setHeight(height, Unit.PIXELS);
//        popupBodyLayout.setWidth(width - 20, Unit.PIXELS);
        diseaseTypeSelectionList.setDescription("Select disease category");
        for (DiseaseCategoryObject disease : diseaseCategorySet) {
            diseaseTypeSelectionList.addItem(disease.getDiseaseCategory());
            diseaseTypeSelectionList.setItemCaption(disease.getDiseaseCategory(), disease.getDiseaseCategory());

        }

        HorizontalLayout diseaseCategorySelectLayout = new HorizontalLayout();
        diseaseCategorySelectLayout.setWidthUndefined();
        diseaseCategorySelectLayout.setHeightUndefined();
        diseaseCategorySelectLayout.setSpacing(true);
        diseaseCategorySelectLayout.setMargin(false);

        popupBodyLayout.addComponent(diseaseCategorySelectLayout);
        popupBodyLayout.setComponentAlignment(diseaseCategorySelectLayout, Alignment.TOP_CENTER);

        Label title = new Label("Disease Category");
        title.setStyleName(ValoTheme.LABEL_SMALL);
        title.addStyleName(ValoTheme.LABEL_BOLD);
        diseaseCategorySelectLayout.addComponent(title);
        diseaseCategorySelectLayout.setComponentAlignment(title, Alignment.TOP_CENTER);
        diseaseTypeSelectionList.setWidth((int) (screenWidth * 0.5), Unit.PIXELS);
        diseaseTypeSelectionList.setNullSelectionAllowed(false);
        diseaseTypeSelectionList.setValue("All Diseases");
        diseaseTypeSelectionList.setImmediate(true);
        diseaseTypeSelectionList.setNewItemsAllowed(false);
        diseaseTypeSelectionList.setTextInputAllowed(false);
        diseaseTypeSelectionList.setStyleName(ValoTheme.COMBOBOX_TINY);
        diseaseCategorySelectLayout.addComponent(diseaseTypeSelectionList);
        diseaseCategorySelectLayout.setComponentAlignment(diseaseTypeSelectionList, Alignment.TOP_LEFT);

        diseaseTypeSelectionList.addValueChangeListener((Property.ValueChangeEvent event) -> {
            boolean showAll = false;
            String value = event.getProperty().getValue().toString();
            if (value.equalsIgnoreCase("All Diseases")) {
                showAll = true;
            }
            int heightColc = 0;
            for (String dName : diseaseGroupsGridLayoutMap.keySet()) {
                if (dName.equalsIgnoreCase(value) || showAll) {
                    diseaseGroupsGridLayoutMap.get(dName).setVisible(true);
                    heightColc += (Integer) diseaseGroupsGridLayoutMap.get(dName).getData();
                } else {
                    diseaseGroupsGridLayoutMap.get(dName).setVisible(false);
                }
            }

            diseaseGroupsNamesPanel.setHeight(Math.min(maxHeight - 30, heightColc), Unit.PIXELS);
            popupWindow.setHeight(diseaseGroupsNamesPanel.getHeight() + (305), Unit.PIXELS);
        });

        VerticalLayout diseaseGroupsNamesContainer = new VerticalLayout();
        diseaseGroupsNamesContainer.addStyleName("paddingleft20");
        diseaseGroupsNamesContainer.setWidth(100, Unit.PERCENTAGE);
        diseaseGroupsNamesContainer.setHeightUndefined();
        popupBodyLayout.addComponent(diseaseGroupsNamesContainer);
        diseaseGroupsNamesContainer.setStyleName("whitelayout");
        GridLayout diseaseNamesHeader = new GridLayout(2, 1);

        diseaseNamesHeader.setWidth(100, Unit.PERCENTAGE);
        diseaseNamesHeader.setHeightUndefined();
        diseaseNamesHeader.setSpacing(true);
        if (smallScreen) {
            diseaseNamesHeader.setMargin(false);
        } else {
            diseaseNamesHeader.setMargin(new MarginInfo(true, false, false, false));
        }

        diseaseNamesHeader.addStyleName("marginbottom");
        diseaseGroupsNamesContainer.addComponent(diseaseNamesHeader);
        Label col1Label = new Label("Publication Name");

        col1Label.setStyleName(ValoTheme.LABEL_SMALL);
        col1Label.addStyleName(ValoTheme.LABEL_BOLD);
        col1Label.addStyleName("paddingleft44");
        col1Label.setWidthUndefined();
        diseaseNamesHeader.addComponent(col1Label, 0, 0);
        diseaseNamesHeader.setComponentAlignment(col1Label, Alignment.TOP_LEFT);

        Label col2Label = new Label("Group Name");

        col2Label.setStyleName(ValoTheme.LABEL_SMALL);
        col2Label.addStyleName(ValoTheme.LABEL_BOLD);
        col2Label.addStyleName("paddingleft40");
        diseaseNamesHeader.addComponent(col2Label, 1, 0);
        col2Label.setWidthUndefined();
        diseaseNamesHeader.setComponentAlignment(col2Label, Alignment.TOP_LEFT);

        diseaseGroupsNamesContainer.addComponent(diseaseGroupsNamesPanel);

        VerticalLayout diseaseNamesUpdateContainerLayout = new VerticalLayout();

        diseaseNamesUpdateContainerLayout.setWidth(100, Unit.PERCENTAGE);
        diseaseNamesUpdateContainerLayout.setMargin(false);
        diseaseCategorySet.stream().filter((diseaseCategory) -> !(diseaseCategory.getDiseaseCategory().equalsIgnoreCase("All Diseases"))).forEach((diseaseCategory) -> {
            HorizontalLayout diseaseNamesUpdateContainer = initDiseaseNamesUpdateContainer(diseaseCategory, screenWidth);

            diseaseNamesUpdateContainerLayout.addComponent(diseaseNamesUpdateContainer);
            diseaseNamesUpdateContainerLayout.setComponentAlignment(diseaseNamesUpdateContainer, Alignment.TOP_CENTER);
            diseaseGroupsGridLayoutMap.put(diseaseCategory.getDiseaseCategory(), diseaseNamesUpdateContainer);
        });
        diseaseGroupsNamesPanel.setContent(diseaseNamesUpdateContainerLayout);
        diseaseGroupsNamesPanel.setHeight((maxHeight - 30), Unit.PIXELS);
        popupWindow.setHeight(diseaseGroupsNamesPanel.getHeight() + (305-35), Unit.PIXELS);

        resetToDefault();

    }

    private final Map<String, HorizontalLayout> diseaseGroupsGridLayoutMap = new HashMap<>();
    private final Map<String, Map<String, ComboBox>> diseaseGroupsSelectionListMap = new HashMap<>();

    private HorizontalLayout initDiseaseNamesUpdateContainer(DiseaseCategoryObject diseaseCategory, int width) {
        GridLayout diseaseNamesUpdateContainer = new GridLayout(2, (diseaseCategory.getDiseaseSubGroups().size() * 2));
        diseaseNamesUpdateContainer.setWidth(width, Unit.PIXELS);
        diseaseNamesUpdateContainer.setHeightUndefined();
        diseaseNamesUpdateContainer.setSpacing(false);

        int widthCalc = 0;
        int row = 0;
        int col = 0;
        subGroupList.clear();
        Map<String, ComboBox> diseaseGroupNameToListMap = new LinkedHashMap<>();
        for (String diseaseGroupName : diseaseCategory.getDiseaseSubGroups().keySet()) {
            VerticalLayout label = generateLabel(diseaseGroupName, diseaseCategory.getDiseaseSubGroupsToFullName().get(diseaseGroupName), diseaseCategory.getDiseaseStyleName());
            diseaseNamesUpdateContainer.addComponent(label, col, row);
            ComboBox list = generateLabelList(diseaseCategory, diseaseGroupName);

            diseaseNamesUpdateContainer.addComponent(list, col + 1, row);
            diseaseNamesUpdateContainer.setComponentAlignment(list, Alignment.TOP_LEFT);
            diseaseGroupNameToListMap.put(diseaseGroupName, list);

            col = 0;
            row++;

            VerticalLayout spacer1 = new VerticalLayout();
            spacer1.setHeight(2, Unit.PIXELS);
            spacer1.setWidth(10, Unit.PIXELS);
            spacer1.setStyleName("whitelayout");
            diseaseNamesUpdateContainer.addComponent(spacer1, col, row);
            VerticalLayout spacer2 = new VerticalLayout();
            spacer2.setHeight(2, Unit.PIXELS);
            spacer2.setWidth(10, Unit.PIXELS);
            spacer2.setStyleName("whitelayout");
            diseaseNamesUpdateContainer.addComponent(spacer2, col + 1, row);

            col = 0;
            row++;
            widthCalc += 26;

        }

        diseaseGroupsSelectionListMap.put(diseaseCategory.getDiseaseCategory(), diseaseGroupNameToListMap);
        VerticalLayout diseaseLabelContainer = new VerticalLayout();
        Label label = new Label("<center><font  color='#ffffff'>" + diseaseCategory.getDiseaseCategory() + "</font></center>");
        label.setContentMode(ContentMode.HTML);
        diseaseLabelContainer.setHeight(widthCalc, Unit.PIXELS);
        diseaseLabelContainer.setWidth(20, Unit.PIXELS);
        VerticalLayout rotateContainer = new VerticalLayout();
        rotateContainer.setWidth(widthCalc, Unit.PIXELS);
        rotateContainer.setHeight(24, Unit.PIXELS);
        diseaseLabelContainer.addComponent(rotateContainer);
        rotateContainer.setStyleName(diseaseCategory.getDiseaseStyleName());
        rotateContainer.addStyleName("rotateheader");
        rotateContainer.addComponent(label);
        rotateContainer.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthUndefined();
        layout.setSpacing(true);
        layout.addComponent(diseaseLabelContainer);
        layout.addComponent(diseaseNamesUpdateContainer);
        layout.setData((((row + 2) / 2) * 25));

        return layout;
    }

    private VerticalLayout generateLabel(String strLabel, String description, String diseaseStyle) {
        DiseaseGroupLabel container = new DiseaseGroupLabel(strLabel, diseaseStyle);
        container.setDescription(description);
        return container;
    }
    private Set<String> subGroupList = new LinkedHashSet<>();

    private ComboBox generateLabelList(DiseaseCategoryObject diseaseCategory, String diseaseSubGroup) {
        final ComboBox diseaseGroupsList = new ComboBox();
        diseaseGroupsList.setStyleName(ValoTheme.COMBOBOX_TINY);
        diseaseGroupsList.setStyleName(ValoTheme.COMBOBOX_SMALL);
        diseaseGroupsList.setWidth(79, Unit.PERCENTAGE);
        diseaseGroupsList.addStyleName("paddingleft20");
        diseaseGroupsList.setNullSelectionAllowed(false);
        diseaseGroupsList.setImmediate(true);
        diseaseGroupsList.setNewItemsAllowed(true);
        diseaseGroupsList.setPageLength(500);
        diseaseGroupsList.setInputPrompt("Select or enter new disease group name");
        diseaseGroupsList.setDescription("Select or enter new disease group name");

        String defaultValue = diseaseCategory.getDiseaseSubGroups().get(diseaseSubGroup);
        diseaseGroupsList.setData(defaultValue);

        diseaseGroupsList.addItem(defaultValue);
        captionAstrMap.put(defaultValue, 0);
        subGroupList.add(defaultValue);

        for (String name : diseaseCategory.getDiseaseSubGroups().keySet()) {
            subGroupList.add(name);
            diseaseGroupsList.addItem(name);
            if (!captionAstrMap.containsKey(name)) {
                captionAstrMap.put(name, 0);
            }

        }
        subGroupList.add("Healthy*");
        diseaseGroupsList.addItem("Healthy*");
        if (!captionAstrMap.containsKey("Healthy*")) {
            captionAstrMap.put("Healthy*", 0);
        }
//        if (!captionAstrMap.containsKey(defaultValue)) {
//            diseaseGroupsList.addItem(defaultValue);
//            captionAstrMap.put(defaultValue, 0);
//        }
        diseaseGroupsList.setNewItemHandler((String newItemCaption) -> {
            String ast = "";
            if (!newItemCaption.contains("*")) {
                ast = "*";
            }
            diseaseGroupsList.addItem(newItemCaption + ast);
            diseaseGroupsList.select(newItemCaption + ast);
            captionAstrMap.put(newItemCaption + ast, 1);
        });
        diseaseGroupsList.setHeight(24, Unit.PIXELS);
        diseaseGroupsList.addValueChangeListener(new Property.ValueChangeListener() {
            private String lastSelected = "";

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (event.getProperty().getValue() == null) {
                    return;
                }
                String selectedItem = event.getProperty().getValue().toString();
                if (!selectedItem.equalsIgnoreCase(diseaseGroupsList.getData().toString())) {
                    String astr = "";
                    if (!selectedItem.contains("*")) {
                        astr = "*";
                        captionAstrMap.put(selectedItem, captionAstrMap.get(selectedItem) + 1);
                        updateAllList(selectedItem, selectedItem + astr);
                    }
                    diseaseGroupsList.setItemCaption(selectedItem, selectedItem + astr);
                } else {
                    diseaseGroupsList.setItemCaption(diseaseGroupsList.getData().toString(), diseaseGroupsList.getData().toString());
                }
                if (!lastSelected.equalsIgnoreCase("")) {
                    captionAstrMap.put(lastSelected, Math.max(captionAstrMap.get(lastSelected) - 1, 0));
                }
                lastSelected = selectedItem;
            }
        });

        return diseaseGroupsList;
    }

    private void updateAllList(String itemId, String itemCap) {
        for (String diseaseKey : diseaseGroupsSelectionListMap.keySet()) {
            Map<String, ComboBox> diseaseGroupNameToListMap = diseaseGroupsSelectionListMap.get(diseaseKey);
            for (String key : diseaseGroupNameToListMap.keySet()) {
                ComboBox list = diseaseGroupNameToListMap.get(key);
                list.setItemCaption(itemId, itemCap);
            }
        }
//        updateListItems();

    }

//    private void updateListItems() {
//        for (String diseaseKey : diseaseGroupsSelectionListMap.keySet()) {
//            Map<String, ComboBox> diseaseGroupNameToListMap = diseaseGroupsSelectionListMap.get(diseaseKey);
//            for (String key : diseaseGroupNameToListMap.keySet()) {
//                ComboBox list = diseaseGroupNameToListMap.get(key);
//                for (String subGroup : subGroupList) {
//                    if (list.getItem(subGroup) != null) {
//                        list.addItem(subGroup);
//                    }
//                }
//            }
//        }
//
//    }
    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        popupWindow.setVisible(true);
    }

    private void resetToDefault() {
        String selectedDiseaseKey = diseaseTypeSelectionList.getValue().toString().trim();

        if (selectedDiseaseKey.equalsIgnoreCase("All Diseases")) {
            for (String diseaseKey : diseaseGroupsSelectionListMap.keySet()) {
                Map<String, ComboBox> diseaseGroupNameToListMap = diseaseGroupsSelectionListMap.get(diseaseKey);
                for (String key : diseaseGroupNameToListMap.keySet()) {
                    ComboBox list = diseaseGroupNameToListMap.get(key);
                    list.setValue(list.getData() + "");
                }
            }
        } else {
            Map<String, ComboBox> diseaseGroupNameToListMap = diseaseGroupsSelectionListMap.get(selectedDiseaseKey);
            for (String key : diseaseGroupNameToListMap.keySet()) {
                ComboBox list = diseaseGroupNameToListMap.get(key);
                list.setValue(list.getData() + "");

            }

        }

    }

    private void updateGroups() {
        Map<String, Map<String, String>> updatedGroupsNamesMap = new HashMap<>();

        diseaseGroupsSelectionListMap.keySet().stream().forEach((diseaseKey) -> {
            Map<String, ComboBox> diseaseGroupNameToListMap = diseaseGroupsSelectionListMap.get(diseaseKey);
            Map<String, String> updatedDiseaseGroupsMappingName = new LinkedHashMap<>();
            diseaseGroupNameToListMap.keySet().stream().forEach((key) -> {
                ComboBox list = diseaseGroupNameToListMap.get(key);
                String selection;
                if (list.getValue() != null) {
                    selection = list.getItemCaption(list.getValue()).trim();
                } else {
                    selection = list.getData().toString();
                }
                updatedDiseaseGroupsMappingName.put(key, selection);
            });

            updatedGroupsNamesMap.put(diseaseKey, updatedDiseaseGroupsMappingName);
        });

        updateSystem(updatedGroupsNamesMap);
        popupWindow.close();

    }

    private void resetToPublicationsNames() {
        updateSystem(null);
        popupWindow.close();

    }

    public abstract void updateSystem(Map<String, Map<String, String>> updatedGroupsNamesMap);

    public void resizeFilter(double resizeFactor) {
        this.setWidth((int) (25 * resizeFactor), Unit.PIXELS);
        this.setHeight((int) (25 * resizeFactor), Unit.PIXELS);
    }

}
