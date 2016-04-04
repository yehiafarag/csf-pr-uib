/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters;

import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import java.util.List;
import java.util.Set;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
import probe.com.model.beans.quant.QuantProtein;
import probe.com.selectionmanager.QuantCentralManager;

/**
 *
 * @author yfa041
 */
public class GroupSwichBtn extends VerticalLayout implements LayoutEvents.LayoutClickListener, Property.ValueChangeListener {

    private final Window popupWindow;
    private final VerticalLayout popupBody;
    private final OptionGroup comparisonList;
    private Set<QuantDiseaseGroupsComparison> selectedComparisonList;
    private final QuantCentralManager Quant_Central_Manager;
    private final List<QuantProtein> searchQuantificationProtList;

    public void setSelectedComparisonList(Set<QuantDiseaseGroupsComparison> selectedComparisonList) {
        this.selectedComparisonList = selectedComparisonList;
    }

    public GroupSwichBtn(QuantCentralManager Quant_Central_Manager, List<QuantProtein> searchQuantificationProtList) {
        this.Quant_Central_Manager = Quant_Central_Manager;
        this.searchQuantificationProtList = searchQuantificationProtList;
        this.setWidth("23px");
        this.setHeight("23px");
        this.addLayoutClickListener(GroupSwichBtn.this);
        this.setStyleName(Reindeer.LAYOUT_BLACK);

        popupBody = new VerticalLayout();

        popupBody.setHeightUndefined();
        popupBody.setStyleName(Reindeer.LAYOUT_WHITE);

        popupWindow = new Window() {

            @Override
            public void close() {
                popupWindow.setVisible(false);
            }

        };
        popupWindow.setCaption("Swich  A / B  -->  B / A ");
        popupWindow.setContent(popupBody);
        popupWindow.setWindowMode(WindowMode.NORMAL);

        popupWindow.setVisible(false);
        popupWindow.setResizable(false);
        popupWindow.setClosable(false);
        popupWindow.setStyleName(Reindeer.WINDOW_LIGHT);
        popupWindow.setModal(true);
        popupWindow.setDraggable(true);

        UI.getCurrent().addWindow(popupWindow);
        popupWindow.center();

        popupWindow.setCaptionAsHtml(true);
        popupWindow.setClosable(true);

        popupBody.setMargin(true);
        popupBody.setSpacing(true);

        comparisonList = new OptionGroup(null);

        comparisonList.setMultiSelect(true);
        comparisonList.setNullSelectionAllowed(true);
        comparisonList.setHtmlContentAllowed(true);
        comparisonList.setImmediate(true);
        comparisonList.setWidth("80%");
        comparisonList.setHeight("80%");
        popupBody.addComponent(comparisonList);

        Button applyFilters = new Button("Apply");
        applyFilters.setDescription("Apply the selected filters");
        applyFilters.setPrimaryStyleName("resetbtn");
        applyFilters.setWidth("50px");
        applyFilters.setHeight("24px");

        popupBody.addComponent(applyFilters);
        popupBody.setComponentAlignment(applyFilters, Alignment.TOP_RIGHT);
        applyFilters.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                popupWindow.close();
            }
        });

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        updateSelectionList();
        popupWindow.setVisible(true);
    }

    private void updateSelectionList() {
        comparisonList.removeValueChangeListener(GroupSwichBtn.this);
        comparisonList.removeAllItems();
        selectedComparisonList = this.Quant_Central_Manager.getUpdatedSelectedDiseaseGroupsComparisonListProteins(searchQuantificationProtList);
        this.setSelectedComparisonList(selectedComparisonList);

        if (selectedComparisonList != null) {
            setWindowHight(selectedComparisonList.size());
            for (QuantDiseaseGroupsComparison comparison : selectedComparisonList) {
                String header = comparison.getComparisonHeader();
                String updatedHeaderI = header.split(" / ")[0].split("\n")[0];
                String updatedHeaderII = header.split(" / ")[1].split("\n")[0];
                String diseaseColor = this.Quant_Central_Manager.getDiseaseHashedColor(header.split(" / ")[1].split("\n")[1]);

                String label = ("<font color='" + diseaseColor + "' style='font-weight: bold;!important'>" + updatedHeaderI + " / " + updatedHeaderII + "</font>");
                comparisonList.addItem(comparison.getComparisonHeader());
                comparisonList.setItemCaption(comparison.getComparisonHeader(), label);
            }
        }
        comparisonList.addValueChangeListener(GroupSwichBtn.this);
    }

    private void setWindowHight(int itemsNumber) {
        int itemH = (20 * itemsNumber) + 120;
        int itemW = 300;
        int height = Math.min(Page.getCurrent().getBrowserWindowHeight() * 90 / 100, itemH);
        int width = Math.min(Page.getCurrent().getBrowserWindowWidth() * 90 / 100, itemW);
        popupBody.setWidth((width) + "px");
        popupWindow.setWidth((width + 40) + "px");
        popupWindow.setHeight((height) + "px");

    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {

        String key = event.getProperty().getValue().toString().replace("[", "").replace("]", "");
        String header = comparisonList.getItemCaption(key);
        if (header == null || header.trim().equalsIgnoreCase("")) {
            return;
        }
        String updatedHeaderI = header.split("!important'>")[1].replace("</font>", "").split(" / ")[0];
        String updatedHeaderII = header.split("!important'>")[1].replace("</font>", "").split(" / ")[1];

        String toReplace = updatedHeaderI + " / " + updatedHeaderII;
        String replaceBy = updatedHeaderII + " / " + updatedHeaderI;
        header = header.replace(toReplace, replaceBy);
        System.out.println("at header ii  " + header);
        comparisonList.setItemCaption(key, header);
    }

}
