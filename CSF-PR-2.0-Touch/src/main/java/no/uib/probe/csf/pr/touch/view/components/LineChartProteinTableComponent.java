package no.uib.probe.csf.pr.touch.view.components;

import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantComparisonProtein;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFListener;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFPR_Central_Manager;
import no.uib.probe.csf.pr.touch.view.components.linechartproteintablecomponents.ProteinTable;
import no.uib.probe.csf.pr.touch.view.core.ExternalLink;
import no.uib.probe.csf.pr.touch.view.core.ProteinTrendLayout;
import no.uib.probe.csf.pr.touch.view.core.SearchingField;
import no.uib.probe.csf.pr.touch.view.core.TrendLegend;

/**
 *
 * @author Yehia Farag
 *
 * this class represents both protein table and linechart component the protein
 * line chart represents the overall protein trend across different comparisons
 */
public class LineChartProteinTableComponent extends VerticalLayout implements CSFListener, LayoutEvents.LayoutClickListener {

    private final CSFPR_Central_Manager CSFPR_Central_Manager;
    private final VerticalLayout controlBtnsContainer;
    private int width, height;
    private final ProteinTable quantProteinTable;
    private final Map<String, QuantComparisonProtein> proteinSearchingMap;

    public LineChartProteinTableComponent(CSFPR_Central_Manager CSFPR_Central_Manager, int width, int height, QuantDiseaseGroupsComparison userCustomizedComparison) {

        this.CSFPR_Central_Manager = CSFPR_Central_Manager;
        this.proteinSearchingMap = new HashMap<>();

        this.setWidth(100, Unit.PERCENTAGE);
        this.setHeight(height, Unit.PIXELS);

        VerticalLayout bodyContainer = new VerticalLayout();
        bodyContainer.setWidth(100, Unit.PERCENTAGE);
        bodyContainer.setHeightUndefined();
        bodyContainer.setSpacing(true);
        this.addComponent(bodyContainer);

        //init toplayout
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setHeight(25, Unit.PIXELS);
        topLayout.setWidth(100, Unit.PERCENTAGE);
        topLayout.setSpacing(true);
        topLayout.setMargin(new MarginInfo(false, false, false, true));
        bodyContainer.addComponent(topLayout);

        HorizontalLayout titleLayoutWrapper = new HorizontalLayout();
        titleLayoutWrapper.setHeight(25, Unit.PIXELS);
        titleLayoutWrapper.setWidthUndefined();
        titleLayoutWrapper.setSpacing(true);
        titleLayoutWrapper.setMargin(false);
        topLayout.addComponent(titleLayoutWrapper);

        Label overviewLabel = new Label("Proteins");
        overviewLabel.setStyleName(ValoTheme.LABEL_BOLD);
        overviewLabel.setWidth(75, Unit.PIXELS);
        titleLayoutWrapper.addComponent(overviewLabel);
        titleLayoutWrapper.setComponentAlignment(overviewLabel, Alignment.MIDDLE_CENTER);

        SearchingField searchingFieldLayout = new SearchingField() {

            @Override
            public void textChanged(String text) {
                quantProteinTable.filterTable(getSearchingProteinsList(text));

            }

        };
        titleLayoutWrapper.addComponent(searchingFieldLayout);
        titleLayoutWrapper.setComponentAlignment(searchingFieldLayout, Alignment.MIDDLE_CENTER);
//        InfoPopupBtn info = new InfoPopupBtn("The protein table and overview chart give an overview for the selected proteins in the selected comparisons.");
//        titleLayoutWrapper.addComponent(info);
//        titleLayoutWrapper.setComponentAlignment(info, Alignment.MIDDLE_CENTER);

        TrendLegend legendLayout = new TrendLegend("linechart");
        legendLayout.setWidthUndefined();
        legendLayout.setHeight(24, Unit.PIXELS);
        topLayout.addComponent(legendLayout);
        topLayout.setComponentAlignment(legendLayout, Alignment.MIDDLE_RIGHT);

        //end of toplayout
        //start chart layout
        VerticalLayout tableLayoutFrame = new VerticalLayout();
        height = height - 44;

        int tableHeight = height;
        width = width - 50;
        tableLayoutFrame.setWidth(width, Unit.PIXELS);
        tableLayoutFrame.setHeight(tableHeight, Unit.PIXELS);
        tableLayoutFrame.addStyleName("roundedborder");
        tableLayoutFrame.addStyleName("whitelayout");
        bodyContainer.addComponent(tableLayoutFrame);
        bodyContainer.setComponentAlignment(tableLayoutFrame, Alignment.MIDDLE_CENTER);
        height = height - 40;
        width = width - 60;
        this.height = height;
        this.width = width;
        quantProteinTable = new ProteinTable(width);//this.initProteinTable();
        tableLayoutFrame.addComponent(quantProteinTable);

        //init side control btns layout 
        controlBtnsContainer = new VerticalLayout();
        controlBtnsContainer.setHeightUndefined();
        controlBtnsContainer.setWidthUndefined();
        controlBtnsContainer.setSpacing(true);

        CSFPR_Central_Manager.registerListener(LineChartProteinTableComponent.this);

    }

    @Override
    public void selectionChanged(String type) {
        if (type.equalsIgnoreCase("protein_selection")) {
            proteinSearchingMap.clear();
            Set<QuantComparisonProtein> selectedProteinsList;
            if (CSFPR_Central_Manager.getSelectedProteinsList() == null) {
                selectedProteinsList = new LinkedHashSet<>();
                for (QuantDiseaseGroupsComparison comparison : CSFPR_Central_Manager.getSelectedComparisonsList()) {
                    selectedProteinsList.addAll(comparison.getQuantComparisonProteinMap().values());
                }

            } else {
                selectedProteinsList = CSFPR_Central_Manager.getSelectedProteinsList();
            }

            quantProteinTable.updateTableData(CSFPR_Central_Manager.getSelectedComparisonsList(), selectedProteinsList);
           selectedProteinsList.stream().forEach((protein) -> {
                proteinSearchingMap.put(protein.getProteinAccession() + "__" + protein.getProteinName(), protein);
            });

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

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public VerticalLayout getControlBtnsContainer() {
        return controlBtnsContainer;
    }

    /**
     * searching for proteins using name or accessions within the quant
     * comparisons table
     *
     * @param keyword query keyword
     * @return list of found quant proteins
     */
    private Set<QuantComparisonProtein> getSearchingProteinsList(String keyword) {
        Set<QuantComparisonProtein> subAccessionMap = new HashSet<>();
        proteinSearchingMap.keySet().stream().filter((key) -> (key.trim().toLowerCase().contains(keyword.toLowerCase().trim()))).forEach((key) -> {
            subAccessionMap.add(proteinSearchingMap.get(key));
        });
        return subAccessionMap;
    }

}
