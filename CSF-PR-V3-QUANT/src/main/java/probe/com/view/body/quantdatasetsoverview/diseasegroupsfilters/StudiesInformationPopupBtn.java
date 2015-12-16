/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import probe.com.model.beans.quant.QuantDatasetObject;
import probe.com.model.beans.quant.QuantDiseaseGroupsComparison;
import probe.com.selectionmanager.CSFFilter;
import probe.com.selectionmanager.QuantCentralManager;
import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.peptidescontainer.popupcomponents.DatasetInformationOverviewLayout;

/**
 *
 * @author yfa041
 */
public class StudiesInformationPopupBtn extends Button implements Button.ClickListener, CSFFilter {

    private final Window popupWindow;
    private final StudyPopupLayout studiesPopupLayout;
    private final QuantCentralManager Quant_Central_Manager;
    private final Map<Integer, DatasetInformationOverviewLayout> datasetInfoLayoutDSIndexMap = new HashMap<Integer, DatasetInformationOverviewLayout>();

    public StudiesInformationPopupBtn(QuantCentralManager Quant_Central_Manager) {
        super("Study Information");
        this.Quant_Central_Manager = Quant_Central_Manager;
        this.setStyleName(Reindeer.BUTTON_LINK);
        this.setDescription("Show studies Information");
        this.addClickListener(StudiesInformationPopupBtn.this);
        this.studiesPopupLayout = new StudyPopupLayout();
        VerticalLayout popupBody = new VerticalLayout();
        popupBody.setWidth("100%");
        popupBody.addComponent(studiesPopupLayout);
        popupBody.setComponentAlignment(studiesPopupLayout, Alignment.TOP_CENTER);

        popupWindow = new Window() {
            @Override
            public void close() {
                popupWindow.setVisible(false);
            }
        };
        popupWindow.setContent(popupBody);
        popupWindow.setWindowMode(WindowMode.NORMAL);
        popupWindow.setWidth("95%");
        popupWindow.setHeight("95%");
        popupWindow.setVisible(false);
        popupWindow.setResizable(false);
        popupWindow.setClosable(false);
        popupWindow.setStyleName(Reindeer.WINDOW_LIGHT);
        popupWindow.setModal(true);
        popupWindow.setDraggable(false);
        popupWindow.center();
        int selectedDsNumber = 0;
        Set<QuantDatasetObject> dsObjects = new TreeSet<QuantDatasetObject>();
        if (Quant_Central_Manager.getSelectedDiseaseGroupsComparisonList() != null && !Quant_Central_Manager.getSelectedDiseaseGroupsComparisonList().isEmpty()) {
            Set<QuantDiseaseGroupsComparison> compSet = Quant_Central_Manager.getSelectedDiseaseGroupsComparisonList();
            for (QuantDiseaseGroupsComparison comp : compSet) {
                selectedDsNumber += comp.getDatasetIndexes().length;
                for (int dsId : comp.getDatasetIndexes()) {
                    DatasetInformationOverviewLayout datasetInfoLayout = new DatasetInformationOverviewLayout((Page.getCurrent().getBrowserWindowWidth() * 90 / 100));
                    datasetInfoLayout.updateDatasetForm(Quant_Central_Manager.getFullQuantDatasetMap().get(dsId));
                    datasetInfoLayout.setWidth("100%");
                    datasetInfoLayoutDSIndexMap.put(dsId, datasetInfoLayout);
                    dsObjects.add(Quant_Central_Manager.getFullQuantDatasetMap().get(dsId));

                }
            }

        } else {
            selectedDsNumber = Quant_Central_Manager.getFilteredDatasetsList().size();
            Map<Integer, QuantDatasetObject> fullDsMap = Quant_Central_Manager.getFilteredDatasetsList();
            for (QuantDatasetObject quantDs : fullDsMap.values()) {
                DatasetInformationOverviewLayout datasetInfoLayout = new DatasetInformationOverviewLayout((Page.getCurrent().getBrowserWindowWidth() * 90 / 100));
                datasetInfoLayout.updateDatasetForm(quantDs);
                datasetInfoLayout.setWidth("100%");
                datasetInfoLayoutDSIndexMap.put(quantDs.getDsKey(), datasetInfoLayout);
                dsObjects.add(quantDs);

            }
        }

        popupWindow.setCaption("&nbsp;&nbsp;Study Information (" + selectedDsNumber + ")");
        studiesPopupLayout.setInformationData(dsObjects);

        UI.getCurrent().addWindow(popupWindow);
        popupWindow.center();

        popupWindow.setCaptionAsHtml(true);
        popupWindow.setClosable(true);

        popupBody.setMargin(true);
        popupBody.setSpacing(true);
        Quant_Central_Manager.registerStudySelectionListener(StudiesInformationPopupBtn.this);

    }

    @Override
    public void buttonClick(ClickEvent event) {
        popupWindow.setVisible(true);
    }

    @Override
    public void selectionChanged(String type) {
        if (type.equalsIgnoreCase("Comparison_Selection")) {
            datasetInfoLayoutDSIndexMap.clear();

            Set<QuantDatasetObject> dsObjects = new TreeSet<QuantDatasetObject>();
            int selectedDsNumber = 0;
            if (Quant_Central_Manager.getSelectedDiseaseGroupsComparisonList() != null && !Quant_Central_Manager.getSelectedDiseaseGroupsComparisonList().isEmpty()) {
                Set<QuantDiseaseGroupsComparison> compSet = Quant_Central_Manager.getSelectedDiseaseGroupsComparisonList();
                for (QuantDiseaseGroupsComparison comp : compSet) {

                    selectedDsNumber += comp.getDatasetIndexes().length;
                    for (int dsId : comp.getDatasetIndexes()) {
                        DatasetInformationOverviewLayout datasetInfoLayout = new DatasetInformationOverviewLayout((Page.getCurrent().getBrowserWindowWidth() * 90 / 100));
                        datasetInfoLayout.updateDatasetForm(Quant_Central_Manager.getFullQuantDatasetMap().get(dsId));
                        datasetInfoLayout.setWidth("100%");
                        datasetInfoLayoutDSIndexMap.put(dsId, datasetInfoLayout);
                        dsObjects.add(Quant_Central_Manager.getFullQuantDatasetMap().get(dsId));

                    }
                }

            } else {
                selectedDsNumber = Quant_Central_Manager.getFilteredDatasetsList().size();
                Map<Integer, QuantDatasetObject> fullDsMap = Quant_Central_Manager.getFilteredDatasetsList();
                for (QuantDatasetObject quantDs : fullDsMap.values()) {
                    DatasetInformationOverviewLayout datasetInfoLayout = new DatasetInformationOverviewLayout((Page.getCurrent().getBrowserWindowWidth() * 90 / 100));
                    datasetInfoLayout.updateDatasetForm(quantDs);
                    datasetInfoLayout.setWidth("100%");
                    datasetInfoLayoutDSIndexMap.put(quantDs.getDsKey(), datasetInfoLayout);
                    dsObjects.add(quantDs);

                }
            }
            popupWindow.setCaption("&nbsp;&nbsp;Study Information (" + selectedDsNumber + ")");
            studiesPopupLayout.setInformationData(dsObjects);

        }
    }

    @Override
    public String getFilterId() {
        return this.getClass().getName();
    }

    @Override
    public void removeFilterValue(String value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    class StudyPopupLayout extends VerticalLayout implements LayoutEvents.LayoutClickListener {

        private final GridLayout topLayout;
        private final VerticalLayout bottomLayout;
        private final VerticalLayout datasetsInformationContainer;
        private VerticalLayout lastSelectedBtn;

        public void setInformationData(Set<QuantDatasetObject> dsObjects) {
            topLayout.removeAllComponents();
            int rowNumb = Math.max(1, ((dsObjects.size() / topLayout.getColumns()) + 1));
            topLayout.setRows(rowNumb);
            if (rowNumb == 1) {
                topLayout.setWidthUndefined();
            } else {
                topLayout.setWidth("100%");
            }

            int colcounter = 0;
            int rowcounter = 0;
            for (QuantDatasetObject quantDS : dsObjects) {
                VerticalLayout btn = this.generateBtn(quantDS.getDsKey(), quantDS.getAuthor() + " (" + quantDS.getYear() + ")");
                topLayout.addComponent(btn, colcounter++, rowcounter);
                if (colcounter >= topLayout.getColumns()) {
                    colcounter = 0;
                    rowcounter++;

                }

            }

//            int subWidth = (int) ((float) bottomLayout.getWidth() - 30);
//            this.initPopupLayoutLayout(cp, subWidth);
            this.selectStudyBtn((VerticalLayout) topLayout.getComponent(0, 0));

        }
        

        public StudyPopupLayout() {
            this.setWidth("100%");
            this.setStyleName(Reindeer.LAYOUT_BLACK);
            this.setHeightUndefined();
            this.setStyleName(Reindeer.LAYOUT_WHITE);
            this.setMargin(false);
            this.setSpacing(true);
            int width = Page.getCurrent().getBrowserWindowWidth() * 90 / 100;

            int colNum = Math.max(1, width / 200);

            topLayout = new GridLayout();
            topLayout.setWidth("100%");
            topLayout.setColumns(colNum);
            topLayout.setStyleName(Reindeer.LAYOUT_WHITE);

            topLayout.setHeightUndefined();
            topLayout.setSpacing(true);

            bottomLayout = new VerticalLayout();
            bottomLayout.setSpacing(true);
            this.addComponent(topLayout);
            bottomLayout.setMargin(new MarginInfo(true, false, false, false));
            this.addComponent(bottomLayout);
            this.setComponentAlignment(bottomLayout,Alignment.BOTTOM_CENTER);
            bottomLayout.setWidth("100%");
            bottomLayout.setHeight("500px");
            
            bottomLayout.setStyleName(Reindeer.LAYOUT_WHITE);
            datasetsInformationContainer = this.initInformationContainer(width);
            bottomLayout.addComponent(datasetsInformationContainer);

        }

        private VerticalLayout initInformationContainer(int width) {
            VerticalLayout generatedPeptidesInformationContainer = new VerticalLayout();
            generatedPeptidesInformationContainer.setWidth(width - 10 + "px");
            generatedPeptidesInformationContainer.setHeightUndefined();
            return generatedPeptidesInformationContainer;

        }

        @Override
        public void layoutClick(LayoutEvents.LayoutClickEvent event) {
            if (lastSelectedBtn != null) {
                lastSelectedBtn.setStyleName("tabbtn");
            }
            VerticalLayout selectedBtn;
            if (event.getClickedComponent() == null) {
                selectedBtn = (VerticalLayout) event.getComponent();

            } else if (event.getClickedComponent() instanceof Label) {
                selectedBtn = (VerticalLayout) event.getClickedComponent().getParent();
            } else {
                selectedBtn = (VerticalLayout) event.getClickedComponent();
            }
            if (selectedBtn == lastSelectedBtn) {
                bottomLayout.setVisible(false);
                lastSelectedBtn = null;
                return;
            }
            this.selectStudyBtn(selectedBtn);
        }

        private void selectStudyBtn(VerticalLayout selectedBtn) {

            lastSelectedBtn = selectedBtn;
            lastSelectedBtn.setStyleName("selectedtabbtn");
            updateDatasetInfoLayout((Integer) lastSelectedBtn.getData());
            bottomLayout.setVisible(true);

        }

        private void updateDatasetInfoLayout(int dsKey) {
            datasetsInformationContainer.removeAllComponents();
            for (int key : datasetInfoLayoutDSIndexMap.keySet()) {
                if (key == (dsKey)) {
                    datasetsInformationContainer.addComponent(datasetInfoLayoutDSIndexMap.get(key));
                    datasetsInformationContainer.setComponentAlignment(datasetInfoLayoutDSIndexMap.get(key), Alignment.BOTTOM_RIGHT);
                    break;
                }
            }
        }

        private VerticalLayout generateBtn(int dsKey, String btnName) {
            VerticalLayout btn = new VerticalLayout();
            btn.addLayoutClickListener(this);
            btn.setHeight("50px");
            btn.setWidth("200px");
            Label btnLabel = new Label(btnName);
            btn.addComponent(btnLabel);
            btn.setComponentAlignment(btnLabel, Alignment.MIDDLE_CENTER);
            btn.setStyleName("tabbtn");
            btn.setData(dsKey);

            return btn;
        }
    }

}
