package no.uib.probe.csf.pr.touch.view.components.datasetfilters;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.view.core.ImageContainerBtn;
import no.uib.probe.csf.pr.touch.view.core.InformationButton;
import no.uib.probe.csf.pr.touch.view.core.PopupWindow;

/**
 *
 * @author Yehia Farag
 *
 * This class allow the users to switch comparisons
 */
public abstract class GroupSwichBtn extends ImageContainerBtn {
    
    private final PopupWindow popupWindow;
    private final VerticalLayout popupBodyLayout;
    private final Set<QuantDiseaseGroupsComparison> selectedComparisonList;
    private final Map<QuantDiseaseGroupsComparison, QuantDiseaseGroupsComparison> equalComparisonMap;
    private final GridLayout table;
    private final Label headerI, headerII;
    private final ArrayList<QuantDiseaseGroupsComparison> updatedComparisonList;
    private final HorizontalLayout btnWrapper;
    private final Panel tablePanelWrapper;
    private final HorizontalLayout topLayout;
    private final LayoutEvents.LayoutClickListener switchListener;
    
    private final int screenWidth = Math.min(Page.getCurrent().getBrowserWindowWidth(), 1000);
    private final int screenHeight = Math.min(Page.getCurrent().getBrowserWindowHeight(), 800);

    /**
     * on click method used to update the selection comparison list and view the
     * pop up window
     */
    @Override
    public void onClick() {
        selectedComparisonList.clear();
        selectedComparisonList.addAll(getUpdatedComparsionList());
        equalComparisonMap.clear();
        equalComparisonMap.putAll(getEqualComparsionMap());
        if (selectedComparisonList.isEmpty()) {
            return;
        }
        
        updateSelectionList();
        popupWindow.setVisible(true);
        
    }

    /**
     *
     */
    public GroupSwichBtn() {
        this.setHeight(40, Unit.PIXELS);
        this.setWidth(40, Unit.PIXELS);
        this.updateIcon(new ThemeResource("img/flip-v-updated.png"));
        this.setEnabled(true);
        this.setReadOnly(false);
        this.addStyleName("pointer");
        this.addStyleName("midimg");
        this.setDescription("Switch protein groups");

        //init data structure
        updatedComparisonList = new ArrayList<>();
        this.selectedComparisonList = new LinkedHashSet<>();
        this.equalComparisonMap = new HashMap<>();

        //init popup window 
        VerticalLayout frame = new VerticalLayout();
        frame.setWidth(screenWidth - 10, Unit.PIXELS);
        frame.setSpacing(true);
        this.popupBodyLayout = new VerticalLayout();
        frame.addComponent(popupBodyLayout);
        
        popupBodyLayout.addStyleName("roundedborder");
        popupBodyLayout.addStyleName("padding20");
        popupBodyLayout.addStyleName("whitelayout");
        
        popupBodyLayout.setWidth(100, Unit.PERCENTAGE);
//        popupBodyLayout.setHeight(screenHeight - 300, Unit.PIXELS);
        popupBodyLayout.setSpacing(true);
        popupBodyLayout.setMargin(true);
        
        popupWindow = new PopupWindow(frame, "Switch Disease Groups") {
            @Override
            public void close() {
                
                popupWindow.setVisible(false);//                Quant_Central_Manager.setDiseaseGroupsComparisonSelection(new LinkedHashSet<QuantDiseaseGroupsComparison>(updatedComparisonList));
            }
        };
        popupWindow.setWidth(screenWidth, Unit.PIXELS);
        //init top layout
        topLayout = new HorizontalLayout();
        topLayout.setWidth(100, Unit.PERCENTAGE);
        topLayout.setHeight(30, Unit.PIXELS);
        topLayout.setSpacing(true);
        
        popupBodyLayout.addComponent(topLayout);
        headerI = new Label("Numerator");
        headerI.setStyleName(ValoTheme.LABEL_SMALL);
        headerI.addStyleName(ValoTheme.LABEL_BOLD);
//        headerI.setWidth(100, Unit.PERCENTAGE);
//        headerI.setContentMode(ContentMode.HTML);
        topLayout.addComponent(headerI);
        topLayout.setComponentAlignment(headerI, Alignment.TOP_CENTER);
        
        VerticalLayout spacer = new VerticalLayout();
        spacer.setSizeFull();
        topLayout.addComponent(spacer);
        topLayout.setSpacing(true);
        
        headerII = new Label("Denominator");
        headerII.setStyleName(ValoTheme.LABEL_SMALL);
        headerII.addStyleName(ValoTheme.LABEL_BOLD);
        headerII.addStyleName("paddingleft16");
//        headerII.setWidth(100, Unit.PERCENTAGE);
        topLayout.addComponent(headerII);
        topLayout.setComponentAlignment(headerII, Alignment.TOP_CENTER);

//        init table
        table = new GridLayout();
        table.setColumnExpandRatio(0, 1);
        table.setColumnExpandRatio(1, 1);
        table.setColumnExpandRatio(2, 1);
        table.setWidth(100, Unit.PERCENTAGE);
        table.setSpacing(true);
        table.setColumns(3);
        table.setRows(1000);
        table.setHeightUndefined();
        table.setHideEmptyRowsAndColumns(true);
        tablePanelWrapper = new Panel();
        tablePanelWrapper.setSizeFull();
        
        tablePanelWrapper.setContent(table);
        tablePanelWrapper.addStyleName(ValoTheme.PANEL_BORDERLESS);
        popupBodyLayout.addComponent(tablePanelWrapper);

        //init bottom layout
//        btnWrapper.setMargin(true);
//        popupBodyLayout.addComponent(btnWrapper);
//        popupBodyLayout.setComponentAlignment(btnWrapper, Alignment.BOTTOM_CENTER);
        HorizontalLayout btnsFrame = new HorizontalLayout();
        btnsFrame.setWidth(100, Unit.PERCENTAGE);
        btnsFrame.addStyleName("roundedborder");
        btnsFrame.addStyleName("padding20");
        btnsFrame.addStyleName("margintop");
        btnsFrame.addStyleName("whitelayout");
        frame.addComponent(btnsFrame);
        
        HorizontalLayout leftsideWrapper = new HorizontalLayout();
        btnsFrame.addComponent(leftsideWrapper);
        btnsFrame.setComponentAlignment(leftsideWrapper, Alignment.TOP_LEFT);
        leftsideWrapper.setSpacing(true);
        
        InformationButton info = new InformationButton("info", true);
        leftsideWrapper.addComponent(info);
        
        Button applyFilters = new Button("Apply");
        applyFilters.setDescription("Apply the selected filters");
        applyFilters.setStyleName(ValoTheme.BUTTON_TINY);
//        applyFilters.setWidth(76, Unit.PIXELS);
//        applyFilters.setHeight(25, Unit.PIXELS);

        applyFilters.addClickListener((Button.ClickEvent event) -> {
            GroupSwichBtn.this.updateComparisons(new LinkedHashSet<>(updatedComparisonList));
            popupWindow.close();
        });
        btnWrapper = new HorizontalLayout();
        btnWrapper.setWidth(100, Unit.PERCENTAGE);
//        btnWrapper.setHeight(100, Unit.PIXELS);
        btnWrapper.addComponent(applyFilters);
        btnWrapper.setComponentAlignment(applyFilters, Alignment.TOP_RIGHT);
        
        btnsFrame.addComponent(btnWrapper);
        
        this.switchListener = GroupSwichBtn.this::switchClick;
        
    }
    
    private void switchClick(LayoutEvents.LayoutClickEvent event) {
        if (event.getComponent() instanceof VerticalLayout) {
            VerticalLayout switchBtn = (VerticalLayout) event.getComponent();
            if (switchBtn.getStyleName().contains("switchbtnselected")) {
                switchBtn.removeStyleName("switchbtnselected");
            } else {
                switchBtn.addStyleName("switchbtnselected");
            }
            int row = (Integer) switchBtn.getData();
            Label labelI = (Label) table.getComponent(0, row);
            Label labelII = (Label) table.getComponent(2, row);
            table.removeComponent(labelI);
            table.removeComponent(labelII);
            table.addComponent(labelI, 2, row);
            table.addComponent(labelII, 0, row);            
            QuantDiseaseGroupsComparison comp = equalComparisonMap.get(updatedComparisonList.get(row));
//            comp.switchComparison();
            updatedComparisonList.set(row, comp);
        }
        
    }

    /**
     * Get updated comparison list from the central selection manager to update
     * the comparisons panel with the latest selected comparisons
     *
     * @return Set of selected comparisons
     */
    public abstract Set<QuantDiseaseGroupsComparison> getUpdatedComparsionList();

    /**
     * Get updated comparison list from the central selection manager to update
     * the comparisons panel with the latest selected comparisons
     *
     * @return Set of selected comparisons
     */
    public abstract Map<QuantDiseaseGroupsComparison, QuantDiseaseGroupsComparison> getEqualComparsionMap();
    
    private void updateSelectionList() {
        table.removeAllComponents();
        updatedComparisonList.clear();
        int row = 0;
        setWindowHight(selectedComparisonList.size());
        for (QuantDiseaseGroupsComparison comparison : selectedComparisonList) {
            
            updatedComparisonList.add(comparison);
            if (!comparison.isSwitchable()) {
                continue;
            }
            String header = comparison.getComparisonHeader();
            String updatedHeaderI = header.split(" / ")[0].split("__")[0];
            String updatedHeaderII = header.split(" / ")[1].split("__")[0];
            String diseaseColor = comparison.getDiseaseCategoryColor();
            Label labelI = new Label("<font  style='!important; color:" + diseaseColor + "'>" + updatedHeaderI + "</font>");
            labelI.setStyleName(ValoTheme.LABEL_SMALL);
            labelI.addStyleName("bottomborder");
            labelI.setDescription(comparison.getComparisonFullName().split(" / ")[0].split("__")[0]);
            Label labelII = new Label("<font  style='color:" + diseaseColor + "'>" + updatedHeaderII + "</font>");
            labelII.setStyleName(ValoTheme.LABEL_SMALL);
            labelII.addStyleName("bottomborder");
            labelII.setDescription(comparison.getComparisonFullName().split(" / ")[1].split("__")[0]);
            labelI.setContentMode(ContentMode.HTML);
            labelII.setContentMode(ContentMode.HTML);
            VerticalLayout switchBtn = swichIconGenerator();
            table.addComponent(labelI, 0, row);
            table.addComponent(switchBtn, 1, row);
            switchBtn.setData(row);
            table.setComponentAlignment(switchBtn, Alignment.MIDDLE_CENTER);
            table.addComponent(labelII, 2, row++);            
            table.setComponentAlignment(labelII, Alignment.TOP_RIGHT);
            
        }
        topLayout.setExpandRatio(headerI, table.getColumnExpandRatio(0));
        topLayout.setExpandRatio(topLayout.getComponent(1), table.getColumnExpandRatio(1));
        topLayout.setExpandRatio(headerII, table.getColumnExpandRatio(2));
        
    }
    
    private void setWindowHight(int itemsNumber) {
        int itemH = (27 * itemsNumber);
        int height = Math.min(screenHeight - 230, itemH);
        tablePanelWrapper.setHeight(height, Unit.PIXELS);
        popupWindow.setHeight(tablePanelWrapper.getHeight() + 230, Unit.PIXELS);
        
    }
    
    private VerticalLayout swichIconGenerator() {
        
        VerticalLayout switchBtn = new VerticalLayout();
        switchBtn.setStyleName("switchbtn");
        switchBtn.setDescription("Click to switch groups");
        switchBtn.setWidth(50, Unit.PIXELS);
        switchBtn.setHeight(25, Unit.PIXELS);
        switchBtn.addLayoutClickListener(switchListener);
        return switchBtn;
        
    }

    /**
     * Update the selection manager with the updated list
     *
     * @param updatedComparisonList
     */
    public abstract void updateComparisons(LinkedHashSet<QuantDiseaseGroupsComparison> updatedComparisonList);
    
}
