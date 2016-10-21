/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.VerticalLayout;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDataset;

/**
 *
 * @author Yehia Farag
 *
 * this class represents study information popup window
 */
public class DatasetButtonsContainerLayout extends VerticalLayout {

    private final GridLayout btnsContainer;
    private final String styleI = "lightbluebackground";
    private final String styleII = "lightredbackground";
    private String lastStyle = "";
    Map<String, String> publicationStyle;

    public void setInformationData(Collection<QuantDataset> dsObjects) {

        btnsContainer.removeAllComponents();
        publicationStyle.clear();

        rowNumb = Math.max(1, ((dsObjects.size() / btnsContainer.getColumns()) + 1));
        btnsContainer.setRows(rowNumb);
        if (rowNumb == 1) {
            btnsContainer.setWidthUndefined();
        } else {
            btnsContainer.setWidth(100, Unit.PERCENTAGE);
        }

        Map<String, Set<QuantDataset>> sortMap = new TreeMap(Collections.reverseOrder());
        dsObjects.stream().forEach((ds) -> {
            String key = ds.getYear() + "_" + ds.getPubMedId();
            if (!sortMap.containsKey(key)) {
                sortMap.put(key, new HashSet<>());
            }
            Set<QuantDataset> set = sortMap.get(key);
            set.add(ds);
            sortMap.put(key, set);
        });

        int colcounter = 0;
        int rowcounter = 0;
        for (String quantDSKey : sortMap.keySet()) {
            for (QuantDataset quantDS : sortMap.get(quantDSKey)) {

                if (!publicationStyle.containsKey(quantDS.getPubMedId())) {
                    if (lastStyle.equalsIgnoreCase(styleI)) {
                        publicationStyle.put(quantDS.getPubMedId(), styleII);
                        lastStyle = styleII;
                    } else {
                        publicationStyle.put(quantDS.getPubMedId(), styleI);
                        lastStyle = styleI;
                    }

                }
                String btnName = "<font size=1 >" + quantDS.getYear() + "</font><br/>"+quantDS.getAuthor()+"<br/><font size=1 >#Proteins: " + quantDS.getTotalProtNum() + "   #Peptides: " + quantDS.getTotalPepNum() + "</font>";

                PopupInfoBtn btn = new PopupInfoBtn(quantDS, btnName, quantDS.getAuthor(), smallScreen);
                btn.addStyleName(publicationStyle.get(quantDS.getPubMedId()));
                btnsContainer.addComponent(btn, colcounter++, rowcounter);
                if (colcounter >= btnsContainer.getColumns()) {
                    colcounter = 0;
                    rowcounter++;

                }
            }
        }

    }

    public void setPublicationData(List<Object[]> publicationObjects) {

        btnsContainer.removeAllComponents();
        publicationStyle.clear();

         rowNumb = Math.max(1, ((publicationObjects.size() / btnsContainer.getColumns()) + 1));
        btnsContainer.setRows(rowNumb);
        if (rowNumb == 1) {
            btnsContainer.setWidthUndefined();
            btnsContainer.setWidth(100, Unit.PERCENTAGE);
        } else {
            btnsContainer.setWidth(100, Unit.PERCENTAGE);
        }

        Map<String, Object[]> sortMap = new TreeMap(Collections.reverseOrder());
        publicationObjects.stream().forEach((obj) -> {
            String key = obj[2].toString() + "_" + obj[0].toString();
            sortMap.put(key, obj);
        });

        int colcounter = 0;
        rowcounter = 0;
        for (String quantDSKey : sortMap.keySet()) {
            Object[] obj = sortMap.get(quantDSKey);

            if (!publicationStyle.containsKey(obj[0].toString())) {
                if (lastStyle.equalsIgnoreCase(styleI)) {
                    publicationStyle.put(obj[0].toString(), styleII);
                    lastStyle = styleII;
                } else {
                    publicationStyle.put(obj[0].toString(), styleI);
                    lastStyle = styleI;
                }

            }
            String btnName = "<font size=1 >" + obj[2].toString() + "</font><br/>"+obj[1].toString() +"<br/><font size=1 >#Proteins: " + obj[5].toString() /*+ "/" + obj[5].toString() + */ + "   #Peptides: " + obj[7].toString() /*+ "/" + obj[7].toString() +*/ + "</font>";

            PopupInfoBtn btn = new PopupInfoBtn(btnName, obj[1].toString(), obj, smallScreen);
            btn.addStyleName(publicationStyle.get(obj[0].toString()));
            btnsContainer.addComponent(btn, colcounter++, rowcounter);
            if (colcounter >= btnsContainer.getColumns()) {
                colcounter = 0;
                rowcounter++;

            }

        }
        if(btnsContainer.getColumns()>sortMap.size() && btnsContainer.getRows()==1){
            for(int x=sortMap.size();x<btnsContainer.getColumns();x++){
                VerticalLayout emptyLayout = new VerticalLayout();
                emptyLayout.setHeight(20,Unit.PIXELS);
                emptyLayout.setWidth(200,Unit.PIXELS);
                btnsContainer.addComponent(emptyLayout, x, 0);
            }
        
        }

    }

    private final boolean smallScreen;
    private final int colNum;
    private int rowcounter,rowNumb;

    public int getRowcounter() {
        return rowNumb;
    }
    public DatasetButtonsContainerLayout(int width, boolean smallScreen) {
        this.smallScreen = smallScreen;
        this.publicationStyle = new HashMap<>();
        this.setWidth(100, Unit.PERCENTAGE);
        this.setHeightUndefined();
        this.setMargin(new MarginInfo(true, false, true, true));
        this.setSpacing(true);

        width = width * 90 / 100;
        colNum = Math.max(1, width / 210);
        btnsContainer = new GridLayout();
        btnsContainer.setWidth(100, Unit.PERCENTAGE);
        btnsContainer.setColumns(colNum);
        for (int x = 0; x < colNum; x++) {
            btnsContainer.setColumnExpandRatio(x, 200);
        }
        btnsContainer.setStyleName("whitelayout");
        btnsContainer.setHeightUndefined();
        btnsContainer.setSpacing(false);
        btnsContainer.setHideEmptyRowsAndColumns(false);

        this.addComponent(btnsContainer);

    }

    public int getColNumber() {
        return colNum;
    }

    public int getLayoutHeight() {
        return btnsContainer.getRows() * 100;
    }

}
