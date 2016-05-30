/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.components.datasetfilters;

import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.logic.beans.QuantProtein;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFPR_Central_Manager;
//import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
//import probe.com.model.beans.quant.QuantProtein;
//import probe.com.selectionmanager.QuantCentralManager;

/**
 *
 * @author yfa041
 */
public class GroupSwichBtn extends VerticalLayout implements LayoutEvents.LayoutClickListener, Property.ValueChangeListener {

    private final Window popupWindow;
    private final VerticalLayout popupBody;
//    private final OptionGroup comparisonList;
    private Set<QuantDiseaseGroupsComparison> selectedComparisonList;
    private final GridLayout table;
    private final CSFPR_Central_Manager Quant_Central_Manager;
    private final List<QuantProtein> searchQuantificationProtList;
    private final Label headerI, headerII;
    private final ArrayList<QuantDiseaseGroupsComparison> updatedComparisonList;
    private final HorizontalLayout btnWrapper;

    public void setSelectedComparisonList(Set<QuantDiseaseGroupsComparison> selectedComparisonList) {
        this.selectedComparisonList = selectedComparisonList;
    }

    public GroupSwichBtn(final CSFPR_Central_Manager Quant_Central_Manager, List<QuantProtein> searchQuantificationProtList) {
        this.Quant_Central_Manager = Quant_Central_Manager;
        this.searchQuantificationProtList = searchQuantificationProtList;
        updatedComparisonList = new ArrayList<QuantDiseaseGroupsComparison>();
        this.setWidth("24px");
        this.setHeight("24px");
        this.setDescription("Switch protein groups");
        this.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                updateSelectionList();
                popupWindow.setVisible(true);
            }
        });
        this.setStyleName("switchicon");

        popupBody = new VerticalLayout();

        popupBody.setHeightUndefined();
        popupBody.setStyleName(Reindeer.LAYOUT_WHITE);

        popupWindow = new Window() {

            @Override
            public void close() {
                
                popupWindow.setVisible(false);
                 Quant_Central_Manager.setDiseaseGroupsComparisonSelection(new LinkedHashSet<QuantDiseaseGroupsComparison>(updatedComparisonList));
            }

        };
        popupWindow.setCaption("<font color='gray' style='font-weight: bold;!important'>&nbsp;&nbsp;Switch  disease groups</font>");
        popupWindow.setContent(popupBody);
        popupWindow.setWindowMode(WindowMode.NORMAL);

        popupWindow.setVisible(false);
        popupWindow.setResizable(false);
        popupWindow.setStyleName(Reindeer.WINDOW_LIGHT);
        popupWindow.setModal(true);
        popupWindow.setDraggable(true);

        UI.getCurrent().addWindow(popupWindow);
//        popupWindow.center();
//        popupWindow.setPositionX(popupWindow.getPositionX());
        popupWindow.setPositionY(100);

        popupWindow.setCaptionAsHtml(true);
        popupWindow.setClosable(true);

        popupBody.setMargin(true);
        popupBody.setSpacing(true);

        //init table
        table = new GridLayout();
        table.setStyleName("switchtable");
        table.setWidth("100%");
        table.setSpacing(true);
        table.setColumns(3);
        table.setRows(1000);
        table.setHeightUndefined();
        table.setHideEmptyRowsAndColumns(true);
        table.setMargin(new MarginInfo(false, false, true, false));

        headerI = new Label("<center><b>Numerator</b></center>");
        headerI.setWidth("100%");
        headerI.setContentMode(ContentMode.HTML);
        headerII = new Label("<center><b>Denominator</b></center>");
        headerII.setContentMode(ContentMode.HTML);
        headerII.setWidth("100%");

//        comparisonList = new OptionGroup(null);
//
//        comparisonList.setMultiSelect(true);
//        comparisonList.setNullSelectionAllowed(true);
//        comparisonList.setHtmlContentAllowed(true);
//        comparisonList.setImmediate(true);
//        comparisonList.setWidth("80%");
//        comparisonList.setHeight("80%");
        popupBody.addComponent(table);

        Button applyFilters = new Button("Apply");
        applyFilters.setDescription("Apply the selected filters");
        applyFilters.setPrimaryStyleName("resetbtn");
        applyFilters.setWidth("76px");
        applyFilters.setHeight("24px");

//        popupBody.addComponent(applyFilters);
//        popupBody.setComponentAlignment(applyFilters, Alignment.TOP_RIGHT);
        applyFilters.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                popupWindow.close();
            }
        });
        btnWrapper = new HorizontalLayout();
        btnWrapper.setWidth("100%");
        btnWrapper.setHeight("50px");
        btnWrapper.addComponent(applyFilters);
        btnWrapper.setComponentAlignment(applyFilters, Alignment.BOTTOM_CENTER);

        table.addLayoutClickListener(GroupSwichBtn.this);

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (event.getClickedComponent() instanceof VerticalLayout) {
            VerticalLayout switchBtn = (VerticalLayout) event.getClickedComponent();
            int row = (Integer) switchBtn.getData() ;
            Label labelI = (Label) table.getComponent(0, row+1);
            Label labelII = (Label) table.getComponent(2, row+1);
            table.removeComponent(labelI);
            table.removeComponent(labelII);
            table.addComponent(labelII, 0, row+1);
            table.setComponentAlignment(labelII, Alignment.MIDDLE_CENTER);
            table.addComponent(labelI, 2, row+1);
            table.setComponentAlignment(labelI, Alignment.MIDDLE_CENTER);
            QuantDiseaseGroupsComparison comp=  updatedComparisonList.get(row);
            comp.switchComparison();
            updatedComparisonList.set(row, comp);
        }

    }

    private void updateSelectionList() {
        table.removeAllComponents();
        updatedComparisonList.clear();
        selectedComparisonList = this.Quant_Central_Manager.getUpdatedSelectedDiseaseGroupsComparisonListProteins(searchQuantificationProtList);//
        int row = 1;
        if (selectedComparisonList != null) {
            table.addComponent(headerI, 0, 0);
            table.setComponentAlignment(headerI, Alignment.MIDDLE_CENTER);
            table.addComponent(headerII, 2, 0);
            table.setComponentAlignment(headerII, Alignment.MIDDLE_CENTER);
            setWindowHight(selectedComparisonList.size());
            for (QuantDiseaseGroupsComparison comparison : selectedComparisonList) {
                updatedComparisonList.add(comparison);
               
                String header = comparison.getComparisonHeader();
                String updatedHeaderI = header.split(" / ")[0].split("\n")[0];
                String updatedHeaderII = header.split(" / ")[1].split("\n")[0];
                String diseaseColor = this.Quant_Central_Manager.getDiseaseHashedColor(header.split(" / ")[1].split("\n")[1]);
                Label labelI = new Label("<center><font color='" + diseaseColor + "' style='font-weight: bold;!important'>" + updatedHeaderI + "</font></center>");
                Label labelII = new Label("<center><font color='" + diseaseColor + "' style='font-weight: bold;!important'>" + updatedHeaderII + "</font></center>");
                labelI.setContentMode(ContentMode.HTML);
                labelII.setContentMode(ContentMode.HTML);
                labelI.setWidth("100%");
                labelII.setWidth("100%");

                VerticalLayout switchBtn = swichIconGenerator();
                table.addComponent(labelI, 0, row);
                table.setComponentAlignment(labelI, Alignment.MIDDLE_CENTER);
                table.addComponent(switchBtn, 1, row);
                switchBtn.setData(row - 1);
                table.setComponentAlignment(switchBtn, Alignment.MIDDLE_CENTER);
                table.addComponent(labelII, 2, row++);
                table.setComponentAlignment(labelII, Alignment.MIDDLE_CENTER);

            }
            table.addComponent(btnWrapper, 2, row);
            table.setComponentAlignment(btnWrapper, Alignment.MIDDLE_CENTER);
        }
//        comparisonList.addValueChangeListener(GroupSwichBtn.this);
    }

    private void setWindowHight(int itemsNumber) {
        int itemH = (25 * itemsNumber) + 200;
        int itemW = 500;
        int browserW = Page.getCurrent().getBrowserWindowWidth();
        if (Page.getCurrent().getBrowserWindowHeight() * 90 / 100 > itemH) {
            popupWindow.setHeightUndefined();
        } else {
            int height = Math.min(Page.getCurrent().getBrowserWindowHeight() * 90 / 100, itemH);
            popupWindow.setHeight((height) + "px");
        }
        int width = Math.min(browserW * 90 / 100, itemW);
        popupBody.setWidth((width) + "px");
        popupWindow.setWidth((width + 20) + "px");

        int x = (browserW / 2) - (width / 2);
        popupWindow.setPositionX(x);

    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {

        String key = event.getProperty().getValue().toString().replace("[", "").replace("]", "");
//        String header = comparisonList.getItemCaption(key);
//        if (header == null || header.trim().equalsIgnoreCase("")) {
//            return;
//        }
//        String updatedHeaderI = header.split("!important'>")[1].replace("</font>", "").split(" / ")[0];
//        String updatedHeaderII = header.split("!important'>")[1].replace("</font>", "").split(" / ")[1];
//
//        String toReplace = updatedHeaderI + " / " + updatedHeaderII;
//        String replaceBy = updatedHeaderII + " / " + updatedHeaderI;
//        header = header.replace(toReplace, replaceBy);
//        System.out.println("at header ii  " + header);
//        comparisonList.setItemCaption(key, header);
    }

    private VerticalLayout swichIconGenerator() {

        VerticalLayout switchBtn = new VerticalLayout();
        switchBtn.setStyleName("switchbtn");
        switchBtn.setWidth("50px");
        switchBtn.setHeight("25px");
        return switchBtn;

    }

}
