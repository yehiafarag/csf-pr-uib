/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.popupreordergroups;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.Transferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.DropTarget;
import com.vaadin.event.dd.TargetDetails;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.Not;
import com.vaadin.event.dd.acceptcriteria.SourceIsTarget;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author yfa041
 */
public class SortableLayoutContainer extends VerticalLayout {

    private final SortableLayout leftSortableDiseaseGroup;
    private final VerticalLayout counterI;
    private final VerticalLayout checkboxLayout;
    private final int itemWidth;
    private final String strTitle;
    private final OptionGroup diseaseGroupSelectOption;

    public SortableLayoutContainer(int w, int subH, String strTitle) {

        this.setSpacing(true);
        this.strTitle = strTitle;
        VerticalLayout headerLayoutI = new VerticalLayout();
        Label titileI = new Label(strTitle);
        titileI.setStyleName("custLabel");
        headerLayoutI.addComponent(titileI);
        this.addComponent(headerLayoutI);
        int width = (w / 2)- 70 ;
        int height = subH - 20;
        HorizontalLayout bodyLayout = new HorizontalLayout();
        this.addComponent(bodyLayout);

        counterI = new VerticalLayout();
        counterI.setWidth("30px");
        counterI.setSpacing(false);
        counterI.setStyleName("countcontainer");
        counterI.setMargin(new MarginInfo(false, true, false, false));
        bodyLayout.addComponent(counterI);

        leftSortableDiseaseGroup = new SortableLayout();
        leftSortableDiseaseGroup.setWidth(width + "px");
        leftSortableDiseaseGroup.setData(strTitle);
        leftSortableDiseaseGroup.setHeight(height + "px");
        bodyLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        leftSortableDiseaseGroup.addStyleName("no-horizontal-drag-hints");
        bodyLayout.addComponent(leftSortableDiseaseGroup);
        
        checkboxLayout = new VerticalLayout();
        checkboxLayout.setWidth("20px");
        checkboxLayout.setSpacing(false);
        checkboxLayout.setStyleName("countcontainer");
        checkboxLayout.setMargin(new MarginInfo(false, true, false, false));
        bodyLayout.addComponent(checkboxLayout);
        diseaseGroupSelectOption = new OptionGroup();
        checkboxLayout.addComponent(diseaseGroupSelectOption);
        checkboxLayout.setComponentAlignment(diseaseGroupSelectOption, Alignment.TOP_LEFT);
        diseaseGroupSelectOption.setWidth("20px");
        diseaseGroupSelectOption.setNullSelectionAllowed(true); // user can not 'unselect'
        diseaseGroupSelectOption.setMultiSelect(true);
        diseaseGroupSelectOption.addStyleName("sortablelayoutselect");
        diseaseGroupSelectOption.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
//                Quant_Central_Manager.setNoSerum(!diseaseGroupSelectOption.getValue().toString().equalsIgnoreCase("[Serum Studies]"));
//                counterLabel.setValue("( " + Quant_Central_Manager.getCurrentDsNumber() + " / " + Quant_Central_Manager.getTotalDsNumber() + " )");

            }

        });
        

        itemWidth = width - 10;

    }

    public void updateListes(Set<String> labels) {

        int counter = 0;
        for (final VerticalLayout component : createComponents(labels)) {
            VerticalLayout container = new VerticalLayout();
            container.setWidth(30 + "px");
            container.setHeight("20px");
            container.setStyleName("countItem");
            Label label = new Label(counter + 1 + "");
            container.addComponent(label);
            counterI.addComponent(container);
            leftSortableDiseaseGroup.addComponent(component, strTitle);
            diseaseGroupSelectOption.addItem(counter);
            diseaseGroupSelectOption.setItemCaption(counter, "");

            counter++;
        }

    }

    private List<VerticalLayout> createComponents(Set<String> datasource) {
        final List<VerticalLayout> componentsList = new ArrayList<VerticalLayout>();

        for (String strLabel : datasource) {
            VerticalLayout container = new VerticalLayout();
            container.setWidth(itemWidth + "px");
            container.setHeight("30px");
            container.setStyleName("rowItem");
            Label label = new Label(strLabel);
            container.addComponent(label);
            componentsList.add(container);
        }
        return componentsList;
    }

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

    public AbstractOrderedLayout getLayout() {
        return layout;
    }
}

class WrappedComponent extends DragAndDropWrapper {

    private final DropHandler dropHandler;

    public WrappedComponent(final Component content,
            final DropHandler dropHandler) {
        super(content);
        this.dropHandler = dropHandler;
        setDragStartMode(DragAndDropWrapper.DragStartMode.WRAPPER);
    }

    @Override
    public DropHandler getDropHandler() {
        return dropHandler;
    }

}

class ReorderLayoutDropHandler implements DropHandler {

    private final AbstractOrderedLayout layout;

    public AbstractOrderedLayout getLayout() {
        return layout;
    }

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
