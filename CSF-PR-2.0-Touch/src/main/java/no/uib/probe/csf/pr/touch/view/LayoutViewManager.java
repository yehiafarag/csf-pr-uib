/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.view;

import com.vaadin.server.VaadinSession;
import java.util.LinkedHashMap;
import java.util.Map;
import no.uib.probe.csf.pr.touch.view.core.BusyTask;
import no.uib.probe.csf.pr.touch.view.core.Resizable;

/**
 *
 * @author Yehia Farag
 *
 * this class represents the view controller the controller responsible for the
 * select the requested layout and view it and component resize
 */
public class LayoutViewManager {

    private final Map<String, Resizable> layoutMap = new LinkedHashMap<>();
    private Resizable currentView;
    private final BusyTask busyTask;

    public LayoutViewManager(BusyTask busyTask) {
        this.busyTask=busyTask;
    }

    public void registerComponent(Resizable component) {
        layoutMap.put(component.getViewId(), component);

    }

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
