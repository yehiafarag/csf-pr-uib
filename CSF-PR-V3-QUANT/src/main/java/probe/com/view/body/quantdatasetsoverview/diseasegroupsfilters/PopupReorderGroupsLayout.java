/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters;

import com.vaadin.event.LayoutEvents;
import com.vaadin.event.Transferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.DropTarget;
import com.vaadin.event.dd.TargetDetails;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.Not;
import com.vaadin.event.dd.acceptcriteria.SourceIsTarget;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.DragAndDropWrapper.DragStartMode;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.poi.hssf.usermodel.HeaderFooter;
import probe.com.selectionmanager.QuantCentralManager;
import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.peptidescontainer.popupcomponents.DatasetInformationOverviewLayout;

/**
 *
 * @author Yehia Farag
 */
public class PopupReorderGroupsLayout extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private final Window popupWindow;
    private final QuantCentralManager Quant_Central_Manager;
    private final VerticalLayout popupBodyLayout;
    private final Map<Integer, DatasetInformationOverviewLayout> datasetInfoLayoutDSIndexMap = new HashMap<Integer, DatasetInformationOverviewLayout>();
    private final SortableLayout sortableDiseaseGroupI, sortableDiseaseGroupII;

    public PopupReorderGroupsLayout(QuantCentralManager Quant_Central_Manager) {
        this.setStyleName("reordergroupsbtn");
        this.setDescription("Re-order All Disease Groups Comparisons");
        this.Quant_Central_Manager = Quant_Central_Manager;
        this.addLayoutClickListener(PopupReorderGroupsLayout.this);
        this.popupBodyLayout = new VerticalLayout();
        VerticalLayout windowLayout = new VerticalLayout();
        popupWindow = new Window() {
            @Override
            public void close() {
                popupWindow.setVisible(false);
            }
        };
        popupWindow.setContent(windowLayout);
        windowLayout.addComponent(popupBodyLayout);
        windowLayout.setComponentAlignment(popupBodyLayout, Alignment.MIDDLE_CENTER);
        int h = 600;
        int w = 600;
        if (Page.getCurrent().getBrowserWindowHeight() < 600) {
            h = Page.getCurrent().getBrowserWindowHeight();
        }
        if (Page.getCurrent().getBrowserWindowWidth() < 600) {
            w = Page.getCurrent().getBrowserWindowWidth();
        }

        popupBodyLayout.setWidth((w - 50) + "px");
        popupBodyLayout.setHeight((h - 50) + "px");
        popupWindow.setWindowMode(WindowMode.NORMAL);
        popupWindow.setWidth(w + "px");
        popupWindow.setHeight(h + "px");
        popupWindow.setVisible(false);
        popupWindow.setResizable(false);
        popupWindow.setClosable(true);
        popupWindow.setStyleName(Reindeer.WINDOW_LIGHT);
        popupWindow.setModal(true);
        popupWindow.setDraggable(false);
        popupWindow.setCaption("&nbsp;&nbsp;Disease Groups Re-order");
        popupWindow.setCaptionAsHtml(true);

        UI.getCurrent().addWindow(popupWindow);
        popupWindow.center();
        this.sortableDiseaseGroupI = new SortableLayout();
        this.sortableDiseaseGroupII = new SortableLayout();
        this.initPopupBody((w - 50), (h - 50));

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        popupWindow.setVisible(true);
    }

    private void initPopupBody(int w, int h) {
        Set<String> datasource = new LinkedHashSet<String>();
        datasource.add("Test - 1");
        datasource.add("Test - 2");
        datasource.add("Test - 3");
        datasource.add("Test - 4");
        datasource.add("Test - 5");
        datasource.add("Test - 6");
        datasource.add("Test - 7");
        datasource.add("Test - 8");

        VerticalLayout mainContainer = new VerticalLayout();
        mainContainer.setStyleName(Reindeer.LAYOUT_WHITE);
        mainContainer.setSpacing(true);
        mainContainer.setWidth(w + "px");
        int subH = (h / 2) - 70;
        mainContainer.setHeightUndefined();
        mainContainer.setMargin(new MarginInfo(true, true, true, true));
        VerticalLayout headerLayoutI = new VerticalLayout();
        Label titileI = new Label(" Disease Group I");
        titileI.setStyleName("custLabel");
        headerLayoutI.addComponent(titileI);
        mainContainer.addComponent(headerLayoutI);
        sortableDiseaseGroupI.setWidth(w + "px");
        sortableDiseaseGroupI.setData("dsG1");
        sortableDiseaseGroupI.setHeight(subH + "px");
        sortableDiseaseGroupI.addStyleName("no-horizontal-drag-hints");

        for (final VerticalLayout component : createComponents(datasource, w)) {
            sortableDiseaseGroupI.addComponent(component, "dsGr1");
        }
        mainContainer.addComponent(sortableDiseaseGroupI);
        mainContainer.setComponentAlignment(sortableDiseaseGroupI, Alignment.TOP_LEFT);

        VerticalLayout headerLayoutII = new VerticalLayout();
        Label titileII = new Label(" Disease Group II");
        titileII.setStyleName("custLabel");
        headerLayoutII.addComponent(titileII);
        mainContainer.addComponent(headerLayoutII);
        sortableDiseaseGroupII.setWidth(w + "px");
        sortableDiseaseGroupII.setData("dsG2");
        sortableDiseaseGroupII.setHeight(subH + "px");
        sortableDiseaseGroupII.addStyleName("no-horizontal-drag-hints");

        for (final VerticalLayout component : createComponents(datasource, w)) {

            sortableDiseaseGroupII.addComponent(component, "dsGr2");
        }
        mainContainer.addComponent(sortableDiseaseGroupII);
        mainContainer.setComponentAlignment(sortableDiseaseGroupII, Alignment.TOP_LEFT);

      
        
        
        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.setSpacing(true);
        btnLayout.setMargin(new MarginInfo(false, true, true, false));
        btnLayout.setWidthUndefined();
        mainContainer.addComponent(btnLayout);
        mainContainer.setComponentAlignment(btnLayout, Alignment.TOP_RIGHT);
        Button applyFilters = new Button("Apply");
        applyFilters.setDescription("Re-order disease groups");
        applyFilters.setPrimaryStyleName("resetbtn");
        applyFilters.setWidth("50px");
        applyFilters.setHeight("24px");

        btnLayout.addComponent(applyFilters);
        applyFilters.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                popupWindow.close();
            }
        });

        Button resetFiltersBtn = new Button("Cancel");
        resetFiltersBtn.setPrimaryStyleName("resetbtn");
        btnLayout.addComponent(resetFiltersBtn);
        resetFiltersBtn.setWidth("50px");
        resetFiltersBtn.setHeight("24px");

        resetFiltersBtn.setDescription("Reset all applied filters");
        resetFiltersBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                popupWindow.close();
            }
        });

        
          popupBodyLayout.addComponent(mainContainer);
        popupBodyLayout.setComponentAlignment(mainContainer, Alignment.TOP_LEFT);
    }

    private List<VerticalLayout> createComponents(Set<String> datasource, int w) {
        final List<VerticalLayout> componentsList = new ArrayList<VerticalLayout>();

        for (String strLabel : datasource) {
            VerticalLayout container = new VerticalLayout();
            container.setWidth(w - 50 + "px");
            container.setHeight("30px");
            container.setStyleName("rowItem");
            Label label = new Label(strLabel);
//            label.setWidth(w, Unit.PIXELS);
            container.addComponent(label);
            componentsList.add(container);
        }
        return componentsList;
    }

    class SortableLayout extends CustomComponent {

        private final AbstractOrderedLayout layout;
        private final DropHandler dropHandler;

        public SortableLayout() {
            layout = new VerticalLayout();
            dropHandler = new ReorderLayoutDropHandler(layout);

            final DragAndDropWrapper pane = new DragAndDropWrapper(layout);
            setCompositionRoot(pane);
        }

        public void addComponent(final Component component, String data) {
            final WrappedComponent wrapper = new WrappedComponent(component,
                    dropHandler);
            wrapper.setSizeUndefined();
            component.setHeight("100%");
            wrapper.setHeight("100%");
            wrapper.setData(data);
            layout.addComponent(wrapper);
        }
    }

    class WrappedComponent extends DragAndDropWrapper {

        private final DropHandler dropHandler;

        public WrappedComponent(final Component content,
                final DropHandler dropHandler) {
            super(content);
            this.dropHandler = dropHandler;
            setDragStartMode(DragStartMode.WRAPPER);
        }

        @Override
        public DropHandler getDropHandler() {
            return dropHandler;
        }

    }

    class ReorderLayoutDropHandler implements DropHandler {

        private final AbstractOrderedLayout layout;

        public ReorderLayoutDropHandler(final AbstractOrderedLayout layout) {
            this.layout = layout;
        }

        @Override
        public AcceptCriterion getAcceptCriterion() {
            return new Not(SourceIsTarget.get());
        }

        @Override
        public void drop(final DragAndDropEvent dropEvent) {
            final Transferable transferable = dropEvent.getTransferable();

            final Component sourceComponent = transferable.getSourceComponent();
            if (sourceComponent instanceof WrappedComponent) {
                final TargetDetails dropTargetData = dropEvent
                        .getTargetDetails();
                final DropTarget target = dropTargetData.getTarget();
                if (!((WrappedComponent) sourceComponent).getData().toString().equalsIgnoreCase(((WrappedComponent) target).getData().toString())) {
                    return;
                }

                // find the location where to move the dragged component
                boolean sourceWasAfterTarget = true;
                int index = 0;
                final Iterator<Component> componentIterator = layout.iterator();

                Component next = null;
                while (next != target && componentIterator.hasNext()) {
                    next = componentIterator.next();
                    if (next != sourceComponent) {
                        index++;
                    } else {
                        sourceWasAfterTarget = false;
                    }
                }
                if (next == null || next != target) {
                    // component not found - if dragging from another layout
                    return;
                }

                // drop on top of target?
                if (dropTargetData.getData("verticalLocation").equals(
                        VerticalDropLocation.MIDDLE.toString())) {
                    if (sourceWasAfterTarget) {
                        index--;
                    }
                } // drop before the target?
                else if (dropTargetData.getData("verticalLocation").equals(
                        VerticalDropLocation.TOP.toString())) {
                    index--;
                    if (index < 0) {
                        index = 0;
                    }
                }

                // move component within the layout
                layout.removeComponent(sourceComponent);
                layout.addComponent(sourceComponent, index);
            }
        }

    }
}
