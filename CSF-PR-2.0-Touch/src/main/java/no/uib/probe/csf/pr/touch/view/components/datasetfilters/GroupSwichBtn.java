package no.uib.probe.csf.pr.touch.view.components.datasetfilters;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.view.core.ImageContainerBtn;

/**
 *
 * @author Yehia Farag
 *
 * This class allow the users to switch comparisons
 */
public abstract class GroupSwichBtn extends ImageContainerBtn {

    private final Window popupWindow;
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
        this.setHeight(45, Unit.PIXELS);
        this.setWidth(45, Unit.PIXELS);
        this.updateIcon(new ThemeResource("img/flip-v-updated.png"));
        this.setEnabled(true);
        this.setReadOnly(false);
        this.addStyleName("smallimg");
        this.setDescription("Switch protein groups");

        //init data structure
        updatedComparisonList = new ArrayList<>();
        this.selectedComparisonList = new LinkedHashSet<>();
        this.equalComparisonMap = new HashMap<>();

        //init popup window 
        int width = Math.min(Page.getCurrent().getBrowserWindowWidth(), 800);
        this.popupBodyLayout = new VerticalLayout();
        VerticalLayout popupBody = new VerticalLayout();
        popupBody.setWidth(100, Unit.PERCENTAGE);
        popupBody.setHeight(100, Unit.PERCENTAGE);
        popupWindow = new Window() {
            @Override
            public void close() {

                popupWindow.setVisible(false);//                Quant_Central_Manager.setDiseaseGroupsComparisonSelection(new LinkedHashSet<QuantDiseaseGroupsComparison>(updatedComparisonList));
            }
        };
        popupWindow.setWidth(width, Unit.PIXELS);
        popupWindow.setContent(popupBody);
        popupBody.addComponent(popupBodyLayout);
        popupBody.setComponentAlignment(popupBodyLayout, Alignment.MIDDLE_CENTER);

        popupWindow.setVisible(false);
        popupWindow.setResizable(false);
        popupWindow.setClosable(true);
        popupWindow.setModal(true);
        popupWindow.setDraggable(false);
        popupWindow.setCaption("<font color='gray' style='font-weight: bold;!important'>&nbsp;&nbsp;Switch  disease groups</font>");
        popupWindow.setCaptionAsHtml(true);
        popupBodyLayout.setStyleName("whitelayout");
        popupBodyLayout.setHeightUndefined();
        popupWindow.setWindowMode(WindowMode.NORMAL);
        UI.getCurrent().addWindow(popupWindow);
        popupWindow.center();

        //init top layout
        topLayout = new HorizontalLayout();
        topLayout.setWidth(width - 20, Unit.PIXELS);
        topLayout.setHeight(30, Unit.PIXELS);
        topLayout.setSpacing(true);

        popupBodyLayout.addComponent(topLayout);
        headerI = new Label("<center><b>Numerator</b></center>");
        headerI.setContentMode(ContentMode.HTML);
        topLayout.addComponent(headerI);

        VerticalLayout spacer = new VerticalLayout();
        spacer.setSizeFull();
        topLayout.addComponent(spacer);

        headerII = new Label("<center><b>Denominator</b></center>");
        headerII.setContentMode(ContentMode.HTML);
        topLayout.addComponent(headerII);

//        init table
        table = new GridLayout();
        table.setColumnExpandRatio(0, 1);
        table.setColumnExpandRatio(1, 1);
        table.setColumnExpandRatio(2, 1);
        table.setWidth(width - 20, Unit.PIXELS);
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
        btnWrapper = new HorizontalLayout();
        btnWrapper.setWidth(100, Unit.PERCENTAGE);
        btnWrapper.setHeight(30, Unit.PIXELS);
        btnWrapper.setMargin(true);

        popupBodyLayout.addComponent(btnWrapper);
        popupBodyLayout.setComponentAlignment(btnWrapper, Alignment.BOTTOM_CENTER);

        Button applyFilters = new Button("Apply");
        applyFilters.setDescription("Apply the selected filters");
        applyFilters.setStyleName(ValoTheme.BUTTON_TINY);
        applyFilters.setWidth(76, Unit.PIXELS);
        applyFilters.setHeight(25, Unit.PIXELS);

        applyFilters.addClickListener((Button.ClickEvent event) -> {
            GroupSwichBtn.this.updateComparisons(new LinkedHashSet<>(updatedComparisonList));
            popupWindow.close();
        });
        btnWrapper.addComponent(applyFilters);
        btnWrapper.setComponentAlignment(applyFilters, Alignment.TOP_RIGHT);

        this.switchListener = GroupSwichBtn.this::switchClick;

    }

    private void switchClick(LayoutEvents.LayoutClickEvent event) {
        if (event.getComponent() instanceof VerticalLayout) {
            VerticalLayout switchBtn = (VerticalLayout) event.getComponent();
            int row = (Integer) switchBtn.getData();
            Label labelI = (Label) table.getComponent(0, row);
            Label labelII = (Label) table.getComponent(2, row);
            table.removeComponent(labelI);
            table.removeComponent(labelII);
            table.addComponent(labelII, 0, row);
            table.addComponent(labelI, 2, row);
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
            Label labelI = new Label("<center><b  style='font-weight: bold;!important; color:" + diseaseColor + "'>" + updatedHeaderI + "</b></center>");
            labelI.setDescription(comparison.getComparisonFullName().split(" / ")[0].split("__")[0]);
            Label labelII = new Label("<center><b  style='font-weight: bold;!important; color:" + diseaseColor + "'>" + updatedHeaderII + "</b></center>");
            labelII.setDescription(comparison.getComparisonFullName().split(" / ")[1].split("__")[0]);
            labelI.setContentMode(ContentMode.HTML);
            labelII.setContentMode(ContentMode.HTML);
            VerticalLayout switchBtn = swichIconGenerator();
            table.addComponent(labelI, 0, row);
            table.addComponent(switchBtn, 1, row);
            switchBtn.setData(row);
            table.setComponentAlignment(switchBtn, Alignment.MIDDLE_CENTER);
            table.addComponent(labelII, 2, row++);

        }
        topLayout.setExpandRatio(headerI, table.getColumnExpandRatio(0));
        topLayout.setExpandRatio(topLayout.getComponent(1), table.getColumnExpandRatio(1));
        topLayout.setExpandRatio(headerII, table.getColumnExpandRatio(2));

    }

    private void setWindowHight(int itemsNumber) {
        int itemH = (27 * itemsNumber);
        int height = Math.min(Page.getCurrent().getBrowserWindowHeight() - 140, itemH);
        tablePanelWrapper.setHeight(height, Unit.PIXELS);
        popupWindow.setHeight(height + 140, Unit.PIXELS);

    }

    private VerticalLayout swichIconGenerator() {

        VerticalLayout switchBtn = new VerticalLayout();
        switchBtn.setStyleName("switchbtn");
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
