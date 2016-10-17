package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
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
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.HeatMapHeaderCellInformationBean;

/**
 *
 * @author Yehia Farag
 */
public class SortableLayoutContainer extends VerticalLayout {

    private final SortableLayout sortableDiseaseGroupLayout;
    private final VerticalLayout counterLayout, counterLayoutContainer;
    private final VerticalLayout checkboxLayout;
//    private final int itemWidth;
    private final String strTitle;
    private final OptionGroup diseaseGroupSelectOption;
    private final Button clearBtn;
    private final Map<HeatMapHeaderCellInformationBean, Boolean> groupSelectionMap;
    private final Set<HeatMapHeaderCellInformationBean> selectionSet;
    private boolean autoClear;//, autoselectall;

    public int getFinalHeight() {
        return (int)bodyPanel.getHeight()+30;
    }
    private boolean singleSelected = false;
    private Set selectAllSet = new HashSet();
    private final ValueChangeListener selectDataListener;
    private final int maxHeight;
    private final Panel bodyPanel;
    
//    private final Map<String, String> diseaseStyleMap;

    public SortableLayoutContainer(final String strTitle, int height) {

        this.setStyleName("whitelayout");
        this.setSpacing(true);
        this.setMargin(new MarginInfo(false, true, false, false));
        this.setWidth(100, Unit.PERCENTAGE);
        this.setHeightUndefined();

        this.strTitle = strTitle;
        this.groupSelectionMap = new HashMap<>();
        this.selectionSet = new LinkedHashSet<>();

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidth(100, Unit.PERCENTAGE);
        headerLayout.setHeight(30, Unit.PIXELS);
        headerLayout.setStyleName("bottomborder");
        headerLayout.addStyleName("margin");
        headerLayout.addStyleName("paddingleft20");
        headerLayout.setMargin(new MarginInfo(false, true, false, false));
        
        
        this.addComponent(headerLayout);

        Label titileI = new Label(strTitle);
        titileI.setStyleName(ValoTheme.LABEL_SMALL);
        titileI.addStyleName(ValoTheme.LABEL_BOLD);
        headerLayout.addComponent(titileI);
        titileI.setWidth(100, Unit.PERCENTAGE);

        clearBtn = new Button("Clear");
        
        clearBtn.setHeight(100,Unit.PERCENTAGE);
         clearBtn.setStyleName(ValoTheme.BUTTON_LINK);
//         clearBtn.addStyleName(ValoTheme.BUTTON_TINY);
//        clearBtn.addStyleName(ValoTheme.BUTTON_SMALL);
          clearBtn.addStyleName("link");
          clearBtn.addStyleName("marginleft28");
       
//        clearBtn.setHeight(18, Unit.PIXELS);
        clearBtn.setEnabled(false);
        headerLayout.addComponent(clearBtn);
        headerLayout.setComponentAlignment(clearBtn, Alignment.TOP_RIGHT);
        clearBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                singleSelected = false;
                diseaseGroupSelectOption.setValue(null);

            }
        });

        bodyPanel = new Panel();
        maxHeight = height - 50;
        bodyPanel.setWidth(100, Unit.PERCENTAGE);
        bodyPanel.setStyleName(ValoTheme.PANEL_BORDERLESS);
//        bodyPanel.addStyleName("paddingleft20");
        bodyPanel.addStyleName("margin");

        HorizontalLayout bodyLayout = new HorizontalLayout();
        bodyLayout.setMargin(false);
        bodyLayout.setStyleName("paddingleft20");
        
        
        bodyLayout.setWidth(100, Unit.PERCENTAGE);
      
        bodyPanel.setContent(bodyLayout);
        this.addComponent(bodyPanel);
         this.setComponentAlignment(bodyPanel, Alignment.BOTTOM_CENTER);

        counterLayoutContainer = new VerticalLayout();
        bodyLayout.addComponent(counterLayoutContainer);

        counterLayoutContainer.setWidth(100, Unit.PERCENTAGE);
        bodyLayout.setExpandRatio(counterLayoutContainer, 10);
        counterLayout = new VerticalLayout();
        counterLayoutContainer.addComponent(counterLayout);
        counterLayoutContainer.setComponentAlignment(counterLayout, Alignment.TOP_RIGHT);
        counterLayout.setWidth(100, Unit.PERCENTAGE);
        counterLayout.setSpacing(false);
        

//        int sortableItremWidth =  containerWidth - 26 - 6 - 15;
        sortableDiseaseGroupLayout = new SortableLayout();
        bodyLayout.addComponent(sortableDiseaseGroupLayout);
        bodyLayout.setExpandRatio(sortableDiseaseGroupLayout, 80);
        sortableDiseaseGroupLayout.setWidth(100, Unit.PERCENTAGE);

        sortableDiseaseGroupLayout.setData(strTitle);
        sortableDiseaseGroupLayout.addStyleName("no-horizontal-drag-hints");

        checkboxLayout = new VerticalLayout();
        bodyLayout.addComponent(checkboxLayout);
        bodyLayout.setExpandRatio(checkboxLayout, 10);

        checkboxLayout.setSpacing(false);
        checkboxLayout.setEnabled(false);
        checkboxLayout.setMargin(false);

        diseaseGroupSelectOption = new OptionGroup();
       
        checkboxLayout.addComponent(diseaseGroupSelectOption);
        checkboxLayout.setComponentAlignment(diseaseGroupSelectOption, Alignment.TOP_LEFT);
        diseaseGroupSelectOption.setHeight(100, Unit.PERCENTAGE);
        diseaseGroupSelectOption.setNullSelectionAllowed(true); // user can not 'unselect'
        diseaseGroupSelectOption.setMultiSelect(true);
        diseaseGroupSelectOption.addStyleName("sortablelayoutselect");
        selectDataListener = (Property.ValueChangeEvent event) -> {
            if (autoClear) {
                autoClear = false;
                return;
            }
            groupSelectionMap.keySet().stream().forEach((key) -> {
                groupSelectionMap.put(key, Boolean.FALSE);
            });
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
        };
        diseaseGroupSelectOption.addValueChangeListener(selectDataListener);

//        itemWidth = sortableItremWidth - 10;
//        initLists(labels);
    }

    public void updateData(LinkedHashSet<HeatMapHeaderCellInformationBean> headers) {
         this.initalLabels=headers;
        diseaseGroupSelectOption.removeValueChangeListener(selectDataListener);
        if (externalListener != null) {
            diseaseGroupSelectOption.removeValueChangeListener(externalListener);
        }
        diseaseGroupSelectOption.removeAllItems();
        sortableDiseaseGroupLayout.removeAllComponents();
        counterLayout.removeAllComponents();
//        
        selectAllSet.clear();
        int counter = 0;
        labelsLayoutSet.clear();
        for (final VerticalLayout component : createComponents(headers)) {
            VerticalLayout container = new VerticalLayout();
            container.setWidth(100, Unit.PERCENTAGE);
            container.setStyleName("countItem");
            Label label = new Label(counter + 1 + "");
            container.addComponent(label);
            this.counterLayout.addComponent(container);
            sortableDiseaseGroupLayout.addComponent(component, strTitle);
            diseaseGroupSelectOption.addItem(counter);
            diseaseGroupSelectOption.setItemCaption(counter, (counter + 1) +"");
            autoClear = true;
            diseaseGroupSelectOption.select(counter);
            selectAllSet.add(counter);

            counter++;
        }
        counterLayout.setHeight((24*counterLayout.getComponentCount()),Unit.PIXELS);

        bodyPanel.setHeight(Math.min(maxHeight, 25 * (counter)), Unit.PIXELS);
        diseaseGroupSelectOption.setHeight(25 * (counter-1), Unit.PIXELS);
        autoClear = false;
        diseaseGroupSelectOption.addValueChangeListener(selectDataListener);
        if (externalListener != null) {
            diseaseGroupSelectOption.addValueChangeListener(externalListener);
        }

    }

    public boolean isSingleSelected() {
        return singleSelected;
    }

    private Property.ValueChangeListener externalListener;

    public void addSelectionValueChangeListener(Property.ValueChangeListener listener) {
        this.externalListener = listener;
        diseaseGroupSelectOption.addValueChangeListener(listener);

    }
    private final Map<HeatMapHeaderCellInformationBean, VerticalLayout> labelsLayoutSet = new HashMap<>();
    private LinkedHashSet<HeatMapHeaderCellInformationBean> initalLabels;
    public void updateLists(Set<HeatMapHeaderCellInformationBean> labels) {
       
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
    
    public void resetToDefault(){
        updateData(initalLabels);
    }

    public void selectAndHideUnselected(Set<HeatMapHeaderCellInformationBean> labels, boolean selectOnly) {

        autoClear = true;
        diseaseGroupSelectOption.setValue(null);

        Set updatedSelectionSet = new HashSet();
        if (labels == null || labels.isEmpty() || (labels.size() == groupsIds.size())) {
            updatedSelectionSet.addAll(selectAllSet);
            for (int i = 0; i < groupsIds.size(); i++) {
                labelsLayoutSet.get(groupsIds.get(i)).removeStyleName("disableLayout");
                diseaseGroupSelectOption.setItemEnabled(i, true);

            }

        } else {
            if (selectOnly) {

                for (int i = 0; i < groupsIds.size(); i++) {
                    if (labels.contains(groupsIds.get(i))) {
                        updatedSelectionSet.add(i);

                    }
                }

            } else {
                for (int i = 0; i < groupsIds.size(); i++) {
                    if (labels.contains(groupsIds.get(i))) {
                        updatedSelectionSet.add(i);
                        labelsLayoutSet.get(groupsIds.get(i)).removeStyleName("disableLayout");
                        diseaseGroupSelectOption.setItemEnabled(i, true);

                    } else {
                        labelsLayoutSet.get(groupsIds.get(i)).addStyleName("disableLayout");
                        diseaseGroupSelectOption.setItemEnabled(i, false);
                    }
                }
            }
        }
        diseaseGroupSelectOption.setValue(updatedSelectionSet);
    }

    public LinkedHashSet<HeatMapHeaderCellInformationBean> getSortedSet() {
        LinkedHashSet sortedSet;
        if (isSingleSelected()) {
            sortedSet = new LinkedHashSet(selectionSet);

        } else {

            sortedSet = new LinkedHashSet();
            groupsIds.stream().filter((groupsId) -> (!labelsLayoutSet.get(groupsId).getStyleName().trim().contains("disableLayout"))).forEach((groupsId) -> {
                sortedSet.add(groupsId);
            });//            sortedSet = new LinkedHashSet(groupsIds);
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
    private final LinkedList<HeatMapHeaderCellInformationBean> groupsIds = new LinkedList<>();

    private List<VerticalLayout> createComponents(Set<HeatMapHeaderCellInformationBean> datasource) {
        final List<VerticalLayout> componentsList = new ArrayList<>();
        groupsIds.clear();
        groupSelectionMap.clear();
        for (HeatMapHeaderCellInformationBean strLabel : datasource) {
            DiseaseGroupLabel container = new DiseaseGroupLabel(strLabel.getDiseaseGroupName(), strLabel.getDiseaseStyleName());
            container.addStyleName("border");
            container.addStyleName("grab");
            container.setDescription(strLabel.getDiseaseGroupFullName());
            componentsList.add(container);
            groupSelectionMap.put(strLabel, Boolean.TRUE);
            groupsIds.add(strLabel);
            labelsLayoutSet.put(strLabel, container);
        }
        return componentsList;
    }

    public final Set<HeatMapHeaderCellInformationBean> getSelectionSet() {
        return selectionSet;
    }

    class SortableLayout extends CustomComponent {

        private final AbstractOrderedLayout layout;
        private final DropHandler dropHandler;

        public SortableLayout() {
            layout = new VerticalLayout();
            layout.setWidth(100, Unit.PERCENTAGE);
            layout.setHeight(100, Unit.PERCENTAGE);
            dropHandler = new ReorderLayoutDropHandler(layout);
            this.setEnabled(true);

            final DragAndDropWrapper pane = new DragAndDropWrapper(layout);
            setCompositionRoot(pane);

        }

        public void addComponent(final Component component, String data) {
            final WrappedComponent wrapper = new WrappedComponent(component,
                    dropHandler);
//            wrapper.setSizeUndefined();
//            component.setHeight("100%");
            wrapper.setHeight(100, Unit.PERCENTAGE);
            wrapper.setWidth(100, Unit.PERCENTAGE);
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

                HeatMapHeaderCellInformationBean srcLabel = groupsIds.get(sourceIndex);
                groupsIds.remove(srcLabel);
                groupsIds.add(index, srcLabel);
                autoClear = true;
                diseaseGroupSelectOption.setValue(null);
                Set selectionSet = new HashSet();
                for (int i = 0; i < groupsIds.size(); i++) {
                    if (groupSelectionMap.get(groupsIds.get(i))) {
                        selectionSet.add(i);
                        diseaseGroupSelectOption.setItemEnabled(i, true);
                    } else {
                        diseaseGroupSelectOption.setItemEnabled(i, false);
                    }

                }
                diseaseGroupSelectOption.setValue(selectionSet);
                // move component within the layout
                layout.removeComponent(sourceComponent);
                layout.addComponent(sourceComponent, index);

            }
        }

    }

}
