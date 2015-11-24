/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters;

import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import probe.com.selectionmanager.DatasetExploringCentralSelectionManager;
import probe.com.view.body.quantdatasetsoverview.QuantDatasetsfullStudiesTableLayout;

/**
 *
 * @author yfa041
 */
public class StudiesTablePopupBtn extends Button implements Button.ClickListener {

    private final Window popupWindow;

    public StudiesTablePopupBtn(DatasetExploringCentralSelectionManager datasetExploringCentralSelectionManager) {
        super("Studies Table");
        this.setStyleName(Reindeer.BUTTON_LINK);
        this.setDescription("Show studies table");
        this.addClickListener(StudiesTablePopupBtn.this);

        VerticalLayout popupBody = new VerticalLayout();
        QuantDatasetsfullStudiesTableLayout quantStudiesTable = new QuantDatasetsfullStudiesTableLayout(datasetExploringCentralSelectionManager);
        popupBody.addComponent(quantStudiesTable);
        popupWindow = new Window() {
            @Override
            public void close() {
                popupWindow.setVisible(false);
            }
        };
        popupWindow.setContent(popupBody);
        popupWindow.setWindowMode(WindowMode.NORMAL);
        popupWindow.setWidth("90%");
        popupWindow.setHeight("530px");
        popupWindow.setVisible(false);
        popupWindow.setResizable(false);
        popupWindow.setClosable(false);
        popupWindow.setStyleName(Reindeer.WINDOW_LIGHT);
        popupWindow.setModal(true);
        popupWindow.setDraggable(false);
        popupWindow.center();

        popupWindow.setCaption("&nbsp;&nbsp;Studies Table ( "+quantStudiesTable.getTotalStudiesNumber()+" )");

        UI.getCurrent().addWindow(popupWindow);
        popupWindow.center();

        popupWindow.setCaptionAsHtml(true);
        popupWindow.setClosable(true);

        popupBody.setMargin(true);
        popupBody.setSpacing(true);

    }

    @Override
    public void buttonClick(ClickEvent event) {
        popupWindow.setVisible(true);
    }

}
