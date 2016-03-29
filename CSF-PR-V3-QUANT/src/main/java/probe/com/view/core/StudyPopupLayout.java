/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.Map;
import java.util.Set;
import probe.com.model.beans.quant.QuantDatasetObject;
import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.peptidescontainer.popupcomponents.DatasetInformationOverviewLayout;

/**
 *
 * @author yfa041
 */
public class StudyPopupLayout extends VerticalLayout {

    private final GridLayout topLayout;
//    private final VerticalLayout bottomLayout;
//    private final VerticalLayout datasetsInformationContainer;
//    private VerticalLayout lastSelectedBtn;
    private Map<Integer, DatasetInformationOverviewLayout> datasetInfoLayoutDSIndexMap;

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
//            VerticalLayout btn = this.generateBtn(quantDS.getDsKey(), quantDS.getAuthor() + " (" + quantDS.getYear() + ")<br/><font size=1 >#Proteins: "+quantDS.getTotalProtNum()+"    #peptides: "+quantDS.getTotalPepNum()+"</font>");
            String btnName =  "<font size=1 >"+quantDS.getPumedID()+"</font><br/>"+quantDS.getAuthor() + "<br/><font size=1 >" + quantDS.getYear() + "</font><br/><font size=1 >#Proteins: "+quantDS.getTotalProtNum()+"   #peptides: "+quantDS.getTotalPepNum()+"</font>";
            PopupInfoBtn btn = new PopupInfoBtn(datasetInfoLayoutDSIndexMap.get(quantDS.getDsKey()), btnName,quantDS.getAuthor());
            topLayout.addComponent(btn, colcounter++, rowcounter);
            if (colcounter >= topLayout.getColumns()) {
                colcounter = 0;
                rowcounter++;

            }

        }

//            int subWidth = (int) ((float) bottomLayout.getWidth() - 30);
//            this.initPopupLayoutLayout(cp, subWidth);
//        this.selectStudyBtn((VerticalLayout) topLayout.getComponent(0, 0));

    }

    public StudyPopupLayout(Map<Integer, DatasetInformationOverviewLayout> datasetInfoLayoutDSIndexMap) {
        this.datasetInfoLayoutDSIndexMap = datasetInfoLayoutDSIndexMap;
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
        this.addComponent(topLayout);
//        datasetsInformationContainer = this.initInformationContainer(width);

//        bottomLayout = new VerticalLayout();
//        bottomLayout.setSpacing(true);
        
//        bottomLayout.setMargin(new MarginInfo(true, false, false, false));
//        this.addComponent(bottomLayout);
//        this.setComponentAlignment(bottomLayout, Alignment.BOTTOM_CENTER);
//        bottomLayout.setWidth("100%");
//        bottomLayout.setHeight("500px");

//        bottomLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        
//        bottomLayout.addComponent(datasetsInformationContainer);

    }

//    private VerticalLayout initInformationContainer(int width) {
//        VerticalLayout generatedPeptidesInformationContainer = new VerticalLayout();
//        generatedPeptidesInformationContainer.setWidth(width - 10 + "px");
//        generatedPeptidesInformationContainer.setHeightUndefined();
//        return generatedPeptidesInformationContainer;
//
//    }

//    @Override
//    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
////        if (lastSelectedBtn != null) {
////            lastSelectedBtn.setStyleName("tabbtn");
////        }
////        VerticalLayout selectedBtn;
////        if (event.getClickedComponent() == null) {
////            selectedBtn = (VerticalLayout) event.getComponent();
////
////        } else if (event.getClickedComponent() instanceof Label) {
////            selectedBtn = (VerticalLayout) event.getClickedComponent().getParent();
////        } else {
////            selectedBtn = (VerticalLayout) event.getClickedComponent();
////        }
////        if (selectedBtn == lastSelectedBtn) {
////            bottomLayout.setVisible(false);
////            lastSelectedBtn = null;
////            return;
////        }
////        this.selectStudyBtn(selectedBtn);
//    }

//    private void selectStudyBtn(VerticalLayout selectedBtn) {
//
////        lastSelectedBtn = selectedBtn;
////        lastSelectedBtn.setStyleName("selectedtabbtn");
////        updateDatasetInfoLayout((Integer) lastSelectedBtn.getData());
////        bottomLayout.setVisible(true);
//
//    }

//    private void updateDatasetInfoLayout(int dsKey) {
//        datasetsInformationContainer.removeAllComponents();
//        for (int key : datasetInfoLayoutDSIndexMap.keySet()) {
//            if (key == (dsKey)) {
//                datasetsInformationContainer.addComponent(datasetInfoLayoutDSIndexMap.get(key));
//                datasetsInformationContainer.setComponentAlignment(datasetInfoLayoutDSIndexMap.get(key), Alignment.BOTTOM_RIGHT);
//                break;
//            }
//        }
//    }

//    private VerticalLayout generateBtn(int dsKey, String btnName) {
//        VerticalLayout btn = new VerticalLayout();
////        btn.addLayoutClickListener(this);
//        btn.setHeight("60px");
//        btn.setWidth("200px");
//        Label btnLabel = new Label(btnName);
//        btnLabel.setContentMode(ContentMode.HTML);
//        btn.addComponent(btnLabel);
//        btn.setComponentAlignment(btnLabel, Alignment.MIDDLE_CENTER);
//        btn.setStyleName("tabbtn");
//        btn.setData(dsKey);
        
        //add popup for testing 
        
//        VerticalLayout infoPopup = new VerticalLayout();
//        infoPopup.setWidth("500px");
//        infoPopup.setHeight("500px");
//        infoPopup.setStyleName(Reindeer.LAYOUT_BLUE);
//        final PopupView pupupTestinglayout = new PopupView(null, infoPopup);
//        pupupTestinglayout.setWidth("2px");
//        pupupTestinglayout.setHeight("2px");
//        btn.addComponent(pupupTestinglayout);
//        btn.setComponentAlignment(pupupTestinglayout, Alignment.BOTTOM_RIGHT);
//        pupupTestinglayout.setVisible(true);
//        pupupTestinglayout.setPopupVisible(false);
//        
//        btn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
//
//            @Override
//            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
//                pupupTestinglayout.setPopupVisible(true);
//            }
//        });
//        pupupTestinglayout.setHideOnMouseOut(true);
        
        
        

//        return btn;
//    }

    public void updateDatasetInfoLayoutDSIndexMap(Map<Integer, DatasetInformationOverviewLayout> datasetInfoLayoutDSIndexMap) {
        this.datasetInfoLayoutDSIndexMap = datasetInfoLayoutDSIndexMap;

    }
}
