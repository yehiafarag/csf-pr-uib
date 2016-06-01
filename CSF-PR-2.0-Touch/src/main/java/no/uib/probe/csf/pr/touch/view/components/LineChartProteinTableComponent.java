package no.uib.probe.csf.pr.touch.view.components;

import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFListener;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFPR_Central_Manager;
import no.uib.probe.csf.pr.touch.view.core.ProteinTrendLayout;
import no.uib.probe.csf.pr.touch.view.core.SearchingField;
import no.uib.probe.csf.pr.touch.view.core.TrendLegend;
import org.vaadin.teemu.VaadinIcons;

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
    private final Table quantProteinTable;
    private final Map<Object, Object[]> tableItemsMap;

    public LineChartProteinTableComponent(CSFPR_Central_Manager CSFPR_Central_Manager, int width, int height, QuantDiseaseGroupsComparison userCustomizedComparison) {

        this.CSFPR_Central_Manager = CSFPR_Central_Manager;
        this.tableItemsMap = new LinkedHashMap<>();

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
                System.out.println("at text changed " + text);

            }

        };
        titleLayoutWrapper.addComponent(searchingFieldLayout);
        titleLayoutWrapper.setComponentAlignment(searchingFieldLayout, Alignment.MIDDLE_CENTER);
//        InfoPopupBtn info = new InfoPopupBtn("The protein table and overview chart give an overview for the selected proteins in the selected comparisons.");
//        titleLayoutWrapper.addComponent(info);
//        titleLayoutWrapper.setComponentAlignment(info, Alignment.MIDDLE_CENTER);

        TrendLegend legendLayout = new TrendLegend("table");
        legendLayout.setWidthUndefined();
        legendLayout.setHeight(24, Unit.PIXELS);
        topLayout.addComponent(legendLayout);
        topLayout.setComponentAlignment(legendLayout, Alignment.MIDDLE_RIGHT);

        //end of toplayout
        //start chart layout
        VerticalLayout tableLayoutFrame = new VerticalLayout();
        height = height - 44;

        int tableHeight = height / 3;
        width = width - 50;
        tableLayoutFrame.setWidth(width, Unit.PIXELS);
        tableLayoutFrame.setHeight(tableHeight, Unit.PIXELS);
        tableLayoutFrame.addStyleName("roundedborder");
        tableLayoutFrame.addStyleName("whitelayout");
        bodyContainer.addComponent(tableLayoutFrame);
        bodyContainer.setComponentAlignment(tableLayoutFrame, Alignment.MIDDLE_CENTER);
        height = height - 40;
        width = width - 40;
        this.height = height;
        this.width = width;
        quantProteinTable = this.initProteinTable();
        tableLayoutFrame.addComponent(quantProteinTable);

        //init side control btns layout 
        controlBtnsContainer = new VerticalLayout();
        controlBtnsContainer.setHeightUndefined();
        controlBtnsContainer.setWidthUndefined();
        controlBtnsContainer.setSpacing(true);

    }

    private Table initProteinTable() {
        Table table = new Table(){
        
        public String getColumnHeader(Object property) {
return "<a title='Tooltip text'" + propertyId + "</a>".replace("'","\"");
}
        };
        table.setWidth(100, Unit.PERCENTAGE);
        table.setHeight(100, Unit.PERCENTAGE);
        table.setSelectable(true);
        table.setColumnReorderingAllowed(false);

        table.setColumnCollapsingAllowed(false);
        table.setImmediate(true); // react at once when something is selected
        table.setMultiSelect(false);

        table.addContainerProperty("Index", Integer.class, null, "", null, Table.Align.RIGHT);
        table.addContainerProperty("Accession", Link.class, null, "Accession", null, Table.Align.LEFT);
        table.addContainerProperty("Name", String.class, null, "Name", null, Table.Align.LEFT);
        table.addContainerProperty("Comparisons Overview", ProteinTrendLayout.class, null, "Comparisons Overview", null, Table.Align.LEFT);

        /* This set contains the ids of the "selected" items */
        final Set<Object> selectedItemIds = new HashSet<>();
        selectedItemIds.add(4); // We'll start off with #4 selected, just to show that it works

        /* This checkbox reflects the contents of the selectedItemIds set */
        table.addGeneratedColumn("selected", (Table source, final Object itemId, Object columnId) -> {
            boolean selected = selectedItemIds.contains(itemId);
            /* When the chekboc value changes, add/remove the itemId from the selectedItemIds set */
            final CheckBox cb = new CheckBox("", selected);
            cb.addValueChangeListener((Property.ValueChangeEvent event) -> {
                if (selectedItemIds.contains(itemId)) {
                    selectedItemIds.remove(itemId);
                } else {
                    selectedItemIds.add(itemId);
                }
            });
            return cb;
        });

        Link link = new Link("google", new ExternalResource("www.google.com"));
        link.setTargetName("_blank");
        link.setPrimaryStyleName("tablelink");
        
          Link link2 = new Link("google", new ExternalResource("www.google.com"));
        link2.setTargetName("_blank");
        link2.setPrimaryStyleName("tablelink"); 
        
        tableItemsMap.put(4, new Object[]{1, link, "Yehia Farag", new ProteinTrendLayout()});
        tableItemsMap.put(5, new Object[]{2, link2, "Yehia Farag", new ProteinTrendLayout()});
        table.addItem(tableItemsMap.get(4), 4);
        table.addItem(tableItemsMap.get(5), 5);

        table.setColumnIcon("selected", VaadinIcons.CHECK_SQUARE_O);
        table.setColumnHeader("selected", "");
        table.setColumnAlignment("selected", Table.Align.CENTER);
        table.setColumnWidth("selected", 60);
        table.setColumnWidth("Index", 47);
        table.setColumnWidth("Accession", 87);
        table.setColumnWidth("Name", 187);

        table.addHeaderClickListener((Table.HeaderClickEvent event) -> {
            table.removeAllItems();
            if (!selectedItemIds.isEmpty()) {
                for (Object itemId : selectedItemIds) {
                    table.addItem(tableItemsMap.get(itemId), itemId);
                }
            } else {
                for (Object itemId : tableItemsMap.keySet()) {
                    table.addItem(tableItemsMap.get(itemId), itemId);
                }
            }

        });

        table.addColumnResizeListener((Table.ColumnResizeEvent event) -> {
            table.setColumnWidth(event.getPropertyId(), event.getPreviousWidth());
        });
       
        return table;

    }

    @Override
    public void selectionChanged(String type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getFilterId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

}
