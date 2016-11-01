package no.uib.probe.csf.pr.touch.view;

import com.vaadin.server.VaadinSession;
import java.util.LinkedHashMap;
import java.util.Map;
import no.uib.probe.csf.pr.touch.view.core.BusyTaskProgressBar;
import no.uib.probe.csf.pr.touch.view.core.ControllingView;

/**
 *
 * @author Yehia Farag
 *
 * This class represents the view controller the controller responsible for
 * handling the requested layout and view it
 */
public class LayoutViewManager {

    /*
     *Map of registered layout visulization
     */
    private final Map<String, ControllingView> layoutMap = new LinkedHashMap<>();
    /*
     *Current visualized layout
     */
    private ControllingView currentView;
    /*
     *System is doing long processing task to push the the system to show  progress bar
     */
    private final BusyTaskProgressBar busyTask;

    /**
     * Constructor to initialize the main attributes
     *
     * @param busyTask
     */
    public LayoutViewManager(BusyTaskProgressBar busyTask) {
        this.busyTask = busyTask;
    }

    /**
     * Register layout component to the system
     *
     * @param component
     */
    public void registerComponent(ControllingView component) {
        layoutMap.put(component.getViewId(), component);

    }

    /**
     * View selected layout based on user selection
     *
     * @param viewId
     */
    public void viewLayout(String viewId) {
        try {
            VaadinSession.getCurrent().getLockInstance().lock();

            this.busyTask.setVisible(true);
            if (currentView != null && !currentView.getViewId().equalsIgnoreCase(viewId) && layoutMap.containsKey(viewId)) {
                currentView.hide();

            }
            if (layoutMap.containsKey(viewId)) {
                currentView = layoutMap.get(viewId);
                currentView.view();
            }
            this.busyTask.setVisible(false);
        } finally {
            VaadinSession.getCurrent().getLockInstance().unlock();
        }

    }
}
