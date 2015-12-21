/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.popupreordergroups;

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
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Yehia Farag
 */
public class SortableLayoutContainer extends VerticalLayout {

    private final SortableLayout sortableDiseaseGroupLayout;
    private final VerticalLayout counterLayout, counterLayoutContainer;
    private final VerticalLayout checkboxLayout;
    private final int itemWidth;
    private final String strTitle;
    private final OptionGroup diseaseGroupSelectOption;
    private final Button clearBtn;
    private final Map<String, Boolean> groupSelectionMap;
    private final Set<String> selectionSet;
    private boolean autoClear;//, autoselectall;
    private boolean singleSelected = false;
    private Set selectAllSet = new HashSet();

    public SortableLayoutContainer(int w, int subH, final String strTitle, Set<String> labels) {

        this.setStyleName(Reindeer.LAYOUT_WHITE);
        this.setSpacing(true);
        this.strTitle = strTitle;
        this.groupSelectionMap = new HashMap<String, Boolean>();
        this.selectionSet = new LinkedHashSet<String>();
        HorizontalLayout headerLayoutI = new HorizontalLayout();
        Label titileI = new Label(strTitle);
        titileI.setStyleName("custLabel");
        headerLayoutI.addComponent(titileI);
        this.addComponent(headerLayoutI);
        int containerWidth = ((w) / 2) - 30;

        this.setWidth(containerWidth + "px");
        int height = subH - 15;
        headerLayoutI.setWidth(containerWidth + "px");

        clearBtn = new Button("Clear");
        clearBtn.setStyleName(Reindeer.BUTTON_LINK);
        clearBtn.setWidth("40px");
        clearBtn.setHeight("18px");
        clearBtn.setEnabled(false);
        headerLayoutI.addComponent(clearBtn);
        headerLayoutI.setComponentAlignment(clearBtn, Alignment.TOP_RIGHT);
        clearBtn.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
//                autoClear = true;
                singleSelected = false;
                diseaseGroupSelectOption.setValue(null);

            }
        });

        Panel bodyPanel = new Panel();
        HorizontalLayout bodyLayout = new HorizontalLayout();
        bodyLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        bodyPanel.setContent(bodyLayout);
        this.addComponent(bodyPanel);
        bodyPanel.setHeight(subH + "px");
        bodyPanel.setWidth((containerWidth) + "px");

        counterLayoutContainer = new VerticalLayout();
        bodyLayout.addComponent(counterLayoutContainer);
        counterLayoutContainer.setHeight(height + "px");
        counterLayoutContainer.setWidth("30px");
        counterLayoutContainer.setStyleName(Reindeer.LAYOUT_WHITE);
        
        counterLayout = new VerticalLayout();
        counterLayoutContainer.addComponent(counterLayout);
        counterLayout.setWidth("30px");
        counterLayout.setSpacing(false);
        counterLayout.setStyleName("countcontainer");

        int sortableItremWidth = containerWidth - 26-6;
        sortableDiseaseGroupLayout = new SortableLayout();
        bodyLayout.addComponent(sortableDiseaseGroupLayout);
        sortableDiseaseGroupLayout.setWidth(sortableItremWidth + "px");
        sortableDiseaseGroupLayout.setHeight(height + "px");
        sortableDiseaseGroupLayout.setData(strTitle);
        sortableDiseaseGroupLayout.addStyleName("no-horizontal-drag-hints");
       

        checkboxLayout = new VerticalLayout();
        bodyLayout.addComponent(checkboxLayout);
        checkboxLayout.setSpacing(false);
        checkboxLayout.setEnabled(false);
        checkboxLayout.setHeight(height + "px");
        checkboxLayout.setStyleName("countcontainer");
        checkboxLayout.setMargin(new MarginInfo(false, false, false, false));

        diseaseGroupSelectOption = new OptionGroup();
        checkboxLayout.addComponent(diseaseGroupSelectOption);
        checkboxLayout.setComponentAlignment(diseaseGroupSelectOption, Alignment.TOP_LEFT);
//        diseaseGroupSelectOption.setWidth("20px");
        diseaseGroupSelectOption.setNullSelectionAllowed(true); // user can not 'unselect'
        diseaseGroupSelectOption.setMultiSelect(true);
        diseaseGroupSelectOption.addStyleName("sortablelayoutselect");
        diseaseGroupSelectOption.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (autoClear) {
                    autoClear = false;
                    return;
                }
                for (String key : groupSelectionMap.keySet()) {
                    groupSelectionMap.put(key, Boolean.FALSE);
                }
                getSelectionSet().clear();
                singleSelected = false;
                int counter = 0;
                for (Object key : ((Set) diseaseGroupSelectOption.getValue())) {
                    if (((Integer) key) >= groupsIds.size()) {
                        continue;
                    }
                    groupSelectionMap.put(groupsIds.get((Integer) key), Boolean.TRUE);
                    singleSelected = true;
                    counter++;
                    getSelectionSet().add(groupsIds.get((Integer) key));
                }
                if (counter == groupsIds.size()) {
                    singleSelected = false;
                }

            }

        });

        itemWidth = sortableItremWidth - 10;
        initLists(labels);
    }

    public boolean isSingleSelected() {
        return singleSelected;
    }

    public void addSelectionValueChangeListener(Property.ValueChangeListener listener) {
        diseaseGroupSelectOption.addValueChangeListener(listener);

    }

    public final void initLists(Set<String> labels) {
        sortableDiseaseGroupLayout.removeAllComponents();
        counterLayout.removeAllComponents();
        diseaseGroupSelectOption.removeAllItems();
        selectAllSet.clear();
        int counter = 0;
        for (final VerticalLayout component : createComponents(labels)) {
            VerticalLayout container = new VerticalLayout();
            container.setWidth(30 + "px");
            container.setHeight("20px");
            container.setStyleName("countItem");
            Label label = new Label(counter + 1 + "");
            container.addComponent(label);
            this.counterLayout.addComponent(container);
            sortableDiseaseGroupLayout.addComponent(component, strTitle);
            diseaseGroupSelectOption.addItem(counter);
            diseaseGroupSelectOption.setItemCaption(counter, "");
            autoClear = true;
            diseaseGroupSelectOption.select(counter);
            selectAllSet.add(counter);
            counter++;
        }
        autoClear = false;

    }

    public void updateLists(Set<String> labels) {

        autoClear = true;
        diseaseGroupSelectOption.setValue(null);

        Set updatedSelectionSet = new HashSet();
        if (labels.isEmpty() || (labels.size() == groupsIds.size())) {
            updatedSelectionSet.addAll(selectAllSet);
        } else {
            for (int i = 0; i < groupsIds.size(); i++) {

                if (labels.contains(groupsIds.get(i)) && groupSelectionMap.get(groupsIds.get(i))) {
                    updatedSelectionSet.add(i);
                }

            }
        }
        diseaseGroupSelectOption.setValue(updatedSelectionSet);

    }

    public LinkedHashSet<String> getSortedSet() {
        LinkedHashSet sortedSet;
        if (isSingleSelected()) {
            sortedSet = new LinkedHashSet(selectionSet);

        } else {
            sortedSet = new LinkedHashSet(groupsIds);
        }
        return sortedSet;

    }

    public void setLayoutMode(String mode) {
        if (mode.equalsIgnoreCase("select")) {
            checkboxLayout.setEnabled(true);
            clearBtn.setEnabled(true);
            sortableDiseaseGroupLayout.setEnabled(false);
        } else {
            checkboxLayout.setEnabled(false);
            clearBtn.setEnabled(false);
            sortableDiseaseGroupLayout.setEnabled(true);
        }

    }

    public void setEnableSelection(boolean enable) {

        checkboxLayout.setEnabled(enable);
        clearBtn.setEnabled(enable);

    }
    private final LinkedList<String> groupsIds = new LinkedList<String>();

    private List<VerticalLayout> createComponents(Set<String> datasource) {
        final List<VerticalLayout> componentsList = new ArrayList<VerticalLayout>();
        groupsIds.clear();
        groupSelectionMap.clear();
        for (String strLabel : datasource) {
            VerticalLayout container = new VerticalLayout();
            container.setWidth(itemWidth + "px");
            container.setHeight("30px");
            container.setStyleName("rowItem");
            container.setDescription(strLabel);
            Label label = new Label(strLabel);
            container.addComponent(label);
            componentsList.add(container);
            groupSelectionMap.put(strLabel, Boolean.FALSE);
            groupsIds.add(strLabel);
        }
        return componentsList;
    }

    public Set<String> getSelectionSet() {
        return selectionSet;
    }

    class SortableLayout extends CustomComponent {

        private final AbstractOrderedLayout layout;
        private final DropHandler dropHandler;

        public SortableLayout() {
            layout = new VerticalLayout();
            dropHandler = new ReorderLayoutDropHandler(layout);
            this.setEnabled(true);

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

        public void removeAllComponents() {
            layout.removeAllComponents();

        }

        public AbstractOrderedLayout getLayout() {
            return layout;
        }
    }

    class WrappedComponent extends DragAndDropWrapper {

        private final DropHandler dropHandler;

        public WrappedComponent(final Component content, final DropHandler dropHandler) {
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
                //update the selection (unselect current selected)
                int sourceIndex = layout.getComponentIndex(sourceComponent);

                String srcLabel = groupsIds.get(sourceIndex);
//                String trgLabel = groupsIds.get(index);

                groupsIds.remove(srcLabel);
                groupsIds.add(index, srcLabel);
                autoClear = true;
                diseaseGroupSelectOption.setValue(null);
                Set selectionSet = new HashSet();
                for (int i = 0; i < groupsIds.size(); i++) {
                    if (groupSelectionMap.get(groupsIds.get(i))) {
                        selectionSet.add(i);
                    }

                }
                diseaseGroupSelectOption.setValue(selectionSet);
//               
                // move component within the layout
                layout.removeComponent(sourceComponent);
                layout.addComponent(sourceComponent, index);

            }
        }

    }

}
