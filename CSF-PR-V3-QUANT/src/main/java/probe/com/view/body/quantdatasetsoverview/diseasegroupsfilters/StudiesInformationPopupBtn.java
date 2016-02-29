/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters;

import com.vaadin.server.Page;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
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
import probe.com.view.core.StudyPopupLayout;

/**
 *
 * @author Yehia Farag
 */
public class StudiesInformationPopupBtn extends Button implements Button.ClickListener, CSFFilter {

    private final Window popupWindow;
    private final StudyPopupLayout studiesPopupLayout;
    private final QuantCentralManager Quant_Central_Manager;
    private final Map<Integer, DatasetInformationOverviewLayout> datasetInfoLayoutDSIndexMap = new HashMap<Integer, DatasetInformationOverviewLayout>();

    public StudiesInformationPopupBtn(QuantCentralManager Quant_Central_Manager) {
//        super("Study Information");
        this.Quant_Central_Manager = Quant_Central_Manager;
        this.setStyleName(Reindeer.BUTTON_LINK);
        this.addStyleName("studyinfo");
        this.setDescription("Show studies Information");
        this.addClickListener(StudiesInformationPopupBtn.this);
        int selectedDsNumber = 0;
        Set<QuantDatasetObject> dsObjects = new TreeSet<QuantDatasetObject>();
        if (Quant_Central_Manager.getSelectedDiseaseGroupsComparisonList() != null && !Quant_Central_Manager.getSelectedDiseaseGroupsComparisonList().isEmpty()) {
            Set<QuantDiseaseGroupsComparison> compSet = Quant_Central_Manager.getSelectedDiseaseGroupsComparisonList();
            for (QuantDiseaseGroupsComparison comp : compSet) {
                selectedDsNumber += comp.getDatasetIndexes().length;
                for (int dsId : comp.getDatasetIndexes()) {
                    DatasetInformationOverviewLayout datasetInfoLayout = new DatasetInformationOverviewLayout((Page.getCurrent().getBrowserWindowWidth() * 90 / 100),Quant_Central_Manager.getDiseaseHashedColorMap());
                    datasetInfoLayout.updateDatasetForm(Quant_Central_Manager.getFullQuantDatasetMap().get(dsId));
//                    datasetInfoLayout.setWidth("100%");
                    datasetInfoLayoutDSIndexMap.put(dsId, datasetInfoLayout);
                    dsObjects.add(Quant_Central_Manager.getFullQuantDatasetMap().get(dsId));

                }
            }

        } else {
            selectedDsNumber = Quant_Central_Manager.getFilteredDatasetsList().size();
            Map<Integer, QuantDatasetObject> fullDsMap = Quant_Central_Manager.getFilteredDatasetsList();
            for (QuantDatasetObject quantDs : fullDsMap.values()) {
                DatasetInformationOverviewLayout datasetInfoLayout = new DatasetInformationOverviewLayout((Page.getCurrent().getBrowserWindowWidth() * 90 / 100),Quant_Central_Manager.getDiseaseHashedColorMap());
                datasetInfoLayout.updateDatasetForm(quantDs);
//                datasetInfoLayout.setWidth("100%");
                datasetInfoLayoutDSIndexMap.put(quantDs.getDsKey(), datasetInfoLayout);
                dsObjects.add(quantDs);

            }
        }
        this.studiesPopupLayout = new StudyPopupLayout(datasetInfoLayoutDSIndexMap);
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
                        DatasetInformationOverviewLayout datasetInfoLayout = new DatasetInformationOverviewLayout((Page.getCurrent().getBrowserWindowWidth() * 90 / 100),Quant_Central_Manager.getDiseaseHashedColorMap());
                        datasetInfoLayout.updateDatasetForm(Quant_Central_Manager.getFullQuantDatasetMap().get(dsId));
//                        datasetInfoLayout.setWidth("100%");
                        datasetInfoLayoutDSIndexMap.put(dsId, datasetInfoLayout);
                        dsObjects.add(Quant_Central_Manager.getFullQuantDatasetMap().get(dsId));

                    }
                }

            } else {
                selectedDsNumber = Quant_Central_Manager.getFilteredDatasetsList().size();
                Map<Integer, QuantDatasetObject> fullDsMap = Quant_Central_Manager.getFilteredDatasetsList();
                for (QuantDatasetObject quantDs : fullDsMap.values()) {
                    DatasetInformationOverviewLayout datasetInfoLayout = new DatasetInformationOverviewLayout((Page.getCurrent().getBrowserWindowWidth() * 90 / 100),Quant_Central_Manager.getDiseaseHashedColorMap());
                    datasetInfoLayout.updateDatasetForm(quantDs);
//                    datasetInfoLayout.setWidth("100%");
                    datasetInfoLayoutDSIndexMap.put(quantDs.getDsKey(), datasetInfoLayout);
                    dsObjects.add(quantDs);

                }
            }
            studiesPopupLayout.updateDatasetInfoLayoutDSIndexMap(datasetInfoLayoutDSIndexMap);
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

   

}
