
package no.uib.probe.csf.pr.touch.view.components.datasetfilters;

import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import no.uib.probe.csf.pr.touch.logic.beans.DiseaseCategoryObject;
import no.uib.probe.csf.pr.touch.view.core.DiseaseGroupLabel;

/**
 *
 * @author Yehia Farag
 *
 * this class represents disease sub group rename and re-combine
 */
public abstract class RecombineDiseaseGroupsCombonent extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private final Window popupWindow;
    private final VerticalLayout popupBodyLayout;
    private final ComboBox diseaseTypeSelectionList;
    private final Map<String, Integer> captionAstrMap;

    public RecombineDiseaseGroupsCombonent(Collection<DiseaseCategoryObject> diseaseCategorySet) {
        this.setDescription("Recombine disease groups");
        this.setWidth(25, Unit.PIXELS);
        this.setHeight(25, Unit.PIXELS);
        this.setStyleName("filterbtn");
        Image icon = new Image();
        icon.setStyleName("cobinegroupbtn");
        icon.setSource(new ThemeResource("img/connect-o.png"));
        this.addComponent(icon);
        this.setComponentAlignment(icon, Alignment.MIDDLE_CENTER);
        icon.setWidth(100, Unit.PERCENTAGE);
        icon.setHeight(100, Unit.PERCENTAGE);
        this.addLayoutClickListener(RecombineDiseaseGroupsCombonent.this);

        this.popupBodyLayout = new VerticalLayout();
        VerticalLayout popupBody = new VerticalLayout();
        popupWindow = new Window() {
            @Override
            public void close() {

                popupWindow.setVisible(false);
            }
        };
        popupWindow.setContent(popupBody);
        popupBody.addComponent(popupBodyLayout);
        popupBody.setComponentAlignment(popupBodyLayout, Alignment.MIDDLE_CENTER);

        popupWindow.setVisible(false);
        popupWindow.setResizable(false);
        popupWindow.setClosable(true);
        popupWindow.setModal(true);
        popupWindow.setDraggable(false);
        popupWindow.setCaption("<font color='gray' style='font-weight: bold;!important'>&nbsp;&nbsp;Recombine Disease Groups</font>");
        popupWindow.setCaptionAsHtml(true);
        popupBodyLayout.setStyleName("whitelayout");
        popupBodyLayout.setHeightUndefined();
        popupWindow.setWindowMode(WindowMode.NORMAL);
        diseaseTypeSelectionList = new ComboBox();
        UI.getCurrent().addWindow(popupWindow);
        popupWindow.center();

        captionAstrMap = new HashMap<>();
        initPopupLayout(diseaseCategorySet);

    }

    private void initPopupLayout(Collection<DiseaseCategoryObject> diseaseCategorySet) {

        int width = Math.min(Page.getCurrent().getBrowserWindowWidth(), 800);
        int height = Math.min(Page.getCurrent().getBrowserWindowHeight(), 800);

        popupWindow.setWidth(width, Unit.PIXELS);
        popupWindow.setHeight(height, Unit.PIXELS);
        popupBodyLayout.setWidth(width - 20, Unit.PIXELS);

        diseaseTypeSelectionList.setDescription("Select disease category");
        for (DiseaseCategoryObject disease : diseaseCategorySet) {
            diseaseTypeSelectionList.addItem(disease.getDiseaseCategory());
            diseaseTypeSelectionList.setItemCaption(disease.getDiseaseCategory(), disease.getDiseaseCategory());

        }

        HorizontalLayout diseaseCategorySelectLayout = new HorizontalLayout();
        diseaseCategorySelectLayout.setWidthUndefined();
        diseaseCategorySelectLayout.setHeightUndefined();
        diseaseCategorySelectLayout.setSpacing(true);
        diseaseCategorySelectLayout.setMargin(new MarginInfo(true, false, false, true));

        popupBodyLayout.addComponent(diseaseCategorySelectLayout);
        popupBodyLayout.setComponentAlignment(diseaseCategorySelectLayout, Alignment.TOP_LEFT);

        Label title = new Label("Disease Category");
        diseaseCategorySelectLayout.addComponent(title);

        diseaseCategorySelectLayout.setComponentAlignment(title, Alignment.TOP_CENTER);
        diseaseTypeSelectionList.setWidth(width * 50 / 100, Unit.PIXELS);
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
            for (String dName : diseaseGroupsGridLayoutMap.keySet()) {
                if (dName.equalsIgnoreCase(value) || showAll) {
                    diseaseGroupsGridLayoutMap.get(dName).setVisible(true);
                } else {
                    diseaseGroupsGridLayoutMap.get(dName).setVisible(false);
                }
            }
        });

        VerticalLayout diseaseGroupsNamesContainer = new VerticalLayout();
        diseaseGroupsNamesContainer.setWidth(100, Unit.PERCENTAGE);
        diseaseGroupsNamesContainer.setHeightUndefined();
        popupBodyLayout.addComponent(diseaseGroupsNamesContainer);
        diseaseGroupsNamesContainer.setStyleName("whitelayout");
        GridLayout diseaseNamesHeader = new GridLayout(2, 1);
        diseaseNamesHeader.setWidth(100, Unit.PERCENTAGE);
        diseaseNamesHeader.setHeightUndefined();
        diseaseNamesHeader.setSpacing(true);
        diseaseNamesHeader.setMargin(new MarginInfo(true, false, false, false));
        diseaseGroupsNamesContainer.addComponent(diseaseNamesHeader);
        Label col1Label = new Label("Group Name");
        col1Label.setWidthUndefined();
        diseaseNamesHeader.addComponent(col1Label, 0, 0);
        diseaseNamesHeader.setComponentAlignment(col1Label, Alignment.MIDDLE_CENTER);

        Label col2Label = new Label("Suggested Name");
        diseaseNamesHeader.addComponent(col2Label, 1, 0);
        col2Label.setWidthUndefined();
        diseaseNamesHeader.setComponentAlignment(col2Label, Alignment.MIDDLE_CENTER);

        Panel diseaseGroupsNamesFrame = new Panel();
        diseaseGroupsNamesFrame.setWidth(100, Unit.PERCENTAGE);
        diseaseGroupsNamesFrame.setHeight((height * 67 / 100), Unit.PIXELS);
        diseaseGroupsNamesContainer.addComponent(diseaseGroupsNamesFrame);
        diseaseGroupsNamesFrame.setStyleName(ValoTheme.PANEL_BORDERLESS);

        VerticalLayout diseaseNamesUpdateContainerLayout = new VerticalLayout();
        diseaseNamesUpdateContainerLayout.setWidth(100, Unit.PERCENTAGE);
        diseaseNamesUpdateContainerLayout.setMargin(true);
        diseaseCategorySet.stream().filter((diseaseCategory) -> !(diseaseCategory.getDiseaseCategory().equalsIgnoreCase("All Diseases"))).forEach((diseaseCategory) -> {
            HorizontalLayout diseaseNamesUpdateContainer = initDiseaseNamesUpdateContainer(diseaseCategory, width - 85);
            diseaseNamesUpdateContainerLayout.addComponent(diseaseNamesUpdateContainer);
            diseaseNamesUpdateContainerLayout.setComponentAlignment(diseaseNamesUpdateContainer, Alignment.TOP_CENTER);
            diseaseGroupsGridLayoutMap.put(diseaseCategory.getDiseaseCategory(), diseaseNamesUpdateContainer);
        });
        diseaseGroupsNamesFrame.setContent(diseaseNamesUpdateContainerLayout);

        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.setMargin(true);
        btnLayout.setSpacing(true);

        Button resetFiltersBtn = new Button("Reset");
        resetFiltersBtn.setStyleName(ValoTheme.BUTTON_TINY);
        btnLayout.addComponent(resetFiltersBtn);

        resetFiltersBtn.setDescription("Reset group names to default");
        resetFiltersBtn.addClickListener((Button.ClickEvent event) -> {
            resetToDefault();
        });
        Button resetToOriginalBtn = new Button("Publications Names");
        resetToOriginalBtn.setStyleName(ValoTheme.BUTTON_TINY);
        btnLayout.addComponent(resetToOriginalBtn);

        resetToOriginalBtn.setDescription("Reset group names to original publication names");
        resetToOriginalBtn.addClickListener((Button.ClickEvent event) -> {
            resetToPublicationsNames();
        });

        Button applyFilters = new Button("Update");
        applyFilters.setDescription("Update disease groups with the selected names");
        applyFilters.setStyleName(ValoTheme.BUTTON_TINY);

        btnLayout.addComponent(applyFilters);
        applyFilters.addClickListener((Button.ClickEvent event) -> {
            updateGroups();
        });

        popupBodyLayout.addComponent(btnLayout);
        popupBodyLayout.setComponentAlignment(btnLayout, Alignment.MIDDLE_RIGHT);
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
        Map<String, ComboBox> diseaseGroupNameToListMap = new LinkedHashMap<>();
        for (String diseaseGroupName : diseaseCategory.getDiseaseSubGroups().keySet()) {
            VerticalLayout label = generateLabel(diseaseGroupName, diseaseCategory.getDiseaseStyleName());
            diseaseNamesUpdateContainer.addComponent(label, col, row);
            ComboBox list = generateLabelList(diseaseCategory, diseaseGroupName);
            diseaseNamesUpdateContainer.addComponent(list, col + 1, row);
            diseaseGroupNameToListMap.put(diseaseGroupName, list);

            col = 0;
            row++;

            VerticalLayout spacer1 = new VerticalLayout();
            spacer1.setHeight(2,Unit.PIXELS);
            spacer1.setWidth(10,Unit.PIXELS);
            spacer1.setStyleName("whitelayout");
            diseaseNamesUpdateContainer.addComponent(spacer1, col, row);
            VerticalLayout spacer2 = new VerticalLayout();
            spacer2.setHeight(2,Unit.PIXELS);
            spacer2.setWidth(10,Unit.PIXELS);
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
        rotateContainer.setWidth(widthCalc ,Unit.PIXELS);
        rotateContainer.setHeight(20, Unit.PIXELS);
        diseaseLabelContainer.addComponent(rotateContainer);
        rotateContainer.setStyleName(diseaseCategory.getDiseaseStyleName());
        rotateContainer.addStyleName("rotateheader");
        rotateContainer.addComponent(label);
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthUndefined();
        layout.setSpacing(true);
        layout.addComponent(diseaseLabelContainer);
        layout.addComponent(diseaseNamesUpdateContainer);

        return layout;
    }

    private VerticalLayout generateLabel(String strLabel, String diseaseStyle) {
        DiseaseGroupLabel container = new DiseaseGroupLabel(strLabel, diseaseStyle);
        return container;
    }

    private ComboBox generateLabelList(DiseaseCategoryObject diseaseCategory, String diseaseSubGroup) {
        final ComboBox diseaseGroupsList = new ComboBox();
        diseaseGroupsList.setStyleName(ValoTheme.COMBOBOX_TINY);
        diseaseGroupsList.setStyleName(ValoTheme.COMBOBOX_SMALL);
        diseaseGroupsList.setNullSelectionAllowed(false);
        diseaseGroupsList.setImmediate(true);
        diseaseGroupsList.setNewItemsAllowed(true);
        diseaseGroupsList.setWidth(99, Unit.PERCENTAGE);
        diseaseGroupsList.setInputPrompt("Select or enter new disease group name");
        diseaseGroupsList.setPageLength(500);
        String defaultValue = diseaseCategory.getDiseaseSubGroups().get(diseaseSubGroup);
        diseaseGroupsList.setData(defaultValue);

        for (String name : diseaseCategory.getDiseaseSubGroups().keySet()) {
            diseaseGroupsList.addItem(name);
            if (!captionAstrMap.containsKey(name)) {
                captionAstrMap.put(name, 0);
            }

        }
        diseaseGroupsList.setNewItemHandler((String newItemCaption) -> {
            String ast = "";
            if (!newItemCaption.contains("*")) {
                ast = "*";
            }
            diseaseGroupsList.addItem(newItemCaption + ast);
            diseaseGroupsList.select(newItemCaption + ast);
            captionAstrMap.put(newItemCaption + ast, 1);
        });
        diseaseGroupsList.setHeight(24,Unit.PIXELS);
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
    
    private void updateAllList(String itemId, String itemCap){
    for (String diseaseKey : diseaseGroupsSelectionListMap.keySet()) {
                Map<String, ComboBox> diseaseGroupNameToListMap = diseaseGroupsSelectionListMap.get(diseaseKey);
                for (String key : diseaseGroupNameToListMap.keySet()) {
                    ComboBox list = diseaseGroupNameToListMap.get(key);
                    list.setItemCaption(itemId,itemCap);
                }
            }
    
    }

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
                    list.setValue(null);
                }
            }
        } else {
            Map<String, ComboBox> diseaseGroupNameToListMap = diseaseGroupsSelectionListMap.get(selectedDiseaseKey);
            for (String key : diseaseGroupNameToListMap.keySet()) {
                ComboBox list = diseaseGroupNameToListMap.get(key);
                list.setValue(null);

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
                    selection = list.getValue().toString().trim();
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

}
